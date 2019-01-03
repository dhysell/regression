package repository.cc.desktop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.WaitUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClaimSurveyListing extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public ClaimSurveyListing(WebDriver webDriver) {
        super(webDriver);
        this.driver = webDriver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean confirmSurveyAdded(String claimNumber) {

        AtomicBoolean surveyIsFound = new AtomicBoolean(false);

        List<WebElement> claimNumberElements = finds(By.cssSelector("a[id*=':ClaimNumber']"));
        List<String> claimNumbers = new ArrayList<>();

        claimNumberElements.forEach((claimNumberElement) -> claimNumbers.add(claimNumberElement.getText()));
        claimNumbers.forEach((currentClaimNumber) -> {
            if (currentClaimNumber.equalsIgnoreCase(claimNumber)) {
                surveyIsFound.set(true);
            }
        });

        if(driver.findElements(By.cssSelector("div[class*='message']")).size() != 0){
            if (find(By.cssSelector("div[class*='message']")).getText()
                    .equalsIgnoreCase("Claim survey already added...please view on the listing page!")) {
                surveyIsFound.set(true);
            }
        }

        return surveyIsFound.get();
    }
}
