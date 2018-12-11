package repository.ab.enums;

public enum TaxFilingStatus {

	Single("Single"),
	MarriedFilingJointly("Married filing jointly"),
	MarriedFilingSeparately("Married filing separately"),
	HeadOfHousehold("Head of household"),
	QualifyingWidowWithDependent("Qualifying widow(er) with dependent child");
	
	String value;
	
	private TaxFilingStatus(String type) {
		value = type;
	}
	
	public String getValue() {
		return value;
	}
	
	
	
}
