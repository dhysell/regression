package previousProgramIncrement.pi2_062818_090518.f80_Membershipredo_DocumentInference;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.policy.PolicyDocuments;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;

/**
* @Author nvadlamudi
* @Requirement :US15862: Do not infer or generate the membership renewal letter on any flat cancels
* @RequirementsLink <a href="http://projects.idfbins.com/thunderhead/Documents/R2%20Thunderhead%20Project/Federation/Renewal%20Documents/Membership%20Renewal%20Requirement.docx">Document Requirements</a>
* @Description : Validate Membership Renewal is not shown in documents page for Flat Cancels
* @DATE Jul 30, 2018
*/
public class US15862MembershipRenewalOnFlatCancel extends BaseTest {
	private static final String membershipRenew = "Membership Renewal";
	private GeneratePolicy mySqPolObj;

	@Test
	public void testCheckUWFlatCancelMembershipRenewal() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh,
				MedicalLimit.TenK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = squirePersonalAuto;

		mySqPolObj = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.Membership)
				.withInsPersonOrCompany(ContactSubType.Person).withInsFirstLastName("MembRenewal", "MidTerm")
				.withPolOrgType(OrganizationType.Individual).withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.PolicyIssued);

		// Cancel Policy
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				mySqPolObj.accountNumber);
		StartCancellation cancelPol = new StartCancellation(driver);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NoPaymentReceived, null,
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), true, 200.00);
		GenericWorkorderComplete submittedPage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());
		submittedPage.clickViewYourPolicy();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsDocuments();
		PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo("Cancel");
		docs.setDocumentDescription(membershipRenew);
		docs.clickSearch();
		ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();
		boolean docFound = false;
		for (String desc : descriptions) {
			if (desc.equals(membershipRenew)) {
				docFound = true;
				break;
			}
		}
		Assert.assertTrue(!docFound, "Policy Number: '" + mySqPolObj.squire.getPolicyNumber() + "' - Document '"
				+ membershipRenew + "' is displayed for Job : Flat Cancel.");
		new GuidewireHelpers(driver).logout();
	}
	
	@Test
	public void testCheckFlatCancelMembershipRenewal() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh,
				MedicalLimit.TenK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = squirePersonalAuto;

		mySqPolObj = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.Membership)
				.withInsPersonOrCompany(ContactSubType.Person).withInsFirstLastName("MembRenewal", "MidTerm")
				.withPolOrgType(OrganizationType.Individual).withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.PolicyIssued);

		// Cancel Policy
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				mySqPolObj.accountNumber);
		StartCancellation cancelPol = new StartCancellation(driver);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.ExcessiveErrors, "Testing Purpose",
				null, true);
		GenericWorkorderComplete submittedPage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());
		submittedPage.clickViewYourPolicy();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsDocuments();
		PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo("Cancel");
		docs.setDocumentDescription(membershipRenew);
		docs.clickSearch();
		ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();
		boolean docFound = false;
		for (String desc : descriptions) {
			if (desc.equals(membershipRenew)) {
				docFound = true;
				break;
			}
		}
		Assert.assertTrue(!docFound, "Policy Number: '" + mySqPolObj.squire.getPolicyNumber() + "' - Document '"
				+ membershipRenew + "' is displayed for Job : Flat Cancel.");
		new GuidewireHelpers(driver).logout();
	}
}
