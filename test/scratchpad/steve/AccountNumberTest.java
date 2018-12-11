package scratchpad.steve;

import java.net.MalformedURLException;

import javax.xml.bind.JAXBException;

import org.testng.annotations.Test;

import services.enums.Broker;
import services.helpers.com.idfbins.membernumber.MemberNumberHelper;
import services.services.com.idfbins.membernumber.MembershipQueryResponse;
public class AccountNumberTest {
	
	private String acctNum = "989997";
	private String acct = "123456";
		
	@Test
	public void testAccountNumber() throws MalformedURLException, JAXBException {
		MemberNumberHelper number = new MemberNumberHelper(Broker.DEV);
		MembershipQueryResponse response = number.getContactRecords(acctNum);
		System.out.println("Survey says: " + response.getServiceStatus().getCode());
	}
}
