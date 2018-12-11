package regression.r2.noclock.contactmanager.address;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsHistoryAB;
import repository.ab.pagelinks.PageLinks;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactMembershipType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
public class AddressStandardization extends BaseTest {
	private WebDriver driver;
	private GenerateContact newContact;
	private AbUsers user;
	private AddressInfo nonStandardAddress = new AddressInfo("Collins St", "", "Pocatello",State.Idaho,"83201",CountyIdaho.Bingham,"United States", AddressType.Home);
	
	@AfterMethod(alwaysRun = true)
	public void afterMethod() throws Exception {

	}
	
	/**
	* @Author sbroderick
	* @Requirement Address Standardization has been changed.
	* @RequirementsLink <a href="https://rally1.rallydev.com/#/10075774537ud/detail/userstory/49436185680">Remove Address Standardization Error Popup</a>
	* @Description Address Standardization errors will be handled with a Validation Results popup.
	* @DATE April 22, 2016
	* @throws Exception
	*/
	
	@Test
	public void addNewContactNonStandardAddress() throws Exception{
		
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        this.newContact = new GenerateContact.Builder(driver)
		.withFirstLastName("Bat", "Man")
		.withPrimaryAddress(nonStandardAddress)
		.withMembershipType(ContactMembershipType.Associate)
		.withGenerateAccountNumber(true)
		.withCreator(user)
		.build(GenerateContactType.Person);
		
		verifyStandardizedDate(newContact.accountNumber, newContact.lastName, false, "Primary Address Added",newContact.addresses.get(0));	
	}
	
	@Test
	public void verifyAddressStandardizationHistory() throws Exception {
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		ArrayList<AddressInfo> addresses = new ArrayList<AddressInfo>();
		addresses.add(new AddressInfo(true));
		addresses.add(new AddressInfo(true));
		addresses.add(new AddressInfo(true));
		addresses.add(new AddressInfo(true));

        this.newContact = new GenerateContact.Builder(driver)
		.withFirstLastName("Bat", "Man")
		.withAddresses(addresses)
		.withMembershipType(ContactMembershipType.Other)
		.withGenerateAccountNumber(true)
		.withCreator(user)
		.build(GenerateContactType.Person);

        PageLinks links = new PageLinks(driver);
		links.clickContactDetailsBasicsHistoryLink();
        ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB(driver);
		ArrayList<String> verifyHistoryResults = new ArrayList<>();
		for(int i = 0; i<newContact.addresses.size(); i++) {
			if(i==0) {
				if(!historyPage.verifyHistoryItemExists("Primary Address Added", this.user.getUserFirstName() +" "+this.user.getUserLastName(), DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.ContactManager, "EEE, MMM d, yyyy"), "Primary Address Added " + newContact.addresses.get(i).getLine1() + ", " + newContact.addresses.get(i).getCity()+", "+ newContact.addresses.get(i).getState().getAbbreviation()+" "+newContact.addresses.get(i).getZip(), true)) {
					Assert.fail("The Address Standardized history item was not found for the address: " + newContact.addresses.get(i).getLine1() + ", " + newContact.addresses.get(i).getCity()+", " + newContact.addresses.get(i).getState().getAbbreviation() + " " + newContact.addresses.get(i).getZip() + "on account " +this.newContact.accountNumber );
				}
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Primary Address Added " + addresses.get(i).getLine1(), "State", " ", addresses.get(i).getState().getName());
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "State");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Primary Address Added " + addresses.get(i).getLine1(), "Standardized On", " ", DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.ContactManager, "EEE MMM dd "));
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "Standardized On");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Primary Address Added " + addresses.get(i).getLine1(), "Standardized By", " ", this.user.getUserFirstName() + " " + this.user.getUserLastName());
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "Standardized By");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Primary Address Added " + addresses.get(i).getLine1(), "Standardize?", "false", "true");
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "Standardize?");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Primary Address Added " + addresses.get(i).getLine1(), "Postal Code", " ", addresses.get(i).getZip());
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "Postal Code");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Primary Address Added " + addresses.get(i).getLine1(), "Office Number", " ", "00");
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "Office Number");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Primary Address Added " + addresses.get(i).getLine1(), "Description", " ", "Default Address");
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "Default Address");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Primary Address Added " + addresses.get(i).getLine1(), "City", " ", addresses.get(i).getCity());
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "City");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Primary Address Added " + addresses.get(i).getLine1(), "Address 1", " ", addresses.get(i).getLine1());	
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "Address 1");
			} else {
				if(!historyPage.verifyHistoryItemExists("Address Added", this.user.getUserFirstName() +" "+this.user.getUserLastName(), DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.ContactManager, "EEE, MMM d, yyyy"), "Secondary Address Added " + newContact.addresses.get(i).getLine1() + ", " + newContact.addresses.get(i).getCity()+", "+ newContact.addresses.get(i).getState().getAbbreviation()+" "+newContact.addresses.get(i).getZip(), true)) {
					Assert.fail("The Address Standardized history item was not found for the address: " + newContact.addresses.get(i).getLine1() + ", " + newContact.addresses.get(i).getCity()+", " + newContact.addresses.get(i).getState().getAbbreviation() + " " + newContact.addresses.get(i).getZip() + "on account " +this.newContact.accountNumber );
				}
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Secondary Address Added " + addresses.get(i).getLine1(), "State", " ", addresses.get(i).getState().getName());
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "State");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Secondary Address Added " + addresses.get(i).getLine1(), "Standardized On", " ", DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.ContactManager, "EEE MMM dd "));
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "Standardized On");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Secondary Address Added " + addresses.get(i).getLine1(), "Standardized By", " ", this.user.getUserFirstName() + " " + this.user.getUserLastName());
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "Standardized By");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Secondary Address Added " + addresses.get(i).getLine1(), "Standardize?", "false", "true");
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "Standardized?");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Secondary Address Added " + addresses.get(i).getLine1(), "Postal Code", " ", addresses.get(i).getZip());
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "Postal Code");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Secondary Address Added " + addresses.get(i).getLine1(), "Office Number", " ", "00");
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "Office Number");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Secondary Address Added " + addresses.get(i).getLine1(), "Description", " ", "Default Address");
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "Default Address");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Secondary Address Added " + addresses.get(i).getLine1(), "City", " ", addresses.get(i).getCity());
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "City");
				verifyHistoryResults = historyPage.verifyHistory("Address Added", "Secondary Address Added " + addresses.get(i).getLine1(), "Address 1", " ", addresses.get(i).getLine1());	
				inspectHistoryResults(verifyHistoryResults, addresses.get(i), "Address Line 1");
			}
		}
	}
	
	private void inspectHistoryResults(ArrayList<String> verifyHistoryResults, AddressInfo address, String changeField) {
		for(String result : verifyHistoryResults) {
			if(result.contains("The Changed Field is expected to be ")) {
				Assert.fail("The "+ address.getLine1() + "address does not have the correct "+changeField+". Please investigate.");
			} else if(result.contains("The Old Value Field is expected to be ")){
				Assert.fail("The "+ address.getLine1() + "address does not have the correct previous value for the "+changeField+". Please investigate.");
			} else if(result.contains("The New Value Field is expected to be ")) {
				Assert.fail("The "+ address.getLine1() + "address does not have the correct new value for the "+changeField+". Please investigate.");
			} else if(result.contains("Field no found in Change Results.")){
				Assert.fail("The "+ address.getLine1() + "address does not have the field "+changeField+". Please investigate.");
			}
		}
    }

	@Test
	public void addressStandardization() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        ArrayList<PLPolicyLocationProperty> locTwoPropertyList = new ArrayList<PLPolicyLocationProperty>();
        
        ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
        PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();

        AdditionalInterest loc1Bldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
        loc1Bldg2AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        loc1Bldg2AddInterest.setFirstMortgage(true);
        loc1Bldg2AdditionalInterests.add(loc1Bldg2AddInterest);
        loc1Bldg1.setPolicyLocationBuildingAdditionalInterestArrayList(loc1Bldg2AdditionalInterests);
        loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);
        locOnePropertyList.add(loc1Bldg1);
        locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo(true)));
        
        PLPolicyLocationProperty loc2Bldg1 = new PLPolicyLocationProperty();
        loc2Bldg1.setPolicyLocationBuildingAdditionalInterestArrayList(loc1Bldg2AdditionalInterests);
        loc2Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);
        locTwoPropertyList.add(loc2Bldg1);
        locationsList.add(new PolicyLocation(locTwoPropertyList, new AddressInfo(true)));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy countrySquire = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Squire", "AdditionalInterest")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
		driver.quit();		
		
        cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		verifyStandardizedDate(countrySquire.accountNumber, countrySquire.pniContact.getLastName(), true, "Primary Address Added", countrySquire.pniContact.getAddress());
//		verifyStandardizedDate(countrySquire.accountNumber, countrySquire.pniContact.getLastName(), true, "Address Added", countrySquire.pniContact.getAddress() );
	}
	
	@Test
	public void testUpdateAddressStandardized() throws Exception {
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		ArrayList<AddressInfo> addresses = new ArrayList<AddressInfo>();
		addresses.add(new AddressInfo(true));
		
		this.newContact = new GenerateContact.Builder(driver)
		.withFirstLastName("Bat", "Man")
		.withAddresses(addresses)
		.withMembershipType(ContactMembershipType.Other)
		.withCreator(user)
		.build(GenerateContactType.Person);
		
		new GuidewireHelpers(driver).logout();
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.loginAndSearchContact(user, this.newContact.firstName, this.newContact.lastName, this.newContact.addresses.get(0).getLine1(), this.newContact.addresses.get(0).getState());
        ContactDetailsBasicsAB basics = new ContactDetailsBasicsAB(driver);
		basics.clickContactDetailsBasicsAddressLink();
        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
		addressPage.clickContactDetailsAdressesEditLink();
		addressPage.setContactDetailsAddressesAddressLine1("Collins St");
		addressPage.clickContactDetailsAddressesUpdate();
        ErrorHandling errors = new ErrorHandling(driver);
		boolean found = errors.checkIfValidationResultsExists();
		if(!found) {
			Assert.fail("After attempting to standardize an address that can't be standardized, the Validation Results message should appear.");
	}
	}
	
	public void verifyStandardizedDate(String acctNum, String name, boolean shouldStandardize, String historyType, AddressInfo address) throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        ContactDetailsBasicsAB basics = searchMe.loginAndSearchContactByAcct(this.user, acctNum, name);
		String standardized = basics.getStandardizedDateTime();
		if(!standardized.isEmpty()) {
			if(!(verifyHistoryItem(historyType) == shouldStandardize)) {
					Assert.fail("The UI should show that the address was standardized in the history.");
			}
		} else {
			if(shouldStandardize) {
				Assert.fail("The UI should show that the address was standardized.");
			}
		}
		if(shouldStandardize) {
			verifyAddressStandardizationHistoryItem(historyType, historyType + " " + address.getLine1(), address, shouldStandardize);
		} else {
			verifyAddressStandardizationHistoryItem(historyType, historyType + " " + address.getLine1(), address, shouldStandardize);
	}
	}
	
	public boolean verifyHistoryItem(String historyItem) {
        PageLinks tabLinks = new PageLinks(driver);
		tabLinks.clickContactDetailsBasicsHistoryLink();
        ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB(driver);
		ArrayList<String> historyItems = historyPage.getHistoryByType(historyItem);
		if(historyItems.size()>1) {
			Assert.fail("Ensure that the Address was standardized only once.");
        } else return historyItems.size() == 1;
		return false;
	}
	
	public void verifyAddressStandardizationHistoryItem(String type, String description, AddressInfo address, boolean shouldStandardize) {
        PageLinks links = new PageLinks(driver);
		links.clickContactDetailsBasicsHistoryLink();
        ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB(driver);
		historyPage.verifyStandardizedAddress(type, description, address, shouldStandardize);
		
		
	}
}
