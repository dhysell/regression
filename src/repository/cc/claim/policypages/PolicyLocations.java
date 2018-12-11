package repository.cc.claim.policypages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;
import repository.gw.helpers.WaitUtils;

import java.util.ArrayList;
import java.util.List;

public class PolicyLocations extends BasePage {

    private TableUtils tableUtils;
    private WaitUtils waitUtilts;

    public PolicyLocations(WebDriver driver) {
        super(driver);
        this.tableUtils = new TableUtils(driver);
        this.waitUtilts = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[@id='ClaimPolicyLocations:ClaimPolicyLocationsScreen:PolicyLocationLDV:PolicyLocationsLV']")
    private WebElement table_PolicyLocations;

    @FindBy(xpath = "//div[contains(@id,'LocationBasedRULV') and not(contains(@id,'-body'))]")
    private WebElement table_CoveredRisks;

    @FindBy(xpath = "//a[contains(@id,'RiskDetailTab')]")
    private WebElement tab_RiskDetails;

    @FindBy(xpath = "//a[contains(@id,'LocationCoveragesTab')]")
    private WebElement tab_Coverages;

    @FindBy(xpath = "//div[@id='ClaimPolicyLocations:ClaimPolicyLocationsScreen:PolicyLocationLDV:PolicyLocationRiskDetailPanelSet:LocationCoverageListDetail:EditablePropertyCoveragesLV']")
    private WebElement table_Coverages;

    @FindBy(xpath = "//div[@id='ClaimPolicyLocations:ClaimPolicyLocationsScreen:PolicyLocationLDV:PolicyLocationRiskDetailPanelSet:LocationCoverageListDetail:ClaimPolicyCovTermsCV:ClaimPolicyCovTermsLV']")
    private WebElement table_CoverageTerms;

    @FindBy(css = "span[id*='ClaimPolicyLocations_AddMoreLocationsButton-btnInnerEl']")
    private WebElement buttonAddDeleteLocations;

    @FindBy(xpath = "//a[contains(@id, 'Update')] | //span[contains(@id, ':FinishPCR-btnEl')]")
    private WebElement buttonUpdate;

    public void clickAddDeleteLocationsButton() {
        waitUtilts.waitUntilElementIsClickable(buttonAddDeleteLocations, 5);
        buttonAddDeleteLocations.click();
    }

    public void clickUpdateButton() {
        waitUntilElementIsVisible(buttonUpdate, 3);
        buttonUpdate.click();
    }

    private void clickCoveragesTab() {
        clickWhenClickable(tab_Coverages);
    }

    private int pickPolicyLocationByAddress(String address) {
        int row = tableUtils.getRowNumberInTableByText(table_PolicyLocations, address);
        tableUtils.clickCellInTableByRowAndColumnName(table_PolicyLocations, row, "Lienholders");
        return row;
    }

    private int pickCoveredRiskBy(String risk) {
        tableUtils.clickRowInTableByText(table_CoveredRisks, risk);
        return tableUtils.getHighlightedRowNumber(table_CoveredRisks);
    }

    private int pickCoverageByType(String type) {
        tableUtils.clickRowInTableByText(table_Coverages, type);
        int rowNum = tableUtils.getHighlightedRowNumber(table_Coverages);
        return rowNum;
    }

    private String getCoverageTermValueBySubject(String subject) {
        int rowNum = tableUtils.getRowNumberInTableByText(table_CoverageTerms, subject);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_CoverageTerms, rowNum, "Description");
    }

    private double getCoverageExposureLimit(int row) {
        String numAsString = tableUtils.getCellTextInTableByRowAndColumnName(table_Coverages, row, "Exposure Limit");
        numAsString = numAsString.replace("$", "");
        numAsString = numAsString.replace(",", "");
        return Double.parseDouble(numAsString);
    }

    private double getCoverageIncidentLimit(int row) {
        String numAsString = tableUtils.getCellTextInTableByRowAndColumnName(table_Coverages, row, "Incident Limit");
        numAsString = numAsString.replace("$", "");
        numAsString = numAsString.replace(",", "");
        return Double.parseDouble(numAsString);
    }

    private double getCoverageDeductible(int row) {
        String numAsString = tableUtils.getCellTextInTableByRowAndColumnName(table_Coverages, row, "Deductible");
        numAsString = numAsString.replace("$", "");
        numAsString = numAsString.replace(",", "");
        return Double.parseDouble(numAsString);
    }


    public double getCoverageExposureLimit(String locationAddress, String coveredRisk, String coverageType) {
        pickPolicyLocationByAddress(locationAddress);
        
        pickCoveredRiskBy(coveredRisk);
        
        clickCoveragesTab();
        
        int row = pickCoverageByType(coverageType);
        
        return getCoverageExposureLimit(row);

    }


    public double getAutoIncreasePercentage(String locationAddress, String coveredRisk, String coverageType, String coverageTermSubject) {

        pickPolicyLocationByAddress(locationAddress);
        
        pickCoveredRiskBy(coveredRisk);
        
        clickCoveragesTab();
        
        pickCoverageByType(coverageType);
        
        String value = getCoverageTermValueBySubject(coverageTermSubject);

        double valAsPercent = Double.parseDouble(value.replace("%", "")) / 100.00;
        return valAsPercent;

    }


    /**
     * @return deductibleAmount, exposureAmount, incidentAmount
     * @Description This function gets the deductible, exposure limit, and incident limit for a given coverage
     */
    public List<String> getCoverageLimitAmounts(String locationAddress, String coveredRisk, String coverageType) {
        pickPolicyLocationByAddress(locationAddress);
        
        pickCoveredRiskBy(coveredRisk);
        
        clickCoveragesTab();
        
        int row = pickCoverageByType(coverageType);
        ArrayList<String> amounts = new ArrayList<String>();

        String deductibleAmt = Double.toString(getCoverageDeductible(row));
        String exposureAmnt = Double.toString(getCoverageExposureLimit(row));
        String incidentAmt = Double.toString(getCoverageIncidentLimit(row));

        amounts.add(deductibleAmt);
        amounts.add(exposureAmnt);
        amounts.add(incidentAmt);
        return amounts;
    }

}
