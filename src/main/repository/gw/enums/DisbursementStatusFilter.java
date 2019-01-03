package repository.gw.enums;

public enum DisbursementStatusFilter {
	All("All"),
	Open_Only("Open only"),
	Issued_Only("Issued only"),
	Rejected_Only("Rejected only"),
	Reapplied_Only("Reapplied only");
	String value;
	
	private DisbursementStatusFilter(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
