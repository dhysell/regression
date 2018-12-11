package repository.pc.desktop;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.BatchSource;
import repository.gw.enums.BatchType;

public class DesktopBulkAgentChange extends BasePage {

    private WebDriver driver;

    public DesktopBulkAgentChange(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    @FindBy(xpath = "//*[contains(@id, 'DesktopBulkAgentUpdate:BPCMenuCV:PCAdminDV:searchBPCBatch-inputEl')]")
    public WebElement select_DesktopBulkAgentChangeBatchIdFilter;

    @FindBy(xpath = "//span[contains(@id, 'DesktopBulkAgentUpdate:BPCMenuCV:PCAdminDV:create-btnInnerEl')]")
    public WebElement button_DesktopBulkAgentCreateNewBatch;

    @FindBy(xpath = "//*[contains(@id, 'DesktopBulkAgentUpdate:BPCMenuCV:PCAdminDV:searchBPCDate-inputEl')]")
    public WebElement select_DesktopBulkAgentCreateDateFilter;

    @FindBy(xpath = "//*[contains(@id, 'DesktopBulkAgentUpdate:BPCMenuCV:PCAdminDV:search-btnInnerEl')]")
    public WebElement button_DesktopBulkAgentCreateDateSearch;

    @FindBy(xpath = "//*[contains(@id,'DesktopBulkAgentUpdate:BPCMenuCV:BatchPCLV_tb:testFilter-inputEl')]")
    public WebElement select_DesktopBulkAgentPrintExportSearch;

    @FindBy(xpath = "//span[contains(@id, 'DesktopBulkAgentUpdate:BPCMenuCV:BatchPCLV_tb:PrintMe')]")
    public WebElement button_DesktopBulkAgentPrintExport;

    @FindBy(xpath = "//input[contains(@id, 'CreateBPCPopup:targetDate-inputEl')]")
    public WebElement editbox_BatchTargetDate;

    @FindBy(xpath = "//textarea[contains(@id, 'CreateBPCPopup:policylist-inputEl')]")
    public WebElement editbox_BatchPolicyListing;

    @FindBy(xpath = "//div[contains(@id, 'CreateBPCPopup:newProducerCode:SelectnewProducerCode')]")
    public WebElement link_NewAgentSearch;

    @FindBy(xpath = "//input[contains(@id, 'ProducerCodeSearchScreen:ProducerCodeSearchDV:Code-inputEl')]")
    public WebElement editbox_AgentNumber;

    @FindBy(xpath = "//span[contains(@id, 'CreateBPCPopup:Update-btnEl')]")
    public WebElement button_SubmitBatch;

    @FindBy(xpath = "//span[contains(@id, 'CreateBPCPopup:Cancel-btnEl')]")
    public WebElement button_Cancel;

    private Guidewire8Select select_BatchType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'CreateBPCPopup:batchType-triggerWrap')]");
    }

    private Guidewire8Select select_Source() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'CreateBPCPopup:batchSource-triggerWrap')]");
    }

    @FindBy(xpath = "//div[contains(@id, 'ProducerCodeSearchPopup:ProducerCodeSearchScreen:ProducerCodeSearchLV-body')]/descendant::table")
    public WebElement table_SerchResults;

    @FindBy(xpath = "//div[contains(@id, 'DesktopBulkAgentUpdate:BPCMenuCV:BatchPCLV-body')]//table//td[2]/div")
    public WebElement batchStatus;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    public void setBatchType(BatchType batchType) {
        Guidewire8Select mySelect = select_BatchType();
        mySelect.selectByVisibleText(batchType.getValue());
    }


    public void setBatchSource(BatchSource source) {
        Guidewire8Select mySelect = select_Source();
        mySelect.selectByVisibleText(source.getValue());
    }


    public void setBatchTargetDate(String targetDate) {
        editbox_BatchTargetDate.click();
        editbox_BatchTargetDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_BatchTargetDate.sendKeys(targetDate);
    }


    public void setPolicyList(String policies) {
        editbox_BatchPolicyListing.click();
        editbox_BatchPolicyListing.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_BatchPolicyListing.sendKeys(policies);
    }


    public void clickSubmitBatch() {
        button_SubmitBatch.click();
    }


    public void clickCancel() {
        super.clickCancel();
    }


    public void clickSearch() {
        super.clickSearch();
    }


    public void clickNewAgentSearch() {
        link_NewAgentSearch.click();
    }


    public void setAgentNumber(String agentNum) {
        editbox_AgentNumber.click();
        editbox_AgentNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_AgentNumber.sendKeys(agentNum);
    }


    public void clickNewAgentResults() {
        find(By.xpath(".//child::tbody/child::tr[1]/child::td/div/a[contains(text(), 'Select')]"))
                .click();
    }


    public void setDesktopBulkAgentChangeBatchIdFilter(String statusToSelect) {
        waitUntilElementIsVisible(select_DesktopBulkAgentChangeBatchIdFilter);
        new Select(select_DesktopBulkAgentChangeBatchIdFilter).selectByVisibleText(statusToSelect);
    }


    public void clickDesktopBulkAgentCreateNewBatch() {
        button_DesktopBulkAgentCreateNewBatch.click();
    }


    public void clickDesktopBulkAgentCreateDateFilter() {
        waitUntilElementIsVisible(select_DesktopBulkAgentCreateDateFilter);
        new Select(select_DesktopBulkAgentCreateDateFilter).selectByVisibleText(null);
    }


    public void clickDesktopBulkAgentCreateDateSearch() {
        waitUntilElementIsClickable(button_DesktopBulkAgentCreateDateSearch);
        button_DesktopBulkAgentCreateDateSearch.click();
    }


    public void setDesktopBulkAgentPrintExportSearch(String statusToSelect) {
        waitUntilElementIsVisible(select_DesktopBulkAgentPrintExportSearch);
        new Select(select_DesktopBulkAgentPrintExportSearch).selectByVisibleText(statusToSelect);
    }


    public void clickDesktopBulkAgentPrintExport() {
        waitUntilElementIsClickable(button_DesktopBulkAgentPrintExport);
        button_DesktopBulkAgentPrintExport.click();
    }


    public String getText() {
        return batchStatus.getText();
    }
}
