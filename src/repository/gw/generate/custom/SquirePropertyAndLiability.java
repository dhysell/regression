package repository.gw.generate.custom;

import repository.gw.enums.Property;

import java.util.ArrayList;

public class SquirePropertyAndLiability {

	public Property.SectionIDeductible section1Deductible = Property.SectionIDeductible.FiveHundred;
    public SquirePropertyLiabilityExclusionsConditions propLiabExclusionsConditions = new SquirePropertyLiabilityExclusionsConditions();
    public SquireLiability liabilitySection = new SquireLiability();
    public SquireFPP squireFPP = new SquireFPP();

	//LOCATIONS
	public ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>();

	//CLUE PROPERTY
    public CLUEPropertyInfo cluePropertyReport = new CLUEPropertyInfo();
	//LINE REVIEW








	public SquirePropertyAndLiability() {
		locationList.add(new PolicyLocation());
	}

}
