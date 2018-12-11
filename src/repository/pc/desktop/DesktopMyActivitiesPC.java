package repository.pc.desktop;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

import java.util.HashMap;
import java.util.List;

public class DesktopMyActivitiesPC extends BasePage {
	
	private TableUtils tableUtils;
	
    public DesktopMyActivitiesPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    @FindBy(xpath = "//input[contains(@id,'DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesLV:0:_Checkbox')]")
    private WebElement checkbox_DesktopMyActivitiesCheckbox;

    @FindBy(xpath = "//select[contains(@id,'DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesLV:activitiesFilter-inputE')]")
    private WebElement select_DesktopMyActivitiesStatusResultsFilter;

    @FindBy(linkText = "Assign")
    private WebElement button_DesktopMyActivitiesResultsAssign;

    @FindBy(xpath = "//a[@id='DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesLV_tb:DesktopActivities_SkipButton-btnInnerEl']")
    private WebElement button_DesktopMyActivitiesSkipComplete;

    @FindBy(xpath = "//a[@id='DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesLV_tb:DesktopActivities_CompleteButton-btnInnerEl']")
    private WebElement button_DesktopMyActivitiesResultsComplete;

    @FindBy(xpath = "//div//span[.='Due Date']/parent::div/parent::div")
    private WebElement columnHeader_MyActivitiesDueDate;


    @FindBy(xpath = "//div[contains(@id, 'DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesLV')]")
    private WebElement table_deskTopMyActivities;

    @FindBy(xpath = "//a[contains(@id, ':DesktopActivities_AssignButton')]")
    private WebElement link_Assign;

    @FindBy(xpath = "//input[contains(@id, 'AssignActivitiesPopup:AssignmentPopupScreen:AssignmentPopupDV:1-inputEl')]")
    private WebElement radio_FindUser;

    @FindBy(xpath = "//input[contains(@id, ':AssignmentSearchInputSet:Username-inputEl')]")
    private WebElement input_FindUserByUserName;

    @FindBy(xpath = "//a[contains(@id, ':SearchLinksInputSet:Search')]")
    private WebElement link_FindByUserSearch;

    @FindBy(xpath = "//a[contains(@id, 'AssignActivitiesPopup:AssignmentPopupScreen:AssignmentUserLV:0:_Select')]")
    private WebElement link_FindByUserSearchInitialAssign;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    public void clickDesktopMyActivitiesCheckboxFirst() {
        waitUntilElementIsClickable(checkbox_DesktopMyActivitiesCheckbox);
        checkbox_DesktopMyActivitiesCheckbox.click();
    }


    public void setDesktopMyActivitiesStatusResultsFilter(String statusToSelect) {
        waitUntilElementIsVisible(select_DesktopMyActivitiesStatusResultsFilter);
        new Select(select_DesktopMyActivitiesStatusResultsFilter).selectByVisibleText(statusToSelect);
    }


    public void clickDesktopMyActivitiesResultsAssign() {
        waitUntilElementIsVisible(button_DesktopMyActivitiesResultsAssign);
        waitUntilElementIsClickable(button_DesktopMyActivitiesResultsAssign);
        button_DesktopMyActivitiesResultsAssign.click();
    }


    public void clickDesktopMyActivitiesResultsComplete() {
        waitUntilElementIsClickable(button_DesktopMyActivitiesSkipComplete);
        button_DesktopMyActivitiesSkipComplete.click();
    }


    public void clickDesktopMyActivitiesResultsSkip() {
        waitUntilElementIsClickable(button_DesktopMyActivitiesResultsComplete);
        button_DesktopMyActivitiesResultsComplete.click();
    }


    public void searchMyActivitiesTableForSpecificSubjectLinkAndClick(String subjectEquals, String idContains, String accountHolderContains) throws Exception {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();

        columnRowKeyValuePairs.put("Subject", subjectEquals);
        if (idContains != null) {
            columnRowKeyValuePairs.put("ID", idContains);
        }
        if (accountHolderContains != null) {
            columnRowKeyValuePairs.put("Account Holder", accountHolderContains);
        }

        while (!columnHeader_MyActivitiesDueDate.getAttribute("class").contains("DESC")) {
            clickWhenClickable(columnHeader_MyActivitiesDueDate);
        }

        boolean found = false;

        if (tableUtils.hasMultiplePages(table_deskTopMyActivities)) {
            Integer numPages = tableUtils.getNumberOfTablePages(table_deskTopMyActivities);
            Integer pageCounter = 1;

            while ((pageCounter <= numPages) && (!found)) {
                tableUtils.setTablePageNumber(table_deskTopMyActivities, pageCounter);

                try {
                    tableUtils.clickLinkInTableByRowAndColumnName(table_deskTopMyActivities, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnsAndValues(table_deskTopMyActivities, columnRowKeyValuePairs)), "Subject");
                    found = true;
                } catch (Exception e) {
                    pageCounter++;
                }
            }
        } else {
            try {
                tableUtils.clickLinkInTableByRowAndColumnName(table_deskTopMyActivities, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnsAndValues(table_deskTopMyActivities, columnRowKeyValuePairs)), "Subject");
                found = true;
            } catch (Exception e) {

            }
        }

        if (!found) {
        	Assert.fail("No Result Was Found in the My Activities Pages When Searching for the Specific Activity: " + subjectEquals + " | " + idContains + " | " + accountHolderContains);
        }
    }


    public void AssignActivityBySearchingWithInsuredName(String accountHolderContains, String username) {
        while (!columnHeader_MyActivitiesDueDate.getAttribute("class").contains("DESC")) {
            clickWhenClickable(columnHeader_MyActivitiesDueDate);
        }

        int row = tableUtils.getRowNumberInTableByText(table_deskTopMyActivities, accountHolderContains);
        tableUtils.setCheckboxInTable(table_deskTopMyActivities, row, true);
        clickWhenClickable(link_Assign);
        waitUntilElementIsClickable(radio_FindUser);
        radio_FindUser.click();
        setText(input_FindUserByUserName, username);
        clickWhenClickable(link_FindByUserSearch);
        clickWhenClickable(link_FindByUserSearchInitialAssign);
    }


    public boolean findDesktopMyActivitiesResultsSubjectSpecific(String subjectEquals, String idContains,
                                                                 String accountHolderContains, String policyTypeEquals) {
        while (!columnHeader_MyActivitiesDueDate.getAttribute("class").contains("DESC")) {
            clickWhenClickable(columnHeader_MyActivitiesDueDate);
        }

        List<WebElement> link = finds(By
                .xpath("//div[contains(@id, 'DesktopActivities:DesktopActivitiesScreen:DesktopActivitiesLV-body')]/descendant::table/descendant::tr/child::td/div[contains(text(), '"
                        + accountHolderContains
                        + "')]/parent::td/preceding-sibling::td/child::div/child::a[contains(text(), '"
                        + subjectEquals + "')]"));
        return link.size() > 0;
    }
}
