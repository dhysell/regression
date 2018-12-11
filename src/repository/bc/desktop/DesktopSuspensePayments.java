package repository.bc.desktop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.Currency;
import repository.gw.enums.SuspensePaymentStatus;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;

public class DesktopSuspensePayments extends BasePage {

	private TableUtils tableUtils;
	private WebDriver driver;
	public DesktopSuspensePayments(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}

	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	public Guidewire8Select select_DesktopSuspensePaymentsStatuses() throws Exception {
		return new Guidewire8Select(driver, "//table[contains(@id, ':DesktopSuspensePaymentsScreen:SuspensePaymentStatusFilter-triggerWrap')]");
	}		
	@FindBy(xpath = "//div[contains(@id,':DesktopSuspensePaymentsScreen:1')]")
    public WebElement table_SuspensePayments;


	@FindBy(xpath = "//a[@id='DesktopSuspensePayments:DesktopSuspensePaymentsScreen:createDisbursement']")
	public WebElement button_CreateDisbursement;	

	@FindBy(xpath = "//a[contains(@id, ':NewPayment')]")
	public WebElement button_New;
	
	@FindBy(xpath = "//a[contains(@id, ':reversesuspensepayments')]")
	public WebElement button_Reverse;

	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------

	public void setSuspensePaymentStatus(SuspensePaymentStatus statusToSelect) {
		try {
			select_DesktopSuspensePaymentsStatuses().selectByVisibleText(statusToSelect.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void clickCreateDisbursement() {
		clickWhenVisible(button_CreateDisbursement);
		
	}	

	public void clickReverse() {
		clickWhenVisible(button_Reverse);
		
	}
	
	public void clickNew() {
		clickWhenVisible(button_New);
		
	}
	public void clickApplyButton() {
		super.clickUpdate();
		
	}
	public void clickOK() {
		super.clickOK();
		
	}

	 public WebElement getSuspensePaymentTableRow(Date paymentDate, String referenceNum, SuspensePaymentStatus status, String transactionsNum, String applyTo, String policyNumber, String loanNumber, Currency currency, String appliedBy, Double amount, String notes) {
	        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
	        
	        if (paymentDate != null) {
	            columnRowKeyValuePairs.put("Payment Date", DateUtils.dateFormatAsString("MM/dd/yyyy", paymentDate));
	        }
	        
	        if (referenceNum != null) {
	            columnRowKeyValuePairs.put("Reference Number", referenceNum);
	        }
	        if (status != null) {
	            columnRowKeyValuePairs.put("Status", status.getValue());
	        }
	        if (transactionsNum != null) {
	            columnRowKeyValuePairs.put("Transactions #", transactionsNum);
	        }
	        if (applyTo != null) {
	            columnRowKeyValuePairs.put("Apply To", applyTo);
	        }
	        if (policyNumber != null) {
	            columnRowKeyValuePairs.put("Policy #", policyNumber);
	        }
	        if (loanNumber != null) {
	            columnRowKeyValuePairs.put("Loan #", loanNumber);
	        }
	        if (currency != null) {
	            columnRowKeyValuePairs.put("Currency", currency.getValue());
	        }
	        if (appliedBy != null) {
	            columnRowKeyValuePairs.put("Applied By", appliedBy);
	        }
	        if (amount != null) {
	            columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
	        }
	        if (notes != null) {
	            columnRowKeyValuePairs.put("Notes", notes);
	        }
	        return tableUtils.getRowInTableByColumnsAndValues(table_SuspensePayments, columnRowKeyValuePairs);
	    }

	 public WebElement getRecentlyMadeSuspensePaymentTableRow(Date paymentDate, String referenceNum, SuspensePaymentStatus status, String transactionsNum, String applyTo, String policyNumber, String loanNumber, Currency currency, String appliedBy, Double amount, String notes) {
	        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
			new TableUtils(getDriver()).sortByHeaderColumn(table_SuspensePayments,"Status");
			new TableUtils(getDriver()).sortByHeaderColumn(table_SuspensePayments,"Payment Date");
			new TableUtils(getDriver()).sortByHeaderColumn(table_SuspensePayments,"Payment Date");

	        if (paymentDate != null) {
	            columnRowKeyValuePairs.put("Payment Date", DateUtils.dateFormatAsString("MM/dd/yyyy", paymentDate));
	        }

	        if (referenceNum != null) {
	            columnRowKeyValuePairs.put("Reference Number", referenceNum);
	        }
	        if (status != null) {
	            columnRowKeyValuePairs.put("Status", status.getValue());
	        }
	        if (transactionsNum != null) {
	            columnRowKeyValuePairs.put("Transactions #", transactionsNum);
	        }
	        if (applyTo != null) {
	            columnRowKeyValuePairs.put("Apply To", applyTo);
	        }
	        if (policyNumber != null) {
	            columnRowKeyValuePairs.put("Policy #", policyNumber);
	        }
	        if (loanNumber != null) {
	            columnRowKeyValuePairs.put("Loan #", loanNumber);
	        }
	        if (currency != null) {
	            columnRowKeyValuePairs.put("Currency", currency.getValue());
	        }
	        if (appliedBy != null) {
	            columnRowKeyValuePairs.put("Applied By", appliedBy);
	        }
	        if (amount != null) {
	            columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
	        }
	        if (notes != null) {
	            columnRowKeyValuePairs.put("Notes", notes);
	        }
	        return tableUtils.getRowInTableByColumnsAndValues(table_SuspensePayments, columnRowKeyValuePairs);
	    }

	 public boolean verifySuspensePayment(Date paymentDate, String referenceNum, SuspensePaymentStatus status, String transactionsNum, String applyTo, String policyNumber, String loanNumber, Currency currency, String appliedBy, Double amount, String notes, String headerColumnName) {
		if(headerColumnName != null)
			sortBySuspensePaymentHeader(headerColumnName);
		 boolean found = false;
			try {
				getSuspensePaymentTableRow(paymentDate, referenceNum, status, transactionsNum, applyTo, policyNumber, loanNumber, currency, appliedBy, amount,  notes);
				found = true;
			} catch (Exception e) {
				found = false;
			}
			return found;
		}

	 public boolean verifyRecentlyMadeSuspensePayment(Date paymentDate, String referenceNum, SuspensePaymentStatus status, String transactionsNum, String applyTo, String policyNumber, String loanNumber, Currency currency, String appliedBy, Double amount, String notes, String headerColumnName) {
		if(headerColumnName != null)
			sortBySuspensePaymentHeader(headerColumnName);
		 boolean found = false;
			try {
				getRecentlyMadeSuspensePaymentTableRow(paymentDate, referenceNum, status, transactionsNum, applyTo, policyNumber, loanNumber, currency, appliedBy, amount,  notes);
				found = true;
			} catch (Exception e) {
				found = false;
			}
			return found;
		}

	 public void sortBySuspensePaymentHeader (String headerColumnName) {
		 //double click the header to move most recent item on top
		 tableUtils.sortByHeaderColumn(table_SuspensePayments, headerColumnName);
		 
		 tableUtils.sortByHeaderColumn(table_SuspensePayments, headerColumnName);
	 }
	 
	 public void clickEditInRow(Date paymentDate, String referenceNum, SuspensePaymentStatus status, String transactionsNum, String applyTo, String policyNumber, String loanNumber, Currency currency, String appliedBy, Double amount, String notes) {
		WebElement theRow = getSuspensePaymentTableRow(paymentDate, referenceNum, status, transactionsNum, applyTo, policyNumber, loanNumber, currency, appliedBy, amount,  notes);
		 int rowNumber = tableUtils.getRowNumberFromWebElementRow(theRow);
		 table_SuspensePayments.findElement(By.xpath(".//a[contains(@id,'" + (rowNumber - 1) + ":EditPayment')]")).click();
		waitForPageLoad();
		}
	 
	 public void clickApplyInRow(Date paymentDate, String referenceNum, SuspensePaymentStatus status, String transactionsNum, String applyTo, String policyNumber, String loanNumber, Currency currency, String appliedBy, Double amount, String notes) {
			WebElement theRow = getSuspensePaymentTableRow(paymentDate, referenceNum, status, transactionsNum, applyTo, policyNumber, loanNumber, currency, appliedBy, amount,  notes);
			 int rowNumber = tableUtils.getRowNumberFromWebElementRow(theRow);
			 table_SuspensePayments.findElement(By.xpath(".//a[contains(@id,'" + (rowNumber - 1) + ":ApplyPayment')]")).click();
			 
	}
	 public void clickApplyToLinkInRow(Date paymentDate, String referenceNum, SuspensePaymentStatus status, String transactionsNum, String policyNumber, String loanNumber, Currency currency, String appliedBy, Double amount, String notes) {
			WebElement theRow = getSuspensePaymentTableRow(paymentDate, referenceNum, status, transactionsNum, null, policyNumber, loanNumber, currency, appliedBy, amount,  notes);
			 int rowNumber = tableUtils.getRowNumberFromWebElementRow(theRow);
			 table_SuspensePayments.findElement(By.xpath(".//a[contains(@id,'" + (rowNumber - 1) + ":ApplyTo')]")).click();
			 
	}
	 public void setCheckBox (Date paymentDate, String referenceNum, SuspensePaymentStatus status, String transactionsNum, String applyTo, String policyNumber, String loanNumber, Currency currency, String appliedBy, Double amount, String notes, boolean setCheckboxTrueFalse) {
			WebElement theRow = getSuspensePaymentTableRow(paymentDate, referenceNum, status, transactionsNum, applyTo, policyNumber, loanNumber, currency, appliedBy, amount,  notes);
			tableUtils.setCheckboxInTable(table_SuspensePayments, tableUtils.getRowNumberFromWebElementRow(theRow), setCheckboxTrueFalse);		
	}		
	 public void reversePayment(Date paymentDate, String referenceNum, SuspensePaymentStatus status, String transactionsNum, String applyTo, String policyNumber, String loanNumber, Currency currency, String appliedBy, Double amount, String notes, boolean setCheckboxTrueFalse){
		 setCheckBox(paymentDate, referenceNum, status, transactionsNum, applyTo, policyNumber, loanNumber, currency, appliedBy, amount, notes, setCheckboxTrueFalse);
		 
		 clickReverse();
		 
		 clickOK();
		 
	 }
	 public void goToLastPage(){
		 try{
			 table_SuspensePayments.findElement(By.xpath(".//a[contains(@data-qtip, 'Last Page')]")).click();;
			 
		 }catch (Exception e) {
			// TODO: handle exception
		}
	 }
	 public void goToNextPage(){
		 try{
			 table_SuspensePayments.findElement(By.xpath(".//span[contains(@class, '-page-next')]")).click();;
			 
		 }catch (Exception e) {
			// TODO: handle exception
		}
	 }
	 public int getNumberPages(){
		 int numberPages =1;
		 try{
			 numberPages = tableUtils.getNumberOfTablePages(table_SuspensePayments);
		 }catch (Exception e) {
			// TODO: handle exception
		}
		 return numberPages;
	 }

	 public String getTransactionNumber(Date paymentDate, String referenceNum, SuspensePaymentStatus status, String transactionsNum, String applyTo, String policyNumber, String loanNumber, Currency currency, String appliedBy, Double amount, String notes){
		refreshPage();
		return new TableUtils(driver).getCellTextInTableByRowAndColumnName(table_SuspensePayments, getSuspensePaymentTableRow(paymentDate,referenceNum,status,transactionsNum,applyTo,policyNumber,loanNumber,currency,appliedBy,amount,notes), "Transactions #");

	 }
}
