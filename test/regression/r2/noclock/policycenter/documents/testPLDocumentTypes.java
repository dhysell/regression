package regression.r2.noclock.policycenter.documents;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.common.BCCommonDocuments;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.actions.NewDocumentPC;
import repository.pc.search.SearchAccountsPC;
import repository.pc.sidemenu.SideMenuPC;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US6660 - Verify document Typesand add new if required
 * @RequirementsLink <a href="http://http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Common-Admin/PC8%20-%20PolicyCenter%20Common%20-%20CommonDocument%20-%20Link%20to%20an%20Existing%20Document.xlsx ">Link to Existing Document Story Card</a>
 * @Description
 * @DATE Sep 13, 2016
 */
public class testPLDocumentTypes extends BaseTest {
    private GeneratePolicy squireFullApp;
    private String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents";
    private Underwriters uw;
    private String environmentToOverride = "DEV";
    private WebDriver driver;


    @Test()
    public void testCreateSquireSubmission() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, environmentToOverride);
        driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK);
        coverages.setUninsuredLimit(UninsuredLimit.CSL300K);
        coverages.setUnderinsuredLimit(UnderinsuredLimit.CSL300K);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability liabilitySection = new SquireLiability();
        liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
        SectionIICoverages sectionIIcoverage = new SectionIICoverages(SectionIICoveragesEnum.PrivateLandingStrips, 0, 1);
        liabilitySection.getSectionIICoverageList().add(sectionIIcoverage);

        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -2);

        this.squireFullApp = new GeneratePolicy.Builder(driver)
                .withInsFirstLastName("Activity", "Document")
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withPolEffectiveDate(newEff)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }

    @Test(dependsOnMethods = {"testCreateSquireSubmission"})
    private void testLinktoExistingDocuments() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, environmentToOverride);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.squireFullApp.squire.getPolicyNumber());
        linkExistingDocument(DocumentType.Appraisals);
        linkExistingDocument(DocumentType.SeniorCitizenDiscount);
        linkExistingDocument(DocumentType.GoodStudentDiscount);
        linkExistingDocument(DocumentType.StarMotorcycleCourse);
        linkExistingDocument(DocumentType.LetterOfTestamentary);
        linkExistingDocument(DocumentType.PowerOfAttorney);
        linkExistingDocument(DocumentType.SR22);
        linkExistingDocument(DocumentType.SR26);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        BCCommonDocuments docs = new BCCommonDocuments(driver);
        boolean appraisalFound = false;
        boolean seniorDiscountFound = false;
        boolean studentFound = false;
        boolean motorcycleFound = false;
        boolean testamentaryFound = false;
        boolean powerAttorneyFound = false;
        boolean sr22Found = false;
        boolean sr26Found = false;

        for (int currentRow = 1; currentRow <= docs.getDocumentTableRowCount(); currentRow++) {
            if (docs.getDocumentNameByRow(currentRow).contains("just_a_doc")) {
                if (docs.getDocumentDescriptionByRow(currentRow).contains(DocumentType.Appraisals.getValue())) {
                    appraisalFound = true;
                }
                if (docs.getDocumentDescriptionByRow(currentRow).contains(DocumentType.SeniorCitizenDiscount.getValue())) {
                    seniorDiscountFound = true;
                    continue;
                }
                if (docs.getDocumentDescriptionByRow(currentRow).contains(DocumentType.GoodStudentDiscount.getValue())) {
                    studentFound = true;
                    continue;
                }
                if (docs.getDocumentDescriptionByRow(currentRow).contains(DocumentType.StarMotorcycleCourse.getValue())) {
                    motorcycleFound = true;
                    continue;
                }
                if (docs.getDocumentDescriptionByRow(currentRow).contains(DocumentType.LetterOfTestamentary.getValue())) {
                    testamentaryFound = true;
                    continue;
                }
                if (docs.getDocumentDescriptionByRow(currentRow).contains(DocumentType.PowerOfAttorney.getValue())) {
                    powerAttorneyFound = true;
                    continue;
                }
                if (docs.getDocumentDescriptionByRow(currentRow).contains(DocumentType.SR22.getValue())) {
                    sr22Found = true;
                    continue;
                }
                if (docs.getDocumentDescriptionByRow(currentRow).contains(DocumentType.SR26.getValue())) {
                    sr26Found = true;
                    continue;
                }
            }
        }


        //to check values in the search - document type dropdown - selecting the values
        docs.selectDocumentType(DocumentType.Appraisals);
        docs.selectDocumentType(DocumentType.SeniorCitizenDiscount);
        docs.selectDocumentType(DocumentType.GoodStudentDiscount);
        docs.selectDocumentType(DocumentType.StarMotorcycleCourse);
        docs.selectDocumentType(DocumentType.LetterOfTestamentary);
        docs.selectDocumentType(DocumentType.PowerOfAttorney);
        docs.selectDocumentType(DocumentType.SR22);
        docs.selectDocumentType(DocumentType.SR26);

        String errorMessage = "";
        if (!appraisalFound && !seniorDiscountFound && !studentFound && !motorcycleFound && !testamentaryFound && !powerAttorneyFound && !sr22Found && !sr26Found) {
            errorMessage = "Expected PL - PC documents 'linking to existing doc' are not available on document search \n";
        }

        SearchAccountsPC accountSearchPC = new SearchAccountsPC(driver);
        accountSearchPC.searchAccountByAccountNumber(this.squireFullApp.accountNumber);
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        if (!summary.verifyCurrentActivity("Appraisal Received", 500)) {
            errorMessage = errorMessage + "Expected Actvity : Appraisal Received is not displayed for document upload \n";
        }

        if (!summary.verifyCurrentActivity("Senior Discount Doc Received", 500)) {
            errorMessage = errorMessage + "Expected Actvity : Senior Discount Doc Received is not displayed for document upload \n";
        }

        if (!summary.verifyCurrentActivity("Good Student Doc Received", 500)) {
            errorMessage = errorMessage + "Expected Actvity : Good Student Doc Received is not displayed for document upload \n";
        }

        if (!summary.verifyCurrentActivity("STAR Document Received", 500)) {
            errorMessage = errorMessage + "Expected Actvity : STAR Document Received is not displayed for document upload \n";
        }

        if (!summary.verifyCurrentActivity("Power of Attorney added", 500)) {
            errorMessage = errorMessage + "Expected Actvity : Power of Attorney added is not displayed for document upload \n";
        }

        if (!summary.verifyCurrentActivity("Letter of Testamentary Received", 500)) {
            errorMessage = errorMessage + "Expected Actvity : Letter of Testamentary Received is not displayed for document upload \n";
        }

        if (!summary.verifyCurrentActivity("SR22 Received", 500)) {
            errorMessage = errorMessage + "Expected Actvity : SR22 Received is not displayed for document upload \n";
        }

        if (!summary.verifyCurrentActivity("SR26 Received", 500)) {
            errorMessage = errorMessage + "Expected Actvity : SR26 Received is not displayed for document upload \n";
        }

        if (errorMessage != "")
            Assert.fail(errorMessage);
    }


    @Test(dependsOnMethods = {"testCreateSquireSubmission"})
    private void testCreateDocumentFromTemplateDocumentTypeValidation() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, environmentToOverride);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.squireFullApp.squire.getPolicyNumber());
        createNewDocumentFromTemplate("303 - Authorization for Show Car Driver Exclusion Endorsement", DocumentType.ShowCarExclusion303);
        createNewDocumentFromTemplate("304 - Authorization for Driver Exclusion Endorsement", DocumentType.DriverExclusion304);
        createNewDocumentFromTemplate("898 - Out of State Information Request Letter", DocumentType.OutOfState898);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        BCCommonDocuments docs = new BCCommonDocuments(driver);
        boolean Doc303Found = false;
        boolean Doc304Found = false;
        boolean Doc898Found = false;

        for (int currentRow = 1; currentRow <= docs.getDocumentTableRowCount(); currentRow++) {

            if (docs.getDocumentDescriptionByRow(currentRow).contains(DocumentType.ShowCarExclusion303.getValue())) {
                Doc303Found = true;
                continue;
            }
            if (docs.getDocumentDescriptionByRow(currentRow).contains(DocumentType.DriverExclusion304.getValue())) {
                Doc304Found = true;
                continue;
            }
            if (docs.getDocumentDescriptionByRow(currentRow).contains(DocumentType.OutOfState898.getValue())) {
                Doc898Found = true;
                continue;
            }
        }
        String errorMessage = "";
        if (!Doc303Found && !Doc304Found && !Doc898Found)
            errorMessage = "Expected documents from template are not shown \n ";

        if (errorMessage != "")
            Assert.fail(errorMessage);
    }

    private void createNewDocumentFromTemplate(String name, DocumentType documentType) {
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_NewDocument();
        actions.click_CreateNewDocument();
        NewDocumentPC doc = new NewDocumentPC(driver);
        doc.searchDocumentTemplateByName(this.squireFullApp.productType, name);
        doc.selectRelatedTo("Policy");
        doc.selectDocumentType(documentType);
        doc.selectReceiver(this.squireFullApp.pniContact.getFirstName());
        doc.clickUpdate();

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
