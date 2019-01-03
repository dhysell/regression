package repository.bc.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class BCCommonAccountSearchPopUp extends BasePage {

	public BCCommonAccountSearchPopUp(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	/**
	 * 
	 * This page is from clicking an account picker after an account editbox (may be in a table or just after an account editbox).
	 * 
	 */

	// clickAccountSearchTableSelectButton

	public void clickAccountPickerTableSelectButton(int rowNumber) {
		find(By.xpath("//a[contains(@id, 'AccountSearchResultsLV:" + (rowNumber - 1) + ":_Select')]")).click();
	}

	public String getAccountSearchResultsAccountNumber(int rowNumber) {
		return find(By.xpath("//div[contains(@id, 'AccountSearchResultsLV-body')]/div/table[starts-with(@id,'gridview-')]/tbody/tr[" + rowNumber + "]/td[3]")).getText();

	}

}
