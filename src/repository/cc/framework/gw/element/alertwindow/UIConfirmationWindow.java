package repository.cc.framework.gw.element.alertwindow;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import repository.cc.framework.gw.element.UIElement;

public class UIConfirmationWindow extends UIElement implements IUIConfirmationWindow {

    private WebElement element;
    private WebDriver driver;

    public UIConfirmationWindow(WebDriver driver, WebElement element) {
        super(driver, element);
        this.element = element;
        this.driver = driver;
    }

    @Override
    public void clickOkButton() {
        this.element.findElement(By.linkText("OK")).click();
    }

    @Override
    public void clickCancelButton() {
        this.element.findElement(By.linkText("Cancel")).click();
    }

    @Override
    public void clickButtonWithText(String buttonText) {
        this.element.findElement(By.linkText(buttonText)).click();
    }
}
