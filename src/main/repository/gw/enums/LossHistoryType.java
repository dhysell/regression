package repository.gw.enums;

public enum LossHistoryType {
	NoLossHistory("No Loss History"),
	ManuallyEntered("Manually Entered"),
	Attached("Attached");
	String value;
	
	private LossHistoryType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
