package previousProgramIncrement.pi2_062818_090518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.generate.custom.UnderlyingInsuranceUmbrella;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_ExclusionsConditions;
import repository.pc.workorders.generic.GenericWorkorderSquirePACoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropLiabUmbCoveragesExclusions;
import repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/53278921400">US7344 - PL - Umbrella remove Hagerty question</a>
 * @Description
 * Regarding US5409 (or US5144) the question "Are any owned vehicles insured with Hagerty or American Modern". This question needs to be removed from Umbrella qualification questions.
 * If underlying squire policy has a 301 endorsement (squire Hagerty endorsement) then the umbrella policy needs to require a 217 endorsement to be added. The issue is that with the Hagerty question in both places, the form gets triggered twice.
 * 
 * ________________________________________________________________
 * 
 * As a user I want to remove a question from Umbrella Qualification questions so that the form does not get triggered twice.
 * 
 * Steps to get there:
 * 
 *     Do a squire policy where the qualification question "Are any owned vehicles insured with Hagerty or American Modern" is marked as YES.
 *     Start an umbrella policy also. NOTE - the qualification question should not be on the umbrella policy question set
 *     Bind and issue both policies
 * 
 * Acceptance Criteria:
 * 
 *     Ensure that on the squire policy the 301 endorsement is inferred and will print in documents
 *     Ensure that on the umbrella policy there is no longer the qualification question "Are any owned vehicles insured with Hagerty or American Modern"
 *     Ensure that on the umbrella policy there is the 217 endorsement inferred and will print in documents
 * 
 * 
 * Requirements: PC8 - Umbrella - Qualification questions (Refer UI Final tab)
 * 
 * Qualification question list and rules: Qualifying Questions All Lines (Refer Umbrella tab)
 * @DATE August 2, 2018
 */

public class US7344_RemoveHagertyQuestion extends BaseTest {
    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    public void generatePolicy() throws Exception {
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
                MedicalLimit.TenK);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire();
        mySquire.squirePA = squirePersonalAuto;
    	
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
        		.withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
				.withDownPaymentType(PaymentType.Cash)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                .build(GeneratePolicyType.FullApp);
    }

    @Test(enabled = true)
    public void EnsureHagertyRemovedFromUmbrella() throws Exception {            
    	generatePolicy();
        
        new Login(driver).loginAndSearchAccountByAccountNumber("msanchez",
                "gw", myPolicyObject.accountNumber);
        

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		SoftAssert softAssert = new SoftAssert();
		GenericWorkorderQualification pcQualificationPage = new GenericWorkorderQualification(driver);
		GenericWorkorderSquirePACoverages pcSquirePACoveragesPage = new GenericWorkorderSquirePACoverages(driver);
		GenericWorkorderSquireAutoCoverages_ExclusionsConditions pcPAExclusionsConditionsPage = new GenericWorkorderSquireAutoCoverages_ExclusionsConditions(driver);
		GenericWorkorderSquireUmbrellaCoverages pcUmbrellaCoverages = new GenericWorkorderSquireUmbrellaCoverages(driver);
		GenericWorkorderSquirePropLiabUmbCoveragesExclusions pcUmbrellaExclusionPage = new GenericWorkorderSquirePropLiabUmbCoveragesExclusions(driver);
		GenericWorkorderPayment pcPaymentPage = new GenericWorkorderPayment(driver);
		GenericWorkorderRiskAnalysis pcRiskPage = new GenericWorkorderRiskAnalysis(driver);
		
		
		pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
		
        new repository.driverConfiguration.BasePage(driver).clickWhenClickable(By.xpath("//td[contains(@id, 'PreQual')]//span[contains(text(), 'Qualification')]"));
        new GuidewireHelpers(driver).editPolicyTransaction();
        
        pcQualificationPage.clickPL_Hagerty(true);
        pcQualificationPage.setHagertyVehicleList("Veh1");
        
        new repository.driverConfiguration.BasePage(driver).clickWhenClickable(By.xpath("//td[contains(@id, 'PAWiz')]//span[contains(text(), 'Section III - Auto')]"));
        new repository.driverConfiguration.BasePage(driver).clickWhenClickable(By.xpath("//td[contains(@id, 'PALine')]//span[contains(text(), 'Coverages')]"));
        
        pcSquirePACoveragesPage.clickCoverageExclusionsTab();
        pcPAExclusionsConditionsPage.fillOutCoveragesExclutionsConditions();
        
		pcWorkOrder.clickGenericWorkorderQuote();
		
        pcSideMenu.clickSideMenuPayment();
		pcPaymentPage.clickAddDownPayment();
		pcPaymentPage.setDownPaymentType(PaymentType.Cash);
		pcPaymentPage.setDownPaymentAmount(5000);
		pcPaymentPage.clickOK();
        
		pcRiskPage.clickReleaseLock();

		new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.agentUserName,
                myPolicyObject.agentInfo.agentPassword, myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);        
		
		pcWorkOrder.clickGenericWorkorderIssue(IssuanceType.Issue);
        
        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);
		squireUmbrellaInfo.setEffectiveDate(myPolicyObject.squire.getEffectiveDate());
		squireUmbrellaInfo.setDraft(true);
		myPolicyObject.squireUmbrellaInfo = squireUmbrellaInfo;
		new GuidewireHelpers(driver).logout();
		myPolicyObject.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.QuickQuote);
		new Login(driver).loginAndSearchSubmission(myPolicyObject);
		
		pcSideMenu.clickSideMenuSquireUmbrellaCoverages();
		pcSideMenu.clickSideMenuPolicyInfo();
		pcUmbrellaCoverages.clickExclusionsConditions();
				
		List<String> validationMessages = pcWorkOrder.getValidationMessagesAlternate();
		
		// Acceptance criteria: Ensure that on the umbrella policy there is no longer the qualification question "Are any owned vehicles insured with Hagerty or American Modern"
        Assert.assertTrue(validationMessages.contains("Additional \"Underlying Insurance\" Umbrella Endorsement 217 must have at least one scheduled item."), "Scheduled item validation did not show.");
        
      
        pcUmbrellaCoverages.clickExclusionsConditions();
		UnderlyingInsuranceUmbrella underlyingInsurance = new UnderlyingInsuranceUmbrella();
		ArrayList<UnderlyingInsuranceUmbrella> underlyingInsuranceList = new ArrayList<>();
		underlyingInsuranceList.add(underlyingInsurance);
		pcUmbrellaExclusionPage.addAdditionalUnderlyingInsuranceUmbrellaEndorsement217Details(underlyingInsuranceList);
		
		pcSideMenu.clickSideMenuRiskAnalysis();
		pcWorkOrder.clickGenericWorkorderQuote();
		pcWorkOrder.clickGenericWorkorderFullApp();
		pcQualificationPage.setUmbrellaQuestionsFavorably();
		pcWorkOrder.clickGenericWorkorderQuote();
		
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.agentUserName, myPolicyObject.agentInfo.agentPassword, myPolicyObject.accountNumber);		
		
		pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.PersonalUmbrella);
		
		pcSideMenu.clickSideMenuPayment();
		pcPaymentPage.clickAddDownPayment();
		pcPaymentPage.setDownPaymentType(PaymentType.Cash);
		pcPaymentPage.setDownPaymentAmount(5000);
		pcPaymentPage.clickOK();
		

		pcSideMenu.clickSideMenuQuote();
		pcSideMenu.clickSideMenuRiskAnalysis();
		pcSideMenu.clickSideMenuForms();
		pcWorkOrder.clickGenericWorkorderIssue(IssuanceType.Issue);
		
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchAccountByAccountNumber("kgarrick", "gw", myPolicyObject.accountNumber);
		
		boolean squireInForce = pcAccountSummaryPage.verifyPolicyStatusInPolicyCenter("01-" + myPolicyObject.accountNumber + "-01", PolicyTermStatus.InForce, 500);
		boolean umbrellaInForce = pcAccountSummaryPage.verifyPolicyStatusInPolicyCenter("01-" + myPolicyObject.accountNumber + "-02", PolicyTermStatus.InForce, 500);
		
		// Acceptance Criteria: We want to ensure that both are in-force
		softAssert.assertTrue(squireInForce, "Squire was not in force, something went wrong!");
		softAssert.assertTrue(umbrellaInForce, "Umbrella was not in force, something went wrong!");
		
		softAssert.assertAll();
    }
}