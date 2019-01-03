package repository.pc.workorders.generic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.idfbins.enums.YesOrNo;

import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderSquireEligibility extends GenericWorkorder {

    TableUtils tableUtils;
    private WebDriver driver;

    public GenericWorkorderSquireEligibility(WebDriver driver) {
        super(driver);
        this.driver = driver;
        tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    String questionText;

//    private String radioElement(String keyword, boolean radioValue) {
//        if (radioValue) {
//            return "//div[contains(text(), '" + keyword
//                    + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "Yes"
//                    + "']/preceding-sibling::input";
//        } else {
//            return "//div[contains(text(), '" + keyword
//                    + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "No"
//                    + "']/preceding-sibling::input";
//        }
//    }
//
//    private String checkElement(String keyword, boolean radioValue) {
//        if (radioValue) {
//            return "//div[contains(text(), '" + keyword
//                    + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "Yes"
//                    + "']/parent::td[contains(@class, 'cb-checked')]";
//        } else {
//            return "//div[contains(text(), '" + keyword
//                    + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "No"
//                    + "']/parent::td[contains(@class, 'cb-checked')]";
//        }
//    }


    /**
     * @param questionText
     * @return boolean with whether expected radio button is marked as checked.
     * @author iclouser
     * Primary Use for Validating Qualifying Questions are selected.
     * If no value is selected will return false
     * Only returns true if the radio is selected matches the passed in expected radio to be selected.
     */

    public boolean whichRadioIsSelected(String questionText, YesOrNo yOrNo) {

        String xpath = "//div[contains(text(),'" + questionText + "')]/parent::td/following-sibling::td/descendant::td[contains(@class, 'cb-checked')]/label";
        // Checks whether any value is selected.
        boolean isAnyValueChecked = finds(By.xpath(xpath)).size() > 0;
        // The value that is checked
        String valueChecked = "";
        if (isAnyValueChecked) {
            valueChecked = find(By.xpath(xpath)).getText();
        }
        return (isAnyValueChecked && (valueChecked.equalsIgnoreCase(yOrNo.getValue())));
    }


    public void clickFarmRevenue(boolean radioValue) {
        questionText = "Does the applicant have any farm revenue?";
        tableUtils.setRadioByText(questionText, radioValue);
    }

    public void clickGreaterThan40k(boolean radioValue) {
        questionText = "Is the total annual revenue greater than $40,000?";
        tableUtils.setRadioByText(questionText, radioValue);
    }


    public void clickGreaterThan20Percent(boolean radioValue) {
        questionText = "Is the custom farming annual revenue greater than 20% of your total annual revenue?";
        tableUtils.setRadioByText(questionText, radioValue);
    }


    public void setStandardFirewithCommodity(boolean radioValue) {
//        questionText = "Do you want to start a Standard Fire with commodities only?";
//        tableUtils.setRadioByText(questionText, radioValue);

        By cssSelector = radioValue ? By.cssSelector("input[id$='isCommoditiesOnly_true-inputEl']") : By.cssSelector("input[id$='isCommoditiesOnly_false-inputEl']");
        clickWhenClickable(cssSelector);
    }

    public void chooseCity() {
        clickFarmRevenue(false);
    }

    public void chooseCountry(boolean ineligibleCustomFarmingCoverage) {
        clickFarmRevenue(true);
        clickGreaterThan40k(false);
        if (ineligibleCustomFarmingCoverage) {
            clickGreaterThan20Percent(true);
        } else {
            clickGreaterThan20Percent(false);
        }

    }


    public void chooseFarm() {
        clickFarmRevenue(true);
        clickGreaterThan40k(true);
        clickGreaterThan20Percent(false);
    }


    public void chooseEligibility(SquireEligibility squireEligibility) {
        switch (squireEligibility) {
            case City:
                chooseCity();
                break;
            case Country:
                chooseCountry(false);
                break;
            case CountryIneligibleCustomFarmingCoverage:
                chooseCountry(true);
                break;
            case FarmAndRanch:
                chooseFarm();
                break;
            case CustomAuto:
                break;
        }
    }

    private Guidewire8Select select_PolicySelection() {
        return new Guidewire8Select(driver, "//table[@id='SubmissionWizard:LineSelectionScreen:policySelectionDropDown-triggerWrap']");
    }


    public void setPolicySelection(boolean standardFire) {
        Guidewire8Select mySelect = select_PolicySelection();
        if (standardFire) {
            mySelect.selectByVisibleText("Standard Fire");
        } else {
            mySelect.selectByVisibleText("Standard Liability");
        }
    }


    public void setStandardIM(boolean standardIM) {
        Guidewire8Select mySelect = select_PolicySelection();
        if (standardIM) {
            mySelect.selectByVisibleText("Standard Inland Marine");
        } else {
            mySelect.selectByVisibleText("4-H Livestock");
        }

    }

//    private String policySelectionRadioElement(String keyword, boolean radioValue) {
//        if (radioValue) {
//            return "//*[contains(text(), '" + keyword
//                    + "')]/parent::td/following-sibling::td/descendant::td[contains(.,'" + "Yes"
//                    + "')]//input";
//        } else {
//            return "//*[contains(text(), '" + keyword
//                    + "')]/parent::td/following-sibling::td/descendant::td[contains(.,'" + "No"
//                    + "')]//input";
//        }
//    }
//
//    private String PolicySelectioncheckElement(String keyword, boolean radioValue) {
//        if (radioValue) {
//            return "//*[contains(text(), '" + keyword
//                    + "')]/parent::td/following-sibling::td/descendant::td[contains(.,'" + "Yes"
//                    + "')]//parent::*[contains(@class, 'cb-checked')]";
//        } else {
//            return "//*[contains(text(), '" + keyword
//                    + "')]/parent::td/following-sibling::td/descendant::td[contains(.,'" + "No"
//                    + "')]//parent::*[contains(@class, 'cb-checked')]";
//        }
//    }


    public void setSquireEligibility(GeneratePolicy policy) {

        if (policy.productType.equals(ProductLineType.StandardFire)) {
            setPolicySelection(true);
            if (!policy.standardFire.hasStdFireCommodity() && (policy.lineSelection.contains(LineSelection.PropertyAndLiabilityLinePL) || policy.lineSelection.contains(LineSelection.PersonalAutoLinePL))) {
                setStandardFirewithCommodity(false);
            }
        } else if (policy.productType.equals(ProductLineType.StandardLiability)) {
            setPolicySelection(false);
        }

//        if (policy.lineSelection.contains(LineSelection.StandardFirePL)) {
//            setPolicySelection(true);
//            if (policy.standardFire.hasStdFireCommodity()) {
//                
//                setStandardFirewithCommodity(false);
//            }
//        } else if (policy.lineSelection.contains(LineSelection.StandardLiabilityPL)) {
//            setPolicySelection(false);
//        }
//        if (policy.standardFireAndLiability.hasStdFireCommodity()) {
//            
//            setStandardFirewithCommodity(false);
//        }
    }


    public void clickAutoOnly(boolean radioValue) {
        questionText = "Is this an auto only policy?";
        tableUtils.setRadioByText(questionText, radioValue);
    }

//    public boolean checkAutoOnly() {
//        questionText = "Is this an auto only policy?";
//        return checkIfElementExists("//div[contains(text(), '" + questionText + "')]", 20);
//    }
}








































