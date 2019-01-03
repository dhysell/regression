package persistence.flatfile.mbre.entities;

import flapjack.layout.SimpleRecordLayout;

public class CCQuarterlyRecordLayout extends SimpleRecordLayout {

	private CCQuarterlyRecordLayout() {
		field("Policy Number", 20);
		field("Claim Number", 20);
		field("Date of Loss", 8);
		field("Named Insured", 60);
		field("Total Loss Adjustment Paid", 12);
		field("Total Loss Payment", 12);
	}
	
}
