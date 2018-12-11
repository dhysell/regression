package repository.pc.workorders.generic;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.InlandMarineCPP.InstallationFloaterCoverage_IDCM_31_4073_Deductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPInlandMarineInstallation;

public class GenericWorkorderInlandMarineInstallationCPP extends BasePage {
    private WebDriver driver;

    public GenericWorkorderInlandMarineInstallationCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    ///////////////////////
    //       Tabs        //
    ///////////////////////

    @FindBy(css = "span[id$=':IMPartScreen:IMInstallationCovDetailCV:CoveragesCovTab-btnEl']")
    private WebElement link_CoveragesTab;

    private void clickCoveragesTab() {
        clickWhenClickable(link_CoveragesTab);
        
    }

    @FindBy(css = "span[id$=':IMPartScreen:IMInstallationCovDetailCV:CPBuildingExclCondCardPropertyTab-btnEl']")
    private WebElement link_ExclusionsAndConditionsTab;

    private void clickExclusionsAndConditionsTab() {
        clickWhenClickable(link_ExclusionsAndConditionsTab);
        
    }

    @FindBy(css = "span[id$=':IMPartScreen:IMInstallationCovDetailCV:ImInstallationAddlInterestTab-btnEl']")
    private WebElement link_AdditionalInterestTab;

    private void clickAdditionalInterestTab() {
        clickWhenClickable(link_AdditionalInterestTab);
        
    }

    ///////////////////////
    //     Coverages     //
    ///////////////////////

    @FindBy(css = "input[id$=':CoverageInputSet:CovPatternInputGroup:imInstallCovLimit-inputEl']")
    private WebElement input_Limit;

    private void setLimit(String value) {
        input_Limit.click();
        input_Limit.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    private Guidewire8Select select_Deductible() {
        return new Guidewire8Select(driver, "//label[contains(text(),'Deductible')]/parent::td/following-sibling::td/table");
    }

    private void setDeductible(InstallationFloaterCoverage_IDCM_31_4073_Deductible deductible) {
        Guidewire8Select mySelect = select_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
        
    }

    @FindBy(css = "textarea[id$=':CoverageInputSet:CovPatternInputGroup:DedcOfProperty-inputEl']")
    private WebElement textarea_PropertyDescription;

    private void setPropertyDescription(String value) {
        textarea_PropertyDescription.click();
        textarea_PropertyDescription.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    ///////////////////////
    //   PUBLIC METHODS  //
    ///////////////////////

    public void fillOutInstallation(GeneratePolicy policy) {

        CPPInlandMarineInstallation installation = policy.inlandMarineCPP.getInstallation();

        //COVERAGES
        clickCoveragesTab();

        setLimit("" + installation.getLimit());
        setDeductible(installation.getDeductible());
        setPropertyDescription(installation.getDescriptionOfProperty());

        //EXCLUSIONS AND CONDITIONS
        clickExclusionsAndConditionsTab();

        //ADDITIONAL INTEREST
        clickAdditionalInterestTab();

    }

}
