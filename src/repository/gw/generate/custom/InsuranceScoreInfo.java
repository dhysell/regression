package repository.gw.generate.custom;

import services.broker.objects.lexisnexis.cbr.response.actual.NcfReport;

import java.util.ArrayList;

public class InsuranceScoreInfo {
	
	private NcfReport brokerReport;
	private double uiInsuranceScore;
	private double uiEnhancedInsuranceScore;
	private String uiFlag;
	private ArrayList<String> uiReasonCodes;
	private ArrayList<UICreditReportBankruptcies> uiBankruptcies;
	private ArrayList<UICreditReportCollectionItems> uiCollectionItems;
	
	private boolean livedAtAddressLessThan6Months = false;
	private AddressInfo formerAddress = new AddressInfo(true);
	
	private boolean nameChangelast6Months = false;
	private String formerFirstName = "BOB";
	private String formerMiddleName = "FERDIDNAN";
	public boolean isLivedAtAddressLessThan6Months() {
		return livedAtAddressLessThan6Months;
	}

	public void setLivedAtAddressLessThan6Months(boolean livedAtAddressLessThan6Months) {
		this.livedAtAddressLessThan6Months = livedAtAddressLessThan6Months;
	}

	public AddressInfo getFormerAddress() {
		return formerAddress;
	}

	public void setFormerAddress(AddressInfo formerAddress) {
		this.formerAddress = formerAddress;
	}

	public boolean isNameChangelast6Months() {
		return nameChangelast6Months;
	}

	public void setNameChangelast6Months(boolean nameChangelast6Months) {
		this.nameChangelast6Months = nameChangelast6Months;
	}

	public String getFormerFirstName() {
		return formerFirstName;
	}

	public void setFormerFirstName(String formerFirstName) {
		this.formerFirstName = formerFirstName;
	}

	public String getFormerMiddleName() {
		return formerMiddleName;
	}

	public void setFormerMiddleName(String formerMiddleName) {
		this.formerMiddleName = formerMiddleName;
	}

	public String getFormerLastName() {
		return fromerLastName;
	}

	public void setFromerLastName(String fromerLastName) {
		this.fromerLastName = fromerLastName;
	}

	private String fromerLastName = "EVANSMITH";

	public NcfReport getBrokerReport() {
		return brokerReport;
	}

	public void setBrokerReport(NcfReport brokerReport) {
		this.brokerReport = brokerReport;
	}

	public double getUiInsuranceScore() {
		return uiInsuranceScore;
	}

	public void setUiInsuranceScore(double uiInsuranceScore) {
		this.uiInsuranceScore = uiInsuranceScore;
	}

	public double getUiEnhancedInsuranceScore() {
		return uiEnhancedInsuranceScore;
	}

	public void setUiEnhancedInsuranceScore(double uiEnhancedInsuranceScore) {
		this.uiEnhancedInsuranceScore = uiEnhancedInsuranceScore;
	}
	
	public String getUiFlag() {
		return uiFlag;
	}

	public void setUiFlag(String uiFlag) {
		this.uiFlag = uiFlag;
	}

	public ArrayList<String> getUiReasonCodes() {
		return uiReasonCodes;
	}

	public void setUiReasonCodes(ArrayList<String> uiReasonCodes) {
		this.uiReasonCodes = uiReasonCodes;
	}

	public ArrayList<UICreditReportBankruptcies> getUiBankruptcies() {
		return uiBankruptcies;
	}

	public void setUiBankruptcies(ArrayList<UICreditReportBankruptcies> uiBankruptcies) {
		this.uiBankruptcies = uiBankruptcies;
	}

	public ArrayList<UICreditReportCollectionItems> getUiCollectionItems() {
		return uiCollectionItems;
	}

	public void setUiCollectionItems(ArrayList<UICreditReportCollectionItems> uiCollectionItems) {
		this.uiCollectionItems = uiCollectionItems;
	}

}
