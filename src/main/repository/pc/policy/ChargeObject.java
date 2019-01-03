package repository.pc.policy;
public class ChargeObject {
	
	
	private double lineAmount;
	
	private String payer;
	private String loanNumber;
	private String oldLoanNumber;
	
	private String linePayer;
	
	private String chargeGroup;
	private String linkedPayer;
	private String chargePattern;
	private String earned;
	private String unearned;
	
	
	
	public ChargeObject(double lineAmount, String linePayer) {
		this.lineAmount = lineAmount;
		this.linePayer = linePayer;
	}
	
	public ChargeObject(double lineAmount, String payer, String loanNumber, String oldLoanNumber, String linePayer,
			String chargeGroup, String linkedPayer, String chargePattern, String earned, String unearned) {
		super();
		this.lineAmount = lineAmount;
		this.payer = payer;
		this.loanNumber = loanNumber;
		this.oldLoanNumber = oldLoanNumber;
		this.linePayer = linePayer;
		this.chargeGroup = chargeGroup;
		this.linkedPayer = linkedPayer;
		this.chargePattern = chargePattern;
		this.earned = earned;
		this.unearned = unearned;
	}

	public void setLineAmount(double lineAmount) {
		this.lineAmount = lineAmount;
	}
	
	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public String getLoanNumber() {
		return loanNumber;
	}

	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}

	public String getOldLoanNumber() {
		return oldLoanNumber;
	}

	public void setOldLoanNumber(String oldLoanNumber) {
		this.oldLoanNumber = oldLoanNumber;
	}

	public String getChargeGroup() {
		return chargeGroup;
	}

	public void setChargeGroup(String chargeGroup) {
		this.chargeGroup = chargeGroup;
	}

	public String getLinkedPayer() {
		return linkedPayer;
	}

	public void setLinkedPayer(String linkedPayer) {
		this.linkedPayer = linkedPayer;
	}

	public String getChargePattern() {
		return chargePattern;
	}

	public void setChargePattern(String chargePattern) {
		this.chargePattern = chargePattern;
	}

	public String getEarned() {
		return earned;
	}

	public void setEarned(String earned) {
		this.earned = earned;
	}

	public String getUnearned() {
		return unearned;
	}

	public void setUnearned(String unearned) {
		this.unearned = unearned;
	}

	public double getLineAmount() {
		return lineAmount;
	}
	
	public void setLinePayer(String linePayer) {
		this.linePayer = linePayer;
	}
	
	public String getLinePayer() {
		return linePayer;
	}
}
