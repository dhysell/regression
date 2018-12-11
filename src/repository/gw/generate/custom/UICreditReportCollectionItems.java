package repository.gw.generate.custom;

import java.util.Date;

public class UICreditReportCollectionItems {

	private String grantorName;
	private long originalAmount;
	private long balanceAmount;
	private Date dateReported;
	
	public String getGrantorName() {
		return grantorName;
	}
	
	public void setGrantorName(String grantorName) {
		this.grantorName = grantorName;
	}

	public long getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(long originalAmount) {
		this.originalAmount = originalAmount;
	}

	public long getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(long balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public Date getDateReported() {
		return dateReported;
	}

	public void setDateReported(Date dateReported) {
		this.dateReported = dateReported;
	}
	
}
