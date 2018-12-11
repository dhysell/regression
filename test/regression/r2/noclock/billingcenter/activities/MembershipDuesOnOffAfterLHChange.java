package regression.r2.noclock.billingcenter.activities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonActivities;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author JQU
 * @Description : DE5150--Membership Dues on/off not coming over to BC
 * 					When there is a payer change for Membership Dues from one lien to another, the old loan number is not coming over on the wax-on membership dues charges.
 * @DATE July 6, 2017
 */
public class MembershipDuesOnOffAfterLHChange extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;	
	private ARUsers arUser = new ARUsers();				
	private String oldLoanContractNumber="LN11111";
	private String newLoanContractNumber="LN22222";
	private String insFirstName="Membershipdues";
	private String insLastName="LHChange";
	private String oldLHNumber, newLHNumber;
	
	@Test
	public void generate() throws Exception {
		
		List<SmallBusinessType> smallBusiness = new ArrayList<SmallBusinessType>();
		smallBusiness.add(SmallBusinessType.Apartments);
		smallBusiness.add(SmallBusinessType.Offices);
		smallBusiness.add(SmallBusinessType.Condominium);
		smallBusiness.add(SmallBusinessType.SelfStorageFacilities);

		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(smallBusiness.get(NumberUtils.generateRandomNumberInt(0, (smallBusiness.size() - 1)))); 
		
		ArrayList<AdditionalInterest> loc1LNProperty1AdditionalInterestsList = new ArrayList<AdditionalInterest>();	
		AdditionalInterest loc1Property1AdditionalInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		loc1Property1AdditionalInterest.setNewContact(CreateNew.Create_New_Always);		
		loc1Property1AdditionalInterest.setLoanContractNumber(oldLoanContractNumber);
		loc1LNProperty1AdditionalInterestsList.add(loc1Property1AdditionalInterest);
		PolicyLocationBuilding building = new PolicyLocationBuilding();
		building.setAdditionalInterestList(loc1LNProperty1AdditionalInterestsList);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(building);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
	
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
				.withPolicyLocations(locationsList)
				.withPolOrgType(OrganizationType.Joint_Venture)
				.withBusinessownersLine(boLine)
				.withANIList(listOfANIs)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName(insFirstName, insLastName)
                .withMembershipDuesOnPNI()
				.withInsPrimaryAddress(new AddressInfo(true))
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		oldLHNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();	
		System.out.println("old LH Number is "+oldLHNumber);
	}
	@Test(dependsOnMethods = { "generate" })
	public void changePayerLHToLH() throws Exception {			
		AdditionalInterest newAdditionalInterest = new AdditionalInterest(ContactSubType.Company);
		newAdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		newAdditionalInterest.setNewContact(CreateNew.Create_New_Always);		
		newAdditionalInterest.setLoanContractNumber(newLoanContractNumber);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), this.myPolicyObj.busOwnLine.getPolicyNumber());

//        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		addAdditionInterestAndBillAllForBOP(newAdditionalInterest);
		newLHNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(1).getLienholderNumber();
		System.out.println("new LH Number is "+newLHNumber);
	}
	
	
	
	
	private void addAdditionInterestAndBillAllForBOP(AdditionalInterest newInterest) throws Exception {
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Add New Interest on Building", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
        building.clickBuildingsBuildingEdit(1);
        building.addAdditionalInterest(true, newInterest);
        building.clickOK();
        SideMenuPC menu = new SideMenuPC(driver);
        menu.clickSideMenuPayerAssignment();
        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
        payerAssignment.setPayerAssignmentBillAllCoverages(newInterest.getLienholderPayerAssignmentString());
        payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);
        policyChangePage.quoteAndIssue();
    }
	
	
	
	
	
	@Test(dependsOnMethods = { "changePayerLHToLH" })
	public void verifyActivity() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		//wait for policy change to come to BC
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Policy_Change);
		String oldChargeGroupName = charge.getChargesOrChargeHoldsPopupTableCellValue("Charge Group", null, oldLHNumber, null, ChargeCategory.Membership_Dues, TransactionType.Policy_Issuance, null, null, null, null, null, null, null, oldLoanContractNumber, null, null, null);
		String oldChargeGroupAddress = charge.getChargesOrChargeHoldsPopupTableCellValue("Charge Group", null, oldLHNumber, null, ChargeCategory.Premium, TransactionType.Policy_Issuance, null, null, null, null, null, null, null, oldLoanContractNumber, null, null, null);
		String newChargeGroupName = charge.getChargesOrChargeHoldsPopupTableCellValue("Charge Group", null, newLHNumber, null, ChargeCategory.Membership_Dues, TransactionType.Policy_Change, null, null, null, null, null, null, null, newLoanContractNumber, null, null, null);
		String newChargeGroupAddress = charge.getChargesOrChargeHoldsPopupTableCellValue("Charge Group", null, newLHNumber, null, ChargeCategory.Premium, TransactionType.Policy_Change, null, null, null, null, null, null, null, newLoanContractNumber, null, null, null);

		//verify the payer change activity
		acctMenu.clickAccountMenuPolicies();
        AccountPolicies policies = new AccountPolicies(driver);
		policies.clickPolicyNumber(myPolicyObj.accountNumber);
        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuActivities();
        BCCommonActivities activity = new BCCommonActivities(driver);
		activity.clickActivityTableSubject(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), OpenClosed.Open, "Payer Change");
        Map<String, String> loanNumberChangeInfoToVerify = new LinkedHashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{				
				this.put("Description", "Old Payer: " + oldLHNumber + " \n" + "Old Loan Number: " + oldLoanContractNumber + " \n" + " New Payer: " + newLHNumber + " \n" + " New Loan Number: " + newLoanContractNumber + " \n" + " Old Charge Groups: " + oldChargeGroupName +"\n "+" \n "+ oldChargeGroupAddress+" \n"+ " New Charge Groups: " +  newChargeGroupName+ " \n "+ newChargeGroupAddress);	
							
		}};
		
		if(!activity.verifyActivityInfo(loanNumberChangeInfoToVerify))
			Assert.fail("Payer change activity verification failed");		
	}
}
