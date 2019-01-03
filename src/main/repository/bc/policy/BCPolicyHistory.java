package repository.bc.policy;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.bc.common.BCCommonHistory;
import repository.gw.elements.Guidewire8Select;

public class BCPolicyHistory extends BCCommonHistory {
	
	private WebDriver driver;

	public BCPolicyHistory(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	//////////////////////////////////////////
	// "Recorded Elements" and their XPaths //
	//////////////////////////////////////////
	
	public Guidewire8Select select_HistoryFilterByTerm() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'HistoryLV:ActiveFilter-triggerWrap')]");
	}

	///////////////////////////////////////
	// Helper Methods for Above Elements //
	///////////////////////////////////////
	
	public void selectHistoryFilterByTerm(String term) {
		select_HistoryFilterByTerm().selectByVisibleText(term);
	}
}
