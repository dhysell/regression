package repository.gw.generate.custom;

import repository.gw.enums.CPUWIssues;

import java.util.ArrayList;

public class CPPCommercialPropertyLine {
	
	private boolean hasChanged = false;
	
	//COVERAGES
	repository.gw.generate.custom.CPPCommercialPropertyLine_Coverages propertyLineCoverages = new repository.gw.generate.custom.CPPCommercialPropertyLine_Coverages();
	
	
	//EXCLUSIONS AND CONDITIONS
	repository.gw.generate.custom.CPPCommercialPropertyLine_ExclusionsConditions propertyLineExclusionsConditions = new repository.gw.generate.custom.CPPCommercialPropertyLine_ExclusionsConditions();
	
	
	private boolean areAllReferencesChecked = true;
	private String howOftenAreAuditsConducted = "Usually only when we are about to be investigated for fraud!";
	private String whoPerformsTheseAudits = "CPA";
	private String whoPerformsTheseAudits_PleaseDescribeOther = "Used to refer to a person or thing that is different or distinct from one already mentioned or known about.";
	private boolean differentEmployeesWriteChecks = true;
	private boolean doYouDepositDaily = true;
	private String explainHowOftenDepositsAreDone = "when the safe can hold nothing more.";
	private boolean proceduresInPlaceForLargeChecks = true;
	private boolean applicantAFraternalOrgonizationOrLaborUnion = false;
	
	private ArrayList<repository.gw.enums.CPUWIssues> underwritingIssuesList = new ArrayList<repository.gw.enums.CPUWIssues>();
	
	
	public CPPCommercialPropertyLine() {
		
	}

	public repository.gw.generate.custom.CPPCommercialPropertyLine_Coverages getPropertyLineCoverages() {
		return propertyLineCoverages;
	}

	public void setPropertyLineCoverages(CPPCommercialPropertyLine_Coverages propertyLineCoverages) {
		this.propertyLineCoverages = propertyLineCoverages;
	}

	public repository.gw.generate.custom.CPPCommercialPropertyLine_ExclusionsConditions getPropertyLineExclusionsConditions() {
		return propertyLineExclusionsConditions;
	}

	public void setPropertyLineExclusionsConditions(CPPCommercialPropertyLine_ExclusionsConditions propertyLineExclusionsConditions) {
		this.propertyLineExclusionsConditions = propertyLineExclusionsConditions;
	}
	
	public boolean hasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}
	
	public void resetChangeCondition() {
		setHasChanged(false);
		propertyLineCoverages.setHasChanged(false);
		propertyLineExclusionsConditions.setHasChanged(false);
	}

	public String getHowOftenAreAuditsConducted() {
		return howOftenAreAuditsConducted;
	}

	public void setHowOftenAreAuditsConducted(String howOftenAreAuditsConducted) {
		this.howOftenAreAuditsConducted = howOftenAreAuditsConducted;
	}

	public String getWhoPerformsTheseAudits() {
		return whoPerformsTheseAudits;
	}

	public void setWhoPerformsTheseAudits(String whoPerformsTheseAudits) {
		this.whoPerformsTheseAudits = whoPerformsTheseAudits;
	}

	public String getWhoPerformsTheseAudits_PleaseDescribeOther() {
		return whoPerformsTheseAudits_PleaseDescribeOther;
	}

	public void setWhoPerformsTheseAudits_PleaseDescribeOther(String whoPerformsTheseAudits_PleaseDescribeOther) {
		this.whoPerformsTheseAudits_PleaseDescribeOther = whoPerformsTheseAudits_PleaseDescribeOther;
	}

	public boolean isDifferentEmployeesWriteChecks() {
		return differentEmployeesWriteChecks;
	}

	public void setDifferentEmployeesWriteChecks(boolean differentEmployeesWriteChecks) {
		this.differentEmployeesWriteChecks = differentEmployeesWriteChecks;
	}

	public boolean isDoYouDepositDaily() {
		return doYouDepositDaily;
	}

	public void setDoYouDepositDaily(boolean doYouDepositDaily) {
		this.doYouDepositDaily = doYouDepositDaily;
	}

	public String getExplainHowOftenDepositsAreDone() {
		return explainHowOftenDepositsAreDone;
	}

	public void setExplainHowOftenDepositsAreDone(String explainHowOftenDepositsAreDone) {
		this.explainHowOftenDepositsAreDone = explainHowOftenDepositsAreDone;
	}

	public boolean isProceduresInPlaceForLargeChecks() {
		return proceduresInPlaceForLargeChecks;
	}

	public void setProceduresInPlaceForLargeChecks(boolean proceduresInPlaceForLargeChecks) {
		this.proceduresInPlaceForLargeChecks = proceduresInPlaceForLargeChecks;
	}

	public boolean isApplicantAFraternalOrgonizationOrLaborUnion() {
		return applicantAFraternalOrgonizationOrLaborUnion;
	}

	public void setApplicantAFraternalOrgonizationOrLaborUnion(boolean applicantAFraternalOrgonizationOrLaborUnion) {
		this.applicantAFraternalOrgonizationOrLaborUnion = applicantAFraternalOrgonizationOrLaborUnion;
	}

	public boolean isHasChanged() {
		return hasChanged;
	}

	public boolean isAreAllReferencesChecked() {
		return areAllReferencesChecked;
	}

	public void setAreAllReferencesChecked(boolean areAllReferencesChecked) {
		this.areAllReferencesChecked = areAllReferencesChecked;
	}

	public ArrayList<repository.gw.enums.CPUWIssues> getUnderwritingIssuesList() {
		return underwritingIssuesList;
	}

	public void setUnderwritingIssuesList(ArrayList<CPUWIssues> underwritingIssuesList) {
		this.underwritingIssuesList = underwritingIssuesList;
	}
	
	
}
