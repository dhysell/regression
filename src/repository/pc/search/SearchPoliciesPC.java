package repository.pc.search;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.topmenu.TopMenuPC;

import java.util.*;

public class SearchPoliciesPC extends BasePage {
	
	private TableUtils tableUtils;
	
    public SearchPoliciesPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    //////////////////////////////////
    // Recorded xPaths for Elements //
    //////////////////////////////////

    @FindBy(xpath = "//div[contains(@id, ':PolicySearch_ResultsLV') or contains(@id, ':PolicySearchResultsLV') or contains(@id, 'AccountFile_Summary_PolicyTermsLV')]")
	private WebElement table_SearchPoliciesPCSearchResults;

    @FindBy(xpath = "//input[contains(@id, ':Name-inputEl') or contains(@id, ':CompanyName-inputEl') or contains(@id, ':GlobalContactNameInputSet:Name-inputEl') or contains(@id, ':Keyword-inputEl')]")
    private WebElement editBox_SearchPoliciesPCCompanyName;
    
    @FindBy(xpath = "//input[contains(@id, ':FirstName-inputEl')]")
    private WebElement editBox_SearchPoliciesPCFirstName;
    
    @FindBy(xpath = "//input[contains(@id, ':LastName-inputEl') or contains(@id, ':Keyword-inputEl')]")
    private WebElement editBox_SearchPoliciesPCLastName;

    @FindBy(xpath = "//input[contains(@id, 'PolicySearchDV:AccountNumber-inputEl')]")
    private WebElement editbox_SearchPoliciesPCAccountNumber;

    @FindBy(xpath = "//input[contains(@id, 'PolicySearchDV:PolicyNumberCriterion-inputEl')]")
    private WebElement editbox_SearchPoliciesPCPolicyNumber;

    ////////////////////////////////////
    // Helper Methods for Interaction //
    ////////////////////////////////////

    private void setCompanyName(String nameToFill) {
		waitUntilElementIsClickable(editBox_SearchPoliciesPCCompanyName);
		editBox_SearchPoliciesPCCompanyName.sendKeys(Keys.chord(Keys.CONTROL + "a"), new StringsUtils().getUniqueName(nameToFill));
	}
    
    private void setFirstName(String nameToFill) {
		clickWhenClickable(editBox_SearchPoliciesPCFirstName);
		editBox_SearchPoliciesPCFirstName.sendKeys(Keys.chord(Keys.CONTROL + "a"), nameToFill);
	}
    
    private void setLastName(String nameToFill) {
		clickWhenClickable(editBox_SearchPoliciesPCLastName);
		editBox_SearchPoliciesPCLastName.sendKeys(Keys.chord(Keys.CONTROL + "a"), nameToFill);
	}

    public void setAccountAccountNumber(String accountNumber) {
        waitUntilElementIsClickable(editbox_SearchPoliciesPCAccountNumber);
        editbox_SearchPoliciesPCAccountNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SearchPoliciesPCAccountNumber.sendKeys(accountNumber);
    }

    public void setPolicyNumber(String policyNumber) {
        waitUntilElementIsClickable(editbox_SearchPoliciesPCPolicyNumber);
        editbox_SearchPoliciesPCPolicyNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        //Number includes hyphens
        if (policyNumber.length() == 12) {
            editbox_SearchPoliciesPCPolicyNumber.sendKeys(policyNumber);
            //Number does not include hyphens, but is a full policy number
        } else if (policyNumber.length() == 10){
            editbox_SearchPoliciesPCPolicyNumber.sendKeys(StringsUtils.formatPolicyNumber(policyNumber));
            //Number does not include the requisite number of digits, possibly missing policy term number.
        } else {
        	Assert.fail("Policy number does not include the proper amount of digits");
        }
    }

    public String selectFirstTableResult() {
		clickSearch();
		List<WebElement> tableResults = tableUtils.getAllTableRows(table_SearchPoliciesPCSearchResults);
		String policyNumber = "";
		if (tableResults.size() <= 0) {
			Assert.fail("No Policy Found, Sorry");
		} else {
			if (tableResults.size() == 1) {
				policyNumber = tableUtils.getCellTextInTableByRowAndColumnName(table_SearchPoliciesPCSearchResults, 1, "Policy #");
//				tableUtils.clickLinkInSpecficRowInTable(table_SearchPoliciesPCSearchResults, 1);
				tableUtils.clickLinkInTableByRowAndColumnName(table_SearchPoliciesPCSearchResults, 1, "Policy #");
			} else {
				tableUtils.sortByHeaderColumn(table_SearchPoliciesPCSearchResults, "Expiration Date");
				tableUtils.sortByHeaderColumn(table_SearchPoliciesPCSearchResults, "Expiration Date");
				policyNumber = tableUtils.getCellTextInTableByRowAndColumnName(table_SearchPoliciesPCSearchResults, 1, "Policy #");
//				tableUtils.clickLinkInSpecficRowInTable(table_SearchPoliciesPCSearchResults, 1);
				tableUtils.clickLinkInTableByRowAndColumnName(table_SearchPoliciesPCSearchResults, 1, "Policy #");
			}
		}
		return policyNumber;
	}

    private void getToPolicySearch() {
    	TopMenuPC topMenu = new TopMenuPC(getDriver());
        topMenu.clickSearchTab();
        
        repository.pc.search.SearchSidebarPC sidebar = new SearchSidebarPC(getDriver());
        sidebar.clickPolicies();
        clickReset();
    }

    // Selects the first policy in the results list and ends on the Policy Summary Page
    // policyNumber can be either format ########## or ##-######-##
    public String searchPolicyByPolicyNumber(String policyNumber) {
        getToPolicySearch();
        setPolicyNumber(policyNumber);
        return selectFirstTableResult();
    }

    public String searchPolicyByPolicyNumberAndTerm(String policyNumber, PolicyTermStatus policyTermStatus) throws Exception {
        getToPolicySearch();
        setPolicyNumber(policyNumber);

        clickSearch();
        List<WebElement> tableResults = tableUtils.getAllTableRows(table_SearchPoliciesPCSearchResults);
        if (tableResults.size() <= 0) {
            Assert.fail("No Policy Found, Sorry");
        } else {
            int tableRowToClick = -1;
            List<WebElement> possiblePolicies = tableUtils.getRowsInTableByColumnNameAndValue(table_SearchPoliciesPCSearchResults, "Status", policyTermStatus.getValue());
            if (possiblePolicies.size() == 1) {
                tableRowToClick = tableUtils.getRowNumberFromWebElementRow(possiblePolicies.get(0));
            } else {
                List<Date> expirationDateList = new ArrayList<Date>();
                for (WebElement row : possiblePolicies) {
                    expirationDateList.add(DateUtils.convertStringtoDate(tableUtils.getCellTextInTableByRowAndColumnName(table_SearchPoliciesPCSearchResults, tableUtils.getRowNumberFromWebElementRow(row), "Expiration Date"), "MM/dd/yyyy"));
                }
                Date latestDate = Collections.max(expirationDateList);
                HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();

                columnRowKeyValuePairs.put("Status", policyTermStatus.getValue());
                columnRowKeyValuePairs.put("Expiration Date", DateUtils.dateFormatAsString("MM/dd/yyyy", latestDate));

                tableRowToClick = tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnsAndValues(table_SearchPoliciesPCSearchResults, columnRowKeyValuePairs));
            }
            if (tableRowToClick == -1) {
            	Assert.fail("The search for a policy in the term status requested yielded no results.");
            } else {
                policyNumber = tableUtils.getCellTextInTableByRowAndColumnName(table_SearchPoliciesPCSearchResults, tableRowToClick, "Policy #");
                tableUtils.clickLinkInSpecficRowInTable(table_SearchPoliciesPCSearchResults, tableRowToClick);
            }
        }
        return policyNumber;
    }


    public String searchPolicyByAccountNumber(String accountNumber) {
		getToPolicySearch();
		setAccountAccountNumber(accountNumber);
		String policyNumber = selectFirstTableResult();
		return policyNumber;
	}

    public void searchPolicyByCompanyName(String companyName) {
		getToPolicySearch();
		setCompanyName(companyName);
		selectFirstTableResult();
	}

    public void searchPolicyByFirstLastName(String firstName, String lastName) {
		getToPolicySearch();
		setFirstName(firstName);
		setLastName(lastName);
		selectFirstTableResult();
	}
}
