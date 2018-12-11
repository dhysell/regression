package repository.pc.workorders.generic;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.InlandMarineCPP.SignsCoverageForm_CM_00_28_Coinsurance;
import repository.gw.enums.InlandMarineCPP.SignsCoverageForm_CM_00_28_Deductible;
import repository.gw.enums.InlandMarineCPP.SignsCoverageForm_CM_00_28_SignType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPCommercialPropertyProperty;
import repository.gw.generate.custom.CPPInlandMarineSignsCM;
import repository.gw.generate.custom.CPPInlandMarineSignsCM_ScheduledSign;

public class GenericWorkorderInlandMarineSignsCPP extends BasePage {
    private WebDriver driver;

    public GenericWorkorderInlandMarineSignsCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    ///////////////////////
    //       Tabs        //
    ///////////////////////

    @FindBy(css = "span[id$=':IMPartScreen:IMSignsCovDetailCV:CoveragesCovTab-btnEl']")
    private WebElement link_CoveragesTab;

    private void clickCoveragesTab() {
        clickWhenClickable(link_CoveragesTab);
        
    }

    @FindBy(css = "span[id$=':IMPartScreen:IMSignsCovDetailCV:ExclusionsConditionsTab-btnEl']")
    private WebElement link_ExclusionsAndConditionsTab;

    private void clickExclusionsAndConditionsTab() {
        clickWhenClickable(link_ExclusionsAndConditionsTab);
        
    }

    ///////////////////////
    //     Coverages     //
    ///////////////////////

    private Guidewire8Select select_Deductible() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Deductible')]/parent::td/following-sibling::td/table");
    }

    private void setDeductible(SignsCoverageForm_CM_00_28_Deductible deductible) {
        Guidewire8Select mySelect = select_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
        
    }

    private Guidewire8Select select_Coinsurance() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Coinsurance')]/parent::td/following-sibling::td/table");
    }

    private void setCoinsurance(SignsCoverageForm_CM_00_28_Coinsurance coinsurance) {
        Guidewire8Select mySelect = select_Coinsurance();
        mySelect.selectByVisibleText(coinsurance.getValue());
        
    }

    //SCHEDULED SIGNS

    @FindBy(css = "span[id$=':IMSignsCovDetailCV:SignsPanelSet:DwellingListDetailPanel:SignsLV_tb:Add-btnEl']")
    private WebElement buttonAddScheduledSign;

    private void clickAddScheduledSign() {
        clickWhenClickable(buttonAddScheduledSign);
        
    }

    private Guidewire8Select select_Location() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Location')]/parent::td/following-sibling::td/table");
    }

    private void setLocation(CPPCommercialPropertyProperty property) {
        Guidewire8Select mySelect = select_Location();
        mySelect.selectByVisibleTextPartial(property.getAddress().getLine1() + ", " + property.getAddress().getCity() + ", " + property.getAddress().getState().getAbbreviation() + " " + property.getAddress().getZip());
        
    }

    private Guidewire8Select select_Property() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Property')]/parent::td/following-sibling::td/table");
    }

    private void setProperty(CPPCommercialPropertyProperty property) {
        Guidewire8Select mySelect = select_Property();
        mySelect.selectByVisibleTextPartial("Property " + property.getPropertyLocationNumber() + ":");
        
    }

    @FindBy(css = "textarea[id$=':SignsPanelSet:DwellingListDetailPanel:signLettering-inputEl']")
    private WebElement textarea_SignLettering;

    private void setSignLettering(String value) {
        textarea_SignLettering.click();
        textarea_SignLettering.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    private Guidewire8Select select_SignType() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Sign Type')]/parent::td/following-sibling::td/table");
    }

    private void setSignType(SignsCoverageForm_CM_00_28_SignType signType) {
        Guidewire8Select mySelect = select_SignType();
        mySelect.selectByVisibleText(signType.getValue());
        
    }

    private Guidewire8Select select_InteriorSign() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Is this an interior sign?')]/parent::td/following-sibling::td/table");
    }

    private Guidewire8Select select_AttachedToBuilding() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Is the sign attached to the building?')]/parent::td/following-sibling::td/table");
    }

    private void setBooleanSelect(Guidewire8Select selectElement, boolean choice) {
        if (choice) {
            selectElement.selectByVisibleText("Yes");
        } else {
            selectElement.selectByVisibleText("No");
        }
        
    }

    @FindBy(css = "input[id$=':IMSignsCovDetailCV:SignsPanelSet:DwellingListDetailPanel:signLimit-inputEl']")
    private WebElement input_ScheduledSignLimit;

    private void setSignLimit(String value) {
        input_ScheduledSignLimit.click();
        input_ScheduledSignLimit.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    ///////////////////////
    //   PUBLIC METHODS  //
    ///////////////////////

    public void fillOutSigns(GeneratePolicy policy) {

        CPPInlandMarineSignsCM signs = policy.inlandMarineCPP.getSigns();

        //COVERAGES
        clickCoveragesTab();

        setDeductible(signs.getDeductible());
        setCoinsurance(signs.getCoinsurance());

        for (CPPInlandMarineSignsCM_ScheduledSign scheduledSign : signs.getScheduledSigns()) {
            clickAddScheduledSign();

            setLocation(scheduledSign.getProperty());
            setProperty(scheduledSign.getProperty());
            setSignLettering(scheduledSign.getSignLettering());
            setSignType(scheduledSign.getSignType());
            setBooleanSelect(select_InteriorSign(), scheduledSign.isInteriorSign());
            setBooleanSelect(select_AttachedToBuilding(), scheduledSign.isAttachedToBuilding());
            setSignLimit("" + scheduledSign.getLimit());
        }

        //EXCLUSIONS AND CONDITIONS
        clickExclusionsAndConditionsTab();

    }

}
