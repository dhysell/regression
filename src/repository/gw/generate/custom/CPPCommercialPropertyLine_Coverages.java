package repository.gw.generate.custom;

public class CPPCommercialPropertyLine_Coverages {
	
	private boolean hasChanged = false;
	
	public CPPCommercialPropertyLine_Coverages() {
		
	}

	//PROPERTY
	private boolean clientsProperty_CR_04_01 = false;
	private int clientsProperty_CR_04_01_Limit = 0;
	private repository.gw.enums.CommercialPropertyLine.ClientsProperty_CR_04_01Deductible clientsProperty_CR_04_01_Deductible = repository.gw.enums.CommercialPropertyLine.ClientsProperty_CR_04_01Deductible.FiveHundred;
	
	private boolean comdominiumAssociationCoverageForm_CP_00_17 = false;
	private boolean condominiumCommercialUnit_OwnersCoverageForm_CP_00_18 = false;
	private boolean electronicData = false;
	private int electronicData_Limit = 0;

	
	//CRIME
	private boolean employeeTheft = false;
	private int employeeTheft_Limit = 100;
	private repository.gw.enums.CommercialPropertyLine.EmployeeTheftDeductible employeeTheft_Deductible = repository.gw.enums.CommercialPropertyLine.EmployeeTheftDeductible.FiveHundred;
	
	private boolean forgeryOrAlteration = false;
	private int forgeryOrAlteration_Limit = 100;
	
	private boolean includeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10 = false;
	private boolean insideThePremises_TheftOfMoneyAndSecurities = false;
	private int insideThePremises_TheftOfMoneyAndSecurities_Limit = 100;
	private repository.gw.enums.CommercialPropertyLine.InsideThePremises_TheftOfMoneyAndSecuritiesDeductible insideThePremises_TheftOfMoneyAndSecurities_Deductible = repository.gw.enums.CommercialPropertyLine.InsideThePremises_TheftOfMoneyAndSecuritiesDeductible.FiveHundred;
	
	private boolean insideThePremises_TheftOfOtherProperty_CR_04_05 = false;
	private int insideThePremises_TheftOfOtherProperty_CR_04_05_Limit = 100;
	private repository.gw.enums.CommercialPropertyLine.InsideThePremises_TheftOfOtherProperty_CR_04_05Deductible insideThePremises_TheftOfOtherProperty_CR_04_05_Deductible = repository.gw.enums.CommercialPropertyLine.InsideThePremises_TheftOfOtherProperty_CR_04_05Deductible.FiveHundred;
	
	private boolean jointLossPayable_CR_20_15 = false;
	private boolean outsideThePremises = false;
	private int outsideThePremises_Limit = 100;
	private repository.gw.enums.CommercialPropertyLine.OutsideThePremisesDeductible outsideThePremises_Deductible = repository.gw.enums.CommercialPropertyLine.OutsideThePremisesDeductible.FiveHundred;
	
	private boolean requiredCrimeInformation = false;
	private String requiredCrimeInformation_ClassCode = "1100(1)";
	private int requiredCrimeInformation_NumberOfEmoployees = 1;
	private int requiredCrimeInformation_NumberOfAdditionalPremises = 1;
	
	
	
	
	
	public boolean isClientsProperty_CR_04_01() {
		return clientsProperty_CR_04_01;
	}
	public void setClientsProperty_CR_04_01(boolean clientsProperty_CR_04_01) {
		this.clientsProperty_CR_04_01 = clientsProperty_CR_04_01;
	}
	public int getClientsProperty_CR_04_01_Limit() {
		return clientsProperty_CR_04_01_Limit;
	}
	public void setClientsProperty_CR_04_01_Limit(int clientsProperty_CR_04_01_Limit) {
		this.clientsProperty_CR_04_01_Limit = clientsProperty_CR_04_01_Limit;
	}
	public repository.gw.enums.CommercialPropertyLine.ClientsProperty_CR_04_01Deductible getClientsProperty_CR_04_01_Deductible() {
		return clientsProperty_CR_04_01_Deductible;
	}
	public void setClientsProperty_CR_04_01_Deductible(repository.gw.enums.CommercialPropertyLine.ClientsProperty_CR_04_01Deductible clientsProperty_CR_04_01_Deductible) {
		this.clientsProperty_CR_04_01_Deductible = clientsProperty_CR_04_01_Deductible;
	}
	public boolean isComdominiumAssociationCoverageForm_CP_00_17() {
		return comdominiumAssociationCoverageForm_CP_00_17;
	}
	public void setComdominiumAssociationCoverageForm_CP_00_17(boolean comdominiumAssociationCoverageForm_CP_00_17) {
		this.comdominiumAssociationCoverageForm_CP_00_17 = comdominiumAssociationCoverageForm_CP_00_17;
	}
	public boolean isCondominiumCommercialUnit_OwnersCoverageForm_CP_00_18() {
		return condominiumCommercialUnit_OwnersCoverageForm_CP_00_18;
	}
	public void setCondominiumCommercialUnit_OwnersCoverageForm_CP_00_18(boolean condominiumCommercialUnit_OwnersCoverageForm_CP_00_18) {
		this.condominiumCommercialUnit_OwnersCoverageForm_CP_00_18 = condominiumCommercialUnit_OwnersCoverageForm_CP_00_18;
	}
	public boolean isElectronicData() {
		return electronicData;
	}
	public void setElectronicData(boolean electronicData) {
		this.electronicData = electronicData;
	}
	public int getElectronicData_Limit() {
		return electronicData_Limit;
	}
	public void setElectronicData_Limit(int electronicData_Limit) {
		this.electronicData_Limit = electronicData_Limit;
	}
	public boolean isEmployeeTheft() {
		return employeeTheft;
	}
	public void setEmployeeTheft(boolean employeeTheft) {
		this.employeeTheft = employeeTheft;
	}
	public int getEmployeeTheft_Limit() {
		return employeeTheft_Limit;
	}
	public void setEmployeeTheft_Limit(int employeeTheft_Limit) {
		this.employeeTheft_Limit = employeeTheft_Limit;
	}
	public repository.gw.enums.CommercialPropertyLine.EmployeeTheftDeductible getEmployeeTheft_Deductible() {
		return employeeTheft_Deductible;
	}
	public void setEmployeeTheft_Deductible(repository.gw.enums.CommercialPropertyLine.EmployeeTheftDeductible employeeTheft_Deductible) {
		this.employeeTheft_Deductible = employeeTheft_Deductible;
	}
	public boolean isForgeryOrAlteration() {
		return forgeryOrAlteration;
	}
	public void setForgeryOrAlteration(boolean forgeryOrAlteration) {
		this.forgeryOrAlteration = forgeryOrAlteration;
	}
	public int getForgeryOrAlteration_Limit() {
		return forgeryOrAlteration_Limit;
	}
	public void setForgeryOrAlteration_Limit(int forgeryOrAlteration_Limit) {
		this.forgeryOrAlteration_Limit = forgeryOrAlteration_Limit;
	}
	public boolean isIncludeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10() {
		return includeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10;
	}
	public void setIncludeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10(boolean includeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10) {
		this.includeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10 = includeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10;
	}
	public boolean isInsideThePremises_TheftOfMoneyAndSecurities() {
		return insideThePremises_TheftOfMoneyAndSecurities;
	}
	public void setInsideThePremises_TheftOfMoneyAndSecurities(boolean insideThePremises_TheftOfMoneyAndSecurities) {
		this.insideThePremises_TheftOfMoneyAndSecurities = insideThePremises_TheftOfMoneyAndSecurities;
	}
	public int getInsideThePremises_TheftOfMoneyAndSecurities_Limit() {
		return insideThePremises_TheftOfMoneyAndSecurities_Limit;
	}
	public void setInsideThePremises_TheftOfMoneyAndSecurities_Limit(int insideThePremises_TheftOfMoneyAndSecurities_Limit) {
		this.insideThePremises_TheftOfMoneyAndSecurities_Limit = insideThePremises_TheftOfMoneyAndSecurities_Limit;
	}
	public repository.gw.enums.CommercialPropertyLine.InsideThePremises_TheftOfMoneyAndSecuritiesDeductible getInsideThePremises_TheftOfMoneyAndSecurities_Deductible() {
		return insideThePremises_TheftOfMoneyAndSecurities_Deductible;
	}
	public void setInsideThePremises_TheftOfMoneyAndSecurities_Deductible(repository.gw.enums.CommercialPropertyLine.InsideThePremises_TheftOfMoneyAndSecuritiesDeductible insideThePremises_TheftOfMoneyAndSecurities_Deductible) {
		this.insideThePremises_TheftOfMoneyAndSecurities_Deductible = insideThePremises_TheftOfMoneyAndSecurities_Deductible;
	}
	public boolean isInsideThePremises_TheftOfOtherProperty_CR_04_05() {
		return insideThePremises_TheftOfOtherProperty_CR_04_05;
	}
	public void setInsideThePremises_TheftOfOtherProperty_CR_04_05(boolean insideThePremises_TheftOfOtherProperty_CR_04_05) {
		this.insideThePremises_TheftOfOtherProperty_CR_04_05 = insideThePremises_TheftOfOtherProperty_CR_04_05;
	}
	public int getInsideThePremises_TheftOfOtherProperty_CR_04_05_Limit() {
		return insideThePremises_TheftOfOtherProperty_CR_04_05_Limit;
	}
	public void setInsideThePremises_TheftOfOtherProperty_CR_04_05_Limit(int insideThePremises_TheftOfOtherProperty_CR_04_05_Limit) {
		this.insideThePremises_TheftOfOtherProperty_CR_04_05_Limit = insideThePremises_TheftOfOtherProperty_CR_04_05_Limit;
	}
	public repository.gw.enums.CommercialPropertyLine.InsideThePremises_TheftOfOtherProperty_CR_04_05Deductible getInsideThePremises_TheftOfOtherProperty_CR_04_05_Deductible() {
		return insideThePremises_TheftOfOtherProperty_CR_04_05_Deductible;
	}
	public void setInsideThePremises_TheftOfOtherProperty_CR_04_05_Deductible(repository.gw.enums.CommercialPropertyLine.InsideThePremises_TheftOfOtherProperty_CR_04_05Deductible insideThePremises_TheftOfOtherProperty_CR_04_05_Deductible) {
		this.insideThePremises_TheftOfOtherProperty_CR_04_05_Deductible = insideThePremises_TheftOfOtherProperty_CR_04_05_Deductible;
	}
	public boolean isJointLossPayable_CR_20_15() {
		return jointLossPayable_CR_20_15;
	}
	public void setJointLossPayable_CR_20_15(boolean jointLossPayable_CR_20_15) {
		this.jointLossPayable_CR_20_15 = jointLossPayable_CR_20_15;
	}
	public boolean isOutsideThePremises() {
		return outsideThePremises;
	}
	public void setOutsideThePremises(boolean outsideThePremises) {
		this.outsideThePremises = outsideThePremises;
	}
	public int getOutsideThePremises_Limit() {
		return outsideThePremises_Limit;
	}
	public void setOutsideThePremises_Limit(int outsideThePremises_Limit) {
		this.outsideThePremises_Limit = outsideThePremises_Limit;
	}
	public repository.gw.enums.CommercialPropertyLine.OutsideThePremisesDeductible getOutsideThePremises_Deductible() {
		return outsideThePremises_Deductible;
	}
	public void setOutsideThePremises_Deductible(repository.gw.enums.CommercialPropertyLine.OutsideThePremisesDeductible outsideThePremises_Deductible) {
		this.outsideThePremises_Deductible = outsideThePremises_Deductible;
	}
	public boolean isRequiredCrimeInformation() {
		return requiredCrimeInformation;
	}
	public void setRequiredCrimeInformation(boolean requiredCrimeInformation) {
		this.requiredCrimeInformation = requiredCrimeInformation;
	}
	public String getRequiredCrimeInformation_ClassCode() {
		return requiredCrimeInformation_ClassCode;
	}
	public void setRequiredCrimeInformation_ClassCode(String requiredCrimeInformation_ClassCode) {
		this.requiredCrimeInformation_ClassCode = requiredCrimeInformation_ClassCode;
	}
	public int getRequiredCrimeInformation_NumberOfEmoployees() {
		return requiredCrimeInformation_NumberOfEmoployees;
	}
	public void setRequiredCrimeInformation_NumberOfEmoployees(int requiredCrimeInformation_NumberOfEmoployees) {
		this.requiredCrimeInformation_NumberOfEmoployees = requiredCrimeInformation_NumberOfEmoployees;
	}
	public int getRequiredCrimeInformation_NumberOfAdditionalPremises() {
		return requiredCrimeInformation_NumberOfAdditionalPremises;
	}
	public void setRequiredCrimeInformation_NumberOfAdditionalPremises(int requiredCrimeInformation_NumberOfAdditionalPremises) {
		this.requiredCrimeInformation_NumberOfAdditionalPremises = requiredCrimeInformation_NumberOfAdditionalPremises;
	}
	
	public boolean hasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}
	
	
	
	
	
	
	
	
}














