package previousProgramIncrement.pi2_062818_090518.f277_BillingCenterLeinholderCleanUp;

import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsLienholderMutipleAccountPaymentWorkflow;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.ArrayList;

import static org.testng.Assert.assertTrue;
/**
* @Author JQU
* @Requirement 	DE4055 -- Multiple Account LH payment Screen allows insured account number to be entered
* 				Acceptance criteria:
sure that on the "Multiple Account Lienholder Payment" when you reach the screen "Add Additional Payments" only lienholder account numbers can be entered. If an insured account number is entered, then display a Validation Rule Block Screen that has the message �Only lienholder account numbers can be entered on this screen.� Also turn the cell red. (Req. 17-04-01.1)
				Steps to get there:
					On the "Desktop" click the "Actions" button > "New Payment" > "Multiple Account Lienholder Payment"
					Proceed to the screen "Add Additional Payments".
					Enter in an insured account number.
 *

 * @DATE July 23, 2018
* 
* */
public class DE4055MultipleAccountLHPaymentScreenAllowsInsuredAccountNumberToEeEntered extends BaseTest{
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private WebDriver driver;
	
	private String loanNumber="LN12345";
	private double amount = NumberUtils.generateRandomNumberInt(100, 500);
	private String hardStopMsg = "Only lienholder account numbers can be entered on this screen";

	private void generatePolicy() throws Exception {			
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();

		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);
		
        GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);
		
		
		
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
		
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);

		loc1LNBldg1AddInterest.setLoanContractNumber(loanNumber);
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		driver.quit();

		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("LHPaymentScreen")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();

	}
	@Test
	public void testMultipleAccountLHPayment() throws Exception{
		
		generatePolicy();
		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		 BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		 desktopMenu.clickDesktopMenuActionsMultipleAccountLienholderPayment();
		 DesktopActionsLienholderMutipleAccountPaymentWorkflow lienPayment = new DesktopActionsLienholderMutipleAccountPaymentWorkflow(driver);
		lienPayment.setAmount(amount);
		lienPayment.clickNext();
		
		lienPayment.fillOutNextLineOnPolicyPaymentsTable(1.0, null, null, myPolicyObj.busOwnLine.getPolicyNumber(), loanNumber);

		lienPayment.clickNext();

		lienPayment.fillOutNextLineOnAdditionalPaymentsTable(myPolicyObj.fullAccountNumber, amount-NumberUtils.generateRandomNumberInt(1, 10), null, null, null);
		lienPayment.clickNext();
		String msgOnTop = lienPayment.getTopInfo(driver);
		assertTrue(msgOnTop.contains(hardStopMsg), "Ensure we got hard stop for account number entered in this screen.");
	}
}
