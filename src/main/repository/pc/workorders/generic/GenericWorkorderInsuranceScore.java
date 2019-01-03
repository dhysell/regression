package repository.pc.workorders.generic;


import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.LexisNexis;
import persistence.globaldatarepo.helpers.LexisNexisHelper;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.InsuranceScore;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.*;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;
import services.broker.objects.lexisnexis.cbr.response.actual.NcfReport;
import services.broker.objects.lexisnexis.generic.request.ReportType;
import services.broker.services.lexisnexis.ServiceLexisNexis;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class GenericWorkorderInsuranceScore extends GenericWorkorder {

	private WebDriver driver;

	public GenericWorkorderInsuranceScore(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	Guidewire8Select select_individual() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':CreditReportContact-triggerWrap')]");
	}

	Guidewire8RadioButton radio_livedLessThan6Months() {
		return new Guidewire8RadioButton(driver, "//table[contains(@id, ':AddressChangeQuestion')]");
	}

	Guidewire8RadioButton radio_nameChangedLast6Months() {
		return new Guidewire8RadioButton(driver, "//table[contains(@id, ':NameChangeQuestion')]");
	}

	@FindBy(xpath = "//a[contains(@id, ':OrderCreditReportButton')]")
	private WebElement button_orderCreditReport;

	@FindBy(xpath = "//a[contains(@id, ':CreditReportScreen:CreditReportPanelSet:EditDetailsButton')]")
	private WebElement button_EditInsuranceReportDetails;

	@FindBy(xpath = "//div[contains(@id, ':LOBWizardStepGroup:CreditReportScreen:CreditReportPanelSet:CreditScoreGeneralInputSet:CreditReportScore-inputEl') or contains(@id, ':CreditReportScore-inputEl')]")
	private WebElement text_InsuranceScore;

	@FindBy(xpath = "//div[contains(@id, ':CreditScoreGeneralInputSet:CreditReportScoreFiltered-inputEl')]")
	private WebElement text_EnhancedInsuranceScore;

	@FindBy(xpath = "//div[contains(@id, ':CreditScoreGeneralInputSet:CreditReportFlag-inputEl')]")
	private WebElement text_Flag;

	@FindBy(xpath = "//label[.='Reason Codes']/parent::td/parent::tr/following-sibling::tr//table")
	private WebElement table_ReasonCodes;

	private List<WebElement> text_ReasonCodeCells() {
		return table_ReasonCodes.findElements(By.xpath(".//tbody/tr//div"));
	}

	@FindBy(xpath = "//a[contains(@id,':LOBWizardStepGroup:CreditReportScreen:CreditReportPanelSet:CreditReportDetailsTabTab')]")
	private WebElement tab_ReportDetails;

	@FindBy(xpath = "//label[.='Bankruptcies']/parent::td/parent::tr/following-sibling::tr//table")
	private WebElement table_Bankruptcies;

	private List<WebElement> text_BankruptcyRows() {
		return table_Bankruptcies.findElements(By.xpath(".//tbody/tr"));
	}

	@FindBy(xpath = "//label[.='Collection Items']/parent::td/parent::tr/following-sibling::tr//table")
	private WebElement table_CollectionItems;

	@FindBy(xpath = "//span[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:CreditReportScreen:PolicyHolderUnder18')]")
	private WebElement validationMessage;

	@FindBy(xpath = "//div[contains(@id, ':CreditScoreGeneralInputSet:CreditReportStatus-inputEl')]")
	private WebElement InsuranceStatus;

	@FindBy(xpath = "//div[contains(@id, ':CreditReportErrorDescription-inputEl')]")
	private WebElement errorDescription;

	@FindBy(xpath = "//input[contains(@id, ':AltFIrstName-inputEl')]")
	private WebElement text_formerFirstName;

	@FindBy(xpath = "//input[contains(@id, ':AltLastName-inputEl')]")
	private WebElement text_formerLastName;

	@FindBy(xpath = "//a[contains(@id, ':CreditReportPanelSet:creditScoreGeneralTabTab')]")
	private WebElement link_CreditScoreGeneralTab;

	@FindBy(xpath = "//a[contains(@id, ':CreditReportPanelSet:CreditReportDetailsTabTab')]")
	private WebElement link_CurrentReportDetailsTab;

	@FindBy(xpath = "//div[contains(., 'Insurance Score (Verisk)')]/following-sibling::div[contains(@id, ':CreditReportScreen:CreditReportPanelSet')]")
	private WebElement table_InsuranceScoreVeriskDetails;

	@FindBy(xpath = "//a[contains(@id, ':CreditReportPanelSet:veriskCreditReportTabTab')]")
	private WebElement link_InsuranceScoreVeriskTab;

	@FindBy(xpath = "//div[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:CreditReportScreen:CreditReportPanelSet:2-body')]")
	private WebElement table_VeriskScore;

	private List<WebElement> text_CollectionItemRows() {
		return table_CollectionItems.findElements(By.xpath(".//tbody/tr"));
	}


	public double getInsuranceScore() {
		waitUntilElementIsClickable(text_InsuranceScore);
		return Double.valueOf(text_InsuranceScore.getText());
	}


	public double getEnhancedInsuranceScore() {

		waitUntilElementIsVisible(text_EnhancedInsuranceScore);
		return (text_EnhancedInsuranceScore.getText() == null || text_EnhancedInsuranceScore.getText().equals("")) ? 0 : Double.valueOf(text_EnhancedInsuranceScore.getText());
	}

	public InsuranceScore_Verisk getInsuranceScore_Verisk() {
		InsuranceScore_Verisk toReturn = new InsuranceScore_Verisk();
		toReturn.setReportType(new TableUtils(driver).getCellTextInTableByRowAndColumnName(table_VeriskScore, 1, "Report Type"));
		toReturn.setReferenceNumber(new TableUtils(driver).getCellTextInTableByRowAndColumnName(table_VeriskScore, 1, "Reference Number"));
		toReturn.setDateOrdered(new TableUtils(driver).getCellTextInTableByRowAndColumnName(table_VeriskScore, 1, "Date Report Ordered"));
		toReturn.setNameFromReport(new TableUtils(driver).getCellTextInTableByRowAndColumnName(table_VeriskScore, 1, "Name From Report"));
		toReturn.setInsuranceStatus(new TableUtils(driver).getCellTextInTableByRowAndColumnName(table_VeriskScore, 1, "Insurance Status"));
		toReturn.setFlag(new TableUtils(driver).getCellTextInTableByRowAndColumnName(table_VeriskScore, 1, "Flag"));

		try {
			toReturn.setInsuranceScore(new TableUtils(driver).getCellTextInTableByRowAndColumnName(table_VeriskScore, 1, "Insurance Score"));
			toReturn.setEnhancedInsuranceScore(new TableUtils(driver).getCellTextInTableByRowAndColumnName(table_VeriskScore, 1, "Enhanced Insurance Score"));
		} catch(Exception e) {
			//probably not logged in as user that can see these fields. ISSupportUser
		}

		return toReturn;
	}

	public String getFlag() {

		waitUntilElementIsClickable(text_Flag);
		return String.valueOf(text_Flag.getText());
	}

	public String getVeriskFlag() {
		String toReturn = new TableUtils(driver).getCellTextInTableByRowAndColumnName(table_VeriskScore, 1, "Flag");
		return toReturn;
	}


	public ArrayList<String> getReasonCodes() {
		waitUntilElementIsClickable(table_ReasonCodes);
		ArrayList<String> toReturn = new ArrayList<String>();
		for (WebElement we : text_ReasonCodeCells()) {
			toReturn.add(we.getText());
		}

		return toReturn;
	}


	public ArrayList<UICreditReportBankruptcies> getBankruptcies() throws ParseException {
		clickWhenClickable(tab_ReportDetails);

		ArrayList<UICreditReportBankruptcies> toReturn = new ArrayList<UICreditReportBankruptcies>();
		for (WebElement we : text_BankruptcyRows()) {
			UICreditReportBankruptcies newRow = new UICreditReportBankruptcies();
			newRow.setDateFiled(DateUtils.convertStringtoDate(we.findElement(By.xpath(".//td[1]/div")).getText(), "MM/dd/yyyy"));
			newRow.setStatus(we.findElement(By.xpath(".//td[2]/div")).getText());
			newRow.setType(we.findElement(By.xpath(".//td[3]/div")).getText());
			toReturn.add(newRow);
		}

		return toReturn;
	}


	public ArrayList<UICreditReportCollectionItems> getCollectionItems() throws ParseException {
		clickWhenClickable(tab_ReportDetails);

		ArrayList<UICreditReportCollectionItems> toReturn = new ArrayList<UICreditReportCollectionItems>();
		for (WebElement we : text_CollectionItemRows()) {
			UICreditReportCollectionItems newRow = new UICreditReportCollectionItems();
			newRow.setGrantorName(we.findElement(By.xpath(".//td[1]/div")).getText());
			newRow.setOriginalAmount(Long.valueOf(we.findElement(By.xpath(".//td[2]/div")).getText()));
			newRow.setBalanceAmount(Long.valueOf(we.findElement(By.xpath(".//td[3]/div")).getText()));
			newRow.setDateReported(DateUtils.convertStringtoDate(we.findElement(By.xpath(".//td[4]/div")).getText(), "MM/dd/yyyy"));

			toReturn.add(newRow);
		}

		return toReturn;
	}


	public InsuranceScoreInfo fillOutCreditReport(GeneratePolicy policy) throws Exception {
		String insured = null;
		String ssn = null;
		if (policy.pniContact.isPerson()) {
			insured = policy.pniContact.getFirstName();
			ssn = policy.pniContact.getSocialSecurityNumber();
		} else if (policy.pniContact.isCompany()) {
			for (PolicyInfoAdditionalNamedInsured ani : policy.aniList) {
				if (ani.getCompanyOrPerson() == ContactSubType.Person) {
					insured = ani.getPersonFirstName();
					ssn = ani.getSocialSecurityTaxNum();
					break;
				}
			}

			if (insured == null) {
				for (Contact pm : policy.squire.policyMembers) {
					if (pm.getPersonOrCompany() == ContactSubType.Person && pm.isAdditionalInsured().equals(policy.lineSelection)) {
						insured = pm.getFirstName();
						ssn = pm.getSocialSecurityNumber();
						break;
					}
				}
			}
		}

		
		if (insured == null) {
			throw new Exception("ERROR: You either have a company PNI with no additional named insured persons... or a company PNI with no policy member persons that are on all sections");
		}

		NcfReport fromBroker = null;
		InsuranceScoreInfo toReturn = null;
		selectCreditReportIndividual(insured);
		
		if(markLivedHere6Months(policy.squire.insuranceScoreReport.isLivedAtAddressLessThan6Months())) {
			fillOutFormerAddress(policy.squire.insuranceScoreReport);
		}

		if(markNameChanged6Months(policy.squire.insuranceScoreReport.isNameChangelast6Months())) {
			fillOutFormerName(policy.squire.insuranceScoreReport);
		}

		
		
		if (policy.insuranceScore) {
			fromBroker = clickOrderReportAndAlsoOrderDirectlyFromBrokerForComparison(ssn);

			toReturn = new InsuranceScoreInfo();
			toReturn.setBrokerReport(fromBroker);
			//            try {
				//                toReturn.setUiInsuranceScore(getInsuranceScore());
			//                toReturn.setUiEnhancedInsuranceScore(getEnhancedInsuranceScore());
			//                if (getReasonCodes().size() > 0)
			//                    toReturn.setUiReasonCodes(getReasonCodes());
			//                if (getBankruptcies().size() > 0)
			//                    toReturn.setUiBankruptcies(getBankruptcies());
			//                if (getCollectionItems().size() > 0)
			//                    toReturn.setUiCollectionItems(getCollectionItems());
			//            } catch (Exception e) {
			//                e.printStackTrace();
			//                throw new Exception("Getting insurance score failed for " + insured);
			//            }
		} else {
			if (policy.squire.alwaysOrderCreditReport) {
				clickOrderReport();
				policy.pniContact.setInsuranceScore(InsuranceScore.Ordered);
			}
		}







		return (toReturn == null)? policy.squire.insuranceScoreReport : toReturn;
	}


	public void selectCreditReportIndividual(String person) {

		Guidewire8Select selectIndividual = select_individual();
		selectIndividual.selectByVisibleTextPartial(person);

	}


	public boolean markLivedHere6Months(boolean value) {
		Guidewire8RadioButton radioLived = radio_livedLessThan6Months();
		radioLived.select(value);
		return value;
	}


	public boolean markNameChanged6Months(boolean value) {
		Guidewire8RadioButton radioNameChanged = radio_nameChangedLast6Months();
		radioNameChanged.select(value);
		return value;
	}


	public void clickOrderReport() {
		clickWhenClickable(button_orderCreditReport);
	}


	public boolean checkOrderInsuranceReportButton() {
		return !button_orderCreditReport.getAttribute("class").contains("-disabled");
	}


	public void clickEditInsuranceReport() {
		clickWhenClickable(button_EditInsuranceReportDetails);
	}


	public boolean isEditInsuranceOrderDetailsDisplayed() {
		return checkIfElementExists(button_EditInsuranceReportDetails, 2000);
	}


	public NcfReport clickOrderReportAndAlsoOrderDirectlyFromBrokerForComparison(String ssn) throws Exception {
		try {
			clickOrderReport();
		} catch (Exception e) {
		}

		boolean printRequestXMLToConsole = false;
		boolean printResponseXMLToConsole = true;
		ServiceLexisNexis testService = new ServiceLexisNexis();
		LexisNexis[] randomCustomers = {LexisNexisHelper.getCustomerBySSN(ssn)};
		NcfReport testResponse = testService.orderCBR(testService.setUpTestOrder(ReportType.CBR, randomCustomers), new GuidewireHelpers(driver).getMessageBrokerConnDetails(), printRequestXMLToConsole, printResponseXMLToConsole);

		return testResponse;
	}


	public String getInsuranceScoreValidationMessage() {
		return validationMessage.getText();
	}


	public String getInsuranceStatus() {
		return InsuranceStatus.getText();
	}


	public String getInsuranceErrorDescription() {
		return errorDescription.getText();
	}


	public void setInsuranceFormerFirstName(String firstName) {
		clickWhenClickable(text_formerFirstName);
		text_formerFirstName.sendKeys(Keys.chord(Keys.CONTROL + "a"), firstName);
		clickProductLogo();
	}


	public void setInsuranceFormerLastName(String lastName) {
		clickWhenClickable(text_formerLastName);

		text_formerLastName.sendKeys(Keys.chord(Keys.CONTROL + "a"), lastName);
		clickProductLogo();
	}


	public void clickCurrentInsuranceScoreGeneral() {
		clickWhenClickable(link_CreditScoreGeneralTab);

	}

	public void clickCurrentReportDetails() {
		clickWhenClickable(link_CurrentReportDetailsTab);

	}


	public void clickInsuranceScoreVerisk() {
		clickWhenClickable(link_InsuranceScoreVeriskTab);

	}


	public InsuranceScoreInfo fillOutCreditReporting(GeneratePolicy policy) throws Exception {
		repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPLInsuranceScore();

		return fillOutCreditReport(policy);
	}


	public void setAddress(String line1) {
		setText(find(By.xpath("//input[contains(@id, ':AddressLine1-inputEl')]")), line1);
	}

	public void setCity(String city) {
		setText(find(By.xpath("//input[contains(@id, ':City-inputEl') or contains(@id, ':city-inputEl')]")), city);
	}

	public void setState(State state) {
		new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap') or contains(@id, ':state-triggerWrap')]").selectByVisibleText(state.getName());
	}

	public void setZip(String zip) {
		setText(find(By.xpath("//input[contains(@id, ':PostalCode-inputEl')]")), zip);
	}

	public WebElement getInsuranceScoreVeriskDetailsTable(){
		return table_InsuranceScoreVeriskDetails;
	}

	public int getInsuranceScoreVeriskDetailsTableRowCount(){
		return new TableUtils(driver).getRowCount(table_InsuranceScoreVeriskDetails);
	}


	public void fillOutFormerAddress(InsuranceScoreInfo insuranceScoreInfo) {
		setAddress(insuranceScoreInfo.getFormerAddress().getLine1());
		setCity(insuranceScoreInfo.getFormerAddress().getCity());
		setState(insuranceScoreInfo.getFormerAddress().getState());
		setZip(insuranceScoreInfo.getFormerAddress().getZip());
	}

	public void fillOutFormerName(InsuranceScoreInfo insuranceScoreInfo) {
		setInsuranceFormerFirstName(insuranceScoreInfo.getFormerFirstName());
		setInsuranceFormerLastName(insuranceScoreInfo.getFormerLastName());
	}












}
