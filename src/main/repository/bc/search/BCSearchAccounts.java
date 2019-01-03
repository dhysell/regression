package repository.bc.search;

import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.bc.account.summary.BCAccountSummary;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AccountType;
import repository.gw.enums.DelinquencyStatus;
import repository.gw.enums.PolicyCompany;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.topmenu.TopMenuPC;

import java.util.ArrayList;
import java.util.List;

public class BCSearchAccounts extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public BCSearchAccounts(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);

    }

    // ****************** elements ************************************//

    @FindBy(xpath = "//input[contains(@id, ':AccountNumberCriterion-inputEl')]")
    private WebElement editbox_BCSearchAccountsAccountNumber;

    @FindBy(xpath = "//input[contains(@id, 'AccountNameCriterion-inputEl')]")
    private WebElement editbox_BCSearchAccountsAccountName;

    @FindBy(xpath = "//input[contains(@id, ':PolicyNumberCriterion-inputEl')]")
    private WebElement editBox_BCSearchAccountsPolicyNumber;

    Guidewire8Select select_BCSearchAccountsStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':AccountSearchDV:DelinquencyStatusCriterion-triggerWrap')]");
    }

    Guidewire8Select select_BCSearchAccountsType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':AccountSearchDV:AccountTypeCriterion-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':Name-inputEl')]")
    private WebElement editBox_BCSearchAccountsCompanyName;

    @FindBy(xpath = "//input[contains(@id, ':FirstName-inputEl')]")
    private WebElement editBox_BCSearchAccountsFirstName;

    @FindBy(xpath = "//input[contains(@id, ':LastName-inputEl')]")
    private WebElement editBox_BCSearchAccountsLastName;

    @FindBy(xpath = "//input[contains(@id, ':City-inputEl')]")
    private WebElement editBox_BCSearchAccountsCity;

    private Guidewire8Select select_BCSearchAccountsState() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':PostalCode-inputEl')]")
    private WebElement editBox_BCSearchAccountsZip;

    @FindBy(xpath = "//div[@id='AccountSearch:AccountSearchScreen:AccountSearchResultsLV']")
    public WebElement table_BCSearchAccountsSearchResultsTable;

    @FindBy(xpath = "//a[@id='SelectMultipleAccountsPopup:SelectMultipleAccountsScreen:AccountSearchDV_tb:addbutton' or contains(text(),'Select')]")
    public WebElement button_BCSearchAccountsSelectAccountButton;


    // ****************** helper methods ************************************//

    public void setBCSearchAccountsAccountNumber(String accountNumber) {
        clickReset();
        clickWhenClickable(editbox_BCSearchAccountsAccountNumber);
        editbox_BCSearchAccountsAccountNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"), accountNumber);
    }

    public void setBCSearchAccountsAccountNumberWithoutResetting(String accountNumber) {
        clickWhenClickable(editbox_BCSearchAccountsAccountNumber);
        editbox_BCSearchAccountsAccountNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"), accountNumber);
    }

    public void setBCSearchAccountsAccountName(String accountName) {
        clickWhenClickable(editbox_BCSearchAccountsAccountName);
        editbox_BCSearchAccountsAccountName.sendKeys(accountName);
    }

    public void setBCSearchAccountsPolicyNumber(String policyNumber) {
        waitUntilElementIsClickable(editBox_BCSearchAccountsPolicyNumber);
        editBox_BCSearchAccountsPolicyNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        if (policyNumber.length() >= 12) {
            editBox_BCSearchAccountsPolicyNumber.sendKeys(policyNumber);
        } else {
            editBox_BCSearchAccountsPolicyNumber.sendKeys(StringsUtils.formatPolicyNumber(policyNumber));
        }
    }

    public void setBCSearchAccountsDelinquencyStatus(DelinquencyStatus status) {
        select_BCSearchAccountsStatus().selectByVisibleText(status.getValue());
    }

    public void setBCSearchAccountsType(AccountType type) {
        select_BCSearchAccountsType().selectByVisibleText(type.getValue());
    }

    public void setBCSearchAccountsCompanyName(String companyName) {
        waitUntilElementIsClickable(editBox_BCSearchAccountsCompanyName);
        if (companyName.length() > 30) {
            editBox_BCSearchAccountsCompanyName.sendKeys(Keys.chord(Keys.CONTROL + "a"), companyName.substring(0, 29));
        } else {
            editBox_BCSearchAccountsCompanyName.sendKeys(Keys.chord(Keys.CONTROL + "a"), companyName);
        }
    }

    public void setBCSearchAccountsFirstName(String firstName) {
        clickWhenClickable(editBox_BCSearchAccountsFirstName);
        editBox_BCSearchAccountsFirstName.sendKeys(Keys.chord(Keys.CONTROL + "a"), firstName);
    }

    public void setBCSearchAccountsLastName(String lastName) {
        clickWhenClickable(editBox_BCSearchAccountsLastName);
        editBox_BCSearchAccountsLastName.sendKeys(Keys.chord(Keys.CONTROL + "a"), lastName);
    }

    public void setBCSearchAccountsCity(String city) {
        clickWhenClickable(editBox_BCSearchAccountsCity);
        editBox_BCSearchAccountsCity.sendKeys(Keys.chord(Keys.CONTROL + "a"), city);
    }

    public void setBCSearchAccountsState(State stateToSelect) {
        Guidewire8Select bcSearchAccountsState = select_BCSearchAccountsState();
        bcSearchAccountsState.selectByVisibleText(stateToSelect.getName());
    }

    public void setBCSearchAccountsZip(String zipPostalCode) {
        clickWhenClickable(editBox_BCSearchAccountsZip);
        editBox_BCSearchAccountsZip.sendKeys(Keys.chord(Keys.CONTROL + "a"), zipPostalCode);
    }


    public String getSearchAccountTableCompanyName(String accountNumber) {
        return new TableUtils(driver).getCellTextInTableByRowAndColumnName(table_BCSearchAccountsSearchResultsTable, new TableUtils(driver).getRowInTableByColumnNameAndValue(table_BCSearchAccountsSearchResultsTable, "Account #", accountNumber), "Account Name");
    }

    public int getSearchTableRowCount() {
        return new TableUtils(driver).getRowCount(table_BCSearchAccountsSearchResultsTable);
    }

    public void clickAccountNumberInRow(int rowNumber) {
        new TableUtils(driver).clickLinkInSpecficRowInTable(table_BCSearchAccountsSearchResultsTable, rowNumber);
    }
    
    public String getSearchAccountTableAccountNumber(int rowNumber) {
        return new TableUtils(driver).getCellTextInTableByRowAndColumnName(table_BCSearchAccountsSearchResultsTable, rowNumber, "Account Name");
    }

    public void clickAccountNumber(String accountNumber) {
        new TableUtils(driver).clickLinkInTable(table_BCSearchAccountsSearchResultsTable, accountNumber);
    }

    public void setAccountNumberCheckboxInResultsTable(String accountNumber) {
        new TableUtils(driver).setCheckboxInTableByText(table_BCSearchAccountsSearchResultsTable, accountNumber, true);
    }

    public void clickSelectAccountsButton() {
        clickWhenClickable(button_BCSearchAccountsSelectAccountButton);
    }

    public void sortSearchTableByCreationDate() {
        new TableUtils(driver).sortByHeaderColumn(table_BCSearchAccountsSearchResultsTable, "Creation Date");
    }

    private void getToSearch() {
        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickSearchTab();
        repository.bc.search.BCSearchMenu searchMenu = new BCSearchMenu(driver);
        searchMenu.clickSearchMenuAccounts();
    }

    public void searchAccountByAccountNumber(String searchAccountNumber) {
    	getToSearch();
    	clickReset();
    	setBCSearchAccountsAccountNumber(searchAccountNumber);
		clickSearch();
		
		boolean found = false;
		int timeToWaitInSeconds = 120;
		while (!found && timeToWaitInSeconds > 0) {
			try {
				clickAccountNumberInRow(1);
				found = true;
			} catch (Exception e) {
				sleep(10); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check the account again.
				clickSearch();
				timeToWaitInSeconds = timeToWaitInSeconds - 10;
			}
		}
		if (!found) {
			Assert.fail("The Account was searched for 2 minutes, but did not appear. Test cannot continue.");
		}
        //If the account number is not found, we need to be able to go to the message queues and check for stuck messages.
    }


    public void searchAccountByPolicyNumber(String policyNumber) {
    	getToSearch();
    	clickReset();
        setBCSearchAccountsPolicyNumber(policyNumber);
		clickSearch();

		boolean found = false;
		int timeToWaitInSeconds = 120;
		while (!found && timeToWaitInSeconds > 0) {
			try {
				clickAccountNumberInRow(1);
				found = true;
			} catch (Exception e) {
				sleep(10); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check the account again.
				clickSearch();
				timeToWaitInSeconds = timeToWaitInSeconds - 10;
			}
		}
		if (!found) {
			Assert.fail("The Account was searched for 2 minutes, but did not appear. Test cannot continue.");
		}
        //If the account number is not found, we need to be able to go to the message queues and check for stuck messages.
    }

    private String getBCSearchAccountsAccountNumber(int rowNumber) {
        return new TableUtils(driver).getCellTextInTableByRowAndColumnName(table_BCSearchAccountsSearchResultsTable, rowNumber, "Account #");
    }

    private void setPartialPolicyNumber(String numToFill) {
        waitUntilElementIsClickable(editBox_BCSearchAccountsPolicyNumber);
        editBox_BCSearchAccountsPolicyNumber.sendKeys(numToFill);
    }

    private String clickBCSearchAccountsAccountNumber(int rowNumber) {
        String accountNumber = getBCSearchAccountsAccountNumber(rowNumber);
        new TableUtils(driver).clickLinkInTable(table_BCSearchAccountsSearchResultsTable, accountNumber);
        return accountNumber;
    }

    private String chooseAndSelectRandomAccount() {
        int randomRow = NumberUtils.generateRandomNumberInt(1, tableUtils.getRowCount(table_BCSearchAccountsSearchResultsTable));
        String accountNumber = clickBCSearchAccountsAccountNumber(randomRow);
        return accountNumber;
    }

    private String chooseAndSelectRandomAccountByCompany(PolicyCompany policyCompany) {
        String policyCompanyGridColumnId = tableUtils.getGridColumnFromTable(table_BCSearchAccountsSearchResultsTable, "Company");
        List<WebElement> possibleMatches = table_BCSearchAccountsSearchResultsTable.findElements(By.xpath(".//tr/td[(contains(@class,'" + policyCompanyGridColumnId + "'))]/div[(contains(.,'" + policyCompany.getValue() + "'))]"));
        if (possibleMatches.size() < 1) {
            tableUtils.sortByHeaderColumn(table_BCSearchAccountsSearchResultsTable, "Company");
            possibleMatches = table_BCSearchAccountsSearchResultsTable.findElements(By.xpath(".//tr/td[(contains(@class,'" + policyCompanyGridColumnId + "'))]/div[(contains(.,'" + policyCompany.getValue() + "'))]"));
            if (possibleMatches.size() < 1) {
                int i = 0;
                while (tableUtils.incrementTablePageNumber(table_BCSearchAccountsSearchResultsTable)) {
                    i++;
                    possibleMatches = table_BCSearchAccountsSearchResultsTable.findElements(By.xpath(".//tr/td[(contains(@class,'" + policyCompanyGridColumnId + "'))]/div[(contains(.,'" + policyCompany.getValue() + "'))]"));
                    if (possibleMatches.size() > 0 || i > 15) {
                        break;
                    }
                }
                if (possibleMatches.size() < 1) {
                    Assert.fail("No acccounts were found that match the required criteria.");
                }
            }
        }
        int randomNumber = NumberUtils.generateRandomNumberInt(1, (possibleMatches.size() - 1));
        int rowNumber = Integer.valueOf(possibleMatches.get(randomNumber).findElement(By.xpath(".//parent::td/parent::tr")).getAttribute("data-recordindex"));
        String accountNumber = clickBCSearchAccountsAccountNumber(rowNumber);
        return accountNumber;
    }


    public String findDelinquentAccount() {
        getToSearch();
        setBCSearchAccountsAccountNumber("");
        setBCSearchAccountsDelinquencyStatus(DelinquencyStatus.Delinquent);
        clickSearch();
        String accountNumber = chooseAndSelectRandomAccount();
        return accountNumber;
    }

    public String findAccountInGoodStanding() {
        getToSearch();
        setBCSearchAccountsAccountNumber("");
        setBCSearchAccountsDelinquencyStatus(DelinquencyStatus.In_Good_Standing);
        setBCSearchAccountsType(AccountType.Insured);
        clickSearch();
        String accountNumber = chooseAndSelectRandomAccount();
        return accountNumber;
    }


    public String findRandomLienholder() {
        getToSearch();
        setBCSearchAccountsAccountNumber("");
        setBCSearchAccountsType(AccountType.Lienholder);
        clickSearch();
        String lienholderNumber = chooseAndSelectRandomAccount();
        return lienholderNumber;
    }

    public String findLienholderAccountInGoodStanding(String accountNumber) {
		getToSearch();
		setBCSearchAccountsAccountNumber(accountNumber);	
		setBCSearchAccountsDelinquencyStatus(DelinquencyStatus.In_Good_Standing);
		setBCSearchAccountsType(AccountType.Lienholder);		
		clickSearch();
		String lienholderNumber = chooseAndSelectRandomAccount();
		return lienholderNumber;
	}

    public String findAccountInGoodStanding(PolicyCompany policycompany) {
        getToSearch();
        setBCSearchAccountsAccountNumber("");
        setBCSearchAccountsDelinquencyStatus(DelinquencyStatus.In_Good_Standing);
        setBCSearchAccountsType(AccountType.Insured);
        clickSearch();
        String accountNumber = chooseAndSelectRandomAccountByCompany(policycompany);
        return accountNumber;
    }

    //can find an account with open policy.

    public String findAccountInGoodStanding(String policyNumber) {
        getToSearch();
        setBCSearchAccountsAccountNumber("");
        setPartialPolicyNumber(policyNumber);
        setBCSearchAccountsDelinquencyStatus(DelinquencyStatus.In_Good_Standing);
        setBCSearchAccountsType(AccountType.Insured);
        clickSearch();
        String accountNumber = chooseAndSelectRandomAccount();
        return accountNumber;
    }


    public String findRecentAccountInGoodStanding(String policyNumber, AccountType accountTypeToSearch) {
        getToSearch();
        setBCSearchAccountsAccountNumber("");
        if (policyNumber == null) {
            setPartialPolicyNumber("");
        } else {
            setPartialPolicyNumber(policyNumber);
        }
        setBCSearchAccountsDelinquencyStatus(DelinquencyStatus.In_Good_Standing);
        setBCSearchAccountsType(accountTypeToSearch);
        clickSearch();
        tableUtils.sortByHeaderColumn(table_BCSearchAccountsSearchResultsTable, "Creation Date");
        tableUtils.sortByHeaderColumn(table_BCSearchAccountsSearchResultsTable, "Creation Date");
        String accountNumber = chooseAndSelectRandomAccount();
        return accountNumber;
    }

    public List<String> findRecentAccountAndPolicyInGoodStanding(String policyNumber, AccountType accountTypeToSearch) {
        List<String> accountAndPolicyNumber = new ArrayList<>();
        getToSearch();
        setBCSearchAccountsAccountNumber("");
        if (policyNumber == null) {
            setPartialPolicyNumber("");
        } else {
            setPartialPolicyNumber(policyNumber);
        }
        setBCSearchAccountsDelinquencyStatus(DelinquencyStatus.In_Good_Standing);
        setBCSearchAccountsType(accountTypeToSearch);
        clickSearch();
        tableUtils.sortByHeaderColumn(table_BCSearchAccountsSearchResultsTable, "Creation Date");
        tableUtils.sortByHeaderColumn(table_BCSearchAccountsSearchResultsTable, "Creation Date");
        String accountNumber = chooseAndSelectRandomAccount();
        accountAndPolicyNumber.add(accountNumber);
        accountAndPolicyNumber.add(new BCAccountSummary(driver).getPolicyNumberFromOpenPolicyStatusTable(accountNumber).substring(0, 12));
        return accountAndPolicyNumber;
    }



    public String findAccountInGoodStanding(PolicyCompany policycompany, String policyNumber) {
        getToSearch();
        setBCSearchAccountsAccountNumber("");
        setPartialPolicyNumber(policyNumber);
        setBCSearchAccountsDelinquencyStatus(DelinquencyStatus.In_Good_Standing);
        setBCSearchAccountsType(AccountType.Insured);
        clickSearch();
        String accountNumber = chooseAndSelectRandomAccountByCompany(policycompany);
        return accountNumber;
    }

    /**
     * This method cannot be used with the "getToSearch" method call. It is intended when picking an account from a picker screen.
     */

    public String pickRandomAccount(String accountNumber) {
        if (accountNumber != null) {
            setBCSearchAccountsAccountNumber(accountNumber);
        }
        clickSearch();
        int rowNumber = new TableUtils(driver).getRandomRowFromTable(table_BCSearchAccountsSearchResultsTable);
        String accountNumberChosen = clickBCSearchAccountsAccountNumber(rowNumber);
        return accountNumberChosen;
    }
}
