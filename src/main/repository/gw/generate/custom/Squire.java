package repository.gw.generate.custom;

import repository.gw.enums.SquireEligibility;

import java.util.ArrayList;

public class Squire extends PolicyCommon {

	public int summedNumAcres = 0;
	public repository.gw.enums.SquireEligibility squireEligibility = repository.gw.enums.SquireEligibility.City;
	public boolean alwaysOrderCreditReport = true;
	public boolean overridePropertyOwnerValidation = false;
	public boolean hagerty = false;

    //POLICY MEMBERS
	public ArrayList<Contact> policyMembers = new ArrayList<Contact>();

	//INSURANCE SCORE
	public InsuranceScoreInfo insuranceScoreReport = new InsuranceScoreInfo();

	//SECTION 1 & 2 PROPERTY & LIABLILITY
	public repository.gw.generate.custom.SquirePropertyAndLiability propertyAndLiability = new SquirePropertyAndLiability();


	//SECTION 3 AUTO LINE
	public SquirePersonalAuto squirePA = new SquirePersonalAuto();


	//SECTION 4  INLAND MARINE
	public repository.gw.generate.custom.SquireInlandMarine inlandMarine = new SquireInlandMarine();


    public Squire() {

    }

    public Squire(repository.gw.enums.SquireEligibility elegibility) {
        this.squireEligibility = elegibility;
    }

	public boolean isCity() {
		return squireEligibility.equals(repository.gw.enums.SquireEligibility.City);
	}
	public boolean isFarmAndRanch() {
		return squireEligibility.equals(repository.gw.enums.SquireEligibility.FarmAndRanch);
    }
	public boolean isCountry() {
		return squireEligibility.equals(repository.gw.enums.SquireEligibility.Country);
	}
	public boolean isCustomAuto() {
		return squireEligibility.equals(repository.gw.enums.SquireEligibility.CustomAuto);
	}
	public boolean isCountryIneligibleCustomFarmingCoverage () {
		return squireEligibility.equals(SquireEligibility.CountryIneligibleCustomFarmingCoverage);
	}
}






















