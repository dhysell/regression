package previousProgramIncrement.pi3_090518_111518.nonFeatures.ARTists;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderMembershipMembers;

/**
* @Author nvadlamudi
* @Requirement :US16548: Add a tooltip & a block when a user tries to add a member with a janky account number
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764/detail/userstory/256674144008">Link Text</a>
* @Description : validating the membership member - OK button availability if two contacts with same accounts.Validating tooltip.
* @DATE Oct 2, 2018
*/
public class US16548MembershipMemberAccountNumberTooltip extends BaseTest {
	private GeneratePolicy myPrevObjPL;

	@Test
	public void testIssueSquirepolicyWithTwoMembers() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		
		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Delete" + StringsUtils.generateRandomNumberDigits(8),
				"ANI" + StringsUtils.generateRandomNumberDigits(8), AdditionalNamedInsuredType.Spouse,
				new AddressInfo(true)) {
			{
				this.setNewContact(CreateNew.Create_New_Always);
			}
		});
		
		Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());

		myPrevObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withANIList(listOfANIs)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Member", "Dupli")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
  }
	
	@Test(dependsOnMethods = {"testIssueSquirepolicyWithTwoMembers"})
	public void testPolicyChangeUWIssuesInformational() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		
		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"New" + StringsUtils.generateRandomNumberDigits(8),
				"ANI" + StringsUtils.generateRandomNumberDigits(8), AdditionalNamedInsuredType.Spouse,
				new AddressInfo(true)) {
			{
				this.setNewContact(CreateNew.Create_New_Always);
			}
		});
		
		Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withANIList(listOfANIs)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Verisk", "Screen")
				.build(GeneratePolicyType.FullApp);
		
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
		new GuidewireHelpers(driver).editPolicyTransaction();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuMembershipMembers();
		GenericWorkorderMembershipMembers membershipMembers = new GenericWorkorderMembershipMembers(driver);
		membershipMembers.clickSearch();
		SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
	    addressBook.searchAddressBookByFirstLastName(myPolicyObjPL.basicSearch, myPolicyObjPL.aniList.get(0).getPersonFirstName(), myPolicyObjPL.aniList.get(0).getPersonLastName(), null, null, null, null, CreateNew.Do_Not_Create_New);
	   
	    //Ensure that the user is stopped if the account # is the same as any other member
	    Assert.assertTrue(membershipMembers.isMemberOKButtonDisabled(), "New Membership contact having duplicate/PNI Account able to select OK button");
	   
	    //Ensure that the tooltip says this: This contact needs their own account number.  Please contact Policy Services
	    Assert.assertTrue(membershipMembers.getMemberOKButtonTooltip().contains("This contact needs their own account number.  Please contact Policy Services."), "Expected tooltip 'This contact needs their own account number.  Please contact Policy Services.' is not displayed.");
	    membershipMembers.clickCancel();
	    membershipMembers.clickSearch();
	    addressBook.searchAddressBookByFirstLastName(myPrevObjPL.basicSearch, myPrevObjPL.aniList.get(0).getPersonFirstName(), myPrevObjPL.aniList.get(0).getPersonLastName(), null, null, null, null, CreateNew.Do_Not_Create_New);
	    membershipMembers.clickOK();
	    membershipMembers.clickNext();
	    sideMenu.clickSideMenuMembershipMembers();
	    membershipMembers.clickSearch();
	    
	    //Ensure that the user is not stopped if the account number shows up correctly
	    addressBook.searchAddressBookByFirstLastName(myPrevObjPL.basicSearch, myPrevObjPL.pniContact.getFirstName(), myPrevObjPL.pniContact.getLastName(), null, null, null, null, CreateNew.Do_Not_Create_New);
	   
	    //Ensure that the user is stopped if the account # is the same as any other member
	    Assert.assertTrue(membershipMembers.isMemberOKButtonDisabled(), "New Membership contact having duplicate/PNI Account able to select OK button");
	   
	    //Ensure that the tooltip says this: This contact needs their own account number.  Please contact Policy Services
	    Assert.assertTrue(membershipMembers.getMemberOKButtonTooltip().contains("This contact needs their own account number.  Please contact Policy Services."), "Expected tooltip 'This contact needs their own account number.  Please contact Policy Services.' is not displayed.");
	    membershipMembers.clickCancel();
	    membershipMembers.clickGenericWorkorderSaveDraft();
	}
}
