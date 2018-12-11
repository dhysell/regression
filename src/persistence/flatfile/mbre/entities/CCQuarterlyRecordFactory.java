package persistence.flatfile.mbre.entities;

import flapjack.model.RecordFactory;

public class CCQuarterlyRecordFactory implements RecordFactory {

	
	public Object build() {
		return new CCQuarterlyRecord();
	}

}
