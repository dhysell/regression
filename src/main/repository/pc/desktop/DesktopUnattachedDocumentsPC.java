package repository.pc.desktop;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import repository.driverConfiguration.BasePage;

public class DesktopUnattachedDocumentsPC extends BasePage {

    public DesktopUnattachedDocumentsPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(linkText = "Scanned Documents")
    public WebElement link_DesktopUnattachedDocumentsLink;

    @FindBy(linkText = "Return to Index Scanned Documents Work List")
    public WebElement link_ReturnToUnattachedDocsLink;

    @FindBy(xpath = "//select[contains(@id,'UnattachedDocumentWorkListPopup:UnattachedDocumentWorkItemsLV_tb:StatusFilter')]")
    public WebElement select_DesktopUnattachedDocumentStatus;


    public void clickDesktopUnattachedDocuments() {
        waitUntilElementIsVisible(link_DesktopUnattachedDocumentsLink);
        waitUntilElementIsClickable(link_DesktopUnattachedDocumentsLink);
        link_DesktopUnattachedDocumentsLink.click();
    }


    public void clickReturnToDesktopUnattachedDocuments() {
        waitUntilElementIsClickable(link_ReturnToUnattachedDocsLink);
        link_ReturnToUnattachedDocsLink.click();
    }


    public void clickDesktopUnattachedDocumentsStatus(String statusToSelect) {
        waitUntilElementIsClickable(select_DesktopUnattachedDocumentStatus);
        new Select(select_DesktopUnattachedDocumentStatus).selectByVisibleText(statusToSelect);
    }

}