package persistence.globaldatarepo.entities;

import javax.persistence.*;

@Entity
@Table(name = "GeneratedPolicies", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class GeneratedPolicies {

	private int id;
	private String agentUserName;
	private String accountNumber;
	private String policyType;
	private String environment;
	private String policyStatus;

	public GeneratedPolicies() {
	}

	public GeneratedPolicies(String agentUserName, String _accountNumber, String policyType, String environment, String status) {
		this.agentUserName = agentUserName;
		this.accountNumber = _accountNumber;
		this.policyType = policyType;
		this.environment = environment;
		this.policyStatus = status;
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "AgentUserName", nullable = false, length = 50)
	public String getAgentUserName() {
		return this.agentUserName;
	}

	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
	}

	@Column(name = "AccountNumber", nullable = false, length = 50)
	public String getAccountNumber() {
		return this.accountNumber;
	}

	public void setAccountNumber(String _accountNumber) {
		this.accountNumber = _accountNumber;
	}

	@Column(name = "PolicyType", nullable = false, length = 50)
	public String getPolicyType() {
		return this.policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	@Column(name = "Environment", nullable = false, length = 50)
	public String getEnvironment() {
		return this.environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	
	@Column(name = "PolicyStatus", nullable = true, length = 50)
	public String getPolicyStatus() {
		return this.policyStatus;
	}

	public void setPolicyStatus(String _status) {
		this.policyStatus = _status;
	}

}
