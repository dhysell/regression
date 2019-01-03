package repository.gw.enums;

public enum ProductLineType {
	
	Squire("Squire", "Squire", "Squire"),
	Businessowners("Businessowners", "Businessowners", "Business Owners"), 
	CPP("CPP", "Commercial Package", "Commercial Package"), 
	StandardIM("Standard Inland Marine", "Standard Inland Marine", "Standard Inland Marine"),
    //	StandardFL("Standard Fire & Liability", "Standard Fire & Liability", "Standard Fire & Liability"),
    StandardFire("Standard Fire & Liability", "Standard Fire & Liability", "Standard Fire & Liability"),
    StandardLiability("Standard Fire & Liability", "Standard Fire & Liability", "Standard Fire & Liability"),
	PersonalUmbrella("Personal Umbrella", "Personal Umbrella", "Personal Umbrella"),
	Membership("Membership", "Membership", "Membership");
	
	String value;
	String name;
	String bcName;
	
	private ProductLineType(String type, String productName, String bcProductName){
		this.value = type;
		this.name = productName;
		this.bcName = bcProductName;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getBCName() {
		return bcName;
	}
}
