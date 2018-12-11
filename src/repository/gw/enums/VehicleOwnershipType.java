package repository.gw.enums;

public enum VehicleOwnershipType {
	Financed("Financed (Making Payments)"),
	Leased("Leased"),
	Own("Own");

	private String ownershipType;

	 VehicleOwnershipType(String ownershipType) {
		this.ownershipType = ownershipType;
	}
	
	public String getValue(){
		return ownershipType;
	}
}
