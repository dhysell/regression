package repository.gw.enums;

public enum DocumentRelatedTo {
	Agency("Agency"),
	Bank("Bank"),
	ClaimParty("Claim Party"),
	Client("Client"),
	County("County"),
	Dealer("Dealer"),
	Finance("Finance"),
	Lienholder("Lienholder"),
	Vendor("Vendor"),
	;
	
	String value;
	
	private DocumentRelatedTo(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
