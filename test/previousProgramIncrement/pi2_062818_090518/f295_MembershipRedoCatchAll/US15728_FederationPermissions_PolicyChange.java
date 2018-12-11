package previousProgramIncrement.pi2_062818_090518.f295_MembershipRedoCatchAll;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderMembershipMembers;
import scratchpad.evan.SideMenuPC;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description FEDERATION PERMISSIONS
* @DATE Sep 20, 2018
*/
public class US15728_FederationPermissions_PolicyChange extends BaseTest {


	GeneratePolicy myPolObj;
	WebDriver driver;

	//Ensure that a Federation user can start a policy change on any type of PL policy
	@Test
	public void validateFederationPermissions_SubmitPolicyChange() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		generatePolicy();
		new Login(driver).loginAndSearchAccountByAccountNumber("dashton", "gw", myPolObj.accountNumber); 
		new AccountSummaryPC(driver).clickPolicyNumber(myPolObj.squire.getPolicyNumber());
		new StartPolicyChange(driver).startPolicyChange("TINKERIN WITH STUFFS", null);
		//Ensure that the Federation user can only view the Policy Info screen and the membership screens in the Policy change transaction on any insurance policy.
		if(myPolObj.lineSelection.contains(LineSelection.PropertyAndLiabilityLinePL)) {
			softAssert.assertTrue(new GuidewireHelpers(driver).finds(By.xpath("//span[contains(@class, 'x-tree-node') and contains(text(), 'Sections I & II')]")).isEmpty(), "FED USER WAS ABLE TO SEE SECTIONS 1 & 2 ON A POLICY CHANGE.");
		} else if (myPolObj.lineSelection.contains(LineSelection.PersonalAutoLinePL)) {
			softAssert.assertTrue(new GuidewireHelpers(driver).finds(By.xpath("//span[contains(@class, 'x-tree-node') and contains(text(), 'Sections III')]")).isEmpty(), "FED USER WAS ABLE TO SEE SECTIONS 3 ON A POLICY CHANGE.");
		}
		new SideMenuPC(driver).clickSideMenuMembershipMembers();
		//Ensure that the Federation user can only edit the membership line.
		new GenericWorkorderMembershipMembers(driver).clickEditMembershipMember(myPolObj.pniContact);
		new GenericWorkorderMembershipMembers(driver).clickOK();
		new GenericWorkorder(driver).clickGenericWorkorderQuote();
		//Ensure that the Federation user can quote and submit the change.
		softAssert.assertFalse(new GenericWorkorder(driver).finds(By.xpath("//div[contains(@class, 'message') and contains(text(), 'Quote Successful! But ViewMultiLineQuote is not viewable per your permissions')]")).isEmpty(), "FED USER DID NOT GET VALIDATION MESSAGE SAYING THAT THEY CANNOT VIEW QUOTE SCREEN");
		
		softAssert.assertAll();
	}
	
	
	@Test
	public void validateFederationPermissions_OOSPolicyChange() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		PLPolicyLocationProperty myProperty = new PLPolicyLocationProperty(Property.PropertyTypePL.ResidencePremises);
		ArrayList<PLPolicyLocationProperty> propertyList = new ArrayList<PLPolicyLocationProperty>();
		propertyList.add(myProperty);
		PolicyLocation myLocation = new PolicyLocation(propertyList);
		ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>();
		locationList.add(myLocation);
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationList;

		Squire mySquire = new Squire();
		mySquire.propertyAndLiability = myPropertyAndLiability;

		myPolObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withSquire(mySquire)
				.withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -5))
				.build(GeneratePolicyType.PolicyIssued);
		new Login(driver).loginAndSearchPolicy_asAgent(myPolObj);
		
		myPolObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getPropertyCoverages().getCoverageA().setLimit(200000);
		myPolObj.updateProperty(myPolObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0), DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 5));
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchAccountByAccountNumber("dashton", "gw", myPolObj.accountNumber); 
		new AccountSummaryPC(driver).clickPolicyNumber(myPolObj.squire.getPolicyNumber());
		new StartPolicyChange(driver).startPolicyChange("TINKERIN WITH STUFFS", null);
		
		List<WebElement> offeringsList = new GuidewireHelpers(driver).finds(By.xpath("//div[contains(@id, 'ProductOffersDV:ProductSelectionLV')]/div/div/table/tbody/child::tr"));
		softAssert.assertFalse(offeringsList.size() > 1, "FED USER HAD MORE THAN ONE OPTION FOR CREATING A NEW SUBMISSION");
		softAssert.assertFalse(offeringsList.isEmpty(), "FED USER DID NOT HAVE ANY OPTIONS WHEN CREATING A NEW SUBMISSION");
		
		
	}
	
	

	private void generatePolicy() throws Exception {
		Calendar calendar = Calendar.getInstance();
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth % 2 == 0) {
			generateSquireAuto();
		} else {
			generateSquireProperty();
		}
	}


	private void generateSquireAuto() throws Exception {
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit.FiftyHigh, repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit.TenK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = squirePersonalAuto;

		myPolObj = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.Membership)
				.withInsPersonOrCompany(ContactSubType.Person)
				.build(GeneratePolicyType.PolicyIssued);
	}

	private void generateSquireProperty() throws Exception {
		PLPolicyLocationProperty myProperty = new PLPolicyLocationProperty(Property.PropertyTypePL.ResidencePremises);
		ArrayList<PLPolicyLocationProperty> propertyList = new ArrayList<PLPolicyLocationProperty>();
		propertyList.add(myProperty);
		PolicyLocation myLocation = new PolicyLocation(propertyList);
		ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>();
		locationList.add(myLocation);
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationList;

		Squire mySquire = new Squire();
		mySquire.propertyAndLiability = myPropertyAndLiability;


		myPolObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withSquire(mySquire)
				.build(GeneratePolicyType.PolicyIssued);
	}


















}
