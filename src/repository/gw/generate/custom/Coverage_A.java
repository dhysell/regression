package repository.gw.generate.custom;

import org.testng.Assert;
import repository.gw.enums.Building;
import repository.gw.enums.CoverageType;

public class Coverage_A {
	private int limit = 100200;
	private Building.ValuationMethod valuationMethod = Building.ValuationMethod.ReplacementCost;
	private repository.gw.enums.CoverageType coverageType = repository.gw.enums.CoverageType.BroadForm;
	private boolean increasedReplacementCost = true;
	private boolean waivedGlassDeductible = true;
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
	public void setCoverageType(repository.gw.enums.CoverageType coverageType) {
		if(coverageType.equals(repository.gw.enums.CoverageType.Peril_1) || coverageType.equals(CoverageType.Peril_1Thru9)) {
			Assert.fail("Coverage type can only be set to Special or Broad Form.");
		}
		this.coverageType = coverageType;
	}
	public boolean isIncreasedReplacementCost() {
		return increasedReplacementCost;
	}
	public void setIncreasedReplacementCost(boolean increasedReplacementCost) {
		this.increasedReplacementCost = increasedReplacementCost;
	}
	public boolean isWaivedGlassDeductible() {
		return waivedGlassDeductible;
	}
	public void setWaivedGlassDeductible(boolean waivedGlassDeductible) {
		this.waivedGlassDeductible = waivedGlassDeductible;
	}
}
