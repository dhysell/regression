package repository.gw.generate.custom;

import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import persistence.globaldatarepo.entities.InsuranceScoreTestCases;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.helpers.DateUtils;

import javax.persistence.Transient;
import java.text.ParseException;
import java.util.Date;

//import org.testng.platform.suite.api.UseTechnicalNames;

public class InsuranceScore_Verisk {
	
	private String reportType;
	private String referenceNumber;
	private String dateOrdered;
	private String nameFromReport;
	private String insuranceStatus;
	private String flag;
	private String vendorReferenceNumber;
	private String insuranceScore;
	private String enhancedInsuranceScore;
	
	
	public InsuranceScore_Verisk() {
	}
	
	
	public InsuranceScore_Verisk(String reportType, String referenceNumber, String dateOrdered, String nameFromReport,
			String insuranceStatus, String flag, String vendorReferenceNumber, String insuranceScore,
			String enhancedInsuranceScore) {
		super();
		this.reportType = reportType;
		this.referenceNumber = referenceNumber;
		this.dateOrdered = dateOrdered;
		this.nameFromReport = nameFromReport;
		this.insuranceStatus = insuranceStatus;
		this.flag = flag;
		this.vendorReferenceNumber = vendorReferenceNumber;
		this.insuranceScore = insuranceScore;
		this.enhancedInsuranceScore = enhancedInsuranceScore;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getDateOrdered() {
		return dateOrdered;
	}
	public void setDateOrdered(String dateOrdered) {
		this.dateOrdered = dateOrdered;
	}
	public String getNameFromReport() {
		return nameFromReport;
	}
	public void setNameFromReport(String nameFromReport) {
		this.nameFromReport = nameFromReport;
	}
	public String getInsuranceStatus() {
		return insuranceStatus;
	}
	public void setInsuranceStatus(String insuranceStatus) {
		this.insuranceStatus = insuranceStatus;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getVendorReferenceNumber() {
		return vendorReferenceNumber;
	}
	public void setVendorReferenceNumber(String vendorReferenceNumber) {
		this.vendorReferenceNumber = vendorReferenceNumber;
	}
	public String getInsuranceScore() {
		return insuranceScore;
	}
	public void setInsuranceScore(String insuranceScore) {
		this.insuranceScore = insuranceScore;
	}
	public String getEnhancedInsuranceScore() {
		return enhancedInsuranceScore;
	}
	public void setEnhancedInsuranceScore(String enhancedInsuranceScore) {
		this.enhancedInsuranceScore = enhancedInsuranceScore;
	}
	
	
	@Transient
	public int calculateEnhancedScore(WebDriver driver, InsuranceScoreTestCases contact) throws ParseException {
		double enhancedScoreCalculated = 0.0;
    	Date contactDOB = repository.gw.helpers.DateUtils.convertStringtoDate(contact.getDobasof102017(), "yyyyMMdd");
    	int age = repository.gw.helpers.DateUtils.getDifferenceBetweenDates(contactDOB, DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateDifferenceOptions.Year);
    	if(age <= 55) {
    		enhancedScoreCalculated = (((56-age)*.005) + 1) * (Integer.valueOf(this.getInsuranceScore().replace("+", "")));
    		return (int)Math.round(enhancedScoreCalculated);
    	} else {
    		return Integer.valueOf(this.getInsuranceScore());
    	}
    }
    
//    if(age <= 55){
//        var enhancedScore = ( ( (56-age)* .005) + 1 ) * (this.CreditScore?.toDouble())
//        this.EnhancedCreditScore_FBM = (java.lang.Math.round(enhancedScore)) as String
//      }else{
//        this.EnhancedCreditScore_FBM = this.CreditScore
//      }
	

}
