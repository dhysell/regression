package previousProgramIncrement.pi1_041918_062718.maintenanceDefects;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.wizards.EditSplitMoveChargeWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactRole;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.TransactionNumber;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author JQU
 * @Requirement DE7414 - **HOT-FIX** BillingCenter is Not Accepting Cancel from PC
 * STEPS : Create a policy which has lien membership dues.
 * Do an Edit/Split/Move to move Dues from Lien to insured
 * Cancel the policy in PolicyCenter
 * Got "DBNullContraintException" and BC will never receive the cancellation from PC
 * @DATE April 24, 2018
 */
public class BillingCenterNotAcceptingCancellationFromPCTest extends BaseTest {
	private GeneratePolicy myPolicyObj = null;
	private WebDriver driver;
	private ARUsers arUser = new ARUsers();
	
	private String loanNumber="LN12345";
	private String LHNumber;
	
	private void generatePolicy() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();		
		
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
		
		AdditionalInterest loc1Property1AdditionalInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
		loc1Property1AdditionalInterest.setLienholderNumber(myContactLienLoc1Obj.lienNumber);
		loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		loc1Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc1Property1AdditionalInterest.setLoanContractNumber(loanNumber);
		
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		location1Property1.setBuildingAdditionalInterest(loc1Property1AdditionalInterest);
		locOnePropertyList.add(location1Property1);
		locationsList.add(new PolicyLocation(locOnePropertyList));		

		SquireLiability liabilitySection = new SquireLiability();
		
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;        

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Accept", "Cancellation")
				.withPaymentPlanType(PaymentPlanType.Annual)				
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);			

		LHNumber = myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();	
		driver.quit();
	}
	@Test	
	public void moveDuesFromLienToInsured() throws Exception {	
        generatePolicy();
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		double duesAmount = charge.getChargeAmount(LHNumber, TransactionNumber.Membership_Dues_Charged);
		charge.clickEditSplitMoveCharges(LHNumber, TransactionNumber.Membership_Dues_Charged, TransactionType.Issuance, myPolicyObj.accountNumber);
		EditSplitMoveChargeWizard move = new EditSplitMoveChargeWizard(driver);
		move.clickAdd();
		move.fillOutEditSplitMoveChargeTable(1, myPolicyObj.fullAccountNumber, null, null, null, duesAmount);
		move.clickUpdate();
	}
	@Test(dependsOnMethods = { "moveDuesFromLienToInsured" })
	public void cancelPolicy() throws Exception{		
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.squire.getPolicyNumber());
        StartCancellation cancellation = new StartCancellation(driver);
        cancellation.cancelPolicy(CancellationSourceReasonExplanation.NoReasonGiven, "Cancel Policy", DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), true);
        //verify cancellation in Pc
        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
        complete.clickViewYourPolicy();
        PolicySummary policySummary = new PolicySummary(driver);
        assertTrue(policySummary.verifyPolicyTransaction(null, null, null, TransactionType.Cancellation, null, 60), "Didn't find the cancellation in PolicyCenter.");      
        new GuidewireHelpers(driver).logout();
	}
	@Test(dependsOnMethods = { "cancelPolicy" })
	public void verifyCancellation() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(120, TransactionType.Cancellation);
	}
}
