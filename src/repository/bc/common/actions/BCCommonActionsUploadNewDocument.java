package repository.bc.common.actions;

import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DocumentFileType;
import repository.gw.enums.DocumentStatus;
import repository.gw.enums.DocumentType;
import repository.gw.enums.SecurityLevel;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;

import java.util.List;

public class BCCommonActionsUploadNewDocument extends BasePage {

    private WebDriver driver;

    public BCCommonActionsUploadNewDocument(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    ////////////
    //Elements//
    ////////////

    @FindBy(xpath = "//input[contains(@id, ':DocumentMetadataInputSet:DocumentName-inputEl')]")
    public WebElement editbox_DocumentName;

    @FindBy(xpath = "//input[contains(@id, ':DocumentMetadataInputSet:Description-inputEl')]")
    public WebElement editbox_Description;

    @FindBy(xpath = "//input[contains(@id, ':DocumentMetadataInputSet:Author-inputEl')]")
    public WebElement editbox_Author;

    @FindBy(xpath = "//input[contains(@id, ':DocumentMetadataInputSet:Recipient-inputEl')]")
    public WebElement editbox_Recipient;

    private Guidewire8Select select_DocumentFileType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':DocumentMetadataInputSet:MimeType-triggerWrap')]");
    }

    private Guidewire8Select select_RelatedTo() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':DocumentMetadataInputSet:RelatedTo-triggerWrap')]");
    }

    private Guidewire8Select select_Status() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':DocumentMetadataInputSet:Status-triggerWrap')]");
    }

    private Guidewire8Select select_SecurityType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':DocumentMetadataInputSet:SecurityType-triggerWrap')]");
    }

    private Guidewire8Select select_DocumentType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':DocumentMetadataInputSet:Type-triggerWrap')]");
    }

    @FindBy(xpath = "//span[contains(text(), 'Browse...')]/parent::span")
    public WebElement button_Browse;

    ///////////
    //Methods//
    ///////////

    public void setAttachment(String filePath, String fileName) {
        try {
            
            clickBrowse();
            
            GuidewireHelpers gh = new GuidewireHelpers(getDriver());
            gh.uploadOrSaveFile(gh.sanitizeFilePath(filePath + fileName));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void clickBrowse() {
        
        try {
            new Actions(getDriver()).moveToElement(button_Browse).click().perform();
        } catch (Exception e) {
            Assert.fail(getCurrentUrl() + "Failed to click Browse.. button");
        }
    }

    public void setDescription(String desc) {
        clickWhenClickable(editbox_Description);
        editbox_Description.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_Description.sendKeys(desc);
    }

    public void selectDocumentFileType(DocumentFileType docFileType) {
        select_DocumentFileType().selectByVisibleText(docFileType.getValue());
        
    }

    // get currently selected file type

    public String getCurrentDocumentFileType() {
        return select_DocumentFileType().getText();
    }

    public void selectRelatedTo(String relatedTo) {
        select_RelatedTo().selectByVisibleTextPartial(relatedTo);
    }

    public String getDocumentName() {
        return waitUntilElementIsVisible(editbox_DocumentName).getAttribute("value");
        // return editbox_DocumentName.getText();
    }

    public void selectStatus(DocumentStatus docStatus) {
        select_Status().selectByVisibleText(docStatus.getValue());
        
    }

    public void selectSecurityType(SecurityLevel docSecurityLevel) {
        select_SecurityType().selectByVisibleText(docSecurityLevel.getValue());
        
    }

    public void selectDocumentType(DocumentType docType) {
        select_DocumentType().selectByVisibleText(docType.getValue());
        
    }

    public void randomSelectDocFileType() {
        List<String> theList = select_DocumentFileType().getList();
        int listSize = theList.size();
        int currentRan = NumberUtils.generateRandomNumberInt(2, listSize - 1);
        select_DocumentFileType().selectByVisibleTextPartial(theList.get(currentRan - 1));

    }

    public void randomSelectDocumentType() {
        List<String> theList = select_DocumentType().getList();
        int listSize = theList.size();
        int currentRan = NumberUtils.generateRandomNumberInt(2, listSize - 1);
        select_DocumentType().selectByVisibleTextPartial(theList.get(currentRan - 1));

    }

    public void uploadAccountLevelDocument(String documentPath, String documentName, DocumentStatus documentStatus, SecurityLevel securityLevel, DocumentType documentType, String relatedTo) {
        if (getDocumentName().equals("")) {
        	if (((RemoteWebDriver)getDriver()).getCapabilities().getPlatform().is(Platform.WINDOWS)) {
        		setAttachment(documentPath, "\\" + documentName);
        	} else {
        		setAttachment(documentPath, "/" + documentName);
        	}
            
        }
        if (getCurrentDocumentFileType().equals("<none>")) {
            randomSelectDocFileType();
            
        }
        if (documentStatus != null) {
            selectStatus(documentStatus);
            
        }
        if (securityLevel != null) {
            selectSecurityType(securityLevel);
            
        }
        if (documentType != null) {
            selectDocumentType(documentType);
            
        }
        if (relatedTo != null) {
            selectRelatedTo(relatedTo);
            
        }
        clickUpdate();
        
    }

    public void uploadPolicyLevelDocument(String documentPath, String documentName, DocumentStatus status, SecurityLevel level,
                                          DocumentType type) {
        if (getDocumentName().equals("")) {
            setAttachment(documentPath, "\\" + documentName);
            
        }

        if (getCurrentDocumentFileType().equals("<none>")) {
            randomSelectDocFileType();
            
        }
        selectStatus(status);
        
        selectSecurityType(level);
        
        selectDocumentType(type);
        
        clickUpdate();
    }
}
