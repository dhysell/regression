package repository.gw.enums;

public enum ActivtyRequestType {
	SubmitToUnderwriting("Submit to Underwriting"), 
	SignatureNeeded("Signature Needed"), 
	RequestAdditionalInformation("Request Additional Information"), 
	PolicyChangeWithClaim("Policy Change with Claim"), 
	PleaseReturnRenewalPaperwork("Please return renewal paperwork"),
	GetMissingCore4Items("Get Missing Core 4 Items"),
	GetMissingInformation("Get Missing Information"),
	GetAutoRegistration("Get Auto Registration");
	
	String value;
	
	private ActivtyRequestType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
