package repository.gw.generate.custom;

import com.idfbins.enums.State;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;
import repository.gw.enums.CommercialAutoUWIssues;
import repository.gw.enums.VehicleOwnershipType;
import repository.gw.helpers.NumberUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Vehicle {

	private boolean hasChanged = false;

	// VEHICLE INFORMATION
	private String vehicleName = "My Car";
	private Contact driverPL = null;
	private AddressInfo garagedAt = null;
	private int vehicleNumber = 0;
	private repository.gw.enums.Vehicle.VehicleType vehicleType = repository.gw.enums.Vehicle.VehicleType.PrivatePassenger;
	private repository.gw.enums.Vehicle.VehicleTypePL vehicleTypePL = repository.gw.enums.Vehicle.VehicleTypePL.PrivatePassenger;
	private String vin = "VIN" + NumberUtils.generateRandomNumberDigits(5);
	private int modelYear = 2015;
	private String make = "Scion";
	private String model = "FR-S";
	private repository.gw.enums.Vehicle.BodyType bodyType = repository.gw.enums.Vehicle.BodyType.Coupe;
	private repository.gw.enums.Vehicle.SizeClass sizeClass = repository.gw.enums.Vehicle.SizeClass.LightTrucksGVWOf10000PoundsOrLess;
	private State licencedState = State.Idaho;
	private boolean overrideFactoryCostNew = false;
	private double factoryCostNew = 10000;
	private double costNew = 10000;
	private double statedValue = 10000;
	private boolean leasedToOthers = false;
	private boolean specialAttachedEquipment = false;
	private String attachedEquipmentType = "Attached";
	private double attachedEquipmentValue = 250;
	private repository.gw.enums.Vehicle.CommutingMiles commutingMiles = repository.gw.enums.Vehicle.CommutingMiles.Business;
	private repository.gw.enums.Vehicle.Usage usage = repository.gw.enums.Vehicle.Usage.FarmUse;
	private int odometer = 25000;
	private int gvw = 7000;
	private repository.gw.enums.Vehicle.MileageFactor mileageFactor = repository.gw.enums.Vehicle.MileageFactor.From2500To7499;
	private repository.gw.enums.Vehicle.Radius radius = repository.gw.enums.Vehicle.Radius.Local0To100Miles;
	private repository.gw.enums.Vehicle.LongDistanceOptions longDistanceOptions = repository.gw.enums.Vehicle.LongDistanceOptions.Mountain;
	private repository.gw.enums.Vehicle.BusinessUseClass businessUseClass = repository.gw.enums.Vehicle.BusinessUseClass.ServiceUse;
	private repository.gw.enums.Vehicle.SecondaryClassType secondaryClassType = repository.gw.enums.Vehicle.SecondaryClassType.Truckers;
	private repository.gw.enums.Vehicle.SecondaryClass secondaryClass = repository.gw.enums.Vehicle.SecondaryClass.CommonCarriers;
	private repository.gw.enums.Vehicle.SeatingCapacity seatingCapacity = repository.gw.enums.Vehicle.SeatingCapacity.None;
	private repository.gw.enums.Vehicle.VehicleTruckTypePL truckType = repository.gw.enums.Vehicle.VehicleTruckTypePL.TractorType;
	private repository.gw.enums.Vehicle.TrailerTypePL trailerType = null;
	private boolean equippedWithAMechanicalLift;
	private repository.gw.enums.CommercialAutoUWIssues uwIssue;
	List<Vehicle> vehicleList = new ArrayList<>();
	private repository.gw.enums.VehicleOwnershipType ownership = repository.gw.enums.VehicleOwnershipType.Own; // Used for portal
	private boolean noDriverAssigned = false;

	// COVERAGES
	private boolean liabilityCoverage = true;
	private boolean collision = false;
	private repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible collisionDeductible = repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible.FiveHundred500;
	private boolean comprehensive = false;
	private repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible comprehensiveDeductible = repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible.FiveHundred500;
	private boolean specifiedCauseofLoss = false;
	private repository.gw.enums.Vehicle.SpecifiedCauseOfLoss specifiedCauseOfLoss = repository.gw.enums.Vehicle.SpecifiedCauseOfLoss.FireTheftAndWindstormOnly;
	private repository.gw.enums.Vehicle.SpecifiedCauseOfLossDeductible specifiedCauseOfLossDeductible = repository.gw.enums.Vehicle.SpecifiedCauseOfLossDeductible.FiveHundred500;

	// ADDITIONAL COVERAGES
	List<repository.gw.enums.Vehicle.CAVehicleAdditionalCoverages> caVehicleAdditionalCoveragesList = new ArrayList<>();
	private repository.gw.enums.Vehicle.CAVehicleAdditionalCoverages caVehicleAdditionalCoverages;
	private repository.gw.enums.Vehicle.SpecifiedCauseOfLoss additionalCoveragesCauseOfLoss = repository.gw.enums.Vehicle.SpecifiedCauseOfLoss.LimitedSpecifiedCausesofLoss_ExceptTheft;
	private boolean AudioVisualData_CA_99_60 = false;
	private int AudioVisualDataLimit = 1000;
	private String audioVisualDataTypeOfEquipment = "It Goes Boom Boom!";
	private repository.gw.enums.Vehicle.RentalReimbursementNumberOfDays rentalNumberOfDays = repository.gw.enums.Vehicle.RentalReimbursementNumberOfDays.Days40;
	private repository.gw.enums.Vehicle.RentalReimbursementLimitPerDay rentalDailyLimit = repository.gw.enums.Vehicle.RentalReimbursementLimitPerDay.ThirtyFive35;
	private boolean autoLoanGapCoverage_CA2071 = false;


	// EXCLUSIONS AND CONDITIONS
	List<repository.gw.enums.Vehicle.CAVehicleExclusionsAndConditions> caVehicleExclusionsAndConditionsList = new ArrayList<>();
	private repository.gw.enums.Vehicle.CAVehicleExclusionsAndConditions caVehicleExclusionsAndConditions;
	private String locationOfDamage = "";
	private String damageItem = "";
	private String damageDescription = "";
	private String explanationOfDamage = "Little Bunny Foo Foo";

	//ADDITIONAL INTERESTS
	List<AdditionalInterest> additionalInterest = new ArrayList<>();

	// Squire coverages
	private boolean squireCollision = false;
	private repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible squireCollisionDeductible;
	private boolean squireComprehensive = false;
	private boolean squireRentalRemburesement = false;
	private PARentalReimbursement squireRentalDeductible;
	private boolean emergencyRoadside = false;
	private boolean additionalLivingExpense = false;
	private boolean fireAndTheft = false;

	// Squire exclusions
	private boolean showCar303 = false;
	private boolean deletion310 = false;
	private String deletion310Description;
	private repository.gw.enums.Vehicle.HowManyTimesAYearDoesApplicantTravelOver300Miles howManyTimesAYearDoesApplicantTravelOver300Miles = repository.gw.enums.Vehicle.HowManyTimesAYearDoesApplicantTravelOver300Miles.OneToFour;
	private boolean doesApplicantInsuredBackHaulOrTransportGoodsWhileNotOperatingUnderTheAuthorityOfAnotherCarrier;
	private boolean doesThePartyWhoseAuthorityUnderWhichTheApplicantInsuredHaveCoverageForTheTrucksAndTrailersShownOnThePolicy;
	private boolean doesTheApplicantInsuredLeaseOrOperateTheirVehicleUnderAnotherPartiesPUCAuthority;
	private boolean doesTheApplicantInsuredLeaseTheVehicleWithoutADriver;


	//CPP UnderwritingQuestions
	boolean isVehicleUsedForTowing = false;
	boolean isVehicleUsedForPoliceRotation = false;
	boolean doTheyRepossessAutos = false;
	boolean doTheyAdvertiseAsTowingCompany = false;

	boolean motorHomeHaveLivingQuarters = false;
	boolean motorHomeContentsCoverage = false;
	private int MobileHomesContentsCoverageCA2016Limit = 1000;
	boolean usedToTransportMigrantWorkers = false;
	boolean existingDamage = false;
	String exitstingDamageDesc = "Cor Drove It!!";
	boolean MotorCarriersTransportGoodsWhileNotUnderAnotherCarrier = false;
	boolean MotorCarriersHasCoverageOnTrucksAndTrailersShownOnThePolicy = false;
	boolean MotorCarriersLeaseOperateVehicleUnderAnotherPartiesPUCAuthority = false;
	boolean MotorCarriersLeaseVehicleWithoutDriver = false;
	boolean VehicleBeingDrivenMoreThan300MileRadius = false;
	boolean doesApplicantInsuredHaulSandOrGravelForOthers = false;
	String whoIsTheRegisteredOwner = "Rabbit Foo Foo";

	//////////////////////////////////
	//// CONSTRUCTORS ////////
	//////////////////////////////////

    public Vehicle() {
		try {
			Vin realVin = VINHelper.getRandomVIN();
			vin = realVin.getVin();
		} catch (Exception e) {
		}
	}

	public Vehicle(AddressInfo address) {
		this.garagedAt = address;
	}

	public Vehicle(repository.gw.enums.Vehicle.VehicleType vehicleType) {
		switch (vehicleType) {
		case Trucks:
			this.setVehicleType(repository.gw.enums.Vehicle.VehicleType.Trucks);
			this.setSizeClass(repository.gw.enums.Vehicle.SizeClass.LightTrucksGVWOf10000PoundsOrLess);
			this.setRadius(repository.gw.enums.Vehicle.Radius.Local0To100Miles);
			this.setBusinessUseClass(repository.gw.enums.Vehicle.BusinessUseClass.ServiceUse);
			this.setSecondaryClassType(repository.gw.enums.Vehicle.SecondaryClassType.Truckers);
			this.setSecondaryClass(repository.gw.enums.Vehicle.SecondaryClass.CommonCarriers);
			break;
		case TruckTractors:
			this.setVehicleType(repository.gw.enums.Vehicle.VehicleType.TruckTractors);
			this.setSizeClass(repository.gw.enums.Vehicle.SizeClass.HeavyTruckTractorAGrossCombinationWeightGCWOf45000PoundsOrLess);
			this.setRadius(repository.gw.enums.Vehicle.Radius.Local0To100Miles);
			this.setBusinessUseClass(repository.gw.enums.Vehicle.BusinessUseClass.ServiceUse);
			this.setSecondaryClassType(repository.gw.enums.Vehicle.SecondaryClassType.Truckers);
			this.setSecondaryClass(repository.gw.enums.Vehicle.SecondaryClass.CommonCarriers);
			break;
		case Trailer:
			this.setVehicleType(repository.gw.enums.Vehicle.VehicleType.Trailer);
			this.setSizeClass(repository.gw.enums.Vehicle.SizeClass.Semitrailers);
			this.setBodyType(repository.gw.enums.Vehicle.BodyType.Semitrailers);
			this.setRadius(repository.gw.enums.Vehicle.Radius.Local0To100Miles);
			this.setSecondaryClassType(repository.gw.enums.Vehicle.SecondaryClassType.Truckers);
			this.setSecondaryClass(repository.gw.enums.Vehicle.SecondaryClass.CommonCarriers);
			break;
		case PublicTransportation:
			this.setVehicleType(repository.gw.enums.Vehicle.VehicleType.PublicTransportation);
			this.setBodyType(repository.gw.enums.Vehicle.BodyType.ChurchBus);
			this.setRadius(repository.gw.enums.Vehicle.Radius.Local0To100Miles);
			this.setSeatingCapacity(repository.gw.enums.Vehicle.SeatingCapacity.OneToEight);
			this.setEquippedWithAMechanicalLift(false);
			break;
		case Miscellaneous:
			this.setVehicleType(repository.gw.enums.Vehicle.VehicleType.Miscellaneous);
			this.setBodyType(repository.gw.enums.Vehicle.BodyType.FlowerCar);
			break;
		case PrivatePassenger:
			this.setVehicleType(repository.gw.enums.Vehicle.VehicleType.PrivatePassenger);
			break;
		default:
			break;
		}

	}

	public Vehicle(repository.gw.enums.Vehicle.VehicleType vehicleType, String vin, int modelYear, String make, String model) {
		this.vehicleType = vehicleType;
		this.vin = vin;
		this.modelYear = modelYear;
		this.make = make;
		this.model = model;
	}

	public Vehicle(repository.gw.enums.Vehicle.VehicleTypePL vehicleTypePL, String vin, int modelYear, String make, String model) {
		this.vehicleTypePL = vehicleTypePL;
		this.vin = vin;
		this.modelYear = modelYear;
		this.make = make;
		this.model = model;
		if (vehicleTypePL == repository.gw.enums.Vehicle.VehicleTypePL.Trailer) {
			trailerType = repository.gw.enums.Vehicle.TrailerTypePL.Camper;
		} else if (vehicleTypePL == repository.gw.enums.Vehicle.VehicleTypePL.SemiTrailer) {
			trailerType = repository.gw.enums.Vehicle.TrailerTypePL.Semi;
		}
	}


	/**
     * @param numberOfVehicles This makes a set amount of random vehicles,
     *                         Or if you put in a Zero(0) for numberOfVehicles, then it will make a random number, between 1-10, of random vehicles.
     * @return
	 */
	public Vehicle(int numberOfVehicles) {

		repository.gw.enums.Vehicle.VehicleType randomVehicleType = repository.gw.enums.Vehicle.VehicleType.getRandomVehicleType();

        if (numberOfVehicles == 0) {
            numberOfVehicles = (int) (Math.random() * 10 + 1);
        }


		List<Vehicle> vehicleList = new ArrayList<Vehicle>();
		for (int i = 0; i < numberOfVehicles; i++) {
			vehicleList.add(new Vehicle(randomVehicleType));
		}
		setVehicleList(vehicleList);
//		getVehicleList();
		
		
		/*List<Vehicle> randomList = new ArrayList<Vehicle>();
        for(int i = 0;i<=2;i++) {
            randomList.add(new Vehicle());
        }*/

    }


	//////////////////////////////////
	//// GETTERS AND SETTERS ////////
	//////////////////////////////////


	public AddressInfo getGaragedAt() {
		return garagedAt;
	}

	public void setGaragedAt(AddressInfo garagedAt) {
		this.garagedAt = garagedAt;
	}

	public int getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(int vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public repository.gw.enums.Vehicle.VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(repository.gw.enums.Vehicle.VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public repository.gw.enums.Vehicle.VehicleTypePL getVehicleTypePL() {
		return vehicleTypePL;
	}

	public void setVehicleTypePL(repository.gw.enums.Vehicle.VehicleTypePL vehicleTypePL) {
		this.vehicleTypePL = vehicleTypePL;
		//REQUIRED TO BE SET
		this.comprehensive = true;
		this.collision = true;
	}

	public repository.gw.enums.Vehicle.VehicleTruckTypePL getTruckType() {
		return truckType;
	}

	public void setTruckType(repository.gw.enums.Vehicle.VehicleTruckTypePL truckType) {
		this.truckType = truckType;
	}

	public repository.gw.enums.Vehicle.TrailerTypePL getTrailerType() {
		return trailerType;
	}

	public void setTrailerType(repository.gw.enums.Vehicle.TrailerTypePL trailerType) {
		this.trailerType = trailerType;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public int getModelYear() {
		return modelYear;
	}

	public void setModelYear(int modelYear) {
		this.modelYear = modelYear;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public repository.gw.enums.Vehicle.BodyType getBodyType() {
		return bodyType;
	}

	public void setBodyType(repository.gw.enums.Vehicle.BodyType bodyType) {
		this.bodyType = bodyType;
	}

	public repository.gw.enums.Vehicle.SizeClass getSizeClass() {
		return sizeClass;
	}

	public void setSizeClass(repository.gw.enums.Vehicle.SizeClass sizeClass) {
		this.sizeClass = sizeClass;
	}

	public State getLicencedState() {
		return licencedState;
	}

	public void setLicencedState(State licencedState) {
		this.licencedState = licencedState;
	}

	public double getFactoryCostNew() {
		return factoryCostNew;
	}

	public void setFactoryCostNew(double factoryCostNew) {
		this.factoryCostNew = factoryCostNew;
		this.costNew = factoryCostNew;
	}

	public boolean isLeasedToOthers() {
		return leasedToOthers;
	}

	public void setLeasedToOthers(boolean yesNo) {
		this.leasedToOthers = yesNo;
	}

	public boolean isSpecialAttachedEquipment() {
		return specialAttachedEquipment;
	}

	public void setSpecialAttachedEquipment(boolean checked) {
		this.specialAttachedEquipment = checked;
	}

	public String getAttachedEquipmentType() {
		return attachedEquipmentType;
	}

	public void setAttachedEquipmentType(String attachedEquipmentType) {
		this.attachedEquipmentType = attachedEquipmentType;
	}

	public double getAttachedEquipmentValue() {
		return attachedEquipmentValue;
	}

	public void setAttachedEquipmentValue(double attachedEquipmentValue) {
		this.attachedEquipmentValue = attachedEquipmentValue;
	}

	public boolean hasCollision() {
		return collision;
	}

	public void setCollision(boolean checked) {
		this.collision = checked;
	}

	public repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible getCollisionDeductible() {
		return collisionDeductible;
	}

	public void setCollisionDeductible(repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible collisionDeductible) {
		this.collisionDeductible = collisionDeductible;
	}

	public boolean hasComprehensive() {
		return comprehensive;
	}

	public void setComprehensive(boolean checked) {
		this.comprehensive = checked;
	}

	public repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible getComprehensiveDeductible() {
		return comprehensiveDeductible;
	}

	public void setComprehensiveDeductible(repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible comprehensiveDeductible) {
		this.comprehensiveDeductible = comprehensiveDeductible;
	}

	public boolean hasSpecifiedCauseofLoss() {
		return specifiedCauseofLoss;
	}

	public void setSpecifiedCauseofLoss(boolean checked) {
		this.specifiedCauseofLoss = checked;
	}

	public repository.gw.enums.Vehicle.SpecifiedCauseOfLoss getSpecifiedCauseOfLoss() {
		return specifiedCauseOfLoss;
	}

	public void setSpecifiedCauseOfLoss(repository.gw.enums.Vehicle.SpecifiedCauseOfLoss specifiedCauseOfLoss) {
		this.specifiedCauseOfLoss = specifiedCauseOfLoss;
	}

	public repository.gw.enums.Vehicle.SpecifiedCauseOfLossDeductible getSpecifiedCauseOfLossDeductible() {
		return specifiedCauseOfLossDeductible;
	}

	public void setSpecifiedCauseOfLossDeductible(repository.gw.enums.Vehicle.SpecifiedCauseOfLossDeductible specifiedCauseOfLossDeductible) {
		this.specifiedCauseOfLossDeductible = specifiedCauseOfLossDeductible;
	}

	public List<repository.gw.enums.Vehicle.CAVehicleAdditionalCoverages> getCaVehicleAdditionalCoveragesList() {
		return caVehicleAdditionalCoveragesList;
	}

	public void setCaVehicleAdditionalCoveragesList(
			List<repository.gw.enums.Vehicle.CAVehicleAdditionalCoverages> caVehicleAdditionalCoveragesList) {
		this.caVehicleAdditionalCoveragesList = caVehicleAdditionalCoveragesList;
	}

	public List<repository.gw.enums.Vehicle.CAVehicleExclusionsAndConditions> getCaVehicleExclusionsAndConditionsList() {
		return caVehicleExclusionsAndConditionsList;
	}

	public void setCaVehicleExclusionsAndConditionsList(
			List<repository.gw.enums.Vehicle.CAVehicleExclusionsAndConditions> caVehicleExclusionsAndConditionsList) {
		this.caVehicleExclusionsAndConditionsList = caVehicleExclusionsAndConditionsList;
	}

	public double getCostNew() {
		return costNew;
	}

	public void setCostNew(double costNew) {
		this.costNew = costNew;
	}

	public double getStatedValue() {
		return statedValue;
	}

	public void setStartedValue(double startedValue) {
		this.statedValue = startedValue;
	}

	public repository.gw.enums.Vehicle.CommutingMiles getCommutingMiles() {
		return commutingMiles;
	}

	public void setCommutingMiles(repository.gw.enums.Vehicle.CommutingMiles commutingMiles) {
		this.commutingMiles = commutingMiles;
	}

	public repository.gw.enums.Vehicle.Usage getUsage() {
		return usage;
	}

	public void setUsage(repository.gw.enums.Vehicle.Usage usage) {
		this.usage = usage;
	}

	public int getOdometer() {
		return odometer;
	}

	public void setOdometer(int odometer) {
		this.odometer = odometer;
	}

	public int getGvw() {
		return gvw;
	}

	public void setGvw(int gvw) {
		this.gvw = gvw;
	}

	public repository.gw.enums.Vehicle.MileageFactor getMileageFactor() {
		return mileageFactor;
	}

	public void setMileageFactor(repository.gw.enums.Vehicle.MileageFactor mileageFactor) {
		this.mileageFactor = mileageFactor;
	}

	public repository.gw.enums.Vehicle.Radius getRadius() {
		return radius;
	}

	public void setRadius(repository.gw.enums.Vehicle.Radius radius) {
		this.radius = radius;
	}

	public repository.gw.enums.Vehicle.BusinessUseClass getBusinessUseClass() {
		return businessUseClass;
	}

	public void setBusinessUseClass(repository.gw.enums.Vehicle.BusinessUseClass businessUseClass) {
		this.businessUseClass = businessUseClass;
	}

	public repository.gw.enums.Vehicle.SecondaryClassType getSecondaryClassType() {
		return secondaryClassType;
	}

	public void setSecondaryClassType(repository.gw.enums.Vehicle.SecondaryClassType secondaryClassType) {
		this.secondaryClassType = secondaryClassType;
	}

	public repository.gw.enums.Vehicle.SecondaryClass getSecondaryClass() {
		return secondaryClass;
	}

	public void setSecondaryClass(repository.gw.enums.Vehicle.SecondaryClass secondaryClass) {
		this.secondaryClass = secondaryClass;
	}

	public repository.gw.enums.Vehicle.SeatingCapacity getSeatingCapacity() {
		return seatingCapacity;
	}

	public void setSeatingCapacity(repository.gw.enums.Vehicle.SeatingCapacity seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}

	public boolean hasEquippedWithAMechanicalLift() {
		return equippedWithAMechanicalLift;
	}

	public void setEquippedWithAMechanicalLift(boolean equippedWithAMechanicalLift) {
		this.equippedWithAMechanicalLift = equippedWithAMechanicalLift;
	}

	public boolean hasSquireCollision() {
		return squireCollision;
	}

	public void setSquireCollision(boolean squireCollision) {
		this.squireCollision = squireCollision;
	}

	public repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible getSquireCollisionDeductible() {
		return squireCollisionDeductible;
	}

	public void setSquireCollisionDeductible(repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible squireCollisionDeductible) {
		this.squireCollisionDeductible = squireCollisionDeductible;
	}

	public boolean hasSquireComprehensive() {
		return squireComprehensive;
	}

	public void setSquireComprehensive(boolean squireComprehensive) {
		this.squireComprehensive = squireComprehensive;
	}

	public boolean hasSquireRentalRemburesement() {
		return squireRentalRemburesement;
	}

	public void setSquireRentalRemburesement(boolean squireRentalRemburesement) {
		this.squireRentalRemburesement = squireRentalRemburesement;
	}

	public PARentalReimbursement getSquireRentalDeductible() {
		return squireRentalDeductible;
	}

	public void setSquireRentalDeductible(PARentalReimbursement squireRentalDeductible) {
		this.squireRentalDeductible = squireRentalDeductible;
	}

	public boolean hasEmergencyRoadside() {
		return emergencyRoadside;
	}

	public void setEmergencyRoadside(boolean emergencyRoadside) {
		this.emergencyRoadside = emergencyRoadside;
	}

	public boolean hasAdditionalLivingExpense() {
		return additionalLivingExpense;
	}

	public void setAdditionalLivingExpense(boolean additionalLivingExpense) {
		this.additionalLivingExpense = additionalLivingExpense;
	}

	public boolean hasFireAndTheft() {
		return fireAndTheft;
	}

	public void setFireAndTheft(boolean fireAndTheft) {
		this.fireAndTheft = fireAndTheft;
	}

	public void setStatedValue(double statedValue) {
		this.statedValue = statedValue;
	}

	public boolean hasShowCar303() {
		return showCar303;
	}

	public void setShowCar303(boolean showCar303) {
		this.showCar303 = showCar303;
	}

	public boolean hasDeletion310() {
		return deletion310;
	}

	public void setDeletion310(boolean deletion310) {
		this.deletion310 = deletion310;
	}

	public String getDeletion310Description() {
		return deletion310Description;
	}

	public void setDeletion310Description(String deletion310Description) {
		this.deletion310Description = deletion310Description;
	}

	public repository.gw.enums.Vehicle.CAVehicleAdditionalCoverages getCAVehicleAdditionalCoverages() {
		return caVehicleAdditionalCoverages;
	}

	public void setCAVehicleAdditionalCoverages(repository.gw.enums.Vehicle.CAVehicleAdditionalCoverages caVehicleAdditionalCoverages) {
		this.caVehicleAdditionalCoverages = caVehicleAdditionalCoverages;
	}

	public boolean hasDoesApplicantInsuredBackHaulOrTransportGoodsWhileNotOperatingUnderTheAuthorityOfAnotherCarrier() {
		return doesApplicantInsuredBackHaulOrTransportGoodsWhileNotOperatingUnderTheAuthorityOfAnotherCarrier;
	}

	public void setDoesApplicantInsuredBackHaulOrTransportGoodsWhileNotOperatingUnderTheAuthorityOfAnotherCarrier(
			boolean doesApplicantInsuredBackHaulOrTransportGoodsWhileNotOperatingUnderTheAuthorityOfAnotherCarrier) {
		this.doesApplicantInsuredBackHaulOrTransportGoodsWhileNotOperatingUnderTheAuthorityOfAnotherCarrier = doesApplicantInsuredBackHaulOrTransportGoodsWhileNotOperatingUnderTheAuthorityOfAnotherCarrier;
	}

	public boolean hasDoesThePartyWhoseAuthorityUnderWhichTheApplicantInsuredHaveCoverageForTheTrucksAndTrailersShownOnThePolicy() {
		return doesThePartyWhoseAuthorityUnderWhichTheApplicantInsuredHaveCoverageForTheTrucksAndTrailersShownOnThePolicy;
	}

	public void setDoesThePartyWhoseAuthorityUnderWhichTheApplicantInsuredHaveCoverageForTheTrucksAndTrailersShownOnThePolicy(
			boolean doesThePartyWhoseAuthorityUnderWhichTheApplicantInsuredHaveCoverageForTheTrucksAndTrailersShownOnThePolicy) {
		this.doesThePartyWhoseAuthorityUnderWhichTheApplicantInsuredHaveCoverageForTheTrucksAndTrailersShownOnThePolicy = doesThePartyWhoseAuthorityUnderWhichTheApplicantInsuredHaveCoverageForTheTrucksAndTrailersShownOnThePolicy;
	}

	public boolean hasDoesTheApplicantInsuredLeaseOrOperateTheirVehicleUnderAnotherPartiesPUCAuthority() {
		return doesTheApplicantInsuredLeaseOrOperateTheirVehicleUnderAnotherPartiesPUCAuthority;
	}

	public void setDoesTheApplicantInsuredLeaseOrOperateTheirVehicleUnderAnotherPartiesPUCAuthority(
			boolean doesTheApplicantInsuredLeaseOrOperateTheirVehicleUnderAnotherPartiesPUCAuthority) {
		this.doesTheApplicantInsuredLeaseOrOperateTheirVehicleUnderAnotherPartiesPUCAuthority = doesTheApplicantInsuredLeaseOrOperateTheirVehicleUnderAnotherPartiesPUCAuthority;
	}

	public boolean hasDoesTheApplicantInsuredLeaseTheVehicleWithoutADriver() {
		return doesTheApplicantInsuredLeaseTheVehicleWithoutADriver;
	}

	public void setDoesTheApplicantInsuredLeaseTheVehicleWithoutADriver(
			boolean doesTheApplicantInsuredLeaseTheVehicleWithoutADriver) {
		this.doesTheApplicantInsuredLeaseTheVehicleWithoutADriver = doesTheApplicantInsuredLeaseTheVehicleWithoutADriver;
	}

	public repository.gw.enums.Vehicle.CAVehicleExclusionsAndConditions getVehicleExclusionsAndConditions() {
		return caVehicleExclusionsAndConditions;
	}

	public void setCAVehicleExclusionsAndConditions(repository.gw.enums.Vehicle.CAVehicleExclusionsAndConditions caVehicleExclusionsAndConditions) {
		this.caVehicleExclusionsAndConditions = caVehicleExclusionsAndConditions;
	}

	public repository.gw.enums.Vehicle.HowManyTimesAYearDoesApplicantTravelOver300Miles getHowManyTimesAYearDoesApplicantTravelOver300Miles() {
		return howManyTimesAYearDoesApplicantTravelOver300Miles;
	}

	public void setHowManyTimesAYearDoesApplicantTravelOver300Miles(
			repository.gw.enums.Vehicle.HowManyTimesAYearDoesApplicantTravelOver300Miles howManyTimesAYearDoesApplicantTravelOver300Miles) {
		this.howManyTimesAYearDoesApplicantTravelOver300Miles = howManyTimesAYearDoesApplicantTravelOver300Miles;
	}

	public boolean isMotorHomeHaveLivingQuarters() {
		return motorHomeHaveLivingQuarters;
	}

	public void setMotorHomeHaveLivingQuarters(boolean motorHomeHaveLivingQuarters) {
		this.motorHomeHaveLivingQuarters = motorHomeHaveLivingQuarters;
	}

	public boolean isMotorHomeContentsCoverage() {
		return motorHomeContentsCoverage;
	}

	public void setMotorHomeContentsCoverage(boolean motorHomeContentsCoverage) {
		this.motorHomeContentsCoverage = motorHomeContentsCoverage;
	}

	public boolean isUsedToTransportMigrantWorkers() {
		return usedToTransportMigrantWorkers;
	}

	public void setUsedToTransportMigrantWorkers(
			boolean usedToTransportMigrantWorkers) {
		this.usedToTransportMigrantWorkers = usedToTransportMigrantWorkers;
	}

	public boolean hasExistingDamage() {
		return existingDamage;
	}

	public void setExistingDamage(boolean existingDamage) {
		this.existingDamage = existingDamage;
	}

	public String getExitstingDamageDesc() {
		return exitstingDamageDesc;
	}

	public void setExitstingDamageDesc(String exitstingDamageDesc) {
		this.exitstingDamageDesc = exitstingDamageDesc;
	}

	public int getMobileHomesContentsCoverageCA2016Limit() {
		return MobileHomesContentsCoverageCA2016Limit;
	}

	public void setMobileHomesContentsCoverageCA2016Limit(
			int mobileHomesContentsCoverageCA2016Limit) {
		MobileHomesContentsCoverageCA2016Limit = mobileHomesContentsCoverageCA2016Limit;
	}

	public repository.gw.enums.Vehicle.SpecifiedCauseOfLoss getAdditionalCoveragesCauseOfLoss() {
		return additionalCoveragesCauseOfLoss;
	}

	public void setAdditionalCoveragesCauseOfLoss(
			repository.gw.enums.Vehicle.SpecifiedCauseOfLoss additionalCoveragesCauseOfLoss) {
		this.additionalCoveragesCauseOfLoss = additionalCoveragesCauseOfLoss;
	}

	public int getAudioVisualDataLimit() {
		return AudioVisualDataLimit;
	}

	public void setAudioVisualDataLimit(int audioVisualDataLimit) {
		AudioVisualDataLimit = audioVisualDataLimit;
	}

	public String getAudioVisualDataTypeOfEquipment() {
		return audioVisualDataTypeOfEquipment;
	}

	public void setAudioVisualDataTypeOfEquipment(
			String audioVisualDataTypeOfEquipment) {
		this.audioVisualDataTypeOfEquipment = audioVisualDataTypeOfEquipment;
	}

	public repository.gw.enums.Vehicle.RentalReimbursementNumberOfDays getRentalNumberOfDays() {
		return rentalNumberOfDays;
	}

	public void setRentalNumberOfDays(repository.gw.enums.Vehicle.RentalReimbursementNumberOfDays rentalNumberOfDays) {
		this.rentalNumberOfDays = rentalNumberOfDays;
	}

	public repository.gw.enums.Vehicle.RentalReimbursementLimitPerDay getRentalDailyLimit() {
		return rentalDailyLimit;
	}

	public void setRentalDailyLimit(repository.gw.enums.Vehicle.RentalReimbursementLimitPerDay rentalDailyLimit) {
		this.rentalDailyLimit = rentalDailyLimit;
	}

	public Contact getDriverPL() {
        if (driverPL == null && !noDriverAssigned) {
			System.out.println("DriverPL for this vehicle is null... so we will be setting the driver to the primary named insured.  This does not set the noDriverAssigned checkbox.");
		}
		return driverPL;
	}

	public void setDriverPL(Contact driverPL) {
		this.driverPL = driverPL;
	}

	public repository.gw.enums.Vehicle.LongDistanceOptions getLongDistanceOptions() {
		return longDistanceOptions;
	}

	public void setLongDistanceOptions(repository.gw.enums.Vehicle.LongDistanceOptions longDistanceOptions) {
		this.longDistanceOptions = longDistanceOptions;
	}

	public boolean isVehicleBeingDrivenMoreThan300MileRadius() {
		return VehicleBeingDrivenMoreThan300MileRadius;
	}

	public void setVehicleBeingDrivenMoreThan300MileRadius(boolean vehicleBeingDrivenMoreThan300MileRadius) {
		VehicleBeingDrivenMoreThan300MileRadius = vehicleBeingDrivenMoreThan300MileRadius;
	}

	public boolean isDoesApplicantInsuredHaulSandOrGravelForOthers() {
		return doesApplicantInsuredHaulSandOrGravelForOthers;
	}

	public void setDoesApplicantInsuredHaulSandOrGravelForOthers(boolean doesApplicantInsuredHaulSandOrGravelForOthers) {
		this.doesApplicantInsuredHaulSandOrGravelForOthers = doesApplicantInsuredHaulSandOrGravelForOthers;
	}

	public List<AdditionalInterest> getAdditionalInterest() {
		return additionalInterest;
	}

	public void setAdditionalInterest(List<AdditionalInterest> additionalInterest) {
		this.additionalInterest = additionalInterest;
	}

	public boolean isAutoLoanGapCoverage_CA2071() {
		return autoLoanGapCoverage_CA2071;
	}

	public void setAutoLoanGapCoverage_CA2071(boolean autoLoanGapCoverage_CA2071) {
		this.autoLoanGapCoverage_CA2071 = autoLoanGapCoverage_CA2071;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public repository.gw.enums.CommercialAutoUWIssues getUwIssue() {
		return uwIssue;
	}

	public void setUwIssue(CommercialAutoUWIssues uwIssue) {
		this.uwIssue = uwIssue;
	}

	public String getLocationOfDamage() {
		return locationOfDamage;
	}

	public void setLocationOfDamage(String locationOfDamage) {
		this.locationOfDamage = locationOfDamage;
	}

	public String getDamageItem() {
		return damageItem;
	}

	public void setDamageItem(String damageItem) {
		this.damageItem = damageItem;
	}

	public String getDamageDescription() {
		return damageDescription;
	}

	public void setDamageDescription(String damageDescription) {
		this.damageDescription = damageDescription;
	}

	public String getExplanationOfDamage() {
		return explanationOfDamage;
	}

	public void setExplanationOfDamage(String explanationOfDamage) {
		this.explanationOfDamage = explanationOfDamage;
	}

	public String getWhoIsTheRegisteredOwner() {
		return whoIsTheRegisteredOwner;
	}

	public void setWhoIsTheRegisteredOwner(String whoIsTheRegisteredOwner) {
		this.whoIsTheRegisteredOwner = whoIsTheRegisteredOwner;
	}

	public boolean isLiabilityCoverage() {
		return liabilityCoverage;
	}

	public void setLiabilityCoverage(boolean liabilityCoverage) {
		this.liabilityCoverage = liabilityCoverage;
	}

	public boolean isVehicleUsedForTowing() {
		return isVehicleUsedForTowing;
	}

	public void setVehicleUsedForTowing(boolean isVehicleUsedForTowing) {
		this.isVehicleUsedForTowing = isVehicleUsedForTowing;
	}

	public boolean isVehicleUsedForPoliceRotation() {
		return isVehicleUsedForPoliceRotation;
	}

	public void setVehicleUsedForPoliceRotation(boolean isBehicleUsedForPoliceRotation) {
		this.isVehicleUsedForPoliceRotation = isBehicleUsedForPoliceRotation;
	}

	public boolean isDoTheyRepossessAutos() {
		return doTheyRepossessAutos;
	}

	public void setDoTheyRepossessAutos(boolean doTheyRepossessAutos) {
		this.doTheyRepossessAutos = doTheyRepossessAutos;
	}

	public boolean isDoTheyAdvertiseAsTowingCompany() {
		return doTheyAdvertiseAsTowingCompany;
	}

	public void setDoTheyAdvertiseAsTowingCompany(boolean doTheyAdvertiseAsTowingCompany) {
		this.doTheyAdvertiseAsTowingCompany = doTheyAdvertiseAsTowingCompany;
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

	public boolean isAudioVisualData_CA_99_60() {
		return AudioVisualData_CA_99_60;
	}

	public void setAudioVisualData_CA_99_60(boolean audioVisualData_CA_99_60) {
		AudioVisualData_CA_99_60 = audioVisualData_CA_99_60;
	}

	public boolean isOverrideFactoryCostNew() {
		return overrideFactoryCostNew;
	}

	public void setOverrideFactoryCostNew(boolean overrideFactoryCostNew) {
		this.overrideFactoryCostNew = overrideFactoryCostNew;
    }
	
	/*public List<Vehicle> setVehicleList(int numberOfVehicles) {
		
		VehicleType randomVehicleType = VehicleType.getRandomVehicleType();
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		for (int i = 0; i < numberOfVehicles; i++) {
			vehicleList.add(new Vehicle(randomVehicleType));
		}
		return vehicleList;
	} */

	public repository.gw.enums.VehicleOwnershipType getOwnership() {
		return ownership;
	}

	public void setOwnership(VehicleOwnershipType ownership) {
		this.ownership = ownership;
	}

	public List<Vehicle> getVehicleList() {
		return vehicleList;
	}

	public void setVehicleList(List<Vehicle> vehicleList) {
		this.vehicleList = vehicleList;
	}

	/**
     * @author iclouser
     * @description removes all default values of person class. So it's essentially an empty person object. Primary Use for portals but can be used for everyone.
	 * Uses java reflection.
	 */
    public void removeDefaultValues() {
        Field[] vehicleFields = Vehicle.class.getDeclaredFields();
        for (Field field : vehicleFields) {
			try {
                if (field.getType().getCanonicalName() == "boolean") {
	                field.set(this, false);
                } else if (field.getType().getCanonicalName() == "char") {
	                field.set(this, '\u0000');
                } else if ((field.getType().isPrimitive())) {
	                field.set(this, 0);
                } else {
	                field.set(this, null);
	            }
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	public boolean isNoDriverAssigned() {
		return noDriverAssigned;
	}

	public void setNoDriverAssigned(boolean noDriverAssigned) {
		this.noDriverAssigned = noDriverAssigned;
	}
}
