package repository.gw.enums;

public enum ReasonNotTaken {
	ApplicantDidntRespondToTheQuote("Applicant didn\'t respond to the quote"), 
	DoesNotMeetCoverageNeeds("Does not meet coverage needs"), 
	PricedTooHigh("Priced too high"), 
	CallAtRenewal("Call at renewal");
	String value;
		
	private ReasonNotTaken(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
