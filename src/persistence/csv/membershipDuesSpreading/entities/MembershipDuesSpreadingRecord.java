package persistence.csv.membershipDuesSpreading.entities;

public class MembershipDuesSpreadingRecord {

	private String jobNumber;
	private String jobStatus;
	private String createTime;
	private String quoteTime;
	private String agentName;
	private String policyPremium;
	
	public MembershipDuesSpreadingRecord() {
		
	}
	
	public MembershipDuesSpreadingRecord(String jobNumber, String jobStatus, String createTime, String quoteTime, String agentName, String policyPremium) {
		setJobNumber(jobNumber);
		setJobStatus(jobStatus);
		setCreateTime(createTime);
		setQuoteTime(quoteTime);
		setAgentName(agentName);
		setPolicyPremium(policyPremium);
	}
	
	public boolean equals(MembershipDuesSpreadingRecord recordToCompare) {
    	return (this.getJobNumber().equals(recordToCompare.getJobNumber()) &&
    			this.getJobStatus().equals(recordToCompare.getJobStatus()) &&
    			this.getCreateTime().equals(recordToCompare.getCreateTime()) &&
    			this.getQuoteTime().equals(recordToCompare.getQuoteTime()) &&
    			this.getAgentName().equals(recordToCompare.getAgentName()) &&
    			this.getPolicyPremium().equals(recordToCompare.getPolicyPremium()));
    }
    
    

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getQuoteTime() {
		return quoteTime;
	}

	public void setQuoteTime(String quoteTime) {
		this.quoteTime = quoteTime;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getPolicyPremium() {
		return policyPremium;
	}

	public void setPolicyPremium(String policyPremium) {
		this.policyPremium = policyPremium;
	}	
}
