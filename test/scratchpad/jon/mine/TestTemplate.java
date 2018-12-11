package scratchpad.jon.mine;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Building.BoxType;
import repository.gw.enums.Building.CauseOfLoss;
import repository.gw.enums.Building.ConstructionType;
import repository.gw.enums.Building.FireBurglaryAlarmGrade;
import repository.gw.enums.Building.FireBurglaryResponseType;
import repository.gw.enums.Building.FireBurglaryTypeOfSystem;
import repository.gw.enums.Building.HouseKeepingMaint;
import repository.gw.enums.Building.OccupancyInterestType;
import repository.gw.enums.Building.ParkingLotSidewalkCharacteristics;
import repository.gw.enums.Building.PercAreaLeasedToOthers;
import repository.gw.enums.Building.RoofCondition;
import repository.gw.enums.Building.RoofingType;
import repository.gw.enums.Building.SafetyEquipment;
import repository.gw.enums.Building.SqFtPercOccupied;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.Building.WiringType;
import repository.gw.enums.BuildingClassCode;
import repository.gw.enums.BusinessownersLine.EmpDishonestyLimit;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsConductedFrequency;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsPerformedBy;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.Location.AutoIncreaseBlgLimitPercentage;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocationBuilding;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;

@SuppressWarnings({"serial", "unused"})
public class TestTemplate extends BaseTest {


    private String password = "gw";
    private String accountNumber = "";
    private String policynumber = "";
    private String policyssn = "";
    private String agentUsername = "rellis";
    private String underwriter = "";


    private String agentSA = "sellis";
    private String agencyManagerUserName = "rpalmer";
    private String accountsReceivable = "jwilliams";
    private String underwriterSupervisor = "hhill";


    @BeforeClass
    public void beforeClass() {

    }

    @AfterClass
    public void afterClass() {

    }




    @Test()
    public void createPolicy() throws Exception {


        // BUSINESS OWNERS LINE
        PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);

        PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, true);
        myLineAddCov.setEmployeeDishonestyCoverage(true);
        myLineAddCov.setEmpDisLimit(EmpDishonestyLimit.Dishonest10000);
        myLineAddCov.setEmpDisNumCoveredEmployees(5);
        myLineAddCov.setEmpDisReferencesChecked(true);
        myLineAddCov.setEmpDisHowOftenAudits(EmployeeDishonestyAuditsConductedFrequency.Semi_Annually);
        myLineAddCov.setEmpDisAuditsPerformedBy(EmployeeDishonestyAuditsPerformedBy.Private_Auditing_Firm);
        myLineAddCov.setEmpDisDiffWriteThanReconcile(true);
        myLineAddCov.setEmpDisLargeCheckProcedures(true);
        // END BUSINESS OWNERS LINE

        // ADDITION INSUREDS
        ArrayList<PolicyBusinessownersLineAdditionalInsured> additonalInsuredBOLineList = new ArrayList<PolicyBusinessownersLineAdditionalInsured>();
        PolicyBusinessownersLineAdditionalInsured additionalInsureds = new PolicyBusinessownersLineAdditionalInsured(ContactSubType.Company, "Wells Fargo", AdditionalInsuredRole.CertificateHolderOnly, new AddressInfo(true));
        additionalInsureds.setAddress(new AddressInfo(true));
        additionalInsureds.setCompanyName("Wells Fargo");
        additionalInsureds.setContactRole("Affiliate");
        additionalInsureds.setMemberNumber("");
        additionalInsureds.setPersonFirstName("Jim");
        additionalInsureds.setPersonLastName("Bowe");
        additionalInsureds.setPhone("2082323231");
        additionalInsureds.setSocialSecurityTaxNum("589625474");
//		additionalInsureds.setAiRole(AdditionalInsuredRole.CoOwnerOfInsuredPremises);
        additionalInsureds.setCompanyOrPerson(ContactSubType.Company);
        additionalInsureds.setSpecialWording(false);
        additionalInsureds.setSpecialWordingAcord101Desc("The Fat Cat Ate The Fat Rat");
        additionalInsureds.setSpecialWordingDesc("And The Fat Bat");
//		additionalInsureds.setVendorRoleListProducts("Cow's");
        additionalInsureds.setWaiverOfSubro(false);

        additonalInsuredBOLineList.add(additionalInsureds);

        myline.setAdditionalCoverageStuff(myLineAddCov);
        myline.setAdditonalInsuredBOLineList(additonalInsuredBOLineList);
        // END ADDITIONAL INSUREDS

        // ADDITIONAL INTERESTS
        ArrayList<AdditionalInterest> additionalInterestList = new ArrayList<AdditionalInterest>();
		AdditionalInterest additionalIterests = new AdditionalInterest("Wells Fargo", null);
        additionalIterests.setAdditionalInterestType(AdditionalInterestType.Mortgagee);
        additionalIterests.setAddress(new AddressInfo());
        additionalIterests.setAppliedToBPP(true);
        additionalIterests.setAppliedToBuilding(true);
        additionalIterests.setCompanyName("Wells Fargo");
        additionalIterests.setCompanyOrInsured(ContactSubType.Company);
        additionalIterests.setFirstMortgage(true);
        additionalIterests.setLoanContractNumber("");
        additionalIterests.setLienholderNumber("");
        additionalIterests.setPersonFirstName("Jim");
        additionalIterests.setPersonLastName("Bowe");
        additionalIterests.setPhone("2082323231");
        additionalIterests.setSocialSecurityTaxNum("589625478");

        //		additionalInterestList.add(additionalIterests);
        // END ADDITIONAL INTERESTS

        // LOCATIONS
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        PolicyLocation myLocation = new PolicyLocation(new AddressInfo(), false);
        myLocation.setManualProtectionClassCode(ProtectionClassCode.Prot5);
        myLocation.setAutoautoIncrease(AutoIncreaseBlgLimitPercentage.AutoInc2Perc);
        myLocation.setAdditionalCoveragesStuff(new PolicyLocationAdditionalCoverages() {{
            this.setMoneyAndSecuritiesCoverage(true);
            this.setMoneySecNumMessengersCarryingMoney(5);
            this.setMoneySecDepositDaily(true);
            this.setMoneySecHowOftenDeposit("Hourly");
            this.setMoneySecOffPremisesLimit(23);
            this.setMoneySecOnPremisesLimit(92);
            this.setOutdoorSignsCoverage(true);
            this.setOutdoorSignsLimit(67);
            this.setWaterBackupAndSumpOverflow(true);

        }});

        // END LOCATIONS

        // BUILDINGS
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>() {{
            new PolicyLocationBuilding() {{
                this.setUsageDescription("Pool 1");
                this.setNamedInsuredOwner(false);
                this.setOccupancySqFtPercOccupied(SqFtPercOccupied.SeventyFiveOneHundredPerc);
                this.setOccupancyPercAreaLeasedToOthers(PercAreaLeasedToOthers.ZeroTenPerc);
                this.setOccupancyNamedInsuredInterest(OccupancyInterestType.TenantOperator);
                this.setClassClassification(BuildingClassCode.ClothingorWearingApparelLadiesandGirlsCoatsSuitsandDresses.getClassification());
//				this.setClassCode(59325);

                this.setBuildingLimit(5000);
                this.setBuildingValuationMethod(ValuationMethod.ActualCashValue);
                this.setBuildingCauseOfLoss(CauseOfLoss.Special);
                this.setBuildingRoofExlusiongEndorsement(false);
                this.setBuildingWindstormHailLossesToRoofSurfacing(false);

                this.setBppLimit(5000);
                this.setBppValuationMethod(ValuationMethod.ReplacementCost);
                this.setBppCauseOfLoss(CauseOfLoss.Special);

                this.setExteriorHouseKeepingAndMaintenance(HouseKeepingMaint.Superior);
                this.setInteriorHouseKeepingAndMaintenance(HouseKeepingMaint.Good);

                //parking lot characteristics
                this.setParkingLotCharacteristicsList(new ArrayList<ParkingLotSidewalkCharacteristics>() {{
                    this.add(ParkingLotSidewalkCharacteristics.Potholes);
                    this.add(ParkingLotSidewalkCharacteristics.RaisedSunkenSurfaces);
                }});

                //safety equipment
                this.setSafetyEquipmentList(new ArrayList<SafetyEquipment>() {{
                    this.add(SafetyEquipment.HandRailinThreeOrMoreSteps);
                    this.add(SafetyEquipment.NonSlipSurfaces);
                }});

                this.setExitsProperlyMarked(true);
                this.setNumFireExtinguishers(12);
                this.setExposureToFlammablesChemicals(false);
                this.setExposureToFlammablesChemicalsDesc("acids");

                this.setConstructionType(ConstructionType.MasonryNonCombustible);
                this.setYearBuilt(1990);
                this.setNumStories(2);
                this.setNumBasements(0);
                this.setTotalArea("2564");
                //SET BASEMENT FINISHED SQ FEET
                //SET BASEMENT UNFINISHED SQ FEET
                this.setSprinklered(true);
                this.setPhotoYear(1993);
                this.setCostEstimatorYear(1999);

                this.setRoofingType(RoofingType.Aluminum);
                this.setFlatRoof(false);
                this.setRoofCondition(RoofCondition.HasSomeWearAndTear);
                this.setYearRoofReplaced(2000);

                this.setWiringType(WiringType.Romex);
                this.setBoxType(BoxType.CircuitBreaker);
                this.setYearLastMajorWiringUpdate(2000);
                this.setWiringUpdateDesc("Bens Kite");

                this.setYearLastMajorHeatingUpdate(2000);
                this.setHeatingUpdateDesc("Camp Fire");

                this.setYearLastMajorPlumbingUpdate(2000);
                this.setPlumbingUpdateDesc("Out House");

                this.setExistingDamage(false);
                this.setExistingDamageDesc("Hammers");

                this.setFireBurglaryTypeOfSystem(FireBurglaryTypeOfSystem.FireBurglary);
                this.setFireBurglaryResponseType(FireBurglaryResponseType.PrivateMonitored);
                this.setFireBurglaryAlarmGrade(FireBurglaryAlarmGrade.SecurityServiceWithTimingDevice);
                this.setAlarmCertificate("Omni");

                this.setInsuredPropertyWithin100Ft(false);
                this.setInsuredPropertyWithin100FtPolicyHolderName("Bob the Builder");
                this.setInsuredPropertyWithin100FtPolicyNumber("08-353652-01");
            }};
        }};


        PolicyLocationBuilding myBuilding = new PolicyLocationBuilding();


        PolicyLocationBuilding myOtherBuilding = new PolicyLocationBuilding();
        myOtherBuilding.setYearBuilt(2000);
        myOtherBuilding.setClassClassification("Antique Stores");
        // END BUILDING


        locOneBuildingList.add(myBuilding);
        myLocation.setBuildingList(locOneBuildingList);
        locationsList.add(myLocation);


        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);

        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
//		.withAgentUserName(agentUsername)
                .withCreateNew(CreateNew.Create_New_Only_If_Does_Not_Exist)
                .withInsPersonOrCompany(ContactSubType.Company)
//		.withInsFirstLastName("Jim", "Bowe")
                .withInsCompanyName("Testing PolicyIssued")
                .withPolOrgType(OrganizationType.LLC)
//		.withPolDuesCounty(CountyIdaho.Bonneville)
                //Membership Dues
                .withMembershipDuesOnAllInsureds()
                .withInsPrimaryAddress(new AddressInfo(true))
//		.withPolTermLengthDays(60)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.Offices))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash)


                .build(GeneratePolicyType.PolicyIssued);

//		accountNumber = myPolicyObj.accountNumber;
//		
//		policynumber = myPolicyObj.policyNumber;
//		underwriter = myPolicyObj.underwriterInfo.getUnderwriterUserName();
//		policyssn = myPolicyObj.socialSecurityNumber;

        System.out.println(accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentUserName());

    }


}











