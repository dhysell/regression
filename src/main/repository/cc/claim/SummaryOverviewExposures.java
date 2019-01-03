package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

import java.math.BigDecimal;

public class SummaryOverviewExposures extends BasePage {

    public SummaryOverviewExposures(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    private WebElement type;
    private String incident;
    private String coverage;
    private String claimant;
    private String adjuster;
    private String status;
    private BigDecimal remainingReserves = new BigDecimal(0.00);
    private String exposureName;

    public SummaryOverviewExposures buildExposure(int row) {

        if (this.exposureName == null) {
            setExposureName(row);
        }

        setType(row);
        setIncident(row);
        setCoverage(row);
        setClaimant(row);
        setAdjuster(row);
        setStatus(row);
        setRemainingReserves(row);

        return this;
    }

    public void setNewExposureName(String name) {
        this.exposureName = name;
    }

    private void setRemainingReserves(int row) {

        String reservesString = null;

        try {
            reservesString = find(By.xpath("//div[@id='ClaimSummary:ClaimSummaryScreen:ClaimSummaryExposuresLV-body']"
                    + "//tr[@data-recordindex='" + row + "']//td[10]/div")).getText().trim();
            if (reservesString.equals("-") || reservesString.equals("")) {
                reservesString = "0.00";
            }

            try {
                reservesString = reservesString.replace("$", "");
                reservesString = reservesString.replace(",", "");
            } catch (Exception e) {
                // TODO: handle exception
            }

        } catch (Exception e) {
            System.out.println("!!!Parsing Error!!!");
        }

        try {
            this.remainingReserves = new BigDecimal(reservesString);
        } catch (Exception e) {
            System.out.println("Error Creating Big Decimal.");
            this.remainingReserves = new BigDecimal(0.00);
        }


    }

    private void setExposureName(int row) {
        this.exposureName = find(By.xpath("//div[@id='ClaimSummary:ClaimSummaryScreen:ClaimSummaryExposuresLV-body']"
                + "//tr[@data-recordindex='" + row + "']//td[2]/div")).getText();
    }

    private void setStatus(int row) {
        this.status = find(By.xpath("//div[@id='ClaimSummary:ClaimSummaryScreen:ClaimSummaryExposuresLV-body']"
                + "//tr[@data-recordindex='" + row + "']//td[7]/div")).getText();
    }

    private void setAdjuster(int row) {
        this.adjuster = find(By.xpath("//div[@id='ClaimSummary:ClaimSummaryScreen:ClaimSummaryExposuresLV-body']"
                + "//tr[@data-recordindex='" + row + "']//td[6]/div")).getText();
    }

    private void setClaimant(int row) {
        this.claimant = find(By.xpath("//div[@id='ClaimSummary:ClaimSummaryScreen:ClaimSummaryExposuresLV-body']"
                + "//tr[@data-recordindex='" + row + "']//a[contains(@id,':Claimant')]")).getText();
    }

    private void setCoverage(int row) {
        this.coverage = find(By.xpath("//div[@id='ClaimSummary:ClaimSummaryScreen:ClaimSummaryExposuresLV-body']"
                + "//tr[@data-recordindex='" + row + "']//td[4]/div")).getText();
    }

    private void setIncident(int row) {
        this.incident = find(By.xpath("//div[@id='ClaimSummary:ClaimSummaryScreen:ClaimSummaryExposuresLV-body']"
                + "//tr[@data-recordindex='" + row + "']//a[contains(@id,':CoverageItem')]")).getText();
    }

    private void setType(int row) {
        this.type = find(By.xpath("//div[@id='ClaimSummary:ClaimSummaryScreen:ClaimSummaryExposuresLV-body']"
                + "//tr[@data-recordindex='" + row + "']//a[contains(@id,':Type')]"));
    }

    public WebElement getType() {
        return this.type;
    }

    public String getIncident() {
        return this.incident;
    }

    public String getCoverage() {
        return this.coverage;
    }

    public String getClaimant() {
        return this.claimant;
    }

    public String getAdjuster() {
        return this.adjuster;
    }

    public String getStatus() {
        return this.status;
    }

    public BigDecimal getRemainingReserves() {
        return this.remainingReserves;
    }

    public String getExposureName() {
        return this.exposureName;
    }
}
