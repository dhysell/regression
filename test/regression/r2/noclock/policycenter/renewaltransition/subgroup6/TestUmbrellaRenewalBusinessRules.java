package regression.r2.noclock.policycenter.renewaltransition.subgroup6;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SRPIncident;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents;
import repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US7932 -  PL - Section IV/IM/Umbrella - Renewal Business Rules
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Business%20Rules%20Personal%20Lines/New_Business_Rules.xlsx">LINK HERE</a>
 * @Description
 * @DATE Jul 15, 2016
 */
@QuarantineClass
public class TestUmbrellaRenewalBusinessRules extends BaseTest {
    private GeneratePolicy mySquireUmbrellaPol;
    private Underwriters uw;
    private WebDriver driver;


    @Test(dependsOnMethods = {"testRenewSquirePolicy"})
    private void testRenewalUmbrellaBusinessRules() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySquireUmbrellaPol.squireUmbrellaInfo.getPolicyNumber());

        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();
        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarPolicyNumber();

        renewal.waitForPreRenewalDirections();
        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(mySquireUmbrellaPol);
        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickPendingTransaction(TransactionType.Renewal);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquireUmbrellaCoverages();
        GenericWorkorderSquireUmbrellaCoverages umbCovs = new GenericWorkorderSquireUmbrellaCoverages(driver);
        umbCovs.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_5000000);

        String[] excludedUWIssues = {"Limit over $2 million", "SRP"};
        validateRiskAnalysisApprovals(excludedUWIssues);
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);


        GenericWorkorder genericWO = new GenericWorkorder(driver);
        genericWO.clickGenericWorkorderSubmitOptionsRenew();

        String validationMessages = risk.getValidationMessagesText();
        boolean testFailed = false;
        String errorMessage = "";
        String sqRenewalMsg = "You are required to bind underlying Squire policy renewal in order to bind Umbrella policy renewal";
        if (!validationMessages.contains(sqRenewalMsg)) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Quote validation message : " + sqRenewalMsg + " is not displayed.\n";
        }

        risk.clickClearButton();

        if (testFailed)
            Assert.fail(errorMessage);

    }


    @Test(dependsOnMethods = {"testIssueUmbrellaPol"})
    private void testRenewSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySquireUmbrellaPol.squireUmbrellaInfo.getPolicyNumber());

        boolean testFailed = false;
        String errorMessage = "";

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (!guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains("Unable to find a Squire policy period effective on"))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected page validation : 'The vehicle must either have a driver assigned or have unassigned driver checked' is not displayed. /n";
        }

        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByPolicyNumber(mySquireUmbrellaPol.squire.getPolicyNumber());
        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();
        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarPolicyNumber();

        renewal.waitForPreRenewalDirections();
        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(mySquireUmbrellaPol);
        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickPendingTransaction(TransactionType.Renewal);
        guidewireHelpers.editPolicyTransaction();

        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        quotePage.clickQuote();
        if (quotePage.isPreQuoteDisplayed()) {
            quotePage.clickPreQuoteDetails();
        }
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.approveAll_IncludingSpecial();

        if (testFailed)
            Assert.fail(errorMessage);
    }


    @Test()
    public void testIssueUmbrellaPol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK, true,
                UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySquireUmbrellaPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withPolTermLengthDays(79)
                .withInsFirstLastName("SUSAN", "REDWINE")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.mySquireUmbrellaPol.accountNumber);
        guidewireHelpers.editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();

        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents squireDriver = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
        squireDriver.clickEditButtonInDriverTable(1);
        squireDriver.clickSRPIncidentsTab();
        squireDriver.setSRPIncident(SRPIncident.UnverifiableDrivingRecord, 2);
        squireDriver.setSRPIncident(SRPIncident.International, 1);
        squireDriver.setSRPIncident(SRPIncident.NoProof, 1);
        squireDriver.clickOk();


        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk.performRiskAnalysisAndQuote(mySquireUmbrellaPol);

        risk.handleBlockSubmit(mySquireUmbrellaPol);

        guidewireHelpers.logout();


        guidewireHelpers.setPolicyType(mySquireUmbrellaPol, GeneratePolicyType.FullApp);
        mySquireUmbrellaPol.convertTo(driver, GeneratePolicyType.PolicyIssued);

        guidewireHelpers.logout();

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_2000000);

        mySquireUmbrellaPol.squireUmbrellaInfo = squireUmbrellaInfo;
        mySquireUmbrellaPol.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);

    }

    private void validateRiskAnalysisApprovals(String[] expectedBlockIssues) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
        riskAnalysis.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            riskAnalysis.approveAll_IncludingSpecial();
            riskAnalysis.Quote();
        }
        sideMenu.clickSideMenuRiskAnalysis();

        FullUnderWriterIssues uwIssues = riskAnalysis.getUnderwriterIssues();

        for (String uwIssue : expectedBlockIssues) {
            if (uwIssues.isInList(uwIssue).equals(UnderwriterIssueType.NONE)) {
                testFailed = true;
                errorMessage = errorMessage + "Expected error Bind Issue : " + uwIssue + " is not displayed.";
            }
        }

        if (testFailed)
            Assert.fail(errorMessage);

    }

}
