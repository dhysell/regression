package repository.gw.generate.custom;

public enum PARentalReimbursement {

	TwentyFive25("25"), Fifty50("50");

	String value;

	private PARentalReimbursement(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
