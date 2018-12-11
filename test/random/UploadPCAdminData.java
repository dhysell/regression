package random;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.administration.AdminImportData;
import repository.gw.enums.ImportAdminDataExistingRecordResolution;
import repository.gw.enums.StartablePlugin;
import repository.gw.exception.GuidewireException;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import repository.gw.servertools.ServerToolsSideBar;
import repository.gw.servertools.ServerToolsStartablePlugin;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuAdministrationPC;

public class UploadPCAdminData extends BaseTest {
	private String commaSeparatedListOfAdminData = System.getProperty("commaSeparatedListOfAdminData", "admin, PolicyformPatterns");
	private String adminDataFolderLocation = System.getProperty("adminDataFolderLocation", "\\\\fbmsqa11.idfbins.com/tmp/PCAdminData/FromDEV");
	
    @Test
    public void loginToPCAndUploadAdminData() throws GuidewireException, Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
		
		Login login = new Login(driver);
		login.login("su", "gw");
		File directory = new File(this.adminDataFolderLocation);
		boolean policyFormPatternsExist = false;
		boolean adminFileUploadSuccessful = true;
		if (directory.exists() && directory.isDirectory()) {
		   getQALogger().info("Admin Data Directory Found. Continuing File Upload...");
		   List<File> xmlFiles = new ArrayList<File>();
		   if (listFiles(directory, xmlFiles) != null ){
			   for (File xmlFile : xmlFiles) {
				   new TopMenuAdministrationPC(driver).clickImportData();
				   if (!new AdminImportData(driver).importAdminData(xmlFile.getAbsolutePath(), ImportAdminDataExistingRecordResolution.Overwrite_All_Exisiting_Records)) {
					   adminFileUploadSuccessful = false;
				   }
				   if (xmlFile.getName().toLowerCase().contains("formpatterns")) {
					   getQALogger().info("Policy Form Patterns were found and uploaded. Form Inference Plugin will need to be restarted.");
					   policyFormPatternsExist = true;
				   }
			   }
		   }
		} else {
			getQALogger().info("No Admin Data Directory Found. Finishing Script.");
		}
		
		if (policyFormPatternsExist) {
			getQALogger().info("Policy Form Patterns were found and uploaded. Restarting Form Inference Plugin...");
			new GuidewireHelpers(driver).pressAltShiftT();
			new ServerToolsSideBar(driver).clickStartablePlugin();
			new ServerToolsStartablePlugin(driver).stopStartablePlugin(StartablePlugin.FormInferenceConfigurationPlugin);
			new ServerToolsStartablePlugin(driver).startStartablePlugin(StartablePlugin.FormInferenceConfigurationPlugin);
		}
		
		new GuidewireHelpers(driver).logout();
		
		if(!adminFileUploadSuccessful) {
			Assert.fail("There was a failure uploading admin data. Please check the log and fix data accordingly.");
		}
    }
    
    private List<File> listFiles(File directory, List<File> finalFileList) {
        // Get all the files from a directory.
        File[] fileList = directory.listFiles();
        if (this.commaSeparatedListOfAdminData == null || this.commaSeparatedListOfAdminData.toLowerCase().trim().equals("")) {
        	for (File file : fileList) {
                if (file.isFile()) {
                    finalFileList.add(file);
                } else if (file.isDirectory()) {
                	listFiles(file, finalFileList);
                }
            }
        } else {
        	String[] listOfFilesSplit = this.commaSeparatedListOfAdminData.split(",");
        	for (File file : fileList) {
	        	for (int i = 0; i < listOfFilesSplit.length; i++) {
	        		if (file.getName().contains(listOfFilesSplit[i].trim()) && file.isFile()) {
	        			finalFileList.add(file);
	        		}
	        	}
        	}
        }
        
        List<File> xmlFiles = new ArrayList<File>();
        for (File fileToVerifyXML : finalFileList) {
        	if (fileToVerifyXML.getAbsolutePath().toLowerCase().endsWith(".xml")) {
        		xmlFiles.add(fileToVerifyXML);
        	}
        }
        if (xmlFiles.isEmpty()) {
        	getQALogger().info("There were no XML Files found to upload.");
        	return null;
        } else {
        	return xmlFiles;
        }
    }
}
