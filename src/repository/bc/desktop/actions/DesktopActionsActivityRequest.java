package repository.bc.desktop.actions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class DesktopActionsActivityRequest extends CommonActionsActivity  {
	
	public DesktopActionsActivityRequest(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	
	public void clickActivityRequestCancel() {
		super.clickCancel();
	}

	
	public void clickActivityRequestUpdate() {
		super.clickUpdate();
	}

	
	public void setActivityRequestSubject(String subjectToFill) {
		super.setDesktopActionsActivitySubject(subjectToFill);
	}

	
	public void setActivityRequestDescription(String descriptionToFill) {
		super.setDesktopActionsActivityDescription(descriptionToFill);
	}

	
	public void setActivityRequestPriority(String priorityToSelect) {
		super.setDesktopActionsActivityPriority(priorityToSelect);
	}

	
	public void setActivityRequestDueDate(String dateToFill) {
		super.setDesktopActionsActivityDueDate(dateToFill);
	}

	
	public void setActivityRequestEscalationDate(String dateToFill) {
		super.setDesktopActionsActivityEscalationDate(dateToFill);
	}

	
	public void setActivityRequestAccount(String numToFill) {
		super.setDesktopActionsActivityAccount(numToFill);
	}

	
	public void setActivityRequestPolicyPeriod(String periodToFill) {
		super.setDesktopActionsActivityPolicyPeriod(periodToFill);
	}

	
	public void setActivityRequestAssignedTo(String nameToFill) {
		super.setDesktopActionsActivityAssignedTo(nameToFill);
	}

	
	public void clickActivityRequestAccountPicker() {
		super.clickDesktopActionsActivityAccountPicker();
	}

	
	public void clickActivityRequestPolicyPeriocPicker() {
		super.clickDesktopActionsActivityPolicyPeriodPicker();
	}

	
	public void clickActivityRequestAssignedToPicker() {
		super.clickDesktopActionsActivityAssignedToPicker();
	}

	
	public void randomSetAssignedToCombobox() {
		super.randomSetDesktopActionsActivityAssignedTo();

	}

	
	public String getCurrentTextInAssignedToCombobox() {
		return super.getCurrentItemInAssignedToCombobox();

	}

}
