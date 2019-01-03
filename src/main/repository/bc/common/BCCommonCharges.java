package repository.bc.common;

import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.CarryOverCharge;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.*;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BCCommonCharges extends BasePage {

	private TableUtils tableUtils;
	
    public BCCommonCharges(WebDriver driver) {
    	super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }
    
	//////////////////////////////////////////
	// "Recorded Elements" and their XPaths //
	//////////////////////////////////////////
    
	@FindBy(xpath = "//div[(contains(@id,'DetailChargesListDetailPanel:ChargesLV')) or (@id= 'ChargeHoldsPopup:ChargeHoldsScreen:ChargesLV') or (@id= 'AccountPolicyBalances:AccountPolicyBalancesScreen:policyLoanBalanceLV:ChargesLV')]")
	public WebElement table_ChargesOrChargeHoldsPopup;

	@FindBy(xpath = "//div[contains(@id,'DetailChargesListDetailPanel:InvoiceItemsLV')]")
    public WebElement table_ChargesInvoiceItems;
	
	@FindBy(xpath = "//a[contains(@id,'DetailChargesScreen:editHold')]")
	public WebElement button_EditHolds;

	@FindBy(xpath = "//span[contains(@id,':chargeInformationTab-btnInnerEl')]")
	public WebElement button_ChargeInformation;

	@FindBy(xpath = "//span[contains(@id,'reversalInformationTab-btnInnerEl')]")
	public WebElement button_ReversalInformation;

    @FindBy(xpath = "//span[contains(@id,':InvoiceItemsTab-btnInnerEl')]")
    public WebElement button_InvoiceItems;

    @FindBy(xpath = "//span[contains(@id,':EditDeliveryOptionsButton-btnInnerEl')]")
    public WebElement button_EditDeliveryOptions;


    @FindBy(xpath = "//div[@id=':ChargeReversalInformationDV:chargeReversalName-inputEl']")
    public WebElement label_ChargeReversalName;

    @FindBy(xpath = "//div[@id=':ChargeReversalInformationDV:reversalReason-inputEl']")
    public WebElement label_ReversalReason;

    @FindBy(xpath = "//div[@id=':ChargeReversalInformationDV:reversalDescription-inputEl']")
    public WebElement label_ReversalDescription;


    @FindBy(xpath = "//a[contains(@id,':__crumb__')]")
    public WebElement link_ReturnToCharges;

	//////////////////////////////////////
	// Helper Methods for Above Elements//
	//////////////////////////////////////

    public void clickChargeInformation() {
        clickWhenVisible(button_ChargeInformation);
    }

    public void clickReversalInformation() {
        clickWhenVisible(button_ReversalInformation);
    }

    public void clickInvoiceItems() {
        clickWhenVisible(button_InvoiceItems);
    }

    public void clickEditDeliveryOptions() {
        clickWhenVisible(button_EditDeliveryOptions);
    }

    public void clickReturnToCharges() {
        if (checkIfElementExists("//a[contains(@id, '__crumb__') or contains(@id, 'Popup:__crumb__')]", 500)) {
            clickWhenVisible(link_ReturnToCharges);
        }
    }

	public WebElement getChargesOrChargeHoldsPopupTable() {
        return table_ChargesOrChargeHoldsPopup;
    }

    public WebElement getChargesInvoiceItemsTable() {
        return table_ChargesInvoiceItems;
    }
    
    public void clickEditHolds() {
        clickWhenClickable(button_EditHolds);
    }
    
    public WebElement getChargesOrChargeHoldsPopupTableRow(Date chargeDate, String defaultPayer, TransactionNumber transactionNumber, ChargeCategory chargeType, TransactionType chargeContext, String chargeGroup, ChargeHoldStatus holdStatus, Date holdReleaseDate, String policyNumber, Double chargeAmount, String chargeDescription, Boolean partialCancel, String loanNumber, String usageDescription, String payerAddress, String deliveryOptions) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if (chargeDate != null) {
            columnRowKeyValuePairs.put("Date", DateUtils.dateFormatAsString("MM/dd/yyyy", chargeDate));
        }
        if (defaultPayer != null) {
            columnRowKeyValuePairs.put("Default Payer", defaultPayer);
        }
        if (transactionNumber != null) {
            columnRowKeyValuePairs.put("Transaction #", transactionNumber.getValue());
        }
        if (chargeType != null) {
            columnRowKeyValuePairs.put("Charge Type", chargeType.getValue());
        }
        if (chargeContext != null) {
            columnRowKeyValuePairs.put("Context", chargeContext.getValue());
        }
        if (chargeGroup != null) {
            columnRowKeyValuePairs.put("Charge Group", chargeGroup);
        }
        if (holdStatus != null) {
            columnRowKeyValuePairs.put("Hold Status", holdStatus.getValue());
        }
        if (holdReleaseDate != null) {
            columnRowKeyValuePairs.put("Hold Release Date", DateUtils.dateFormatAsString("MM/dd/yyyy", holdReleaseDate));
        }
        if (policyNumber != null) {
            columnRowKeyValuePairs.put("Policy", policyNumber);
        }
        if (chargeAmount != null) {
            columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(chargeAmount));
        }
        if (chargeDescription != null) {
            columnRowKeyValuePairs.put("Description", chargeDescription);
        }
        if (partialCancel != null) {
            String partialCancelString = "";
            if (partialCancel) {
                partialCancelString = "Yes";
            }
            columnRowKeyValuePairs.put("Partial Cancel", partialCancelString);
        }
        if (loanNumber != null) {
            columnRowKeyValuePairs.put("Loan Number", loanNumber);
        }
        if (usageDescription != null) {
            if (!usageDescription.equals("anyText")) {
                columnRowKeyValuePairs.put("Usage", usageDescription);
            } else {
                columnRowKeyValuePairs.put("Usage", "/div[not(starts-with(.,''))]\"");
            }
        }
        if (payerAddress != null) {
            columnRowKeyValuePairs.put("Payer Address", payerAddress);
        }
        if (deliveryOptions != null) {
            columnRowKeyValuePairs.put("Delivery Options", deliveryOptions);
        }

        return tableUtils.getRowInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs);
    }
    
    public WebElement getChargesOrChargeHoldsInvoiceItemsTableRow(String installmentNumber, Date eventDate, Date statementDate, Date invoiceDueDate, String invoiceNumber, String invoiceItemOwner, String invoiceItemPayer, TransactionType invoiceItemChargeContext, ChargeCategory invoiceItemChargeType, Double invoiceItemAmount, Double invoiceItemPaidAmount) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if (installmentNumber != null) {
            columnRowKeyValuePairs.put("Installment #", installmentNumber);
        }
        if (eventDate != null) {
            columnRowKeyValuePairs.put("Event Date", DateUtils.dateFormatAsString("MM/dd/yyyy", eventDate));
        }
        if (statementDate != null) {
            columnRowKeyValuePairs.put("Statement Date", DateUtils.dateFormatAsString("MM/dd/yyyy", statementDate));
        }
        if (invoiceDueDate != null) {
            columnRowKeyValuePairs.put("Invoice Due Date", DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDueDate));
        }
        if (invoiceNumber != null) {
            columnRowKeyValuePairs.put("Invoice Number", invoiceNumber);
        }
        if (invoiceItemOwner != null) {
            columnRowKeyValuePairs.put("Owner", invoiceItemOwner);
        }
        if (invoiceItemPayer != null) {
            columnRowKeyValuePairs.put("Payer", invoiceItemPayer);
        }
        if (invoiceItemChargeContext != null) {
            columnRowKeyValuePairs.put("Context", invoiceItemChargeContext.getValue());
        }
        if (invoiceItemChargeType != null) {
            columnRowKeyValuePairs.put("Type", invoiceItemChargeType.getValue());
        }
        if (invoiceItemAmount != null) {
            columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(invoiceItemAmount));
        }
        if (invoiceItemPaidAmount != null) {
            columnRowKeyValuePairs.put("Paid Amount", StringsUtils.currencyRepresentationOfNumber(invoiceItemPaidAmount));
        }
        return tableUtils.getRowInTableByColumnsAndValues(table_ChargesInvoiceItems, columnRowKeyValuePairs);
    }
    
    public void setChargesOrChargeHoldsInvoiceItemsTableCheckBox(String installmentNumber, Date eventDate, Date statementDate, Date invoiceDueDate, String invoiceNumber, String invoiceItemOwner, String invoiceItemPayer, TransactionType invoiceItemChargeContext, ChargeCategory invoiceItemChargeType, Double invoiceItemAmount, Double invoiceItemPaidAmount) {
        WebElement paymentRow = getChargesOrChargeHoldsInvoiceItemsTableRow(installmentNumber, eventDate, statementDate, invoiceDueDate, invoiceNumber, invoiceItemOwner, invoiceItemPayer, invoiceItemChargeContext, invoiceItemChargeType, invoiceItemAmount, invoiceItemPaidAmount);
        tableUtils.setCheckboxInTable(table_ChargesInvoiceItems, tableUtils.getRowNumberFromWebElementRow(paymentRow), true);
    }
    
    public String getChargesOrChargeHoldsPopupTableCellValue(String headerColumn, Date chargeDate, String defaultPayer, TransactionNumber transactionNumber, ChargeCategory chargeType, TransactionType chargeContext, String chargeGroup, ChargeHoldStatus holdStatus, Date holdReleaseDate, String policyNumber, Double chargeAmount, String chargeDescription, Boolean partialCancel, String loanNumber, String usageDescription, String payerAddress, String deliveryOptions) {
        WebElement tableRow = getChargesOrChargeHoldsPopupTableRow(chargeDate, defaultPayer, transactionNumber, chargeType, chargeContext, chargeGroup, holdStatus, holdReleaseDate, policyNumber, chargeAmount, chargeDescription, partialCancel, loanNumber, usageDescription, payerAddress, deliveryOptions);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, tableRow, headerColumn);
    }
    
    public boolean waitUntilChargesFromPolicyContextArriveByTransactionDateAndPayer(int secondsToWait, Date transactionDate, TransactionType chargeContext, String payer) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Date", DateUtils.dateFormatAsString("MM/dd/yyyy", transactionDate));
        columnRowKeyValuePairs.put("Context", chargeContext.getValue());
        columnRowKeyValuePairs.put("Default Payer", payer);
        List<WebElement> tableRows = tableUtils.getRowsInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs);
        boolean found = false;
        long secondsRemaining = secondsToWait;
        int delayInterval = 5;
        while (!found && (secondsRemaining > 0)) {
            if (tableRows.size() > 0) {
                found = true;
            } else {
                sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for charges again.
                secondsRemaining -= delayInterval;
                repository.bc.account.BCAccountMenu acctMenuStuff = new repository.bc.account.BCAccountMenu(getDriver());
                acctMenuStuff.clickBCMenuSummary();
                acctMenuStuff.clickBCMenuCharges();
                tableRows = tableUtils.getRowsInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs);
            }
        }
        return found;
    }
    
    public boolean waitUntilChargesFromPolicyContextArrive(int secondsToWait, TransactionType chargeContext) {
        List<WebElement> issuanceRows = tableUtils.getRowsInTableByColumnNameAndValue(table_ChargesOrChargeHoldsPopup, "Context", chargeContext.getValue());
        boolean found = false;
        long secondsRemaining = secondsToWait;
        int delayInterval = 5;
        while (!found && (secondsRemaining > 0)) {
            if (issuanceRows.size() > 0) {
                found = true;
            } else {
                sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for charges again.
                secondsRemaining -= delayInterval;
                repository.bc.account.BCAccountMenu acctMenuStuff = new repository.bc.account.BCAccountMenu(getDriver());
                acctMenuStuff.clickBCMenuSummary();
                acctMenuStuff.clickBCMenuCharges();
                issuanceRows = tableUtils.getRowsInTableByColumnNameAndValue(table_ChargesOrChargeHoldsPopup, "Context", chargeContext.getValue());
            }
        }
        return found;
    }
    
    /**
     * Defaults wait time to 2 minutes
     *
     * @return
     */
    public boolean waitUntilChargesFromPolicyContextArrive(TransactionType chargeContext) {
        return waitUntilChargesFromPolicyContextArrive(120, chargeContext);
    }

    public boolean waitUntilIssuanceChargesArrive(int secondsToWait) {
        return waitUntilChargesFromPolicyContextArrive(secondsToWait, TransactionType.Policy_Issuance);
    }

    /**
     * Defaults wait time to 2 minutes
     *
     * @return
     */
    public boolean waitUntilIssuanceChargesArrive() {
        return waitUntilIssuanceChargesArrive(120);
    }

    public boolean waitUntilPolicyCancellationArrive(int secondsToWait) {
        return waitUntilChargesFromPolicyContextArrive(secondsToWait, TransactionType.Cancellation);
    }

    /**
     * Defaults wait time to 2 minutes
     *
     * @return
     */
    public boolean waitUntilPolicyCancellationArrive() {
        return waitUntilChargesFromPolicyContextArrive(120, TransactionType.Cancellation);
    }
    
    public boolean waitUntilChargesFromTransactionDescriptionArrive(int secondsToWait, String transactionDescription) {
        List<WebElement> tableRows = null;
        try {
            tableRows = tableUtils.getRowsInTableByColumnNameAndValue(table_ChargesOrChargeHoldsPopup, "Description", transactionDescription);
        } catch (Exception e) {
            e.printStackTrace();
            systemOut("There was an error looking for the Description column. There are most likely no charges with a description yet. Starting in to loop...");
        }
        boolean found = false;
        long secondsRemaining = secondsToWait;
        int delayInterval = 5;
        while (!found && (secondsRemaining > 0)) {
            if (tableRows != null) {
                if (tableRows.size() > 0) {
                    found = true;
                } else {
                    sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for charges again.
                    secondsRemaining -= delayInterval;
                    repository.bc.account.BCAccountMenu acctMenuStuff = new repository.bc.account.BCAccountMenu(getDriver());
                    acctMenuStuff.clickBCMenuSummary();
                    acctMenuStuff.clickBCMenuCharges();
                    tableRows = tableUtils.getRowsInTableByColumnNameAndValue(table_ChargesOrChargeHoldsPopup, "Description", transactionDescription);
                }
            } else {
                sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for charges again.
                secondsRemaining -= delayInterval;
                repository.bc.account.BCAccountMenu acctMenuStuff = new repository.bc.account.BCAccountMenu(getDriver());
                acctMenuStuff.clickBCMenuSummary();
                acctMenuStuff.clickBCMenuCharges();

                try {
                	tableRows = tableUtils.getRowsInTableByColumnNameAndValue(table_ChargesOrChargeHoldsPopup, "Description", transactionDescription);
                } catch (Exception e) {
                    systemOut("There was an error looking for the Description column. There are most likely no charges with a description still. Trying again...");
                }
            }
        }
        return found;
    }
    
    public double getIssuanceChargeTotals() {
        List<WebElement> issuanceRowTotalDivs = tableUtils.getRowsInTableByColumnNameAndValue(table_ChargesOrChargeHoldsPopup, "Context", TransactionType.Issuance.getValue());
        double totalValue = 0;
        for (WebElement div : issuanceRowTotalDivs) {
            totalValue = totalValue + NumberUtils.getCurrencyValueFromElement(div);
        }
        return totalValue;
    }
    
    public boolean verifyChargesMatchPayers(GeneratePolicy policyObject) {
        ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>();
        switch (policyObject.productType) {
            case Businessowners:
                locationList = policyObject.busOwnLine.locationList;
                break;
            case CPP:
                locationList = policyObject.commercialPackage.locationList;
                break;
            case PersonalUmbrella:
                break;
            case Squire:
                locationList = policyObject.squire.propertyAndLiability.locationList;
                break;
            case StandardIM:
                break;
            case Membership:
                break;
            case StandardFire:
                locationList = policyObject.standardFire.getLocationList();
                break;
            case StandardLiability:
                locationList = policyObject.standardLiability.getLocationList();
                break;
            default:
                break;
        }

        boolean found = false;
        for (PolicyLocation locations : locationList) {
            if (!locations.getBuildingList().isEmpty()) { // For BOP Location Building List
                for (PolicyLocationBuilding locBldg : locations.getBuildingList()) {
                    if (locBldg.getAdditionalInterestList().size() > 0) {
                        if (locBldg.getAdditionalInterestList().get(0).getAdditionalInterestBilling() == AdditionalInterestBilling.Bill_Lienholder) {
                            String defaultPayer = locBldg.getAdditionalInterestList().get(0).getLienholderNumber();
                            String chargeAmount = StringsUtils.currencyRepresentationOfNumber(locBldg.getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount());
                        	System.out.println("lien number is " + defaultPayer);
                            System.out.println("lien premium is " + chargeAmount);
                            HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
                            columnRowKeyValuePairs.put("Default Payer", defaultPayer);
                            columnRowKeyValuePairs.put("Amount", chargeAmount);
                            columnRowKeyValuePairs.put("Context", TransactionType.Policy_Issuance.getValue());
                            List<WebElement> rowMatch = tableUtils.getRowsInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs);
                            if (rowMatch.size() > 0) {
                                found = true;
                            } else {
                                found = false;
                                break;
                            }
                        }
                    } else {
                    	String defaultPayer = policyObject.accountNumber;
                        String chargeAmount = StringsUtils.currencyRepresentationOfNumber(new GuidewireHelpers(getDriver()).getPolicyPremium(policyObject).getInsuredPremium());
                    	System.out.println("Insured account number is " + defaultPayer);
                        System.out.println("Insured premium is " + chargeAmount);
                        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
                        columnRowKeyValuePairs.put("Default Payer", defaultPayer);
                        columnRowKeyValuePairs.put("Amount", chargeAmount);
                        columnRowKeyValuePairs.put("Context", TransactionType.Policy_Issuance.getValue());
                        List<WebElement> rowMatch = tableUtils.getRowsInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs);
                        if (rowMatch.size() > 0) {
                            found = true;
                        } else {
                            found = false;
                            break;
                        }
                    }
                }
            } else { // For PL Location Building List
                for (PLPolicyLocationProperty locationProperty : locations.getPropertyList()) {
                    if (locationProperty.getBuildingAdditionalInterest().size() > 0) {
                        if (locationProperty.getBuildingAdditionalInterest().get(0).getAdditionalInterestBilling() == AdditionalInterestBilling.Bill_Lienholder) {
                        	String defaultPayer = locationProperty.getBuildingAdditionalInterest().get(0).getLienholderNumber();
                            String chargeAmount = StringsUtils.currencyRepresentationOfNumber(locationProperty.getBuildingAdditionalInterest().get(0).getAdditionalInterestPremiumAmount());
                        	System.out.println("lien number is " + defaultPayer);
                            System.out.println("lien premium is " + chargeAmount);
                            HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
                            columnRowKeyValuePairs.put("Default Payer", defaultPayer);
                            columnRowKeyValuePairs.put("Amount", chargeAmount);
                            columnRowKeyValuePairs.put("Context", TransactionType.Policy_Issuance.getValue());
                            List<WebElement> rowMatch = tableUtils.getRowsInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs);
                            if (rowMatch.size() > 0) {
                                found = true;
                            } else {
                                found = false;
                                break;
                            }
                        }
                    } else {
                    	String defaultPayer = policyObject.accountNumber;
                        String chargeAmount = StringsUtils.currencyRepresentationOfNumber(new GuidewireHelpers(getDriver()).getPolicyPremium(policyObject).getInsuredPremium());
                    	System.out.println("Insured account number is " + defaultPayer);
                        System.out.println("Insured premium is " + chargeAmount);
                        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
                        columnRowKeyValuePairs.put("Default Payer", defaultPayer);
                        columnRowKeyValuePairs.put("Amount", chargeAmount);
                        columnRowKeyValuePairs.put("Context", TransactionType.Policy_Issuance.getValue());
                        List<WebElement> rowMatch = tableUtils.getRowsInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs);
                        if (rowMatch.size() > 0) {
                            found = true;
                        } else {
                            found = false;
                            break;
                        }
                    }
                }
            }

        }
        return found;
    }
    
    public void clickDefaultPayerRow(String payerNumber) {
    	tableUtils.clickRowInTableByRowNumber(table_ChargesOrChargeHoldsPopup, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_ChargesOrChargeHoldsPopup, "Default Payer", payerNumber)));
    }

    public void clickDefaultPayerNumberHyperlink(String payerNumber) {
    	tableUtils.clickLinkInSpecficRowInTable(table_ChargesOrChargeHoldsPopup, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_ChargesOrChargeHoldsPopup, "Default Payer", payerNumber)));
    }

    public boolean checkIfDefaultPayerLinkIsActive() {
        WebElement defaultPayerLinkElement = tableUtils.getCellWebElementInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, 1, "Default Payer");
        return new GuidewireHelpers(getDriver()).checkIfLinkIsActive(defaultPayerLinkElement);
    }

    public String getLienHolderNumberWithLoanNumber(String loanNumber) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Loan Number", loanNumber);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs)), "Default Payer");
    }

    public String getPolicyNumberWithPayerCharge(String defaultPayer) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Default Payer", defaultPayer);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs)), "Policy");
    }

    public String getInvoiceNumber(String payerNumber) {
        
        clickDefaultPayerRow(payerNumber);
        
        return tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesInvoiceItems, tableUtils.getRowInTableByColumnNameAndValue(table_ChargesInvoiceItems, "Payer", payerNumber), "Invoice Number");
    }
    
    public boolean verifyChargeAsPartialCancel(Date chargeDate, String defaultPayer, TransactionType chargeContext, String chargeGroup, String chargeDescription, String loanNumber) {
        try {
            if (loanNumber != null) {
                getChargesOrChargeHoldsPopupTableRow(chargeDate, defaultPayer, null, null, chargeContext, chargeGroup, null, null, null, null, chargeDescription, true, loanNumber, null, null, null);
            } else {
                getChargesOrChargeHoldsPopupTableRow(chargeDate, defaultPayer, null, null, chargeContext, chargeGroup, null, null, null, null, chargeDescription, true, null, null, null, null);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public double getChargeAmount(String payerNumber, String description) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Default Payer", payerNumber);
        columnRowKeyValuePairs.put("Description", description);
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs)), "Amount"));
    }

    public double getChargeAmount(String payerNumber, TransactionNumber transactionNumber) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Default Payer", payerNumber);
        columnRowKeyValuePairs.put("Transaction #", transactionNumber.getValue());
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs)), "Amount"));
    }

    public String getPolicyPeriod(String payerNumber, String description) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Default Payer", payerNumber);
        columnRowKeyValuePairs.put("Description", description);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs)), "Policy");
    }

    public String getChargeGroup(String payerNumber, String description) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Default Payer", payerNumber);
        columnRowKeyValuePairs.put("Description", description);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs)), "Charge Group");
    }

    public boolean checkIfContextLinkIsActive() {
        WebElement defaultPayerLinkElement = tableUtils.getCellWebElementInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, 1, "Context");
        return new GuidewireHelpers(getDriver()).checkIfLinkIsActive(defaultPayerLinkElement);
    }
    
    public List<Double> getListOfChargesPerContext(TransactionType context) {
        List<Double> chargesList = new ArrayList<Double>();
        List<WebElement> tableRows = tableUtils.getRowsInTableByColumnNameAndValue(table_ChargesOrChargeHoldsPopup, "Context", context.getValue());
        for (WebElement row : tableRows) {
            double rowCharge = NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, row, "Amount"));
            chargesList.add(rowCharge);
        }
        while (tableUtils.incrementTablePageNumber(table_ChargesOrChargeHoldsPopup)) {
            tableRows = tableUtils.getRowsInTableByColumnNameAndValue(table_ChargesOrChargeHoldsPopup, "Context", context.getValue());
            if (tableRows.size() > 0) {
                for (WebElement row : tableRows) {
                    double rowCharge = NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, row, "Amount"));
                    chargesList.add(rowCharge);
                }
            }
        }

        int maxPage = tableUtils.getNumberOfTablePages(table_ChargesOrChargeHoldsPopup);
        if (maxPage > 1) {
            tableUtils.setTablePageNumber(table_ChargesOrChargeHoldsPopup, 1);
        }
        return chargesList;
    }
    
    public List<Double> getListOfPaidAmountsInInvoiceItemsTable() {
        List<Double> paidAmount = new ArrayList<Double>();
        List<WebElement> invoiceItemsTableRows = tableUtils.getAllTableRows(table_ChargesInvoiceItems);
        for (WebElement row : invoiceItemsTableRows) {
            double amount = NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesInvoiceItems, tableUtils.getRowNumberFromWebElementRow(row), "Paid Amount"));
            paidAmount.add(amount);
        }
        return paidAmount;
    }
    
    public boolean verifyChargePayersWithInvoiceItemsPayers() {
    	String defaultPayer, invoiceItemsPayer;
        boolean payersAreSame = true;
        int chargesTableRows, invoiceitemsTableRowCount, currentPage = 1;
        chargesTableRows = tableUtils.getRowCount(table_ChargesOrChargeHoldsPopup);
        for (int i = 1; i < chargesTableRows; i++) {
            // get the Default Payer for each row in Charges table
            defaultPayer = tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, i, "Default Payer");
            // click the row to make the Invoice Items table for that row display
            tableUtils.clickRowInTableByRowNumber(table_ChargesOrChargeHoldsPopup, i);
            
            invoiceitemsTableRowCount = tableUtils.getRowCount(table_ChargesInvoiceItems);
            // get the Payer of each row in Invoice Items table, compare them with Default Payer in Charges table,
            for (int k = 1; k < invoiceitemsTableRowCount; k++) {
                invoiceItemsPayer = tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesInvoiceItems, k, "Payer");
                // for lienholder, the Default Payer is 13 digits while the payer in Invoice table is 10 digits
                if (!invoiceItemsPayer.equals(defaultPayer)) {
                    System.out.println("The Default Payer in row " + (i) + " in Charges table is not equal to the Payer in row " + (k) + " in Invoice Items table in page " + currentPage);
                    payersAreSame = false;
                }
            }
        }
        while (tableUtils.incrementTablePageNumber(table_ChargesOrChargeHoldsPopup)) {
            chargesTableRows = tableUtils.getRowCount(table_ChargesOrChargeHoldsPopup);
            for (int i = 1; i < chargesTableRows; i++) {
                // get the Default Payer for each row in Charges table
                defaultPayer = tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, i, "Default Payer");
                // click the row to make the Invoice Items table for that row display
                tableUtils.clickRowInTableByRowNumber(table_ChargesOrChargeHoldsPopup, i);
                
                invoiceitemsTableRowCount = tableUtils.getRowCount(table_ChargesInvoiceItems);
                // get the Payer of each row in Invoice Items table, compare them with Default Payer in Charges table,
                for (int k = 1; k < invoiceitemsTableRowCount; k++) {
                    invoiceItemsPayer = tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesInvoiceItems, k, "Payer");
                    // for lienholder, the Default Payer is 13 digits while the payer in Invoice table is 10 digits
                    if (!invoiceItemsPayer.equals(defaultPayer)) {
                        System.out.println("The Default Payer in row " + (i) + " in Charges table is not equal to the Payer in row " + (k) + " in Invoice Items table in page " + currentPage);
                        payersAreSame = false;
                    }
                }
            }
            currentPage = tableUtils.getCurrentTablePageNumber(table_ChargesOrChargeHoldsPopup);
        }
        return payersAreSame;
    }
    
    public void clickPolicyNumberInChargesTableRow(Date chargeDate, String defaultPayer, TransactionNumber transactionNumber, ChargeCategory chargeType, TransactionType chargeContext, String chargeGroup, ChargeHoldStatus holdStatus, Date holdReleaseDate, String policyNumber, Double chargeAmount, String chargeDescription, Boolean partialCancel, String loanNumber, String usageDescription, String payerAddress, String deliveryOptions) {
        WebElement tableRow = getChargesOrChargeHoldsPopupTableRow(chargeDate, defaultPayer, transactionNumber, chargeType, chargeContext, chargeGroup, holdStatus, holdReleaseDate, policyNumber, chargeAmount, chargeDescription, partialCancel, loanNumber, usageDescription, payerAddress, deliveryOptions);
        tableUtils.clickLinkInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, tableUtils.getRowNumberFromWebElementRow(tableRow), "Policy");
    }
    
    /**
     * Defaults wait time to 2 minutes
     *
     * @return
     */
    public boolean waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(Date transactionDate, TransactionType chargeContext, String policyNumber) {
        return waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, transactionDate, chargeContext, policyNumber);
    }
    
    public boolean waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(int secondsToWait, Date transactionDate, TransactionType chargeContext, String policyNumber) {
        sortChargeTableByRecentDate();
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if (transactionDate != null) {
            columnRowKeyValuePairs.put("Date", DateUtils.dateFormatAsString("MM/dd/yyyy", transactionDate));
        }
        if (chargeContext != null) {
            columnRowKeyValuePairs.put("Context", chargeContext.getValue());
        }
        if (policyNumber != null) {
            columnRowKeyValuePairs.put("Policy", policyNumber);
        }
        List<WebElement> tableRows = tableUtils.getRowsInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs);
        boolean found = false;
        long secondsRemaining = secondsToWait;
        int delayInterval = 5;
        while (!found && (secondsRemaining > 0)) {
            if (tableRows.size() > 0) {
                found = true;
            } else {
                sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for charges again.
                secondsRemaining -= delayInterval;
                repository.bc.account.BCAccountMenu acctMenuStuff = new BCAccountMenu(getDriver());
                acctMenuStuff.clickBCMenuSummary();
                acctMenuStuff.clickBCMenuCharges();
                tableRows = tableUtils.getRowsInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs);
            }
        }
        return found;
    }
    
    public List<Double> calculateCreditDistribution(PaymentPlanType paymentPlanType, double creditAmount, Date policyChangeDate, Date policyExpirationDate) {
        List<Double> creditList = new ArrayList<Double>();
        double annualPremiumForChange = AccountInvoices.policyChangeFutureInvoiceAmount(creditAmount, policyChangeDate, policyExpirationDate);
        double additionalPremiumEachInvoice = NumberUtils.round((annualPremiumForChange / paymentPlanType.getNumberOfPaymentPeriods()), 2);
        int invoiceRows = tableUtils.getRowCount(table_ChargesInvoiceItems);
        for (int i = invoiceRows - 1; i >= 0; i--) {
            InvoiceStatus invoiceStatus = InvoiceStatus.valueOf(tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesInvoiceItems, i, "Statement Date"));
            if (invoiceStatus.equals(InvoiceStatus.Planned)) {
                creditList.add(0, additionalPremiumEachInvoice);
                annualPremiumForChange = NumberUtils.round((annualPremiumForChange - additionalPremiumEachInvoice), 2);
            } else if (invoiceStatus.equals(InvoiceStatus.Billed)) {
                creditList.add(0, annualPremiumForChange);
                break;
            } else {// for Due
                creditList.set(0, annualPremiumForChange + creditList.get(0));
                break;
            }
        }
        return creditList;
    }
    
    public boolean checkIfPolicyLinkIsActive() {
        WebElement policyNumberLinkElement = tableUtils.getCellWebElementInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, 1, "Policy");
        return new GuidewireHelpers(getDriver()).checkIfLinkIsActive(policyNumberLinkElement);
    }
    
    public boolean verifyCharges(Date chargeDate,ChargeCategory chargeType, String policyNumber, Double chargeAmount,String defaultPayer, TransactionType chargeContext, String chargeGroup, String chargeDescription, String loanNumber) {
        sortChargeTableByRecentDate();
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if (chargeDate != null) {
            columnRowKeyValuePairs.put("Date", DateUtils.dateFormatAsString("MM/dd/yyyy", chargeDate));
        }if (chargeType != null) {
            columnRowKeyValuePairs.put("Charge Type", chargeType.getValue());
        }
        if (policyNumber != null) {
            columnRowKeyValuePairs.put("Policy", policyNumber);
        }
        if (chargeAmount != null) {
            columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(chargeAmount));
        }
        if (defaultPayer != null) {
            columnRowKeyValuePairs.put("Default Payer", defaultPayer);
        }
        if (chargeContext != null) {
            columnRowKeyValuePairs.put("Context", chargeContext.getValue());
        }
        if (chargeGroup != null) {
            columnRowKeyValuePairs.put("Charge Group", chargeGroup);
        }
        if (chargeDescription != null) {
            columnRowKeyValuePairs.put("Description", chargeDescription);
        }
        if (loanNumber != null) {
            columnRowKeyValuePairs.put("Loan Number", loanNumber);
        }
        return tableUtils.getRowsInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs).size() > 0;
    }

    public void clickCarryOverCharge(String defaultPayer, TransactionNumber transaction, TransactionType context, String policyNumber) {
        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        if(transaction != null) {
            columnRowKeyValuePairs.put("Default Payer", defaultPayer);
        }
        if(transaction != null) {
            columnRowKeyValuePairs.put("Transaction #", transaction.getValue());
        }
        if(context!=null){
        columnRowKeyValuePairs.put("Context", context.getValue());
        }
        if(context!=null) {
            columnRowKeyValuePairs.put("Policy", policyNumber);
        }
        tableUtils.clickLinkInSpecficRowInTable(table_ChargesOrChargeHoldsPopup, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnsAndValues(table_ChargesOrChargeHoldsPopup, columnRowKeyValuePairs)));
        clickWhenClickable(find(By.linkText("Create Carry Over Charge")));
    }
    
    public void clickCarryOverChargeAndCreateCharge(String defaultPayer, TransactionNumber transaction, TransactionType context, String policyNumber, TrueFalse createInsuredCarryOverTrueOrFalse, double carryOverAmount) {
        clickCarryOverCharge(defaultPayer, transaction, context, policyNumber);
        repository.bc.account.CarryOverCharge carryoverCharge = new CarryOverCharge(getDriver());
        carryoverCharge.createCarryOverChargeWithRandomOLIEPolicyNumber(createInsuredCarryOverTrueOrFalse.getValue(), carryOverAmount);
    }
    
    public boolean verifyCarryOverChargeIfCreatedOrNot(Date chargeDate, String defaultPayer, String policyNumber, double carryOverAmount) {
        try {
            getChargesOrChargeHoldsPopupTableRow(chargeDate, defaultPayer, TransactionNumber.CarryOver_Charge_Charged, null, TransactionType.General, null, null, null, policyNumber, carryOverAmount, null, null, null, null, null, null);
            System.out.println("CarryOver charge found");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void clickCarryOverChargeAndCreateChargeAndVerifyIfCreatedOrNot(String defaultPayer, TransactionNumber transaction, TransactionType context, String policyNumber, TrueFalse createInsuredCarryOverTrueOrFalse, double carryOverAmount) {
        clickCarryOverChargeAndCreateCharge(defaultPayer, transaction, context, policyNumber, createInsuredCarryOverTrueOrFalse, carryOverAmount);
        if (defaultPayer.substring(0, 1).equals("9") && createInsuredCarryOverTrueOrFalse.getValue()) {
            defaultPayer = policyNumber.substring(3, 9).concat("-001");
        }
        Assert.assertTrue(verifyCarryOverChargeIfCreatedOrNot(DateUtils.getCenterDate(getDriver(), ApplicationOrCenter.BillingCenter), defaultPayer, policyNumber, carryOverAmount), "CarryOver charge not created");
    }
    
    public void clickChargeRow(Date chargeDate, String defaultPayer, TransactionNumber transactionNumber, ChargeCategory chargeType, TransactionType chargeContext, String chargeGroup, ChargeHoldStatus holdStatus, Date holdReleaseDate, String policyNumber, Double chargeAmount, String chargeDescription, Boolean partialCancel, String loanNumber, String usageDescription, String payerAddress, String deliveryOptions) {
        try {
            clickWhenClickable(getChargesOrChargeHoldsPopupTableRow(chargeDate, defaultPayer, transactionNumber, chargeType, chargeContext, chargeGroup, holdStatus, holdReleaseDate, policyNumber, chargeAmount, chargeDescription, partialCancel, loanNumber, usageDescription, payerAddress, deliveryOptions));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("The row in the Charges table matching the criteria passed in failed. Please verify criteria and try again.");
        }
    }
    
    public String clickChargeContextCenterConnectHyperlink(Date chargeDate, String defaultPayer, String policyNumber, String usageDescription) {
        WebElement chargeTableRow = null;
        try {
            chargeTableRow = getChargesOrChargeHoldsPopupTableRow(chargeDate, defaultPayer, null, null, null, null, null, null, policyNumber, null, null, null, null, usageDescription, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("The row in the Charges table matching the criteria passed in failed. Please verify criteria and try again.");
        }
        tableUtils.clickLinkInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, tableUtils.getRowNumberFromWebElementRow(chargeTableRow), "Context");

        

        WebDriver driver = getDriver();
        String mainBCWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
            if (driver.getTitle().contains("Guidewire PolicyCenter (Service Account)")) {
                systemOut("Switching to Policy Center Window");
                break;
            }
        }
        return mainBCWindow;
    }
    
    public double getAmountForChargeGroup(String chargeGroup) {
		waitUntilElementIsVisible(table_ChargesOrChargeHoldsPopup);
		int row = tableUtils.getRowNumberInTableByText(table_ChargesOrChargeHoldsPopup, chargeGroup);
		String chargeGroupAmount = tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, row, "Amount");
		return NumberUtils.getCurrencyValueFromElement(chargeGroupAmount);
	}

	public List<String> getChargeGroupForAmount(double amount) {
		List<String> returnList = new ArrayList<String>();
		waitUntilElementIsVisible(table_ChargesOrChargeHoldsPopup);
		List<WebElement> chargegrouprows = tableUtils.getRowsInTableByColumnNameAndValue(table_ChargesOrChargeHoldsPopup, "Amount", StringsUtils.currencyRepresentationOfNumber(amount));
		for (WebElement chargeGroup : chargegrouprows) {
			returnList.add(tableUtils.getCellTextInTableByRowAndColumnName(table_ChargesOrChargeHoldsPopup, tableUtils.getRowNumberFromWebElementRow(chargeGroup), "Charge Group"));
		}
		return returnList;
	}

	public void sortChargeTableByRecentDate(){
        new TableUtils(getDriver()).sortByHeaderColumn(table_ChargesOrChargeHoldsPopup,"Default Payer");
        new TableUtils(getDriver()).sortByHeaderColumn(table_ChargesOrChargeHoldsPopup,"Date");
        new TableUtils(getDriver()).sortByHeaderColumn(table_ChargesOrChargeHoldsPopup,"Date");
    }
}
