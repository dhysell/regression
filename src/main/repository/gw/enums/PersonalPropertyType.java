package repository.gw.enums;

public enum PersonalPropertyType {

	MedicalSuppliesAndEquipment("Medical Supplies & Equipment"), //Ded 25 - 1000 //extra fields
	RefrigeratedMilk("Refrigerated Milk"), //Ded 100
	MilkContaminationAndRefrigeration("Milk Contamination & Refrigeration"), //Ded 250
	SportingEquipment("Sporting Equipment"), //Ded 25 - 1000 //no limit
	GolfEquipment("Golf Equipment"), //Ded 25 - 1000 //no limit
	PhotographicEquipment("Photographic Equipment"), //Ded 0 - 1000 //no limit
	MusicalInstruments("Musical Instruments"), //Ded 50 //no limit
	ExteriorOrnaments("Exterior Ornaments"), //Ded 25 - 1000 //no limit
	Collectibles("Collectibles"), //Ded 25 - 1000 //no limit
	FineArts("Fine Arts"), //Ded 0% - 40% //no limit
	Furs("Furs"), //Ded 0 //no limit
	Jewelry("Jewelry"), //Ded 0% - 40% //no limit
	SaddlesAndTack("Saddles & Tack"), //Ded 25 - 1000 //no limit
	MedicalDevices("Medical Devices"), //no ded //no limit
	OfficeEquipment("Office Equipment"), //Ded 25 - 1000 //no limit
	RadioAntennae("Radio Antennae"), //Ded 25 - 1000 //no limit
	RadioReceiversAndTransmitters("Radio Receivers & Transmitters"), //Ded 25 - 1000 //no limit
	StereoEquipment("Stereo Equipment"), //Ded 25 - 1000 //no limit
	TailoringEquipment("Tailoring Equipment"), //Ded 25 - 1000 //no limit
	TelephoneEquipment("Telephone Equipment"), //Ded 100 - 1000 //no limit
	VideoEquipment("Video Equipment"), //Ded 25 - 1000 //no limit
	Tools("Tools"), //Ded 25 - 1000 //no limit
	BeeContainers("Bee Containers"), //Ded 25 - 1000 //no limit
	BlanketRadios("Blanket Radios"); //Ded 25 - 1000 //no limit
	
	String value;
	
	private PersonalPropertyType(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
	
}
