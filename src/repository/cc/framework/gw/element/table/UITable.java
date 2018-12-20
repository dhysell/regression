package repository.cc.framework.gw.element.table;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import repository.cc.framework.gw.element.Identifier;
import repository.cc.framework.gw.element.UIElement;
import repository.gw.helpers.WaitUtils;

import java.util.ArrayList;
import java.util.List;

public class UITable extends UIElement implements IGWUITable {

    private WebElement element;
    private WebDriver driver;
    private Identifier identifier;

    public UITable(WebDriver driver, WebElement element, Identifier identifier) {
        super(driver, element);
        this.element = element;
        this.driver = driver;
        this.identifier = identifier;
    }

    private void refreshTableElement() {
        WaitUtils waitUtils = new WaitUtils(this.driver);
        String elementID = this.element.getAttribute("id");

        try {
            this.element = waitUtils.waitUntilElementIsVisible(this.identifier.getReference(), 10);
        } catch (TimeoutException t) {
            try {
                this.element = waitUtils.waitUntilElementIsClickable(this.identifier.getReference(), 20);
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
        if (this.element != null) {
            this.element = this.element.findElement(By.tagName("table"));
        }
    }

    @Override
    public List<UITableRow> getRows() {
        List<UITableRow> rows = new ArrayList<>();

        this.element.findElements(By.tagName("tr")).forEach((row) -> {
            rows.add(new UITableRow(this.driver, row));
        });
        return rows;
    }

    @Override
    public void clickAdd() {

    }

    @Override
    public void clickRemove() {

    }

    @Override
    public UITableRow getRowWithText(String value) {

        boolean isLastPage = false;

        do {
            refreshTableElement();
            for (WebElement row : this.element.findElements(By.tagName("tr"))) {
                for (WebElement cell : row.findElements(By.tagName("td"))) {
                    if (cell.getText().toUpperCase().contains(value.toUpperCase())) {
                        return new UITableRow(this.driver, row);
                    }
                }
            }

            if (!this.element.findElement(TOOLBAR_REFERENCE).findElement(NEXT_PAGE_REFERENCE).getAttribute("class").contains("x-btn-disabled")) {
                clickNextPage();
                //this.element = driver.findElement(By.id(""+ tableID +""));
            } else {
                isLastPage = true;
            }
        } while (!isLastPage);
        return null;
    }

    @Override
    public void clickNextPage() {
        this.element.findElement(TOOLBAR_REFERENCE).findElement(NEXT_PAGE_REFERENCE).click();
    }

    @Override
    public void clickPreviousPage() {
        this.element.findElement(TOOLBAR_REFERENCE).findElement(PREVIOUS_PAGE_REFERENCE).click();
    }

    @Override
    public void clickLastPage() {
        this.element.findElement(TOOLBAR_REFERENCE).findElement(LAST_PAGE_REFERENCE).click();
    }

    @Override
    public void clickFirstPage() {
        this.element.findElement(TOOLBAR_REFERENCE).findElement(FIRST_PAGE_REFERENCE).click();
    }

    @Override
    public void clickToolbarButtonWithText(String buttonText) {

    }
}
