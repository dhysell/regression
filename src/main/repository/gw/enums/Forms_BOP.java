package repository.gw.enums;

public enum Forms_BOP {

	OfferToPurchaseExtendedReportingPeriodLetter("Offer To Purchase Extended Reporting Period Letter", "IDCW1100030918", "IDCW 11 0003", "09 18", false, false, false, true, false, false, false, false, false, false),
	EmploymentPracticesLiabilityInsurance("Employment Practices Liability Insurance", "IDBP3120050918", "IDBP 31 2005", "09 18", false, true, true, false, false, true, true, false, true, true),
	CommercialEmploymentPracticesLiabilityInsuranceCoverageSupplementalDeclarations("Commercial Employment Practices Liability Insurance Coverage Supplemental Declarations", "IDCW0300010918", "IDCW 03 0001", "09 18", false, true, true, false, false, true, true, false, true, true),
	EmploymentPracticesLiabilityWarrantyStatement("Employment Practices Liability Warranty Statement", "IDCW3200010918", "IDCW 32 0001", "09 18", false, true, true, false, false, true, true, false, true, true),
	BusinessownersPolicyDeclarations("Businessowners Policy Declarations", "IDBP0300010816", "IDBP 03 0001", "08 16", false, true, true, false, false, true, true, true, true, true),
	EmploymentPracticesLiabilityInsuranceSupplementalApplication("Employment Practices Liability Insurance Supplemental Application", "IDCW0100010918", "IDCW 01 0001", "09 18", false, true, true, false, false, true, true, false, true, true);
	

	String name;
	String code;
	String number;
	String edition;
	boolean submission;
	boolean issuance;
	boolean policyChange;
	boolean cancelation;
	boolean reinstate;
	boolean renewal;
	boolean rewriteFullTerm;
	boolean rewriteRemainderOfTerm;
	boolean rewriteNewTerm;
	boolean rewriteNewAccount;
	
	
	
	private Forms_BOP(String name, String code, String number, String edition, boolean submission, boolean issuance,
			boolean policyChange, boolean cancelation, boolean reinstate, boolean renewal, boolean rewriteFullTerm,
			boolean rewriteRemainderOfTerm, boolean rewriteNewTerm, boolean rewriteNewAccount) {
		this.name = name;
		this.code = code;
		this.number = number;
		this.edition = edition;
		this.submission = submission;
		this.issuance = issuance;
		this.policyChange = policyChange;
		this.cancelation = cancelation;
		this.reinstate = reinstate;
		this.rewriteFullTerm = rewriteFullTerm;
		this.rewriteRemainderOfTerm = rewriteRemainderOfTerm;
		this.rewriteNewTerm = rewriteNewTerm;
		this.rewriteNewAccount = rewriteNewAccount;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getNumber() {
		return number;
	}

	public String getEdition() {
		return edition;
	}

	public boolean isSubmission() {
		return submission;
	}

	public boolean isIssuance() {
		return issuance;
	}

	public boolean isPolicyChange() {
		return policyChange;
	}

	public boolean isCancelation() {
		return cancelation;
	}

	public boolean isReinstate() {
		return reinstate;
	}

	public boolean isRewriteFullTerm() {
		return rewriteFullTerm;
	}

	public boolean isRewriteRemainderOfTerm() {
		return rewriteRemainderOfTerm;
	}

	public boolean isRewriteNewTerm() {
		return rewriteNewTerm;
	}

	public boolean isRewriteNewAccount() {
		return rewriteNewAccount;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public void setSubmission(boolean submission) {
		this.submission = submission;
	}

	public void setIssuance(boolean issuance) {
		this.issuance = issuance;
	}

	public void setPolicyChange(boolean policyChange) {
		this.policyChange = policyChange;
	}

	public void setCancelation(boolean cancelation) {
		this.cancelation = cancelation;
	}

	public void setReinstate(boolean reinstate) {
		this.reinstate = reinstate;
	}

	public void setRewriteFullTerm(boolean rewriteFullTerm) {
		this.rewriteFullTerm = rewriteFullTerm;
	}

	public void setRewriteRemainderOfTerm(boolean rewriteRemainderOfTerm) {
		this.rewriteRemainderOfTerm = rewriteRemainderOfTerm;
	}

	public void setRewriteNewTerm(boolean rewriteNewTerm) {
		this.rewriteNewTerm = rewriteNewTerm;
	}

	public void setRewriteNewAccount(boolean rewriteNewAccount) {
		this.rewriteNewAccount = rewriteNewAccount;
	}

}
