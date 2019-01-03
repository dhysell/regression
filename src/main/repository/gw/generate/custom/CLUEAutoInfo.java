package repository.gw.generate.custom;

import services.broker.objects.lexisnexis.clueauto.response.actual.CluePersonalAuto;

import java.util.ArrayList;

public class CLUEAutoInfo {
	
	private CluePersonalAuto brokerReport;
	private ArrayList<repository.gw.generate.custom.UIAutoClaimReported> claimsReported;
	
	public CluePersonalAuto getBrokerReport() {
		return brokerReport;
	}
	
	public void setBrokerReport(CluePersonalAuto brokerReport) {
		this.brokerReport = brokerReport;
	}

	public ArrayList<repository.gw.generate.custom.UIAutoClaimReported> getClaimsReported() {
		return claimsReported;
	}

	public void setClaimsReported(ArrayList<UIAutoClaimReported> claimsReported) {
		this.claimsReported = claimsReported;
	}
	
	

}
