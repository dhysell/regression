package repository.gw.enums;

//please add more as needed.
public enum ActivityReminderType {
	GeneralReminder("General reminder (diary)"), 
	ReviewProducer("Review Producer"),
	VerifyCoverage("Verify Coverage");
	
	String value;
	
	private ActivityReminderType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
