package repository.gw.enums;

public enum FileMarker {
	
	Cash_Only("Cash Only"),
	Certificate("Certificate"),
	Collection("Collection"),
	Do_not_Reinstate_Rewrite("Do not Reinstate/Rewrite"),
	Form_E("Form E"),
	Oregon_Washington_Construction_Board("Oregon/Washington Construction Board"),
	Pending_Cancel("Pending Cancel"),
	Pictures_in_DISR("Pictures in DISR"),
	Returned_Mail("Returned Mail"),	
	SR22("SR22"),
	Treaty_Exception("Treaty Exception"),
	Umbrella("Umbrella");	
	
	String value;
	
	private FileMarker(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}

}
