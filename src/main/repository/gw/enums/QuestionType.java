package repository.gw.enums;

public enum QuestionType {

	Boolean("Boolean"), 
	String("String"), 
	Choice("Choice"), 
	Date("Date"),
	Integer("Integer"),
	OR("OR");

	String value;

	private QuestionType(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
