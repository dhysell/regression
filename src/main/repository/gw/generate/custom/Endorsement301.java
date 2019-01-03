package repository.gw.generate.custom;

public class Endorsement301 {
	
	private int year = 2000;
	private String make = "Scion";
	private String model = "FRS";
	private String vin = "MyViNnUmBeR123";
	
	public Endorsement301(int year, String make, String model, String vin) {
		super();
		this.year = year;
		this.make = make;
		this.model = model;
		this.vin = vin;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}
	
	
	

}
