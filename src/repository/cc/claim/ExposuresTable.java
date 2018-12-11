package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.WaitUtils;

import java.util.ArrayList;
import java.util.List;

public class ExposuresTable extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public ExposuresTable(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public ExposuresTable onExposureTable() {
        try {
            waitUtils.waitUntilElementIsVisible(By.cssSelector("span[id$='ExposuresScreen:ttlBar']"));
        } catch (Exception e) {
            List<WebElement> messageElements = finds(By.cssSelector("div[class='message']"));
            List<String> errorMessages = new ArrayList<>();

            for (WebElement messageElement : messageElements) {
                if (!messageElement.getText().isEmpty()) {
                    errorMessages.add(messageElement.getText());
                }
            }

            if (errorMessages.size() > 0) {
                for (String errorMessage : errorMessages) {
                    System.out.println(errorMessage);
                }
                Assert.fail("Unable to Add Coverage to Exposures.");
            } else {
                Assert.fail("The Exposures Table Page Failed to Load.");
            }
        }

        return this;
    }

    public List<WebElement> getIncidentTypeLinks() {
        List<WebElement> incidentTypeLinks = finds(By.cssSelector("div[id*='ClaimExposuresScreen:ExposuresLV-body'] table tbody tr td  a[id*='CoverageItem']"));
        return incidentTypeLinks;
    }

    public EditExposures selectExposure(int rowNumberSelection) {
        List<WebElement> exposureLinks = getIncidentTypeLinks();
        exposureLinks.get(rowNumberSelection).click();
        return new EditExposures(this.driver);
    }


}
