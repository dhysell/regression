package repository.cc.claim.searchpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;


/**
 * @author iclouser
 * The main class will contain all the side menu links. It defualts to simple search simple search on initial pageload.
 * Using the publicly available methods will return the Interface of the page it goes to.
 * @Date 4/14/2016
 */
public class SearchPageCC extends BasePage {

    private WebDriver driver;

    public SearchPageCC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        waitUntilElementIsClickable(By.xpath("//span[text()='Advanced Search']"));
        PageFactory.initElements(driver, this);
    }

    // Elements

    @FindBy(xpath = "//span[text()='Simple Search']")
    private WebElement link_SimpleSearch;

    @FindBy(xpath = "//span[text()='Advanced Search']")
    private WebElement link_AdvancedSearch;

    @FindBy(xpath = "//span[text()='Recently Viewed']")
    private WebElement link_RecentlyViewed;

    @FindBy(xpath = "//span[text()='Medicare Section 111']")
    private WebElement link_MedicareSearc111;

    @FindBy(xpath = "//span[text()='Activities']")
    public WebElement link_ActivitySearch;

    @FindBy(xpath = "//span[text()='Checks']")
    private WebElement link_CheckSearch;

    // Helper Functions 

    public repository.cc.claim.searchpages.MedicareSearchsCC clickMedicareSearch111() {
        clickWhenClickable(link_MedicareSearc111);
        return new MedicareSearchsCC(this.driver);
    }

    public SimpleSearch clickSimpleSearch() {
        clickWhenClickable(link_SimpleSearch);
        return new SimpleSearch(this.driver);
    }

    public repository.cc.claim.searchpages.SearchChecksCC clickCheckSearch() {
        clickWhenClickable(link_CheckSearch);
        return new SearchChecksCC(this.driver);
    }

    public repository.cc.claim.searchpages.AdvancedSearchCC clickAdvancedSearch() {
        clickWhenClickable(link_AdvancedSearch);
        return new AdvancedSearchCC(this.driver);
    }
}
