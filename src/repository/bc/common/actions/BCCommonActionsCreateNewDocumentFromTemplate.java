package repository.bc.common.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DocumentTemplateName;
import repository.gw.enums.DocumentType;
import repository.gw.enums.ReturnCheckReason;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;

public class BCCommonActionsCreateNewDocumentFromTemplate extends BasePage {
	
	private WebDriver driver;
	
	public BCCommonActionsCreateNewDocumentFromTemplate(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	////////////////////////////////
	// Elements And Recorded Xpaths//
	////////////////////////////////

	@FindBy(xpath = "//a[contains(@id, 'NewTemplateDocumentDV:CreateDocument')]")
	private WebElement button_CreateDocument;

	@FindBy(xpath = "//div[contains(@id, 'NewTemplateDocumentDV:TemplatePicker:SelectTemplatePicker')]")
	private WebElement button_DocumentTemplatePicker;

	@FindBy(xpath = "//input[contains(@id, 'DocumentTemplateSearchScreen:DocumentTemplateSearchDV:Keywords-inputEl')]")
	private WebElement editbox_Keywords;

	@FindBy(xpath = "//input[contains(@id, 'DocumentTemplateSearchScreen:DocumentTemplateSearchDV:EffectiveDate-inputEl') or contains(@id, ':NewTemplateDocumentInputSet:EffectiveDate-inputEl')]")
	private WebElement editbox_EffectiveDate;

	private Guidewire8Select select_DocumentTemplateType() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'DocumentTemplateSearchDV:DocumentType-triggerWrap')]");
	}	
	
	private Guidewire8Select select_RelatedTo() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':NewTemplateDocumentDV:DocumentMetadataInputSet:RelatedTo-triggerWrap')]");
	}
	
	private Guidewire8Select select_ReturnReason() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':NewTemplateDocumentInputSet:ReturnedReason-triggerWrap')]");
	}
	
	@FindBy(xpath = "//input[contains(@id, ':NewTemplateDocumentInputSet:PaymentAmount-inputEl')]")
	private WebElement editBox_PaymentAmount;	
	
	@FindBy(xpath = "//input[contains(@id, ':NewTemplateDocumentInputSet:ConfirmationNumber-inputEl')]")
	private WebElement editBox_ConfirmationNumber;	
	
	@FindBy(xpath = "//input[contains(@id, ':NewTemplateDocumentInputSet:Copies-inputEl')]")
	private WebElement editBox_Copies;	
	
	@FindBy(xpath = "//input[contains(@id, ':NewTemplateDocumentInputSet:ConfirmationNumber-inputEl')]")
	private WebElement editBox_CheckNumber;
	
	@FindBy(xpath = "//input[contains(@id, ':NewTemplateDocumentInputSet:AsOfDate-inputEl')]")
	private WebElement editbox_LastPaymentDate;
	
	@FindBy(xpath = "//input[contains(@id, ':NewTemplateDocumentInputSet:Other-inputEl')]")
	private WebElement editbox_ReturnedReasonOther;
	
	@FindBy(xpath = "//div[(contains(@id, 'AccountSearchResultsLV')) or (contains(@id, 'DocumentTemplateSearchResultsLV')) or (contains(@id, 'PolicySearchResultsLV')) or (contains(@id, 'PolicySearch_ResultsLV')) or (contains(@id, 'InvoiceSearchResultsLV')) or (contains(@id, 'LienholderSearchPanelSet:2')) or (contains(@id, 'ContactSearchPopup:ContactSearchScreen')) or (contains(@id, 'ContactSearchLV_FBMPanelSet:1')) or (contains(@id, 'ClaimSearchScreen:ClaimSearchResultsLV'))]")
	private WebElement table_SearchSearchResults;
	////////////////////////////////////////////
	// Helper Methods To Interact With Elements//
	////////////////////////////////////////////
	
	
	
	public void selectDocumentTemplateType(DocumentTemplateName documentName) {
		select_DocumentTemplateType().selectByVisibleTextPartial(documentName.getValue());
	}
	public int getTemplateTypesNumber() {
		clickDocumentTemplatePicker();
		waitForPostBack();
		return select_DocumentTemplateType().getList().size();
	}
	
	public void selectRelatedTo(String accountNum) {
		select_RelatedTo().selectByVisibleTextPartial(accountNum);
	}
	
	
	
	public void selectReturnReason(ReturnCheckReason reason) {
		select_ReturnReason().selectByVisibleText(reason.getValue());
	}
	
	public void setPaymentAmount(double amount) {		
		clickWhenVisible(editBox_PaymentAmount);
		editBox_PaymentAmount.sendKeys(Keys.HOME);
		editBox_PaymentAmount.sendKeys(String.valueOf(amount));
		
	}
	
	public void setConfirmationNumber(String Number) {		
		clickWhenVisible(editBox_ConfirmationNumber);
		editBox_ConfirmationNumber.sendKeys(Keys.HOME);
		editBox_ConfirmationNumber.sendKeys(Number);
		
	}
	
	public void setCopies(String numberOfCopies) {		
		clickWhenVisible(editBox_Copies);
		editBox_Copies.sendKeys(Keys.HOME);
		editBox_Copies.sendKeys(numberOfCopies);
		
	}
	
	public void setCheckNumber(String checkNumber) {		
		clickWhenVisible(editBox_CheckNumber);
		editBox_CheckNumber.sendKeys(Keys.HOME);
		editBox_CheckNumber.sendKeys(checkNumber);
		
	}
	
	
	public void setReturnedReasonOther(String other) {		
		clickWhenVisible(editbox_ReturnedReasonOther);		
		editbox_ReturnedReasonOther.sendKeys(other);
		
	}
	
	public void clickUpdate() {
		super.clickUpdate();
	}

	
	public void clickCancel() {
		super.clickCancel();
	}

	
	public void clickClose() {
		super.clickClose();
	}

	
	public void clickSearch() {
		super.clickSearch();
	}

	
	public void clickReset() {
		super.clickReset();
	}

	
	public void clickCreateDocument() {
		clickWhenClickable(button_CreateDocument);
		
	}

	
	public void clickDocumentTemplatePicker() {
		clickWhenClickable(button_DocumentTemplatePicker);
		
	}

	
	public void selectDocumentTemplateType(DocumentType documentType) {
		Guidewire8Select mySelect = select_DocumentTemplateType();
		mySelect.selectByVisibleText(documentType.getValue());
	}

	
	public void setTemplateSearchKeywords(String keywords) {
		clickWhenClickable(editbox_Keywords);
		editbox_EffectiveDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_Keywords.sendKeys(keywords);
	}

	
	public void setEffectiveDate(Date efectiveDate) {
		clickWhenClickable(editbox_EffectiveDate);
		editbox_EffectiveDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_EffectiveDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", efectiveDate));
	}
	
	public void setLastPaymentDate(Date lastPaymentDate) {
		clickWhenClickable(editbox_LastPaymentDate);
		editbox_LastPaymentDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_LastPaymentDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", lastPaymentDate));
	}

	
	public String selectRandomResultFromSearchResults() {
		int randomRow = NumberUtils.generateRandomNumberInt(1, new TableUtils(getDriver()).getRowCount(table_SearchSearchResults));
		String documentTemplateName = getDocumentTemplateName(randomRow);
		selectDocumentTemplate(randomRow);
		return documentTemplateName;
	}

	
	public void selectDocumentTemplate(int rowNumber) {
		new TableUtils(getDriver()).clickSelectLinkInTable(table_SearchSearchResults, rowNumber);
	}

	
	public String getDocumentTemplateName(int rowNumber) {
		String documentNameGridColumnID = new TableUtils(getDriver()).getGridColumnFromTable(table_SearchSearchResults, "Name");
		return table_SearchSearchResults.findElement(By.xpath(".//tr[contains(@data-recordindex, '" + (rowNumber - 1)
				+ "')]/td[contains(@class, '" + documentNameGridColumnID + "')]/div")).getText();
	}

	
	public String selectDocumentTemplate(DocumentType documentType) {
		clickDocumentTemplatePicker();
		
		selectDocumentTemplateType(documentType);
		clickSearch();
		
		return selectRandomResultFromSearchResults();
	}
	
	public DocumentTemplateName randomSelectDocumentTemplate(DocumentType documentType) {
		String documentName=selectDocumentTemplate(documentType);
		for (DocumentTemplateName name : DocumentTemplateName.values()) {
            if(documentName.equals(name.getValue()))
            	return name;
		}
		return null;		
	}}
	

