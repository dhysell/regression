package repository.ab.administration;

import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PhoneType;
import repository.gw.generate.ab.UserProfile;
import repository.gw.helpers.TableUtils;

import java.util.Map;

public class NewUser extends BasePage {
	
	private WebDriver driver;

    public NewUser(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[contains(@id,'NewUser:UserDetailScreen:UserDetailDV:FirstName-inputEl')]")
    private WebElement editbox_NewUserFirstName;

    @FindBy(xpath = "//input[contains(@id,'NewUser:UserDetailScreen:UserDetailDV:MiddleName-inputEl')]")
    private WebElement editbox_NewUserMiddleName;

    @FindBy(xpath = "//input[contains(@id,'NewUser:UserDetailScreen:UserDetailDV:LastName-inputEl')]")
    private WebElement editbox_NewUserLastName;

    @FindBy(xpath = "//input[contains(@id,'NewUser:UserDetailScreen:UserDetailDV:Username-inputEl')]")
    private WebElement editbox_NewUserUserName;

    @FindBy(xpath = "//input[contains(@id,'NewUser:UserDetailScreen:UserDetailDV:PasswordInputWidget-inputEl')]")
    private WebElement editbox_NewUserPassword;

    @FindBy(xpath = "//input[contains(@id,'NewUser:UserDetailScreen:UserDetailDV:ConfirmInputWidget-inputEl')]")
    private WebElement editbox_NewUserConfirmPassword;

    @FindBy(xpath = "//span[contains(@id,'NewUser:UserDetailScreen:UserDetailDV:UserRolesLV_tb:Add-btnEl')]")
    private WebElement editbox_NewUserRolesAdd;

    @FindBy(xpath = "//span[contains(@id,'NewUser:UserDetailScreen:UserDetailDV:UserGroupsLV_tb:Add-btnEl')]")
    private WebElement button_NewUserGroupsAdd;

    private WebElement treeExpander_BrowseGroups(String prefaceGroup) {
        return div_NewUserBrowseGroups.findElement(By.xpath(".//span[contains(.,'" + prefaceGroup + "')]/preceding-sibling::img[contains(@class, 'tree-expander')]"));
    }

    private WebElement getGroup(String group) {
        return div_NewUserBrowseGroups.findElement(By.xpath(".//span[contains(., '" + group + "')]"));
    }

    @FindBy(xpath = "//div[contains(@id,'OrganizationGroupTreePopup:OrganizationGroupTreePopupScreen:OrganizationGroupTreePopup:OrganizationGroupTreePopupPicker')]")
    private WebElement div_NewUserBrowseGroups;

    private void setGroups(String group) {
        clickGroupsAdd();
        
        clickWhenClickable(find(By.xpath("//span[contains(@id,'OrganizationGroupTreePopup:OrganizationGroupTreePopupScreen:ttlBar')]")));

        if (group.contains("IS")) {
            clickWhenClickable(treeExpander_BrowseGroups("IS Support"));
        } else {
            WebElement node = treeExpander_BrowseGroups("Farm Bureau Mutual Insurance Company of Idaho");
            waitUntilElementIsVisible(node);
            node.click();
        }
        clickWhenClickable(getGroup(group));
        
    }

    @FindBy(xpath = "//span[contains(@id,'NewUser:UserDetailScreen:ProfileCardTab-btnEl')]")
    private WebElement tab_NewUserProfile;

    @FindBy(xpath = "//span[contains(@id,'NewUser:UserDetailScreen:Update-btnEl')]")
    private WebElement button_NewUserUpdate;

    public void clickUpdate() {
        clickWhenClickable(button_NewUserUpdate);  
    }

    private void clickProfileTab() {
        clickWhenClickable(tab_NewUserProfile);
    }

    private void clickGroupsAdd() {
        clickWhenClickable(button_NewUserGroupsAdd);
    }

    private void setRolesPermission(String permission) {
        clickRolesAdd();
        new TableUtils(getDriver()).selectValueForSelectInTable(find(By.xpath("//div[contains(@id,'NewUser:UserDetailScreen:UserDetailDV:UserRolesLV')]")), new TableUtils(getDriver()).getNextAvailableLineInTable(find(By.xpath("//div[contains(@id,'NewUser:UserDetailScreen:UserDetailDV:UserRolesLV')]"))), "Name", permission);
    }

    private void clickRolesAdd() {
        clickWhenClickable(editbox_NewUserRolesAdd);
    }

    private void setConfirmPassword(String confirmedPassword) {
        
        clickWhenClickable(editbox_NewUserConfirmPassword);
        editbox_NewUserConfirmPassword.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserConfirmPassword.sendKeys(confirmedPassword);
        editbox_NewUserConfirmPassword.sendKeys(Keys.TAB);
        
    }

    private void setPassword(String password) {
        
        clickWhenClickable(editbox_NewUserPassword);
        editbox_NewUserPassword.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserPassword.sendKeys(password);
        editbox_NewUserPassword.sendKeys(Keys.TAB);
        
    }

    private void setUserName(String userName) {
        
        clickWhenClickable(editbox_NewUserUserName);
        editbox_NewUserUserName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserUserName.sendKeys(userName);
        editbox_NewUserUserName.sendKeys(Keys.TAB);
        
    }

    private void setLastName(String lastName) {
        
        clickWhenClickable(editbox_NewUserLastName);
        editbox_NewUserLastName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserLastName.sendKeys(lastName);
        editbox_NewUserLastName.sendKeys(Keys.TAB);
        
    }

    private void setMiddleName(String middleName) {
        
        if (!(middleName == null || middleName.equals("") || middleName.isEmpty())) {
            clickWhenClickable(editbox_NewUserMiddleName);
            editbox_NewUserMiddleName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
            editbox_NewUserMiddleName.sendKeys(middleName);
            editbox_NewUserMiddleName.sendKeys(Keys.TAB);
            
        }
    }

    private void setFirstName(String firstName) {
        
        clickWhenClickable(editbox_NewUserFirstName);
        editbox_NewUserFirstName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserFirstName.sendKeys(firstName);
        editbox_NewUserFirstName.sendKeys(Keys.TAB);
        
    }

    //Profile Tab
    @FindBy(xpath = "//input[contains(@id,'NewUser:UserDetailScreen:UserProfileDV:UserPreferencesContactInputSet:JobTitle-inputEl')]")
    private WebElement editbox_NewUserProfileJobTitle;

    private void setJobTitle(String title) {
        
        clickWhenClickable(editbox_NewUserProfileJobTitle);
        editbox_NewUserProfileJobTitle.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserProfileJobTitle.sendKeys(title);
        editbox_NewUserProfileJobTitle.sendKeys(Keys.TAB);
        
    }

    @FindBy(xpath = "//input[contains(@id,'NewUser:UserDetailScreen:UserProfileDV:UserPreferencesContactInputSet:Department-inputEl')]")
    private WebElement editbox_NewUserProfileDepartment;

    private void setDepartment(String department) {
        
        clickWhenClickable(editbox_NewUserProfileDepartment);
        editbox_NewUserProfileDepartment.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserProfileDepartment.sendKeys(department);
        editbox_NewUserProfileDepartment.sendKeys(Keys.TAB);
        
    }

    @FindBy(xpath = "//input[contains(@id,'NewUser:UserDetailScreen:UserProfileDV:UserPreferencesContactInputSet:UserAddress:GlobalAddressInputSet:PostalCode-inputEl')]")
    private WebElement editbox_NewUserProfileZipCode;

    private void setZipCode(String zipCode) {
        
        clickWhenClickable(editbox_NewUserProfileZipCode);
        editbox_NewUserProfileZipCode.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserProfileZipCode.sendKeys(zipCode);
        editbox_NewUserProfileZipCode.sendKeys(Keys.TAB);
        
    }

    @FindBy(xpath = "//input[contains(@id,'NewUser:UserDetailScreen:UserProfileDV:UserPreferencesContactInputSet:UserAddress:GlobalAddressInputSet:AddressLine1-inputEl')]")
    private WebElement editbox_NewUserProfileAddressLineOne;

    private void setAddressLineOne(String lineOne) {
        
        clickWhenClickable(editbox_NewUserProfileAddressLineOne);
        editbox_NewUserProfileAddressLineOne.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserProfileAddressLineOne.sendKeys(lineOne);
        editbox_NewUserProfileAddressLineOne.sendKeys(Keys.TAB);
        
    }

    @FindBy(xpath = "//input[contains(@id,'NewUser:UserDetailScreen:UserProfileDV:UserPreferencesContactInputSet:UserAddress:GlobalAddressInputSet:City-inputEl')]")
    private WebElement editbox_NewUserProfileCity;

    private void setCity(String city) {
        
        clickWhenClickable(editbox_NewUserProfileCity);
        editbox_NewUserProfileCity.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserProfileCity.sendKeys(city);
        editbox_NewUserProfileCity.sendKeys(Keys.TAB);
        
    }

    private Guidewire8Select select_NewUsersState() {
        return new Guidewire8Select(driver, "//table[contains(@id,'NewUser:UserDetailScreen:UserProfileDV:UserPreferencesContactInputSet:UserAddress:GlobalAddressInputSet:State-triggerWrap')]");
    }

    private void select_State(State state) {
        select_NewUsersState().selectByVisibleTextPartial(state.getName());

    }

    private void setNewUserCity(String city) {
        
        clickWhenClickable(editbox_NewUserProfileCity);
        editbox_NewUserProfileCity.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserProfileCity.sendKeys(city);
        editbox_NewUserProfileCity.sendKeys(Keys.TAB);
        
    }

    @FindBy(xpath = "//input[contains(@id,':UserHomePhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    private WebElement editbox_NewUserProfileHomePhone;

    private void setHomePhone(String phone) {
        
        clickWhenClickable(editbox_NewUserProfileHomePhone);
        editbox_NewUserProfileHomePhone.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserProfileHomePhone.sendKeys(phone);
        editbox_NewUserProfileHomePhone.sendKeys(Keys.TAB);
        
    }

    @FindBy(xpath = "//input[contains(@id,':UserFaxPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    private WebElement editbox_NewUserProfileFaxPhone;

    private void setFaxPhone(String phone) {
        
        clickWhenClickable(editbox_NewUserProfileFaxPhone);
        editbox_NewUserProfileFaxPhone.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserProfileFaxPhone.sendKeys(phone);
        editbox_NewUserProfileFaxPhone.sendKeys(Keys.TAB);
        
    }

    @FindBy(xpath = "//input[contains(@id,':UserCellPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    private WebElement editbox_NewUserProfileMobilePhone;

    private void setMobilePhone(String phone) {
        
        clickWhenClickable(editbox_NewUserProfileMobilePhone);
        editbox_NewUserProfileMobilePhone.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserProfileMobilePhone.sendKeys(phone);
        editbox_NewUserProfileMobilePhone.sendKeys(Keys.TAB);
        
    }

    @FindBy(xpath = "//input[contains(@id,':UserWorkPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
    private WebElement editbox_NewUserProfileWorkPhone;

    private void setWorkPhone(String phone) {
        
        clickWhenClickable(editbox_NewUserProfileWorkPhone);
        editbox_NewUserProfileWorkPhone.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserProfileWorkPhone.sendKeys(phone);
        editbox_NewUserProfileWorkPhone.sendKeys(Keys.TAB);
        
    }

    @FindBy(xpath = "//input[contains(@id,'NewUser:UserDetailScreen:UserProfileDV:UserPreferencesContactInputSet:Email-inputEl')]")
    private WebElement editbox_NewUserProfileEmail;

    private void setEmail(String email) {
        
        clickWhenClickable(editbox_NewUserProfileEmail);
        editbox_NewUserProfileEmail.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_NewUserProfileEmail.sendKeys(email);
        editbox_NewUserProfileEmail.sendKeys(Keys.TAB);
        
    }

    private void setPhoneNumbers(Map<String, PhoneType> phoneNumbers) {

        for (Map.Entry<String, PhoneType> entry : phoneNumbers.entrySet()) {
            if (entry.getValue() == PhoneType.Business) {
                System.out.println("There is no business phone on the User entry page.  Only work phone.");
            } else if (entry.getValue() == PhoneType.Home) {
                setFaxPhone(entry.getKey());
            } else if (entry.getValue() == PhoneType.Mobile) {
                setMobilePhone(entry.getKey());
            } else if (entry.getValue() == PhoneType.Work) {
                setWorkPhone(entry.getKey());
            } else {
                System.out.println("You really should have a type for each phone number in this list.");
            }
        }
    }

    public void inputNewUser(UserProfile user) {
        setFirstName(user.getFirstName());
        setMiddleName(user.getMiddleName());
        setLastName(user.getLastName());
        setUserName(user.getUserName());
        setPassword(user.getPassword());
        setConfirmPassword(user.getPassword());
        for (String role : user.getRoles()) {
            setRolesPermission(role);
        }
        for (String group : user.getGroup()) {
            setGroups(group);
        }
        clickProfileTab();
        setJobTitle(user.getJobTitle());
        setDepartment(user.getDepartment());
        setZipCode(user.getAddressInfo().getZip());
        setAddressLineOne(user.getAddressInfo().getLine1());
        setCity(user.getAddressInfo().getCity());
        select_State(user.getAddressInfo().getState());
        setEmail(user.getEmail());
        setPhoneNumbers(user.getPhoneNumbers());
        clickUpdate();
    }

}
