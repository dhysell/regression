package repository.gw.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PaymentInstrumentEnum {
	Cash("Cash"),
	Check("Check"),	
	Credit_Debit("Credit / Debit"),
	ACH_EFT("ACH/EFT"),
	Check_Additional_Interest("Check - Additional Interest"),
	Check_Cash_Equivalent("Check - Cash Equivalent"),
	Check_Title_Company("Check - Title Company"),
	Responsive("Responsive"),
	None("None"),
	Unapplied_fund_Account("Unapplied fund (Account)");
	String value;

	private static final Map<String, PaymentInstrumentEnum> lookup = new HashMap<String,PaymentInstrumentEnum>();
	static {
		for(PaymentInstrumentEnum s : EnumSet.allOf(PaymentInstrumentEnum.class))
			lookup.put(s.getValue(), s);
	}

	private PaymentInstrumentEnum(String type){
		this.value = type;
	}

	public String getValue(){
		return this.value;
	}

	public static PaymentInstrumentEnum get(String paymentInstrument) { 
		return lookup.get(paymentInstrument); 
	}
}
