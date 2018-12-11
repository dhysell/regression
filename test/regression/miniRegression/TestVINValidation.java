package regression.miniRegression;

import services.broker.objects.vin.requestresponse.ValidateVINRequest;
import services.broker.objects.vin.requestresponse.ValidateVINResponse;
import services.broker.services.vin.ServiceVINValidation;
import com.idfbins.driver.BaseTest;
import services.enums.Broker;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;

public class TestVINValidation extends BaseTest {

    private Broker mbConnDetails;
    private boolean printRequestXMLToConsole = false;
    private boolean printResponseXMLToConsole = false;
    private ServiceVINValidation testService;

    @Test()
    public void testValidateVIN() throws Exception {
        Vin testVin = VINHelper.getRandomVIN();

        testService = new ServiceVINValidation();
        mbConnDetails = Broker.DEV;
        printRequestXMLToConsole = true;
        printResponseXMLToConsole = true;
        ValidateVINRequest foo = testService.setUpTestValidateVINRequest(testVin.getVin());
        ValidateVINResponse testResponse = testService.validateVIN2(foo, mbConnDetails, printRequestXMLToConsole, printResponseXMLToConsole);

        String code = testResponse.getServiceStatus().getCode();
        Assert.assertEquals(code, "000", "ERROR: Based off the status code, " + testResponse.getServiceStatus().getDescription() + ": ");
        Assert.assertEquals(testResponse.getYear(), Integer.valueOf(testVin.getYear()), "ERROR: Vin Year did not match expected");
    }
	
	/*@Test()
	public void testValidateAllVins() throws Exception {
		this.mbConnDetails = BrokerMQ.MQDEV;
		this.printRequestXMLToConsole = false;
		this.printResponseXMLToConsole = false;
		this.testService = new ServiceVINValidation();
		List<Vin> testVin = VINHelper.getAllVins();
		ValidateVINResponse testResponse;
		int countWrong = 0;
		for (Vin v : testVin) {
			testResponse = this.testService.validateVIN(this.testService.setUpTestValidateVINRequest(v.getVin()), this.mbConnDetails, this.printRequestXMLToConsole, this.printResponseXMLToConsole);
			String serviceMake = null;
			String serviceModel = null;
			Integer serviceYear = null;
			String databaseMake;
			String databaseModel;
			Integer databaseYear;

			databaseMake = v.getMake().toUpperCase().trim();
			databaseModel = v.getModel().toUpperCase().trim();
			databaseYear = Integer.valueOf(v.getYear().trim());
			
			try {
				serviceMake = testResponse.getMake().toUpperCase().trim();
				serviceModel = testResponse.getModel().toUpperCase().trim();
				serviceYear = testResponse.getYear();
			} catch (Exception e) {
				System.out.println("ERROR: Not Match Between Service and DB\n\tDATABASE: " + databaseYear + " " + databaseMake + " " + databaseModel + "\n\tSERVICE: " + serviceYear + " " + serviceMake + " " + serviceModel);
				countWrong++;
			}
			
			if(serviceMake != null) {
				if((!serviceMake.equals(databaseMake)) || (!serviceModel.equals(databaseModel)) || (!serviceYear.equals(databaseYear))) {

					System.out.println("ERROR: Not Match Between Service and DB\n\tDATABASE: " + databaseYear + " " + databaseMake + " " + databaseModel + "\n\tSERVICE: " + serviceYear + " " + serviceMake + " " + serviceModel);
					countWrong++;
				}
			}
		}
		
		System.out.println("ERROR TOTAL WRONG: " + countWrong);
	}*/

}
