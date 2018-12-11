package repository.gw.generate.custom;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import persistence.globaldatarepo.entities.InsuranceScoreTestCases;
import persistence.globaldatarepo.entities.Names;
import persistence.globaldatarepo.entities.VeriskMvr;
import persistence.globaldatarepo.helpers.NamesHelper;
import repository.gw.enums.*;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Contact {
	
//	private boolean hasChanged = false;

	private boolean isNamedInsured = false;
	private repository.gw.enums.PersonSuffix insSuffixName = repository.gw.enums.PersonSuffix.None;
	private String firstName = "First";
	private String middleName = null;
	private String lastName = "Last-" + repository.gw.helpers.DateUtils.dateFormatAsString("yyMMddHHmmss", new Date()) + repository.gw.helpers.StringsUtils.generateRandomNumberDigits(4);
	private String fullName =""; // Ian created this to gather the string from the assigned driver edit screen. It's intent is to be used for anything from first and last to a "Sr. John Hamilton the Archiduke of Kentucky".
	// added company stuff because we had to... very weird I know... but deal with it -Cor
	private String companyName = "Company-" + repository.gw.helpers.DateUtils.dateFormatAsString("yyMMddHHmmss", new Date()) + repository.gw.helpers.StringsUtils.generateRandomNumberDigits(4);
	
	private repository.gw.enums.ContactSubType personOrCompany = repository.gw.enums.ContactSubType.Person;
	private ArrayList<repository.gw.enums.LineSelection> additionalInsuredLines = new ArrayList<repository.gw.enums.LineSelection>();
	
	private String socialSecurityNumber = null;
	private String taxIDNumber = null;
	private String alternateID = null;
	
	private Gender gender = Gender.Female;
	private Date dob = repository.gw.helpers.DateUtils.dateAddSubtract(new Date(), repository.gw.enums.DateAddSubtractOptions.Year, -35);
	private repository.gw.enums.MaritalStatus maritalStatus = repository.gw.enums.MaritalStatus.Single;
	private String driversLicenseNum = "DA" + StringsUtils.generateRandomNumberDigits(6) + "C";
	private State stateLicenced = State.Idaho;
	private Date licenseYear = repository.gw.helpers.DateUtils.dateAddSubtract(new Date(), repository.gw.enums.DateAddSubtractOptions.Year, -20);
	private int age = 35; 
	private Date hireDate = new Date();
	private boolean excludedDriver = false;
	private int driverNumber = -1;
	private repository.gw.enums.DrivingExp drivingExp = repository.gw.enums.DrivingExp.MoreThan2Years;
	private repository.gw.enums.DriverType driverType = repository.gw.enums.DriverType.Primary;
	private String primaryVehicleDriven = "8675309";
	private repository.gw.enums.VehicleUse vehicleUse = repository.gw.enums.VehicleUse.NotDriventoWorkorSchool;
	private RelationshipsAB relationshipAB = RelationshipsAB.Spouse;
	private repository.gw.enums.RelationshipToInsuredPolicyMember relationPolicyMember;
	private repository.gw.enums.RelationshipToInsured relationToInsured = repository.gw.enums.RelationshipToInsured.SignificantOther;
	private repository.gw.enums.RelationshipToInsuredCPP relationshipToInsuredCPP = repository.gw.enums.RelationshipToInsuredCPP.SignificantOther;
	private repository.gw.enums.CommuteType commuteType = repository.gw.enums.CommuteType.WorkFromHome;
	private String occupation = "Lazy Person";
	private boolean fromPrefill = false;
	private boolean isInsuredAlready = false;
	private boolean hasPhysicalImpairmentOrEpilepsy = false;
	private boolean currentInsurance = true;
	private String currentInsuranceCompPolicy = "Farm Bureau Policy #: 1B124576";
	private repository.gw.enums.HowLongWithoutCoverage withOutCoverage = repository.gw.enums.HowLongWithoutCoverage.NewDriver;
	private ArrayList<repository.gw.enums.ContactRole> roles = new ArrayList<repository.gw.enums.ContactRole>();
	private boolean isDupSSN = false;
	//Membership and SR22 Stuff
	private boolean hasSR22Charges = false;
	private double sr22Charge = 0;
	private boolean contactIsPNI = false;
	private repository.gw.enums.ContactRelationshipToMember contactRelationshipToPNI = repository.gw.enums.ContactRelationshipToMember.None;
	private boolean hasMembershipDues = false;
	private double membershipDuesAmount = 0;
	private boolean membershipDuesAreLienPaid = false;
	private repository.gw.generate.custom.AdditionalInterest membershipDuesLienHolder = null;
	private CountyIdaho membershipDuesCounty = null;
	private repository.gw.enums.MembershipCurrentMemberDuesStatus membershipDuesCurrentChargeStatus = repository.gw.enums.MembershipCurrentMemberDuesStatus.Charged;
	private repository.gw.enums.MembershipRenewalMemberDuesStatus membershipDuesRenewalChargeStatus = repository.gw.enums.MembershipRenewalMemberDuesStatus.No_Change;
	
	private AddressInfo address = new AddressInfo(true);
	private List<AddressInfo> addressList = new ArrayList<AddressInfo>();

	private repository.gw.enums.InsuranceScore insuranceScore = repository.gw.enums.InsuranceScore.NeedsOrdered;
	public boolean isVeriskTestData = false;
    public boolean isSpecial = false;
    private boolean orderMVR = true;
    private UnderwriterIssues_PL assignedUWIssue = null;
    
    private List<String> dbaList = new ArrayList<String>();
    private List<VeriskMvr> veriskMVRReport = new ArrayList<VeriskMvr>();
    
	//////////////////////////////////
	//// CONSTRUCTORS ////////
	//////////////////////////////////

	public List<VeriskMvr> getVeriskMVRReport() {
		return veriskMVRReport;
	}

	public void setVeriskMVRReport(List<VeriskMvr> veriskMVRReport) {
		this.veriskMVRReport = veriskMVRReport;
	}

	public List<String> getDbaList() {
		return dbaList;
	}

	public void setDbaList(List<String> dbaList) {
		this.dbaList = dbaList;
	}

	public Contact() {
		try {
			Names myNameIs = NamesHelper.getRandomName();
			this.firstName = myNameIs.getFirstName();
			this.lastName = myNameIs.getLastName();
		} catch (Exception e) {
		}
		
		construct();
		this.addressList.add(this.address);
	}
	
	
	public Contact(List<VeriskMvr> mvrList) {
		String[] split = mvrList.get(0).getLastNameFirstName().split(",");
		this.firstName = split[1].trim();
		this.lastName = split[0].trim();
		this.driversLicenseNum = mvrList.get(0).getDriverLicense();
		this.stateLicenced = State.valueOfAbbreviation(mvrList.get(0).getState());
		for(VeriskMvr mvr : mvrList) {
			this.veriskMVRReport.add(mvr);
		}
		this.isVeriskTestData = true;
	}
	
	
	
	
	public Contact(InsuranceScoreTestCases testCase) throws ParseException {
		this.firstName = testCase.getFirstName();
		this.middleName = testCase.getMiddleName();
		this.lastName = testCase.getLastName();
		this.socialSecurityNumber = testCase.getSsn();
		if(!testCase.getDobasof102017().isEmpty()) {
			this.dob = repository.gw.helpers.DateUtils.convertStringtoDate(testCase.getDobasof102017(), "yyyyMMdd");
		}
		this.address = new AddressInfo(testCase.getaddressLine1(), testCase.getCity(), State.valueOfAbbreviation(testCase.getState()), testCase.getZipCode());
		this.addressList.clear();
		this.addressList.add(this.address);
	}
	
	public Contact(Contact contact) {
		this.isNamedInsured = contact.isNamedInsured;
		this.insSuffixName = contact.insSuffixName;
		this.firstName = contact.firstName;
		this.middleName = contact.middleName;
		this.lastName = contact.lastName;
		this.fullName = contact.fullName;
		this.companyName = contact.companyName;
		this.personOrCompany = contact.personOrCompany;
		this.additionalInsuredLines = contact.additionalInsuredLines;
		this.socialSecurityNumber = contact.socialSecurityNumber;
		this.taxIDNumber = contact.taxIDNumber;
		this.alternateID = contact.alternateID;
		this.gender = contact.gender;
		this.dob = contact.dob;
		this.maritalStatus = contact.maritalStatus;
		this.driversLicenseNum = contact.driversLicenseNum;
		this.stateLicenced = contact.stateLicenced;
		this.licenseYear = contact.licenseYear;
		this.age = contact.age;
		this.hireDate = contact.hireDate;
		this.excludedDriver = contact.excludedDriver;
		this.drivingExp = contact.drivingExp;
		this.driverType = contact.driverType;
		this.primaryVehicleDriven = contact.primaryVehicleDriven;
		this.vehicleUse = contact.vehicleUse;
		this.relationPolicyMember = contact.relationPolicyMember;
		this.relationToInsured = contact.relationToInsured;
		this.relationshipToInsuredCPP = contact.relationshipToInsuredCPP;
		this.commuteType = contact.commuteType;
		this.occupation = contact.occupation;
		this.fromPrefill = contact.fromPrefill;
		this.isInsuredAlready = contact.isInsuredAlready;
		this.hasPhysicalImpairmentOrEpilepsy = contact.hasPhysicalImpairmentOrEpilepsy;
		this.currentInsurance = contact.currentInsurance;
		this.currentInsuranceCompPolicy = contact.currentInsuranceCompPolicy;
		this.withOutCoverage = contact.withOutCoverage;
		this.roles = contact.roles;
		this.hasSR22Charges = contact.hasSR22Charges;
		this.sr22Charge = contact.sr22Charge;
		this.contactIsPNI = contact.contactIsPNI;
		this.contactRelationshipToPNI = contact.contactRelationshipToPNI;
		this.hasMembershipDues = contact.hasMembershipDues;
		this.membershipDuesAmount = contact.membershipDuesAmount;
		this.membershipDuesAreLienPaid = contact.membershipDuesAreLienPaid;
		this.membershipDuesLienHolder = contact.membershipDuesLienHolder;
		this.membershipDuesCounty = contact.membershipDuesCounty;
		this.membershipDuesCurrentChargeStatus = contact.membershipDuesCurrentChargeStatus;
		this.membershipDuesRenewalChargeStatus = contact.membershipDuesRenewalChargeStatus;
		this.address = contact.address;
		this.addressList = contact.addressList;
		this.insuranceScore = contact.insuranceScore;
		this.isSpecial = contact.isSpecial;
		this.randomWords = contact.randomWords;
	}

	public Contact(boolean isPNIContact) {
		try {
			Names myNameIs = NamesHelper.getRandomName();
			this.firstName = myNameIs.getFirstName();
			this.lastName = myNameIs.getLastName();
		} catch (Exception e) {
		}
		
		construct();
		this.contactIsPNI = isPNIContact;
		if (isPNIContact) {
			this.hasMembershipDues = true;
		}
	}
	
	public Contact(PolicyInfoAdditionalNamedInsured aniToCopy) {
		this.personOrCompany = aniToCopy.getCompanyOrPerson();
		this.companyName = aniToCopy.getCompanyName();
		this.firstName = aniToCopy.getPersonFirstName();
		this.middleName = aniToCopy.getPersonMiddleName();
		this.lastName = aniToCopy.getPersonLastName();
		this.address = aniToCopy.getAddress();
		this.addressList = aniToCopy.getAddressList();
		this.dob = aniToCopy.getAniDOB();
		this.hasMembershipDues = aniToCopy.hasMembershipDues();
		this.membershipDuesAmount = aniToCopy.getMembershipDuesAmount();
		this.membershipDuesAreLienPaid = aniToCopy.isMembershipDuesAreLienPaid();
		this.membershipDuesLienHolder = aniToCopy.getMembershipDuesLienHolder();
		
		switch (aniToCopy.getRelationshipToPNI()) {
			case Spouse:
				this.contactRelationshipToPNI = repository.gw.enums.ContactRelationshipToMember.Spouse;
				break;
			case ChildWard:
				this.contactRelationshipToPNI = repository.gw.enums.ContactRelationshipToMember.Child_Ward;
				break;
			case ParentGuardian:
				this.contactRelationshipToPNI = repository.gw.enums.ContactRelationshipToMember.Parent_Guardian;
				break;
			default:
				break;
		}
	}

	public Contact(String firstName, String lastName, Gender gender, Date dob) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dob = dob;
		construct();
	}
	
	public Contact(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.personOrCompany = repository.gw.enums.ContactSubType.Person;
	}
	
	public void construct() {
		// Add random words
		this.randomWords.add("sausage");
		this.randomWords.add("blubber");
		this.randomWords.add("pencil");
		this.randomWords.add("cloud");
		this.randomWords.add("moon");
		this.randomWords.add("water");
		this.randomWords.add("computer");
		this.randomWords.add("school");
		this.randomWords.add("network");
		this.randomWords.add("hammer");
		this.randomWords.add("walking");
		this.randomWords.add("violently");
		this.randomWords.add("mediocre");
		this.randomWords.add("literature");
		this.randomWords.add("chair");
		this.randomWords.add("two");
		this.randomWords.add("window");
		this.randomWords.add("cords");
		this.randomWords.add("musical");
		this.randomWords.add("zebra");
		this.randomWords.add("xylophone");
		this.randomWords.add("penguin");
		this.randomWords.add("home");
		this.randomWords.add("dog");
		this.randomWords.add("final");
		this.randomWords.add("ink");
		this.randomWords.add("teacher");
		this.randomWords.add("fun");
		this.randomWords.add("website");
		this.randomWords.add("banana");
		this.randomWords.add("uncle");
		this.randomWords.add("softly");
		this.randomWords.add("mega");
		this.randomWords.add("ten");
		this.randomWords.add("awesome");
		this.randomWords.add("attatch");
		this.randomWords.add("blue");
		this.randomWords.add("internet");
		this.randomWords.add("bottle");
		this.randomWords.add("tight");
		this.randomWords.add("zone");
		this.randomWords.add("tomato");
		this.randomWords.add("prison");
		this.randomWords.add("hydro");
		this.randomWords.add("cleaning");
		this.randomWords.add("telivision");
		this.randomWords.add("send");
		this.randomWords.add("frog");
		this.randomWords.add("cup");
		this.randomWords.add("book");
		this.randomWords.add("zooming");
		this.randomWords.add("falling");
		this.randomWords.add("evily");
		this.randomWords.add("gamer");
		this.randomWords.add("lid");
		this.randomWords.add("juice");
		this.randomWords.add("moniter");
		this.randomWords.add("captain");
		this.randomWords.add("bonding");
		this.randomWords.add("loudly");
		this.randomWords.add("thudding");
		this.randomWords.add("guitar");
		this.randomWords.add("shaving");
		this.randomWords.add("hair");
		this.randomWords.add("soccer");
		this.randomWords.add("water");
		this.randomWords.add("racket");
		this.randomWords.add("table");
		this.randomWords.add("late");
		this.randomWords.add("media");
		this.randomWords.add("desktop");
		this.randomWords.add("flipper");
		this.randomWords.add("club");
		this.randomWords.add("flying");
		this.randomWords.add("smooth");
		this.randomWords.add("monster");
		this.randomWords.add("purple");
		this.randomWords.add("guardian");
		this.randomWords.add("bold");
		this.randomWords.add("hyperlink");
		this.randomWords.add("presentation");
		this.randomWords.add("world");
		this.randomWords.add("national");
		this.randomWords.add("comment");
		this.randomWords.add("element");
		this.randomWords.add("magic");
		this.randomWords.add("lion");
		this.randomWords.add("sand");
		this.randomWords.add("crust");
		this.randomWords.add("toast");
		this.randomWords.add("jam");
		this.randomWords.add("hunter");
		this.randomWords.add("forest");
		this.randomWords.add("foraging");
		this.randomWords.add("silently");
		this.randomWords.add("tawesomated");
		this.randomWords.add("joshing");
		this.randomWords.add("pong");
	}

	//////////////////////////////////
	//// GETTERS AND SETTERS ////////
	//////////////////////////////////
	
	public String getFullName() {
		if(fullName.isEmpty()) {
			if(middleName == null || middleName.isEmpty()) {
				return firstName + " " + lastName;
			} else {
				return firstName + " " + middleName + " " + lastName;
			}
		}
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

    public void setDob(WebDriver driver, Date dob) {
		this.dob = dob;
		try {
            this.age = repository.gw.helpers.DateUtils.getDifferenceBetweenDates(dob, repository.gw.helpers.DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateDifferenceOptions.Year);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public repository.gw.enums.MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getDriversLicenseNum() {
		return driversLicenseNum;
	}

	public void setDriversLicenseNum(String driversLicenseNum) {
		this.driversLicenseNum = driversLicenseNum;
	}

	public State getStateLicenced() {
		return stateLicenced;
	}

	public void setStateLicenced(State stateLicenced) {
		this.stateLicenced = stateLicenced;
	}

	public Date getLicenseYear() {
		return licenseYear;
	}

	public void setLicenseYear(Date licenseYear) {
		this.licenseYear = licenseYear;
	}

	public int getAge() {
		return age;
	}

    public void setAge(WebDriver driver, int age) {
		this.age = age;
		try {
            Date currentSystemDate = repository.gw.helpers.DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
			this.dob = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Year, -age);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public boolean isExcludedDriver() {
		return excludedDriver;
	}

	public void setExcludedDriver(boolean yesNo) {
		this.excludedDriver = yesNo;
	}
	
	public repository.gw.enums.DrivingExp getDrivingExp() {
		return drivingExp;
	}

	public void setDrivingExp(DrivingExp drivingExp) {
		this.drivingExp = drivingExp;
	}

	public String getPrimaryVehicleDriven() {
		return primaryVehicleDriven;
	}
	
	public void setPrimaryVehicleDriven(String primaryVehicleDriven) {
		this.primaryVehicleDriven = primaryVehicleDriven;
	}

	public repository.gw.enums.DriverType getDriverType() {
		return driverType;
	}

	public void setDriverType(DriverType driverType) {
		this.driverType = driverType;
	}

	public repository.gw.enums.VehicleUse getVehicleUse() {
		return vehicleUse;
	}

	public void setVehicleUse(VehicleUse vehicleUse) {
		this.vehicleUse = vehicleUse;
	}

	public repository.gw.enums.RelationshipToInsured getRelationToInsured() {
		return relationToInsured;
	}

	public void setRelationToInsured(RelationshipToInsured relationToInsured) {
		this.relationToInsured = relationToInsured;
	}

	public repository.gw.enums.CommuteType getCommuteType() {
		return commuteType;
	}

	public void setCommuteType(CommuteType commuteType) {
		this.commuteType = commuteType;
	}

	public AddressInfo getAddress() {
		return address;
	}

	public void setAddress(AddressInfo address) {
		this.address = address;
		this.addressList.add(address);
	}
	
	public List<AddressInfo> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<AddressInfo> addressList) {
		this.addressList = addressList;
	}
	
	public AddressInfo getAddressByType(AddressType addressType) {
        AddressInfo addressToReturn = null;
        for (AddressInfo addressTemp : this.addressList) {
            if (addressTemp.getType().equals(addressType)) {
                addressToReturn = addressTemp;
                break;
            }
        }
        if (addressToReturn == null) {
            Assert.fail("There was no address found containing the address type " + addressType.getValue() + " . Please investigate.");
        }
        return addressToReturn;
    }
    
    public AddressInfo getAddressByKeyword(String addressOptionalKeyword) {
        AddressInfo addressToReturn = null;
        for (AddressInfo addressTemp : this.addressList) {
            if (addressTemp.getOptionalKeyword().equals(addressOptionalKeyword)) {
                addressToReturn = addressTemp;
                break;
            }
        }
        if (addressToReturn == null) {
            Assert.fail("There was no address found containing the Keyword " + addressOptionalKeyword + " . Please investigate.");
        }
        return addressToReturn;
    }

	public void setOccupation(String job) {
		this.occupation = job;
	}

	public String getOccupation() {
		return occupation;
	}
	
	public repository.gw.enums.RelationshipToInsuredPolicyMember getRelationPolicyMember(){
		return this.relationPolicyMember;
	}
	
	public void setRelationPolicyMember(RelationshipToInsuredPolicyMember relation){
		this.relationPolicyMember = relation;
	}

	public List<String> getRandomWords() {
		return randomWords;
	}

	public void setRandomWords(List<String> randomWords) {
		this.randomWords = randomWords;
	}

	private List<String> randomWords = new ArrayList<String>();

	public repository.gw.enums.RelationshipToInsuredCPP getRelationshipToInsuredCPP() {
		return relationshipToInsuredCPP;
	}

	public void setRelationshipToInsuredCPP(
			RelationshipToInsuredCPP relationshipToInsuredCPP) {
		this.relationshipToInsuredCPP = relationshipToInsuredCPP;
	}

	public boolean isFromPrefill() {
		return fromPrefill;
	}

	public void setFromPrefill(boolean fromPrefill) {
		this.fromPrefill = fromPrefill;
	}

	public boolean isInsuredAlready() {
		return isInsuredAlready;
	}

	public void setInsuredAlready(boolean isInsuredAlready) {
		this.isInsuredAlready = isInsuredAlready;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public repository.gw.enums.ContactSubType getPersonOrCompany() {
		return personOrCompany;
	}

	public void setPersonOrCompany(repository.gw.enums.ContactSubType personOrCompany) {
		this.personOrCompany = personOrCompany;
	}

	public ArrayList<repository.gw.enums.LineSelection> isAdditionalInsured() {
		return additionalInsuredLines;
	}

	public void setAdditionalInsured(ArrayList<LineSelection> additionalInsuredLines) {
		this.additionalInsuredLines = additionalInsuredLines;
	}

	public boolean isNamedInsured() {
		return isNamedInsured;
	}

	public void setNamedInsured(boolean isNamedInsured) {
		this.isNamedInsured = isNamedInsured;
	}

	public boolean isHasPhysicalImpairmentOrEpilepsy() {
		return hasPhysicalImpairmentOrEpilepsy;
	}

	public void setHasPhysicalImpairmentOrEpilepsy(boolean hasPhysicalImpairmentOrEpilepsy) {
		this.hasPhysicalImpairmentOrEpilepsy = hasPhysicalImpairmentOrEpilepsy;
	}
	
	public boolean hasCurrentInsurance() {
		return currentInsurance;
	}

	public void setCurrentInsurance(boolean currentInsurance) {
		this.currentInsurance = currentInsurance;
	}
	
	public String getCurrentInsuranceCompPolicy(){
		return this.currentInsuranceCompPolicy;
	}
	
	public void setCurrentInsuranceCompPolicy(String compPolicyNum){
		this.currentInsuranceCompPolicy = compPolicyNum;
	}
	
	public repository.gw.enums.HowLongWithoutCoverage getHowLongWithoutCoverageType(){
		return this.withOutCoverage;
	}
	
	public void setHowLongWithoutCoverageType(HowLongWithoutCoverage howlong){
		this.withOutCoverage = howlong;
	}
	
	public boolean hasSR22Charges() {
		return hasSR22Charges;
	}

	public void setSR22Charges(boolean hasSR22Charges) {
		this.hasSR22Charges = hasSR22Charges;
	}

	public double getSr22Charge() {
		return sr22Charge;
	}

	public void setSr22Charge(double sr22Charge) {
		this.sr22Charge = sr22Charge;
	}
	
	public boolean isContactIsPNI() {
		return contactIsPNI;
	}

	public void setContactIsPNI(boolean contactIsPNI) {
		this.contactIsPNI = contactIsPNI;
	}

	public repository.gw.enums.ContactRelationshipToMember getContactRelationshipToPNI() {
		return contactRelationshipToPNI;
	}

	public void setContactRelationshipToPNI(ContactRelationshipToMember contactRelationshipToPNI) {
		this.contactRelationshipToPNI = contactRelationshipToPNI;
	}

	public boolean hasMembershipDues() {
		return hasMembershipDues;
	}

	public void setHasMembershipDues(boolean hasMembershipDues) {
		this.hasMembershipDues = hasMembershipDues;
	}

	public double getMembershipDuesAmount() {
		return membershipDuesAmount;
	}

	public void setMembershipDuesAmount(double membershipDuesAmount) {
		this.membershipDuesAmount = membershipDuesAmount;
	}
	
	public boolean isMembershipDuesAreLienPaid() {
		return membershipDuesAreLienPaid;
	}

	public void setMembershipDuesAreLienPaid(boolean membershipDuesAreLienPaid) {
		this.membershipDuesAreLienPaid = membershipDuesAreLienPaid;
	}
	
	public repository.gw.generate.custom.AdditionalInterest getMembershipDuesLienHolder() {
		return membershipDuesLienHolder;
	}

	public void setMembershipDuesLienHolder(AdditionalInterest membershipDuesLienHolder) {
		this.membershipDuesLienHolder = membershipDuesLienHolder;
	}
	
	public CountyIdaho getMembershipDuesCounty() {
		return membershipDuesCounty;
	}

	public void setMembershipDuesCounty(CountyIdaho membershipDuesCounty) {
		this.membershipDuesCounty = membershipDuesCounty;
	}

	public repository.gw.enums.MembershipCurrentMemberDuesStatus getMembershipDuesCurrentChargeStatus() {
		return membershipDuesCurrentChargeStatus;
	}

	public void setMembershipDuesCurrentChargeStatus(MembershipCurrentMemberDuesStatus membershipDuesCurrentChargeStatus) {
		this.membershipDuesCurrentChargeStatus = membershipDuesCurrentChargeStatus;
	}

	public repository.gw.enums.MembershipRenewalMemberDuesStatus getMembershipDuesRenewalChargeStatus() {
		return membershipDuesRenewalChargeStatus;
	}

	public void setMembershipDuesRenewalChargeStatus(MembershipRenewalMemberDuesStatus membershipDuesRenewalChargeStatus) {
		this.membershipDuesRenewalChargeStatus = membershipDuesRenewalChargeStatus;
	}

//	public boolean hasChanged() {
//		return hasChanged;
//	}
//
//	public void setHasChanged(boolean hasChanged) {
//		this.hasChanged = hasChanged;
//	}

	/**
	 * @author iclouser 
	 * @description removes all default values of person class. So it's essentially an empty person object. Primary Use for portals but can be used for everyone. 
	 * Uses java reflection.
	 */
	public void removeDefaultValues(){
		Field[]personFields =Contact.class.getDeclaredFields();
		for(Field field: personFields){
			try {
                if (field.getType().getCanonicalName() == "boolean") {
	                field.set(this, false);
                } else if (field.getType().getCanonicalName() == "char") {
	                field.set(this, '\u0000');
                } else if ((field.getType().isPrimitive())) {
	                field.set(this, 0);
                } else {
	                field.set(this, null);
	            }
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
	}

	public repository.gw.enums.PersonSuffix getInsSuffixName() {
		return insSuffixName;
	}

	public void setInsSuffixName(PersonSuffix insSuffixName) {
		this.insSuffixName = insSuffixName;
	}

	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	public String getTaxIDNumber() {
		return taxIDNumber;
	}

	public void setTaxIDNumber(String taxIDNumber) {
		this.taxIDNumber = taxIDNumber;
	}
	
	public RelationshipsAB getRelationshipAB() {
		return this.relationshipAB;
	}
	
	public void setRelationshipAB(RelationshipsAB _relationshipAB) {
		this.relationshipAB = _relationshipAB;
	}

	public String getAlternateID() {
		return alternateID;
	}

	public void setAlternateID(String alternateID) {
		this.alternateID = alternateID;
	}
	
	public boolean isPerson() {
		return personOrCompany.equals(repository.gw.enums.ContactSubType.Person);
	}
	
	public boolean isCompany() {
		return personOrCompany.equals(repository.gw.enums.ContactSubType.Company);
	}
	
	public ArrayList<repository.gw.enums.ContactRole> getContactRoles(){
		return this.roles;
	}
	
	public void setContactRoles(ArrayList<repository.gw.enums.ContactRole> _roles) {
		this.roles = _roles;
	}
	
	public void addContactRole(ContactRole role) {
		this.roles.add(role);
	}

	public repository.gw.enums.InsuranceScore getInsuranceScore() {
		return insuranceScore;
}

	public void setInsuranceScore(InsuranceScore insuranceScore) {
		this.insuranceScore = insuranceScore;
	}
    
    public String returnFullName() {
    	String pmName = "";
        if (this.getMiddleName() != null && this.getMiddleName() != "") {
            pmName = this.getFirstName() + " " + this.getMiddleName() + " " + this.getLastName();
        } else {
            pmName = this.getFirstName() + " " + this.getLastName();
}
        return pmName;
    }

	public String getName() {
		if(personOrCompany.equals(ContactSubType.Company)) {
			return companyName;
		} else {
			return firstName + " " + lastName;
		}
	}
	
	public void setIsDupSSN(boolean isDup) {
		this.isDupSSN = isDup;
	}
	
	public boolean getIsDupSSN() {
		return this.isDupSSN;
	}

	public boolean isOrderMVR() {
		return orderMVR;
	}

	public void setOrderMVR(boolean orderMVR) {
		this.orderMVR = orderMVR;
	}

	public int getDriverNumber() {
		return driverNumber;
	}

	public void setDriverNumber(int driverNumber) {
		this.driverNumber = driverNumber;
	}

	public UnderwriterIssues_PL getAssignedUWIssue() {
		return assignedUWIssue;
	}

	public void setAssignedUWIssue(UnderwriterIssues_PL assignedUWIssue) {
		this.assignedUWIssue = assignedUWIssue;
	}


}























