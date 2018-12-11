package regression.miniRegression;

import com.idfbins.driver.BaseTest;
import services.enums.Broker;
import services.helpers.com.idfbins.membernumber.MemberNumberHelper;
import org.testng.annotations.Test;
import services.services.com.idfbins.membernumber.MembershipQueryResponse;

public class HelperTest extends BaseTest {
	
	/*@Test(enabled=true)
	public void testDisbursementCheckStatusHelper() throws Exception {
		Map<String, Environments> environmentsMap = new HashMap<String, Environments>();
		environmentsMap.put("BC", new Environments(500, "test", "chofman@idfbins.com", "test", "test", "BC", true, true, true, 8, "chrome"));
		DisbursementCheckStatusAPIHelper helper = new DisbursementCheckStatusAPIHelper(environmentsMap);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2015);
		cal.set(Calendar.MONTH, Calendar.APRIL);
		cal.set(Calendar.DAY_OF_MONTH, 14);
		Date issueDate = cal.getTime();
		
		ServiceStatus myRN = helper.updateCheckStatus("BCDEV:1000000042", "141", "O", 33.00, issueDate);
		
		System.out.println(myRN.getCode());
		System.out.println(myRN.getDescription());
		System.out.println(myRN.getDetails());
	}
	
	@Test(enabled=false)
	public void testJenkinsBuildTrigger() throws IOException {
		JenkinsBuildHelper.buildProject(JenkinsProject.Thunderhead_JavaSeleniumBuildCheck);
	}
	
	@Test(enabled=false)
	public void testGeoCodingHelper() throws Exception {
		GeoCodingHelper helper = new GeoCodingHelper("http://fbms2091.idfbins.com");
		GeoCodedAddress myAddr = helper.getGeoCodedAddress("761 Park Ave", "Pocatello", "ID", "83201");
		
		System.out.println(myAddr.getCandidates().get(0).getLocation().getX());
		System.out.println(myAddr.getCandidates().get(0).getLocation().getY());
		System.out.println("");
	}
	
	@Test(enabled=false)
	public void testRoutingNumberHelper() throws Exception {
		RoutingNumberHelper helper = new RoutingNumberHelper(Broker.MBDEV);
		RoutingNumber myRN = helper.validateRoutingNumber("124000054");
		
		System.out.println(myRN.getCustomerName());
	}
	
	@Test(enabled=false)
	public void testPolicyServicesHelperGW() throws Exception {
		Map<ApplicationOrCenter, Environments> environmentsMap = new HashMap<ApplicationOrCenter, Environments>();
		environmentsMap.put(ApplicationOrCenter.getApplicationOrCenterFromStrValue("AB"), new Environments(500, "test", "chofman@idfbins.com", "test", "test", "AB", true, true, true, 8, "chrome"));
		PolicyServicesHelper helper = new PolicyServicesHelper(environmentsMap);
		BankInfo myRN = helper.validateRoutingNumber("124000054");
		
		System.out.println(myRN.getName());
	}
	
	@Test(enabled=true)
	public void testMembUpdate() throws Exception {
		MembershipUpdateHelper helper = new MembershipUpdateHelper(Broker.MBDEV);
		MembershipRecords responseObj = helper.getDuesRecords(new Date());
		
		System.out.println(responseObj.getMembershipRecord().get(0).getCountyNumber());
		System.out.println(responseObj.getMembershipRecord().get(0).getMembershipNumber());
		System.out.println(responseObj.getMembershipRecord().get(0).getEffectiveDate());
	}
	
	
	@Test(enabled=true)
	public void testCRMServicesHelperGW() throws Exception {
		CRMServicesHelper helper = new CRMServicesHelper("http://ab8uat/ab");
		CRMSearchResponse myRN = helper.searchByAccountNumber("151188");
		
		System.out.println(myRN);
	}
	*/

    @Test(enabled = true)
    public void testMemberNumber() throws Exception {
        MemberNumberHelper helper = new MemberNumberHelper(Broker.DEV);
        MembershipQueryResponse myResponse = helper.getContactRecords("284089");
        System.out.println(myResponse.getServiceStatus().getCode());
    }
	
}
