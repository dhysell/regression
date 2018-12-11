package persistence;

import java.util.ArrayList;
import java.util.Date;

import org.testng.annotations.Test;

import persistence.enums.PolicyLineType;
import persistence.enums.PolicyType;
import persistence.enums.SqlSigns;
import persistence.enums.TransactionType;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.guidewire.entities.ExistingPolicyLookUp;
import persistence.guidewire.helpers.ExistingPoliciesHelper;


public class GetRandomPolicy {

	@Test()
	public void testGetRandomPolicy() throws Exception {
		
		ArrayList<ExistingPolicyLookUp> Policy1Arry = ExistingPoliciesHelper.getRandomPolicyByType("QA", PolicyType.Squire, PolicyLineType.PersonalAutoLinePL, TransactionType.Issuance, new Date(), SqlSigns.LessThan);
		String PolicyAccountNum = Policy1Arry.get(0).getaccountNumber();
		Date PolicyJobClose = Policy1Arry.get(0).getcloseDate();
		Integer AgentUserID = Policy1Arry.get(0).getcode();
		System.out.println(PolicyAccountNum);
		System.out.println(PolicyJobClose);
		System.out.println(AgentUserID);
		Agents agent = AgentsHelper.getAgentByAgentNumber(AgentUserID+"");
		System.out.println(agent.getAgentUserName());
		
		
	}
	
}