package repository.ab.topmenu;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TopMenuAdministrationAB extends TopMenuAB {

    private WebDriver driver;
    public TopMenuAdministrationAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity-itemEl')]")
    public WebElement Users_and_Security;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_AdminUserSearchPage-itemEl')]")
    public WebElement Users;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_AdminGroupSearchPage-itemEl')]")
    public WebElement Groups;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_Roles-itemEl')]")
    public WebElement Roles;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_Regions-itemEl')]")
    public WebElement Regions;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_OrganizationSearchPage-itemEl')]")
    public WebElement Organizations;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_AdminProducerCodeSearch-itemEl')]")
    public WebElement Producer_Codes;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_UWAuthorityProfiles-itemEl')]")
    public WebElement Authority_Profiles;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_Attributes-itemEl')]")
    public WebElement Attributes;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_SearchAffinityGroup-itemEl')]")
    public WebElement Affinity_Groups;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_BusinessSettings-itemEl')]")
    public WebElement Business_Settings;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_BusinessSettings:BusinessSettings_ActivityPatterns-itemEl')]")
    public WebElement Activity_Patterns;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_BusinessSettings:BusinessSettings_Holidays-itemEl')]")
    public WebElement Holidays;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_BusinessSettings:BusinessSettings_FormPatterns-itemEl')]")
    public WebElement Policy_Form_Patterns;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_BusinessSettings:BusinessSettings_PolicyHolds-itemEl')]")
    public WebElement Policy_Holds;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_Monitoring-itemEl')]")
    public WebElement Monitoring;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_Monitoring:Monitoring_MessageSearch-itemEl')]")
    public WebElement Messages;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_Monitoring:Monitoring_MessagingDestinationControlList-itemEl')]")
    public WebElement Message_Queues;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_Monitoring:Monitoring_WorkflowSearch-itemEl')]")
    public WebElement Workflows;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_Monitoring:Monitoring_WorkflowStats-itemEl')]")
    public WebElement Workflow_Statistics;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_Utilities-itemEl')]")
    public WebElement Utilities;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_Utilities:Utilities_ImportWizard-itemEl')]")
    public WebElement Import_Data;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_Utilities:Utilities_ExportData-itemEl')]")
    public WebElement Export_Data;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_Utilities:Utilities_ScriptParametersPage-itemEl')]")
    public WebElement Script_Parameters;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_Utilities:Utilities_DataFlowMasks-itemEl')]")
    public WebElement Spreadsheet_Export_Formats;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab:Admin_Utilities:Utilities_DataChangePage-itemEl')]")
    public WebElement Data_Change;

    // open menu methods
    public void openUsersAndSecurityMenu() {
        Actions mouse = new Actions(driver);
        mouse.moveToElement(Users_and_Security);
        mouse.build().perform();
    }

    public void openBusinessSettings() {
        Actions mouse = new Actions(driver);
        mouse.moveToElement(Business_Settings);
        mouse.build().perform();
    }

    public void openMonitoring() {
        Actions mouse = new Actions(driver);
        mouse.moveToElement(Monitoring);
        mouse.build().perform();
    }

    public void openUtilities() {
        Actions mouse = new Actions(driver);
        mouse.moveToElement(Utilities);
        mouse.build().perform();
    }

    // first menu
    public void clickUsersAndSecurityMenu() {
        clickWhenClickable(Users_and_Security);
    }

    public void clickUserSearch() {
        openUsersAndSecurityMenu();
        clickWhenVisible(Users);
    }

    public void clickGroupSearch() {
        openUsersAndSecurityMenu();
        clickWhenVisible(Groups);
    }

    public void clickRoles() {
        openUsersAndSecurityMenu();
        clickWhenVisible(Roles);
    }

    public void clickRegions() {
        openUsersAndSecurityMenu();
        clickWhenVisible(Regions);
    }

    public void clickRegionSearch() {
        openUsersAndSecurityMenu();
        clickWhenVisible(Regions);
    }

    public void clickOrganizationSearch() {
        openUsersAndSecurityMenu();
        clickWhenVisible(Organizations);
    }

    public void clickProducerCodes() {
        openUsersAndSecurityMenu();
        clickWhenVisible(Producer_Codes);
    }

    public void clickAuthorityProfiles() {
        openUsersAndSecurityMenu();
        clickWhenVisible(Authority_Profiles);
    }

    public void clickAttributes() {
        openUsersAndSecurityMenu();
        clickWhenVisible(Attributes);
    }

    public void clickAffinityGroups() {
        openUsersAndSecurityMenu();
        clickWhenVisible(Affinity_Groups);
    }

    // second menu
    public void clickBusinessSettings() {
        clickWhenClickable(Business_Settings);
    }

    public void clickActivityPatterns() {
        openBusinessSettings();
        clickWhenVisible(Activity_Patterns);
    }

    public void clickHolidays() {
        openBusinessSettings();
        clickWhenVisible(Holidays);
    }

    public void clickPolicyFormPatterns() {
        openBusinessSettings();
        clickWhenVisible(Policy_Form_Patterns);
    }

    public void clickPolicyHolds() {
        openBusinessSettings();
        clickWhenVisible(Policy_Holds);
    }

    // third menu
    public void clickMonitoring() {
        clickWhenClickable(Monitoring);
    }

    public void clickEventMessages() {
        openMonitoring();
        clickWhenVisible(Messages);
    }

    public void clickMessageQueues() {
        openMonitoring();
        clickWhenVisible(Message_Queues);
    }

    public void clickWorkflows() {
        openMonitoring();
        clickWhenVisible(Workflows);
    }

    public void clickWorkflowStatistics() {
        openMonitoring();
        clickWhenVisible(Workflow_Statistics);
    }

    // fourth menu
    public void clickUtilities() {
        clickWhenClickable(Utilities);
    }

    public void clickImportData() {
        openUtilities();
        clickWhenVisible(Import_Data);
    }

    public void clickExportData() {
        openUtilities();
        clickWhenVisible(Export_Data);
    }

    public void clickScriptParameters() {
        openUtilities();
        clickWhenVisible(Script_Parameters);
    }

    public void clickSpreadsheetExportFormats() {
        openUtilities();
        clickWhenVisible(Spreadsheet_Export_Formats);
    }

    public void clickDataChange() {
        openUtilities();
        clickWhenVisible(Data_Change);
    }
}