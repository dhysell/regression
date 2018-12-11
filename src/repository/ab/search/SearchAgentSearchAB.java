package repository.ab.search;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import repository.ab.contact.AgentDetailsAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.ab.topmenu.TopMenuSearchAB;
import repository.gw.login.Login;

import java.util.List;

public class SearchAgentSearchAB extends SearchAB {

    private WebDriver driver;

    public SearchAgentSearchAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//span[contains(@id, 'AgentSearch:AgentSearchScreen:ttlBar')]")
    public WebElement text_SearchAgentSearchPageTitle;

    @FindBy(xpath = "//input[contains(@id, ':AgentSearchScreen:AgentSearchDV:FBMSearchInputSet:AgentNumber-inputEl')]")
    public WebElement editbox_SearchAgentSearchAgentNumber;

    @FindBy(xpath = "//input[contains(@id, ':AgentSearchScreen:AgentSearchDV:FBMSearchInputSet:GlobalPersonNameInputSet:LastName-inputEl')]")
    public WebElement editbox_SearchAgentSearchLastName;

    @FindBy(xpath = "//input[contains(@id, ':AgentSearchScreen:AgentSearchDV:FBMSearchInputSet:GlobalPersonNameInputSet:FirstName-inputEl')]")
    public WebElement editbox_SearchAgentSearchFirstName;

    @FindBy(xpath = "//input[contains(@id, ':AgentSearchScreen:AgentSearchDV:FBMSearchInputSet:MiddleName-inputEl')]")
    public WebElement editbox_SearchAgentSearchMiddleName;

    @FindBy(xpath = "//input[contains(@id, ':AgentSearchScreen:AgentSearchDV:FBMSearchInputSet:AlternateName-inputEl')]")
    public WebElement editbox_SearchAgentSearchAlternateName;

    @FindBy(xpath = "//a[contains(@id, ':AgentSearchScreen:AgentSearchDV:Search')]")
    public WebElement button_SearchAgentSearchSearch;

    @FindBy(xpath = "//a[contains(@id, ':AgentSearchScreen:AgentSearchDV:Reset')]")
    public WebElement button_SearchAgentSearchReset;

    public List<WebElement> link_SearchAgentSearchAgent(String name, String agentNum) {
        return finds(
                By.xpath("//div[contains(., '" + agentNum + "')]/ancestor::tr[1]/td/div/a[contains(., '" + name + "')]"));
    }

    public WebElement link_SearchAgentSearchAgent(String agentNum, String lastName, String firstName) {
        return find(By.xpath("//a[contains(., '" + agentNum + "')]/ancestor::td[1]/following-sibling::td/div/a[contains(., '" + lastName + "') and contains(., '" + firstName + "')]"));
    }

    public WebElement link_SearchAgentSearchAgentWithPreferredName(String agentNum, String preferredName) {
        return find(By.xpath("//a[contains(., '" + agentNum + "')]/ancestor::td[1]/following-sibling::td/div/a[contains(., '" + preferredName + "')]"));
    }

    public WebElement link_SearchAgentSearchSelect(String lastName, String agentNum) {
        String xpathToRun = "//a[contains(., '" + lastName + "')]/ancestor::tr/td/div[contains(., '" + agentNum
                + "')]/ancestor::tr/td/div/a[contains(@id, 'Select')]";
        System.out.println(xpathToRun);
        waitForPostBack();
        return find(By.xpath(xpathToRun));
    }

    // -------------------------------------
    // Helper Methods for Above Elements
    // -------------------------------------

    public ContactDetailsBasicsAB loginAndSearchAgent(AbUsers abUser, Agents agent) {
        new Login(getDriver()).login(abUser.getUserName(), abUser.getUserPassword());
        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();
        
        SidebarAB sideMenu = new SidebarAB(driver);
        sideMenu.clickSidebarAgentSearchLink();
        return searchAgentContact(agent);
    }

    public boolean isAgentSearch() {
        
        return checkIfElementExists(text_SearchAgentSearchPageTitle, 1000);
    }

    public void setSearchAgentSearchAgentNumber(String agentNumberToSearch) {
        
        waitUntilElementIsVisible(editbox_SearchAgentSearchAgentNumber);
        editbox_SearchAgentSearchAgentNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SearchAgentSearchAgentNumber.sendKeys(Keys.DELETE);
        editbox_SearchAgentSearchAgentNumber.sendKeys(agentNumberToSearch);
        editbox_SearchAgentSearchAgentNumber.sendKeys(Keys.TAB);
        
    }

    public void setAgentName(String fullName) {

        waitUntilElementIsVisible(editbox_SearchAgentSearchLastName);
        String[] parsedName = fullName.split(" ");
        editbox_SearchAgentSearchLastName.sendKeys(parsedName[1]);

        waitUntilElementIsVisible(editbox_SearchAgentSearchFirstName);
        editbox_SearchAgentSearchFirstName.sendKeys(parsedName[0]);
    }

    public void clickSearchAgentSearchSearch() {
        waitUntilElementIsClickable(button_SearchAgentSearchSearch);
        button_SearchAgentSearchSearch.click();
        
    }

    public void clickSearchAgentSearchReset() {
        waitUntilElementIsClickable(button_SearchAgentSearchReset);
        button_SearchAgentSearchReset.click();
        
    }

    public boolean clickSearchAgentSearchAgentLink(String name, String agentNum) {
        System.out.println("The agent info we are searching for is " + name + " and " + agentNum + ".");
        if (link_SearchAgentSearchAgent(name, agentNum).size() > 0) {
            clickWhenVisible(link_SearchAgentSearchAgent(name, agentNum).get(0));
            return true;
        } else {
            return false;
        }
    }

    public void clickAgentLink(String agentNum, String firstName, String lastName) {
        clickWhenClickable(link_SearchAgentSearchAgent(agentNum, lastName, firstName));
    }

    public void clickAgentLinkWithPreferredName(String agentNum, String preferredName) {
        clickWhenClickable(link_SearchAgentSearchAgentWithPreferredName(agentNum, preferredName));
    }

    public void clickSearchAgentSearchAgentSelectLink(String lastName, String agentNum) {
        clickWhenClickable(link_SearchAgentSearchSelect(lastName, agentNum));
        
    }

    public void clickSearchDetailsButton(String lastName, String agentNum) {
        super.clickSearchDetailsButton(lastName, agentNum);

    }

    public ContactDetailsBasicsAB searchAgentContact(Agents agent) {
        setSearchAgentSearchAgentNumber(agent.getAgentNum());
        clickSearchAgentSearchSearch();
        clickSearchAgentSearchAgentLink(agent.getAgentPreferredName(), agent.getAgentNum());
        return new ContactDetailsBasicsAB(driver);
    }

    public AgentDetailsAB searchAgentDetails(String agentNum, String agentLastName) {
        setSearchAgentSearchAgentNumber(agentNum);
        clickSearchAgentSearchSearch();
        clickSearchDetailsButton(agentLastName, agentNum);
        return new AgentDetailsAB(driver);
    }

    public ContactDetailsBasicsAB loginAndSearchAgentByAgentNumber(AbUsers abUser, String agentNum, String lastName) {
        Login logMeIn = new Login(getDriver());
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());
        
        TopMenuSearchAB menu = new TopMenuSearchAB(driver);
        menu.clickSearch();
        SidebarAB sidebar = new SidebarAB(driver);
        sidebar.clickSidebarAgentSearchLink();

        SearchAgentSearchAB searchPage = new SearchAgentSearchAB(driver);
        searchPage.setSearchAgentSearchAgentNumber(agentNum);
        searchPage.clickSearchAgentSearchSearch();
        searchPage.clickSearchAgentSearchAgentLink(lastName, agentNum);
        return new ContactDetailsBasicsAB(driver);
    }

}
