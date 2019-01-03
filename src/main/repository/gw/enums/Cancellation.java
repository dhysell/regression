package repository.gw.enums;

public class Cancellation {
	
	public enum CancellationOptions {
		ScheduleCancel,
		CancelNow;
	}
	
	public enum WithdrawTransaction {
		WithDocuments,
		WithOUTDocuments,
	}
	
	public enum CancellationSource {
		Carrier("Carrier"),
		Member("Member"),
		Insured("Insured");
		
		String source;
		
		CancellationSource(String source) {
			this.source = source;
		}
		
		private String getSourceValueInner() {
			return this.source;
		}
	}
	
	public enum PendingCancellationSource {
		Billing("Billing"),
		Underwriting("Underwriting");
		
		String source;
		
		PendingCancellationSource(String source) {
			this.source = source;
		}
		
		private String getSourceValueInner() {
			return this.source;
		}
	}
	
	public enum CancellationStatus {
		Cancelling("Canceling"),
		Cancelled("Canceled");
		
		String status;
		
		CancellationStatus(String status) {
			this.status = status;
		}
		
		public String getValue(){
			return this.status;
		}
	}
	
	private enum CancellationSourceReason {
		ActivitiesOmissionsInsuredIncreasedHazard("activities or omissions by the insured which increase insured hazard", CancellationSource.Carrier),
		BreachPolicyTermsConditions("breach of policy terms and conditions", CancellationSource.Carrier),
		ChangesRiskMateriallyIncreasesRiskOfLoss("changes in the risk which materially increases the risk of loss", CancellationSource.Carrier),
		FraudMaterialMisrepresentation("fraud or material misrepresentation", CancellationSource.Carrier),
		LackUnderwritingInfo("lack of underwriting information", CancellationSource.Carrier),
		LossHistory("loss history", CancellationSource.Carrier),
		NonPayment("non-payment", CancellationSource.Carrier),
		NotMeetingUnderwritingRequirements("not meeting underwriting requirements", CancellationSource.Carrier),
		NSF("NSF", CancellationSource.Carrier),
		PolicyRewrittenReplacedFlatCancel("policy rewritten or replaced (flat cancel)", CancellationSource.Carrier),
		Other("other", CancellationSource.Carrier),
		ReinsuranceRestrictions("reinsurance restrictions", CancellationSource.Carrier),
		RenumberingToAnotherPolicy("renumbering to another policy", CancellationSource.Carrier),
		UnacceptableLiabilityExposure("unacceptable liability exposure", CancellationSource.Carrier),
		UnacceptablePropertyExposure("unacceptable property exposure", CancellationSource.Carrier),
		InsuredRequest("insured request", CancellationSource.Insured),
		PolicyNotTaken("policy not-taken", CancellationSource.Insured),
		PolicyNonRenewed("policy non-renewed", CancellationSource.Carrier),
		RewrittenToAnotherPolicy("rewritten to another policy", CancellationSource.Insured),
		MemberRequest("Member Request", CancellationSource.Member),
		OtherForMembership("other", CancellationSource.Member);
		
		String reason;
		CancellationSource source;
		
		CancellationSourceReason(String reason, CancellationSource source) {
			this.reason = reason;
			this.source = source;
		}
		
		private String getReasonValueInner() {
			return this.reason;
		}
		
		private CancellationSource getSourceInner() {
			return this.source;
		}
	}
	
	private enum PendingCancellationSourceReason {
		ActivitiesOmissionsInsuredIncreasedHazard("activities or omissions by the insured which increase insured hazard", PendingCancellationSource.Underwriting),
		BreachPolicyTermsConditions("breach of policy terms and conditions", PendingCancellationSource.Underwriting),
		ChangesRiskMateriallyIncreasesRiskOfLoss("changes in the risk which materially increases the risk of loss", PendingCancellationSource.Underwriting),
		FraudMaterialMisrepresentation("fraud or material misrepresentation", PendingCancellationSource.Underwriting),
		LackUnderwritingInfo("lack of underwriting information", PendingCancellationSource.Underwriting),
		LossHistory("loss history", PendingCancellationSource.Underwriting),
		NonPayment("non-payment", PendingCancellationSource.Billing),
		NotMeetingUnderwritingRequirements("not meeting underwriting requirements", PendingCancellationSource.Underwriting),
		NSF("NSF", PendingCancellationSource.Billing),
		PolicyRewrittenReplacedFlatCancel("policy rewritten or replaced (flat cancel)", PendingCancellationSource.Underwriting),
		ReinsuranceRestrictions("reinsurance restrictions", PendingCancellationSource.Underwriting),
		UnacceptableLiabilityExposure("unacceptable liability exposure", PendingCancellationSource.Underwriting),
		UnacceptablePropertyExposure("unacceptable property exposure", PendingCancellationSource.Underwriting);
		
		String reason;
		PendingCancellationSource source;
		
		PendingCancellationSourceReason(String reason, PendingCancellationSource source) {
			this.reason = reason;
			this.source = source;
		}
		
		private String getReasonValueInner() {
			return this.reason;
		}
		
		private PendingCancellationSource getSourceInner() {
			return this.source;
		}
	}
		
	public enum CancellationSourceReasonExplanation {
//		CitationsOfListedDriver_NONRenew("citations of listed driver(s)", CancellationSourceReason.PolicyNonRenewed),
//		CondemnedOrUnsafePremises("condemned or unsafe premises", CancellationSourceReason.PolicyNonRenewed),
//		OneOrMoreListedDriverHavingASuspendedDriversLicense("one or more listed driver(s) having a suspended driver's license", CancellationSourceReason.PolicyNonRenewed),
//		PoorLossHistory_NONRenew("poor loss history", CancellationSourceReason.PolicyNonRenewed),
//		PropertyBeingVacantOrBelowOccupancyLimit("property being vacant or below occupancy limit", CancellationSourceReason.PolicyNonRenewed),
//		SubstantialChangeInRiskOrIncreaseInHazard("substantial change in risk or increase in hazard", CancellationSourceReason.PolicyNonRenewed),
//		SuspensionOrRevocationOfLicenseOrPermits("suspension or revocation of license or permits", CancellationSourceReason.PolicyNonRenewed),
//		ThisAccountNotMeetingOurEligibilityRequirements_NONRenew("this account not meeting our eligibility requirements", CancellationSourceReason.PolicyNonRenewed),
		
		FailureToComplySafetyRecommendations("failure to comply with safety recommendations", CancellationSourceReason.ActivitiesOmissionsInsuredIncreasedHazard),
		OtherActivitiesOmissionsInsIncreaseHaz("other", CancellationSourceReason.ActivitiesOmissionsInsuredIncreasedHazard),
		ViolationOfHealthSafetyFireCodes("violation of health, safety, or fire codes", CancellationSourceReason.ActivitiesOmissionsInsuredIncreasedHazard),
		FailureToComplyWithTermsConditions("failure to comply with terms and conditions", CancellationSourceReason.BreachPolicyTermsConditions),
		NotReportingPayrollGrossSales("not reporting payroll or gross sales", CancellationSourceReason.BreachPolicyTermsConditions),
		CondemnedUnsafe("condemned or unsafe premises", CancellationSourceReason.ChangesRiskMateriallyIncreasesRiskOfLoss),
		CondemnedUnsafePremises("condemned or unsafe premises", CancellationSourceReason.PolicyNonRenewed),
		SubstantialChangeRiskIncreaseInHazard("substantial change in risk or increase in hazard", CancellationSourceReason.ChangesRiskMateriallyIncreasesRiskOfLoss),
		SuspensionRevocationLicensePermitsChangeRiskMaterially("suspension or revocation of license or permits", CancellationSourceReason.ChangesRiskMateriallyIncreasesRiskOfLoss),
		VacantBelowOccupancyLimitsChangeRiskMaterially("property being vacant or below occupancy limit", CancellationSourceReason.ChangesRiskMateriallyIncreasesRiskOfLoss),
		CriminalConductByInsured("criminal conduct by insured", CancellationSourceReason.FraudMaterialMisrepresentation),
		FraudMaterialMisrepresentation("fraud or material misrepresentation", CancellationSourceReason.FraudMaterialMisrepresentation),
		MoralRisk("moral risk", CancellationSourceReason.FraudMaterialMisrepresentation),
		CompletedApplication("not receiving completed endorsement(s) required to process this policy", CancellationSourceReason.LackUnderwritingInfo),
		DriversLicenseInfo("not receiving current driver's license information for one or more driver(s) listed on the policy", CancellationSourceReason.LackUnderwritingInfo),
		LossRuns("not receiving acceptable loss runs", CancellationSourceReason.LackUnderwritingInfo),
		OtherLackUWInfo("other", CancellationSourceReason.LackUnderwritingInfo),
		NotReceivingRequiredJewelryAppraisalAndOrPhoto("not receiving required jewelry appraisal(s) and/or photo(s)", CancellationSourceReason.LackUnderwritingInfo),
		OneOrMoreDriverListedOnThePolicyWithoutAnIdahoDriversLicense("one or more driver(s) listed on the policy without an Idaho driver's license", CancellationSourceReason.LackUnderwritingInfo),
		Photos("not receiving photos", CancellationSourceReason.LackUnderwritingInfo),
		ReplacementCostEstimator("not receiving replacement cost estimator/Marshall & Swift", CancellationSourceReason.LackUnderwritingInfo),
		Signature("not receiving insured's signature", CancellationSourceReason.LackUnderwritingInfo),
		PoorLossHistory("poor loss history", CancellationSourceReason.LossHistory),
		NoPaymentReceived("no payment received", CancellationSourceReason.NonPayment),
		CitationsOfListedDriver("citations of listed driver(s)", CancellationSourceReason.NotMeetingUnderwritingRequirements),
		OneOrMoreListedDriverHavingASuspendedriversLicense("one or more listed driver(s) having a suspended driver's license", CancellationSourceReason.NotMeetingUnderwritingRequirements),
		ThisAccountNotMeetingOurEligibilityRequirements("this account not meeting our eligibility requirements", CancellationSourceReason.NotMeetingUnderwritingRequirements),
		AccountClosed("account closed", CancellationSourceReason.NSF),
		AccountFrozen("account frozen", CancellationSourceReason.NSF),
		InsufficientFunds("insufficient funds", CancellationSourceReason.NSF),
		InvalidAccount("invalid account", CancellationSourceReason.NSF),
		OtherNSF("other", CancellationSourceReason.NSF),
		PaymentStopped("payment stopped", CancellationSourceReason.NSF),
		AuthorizationRevoked("authorization revoked", CancellationSourceReason.NSF),		
		
		NoAccountUnableToLocateAccount("No Account/ Unable to locate account", CancellationSourceReason.NSF),
		UnauthorizedDebit("Unauthorized Debit", CancellationSourceReason.NSF),
		UncollectedFunds("Uncollected Funds", CancellationSourceReason.NSF),
		CustomerAdvisesNotAuthorized("Customer advises not authorized", CancellationSourceReason.NSF),
		CheckTruncationEntryReturnStaleDate("Check truncation entry return/ stale date", CancellationSourceReason.NSF),
		BranchSoldToAnotherDFI("Branch sold to another DFI", CancellationSourceReason.NSF),
		AccountHolderDeceased("Account-holder Deceased", CancellationSourceReason.NSF),
		BeneficiaryDeceased("Beneficiary Deceased", CancellationSourceReason.NSF),
		NonTransactionAccount("Non-transaction account", CancellationSourceReason.NSF),
		CorporateCustomerAdvisesNotAuthorized("Corporate customer advises not authorized", CancellationSourceReason.NSF),	
		InvalidRoutingNumber("Invalid Routing Number", CancellationSourceReason.NSF),
		
		ExcessiveErrors("excessive errors", CancellationSourceReason.PolicyRewrittenReplacedFlatCancel),
		OtherPolicyRewrittenOrReplaced("other", CancellationSourceReason.PolicyRewrittenReplacedFlatCancel),
		OtherOther("other", CancellationSourceReason.Other),
		Rewritten("this policy being rewritten or replaced", CancellationSourceReason.PolicyRewrittenReplacedFlatCancel),
		LossOrDecreaseInReinsurance("loss or decrease in reinsurance", CancellationSourceReason.ReinsuranceRestrictions),
		ReinsuranceExclusion("reinsurance exclusion", CancellationSourceReason.ReinsuranceRestrictions),
		NameChange("name change", CancellationSourceReason.RenumberingToAnotherPolicy),
		MovedOutOfStateUnacceptableLiabilityExposure("insured having moved out of state", CancellationSourceReason.UnacceptableLiabilityExposure),
		OperationDoesNotQuality("business operations no longer qualifying", CancellationSourceReason.UnacceptableLiabilityExposure),
		OperationsOutOfOperatingTerritory("business operations being out of our operating territory", CancellationSourceReason.UnacceptableLiabilityExposure),
		OtherUnacceptableLiabilityExposure("other", CancellationSourceReason.UnacceptableLiabilityExposure),
		SuspensionRevocationLicensePermitsUnacceptableLiability("suspension or revocation of license or permits", CancellationSourceReason.UnacceptableLiabilityExposure),
		CondemnedOrUnsafe("condemned or unsafe premises", CancellationSourceReason.UnacceptablePropertyExposure),
		ConditionOfProperty("condition of property", CancellationSourceReason.UnacceptablePropertyExposure),
		OtherUnacceptableProperty("other", CancellationSourceReason.UnacceptablePropertyExposure),
		UseOfProperty("the usage of the property", CancellationSourceReason.UnacceptablePropertyExposure),
		VacantBelowOccupancyLimitsChangeRiskMateriallyUnaccetablePropertyExposure("property being vacant or below occupancy limit", CancellationSourceReason.UnacceptablePropertyExposure),
		CoverageNotAvailable("coverage not available", CancellationSourceReason.InsuredRequest),
		DuplicateCoverage("duplicate coverage", CancellationSourceReason.InsuredRequest),
		Foreclosure("foreclosure", CancellationSourceReason.InsuredRequest),
		InsuredPassedAway("insured passed away", CancellationSourceReason.InsuredRequest),
		MovedOutOfStateInsuredRequest("insured having moved out of state", CancellationSourceReason.InsuredRequest),
		NoEmployeesOperations("no employees/operations", CancellationSourceReason.InsuredRequest),
		NoReasonGiven("no reason given", CancellationSourceReason.InsuredRequest),
		OtherInsuredRequest("other", CancellationSourceReason.InsuredRequest),
		OutOfBusinessSold("out of business/sold ", CancellationSourceReason.InsuredRequest),
//		PolicyNotRenewed("policy not renewed", CancellationSourceReason.InsuredRequest),
		PricedToHigh("priced too high", CancellationSourceReason.InsuredRequest),
		Service("service", CancellationSourceReason.InsuredRequest),
		WentWithAnotherCarrier("went with another carrier", CancellationSourceReason.InsuredRequest),
		SubmittedOnAccident("submitted on accident", CancellationSourceReason.PolicyNotTaken),
		CloseDidNotHappen("close did not happen", CancellationSourceReason.PolicyNotTaken),
		ClosedPreviousToCancelEffDate("closed previous to cancel effective date", CancellationSourceReason.PolicyNotTaken),
		OtherPolicyNotTaken("other", CancellationSourceReason.InsuredRequest),
		PolicyNotTaken("policy not-taken", CancellationSourceReason.InsuredRequest),
		Brokerage("Brokerage", CancellationSourceReason.RewrittenToAnotherPolicy),
		FarmBureau("Farm Bureau", CancellationSourceReason.RewrittenToAnotherPolicy),
		WesternCommunity("Western Community", CancellationSourceReason.RewrittenToAnotherPolicy),
		MembershipNoReasonGiven("no reason given", CancellationSourceReason.MemberRequest),
		MembershipOtherMemberRequest("other", CancellationSourceReason.MemberRequest);
		
		String explanation;
		CancellationSourceReason reason;
		
		CancellationSourceReasonExplanation(String explanation, CancellationSourceReason reason) {
			this.explanation = explanation;
			this.reason = reason;
		}
		
		public String getExplanationValue() {
			return this.explanation;
		}
		
		public String getReasonValue() {
			return this.reason.getReasonValueInner();
		}
		
		public String getSourceValue() {
			return this.reason.getSourceInner().getSourceValueInner();
		}
	}
	
	public enum PendingCancellationSourceReasonExplanation {
//		CitationsOfListedDriver_NONRenew("citations of listed driver(s)", CancellationSourceReason.PolicyNonRenewed),
//		CondemnedOrUnsafePremises("condemned or unsafe premises", CancellationSourceReason.PolicyNonRenewed),
//		OneOrMoreListedDriverHavingASuspendedDriversLicense("one or more listed driver(s) having a suspended driver's license", CancellationSourceReason.PolicyNonRenewed),
//		PoorLossHistory_NONRenew("poor loss history", CancellationSourceReason.PolicyNonRenewed),
//		PropertyBeingVacantOrBelowOccupancyLimit("property being vacant or below occupancy limit", CancellationSourceReason.PolicyNonRenewed),
//		SubstantialChangeInRiskOrIncreaseInHazard("substantial change in risk or increase in hazard", CancellationSourceReason.PolicyNonRenewed),
//		SuspensionOrRevocationOfLicenseOrPermits("suspension or revocation of license or permits", CancellationSourceReason.PolicyNonRenewed),
//		ThisAccountNotMeetingOurEligibilityRequirements_NONRenew("this account not meeting our eligibility requirements", CancellationSourceReason.PolicyNonRenewed),
		
		FailureToComplySafetyRecommendations("failure to comply with safety recommendations", PendingCancellationSourceReason.ActivitiesOmissionsInsuredIncreasedHazard),
		OtherActivitiesOmissionsInsIncreaseHaz("other", PendingCancellationSourceReason.ActivitiesOmissionsInsuredIncreasedHazard),
		ViolationOfHealthSafetyFireCodes("violation of health, safety, or fire codes", PendingCancellationSourceReason.ActivitiesOmissionsInsuredIncreasedHazard),
		FailureToComplyWithTermsConditions("failure to comply with terms and conditions", PendingCancellationSourceReason.BreachPolicyTermsConditions),
		NotReportingPayrollGrossSales("not reporting payroll or gross sales", PendingCancellationSourceReason.BreachPolicyTermsConditions),
		CondemnedUnsafe("condemned or unsafe premises", PendingCancellationSourceReason.ChangesRiskMateriallyIncreasesRiskOfLoss),
		SubstantialChangeRiskIncreaseInHazard("substantial change in risk or increase in hazard", PendingCancellationSourceReason.ChangesRiskMateriallyIncreasesRiskOfLoss),
		SuspensionRevocationLicensePermitsChangeRiskMaterially("suspension or revocation of license or permits", PendingCancellationSourceReason.ChangesRiskMateriallyIncreasesRiskOfLoss),
		VacantBelowOccupancyLimitsChangeRiskMaterially("property being vacant or below occupancy limit", PendingCancellationSourceReason.ChangesRiskMateriallyIncreasesRiskOfLoss),
		CriminalConductByInsured("criminal conduct by insured", PendingCancellationSourceReason.FraudMaterialMisrepresentation),
		FraudMaterialMisrepresentation("fraud or material misrepresentation", PendingCancellationSourceReason.FraudMaterialMisrepresentation),
		MoralRisk("moral risk", PendingCancellationSourceReason.FraudMaterialMisrepresentation),
		CompletedApplication("not receiving completed endorsement(s) required to process this policy", PendingCancellationSourceReason.LackUnderwritingInfo),
		DriversLicenseInfo("not receiving current driver's license information for one or more driver(s) listed on the policy", PendingCancellationSourceReason.LackUnderwritingInfo),
		LossRuns("not receiving acceptable loss runs", PendingCancellationSourceReason.LackUnderwritingInfo),
		OtherLackUWInfo("other", PendingCancellationSourceReason.LackUnderwritingInfo),
		NotReceivingRequiredJewelryAppraisalAndOrPhoto("not receiving required jewelry appraisal(s) and/or photo(s)", PendingCancellationSourceReason.LackUnderwritingInfo),
		OneOrMoreDriverListedOnThePolicyWithoutAnIdahoDriversLicense("one or more driver(s) listed on the policy without an Idaho driver's license", PendingCancellationSourceReason.LackUnderwritingInfo),
		Photos("not receiving photos", PendingCancellationSourceReason.LackUnderwritingInfo),
		ReplacementCostEstimator("not receiving replacement cost estimator/Marshall & Swift", PendingCancellationSourceReason.LackUnderwritingInfo),
		Signature("not receiving insured’s signature", PendingCancellationSourceReason.LackUnderwritingInfo),
		PoorLossHistory("poor loss history", PendingCancellationSourceReason.LossHistory),
		NoPaymentReceived("no payment received", PendingCancellationSourceReason.NonPayment),
		CitationsOfListedDriver("citations of listed driver(s)", PendingCancellationSourceReason.NotMeetingUnderwritingRequirements),
		OneOrMoreListedDriverHavingASuspendedriversLicense("one or more listed driver(s) having a suspended driver's license", PendingCancellationSourceReason.NotMeetingUnderwritingRequirements),
		ThisAccountNotMeetingOurEligibilityRequirements("this account not meeting our eligibility requirements", PendingCancellationSourceReason.NotMeetingUnderwritingRequirements),
		AccountClosed("account closed", PendingCancellationSourceReason.NSF),
		AccountFrozen("account frozen", PendingCancellationSourceReason.NSF),
		InsufficientFunds("insufficient funds", PendingCancellationSourceReason.NSF),
		InvalidAccount("invalid account", PendingCancellationSourceReason.NSF),
		OtherNSF("other", PendingCancellationSourceReason.NSF),
		PaymentStopped("payment stopped", PendingCancellationSourceReason.NSF),
		AuthorizationRevoked("authorization revoked", PendingCancellationSourceReason.NSF),
		ExcessiveErrors("excessive errors", PendingCancellationSourceReason.PolicyRewrittenReplacedFlatCancel),
		OtherPolicyRewrittenOrReplaced("other", PendingCancellationSourceReason.PolicyRewrittenReplacedFlatCancel),
		Rewritten("this policy being rewritten or replaced", PendingCancellationSourceReason.PolicyRewrittenReplacedFlatCancel),
		LossOrDecreaseInReinsurance("loss or decrease in reinsurance", PendingCancellationSourceReason.ReinsuranceRestrictions),
		ReinsuranceExclusion("reinsurance exclusion", PendingCancellationSourceReason.ReinsuranceRestrictions),
		MovedOutOfStateUnacceptableLiabilityExposure("insured having moved out of state", PendingCancellationSourceReason.UnacceptableLiabilityExposure),
		OperationDoesNotQuality("business operations no longer qualifying", PendingCancellationSourceReason.UnacceptableLiabilityExposure),
		OperationsOutOfOperatingTerritory("business operations being out of our operating territory", PendingCancellationSourceReason.UnacceptableLiabilityExposure),
		OtherUnacceptableLiabilityExposure("other", PendingCancellationSourceReason.UnacceptableLiabilityExposure),
		SuspensionRevocationLicensePermitsUnacceptableLiability("suspension or revocation of license or permits", PendingCancellationSourceReason.UnacceptableLiabilityExposure),
		CondemnedOrUnsafe("condemned or unsafe premises", PendingCancellationSourceReason.UnacceptablePropertyExposure),
		ConditionOfProperty("condition of property", PendingCancellationSourceReason.UnacceptablePropertyExposure),
		OtherUnacceptableProperty("other", PendingCancellationSourceReason.UnacceptablePropertyExposure),
		UseOfProperty("the usage of the property", PendingCancellationSourceReason.UnacceptablePropertyExposure),
		VacantBelowOccupancyLimitsChangeRiskMateriallyUnaccetablePropertyExposure("property being vacant or below occupancy limit", PendingCancellationSourceReason.UnacceptablePropertyExposure);
		
		String explanation;
		PendingCancellationSourceReason reason;
		
		PendingCancellationSourceReasonExplanation(String explanation, PendingCancellationSourceReason reason) {
			this.explanation = explanation;
			this.reason = reason;
		}
		
		public String getExplanationValue() {
			return this.explanation;
		}
		
		public String getReasonValue() {
			return this.reason.getReasonValueInner();
		}
		
		public String getSourceValue() {
			return this.reason.getSourceInner().getSourceValueInner();
		}
	}
		
		//5-31-16 Steve Broderick added cancellation Effective Date Field
		// if Cancellation Effective Date Field is -1 then the inception date of the policy should be the effective Date.
		// if Cancellation Effective Date Field is -2 then no default.
	public enum CancellationSourceReasonPL {
		ChangesRiskMateriallyIncreasesRiskOfLoss("Changes in the risk which materially increases the risk of loss", CancellationSource.Carrier, 30),
		FraudMaterialMisrepresentation("Fraud or material misrepresentation", CancellationSource.Carrier, 30),
		LackUnderwritingInfo("Lack of underwriting information", CancellationSource.Carrier, 30),
		LossHistory("loss history", CancellationSource.Carrier, 30),
		NonPayment("Non-Payment", CancellationSource.Carrier, 20),
		NotMeetingUnderwritingRequirements("Not meeting underwriting requirements", CancellationSource.Carrier, 30),
		NSF("NSF", CancellationSource.Carrier, 20),
		PolicyRewrittenReplacedFlatCancel("Policy rewritten or replaced (flat cancel)", CancellationSource.Carrier, 0),
		RenumberingToAnotherPolicy("Renumbering to another Policy", CancellationSource.Carrier, -1),
		UnacceptableLiabilityExposure("Unacceptable liability exposure", CancellationSource.Carrier, 30),
		UnacceptablePropertyExposure("Unacceptable property exposure", CancellationSource.Carrier, 30),
		WeHaveNotReceivedCompletedEndorsementsRequiredToProcessPolicy("We have not received completed endorsements required to process your policy", CancellationSource.Carrier, 30),
		WeHaveNotReceivedCurrentDriversLicenseInformationForListedDrivers("We have not received current driver's license information for listed driver(s)", CancellationSource.Carrier, 30),
		OneOrMoreListedDriversHaveNotObtainedAnIdahoDriversLicense("One or more listed drivers have not obtained an Idaho driver’s license", CancellationSource.Carrier, 30),
		CitationsOfListedDrivers("Citations of listed drivers", CancellationSource.Carrier, 30),
		OneOrMoreListedDriversHasASuspendedDriversLicense("One or more listed drivers has a suspended driver’s license",CancellationSource.Carrier, 30),
		WeHaveNotReceivedACurrentJewelryAppraisalOnAllJewelryWeInsure("We have not received a current jewelry appraisal on all jewelry we insure",CancellationSource.Carrier, 30),
		TheAccountDoesNotMeetOurEligibilityRequirements("The account does not meet our eligibility requirements",CancellationSource.Carrier, 30),
		InsuredRequest("Insured Request", CancellationSource.Insured, 0),
		PolicyNotTaken("Policy not-taken", CancellationSource.Insured, 0),
		RewrittenToAnotherPolicy("Rewritten to another policy", CancellationSource.Insured, 0);
		
		String reason;
		CancellationSource source;
		int days;
		
		CancellationSourceReasonPL(String reason, CancellationSource source, int days) {
			this.reason = reason;
			this.source = source;
			this.days = days;
		}
		
		public String getReasonValueInner() {
			return this.reason;
		}
		
		public CancellationSource getSourceInner() {
			return this.source;
		}
				
		public int getDays(){
			return this.days;
		}
	}
	
	public enum CancellationSourceReasonExplanationPL {
		CondemnedUnsafeChangesInRisk("Condemned or unsafe", CancellationSourceReasonPL.ChangesRiskMateriallyIncreasesRiskOfLoss),
		SubstantialChangeRiskIncreaseInHazard("Substantial change in risk or increase in hazard", CancellationSourceReasonPL.ChangesRiskMateriallyIncreasesRiskOfLoss),
//		SuspensionRevocationLicensePermitsChangeRiskMaterially("Suspension or revocation of license or permits", CancellationSourceReasonPL.ChangesRiskMateriallyIncreasesRiskOfLoss),
		VacantBelowOccupancyLimitsChangeRiskMaterially("Vacant; below occupancy limits", CancellationSourceReasonPL.ChangesRiskMateriallyIncreasesRiskOfLoss),
		CriminalConductByInsured("Criminal conduct by insured", CancellationSourceReasonPL.FraudMaterialMisrepresentation),
		FraudMaterialMisrepresentation("Fraud or material misrepresentation", CancellationSourceReasonPL.FraudMaterialMisrepresentation),
		MoralRisk("Moral risk", CancellationSourceReasonPL.FraudMaterialMisrepresentation),
		CompletedApplication("Completed application", CancellationSourceReasonPL.LackUnderwritingInfo),
		DriversLicenseInfo("Driver's license information", CancellationSourceReasonPL.LackUnderwritingInfo),
		LossRuns("Loss runs", CancellationSourceReasonPL.LackUnderwritingInfo),
		OtherLackUWInfo("Other", CancellationSourceReasonPL.LackUnderwritingInfo),
		Photos("Photos", CancellationSourceReasonPL.LackUnderwritingInfo),
		ReplacementCostEstimator("Replacement cost estimator", CancellationSourceReasonPL.LackUnderwritingInfo),
		Signature("Signature", CancellationSourceReasonPL.LackUnderwritingInfo),
		ExcessiveNumberOfLosses("Excessive number of losses", CancellationSourceReasonPL.LossHistory),
		PoorLossHistory("Poor loss history", CancellationSourceReasonPL.LossHistory),
		SeverityOfLoss("Severity of loss", CancellationSourceReasonPL.LossHistory),
		NoPaymentReceived("No payment received", CancellationSourceReasonPL.NonPayment),
		NoLongerEligibleForProgram("No longer eligible for program", CancellationSourceReasonPL.NotMeetingUnderwritingRequirements),
		AccountClosed("Account closed", CancellationSourceReasonPL.NSF),
		AccountFrozen("Account frozen", CancellationSourceReasonPL.NSF),
		InsufficientFunds("Insufficient funds", CancellationSourceReasonPL.NSF),
		InvalidAccount("Invalid account", CancellationSourceReasonPL.NSF),
		OtherNSF("Other", CancellationSourceReasonPL.NSF),
		PaymentStopped("Payment Stopped", CancellationSourceReasonPL.NSF),
		AuthorizationRevoked("Authorization revoked", CancellationSourceReasonPL.NSF),
		ExcessiveErrors("Excessive errors", CancellationSourceReasonPL.PolicyRewrittenReplacedFlatCancel),
		OtherPolicyRewrittenOrReplaced("Other", CancellationSourceReasonPL.PolicyRewrittenReplacedFlatCancel),
		Rewritten("Rewritten", CancellationSourceReasonPL.PolicyRewrittenReplacedFlatCancel),
		NameChange("Name Change", CancellationSourceReasonPL.RenumberingToAnotherPolicy),
		CitationsOfListedDrivers("Citations of listed drivers", CancellationSourceReasonPL.UnacceptableLiabilityExposure),
		MovedOutOfStateUnacceptableLiabilityExposure("Moved out of state", CancellationSourceReasonPL.UnacceptableLiabilityExposure),
		NewActivity("New activity", CancellationSourceReasonPL.UnacceptableLiabilityExposure),
		OtherUnacceptableLiabilityExposure("Other", CancellationSourceReasonPL.UnacceptableLiabilityExposure),
		CondemnedOrUnsafe("Condemned or unsafe", CancellationSourceReasonPL.UnacceptablePropertyExposure),
		ConditionOfProperty("Condition of property", CancellationSourceReasonPL.UnacceptablePropertyExposure),
		OtherUnacceptableProperty("Other", CancellationSourceReasonPL.UnacceptablePropertyExposure),
		UseOfProperty("Use of property", CancellationSourceReasonPL.UnacceptablePropertyExposure),
		VacantBelowOccupancyLimitsChangeRiskMateriallyUnaccetablePropertyExposure("Vacant; below occupancy limits", CancellationSourceReasonPL.UnacceptablePropertyExposure),
		ThisAccountDoesNotMeetOurEligibilityRequirements("This account does not meet our eligibility requirements",CancellationSourceReasonPL.LackUnderwritingInfo),
		WeDoNotHaveSufficientPremiumToContinueYourPolicy("We do not have sufficient premium to continue your policy",CancellationSourceReasonPL.LackUnderwritingInfo),
		MissingCompletedEndorsement("Missing completed endorsements",CancellationSourceReasonPL.WeHaveNotReceivedCompletedEndorsementsRequiredToProcessPolicy),
		DriversLicenseInformation("Driver's license information",CancellationSourceReasonPL.WeHaveNotReceivedCurrentDriversLicenseInformationForListedDrivers),
		IdahoDriversLicenseRequired("Idaho driver’s license required", CancellationSourceReasonPL.OneOrMoreListedDriversHaveNotObtainedAnIdahoDriversLicense),
		IneligibleCitations("Ineligible Citations", CancellationSourceReasonPL.CitationsOfListedDrivers),
		SuspendedInvalidDriversLicense("Suspended/Invalid driver's license",CancellationSourceReasonPL.OneOrMoreListedDriversHasASuspendedDriversLicense),
		JewelryAppraisalRequired("Jewelry appraisal required",CancellationSourceReasonPL.WeHaveNotReceivedACurrentJewelryAppraisalOnAllJewelryWeInsure),
		IneligibleAccount("Ineligible Account",CancellationSourceReasonPL.TheAccountDoesNotMeetOurEligibilityRequirements),
		CoverageNotAvailable("Coverage not available", CancellationSourceReasonPL.InsuredRequest),
		DuplicateCoverage("Duplicate coverage", CancellationSourceReasonPL.InsuredRequest),
		Foreclosure("Foreclosure", CancellationSourceReasonPL.InsuredRequest),
		InsuredPassedAway("Insured passed away", CancellationSourceReasonPL.InsuredRequest),
		MovedOutOfStateInsuredRequest("Moved out of state", CancellationSourceReasonPL.InsuredRequest),
		NoReasonGiven("No reason given", CancellationSourceReasonPL.InsuredRequest),
		OtherInsuredRequest("Other", CancellationSourceReasonPL.InsuredRequest),
		PolicyNotRenewed("Policy not renewed", CancellationSourceReasonPL.InsuredRequest),
		PricedToHigh("Priced too high", CancellationSourceReasonPL.InsuredRequest),
		Service("Service", CancellationSourceReasonPL.InsuredRequest),
		WentWithAnotherCarrier("Went with another carrier", CancellationSourceReasonPL.InsuredRequest),
		YourRequestForCancellation("Your Request for Cancellation", CancellationSourceReasonPL.InsuredRequest),
		BoundOnAccident("Bound on accident", CancellationSourceReasonPL.PolicyNotTaken),
		CloseDidNotHappen("Close did not happen", CancellationSourceReasonPL.PolicyNotTaken),
		ClosedPreviousToCancelEffDate("Closed previous to cancel effective date", CancellationSourceReasonPL.PolicyNotTaken),
		OtherPolicyNotTaken("Other", CancellationSourceReasonPL.PolicyNotTaken),
		Brokerage("Brokerage", CancellationSourceReasonPL.RewrittenToAnotherPolicy),
		FarmBureau("Farm Bureau", CancellationSourceReasonPL.RewrittenToAnotherPolicy),
		WesternCommunity("Western Community", CancellationSourceReasonPL.RewrittenToAnotherPolicy); 
		
		String explanation;
		CancellationSourceReasonPL reason;
		int days;
		
		CancellationSourceReasonExplanationPL(String explanation, CancellationSourceReasonPL reason) {
			this.explanation = explanation;
			this.reason = reason;
		}
		
		public String getExplanationValue() {
			return this.explanation;
		}
		
		public String getReasonValue() {
			return this.reason.getReasonValueInner();
		}
		
		public String getSourceValue() {
			return this.reason.getSourceInner().getSourceValueInner();
		}
	}		
	
	//This enum is used to ensure that field office personnel receive the Insured source only.
	public enum AgentCancellationSource implements GetValue{
		None("<none>"),
		Insured("Insured");

		String value;
		
		AgentCancellationSource(String _value) {
			this.value = _value;
		}
		
		
		public String getValue() {
			return this.value;
		}
	}
}
