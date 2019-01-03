package repository.gw.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum RiskCategoryCode {

	
	NONE("<none>"),
	ZERO("000"),
	ONE("001"),
	TWO("002"),
	THREE("003"),
	FOUR("004"),
	FIVE("005"),
	SIX("006"),
	SEVEN("007"),
	EIGHT("008"),
	NINE("009"),
	TEN("010"),
	ELEVEN("011"),
	TWELVE("012"),
	THIRTEEN("013"),
	FOURTEEN("014"),
	FIFTEEN("015"),
	SIXTEEN("016"),
	SEVENTEEN("017"),
	EIGHTTEEN("018"),
	NINETEEN("019"),
	THIRTYTWO("032"),
	THIRTYTHREE("033"),
	THIRTYFOUR("034"),
	THIRTYFIVE("035"),
	THIRTYSIX("036"),
	THIRTYSEVEN("037"),
	THIRTYEIGHT("038"),
	THIRTYNINE("039"),
	FOURTYTWO("042"),
	FOURTYTHREE("043"),
	FOURTYFOUR("044"),
	FOURTYFIVE("045"),
	FOURTYSIX("046"),
	FOURTYSEVEN("047"),
	FOURTYEIGHT("048"),
	FOURTYNINE("049"),
	FIFTY("050"),
	FIFTYONE("051"),
	FIFTYTWO("052"),
	FIFTYTHREE("053"),
	FIFTYFOUR("054"),
	FIFTYFIVE("055"),
	FIFTYSIX("056"),
	FIFTYSEVEN("057"),
	FIFTYEIGHT("058"),
	FIFTYNINE("059"),
	SIXTY("060"),
	SIXTYONE("061"),
	SIXTYTWO("062"),
	SIXTYTHREE("063"),
	SIXTYFOUR("064"),
	SIXTYFIVE("065"),
	SIXTYSIX("066"),
	SIXTYSEVEN("067"),
	SIXTYEIGHT("068"),
	SIXTYNINE("069"),
	SEVENTY("070"),
	SEVENTYONE("071"),
	SEVENTYTWO("072"),
	SEVENTYTHREE("073"),
	SEVENTYFOUR("074"),
	SEVENTYFIVE("075"),
	SEVENTYSIX("076"),
	SEVENTYSEVEN("077"),
	SEVENTYEIGHT("078"),
	SEVENTYNINE("079"),
	EIGHTY("080"),
	EIGHTYONE("081"),
	EIGHTYTWO("082"),
	EIGHTYTHREE("083"),
	EIGHTYFOUR("084"),
	EIGHTYFIVE("085"),
	EIGHTYSIX("086"),
	EIGHTYSEVEN("087"),
	EIGHTYEIGHT("088"),
	EIGHTYNINE("089"),
	NINTY("090"),
	NINTYONE("091"),
	NINTYTWO("092"),
	NINTYTHREE("093"),
	NINTYFIVE("095"),
	NINTYFOUR("094");
	String value;
	
	private RiskCategoryCode(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
	
	private static final List<RiskCategoryCode> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final Random RANDOM = new Random();

	public static RiskCategoryCode random()  {
		return VALUES.get(RANDOM.nextInt(VALUES.size()));
	}
}




















