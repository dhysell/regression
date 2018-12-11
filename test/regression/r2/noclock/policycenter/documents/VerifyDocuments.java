/*
 * Jon Larsen 6/19/2015
 *
 * This test is to be used for checking the existence of documents
 *
 * This test is to ensure that the correct uploaded documents are being sent to
 * Billing Center to Policy Center.
 * Verifies that the correct Activity was created for uploaded document.
 *
 * Documents Used are located on S:\\QA (just_a_doc.txt, just_another_doc.txt)
 *
 * Tests for: DE2459
 * Documents sent over to BC currently stored as document type "unknown scanned document"
 * This should be fixed after next snapshot refresh/merge and needs to be validated
 *
 * VerifyDocTypesPC_BC_DE2459
 *
 */
package regression.r2.noclock.policycenter.documents;

import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonActivities;
import repository.bc.common.BCCommonDocuments;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchPolicies;
import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;
import repository.driverConfiguration.Config;
import repository.gw.enums.DocumentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.actions.ActionsPC;
import repository.pc.actions.NewDocumentPC;
import repository.pc.desktop.DesktopMyActivitiesPC;
import repository.pc.search.SearchAccountsPC;
import repository.pc.search.SearchDocumentsPC;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import persistence.globaldatarepo.helpers.RoundTripHelper;
import scratchpad.jon.mine.GenerateTestPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@QuarantineClass
public class VerifyDocuments extends BaseTest {

    private String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents";
    private String accountNumber = "";
    GeneratePolicy myPolicyObj = null;

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

    // private List<String> requiredDocuments = new ArrayList<String>() {
    // private static final long serialVersionUID = 1L;
    // {
    // this.add("BOP Disclosure Letter");
    // this.add("Businessowners Transition Application");
    // //this.add("Evidence of Commercial Property Insurance");
    // //this.add("Loss Payable Clauses");
    // //this.add("Renewal Billing");
    // this.add("Evidence of Commercial Property Insurance");
    // this.add("Loss Payable Clauses");
    // this.add("Businessowners Coverage Form");
    // this.add("Idaho Changes");
    // this.add("Employment - Related Practices Exclusion");
    // this.add("Abuse or Molestation Exclusion");
    // this.add("Exclusion - Silica or Silica-Related Dust");
    // this.add("Exclusion - Certified Acts of Terrorism Involving Nuclear,
    // Biological, Chemical or Radiological Terrorism");
    // this.add("Fungi or Bacteria Exclusion (Liability)");
    // this.add("Amendment of Insured Contract Definition");
    // this.add("Exclusion - Year 2000 Computer-Related and Other Electronic
    // Problems");
    // this.add("Businessowners Coverage Form Index");
    // this.add("Businessowners Policy Declarations");
    // this.add("Pollutants Definition Endorsement");
    // this.add("Equipment Breakdown Enhancement Endorsement");
    // this.add("Affiliate and Subsidiary Definition Endorsement");
    // this.add("Mobile Equipment Modification Endorsement");
    // this.add("Terrorism Coverage and Premium");
    // this.add("Premium Rounding and Waiver Endorsement");
    // this.add("U.S. Treasury Department's Office of Foreign Assets Control
    // (\"OFAC\")");
    //
    // }};

    private List<String> accountsReceivable = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            this.add("sbrunson");
            this.add("hhill");
            this.add("proberts");
            this.add("jbriggs");
            this.add("jwilliams");
            this.add("tnelson");
            this.add("bcarling");

        }
    };

    DocumentType documentType = sensativeDocTypeList.get(new Random().nextInt(sensativeDocTypeList.size()));
    DocumentType regDocType = regularDocType.get(new Random().nextInt(regularDocType.size()));
    String userAR = accountsReceivable.get(new Random().nextInt(accountsReceivable.size()));

    private WebDriver driver;

    @Test(enabled = true)
    public void createPolicy() throws Exception {

        GenerateTestPolicy policy = new GenerateTestPolicy();
        policy.generateRandomPolicy(1, 1, 1, "Verify Documents");
        this.myPolicyObj = policy.myPolicy;

        System.out.println(accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentUserName());

    }

    @Test(dependsOnMethods = {"createPolicy"})
    public void saveforRoundTrip() throws Exception {
        RoundTripHelper.saveRoundTripAccount(Integer.valueOf(myPolicyObj.accountNumber), myPolicyObj.agentInfo.getAgentUserName());
    }

    // upload 5 documents: BankInfo, BankruptcyPapers, CheckRequests,
    // DebitCreditVouchers
    // and 1 random document for negative testing purposes.
    @Test(dependsOnMethods = {"createPolicy"})
    public void uploadDocuments() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword());

        SearchPoliciesPC search = new SearchPoliciesPC(driver);
        search.searchPolicyByAccountNumber(myPolicyObj.accountNumber);

        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_NewDocument();
        actions.click_LinkToExistingDoc();
        System.out.println("Uploading Doc Type: " + documentType);
        NewDocumentPC doc = new NewDocumentPC(driver);
        doc.setAttachment(documentDirectoryPath, "\\just_a_doc");
        doc.selectRelatedTo("Account");
        doc.selectDocumentType(documentType);
        doc.clickUpdate();


        actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_NewDocument();
        actions.click_LinkToExistingDoc();
        System.out.println("Uploading Regular Doc Type: " + regDocType);
        doc = new NewDocumentPC(driver);
        doc.setAttachment(documentDirectoryPath, "\\just_another_doc");
        doc.selectRelatedTo("Account");
        doc.selectDocumentType(regDocType);
        doc.clickUpdate();

        //verify document upload.
        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuToolsDocuments();

    }//END uploadDocuments

    // /**
    // * @Author sbroderick
    // * @Requirement Documents Generated by the system should not be editable.
    // * @RequirementsLink <a
    // href="http://projects.idfbins.com/policycenter/_layouts/15/WopiFrame.aspx?sourcedoc=/policycenter/To%20Be%20Process/Requirements%20Documentation/12.0%20-%20Tools.pptx&action=default">http://projects.idfbins.com/policycenter/_layouts/15/WopiFrame.aspx?sourcedoc=/policycenter/To%20Be%20Process/Requirements%20Documentation/12.0%20-%20Tools.pptx&action=default</a>
    // * @Description Ensures that documents that are generated by the system
    // are uneditable
    // * @DATE Sep 30, 2015
    // * @throws Exception
    // */
    // @Test(dependsOnMethods={"uploadDocuments"})
    // public void checkEditability() throws Exception {
    // //use uat 157785
    // Boolean docEditable = false;
    //
    // Configuration.setProduct(Product.PolicyCenter);
    //
    // Login login = new Login(driver);
    // login.login(myPolicyObj.agentInfo.getAgentUserName(),
    // myPolicyObj.agentInfo.getAgentPassword());
    //
    // gw.search.interfaces.ISearchPolicies search =
    // gw.search.SearchFactory.getPolicySearch();
    // search.searchPolicyByAccountNumber(myPolicyObj.accountNumber);
    //
    // ISideMenu sidebar = SideMenuFactory.getMenu();
    // sidebar.clickSideMenuToolsDocuments();
    //
    // PolicyDocuments docs = PolicyFactory.getPolicyDocuments(driver);
    // docs.clickReset();
    // docs.clickSearch();
    //
    // ISearchDocuments searchDocs =
    // gw.search.SearchFactory.getDocumentsSearch();
    //
    // PolicyDocuments docDetails =
    // pc.policy.PolicyFactory.getPolicyDocumentsPage();
    //
    //
    // //List<WebElement> documentsOnPolicy = docs.getSearchResults();
    // for(String doc : requiredDocuments) {
    // WebElement document =
    // searchDocs.verifyDocumentExistsDescriptionElement(doc);
    // document.click();
    // boolean editExists = docDetails.clickDocumentDetailsEdit();
    // docDetails = pc.policy.PolicyFactory.getPolicyDocumentsPage();
    // if(editExists){
    // throw new
    // GuidewirePolicyCenterException(Configuration.getWebDriver().getCurrentUrl(),
    // myPolicyObj.accountNumber, "The system document, "+ doc + " was
    // editable.");
    // }
    // docDetails.clickReturnToDocuments();
    // searchDocs = gw.search.SearchFactory.getDocumentsSearch();
    // }
    // }

    // verify that only AR&Accounting roles can see Bankinfo documents
    @Test(dependsOnMethods = {"uploadDocuments"})
    public void verifyPCDocumentsAR() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber(userAR, "gw", myPolicyObj.accountNumber);
        SideMenuPC menu = new SideMenuPC(driver);
        menu.clickSideMenuToolsDocuments();
        SearchDocumentsPC docs = new SearchDocumentsPC(driver);
        WebElement document = docs.verifyDocumentExistsDescriptionElement(documentType.getValue());
        if (document == null) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + " could not find document of type: " + documentType.getValue());
        }
        if (document.findElements(By.xpath(".//parent::div/parent::td/preceding-sibling::td[1]/div/a[contains(text(), 'just_a_doc')]")).size() <= 0) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "User with Role of 'Accounts Receivable' was unable to see the uploaded document of type: " + documentType.getValue());
        }

        docs = new SearchDocumentsPC(driver);
        document = docs.verifyDocumentExistsDescriptionElement(regDocType.getValue());
        if (document != null) {
            if (document.findElements(By.xpath(".//parent::div/parent::td/preceding-sibling::td[1]/div/a[contains(text(), 'just_another_doc')]")).size() <= 0) {
                Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "User with Role of 'Accounts Receivable' was unable to see the uploaded document of type: " + regDocType.getValue());
            }
        }
    }

    // verify agent cannot view bank info doc
    @Test(dependsOnMethods = {"uploadDocuments"})
    public void verifyPCDocumentsAgent() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword());

        SearchPoliciesPC search = new SearchPoliciesPC(driver);
        search.searchPolicyByAccountNumber(myPolicyObj.accountNumber);
        SideMenuPC menu = new SideMenuPC(driver);
        menu.clickSideMenuToolsDocuments();
        SearchDocumentsPC docs = new SearchDocumentsPC(driver);
        WebElement document;
        document = docs.verifyDocumentExistsDescriptionElement(documentType.getValue());
        if (document != null) {
            if (documentType.equals(DocumentType.Bank_Information) || documentType.equals(DocumentType.Bankruptcy_Papers)) {
                if (document.findElements(By.xpath(".//parent::div/parent::td/preceding-sibling::td[1]/div/a[contains(text(), 'just_a_doc')]")).size() > 0) {
                    Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + " User with Role of 'Agent' was able to see the uploaded document of type: " + documentType.getValue() + " when they should NOT be allowed to.");
                }
            }
        }

        docs = new SearchDocumentsPC(driver);
        document = docs.verifyDocumentExistsDescriptionElement(regDocType.getValue());
        if (document != null) {
            if (document.findElements(By.xpath(".//parent::div/parent::td/preceding-sibling::td[1]/div/a[contains(text(), 'just_another_doc')]")).size() <= 0) {
                Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + " User with Role of 'Agent' was unable to see the uploaded document of type: " + documentType.getValue() + " when they should be allowed to.");
            }
        }
    }

    // log into BC and verify Documents were set and correct DocType
    @Test(dependsOnMethods = {"uploadDocuments"})
    public void verifyBCDocuments() throws Exception {

        Boolean docFound = false;
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("sbrunson", "gw");

        BCSearchPolicies search = new BCSearchPolicies(driver);
        search.searchPolicyByAccountNumber(myPolicyObj.accountNumber);
        BCPolicyMenu sidebar = new BCPolicyMenu(driver);
        sidebar.clickBCMenuDocuments();
        BCCommonDocuments docs = new BCCommonDocuments(driver);
        docs.clickReset();
        docs.clickSearch();
        List<WebElement> documents = docs.getSearchResults();
        for (WebElement element : documents) {
            if (element.findElement(By.xpath(".//td[3]")).getText().equalsIgnoreCase("just_a_doc")) {
                // kind of a reverse look up because the type printed does not
                // always match the Enum string.
                // same reasoning for replacing the / with a space.
                if (documentType.getValue().replace("/", " ").contains(element.findElement(By.xpath(".//td[5]")).getText())) {
                    System.out.println(DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.PolicyCenter, "MMddyyyy"));
                    if (element.findElement(By.xpath(".//td[8]")).getText().replace("/", "").equalsIgnoreCase(DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.PolicyCenter, "MMddyyyy"))) {
                        docFound = true;
                        break;
                    }
                }
            }
        }

        if (!docFound) {
            Assert.fail("Unable to find uploaded document: just_a_doc, or Type: " + documentType.getValue() + " in BC from PC");
        }

        // VERIFY THE OTHER DOCUMENT DID NOT COME OVER.
    }

    // verify the proper activity was created per each document uploaded
    // 12.6.6.2
    @Test(dependsOnMethods = {"uploadDocuments"})
    public void verifyDocumentsPCActivity() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login(myPolicyObj.underwriterInfo.getUnderwriterUserName(),
                myPolicyObj.underwriterInfo.getUnderwriterPassword());

        DesktopMyActivitiesPC activity = new DesktopMyActivitiesPC(driver);

        try {
            switch (regDocType.getValue()) {
                case "Audit":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Audit Report Received", myPolicyObj.accountNumber, "");
                    break;
                case "BinderLienLetters":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Bind/Letter Received", myPolicyObj.accountNumber, "");
                    break;
                case "ClueReports":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Clue Report Received", myPolicyObj.accountNumber, "");
                    break;
                case "CostEstimator":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Cost Estimator Received", myPolicyObj.accountNumber, "");
                    break;
                case "DriversLicense":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Drivers's License Received", myPolicyObj.accountNumber, "");
                    break;
                case "InspectionReport":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Inspection Reports Received", myPolicyObj.accountNumber, "");
                    break;
                case "ISRBDocuments":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("ISRB Documents Received", myPolicyObj.accountNumber, "");
                    break;
                case "LossRun":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Loss Run Received", myPolicyObj.accountNumber, "");
                    break;
                case "Photos":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Photos Received", myPolicyObj.accountNumber, "");
                    break;
                case "PriorPolicyInformation":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Prior Policy Information Received", myPolicyObj.accountNumber, "");
                    break;
                case "Questionnaire":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Questionnaire Received", myPolicyObj.accountNumber, "");
                    break;
                case "RatingWorksheet":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Rating Worksheet Received", myPolicyObj.accountNumber, "");
                    break;
                case "Registration":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Registration Received", myPolicyObj.accountNumber, "");
                    break;
                case "Secretary of State":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Secretary of State Received", myPolicyObj.accountNumber, "");
                    break;
                case "Signature Page":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Signature Page Received", myPolicyObj.accountNumber, "");
                    break;
                case "Treaty Exceptions":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Treaty Exceptions Received", myPolicyObj.accountNumber, "");
                    break;
                case "UM/UIM Rejection Letter":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("UM/UIM Rejection Letter Received", myPolicyObj.accountNumber, "");
                    break;
                case "Website Information":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Information Received", myPolicyObj.accountNumber, "");
                    break;
                case "Other Supporting Documents":
                    activity.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Documents Received", myPolicyObj.accountNumber, "");
                    break;
            }
        } catch (Exception e) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "Was Unable to find and select Activity associated with document type: " + regDocType.getValue());
        }

    }

    @Test(dependsOnMethods = {"uploadDocuments"})
    public void verifyDocumentsBCActivity() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("sbrunson", "gw");

        SearchAccountsPC search = new SearchAccountsPC(driver);
        search.searchAccountByAccountNumber(myPolicyObj.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuActivities();
        BCCommonActivities bcActivity = new BCCommonActivities(driver);
        if (!bcActivity.isActivity(documentType.getValue())) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + " Unable to locate Activity associated with Uploaded PC document: " + documentType.getValue());
        }
    }

}
