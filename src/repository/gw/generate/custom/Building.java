package repository.gw.generate.custom;

import repository.gw.helpers.NumberUtils;

import java.util.ArrayList;

public class Building {
	
	public Building() {
		
	}
	

	protected int number = -1;
	protected String usageDescription = "Test Default Building";
	protected String classCode = "59325";
	protected int yearBuilt = 2013;
	protected int numStories = 1;
	protected int numBasements = 0;
	protected String totalArea = "3000";
	protected String basementArea = "100";
	protected repository.gw.enums.Building.ConstructionType constructionType = repository.gw.enums.Building.ConstructionType.Frame;
	
	protected repository.gw.enums.Building.RoofCondition roofCondition = repository.gw.enums.Building.RoofCondition.NoIssues;
	protected boolean flatRoof = false;
	protected repository.gw.enums.Building.RoofingType roofingType = repository.gw.enums.Building.RoofingType.Aluminum;
	protected int yearRoofReplaced = this.yearBuilt;
	
	protected int yearLastMajorHeatingUpdate = this.yearBuilt;
	protected String heatingUpdateDesc = "Original Construction";
	
	protected int yearLastMajorPlumbingUpdate = this.yearBuilt;
	protected String plumbingUpdateDesc = "Original Construction";
	
	protected repository.gw.enums.Building.WiringType wiringType = repository.gw.enums.Building.WiringType.Romex;
	protected repository.gw.enums.Building.BoxType boxType = repository.gw.enums.Building.BoxType.CircuitBreaker;
	protected int yearLastMajorWiringUpdate = this.yearBuilt;
	protected String wiringUpdateDesc = "Original Construction";
	
	protected repository.gw.enums.Building.FireBurglaryTypeOfSystem fireBurglaryTypeOfSystem;
	protected repository.gw.enums.Building.FireBurglaryResponseType fireBurglaryResponseType;
	protected repository.gw.enums.Building.FireBurglaryAlarmGrade fireBurglaryAlarmGrade;
	protected String alarmCertificate;
	
	
	protected ArrayList<repository.gw.generate.custom.AdditionalInterest> additionalInterestList = new ArrayList<repository.gw.generate.custom.AdditionalInterest>();
	
	
	
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getUsageDescription() {
		return usageDescription;
	}
	public void setUsageDescription(String usageDescription) {
		this.usageDescription = usageDescription;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	
	public void setClassCode(String...classCode) {
		this.classCode = classCode[NumberUtils.generateRandomNumberInt(0, classCode.length-1)];
	}
	public int getYearBuilt() {
		return yearBuilt;
	}
	public void setYearBuilt(int yearBuilt) {
		this.yearBuilt = yearBuilt;
	}
	public int getNumStories() {
		return numStories;
	}
	public void setNumStories(int numStories) {
		this.numStories = numStories;
	}
	public int getNumBasements() {
		return numBasements;
	}
	public void setNumBasements(int numBasements) {
		this.numBasements = numBasements;
	}
	public String getTotalArea() {
		return totalArea;
	}
	public void setTotalArea(String totalArea) {
		this.totalArea = totalArea;
	}
	public repository.gw.enums.Building.ConstructionType getConstructionType() {
		return constructionType;
	}
	public void setConstructionType(repository.gw.enums.Building.ConstructionType constructionType) {
		this.constructionType = constructionType;
	}
	public String getBasementArea() {
		return basementArea;
	}
	public void setBasementArea(String basementArea) {
		this.basementArea = basementArea;
	}
	public repository.gw.enums.Building.RoofCondition getRoofCondition() {
		return roofCondition;
	}
	public void setRoofCondition(repository.gw.enums.Building.RoofCondition roofCondition) {
		this.roofCondition = roofCondition;
	}
	public boolean isFlatRoof() {
		return flatRoof;
	}
	public void setFlatRoof(boolean flatRoof) {
		this.flatRoof = flatRoof;
	}
	public repository.gw.enums.Building.RoofingType getRoofingType() {
		return roofingType;
	}
	public void setRoofingType(repository.gw.enums.Building.RoofingType roofingType) {
		this.roofingType = roofingType;
	}
	public int getYearRoofReplaced() {
		return yearRoofReplaced;
	}
	public void setYearRoofReplaced(int yearRoofReplaced) {
		this.yearRoofReplaced = yearRoofReplaced;
	}
	public int getYearLastMajorHeatingUpdate() {
		return yearLastMajorHeatingUpdate;
	}
	public void setYearLastMajorHeatingUpdate(int yearLastMajorHeatingUpdate) {
		this.yearLastMajorHeatingUpdate = yearLastMajorHeatingUpdate;
	}
	public String getHeatingUpdateDesc() {
		return heatingUpdateDesc;
	}
	public void setHeatingUpdateDesc(String heatingUpdateDesc) {
		this.heatingUpdateDesc = heatingUpdateDesc;
	}
	public int getYearLastMajorPlumbingUpdate() {
		return yearLastMajorPlumbingUpdate;
	}
	public void setYearLastMajorPlumbingUpdate(int yearLastMajorPlumbingUpdate) {
		this.yearLastMajorPlumbingUpdate = yearLastMajorPlumbingUpdate;
	}
	public String getPlumbingUpdateDesc() {
		return plumbingUpdateDesc;
	}
	public void setPlumbingUpdateDesc(String plumbingUpdateDesc) {
		this.plumbingUpdateDesc = plumbingUpdateDesc;
	}
	public repository.gw.enums.Building.WiringType getWiringType() {
		return wiringType;
	}
	public void setWiringType(repository.gw.enums.Building.WiringType wiringType) {
		this.wiringType = wiringType;
	}
	public int getYearLastMajorWiringUpdate() {
		return yearLastMajorWiringUpdate;
	}
	public void setYearLastMajorWiringUpdate(int yearLastMajorWiringUpdate) {
		this.yearLastMajorWiringUpdate = yearLastMajorWiringUpdate;
	}
	public String getWiringUpdateDesc() {
		return wiringUpdateDesc;
	}
	public void setWiringUpdateDesc(String wiringUpdateDesc) {
		this.wiringUpdateDesc = wiringUpdateDesc;
	}
	public repository.gw.enums.Building.FireBurglaryTypeOfSystem getFireBurglaryTypeOfSystem() {
		return fireBurglaryTypeOfSystem;
	}
	public void setFireBurglaryTypeOfSystem(repository.gw.enums.Building.FireBurglaryTypeOfSystem fireBurglaryTypeOfSystem) {
		this.fireBurglaryTypeOfSystem = fireBurglaryTypeOfSystem;
	}
	public repository.gw.enums.Building.FireBurglaryResponseType getFireBurglaryResponseType() {
		return fireBurglaryResponseType;
	}
	public void setFireBurglaryResponseType(repository.gw.enums.Building.FireBurglaryResponseType fireBurglaryResponseType) {
		this.fireBurglaryResponseType = fireBurglaryResponseType;
	}
	public repository.gw.enums.Building.FireBurglaryAlarmGrade getFireBurglaryAlarmGrade() {
		return fireBurglaryAlarmGrade;
	}
	public void setFireBurglaryAlarmGrade(repository.gw.enums.Building.FireBurglaryAlarmGrade fireBurglaryAlarmGrade) {
		this.fireBurglaryAlarmGrade = fireBurglaryAlarmGrade;
	}
	public String getAlarmCertificate() {
		return alarmCertificate;
	}
	public void setAlarmCertificate(String alarmCertificate) {
		this.alarmCertificate = alarmCertificate;
	}
	public ArrayList<repository.gw.generate.custom.AdditionalInterest> getAdditionalInterestList() {
		return additionalInterestList;
	}
	public void setAdditionalInterestList(ArrayList<AdditionalInterest> additionalInterestList) {
		this.additionalInterestList = additionalInterestList;
	}
	public repository.gw.enums.Building.BoxType getBoxType() {
		return boxType;
	}
	public void setBoxType(repository.gw.enums.Building.BoxType boxType) {
		this.boxType = boxType;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}






