package repository.gw.enums;

public enum AdditionalInterestType {
	Additional_Insured_Building_Owner("Additional Insured Building Owner BP 12 31"), 
	Building_Owner_Loss_Payable("Building Owner Loss Payable Clause BP 12 03"), 
	Contract_Of_Sale("Contract of Sale Clause BP 12 03"), 
	Lenders_Loss_Payable("Lender's Loss Payable Clause BP 12 03"), 
	Loss_Payable("Loss Payable Clause BP 12 03"), 
	Mortgagee("Mortgagee"),
	LessorPL("Lessor"),
	LienholderPL("Lienholder");
	String value;
	
	private AdditionalInterestType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}

}
