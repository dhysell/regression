package repository.bc.account;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.bc.common.BCCommonHistory;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.HistoryFilterByDate;
import repository.gw.enums.HistoryFilterByType;

public class BCAccountHistory extends BCCommonHistory {
	
	private WebDriver driver;

	public BCAccountHistory(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	//////////////////////////////////////////
	// "Recorded Elements" and their XPaths //
	//////////////////////////////////////////

	public Guidewire8Select select_HistoryFilterByDate() {
		return new Guidewire8Select(driver, "//table[contains(@id,'HistoryLV:DateFilter-triggerWrap')]");
	}

	public Guidewire8Select select_HistoryFilterByType() {
		return new Guidewire8Select(driver, "//table[contains(@id,'HistoryLV:HistoryEventTypeFilter-triggerWrap')]");
	}

	///////////////////////////////////////
	// Helper Methods for Above Elements //
	///////////////////////////////////////
	
	public void selectAccountHistoryFilterByDate(HistoryFilterByDate date) {
		select_HistoryFilterByDate().selectByVisibleText(date.getValue());
	}

	public void selectAccountHistoryFilterByType(HistoryFilterByType type) {
		select_HistoryFilterByType().selectByVisibleText(type.getValue());
	}
}
