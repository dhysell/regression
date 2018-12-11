package repository.pc.workorders.generic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import services.broker.objects.lexisnexis.clueauto.response.actual.CluePersonalAuto;
import services.broker.objects.lexisnexis.generic.request.ReportType;
import services.broker.services.lexisnexis.ServiceLexisNexis;
import repository.driverConfiguration.BasePage;
import services.enums.Broker;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.generate.custom.CLUEAutoInfo;
import repository.gw.generate.custom.UIAutoClaimReported;
import repository.gw.generate.custom.UIClaimReportedPayoutReserve;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.LexisNexis;
import persistence.globaldatarepo.helpers.LexisNexisHelper;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderSquireCLUEAuto extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderSquireCLUEAuto(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[contains(@id, ':LineWizardStepSet:PAWizardStepGroup:PAPriorLossExtScreen:PAPriorLossExtPanelSet:LossListDetailPanel:LossesLV_tb:ClueButton') or contains(@id, ':orderClueButton')]")
    private WebElement button_RetrieveCLUE;
    @FindBy(xpath = "//a[contains(@id, ':LineWizardStepSet:PAWizardStepGroup:PAPriorLossExtScreen:PAPriorLossExtPanelSet:LossListDetailPanel:LossesLV_tb:FlagClaimsAsInvalid')]")
    private WebElement button_IncludeExclude;
    @FindBy(xpath = "//a[contains (@id,'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PAPriorLossExtScreen:PAPriorLossExtPanelSet:OrderWithConfirmationID')]")
    private WebElement button_OrderAutoHistory;

    public void clickOrderAutoHistory() {
        clickWhenClickable(button_OrderAutoHistory);
    }

    public void clickRetrieveCLUE() {
        clickWhenClickable(button_RetrieveCLUE);
    }


    public boolean checkRetrieveCLUE() {
        return checkIfElementExists(button_RetrieveCLUE, 1000);
    }

    public void clickIncludeExclude() {
        clickWhenClickable(button_IncludeExclude);
    }

    public CLUEAutoInfo clickRetrieveCLUEAndAlsoValidateDirectlyFromBrokerForComparison(String firstName, String middleName, String lastName) throws Exception {
    	repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);
    	sideMenu.clickSideMenuClueAuto();
        clickRetrieveCLUE();
        boolean printRequestXMLToConsole = false;
        boolean printResponseXMLToConsole = true;
        Broker conn = new GuidewireHelpers(driver).getMessageBrokerConnDetails();
        systemOut(conn.name());
        ServiceLexisNexis testService = new ServiceLexisNexis();
        LexisNexis[] randomCustomers = {LexisNexisHelper.getCustomerByName(firstName, middleName, lastName)};
        CluePersonalAuto testResponse = testService.orderCLUEAuto(testService.setUpTestOrder(ReportType.CLUE_AUTO, randomCustomers), conn, printRequestXMLToConsole, printResponseXMLToConsole);
        CLUEAutoInfo toReturn = new CLUEAutoInfo();
        toReturn.setBrokerReport(testResponse);
        try {
            toReturn.setClaimsReported(getClaimsReported());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturn;
    }

    @FindBy(xpath = "//div[contains(@id,':LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PAPriorLossExtScreen:PAPriorLossExtPanelSet:LossListDetailPanel:LossesLV')]//table")
    private WebElement table_ClaimsReport;

    private WebElement getClaimsReportedTable() {
        return table_ClaimsReport;
    }

    private List<WebElement> getClaimsReportedRows() {
        return table_ClaimsReport.findElements(By.xpath(".//tr"));
    }

    @FindBy(xpath = "//div[contains(@id,':LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PAPriorLossExtScreen:PAPriorLossExtPanelSet:LossListDetailPanel:clueLossDetailsCV:PAPriorLossPaymentExtLV-body')]//table")
    private WebElement table_ClaimsReportPayouts;

    private List<WebElement> getClaimsReportedPayoutsRows() {
        return table_ClaimsReportPayouts.findElements(By.xpath(".//tr"));
    }

    @FindBy(xpath = "//div[contains(@id,':LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PAPriorLossExtScreen:PAPriorLossExtPanelSet:LossListDetailPanel:clueLossDetailsCV:PAPriorLossDetailsExtDV')]//table")
    private WebElement table_ClaimsReportDetails;

    private String getUIData(String labelName) {
        WebElement toUse = table_ClaimsReportDetails.findElement(By.xpath(".//label[.='" + labelName + "']/parent::td/following-sibling::td/div"));
        return toUse.getText();
    }

    private ArrayList<UIAutoClaimReported> getClaimsReported() {
        ArrayList<UIAutoClaimReported> toReturn = new ArrayList<UIAutoClaimReported>();
        UIAutoClaimReported row = new UIAutoClaimReported();
        for (int rowNumber = 1; rowNumber <= getClaimsReportedRows().size(); rowNumber++) {
            clickWhenClickable(getClaimsReportedTable().findElement(By.xpath(".//tr[" + rowNumber + "]")));
            row.setClaimDate(getClaimsReportedTable().findElement(By.xpath(".//tr[" + rowNumber + "]//td[2]/div")).getText());
            row.setPolicyHolderName(getClaimsReportedTable().findElement(By.xpath(".//tr[" + rowNumber + "]//td[3]/div")).getText());
            row.setDriverName(getClaimsReportedTable().findElement(By.xpath(".//tr[" + rowNumber + "]//td[4]/div")).getText());
            row.setClaimAge(getClaimsReportedTable().findElement(By.xpath(".//tr[" + rowNumber + "]//td[5]/div")).getText());

            ArrayList<UIClaimReportedPayoutReserve> rowPayouts = new ArrayList<UIClaimReportedPayoutReserve>();
            for (WebElement we2 : getClaimsReportedPayoutsRows()) {
                UIClaimReportedPayoutReserve row2 = new UIClaimReportedPayoutReserve();
                row2.setClaimType(we2.findElement(By.xpath(".//td[2]/div")).getText());
                row2.setStatus(we2.findElement(By.xpath(".//td[3]/div")).getText());
                row2.setPaymentAmount(we2.findElement(By.xpath(".//td[4]/div")).getText());
                rowPayouts.add(row2);
            }

            row.setPayoutsReserves(rowPayouts);
            row.setSubClaimDate(getUIData("Claim Date"));
            row.setClaimStatus(getUIData("Claim Status"));
            row.setAtFaultIndicator(getUIData("At Fault Indicator"));
            row.setClaimScope(getUIData("Claim Scope"));
            row.setSubClaimType(getUIData("Claim Type"));
            row.setClaimNumber(getUIData("Claim Number"));
            row.setClaimDescription(getUIData("Claim Description"));
            row.setPaymentDate(getUIData("Payment Date"));
            row.setSubClaimAge(getUIData("Claim Age"));
            row.setDisputeDate(getUIData("Dispute Date"));
            row.setCatastropheIndicator(getUIData("Catastrophe Indicator"));
            row.setInsurer(getUIData("Insurer"));
            row.setSubPolicyHolderName(getUIData("Policy Holder Name"));
            row.setAddress(getUIData("Address"));
            row.setCity(getUIData("City"));
            row.setState(getUIData("State"));
            row.setZip(getUIData("Zip"));
            row.setLicenseNumber(getUIData("License Number"));
            row.setLicenseState(getUIData("License State"));
            row.setVehicleOperator(getUIData("Vehicle Operator"));
            row.setVehicleOperatorDOB(getUIData("Vehicle Operator DOB"));
            row.setOperatorGender(getUIData("Operator Gender"));
            row.setVehicleYear(getUIData("Vehicle Year"));
            row.setVehicleMakeAndModel(getUIData("Vehicle Make & Model"));
            row.setVin(getUIData("VIN"));

            toReturn.add(row);
        }

        return toReturn;
    }

    @FindBy(xpath = "//div[contains(@id,':LOBWizardStepGroup:LineWizardStepSet:PAWizardStepGroup:PAPriorLossExtScreen:PAPriorLossExtPanelSet:LossListDetailPanel:LossesLV')]")
    private WebElement tableUtilsTable_ClaimsReport;


    public void setVehiclesOnAssignedDriversToWaived() throws Exception {
        String claimDateStr = null;
        BigDecimal totalPaymentAmount = new BigDecimal(0);

        Date todaysPCDate = ClockUtils.getCurrentDates(driver).get(ApplicationOrCenter.PolicyCenter);

        for (WebElement we : getClaimsReportedRows()) {
            clickWhenClickable(we);
            for (WebElement we2 : getClaimsReportedPayoutsRows()) {

                String amountToAdd = we2.findElement(By.xpath(".//td[4]/div")).getText();
                BigDecimal toAdd = new BigDecimal(amountToAdd);

                totalPaymentAmount = totalPaymentAmount.add(toAdd);
            }
//			claimDateStr = we.findElement(By.xpath(".//td[2]/div")).getText();						
            Date claimDate = DateUtils.convertStringtoDate(claimDateStr, "MM/dd/yyyy");
            int numMonthsDiff = DateUtils.getDifferenceBetweenDates(todaysPCDate, claimDate, DateDifferenceOptions.Month);

            boolean ageCheck = numMonthsDiff < 35;

            int paymentCompare = totalPaymentAmount.compareTo(new BigDecimal(750));
            boolean paymentCheck1 = paymentCompare == 0;
            boolean paymentCheck2 = paymentCompare == 1;
            boolean paymentCheck = paymentCheck1 || paymentCheck2;
            if (ageCheck && paymentCheck) {
                selectValueForSelectInTable(tableUtilsTable_ClaimsReport, tableUtils.getHighlightedRowNumber(tableUtilsTable_ClaimsReport), "Assign To Vehicle", "Waived");
                setValueForCellInsideTable(tableUtilsTable_ClaimsReport, tableUtils.getHighlightedRowNumber(tableUtilsTable_ClaimsReport), "Reason For Waiver", "ReasonForWaiverID", "Default Reason");
            }

        }

    }


    public void setVehiclesToAssignedDriversToWaived() throws Exception {
        String claimDateStr = null;
        BigDecimal totalPaymentAmount = new BigDecimal(0);

        Date todaysPCDate = ClockUtils.getCurrentDates(driver).get(ApplicationOrCenter.PolicyCenter);
        for (int rowNumber = 1; rowNumber <= getClaimsReportedRows().size(); rowNumber++) {

            clickWhenClickable(getClaimsReportedTable().findElement(By.xpath(".//tr[" + rowNumber + "]")));
            claimDateStr = getClaimsReportedTable().findElement(By.xpath(".//tr[" + rowNumber + "]//td[2]/div")).getText();
            for (WebElement we2 : getClaimsReportedPayoutsRows()) {
                String amountToAdd = we2.findElement(By.xpath(".//td[4]/div")).getText();
                BigDecimal toAdd = new BigDecimal(amountToAdd);
                totalPaymentAmount = totalPaymentAmount.add(toAdd);
            }
            Date claimDate = DateUtils.convertStringtoDate(claimDateStr, "MM/dd/yyyy");
            int numMonthsDiff = DateUtils.getDifferenceBetweenDates(todaysPCDate, claimDate, DateDifferenceOptions.Month);

            boolean ageCheck = numMonthsDiff < 35;

            int paymentCompare = totalPaymentAmount.compareTo(new BigDecimal(750));
            boolean paymentCheck1 = paymentCompare == 0;
            boolean paymentCheck2 = paymentCompare == 1;
            boolean paymentCheck = paymentCheck1 || paymentCheck2;
            if (ageCheck && paymentCheck) {
                String Included = tableUtils.getCellTextInTableByRowAndColumnName(tableUtilsTable_ClaimsReport, rowNumber, "Included");
                if (Included.equals("Yes")) {
                    selectValueForSelectInTable(tableUtilsTable_ClaimsReport, tableUtils.getHighlightedRowNumber(tableUtilsTable_ClaimsReport), "Assign To Vehicle", "Waived");
                    setValueForCellInsideTable(tableUtilsTable_ClaimsReport, tableUtils.getHighlightedRowNumber(tableUtilsTable_ClaimsReport), "Reason For Waiver", "ReasonForWaiverID", "Default Reason");
                }
            }
        }
    }
    

    private void selectValueForSelectInTable(WebElement tableDivElement, int tableRowNumber, String headerColumnText, String valueToSelect) {
        String gridColumnID = tableUtils.getGridColumnFromTable(tableDivElement, headerColumnText);
        WebElement tableCell = tableDivElement.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + gridColumnID + "')]/div"));
       clickWhenClickable(tableCell);
        Actions action = new Actions(driver);
        action.moveToElement(tableCell).doubleClick().build().perform();
        Guidewire8Select mySelect = new Guidewire8Select(driver, "(//table[contains(@id,'simplecombo') and contains(@id,'triggerWrap')])[last()]");
        mySelect.selectByVisibleTextPartial(valueToSelect);
       clickProductLogo();
    }

    private void setValueForCellInsideTable(WebElement tableDivElement, int tableRowNumber, String tableHeaderName, String cellInputName, String valueToSet) {
        String gridColumnID = tableUtils.getGridColumnFromTable(tableDivElement, tableHeaderName);
        WebElement tableCell = tableDivElement.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + gridColumnID + "')]/div"));
       clickWhenClickable(tableCell);
        Actions action = new Actions(driver);
        action.moveToElement(tableCell).doubleClick().build().perform();
        WebElement editBox_DesktopActionsLienholderMultipleAccountPaymentsChargeGroupInner = tableDivElement.findElement(By.xpath(".//input[contains(@name,'" + cellInputName + "')] | .//textarea[contains(@name,'" + cellInputName + "')]"));
        editBox_DesktopActionsLienholderMultipleAccountPaymentsChargeGroupInner.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editBox_DesktopActionsLienholderMultipleAccountPaymentsChargeGroupInner.sendKeys(valueToSet);
       clickProductLogo();
    }


    public void setAssignedToVehicle(String value) {
        selectValueForSelectInTable(tableUtilsTable_ClaimsReport, tableUtils.getHighlightedRowNumber(tableUtilsTable_ClaimsReport), "Assign To Vehicle", value);
    }


    public void setAssignedToVehicleSRP() {
        int tableRowNumber = tableUtils.getRowCount(tableUtilsTable_ClaimsReport);
        for (int i = 1; i <= tableRowNumber; i++) {
            String point = tableUtils.getCellTextInTableByRowAndColumnName(tableUtilsTable_ClaimsReport, i, "Included");
            if (point.equals("Yes")) {
                tableUtils.clickRowInTableByRowNumber(tableUtilsTable_ClaimsReport, i);
                tableUtils.getCellTextInTableByRowAndColumnName(tableUtilsTable_ClaimsReport, i, "Assign To Vehicle");
                selectValueForSelectInTable(tableUtilsTable_ClaimsReport, tableUtils.getHighlightedRowNumber(tableUtilsTable_ClaimsReport), "Assign To Vehicle", "in");
            }
        }

    }


    public String getClaimsReportTableCellByRowColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(tableUtilsTable_ClaimsReport, row, columnName);
    }


    public void selectClueAutoLossesCheckBoxByRow(int row) {
        tableUtils.setCheckboxInTable(tableUtilsTable_ClaimsReport, row, true);
    }


    public boolean checkIncludExcludeButtonDisabled() {
        try {
            return button_IncludeExclude.getAttribute("class").contains("disabled");
        } catch (Exception e) {
            return false;
        }
    }


    @FindBy(xpath = "//div[contains(@id,':PAPriorLossExtScreen:PAPriorLossExtPanelSet:SelectedReportListPanelLV:SelectedClaimListDetailPanel:APAutoClaimsLV-body')]//table")
    private WebElement table_AutoHistoryClaimsReport;

    private WebElement getAutoHistoryClaimsReportedTable() {
        return table_AutoHistoryClaimsReport;
    }

    private List<WebElement> getAutoHistoryClaimsReportedRows() {
        return table_AutoHistoryClaimsReport.findElements(By.xpath(".//tr"));
    }


    public ArrayList<UIAutoClaimReported> getAutoHistoryClaimsReported() {
        ArrayList<UIAutoClaimReported> toReturn = new ArrayList<UIAutoClaimReported>();
        UIAutoClaimReported row = new UIAutoClaimReported();
        for (int rowNumber = 1; rowNumber <= getAutoHistoryClaimsReportedRows().size(); rowNumber++) {
            clickWhenClickable(getAutoHistoryClaimsReportedTable().findElement(By.xpath(".//tr[" + rowNumber + "]")));

            row.setClaimNumber(getAutoHistoryClaimsReportedTable().findElement(By.xpath(".//tr[" + rowNumber + "]//td[2]/div")).getText());
            row.setClaimDate(getAutoHistoryClaimsReportedTable().findElement(By.xpath(".//tr[" + rowNumber + "]//td[5]/div")).getText());

           /* row.setSubClaimDate(getUIData("Claim Date"));
            row.setClaimStatus(getUIData("Claim Status"));
            row.setAtFaultIndicator(getUIData("At Fault Indicator"));
            row.setClaimScope(getUIData("Claim Scope"));
            row.setSubClaimType(getUIData("Claim Type"));
            row.setClaimNumber(getUIData("Claim Number"));
            row.setClaimDescription(getUIData("Claim Description"));
            row.setPaymentDate(getUIData("Payment Date"));
            row.setSubClaimAge(getUIData("Claim Age"));
            row.setDisputeDate(getUIData("Dispute Date"));
            row.setCatastropheIndicator(getUIData("Catastrophe Indicator"));
            row.setInsurer(getUIData("Insurer"));
            row.setSubPolicyHolderName(getUIData("Policy Holder Name"));
            row.setAddress(getUIData("Address"));
            row.setCity(getUIData("City"));
            row.setState(getUIData("State"));
            row.setZip(getUIData("Zip"));
            row.setLicenseNumber(getUIData("License Number"));
            row.setLicenseState(getUIData("License State"));
            row.setVehicleOperator(getUIData("Vehicle Operator"));
            row.setVehicleOperatorDOB(getUIData("Vehicle Operator DOB"));
            row.setOperatorGender(getUIData("Operator Gender"));
            row.setVehicleYear(getUIData("Vehicle Year"));
            row.setVehicleMakeAndModel(getUIData("Vehicle Make & Model"));
            row.setVin(getUIData("VIN"));*/

            toReturn.add(row);
        }

        return toReturn;
    }

    @FindBy(xpath = "//label[contains(@id,':PAPriorLossExtScreen:PAPriorLossExtPanelSet:APPropReportLabelClaimNotFound')]")
    private WebElement noClaims_AutoHistory;

    @FindBy(xpath = "//label[contains(@id,':PAPriorLossExtScreen:PAPriorLossExtPanelSet:LossHistoryInputSet:0')]")
    private WebElement noLoss_AutoHistory;
    public boolean isNoMatchesFound(){

        boolean noLoss= checkIfElementExists(noLoss_AutoHistory, 10) &&  noLoss_AutoHistory.getText().equals("No Loss History");
        boolean noClaims= checkIfElementExists(noClaims_AutoHistory, 10) &&  noClaims_AutoHistory.getText().equals("Claims Activity Profiler: No Matches");
        return noLoss&&noClaims;
    }
}
