package regression.r2.noclock.contactmanager.contact;


import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class AccountNumbersAssignedToKids extends BaseTest {
	private WebDriver driver;
	GeneratePolicy myPolicyObj = null;
	
	public void squireAuto() throws Exception {
		// driver
		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact kid = new Contact("Jr", "Abignale", Gender.Male, DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -16));
//		RelationshipToInsured
		kid.setRelationToInsured(RelationshipToInsured.Child);
//		kid.setRelationPolicyMember(RelationshipToInsuredPolicyMember.Child);
//		kid.setRelationPolicyMember(RelationshipToInsuredPolicyMember.Child);
//		kid.setRelationshipToInsuredCPP(RelationshipToInsuredCPP.Child);
		driversList.add(kid);
		
		
		// vehicle
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
		vehicleList.add(vehicle);

		// line auto coverages
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
		coverages.setUnderinsured(false);

		// whole auto line
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        Squire mySquire = new Squire();
        mySquire.squirePA = squirePersonalAuto;

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("Frank", "Abignale")
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test
	public void checkKidContactForAcctNum() throws Exception {
		squireAuto();
		AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy Services");
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		ArrayList<String> names = searchMe.validateAccountNumberNotAssignedToKid(abUser, myPolicyObj.accountNumber);
		for(String name : names) {
			if(name.contains(myPolicyObj.squire.policyMembers.get(1).getFirstName())){
				Assert.fail("The kid should not have the account number listed for his contact.");
			}
		}
		new GuidewireHelpers(driver).logout();
        searchMe = new AdvancedSearchAB(driver);
		searchMe.loginAndSearchContact(abUser, myPolicyObj.squire.policyMembers.get(1).getFirstName(), myPolicyObj.squire.policyMembers.get(1).getLastName(), myPolicyObj.squire.policyMembers.get(1).getAddress().getLine1(), myPolicyObj.squire.policyMembers.get(1).getAddress().getState());
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		String acctNum = basicsPage.getContactDetailsBasicsAccountNumber();
		if(acctNum!=null) {
			Assert.fail("Under age contacts should not have parents account number.");
		}
	}
}
