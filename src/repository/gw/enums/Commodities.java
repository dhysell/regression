package repository.gw.enums;

public class Commodities {
	
	public enum ProducerInformaion_Organization {
		LLC,
		Corperation,
		Partnership,
		SoleProprietor,
		Retired,
		Other
	}
	
	public enum ProducerInformaion_Agribusiness {
		Finance,
		Agronomic,
		Livestock,
		Equipment,
		Education,
		FarmRanchManager,
		FarmRanchEmployee,
		SeedFeedFertilizerDealer,
        Other;
	}
	
	public enum ProducerInformaion_DoYouMarketTheFollowing {
		Organic,
		NonGMO,
		AllNatural,
		PrivateLabel,
		GrownInIdaho,
        Other;
	}
	
	public enum ProducerInformaion_WaterUser {
		GroundWater,
		SurfaceWater,
        StockWater;
    }

    public enum AcresInOperation {
        Irrigated,
        Dryland,
        Pasture,
        BLM,
        ForestService,
        StateLand,
        CRP;
	}

    public enum AcresPlanted {
        Alfalfahay("Alfalfa Hay"),
        SmallGrains("Small Grains"),
        Teff("Teff"),
        Timothy("Timothy"),
        SpringHardWheat("Spring Hard Wheat"),
        SpringSoftWheat("Spring Soft Wheat"),
        WinterHardWheat("Winter Hard Wheat"),
        WinterSoftWheat("Winter Soft Wheat"),
        Oats("Oats"),
        OilsSeeds("Oils Seeds"),
        Hops("Hops"),
        Mint("Mint"),
        Asparagus("Asparagus"),
        SweetCorm("Sweet Corn"),
        SilageCorn("Silage Corn"),
        FieldCorn("Field Corn"),
        MaltBarley("Malt Barley"),
        FeedBarley("Feed Barley"),
        Potato_Commercial("Potato (Commercial)"),
        PotatoSeed("Potato Seed"),
        Onions_Commercial("Onions (Commercial)"),
        OnionSeed("Onion Seed"),
        SugarBeets("Sugar Beets"),
        Apples("Apples"),
        Berries("Berries"),
        Grapes("Grapes"),
        Peaches("Peaches"),
        Plum("Plum"),
        Cherry("Cherry"),
        Other("Other"),
        DryPeas("Dry Peas"),
        Lentils("Lentils"),
        DryBeans("Dry Beans"),
        GreenBeans("Green Beans"),
        GarbanzoBeans("Garbanzo Beans"),
        Forestry("Forestry"),
        TurfSod("Turf/Sod"),
        Nursery("Nursery"),
        Greethouse("Greenhouse"),
        Corn("Corn"),
        Alfalfa("Alfalfa"),
        Carrot("Carrot"),
        Clover("Clover"),
        Grass("Grass");

        String value;

        private AcresPlanted(String type) {
            value = type;
        }

        public String getValue() {
            return value;
        }
		  
	}
	
	public enum LivestockInventory {
		Cow_Lactating,
		Cow_HeiferDevelopment,
		Cow_CalfDevelopment,
		Beef_SeedStock,
		Beef_CowCalf,
		Beef_StockerGrower,
		Beef_Feedlot,
		Beef_Show,
		Aquaculture,
		Goat_Milk,
		Goat_Meat,
		Goat_Show,
		Fur,
		Poultry_Layer,
		Poultry_Broiler,
		BeePolinators,
		BeeHoney,
		Sheep_Production,
		Sheep_Show,
		Swine_Farrow,
		Swine_Finish,
		Swine_Show,
		Equine_Training,
		Equine_Boarding,
        Equine_Breeding;
	}
}









