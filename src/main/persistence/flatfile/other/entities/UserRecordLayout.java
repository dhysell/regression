package persistence.flatfile.other.entities;

import flapjack.layout.SimpleRecordLayout;

public class UserRecordLayout extends SimpleRecordLayout {

	private UserRecordLayout() {
		field("First Name", 11);
		field("Last Name", 11);
		field("Username", 11);
		field("Terminator", 1);
	}
	
}
