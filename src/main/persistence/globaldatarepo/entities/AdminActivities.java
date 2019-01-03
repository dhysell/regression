package persistence.globaldatarepo.entities;

// Generated Aug 4, 2016 3:32:24 PM by Hibernate Tools 4.0.0

import javax.persistence.*;
import java.util.Date;

/**
 * PcAdminActivities generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PC_AdminActivities", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class AdminActivities implements java.io.Serializable {

	private int id;
	private String accountNumber;
	private String agent;
	private String policyType;
	private String environment;
	private String policyStatus;
	private String requestApprovalActivity;
	private String bindActivity;
	private String documentsActivity;
	private String uwchangeActivity;
	private String assistantChangeActivity;
	private String renewalUw;
	private String csr;
	private Date agentEscalated;
	private Date csractivity;

	public AdminActivities() {
	}

	public AdminActivities(int id, String accountNumber) {
		this.id = id;
		this.accountNumber = accountNumber;
	}

	public AdminActivities(int id, String accountNumber, String agent,
			String policyType, String environment, String policyStatus,
			String requestApprovalActivity, String bindActivity,
			String documentsActivity, String uwchangeActivity,
			String assistantChangeActivity, String renewalUw, String csr,
			Date agentEscalated, Date csractivity) {
		this.id = id;
		this.accountNumber = accountNumber;
		this.agent = agent;
		this.policyType = policyType;
		this.environment = environment;
		this.policyStatus = policyStatus;
		this.requestApprovalActivity = requestApprovalActivity;
		this.bindActivity = bindActivity;
		this.documentsActivity = documentsActivity;
		this.uwchangeActivity = uwchangeActivity;
		this.assistantChangeActivity = assistantChangeActivity;
		this.renewalUw = renewalUw;
		this.csr = csr;
		this.agentEscalated = agentEscalated;
		this.csractivity = csractivity;
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

	@Column(name = "AccountNumber", nullable = false, length = 15)
	public String getAccountNumber() {
		return this.accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Column(name = "Agent", length = 50)
	public String getAgent() {
		return this.agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	@Column(name = "PolicyType", length = 50)
	public String getPolicyType() {
		return this.policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	@Column(name = "Environment", length = 50)
	public String getEnvironment() {
		return this.environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	@Column(name = "PolicyStatus", length = 50)
	public String getPolicyStatus() {
		return this.policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	@Column(name = "RequestApprovalActivity", length = 50)
	public String getRequestApprovalActivity() {
		return this.requestApprovalActivity;
	}

	public void setRequestApprovalActivity(String requestApprovalActivity) {
		this.requestApprovalActivity = requestApprovalActivity;
	}

	@Column(name = "BindActivity", length = 50)
	public String getBindActivity() {
		return this.bindActivity;
	}

	public void setBindActivity(String bindActivity) {
		this.bindActivity = bindActivity;
	}

	@Column(name = "DocumentsActivity", length = 50)
	public String getDocumentsActivity() {
		return this.documentsActivity;
	}

	public void setDocumentsActivity(String documentsActivity) {
		this.documentsActivity = documentsActivity;
	}

	@Column(name = "UWChangeActivity", length = 50)
	public String getUwchangeActivity() {
		return this.uwchangeActivity;
	}

	public void setUwchangeActivity(String uwchangeActivity) {
		this.uwchangeActivity = uwchangeActivity;
	}

	@Column(name = "AssistantChangeActivity", length = 50)
	public String getAssistantChangeActivity() {
		return this.assistantChangeActivity;
	}

	public void setAssistantChangeActivity(String assistantChangeActivity) {
		this.assistantChangeActivity = assistantChangeActivity;
	}

	@Column(name = "RenewalUW", length = 50)
	public String getRenewalUw() {
		return this.renewalUw;
	}

	public void setRenewalUw(String renewalUw) {
		this.renewalUw = renewalUw;
	}

	@Column(name = "CSR", length = 50)
	public String getCsr() {
		return this.csr;
	}

	public void setCsr(String csr) {
		this.csr = csr;
	}

	@Column(name = "AgentEscalated", length = 10)
	public Date getAgentEscalated() {
		return this.agentEscalated;
	}

	public void setAgentEscalated(Date agentEscalated) {
		this.agentEscalated = agentEscalated;
	}

	
	@Column(name = "CSRActivity", length = 23)
	public Date getCsractivity() {
		return this.csractivity;
	}

	public void setCsractivity(Date csractivity) {
		this.csractivity = csractivity;
	}

}