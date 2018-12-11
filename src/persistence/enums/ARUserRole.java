package persistence.enums;

public enum ARUserRole {
	Billing_Clerical("BillingClerical"),
	Billing_Clerical_Advanced("BillingClericalAdvanced"),
	Billing_Manager("BillingManager"),
	View_Only("ViewOnly"),
	BC_View_Only("BCViewOnly"),
	Charge_Manipulator("ChargeManipulator"),
	CSR("CSR");
	String value;
	
	private ARUserRole(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
