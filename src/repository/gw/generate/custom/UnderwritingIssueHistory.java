package repository.gw.generate.custom;

public class UnderwritingIssueHistory {
	
	private String user;
	private String date;
	private String effectiveDate;
	private String policyTransaction;
	private String allowEdit;
	private String through;
	private String validUntil;
	private String status;
	
	public UnderwritingIssueHistory() {
	}

	public UnderwritingIssueHistory(String user, String date, String effectiveDate, String policyTransaction,
			String allowEdit, String through, String validUntil, String status) {
		this.user = user;
		this.date = date;
		this.effectiveDate = effectiveDate;
		this.policyTransaction = policyTransaction;
		this.allowEdit = allowEdit;
		this.through = through;
		this.validUntil = validUntil;
		this.status = status;
	}

	public String getUser() {
		return user;
	}

	public String getDate() {
		return date;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public String getPolicyTransaction() {
		return policyTransaction;
	}

	public String getAllowEdit() {
		return allowEdit;
	}

	public String getThrough() {
		return through;
	}

	public String getValidUntil() {
		return validUntil;
	}

	public String getStatus() {
		return status;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public void setPolicyTransaction(String policyTransaction) {
		this.policyTransaction = policyTransaction;
	}

	public void setAllowEdit(String allowEdit) {
		this.allowEdit = allowEdit;
	}

	public void setThrough(String through) {
		this.through = through;
	}

	public void setValidUntil(String validUntil) {
		this.validUntil = validUntil;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	

}
