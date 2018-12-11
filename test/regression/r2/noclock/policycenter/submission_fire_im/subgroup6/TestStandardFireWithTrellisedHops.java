package regression.r2.noclock.policycenter.submission_fire_im.subgroup6;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InsuranceScore;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * This test will pass and need to modify once all the existing issues are fixed.
 *
 * @Author nvadlamudi
 * @Requirement :US8067: Standard Fire - Trellised Hops Only
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Jul 28, 2016
 */

/**
 * @Author nvadlamudi
 * @Requirement :US8067: Standard Fire - Trellised Hops Only
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Jul 28, 2016
 */
public class TestStandardFireWithTrellisedHops extends BaseTest {
    private GeneratePolicy stdFireLiab_Fire_PolicyObj;
    private Underwriters uw;

    private WebDriver driver;

//    @Test(dependsOnMethods = {"testValidateTrellisedHopsProperty"})
    private void testValidateNonCommodityProperty() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList1 = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.SolarPanels);
        locOnePropertyList1.add(property2);
        PolicyLocation locToAdd1 = new PolicyLocation(locOnePropertyList1);

        locToAdd1.setPlNumAcres(11);
        locToAdd1.setAddress(new AddressInfo());
        locationsList1.add(locToAdd1);

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setStdFireCommodity(false);
        myStandardFire.setLocationList(locationsList1);


        new GuidewireHelpers(driver).logout();
        stdFireLiab_Fire_PolicyObj.standardFire = myStandardFire;
        stdFireLiab_Fire_PolicyObj.pniContact.setInsuranceScore(InsuranceScore.NeedsOrdered);
        stdFireLiab_Fire_PolicyObj.lineSelection.add(LineSelection.StandardFirePL);
        stdFireLiab_Fire_PolicyObj.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.FullApp);


        //agent will never see photo year, so added UW to handle this.
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFireLiab_Fire_PolicyObj.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE);

        propertyDetails.fillOutPropertyDetails_FA(property);

        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        constructionPage.setCoverageAPropertyDetailsFA(property);
        int yearField = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy");
        int monthField = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "MM");
        //agent will never see photo year, so added UW to handle this.
        constructionPage.setPhotoYear(monthField + "/" + yearField);
        protectionPage.clickOK();
        int buildingNumber = propertyDetails.getSelectedBuildingNum();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, buildingNumber);
        coverages.setCoverageELimit(200000);
        coverages.setCoverageECoverageType(CoverageType.Peril_1);
        coverages.setCoverageCLimit(200000);
        coverages.setCoverageCValuation(property.getPropertyCoverages());

        //Quote and validate UW Issue
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        quote.clickQuote();

        String[] expectedUWMessages = {"Missing MS year", "Missing Photo year"};
        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        for (String uwIssue : expectedUWMessages) {
            boolean found = false;
            for (int i = 0; i < riskAnalysis.getUWIssuesList().size(); i++) {
                String currentUWIssueText = riskAnalysis.getUWIssuesList().get(i).getText();
                if (!currentUWIssueText.contains("Blocking Bind") && (!currentUWIssueText.contains("Blocking Issuance"))) {
                    if (currentUWIssueText.contains(uwIssue)) {
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                testFailed = true;
                errorMessage = errorMessage + "Unexpected error Bind Issue : " + uwIssue + " is displayed.\n";
            }
        }

        if (testFailed && errorMessage != "")
            Assert.fail(errorMessage);
    }

//    @Test(dependsOnMethods = {"testCreateStandardFireWithSquire"})
    private void testValidateTrellisedHopsProperty() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(stdFireLiab_Fire_PolicyObj.agentInfo.getAgentUserName(), stdFireLiab_Fire_PolicyObj.agentInfo.getAgentPassword(), stdFireLiab_Fire_PolicyObj.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propDet = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        boolean testFailed = false;
        String errorMessage = "";

        try {
            propDet.clickAdd();
            propDet.clickCancel();
            testFailed = true;
            errorMessage = errorMessage + "Add button is enabled for property : Trellised Hops \n";
        } catch (Exception e) {
            testFailed = false;
        }

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);

        if (coverages.isSectionIDeductibleEditable()) {
            testFailed = true;
            errorMessage = errorMessage + "Deductible button is enabled for property : Trellised Hops \n";
        }

        if (coverages.isCoverageECoverageTypeEditable()) {
            testFailed = true;
            errorMessage = errorMessage + "Coverage E Coverage Type is editable for property : Trellised Hops \n";
        }

        //Adding second location and checking whether
        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickNewLocation();
        propertyLocations.addLocationInfoFA(new AddressInfo(), 2, 2);
        propertyLocations.clickStandardizeAddress();
        try {
            propertyLocations.clickLocationInformationOverride();
        } catch (Exception e) {
        }
        propertyLocations.clickOK();
        sideMenu.clickSideMenuSquirePropertyDetail();
        propDet.highLightPropertyLocationByNumber(2);
        try {
            propDet.clickAdd();
            propDet.clickCancel();
            testFailed = true;
            errorMessage = errorMessage + "Add button is enabled for property : Trellised Hops \n";
        } catch (Exception e) {
            testFailed = false;
        }

        //Adding lien Holder
        sideMenu.clickSideMenuSquirePropertyDetail();
        propDet.clickViewOrEditBuildingButton(1);
        AddressInfo bankAddress = new AddressInfo();

        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest("Test" + StringsUtils.generateRandomNumberDigits(4), "LessorPerson", bankAddress);
        loc2Bldg1AddInterest.setNewContact(CreateNew.Create_New_Always);
        loc2Bldg1AddInterest.setAddress(bankAddress);
        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);

        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
        SearchAddressBookPC search = new SearchAddressBookPC(driver);
        search.searchForContact(stdFireLiab_Fire_PolicyObj.basicSearch, loc2Bldg1AddInterest);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
        additionalInterests.setAddressListing("New...");
        additionalInterests.setContactEditAddressLine1(loc2Bldg1AddInterest.getAddress().getLine1());
        additionalInterests.setContactEditAddressCity(loc2Bldg1AddInterest.getAddress().getCity());
        additionalInterests.setContactEditAddressState(loc2Bldg1AddInterest.getAddress().getState());
        additionalInterests.setContactEditAddressZipCode(loc2Bldg1AddInterest.getAddress().getZip());
        //additionalInterests.setAddressType(loc2Bldg1AddInterest.getAddress().getType());
        additionalInterests.setContactEditAddressAddressType(loc2Bldg1AddInterest.getAddress().getType());

        //first mortgage checkbox is mandate to select payment assignment screen
        additionalInterests.checkBuildingsPropertyAdditionalInterestsFirstMortgage(true);
        additionalInterests.clickRelatedContactsTab();
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
        propDet.clickOk();
        sideMenu.clickSideMenuPayerAssignment();
        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);

        if (!payerAssignment.getPayerAssignmentPayerDetailsByLocationAndBuildingNumber(1, 1).contains("Insured")) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Insured payer is not displayed in Payer Assignment \n";
        }

        //Quote and validate UW Issue
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        quote.clickQuote();

        String expectedUWMessages = "Trellised Hops Exists";
        boolean messageFound = false;
        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        for (int i = 0; i < riskAnalysis.getUWIssuesList().size(); i++) {
            String currentUWIssueText = riskAnalysis.getUWIssuesList().get(i).getText();
            if (!currentUWIssueText.contains("Blocking Bind") && (!currentUWIssueText.contains("Blocking Issuance"))) {
                if (currentUWIssueText.contains(expectedUWMessages)) {
                    messageFound = true;
                    break;
                }
            }
        }
        if (!messageFound) {
            testFailed = true;
            errorMessage = errorMessage + "Expected error Bind Issue : " + expectedUWMessages + " is not displayed.\n";
        }

        if (testFailed && errorMessage != "")
            Assert.fail(errorMessage);
    }
}
