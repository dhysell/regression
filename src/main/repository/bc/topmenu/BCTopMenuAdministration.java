package repository.bc.topmenu;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BCTopMenuAdministration extends BCTopMenu {

	public BCTopMenuAdministration(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	//Elements
	
	//Main Admin Menu Headers
	@FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_UsersAndSecurity-itemEl')]")
    private WebElement Users_and_Security;
	
	@FindBy(xpath =  "//div[starts-with(@id,'TabBar:AdministrationTab:Admin_UsersAndSecurity-arrowEl')]")
    private WebElement Users_and_Security_Arrow;
	
	@FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_BusinessSettings-itemEl')]")
	private WebElement Business_Settings;
	
	@FindBy(xpath =  "//div[starts-with(@id,'TabBar:AdministrationTab:Admin_BusinessSettings-arrowEl')]")
	private WebElement Business_Settings_Arrow;
	
	@FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_Monitoring-itemEl')]")
	private WebElement Monitoring;
	
	@FindBy(xpath =  "//div[starts-with(@id,'TabBar:AdministrationTab:Admin_Monitoring-arrowEl')]")
	private WebElement Monitoring_Arrow;
	
	@FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_Utilities-itemEl')]")
	private WebElement Utilities;
	
	@FindBy(xpath =  "//div[starts-with(@id,'TabBar:AdministrationTab:Admin_Utilities-arrowEl')]")
	private WebElement Utilities_Arrow;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_GLMapping_GW-itemEl')]")
    private WebElement GL_Mapping;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_DocumentProductionResource-itemEl')]")
    private WebElement Document_Production_Resource;
	
    //Users & Security Sub-menu
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_UsersAndSecurity:UsersAndSecurity_UserSearch-itemEl')]")
    private WebElement Users;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_UsersAndSecurity:UsersAndSecurity_Groups-itemEl')]")
    private WebElement Groups;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_UsersAndSecurity:UsersAndSecurity_Roles-itemEl')]")
    private WebElement Roles;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_UsersAndSecurity:UsersAndSecurity_SecurityZones-itemEl')]")
    private WebElement Security_Zones;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_UsersAndSecurity:UsersAndSecurity_AuthorityLimitProfiles-itemEl')]")
    private WebElement Authority_Limit_Profile;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_UsersAndSecurity:UsersAndSecurity_PortalAuthorizationAdminPage-itemEl')]")
    private WebElement Portal_Authorization_Admin_Console;
    
    //Business Settings Sub-Menu
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_BusinessSettings:BusinessSettings_ActivityPatterns-itemEl')]")
    private WebElement Activity_Patterns;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_BusinessSettings:BusinessSettings_AgencyBillPlans-itemEl')]")
    private WebElement Agency_Bill_Plans;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_BusinessSettings:BusinessSettings_BillingPlans-itemEl')]")
    private WebElement Billing_Plans;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_BusinessSettings:BusinessSettings_ChargePatterns-itemEl')]")
    private WebElement Charge_Patterns;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_BusinessSettings:BusinessSettings_CollectionAgencies-itemEl')]")
    private WebElement Collection_Agencies;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_BusinessSettings:BusinessSettings_CommissionPlans-itemEl')]")
    private WebElement Commission_Plans;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_BusinessSettings:BusinessSettings_DelinquencyPlans-itemEl')]")
    private WebElement Delinquency_Plans;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdminTab:Admin_BusinessSettings:BusinessSettings_Holidays-itemEl')]")
    private WebElement Holidays;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_BusinessSettings:BusinessSettings_PaymentAllocationPlans-itemEl')]")
    private WebElement Payment_Allocation_Plans;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_BusinessSettings:BusinessSettings_PaymentPlans-itemEl')]")
    private WebElement Payment_Plans;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_BusinessSettings:BusinessSettings_ReturnPremiumPlans-itemEl')]")
    private WebElement Return_Premium_Plans;
    
    //Monitoring Sub-Menu
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_Monitoring:Monitoring_MessagingDestinationControlList-itemEl')]")
    private WebElement Message_Queues;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_Monitoring:Monitoring_WorkflowSearch-itemEl')]")
    private WebElement Workflows;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_Monitoring:Monitoring_WorkflowStats-itemEl')]")
    private WebElement Workflow_Statistics;
    
    //Utilities Sub-Menu
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_Utilities:Utilities_ImportWizard-itemEl')]")
    private WebElement Import_Data;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_Utilities:Utilities_ExportData-itemEl')]")
    private WebElement Export_Data;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_Utilities:Utilities_ScriptParametersPage-itemEl')]")
    private WebElement Script_Parameters;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_Utilities:Utilities_DataChangePage-itemEl')]")
    private WebElement Data_Change;
    
    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AdministrationTab:Admin_Utilities:Utilities_AdminDataSorter-itemEl')]")
    private WebElement Admin_Sorter;
    
    //Methods

    //Main Admin Menu
    public void clickUsersAndSecurity() {
        clickAdministrationArrow();
        clickWhenClickable(Users_and_Security);
    }
    
    public void clickBusinessSettings() {
        clickAdministrationArrow();
        clickWhenClickable(Business_Settings);
    }
    
    public void clickMonitoring() {
        clickAdministrationArrow();
        clickWhenClickable(Monitoring);
    }
    
    public void clickUtilities() {
        clickAdministrationArrow();
        clickWhenClickable(Utilities);
    }
    
    public void clickGLMapping() {
        clickAdministrationArrow();
        clickWhenClickable(GL_Mapping);
    }
    
    public void clickDocumentProductionResource() {
        clickAdministrationArrow();
        clickWhenClickable(Document_Production_Resource);
    }
    
    //Users & Security Sub-Menu
    public void clickUsersAndSecurityUsers() {
        clickAdministrationArrow();
        hoverOver(Users_and_Security_Arrow);
        clickWhenClickable(Users);
    }

    public void clickUsersAndSecurityGroups() {
        clickAdministrationArrow();
        hoverOver(Users_and_Security_Arrow);
        clickWhenClickable(Groups);
    }

    public void clickUsersAndSecurityRoles() {
        clickAdministrationArrow();
        hoverOver(Users_and_Security_Arrow);
        clickWhenClickable(Roles);
    }
    
    public void clickUsersAndSecuritySecurityZones() {
        clickAdministrationArrow();
        hoverOver(Users_and_Security_Arrow);
        clickWhenClickable(Security_Zones);
    }

    public void clickUsersAndSecurityAuthorityLimitProfile() {
        clickAdministrationArrow();
        hoverOver(Users_and_Security_Arrow);
        clickWhenClickable(Authority_Limit_Profile);
    }
    
    public void clickUsersAndSecurityPortalAuthorizationAdminConsole() {
        clickAdministrationArrow();
        hoverOver(Users_and_Security_Arrow);
        clickWhenClickable(Portal_Authorization_Admin_Console);
    }

    //Business Settings Sub-Menu
    public void clickBusinessSettingsActivityPatterns() {
        clickAdministrationArrow();
        hoverOver(Business_Settings_Arrow);
        clickWhenClickable(Activity_Patterns);
    }
    
    public void clickBusinessSettingsAgencyBillPlans() {
        clickAdministrationArrow();
        hoverOver(Business_Settings_Arrow);
        clickWhenClickable(Agency_Bill_Plans);
    }
    
    public void clickBusinessSettingsBillingPlans() {
        clickAdministrationArrow();
        hoverOver(Business_Settings_Arrow);
        clickWhenClickable(Billing_Plans);
    }
    
    public void clickBusinessSettingsChargePatterns() {
        clickAdministrationArrow();
        hoverOver(Business_Settings_Arrow);
        clickWhenClickable(Charge_Patterns);
    }
    
    public void clickBusinessSettingsCollectionAgencies() {
        clickAdministrationArrow();
        hoverOver(Business_Settings_Arrow);
        clickWhenClickable(Collection_Agencies);
    }
    
    public void clickBusinessSettingsCommissionPlans() {
        clickAdministrationArrow();
        hoverOver(Business_Settings_Arrow);
        clickWhenClickable(Commission_Plans);
    }
    
    public void clickBusinessSettingsDelinquencyPlans() {
        clickAdministrationArrow();
        hoverOver(Business_Settings_Arrow);
        clickWhenClickable(Delinquency_Plans);
    }

    public void clickBusinessSettingsHolidays() {
        clickAdministrationArrow();
        hoverOver(Business_Settings_Arrow);
        clickWhenClickable(Holidays);
    }
    
    public void clickBusinessSettingsPaymentAllocationPlans() {
        clickAdministrationArrow();
        hoverOver(Business_Settings_Arrow);
        clickWhenClickable(Payment_Allocation_Plans);
    }
    
    public void clickBusinessSettingsPaymentPlans() {
        clickAdministrationArrow();
        hoverOver(Business_Settings_Arrow);
        clickWhenClickable(Payment_Plans);
    }
    
    public void clickBusinessSettingsReturnPremiumPlans() {
        clickAdministrationArrow();
        hoverOver(Business_Settings_Arrow);
        clickWhenClickable(Return_Premium_Plans);
    }

    //Monitoring Sub-Menu
    public void clickMontoringMessageQueues() {
        clickAdministrationArrow();
        hoverOver(Monitoring_Arrow);
        clickWhenClickable(Message_Queues);
    }

    public void clickMontoringWorkflows() {
        clickAdministrationArrow();
        hoverOver(Monitoring_Arrow);
        clickWhenClickable(Workflows);
    }

    public void clickMontoringWorkflowStatistics() {
        clickAdministrationArrow();
        hoverOver(Monitoring_Arrow);
        clickWhenClickable(Workflow_Statistics);
    }

    //Utilities Sub-Menu
    public void clickUtilitiesImportData() {
        clickAdministrationArrow();
        hoverOver(Utilities_Arrow);
        clickWhenClickable(Import_Data);
    }

    public void clickUtilitiesExportData() {
        clickAdministrationArrow();
        hoverOver(Utilities_Arrow);
        clickWhenClickable(Export_Data);
    }

    public void clickUtilitiesScriptParameters() {
        clickAdministrationArrow();
        hoverOver(Utilities_Arrow);
        clickWhenClickable(Script_Parameters);
    }

    public void clickUtilitiesDataChange() {
        clickAdministrationArrow();
        hoverOver(Utilities_Arrow);
        clickWhenClickable(Data_Change);
    }
    
    public void clickUtilitiesAdminSorter() {
        clickAdministrationArrow();
        hoverOver(Utilities_Arrow);
        clickWhenClickable(Admin_Sorter);
    }
}
