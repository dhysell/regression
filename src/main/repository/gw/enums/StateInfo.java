package repository.gw.enums;

public class StateInfo {

	public enum Un_UnderInsuredMotoristLimit { 
		FiftyThousand50K("50,000"), 
		OneHundredThousand100K("100,000"),
		TwoHundredFiftyThousand250K("250,000"), 
		ThreeHundredThousand300K("300,000"),
		ThreeHundredFiftyThousand350K("350,000"), 
		FiveHundredThousand500K("500,000"),
		SevenHundredFiftyThousand750K("750,000"), 
		OneMillion1M("1,000,000");
		String value;
			
		private Un_UnderInsuredMotoristLimit(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	
}
