package repository.gw.enums;

public enum PolicyType {
	Calf4H("4H Calf"), 
	Assign("Assigned Risk"),
	BOP("BOP"), 
	City("City"),
	CommercialCrime("Commercial Crime"), 
	CommercialGarage("Commercial Garage"),
	CommercialInlandMarine("Commercial Inland Marine"), 
	CommercialLiablility("Commercial Liability"),
	CommercialUnbrella("Commercial Umbrella"), 
	Country("Country"),
	CPP("CPP"), 
	CropGrain("Crop Grain"),
	CropHail("Crop Hail"), 
	FarmAndRanch("Farm and Ranch"),
	FidelityBonds("Fidelity Bonds"), 
	GarageKeepers("Garage Keepersa"),
	GarageLiability("Garage Liability"),
	GarageLiabilityBroadened("Garage Liability Broadened"), 
	Membership("Membership"),
	StandardAuto("Standard Auto"), 
	StandardFire("Standard Fire"),
	StandardInlandmarine("Standard Inland Marine"), 
	StandardLiability("Standard Liability"),
	SuretyBonds("Surety Bonds"), 
	Unbrella("Umbrella"),
	WescomAuto("Wescom Auto"),
	CommercialAuto("Commercial Auto"),
	CommercialProperty("Commercial Property");
	String value;
	
	private PolicyType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
