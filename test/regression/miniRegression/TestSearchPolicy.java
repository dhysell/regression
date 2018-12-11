package regression.miniRegression;

import java.util.Map;

import services.enums.Broker;
import services.helpers.com.idfbins.policyxml.PolicyXMLHelper;
import services.services.com.idfbins.policyxml.PolicySearch;
import services.services.com.idfbins.policyxml.policyretrieveresponse.Policy;
public class TestSearchPolicy {
    public static void main(String[] args) throws Exception {
        PolicyXMLHelper pxmlh = new PolicyXMLHelper(Broker.DEV);

        //returns a map of the policies attached to a given member number on the specified broker for a given date. null defaults to today.
        Map<String, PolicySearch> mapOfPolicies = pxmlh.getPoliciesAsKeyValueMap("057922", null);

        //outputs all policies to console for a given member number
        //you can then choose a policy number and give it to the getPolicyObjectFromSearch() method.
        pxmlh.showPolicies(mapOfPolicies);

        //returns policy object for the given policy number from the specified broker for a given date. null defaults to today.
        Policy policy = pxmlh.getPolicyObjectFromSearch(mapOfPolicies.get("01-057922-01"), null);
        System.out.println(policy.getPolicyLocations().getPolicyLocation().get(0).getLocation().getAddressLine1());
    }

}
