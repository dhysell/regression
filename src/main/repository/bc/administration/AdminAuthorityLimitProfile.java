package repository.bc.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

public class AdminAuthorityLimitProfile extends BasePage {

	private TableUtils tableUtils;
	
	public AdminAuthorityLimitProfile(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths for Authority Limit Profile screen
	// -----------------------------------------------

	@FindBy(xpath = "//div[@id='AuthorityLimitProfiles:AuthorityLimitProfileScreen:AuthorityLimitProfilesLV')]")
	public WebElement table_AdminAuthorityLimitProfile;

	@FindBy(xpath = "//a[@id='AuthorityLimitProfiles:AuthorityLimitProfileScreen:AuthorityLimitProfiles_AddButton']")
	public WebElement button_AdminAuthorityLimitProfileAddAuthorityLimitProfile;

	@FindBy(xpath = "//a[@id='AuthorityLimitProfiles:AuthorityLimitProfileScreen:AuthorityLimitProfiles_DeleteButton']")
	public WebElement button_AdminAuthorityLimitProfileDelete;

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths for New Authority Limit Profile
	// -----------------------------------------------
	@FindBy(xpath = "//input[@id='NewAuthorityLimitProfile:AuthorityLimitProfileDetailScreen:AuthorityLimitProfileDV:Name-inputEl']")
	public WebElement editbox_AdminNewAuthorityLimitProfileName;

	@FindBy(xpath = "//input[@id='NewAuthorityLimitProfile:AuthorityLimitProfileDetailScreen:AuthorityLimitProfileDV:Description-inputEl']")
	public WebElement editbox_AdminNewAuthorityLimitProfileDescription;

	// -------------------------------------------------------
	// Helper Methods for Above Elements for Authority Limit Profile screen
	// -------------------------------------------------------

	public WebElement getAdminAuthorityLimitProfileTable() {
		waitUntilElementIsVisible(table_AdminAuthorityLimitProfile);
		return table_AdminAuthorityLimitProfile;
	}

	public void clickAddAuthorityLimitProfileButton() {
		clickWhenVisible(button_AdminAuthorityLimitProfileAddAuthorityLimitProfile);
	}

	public void clickAuthorityLimitProfileDeleteButton() {
		clickWhenVisible(button_AdminAuthorityLimitProfileDelete);
	}

	public void clickActivityPatternsTableCheckBoxBySubject(String subject) {
		tableUtils.setCheckboxInTable(table_AdminAuthorityLimitProfile, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_AdminAuthorityLimitProfile, "Subject", subject)), true);
	}

	// -------------------------------------------------------
	// Helper Methods for Above Elements for NEW Authority Limit Profile screen
	// -------------------------------------------------------

	public void setAdminNewAuthorityLimitProfileName(String name) {
		waitUntilElementIsVisible(editbox_AdminNewAuthorityLimitProfileName);
		editbox_AdminNewAuthorityLimitProfileName.sendKeys(name);
	}

	public void setAdminNewAuthorityLimitProfileDescription(String description) {
		waitUntilElementIsVisible(editbox_AdminNewAuthorityLimitProfileDescription);
		editbox_AdminNewAuthorityLimitProfileDescription.sendKeys(description);
	}
	
	public boolean checkIfCellExistsInTableByLinkText(String linkText) {
		return tableUtils.checkIfLinkExistsInTable(table_AdminAuthorityLimitProfile, linkText);
	}

}
