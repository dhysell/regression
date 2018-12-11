package repository.gw.generate.custom;

import repository.gw.enums.GaragekeepersCoverage;

public class CPPCommercialAutoGarageKeepers {
	
	private boolean hasChanged = false;
	
	private repository.gw.generate.custom.AddressInfo address = null;
	private int garageKeeperCoverageLimit = 6001;
	private GaragekeepersCoverage.LegalOrPrimary legalOrPrimary = GaragekeepersCoverage.LegalOrPrimary.Primary;
	private boolean collision = true;
	private GaragekeepersCoverage.CollisionDeductible collisionDeductible = GaragekeepersCoverage.CollisionDeductible.FiveHundred;
	private boolean comprehensive = false;
	private GaragekeepersCoverage.ComprehensiveDeductible comprehensiveDeductible = GaragekeepersCoverage.ComprehensiveDeductible.FiveHundredAndTwoThousandFiveHundred;
	private boolean specifiedCausesOfLoss = false;
	
	public CPPCommercialAutoGarageKeepers(repository.gw.generate.custom.AddressInfo address) {
		this.address = address;
	}
	
	public int getGarageKeeperCoverageLimit() {
		return garageKeeperCoverageLimit;
	}
	public void setGarageKeeperCoverageLimit(int garageKeeperCoverageLimit) {
		this.garageKeeperCoverageLimit = garageKeeperCoverageLimit;
	}
	public GaragekeepersCoverage.LegalOrPrimary getLegalOrPrimary() {
		return legalOrPrimary;
	}
	public void setLegalOrPrimary(GaragekeepersCoverage.LegalOrPrimary legalOrPrimary) {
		this.legalOrPrimary = legalOrPrimary;
	}
	public boolean isCollision() {
		return collision;
	}
	public void setCollision(boolean collision) {
		this.collision = collision;
	}
	public GaragekeepersCoverage.CollisionDeductible getCollisionDeductible() {
		return collisionDeductible;
	}
	public void setCollisionDeductible(GaragekeepersCoverage.CollisionDeductible collisionDeductible) {
		this.collisionDeductible = collisionDeductible;
	}
	public boolean isComprehensive() {
		return comprehensive;
	}
	public void setComprehensive(boolean comprehensive) {
		this.comprehensive = comprehensive;
	}
	public GaragekeepersCoverage.ComprehensiveDeductible getComprehensiveDeductible() {
		return comprehensiveDeductible;
	}
	public void setComprehensiveDeductible(
			GaragekeepersCoverage.ComprehensiveDeductible comprehensiveDeductible) {
		this.comprehensiveDeductible = comprehensiveDeductible;
	}
	public boolean isSpecifiedCausesOfLoss() {
		return specifiedCausesOfLoss;
	}
	public void setSpecifiedCausesOfLoss(boolean specifiedCausesOfLoss) {
		this.specifiedCausesOfLoss = specifiedCausesOfLoss;
	}
	public repository.gw.generate.custom.AddressInfo getAddress() {
		return address;
	}
	public void setAddress(AddressInfo address) {
		this.address = address;
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}
}
