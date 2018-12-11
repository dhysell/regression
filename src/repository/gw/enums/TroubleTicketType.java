package repository.gw.enums;

public enum TroubleTicketType {

	Dispute("Dispute"),
	ProcessingError("Processing Error"),
	CustomerComplaint("Customer Complaint"),
	CustomerQuestion("Customer Question"),
	ReportError("Report Error"),
	DisasterHold("Disaste rHold"),
	PNIChange("PNI Change"),
	RewriteToHold("Rewrite to Hold"),
	SquireRenewalHold("Squire Renewal Hold"),
    PromisedPayment("Promised Payment"),
    RenewalGracePeriod("Renewal Grace Period"),
    PromisePayment("Promise Payment");
	String value;
		
	private TroubleTicketType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
