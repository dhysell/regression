package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

import java.util.ArrayList;
import java.util.List;

public class CPPInlandMarineBaileesCustomers {
	
	private InlandMarineCPP.BaileesCustomersCoverageForm_IH_00_85_Deductible deductible = InlandMarineCPP.BaileesCustomersCoverageForm_IH_00_85_Deductible.TwoHundredFifty;
	
	private boolean propertyAtYourPremises = false;
	private List<BaileesCustomer> propertyAtYourPremisesList = new ArrayList<BaileesCustomer>();
	
	private boolean propertyInStorageAtYourPremises = false;
	private List<BaileesCustomer> propertyInStorageAtYourPremisesList = new ArrayList<BaileesCustomer>();
	
	private boolean propertyAwayFromYourPremises = false;
	private List<BaileesCustomer> propertyAwayFromYourPremisesList = new ArrayList<BaileesCustomer>();
	
	private boolean waterExclusion_IH_99_18 = false;

	public CPPInlandMarineBaileesCustomers(List<BaileesCustomer> propertyAtYourPremisesList, List<BaileesCustomer> propertyInStorageAtYourPremisesList,List<BaileesCustomer> propertyAwayFromYourPremisesList) {
		this.propertyAtYourPremisesList = propertyAtYourPremisesList;
		this.propertyInStorageAtYourPremisesList = propertyInStorageAtYourPremisesList;
		this.propertyAwayFromYourPremisesList = propertyAwayFromYourPremisesList;
	}

	public InlandMarineCPP.BaileesCustomersCoverageForm_IH_00_85_Deductible getDeductible() {
		return deductible;
	}

	public void setDeductible(InlandMarineCPP.BaileesCustomersCoverageForm_IH_00_85_Deductible deductible) {
		this.deductible = deductible;
	}

	public boolean isPropertyAtYourPremises() {
		return propertyAtYourPremises;
	}

	public void setPropertyAtYourPremises(boolean propertyAtYourPremises) {
		this.propertyAtYourPremises = propertyAtYourPremises;
	}

	public List<BaileesCustomer> getPropertyAtYourPremisesList() {
		return propertyAtYourPremisesList;
	}

	public void setPropertyAtYourPremisesList(List<BaileesCustomer> propertyAtYourPremisesList) {
		this.propertyAtYourPremisesList = propertyAtYourPremisesList;
	}

	public boolean isPropertyInStorageAtYourPremises() {
		return propertyInStorageAtYourPremises;
	}

	public void setPropertyInStorageAtYourPremises(boolean propertyInStorageAtYourPremises) {
		this.propertyInStorageAtYourPremises = propertyInStorageAtYourPremises;
	}

	public List<BaileesCustomer> getPropertyInStorageAtYourPremisesList() {
		return propertyInStorageAtYourPremisesList;
	}

	public void setPropertyInStorageAtYourPremisesList(List<BaileesCustomer> propertyInStorageAtYourPremisesList) {
		this.propertyInStorageAtYourPremisesList = propertyInStorageAtYourPremisesList;
	}

	public boolean isPropertyAwayFromYourPremises() {
		return propertyAwayFromYourPremises;
	}

	public void setPropertyAwayFromYourPremises(boolean propertyAwayFromYourPremises) {
		this.propertyAwayFromYourPremises = propertyAwayFromYourPremises;
	}

	public List<BaileesCustomer> getPropertyAwayFromYourPremisesList() {
		return propertyAwayFromYourPremisesList;
	}

	public void setPropertyAwayFromYourPremisesList(List<BaileesCustomer> propertyAwayFromYourPremisesList) {
		this.propertyAwayFromYourPremisesList = propertyAwayFromYourPremisesList;
	}

	public boolean isWaterExclusion_IH_99_18() {
		return waterExclusion_IH_99_18;
	}

	public void setWaterExclusion_IH_99_18(boolean waterExclusion_IH_99_18) {
		this.waterExclusion_IH_99_18 = waterExclusion_IH_99_18;
	}

	
	
	
	
}












