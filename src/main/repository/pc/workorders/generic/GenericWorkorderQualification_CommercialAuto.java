package repository.pc.workorders.generic;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import repository.gw.helpers.TableUtils;

public class GenericWorkorderQualification_CommercialAuto extends GenericWorkorderQualification {

    TableUtils tableUtils;

    public GenericWorkorderQualification_CommercialAuto(WebDriver driver) {
        super(driver);
        tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public void clickCA_AutoWithOutDrivers(boolean radioValue) {
        tableUtils.setRadioByText("without drivers", radioValue);
    }


    public void clickCA_InterchangeAgreement(boolean radioValue) {
        tableUtils.setRadioByText("interchange agreements", radioValue);
    }


    public void clickCA_IndustrialUseVehicles(boolean radioValue) {
        tableUtils.setRadioByText("hauling of chemicals", radioValue);
    }


    public void clickCA_ServiceCommision(boolean radioValue) {
        tableUtils.setRadioByText("commission", radioValue);
    }


    public void clickCA_InterstateCommerce(boolean radioValue) {
        tableUtils.setRadioByText("Interstate", radioValue);
    }


    public void clickCA_HazardousMaterialWarning(boolean radioValue) {
        tableUtils.setRadioByText("hazardous material warnings", radioValue);
    }


    public void clickCA_TransportTheFollowing(boolean radioValue) {
        tableUtils.setRadioByText("transport any of the following", radioValue);
    }


    public void clickCA_TransportMilk(boolean radioValue) {
        tableUtils.setRadioByText("transport milk", radioValue);
    }


    public void clickCA_DUI(boolean radioValue) {
        tableUtils.setRadioByText("DUI", radioValue);
    }


    public void clickCA_YouthfulDrivers(boolean radioValue) {
        tableUtils.setRadioByText("youthful drivers", radioValue);
    }


    public void clickCA_FoodDelivery(boolean radioValue) {
        tableUtils.setRadioByText("Does the applicant provide any food delivery such as pizza", radioValue);
    }


    public void clickCA_PilotCarService(boolean radioValue) {
        tableUtils.setRadioByText("Does the applicant provide any pilot car services", radioValue);
    }


    public void clickCA_LeasedAutosWithOthers(boolean radioValue) {
        tableUtils.setRadioByText("lease autos to others with drivers", radioValue);
    }


    public void clickCA_LicenseRevoced(boolean radioValue) {
        tableUtils.setRadioByText("revoked or suspended ", radioValue);
    }


    public void clickCA_TrafficCitation(boolean radioValue) {
        tableUtils.setRadioByText("traffic citation", radioValue);
    }


    public void clickCA_RetailBasis(boolean radioValue) {
        tableUtils.setRadioByText("retail basis", radioValue);
        if (radioValue) {
            //set text box
            clickWhenClickable(find(By.xpath("//div[contains(text(), 'Please describe products or goods sold.')]/../following-sibling::td/div")));
            find(By.xpath("//textarea[contains(@name, 'c2')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
            find(By.xpath("//textarea[contains(@name, 'c2')]")).sendKeys("Stuff and Things");
        }
    }


    public void clickCA_TimeDelivery(boolean radioValue) {
        tableUtils.setRadioByText("time sensitive delivery", radioValue);
    }


    public void clickCA_TransportForOthers(boolean radioValue) {
        tableUtils.setRadioByText("transport products of others", radioValue);
    }


    public void check_OtherStates(boolean checked) {
        if (checked && finds(By.xpath("//div[contains(text(), 'Other States')]/../following-sibling::td/div[contains(@class, 'checked')]")).isEmpty()) {
            find(By.xpath("//div[contains(text(), 'Other States')]/../following-sibling::td/div/img")).click();
        } else if (!checked && !finds(By.xpath("//div[contains(text(), 'Other States')]/../following-sibling::td/div[contains(@class, 'checked')]")).isEmpty()) {
            find(By.xpath("//div[contains(text(), 'Other States')]/../following-sibling::td/div/img")).click();
        }
    }


    public void clickQualificationNext() {
        super.clickPolicyChangeNext();
    }


    public List<WebElement> getValidationMessages() {
        return super.getValidationMessages();
    }


}

















