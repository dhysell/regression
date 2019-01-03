package repository.gw.enums;

public enum VehicleUse {
	NotDriventoWorkorSchool("Not Driven to Work or School"),
	DriventoWorkorSchoollessthan15miles("Driven to Work or School less than 15 miles"),
	DriventoWorkorSchoolmorethan15miles("Driven to Work or School more than 15 miles");
	String value;
		
	private VehicleUse(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
