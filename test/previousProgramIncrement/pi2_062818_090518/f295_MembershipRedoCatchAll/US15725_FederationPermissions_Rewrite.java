package previousProgramIncrement.pi2_062818_090518.f295_MembershipRedoCatchAll;


import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property;
import repository.gw.enums.RewriteType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.rewrite.StartRewrite;
import scratchpad.evan.SideMenuPC;

import java.util.ArrayList;
import java.util.Calendar;

/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description 
* @DATE Sep 20, 2018
*/
public class US15725_FederationPermissions_Rewrite extends BaseTest {

	GeneratePolicy myPolObj;
	WebDriver driver;

	@Test
	public void validateFederationPermissions() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		generatePolicy(RewriteType.FullTerm);
		new Login(driver).loginAndSearchPolicy_asUW(myPolObj);
		myPolObj.cancelPolicy(CancellationSourceReasonExplanation.ExcessiveErrors, "NO POLICY FOR YOU!!!!!!", null, true);
		myPolObj.squire.setPolicyCanceled(true);
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchPolicy_asUW(myPolObj);
		new StartRewrite(driver).startRewrite(RewriteType.FullTerm);
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchAccountByAccountNumber("dashton", "gw", myPolObj.accountNumber);
		new AccountSummaryPC(driver).clickWorkOrderbyJobType(TransactionType.Rewrite_Full_Term);
		softAssert.assertFalse(new PolicySummary(driver).finds(By.xpath("//span[text()='Policy Info']/parent::div/parent::td[not(contains(@class, 'g-disabled'))]")).isEmpty(), "FEDERATION USER WAS NOT ABLE TO VIEW POLICY INFO PAGE ON REWRITE JOB");
		softAssert.assertFalse(new PolicySummary(driver).finds(By.xpath("//span[text()='Membership']/parent::div/parent::td[not(contains(@class, 'g-disabled'))]")).isEmpty(), "FEDERATION USER WAS NOT ABLE TO VIEW MEMBERSHIP PAGE ON REWRITE JOB");
		softAssert.assertTrue(new PolicySummary(driver).finds(By.xpath("//span[text()='Section III - Auto']/parent::div/parent::td")).isEmpty(), "FEDERATION USER WAS ABLE TO VIEW Section III - Auto PAGE ON REWRITE JOB");

		new SideMenuPC(driver).clickSideMenuMembershipMembershipType();
		softAssert.assertFalse(new SideMenuPC(driver).finds(By.xpath("//div[contains(text(), 'Associate')]")).isEmpty(), "MEMBERSHIP TYPE WAS EDITABLE TO A FEDERATION USER ON REWRITE JOB");
		new SideMenuPC(driver).clickSideMenuMembershipMembers();
		softAssert.assertTrue(new SideMenuPC(driver).finds(By.xpath("//div[contains(@id, ':MembershipMembersPanelSet:0')]//*[contains(text(), 'Edit')]")).isEmpty(), "MEMBER CONTACT WAS EDITABLE TO A FEDERATION USER ON REWRITE JOB");

		softAssert.assertAll();

	}


	@Test
	public void validateFederationPermissions_Rewrite_MembershipOnly() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);


		myPolObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Membership)
				.build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicyByAccountNumber("ctaft", "gw", myPolObj.accountNumber);
		myPolObj.cancelPolicy(CancellationSourceReasonExplanation.OtherOther, "NO POLICY FOR YOU!!!!!!", null, true);
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchAccountByAccountNumber("dashton", "gw", myPolObj.accountNumber); 
		new AccountSummaryPC(driver).clickPolicyNumber(myPolObj.membership.getPolicyNumber());
		new PolicyMenu(driver).clickMenuActions();
		Assert.assertFalse(new GuidewireHelpers(driver).finds(By.xpath("//a[contains(@id, ':RewriteFullTerm-itemEl')]")).isEmpty(), "FED USER WAS UNABLE TO START A REWRITE ON A MEMBERSHIP ONLY POLICY");
		new GuidewireHelpers(driver).clickProductLogo();
		new StartRewrite(driver).rewriteFullTerm(myPolObj);
		Assert.assertFalse(new GuidewireHelpers(driver).finds(By.xpath("//span[contains(text(), 'Rewrite Full Term Submitted')]")).isEmpty(), "FED USER WAS UNABLE TO COMPLETE THE REWRITE JOB ON MEMEBERSHIP OPNLY POLICY");
	}

	@Test
	public void validateFederationPermissions_Rewrite_Squire() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		generatePolicy(RewriteType.FullTerm);
		new Login(driver).loginAndSearchPolicy_asUW(myPolObj);
		myPolObj.cancelPolicy(CancellationSourceReasonExplanation.ExcessiveErrors, "NO POLICY FOR YOU!!!!!!", null, true);
		myPolObj.squire.setPolicyCanceled(true);
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchAccountByAccountNumber("dashton", "gw", myPolObj.accountNumber); 
		new AccountSummaryPC(driver).clickPolicyNumber(myPolObj.squire.getPolicyNumber());
		new ActionsPC(driver).click_Actions();
		Assert.assertTrue(new GuidewireHelpers(driver).finds(By.xpath("//a[contains(@id, ':RewriteFullTerm-itemEl')]")).isEmpty(), "FED USER WAS ABLE TO START A REWRITE ON A SQUIRE POLICY");
	}



	private void generatePolicy(RewriteType rewriteType) throws Exception {
		Calendar calendar = Calendar.getInstance();
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth % 2 == 0) {
			generateSquireAuto(rewriteType);
		} else {
			generateSquireProperty(rewriteType);
		}
	}


	private void generateSquireAuto(RewriteType rewriteType) throws Exception {
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
				.withPaymentPlanType(PaymentPlanType.Annual)
				.forRewrite(driver, rewriteType)
				.build(GeneratePolicyType.PolicyIssued);
	}

	private void generateSquireProperty(RewriteType rewriteType) throws Exception {
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
				.withPaymentPlanType(PaymentPlanType.Annual)
				.forRewrite(driver, rewriteType)
				.build(GeneratePolicyType.PolicyIssued);
	}













}
