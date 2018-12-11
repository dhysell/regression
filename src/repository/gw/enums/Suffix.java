package repository.gw.enums;

public enum Suffix implements GetValue {
	None("<none>"),
	JR("Jr."),
	SR("Sr."),
	I("I"),
	II("II"),
	III("III"),
	IV("IV"),
	V("V"),
	VI("VI"),
	VII("VII"),
	VIII("VIII"),
	IX("IX"),
	X("X"),
	DVM("D.V.M."),
	MD("M.D."),
	PHD("PhD."),
	DO("D.O."),
	Esq("Esq."),
	DC("D.C."),
	DDS("D.D.S."),
	PA("P.A."),
	PC("P.C."),
	PT("P.T."),
	CPA("C.P.A."),
	LMT("LMT");
	
	String value;
	
	private Suffix(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
