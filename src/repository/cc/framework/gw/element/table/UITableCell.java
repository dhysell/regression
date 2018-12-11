package repository.cc.framework.gw.element.table;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import repository.cc.framework.gw.element.UIElement;
import repository.cc.framework.gw.element.checkbox.UICheckbox;

import java.util.List;

public class UITableCell extends UIElement implements IUITableCell {

    private WebDriver driver;
    private WebElement element;

    public UITableCell(WebDriver driver, WebElement element) {
        super(driver, element);
        this.driver = driver;
        this.element = element;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public void clickSelect() {

    }

    @Override
    public void clickLink() {
        element.findElement(By.tagName("a")).click();
    }

    @Override
    public void fillTextBox(String text) {
        this.element.click();
        List<WebElement> textboxes = this.driver.findElements(By.cssSelector("input[id*='input-']"));
        WebElement selectedTextbox = null;
        int currentTextbox = 0;
        for (WebElement textbox : textboxes) {
            Integer idValue = Integer.valueOf(textbox.getAttribute("id").split("-")[1]);
            if (idValue > currentTextbox) {
                currentTextbox = idValue;
                selectedTextbox = textbox;
            }
        }
        if (selectedTextbox != null) {
            selectedTextbox.clear();
            selectedTextbox.sendKeys(text);
            this.driver.findElement(By.id("QuickJump-inputEl")).click();
        }
    }

    @Override
    public void fillTextArea(String text) {
        this.element.click();
        List<WebElement> textareas = this.driver.findElements(By.cssSelector("textarea[id*='textarea-']"));
        WebElement selectedTextArea = null;
        int currentTextArea = 0;
        for (WebElement textarea : textareas) {
            Integer idValue = Integer.valueOf(textarea.getAttribute("id").split("-")[1]);
            if (idValue > currentTextArea) {
                currentTextArea = idValue;
                selectedTextArea = textarea;
            }
        }
        if (selectedTextArea != null) {
            selectedTextArea.clear();
            selectedTextArea.sendKeys(text);
            this.driver.findElement(By.id("QuickJump-inputEl")).click();
        }
    }

    @Override
    public void clickCheckbox() {
        new UICheckbox(this.driver, this.element.findElement(By.tagName("img"))).click();
    }
}
