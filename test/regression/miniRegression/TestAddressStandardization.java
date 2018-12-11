package regression.miniRegression;

import services.broker.objects.address.requestresponse.AddressResponse;
import services.broker.services.address.ServiceAddressStandardization;
import com.idfbins.driver.BaseTest;
import services.enums.Broker;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestAddressStandardization extends BaseTest {

    private Broker mbConnDetails;
    private boolean printRequestXMLToConsole = false;
    private boolean printResponseXMLToConsole = false;
    private ServiceAddressStandardization testService;

    @Test()
    public void testStandardizeAddress() throws Exception {
        this.mbConnDetails = Broker.QA;
        this.printRequestXMLToConsole = true;
        this.printResponseXMLToConsole = true;
        this.testService = new ServiceAddressStandardization();
        AddressResponse testResponse = this.testService.standardizeAddress(this.testService.setUpTestAddressRequest("asdfasd", null, "Pocatello", "ID", null, "83201", null), this.mbConnDetails, this.printRequestXMLToConsole, this.printResponseXMLToConsole);

        String code = testResponse.getServiceStatus().getCode();
        Assert.assertEquals(code, "000", "ERROR: Based off the status code, " + testResponse.getServiceStatus().getDescription() + ":");

        if (testResponse.getStandardizedAddresses().size() > 0) {
            System.out.println("Standardized Address Line 1: " + testResponse.getStandardizedAddresses().get(0).getAddressLine1());
            System.out.println("Standardized City: " + testResponse.getStandardizedAddresses().get(0).getCity());
            System.out.println("Standardized County: " + testResponse.getStandardizedAddresses().get(0).getCounty());
            System.out.println("Standardized State: " + testResponse.getStandardizedAddresses().get(0).getState());
            System.out.println("Standardized Zip: " + testResponse.getStandardizedAddresses().get(0).getZip());
        }

    }

}
