package repository.cc.administration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ScriptParameter;
import repository.gw.helpers.WaitUtils;

public class ScriptParametersCC extends BasePage {

    WaitUtils waitUtils;

    public ScriptParametersCC(WebDriver webDriver) {
        super(webDriver);
        this.waitUtils = new WaitUtils(getDriver());
        PageFactory.initElements(webDriver, this);
    }

    @FindBy(css = "div[id*=':ScriptParametersLV-body'] div table")
    private WebElement tableScriptParameters;

    @FindBy(css = "a span span span[class*='-page-next']")
    private WebElement buttonNextPage;

    private void clickNextPageButton() {
        waitUtils.waitUntilElementIsClickable(buttonNextPage);
        buttonNextPage.click();
    }

    @FindBy(xpath = "//span[contains(text(),'Script Parameters')]")
    private WebElement pageHeader;

    public void clickPageHeader() {
        waitUtils.waitUntilElementIsVisible(pageHeader);
        pageHeader.click();
    }

    @FindBy(css = "input[id*='ListPaging-inputEl']")
    private WebElement inputPage;

    private void setPageNumber(int page) {
        waitUtils.waitUntilElementIsVisible(inputPage, 10);
        inputPage.clear();
        inputPage.sendKeys(Integer.toString(page));
        inputPage.sendKeys(Keys.ENTER);
        waitForPostBack();
    }
    
//    :FirstSubjectCharacter-triggerWrap
    private Guidewire8Select startsWith() {
        return new Guidewire8Select(getDriver(), "//table[contains(@id, 'FirstSubjectCharacter-triggerWrap')]");
    }

    public void selectScriptParameterFilter(String filterBy) {
        Guidewire8Select mySelect = startsWith();
    	waitUntilElementIsClickable(mySelect.getSelectButtonElement(), 10);
        mySelect.selectByVisibleText(filterBy);
    }
    
    public void modifyScriptParameter(ScriptParameter scriptParameter, boolean yesNo) {

        WebElement hyperLink = null;
        boolean found = false;
        int count = 2;

        while (found == false) {
            try {
                hyperLink = find(By.xpath("//a[contains(text(),'" + scriptParameter + "')]"));
                found = true;
            } catch (Exception e) {
                setPageNumber(count);
                count++;
            }
        }
        waitUtils.waitUntilElementIsClickable(hyperLink, 5);
        hyperLink.click();

        clickEdit();

        setRadioValue(yesNo);
    }

    private void setRadioValue(boolean value) {
        if (value == true) {
            find(By.cssSelector("input[id*='BitValue_true-inputEl']")).click();
        } else if (value == false) {
            find(By.cssSelector("input[id*='BitValue_false-inputEl']")).click();
        }

        clickUpdate();
    }
}
