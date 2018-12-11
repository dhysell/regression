package repository.gw.enums;

public enum IssuanceType {
	FollowedByPolicyChange("Followed by a Policy Change"), 
	FollowedByPolicyCancel("Followed by a Policy Cancel"), 
	NoActionRequired("No Action Required"),
	Issue("Issue");
	String value;
	
	private IssuanceType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
