package repository.gw.errorhandling;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.custom.PolicyLocation;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderLocations;

import java.util.List;

public class ErrorHandlingLocations extends ErrorHandlingHelpers {

    private WebDriver driver;

    public ErrorHandlingLocations(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    int i = 0;
    int j = 0;


    public void errorHandlingLocationsPage(int timeToWait, PolicyLocation location) throws GuidewireException {

        saveDraft(timeToWait);
        if (errorExists()) {
            GenericWorkorderLocations locationDetailsPage = new GenericWorkorderLocations(driver);
            ErrorHandling errorBannerMessagesList = new ErrorHandling(driver);
            this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
            for (this.i = 0; i < errorMessage.size(); i++) {
                errorMessage = errorHandlingLocationsPageSwitchCase(timeToWait, location);
            }
            locationDetailsPage.clickLocationsOk();
        }
        checkForRemainingErrors("Locations");
        resetClassFields();
    }

    public List<WebElement> errorHandlingLocationsPageSwitchCase(int timeToWait, PolicyLocation location)
            throws GuidewireException {
        GenericWorkorderLocations locationDetailsPage = new GenericWorkorderLocations(driver);
        ErrorHandling errorBannerMessagesList = new ErrorHandling(driver);
        String errorString = this.errorMessage.get(this.i).getText();

        switch (errorString) {
            case "On current page:":
                GenericWorkorder workorder = new GenericWorkorder(driver);
                workorder.clickGenericWorkorderSaveDraft();
                errorBannerMessagesList = new ErrorHandling(driver);
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                int tabErrorCount = 0;
                for (this.j = 0; j == errorMessage.size(); j++) {
                    String tabErrorString = errorMessage.get(j).getText();
                    if ((tabErrorString.equalsIgnoreCase("On \"Location Details\":"))
                            || (tabErrorString.equalsIgnoreCase("On current page:"))) {
                        tabErrorCount++;
                    }
                }
                if (tabErrorCount == 0) {
                    i = -1;
                }
                break;
            case "On \"Location Details\":":
            case "On \"Details\":":
                ErrorHandling errorLocationDetails = new ErrorHandling(driver);
                clickWhenClickable(errorLocationDetails.link_ErrorHandlingOnLocationDetails());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                i = -1;
                break;
            case "Manual Public Protection Class Code : Missing required field \"Manual Public Protection Class Code\"":
                locationDetailsPage.setLocationsManualProtectionClassCode(location.getManualProtectionClassCode());
                break;
            case "Is there a Playground at this Location? : Missing required field \"Is there a Playground at this Location?\"":
                locationDetailsPage.setLocationsPlayground(location.isPlaygroundYes());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Is there a Pool at this Location? : Missing required field \"Is there a Pool at this Location?\"":
                locationDetailsPage.setLocationsSwimmingPool(location.isPoolYes());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Is the operation seasonal-closed for more than 30 consecutive days? : Missing required field \"Is the operation seasonal-closed for more than 30 consecutive days?\"":
                locationDetailsPage.setLocationsSeasonalOperation(location.isSeasonalYes());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Annual Gross Receipts at this Location? : Missing required field \"Annual Gross Receipts at this Location?\"":
            case "Annual Gross Receipts at this Location? : must be a numeric value.":
                locationDetailsPage.setLocationsAnnualGrossReceipts(location.getAnnualGrossReceipts());
                break;
            default:
                saveDraft(10);
                checkForRemainingErrors("Locations");
                break;
        }
        return errorMessage;
    }
}
