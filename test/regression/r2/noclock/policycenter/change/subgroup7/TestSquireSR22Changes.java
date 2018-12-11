package regression.r2.noclock.policycenter.change.subgroup7;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SR22FilingFee;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
* @Author nvadlamudi
* @Requirement : DE4321: The Total Gross Premium is wrong, DE4172 - SR 22 - Driver
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description 
* @DATE Jan 18, 2017
*/
public class TestSquireSR22Changes extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
	private Agents agent;
	
	@Test ()
	public void testGenerateSquireAutoIssuance() throws Exception {
		this.agent = AgentsHelper.getRandomAgent();

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        myPolicyObj = new GeneratePolicy.Builder(driver)
				.withAgent(agent)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withCreateNew(CreateNew.Create_New_Always)				
				.withInsPersonOrCompany(ContactSubType.Person)				
				.withInsFirstLastName("Guy", "Auto")
				.withSocialSecurityNumber(StringsUtils.generateRandomNumber(666000000, 666999999))
				.withPolOrgType(OrganizationType.Individual)				
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

	} 
	@Test (dependsOnMethods = { "testGenerateSquireAutoIssuance" })
	public void verifyEditPolicyChangesSquireAuto() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());
		
		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);
		
		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPADrivers();

		
		//Change to SSN, Change Driver's info
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.clickEditButtonInDriverTable(1);
        paDrivers.setSR22Checkbox(true);
		paDrivers.setSR22FilingFee(SR22FilingFee.Charged);
        paDrivers.setSR22EffectiveDate(currentSystemDate);
		paDrivers.clickOk();
        String errorMessage = "";
		//Verify Error message not exists when SR22 Selected and Click on OK button
		ErrorHandlingHelpers error = new ErrorHandlingHelpers(driver);
		if(error.areThereErrorMessages()) 
			errorMessage = errorMessage + "Selecting SR22 should not trigger error message";

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickSaveDraftButton();
		
		quote.clickQuote();
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.approveAll_IncludingSpecial();

        sideMenu.clickSideMenuQuote();
		
		if(quote.getQuoteSR22Charge() != 25){
			errorMessage = errorMessage + "SR22 Charges $25 is not shown on the quote page.\n";
		}
		
		if(quote.getQuoteChangeInCost() != 25){
			errorMessage = errorMessage + "SR22 Charges $25 is not shown on the quote page.\n";
		}

        if (quote.getQuoteTotalGrossPremium() != this.myPolicyObj.squire.getPremium().getTotalGrossPremium()) {
			errorMessage = errorMessage + " Total gross amount is not same.";
		}

        if (quote.getQuoteTotalDiscountsSurcharges() != this.myPolicyObj.squire.getPremium().getTotalDiscountsSurcharges()) {
			errorMessage = errorMessage + " Total discount/ surchanges amount is not same.";
		}

        GenericWorkorderPolicyInfo polinfo = new GenericWorkorderPolicyInfo(driver);
		polinfo.clickEditPolicyTransaction();		
		sideMenu.clickSideMenuPADrivers();		
		paDrivers.clickEditButtonInDriverTable(1);
		paDrivers.setSR22Checkbox(false);	
		paDrivers.clickOk();
		risk.Quote();

        //Verify SR22 value $25 not displayed when SR22 check box is unselected
		if(quote.getQuoteSR22Charge() == 25)
			errorMessage = errorMessage + "SR22 value Sould be '-' when SR22 Checkbox unselected";
		
		polinfo.clickEditPolicyTransaction();
		sideMenu.clickSideMenuPADrivers();
		paDrivers.clickEditButtonInDriverTable(1);
		paDrivers.setSR22Checkbox(true);
		new GuidewireHelpers(driver).logout();
		
		if(errorMessage != ""){
			Assert.fail(errorMessage);
		}
	}	
		
}
