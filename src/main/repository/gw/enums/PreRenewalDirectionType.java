package repository.gw.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum PreRenewalDirectionType {
	Non_RenewAndReferToUnderwriter("Non-Renew and Refer to Underwriter"),
	ReferToUnderwriter("Refer to Underwriter"),
	ReferToUnderwriterAssistant("Refer to Underwriter Assistant");
	String value;
	
	private PreRenewalDirectionType(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
	
	public static PreRenewalDirectionType getEnum(String name) {
		for(PreRenewalDirectionType directionType : values()) {
			if(directionType.getValue().equals(name)) {
				return directionType;
			}
		}
		return null;
	}
	
	private static final List<PreRenewalDirectionType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final Random RANDOM = new Random();

	public static PreRenewalDirectionType random()  {
		return VALUES.get(RANDOM.nextInt(VALUES.size()));
	}
	
	
}
