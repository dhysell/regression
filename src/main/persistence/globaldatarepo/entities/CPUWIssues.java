package persistence.globaldatarepo.entities;
// default package
// Generated Mar 6, 2017 4:11:15 PM by Hibernate Tools 5.2.0.Beta1

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Cpuwissues generated by hbm2java
 */
@Entity
@Table(name = "CPUWIssues", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class CPUWIssues {

	private Integer id;
	private String ruleName;
	private String applyRuleTo;
	private String ruleCondition;
	private String ruleMessage;
	private String quickQuote;
	private String fullApplication;
	private String issuance;
	private String policyChange;
	private String cancellation;
	private String rewrite;
	private String renewal;
	private String reinstatement;
	private String rewriteNewAccount;
	private String audit;
	private String result;
	private String approvalButton;

	public CPUWIssues() {
	}

	public CPUWIssues(String ruleName, String applyRuleTo, String ruleCondition, String ruleMessage, String quickQuote, String fullApplication, String issuance, String policyChange, String cancellation, String rewrite, String renewal,
			String reinstatement, String rewriteNewAccount, String audit, String result, String approvalButton) {
		this.ruleName = ruleName;
		this.applyRuleTo = applyRuleTo;
		this.ruleCondition = ruleCondition;
		this.ruleMessage = ruleMessage;
		this.quickQuote = quickQuote;
		this.fullApplication = fullApplication;
		this.issuance = issuance;
		this.policyChange = policyChange;
		this.cancellation = cancellation;
		this.rewrite = rewrite;
		this.renewal = renewal;
		this.reinstatement = reinstatement;
		this.rewriteNewAccount = rewriteNewAccount;
		this.audit = audit;
		this.result = result;
		this.approvalButton = approvalButton;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "RuleName", length = 500)
	public String getRuleName() {
		return this.ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	@Column(name = "ApplyRuleTo", length = 500)
	public String getApplyRuleTo() {
		return this.applyRuleTo;
	}

	public void setApplyRuleTo(String applyRuleTo) {
		this.applyRuleTo = applyRuleTo;
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

	@Column(name = "QuickQuote", length = 500)
	public String getQuickQuote() {
		return this.quickQuote;
	}

	public void setQuickQuote(String quickQuote) {
		this.quickQuote = quickQuote;
	}

	@Column(name = "FullApplication", length = 500)
	public String getFullApplication() {
		return this.fullApplication;
	}

	public void setFullApplication(String fullApplication) {
		this.fullApplication = fullApplication;
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

	@Column(name = "Cancellation", length = 500)
	public String getCancellation() {
		return this.cancellation;
	}

	public void setCancellation(String cancellation) {
		this.cancellation = cancellation;
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

	@Column(name = "Reinstatement", length = 500)
	public String getReinstatement() {
		return this.reinstatement;
	}

	public void setReinstatement(String reinstatement) {
		this.reinstatement = reinstatement;
	}

	@Column(name = "RewriteNewAccount", length = 500)
	public String getRewriteNewAccount() {
		return this.rewriteNewAccount;
	}

	public void setRewriteNewAccount(String rewriteNewAccount) {
		this.rewriteNewAccount = rewriteNewAccount;
	}

	@Column(name = "Audit ", length = 500)
	public String getAudit() {
		return this.audit;
	}

	public void setAudit(String audit) {
		this.audit = audit;
	}

	@Column(name = "Result", length = 500)
	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(name = "ApprovalButton", length = 500)
	public String getApprovalButton() {
		return this.approvalButton;
	}

	public void setApprovalButton(String approvalButton) {
		this.approvalButton = approvalButton;
	}

}