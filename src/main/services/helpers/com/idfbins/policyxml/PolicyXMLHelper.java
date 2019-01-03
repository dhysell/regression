package services.helpers.com.idfbins.policyxml;

import com.google.gson.Gson;
import com.idfbins.nexus.common.vo.policy.PolicyNumber;
import com.idfbins.nexus.common.vo.policy.PolicyNumberFactory;
import services.enums.Broker;
import services.services.com.idfbins.policyxml.PolicySearch;
import services.services.com.idfbins.policyxml.policyretrieveresponse.Policy;
import services.utils.JSONUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PolicyXMLHelper {

	private Broker broker;

	public PolicyXMLHelper(Broker broker) {
		this.broker = broker;
	}

	public Policy getPolicy(String policyNumber, String formattedPolicyNumber, Date asOfDate, String company) throws JAXBException, MalformedURLException {
		if (asOfDate == null) {
			asOfDate = new Date();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		JAXBContext jc = JAXBContext.newInstance("services.com.idfbins.policyxml.policyretrieveresponse");
		Unmarshaller u = jc.createUnmarshaller();
        URL url = new URL("http://" + this.broker.getMBHost() + ":7080/nexus/policy/retrieve?company=" + company + "&policyNumber=" + policyNumber + "&policyNumberFormatted=" + formattedPolicyNumber + "&asOfDate=" + sdf.format(asOfDate));

		System.out.println("Getting XML file from:");
		System.out.println(url);

		@SuppressWarnings("rawtypes")
		JAXBElement jaxbelement = (JAXBElement) u.unmarshal(url);
		Policy policy = (Policy) jaxbelement.getValue();
		return policy;
	}

	public Policy getPolicyObjectFromSearch(PolicySearch policy, Date asOfDate) throws Exception  {

		PolicyNumberFactory pnf = PolicyNumberFactory.getInstance();
		PolicyNumber pn = pnf.create(policy.getPolicyNumber());
		System.out.println("||||||||||||||||||||COR!!!!!!!!!!!!!! " +  policy.getPolicyNumber());
		pn.setLobType(policy.getPolicyType());

		String resultPolicyNumber = pn.getFormattedString().replace("-", "");
		String formattedPolicyNumber = pn.getFormattedString();
		String company = "IDFBINS";
		if (pn.getFormattedString().substring(0, 2).toString().equalsIgnoreCase("08")) {
			company = "WCINS";
			formattedPolicyNumber = pn.toWCSIPolicyNumber().getFormattedString();
			resultPolicyNumber = pn.toWCSIPolicyNumber().getUnformattedPolicy();
		}

		Policy result = getPolicy(resultPolicyNumber, formattedPolicyNumber, asOfDate, company);
		return result;
	}

	public Map<String, PolicySearch> getPoliciesAsKeyValueMap(String memberNumber, Date asOfDate) throws IOException {
		Gson gson = new Gson();

        String urlToTest = "http://" + this.broker.getMBHost() + ":7080/nexus/policy/search?policyNumber=" + memberNumber + "&f=json";
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		if (asOfDate != null) {
			urlToTest += "&asOfDate=" + sdf.format(asOfDate);
		}
		String jsonResponse = JSONUtil.getURLResponseAsString(urlToTest);
		PolicySearch[] myPolicies = gson.fromJson(jsonResponse,	PolicySearch[].class);

		Map<String, PolicySearch> map = new HashMap<String, PolicySearch>();
		for (PolicySearch policy : myPolicies) {
			map.put(policy.getPolicyNumber(), policy);
		}
		return map;
	}

	public void showPolicies(Map<String, PolicySearch> map) {
		for (Map.Entry<String, PolicySearch> entry : map.entrySet()) {
			PolicySearch policy = entry.getValue();
			System.out.println(policy.getPolicyNumber() + " "
					+ policy.getInsuredName() + " " 
					+ policy.getAddress() + " "
					+ policy.getCity() + " " 
					+ policy.getState() + " "
					+ policy.getPostalCode() + " " 
					+ policy.getEffectiveDate()	+ " " 
					+ policy.getExpirationDate() + " "
					+ policy.getPolicyType());
		}
	}
}
