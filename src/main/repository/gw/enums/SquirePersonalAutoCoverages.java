package repository.gw.enums;

public class SquirePersonalAutoCoverages {

	public enum LiabilityLimit{
		
		TwentyFiveLow("25/50/15"),
		TwentyFiveHigh("25/50/25"),
		FiftyLow("50/100/25"),
		FiftyHigh("50/100/50"),
		OneHundredLow("100/300/50"),
		OneHundredHigh("100/300/100"),
		ThreeHundredLow("300/500/100"),
		ThreeHundredHigh("300/500/300"),
		CSL75K("75,000 CSL"),
		CSL100K("100,000 CSL"),
		CSL300K("300,000 CSL"),
		CSL500K("500,000 CSL"),
		CSL1M("1,000,000 CSL");
		String value;
		
		private LiabilityLimit(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum MedicalLimit {
		
		OneK("1,000"),
		TwoK("2,000"),
		FiveK("5,000"),
		TenK("10,000"),
		TwentyFiveK("25,000");
		String value;
		
		private MedicalLimit(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum UninsuredLimit{
		
		TwentyFive("25/50"),
		Fifty("50/100"),
		OneHundred("100/300"),
		ThreeHundred("300/500"),
		CSL75K("75,000 CSL"),
		CSL100K("100,000 CSL"),
		CSL300K("300,000 CSL"),
		CSL500K("500,000 CSL"),
		CSL1M("1,000,000 CSL");

		String value;
		
		private UninsuredLimit(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum UnderinsuredLimit{
		
		Thirty("30/60"),
		Fifty("50/100"),
		OneHundred("100/300"),
		ThreeHundred("300/500"),
		CSL75K("75,000 CSL"),
		CSL100K("100,000 CSL"),
		CSL300K("300,000 CSL"),
		CSL500K("500,000 CSL"),
		CSL1M("1,000,000 CSL");

		String value;
		
		private UnderinsuredLimit(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum CollisionLimit{
		
		TwentyFive("25"),
		Fifty("50"),
		OneHundred("100"),
		TwoFifty("250"),
		FiveHundred("500"),
		OneK("1000");
		
		String value;
		
		private CollisionLimit(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum ComprehensiveLimit{
		
		TwentyFive("25"),
		Fifty("50"),
		OneHundred("100"),
		TwoFifty("250"),
		FiveHundred("500"),
		OneK("1000");
		
		
		String value;
		
		private ComprehensiveLimit(String value){
			this.value = value;
		}
		
		public String getValue(){
			return this.value;
		}
		
	}
	
public enum RentalReimbursementLimit{
		
		TwentyFive("25"),
		Fifty("50");
		
		String value;
		
		private RentalReimbursementLimit(String value){
			this.value = value;
		}
		
		public String getValue(){
			return this.value;
		}		
	}
	
}
