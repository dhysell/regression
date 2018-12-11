package previousProgramIncrement.pi2_062818_090518.f295_MembershipRedoCatchAll;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.WaitUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;

import java.util.List;

/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description ENSURE FED USER CAN CREATE A MOP ACCOUNT
* @DATE Sep 20, 2018
*/
public class US15727_FederationPermissions_SubmissionIssuance extends BaseTest {

	
	GeneratePolicy myPolObj;
	WebDriver driver;

	@Test(enabled=false)
	public void validateFederationPermissions_FED_CreateAccount() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		myPolObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Membership)
				.asAlternateUser("dashton", "gw")
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	//Ensure that the Federation user can issue only a Membership Only policy and no other policy type 
	//Ensure that the fed user can only submit/issue a Membership Only policy started themselves or another fed user
	@Test
	public void validateFederationPermissions_FED_CompleteSubmission() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		myPolObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Membership)
				.asAlternateUser("dashton", "gw")
				.build(GeneratePolicyType.FullApp);
		
		new Login(driver).loginAndSearchAccountByAccountNumber("tmoore", "gw", myPolObj.accountNumber);
		new AccountSummaryPC(driver).clickAccountSummaryPendingTransactionByProduct(ProductLineType.Membership);
		new GenericWorkorder(driver).clickGenericWorkorderQuote();
		new SideMenuPC(driver).clickSideMenuForms();
		new SideMenuPC(driver).clickSideMenuPayment();
		new GenericWorkorderPayment(driver).fillOutPaymentPage(myPolObj);
		new GenericWorkorder(driver).clickWhenClickable(new GenericWorkorder(driver).find(By.xpath("//span[contains(@id, ':BindOptions-btnEl')]")));
		new GenericWorkorder(driver).clickWhenClickable(new GenericWorkorder(driver).find(By.xpath("//div[contains(@id, ':BindAndIssue')]")));
		new GenericWorkorder(driver).selectOKOrCancelFromPopup(OkCancel.OK);
		new WaitUtils(driver).waitForPostBack();
		Assert.assertFalse(new GenericWorkorder(driver).finds(By.xpath("//span[contains(@id, 'JobComplete:JobCompleteScreen:ttlBar')]")).isEmpty(), "ISSUANCE JOB DID NOT COMMPLETE WHEN ISSUED BY A DIFFERENT FED USER THAN THE ONE THAT STARTED IT.");
	}
	
	//Ensure that a Federation user only has the option to submit a Membership Only policy when choosing Submission.
	@Test
	public void validateFederationPermissions_FED_CreateMembershipOnly() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		Contact myContact = new Contact();
		myContact.setPersonOrCompany(ContactSubType.Company);
		
		new Login(driver).login("dashton", "gw");
		new TopMenuPolicyPC(driver).clickNewSubmission();
		new SubmissionNewSubmission(driver).fillOutFormSearchAndEitherCreateNewOrUseExisting(true, CreateNew.Create_New_Always, ContactSubType.Company, null, null, null, myContact.getCompanyName(), "Pocatello", State.Idaho, "83201");
		new SubmissionCreateAccount(driver).setAgentNumber();
		new SubmissionCreateAccount(driver).setSubmissionCreateAccountBasicsTIN(String.valueOf(NumberUtils.generateRandomNumberDigits(9)));
		new SubmissionCreateAccount(driver).createNewContact(myContact);
		
		List<WebElement> offeringsList = new GuidewireHelpers(driver).finds(By.xpath("//div[contains(@id, 'ProductOffersDV:ProductSelectionLV')]/div/div/table/tbody/child::tr"));
		Assert.assertFalse(offeringsList.size() > 1, "FED USER HAD MORE THAN ONE OPTION FOR CREATING A NEW SUBMISSION");
		Assert.assertFalse(offeringsList.isEmpty(), "FED USER DID NOT HAVE ANY OPTIONS WHEN CREATING A NEW SUBMISSION");
	}
	
	//Ensure that the fed user can only submit/issue a Membership Only policy started themselves or another fed user
	@Test(enabled = true)
	public void validateFederationPermissions_FED_StartedAgentAccount() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		myPolObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Membership)
				.build(GeneratePolicyType.FullApp);
		
		new Login(driver).loginAndSearchAccountByAccountNumber("dashton", "gw", myPolObj.accountNumber);
		new AccountSummaryPC(driver).clickAccountSummaryPendingTransactionByProduct(ProductLineType.Membership);
		new GenericWorkorder(driver).clickGenericWorkorderQuote();
		new SideMenuPC(driver).clickSideMenuForms();
		new GenericWorkorderPayment(driver).fillOutPaymentPage(myPolObj);
		boolean submitButtonActive = new GuidewireHelpers(driver).checkIfElementExists("//a[contains(@id, ':BindOptions') and not(contains(@class, 'x-disabled'))]", 5000);
		Assert.assertTrue(submitButtonActive, "FED USER WAS ABLE TO COMPETE A MEMEBERSHIP ONLY POLICY STARTED BY AN AGENT");
		
		
	}
	
	//Ensure that the Federation user can issue only a Membership Only policy and no other policy type 
	@Test
	public void validateFederationPermissions_FED_SubmitAgentAccount() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit.FiftyHigh, repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit.TenK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.getVehicleList().get(0).setGaragedAt(null);

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = squirePersonalAuto;

		myPolObj = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.Membership)
				.withInsPersonOrCompany(ContactSubType.Person)
				.build(GeneratePolicyType.FullApp);
		
		new Login(driver).loginAndSearchAccountByAccountNumber("dashton", "gw", myPolObj.accountNumber);
		new AccountSummaryPC(driver).clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
		softAssert.assertTrue(new GuidewireHelpers(driver).finds(By.xpath("//span[contains(@id, ':BindOptions-btnInnerEl')]")).isEmpty(), "FED USER WAS ABLE TO SEE THE SUBMIT OPTIONS BUTTON ON A SQUIRE POLICY STARTED BY AN AGENT");
		softAssert.assertTrue(new GuidewireHelpers(driver).finds(By.xpath("//span[contains(@id, ':CloseOptions-btnInnerEl')]")).isEmpty(), "FED USER WAS ABLE TO SEE THE CLOSE OPTIONS BUTTON ON A SQUIRE POLICY STARTED BY AN AGENT");
		new GuidewireHelpers(driver).editPolicyTransaction();
		new GuidewireHelpers(driver).waitForPostBack();
        softAssert.assertTrue(new GuidewireHelpers(driver).getFirstErrorMessage().contains("You do not have the permission required to perform this action:"), "FED USER WAS NOT STOPPED FROM EDITING A SQUIRE SUBMISSION SUBMITTED BY AN AGENT");
        softAssert.assertAll();
		
		
	}
	
	
	
	
	
	
	
	
	
}
