package repository.gw.generate.custom;

import repository.gw.enums.*;

import java.util.ArrayList;

public class PersonalLines {
	
	public repository.gw.enums.PackageRiskType packageRisk = PackageRiskType.Office;
	
	
	public Squire squire = null;
	
	
	
	
	public repository.gw.enums.SquireEligibility squireEligibility = SquireEligibility.City;
	public int summedNumAcres = 0;
	public boolean alwaysOrderCreditReport = true;
	
	//SECTION I & II - PROPERTY & LIABILITY
	public boolean overridePropertyOwnerValidation = false;
	public Property.SectionIDeductible section1Deductible = Property.SectionIDeductible.FiveHundred;
	public SquirePropertyLiabilityExclusionsConditions propLiabExclusionsConditions;
	public SquireLiability liabilitySection;
	public SquireFPP squireFPP;
	
	public ArrayList<repository.gw.enums.LineSelection> lineSelection = new ArrayList<LineSelection>();

	public ArrayList<InlandMarineTypes.InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarineTypes.InlandMarine>();
	public ArrayList<InlandMarineTypes.InlandMarine> inlandMarineCoverageSelection_Standard_IM = new ArrayList<InlandMarineTypes.InlandMarine>();
	
	
	
	
	
	
	
	
	
	

}












