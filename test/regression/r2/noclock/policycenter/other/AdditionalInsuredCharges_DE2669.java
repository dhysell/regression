package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.AddressType;
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
import repository.gw.enums.Building.SpoilageRiskClass;
import repository.gw.enums.Building.SqFtPercOccupied;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.Building.WiringType;
import repository.gw.enums.BusinessownersLine.LiabilityLimits;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.Location.AutoIncreaseBlgLimitPercentage;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.SpoilageLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocationAdditionalInsured;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.PolicyLocationBuildingAdditionalCoverages;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.search.SearchAccountsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;

/*
 * Jon Larsen 6/11/2015
 * This test class is for verifying correct charges on an account.
 */
//DE2669 Additional Insured Charges
//BP 04 16 Lessor  of Leased Equipment and BP 04 02 Manager or Lessor of Premise should charge $7 for Apartment and Offices and $20 for all else for EACH additional insured.
//BP 04 47 Vendors,  BP 04 48 Designated Persons and BP 14 05 Grantor of Franchise should charge $12 for $300,000 limits, $13 for $500,000 and $1,000,000 limits for EACH additional insured not just one.
//Spoilage
/*
 * Reviewed by Steve Broderick
 * */
@QuarantineClass
public class AdditionalInsuredCharges_DE2669 extends BaseTest {

    GeneratePolicy myPolicy = null;
    @SuppressWarnings("serial")
    private List<AdditionalInsuredRole> roleList = new ArrayList<AdditionalInsuredRole>() {
        {
            this.add(AdditionalInsuredRole.Vendors);
            this.add(AdditionalInsuredRole.DesignatedPersonOrOrganization);
            this.add(AdditionalInsuredRole.GrantorOfFranchise);
        }
    };

    @SuppressWarnings("serial")
    private List<SmallBusinessType> smallBusiness = new ArrayList<SmallBusinessType>() {
        {
            this.add(SmallBusinessType.Apartments);
            this.add(SmallBusinessType.Offices);
            this.add(SmallBusinessType.Condominium);
            this.add(SmallBusinessType.SelfStorageFacilities);
        }
    };

    @SuppressWarnings("serial")
    private List<SpoilageLimit> spoilageLimit = new ArrayList<SpoilageLimit>() {
        {
            this.add(SpoilageLimit.Five000);
            this.add(SpoilageLimit.Ten000);
            this.add(SpoilageLimit.Fifteen000);
            this.add(SpoilageLimit.Twenty000);
            this.add(SpoilageLimit.TwentyFive000);
            this.add(SpoilageLimit.Thirty000);
            this.add(SpoilageLimit.Forty000);
            this.add(SpoilageLimit.FortyFive000);
            this.add(SpoilageLimit.Fifty000);
        }
    };

    @SuppressWarnings("serial")
    private List<SpoilageRiskClass> spoilageRisk = new ArrayList<SpoilageRiskClass>() {
        {
            this.add(SpoilageRiskClass.BakeryGoods28);
            this.add(SpoilageRiskClass.CheeseShops28);
            this.add(SpoilageRiskClass.ConvenienceFoodStore56);
            this.add(SpoilageRiskClass.DairyProductsExcludingIceCream56);
            this.add(SpoilageRiskClass.DairyProductsIncludingIceCream74);
            this.add(SpoilageRiskClass.Delicatessens28);
            this.add(SpoilageRiskClass.Florists74);
            this.add(SpoilageRiskClass.FruitsAndVegetables28);
            this.add(SpoilageRiskClass.GroceryStores56);
            this.add(SpoilageRiskClass.MeatAndPoultryMarkets56);
            this.add(SpoilageRiskClass.PharmaceuticalsNonManufacturing56);
            this.add(SpoilageRiskClass.Restaurants28);
            this.add(SpoilageRiskClass.Seafood74);
            this.add(SpoilageRiskClass.Supermarkets56);
        }
    };

    @SuppressWarnings("serial")
    private List<LiabilityLimits> liabilityLimits = new ArrayList<LiabilityLimits>() {
        {
            this.add(LiabilityLimits.Three00_600_600);
            this.add(LiabilityLimits.Five00_1000_1000);
            this.add(LiabilityLimits.One000_2000_2000);
            this.add(LiabilityLimits.Two000_4000_4000);
        }
    };

    private WebDriver driver;

    @SuppressWarnings("serial")
    @Test(enabled = true)
    public void generatePolicy() throws Exception {

        // Spoilage mySpoilage = SpoilageHelper.getRandomSpoilage();
        //
        // Boolean powerOutage = getRandBoolean();
        // Boolean refMaintAgreement = getRandBoolean();
        // Boolean breakdownContamination = getRandBoolean();

        // BUSINESS OWNERS LINE
        PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(smallBusiness.get(NumberUtils.generateRandomNumberInt(0, (smallBusiness.size() - 1)))) {
            {
                this.setLiabilityLimits(liabilityLimits.get(NumberUtils.generateRandomNumberInt(0, (liabilityLimits.size() - 1))));
                this.setAdditionalCoverageStuff(new PolicyBusinessownersLineAdditionalCoverages(false, true) {{
                }}); // END ADDITIONAL COVERAGES

                this.setAdditonalInsuredBOLineList(new ArrayList<PolicyBusinessownersLineAdditionalInsured>() {{
                    this.add(new PolicyBusinessownersLineAdditionalInsured(ContactSubType.Company, "Howell Plumbing &  Heating INC", roleList.get(NumberUtils.generateRandomNumberInt(0, (roleList.size() - 1))), new AddressInfo("2913 Sunbeam Rd", null, "American Falls", State.Idaho, "83211", CountyIdaho.Power, null, AddressType.Mailing)));
                    this.add(new PolicyBusinessownersLineAdditionalInsured(ContactSubType.Company, "Wholesale Parts INC", roleList.get(NumberUtils.generateRandomNumberInt(0, (roleList.size() - 1))), new AddressInfo("PO Box 4885", null, "Pocatello", State.Idaho, "83205", "", null, AddressType.Mailing)));
                    this.add(new PolicyBusinessownersLineAdditionalInsured(ContactSubType.Person, "Kayrene", "Kelley", roleList.get(NumberUtils.generateRandomNumberInt(0, (roleList.size() - 1))), new AddressInfo("1249 N 950 E", null, "Shelley", State.Idaho, "83274", CountyIdaho.Bingham, null, AddressType.Mailing)));
                }}); // END BUSINESS OWNERS LINE ADDITIONAL INSURED LIST
            }
        }; // END BUSINESS OWNERS LINE

        // LOCATIONS
        ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>() {{
            this.add(new PolicyLocation(new AddressInfo(true), false) {
                {
                    this.setManualProtectionClassCode(ProtectionClassCode.Prot5);
                    this.setAutoautoIncrease(AutoIncreaseBlgLimitPercentage.AutoInc2Perc);
                    this.setAdditionalCoveragesStuff(new PolicyLocationAdditionalCoverages() {{
                    }}); // END ADDITIONAL COVERAGES STUFF
                    this.setAdditionalInsuredLocationsList(new ArrayList<PolicyLocationAdditionalInsured>() {
                        {
                            this.add(new PolicyLocationAdditionalInsured(ContactSubType.Company) {
                                {
                                    this.setAiRole(AdditionalInsuredRole.LessorOfLeasedEquipment);
                                    this.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
                                }
                            });

                            this.add(new PolicyLocationAdditionalInsured(ContactSubType.Company) {
                                {
                                    this.setAiRole(AdditionalInsuredRole.LessorOfLeasedEquipment);
                                    this.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
                                }
                            });

                            this.add(new PolicyLocationAdditionalInsured(ContactSubType.Company) {
                                {
                                    this.setAiRole(AdditionalInsuredRole.ManagersOrLessorsOrPremises);
                                    this.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
                                }
                            });
                        }
                    });

                    // LOCATION BULDING
                    this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {{
                                             this.add(new PolicyLocationBuilding() {{
                                                 this.setUsageDescription("Building 1");
                                                 this.setNamedInsuredOwner(false);
                                                 this.setOccupancySqFtPercOccupied(SqFtPercOccupied.SeventyFiveOneHundredPerc);
                                                 this.setOccupancyPercAreaLeasedToOthers(PercAreaLeasedToOthers.ZeroTenPerc);
                                                 this.setOccupancyNamedInsuredInterest(OccupancyInterestType.TenantOperator);
                                                 // this.setClassClassification(BuildingClassCode.ClothingorWearingApparelLadiesandGirlsCoatsSuitsandDresses.getClassification());
                                                 // this.setClassCode(buildingClassCodesForPDDeductible.get(new
                                                 // Random().nextInt(buildingClassCodesForPDDeductible.size())));

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

                                                 // parking lot characteristics
                                                 this.setParkingLotCharacteristicsList(new ArrayList<ParkingLotSidewalkCharacteristics>() {{
                                                     this.add(ParkingLotSidewalkCharacteristics.Potholes);
                                                     this.add(ParkingLotSidewalkCharacteristics.RaisedSunkenSurfaces);
                                                 }}); // END PARKINGLOT CHARACERISTICS

                                                 // safety equipment
                                                 this.setSafetyEquipmentList(new ArrayList<SafetyEquipment>() {{
                                                     this.add(SafetyEquipment.HandRailinThreeOrMoreSteps);
                                                     this.add(SafetyEquipment.NonSlipSurfaces);
                                                 }}); // END SAFETY EQUIPMENT LIST

                                                 this.setExitsProperlyMarked(true);
                                                 this.setNumFireExtinguishers(12);
                                                 this.setExposureToFlammablesChemicals(false);
                                                 this.setExposureToFlammablesChemicalsDesc("acids");

                                                 this.setConstructionType(ConstructionType.MasonryNonCombustible);
                                                 this.setYearBuilt(2013);
                                                 this.setNumStories(2);
                                                 this.setNumBasements(0);
                                                 this.setTotalArea("2564");
                                                 // SET BASEMENT FINISHED SQ FEET
                                                 // SET BASEMENT UNFINISHED SQ FEET
                                                 this.setSprinklered(true);
                                                 this.setPhotoYear(1993);
                                                 this.setCostEstimatorYear(1999);

                                                 this.setRoofingType(RoofingType.Aluminum);
                                                 this.setFlatRoof(false);
                                                 this.setRoofCondition(RoofCondition.NoIssues);
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
                                                 this.setAdditionalCoveragesStuff(new PolicyLocationBuildingAdditionalCoverages() {{
                                                     this.setSpoilageCoverage(true);
                                                     // this.setSpoilageBreakdownContamination(breakdownContamination);
                                                     this.setSpoilageLimit(spoilageLimit.get(NumberUtils.generateRandomNumberInt(0, (spoilageLimit.size() - 1))));
                                                     // this.setSpoilagePowerOutage(powerOutage);
                                                     this.setSpoilageProductDescription("Fish Food");
                                                     // this.setSpoilageRefrigerationMaintAgreement(refMaintAgreement);
                                                     this.setSpoilageRiskDescriptionClass(spoilageRisk.get(NumberUtils.generateRandomNumberInt(0, (spoilageRisk.size() - 1))));
                                                 }});
                                                 this.setAdditionalInterestList(new ArrayList<AdditionalInterest>() {{
                                                     this.add(new AdditionalInterest(ContactSubType.Company) {{
                                                         this.setAdditionalInterestType(AdditionalInterestType.Mortgagee);
                                                         this.setAddress(new AddressInfo("1400 S Vista Ave", null, "Boise", State.Idaho, "83705", "Ada", null, AddressType.Lienholder));
                                                         this.setAppliedToBPP(true);
                                                         this.setAppliedToBuilding(true);
                                                         this.setCompanyName("Wells Fargo");
                                                         this.setCompanyOrInsured(ContactSubType.Company);
                                                         this.setFirstMortgage(true);
                                                         this.setPhone("2082323231");
                                                     }}); // END BUILDING ADDITIONAL
                                                 }}); // END BUILDING ADDITIONAL INTREST LIST
                                             }}); // END BUILDING
                                         }} // END BUILDING LIST
                    ); // END SET BUILDING LIST
                }
            });// END POLICY LOCATION
        }}; // END LOCATION LIST

        // END LOCATIONS

        System.out.println("GENERATE \n");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        myPolicy = new GeneratePolicy.Builder(driver)
                .withBusinessownersLine(boLine)
                .withPolicyLocations(locationsLists)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("AI Charges")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(new AddressInfo(true))
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)

                .build(GeneratePolicyType.QuickQuote);

        System.out.println(myPolicy.accountNumber);
        System.out.println(myPolicy.agentInfo.getAgentUserName());
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void verifyCharges() throws Exception {
        Boolean testFailed = false;
        String failMessage = "";

        SmallBusinessType businessType = myPolicy.busOwnLine.getSmallBusinessType();
        LiabilityLimits limits = myPolicy.busOwnLine.getLiabilityLimits();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword());

        SearchAccountsPC search = new SearchAccountsPC(driver);
        search.searchAccountByAccountNumber(myPolicy.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickWorkOrderbySuffix("001");

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQuote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        // if Apartment or Office $7 else $20
        double lessor = quote.getAdditionalInsuredCharges("Lessor Of Leased Equipment BP 04 16");
        double manager = quote.getAdditionalInsuredCharges("Managers Or Lessor Of Premises BP 04 02");

        // if Limit$300,000 $12 $500K - $1M $13
        double vendors = quote.getAdditionalInsuredCharges("Vendors BP 04 47");
        double designatedPersons = quote.getAdditionalInsuredCharges("Designated Person Or Organization BP 04 48");
        double grantorOfFranchise = quote.getAdditionalInsuredCharges("Grantor Of Franchise BP 14 05");

        if (businessType.equals(SmallBusinessType.Apartments) || businessType.equals(SmallBusinessType.Offices)) {
            if (lessor % 7 != 0) {
                failMessage += failMessage
                        + "\nLessor Of Leased Equipment BP 04 16: charge was not $7 per A.I. when Small BusinessType is "
                        + businessType.getValue();
                testFailed = true;
            }
            if (manager % 7 != 0) {
                failMessage += failMessage
                        + "\nManagers Or Lessor Of Premises BP 04 02: charge was not $7 per A.I. when Small BusinessType is "
                        + businessType.getValue();
                testFailed = true;
            }
        } else {
            if (lessor % 20 != 0) {
                failMessage += failMessage + "\nLessor Of Leased Equipment BP 04 16: charge was not $20 per A.I.";
                testFailed = true;
            }
            if (manager % 20 != 0) {
                failMessage += failMessage + "\nManagers Or Lessor Of Premises BP 04 02: charge was not $20 per A.I.";
                testFailed = true;
            }
        }

        if (limits.equals(LiabilityLimits.Three00_600_600)) {
            if (vendors % 12 != 0) {
                failMessage += failMessage
                        + "\nVendors BP 04 47: charge was not $12 per A.I. when Liability Limits are "
                        + limits.getValue();
                testFailed = true;
            }
            if (designatedPersons % 12 != 0) {
                failMessage += failMessage
                        + "\nDesignated Person Or Organization BP 04 48: charge was not $12 per A.I. when Liability Limits are "
                        + limits.getValue();
                testFailed = true;
            }
            if (grantorOfFranchise % 12 != 0) {
                failMessage += failMessage
                        + "\nGrantor Of Franchise BP 14 05: charge was not $12 per A.I. when Liability Limits are "
                        + limits.getValue();
                testFailed = true;
            }
        } else if (limits.equals(LiabilityLimits.Five00_1000_1000) || limits.equals(LiabilityLimits.One000_2000_2000)
                || limits.equals(LiabilityLimits.One000_2000_2000)) {
            if (vendors % 13 != 0) {
                failMessage += failMessage
                        + "\nVendors BP 04 47: charge was not $13 per A.I. when Liability Limits are "
                        + limits.getValue();
                testFailed = true;
            }
            if (designatedPersons % 13 != 0) {
                failMessage += failMessage
                        + "\nDesignated Person Or Organization BP 04 48: charge was not $13 per A.I. when Liability Limits are "
                        + limits.getValue();
                testFailed = true;
            }
            if (grantorOfFranchise % 13 != 0) {
                failMessage += failMessage
                        + "\nGrantor Of Franchise BP 14 05: charge was not $13 per A.I. when Liability Limits are "
                        + limits.getValue();
                testFailed = true;
            }
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + failMessage);
        }
    }

}
