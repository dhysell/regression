package repository.gw.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ImportAdminDataExistingRecordResolution;
import repository.pc.topmenu.TopMenuAdministrationPC;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminImportData extends BasePage {
    private WebDriver driver;
    private Logger logger;

    public AdminImportData(WebDriver driver) {
    	super(driver);
        this.driver = driver;
        this.logger = LoggerFactory.getLogger(this.getClass());
        PageFactory.initElements(driver, this);
    }

    // //////////
    // Elements//
    // //////////

    @FindBy(xpath = "//a[contains(@id, 'fileuploadfield-') and contains(@id, '-button')]")
    private WebElement button_BrowseFile;
    
    @FindBy(xpath = "//label[contains(@id, 'ImportWizard:ImportWizard_UploadScreen:ImportWizard_UploadDV:1')]")
    private WebElement label_UploadResult;
    
    @FindBy(xpath = "//label[contains(@id, 'ImportResults:ImportResultsScreen:ImportResultsDV:0')]")
    private WebElement label_ImportResult;
    
    private Guidewire8Select select_ExistingRecordResolution() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'ImportWizard:ImportWizard_UploadScreen:ImportWizard_UploadDV:Resolution-triggerWrap')]");
    }
    
    // /////////
    // Methods//
    // /////////

    public void clickFileUploadBrowseButton() {
    	clickWhenClickable(button_BrowseFile);
    }
    
    public void uploadAdminDataFile(String filePathForFileToUpload) throws AWTException {
    	clickFileUploadBrowseButton();
    	sleep(1); //Used to ensure that the Pop-up window has time to fully appear.
    	uploadOrSaveFile(filePathForFileToUpload);
    }
    
    public void selectExistingRecordResolution(ImportAdminDataExistingRecordResolution existingRecordResolutionOption) {
    	select_ExistingRecordResolution().selectByVisibleText(existingRecordResolutionOption.getValue());
    }
    
    public boolean importAdminData(String filePathForFileToUpload, ImportAdminDataExistingRecordResolution existingRecordResolutionOption) throws AWTException {
    	uploadAdminDataFile(filePathForFileToUpload);
    	if (checkIfElementExists("//label[contains(@id, 'ImportWizard:ImportWizard_UploadScreen:ImportWizard_UploadDV:1')]", 2000)) {
    		if (label_UploadResult.getText().contains("No conflicts were found with existing records.")) {
    			logger.info("No conflicts were found. Continuing upload.");
    		} else {
    			logger.info("Conflicts were found. Resolving them now by using the " + existingRecordResolutionOption.getValue() + " option...");
    			selectExistingRecordResolution(existingRecordResolutionOption);
    		}
    	}
    	clickFinish();
    	
    	boolean uploadSuccessful = true;
    	String importResult = label_ImportResult.getText();
    	if (importResult.contains("Data was successfully imported.")) {
    		Pattern pattern = Pattern.compile("^Data was successfully imported. (\\d+) records were inserted, (\\d+) records were updated, and (\\d+) records were deleted.$");
    		Matcher matcher = pattern.matcher(importResult);
    		matcher.find();
        	logger.info("There " + ((matcher.group(1).equals("1")) ? "was " : "were ") + matcher.group(1) + ((matcher.group(1).equals("1")) ? " record " : " records ") + "Imported from file at location: " + filePathForFileToUpload + ".");
        	logger.info("There " + ((matcher.group(2).equals("1")) ? "was " : "were ") + matcher.group(2) + ((matcher.group(2).equals("1")) ? " record " : " records ") + "Updated from file at location: " + filePathForFileToUpload + ".");
        	logger.info("There " + ((matcher.group(3).equals("1")) ? "was " : "were ") + matcher.group(3) + ((matcher.group(3).equals("1")) ? " record " : " records ") + "Deleted from file at location: " + filePathForFileToUpload + ".");
    	} else {
    		if (filePathForFileToUpload.toLowerCase().contains("formpatterns")) {
    			if (importResult.contains("Data import failed: error:entity.FormPattern/Form") && importResult.contains("is already in use by another form")) {
    				String formNumberToDelete = importResult.substring((importResult.indexOf("[") + 1), importResult.indexOf("]"));
    				new TopMenuAdministrationPC(driver).clickPolicyFormPatterns();
    				if (new AdminPolicyFormPatterns(driver).deletePolicyFormPattern(formNumberToDelete, null, null, null, null)) {
    					new TopMenuAdministrationPC(driver).clickImportData();
    					importAdminData(filePathForFileToUpload, existingRecordResolutionOption);
    				} else {
    					uploadSuccessful = false;
    				}
    			} else {
    				logger.error("The attempt to update policy form patterns found at location: " +  filePathForFileToUpload + " failed, and the failure message was one that was not expected. That message was: " + importResult);
    	    		uploadSuccessful = false;
    			}
    		} else {
	    		logger.error("The attempt to update admin data found at location: " +  filePathForFileToUpload + " failed. Please investigate this issue.");
	    		uploadSuccessful = false;
    		}
    	}
    	return uploadSuccessful;
    }
}
