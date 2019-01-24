package repository.cc.framework.gw.element.textbox;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import repository.cc.framework.gw.element.UIElement;
import repository.cc.framework.gw.interfaces.IUITextOperations;

public class UITextbox extends UIElement implements IUITextOperations {

    private WebDriver driver;
    private WebElement element;

    public UITextbox(WebDriver driver, WebElement element) {
        super(driver, element);
        this.driver = driver;
        this.element = element;
    }

    @Override
    public void fill(String value) {
        this.element.clear();
        this.element.sendKeys(value);
        this.element.sendKeys(Keys.TAB);
    }

    @Override
    public void fillIfEmpty(String value) {
        String fieldValue = this.element.getAttribute("value");
        if (fieldValue.equalsIgnoreCase("<none>") || fieldValue.equalsIgnoreCase("")) {
            fill(value);
        }
    }

    @Override
    public String screenGrab() {
        return this.element.getAttribute("value");
    }
}
