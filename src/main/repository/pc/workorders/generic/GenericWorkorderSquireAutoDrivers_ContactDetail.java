package repository.pc.workorders.generic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;

import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AddressType;
import repository.gw.enums.CommuteType;
import repository.gw.enums.HowLongWithoutCoverage;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.NotRatedReason;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SR22FilingFee;
import repository.gw.generate.custom.Contact;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;

public class GenericWorkorderSquireAutoDrivers_ContactDetail extends GenericWorkorderSquireAutoDrivers {
	
	private WebDriver driver;

    public GenericWorkorderSquireAutoDrivers_ContactDetail(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private void onDriver(Contact driver) {
    	if(!finds(By.xpath("//span[(@class='g-title') and (text()='Edit Driver')]")).isEmpty()) {
    		if(!finds(By.xpath("//div[contains(@id, ':LastName-inputEl') and text()='" + driver.getFullName() + "']")).isEmpty()) {
    			
    		}
    	}
	}
    
    public void fillOutContactDetails_QQ(Contact driver) {
    	onDriver(driver);
        fillOutDriver_QQ(driver);
        fillOutPerson_QQ(driver);
    }

    public void fillOutContactDetails_FA(Contact driver) {
        fillOutDriver_FA(driver);
        fillOutPerson_FA(driver);
        fillOutWorkCommuteAddress(driver);
        fillOutPolicyDriverQuestions(driver);

        if (new GuidewireHelpers(getDriver()).errorMessagesExist()) {
            String errorMessage = new GuidewireHelpers(getDriver()).getFirstErrorMessage();
            if (errorMessage.contains("Invalid Drivers License format for specified State, cannot order MVR.")) {
                Assert.fail("Driver License number is null or invalid" + driver.getDriversLicenseNum());
            } else {
                Assert.fail("An Unhandled Error was found on the drivers screen. The error was: " + errorMessage);
            }
        }
    }

    public void fillOutContactDetails(Contact driver) {
        fillOutDriver(driver);
        fillOutPerson(driver);
        fillOutWorkCommuteAddress(driver);
        fillOutPolicyDriverQuestions(driver);
    }


    public void fillOutDriver_QQ(Contact driver) {
    	if(driver.isExcludedDriver()) {
    		setExcludedDriverRadio(true);
    }
    }

    public void fillOutDriver_FA(Contact driver) {
        setPhysicalImpairmentOrEpilepsy(driver.isHasPhysicalImpairmentOrEpilepsy());
    }

    public void fillOutDriver(Contact driver) {
        setSR22Checkbox(driver.hasSR22Charges());
        setPhysicalImpairmentOrEpilepsy(driver.isHasPhysicalImpairmentOrEpilepsy());
    }


    public void fillOutPerson_QQ(Contact driver) {
        selectMaritalStatus(driver.getMaritalStatus());
        selectGender(driver.getGender());
    }

    public void fillOutPerson_FA(Contact driver) {
        selectRelationship(driver.getRelationToInsured());
        setOccupation(driver.getOccupation());
        if (driver.isSpecial) {
            setLicenseNumber("PH105932B");
            selectLicenseState(State.Idaho);
        } else {
            setLicenseNumber(driver.getDriversLicenseNum());
            selectLicenseState(driver.getStateLicenced());
        }
    }

    public void fillOutPerson(Contact driver) {

    }

    @FindBy(xpath = "//a[contains(@id, ':AddressListingMenuIcon')]")
    private WebElement button_PADriversAddressListingLink;
    
    @FindBy(xpath = "//div[contains(@id, ':newAddressBuddy')]")
    private WebElement button_PADriversAddressListingNewLocation;
    
//    PADriverPopup:DriverDetailsCV:FBM_PolicyContactDetailsDV:AddressListing:AddressListingMenuIcon
    public void fillOutWorkCommuteAddress(Contact driver) {
        selectCommuteType(driver.getCommuteType());
        clickProductLogo();
        if(checkIfElementExists(button_PADriversAddressListingLink, 2000)) {
        	repository.pc.workorders.generic.GenericWorkorder wo = new GenericWorkorder(this.driver);
        	wo.setNewAddress(driver.getAddressByType(AddressType.Work));
        }
    }


    private Guidewire8Select select_PersonLicenseState() {
        return new Guidewire8Select(driver, "//table[contains(@id,'PersonNameInputSet:DriversLicenseState2-triggerWrap')]");
    }

    public void selectLicenseState(State state) {
        Guidewire8Select selectLicenseState = select_PersonLicenseState();
        selectLicenseState.selectByVisibleText(state.getName());
    }

    @FindBy(xpath = "//input[contains(@id, 'PersonNameInputSet:licenseNumber-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversLicense;

    public void setLicenseNumber(String licenseNum) {
        JavascriptExecutor jse = (JavascriptExecutor) getDriver();
        jse.executeScript("arguments[0].click", editbox_GenericWorkorderPolicyPersonalAutoDriversLicense);
        editbox_GenericWorkorderPolicyPersonalAutoDriversLicense.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_GenericWorkorderPolicyPersonalAutoDriversLicense.sendKeys(String.valueOf(licenseNum));
    }

    @FindBy(xpath = "//input[contains(@id, 'PersonNameInputSet:occupation-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversOccupation;

    public String getOccupation() {
        
        waitUntilElementIsVisible(editbox_GenericWorkorderPolicyPersonalAutoDriversOccupation);
        return editbox_GenericWorkorderPolicyPersonalAutoDriversOccupation.getAttribute("value");
    }

    public void setOccupation(String occupation) {
        
        clickWhenClickable(editbox_GenericWorkorderPolicyPersonalAutoDriversOccupation);
        editbox_GenericWorkorderPolicyPersonalAutoDriversOccupation.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_GenericWorkorderPolicyPersonalAutoDriversOccupation.sendKeys(String.valueOf(occupation));
        
    }

    private Guidewire8Select select_PersonRelationshipToInsured() {
        return new Guidewire8Select(driver, "//table[contains(@id,'PersonNameInputSet:RelationToInsured-triggerWrap')]");
    }

    public void selectRelationship(RelationshipToInsured relationship) {
        Guidewire8Select selectRelationship = select_PersonRelationshipToInsured();
        selectRelationship.selectByVisibleTextPartial(relationship.getValue());
    }

    private Guidewire8RadioButton radio_PhyisicalImpairmentOrEpilepsy() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, ':PolicyContactRoleNameInputSet:PolicyContactRoleInputSet:impairOrEpilep')]");
    }

    public void setPhysicalImpairmentOrEpilepsy(boolean radioValue) {
        Guidewire8RadioButton radio = radio_PhyisicalImpairmentOrEpilepsy();
        radio.select(radioValue);
    }

    private Guidewire8Checkbox checkBox_SR22() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id,'PolicyContactRoleInputSet:sr22')]");
    }

    public void setSR22Checkbox(boolean trueFalseChecked) {
        checkBox_SR22().select(trueFalseChecked);
    }

    private Guidewire8Select select_PersonCommute() {
        return new Guidewire8Select(driver, "//table[contains(@id,'FBM_PolicyContactDetailsDV:CommuteType-triggerWrap')]");
    }

    public void selectCommuteType(CommuteType commute) {
        Guidewire8Select selectCommute = select_PersonCommute();
        selectCommute.selectByVisibleTextPartial(commute.getValue());
    }


    public void fillOutPolicyDriverQuestions(Contact driver) {
        selectDriverHaveCurrentInsurance(driver.hasCurrentInsurance());
        if (driver.hasCurrentInsurance()) {
            setCurrentInsuranceCompanyPolicyNumber(driver.getCurrentInsuranceCompPolicy());
        } else {
            setHowLongWithoutCoverage(driver.getHowLongWithoutCoverageType());
        }
    }

    private Guidewire8Select select_PersonGender() {
        return new Guidewire8Select(driver, "//table[contains(@id,'PersonNameInputSet:Gender-triggerWrap')]");
    }

    public void selectGender(Gender gender) {
        Guidewire8Select selectGender = select_PersonGender();
        selectGender.selectByVisibleText(gender.toString());
    }


    private Guidewire8Select select_PersonMaritalStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id,'PersonNameInputSet:maritalStatus-triggerWrap')]");
    }

    public void selectMaritalStatus(MaritalStatus maritalStatus) {
        Guidewire8Select selectMaritalStatus = select_PersonMaritalStatus();
        selectMaritalStatus.selectByVisibleTextPartial(maritalStatus.getValue());
    }
    
    public MaritalStatus getSelectedMaritalStatus() {
    	return MaritalStatus.getEnumFromStringValue(select_PersonMaritalStatus().getText());
    }

    public List<String> getMaritalStatusValues() {
        return select_PersonMaritalStatus().getList();
    }


    private Guidewire8RadioButton radio_driverHaveCurrentInsurance() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, ':QuestionSetLV-body')]/div/table");
    }

    public void selectDriverHaveCurrentInsurance(boolean radioValue) {
        Guidewire8RadioButton radio = radio_driverHaveCurrentInsurance();
        radio.select(radioValue);
    }

    private Guidewire8Select select_DriverWithoutCoverage() {
        return new Guidewire8Select(driver, "//div[contains(@id,':QuestionSetLV-body')]/following-sibling::div[1]/table/tbody/tr/td[2]/table");
    }

    public void setHowLongWithoutCoverage(HowLongWithoutCoverage howlongWithoutCov) {
        clickWhenClickable(By.cssSelector("div[id$=':QuestionSetLV-body'] td.g-cell-edit"));
        
        Guidewire8Select selectNotRatedReason = select_DriverWithoutCoverage();
        selectNotRatedReason.selectByVisibleTextPartial(howlongWithoutCov.getValue());
    }

    public void setCurrentInsuranceCompanyPolicyNumber(String compAndPolicyNumber) {
        clickWhenClickable(find(By.xpath("//div[contains(text(), 'driver have current insurance')]/ancestor::tr[1]/following-sibling::tr[1]/child::td/div[contains(text(),'List the company and policy number')]/ancestor::td[1]/following-sibling::td/div")));
        WebElement textField = find(By.xpath("//div[contains(@id,':QuestionSetLV-body')]/following-sibling::div[1]/table/tbody/tr/td[2]/textarea"));
        textField.clear();
        textField.sendKeys(compAndPolicyNumber);
        clickProductLogo();
    }

    public boolean isSR22CheckboxChecked() {
        return checkBox_SR22().isSelected();
    }

    public String getLicenseNumber() {
        
        waitUntilElementIsVisible(editbox_GenericWorkorderPolicyPersonalAutoDriversLicense);
        return editbox_GenericWorkorderPolicyPersonalAutoDriversLicense.getAttribute("value");
    }


    private Guidewire8RadioButton radio_ExcludedDriver() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, ':PolicyContactRoleNameInputSet:PolicyContactRoleInputSet:excludedDriverButtonThingie-containerEl')]/table");
    }

    public void setExcludedDriverRadio(boolean yesno) {
        Guidewire8RadioButton radio = radio_ExcludedDriver();
        radio.select(yesno);
    }

    private Guidewire8Checkbox checkBox_NotRated() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id,'PolicyContactRoleInputSet:ExcludedDriver')]");
    }

    public void setNotRatedCheckbox(boolean trueFalseChecked) {
        checkBox_NotRated().select(trueFalseChecked);
    }

    private Guidewire8Select select_NotRatedReason() {
        return new Guidewire8Select(driver, "//table[contains(@id,'NotRatedReason-triggerWrap')]");
    }

    public void selectNotRatedReason(NotRatedReason reason) {
        Guidewire8Select selectNotRatedReason = select_NotRatedReason();
        selectNotRatedReason.selectByVisibleTextPartial(reason.getValue());
    }

    private Guidewire8Select select_SR22FilingFee() {
        return new Guidewire8Select(driver, "//table[contains(@id,'PolicyContactRoleInputSet:sr22FilingFee')]");
    }

    public void setSR22FilingFee(SR22FilingFee sr22FilingFee) {
        Guidewire8Select mySelect = select_SR22FilingFee();
        mySelect.selectByVisibleTextPartial(sr22FilingFee.getValue());
    }

    @FindBy(xpath = "//div[contains(@id, ':sr22FilingFee-inputEl')]")
    private WebElement text_SR22FilingFee;

    public List<String> getSR22FilingFee() {
        if (finds(By.xpath("")).isEmpty()) {
            ArrayList<String> returnList = new ArrayList<String>();
            returnList.add(text_SR22FilingFee.getText());
            return returnList;
        } else {
        Guidewire8Select mySelect = select_SR22FilingFee();
        return mySelect.getList();
    }
    }

    @FindBy(xpath = "//input[contains(@id, ':sr22effdate-inputEl')]")
    private WebElement editbox_SR22EffectiveDate;

    public void setSR22EffectiveDate(Date effectiveDate) {
        waitUntilElementIsClickable(editbox_SR22EffectiveDate);
        editbox_SR22EffectiveDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SR22EffectiveDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", effectiveDate));
    }

    @FindBy(xpath = "//input[contains(@id, ':sr22FilingChargeDate-inputEl')]")
    private WebElement editbox_SR22FilingChargeDate;

    public void setSR22FilingChargeDate(Date chargeDate) {
        waitUntilElementIsClickable(editbox_SR22FilingChargeDate);
        editbox_SR22FilingChargeDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SR22FilingChargeDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", chargeDate));
    }


    public boolean isSR22FilingFeeEditable() {
        try {
            if (!getSR22FilingFee().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    public boolean isSR22FilingChargeDateEditable() {
        return !finds(By.xpath("//input[contains(@id, ':sr22FilingChargeDate-inputEl')]")).isEmpty();
    }

    @FindBy(xpath = "//label[contains(@id,'PADriverPopup:') and contains(.,'Invalid Driver')]")
    private WebElement text_ValidDriversLicenseNumMessage;

    public boolean checkIfInvalidDriversLicenseMessageExists() {
        return checkIfElementExists(text_ValidDriversLicenseNumMessage, 5000);
    }


    @FindBy(xpath = "//div[contains(@id, 'PolicyContactRoleNameInputSet:PersonNameInputSet:RatedAge-inputEl')]")
    private WebElement editbox_RatedAge;
    @FindBy(xpath = "//input[contains(@id, 'PolicyContactRoleNameInputSet:PersonNameInputSet:RatedAge-inputEl')]")
    private WebElement editbox_RatedAgeEditable;

    public String getDriverRatedAge() {
        try {
            return editbox_RatedAge.getText();
        } catch (Exception e) {
            return editbox_RatedAgeEditable.getAttribute("value");
        }
    }

    public void setDriverRatedAge(String text) {
        setText(editbox_RatedAgeEditable, text);
    }

    @FindBy(xpath = "//input[contains(@id, ':PersonNameInputSet:PCRIdentificationInputSet:SSN-inputEl')]")
    private WebElement editbox_GenericWorkorderPolicyPersonalAutoDriversSSN;

    public void setSSN(String ssn) {
        
        clickWhenClickable(editbox_GenericWorkorderPolicyPersonalAutoDriversSSN);
        editbox_GenericWorkorderPolicyPersonalAutoDriversSSN.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_GenericWorkorderPolicyPersonalAutoDriversSSN.sendKeys(String.valueOf(ssn));
        
    }

    public boolean hasInvalidLicenseFormat() {
        boolean found = false;
        if (new GuidewireHelpers(getDriver()).errorMessagesExist()) {
            if (new GuidewireHelpers(getDriver()).equalsErrorMessage("License format for specified State, cannot order MVR")) {
                return true;
            }
        }
        return found;
    }


    @FindBy(xpath = "//div[contains(@id, 'PolicyContactRoleInputSet:sr22-inputEl')]")
    private WebElement editbox_SR22;
    
    @FindBy(xpath = "//input[contains(@id, ':DateOfBirth-input')]")
	private WebElement editbox_AutoDriversDOB;
    
    public void setAutoDriversDOB() {
		clickWhenClickable(editbox_AutoDriversDOB);
		editbox_AutoDriversDOB.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_AutoDriversDOB.sendKeys("01/01/1980");
	}
	
	public void setAutoDriversDOB(Date dob) {
		clickWhenClickable(editbox_AutoDriversDOB);
		editbox_AutoDriversDOB.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_AutoDriversDOB.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", dob));
	}
}



















