package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.cc.entities.InvolvedParty;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.helpers.WaitUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NewPerson extends BasePage {

    private WaitUtils waitUtils;
    private WebDriver driver;
    private TableUtils tableUtils;

    public NewPerson(WebDriver driver) {
        super(driver);
        this.waitUtils = new WaitUtils(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[contains(@id,':EditableClaimContactRolesLV-body')]")
    public WebElement table_Roles;

    @FindBy(css = "")
    private WebElement tableRowCell;

    @FindBy(css = "div[id*=':ContactBasicsHeaderInputSet:EditableClaimContactRolesLV']")
    private WebElement rolesTableDiv;

    public Guidewire8Select select_Role() {

        waitUtils.waitUntilElementIsVisible(rolesTableDiv, 15);
        tableUtils.clickCellInTableByRowAndColumnName(rolesTableDiv, 1, "Role");
        WebElement selectBox = tableUtils.getCellWebElementInTableByRowAndColumnName(table_Roles, 1, "Role");
        waitUtils.waitUntilElementIsClickable(tableRowCell, 15);
        clickWhenClickable(tableRowCell);
        

        String partialID = selectBox.getAttribute("id");

        return new Guidewire8Select(driver, "//table[contains(@id,'" + partialID + "')]");
    }

    public void setRole(String role) {
        waitUtils.waitUntilElementIsVisible(table_Roles, 15);
        clickWhenClickable(driver.findElement(By.cssSelector("div[id*='ContactBasicsHeaderInputSet:EditableClaimContactRolesLV-body'] " +
                "table tr:first-child td:nth-child(3)")));
        
        String selectAll = Keys.chord(Keys.CONTROL, "a");
        sendArbitraryKeys(selectAll);
        sendArbitraryKeys(Keys.DELETE);
        
        sendArbitraryKeys(role);
        
        sendArbitraryKeys(Keys.ENTER);
        waitForPostBack();
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        
    }

    public InvolvedParty buildNewPersonByName(String[] userFullName, String role) {

        setRole(role);
        clickPageHeader();
        setFirstName(userFullName[0]);
        setLastName(userFullName[1]);
        String ssn = setSSNRandom();

        LocalDate date = LocalDate.now().minusYears(20).minusDays(NumberUtils.generateRandomNumberInt(0, 10000));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String text = date.format(formatter);

        String dateOfBirth = setDateOfBirth(text);

        String gender = setGenderRandom();

        String addressOne = setAddressOne(Integer.toString(NumberUtils.generateRandomNumberInt(1, 9000)) + " Main St");
        String city = setCity("Pocatello");
        String zip = setZip("83204");

        clickUpdate();

        InvolvedParty newContact = new InvolvedParty.Builder()
                .withFirstName(userFullName[0]).withLastName(userFullName[1])
                .withAddressOne(addressOne).withCity(city).withZip(zip)
                .withRole(role).withSSN(ssn).withDateOfBirth(dateOfBirth)
                .withGender(gender).build();

        return newContact;
    }

    @FindBy(css = "input[id*=':GlobalAddressInputSet:City-inputEl']")
    private WebElement cityInput;

    private String setCity(String city) {
        setText(cityInput, city);
        return city;
    }

    @FindBy(css = "input[id*=':GlobalAddressInputSet:PostalCode-inputEl']")
    private WebElement zipInput;

    private String setZip(String zip) {
        setText(zipInput, zip);
        return zip;
    }

    @FindBy(css = "input[id*=':AddressLine1-inputEl']")
    private WebElement addressOneInput;

    private String setAddressOne(String addressOne) {
        setText(addressOneInput, addressOne);
        return addressOne;
    }

    @FindBy(css = "input[id*=':FirstName-inputEl']")
    private WebElement firstNameInput;

    public void setFirstName(String firstName) {
        waitUtils.waitUntilElementIsClickable(firstNameInput, 5000);
        firstNameInput.sendKeys(firstName);
    }

    @FindBy(css = "input[id*=':LastName-inputEl']")
    private WebElement lastNameInput;

    public void setLastName(String lastName) {
        waitUtils.waitUntilElementIsClickable(lastNameInput, 5000);
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
    }

    @FindBy(css = "input[id*=':SSN-inputEl']")
    private WebElement ssnInput;

    public String setSSNRandom() {
        waitUtils.waitUntilElementIsClickable(ssnInput, 5000);

        String groupOne = Integer.toString(NumberUtils.generateRandomNumberInt(500, 599));
        String groupTwo = Integer.toString(NumberUtils.generateRandomNumberInt(10, 99));
        String groupThree = Integer.toString(NumberUtils.generateRandomNumberInt(1000, 9999));

        String ssnString = groupOne + groupTwo + groupThree;

        setText(ssnInput, ssnString);
        return ssnString;
    }

    @FindBy(css = "input[id*=':DateOfBirth-inputEl']")
    private WebElement dateOfBirthInput;

    public String setDateOfBirth(String dateOfBirth) {
        dateOfBirthInput.click();
        dateOfBirthInput.sendKeys(dateOfBirth);
        sendArbitraryKeys(Keys.TAB);

        return dateOfBirth;
    }

    private String dateFormatter(LocalDate dateToFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        String formattedString = dateToFormat.format(formatter);
        return formattedString;
    }

    private Guidewire8Select genderSelect() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Gender-triggerWrap')]");
    }

    public String setGenderRandom() {
        List<String> options = genderSelect().getList();
        int selectionIndex = NumberUtils.generateRandomNumberInt(1, options.size() - 1);
        genderSelect().selectByVisibleText(options.get(selectionIndex));
        return options.get(selectionIndex);
    }

    @FindBy(css = "a[id*=':CustomUpdateButton']")
    private WebElement updateButton;

    public void clickUpdate() {
        waitForPageLoad();
        waitUtils.waitUntilElementIsClickable(updateButton);
        updateButton.click();
        waitForPageLoad();
    }

    @FindBy(css = "span[id*='NewPartyInvolvedPopup:ContactDetailScreen:ttlBar']")
    private WebElement pageHeader;

    private void clickPageHeader() {
        clickWhenClickable(pageHeader);
    }


}
