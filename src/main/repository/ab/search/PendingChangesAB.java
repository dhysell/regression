package repository.ab.search;


import com.idfbins.enums.OkCancel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;

public class PendingChangesAB extends BasePage {

    private WebDriver driver;

    public PendingChangesAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // *******************************************************************************
    // Repository Items
    // *******************************************************************************

    @FindBy(xpath = "//span[contains(@id, ':PendingChangesScreen:CreatesCardTab-btnEl')]")
    public WebElement text_PendingChanges;

    //Creates

    @FindBy(xpath = "//span[contains(@id, ':PendingChangesScreen:PendingCreatesListDetailPanel:PendingContactCreateDetailedLV_tb:PendingContactCreate_ApproveButton-btnEl')]")
    public WebElement button_PendingChangesApprove;

    @FindBy(xpath = "//span[contains(@id, ':PendingCreatesListDetailPanel:PendingContactCreateDetailedLV_tb:PendingContactCreate_RejectButton-btnEl')]")
    public WebElement button_PendingChangesReject;

    @FindBy(xpath = "//span[contains(@id, ':PendingChangesScreen:PendingCreatesListDetailPanel:PendingContactCreateDetailedLV_tb:ApproveAndEditButton-btnEl')]")
    public WebElement button_PendingChangesApproveThenEdit;

    @FindBy(xpath = "//span[contains(@id, 'PendingChanges:PendingChangesScreen:PendingCreatesListDetailPanel:PendingContactCreateDetailedLV_tb:PendingContactCreate_FindDuplicatesButton-btnEl')]")
    public WebElement button_PendingChangesCheckForDuplicates;

    @FindBy(xpath = "//div[contains(@id, ':PendingCreatesListDetailPanel:PendingContactCreateDetailedLV') and not(contains(@id,'-body'))]")
    public WebElement table_PendingChanges;

    public WebElement pendingChangesContact(String contact, String user) {
        return table_PendingChanges.findElement(By.xpath(".//div[contains(., '" + contact + "')]/parent::td/following-sibling::td/div[contains(., '" + user + "')]/parent::td/parent::tr"));
    }

    public Guidewire8Select select_RejectChange() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'RejectChangePopup:RejectScreen:RejectReasonDV:RejectReason-triggerWrap')]");
    }

    @FindBy(xpath = "//span[contains(@id, 'RejectChangePopup:RejectScreen:ToolbarButton-btnEl')]")
    public WebElement button_RejectChange;

    // *******************************************************************************
    // Methods
    // *******************************************************************************

    public PendingChangesAB getToPendingChanges(String changeType) {
        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();
        SidebarAB sidebar = new SidebarAB(driver);
        return sidebar.clickSidebarPendingChanges(changeType);
    }

    public void clickCreates() {
        
        clickWhenClickable(text_PendingChanges);
    }

    public String clickApprove(OkCancel ok) {
        
        clickWhenClickable(button_PendingChangesApprove);
        String popUpContents = getPopupTextContents();
        selectOKOrCancelFromPopup(ok);
        return popUpContents;
    }

    public void clickReject(String reason) {
        
        clickWhenClickable(button_PendingChangesReject);
        
        select_RejectChange().selectByVisibleTextPartial("More Information Required");
        clickWhenClickable(button_RejectChange);
    }

    public String clickApproveThenEdit(OkCancel ok) {
        
        clickWhenClickable(button_PendingChangesApproveThenEdit);
        String popUpContents = getPopupTextContents();
        selectOKOrCancelFromPopup(ok);
        return popUpContents;
    }

    public void clickCheckForDuplicates() {
        
        clickWhenClickable(button_PendingChangesCheckForDuplicates);
    }

    public boolean clickContact(String contact, String user) {
        String[] contactNameArray = contact.split(" ");
        
        TableUtils tableHelpers = new TableUtils(driver);
        if (tableHelpers.hasMultiplePages(table_PendingChanges)) {
            int numberOfPages = tableHelpers.getNumberOfTablePages(table_PendingChanges);
            for (int i = 1; i <= numberOfPages; i++) {
                ArrayList<String> claimContacts = tableHelpers.getAllCellTextFromSpecificColumn(table_PendingChanges, "Contact");
                for (int lcv = 0; lcv < claimContacts.size(); lcv++) {
                    if (claimContacts.get(lcv).contains(contactNameArray[contactNameArray.length - 1])) {
                        clickWhenClickable(pendingChangesContact(claimContacts.get(lcv), user));
                        return true;
                    }
                }
                tableHelpers.clickNextPageButton(table_PendingChanges);
            }
            return false;

        } else {
            if (checkIfElementExists(pendingChangesContact(contact, user), 1000)) {
                clickWhenClickable(pendingChangesContact(contact, user));
                return true;
            } else {
                return false;
            }
        }
    }

}
