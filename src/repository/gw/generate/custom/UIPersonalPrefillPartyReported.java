package repository.gw.generate.custom;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;

import java.util.Date;

public class UIPersonalPrefillPartyReported {
	
	private String consumerName;
	private Date dateOfBirth;
	private Gender gender;
	private String ssn;
	private State licenseState;
	private String licenseNumber;
	
	public String getConsumerName() {
		return consumerName;
	}
	
	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public State getLicenseState() {
		return licenseState;
	}

	public void setLicenseState(State licenseState) {
		this.licenseState = licenseState;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

}
