package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

import java.util.ArrayList;
import java.util.List;

public class CPPInlandMarineComputerSystems {

	private InlandMarineCPP.ComputerSystemsCoverageForm_IH_00_75_Deductible deductible = InlandMarineCPP.ComputerSystemsCoverageForm_IH_00_75_Deductible.FiveHundred;
	private InlandMarineCPP.ComputerSystemsCoverageForm_IH_00_75_Coinsurance coinsurance = InlandMarineCPP.ComputerSystemsCoverageForm_IH_00_75_Coinsurance.EightlyPercent;
	private boolean purchasedCommercialGradeAntiVirus = true;
	
	private boolean virusHarmfulCodeOrSimilar = true;
	private int virusHarmfulCodeOrSimilarLimit = 1000;
	
	private List<CPPInlandMarineComputerSystems_LocationBlanket> locationBlankets = new ArrayList<>();
	
	//Portable computers
	private List<CPPInlandMarineComputerSystems_PortableComputer> portableComputers = new ArrayList<>();
	
	//Exclusions and conditions
	private boolean theftFromUnattendedVehicleExclusion_IDCM_31_4003 = true;
	private boolean waterExclusion_IH_99_18 = true;
	
	public CPPInlandMarineComputerSystems(List<CPPInlandMarineComputerSystems_LocationBlanket> locationBlankets) {
		this.locationBlankets = locationBlankets;
		this.portableComputers.add(new CPPInlandMarineComputerSystems_PortableComputer());
		this.portableComputers.add(new CPPInlandMarineComputerSystems_PortableComputer());
	}

	public InlandMarineCPP.ComputerSystemsCoverageForm_IH_00_75_Deductible getDeductible() {
		return deductible;
	}

	public void setDeductible(InlandMarineCPP.ComputerSystemsCoverageForm_IH_00_75_Deductible deductible) {
		this.deductible = deductible;
	}

	public InlandMarineCPP.ComputerSystemsCoverageForm_IH_00_75_Coinsurance getCoinsurance() {
		return coinsurance;
	}

	public void setCoinsurance(InlandMarineCPP.ComputerSystemsCoverageForm_IH_00_75_Coinsurance coinsurance) {
		this.coinsurance = coinsurance;
	}

	public boolean isPurchasedCommercialGradeAntiVirus() {
		return purchasedCommercialGradeAntiVirus;
	}

	public void setPurchasedCommercialGradeAntiVirus(boolean purchasedCommercialGradeAntiVirus) {
		this.purchasedCommercialGradeAntiVirus = purchasedCommercialGradeAntiVirus;
	}

	public List<CPPInlandMarineComputerSystems_LocationBlanket> getLocationBlankets() {
		return locationBlankets;
	}

	public void setLocationBlankets(List<CPPInlandMarineComputerSystems_LocationBlanket> locationBlankets) {
		this.locationBlankets = locationBlankets;
	}

	public boolean isTheftFromUnattendedVehicle() {
		return theftFromUnattendedVehicleExclusion_IDCM_31_4003;
	}

	public void setTheftFromUnattendedVehicle(boolean theftFromUnattendedVehicle) {
		this.theftFromUnattendedVehicleExclusion_IDCM_31_4003 = theftFromUnattendedVehicle;
	}

	public boolean isWaterExclusion() {
		return waterExclusion_IH_99_18;
	}

	public void setWaterExclusion(boolean waterExclusion) {
		this.waterExclusion_IH_99_18 = waterExclusion;
	}

	public boolean isVirusHarmfulCodeOrSimilar() {
		return virusHarmfulCodeOrSimilar;
	}

	public void setVirusHarmfulCodeOrSimilar(boolean virusHarmfulCodeOrSimilar) {
		this.virusHarmfulCodeOrSimilar = virusHarmfulCodeOrSimilar;
	}

	public int getVirusHarmfulCodeOrSimilarLimit() {
		return virusHarmfulCodeOrSimilarLimit;
	}

	public void setVirusHarmfulCodeOrSimilarLimit(int virusHarmfulCodeOrSimilarLimit) {
		this.virusHarmfulCodeOrSimilarLimit = virusHarmfulCodeOrSimilarLimit;
	}

	public boolean isTheftFromUnattendedVehicleExclusion_IDCM_31_4003() {
		return theftFromUnattendedVehicleExclusion_IDCM_31_4003;
	}

	public void setTheftFromUnattendedVehicleExclusion_IDCM_31_4003(
			boolean theftFromUnattendedVehicleExclusion_IDCM_31_4003) {
		this.theftFromUnattendedVehicleExclusion_IDCM_31_4003 = theftFromUnattendedVehicleExclusion_IDCM_31_4003;
	}

	public boolean isWaterExclusion_IH_99_18() {
		return waterExclusion_IH_99_18;
	}

	public void setWaterExclusion_IH_99_18(boolean waterExclusion_IH_99_18) {
		this.waterExclusion_IH_99_18 = waterExclusion_IH_99_18;
	}

	public List<CPPInlandMarineComputerSystems_PortableComputer> getPortableComputers() {
		return portableComputers;
	}

	public void setPortableComputers(List<CPPInlandMarineComputerSystems_PortableComputer> portableComputers) {
		this.portableComputers = portableComputers;
	}

}
