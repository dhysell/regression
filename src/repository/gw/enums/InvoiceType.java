package repository.gw.enums;

public enum InvoiceType {
	NewBusinessDownPayment("New Business Down Payment"),
	Shortage("Shortage"),
	Scheduled("Scheduled"),	
	RewriteDownPayment("Rewrite Down Payment"),
	RenewalDownPayment("Renewal Down Payment"),
	//ReinstateDownPayment("Reinstate Down Payment"),
	CashonlyRollup("Cashonly Rollup"),
	LienholderOnset("Lienholder Onset");
	String value;
		
	private InvoiceType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
