package previousProgramIncrement.pi1_041918_062718.maintenanceDefects;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.policy.PolicyDocuments;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * @Author nvadlamudi
 * @Requirement: DE7479: ***HOT FIX*** Lien Address Change on auto not
 *               generating AI decs STEPS: Have a Squire policy with Section 3
 *               Have an addtl interest on at least on vehicle., Complete a
 *               policy change to change the lienholder address on the vehicle.
 *               Bind and issue the change. Go to documents
 * @Description: Validaing the Lien address changes should trigger a new
 *               additional interest dec.
 * @DATE May 07, 2018
 */
public class LienAddressChangeAutoForAIDec extends BaseTest {
	private GeneratePolicy myPolicyObj;
	private Underwriters uw;
	private String AIDocument = "Additional Interest Declarations";
	private String ai1Name;
	private AddressInfo ai1Address;
	private WebDriver driver;

	@Test
	public void testPolicyChangeWithAIAddress() throws Exception {
		
		//Issue Squire policy 
		issueSquireAuto();
		
		// changing the address from AB
		contactManagerLienAddressChange();
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);

		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolicyObj.accountNumber);
		Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", currentSystemDate);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();
		GenericWorkorderVehicles_Details vehicalTab = new GenericWorkorderVehicles_Details(driver);
		vehicalTab.selectDriverTableSpecificRowByText(1);
		AdditionalInterest autoAI = new AdditionalInterest(ContactSubType.Company);
		autoAI.setCompanyName(
				myPolicyObj.squire.squirePA.getVehicleList().get(0).getAdditionalInterest().get(0).getCompanyName());
		autoAI.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		autoAI.setAddress(ai1Address);
		autoAI.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIIIAuto);
		vehicalTab.removeVehicleAdditionalInterestByName(autoAI.getCompanyName());
		vehicalTab.addAutoAdditionalInterest(true, autoAI);
		vehicalTab.clickOK();
		
		sideMenu.clickSideMenuRiskAnalysis();
		policyChangePage.quoteAndIssue();
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickPolicyNumber();
	
		sideMenu.clickSideMenuToolsDocuments();
		PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo("Change");
		docs.setDocumentDescription(AIDocument);
		docs.clickSearch();
		ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();
		boolean docFound = false;
		for (String desc : descriptions) {
			if (desc.equals(AIDocument)) {
				docFound = true;
				break;
			}
		}

		ArrayList<String> aiNames = docs.getDocumentNameAddress();
		boolean ai1Found = false;
		for (String aiName : aiNames) {
			if (aiName.contains(ai1Name)) {
				ai1Found = true;
			}
		}
		Assert.assertTrue(docFound, "Policy Number: '" + myPolicyObj.squire.getPolicyNumber() + "' - Document '"
				+ AIDocument + "' is not displayed for Additional Interest Address change.");
		Assert.assertTrue(ai1Found, "Policy Number: '" + myPolicyObj.squire.getPolicyNumber()
				+ "' - Document page for '" + AIDocument + "' not showing Additional Interest Name :" + ai1Name + ".");
 }

	public void issueSquireAuto() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

		GenerateContact newLienholder = new GenerateContact.Builder(driver)
				.withCompanyName("DE7479"+NumberUtils.generateRandomNumberDigits(6))
				.withRoles(rolesToAdd)
				.withUniqueName(true)
				.withGeneratedLienNumber(true)
				.build(GenerateContactType.Company);
		driver.quit();
		
		AdditionalInterest autoAI = new AdditionalInterest(newLienholder.companyName, newLienholder.addresses.get(0));
		autoAI.setLienholderNumber("");
		autoAI.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		autoAI.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
				MedicalLimit.TenK);
		coverages.setUnderinsured(false);

		// Vehicle
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		autoAI.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIIIAuto);
		ArrayList<AdditionalInterest> autoAIList = new ArrayList<AdditionalInterest>();
		autoAIList.add(autoAI);
		Vehicle toAdd = new Vehicle();
		toAdd.setVehicleTypePL(VehicleTypePL.PrivatePassenger);
		toAdd.setAdditionalInterest(autoAIList);
		
		vehicleList.add(toAdd);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = squirePersonalAuto;

		myPolicyObj = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PersonalAutoLinePL).withInsFirstLastName("DE7479", "LienAdd")
				.withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		driver.quit();

	}
	private void contactManagerLienAddressChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		ai1Name = myPolicyObj.squire.squirePA.getVehicleList().get(0).getAdditionalInterest().get(0).getCompanyName();
		ArrayList<AddressInfo> addresses1 = new ArrayList<AddressInfo>();
		ai1Address = new AddressInfo(true);
		addresses1.add(myPolicyObj.squire.squirePA.getVehicleList().get(0).getAdditionalInterest().get(0).getAddress());
		addresses1.add(ai1Address);
		
		AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");
//		new Login(driver).login(user.getUserName(), user.getUserPassword());
		TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab(user);

		AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);

		// Changing 1st address
		searchMe.searchCompanyByName(ai1Name,
				myPolicyObj.squire.squirePA.getVehicleList().get(0).getAdditionalInterest().get(0).getAddress()
						.getLine1(),
				myPolicyObj.squire.squirePA.getVehicleList().get(0).getAdditionalInterest().get(0).getAddress()
						.getState());
		ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsEditLink();
		basicsPage.clickContactDetailsBasicsAddressLink();
		ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
		addressPage.addAddresses(addresses1);
		addressPage.clickContactDetailsAddressesUpdate();
		driver.quit();
	}

}
