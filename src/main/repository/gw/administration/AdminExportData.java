package repository.gw.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AdminDataExportType;
import repository.gw.enums.SecurityDictionaryExportType;
import repository.gw.helpers.DateUtils;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminExportData extends BasePage {
    private WebDriver driver;
    private Logger logger;

    public AdminExportData(WebDriver driver) {
    	super(driver);
        this.driver = driver;
        this.logger = LoggerFactory.getLogger(this.getClass());
        PageFactory.initElements(driver, this);
    }

    // //////////
    // Elements//
    // //////////

    @FindBy(xpath = "//a[contains(@id, 'ExportData:ExportDataScreen:ExportDataDV:ExportButton')]")
    private WebElement button_ExportAdminData;
    
    @FindBy(xpath = "//a[contains(@id, 'ExportData:ExportDataScreen:SecurityDictionaryDV:DownloadButton')]")
    private WebElement button_ExportSecurityDictionary;
    
    private Guidewire8Select select_ExportAdminDataOption() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'ExportData:ExportDataScreen:ExportDataDV:DataSet-triggerWrap')]");
    }
    
    private Guidewire8Select select_ExportSecurityDictionaryOption() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'ExportData:ExportDataScreen:SecurityDictionaryDV:OutputFormat-triggerWrap')]");
    }
    
    // /////////
    // Methods//
    // /////////

    private void clickExportAdminDataButton() {
    	clickWhenClickable(button_ExportAdminData);
    }
    
    private void clickExportSecurityDictionaryButton() {
    	clickWhenClickable(button_ExportSecurityDictionary);
    }
    
    private void selectAdminDataTypeToExport(AdminDataExportType adminDataType) {
    	select_ExportAdminDataOption().selectByVisibleText(adminDataType.getValue());
    }
    
    private void selectSecurityDictionaryFormatToExport(SecurityDictionaryExportType securityDictionaryFormatType) {
    	select_ExportSecurityDictionaryOption().selectByVisibleText(securityDictionaryFormatType.getValue());
    }
    
    public List<AdminDataExportType> getListOfAdminDataOptions() {
    	List<AdminDataExportType> adminDataExportList = new ArrayList<AdminDataExportType>();
    	for (String selectItem : select_ExportAdminDataOption().getList()) {
    		if (!selectItem.equals("<none>")) {
    			adminDataExportList.add(AdminDataExportType.getEnumValueFromString(selectItem));
    		}
    	}
    	return adminDataExportList;
    }
    
    public void exportAdminDataFile(AdminDataExportType adminDataType, String fileDirectoryPath) throws AWTException {
    	logger.info("Exporting Data...");
    	selectAdminDataTypeToExport(adminDataType);
    	clickExportAdminDataButton();
    	waitForPostBack(60);
    	sleep(1); //Used to ensure that the Pop-up window has time to fully appear.
    	logger.info("Saving Data...");
    	//This cannot currently handle dialog pop-ups (i.e. - asking to overwrite a file).
    	uploadOrSaveFile(fileDirectoryPath + ((fileDirectoryPath.endsWith("/") || fileDirectoryPath.endsWith("\\")) ? "" : "\\") + adminDataType.getFileNameValue() + "." + adminDataType.getFileNameExtension());
    	logger.info("Save Successful");
    }
    
    public void exportSecurityDictionaryFile(SecurityDictionaryExportType securityDictionaryFormatType, String fileDirectoryPath) throws AWTException {
    	logger.info("Exporting Data...");
    	selectSecurityDictionaryFormatToExport(securityDictionaryFormatType);
    	clickExportSecurityDictionaryButton();
    	waitForPostBack(60);
    	sleep(1); //Used to ensure that the Pop-up window has time to fully appear.
    	logger.info("Saving Data...");
    	//This cannot currently handle dialog pop-ups (i.e. - asking to overwrite a file).
    	uploadOrSaveFile(fileDirectoryPath + securityDictionaryFormatType.getFileNameValue() + ((fileDirectoryPath.endsWith("/") || fileDirectoryPath.endsWith("\\")) ? "" : "\\") + DateUtils.dateFormatAsString("yyyyMMddHHmmss", new Date()) + "." + securityDictionaryFormatType.getFileNameExtension());
    	logger.info("Save Successful");
    }
    
    public boolean verifyExportedDataFile(AdminDataExportType adminDataType, String fileDirectoryPath) {
    	if (getExportedDataFileSize(adminDataType, fileDirectoryPath) <= (2 * 1024)) {//exportedFile.length is in bytes. Checking if the file is less than 2 Kb (2 times 1024 bytes).
    		return false;
    	} else {
    		return true;
    	}
    }
    
    public double getExportedDataFileSize(AdminDataExportType adminDataType, String fileDirectoryPath) {
    	File exportedFile = new File(fileDirectoryPath + ((fileDirectoryPath.endsWith("/") || fileDirectoryPath.endsWith("\\")) ? "" : "\\") + adminDataType.getFileNameValue() + "." + adminDataType.getFileNameExtension());
    	return exportedFile.length();
    }
}
