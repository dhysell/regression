package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.WaitUtils;

import java.util.ArrayList;
import java.util.List;

public class EditExposures extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;


    public EditExposures(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public EditExposures onEditExposures() {
        try {
            waitUtils.waitUntilElementIsVisible(By.cssSelector("a[id='ExposureDetail:ExposureDetailScreen:Edit']"));
        } catch (Exception e) {
            Assert.fail("The EditExposures Page Failed to Load.");
        }

        return this;
    }

    @FindBy(xpath = "//div[@id='ClaimExposures:ClaimExposuresScreen:ExposuresLV-body']/div/table")
    private WebElement table_Exposures;

    @FindBy(xpath = "//div[@id='ClaimExposures:ClaimExposuresScreen:ExposuresLV-body']/div/table/tbody/tr/td[3]/div/a")
    private List<WebElement> links_Exposures;

    @FindBy(xpath = "//a[@id='ExposureDetail:ExposureDetailScreen:Edit']")
    private WebElement button_Edit;

    private Guidewire8Select coverageSelect() {
        return new Guidewire8Select(driver,"//table[contains(@id,':Coverage-triggerWrap')]");
    }

    @FindBy(css = "span[id='ExposureDetail:ExposureDetailScreen:Update-btnInnerEl']")
    private WebElement updateButton;

    private void clickUpdateButton() {
        clickWhenClickable(updateButton);
    }

    private void setRandomCoverage() {
        List<String> coverages = coverageSelect().getList();
        List<String> validCoverages = new ArrayList<>();
        for (String coverage : coverages) {
            if (!coverage.equalsIgnoreCase("<none>")) {
                validCoverages.add(coverage);
            }
        }

        String selectionText = "";

        try {
            selectionText = validCoverages.get(NumberUtils.generateRandomNumberInt(0, validCoverages.size() - 1));
        } catch (Exception e) {
            System.out.println("Coverage Missing");
            for (String validCoverage : validCoverages) {
                System.out.println(validCoverage);
            }
            Assert.fail("Verrify Coverages Exist.");
        }

        coverageSelect().selectByVisibleText(selectionText);
    }

    @FindBy(css = "a[id='ExposureDetail:ExposureDetail_UpLink']")
    private WebElement upToExposuresLink;

    public void clickUpToExposures() {
        clickWhenClickable(upToExposuresLink);
    }

    private void checkErrorMessages() {
        try {
            List<WebElement> messageElements = finds(By.cssSelector("div[class='message']"));
            for (WebElement messageElement : messageElements) {
                System.out.println(messageElement.getText());
            }
        } catch (Exception e) {

        }
    }


    public void setCoverageAndUpdateExposure() {
        setRandomCoverage();
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        clickUpdateButton();
        checkErrorMessages();
    }

    public int getNumberOfExposureRows() {
        List<WebElement> tableRows = table_Exposures
                .findElements(By.xpath("//tbody/tr[@class='x-grid-row  x-grid-data-row']"));
        int rows = tableRows.size();
        return rows;
    }


    public void clickExposureLinks(int rowNum) {
        clickWhenClickable(links_Exposures.get(rowNum));
    }


    public void clickEditButton() {
        clickWhenClickable(button_Edit);
        waitUtils.waitUntilElementIsVisible(By.cssSelector("span[id*='ExposureDetailScreen:Update-btnInnerEl']"));
    }

    @FindBy(xpath = "//span[contains(@id,':ExposureDetailScreen:ttlBar')]")
    WebElement element_ExposureName;


    public String getExposureName() {
        return element_ExposureName.getText();
    }

    @FindBy(xpath = "//div[contains(@id,':CoverageSubType-inputEl')]")
    WebElement element_CoverageSubtype;


    public String getCoverageSubtype() {
        String rawString = element_CoverageSubtype.getText();
        String costCat = null;

        try {
            String[] parts = rawString.split("-", 2);
            costCat = parts[1].trim();
        } catch (Exception e) {
            costCat = rawString;
        }

        return costCat;
    }

    @FindBy(xpath = "//a[contains(@id,'_ReopenButton')]")
    WebElement buttonReopenExposure;


    public repository.cc.claim.ReopenExposure clickReopenExposureButton() {
        waitUtils.waitUntilElementIsVisible(buttonReopenExposure);
        clickWhenClickable(buttonReopenExposure);
        return new ReopenExposure(this.driver);
    }

    @FindBy(xpath = "//a[@id='ExposureDetail:ExposureDetailScreen:ExposureDetailScreen_CreateReserveButton']")
    WebElement button_CreateReserve;

    public void clickCreateReserveButton() {
        clickWhenClickable(button_CreateReserve);
    }

    @FindBy(css = "a[id*=':Vehicle_Incident:Vehicle_IncidentMenuIcon']")
    private WebElement vehicleIncidentPicker;

    private void clickVehiclePicker() {
        clickWhenClickable(vehicleIncidentPicker);
    }

    @FindBy(css = "a[id*=':VehicleDamageDV_EditIncidentMenuItem-itemEl']")
    private WebElement editIncidentDetailsLink;

    private void clickEditIncidentDetails() {
        clickWhenClickable(editIncidentDetailsLink);
    }

    public repository.cc.claim.Incidents prepCCCIntegrations() {
        clickVehiclePicker();
        clickEditIncidentDetails();
        return new Incidents(this.driver);
    }
}
