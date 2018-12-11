package repository.gw.enums;

public enum HowLongWithoutCoverage {
		SixtyOrLess("60 Days or less"),
		BetweenSixtyAndSixMonths("Between 60 days and 6 months"),
		MorethanSixMonths("6 months or more"),
		NewDriver("New Driver");
	
		String value;			
		private HowLongWithoutCoverage(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
}
