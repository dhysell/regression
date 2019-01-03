package repository.bc.desktop.actions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class DesktopActionsActivityReminder extends CommonActionsActivity  {
	
	public DesktopActionsActivityReminder(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	
	public void clickActivityReminderCancel() {
		super.clickCancel();
	}

	
	public void clickActivityReminderUpdate() {
		super.clickUpdate();
	}

	
	public void setActivityReminderSubject(String subjectToFill) {
		super.setDesktopActionsActivitySubject(subjectToFill);
	}

	
	public void setActivityReminderDescription(String descriptionToFill) {
		super.setDesktopActionsActivityDescription(descriptionToFill);
	}

	
	public void setActivityReminderPriority(String priorityToSelect) {
		super.setDesktopActionsActivityPriority(priorityToSelect);
	}

	
	public void setActivityReminderDueDate(String dateToFill) {
		super.setDesktopActionsActivityDueDate(dateToFill);
	}

	
	public void setActivityReminderEscalationDate(String dateToFill) {
		super.setDesktopActionsActivityEscalationDate(dateToFill);
	}

	
	public void setActivityReminderAccount(String numToFill) {
		super.setDesktopActionsActivityAccount(numToFill);
	}

	
	public void setActivityReminderPolicyPeriod(String periodToFill) {
		super.setDesktopActionsActivityPolicyPeriod(periodToFill);
	}

	
	public void setActivityReminderAssignedTo(String nameToFill) {
		super.setDesktopActionsActivityAssignedTo(nameToFill);
	}

	
	public void clickActivityReminderAccountPicker() {
		super.clickDesktopActionsActivityAccountPicker();
	}

	
	public void clickActivityReminderPolicyPeriocPicker() {
		super.clickDesktopActionsActivityPolicyPeriodPicker();
	}

	
	public void clickActivityReminderAssignedToPicker() {
		super.clickDesktopActionsActivityAssignedToPicker();
	}

}
