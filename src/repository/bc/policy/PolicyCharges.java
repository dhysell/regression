package repository.bc.policy;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.bc.common.BCCommonCharges;

public class PolicyCharges extends BCCommonCharges{

	public PolicyCharges(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	////////////////////////////
	// WebElements and xPaths //
	////////////////////////////

	////////////////////
	// Helper Methods //
	////////////////////
}
