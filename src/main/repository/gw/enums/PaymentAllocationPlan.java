package repository.gw.enums;

public enum PaymentAllocationPlan {
	DefaultPaymentAllocationPlan("Default Payment Allocation Plan"),
	DefaultUpgradePaymentAllocationPlan("Default Upgrade Payment Allocation Plan"),
	InsuredPaymentAllocationPlan("Insured Payment Allocation Plan"),
	LienholderPaymentAllocationPlan("Lienholder Payment Allocation Plan");
	String value;

	private PaymentAllocationPlan(String type){
		value = type;
	}

	public String getValue(){
		return value;
	}	
}
