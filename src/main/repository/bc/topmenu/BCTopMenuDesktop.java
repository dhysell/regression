package repository.bc.topmenu;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class BCTopMenuDesktop extends BCTopMenu {

	public BCTopMenuDesktop(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	@FindBy(how = How.XPATH, using = "//a[starts-with(@id,'TabBar:DesktopTab:DesktopGroup_DesktopApprovals-itemEl')]")
	public WebElement link_MenuDesktopApprovalStatuses;

	@FindBy(how = How.XPATH, using = "//a[starts-with(@id,'TabBar:DesktopTab:DesktopGroup_DesktopDisbursements-itemEl')]")
	public WebElement link_MenuDesktopDisbursements;

	@FindBy(how = How.XPATH, using = "//a[starts-with(@id,'TabBar:DesktopTab:DesktopGroup_DesktopHeldCharges-itemEl')]")
	public WebElement link_MenuDesktopHeldCharges;

	@FindBy(how = How.XPATH, using = "//a[starts-with(@id,'TabBar:DesktopTab:DesktopGroup_DesktopActivities-itemEl')]")
	public WebElement link_MenuDesktopMyActivities;

	@FindBy(how = How.XPATH, using = "//a[starts-with(@id,'TabBar:DesktopTab:DesktopGroup_DesktopDelinquencies-itemEl')]")
	public WebElement link_MenuDesktopMyDelinquencies;

	@FindBy(how = How.XPATH, using = "//a[starts-with(@id,'TabBar:DesktopTab:DesktopGroup_DesktopQueues-itemEl')]")
	public WebElement link_MenuDesktopMyQueues;

	@FindBy(how = How.XPATH, using = "//a[starts-with(@id,'TabBar:DesktopTab:DesktopGroup_DesktopTroubleTickets-itemEl')]")
	public WebElement link_MenuDesktopMyTroubleTickets;

	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------

	public void clickApprovalStatuses() {
		clickDesktopArrow();
		
		clickWhenVisible(link_MenuDesktopApprovalStatuses);
		
	}

	public void clickDisbursements() {
		clickDesktopArrow();
		
		clickWhenVisible(link_MenuDesktopDisbursements);
		
	}

	public void clickHeldCharges() {
		clickDesktopArrow();
		
		clickWhenVisible(link_MenuDesktopHeldCharges);
		
	}

	public void clickMyActivities() {
		clickDesktopArrow();
		
		clickWhenVisible(link_MenuDesktopMyActivities);
		
	}

	public void clickMyDelinquencies() {
		clickDesktopArrow();
		
		clickWhenVisible(link_MenuDesktopMyDelinquencies);
		
	}

	public void clickMyQueues() {
		clickDesktopArrow();
		
		clickWhenVisible(link_MenuDesktopMyQueues);
		
	}

	public void clickMyTroubleTickets() {
		clickDesktopArrow();
		
		clickWhenVisible(link_MenuDesktopMyTroubleTickets);
		
	}
}
