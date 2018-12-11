package persistence.globaldatarepo.entities;

// Generated May 4, 2016 3:33:37 PM by Hibernate Tools 4.0.0

import javax.persistence.*;

/**
 * Gluwissues generated by hbm2java
 */
@Entity
@Table(name = "GLUWIssues", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class GLUWIssues {

	private int id;
	private String ruleCondition;
	private String ruleMessage;
	private String ruleToApply;
	private String qq;
	private String fa;
	private String issuance;
	private String policyChange;
	private String cancelation;
	private String rewrite;
	private String renewal;
	private String reinstate;
	private String rewriteNewAccount;
	private String resultAndBlockingPoints;

	public GLUWIssues() {
	}

	public GLUWIssues(int id) {
		this.id = id;
	}

	public GLUWIssues(int id, String ruleCondition, String ruleMessage, String ruleToApply, String qq, String fa, String issuance, String policyChange, String cancelation, String rewrite, String renewal, String reinstate, String rewriteNewAccount, String resultAndBlockingPoints) {
		this.id = id;
		this.ruleCondition = ruleCondition;
		this.ruleMessage = ruleMessage;
		this.ruleToApply = ruleToApply;
		this.qq = qq;
		this.fa = fa;
		this.issuance = issuance;
		this.policyChange = policyChange;
		this.cancelation = cancelation;
		this.rewrite = rewrite;
		this.renewal = renewal;
		this.reinstate = reinstate;
		this.rewriteNewAccount = rewriteNewAccount;
		this.resultAndBlockingPoints = resultAndBlockingPoints;
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

	@Column(name = "RuleCondition", length = 500)
	public String getRuleCondition() {
		return this.ruleCondition;
	}

	public void setRuleCondition(String ruleCondition) {
		this.ruleCondition = ruleCondition;
	}

	@Column(name = "RuleMessage", length = 500)
	public String getRuleMessage() {
		return this.ruleMessage;
	}

	public void setRuleMessage(String ruleMessage) {
		this.ruleMessage = ruleMessage;
	}

	@Column(name = "RuleToApply", length = 500)
	public String getRuleToApply() {
		return this.ruleToApply;
	}

	public void setRuleToApply(String ruleToApply) {
		this.ruleToApply = ruleToApply;
	}

	@Column(name = "QQ", length = 500)
	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "FA", length = 500)
	public String getFa() {
		return this.fa;
	}

	public void setFa(String fa) {
		this.fa = fa;
	}

	@Column(name = "Issuance", length = 500)
	public String getIssuance() {
		return this.issuance;
	}

	public void setIssuance(String issuance) {
		this.issuance = issuance;
	}

	@Column(name = "PolicyChange", length = 500)
	public String getPolicyChange() {
		return this.policyChange;
	}

	public void setPolicyChange(String policyChange) {
		this.policyChange = policyChange;
	}

	@Column(name = "Cancelation", length = 500)
	public String getCancelation() {
		return this.cancelation;
	}

	public void setCancelation(String cancelation) {
		this.cancelation = cancelation;
	}

	@Column(name = "Rewrite", length = 500)
	public String getRewrite() {
		return this.rewrite;
	}

	public void setRewrite(String rewrite) {
		this.rewrite = rewrite;
	}

	@Column(name = "Renewal", length = 500)
	public String getRenewal() {
		return this.renewal;
	}

	public void setRenewal(String renewal) {
		this.renewal = renewal;
	}

	@Column(name = "Reinstate", length = 500)
	public String getReinstate() {
		return this.reinstate;
	}

	public void setReinstate(String reinstate) {
		this.reinstate = reinstate;
	}

	@Column(name = "RewriteNewAccount", length = 500)
	public String getRewriteNewAccount() {
		return this.rewriteNewAccount;
	}

	public void setRewriteNewAccount(String rewriteNewAccount) {
		this.rewriteNewAccount = rewriteNewAccount;
	}

	@Column(name = "ResultAndBlockingPoints", length = 500)
	public String getResultAndBlockingPoints() {
		return this.resultAndBlockingPoints;
	}

	public void setResultAndBlockingPoints(String resultAndBlockingPoints) {
		this.resultAndBlockingPoints = resultAndBlockingPoints;
	}

}
