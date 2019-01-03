package repository.gw.enums;

public enum ClaimVendorType {
	APlus("A-Plus"),
	AutoTowingVendor("Auto Towing Vendor"),
	AutobodyRepairShop("Autobody Repair Shop"),
	CLUE("C.L.U.E."),
	DepartmentofTransportation("Department of Transportation"),
	LawFirm("Law Firm"),
	MarketConduct("Market Conduct"),
	MedicalCareOrganization("Medical Care Organization"),
	MedicareMedicaidServices("Medicare & Medicaid Services"),
	Other("Other"),
	SalvageVendor("Salvage Vendor");
	
	String value;
	   
    
    private ClaimVendorType(String type){
        value = type;
    }
    
    public String getValue(){
        return value;
    }
	
	

	
}
