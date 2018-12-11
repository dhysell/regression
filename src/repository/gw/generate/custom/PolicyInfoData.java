package repository.gw.generate.custom;

import java.util.Date;

public class PolicyInfoData {
	
	private Date effectiveDate;
	private Date expirationDate;
	private PrefillInfo prefillInfo;
	
	public PolicyInfoData(Date effectiveDate, Date expirationDate, PrefillInfo prefillInfo){
		setEffectiveDate(effectiveDate);
		setExpirationDate(expirationDate);
		setPrefillInfo(prefillInfo);
	}
	
	public Date getEffectiveDate(){
		return this.effectiveDate;
	}
	
	public void setEffectiveDate(Date _effectiveDate){
		this.effectiveDate = _effectiveDate;
	}
	
	public Date getExpirationDate(){
		return this.expirationDate;
	}
	
	public void setExpirationDate(Date _expirationDate){
		this.expirationDate = _expirationDate;
	}

	public PrefillInfo getPrefillInfo() {
		return prefillInfo;
	}

	public void setPrefillInfo(PrefillInfo prefillInfo) {
		this.prefillInfo = prefillInfo;
	}
	
}
