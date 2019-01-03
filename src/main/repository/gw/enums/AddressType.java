package repository.gw.enums;

public enum AddressType {
	TenNinetyNine("1099"), 
	Billing("Billing"), 
	Business("Business"), 
	Home("Home"), 
	Lienholder("Lienholder"), 
	Mailing("Mailing"), 
	Other("Other"), 
	Vendor("Vendor"), 
	Work("Work");
	String value;
	
	private AddressType(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
}
