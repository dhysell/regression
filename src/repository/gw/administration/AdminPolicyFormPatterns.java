package repository.gw.administration;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.gw.helpers.TableUtils;

public class AdminPolicyFormPatterns extends BasePage {
    private WebDriver driver;
    private Logger logger;

    public AdminPolicyFormPatterns(WebDriver driver) {
    	super(driver);
        this.driver = driver;
        this.logger = LoggerFactory.getLogger(this.getClass());
        PageFactory.initElements(driver, this);
    }

    // //////////
    // Elements//
    // //////////
    
    @FindBy(xpath = "//a[contains(@id, 'FormPatterns:FormPatternSearchScreen:FormPatternSearchResultsLV_tb:AddButton')]")
    private WebElement button_AddForm;
    
    @FindBy(xpath = "//a[contains(@id, 'FormPatterns:FormPatternSearchScreen:FormPatternSearchResultsLV_tb:DuplicateButton')]")
    private WebElement button_DuplicateForm;
    
    @FindBy(xpath = "//a[contains(@id, 'FormPatterns:FormPatternSearchScreen:FormPatternSearchResultsLV_tb:DeleteButton')]")
    private WebElement button_DeleteForm;
    
    @FindBy(xpath = "//input[contains(@id, 'FormPatterns:FormPatternSearchScreen:FormPatternSearchDV:FormNumber-inputEl')]")
    private WebElement editBox_FormNumber;
    
    @FindBy(xpath = "//input[contains(@id, 'FormPatterns:FormPatternSearchScreen:FormPatternSearchDV:FormName-inputEl')]")
    private WebElement editBox_FormName;
    
    @FindBy(xpath = "//input[contains(@id, 'FormPatterns:FormPatternSearchScreen:FormPatternSearchDV:GroupCode-inputEl')]")
    private WebElement editBox_GroupCode;
    
    private void clickDeleteForm() {
    	clickWhenClickable(button_DeleteForm);
    }
    
    private Guidewire8Select select_Product() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'FormPatterns:FormPatternSearchScreen:FormPatternSearchDV:Product-triggerWrap')]");
    }
    
    private Guidewire8Select select_Job() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'FormPatterns:FormPatternSearchScreen:FormPatternSearchDV:Job-triggerWrap')]");
    }
    
    @FindBy(xpath = "//div[contains(@id, 'FormPatterns:FormPatternSearchScreen:FormPatternSearchResultsLV')]")
    private WebElement table_PolicyFormPatternsSearchResults;
    
    // /////////
    // Methods//
    // /////////
    
    private void setFormNumber(String formNumber) {
        setText(editBox_FormNumber, formNumber);
    }
    
    private void setFormName(String formName) {
        setText(editBox_FormName, formName);
    }
    
    private void setGroupCode(String groupCode) {
        setText(editBox_GroupCode, groupCode);
    }
    
    private void selectProduct(ProductLineType productType) {
    	select_Product().selectByVisibleText(productType.getValue());
    }
    
    private void selectJob(TransactionType transactionType) {
    	select_Job().selectByVisibleText(transactionType.getValue());
    }
    
    private void searchFormPattern (String formNumber, String formName, ProductLineType productType, TransactionType transactionType, String groupCode) {
    	clickReset();
    	if (formNumber != null) {
    		setFormNumber(formNumber);
    	}
    	if (formName != null) {
    		setFormName(formName);
    	}
    	if (productType != null) {
    		selectProduct(productType);
    	}
    	if (transactionType != null) {
    		selectJob(transactionType);
    	}
    	if (groupCode != null) {
    		setGroupCode(groupCode);
    	}
    	clickSearch();
    }
    
    public boolean deletePolicyFormPattern (String formNumber, String formName, ProductLineType productType, TransactionType transactionType, String groupCode) {
    	String [] formNumberSplit = null;
    	String formNumberFormatted = null;
    	if (formNumber != null) {
    		if (!formNumber.trim().contains(" ")) {
    			//Take IDBP1000050918 and Split it to IDBP 10 0005
				formNumberSplit = formNumber.substring(0, formNumber.length() - 3).split("[a-zA-Z]+");
				formNumberFormatted = formNumber.substring(0, formNumber.indexOf(formNumberSplit[1])) + " " + formNumberSplit[1].substring(0, 2) + " " + formNumberSplit[1].substring(2, (formNumberSplit[1].length() - 1));
				searchFormPattern(formNumberFormatted, formName, productType, transactionType, groupCode);
    		} else {
    			searchFormPattern(formNumber, formName, productType, transactionType, groupCode);
    		}
    	}
    	TableUtils tableUtils = new TableUtils(driver);
    	if (tableUtils.getRowCount(table_PolicyFormPatternsSearchResults) <= 0) {
    		if (formNumber != null) {
    			formNumberFormatted = formNumber.substring(0, formNumber.indexOf(formNumberSplit[1])) + " " + formNumberSplit[1].substring(0, (formNumberSplit[1].length() - 1));
    			searchFormPattern(formNumberFormatted, formName, productType, transactionType, groupCode);
    			if (tableUtils.getRowCount(table_PolicyFormPatternsSearchResults) <= 0) {
    				logger.error("The form searched could not be found. We will not be able to delete this form.");
        			return false;
    			}
    		} else {
    			logger.error("The form searched could not be found. We will not be able to delete this form.");
    			return false;
    		}
    	}
    	if (formNumber != null) {
    		try {
    			new TableUtils(driver).setCheckboxInTableByText(table_PolicyFormPatternsSearchResults, formNumber, true);
    		} catch (Exception e) {
    			new TableUtils(driver).setCheckboxInTableByText(table_PolicyFormPatternsSearchResults, formNumberFormatted, true);
    		}
    	} else if (formName != null) {
    		new TableUtils(driver).setCheckboxInTableByText(table_PolicyFormPatternsSearchResults, formName, true);
    	}
    	clickDeleteForm();
    	selectOKOrCancelFromPopup(OkCancel.OK);
    	return true;
    }
}