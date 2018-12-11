package repository.pc.workorders.generic;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.*;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenericWorkorderCommercialAutoDriver extends GenericWorkorder {

	private TableUtils tableUtils;
	private WebDriver driver;
    public GenericWorkorderCommercialAutoDriver(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//span[contains(@id, ':AddContactsButton-btnEl')]")
    private WebElement button_AddDriver;

    @FindBy(xpath = "//div[contains(@id, ':AddContactsButton:AddNewDriver')]")
    private WebElement button_AddNewDriver;

    @FindBy(xpath = "//span[contains(@id, ':Remove-btnEl')]")
    private WebElement button_RemoveDriver;

    @FindBy(xpath = "//input[contains(@id, ':GlobalPersonNameInputSet:FirstName-inputEl')]")
    private WebElement editbox_FirstName;

    @FindBy(xpath = "//input[contains(@id, ':GlobalPersonNameInputSet:LastName-inputEl')]")
    private WebElement editbox_LastName;
    
    @FindBy(xpath = "//input[contains(@id, ':DateOfBirth-input')]")
	private WebElement editbox_DOB;

    Guidewire8Select select_Gender() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':Gender-triggerWrap')]");
    }

    Guidewire8Select select_MaritalStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':MaritalStatus-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':LicenseNumber-inputEl')]")
    private WebElement editbox_LicenseNumber;

    Guidewire8Select select_LicenseState() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':LicenseState-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':YearLicensed-inputEl')]")
    private WebElement editbox_licenseYear;

    @FindBy(xpath = "//input[contains(@id, ':HireDate-inputEl')]")
    private WebElement editbox_HireDate;

    @FindBy(xpath = "//input[contains(@id, ':DriversAge-inputEl')]")
    private WebElement editbox_Age;

    Guidewire8Select select_RatedVehicle() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ratedVehicles-triggerWrap')]");
    }

    Guidewire8RadioButton radio_Excluded() {
        return new Guidewire8RadioButton(driver,"//table[contains(@id, ':BADriversDV:excluded')]");
    }

    Guidewire8Select select_DriverType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':DriverType-triggerWrap')]");
    }

    Guidewire8Select select_VehicleUse() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':VehicleUse-triggerWrap')]");
    }

    Guidewire8Select select_RalationshipToInsured() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':RelationshipPrimary-triggerWrap')]");
    }

    Guidewire8Select select_PrimaryVehicleDriven() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ratedVehicles-triggerWrap')]");
    }

    /**
     * @author bmartin
     * @Description - Select which vehicle an under-age driver drives based on VIN
     * @DATE - Feb 18, 2016
     */
    public void selectPrimaryVehicleDriven(Vehicle vehicle) {
        Guidewire8Select mySelect = select_PrimaryVehicleDriven();
        if (vehicle == null) {
            mySelect.selectByVisibleTextRandom();
        } else {
            mySelect.selectByVisibleTextPartial(vehicle.getVin());
        }
    }

    @FindBy(xpath = "//div[contains(@class, 'message')]")
    private List<WebElement> validationErrors;

    @FindBy(xpath = "//div[contains(@id, ':BADriversLV-body')]")
    private WebElement table_Drivers;


    public List<WebElement> getValidationErrors() {
        return validationErrors;
    }


    public boolean hasInvalidFormat() {
        List<WebElement> invalidFormat = finds(By.xpath("//span[contains(text(), 'License format for specified State, cannot order MVR')]"));
        return !invalidFormat.isEmpty();
    }


    public void clickAddDriver() {
        long endTime = new Date().getTime() + (10 * 1000);
        do {
            clickWhenClickable(button_AddDriver);
            systemOut("TRY CLICKING ADD NEW DRIVER");
        }
        while (finds(By.xpath("//div[contains(@id, ':AddContactsButton:AddNewDriver')]")).isEmpty() && new Date().getTime() < endTime);

        clickWhenClickable(button_AddNewDriver);
    }


    public void clickRemoveDriver() {
        clickWhenClickable(button_RemoveDriver);
    }


    public void selectAllDrivers() {
        if (!finds(By.xpath("//span[contains(text(), 'License #')]/parent::div/parent::div/parent::div/child::div[1]/span/div")).isEmpty()) {
            clickWhenClickable(find(By.xpath("//span[contains(text(), 'License #')]/parent::div/parent::div/parent::div/child::div[1]/span/div")));
        }
    }


    public void removeDriverByName(String firstName, String lastName) {
        if (selectDriverByName(firstName, lastName)) {
            clickWhenClickable(button_RemoveDriver);
        } else {
            Assert.fail("No drivers were able to be selected to remove");
        }

    }


    public void removeDriverByLicenseNumber(String licenseNumber) {
        if (selectDriverByLicenseNubmer(licenseNumber)) {
            clickWhenClickable(button_RemoveDriver);
        } else {
            Assert.fail("No drivers were able to be selected to remove");
        }

    }


    public boolean selectDriverByName(String firstName, String lastName) {
        List<WebElement> selectableDrivers = table_Drivers.findElements(By.xpath("//tbody/child::tr/child::td[2]/div[contains(text(), '" + lastName + "')]/parent::td/following-sibling::td/div[contains(text(), '" + firstName + "')]/parent::td/parent::tr/child::td[1]/div/img"));
        if (!selectableDrivers.isEmpty()) {
            selectableDrivers.get(0).click();
            return true;
        } else {
            return false;
        }
    }


    public boolean selectDriverByLicenseNubmer(String licenseNumber) {
        //		List<WebElement> selectableDrivers = table_Drivers.findElements(By.xpath("./tbody/child::tr/child::td[5]/div[contains(text(), '" + licenseNumber + "')]/parent::td/parent::tr/child::td[1]/div/img"));
        try {
            tableUtils.setCheckboxInTableByLinkText(table_Drivers, licenseNumber, true);
            return true;
        } catch (Exception e) {
            return false;
        }
        //		if(!selectableDrivers.isEmpty()) {
        //			selectableDrivers.get(0).click();
        //			return true;
        //		} else {
        //			return false;
        //		}
    }


    public boolean clickDriverByLicenseNumber(String licenseNumber) {
        //		List<WebElement> selectableDrivers = table_Drivers.findElements(By.xpath("./tbody/child::tr/child::td[5]/div[contains(text(), '" + licenseNumber + "')]"));
        try {
            tableUtils.clickRowInTableByText(table_Drivers, licenseNumber);
            //			tableUtils.clickLinkInTable(table_Drivers, licenseNumber);
            return true;
        } catch (Exception e) {
            return false;
        }
        //		if(!selectableDrivers.isEmpty()) {
        //			selectableDrivers.get(0).click();
        //			delay(100);
        //			return true;
        //		} else {
        //			return false;
        //		}
    }

    public void setFirstName(String nameToFill) {
		clickWhenClickable(editbox_FirstName);
		editbox_FirstName.sendKeys(Keys.chord(Keys.CONTROL + "a"), nameToFill);
	}
    
    public void setLastName(String nameToFill) {
		clickWhenClickable(editbox_LastName);
		editbox_LastName.sendKeys(Keys.chord(Keys.CONTROL + "a"), nameToFill);
	}
    
    public void setDOB(Date dob) {
		clickWhenClickable(editbox_DOB);
		editbox_DOB.sendKeys(Keys.chord(Keys.CONTROL + "a"), (DateUtils.dateFormatAsString("MM/dd/yyyy", dob)));
	}

    public void setGender(Gender gender) {
        Guidewire8Select mySelect = select_Gender();
        mySelect.selectByVisibleText(gender.name());
    }


    public void setMaritalStatus(MaritalStatus status) {
        Guidewire8Select mySelect = select_MaritalStatus();
        mySelect.selectByVisibleText(status.getValue());
    }


    public void setLicenseNumber(String licenseNumber) {
        clickWhenClickable(editbox_LicenseNumber);
        editbox_LicenseNumber.sendKeys(Keys.CONTROL + "a");
        editbox_LicenseNumber.sendKeys(licenseNumber);
        clickWhenClickable(editbox_FirstName);
    }


    public void setLicenseState(State state) {
        Guidewire8Select mySelect = select_LicenseState();
        mySelect.selectByVisibleText(state.getName());
    }


    public void setLicenseYear(int year) {
        clickWhenClickable(editbox_licenseYear);
        editbox_licenseYear.sendKeys(Keys.CONTROL + "a");
        editbox_licenseYear.sendKeys(String.valueOf(year));
    }


    public void setHireDate(String hireDate) {
        clickWhenClickable(editbox_HireDate);
        editbox_HireDate.sendKeys(Keys.CONTROL + "a");
        editbox_HireDate.sendKeys(hireDate);
    }


    public void setAge(int age) {
        clickWhenClickable(editbox_Age);
        editbox_Age.sendKeys(Keys.CONTROL + "a");
        editbox_Age.sendKeys(String.valueOf(age));
    }


    public void setExcluded(boolean yesno) {
        radio_Excluded().select(yesno);
    }


    public void setPrimaryVehicleDriven(String vehicle) {
        Guidewire8Select mySelect = select_RatedVehicle();
        mySelect.selectByVisibleTextPartial(vehicle);
    }


    public void setDriverType(DriverType type) {
        Guidewire8Select mySelect = select_DriverType();
        mySelect.selectByVisibleText(type.getValue());
    }


    public void setVehicleUse(VehicleUse use) {
        Guidewire8Select mySelect = select_VehicleUse();
        mySelect.selectByVisibleText(use.getValue());
    }


    public void setRelationshipToInsured(RelationshipToInsuredCPP relation) {
        Guidewire8Select mySelect = select_RalationshipToInsured();
        mySelect.selectByVisibleText(relation.getValue());
    }


    public void addDriver(Contact driver) {
        clickAddDriver();
        setFirstName(driver.getFirstName());
        setLastName(driver.getLastName());
        setGender(driver.getGender());
        setDOB(driver.getDob());
        setMaritalStatus(driver.getMaritalStatus());
        setLicenseNumber(driver.getDriversLicenseNum());
        setLicenseState(driver.getStateLicenced());
        setExcluded(driver.isExcludedDriver());

        if (driver.getAge() < 21) {
            // setRatedVehicle(driver.getPrimaryVehicleDriven());
            setDriverType(driver.getDriverType());
            setVehicleUse(driver.getVehicleUse());
            //			setRelationshipToInsured(driver.getRelationshipToInsuredCPP());
        }
        setRelationshipToInsured(driver.getRelationshipToInsuredCPP());
    }


    public void fillOutCommercialAutoDriver(GeneratePolicy policy) {

        if (new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
            for (Contact driver : policy.commercialAutoCPP.getDriversList()) {
                addDriverQQ(driver);
            }
        } else {
            //get order of drivers
            List<WebElement> drivers = finds(By.xpath("//div[contains(@id, 'BADriversPanelSet:BADriversLV-body')]/div/table/tbody/child::tr/td[5]"));
            List<String> driversString = new ArrayList<String>();
            for (WebElement dl : drivers) {
                driversString.add(dl.getText());
            }
            for (String driverNumber : driversString) {
                for (Contact driver : policy.commercialAutoCPP.getDriversList()) {
                    if (driver.getDriversLicenseNum().equals(driverNumber)) {
                        fillOutDriverFullApp(driver);
                        break;
                    }
                }
            }
        }
    }


    public void addDriverQQ(Contact driver) {
        clickAddDriver();
        setGender(driver.getGender());
        setDOB(driver.getDob());
        setLicenseNumber(driver.getDriversLicenseNum());
        if (driver.getAge() < 21) {
            setPrimaryVehicleDriven(driver.getPrimaryVehicleDriven());
            setDriverType(driver.getDriverType());
            setVehicleUse(driver.getVehicleUse());
        }
    }


    public void fillOutDriverFullApp(Contact driver) {
        clickDriverByLicenseNumber(driver.getDriversLicenseNum());
        setFirstName(driver.getFirstName());
        setLastName(driver.getLastName());
        setMaritalStatus(driver.getMaritalStatus());
        setLicenseState(driver.getStateLicenced());
        setRelationshipToInsured(driver.getRelationshipToInsuredCPP());
        setExcluded(driver.isExcludedDriver());
    }


}












