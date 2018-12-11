package repository.bc.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

public class AdminCommissionPlans extends BasePage {
	
	private WebDriver driver;

	public AdminCommissionPlans(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths
	// -----------------------------------------------

	public Guidewire8Select comboBox_AdminCommissionPlan() throws Exception {
		return new Guidewire8Select(driver, "//table[contains(@id,'CommissionPlans:CommissionPlansScreen:CommissionPlansLV:Filter-triggerWrap')]");
	}

	// -------------------------------------------------------
	// Helper Methods for Above Elements - Trouble Tickets Page
	// -------------------------------------------------------

	public void setAdminCommissionPlanShowStatus(String selectString) {
		try {
			comboBox_AdminCommissionPlan().selectByVisibleText(selectString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
