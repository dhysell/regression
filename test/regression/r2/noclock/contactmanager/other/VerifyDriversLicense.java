package regression.r2.noclock.contactmanager.other;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.DriverLicenseNumbers;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.DriverLicenseNumbersHelpers;
import regression.r2.noclock.contactmanager.contact.NewPersonTest;

/**
 * @Author sbroderick
 * @Requirement Link coming soon.
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description Link to DL rules. <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/CRS/Technical%20Information/MVR%20Order%20Requirements_2015-12-01.pdf"> Link to DL validation rules Document</a>
 * @DATE Jul 24, 2017
 */
public class VerifyDriversLicense extends BaseTest {
	private WebDriver driver;
	AbUsers abUser = null;
	GenerateContact myContact = null;
	
	@Test
	public void testDLNumbers() throws Exception{
		NewPersonTest newPerson = new NewPersonTest();
		newPerson.testNewPerson();
		this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
		this.myContact = newPerson.getPersonContact();
		new GuidewireHelpers(driver).logout();
		validateDLNumbersInvalid();	
		validateDLNumbersValid();
	}
	
	public void validateDLNumbersInvalid() throws Exception{
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.loginAndSearchContact(this.abUser, myContact.firstName, myContact.lastName, myContact.addresses.get(0).getLine1(), myContact.addresses.get(0).getState());

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsEditLink();
		contactPage.setDateOfBirth("10/10/1980");
		contactPage.clickContactDetailsBasicsUpdateLink();
		contactPage.clickContactDetailsBasicsEditLink();
		ArrayList<State> states = State.valuesOriginal50();

//		Used for debugging purposes of a specific state.
/*		ArrayList<State> states = new ArrayList<State>();
		states.add(State.Oregon);
		states.add(State.Pennsylvania);
*/
 		State stateToCheck;

		for (State st : states) {
            contactPage = new ContactDetailsBasicsAB(driver);
			stateToCheck = st;
			contactPage.setDLState(stateToCheck.getName());
			contactPage.setDL("");
            List<DriverLicenseNumbers> results = DriverLicenseNumbersHelpers.getDLNumbers(st);
			for (DriverLicenseNumbers dln : results) {
                contactPage = new ContactDetailsBasicsAB(driver);
				System.out.println("Testing validation rules for State: " + st + ".");
				String validNumber = dln.getDlnumber()+"A1B2C3";
                contactPage.setDL(validNumber); // valid number
                Assert.assertEquals(contactPage.checkIfInvalidDriversLicenseMessageExists(), true, "ERROR: For State " + stateToCheck.getAbbreviation() + " and Valid Number: " + validNumber + " Error message was not showing and should.");
				contactPage.clickContactDetailsBasicsUpdateLink();
                Assert.assertEquals(contactPage.checkIfInvalidDriversLicenseMessageExists(), true, "ERROR: For State " + stateToCheck.getAbbreviation() + " and Valid Number: " + validNumber + " Error message was not showing and should.");
                contactPage = new ContactDetailsBasicsAB(driver);
				contactPage.clickContactDetailsBasicsEditLink();
			}
		}
		new GuidewireHelpers(driver).logout();
	}
	
	public void validateDLNumbersValid() throws Exception{
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
		searchMe.loginAndSearchContact(this.abUser, "Stor", "Andan", "261 Collins St", State.Idaho);
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsEditLink();
		contactPage.setDateOfBirth("10/10/1980");
		contactPage.clickContactDetailsBasicsUpdateLink();
		contactPage.clickContactDetailsBasicsEditLink();
		ArrayList<State> states = State.valuesOriginal50();
		State stateToCheck;

		for (State st : states) {
            contactPage = new ContactDetailsBasicsAB(driver);
			stateToCheck = st;
			contactPage.setDLState(stateToCheck.getName());
			contactPage.setDL("");
            List<DriverLicenseNumbers> results = DriverLicenseNumbersHelpers.getDLNumbers(st);
			for (DriverLicenseNumbers dln : results) {
                contactPage = new ContactDetailsBasicsAB(driver);
				System.out.println("Testing validation rules for State: " + st + ".");
				String validNumber = dln.getDlnumber();
                contactPage.setDL(validNumber);
                Assert.assertEquals(contactPage.checkIfInvalidDriversLicenseMessageExists(), false, "ERROR: For State " + stateToCheck.getAbbreviation() + " and Valid Number: " + validNumber + " Error message was showing and should not appear.");
				contactPage.clickContactDetailsBasicsUpdateLink();
                Assert.assertEquals(contactPage.checkIfInvalidDriversLicenseMessageExists(), false, "ERROR: For State " + stateToCheck.getAbbreviation() + " and Valid Number: " + validNumber + " Error message was showing and should not appear.");
                contactPage = new ContactDetailsBasicsAB(driver);
				contactPage.clickContactDetailsBasicsEditLink();
			}
		}
		new GuidewireHelpers(driver).logout();
	}
	
//	@Test
	public void lowercaseDL() throws Exception{
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
        PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();

        AdditionalInterest loc1Bldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
        loc1Bldg2AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        loc1Bldg2AddInterest.setFirstMortgage(true);
        loc1Bldg2AdditionalInterests.add(loc1Bldg2AddInterest);
        loc1Bldg1.setPolicyLocationBuildingAdditionalInterestArrayList(loc1Bldg2AdditionalInterests);
        loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);
        locOnePropertyList.add(loc1Bldg1);
        locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy newPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Squire", "AdditionalInterest")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
		
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");

        Login logMeIn = new Login(driver);
		logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchByFirstLastName(newPolicy.pniContact.getFirstName(), newPolicy.pniContact.getLastName(),
				newPolicy.pniContact.getAddress().getLine1());

        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		String dl = basicsPage.getDLNumber();
		System.out.println(dl.toUpperCase());
		System.out.println("Compare dl " + dl + " to " + dl.toUpperCase());
		org.testng.Assert.assertSame(dl, dl.toUpperCase(), "Drivers Licence should be uppercase.");
		
		basicsPage.clickContactDetailsBasicsEditLink();
		basicsPage.setDL(dl.toLowerCase());
		
		basicsPage.clickContactDetailsBasicsUpdateLink();
		String editedDl = basicsPage.getDLNumber();
		
		org.testng.Assert.assertSame(editedDl, editedDl.toUpperCase(), "Drivers Licence should be uppercase.");			
	}

}
