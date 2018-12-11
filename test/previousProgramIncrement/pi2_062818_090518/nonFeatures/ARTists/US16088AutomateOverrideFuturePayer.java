package previousProgramIncrement.pi2_062818_090518.nonFeatures.ARTists;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_PriorLosses;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author nvadlamudi
* @Requirement :US16088: ***HOT FIX*** Automate Override Future Payer box
* @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Common-Admin/PC8%20-%20Common%20-%20QuoteApplication%20-%20Payer%20Assignment.xlsx">PC8 - Common - QuoteApplication - Payer Assignment</a>
* @Description : Validate OFP checkbox with agent and UW Logins
* @DATE Aug 29, 2018
*/
public class US16088AutomateOverrideFuturePayer extends BaseTest {
   private GeneratePolicy myPolicyObjPL;
  
  @Test
  public void testIssueAndFirstPolicyChangeSquirepolicy() throws Exception {	  
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		// Additional Interest
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc1LNBldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		loc1LNBldg1AddInterest.setLoanContractNumber("LN12345");
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		PLPolicyLocationProperty plBuilding = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		plBuilding.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);

		locOnePropertyList.add(plBuilding);
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locationsList.add(locToAdd);
		
		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability.locationList = locationsList;	
		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -10);
		
		
		 myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.Membership)
				.withPolEffectiveDate(newEff)
				.withInsFirstLastName("Automate", "OFP")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),myPolicyObjPL.squire.getPolicyNumber());

        // First Change
        Date firstChangeDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Day, 20);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("First Policy Change", firstChangeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(
				driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.removeAdditionalInterestAsFirstMortgage();
        GenericWorkorderAdditionalInterests additionalInterest = new GenericWorkorderAdditionalInterests(driver);
    
        AdditionalInterest ai = new AdditionalInterest(ContactSubType.Company);
		ai.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		ai.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		ai.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		ai.setLoanContractNumber("LN46785");		
		ai.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIProperty);
		
        additionalInterest.fillOutAdditionalInterest(true, ai);
        propertyDetail.clickOK();
		
		sideMenu.clickSideMenuPayerAssignment();
		GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
		payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(1, 1, ai.getCompanyName(), true, false);
		payerAssignment.setPayerAssignmentBillLiabilityCoverages("General Liability", true, false, ai.getCompanyName());
		
		payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
			risk.performRiskAnalysisAndQuote(myPolicyObjPL);
		}		
		policyChangePage.clickIssuePolicy();
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
	}
	
	@Test(dependsOnMethods = {"testIssueAndFirstPolicyChangeSquirepolicy"})
	public void testCheckAutomateOverrideFutureDefault() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.squire.getPolicyNumber());
		// initiate OSS Policy change

		Date firstChangeDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, 2);
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Second Policy Change", firstChangeDate);
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(
				driver);
		propertyDetail.clickViewOrEditBuildingButton(1);
		propertyDetail.removeAdditionalInterestAsFirstMortgage();
		GenericWorkorderAdditionalInterests additionalInterest = new GenericWorkorderAdditionalInterests(driver);

		AdditionalInterest ai = new AdditionalInterest(ContactSubType.Company);
		ai.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		ai.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		ai.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		ai.setLoanContractNumber("LN46785");
		ai.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIProperty);

		additionalInterest.fillOutAdditionalInterest(true, ai);
		propertyDetail.clickOK();
		sideMenu.clickSideMenuSquirePropertyCoverages();
		GenericWorkorderSquirePropertyAndLiabilityCoverages propCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(
				driver);
		propCoverages.clickSectionIICoveragesTab();
		GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages addCoverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(
				driver);
		
		addCoverage.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300_500_100);
		sideMenu.clickSideMenuPayerAssignment();
		GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
		payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);
		
		//Ensure that the box is automatically checked if it is available.
		Assert.assertTrue(payerAssignment.getSectionIPropertyOFPCheckboxSelectedReadOnly(1, 1).contains("Yes"), "Section I - Property Override Future Payer (OFP) is not selected" );
		Assert.assertTrue(payerAssignment.getSectionIIPropertyOFPCheckboxSelectedReadOnly(1, "General Liability (Acres)").contains("Yes"), "Section II - Liability Override Future Payer (OFP) is not selected" );
	
		new GuidewireHelpers(driver).logout();
		
		//Ensure that the box is editable by UW & AR peeps.  
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),myPolicyObjPL.squire.getPolicyNumber());
        new PolicySummary(driver).clickPendingTransaction(TransactionType.Policy_Change);
        
         sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPayerAssignment();
        payerAssignment = new GenericWorkorderPayerAssignment(driver);
       
        payerAssignment.selectSectionIPropertyOFPCheckbox(1, 1, false);
        payerAssignment.selectSectionIIPropertyOFPCheckbox(1, "General Liability (Acres)", false);
        payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);
        
        sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
			risk.performRiskAnalysisAndQuote(myPolicyObjPL);
		}		
	    policyChangePage = new StartPolicyChange(driver);		
		policyChangePage.clickIssuePolicy();
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
	}
	
	
}
