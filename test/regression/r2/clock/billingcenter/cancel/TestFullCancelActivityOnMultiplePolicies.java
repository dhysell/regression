package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.policy.summary.PolicySummaryInvoicingOverrides;
import repository.bc.topmenu.BCTopMenuPolicy;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InsuranceScore;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US10344: [part II] Trigger Activity for Full Cancels w/
 * Multiple Policies
 * @RequirementsLink <a href=
 * "https://rally1.rallydev.com/#/33274298124d/detail/userstory/88103407708"
 * </a>
 * @Description
 * @DATE Mar 7, 2017
 */
public class TestFullCancelActivityOnMultiplePolicies extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy mySqPolicy;
    private ARUsers arUser;

    @Test
    public void testIssueSquirePolicy() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
        SquirePropertyAndLiability property = new SquirePropertyAndLiability();


        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);
        property.locationList = locationsList;

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
        property.liabilitySection = myLiab;

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);


        Squire mySquire = new Squire();
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = property;
        mySquire.squireEligibility = SquireEligibility.City;
        mySquire.setTypeToGenerate(GeneratePolicyType.PolicyIssued);

        mySqPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("FullCancel", "Activity")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(mySquire.getTypeToGenerate());

    }

    @Test(dependsOnMethods = {"testIssueSquirePolicy"})
    private void testIssueStandardIMPol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
        inlandMarineCoverageSelection_PL_IM.add(InlandMarine.FarmEquipment);
        inlandMarineCoverageSelection_PL_IM.add(InlandMarine.PersonalProperty);

        // FPP
        // Scheduled Item for 1st FPP
        IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 1000);
        IMFarmEquipmentScheduledItem scheduledItem2 = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip1 = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip1.add(scheduledItem1);
        farmEquip1.add(scheduledItem2);
        FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm,
                IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip1);

        ArrayList<FarmEquipment> allFarmEquip1 = new ArrayList<FarmEquipment>();
        allFarmEquip1.add(imFarmEquip1);

        // PersonalProperty
        PersonalProperty ppropTool = new PersonalProperty();
        ppropTool.setType(PersonalPropertyType.Tools);
        ppropTool.setDeductible(PersonalPropertyDeductible.Ded25);
        PersonalPropertyScheduledItem tool = new PersonalPropertyScheduledItem();
        tool.setDescription("Big Tool");
        tool.setLimit(5000);
        ArrayList<PersonalPropertyScheduledItem> tools = new ArrayList<PersonalPropertyScheduledItem>();
        tools.add(tool);
        ppropTool.setScheduledItems(tools);
        ArrayList<String> ppropAdditIns1 = new ArrayList<String>();
        ppropAdditIns1.add("First Guy");
        ppropTool.setAdditionalInsureds(ppropAdditIns1);

        PersonalPropertyList ppropList1 = new PersonalPropertyList();
        ppropList1.setTools(ppropTool);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
        myStandardInlandMarine.farmEquipment = allFarmEquip1;
        myStandardInlandMarine.personalProperty_PL_IM = ppropList1.getAllPersonalPropertyAsList();

        mySqPolicy.standardInlandMarine = myStandardInlandMarine;
        mySqPolicy.addLineOfBusiness(ProductLineType.StandardIM, GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueStandardIMPol"})
    private void testIssueStdFirePol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        ArrayList<LineSelection> productLines = new ArrayList<LineSelection>();
        productLines.add(LineSelection.StandardFirePL);

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);
        myStandardFire.setStdFireCommodity(false);
        mySqPolicy.pniContact.setInsuranceScore(InsuranceScore.NeedsOrdered);
		mySqPolicy.standardFire = myStandardFire;
        mySqPolicy.lineSelection.add(LineSelection.StandardFirePL);
        mySqPolicy.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.PolicyIssued);
        driver.quit();

        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

        mySqPolicy.standardLiability = myStandardLiability;
        mySqPolicy.stdFireLiability = true;
        mySqPolicy.lineSelection.add(LineSelection.StandardLiabilityPL);
        mySqPolicy.addLineOfBusiness(ProductLineType.StandardLiability, GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueStdFirePol"})
    private void testIssueUmbrellaPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        mySqPolicy.squireUmbrellaInfo = squireUmbrellaInfo;
        mySqPolicy.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueUmbrellaPolicy"})
    private void testCloseAllTroubleTicketsOverrideInvoices() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(),
                this.arUser.getPassword(), this.mySqPolicy.squire.getPolicyNumber());

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickBCMenuTroubleTickets();

        BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
        troubleTicket.closeFirstTroubleTicket();

        //Standard IM
        BCTopMenuPolicy topMenuStuff = new BCTopMenuPolicy(driver);
        topMenuStuff.menuPolicySearchPolicyByPolicyNumber(mySqPolicy.standardInlandMarine.getPolicyNumber());

        policyMenu.clickBCMenuTroubleTickets();
        troubleTicket.closeFirstTroubleTicket();

        policyMenu.clickBCMenuSummary();
        BCPolicySummary summaryPage = new BCPolicySummary(driver);
        summaryPage.updateInvoicingOverride();

        PolicySummaryInvoicingOverrides invoiceOverrides = new PolicySummaryInvoicingOverrides(driver);
        invoiceOverrides.overrideInvoiceStream(mySqPolicy.squire.getPolicyNumber());

        //Standard Fire
        topMenuStuff.menuPolicySearchPolicyByPolicyNumber(this.mySqPolicy.standardFire.getPolicyNumber());
        policyMenu.clickBCMenuTroubleTickets();
        troubleTicket.closeFirstTroubleTicket();

        policyMenu.clickBCMenuSummary();
        summaryPage.updateInvoicingOverride();
        invoiceOverrides.overrideInvoiceStream(mySqPolicy.squire.getPolicyNumber());

        //Standard Liability
        topMenuStuff.menuPolicySearchPolicyByPolicyNumber(this.mySqPolicy.standardLiability.getPolicyNumber());
        policyMenu.clickBCMenuTroubleTickets();
        troubleTicket.closeFirstTroubleTicket();

        policyMenu.clickBCMenuSummary();
        summaryPage.updateInvoicingOverride();
        invoiceOverrides.overrideInvoiceStream(mySqPolicy.squire.getPolicyNumber());

        //Umbrella
        topMenuStuff.menuPolicySearchPolicyByPolicyNumber(this.mySqPolicy.squireUmbrellaInfo.getPolicyNumber());
        policyMenu.clickBCMenuTroubleTickets();
        troubleTicket.closeFirstTroubleTicket();

        new GuidewireHelpers(driver).logout();

    }

    @Test(dependsOnMethods = {"testCloseAllTroubleTicketsOverrideInvoices"})
    private void testRunInvoiceAndMoveClock() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(),
                this.arUser.getPassword(), this.mySqPolicy.squire.getPolicyNumber());
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        // Move clock 1 day
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);

        // Run Inoice due
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        new BatchHelpers(cf).runBatchProcess(BatchProcess.BC_Workflow);

        new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"testRunInvoiceAndMoveClock"})
    public void verifyCancelationCompletionInPolicyCenter() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(mySqPolicy.underwriterInfo.getUnderwriterUserName(), mySqPolicy.underwriterInfo.getUnderwriterPassword(), mySqPolicy.squire.getPolicyNumber());

        String activityName = "Multiple cancel document needed";
        String activityAssignedTo = "PL Accounts Receivable - Personal Lines Accounts Receivable";

        //Squire
        PolicySummary polSummary = new PolicySummary(driver);
        if (!polSummary.checkIfActivityExists(activityName)) {
            Assert.fail("Expected Activity : " + activityName + " is not displayed.");

        }

        if (!polSummary.getActivityAssignment(activityName).contains(activityAssignedTo)) {
        	Assert.fail("Expected Activity : " + activityName + " is not Assigned to " + activityAssignedTo);
        }

        //Standard Inland Marine
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByPolicyNumber(mySqPolicy.standardInlandMarine.getPolicyNumber());

        if (!polSummary.checkIfActivityExists(activityName)) {
        	Assert.fail("Expected Activity : " + activityName + " is not displayed.");

        }

        if (!polSummary.getActivityAssignment(activityName).contains(activityAssignedTo)) {
        	Assert.fail("Expected Activity : " + activityName + " is not Assigned to " + activityAssignedTo);
        }

        //Standard Fire
        policySearchPC.searchPolicyByPolicyNumber(this.mySqPolicy.standardFire.getPolicyNumber());

        if (!polSummary.checkIfActivityExists(activityName)) {
        	Assert.fail("Expected Activity : " + activityName + " is not displayed.");

        }

        if (!polSummary.getActivityAssignment(activityName).contains(activityAssignedTo)) {
        	Assert.fail("Expected Activity : " + activityName + " is not Assigned to " + activityAssignedTo);
        }

        //Standard Liability
        policySearchPC.searchPolicyByPolicyNumber(this.mySqPolicy.standardLiability.getPolicyNumber());

        if (!polSummary.checkIfActivityExists(activityName)) {
        	Assert.fail("Expected Activity : " + activityName + " is not displayed.");

        }

        if (!polSummary.getActivityAssignment(activityName).contains(activityAssignedTo)) {
        	Assert.fail("Expected Activity : " + activityName + " is not Assigned to " + activityAssignedTo);
        }

        //Umbrella
        policySearchPC.searchPolicyByPolicyNumber(this.mySqPolicy.squireUmbrellaInfo.getPolicyNumber());

        if (!polSummary.checkIfActivityExists(activityName)) {
        	Assert.fail("Expected Activity : " + activityName + " is not displayed.");

        }

        if (!polSummary.getActivityAssignment(activityName).contains(activityAssignedTo)) {
        	Assert.fail("Expected Activity : " + activityName + " is not Assigned to " + activityAssignedTo);
        }

        new GuidewireHelpers(driver).logout();

    }

}
