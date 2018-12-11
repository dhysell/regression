package random;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.administration.AdminExportData;
import repository.gw.enums.AdminDataExportType;
import repository.gw.exception.GuidewireException;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuAdministrationPC;

public class ExportPCAdminData extends BaseTest {
	
    @Test
    public void loginToPCAndExportAllAdminData() throws GuidewireException, Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
		
		Login login = new Login(driver);
		login.login("su", "gw");
		
		new TopMenuAdministrationPC(driver).clickExportData();
		String dataFileExportPath = "\\\\fbmsqa11.idfbins.com\\tmp\\PCAdminData";
		List<AdminDataExportType> adminDataList = new AdminExportData(driver).getListOfAdminDataOptions();
		
		for (AdminDataExportType adminType : adminDataList) {
			getQALogger().info("Exporting " + adminType.getValue() + " Data...");
			new AdminExportData(driver).exportAdminDataFile(adminType, dataFileExportPath);
			
		}
		
		getQALogger().info("Done Exporting Data. Finishing Script...");
		new GuidewireHelpers(driver).logout();
    }
}
