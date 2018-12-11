package repository.pc.topmenu;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.pc.administration.AdministrationMenuPC;

public class TopMenuAdministrationPC extends TopMenuPC {

    public TopMenuAdministrationPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath =  "//div[contains(@id,'TabBar:AdminTab:Admin_TownshipRangePage')]")
    public WebElement link_TownhsipRange;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity-itemEl')]")
    public WebElement Users_and_Security;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_AdminUserSearchPage-itemEl')]")
    public WebElement Users;

    @FindBy(xpath =  "//td[starts-with(@id,'Admin:MenuLinks:Admin_UsersAndSecurity:UsersAndSecurity_AdminUserSearchPage')]")
    public WebElement Users_sidebar;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_AdminGroupSearchPage-itemEl')]")
    public WebElement Groups;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_Roles-itemEl')]")
    public WebElement Roles;
    
    @FindBy(xpath =  "//td[contains(@id,'Admin:MenuLinks:Admin_UsersAndSecurity:UsersAndSecurity_Roles')]")
    public WebElement sidebar_Roles;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_Regions-itemEl')]")
    public WebElement Regions;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_OrganizationSearchPage-itemEl')]")
    public WebElement Organizations;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_AdminProducerCodeSearch-itemEl')]")
    public WebElement Producer_Codes;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_UWAuthorityProfiles-itemEl')]")
    public WebElement Authority_Profiles;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_Attributes-itemEl')]")
    public WebElement Attributes;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_UsersAndSecurity:UsersAndSecurity_SearchAffinityGroup-itemEl')]")
    public WebElement Affinity_Groups;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_BusinessSettings-itemEl')]")
    public WebElement Business_Settings;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_BusinessSettings:BusinessSettings_ActivityPatterns-itemEl')]")
    public WebElement Activity_Patterns;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_BusinessSettings:BusinessSettings_Holidays-itemEl')]")
    public WebElement Holidays;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_BusinessSettings:BusinessSettings_FormPatterns-itemEl')]")
    public WebElement Policy_Form_Patterns;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_BusinessSettings:BusinessSettings_PolicyHolds-itemEl')]")
    public WebElement Policy_Holds;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_Monitoring-itemEl')]")
    public WebElement Monitoring;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_Monitoring:Monitoring_MessageSearch-itemEl')]")
    public WebElement Messages;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_Monitoring:Monitoring_MessagingDestinationControlList-itemEl')]")
    public WebElement Message_Queues;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_Monitoring:Monitoring_WorkflowSearch-itemEl')]")
    public WebElement Workflows;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_Monitoring:Monitoring_WorkflowStats-itemEl')]")
    public WebElement Workflow_Statistics;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_Utilities-itemEl')]")
    public WebElement Utilities;

    // *[@id="TabBar:AdminTab:Admin_Utilities-arrowEl"]
    @FindBy(xpath =  "//div[starts-with(@id,'TabBar:AdminTab:Admin_Utilities-arrowEl')]")
    public WebElement UtilitiesArrow;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_Utilities:Utilities_ImportWizard-itemEl')]")
    public WebElement Import_Data;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_Utilities:Utilities_ExportData-itemEl')]")
    public WebElement Export_Data;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_Utilities:Utilities_ScriptParametersPage-itemEl')]")
    public WebElement Script_Parameters;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_Utilities:Utilities_DataFlowMasks-itemEl')]")
    public WebElement Spreadsheet_Export_Formats;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_Utilities:Utilities_DataChangePage-itemEl')]")
    public WebElement Data_Change;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_Utilities:Utilities_ProductModelViewer_Ext-itemEl')]")
    public WebElement Product_Model_Viewer;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_Utilities:Utilities_ReportLogViewer_Ext-itemEl')]")
    public WebElement Report_Payload_Retrieval;

    // first menu
    public void clickTownshipRange() {
        clickAdministrationArrow();
        clickWhenVisible(link_TownhsipRange);
    }
    
    public void clickUsersAndSecurityMenu() {
        clickAdministrationArrow();
        clickWhenVisible(Users_and_Security);
    }

    public void clickUserSearch() {
        clickAdministrationArrow();
        hoverOverAndClick(Users_and_Security);
        clickWhenClickable(Users_sidebar);
        // clickWhenVisible(Users);
    }

    public void clickGroupSearch() {
        clickAdministrationArrow();
        hoverOver(Users_and_Security);
        clickWhenVisible(Groups);
    }

    public void clickRoles() {
        clickAdministrationArrow();
        hoverOverAndClick(Users_and_Security);
        clickWhenClickable(sidebar_Roles);
    }

    public void clickRegions() {
        clickAdministrationArrow();
        hoverOver(Users_and_Security);
        clickWhenVisible(Regions);
    }

    public void clickRegionSearch() {
        clickAdministrationArrow();
        hoverOver(Users_and_Security);
        clickWhenVisible(Regions);
    }

    public void clickOrganizationSearch() {
        clickAdministrationArrow();
        hoverOver(Users_and_Security);
        clickWhenVisible(Organizations);
    }

    public void clickProducerCodes() {
        clickAdministrationArrow();
        hoverOver(Users_and_Security);
        clickWhenVisible(Producer_Codes);
    }

    public void clickAuthorityProfiles() {
        clickAdministrationArrow();
        hoverOver(Users_and_Security);
        clickWhenVisible(Authority_Profiles);
    }

    public void clickAttributes() {
        clickAdministrationArrow();
        hoverOver(Users_and_Security);
        clickWhenVisible(Attributes);
    }


    public void clickAffinityGroups() {
        clickAdministrationArrow();
        hoverOver(Users_and_Security);
        clickWhenVisible(Affinity_Groups);
    }

    // second menu
    public void clickBusinessSettings() {
        clickAdministrationArrow();
        clickWhenVisible(Business_Settings);
    }

    public void clickActivityPatterns() {
        clickAdministrationArrow();
        hoverOver(Business_Settings);
        clickWhenVisible(Activity_Patterns);
    }

    public void clickHolidays() {
        clickAdministrationArrow();
        hoverOver(Business_Settings);
        clickWhenVisible(Holidays);
    }

    public void clickPolicyFormPatterns() {
        clickAdministrationArrow();
        hoverOver(Business_Settings);
        clickWhenVisible(Policy_Form_Patterns);
    }

    public void clickPolicyHolds() {
        clickAdministrationArrow();
        hoverOver(Business_Settings);
        clickWhenVisible(Policy_Holds);
    }

    // third menu
    public void clickMonitoring() {
        clickAdministrationArrow();
        clickWhenVisible(Monitoring);
    }

    public void clickEventMessages() {
        clickAdministrationArrow();
        hoverOverAndClick(Monitoring);
        // hoverOver(Monitoring);
        // clickWhenVisible(Message_Queues);
        AdministrationMenuPC sidemenu = new AdministrationMenuPC(getDriver());
        sidemenu.clickMessageQueues();
    }

    public void clickMessageQueues() {
        clickAdministrationArrow();
        hoverOver(Monitoring);
        clickWhenVisible(Message_Queues);
    }

    public void clickWorkflows() {
        clickAdministrationArrow();
        hoverOver(Monitoring);
        clickWhenVisible(Workflows);
    }

    public void clickWorkflowStatistics() {
        clickAdministrationArrow();
        hoverOver(Monitoring);
        
        clickWhenVisible(Workflow_Statistics);
        
    }

    // fourth menu
    public void clickUtilities() {
        clickAdministrationArrow();
        clickWhenClickable(Utilities);
        
    }

    public void clickImportData() {
        clickAdministrationArrow();
        hoverOver(Utilities);
        hoverOver(UtilitiesArrow);
        clickWhenVisible(Import_Data);
        
    }

    public void clickExportData() {
        clickAdministrationArrow();
        hoverOver(Utilities);
        hoverOver(UtilitiesArrow);
        clickWhenClickable(Export_Data);
    }

    public void clickScriptParameters() {
        clickAdministrationArrow();
        hoverOver(Utilities);
        clickWhenVisible(Script_Parameters);
    }

    public void clickSpreadsheetExportFormats() {
        clickAdministrationArrow();
        hoverOver(Utilities);
        clickWhenVisible(Spreadsheet_Export_Formats);
    }

    public void clickDataChange() {
        clickAdministrationArrow();
        hoverOver(Utilities);
        clickWhenVisible(Data_Change);
    }
    
    public void clickProductModelViewer() {
        clickAdministrationArrow();
        hoverOver(Utilities);
        hoverOver(UtilitiesArrow);
        clickWhenVisible(Product_Model_Viewer);
    }
    
    public void clickReportPayloadRetrieval() {
        clickAdministrationArrow();
        hoverOver(Utilities);
        hoverOver(UtilitiesArrow);
        clickWhenVisible(Report_Payload_Retrieval);
    }
}