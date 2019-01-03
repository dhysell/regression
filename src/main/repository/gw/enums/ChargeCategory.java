package repository.gw.enums;

public enum ChargeCategory {
	Membership_Dues("Membership Dues"),
	Membership_Dues_Reversed ("Membership Dues (Reversed)"),
	Premium("Premium"),
	Policy_Payment_Reversal_Fee("Policy Payment Reversal Fee"),
	Installment_Fee("Installment Fee"),
	SR22_Fee("SR-22 Fee"),
	Recapture ("Recapture"),
	Policy_Recapture ("Policy Recapture"),
	Down_Payment("Down Payment");
	
	String value;
	
	private ChargeCategory(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
