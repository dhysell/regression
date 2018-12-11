package scratchpad.sai.sgunda.billingCenterStandaloneProject.testPad;

import com.idfbins.driver.BaseTest;
import gwclockservice.bcdebugtoolsapi.BCDebugToolsAPIPortType;
import org.testng.annotations.Test;
import scratchpad.sai.sgunda.billingCenterStandaloneProject.jars.BCDebugToolsAPIBC;

import javax.xml.ws.BindingProvider;
import java.net.URL;

import static java.lang.Thread.sleep;

public class test extends BaseTest {


    private void attachAuthentication(BindingProvider api) {
        api.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "su");
        api.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "gw");
    }

    @Test
    public void SquirePolicy() throws Exception {

        long startTime = System.nanoTime();
        sleep(1000*62);
        long endTime = System.nanoTime();

        double minutesAndSeconds = ((double)(endTime - startTime) / 1.0E9D )/60;
        System.out.println("Took " + (int) minutesAndSeconds +" min and " + (int) ((minutesAndSeconds - (int) minutesAndSeconds)*60) + " sec.");


        double number1 = 10.123456;
        double number2 = (int)Math.round(number1 * 100)/(double)100;
        System.out.println(number2);



        BCDebugToolsAPIBC bcService = new BCDebugToolsAPIBC(new URL("http://fbmqa10a7106:8582/bc/ws/gw/webservice/policycenter/bc801/BCDebugToolsAPI?WSDL"));
        BCDebugToolsAPIPortType bcApiPort =  bcService.getBCDebugToolsAPISoap11Port();

        ((BindingProvider) bcApiPort).getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", 10000);

        ((BindingProvider) bcApiPort).getRequestContext().put("com.sun.xml.internal.ws.request.timeout", 5000);

        attachAuthentication((BindingProvider) bcApiPort);
        //bcApiPort.restartExecutorsOnInvoiceDueBatch();

        /*
        bcApiPort.runInvoiceDue();

        boolean found = false;
        int timeToWaitInSeconds = 300;
        while (!found && timeToWaitInSeconds > 0) {
                Thread.sleep(5000);
                timeToWaitInSeconds = timeToWaitInSeconds - 10;
              System.out.println("timeToWaitInSeconds" + "  Left");
            found = bcApiPort.isInvoiceDueWorkQueueProcessingComplete();
            }*/
        }

    }
