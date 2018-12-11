package persistence.flatfile.mbre.entities;

import flapjack.model.RecordFactory;

public class PCMonthlyPolicyRecordFactory implements RecordFactory {

	
	public Object build() {
		return new PCMonthlyPolicyRecord();
	}

}
