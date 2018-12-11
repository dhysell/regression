package persistence.flatfile.mbre.entities;

import flapjack.model.RecordFactory;

public class PCMonthlyLocationRecordFactory implements RecordFactory {

	
	public Object build() {
		return new PCMonthlyLocationRecord();
	}

}
