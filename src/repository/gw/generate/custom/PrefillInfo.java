package repository.gw.generate.custom;

import services.broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillReport;

import java.util.ArrayList;

public class PrefillInfo {
	
	private DataprefillReport brokerReport;
	private ArrayList<repository.gw.generate.custom.UIPersonalPrefillPartyReported> partiesReported;
	private ArrayList<repository.gw.generate.custom.UIPersonalPrefillVehicleReported> vehiclesReported;
	private ArrayList<repository.gw.generate.custom.UIPersonalPrefillPriorPolicyReported> priorPoliciesReported;

	public DataprefillReport getBrokerReport() {
		return brokerReport;
	}

	public void setBrokerReport(DataprefillReport brokerReport) {
		this.brokerReport = brokerReport;
	}

	public ArrayList<repository.gw.generate.custom.UIPersonalPrefillPartyReported> getPartiesReported() {
		return partiesReported;
	}

	public void setPartiesReported(ArrayList<UIPersonalPrefillPartyReported> partiesReported) {
		this.partiesReported = partiesReported;
	}

	public ArrayList<repository.gw.generate.custom.UIPersonalPrefillVehicleReported> getVehiclesReported() {
		return vehiclesReported;
	}

	public void setVehiclesReported(ArrayList<UIPersonalPrefillVehicleReported> vehiclesReported) {
		this.vehiclesReported = vehiclesReported;
	}

	public ArrayList<repository.gw.generate.custom.UIPersonalPrefillPriorPolicyReported> getPriorPoliciesReported() {
		return priorPoliciesReported;
	}

	public void setPriorPoliciesReported(ArrayList<UIPersonalPrefillPriorPolicyReported> priorPoliciesReported) {
		this.priorPoliciesReported = priorPoliciesReported;
	}
	

}
