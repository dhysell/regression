package repository.gw.enums;

public enum UserRole {
	BillingClerical("Billing Clerical"),
	BillingClericalAdvanced("Billing Clerical Advanced"),
	BillingManager("Billing Manager"),
	Broker("Broker"),
	CFO("CFO"),
	ClientDataAdmin("Client Data Admin"),
	CommissionsAdmin("Commissions Admin"),
	DirectCollector("DirectCollector"),
	EventMessagingCheck("Event Messaging Check"),
	FinanceClerical("Finance Clerical"),
	FinanceManager("Finance Manager"),
	GeneralAdmin("General Admin"),
	InformationServices("Information Services"),
	Maintenance("Maintenance"),
	PCViewOnly("PC View Only"),
	PlanAdmin("Plan Admin"),
	RuleAdmin("Rule Admin"),
	Superuser("Superuser"),
	Underwriter("Underwriter"),
	UserAdmin("User Admin");
	String value;
		
	private UserRole(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
