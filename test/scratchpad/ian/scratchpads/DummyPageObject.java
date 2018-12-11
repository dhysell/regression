package scratchpad.ian.scratchpads;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;

public class DummyPageObject extends BasePage {

    private WebDriver driver;

    public DummyPageObject(WebDriver webDriver) {
        super(webDriver);
//        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "input[id$='']")
    private WebElement input_Something;

    public void filloutPage() {
        // fills out a page.
        clickWhenClickable(input_Something);
        systemOut("asdfasdf");
        clickWhenVisible(input_Something);
        setText(input_Something, "as;dlfkjasd;lkfjasdl;kf");
        waitForPostBack();
        waitUntilElementIsClickable(input_Something);
        find(By.cssSelector("input#id"));
    }
}
