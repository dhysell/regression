package repository.bc.search;

import com.idfbins.enums.State;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PolicySearchPolicyOpenStatus;
import repository.gw.enums.PolicySearchPolicyProductType;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.topmenu.TopMenuPC;

public class BCSearchPolicies extends BasePage {
	
	private WebDriver driver;

	public BCSearchPolicies(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	//////////////////////////////////
	// Recorded xPaths for Elements //
	//////////////////////////////////

	@FindBy(xpath = "//input[contains(@id, ':AccountNumberCriterion-inputEl')]")
	private WebElement editbox_BCSearchPoliciesAccountNumber;

	@FindBy(xpath = "//input[contains(@id, ':PolicyNumberCriterion-inputEl')]")
	private WebElement editBox_BCSearchPoliciesPolicyNumber;
	
	Guidewire8Select select_BCSearchPoliciesPolicyStatus() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':PolicySearchDV:StatusCriterion-triggerWrap')]");
	}

	Guidewire8Select select_BCSearchPoliciesProduct() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':PolicySearchDV:ProductCriterion-triggerWrap')]");
	}

	Guidewire8Select select_BCSearchPoliciesBillingMethod() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':BillingMethodCriterion-triggerWrap')]");
	}
	
	@FindBy(xpath = "//input[contains(@id, ':Name-inputEl')]")
    private WebElement editBox_BCSearchPoliciesCompanyName;

	@FindBy(xpath = "//input[contains(@id, ':FirstName-inputEl')]")
	private WebElement editBox_BCSearchPoliciesFirstName;

	@FindBy(xpath = "//input[contains(@id, ':LastName-inputEl')]")
	private WebElement editBox_BCSearchPoliciesLastName;
	
	@FindBy(xpath = "//input[contains(@id, ':City-inputEl')]")
	private WebElement editBox_BCSearchPoliciesCity;
	
	private Guidewire8Select select_BCSearchPoliciesState() {
 		return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap')]");
	}
	
	@FindBy(xpath = "//input[contains(@id, ':PostalCode-inputEl')]")
	private WebElement editBox_BCSearchPoliciesZip;

    @FindBy(xpath = "//div[@id='PolicySearch:PolicySearchScreen:PolicySearchResultsLV' or @id = 'PolicySearch:PolicySearchScreen:PolicySearchResultsLV']")
	private WebElement table_BCSearchPoliciesSearchResultsTable;
	
	@FindBy(xpath = "//a[@id='SelectMultiplePoliciesPopup:SelectMultiplePoliciesScreen:PolicySearchDV_tb:addbutton']")
	private WebElement button_BCSearchPoliciesSelectPoliciesButton;

	////////////////////////////////////
	// Helper Methods for Interaction //
	////////////////////////////////////

	private void getToSearch() {
        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickSearchTab();
        repository.bc.search.BCSearchMenu searchMenu = new BCSearchMenu(driver);
        searchMenu.clickSearchMenuPolicies();
    }
	
	public String getBCSearchPoliciesSearchResultsPolicyNumber(int rowNumber) {
		return new TableUtils(driver).getCellTextInTableByRowAndColumnName(table_BCSearchPoliciesSearchResultsTable, rowNumber, "Policy #");
	}
	
	public void clickBCSearchPoliciesSearchResultsPolicyNumber(int rowNumber) {
		new TableUtils(driver).clickLinkInTableByRowAndColumnName(table_BCSearchPoliciesSearchResultsTable, rowNumber, "Policy #");
	}

	public String searchPolicyByAccountNumber(String accountNumber) {
		getToSearch();
		setBCSearchPoliciesAccountNumber(accountNumber);
		clickSearch();
		new TableUtils(driver).sortByHeaderColumn(table_BCSearchPoliciesSearchResultsTable, "Policy #");
		
		boolean found = false;
		int timeToWaitInSeconds = 120;
		String policyNumber = "";
		while (!found && timeToWaitInSeconds > 0) {
			try {
				policyNumber = getBCSearchPoliciesSearchResultsPolicyNumber(1);
				found = true;
			} catch (Exception e) {
				sleep(10); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for the policy again.
				clickSearch();
				new TableUtils(driver).sortByHeaderColumn(table_BCSearchPoliciesSearchResultsTable, "Policy #");
				timeToWaitInSeconds = timeToWaitInSeconds - 10;
			}
		}
		if (!found) {
			Assert.fail("The policy was searched for 2 minutes, but did not appear. Test cannot continue.");
		}
		
		String policyNumberSanitized = policyNumber.substring(0, (policyNumber.length() - 2));
		clickBCSearchPoliciesSearchResultsPolicyNumber(1);
		return policyNumberSanitized;
	}
	
	// Selects the first policy in the results list and ends on the Policy Summary Page
	// policyNumber can be either format ########## or ##-######-##
	public String searchPolicyByPolicyNumber(String policyNumber) {
		getToSearch();
		setBCSearchPoliciesPolicyNumber(policyNumber);
		clickSearch();
		new TableUtils(driver).sortByHeaderColumn(table_BCSearchPoliciesSearchResultsTable, "Policy #");
		
		boolean found = false;
		int timeToWaitInSeconds = 200;
		String searchResultsPolicyNumber = "";
		while (!found && timeToWaitInSeconds > 0) {
			try {
				searchResultsPolicyNumber = getBCSearchPoliciesSearchResultsPolicyNumber(1);
				found = true;
			} catch (Exception e) {
				sleep(10); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for the policy again.
				clickSearch();
				new TableUtils(driver).sortByHeaderColumn(table_BCSearchPoliciesSearchResultsTable, "Policy #");
				timeToWaitInSeconds = timeToWaitInSeconds - 10;
			}
		}
		if (!found) {
			Assert.fail("The policy was searched for 3 minutes, but did not appear. Probably Create PolicyPeriod BillingInstructions not made to BC. Test cannot continue.");
		}
		
		String policyNumberSanitized = searchResultsPolicyNumber.substring(0, (searchResultsPolicyNumber.length() - 2));
		clickBCSearchPoliciesSearchResultsPolicyNumber(1);
		return policyNumberSanitized;
	}
		
	public void setBCSearchPoliciesAccountNumber(String accountNumber) {
		clickReset();
		clickWhenClickable(editbox_BCSearchPoliciesAccountNumber);
		editbox_BCSearchPoliciesAccountNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"), accountNumber);
	}
	
	public void setBCSearchPoliciesAccountNumberWithoutResetting(String accountNumber) {
		clickWhenClickable(editbox_BCSearchPoliciesAccountNumber);
		editbox_BCSearchPoliciesAccountNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"), accountNumber);
	}
	
	public void setBCSearchPoliciesPolicyNumber(String policyNumber) {
		waitUntilElementIsClickable(editBox_BCSearchPoliciesPolicyNumber);
		editBox_BCSearchPoliciesPolicyNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		if (policyNumber.length() >= 12) {
			editBox_BCSearchPoliciesPolicyNumber.sendKeys(policyNumber);
		} else {
			editBox_BCSearchPoliciesPolicyNumber.sendKeys(StringsUtils.formatPolicyNumber(policyNumber));
		}
	}
	
	public void setBCSearchPoliciesPartialPolicyNumber(String numToFill) {
		waitUntilElementIsClickable(editBox_BCSearchPoliciesPolicyNumber);
		editBox_BCSearchPoliciesPolicyNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"), numToFill);
	}
	
	public void setBCSearchPoliciesPolicyStatus(PolicySearchPolicyOpenStatus status) {
		select_BCSearchPoliciesPolicyStatus().selectByVisibleText(status.getValue());
	}

	public void setBCSearchPoliciesProduct(PolicySearchPolicyProductType product) {
		select_BCSearchPoliciesProduct().selectByVisibleText(product.getValue());
	}

	public void setBCSearchPoliciesBillingMethod(String billingMehtod) {
		select_BCSearchPoliciesBillingMethod().selectByVisibleText(billingMehtod);
	}
	
	public void setBCSearchPoliciesCompanyName(String companyName) {
		waitUntilElementIsClickable(editBox_BCSearchPoliciesCompanyName);
		if (companyName.length() > 30) {
			editBox_BCSearchPoliciesCompanyName.sendKeys(Keys.chord(Keys.CONTROL + "a"),companyName.substring(0, 29));
		} else {
			editBox_BCSearchPoliciesCompanyName.sendKeys(Keys.chord(Keys.CONTROL + "a"), companyName);
		}
	}
	
	public void setBCSearchPoliciesFirstName(String firstName) {
		clickWhenClickable(editBox_BCSearchPoliciesFirstName);
		editBox_BCSearchPoliciesFirstName.sendKeys(Keys.chord(Keys.CONTROL + "a"), firstName);
	}

	public void setBCSearchPoliciesLastName(String lastName) {
		clickWhenClickable(editBox_BCSearchPoliciesLastName);
		editBox_BCSearchPoliciesLastName.sendKeys(Keys.chord(Keys.CONTROL + "a"), lastName);
	}

	public void setBCSearchPoliciesCity(String city) {
		clickWhenClickable(editBox_BCSearchPoliciesCity);
		editBox_BCSearchPoliciesCity.sendKeys(Keys.chord(Keys.CONTROL + "a"), city);
	}
	
	public void setBCSearchPoliciesState(State stateToSelect) {
		Guidewire8Select bcSearchAccountsState = select_BCSearchPoliciesState();
		bcSearchAccountsState.selectByVisibleText(stateToSelect.getName());
	}
	
	public void setBCSearchPoliciesZip(String zipPostalCode) {
		clickWhenClickable(editBox_BCSearchPoliciesZip);
		editBox_BCSearchPoliciesZip.sendKeys(Keys.chord(Keys.CONTROL + "a"), zipPostalCode);
	}
	
	public void setPolicyNumberCheckboxInResultsTable(String policyNumber) {
		new TableUtils(driver).setCheckboxInTableByText(table_BCSearchPoliciesSearchResultsTable, policyNumber, true);
	}
	
	public void clickSelectPoliciesButton() {
		clickWhenClickable(button_BCSearchPoliciesSelectPoliciesButton);
	}
	
	public String findPolicyInGoodStanding(String partialOrFullAccountNumber, String partialOrFullPolicyNumber, PolicySearchPolicyProductType productType) {
		getToSearch();
		if (partialOrFullAccountNumber != null) {
			setBCSearchPoliciesAccountNumberWithoutResetting(partialOrFullAccountNumber);
		}
		if (partialOrFullPolicyNumber != null) {
			setBCSearchPoliciesPartialPolicyNumber(partialOrFullPolicyNumber);
		}
		setBCSearchPoliciesPolicyStatus(PolicySearchPolicyOpenStatus.Open);
		if (productType != null) {
			setBCSearchPoliciesProduct(productType);
		}
		clickSearch();
		new TableUtils(driver).sortByHeaderColumn(table_BCSearchPoliciesSearchResultsTable, "Policy #");
		String policyNumber = chooseAndSelectRandomPolicy();
		String policyNumberSanitized = policyNumber.substring(0, (policyNumber.length() - 2));
		return policyNumberSanitized;
	}
	
	private String chooseAndSelectRandomPolicy () {
		int randomRow = NumberUtils.generateRandomNumberInt(1, new TableUtils(driver).getRowCount(table_BCSearchPoliciesSearchResultsTable));
		String policyNumber = getBCSearchPoliciesSearchResultsPolicyNumber(randomRow);
		clickBCSearchPoliciesSearchResultsPolicyNumber(randomRow);
		return policyNumber;
	}
}
