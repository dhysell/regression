package previousProgramIncrement.pi3_090518_111518.f217_PolicyCenterDoubleDataEntry;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;
import com.idfbins.enums.State;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.AddressType;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.CommuteType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.search.SearchAddressBookPC_basics;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.VINHelper;

/**
* @Author sbroderick
* @Requirement Ensure that in specific places that the Address Type defaults to mailing
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:w:/r/sites/TeamNucleus/_layouts/15/Doc.aspx?sourcedoc=%7B23E9C775-5DFA-47A7-91C5-5EAB4D758993%7D&file=US16285%20-%20CM%20-%20Address%20Type%20Defaults.docx&action=default&mobileredirect=true">US16285 Address Type Defaults</a>
* @Description Ensure that in specific places that the Address Type defaults to mailing
* @DATE Sep 13, 2018
* 
* Acceptance criteria:
Ensure that on the account creation screen the address type is defaulted to mailing because this is where the mailing address would be added
Ensure that any member created on the policy has their address defaulted to mailing when the contact is created
Ensure that when a work address is entered in section 3 that an address type of work is sent or added to that address
Ensure that the PC user can change to any of the other options if the defaulted option is not one they want
* 
*/
public class US16285_AddressTypeDefaults extends BaseTest{
	
	public Contact getToNewSubmission(ContactSubType type, WebDriver driver) throws Exception {
		Contact pniContact;
		if(type.equals(ContactSubType.Person)) {
			pniContact = new Contact("Jake", "Zaugg", Gender.Male, DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -21));
			pniContact.setContactIsPNI(true);
			pniContact.setAddress(new AddressInfo(true));
			pniContact.setTaxIDNumber(String.valueOf(NumberUtils.generateRandomNumberDigits(9)));
			pniContact.setSocialSecurityNumber(String.valueOf(NumberUtils.generateRandomNumberDigits(9)));

		} else {
			pniContact = new Contact();
			pniContact.setCompanyName("Jakes");
			pniContact.setAddress(new AddressInfo(true));
			pniContact.setTaxIDNumber(String.valueOf(NumberUtils.generateRandomNumberDigits(9)));
			pniContact.setSocialSecurityNumber(String.valueOf(NumberUtils.generateRandomNumberDigits(9)));

		}
		
	    Agents agent = AgentsHelper.getRandomAgent();
	    
		new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());
		if (new Login(driver).accountLocked()) {
			agent = new Login(driver).loginAsRandomAgent();
		}
		new TopMenuPolicyPC(driver).clickNewSubmission();
		return pniContact;
	}
	
		@Test
		public void testAddressTypeDefaultsToMailing() throws Exception {
			
			Config cf = new Config(ApplicationOrCenter.PolicyCenter, "it");
		    WebDriver driver = buildDriver(cf);
		    
		    Contact pniContact = getToNewSubmission(ContactSubType.Company, driver);
		    
			SearchAddressBookPC_basics basicSearch = new SearchAddressBookPC_basics(driver);
			basicSearch.setSearchCriteria(null, null, pniContact.getCompanyName(), null, null, null, null, null, null, null);
			SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
			searchPC.clickSearch();
			searchPC.createNew(pniContact.getCompanyName(), null, null);
			SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
			Assert.assertTrue(createAccountPage.getSubmissionCreateAccountBasicsPrimaryAddressAddressType().contains("Mailing"), "The Address Type for the PNI did not default to Mailing.");
			createAccountPage.setSubmissionCreateAccountBasicsPrimaryAddressAddressType("Home");
			Assert.assertTrue(createAccountPage.getSubmissionCreateAccountBasicsPrimaryAddressAddressType().contains("Home"), "After attempting to change the Address type from the defaulted value of Mailing, the attempt failed. Please investigate.");
		}
		
		@Test
		public void testAddressTypeDefaultsToWork() throws Exception {
			
			Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		    WebDriver driver = buildDriver(cf);
		    
		    List<AddressInfo> addressList = new ArrayList<>();
		    addressList.add(new AddressInfo(true));
		    AddressInfo workAddress = new AddressInfo(true);
		    workAddress.setType(AddressType.Work);
		    addressList.add(workAddress);
		    
	        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.OneHundredHigh, MedicalLimit.TenK);
	        coverages.setUninsuredLimit(UninsuredLimit.OneHundred);
	        
	        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
	        squirePersonalAuto.setCoverages(coverages);

	      //Vehicles
	        Vin vin = VINHelper.getRandomVIN();
	        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
	        Vehicle veh1 = new Vehicle();
	        veh1.setVehicleTypePL(VehicleTypePL.PassengerPickup);
	        veh1.setVin(vin.getVin());

	        veh1.setEmergencyRoadside(true);
	        veh1.setAdditionalLivingExpense(true);
	        veh1.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);

	        vehicleList.add(veh1);      
	        
	        squirePersonalAuto.setCoverages(coverages);
	        squirePersonalAuto.setVehicleList(vehicleList);

	        Squire mySquire = new Squire(SquireEligibility.City);
	        mySquire.squirePA = squirePersonalAuto;


	        GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
	                .withSquire(mySquire)
	                .withProductType(ProductLineType.Squire)
	                .withLineSelection(LineSelection.PersonalAutoLinePL)
	                .withInsFirstLastName("Test", "WorkAddress")
	                .withInsAddressList(addressList)
	                .withPniCommuteType(CommuteType.PersonalVehicle)
	                .withPaymentPlanType(PaymentPlanType.Annual)
	                .withDownPaymentType(PaymentType.Cash)
	                .build(GeneratePolicyType.PolicyIssued);
	        
	        driver.get(cf.getUrlOfCenter(ApplicationOrCenter.ContactManager));
	        
	        assertTrue(verifyAddressTypeInAB(driver, myPolicyObjPL.pniContact.getFirstName(), myPolicyObjPL.pniContact.getLastName(), myPolicyObjPL.pniContact.getAddressByType(AddressType.Mailing).getLine1(), myPolicyObjPL.pniContact.getAddressByType(AddressType.Mailing).getType(), myPolicyObjPL.pniContact.getAddressByType(AddressType.Mailing)), "The PNI's mailing address did not transfer to ContactManager.");
	        new GuidewireHelpers(driver).logout();
	        assertTrue(verifyAddressTypeInAB(driver, myPolicyObjPL.pniContact.getFirstName(), myPolicyObjPL.pniContact.getLastName(), myPolicyObjPL.pniContact.getAddressByType(AddressType.Mailing).getLine1(), myPolicyObjPL.pniContact.getAddressByType(AddressType.Work).getType(), myPolicyObjPL.pniContact.getAddressByType(AddressType.Work)), "The PNI's work address address type didn't show the work address in ContactManager.");
		}
		
		@Test
		public void testAddressDefaultsForAdditionalNamedInsuredAndAdditionalInsured() throws Exception {
			
			Config cf = new Config(ApplicationOrCenter.PolicyCenter);
			WebDriver driver = buildDriver(cf);

			PolicyInfoAdditionalNamedInsured policyANI = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "AddNamed", "Ins", AdditionalNamedInsuredType.Friend, new AddressInfo(true));
		    policyANI.setNewContact(CreateNew.Create_New_Always);
		    ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<>();
		    listOfANIs.add(policyANI);
		    
		    
		    String[] nameArray = new StringsUtils().getUniqueName("Additional", null, "Ins");
		    PolicyBusinessownersLineAdditionalInsured ai = new PolicyBusinessownersLineAdditionalInsured(ContactSubType.Person, nameArray[0], nameArray[2], AdditionalInsuredRole.CertificateHolderOnly, new AddressInfo(true));
		    ai.setNewContact(CreateNew.Create_New_Always);
		    
		    ArrayList<PolicyBusinessownersLineAdditionalInsured> aiList = new ArrayList<>();
		    aiList.add(ai);
		    
		    PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(SmallBusinessType.ProcessingServiceRisks, aiList);
			
			int randomYear = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);
			
			PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
			loc1Bldg1.setYearBuilt(randomYear);
			loc1Bldg1.setClassClassification("storage");
			loc1Bldg1.setUsageDescription("Insured Building");

			ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<>();
			locOneBuildingList.add(loc1Bldg1);

			ArrayList<PolicyLocation> locationsList = new ArrayList<>();
			locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
			

			GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
					.withCreateNew(CreateNew.Create_New_Always)
					.withANIList(listOfANIs)
					.withBusinessownersLine(boLine)
					.withInsPersonOrCompany(ContactSubType.Person)
					.withInsFirstLastName("Bop", "Me")
					.withPolicyLocations(locationsList)
					.withPaymentPlanType(PaymentPlanType.Quarterly)
					.withDownPaymentType(PaymentType.Cash)
					.build(GeneratePolicyType.PolicyIssued);
			
			driver.get(cf.getUrlOfCenter(ApplicationOrCenter.ContactManager));
		        
			assertTrue(verifyAddressTypeInAB(driver, myPolicyObj.aniList.get(0).getPersonFirstName(), myPolicyObj.aniList.get(0).getPersonLastName(), myPolicyObj.aniList.get(0).getAddress().getLine1(), myPolicyObj.aniList.get(0).getAddress().getType(), myPolicyObj.aniList.get(0).getAddress()), "The Additional Named Insureds Address did not default to Mailing");
			new GuidewireHelpers(driver).logout();
			assertTrue(verifyAddressTypeInAB(driver, myPolicyObj.busOwnLine.getAdditonalInsuredBOLineList().get(0).getPersonFirstName(), myPolicyObj.busOwnLine.getAdditonalInsuredBOLineList().get(0).getPersonLastName(), myPolicyObj.busOwnLine.getAdditonalInsuredBOLineList().get(0).getAddress().getLine1(), myPolicyObj.busOwnLine.getAdditonalInsuredBOLineList().get(0).getAddress().getType(), myPolicyObj.busOwnLine.getAdditonalInsuredBOLineList().get(0).getAddress()), "The Additional Insureds Address did not default to Mailing");
		}
		
		public boolean verifyAddressTypeInAB(WebDriver driver, String firstName, String lastNameOrCompanyName, String primaryAddress, AddressType addressType, AddressInfo addressToFind) throws Exception {
			
			AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy");
	        AdvancedSearchAB searchAb = new AdvancedSearchAB(driver);
	        searchAb.loginAndGetToSearch(abUser);
	        if(firstName != null) {
	        	searchAb.searchByFirstNameLastNameAnyAddress(firstName, lastNameOrCompanyName);
//	        	searchAb.searchByFirstLastName(firstName, lastNameOrCompanyName, primaryAddress);//didn't click on results because of defect7906 that we are waiting Anthony to fix.
	        } else {
	        	searchAb.searchCompanyByName(lastNameOrCompanyName, primaryAddress, State.Idaho);
	        }
	        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
	        basicsPage.clickContactDetailsBasicsAddressLink();
	        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
	        String uiAddressType = addressPage.getTypeByAddress(addressToFind.getLine1());
	        System.out.println("The UI address type is: " +uiAddressType);
	        if(addressType.getValue().equals(uiAddressType)) {
	        	return true;
	        } else {
	        	return false;
	        }
		}
}
