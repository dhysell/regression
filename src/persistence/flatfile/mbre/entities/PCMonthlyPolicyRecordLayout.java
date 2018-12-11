package persistence.flatfile.mbre.entities;

import flapjack.layout.SimpleRecordLayout;

public class PCMonthlyPolicyRecordLayout extends SimpleRecordLayout {

	private PCMonthlyPolicyRecordLayout() {
		field("Company Number", 10);
		field("Named Insured Line 1", 60);
		field("Named Insured Line 2", 60);
		field("Principal Address Line 1", 40);
		field("Principal Address Line 2", 40);
		field("Principal City", 30);
		field("Principal State", 2);
		field("Principal County", 30);
		field("Principal Zip Code", 5);
		field("Current Policy Number", 20);
		field("Previous Policy Number", 20);
		field("Policy Effective Date", 8);
		field("Policy Expiration Date", 8);
		field("Accounting Date", 6);
		field("Transaction Effective Date", 8);
		field("Equipment Breakdown Premium", 8);
		field("Package/Property Premium", 8);
		field("Deductible", 8);
		field("Transaction Code", 2);
		field("Company Branch Code", 10);
		field("Policy Type", 10);
	}
	
}
