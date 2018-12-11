package repository.gw.enums;

public enum OrganizationTypePL implements GetValue {

	Individual("Individual"),
	Partnership("Partnership"),
	Corporation("Corporation"),
	Joint_Venture("Joint venture"),
	TrustOrEstate("Trust or Estate"),
	LLC("Limited Liability Company"),
	LimitedPartnership("Limited partnership"),
	Other("Other"),
	Organization("Organization");
	
	String value;
	boolean forPerson;
	
	private OrganizationTypePL(String type){
		this.value = type;
	}
	
	public String getValue(){
		return value;
	}
}
