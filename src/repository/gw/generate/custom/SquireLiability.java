package repository.gw.generate.custom;

import repository.gw.enums.AdditionalInterestBilling;

import java.util.ArrayList;
import java.util.List;

public class SquireLiability {
	
	public SquireLiability() {
	}
	
//	private List<SectionIICoveragesEnum> sectionIICoverages = new ArrayList<SectionIICoveragesEnum>();

	private repository.gw.enums.Property.SectionIIGeneralLiabLimit generalLiabilityLimit = repository.gw.enums.Property.SectionIIGeneralLiabLimit.Limit_100_300_100;
	private repository.gw.enums.Property.SectionIIMedicalLimit medicalLimit = repository.gw.enums.Property.SectionIIMedicalLimit.Limit_1000;
	
	private List<SectionIICoverages> sectionIICoverageList = new ArrayList<SectionIICoverages>();
	
	public SectionIICoverages getCoverageByEnum(repository.gw.enums.Property.SectionIICoveragesEnum sectionIICoverage_Enum) {
		for(SectionIICoverages coverage : sectionIICoverageList) {
			if(coverage.getSectionIICoverage().equals(sectionIICoverage_Enum)) {
				return coverage;
			}
		}
		return null;
	}
	
	
//	private boolean coverageCustomFarmingflag = false;
//	private SectionIICoveragesEnum coverageCustomFarming = SectionIICoveragesEnum.CustomFarming;
//	private String coverageCustomFarmingAmount = "1001";
//	
//	private boolean coverageNamedPersonsMedical = false;
//	private SectionIICoveragesEnum coverageNamedPersonsMedicalName = SectionIICoveragesEnum.NamedPersonsMedical;
//	private Property.SectionIICoverageNamedPersonsMedicalDeductible coverageNamedPersonsMedicalDeductible = null;
//	private ArrayList<SquireLiablityCoverageNamedPersonsMedicalPerson> coverageNamedPersonsMedicalPersons = null;
//	
//	private boolean coverageAllTerrainVehicles = false;
//	private SectionIICoveragesEnum coverageAllTerrainVehiclesName = SectionIICoveragesEnum.AllTerrainVehicles;
//	private int coverageAllTerrainVehiclesQuantity = 0;
//	
//	private boolean coverageBuffaloAndElk = false;
//	private SectionIICoveragesEnum coverageBuffaloAndElkName = SectionIICoveragesEnum.BuffaloAndElk;
//	private int coverageBuffaloAndElkBuffaloQuantity = 0;
//	private int coverageBuffaloAndElkElkQuantity = 0;
//	
//	private boolean coverageChildCare = false;
//	private SectionIICoveragesEnum coverageChildCareName = SectionIICoveragesEnum.ChildCare;
//	private int coverageChildCareQuantity = 0;
//	
//	private boolean coverageHorseBoarding = false;
//	private SectionIICoveragesEnum coverageHorseBoardingName = SectionIICoveragesEnum.HorseBoarding;
//	private int coverageHorseBoardingQuantity = 0;
//	
//	private boolean coverageHorseBoardingAndPasturing = false;
//	private SectionIICoveragesEnum coverageHorseBoardingAndPasturingName = SectionIICoveragesEnum.HorseBoardingAndPasturing;
//	private int coverageHorseBoardingAndPasturingQuantity = 0;
//	
//	private boolean coverageIncidentalOccupancy = false;
//	private SectionIICoveragesEnum coverageIncidentalOccupancyName = SectionIICoveragesEnum.IncidentalOccupancy;
//	private ArrayList<SquireLiablityCoverageIncidentalOccupancyItem> coverageIncidentalOccupancyItems = null;
//	
//	private boolean coverageMotorBoats = false;
//	private SectionIICoveragesEnum coverageMotorBoatsName = SectionIICoveragesEnum.MotorBoats;
//	private int coverageMotorBoatsQuantity = 0;
//	
//	private boolean coverageOffRoadMotorcycles = false;
//	private SectionIICoveragesEnum coverageOffRoadMotorcyclesName = SectionIICoveragesEnum.OffRoadMotorcycles;
//	private int coverageOffRoadMotorcyclesQuantity = 0;
//	
//	private boolean coveragePersonalWatercraft = false;
//	private SectionIICoveragesEnum coveragePersonalWatercraftName = SectionIICoveragesEnum.PersonalWatercraft;
//	private int coveragePersonalWatercraftQuantity = 0;
//	
//	private boolean coveragePrivateLandingStrips = false;
//	private SectionIICoveragesEnum coveragePrivateLandingStripsName = SectionIICoveragesEnum.PrivateLandingStrips;
//	private int coveragePrivateLandingStripsQuantity = 0;
//	
//	private boolean coverageRecreationalVehicles = false;
//	private SectionIICoveragesEnum coverageRecreationalVehiclesName = SectionIICoveragesEnum.RecreationalVehicles;
//	private int coverageRecreationalVehiclesQuantity = 0;
//	
//	private boolean coverageSailboats = false;
//	private SectionIICoveragesEnum coverageSailboatsName = SectionIICoveragesEnum.Sailboats;
//	private int coverageSailboatsQuantity = 0;
//	
//	private boolean coverageSeedsmanEAndO = false;
//	private SectionIICoveragesEnum coverageSeedsmanEAndOName = SectionIICoveragesEnum.SeedsmanEAndO;
//	
//	private boolean coverageSnowmobiles = false;
//	private SectionIICoveragesEnum coverageSnowmobilesName = SectionIICoveragesEnum.Snowmobiles;
//	private int coverageSnowmobilesQuantity = 0;
//	
//	private boolean coverageWatercraftLength = false;
//	private SectionIICoveragesEnum coverageWatercraftLengthName = SectionIICoveragesEnum.WatercraftLength;
//	private ArrayList<SquireLiablityCoverageWatercraftLengthItem> coverageWatercraftLengthItems = new ArrayList<SquireLiablityCoverageWatercraftLengthItem>();
//	
//	private boolean coverageLivestock = false;
//	private SectionIICoveragesEnum coverageLivestockName = SectionIICoveragesEnum.Livestock;
//	private ArrayList<SquireLiablityCoverageLivestockItem> coverageLivestockItems = new ArrayList<SquireLiablityCoverageLivestockItem>();
//	
//	private boolean cropDustingAndSpraying = false;
//	private SectionIICoveragesEnum cropDustingAndSprayingName = SectionIICoveragesEnum.CropDustingAndSpraying;
//	
//	private boolean coverageEmployerNOE1 = false;
//	private SectionIICoveragesEnum coverageEmployerNOE1Name = SectionIICoveragesEnum.EmployersNOE1;
//	private int coverageEmployerNOE1Quantity = 1;
//	
//	private boolean coverageEmployerNOE2 = false;
//	private SectionIICoveragesEnum coverageEmployerNOE2Name = SectionIICoveragesEnum.EmployersNOE2;
//	private int coverageEmployerNOE2Quantity = 8;
//	
//	private boolean coverageGolfCart = false;
//	private SectionIICoveragesEnum coverageGolfCartName = SectionIICoveragesEnum.GolfCart;
//	private int coverageGolfCartQuantity = 1;
//
//	private boolean coverageFeedLotCustomFarmingEndorsement258 = false;
//	private SectionIICoveragesEnum FeedLotCustomFarmingEndorsement258Name = SectionIICoveragesEnum.FeedlotCustomFarmingEndo258;
//	private int FeedLotCustomFarmingEndorsement258Quantity = 1;
//	
	private repository.gw.enums.AdditionalInterestBilling additionalInterestBilling = repository.gw.enums.AdditionalInterestBilling.Bill_Insured;
	private repository.gw.generate.custom.AdditionalInterest liabilityAdditionalInterest = null;
	private double liabilityAdditionalInterestPremiumAmount = 0;
	
	
//	@SuppressWarnings("serial")
//	public void setSectionIICoverages(List<SectionIICoveragesEnum> sectionIICoverages) {
//		this.sectionIICoverages = sectionIICoverages;
//		for(SectionIICoveragesEnum coverage : sectionIICoverages) {
//			switch(coverage) {
//				case AllTerrainVehicles:
//					this.setCoverageAllTerrainVehicles(true);
//					this.setCoverageAllTerrainVehiclesQuantity(1);
//					break;
//				case BuffaloAndElk:
//					this.setCoverageBuffaloAndElk(true);
//					this.setCoverageBuffaloAndElk_BuffaloQuantity(1);
//					this.setCoverageBuffaloAndElk_ElkQuantity(1);
//					break;
//				case ChildCare:
//					this.setCoverageChildCare(true);
//					this.setCoverageChildCareQuantity(1);
//					break;
//				case CropDustingAndSpraying:
//					this.setCropDustingAndSpraying(true);
//					break;
//				case CustomFarming:
//					this.setCoverageCustomFarming(true);
//					this.setCustomFarmingAmount("1001");
//					break;
//				case CustomFarmingFire:
//					
//					break;
//				case EmployersNOE1:
//					this.setCoverageEmployerNOE1(true);
//					this.setCoverageEmployerNOE1Quantity(1);
//					break;
//				case EmployersNOE2:
//					this.setCoverageEmployerNOE2(true);
//					this.setCoverageEmployerNOE2Quantity(8);
//					break;
//				case FeedlotCustomFarmingEndo258:
//					this.setCoverageFeedLotCustomFarmingEndorsement258(true);
//					this.setFeedLotCustomFarmingEndorsement258Quantity(1);
//					break;
//				case GolfCart:
//					this.setCoverageGolfCart(true);
//					this.setCoverageGolfCartQuantity(1);
//					break;
//				case HorseBoarding:
//					this.setCoverageHorseBoarding(true);
//					this.setCoverageHorseBoardingQuantity(1);
//					break;
//				case HorseBoardingAndPasturing:
//					this.setCoverageHorseBoardingAndPasturing(true);
//					this.setCoverageHorseBoardingQuantity(1);
//					break;
//				case IncidentalOccupancy:
//					this.setCoverageIncidentalOccupancy(true);
//					break;
//				case Livestock:
//					this.setCoverageLivestock(true);
//					this.setCoverageLivestockItems(new ArrayList<SquireLiablityCoverageLivestockItem>() {{
//						this.add(new SquireLiablityCoverageLivestockItem());
//					}});
//					break;
//				case MotorBoats:
//					this.setCoverageMotorBoats(true);
//					this.setCoverageMotorBoatsQuantity(1);
//					break;
//				case NamedPersonsMedical:
//					this.setCoverageNamedPersonsMedical(true);
//					break;
//				case OffRoadMotorcycles:
//					this.setCoverageOffRoadMotorcycles(true);
//					this.setCoverageOffRoadMotorcyclesQuantity(1);
//					break;
//				case PersonalWatercraft:
//					this.setCoveragePersonalWatercraft(true);
//					this.setCoveragePersonalWatercraftQuantity(1);
//					break;
//				case PrivateLandingStrips:
//					this.setCoveragePrivateLandingStrips(true);
//					this.setCoveragePrivateLandingStripsQuantity(1);
//					break;
//				case RecreationalVehicles:
//					this.setCoverageRecreationalVehicles(true);
//					this.setCoverageRecreationalVehiclesQuantity(1);
//					break;
//				case Sailboats:
//					this.setCoverageSailboats(true);
//					this.setCoverageSailboatsQuantity(1);
//					break;
//				case SeedsmanEAndO:
//					this.setCoverageSeedsmanEAndO(true);
//					break;
//				case Snowmobiles:
//					this.setCoverageSnowmobiles(true);
//					this.setCoverageSnowmobilesQuantity(1);
//					break;
//				case WatercraftLength:
//					this.setCoverageWatercraftLength(true);
//					break;
//			}
//		}
//	}
		
	
	
	public repository.gw.enums.Property.SectionIIGeneralLiabLimit getGeneralLiabilityLimit() {
		return generalLiabilityLimit;
	}
	
	public void setGeneralLiabilityLimit(repository.gw.enums.Property.SectionIIGeneralLiabLimit generalLiabilityLimit) {
		this.generalLiabilityLimit = generalLiabilityLimit;
	}

	public repository.gw.enums.Property.SectionIIMedicalLimit getMedicalLimit() {
		return medicalLimit;
	}

	public void setMedicalLimit(repository.gw.enums.Property.SectionIIMedicalLimit medicalLimit) {
		this.medicalLimit = medicalLimit;
	}
	
//	public boolean isCoverageNamedPersonsMedical() {
//		return coverageNamedPersonsMedical;
//	}
//
//	public void setCoverageNamedPersonsMedical(boolean coverageNamedPersonsMedical) {
//		this.coverageNamedPersonsMedical = coverageNamedPersonsMedical;
//	}
//
//	public SectionIICoveragesEnum getCoverageNamedPersonsMedicalName() {
//		return coverageNamedPersonsMedicalName;
//	}
//	
//	public Property.SectionIICoverageNamedPersonsMedicalDeductible getCoverageNamedPersonsMedicalDeductible() {
//		return coverageNamedPersonsMedicalDeductible;
//	}
//
//	public void setCoverageNamedPersonsMedicalDeductible(Property.SectionIICoverageNamedPersonsMedicalDeductible coverageNamedPersonsMedicalDeductible) {
//		this.coverageNamedPersonsMedicalDeductible = coverageNamedPersonsMedicalDeductible;
//	}
//
//	public ArrayList<SquireLiablityCoverageNamedPersonsMedicalPerson> getCoverageNamedPersonsMedicalPersons() {
//		return coverageNamedPersonsMedicalPersons;
//	}
//
//	public void setCoverageNamedPersonsMedicalPersons(ArrayList<SquireLiablityCoverageNamedPersonsMedicalPerson> coverageNamedPersonsMedicalPersons) {
//		this.coverageNamedPersonsMedicalPersons = coverageNamedPersonsMedicalPersons;
//	}
//
//	public boolean isCoverageAllTerrainVehicles() {
//		return coverageAllTerrainVehicles;
//	}
//
//	public void setCoverageAllTerrainVehicles(boolean coverageAllTerrainVehicles) {
//		this.coverageAllTerrainVehicles = coverageAllTerrainVehicles;
//	}
//
//	public SectionIICoveragesEnum getCoverageAllTerrainVehiclesName() {
//		return coverageAllTerrainVehiclesName;
//	}
//
//	public int getCoverageAllTerrainVehiclesQuantity() {
//		return coverageAllTerrainVehiclesQuantity;
//	}
//
//	public void setCoverageAllTerrainVehiclesQuantity(int coverageAllTerrainVehiclesQuantity) {
//		this.coverageAllTerrainVehiclesQuantity = coverageAllTerrainVehiclesQuantity;
//	}
//
//	public boolean isCoverageBuffaloAndElk() {
//		return coverageBuffaloAndElk;
//	}
//
//	public void setCoverageBuffaloAndElk(boolean coverageBuffaloAndElk) {
//		this.coverageBuffaloAndElk = coverageBuffaloAndElk;
//	}
//
//	public SectionIICoveragesEnum getCoverageBuffaloAndElkName() {
//		return coverageBuffaloAndElkName;
//	}
//
//	public int getCoverageBuffaloAndElkBuffaloQuantity() {
//		return coverageBuffaloAndElkBuffaloQuantity;
//	}
//
//	public void setCoverageBuffaloAndElk_BuffaloQuantity(int coverageBuffaloAndElkBuffaloQuantity) {
//		this.coverageBuffaloAndElkBuffaloQuantity = coverageBuffaloAndElkBuffaloQuantity;
//	}
//
//	public int getCoverageBuffaloAndElkElkQuantity() {
//		return coverageBuffaloAndElkElkQuantity;
//	}
//
//	public void setCoverageBuffaloAndElk_ElkQuantity(int coverageBuffaloAndElkElkQuantity) {
//		this.coverageBuffaloAndElkElkQuantity = coverageBuffaloAndElkElkQuantity;
//	}
//
//	public boolean isCoverageChildCare() {
//		return coverageChildCare;
//	}
//
//	public void setCoverageChildCare(boolean coverageChildCare) {
//		this.coverageChildCare = coverageChildCare;
//	}
//
//	public SectionIICoveragesEnum getCoverageChildCareName() {
//		return coverageChildCareName;
//	}
//
//	public int getCoverageChildCareQuantity() {
//		return coverageChildCareQuantity;
//	}
//
//	public void setCoverageChildCareQuantity(int coverageChildCareQuantity) {
//		this.coverageChildCareQuantity = coverageChildCareQuantity;
//	}
//
//	public boolean isCoverageHorseBoarding() {
//		return coverageHorseBoarding;
//	}
//
//	public void setCoverageHorseBoarding(boolean coverageHorseBoarding) {
//		this.coverageHorseBoarding = coverageHorseBoarding;
//	}
//
//	public SectionIICoveragesEnum getCoverageHorseBoardingName() {
//		return coverageHorseBoardingName;
//	}
//
//	public int getCoverageHorseBoardingQuantity() {
//		return coverageHorseBoardingQuantity;
//	}
//
//	public void setCoverageHorseBoardingQuantity(int coverageHorseBoardingQuantity) {
//		this.coverageHorseBoardingQuantity = coverageHorseBoardingQuantity;
//	}
//
//	public boolean isCoverageHorseBoardingAndPasturing() {
//		return coverageHorseBoardingAndPasturing;
//	}
//
//	public void setCoverageHorseBoardingAndPasturing(boolean coverageHorseBoardingAndPasturing) {
//		this.coverageHorseBoardingAndPasturing = coverageHorseBoardingAndPasturing;
//	}
//
//	public SectionIICoveragesEnum getCoverageHorseBoardingAndPasturingName() {
//		return coverageHorseBoardingAndPasturingName;
//	}
//
//	public int getCoverageHorseBoardingAndPasturingQuantity() {
//		return coverageHorseBoardingAndPasturingQuantity;
//	}
//
//	public void setCoverageHorseBoardingAndPasturingQuantity(int coverageHorseBoardingAndPasturingQuantity) {
//		this.coverageHorseBoardingAndPasturingQuantity = coverageHorseBoardingAndPasturingQuantity;
//	}
//
//	public boolean isCoverageIncidentalOccupancy() {
//		return coverageIncidentalOccupancy;
//	}
//
//	public void setCoverageIncidentalOccupancy(boolean coverageIncidentalOccupancy) {
//		this.coverageIncidentalOccupancy = coverageIncidentalOccupancy;
//	}
//
//	public SectionIICoveragesEnum getCoverageIncidentalOccupancyName() {
//		return coverageIncidentalOccupancyName;
//	}
//
//	public ArrayList<SquireLiablityCoverageIncidentalOccupancyItem> getCoverageIncidentalOccupancyItems() {
//		return coverageIncidentalOccupancyItems;
//	}
//
//	public void setCoverageIncidentalOccupancyItems(ArrayList<SquireLiablityCoverageIncidentalOccupancyItem> coverageIncidentalOccupancyItems) {
//		this.coverageIncidentalOccupancyItems = coverageIncidentalOccupancyItems;
//	}
//
//	public boolean isCoverageMotorBoats() {
//		return coverageMotorBoats;
//	}
//
//	public void setCoverageMotorBoats(boolean coverageMotorBoats) {
//		this.coverageMotorBoats = coverageMotorBoats;
//	}
//
//	public SectionIICoveragesEnum getCoverageMotorBoatsName() {
//		return coverageMotorBoatsName;
//	}
//
//	public int getCoverageMotorBoatsQuantity() {
//		return coverageMotorBoatsQuantity;
//	}
//
//	public void setCoverageMotorBoatsQuantity(int coverageMotorBoatsQuantity) {
//		this.coverageMotorBoatsQuantity = coverageMotorBoatsQuantity;
//	}
//
//	public boolean isCoverageOffRoadMotorcycles() {
//		return coverageOffRoadMotorcycles;
//	}
//
//	public void setCoverageOffRoadMotorcycles(boolean coverageOffRoadMotorcycles) {
//		this.coverageOffRoadMotorcycles = coverageOffRoadMotorcycles;
//	}
//
//	public SectionIICoveragesEnum getCoverageOffRoadMotorcyclesName() {
//		return coverageOffRoadMotorcyclesName;
//	}
//
//	public int getCoverageOffRoadMotorcyclesQuantity() {
//		return coverageOffRoadMotorcyclesQuantity;
//	}
//
//	public void setCoverageOffRoadMotorcyclesQuantity(int coverageOffRoadMotorcyclesQuantity) {
//		this.coverageOffRoadMotorcyclesQuantity = coverageOffRoadMotorcyclesQuantity;
//	}
//
//	public boolean isCoveragePersonalWatercraft() {
//		return coveragePersonalWatercraft;
//	}
//
//	public void setCoveragePersonalWatercraft(boolean coveragePersonalWatercraft) {
//		this.coveragePersonalWatercraft = coveragePersonalWatercraft;
//	}
//
//	public SectionIICoveragesEnum getCoveragePersonalWatercraftName() {
//		return coveragePersonalWatercraftName;
//	}
//
//	public int getCoveragePersonalWatercraftQuantity() {
//		return coveragePersonalWatercraftQuantity;
//	}
//
//	public void setCoveragePersonalWatercraftQuantity(int coveragePersonalWatercraftQuantity) {
//		this.coveragePersonalWatercraftQuantity = coveragePersonalWatercraftQuantity;
//	}
//
//	public boolean isCoveragePrivateLandingStrips() {
//		return coveragePrivateLandingStrips;
//	}
//
//	public void setCoveragePrivateLandingStrips(boolean coveragePrivateLandingStrips) {
//		this.coveragePrivateLandingStrips = coveragePrivateLandingStrips;
//	}
//
//	public SectionIICoveragesEnum getCoveragePrivateLandingStripsName() {
//		return coveragePrivateLandingStripsName;
//	}
//
//	public int getCoveragePrivateLandingStripsQuantity() {
//		return coveragePrivateLandingStripsQuantity;
//	}
//
//	public void setCoveragePrivateLandingStripsQuantity(int coveragePrivateLandingStripsQuantity) {
//		this.coveragePrivateLandingStripsQuantity = coveragePrivateLandingStripsQuantity;
//	}
//
//	public boolean isCoverageRecreationalVehicles() {
//		return coverageRecreationalVehicles;
//	}
//
//	public void setCoverageRecreationalVehicles(boolean coverageRecreationalVehicles) {
//		this.coverageRecreationalVehicles = coverageRecreationalVehicles;
//	}
//
//	public SectionIICoveragesEnum getCoverageRecreationalVehiclesName() {
//		return coverageRecreationalVehiclesName;
//	}
//
//	public int getCoverageRecreationalVehiclesQuantity() {
//		return coverageRecreationalVehiclesQuantity;
//	}
//
//	public void setCoverageRecreationalVehiclesQuantity(int coverageRecreationalVehiclesQuantity) {
//		this.coverageRecreationalVehiclesQuantity = coverageRecreationalVehiclesQuantity;
//	}
//
//	public boolean isCoverageSailboats() {
//		return coverageSailboats;
//	}
//
//	public void setCoverageSailboats(boolean coverageSailboats) {
//		this.coverageSailboats = coverageSailboats;
//	}
//
//	public SectionIICoveragesEnum getCoverageSailboatsName() {
//		return coverageSailboatsName;
//	}
//
//	public int getCoverageSailboatsQuantity() {
//		return coverageSailboatsQuantity;
//	}
//
//	public void setCoverageSailboatsQuantity(int coverageSailboatsQuantity) {
//		this.coverageSailboatsQuantity = coverageSailboatsQuantity;
//	}
//
//	public boolean isCoverageSeedsmanEAndO() {
//		return coverageSeedsmanEAndO;
//	}
//
//	public void setCoverageSeedsmanEAndO(boolean coverageSeedsmanEAndO) {
//		this.coverageSeedsmanEAndO = coverageSeedsmanEAndO;
//	}
//
//	public SectionIICoveragesEnum getCoverageSeedsmanEAndOName() {
//		return coverageSeedsmanEAndOName;
//	}
//
//	public boolean isCoverageSnowmobiles() {
//		return coverageSnowmobiles;
//	}
//
//	public void setCoverageSnowmobiles(boolean coverageSnowmobiles) {
//		this.coverageSnowmobiles = coverageSnowmobiles;
//	}
//
//	public SectionIICoveragesEnum getCoverageSnowmobilesName() {
//		return coverageSnowmobilesName;
//	}
//
//	public int getCoverageSnowmobilesQuantity() {
//		return coverageSnowmobilesQuantity;
//	}
//
//	public void setCoverageSnowmobilesQuantity(int coverageSnowmobilesQuantity) {
//		this.coverageSnowmobilesQuantity = coverageSnowmobilesQuantity;
//	}
//
//	public boolean isCoverageWatercraftLength() {
//		return coverageWatercraftLength;
//	}
//
//	public void setCoverageWatercraftLength(boolean coverageWatercraftLength) {
//		this.coverageWatercraftLength = coverageWatercraftLength;
//	}
//
//	public SectionIICoveragesEnum getCoverageWatercraftLengthName() {
//		return coverageWatercraftLengthName;
//	}
//
//	public ArrayList<SquireLiablityCoverageWatercraftLengthItem> getCoverageWatercraftLengthItems() {
//		return coverageWatercraftLengthItems;
//	}
//
//	public void setCoverageWatercraftLengthItems(ArrayList<SquireLiablityCoverageWatercraftLengthItem> coverageWatercraftLengthItems) {
//		this.coverageWatercraftLengthItems = coverageWatercraftLengthItems;
//	}
//
//	public boolean isCoverageLivestock() {
//		return coverageLivestock;
//	}
//
//	public void setCoverageLivestock(boolean coverageLivestock) {
//		this.coverageLivestock = coverageLivestock;
//	}
//
//	public SectionIICoveragesEnum getCoverageLivestockName() {
//		return coverageLivestockName;
//	}
//
//	public List<SquireLiablityCoverageLivestockItem> getCoverageLivestockItems() {
//		return coverageLivestockItems;
//	}
//
//	public void setCoverageLivestockItems(ArrayList<SquireLiablityCoverageLivestockItem> coverageLivestockItems) {
//		this.coverageLivestockItems = coverageLivestockItems;
//	}	
//	
//	public void setCoverageCustomFarming(boolean truefalse){
//		this.coverageCustomFarmingflag = truefalse;
//	}	
//	
//	public boolean isCoverageCustomFarming(){
//		return this.coverageCustomFarmingflag;
//	}
//	
//	public void setCustomFarmingAmount(String amount){
//		this.coverageCustomFarmingAmount = amount;
//	}
//	
//	public SectionIICoveragesEnum getCoverageCustomFarmingName(){
//		return this.coverageCustomFarming;
//	}
//	
//	public String getCustomFarmingAmount(){
//		return this.coverageCustomFarmingAmount;
//	}
//
//	public List<SectionIICoveragesEnum> getSectionIICoverages() {
//		return sectionIICoverages;
//	}
//	
//	public boolean isCropDustingAndSpraying() {
//		return cropDustingAndSpraying;
//	}
//
//	public void setCropDustingAndSpraying(boolean cropDustingAndSpraying) {
//		this.cropDustingAndSpraying = cropDustingAndSpraying;
//	}
//
//	public SectionIICoveragesEnum getCropDustingAndSprayingName() {
//		return cropDustingAndSprayingName;
//	}
//
//	public void setCropDustingAndSprayingName(SectionIICoveragesEnum cropDustingAndSprayingName) {
//		this.cropDustingAndSprayingName = cropDustingAndSprayingName;
//	}
//
//	public boolean isCoverageCustomFarmingflag() {
//		return coverageCustomFarmingflag;
//	}
//
//	public void setCoverageCustomFarmingflag(boolean coverageCustomFarmingflag) {
//		this.coverageCustomFarmingflag = coverageCustomFarmingflag;
//	}
//
//	public SectionIICoveragesEnum getCoverageCustomFarming() {
//		return coverageCustomFarming;
//	}
//
//	public void setCoverageCustomFarming(SectionIICoveragesEnum coverageCustomFarming) {
//		this.coverageCustomFarming = coverageCustomFarming;
//	}

//	public String getCoverageCustomFarmingAmount() {
//		return coverageCustomFarmingAmount;
//	}
//
//	public void setCoverageCustomFarmingAmount(String coverageCustomFarmingAmount) {
//		this.coverageCustomFarmingAmount = coverageCustomFarmingAmount;
//	}
//
//	public boolean isCoverageEmployerNOE1() {
//		return coverageEmployerNOE1;
//	}
//
//	public void setCoverageEmployerNOE1(boolean coverageEmployerNOE1) {
//		this.coverageEmployerNOE1 = coverageEmployerNOE1;
//	}
//
//	public SectionIICoveragesEnum getCoverageEmployerNOE1Name() {
//		return coverageEmployerNOE1Name;
//	}
//
//	public void setCoverageEmployerNOE1Name(SectionIICoveragesEnum coverageEmployerNOE1Name) {
//		this.coverageEmployerNOE1Name = coverageEmployerNOE1Name;
//	}
//
//	public int getCoverageEmployerNOE1Quantity() {
//		return coverageEmployerNOE1Quantity;
//	}
//
//	public void setCoverageEmployerNOE1Quantity(int coverageEmployerNOE1Quantity) {
//		this.coverageEmployerNOE1Quantity = coverageEmployerNOE1Quantity;
//	}
//
//	public boolean isCoverageEmployerNOE2() {
//		return coverageEmployerNOE2;
//	}
//
//	public void setCoverageEmployerNOE2(boolean coverageEmployerNOE2) {
//		this.coverageEmployerNOE2 = coverageEmployerNOE2;
//	}
//
//	public SectionIICoveragesEnum getCoverageEmployerNOE2Name() {
//		return coverageEmployerNOE2Name;
//	}
//
//	public void setCoverageEmployerNOE2Name(SectionIICoveragesEnum coverageEmployerNOE2Name) {
//		this.coverageEmployerNOE2Name = coverageEmployerNOE2Name;
//	}
//
//	public int getCoverageEmployerNOE2Quantity() {
//		return coverageEmployerNOE2Quantity;
//	}
//
//	public void setCoverageEmployerNOE2Quantity(int coverageEmployerNOE2Quantity) {
//		this.coverageEmployerNOE2Quantity = coverageEmployerNOE2Quantity;
//	}
//
//	public boolean isCoverageGolfCart() {
//		return coverageGolfCart;
//	}
//
//	public void setCoverageGolfCart(boolean coverageGolfCart) {
//		this.coverageGolfCart = coverageGolfCart;
//	}
//
//	public SectionIICoveragesEnum getCoverageGolfCartName() {
//		return coverageGolfCartName;
//	}
//
//	public void setCoverageGolfCartName(SectionIICoveragesEnum coverageGolfCartName) {
//		this.coverageGolfCartName = coverageGolfCartName;
//	}
//
//	public int getCoverageGolfCartQuantity() {
//		return coverageGolfCartQuantity;
//	}
//
//	public void setCoverageGolfCartQuantity(int coverageGolfCartQuantity) {
//		this.coverageGolfCartQuantity = coverageGolfCartQuantity;
//	}
//
//	public void setCoverageNamedPersonsMedicalName(SectionIICoveragesEnum coverageNamedPersonsMedicalName) {
//		this.coverageNamedPersonsMedicalName = coverageNamedPersonsMedicalName;
//	}
//
//	public void setCoverageAllTerrainVehiclesName(SectionIICoveragesEnum coverageAllTerrainVehiclesName) {
//		this.coverageAllTerrainVehiclesName = coverageAllTerrainVehiclesName;
//	}
//
//	public void setCoverageBuffaloAndElkName(SectionIICoveragesEnum coverageBuffaloAndElkName) {
//		this.coverageBuffaloAndElkName = coverageBuffaloAndElkName;
//	}
//
//	public void setCoverageChildCareName(SectionIICoveragesEnum coverageChildCareName) {
//		this.coverageChildCareName = coverageChildCareName;
//	}
//
//	public void setCoverageHorseBoardingName(SectionIICoveragesEnum coverageHorseBoardingName) {
//		this.coverageHorseBoardingName = coverageHorseBoardingName;
//	}
//
//	public void setCoverageHorseBoardingAndPasturingName(SectionIICoveragesEnum coverageHorseBoardingAndPasturingName) {
//		this.coverageHorseBoardingAndPasturingName = coverageHorseBoardingAndPasturingName;
//	}
//
//	public void setCoverageIncidentalOccupancyName(SectionIICoveragesEnum coverageIncidentalOccupancyName) {
//		this.coverageIncidentalOccupancyName = coverageIncidentalOccupancyName;
//	}
//
//	public void setCoverageMotorBoatsName(SectionIICoveragesEnum coverageMotorBoatsName) {
//		this.coverageMotorBoatsName = coverageMotorBoatsName;
//	}
//
//	public void setCoverageOffRoadMotorcyclesName(SectionIICoveragesEnum coverageOffRoadMotorcyclesName) {
//		this.coverageOffRoadMotorcyclesName = coverageOffRoadMotorcyclesName;
//	}
//
//	public void setCoveragePersonalWatercraftName(SectionIICoveragesEnum coveragePersonalWatercraftName) {
//		this.coveragePersonalWatercraftName = coveragePersonalWatercraftName;
//	}
//
//	public void setCoveragePrivateLandingStripsName(SectionIICoveragesEnum coveragePrivateLandingStripsName) {
//		this.coveragePrivateLandingStripsName = coveragePrivateLandingStripsName;
//	}
//
//	public void setCoverageRecreationalVehiclesName(SectionIICoveragesEnum coverageRecreationalVehiclesName) {
//		this.coverageRecreationalVehiclesName = coverageRecreationalVehiclesName;
//	}
//
//	public void setCoverageSailboatsName(SectionIICoveragesEnum coverageSailboatsName) {
//		this.coverageSailboatsName = coverageSailboatsName;
//	}
//
//	public void setCoverageSeedsmanEAndOName(SectionIICoveragesEnum coverageSeedsmanEAndOName) {
//		this.coverageSeedsmanEAndOName = coverageSeedsmanEAndOName;
//	}
//
//	public void setCoverageSnowmobilesName(SectionIICoveragesEnum coverageSnowmobilesName) {
//		this.coverageSnowmobilesName = coverageSnowmobilesName;
//	}
//
//	public void setCoverageWatercraftLengthName(SectionIICoveragesEnum coverageWatercraftLengthName) {
//		this.coverageWatercraftLengthName = coverageWatercraftLengthName;
//	}
//
//	public void setCoverageLivestockName(SectionIICoveragesEnum coverageLivestockName) {
//		this.coverageLivestockName = coverageLivestockName;
//	}
//
//
//
//	public boolean isCoverageFeedLotCustomFarmingEndorsement258() {
//		return coverageFeedLotCustomFarmingEndorsement258;
//	}
//
//
//
//	public void setCoverageFeedLotCustomFarmingEndorsement258(boolean coverageFeedLotCustomFarmingEndorsement258) {
//		this.coverageFeedLotCustomFarmingEndorsement258 = coverageFeedLotCustomFarmingEndorsement258;
//	}
//
//
//
//	public SectionIICoveragesEnum getFeedLotCustomFarmingEndorsement258Name() {
//		return FeedLotCustomFarmingEndorsement258Name;
//	}
//
//
//
//	public void setFeedLotCustomFarmingEndorsement258Name(SectionIICoveragesEnum feedLotCustomFarmingEndorsement258Name) {
//		FeedLotCustomFarmingEndorsement258Name = feedLotCustomFarmingEndorsement258Name;
//	}
//
//
//
//	public int getFeedLotCustomFarmingEndorsement258Quantity() {
//		return FeedLotCustomFarmingEndorsement258Quantity;
//	}
//
//
//
//	public void setFeedLotCustomFarmingEndorsement258Quantity(int feedLotCustomFarmingEndorsement258Quantity) {
//		FeedLotCustomFarmingEndorsement258Quantity = feedLotCustomFarmingEndorsement258Quantity;
//	}
	
	
	
	public repository.gw.enums.AdditionalInterestBilling getAdditionalInterestBilling() {
		return additionalInterestBilling;
	}

	public void setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling additionalInterestBilling) {
		this.additionalInterestBilling = additionalInterestBilling;
	}
	
	public void setAdditionalInterestBilling(AdditionalInterestBilling additionalInterestBilling, repository.gw.generate.custom.AdditionalInterest liabilityAdditionalInterest) {
		this.additionalInterestBilling = additionalInterestBilling;
		this.liabilityAdditionalInterest = liabilityAdditionalInterest;
	}

	public repository.gw.generate.custom.AdditionalInterest getLiabilityAdditionalInterest() {
		return liabilityAdditionalInterest;
	}

	public void setLiabilityAdditionalInterest(AdditionalInterest liabilityAdditionalInterest) {
		this.liabilityAdditionalInterest = liabilityAdditionalInterest;
	}

	public double getLiabilityAdditionalInterestPremiumAmount() {
		return liabilityAdditionalInterestPremiumAmount;
	}

	public void setLiabilityAdditionalInterestPremiumAmount(double liabilityAdditionalInterestPremiumAmount) {
		this.liabilityAdditionalInterestPremiumAmount = liabilityAdditionalInterestPremiumAmount;
	}



	public List<SectionIICoverages> getSectionIICoverageList() {
		return sectionIICoverageList;
	}



	public void setSectionIICoverageList(List<SectionIICoverages> sectionIICoverageList) {
		this.sectionIICoverageList = sectionIICoverageList;
	}

	

}
