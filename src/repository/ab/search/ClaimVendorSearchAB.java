package repository.ab.search;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AbUsers;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;

public class ClaimVendorSearchAB extends SearchAB {

    private WebDriver driver;
    private TableUtils tableUtils;

    public ClaimVendorSearchAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(@id, 'ClaimVendorSearch:ClaimVendorSearchScreen:ttlBar')]")
    private WebElement text_ClaimVendorPageTitle;

    @FindBy(xpath = "//input[contains(@id, 'ClaimVendorSearch:ClaimVendorSearchScreen:ClaimVendorSearchDV:FBMSearchInputSet:GlobalContactNameInputSet:Name-inputEl')]")
    private WebElement editbox_ClaimVendorSearchName;

    private Guidewire8Select select_AdvancedSearchContactType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:ContactSubtype-triggerWrap')]");
    }

    @FindBy(xpath = "//div[@id = 'ClaimVendorSearch:ClaimVendorSearchScreen:1']")
    private WebElement div_ClaimVendorSearchResults;


    /*
     *
     * 						Methods
     *
     *
     *
     * */

    public boolean isVendorSearch() {
        if (checkIfElementExists(text_ClaimVendorPageTitle, 1000)) {
            return true;
        } else {
            return false;
        }
    }

    @FindBy(xpath = "//input[contains(@id,'ClaimVendorSearch:ClaimVendorSearchScreen:ClaimVendorSearchDV:FBMSearchInputSet:ClaimVendorStartingNumber-inputEl')]")
    private WebElement editbox_ClaimVendorSearchStartNumber;

    private void setVendorStartNum(String vendNum) {
        
        clickWhenClickable(editbox_ClaimVendorSearchStartNumber);
        editbox_ClaimVendorSearchStartNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ClaimVendorSearchStartNumber.sendKeys(Keys.DELETE);
        editbox_ClaimVendorSearchStartNumber.sendKeys(vendNum);
        editbox_ClaimVendorSearchStartNumber.sendKeys(Keys.TAB);
        

    }

    @FindBy(xpath = "//input[contains(@id,'ClaimVendorSearch:ClaimVendorSearchScreen:ClaimVendorSearchDV:FBMSearchInputSet:ClaimVendorEndingNumber-inputEl')]")
    private WebElement editbox_ClaimVendorSearchEndNumber;

    private void setVendorEndNum(String vendNum) {
        
        clickWhenClickable(editbox_ClaimVendorSearchEndNumber);
        editbox_ClaimVendorSearchEndNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ClaimVendorSearchEndNumber.sendKeys(Keys.DELETE);
        editbox_ClaimVendorSearchEndNumber.sendKeys(vendNum);
        editbox_ClaimVendorSearchEndNumber.sendKeys(Keys.TAB);
        
    }

    private void setVendorName(String name) {
        
        clickWhenClickable(editbox_ClaimVendorSearchName);
        editbox_ClaimVendorSearchName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ClaimVendorSearchName.sendKeys(Keys.DELETE);
        editbox_ClaimVendorSearchName.sendKeys(name);
        editbox_ClaimVendorSearchName.sendKeys(Keys.TAB);
    }

    public ContactDetailsBasicsAB searchClaimVendorSearch(String name) throws Exception {
        setVendorName(name);
        super.clickSearch();
        int rowCount = tableUtils.getRowCount(div_ClaimVendorSearchResults);
        //this generates a random number between 1 and the row count.
        int row = (int) (Math.random() * rowCount + 1);
        super.clickClaimVendorSearchResult(row);
        return new ContactDetailsBasicsAB(getDriver());
    }

    private ClaimVendorSearchAB getToVendorSearch(AbUsers user) throws Exception {
        Login lp = new Login(getDriver());
        lp.login(user.getUserName(), user.getUserPassword());
        TopMenuAB menu = new TopMenuAB(driver);
        menu.clickSearchTab();
        SidebarAB sidebarLinks = new SidebarAB(driver);
        sidebarLinks.clickClaimVendorSearch();
        return new ClaimVendorSearchAB(getDriver());
    }

    public ContactDetailsBasicsAB loginAndSearchVendor(AbUsers user, String vendorNum) throws Exception {
        getToVendorSearch(user);
        setVendorStartNum(vendorNum);
        setVendorEndNum(vendorNum);
        super.clickSearch();
        int rowCount = tableUtils.getRowCount(div_ClaimVendorSearchResults);
        //this generates a random number between 1 and the row count.
        int row = (int) (Math.random() * rowCount + 1);
        super.clickClaimVendorSearchResult(row);
        return new ContactDetailsBasicsAB(getDriver());
    }

    public ContactDetailsBasicsAB loginAndSearchForVendorWithRangeOfNums(AbUsers user, String vendorStartNum, String vendorEndNum) throws Exception {
        getToVendorSearch(user);
        setVendorStartNum(vendorStartNum);
        setVendorEndNum(vendorEndNum);
        super.clickSearch();
        int rowCount = tableUtils.getRowCount(div_ClaimVendorSearchResults);
        //this generates a random number between 1 and the row count.
        int row = (int) (Math.random() * rowCount + 1);
        super.clickClaimVendorSearchResult(row);
        return new ContactDetailsBasicsAB(getDriver());
    }
}
