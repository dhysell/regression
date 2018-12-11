package persistence.guidewire.entities;

import java.util.Date;

public class ExistingPolicyLookUp {
	
	private String accountNumber;
	private String policyNumber;
	private Integer policyPeriodTermNumber;
	private String policyLine;
	private String policyTransactionJob;
	private Date periodStart;
	private Date periodEnd;
	private Date closeDate;
	private Integer code;
	
	
	public String getaccountNumber() {
		return accountNumber;
	}
	
	public void setAccountNumber(String AccountNumber) {
		this.accountNumber = AccountNumber;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String PolicyNumber) {
		this.policyNumber = PolicyNumber;
	}

	public Integer getPolicyPeriodTermNumber() {
		return policyPeriodTermNumber;
	}

	public void setPolicyPeriodTermNumber(Integer policyPeriodTermNumber) {
		this.policyPeriodTermNumber = policyPeriodTermNumber;
	}

	public String getpolicyLine() {
		return policyLine;
	}

	public void setpolicyLine(String policyLine) {
		this.policyLine = policyLine;
	}

	public String getpolicyTransactionJob() {
		return policyTransactionJob;
	}

	public void setpolicyTransactionJob(String policyTransactionJob) {
		this.policyTransactionJob = policyTransactionJob;
	}

	
	public Date getperiodStart() {
		return periodStart;
	}

	public void setperiodEnd(Date periodEnd) {
		this.periodEnd = periodEnd;
	}

	public Date getperiodEnd() {
		return periodEnd;
	}

	public void setperiodStart(Date periodStart) {
		this.periodStart = periodStart;
	}
	public void setcloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public Date getcloseDate() {
		return closeDate;
	}
	public void setcode(Integer code) {
		this.code = code;
	}

	public Integer getcode() {
		return code;
	}
}
