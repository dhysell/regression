package regression.r2.noclock.policycenter.renewaltransition.subgroup3;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Measurement;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US7930: Section I/Standard Fire Renewal Business rules
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Business%20Rules%20Personal%20Lines/New_Business_Rules.xlsx">LINK HERE</a>
 * @Description
 * @DATE Jun 30, 2016
 */
public class TestStandardFireRenewalBusinessRules extends BaseTest {
    private GeneratePolicy myStandardFirePol;
    private Underwriters uw;

    private WebDriver driver;

    @Test()
    private void testIssueStandardFirePolicy() throws Exception {
        Agents agent = AgentsHelper.getRandomAgent();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
        propLoc.setPlNumAcres(10);
        propLoc.setPlNumResidence(6);
        locationsList.add(propLoc);

        myStandardFirePol = new GeneratePolicy.Builder(driver).withAgent(agent).withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL).withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person).withInsFirstLastName("Renew", "Rules").withInsAge(26)
                .withPolTermLengthDays(79).withPolOrgType(OrganizationType.Individual)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueStandardFirePolicy"})
    public void testRenewValidateBusinessRules() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myStandardFirePol.accountNumber);

        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();

        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarPolicyNumber();

        renewal.waitForPreRenewalDirections();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(myStandardFirePol);

        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickPendingTransaction(TransactionType.Renewal);

        new GuidewireHelpers(driver).editPolicyTransaction();
        // Screen Validations
        testScreenValidation();

        // Block Issues
        testStandardFireBlockIssueBind();

        // Quote Errors
        testStandardFireQuoteError();
    }

    private void testScreenValidation() throws Exception {
        boolean testFailed = false;
        String errorMessage = "";

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propDet = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propDet.clickViewOrEditBuildingButton(1);

        // Screen Validation: PR079 - This building does not qualify for
        // Coverage A
        propDet.setDwellingVacantRadio(true);
        propDet.sendArbitraryKeys(Keys.TAB);
        propDet.setUnits(NumberOfUnits.TwoUnits);
        if (!propDet.getPropertyValidationMessage().contains(
                "This building does not qualify for Coverage A. Please change the property type to a Coverage E Premises")) {
            testFailed = true;
            errorMessage = "Expected validation: 'Dwelling Vacant: This building does not qualify for Coverage A' is not displayed. \n";
        }
        propDet.setDwellingVacantRadio(false);
        propDet.sendArbitraryKeys(Keys.TAB);
        propDet.clickPropertyConstructionTab();

        // PR030: If Property Type = (Residence Premises, Dwelling Premises, or
        // Vacation Home) Construction Type = (Mobile Home or
        // Modular/Manufactured); then Sq. ft. >= 840
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
        constructionPage.setSquareFootage(399);
        propDet.sendArbitraryKeys(Keys.TAB);
        // PR043 - Modular/Manufactured Dwelling Premises cannot be older than
        // 1985.
        constructionPage.setYearBuilt(1984);
        constructionPage.clickOK();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        String modularMessage = "For Modular/Manufactured Dwelling Premises to qualify for Coverage A, the square feet must be greater or equal to 400. If not then please move the property to Coverage E";

        String validationMessages = risk.getValidationMessagesText();
        if (!validationMessages.contains(modularMessage)) {
            testFailed = true;
            errorMessage = "Expected Quote validation message : " + modularMessage + " is not displayed.\n";
        }
        String modularYearBuilt = "Modular/Manufactured Dwelling Premises cannot be older than 1985";
        if (!validationMessages.contains(modularYearBuilt)) {
            testFailed = true;
            errorMessage = "Expected Quote validation message : " + modularYearBuilt + " is not displayed.\n";
        }

        risk.clickClearButton();
        constructionPage.setSquareFootage(3000);
        propDet.sendArbitraryKeys(Keys.TAB);
        constructionPage.setYearBuilt(2013);
        constructionPage.setConstructionType(ConstructionTypePL.NonFrame);
        constructionPage.clickOK();
        // Contents with actual cash value must have limit greater or equal to
        // $7,500 . (PR081)
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        covs.clickSpecificBuilding(1, 1);
        covs.setCoverageCLimit(7499);
        covs.clickNext();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        String valMessage = "Limit : Contents with actual cash value must have limit greater or equal to $7,500";
        if (!guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains(valMessage))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected page validation : '" + valMessage + "' is not displayed. /n";
        }

        // Contents with replacement cost must have limit greater or equal to
        // $15,000 . (PR082)
        covs.setCoverageCLimit(14999);
        covs.selectCoverageCCoverageType(CoverageType.BroadForm);
        covs.clickNext();
        String valMessage2 = "Limit : Contents with replacement cost must have limit greater or equal to $15,000";
        if (!guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains(valMessage2))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected page validation : '" + valMessage2 + "' is not displayed. /n";
        }
        sideMenu.clickSideMenuSquirePropertyCoverages();
        covs.setCoverageCLimit(34999);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

        // Contents with actual cash value must have limit greater or equal to
        // $7,500 . (PR015)
        int buildingNumber = addNewStandardFireProperty(PropertyTypePL.DwellingPremisesCovE, NumberOfUnits.OneUnit,
                70000, ConstructionTypePL.Frame);
        covs.clickSpecificBuilding(1, buildingNumber);
        covs.checkOptionalCoverageC(true);
        covs.setCoverageCLimit(7000);
        quote.clickQuote();
        String valMessage3 = "Contents with actual cash value must have limit greater or equal to $7,500";
        if (!guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains(valMessage3))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected page validation : '" + valMessage3 + "' is not displayed. /n";
        }
        covs.checkOptionalCoverageC(false);

        // Modular/Manufactured Vacation Home cannot be older than 1984.(PR036)
        buildingNumber = addNewStandardFireProperty(PropertyTypePL.VacationHome, NumberOfUnits.OneUnit, 70000,
                ConstructionTypePL.Frame);
        sideMenu.clickSideMenuSquirePropertyDetail();
        propDet.clickViewOrEditBuildingButton(buildingNumber);
        propDet.clickPropertyConstructionTab();
        constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
        constructionPage.setYearBuilt(1984);
        constructionPage.clickOK();

        String vacationHomeModularMessage = "Modular/Manufactured Vacation Home cannot be older than 1984";
        validationMessages = risk.getValidationMessagesText();
        if (!validationMessages.contains(vacationHomeModularMessage)) {
            testFailed = true;
            errorMessage = "Expected Quote validation message : " + vacationHomeModularMessage + " is not displayed.\n";
        }
        risk.clickClearButton();
        constructionPage.setYearBuilt(2013);
        constructionPage.setConstructionType(ConstructionTypePL.NonFrame);
        constructionPage.clickOK();

        // Any location with property on it must have at least 1 acre. (PR050)
        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation plProperty = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        plProperty.clickEditLocation(1);
//        plProperty.setAcres(0);
        plProperty.clickOK();
        String valMessage4 = "Acres : Must enter at least 1 acre";
        if (!guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains(valMessage4))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected page validation : '" + valMessage4 + "' is not displayed. /n";
        }
//        plProperty.setAcres(10);
//        plProperty.setNumberOfResidence(8);
        plProperty.clickOK();

        quote.clickSaveDraftButton();

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }
    }

    private void testStandardFireQuoteError() throws Exception {
        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation plProperty = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        plProperty.clickEditLocation(1);
//        plProperty.setNumberOfResidence(0);
        plProperty.clickOK();
        String valMsg1 = "Please correct the number of residence for this location";

        int buildingNumber = addNewStandardFireProperty(PropertyTypePL.CondominiumVacationHome, NumberOfUnits.TwoUnits,
                39999, ConstructionTypePL.Frame);
        String valMsg2 = "Limit on Property #" + buildingNumber + " on location #1 must be at least $40,000.";

        buildingNumber = addNewStandardFireProperty(PropertyTypePL.VacationHomeCovE, NumberOfUnits.OneUnit, 14999,
                ConstructionTypePL.Frame);
        String valMsg3 = "Limit on Property #" + buildingNumber + " on location #1 must be at least $15,000.";

        sideMenu.clickSideMenuRiskAnalysis();
        risk.Quote();

        String validationMessages = risk.getValidationMessagesText();
        if (!validationMessages.contains(valMsg1)) {
            testFailed = true;
            errorMessage = "Expected Quote validation message : " + valMsg1 + " is not displayed.\n";
        }
        if (!validationMessages.contains(valMsg2)) {
            testFailed = true;
            errorMessage = "Expected Quote validation message : " + valMsg2 + " is not displayed.\n";
        }
        if (!validationMessages.contains(valMsg3)) {
            testFailed = true;
            errorMessage = "Expected Quote validation message : " + valMsg3 + " is not displayed.\n";
        }

        risk.clickClearButton();
        sideMenu.clickSideMenuPropertyLocations();
        plProperty.clickEditLocation(1);
//        plProperty.setNumberOfResidence(12);
        plProperty.clickOK();
        setPropertyCoveragesCoverageLimit(PropertyTypePL.VacationHomeCovE, 20000, buildingNumber);
        setPropertyCoveragesCoverageLimit(PropertyTypePL.CondominiumVacationHome, 50000, buildingNumber - 1);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        quote.clickQuote();

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }
    }

    private int addNewStandardFireProperty(PropertyTypePL type, NumberOfUnits units, double limit,
                                           ConstructionTypePL constructionType) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propDet = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        PLPolicyLocationProperty property = new PLPolicyLocationProperty(type);
        property.setConstructionType(constructionType);
        property.setNumberOfUnits(units);

        propDet.fillOutPropertyDetails_FA(property);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.fillOutPropertyConstrustion_FA(property);
        if (type.equals(PropertyTypePL.VacationHomeCovE)) {
            constructionPage.setMeasurement(Measurement.SQFT);
        }
        constructionPage.clickOK();
        int BuildingNumber = propDet.getSelectedBuildingNum();
        setPropertyCoveragesCoverageLimit(type, limit, BuildingNumber);
        return BuildingNumber;
    }

    private void setPropertyCoveragesCoverageLimit(PropertyTypePL type, double limit, int BuildingNumber) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, BuildingNumber);
        PLPolicyLocationProperty property = new PLPolicyLocationProperty(type);

        if (type.equals(PropertyTypePL.VacationHomeCovE) || type.equals(PropertyTypePL.DwellingPremisesCovE)) {
            coverages.setCoverageELimit(limit);
            coverages.setCoverageECoverageType(CoverageType.Peril_1);
            coverages.setCoverageCLimit(16000);

            if (type.equals(PropertyTypePL.VacationHomeCovE)) {
                coverages.setCoverageCValuation(this.myStandardFirePol.standardFire.getLocationList().get(0).getPropertyList().get(0).getPropertyCoverages());
            }
        } else {
            coverages.setCoverageALimit(limit);
            coverages.setCoverageCLimit(limit);
            try {
                coverages.setCoverageAIncreasedReplacementCost(property.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
            } catch (Exception e) {
            }
            coverages.setCoverageCValuation(property.getPropertyCoverages());
        }
    }

    private void testStandardFireBlockIssueBind() throws Exception {

        // Adding Section I deductible is set at <<Deductible here>>.
        // Underwriting approval required to bind. (PR001)
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        covs.selectSectionIDeductible(SectionIDeductible.TenThousand);

        // Add property over $1,500,000
        covs.setCoverageALimit(2000000);

        // more than 4 rental units and Property value greater than $750,000
        addNewStandardFireProperty(PropertyTypePL.DwellingPremises, NumberOfUnits.FourUnits, 800000,
                ConstructionTypePL.Frame);

        // property built before 1954 and vacant property
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.setYearBuilt(1953);
        constructionPage.clickOK();

        // Quote
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        quote.clickQuote();
        validateRiskAnalysisApprovals();

    }

    private void validateRiskAnalysisApprovals() {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);

        String[] expectedUWMessages = {"Only 4 Rental Units allowed", "More than 1 rental unit per property",
                "High property limit", "Section I deductible $10,000 or higher",
                "Cov A building built earlier than 1954"};

        for (String uwIssue : expectedUWMessages) {
            boolean messageFound = false;
            for (int i = 0; i < riskAnalysis.getUWIssuesList().size(); i++) {
                String currentUWIssueText = riskAnalysis.getUWIssuesList().get(i).getText();
                if (!currentUWIssueText.contains("Blocking Bind")
                        && (!currentUWIssueText.contains("Blocking Issuance"))) {
                    if (currentUWIssueText.contains(uwIssue)) {
                        messageFound = true;
                        break;
                    }
                }
            }
            if (!messageFound) {
                testFailed = true;
                errorMessage = errorMessage + "Expected error Bind Issue : " + uwIssue + " is not displayed.\n";
            }
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }
    }

}
