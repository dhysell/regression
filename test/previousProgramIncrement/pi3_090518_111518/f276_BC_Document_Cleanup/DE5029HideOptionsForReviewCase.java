package previousProgramIncrement.pi3_090518_111518.f276_BC_Document_Cleanup;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.common.BCCommonMenu;
import repository.bc.common.actions.BCCommonActionsCreateNewDocumentFromTemplate;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.enums.PolicySearchPolicyProductType;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author JQU
* * @Requirement 	DE5029 -- Hide options in drop down for review case	
* * @ Acceptance criteria:
					Ensure that all document types besides AdHoc is hidden for the drop down in Review Case.
* * @Steps to get there:
					Click actions on account summary
					New document, Create new document
					Search document templates
					Drop down on document type
					Choose Ad Hoc Doc (should be the only available option)
Actual: All BC document types are available to choose. User get a null pointer or no search options when choosing anything but AdHoc.
Expected: When creating a new document from a template the only document type that available to choose should be "Ad Hoc Doc"
All other options should be hidden from the UI since they bring up zero results or they throw an error.  And they will not be used. 	
					
* @RequirementsLink <a href=""></a>
* @DATE September 10, 2018*/
public class DE5029HideOptionsForReviewCase extends BaseTest{
	private WebDriver driver;
	private String policyNumber = null;
	private ARUsers arUser = new ARUsers();
	
	@Test
	public void findRandomPolicynumber() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login("su","gw");
		BCSearchPolicies policySearch = new BCSearchPolicies(driver);
		this.policyNumber = policySearch.findPolicyInGoodStanding("2356", null, PolicySearchPolicyProductType.Squire);
		System.out.println(policyNumber);
	}
	@Test(dependsOnMethods = { "findRandomPolicynumber" })	
	public void makeMultiplePaymentAndVerify() throws Exception {		
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), policyNumber.substring(3, 9));
		BCCommonMenu commonMenu = new BCCommonMenu(driver);
		commonMenu.clickBCMenuActionsCreateNewDocumentFromTemplate();
		BCCommonActionsCreateNewDocumentFromTemplate docTemplate = new BCCommonActionsCreateNewDocumentFromTemplate(driver);		
		if(docTemplate.getTemplateTypesNumber() != 2)
			Assert.fail("The Document Template drop down should only have none and Ad Hoc Type.");
	}
}
