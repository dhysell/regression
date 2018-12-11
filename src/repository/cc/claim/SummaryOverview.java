package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

import java.util.ArrayList;
import java.util.List;

public class SummaryOverview extends BasePage {

    private WebDriver driver;

    public SummaryOverview(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public List<repository.cc.claim.SummaryOverviewExposures> getExposuresList() {
        List<repository.cc.claim.SummaryOverviewExposures> exposuresList = new ArrayList<>();

        int numExposures = finds(By.xpath("//a[contains(@id,':Type')]")).size();

        for (int i = 0; i < numExposures; i++) {
            repository.cc.claim.SummaryOverviewExposures exposure = new repository.cc.claim.SummaryOverviewExposures(this.driver).buildExposure(i);
            exposuresList.add(exposure);
        }

        return exposuresList;
    }

    public repository.cc.claim.SummaryOverviewExposures getExposuresObject(int expNumber) {

        repository.cc.claim.SummaryOverviewExposures exposure = new SummaryOverviewExposures(this.driver).buildExposure(expNumber);
        return exposure;
    }
}
