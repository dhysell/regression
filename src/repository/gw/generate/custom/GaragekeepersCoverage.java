package repository.gw.generate.custom;

public class GaragekeepersCoverage {

	private int limit = 1000;
	private repository.gw.enums.GaragekeepersCoverage.LegalOrPrimary legalOrPrimary = repository.gw.enums.GaragekeepersCoverage.LegalOrPrimary.Primary;
	private repository.gw.enums.GaragekeepersCoverage.CollisionDeductible collisionDeductible = repository.gw.enums.GaragekeepersCoverage.CollisionDeductible.OneHundred;
	private repository.gw.enums.GaragekeepersCoverage.ComprehensiveDeductible comprehensiveDeductible = repository.gw.enums.GaragekeepersCoverage.ComprehensiveDeductible.OneHundredAndFiveHundred;
	private repository.gw.enums.GaragekeepersCoverage.SpecifiedCausesOfLoss specifiedCausesOfLoss = repository.gw.enums.GaragekeepersCoverage.SpecifiedCausesOfLoss.None;

	//////////////////////////////////
	//// CONSTRUCTORS ////////
	//////////////////////////////////

	public GaragekeepersCoverage() {

	}

	//////////////////////////////////
	//// GETTERS AND SETTERS ////////
	//////////////////////////////////

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public repository.gw.enums.GaragekeepersCoverage.LegalOrPrimary getLegalOrPrimary() {
		return legalOrPrimary;
	}

	public void setLegalOrPrimary(repository.gw.enums.GaragekeepersCoverage.LegalOrPrimary legalOrPrimary) {
		this.legalOrPrimary = legalOrPrimary;
	}

	public repository.gw.enums.GaragekeepersCoverage.CollisionDeductible getCollisionDeductible() {
		return collisionDeductible;
	}

	public void setCollisionDeductible(repository.gw.enums.GaragekeepersCoverage.CollisionDeductible collisionDeductible) {
		this.collisionDeductible = collisionDeductible;
	}

	public repository.gw.enums.GaragekeepersCoverage.ComprehensiveDeductible getComprehensiveDeductible() {
		return comprehensiveDeductible;
	}

	public void setComprehensiveDeductible(repository.gw.enums.GaragekeepersCoverage.ComprehensiveDeductible comprehensiveDeductible) {
		this.comprehensiveDeductible = comprehensiveDeductible;
	}

	public repository.gw.enums.GaragekeepersCoverage.SpecifiedCausesOfLoss getSpecifiedCausesOfLoss() {
		return specifiedCausesOfLoss;
	}

	public void setSpecifiedCausesOfLoss(repository.gw.enums.GaragekeepersCoverage.SpecifiedCausesOfLoss specifiedCausesOfLoss) {
		this.specifiedCausesOfLoss = specifiedCausesOfLoss;
	}

}
