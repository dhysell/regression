package repository.bc.common;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.gw.enums.OpenClosed;
import repository.gw.helpers.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * NOTE: This class is not the same as the BC TroubleTickets Class!!! When a user enters the TroubleTickets page
 * and clicks on a specific TroubleTicket, they are brought to the TroubleTicket (no "S") page. This class
 * is representative of the second page.
 */
public class BCCommonTroubleTicket extends BCCommonTroubleTickets {

	public BCCommonTroubleTicket(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths for Trouble Ticket Page
	// -----------------------------------------------

	@FindBy(xpath = "//div[contains(@id,'TroubleTicketDetailsPopup:TroubleTicketDetailsScreen:TroubleTicketInfoDV:CreateDate-inputEl')]")
	public WebElement label_TroubleTicketCreateDate;
	
	@FindBy(xpath = "//div[contains(@id,'TroubleTicketDetailsPopup:TroubleTicketDetailsScreen:TroubleTicketInfoDV:DueDate-inputEl')]")
	public WebElement label_TroubleTicketDueDate;
	
	@FindBy(xpath = "//div[contains(@id,'TroubleTicketDetailsPopup:TroubleTicketDetailsScreen:TroubleTicketInfoDV:Status-inputEl')]")
	public WebElement label_TroubleTicketStatus;
	
	@FindBy(xpath = "//a[contains(@id,'TroubleTicketDetailsPopup:TroubleTicketDetailsScreen:TroubleTicketDetailsPopup_CloseButton') or contains(@id,'TroubleTicketDetailsPopup:TroubleTicketDetailsScreen:TroubleTicketDetailsPopup_CloseButton-btnInnerEl')]")
	public WebElement button_TroubleTicketClose;

	// -------------------------------------------------------
	// Helper Methods for Above Elements - Trouble Tickets Page
	// -------------------------------------------------------

	public Date getTroubleTicketCreationDate() {
		Date dateToReturn = null;
		try {
			dateToReturn = DateUtils.convertStringtoDate(label_TroubleTicketCreateDate.getText(), "MM/dd/yyyy");
		} catch (ParseException e) {
			Assert.fail("Error Parsing String to convert to Date.");
		}
		return dateToReturn;
	}
	
	public Date getTroubleTicketDueDate() {
		Date dateToReturn = null;
		try {
			dateToReturn = DateUtils.convertStringtoDate(label_TroubleTicketDueDate.getText(), "MM/dd/yyyy");
		} catch (ParseException e) {
			Assert.fail("Error Parsing String to convert to Date.");
		}
		return dateToReturn;
	}
	
	public OpenClosed getTTStatus() {
		OpenClosed openClosed = null;
		String textInStatus = label_TroubleTicketStatus.getText();
		if(textInStatus.contains("Open")) {
			openClosed=OpenClosed.Open;
		}else if(textInStatus.contains("Closed")) {	
			openClosed=OpenClosed.Closed;
		}
	return openClosed;
	}
	
	public Boolean verifyIfTroubleTicketIsInClosedStatus() {
		Boolean closed = false;
		String textInStatus = label_TroubleTicketStatus.getText();
		if(textInStatus.contains("Closed")) {	
			closed=true;
		}
	return closed;
	}
	public Boolean verifyIfTroubleTicketIsInOpenStatus() {
		Boolean open = false;
		String textInStatus = label_TroubleTicketStatus.getText();
		if(textInStatus.contains("Open")) {	
			open=true;
		}
	return open;
	}
	
	public void clickTroubleTicketCloseButton() {
		clickWhenVisible(button_TroubleTicketClose);
		selectOKOrCancelFromPopup(OkCancel.OK);
	}
}