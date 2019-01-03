package repository.cc.framework.gw.element;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import repository.cc.framework.gw.cc.ClaimDBSearch;
import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.gw.element.alertwindow.UIConfirmationWindow;
import repository.cc.framework.gw.element.checkbox.UICheckbox;
import repository.cc.framework.gw.element.selectbox.UISelectBox;
import repository.cc.framework.gw.element.table.UITable;
import repository.cc.framework.gw.element.textbox.UITextbox;
import repository.gw.helpers.WaitUtils;
import repository.cc.framework.gw.element.Identifier;
import repository.cc.framework.gw.element.UIElement;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class UIActions {

    private WebDriver driver;
    private WaitUtils waitUtils;
    public ClaimDBSearch withDB;

    public UIActions(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(this.driver);
    }

    private WebElement findElement(Identifier identifier) {

        WebElement element = null;

        try {
            element = waitUtils.waitUntilElementIsVisible(identifier.getReference(), 10);
        } catch (TimeoutException t) {
            try {
                element = waitUtils.waitUntilElementIsClickable(identifier.getReference(), 20);
            } catch (TimeoutException e) {
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("*** See Stack Trace ***");
        }
        if (!element.isEnabled()) {
            System.out.println("Element is not Enabled");
        }
        return element;
    }

    private WebElement findOptional(Identifier identifier) {

        WebElement element = null;

        try {
            element = waitUtils.waitUntilElementIsVisible(identifier.getReference(), 1);
        } catch (TimeoutException t) {
            try {
                element = waitUtils.waitUntilElementIsClickable(identifier.getReference(), 1);
            } catch (TimeoutException e) {
                System.out.println("Optional Element Not Found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getLocalizedMessage());
        }
        return element;
    }

    private List<WebElement> findOptionalElements(Identifier identifier) {

        List<WebElement> elements = null;

        try {
            elements = waitUtils.waitUntilElementsAreVisible(identifier.getReference(), 1);
        } catch (TimeoutException t) {
            try {
                elements = waitUtils.waitUntilElementsAreVisible(identifier.getReference(), 1);
            } catch (TimeoutException e) {
                System.out.println("Optional Element Not Found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getLocalizedMessage());
        }
        return elements;
    }

    public void withTabArrow(Identifier identifier) {
        WebElement element = findElement(identifier);
        Dimension size = element.getSize();
        Actions build = new Actions(this.driver);
        build.moveToElement(element, size.getWidth() - 12, 10).click().build().perform();
    }

    public UITable withTable(Identifier identifier) {
        WebElement element = findElement(identifier);
        if (!element.getTagName().equalsIgnoreCase("table")) {
            element = element.findElements(By.tagName("table")).get(0);
        }
        return new UITable(this.driver, element, identifier);
    }

    public UITextbox withTexbox(Identifier identifier) {
        WebElement element = findElement(identifier);
        return new UITextbox(this.driver, element);
    }

    public UISelectBox withSelectBox(Identifier identifier) {
        this.findElement(CCIDs.ESCAPE_CLICKER).click();

        WebElement selectBox = findElement(identifier);
        selectBox.click();

        List<WebElement> listElements = new ArrayList<>(findElement(CCIDs.LIST_OPTIONS).findElements(By.tagName("li")));
        return new UISelectBox(this.driver, selectBox, listElements);
    }

    public UISelectBox withOptionalSelectBox(Identifier identifier) {
        this.findElement(CCIDs.ESCAPE_CLICKER).click();

        WebElement selectBox = null;
        List<WebElement> listElements = new ArrayList<>();

        try {
            selectBox = findOptional(identifier);
            if (selectBox != null) {
                selectBox.click();
                listElements = new ArrayList<>(findOptional(CCIDs.LIST_OPTIONS).findElements(By.tagName("li")));
            }
        } catch (TimeoutException t) {
            System.out.println("Element Not found.  Returning shell element.");
        }
        return new UISelectBox(this.driver, selectBox, listElements);
    }

    public UIElement withElement(Identifier identifier) {
        WebElement element = null;
        try {
            element = findElement(identifier);
        } catch (TimeoutException t) {
            System.out.println("Element Not found.  Returning shell element.");
        }
        return new UIElement(this.driver, element);
    }

    public UIElement withOptionalElement(Identifier identifier) {
        WebElement element = null;
        try {
            element = findOptional(identifier);
        } catch (TimeoutException t) {
            System.out.println("Element Not found.  Returning shell element.");
        }
        return new UIElement(this.driver, element);
    }

    public void waitUntilElementVisible(Identifier identifier, int waitSeconds) {
        WebElement element = (new WebDriverWait(driver, waitSeconds))
                .until(ExpectedConditions.presenceOfElementLocated(identifier.getReference()));
    }

    public UICheckbox withCheckbox(Identifier identifier) {
        WebElement element = findElement(identifier);
        return new UICheckbox(this.driver, element);
    }

    public UIConfirmationWindow withConfirmationWindow() {
        WebElement element = findElement(new Identifier(By.cssSelector("div[id*='messagebox-']")));
        return new UIConfirmationWindow(this.driver, element);
    }


    /**
     * @deprecated This method is not for public use.
     */
    @Deprecated
    public void initializeDatabase(Connection connection) {
        this.withDB = new ClaimDBSearch(connection);
    }
}

