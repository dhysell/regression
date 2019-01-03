package repository.gw.enums;

public enum PolicySearchPolicyBillingMethod {
	None("<none>"), 
	Direct_Bill("Direct Bill"),
	List_Bill("List Bill");
	String value;
	
	private PolicySearchPolicyBillingMethod(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
