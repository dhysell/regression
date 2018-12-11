package repository.gw.enums;

public enum ImportAdminDataExistingRecordResolution {
	None("<none>"),
	Overwrite_All_Exisiting_Records("Overwrite all existing records"),
	Disard_Updates_To_Existing_Records("Discard updates to existing records"),
	Case_By_Case_Resolution("Case-by-case resultion");
	String value;
		
	private ImportAdminDataExistingRecordResolution(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
