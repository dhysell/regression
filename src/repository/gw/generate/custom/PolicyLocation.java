package repository.gw.generate.custom;

import com.idfbins.enums.CountyIdaho;
import org.testng.Assert;
import persistence.globaldatarepo.entities.TownshipRange;
import persistence.globaldatarepo.helpers.TownshipRangeHelper;
import repository.gw.enums.Location;
import repository.gw.enums.PLLocationType;
import repository.gw.enums.SectionI_UnderwriterIssues;
import repository.gw.exception.GuidewireException;

import java.util.ArrayList;

public class PolicyLocation {

	private int number = -1;
	private repository.gw.enums.PLLocationType plLocationType = repository.gw.enums.PLLocationType.Address;
	private AddressInfo address = new AddressInfo(true);
	private boolean standardize = false;
	private Location.ProtectionClassCode manualProtectionClassCode = Location.ProtectionClassCode.Prot3;
	private Location.AutoIncreaseBlgLimitPercentage autoIncrease = Location.AutoIncreaseBlgLimitPercentage.AutoInc2Perc;
	private boolean playgroundYes = false;
	private boolean poolYes = false;
	private boolean seasonalYes = false;
	private double annualGrossReceipts = 12345;
	private ArrayList<PolicyLocationAdditionalInsured> additionalInsuredLocationsList = new ArrayList<PolicyLocationAdditionalInsured>();
	private PolicyLocationAdditionalCoverages additionalCoveragesStuff = new PolicyLocationAdditionalCoverages();
	private ArrayList<PolicyLocationBuilding> buildingList = new ArrayList<PolicyLocationBuilding>();
	private int plNumAcres = 1;
	private int plNumResidence = 1;
	private boolean addressIsStandardized = false;
	private TownshipRange township = null;
	private Location.LocationType locationType = Location.LocationType.Address;
	
	//jlarsen 3/12/2016 R2 CPP
	private boolean nonSpecificLocation = false;
	private String loanOrContractNumber = "LN12345";
	
	private repository.gw.enums.SectionI_UnderwriterIssues sectionIUWIssue = null;
	
	
	//R2 PL
	//PROPERTY DETAIL
	private ArrayList<PLPolicyLocationProperty> propertyList = new ArrayList<PLPolicyLocationProperty>();
	
	

	public PolicyLocation() {
		propertyList.add(new PLPolicyLocationProperty());
	}
	
	public PolicyLocation(AddressInfo address) {
		this.address = address;
	}
	
	public PolicyLocation(CountyIdaho county) throws Exception {
		setTownshipRange(county);
		setLocationType(Location.LocationType.SectionTownshipRange);
	}

	public PolicyLocation(AddressInfo address, ArrayList<PolicyLocationBuilding> buildingList) throws GuidewireException {
		setAddress(address);
		setBuildingList(buildingList);
	}

    public PolicyLocation(ArrayList<PLPolicyLocationProperty> buildingListPL, AddressInfo address) {
		setAddress(address);
		this.setPropertyList(buildingListPL);
//		setBuildingListPL(buildingListPL);
	}

	public PolicyLocation(AddressInfo address, boolean standardize) {
		setAddress(address);
		setStandardize(standardize);
	}

	public PolicyLocation(AddressInfo address,
			ArrayList<PolicyLocationAdditionalInsured> additionalInsuredLocationsList,
			ArrayList<PolicyLocationBuilding> plBuildingList) throws GuidewireException {
		setAddress(address);
		setAdditionalInsuredLocationsList(additionalInsuredLocationsList);
		setBuildingList(plBuildingList);
	}

	public PolicyLocation(AddressInfo address, boolean standardize,
			ArrayList<PolicyLocationAdditionalInsured> additionalInsuredLocationsList,
			ArrayList<PolicyLocationBuilding> plBuildingList) throws GuidewireException {
		setAddress(address);
		setStandardize(standardize);
		setAdditionalInsuredLocationsList(additionalInsuredLocationsList);
		setBuildingList(plBuildingList);
	}

	// **************************************************************************************************************************
	public PolicyLocation(ArrayList<PolicyLocationAdditionalInsured> additionalInsuredLocationsList,
                          ArrayList<PLPolicyLocationProperty> buildingListPL, AddressInfo address) {
		setAddress(address);
		setAdditionalInsuredLocationsList(additionalInsuredLocationsList);
		this.setPropertyList(buildingListPL);
//		setBuildingListPL(buildingListPL);
	}

	public PolicyLocation(boolean standardize,
                          ArrayList<PolicyLocationAdditionalInsured> additionalInsuredLocationsList,
                          ArrayList<PLPolicyLocationProperty> buildingListPL, AddressInfo address) {
		setAddress(address);
		setStandardize(standardize);
		setAdditionalInsuredLocationsList(additionalInsuredLocationsList);
		this.setPropertyList(buildingListPL);
//		setBuildingListPL(buildingListPL);
	}

    public PolicyLocation(ArrayList<PLPolicyLocationProperty> buildingListPL) {
		this.setPropertyList(buildingListPL);
//		setBuildingListPL(buildingListPL);
	}
	// **************************************************************************************************************************

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public repository.gw.enums.PLLocationType getPlLocationType() {
		return plLocationType;
	}

	public void setPlLocationType(PLLocationType plLocationType) {
		this.plLocationType = plLocationType;
	}

	public AddressInfo getAddress() {
		return address;
	}

	public void setAddress(AddressInfo address) {
		this.address = address;
	}
	
	public String getFullAddressString() {
		String fullAddress = null;
		if (this.address.getLine2() != null && !this.address.getLine2().equals("")) {
			fullAddress = this.address.getLine1() + ", " + this.address.getLine2() + ", " + this.address.getCity() + ", " + this.address.getState().getAbbreviation() + " " + this.getAddress().getZip();
		} else {
			fullAddress = this.address.getLine1() + ", " + this.address.getCity() + ", " + this.address.getState().getAbbreviation() + " " + this.getAddress().getZip();
		}
		return fullAddress;
	}

	public boolean isStandardize() {
		return standardize;
	}

	public void setStandardize(boolean standardize) {
		this.standardize = standardize;
	}

	public Location.ProtectionClassCode getManualProtectionClassCode() {
		return manualProtectionClassCode;
	}

	public void setManualProtectionClassCode(Location.ProtectionClassCode manualProtectionClassCode) {
		this.manualProtectionClassCode = manualProtectionClassCode;
	}

	public Location.AutoIncreaseBlgLimitPercentage getAutoIncrease() {
		return autoIncrease;
	}

	public void setAutoautoIncrease(Location.AutoIncreaseBlgLimitPercentage autoIncrease) {
		this.autoIncrease = autoIncrease;
	}

	public boolean isPlaygroundYes() {
		return playgroundYes;
	}

	public void setPlaygroundYes(boolean playgroundYes) {
		this.playgroundYes = playgroundYes;
	}

	public boolean isPoolYes() {
		return poolYes;
	}

	public void setPoolYes(boolean poolYes) {
		this.poolYes = poolYes;
	}

	public boolean isSeasonalYes() {
		return seasonalYes;
	}

	public void setSeasonalYes(boolean seasonalYes) {
		this.seasonalYes = seasonalYes;
	}

	public double getAnnualGrossReceipts() {
		return annualGrossReceipts;
	}

	public void setAnnualGrossReceipts(double annualGrossReceipts) {
		this.annualGrossReceipts = annualGrossReceipts;
	}

	public ArrayList<PolicyLocationAdditionalInsured> getAdditionalInsuredLocationsList() {
		return additionalInsuredLocationsList;
	}

	public void setAdditionalInsuredLocationsList(
			ArrayList<PolicyLocationAdditionalInsured> additionalInsuredLocationsList) {
		this.additionalInsuredLocationsList = additionalInsuredLocationsList;
	}

	public PolicyLocationAdditionalCoverages getAdditionalCoveragesStuff() {
		return additionalCoveragesStuff;
	}

	public void setAdditionalCoveragesStuff(PolicyLocationAdditionalCoverages additionalCoveragesStuff) {
		this.additionalCoveragesStuff = additionalCoveragesStuff;
	}

	public ArrayList<PolicyLocationBuilding> getBuildingList() {
		return buildingList;
	}

	public void setBuildingList(ArrayList<PolicyLocationBuilding> buildingList) {
		if (buildingList == null || buildingList.size() < 1) {
			Assert.fail("Every Location Must Have At Least One Building");
		} else {
			this.buildingList = buildingList;
		}
	}
	
	public int getNumBuildings() {
		int num = 0;
		num = num + this.getBuildingList().size();
		return num;
	}

//	public ArrayList<PLPolicyLocationProperty> getBuildingListPL() {
//		return plBuildingList;
//	}
//
//	public void setBuildingListPL(ArrayList<PLPolicyLocationProperty> plBuildingList) throws GuidewireException {
//		if (plBuildingList == null || plBuildingList.size() < 1) {
//			Assert.fail(getCurrentUrl() +
//					"Every Location Must Have At Least One Building");
//		} else {
//			this.plBuildingList = plBuildingList;
//		}
//	}



	public int getPlNumAcres() {
		return plNumAcres;
	}

	public void setPlNumAcres(int plNumAcres) {
		this.plNumAcres = plNumAcres;
	}

	public int getPlNumResidence() {
		return plNumResidence;
	}

	public void setPlNumResidence(int plNumResidence) {
		this.plNumResidence = plNumResidence;
	}

	public boolean isNonSpecificLocation() {
		return nonSpecificLocation;
	}

	public void setNonSpecificLocation(boolean nonSpecificLocation) {
		this.nonSpecificLocation = nonSpecificLocation;
	}

	public boolean isAddressIsStandardized() {
		return addressIsStandardized;
	}

	public void setAddressIsStandardized(boolean addressIsStandardized) {
		this.addressIsStandardized = addressIsStandardized;
	}

	public String getLoanOrContractNumber() {
		return loanOrContractNumber;
	}

	public void setLoanOrContractNumber(String loanOrContractNumber) {
		this.loanOrContractNumber = loanOrContractNumber;
	}
	
	public TownshipRange getTownshipRange(){
		return this.township;
	}
	
	public void setTownshipRange(CountyIdaho county) throws Exception{
		this.township = TownshipRangeHelper.getRandomTownshipRangeForCounty(county.getValue());
	}
	
	public Location.LocationType getLocationType(){
		return this.locationType;
	}
	
	public void setLocationType(Location.LocationType _locationType){
		this.locationType = _locationType;
	}
	
	public TownshipRange getTownship() {
		return township;
	}

	public void setTownship(TownshipRange township) {
		this.township = township;
	}

	public ArrayList<PLPolicyLocationProperty> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(ArrayList<PLPolicyLocationProperty> propertyList) {
		this.propertyList = propertyList;
	}

	public void setAutoIncrease(Location.AutoIncreaseBlgLimitPercentage autoIncrease) {
		this.autoIncrease = autoIncrease;
	}

	public repository.gw.enums.SectionI_UnderwriterIssues getSectionIUWIssue() {
		return sectionIUWIssue;
	}

	public void setSectionIUWIssue(SectionI_UnderwriterIssues sectionIUWIssue) {
		this.sectionIUWIssue = sectionIUWIssue;
	}
}
