package scratchpad.sai;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.helpers.BatchHelpers;
import gwclockhelpers.ApplicationOrCenter;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class StopOLIEBatchInAllTestEnvi extends BaseTest {

    String stopLogPrint = "--- Stop Log Info ---\n";
    String startLogPrint = "--- Start Log Info ---\n";


    public List<String> getListOfEnvironments(){
        List<String> listOfEnvironments =  new ArrayList<>();
        listOfEnvironments.add("UAT");
        listOfEnvironments.add("QA");
        listOfEnvironments.add("IT");
        listOfEnvironments.add("DEV");
        listOfEnvironments.add("STAB01");
        listOfEnvironments.add("STAB02");
        listOfEnvironments.add("STAB03");
        listOfEnvironments.add("STAB04 (Anthony)");
        listOfEnvironments.add("STAB05");
        listOfEnvironments.add("REGR01");
        listOfEnvironments.add("REGR02");
        listOfEnvironments.add("REGR03");
        listOfEnvironments.add("REGR04");
        listOfEnvironments.add("REGR05");
        listOfEnvironments.add("REGR06");
        listOfEnvironments.add("REGR07");
        listOfEnvironments.add("QA3");
        listOfEnvironments.add("DEV3");
        listOfEnvironments.add("TRAIN01");
        listOfEnvironments.add("JOEY");
        listOfEnvironments.add("TED");
        listOfEnvironments.add("CHRIS");
        listOfEnvironments.add("YOGESH");
        listOfEnvironments.add("ANURAG");
        listOfEnvironments.add("PFRANSEN");
        listOfEnvironments.add("MGUZMAN");
        listOfEnvironments.add("ECOLEMAN");
        listOfEnvironments.add("DSANDBORGH");
        listOfEnvironments.add("MARKAPP");
        listOfEnvironments.add("KTENNANT");
        listOfEnvironments.add("AATKINSONR3");
        listOfEnvironments.add("AATKINSONMAINTENANCE");
        listOfEnvironments.add("SGUNDA");
        listOfEnvironments.add("SGUNDARELEASE");
        listOfEnvironments.add("VINOD-VM2");
        listOfEnvironments.add("DDAVIS");
        listOfEnvironments.add("DINESH");
        listOfEnvironments.add("AATKINSONPROD");
        listOfEnvironments.add("PRASHANTH");
        listOfEnvironments.add("VINOD-VM1");
        listOfEnvironments.add("JQU");
        listOfEnvironments.add("JQU-PI");
        listOfEnvironments.add("SBRODERICK");
        listOfEnvironments.add("JLARSEN");
        listOfEnvironments.add("DHYSELL");
        return listOfEnvironments;
    }

    @Test(enabled = true)
    public void stopOLIEBatch() throws Exception {

        List<String> listOfEnvironments = getListOfEnvironments();

        for (String listOfEnvironment : listOfEnvironments) {
             try {
                 System.out.println("\n @@@@@@@@@@@@@@@ Stopping in "+ listOfEnvironment + " @@@@@@@@@@@@@@@ \n");
                 Config cf = new Config(ApplicationOrCenter.BillingCenter,listOfEnvironment);
                 new BatchHelpers(cf).stopBatchProcess(BatchProcess.FBM_OlieBankInformation_Import);
                 stopLogPrint = stopLogPrint + "Stopped in  "+ listOfEnvironment + "\n";
             } catch (Exception e){
                 e.printStackTrace();
                 System.out.println("\n XXXXXXXXXXXXXXX Failed to stop in "+ listOfEnvironment + " XXXXXXXXXXXXXXX \n");
                 stopLogPrint = stopLogPrint + "Failed to stop in  "+ listOfEnvironment + "\n";
             }
        }
        System.out.println(stopLogPrint);
    }

    @Test(enabled = false)
    public void startOLIEBatch() {

        List<String> listOfEnvironments = getListOfEnvironments();

        for (String listOfEnvironment : listOfEnvironments) {
            try {
                System.out.println("\n @@@@@@@@@@@@@@@ Starting in "+ listOfEnvironment + " @@@@@@@@@@@@@@@ \n");
                Config cf = new Config(ApplicationOrCenter.BillingCenter,listOfEnvironment);
                new BatchHelpers(cf).startBatchProcess(BatchProcess.FBM_OlieBankInformation_Import);
                startLogPrint = startLogPrint + "Started in  "+ listOfEnvironment + "\n";
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("\n XXXXXXXXXXXXXXX Failed to Start Batch in "+ listOfEnvironment + " XXXXXXXXXXXXXXX \n");
                startLogPrint = startLogPrint + "Failed to Start in "+ listOfEnvironment + "\n";
            }
        }
        System.out.println(startLogPrint);
    }



    }
