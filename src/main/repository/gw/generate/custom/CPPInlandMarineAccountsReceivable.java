package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

import java.util.ArrayList;
import java.util.List;

public class CPPInlandMarineAccountsReceivable {
	
	private InlandMarineCPP.AccountsReceivableCoverageForm_CM_00_66_ClassificationOfRisk classificationOfRisk = InlandMarineCPP.AccountsReceivableCoverageForm_CM_00_66_ClassificationOfRisk.Manufacturer;
	private InlandMarineCPP.AccountsReceivableCoverageForm_CM_00_66_Coinsurance coinsurance = InlandMarineCPP.AccountsReceivableCoverageForm_CM_00_66_Coinsurance.EightlyPercent;
	
	private boolean accountsReceivable_AwayFromYourPremises = true;
	private String accountsReceivable_AwayFromYourPremises_Limit = "1000";
	
	List<CPPInlandMarineAccountsReceivable_LocationBlanket> locationBlankets = new ArrayList<CPPInlandMarineAccountsReceivable_LocationBlanket>();

	public CPPInlandMarineAccountsReceivable(List<CPPInlandMarineAccountsReceivable_LocationBlanket> locationBlankets) {
		this.locationBlankets = locationBlankets;
	}

	public InlandMarineCPP.AccountsReceivableCoverageForm_CM_00_66_ClassificationOfRisk getClassificationOfRisk() {
		return classificationOfRisk;
	}

	public void setClassificationOfRisk(InlandMarineCPP.AccountsReceivableCoverageForm_CM_00_66_ClassificationOfRisk classificationOfRisk) {
		this.classificationOfRisk = classificationOfRisk;
	}

	public InlandMarineCPP.AccountsReceivableCoverageForm_CM_00_66_Coinsurance getCoinsurance() {
		return coinsurance;
	}

	public void setCoinsurance(InlandMarineCPP.AccountsReceivableCoverageForm_CM_00_66_Coinsurance coinsurance) {
		this.coinsurance = coinsurance;
	}

	public boolean isAccountsReceivable_AwayFromYourPremises() {
		return accountsReceivable_AwayFromYourPremises;
	}

	public void setAccountsReceivable_AwayFromYourPremises(boolean accountsReceivable_AwayFromYourPremises) {
		this.accountsReceivable_AwayFromYourPremises = accountsReceivable_AwayFromYourPremises;
	}

	public String getAccountsReceivable_AwayFromYourPremises_Limit() {
		return accountsReceivable_AwayFromYourPremises_Limit;
	}

	public void setAccountsReceivable_AwayFromYourPremises_Limit(String accountsReceivable_AwayFromYourPremises_Limit) {
		this.accountsReceivable_AwayFromYourPremises_Limit = accountsReceivable_AwayFromYourPremises_Limit;
	}

	public List<CPPInlandMarineAccountsReceivable_LocationBlanket> getLocationBlankets() {
		return this.locationBlankets;
	}

	public void setLocationBlankets(List<CPPInlandMarineAccountsReceivable_LocationBlanket> locationBlanketCoverageList) {
		this.locationBlankets = locationBlanketCoverageList;
	}

}



















