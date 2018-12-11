package regression.r2.noclock.policycenter.other;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.actions.NewDocumentPC;
import repository.pc.activity.GenericActivityPC;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

@QuarantineClass
public class TestActivityErrorCheck extends BaseTest {

    GeneratePolicy myPolicyObj = null;

    private String agentUserName = null;
    private String password = null;
    private String userNameUW = null;
    private String accountNumber = null;
    private String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents";
    private DocumentType documentType = null;
    DocumentType regDocType;
    private String policyNumber = null;
    private String loginUserName = null;
    private String doc1 = "doc_test.doc";
    private String doc2 = "JPG Document.jpg";
    private ArrayList<String> documentNames = new ArrayList<String>();


    private void agentOrUnderWriter() throws Exception {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        if (dayOfMonth % 2 == 0) {
            this.loginUserName = myPolicyObj.agentInfo.getAgentUserName();
        } else {
            this.loginUserName = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Commercial, Underwriter.UnderwriterTitle.Underwriter).getUnderwriterUserName();
        }
    }

    private WebDriver driver;

    @Test(
            enabled = true,
            description = "Runs Policy Through Quick Quote App")

    public void generatePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        documentType = sensativeDocTypeList.get(new Random().nextInt(sensativeDocTypeList.size()));
        regDocType = regularDocType.get(new Random().nextInt(regularDocType.size()));

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompanyDependingOnDay("Activit", "Error", "Activity Error")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        agentOrUnderWriter();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();

        this.agentUserName = myPolicyObj.agentInfo.getAgentUserName();
        this.password = myPolicyObj.agentInfo.getAgentPassword();
        this.accountNumber = myPolicyObj.accountNumber;

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("Agent: " + agentUserName + "\n#############");

        Login login = new Login(driver);
        login.loginAndSearchAccountByAccountNumber(loginUserName, password, accountNumber);

        ActionsPC myActions = new ActionsPC(driver);
        myActions.clickToNewMail();
        myActions.click_ReviewNewMail();

        GenericActivityPC myActivity = new GenericActivityPC(driver);
        myActivity.setSubject("Review New Mail");
        myActivity.setText("Review New Mail check");
        myActivity.clickOK();

        myActions = new ActionsPC(driver);
        myActions.clickToRequest();
        myActions.click_LegalReview();

        myActivity = new GenericActivityPC(driver);
        myActivity.setTargetDueDate(DateUtils.dateFormatAsString("MMddyyyy", DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -2)));
        myActivity.sendArbitraryKeys(Keys.TAB);
        myActivity = new GenericActivityPC(driver);
        myActivity.setSubject("Activity Escalation Check");
        myActivity = new GenericActivityPC(driver);
        myActivity.setText("Activity Escalation Check");
        myActivity.clickOK();

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickWorkOrderbySuffix("001");

        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_NewDocument();
        actions.click_LinkToExistingDoc();
        System.out.println("Uploading Doc Type: " + documentType);
        NewDocumentPC doc = new NewDocumentPC(driver);
        doc.setAttachment(documentDirectoryPath, "\\just_a_doc");
        doc.selectRelatedTo("Account");
        doc.selectDocumentType(DocumentType.Reinstatement_Document);
        doc.clickUpdate();

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("Login UserName: " + loginUserName);
        System.out.println("Agent: " + agentUserName + "\n#############");

    }

    /**
     * @Author bmartin
     * @Description Checks Review New Mail Activity for errors
     * @DATE Feb 19, 2016
     */
    @Test(
            enabled = true,
            description = "Checks Review new mail Activity",
            dependsOnMethods = {"generatePolicy"})
    public void checkActivityErrorNewMail() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);


        new Login(driver).loginAndSearchAccountByAccountNumber(loginUserName, password, accountNumber);

        AccountSummaryPC accSum = new AccountSummaryPC(driver);
        if (accSum.finds(By.xpath("//a[contains(text(), 'Review new mail')]")).isEmpty()) {
            Assert.fail(driver.getCurrentUrl() + accountNumber + "No Activity with subject of Review new Mail was found.");
        }
        accSum.clickActivitySubject("Review new mail");

        ErrorHandling errorHandle = new ErrorHandling(driver);
        if (errorHandle.text_ErrorHandlingErrorBannerMessages().size() > 0) {
            String errorMsg = errorHandle.text_ErrorHandlingErrorBannerMessages().get(0).getText();
            System.out.println("When Checking the Review New Mail Activity this error occurred: " + errorMsg);
        }
    }

    /**
     * @Author bmartin
     * @Description Checks an activity that has been Escalated for errors
     * @DATE Feb 19, 2016
     */
    @Test(
            enabled = true,
            description = "Checks an activity that has been Escalated.",
            dependsOnMethods = {"generatePolicy"})
    public void checkActivityErrorEscalated() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(loginUserName, password, accountNumber);

        AccountSummaryPC accSum = new AccountSummaryPC(driver);
        if (accSum.finds(By.xpath("//a[contains(text(), 'Legal review')]")).isEmpty()) {
            Assert.fail(driver.getCurrentUrl() + accountNumber + "No Activity with subject of Legal review was found.");
        }
        accSum.clickActivitySubject("Legal review");

        ErrorHandling errorHandle = new ErrorHandling(driver);
        if (errorHandle.text_ErrorHandlingErrorBannerMessages().size() > 0) {
            String errorMsg = errorHandle.text_ErrorHandlingErrorBannerMessages().get(0).getText();
            System.out.println("When Checking the Escalated Activity this error occurred: " + errorMsg);
        }
    }

    /**
     * @Author bmartin
     * @Description Checks an activity that has a document added for errors
     * @DATE Feb 19, 2016
     */
    @Test(
            enabled = true,
            description = "Checks an activity that has a document added.",
            dependsOnMethods = {"generatePolicy"})
    public void checkActivityErrorDocuments() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(loginUserName, password, accountNumber);

        AccountSummaryPC accSum = new AccountSummaryPC(driver);
        if (accSum.finds(By.xpath("//a[contains(text(), 'Reinstate Documents')]")).isEmpty()) {
            Assert.fail(driver.getCurrentUrl() + accountNumber + "No Activity with subject of Reinstate Documents was found.");
        }
        accSum.clickActivitySubject("Reinstate Documents");

        ErrorHandling errorHandle = new ErrorHandling(driver);
        if (errorHandle.text_ErrorHandlingErrorBannerMessages().size() > 0) {
            String errorMsg = errorHandle.text_ErrorHandlingErrorBannerMessages().get(0).getText();
            System.out.println("When Checking the Document Activity this error occurred: " + errorMsg);
        }
    }

    @Test(
            enabled = true,
            dependsOnMethods = {"generatePolicy"},
            description = "Setups App through Issued")
    public void fullIssuedApp() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj.convertTo(driver, GeneratePolicyType.PolicyIssued);

        this.policyNumber = myPolicyObj.busOwnLine.getPolicyNumber();
        this.userNameUW = myPolicyObj.underwriterInfo.getUnderwriterUserName();

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("PolicyNumber: " + policyNumber);
        System.out.println("Under Writer: " + userNameUW + "\n#############");

    }

    @Test(
            enabled = true,
            dependsOnMethods = {"fullIssuedApp"},
            description = "Checks upload of multiple documents")
    public void checkMultiDocUpload() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // FilePath Names and Document Names get added to the list.
        String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents\\";
        documentNames.add(doc1);
        documentNames.add(doc2);

        new Login(driver).loginAndSearchAccountByAccountNumber(agentUserName, password, accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickPolicyNumber(policyNumber);


        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_NewDocument();
        actions.click_AttachMultipleDocuments();

        // The elements below are in an iframe so we must switch to the iframe to interact with them.

        driver.switchTo().frame(actions.find(By.xpath("//div[contains(@id,'NewMultiDocumentLinkedWorksheet:NewMultiDocumentLinkedScreen:0-body')]//iframe")));

        NewDocumentPC doc = new NewDocumentPC(driver);
        doc.selectRandom_RelatedTo();
        doc.selectRandom_MultiDocumentType();
        doc.setMultiDocumentAttachments(documentDirectoryPath, documentNames);
        doc.clickUploadFiles();

        // Switch out of the iframe to main window.
        driver.switchTo().defaultContent();

    }

    private List<DocumentType> sensativeDocTypeList = new ArrayList<DocumentType>() {
        private static final long serialVersionUID = 1L;

        {
            this.add(DocumentType.Bank_Information);
            this.add(DocumentType.Bankruptcy_Papers);
            this.add(DocumentType.Check_Request);
            this.add(DocumentType.Disputed_Bank_Statement);
        }
    };

    private List<DocumentType> regularDocType = new ArrayList<DocumentType>() {
        private static final long serialVersionUID = 1L;

        {
            this.add(DocumentType.Audit);
            this.add(DocumentType.Binder_Lien_Letters);
            this.add(DocumentType.Clue_Reports);
            this.add(DocumentType.Cost_Estimator);
            this.add(DocumentType.Drivers_License);
            this.add(DocumentType.Inspection_Report);
            this.add(DocumentType.ISRB_Documents);
            this.add(DocumentType.Loss_Run);
            this.add(DocumentType.Other_Supporting_Documents);
            this.add(DocumentType.Receipt_Of_Payment);
            this.add(DocumentType.Photos);
            this.add(DocumentType.Prior_Policy_Information);
            this.add(DocumentType.Questionnaire);
            this.add(DocumentType.Rating_Worksheet);
            this.add(DocumentType.Registration);
            this.add(DocumentType.Reinstatement_Document);
            this.add(DocumentType.Secretary_Of_State);
            this.add(DocumentType.Signature_Page);
            this.add(DocumentType.Treaty_Exceptions);
            this.add(DocumentType.UMUIM_Rejection_Letter);
            this.add(DocumentType.Website_Information);
        }
    };

}
