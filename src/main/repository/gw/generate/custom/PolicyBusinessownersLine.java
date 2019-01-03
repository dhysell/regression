package repository.gw.generate.custom;


import org.testng.Assert;
import repository.gw.enums.BusinessownersLine;
import repository.gw.exception.GuidewireException;

import java.util.ArrayList;

public class PolicyBusinessownersLine extends PolicyCommon {

	private BusinessownersLine.SmallBusinessType smallBusinessType = BusinessownersLine.SmallBusinessType.StoresRetail;
	private BusinessownersLine.PropertyGlassDeductible propertyDeductible = BusinessownersLine.PropertyGlassDeductible.HalfK;
	private BusinessownersLine.LiabilityLimits liabilityLimits = BusinessownersLine.LiabilityLimits.Five00_1000_1000;
	private boolean pdDeductibleChecked = false;
	private BusinessownersLine.PDDeductibleAmount pdDeductible = BusinessownersLine.PDDeductibleAmount.PD500;
	private float damagePremisesAmount = 50000;
	private BusinessownersLine.PremisesMedicalExpense medicalLimit = BusinessownersLine.PremisesMedicalExpense.Exp5000;
	private ArrayList<repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured> additonalInsuredBOLineList = new ArrayList<repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured>();
    private repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages additionalCoverageStuff = new repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages(false, true);
	private repository.gw.generate.custom.PolicyBusinessownersLineExclusionsConditions exclusionsConditionsStuff = new repository.gw.generate.custom.PolicyBusinessownersLineExclusionsConditions();
	
	public ArrayList<repository.gw.generate.custom.PolicyLocation> locationList = new ArrayList<PolicyLocation>();

	public PolicyBusinessownersLine() {

	}

	public PolicyBusinessownersLine(BusinessownersLine.SmallBusinessType smallBusinessType) throws GuidewireException {
		setSmallBusinessType(smallBusinessType);
	}

	public PolicyBusinessownersLine(BusinessownersLine.SmallBusinessType smallBusinessType,
                                    ArrayList<repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured> additonalInsuredBOLineList) throws GuidewireException {
		setSmallBusinessType(smallBusinessType);
		setAdditonalInsuredBOLineList(additonalInsuredBOLineList);
	}

	public BusinessownersLine.SmallBusinessType getSmallBusinessType() {
		return smallBusinessType;
	}

	public void setSmallBusinessType(BusinessownersLine.SmallBusinessType smallBusinessType) {
		if (smallBusinessType == null) {
			Assert.fail("smallBusinessType Cannot be Null");
		} else {
			this.smallBusinessType = smallBusinessType;
		}
	}

	public BusinessownersLine.PropertyGlassDeductible getPropertyDeductible() {
		return propertyDeductible;
	}

	public void setPropertyDeductible(BusinessownersLine.PropertyGlassDeductible propertyDeductible) {
		this.propertyDeductible = propertyDeductible;
	}

	public BusinessownersLine.LiabilityLimits getLiabilityLimits() {
		return liabilityLimits;
	}

	public void setLiabilityLimits(BusinessownersLine.LiabilityLimits liabilityLimits) {
		this.liabilityLimits = liabilityLimits;
	}

	public boolean isPdDeductibleChecked() {
		return pdDeductibleChecked;
	}

	public void setPdDeductibleChecked(boolean pdDeductibleChecked) {
		this.pdDeductibleChecked = pdDeductibleChecked;
	}

	public BusinessownersLine.PDDeductibleAmount getPdDeductible() {
		return pdDeductible;
	}

	public void setPdDeductible(BusinessownersLine.PDDeductibleAmount pdDeductible) {
		this.pdDeductible = pdDeductible;
	}

	public float getDamagePremisesAmount() {
		return damagePremisesAmount;
	}

	public void setDamagePremisesAmount(float damagePremisesAmount) {
		if (damagePremisesAmount <= 50000) {
			Assert.fail("damagePremisesAmount must be greater than or equal to 50000");
		}
		this.damagePremisesAmount = damagePremisesAmount;
	}

	public BusinessownersLine.PremisesMedicalExpense getMedicalLimit() {
		return medicalLimit;
	}

	public void setMedicalLimit(BusinessownersLine.PremisesMedicalExpense medicalLimit) {
		this.medicalLimit = medicalLimit;
	}

	public ArrayList<repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured> getAdditonalInsuredBOLineList() {
		return additonalInsuredBOLineList;
	}

	public void setAdditonalInsuredBOLineList(
			ArrayList<PolicyBusinessownersLineAdditionalInsured> additonalInsuredBOLineList) {
		this.additonalInsuredBOLineList = additonalInsuredBOLineList;
	}

	public repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages getAdditionalCoverageStuff() {
		return additionalCoverageStuff;
	}

	public void setAdditionalCoverageStuff(PolicyBusinessownersLineAdditionalCoverages additionalCoverageStuff) {
		this.additionalCoverageStuff = additionalCoverageStuff;
	}

	public repository.gw.generate.custom.PolicyBusinessownersLineExclusionsConditions getExclusionsConditionsStuff() {
		return exclusionsConditionsStuff;
	}

	public void setExclusionsConditionsStuff(PolicyBusinessownersLineExclusionsConditions exclusionsConditionsStuff) {
		this.exclusionsConditionsStuff = exclusionsConditionsStuff;
	}

//	public boolean isOverrideEffExpDates() {
//		return overrideEffExpDates;
//	}
//
//	public void setOverrideEffExpDates(boolean overrideEffExpDates) {
//		this.overrideEffExpDates = overrideEffExpDates;
//	}
//
//	public PolicyPremium getPolicyPremium() {
//		return policyPremium;
//	}
//
//	public void setPolicyPremium(PolicyPremium policyPremium) {
//		this.policyPremium = policyPremium;
//	}

}
