package repository.gw.helpers;

import com.idfbins.enums.Browsers;
import com.idfbins.helpers.EmailUtils;
import isup.IsUp;
import org.testng.Assert;
import services.broker.objects.address.requestresponse.AddressResponse;
import services.broker.services.address.ServiceAddressStandardization;
import services.enums.Broker;

import java.util.ArrayList;

public class GuidewireIsUpHelper {
	
	private String xpathUsernameToCheck = "//input[starts-with(@id,'Login:LoginScreen:LoginDV:username-inputEl')]";
	private ArrayList<String> emailsToSendTo = new ArrayList<String>();

	private boolean serverCheck(ArrayList<String> listOfUrlsToCheck) throws Exception {
		emailsToSendTo.add("architect@idfbins.com");
		emailsToSendTo.add("qa@idfbins.com");
		
		boolean allUp = true;
		boolean oneUp = true;
		
		for (String url : listOfUrlsToCheck) {						
			oneUp = new IsUp().isUrlAccessible(Browsers.Chrome, url, xpathUsernameToCheck, 10 * 60 * 1000, emailsToSendTo);

			if (!oneUp && allUp) {
				allUp = oneUp;
			}
		}

		return allUp;
	}
	
	// PL Environments
	public boolean checkIfServersAreUp_QA2() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://pc8qa2/pc");
		listOfUrlsToCheck.add("http://bc8qa2/bc");
		listOfUrlsToCheck.add("http://ab8qa2/ab");
		listOfUrlsToCheck.add("http://cc8qa2/cc");

		return serverCheck(listOfUrlsToCheck);
	}

	public boolean checkIfServersAreUp_IT2() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://pc8it2/pc");
		listOfUrlsToCheck.add("http://bc8it2/bc");
		listOfUrlsToCheck.add("http://ab8it2/ab");
		listOfUrlsToCheck.add("http://cc8it2/cc");

		return serverCheck(listOfUrlsToCheck);
	}

	public boolean checkIfServersAreUp_UAT2() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://pc8uat2/pc");
		listOfUrlsToCheck.add("http://pc8buat2/pc");
		listOfUrlsToCheck.add("http://bc8uat2/bc");
		listOfUrlsToCheck.add("http://bc8buat2/bc");
		listOfUrlsToCheck.add("http://ab8uat2/ab");
		listOfUrlsToCheck.add("http://cc8uat2/cc");
		listOfUrlsToCheck.add("http://cc8buat2/cc");

		return serverCheck(listOfUrlsToCheck);
	}

	public boolean checkIfServersAreUp_DEV2() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://pc8dev2/pc");
		listOfUrlsToCheck.add("http://bc8dev2/bc");
		listOfUrlsToCheck.add("http://ab8dev2/ab");
		listOfUrlsToCheck.add("http://cc8dev2/cc");

		return serverCheck(listOfUrlsToCheck);
	}
	
	public boolean checkIfServersAreUp_BRETT2() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://fbmis751vm-gw8:8180/pc");
		listOfUrlsToCheck.add("http://fbmis751vm-gw8:8580/bc");
		listOfUrlsToCheck.add("http://fbmis751vm-gw8:8280/ab");
		listOfUrlsToCheck.add("http://fbmis751vm-gw8:8380/cc");

		return serverCheck(listOfUrlsToCheck);
	}
	
	public boolean checkIfServersAreUp_COR2() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://fbmis798vm-gw8:8180/pc");
		listOfUrlsToCheck.add("http://fbmis798vm-gw8:8580/bc");
		listOfUrlsToCheck.add("http://fbmis798vm-gw8:8280/ab");
		listOfUrlsToCheck.add("http://fbmis798vm-gw8:8380/cc");

		return serverCheck(listOfUrlsToCheck);
	}
	
	public boolean checkIfServersAreUp_STEVE2() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://fbmis796vm-gw8:8180/pc");
		listOfUrlsToCheck.add("http://fbmis796vm-gw8:8580/bc");
		listOfUrlsToCheck.add("http://fbmis796vm-gw8:8280/ab");
		listOfUrlsToCheck.add("http://fbmis796vm-gw8:8380/cc");

		return serverCheck(listOfUrlsToCheck);
	}
	
	// STAB Environments
	public boolean checkIfServersAreUp_STAB01() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://fbmsgw-stab01:8180/pc");
		listOfUrlsToCheck.add("http://fbmsgw-stab01:8580/bc");
		listOfUrlsToCheck.add("http://fbmsgw-stab01:8280/ab");

		return serverCheck(listOfUrlsToCheck);
	}
	
	public boolean checkIfServersAreUp_STAB02() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://fbmsgw-stab02:8180/pc");
		listOfUrlsToCheck.add("http://fbmsgw-stab02:8580/bc");
		listOfUrlsToCheck.add("http://fbmsgw-stab02:8280/ab");

		return serverCheck(listOfUrlsToCheck);
	}
	
	public boolean checkIfServersAreUp_STAB03() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://fbmsgw-stab03:8180/pc");
		listOfUrlsToCheck.add("http://fbmsgw-stab03:8580/bc");
		listOfUrlsToCheck.add("http://fbmsgw-stab03:8280/ab");

		return serverCheck(listOfUrlsToCheck);
	}
	
	// REGR Environments
	public boolean checkIfServersAreUp_REGR01() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://fbmsgw-regr01:8180/pc");
		listOfUrlsToCheck.add("http://fbmsgw-regr01:8580/bc");
		listOfUrlsToCheck.add("http://fbmsgw-regr01:8280/ab");

		return serverCheck(listOfUrlsToCheck);
	}
	
	public boolean checkIfServersAreUp_REGR02() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://fbmsgw-regr02:8180/pc");
		listOfUrlsToCheck.add("http://fbmsgw-regr02:8580/bc");
		listOfUrlsToCheck.add("http://fbmsgw-regr02:8280/ab");

		return serverCheck(listOfUrlsToCheck);
	}
	
	public boolean checkIfServersAreUp_REGR03() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://fbmsgw-regr03:8180/pc");
		listOfUrlsToCheck.add("http://fbmsgw-regr03:8580/bc");
		listOfUrlsToCheck.add("http://fbmsgw-regr03:8280/ab");

		return serverCheck(listOfUrlsToCheck);
	}
	
	public boolean checkIfServersAreUp_REGR04() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://fbmsgw-regr04:8180/pc");
		listOfUrlsToCheck.add("http://fbmsgw-regr04:8580/bc");
		listOfUrlsToCheck.add("http://fbmsgw-regr04:8280/ab");

		return serverCheck(listOfUrlsToCheck);
	}
	
	// BOP Environments
	public boolean checkIfServersAreUp_IT() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://pc8it/pc");
		listOfUrlsToCheck.add("http://bc8it/bc");
		listOfUrlsToCheck.add("http://ab8it/ab");
		listOfUrlsToCheck.add("http://cc8it/cc");

		return serverCheck(listOfUrlsToCheck);
	}
	
	public boolean checkIfServersAreUp_DEV() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://pc8dev/pc");
		listOfUrlsToCheck.add("http://bc8dev/bc");
		listOfUrlsToCheck.add("http://ab8dev/ab");
		listOfUrlsToCheck.add("http://cc8dev/cc");

		return serverCheck(listOfUrlsToCheck);
	}
	
	public boolean checkIfServersAreUp_UAT() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://pc8uat/pc");
		listOfUrlsToCheck.add("http://pc8buat/pc");
		listOfUrlsToCheck.add("http://bc8uat/bc");
		listOfUrlsToCheck.add("http://bc8buat/bc");
		listOfUrlsToCheck.add("http://ab8uat/ab");
		listOfUrlsToCheck.add("http://cc8uat/cc");
		listOfUrlsToCheck.add("http://cc8buat/cc");

		return serverCheck(listOfUrlsToCheck);
	}
	
	public boolean checkIfServersAreUp_BRETTTRUNK() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://fbmis751vm-gw8:8181/pc");
		listOfUrlsToCheck.add("http://fbmis751vm-gw8:8581/bc");
		listOfUrlsToCheck.add("http://fbmis751vm-gw8:8281/ab");
		listOfUrlsToCheck.add("http://fbmis751vm-gw8:8381/cc");

		return serverCheck(listOfUrlsToCheck);
	}
	
	public boolean checkIfServersAreUp_BRETTBRANCH() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://fbmis751vm-gw8:8183/pc");
		listOfUrlsToCheck.add("http://fbmis751vm-gw8:8583/bc");
		listOfUrlsToCheck.add("http://fbmis751vm-gw8:8283/ab");
		listOfUrlsToCheck.add("http://fbmis751vm-gw8:8383/cc");

		return serverCheck(listOfUrlsToCheck);
	}
	
	// CPP Environments
	public boolean checkIfServersAreUp_QA3() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://pc8qa3/pc");
		listOfUrlsToCheck.add("http://bc8qa3/bc");
		listOfUrlsToCheck.add("http://ab8qa3/ab");

		return serverCheck(listOfUrlsToCheck);
	}	
	
	public boolean checkIfServersAreUp_DEV3() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://pc8dev3/pc");
		listOfUrlsToCheck.add("http://bc8dev3/bc");
		listOfUrlsToCheck.add("http://ab8dev3/ab");

		return serverCheck(listOfUrlsToCheck);
	}	
	
	public boolean checkIfServersAreUp_BMARTIN() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://fbmqaw10-01vm:8180/pc");
		listOfUrlsToCheck.add("http://fbmqaw10-01vm:8580/bc");
		listOfUrlsToCheck.add("http://fbmqaw10-01vm:8280/ab");
		listOfUrlsToCheck.add("http://fbmqaw10-01vm:8380/cc");

		return serverCheck(listOfUrlsToCheck);
	}
	
	// OTHER Environments	
	public boolean checkIfServersAreUp_PRD() throws Exception {
		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://pc/pc");
		listOfUrlsToCheck.add("http://bc/bc");
		listOfUrlsToCheck.add("http://ab/ab");
		listOfUrlsToCheck.add("http://cc/cc");

		return serverCheck(listOfUrlsToCheck);
	}
	
	public boolean checkIfServersAreUp_R2PRDTest() throws Exception {
		emailsToSendTo.add("aatkinson@idfbins.com");

		ArrayList<String> listOfUrlsToCheck = new ArrayList<String>();
		listOfUrlsToCheck.add("http://fbmpgm10a7703:9190/pc");

		return serverCheck(listOfUrlsToCheck);
	}

    public void testAddressStandardization(String serverSet, Broker serverMBMQ) throws Exception {
		//System.out.println("ATTENTION!!! THE TEST TO VERIFY THAT ADDRESS STANDARDIZATION IS WORKING HAS BEEN COMMENTED OUT FOR THE TIME BEING. PLEASE BRING THIS METHOD BACK WHEN YOU ARE DONE WORKING ON IT. THIS MEANS YOU, BRETT!!!");
		ServiceAddressStandardization testService = new ServiceAddressStandardization();
		AddressResponse testResponse = testService.standardizeAddress(testService.setUpTestAddressRequest("234 South 17th Avenue", null, "Pocatello", "ID", null, "83201", null), serverMBMQ, false, false);
		
		boolean passed = testResponse.getServiceStatus().getCode().equals("000");
		if(!passed) {
			ArrayList<String> emailsToSendTo = new ArrayList<String>();
			emailsToSendTo.add("architect@idfbins.com");
			emailsToSendTo.add("qa@idfbins.com");
			
			String bodyOfEmail = "All,<br/><br/><p>We did a quick check to see if address standardization worked"
					+ " in " + serverSet + ".  You are receiving this email <u>because the message broker"
					+ " returned an unsuccessful status code.</u>  Please investigate further.  Thank you.</p>";

			new EmailUtils().sendEmail(emailsToSendTo, "AddressStandardization Service is Down: " + serverMBMQ.getMQHost(), bodyOfEmail, null);
		}
				
		Assert.assertTrue(passed, "ERROR: Address Standardization Returned a Status Code Of: " + testResponse.getServiceStatus().getCode() + ".  Expected a Status Code Of: 000. Assuming Address Standardization is Down. See emails.");
	}

}
