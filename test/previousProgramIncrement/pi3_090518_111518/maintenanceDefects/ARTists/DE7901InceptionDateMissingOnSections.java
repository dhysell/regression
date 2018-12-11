package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.ARTists;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyScheduledItemType;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author nvadlamudi
* @Requirement :DE7901: ***HOT FIX*** Inception Dates are sometimes missing on sections
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764/detail/defect/252759916656">Link Text</a>
* @Description : testing both the scenarios and validating the policy info - Inception dates are not missing
* @DATE Sep 18, 2018
*/
public class DE7901InceptionDateMissingOnSections extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySquirePol;

	@Test()
	public void testcheckSubmissionIssuanceInceptionDates() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
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
                .withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -1))
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .isDraft()
				.build(GeneratePolicyType.FullApp);
		new Login(driver).loginAndSearchSubmission(mySquirePol);
		SideMenuPC sideMenu = new SideMenuPC(driver);	
		sideMenu.clickSideMenuPolicyInfo();
		sideMenu.clickSideMenuLineSelection();
		GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.checkSquireInlandMarine(true);		
		sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		String currentSysDate = DateUtils.dateFormatAsString("MM/dd/yyyy", mySquirePol.squire.getEffectiveDate());
	    Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy", polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.InlandMarine)).equals(currentSysDate), "Submission - Inception Section: Inland Marine - Inception Date is not shown correctly.");
	    sideMenu.clickSideMenuQualification();
	    new GenericWorkorderQualification(driver).clickQualificationIMLosses(false);
	    
	    GenericWorkorderSquireInlandMarine inlandMarinePage = new GenericWorkorderSquireInlandMarine(driver);
		inlandMarinePage.fillOutPLInlandMarine(mySquirePol);
		sideMenu.clickSideMenuRiskAnalysis();
		new GenericWorkorderRiskAnalysis(driver).performRiskAnalysisAndQuote(mySquirePol);
		ActivityPopup activityPopupPage = new ActivityPopup(driver);
		activityPopupPage.clickActivityCancel();
		sideMenu.clickSideMenuPayment();
		GenericWorkorderPayment payment = new GenericWorkorderPayment(driver);
		payment.fillOutPaymentPage(mySquirePol);
		sideMenu.clickSideMenuForms();
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		
		if(payment.checkGenericWorkorderSubmitOptionsIssuePolicyOption()){
			sideMenu.clickSideMenuPayment();			
			payment.clickGenericWorkorderSubmitOptionsIssuePolicy();			
		}else {
			sideMenu.clickSideMenuPayment();
			payment.clickGenericWorkorderSubmitOptionsSubmitOnly();
			new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
			mySquirePol.squire.setPolicyNumber(completePage.getPolicyNumber());
			completePage.clickAccountNumber();
			
			AccountSummaryPC aSumm = new AccountSummaryPC(driver);
			ArrayList<String> activityOwners = new ArrayList<String>();
			activityOwners = aSumm.getActivityAssignedTo("Submitted Full Application");
			
			Underwriters uw = UnderwritersHelper.getUnderwriterInfoByFullName(activityOwners.get(activityOwners.size() - 1));
			new GuidewireHelpers(driver).logout();
			
			new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySquirePol.accountNumber);
			AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
			accountSummaryPage.clickActivitySubject("Submitted Full Application");			
			payment.clickGenericWorkorderQuote();
			sideMenu.clickSideMenuQuote();
			GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);			
			quotePage.issuePolicy(IssuanceType.NoActionRequired);
			
		}
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		mySquirePol.squire.setPolicyNumber(completePage.getPolicyNumber());
		completePage.clickPolicyNumber();
		PolicySummary polSummary = new PolicySummary(driver);
		polSummary.clickCompletedTransactionByType(TransactionType.Submission);
	    
		sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuLineSelection();
	    sideMenu.clickSideMenuPolicyInfo();
	    polInfo = new GenericWorkorderPolicyInfo(driver);
	    Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy", polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy)).equals(currentSysDate), "Submission - Inception Section: Policy - Inception Date is not shown correctly.");
	    Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy", polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Property)).equals(currentSysDate), "Submission - Inception Section: Property - Inception Date is not shown correctly.");
		Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy", polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Liability)).equals(currentSysDate), "Submission - Inception Section: Liability - Inception Date is not shown correctly.");
		Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy", polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Auto)).equals(currentSysDate), "Submission - Inception Section: Auto - Inception Date is not shown correctly.");
		Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy", polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.InlandMarine)).equals(currentSysDate), "Submission - Inception Section: Inland Marine - Inception Date is not shown correctly.");
		
		new GenericWorkorderComplete(driver).clickPolicyNumber();
	    polSummary.clickCompletedTransactionByType(TransactionType.Issuance);
	    sideMenu.clickSideMenuPolicyInfo();
	    Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy", polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy)).equals(currentSysDate), "Issuance - Inception Section: Policy - Inception Date is not shown correctly.");
	    Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy", polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Property)).equals(currentSysDate), "Issuance - Inception Section: Property - Inception Date is not shown correctly.");
		Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy", polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Liability)).equals(currentSysDate), "Issuance - Inception Section: Liability - Inception Date is not shown correctly.");
		Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy", polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Auto)).equals(currentSysDate), "Issuance - Inception Section: Auto - Inception Date is not shown correctly.");
		Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy", polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.InlandMarine)).equals(currentSysDate), "Issuance - Inception Section: Inland Marine - Inception Date is not shown correctly.");
		 
	}
	
	@Test(dependsOnMethods = {"testcheckSubmissionIssuanceInceptionDates"})
	public void testcheckRewriteInceptionDates() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getRandomUnderwriter(UnderwriterLine.Personal);
		
		
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.mySquirePol.accountNumber);
		StartCancellation cancelation = new StartCancellation(driver);
		cancelation.cancelPolicy(CancellationSourceReasonExplanation.Photos, "YOU GOT CANCELED",
				DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), true);
		new GuidewireHelpers(driver).clickWhenClickable(driver
				.findElement(By.xpath("//div[contains(@id, ':JobCompleteScreen:JobCompleteDV:ViewPolicy-inputEl')]")));
		new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Summary')]", 10000,
				"UNABLE TO GET TO POLICY SUMMARY PAGE AFTER CLICKING VIEW POLICY ON JOB COMPLETE PAGE");

		StartRewrite rewritePol = new StartRewrite(driver);
		rewritePol.startRewriteNewTerm();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();
		new GenericWorkorderVehicles_Details(driver).removeAllVehicles();
		sideMenu.clickSideMenuPADrivers();
		GenericWorkorderSquireAutoDrivers paDrivers = new GenericWorkorderSquireAutoDrivers(driver);

		paDrivers.setCheckBoxInDriverTable(1);
		paDrivers.clickRemoveButton();
		sideMenu.clickSideMenuLineSelection();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.checkPersonalAutoLine(false);
		sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		sideMenu.clickSideMenuLineSelection();
		lineSelection.checkPersonalAutoLine(true);
		sideMenu.clickSideMenuPolicyInfo();
		String currentSysDate = DateUtils.dateFormatAsString("MM/dd/yyyy", mySquirePol.squire.getEffectiveDate());
	      
		Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy",polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy)).equals(currentSysDate),"Issuance - Inception Section: Policy - Inception Date is not shown correctly.");
		Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy",polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Property)).equals(currentSysDate),"Issuance - Inception Section: Property - Inception Date is not shown correctly.");
		Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy",polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Liability)).equals(currentSysDate),"Issuance - Inception Section: Liability - Inception Date is not shown correctly.");
		Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy",polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Auto)).equals(currentSysDate),"Issuance - Inception Section: Auto - Inception Date is not shown correctly.");
		Assert.assertTrue(DateUtils.dateFormatAsString("MM/dd/yyyy",polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.InlandMarine)).equals(currentSysDate),"Issuance - Inception Section: Inland Marine - Inception Date is not shown correctly.");

	}
}
