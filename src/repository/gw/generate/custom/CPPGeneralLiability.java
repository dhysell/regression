package repository.gw.generate.custom;

import repository.gw.enums.GeneralLiability;
import repository.gw.enums.GeneralLiabilityForms;

import java.util.ArrayList;
import java.util.List;

public class CPPGeneralLiability {
	
	private boolean hasChanged = false;
	
	public List<repository.gw.enums.GeneralLiabilityForms> glForms = new ArrayList<repository.gw.enums.GeneralLiabilityForms>();

	private boolean glExposures_HasChanged = false;
	private List<repository.gw.generate.custom.CPPGeneralLiabilityExposures> glExposures = new ArrayList<repository.gw.generate.custom.CPPGeneralLiabilityExposures>();
	private repository.gw.generate.custom.CPPGeneralLiabilityCoverages glCoverages = new repository.gw.generate.custom.CPPGeneralLiabilityCoverages();

	private boolean liquorLiability = false;
	
	private boolean contractorQuestions_HasChanged = false;

	private boolean applicantMoveBuildings = false;
	private int numberBuildingsBuiltLastYear = 10;
	private int numberBuildingsBuiltThisYear = 10;
	
	private boolean areWrittenContractsObtained = true;
	private boolean areyouNamedAsAdditionalInsured = true;
	
	private boolean contractorQuestionsFilledOut = false;
	

	//percentage type contractor
	private int generalContractor = 25;
	private int subContractor = 25;
	private int developer = 25;
	private int ownerBuilder = 25;

	//percentage of work in state
	private int idaho = 20;
	private int oregon = 20;
	private int utah = 20;
	private int montana = 20;
	private int washington = 20;
	private int wyoming = 0;
	private int allOther = 0;

	private int grossAnnualReciepts = 100;

	//percentage of construction type
	private int residentialConstruction = 50;
	private int residentialNew = 50;
	private int residentialRemodel = 50;
	private int commercialConstruction = 50;
	private int commercialNew = 50;
	private int commercialRemodel = 50;


	private List<GeneralLiability.ExposureUnderwritingQuestions.ResidentialConstructionType> residentialTypes = new ArrayList<GeneralLiability.ExposureUnderwritingQuestions.ResidentialConstructionType>();

	private List<GeneralLiability.ExposureUnderwritingQuestions.CommercialConstructionType> commercialtypes = new ArrayList<GeneralLiability.ExposureUnderwritingQuestions.CommercialConstructionType>();

	private String commercialConstructionOtherDescription = "I build stuff";


	private List<GeneralLiability.ExposureUnderwritingQuestions.ConstructionActivities> constructionActivityList = new ArrayList<GeneralLiability.ExposureUnderwritingQuestions.ConstructionActivities>();

	private int AirConditioningServiceInstallation = 50; 
	private int Carpentry_Commercial = 50;
	private int CarpetRugUpholsteryCleaning = 0; 
	private int ConcreteConstruction = 0;
	private int DrivewayParkingAreaSidewalkRepairPavingRepair = 0;
	private int ElectricalWorkWithinBuildings = 0;
	private int FenceErectionContractors = 0;
	private int FurnitureOrFixturesInstallationInOffices = 0;
	private int GradingOfLand = 0;
	private int HeatOrHeat_AirConditioningInstallation_service_NOLPG = 0;
	private int Insulation_NoFoamOrChemical = 0;
	private int Janitorial = 0;
	private int Masonry = 0;
	private int MetalErection_Structural = 0;
	private int PaintingInterior = 0;
	private int PlasteringOrStuccoWork_No_EFIS = 0;
	private int PlumbingResidential = 0;
	private int RoofingResidential_NoRe_Roofing = 0;
	private int SignPaintingOrLettering_Inside_NoSpraying = 0;
	private int SupervisorOnly = 0;
	private int WindowCleaning_3StoriesOrLess = 0;

	private int ApplianceInstallation_HouseholdOrCommercial = 50; 
	private int Carpentry_Residential = 0;
	private int CeilingOrWallInstallation_Metal = 0;
	private int DoorWindoWorassembledmillworkinstallationmetal = 0;
	private int DrywallOrWallboardinstallation = 0;
	private int Excavation = 0;
	private int FloorCoveringInstalation_NotTileOrStone = 0;
	private int GlassDealersandGlaziers_SalesAndInstallation = 0;
	private int GutterInstallation = 0;
	private int HouseFurnishingInstallation = 0;
	private int InteriorDecorators = 0;
	private int LawnSprinklerInstallation = 0;
	private int MetalErection_NotStructural = 0;
	private int Painting_Exterior_LessThan3Stories = 0;
	private int PaperHanging = 0;
	private int Plumbing_Commercial_Notindustrial_NoBoiler = 0;
	private int RoofingCommercial_NoRe_Roofing = 0;
	private int SidingInstallation_3StoriesOrLess = 0;
	private int SnowRemoval = 0;
	private int TileStoneMarbleMosaic_Interior = 0;









	// MODIFIERS
	// LINE REVIEW

	public CPPGeneralLiability() {
		construct();
	}

	public CPPGeneralLiability(repository.gw.generate.custom.CPPGeneralLiabilityCoverages GLCoverages,
                               List<repository.gw.generate.custom.CPPGeneralLiabilityExposures> GLExposures) {
		this.glCoverages = GLCoverages;
		this.glExposures = GLExposures;
		construct();
	}
	
	// default construction to always perform on this class
	public void construct() {
		this.residentialTypes.add(GeneralLiability.ExposureUnderwritingQuestions.ResidentialConstructionType.One_TwoFamily);
		this.commercialtypes.add(GeneralLiability.ExposureUnderwritingQuestions.CommercialConstructionType.Retail);
		this.constructionActivityList.add(GeneralLiability.ExposureUnderwritingQuestions.ConstructionActivities.AirConditioningServiceInstallation);
		this.constructionActivityList.add(GeneralLiability.ExposureUnderwritingQuestions.ConstructionActivities.Carpentry_Commercial);
	}

	public repository.gw.generate.custom.CPPGeneralLiabilityCoverages getCPPGeneralLiabilityCoverages() {
		return glCoverages;
	}

	public void setCPPGeneralLiabilityCoverages(repository.gw.generate.custom.CPPGeneralLiabilityCoverages coverages) {
		this.glCoverages = coverages;
	}

	public List<repository.gw.generate.custom.CPPGeneralLiabilityExposures> getCPPGeneralLiabilityExposures() {
		return glExposures;
	}

	public void setCPPGeneralLiabilityExposures(List<CPPGeneralLiabilityExposures> exposures) {
		this.glExposures = exposures;
	}

	public repository.gw.generate.custom.CPPGeneralLiabilityCoverages getGLCoverages() {
		return glCoverages;
	}

	public void setGLCoverages(CPPGeneralLiabilityCoverages gLCoverages) {
		glCoverages = gLCoverages;
	}

	public boolean isLiquorLiability() {
		return liquorLiability;
	}

	public void setLiquorLiability(boolean liquorLiability) {
		this.liquorLiability = liquorLiability;
	}

	public boolean isApplicantMoveBuildings() {
		return applicantMoveBuildings;
	}

	public void setApplicantMoveBuildings(boolean applicantMoveBuildings) {
		this.applicantMoveBuildings = applicantMoveBuildings;
	}

	public int getNumberBuildingsBuiltLastYear() {
		return numberBuildingsBuiltLastYear;
	}

	public void setNumberBuildingsBuiltLastYear(int numberBuildingsBuiltLastYear) {
		this.numberBuildingsBuiltLastYear = numberBuildingsBuiltLastYear;
	}

	public int getNumberBuildingsBuiltThisYear() {
		return numberBuildingsBuiltThisYear;
	}

	public void setNumberBuildingsBuiltThisYear(int numberBuildingsBuiltThisYear) {
		this.numberBuildingsBuiltThisYear = numberBuildingsBuiltThisYear;
	}

	public int getGeneralContractor() {
		return generalContractor;
	}

	public void setGeneralContractor(int generalContractor) {
		this.generalContractor = generalContractor;
	}

	public int getSubContractor() {
		return subContractor;
	}

	public void setSubContractor(int subContractor) {
		this.subContractor = subContractor;
	}

	public int getDeveloper() {
		return developer;
	}

	public void setDeveloper(int developer) {
		this.developer = developer;
	}

	public int getOwnerBuilder() {
		return ownerBuilder;
	}

	public void setOwnerBuilder(int ownerBuilder) {
		this.ownerBuilder = ownerBuilder;
	}

	public int getIdaho() {
		return idaho;
	}

	public void setIdaho(int idaho) {
		this.idaho = idaho;
	}

	public int getOregon() {
		return oregon;
	}

	public void setOregon(int oregon) {
		this.oregon = oregon;
	}

	public int getUtah() {
		return utah;
	}

	public void setUtah(int utah) {
		this.utah = utah;
	}

	public int getMontana() {
		return montana;
	}

	public void setMontana(int montana) {
		this.montana = montana;
	}

	public int getWashington() {
		return washington;
	}

	public void setWashington(int washington) {
		this.washington = washington;
	}

	public int getWyoming() {
		return wyoming;
	}

	public void setWyoming(int wyoming) {
		this.wyoming = wyoming;
	}

	public int getAllOther() {
		return allOther;
	}

	public void setAllOther(int allOther) {
		this.allOther = allOther;
	}

	public int getGrossAnnualReciepts() {
		return grossAnnualReciepts;
	}

	public void setGrossAnnualReciepts(int grossAnnualReciepts) {
		this.grossAnnualReciepts = grossAnnualReciepts;
	}

	public int getResidentialConstruction() {
		return residentialConstruction;
	}

	public void setResidentialConstruction(int residentialConstruction) {
		this.residentialConstruction = residentialConstruction;
	}

	public int getResidentialNew() {
		return residentialNew;
	}

	public void setResidentialNew(int residentialNew) {
		this.residentialNew = residentialNew;
	}

	public int getResidentialRemodel() {
		return residentialRemodel;
	}

	public void setResidentialRemodel(int residentialRemodel) {
		this.residentialRemodel = residentialRemodel;
	}

	public int getCommercialConstruction() {
		return commercialConstruction;
	}

	public void setCommercialConstruction(int commercialConstruction) {
		this.commercialConstruction = commercialConstruction;
	}

	public int getCommercialNew() {
		return commercialNew;
	}

	public void setCommercialNew(int commercialNew) {
		this.commercialNew = commercialNew;
	}

	public int getCommercialRemodel() {
		return commercialRemodel;
	}

	public void setCommercialRemodel(int commercialRemodel) {
		this.commercialRemodel = commercialRemodel;
	}

	public List<GeneralLiability.ExposureUnderwritingQuestions.ResidentialConstructionType> getResidentialTypes() {
		return residentialTypes;
	}

	public void setResidentialTypes(List<GeneralLiability.ExposureUnderwritingQuestions.ResidentialConstructionType> residentialTypes) {
		this.residentialTypes = residentialTypes;
	}

	public List<GeneralLiability.ExposureUnderwritingQuestions.CommercialConstructionType> getCommercialtypes() {
		return commercialtypes;
	}

	public void setCommercialtypes(List<GeneralLiability.ExposureUnderwritingQuestions.CommercialConstructionType> commercialtypes) {
		this.commercialtypes = commercialtypes;
	}

	public String getCommercialConstructionOtherDescription() {
		return commercialConstructionOtherDescription;
	}

	public void setCommercialConstructionOtherDescription(String commercialConstructionOtherDescription) {
		this.commercialConstructionOtherDescription = commercialConstructionOtherDescription;
	}

	public List<GeneralLiability.ExposureUnderwritingQuestions.ConstructionActivities> getConstructionActivityList() {
		return constructionActivityList;
	}

	public void setConstructionActivityList(List<GeneralLiability.ExposureUnderwritingQuestions.ConstructionActivities> constructionActivityList) {
		this.constructionActivityList = constructionActivityList;
	}

	public int getAirConditioningServiceInstallation() {
		return AirConditioningServiceInstallation;
	}

	public void setAirConditioningServiceInstallation(int airConditioningServiceInstallation) {
		AirConditioningServiceInstallation = airConditioningServiceInstallation;
	}

	public int getCarpentry_Commercial() {
		return Carpentry_Commercial;
	}

	public void setCarpentry_Commercial(int carpentry_Commercial) {
		Carpentry_Commercial = carpentry_Commercial;
	}

	public int getCarpetRugUpholsteryCleaning() {
		return CarpetRugUpholsteryCleaning;
	}

	public void setCarpetRugUpholsteryCleaning(int carpetRugUpholsteryCleaning) {
		CarpetRugUpholsteryCleaning = carpetRugUpholsteryCleaning;
	}

	public int getConcreteConstruction() {
		return ConcreteConstruction;
	}

	public void setConcreteConstruction(int concreteConstruction) {
		ConcreteConstruction = concreteConstruction;
	}

	public int getDrivewayParkingAreaSidewalkRepairPavingRepair() {
		return DrivewayParkingAreaSidewalkRepairPavingRepair;
	}

	public void setDrivewayParkingAreaSidewalkRepairPavingRepair(int drivewayParkingAreaSidewalkRepairPavingRepair) {
		DrivewayParkingAreaSidewalkRepairPavingRepair = drivewayParkingAreaSidewalkRepairPavingRepair;
	}

	public int getElectricalWorkWithinBuildings() {
		return ElectricalWorkWithinBuildings;
	}

	public void setElectricalWorkWithinBuildings(int electricalWorkWithinBuildings) {
		ElectricalWorkWithinBuildings = electricalWorkWithinBuildings;
	}

	public int getFenceErectionContractors() {
		return FenceErectionContractors;
	}

	public void setFenceErectionContractors(int fenceErectionContractors) {
		FenceErectionContractors = fenceErectionContractors;
	}

	public int getFurnitureOrFixturesInstallationInOffices() {
		return FurnitureOrFixturesInstallationInOffices;
	}

	public void setFurnitureOrFixturesInstallationInOffices(int furnitureOrFixturesInstallationInOffices) {
		FurnitureOrFixturesInstallationInOffices = furnitureOrFixturesInstallationInOffices;
	}

	public int getGradingOfLand() {
		return GradingOfLand;
	}

	public void setGradingOfLand(int gradingOfLand) {
		GradingOfLand = gradingOfLand;
	}

	public int getHeatOrHeat_AirConditioningInstallation_service_NOLPG() {
		return HeatOrHeat_AirConditioningInstallation_service_NOLPG;
	}

	public void setHeatOrHeat_AirConditioningInstallation_service_NOLPG(int heatOrHeat_AirConditioningInstallation_service_NOLPG) {
		HeatOrHeat_AirConditioningInstallation_service_NOLPG = heatOrHeat_AirConditioningInstallation_service_NOLPG;
	}

	public int getInsulation_NoFoamOrChemical() {
		return Insulation_NoFoamOrChemical;
	}

	public void setInsulation_NoFoamOrChemical(int insulation_NoFoamOrChemical) {
		Insulation_NoFoamOrChemical = insulation_NoFoamOrChemical;
	}

	public int getJanitorial() {
		return Janitorial;
	}

	public void setJanitorial(int janitorial) {
		Janitorial = janitorial;
	}

	public int getMasonry() {
		return Masonry;
	}

	public void setMasonry(int masonry) {
		Masonry = masonry;
	}

	public int getMetalErection_Structural() {
		return MetalErection_Structural;
	}

	public void setMetalErection_Structural(int metalErection_Structural) {
		MetalErection_Structural = metalErection_Structural;
	}

	public int getPaintingInterior() {
		return PaintingInterior;
	}

	public void setPaintingInterior(int paintingInterior) {
		PaintingInterior = paintingInterior;
	}

	public int getPlasteringOrStuccoWork_No_EFIS() {
		return PlasteringOrStuccoWork_No_EFIS;
	}

	public void setPlasteringOrStuccoWork_No_EFIS(int plasteringOrStuccoWork_No_EFIS) {
		PlasteringOrStuccoWork_No_EFIS = plasteringOrStuccoWork_No_EFIS;
	}

	public int getPlumbingResidential() {
		return PlumbingResidential;
	}

	public void setPlumbingResidential(int plumbingResidential) {
		PlumbingResidential = plumbingResidential;
	}

	public int getRoofingResidential_NoRe_Roofing() {
		return RoofingResidential_NoRe_Roofing;
	}

	public void setRoofingResidential_NoRe_Roofing(int roofingResidential_NoRe_Roofing) {
		RoofingResidential_NoRe_Roofing = roofingResidential_NoRe_Roofing;
	}

	public int getSignPaintingOrLettering_Inside_NoSpraying() {
		return SignPaintingOrLettering_Inside_NoSpraying;
	}

	public void setSignPaintingOrLettering_Inside_NoSpraying(int signPaintingOrLettering_Inside_NoSpraying) {
		SignPaintingOrLettering_Inside_NoSpraying = signPaintingOrLettering_Inside_NoSpraying;
	}

	public int getSupervisorOnly() {
		return SupervisorOnly;
	}

	public void setSupervisorOnly(int supervisorOnly) {
		SupervisorOnly = supervisorOnly;
	}

	public int getWindowCleaning_3StoriesOrLess() {
		return WindowCleaning_3StoriesOrLess;
	}

	public void setWindowCleaning_3StoriesOrLess(int windowCleaning_3StoriesOrLess) {
		WindowCleaning_3StoriesOrLess = windowCleaning_3StoriesOrLess;
	}

	public int getApplianceInstallation_HouseholdOrCommercial() {
		return ApplianceInstallation_HouseholdOrCommercial;
	}

	public void setApplianceInstallation_HouseholdOrCommercial(int applianceInstallation_HouseholdOrCommercial) {
		ApplianceInstallation_HouseholdOrCommercial = applianceInstallation_HouseholdOrCommercial;
	}

	public int getCarpentry_Residential() {
		return Carpentry_Residential;
	}

	public void setCarpentry_Residential(int carpentry_Residential) {
		Carpentry_Residential = carpentry_Residential;
	}

	public int getCeilingOrWallInstallation_Metal() {
		return CeilingOrWallInstallation_Metal;
	}

	public void setCeilingOrWallInstallation_Metal(int ceilingOrWallInstallation_Metal) {
		CeilingOrWallInstallation_Metal = ceilingOrWallInstallation_Metal;
	}

	public int getDoorWindoWorassembledmillworkinstallationmetal() {
		return DoorWindoWorassembledmillworkinstallationmetal;
	}

	public void setDoorWindoWorassembledmillworkinstallationmetal(int doorWindoWorassembledmillworkinstallationmetal) {
		DoorWindoWorassembledmillworkinstallationmetal = doorWindoWorassembledmillworkinstallationmetal;
	}

	public int getDrywallOrWallboardinstallation() {
		return DrywallOrWallboardinstallation;
	}

	public void setDrywallOrWallboardinstallation(int drywallOrWallboardinstallation) {
		DrywallOrWallboardinstallation = drywallOrWallboardinstallation;
	}

	public int getExcavation() {
		return Excavation;
	}

	public void setExcavation(int excavation) {
		Excavation = excavation;
	}

	public int getFloorCoveringInstalation_NotTileOrStone() {
		return FloorCoveringInstalation_NotTileOrStone;
	}

	public void setFloorCoveringInstalation_NotTileOrStone(int floorCoveringInstalation_NotTileOrStone) {
		FloorCoveringInstalation_NotTileOrStone = floorCoveringInstalation_NotTileOrStone;
	}

	public int getGlassDealersandGlaziers_SalesAndInstallation() {
		return GlassDealersandGlaziers_SalesAndInstallation;
	}

	public void setGlassDealersandGlaziers_SalesAndInstallation(int glassDealersandGlaziers_SalesAndInstallation) {
		GlassDealersandGlaziers_SalesAndInstallation = glassDealersandGlaziers_SalesAndInstallation;
	}

	public int getGutterInstallation() {
		return GutterInstallation;
	}

	public void setGutterInstallation(int gutterInstallation) {
		GutterInstallation = gutterInstallation;
	}

	public int getHouseFurnishingInstallation() {
		return HouseFurnishingInstallation;
	}

	public void setHouseFurnishingInstallation(int houseFurnishingInstallation) {
		HouseFurnishingInstallation = houseFurnishingInstallation;
	}

	public int getInteriorDecorators() {
		return InteriorDecorators;
	}

	public void setInteriorDecorators(int interiorDecorators) {
		InteriorDecorators = interiorDecorators;
	}

	public int getLawnSprinklerInstallation() {
		return LawnSprinklerInstallation;
	}

	public void setLawnSprinklerInstallation(int lawnSprinklerInstallation) {
		LawnSprinklerInstallation = lawnSprinklerInstallation;
	}

	public int getMetalErection_NotStructural() {
		return MetalErection_NotStructural;
	}

	public void setMetalErection_NotStructural(int metalErection_NotStructural) {
		MetalErection_NotStructural = metalErection_NotStructural;
	}

	public int getPainting_Exterior_LessThan3Stories() {
		return Painting_Exterior_LessThan3Stories;
	}

	public void setPainting_Exterior_LessThan3Stories(int painting_Exterior_LessThan3Stories) {
		Painting_Exterior_LessThan3Stories = painting_Exterior_LessThan3Stories;
	}

	public int getPaperHanging() {
		return PaperHanging;
	}

	public void setPaperHanging(int paperHanging) {
		PaperHanging = paperHanging;
	}

	public int getPlumbing_Commercial_Notindustrial_NoBoiler() {
		return Plumbing_Commercial_Notindustrial_NoBoiler;
	}

	public void setPlumbing_Commercial_Notindustrial_NoBoiler(int plumbing_Commercial_Notindustrial_NoBoiler) {
		Plumbing_Commercial_Notindustrial_NoBoiler = plumbing_Commercial_Notindustrial_NoBoiler;
	}

	public int getRoofingCommercial_NoRe_Roofing() {
		return RoofingCommercial_NoRe_Roofing;
	}

	public void setRoofingCommercial_NoRe_Roofing(int roofingCommercial_NoRe_Roofing) {
		RoofingCommercial_NoRe_Roofing = roofingCommercial_NoRe_Roofing;
	}

	public int getSidingInstallation_3StoriesOrLess() {
		return SidingInstallation_3StoriesOrLess;
	}

	public void setSidingInstallation_3StoriesOrLess(int sidingInstallation_3StoriesOrLess) {
		SidingInstallation_3StoriesOrLess = sidingInstallation_3StoriesOrLess;
	}

	public int getSnowRemoval() {
		return SnowRemoval;
	}

	public void setSnowRemoval(int snowRemoval) {
		SnowRemoval = snowRemoval;
	}

	public int getTileStoneMarbleMosaic_Interior() {
		return TileStoneMarbleMosaic_Interior;
	}

	public void setTileStoneMarbleMosaic_Interior(int tileStoneMarbleMosaic_Interior) {
		TileStoneMarbleMosaic_Interior = tileStoneMarbleMosaic_Interior;
	}

	public boolean isAreWrittenContractsObtained() {
		return areWrittenContractsObtained;
	}

	public void setAreWrittenContractsObtained(boolean areWrittenContractsObtained) {
		this.areWrittenContractsObtained = areWrittenContractsObtained;
	}

	public boolean isAreyouNamedAsAdditionalInsured() {
		return areyouNamedAsAdditionalInsured;
	}

	public void setAreyouNamedAsAdditionalInsured(boolean areyouNamedAsAdditionalInsured) {
		this.areyouNamedAsAdditionalInsured = areyouNamedAsAdditionalInsured;
	}

	public boolean isContractorQuestionsFilledOut() {
		return contractorQuestionsFilledOut;
	}

	public void setContractorQuestionsFilledOut(boolean contractorQuestionsFilledOut) {
		this.contractorQuestionsFilledOut = contractorQuestionsFilledOut;
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
		setGlExposures_HasChanged(false);
		glCoverages.setHasChanged(false);
		setContractorQuestions_HasChanged(false);
	}
	
	public void resetChangeCondition() {
		setHasChanged(false);
	}

	public boolean glExposures_HasChanged() {
		return glExposures_HasChanged;
	}

	public void setGlExposures_HasChanged(boolean glExposures_HasChanged) {
		this.glExposures_HasChanged = glExposures_HasChanged;
	}

	public boolean contractorQuestions_HasChanged() {
		return contractorQuestions_HasChanged;
	}

	public void setContractorQuestions_HasChanged(boolean contractorQuestions_HasChanged) {
		this.contractorQuestions_HasChanged = contractorQuestions_HasChanged;
	}

	public List<repository.gw.enums.GeneralLiabilityForms> getGlForms() {
		return glForms;
	}

	public void setGlForms(List<GeneralLiabilityForms> glForms) {
		this.glForms = glForms;
	}

}
