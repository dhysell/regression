package repository.gw.enums;

public enum LossRatioDiscounts {
	FIFTEENDISCOUNT("15% discount"),
	TWENTYFIVESURCHARGE("25% surcharge"),
	TWENTYSURCHARGE("20% surcharge"),
	FIVEDISCOUNT("5% discount"),
	SEVENTYSURCHARGE("70% surcharge"),
	TENSURCHARGE("10% surcharge"),
	TENDISCOUNT("10% discount"),
	ZERODISCOUNT("0% discount");
	String value;
	
	private LossRatioDiscounts(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
