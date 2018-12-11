package repository.gw.enums;

public enum PaymentReturnedPaymentReason {
	None("<none>"),
	AccountClosed("Account Closed"),
	AccountFrozen("Account Frozen"),
	AuthorizationRevoked("Authorization Revoked"),
	InsufficientFunds("Insufficient Funds"),
	InvalidAccount("Invalid Account"),
	OtherDidNotHonor("Other: Did not honor - please contact bank for details"),
	
	NoAccountUnableToLocateAccount("No Account/ Unable to locate account"),
	UnauthorizedDebit("Unauthorized Debit"),
	UncollectedFunds("Uncollected Funds"),
	CustomerAdvisesNotAuthorized("Customer advises not authorized"),
	CheckTruncationEntryReturnStaleDate("Check truncation entry return/ stale date"),
	BranchSoldToAnotherDFI("Branch sold to another DFI"),
	AccountHolderDeceased("Account-holder Deceased"),
	BeneficiaryDeceased("Beneficiary Deceased"),
	NonTransactionAccount("Non-transaction Account"),
	CorporateCustomerAdvisesNotAuthorized("Corporate customer advises not authorized"),
	InvalidRoutingNumber("Invalid Routing Number"),	
	Payment_Stopped("Payment Stopped");
	String value;
	
	private PaymentReturnedPaymentReason(String type){
		this.value = type;
	}
	
	public String getValue(){
		return this.value;
	}
}
