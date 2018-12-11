package regression.miniRegression;

import services.broker.objects.lexisnexis.cbr.response.actual.NcfReport;
import services.broker.objects.lexisnexis.cbr.response.actual.Summary.CreditRange;
import services.broker.objects.lexisnexis.generic.request.ReportType;
import services.broker.services.lexisnexis.ServiceLexisNexis;
import com.idfbins.driver.BaseTest;
import services.enums.Broker;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.globaldatarepo.entities.LexisNexis;
import persistence.globaldatarepo.helpers.LexisNexisHelper;

import java.util.Date;

public class TestLexisNexis extends BaseTest {

    private Broker mbConnDetails;
    private boolean printRequestXMLToConsole = false;
    private boolean printResponseXMLToConsole = false;
    private ServiceLexisNexis testService;
	
	/*@Test()
	public void testOrderMVR() throws Exception {
		this.mbConnDetails = BrokerMQ.MQDEV;
		this.printRequestXMLToConsole = false;
		this.printResponseXMLToConsole = false;
		this.testService = new ServiceLexisNexis();
		LexisNexis[] randomCustomers = {LexisNexisHelper.getRandomCustomerMVR(new Date())};
//		LexisNexis[] randomCustomers = {LexisNexisHelper.getCustomerByName("EDDIE", "BADER")};
		MvrReport testResponse = this.testService.orderMVR(this.testService.setUpTestOrder(ReportType.MVR, randomCustomers), this.mbConnDetails, this.printRequestXMLToConsole, this.printResponseXMLToConsole);
		
		String convicDesc = testResponse.getReport().getViolations().getVoilationsList().get(0).getDescription().toString();
		Assert.assertEquals(convicDesc, "CONVICTION", "ERROR: Conviction description not found.");
	}
	
	@Test()
	public void testOrderMVR() throws Exception {
		this.mbConnDetails = BrokerMQ.MQDEV;
		this.printRequestXMLToConsole = false;
		this.printResponseXMLToConsole = true;
		this.testService = new ServiceLexisNexis();
		List<LexisNexis> custs = LexisNexisHelper.getCustomersMVR(new Date());
		for(LexisNexis cust : custs) {
			LexisNexis[] randomCustomers = {cust};
			MvrReport testResponse = this.testService.orderMVR(this.testService.setUpTestOrder(ReportType.MVR, randomCustomers), this.mbConnDetails, this.printRequestXMLToConsole, this.printResponseXMLToConsole);
			
			List<MvrMessage> msgs = testResponse.getReport().getMessages().getMessageList();
			for(MvrMessage msg : msgs) {
				if(msg.getValue().contains("ISSUE TYPE:")) {
					if(msg.getValue().equals("ISSUE TYPE: DRIVERS LICENSE")) {
						String firstName = testResponse.getMvrSearchDataset().getMvrSubject().getName().getFirst();
						String middleName = testResponse.getMvrSearchDataset().getMvrSubject().getName().getMiddle();
						String lastName = testResponse.getMvrSearchDataset().getMvrSubject().getName().getLast();
						System.out.println(firstName + " " + middleName + " " + lastName);
					}
					
				}
			}
		}
		
		
	}*/

    @Test()
    public void testOrderCBR() throws Exception {
        this.mbConnDetails = Broker.DEV;
        this.printRequestXMLToConsole = false;
        this.printResponseXMLToConsole = true;
        this.testService = new ServiceLexisNexis();
        LexisNexis[] randomCustomers = {LexisNexisHelper.getRandomCustomerCBR(new Date())};
//		LexisNexis[] randomCustomers = {LexisNexisHelper.getCustomerByName("EDDIE", "BADER")};
        NcfReport testResponse = this.testService.orderCBR(this.testService.setUpTestOrder(ReportType.CBR, randomCustomers), this.mbConnDetails, this.printRequestXMLToConsole, this.printResponseXMLToConsole);

        CreditRange summaryHighCredit = testResponse.getReport().getSummary().getCreditRange();
        Assert.assertEquals(summaryHighCredit != null, true, "ERROR: Summary credit range high not found.");
    }
	
	/*@Test()
	public void testOrderCLUEAuto() throws Exception {
		this.mbConnDetails = BrokerMQ.MQDEV;
		this.printRequestXMLToConsole = true;
		this.printResponseXMLToConsole = true;
		this.testService = new ServiceLexisNexis();
		LexisNexis[] randomCustomers = {LexisNexisHelper.getRandomCustomerCLUEAuto(new Date())};
//		LexisNexis[] randomCustomers = {LexisNexisHelper.getCustomerByName("EDDIE", "BADER")};
		CluePersonalAuto testResponse = this.testService.orderCLUEAuto(this.testService.setUpTestOrder(ReportType.CLUE_AUTO, randomCustomers), this.mbConnDetails, this.printRequestXMLToConsole, this.printResponseXMLToConsole);
		
		String clueFileNumber = testResponse.getReport().getResultsDataset().getClaimsHistory().get(0).getClaim().get(0).getClueFileNumber();
		Assert.assertEquals(clueFileNumber.length() > 1, true, "ERROR: Clue Auto File number on first claim not found.");
	}
	
	@Test()
	public void testOrderCLUEProperty_PrimaryInsured() throws Exception {
		this.mbConnDetails = BrokerMQ.MQDEV;
		this.printRequestXMLToConsole = false;
		this.printResponseXMLToConsole = false;
		this.testService = new ServiceLexisNexis();
		LexisNexis[] randomCustomers = {LexisNexisHelper.getRandomCustomerCLUEProperty(new Date())};
//		LexisNexis[] randomCustomers = {LexisNexisHelper.getCustomerByName("EDDIE", "BADER")};
		CluePersonalProperty testResponse = this.testService.orderCLUEProperty(this.testService.setUpTestOrder(ReportType.CLUE_PROPERTY, randomCustomers), this.mbConnDetails, this.printRequestXMLToConsole, this.printResponseXMLToConsole);
		
		String clueFileNumber = testResponse.getReport().getResultsDataset().getClaimsHistory().get(0).getClaim().get(0).getClueFileNumber();
		Assert.assertEquals(clueFileNumber.length() > 1, true, "ERROR: Clue Property File number on first claim not found.");
	}
	
	@Test()
	public void testOrderCLUEProperty_PrimaryAdditionalInsured() throws Exception {
		this.mbConnDetails = BrokerMQ.MQDEV;
		this.printRequestXMLToConsole = false;
		this.printResponseXMLToConsole = false;
		this.testService = new ServiceLexisNexis();
		LexisNexis[] randomCustomers = {LexisNexisHelper.getRandomCustomerCLUEProperty(new Date()), LexisNexisHelper.getRandomCustomerCLUEProperty(new Date())};
//		LexisNexis[] randomCustomers = {LexisNexisHelper.getCustomerByName("EDDIE", "BADER"), LexisNexisHelper.getCustomerByName("EDDIE", "BADER")};
		CluePersonalProperty testResponse = this.testService.orderCLUEProperty(this.testService.setUpTestOrder(ReportType.CLUE_PROPERTY, randomCustomers), this.mbConnDetails, this.printRequestXMLToConsole, this.printResponseXMLToConsole);
		
		String clueFileNumber = testResponse.getReport().getResultsDataset().getClaimsHistory().get(0).getClaim().get(0).getClueFileNumber();
		Assert.assertEquals(clueFileNumber.length() > 1, true, "ERROR: Clue Property File number on first claim not found.");
	}
	
	@Test()
	public void testOrderPrefillPersonal() throws Exception {
		this.mbConnDetails = BrokerMQ.MQDEV;
		this.printRequestXMLToConsole = false;
		this.printResponseXMLToConsole = false;
		this.testService = new ServiceLexisNexis();
		LexisNexis[] randomCustomers = {LexisNexisHelper.getRandomCustomerPrefillPersonal(new Date())};
//		LexisNexis[] randomCustomers = {LexisNexisHelper.getCustomerByName("EDDIE", "BADER")};
		DataprefillReport testResponse = this.testService.orderPrefillPersonal(this.testService.setUpTestOrder(ReportType.AUTO_DATAPREFILL, randomCustomers), this.mbConnDetails, this.printRequestXMLToConsole, this.printResponseXMLToConsole);
		
		ADDAdminStatusEnum status = testResponse.getReport().getSummary().getDriverDiscoveryReport().getStatus();
		Assert.assertNotEquals(status, null, "ERROR: Status for Driver Discovery not found.");
	}
	
	@Test()
	public void testOrderPrefillCommercial_Company() throws Exception {
		this.mbConnDetails = BrokerMQ.MQDEV;
		this.printRequestXMLToConsole = false;
		this.printResponseXMLToConsole = true;
		this.testService = new ServiceLexisNexis();
		LexisNexis[] randomCustomers = {LexisNexisHelper.getRandomCustomerPrefillCommercial(new Date(), true)};
//		LexisNexis[] randomCustomers = {LexisNexisHelper.getCustomerByLastName("ELITE DRYWALLERS INC")};	
		Report testResponse = this.testService.orderPrefillCommercial(this.testService.setUpTestOrder(ReportType.COMMERCIAL_DATAPREFILL, randomCustomers), this.mbConnDetails, this.printRequestXMLToConsole, this.printResponseXMLToConsole);
		
		Integer numVehicles = testResponse.getSummary().getNumberDeveloped();
		Assert.assertNotEquals(numVehicles, 0, "ERROR: Status for Driver Discovery not found.");
	}
	
	@Test()
	public void testOrderPrefillCommercial_Person() throws Exception {
		this.mbConnDetails = BrokerMQ.MQDEV;
		this.printRequestXMLToConsole = false;
		this.printResponseXMLToConsole = false;
		this.testService = new ServiceLexisNexis();
		LexisNexis[] randomCustomers = {LexisNexisHelper.getRandomCustomerPrefillCommercial(new Date(), false)};
//		LexisNexis[] randomCustomers = {LexisNexisHelper.getCustomerByName("EDDIE", "BADER")};
		Report testResponse = this.testService.orderPrefillCommercial(this.testService.setUpTestOrder(ReportType.COMMERCIAL_DATAPREFILL, randomCustomers), this.mbConnDetails, this.printRequestXMLToConsole, this.printResponseXMLToConsole);
		
		Integer numVehicles = testResponse.getSummary().getNumberDeveloped();
		Assert.assertNotEquals(numVehicles, 0, "ERROR: Status for Driver Discovery not found.");
	}*/

}
