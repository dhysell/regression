package persistence.globaldatarepo.entities;
// Generated Sep 28, 2018 2:09:49 PM by Hibernate Tools 5.2.10.Final

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * FarmBureauPropertyMaster0917 generated by hbm2java
 */
@Entity
@Table(name = "FarmBureauPropertyMaster0917", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class FarmBureauPropertyMaster0917 {

	private Integer id;
	private String testCaseNumber;
	private String individualbusinessId;
	private String businessname;
	private String businessasdba;
	private String businessfederalid;
	private String insuredlastname;
	private String insuredfirstname;
	private String insuredmiddleinitial;
	private String insuredakalastname;
	private String insuredakafirstname;
	private String insuredakamiddleinitial;
	private String insuredssn;
	private String insureddob;
	private String insuredgender;
	private String secondinsuredlastname;
	private String secondinsuredfirstname;
	private String secondinsuredmiddleinitial;
	private String secondinsuredakalastname;
	private String secondinsuredakafirstname;
	private String secondinsuredakamiddleinitial;
	private String secondinsuredssn;
	private String secondinsureddob;
	private String secondinduredgender;
	private String lossoraccidentlocationstreetnumber;
	private String lossoraccidentlocationstreetname;
	private String lossoraccidentlocationstreettype;
	private String lossoraccidentlocationaptnumbrer;
	private String lossoraccidentlocationcity;
	private String lossoraccidentlocationstate;
	private String lossoraccidentlocationzipcode;
	private String lossoraccidentlocationzipcodeextension;
	private String insuredsstreetnumber;
	private String insuredsstreetname;
	private String insuredsstreettype;
	private String insuredsaptnumer;
	private String insuredscity;
	private String insuredsstate;
	private String insuredszipcode;
	private String insuredszipcodeextension;
	private String mortgagee;
	private String loannumber;
	private String policytype;
	private String policynumber;
	private String accountnumber;
	private String lossaccidentdate;
	private String claimamount;
	private String claimtype;
	private String casefilenumberclaim;
	private String catastrophenumber;
	private String descriptionofinjurycauseofloss;
	private String claimantlastname;
	private String claimantfirstname;
	private String claimantmiddlename;
	private String claimantstreetnumber;
	private String claimantstreename;
	private String claimantstreettype;
	private String claimantaptnumber;
	private String claimantcity;
	private String claimantstate;
	private String claimantzipcode;
	private String claimantzipcodeextension;
	private String claimantssn;
	private String claimantdob;
	private String claimantgender;
	private String fillerremarks;
	private String claimstatus;
	private String reportingstatusupdateflags;
	private String amBestnumber;
	private String domesticviolenceindicator;
	private String domesticviolencestatecode;
	private String fillerremarks2;

	public FarmBureauPropertyMaster0917() {
	}

	public FarmBureauPropertyMaster0917(String testCaseNumber, String individualbusinessId, String businessname,
			String businessasdba, String businessfederalid, String insuredlastname, String insuredfirstname,
			String insuredmiddleinitial, String insuredakalastname, String insuredakafirstname,
			String insuredakamiddleinitial, String insuredssn, String insureddob, String insuredgender,
			String secondinsuredlastname, String secondinsuredfirstname, String secondinsuredmiddleinitial,
			String secondinsuredakalastname, String secondinsuredakafirstname, String secondinsuredakamiddleinitial,
			String secondinsuredssn, String secondinsureddob, String secondinduredgender,
			String lossoraccidentlocationstreetnumber, String lossoraccidentlocationstreetname,
			String lossoraccidentlocationstreettype, String lossoraccidentlocationaptnumbrer,
			String lossoraccidentlocationcity, String lossoraccidentlocationstate, String lossoraccidentlocationzipcode,
			String lossoraccidentlocationzipcodeextension, String insuredsstreetnumber, String insuredsstreetname,
			String insuredsstreettype, String insuredsaptnumer, String insuredscity, String insuredsstate,
			String insuredszipcode, String insuredszipcodeextension, String mortgagee, String loannumber,
			String policytype, String policynumber, String accountnumber, String lossaccidentdate, String claimamount,
			String claimtype, String casefilenumberclaim, String catastrophenumber,
			String descriptionofinjurycauseofloss, String claimantlastname, String claimantfirstname,
			String claimantmiddlename, String claimantstreetnumber, String claimantstreename, String claimantstreettype,
			String claimantaptnumber, String claimantcity, String claimantstate, String claimantzipcode,
			String claimantzipcodeextension, String claimantssn, String claimantdob, String claimantgender,
			String fillerremarks, String claimstatus, String reportingstatusupdateflags, String amBestnumber,
			String domesticviolenceindicator, String domesticviolencestatecode, String fillerremarks2) {
		this.testCaseNumber = testCaseNumber;
		this.individualbusinessId = individualbusinessId;
		this.businessname = businessname;
		this.businessasdba = businessasdba;
		this.businessfederalid = businessfederalid;
		this.insuredlastname = insuredlastname;
		this.insuredfirstname = insuredfirstname;
		this.insuredmiddleinitial = insuredmiddleinitial;
		this.insuredakalastname = insuredakalastname;
		this.insuredakafirstname = insuredakafirstname;
		this.insuredakamiddleinitial = insuredakamiddleinitial;
		this.insuredssn = insuredssn;
		this.insureddob = insureddob;
		this.insuredgender = insuredgender;
		this.secondinsuredlastname = secondinsuredlastname;
		this.secondinsuredfirstname = secondinsuredfirstname;
		this.secondinsuredmiddleinitial = secondinsuredmiddleinitial;
		this.secondinsuredakalastname = secondinsuredakalastname;
		this.secondinsuredakafirstname = secondinsuredakafirstname;
		this.secondinsuredakamiddleinitial = secondinsuredakamiddleinitial;
		this.secondinsuredssn = secondinsuredssn;
		this.secondinsureddob = secondinsureddob;
		this.secondinduredgender = secondinduredgender;
		this.lossoraccidentlocationstreetnumber = lossoraccidentlocationstreetnumber;
		this.lossoraccidentlocationstreetname = lossoraccidentlocationstreetname;
		this.lossoraccidentlocationstreettype = lossoraccidentlocationstreettype;
		this.lossoraccidentlocationaptnumbrer = lossoraccidentlocationaptnumbrer;
		this.lossoraccidentlocationcity = lossoraccidentlocationcity;
		this.lossoraccidentlocationstate = lossoraccidentlocationstate;
		this.lossoraccidentlocationzipcode = lossoraccidentlocationzipcode;
		this.lossoraccidentlocationzipcodeextension = lossoraccidentlocationzipcodeextension;
		this.insuredsstreetnumber = insuredsstreetnumber;
		this.insuredsstreetname = insuredsstreetname;
		this.insuredsstreettype = insuredsstreettype;
		this.insuredsaptnumer = insuredsaptnumer;
		this.insuredscity = insuredscity;
		this.insuredsstate = insuredsstate;
		this.insuredszipcode = insuredszipcode;
		this.insuredszipcodeextension = insuredszipcodeextension;
		this.mortgagee = mortgagee;
		this.loannumber = loannumber;
		this.policytype = policytype;
		this.policynumber = policynumber;
		this.accountnumber = accountnumber;
		this.lossaccidentdate = lossaccidentdate;
		this.claimamount = claimamount;
		this.claimtype = claimtype;
		this.casefilenumberclaim = casefilenumberclaim;
		this.catastrophenumber = catastrophenumber;
		this.descriptionofinjurycauseofloss = descriptionofinjurycauseofloss;
		this.claimantlastname = claimantlastname;
		this.claimantfirstname = claimantfirstname;
		this.claimantmiddlename = claimantmiddlename;
		this.claimantstreetnumber = claimantstreetnumber;
		this.claimantstreename = claimantstreename;
		this.claimantstreettype = claimantstreettype;
		this.claimantaptnumber = claimantaptnumber;
		this.claimantcity = claimantcity;
		this.claimantstate = claimantstate;
		this.claimantzipcode = claimantzipcode;
		this.claimantzipcodeextension = claimantzipcodeextension;
		this.claimantssn = claimantssn;
		this.claimantdob = claimantdob;
		this.claimantgender = claimantgender;
		this.fillerremarks = fillerremarks;
		this.claimstatus = claimstatus;
		this.reportingstatusupdateflags = reportingstatusupdateflags;
		this.amBestnumber = amBestnumber;
		this.domesticviolenceindicator = domesticviolenceindicator;
		this.domesticviolencestatecode = domesticviolencestatecode;
		this.fillerremarks2 = fillerremarks2;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TestCaseNumber", length = 5000)
	public String getTestCaseNumber() {
		return this.testCaseNumber;
	}

	public void setTestCaseNumber(String testCaseNumber) {
		this.testCaseNumber = testCaseNumber;
	}

	@Column(name = "INDIVIDUALBUSINESSID", length = 5000)
	public String getIndividualbusinessId() {
		return this.individualbusinessId;
	}

	public void setIndividualbusinessId(String individualbusinessId) {
		this.individualbusinessId = individualbusinessId;
	}

	@Column(name = "BUSINESSNAME", length = 5000)
	public String getBusinessname() {
		return this.businessname;
	}

	public void setBusinessname(String businessname) {
		this.businessname = businessname;
	}

	@Column(name = "BUSINESSASDBA", length = 5000)
	public String getBusinessasdba() {
		return this.businessasdba;
	}

	public void setBusinessasdba(String businessasdba) {
		this.businessasdba = businessasdba;
	}

	@Column(name = "BUSINESSFEDERALID", length = 5000)
	public String getBusinessfederalid() {
		return this.businessfederalid;
	}

	public void setBusinessfederalid(String businessfederalid) {
		this.businessfederalid = businessfederalid;
	}

	@Column(name = "INSUREDLASTNAME", length = 5000)
	public String getInsuredlastname() {
		return this.insuredlastname;
	}

	public void setInsuredlastname(String insuredlastname) {
		this.insuredlastname = insuredlastname;
	}

	@Column(name = "INSUREDFIRSTNAME", length = 5000)
	public String getInsuredfirstname() {
		return this.insuredfirstname;
	}

	public void setInsuredfirstname(String insuredfirstname) {
		this.insuredfirstname = insuredfirstname;
	}

	@Column(name = "INSUREDMIDDLEINITIAL", length = 5000)
	public String getInsuredmiddleinitial() {
		return this.insuredmiddleinitial;
	}

	public void setInsuredmiddleinitial(String insuredmiddleinitial) {
		this.insuredmiddleinitial = insuredmiddleinitial;
	}

	@Column(name = "INSUREDAKALASTNAME", length = 5000)
	public String getInsuredakalastname() {
		return this.insuredakalastname;
	}

	public void setInsuredakalastname(String insuredakalastname) {
		this.insuredakalastname = insuredakalastname;
	}

	@Column(name = "INSUREDAKAFIRSTNAME", length = 5000)
	public String getInsuredakafirstname() {
		return this.insuredakafirstname;
	}

	public void setInsuredakafirstname(String insuredakafirstname) {
		this.insuredakafirstname = insuredakafirstname;
	}

	@Column(name = "INSUREDAKAMIDDLEINITIAL", length = 5000)
	public String getInsuredakamiddleinitial() {
		return this.insuredakamiddleinitial;
	}

	public void setInsuredakamiddleinitial(String insuredakamiddleinitial) {
		this.insuredakamiddleinitial = insuredakamiddleinitial;
	}

	@Column(name = "INSUREDSSN", length = 5000)
	public String getInsuredssn() {
		return this.insuredssn;
	}

	public void setInsuredssn(String insuredssn) {
		this.insuredssn = insuredssn;
	}

	@Column(name = "INSUREDDOB", length = 5000)
	public String getInsureddob() {
		return this.insureddob;
	}

	public void setInsureddob(String insureddob) {
		this.insureddob = insureddob;
	}

	@Column(name = "INSUREDGENDER", length = 5000)
	public String getInsuredgender() {
		return this.insuredgender;
	}

	public void setInsuredgender(String insuredgender) {
		this.insuredgender = insuredgender;
	}

	@Column(name = "SECONDINSUREDLASTNAME", length = 5000)
	public String getSecondinsuredlastname() {
		return this.secondinsuredlastname;
	}

	public void setSecondinsuredlastname(String secondinsuredlastname) {
		this.secondinsuredlastname = secondinsuredlastname;
	}

	@Column(name = "SECONDINSUREDFIRSTNAME", length = 5000)
	public String getSecondinsuredfirstname() {
		return this.secondinsuredfirstname;
	}

	public void setSecondinsuredfirstname(String secondinsuredfirstname) {
		this.secondinsuredfirstname = secondinsuredfirstname;
	}

	@Column(name = "SECONDINSUREDMIDDLEINITIAL", length = 5000)
	public String getSecondinsuredmiddleinitial() {
		return this.secondinsuredmiddleinitial;
	}

	public void setSecondinsuredmiddleinitial(String secondinsuredmiddleinitial) {
		this.secondinsuredmiddleinitial = secondinsuredmiddleinitial;
	}

	@Column(name = "SECONDINSUREDAKALASTNAME", length = 5000)
	public String getSecondinsuredakalastname() {
		return this.secondinsuredakalastname;
	}

	public void setSecondinsuredakalastname(String secondinsuredakalastname) {
		this.secondinsuredakalastname = secondinsuredakalastname;
	}

	@Column(name = "SECONDINSUREDAKAFIRSTNAME", length = 5000)
	public String getSecondinsuredakafirstname() {
		return this.secondinsuredakafirstname;
	}

	public void setSecondinsuredakafirstname(String secondinsuredakafirstname) {
		this.secondinsuredakafirstname = secondinsuredakafirstname;
	}

	@Column(name = "SECONDINSUREDAKAMIDDLEINITIAL", length = 5000)
	public String getSecondinsuredakamiddleinitial() {
		return this.secondinsuredakamiddleinitial;
	}

	public void setSecondinsuredakamiddleinitial(String secondinsuredakamiddleinitial) {
		this.secondinsuredakamiddleinitial = secondinsuredakamiddleinitial;
	}

	@Column(name = "SECONDINSUREDSSN", length = 5000)
	public String getSecondinsuredssn() {
		return this.secondinsuredssn;
	}

	public void setSecondinsuredssn(String secondinsuredssn) {
		this.secondinsuredssn = secondinsuredssn;
	}

	@Column(name = "SECONDINSUREDDOB", length = 5000)
	public String getSecondinsureddob() {
		return this.secondinsureddob;
	}

	public void setSecondinsureddob(String secondinsureddob) {
		this.secondinsureddob = secondinsureddob;
	}

	@Column(name = "SECONDINDUREDGENDER", length = 5000)
	public String getSecondinduredgender() {
		return this.secondinduredgender;
	}

	public void setSecondinduredgender(String secondinduredgender) {
		this.secondinduredgender = secondinduredgender;
	}

	@Column(name = "LOSSORACCIDENTLOCATIONSTREETNUMBER", length = 5000)
	public String getLossoraccidentlocationstreetnumber() {
		return this.lossoraccidentlocationstreetnumber;
	}

	public void setLossoraccidentlocationstreetnumber(String lossoraccidentlocationstreetnumber) {
		this.lossoraccidentlocationstreetnumber = lossoraccidentlocationstreetnumber;
	}

	@Column(name = "LOSSORACCIDENTLOCATIONSTREETNAME", length = 5000)
	public String getLossoraccidentlocationstreetname() {
		return this.lossoraccidentlocationstreetname;
	}

	public void setLossoraccidentlocationstreetname(String lossoraccidentlocationstreetname) {
		this.lossoraccidentlocationstreetname = lossoraccidentlocationstreetname;
	}

	@Column(name = "LOSSORACCIDENTLOCATIONSTREETTYPE", length = 5000)
	public String getLossoraccidentlocationstreettype() {
		return this.lossoraccidentlocationstreettype;
	}

	public void setLossoraccidentlocationstreettype(String lossoraccidentlocationstreettype) {
		this.lossoraccidentlocationstreettype = lossoraccidentlocationstreettype;
	}

	@Column(name = "LOSSORACCIDENTLOCATIONAPTNUMBRER", length = 5000)
	public String getLossoraccidentlocationaptnumbrer() {
		return this.lossoraccidentlocationaptnumbrer;
	}

	public void setLossoraccidentlocationaptnumbrer(String lossoraccidentlocationaptnumbrer) {
		this.lossoraccidentlocationaptnumbrer = lossoraccidentlocationaptnumbrer;
	}

	@Column(name = "LOSSORACCIDENTLOCATIONCITY", length = 5000)
	public String getLossoraccidentlocationcity() {
		return this.lossoraccidentlocationcity;
	}

	public void setLossoraccidentlocationcity(String lossoraccidentlocationcity) {
		this.lossoraccidentlocationcity = lossoraccidentlocationcity;
	}

	@Column(name = "LOSSORACCIDENTLOCATIONSTATE", length = 5000)
	public String getLossoraccidentlocationstate() {
		return this.lossoraccidentlocationstate;
	}

	public void setLossoraccidentlocationstate(String lossoraccidentlocationstate) {
		this.lossoraccidentlocationstate = lossoraccidentlocationstate;
	}

	@Column(name = "LOSSORACCIDENTLOCATIONZIPCODE", length = 5000)
	public String getLossoraccidentlocationzipcode() {
		return this.lossoraccidentlocationzipcode;
	}

	public void setLossoraccidentlocationzipcode(String lossoraccidentlocationzipcode) {
		this.lossoraccidentlocationzipcode = lossoraccidentlocationzipcode;
	}

	@Column(name = "LOSSORACCIDENTLOCATIONZIPCODEEXTENSION", length = 5000)
	public String getLossoraccidentlocationzipcodeextension() {
		return this.lossoraccidentlocationzipcodeextension;
	}

	public void setLossoraccidentlocationzipcodeextension(String lossoraccidentlocationzipcodeextension) {
		this.lossoraccidentlocationzipcodeextension = lossoraccidentlocationzipcodeextension;
	}

	@Column(name = "INSUREDSSTREETNUMBER", length = 5000)
	public String getInsuredsstreetnumber() {
		return this.insuredsstreetnumber;
	}

	public void setInsuredsstreetnumber(String insuredsstreetnumber) {
		this.insuredsstreetnumber = insuredsstreetnumber;
	}

	@Column(name = "INSUREDSSTREETNAME", length = 5000)
	public String getInsuredsstreetname() {
		return this.insuredsstreetname;
	}

	public void setInsuredsstreetname(String insuredsstreetname) {
		this.insuredsstreetname = insuredsstreetname;
	}

	@Column(name = "INSUREDSSTREETTYPE", length = 5000)
	public String getInsuredsstreettype() {
		return this.insuredsstreettype;
	}

	public void setInsuredsstreettype(String insuredsstreettype) {
		this.insuredsstreettype = insuredsstreettype;
	}

	@Column(name = "INSUREDSAPTNUMER", length = 5000)
	public String getInsuredsaptnumer() {
		return this.insuredsaptnumer;
	}

	public void setInsuredsaptnumer(String insuredsaptnumer) {
		this.insuredsaptnumer = insuredsaptnumer;
	}

	@Column(name = "INSUREDSCITY", length = 5000)
	public String getInsuredscity() {
		return this.insuredscity;
	}

	public void setInsuredscity(String insuredscity) {
		this.insuredscity = insuredscity;
	}

	@Column(name = "INSUREDSSTATE", length = 5000)
	public String getInsuredsstate() {
		return this.insuredsstate;
	}

	public void setInsuredsstate(String insuredsstate) {
		this.insuredsstate = insuredsstate;
	}

	@Column(name = "INSUREDSZIPCODE", length = 5000)
	public String getInsuredszipcode() {
		return this.insuredszipcode;
	}

	public void setInsuredszipcode(String insuredszipcode) {
		this.insuredszipcode = insuredszipcode;
	}

	@Column(name = "INSUREDSZIPCODEEXTENSION", length = 5000)
	public String getInsuredszipcodeextension() {
		return this.insuredszipcodeextension;
	}

	public void setInsuredszipcodeextension(String insuredszipcodeextension) {
		this.insuredszipcodeextension = insuredszipcodeextension;
	}

	@Column(name = "MORTGAGEE", length = 5000)
	public String getMortgagee() {
		return this.mortgagee;
	}

	public void setMortgagee(String mortgagee) {
		this.mortgagee = mortgagee;
	}

	@Column(name = "LOANNUMBER", length = 5000)
	public String getLoannumber() {
		return this.loannumber;
	}

	public void setLoannumber(String loannumber) {
		this.loannumber = loannumber;
	}

	@Column(name = "POLICYTYPE", length = 5000)
	public String getPolicytype() {
		return this.policytype;
	}

	public void setPolicytype(String policytype) {
		this.policytype = policytype;
	}

	@Column(name = "POLICYNUMBER", length = 5000)
	public String getPolicynumber() {
		return this.policynumber;
	}

	public void setPolicynumber(String policynumber) {
		this.policynumber = policynumber;
	}

	@Column(name = "ACCOUNTNUMBER", length = 5000)
	public String getAccountnumber() {
		return this.accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	@Column(name = "LOSSACCIDENTDATE", length = 5000)
	public String getLossaccidentdate() {
		return this.lossaccidentdate;
	}

	public void setLossaccidentdate(String lossaccidentdate) {
		this.lossaccidentdate = lossaccidentdate;
	}

	@Column(name = "CLAIMAMOUNT", length = 5000)
	public String getClaimamount() {
		return this.claimamount;
	}

	public void setClaimamount(String claimamount) {
		this.claimamount = claimamount;
	}

	@Column(name = "CLAIMTYPE", length = 5000)
	public String getClaimtype() {
		return this.claimtype;
	}

	public void setClaimtype(String claimtype) {
		this.claimtype = claimtype;
	}

	@Column(name = "CASEFILENUMBERCLAIM", length = 5000)
	public String getCasefilenumberclaim() {
		return this.casefilenumberclaim;
	}

	public void setCasefilenumberclaim(String casefilenumberclaim) {
		this.casefilenumberclaim = casefilenumberclaim;
	}

	@Column(name = "CATASTROPHENUMBER", length = 5000)
	public String getCatastrophenumber() {
		return this.catastrophenumber;
	}

	public void setCatastrophenumber(String catastrophenumber) {
		this.catastrophenumber = catastrophenumber;
	}

	@Column(name = "DESCRIPTIONOFINJURYCAUSEOFLOSS", length = 5000)
	public String getDescriptionofinjurycauseofloss() {
		return this.descriptionofinjurycauseofloss;
	}

	public void setDescriptionofinjurycauseofloss(String descriptionofinjurycauseofloss) {
		this.descriptionofinjurycauseofloss = descriptionofinjurycauseofloss;
	}

	@Column(name = "CLAIMANTLASTNAME", length = 5000)
	public String getClaimantlastname() {
		return this.claimantlastname;
	}

	public void setClaimantlastname(String claimantlastname) {
		this.claimantlastname = claimantlastname;
	}

	@Column(name = "CLAIMANTFIRSTNAME", length = 5000)
	public String getClaimantfirstname() {
		return this.claimantfirstname;
	}

	public void setClaimantfirstname(String claimantfirstname) {
		this.claimantfirstname = claimantfirstname;
	}

	@Column(name = "CLAIMANTMIDDLENAME", length = 5000)
	public String getClaimantmiddlename() {
		return this.claimantmiddlename;
	}

	public void setClaimantmiddlename(String claimantmiddlename) {
		this.claimantmiddlename = claimantmiddlename;
	}

	@Column(name = "CLAIMANTSTREETNUMBER", length = 5000)
	public String getClaimantstreetnumber() {
		return this.claimantstreetnumber;
	}

	public void setClaimantstreetnumber(String claimantstreetnumber) {
		this.claimantstreetnumber = claimantstreetnumber;
	}

	@Column(name = "CLAIMANTSTREENAME", length = 5000)
	public String getClaimantstreename() {
		return this.claimantstreename;
	}

	public void setClaimantstreename(String claimantstreename) {
		this.claimantstreename = claimantstreename;
	}

	@Column(name = "CLAIMANTSTREETTYPE", length = 5000)
	public String getClaimantstreettype() {
		return this.claimantstreettype;
	}

	public void setClaimantstreettype(String claimantstreettype) {
		this.claimantstreettype = claimantstreettype;
	}

	@Column(name = "CLAIMANTAPTNUMBER", length = 5000)
	public String getClaimantaptnumber() {
		return this.claimantaptnumber;
	}

	public void setClaimantaptnumber(String claimantaptnumber) {
		this.claimantaptnumber = claimantaptnumber;
	}

	@Column(name = "CLAIMANTCITY", length = 5000)
	public String getClaimantcity() {
		return this.claimantcity;
	}

	public void setClaimantcity(String claimantcity) {
		this.claimantcity = claimantcity;
	}

	@Column(name = "CLAIMANTSTATE", length = 5000)
	public String getClaimantstate() {
		return this.claimantstate;
	}

	public void setClaimantstate(String claimantstate) {
		this.claimantstate = claimantstate;
	}

	@Column(name = "CLAIMANTZIPCODE", length = 5000)
	public String getClaimantzipcode() {
		return this.claimantzipcode;
	}

	public void setClaimantzipcode(String claimantzipcode) {
		this.claimantzipcode = claimantzipcode;
	}

	@Column(name = "CLAIMANTZIPCODEEXTENSION", length = 5000)
	public String getClaimantzipcodeextension() {
		return this.claimantzipcodeextension;
	}

	public void setClaimantzipcodeextension(String claimantzipcodeextension) {
		this.claimantzipcodeextension = claimantzipcodeextension;
	}

	@Column(name = "CLAIMANTSSN", length = 5000)
	public String getClaimantssn() {
		return this.claimantssn;
	}

	public void setClaimantssn(String claimantssn) {
		this.claimantssn = claimantssn;
	}

	@Column(name = "CLAIMANTDOB", length = 5000)
	public String getClaimantdob() {
		return this.claimantdob;
	}

	public void setClaimantdob(String claimantdob) {
		this.claimantdob = claimantdob;
	}

	@Column(name = "CLAIMANTGENDER", length = 5000)
	public String getClaimantgender() {
		return this.claimantgender;
	}

	public void setClaimantgender(String claimantgender) {
		this.claimantgender = claimantgender;
	}

	@Column(name = "FILLERREMARKS", length = 5000)
	public String getFillerremarks() {
		return this.fillerremarks;
	}

	public void setFillerremarks(String fillerremarks) {
		this.fillerremarks = fillerremarks;
	}

	@Column(name = "CLAIMSTATUS", length = 5000)
	public String getClaimstatus() {
		return this.claimstatus;
	}

	public void setClaimstatus(String claimstatus) {
		this.claimstatus = claimstatus;
	}

	@Column(name = "REPORTINGSTATUSUPDATEFLAGS", length = 5000)
	public String getReportingstatusupdateflags() {
		return this.reportingstatusupdateflags;
	}

	public void setReportingstatusupdateflags(String reportingstatusupdateflags) {
		this.reportingstatusupdateflags = reportingstatusupdateflags;
	}

	@Column(name = "AMBESTNUMBER", length = 5000)
	public String getAmBestnumber() {
		return this.amBestnumber;
	}

	public void setAmBestnumber(String amBestnumber) {
		this.amBestnumber = amBestnumber;
	}

	@Column(name = "DOMESTICVIOLENCEINDICATOR", length = 5000)
	public String getDomesticviolenceindicator() {
		return this.domesticviolenceindicator;
	}

	public void setDomesticviolenceindicator(String domesticviolenceindicator) {
		this.domesticviolenceindicator = domesticviolenceindicator;
	}

	@Column(name = "DOMESTICVIOLENCESTATECODE", length = 5000)
	public String getDomesticviolencestatecode() {
		return this.domesticviolencestatecode;
	}

	public void setDomesticviolencestatecode(String domesticviolencestatecode) {
		this.domesticviolencestatecode = domesticviolencestatecode;
	}

	@Column(name = "FILLERREMARKS2", length = 5000)
	public String getFillerremarks2() {
		return this.fillerremarks2;
	}

	public void setFillerremarks2(String fillerremarks2) {
		this.fillerremarks2 = fillerremarks2;
	}
	
	
	
	@Transient
	public static FarmBureauPropertyMaster0917 getRandomVeriskContact() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<FarmBureauPropertyMaster0917> veriskCriteria = builder.createQuery(FarmBureauPropertyMaster0917.class);
			Root<FarmBureauPropertyMaster0917> underwriterCriteriaRoot = veriskCriteria.from(FarmBureauPropertyMaster0917.class);
			veriskCriteria.select(underwriterCriteriaRoot);
			
			List<FarmBureauPropertyMaster0917> results = session.createQuery(veriskCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			FarmBureauPropertyMaster0917 verisk = results.get(index);
			
			session.getTransaction().commit();
			
			return verisk;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}




		
	}




	@Transient
	public static 	List<FarmBureauPropertyMaster0917> getVeriskReportWithFirstAndLastName(String insFirstName , String insLastName) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
		try {
			sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<FarmBureauPropertyMaster0917> veriskCriteria = builder.createQuery(FarmBureauPropertyMaster0917.class);
			Root<FarmBureauPropertyMaster0917> veriskCriteriaRoot = veriskCriteria.from(FarmBureauPropertyMaster0917.class);


			Expression<String> firstName = veriskCriteriaRoot.get("insuredfirstname");
			Expression<String> lastName = veriskCriteriaRoot.get("insuredlastname");
			Predicate agentFirstNameLike = builder.like(firstName, "%" + insFirstName + "%");
			Predicate agentLastNameLike = builder.like(lastName, "%" + insLastName + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(agentFirstNameLike);
			predicates.add(agentLastNameLike);


			veriskCriteria.select(veriskCriteriaRoot).where(predicates.toArray(new Predicate[]{}));;
     		List<FarmBureauPropertyMaster0917> results = session.createQuery(veriskCriteria).getResultList();
			session.getTransaction().commit();

			return results;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
			pf.close();
		}
	}
	
	
	@Transient
	public static List<FarmBureauPropertyMaster0917> getAPlusAutoTestCase() throws Exception {
		FarmBureauPropertyMaster0917 randomTestCase = getRandomVeriskContact();
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
	
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<FarmBureauPropertyMaster0917> agentCriteria = builder.createQuery(FarmBureauPropertyMaster0917.class);
			Root<FarmBureauPropertyMaster0917> agentCriteriaRoot = agentCriteria.from(FarmBureauPropertyMaster0917.class);
			Expression<String> agentRegion = agentCriteriaRoot.get("testCaseNumber");
			Predicate agentRegionLike = builder.like(agentRegion,  randomTestCase.getTestCaseNumber() );
			agentCriteria.select(agentCriteriaRoot).where(agentRegionLike);
	
			List<FarmBureauPropertyMaster0917> results = session.createQuery(agentCriteria).getResultList();
	
            session.getTransaction().commit();
	
            return results;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	
	
	
	
	
	
	
	
	
	

}