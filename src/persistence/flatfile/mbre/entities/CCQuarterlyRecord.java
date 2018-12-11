package persistence.flatfile.mbre.entities;

public class CCQuarterlyRecord {

	private String policyNumber;
	private String claimNumber;
	private String dateOfLoss;
	private String namedInsured;
	private String totalLossAdjustmentPaid;
	private String totalLossPayment;
	
	public CCQuarterlyRecord() {
		
	}
	
	public CCQuarterlyRecord(String policyNumber, String claimNumber, String dateOfLoss, String namedInsured, String totalLossAdjustmentPaid, String totalLossPayment) {
		setPolicyNumber(policyNumber);
		setClaimNumber(claimNumber);
		setDateOfLoss(dateOfLoss);
		setNamedInsured(namedInsured);
		setTotalLossAdjustmentPaid(totalLossAdjustmentPaid);
		setTotalLossPayment(totalLossPayment);
	}
	
	public boolean equals(CCQuarterlyRecord recordToCompare) {
    	return (this.getPolicyNumber().equals(recordToCompare.getPolicyNumber()) &&
    			this.getClaimNumber().equals(recordToCompare.getClaimNumber()) &&
    			this.getDateOfLoss().equals(recordToCompare.getDateOfLoss()) &&
    			this.getNamedInsured().equals(recordToCompare.getNamedInsured()) &&
    			this.getTotalLossAdjustmentPaid().equals(recordToCompare.getTotalLossAdjustmentPaid()) &&
    			this.getTotalLossPayment().equals(recordToCompare.getTotalLossPayment()));
    }
    
    public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	
	public String getDateOfLoss() {
		return dateOfLoss;
	}

	public void setDateOfLoss(String dateOfLoss) {
		this.dateOfLoss = dateOfLoss;
	}

	public String getNamedInsured() {
		return namedInsured;
	}

	public void setNamedInsured(String namedInsured) {
		this.namedInsured = namedInsured;
	}

	public String getTotalLossAdjustmentPaid() {
		return totalLossAdjustmentPaid;
	}

	public void setTotalLossAdjustmentPaid(String totalLossAdjustmentPaid) {
		this.totalLossAdjustmentPaid = totalLossAdjustmentPaid;
	}

	public String getTotalLossPayment() {
		return totalLossPayment;
	}

	public void setTotalLossPayment(String totalLossPayment) {
		this.totalLossPayment = totalLossPayment;
	}
	
}
