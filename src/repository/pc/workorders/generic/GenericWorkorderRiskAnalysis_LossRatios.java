package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.LossRatioDiscounts;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderRiskAnalysis_LossRatios extends GenericWorkorderRiskAnalysis {
	
	private WebDriver driver;
	private TableUtils tableUtils;
	
    public GenericWorkorderRiskAnalysis_LossRatios(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }


    @FindBy(xpath = "//div[contains(@id, 'LossRatiosPanelSet:0:SectionDiscountDV:discountId-inputEl') or contains(@id, ':RiskAnalysisCV:LossRatiosPanelSet:propertyDiscountId-inputEl')]")
    private WebElement lossRatioPropertyDiscountDefault;

    @FindBy(xpath = "//div[contains(@id, 'LossRatiosPanelSet:1:SectionDiscountDV:discountId-inputEl')]")
    private WebElement lossRatioLiabilityDiscountDefault;

    @FindBy(xpath = "//div[contains(@id, 'LossRatiosPanelSet:3:SectionDiscountDV:discountId-inputEl')]")
    private WebElement lossRatioIMDiscountDefault;

    @FindBy(xpath = "//div[contains(@id, ':LossRatiosPanelSet:2:SectionDiscountDV:discountId-inputEl')]")
    private WebElement lossRatioAutoDiscountDefault;

    @FindBy(xpath = "//div[contains(@id, 'LossRatiosPanelSet:autoDiscountAmount-inputEl')]")
    private WebElement lossRatioAutoDiscount;


    public void fillOutLossRatiosTab(GeneratePolicy policy) {
        fillOutSectionI_Property(policy);
        fillOutSectionII_Liability(policy);
        fillOutSectionIII_Auto(policy);
        fillOutSecitonIV_InlandMarine(policy);
    }

    public void fillOutSectionI_Property(GeneratePolicy policy) {
        clickLossRatioPropertyTab();

    }

    public void fillOutSectionII_Liability(GeneratePolicy policy) {
        clickLossRatioLiabilityTab();

    }

    public void fillOutSectionIII_Auto(GeneratePolicy policy) {
        clickLossRatioAutoTab();

    }

    public void fillOutSecitonIV_InlandMarine(GeneratePolicy policy) {
        clickLossRatioInlandMarineTab();

    }


    @FindBy(xpath = "//span[contains(@id, 'RiskAnalysisCV:LossRatiosPanelSet:2:SectionLRTab-btnWrap')]")
    private WebElement lossRatioAutoTab;

    public void clickLossRatioAutoTab() {
        
        clickWhenClickable(lossRatioAutoTab);
        
    }

    @FindBy(xpath = "//span[contains(@id, 'RiskAnalysisCV:LossRatiosPanelSet:3:SectionLRTab-btnWrap')]")
    private WebElement lossRatioIMTab;

    public void clickLossRatioInlandMarineTab() {
        
        clickWhenClickable(lossRatioIMTab);
        
    }

    @FindBy(xpath = "//span[contains(@id, 'RiskAnalysisCV:LossRatiosPanelSet:0:SectionLRTab-btnWrap')]")
    private WebElement lossRatioPropertyTab;

    public void clickLossRatioPropertyTab() {
        
        clickWhenClickable(lossRatioPropertyTab);
    }

    @FindBy(xpath = "//span[contains(@id, 'RiskAnalysisCV:LossRatiosPanelSet:1:SectionLRTab-btnWrap')]")
    private WebElement lossRatioLiabilityTab;

    public void clickLossRatioLiabilityTab() {
        
        clickWhenClickable(lossRatioLiabilityTab);
        
    }


    public String getDiscountAmt() {
        
        return lossRatioAutoDiscount.getText();
    }

    public String getPropertyDefaultDiscountAmount() {
        
        String defalutValue = "";
        if (checkIfElementExists(lossRatioPropertyDiscountDefault, 1000)) {
            defalutValue = lossRatioPropertyDiscountDefault.getText();
        }
        return defalutValue;
    }


    public String getLiabilityDefaultDiscountAmount() {
        
        String defalutValue = "";
        if (checkIfElementExists(lossRatioLiabilityDiscountDefault, 1000)) {
            defalutValue = lossRatioLiabilityDiscountDefault.getText();
        }
        return defalutValue;
    }


    public String getInlandMarineDefaultDiscountAmount() {
        
        String defalutValue = "";
        if (checkIfElementExists(lossRatioIMDiscountDefault, 1000)) {
            defalutValue = lossRatioIMDiscountDefault.getText();
        }
        return defalutValue;
    }

    public String getAutoDefaultDiscountAmount() {
        
        String defalutValue = "";
        if (checkIfElementExists(lossRatioAutoDiscountDefault, 1000)) {
            defalutValue = lossRatioAutoDiscountDefault.getText();
        }
        return defalutValue;
    }


    public double getLossRatioAutoSummaryTotalLossRatio() {
        WebElement element = table_LossRatioAutoLossSummary.findElement(By.xpath(".//tfoot/tr/td[4]"));
        String lossRatio = element.getText();
        return Double.parseDouble(lossRatio.replace("%", "").replace(",", ""));
    }


    public double getLossRatioAutoSummaryLossRatioByYear(String yearValue) {
        int row = tableUtils.getRowNumberInTableByText(table_LossRatioAutoLossSummary, yearValue);
        String lossRatio = tableUtils.getCellTextInTableByRowAndColumnName(table_LossRatioAutoLossSummary, row, "L/R");
        return Double.parseDouble(lossRatio.replace("%", "").replace(",", ""));
    }


    public double getLossRatioPropertySummaryTotalLossRatio() {
        WebElement element = table_LossRatioPropertyLossSummary.findElement(By.xpath(".//tfoot/tr/td[4]"));
        String lossRatio = element.getText();
        return Double.parseDouble(lossRatio.replace("%", "").replace(",", ""));
    }


    public double getLossRatioPropertySummaryLossRatioByYear(String yearValue) {
        int row = tableUtils.getRowNumberInTableByText(table_LossRatioPropertyLossSummary, yearValue);
        String lossRatio = tableUtils.getCellTextInTableByRowAndColumnName(table_LossRatioPropertyLossSummary, row, "L/R");
        return Double.parseDouble(lossRatio.replace("%", "").replace(",", ""));
    }


    public double getLossRatioIMSummaryTotalLossRatio() {
        WebElement element = table_LossRatioIMLossSummary.findElement(By.xpath(".//tfoot/tr/td[4]"));
        String lossRatio = element.getText();
        return Double.parseDouble(lossRatio.replace("%", "").replace(",", ""));
    }


    public double getLossRatioIMSummaryLossRatioByYear(String yearValue) {
        int row = tableUtils.getRowNumberInTableByText(table_LossRatioIMLossSummary, yearValue);
        String lossRatio = tableUtils.getCellTextInTableByRowAndColumnName(table_LossRatioIMLossSummary, row, "L/R");
        return Double.parseDouble(lossRatio.replace("%", "").replace(",", ""));
    }

    public double getLossRatioPolicyLossesByYear(String expectedYear) {
        int row = tableUtils.getRowNumberInTableByText(table_LossRatioPolicyLossSummary, expectedYear);
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_LossRatioPolicyLossSummary, row, "Losses"));
    }


    public double getLossRatioPolicyLossesLRByYear(String expectedYear) {
        int row = tableUtils.getRowNumberInTableByText(table_LossRatioPolicyLossSummary, expectedYear);
        String lossRatio = tableUtils.getCellTextInTableByRowAndColumnName(table_LossRatioPolicyLossSummary, row, "L/R");
        if (lossRatio.contains("-")) {
            return 0;
        } else {
            return Double.parseDouble(lossRatio.replace("%", "").replace(",", ""));
        }
    }


    public double getLossRatioPolicyLossesPremiumByYear(String expectedYear) {
        int row = tableUtils.getRowNumberInTableByText(table_LossRatioPolicyLossSummary, expectedYear);
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_LossRatioPolicyLossSummary, row, "Premium"));
    }


    public double getLossRatioAutoSummaryTotalLoss() {
        WebElement element = table_LossRatioAutoLossSummary.findElement(By.xpath(".//tfoot/tr/td[2]"));
        return NumberUtils.getCurrencyValueFromElement(element.getText());
    }


    public double getLossRatioAutoSummaryTotalPremium() {
        WebElement element = table_LossRatioAutoLossSummary.findElement(By.xpath(".//tfoot/tr/td[3]"));
        return NumberUtils.getCurrencyValueFromElement(element.getText());
    }

    @FindBy(xpath = "//div[contains(@id, 'RiskAnalysisCV:LossRatiosPanelSet:TotalLossRatio:PLLossRatioPanelLV')]")
    private WebElement table_LossRatioPolicyLossSummary;


    @FindBy(xpath = "//div[contains(@id, 'RiskAnalysisCV:LossRatiosPanelSet:AutoLossRatioPanel:PLLossRatioPanelLV')]")
    private WebElement table_LossRatioAutoLossSummary;

    @FindBy(xpath = "//div[contains(@id, 'RiskAnalysisCV:LossRatiosPanelSet:sectionILossRatio:PLLossRatioPanelLV')]")
    private WebElement table_LossRatioPropertyLossSummary;


    @FindBy(xpath = "//div[contains(@id, 'RiskAnalysisCV:LossRatiosPanelSet:IMLossRatioPanel:PLLossRatioPanelLV')]")
    private WebElement table_LossRatioIMLossSummary;

    public void setLossRatioPropertyDiscount(LossRatioDiscounts dscountType) {
        Guidewire8Select mySelect = select_LossRatioPropertyDiscount();
        mySelect.selectByVisibleText(dscountType.getValue());
    }


    public String getLossRatioPropertyDiscount() {
        Guidewire8Select mySelect = select_LossRatioPropertyDiscount();
        return mySelect.getText();
    }


    public void setLossRatioLiabilityDiscount(LossRatioDiscounts dscountType) {
        Guidewire8Select mySelect = select_LossRatioLiabilityDiscount();
        mySelect.selectByVisibleText(dscountType.getValue());
    }


    public String getLossRatioLiabilityDiscount() {
        Guidewire8Select mySelect = select_LossRatioLiabilityDiscount();
        return mySelect.getText();
    }


    public void setLossRatioAutoDiscount(LossRatioDiscounts dscountType) {
        Guidewire8Select mySelect = select_LossRatioAutoDiscount();
        mySelect.selectByVisibleText(dscountType.getValue());
    }


    public String getLossRatioAutoDiscount() {
        Guidewire8Select mySelect = select_LossRatioAutoDiscount();
        return mySelect.getText();
    }


    public void setLossRatioIMDiscount(LossRatioDiscounts dscountType) {
        Guidewire8Select mySelect = select_LossRatioIMDiscount();
        mySelect.selectByVisibleText(dscountType.getValue());
    }

    public String getLossRatioIMDiscount() {
        Guidewire8Select mySelect = select_LossRatioIMDiscount();
        return mySelect.getText();
    }


    private Guidewire8Select select_LossRatioPropertyDiscount() {
        return new Guidewire8Select(driver, "//table[contains(@id, '0:SectionDiscountDV:discountId-triggerWrap') or contains(@id, ':LossRatiosPanelSet:stdFireDiscountId-triggerWrap') or contains(@id, ':SectionDiscountDV:discountId-triggerWrap')]");
    }

    private Guidewire8Select select_LossRatioLiabilityDiscount() {
        return new Guidewire8Select(driver, "//table[contains(@id, '1:SectionDiscountDV:discountId-triggerWrap')]");
    }

    private Guidewire8Select select_LossRatioAutoDiscount() {
        return new Guidewire8Select(driver, "//table[contains(@id, '2:SectionDiscountDV:discountId-triggerWrap')]");
    }

    private Guidewire8Select select_LossRatioIMDiscount() {
        return new Guidewire8Select(driver, "//table[contains(@id, '3:SectionDiscountDV:discountId-triggerWrap')]");
    }


}











