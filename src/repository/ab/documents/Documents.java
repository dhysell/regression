package repository.ab.documents;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DocumentRelatedTo;
import repository.gw.enums.DocumentType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.List;

public class Documents extends BasePage {


    private WebDriver driver;

    public Documents(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[contains(@id,'ContactDocuments:Claim_DocumentsScreen:DocumentsLV')]")
    private WebElement table_DocumentsTable;

    public Guidewire8Select select_DocumentsRelatedTo() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'ContactDocuments:Claim_DocumentsScreen:ContactDocumentSearchDV:RelatedTo-triggerWrap')]");
    }

    public Guidewire8Select select_DocumentsType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'ContactDocuments:Claim_DocumentsScreen:ContactDocumentSearchDV:Section-triggerWrap')]");
    }

    @FindBy(xpath = "//*[contains(@id,'ContactDocuments:Claim_DocumentsScreen:ContactDocumentSearchDV:NameOrID-inputEl')]")
    private WebElement input_DocumentsNameOrIdentifier;

    public Guidewire8Select select_DocumentsStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'ContactDocuments:Claim_DocumentsScreen:ContactDocumentSearchDV:Status-triggerWrap')]");
    }

    @FindBy(xpath = "//*[contains(@id,'ContactDocuments:Claim_DocumentsScreen:ContactDocumentSearchDV:Author-inputEl')]")
    private WebElement input_DocumentsAuthor;

    public Guidewire8RadioButton radio_DocumentsIncludeHidden() {
        return new Guidewire8RadioButton(getDriver(), "//div[contains(@id, 'ContactDocuments:Claim_DocumentsScreen:ContactDocumentSearchDV:IncludeObsoletes-containerEl')]/table");
    }

    @FindBy(xpath = "//*[contains(@id,'ContactDocuments:Claim_DocumentsScreen:ContactDocumentSearchDV:SearchAndResetInputSet:SearchLinksInputSet:Search')]")
    private WebElement button_DocumentsSearch;

    @FindBy(xpath = "//*[contains(@id,'ContactDocuments:Claim_DocumentsScreen:ContactDocumentSearchDV:SearchAndResetInputSet:SearchLinksInputSet:Reset')]")
    private WebElement button_DocumentsReset;


    public WebElement button_DocumentsByDateTime(Date uploadDate) {
        return find(By.xpath("//div[contains(., '" + DateUtils.dateFormatAsString("MM/dd/yyyy", uploadDate) + "')]/ancestor::tr/td/div/a[contains(., 'View')]"));
    }

    public List<WebElement> tr_DocumentsByNameTypeAuthorDate(String name, String Type, String author, String uploadDate) {
        return finds(By.xpath("//div[contains(., '" + uploadDate + "')]/ancestor::tr/td/div[contains(., '" + author + "')]/ancestor::tr/td/div[contains(., '" + Type + "')]/ancestor::tr/td/div/a[contains(., '" + name + "')]/ancestor::tr[1]"));
    }

    public WebElement button_ViewDocumentsByNameTypeAuthorDate(String name, String Type, String author, String uploadDate) {
        return find(By.xpath("//div[contains(., '" + uploadDate + "')]/ancestor::tr/td/div[contains(., '" + author + "')]/ancestor::tr/td/div[contains(., '" + Type + "')]/ancestor::tr/td/div/a[contains(., '" + name + "')]/ancestor::tr/td/div/a[contains(., 'View')]"));
    }


    public void selectRelatedTo(DocumentRelatedTo relatedTo) {
        Guidewire8Select relatedToSelect = select_DocumentsRelatedTo();
        relatedToSelect.selectByVisibleText(relatedTo.getValue());
    }

    public void selectType(DocumentType type) {
        Guidewire8Select relatedToSelect = select_DocumentsRelatedTo();
        relatedToSelect.selectByVisibleText(type.getValue());
    }

    public void setNameOrIdentifier(String name) {
        
        waitUntilElementIsVisible(input_DocumentsNameOrIdentifier);
        
        input_DocumentsNameOrIdentifier.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        input_DocumentsNameOrIdentifier.sendKeys(name);
        
        input_DocumentsNameOrIdentifier.sendKeys(Keys.TAB);
        
    }

    public boolean verifyDocumentByNameTypeAuthorDate(String name, DocumentType type, String author, Date uploadDate) {
        System.out.println("today is " + DateUtils.dateFormatAsString("MM/dd/yyyy", uploadDate));
//		$x("//div[contains(., '11/08/2016')]/ancestor::tr/td/div[contains(., 'Emma Sorensen')]/ancestor::tr/td/div[contains(., 'Other')]/ancestor::tr/td/div/a[contains(., 'txt_test')]/ancestor::tr[1]")
        if (tr_DocumentsByNameTypeAuthorDate(name, type.getValue(), author, DateUtils.dateFormatAsString("MM/dd/yyyy", uploadDate)).size() > 0) {
            return true;
        } else if (new TableUtils(getDriver()).incrementTablePageNumber(table_DocumentsTable)) {
            verifyDocumentByNameTypeAuthorDate(name, type, author, uploadDate);
        }
        return false;
    }

    public String clickViewDocument(String name, DocumentType type, String author, Date uploadDate, int attempt) {

        if (tr_DocumentsByNameTypeAuthorDate(name, type.getValue(), author, DateUtils.dateFormatAsString("MM/dd/yyyy", uploadDate)).size() > 0) {
            List<WebElement> views = tr_DocumentsByNameTypeAuthorDate(name, type.getValue(), author, DateUtils.dateFormatAsString("MM/dd/yyyy", uploadDate)).get(0).findElements(By.xpath(".//td/div/a[contains(@onclick, 'The selected document could not be viewed, because it has not yet been committed to the database. Please try again once the document has been saved.')]"));
            if (!views.isEmpty()) {
                Assert.fail("When Clicking view, the Document hasn't round tripped yet. Check to make sure the document makes the round trip.");
            }
        }
        button_ViewDocumentsByNameTypeAuthorDate(name, type.getValue(), author, DateUtils.dateFormatAsString("MM/dd/yyyy", uploadDate)).click();
        return getNewWindow();
    }

    public String getNewWindow() {

        WebDriver driver = getDriver();
        String mainPCWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
            if (!driver.getCurrentUrl().contains("ab8uat/ab")) {
                systemOut("Switching to Documents Window");
                break;
            }
        }
        return mainPCWindow;
    }

    public boolean isErrorTextPresent() {
        List<WebElement> failureText = finds(By.xpath("//*[contains(., 'There was a problem fetching the document from ImageRight. Please contact the Help Desk at extension 4350.')]"));
        return !failureText.isEmpty();
    }

    public void closeDocumentsWebpage(String mainWindowHandle) {
        getDriver().close();
        getDriver().switchTo().window(mainWindowHandle);
    }
}
