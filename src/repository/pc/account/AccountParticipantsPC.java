/*
 * Jon Larsen 4/7/2015
 * class lists elements on the Account Participants page.
 * the Roles combobox is a custom combo box because it is not the same structure as the GW8Selects.
 */

package repository.pc.account;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

import java.util.List;

public class AccountParticipantsPC extends BasePage {

    private WebDriver driver;

    public AccountParticipantsPC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//span[contains(@id, 'AccountFile_RolesScreen:Edit-btnEl')]")
    public WebElement button_Edit;

    @FindBy(xpath = "//span[contains(@id, 'AccountFile_RolesScreen:Cancel-btnEl')]")
    public WebElement button_Cancel;

    @FindBy(xpath = "//span[contains(@id, 'AccountFile_RolesScreen:Add-btnEl')]")
    public WebElement button_Add;

    @FindBy(xpath = "//input[@name='AssignedUser']")
    public WebElement editbox_AssignedUser;

    @FindBy(xpath = "//span[text() = 'Assigned User']/following-sibling::div")
    public WebElement editbox_AssignedUserArrow;

    Guidewire8Select select_AssignedGroup() {
        return new Guidewire8Select(driver, "(//input[@name='AssignedGroup']/ancestor::table)[1]");
    }

    @FindBy(xpath = "//a[contains(@id, 'AssignedUser:UserBrowseMenuItem')]")
    public WebElement button_SelectUser;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    public void clickEdit() {
        button_Edit.click();
    }


    public void clickUpdate() {
        super.clickUpdate();
    }


    public void clickCancel() {
        button_Cancel.click();
    }


    public void clickAdd() {
        button_Add.click();
    }


    public void clickRemove() {
        super.clickRemove();
    }


    public void clickSelectUser() {
        button_SelectUser.click();
    }


    public void setAssignedUser(String user) {
        find(By
                .xpath("//div[@id='AccountFile_Roles:AccountFile_RolesScreen:AccountRolesLV-body']/descendant::tr[last()]/child::td[3]/div"))
                .click();
        editbox_AssignedUser.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_AssignedUser.sendKeys(user);
        editbox_AssignedUser.sendKeys(Keys.chord(Keys.SHIFT, Keys.TAB));
    }

    // This Select role appears and acts like a Guidewire 8 Select Combobox but
    // it is different.
    // The steps on getting and setting values are similar but xpaths are
    // different.
    // First need to click on the div where the combobox would be. most likely
    // have to start another static element
    // then get bound list that is visible and do string compares.

    public void selectRole(String role) {
        find(By
                .xpath("//div[@id='AccountFile_Roles:AccountFile_RolesScreen:AccountRolesLV-body']/descendant::tr[last()]/child::td[2]/div"))
                .click();
        List<WebElement> roleList = finds(
                By.xpath("//div[contains(@id, 'boundlist-') and not(contains(@style, 'display:none'))]/div/ul/li"));
        for (int i = 0; i < roleList.size(); i++) {
            System.out.println(roleList.get(i).getText());
            if (roleList.get(i).getText().equals(role)) {
                System.out.println(roleList.get(i).toString());
                roleList.get(i).click();
                // break; //break Jon
            }
        }
    }


    public void selectAssignedGroup(String group) {
        find(By
                .xpath("//div[@id='AccountFile_Roles:AccountFile_RolesScreen:AccountRolesLV-body']/descendant::tr[last()]/child::td[4]/div"))
                .click();
        List<WebElement> roleList = finds(
                By.xpath("//div[contains(@id, 'boundlist-') and not(contains(@style, 'display:none'))]/div/ul/li"));
        for (int i = 0; i < roleList.size(); i++) {
            if (roleList.get(i).getText().equals(group)) {
                roleList.get(i).click();
            }
        }
    }


    public void setParticipantCheckbox(String assignedUser, String role) {
        WebElement checkbox = find(By
                .xpath("//div[@id='AccountFile_Roles:AccountFile_RolesScreen:AccountRolesLV-body']/descendant::tbody/child::tr/child::td/div/child::div[contains(text(), '"
                        + assignedUser + "')]/ancestor::td/preceding-sibling::td/div[contains(text(), '" + role
                        + "')]/ancestor::tr[1]/td/div/img"));
        if (!checkbox.getAttribute("class").contains("-checked")) {
            checkbox.click();
        }
    }


    public void clearParticipantCheckbox(String assignedUser, String role) {
        WebElement checkbox = find(By
                .xpath("//div[@id='AccountFile_Roles:AccountFile_RolesScreen:AccountRolesLV-body']/descendant::tbody/child::tr/child::td/div/child::div[contains(text(), '"
                        + assignedUser + "')]/ancestor::td/preceding-sibling::td/div[contains(text(), '" + role
                        + "')]/ancestor::tr[1]/td/div/img"));
        if (checkbox.getAttribute("class").contains("-checked")) {
            checkbox.click();
        }
    }

}
