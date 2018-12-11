package repository.gw.generate.custom;

import java.util.ArrayList;
import java.util.Date;

public class UIPersonalPrefillPriorPolicyReported {
	
	private String carrier;
	private String policyNumber;
	private String policyStatus;
	private Date firstInsuredDate;
	private Date effectiveDate;
	private Date expirationDate;
	private ArrayList<repository.gw.generate.custom.UIPersonalPrefillPriorPolicyReportedVehicle> vehicles;
	
	public String getCarrier() {
		return carrier;
	}
	
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicyStatus() {
		return policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	public Date getFirstInsuredDate() {
		return firstInsuredDate;
	}

	public void setFirstInsuredDate(Date firstInsuredDate) {
		this.firstInsuredDate = firstInsuredDate;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public ArrayList<repository.gw.generate.custom.UIPersonalPrefillPriorPolicyReportedVehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(ArrayList<UIPersonalPrefillPriorPolicyReportedVehicle> vehicles) {
		this.vehicles = vehicles;
	}

}
