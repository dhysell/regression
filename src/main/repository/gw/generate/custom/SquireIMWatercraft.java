package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineTypes;
import repository.gw.enums.Vehicle;

import java.util.List;

public class SquireIMWatercraft {
	
	private InlandMarineTypes.WatercraftTypes waterToyType;
	private Vehicle.PAComprehensive_CollisionDeductible waterToyDeductible;
	private boolean inspected = true;
	private boolean existingDamage = false;
	private String damageDescription = null;
	private String registeredOwner = "Bill Martin";
	private List<String> additionalInsured;
	private List<repository.gw.generate.custom.AdditionalInterest> addInterests;
	private List<WatercraftScheduledItems> scheduledItems;
	
	//*****Constructors************
	//At minimum there needs to be a type, a deductible, and a scheduled Item.  I set the other variables to accomodate the bare minimum.
	
	public SquireIMWatercraft(InlandMarineTypes.WatercraftTypes _waterToyType, Vehicle.PAComprehensive_CollisionDeductible deductible, List<WatercraftScheduledItems> scheduledItems){
		setWaterToyType(_waterToyType);
		setWaterToyDeductible(deductible);
		setScheduledItems(scheduledItems);
	}
	
	public SquireIMWatercraft(InlandMarineTypes.WatercraftTypes _waterToyType, Vehicle.PAComprehensive_CollisionDeductible deductible, WatercraftScheduledItems scheduledItem){
		setWaterToyType(_waterToyType);
		setWaterToyDeductible(deductible);
		addScheduledItems(scheduledItem);
	}	
	
	//*****Getters and Setters*****
	
	public void setWaterToyType(InlandMarineTypes.WatercraftTypes _waterToyType){
		this.waterToyType = _waterToyType;
	}
	
	public InlandMarineTypes.WatercraftTypes getWaterToyType(){
		return this.waterToyType;
	}
	
	public void setWaterToyDeductible(Vehicle.PAComprehensive_CollisionDeductible deductible){
		this.waterToyDeductible = deductible;
	}
	
	public Vehicle.PAComprehensive_CollisionDeductible getWaterToyDeductible(){
		return this.waterToyDeductible;
	}
	
	public void setInspected(boolean _inspected){
		this.inspected = _inspected;
	}
	
	
	public boolean getInspected(){
		return this.inspected;
	}
	
	public void setExistingDamage(boolean _existingDamage){
		this.existingDamage = _existingDamage;
	}
	
	public boolean getExistingDamage(){
		return this.existingDamage;
	}
	
	public void setDamageDescription(String description){
		this.damageDescription = description;
	}
	
	public String getDamageDescription(){
		return this.damageDescription;
	}
	
	public void setRegisteredOwner(String owner){
		if(owner == null || owner==""){
			this.registeredOwner = "Bill Martin";
		}
		else{
			this.registeredOwner = owner;
		}
	}
	
	public String getRegisteredOwner(){
		return this.registeredOwner;
	}
	
	public void setAdditionalInsured(List<String> _additionalInsured){
		this.additionalInsured = _additionalInsured;
	}
	
	public void addAdditionalInsured(String _additionalInsuredString){
		this.additionalInsured.add(_additionalInsuredString);
	}
	
	public List<String> getAdditionalInsured(){
		return this.getAdditionalInsured();
	}
	
	public void setAdditionalInterest(List<repository.gw.generate.custom.AdditionalInterest> _additionalInterest){
		this.addInterests = _additionalInterest;
	}
	
	public void addAdditionalInterest(repository.gw.generate.custom.AdditionalInterest _additionalInterestString){
		this.addInterests.add(_additionalInterestString);
	}
	
	public List<AdditionalInterest> getAdditionalInterest(){
		return this.addInterests;
	}
	
	public void setScheduledItems(List<WatercraftScheduledItems> _toys){
		this.scheduledItems = _toys;
	}
	
	public void addScheduledItems(WatercraftScheduledItems toy){
		this.scheduledItems.add(toy);
	}
	
	public List<WatercraftScheduledItems> getScheduledItems(){
		return this.scheduledItems;
	}	
}
