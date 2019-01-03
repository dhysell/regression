package repository.cc.framework.gw.element.table;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import repository.cc.framework.gw.element.UIElement;

import java.util.List;

public class UITableRow extends UIElement implements IUITableRow {

    private WebDriver driver;
    private WebElement element;

    public UITableRow(WebDriver driver, WebElement element) {
        super(driver, element);
        this.driver = driver;
        this.element = element;
    }

    @Override
    public List<UITableCell> getCells() {
        return null;
    }

    @Override
    public UITableCell getCell(int cellNum) {
        WebElement element = this.element.findElements(By.tagName("td")).get(cellNum);
        if (element != null) {
            return new UITableCell(this.driver, element);
        } else {
            return null;
        }
    }

    @Override
    public UITableCell getCellByText(String cellText) {
        List<WebElement> cells = this.element.findElements(By.tagName("td"));
        for (WebElement cell : cells) {
            if (cell.getText().equalsIgnoreCase(cellText)) {
                return new UITableCell(this.driver, cell);
            }
        }
        return null;
    }

    @Override
    public boolean hasValueAtCell(String whatToSearch, int cellNumber) {
        return false;
    }

    @Override
    public void clickCheckBox() {
        this.getCell(0).clickCheckbox();
    }

    @Override
    public void clickSelectButton() {
        this.element.findElement(By.linkText("Select")).click();
    }

    @Override
    public void clickButtonWithText(String buttonText) {
        Actions builder = new Actions(this.driver);
        builder.click(this.element.findElement(By.linkText(buttonText))).moveByOffset(1, 1).build().perform();
        //this.element.findElement(By.linkText(buttonText)).click();
    }

    @Override
    public void clickRadioWithLabel(String label) {
        this.element.findElement(By.xpath(".//td/label[contains(text(),'" + label + "')]/preceding-sibling::input")).click();
    }
}
