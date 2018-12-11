package persistence.guidewire.entities;

import persistence.enums.PolicyLineType;
import persistence.enums.PolicyType;

import java.util.Date;

public class InceptionDateStuff {
	
	private String policyPeriodPrimaryNamedInsured;
	private String policyPeriodPolicyNumber;
	private Integer policyPeriodTermNumber;
	private Date policyPeriodCancellationDate;
	private Date policyCreateTime;
	private Date policyInceptionDate;
	private Date policyIssueDate;
	private PolicyType policyProductName;
	private PolicyLineType policyLineNamePolicySelection;
	private Date policyExpirationDate;
	
	public String getPolicyPeriodPrimaryNamedInsured() {
		return policyPeriodPrimaryNamedInsured;
	}
	
	public void setPolicyPeriodPrimaryNamedInsured(String policyPeriodPrimaryNamedInsured) {
		this.policyPeriodPrimaryNamedInsured = policyPeriodPrimaryNamedInsured;
	}

	public String getPolicyPeriodPolicyNumber() {
		return policyPeriodPolicyNumber;
	}

	public void setPolicyPeriodPolicyNumber(String policyPeriodPolicyNumber) {
		this.policyPeriodPolicyNumber = policyPeriodPolicyNumber;
	}

	public Integer getPolicyPeriodTermNumber() {
		return policyPeriodTermNumber;
	}

	public void setPolicyPeriodTermNumber(Integer policyPeriodTermNumber) {
		this.policyPeriodTermNumber = policyPeriodTermNumber;
	}

	public Date getPolicyPeriodCancellationDate() {
		return policyPeriodCancellationDate;
	}

	public void setPolicyPeriodCancellationDate(Date policyPeriodCancellationDate) {
		this.policyPeriodCancellationDate = policyPeriodCancellationDate;
	}

	public Date getPolicyCreateTime() {
		return policyCreateTime;
	}

	public void setPolicyCreateTime(Date policyCreateTime) {
		this.policyCreateTime = policyCreateTime;
	}

	public Date getPolicyInceptionDate() {
		return policyInceptionDate;
	}

	public void setPolicyInceptionDate(Date policyInceptionDate) {
		this.policyInceptionDate = policyInceptionDate;
	}

	public Date getPolicyIssueDate() {
		return policyIssueDate;
	}

	public void setPolicyIssueDate(Date policyIssueDate) {
		this.policyIssueDate = policyIssueDate;
	}

	public PolicyType getPolicyProductName() {
		return policyProductName;
	}

	public void setPolicyProductName(String policyProductName) {
		this.policyProductName = PolicyType.getTypeFromDBValue(policyProductName);
	}

	public PolicyLineType getPolicyLineNamePolicySelection() {
		return policyLineNamePolicySelection;
	}

	public void setPolicyLineNamePolicySelection(String policyLineNamePolicySelection) {
		if(getPolicyProductName() == PolicyType.StandardFL) {
			this.policyLineNamePolicySelection = PolicyLineType.getTypeFromDBValue(policyLineNamePolicySelection, true);
		} else {
			this.policyLineNamePolicySelection = PolicyLineType.getTypeFromDBValue(policyLineNamePolicySelection, false);
		}
		
	}

	public Date getPolicyExpirationDate() {
		return policyExpirationDate;
	}

	public void setPolicyExpirationDate(Date policyExpirationDate) {
		this.policyExpirationDate = policyExpirationDate;
	}

}
