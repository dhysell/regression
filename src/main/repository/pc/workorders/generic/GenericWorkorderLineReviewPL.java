package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

import java.util.ArrayList;
import java.util.Date;

public class GenericWorkorderLineReviewPL extends GenericWorkorder {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderLineReviewPL(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }


    public void fillOutLineReview(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyLineReview();

    }


    @FindBy(xpath = "//div[contains(text(),'Accidental Death')]/../following-sibling::td/div")
    private WebElement lineReviewAccidentalDeath;

    @FindBy(xpath = "//div[contains(text(),'Deductible')]/../following-sibling::td/div")
    private WebElement propertyLineDeductible;

    @FindBy(xpath = "//div[contains(text(),'Damage to Property')]/../following-sibling::td/div")
    private WebElement lineReviewDamagePropOthers;

    @FindBy(xpath = "//div[contains(text(),'Liability')]/../following-sibling::td/div")
    private WebElement lineReviewLiabilityBIPD;

    @FindBy(xpath = "//div[contains(text(),'Medical')]/../following-sibling::td/div")
    private WebElement lineReviewMedical;

    @FindBy(xpath = "//div[contains(text(),'Uninsured Motorist - Bodily Injury')]/../following-sibling::td/div")
    private WebElement lineReviewUninsuredMotoristBodilyInjury;

    @FindBy(xpath = "//div[contains(text(),'Rental Reimbursement')]/../following-sibling::td/div")
    private WebElement lineReviewRentalReimbursement;

    @FindBy(xpath = "//div[contains(text(),'Loss of Use by Theft - Reimbursement')]/../following-sibling::td/div")
    private WebElement lineReviewLossOfUseByTheftReimbursement;

    @FindBy(xpath = "//div[contains(text(),'Emergency Roadside Service')]/../following-sibling::td/divs")
    private WebElement lineReviewEmergencyRoadService;

    @FindBy(xpath = "//div[contains(@id, ':PAVehicleSummaryLV')]")
    private WebElement lineReviewVehicleTable;

    @FindBy(xpath = "//a[contains(@id,':SectionICVTab')]")
    private WebElement sectionOneCoveragesTab;

    @FindBy(xpath = "//a[contains(@id,':SectionIICVTab')]")
    private WebElement sectionTwoCoveragesTab;

    @FindBy(xpath = "//span[contains(., 'Additional Insureds')]/parent::span[contains(@id, 'AdditionalInsuredsTab-btnEl')]")
    private WebElement link_LineReviewAdditionalInsureds;

    @FindBy(xpath = "//label[contains(., 'Additional Insureds')]/ancestor::tr[2]/following-sibling::tr/td/div")
    private WebElement divContainer_LineReviewAdditionalInsureds;


    public void clickSectionOneCoverages() {
        clickWhenClickable(sectionOneCoveragesTab);
    }

    //@FindBy(xpath = "//div[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:LineReviewScreen:ReviewSummaryCV:PolicyLineSummaryPanelSet:HOLineReviewCV:HOLineReviewCV:0:HOSectionILineReviewLV']")
    //[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:LineReviewScreen:ReviewSummaryCV:PolicyLineSummaryPanelSet:HOLineReviewCV:HOLineReviewCV:std_fire:PolicyLineSummaryPanelSet:0:HOSectionILineReviewLV']")

    @FindBy(xpath = "//div[contains(@id, ':HOLineReviewCV:0:HOSectionILineReviewLV') or contains (@id, ':HOLineReviewCV:std_fire:PolicyLineSummaryPanelSet:LocationsIterator:0:HOSectionILineReviewLV')]")
    private WebElement table_PropertyCoverages;

    @FindBy(xpath = "//div[contains(@id, ':PolicyLineSummaryPanelSet:HOLineReviewCV:HOLineReviewCV:stdLiability:PolicyLineSummaryPanelSet:2')]")
    private WebElement table_SectionIISummaryCoverages;


    public WebElement getPropertyCovageTable() {
        return table_PropertyCoverages;
    }


    public int getPropertyCoverageTableRowCount() {
        
        return tableUtils.getRowCount(table_PropertyCoverages);
    }


    public String getPropertyCoverageTableCellByRowNumberColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PropertyCoverages, row, columnName);
    }

    public WebElement getPropertyLineDeductible() {
        return propertyLineDeductible;
    }


    public void clickSectionTwoCoverages() {
        clickWhenClickable(sectionTwoCoveragesTab);
        
        long endTime = new Date().getTime() + 10000;
        boolean isOnPage = new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, 'PolicyLineSummaryPanelSet:SummaryTab-btnInnerEl')]");
        while (!isOnPage && new Date().getTime() < endTime) {
            
            isOnPage = new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, 'PolicyLineSummaryPanelSet:SummaryTab-btnInnerEl')]");
        }
        if (!isOnPage) {
            Assert.fail("Unable to get to Section II Coverages tab within 10 seconds.");
        }
    }

    public double getLineReviewAccidentalDeath() {
        return Double.parseDouble(lineReviewAccidentalDeath.getText());
    }

    public String getLineReviewLiabilityBIPD() {
        return lineReviewLiabilityBIPD.getText();
    }

    public String getLineReviewMedical() {
        return lineReviewMedical.getText();
    }

    public double getDamageToPropertyOthers() {
        return Double.parseDouble(lineReviewDamagePropOthers.getText());
    }

    public String getLineReviewUninsuredMotoristBodilyInjury() {
        return lineReviewUninsuredMotoristBodilyInjury.getText();
    }

    public double getLineReviewRentalReimbursement() {
        return Double.parseDouble(lineReviewRentalReimbursement.getText());
    }

    public double getLineReviewLossOfUseByTheftReimbursement() {
        return Double.parseDouble(lineReviewLossOfUseByTheftReimbursement.getText());
    }

    public double getLineReviewEmergencyRoadService() {
        return NumberUtils.getCurrencyValueFromElement(lineReviewEmergencyRoadService.getText());
    }

    public int getLineReviewVehicleTableRowCount() {
        return tableUtils.getRowCount(lineReviewVehicleTable);
    }

    public ArrayList<String> getLineReviewVehicleColumnName(String columnName) {
        return tableUtils.getAllCellTextFromSpecificColumn(lineReviewVehicleTable, columnName);
    }


    public String getLineReviewPropertyAddresses(int locationNumber) {
        WebElement lineReivew_PropertyLocations = find(By.xpath("//div[contains(@id, ':LineReviewScreen:ReviewSummaryCV:PolicyLineSummaryPanelSet:HOLineReviewCV:HOLineReviewCV:" + (locationNumber - 1) + ":0')]"));
        return lineReivew_PropertyLocations.getText();
    }


    public void clickAdditionalInsureds() {
        clickWhenClickable(link_LineReviewAdditionalInsureds);
        
    }


    public ArrayList<String> getAdditionalInsureds() {
        clickAdditionalInsureds();
        
        return tableUtils.getAllCellTextFromSpecificColumn(divContainer_LineReviewAdditionalInsureds, "Name");
    }


    public ArrayList<String> getLimitColumn() {
        return tableUtils.getAllCellTextFromSpecificColumn(table_PropertyCoverages, "Limit");

    }


    public String getOccurenceLimit(String desc) {
        String limit = null;
        int row = tableUtils.getRowNumberInTableByText(table_SectionIISummaryCoverages, desc);
        for (int currentRow = row; currentRow <= tableUtils.getRowCount(table_SectionIISummaryCoverages); currentRow++) {
            WebElement descElement = table_SectionIISummaryCoverages.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex, '" + (currentRow - 1) + "')]/td[1]"));
            WebElement limitElement = table_SectionIISummaryCoverages.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex, '" + (currentRow - 1) + "')]/td[2]"));
            if (descElement.getText().contains("Per Occurence Limit")) {
                limit = limitElement.getText();
                break;
            }
        }

        return limit.replace(",", "");
    }
}
