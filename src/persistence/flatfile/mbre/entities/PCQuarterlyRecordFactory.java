package persistence.flatfile.mbre.entities;

import flapjack.model.RecordFactory;

public class PCQuarterlyRecordFactory implements RecordFactory {

	
	public Object build() {
		return new PCQuarterlyRecord();
	}

}
