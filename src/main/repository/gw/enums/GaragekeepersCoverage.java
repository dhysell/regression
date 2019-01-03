package repository.gw.enums;

public class GaragekeepersCoverage {
	
	public enum LegalOrPrimary {
		None("<none>"), 
		Legal("Legal"), 
		Primary("Primary");
		
		String value;
		
		private LegalOrPrimary(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum CollisionDeductible {
		None("<none>"), 
		OneHundred("100"), 
		TwoHundredFifty("250"),
		FiveHundred("500");
		
		String value;
		
		private CollisionDeductible(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum ComprehensiveDeductible {
		None("<none>"), 
		OneHundredAndFiveHundred("100/500"), 
		TwoHundredFiftyAndOneThousand("250/1000"),
		FiveHundredAndTwoThousandFiveHundred("500/2500");
		
		String value;
		
		private ComprehensiveDeductible(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum SpecifiedCausesOfLoss {
		None("<none>");
		
		String value;
		
		private SpecifiedCausesOfLoss(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum SpecifiedCausesOfLossDeductible {
		None("<none>"), 
		OneHundredAndFiveHundred("100/500"), 
		TwoHundredFiftyAndOneThousand("250/1000"),
		FiveHundredAndTwoThousandFiveHundred("500/2500");
		
		String value;
		
		private SpecifiedCausesOfLossDeductible(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
}
