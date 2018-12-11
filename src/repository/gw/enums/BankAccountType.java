package repository.gw.enums;

import java.util.HashMap;
import java.util.Map;

public enum BankAccountType {
	Business_Checking("Business Checking","BusChk"), 
	Business_Savings("Business Savings", "BusSav"), 
	Personal_Checking("Personal Checking", "PerChk"), 
	Personal_Savings("Personal Savings", "PerSav");
	String value;
	String bcValue;
	
	private static final Map<String, BankAccountType> accountTypeByString = new HashMap<String, BankAccountType>();

	static {
		for (BankAccountType type : values()) {
			accountTypeByString.put(type.getValue(), type);
		}
	}
	
	private static final Map<String, BankAccountType> accountTypeByStringBC = new HashMap<String, BankAccountType>();
	
	static {
		for (BankAccountType type : values()) {
			accountTypeByStringBC.put(type.getBCValue(), type);
		}
	}
	
	private BankAccountType(String type, String bcValue){
		value = type;
		this.bcValue = bcValue;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getBCValue(){
		return bcValue;
	}
	
	public static BankAccountType importFromString(final String value) {
		final BankAccountType bankAccountType = accountTypeByString.get(value);
		if (bankAccountType != null) {
			return bankAccountType;
		} else {
			return null;
		}
	}
}
