package persistence.globaldatarepo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ClaimsDataFNOL", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")

public class ClaimsFNOLData {

	private String claimNumber;
	private String policyNumber;
	private String dateOfLoss;
	private String timeStamp;
	private String lob;
	private String environment;
	
	public ClaimsFNOLData() {
		
	}
	
	public ClaimsFNOLData(String claimNumber, String policyNumber, String dateOfLoss, String creationTimeStamp, String lob, String environment) {
		this.claimNumber = claimNumber;
		this.policyNumber = policyNumber;
		this.dateOfLoss = dateOfLoss;
		this.timeStamp = creationTimeStamp;
		this.lob = lob;
		this.environment = environment;
	}
	
	@Id
	@Column(name = "ClaimNumber", nullable = false, length = 50)
	public String getClaimNumber() {
		return this.claimNumber;
	}
	
	@Column(name = "PolicyNumber", nullable = false, length = 50)
	public String getPolicyNumber() {
		return this.policyNumber;
	}
	
	@Column(name = "DateOfLoss", nullable = false, length = 50)
	public String getDateOfLoss() {
		return this.dateOfLoss;
	}
	
	@Column(name = "CreationDateTime", nullable = false, length = 50)
	public String getTimeStamp() {
		return this.timeStamp;
	}
	
	@Column(name = "LineOfBusiness", nullable = false, length = 50)
	public String getLob() {
		return this.lob;
	}
	
	@Column(name = "Environment", nullable = false, length = 50)
	public String getEnvironment() {
		return this.environment;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public void setDateOfLoss(String dateOfLoss) {
		this.dateOfLoss = dateOfLoss;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public void setLob(String lob) {
		this.lob = lob;
	}
	
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	
}
