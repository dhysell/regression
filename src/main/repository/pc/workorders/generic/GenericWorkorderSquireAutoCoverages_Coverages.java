package repository.pc.workorders.generic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.generate.GeneratePolicy;

public class GenericWorkorderSquireAutoCoverages_Coverages extends GenericWorkorderSquireAutoCoverages {

    private WebDriver driver;

    public GenericWorkorderSquireAutoCoverages_Coverages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private void setSelectInFieldsetByText(String tableXpath, String textInTable, String textForSelect) {
        Guidewire8Select tableSelect = new Guidewire8Select(driver, tableXpath + "//legend//*[contains(text(),'" + textInTable + "')]/ancestor::fieldset//table[contains(@id, '-triggerWrap')]");
        tableSelect.selectByVisibleTextPartial(textForSelect);
    }

    private void setCheckboxInFieldsetByText(String tableXpath, String textInTable, boolean value) {
        Guidewire8Checkbox tableCheckbox = new Guidewire8Checkbox(driver, tableXpath + "//legend//*[contains(text(),'" + textInTable + "')]/ancestor::div/table[contains(@class, 'checkbox')]");
        tableCheckbox.select(value);
    }


    public void fillOutCoveragesTab(GeneratePolicy policy) {
        clickCoverageTab();
        setLiabilityCoverage(policy.squire.squirePA.getCoverages().getLiability());
        setMedicalCoverage(policy.squire.squirePA.getCoverages().getMedical());
        setUninsuredCoverage(policy.squire.squirePA.getCoverages().hasUninsured(), policy.squire.squirePA.getCoverages().getUninsuredLimit());
        setUnderinsuredCoverage(policy.squire.squirePA.getCoverages().hasUnderinsured(), policy.squire.squirePA.getCoverages().getUnderinsuredLimit());
    }

    private Guidewire8Select select_LiabilityLimit() {
        return new Guidewire8Select(driver, "//fieldset//legend[.='Liability']/following-sibling::div//table[contains(@id, ':LiabilityTermInput-triggerWrap')]");
    }

    private Guidewire8Select select_MedicalLimit() {
        return new Guidewire8Select(driver, "//fieldset//legend[.='Medical']/following-sibling::div//table[contains(@id, ':OptionTermInput-triggerWrap')]");
    }

    private Guidewire8Select select_UnInsuredMotoristCoverage() {
        return new Guidewire8Select(driver, "//fieldset//legend[.='Uninsured Motorist - Bodily Injury']/following-sibling::div//table[contains(@id, ':CoverageInputSet:CovPatternInputGroup:PackageTermInput-triggerWrap')]");
    }

    private Guidewire8Select select_UnderinsuredMotoristBodilyInjury() {
        return new Guidewire8Select(driver, "//fieldset//legend[.='Underinsured Motorist - Bodily Injury']/following-sibling::div//table[contains(@id, ':CoverageInputSet:CovPatternInputGroup:PackageTermInput-triggerWrap')]");
    }

    private String lineCoveragesTableXpath = "//div[contains(@id, 'PAWizardStepGroup:PersonalAutoScreen:PersonalAutoCoveragesPanelSet:PersonalAuto_AllVehicleCoveragesDV_ref')]/table";


    public void setLiabilityCoverage(LiabilityLimit liabilityLimit) {
        setSelectInFieldsetByText(lineCoveragesTableXpath, "Liability", liabilityLimit.getValue());
    }


    public void setMedicalCoverage(MedicalLimit medicalLimit) {
        setSelectInFieldsetByText(lineCoveragesTableXpath, "Medical", medicalLimit.getValue());
    }

    public void setUninsuredCoverage(boolean hasCoverage, UninsuredLimit unInsuredLimit) {
        if (unInsuredLimit != null) {
            checkAndSelectCoverage(lineCoveragesTableXpath, "Uninsured", hasCoverage, unInsuredLimit.getValue());
        } else {
            setCheckboxInFieldsetByText(lineCoveragesTableXpath, "Uninsured", hasCoverage);
        }
    }


    public void setUnderinsuredCoverage(boolean hasCoverage, UnderinsuredLimit underInsuredLimit) {
        if (underInsuredLimit != null) {
            checkAndSelectCoverage(lineCoveragesTableXpath, "Underinsured", hasCoverage, underInsuredLimit.getValue());
        } else {
            setCheckboxInFieldsetByText(lineCoveragesTableXpath, "Underinsured", hasCoverage);
        }
    }

    private void checkAndSelectCoverage(String tableXpath, String coverage, boolean hasCoverage, String coverageAmount) {
        setCheckboxInFieldsetByText(tableXpath, coverage, hasCoverage);
        if (hasCoverage) {
            setSelectInFieldsetByText(tableXpath, coverage, coverageAmount);
        }
    }

    public boolean verifyUMCoveragesList(String item) {
        Guidewire8Select UMCoverages = select_UnInsuredMotoristCoverage();
        return UMCoverages.isItemInList(item);
    }


    public String getLiabilityLimit() {
        return select_LiabilityLimit().getText();
    }


    public String getMedicalLimit() {
        return select_MedicalLimit().getText();
    }


    public String getUninsuredMotoristBodilyInjury() {
        return select_UnInsuredMotoristCoverage().getText();
    }

    public String getUnderInsuredMotoristBodilyInjury() {
        return select_UnderinsuredMotoristBodilyInjury().getText();
    }

}












































