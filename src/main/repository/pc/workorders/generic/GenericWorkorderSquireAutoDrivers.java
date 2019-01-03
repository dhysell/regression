package repository.pc.workorders.generic;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;

import repository.gw.enums.CommuteType;
import repository.gw.enums.HowLongWithoutCoverage;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.RelationshipToInsuredPolicyMember;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import persistence.enums.Underwriter.UnderwriterLine;
import repository.pc.search.SearchSubmissionsPC;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderSquireAutoDrivers extends GenericWorkorder {

	private TableUtils tableUtils;
	private WebDriver driver;
	public GenericWorkorderSquireAutoDrivers(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}

	private void onpage() throws Exception {
		if(new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@class, 'g-title') and (text()='Drivers')]")) {
			//do nothing
		} else {
			new repository.pc.sidemenu.SideMenuPC(getDriver()).clickSideMenuPADrivers();
		}

	}
	
	public void addNewDrivers(GeneratePolicy policy) throws Exception {
		onpage();
		
		for(Contact driver : policy.squire.squirePA.getDriversList()) {
			if(driver.getDriverNumber() == -1) {
				addExistingDriver(driver.getFirstName());
				new GenericWorkorderSquireAutoDrivers_ContactDetail(getDriver()).fillOutContactDetails_QQ(driver);
				new GenericWorkorderSquireAutoDrivers_ContactDetail(getDriver()).fillOutContactDetails_FA(driver);
				new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(getDriver()).fillOutMotorVehicleRecord_QQ(driver);
				new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(getDriver()).fillOutMotorVehicleRecord_FA(driver);
				new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(getDriver()).fillOutSelfReportingIncidents_QQ(driver);
				new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(getDriver()).fillOutSelfReportingIncidents_FA(driver);
				clickOk();
				if(!finds(By.xpath("//div[contains(text(), '" + driver.getFirstName() + "') and contains(text(), '" + driver.getLastName() + "')]/parent::td/preceding-sibling::td[1]/div")).isEmpty()) {
					driver.setDriverNumber(Integer.valueOf(find(By.xpath("//div[contains(text(), '" + driver.getFirstName() + "') and contains(text(), '" + driver.getLastName() + "')]/parent::td/preceding-sibling::td[1]/div")).getText()));
				}
			}
			
		}
		
		
	}

	public void addNewDriver(Contact driver) throws Exception {
		onpage();
		addExistingDriver(driver.getFirstName());
		new GenericWorkorderSquireAutoDrivers_ContactDetail(getDriver()).fillOutContactDetails_QQ(driver);
		new GenericWorkorderSquireAutoDrivers_ContactDetail(getDriver()).fillOutContactDetails_FA(driver);
		new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(getDriver()).fillOutMotorVehicleRecord_QQ(driver);
		new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(getDriver()).fillOutMotorVehicleRecord_FA(driver);
		new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(getDriver()).fillOutSelfReportingIncidents_QQ(driver);
		new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(getDriver()).fillOutSelfReportingIncidents_FA(driver);
		clickOk();
		if(!finds(By.xpath("//div[contains(text(), '" + driver.getFirstName() + "') and contains(text(), '" + driver.getLastName() + "')]/parent::td/preceding-sibling::td[1]/div")).isEmpty()) {
			driver.setDriverNumber(Integer.valueOf(find(By.xpath("//div[contains(text(), '" + driver.getFirstName() + "') and contains(text(), '" + driver.getLastName() + "')]/parent::td/preceding-sibling::td[1]/div")).getText()));
		}
	}

	public void fillOutDriversQQ(GeneratePolicy policy) throws Exception {
		onpage();
		Login login = new Login(getDriver());
		for(Contact driver : policy.squire.squirePA.getDriversList()) {
			if (policy.mvr | policy.clueAuto | policy.clueProperty | policy.insuranceScore | policy.prefillPersonal) {
				driver.isSpecial = true;
			}
			if(driver.isExcludedDriver()) {
				login.switchUserToUW(UnderwriterLine.Personal);
				repository.pc.search.SearchSubmissionsPC searchSubmissions = new SearchSubmissionsPC(getDriver());
				searchSubmissions.searchAndSelectSubmission(policy.accountNumber);
				new repository.pc.sidemenu.SideMenuPC(getDriver()).clickSideMenuPADrivers();
			}
			addExistingDriver(driver.getFirstName());
			new GenericWorkorderSquireAutoDrivers_ContactDetail(getDriver()).fillOutContactDetails_QQ(driver);
			new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(getDriver()).fillOutMotorVehicleRecord_QQ(driver);
			new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(getDriver()).fillOutSelfReportingIncidents_QQ(driver);
			clickOk();
			
			if(!finds(By.xpath("//div[contains(text(), '" + driver.getFirstName() + "') and contains(text(), '" + driver.getLastName() + "')]/parent::td/preceding-sibling::td[1]/div")).isEmpty()) {
				driver.setDriverNumber(Integer.valueOf(find(By.xpath("//div[contains(text(), '" + driver.getFirstName() + "') and contains(text(), '" + driver.getLastName() + "')]/parent::td/preceding-sibling::td[1]/div")).getText()));
			}
			
			
			if(driver.isExcludedDriver()) {
				new repository.pc.sidemenu.SideMenuPC(getDriver()).clickSideMenuPACoverages();
				new GenericWorkorderSquireAutoCoverages_Coverages(getDriver()).fillOutSquireAutoCoverages_Coverages(policy);
				repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_ExclusionsConditions paExclusions = new GenericWorkorderSquireAutoCoverages_ExclusionsConditions(getDriver());
				paExclusions.clickCoverageExclusionsTab();
				paExclusions.addDriverExclusionEndorsement304(driver.getFirstName());

				clickGenericWorkorderSaveDraft();
				new GuidewireHelpers(getDriver()).logout();
				login.loginAndSearchSubmission(policy);
				new SideMenuPC(getDriver()).clickSideMenuPADrivers();
			}
		}
	}

	public void fillOutDriversFA(GeneratePolicy policy) throws Exception {
		onpage();
		for (Contact driver : policy.squire.squirePA.getDriversList()) {
			if(policy.squire.isFarmAndRanch()) {
				addExistingDriver(driver.getFirstName());
				new GenericWorkorderSquireAutoDrivers_ContactDetail(getDriver()).fillOutContactDetails_QQ(driver);
				new GenericWorkorderSquireAutoDrivers_ContactDetail(getDriver()).fillOutContactDetails_FA(driver);
				new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(getDriver()).fillOutMotorVehicleRecord_QQ(driver);
				new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(getDriver()).fillOutMotorVehicleRecord_FA(driver);
				new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(getDriver()).fillOutSelfReportingIncidents_QQ(driver);
				new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(getDriver()).fillOutSelfReportingIncidents_FA(driver);
				clickOk();
			} else {
				if (driver.isContactIsPNI()) {
					driver = policy.pniContact;
				}
				if (policy.mvr | policy.clueAuto | policy.clueProperty | policy.insuranceScore | policy.prefillPersonal) {
					driver.isSpecial = true;
				}
				clickEditButtonInDriverTableByDriverName(driver.returnFullName());
				new GenericWorkorderSquireAutoDrivers_ContactDetail(getDriver()).fillOutContactDetails_FA(driver);
				new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(getDriver()).fillOutMotorVehicleRecord_FA(driver);
				new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(getDriver()).fillOutSelfReportingIncidents_FA(driver);
				clickOk();
			}
		}
	}

	public void fillOutDrivers(GeneratePolicy policy) throws GuidewireNavigationException {
		for (Contact driver : policy.squire.squirePA.getDriversList()) {
			if (policy.mvr | policy.clueAuto | policy.clueProperty | policy.insuranceScore | policy.prefillPersonal) {
				driver.isSpecial = true;
			}
			new GenericWorkorderSquireAutoDrivers_ContactDetail(getDriver()).fillOutContactDetails(driver);
			new GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(getDriver()).fillOutMotorVehicleRecord(driver);
			new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(getDriver()).fillOutSelfReportingIncidents(driver);
			clickOk();
		}
	}


	public void clickContactDetailTab() throws GuidewireNavigationException {
		clickWhenClickable(button_PADriversContactDetail);
		new GuidewireHelpers(getDriver()).isOnPage("//label[contains(text(), 'driver')]", 5000, "UNABLE TO GET TO CONTACT DETAILS TAB");
	}

	public void clickMotorVehicleRecord() throws GuidewireNavigationException {
		clickWhenClickable(button_PAMotorVehicleRecord);
		new GuidewireHelpers(getDriver()).isOnPage("//label[contains(@id, ':PersonalMotorVehicleRecordsDV:')]", 5000, "UNABLE TO GET TO MOTOR VEHICLE RECORD TAB");
	}

	public void clickSRPIncidentsTab() throws GuidewireNavigationException {
		clickWhenClickable(link_SRPIncidents);
		new GuidewireHelpers(getDriver()).isOnPage("//label[contains(text(), 'SRP')]", 5000, "UNABLE TO GET TO SELF REPORTING INCIDENTS TAB");
	}

	public void clickCancel() {
		super.clickCancel();
	}

	public void clickOk() throws GuidewireNavigationException {
		super.clickOK();
		new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@class, 'g-title') and contains(@id, 'DriversListDetailPanel:DriverDetailTitle')]", 5000, "UNABLE TO GET TO MAIN DRIVER PAGE AFTER CLICKING OK");
	}

	public void clickNext() {
		super.clickNext();
	}

	public void addExistingDriver(String name) {
		String nameXpath = "//span[contains(text(), '" + name + "')]/parent::a[contains(@id, ':AddDriver:AddExistingContact')]";
		clickWhenClickable(button_PADriversAdd);
		hoverOver(link_PADriversAddExistingDriver);
		clickWhenClickable(By.xpath(nameXpath));
		waitUntilElementIsVisible(link_DriverHeaderPage);
	}

	public void clickOrderMVR() {
		clickWhenClickable(button_PARetrieveMVR);
	}

	public void clickRemoveButton() {
		super.clickRemove();
	}


	@FindBy(xpath = "//span[contains(@id, ':DriverDetailsCV:MVRDetailCardTab-btnInnerEl')]")
	private WebElement button_PAMotorVehicleRecord;


	@FindBy(xpath = "//span[contains(@id, ':SelfReportedTabTab-btnEl')]")
	private WebElement link_SRPIncidents;

	@FindBy(xpath = "//span[contains(@id, 'DriversLV_tb:AddDriver-btnEl')]")
	private WebElement button_PADriversAdd;

	@FindBy(xpath = "//*[contains(@id, 'PolicyContactDetailCardTab-btnEl')]")
	private WebElement button_PADriversContactDetail;

	@FindBy(xpath = "//span[contains(@id, 'DriversLV_tb:AddDriver:AddExistingContact')]")
	private WebElement link_PADriversAddExistingDriver;

	@FindBy(xpath = "//a[contains(@id, 'PADriverPopup:RetrieveMVRButton') or contains(@id, 'PADriverPopup:OrderMVRButton') or contains(@id, 'PADriverPopup:ReorderMVRButton') or contains(@id, 'PADriver_FBMPopup:OrderMVRButton')]")
	private WebElement button_PARetrieveMVR;

	@FindBy(xpath = "//*[contains(@id, 'PolicyContactRoleInputSet:followUpDate-inputEl')]")
	private WebElement editbox_followUpDate;

	@FindBy(xpath = "//a[contains(@id, 'DriversListDetailPanel:DriversLV:0:EditLink')]")
	private WebElement editbox_driver;

	@FindBy(xpath = "//div[contains(@id, 'PADriversPanelSet:DriversListDetailPanel:DriversLV')]")
	private WebElement table_driverDetails;

	@FindBy(xpath = "//span[contains(text(), 'Edit Driver') and contains(@id, ':ttlBar')]")
	private WebElement link_DriverHeaderPage;

	/**************************************************************************************************************************
	 * Methods
	 ************************************************************************************************************************/


	public boolean checkAddDriverExists() {
		return checkIfElementExists(button_PADriversAdd, 1000);
	}

	public void clickEditFirstDriver() {
		clickWhenClickable(editbox_driver);
	}

	public void setFollowUpDate(Date date) {
		editbox_followUpDate.click();
		editbox_followUpDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_followUpDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", date));
	}

	public boolean checkIfMVRButtonIsClickable() {
		String classAttr = button_PARetrieveMVR.getAttribute("class");
		return !classAttr.contains("-disabled");
	}

	WebElement getDriverDetailsTable() {
		return table_driverDetails;
	}

	public int getDriverTableRowsCount() {
		return tableUtils.getRowCount(table_driverDetails);
	}


	public void clickEditButtonInDriverTable(int rowNumber) {
		tableUtils.clickLinkInSpecficRowInTable(table_driverDetails, rowNumber);
	}


	public void clickEditButtonInDriverTableByDriverName(String driverName) {
		tableUtils.clickLinkInSpecficRowInTable(table_driverDetails, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_driverDetails, "Name", driverName)));
	}

	public boolean checkIfDriverExists(String driverName) {
		return tableUtils.verifyRowExistsInTableByColumnsAndValues(table_driverDetails, "Name", driverName);
	}

	public void setCheckBoxInDriverTable(int rowNumber) {
		tableUtils.setCheckboxInTable(table_driverDetails, rowNumber, true);
	}


	public void setCheckBoxInDriverTableByDriverName(String driverName) {
		tableUtils.setCheckboxInTable(table_driverDetails, tableUtils.getRowNumberInTableByText(table_driverDetails, driverName), true);
	}


	public String getDriverTableColumnValueByRow(int currentDriver, String columnName) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_driverDetails, currentDriver, columnName);
	}


	public void selectDriverTableSpecificRowByRow(String textInTable) {
		tableUtils.clickRowInTableByText(table_driverDetails, textInTable);
	}


	/**
	 * Used to verify questions answered by portal
	 *
	 * @param currentlyHasInsurance
	 * @param timeWithoutInsurance  nullable
	 * @return boolean info is correctly set
	 * @Author Ian Clouser
	 */

	public boolean verifyPolicyDriverQuestionsCorrect(boolean currentlyHasInsurance, HowLongWithoutCoverage timeWithoutInsurance) {
		// div[id*='FBM_PolicyContactDetailsDV'][id$='QuestionSetLV-body'] td.x-grid-cell-last > div css to get the question sets.  could use table utils?
		List<WebElement> driverQuestions = finds(By.cssSelector("div[id*='FBM_PolicyContactDetailsDV'][id$='QuestionSetLV-body'] td.x-grid-cell-last > div"));

		if (currentlyHasInsurance) {
			return driverQuestions.get(0).getText().equalsIgnoreCase("yes");
		} else {
			return driverQuestions.get(0).getText().equalsIgnoreCase("no") && driverQuestions.get(1).getText().equalsIgnoreCase(timeWithoutInsurance.getValue());
		}
	}


	/**
	 * @param driverName This is the driver name in the driver table you want.
	 * @return Person
	 * @Author Ian Clouser
	 * @Decription This function is used for by the portals to gather driver info in PC.
	 */

	public Contact gatherDriverInfo(String driverName) {
		By firstNameSelector = By.cssSelector("div[id$=':FirstName-inputEl']"); // Selector for first name.
		By lastNameSelector = By.cssSelector("div[id$=':LastName-inputEl']");
		By dobSelector = By.cssSelector("div[id$=':DateOfBirth-inputEl']");
		By maritalStatusSelector = By.cssSelector("div[id$=':maritalStatus-inputEl']");
		By genderSelector = By.cssSelector("div[id$=':Gender-inputEl']");
		By relationToInsured = By.cssSelector("div[id$=':RelationToInsured-inputEl']");
		By occupationSelector = By.cssSelector("div[id$=':occupation-inputEl']");
		By licenseNumSelector = By.cssSelector("div[id$=':licenseNumber-inputEl']");
		By commuteTypeSelector = By.cssSelector("div[id$=':CommuteType-inputEl']");
		By licenseStateSelector = By.cssSelector("div[id$=':DriversLicenseState2-inputEl']");
		// commute type :CommuteType-inputEl
		// license state :DriversLicenseState2-inputEl

		By tableSelector = By.cssSelector("div[id$=':DriversLV-body'");
		int rowThatHasName = tableUtils.getRowNumberInTableByText(find(tableSelector), driverName);

		Contact backEndDriver = new Contact();
		backEndDriver.removeDefaultValues();
		tableUtils.clickRowInTableByRowNumber(find(tableSelector), rowThatHasName);

		waitUntilElementIsClickable(genderSelector);

		backEndDriver.setFirstName(find(firstNameSelector).getText());
		backEndDriver.setLastName(find(lastNameSelector).getText());
		try {
			backEndDriver.setDob(driver, DateUtils.convertStringtoDate(find(dobSelector).getText(), "MM/dd/yyyy"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		backEndDriver.setMaritalStatus(MaritalStatus.getEnumFromStringValue(find(maritalStatusSelector).getText()));
		backEndDriver.setGender(Gender.valueOf(find(genderSelector).getText()));
		backEndDriver.setRelationPolicyMember(RelationshipToInsuredPolicyMember.valueOf(find(relationToInsured).getText()));
		backEndDriver.setOccupation(find(occupationSelector).getText());
		backEndDriver.setDriversLicenseNum(find(licenseNumSelector).getText());
		backEndDriver.setStateLicenced(State.valueOfName(find(licenseStateSelector).getText()));
		backEndDriver.setCommuteType(CommuteType.getEnumFromStringValue(find(commuteTypeSelector).getText()));

		return backEndDriver;
	}
}
