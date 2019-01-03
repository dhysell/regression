package repository.gw.enums;

public enum TransactionType {
	General("General"),
	Submission("Submission"),
	Issuance("Issuance"),
	Policy_Change("Policy Change"),
	Policy_Issuance("Policy Issuance"),	
	Renewal("Renewal"),
	Cancellation("Cancellation"),
	Reinstatement("Reinstatement"),
	//PLEASE BE AWARE THAT BILLING CENTER ONLY DISPLAYS REWRITE CHARGES AS "REWRITE" AND DOES NOT DIFFERENTIATE
	//BETWEEN THE VARIOUS REWRITE TYPES (I.E. - REWRITE NEW TERM, FULL TERM, REMAINDER OF TERM). PLEASE SELECT REWRITE ONLY
	//IF YOU ARE USING THIS ENUM FOR THE CHARGES TABLE IN B.C. DO NOT USE ONE OF THE OTHER REWRITE TYPES.
	Rewrite("Rewrite"),
	Rewrite_New_Term("Rewrite New Term"),
	Rewrite_Full_Term("Rewrite Full Term");
	
	String value;
	
	private TransactionType(String value) {
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
}