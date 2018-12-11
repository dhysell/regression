package repository.gw.elements;

import org.openqa.selenium.*;
import repository.driverConfiguration.BasePage;

import java.util.Date;


public class Guidewire8Checkbox extends BasePage {


    private By tableSelector = null;
    private By checkboxSelector = null;

    public Guidewire8Checkbox(WebDriver driver, String tableDivXPath) {
        super(driver);
        tableSelector = By.xpath(tableDivXPath);
        checkboxSelector = By.xpath(".//descendant::input[@role='checkbox']");
    }

    public Guidewire8Checkbox(WebDriver driver, WebElement tableDivWebElement) {
        super(driver);
        tableSelector = By.xpath(getXpathFromElement(tableDivWebElement));

        checkboxSelector = By.xpath(".//descendant::input[@role='checkbox']");
    }

    public void select(Boolean yesno) {
        waitUntilElementIsClickable(find(tableSelector).findElement(checkboxSelector));
        long stopTime = new Date().getTime() + 5000;
        Boolean isCorrect = false;
        do {
            if (yesno) {
                this.set();
            } else {
                this.clear();
            }

            if (yesno && isSelected()) {
                isCorrect = true;
            } else if (yesno && !isSelected()) {
                this.select(yesno);
            } else if (!yesno && isSelected()) {
                this.select(yesno);
            } else if (!yesno && !isSelected()) {
                isCorrect = true;
            }
        } while (new Date().getTime() < stopTime && !isCorrect);
    }

    public void set() {
        if (!isSelected()) {
            clickWhenClickable(find(tableSelector).findElement(checkboxSelector));
        }
        waitForPostBack();
    }

    public boolean isSelected() {
        try {
            waitForPostBack();
            return find(tableSelector).getAttribute("class").contains("cb-checked");
        } catch (StaleElementReferenceException sere) {
            waitForPostBack();
            return find(tableSelector).getAttribute("class").contains("cb-checked");
        }
    }

    public void clear() {
        if (isSelected()) {
            hoverOverAndClick(find(tableSelector).findElement(checkboxSelector));
        }
        waitForPostBack();
    }

    public String getTagName() {
        return find(tableSelector).findElement(checkboxSelector).getTagName();
    }

    public String getAttribute(String name) {
        return find(tableSelector).findElement(checkboxSelector).getAttribute(name);
    }

    public String getCssValue(String propertyName) {
        return find(tableSelector).findElement(checkboxSelector).getCssValue(propertyName);
    }

    public boolean isEnabled() {
        return find(tableSelector).findElement(checkboxSelector).isEnabled();
    }

    public boolean isDisplayed() {
        return find(tableSelector).findElement(checkboxSelector).isDisplayed();
    }

    public Point getLocation() {
        return find(tableSelector).findElement(checkboxSelector).getLocation();
    }

    public Dimension getSize() {
        return find(tableSelector).findElement(checkboxSelector).getSize();
    }

    public Rectangle getRect() {
        return find(tableSelector).findElement(checkboxSelector).getRect();
    }

    public boolean checkIfElementExists(int checkTime) {
	    int normalizedTime = timeFixer(checkTime);
        boolean found = finds(tableSelector).size() > 0;

        for (int i = 0; !found && i < normalizedTime; ++i) {
            sleep(1); //Unsure if this loop is necessary.
            found = finds(tableSelector).size() > 0;
        }
        return found;
    }
}
