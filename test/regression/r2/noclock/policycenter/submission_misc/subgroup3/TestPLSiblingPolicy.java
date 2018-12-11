package regression.r2.noclock.policycenter.submission_misc.subgroup3;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderModifiers;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US7820 : PL - Sibling Policy
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/PersonalAuto/PC8%20-%20PA%20-%20QuoteApplication%20-%20Sibling%20Policy.xlsx">Link Text</a>
 * @Description
 * @DATE Oct 14, 2016
 */
@QuarantineClass
public class TestPLSiblingPolicy extends BaseTest {
    private GeneratePolicy mySquirePolicy;
    private GeneratePolicy mySiblingPolicy;
    private int squirePolInitialDiscount;
    private Underwriters genericPLUnderwriter;

    private WebDriver driver;

    @Test()
    private void testIssuedSquirePol() throws Exception {
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

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySquirePolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Test", "SqForSib")
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchPolicyByAccountNumber(this.mySquirePolicy.agentInfo.agentUserName, this.mySquirePolicy.agentInfo.agentPassword, this.mySquirePolicy.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuModifiers();

        GenericWorkorderModifiers modifier = new GenericWorkorderModifiers(driver);
        squirePolInitialDiscount = modifier.getTotalPolicyDiscount();
        System.out.println("Initial Sq Pol Discount: " + squirePolInitialDiscount);
    }

    @Test(dependsOnMethods = {"testIssuedSquirePol"})
    private void testFullAppSiblingPol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL75K, MedicalLimit.TenK, true, UninsuredLimit.CSL75K, false, UnderinsuredLimit.CSL75K);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySiblingPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withSiblingPolicy(mySquirePolicy, "Test", "Sibling")
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testFullAppSiblingPol"})
    private void testValidateSiblingSubmission() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        genericPLUnderwriter = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(genericPLUnderwriter.getUnderwriterUserName(), genericPLUnderwriter.getUnderwriterPassword(), mySiblingPolicy.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickEditPolicyTransaction();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        String errorMessage = "";
        sideMenu.clickSideMenuPolicyInfo();
        sideMenu.clickSideMenuPAModifiers();
        GenericWorkorderModifiers modifier = new GenericWorkorderModifiers(driver);
        System.out.println(modifier.getSquirePolicyModifierDiscountSurchargeByRow(1));
        if (this.squirePolInitialDiscount < 16 && !modifier.getSquirePolicyModifierDiscountSurchargeByRow(1).contains(this.squirePolInitialDiscount + "")) {
            errorMessage = "Sibling Policy Discount/surcharge not shown in modifers /n";
        }

        sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickSectionIICoveragesTab();
        //selecting all Section II - General Liability Limits to see those values are available
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        section2Covs.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_25_50_15);
        section2Covs.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_25_50_25);

        section2Covs.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);
        section2Covs.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_50);
        section2Covs.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);
        section2Covs.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_100_300_50);
        section2Covs.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_100_300_100);
        section2Covs.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        //checking Squire Auto - Coverages for sibling policy
        LiabilityLimit limit = (guidewireHelpers.getRandBoolean()) ? LiabilityLimit.CSL300K : ((guidewireHelpers.getRandBoolean()) ? LiabilityLimit.OneHundredLow : LiabilityLimit.OneHundredHigh);

        GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
        coveragePage.setLiabilityCoverage(limit);
        sideMenu.clickSideMenuPACoverages();
        coveragePage.setUninsuredCoverage(true, UninsuredLimit.CSL300K);
        sideMenu.clickSideMenuPADrivers();
        paDrivers.clickEditButtonInDriverTableByDriverName(mySiblingPolicy.pniContact.getFirstName());
        paDrivers.selectMaritalStatus(MaritalStatus.Married);
        paDrivers.clickOk();

        sideMenu.clickSideMenuRiskAnalysis();
        risk.Quote();
        sideMenu.clickSideMenuRiskAnalysis();
        String[] uwIssue = {"Sibling ANI", "Sibling and Not Single", "Sibling Policy Liability Limits"};


        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

        for (String uwCurrentIssue : uwIssue) {
            if (uwIssues.isInList(uwCurrentIssue).equals(UnderwriterIssueType.NONE)) {
                errorMessage = errorMessage + "Expected error Bind Issue : " + uwCurrentIssue + " is not displayed.";
            }
        }

        policyInfo.clickEditPolicyTransaction();

        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles vehicleDetails = new GenericWorkorderVehicles(driver);
        vehicleDetails.setCheckBoxInVehicleTable(1);
        vehicleDetails.clickRemoveVehicle();

        sideMenu.clickSideMenuPADrivers();
        paDrivers.setCheckBoxInDriverTable(1);
        paDrivers.clickRemoveButton();

        sideMenu.clickSideMenuLineSelection();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
        lineSelection.checkPersonalAutoLine(false);

        sideMenu.clickSideMenuRiskAnalysis();
        risk.Quote();
        String validationMessages = risk.getValidationMessagesText();
        String valMeg1 = "Policy: To write a sibling policy Section III is required. (SQ052)";
        if (!validationMessages.contains(valMeg1)) {
            errorMessage = errorMessage + "Expected Validation message : '" + valMeg1 + "' is not displayed.";
        }
        risk.clickClearButton();

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }


    @Test(dependsOnMethods = {"testValidateSiblingSubmission"})
    private void testValidateModifiersSiblingDiscountWithAgent() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(mySiblingPolicy.agentInfo.agentUserName, mySiblingPolicy.agentInfo.agentPassword, mySiblingPolicy.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPAModifiers();
        String errorMessage = "";
        GenericWorkorderModifiers modifier = new GenericWorkorderModifiers(driver);
        System.out.println(modifier.getSquirePolicyModifierDiscountSurchargeByRow(1));
        if (this.squirePolInitialDiscount < 16 && !modifier.getSquirePolicyModifierDiscountSurchargeByRow(1).contains(this.squirePolInitialDiscount + "")) {
            errorMessage = "Sibling Policy Discount/surcharge not shown in modifers /n";
        }
        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }
}
