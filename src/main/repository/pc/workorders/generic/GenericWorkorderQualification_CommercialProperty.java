package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import repository.gw.helpers.TableUtils;

public class GenericWorkorderQualification_CommercialProperty extends GenericWorkorderQualification {

    TableUtils tableUtils;

    public GenericWorkorderQualification_CommercialProperty(WebDriver driver) {
        super(driver);
        tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public void clickCPP_PriorInsurance(boolean radioValue) {
        tableUtils.setRadioByText("Does the applicant/insured have prior insurance", radioValue);
    }


    public void clickCPP_LossesLast3Years(boolean radioValue) {
        tableUtils.setRadioByText("Has applicant had any losses in the last 3 years", radioValue);
    }


    public void clickCPHasOtherInsurance_SharedRisk(boolean radioValue) {
        tableUtils.setRadioByText("Will the applicant/insured have other insurance on the" +
                " same property subject to this policy i.e. sharing the risk with another carrier", radioValue);
    }


}















