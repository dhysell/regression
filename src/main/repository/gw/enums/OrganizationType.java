package repository.gw.enums;

public enum OrganizationType {
	Individual("Individual", true),
	Partnership("Partnership", true), 
	Joint_Venture("Joint venture", true), 
	LLC("Limited Liability Company", false), 
	Organization("Organization, including a Corporation", false),
	Sibling("Sibling", true),
	Trust("Trust", false);
	String value;
	boolean forPerson;
	
	private OrganizationType(String type, boolean forPerson){
		this.value = type;
		this.forPerson = forPerson;
	}
	
	public String getValue(){
		return value;
	}
	
	public boolean isForPerson(){
		return forPerson;
	}
}
