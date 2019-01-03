package repository.bc.policy;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

public class PolicyCommissions extends BasePage {
	
	private WebDriver driver;

	public PolicyCommissions(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	//////////////////////////////////////////
	// "Recorded Elements" and their XPaths //
	//////////////////////////////////////////

	@FindBy(xpath = "//a[@id='PolicyDetailCommissions:PolicyDetailCommissionsScreen:PolicyCommissionsDetailPanelDV:0:PolicyDetailCommissionsProducerInfoInputSet:ProducerName-inputEl']")
	public WebElement link_PolicyCommissionsTransferPolicy;

	@FindBy(xpath = "//a[@id='PolicyDetailCommissions:PolicyDetailCommissionsScreen:PolicyCommissionsDetailPanelDV:0:PolicyDetailCommissionsProducerInfoInputSet:CommissionPlan-inputEl']")
	public WebElement link_PolicyCommissionsPlan;
	
	@FindBy(xpath = "//a[@id='PolicyDetailCommissions:PolicyDetailCommissionsScreen:TransferPolicy']")
	public WebElement button_PolicyCommissionsTransferPolicy;

	Guidewire8Select select_Producer() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':PolicyDetailCommissionsScreen:TransactionsLV:Producer-triggerWrap')]");
	}

	//////////////////////////////////////
	// Helper Methods for Above Elements//
	//////////////////////////////////////
	
	public void setProducer(String producer) {
		select_Producer().selectByVisibleText(producer);
	}

	public void clickPolicyCommissionsTransferPolicy() {
		clickWhenVisible(button_PolicyCommissionsTransferPolicy);
	}

	public void clickPolicyCommissionsProducerName() {
		clickWhenVisible(link_PolicyCommissionsTransferPolicy);
	}

	public void clickPolicyCommissionsPlan() {
		clickWhenVisible(link_PolicyCommissionsPlan);
	}
}
