package repository.gw.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum PackageRiskType {
	Apartment("Apartment"),
	Contractor("Contractors"),
	IndustrialProcessing("Industrial/Processing"),
	Institutional("Institutional"),
	Mercantile("Mercantile"),
	MotelHotel("Motel/Hotel"),
	Office("Office"),
	Service("Service");
	String value;
	
	private PackageRiskType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}

	public static PackageRiskType getRandomRiskType()  {
		final Random RANDOM = new Random();
		final List<PackageRiskType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		final int SIZE = VALUES.size();
		if(!VALUES.get(RANDOM.nextInt(SIZE)).equals(PackageRiskType.Contractor)) {
			return VALUES.get(RANDOM.nextInt(SIZE));
		} else {
			getRandomRiskType();
		}
		return null;
	}
}
