package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.generate.custom.PolicyLocation;

public class GenericWorkorderLocationsAdditionalCoverages extends BasePage {
    private WebDriver driver;

    public GenericWorkorderLocationsAdditionalCoverages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    Guidewire8Checkbox checkbox_MoneyAndSecurities() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPLocationAdditionalCoverageCrimeCatDV:0:FBLocationsAddnlCoverageInputSet:CovPatternInputGroup-legendChk')]");
    }

    @FindBy(xpath = "//input[contains(@id, '0:CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl')]")
    public WebElement editbox_NumberOfMessengers;

    Guidewire8RadioButton radio_DepositDaily() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'CovPatternInputGroup:1:CovTermInputSet:BooleanTermInput')]");
    }

    @FindBy(xpath = "//textarea[contains(@id, 'CovPatternInputGroup:2:CovTermInputSet:DirectTermTextBox-inputEl')]")
    public WebElement editbox_FrequencyOfDailyDeposits;

    @FindBy(xpath = "//input[contains(@id, 'CovPatternInputGroup:3:CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl')]")
    public WebElement editbox_MoneyAndSecuritiesOnPremisisLimit;

    @FindBy(xpath = "//input[contains(@id, 'CovPatternInputGroup:4:CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl')]")
    public WebElement editbox_MoneyAndSecuritiesOffPremisisLimit;


    Guidewire8Checkbox checkbox_OutdoorSign() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPLocationAdditionalCoverageBOPLocationCatDV:0:FBLocationsAddnlCoverageInputSet:CovPatternInputGroup-legendChk')]");
    }

    @FindBy(xpath = "//label[contains(text(), 'Outdoor Sign Limit')]/parent::td/following-sibling::td/input[contains(@id, ':CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl')]")
    public WebElement editbox_OutdoorSignLimit;


    Guidewire8Checkbox checkbox_WaterBackupSumpOverflow() {
//		return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPLocationAdditionalCoverageBOPLocationCatDV:1:FBLocationsAddnlCoverageInputSet:CovPatternInputGroup-legendChk')]");
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Water Backup and Sump Overflow BP 04 53')]/preceding-sibling::table");

    }


    public void fillOutLocationsAdditionalCoverages(PolicyLocation loc) {

        if (loc.getAdditionalCoveragesStuff().isMoneyAndSecuritiesCoverage()) {
            setMoneysAndSecuritiesCheckbox(loc.getAdditionalCoveragesStuff().isMoneyAndSecuritiesCoverage());
            setNumberOfMessengers(loc.getAdditionalCoveragesStuff().getMoneySecNumMessengersCarryingMoney());
            clickProductLogo();
            setDepositDailyRadio(loc.getAdditionalCoveragesStuff().isMoneySecDepositDaily());
            clickProductLogo();
            setHowOftenDailyDeposit(loc.getAdditionalCoveragesStuff().getMoneySecHowOftenDeposit());
            setMoneyAndSecurityOnPremisisLimit(loc.getAdditionalCoveragesStuff().getMoneySecOnPremisesLimit());
            setMoneyAndSecurityOffPremisLimit(loc.getAdditionalCoveragesStuff().getMoneySecOffPremisesLimit());
        }

        setWaterBackupSumpOverflowCheckbox(loc.getAdditionalCoveragesStuff().isWaterBackupAndSumpOverflow());
        if (loc.getAdditionalCoveragesStuff().isOutdoorSignsCoverage()) {
            setOutdoorSignCheckbox(loc.getAdditionalCoveragesStuff().isOutdoorSignsCoverage());
            setOutdoorSignLimit(loc.getAdditionalCoveragesStuff().getOutdoorSignsLimit());
        }

    }


    public void setMoneysAndSecuritiesCheckbox(Boolean checkedUnchecked) {
        checkbox_MoneyAndSecurities().select(checkedUnchecked);
    }


    public void setNumberOfMessengers(int number) {
    	setText(editbox_NumberOfMessengers, String.valueOf(number));
    }


    public void setDepositDailyRadio(Boolean yesno) {
        radio_DepositDaily().select(yesno);
    }


    public void setHowOftenDailyDeposit(String frequency) {
    	setText(editbox_FrequencyOfDailyDeposits, frequency);
    }


    public void setMoneyAndSecurityOnPremisisLimit(long limit) {
    	setText(editbox_MoneyAndSecuritiesOnPremisisLimit, String.valueOf(limit));
    }


    public void setMoneyAndSecurityOffPremisLimit(long limit) {
    	setText(editbox_MoneyAndSecuritiesOffPremisisLimit, String.valueOf(limit));
    }


    public void setOutdoorSignCheckbox(Boolean checked) {
        checkbox_OutdoorSign().select(checked);
    }


    public void setOutdoorSignLimit(long limit) {
    	setText(editbox_OutdoorSignLimit, String.valueOf(limit));
    }


    public void setWaterBackupSumpOverflowCheckbox(Boolean checked) {
        Guidewire8Checkbox myCheckbox = checkbox_WaterBackupSumpOverflow();
        myCheckbox.select(checked);
    }


}












