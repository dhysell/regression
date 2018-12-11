package repository.gw.enums;

public enum UnderwriterIssues_BOP {
	BOPBR6("Employment Practices Liability Warranty Statement IDCW 32 0001 is required.", 
			"Employment Practices Liability Warranty Statement IDCW 32 0001 is required.", 
			UnderwriterIssueType.Informational, 
			ApprovalType.NONE, 
			true, true, true, true, false, true, true, false, true,
			false, null, null),
	BOPBR7("EPLI limit must be submitted to our reinsurance carrier for consideration and pricing.", 
			"EPLI limit must be submitted to our reinsurance carrier for consideration and pricing. Complete supplemental application and submit to Underwriting.", 
			UnderwriterIssueType.BlockQuoteRelease, 
			ApprovalType.APPROVE, 
			false, true, true, true, false, true, true, false, true,
			false, "Issuance", ValidUntil.NextChange),
	BOPBR8("EPLI deductible of $10,000 or $25,000 has been chosen and must be submitted to our reinsurance carrier for consideration and pricing", 
			"Limits of $500,000 or $1,000,000 with a deductible of $10,000 or higher must be submitted to our reinsurance carrier for consideration and pricing because of Location: ${Location #}, Building: ${Building #}.", 
			UnderwriterIssueType.BlockQuoteRelease, 
			ApprovalType.APPROVE, 
			false, true, true, true, false, true, true, false, false,
			false, "Issuance", ValidUntil.NextChange),
	BOPBR9("Applicant/Insured�s business type is not eligible for EPLI coverage.", 
			"Applicant/Insured�s business type is not eligible for EPLI coverage. Contact Brokerage for coverage.", 
			UnderwriterIssueType.BlockQuoteRelease, 
			ApprovalType.APPROVE, 
			false, true, true, true, false, true, true, false, true,
			true, "Issuance", ValidUntil.Rescinded),
	BOPBR12("Limits of $500,000 or $1,000,000 with a deductible of $25,000 or higher must be submitted to our reinsurance carrier for consideration and pricing because of Location: ${Location #}, Building: ${Building #}.", 
			"Limits of $500,000 or $1,000,000 with a deductible of $25,000 or higher must be submitted to our reinsurance carrier for consideration and pricing because of Location: ${Location #}, Building: ${Building #}.", 
			UnderwriterIssueType.BlockQuote, 
			ApprovalType.APPROVE, 
			false, true, true, true, false, true, true, false, false,
			false, "Issuance", ValidUntil.NextChange),
	BOPBR13("Applicant/Insured has over 100 Full Time Employees.", "Applicant/Insured has over 100 Full Time Employees. Risk must be submitted to our reinsurance carrier for consideration and pricing. Complete supplemental application and submit to underwriting.", 
			UnderwriterIssueType.BlockQuoteRelease, 
			ApprovalType.APPROVE, 
			false, true, true, true, false, true, true, false, true,
			false, "Issuance", ValidUntil.NextChange),
	BOPBR14("Legal Services with over 100 employees are not eligible because of Location: ${Location #}, Building: ${Building #}.", 
			"Legal Services with over 100 employees are not eligible because of Location: ${Location #}, Building: ${Building #}. Please refer to Brokerage for coverage.", 
			UnderwriterIssueType.Reject, 
			ApprovalType.NONE, 
			true, true, true, true, false, true, true, false, true,
			false, null, ValidUntil.Rescinded),
	BOPBR18("Legal services are not eligible for limits in excess of $250,000 because of Location: ${Location #}, Building: ${Building #}.  Please correct or contact Brokerage for increased limits.", 
			"Legal services are not eligible for limits in excess of $250,000 because of Location: ${Location #}, Building: ${Building #}. Please correct or contact Brokerage for increased limits.", 
			UnderwriterIssueType.Reject, 
			ApprovalType.NONE, 
			true, true, true, true, false, true ,true, false, false,
			false, null, ValidUntil.Rescinded),
	BOPBR19("Adding Employment Practices Liability coverage within six months of the policy's expiration date requires approval. Please contact underwriting", 
			"Adding Employment Practices Liability coverage within six months of the policy's expiration date requires approval. Please complete and upload the Employment Practices Liability Warranty Statement and contact underwriting for approval.", 
			UnderwriterIssueType.BlockQuoteRelease, 
			ApprovalType.SPECIALAPPROVE, 
			false, true, true, true, false, true ,false, false, false,
			false, "Issuance", ValidUntil.Rescinded),
	
	BOPBR20("Employment Practices Liability requires final approval from our reinsurance carrier before completing.", 
			"Employment Practices Liability requires final approval from our reinsurance carrier before completing the transaction or renewal.  Please contact underwriting for approval letter.", 
			UnderwriterIssueType.BlockSubmit, 
			ApprovalType.APPROVE, 
			false, true, true, true, false, true , true, false, false,
			true, "Issuance", ValidUntil.EndOfTerm),
	
	
	
	BOPTenDayBindingAuthority("Policy Effective Date is equal to or more than your 10 day submitting authority.  If you want this date, please request approval from Underwriting", 
			"Policy Effective Date is equal to or more than your 10 day submitting authority.  If you want this date, please request approval from Underwriting", 
			UnderwriterIssueType.BlockSubmit, 
			ApprovalType.APPROVE, 
			false, true, true, true, false, true, true, false, true,
			false, null, null);
	
	
	
	
	String shortDescription;
	String longDescription;
	UnderwriterIssueType issuetype;
	ApprovalType approvalType;
	boolean quickquote;
	boolean fullapp;
	boolean issuance;
	boolean policyChange;
	boolean cancellation;
	boolean rewrite;
	boolean renewal;
	boolean reinstate;
	boolean rewriteNewAccount;
	
	boolean allowEdit;
	String through;
	ValidUntil validUntil;
	
	
	private UnderwriterIssues_BOP(String shortDescription, String longDescription, UnderwriterIssueType issuetype,
			ApprovalType approvalType, boolean quickquote, boolean fullapp, boolean issuance, boolean policyChange,
			boolean cancellation, boolean rewrite, boolean renewal, boolean reinstate, boolean rewriteNewAccount, boolean allowEdit, String through, ValidUntil validUntil) {
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
		this.issuetype = issuetype;
		this.approvalType = approvalType;
		this.quickquote = quickquote;
		this.fullapp = fullapp;
		this.issuance = issuance;
		this.policyChange = policyChange;
		this.cancellation = cancellation;
		this.rewrite = rewrite;
		this.renewal = renewal;
		this.reinstate = reinstate;
		this.rewriteNewAccount = rewriteNewAccount;
	}


	public String getShortDescription() {
		return shortDescription;
	}


	public String getLongDescription() {
		return longDescription;
	}


	public UnderwriterIssueType getIssuetype() {
		return issuetype;
	}


	public ApprovalType getApprovalType() {
		return approvalType;
	}


	public boolean isQuickquote() {
		return quickquote;
	}


	public boolean isFullapp() {
		return fullapp;
	}


	public boolean isIssuance() {
		return issuance;
	}


	public boolean isPolicyChange() {
		return policyChange;
	}


	public boolean isCancellation() {
		return cancellation;
	}


	public boolean isRewrite() {
		return rewrite;
	}


	public boolean isRenewal() {
		return renewal;
	}


	public boolean isReinstate() {
		return reinstate;
	}


	public boolean isRewriteNewAccount() {
		return rewriteNewAccount;
	}
}
