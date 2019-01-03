package persistence.flatfile.mbre.entities;

import flapjack.layout.SimpleRecordLayout;

public class PCQuarterlyRecordLayout extends SimpleRecordLayout {

	private PCQuarterlyRecordLayout() {
		field("Named Insured", 60);
		field("Policy Number", 20);
		field("Policy Effective Date", 8);
		field("Policy Expiration Date", 8);
		field("Total Annual Premium", 12);
	}
	
}
