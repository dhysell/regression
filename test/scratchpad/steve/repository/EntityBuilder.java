package scratchpad.steve.repository;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.driverConfiguration.DriverBuilder;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import repository.gw.servertools.ServerToolsSideBar;
import gwclockhelpers.ApplicationOrCenter;

public class EntityBuilder extends BasePage {

    private WebDriver driver;

    public EntityBuilder(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[contains(@id, 'PLEntityBuilder:policyCountText-inputEl')]")
    private WebElement input_EntityBuilderNumberOfPolicies;

    @FindBy(xpath = "//input[contains(@id, 'PLEntityBuilder:policyMemberText-inputEl')]")
    private WebElement input_EntityBuilderNumberOfMembers;

    private Guidewire8Checkbox checkBox_EntityBuilderPropertyLiability() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id,'PLEntityBuilder:IncludeLine1and2')]");
    }

    private Guidewire8Checkbox checkBox_EntityBuilderPersonalAuto() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id,'PLEntityBuilder:IncludeLine3')]");
    }

    private Guidewire8Checkbox checkBox_EntityBuilderInlandMarine() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id,'PLEntityBuilder:IncludeLine4')]");
    }

    private Guidewire8Checkbox checkBox_EntityBuilderApproveUWIssues() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id,'PLEntityBuilder:ApproveAndQuote')]");
    }

    @FindBy(xpath = "//span[contains(@id, 'PLEntityBuilder:BuildPLEntity-btnEl')]")
    private WebElement button_EntityBuilderBuildEntity;

    @FindBy(xpath = "//a[contains(@id, 'PLEntityBuilder:0:PolicyNumber')]")
    private WebElement link_EntityBuilderTransactionNumber;
   
    
    /*
     * *******************************************************************************************************************************************************************************
      									Helper Methods
     	
    *************************************************************************************************************************************************************************
    */

    private void setNumberOfPolicies(int numberOfPolicies) {
        if (numberOfPolicies >= 0) {
            waitUntilElementIsClickable(input_EntityBuilderNumberOfPolicies);
            input_EntityBuilderNumberOfPolicies.click();
            input_EntityBuilderNumberOfPolicies.sendKeys(Keys.chord(Keys.CONTROL + "a"));
            input_EntityBuilderNumberOfPolicies.sendKeys(Keys.DELETE);
            input_EntityBuilderNumberOfPolicies.sendKeys(numberOfPolicies + "");
            input_EntityBuilderNumberOfPolicies.sendKeys(Keys.TAB);
        }
    }

    private void setNumberOfMembers(int numberOfMembers) {
        if (numberOfMembers >= 0) {
            waitUntilElementIsClickable(input_EntityBuilderNumberOfMembers);
            input_EntityBuilderNumberOfMembers.click();
            input_EntityBuilderNumberOfMembers.sendKeys(Keys.chord(Keys.CONTROL + "a"));
            input_EntityBuilderNumberOfMembers.sendKeys(Keys.DELETE);
            input_EntityBuilderNumberOfMembers.sendKeys(numberOfMembers + "");
            input_EntityBuilderNumberOfMembers.sendKeys(Keys.TAB);
        }
    }

    private void setPropertyAndLiability() {
        waitUntilElementIsClickable(By.xpath("//table[contains(@id,'PLEntityBuilder:IncludeLine1and2')]"));
        checkBox_EntityBuilderPropertyLiability().select(true);
    }

    private void setPersonalAuto() {
        waitUntilElementIsClickable(By.xpath("//table[contains(@id,'PLEntityBuilder:IncludeLine3')]"));
        checkBox_EntityBuilderPersonalAuto().select(true);
    }

    private void setInlandMarine() {
        waitUntilElementIsClickable(By.xpath("//table[contains(@id,'PLEntityBuilder:IncludeLine4')]"));
        checkBox_EntityBuilderInlandMarine().select(true);
    }

    private void setApproveUWIssuesAndQuote() {
        waitUntilElementIsClickable(By.xpath("//table[contains(@id,'PLEntityBuilder:ApproveAndQuote')]"));
        checkBox_EntityBuilderApproveUWIssues().select(true);
    }

    private String clickBuildEntity() {
        waitUntilElementIsClickable(button_EntityBuilderBuildEntity);
        button_EntityBuilderBuildEntity.click();
        waitUntilElementIsClickable(link_EntityBuilderTransactionNumber);
        return link_EntityBuilderTransactionNumber.getText();
    }

    private void clickTransactionNumber() {
        waitUntilElementIsClickable(link_EntityBuilderTransactionNumber);
        link_EntityBuilderTransactionNumber.click();
    }

    public String buildEntity(int numberOfPolicies, int numberOfMembers, boolean section1And2, boolean section3, boolean section4, boolean uwApprove) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver1 = new DriverBuilder().buildGWWebDriver(cf);
        new Login(driver1).login("su", "gw");
        new GuidewireHelpers(driver1).pressAltShiftT();
        ServerToolsSideBar serverToolsSideBar = new ServerToolsSideBar(driver1);
        serverToolsSideBar.clickEntityBuilder();
        if (numberOfPolicies > 0) {
            setNumberOfPolicies(numberOfPolicies);
        }

        if (numberOfMembers > 0) {
            setNumberOfMembers(numberOfMembers);
        }

        if (section1And2) {
            setPropertyAndLiability();
        }

        if (section3) {
            setPersonalAuto();
        }

        if (section4) {
            setInlandMarine();
        }

        if (uwApprove) {
            setApproveUWIssuesAndQuote();
        }
        return clickBuildEntity();
    }

    public GeneratePolicy buildEntityGetAccountNumber(int numberOfPolicies, int numberOfMembers, boolean section1And2, boolean section3, boolean section4, boolean uwApprove) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver2 = new DriverBuilder().buildGWWebDriver(cf);
        new Login(driver2).login("su", "gw");
        new GuidewireHelpers(driver2).pressAltShiftT();
        ServerToolsSideBar serverToolsSideBar = new ServerToolsSideBar(driver2);
        serverToolsSideBar.clickEntityBuilder();
        if (numberOfPolicies > 0) {
            setNumberOfPolicies(numberOfPolicies);
        }

        if (numberOfMembers > 0) {
            setNumberOfMembers(numberOfMembers);
        }

        if (section1And2) {
            setPropertyAndLiability();
        }

        if (section3) {
            setPersonalAuto();
        }

        if (section4) {
            setInlandMarine();
        }

        if (uwApprove) {
            setApproveUWIssuesAndQuote();
        }

        String accountNumber = StringsUtils.getAccountNumberFromTransactionNumber(clickBuildEntity());
        clickTransactionNumber();

        GeneratePolicy policy = new GeneratePolicy(driver);
        policy.accountNumber = accountNumber;
        InfoBar info = new InfoBar(driver);
        policy.agentInfo = info.getAgent();

        return policy;


    }
}
