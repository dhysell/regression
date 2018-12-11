package repository.gw.enums;

public enum PersonalPropertyScheduledItemType {
	
	Camera("Camera", PersonalPropertyType.PhotographicEquipment),
	Lens("Lens", PersonalPropertyType.PhotographicEquipment),
	Misc("Misc", PersonalPropertyType.PhotographicEquipment),
	Base("Base", PersonalPropertyType.BlanketRadios),
	Mobile("Mobile", PersonalPropertyType.BlanketRadios),
	Portable("Portable", PersonalPropertyType.BlanketRadios),
	Telephone("Telephone", PersonalPropertyType.BlanketRadios),
	Guns("Guns",PersonalPropertyType.SportingEquipment),
	Others("Others",PersonalPropertyType.SportingEquipment),
	Saddle("Saddle",PersonalPropertyType.SaddlesAndTack),
	Other("Other",PersonalPropertyType.SaddlesAndTack),
	Watch("Watch",PersonalPropertyType.Jewelry),
	Ring("Ring",PersonalPropertyType.Jewelry),
	Necklace("Necklace",PersonalPropertyType.Jewelry),
	Locket("Locket",PersonalPropertyType.Jewelry),
	Earings("Earings",PersonalPropertyType.Jewelry),
	Chain("Chain",PersonalPropertyType.Jewelry),
	Brooch("Brooch",PersonalPropertyType.Jewelry),
	Bracelet("Bracelet",PersonalPropertyType.Jewelry),
	JewelaryOther("Other",PersonalPropertyType.Jewelry);
	
	
	
	
	String value;
	PersonalPropertyType parentPersonalPropertyType;
	
	private PersonalPropertyScheduledItemType(String value, PersonalPropertyType parentPersonalPropertyType) {
		this.value = value;
		this.parentPersonalPropertyType = parentPersonalPropertyType;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public PersonalPropertyType getParentPersonalPropertyType() {
		return this.parentPersonalPropertyType;
	}

}
