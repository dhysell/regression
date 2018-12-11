package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.ARTists;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyScheduledItemType;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author nvadlamudi
* @Requirement :DE7922: ***HOT FIX*** Keep getting error "You cannot change a locked branch"
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764/detail/defect/253949487916">Link Text</a>
* @Description : Validate OSS changes on renewal and nothing is blocked for renewal and changes
* @DATE Sep 18, 2018
*/
public class DE7922OSSChangeOnRenewal extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySquirePol;
  @Test
  public void testIssuePolicyWithRenewal() throws Exception {
	  		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
			WebDriver driver = buildDriver(cf);
			Date centerDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
			// GENERATE POLICY
			ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
			ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

			locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
			locationsList.add(new PolicyLocation(locOnePropertyList));
			
			SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
			myPropertyAndLiability.locationList = locationsList;

			SquireInlandMarine myInlandMarine = new SquireInlandMarine();
			PersonalPropertyScheduledItem myItem = new PersonalPropertyScheduledItem();
			myItem.setParentPersonalPropertyType(PersonalPropertyType.Jewelry);
			myItem.setType(PersonalPropertyScheduledItemType.Ring);
			myItem.setDescription("Big @$$ rock!!!");
			myItem.setAppraisalDate(DateUtils.dateAddSubtract(centerDate, DateAddSubtractOptions.Year, -7));
			myItem.setPhotoUploadDate(DateUtils.dateAddSubtract(centerDate, DateAddSubtractOptions.Year, -7));
			myItem.setLimit(25000);

			PersonalProperty myPersonalProperty = new PersonalProperty();
			myPersonalProperty.setType(PersonalPropertyType.Jewelry);
			myPersonalProperty.setLimit(25000);
			myPersonalProperty.setDeductible(PersonalPropertyDeductible.Ded5Perc);
			myPersonalProperty.getScheduledItems().add(myItem);
			myInlandMarine.personalProperty_PL_IM.add(myPersonalProperty);
			myInlandMarine.inlandMarineCoverageSelection_PL_IM.add(InlandMarine.PersonalProperty);

			Squire mySquire = new Squire(SquireEligibility.City);
			mySquire.propertyAndLiability = myPropertyAndLiability;
			mySquire.inlandMarine = myInlandMarine;

			mySquirePol = new GeneratePolicy.Builder(driver)
	                .withSquire(mySquire)
	                .withProductType(ProductLineType.Squire)
	                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL,LineSelection.PersonalAutoLinePL)
	                .withInsFirstLastName("Check", "Incept")
	                .withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -287))
	                .withPaymentPlanType(PaymentPlanType.Annual)
	                .withDownPaymentType(PaymentType.Cash)
					.build(GeneratePolicyType.PolicyIssued);
			
			 // run the batch job to get renewal job
	        new BatchHelpers(driver).runBatchProcess(BatchProcess.Policy_Renewal_Start);
  }
  
  @Test(dependsOnMethods = {"testIssuePolicyWithRenewal"})
  public void testCancelReinstatePolicyTerm() throws Exception {
      Config cf = new Config(ApplicationOrCenter.PolicyCenter);
      driver = buildDriver(cf);
      Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
              Underwriter.UnderwriterTitle.Underwriter);

      new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
    		  mySquirePol.accountNumber);
      
      Date firstChangeDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, 20);
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Second Policy Change", firstChangeDate);
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(
				driver);
		propertyDetail.clickViewOrEditBuildingButton(1);
		GenericWorkorderAdditionalInterests additionalInterest = new GenericWorkorderAdditionalInterests(driver);

		AdditionalInterest ai = new AdditionalInterest(ContactSubType.Company);
		ai.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		ai.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		ai.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		ai.setLoanContractNumber("LN46785");
		ai.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIProperty);
		SoftAssert softAssert = new SoftAssert();
		
		additionalInterest.fillOutAdditionalInterest(true, ai);
		propertyDetail.clickOK();
		softAssert.assertFalse(new BasePage(driver).finds(By.xpath("//label[contains(text(), 'You cannot change a locked branch')]")).isEmpty(), 
				"MESSAGES 'ILLEGALARGUMENTEXCEPTION: You cannot change a locked branch' ON AN OOS CHANGE");
		
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
		sideMenu.clickSideMenuRiskAnalysis();
		softAssert.assertFalse(new BasePage(driver).finds(By.xpath("//label[contains(text(), 'You cannot change a locked branch')]")).isEmpty(), 
				"MESSAGES 'ILLEGALARGUMENTEXCEPTION: You cannot change a locked branch' ON AN OOS CHANGE");
		
		policyChangePage.quoteAndIssue();
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickApplyPolicyChangeToFuturePeriodsLink();
		new GenericWorkorderRiskAnalysis(driver).clickClose();
		PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
		preRenewalPage.closePreRenewalExplanations(mySquirePol);
		
		PolicySummary policySummary = new PolicySummary(driver);
		policySummary.clickPendingTransaction(TransactionType.Renewal);
		sideMenu.clickSideMenuPayerAssignment();
		payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		risk.approveAll_IncludingSpecial();
		StartRenewal renewal = new StartRenewal(driver);
		renewal.clickRenewPolicy(RenewalCode.Renew_Good_Risk);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
	     
  }
}
