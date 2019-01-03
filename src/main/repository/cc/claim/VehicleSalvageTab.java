package repository.cc.claim;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.WaitUtils;

public class VehicleSalvageTab extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public VehicleSalvageTab(WebDriver driver) {
        super(driver);
        this.driver = getDriver();
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    private Guidewire8Select selectPrimaryDamage() {
        return new Guidewire8Select(driver, "//table[contains(@id,'Property_Damage_RangeInput-triggerWrap')]");
    }

    private void setPrimaryDamage(String selection) {
        selectPrimaryDamage().selectByVisibleText(selection);
    }

    private Guidewire8Select selectTypeOfLoss() {
        return new Guidewire8Select(driver, "//table[contains(@id,'CopartDetailsInputSet:Type_Of_Loss-triggerWrap')]");
    }

    private void setTypeOfLoss(String selection) {
        selectTypeOfLoss().selectByVisibleText(selection);
    }

    private Guidewire8Select selectReserve() {
        return new Guidewire8Select(driver, "//table[contains(@id,'CopartDetailsDV:CopartDetailsInputSet:reserve_typeId-triggerWrap')]");
    }

    private Guidewire8Select selectLocation() {
        return new Guidewire8Select(driver, "//table[contains(@id,'CopartDetailsDV:yard_location-triggerWrap')]");
    }

    private void setLocationRandom() {
        selectLocation().selectByVisibleTextRandom();
    }

    private Guidewire8Select selectPickupAddressLocation() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Address_Picker-triggerWrap')]");
    }

    private void setPickupAddressLocationRandom() {
        selectPickupAddressLocation().selectByVisibleTextRandom();
    }

    private void setReserve(String selection) {
        selectReserve().selectByVisibleText(selection);
    }

    @FindBy(css = "input[id*='CopartDetailsDV:copart_vehicle_location_option2-inputEl']")
    private WebElement radioPickupNotRequired;

    private void clickPickupNotRequired() {
        radioPickupNotRequired.click();
    }

    @FindBy(css = "input[id*=':CopartDetailsDV:copart_vehicle_location_option']")
    private WebElement radioPickupAddress;

    private void clickPickupAddress() {
        waitUtils.waitUntilElementIsClickable(radioPickupAddress);
        Actions build = new Actions(driver);
        build.moveToElement(radioPickupAddress, 6, 5).doubleClick().build().perform();
    }

    @FindBy(css = "span[id*='VehIncidentDetailDV:CopartStateToolbar:SubmitInstruction-btnInnerEl']")
    private WebElement buttonCreateCopartAssignment;

    private void clickCreateCopartAssignment() {
        buttonCreateCopartAssignment.click();
    }

    @FindBy(css = "div[id*='CopartStatusInputSet:AssignmentStatus-inputEl']")
    private WebElement elementAssignmentStatus;

    private String getAssignmentStatus() {
        return elementAssignmentStatus.getText();
    }

    @FindBy(css = "div[id*='CopartStatusInputSet:Description-inputEl']")
    private WebElement elementStatusDescription;

    private String getStatusDescription() {
        return elementStatusDescription.getText();
    }

    @FindBy(css = "a[id*='VehIncidentDetailDV:VehicleDamage_VehicleSalvageCardTab']")
    private WebElement tabVehicleSalvage;

    private VehicleSalvageTab clickVehicleSalvageTab() {
        waitUtils.waitUntilElementIsVisible(tabVehicleSalvage);
        tabVehicleSalvage.click();
        return new VehicleSalvageTab(getDriver());
    }

    private Guidewire8Select selectPickupAddressContactPerson() {
        return new Guidewire8Select(driver, "//table[contains(@id,':CopartDetailsDV:VehicleIncident_AltContactName-triggerWrap')]");
    }

    private void setPickupAddressContactPerson(String selection) {
        selectPickupAddressContactPerson().selectByVisibleText(selection);
    }

    @FindBy(css = "span[id*='ClaimInfoBar:Insured-btnInnerEl'] span[class*='infobar_elem_val']")
    private WebElement elementInsuredName;

    private String getInsuredName() {
        return  elementInsuredName.getText();
    }

    public void createCopartDefaultAssignment() {
        setPrimaryDamage("HAIL STD");
        setTypeOfLoss("HAIL STD");
        setReserve("No");
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        clickPickupAddress();
        setPickupAddressLocationRandom();
        setPickupAddressContactPerson(getInsuredName());
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        clickCreateCopartAssignment();

        clickVehicleSalvageTab();

        waitUtils.waitUntilElementIsVisible(elementAssignmentStatus);

        if (getAssignmentStatus().equalsIgnoreCase("Error")) {
            System.out.println("Copart service returned the following error message: ");
            Assert.fail(getStatusDescription());
        }
    }
}
