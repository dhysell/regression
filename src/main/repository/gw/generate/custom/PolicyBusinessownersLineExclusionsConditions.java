package repository.gw.generate.custom;

import java.util.ArrayList;

public class PolicyBusinessownersLineExclusionsConditions {

	private boolean comprehensiveBusinessLiabilityExclusion = false;
    private ArrayList<String> compBusLiabExcTwoDescriptions = new ArrayList<String>();
	private boolean damageToWorkPerformedBySubcontractorsOnYourBehalfExclusion = false;
	private boolean limitationOfCoverageToDesignatedPremisesOrProjectExclusion = false;
    private ArrayList<String> limitCovDesigPremExcPremisesAndProject = new ArrayList<String>();
	private boolean liabilityManuscriptEndorsement = false;
    private ArrayList<String> liabilityManuscriptEndorsementDescription = new ArrayList<String>();
	private boolean propertyManuscriptEndorsement = false;
    private ArrayList<String> propertyManuscriptEndorsementDescription = new ArrayList<String>();

	public PolicyBusinessownersLineExclusionsConditions() {

	}

	public boolean isComprehensiveBusinessLiabilityExclusion() {
		return comprehensiveBusinessLiabilityExclusion;
	}

	public void setComprehensiveBusinessLiabilityExclusion(boolean comprehensiveBusinessLiabilityExclusion) {
		this.comprehensiveBusinessLiabilityExclusion = comprehensiveBusinessLiabilityExclusion;
	}

    public ArrayList<String> getCompBusLiabExcTwoDescriptions() {
		return compBusLiabExcTwoDescriptions;
	}

    public void setCompBusLiabExcTwoDescriptions(ArrayList<String> compBusLiabExcTwoDescriptions) {
		this.compBusLiabExcTwoDescriptions = compBusLiabExcTwoDescriptions;
	}

	public boolean isDamageToWorkPerformedBySubcontractorsOnYourBehalfExclusion() {
		return damageToWorkPerformedBySubcontractorsOnYourBehalfExclusion;
	}

	public void setDamageToWorkPerformedBySubcontractorsOnYourBehalfExclusion(
			boolean damageToWorkPerformedBySubcontractorsOnYourBehalfExclusion) {
		this.damageToWorkPerformedBySubcontractorsOnYourBehalfExclusion = damageToWorkPerformedBySubcontractorsOnYourBehalfExclusion;
	}

	public boolean isLimitationOfCoverageToDesignatedPremisesOrProjectExclusion() {
		return limitationOfCoverageToDesignatedPremisesOrProjectExclusion;
	}

	public void setLimitationOfCoverageToDesignatedPremisesOrProjectExclusion(
			boolean limitationOfCoverageToDesignatedPremisesOrProjectExclusion) {
		this.limitationOfCoverageToDesignatedPremisesOrProjectExclusion = limitationOfCoverageToDesignatedPremisesOrProjectExclusion;
	}

    public ArrayList<String> getLimitCovDesigPremExcPremisesAndProject() {
		return limitCovDesigPremExcPremisesAndProject;
	}

    public void setLimitCovDesigPremExcPremisesAndProject(ArrayList<String> limitCovDesigPremExcPremisesAndProject) {
		this.limitCovDesigPremExcPremisesAndProject = limitCovDesigPremExcPremisesAndProject;
	}

	public boolean isLiabilityManuscriptEndorsement() {
		return liabilityManuscriptEndorsement;
	}

	public void setLiabilityManuscriptEndorsement(boolean liabilityManuscriptEndorsement) {
		this.liabilityManuscriptEndorsement = liabilityManuscriptEndorsement;
	}

    public ArrayList<String> getLiabilityManuscriptEndorsementDescription() {
		return liabilityManuscriptEndorsementDescription;
	}

	public void setLiabilityManuscriptEndorsementDescription(
            ArrayList<String> liabilityManuscriptEndorsementDescription) {
		this.liabilityManuscriptEndorsementDescription = liabilityManuscriptEndorsementDescription;
	}

	public boolean isPropertyManuscriptEndorsement() {
		return propertyManuscriptEndorsement;
	}

	public void setPropertyManuscriptEndorsement(boolean propertyManuscriptEndorsement) {
		this.propertyManuscriptEndorsement = propertyManuscriptEndorsement;
	}

    public ArrayList<String> getPropertyManuscriptEndorsementDescription() {
		return propertyManuscriptEndorsementDescription;
	}

	public void setPropertyManuscriptEndorsementDescription(
            ArrayList<String> propertyManuscriptEndorsementDescription) {
		this.propertyManuscriptEndorsementDescription = propertyManuscriptEndorsementDescription;
	}

}
