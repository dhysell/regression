package regression.r2.noclock.policycenter.submission_property.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.activity.ActivityPopup;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

public class TestSquirePropertyLiabilityFPPNonOwnedEquipmentRisk extends BaseTest {
    private GeneratePolicy myCountryPolicyObjPL = null;
    private GeneratePolicy myFRPolicyObjPL = null;
    private Underwriters uw;
    /**
     * @Author nvadlamudi
     * @Requirement : US9369: Add risk and Category to Non Owned Equipment
     * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Homeowners/PC8%20-%20HO%20-%20QuoteApplication%20-%20Risk%20and%20Category.xlsx">Link Text</a>
     * @Description
     * @DATE Sep 26, 2016
     * @throws Exception
     */
    private WebDriver driver;

    @Test()
    public void testFPPCountrySquirePol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));

        PolicyLocation resPrem = new PolicyLocation(locOnePropertyList);
        resPrem.setPlNumAcres(11);
        resPrem.setPlNumResidence(5);

        locationsList.add(resPrem);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquireFPP squireFPP = new SquireFPP();
        squireFPP.setOverrideEmptyItemsList(true);
        squireFPP.setNonOwnedEquipmentLimit(20000);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        myPropertyAndLiability.squireFPP = squireFPP;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myCountryPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Country", "FPPNONOwned")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicySubmitted);

        validateFPPNonOwnedRisk(myCountryPolicyObjPL);
    }


    @Test()
    public void testFPPFarmRanchSquirePol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));

        PolicyLocation resPrem = new PolicyLocation(locOnePropertyList);
        resPrem.setPlNumAcres(11);
        resPrem.setPlNumResidence(5);

        locationsList.add(resPrem);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquireFPP squireFPP = new SquireFPP();
        squireFPP.setOverrideEmptyItemsList(true);
        squireFPP.setNonOwnedEquipmentLimit(20000);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        myPropertyAndLiability.squireFPP = squireFPP;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myFRPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("FR", "FPPNONOwned")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicySubmitted);

        validateFPPNonOwnedRisk(myFRPolicyObjPL);
    }

    private void validateFPPNonOwnedRisk(GeneratePolicy pol) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), pol.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickIssuePolicy();
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

        risk.approveAll_IncludingSpecial();
        risk.Quote();
        sideMenu.clickSideMenuQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.issuePolicy(IssuanceType.NoActionRequired);
        boolean testFailed = false;
        String errorMessage = "";
        String valMsg = "Section I: Risk and Category must be entered on all properties and Farm and Personal Property (if added) to issue policy. (PR080)";
        if (!risk.getValidationMessagesText().contains(valMsg)) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Validation message : '" + valMsg + "' is not displayed.";
        }
        risk.clickClearButton();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickFarmPersonalProperty();
        coverages.setFarmPersonalPropertyRisk("A");
        quote.clickSaveDraftButton();
        quote.clickQuote();

        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchAccountByAccountNumber(pol.agentInfo.agentUserName, pol.agentInfo.getAgentPassword(), pol.accountNumber);
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);

        try {
            accountSummaryPage.clickActivitySubject("Submitted Full Application");
        } catch (Exception e) {
        }
        ActivityPopup activityPopupPage = new ActivityPopup(driver);
        try {
            activityPopupPage.setUWIssuanceActivity();
        } catch (Exception e) {
            activityPopupPage.clickCloseWorkSheet();
        }
        SideMenuPC menu = new SideMenuPC(driver);
        menu.clickSideMenuPolicyInfo();
        menu.clickSideMenuSquirePropertyCoverages();
        coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickFarmPersonalProperty();
        if (!coverages.getFarmPersonalPropertyRisk().contains("A")) {
            testFailed = true;
            errorMessage = errorMessage + "Expected FPP - Non Owned Equipment - Risk : A is not displayed \n";
        }


        if (testFailed)
            Assert.fail(errorMessage);
    }
}
