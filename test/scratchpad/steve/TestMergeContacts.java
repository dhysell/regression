package scratchpad.steve;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.ab.contact.ReviewContacts;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.search.PotentialDuplicateContacts;
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

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

/**
* @Author sbroderick
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description 
* @DATE Nov 1, 2018
*/
public class TestMergeContacts extends BaseTest {
	
	private WebDriver driver;
	
//	This is a work in progress.	
//	@Test
	public void testMergeContacts() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        
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
	
        GenerateContact myContactObj = new GenerateContact.Builder(driver)
				.withCreator(user)
				.withFirstLastName(myPolicyObjPL.pniContact.getFirstName(), myPolicyObjPL.pniContact.getMiddleName(), myPolicyObjPL.pniContact.getLastName())
				.withPrimaryAddress(myPolicyObjPL.pniContact.getAddress())
				.withTIN(myPolicyObjPL.pniContact.getTaxIDNumber())
				.build(GenerateContactType.Person);

		System.out.println("accountNumber: " + myContactObj.accountNumber);
		new GuidewireHelpers(driver).logout();
        
		new Login(driver).login(superUser.getUserName(), superUser.getUserPassword());
		new BatchHelpers(cf).runBatchProcess(BatchProcess.DuplicateContactsFinder);
		new GuidewireHelpers(driver).logout(); 
		PotentialDuplicateContacts dupContacts = new PotentialDuplicateContacts(driver);
		ReviewContacts reviewContacts = dupContacts.reviewPotentialDuplicateContact(superUser, myPolicyObjPL.pniContact.getLastName(), myPolicyObjPL.pniContact.getAddress().getLine1()+", "+myPolicyObjPL.pniContact.getAddress().getCity()+", ", "Exact", false);
		reviewContacts.setMergedContactObjects();
		reviewContacts.setAddressInfo();ArrayList<MergeContact> mergedContacts = reviewContacts.clickMerge();
		new GuidewireHelpers(driver).logout();
		AdvancedSearchAB searchAB = new AdvancedSearchAB(driver);
		if(mergedContacts.get(0).getIsCompany()) {
			Assert.assertFalse(searchAB.multipleResults(AbUserHelper.getRandomDeptUser("Policy Service"), null, mergedContacts.get(0).getLastNameOrCompanyName(), mergedContacts.get(0).getAddresses().get(0).getLine1(), mergedContacts.get(0).getAddresses().get(0).getState()), "After merging contacts, there should only be one contact.");
		} else {
			Assert.assertFalse(searchAB.multipleResults(AbUserHelper.getRandomDeptUser("Policy Service"), mergedContacts.get(0).getFirstName(), mergedContacts.get(0).getLastNameOrCompanyName(), mergedContacts.get(0).getAddresses().get(0).getLine1(), mergedContacts.get(0).getAddresses().get(0).getState()), "After merging a contact there should only be one contact.");
		}
	}
	
	@Test
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
		} else {
			Assert.assertFalse(searchAB.multipleResults(AbUserHelper.getRandomDeptUser("Policy Service"), mergedContacts.get(0).getFirstName(), mergedContacts.get(0).getLastNameOrCompanyName(), mergedContacts.get(0).getAddresses().get(0).getLine1(), mergedContacts.get(0).getAddresses().get(0).getState()), "After merging a contact there should only be one contact.");
		}
	}
}
