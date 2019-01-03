package repository.gw.login;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.pc.search.SearchAccountsPC;
import repository.pc.search.SearchOtherJobsPC;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.search.SearchSubmissionsPC;

public class Login extends BasePage {
    private WebDriver driver;
    private Logger logger;

    public Login(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.logger = LoggerFactory.getLogger(this.getClass());
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    @FindBy(xpath = "//input[starts-with(@id,'Login:LoginScreen:LoginDV:username-inputEl')]")
    private WebElement editbox_LoginUsername;

    @FindBy(xpath = "//input[starts-with(@id,'Login:LoginScreen:LoginDV:password-inputEl')]")
    private WebElement editbox_LoginPassword;

    @FindBy(xpath = "//a[starts-with(@id,'Login:LoginScreen:LoginDV:submit')]")
    private WebElement button_LoginLogin;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    private void setLoginUserName(String userName) {
    	waitUntilElementIsClickable(editbox_LoginUsername);
    	editbox_LoginUsername.sendKeys(Keys.chord(Keys.CONTROL + "a"), userName);
        logger.info("USERNAME LOGGED IN / TRIED TO LOG IN: " + userName);
    }

    private void setLoginPassword(String password) {
    	waitUntilElementIsClickable(editbox_LoginPassword);
    	editbox_LoginPassword.sendKeys(Keys.chord(Keys.CONTROL + "a"), password);
        logger.info("PASSWORD USED TO LOG IN: " + password);
    }

    private void clickLoginLogin(WebDriver driver) {
        clickWhenClickable(button_LoginLogin);
    }

    public void login(String userName, String password) {
        setLoginUserName(userName);
        setLoginPassword(password.trim());
        clickLoginLogin(driver);
        waitForPostBack();
    }
    
    public Agents loginAsRandomAgent() throws Exception {
    	Agents agentInfo = AgentsHelper.getRandomAgent();
        new Login(driver).login(agentInfo.getAgentUserName(), agentInfo.getAgentPassword());
    	return agentInfo;
    }

    public WebElement getLoginUsername() {
        return editbox_LoginUsername;
    }

    public boolean accountLocked() {
        return finds(By.xpath("//label[contains(text(), 'account has been locked')]")).size() > 0;
    }

    /**
     * Logs in and searches for a policy using the account number. Works for PC and BC
     *
     * @param userName      Name of the user
     * @param password      Password of the user
     * @param accountNumber Used to search for a policy
     * @throws Exception
     */
    public void loginAndSearchPolicyByAccountNumber(String userName, String password, String accountNumber) {
        login(userName, password);
        if (accountLocked()) {
            Assert.fail("ACCOUNT FOR " + userName + " WAS LOCKED. FAILED TO LOGIN");
        }
        switch (new GuidewireHelpers(driver).getCurrentCenter()) {

            case PolicyCenter:
                SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
                policySearchPC.searchPolicyByAccountNumber(accountNumber);
                waitUntilElementIsClickable(By.cssSelector("div[id$='Policy_Summary_AccountDV:Number-inputEl']"));
                break;
            case BillingCenter:
                BCSearchPolicies policySearchBC = new BCSearchPolicies(driver);
                policySearchBC.searchPolicyByAccountNumber(accountNumber);
                waitUntilElementIsClickable(By.cssSelector("div[id$='PolicyDetailDV:PolicyNumber-inputEl']"));
            default:
                break;
        }
    }

    /**
     * Logs in and searches for a policy using the account number. Works for PC and BC
     *
     * @param policy     Generate Policy Object
     */
    public void loginAndSearchPolicyByAccountNumber(GeneratePolicy policy) {
        loginAndSearchPolicyByAccountNumber(policy.agentInfo.getAgentUserName(), policy.agentInfo.getAgentPassword(), policy.accountNumber);
    }

    /**
     * Logs in and searches for a policy using the policy number. Works for PC and BC
     *
     * @param userName     Name of the user
     * @param password     Password of the user
     * @param policyNumber Used to search for a policy
     */
    public void loginAndSearchPolicyByPolicyNumber(String userName, String password, String policyNumber) {
        login(userName, password);
        if (accountLocked()) {
            Assert.fail("ACCOUNT FOR " + userName + " WAS LOCKED. FAILED TO LOGIN");
        }
        // search for policy
        switch (new GuidewireHelpers(driver).getCurrentCenter()) {
            case PolicyCenter:
                SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
                policySearchPC.searchPolicyByPolicyNumber(policyNumber);
                break;
            case BillingCenter:

                BCSearchPolicies bcSearchPolicies = new BCSearchPolicies(driver);
                try {
                    bcSearchPolicies.searchPolicyByPolicyNumber(policyNumber);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            default:
                break;
        }
        waitForPostBack();
    }

    public void loginAndSearchPolicy_asAgent(GeneratePolicy policy) {
        loginAndSearchPolicyByPolicyNumber(policy.agentInfo.agentUserName, policy.agentInfo.agentPassword, setPolicyNumber(policy));
    }

    private String setPolicyNumber(GeneratePolicy policy) {
        String policyNumber = "";
        switch (policy.productType) {
            case Businessowners:
                policyNumber = policy.busOwnLine.getPolicyNumber();
                break;
            case CPP:
                policyNumber = policy.commercialPackage.getPolicyNumber();
                break;
            case Membership:
                policyNumber = policy.membership.getPolicyNumber();
                break;
            case PersonalUmbrella:
                policyNumber = policy.squireUmbrellaInfo.getPolicyNumber();
                break;
            case Squire:
                policyNumber = policy.squire.getPolicyNumber();
                break;
            case StandardFire:
                policyNumber = policy.standardFire.getPolicyNumber();
                break;
            case StandardIM:
                policyNumber = policy.standardInlandMarine.getPolicyNumber();
                break;
            case StandardLiability:
                policyNumber = policy.standardLiability.getPolicyNumber();
                break;
        }
        return policyNumber;
    }

    public void loginAndSearchPolicy_asUW(GeneratePolicy policy) {
        loginAndSearchPolicyByPolicyNumber(policy.underwriterInfo.getUnderwriterUserName(), policy.underwriterInfo.getUnderwriterPassword(), setPolicyNumber(policy));
    }

    /**
     * Logs in to PC and searches for a policy using the policy number, and then selects the term specified.
     *
     * @param userName         Name of the user
     * @param password         Password of the user
     * @param policyNumber     Used to search for a policy
     * @param policyTermStatus Term status Used to search for a policy
     */
    public void loginToPCAndSearchPolicyByPolicyNumberAndTerm(String userName, String password, String policyNumber, PolicyTermStatus policyTermStatus) {
        login(userName, password);
        if (accountLocked()) {
            Assert.fail("ACCOUNT FOR " + userName + " WAS LOCKED. FAILED TO LOGIN");
        }

        // search for policy
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        try {
            policySearchPC.searchPolicyByPolicyNumberAndTerm(policyNumber, policyTermStatus);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("There was an error searching for the policy and term. Please investigate.");
        }
        waitForPostBack();
    }

    /**
     * Logs in and searches for an account. Works for PC and BC
     *
     * @param userName      Name of the user
     * @param password      Password of the user
     * @param accountNumber Used to search for an account
     * @throws Exception
     */
    public void loginAndSearchAccountByAccountNumber(String userName, String password, String accountNumber) {
        login(userName, password);
        if (accountLocked()) {
            Assert.fail("ACCOUNT FOR " + userName + " WAS LOCKED. FAILED TO LOGIN");
        }
        // search for account
        switch (new GuidewireHelpers(driver).getCurrentCenter()) {
            case PolicyCenter:
                SearchAccountsPC accountSearchPC = new SearchAccountsPC(driver);
                accountSearchPC.searchAccountByAccountNumber(accountNumber);
                break;
            case BillingCenter:
                BCSearchAccounts accountSearchBC = new BCSearchAccounts(driver);
                accountSearchBC.searchAccountByAccountNumber(accountNumber);
                break;
            default:
                break;
        }
        waitForPostBack();
    }

    /**
     * Logs in and searches for a job using the account number. Works for PC and BC
     *
     * @param userName      Name of the user
     * @param password      Password of the user
     * @param accountNumber Used to search for a transaction
     * @throws Exception
     */
    public void loginAndSearchJob(String userName, String password, String accountNumber) {
        login(userName, password);
        if (accountLocked()) {
            Assert.fail("ACCOUNT FOR " + userName + " WAS LOCKED. FAILED TO LOGIN");
        }

        // search for transaction
        switch (new GuidewireHelpers(driver).getCurrentCenter()) {
            case PolicyCenter:
                SearchOtherJobsPC jobSearchPC = new SearchOtherJobsPC(driver);
                jobSearchPC.searchJobByAccountNumber(accountNumber, "001");
                break;
            default:
                break;
        }
    }

    /**
     * This method logs into a center (you must have already navigated to the page and be on the login screen first.)
     * and searches for an account, then clicks on the latest submission workorder.
     *
     * @param userName      Username to login as.
     * @param password      Password to use to login.
     * @param accountNumber Account number to search.
     */
    public void loginAndSearchSubmission(String userName, String password, String accountNumber) {
        login(userName, password);
        if (accountLocked()) {
            Assert.fail("ACCOUNT FOR " + userName + " WAS LOCKED. FAILED TO LOGIN");
        }
        SearchSubmissionsPC searchSubmission = new SearchSubmissionsPC(driver);
        searchSubmission.searchAndSelectSubmission(accountNumber);
    }

    // jlarsen 4/19/2016
    // logs into center and searches latest submission workorder.
    //pass in entire policy cus i'm lazy and don't want to type it all out anymore
    public String loginAndSearchSubmission(GeneratePolicy policy) {
        login(policy.agentInfo.getAgentUserName(), policy.agentInfo.getAgentPassword());
        if (accountLocked()) {
            Assert.fail("ACCOUNT FOR " + policy.agentInfo.getAgentUserName() + " WAS LOCKED. FAILED TO LOGIN");
        }
        SearchSubmissionsPC searchSubmission = new SearchSubmissionsPC(driver);
        return searchSubmission.searchSubmission(policy);
    }

    public void switchUserToUW(Underwriter.UnderwriterLine uwLine) throws Exception {
        new GuidewireHelpers(driver).logout();
        Underwriters randomUW = UnderwritersHelper.getRandomUnderwriter(uwLine);
        login(randomUW.getUnderwriterUserName(), randomUW.getUnderwriterPassword());
    }
}
