package repository.gw.enums;

public enum FormatType {
	BooleanRadio,
	StringTextBox,
	ChoiceSelect,
	StringField,
	BooleanCheckbox,
	IntegerField,
	DateField;
	String value;
	
	public String getValue(){
		return value;
	}
}
