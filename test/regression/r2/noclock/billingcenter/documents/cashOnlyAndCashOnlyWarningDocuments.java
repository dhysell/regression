package regression.r2.noclock.billingcenter.documents;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDocuments;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

 /**
* @Author sreddy
* @Requirement : US7857 & US7854 Cash Only & Cash only warning documents
* @Description Make 3 payments from multiple payment screen
*              Reverse these payments with insufficient funds reason to put account on Cash only status
*              Check for the Cash only and cash only warning documents  in documents section
* @DATE Jan 25, 2017
*/

 public class cashOnlyAndCashOnlyWarningDocuments extends BaseTest {
	 private WebDriver driver;
		private GeneratePolicy myPolicyObj;		
		private ARUsers arUser = new ARUsers();		
		private String lienholderNumber, lienholderLoanNumber;
		private double lienholderPaymentAmount = 00.00;
		public String zyz = "none";
		public String justNumber = "none";
		private BCAccountMenu acctMenu;			
		private double aaAmount1 = 10.00;
		private double aaAmount2 = 20.00;
		private double aaAmount3 = 30.00;
		
		@Test
		public void generatePolicy() throws Exception {		
			ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
			ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
			
			ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();		
			AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);		
			loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
			loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
			PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
			loc1Bldg1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
			locOneBuildingList.add(loc1Bldg1);
			locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

			Config cf = new Config(ApplicationOrCenter.PolicyCenter);
			driver = buildDriver(cf);
            this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
					.withInsPersonOrCompany(ContactSubType.Company)
					.withInsCompanyName("PaymentAllocationPlanAndPolicyLevelDelinqPlanTest")
					.withPolOrgType(OrganizationType.Partnership)
					.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities))
					.withPolicyLocations(locationsList)
                    .withPaymentPlanType(PaymentPlanType.getRandom())
					.withDownPaymentType(PaymentType.Cash)
					.build(GeneratePolicyType.PolicyIssued);
			lienholderNumber= myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
			
			this.lienholderNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
	 		this.lienholderLoanNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber();
	 		this.lienholderPaymentAmount = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();

		}
		
 	@Test(dependsOnMethods = { "generatePolicy" })
 	public void makeInsuredDownPayment() throws Exception {
 				
 		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
 		Config cf = new Config(ApplicationOrCenter.BillingCenter);
 		driver = buildDriver(cf);
 		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());
 		
 		new GuidewireHelpers(driver).logout();
 	}
	
 	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
 	public void makeThreePayments() throws Exception {
 		Config cf = new Config(ApplicationOrCenter.BillingCenter);
 		driver = buildDriver(cf);
 		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);

 		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
 		accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, this.lienholderPaymentAmount);
 		
 		this.zyz = "Policy";
        this.justNumber = myPolicyObj.busOwnLine.getPolicyNumber();

        this.justNumber = myPolicyObj.busOwnLine.getPolicyNumber();
		BCTopMenu topMenu1 = new BCTopMenu(driver);
		topMenu1.clickDesktopTab();


		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultiplePayment();


		DesktopActionsMultiplePayment multiplePaymentsPage = new DesktopActionsMultiplePayment(driver);
        multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(this.justNumber, PaymentInstrumentEnum.Check, aaAmount1);
        multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(this.justNumber, PaymentInstrumentEnum.Check, aaAmount2);
        multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(this.justNumber, PaymentInstrumentEnum.Check, aaAmount3);
		multiplePaymentsPage.clickNext();
		System.out.println(this.justNumber);

		multiplePaymentsPage.clickFinish();
		BCSearchAccounts accountSearchBC = new BCSearchAccounts(driver);
		accountSearchBC.searchAccountByAccountNumber(myPolicyObj.fullAccountNumber);

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPayments();

		AccountPayments acctPayment = new AccountPayments(driver);
		
		acctPayment.reversePaymentAtFault(null, aaAmount1, null, aaAmount1,PaymentReturnedPaymentReason.InsufficientFunds);

		acctPayment.reversePaymentAtFault(null,aaAmount2, null, aaAmount2,PaymentReturnedPaymentReason.InsufficientFunds);

		acctPayment.reversePaymentAtFault(null,aaAmount3, null, aaAmount3,PaymentReturnedPaymentReason.InsufficientFunds);

		acctMenu.clickBCMenuDocuments();

		BCCommonDocuments acctDocs = new BCCommonDocuments(driver);

        Date docDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        Assert.assertTrue(acctDocs.verifyDocument("Cash Only Warning Letter", DocumentType.Cash_Only_Warning_Letter, null, null, docDate, null), "Cash Only Warning Document Not Generated");
        Assert.assertTrue(acctDocs.verifyDocument("Cash Only Letter", DocumentType.Cash_Only_Letter, null, null, docDate, null), "Cash Only Document Not Generated");
				
 	}
	
  }
