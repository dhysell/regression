package repository.pc.actions;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DocumentType;
import repository.gw.enums.ProductLineType;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;

import java.util.List;

public class NewDocumentPC extends BasePage {

    private WebDriver driver;

    public NewDocumentPC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private Guidewire8Select select_RelatedTo() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'ContactNewDocumentLinkedWorksheet:NewDocumentLinkedScreen:DocumentDetailsInputSet:RelatedTo-triggerWrap') or contains(@id, ':DocumentMetadataInputSet:RelatedTo-triggerWrap')]");
    }

    private Guidewire8Select select_DocumentType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'InputSet:Type-triggerWrap') or contains(@id, ':DocumentMetadataInputSet:Type-triggerWrap')]");
    }

    private Guidewire8Select select_ProductType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'ReviewCaseFormPatternSearchDV:Product-triggerWrap')]");
    }

    private Guidewire8Select select_Receiver() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':DocumentMetadataInputSet:Receiver-triggerWrap')]");
    }

    private Guidewire8Select select_Item() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':DocumentMetadataInputSet:additionalInteresDetails')]");
    }

    @FindBy(xpath = "//div[contains(@id, ':ReviewCaseFormPatternSearchPopupScreen:FormPatternSearchPopupResultsLV')]")
    private WebElement table_NewdocumentFormPatterns;

    @FindBy(xpath = "//select[@name='DocumentType']")
    public WebElement select_MultiDocTypeOfDocument;

    @FindBy(xpath = "//select[@name='RelatedTo']")
    private WebElement select_MultiDocRelatedTo;

    @FindBy(xpath = "//input[contains(@id, ':DocumentMetadataInputSet:Description-inputEl')]")
    private WebElement editbox_Description;

    @FindBy(xpath = "//input[contains(@id, ':DocumentMetadataInputSet:Comment-inputEl')]")
    private WebElement editbox_Comments;

    @FindBy(xpath = "//input[@value='Upload Files']")
    public WebElement button_MultiDocUploadFiles;

    @FindBy(xpath = "//input[@name='uploadfile']")
    public WebElement button_MultiDocChooseFiles;

    @FindBy(xpath = "//div[contains(@id, ':FormPatternPicker:SelectFormPatternPicker')]")
    private WebElement link_SelectFormPattern;

    // @FindBy(xpath = "//span[contains(@id, 'fileuploadfield') and
    // contains(@id, 'button-btnEl')]")
    @FindBy(xpath = "//span[contains(text(), 'Browse...')]/parent::span")
    private WebElement button_Browse;


    @SuppressWarnings("unused")
    private WebElement link_SelectFormPatternByName(String name) {
        return table_NewdocumentFormPatterns.findElement(By.xpath("//td[contains(., '" + name.trim() + "')]//preceding-sibling::td/div/a[contains(@id, ':_Select')]"));
    }


    public void clickUpdate() {
        super.clickUpdate();
    }


    public void clickCancel() {
        super.clickCancel();
    }


    public void setAttachment(String filePath, String fileName) {
        try {
            clickBrowse();
            sleep(2); //Used to ensure that the pop-up dialogue opens before continuing.
            GuidewireHelpers gh = new GuidewireHelpers(getDriver());
            gh.uploadOrSaveFile(gh.sanitizeFilePath(filePath + fileName));
            sleep(2); //Used to ensure that the pop-up dialogue closes fully before continuing.
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void clickBrowse() {
        try {
            // waitUntilElementIsVisible(button_Browse);
            new Actions(getDriver()).moveToElement(button_Browse).click().perform();
        } catch (Exception e) {
            Assert.fail("Failed to click Browse.. button");
        }
    }


    public void selectRelatedTo(String relatedTo) {
        Guidewire8Select mySelect = select_RelatedTo();
        mySelect.selectByVisibleTextPartial(relatedTo);
    }


    public void selectDocumentType(DocumentType docType) {
        Guidewire8Select mySelect = select_DocumentType();
        mySelect.selectByVisibleText(docType.getValue());
    }


    public void setDescription(String desc) {
        clickWhenClickable(editbox_Description);
        editbox_Description.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_Description.sendKeys(desc);
    }


    public void setComments(String comment) {
        clickWhenClickable(editbox_Comments);
        editbox_Comments.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_Comments.sendKeys(comment);
    }


    public void selectRandom_MultiDocumentType() {
        int randomNumber = NumberUtils.generateRandomNumberInt(1, new Select(select_MultiDocTypeOfDocument).getOptions().size() - 1);
        new Select(select_MultiDocTypeOfDocument).selectByIndex(randomNumber);
    }


    public void selectRandom_RelatedTo() {
        int randomNumber = NumberUtils.generateRandomNumberInt(1, new Select(select_MultiDocRelatedTo).getOptions().size() - 1);
        new Select(select_MultiDocRelatedTo).selectByIndex(randomNumber);
    }


    public void selectSpecific_MultiDocumentType(String name) {
        ((Select) select_MultiDocTypeOfDocument).selectByVisibleText(name);
    }


    public void selectSpecific_MultiDocumentRelatedTo(String name) {
        ((Select) select_MultiDocRelatedTo).selectByVisibleText(name);
    }


    public void setMultiDocumentAttachments(String filePath, List<String> files) {

        String bigAssStringWithFileNames = "";

        // concantenates the filepath and file name and encloses it in quotes
        // and adds a space between each file.
        for (String file : files) {
            bigAssStringWithFileNames += "\"" + filePath + file + "\" ";
        }

        try {
            clickChooseFiles();
            // upload.setFilePath(Configuration.getBrowser().getValue(),
            // filePath);
            sleep(2); //Used to ensure that the pop-up dialogue opens before continuing.
            GuidewireHelpers gh = new GuidewireHelpers(getDriver());
            gh.uploadOrSaveFile(bigAssStringWithFileNames);
            sleep(2); //Used to ensure that the pop-up dialogue closes fully before continuing.

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void clickChooseFiles() {
        clickWhenClickable(button_MultiDocChooseFiles);
    }


    public void clickUploadFiles() {
        clickWhenClickable(button_MultiDocUploadFiles);
    }


    public void searchDocumentTemplateByName(ProductLineType product, String docName) {
        
        clickWhenClickable(link_SelectFormPattern);
        
        super.clickReset();
        Guidewire8Select mySelect = select_ProductType();
        mySelect.selectByVisibleTextPartial(product.getValue());
        super.clickSearch();
        
        int row = new TableUtils(getDriver()).getRowNumberInTableByText(table_NewdocumentFormPatterns, docName);
        new TableUtils(getDriver()).clickLinkInSpecficRowInTable(table_NewdocumentFormPatterns, row);
        
    }


    public void selectReceiver(String name) {
        Guidewire8Select mySelect = select_Receiver();
        mySelect.selectByVisibleTextPartial(name);
    }


    public void selectAdditionalInterestItem(String name) {
        Guidewire8Select mySelect = select_Item();
        mySelect.selectByVisibleTextPartial(name);
    }


    public boolean checkAdditionalInterestsItemExists() {
        try {
            return checkIfElementExists(select_Item().getSelectButtonElement(), 1000);
        } catch (Exception e) {
            return false;
        }
    }

    public void uploadAccountLevelDocument(String documentPath, String documentName, String relatedTo, DocumentType documentType) {
        setAttachment(documentPath, "\\" + documentName);
        
        selectRelatedTo(relatedTo);
        
        selectDocumentType(documentType);
        
        clickUpdate();
    }
}
