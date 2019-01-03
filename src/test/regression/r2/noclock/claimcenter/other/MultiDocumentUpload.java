package regression.r2.noclock.claimcenter.other;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.actionsmenu.ActionsMenu;
import repository.cc.claim.NewDocument;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
@QuarantineClass
public class MultiDocumentUpload extends BaseTest {
	private WebDriver driver;
    private String userName = ClaimsUsers.abatts.toString();
    private String password = "gw";
    private String claimNumber = "";
    private String doc1 = "doc_test.doc";
    private String doc2 = "JPG Document.jpg";
    private ArrayList<String> documentNames = new ArrayList<String>();

    // FNOL Specific Strings
    private String incidentName = "Random";
    private String lossDescription = "Loss Description Test";
    private String lossCause = "Random";
    private String lossRouter = "Random";
    private String address = "Random";
    private String policyNumber = "Random";

    @Test
    public void newFNOL() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(driver).withCreatorUserNamePassword(userName, password)
                .withSpecificIncident(incidentName).withLossDescription(lossDescription).withLossCause(lossCause)
                .withLossRouter(lossRouter).withAdress(address).withPolicyNumber(policyNumber)
                .build(GenerateFNOLType.Property);
        this.claimNumber = myFNOLObj.claimNumber;

    }

    /**
     * @throws GuidewireException
     * @Author iclouser
     * @Requirement Test Multi-Document Upload
     * @RequirementsLink <a href="https://rally1.rallydev.com/#/10552165958d/detail/userstory/44251373045">Rally Story</a>
     * @Description Test Adds two documents and uploads them. Then validates that show up in the documents area.
     * @DATE Mar 2, 2016
     */
    @Test(dependsOnMethods = "newFNOL")
    public void multiDocumentUpload() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(userName, password);

        TopMenu topMenu = new TopMenu(driver);
        ActionsMenu actionsMenu = new ActionsMenu(driver);
        NewDocument newDoc = new NewDocument(driver);

        // FilePath Names and Document Names get added to the list.
        String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents\\";
        documentNames.add(doc1);
        documentNames.add(doc2);

        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);

        actionsMenu.clickActionsButton();

        actionsMenu.clickAttachMulitipleDocuments();


        // The elements below are in an iframe so we must switch to the iframe to interact with them.

        driver.switchTo().frame(driver.findElement(By.xpath("//div[contains(@id,'NewMultiDocumentLinkedScreen:0-body')]//iframe")));

        newDoc.selectRandom_MultiDocumentType();

        newDoc.setMultiDocumentAttachments(documentDirectoryPath, documentNames);

        newDoc.clickUploadFiles();
        // Switch out of the iframe to main window.
        driver.switchTo().defaultContent();
        newDoc.clickClose();
    }

    @Test(dependsOnMethods = "newFNOL")
    public void validateMultiDocumentsUploaded() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(userName, password);

        TopMenu topMenu = new TopMenu(driver);
        NewDocument newDoc = new NewDocument(driver);
        SideMenuCC sideMenu = new SideMenuCC(driver);

        topMenu.clickClaimTabArrow();

        topMenu.setClaimNumberSearch(claimNumber);

        sideMenu.clickDocuments();


        String tableXpath = "//div[@id='ClaimDocuments:Claim_DocumentsScreen:DocumentsLV']";

        for (String doc : documentNames) {
            // removing the file extension to just get the document name. 
            String[] tmpStringArray = doc.split("\\.");

            // Click the name will error if it isn't there. If it does fail make sure irpostcommit message queue is running. tmpStringArray[0] gets name of the document
            // and tmpStringArray[1] would get the extension type.
            new TableUtils(driver).clickLinkInTable(driver.findElement(By.xpath(tableXpath)), tmpStringArray[0]);

            String pageName = driver.findElement(By.xpath("//span[contains(@id, 'DocumentDetailsScreen:ttlBar')]")).getText();

            Assert.assertTrue(pageName.equalsIgnoreCase("Document Details"), "The link you clicked didn't seem to go where expected");

            newDoc.clickReturnToDocuments();

        }

    }

}
