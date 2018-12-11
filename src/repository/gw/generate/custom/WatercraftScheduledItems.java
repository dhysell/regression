package repository.gw.generate.custom;

import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;
import repository.gw.enums.InlandMarineTypes;
import repository.gw.helpers.DateUtils;

import java.util.Date;

public class WatercraftScheduledItems {
	
	private InlandMarineTypes.WatercraftTypes type;
	private Date year;
	private Vin vin;
	private String make;
	private String model;
	private int limit;
	private InlandMarineTypes.WatercratItems item;
	private int boatLength;
	private InlandMarineTypes.BoatType boatType;
	private String description = "Testing Purpose";
	
	public WatercraftScheduledItems(InlandMarineTypes.WatercraftTypes type, Date year, int limit) throws Exception{
		setWatercraftTypes(type);
		setDate(year);
		setVin();
		setMake(vin.getMake());
		setModel(vin.getModel());
		setLimit(limit);
	}
	
	public void setWatercraftTypes(InlandMarineTypes.WatercraftTypes _type){
		this.type = _type;
	}
	
	public InlandMarineTypes.WatercraftTypes getWatercraftTypes(){
		return this.type;
	}
	
	public void setDate(Date _date){
		this.year = _date;
	}
	
	public String getYearString(){
		return DateUtils.dateFormatAsString("yyyy", this.year);
	}
	
	public Date getYear(){
		return this.year;
	}
	
	private void setVin() throws Exception{
		this.vin = VINHelper.getRandomVIN();
	}
	
	public String getVin(){
		return this.vin.getVin();
	}
	
	public void setMake(String _make){
		this.make = vin.getMake();
	}
	
	public String getMake(){
		return this.make;
	}
	
	public void setDescription(String desc){
		this.description = desc;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setModel(String _model){
		this.model = this.vin.getModel();
	}
	
	public String getModel(){
		return this.model;
	}
	
	public void setLimit(int _limit){
		if(_limit <= 0){
			this.limit = 1;
		}
		else{
			this.limit = _limit;
		}
	}
	
	public int getLimit(){
		return this.limit;
	}
	
	public void setItem(InlandMarineTypes.WatercratItems _type){
		this.item = _type;
	}
	
	public InlandMarineTypes.WatercratItems getItem(){
		return this.item;
	}
	
	public void setLength(int length){
		this.boatLength = length;
	}
	
	public int getLength(){
		return this.boatLength;
	}
	
	public void setBoatType(InlandMarineTypes.BoatType _type){
		this.boatType = _type;
	}
	
	public InlandMarineTypes.BoatType getBoatType(){
		return this.boatType;
	}
	
	
}
