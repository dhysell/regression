package repository.gw.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ReportPayloadRetrieval;
import repository.gw.enums.ReportPayloadRetrieval.Direction;
import repository.gw.enums.ReportPayloadRetrieval.ReportType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;
import services.verisk.iso.ISO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminReportPayloadRetrieval extends BasePage {
    private WebDriver driver;

    public AdminReportPayloadRetrieval(WebDriver driver) {
    	super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // //////////
    // Elements//
    // //////////

    @FindBy(xpath = "//a[contains(@id, 'ReportLogViewer_Ext:ProductModelViewScreen:QueryAddButton')]")
    private WebElement button_AddFilter;
    
    @FindBy(xpath = "//input[@id='ReportLogViewer_Ext:ProductModelViewScreen:dateInput-inputEl']")
    private WebElement editbox_ReportDate;
    
    @FindBy(xpath = "//input[@id='ReportLogViewer_Ext:ProductModelViewScreen:JobNumberInput-inputEl']")
    private WebElement editbox_JobNumber;
    
    @FindBy(xpath = "//textarea[@id='ReportLogViewer_Ext:ProductModelViewScreen:fasdf-inputEl']")
    private WebElement textArea_Payload;
    
    @FindBy(xpath = "//div[@id='ReportLogViewer_Ext:ProductModelViewScreen:1']")
    public WebElement table_SearchResultsTable;
    
    private Guidewire8Select select_ReportType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'reportTypeInput-triggerWrap')]");
    }
    
    private Guidewire8Select select_Direction() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'directionInput-triggerWrap')]");
    }
    
    private Guidewire8Select select_ReportTypeTableFilter() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'reportTypeFilter-triggerWrap')]");
    }
    
    // /////////
    // Methods//
    // /////////

    private void clickAddFilterButton() {
    	clickWhenClickable(button_AddFilter);
    }
    
    private void selectReportType(ReportPayloadRetrieval.ReportType reportType) {
    	select_ReportType().selectByVisibleText(reportType.getValue());
    }
    
    private void setDate(Date date) {
    	setText(editbox_ReportDate, DateUtils.dateFormatAsString("MM/dd/yyyy", date));
    }
    
    private void setJobNumber(String policyTransactionNumber) {
    	setText(editbox_JobNumber, policyTransactionNumber);
    }
    
    private String getPayload() {
    	waitUntilElementIsVisible(textArea_Payload);
    	return textArea_Payload.getText();
    }
    
    private void selectDirection(ReportPayloadRetrieval.Direction direction) {
    	select_Direction().selectByVisibleText(direction.getValue());
    }
    
    private void selectReportTypeTableFilter(String reportTypeTableFilter) {
    	select_ReportTypeTableFilter().selectByVisibleTextExactContent(reportTypeTableFilter);
    }
    
    public void createFilter (ReportPayloadRetrieval.ReportType reportType, Date date, String policyTransactionNumber, ReportPayloadRetrieval.Direction direction) {
    	if (reportType != null) {
    		selectReportType(reportType);
    	}
    	if (date != null) {
    		setDate(date);
    	}
    	if (policyTransactionNumber != null) {
    		setJobNumber(policyTransactionNumber);
    	}
    	if (direction != null) {
    		selectDirection(direction);
    	}
    	clickAddFilterButton();
    }
    
    public void selectPreviouslyCreatedResultsFilter (ReportPayloadRetrieval.ReportType reportType, Date date, String policyTransactionNumber, ReportPayloadRetrieval.Direction direction) {
    	StringBuilder filterStringBuilder = new StringBuilder();
    	if (policyTransactionNumber != null) {
    		filterStringBuilder.append(policyTransactionNumber);
    	}
    	filterStringBuilder.append(": ");
    	if (reportType != null) {
    		if (reportType == ReportType.None) {
    			filterStringBuilder.append(ReportType.All.getValue().toUpperCase().trim().replaceAll(" ", ""));
    		} else {
    			filterStringBuilder.append(reportType.getValue().toUpperCase().trim().replaceAll(" ", ""));
    		}
    	}
    	filterStringBuilder.append(" - ");
    	if (date != null) {
    		filterStringBuilder.append(DateUtils.dateFormatAsString("yyyy-MM-dd", date));
    	} else {
    		filterStringBuilder.append("null");
    	}
    	filterStringBuilder.append(" ");    	
    	if (direction != null) {
    		if (direction != Direction.None) {
    			filterStringBuilder.append(direction.getValue());
    		}
    	}
    	selectReportTypeTableFilter(filterStringBuilder.toString());
    }
    
    public void filterPayloadRetrievalResults (ReportPayloadRetrieval.ReportType reportType, Date date, String policyTransactionNumber, ReportPayloadRetrieval.Direction direction) {
    	createFilter (reportType, date, policyTransactionNumber, direction);
    	selectPreviouslyCreatedResultsFilter(reportType, date, policyTransactionNumber, direction);
    }
    
    public WebElement getPayloadRetrievalResultsTable() {
    	waitUntilElementIsVisible(table_SearchResultsTable);
    	return table_SearchResultsTable;
    }
    
    public List<ISO> getListOfAllISOResults() throws JAXBException {
    	List<ISO> listOfISOResults = new ArrayList<ISO>();
    	TableUtils tableUtils = new TableUtils(driver);
    	
    	JAXBContext jaxbContext = JAXBContext.newInstance(ISO.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
    	do {
    		int rowCount = tableUtils.getRowCount(table_SearchResultsTable);
	    	for (int i = 1; i <= rowCount; i++) {
	    		tableUtils.clickRowInTableByRowNumber(table_SearchResultsTable, i);
	    		
	    		StringReader reader = new StringReader(getPayload());
	    		listOfISOResults.add((ISO) unmarshaller.unmarshal(reader));
	    	}
    	} while (tableUtils.incrementTablePageNumber(table_SearchResultsTable));
    	return listOfISOResults;
    }
    
    public List<String> getListOfAllISOResultsStrings() {
    	List<String> listOfISOResults = new ArrayList<String>();
    	TableUtils tableUtils = new TableUtils(driver);
    	
    	do {
    		int rowCount = tableUtils.getRowCount(table_SearchResultsTable);
	    	for (int i = 1; i <= rowCount; i++) {
	    		tableUtils.clickRowInTableByRowNumber(table_SearchResultsTable, i);
	    		listOfISOResults.add(getPayload());
	    	}
    	} while (tableUtils.incrementTablePageNumber(table_SearchResultsTable));
    	return listOfISOResults;
    }
    
    public List<ISO> getListOfISOResults(ReportPayloadRetrieval.Direction direction) throws JAXBException {
    	List<ISO> listOfISOResults = new ArrayList<ISO>();
    	TableUtils tableUtils = new TableUtils(driver);
    	
    	JAXBContext jaxbContext = JAXBContext.newInstance(ISO.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
    	do {
    		int rowCount = tableUtils.getRowCount(table_SearchResultsTable);
	    	for (int i = 1; i <= rowCount; i++) {
	    		tableUtils.clickRowInTableByRowNumber(table_SearchResultsTable, i);
	    		
	    		StringReader reader = new StringReader(getPayload());
	    		listOfISOResults.add((ISO) unmarshaller.unmarshal(reader));
	    	}
    	} while (tableUtils.incrementTablePageNumber(table_SearchResultsTable));
    	return listOfISOResults;
    }
    
    public List<String> getListOfISOResultsStrings(ReportPayloadRetrieval.Direction direction) {
    	List<String> listOfISOResults = new ArrayList<String>();
    	TableUtils tableUtils = new TableUtils(driver);
    	do {
    		int rowCount = tableUtils.getRowCount(table_SearchResultsTable);
	    	for (int i = 1; i <= rowCount; i++) {
	    		tableUtils.clickRowInTableByRowNumber(table_SearchResultsTable, i);
	    		listOfISOResults.add(getPayload());
	    	}
    	} while (tableUtils.incrementTablePageNumber(table_SearchResultsTable));
    	return listOfISOResults;
    }
    
    public List<ISO> getListOfISOResults(ReportPayloadRetrieval.ReportType reportType, Date date, String policyTransactionNumber, ReportPayloadRetrieval.Direction direction) throws JAXBException {
    	filterPayloadRetrievalResults(reportType, date, policyTransactionNumber, direction);
    	if (direction != null && direction != Direction.None) {
    		return getListOfISOResults(direction);
    	} else {
    		return getListOfAllISOResults();
    	}
    }
    
    public List<String> getListOfISOResultsStrings(ReportPayloadRetrieval.ReportType reportType, Date date, String policyTransactionNumber, ReportPayloadRetrieval.Direction direction) {
    	filterPayloadRetrievalResults(reportType, date, policyTransactionNumber, direction);
    	if (direction != null && direction != Direction.None) {
    		return getListOfISOResultsStrings(direction);
    	} else {
    		return getListOfAllISOResultsStrings();
    	}
    }
}
