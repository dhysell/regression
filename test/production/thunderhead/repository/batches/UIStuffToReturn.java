package production.thunderhead.repository.batches;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;

public class UIStuffToReturn extends BasePage {
	
	public UIStuffToReturn(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

	private String[] batchIds;
	private String[] batchNames;
	
	public String[] getBatchIds() {
		return batchIds;
	}
	
	public void setBatchIds(String[] batchIds) {
		this.batchIds = batchIds;
	}
	
	public String[] getBatchNames() {
		return batchNames;
	}
	
	public void setBatchNames(String[] batchNames) {
		this.batchNames = batchNames;
	}
}
