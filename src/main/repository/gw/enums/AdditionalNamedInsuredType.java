package repository.gw.enums;

public enum AdditionalNamedInsuredType {
	Affiliate("Affiliate"), 
	ChildWard("Child/Ward"), 
	CommonOwnership("Common Ownership"), 
	Employee("Employee"), 
	Friend("Friend"), 
	OtherFamily("Other Family"), 
	ParentGuardian("Parent/Guardian"), 
	Partner("Partner"), 
	Sibling("Sibling"), 
	Spouse("Spouse"), 
	Subsidiary("Subsidiary");
	String type;
	
	private AdditionalNamedInsuredType(String type){
		this.type = type;
	}
	
	public String getType(){
		return type;
	}
}
