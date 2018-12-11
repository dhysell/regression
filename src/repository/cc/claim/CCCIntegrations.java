package repository.cc.claim;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class CCCIntegrations extends BasePage {

    private WebDriver driver;

    public CCCIntegrations(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "a[id*='ClaimSummary:ClaimSummaryScreen:ClaimSummaryExposuresLV:0:CoverageItem']")
    WebElement incidentTypeLink;

    private void clickIncidentTypeLink() {
        clickWhenClickable(incidentTypeLink);
    }

    public void prepareCCCIncident() {

        EditExposures exposure = new EditExposures(this.driver);

        clickIncidentTypeLink();
        exposure.prepCCCIntegrations().addDataForCCC();

    }

}
