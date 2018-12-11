package random;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.topmenu.BCTopMenuAdministration;
import repository.driverConfiguration.Config;
import repository.gw.administration.AdminImportData;
import repository.gw.enums.ImportAdminDataExistingRecordResolution;
import repository.gw.exception.GuidewireException;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;

public class UploadBCAdminDeltas extends BaseTest {
	
    @Test
    public void loginToBCAndUploadAdminDeltas() throws GuidewireException, Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        WebDriver driver = buildDriver(cf);
		
		Login login = new Login(driver);
		login.login("su", "gw");
		File directory = new File("\\\\fbmsqa11.idfbins.com/tmp/AdminDeltas/" + cf.getEnv().toUpperCase() + "/modules/configuration/config/import/AdminDelta");
		if (directory.exists() && directory.isDirectory()) {
		   getQALogger().info("Admin Delta Directory Found. Continuing File Upload...");
		   List<File> xmlFiles = new ArrayList<File>();
		   if (listFiles(directory, xmlFiles) != null ){
			   for (File xmlFile : xmlFiles) {
				   new BCTopMenuAdministration(driver).clickUtilitiesImportData();
				   new AdminImportData(driver).importAdminData(xmlFile.getAbsolutePath(), ImportAdminDataExistingRecordResolution.Overwrite_All_Exisiting_Records); 
			   }
		   }
		} else {
			getQALogger().info("No Admin Delta Directory Found. Finishing Script.");
		}
		
		new GuidewireHelpers(driver).logout();
    }
    
    private List<File> listFiles(File directory, List<File> finalFileList) {
        // Get all the files from a directory.
        File[] fileList = directory.listFiles();
        for (File file : fileList) {
            if (file.isFile()) {
                finalFileList.add(file);
            } else if (file.isDirectory()) {
            	listFiles(file, finalFileList);
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
