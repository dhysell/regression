package scratchpad.denver.old;

import java.math.BigDecimal;
public class APlusFile {
	String claimNumber;
	BigDecimal amountPaid;
	
	public APlusFile(String claimNumber, BigDecimal amountPaid) {
		this.claimNumber = claimNumber;
		this.amountPaid = amountPaid;
	}
		
	public APlusFile AplusFile() {
		return this;
	}
}
