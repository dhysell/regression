package repository.pc.workorders.renewal;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

public class RenewalInformation extends BasePage {

	private TableUtils tableUtils;
	
    public RenewalInformation(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//div[contains(@id, 'RenewalWizard:RenewalWizard_PastTermInfoScreen:RenewalPastTermSectionIIIVehicleDV:1')]")
    public WebElement table_RenewalInformationSectionIIIVehicle;

    @FindBy(xpath = "//div[contains(@id, 'RenewalWizard:RenewalWizard_PastTermInfoScreen:RenewalPastTermSectionIILiabilityDV_ref')]")
    public WebElement table_RenewalInformationSectionIILiability;


    public int getRenewalInformationSectionIIIVehicleRowCount() {
        return tableUtils.getRowCount(table_RenewalInformationSectionIIIVehicle);
    }


    public void setRenewalOdometerByVehicleNumber(int vehicleNumber, String odometer) {
        int vehicleRow = tableUtils.getRowNumberInTableByText(table_RenewalInformationSectionIIIVehicle, "Vehicle " + vehicleNumber);
        tableUtils.clickCellInTableByRowAndColumnName(table_RenewalInformationSectionIIIVehicle, vehicleRow, "Renewal Odometer");
        WebElement text_occuranceDate = table_RenewalInformationSectionIIIVehicle.findElement(By.xpath(".//input[contains(@name,'renewalOdometer')]"));
        text_occuranceDate.sendKeys(odometer);
    }


    public String getRenewalSectionIIIDetailsBySpecificVehicle(String vehicle, String column) {
        int row = tableUtils.getRowNumberInTableByText(table_RenewalInformationSectionIIIVehicle, vehicle);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_RenewalInformationSectionIIIVehicle, row, column);
    }


    public void setCustomFarming(int coverageLimit) {
        clickWhenClickable(table_RenewalInformationSectionIILiability.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex, '0')]/td[2]//div")));
        WebElement text_customFarming = table_RenewalInformationSectionIILiability.findElement(By.xpath(".//input[contains(@name,'LastYearsAmount')]"));
        text_customFarming.sendKeys(String.valueOf(coverageLimit));
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
    }
}
