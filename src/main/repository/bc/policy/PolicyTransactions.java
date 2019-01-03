package repository.bc.policy;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.TransactionFilter;

public class PolicyTransactions extends BasePage {
	
	private WebDriver driver;

	public PolicyTransactions(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	//////////////////////////////////////////
	// "Recorded Elements" and their XPaths //
	//////////////////////////////////////////

	Guidewire8Select select_Show() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':TransactionDetailsLV:Filter-triggerWrap')]");
	}
	
	//////////////////////////////////////
	// Helper Methods for Above Elements//
	//////////////////////////////////////

	public void selectDateRange(TransactionFilter range) {
		select_Show().selectByVisibleText(range.getValue());
	}
}
