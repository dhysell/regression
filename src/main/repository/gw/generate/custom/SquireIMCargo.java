package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineTypes;

import java.util.ArrayList;
import java.util.List;

public class SquireIMCargo {
	
	private InlandMarineTypes.CargoClass cargoClass;
	private InlandMarineTypes.ClassIIICargoTypes cargoType;
	private int radius;
	private String customerOwnsHaul;
	private boolean cargoCoverage;
	private ArrayList<String> cargoAdditionalInsured;
	private String cargoDescription = null;
	//Scheduled Items use Vehicle data.
	private ArrayList<Cargo> scheduledItems;	

	
	//Constructor
	public SquireIMCargo(InlandMarineTypes.CargoClass _cargoClass, int _radius, int scheduleLimit, String _customerOwnsHaul, boolean _cargoCoverage, ArrayList<Cargo> _scheduledItems){
		setCargoClass(_cargoClass);		
		setRadius(_radius);
		setCargoCoverage(_cargoCoverage);
		setScheduledItems(_scheduledItems);
	}
	
	//need class III
	public SquireIMCargo(InlandMarineTypes.CargoClass _cargoClass, InlandMarineTypes.ClassIIICargoTypes _cargoType, int _radius, int scheduleLimit, String _customerOwnsHaul, boolean _cargoCoverage, ArrayList<Cargo> _scheduledItems){
		setCargoClass(_cargoClass);	
		setCargoType(_cargoType);
		setRadius(_radius);
		setCargoCoverage(_cargoCoverage);
		setScheduledItems(_scheduledItems);
	}
	
	public SquireIMCargo(InlandMarineTypes.CargoClass _cargoClass, InlandMarineTypes.ClassIIICargoTypes _cargoType, int _radius, int scheduleLimit, String _customerOwnsHaul, boolean _cargoCoverage, ArrayList<Cargo> _scheduledItems, ArrayList<String> _cargoAdditionalInsured){
		setCargoClass(_cargoClass);	
		setCargoType(_cargoType);
		setRadius(_radius);
		setCargoCoverage(_cargoCoverage);
		setScheduledItems(_scheduledItems);
		setCargoAdditionalInsured(_cargoAdditionalInsured);
	}
	
	public SquireIMCargo(InlandMarineTypes.CargoClass _cargoClass, String _cargoDescription, ArrayList<Cargo> _scheduledItems){
		setCargoClass(_cargoClass);	
		setCargoDescription(_cargoDescription);
		setScheduledItems(_scheduledItems);
	}
	
	
	//Getters and Setters
	public InlandMarineTypes.CargoClass getCargoClass(){
		return this.cargoClass;
	}
	
	public void setCargoClass(InlandMarineTypes.CargoClass _cargoClass){
		this.cargoClass = _cargoClass;
	}
	
	public InlandMarineTypes.ClassIIICargoTypes getCargoType(){
		return this.cargoType;
	}
	
	public void setCargoType(InlandMarineTypes.ClassIIICargoTypes _cargoType){
		this.cargoType = _cargoType;
	}
	
	public int getRadius(){
		return this.radius;
	}
	
	public void setRadius(int _radius){
		this.radius = _radius;
	}
	
	public boolean getCargoCoverage(){
		return this.cargoCoverage;
	}
	
	public void setCargoCoverage(boolean _cargoCoverage){
		this.cargoCoverage = _cargoCoverage;
	}
	
	public List<Cargo> getScheduledItemList(){
		return this.scheduledItems;
	}
	
	public void setScheduledItems(ArrayList<Cargo> _scheduledItem){
		this.scheduledItems = _scheduledItem;
	} 
	
	public void addScheduledItem(Cargo _scheduledItem){
		this.scheduledItems.add(_scheduledItem);
	}
	
	public void setCustomerOwnsHaul(String _customerOwnsHaul){
		this.customerOwnsHaul = _customerOwnsHaul;
	} 
	
	public String getCustomerOwnsHaul(){
		return this.customerOwnsHaul;
	}
	
	public List<String> getCargoAdditionalInsured(){
		return this.cargoAdditionalInsured;
	}
	
	public void setCargoAdditionalInsured(ArrayList<String> addInsured){
		this.cargoAdditionalInsured = addInsured;
	} 
	
	public void addCargoAdditionalInsured(String addInsured){
		this.cargoAdditionalInsured.add(addInsured);
	}
	
	public int getCargoLimit(){
		int sum = 0;
		for(Cargo trailer : scheduledItems){
			sum += Integer.parseInt(trailer.getLimit());
		}
		return sum;	
	}
	
	public String getCargoDescription(){
		return this.cargoDescription;
	}
	
	public void setCargoDescription(String description){
		if(description.length() > 250){
			description = description.substring(0, 250);
		}
		this.cargoDescription = description;
	}
}
