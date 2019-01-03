package repository.gw.enums;

public enum PoliciesSearchFor {
	Cancellation("Cancellation"), 
	FinalAudit("Final Audit"), 
	Policy("Policy"), 
	PolicyChange("Policy Change"), 
	PremiumReport("Premium Report"), 
	Reinstatement("Reinstatement"), 
	Renewal("Renewal"), 
	Rewrite("Rewrite");
	
	String value;
	
	private PoliciesSearchFor(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}

}
