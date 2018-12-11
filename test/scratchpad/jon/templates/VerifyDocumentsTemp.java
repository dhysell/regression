package scratchpad.jon.templates;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.DocumentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import repository.pc.actions.ActionsPC;
import repository.pc.actions.NewDocumentPC;
import scratchpad.jon.mine.GenerateTestPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * this class to put put documents tests that will eventually be moved to verify documents regression test.
 *
 * @author jlarsen
 */
public class VerifyDocumentsTemp extends BaseTest {

    private String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents";
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

    DocumentType documentType = sensativeDocTypeList.get(new Random().nextInt(sensativeDocTypeList.size()));
    DocumentType regDocType = regularDocType.get(new Random().nextInt(regularDocType.size()));

    private WebDriver driver;

    @Test(enabled = true)
    public void createPolicy() throws Exception {

        GenerateTestPolicy policy = new GenerateTestPolicy();
        policy.generateRandomPolicy("Verify Documents Temp");
        this.myPolicyObj = policy.myPolicy;

        System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentUserName());
    }

    //upload 5 documents: BankInfo, BankruptcyPapers, CheckRequests, DebitCreditVouchers
    // and 1 random document for negative testing purposes.
    @Test(dependsOnMethods = {"createPolicy"})
    public void uploadDocuments() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);


        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_NewDocument();
        actions.click_LinkToExistingDoc();
        NewDocumentPC doc = new NewDocumentPC(driver);
        doc.setAttachment(documentDirectoryPath, "\\just_a_doc");
        doc.selectRelatedTo("Account");
        doc.selectDocumentType(documentType);
        doc.clickUpdate();


        actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_NewDocument();
        actions.click_LinkToExistingDoc();
        doc = new NewDocumentPC(driver);
        doc.setAttachment(documentDirectoryPath, "\\just_another_doc");
        doc.selectRelatedTo("Account");
        doc.selectDocumentType(regDocType);
        doc.clickUpdate();

    }

}
