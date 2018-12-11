package persistence;

import org.testng.annotations.Test;

public class AgentsTest {
	
	@Test
	public void testGetAgencyNames() throws Exception {
//		ArrayList<String> names = AgentsHelper.getAllAgencyNames();
//		
//		for(String n : names) {
//			System.out.println(n);
//		}
		System.out.println("THERE ARE AGE NO LONGER AGENCIES");
	}
	
	/*@Test
	public void testGetPolicies() throws Exception {
		
		Date forCancellation = new Date();
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			forCancellation = format.parse("2015-12-01");
		} catch (Exception e) {
			throw new ParseException(("Parse Exception: The String could not be parsed to convert to a date."), 0);
		}
		
		InceptionDateStuff pol = ExistingPoliciesHelper.getRandomPolicyForInceptionDates("DEV2", PolicyType.Squire, PolicyLineType.PersonalAutoLinePL, true, forCancellation, false);
		System.out.println(pol.getPolicyPeriodPrimaryNamedInsured());
		System.out.println(pol.getPolicyPeriodPolicyNumber());
		System.out.println(pol.getPolicyPeriodTermNumber());
		System.out.println(pol.getPolicyPeriodCancellationDate());
		System.out.println(pol.getPolicyCreateTime());
		System.out.println(pol.getPolicyInceptionDate());
		System.out.println(pol.getPolicyIssueDate());
		System.out.println(pol.getPolicyExpirationDate());
		
		System.out.println(pol.getPolicyProductName());
		System.out.println(pol.getPolicyLineNamePolicySelection());
		
	}*/
	
	/*@Test
	public void testGetPoliciesByAgentUserName() throws Exception {
		
		GeneratedPoliciesHelper.createNewAgentPolicyData("dbosworth", "08-123456-01", "BOP", "UAT");		
		
		List<GeneratedPolicies> pols = GeneratedPoliciesHelper.getPoliciesByUserName("dbosworth");
		System.out.println("test");
	}*/

	/*@Test
	public void testGetRandomUnderwriter() throws Exception {
		
		Underwriters randomUnderwriter = UnderwritersHelper.getRandomUnderwriter();
		System.out.println(randomUnderwriter.getUnderwriterFirstName() + " " +  randomUnderwriter.getUnderwriterLastName());
		System.out.println(randomUnderwriter.getUnderwriterUserName());
	}
	
	@Test
	public void testGetUnderwriterUsername() throws Exception {
		
		Underwriters underwriter = UnderwritersHelper.getUnderwriterInfo("Ellen", "Van");
		System.out.println(underwriter.getUnderwriterFirstName() + " " +  underwriter.getUnderwriterLastName());
		System.out.println(underwriter.getUnderwriterUserName());
		System.out.println(underwriter.getUnderwriterPassword());
	}*/
	
	/*@Test
	public void testGetRandomAgent() throws Exception {
		
		Agents randomAgent = AgentsHelper.getRandomAgent();
		System.out.println(randomAgent.getAgentFirstName() + " " +  randomAgent.getAgentLastName());
		System.out.println(randomAgent.getAgentPassword());
	}
	
	@Test
	public void testGetAgent() throws Exception {
		
		Agents agent = AgentsHelper.getAgentByName("Doug", "Johnson");
		System.out.println(agent.getAgentUserName());
		System.out.println(agent.getAgentPassword());
	}
	
	@Test
	public void testGetAgentByUserName() throws Exception {
		
		Agents agent = AgentsHelper.getAgentByUserName("dbosworth");
		System.out.println(agent.getAgentFirstName());
		System.out.println(agent.getAgentLastName());
		System.out.println(agent.getAgentPassword());
	}
	
	@Test()
	public void testGetRandomAgent() throws Exception {
		
		AgentsView randomAgent = AgentsViewHelper.getRandomAgent();
		System.out.println(randomAgent.getAgentFirstName() + " " +  randomAgent.getAgentLastName());
		//System.out.println(randomAgent.getAgentPassword());
	}
	
	@Test()
	public void testGetAgent() throws Exception {
		
		AgentsView agent = AgentsViewHelper.getAgentByName("Doug", "Johnson");
		System.out.println(agent.getAgentUserName());
		//System.out.println(agent.getAgentPassword());
	}
	
	@Test()
	public void testGetAgentByUserName() throws Exception {
		
		AgentsView agent = AgentsViewHelper.getAgentByUserName("dbosworth");
		System.out.println(agent.getAgentFirstName());
		System.out.println(agent.getAgentLastName());
		//System.out.println(agent.getAgentPassword());
	}*/
}