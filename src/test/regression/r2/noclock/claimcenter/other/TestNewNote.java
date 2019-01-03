package regression.r2.noclock.claimcenter.other;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.cc.actionsmenu.ActionsMenu;
import repository.cc.claim.NewNote;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;

public class TestNewNote extends BaseTest {
	private WebDriver driver;
    private String userName;
    private String passWord;

    @BeforeClass
    public void beforeClass() {

        userName = "lhopster";
        passWord = "gw";
    }

    // Logs in as the user that was specified above
    @BeforeMethod
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(userName, passWord);
    }

    @Test
    public void testNewNote() {
        TopMenu topMenu = new TopMenu(driver);
        ActionsMenu actionsMenu = new ActionsMenu(driver);
        NewNote newNote = new NewNote(driver);
        SideMenuCC sideMenu = new SideMenuCC(driver);

        String inputNoteSubject = "Test Note - User Created Note";
        String inputNoteText = "This is just a test to make sure the note functionality is working as intended.";

        topMenu.clickClaimTab();

        actionsMenu.clickActionsButton();

        actionsMenu.clickNewNote();

        newNote.selectRandomTopic();

        newNote.sendSubject(inputNoteSubject);

        newNote.sendTextboxText(inputNoteText);

        newNote.clickUpdateButton();

        sideMenu.clickNotesLink();


        WebElement backendNoteSubject = driver.findElement(By.xpath("//div[contains(@id, 'ClaimNotes:NotesSearchScreen:ClaimNotesLV:0:Subject')]"));
        WebElement backendNoteText = driver.findElement(By.xpath("//div[contains(@id, 'ClaimNotes:NotesSearchScreen:ClaimNotesLV:0:Body')]"));

        Assert.assertTrue(inputNoteSubject.equals(backendNoteSubject.getText()), "Could not find the expected note subject: " + inputNoteSubject + " found: " + backendNoteSubject + " instead.");
        Assert.assertTrue(backendNoteText.getText().contains(inputNoteText), "Portal Text doesn't match backend text");

    }

}
