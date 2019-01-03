package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderSquireUmbrellaCoverages extends GenericWorkorder {
	
	private WebDriver driver;

    public GenericWorkorderSquireUmbrellaCoverages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public Guidewire8Select select_IncreasedLimit() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:PUPLiabilityCoverageInfoPUEScreen:CoverageInputSet:CovPatternInputGroup:0:CovTermInputSet:OptionTermInput-triggerWrap') or contains(@id, ':PULI_LiabilityIncreasedLimit_ExtField-triggerWrap')]");
    }

    @FindBy(xpath = "//a[contains(@id, ':PUPLiabilityCoverageInfoPUEScreen:PUPLiabilityCoverageInfoPanelSet:PUPExclusionsAndConditionsTab')]")
    private WebElement link_ExclusionsConditions;

    @FindBy(xpath = "//div[contains(@id, ':PUPLiabilityCoverageInfoPUEScreen:0:PUPCoveragesLV') or contains(@id, ':PUPLiabilityCoverageInfoPanelSet:0:PUPCoveragesLV')]")
    private WebElement table_FarmPersuits;

    @FindBy(xpath = "//div[contains(@id, ':PUPLiabilityCoverageInfoPanelSet:UnderlyingLimit-inputEl')]")
    private WebElement UnderlyingPolicyDetails_Limit;


    @FindBy(xpath = "//a[contains(@id, ':UmbrellaResyncWithSquireBtn')]")
    private WebElement button_ResyncWithSquire;


    public void fillOutUnbrellaCoverages(GeneratePolicy policy) {
        setIncreasedLimit(policy.squireUmbrellaInfo.getIncreasedLimit());

        clickExclusionsConditions();
        if (policy.squireUmbrellaInfo.getExclusionsConditions() != null) {
        	
        }

        clickSaveDraft();
    }


    public void setIncreasedLimit(SquireUmbrellaIncreasedLimit limit) {
        Guidewire8Select ddown = select_IncreasedLimit();
        ddown.selectByVisibleText(limit.getValue());
    }


    public void clickExclusionsConditions() {
        clickWhenClickable(link_ExclusionsConditions);
    }


    public void clickSaveDraft() {
        super.clickGenericWorkorderSaveDraft();
    }


    public boolean checkUnderlyingCoverageExistsByName(String coverage) {
        int row = 0;
        row = new TableUtils(getDriver()).getRowNumberInTableByText(table_FarmPersuits, coverage);
        return row > 0;
    }


    public String getUnderlyingLimit() {
        return UnderlyingPolicyDetails_Limit.getText();
    }


    public void clicResyncWithSquire() {
        clickWhenClickable(button_ResyncWithSquire);
    }

}
