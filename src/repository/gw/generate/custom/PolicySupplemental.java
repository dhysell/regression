package repository.gw.generate.custom;

import repository.gw.enums.Supplemental;

import java.util.ArrayList;

public class PolicySupplemental {

	private boolean applicantOperateDaySpaYes = false;
	private boolean applicantHaveOffPremisesExposuresYes = false;
	private ArrayList<Supplemental.ApplicantHaveOffPremisesExposuresCharacteristics> applicantHaveOffPremisesExposuresCharacteristicsList = new ArrayList<Supplemental.ApplicantHaveOffPremisesExposuresCharacteristics>(
			null);

	public PolicySupplemental() {

	}

	public boolean isApplicantOperateDaySpaYes() {
		return applicantOperateDaySpaYes;
	}

	public void setApplicantOperateDaySpaYes(boolean applicantOperateDaySpaYes) {
		this.applicantOperateDaySpaYes = applicantOperateDaySpaYes;
	}

	public boolean isApplicantHaveOffPremisesExposuresYes() {
		return applicantHaveOffPremisesExposuresYes;
	}

	public void setApplicantHaveOffPremisesExposuresYes(boolean applicantHaveOffPremisesExposuresYes) {
		this.applicantHaveOffPremisesExposuresYes = applicantHaveOffPremisesExposuresYes;
	}

	public ArrayList<Supplemental.ApplicantHaveOffPremisesExposuresCharacteristics> getApplicantHaveOffPremisesExposuresCharacteristicsList() {
		return applicantHaveOffPremisesExposuresCharacteristicsList;
	}

	public void setApplicantHaveOffPremisesExposuresCharacteristicsList(
			ArrayList<Supplemental.ApplicantHaveOffPremisesExposuresCharacteristics> applicantHaveOffPremisesExposuresCharacteristics) {
		this.applicantHaveOffPremisesExposuresCharacteristicsList = applicantHaveOffPremisesExposuresCharacteristics;
	}

}
