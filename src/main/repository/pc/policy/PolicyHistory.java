package repository.pc.policy;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

import java.util.HashMap;

public class PolicyHistory extends BasePage {

    public PolicyHistory(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[contains(@id,':HistoryScreenResultsLV')]")
    private WebElement table_HistoryItems;


    public String historyItemExists(HashMap<String, String> columnRowKeyValuePairs) {
        WebElement row = new TableUtils(getDriver()).getRowInTableByColumnsAndValues(table_HistoryItems, columnRowKeyValuePairs);
        return row.getText();
    }

}
