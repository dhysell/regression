package repository.gw.enums;

public enum ContactNameCasingFilter {
	All("All"),
	One("Begin with 1"),
	Two("Begin with 2"),
	Three("Begin with 3"),
	Four("Begin with 4"),
	Five("Begin with 5"),
	Six("Begin with 6"),
	Seven("Begin with 7"),
	Eight("Begin with 8"),
	Nine("Begin with 9"),
	Zero("Begin with 0"),
	A("Begin with A"),
	B("Begin with B"),
	C("Begin with C"),
	D("Begin with D"),
	E("Begin with E"),
	F("Begin with F"),
	G("Begin with G"),
	H("Begin with H"),
	I("Begin with I"),
	J("Begin with J"),
	K("Begin with K"),
	L("Begin with L"),
	M("Begin with M"),
	N("Begin with N"),
	O("Begin with O"),
	P("Begin with P"),
	Q("Begin with Q"),
	R("Begin with R"),
	S("Begin with S"),
	T("Begin with T"),
	U("Begin with U"),
	V("Begin with V"),
	W("Begin with W"),
	X("Begin with X"),
	Y("Begin with Y"),
	Z("Begin with Z");
	
	String value;
		
	private ContactNameCasingFilter(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
