package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

public class CPPInlandMarineSignsCM_ScheduledSign {

	private CPPCommercialPropertyProperty property;
	private String signLettering = "This is a sign";
	private InlandMarineCPP.SignsCoverageForm_CM_00_28_SignType signType = InlandMarineCPP.SignsCoverageForm_CM_00_28_SignType.ElectronicLED;
	private boolean interiorSign = true;
	private boolean attachedToBuilding = true;
	private int limit = 1000;
	
	public CPPInlandMarineSignsCM_ScheduledSign(CPPCommercialPropertyProperty property) {
		this.property = property;
	}

	public CPPCommercialPropertyProperty getProperty() {
		return property;
	}

	public void setProperty(CPPCommercialPropertyProperty property) {
		this.property = property;
	}

	public String getSignLettering() {
		return signLettering;
	}

	public void setSignLettering(String signLettering) {
		this.signLettering = signLettering;
	}

	public InlandMarineCPP.SignsCoverageForm_CM_00_28_SignType getSignType() {
		return signType;
	}

	public void setSignType(InlandMarineCPP.SignsCoverageForm_CM_00_28_SignType signType) {
		this.signType = signType;
	}

	public boolean isInteriorSign() {
		return interiorSign;
	}

	public void setInteriorSign(boolean interiorSign) {
		this.interiorSign = interiorSign;
	}

	public boolean isAttachedToBuilding() {
		return attachedToBuilding;
	}

	public void setAttachedToBuilding(boolean attachedToBuilding) {
		this.attachedToBuilding = attachedToBuilding;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
}
