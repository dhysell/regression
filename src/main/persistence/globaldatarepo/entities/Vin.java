package persistence.globaldatarepo.entities;

import javax.persistence.*;

@Entity
@Table(name = "VIN", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class Vin {

	private int id;
	private String vin;
	private String year;
	private String make;
	private String model;

	public Vin() {
	}

	public Vin(int id, String vin, String year, String make, String model) {
		this.id = id;
		this.vin = vin;
		this.year = year;
		this.make = make;
		this.model = model;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "VIN", nullable = false, length = 17)
	public String getVin() {
		return this.vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	@Column(name = "Year", nullable = false, length = 4)
	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Column(name = "Make", nullable = false, length = 50)
	public String getMake() {
		return this.make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	@Column(name = "Model", nullable = false, length = 50)
	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

}
