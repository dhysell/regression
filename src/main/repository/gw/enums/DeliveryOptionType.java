package repository.gw.enums;

public enum DeliveryOptionType {
	AsTheirInterestsMayAppear("As Their Interests May Appear", "As Their Interests May Appear"), 
	ItsSuccessorsAndOrAssigns("Its Successors And/Or Assigns", "Its Successors And/Or Assigns"), 
	ISAOAorATIMA("ISAOA or ATIMA", "ISAOA ATIMA"),
	Attention("Attention", "ATTN "), 
	InCareOf("In Care Of", "% "), 
	DBA("DBA", "Doing Business As "), 
	FBO("FBO", "For the Benefit Of "), 
	FLCA("FLCA", "Federal Land Credit Association (Long Term Loans) "), 
	MAC("MAC", "Multi Asset Class "), 
	Other("Other", "Other (No description will show up) "), 
	PCA("PCA", "Production Credit Association (Short Term Loans) ");
	
	String typeValue;
	String descValue;
	
	private DeliveryOptionType(String typeValue, String descValue) {
		this.typeValue = typeValue;
		this.descValue = descValue;
	}
	
	public String getTypeValue() {
		return this.typeValue;
	}
	
	public String getDescValue() {
		return this.descValue;
	}
}
