package previousProgramIncrement.pi2_062818_090518.nonFeatures.NoExceptions;

import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonActivities;
import repository.bc.common.BCCommonDocuments;
import repository.bc.search.BCSearchAccounts;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AccountType;
import repository.gw.enums.DocumentStatus;
import repository.gw.enums.DocumentType;
import repository.gw.enums.OpenClosed;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import repository.pc.actions.ActionsPC;
import repository.pc.actions.NewDocumentPC;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author sgunda
 * @Requirement US15765 - **HOT-FIX** Correct Disputed Bank Statement back to "Debit/Credit Voucher" and create "Disputed Bank Statement"
 * @DATE Jul 21, 2018
 */

public class US15765TestPCBCDocsAndVerifyActivitiesNoExceptions extends BaseTest {

    private WebDriver driver;
    private BCAccountMenu acctMenu;
    private String documentDirectoryPath = "\\\\fbmsqa11.idfbins.com\\testing_data\\test-documents\\DocToUpload";
    private String dotExtension=".pdf";
    private ARUsers arUser = new ARUsers();
    private Underwriters uw ;
    private Underwriter.UnderwriterLine line;
    private BCSearchAccounts searchAccount;
    private BCCommonDocuments bcCommonDocuments;
    private List<String> accountAndPolicyNumber;
    private String activitySubject;

    private HashMap<String, DocumentType> docHashMap(){
        HashMap<String, DocumentType> documentsToTest = new HashMap<>();
        documentsToTest.put("DisputedBankStatement", DocumentType.Disputed_Bank_Statement);
        documentsToTest.put("ReceiptOfPayment", DocumentType.Receipt_Of_Payment);
        documentsToTest.put("DebitCreditVoucher", DocumentType.Debit_Credit_Voucher);
        return documentsToTest;
    }

    private void uploadDocumentInPC() {
        HashMap<String, DocumentType> documentsToTest = docHashMap();

        for(Map.Entry< String, DocumentType> pair : documentsToTest.entrySet()){
            String documentName = pair.getKey();
            DocumentType documentType = pair.getValue();
            ActionsPC actions = new ActionsPC(driver);
            actions.click_Actions();
            actions.click_NewDocument();
            actions.click_LinkToExistingDoc();
            new NewDocumentPC(driver).uploadAccountLevelDocument(documentDirectoryPath ,documentName.concat(dotExtension),"Policy",documentType);
            getQALogger().info("Successfully uploaded  Document " + documentType.getValue());
        }
    }

    private List<Boolean> verifyDocumentsInBCAndCheckIfTriggeredActivityOrNot() {
        List<Boolean> didAnythingBreak = new ArrayList<>();
        HashMap<String, DocumentType> documentsToTest = docHashMap();

        for(Map.Entry< String, DocumentType> pair : documentsToTest.entrySet()){
            String documentName = pair.getKey();
            DocumentType documentType = pair.getValue();

            acctMenu.clickBCMenuDocuments();

            String mainGWWindow = bcCommonDocuments.clickViewDocumentInTable(documentName, documentType, DocumentStatus.Final, null , null,null);
            boolean  docIsViewable= bcCommonDocuments.isDocumentPresentInImageRight();

           // if(!(bcCommonDocuments.verifyDocument(documentName, documentType, DocumentStatus.Final, null,null,null))){
             if(!docIsViewable){
                getQALogger().info(documentType.getValue() + " did not made its way to BC ");
                didAnythingBreak.add(true);
            }else{
                didAnythingBreak.add(false);
            }

            driver.switchTo().window(mainGWWindow);


            acctMenu.clickBCMenuActivities();
            BCCommonActivities bcCommonActivities = new BCCommonActivities(driver);

            if (documentType.equals(DocumentType.Disputed_Bank_Statement)) {
                activitySubject= "Disputed bank statement";
            } else if (documentType.equals(DocumentType.Receipt_Of_Payment)) {
                activitySubject= "Scanned Receipt of Payment Received";
            } else {
                activitySubject= "Credit/Debit Voucher";
            }

            if(!bcCommonActivities.verifyActivityTable(null,OpenClosed.Open, activitySubject)){
                getQALogger().info("Activity was not triggered when following doc was uploaded in PolicyCenter : " + documentType.getValue());
                didAnythingBreak.add(true);
            }else{
                didAnythingBreak.add(false);
            }
        }
        return didAnythingBreak;
    }

    @Test(enabled = false)
    public void findUsableAccountNumberAndUploadDocumentsInPC() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter, "DEV");
        driver = buildDriver(cf);
        new Login(driver).login("su","gw");

        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        searchAccount = new BCSearchAccounts(driver);

        switch (dayOfMonth % 2) {
            case 0:
                try{
                    accountAndPolicyNumber= searchAccount.findRecentAccountAndPolicyInGoodStanding("01-", AccountType.Insured);
                    getQALogger().info("Farm Bureau Mutual account is " + accountAndPolicyNumber.get(0));
                    line = Underwriter.UnderwriterLine.Personal;
                    this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
                }
                catch(Exception e){
                    getQALogger().error("No Good Standing Farm Bureau account found");
                    throw new SkipException("No good standing account found , So this this test ends here ");
                }
                break;
            case 1:
                try{
                    accountAndPolicyNumber= searchAccount.findRecentAccountAndPolicyInGoodStanding("08-", AccountType.Insured);
                    getQALogger().info("Western community insurance account is " + accountAndPolicyNumber.get(0));
                    line = Underwriter.UnderwriterLine.Commercial;
                    this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
                }
                catch(Exception e){
                    getQALogger().error("No Good Standing Western community account found");
                    throw new SkipException("No good standing account found , So this this test ends here ");
                }
                break;
            default:
                try {
                    accountAndPolicyNumber= searchAccount.findRecentAccountAndPolicyInGoodStanding("08-", AccountType.Insured);
                    getQALogger().info("Western community insurance account is " + accountAndPolicyNumber.get(0));
                    line = Underwriter.UnderwriterLine.Commercial;
                    this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
                }
                catch(Exception e){
                    getQALogger().error("No Good Standing Western community account found");
                    throw new SkipException("No good standing account found , So this this test ends here ");
                }
                break;
        }
        driver.quit();

        cf = new Config(ApplicationOrCenter.PolicyCenter, "DEV");
        driver = buildDriver(cf);

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(line, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.underwriterUserName, uw.underwriterPassword, accountAndPolicyNumber.get(1));
        uploadDocumentInPC();

    }

    @Test(dependsOnMethods = {"findUsableAccountNumberAndUploadDocumentsInPC"} , enabled = false)
    public void verifyIfDocumentsUploadedInPCMadeItsWayToBC() throws Exception {

        Config cf = new Config(ApplicationOrCenter.BillingCenter, "DEV");
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), accountAndPolicyNumber.get(0));
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuDocuments();
        bcCommonDocuments = new BCCommonDocuments(driver);
        bcCommonDocuments.waitUntilDocumentStatusChanges(240,"DebitCreditVoucher",DocumentType.Debit_Credit_Voucher,DocumentStatus.Final,null,null,null);
        List<Boolean> checkIfAnythingBreak = verifyDocumentsInBCAndCheckIfTriggeredActivityOrNot();
        Assert.assertFalse(checkIfAnythingBreak.contains(true),"Something went wrong please check the log to see what has failed ");

    }
}