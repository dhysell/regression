package repository.cc.framework.gw.element;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import repository.cc.framework.gw.interfaces.IUIElementOperations;

public class UIElement implements IUIElementOperations {

    private WebElement element;
    private WebDriver driver;

    public UIElement(WebDriver driver, WebElement element) {
        this.element = element;
        this.driver = driver;
    }

    @Override
    public void click() {
        if (this.isPresent()) {
            this.element.click();
        } else {
            Assert.fail("Element is not Clickable");
        }
    }

    @Override
    public void doubleClick() {
        System.out.println("Not yet implemented.");
    }

    @Override
    public void hover() {
        new Actions(this.driver).moveToElement(this.element, 1, 1).build().perform();
    }

    @Override
    public String screenGrab() {
        if (this.element != null) {
            return this.element.getText();
        } else {
            return "";
        }
    }

    @Override
    public boolean isPresent() {
        return this.element != null && this.element.isEnabled();
    }

    public void clickTabArrow() {
        Dimension size = this.element.getSize();
        Actions build = new Actions(this.driver);
        build.moveToElement(this.element, size.getWidth() - 12, 10).click().build().perform();
    }
}
