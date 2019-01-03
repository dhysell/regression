package repository.pc.workorders.generic;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.helpers.TableUtils;

public class GenericWorkorderQualification_GeneralLiability extends GenericWorkorderQualification {

    TableUtils tableUtils;

    public GenericWorkorderQualification_GeneralLiability(WebDriver driver) {
        super(driver);
        tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public void clickGL_HazardousMaterials(boolean radioValue) {
        tableUtils.setRadioByText("hazardous materials", radioValue);
    }


    public void clickGL_RecreationalActivities(boolean radioValue) {
        tableUtils.setRadioByText("ski lifts", radioValue);
    }


    public void clickGL_Explosives(boolean radioValue) {
        tableUtils.setRadioByText("explosives, firearms, fireworks, or ammunition", radioValue);
    }


    public void clickGL_RelatedToIndustry(boolean radioValue) {
        tableUtils.setRadioByText("related to the aircraft", radioValue);
    }


    public void clickGL_BeachesNotIncidental(boolean radioValue) {
        tableUtils.setRadioByText("beaches not incidental", radioValue);
    }


    public void clickGL_ManufactureAnyProduct(boolean radioValue) {
        tableUtils.setRadioByText("manufacture any product", radioValue);
    }


    public void clickGL_OtherBusinessNames(boolean radioValue) {
        tableUtils.setRadioByText("other business names", radioValue);
    }


    public void clickGL_DocksOrFloats(boolean radioValue) {
        tableUtils.setRadioByText("boats, docks, or floats", radioValue);
    }


    public void clickGL_ExposureToChemicals(boolean radioValue) {
        tableUtils.setRadioByText("exposure to chemicals", radioValue);
    }


    public void clickGL_DistributeProductsOutside(boolean radioValue) {
        tableUtils.setRadioByText("distribute products outside", radioValue);
    }


    public void clickGL_SoldOrRepackaged(boolean radioValue) {
        tableUtils.setRadioByText("sold or repackaged", radioValue);
    }


    public void clickGL_RecalledAnyProduct(boolean radioValue) {
        tableUtils.setRadioByText("recalled any product", radioValue);
    }


    public void clickGL_DescontinuedAnyLines(boolean radioValue) {
        tableUtils.setRadioByText("discontinued any lines", radioValue);
    }


    public void clickGL_SeasonalBusiness(boolean radioValue) {
        tableUtils.setRadioByText("business a seasonal business", radioValue);
    }


    public void clickGL_OtherOperationLocations(boolean radioValue) {
        tableUtils.setRadioByText("other operations or locations", radioValue);
    }


    public void clickGL_EquipmentLoanedOrRented(boolean radioValue) {
        tableUtils.setRadioByText("equipment loaned or rented", radioValue);
    }


    public void clickGL_SponsorOrConductEvents(boolean radioValue) {
        tableUtils.setRadioByText("sponsor or conduct", radioValue);
    }


    public void clickGL_SubcontractAnyWork(boolean radioValue) {
        tableUtils.setRadioByText("subcontract any work", radioValue);
    }


    public void clickGL_OperationsOutsideIdaho(boolean radioValue) {
        tableUtils.setRadioByText("Does applicant conduct operations outside the State of Idaho", radioValue);
    }


    public void clickGL_ProductsSoldOtherLabels(boolean radioValue) {
        tableUtils.setRadioByText("products sold under the label of others", radioValue);
    }


    public void clickQualificationNotForProfitOrgCharitableCauses(boolean radioValue) {
        tableUtils.setRadioByText("Is the applicant/insured a not for profit organization whose major purposes is charitable causes?", radioValue);
    }


    public void clickGL_ConductOperationsOutside(boolean radioValue) {
        tableUtils.setRadioByText("conduct operations outside", radioValue);
    }


    public void clickGL_SoldUnderLabel(boolean radioValue) {
        tableUtils.setRadioByText("sold under the label of others", radioValue);
    }


    public void clickGL_RequireSubcontractorLimits(boolean radioValue) {
        clickGL_SubcontractAnyWork(true);
        tableUtils.setRadioByText("require subcontractors to carry", radioValue);
    }


    public void clickGL_CertificatesOfInsurance(boolean radioValue) {
        clickGL_SubcontractAnyWork(true);
        tableUtils.setRadioByText("certificates of insurance", radioValue);
    }


    public void setGL_AnnualCostOfWorkSubcontracted(int subcontractedCost) {
        clickGL_SubcontractAnyWork(true);
        List<WebElement> myQuestion = finds(By.xpath("//div[contains(text(), 'annual cost of work subcontracted')]/parent::td/following-sibling::td/div"));
        if (!myQuestion.isEmpty()) {
            clickWhenClickable(myQuestion.get(0));
            find(By.xpath("//input[contains(@name, 'c2')]")).sendKeys(String.valueOf(subcontractedCost));
        }
    }

    // ADD CHECKBOXES HERE
    @FindBy(xpath = "//div[contains(text(), 'Do you transport any of the following? If yes, check all that apply.')]")
    private WebElement radio_transposrtQuestions;


    public void setExplosivesCheckbox(boolean checked) {
        radio_transposrtQuestions.findElement(By.xpath(".//parent::td/parent::tr/following-sibling::tr[1]/child::td/div/img"));
    }


    public void setLogsCheckbox(boolean checked) {
        radio_transposrtQuestions.findElement(By.xpath(".//parent::td/parent::tr/following-sibling::tr[2]/child::td/div/img"));
    }


    public void setFertilizerCheckbox(boolean checked) {
        radio_transposrtQuestions.findElement(By.xpath(".//parent::td/parent::tr/following-sibling::tr[3]/child::td/div/img"));
    }


    public void setHeavyEquipmentCheckbox(boolean checked) {
        radio_transposrtQuestions.findElement(By.xpath(".//parent::td/parent::tr/following-sibling::tr[4]/child::td/div/img"));
    }


    public void setPetroleumCheckbox(boolean checked) {
        radio_transposrtQuestions.findElement(By.xpath(".//parent::td/parent::tr/following-sibling::tr[5]/child::td/div/img"));
    }


    public void setHazardousOrFlammableCheckbox(boolean checked) {
        radio_transposrtQuestions.findElement(By.xpath(".//parent::td/parent::tr/following-sibling::tr[6]/child::td/div/img"));
    }


    public List<WebElement> getValidationMessages() {
        return super.getValidationMessages();
    }


}















