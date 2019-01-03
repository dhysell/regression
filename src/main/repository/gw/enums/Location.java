package repository.gw.enums;

public class Location {
	
	public enum LocationType {
		Address("Address"), 
		Legal("Legal"), 
		SectionTownshipRange("Section, Township, Range");
		
		String value;
		
		private LocationType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
	public enum ProtectionClassCode {
		Prot1("1"), 
		Prot2("2"), 
		Prot3("3"), 
		Prot4("4"), 
		Prot5("5"), 
		Prot6("6"), 
		Prot7("7"), 
		Prot8("8"), 
		Prot9("9"), 
		Prot10("10");
		
		String value;
		
		private ProtectionClassCode(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		public static ProtectionClassCode valueOfName(final String name) {
			final String enumName = name.replaceAll(" ", "");
			try {
				return valueOf("Prot"+enumName);
			} catch (final IllegalArgumentException e) {
				return null;
			}
		}
	}
	
	public enum AutoIncreaseBlgLimitPercentage {
		AutoInc2Perc("2%"), 
		AutoInc4Perc("4%"), 
		AutoInc6Perc("6%"), 
		AutoInc8Perc("8%"), 
		AutoInc10Perc("10%");
		
		String value;
		
		private AutoIncreaseBlgLimitPercentage(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
}
