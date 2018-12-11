package repository.gw.enums;

public enum PersonSuffix {
	None("<none>"),
	Jr("Jr."),
	Sr("Sr."),
	I("I"),
	II("II"), 
	III("III"), 
	IV("IV"),
	MD("M.D."),
	PhD("PhD."),
	DO("D.O."),
	Esq("Esq."), 
	CPA("C.P.A."), 
	DC("D.C."),
	DDS("D.D.S."),
	DVM("D.V.M."),
	IX("IX"),
	LMT("L.M.T."), 
	PA("P.A."), 
	PC("P.C."),
	PT("P.T."),
	V("V"),
	VI("VI"),
	VII("VII"), 
	VIII("VIII"), 
	X("X");
	
	String value;
	
	private PersonSuffix(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
}
