package repository.pc.workorders.generic;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.InlandMarineCPP.MiscellaneousArticlesCoverageForm_IH_00_79_Coinsurance;
import repository.gw.enums.InlandMarineCPP.MiscellaneousArticlesCoverageForm_IH_00_79_Deductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPInlandMarineMiscellaneousArticles;
import repository.gw.generate.custom.CPPInlandMarineMiscellaneousArticles_ScheduledItem;

public class GenericWorkorderInlandMarineMiscellaneousArticlesCPP extends BasePage {
    private WebDriver driver;

    public GenericWorkorderInlandMarineMiscellaneousArticlesCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    ///////////////////////
    //       Tabs        //
    ///////////////////////

    @FindBy(css = "span[id$=':miscArticlesPartPanelSet:MiscArticlesPartPanelSet:coveragesTab-btnEl']")
    private WebElement link_CoveragesTab;

    private void clickCoveragesTab() {
        clickWhenClickable(link_CoveragesTab);
        
    }

    @FindBy(css = "span[id$=':miscArticlesPartPanelSet:MiscArticlesPartPanelSet:scheduleArticlesTab-btnEl']")
    private WebElement link_ScheduledItemsTab;

    private void clickScheduledItemsTab() {
        clickWhenClickable(link_ScheduledItemsTab);
        
    }

    @FindBy(css = "span[id$=':miscArticlesPartPanelSet:MiscArticlesPartPanelSet:exclCondTab-btnEl']")
    private WebElement link_ExclusionsAndConditionsTab;

    private void clickExclusionsAndConditionsTab() {
        clickWhenClickable(link_ExclusionsAndConditionsTab);
        
    }

    @FindBy(css = "span[id$=':miscArticlesPartPanelSet:MiscArticlesPartPanelSet:addtInterestTab-btnEl']")
    private WebElement link_AdditionalInterestTab;

    private void clickAdditionalInterestTab() {
        clickWhenClickable(link_AdditionalInterestTab);
        
    }

    ///////////////////////
    //     Coverages     //
    ///////////////////////

    private Guidewire8Select select_Deductible() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Deductible')]/parent::td/following-sibling::td/table");
    }

    private void setDeductible(MiscellaneousArticlesCoverageForm_IH_00_79_Deductible deductible) {
        Guidewire8Select mySelect = select_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
        
    }

    private Guidewire8Select select_Coinsurance() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Coinsurance %')]/parent::td/following-sibling::td/table");
    }

    private void setCoinsurance(MiscellaneousArticlesCoverageForm_IH_00_79_Coinsurance coinsurance) {
        Guidewire8Select mySelect = select_Coinsurance();
        mySelect.selectByVisibleText(coinsurance.getValue());
        
    }

    //Miscellaneous Articles Blanket Coverage IH 79 01

    @FindBy(xpath = "//div[contains(text(), 'Miscellaneous Articles Blanket Coverage IH 79 01')]/preceding-sibling::table//input")
    private WebElement checkbox_MiscArticlesBlanket;

    private Guidewire8Checkbox checkbox_MiscArticlesBlanket() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Miscellaneous Articles Blanket Coverage IH 79 01')]/preceding-sibling::table");
    }

    private boolean checkMiscArticlesBlanket(Boolean checked) {
        if (!checkbox_MiscArticlesBlanket().isSelected() && checked) {
            clickAndHoldAndRelease(checkbox_MiscArticlesBlanket);
        }
        
        return checked;
    }

    @FindBy(xpath = "//label[contains(text(),'Per Occurrence')]/parent::td/following-sibling::td/input")
    private WebElement input_BlanketPerOccurrence;

    private void setBlanketPerOccurrence(String value) {
        input_BlanketPerOccurrence.click();
        input_BlanketPerOccurrence.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(xpath = "//label[contains(text(),'Description')]/parent::td/following-sibling::td/textarea")
    private WebElement textarea_BlanketDescription;

    private void setBlanketDescription(String value) {
        textarea_BlanketDescription.click();
        textarea_BlanketDescription.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    ///////////////////////
    //  SCHEDULED ITEMS  //
    ///////////////////////

    @FindBy(css = "span[id$=':MiscArticlesScheduledArticlesPanelSet:Add-btnEl']")
    private WebElement button_AddScheduledItem;

    private void clickAddScheduledItem() {
        clickWhenClickable(button_AddScheduledItem);
        
    }

    @FindBy(xpath = "//label[contains(text(),'Model Year')]/parent::td/following-sibling::td/input")
    private WebElement input_ModelYear;

    private void setModelYear(String value) {
        input_ModelYear.click();
        input_ModelYear.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(xpath = "//label[contains(text(),'Make')]/parent::td/following-sibling::td/input")
    private WebElement input_Make;

    private void setMake(String value) {
        input_Make.click();
        input_Make.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(xpath = "//label[text()='Model']/parent::td/following-sibling::td/input")
    private WebElement input_Model;

    private void setModel(String value) {
        input_Model.click();
        input_Model.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(xpath = "//label[contains(text(),'Equipment ID')]/parent::td/following-sibling::td/input")
    private WebElement input_EquipmentID;

    private void setEquipmentID(String value) {
        input_EquipmentID.click();
        input_EquipmentID.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(xpath = "//label[contains(text(),'Description')]/parent::td/following-sibling::td/textarea")
    private WebElement textArea_ScheduledItemDescription;

    private void setScheduledItemDescription(String value) {
        textArea_ScheduledItemDescription.click();
        textArea_ScheduledItemDescription.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(xpath = "//label[contains(text(),'Limit')]/parent::td/following-sibling::td/input")
    private WebElement input_ScheduledItemLimit;

    private void setScheduledItemLimit(String value) {
        input_ScheduledItemLimit.click();
        input_ScheduledItemLimit.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    ///////////////////////
    //   PUBLIC METHODS  //
    ///////////////////////

    public void fillOutMiscellaneousArticles(GeneratePolicy policy) {

        CPPInlandMarineMiscellaneousArticles miscArticles = policy.inlandMarineCPP.getMiscellaneousArticles();

        //COVERAGES
        clickCoveragesTab();

        setDeductible(miscArticles.getDeductible());
        setCoinsurance(miscArticles.getCoinsurance());

        if (miscArticles.isMiscellaneousArticlesBlanketCoverage()) {
            checkMiscArticlesBlanket(true);
            setBlanketPerOccurrence("" + miscArticles.getBlanketCoveragePerOccurrance());
            setBlanketDescription(miscArticles.getBlanketCoverageDescription());
        }

        //SCHEDULED ITEMS
        clickScheduledItemsTab();

        for (CPPInlandMarineMiscellaneousArticles_ScheduledItem item : miscArticles.getScheduledItems()) {
            clickAddScheduledItem();

            setModelYear(item.getModelYear());
            setMake(item.getMake());
            setModel(item.getModel());
            setEquipmentID(item.getEquipmentID());
            setScheduledItemDescription(item.getDescription());
            setScheduledItemLimit("" + item.getLimit());
        }

        //EXCLUSIONS AND CONDITIONS
        clickExclusionsAndConditionsTab();


        //ADDITIONAL INTERESTS
        clickAdditionalInterestTab();

    }

}
