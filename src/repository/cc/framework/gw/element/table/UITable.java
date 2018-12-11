package repository.cc.framework.gw.element.table;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import repository.cc.framework.gw.element.UIElement;

import java.util.ArrayList;
import java.util.List;

public class UITable extends UIElement implements IUITable {

    private WebElement element;
    private WebDriver driver;

    public UITable(WebDriver driver, WebElement element) {
        super(driver, element);
        this.element = element;
        this.driver = driver;
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
        for (WebElement row : this.element.findElements(By.tagName("tr"))) {
            for (WebElement cell : row.findElements(By.tagName("td"))) {
                if (cell.getText().toUpperCase().contains(value.toUpperCase())) {
                    return new UITableRow(this.driver, row);
                }
            }
        }
        return null;
    }
}
