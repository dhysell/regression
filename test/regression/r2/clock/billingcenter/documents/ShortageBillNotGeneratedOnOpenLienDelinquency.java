package regression.r2.clock.billingcenter.documents;


import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonDocuments;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sgunda
 * @Requirement DE7243 - **Hot-Fix** Shortage Bill is not being generated when there is an open lien-paid delinquency on a policy
 * @RequirementsLink <a href=https://rally1.rallydev.com/#/7832667974d/detail/defect/200398774808>Rally Defect DE7243</a>
 * @DATE Mar 1, 2018
 */
public class ShortageBillNotGeneratedOnOpenLienDelinquency extends BaseTest {
	private WebDriver driver;
    private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
    private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
    private ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
    private GeneratePolicy myPolicyObj = null;
    private ARUsers arUser = new ARUsers();
    private String lienholderNumber = null;
    private String lienholderLoanNumber = null;
    private String policyChangeDescription = "change coverage";
    private BCAccountMenu acctMenu;
    private BCCommonDelinquencies acctDelinquency;
    private double costChg;
    private Date invoiceDate;
    private BCCommonDocuments accountDocuments;

    @Test
    public void generatePolicy() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
    	driver = buildDriver(cf);
        int yearTest = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);
        PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
        loc1Bldg1.setYearBuilt(yearTest);
        loc1Bldg1.setClassClassification("storage");

        PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
        loc1Bldg2.setYearBuilt(2010);
        loc1Bldg2.setClassClassification("storage");
        
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

        AddressInfo addIntTest = new AddressInfo();
        addIntTest.setLine1("PO Box 711");
        addIntTest.setCity("Pocatello");
        addIntTest.setState(State.Idaho);
		addIntTest.setZip("83204");//-0711

		ArrayList<AddressInfo> addressInfoList = new ArrayList<AddressInfo>();
		addressInfoList.add(addIntTest);
		GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.withAddresses(addressInfoList)
				.build(GenerateContactType.Company);
		
		driver.quit();

        AdditionalInterest loc1Bld2AddInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, addIntTest);
        loc1Bld2AddInterest.setAddress(addIntTest);
        loc1Bld2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        loc1Bldg2AdditionalInterests.add(loc1Bld2AddInterest);
        loc1Bldg2.setAdditionalInterestList(loc1Bldg2AdditionalInterests);

        this.locOneBuildingList.add(loc1Bldg1);
        this.locOneBuildingList.add(loc1Bldg2);
        this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("DE7243 BOP w/Fix")
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        this.lienholderNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
        this.lienholderLoanNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLoanContractNumber();
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void makeInsuredDownPayment() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"makeInsuredDownPayment"})
    public void moveClocks() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 25);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
    }

    @Test(dependsOnMethods = {"moveClocks"})
    public void changeCoverageOnBuildingOne() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.changeBOPBuildingCoverage(1, 400000.00, 400000.00);
        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
        complete.clickViewYourPolicy();
        PolicySummary pcSum = new PolicySummary(driver);
        costChg = pcSum.getTransactionPremium(null, policyChangeDescription);
        System.out.println("The cost change after policy change is  :" + costChg);

        new GuidewireHelpers(driver).logout();

    }

    @Test(dependsOnMethods = {"changeCoverageOnBuildingOne"})
    public void verifyPolicyChangeCharges() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuCharges();

        AccountCharges accountCharges = new AccountCharges(driver);
        Assert.assertTrue(accountCharges.waitUntilChargesFromPolicyContextArrive(180, TransactionType.Policy_Change));

        acctMenu.clickAccountMenuInvoices();
        AccountInvoices accountInvoices = new AccountInvoices(driver);
        invoiceDate = accountInvoices.getInvoiceDateByInvoiceType(InvoiceType.Shortage);

    }

    @Test(dependsOnMethods = {"verifyPolicyChangeCharges"})
    public void makeLienDelinqentAndBillInsuredShortageToVerifyIfDocumentIsCreated() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 8);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuDelinquencies();
        acctDelinquency = new BCCommonDelinquencies(driver);
        Assert.assertTrue(acctDelinquency.verifyDelinquencyExists(OpenClosed.Open, DelinquencyReason.PastDueLienPartialCancel, null, this.lienholderLoanNumber, null, null, null, null), "Delinquency does not exist Which is not Expected");

        ClockUtils.setCurrentDates(cf, invoiceDate);
       new BatchHelpers(cf). runBatchProcess(BatchProcess.Invoice);


        acctMenu.clickAccountMenuInvoices();
        AccountInvoices accountInvoices = new AccountInvoices(driver);
        accountInvoices.verifyInvoice(InvoiceType.Shortage, InvoiceStatus.Billed, costChg);

        acctMenu.clickBCMenuDocuments();
        Assert.assertTrue(accountDocuments.verifyDocument("Shortage Bill", DocumentType.Shortage_Bill, null, null, invoiceDate, null), "Shortage bill document to generate, test failed");
    }


}

