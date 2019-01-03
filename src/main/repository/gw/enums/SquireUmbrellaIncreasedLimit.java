package repository.gw.enums;

public enum SquireUmbrellaIncreasedLimit {
	
	Limit_1000000("1,000,000"),
	Limit_2000000("2,000,000"),
	Limit_3000000("3,000,000"),
	Limit_4000000("4,000,000"),
	Limit_5000000("5,000,000"),
	Limit_6000000("6,000,000"),
	Limit_20000000("20,000,000");
	
	String value;
	
	private SquireUmbrellaIncreasedLimit(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}

}
