package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.TableUtils;

import java.util.List;

public class Documents extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;

    public Documents(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(getDriver());
        PageFactory.initElements(driver, this);
    }

    /// Guidewire Elements
    public Guidewire8Select select_RelatedTo() {
        return new Guidewire8Select(driver, "//table[contains(@id='RelatedTo-triggerWrap')]");
    }

    public Guidewire8Select select_DocumentType() {
        return new Guidewire8Select(driver, "//table[contains(@id='Section-triggerWrap')]");
    }

    public Guidewire8Select select_Status() {
        return new Guidewire8Select(driver, "//table[contains(@id='Status-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id='NameOrID-inputEl')]")
    public WebElement input_NameOrIdentifier;

    @FindBy(xpath = "//input[contains(@id, 'Author-inputEl')]")
    public WebElement input_Author;

    @FindBy(xpath = "//a[contains(@id,'SearchLinksInputSet:Search')]")
    public WebElement button_Search;

    @FindBy(xpath = "//a[contains(@id,'SearchLinksInputSet:Reset')]")
    public WebElement button_Reset;

    @FindBy(xpath = "//a[contains(@id,'ClaimDocuments_ObsolesceButton')]")
    public WebElement button_HideDocuments;

    @FindBy(xpath = "//a[contains(@id,'ClaimDocuments_EmailButton')]")
    public WebElement button_EmailDocuments;

    @FindBy(xpath = "//a[contains(@id,'ClaimDocuments_TransferDocs')]")
    public WebElement button_TransferDocuments;

    @FindBy(xpath = "//a[contains(@id,'TransferCopyDocumentsPopup:Update')]")
    public WebElement Button_Transfer;


    /// Transfer Documents Page elements ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @FindBy(xpath = "//a[contains(text(),'Return to Documents')]")
    public WebElement link_ReturnToDocuments;

    @FindBy(xpath = "//a[contains(@id, 'Cancel')]")
    public WebElement button_Cancel;

    @FindBy(xpath = "//input[contains(@id,'DocumentsTransferPopup:Claim-inputEl')]")
    public WebElement input_ClaimNumber;

    @FindBy(xpath = "//div[contains(@id,'Claim:SelectClaim')] | //a[contains(@id,'ClaimSearchAndResetInputSet:Search')]")
    public WebElement button_SearchClaimNum;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /// Guidewire Helpers

    public void selectSpecific_RelatedTo(String relation) {
        Guidewire8Select mySelect = select_RelatedTo();
        mySelect.selectByVisibleTextPartial(relation);
    }

    public void selectRandom_RelatedTo() {
        Guidewire8Select mySelect = select_RelatedTo();
        mySelect.selectByVisibleTextRandom();
    }

    public void selectSpecific_DocumentType(String type) {
        Guidewire8Select mySelect = select_DocumentType();
        mySelect.selectByVisibleTextPartial(type);
    }

    public void selectRandom_DocumentType() {
        Guidewire8Select mySelect = select_DocumentType();
        mySelect.selectByVisibleTextRandom();
    }

    public void sendNameOrIndentifier(String nameOrIdent) {
        input_NameOrIdentifier.sendKeys(nameOrIdent);
    }

    public void selectSpecific_DocumentStatus(String status) {
        Guidewire8Select mySelect = select_Status();
        mySelect.selectByVisibleTextPartial(status);
    }

    public void selectRandom_DocumentStatus() {
        Guidewire8Select mySelect = select_Status();
        mySelect.selectByVisibleTextRandom();
    }

    public void sendDocumentAuthor(String auth) {
        input_Author.sendKeys(auth);
    }

    public void clickSearchButton() {
        clickWhenClickable(button_Search);
    }

    public void clickResetButton() {
        clickWhenClickable(button_Reset);
    }

    public void clickHideDocuments() {
        clickWhenClickable(button_HideDocuments);
    }

    public void clickEmailDocuments() {
        clickWhenClickable(button_EmailDocuments);
    }

    public void clickTransferDocuments() {
        clickWhenClickable(button_TransferDocuments);
    }

    public void clickReturnToDocuments() {
        clickWhenClickable(link_ReturnToDocuments);
    }

    public void clickCancelButton() {
        clickWhenClickable(button_Cancel);
    }

    public void sendClaimNumber(String cNum) {
        input_ClaimNumber.sendKeys(cNum);
    }

    public void clickSearchClaim() {
        clickWhenClickable(button_SearchClaimNum);
    }

    public void clickTranserButton() {
        clickWhenClickable(Button_Transfer);
    }

    // Copy of TableUtils function with different xpath. 
    public void setCheckboxInTableByText(WebElement tableDivElement, String textInTable, boolean setCheckboxTrueFalse) {
        WebElement tableCheckBoxElement = null;
        try {
            tableCheckBoxElement = tableDivElement.findElement(By.xpath("//tbody/descendant::div[contains(text(), '" + textInTable + "')]/../preceding-sibling::td/div[contains(@class, '-inner-checkcolumn')]"));
        } catch (Exception e) {
            tableCheckBoxElement = tableDivElement.findElement(By.xpath("//tbody/descendant::a[contains(text(), '" + textInTable + "')]/../../../td/div[contains(@class, '-inner-checkcolumn')]"));
        }
        if (setCheckboxTrueFalse == true) {
            if (isTableCheckboxChecked(tableCheckBoxElement)) {
                //Do Nothing
            } else {
                clickWhenClickable(tableCheckBoxElement);
            }
        } else {
            if (isTableCheckboxChecked(tableCheckBoxElement)) {
                clickWhenClickable(tableCheckBoxElement);
            } else {
                //Do Nothing
            }
        }
    }

    private boolean isTableCheckboxChecked(WebElement tableCheckBoxElement) {
        List<WebElement> possibleCheckboxMatches = tableCheckBoxElement.findElements(By.xpath(".//div[contains(@class,'grid-checkcolumn-checked')] | .//div/img[contains(@class,'grid-checkcolumn-checked')]"));
        return possibleCheckboxMatches.size() > 0;
    }

    public void deleteDocument(String documentName) {
        WebElement documentsTable = find(By.xpath("//div[contains(@id,'Claim_DocumentsScreen:DocumentsLV')]"));
        WebElement row = documentsTable.findElement(By.xpath("//a[contains(text(),'"+documentName+"')]/ancestor::tr"));
        row.findElement(By.xpath("//a[contains(text(),'Delete')]")).click();
        clickOK();
    }
}
