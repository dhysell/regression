package production.thunderhead.repository.overview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;

public class Overview extends BasePage {
    public Overview(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	@FindBy(xpath="//a[@href='/thunderhead/spring/admin.jsp?method=batchesByStatus&batchStatus=ERRORED']")
	public WebElement link_OverviewErroredBatches;
	
	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------
	public void clickOverviewErroredBatches() {
		clickWhenClickable(link_OverviewErroredBatches);
	}
}
