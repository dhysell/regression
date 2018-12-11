package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_PriorLosses;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;

import java.util.ArrayList;

/**
* @Author nvadlamudi
* @Requirement :US15803: Disable/change UW Issues - Policy Level
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:x:/s/ARTists2/EfkD5tWiAwlAq85ITIpy3M8B9Hg5URkky9PGfOGjeHJQnA?e=u0CR2b">UW Issues to Change for Auto Issue</a>
* @Description : validate new UW Issues (SQ064, SQ065, SQ066) in membership Branch
* @DATE Jul 20, 2018
*/
public class US15803DisableMembershipPolicyLevelUW extends BaseTest {
	private WebDriver driver;
	SoftAssert softAssert = new SoftAssert();

	@Test
	public void testMembershipPolicyLevelUWIssuesInSubmission() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		 driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		// Additional Interest
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		loc1LNBldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc1LNBldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		loc1LNBldg1AddInterest.setLoanContractNumber("LN12345");
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		PLPolicyLocationProperty plBuilding = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		plBuilding.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		
		locOnePropertyList.add(plBuilding);
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;

		Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
		mySquire.propertyAndLiability = myPropertyAndLiability;

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.Membership)
				.withInsFirstLastName("PolLevel", "Block")
				.isDraft()
				.build(GeneratePolicyType.FullApp);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
		new GuidewireHelpers(driver).editPolicyTransaction();
		SideMenuPC sideMenu = new SideMenuPC(driver);

		// SQ064 Additional Interest payer on membership dues Change to block
		// bind
//		sideMenu.clickSideMenuPayerAssignment();
		GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
		String payerName = myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0)
				.getAdditionalInterestList().get(0).getCompanyName();
		payerName = payerName.substring(0, payerName.length() - 5);
//		payerAssignment.setPayerAssignmentBillMembershipDues(myPolicyObjPL.pniContact.getFirstName(), true, false,
//				payerName);
//		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
//		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
//		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
//		if (quotePage.isPreQuoteDisplayed()) {
//			quotePage.clickPreQuoteDetails();
//		}
//		sideMenu.clickSideMenuRiskAnalysis();
//		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
//
//		PLUWIssues uwBlockBindExpected = PLUWIssues.AdditionalInterestOnMembershipDues;
//		softAssert.assertFalse(
//				!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
//				"Expected Blocking Quote : " + uwBlockBindExpected.getShortDesc() + " is not displayed");

		// SQ065 Membership Dues on Payer Assignment Change to block bind
		new GuidewireHelpers(driver).editPolicyTransaction();
		sideMenu.clickSideMenuPayerAssignment();
		payerAssignment.setPayerAssignmentBillMembershipDues(myPolicyObjPL.pniContact.getFirstName(), true, false,
				"Insured");
		payerAssignment.setPayerAssignmentBillLiabilityCoverages("General Liability", true, false, payerName);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.Quote();
		quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}

		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		PLUWIssues uwBlockBindExpected = PLUWIssues.MembershipDuesOnPayerAssignment;
		softAssert.assertFalse(
				!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
				"Expected Blocking Quote : " + uwBlockBindExpected.getShortDesc() + " is not displayed");

		softAssert.assertAll();
	}

	@Test
	public void testMembershipPolicyLevelUWIssuesInPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		// Additional Interest
		AdditionalInterest loc1Property1AdditionalInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc1Property1AdditionalInterest.setFirstMortgage(true);
		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		property1.setBuildingAdditionalInterest(loc1Property1AdditionalInterest);
		
		PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		

		locOnePropertyList.add(property1);
		locOnePropertyList.add(property2);
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = myLiab;

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;

		GeneratePolicy mySquirePolicy = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.Membership)
				.withInsFirstLastName("UWIssue", "Member").withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicyByAccountNumber(mySquirePolicy.agentInfo.getAgentUserName(),
				mySquirePolicy.agentInfo.getAgentPassword(), mySquirePolicy.accountNumber);
		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Testing Purpose",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

		String payerName = mySquirePolicy.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0)
				.getAdditionalInterestList().get(0).getCompanyName();
		payerName = payerName.substring(0, payerName.length() - 5);
		
		// SQ066	Membership Dues on Payer Assignment for policy change	Change to block bind
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
	    propertyDetail.addExistingAdditionalInterest(payerName);
	    GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lienholder");
        additionalInterests.checkBuildingsPropertyAdditionalInterestsFirstMortgage(true);
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
        propertyDetail.clickOK();
        propertyDetail.clickOkayIfMSPhotoYearValidationShows();
		sideMenu.clickSideMenuPayerAssignment();
		GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
		payerAssignment.setPayerAssignmentBillMembershipDues(mySquirePolicy.pniContact.getFirstName(), true, false,
				"Insured");
		payerAssignment.setPayerAssignmentBillLiabilityCoverages("General Liability", true, false, payerName);
		payerAssignment.setPayerAssignmentBillLiabilityCoverages("Additional Residence", true, false, payerName);
		
		payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(1, 2, payerName, true, false);
		payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		PLUWIssues uwBlockBindExpected = PLUWIssues.MembershipDuesOnPayerAssigmmentForChange;
		softAssert.assertFalse(
				!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
				"Expected Blocking Quote : " + uwBlockBindExpected.getShortDesc() + " is not displayed");

		softAssert.assertAll();
	}

}