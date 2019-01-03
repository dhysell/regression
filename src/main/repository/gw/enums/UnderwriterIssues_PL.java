package repository.gw.enums;

public enum UnderwriterIssues_PL {


	PassengerVehicleOlderThanTwentyYearsWithCompAndCollision_AU018("AUTO: The ${Vehicle description} is older than 20 years and has material damage. Underwriting approval is required to issue.(AU018)", 
			"AUTO: The ${Vehicle description} is older than 20 years and has material damage. Underwriting approval is required to issue.(AU018)", 
			UnderwriterIssueType.BlockIssuance, 
			ApprovalType.APPROVE, 
			false, true, false, true, false, true, true, false, true,
			false, null, ValidUntil.Rescinded),

	MVRforeachdriver_AU048(null, 
			"MVR not ordered for Driver ${driver no. here}. Underwriting approval required to quote policy. (AU048)", 
			UnderwriterIssueType.BlockSubmit, 
			ApprovalType.APPROVE, 
			false, true, false, true, false, true, true, false, true,
			false, null, ValidUntil.Rescinded),

	NotvalidlicenseonMVR_AU065(null, 
			"The License Status on MVR for ${Driver name here} �is not valid. Underwriting approval required to submit policy. (AU065)", 
			UnderwriterIssueType.BlockSubmit, 
			ApprovalType.APPROVE, 
			false, true, true, true, false, true, true, false, true,
			false, null, ValidUntil.EndOfTerm),

	MVRnotfound_AU066(null, 
			"The MVR for ${Driver name here} �was not found. Underwriting approval required to submit policy. (AU066)", 
			UnderwriterIssueType.BlockSubmit, 
			ApprovalType.APPROVE, 
			false, true, true, true, false, true, true, false, true,
			false, null, ValidUntil.EndOfTerm),
	
	WaivedMVRIncident_AU69("MVR incident was waived", 
			"Requested MVR incident to be waived. Underwriter will need to wave their magic wand to approve it before you can submit policy. (AU069)", 
			UnderwriterIssueType.BlockSubmit, 
			ApprovalType.APPROVE, 
			false, true, true, true, false, true, true, false, true,
			false, "Issuance", ValidUntil.Rescinded),
	
	UnableToRetrieveInsuranceScore_SQ081("Unable to retrive insurance score.  (SQ081)", 
			"An insurance score was ordered, but we received a \"no hit\" response.  First confirm all information with the insured, and then request approval from underwriting.", 
			UnderwriterIssueType.BlockQuoteRelease, 
			ApprovalType.APPROVE, 
			false, true, false, true, false, true, true, false, true,
			true, "Issuance", ValidUntil.NextChange),
	
	HighSRPDriverlessVehicle_AU087(null, 
			"Vehicle ${Vehicle # here} has SRP that is not equal to 03 on a transitioned policy. Please review the policy in OLIE. (AU087)", 
			UnderwriterIssueType.BlockQuote, 
			ApprovalType.APPROVE, 
			false, false, false, false, false, false, true, false, false,
			false, null, ValidUntil.Rescinded);



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


	private UnderwriterIssues_PL(String shortDescription, String longDescription, UnderwriterIssueType issuetype,
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
