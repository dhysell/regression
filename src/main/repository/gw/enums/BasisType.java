package repository.gw.enums;

public enum BasisType {
	Payroll("Payroll"),
	GrossSales("Gross Sales"),
	Admissions("Admissions"),
	Units_Each("Units - Each"),
	AreainAcres("Area in Acres");
	String value;
		
	private BasisType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
	
	public static BasisType valueOfName(final String name) {
		final String enumName = name.replaceAll(" ", "").replaceAll("-", "_");
		try {
			return valueOf(enumName);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}
}
