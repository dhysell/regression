package repository.gw.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.enums.ScriptParameter;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;

public class AdminScriptParameters extends BasePage {
    private WebDriver driver;
    private TableUtils tableUtils;

    public AdminScriptParameters(WebDriver driver) {
    	super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // //////////
    // Elements//
    // //////////

    @FindBy(xpath = "//div[@id='ScriptParametersPage:ScriptParametersScreen:ScriptParametersLV']")
    public WebElement table_ScriptParametersTable;
    
    @FindBy(xpath = "//input[@id='ScriptParameterDetail:ScriptParameterDetailScreen:ScriptParameterDetailDV:IntegerValue-inputEl']")
    private WebElement editbox_IntegerValue;
    
    @FindBy(xpath = "//input[@id='ScriptParameterDetail:ScriptParameterDetailScreen:ScriptParameterDetailDV:DecimalValue-inputEl']")
    private WebElement editbox_BigDecimalValue;
    
    @FindBy(xpath = "//input[@id='ScriptParameterDetail:ScriptParameterDetailScreen:ScriptParameterDetailDV:VarcharValue-inputEl']")
    private WebElement editbox_StringValue;
    
    @FindBy(xpath = "//input[@id='ScriptParameterDetail:ScriptParameterDetailScreen:ScriptParameterDetailDV:DatetimeValue-inputEl']")
    private WebElement editbox_DateValue;
    
    @FindBy(xpath = "//a[@id='ScriptParameterDetail:ScriptParameterDetail_UpLink']")
    private WebElement link_UpToScriptParameters;
    
    private Guidewire8RadioButton radio_booleanValue() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'ScriptParameterDetail:ScriptParameterDetailScreen:ScriptParameterDetailDV:BitValue-containerEl')]/table");
    }
    
    // /////////
    // Methods//
    // /////////

    public WebElement getScriptParametersTable() {
    	waitUntilElementIsVisible(table_ScriptParametersTable);
    	return table_ScriptParametersTable;
    }
    
    public void clickUpToScriptParametersLink() {
    	clickWhenClickable(link_UpToScriptParameters);
    }
    
    private WebElement getScriptParameterRow(ScriptParameter scriptParameter) {
    	char firstLetter = scriptParameter.getValue().toUpperCase().charAt(0);
    	if (firstLetter >= 'H' && firstLetter <= 'Z') {
    		tableUtils.sortByHeaderColumn(table_ScriptParametersTable, "Name");
    		tableUtils.setTablePageNumber(table_ScriptParametersTable, 1);
    		if (tableUtils.getCellTextInTableByRowAndColumnName(table_ScriptParametersTable, 1, "Name").substring(0, 1).matches("[\\W0-9a-hA-H]")) {
    			tableUtils.sortByHeaderColumn(table_ScriptParametersTable, "Name");
    		}
    	}
    	do {
    		if (tableUtils.checkIfLinkExistsInTable(table_ScriptParametersTable, scriptParameter.getValue())) {
    			return tableUtils.getRowInTableByColumnNameAndExactValue(table_ScriptParametersTable, "Name", scriptParameter.getValue());
    		}
    	} while (tableUtils.incrementTablePageNumber(table_ScriptParametersTable));
    	return null;
    }
    
    public void editScriptParameter (ScriptParameter scriptParameter, Object valueToSet) {
    	WebElement tableRow = getScriptParameterRow(scriptParameter);
    	String[] stringSplit = tableUtils.getCellTextInTableByRowAndColumnName(table_ScriptParametersTable, tableRow, "Type").split("\\.");
    	tableUtils.clickLinkInTableByRowAndColumnName(table_ScriptParametersTable, tableUtils.getRowNumberFromWebElementRow(tableRow), "Name");
    	clickEdit();
    	switch (stringSplit[2]) {
    	case "String" :
    		setText(editbox_StringValue, (String) valueToSet);
    		break;
    	case "Boolean" :
    		radio_booleanValue().select((Boolean)valueToSet);
    		break;
    	case "Integer" :
    		setText(editbox_IntegerValue, String.valueOf(valueToSet));
    		break;
    	case "BigDecimal" :
    		setText(editbox_BigDecimalValue, String.valueOf(valueToSet));
    		break;
    	case "Date" :
    		setText(editbox_DateValue, DateUtils.dateFormatAsString("MM/dd/yyyy", (Date) valueToSet));
    		break;
    	default :
    		//Do Nothing
    		break;
    	}
    	clickUpdate();
    }
}
