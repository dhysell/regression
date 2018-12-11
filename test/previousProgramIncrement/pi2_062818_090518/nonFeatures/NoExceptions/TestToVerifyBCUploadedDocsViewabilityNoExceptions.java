package previousProgramIncrement.pi2_062818_090518.nonFeatures.NoExceptions;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.common.BCCommonDocuments;
import repository.bc.common.actions.BCCommonActionsUploadNewDocument;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.AccountType;
import repository.gw.enums.DocumentStatus;
import repository.gw.enums.DocumentType;
import repository.gw.enums.SecurityLevel;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
public class TestToVerifyBCUploadedDocsViewabilityNoExceptions extends BaseTest {
	private WebDriver driver;
	private BCAccountMenu acctMenu;	
	private String documentDirectoryPath = "\\\\fbmsqa11.idfbins.com\\testing_data\\test-documents\\DocToUpload";
    private String dotExtension = ".pdf";
	private String companyIs =null;
	private Date uploadDate;
	private ARUsers arUser = new ARUsers();
	private BCSearchAccounts searchAccount;
	private BCAccountSummary acctSummary;
	private String acctNumber=null,policyNumber=null;
	private BCCommonDocuments acctDoc;
	private BCCommonActionsUploadNewDocument acctActionUploadNewDoc;

	public void testAllDocuments() {
		
		HashMap<String, DocumentType> documentsToTest = new HashMap<>();
        documentsToTest.put("BankInformation", DocumentType.Bank_Information);
        documentsToTest.put("BankruptcyPapers", DocumentType.Bankruptcy_Papers);
        documentsToTest.put("CheckRequest", DocumentType.Check_Request);
        documentsToTest.put("DebitCreditVoucher", DocumentType.Disputed_Bank_Statement);
        documentsToTest.put("ReceiptOfPayment", DocumentType.Receipt_Of_Payment);
		StringBuilder failedDocuments = new StringBuilder();
		uploadDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
		
		for(Map.Entry< String, DocumentType> pair : documentsToTest.entrySet()){
			String documentName = pair.getKey();
			DocumentType documentType = pair.getValue();
			
			boolean result = verifyDocuments(documentName, documentType);
			if(!result){
				failedDocuments.append(documentType.getValue()).append(", ");
			} 
		}		
		if(failedDocuments.length() > 0){
			Assert.fail(failedDocuments.append(" documents failed to round trip or to store on IR, Check the console out to find the location where they are stored").toString());
		}

	}

    private boolean verifyDocuments(String docToUpload, DocumentType docType) {

        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuActionsUploadNewDocument();
		acctActionUploadNewDoc = new BCCommonActionsUploadNewDocument(driver);
        acctActionUploadNewDoc.uploadAccountLevelDocument(documentDirectoryPath, docToUpload.concat(dotExtension), DocumentStatus.Draft, SecurityLevel.Unrestricted, docType, policyNumber.substring(0, 12));
		acctMenu.clickBCMenuDocuments();
		acctDoc = new BCCommonDocuments(driver);
        acctDoc.verifyDocument(docToUpload, docType, null, arUser.getFirstName() +" "+ arUser.getLastName(), uploadDate, uploadDate);
		
        acctDoc.waitUntilDocumentStatusChanges(240, docToUpload, docType, DocumentStatus.Final,arUser.getFirstName() +" "+ arUser.getLastName(), uploadDate,uploadDate);
		String mainGWWindow = acctDoc.clickViewDocumentInTable(docToUpload, docType, null, arUser.getFirstName() +" "+ arUser.getLastName(), uploadDate,uploadDate);
		boolean  docIsViewable= acctDoc.isDocumentPresentInImageRight();
		if(!docIsViewable){
			getQALogger().error("XXXXXXX---  "+docType.getValue() + " failed to store on ImageRight or failed to make round trip. The Document should have been stored at: " + driver.getCurrentUrl() + ". ----XXXXXXXX");
		} else{
			getQALogger().info("@@-------  "+docType.getValue() + " was successfully uploaded and  verified. "+" -------------@@");
		}

		driver.switchTo().window(mainGWWindow);
		
		return docIsViewable;
		
	}
			
	@Test(enabled = false)
    public void findUseableAccountNumber() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter,"DEV");
		driver = buildDriver(cf);
		new Login(driver).login("su","gw");
	
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		searchAccount = new BCSearchAccounts(driver);

		switch (dayOfMonth % 2) {
		case 0:
		try{
				acctNumber= searchAccount.findRecentAccountInGoodStanding("01-", AccountType.Insured);
				getQALogger().info("Farm Bureau Mutual account is " + acctNumber);
				companyIs = "IDFBINS";
			}
		catch(Exception e){
			getQALogger().error("No Good Standing Farm Bureau account found");
			throw new SkipException("No good standing account found , So this this test ends here ");
		}
			break;
		case 1:
		try{		
				acctNumber= searchAccount.findRecentAccountInGoodStanding("08-", AccountType.Insured);
				getQALogger().info("Western community insurance account is " + acctNumber);
				companyIs = "WCINS";
		}		
		catch(Exception e){
			getQALogger().error("No Good Standing Western community account found");
			throw new SkipException("No good standing account found , So this this test ends here ");
		}
			break;
		default:
			try {
				acctNumber= searchAccount.findRecentAccountInGoodStanding("08-", AccountType.Insured);
				getQALogger().info("Western community insurance account is " + acctNumber);
				companyIs = "WCINS";
		}		
		catch(Exception e){
			getQALogger().error("No Good Standing Western community account found");
			throw new SkipException("No good standing account found , So this this test ends here ");
			}
			break;
		}
	}	
	
	@Test(dependsOnMethods = { "findUseableAccountNumber" } , enabled = false)
	public void uploadDocumentAndVerifyViewabilityInImageRight() throws Exception {
		if(companyIs.equals("IDFBINS")){
	 		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		}else{
			this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		}
		Config cf = new Config(ApplicationOrCenter.BillingCenter,"DEV");
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), acctNumber);
		acctSummary = new BCAccountSummary(driver);
		policyNumber = acctSummary.getPolicyNumberFromOpenPolicyStatusTable(acctNumber.substring(0, 6));
        acctMenu = new BCAccountMenu(driver);
		
		testAllDocuments();
	}
	
}