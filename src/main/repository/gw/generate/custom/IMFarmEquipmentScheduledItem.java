package repository.gw.generate.custom;

import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;

import java.util.ArrayList;

public class IMFarmEquipmentScheduledItem {

	//Scheduled Equipment
		private String typeOfEquipment;
		private Vin vin;
		private String itemDescription;
		private int limit;
		private String location;
		private ArrayList<String> additionalInsured;
		private ArrayList<repository.gw.generate.custom.AdditionalInterest> addInterest;
		
		
	//Constructors
	public IMFarmEquipmentScheduledItem(String _typeOfEquipment, String _itemDescription, int _limit) throws Exception{
		setVin();
		setTypeOfEquipment(_typeOfEquipment);
		setItemDescription(_itemDescription);
		setLimit(_limit);
	}
	
	//getters and setters
		private void setVin() throws Exception{
			this.vin = VINHelper.getRandomVIN();
		}
		
		public Vin getVinObj(){
			return this.vin;
		}
		
		public String getVin(){
			return this.vin.getVin();		
		}
		
		public String getYear(){
			return this.vin.getYear();
		}
		
		public String getMake(){
			return this.vin.getMake();
		}
		
		public void setItemDescription(String _itemDescription) {
			this.itemDescription = _itemDescription;
		}
		
		public String getItemDescription(){
			return this.itemDescription;
		}
		
		public void setLocation(String _location) {
			this.location = _location;
		}
		
		public String getLocation(){
			return this.location;
		}
		
		public void setAdditionalInsured(ArrayList<String> _additionalInsured) {
			this.additionalInsured = _additionalInsured;
		}
		
		public ArrayList<String> getAdditionalInsured(){
			return this.additionalInsured;
		}
		
		public void addAdditionalInsured(String addInsured){
			this.additionalInsured.add(addInsured);
		}
		
		public void setTypeOfEquipment(String _typeOfEquipment) {
			this.typeOfEquipment = _typeOfEquipment;
		}
		
		public String getTypeOfEquipment(){
			return this.typeOfEquipment;
		}
		
		public ArrayList<repository.gw.generate.custom.AdditionalInterest> getAddInterestList(){
			return this.addInterest;
		}
		
		public void setAddInterestList(ArrayList<repository.gw.generate.custom.AdditionalInterest> _addInterest){
			this.addInterest = _addInterest;
		}
		
		public void addAdditionalInterest(AdditionalInterest _addInterest){
			addInterest.add(_addInterest);
		}
		
		public void setLimit(int _limit) {
			this.limit = _limit;
		}
		
		public int getLimit(){
			return this.limit;
		}
}
