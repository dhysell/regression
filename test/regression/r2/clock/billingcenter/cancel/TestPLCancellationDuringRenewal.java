package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchOtherJobsPC;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US8740: Cancellation During Renewal
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/BOP%20Renewal/8.0%20-%20Renewal.pptx"></a>
 * @Description
 * @DATE Nov 4, 2016
 */
@QuarantineClass
public class TestPLCancellationDuringRenewal extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy mySquirePolicyObjPL = null;
    private ARUsers arUser;
    private GeneratePolicy myPolicyObjPL;

    @Test()
    public void testIssueSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Farm Equipment
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);
        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.SpecialForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip);
        // Rec Equip
        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500", PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));

        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.FarmEquipment);
        imTypes.add(InlandMarine.RecreationalEquipment);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        //NOT USED IN THIS TEST THAT I SEE.
//        SquireFPP squireFPP = new SquireFPP();
//        SquireFPPTypeItem machinery1 = new SquireFPPTypeItem(squireFPP, FPPFarmPersonalPropertySubTypes.Tractors);
//        SquireFPPTypeItem machinery2 = new SquireFPPTypeItem(squireFPP, FPPFarmPersonalPropertySubTypes.HarvestersHeaders);
//        SquireFPPTypeItem irrigation1 = new SquireFPPTypeItem(squireFPP, FPPFarmPersonalPropertySubTypes.CirclePivots);
//        SquireFPPTypeItem irrigation2 = new SquireFPPTypeItem(squireFPP, FPPFarmPersonalPropertySubTypes.WheelLines);
//
//        squireFPP.setItems(machinery1, machinery2, irrigation1, irrigation2);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));

        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);

        locationsList.add(locToAdd);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.inlandMarine = myInlandMarine;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySquirePolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("Cancel", "Renewal")
                .withPolTermLengthDays(49)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }


    @Test(dependsOnMethods = {"testIssueSquirePolicy"})
    private void testRunInvoiceWithMakingDownpayment() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.mySquirePolicyObjPL.squire.getPolicyNumber());

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickTopInfoBarAccountNumber();
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
        if (invoice.getInvoiceStatus().equals(InvoiceStatus.Planned)) {
            new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        }
        acctMenu.clickBCMenuTroubleTickets();
        acctMenu.clickAccountMenuInvoices();
        if (invoice.getInvoiceStatus().equals(InvoiceStatus.Billed)) {
            acctMenu = new BCAccountMenu(driver);
            acctMenu.clickAccountMenuActionsNewDirectBillPayment();
            NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
            directPayment.makeDirectBillPaymentExecute(mySquirePolicyObjPL.squire.getPremium().getDownPaymentAmount(), mySquirePolicyObjPL.accountNumber);
        } else
            Assert.fail("running the invoice batch process  failed!!!!");
    }

    @Test(dependsOnMethods = {"testRunInvoiceWithMakingDownpayment"})
    private void testCreateRenewalTransaction() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.squire.getPolicyNumber());

        PolicyMenu pcMenu;
        pcMenu = new PolicyMenu(driver);
        pcMenu.clickRenewPolicy();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(mySquirePolicyObjPL);


        SearchOtherJobsPC searchJob = new SearchOtherJobsPC(driver);
        searchJob.searchJobByAccountNumber(mySquirePolicyObjPL.accountNumber, "003");

        SideMenuPC sideMenu = new SideMenuPC(driver);
        if (!mySquirePolicyObjPL.productType.equals(ProductLineType.StandardIM) && !mySquirePolicyObjPL.productType.equals(ProductLineType.PersonalUmbrella)) {
            sideMenu.clickSideMenuPLInsuranceScore();
            GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
            creditReport.fillOutCreditReport(mySquirePolicyObjPL);
        } else {
            GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
            polInfo.clickEditPolicyTransaction();
        }

        StartRenewal renewal = new StartRenewal(driver);
        renewal.quoteAndIssueRenewal(RenewalCode.Renew_Good_Risk, myPolicyObjPL);
    }

    @Test(dependsOnMethods = {"testCreateRenewalTransaction"})
    private void testBCRenewalInvoices() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.mySquirePolicyObjPL.squire.getPolicyNumber());

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickTopInfoBarAccountNumber();
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
        invoice.clickAccountInvoicesTableRowByRowNumber(2);
        getQALogger().info("Second Invoice : " + invoice.getInvoiceAmountByRowNumber(2) + invoice.getInvoiceStatus());
        if (invoice.getInvoiceAmountByRowNumber(2) == 0 && !invoice.getInvoiceStatus().equals(InvoiceStatus.Planned)) {
            Assert.fail("Renewal Invoice is not displayed.!");
        }

    }


    @Test(dependsOnMethods = {"testBCRenewalInvoices"})
    public void moveClocks49DaysRunInvoiceBatch() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 49);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
    }

    @Test(dependsOnMethods = {"moveClocks49DaysRunInvoiceBatch"})
    public void moveClocks1DayRunInvoiceDueBatch() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.mySquirePolicyObjPL.squire.getPolicyNumber());

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickTopInfoBarAccountNumber();
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        acctMenu.clickBCMenuDelinquencies();
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
        boolean delinquencyFound = delinquency.verifyDelinquencyByReason(OpenClosed.Closed, DelinquencyReason.NotTaken,null, currentDate);

        if (!delinquencyFound) {
            Assert.fail("Not Taken Status is not found");
        }
    }

    @Test(dependsOnMethods = {"moveClocks1DayRunInvoiceDueBatch"})
    private void testSquireCancellation() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.mySquirePolicyObjPL.accountNumber);
        PolicySummary summary = new PolicySummary(driver);
        if (summary.getCompletedTransactionEffectiveDate(TransactionType.Cancellation) == null) {
            Assert.fail("Cancellation is not done for renewal term");
        }


    }

    @Test()
    private void issueAnotherSquirePol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquirePersonalAutoCoverages sqpaCoverages = new SquirePersonalAutoCoverages(LiabilityLimit.ThreeHundredHigh, MedicalLimit.TenK, true, UninsuredLimit.ThreeHundred, false, UnderinsuredLimit.ThreeHundred);
        sqpaCoverages.setAccidentalDeath(true);

        // Vehicle List
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle veh1 = new Vehicle();
        veh1.setVehicleTypePL(VehicleTypePL.PrivatePassenger);
        vehicleList.add(veh1);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(sqpaCoverages);
        squirePersonalAuto.setVehicleList(vehicleList);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;
        property.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = property;


        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Cancel", "Renewal")
                .withSocialSecurityNumber(StringsUtils.generateRandomNumber(666000000, 666999999))
                .withPolTermLengthDays(49)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"issueAnotherSquirePol"})
    private void testStartRenewalOnIssuedSquire() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login with UW
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.squire.getPolicyNumber());

        PolicyMenu pcMenu = new PolicyMenu(driver);
        pcMenu.clickRenewPolicy();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(myPolicyObjPL);


        SearchOtherJobsPC searchJob = new SearchOtherJobsPC(driver);
        searchJob.searchJobByAccountNumber(myPolicyObjPL.accountNumber, "000");

        SideMenuPC sideMenu = new SideMenuPC(driver);
        if (!myPolicyObjPL.productType.equals(ProductLineType.StandardIM) && !myPolicyObjPL.productType.equals(ProductLineType.PersonalUmbrella)) {
            sideMenu.clickSideMenuPLInsuranceScore();
            GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
            creditReport.fillOutCreditReport(myPolicyObjPL);
        } else {
            GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
            polInfo.clickEditPolicyTransaction();
        }

        StartRenewal renewal = new StartRenewal(driver);
        renewal.quoteAndIssueRenewal(RenewalCode.Renew_Good_Risk, myPolicyObjPL);

    }

    @Test(dependsOnMethods = {"testStartRenewalOnIssuedSquire"})
    private void testCancelCurrentPolicyTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        String errorMessage = "";

        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        String comment = "Testing Purpose";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.LossRuns, comment, currentDate, true);

        // searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByAccountNumber(myPolicyObjPL.accountNumber);

        PolicySummary summary = new PolicySummary(driver);
        if (summary.getTransactionNumber(TransactionType.Cancellation, comment) == null) {
            errorMessage = errorMessage + "Cancellation is not done for current term !!!\n";
        }

        Date effectiveDate = summary.getCompletedTransactionEffectiveDate(TransactionType.Renewal);
        summary.getTransactionNumberByEffectiveDate(TransactionType.Cancellation, effectiveDate);

        String initialCancellationTransaction = summary.getTransactionNumberByEffectiveDate(TransactionType.Cancellation, myPolicyObjPL.squire.getEffectiveDate());

        String renewalCancellationTransaction = summary.getTransactionNumberByEffectiveDate(TransactionType.Cancellation, effectiveDate);

        if (initialCancellationTransaction == null || renewalCancellationTransaction == null) {
            errorMessage = errorMessage + "Cancellation is not done for renewal term \n";
        }

        SideMenuPC polMenu = new SideMenuPC(driver);
        polMenu.clickSideMenuToolsDocuments();
        PolicyDocuments pcDoc = new PolicyDocuments(driver);
        pcDoc.selectRelatedTo(renewalCancellationTransaction);
        pcDoc.clickSearch();

        if (pcDoc.getDocumentCount(renewalCancellationTransaction) > 0) {
            errorMessage = errorMessage + "Documents are displayed for Renewal Cancellation Transaction \n";
        }


        if (errorMessage != "")
            Assert.fail(errorMessage);
    }

}
