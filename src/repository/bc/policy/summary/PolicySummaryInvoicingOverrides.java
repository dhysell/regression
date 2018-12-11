package repository.bc.policy.summary;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

public class PolicySummaryInvoicingOverrides extends BasePage {


	private WebDriver driver;
	public PolicySummaryInvoicingOverrides(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/////////////////////////
	// xPaths for Elements //
	/////////////////////////

	@FindBy(xpath = "//input[contains(@id, ':OverridingPayerAccount-inputEl')]")
	private WebElement editBox_OverridingPayer;

	Guidewire8Select select_OverridingInvoiceStream() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':OverridingInvoiceStream-triggerWrap')]");
	}

	///////////////////////////////
	// Helper Methods for xPaths //
	///////////////////////////////

	public void selectOverridingInvoiceStream(String overridingInvoiceStream) {
		Guidewire8Select mySelect = select_OverridingInvoiceStream();
		mySelect.selectByVisibleTextPartial(overridingInvoiceStream);
	}

	public void overrideInvoiceStream(String overridingInvoiceStream) {
		Guidewire8Select mySelect = select_OverridingInvoiceStream();
		mySelect.selectByVisibleTextPartial(overridingInvoiceStream);
		clickNext();
		clickFinish();
	}

	public boolean checkIfOverridingPayerEditboxExists() {
		return checkIfElementExists(editBox_OverridingPayer, 1000);
	}
}
