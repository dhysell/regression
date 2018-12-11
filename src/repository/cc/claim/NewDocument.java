package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.exception.GuidewireException;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;

import java.util.List;

public class NewDocument extends BasePage {

    private WebDriver driver;

    public NewDocument(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    ///////////////////////////////// Guidewire Elements ///////////////////////////////// /////////////////////////////////////////////////

    @FindBy(xpath = "//a[contains(@id,'fileuploadfield')]")
    public WebElement button_Browse;

    @FindBy(xpath = "//input[contains(@id,'NewDocumentName-inputEl')]")
    public WebElement input_DocumentName;

    @FindBy(xpath = "//input[contains(@id,'Description-inputEl')]")
    public WebElement input_DocumentDescription;

    public Guidewire8Select select_DocumentType() {
        return new Guidewire8Select(driver,"//table[contains(@id,'DocumentDetailsInputSet:Type-triggerWrap')]");
    }

    @FindBy(xpath = "//a[contains(@id,'NewDocumentLinkedScreen:Update') or contains(@id,'NewDocumentFromTemplateScreen:Update')]")
    public WebElement button_Update;

    @FindBy(xpath = "//a[contains(@id,'NewDocumentLinkedScreen:Cancel-btnWrap')]")
    public WebElement button_Cancel;

    // ** Multiple Document Elements **//

    @FindBy(xpath = "//input[@id='Description']")
    public WebElement input_MultiDocDescription;

    @FindBy(xpath = "//select[@name='DocumentType']")
    public WebElement select_MultiDocTypeOfDocument;

    @FindBy(xpath = "//select[@name='RelatedTo']")
    public WebElement select_MultiDocRelatedTo;

    @FindBy(xpath = "//input[@name='uploadfile']")
    public WebElement button_MultiDocChooseFiles;

    @FindBy(xpath = "//input[@value='More Files']")
    public WebElement button_MultiDocMoreFiles;

    @FindBy(xpath = "//input[@value='Upload Files']")
    public WebElement button_MultiDocUploadFiles;

    @FindBy(xpath = "//a[contains(@id, 'NewMultiDocumentLinkedScreen:Cancel')]")
    public WebElement button_Close;

    @FindBy(xpath = "//a[contains(text(), 'Return to Documents')]")
    public WebElement link_ReturnToDocs;

    @FindBy(xpath = "//div[@id='ClaimNewDocumentFromTemplateWorksheet:NewDocumentFromTemplateScreen"
            + ":NewTemplateDocumentDV:TemplatePicker:SelectTemplatePicker']")
    public WebElement link_SpyGlass;

    @FindBy(xpath = "//input[@id='DocumentTemplateSearchPopup:DocumentTemplateSearchScreen:DocumentTemplateSearchDV:Keywords-inputEl']")
    public WebElement input_Keywords;

    @FindBy(xpath = "//a[contains(@id,':SearchAndResetInputSet:SearchLinksInputSet:Search')]")
    public WebElement button_Search;

    @FindBy(xpath = "//a[contains(@id,':NewTemplateDocumentDV:CreateDocument')]")
    public WebElement button_CreateDocument;

    public void clickCreateDocument() {
        clickWhenClickable(button_CreateDocument);
    }

    public void clickSearchButton() {
        clickWhenClickable(button_Search);
    }

    public void setKeywords(String keywords) {
        input_Keywords.sendKeys(keywords);
    }

    public void clickSpyGlass() {
        clickWhenClickable(link_SpyGlass);
    }

    private Guidewire8Select selectTo() {
        return new Guidewire8Select(driver,"//table[contains(@id,'NewTemplateDocumentInputSet:DynamicInput-triggerWrap')]");
    }

    private void setSelectTo(String selectString) {
        selectTo().selectByVisibleText(selectString);
    }

    private void setSelectTo() {
        selectTo().selectByVisibleTextRandom();
    }

    public void assignDocumentTo() {
        
        setSelectTo();
    }

    public void assignDocumentTo(String selectString) {
        
        setSelectTo(selectString);
    }

    public void createDocumentFromTemplate(String keyword, String formName, String description, String path) {
        
        clickSpyGlass();
        
        setKeywords(keyword);
        clickSearchButton();

        

        int count = 0;

        List<WebElement> selectbuttons = finds(By.xpath("//div[contains(@id,':DocumentTemplateSearchResultLV-body')]/div/table/tbody/tr/td[1]/div/a"));
        List<WebElement> templates = finds(By.xpath("//div[contains(@id,':DocumentTemplateSearchResultLV-body')]/div/table/tbody/tr/td[2]"));

        for (WebElement template : templates) {
            
            if (template.getText().equalsIgnoreCase(formName)) {
                System.out.println(template.getText());
                
                clickWhenClickable(selectbuttons.get(count));
                break;
            }
            count++;
        }
    }

    public void clickButtonBrowse() {
        clickWhenClickable(button_Browse);
    }

    public void sendDocumentName(String textName) {
        input_DocumentName.sendKeys(textName);
    }

    public void sendDocumentDescription(String textName) {
        input_DocumentDescription.sendKeys(textName);
    }

    public void selectRandom_DocumentType() {
        Guidewire8Select mySelect = select_DocumentType();
        mySelect.selectByVisibleTextRandom();
    }

    public void selectSpecific_DocumentType(String item) {
        Guidewire8Select mySelect = select_DocumentType();
        mySelect.selectByVisibleTextPartial(item);
    }

    public void clickUpdateButton() {
        clickWhenClickable(button_Update);
    }

    public void clickUpdateCancel() {
        clickWhenClickable(button_Cancel);
    }

    public void setAttachment(String filePath, String fileName) {
    	try {
            
            clickBrowse();
            
            GuidewireHelpers gh = new GuidewireHelpers(getDriver());
            gh.uploadOrSaveFile(gh.sanitizeFilePath(filePath + fileName));
            
        } catch (Exception e) {
            System.out.println(e);
        }
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
            
            GuidewireHelpers gh = new GuidewireHelpers(getDriver());
            gh.uploadOrSaveFile(bigAssStringWithFileNames);
            

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void clickBrowse() throws GuidewireException {
        
        try {
//             waitUntilElementIsVisible(button_Browse);
//            new Actions(Configuration.getWebDriver()).moveToElement(button_Browse).click().perform();

            waitUntilElementIsClickable(button_Browse).click();
        } catch (Exception e) {
            throw new GuidewireException(driver.getCurrentUrl(), "Failed to click Browse.. button");
        }
    }

    // ** Multiple Document Helper functions **//

    public void sendMultiDocDescription(String description) {
        input_MultiDocDescription.sendKeys(description);
    }

    public void selectRandom_MultiDocumentType() {
        int randomNumber = NumberUtils.generateRandomNumberInt(1, new Select(select_MultiDocTypeOfDocument).getOptions().size() - 1);
        new Select(select_MultiDocTypeOfDocument).selectByIndex(randomNumber);
    }

    public void selectSpecific_MultiDocumentType(String name) {
        ((Select) select_MultiDocTypeOfDocument).selectByVisibleText(name);
    }

    public void selectRandom_MultiDocumentRelatedTo() {
        int randomNumber = NumberUtils.generateRandomNumberInt(0, new Select(select_MultiDocRelatedTo).getOptions().size() - 1);
        new Select(select_MultiDocRelatedTo).selectByIndex(randomNumber);
    }

    public void clickChooseFiles() {
        clickWhenClickable(button_MultiDocChooseFiles);
    }

    public void clickMoreFiles() {
        clickWhenClickable(button_MultiDocMoreFiles);
    }

    public void clickUploadFiles() {
        clickWhenClickable(button_MultiDocUploadFiles);
    }

    public void clickClose() {
        clickWhenClickable(button_Close);
    }

    public void clickReturnToDocuments() {
        clickWhenClickable(link_ReturnToDocs);

    }


}
