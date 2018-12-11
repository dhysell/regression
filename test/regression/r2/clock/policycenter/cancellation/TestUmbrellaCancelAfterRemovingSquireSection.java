package regression.r2.clock.policycenter.cancellation;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.topmenu.BCTopMenuPolicy;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
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
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.activity.UWActivityPC;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyLocation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : DE7055: Umbrella cancel not completing when section(s) gone
 * from squire
 * @RequirementsLink <a href=
 * "https://rally1.rallydev.com/#/128071644704d/detail/defect/190249354696">
 * DE7055</a>
 * @Description
 * @DATE Jan 29, 2018
 */
@QuarantineClass
public class TestUmbrellaCancelAfterRemovingSquireSection extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy mySqPolicy;
    private ARUsers arUser;
    private Underwriters uw;

    @Test
    public void testIssueSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
        loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        loc1LNBldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        loc1LNBldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
        loc1LNBldg1AddInterest.setLoanContractNumber("LN12345");

        loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
        PLPolicyLocationProperty plBuilding = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        plBuilding.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
        locOnePropertyList.add(plBuilding);
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
                MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySqPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Cancel", "Partial")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueSquirePolicy"})
    public void testIssueUmbrella() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        mySqPolicy.squireUmbrellaInfo = squireUmbrellaInfo;
        mySqPolicy.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueUmbrella"})
    public void testMakeInsuredDownPayment() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(),
                this.arUser.getPassword(), this.mySqPolicy.accountNumber);

        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(mySqPolicy, mySqPolicy.squire.getPremium().getDownPaymentAmount(),
                mySqPolicy.squire.getPolicyNumber());

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"testMakeInsuredDownPayment"})
    public void moveClocks() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
    }

    @Test(dependsOnMethods = {"moveClocks"})
    public void runInvoiceDueAndCheckDelinquency() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(),
                this.arUser.getPassword(), this.mySqPolicy.accountNumber);

        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);

        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.BC_Workflow);

        BCTopMenuPolicy topMenuStuff = new BCTopMenuPolicy(driver);
        topMenuStuff.menuPolicySearchPolicyByPolicyNumber(mySqPolicy.squire.getPolicyNumber());
        new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
        // delay required
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"runInvoiceDueAndCheckDelinquency"})
    public void testRemoveSectionIAndII() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(),
                this.mySqPolicy.squire.getPolicyNumber());
        PolicySummary pcSummary = new PolicySummary(driver);
        String transactionEffDate = "";
        transactionEffDate = pcSummary.getPendingCancelTransactionEffectiveDate(1);
        if (transactionEffDate != "") {
            pcSummary.clickActivity("Partial Cancel AR");
            UWActivityPC activityPopupPage = new UWActivityPC(driver);
            activityPopupPage.clickCompleteAndAssignTo();
            InfoBar infoBar = new InfoBar(driver);
            infoBar.clickInfoBarPolicyNumber();
            uw = UnderwritersHelper.getUnderwriterInfoByFullName(pcSummary.getActivityAssignment("Partial Cancel UW"));

        } else {
            Assert.fail("Pending Cancellation is not shown in PC!!!!!!!");
        }

        new GuidewireHelpers(driver).logout();
        driver.quit();
        
        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                mySqPolicy.squire.getPolicyNumber());

        pcSummary = new PolicySummary(driver);
        pcSummary.clickActivity("Partial Cancel UW");
        UWActivityPC activityPopupPage = new UWActivityPC(driver);
        activityPopupPage.setActivitySubject("Closed");
        activityPopupPage.setText("Testing Purpose");
        activityPopupPage.clickComplete();

        new GuidewireHelpers(driver).logout();
        driver.quit();

        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                mySqPolicy.squire.getPolicyNumber());

        StartPolicyChange policyChange = new StartPolicyChange(driver);
        policyChange.startPolicyChange("Removing Section Iand II",
                DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyDetail propertyDetail = new GenericWorkorderSquirePropertyDetail(driver);
        propertyDetail.removeBuilding("1");
        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyLocation propertyLocations = new GenericWorkorderSquirePropertyLocation(driver);
        propertyLocations.SelectLocationsCheckboxByRowNumber(1);
        propertyLocations.clickRemoveButton();
        sideMenu.clickSideMenuLineSelection();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
        lineSelection.checkPersonalPropertyLine(false);
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        risk.Quote();
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll();
        risk.approveAll_IncludingSpecial();
        sideMenu.clickSideMenuQuote();
        policyChange.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        new BasePage(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        completePage.clickViewYourPolicy();
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"testRemoveSectionIAndII"})
    public void testMoveClock() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 45);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Partial_Nonpay_Cancel);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Partial_Nonpay_Cancel_Documents);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
    }

    @Test(dependsOnMethods = {"testMoveClock"})
    private void testUmbrellaCancellation() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                mySqPolicy.squireUmbrellaInfo.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

        String comment = "For Rewrite full term of the policy";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, comment, currentDate, true);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        new BasePage(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        completePage.clickViewYourPolicy();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
        docs.setDocumentDescription("Cancellation Notice");
        docs.selectRelatedTo("Cancel");
        docs.clickSearch();
        docs.verifyDocumentPreviewButton();
        if (new GuidewireHelpers(driver).errorMessagesExist() && new GuidewireHelpers(driver).containsErrorMessage("Exception")) {
            Assert.fail(new GuidewireHelpers(driver).getFirstErrorMessage());
        }
    }

}
