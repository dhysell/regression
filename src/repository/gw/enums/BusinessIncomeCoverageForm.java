package repository.gw.enums;

public class BusinessIncomeCoverageForm {

	public enum BusinessIncomeCoverageType {
		
		BusinessIncome_AndExtraExpense_CoverageForm_CP_00_30("Business Income (And Extra Expense) Coverage Form CP 00 30"),
		BusinessIncome_WithoutExtraExpense_CoverageForm_CP_00_32("Business Income (Without Extra Expense) Coverage Form CP 00 32");
		String value;
		
		private BusinessIncomeCoverageType(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum CoverageOptions {
		BusinessIncomeAgreedValue("Business Income Agreed Value"),
		Coinsurance("Coinsurance"),
		MaximumPeriodOfIndemnity("Maximum Period Of Indemnity"),
		MonthlyLimitOfIndemnity("Monthly Limit Of Indemnity");
		String value;
		
		private CoverageOptions(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum CoverageType {
		BusinessIncomeIncludingRentalValue("Business Income including rental value"),
		BusinessIncomeOtherThanRentalValue("Business Income other than rental value"),
		RentalValue("Rental value");
		String value;
		
		private CoverageType(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum PropertyDescription {
		MercantileAndNon_ManufacturingOperations("Mercantile And Non-manufacturing Operations"),
		Manufacturing("Manufacturing"),
		RentalProperties("Rental Properties");
		String value;
		
		private PropertyDescription(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum MonthlyLimit {
		OneThird("1/3"),
		OneFourth("1/4"),
		OneSixth("1/6");
		String value;
		
		private MonthlyLimit(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum CoinsurancePercent {
		FiftyPercent("50%"),
		SixtyPercent("60%"),
		SeventyPercent("70%"),
		EightyPercent("80%"),
		NinetyPercent("90%"),
		OneHundredPercent("100%");
		String value;
		
		private CoinsurancePercent(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
}
