package regression.r2.noclock.billingcenter.documents;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.summary.BCAccountSummary;
import repository.bc.administration.AdminUserDetails;
import repository.bc.administration.AdminUsers;
import repository.bc.administration.BCAdministrationMenu;
import repository.bc.common.BCCommonDocuments;
import repository.bc.common.actions.BCCommonActionsUploadNewDocument;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.DocumentStatus;
import repository.gw.enums.DocumentType;
import repository.gw.enums.SecurityLevel;
import repository.gw.enums.UserRole;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**US6165
* @Author jqu
* @Action Items:
	Verify the correct users can search and view the documents.
	Create a new permission to "View Bank Information Documents" called bankdocview
	Apply new permission to 
	Billing Clerical
	Finance Clerical
	Finance Manager
	General Admin
	Super User 
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/15%20-%20Roles%20Permissions/05-04%20Add%20Security%20to%20Bank%20Information%20documents.docx">Business Requirements</a>* 
* @DATE Dec 28, 2015
*/

//import com.idfbins.driver.BaseTest;
@QuarantineClass
public class SecurityToBankInformationDocumentTest extends BaseTest {
	private WebDriver driver;
	public ARUsers arUser = new ARUsers();	
	private BCTopMenu menu;
	private String acctNum, randomDocName=null, fileNameNoExtension;	
	private BCPolicyMenu bcMenu;		
	private BCSearchAccounts mySearch;
	private String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents";
	private String bankInfoDocName="just_a_doc", userWithViewDocRole, userWithPCViewOnlyRole;
	private ArrayList<String> usersList = new ArrayList<>();
	private Date currentDate;
	private BCAdministrationMenu adminMenu;
	private AdminUsers adminUsers;
	private AdminUserDetails userDetails;
	
	private String randomSelectFile(final File folder) {
    	boolean found=false;
    	String randomFileName = null;
        File[] filesInFolder = folder.listFiles();
        while(!found){          
            randomFileName=filesInFolder[NumberUtils.generateRandomNumberInt(0, filesInFolder.length-1)].getName();
            if(!randomFileName.equals("Thumbs.db") && (randomFileName.contains("."))){
              found=true;
            }
          }
        return randomFileName;
     }
	
	private void loginBCAsSuperUser() throws Exception{
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login("su", "gw");
	}
	@Test
	public void randomlyFindAccountAndDocNameToUpload() throws Exception {
		loginBCAsSuperUser();
			
		//find an existing policy number to upload documents
		menu = new BCTopMenu(driver);
		menu.clickSearchTab();
		mySearch=new BCSearchAccounts(driver);
		acctNum= mySearch.findAccountInGoodStanding("08-");
		System.out.println("acctount number is : "+acctNum);
		BCAccountSummary summary = new BCAccountSummary(driver);
		summary.clickPolicyNumberInOpenPolicyStatusTable(acctNum.substring(0, 6));
		//randomly select a document with extention from the folder to upload
		final File folder = new File(documentDirectoryPath);
		do{
			randomDocName=randomSelectFile(folder);			
			System.out.println("The randomly selected document name : "+randomDocName);			
			fileNameNoExtension=randomDocName.substring(0,randomDocName.indexOf("."));
			System.out.println("The file name is : "+fileNameNoExtension);
		}while(!randomDocName.contains("."));
		//upload the document with the name as fileNameNoExtension, and type is not Bank Information
		//upload a document with the name of bankInfoDocName and the type is Bank Information.
        bcMenu = new BCPolicyMenu(driver);
		
		for(int i=0;i<2; i++){
			bcMenu.clickBCMenuActionsUploadNewDocument();
			BCCommonActionsUploadNewDocument pcNewDoc = new BCCommonActionsUploadNewDocument(driver);
			if(i==0){
				pcNewDoc.uploadPolicyLevelDocument(documentDirectoryPath, randomDocName, DocumentStatus.Draft, SecurityLevel.Confidential, DocumentType.Other);
			}else{
				
				pcNewDoc.uploadPolicyLevelDocument(documentDirectoryPath, bankInfoDocName, DocumentStatus.Final, SecurityLevel.Confidential, DocumentType.Bank_Information);
			}
		}
		new GuidewireHelpers(driver).logout();
	}
	@Test(dependsOnMethods = { "randomlyFindAccountAndDocNameToUpload" })	
	public void findUsersWithBankDocViewPermissionAndUserWithPCViewOnlyRole() throws Exception {			
				
		//the following users have 'bankdocview' permission
		//randomly find a username from them
		usersList.add("Billing Clerical");
		usersList.add("Finance Clerical");
		usersList.add("Finance Manager");
		usersList.add("General Admin");
		usersList.add("Superuser");
		
		loginBCAsSuperUser();
		menu = new BCTopMenu(driver);
		menu.clickAdministrationTab();
		adminMenu = new BCAdministrationMenu(driver);
		adminMenu.clickAdminMenuUsersAndSecurityUsers();
		adminUsers = new AdminUsers(driver);
		
		//randomly select usrnames from usersList		
		adminUsers.setAdminUsersRole(usersList.get(NumberUtils.generateRandomNumberInt(1, usersList.size())-1));
		adminUsers.clickSearch();

		int randNum=NumberUtils.generateRandomNumberInt(1, adminUsers.getAdminUsersTableRowCount());		
		userWithViewDocRole=adminUsers.getAdminUsersTableUsername(randNum);	
		//randomly select username from Information Service and add 'PC View Only' role to him/her
		adminUsers.setAdminUsersRole("Information Services");
		adminUsers.clickSearch();
		randNum = NumberUtils.generateRandomNumberInt(2, adminUsers.getAdminUsersTableRowCount() - 1);
		userWithPCViewOnlyRole=adminUsers.getAdminUsersTableUsername(randNum);
		adminUsers.clickAdminUsersTableDisplayName(randNum);
		userDetails = new AdminUserDetails(driver);
		userDetails.addNewUserRole(UserRole.PCViewOnly);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "findUsersWithBankDocViewPermissionAndUserWithPCViewOnlyRole" })	
	public void verifyDocSearchByUsersWithBankDocViewPermission() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		currentDate=DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(userWithViewDocRole, arUser.getPassword(), acctNum);
		bcMenu = new BCPolicyMenu(driver);
		bcMenu.clickBCMenuDocuments();
		BCCommonDocuments doc = new BCCommonDocuments(driver);
		doc.clickSearch();	
		if(doc.getDocumentsTableRow(fileNameNoExtension, DocumentType.Other, DocumentStatus.Draft, null, currentDate, null)==null){
			Assert.fail("Didn't find the document named as "+ fileNameNoExtension);	
		}
		if(doc.getDocumentsTableRow(bankInfoDocName, DocumentType.Bank_Information, DocumentStatus.Final, null, currentDate, null)==null){
			Assert.fail("Didn't find the Bank Information document named as "+ bankInfoDocName);	
		}
	}
	
	///users with 'PC View Only' role will get hard stop on Bank Informantion doc search and warning messages 
	@Test(dependsOnMethods = { "verifyDocSearchByUsersWithBankDocViewPermission" })	
	public void verifyWarningsOnUsersWithPCWiewOnlyRole() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(userWithPCViewOnlyRole, arUser.getPassword(), acctNum);
        bcMenu = new BCPolicyMenu(driver);
		bcMenu.clickBCMenuDocuments();
		BCCommonDocuments doc = new BCCommonDocuments(driver);
		doc.selectDocumentType(DocumentType.Bank_Information);
		doc.clickSearch();
		if (!new GuidewireHelpers(driver).containsErrorMessage("You do not have adequate permissions to view Bank Information documents")) {
			Assert.fail("users with PC View Only role should get warning msg when searching Bank Information.");
		}
		////The following block will be uncommented when the bug is fixed.
		
//		doc.clickReset();
//		//		doc.clickSearch();
//		if(doc.getDocumentsTableRow(fileNameNoExtension, DocumentType.Other, DocumentStatus.Draft, null, currentDate, null)==null){
//			throw new GuidewireException(getCurrentURL(), "Didn't find the document named as "+ fileNameNoExtension);	
//		}
//		if(doc.getDocumentsTableRow(bankInfoDocName, DocumentType.Bank_Information, DocumentStatus.Final, null, currentDate, null)!=null){
//			throw new GuidewireException(getCurrentURL(), "users with PC View Only role should not be able to search the Bank Information document");	
//		}
//		if(!doc.getWarningMessage().contains("Bank Information is excluded from search results.")){
//			throw new GuidewireException(getCurrentURL(), "users with PC View Only role should get warning msg.");
//		}		
	}
	
	@Test(dependsOnMethods = { "verifyWarningsOnUsersWithPCWiewOnlyRole" })	
	public void removePCWiewOnlyRoleFromTheUser() throws Exception {
		loginBCAsSuperUser();
		menu = new BCTopMenu(driver);
		menu.clickAdministrationTab();
		adminMenu = new BCAdministrationMenu(driver);
		adminMenu.clickAdminMenuUsersAndSecurityUsers();
		adminUsers = new AdminUsers(driver);
		adminUsers.setAdminUsersName(userWithPCViewOnlyRole);
		adminUsers.clickSearch();

		adminUsers.clickAdminUsersTableDisplayName(userWithPCViewOnlyRole);
		userDetails = new AdminUserDetails(driver);
		userDetails.removeUserRole(UserRole.PCViewOnly);
	}

}
