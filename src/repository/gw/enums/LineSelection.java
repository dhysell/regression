package repository.gw.enums;

public enum LineSelection {

	PersonalAutoLinePL("Personal Auto Line"),
	PropertyAndLiabilityLinePL("Property Line"),
	InlandMarineLinePL("Squire Inland Marine"),
	CommercialPropertyLineCPP("Commercial Property Line"),
	GeneralLiabilityLineCPP("General Liability Line"),
	CommercialAutoLineCPP("Commercial Auto Line"),
	InlandMarineLineCPP("Inland Marine Line"),
//	FourHLivestock("4-H Livestock"),
	StandardFirePL("Standard Fire"),
	StandardInlandMarine("Standard Inland Marine"),
	StandardLiabilityPL("Standard Liability"),
	Membership("Membership"),
	Businessowners("Businessowners Line");
	
	String value;
		
	private LineSelection(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
