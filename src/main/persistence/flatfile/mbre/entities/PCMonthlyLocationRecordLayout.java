package persistence.flatfile.mbre.entities;

import flapjack.layout.SimpleRecordLayout;

public class PCMonthlyLocationRecordLayout extends SimpleRecordLayout {

	private PCMonthlyLocationRecordLayout() {
		field("Current Policy Number", 20);
		field("Transaction Code", 2);
		field("Location Sequence Number", 3);
		field("Location Address 1", 60);
		field("Location Address 2", 40);
		field("Location City", 40);
		field("Location State", 2);
		field("Location County", 30);
		field("Location Zip Code", 7);
		field("Location Occupancy Code", 10);
		field("Location Building Value", 10);
		field("Location Contents Value", 10);
		field("Location Business Income Value", 10);
		field("Location Contact - Name", 20);
		field("Location Contact - Phone Number", 15);
		field("Tenant / Owner", 1);
	}
	
}
