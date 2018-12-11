package repository.bc.search;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class BCSearchTroubleTickets extends BasePage {

	public BCSearchTroubleTickets(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	/**
	 * 
	 * This page is from clicking an account picker after an account editbox (may be in a table or just after an account editbox).
	 * 
	 * @throws Exception
	 */
	@FindBy(xpath = "//input[@id='TroubleTicketSearch:TroubleTicketSearchScreen:TroubleTicketSearchDV:TroubleTicketNumberCriterion-inputEl']")
	public WebElement editbox_BCSearchTroubleTicketsNumber;

	@FindBy(xpath = "//input[@id='TroubleTicketSearch:TroubleTicketSearchScreen:TroubleTicketSearchDV:HasHoldCriterion-inputEl']")
	public WebElement checkBox_BCSearchTroubleTicketsHasHolds;

	@FindBy(xpath = "//input[@id='TroubleTicketSearch:TroubleTicketSearchScreen:TroubleTicketSearchDV:AssignedToUserCriterion-inputEl']")
	public WebElement editbox_BCSearchTroubleTicketsAssignedTo;

	public void clickBCSearchTroubleTicketsHasHolds() {
		clickWhenVisible(checkBox_BCSearchTroubleTicketsHasHolds);
	}

	public void setBCSearchTroubleTicketsAssignedTo(String assignedTo) {
		clickWhenVisible(editbox_BCSearchTroubleTicketsAssignedTo);
		editbox_BCSearchTroubleTicketsAssignedTo.sendKeys(assignedTo);
	}

	public void setBCSearchTroubleTicketsNumber(String number) {
		clickWhenVisible(editbox_BCSearchTroubleTicketsNumber);
		editbox_BCSearchTroubleTicketsNumber.sendKeys(number);
	}

}
