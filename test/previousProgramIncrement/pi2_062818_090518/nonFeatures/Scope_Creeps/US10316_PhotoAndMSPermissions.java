package previousProgramIncrement.pi2_062818_090518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/87556354552">US10316 - Photo and MS year permission</a>
 * @Description
 * As an agent or PA, I want to be able to initially add photo month/date and MS month/date, but not edit it later, on property (section I buildings) so that underwriting does not have to fill this information in on all new business/new property. 
 * 
 * Requirements: PC8 - Squire - QuoteApplication - Property Detail (Refer UI Final tab)
 * 
 * Steps to get there:
 * 
 *     As agent/PA start a new submission that has section I
 *     Add different coverage A and coverage E buidlings
 *     Agent/PA should be able to put the month & year on buildings for photo and MS
 * 
 * Acceptance criteria:
 * 
 *     Ensure that as an agent or PA the month/year field on photo and MS are available to edit
 *     Ensure that once the agent or PA leaves the screen that field is no longer editable by the agent or PA
 *     Ensure that underwriter role is able to add or edit both the photo and MS fields
 *     Ensure that the UW issue still infers on Risk Analysis if any photo or MS fields are left blank after quote 

 * Allow adding for first time, but not edit.

 * CL only requires year and not month, should PL change to only require Year? Talked to UW supervisors on 1-26-17. They want to keep month/year in PL.  Brenda S.
 * @DATE August 1, 2018
 */

//@Test(groups= {"ClockMove"})
public class US10316_PhotoAndMSPermissions extends BaseTest {

    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;


    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
				.withDownPaymentType(PaymentType.Cash)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                .build(GeneratePolicyType.FullApp);
    }

    @Test(enabled = true)
    public void CheckPermissionsPhotoYearMS() throws Exception {


            generatePolicy();


//      new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.underwriterInfo.underwriterUserName,
//      myPolicyObject.underwriterInfo.underwriterPassword, myPolicyObject.accountNumber);
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.agentUserName,
        myPolicyObject.agentInfo.agentPassword, myPolicyObject.accountNumber);
        
    	
    	
//      @Testing config
//      Config cf = new Config(ApplicationOrCenter.PolicyCenter);
//      driver = buildDriver(cf);
//      new Login(driver).loginAndSearchAccountByAccountNumber("kbaxter", "gw", "280777");
        
        

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcPropertyDetailPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		GenericWorkorderSquirePropertyDetail pcPropertyPage = new GenericWorkorderSquirePropertyDetail(driver);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails pcProtectionDetailsPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver);
		GenericWorkorderSquirePropertyDetailConstruction pcPropertyDetailConstructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
		
		pcAccountSummaryPage.clickCurrentActivitiesSubject("Underwriter has reviewed this job");
//		pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
		pcSideMenu.clickSideMenuSquirePropertyDetail();
		
		
		new GuidewireHelpers(driver).editPolicyTransaction();
		PLPolicyLocationProperty myProperty = new PLPolicyLocationProperty();
		pcPropertyPage.setProperty(myProperty, SectionIDeductible.FiveHundred);
		pcProtectionDetailsPage.fillOutPropertyProtectionDetails(myProperty);
		pcPropertyDetailPage.clickPropertyConstructionTab();		
		
		// Acceptance criteria: Agent should be able to edit these
		Assert.assertTrue(pcPropertyDetailConstructionPage.MSPhotoYearEditable(), "MS Year field was not editable!");
		Assert.assertTrue(pcPropertyDetailConstructionPage.PhotoYearEditable(), "MS Year field was not editable!");
		pcWorkOrder.clickOK();
		
		Boolean msPhotoYearFound = false;
		for (WebElement ele : pcWorkOrder.getValidationResultsList()) { // This validation will always show now after hitting okay without them filled
			if (ele.getText().contains("upload the MS") || ele.getText().contains("upload the photos")) msPhotoYearFound = true;
		}
		Assert.assertTrue(msPhotoYearFound, "Validation for MS or Photo year was not displayed");
		
		pcPropertyDetailConstructionPage.setMSYear("01/1980");
		pcPropertyDetailConstructionPage.setPhotoYear("01/1980");
		
		pcWorkOrder.clickOK();
		
		msPhotoYearFound = false;
		for (WebElement ele : pcWorkOrder.getValidationResultsList()) { // This validation will always show now after hitting okay without them filled
			if (ele.getText().contains("upload the MS") || ele.getText().contains("upload the photos")) msPhotoYearFound = true;
		}
		Assert.assertFalse(msPhotoYearFound, "Validation for MS or Photo year was displayed and it shouldn't be after filling them in");
		
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchAccountByAccountNumber("kgarrick", "gw", myPolicyObject.accountNumber);
		pcAccountSummaryPage.clickCurrentActivitiesSubject("Underwriter has reviewed this job");
		
		pcSideMenu.clickSideMenuSquirePropertyDetail();
		pcPropertyPage.clickViewOrEditBuildingButton(1);
		pcPropertyDetailPage.clickPropertyConstructionTab();	
		
		// Acceptance criteria: UW should be able to edit these
		Assert.assertTrue(pcPropertyDetailConstructionPage.MSPhotoYearEditable(), "MS Year field was not editable!");
		Assert.assertTrue(pcPropertyDetailConstructionPage.PhotoYearEditable(), "MS Year field was not editable!");
    }
}




















