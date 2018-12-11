package previousProgramIncrement.pi3_090518_111518.nonFeatures.Nucleus;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.State;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ReviewContacts;
import repository.ab.pagelinks.PageLinks;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.search.PotentialDuplicateContacts;
import repository.ab.sidebar.SidebarAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.MergeContact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

/**
* @Author sbroderick
* @Requirement We created a new tool to merge contacts.
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:w:/r/sites/TeamNucleus/_layouts/15/Doc.aspx?sourcedoc=%7B0AD8A949-7002-4A43-97D2-A14FF1A549B7%7D&file=US16280%20-%20CM%20-%20Ensure%20Contacts%20are%20merged%20in%20all%20centers.docx&action=default&mobileredirect=true">Requirements for the Merge Tool</a>
* @Description 
* @DATE Nov 1, 2018
*/
public class US16280_MergeContacts extends BaseTest{
	private WebDriver driver;
	
	
	@Test
	public void testMergeContacts() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);
        
        AbUsers superUser = AbUserHelper.getUserByUserName("su");
		new Login(driver).login(superUser.getUserName(), superUser.getUserPassword());
		new BatchHelpers(cf).runBatchProcess(BatchProcess.DuplicateContactsFinder);
		new GuidewireHelpers(driver).logout(); 
        
        driver.get(cf.getEnvironmentsMap().get(ApplicationOrCenter.PolicyCenter).getUrl());
       
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();

        loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);

        locOnePropertyList.add(loc1Bldg1);
        locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Dup", "Test")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        
        driver.get(cf.getEnvironmentsMap().get(ApplicationOrCenter.ContactManager).getUrl());
        AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");
        
        ArrayList<AddressInfo> addresses = new ArrayList<>();
        addresses.add(myPolicyObjPL.pniContact.getAddress());
        addresses.add(new AddressInfo(true));
	
        GenerateContact myContactObj = new GenerateContact.Builder(driver)
				.withCreator(user)
				.withFirstLastName(myPolicyObjPL.pniContact.getFirstName(), myPolicyObjPL.pniContact.getMiddleName(), myPolicyObjPL.pniContact.getLastName())
				.withAddresses(addresses)
				.withTIN(myPolicyObjPL.pniContact.getTaxIDNumber())
				.build(GenerateContactType.Person);

		System.out.println("accountNumber: " + myContactObj.accountNumber);
		new GuidewireHelpers(driver).logout();
        
		new Login(driver).login(superUser.getUserName(), superUser.getUserPassword());
		new BatchHelpers(cf).runBatchProcess(BatchProcess.DuplicateContactsFinder);
		new GuidewireHelpers(driver).logout(); 
		PotentialDuplicateContacts dupContacts = new PotentialDuplicateContacts(driver);
		ReviewContacts reviewContacts = dupContacts.reviewPotentialDuplicateContact(superUser, myPolicyObjPL.pniContact.getLastName(), myPolicyObjPL.pniContact.getAddress().getLine1()+", "+myPolicyObjPL.pniContact.getAddress().getCity()+", ", "Exact", true);
		reviewContacts.setMergedContactObjects();
		reviewContacts.setAddressInfo();
		ArrayList<MergeContact> mergedContacts = reviewContacts.clickMerge();
		new GuidewireHelpers(driver).logout();
		AdvancedSearchAB searchAB = new AdvancedSearchAB(driver);
		if(mergedContacts.get(0).getIsCompany()) {
			Assert.assertFalse(searchAB.multipleResults(AbUserHelper.getRandomDeptUser("Policy Service"), null, mergedContacts.get(0).getLastNameOrCompanyName(), mergedContacts.get(0).getAddresses().get(0).getLine1(), mergedContacts.get(0).getAddresses().get(0).getState()), "After merging contacts, there should only be one contact.");
			searchAB.searchCompanyByName(mergedContacts.get(0).getLastNameOrCompanyName(), mergedContacts.get(0).getAddresses().get(0).getLine1(), State.Idaho);
		} else {
			Assert.assertFalse(searchAB.multipleResults(AbUserHelper.getRandomDeptUser("Policy Service"), mergedContacts.get(0).getFirstName(), mergedContacts.get(0).getLastNameOrCompanyName(), mergedContacts.get(0).getAddresses().get(0).getLine1(), mergedContacts.get(0).getAddresses().get(0).getState()), "After merging a contact there should only be one contact.");
			searchAB.searchByFirstLastName(mergedContacts.get(0).getFirstName(), mergedContacts.get(0).getLastNameOrCompanyName(), mergedContacts.get(0).getAddresses().get(0).getLine1());
		}
		ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		String uiAccount = contactPage.getContactDetailsBasicsAccountNumber();
		if(!mergedContacts.get(0).getAccountNumber().contains(uiAccount)) {
			Assert.fail("The Account Number should be on the merged contact after the merge.");
		}

		PageLinks tabLinks = new PageLinks(driver);
		tabLinks.clickContactDetailsBasicsAddressLink();
		ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
		ArrayList<String> abAddresses =  addressPage.getAddresses();
		boolean address1Found = false;
		boolean address2Found = false;
		for(String address : abAddresses) {
				if(address.contains(myContactObj.addresses.get(1).getLine1())) {
					address2Found = true;
				} else if(address.contains(myContactObj.addresses.get(0).getLine1())) {
					address1Found = true;
				}
		}
		if(!address1Found) {
			Assert.fail("The retired Contacts address: "+myContactObj.addresses.get(0)+" was not found in the kept contact.  See Account: "  + myPolicyObjPL.accountNumber);
		}
		if(!address2Found){
			Assert.fail("The retired Contacts address: "+myContactObj.addresses.get(1)+" was not found in the kept contact. See Account: "  + myPolicyObjPL.accountNumber);
		}
	}
	
//	@Test
	public void grabRandomMatchForTestGeneration() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		PotentialDuplicateContacts dupContacts = new PotentialDuplicateContacts(driver);
		
		ReviewContacts reviewContacts = dupContacts.clickRandomMatch();
		reviewContacts.setMergedContactObjects();
		reviewContacts.setAddressInfo();
		ArrayList<MergeContact> mergedContacts = reviewContacts.clickMerge();
		new GuidewireHelpers(driver).logout();
		AdvancedSearchAB searchAB = new AdvancedSearchAB(driver);
		if(mergedContacts.get(0).getIsCompany()) {
			Assert.assertFalse(searchAB.multipleResults(AbUserHelper.getRandomDeptUser("Policy Service"), null, mergedContacts.get(0).getLastNameOrCompanyName(), mergedContacts.get(0).getAddresses().get(0).getLine1(), mergedContacts.get(0).getAddresses().get(0).getState()), "After merging contacts, there should only be one contact.");
//			searchAB.searchCompanyByName(companyName, address, state);
		} else {
			Assert.assertFalse(searchAB.multipleResults(AbUserHelper.getRandomDeptUser("Policy Service"), mergedContacts.get(0).getFirstName(), mergedContacts.get(0).getLastNameOrCompanyName(), mergedContacts.get(0).getAddresses().get(0).getLine1(), mergedContacts.get(0).getAddresses().get(0).getState()), "After merging a contact there should only be one contact.");
//			searchAB.searchByFirstLastName(firstName, lastName, address);
		}
		ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		
		
	}
	
	@Test
	public void potentialDupPage() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);
        SidebarAB sideMenu = new SidebarAB(driver);
        sideMenu.clickMergeContacts(AbUserHelper.getUserByUserName("su"));
		PotentialDuplicateContacts dupContacts = new PotentialDuplicateContacts(driver);
		ArrayList<String> types = dupContacts.getTypes();
		if(types.size()<10) {
			Assert.fail("The Types dropdown should exist with contact roles.");
		}
	}
}
