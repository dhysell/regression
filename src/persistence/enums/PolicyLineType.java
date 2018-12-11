package persistence.enums;

public enum PolicyLineType {

	PersonalAutoLinePL("PersonalAutoLine", false),
	PropertyAndLiabilityLinePL("HomeownersLine_HOE", false),
	InlandMarineLinePL("PIMLine", false),
	
	CommercialPropertyLineCPP("CPLine", false),
	GeneralLiabilityLineCPP("GLLine", false),
	CommercialAutoLineCPP("BusinessAutoLine", false),
	InlandMarineLineCPP("IMLine", false),
	
	FourHLivestock("10001", false),
	StandardInlandMarine("10002", false),
	
	StandardFirePL("10001", true),
	StandardLiabilityPL("10002", true),
	
	Businessowners("BOPLine", false),
	
	PersonalUmbrellaLine("PersonalUmbrellaLine_PUE", false);
	
	String dbValue;
	boolean stdFireLiab;
	
	private PolicyLineType(String dbValue, boolean stdFireLiab) {
		this.dbValue = dbValue;
		this.stdFireLiab = stdFireLiab;
	}
	
	public String getDBValue(){
		return dbValue;
	}
	
	public boolean isStdFireLiab(){
		return stdFireLiab;
	}
	
	public static PolicyLineType getTypeFromDBValue(String dbValue, boolean stdFireLiab) {
		PolicyLineType toReturn = null;
		for(PolicyLineType ptype : values()) {
			if(stdFireLiab) {
				if(ptype.isStdFireLiab()) {
					if(ptype.getDBValue().equals(dbValue)) {
						toReturn = ptype;
						break;
					}
				}
			} else {
				if(!ptype.isStdFireLiab()) {
					if(ptype.getDBValue().equals(dbValue)) {
						toReturn = ptype;
						break;
					}
				}
			}
			
		}
		return toReturn;
	}
	
}
