package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

import java.util.ArrayList;
import java.util.List;

public class CPPInlandMarineCameraAndMusicalInstrumentDealers {

	private int propertyAwayFromPremisesLimit = 1000;
	private int propertyInTransitLimit = 1000;
	private int propertyNotAtPremisesLimit = 1000;
	
	private InlandMarineCPP.CameraAndMusicalInstrumentDealers_CM_00_21_Deductible deductible = InlandMarineCPP.CameraAndMusicalInstrumentDealers_CM_00_21_Deductible.FiveHundred;
	private InlandMarineCPP.CameraAndMusicalInstrumentDealers_CM_00_21_Coinsurance coinsurance = InlandMarineCPP.CameraAndMusicalInstrumentDealers_CM_00_21_Coinsurance.EightlyPercent;
	
	private List<CPPInlandMarineCameraAndMusicalInstrumentDealers_LocationBlanket> locationBlankets = new ArrayList<>();
	
	private boolean propertyAwayFromYourPremisesInTheCare = true;
	private boolean propertyInTransit = true;
	private boolean propertyNotAtYourPremisesAndNotIncludedAbove = true;
	
	private boolean theftFromUnattendedVehicleExclusion_IDCM_31_4003 = true;
	private boolean waterExclusion_IH_99_18 = true;
	
	public CPPInlandMarineCameraAndMusicalInstrumentDealers(List<CPPInlandMarineCameraAndMusicalInstrumentDealers_LocationBlanket> locationBlankets) {
		this.locationBlankets = locationBlankets;
	}

	public int getPropertyAwayFromPremisesLimit() {
		return propertyAwayFromPremisesLimit;
	}

	public void setPropertyAwayFromPremisesLimit(int propertyAwayFromPremisesLimit) {
		this.propertyAwayFromPremisesLimit = propertyAwayFromPremisesLimit;
	}

	public int getPropertyInTransitLimit() {
		return propertyInTransitLimit;
	}

	public void setPropertyInTransitLimit(int propertyInTransitLimit) {
		this.propertyInTransitLimit = propertyInTransitLimit;
	}

	public int getPropertyNotAtPremisesLimit() {
		return propertyNotAtPremisesLimit;
	}

	public void setPropertyNotAtPremisesLimit(int propertyNotAtPremisesLimit) {
		this.propertyNotAtPremisesLimit = propertyNotAtPremisesLimit;
	}

	public InlandMarineCPP.CameraAndMusicalInstrumentDealers_CM_00_21_Deductible getDeductible() {
		return deductible;
	}

	public void setDeductible(InlandMarineCPP.CameraAndMusicalInstrumentDealers_CM_00_21_Deductible deductible) {
		this.deductible = deductible;
	}

	public InlandMarineCPP.CameraAndMusicalInstrumentDealers_CM_00_21_Coinsurance getCoinsurance() {
		return coinsurance;
	}

	public void setCoinsurance(InlandMarineCPP.CameraAndMusicalInstrumentDealers_CM_00_21_Coinsurance coinsurance) {
		this.coinsurance = coinsurance;
	}

	public List<CPPInlandMarineCameraAndMusicalInstrumentDealers_LocationBlanket> getLocationBlankets() {
		return locationBlankets;
	}

	public void setLocationBlankets(
			List<CPPInlandMarineCameraAndMusicalInstrumentDealers_LocationBlanket> locationBlankets) {
		this.locationBlankets = locationBlankets;
	}

	public boolean isTheftFromUnattendedVehicleExclusion() {
		return theftFromUnattendedVehicleExclusion_IDCM_31_4003;
	}

	public void setTheftFromUnattendedVehicleExclusion(boolean theftFromUnattendedVehicleExclusion) {
		this.theftFromUnattendedVehicleExclusion_IDCM_31_4003 = theftFromUnattendedVehicleExclusion;
	}

	public boolean isWaterExclusion() {
		return waterExclusion_IH_99_18;
	}

	public void setWaterExclusion(boolean waterExclusion) {
		this.waterExclusion_IH_99_18 = waterExclusion;
	}

	public boolean isPropertyAwayFromYourPremisesInTheCare() {
		return propertyAwayFromYourPremisesInTheCare;
	}

	public void setPropertyAwayFromYourPremisesInTheCare(boolean propertyAwayFromYourPremisesInTheCare) {
		this.propertyAwayFromYourPremisesInTheCare = propertyAwayFromYourPremisesInTheCare;
	}

	public boolean isPropertyInTransit() {
		return propertyInTransit;
	}

	public void setPropertyInTransit(boolean propertyInTransit) {
		this.propertyInTransit = propertyInTransit;
	}

	public boolean isPropertyNotAtYourPremisesAndNotIncludedAbove() {
		return propertyNotAtYourPremisesAndNotIncludedAbove;
	}

	public void setPropertyNotAtYourPremisesAndNotIncludedAbove(boolean propertyNotAtYourPremisesAndNotIncludedAbove) {
		this.propertyNotAtYourPremisesAndNotIncludedAbove = propertyNotAtYourPremisesAndNotIncludedAbove;
	}

}
