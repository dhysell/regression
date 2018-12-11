package repository.gw.enums;

public enum LiquorLiabilityOperationTypeGL {
	
	None("<none>"),
	Clubs("Clubs (70412)"),
	ManufacturersWholesalersAndDistributorsForConsumptionOffPremises("Manufacturers, wholesalers and distributors for consumption off premises (50911)"),
	RestaurantsHotelsMotelsTastingRoomsIncludingPackageSales("Restaurants, hotels, motels, tasting rooms including package sales (58161)"),
	PackageAndOtherRetailForConsumptionOffPremises("Package and other retail for consumption off premises (59211)");
	String value;
		
	private LiquorLiabilityOperationTypeGL(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
