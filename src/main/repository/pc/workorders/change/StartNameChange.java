package repository.pc.workorders.change;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactTransferAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.gw.activity.ActivityPopup;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ContactRole;
import repository.gw.enums.GenerateContactType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.generic.GenericWorkorder;

import java.util.ArrayList;


public class StartNameChange extends GenericWorkorder {

    private WebDriver driver;

    public StartNameChange(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private GenerateContactType contactType = null;
    private GenerateContact myContactObj;
    private ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
    private GeneratePolicy myPolicyObj;

    @FindBy(xpath = "//div[contains(@id, ':AccountInfoInputSet:Name-inputEl')]")
    private WebElement changePrimaryInsuredContact;

    @FindBy(xpath = "//*[contains(@id, 'NameDenorm_FBM-inputEl') or contains(@id, ':FirstNameDenorm_FBM-inputEl')]")
    private WebElement editbox_PrimaryNamedInsuredChangeFirstName;
    
    @FindBy(xpath = "//*[contains(@id, 'NameDenorm_FBM-inputEl') or contains(@id, ':LastNameDenorm_FBM-inputEl')]")
    private WebElement editbox_PrimaryNamedInsuredChangeLastName;

    @FindBy(xpath = "//a[contains(@id, ':BindPolicyChange')]")
    private WebElement IssueNameChange;

    @FindBy(xpath = "//span[contains(@id, ':BindPolicyChange-btnEl')]")
    private WebElement IssueNameChangeAlt;

    private Guidewire8Select select_CompanyOrPerson() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ContactTypeDenorm_FBM-triggerWrap')]");
    }
    
/************************************************************************************************************************************************************************************************************************
    Private Helper Methods
**************************************************************************************************************************************************************************************************************************/    
    
    
    private void initiateNameChangeFromPolicySummary() {
    	
            repository.pc.policy.PolicyMenu policyMenu = new PolicyMenu(driver);
            policyMenu.clickMenuActions();
            policyMenu.clickNameChange();
            waitUntilElementIsVisible(editbox_PrimaryNamedInsuredChangeFirstName);
    }
    
    private void setNameChangeInfo() {
    	
    }
    
    private String getUWFullName() throws GuidewireException {
    	repository.pc.policy.PolicySummary summary = new repository.pc.policy.PolicySummary(driver);
        String underwriterFullName = summary.getActivityAssignment("Primary Named Insured Change for Underwriter");
        System.out.println("The underwriter that received the Name Change activity is: " + underwriterFullName);
        return underwriterFullName.trim();
    }
    
    private void startNameChange(GenerateContactType contactType, String newFirstName, String newLastNameOrCompanyName, String reason) {
    	initiateNameChangeFromPolicySummary();
        waitUntilElementIsVisible(editbox_PrimaryNamedInsuredChangeFirstName);
        Guidewire8Select companyOrPerson = select_CompanyOrPerson();
        String contactTypeName = contactType.name();
        companyOrPerson.selectByVisibleTextPartial(contactTypeName);
        
        this.contactType = contactType;
        
        if (contactType == GenerateContactType.Company) {
            setNewCompanyName(newLastNameOrCompanyName);
        } else if (contactType == GenerateContactType.Person) {
            setNewPersonName(newFirstName, newLastNameOrCompanyName);
        }

        setChangeReason(reason);
        clickGenericWorkorderUpdate();
    }
    
    private void setNewPersonName(String newfirstName, String lastName) {
    	waitUntilElementIsClickable(editbox_PrimaryNamedInsuredChangeFirstName);
    	editbox_PrimaryNamedInsuredChangeFirstName.click();
    	editbox_PrimaryNamedInsuredChangeFirstName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
    	editbox_PrimaryNamedInsuredChangeFirstName.sendKeys(newfirstName);
    	editbox_PrimaryNamedInsuredChangeFirstName.sendKeys(Keys.TAB);
    	waitUntilElementIsClickable(editbox_PrimaryNamedInsuredChangeLastName);
    	editbox_PrimaryNamedInsuredChangeLastName.click();
    	editbox_PrimaryNamedInsuredChangeLastName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
    	editbox_PrimaryNamedInsuredChangeLastName.sendKeys(lastName);
    	editbox_PrimaryNamedInsuredChangeLastName.sendKeys(Keys.TAB);

    }


    private void setNewCompanyName(String newCompanyName) {
    	waitUntilElementIsClickable(editbox_PrimaryNamedInsuredChangeLastName);
    	editbox_PrimaryNamedInsuredChangeLastName.click();
    	editbox_PrimaryNamedInsuredChangeLastName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
    	editbox_PrimaryNamedInsuredChangeLastName.sendKeys(newCompanyName);
    }
    
    private void sendToContactManager() {
        repository.pc.policy.PolicySummary summaryPage = new repository.pc.policy.PolicySummary(driver);
        String activityName = "Primary Named Insured Change for Underwriter";
        summaryPage.clickActivity(activityName);

        ActivityPopup activity = new ActivityPopup(getDriver());
        activity.sendToContactManager(activityName);
    }
    
    public GenerateContact createContact(boolean transfer, GenerateContactType contactType, String personFirstName, String lastNameOrCompanyName) throws Exception {
        AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy Services");//changed to Policy serices from Numbering cus there is no dept of Numbering.
        if (transfer && contactType == GenerateContactType.Company) {
            myContactObj = new GenerateContact.Builder(driver)
                    .withCreator(abUser)
                    .withCompanyName(lastNameOrCompanyName)
                    .withRoles(rolesToAdd)
                    .withGenerateAccountNumber(true)
                    .build(GenerateContactType.Company);

        } else if (transfer && contactType == GenerateContactType.Person) {
            myContactObj = new GenerateContact.Builder(driver)
                    .withCreator(abUser)
                    .withFirstLastName(personFirstName, lastNameOrCompanyName)
                    .withGenerateAccountNumber(true)
                    .withRoles(rolesToAdd)
                    .build(GenerateContactType.Person);
        } else {
            // TODO: Generate Method to handle name changes without transfer.
        }
        return myContactObj;
    }

    
/********************************************************************************************************************************************************************************************************************************
 *  Public Methods
 * @throws Exception 
 ********************************************************************************************************************************************************************************************************************************* */    
 
    public void sendActivityToContactManager(GeneratePolicy policy, GenerateContactType contactType, String newFirstName, String newLastNameOrCompanyName, String reason) throws Exception {
    	this.myPolicyObj = policy;
    	startNameChange(contactType, newFirstName, newLastNameOrCompanyName, reason);
    	sendToContactManager();
    	String uw = getUWFullName();
    	GuidewireHelpers help = new GuidewireHelpers(driver);
    	help.logout();
    	Underwriters underwriter = UnderwritersHelper.getUnderwriterInfoByFullName(uw);
    	new Login(driver).loginAndSearchPolicyByAccountNumber(underwriter.getUnderwriterUserName(), underwriter.getUnderwriterPassword(), policy.accountNumber);
    	repository.pc.policy.PolicySummary summaryPage = new PolicySummary(driver);
    	summaryPage.clickActivity("Primary Named Insured Change for Underwriter");
    	ActivityPopup activity = new ActivityPopup(getDriver());
        activity.sendToContactManager("Primary Named Insured Change for Underwriter");
    }
        
    public GenerateContact changePNI(GeneratePolicy myPolicyObj, GenerateContactType contactType, String newFirstName, String newLastNameOrCompanyName) throws Exception {
        // generate new contact from the type
        this.myPolicyObj = myPolicyObj;
        sendActivityToContactManager(myPolicyObj, contactType, newFirstName, newLastNameOrCompanyName, "Didn't like his old one.");
        GenerateContact newContact = createContact(true, contactType, newFirstName, newLastNameOrCompanyName);
        transferContact(myPolicyObj, newContact);

        return myContactObj;
    }


    public void transferContact(GeneratePolicy myPolicyObj, GenerateContact newContact) throws Exception {
        TopMenuAB menu = new TopMenuAB(driver);
        menu.clickSearchTab();
        AdvancedSearchAB searchAB = new AdvancedSearchAB(driver);

        if (myPolicyObj.pniContact.getCompanyName() == null) {
            searchAB.searchByFirstLastName(myPolicyObj.pniContact.getFirstName(), myPolicyObj.pniContact.getLastName(),
                    myPolicyObj.pniContact.getAddress().getLine1());
        } else {
            searchAB.searchCompanyByName(myPolicyObj.pniContact.getCompanyName(), myPolicyObj.pniContact.getAddress().getLine1(), myPolicyObj.pniContact.getAddress().getState());
        }

        ContactDetailsBasicsAB person = new ContactDetailsBasicsAB(driver);
        person.clickActionsButton();
        person.clickTransferButton();

        ContactTransferAB transferMe = new ContactTransferAB(driver);

        transferMe.transferContact(newContact.accountNumber);

        // IContactDetailsBasics newContactPage =
        // ContactFactory.getContactPage();
        // String contactPageTitle = newContactPage.getContactPageTitle();
        // if(this.myContactObj.companyName.isEmpty()){
        // if(!contactPageTitle.equals(myContactObj.firstName +" "+
        // myContactObj.lastName)){
        // throw new Exception("After clicking Transfer, AB did not take you to
        // the new Contacts page.");
        // }
        // } //else {
        // if(!contactPageTitle.equals(myContactObj.companyName)) {
        // throw new Exception("After clicking Transfer, AB did not take you to
        // the new Contacts page: " + myContactObj.accountNumber);
        // }
        // }

    }
    
    
    
/*
    private String startNameChange(GenerateContactType contactType, String newFirstName, String newLastNameOrCompanyName, String reason) {
        this.contactType = contactType;
        delay(100);
        if (contactType == GenerateContactType.Company) {
            setNewCompanyName(newLastNameOrCompanyName);
        } else if (contactType == GenerateContactType.Person) {
            setNewPersonName(newName);
        }

        setChangeReason(reason);
        

        long endTime = new Date().getTime() + (10 * 1000);
        do {
            if (checkIfElementExists("//a[contains(@id, 'Update')] | //span[contains(@id, ':FinishPCR-btnEl')]", 1000)) {
                clickGenericWorkorderUpdate();
            }
            
        } while (finds(By.xpath("//span[contains(@id, 'PolicyChangeWizard_PolicyInfoScreen:ttlBar')]"))
                .size() <= 0 && new Date().getTime() < endTime);

        PolicySummary summary = new PolicySummary(driver);
        String underwriterFullName = summary.getActivityAssignment("Primary Named Insured Change for Underwriter");
        System.out.println("The underwriter that received the Name Change activity is: " + underwriterFullName);
        return underwriterFullName.trim();

    }

    private String startNameChange(GenerateContactType contactType, String reason) {
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickNameChange();
        
        waitUntilElementIsVisible(editbox_PrimaryNamedInsuredChangeFirstName);
        Guidewire8Select companyOrPerson = select_CompanyOrPerson();
        String contactTypeName = contactType.name();
        companyOrPerson.selectByVisibleTextPartial(contactTypeName);

        DataFactory factory = new DataFactory();
        Random rand = new Random();
        String newName = factory.getName() + rand.nextInt();
        return startNameChange(contactType, newName, reason);

    }

    private void setNewPersonName(String newfirstName, String lastName) {
    	waitUntilElementIsClickable(editbox_PrimaryNamedInsuredChangeFirstName);
    	editbox_PrimaryNamedInsuredChangeFirstName.click();
    	editbox_PrimaryNamedInsuredChangeFirstName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
    	editbox_PrimaryNamedInsuredChangeFirstName.sendKeys(newfirstName);
    	editbox_PrimaryNamedInsuredChangeFirstName.sendKeys(Keys.TAB);
    	waitUntilElementIsClickable(editbox_PrimaryNamedInsuredChangeLastName);
    	editbox_PrimaryNamedInsuredChangeLastName.click();
    	editbox_PrimaryNamedInsuredChangeLastName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
    	editbox_PrimaryNamedInsuredChangeLastName.sendKeys(lastName);
    	editbox_PrimaryNamedInsuredChangeLastName.sendKeys(Keys.TAB);

    }


    public void setNewCompanyName(String newCompanyName) {
    	waitUntilElementIsClickable(editbox_PrimaryNamedInsuredChangeLastName);
    	editbox_PrimaryNamedInsuredChangeLastName.click();
    	editbox_PrimaryNamedInsuredChangeLastName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
    	editbox_PrimaryNamedInsuredChangeLastName.sendKeys(newCompanyName);
    }


    public void sendToContactManager() {

        
        PolicySummary summaryPage = new PolicySummary(driver);
        String activityName = "Primary Named Insured Change for Underwriter";
        summaryPage.clickActivity(activityName);

        ActivityPopup activity = new ActivityPopup(getDriver());
        activity.sendToContactManager(activityName);
    }

    public GenerateContact createContact(boolean transfer) throws Exception {
        AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy Services");//changed to Policy serices from Numbering cus there is no dept of Numbering.
        if (transfer && contactType == GenerateContactType.Company) {
            myContactObj = new GenerateContact.Builder(driver)
                    .withCreator(abUser)
                    .withRoles(rolesToAdd)
                    .withGenerateAccountNumber(true)
                    .build(GenerateContactType.Company);

        } else if (transfer && contactType == GenerateContactType.Person) {
            myContactObj = new GenerateContact.Builder(driver)
                    .withCreator(abUser)
                    .withGenerateAccountNumber(true)
                    .withRoles(rolesToAdd)
                    .build(GenerateContactType.Person);
        } else {
            // TODO: Generate Method to handle name changes without transfer.
        }
        return myContactObj;
    }

    public void transferContact() throws Exception {
        TopMenuAB menu = new TopMenuAB(driver);
        menu.clickSearchTab();
        AdvancedSearchAB searchAB = new AdvancedSearchAB(driver);

        if (myPolicyObj.pniContact.getCompanyName() == null) {
            searchAB.searchByFirstLastName(myPolicyObj.pniContact.getFirstName(), myPolicyObj.pniContact.getLastName(),
                    myPolicyObj.pniContact.getAddress().getLine1());
        } else {
            searchAB.searchCompanyByName(myPolicyObj.pniContact.getCompanyName(), myPolicyObj.pniContact.getAddress().getLine1(), myPolicyObj.pniContact.getAddress().getState());
        }

        ContactDetailsBasicsAB person = new ContactDetailsBasicsAB(driver);
        person.clickActionsButton();
        person.clickTransferButton();

        ContactTransferAB transferMe = new ContactTransferAB(driver);

        transferMe.clickContactPicker();

        AdvancedSearchAB searchNewContact = new AdvancedSearchAB(driver);
        searchNewContact.searchByAccountNumber(myContactObj.accountNumber);

        
        transferMe = new ContactTransferAB(driver);
        transferMe.ContactTransferRadioClicker(true);
        transferMe.clickTransferButton();
        

        // IContactDetailsBasics newContactPage =
        // ContactFactory.getContactPage();
        // String contactPageTitle = newContactPage.getContactPageTitle();
        // if(this.myContactObj.companyName.isEmpty()){
        // if(!contactPageTitle.equals(myContactObj.firstName +" "+
        // myContactObj.lastName)){
        // throw new Exception("After clicking Transfer, AB did not take you to
        // the new Contacts page.");
        // }
        // } //else {
        // if(!contactPageTitle.equals(myContactObj.companyName)) {
        // throw new Exception("After clicking Transfer, AB did not take you to
        // the new Contacts page: " + myContactObj.accountNumber);
        // }
        // }

    }


    public GenerateContact changePNI(GeneratePolicy myPolicyObj, GenerateContactType contactType) throws Exception {
        // generate new contact from the type
        this.myPolicyObj = myPolicyObj;
        String underwriterFullName = startNameChange(contactType, "Didn't like his old one.");
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByFullName(underwriterFullName);
        Login login = new Login(myPolicyObj.getDriver());
        login.loginAndSearchPolicyByAccountNumber(uw.underwriterUserName, uw.getUnderwriterPassword(), myPolicyObj.accountNumber);
        sendToContactManager();
//        GenerateContact newContact = createContact(true);
        transferContact();

        return myContactObj;
    }


    public void changePNI() {
        startNameChange(GenerateContactType.Company, "test name", "test reason");
        sendToContactManager();

        // in CM, make a new contact

    }


    public void changePNI(GenerateContactType contactType, String newNameFirstName, String newLastNameOrCompanyName, String reason) {

        startPolicyNameChange(contactType, newName, reason);

    }


    public void setPolicyObject(GeneratePolicy myPolicyObj) {

    }


    public void startPolicyNameChange(GenerateContactType contactType, String newName, String reason) {

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickNameChange();
        
        Guidewire8Select companyOrPerson = select_CompanyOrPerson();
        String contactTypeName = contactType.name();
        companyOrPerson.selectByVisibleTextPartial(contactTypeName);

        startNameChange(contactType, newName, reason);

    }
*/
    public String verifyRowExistsRecentNotesTable() {

        WebElement element = find(By.xpath("//div[contains(@id, 'x-form-el-PolicyFile_Summary:Policy_SummaryScreen:NotesSummaryLV:0:NoteRowSet:Body')]"));
        String value = element.getText();
        return value;
    }


}
