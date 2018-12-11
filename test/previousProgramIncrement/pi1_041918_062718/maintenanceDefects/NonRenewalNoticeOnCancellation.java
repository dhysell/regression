package previousProgramIncrement.pi1_041918_062718.maintenanceDefects;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;

/**
 * @Author nvadlamudi
 * @Requirement: DE7439: Non-Renewal notice IWUW 10 0107 0917 not inferring on cancellation 
 * 				 STEPS: Have an existing policy (this will be for both PL and CL) 
 * 						Move to renewal -80 days, 
 * 						run the 'policy renewal start' batch Make sure the renewal job is in 'renewing' status 
 * 						move to renewal -50 days, run 'workflow' to bind the renewal job 
 * 						move 1 to 2 more days and start a cancel in the current term 
 * 						Choose Source= Carrier; Reason = Policy Non Renewed; Reason Explanation = any from the drop down
 *               		Click Start Cancellation and then choose either 'Schedule Cancel' or ' Cancel Now' from the submit options drop down
 * @Description: Validate the Non Renewal Notice: IWUW 10 0107 09 17 showing up
 *               in policy document page
 * @DATE May 03, 2018
 */
public class NonRenewalNoticeOnCancellation extends BaseTest {
	private GeneratePolicy myPolicyObj;
	private String renewalDocument = "Non Renewal Notice";
	private WebDriver driver;
	private Config cf;

	@Test
	public void testNonRenewalNoticeOnCancellation() throws Exception {
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		int RandomJobs = 1;//NumberUtils.generateRandomNumberInt(1, 2);
		Underwriters uw = null;
		switch (RandomJobs) {
		case 1:
			testIssueSquirePol();
			uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
					Underwriter.UnderwriterTitle.Underwriter);
			testBatchesForRenewal(uw);
			break;
		case 2:
			testIssueBOPPol();	
			uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Commercial,
					Underwriter.UnderwriterTitle.Underwriter);
			break;
		}

		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);
		AccountSummaryPC summary = new AccountSummaryPC(driver);
		summary.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);

		// Cancel policy
		StartCancellation cancelPol = new StartCancellation(driver);	
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.CondemnedUnsafePremises,	"Testing purpose", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), false);
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickViewYourPolicy();

		PolicySummary summaryPage = new PolicySummary(driver);
		String cancelTransNumber = summaryPage.getPendingPolicyTransactionByColumnName(TransactionType.Cancellation.getValue(), "Transaction #");
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsDocuments();
		PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo(cancelTransNumber);
		docs.clickSearch();
		ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();
		Assert.assertTrue(descriptions.contains(renewalDocument), "Policy Number: " + myPolicyObj.squire.getPolicyNumber() + " - Renewal Document '" + renewalDocument + "' is not displayed in Documents -> Job '" + cancelTransNumber);

	}

	private void testIssueBOPPol() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		int randomYear = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);

		PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, true);
		PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
		myline.setAdditionalCoverageStuff(myLineAddCov);

		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(randomYear);
		loc1Bldg1.setClassClassification("storage");

		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));


		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("DE7439 RenewalCancel")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(myline)
				.withPolTermLengthDays(50)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Commercial,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolicyObj.accountNumber);
		StartRenewal renewal = new StartRenewal(driver);
		renewal.startRenewal();
		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
		
		new GuidewireHelpers(driver).logout();
	}

	private void testIssueSquirePol() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty plBuilding = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locOnePropertyList.add(plBuilding);
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;
		mySquire.squirePA = squirePersonalAuto;

		myPolicyObj = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("RenewalPL", "DE7439")
				.withPolTermLengthDays(50)
				.withPolOrgType(OrganizationType.Individual)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	private void testBatchesForRenewal( Underwriters uw) throws Exception {
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolicyObj.accountNumber);
		StartRenewal renewal = new StartRenewal(driver);
		renewal.renewPolicyManually(RenewalCode.Renew_Good_Risk, myPolicyObj);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
		
		new GuidewireHelpers(driver).logout();
	}

}
