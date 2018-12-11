package services.helpers.com.idfbins.membershipupdate;

import services.enums.Broker;
import services.services.com.idfbins.membershipupdate.MembershipRecords;
import services.utils.JSONUtil;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MembershipUpdateHelper {

	private Broker broker;

	public MembershipUpdateHelper(Broker broker) {
		this.broker = broker;
	}

	public MembershipRecords getDuesRecords(Date date) throws Exception {
		String dateSTR = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String urlToTest = "http://" + this.broker.getMBHost() + ":7080/membershipUpdate/retrieve?date=" + dateSTR;
		String xmlResponse = JSONUtil.getURLResponseAsString(urlToTest);
		
		StringReader stringReader = new StringReader(xmlResponse);
		MembershipRecords myRecords = JAXB.unmarshal(stringReader, MembershipRecords.class);
        System.out.println("http://" + this.broker.getMBHost() + ":7080/membershipUpdate/retrieve?date=" + dateSTR);
		return myRecords;
	}
}