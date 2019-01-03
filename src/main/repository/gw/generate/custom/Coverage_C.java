package repository.gw.generate.custom;

import org.testng.Assert;
import repository.gw.enums.Building;
import repository.gw.enums.CoverageType;

public class Coverage_C {
	private double limitPercent = 50;
	private int additionalValue = 140000;
	private Building.ValuationMethod valuationMethod = Building.ValuationMethod.ActualCashValue;
	private repository.gw.enums.CoverageType coverageType = repository.gw.enums.CoverageType.BroadForm;
	public double getLimitPercent() {
		return limitPercent;
	}
	public void setLimitPercent(double limitPercent) {
		this.limitPercent = limitPercent;
	}
	public int getAdditionalValue() {
		return additionalValue;
	}
	public void setAdditionalValue(int additionalValue) {
		this.additionalValue = additionalValue;
	}
	public Building.ValuationMethod getValuationMethod() {
		return valuationMethod;
	}
	public void setValuationMethod(Building.ValuationMethod valuationMethod) {
		this.valuationMethod = valuationMethod;
	}
	public repository.gw.enums.CoverageType getCoverageType() {
		return coverageType;
	}
	public void setCoverageType(repository.gw.enums.CoverageType coverageType) {
		if(coverageType.equals(repository.gw.enums.CoverageType.Peril_1) || coverageType.equals(CoverageType.Peril_1Thru9)) {
			Assert.fail("Coverage C can only be set to Broad or Special forms.");
		}
		this.coverageType = coverageType;
	}
}
