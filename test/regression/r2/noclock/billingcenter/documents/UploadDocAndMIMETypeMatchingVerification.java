package regression.r2.noclock.billingcenter.documents;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonDocuments;
import repository.bc.common.actions.BCCommonActionsUploadNewDocument;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.DocumentFileType;
import repository.gw.enums.DocumentStatus;
import repository.gw.enums.DocumentType;
import repository.gw.enums.SecurityLevel;
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
/**
* @Author jqu
* @Requirement This test is intended to test the ability for a user in Billing Center to be able to upload documents related to Accounts, Policies.
* It also verifies MIME to doc type matching.* 
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/19%20-%20Uploaded%20Documents/Document%20Upload%20Requirements.docx">Business Requirements</a>
* @RequirementsLink <a href="http://projects.idfbins.com/gwintegrations/Shared%20Documents/Document%20Management/Document%20Upload/Document%20Details.xlsx">Document Types</a>
* @DATE Nov 04, 2015
*/
public class UploadDocAndMIMETypeMatchingVerification extends BaseTest {
	private WebDriver driver;
	private String username="sbrunson";
	private String password="gw";
	private BCTopMenu menu;
	private String acctNum, randomDocName=null, fileType, autoFilledFileType, fileNameNoExtension;
	private BCAccountMenu acctMenu;	
	private BCPolicyMenu bcMenu;		
	private BCSearchAccounts mySearch;
	private String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents";
	private int errorCount=0;	
	private Date currentDate;
	private boolean findDoc;
	public ARUsers arUser = new ARUsers();
			
	private Map<String, String> docExtensionAndType = new LinkedHashMap<String, String>() {	
		private static final long serialVersionUID = 1L;
		{
			this.put(".doc", DocumentFileType.MicrosoftWordDocument.getValue());
			this.put(".docx", DocumentFileType.OpenXMLWordDocument.getValue());
			this.put(".txt", DocumentFileType.PlainText.getValue());
			this.put(".xlsx", DocumentFileType.OpenXMLSpreadSheet.getValue());
			this.put(".pdf", DocumentFileType.PDF.getValue());
			this.put(".xml", DocumentFileType.XML.getValue());
			this.put(".html", DocumentFileType.HTML.getValue());
			this.put(".gif", DocumentFileType.GIFImage.getValue());
			this.put(".tif", DocumentFileType.TIFFImage.getValue());
			this.put(".bmp",DocumentFileType.BitmapImage.getValue());
			this.put(".jpg", DocumentFileType.JPEGImage.getValue());
			this.put(".csv", DocumentFileType.CSV.getValue());
			this.put(".wav", DocumentFileType.WaveAudio.getValue());
			this.put(".mpg", DocumentFileType.MPEGVideo.getValue());
			this.put(".ps", DocumentFileType.Postscript.getValue());
			this.put(".pptx", DocumentFileType.OpenXMLPresentation.getValue());
			this.put(".rtf", DocumentFileType.RichTextFormat.getValue());
			this.put(".png", DocumentFileType.PNGImage.getValue());			
	}};
			
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
	
	@Test
	public void randomlyFindAccountAndDocNameToUpload() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		currentDate=DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);		
		//find an existing account and policy number
		menu = new BCTopMenu(driver);
		menu.clickSearchTab();
		mySearch=new BCSearchAccounts(driver);
		acctNum= mySearch.findAccountInGoodStanding("08-");
		System.out.println("acctount number is : "+acctNum);
		//randomly select a document with extention from the folder
		final File folder = new File(documentDirectoryPath);
		do{
			randomDocName=randomSelectFile(folder);			
			getQALogger().info("The randomly selected document name : "+randomDocName);
			//find the key from the randomDocName, and then find the value based on the key in the map
			fileType=docExtensionAndType.get(randomDocName.substring(randomDocName.indexOf(".")));
			fileNameNoExtension=randomDocName.substring(0,randomDocName.indexOf("."));
			getQALogger().info("The file type should be : "+fileType);
		}while(!randomDocName.contains("."));
	}
	
	@Test(dependsOnMethods = { "randomlyFindAccountAndDocNameToUpload" })
	public void uploadDocumentOnAccountAndVerifyTypeMatching() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), acctNum);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuActionsUploadNewDocument();
		BCCommonActionsUploadNewDocument newDoc = new BCCommonActionsUploadNewDocument(driver);
		newDoc.setAttachment(documentDirectoryPath, "\\" + randomDocName);

		autoFilledFileType=newDoc.getCurrentDocumentFileType();
		System.out.println("Auto select file type is : " + autoFilledFileType);
		if(!autoFilledFileType.equals(fileType)){
			getQALogger().error("Auto filled file type doesn't match the uploaded file.");
			errorCount++;			
		}
		newDoc.uploadAccountLevelDocument(documentDirectoryPath, randomDocName, DocumentStatus.Draft, SecurityLevel.Unrestricted, DocumentType.Other, acctNum.substring(0, 6));
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "uploadDocumentOnAccountAndVerifyTypeMatching" })	
	public void verifyDocumentAndDelete() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(username, password, acctNum);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuDocuments();
		BCCommonDocuments acctDoc = new BCCommonDocuments(driver);
		findDoc=acctDoc.verifyDocument(currentDate, fileNameNoExtension);
		if(!findDoc){
			getQALogger().error("Account Level document is not uploaded sucessfully.");
			errorCount++;			
		}else{//test delete
			acctDoc.deleteDocument(currentDate, fileNameNoExtension);
			findDoc = acctDoc.verifyDocument(currentDate, fileNameNoExtension);
			if(findDoc){
				getQALogger().error("Couldn't delete the account level document sucessfully.");
				errorCount++;			
			}
		}
		new GuidewireHelpers(driver).logout();
	}
	//the following is to test uploading doc on a policy
	@Test(dependsOnMethods = { "verifyDocumentAndDelete" })	
	public void uploadDocumentOnPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), acctNum);
        bcMenu = new BCPolicyMenu(driver);
		bcMenu.clickBCMenuActionsUploadNewDocument();
		BCCommonActionsUploadNewDocument pcNewDoc = new BCCommonActionsUploadNewDocument(driver);
		pcNewDoc.setAttachment(documentDirectoryPath, "\\"+randomDocName);

		autoFilledFileType=pcNewDoc.getCurrentDocumentFileType();
		getQALogger().info("Auto select file type is : "+autoFilledFileType);
		if(!autoFilledFileType.equals(fileType)){
			getQALogger().error("Auto filled file type doesn't match the uploaded file for the policy.");
			errorCount++;			
		}
		pcNewDoc.selectStatus(DocumentStatus.Draft);
		pcNewDoc.uploadPolicyLevelDocument(documentDirectoryPath, randomDocName, DocumentStatus.Approved, SecurityLevel.Confidential, DocumentType.Bank_Information);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "uploadDocumentOnPolicy" })	
	public void verifyDocumentOnPolicyAndDelete() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), acctNum);
		bcMenu = new BCPolicyMenu(driver);
		bcMenu.clickBCMenuDocuments();
		BCCommonDocuments pcDoc = new BCCommonDocuments(driver);
		pcDoc.selectDocumentStatus(DocumentStatus.Approved);
		pcDoc.clickSearch();
		findDoc = pcDoc.verifyDocument(currentDate, fileNameNoExtension);
		if(!findDoc){
			getQALogger().error("Policy Level document is not uploaded sucessfully.");
			errorCount++;			
		}else{//test delete
			pcDoc.deleteDocument(currentDate, fileNameNoExtension);
			findDoc = pcDoc.verifyDocument(currentDate, fileNameNoExtension);
			if(findDoc){
				getQALogger().error("Couldn't delete the policy level document sucessfully.");
				errorCount++;			
			}
		}
		if (errorCount>0) {
			Assert.fail("found errors, please see the output for the detail.");
		}
		new GuidewireHelpers(driver).logout();
	}
}
