package previousProgramIncrement.pi2_062818_090518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetail;

import java.util.ArrayList;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/116795501224">US11238 - Coverage E Dwelling Under Construction needs Additional Contents available</a>
 * @Description
 * As a PC user, I need the ability to add additional contents to a Coverage E Dwelling Under Construction, in order to continue to offer and show coverage offerings to customers going forward in PolicyCenter, as this was a missed requirement originally.
 * 
 * Requirements: PC8 - Common - QuoteApplication - Coverages (Refer UI Mockup tab)
 * 
 * Steps to get there:
 * 
 *     As agent start a new submission that includes sections I & II.
 *     Add a Dwelling Under Construction Coverage E.
 *     There needs to be a checkbox with ability to add additional contents in this building. (screenshot of what it should look like is attached)
 * 
 * Acceptance criteria:
 * 
 *     User should always see checkbox option to add contents on Dwelling Under Construction Cov E (same as Cov A Dwelling Under Construction)
 *     Applies to new submission, policy change, and all editable jobs as permissions allow. 
 * 
 * 
 * **There are 2 policies that renew in December (057674-11 & 126620-01). Need to try to get work done by that time. There is no workaround other than not move policy to PC. Also check PC policy 281686 to see if adjustments need to be made. Added second line for contents only at same location. Transition date has changed but need this fixed for production.  AS OF 4-27-18 057674-11 AND 126620-01 NO LONGER NEED INTERVENTION. THE HOMES UNDER CONSTRUCTION ARE COMPLETED.
 * 
 * 8-9-18 Will need to check PC for any Dwelling Under Construction Coverage E buildings that might have been added
 * @DATE August 9, 2018
 */

public class US11238_CoverageCCheckbox extends BaseTest {
    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    @Test(enabled = true)
    public void NewSubmissionCheckForCheckbox() throws Exception {


            Config cf = new Config(ApplicationOrCenter.PolicyCenter);
            driver = buildDriver(cf);
            
            Squire mySquire = new Squire();
            PLPolicyLocationProperty newProperty1 = new PLPolicyLocationProperty(PropertyTypePL.DwellingUnderConstructionCovE);
            PolicyLocation newLocation = new PolicyLocation();
            ArrayList<PLPolicyLocationProperty> propertyLocationList1 = new ArrayList<PLPolicyLocationProperty>();
            ArrayList<PolicyLocation> newLocationList = new ArrayList<PolicyLocation>();        
            newProperty1.setYearBuilt(DateUtils.getYearFromDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -0)));
            propertyLocationList1.add(newProperty1);        
            newLocation.setPropertyList(propertyLocationList1);
            newLocationList.add(newLocation);
            mySquire.propertyAndLiability.locationList = newLocationList;
           
            myPolicyObject = new GeneratePolicy.Builder(driver)
            		.withSquire(mySquire)
                    .withProductType(ProductLineType.Squire)
                    .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                    .withPolOrgType(OrganizationType.Individual)
    				.withDownPaymentType(PaymentType.Cash)
                    .withInsFirstLastName("ScopeCreeps", "NonFeature")
                    .isDraft()
                    .build(GeneratePolicyType.QuickQuote);


        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.agentUserName,
              myPolicyObject.agentInfo.agentPassword, myPolicyObject.accountNumber);
        

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		GenericWorkorderSquirePropertyCoverages pcSquirePropertyCoveragesPage = new GenericWorkorderSquirePropertyCoverages(driver);
		
		
		pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
		
		pcSideMenu.clickSideMenuSquirePropertyCoverages();
		
		// Acceptance criteria: Checkbox for Coverage C should be available
        Assert.assertTrue(pcSquirePropertyCoveragesPage.checkCovCCheckboxExists(), "Coverage C checkbox was not visible.");
    }
    
    @Test(enabled = true)
    public void PolicyChangeCheckForCheckbox() throws Exception {


            Config cf = new Config(ApplicationOrCenter.PolicyCenter);
            driver = buildDriver(cf);
           
            myPolicyObject = new GeneratePolicy.Builder(driver)
                    .withProductType(ProductLineType.Squire)
                    .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                    .withPolOrgType(OrganizationType.Individual)
    				.withDownPaymentType(PaymentType.Cash)
                    .withInsFirstLastName("ScopeCreeps", "NonFeature")
                    .build(GeneratePolicyType.PolicyIssued);


        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(),
                myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);
        

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorderSquirePropertyDetail pcPropertyPage = new GenericWorkorderSquirePropertyDetail(driver);
		GenericWorkorderSquirePropertyCoverages pcSquirePropertyCoveragesPage = new GenericWorkorderSquirePropertyCoverages(driver);
		StartPolicyChange pcPolicyChange = new StartPolicyChange(driver);
		
		
		pcPolicyChange.startPolicyChange("This is a change", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		
		pcSideMenu.clickSideMenuSquirePropertyDetail();
		
		PLPolicyLocationProperty newProperty1 = new PLPolicyLocationProperty(PropertyTypePL.DwellingUnderConstructionCovE);
		newProperty1.setYearBuilt(DateUtils.getYearFromDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -0)));
		pcPropertyPage.setProperty(newProperty1, SectionIDeductible.FiveHundred);
		
		pcSideMenu.clickSideMenuSquirePropertyCoverages();
		
		pcSquirePropertyCoveragesPage.clickSpecificBuilding(1,2);
		
		// Acceptance criteria: Checkbox for Coverage C should be available
        Assert.assertTrue(pcSquirePropertyCoveragesPage.checkCovCCheckboxExists(), "Coverage C checkbox to enable was not there!");
    }
    
    @Test(enabled = true)
    public void RewriteFullTermCheckForCheckbox() throws Exception {


            Config cf = new Config(ApplicationOrCenter.PolicyCenter);
            driver = buildDriver(cf);
           
            myPolicyObject = new GeneratePolicy.Builder(driver)
                    .withProductType(ProductLineType.Squire)
                    .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                    .withPolOrgType(OrganizationType.Individual)
    				.withDownPaymentType(PaymentType.Cash)
                    .withInsFirstLastName("ScopeCreeps", "NonFeature")
                    .build(GeneratePolicyType.PolicyIssued);

        new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);

        
		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
		StartCancellation pcCancellationPage = new StartCancellation(driver);
		GenericWorkorderSquirePropertyDetail pcPropertyPage = new GenericWorkorderSquirePropertyDetail(driver);
		GenericWorkorderSquirePropertyCoverages pcSquirePropertyCoveragesPage = new GenericWorkorderSquirePropertyCoverages(driver);
		
		
		pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
		pcPolicyMenu.clickMenuActions();
		pcPolicyMenu.clickCancelPolicy();
		pcCancellationPage.setSourceReasonAndExplanation(Cancellation.CancellationSourceReasonExplanation.Rewritten);
		pcCancellationPage.clickStartCancellation();
		pcCancellationPage.clickSubmitOptionsCancelNow();
		pcWorkorderCompletePage.clickViewYourPolicy();
		
		pcPolicyMenu.clickMenuActions();
		pcPolicyMenu.clickRewriteFullTerm();
		
		pcSideMenu.clickSideMenuSquirePropertyDetail();
		
		PLPolicyLocationProperty newProperty1 = new PLPolicyLocationProperty(PropertyTypePL.DwellingUnderConstructionCovE);
		newProperty1.setYearBuilt(DateUtils.getYearFromDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -0)));
		pcPropertyPage.setProperty(newProperty1, SectionIDeductible.FiveHundred);
		
		pcSideMenu.clickSideMenuSquirePropertyCoverages();
		
		pcSquirePropertyCoveragesPage.clickSpecificBuilding(1,2);
		
		// Acceptance criteria: Checkbox for Coverage C should be available
        Assert.assertTrue(pcSquirePropertyCoveragesPage.checkCovCCheckboxExists(), "Coverage C checkbox to enable was not there!");
    }
}