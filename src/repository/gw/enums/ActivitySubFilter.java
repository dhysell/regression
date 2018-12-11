package repository.gw.enums;

public enum ActivitySubFilter {
	
	All("All"),
	AccountInactive("Account Inactive"),
	ChargeReversal("Charge Reversal"),
	Credit("Credit"),
	CreditReversal("Credit Reversal"),
	Disbursement("Disbursement"),
	FundTransfer("Fund Transfer"),
	FundTransferReversal("Fund Transfer Reversal"),
	NegativeWriteOff("Negative Write-Off"),
	NegativeWriteOffReversal("Negative Write-Off Reversal"),
	WriteOff("Write-Off"), 
	WriteOffReversal("Write-Off Reversal");
	String value;
	
	private ActivitySubFilter(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}

}
