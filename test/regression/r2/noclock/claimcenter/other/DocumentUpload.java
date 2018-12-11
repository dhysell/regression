package regression.r2.noclock.claimcenter.other;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.actionsmenu.ActionsMenu;
import repository.cc.claim.NewDocument;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
public class DocumentUpload extends BaseTest {
	private WebDriver driver;
    private String userName = ClaimsUsers.abatts.toString();
    private String password = "gw";
    private String claimNumber = "";
    private String documentName = "just_a_doc";

    @Test
    public void uploadDocument() throws Exception {

        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        TopMenu topMenu = new TopMenu(driver);
        ActionsMenu actionsMenu = new ActionsMenu(driver);
        NewDocument newDoc = new NewDocument(driver);

        new Login(driver).login(userName, password);

        String documentDirectoryPath = "\\\\fbmsqa11\\test_data\\test-documents\\";
        topMenu.clickClaimTabArrow();

        topMenu.clickRandomClaim();

        claimNumber = topMenu.gatherClaimNumber();

        actionsMenu.clickActionsButton();

        actionsMenu.clickAttachExistingDocument();

        newDoc.setAttachment(documentDirectoryPath, documentName);
        newDoc.selectRandom_DocumentType();
        newDoc.sendDocumentDescription("Test of single uploaded document");
        newDoc.clickUpdateButton();
    }

    @Test(dependsOnMethods = {"uploadDocument"})
    public void verifyDocumentUploaded() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        TopMenu topMenu = new TopMenu(driver);
        SideMenuCC sideMenu = new SideMenuCC(driver);

        new Login(driver).login(userName, password);
        topMenu.clickClaimTabArrow();

        topMenu.setClaimNumberSearch(claimNumber);

        sideMenu.clickDocuments();


        String tableXpath = "//div[@id='ClaimDocuments:Claim_DocumentsScreen:DocumentsLV']";
        new TableUtils(driver).clickLinkInTable(driver.findElement(By.xpath(tableXpath)), documentName);


        String pageName = driver.findElement(By.xpath("//span[contains(@id, 'DocumentDetailsScreen:ttlBar')]")).getText();

        Assert.assertTrue(pageName.equalsIgnoreCase("Document Details"), "The link you clicked didn't seem to go where expected");

    }

}
