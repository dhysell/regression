package persistence.flatfile.other.entities;

import flapjack.model.RecordFactory;

public class UserRecordFactory implements RecordFactory {

	
	public Object build() {
		return new UserRecord();
	}

}
