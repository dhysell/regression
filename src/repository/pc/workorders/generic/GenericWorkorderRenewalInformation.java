package repository.pc.workorders.generic;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Optional;

import repository.driverConfiguration.BasePage;


public class GenericWorkorderRenewalInformation extends BasePage {

    public GenericWorkorderRenewalInformation(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
    
    repository.pc.workorders.generic.GenericWorkorder genWO = new GenericWorkorder(getDriver());
	
	@FindBy(xpath = "//div[text()='Custom Farming']/parent::td/following-sibling::td/div")
    private WebElement clickbox_CustomFarmingLastYearsReciepts;
	
	@FindBy(xpath = "//input[@name='LastYearsReceipt']")
    private WebElement input_LastyearsAmount;
	
	@FindBy(xpath = "//div[text()='Custom Farming']/parent::td/following-sibling::td[2]/div")
    private WebElement clickbox_CustomFarmingRenewalEstimate;
	
	@FindBy(xpath = "//input[@name='RenewalEstimatedReceipt']")
    private WebElement input_RenewalEstimateAmount;
	
	
	
	@FindBy(xpath = "//div[contains(@id, ':RenewalPastTermSectionIIIVehicleDV:1-body')]")
    private WebElement table_FarmAndShowVehicle;
	
	public void setCustomFarmingLastYearsAmount(int amount) {
		genWO.clickWhenClickable(clickbox_CustomFarmingLastYearsReciepts);
		input_LastyearsAmount.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		input_LastyearsAmount.sendKeys(amount+"");
		genWO.clickProductLogo();
	}
	
	public void setCustomFarmingRenewalExtimatedReceipts(int amount) {
		genWO.clickWhenClickable(clickbox_CustomFarmingRenewalEstimate);
		input_RenewalEstimateAmount.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		input_RenewalEstimateAmount.sendKeys(amount+"");
		genWO.clickProductLogo();
	}
	
	
	
	public void setFarmAndShowcarRenewalOdometer(@Optional("500") String odometerReading) {
		List<WebElement> vehicleList = table_FarmAndShowVehicle.findElements(By.xpath(".//div/table/tbody/child::tr"));
		for(int i= 0;i<=vehicleList.size();i++) {
			genWO.clickWhenClickable(vehicleList.get(i).findElement(By.xpath(".//child::td[6]/div")));
			genWO.find(By.xpath("//input[@name='renewalOdometer']")).sendKeys(Keys.chord(Keys.CONTROL+"a"));
			genWO.find(By.xpath("//input[@name='renewalOdometer']")).sendKeys(odometerReading);
			genWO.clickProductLogo();
		}
	}
	
	
	
	

}
