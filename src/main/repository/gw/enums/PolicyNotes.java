package repository.gw.enums;

public class PolicyNotes {
	
	public enum RelatedTo {
		Any("Any"), 
		Account_Only("Account Only"),
		Delinquencies("Delinquencies"),
		Disbursements("Disbursements"),
		Invoices("Invoices"),
		Payments("Payments"),
		Policies("Policies"),
		Producer("Producer"),
		Transactions("Transactions"),
		Trouble_Tickets("Trouble Tickets");
		
		String value;
		
		private RelatedTo(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
		
		public static RelatedTo valueOfRelatedToString(final String name) {
			final String enumName = name.replaceAll(" ", "_");
			try {
				return valueOf(enumName);
			} catch (final IllegalArgumentException e) {
				return null;
			}
		}
	}
	
	public enum Topic {
		Any("Any"), 
		Billing_Eror("Billing Eror"),
		Delinquency("Delinquency"),
		Disbursement("Disbursement"),
		Dispute("Dispute"),
		General("General"),
		Policy_Transfer("Policy Transfer"),
		Renewal_Carryover("Renewal Carryover"),
		Subrogation("Subrogation");
		
		String value;
		
		private Topic(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
		
		public static Topic valueOfTopicString(final String name) {
			final String enumName = name.replaceAll(" ", "_");
			try {
				return valueOf(enumName);
			} catch (final IllegalArgumentException e) {
				return null;
			}
		}
	}
	
	public enum DateRange {
		Any("Any"), 
		Today("Today"),
		Seven_Days_Ago("7 days ago"),
		Thirty_Days_Ago("30 days ago"),
		Ninety_Days_Ago("90 days ago"),
		OneHundredEighty_Days_Ago("180 days ago"),
		ThreeHundredSixtyFive_Days_Ago("365 days ago");
		
		String value;
		
		private DateRange(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
		
		public static DateRange valueOfDateRangeString(final String name) {
			final String enumName = name.replaceAll(" ", "_");
			try {
				return valueOf(enumName);
			} catch (final IllegalArgumentException e) {
				return null;
			}
		}
	}
	
	public enum SortBy {
		Author("Author"), 
		Date("Date"),
		Subject("Subject"),
		Topic("Topic");
		
		String value;
		
		private SortBy(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
		
		public static SortBy valueOfSortByString(final String name) {
			final String enumName = name.replaceAll(" ", "_");
			try {
				return valueOf(enumName);
			} catch (final IllegalArgumentException e) {
				return null;
			}
		}
	}
}
