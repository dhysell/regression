package repository.pc.workorders.generic;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderPolicyInfo2 extends BasePage {
	
	private WebDriver driver;
	private TableUtils tableUtils;
	
	public GenericWorkorderPolicyInfo2(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}
	
	
	
	//ORGANIZTION TYPE
	private Guidewire8Select select_SubmissionPolicyInfoOrganizationType() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'AccountInfoInputSet:OrganizationType-triggerWrap')]");
	}
	public void setPolicyInfoOrganizationType(OrganizationType polOrgType) {
		Guidewire8Select mySelect = select_SubmissionPolicyInfoOrganizationType();
		mySelect.selectByVisibleText(polOrgType.getValue());
	}
	
	//PNI INFO
	public void fillOutPrimaryNamedInsuredInfo(GeneratePolicy policy) {
		
	}
	
	//BUSINESS AND OPPERATIONS
	public void fillOutBusinessAndOperations(GeneratePolicy policy) {
		setPolicyInfoYearBusinessStarted(policy.yearBusinessStarted);
		setPolicyInfoDescriptionOfOperations(policy.descriptionOfOperations);
	}
	
	public void fillOutPolicyDetails(GeneratePolicy policy) {
		
	}
	
	public void fillOutAgentOfRecordAndService(GeneratePolicy policy) {
		
	}
	
	public void fillOutAdditionalNamedInsured(GeneratePolicy policy) {
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@FindBy(xpath = "//input[contains(@id, 'AccountInfoInputSet:BOPInputSet:YearBusinessStarted-inputEl')]")
	private WebElement editbox_SubmissionPolicyInfoYearBusinessStarted;
	private void setPolicyInfoYearBusinessStarted(String yearBusinessStarted) {
		waitUntilElementIsClickable(editbox_SubmissionPolicyInfoYearBusinessStarted);
		editbox_SubmissionPolicyInfoYearBusinessStarted.click();
		editbox_SubmissionPolicyInfoYearBusinessStarted.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_SubmissionPolicyInfoYearBusinessStarted.sendKeys(yearBusinessStarted);
	}

	@FindBy(xpath = "//textarea[contains(@id, 'AccountInfoInputSet:BOPInputSet:BusOps-inputEl')]")
	private WebElement editbox_SubmissionPolicyInfoDescriptionOfOperations;
	private void setPolicyInfoDescriptionOfOperations(String descriptionOfOperations) {
		if (descriptionOfOperations == null) {
			descriptionOfOperations = "They operate their operation in the operable fashion of operators";
		}
		waitUntilElementIsVisible(editbox_SubmissionPolicyInfoDescriptionOfOperations);
		editbox_SubmissionPolicyInfoDescriptionOfOperations.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_SubmissionPolicyInfoDescriptionOfOperations.sendKeys(descriptionOfOperations);
	}
	

}
