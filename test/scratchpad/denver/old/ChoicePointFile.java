package scratchpad.denver.old;

import java.math.BigDecimal;
public class ChoicePointFile {

	String claimNumber;
	BigDecimal amountPaid;
	
	public ChoicePointFile(String claimNumber, BigDecimal amountPaid) {
		this.claimNumber = claimNumber;
		this.amountPaid = amountPaid;
	}
		
	public ChoicePointFile() {
		
	}
}
