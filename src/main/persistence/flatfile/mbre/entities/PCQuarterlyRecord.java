package persistence.flatfile.mbre.entities;

public class PCQuarterlyRecord {

	private String namedInsured;
	private String policyNumber;
	private String policyEffectiveDate;
	private String policyExpirationDate;
	private String totalAnnualPremium;
	
	public PCQuarterlyRecord() {
		
	}
	
	public PCQuarterlyRecord(String namedInsured, String policyNumber, String policyEffectiveDate, String policyExpirationDate, String totalAnnualPremium) {
		setNamedInsured(namedInsured);
		setPolicyNumber(policyNumber);
		setPolicyEffectiveDate(policyEffectiveDate);
		setPolicyExpirationDate(policyExpirationDate);
		setTotalAnnualPremium(totalAnnualPremium);
	}
	
	public boolean equals(PCQuarterlyRecord recordToCompare) {
    	return (this.getNamedInsured().equals(recordToCompare.getNamedInsured()) &&
    			this.getPolicyNumber().equals(recordToCompare.getPolicyNumber()) &&
    			this.getPolicyEffectiveDate().equals(recordToCompare.getPolicyEffectiveDate()) && 
    			this.getPolicyExpirationDate().equals(recordToCompare.getPolicyExpirationDate()) && 
    			this.getTotalAnnualPremium().equals(recordToCompare.getTotalAnnualPremium()));
    }
	
	public String getNamedInsured() {
		return namedInsured;
	}

	public void setNamedInsured(String namedInsured) {
		this.namedInsured = namedInsured;
	}
    
    public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicyEffectiveDate() {
		return policyEffectiveDate;
	}

	public void setPolicyEffectiveDate(String policyEffectiveDate) {
		this.policyEffectiveDate = policyEffectiveDate;
	}

	public String getPolicyExpirationDate() {
		return policyExpirationDate;
	}

	public void setPolicyExpirationDate(String policyExpirationDate) {
		this.policyExpirationDate = policyExpirationDate;
	}

	public String getTotalAnnualPremium() {
		return totalAnnualPremium;
	}

	public void setTotalAnnualPremium(String totalAnnualPremium) {
		this.totalAnnualPremium = totalAnnualPremium;
	}
	
}
