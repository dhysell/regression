package repository.pc.workorders.submission;

import java.util.Date;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.idfbins.enums.State;

import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PersonSuffix;
import repository.gw.enums.PhoneType;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.pc.workorders.generic.GenericWorkorder;

public class SubmissionCreateAccount extends GenericWorkorder {

    private WebDriver driver;

    public SubmissionCreateAccount(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    @FindBy(xpath = "//label[contains(@id, 'CreateAccount:CreateAccountScreen:0')]")
    private WebElement text_SubmissionCreateAccountBasicsConfirmAccountInfo;
    
    @FindBy(xpath = "//input[contains(@id, 'FirstName-inputEl')]")
    private WebElement editbox_SubmissionCreateAccountBasicsFirstName;
    
    @FindBy(xpath = "//input[contains(@id, ':MiddleName-inputEl')]")
    private WebElement editbox_SubmissionCreateAccountBasicsMiddleName;
    
    @FindBy(xpath = "//input[contains(@id, ':LastName-inputEl')]")
    private WebElement editbox_SubmissionCreateAccountBasicsLastName;
    
    @FindBy(xpath = "//input[contains(@id, ':Name-inputEl')]")
    private WebElement editbox_SubmissionCreateAccountBasicsCompanyName;

    @FindBy(xpath = "//input[(@id='CreateAccount:CreateAccountScreen:CreateAccountContactInputSet:SSN-inputEl') or contains(@id, 'FBNewSubmission:ContactSearchScreen:SSN-inputEl') or contains(@id, ':SSN-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsSSN;

    @FindBy(xpath = "//span[contains(@id,'CreateAccount:CreateAccountScreen:AddressTabTab-btnEl')]")
    public WebElement link_SubmissionCreateAccountBasicsAddresses;

    @FindBy(xpath = "//input[(@id='CreateAccount:CreateAccountScreen:CreateAccountContactInputSet:TIN-inputEl') or contains(@id, ':TIN-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsTIN;

    @FindBy(xpath = "//input[contains(@id, ':Input-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsAltID;

    @FindBy(xpath = "//input[@id='CreateAccount:CreateAccountScreen:AddressInputSet:OfficeNumber']")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryAddressOfficeNumber;

    @FindBy(xpath = "//input[contains(@id, ':GlobalAddressInputSet:AddressLine1-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryAddressAddressLine1;

    @FindBy(xpath = "//input[contains(@id, ':GlobalAddressInputSet:AddressLine2-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryAddressAddressLine2;

    @FindBy(xpath = "//input[contains(@id, ':GlobalAddressInputSet:City-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryAddressCity;

    public Guidewire8Select select_SubmissionCreateAccountBasicsPrimaryAddressState() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':GlobalAddressInputSet:State-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':GlobalAddressInputSet:PostalCode-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryAddressZipCode;

    @FindBy(xpath = "//input[contains(@id, ':GlobalAddressInputSet:County-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryAddressCounty;

    public Guidewire8Select select_SubmissionCreateAccountBasicsPrimaryAddressAddressType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':AddressType-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id,':AddressInputSet:Description-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryAddressDescription;

    @FindBy(xpath = "//input[contains(@id, ':EmailAddress1-inputEl') or contains(@id, ':Primary-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryAddressEmailMain;

    @FindBy(xpath = "//input[@id='CreateAccount:CreateAccountScreen:CreateAccountContactInputSet:EmailAddress2']")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryAddressEmailAlternate;

    @FindBy(xpath = "//a[contains(@id,':ForceDupCheckUpdate')]")
    public WebElement button_SubmissionCreateAccountUpdate;

    @FindBy(xpath = "//a[@id='CreateAccount:CreateAccountScreen:Cancel']")
    public WebElement button_SubmissionCreateAccountCancel;

    @FindBy(xpath = "//a[@id='CreateAccount:CreateAccountScreen:CheckForDuplicates']")
    public WebElement button_SubmissionCreateAccountCheckForDuplicates;

    @FindBy(xpath = "//span[@id='AddressStandardizationPopup:LocationScreen:WarningNotFound']")
    public WebElement label_SubmissionCreateAccountAddressNotFound;

    @FindBy(xpath = "//input[contains(@id,':AddressInputSet:BusinessPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryBusinessPhone;

    @FindBy(xpath = "//input[contains(@id,':WorkPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryWorkPhone;

    @FindBy(xpath = "//input[contains(@id,':AddressInputSet:HomePhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryHomePhone;

    @FindBy(xpath = "//input[contains(@id,':AddressInputSet:MobilePhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryMobilePhone;

    @FindBy(xpath = "//input[contains(@id,':AddressInputSet:FaxPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryFaxPhone;

    public Guidewire8Select select_SubmissionCreateAccountBasicsPrimaryAddressPrimaryPhone() {
        return new Guidewire8Select(driver, "//table[contains(@id,':AddressInputSet:primaryPhone-triggerWrap')]");
    }

    @FindBy(xpath = "//input[@id='CreateAccount:CreateAccountScreen:CreateAccountContactInputSet:GlobalPersonNameInputSet:MiddleName-inputEl']")
    public WebElement editbox_SubmissionMiddleName;

    @FindBy(xpath = "//input[@id='CreateAccount:CreateAccountScreen:CreateAccountContactInputSet:FormerLastName-inputEl']")
    public WebElement editbox_SubmissionFormerLastName;

    @FindBy(xpath = "//input[@id='CreateAccount:CreateAccountScreen:CreateAccountContactInputSet:AlternateName-inputEl']")
    public WebElement editbox_SubmissionAlternateName;

    @FindBy(xpath = "//a[@id= 'DuplicateContactsPopup:__crumb__']")
    private WebElement link_ReturnToCreateAccount;
    
    public String getConfirmAccountInfoText() {
    	waitForPageLoad();
    	return text_SubmissionCreateAccountBasicsConfirmAccountInfo.getText();
    }
    
    private void setFirstName(String firstName) {
    	setText(editbox_SubmissionCreateAccountBasicsFirstName, firstName);
    }
    
    private void setLastName(String lastName) {
    	setText(editbox_SubmissionCreateAccountBasicsLastName, lastName);
    }
    
    private void setCompanyName(String compName) {
    	setText(editbox_SubmissionCreateAccountBasicsCompanyName, compName);
    }
    
    public Guidewire8Select select_SubmissionSuffixName() {
        return new Guidewire8Select(driver, "//table[contains( @id, ':Suffix-triggerWrap')]");
    }


    public void clickAddresses() {
        clickWhenClickable(link_SubmissionCreateAccountBasicsAddresses);
    }


    public void setSubmissionCreateAccountBasicsSSN(String ssn) {
    	setText(editbox_SubmissionCreateAccountBasicsSSN, ssn);
    }


    public void setSubmissionCreateAccountBasicsTIN(String tin) {
    	setText(editbox_SubmissionCreateAccountBasicsTIN, tin);
    }


    public void setSubmissionCreateAccountBasicsAltID(String altID) {
    	setText(editbox_SubmissionCreateAccountBasicsAltID, altID);
    }


    public void setSubmissionCreateAccountBasicsPrimaryAddressOfficeNumber(String officeNumber) {
    	setText(editbox_SubmissionCreateAccountBasicsPrimaryAddressOfficeNumber, officeNumber);
    }

    //Edited AddressLine1, city, state, Zip, Address Type to use Common - Steve Broderick


    public void setSubmissionCreateAccountBasicsPrimaryAddressAddressLine1(String addressLine) {
//		waitUntilElementIsVisible(editbox_SubmissionCreateAccountBasicsPrimaryAddressAddressLine1);
        setText(editbox_SubmissionCreateAccountBasicsPrimaryAddressAddressLine1, addressLine);
//		editbox_SubmissionCreateAccountBasicsPrimaryAddressAddressLine1.sendKeys(addressLine);
    }


    public void setSubmissionCreateAccountBasicsPrimaryAddressAddressLine2(String addressLine) {
    	setText(editbox_SubmissionCreateAccountBasicsPrimaryAddressAddressLine1, addressLine);
    }

    //Edited AddressLine1, city, state, Zip, Address Type to use Common - Steve Broderick

    public void setSubmissionCreateAccountBasicsPrimaryAddressCity(String city) {
        setText(editbox_SubmissionCreateAccountBasicsPrimaryAddressCity, city);
    }

    //Edited AddressLine1, city, state, Zip, Address Type to use Common - Steve Broderick

    public void setSubmissionCreateAccountBasicsPrimaryAddressState(String state) {
        Guidewire8Select mySelect = select_SubmissionCreateAccountBasicsPrimaryAddressState();
        mySelect.selectByVisibleText(state);
    }
    
    public String getSubmissionCreateAccountBasicsPrimaryAddressState() {
    	waitForPageLoad();
    	Guidewire8Select mySelect = select_SubmissionCreateAccountBasicsPrimaryAddressState();
        return mySelect.getText();
    }

    //Edited AddressLine1, city, state, Zip, Address Type to use Common - Steve Broderick

    public void setSubmissionCreateAccountBasicsPrimaryAddressZipCode(String zipCode) {
        setText(editbox_SubmissionCreateAccountBasicsPrimaryAddressZipCode, zipCode);
    }


    public void setSubmissionCreateAccountBasicsPrimaryAddressCounty(String county) {
        // jon lasen county not settable in DEV2 or IT2
        if (!finds(By.xpath("//input[@id='CreateAccount:CreateAccountScreen:AddressInputSet:globalAddressContainer:GlobalAddressInputSet:County-inputEl']")).isEmpty()) {
        	setText(editbox_SubmissionCreateAccountBasicsPrimaryAddressCounty, county);
        }
    }

    public void setSubmissionCreateAccountBasicsPrimaryAddressDescription(String description) {
    	setText(editbox_SubmissionCreateAccountBasicsPrimaryAddressDescription, description);
    }

    //Edited AddressLine1, city, state, Zip, Address Type to use Common - Steve Broderick

    public void setSubmissionCreateAccountBasicsPrimaryAddressAddressType(String addressType) {
        Guidewire8Select mySelect = select_SubmissionCreateAccountBasicsPrimaryAddressAddressType();
        mySelect.selectByVisibleText(addressType);
    }
    
    public String getSubmissionCreateAccountBasicsPrimaryAddressAddressType() {
    	waitForPageLoad();
        return select_SubmissionCreateAccountBasicsPrimaryAddressAddressType().getText();
        
    }


    public void setSubmissionCreateAccountBasicsPrimaryAddressBusinessPhone(String businessPhone) {
        waitUntilElementIsVisible(editbox_SubmissionCreateAccountBasicsPrimaryBusinessPhone);
        setText(editbox_SubmissionCreateAccountBasicsPrimaryBusinessPhone, businessPhone);
        if (editbox_SubmissionCreateAccountBasicsPrimaryBusinessPhone.getAttribute("value").isEmpty()) {
        	setText(editbox_SubmissionCreateAccountBasicsPrimaryBusinessPhone, businessPhone);
            clickWhenClickable(editbox_SubmissionCreateAccountBasicsPrimaryWorkPhone);
        }
        waitForPostBack();
        Guidewire8Select mySelect = select_SubmissionCreateAccountBasicsPrimaryAddressPrimaryPhone();
        mySelect.selectByVisibleText(PhoneType.Business.getValue());
    }


    public void setBusinessphone(String phone) {
    	setText(editbox_SubmissionCreateAccountBasicsPrimaryBusinessPhone, phone);
    }


    public void setWorkPhone(String phone) {
    	setText(editbox_SubmissionCreateAccountBasicsPrimaryWorkPhone, phone);
    }


    public void setMobilePhone(String phone) {
    	setText(editbox_SubmissionCreateAccountBasicsPrimaryMobilePhone, phone);
    }


    public void setHomePhone(String phone) {
    	setText(editbox_SubmissionCreateAccountBasicsPrimaryHomePhone, phone);
    }


    public void setFaxPhone(String phone) {
    	setText(editbox_SubmissionCreateAccountBasicsPrimaryFaxPhone, phone);
    }


    public void clickSubmissionCreateAccountUpdate() {
        long end = new Date().getTime() + 10000;
        do {
            try {
                clickWhenClickable(button_SubmissionCreateAccountUpdate);
                if (new GuidewireHelpers(driver).overrideAddressStandardization()) {
                	clickWhenClickable(button_SubmissionCreateAccountUpdate);
                }
            } catch (Exception e) {
                clickUpdate();
            }
            try {
                clickWhenClickable(link_ReturnToCreateAccount);
                clickWhenClickable(button_SubmissionCreateAccountUpdate);
            } catch (Exception e) {

            }
        }
        while (finds(By.xpath("//a[@id='CreateAccount:CreateAccountScreen:ForceDupCheckUpdate']")).size() > 0 && new Date().getTime() < end);
    }


    public void clickCreateAccountUpdate() {

        try {
            clickWhenClickable(button_SubmissionCreateAccountUpdate);
        } catch (Exception e) {
            clickUpdate();
        }
    }


    public WebElement getSubmissionCreateAccountAddressNotFound() throws Exception {
        if (finds(By.xpath("//span[@id='AddressStandardizationPopup:LocationScreen:WarningNotFound']"))
                .size() > 0) {
            return label_SubmissionCreateAccountAddressNotFound;
        } else {
            throw new Exception("The 'Address not found' label was not present on the Create Account Page");
        }

    }


    public void fillOutPrimaryAddressFields(AddressInfo insPrimaryAddressFields) {

//		setSubmissionCreateAccountBasicsPrimaryAddressCountry(insPrimaryAddressFields.getCountry());
        setSubmissionCreateAccountBasicsPrimaryAddressCity(insPrimaryAddressFields.getCity());
        setSubmissionCreateAccountBasicsPrimaryAddressCounty(insPrimaryAddressFields.getCounty());

        setSubmissionCreateAccountBasicsPrimaryAddressAddressLine1(insPrimaryAddressFields.getLine1());
        if (insPrimaryAddressFields.getLine2() != null) {
            setSubmissionCreateAccountBasicsPrimaryAddressAddressLine2(insPrimaryAddressFields.getLine2());
        }
        setSubmissionCreateAccountBasicsPrimaryAddressAddressType(insPrimaryAddressFields.getType().getValue());

        setSubmissionCreateAccountBasicsPrimaryAddressState(insPrimaryAddressFields.getState().getName());
        setSubmissionCreateAccountBasicsPrimaryAddressZipCode(insPrimaryAddressFields.getZip());

        setSubmissionCreateAccountBasicsPrimaryAddressEmailMain(insPrimaryAddressFields.getEmailAddress());
        // setSubmissionCreateAccountBasicsPrimaryAddressBusinessPhone(insPrimaryAddressFields.getPhoneBusiness().toString());

        if (insPrimaryAddressFields.getPhonePrimary() != null) {
            setSubmissionCreateAccountBasicsPrimaryAddressBusinessPhone(insPrimaryAddressFields.getPhoneBusiness());
            editbox_SubmissionCreateAccountBasicsPrimaryAddressDescription.click();
            if (insPrimaryAddressFields.getPhoneFax() != null) {
                setFaxPhone(insPrimaryAddressFields.getPhoneFax());
            }
            if (insPrimaryAddressFields.getPhoneHome() != null) {
                setHomePhone(insPrimaryAddressFields.getPhoneHome());
            }
            if (insPrimaryAddressFields.getPhoneWork() != null) {
                setWorkPhone(insPrimaryAddressFields.getPhoneWork());
            }
            if (insPrimaryAddressFields.getPhoneMobile() != null) {
                setMobilePhone(insPrimaryAddressFields.getPhoneMobile());
            }
        }

    }


    public void updateAddressStandardization() {
        waitUntilElementIsVisible(super.button_GenericWorkorderOverride);
        clickWhenClickable(super.button_GenericWorkorderOverride);
        try {
            clickSubmissionCreateAccountUpdate();
        } catch (Exception e) {
            driver.navigate().refresh();
            sleep(1); //Used to ensure that the refresh has completed fully.
            driver.switchTo().alert().accept();
            sleep(1); //Used to wait for the alert to clear fully.
            clickSubmissionCreateAccountUpdate();
        }

    }


    public void setSubmissionCreateAccountBasicsPrimaryAddressEmailMain(InternetAddress emailAddressMain) {
    	setText(editbox_SubmissionCreateAccountBasicsPrimaryAddressEmailMain, emailAddressMain.toString());
    }


    public void setSubmissionCreateAccountBasicsPrimaryAddressEmailAlternate(InternetAddress emailAddressAlternate) {
    	setText(editbox_SubmissionCreateAccountBasicsPrimaryAddressEmailAlternate, emailAddressAlternate.toString());
    }


    public void setPersonMiddleName(String middleName) {
    	setText(editbox_SubmissionMiddleName, middleName);
    }


    public void setPersonAlternateName(String altName) {
    	setText(editbox_SubmissionAlternateName, altName);
    }


    public void setPersonFormerName(String formerName) {
    	setText(editbox_SubmissionFormerLastName, formerName);
    }


    public void setPersonSuffixName(PersonSuffix suffixName) {
        Guidewire8Select mySelect = select_SubmissionSuffixName();
        mySelect.selectByVisibleText(suffixName.getValue());
    }

    //This assumes no address has been added.

    public void setAddressOnAddressDetails(List<AddressInfo> addresses) {
        for (int i = 0; i < addresses.size(); i++) {
            setSubmissionCreateAccountBasicsPrimaryAddressAddressLine1(addresses.get(i).getLine1());
            setSubmissionCreateAccountBasicsPrimaryAddressCity(addresses.get(i).getCity());
            setSubmissionCreateAccountBasicsPrimaryAddressState(addresses.get(i).getState().getName());
            setSubmissionCreateAccountBasicsPrimaryAddressZipCode(addresses.get(i).getZip());
            setSubmissionCreateAccountBasicsPrimaryAddressAddressType(addresses.get(i).getType().getValue());
            if (i != addresses.size() - 1) {
                clickAdd();
            }
        }
    }

    @FindBy(xpath = "//div[contains(@id, 'DuplicateContactsPopup:DuplicateContactsScreen:ResultsLV')]")
    public WebElement table_MatchingContacts;


    public void clickSubmissionCreateAccountSelect(int row) {
        new TableUtils(getDriver()).clickLinkInSpecficRowInTable(table_MatchingContacts, row);
    }

    private Guidewire8Select select_AgentNumber() {
        return new Guidewire8Select(getDriver(), "//table[contains(@id, ':ProducerCode-triggerWrap')]");
    }
    public void setAgentNumber() {
        Guidewire8Select mySelect = select_AgentNumber();
        mySelect.selectByVisibleTextPartial("Bill Terry (549)");
    }
    
    public void createAccount(GeneratePolicy policy) {
        if(policy.nonAgent || policy.altUser) {
            setAgentNumber();
        }

        createNewContact(policy.pniContact);
        //skandibanda - to handle lexisnexis data duplicates
        if (policy.lexisNexisData && new ErrorHandlingHelpers(driver).getErrorMessage().contains("A contact with the specified SSN/TIN already exists in ContactManager.")) {
            clickSubmissionCreateAccountSelect(1);
            clickCreateAccountUpdate();
        }
    }
    
    @FindBy(xpath = "//input[contains(@id, ':DateOfBirth-input')]")
    private WebElement editbox_DOB;

    public void setDOB(String dateToSet) {
    	clickWhenClickable(editbox_DOB);
    	editbox_DOB.sendKeys(Keys.chord(Keys.CONTROL + "a"), dateToSet);
    	clickProductLogo();
    }
    
    public void setDOB(Date dateToSet) {
    	setDOB(DateUtils.dateFormatAsString("MM/dd/yyyy", dateToSet));
    }
    
    public void createNewContact(Contact newContact) {
    	waitForPageLoad();
        if(checkIfElementExists(editbox_SubmissionCreateAccountBasicsFirstName, 3000)) {
        	 setFirstName(newContact.getFirstName());
        	 setLastName(newContact.getLastName());
        } else {
        	setCompanyName(newContact.getCompanyName());
        }
    	
    	if (newContact.getMiddleName() != null && !newContact.getMiddleName().isEmpty()) {
            setPersonMiddleName(newContact.getMiddleName());
        }//END IF

        if (newContact.getInsSuffixName() != PersonSuffix.None && !newContact.getInsSuffixName().equals(PersonSuffix.None)) {
            setPersonSuffixName(newContact.getInsSuffixName());
        }//END IF
        // jlarsen 10/13/2015
        // DOB is now a required field when creating an account
        if (newContact.isPerson()) {
            if (newContact.getDob() == null) {
            	setDOB("01/01/1980");
            } else {
            	setDOB(DateUtils.dateFormatAsString("MM/dd/yyyy", newContact.getDob()));
            }//END ELSE

        }//END IF
        if (newContact.getAlternateID() != null) {
            setSubmissionCreateAccountBasicsAltID(newContact.getAlternateID());
        }

        if (newContact.getTaxIDNumber() != null && newContact.getTaxIDNumber().length() > 8) {
            setSSNTIN(newContact);
        }

        if (newContact.getSocialSecurityNumber() != null && newContact.getSocialSecurityNumber().length() > 8) {
            setSSNTIN(newContact);
        }

        fillOutPrimaryAddressFields((newContact.getAddressList().isEmpty()) ? newContact.getAddress() : newContact.getAddressList().get(0));



        clickSubmissionCreateAccountUpdate();

        GuidewireHelpers gwHelpers = new GuidewireHelpers(driver);
        if (gwHelpers.errorMessagesExist()) {
            if (gwHelpers.getFirstErrorMessage().contains("A contact with the specified TIN already exists")) {
                clickSubmissionCreateAccountUpdate();
            } else if (gwHelpers.getFirstErrorMessage().contains("At least one of the following fields has to be filled out: SSN, TIN, or Alternate ID")) {
            	if (newContact.getTaxIDNumber() != null && newContact.getTaxIDNumber().length() > 8) {
                    setSSNTIN(newContact);
                }

                if (newContact.getSocialSecurityNumber() != null && newContact.getSocialSecurityNumber().length() > 8) {
                    setSSNTIN(newContact);
                }
            	clickSubmissionCreateAccountUpdate();
            } else {
            	if(checkIfElementExists("//span[contains(@id, 'CreateAccount:CreateAccountScreen:ttlBar')]", 2)) {
            		Assert.fail("There was an unhandled error while filling out the create account page. Please see the screenshot for more information.");
            	}
            }//END IF
        }//END IF
    }
        
    private void setSSNTIN(Contact newContact) {
        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
        if (newContact.isPerson()) {
            if (newContact.getSocialSecurityNumber() != null) {
                createAccountPage.setSubmissionCreateAccountBasicsSSN(newContact.getSocialSecurityNumber());
            } else if (newContact.getTaxIDNumber() != null) {
                createAccountPage.setSubmissionCreateAccountBasicsTIN(newContact.getTaxIDNumber());
            }
        }

        if (newContact.isCompany()) {
            if (newContact.getTaxIDNumber() != null) {
                createAccountPage.setSubmissionCreateAccountBasicsTIN(newContact.getTaxIDNumber());
            } else if (newContact.getSocialSecurityNumber() != null) {
                createAccountPage.setSubmissionCreateAccountBasicsSSN(newContact.getSocialSecurityNumber());
            }
        }
        
//		if (policy.pniContact.getSocialSecurityNumber() != null && policy.pniContact.isPerson()) {
//			createAccountPage.setSubmissionCreateAccountBasicsSSN(policy.pniContact.getSocialSecurityNumber());
//		}

//		if (policy.pniContact.getTaxIDNumber() != null && policy.pniContact.isCompany()) {
//			createAccountPage.setSubmissionCreateAccountBasicsTIN(policy.pniContact.getTaxIDNumber());
//		}
    }
    
    public boolean namePrefilled(String firstName, String middleName, String lastName) {
    	if(firstName.contains(editbox_SubmissionCreateAccountBasicsFirstName.getText()) && lastName.contains(editbox_SubmissionCreateAccountBasicsLastName.getText())) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean addressPrefilled(String street, String city, State state, String zip) {
    	waitForPageLoad();
    	if(street.contains(editbox_SubmissionCreateAccountBasicsPrimaryAddressAddressLine1.getText()) && city.contains(editbox_SubmissionCreateAccountBasicsPrimaryAddressCity.getText()) && state.getName().contains(getSubmissionCreateAccountBasicsPrimaryAddressState()) && zip.contains(editbox_SubmissionCreateAccountBasicsPrimaryAddressZipCode.getText())) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean companyNamePrefilled(String companyName) {
    	if(companyName.contains(editbox_SubmissionCreateAccountBasicsCompanyName.getText())){
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean ssnPrefilled(String ssn) {
    	if(ssn.contains(editbox_SubmissionCreateAccountBasicsSSN.getText().trim())){
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean tinPrefilled(String tin) {
    	if(tin.contains(editbox_SubmissionCreateAccountBasicsTIN.getText().trim())){
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean companyPhonePrefilled(String businessPhone) {
    	if(businessPhone.contains(editbox_SubmissionCreateAccountBasicsPrimaryBusinessPhone.getText())){
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean personMobilePhonePrefilled(String mobilePhone) {
    	if(mobilePhone.contains(editbox_SubmissionCreateAccountBasicsPrimaryMobilePhone.getText())){
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /*
     * 

    @FindBy(xpath = "//input[(@id='CreateAccount:CreateAccountScreen:CreateAccountContactInputSet:SSN-inputEl') or contains(@id, 'FBNewSubmission:ContactSearchScreen:SSN-inputEl') or contains(@id, ':SSN-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsSSN;

    @FindBy(xpath = "//span[contains(@id,'CreateAccount:CreateAccountScreen:AddressTabTab-btnEl')]")
    public WebElement link_SubmissionCreateAccountBasicsAddresses;

    @FindBy(xpath = "//input[(@id='CreateAccount:CreateAccountScreen:CreateAccountContactInputSet:TIN-inputEl') or contains(@id, ':TIN-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsTIN;

    @FindBy(xpath = "//input[contains(@id, ':Input-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsAltID;

    @FindBy(xpath = "//input[@id='CreateAccount:CreateAccountScreen:AddressInputSet:OfficeNumber']")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryAddressOfficeNumber;

    @FindBy(xpath = "//input[contains(@id, ':GlobalAddressInputSet:AddressLine1-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryAddressAddressLine1;

    @FindBy(xpath = "//input[contains(@id, ':GlobalAddressInputSet:AddressLine2-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryAddressAddressLine2;

    @FindBy(xpath = "//input[contains(@id, ':GlobalAddressInputSet:City-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryAddressCity;

    public Guidewire8Select select_SubmissionCreateAccountBasicsPrimaryAddressState() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':GlobalAddressInputSet:State-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':GlobalAddressInputSet:PostalCode-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryAddressZipCode;

    @FindBy(xpath = "//input[contains(@id, ':GlobalAddressInputSet:County-inputEl')]")
    public WebElement editbox_SubmissionCreateAccountBasicsPrimaryAddressCounty;

     * 
     * 
     * 
     * 
     * */
}