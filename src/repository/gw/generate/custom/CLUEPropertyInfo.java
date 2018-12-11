package repository.gw.generate.custom;

import services.broker.objects.lexisnexis.clueproperty.response.actual.CluePersonalProperty;

import java.util.ArrayList;

public class CLUEPropertyInfo {
	
	private CluePersonalProperty brokerReport;
	private ArrayList<repository.gw.generate.custom.UIPropertyClaimReported> claimsReported;
	
	public CluePersonalProperty getBrokerReport() {
		return brokerReport;
	}
	
	public void setBrokerReport(CluePersonalProperty brokerReport) {
		this.brokerReport = brokerReport;
	}

	public ArrayList<repository.gw.generate.custom.UIPropertyClaimReported> getClaimsReported() {
		return claimsReported;
	}

	public void setClaimsReported(ArrayList<UIPropertyClaimReported> claimsReported) {
		this.claimsReported = claimsReported;
	}	

}
