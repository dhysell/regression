package repository.gw.errorhandling;

import com.idfbins.enums.CountyIdaho;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

import java.util.Date;
import java.util.List;

public class ErrorHandlingHelpers extends BasePage {

    protected List<WebElement> errorMessage = null;
    protected List<WebElement> errorMessageValidationResults = null;
    int i = 0;
    int j = 0;

    private WebDriver driver;

    public ErrorHandlingHelpers(WebDriver webDriver) {
        super(webDriver);
        this.driver = webDriver;
    }

    /////////////////////////////////////////////////
    /// Generic Helper Methods for Use on Each Page///
    /////////////////////////////////////////////////

    public boolean errorExists() {
        return finds(By.xpath("//div[contains(@id, '_msgs')]/div[1] | //span[contains(@id, '_msgs_msgs')]/div[1]")).size() > 0;
    }

    public String getErrorMessage() {
    	waitForPostBack();
        List<WebElement> errormessages = finds(By.xpath("//div[contains(@id, '_msgs')]/div[1] | //span[contains(@id, '_msgs_msgs')]/div[1]"));
        if (errormessages.isEmpty()) {
            return "";
        } else {
            return errormessages.get(0).getText();
        }

    }

    public String getSpecificErrorMessage(int errMsgNum) {
        List<WebElement> errormessages = finds(By.xpath("//div[contains(@id, '_msgs')]/div | //span[contains(@id, '_msgs_msgs')]/div"));
        if (errormessages.isEmpty()) {
            return "";
        } else {
            return errormessages.get(errMsgNum).getText();
        }

    }

    public boolean areThereErrorMessages() {
        return checkIfElementExists("//div[contains(@id, '_msgs')]/div[1] | //span[contains(@id, '_msgs_msgs')]/div[1] | //div[contains(@id, 'message')] | //div[contains(@class, 'message')]", 500);
    }

    public boolean errorExistsValidationResults() {
        return finds(By.xpath("//div[contains(@id, 'grpMsgs')] | //span[contains(@id, 'grpMsgs_msgs')]/div[1]")).size() > 0;
    }

    public String getValidationResultsErrorText() {
        ErrorHandling error = new ErrorHandling(driver);
        return error.text_ErrorHandlingValidationResults().get(0).getText();
    }

    public Boolean isValidationResults() {
        ErrorHandling error = new ErrorHandling(driver);
        return checkIfElementExists(error.text_ValidationResults(), 1000);
    }

    public void saveDraft(int timeToWait) {
        GenericWorkorder workorder = new GenericWorkorder(driver);
        workorder.clickGenericWorkorderSaveDraft();
    }

    protected void checkForRemainingErrors(String page) {
        ErrorHandling errorBannerMessagesList = new ErrorHandling(driver);
        if (errorExists() || errorExistsValidationResults()) {
            if (errorExists()) {
                errorBannerMessagesList = new ErrorHandling(driver);
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                String errorString = errorMessage.get(0).getText();
                Assert.fail(getCurrentUrl() + "There was an unhandled error on the " + page + " Page. The error was: " + errorString);
            } else {
                errorBannerMessagesList = new ErrorHandling(driver);
                this.errorMessageValidationResults = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                String errorString = errorMessageValidationResults.get(0).getText();
                Assert.fail(getCurrentUrl() + "There was an unhandled error on the " + page + " Page. The error was: " + errorString);
            }
        }
    }

    private void reQuote() {
        ErrorHandling errorQuote = new ErrorHandling(driver);
        errorQuote.button_Clear().click();
        GenericWorkorderRiskAnalysis riskQuote = new GenericWorkorderRiskAnalysis(driver);
        riskQuote.Quote();
    }

    public Boolean errorHandlingRiskAnalysis() throws Exception {

        long stopTime = new Date().getTime() + (120 * 1000);
        int count = 0;

        do {
            if (finds(By.xpath("//span[contains(text(), 'Validation Results')]/ancestor::tr[1]/following-sibling::tr/child::td/child::div/child::div[contains(text(), 'An invalid quote was generated')]")).size() > 0) {
                reQuote();
            } else if (finds(By.xpath("//span[contains(text(), 'Validation Results')]/ancestor::tr[1]/following-sibling::tr/child::td/child::div/child::div[contains(text(), 'The date that the Quote was needed')]")).size() > 0) {
                System.out.println("The System Clock Was Moved During Generate");
                SideMenuPC sideMenu = new SideMenuPC(driver);
                sideMenu.clickSideMenuPolicyInfo();

                GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
                policyInfo.setPolicyInfoEffectiveDate(DateUtils.getCenterDateAsString(driver,ApplicationOrCenter.PolicyCenter, "MMddyyyy"));
                reQuote();
            } else if (finds(By.xpath("//div[contains(@id, ':Quote_SummaryDV:TotalPremium-inputEl') or contains(@id, 'Quote_SummaryDV:PolicyPremiumInputSet:TotalGrossPremium-inputEl')  or contains(@id, 'Quote_SummaryDV:PolicyPremiumInputSet:TotalGrossPremium2-inputEl')]")).size() > 0 || !finds(By.xpath("//span/span[contains(text(), 'Quoted')]")).isEmpty()) {///hope this quoted doesn't break a lot of  things jlarsen
                return false;
            } else if (finds(By.xpath("div[contains(text(), 'Policy Info - The billing county needs to be set')]")).size() > 0) {
                SideMenuPC sideMenu = new SideMenuPC(driver);
                sideMenu.clickSideMenuPolicyInfo();
                GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
                policyInfo.setPolicyInfoBillingCounty(CountyIdaho.Bannock.getValue());
                reQuote();
            } else if (areThereErrorMessages()) {
                if (getErrorMessage().contains("Could not find this Policy in Billing System")) {
                	do {
                		sleep(5); //Used to wait and re-check policy every 5 seconds.
	                    SideMenuPC sideMenuStuff = new SideMenuPC(driver);
	                    sideMenuStuff.clickSideMenuQuote();
                	} while ((areThereErrorMessages() && (getErrorMessage().contains("Could not find this Policy in Billing System"))) || (new Date().getTime() < stopTime));
                }
            }
            count++;
            System.out.println(count + " try and runTime of: " + String.valueOf((new Date().getTime() - (stopTime - (120 * 1000))) / (60 * 1000)));
        } while (new Date().getTime() < stopTime && count <= 5);
        return true;
    }

    protected void resetClassFields() {
        this.errorMessage = null;
        this.errorMessageValidationResults = null;
        this.i = 0;
    }


    /////////////////////////////////////////////
    /// Switch Cases for the page methods above///
    /////////////////////////////////////////////
    public List<WebElement> errorHandlingValidationResultsSwitchCase(int timeToWait, PolicyLocationBuilding building) {
        GenericWorkorderBuildings buildingsPage = new GenericWorkorderBuildings(driver);
        String errorString = this.errorMessageValidationResults.get(this.i).getText();
        switch (errorString) {
            case "Must Select at Least 1 Parking Lot and Sidewalk Characteristics.":
                buildingsPage.setParkingLotSidewalkCharacteristics(building.getParkingLotCharacteristicsList());
                break;
            case "Must Select at Least 1 Safety Equipment.":
                buildingsPage.setSafetyEquipment(building.getSafetyEquipmentList());
                break;
            default:
                saveDraft(10);
                checkForRemainingErrors("Validation Results");
                break;
        }
        return errorMessageValidationResults;
    }


}
