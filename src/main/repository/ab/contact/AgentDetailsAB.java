package repository.ab.contact;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;

public class AgentDetailsAB extends BasePage    {

    public AgentDetailsAB(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[contains(@id, 'ViewAgentDetailPopup:__crumb__')]")
    private WebElement link_AgentDetailsReturnToAgentSearch;

    @FindBy(xpath = "//div[contains(@id, 'ViewAgentDetailPopup:AgencyManager-inputEl')]")
    private WebElement link_AgentDetailsAgentNumber;

    @FindBy(xpath = "//div[contains(@id, ':WorkAddress-inputEl')]")
    private WebElement text_AgentDetailsWorkAddress;

    @FindBy(xpath = "//div[contains(@id, ':County-inputEl')]")
    private WebElement link_AgentDetailsAgentCounty;

    @FindBy(xpath = "//div[contains(@id, ':Agency-inputEl')]")
    private WebElement link_AgentDetailsAgentAgency;

    @FindBy(xpath = "//div[contains(@id, 'ViewAgentDetailPopup:Manager-inputEl')]")
    private WebElement link_AgentDetailsAgentsRegionalManager;

    @FindBy(xpath = "//div[contains(@id, 'ViewAgentDetailPopup:AgentContactsLV')]")
    private WebElement tableDiv_AgentDetailsCustomerTable;

    @FindBy(xpath = "//span[contains(@id, ':AgentContactsLV_tb:ViewAgentDetails_PrintButton-btnEl')]")
    private WebElement button_AgentDetailsPrint;


    /*
        public WebElement button_SearchResultsDetails(String lastName, String agentNum) {
            return find(By.xpath("//a[contains(., '"+ agentNum +"')]/ancestor::td/following-sibling::td/div/a[contains(., '"+ lastName +"')]/ancestor::td/following-sibling::td/div/a[contains(., 'Details')]"));

        }

        public WebElement link_AgentDetails(String agentNum) {
            return find(By.xpath("//div[contains(., '"+ agentNum +"')]"));
        }

    */
    private String getAgentNumber() {
        return link_AgentDetailsAgentNumber.getText();
    }

    public void clickPrint() {
        clickWhenClickable(button_AgentDetailsPrint);
    }

    public ArrayList<String> getCustomerAges() {
        return new TableUtils(getDriver()).getAllCellTextFromSpecificColumn(tableDiv_AgentDetailsCustomerTable, "Age");
    }

    private void clickAgentNumber() {
        clickWhenClickable(link_AgentDetailsAgentNumber);
    }

    public boolean contactAssociated() {
        waitUntilElementIsVisible(tableDiv_AgentDetailsCustomerTable);
        if (new TableUtils(getDriver()).getRowCount(tableDiv_AgentDetailsCustomerTable) >= 1) {
            clickWhenClickable(link_AgentDetailsReturnToAgentSearch);
            selectOKOrCancelFromPopup(OkCancel.OK);
            return true;
        } else {
            clickWhenClickable(link_AgentDetailsReturnToAgentSearch);
            selectOKOrCancelFromPopup(OkCancel.OK);
            return false;
        }
    }

}
