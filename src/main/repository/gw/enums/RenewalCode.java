package repository.gw.enums;

public enum RenewalCode {
	Renew_Good_Risk("Renew - good risk"),
	Renew_Account_Consideration("Renew - account consideration"),
	Renew_Assigned_Risk("Renew - assigned risk"),
	Renew_Legal_Requirement("Renew - legal requirement"),
	Renew_Producer_Consideration("Renew - producer consideration");
	
	String value;
	
	RenewalCode(String renewalCode) {
		value = renewalCode;
	}
	
	public String getValue(){
		return value;
	}
}
