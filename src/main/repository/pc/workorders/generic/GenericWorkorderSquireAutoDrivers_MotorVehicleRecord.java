package repository.pc.workorders.generic;

import java.text.ParseException;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.idfbins.enums.State;

import repository.gw.enums.DateDifferenceOptions;
import repository.gw.enums.SRPIncident;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.custom.Contact;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.VeriskMvr;

public class GenericWorkorderSquireAutoDrivers_MotorVehicleRecord extends GenericWorkorderSquireAutoDrivers {

    private TableUtils tableUtils;
    private WebDriver driver;

    public GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);

    }


    public void fillOutMotorVehicleRecord_QQ(Contact driver) {

    }

    public void fillOutMotorVehicleRecord_FA(Contact driver) throws GuidewireNavigationException {
    	if(driver.isOrderMVR()) {
    		retrieveMVR(driver.getFirstName(), driver.getDriversLicenseNum(), State.Idaho);
    	}

        if (driver.isSpecial || driver.isVeriskTestData) {
            clickMotorVehicleRecord();
            for(VeriskMvr incident : driver.getVeriskMVRReport()) {
            	if(incident.getAssignedSRPIncident() != null || incident.getAssignedSRPIncident() != "") {
            		selectMVRIncidents(SRPIncident.valueOf(incident.getAssignedSRPIncident()));
            	} else {
            		selectMVRIncidents(SRPIncident.Waived);
            	}
            }
        }
    }

    public void fillOutMotorVehicleRecord(Contact driver) {

    }


    @FindBy(xpath = "//*[contains(@id, 'MVRDetailCardTab-btnEl') or contains(@id, ':RetrieveMVRButton-btnEl')]")
    private WebElement button_PADriversMVR;

    private void retrieveMVR(String name, String license, State state) {
        clickOrderMVR();
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        hoverOver(button_PADriversMVR);
        clickWhenClickable(button_PADriversMVR);
    }


    @FindBy(xpath = "//div[contains(@id,'PADriverPopup:DriverDetailsCV:MVRIncidentCard:MVRIncidentLV') or contains(@id,'PADriver_FBMPopup:DriverDetailsCV:MVRIncidentCard:MVRIncidentLV')]")
    private WebElement table_MVRIncident;

    public void selectMVRIncidents(SRPIncident incident) {
		try {			
			if(!checkMVRNotFoundMessage()){
            int tableRowCount = tableUtils.getRowCount(table_MVRIncident);

			for (int i = 1; i <= tableRowCount; i++) {
                Date todaysPCDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
                Date claimDate = DateUtils.convertStringtoDate(tableUtils.getCellTextInTableByRowAndColumnName(table_MVRIncident, i, "Vio/Susp Date"), "MM/dd/yyyy");

				int numMonthsDiff = DateUtils.getDifferenceBetweenDates(claimDate, todaysPCDate, DateDifferenceOptions.Month);

				boolean ageCheck = numMonthsDiff < 35;

				if (ageCheck) {
					selectMVRIncident(i, incident);
					}
				}
			}
			
        } catch (ParseException e) {
            Assert.fail("Could not successfully parse the claimDateString.");
        }
    }

    public void selectMVRIncident(int tableRowNumber, SRPIncident incident) {
        tableUtils.selectValueForSelectInTable(table_MVRIncident, tableRowNumber, "Assign SRP Incident", incident.getValue());

        if (incident.equals(SRPIncident.Waived)) {
            tableUtils.setValueForCellInsideTable(table_MVRIncident, tableRowNumber, "Reason For Waiving", "reasonForWaive", "Default Reason");
        }
    }

	@FindBy(xpath = "//a[contains(@id, ':refreshMVRButton')]")
	private WebElement button_PARefreshMVR;
	public void clickRefreshMVR() {
		clickWhenClickable(button_PARefreshMVR);
	}

	@FindBy(xpath = "//div[contains(@id, ':PersonalMotorVehicleRecordsDV') and  contains(., 'MVR received with No-Hit status') or contains (., 'No MVR records on file')]")
	private WebElement lable_PAMVRNotFound;
	
	public boolean checkMVRNotFoundMessage() {
		return checkIfElementExists(lable_PAMVRNotFound, 100);
	}
	
	@FindBy(xpath = "//div[contains(@id, ':RequestedDate-inputEl')]")
	private WebElement text_PAMVRRequestedDate;
	
	public String MVRRequestedDate() throws ParseException{
		return text_PAMVRRequestedDate.getText();
	}


}
