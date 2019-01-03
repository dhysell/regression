package repository.gw.generate.custom;

import java.math.BigDecimal;
import java.util.Date;

public class UIPersonalPrefillVehicleReported {
	
	private int modelYear;
	private String make;
	private String model;
	private String vin;
	private Date plateExpirationDate;
	private String weight;
	private BigDecimal price;
	private String owner;
	
	public int getModelYear() {
		return modelYear;
	}
	
	public void setModelYear(int modelYear) {
		this.modelYear = modelYear;
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

	public Date getPlateExpirationDate() {
		return plateExpirationDate;
	}

	public void setPlateExpirationDate(Date plateExpirationDate) {
		this.plateExpirationDate = plateExpirationDate;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	

}
