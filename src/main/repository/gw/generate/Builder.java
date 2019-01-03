package repository.gw.generate;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.LexisNexis;
import persistence.globaldatarepo.helpers.LexisNexisHelper;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.*;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.*;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@SuppressWarnings("unused")
public class Builder extends BasePage {

    private Agents agentInfo = null;
    private boolean randomAgent = true;
    private repository.gw.enums.CreateNew createNew = repository.gw.enums.CreateNew.Create_New_Always;
    private repository.gw.enums.ContactSubType insPersonOrCompany = null;
    private String insFirstName = null;
    private String insLastName = null;
    private String insMiddleName = null;
    private repository.gw.enums.PersonSuffix insSuffixName = repository.gw.enums.PersonSuffix.None;
    private String insCompanyName = null;
    private String insDriversLicenseNumber = "DA" + StringsUtils.generateRandomNumberDigits(6) + "C";
    private State insDriversLicenseState = State.Idaho;
    private String socialSecurityNumber = null;
    private String taxIDNumber = null;
    private AddressInfo insPrimaryAddress = new AddressInfo();
    private repository.gw.enums.OrganizationType polOrgType = repository.gw.enums.OrganizationType.Joint_Venture;
    private CountyIdaho polDuesCounty = null;
    private Boolean membershipDuesOnAllInsureds = false;
    private Boolean sr22ChargesForPrimaryNamedInsured = false;
    private boolean membershipDuesForPNISetInBuilder = false;
    private Boolean membershipDuesForPrimaryNamedInsured = true;
    private int polTermLengthDays = 0;
    private Date effectiveDate = null;
    private ArrayList<PolicyInfoAdditionalNamedInsured> aniList = null;
    private PolicyBusinessownersLine busOwnLine = null;
    private ArrayList<PolicyLocation> locationList = null;
    private repository.gw.enums.PaymentPlanType paymentPlanType = repository.gw.enums.PaymentPlanType.Quarterly;
    private repository.gw.enums.PaymentType downPaymentType = repository.gw.enums.PaymentType.Cash;
    private BankAccountInfo bankAccountInfo = null;
    private Boolean handleBlockSubmit = true;
    private Date insDOB = repository.gw.helpers.DateUtils.dateAddSubtract(new Date(), repository.gw.enums.DateAddSubtractOptions.Year, -35);
    private boolean lexisNexisData;
    private boolean prefillPersonal;
    private boolean prefillCommercial;
    private boolean insuranceScore;
    private boolean mvr;
    private boolean clueAuto;
    private boolean clueProperty;
    private PolicySupplemental supplemental = null;

    private repository.gw.enums.ProductLineType productType = repository.gw.enums.ProductLineType.Businessowners;
    private ArrayList<repository.gw.enums.LineSelection> lineSelection = new ArrayList<repository.gw.enums.LineSelection>() {
        public static final long serialVersionUID = 1L;

        {
            this.add(repository.gw.enums.LineSelection.CommercialAutoLineCPP);
            this.add(repository.gw.enums.LineSelection.CommercialPropertyLineCPP);
            this.add(repository.gw.enums.LineSelection.GeneralLiabilityLineCPP);
            this.add(repository.gw.enums.LineSelection.InlandMarineLineCPP);
            this.add(repository.gw.enums.LineSelection.PersonalAutoLinePL);
            this.add(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL);
        }
    };
    private CPPCommercialAuto cppCommercialAuto = new CPPCommercialAuto();
    private CPPGeneralLiability cppGeneralLiability = new CPPGeneralLiability();
    private CPPCommercialProperty commercialPropertyCPP = new CPPCommercialProperty();
    private SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
    private repository.gw.enums.SquireEligibility squireEligibility = repository.gw.enums.SquireEligibility.City;
    private String insHouseholdMemberRole = "Driver";
    private ArrayList<InlandMarineTypes.InlandMarine> inlandMarineCoverageSelection_PL_IM = null;
    private repository.gw.enums.RelationshipToInsured insRelationshipToInsured = RelationshipToInsured.Insured;
    private ArrayList<RecreationalEquipment> recEquipment = null;
    private ArrayList<Livestock> livestock = null;
    private ArrayList<PersonalProperty> personalProperty = new ArrayList<PersonalProperty>();
    private Property.SectionIDeductible section1Deductible = Property.SectionIDeductible.FiveHundred;
    private ArrayList<SquireIMCargo> cargo;
    private ArrayList<SquireIMWatercraft> watercrafts;
    private ArrayList<FarmEquipment> farmEquipment;
    private SquireLiability liabilitySection;
    private SquirePropertyLiabilityExclusionsConditions propLiabExclusionsConditions;
    private ArrayList<Contact> policyMembers = new ArrayList<Contact>();
    private repository.gw.generate.GeneratePolicy squirePolicyUsedForUmbrella;
    private repository.gw.generate.GeneratePolicy squirePolicyUsedForStandardFire;
    private repository.gw.generate.GeneratePolicy standardFirePolicyUsedForLiability;
    private repository.gw.generate.GeneratePolicy standardFirePolicyUsedForSquire;
    private repository.gw.generate.GeneratePolicy standardFirePolicyUsedForBOP;
    private repository.gw.generate.GeneratePolicy standardIMPolicyUsedFor4HLiveStock;
    private repository.gw.generate.GeneratePolicy squirePolicyUsedForStandardIM;
    private repository.gw.generate.GeneratePolicy bopPolicyUsedForCPP;
    private repository.gw.generate.GeneratePolicy bopPolicyUsedForSquire;
    private repository.gw.generate.GeneratePolicy standardIMPolicyUsedForStdFire;
    private repository.gw.generate.GeneratePolicy multipleStandardFirePolicies;
    private SquireUmbrellaInfo squireUmbrellaInfo;
    private SquireFPP squireFPP;
    private repository.gw.enums.PackageRiskType riskType = repository.gw.enums.PackageRiskType.Apartment;
    /*private boolean IM4H;*/
    private boolean squireStdFire, StdFireSquire, StdFireBOP, multipleStdFire, commodity, stdFireLiability, stdIMSquire, bopForCPP, bopForSQ, stdIMStdFire;
    private boolean alwaysOrderCreditReport = true;
    private CountyIdaho ratingCounty;
    private boolean draft = false;

    private WebDriver webDriver;

    public Builder(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    public Builder isDraft() {
        this.draft = true;
        return this;
    }

    public Builder withSquireUmbrellaInfo(SquireUmbrellaInfo squireUmbrellaInfo) {
        this.squireUmbrellaInfo = squireUmbrellaInfo;
        return this;
    }

    public Builder withSquirePolicyUsedForUmbrella(repository.gw.generate.GeneratePolicy squirePolicyUsedForUmbrella) {
        this.squirePolicyUsedForUmbrella = squirePolicyUsedForUmbrella;
        this.agentInfo = squirePolicyUsedForUmbrella.agentInfo;
        return this;
    }

    public Builder withSquirePolicyUsedForStandardFire(repository.gw.generate.GeneratePolicy squirePolicyUsedForStandardFire, boolean squireStdFire, boolean commodity) {
        this.squirePolicyUsedForStandardFire = squirePolicyUsedForStandardFire;
        this.agentInfo = squirePolicyUsedForStandardFire.agentInfo;
        this.squireStdFire = squireStdFire;
        this.commodity = commodity;
        return this;
    }

    public Builder withStandardFirePolicyUsedForStandardLiability(repository.gw.generate.GeneratePolicy standardFirePolicyUsedForLiability, boolean stdFireLiability) {
        this.standardFirePolicyUsedForLiability = standardFirePolicyUsedForLiability;
        this.agentInfo = standardFirePolicyUsedForLiability.agentInfo;
        this.stdFireLiability = stdFireLiability;
        return this;
    }

	/*public Builder withSquirePolicyUsedForStandardIM(GeneratePolicy standardIMPolicyUsedFor4HLiveStock, boolean IM4H) {
		this.standardIMPolicyUsedFor4HLiveStock = standardIMPolicyUsedFor4HLiveStock;
		this.agentInfo = standardIMPolicyUsedFor4HLiveStock.agentInfo;
		this.IM4H = IM4H;
		return this;
	}*/


    public Builder withSquirePolicyUsedForStandardIM(repository.gw.generate.GeneratePolicy squirePolicyUsedForStandardIM, boolean stdIMSquire) {
        this.squirePolicyUsedForStandardIM = squirePolicyUsedForStandardIM;
        this.agentInfo = squirePolicyUsedForStandardIM.agentInfo;
        this.stdIMSquire = stdIMSquire;
        return this;
    }

    public Builder withBOPPolicyUsedForCPP(repository.gw.generate.GeneratePolicy bopPolicyUsedForCPP, boolean bopForCPP) {
        this.bopPolicyUsedForCPP = bopPolicyUsedForCPP;
        this.agentInfo = bopPolicyUsedForCPP.agentInfo;
        this.bopForCPP = bopForCPP;
        return this;
    }

    public Builder withBOPPolicyUsedForSquire(repository.gw.generate.GeneratePolicy bopPolicyUsedForSquire, boolean bopForSQ) {
        this.bopPolicyUsedForSquire = bopPolicyUsedForSquire;
        this.agentInfo = bopPolicyUsedForSquire.agentInfo;
        this.bopForSQ = bopForSQ;
        return this;
    }

    public Builder withStandardIMUsedForStandardFire(repository.gw.generate.GeneratePolicy standardIMPolicyUsedForStdFire, boolean stdIMStdFire) {
        this.standardIMPolicyUsedForStdFire = standardIMPolicyUsedForStdFire;
        this.agentInfo = standardIMPolicyUsedForStdFire.agentInfo;
        this.stdIMStdFire = stdIMStdFire;
        return this;
    }

    public Builder withStandardFirePolicyUsedForSquire(repository.gw.generate.GeneratePolicy standardFirePolicyUsedForSquire, boolean StdFireSquire) {
        this.standardFirePolicyUsedForSquire = standardFirePolicyUsedForSquire;
        this.agentInfo = standardFirePolicyUsedForSquire.agentInfo;
        this.StdFireSquire = StdFireSquire;
        return this;
    }

    public Builder withStandardFirePolicyUsedForBOP(repository.gw.generate.GeneratePolicy standardFirePolicyUsedForBOP, boolean StdFireBOP) {
        this.standardFirePolicyUsedForBOP = standardFirePolicyUsedForBOP;
        this.agentInfo = standardFirePolicyUsedForBOP.agentInfo;
        this.StdFireBOP = StdFireBOP;
        return this;
    }

    public Builder withMultipleStandardFirePolicies(GeneratePolicy multipleStandardFirePolicies, boolean multipleStdFire) {
        this.multipleStandardFirePolicies = multipleStandardFirePolicies;
        this.agentInfo = multipleStandardFirePolicies.agentInfo;
        this.multipleStdFire = multipleStdFire;
        return this;
    }

    public Builder withSquireLiability(SquireLiability liabilitySection) {
        this.liabilitySection = liabilitySection;
        return this;
    }

    public Builder withSquirePropertyLiabilityExclusionsConditions(SquirePropertyLiabilityExclusionsConditions propLiabExclusionsConditions) {
        this.propLiabExclusionsConditions = propLiabExclusionsConditions;
        return this;
    }

    // JON LARSEN R2
    public Builder withLineSelection(ArrayList<LineSelection> lineSelectionList) {
        this.lineSelection = lineSelectionList;
        return this;
    }

    public Builder withCPPCommercialAuto(CPPCommercialAuto cppCommercialAuto) {
        this.cppCommercialAuto = cppCommercialAuto;
        return this;
    }

    public Builder withCPPGeneralLiability(CPPGeneralLiability cppGeneralLiability) {
        this.cppGeneralLiability = cppGeneralLiability;
        return this;
    }

    public Builder withCPPCommercialProperty(CPPCommercialProperty commercialPropertyCPP) {
        this.commercialPropertyCPP = commercialPropertyCPP;
        return this;
    }

    public Builder withSquirePersonalAuto(SquirePersonalAuto squirePersonalAuto) {
        this.squirePersonalAuto = squirePersonalAuto;
        return this;
    }

    public Builder withSquirePersonalAuto() {
        this.squirePersonalAuto = new SquirePersonalAuto();
        return this;
    }

    public Builder withSquirePersonalAuto(SquirePersonalAutoCoverages sqpaCoverages) {
        this.squirePersonalAuto = new SquirePersonalAuto();
        this.squirePersonalAuto.setCoverages(sqpaCoverages);
        return this;
    }

    public Builder withSquireEligibility(SquireEligibility squireEligibility) {
        this.productType = repository.gw.enums.ProductLineType.Squire;
        this.squireEligibility = squireEligibility;
        return this;
    }

    // END R2

    public Builder withProductType(repository.gw.enums.ProductLineType productType) {
        this.productType = productType;
        return this;
    }

    public Builder withCreateNew(CreateNew createNew) {
        this.createNew = createNew;
        return this;
    }

    public Builder withAgent(Agents agentInfo) {
        this.agentInfo = agentInfo;
        this.randomAgent = false;
        return this;
    }

    public Builder withInsPersonOrCompany(repository.gw.enums.ContactSubType insPersonOrCompany) {
        this.insPersonOrCompany = insPersonOrCompany;
        return this;
    }

    public Builder withInsFirstLastName(String insFirstName, String insLastName) {
        this.insFirstName = insFirstName;
        this.insLastName = insLastName;
        this.insPersonOrCompany = repository.gw.enums.ContactSubType.Person;
        return this;
    }

    public Builder withInsCompanyName(String insCompanyName) {
        this.insCompanyName = insCompanyName;
        this.insPersonOrCompany = repository.gw.enums.ContactSubType.Company;
        return this;
    }

    public Builder withInsPersonOrCompanyDependingOnDay(String insFirstName, String insLastName, String insCompanyName) {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        if (dayOfMonth % 2 == 0) {
            this.insFirstName = insFirstName;
            this.insLastName = insLastName;
            this.insPersonOrCompany = repository.gw.enums.ContactSubType.Person;
        } else {
            this.insCompanyName = insCompanyName;
            this.insPersonOrCompany = repository.gw.enums.ContactSubType.Company;
        }

        return this;
    }

    public Builder withInsPersonOrCompanyRandom(String insFirstName, String insLastName, String insCompanyName) {
        this.insPersonOrCompany = new Random().nextBoolean() ? repository.gw.enums.ContactSubType.Company : repository.gw.enums.ContactSubType.Person;

        if (this.insPersonOrCompany == repository.gw.enums.ContactSubType.Person) {
            this.insFirstName = insFirstName;
            this.insLastName = insLastName;
        } else if (this.insPersonOrCompany == repository.gw.enums.ContactSubType.Company) {
            this.insCompanyName = insCompanyName;
        }

        return this;
    }

    public Builder withSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
        return this;
    }

    public Builder withTaxIDNumber(String taxIDNumber) {
        this.taxIDNumber = taxIDNumber;
        return this;
    }

    public Builder withInsPrimaryAddress(AddressInfo insPrimaryAddress) {
        this.insPrimaryAddress = insPrimaryAddress;
        return this;
    }

    public Builder withRatingCounty(CountyIdaho _county) {
        this.ratingCounty = _county;
        return this;
    }

    public Builder withPolOrgType(OrganizationType polOrgType) {
        this.polOrgType = polOrgType;
        return this;
    }

    public Builder withPolDuesCounty(CountyIdaho polDuesCounty) {
        this.polDuesCounty = polDuesCounty;
        return this;
    }

    public Builder withMembershipDuesOnAllInsureds() {
        this.membershipDuesOnAllInsureds = true;
        return this;
    }

    public Builder withSR22ChargesForPrimaryNamedInsured(Boolean hasSR22ChargesForPrimaryNamedInsured) {
        this.sr22ChargesForPrimaryNamedInsured = hasSR22ChargesForPrimaryNamedInsured;
        return this;
    }

    public Builder withMembershipDuesForPrimaryNamedInsured(Boolean hasMembershipDuesForPrimaryNamedInsured) {
        this.membershipDuesForPNISetInBuilder = true;
        this.membershipDuesForPrimaryNamedInsured = hasMembershipDuesForPrimaryNamedInsured;
        return this;
    }

    public Builder withPolTermLengthDays(int polTermLengthDays) {
        this.polTermLengthDays = polTermLengthDays;
        return this;
    }

    public Builder withPolEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
        return this;
    }

    public Builder withANIList(ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs) {
        this.aniList = listOfANIs;
        return this;
    }

    public Builder withBusinessownersLine(PolicyBusinessownersLine boLine) {
        this.busOwnLine = boLine;
        return this;
    }

    public Builder withBusinessownersLine() throws Exception {
        PolicyBusinessownersLine basicBOLine = new PolicyBusinessownersLine();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        this.locationList = locationsList;
        this.busOwnLine = basicBOLine;
        return this;
    }

    public Builder withBusinessownersLine(String insCompanyName) throws Exception {
        this.insCompanyName = insCompanyName;
        this.insPersonOrCompany = repository.gw.enums.ContactSubType.Company;

        PolicyBusinessownersLine basicBOLine = new PolicyBusinessownersLine();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        this.locationList = locationsList;
        this.busOwnLine = basicBOLine;
        return this;
    }

    public Builder withBusinessownersLine(String insCompanyName, repository.gw.enums.PaymentType downPaymentType) throws Exception {
        this.insCompanyName = insCompanyName;
        this.insPersonOrCompany = repository.gw.enums.ContactSubType.Company;

        PolicyBusinessownersLine basicBOLine = new PolicyBusinessownersLine();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        this.locationList = locationsList;
        this.busOwnLine = basicBOLine;

        Random random = new Random();
        ArrayList<repository.gw.enums.PaymentPlanType> plans = new ArrayList<repository.gw.enums.PaymentPlanType>();
        plans.add(repository.gw.enums.PaymentPlanType.Annual);
        plans.add(repository.gw.enums.PaymentPlanType.Semi_Annual);
        plans.add(repository.gw.enums.PaymentPlanType.Quarterly);
        plans.add(repository.gw.enums.PaymentPlanType.Monthly);
        int index = random.nextInt(plans.size());

        this.paymentPlanType = plans.get(index);

        this.downPaymentType = downPaymentType;

        this.productType = ProductLineType.Businessowners;

        return this;
    }

    public Builder withBusinessownersLine(String insCompanyName, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType) throws Exception {
        this.insCompanyName = insCompanyName;
        this.insPersonOrCompany = repository.gw.enums.ContactSubType.Company;

        PolicyBusinessownersLine basicBOLine = new PolicyBusinessownersLine();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        this.locationList = locationsList;
        this.busOwnLine = basicBOLine;

        this.paymentPlanType = paymentPlanType;

        this.downPaymentType = downPaymentType;

        return this;
    }

    public Builder withPolicyLocations(ArrayList<PolicyLocation> locationList) {
        this.locationList = locationList;
        return this;
    }

    public Builder withPolicyLocations(ArrayList<PolicyLocation> locationList, Property.SectionIDeductible section1Deductible) {
        this.locationList = locationList;
        this.section1Deductible = section1Deductible;
        return this;
    }

    public Builder withSupplemental(PolicySupplemental supplement) {
        this.supplemental = supplement;
        return this;
    }

    public Builder withPaymentPlanType(repository.gw.enums.PaymentPlanType paymentPlan) {
        this.paymentPlanType = paymentPlan;
        return this;
    }

    public Builder withRandomPaymentPlanType() {
        Random random = new Random();
        ArrayList<repository.gw.enums.PaymentPlanType> plans = new ArrayList<repository.gw.enums.PaymentPlanType>();
        plans.add(repository.gw.enums.PaymentPlanType.Annual);
        plans.add(repository.gw.enums.PaymentPlanType.Semi_Annual);
        plans.add(repository.gw.enums.PaymentPlanType.Quarterly);
        plans.add(PaymentPlanType.Monthly);
        int index = random.nextInt(plans.size());

        this.paymentPlanType = plans.get(index);
        return this;
    }

    public Builder withDownPaymentType(repository.gw.enums.PaymentType downPaymentType) {
        this.downPaymentType = downPaymentType;
        return this;
    }

    public Builder withRandomDownPaymentType() {
        Random random = new Random();
        ArrayList<repository.gw.enums.PaymentType> payments = new ArrayList<repository.gw.enums.PaymentType>();
        payments.add(repository.gw.enums.PaymentType.ACH_EFT);
        payments.add(repository.gw.enums.PaymentType.Credit_Debit);
        payments.add(repository.gw.enums.PaymentType.Cash);
        payments.add(repository.gw.enums.PaymentType.Check);
        payments.add(repository.gw.enums.PaymentType.Cash_Equivalent);
        payments.add(repository.gw.enums.PaymentType.Title_Company);
        payments.add(repository.gw.enums.PaymentType.Website);
        payments.add(PaymentType.Intercompany_Transfer);
        int index = random.nextInt(payments.size());

        this.downPaymentType = payments.get(index);
        return this;
    }

    public Builder withBankAccountInfo(BankAccountInfo bankAccountInfo) {
        this.bankAccountInfo = bankAccountInfo;
        return this;
    }

    public Builder withHandleBlockSubmit(Boolean yesno) {
        this.handleBlockSubmit = yesno;
        return this;
    }

    public Builder withInsMiddleName(String insMiddleName) {
        this.insMiddleName = insMiddleName;
        return this;
    }

    public Builder withInsSuffixName(PersonSuffix insSuffixName) {
        this.insSuffixName = insSuffixName;
        return this;
    }

    public Builder withInlandMarine(ArrayList<InlandMarineTypes.InlandMarine> _inlandMarine) {
        this.inlandMarineCoverageSelection_PL_IM = _inlandMarine;
        return this;
    }

    public Builder withRecEquipment(ArrayList<RecreationalEquipment> _recEquipment) {
        this.recEquipment = _recEquipment;
        return this;
    }

    public Builder withLivestock(LivestockList livestock) {
        this.livestock = livestock.getAllLivestockAsList();
        return this;
    }

    public Builder withCargo(ArrayList<SquireIMCargo> _cargo) {
        this.cargo = _cargo;
        return this;
    }

    public Builder withFarmEquipment(ArrayList<FarmEquipment> _farmEquipment) {
        this.farmEquipment = _farmEquipment;
        return this;
    }

    public Builder withWatercraft(ArrayList<SquireIMWatercraft> _watercrafts) {
        this.watercrafts = _watercrafts;
        return this;
    }

    public Builder withPersonalProperty(PersonalPropertyList personalProperty) {
        this.personalProperty = personalProperty.getAllPersonalPropertyAsList();
        return this;
    }

    public Builder withInsDOB(Date dob) {
        this.insDOB = dob;
        return this;
    }

    public Builder withInsAge(int age) {
        Date currentSystemDate = repository.gw.helpers.DateUtils.getCenterDate(webDriver, ApplicationOrCenter.PolicyCenter);
        this.insDOB = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Year, -age);
        return this;
    }

    public Builder withSquireFPP(SquireFPP squireFPP) {
        this.squireFPP = squireFPP;
        return this;
    }

    public Builder withPackageRiskType(PackageRiskType riskType) {
        this.riskType = riskType;
        return this;
    }

    public Builder withoutOrderingCreditReport() {
        this.alwaysOrderCreditReport = false;
        return this;
    }

    public Builder withPolicyMembers(ArrayList<Contact> _policyMembers) {
        this.policyMembers = _policyMembers;
        return this;
    }

    public Builder withLexisNexisData(boolean person, boolean prefillPersonal, boolean prefillCommercial, boolean insuranceScore, boolean mvr, boolean clueAuto, boolean clueProperty) throws Exception {
        this.lexisNexisData = true;
        this.prefillPersonal = prefillPersonal;
        this.prefillCommercial = prefillCommercial;
        this.insuranceScore = insuranceScore;
        this.mvr = mvr;
        this.clueAuto = clueAuto;
        this.clueProperty = clueProperty;

        LexisNexis customer = LexisNexisHelper.getRandomCustomer(ClockUtils.getCurrentDates(webDriver).get(ApplicationOrCenter.PolicyCenter), person, prefillPersonal, prefillCommercial, insuranceScore, mvr, clueAuto, clueProperty);

        if (person) {
            this.insPersonOrCompany = repository.gw.enums.ContactSubType.Person;
            this.insFirstName = customer.getFirstName();
            this.insLastName = customer.getLastName();
            this.insMiddleName = customer.getMiddleName();
            this.insDOB = customer.getDob();
            this.socialSecurityNumber = customer.getSsn();
            this.insDriversLicenseNumber = customer.getDlnumber();

            if (this.insDriversLicenseState != State.Idaho) {
                this.insDriversLicenseState = State.Idaho;
                this.insDriversLicenseNumber = "GA136510F";
            }

            if (this.insDriversLicenseNumber == null) {
                this.insDriversLicenseNumber = "GA136510F";
                this.insDriversLicenseState = State.Idaho;
            } else {
                this.insDriversLicenseState = State.valueOfAbbreviation(customer.getState());
            }
        } else {
            this.insPersonOrCompany = repository.gw.enums.ContactSubType.Company;
            this.insCompanyName = customer.getLastName();
        }

        this.insPrimaryAddress = new AddressInfo(customer.getStreet(), customer.getApartment(), customer.getCity(), State.valueOfAbbreviation(customer.getState()), customer.getZip(), CountyIdaho.Ada, "United States", AddressType.Home);

        return this;
    }

    public Builder withLexisNexisDataCustomer(ContactSubType companyOrPerson, String personFirstName, String personLastName, String personMiddleName, Date dob, String socialSecurityNumber, String driverLicenseNumber, AddressInfo address, boolean clueAuto) {

        this.lexisNexisData = true;
        this.clueAuto = clueAuto;
        this.insPersonOrCompany = companyOrPerson;
        this.insFirstName = personFirstName;
        this.insLastName = personLastName;
        this.insMiddleName = personMiddleName;
        this.insDOB = dob;
        this.insPrimaryAddress = address;
        this.socialSecurityNumber = socialSecurityNumber;
        this.insDriversLicenseNumber = driverLicenseNumber;

        return this;
    }

    public Builder withspecialDL(String driverLicenseNumber) {
        this.insDriversLicenseNumber = driverLicenseNumber;
        return this;
    }

//	public GeneratePolicy build(GeneratePolicyType typeToGenerate) throws Exception {
//		return new GeneratePolicy(typeToGenerate, this);
//	}

}
