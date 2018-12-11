package repository.gw.generate.custom;

import repository.gw.enums.Building;
import repository.gw.enums.CoverageType;

public class Coverage_E {

	private int limit = 5000;
	private Building.ValuationMethod valuationMethod = Building.ValuationMethod.ActualCashValue;
	private repository.gw.enums.CoverageType coverageType = repository.gw.enums.CoverageType.Peril_1;
	private boolean addGlassCoverage = false;
	private boolean addWeightOfIceAndSnow = false;
	
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
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
	public void setCoverageType(CoverageType coverageType) {
		this.coverageType = coverageType;
	}
	public boolean isAddGlassCoverage() {
		return addGlassCoverage;
	}
	public void setAddGlassCoverage(boolean addGlassCoverage) {
		this.addGlassCoverage = addGlassCoverage;
	}
	public boolean isAddWeightOfIceAndSnow() {
		return addWeightOfIceAndSnow;
	}
	public void setAddWeightOfIceAndSnow(boolean addWeightOfIceAndSnow) {
		this.addWeightOfIceAndSnow = addWeightOfIceAndSnow;
	}
	
	
	
}
