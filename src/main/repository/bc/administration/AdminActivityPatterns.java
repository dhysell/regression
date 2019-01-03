package repository.bc.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.TableUtils;

public class AdminActivityPatterns extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public AdminActivityPatterns(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    // -----------------------------------------------
    // "Recorded Elements" and their XPaths for Activity Patterns page
    // -----------------------------------------------
    public Guidewire8Select comboBox_AdminActivityPatternsFilter() throws Exception {
        return new Guidewire8Select(driver, "//table[contains(@id,':ActivityPatternsFilter-triggerWrap')]");
    }

    @FindBy(xpath = "//div[@id='ActivityPatterns:ActivityPatternsScreen:ActivityPatternsLV')]")
    public WebElement table_AdministrationActivityPatterns;

    @FindBy(xpath = "//a[@id='ActivityPatterns:ActivityPatternsScreen:ActivityPatterns_NewActivityPatternButton']")
    public WebElement button_AdministrationNewActivityPattern;

    @FindBy(xpath = "//a[@id='ActivityPatterns:ActivityPatternsScreen:ActivityPatterns_DeleteButton']")
    public WebElement button_AdministrationActivityPatternsDelete;

    // -----------------------------------------------
    // "Recorded Elements" and their XPaths for New Activity Patterns page
    // -----------------------------------------------
    @FindBy(xpath = "//input[@id='NewActivityPattern:ActivityPatternDetailScreen:ActivityPatternDV:Subject-inputEl']")
    public WebElement editbox_AdministrationNewActivityPatternSubject;

    public Guidewire8Select comboBox_AdminNewActivityPatternsCategory() throws Exception {
        return new Guidewire8Select(driver, "//table[contains(@id,':ActivityPatternDV:Category-triggerWrap')]");
    }

    @FindBy(xpath = "//input[@id='NewActivityPattern:ActivityPatternDetailScreen:ActivityPatternDV:Code-inputEl']")
    public WebElement editbox_AdministrationNewActivityPatternCode;

    public Guidewire8Select comboBox_AdminNewActivityPatternsPriority() throws Exception {
        return new Guidewire8Select(driver, "//table[contains(@id,':ActivityPatternDV:Priority-triggerWrap')]");
    }

    // -------------------------------------------------------
    // Helper Methods for Above Elements for Activity Patterns screen
    // -------------------------------------------------------

    public WebElement getAdminActivityPatternsTable() {
        waitUntilElementIsVisible(table_AdministrationActivityPatterns);
        return table_AdministrationActivityPatterns;
    }

    public void clickNewActivityPatternButton() {
        clickWhenVisible(button_AdministrationNewActivityPattern);
    }

    public void clickActivityPatternDeleteButton() {
        clickWhenVisible(button_AdministrationActivityPatternsDelete);
    }

    public void setNewActivityPatternFilter(String category) {
        try {
            comboBox_AdminActivityPatternsFilter().selectByVisibleText(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickActivityPatternsTableCheckBoxBySubject(String subject) {
        tableUtils.setCheckboxInTable(table_AdministrationActivityPatterns, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_AdministrationActivityPatterns, "Subject", subject)), true);
    }

    // -------------------------------------------------------
    // Helper Methods for Above Elements for New Activity Patterns screen
    // -------------------------------------------------------

    public void setAdminNewActivityPatternsSubject(String subject) {
        waitUntilElementIsVisible(editbox_AdministrationNewActivityPatternSubject);
        editbox_AdministrationNewActivityPatternSubject.sendKeys(subject);
    }

    public void setAdminNewActivityPatternsCode(String code) {
        waitUntilElementIsVisible(editbox_AdministrationNewActivityPatternCode);
        editbox_AdministrationNewActivityPatternCode.sendKeys(code);
    }

    public void setNewActivityPatternCategory(String category) {
        try {
            comboBox_AdminNewActivityPatternsCategory().selectByVisibleText(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNewActivityPatternPriority(String priority) {
        try {
            comboBox_AdminNewActivityPatternsPriority().selectByVisibleText(priority);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfCellExistsInTableByLinkText(String linkText) {
        return tableUtils.checkIfLinkExistsInTable(table_AdministrationActivityPatterns, linkText);
    }

}
