package repository.bc.policy;

import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.common.BCCommonMenu;
import repository.bc.common.actions.BCCommonActionsManualActivity;
import repository.gw.enums.PARCActivtyType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.infobar.InfoBar;

public class BCPolicyMenu extends BCCommonMenu {

	public BCPolicyMenu(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	
	// elements for Actions Drop Down
	@FindBy(xpath = "//a[@id='PolicyGroup:PolicyDetailMenuActions:PolicyDetailMenuActions_BillingInstruction-itemEl']")
	public WebElement link_PolicyActionsBillingInstructions;
	
	@FindBy(xpath = "//a[@id='PolicyGroup:PolicyDetailMenuActions:NewEmail-itemEl']")
	public WebElement link_PolicyActionsNewEmail;
	//End Actions Drop Down Block
	
	@FindBy(xpath = "//td[contains(@id,':PolicyGroup_PolicyDetailPayments')]")
	public WebElement link_PaymentSchedule;
	
	@FindBy(xpath = "//td[contains(@id,':PolicyGroup_PolicyDetailNotes')]")
	public WebElement link_PolicyNotes;

	//The following elements are for use as the PCUser User
	@FindBy(xpath = "//td[contains(@id,':PolicyGroup_PolicyDetailInsuredPayments')]")
	public WebElement link_InsuredPayments;

	@FindBy(xpath = "//td[contains(@id,':PolicyGroup_PolicyDetailLienholderPayments')]")
	public WebElement link_LienholderPayments;
	//end PCUser Block
	
	@FindBy(xpath = "//a[contains(@id,':PolicyInfoBar:AccountNumber')]")
	public WebElement link_TopInfoBarAccountNumber;

	@FindBy(xpath = "//a[@id='PolicyGroup:PolicyDetailMenuActions:0:NewActivityMenuItemSet_Category-itemEl']")
	public WebElement link_PolicyMenuActionsPARC;

	@FindBy(xpath = "//*[@id='PolicyGroup:PolicyDetailMenuActions:0:NewActivityMenuItemSet_Category:0:item-textEl']")
	public WebElement link_PolicyMenuActionsPARCChangeEFTDraftDate;

	@FindBy(xpath = "//*[@id='PolicyGroup:PolicyDetailMenuActions:0:NewActivityMenuItemSet_Category:1:item-textEl']")
	public WebElement link_PolicyMenuActionsPARCChangePaymentPlan;

	@FindBy(xpath = "//*[@id='PolicyGroup:PolicyDetailMenuActions:0:NewActivityMenuItemSet_Category:2:item-textEl']")
	public WebElement link_PolicyMenuActionsPARCIncreasedBilledInvoice;

	@FindBy(xpath = "//*[@id='PolicyGroup:PolicyDetailMenuActions:0:NewActivityMenuItemSet_Category:3:item-textEl']")
	public WebElement link_PolicyMenuActionsPARCIntercompanyTransferPayment;

	@FindBy(xpath = "//*[@id='PolicyGroup:PolicyDetailMenuActions:0:NewActivityMenuItemSet_Category:4:item-textEl']")
	public WebElement link_PolicyMenuActionsPARCOtherPARCMessage;

	@FindBy(xpath = "//*[@id='PolicyGroup:PolicyDetailMenuActions:0:NewActivityMenuItemSet_Category:5:item-textEl']")
	public WebElement link_PolicyMenuActionsPARCRe_RunEFT;

	@FindBy(xpath = "//*[@id='PolicyGroup:PolicyDetailMenuActions:0:NewActivityMenuItemSet_Category:6:item-textEl']")
	public WebElement link_PolicyMenuActionsPARCStopEFTCancel;

	@FindBy(xpath = "//*[@id='PolicyGroup:PolicyDetailMenuActions:0:NewActivityMenuItemSet_Category:7:item-textEl']")
	public WebElement link_PolicyMenuActionsPARCStopEFTNewBank;


	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------

	public void clickPaymentSchedule() {
		link_PaymentSchedule.click();
	}

	public void clickPolicyNotes() {
		clickWhenClickable(link_PolicyNotes);
	}

	// The following menu items only exist in the 'pcuser' account for Billing
	// Center. Please do not use these methods unless you are logged in as
	// pcuser.
	public void clickInsuredPayments() {
		link_InsuredPayments.click();
	}

	public void clickLienholderPayments() {
		link_LienholderPayments.click();
	}
	//End PCUser Block
	
	public void clickTopInfoBarAccountNumber() {
		clickWhenClickable(link_TopInfoBarAccountNumber);
	}

	public void clickAndCreatePARCActivity(PARCActivtyType typeOfActivity ) {
		clickBCMenuActions();
		
		hoverOver(link_PolicyMenuActionsPARC);
		
		link_PolicyMenuActionsPARC.click();
		
		repository.bc.common.actions.BCCommonActionsManualActivity bcCommonActionsManualActivity = new BCCommonActionsManualActivity(getDriver());
		switch (typeOfActivity) {
			case Change_EFT_Draft_Date:
				hoverOver(link_PolicyMenuActionsPARCChangeEFTDraftDate);
				
				link_PolicyMenuActionsPARCChangeEFTDraftDate.click();
				
				setText(bcCommonActionsManualActivity.editBox_ManualActivityPARCDateInput,DateUtils.dateFormatAsString("MM/dd/yyyy",DateUtils.getCenterDate(getDriver(),ApplicationOrCenter.BillingCenter)));
				break;
			case Change_Payment_Plan:
				hoverOver(link_PolicyMenuActionsPARCChangePaymentPlan);
				
				link_PolicyMenuActionsPARCChangePaymentPlan.click();
				
				bcCommonActionsManualActivity.selectNewPaymentPlan(PaymentPlanType.getRandom());
				
				setText(bcCommonActionsManualActivity.editBox_ManualActivityPARCDateInput,DateUtils.dateFormatAsString("MM/dd/yyyy",DateUtils.getCenterDate(getDriver(),ApplicationOrCenter.BillingCenter)));
				break;
			case Increased_Billed_Invoice:
				hoverOver(link_PolicyMenuActionsPARCIncreasedBilledInvoice);
				
				link_PolicyMenuActionsPARCIncreasedBilledInvoice.click();
				
				setText(bcCommonActionsManualActivity.editBox_ManualActivityPARCDateInput,DateUtils.dateFormatAsString("MM/dd/yyyy",DateUtils.getCenterDate(getDriver(),ApplicationOrCenter.BillingCenter)));
				
				setText(bcCommonActionsManualActivity.editBox_ManualActivityPARCDraftAmount,String.valueOf(NumberUtils.generateRandomNumberInt(100,999)));
				break;
			case Intercompany_Transfer_Payment:
				hoverOver(link_PolicyMenuActionsPARCIntercompanyTransferPayment);
				
				link_PolicyMenuActionsPARCIntercompanyTransferPayment.click();
				
				setText(bcCommonActionsManualActivity.editBox_ManualActivityPARCDraftAmount,String.valueOf(NumberUtils.generateRandomNumberInt(100,999)));
				
				setText(bcCommonActionsManualActivity.editBox_ManualActivityPARCTargetPolicy,new InfoBar(getDriver()).getInfoBarPolicyNumber().substring(8,20));
				break;
			case Other_PARC_Message:
				hoverOver(link_PolicyMenuActionsPARCOtherPARCMessage);
				
				link_PolicyMenuActionsPARCOtherPARCMessage.click();
				
				bcCommonActionsManualActivity.setManualActivityDescription("This is only for Other PARC Message");
				break;
			case Re_RunEFT:
				hoverOver(link_PolicyMenuActionsPARCRe_RunEFT);
				
				link_PolicyMenuActionsPARCRe_RunEFT.click();
				
				setText(bcCommonActionsManualActivity.editBox_ManualActivityPARCDateInput,DateUtils.dateFormatAsString("MM/dd/yyyy",DateUtils.getCenterDate(getDriver(),ApplicationOrCenter.BillingCenter)));
				
				setText(bcCommonActionsManualActivity.editBox_ManualActivityPARCDraftAmount,String.valueOf(NumberUtils.generateRandomNumberInt(100,999)));
				break;
			case Stop_EFT_Cancel:
				hoverOver(link_PolicyMenuActionsPARCStopEFTCancel);
				
				link_PolicyMenuActionsPARCStopEFTCancel.click();
				
				setText(bcCommonActionsManualActivity.editBox_ManualActivityPARCDateInput,DateUtils.dateFormatAsString("MM/dd/yyyy",DateUtils.getCenterDate(getDriver(),ApplicationOrCenter.BillingCenter)));
				break;
			case Stop_EFT_New_Bank:
				hoverOver(link_PolicyMenuActionsPARCStopEFTNewBank);
				
				link_PolicyMenuActionsPARCStopEFTNewBank.click();
				
				setText(bcCommonActionsManualActivity.editBox_ManualActivityPARCDateInput,DateUtils.dateFormatAsString("MM/dd/yyyy",DateUtils.getCenterDate(getDriver(),ApplicationOrCenter.BillingCenter)));
				break;
		}
		
        bcCommonActionsManualActivity.clickUpdate();
		
	}
}