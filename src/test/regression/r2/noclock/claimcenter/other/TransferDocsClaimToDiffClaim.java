package regression.r2.noclock.claimcenter.other;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.actionsmenu.ActionsMenu;
import repository.cc.claim.Documents;
import repository.cc.claim.NewDocument;
import repository.cc.claim.searchpages.AdvancedSearchCC;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
@QuarantineClass
public class TransferDocsClaimToDiffClaim extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers user = ClaimsUsers.abatts;
    private String password = "gw";

    private String claimNumber1 = null;
    private String claimNumber2 = null;
    private String documentName1 = "JPG Document.jpg";
    private String documentName2 = "PDF Document.pdf";

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "Random";

    @Test
    public void generateTwoFNOLS() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL autoFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Property);

        this.claimNumber1 = autoFNOLObj.claimNumber;


        GenerateFNOL propFNOLObj = new GenerateFNOL.Builder(driver)
                .withCreatorUserNamePassword(user.toString(), password)
                .withSpecificIncident(incidentName)
                .withLossDescription(lossDescription)
                .withLossCause(lossCause)
                .withLossRouter(lossRouter)
                .withAdress(address)
                .withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Auto);
        this.claimNumber2 = propFNOLObj.claimNumber;


    }

    @Test(dependsOnMethods = {"generateTwoFNOLS"})
    public void addADocumentToEachClaim() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(user.toString(), password);
        TopMenu topMenu = new TopMenu(driver);
        ActionsMenu actionsMenu = new ActionsMenu(driver);
        NewDocument newDoc = new NewDocument(driver);

        String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents\\";

        // Uploading the first document to first claim.
        topMenu.clickClaimTabArrow();

        topMenu.setClaimNumberSearch(claimNumber1);

        actionsMenu.clickActionsButton();

        actionsMenu.clickAttachExistingDocument();

        newDoc.setAttachment(documentDirectoryPath, documentName1);

        newDoc.selectRandom_DocumentType();

        newDoc.sendDocumentDescription("first document");

        newDoc.clickUpdateButton();

        // Upload Second Document to second claim

        topMenu.clickClaimTabArrowInClaim();


        topMenu.setClaimNumberSearch(claimNumber2);

        actionsMenu.clickActionsButton();

        actionsMenu.clickAttachExistingDocument();

        newDoc.setAttachment(documentDirectoryPath, documentName2);

        newDoc.selectRandom_DocumentType();

        newDoc.sendDocumentDescription("Second Document");

        newDoc.clickUpdateButton();

    }

    /**
     * @throws GuidewireException
     * @Author iclouser
     * @Requirement Make sure you can transfer documents between different claims.
     * @RequirementsLink <a href=https://rally1.rallydev.com/#/10552165958d/detail/defect/52230213983">Link to Defect</a>
     * @Description Test to transfer documents between claims.
     * @DATE Mar 7, 2016
     */
    @Test(dependsOnMethods = {"addADocumentToEachClaim"})
    public void transferDocuments() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(user.toString(), password);
        TopMenu topMenu = new TopMenu(driver);
        SideMenuCC sideMenu = new SideMenuCC(driver);
        Documents docs = new Documents(driver);
        AdvancedSearchCC advancedSearch = new AdvancedSearchCC(driver);

        // Uploading the first document to first claim.
        topMenu.clickClaimTabArrow();

        topMenu.setClaimNumberSearch(claimNumber1);

        sideMenu.clickDocuments();

        String tableXpath = "//div[contains(@id,'Claim_DocumentsScreen:DocumentsLV')]";

        // This splits the the name of the document and it's file extension. 
        // Example: testDoc.docx
        // splitDocumentString[0] would equal testDoc
        // splitDocumentString[1] would equal docx
        String[] splitDocument1String = documentName1.split("\\.");

        docs.setCheckboxInTableByText(driver.findElement(By.xpath(tableXpath)), splitDocument1String[0], true);

        docs.clickTransferDocuments();

        docs.clickSearchClaim();

        advancedSearch.inputClaimNumber(claimNumber2);

        docs.clickSearchClaim();
        advancedSearch.clickSelectButton();

        docs.clickTranserButton();

        sideMenu.clickDocuments();

        List<WebElement> documentFound = driver.findElements(By.xpath("//a[contains(text(),'" + splitDocument1String[0] + "')]"));

        Assert.assertTrue(documentFound.size() > 0, "The document wasn't found");

    }
}
