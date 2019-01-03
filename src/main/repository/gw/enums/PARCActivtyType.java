package repository.gw.enums;

public enum PARCActivtyType {
	Change_EFT_Draft_Date("Change EFT Draft Date"),
	Change_Payment_Plan("Change payment plan"),
	Increased_Billed_Invoice("Increased Billed invoice"),
	Intercompany_Transfer_Payment("Intercompany Transfer Payment"),
	Other_PARC_Message("Other PARC message"),
	Re_RunEFT("Re-Run EFT"),
	Stop_EFT_Cancel("Stop EFT Cancel"),
	Stop_EFT_New_Bank("Stop EFT New Bank");

	String value;

	private PARCActivtyType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
	public static PARCActivtyType getRandom() {
		return values()[(int) (Math.random() * values().length)];
	}
}
