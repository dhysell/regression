package repository.gw.infobar;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.ProductLineType;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.helpers.GuidewireHelpers;

public class InfoBar extends BasePage {
	
	private WebDriver driver;

    public InfoBar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    @FindBy(xpath = "//span[contains(@id, ':AccountNumber-btnInnerEl')]/child::span[contains(@class, 'infobar_elem_val')]")
    public WebElement text_InfoBarAccountNumber;

    @FindBy(xpath = "//div[@id='infoBar-innerCt']/div[@id='infoBar-targetEl']/a[contains(@id, ':AccountNumber')]")
    public WebElement link_InfoBarAccountNumber;

    @FindBy(xpath = "//span[contains(@id, ':PolicyNumber-btnInnerEl')]/child::span[contains(@class, 'infobar_elem_val')]")
    public WebElement text_InfoBarPolicyNumber;

    @FindBy(xpath = "//div[@id='infoBar-innerCt']/div[@id='infoBar-targetEl']/a[contains(@id, ':PolicyNumber')]")
    public WebElement link_InfoBarPolicyNumber;

    @FindBy(xpath = "//span[contains(@id, ':Agent-btnInnerEl')]/child::span[2]")
    public WebElement text_AgentName;

    @FindBy(xpath = "//span[contains(@id, ':Underwriter-btnInnerEl')]/child::span[2]")
    public WebElement text_UnderwriterName;

    @FindBy(xpath = "//span[contains(@id, 'InfoBar:JobLabel-btnInnerEl')]/span")
    public WebElement text_JobLabel;

    @FindBy(xpath = "//span[contains(@id, 'InfoBar:EditLock-btnInnerEl')]/span")
    public WebElement text_EditLockLabel;

    @FindBy(xpath = "//span[contains(@id, 'InfoBar:AccountName-btnInnerEl')]/span")
    public WebElement text_AccountNameLabel;

    @FindBy(xpath = "//span[contains(@id, 'InfoBar:LOBLabelAndIcon-btnInnerEl')]/span")
    public WebElement text_ProductLineType;


    public String getInfoBarJobLabel() {
        waitUntilElementIsVisible(text_JobLabel);
        return text_JobLabel.getText();
    }


    public boolean checkIfEditLockLabelExists() {
        boolean found = false;
        try {
            waitUntilElementIsVisible(text_EditLockLabel, 1000);
            found = true;
        } catch (Exception e) {
            systemOut("Edit Lock Label not found.");
        }
        return found;
    }


    public String getInfoBarEditLockValue() {
        waitUntilElementIsVisible(text_EditLockLabel);
        return text_EditLockLabel.getText();
    }


    public repository.gw.enums.ProductLineType getInfoBarProductLineType() {
        waitUntilElementIsVisible(text_ProductLineType);
        return ProductLineType.valueOf(text_ProductLineType.getText());
    }


    public String getInfoBarAccountName() {
        waitUntilElementIsVisible(text_AccountNameLabel);
        return text_AccountNameLabel.getText();
    }


    public String getInfoBarAccountNumber() {
        waitUntilElementIsVisible(text_InfoBarAccountNumber);
        String fullAccountString = text_InfoBarAccountNumber.getText();
        String accountNumber = null;
        if (new GuidewireHelpers(driver).getCurrentCenter().getName().equals("BC")) {
            String[] stringSplit = fullAccountString.split("#");
            accountNumber = stringSplit[1].substring(0, 6);
        } else {
            accountNumber = fullAccountString;
        }
        return accountNumber;
    }


    public String clickInfoBarAccountNumber() {
        String accountNumber = getInfoBarAccountNumber();
        clickWhenVisible(link_InfoBarAccountNumber);
        return accountNumber;
    }


    public boolean infoBarAccountNumberExists() {
        return checkIfElementExists(link_InfoBarAccountNumber, 2500);
    }


    public String getInfoBarPolicyNumber() {
        waitUntilElementIsVisible(text_InfoBarPolicyNumber);
        return text_InfoBarPolicyNumber.getText();
    }


    public String clickInfoBarPolicyNumber() throws GuidewireNavigationException {
        String policyNumber = getInfoBarPolicyNumber();
        clickWhenClickable(link_InfoBarPolicyNumber);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@class, 'g-title') and contains(text(), 'Summary')]", 10, "UNABLE TO GET TO POLCY SUMMARY SCREEN AFTER CLICKING POLICY NUMBER LINK.");
        return policyNumber;
    }


    public boolean infoBarPolicyNumberExists() {
        return checkIfElementExists(link_InfoBarPolicyNumber, 2500);
    }


    public Underwriters getUnderwriter() throws Exception {
        waitUntilElementIsVisible(text_UnderwriterName);
        return UnderwritersHelper.getUnderwriterInfoByFullName(text_UnderwriterName.getText());
    }


    public Agents getAgent() throws Exception {
        waitUntilElementIsVisible(text_AgentName);
        return AgentsHelper.getAgentByFullName(text_AgentName.getText());
    }
}





















