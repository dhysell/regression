package previousProgramIncrement.pi1_041918_062718.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderModifiers;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.rewrite.StartRewrite;

import java.util.ArrayList;
import java.util.Date;

/**
 * @Author ecoleman
 * @Requirement 
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/defect/219201892088">DE7487</a>
 * @Description 
Production

Scenario 1:

    Have inforce policy that is in the first term. This means loyalty discount is 0%
    Do policy change of any type some days in the future.
    Continue to move clock and make some random changes. 

Actual: See that on the policy change the loyalty discount has changed to being 1 year of coverage. Each policy change is adding another year and will start increasing the discount. Yet insured has not had a policy for a year yet.
Expected: The loyalty discount should only update at each renewal.

Scenario 2:

    Start new submission with new person, bind & issue.
    Policy change cancel and RFT changing inception dates to give client 10 years of loyalty. Issue.
    Do policy change few more weeks into policy.

Actual: See that on the policy change the loyalty discount has changed to being 11 years of coverage. Another year was added.
Expected: The loyalty discount should only update at each renewal. Should still be only at 10 years.


Requirements link(s): PC8 - Squire -QuoteApplication - Modifiers (Informational UI Mockup tab)

Discounts spreadsheet - Policy Discounts tab


See attached screenshots.
 * @DATE May 9, 2018
 */

@Test(groups= {"ClockMove"})
public class DE7487_LoyaltyDiscount extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObject = null;

	
	@Test(enabled = true)
	public void scenario1VerifyLoyaltyDiscountNotChangedOnPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));
		SquireLiability liabilitySection = new SquireLiability();
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = liabilitySection;
		Squire mySquire = new Squire();
		mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObject = new GeneratePolicy.Builder(driver)
		.withDownPaymentType(PaymentType.Cash)
		.withSquire(mySquire)
		.withProductType(ProductLineType.Squire)
		.withSquireEligibility(SquireEligibility.City)
		.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
		.withPolOrgType(OrganizationType.Individual)
		.withInsFirstLastName("Scope", "Creeps")
		.build(GeneratePolicyType.PolicyIssued);
		
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        GenericWorkorderModifiers pcModifiersPage = new GenericWorkorderModifiers(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcPropertyDetailPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
        GenericWorkorderRiskAnalysis pcRiskPage = new GenericWorkorderRiskAnalysis(driver);
        GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages pcPropertyDetailCoveragesPage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		SoftAssert softAssert = new SoftAssert();		
		
		// We want a couple days between each policy change, so a week here.
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 7);		
		policyChangePage.startPolicyChange("Change#1 Remove location", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		
		// Here, we ensure that the loyalty discount starts at 0 like it should.
		pcSideMenu.clickSideMenuModifiers();		
		String loyaltyDiscountEligibility = pcModifiersPage.getSquirePolicyModififierEligibilityByCategory("Loyalty");
		// 5/21/18 : There's a little confusion on which this should be.. changing throughout test
		softAssert.assertEquals(loyaltyDiscountEligibility, "0", "The Loyalty discount didn't start at 1, a new account should be 1..");
//		Assert.assertEquals(loyaltyDiscountEligibility, "1", "The Loyalty discount didn't start at 0, a new account should be 0..");
		
		pcSideMenu.clickSideMenuSquirePropertyDetail();
		pcPropertyDetailPage.removeBuilding("2");				
		pcWorkOrder.clickGenericWorkorderQuote();	
		pcSideMenu.clickSideMenuRiskAnalysis();			
		pcRiskPage.approveAll();		
		pcWorkOrder.clickIssuePolicyButton();		
		pcWorkorderCompletePage.clickViewYourPolicy();		
		
		
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 7);		
		policyChangePage.startPolicyChange("Change#2 Increase Section I Coverage A Limit", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		
		// We want to make sure that the loyalty discount is still 0, since it hasn't been a year since the policy started
		pcSideMenu.clickSideMenuModifiers();
		loyaltyDiscountEligibility = pcModifiersPage.getSquirePolicyModififierEligibilityByCategory("Loyalty");
//		softAssert.assertEquals(loyaltyDiscountEligibility, "1", "The Loyalty discount was not 1, it should still be 1 after a policy change when less than a year has passed.");
		Assert.assertEquals(loyaltyDiscountEligibility, "0", "The Loyalty discount was not 0, it should still be 0 after a policy change when less than a year has passed.");
		
		pcSideMenu.clickSideMenuSquirePropertyCoverages();
		pcPropertyDetailCoveragesPage.setCoverageALimit(150000.0);
		pcWorkOrder.clickGenericWorkorderQuote();	
		pcSideMenu.clickSideMenuRiskAnalysis();			
		pcRiskPage.approveAll();		
		pcWorkOrder.clickIssuePolicyButton();		
		pcWorkorderCompletePage.clickViewYourPolicy();
		
		// Starting a policy change just to check the loyalty discount
		policyChangePage.startPolicyChange("Change#3 Fake Change", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		// We want to make sure that the loyalty discount is still 0, since it hasn't been a year since the policy started
		pcSideMenu.clickSideMenuModifiers();
		loyaltyDiscountEligibility = pcModifiersPage.getSquirePolicyModififierEligibilityByCategory("Loyalty");
//		softAssert.assertEquals(loyaltyDiscountEligibility, "1", "The Loyalty discount was not 1, it should still be 1 after a policy change when less than a year has passed.");
		softAssert.assertEquals(loyaltyDiscountEligibility, "0", "The Loyalty discount was not 0, it should still be 0 after a policy change when less than a year has passed.");
		
		softAssert.assertAll();
	}
	
	
	@Test(enabled = true)
	public void scenario2VerifyLoyaltyDiscountNotChangedOnPolicyInceptionDatechange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		Squire mySquire = new Squire();

        myPolicyObject = new GeneratePolicy.Builder(driver)
		.withSquire(mySquire)
		.withProductType(ProductLineType.Squire)
		.withSquireEligibility(SquireEligibility.City)
		.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
		.withPolOrgType(OrganizationType.Individual)
		.withInsFirstLastName("Scope", "Creeps")
		.build(GeneratePolicyType.PolicyIssued);
		
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        GenericWorkorderModifiers pcModifiersPage = new GenericWorkorderModifiers(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
        GenericWorkorderRiskAnalysis pcRiskPage = new GenericWorkorderRiskAnalysis(driver);
        GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages pcPropertyDetailCoveragesPage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartRewrite pcRewritePage = new StartRewrite(driver);
        GenericWorkorderPolicyInfo pcPolicyInfoPage = new GenericWorkorderPolicyInfo(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);		
		
		pcPolicyMenu.clickMenuActions();
		pcPolicyMenu.clickCancelPolicy();
		pcCancellationPage.setSourceReasonAndExplanation(Cancellation.CancellationSourceReasonExplanation.Rewritten);
        pcCancellationPage.clickStartCancellation();
		pcCancellationPage.clickSubmitOptionsCancelNow();
		pcWorkorderCompletePage.clickViewYourPolicy();
		
		// Here we will RFT /w inception date 10 years back
		pcPolicyMenu.clickMenuActions();
		pcPolicyMenu.clickRewriteFullTerm();		
		pcSideMenu.clickSideMenuPolicyInfo();
		pcPolicyInfoPage.setTransferedFromAnotherPolicy(true);
		pcPolicyInfoPage.setCheckBoxInInceptionDateByRow(1, true);
		Date newInceptionDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -9);
		pcPolicyInfoPage.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, new GuidewireHelpers(driver).getPolicyNumber(myPolicyObject));
		pcPolicyInfoPage.setInceptionDateByRow(1, newInceptionDate);
		

		// We want to make sure that the loyalty discount is 10, since we changed the inception date to have 10 years of loyalty
		pcSideMenu.clickSideMenuModifiers();
		String loyaltyDiscountEligibility = pcModifiersPage.getSquirePolicyModififierEligibilityByCategory("Loyalty");
		Assert.assertEquals(loyaltyDiscountEligibility, "9", "The Loyalty discount was not 9, it should be 9 after a policy change when the inception date was changed to 10 years prior.");
		
		pcSideMenu.clickSideMenuSquirePropertyCoverages();
		pcPropertyDetailCoveragesPage.setCoverageALimit(150000.0);
		pcSideMenu.clickSideMenuSquireEligibility();
		pcRewritePage.rewriteFullTermGuts(myPolicyObject);
		pcRewritePage.clickIssuePolicy();
		pcWorkorderCompletePage.clickViewYourPolicy();		
		
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Week, 4);		
		policyChangePage.startPolicyChange("Change#2 Increase Section I Coverage A Limit", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		
		// We want to make sure that the loyalty discount is 10, since we changed the inception date to have 10 years of loyalty
		pcSideMenu.clickSideMenuModifiers();
		loyaltyDiscountEligibility = pcModifiersPage.getSquirePolicyModififierEligibilityByCategory("Loyalty");
		Assert.assertEquals(loyaltyDiscountEligibility, "9", "The Loyalty discount was not 9, it should be 9 after a policy change when the inception date was changed to 10 years prior.");
		
		pcSideMenu.clickSideMenuSquirePropertyCoverages();
		pcPropertyDetailCoveragesPage.setCoverageALimit(140000.0);
		pcWorkOrder.clickGenericWorkorderQuote();	
		pcSideMenu.clickSideMenuRiskAnalysis();			
		pcRiskPage.approveAll();		
		pcWorkOrder.clickIssuePolicyButton();		
		pcWorkorderCompletePage.clickViewYourPolicy();
		
		// Starting a policy change just to check the loyalty discount
		policyChangePage.startPolicyChange("Change#3 Fake Change", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		// We want to make sure that the loyalty discount is still 10, since a year hasn't passed
		pcSideMenu.clickSideMenuModifiers();
		loyaltyDiscountEligibility = pcModifiersPage.getSquirePolicyModififierEligibilityByCategory("Loyalty");
		Assert.assertEquals(loyaltyDiscountEligibility, "9", "The Loyalty discount was not 9, it should still be 9 after policy changes not moving the clock"
				+ " after a policy change.");
	}
}

