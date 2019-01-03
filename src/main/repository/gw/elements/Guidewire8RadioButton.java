package repository.gw.elements;

import org.openqa.selenium.*;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;

public class Guidewire8RadioButton extends BasePage {

    private WebElement radioGroup;

    public Guidewire8RadioButton(WebDriver driver, String radioGroupDivXPath) {
        super(driver);
        this.radioGroup = find(By.xpath(radioGroupDivXPath));
    }

    public Guidewire8RadioButton(WebDriver driver, WebElement radioGroupDivWebElement) {
        super(driver);
        this.radioGroup = radioGroupDivWebElement;
    }

    public void select(boolean yesno) {
        boolean setCorrectly = false;
        int i = 0;
        while (!setCorrectly && i < 5) {
        	waitForPostBack();
            if (yesno) {
                WebElement radioYes = find(By.xpath(getXpathFromElement(this.radioGroup))).findElement(By.xpath(".//descendant::label[text()='Yes']/preceding-sibling::input"));
                waitUntilElementIsClickable(radioYes);
                clickAndHoldAndRelease(radioYes);
                String xpathToCheck = getXpathFromElement(this.radioGroup) + "/descendant::label[text()='Yes']/parent::td[contains(@class, 'x-form-cb-checked')]";
                if (checkIfElementExists(xpathToCheck, 500)) {
                    setCorrectly = true;
                } else {
                    xpathToCheck = getXpathFromElement(this.radioGroup) + "/descendant::label[text()='Yes']/preceding-sibling::input[contains(@class, 'x-form-radio-focus')]";
                    if (checkIfElementExists(xpathToCheck, 500)) {
                        setCorrectly = true;
                    }
                }
            } else {
                WebElement radioNo = find(By.xpath(getXpathFromElement(this.radioGroup))).findElement(By.xpath(".//descendant::label[text()='No']/preceding-sibling::input"));
                waitUntilElementIsClickable(radioNo);
                clickAndHoldAndRelease(radioNo);
                String xpathToCheck = getXpathFromElement(this.radioGroup) + "/descendant::label[text()='No']/parent::td[contains(@class, 'x-form-cb-checked')]";
                if (checkIfElementExists(xpathToCheck, 500)) {
                    setCorrectly = true;
                } else {
                    xpathToCheck = getXpathFromElement(this.radioGroup) + "/descendant::label[text()='No']/preceding-sibling::input[contains(@class, 'x-form-radio-focus')]";
                    if (checkIfElementExists(xpathToCheck, 500)) {
                        setCorrectly = true;
                    }
                }
            }
            i++;
        }
        if (!setCorrectly) {
            Assert.fail("The correct radio button could not be set after 5 attempts. Please investigate the cause.");
        }
    }

    public void select(String labelValueToSelect) {
        WebElement radioCorrespondingToLabel = this.radioGroup.findElement(By.xpath(".//label[text()='" + labelValueToSelect + "']"));
        waitUntilElementIsClickable(radioCorrespondingToLabel);
        clickAndHoldAndRelease(radioCorrespondingToLabel);
    }

    public boolean isDisplayed() {
        return radioGroup.isDisplayed();
    }

    public WebElement getWebElement() {
        return radioGroup;
    }

    public String getTagName() {
        return radioGroup.getTagName();
    }

    public String getAttribute(String name) {
        return radioGroup.getAttribute(name);
    }

    public boolean isSelected() {
        return radioGroup.isSelected();
    }

    public boolean isSelected(boolean yesno) {
        String xpathToCheck;
        if (yesno) {
            xpathToCheck = getXpathFromElement(this.radioGroup) + "/descendant::label[text()='Yes']/parent::div/parent::td/parent::tr/parent::tbody/parent::table[contains(@class, 'x-form-cb-checked')]";
        } else {
            xpathToCheck = getXpathFromElement(this.radioGroup) + "/descendant::label[text()='No']/parent::div/parent::td/parent::tr/parent::tbody/parent::table[contains(@class, 'x-form-cb-checked')]";
        }

        return checkIfElementExists(xpathToCheck, 500);
    }

    public boolean isEnabled() {
        return radioGroup.isEnabled();
    }

    public Point getLocation() {
        return radioGroup.getLocation();
    }

    public Dimension getSize() {
        return radioGroup.getSize();
    }

    public Rectangle getRect() {
        return radioGroup.getRect();
    }

    public String getCssValue(String propertyName) {
        return radioGroup.getCssValue(propertyName);
    }
}
