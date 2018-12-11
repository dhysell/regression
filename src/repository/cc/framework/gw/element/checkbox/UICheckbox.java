package repository.cc.framework.gw.element.checkbox;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import repository.cc.framework.gw.element.UIElement;

public class UICheckbox extends UIElement implements IUICheckbox {

    private WebElement element;
    private WebDriver driver;

    public UICheckbox(WebDriver driver, WebElement element) {
        super(driver, element);
        this.element = element;
        this.driver = driver;
    }

    @Override
    public void click() {
        Actions builder = new Actions(this.driver);
        Action clickCheckbox = builder.clickAndHold(this.element)
                .moveByOffset(1, 1)
                .release(this.element)
                .build();
        clickCheckbox.perform();
    }
}
