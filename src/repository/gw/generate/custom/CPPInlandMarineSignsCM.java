package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

import java.util.ArrayList;
import java.util.List;

public class CPPInlandMarineSignsCM {

	private InlandMarineCPP.SignsCoverageForm_CM_00_28_Deductible deductible = InlandMarineCPP.SignsCoverageForm_CM_00_28_Deductible.FivePercent;
	private InlandMarineCPP.SignsCoverageForm_CM_00_28_Coinsurance coinsurance = InlandMarineCPP.SignsCoverageForm_CM_00_28_Coinsurance.NinetyPercent;
	
	private List<CPPInlandMarineSignsCM_ScheduledSign> scheduledSigns = new ArrayList<>();
	
	public CPPInlandMarineSignsCM(List<CPPInlandMarineSignsCM_ScheduledSign> scheduledSigns) {
		this.scheduledSigns = scheduledSigns;
	}

	public InlandMarineCPP.SignsCoverageForm_CM_00_28_Deductible getDeductible() {
		return deductible;
	}

	public void setDeductible(InlandMarineCPP.SignsCoverageForm_CM_00_28_Deductible deductible) {
		this.deductible = deductible;
	}

	public InlandMarineCPP.SignsCoverageForm_CM_00_28_Coinsurance getCoinsurance() {
		return coinsurance;
	}

	public void setCoinsurance(InlandMarineCPP.SignsCoverageForm_CM_00_28_Coinsurance coinsurance) {
		this.coinsurance = coinsurance;
	}

	public List<CPPInlandMarineSignsCM_ScheduledSign> getScheduledSigns() {
		return scheduledSigns;
	}

	public void setScheduledSigns(List<CPPInlandMarineSignsCM_ScheduledSign> scheduledSigns) {
		this.scheduledSigns = scheduledSigns;
	}

}
