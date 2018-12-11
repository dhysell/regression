package repository.ab.sidebar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AbUsers;
import repository.ab.search.*;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.BasePage;

public class SidebarAB extends BasePage {

    private WebDriver driver;

    public SidebarAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ----------------------------------------------------------------------------------
    // Administration
    // ----------------------------------------------------------------------------------

    @FindBy(xpath = "//div[contains(@id,'Admin:AdminMenuActions:AdminMenuActions_NewUserMenuItem')]")
    public WebElement link_SidebarUsersNewUser;


    public void initiateNewUser() {
        clickActions();
        clickWhenClickable(link_SidebarUsersNewUser);
    }


    // ----------------------------------------------------------------------------------
    // Desktop
    // ----------------------------------------------------------------------------------

    @FindBy(xpath = "//td[@id='DesktopGroup:MenuLinks:DesktopGroup_DesktopActivities']")
    public WebElement link_SidebarDesktopActivities;

    @FindBy(xpath = "//td[@id='DesktopGroup:MenuLinks:DesktopGroup_DesktopQueuedActivities']")
    public WebElement link_SidebarDesktopQueues;

    @FindBy(xpath = "//span[contains(@id, 'DesktopGroup:DesktopMenuActions-btnEl') or contains(@id, 'Contact:ContactMenuActions-btnInnerEl') or contains(@id,'Admin:AdminMenuActions-btnEl')]")
    public WebElement button_SidebarActions;

    @FindBy(xpath = "//div[@id='DesktopGroup:DesktopMenuActions:DesktopMenuActions_UserVacation']")
    public WebElement button_SidebarActionsVacationStatus;

    @FindBy(xpath = "//td[@id='DesktopGroup:MenuLinks:DesktopGroup_DesktopContactNames']")
    public WebElement link_SidebarNames;

    public void clickSidebarActivitiesLink() {
        
        link_SidebarDesktopActivities.click();
    }

    public void clickSidebarQueuesLink() {
        clickWhenClickable(link_SidebarDesktopQueues);
    }

    public void clickActions() {
        clickWhenClickable(button_SidebarActions);
    }

    public void clickVacationStatus() {
        clickWhenClickable(button_SidebarActionsVacationStatus);
    }

    public void clickNames() {
        clickWhenClickable(link_SidebarNames);
        waitForPageLoad();        
    }


    // ----------------------------------------------------------------------------------
    // Search
    // ----------------------------------------------------------------------------------

    @FindBy(xpath = "//td[@id='ABContacts:MenuLinks:ABContacts_ABContactSearchesGroup:ABContactSearchesGroup_ABContactSearch']")
    private WebElement link_SidebarSearchAdvancedSearch;

    @FindBy(xpath = "//td[@id='ABContacts:MenuLinks:ABContacts_ABContactSearchesGroup:ABContactSearchesGroup_SimpleABContactSearch']")
    private WebElement link_SidebarSearchSimpleSearch;

    @FindBy(xpath = "//td[@id='ABContacts:MenuLinks:ABContacts_ABContactSearchesGroup:ABContactSearchesGroup_AgentSearch']")
    private WebElement link_SidebarSearchAgentSearch;

    @FindBy(xpath = "//td[@id='ABContacts:MenuLinks:ABContacts_ABContactSearchesGroup:ABContactSearchesGroup_ActivitySearch']")
    private WebElement link_SidebarSearchActivitySearch;

    @FindBy(xpath = "//td[@id='ABContacts:MenuLinks:ABContacts_ABContactSearchesGroup:ABContactSearchesGroup_ClaimVendorSearch']")
    private WebElement link_SidebarSearchClaimVendorSearch;

    @FindBy(xpath = "//td[contains(@id, 'ABContacts:MenuLinks:ABContacts_ABContactSearchesGroup:ABContactSearchesGroup_RecentlyViewedSearch')]")
    private WebElement link_SidebarSearchRecentlyViewedSearch;

    @FindBy(xpath = "//td[contains(@id, 'ABContacts:MenuLinks:ABContacts_ABContactSearchesGroup:ABContactSearchesGroup_PhoneSearch')]")
    private WebElement link_SidebarSearchPhoneNumberSearch;

    @FindBy(xpath = "//td[contains(@id, ':ABContactSearchesGroup_AddressSearch')]")
    private WebElement link_SidebarSearchAddressSearch;

    @FindBy(xpath = "//td[contains(@id, ':MenuLinks:ABContacts_PendingChanges')]")
    private WebElement link_SidebarSearchPendingChanges;

    @FindBy(xpath = "//td[contains(@id, ':ABContacts_PendingChangesGroup:PendingChangesGroup_PendingChangesCC')]")
    private WebElement link_SidebarSearchPendingChangesCC;

    @FindBy(xpath = "//td[contains(@id, '_PendingChangesGroup:PendingChangesGroup_PendingChangesPC')]")
    private WebElement link_SidebarSearchPendingChangesPC;

    @FindBy(xpath = "//td[@id='ABContacts:MenuLinks:ABContacts_ABContactSearchesGroup:ABContactSearchesGroup_FBMSolrSearch']")
    private WebElement link_SidebarSearchSolrSearch;
    
    @FindBy(xpath = "//td[contains(@id, 'ABContacts:MenuLinks:ABContacts_MergeContacts')]")
    private WebElement link_SidebarSearchMergeContacts;

    public repository.ab.search.AdvancedSearchAB clickSidebarAdvancedSearchLink() {
        clickWhenClickable(link_SidebarSearchAdvancedSearch);
        return new AdvancedSearchAB(driver);
    }

    public repository.ab.search.SimpleSearchAB clickSidebarSimpleSearchLink() {
        clickWhenClickable(link_SidebarSearchSimpleSearch);
        return new SimpleSearchAB(driver);
    }

    public repository.ab.search.SearchAgentSearchAB clickSidebarAgentSearchLink() {
        clickWhenClickable(link_SidebarSearchAgentSearch);
        
        return new SearchAgentSearchAB(driver);
    }

    public repository.ab.search.ActivitySearchAB clickSidebarActivitySearchLink() {
        clickWhenClickable(link_SidebarSearchActivitySearch);
        return new ActivitySearchAB(driver);
    }

    public repository.ab.search.RecentlyViewedAB clickSidebarRecentlyViewedSearchLink() {
        clickWhenClickable(link_SidebarSearchRecentlyViewedSearch);
        
        waitUntilElementIsVisible(find(By.xpath("//span[contains(@id,'RecentlyViewedSearch:ttlBar')]")));
        return new RecentlyViewedAB(driver);
    }

    public repository.ab.search.ClaimVendorSearchAB clickClaimVendorSearch() {
        
        clickWhenClickable(link_SidebarSearchClaimVendorSearch);
        
        return new ClaimVendorSearchAB(driver);
    }

    public void clickSidebarPhoneNumberSearch() {
        clickWhenClickable(link_SidebarSearchPhoneNumberSearch);
    }

    public repository.ab.search.AddressSearchAB clickSidebarAddressSearch() {
        
        clickWhenClickable(link_SidebarSearchAddressSearch);
        
        return new AddressSearchAB(driver);
    }

    public repository.ab.search.PendingChangesAB clickSidebarPendingChanges(String changes) {
        
        clickWhenClickable(link_SidebarSearchPendingChanges);
        
        if (changes.contains("Lienholder")) {
            clickWhenClickable(link_SidebarSearchPendingChangesPC);
        } else {
            clickWhenClickable(link_SidebarSearchPendingChangesCC);
        }
        return new PendingChangesAB(driver);
    }
    
    public boolean pendingChangedExists() {
    	if(checkIfElementExists(link_SidebarSearchPendingChanges, 1)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public SolrContactSearch clickSolrSearch() {
    	waitUntilElementIsVisible(link_SidebarSearchSolrSearch);
    	link_SidebarSearchSolrSearch.click();
    	return new SolrContactSearch(driver);
    }
    
    private void clickMerge() {
    	waitUntilElementIsClickable(link_SidebarSearchMergeContacts);
        clickWhenClickable(link_SidebarSearchMergeContacts);
//    	return new PotentialDuplicateContacts();
    }
    
    public void clickMergeContacts(AbUsers user) {
    	TopMenuAB topMenu = new TopMenuAB(driver);
    	topMenu.clickSearchTab(user);
    	clickMerge();    	
    }

    // ----------------------------------------------------------------------------------
    // Contact
    // ----------------------------------------------------------------------------------

    @FindBy(xpath = "//td[@id='Contact:MenuLinks:Contact_ContactDetail']")
    public WebElement link_SidebarContactDetails;

    @FindBy(xpath = "//td[@id='Contact:MenuLinks:Contact_ContactActivities']")
    public WebElement link_SidebarContactWorkplan;

    @FindBy(xpath = "//a[contains(@id, 'Contact:ContactMenuActions:ClaimMenuActions_NewDocument:ContactNewDocumentMenuItemSet:ClaimNewDocumentMenuItemSet_Link-itemEl')]")
    public WebElement link_SidebarAttachDocument;

    @FindBy(xpath = "//td[@id='Contact:MenuLinks:Contact_ContactDocuments']")
    public WebElement link_SidebarDocuments;

    @FindBy(xpath = "//div[contains(@id,'Contact:ContactMenuActions:ContactActions:Transfer')]")
    public WebElement link_SidebarTransfer;

    @FindBy(xpath = "//div[contains(@id,'Contact:ContactMenuActions:ContactActions:Transfer:VendorTransfer')]")
    public WebElement link_SidebarTransferVendorTransfer;

    @FindBy(xpath = "//div[contains(@id,':NewActivityMenuItemSet_Category')]")
    public WebElement link_SidebarRequest;

    @FindBy(xpath = "//div[contains(@id,':NewActivityMenuItemSet_Category:0:item')]")
    public WebElement link_SidebarReviewAddress;

    @FindBy(xpath = "//div[contains(@id,':NewActivityMenuItemSet_Category:1:item')]")
    public WebElement link_SidebarReviewContact;

    @FindBy(xpath = "//div[contains(@id,'Contact:ContactMenuActions:ContactActions:SendActivity')]")
    public WebElement link_SendActivity;

    @FindBy(xpath = "//div[contains(@id,'Contact:ContactMenuActions:ContactActions:SendActivity:PolicyCenter')]")
    public WebElement button_ToPolicyCenter;

    @FindBy(xpath = "//div[contains(@id,'Contact:ContactMenuActions:ContactActions:Transfer:AccountTransfer')]")
    public WebElement link_SidebarTransferAcctTransfer;

    @FindBy(xpath = "//div[contains(@id,':ContactActions:Transfer:LienholderTransfer')]")
    public WebElement link_SidebarTransferLienTransfer;


    public void clickSidebarContactDetailsLink() {
        clickWhenClickable(link_SidebarContactDetails);
    }

    public void clickSidebarContactWorkplanLink() {
        clickWhenClickable(link_SidebarContactWorkplan);
    }

    public void clickSideBarAttachDocument() {
        clickWhenClickable(link_SidebarAttachDocument);
    }

    public void initiateAttachedDocument() {
        clickActions();
        clickSideBarAttachDocument();
    }

    public void clickSideBarDocuments() {
        clickWhenClickable(link_SidebarDocuments);
    }

    public void initiateTransfer() {
        clickActions();
        int lcv = 0;
        do {
            hoverOver(link_SidebarTransfer);
            clickWhenClickable(link_SidebarTransferVendorTransfer);
            lcv++;
        }
        while (!checkIfElementExists("//*[contains(@id,'ContactTransfer:TransferPanelSet:ValidContact-inputEl')]", 1000) && lcv < 100);
        
    }

    public void initiateAcctTransfer() {
        clickActions();
        int lcv = 0;
        do {
            hoverOver(link_SidebarTransfer);
            clickWhenClickable(link_SidebarTransferAcctTransfer);
            lcv++;
        }
        while (!checkIfElementExists("//*[contains(@id,'ContactTransfer:TransferPanelSet:ValidContact-inputEl')]", 1000) && lcv < 100);
        
    }

    public void initiateLienTransfer() {
        clickActions();
        int lcv = 0;
        do {
            hoverOver(link_SidebarTransfer);
            clickWhenClickable(link_SidebarTransferLienTransfer);
            lcv++;
        }
        while (!checkIfElementExists("//*[contains(@id,'ContactTransfer:TransferPanelSet:ValidContact-inputEl')]", 1000) && lcv < 100);
        
    }

    public void initiateReviewContactActivity(String reviewActivityType) {
        
        clickActions();
        
        clickWhenClickable(link_SidebarRequest);
        
        if (reviewActivityType.contains("Address")) {
            clickWhenClickable(link_SidebarReviewAddress);
        } else {
            clickWhenClickable(link_SidebarReviewContact);
        }
        
    }

    public void sendActivityToPC() {
        clickActions();
        int lcv = 0;
        do {
            hoverOver(link_SendActivity);
            clickWhenClickable(button_ToPolicyCenter);
            lcv++;
        } while (!checkIfElementExists("//*[contains(@id,'SendActivityPopup:Cancel-btnEl')]", 1000) && lcv < 100);
        

    }


}
