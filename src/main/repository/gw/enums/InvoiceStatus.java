package repository.gw.enums;

public enum InvoiceStatus {
	Planned("Planned"),
	Billed("Billed"),
	Due("Due"),
	Carried_Forward("Carried Forward");
	String value;
		
	private InvoiceStatus(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
