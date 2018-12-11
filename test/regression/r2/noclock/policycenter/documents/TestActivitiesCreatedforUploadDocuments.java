package regression.r2.noclock.policycenter.documents;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.actions.NewDocumentPC;
import repository.pc.search.SearchAccountsPC;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : DE4273: Upload Documents -- No activity created when it should be
 * @Description -  Issue policy and verifying the activities got created at account level for each document type that we upload
 * @DATE Nov 28, 2016
 */
public class TestActivitiesCreatedforUploadDocuments extends BaseTest {
    private GeneratePolicy polToReturn;
    private String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents";
    private Underwriters underwriterInfo;
    private String environmentToOverride = "DEV";

    private WebDriver driver;

    @Test()
    public void testGenerateSquireAutoOnlyIssuance() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, environmentToOverride);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        polToReturn = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withInsFirstLastName("Test", "Auto")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testGenerateSquireAutoOnlyIssuance"})
    public void testLinktoExistingDocuments() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, environmentToOverride);
        driver = buildDriver(cf);

        underwriterInfo = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(), polToReturn.squire.getPolicyNumber());

        DocumentType[] documents = {DocumentType.Appraisals, DocumentType.SeniorCitizenDiscount, DocumentType.GoodStudentDiscount, DocumentType.StarMotorcycleCourse, DocumentType.LetterOfTestamentary, DocumentType.PowerOfAttorney, DocumentType.Renewal_Documents_With_Changes, DocumentType.Renewal_Documents_Without_changes, DocumentType.Binder_Lien_Letters,
                DocumentType.Clue_Reports, DocumentType.Drivers_License, DocumentType.Inspection_Report, DocumentType.Loss_Run, DocumentType.Photos, DocumentType.Prior_Policy_Information, DocumentType.Questionnaire, DocumentType.Registration, DocumentType.UMUIM_Rejection_Letter,
                DocumentType.ISRB_Documents, DocumentType.Cost_Estimator, DocumentType.Rating_Worksheet, DocumentType.Secretary_Of_State, DocumentType.Professional_Audit,
                DocumentType.Signature_Page, DocumentType.Treaty_Exceptions, DocumentType.Reinstatement_Document, DocumentType.SR22, DocumentType.SR26, DocumentType.Website_Information, DocumentType.Other_Supporting_Documents};

        for (DocumentType document : documents) {
            linkExistingDocument(document);
        }

        SearchAccountsPC accountSearchPC = new SearchAccountsPC(driver);
        accountSearchPC.searchAccountByAccountNumber(polToReturn.accountNumber);
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        String[] activities = {"Appraisal Received", "Senior Discount Doc Received", "Good Student Doc Received", "STAR Document Received", "Power of Attorney added",
                "Letter of Testamentary Received", "SR22 Received", "SR26 Received", "Renewal Documents with changes", "Renewal Documents without changes",
                "Professional Audit Report Received", "ISRB Documents Received", "Cost Estimator Received", "Rating Worksheet Received", "Secretary of State Received",
                "Signature Page Received", "Treaty Exceptions Received", "Binder/Letter Received", "Clue Report Received", "Driver's License Received",
                "Inspection Reports Received", "Loss Run Received", "Photos Received", "Prior Policy Information Received", "Questionnaire Received",
                "Registration Received", "UM/UIM Rejection Letter Received", "Reinstate Documents", "Information Received", "Documents Received"};

        String errorMessage = "";
        for (String activity : activities) {

            if (!summary.getActivitySubjectFromTable().contains(activity)) {
                errorMessage = errorMessage + "Expected Actvity : " + activity + " is not displayed for document upload \n";
            }
        }

        if (errorMessage != "")
            Assert.fail(errorMessage);
    }

    private void linkExistingDocument(DocumentType documentType) {
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_NewDocument();
        actions.click_LinkToExistingDoc();
        System.out.println("Uploading Doc Type: " + documentType);
        NewDocumentPC doc = new NewDocumentPC(driver);
        doc.setAttachment(documentDirectoryPath, "\\just_a_doc");
        // delay required to upload the documents
        doc.selectRelatedTo("Account");
        doc.selectDocumentType(documentType);
        doc.sendArbitraryKeys(Keys.TAB);
        doc.clickUpdate();
    }

}
