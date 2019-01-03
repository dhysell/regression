package repository.gw.generate.custom;

import java.util.Date;

public class CancellationObject {
	
	private repository.gw.enums.Cancellation.CancellationSourceReasonExplanation explanationToSelect;
	private String description;
	private Date cancellationDate;
	private boolean CancelNow;
	private String transactionNumber;
	
	
	public CancellationObject() {
	}
	
	public CancellationObject(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation explanationToSelect, String description,
                              Date cancellationDate, boolean cancelNow, String transactionNumber) {
		this.explanationToSelect = explanationToSelect;
		this.description = description;
		this.cancellationDate = cancellationDate;
		CancelNow = cancelNow;
		this.transactionNumber = transactionNumber;
	}
	
	
	public repository.gw.enums.Cancellation.CancellationSourceReasonExplanation getExplanationToSelect() {
		return explanationToSelect;
	}
	public void setExplanationToSelect(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation explanationToSelect) {
		this.explanationToSelect = explanationToSelect;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCancellationDate() {
		return cancellationDate;
	}
	public void setCancellationDate(Date cancellationDate) {
		this.cancellationDate = cancellationDate;
	}
	public boolean isCancelNow() {
		return CancelNow;
	}
	public void setCancelNow(boolean cancelNow) {
		CancelNow = cancelNow;
	}
	public String getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	
	
	
	

}
