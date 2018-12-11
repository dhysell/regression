package repository.cc.framework.utils.helpers;

import org.openqa.selenium.WebDriver;
import repository.gw.helpers.WaitUtils;

import java.util.Arrays;

public class Navigator {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public Navigator(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(this.driver);
    }

    public void navigateTo(Identifier[] identifiers) {
        Arrays.stream(identifiers).forEach(identifier -> {
            new ElementLocator(driver, identifier).click();
        });
    }
}
