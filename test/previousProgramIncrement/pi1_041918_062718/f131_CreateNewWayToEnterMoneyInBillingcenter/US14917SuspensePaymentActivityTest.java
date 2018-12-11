package previousProgramIncrement.pi1_041918_062718.f131_CreateNewWayToEnterMoneyInBillingcenter;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonActivities;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.DesktopSuspensePayments;
import repository.bc.search.BCSearchAccounts;
import repository.bc.wizards.CreateNewSuspensePaymentWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SuspensePaymentStatus;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author JQU
* @Requirement 	US14917 - Verify suspense payment activity works for both insured and LH
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/02%20-%20Activities/02-05%20Activities%20List.xlsx?web=1">02-05 Activities List</a>

* @DATE May 11, 2018*/
public class US14917SuspensePaymentActivityTest extends BaseTest {
	private WebDriver driver;
	private Config cf;
	private ARUsers arUser = new ARUsers();
	private GeneratePolicy myPolicyObj = null;
	private Date currentDate;
	private int suspenseAmount1 = NumberUtils.generateRandomNumberInt(20, 100);
	private int suspenseAmount2 = NumberUtils.generateRandomNumberInt(101, 200);
	private String lienholderNumber;
	private String insuredActivity = "Suspense payment charge received";
	private String LHActivity = "Suspense payment charge received for Lien account ";
	
	private boolean findPaymentRow(DesktopSuspensePayments suspensePayment, Date date, SuspensePaymentStatus status, String accountNumber, double amount){
			int pageNumber=0;
			boolean foundRow = false;
			while(pageNumber<suspensePayment.getNumberPages() && !foundRow){
				try{
					suspensePayment.getSuspensePaymentTableRow(date, null, status, null, accountNumber, null, null, null, null, (double)amount, null);
					foundRow = true;
				}catch (Exception e) {
					getQALogger().error("Didn't find the suspense payment on page #"+pageNumber);
					pageNumber++;
					suspensePayment.goToNextPage();
				}			
			}
			return foundRow;
	}
	private void createSuspensePayment(CreateNewSuspensePaymentWizard suspensePmtWizard, Date paymentDate, double amount, String accountNumber, String policyNumber){
		suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);
		suspensePmtWizard.setSuspensePaymentDate(paymentDate);
		suspensePmtWizard.setSuspensePaymentAmount(amount);
		suspensePmtWizard.selectPaymentInstrument(PaymentInstrumentEnum.Cash);

		if(policyNumber != null){
			suspensePmtWizard.setAccountNumberFromPicker(accountNumber);
			suspensePmtWizard.selectPolicyNumber(policyNumber);
		}else{
			suspensePmtWizard.setAccountNumber(accountNumber);
		}
		suspensePmtWizard.clickCreate();
	}

    private void verifyActivity(BCCommonActivities activity, String insuredLHActivity, String priority, Date dueDate, String accountNumber, String policy, String assignedTo, String status) {
		Map<String, String> insuredLHActivityToVerify = new LinkedHashMap<String, String>() {	
			private static final long serialVersionUID = 1L;
			{
				this.put("Subject", insuredLHActivity);
				this.put("Description", "Suspense payment was made");
				this.put("Priority", priority);
				this.put("Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", dueDate));
				this.put("Escalation Date", DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(dueDate, DateAddSubtractOptions.Day, 10)));
				this.put("Account", accountNumber);
                this.put("Policy Period", policy);
				this.put("Assigned to", assignedTo);
				this.put("Status", status);					
		}};
		assertTrue(activity.verifyActivityInfo(insuredLHActivityToVerify), "Verify suspense payment activity failed for "+accountNumber);
	}
	
	private void generatePolicy() throws Exception {

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();				

		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.ContactManager));
		
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);
		
		AdditionalInterest loc1Property1AdditionalInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
		loc1Property1AdditionalInterest.setLienholderNumber(myContactLienLoc1Obj.lienNumber);
		loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		location1Property1.setBuildingAdditionalInterest(loc1Property1AdditionalInterest);
		locOnePropertyList.add(location1Property1);
		locationsList.add(new PolicyLocation(locOnePropertyList));		

		SquireLiability liabilitySection = new SquireLiability();		

		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;      

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsFirstLastName("SuspensePayment", "Activity")
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.QuickQuote);	
		lienholderNumber = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
	}
	
	@Test
	public void makeSuspensePaymentForInsured() throws Exception {
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		generatePolicy();	

		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);		
		
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuSuspensePayments();
		DesktopSuspensePayments suspensePayment = new DesktopSuspensePayments(driver);
		suspensePayment.clickNew();
		CreateNewSuspensePaymentWizard suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);		
		createSuspensePayment(suspensePmtWizard, currentDate, suspenseAmount1, myPolicyObj.accountNumber+"-001", null);
		//verify the suspense payment
		assertTrue(findPaymentRow(suspensePayment, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), SuspensePaymentStatus.Open, myPolicyObj.accountNumber+"-001", (double)suspenseAmount1), "didn't find the suspense payment with amount $"+suspenseAmount1+"for account "+myPolicyObj.accountNumber);;
		
		//create suspense payment for LH
		suspensePayment.clickNew();
		suspensePmtWizard = new CreateNewSuspensePaymentWizard(driver);
		createSuspensePayment(suspensePmtWizard, currentDate, suspenseAmount2, lienholderNumber, null);
		
		//verify the suspense payment
		assertTrue(findPaymentRow(suspensePayment, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), SuspensePaymentStatus.Open, lienholderNumber, (double)suspenseAmount2), "didn't find the suspense payment with amount $"+suspenseAmount2+"for Lienholder "+lienholderNumber);;
		
		
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));

		myPolicyObj.convertTo(driver, GeneratePolicyType.PolicyIssued);	
	}
	@Test(dependsOnMethods = { "makeSuspensePaymentForInsured" })
    public void verifyActivit() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		//verify Insured suspense payment activity
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuActivities();
        BCCommonActivities activity = new BCCommonActivities(driver);
		assertTrue(activity.verifyActivityTable(currentDate, OpenClosed.Open, insuredActivity), "Couldn't find the suspense payment activity for account " + myPolicyObj.accountNumber);
		
		activity.clickActivityTableSubject(currentDate, OpenClosed.Open, insuredActivity);		
		verifyActivity(activity, insuredActivity, "High", currentDate, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), "AR General Farm Bureau", "Open");
		
		//verify LH suspense payment activity
		BCSearchAccounts search = new BCSearchAccounts(driver);
		search.searchAccountByAccountNumber(lienholderNumber);
		accountMenu.clickBCMenuActivities();
		assertTrue(activity.verifyActivityTable(currentDate, OpenClosed.Open, LHActivity+lienholderNumber), "Couldn't find the suspense payment activity for lienholder "+lienholderNumber);
		activity.clickActivityTableSubject(currentDate, OpenClosed.Open, LHActivity);
		verifyActivity(activity, LHActivity, "High", currentDate, lienholderNumber, myPolicyObj.squire.getPolicyNumber(), "AR General Farm Bureau", "Open");
	}
}
