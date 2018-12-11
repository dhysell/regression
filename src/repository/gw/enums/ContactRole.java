package repository.gw.enums;

public enum ContactRole {
	Agent("Agent"), 
	Agency("Agency"),
	Bank("Bank"),
	ClaimParty("Claim Party"), 
	Client("Client"),
	County("County"),
	Dealer("Dealer"), 
	Finance("Finance"), 
	Lienholder("Lienholder"), 
	Vendor("Vendor");
	String value;
		
	private ContactRole(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
