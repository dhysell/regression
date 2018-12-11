package persistence.globaldatarepo.entities;

// Generated Jul 22, 2016 3:44:57 PM by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RegionCounty generated by hbm2java
 */
@Entity
@Table(name = "RegionCounty", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class RegionCounty {

	private String countyName;
	private String regionName;
	private String underWriter;

	public RegionCounty() {
	}

	public RegionCounty(String countyName, String regionName, String underWriter) {
		this.countyName = countyName;
		this.regionName = regionName;
		this.underWriter = underWriter;
	}

	@Id
	@Column(name = "CountyName", unique = true, nullable = false, length = 100)
	public String getCountyName() {
		return this.countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	@Column(name = "RegionName", nullable = false, length = 100)
	public String getRegionName() {
		return this.regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@Column(name = "UnderWriter", nullable = false, length = 100)
	public String getUnderWriter() {
		return this.underWriter;
	}

	public void setUnderWriter(String underWriter) {
		this.underWriter = underWriter;
	}

}
