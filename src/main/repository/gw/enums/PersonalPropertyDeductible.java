package repository.gw.enums;

public enum PersonalPropertyDeductible {
 
	Ded0("0", false), 
	Ded25("25", false), 
	Ded50("50", false), 
	Ded100("100", false), 
	Ded250("250", false), 
	Ded500("500", false), 
	Ded1000("1000", false),
	Ded0Perc("0%", true),
	Ded5Perc("5%", true),
	Ded10Perc("10%", true),
	Ded20Perc("20%", true),
	Ded40Perc("40%", true);
	
	String value;
	boolean percentage;
	
	private PersonalPropertyDeductible(String value, boolean percentage) {
		this.value = value;
		this.percentage = percentage;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public boolean isPercentage() {
		return this.percentage;
	}
	
}
