package scratchpad.cor.random;

import java.io.File;
import java.util.ArrayList;

import org.testng.annotations.Test;

import com.idfbins.ftputilities.FTPHelpers;

import persistence.flatfile.mbre.entities.CCQuarterlyRecord;
import persistence.flatfile.mbre.entities.PCMonthlyLocationRecord;
import persistence.flatfile.mbre.entities.PCMonthlyPolicyRecord;
import persistence.flatfile.mbre.entities.PCQuarterlyRecord;
import persistence.flatfile.mbre.helpers.MBRERecordHelper;
public class FlapJackAndFTPExample {
	
	private String ccQuarterlyFilePath = "C:/tmp/mbre/CCQuarterlyMutualBoilerRe.txt";
	private String pcQuarterlyFilePath = "C:/tmp/mbre/PCQuarterlyMutualBoilerRe.txt";
	private String pcMonthlyPolicyFilePath = "C:/tmp/mbre/PCMonthlyMbrePolicyFile.txt";
	private String pcMonthlyLocationFilePath = "C:/tmp/mbre/PCMonthlyMbreLocationFile.txt";
	
	@Test()
	public void ftpStuff() throws Exception {
		
		FTPHelpers.connectAndLogin("fbms2077", "mbrokerqa", "zaq12wsx");
		
		for(String d : FTPHelpers.listDirectoriesAsString()) {
			System.out.println(d);
		}
		
		FTPHelpers.changeDirectory("mbre/out");
		
		for(String f : FTPHelpers.listFilesAsString()) {
			System.out.println(f);
		}
		
		FTPHelpers.retrieveFile("CCQuarterlyMutualBoilerRe.txt", ccQuarterlyFilePath);
		FTPHelpers.retrieveFile("PCQuarterlyMutualBoilerRe.txt", pcQuarterlyFilePath);
		FTPHelpers.retrieveFile("PCMonthlyMbrePolicyFile.txt", pcMonthlyPolicyFilePath);
		FTPHelpers.retrieveFile("PCMonthlyMbreLocationFile.txt", pcMonthlyLocationFilePath);
		
		FTPHelpers.logoutAndDisconnect();

	}

    @Test(dependsOnMethods={"ftpStuff"})
    public void mapFilesToObjects() throws Exception {
    	
    	ArrayList<CCQuarterlyRecord> listOfActualCCQRecords = MBRERecordHelper.getCCQuarterlyRecords(new File(ccQuarterlyFilePath));
    	System.out.println(listOfActualCCQRecords.get(0).getClaimNumber());
    	
    	ArrayList<PCQuarterlyRecord> listOfActualPCQRecords = MBRERecordHelper.getPCQuarterlyRecords(new File(pcQuarterlyFilePath));
    	System.out.println(listOfActualPCQRecords.get(0).getPolicyNumber());
    	
    	ArrayList<PCMonthlyPolicyRecord> listOfActualPCMPRecords = MBRERecordHelper.getPCMonthlyPolicyRecords(new File(pcMonthlyPolicyFilePath));
    	System.out.println(listOfActualPCMPRecords.get(0).getCurrentPolicyNumber());
    	
    	ArrayList<PCMonthlyLocationRecord> listOfActualPCMLRecords = MBRERecordHelper.getPCMonthlyLocationRecords(new File(pcMonthlyLocationFilePath));
    	System.out.println(listOfActualPCMLRecords.get(0).getCurrentPolicyNumber());
    	
    }

    


}