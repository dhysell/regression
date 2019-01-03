package persistence.globaldatarepo.entities;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "TownshipRange", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class TownshipRange {

	private int id;
	private String county;
	private String township;
	private String townshipDirection;
	private String range;
	private String rangeDirection;
	@Transient
	private ArrayList<String> sections = new ArrayList<String>();

	public TownshipRange() {
	}

	public TownshipRange(int id, String county, String township, String townshipDirection, String range,
			String rangeDirection) {
		this.id = id;
		this.county = county;
		this.township = township;
		this.townshipDirection = townshipDirection;
		this.range = range;
		this.rangeDirection = rangeDirection;
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

	@Column(name = "County", nullable = false, length = 50)
	public String getCounty() {
		return this.county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	@Column(name = "Township", nullable = false, length = 3)
	public String getTownship() {
		return this.township;
	}

	public void setTownship(String township) {
		this.township = township;
	}

	@Column(name = "TownshipDirection", nullable = false, length = 1)
	public String getTownshipDirection() {
		return this.townshipDirection;
	}

	public void setTownshipDirection(String townshipDirection) {
		this.townshipDirection = townshipDirection;
	}

	@Column(name = "Range", nullable = false, length = 3)
	public String getRange() {
		return this.range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	@Column(name = "RangeDirection", nullable = false, length = 3)
	public String getRangeDirection() {
		return this.rangeDirection;
	}

	public void setRangeDirection(String rangeDirection) {
		this.rangeDirection = rangeDirection;
	}
	
	@Transient
	public ArrayList<String> getSections(){
		return this.sections;
	}
	
	@Transient
	public void setSections(ArrayList<String> _sections){
		this.sections = _sections;
	}
	
	@Transient
	public void addSection(String _section){
		this.sections.add(_section);
	}

}
