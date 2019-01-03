package repository.bc.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class BCCommonContacts extends BasePage {

	public BCCommonContacts(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	//////////////////////////////////////////
	// "Recorded Elements" and their XPaths //
	//////////////////////////////////////////
	
	@FindBy(xpath = "//div[@id='AccountDetailContacts:AccountDetailContactsScreen:DetailPanel:AccountContactCV:ContactDetailCardTab']")
	public WebElement tab_AccountContactInfo;

	@FindBy(xpath = "//div[@id='AccountDetailContacts:AccountDetailContactsScreen:DetailPanel:AccountContactCV:ContactDetailCardTab']")
	public WebElement tab_AccountContactCorrespondence;

	@FindBy(xpath = "//div[text() = 'Agent']/../../td[2]/div")
	public WebElement textbox_AgentName;

	///////////////////////////////////////
	// Helper Methods for Above Elements //
	///////////////////////////////////////
	
	public void clickAccountContactsContactInfo() {
		clickWhenVisible(tab_AccountContactInfo);
	}

	public void clickAccountContactsCorrespondence() {
		clickWhenVisible(tab_AccountContactCorrespondence);
	}

	public String getAgentName() {
		return textbox_AgentName.getText();
	}
}
