package scratchpad.sai.sgunda;

import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsElectronicPaymentEntry;
import repository.bc.topmenu.BCTopMenu;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentLocation;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import gwclockservice.bcdebugtoolsapi.BCDebugToolsAPI;
import gwclockservice.bcdebugtoolsapi.BCDebugToolsAPIPortType;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.xml.ws.BindingProvider;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ScratchPad extends BaseTest {

    public List<String> getPolicyNumbers(){

        List<String> policyNumbers = new ArrayList<>();
        policyNumbers.add("");
        policyNumbers.add("");
        policyNumbers.add("");

        return policyNumbers;
    }

    private void attachAuthentication(BindingProvider api) {
        api.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "su");
        api.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "gw");
    }

    private WebDriver driver;
    @Test
    public void SquirePolicy() throws Exception {

        BCDebugToolsAPI bcService = new BCDebugToolsAPI(new URL("http://fbmqa10a7106:8582/bc/ws/gw/webservice/policycenter/bc801/BCDebugToolsAPI?WSDL"));
        BCDebugToolsAPIPortType bcApiPort = bcService.getBCDebugToolsAPISoap11Port();

        ((BindingProvider) bcApiPort).getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", 10000);

        ((BindingProvider) bcApiPort).getRequestContext().put("com.sun.xml.internal.ws.request.timeout", 5000);

        attachAuthentication((BindingProvider) bcApiPort);
        bcApiPort.isInvoiceRunning();
        bcApiPort.getCurrentDate().toGregorianCalendar().getTime();

        Config cf = new Config(ApplicationOrCenter.BillingCenter, "UAT");
        driver = buildDriver(cf);
        new Login(driver).login("skane", "gw");
/*
        BCSearchPolicies policySearch = new BCSearchPolicies(driver);
        this.policyNumber1 = policySearch.findPolicyInGoodStanding("259", null, null);
        this.policyNumber2 = policySearch.findPolicyInGoodStanding("259", null, null);
*/
        BCTopMenu topMenu = new BCTopMenu(driver);
        topMenu.clickDesktopTab();

        List<String> polNo = getPolicyNumbers();

        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
        desktopMenu.clickDesktopMenuActionsElectronicPayment();


        DesktopActionsElectronicPaymentEntry electronicPaymentsPage = new DesktopActionsElectronicPaymentEntry(driver);
        TableUtils tableUtils = new TableUtils(driver);
        int rowCount = tableUtils.getRowCount(electronicPaymentsPage.table_DesktopActionsElectronicPaymenEntry);

        for(int i = 1 ; i<=rowCount ; i++){
            if(i%3 == 0) {
                electronicPaymentsPage.fillOutNextLineOnElectronicPaymentTable(i,"01-280100-01" /*polNo.get(i-1)*/, PaymentInstrumentEnum.Credit_Debit, PaymentLocation.NexusPayment,Double.valueOf(i) /*Double.valueOf(NumberUtils.generateRandomNumberInt(10,1000))*/);
            } else if( i%2 == 0) {
                electronicPaymentsPage.fillOutNextLineOnElectronicPaymentTable(i,"01-280100-01", PaymentInstrumentEnum.ACH_EFT, PaymentLocation.WebSite,  Double.valueOf(i));
            } else {
                electronicPaymentsPage.fillOutNextLineOnElectronicPaymentTable(i,"01-280100-01", PaymentInstrumentEnum.ACH_EFT, PaymentLocation.NexusPayment,  Double.valueOf(i));
            }

        }

        electronicPaymentsPage.clickAdd();
        rowCount = tableUtils.getRowCount(electronicPaymentsPage.table_DesktopActionsElectronicPaymenEntry);
        for(int i = 51 ; i<=rowCount-20 ; i++){
            if(i%3 == 0) {
                electronicPaymentsPage.fillOutNextLineOnElectronicPaymentTable( i,"08-280170-01", PaymentInstrumentEnum.Credit_Debit, PaymentLocation.NexusPayment,Double.valueOf(i) /*Double.valueOf(NumberUtils.generateRandomNumberInt(10,1000))*/);
            } else if( i%2 == 0) {
                electronicPaymentsPage.fillOutNextLineOnElectronicPaymentTable(i,"08-280170-01", PaymentInstrumentEnum.ACH_EFT, PaymentLocation.WebSite,  Double.valueOf(i));
            } else {
                electronicPaymentsPage.fillOutNextLineOnElectronicPaymentTable(i,"08-280170-01", PaymentInstrumentEnum.ACH_EFT, PaymentLocation.NexusPayment,  Double.valueOf(i));
            }

        }

        electronicPaymentsPage.clickNext();
        electronicPaymentsPage.clickFinish();

 /*
        System.out.println(tableUtils.getRowCount(electronicPaymentsPage.table_DesktopActionsElectronicPaymenEntry)+ " count after clicking add");

        electronicPaymentsPage.clickAdd();
        System.out.println(tableUtils.getRowCount(electronicPaymentsPage.table_DesktopActionsElectronicPaymenEntry)+ " count after clicking add second time");


        electronicPaymentsPage.fillOutNextLineOnElectronicPaymentTable(policyNumber1, PaymentInstrumentEnum.ACH_EFT, PaymentLocation.WebSite, 20.00);
        electronicPaymentsPage.fillOutNextLineOnElectronicPaymentTable(policyNumber2, PaymentInstrumentEnum.Credit_Debit, PaymentLocation.NexusPayment, 15.00);
        electronicPaymentsPage.clickNext();
        electronicPaymentsPage.clickFinish();
*/

    }

}
