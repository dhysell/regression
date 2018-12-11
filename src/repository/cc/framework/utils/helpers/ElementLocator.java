package repository.cc.framework.utils.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import repository.cc.topmenu.TopMenu;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.WaitUtils;

import java.util.ArrayList;
import java.util.List;

public class ElementLocator {

    private Identifier identifier;
    private WebElement element;
    private WebDriver driver;
    private WaitUtils waitUtils;
    private List<ElementLocator> listItems;

    public ElementLocator(WebDriver driver, List<ElementLocator> list, WebElement element) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(this.driver);
        this.listItems = list;
        this.element = element;
    }

    public ElementLocator(WebDriver driver, Identifier identifier) {
        this.identifier = identifier;
        this.driver = driver;
        this.waitUtils = new WaitUtils(this.driver);
        this.element = findElement();
    }

    public ElementLocator(WebDriver driver, WebElement element) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(this.driver);
        this.element = element;
    }

    public ElementLocator(WebDriver driver, By byReference, int waintInSeconds) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(this.driver);
        this.element = waitUtils.waitUntilElementIsClickable(byReference, waintInSeconds);
    }


    public void click() {
        if (this.identifier != null && this.identifier.getType().equals(Identifier.OFFSET)) {
            if (this.identifier.equals(CCIDs.NewClaim.Navigation.STEPS[1]) || this.identifier.equals(CCIDs.Claim.Navigation.STEPS[1])) {
                new TopMenu(this.driver).clickClaimTabArrow();
            }
        } else {
            this.element.click();
        }
    }

    public void fill(String value) {
        this.element.clear();
        this.element.sendKeys(value);
    }

    public void fillIfEmpty(String value) {
        if (this.element.getAttribute("value").equalsIgnoreCase("")) {
            this.element.sendKeys(value);
        }
    }

    public WebElement getElement() {
        return element;
    }

    private WebElement findElement() {
        switch (this.identifier.getType()) {
            case Identifier.ID:
                return findElementByID();
            case Identifier.CSS:
                return findElementByCSS();
            case Identifier.CLASS:
                return findElementByClass();
            case Identifier.NAME:
                return findElementByName();
            case Identifier.OFFSET:
                return null;
        }
        return null;
    }

    private WebElement findElementByName() {
        WebElement element = null;

        switch (this.identifier.getWaitType()) {
            case Identifier.WAIT_CLICK:
                if (this.identifier.equals(CCIDs.LIST_OPTIONS)) {
                    List<WebElement> dropDowns = this.driver.findElements(By.name(this.identifier.getValue()));
                    element = dropDowns.get(dropDowns.size() - 1);
                } else {
                    element = waitUtils.waitUntilElementIsClickable(By.name(this.identifier.getValue()), 10);
                }
                break;
            case Identifier.WAIT_VISIBLE:
                element = waitUtils.waitUntilElementIsVisible(By.name(this.identifier.getValue()), 10);
                break;
            case Identifier.WAIT_OPTIONAL:
                try {
                    element = waitUtils.waitUntilElementIsVisible(By.name(this.identifier.getValue()), 3);
                } catch (Exception e) {
                    // Optional element not found, proceed.
                }
                break;
        }
        return element;
    }


    private WebElement findElementByClass() {
        WebElement element = null;

        switch (this.identifier.getWaitType()) {
            case Identifier.WAIT_CLICK:
                element = waitUtils.waitUntilElementIsClickable(By.className(this.identifier.getValue()), 10);
                break;
            case Identifier.WAIT_VISIBLE:
                element = waitUtils.waitUntilElementIsVisible(By.className(this.identifier.getValue()), 10);
                break;
            case Identifier.WAIT_OPTIONAL:
                try {
                    element = waitUtils.waitUntilElementIsVisible(By.className(this.identifier.getValue()), 3);
                } catch (Exception e) {
                    // Optional element not found, proceed.
                }
                break;
        }
        return element;
    }

    private WebElement findElementByCSS() {
        WebElement element = null;

        switch (this.identifier.getWaitType()) {
            case Identifier.WAIT_CLICK:
                if (this.identifier.equals(CCIDs.LIST_OPTIONS)) {
                    List<WebElement> dropDowns = this.driver.findElements(By.cssSelector(this.identifier.getValue()));
                    element = dropDowns.get(dropDowns.size() - 1);
                } else {
                    element = waitUtils.waitUntilElementIsClickable(By.cssSelector(this.identifier.getValue()), 10);
                }
                break;
            case Identifier.WAIT_VISIBLE:
                element = waitUtils.waitUntilElementIsVisible(By.cssSelector(this.identifier.getValue()), 10);
                break;
            case Identifier.WAIT_OPTIONAL:
                try {
                    element = waitUtils.waitUntilElementIsVisible(By.cssSelector(this.identifier.getValue()), 3);
                } catch (Exception e) {
                    // Optional element not found, proceed.
                }
                break;
        }
        return element;
    }

    private WebElement findElementByID() {
        WebElement element = null;

        switch (this.identifier.getWaitType()) {
            case Identifier.WAIT_CLICK:
                element = waitUtils.waitUntilElementIsClickable(By.id(this.identifier.getValue()), 10);
                break;
            case Identifier.WAIT_VISIBLE:
                element = waitUtils.waitUntilElementIsVisible(By.id(this.identifier.getValue()), 10);
                break;
            case Identifier.WAIT_OPTIONAL:
                try {
                    element = waitUtils.waitUntilElementIsVisible(By.id(this.identifier.getValue()), 3);
                } catch (Exception e) {
                    // Optional element not found, proceed.
                }
                break;
        }
        return element;
    }

    public void clickSelect() {
        this.element.findElement(By.linkText("Select")).click();
    }

    public ElementLocator findRowWithText(String value) {
        List<ElementLocator> rows = this.getRows();
        for (ElementLocator row : rows) {
            for (WebElement cell : row.element.findElements(By.tagName("td"))) {
                if (cell.getText().equalsIgnoreCase(value)) {
                    return row;
                }
            }
        }
        return null;
    }

    public List<ElementLocator> getRows() {

        List<ElementLocator> rows = new ArrayList<>();

        this.element.findElements(By.tagName("tr")).forEach((row) -> {
            rows.add(new ElementLocator(this.driver, row));
        });
        return rows;
    }

    public void select(String selection) {
        for (ElementLocator listItem : this.getSelectItems()) {
            if (listItem.getElement().getText().equalsIgnoreCase(selection)) {
                listItem.click();
                break;
            }
        }
    }

    public void selectIfNotSet(String selection) {
        WebElement select = this.getElement();
        if (this.element.getAttribute("value").equalsIgnoreCase("New...")
                || select.getText().equalsIgnoreCase("<none>")) {
            new Actions(this.driver).sendKeys(Keys.ESCAPE).perform();
            select(selection);
        } else {
            new Actions(this.driver).sendKeys(Keys.ESCAPE).perform();
        }
    }

    public List<ElementLocator> getListItems() {
        return listItems;
    }

    public List<ElementLocator> getSelectItems() {
        return listItems;
    }

    public void clickRandomCheckbox() {
        List<WebElement> checkboxes = this.getElement().findElements(By.tagName("img"));
        WebElement checkbox = checkboxes.get(NumberUtils.generateRandomNumberInt(0, checkboxes.size() - 1));

        Actions builder = new Actions(driver);
        Action clickCheckbox = builder.clickAndHold(checkbox)
                .moveByOffset(1, 1)
                .release(checkbox)
                .build();
        clickCheckbox.perform();
    }

    public void clickCheckbox(int index) {
        List<WebElement> checkboxes = this.getElement().findElements(By.tagName("img"));
        WebElement checkbox = checkboxes.get(index);

        Actions builder = new Actions(driver);
        Action clickCheckbox = builder.clickAndHold(checkbox)
                .moveByOffset(1, 1)
                .release(checkbox)
                .build();
        clickCheckbox.perform();
    }

    public void selectRandom() {
        List<ElementLocator> selectItems = this.getSelectItems();
        List<ElementLocator> validItems = new ArrayList<>();
        int count = 0;

        for (ElementLocator select : selectItems) {
            if (!select.getElement().getText().equalsIgnoreCase("New...")
                    && !select.getElement().getText().equalsIgnoreCase("<none>")) {
                validItems.add(select);
                count++;
            }
        }

        validItems.get(NumberUtils.generateRandomNumberInt(0, count - 1)).getElement().click();
    }

    public void clickFirstLink() {
        List<WebElement> propertiesLinks = this.element.findElements(By.tagName("a"));
        propertiesLinks.get(0).click();
    }

    public void hover() {
        new Actions(this.driver).moveToElement(this.element, 1, 1).build().perform();
    }

    public ElementLocator findCellWithText(String cellText) {
        List<WebElement> cells = this.getElement().findElements(By.tagName("td"));
        for (WebElement cell : cells) {
            if (cell.getText().equalsIgnoreCase(cellText)) {
                return new ElementLocator(driver, cell);
            }
        }
        return null;
    }
}
