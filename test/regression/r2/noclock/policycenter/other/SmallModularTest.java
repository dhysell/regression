/*
 * Jon Larsen 6/3/2015
 * this Test Suite is for all the Small Quick Test to cut time in running each one individually
 * Generate will be broken up into its separate modules and each small test can be run in between those
 *
 * IF setting a specific value for Generate, add a //COMMENT, directly after so it doesn't get changed by someone else.
 *
 * Reviewed by Steve Broderick
 */
package regression.r2.noclock.policycenter.other;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;
import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsPaidDuesAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.ActivtyRequestType;
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
import repository.gw.enums.Building.SpoilageRiskClass;
import repository.gw.enums.Building.SqFtPercOccupied;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.Building.WiringType;
import repository.gw.enums.BuildingClassCode;
import repository.gw.enums.BusinessownersLine.EmpDishonestyLimit;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsConductedFrequency;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsPerformedBy;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.Location.AutoIncreaseBlgLimitPercentage;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.SpoilageLimit;
import repository.gw.enums.TaxReportingOption;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.BankAccountInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.PolicyLocationBuildingAdditionalCoverages;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountContactsPC;
import repository.pc.account.AccountsSideMenuPC;
import repository.pc.activity.GenericActivityPC;
import repository.pc.activity.NewActivityRequestPC;
import repository.pc.search.SearchAccountsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineIncludedCoverages;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import persistence.globaldatarepo.helpers.ActivityPatternsHelper;

@QuarantineClass
public class SmallModularTest extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    private Date currentDate;
    private ArrayList<String> buildingClassCodesForPDDeductible = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            this.add("63831");
            this.add("63830");
            this.add("63841");
        }
    };
    private WebDriver driver;

    @Test(description = "Generates QuickQuote Submission", enabled = true)
    public void generateQuickQuote() throws Exception {

        // BUSINESS OWNERS LINE
        PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail) {
            {
                this.setAdditionalCoverageStuff(new PolicyBusinessownersLineAdditionalCoverages(false, true) {
                    {
                        this.setEmployeeDishonestyCoverage(false);
                        this.setEmpDisLimit(EmpDishonestyLimit.Dishonest10000);
                        this.setEmpDisNumCoveredEmployees(5);
                        this.setEmpDisReferencesChecked(true);
                        this.setEmpDisHowOftenAudits(EmployeeDishonestyAuditsConductedFrequency.Semi_Annually);
                        this.setEmpDisAuditsPerformedBy(EmployeeDishonestyAuditsPerformedBy.Private_Auditing_Firm);
                        this.setEmpDisDiffWriteThanReconcile(true);
                        this.setEmpDisLargeCheckProcedures(true);
                    }
                }); // END ADDITIONAL COVERAGES
                this.setAdditonalInsuredBOLineList(new ArrayList<PolicyBusinessownersLineAdditionalInsured>() {
                    private static final long serialVersionUID = 1L;

                    {
                    }
                }); // END BUSINESS OWNERS LINE ADDITIONAL INSURED LIST
            }
        }; // END BUSINESS OWNERS LINE

        // LOCATIONS
        ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>() {
            private static final long serialVersionUID = 1L;

            {

                this.add(new PolicyLocation(new AddressInfo(true), false) {
                    {
                        this.setManualProtectionClassCode(ProtectionClassCode.Prot5);
                        this.setAutoautoIncrease(AutoIncreaseBlgLimitPercentage.AutoInc2Perc);
                        this.setAdditionalCoveragesStuff(new PolicyLocationAdditionalCoverages() {
                            {
                                this.setMoneyAndSecuritiesCoverage(true);
                                this.setMoneySecNumMessengersCarryingMoney(5);
                                this.setMoneySecDepositDaily(true);
                                this.setMoneySecHowOftenDeposit("Hourly");
                                this.setMoneySecOffPremisesLimit(230);
                                this.setMoneySecOnPremisesLimit(920);
                                this.setOutdoorSignsCoverage(true);
                                this.setOutdoorSignsLimit(6700);
                                this.setWaterBackupAndSumpOverflow(true);
                            }
                        }); // END ADDITIONAL COVERAGES STUFF
                        // LOCATION BULDING
                        this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {
                                                 private static final long serialVersionUID = 1L;

                                                 {
                                                     this.add(new PolicyLocationBuilding() {
                                                         {
                                                             this.setUsageDescription("Building 1");
                                                             this.setNamedInsuredOwner(false);
                                                             this.setOccupancySqFtPercOccupied(SqFtPercOccupied.SeventyFiveOneHundredPerc);
                                                             this.setOccupancyPercAreaLeasedToOthers(PercAreaLeasedToOthers.ZeroTenPerc);
                                                             this.setOccupancyNamedInsuredInterest(OccupancyInterestType.TenantOperator);
                                                             // this.setClassClassification(BuildingClassCode.ClothingorWearingApparelLadiesandGirlsCoatsSuitsandDresses.getClassification());
                                                             this.setClassCode(buildingClassCodesForPDDeductible
                                                                     .get(new Random().nextInt(buildingClassCodesForPDDeductible.size()))); // SET FOR PD DEDUCTIBLE

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
                                                             this.setParkingLotCharacteristicsList(
                                                                     new ArrayList<ParkingLotSidewalkCharacteristics>() {
                                                                         private static final long serialVersionUID = 1L;

                                                                         {
                                                                             this.add(ParkingLotSidewalkCharacteristics.Potholes);
                                                                             this.add(ParkingLotSidewalkCharacteristics.RaisedSunkenSurfaces);
                                                                         }
                                                                     }); // END PARKINGLOT CHARACERISTICS

                                                             // safety equipment
                                                             this.setSafetyEquipmentList(new ArrayList<SafetyEquipment>() {
                                                                 private static final long serialVersionUID = 1L;

                                                                 {
                                                                     this.add(SafetyEquipment.HandRailinThreeOrMoreSteps);
                                                                     this.add(SafetyEquipment.NonSlipSurfaces);
                                                                 }
                                                             }); // END SAFETY EQUIPMENT LIST

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
                                                             this.setFireBurglaryAlarmGrade(
                                                                     FireBurglaryAlarmGrade.SecurityServiceWithTimingDevice);
                                                             this.setAlarmCertificate("Omni");

                                                             this.setInsuredPropertyWithin100Ft(false);
                                                             this.setInsuredPropertyWithin100FtPolicyHolderName("Bob the Builder");
                                                             this.setInsuredPropertyWithin100FtPolicyNumber("08-353652-01");
                                                             this.setAdditionalCoveragesStuff(
                                                                     new PolicyLocationBuildingAdditionalCoverages() {
                                                                         {
                                                                             this.setSpoilageCoverage(true);
                                                                             this.setSpoilageBreakdownContamination(true);
                                                                             this.setSpoilageLimit(SpoilageLimit.Five000);
                                                                             this.setSpoilagePowerOutage(true);
                                                                             this.setSpoilageProductDescription("Fish Food");
                                                                             this.setSpoilageRefrigerationMaintAgreement(true);
                                                                             this.setSpoilageRiskDescriptionClass(
                                                                                     SpoilageRiskClass.FruitsAndVegetables28);
                                                                         }
                                                                     });
                                                             this.setAdditionalInterestList(
                                                                     new ArrayList<AdditionalInterest>() {
                                                                         private static final long serialVersionUID = 1L;

                                                                         {
												new AdditionalInterest("Wells Fargo", null) {
                                                                                 {
                                                                                     this.setAdditionalInterestType(
                                                                                             AdditionalInterestType.Mortgagee);
                                                                                     this.setAddress(new AddressInfo(true));
                                                                                     this.setAppliedToBPP(true);
                                                                                     this.setAppliedToBuilding(true);
                                                                                     this.setCompanyName("Wells Fargo");
                                                                                     this.setCompanyOrInsured(ContactSubType.Company);
                                                                                     this.setFirstMortgage(true);
                                                                                     this.setLoanContractNumber("");
                                                                                     this.setLienholderNumber("");
                                                                                     this.setPersonFirstName("Jim");
                                                                                     this.setPersonLastName("Bowe");
                                                                                     this.setPhone("2082323231");
                                                                                     this.setSocialSecurityTaxNum("589625478");
                                                                                 }
                                                                             }; // END BUILDING ADDITIONAL
                                                                             // INTREST
                                                                         }
                                                                     }); // END BUILDING ADDITIONAL INTREST
                                                             // LIST
                                                         }
                                                     }); // END BUILDING
                                                 }
                                             } // END BUILDING LIST
                        ); // END SET BUILDING LIST
                    }
                });// END POLICY LOCATION
            }
        }; // END LOCATION LIST

        // END LOCATIONS

        System.out.println("GENERATE \n");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        myPolicyObj = new GeneratePolicy.Builder(driver)
                // .withAgentUserName(agentUsername)
                .withBusinessownersLine(boLine).withPolicyLocations(locationsLists)
                .withInsPersonOrCompanyRandom("Small", "Defects", "Small Defects")
                .withPolOrgType(OrganizationType.Partnership)
                // .withPolDuesCounty(CountyIdaho.Bonneville)
                // Membership Dues
                .withMembershipDuesOnPNI()
                .withMembershipDuesOnAllInsureds() // SET TO VERIFY MEMBERSHIP DUES IN AB
                .withInsPrimaryAddress(new AddressInfo(true))
                // .withPolTermLengthDays(51)
                // .withBusinessownersLine(new
                // PolicyBusinessownersLine(SmallBusinessType.Offices))
                .withPaymentPlanType(PaymentPlanType.Monthly).withDownPaymentType(PaymentType.Cash)
//				.withANIList(new ArrayList<PolicyInfoAdditionalNamedInsured>() {{
//					this.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Company));
//				}})
                .build(GeneratePolicyType.QuickQuote);

        System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentUserName());

    }

    // THIS TEST WILL FAIL UNTIL DE2665 IS COMPLETE. IS A LOW PRIORITY DEFECT SO
    // MAY BE A WHILE
    @Test(enabled = true, dependsOnMethods = {"generateQuickQuote"}, groups = {"FullApp",
            "Expiration Date"}, description = "When Term Type on Policy Info is set to \n Annual, Exparation date should default back to 12 Months.")
    public void expirationDateChange() throws Exception {
        boolean testPass = true;
        System.out.println("Expiration Date Change");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Map<ApplicationOrCenter, Date> datesMap = ClockUtils.getCurrentDates(driver);
        Date pcDate = datesMap.get(ApplicationOrCenter.PolicyCenter);

        new Login(driver).loginAndSearchJob(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(),
                myPolicyObj.accountNumber);

        GenericWorkorderPolicyInfo info = new GenericWorkorderPolicyInfo(driver);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();
        info.setPolicyInfoTermType("Other");

        Date eighteenMonths = DateUtils.dateAddSubtract(pcDate, DateAddSubtractOptions.Month, 18);
        info.setPolicyInfoExpirationDate(eighteenMonths);

        info = new GenericWorkorderPolicyInfo(driver);
        info.setPolicyInfoTermType("Annual");

        Date yearDate = DateUtils.dateAddSubtract(pcDate, DateAddSubtractOptions.Year, 1);
        if (!info.getPolicyInfoExpirationDate().equals(yearDate)) {
            testPass = false;

            Assert.fail(driver.getCurrentUrl() + "Policy exparation date did not revert to 1 Year when Term Type set to Annual.");
        }

        TopInfo topInfoStuff = new TopInfo(driver);
        topInfoStuff.clickTopInfoLogout();

    }

    //jlarsen 3/9/2017 commented out for test is unneeded. 
    //this functionality will be tested indirectly thru other tests.
//	// jlarsen 12/22/2015
//	// moved in from ReturnLinks.java class
//	@Test(dependsOnMethods = { "generateQuickQuote" }, enabled = true)
//	public void testReturnLinks() throws Exception {
//
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//
//		loginAndSearchJob(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(),myPolicyObj.accountNumber);
//
//		ISideMenu mySideMenu = SideMenuFactory.getMenu();
//		mySideMenu.clickSideMenuLocations();
//
//		GenericWorkorderLocations myGenWorkLoc = new GenericWorkorderLocations(driver);
//		myGenWorkLoc.clickLocationsLocationEdit(1);
//		//		myGenWorkLoc.clickReturnToLocations();
//		//		myGenWorkLoc.verifyPopupWarning();
//		//		mySideMenu = SideMenuFactory.getMenu();
//		mySideMenu.clickSideMenuBuildings();
//		//		GenericWorkorderBuildings myGenWorkBuild = new GenericWorkorderBuildings(driver);
//		myGenWorkBuild.clickBuildingsBuildingEdit(1);
//		//		myGenWorkBuild.clickReturnToBuildings();
//		//		myGenWorkBuild.verifyPopupWarning();
//	}

    @Test(enabled = true, dependsOnMethods = {"generateQuickQuote"}, groups = {"FullApp"}, description = "Runs Policy Through Full App")
    public void generateFullApp() throws Exception {

        System.out.println("FullApp \n");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj.convertTo(driver, GeneratePolicyType.FullApp);

    }


    @Test(enabled = true, dependsOnMethods = {
            "generateFullApp"}, description = "Verifies that nolonger get a null pointer when selecting Company or Person on Account Contacts Page")
    public void nullPointerAccountContactsPage() throws Exception {
        System.out.println("nullPointerAccountContactsPage \n");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(),
                myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        AccountsSideMenuPC account = new AccountsSideMenuPC(driver);
        account.clickContacts();

        AccountContactsPC contacts = new AccountContactsPC(driver);
        contacts.selectPersonCompany("Companies");
        if (contacts.finds(By.xpath("//div[contains(text(), 'NullPointerException: null')]"))
                .size() > 0) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber +
                    "Got NULL pointer when setting Contact Type on Accounts Contacts Page.");
        }

        contacts = new AccountContactsPC(driver);
        contacts.selectPersonCompany("Persons");
        if (contacts.finds(By.xpath("//div[contains(text(), 'NullPointerException: null')]"))
                .size() > 0) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber +
                    " Got NULL pointer when setting Contact Type on Accounts Contacts Page.");
        }
    }

    @Test(enabled = true, dependsOnMethods = {"generateFullApp"}, groups = {"FullApp",
            "PDDeductible"}, description = "Verifies if Specific Building Class Codes default PD Deductible set, uneditable, and $500.")
    public void verifyPDDeductible() throws Exception {

        System.out.println("verifyPDDeductible \n");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(),
                myPolicyObj.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBusinessownersLine();

        GenericWorkorderBusinessownersLineIncludedCoverages boLine = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
        if (boLine.isBusinessownersLineIncludedCoveragesPDDeductibleChecked()) {
            try {
                myPolicyObj.busOwnLine.setPdDeductibleChecked(true);
                boLine.filOutLiabilityCoverages(myPolicyObj.busOwnLine);
                throw new Exception("PD Deductible is not set for building class code: " + myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getClassCode());
            } catch (Exception e) {
            }
        }

        if (!boLine.getBusinessownersLineIncludedCoveragesPDDeductibleAmount().equals("500")) {
            throw new Exception("PD Deductible was not defaulted to $500");
        }
    }

    @Test(enabled = true, dependsOnMethods = {"generateFullApp"}, groups = {"FullApp"}, description = "To ensure that the bind button is not available prior to Monthly Payment Info being filled out.")
    public void monthlyPaymentBind_DE2634() throws Exception {
        System.out.println("Monthly Payment Bind_DE2634");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        // payments screen
        new GenericWorkorder(driver).clickGenericWorkorderQuote();

        SideMenuPC sideMenu = new SideMenuPC(driver);

        sideMenu.clickSideMenuRiskAnalysis();
        sideMenu.clickSideMenuPolicyReview();
        sideMenu.clickSideMenuQuote();
        sideMenu.clickSideMenuForms();
        sideMenu.clickSideMenuPayment();

        // verify Bind not Enabled
        GenericWorkorderPayment payment = new GenericWorkorderPayment(driver);
        try {
            payment.SubmitOnly();
            throw new Exception("Bind was available before payment information was entered");
        } catch (Exception e) {
        }

        // set to monthly payment
        payment.clickPaymentPlan(PaymentPlanType.Monthly);
        // add downpayment
        payment.clickAddDownPayment();
        payment = new GenericWorkorderPayment(driver);
        payment.setDownPaymentToStillOwing();
        payment.setDownPaymentType(myPolicyObj.downPaymentType);
        payment.clickOk();

        // verify Bind not Enabled
        try {
            payment.SubmitOnly();
            throw new Exception("Bind was available before payment information was entered");
        } catch (Exception e) {
        }

        // add Payment Info
        payment.clickAddPaymentInfo();
        payment = new GenericWorkorderPayment(driver);
        payment.setPaymentInfo(new BankAccountInfo());
        payment.clickOk();

        // verify Bind is available
        if (!payment.checkIfElementExists("//span[contains(@id, ':JobWizardToolbarButtonSet:BindOptions-btnEl')]/parent::span/parent::a[not(contains(@class, 'x-btn-disabled'))]", 5)) {
            throw new Exception("Bind Options is not available after payment information is filled out.");
        }

        payment.removeFirstDownPayment();
        myPolicyObj.paymentPlanType = PaymentPlanType.Annual;
        payment = new GenericWorkorderPayment(driver);
        payment.clickPaymentPlan(myPolicyObj.paymentPlanType);
        GenericWorkorder wo = new GenericWorkorder(driver);
        wo.clickGenericWorkorderSaveDraft();
    }

    @Test(enabled = true, dependsOnMethods = {"generateFullApp"}, description = "Processes policy through Policy Bound")
    public void generatePolicyBound() throws Exception {
        System.out.println("PolicyBound \n");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj.convertTo(driver, GeneratePolicyType.PolicySubmitted);
    }

    @Test(enabled = true, dependsOnMethods = {"generatePolicyBound"}, description = "Issues Policy")
    public void generatePolicyIssued() throws Exception {

        System.out.println("Policy Issued \n");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj.convertTo(driver, GeneratePolicyType.PolicyIssued);
    }

    @Test(enabled = true, dependsOnMethods = {"generatePolicyIssued", "verifyPDDeductible"}, groups = {
            "FullApp",
            "PDDeductible"}, description = "Verifies that user is alerted if building class code no longer requires PD Deductible")
    public void changeClassCode() throws Exception {
        Boolean testFailed = false;

        System.out.println("changeClassCode \n");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Map<ApplicationOrCenter, Date> datesMap = ClockUtils.getCurrentDates(driver);
        Date pcDate = datesMap.get(ApplicationOrCenter.PolicyCenter);

        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(),
                myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        StartPolicyChange change = new StartPolicyChange(driver);
        change.startPolicyChange("Change PD Deductible", pcDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
        building.clickBuildingsBuildingEdit(1);
        building.selectFirstBuildingCodeResult(BuildingClassCode.AntiqueStores.getClassification());
        building.clickOK();

        // verify validation results
        ErrorHandlingHelpers error = new ErrorHandlingHelpers(driver);
        try {
            if (error.isValidationResults()) {
                ErrorHandling handling = new ErrorHandling(driver);
                handling.button_Clear().click();
            } else {
                testFailed = true;
            }
        } catch (Exception e) {
            testFailed = true;
        }

        building.clickOK();

        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

		new GenericWorkorder(driver).clickGenericWorkorderQuote();

        // Wait time for policy to Quote
        long stopTime = new Date().getTime() + (120 * 1000);
        do {
        }
        while (new GuidewireHelpers(driver).finds(By.xpath("//span[contains(@id, '_QuoteScreen:ttlBar')]")).size() <= 0 && new Date().getTime() < stopTime);

        GenericWorkorder workOrder = new GenericWorkorder(driver);
        workOrder.clickWithdrawTransaction();
        try {
            workOrder.selectOKOrCancelFromPopup(OkCancel.OK);
        } catch (Exception e) {
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "No Validation Error was thrown when changing to a \n builidng class code that nolonger requires \n PD Deductible.");
        }
    }

    @Test(enabled = true, dependsOnMethods = {
            "generatePolicyIssued"}, groups = {}, description = "Verify Membership Dues sent to Contact Manager")
    public void verifyMembershipDuesAB() throws Exception {
        if (myPolicyObj.pniContact.getPersonOrCompany() != null && myPolicyObj.pniContact.getPersonOrCompany().equals(ContactSubType.Person)) {
            System.out.println("Verify Membership Dues");

            Config cf = new Config(ApplicationOrCenter.ContactManager);
            driver = buildDriver(cf);

            Login login = new Login(driver);
            login.login("esorensen", "gw");

            TopMenuAB topMenu = new TopMenuAB(driver);
            topMenu.clickSearchTab();

            SidebarAB sidebar = new SidebarAB(driver);
            sidebar.clickSidebarAdvancedSearchLink();

            AdvancedSearchAB advSearch = new AdvancedSearchAB(driver);
            if (myPolicyObj.pniContact.getCompanyName() == null || myPolicyObj.pniContact.getCompanyName() == "") {
                advSearch.searchByFirstLastName(myPolicyObj.pniContact.getFirstName(), myPolicyObj.pniContact.getLastName(),
                        myPolicyObj.pniContact.getAddress().getLine1());
                ContactDetailsBasicsAB details = new ContactDetailsBasicsAB(driver);
                details.clickContactDetailsBasicsPaidDuesLink();
                ContactDetailsPaidDuesAB paidDues = new ContactDetailsPaidDuesAB(driver);
                List<WebElement> paidDuesList = paidDues.getPaidDuesList();
                if (paidDuesList.size() <= 0) {
                    Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber +
                            " Membership Dues did not correctly transfer to Contact Manager.");
                }
            } else {
                System.out.println("Membership Dues check Currently only available for Policy Person");
            }
        } else {
            throw new SkipException("verifyContactIssuanceAB because Policy created was a Company.");
        }
    }

    @Test(enabled = true, dependsOnMethods = {"generateQuickQuote"}, groups = {}, description = "Verify that contact was sent to Contact Manager")
    public void verifyContactIssuanceAB() throws Exception {
        System.out.println("Verify Contact Issuance AB");

        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("esorensen", "gw");

        TopMenuAB topMenu = new TopMenuAB(driver);
        topMenu.clickSearchTab();

        SidebarAB sidebar = new SidebarAB(driver);
        sidebar.clickSidebarAdvancedSearchLink();

        AdvancedSearchAB advSearch = new AdvancedSearchAB(driver);
        if (myPolicyObj.pniContact.getPersonOrCompany().equals(ContactSubType.Person)) {
            advSearch.searchByFirstLastName(myPolicyObj.pniContact.getFirstName(), myPolicyObj.pniContact.getLastName(), myPolicyObj.pniContact.getAddress().getLine1());
            ContactDetailsBasicsAB details = new ContactDetailsBasicsAB(driver);
            if (!details.getContactPageTitle().contains(myPolicyObj.pniContact.getFirstName()) || !details.getContactPageTitle().contains(myPolicyObj.pniContact.getLastName())) {
                throw new Exception("Contact Name did not correctly transfer to Contact Manager.");
            }
        } else {
            advSearch.searchCompanyByName(myPolicyObj.pniContact.getCompanyName(), myPolicyObj.pniContact.getAddress().getLine1(), myPolicyObj.pniContact.getAddress().getState());
            ContactDetailsBasicsAB details = new ContactDetailsBasicsAB(driver);
            if (!details.getContactPageTitle().contains(myPolicyObj.pniContact.getCompanyName())) {
                throw new Exception("Contact Name did not correctly transfer to Contact Manager.");
            }
        }
    }

    // THIS TEST IS TO VERIFY THAT PD DEDUCTIBLE IS BEING CHARGED FOR CORRECTLY
    @Test(enabled = true, dependsOnMethods = {"generatePolicyIssued"})
    public void verifyPDDeductibleCharges() throws Exception {
        Boolean testFailed = false;
        System.out.println("verify PD Deductible charges \n");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Map<ApplicationOrCenter, Date> datesMap = ClockUtils.getCurrentDates(driver);
        Date pcDate = datesMap.get(ApplicationOrCenter.PolicyCenter);

        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(),
                myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        StartPolicyChange change = new StartPolicyChange(driver);
        change.startPolicyChange("Change PD Deductible", pcDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
        building.clickBuildingsBuildingEdit(1);
        building.selectFirstBuildingCodeResult(BuildingClassCode.AntiqueStores.getClassification());
        building.clickOK();

        ErrorHandlingHelpers error = new ErrorHandlingHelpers(driver);
        try {
            if (error.isValidationResults()) {
                ErrorHandling handling = new ErrorHandling(driver);
                handling.button_Clear().click();
            }
        } catch (Exception e) {
        }

        building.clickOK();

        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBusinessownersLine();

        GenericWorkorderBusinessownersLineIncludedCoverages boLine = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
        myPolicyObj.busOwnLine.setPdDeductibleChecked(false);
        boLine.filOutLiabilityCoverages(myPolicyObj.busOwnLine);

        new GenericWorkorder(driver).clickGenericWorkorderQuote();

        // Wait time for policy to Quote
        long stopTime = new Date().getTime() + (120 * 1000);
        do {
        }
        while (new GuidewireHelpers(driver).finds(By.xpath("//span[contains(@id, '_QuoteScreen:ttlBar')]")).size() <= 0 && new Date().getTime() < stopTime);

        sideMenu.clickSideMenuQuote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        System.out.println(quote.getDescriptionValue("PD Deductible"));
        if (!quote.getDescriptionValue("PD Deductible").equalsIgnoreCase("Decline")) {
            testFailed = true;
        }

        GenericWorkorder workOrder = new GenericWorkorder(driver);
        workOrder.clickWithdrawTransaction();
        try {
            workOrder.selectOKOrCancelFromPopup(OkCancel.OK);
        } catch (Exception e) {
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "Policy is charging for PD Deductible when is should not be.");
        }
    }

    // jlarsen 12/22/2015
    // disabled due to unknown failure with selenium
    @Test(enabled = false, dependsOnMethods = {"generatePolicyIssued"})
    public void verifySSNSearchResults() throws Exception {

        System.out.println("Running verifySSNSearchResults");
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).login("hhill", "gw");

        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickSearchTab();

        SearchAccountsPC search = new SearchAccountsPC(driver);
        search.clickReset();

        if (myPolicyObj.pniContact.getPersonOrCompany() != null) {
            switch (myPolicyObj.pniContact.getPersonOrCompany().getValue()) {
                case "Person":
                    search.setSSNTIN(myPolicyObj.pniContact.getSocialSecurityNumber(), TaxReportingOption.SSN);
                    break;
                case "Company":
                    search.setSSNTIN(myPolicyObj.pniContact.getTaxIDNumber(), TaxReportingOption.TIN);
                    break;
            }
            search.clickSearch();
            if (search.finds(By.xpath("//div[contains(@class, 'message') and contains(text(), 'The search returned zero results')]")).size() > 0) {
                Assert.fail("Search for SSN/TIN, returned zero results.");
            }
        } else {
            Assert.fail(driver.getCurrentUrl() + "insPersonOrCompnay was set to NULL. check Console output to find out why" + myPolicyObj.accountNumber);
        }
    }

    @Test(description = "Test Renewal Paperwork Activity", dependsOnMethods = "generatePolicyIssued")
    public void testRenewalActivity() throws Exception {

        ActivtyRequestType activityRequest = ActivtyRequestType.PleaseReturnRenewalPaperwork;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);

        NewActivityRequestPC requestActivity = new NewActivityRequestPC(driver);
        requestActivity.initiateActivity(activityRequest);

        GenericActivityPC newActivity = new GenericActivityPC(driver);
        String assignTo = newActivity.getAssignTo();
        String[] assignToParts = assignTo.split(" ");
        System.out.println("assignToParts[0] should equal: " + assignToParts[0] + " which should equal:" + myPolicyObj.agentInfo.getAgentFirstName());
        System.out.println("assignToParts[1] should equal: " + assignToParts[1] + " which should equal:" + myPolicyObj.agentInfo.getAgentLastName());
        if (!assignToParts[0].equals(myPolicyObj.agentInfo.getAgentFirstName()) && !assignToParts[1].equals(myPolicyObj.agentInfo.getAgentLastName())) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "The default Assign To field defaults to someone other than the agent.");
        }
//		String escalationDate = newActivity.getEscalationDate();
        String targetDate = newActivity.getTargetDueDate();
        int targetDays = Integer.valueOf(ActivityPatternsHelper.getActivityBySubject(activityRequest.getValue()).getTargetDays());
        DateAddSubtractOptions targetDayType = (ActivityPatternsHelper.getActivityBySubject(activityRequest.getValue()).getTargetDaysType().equals("Calendar days")) ? DateAddSubtractOptions.Day : DateAddSubtractOptions.BusinessDay;
//		String escalationDateCalc = DateUtils.dateFormatAsString("MM/dd/yyyy",	DateUtils.dateAddSubtract(this.currentDate, DateAddSubtractOptions.BusinessDay, 20));
        String targetDateCalc = DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(this.currentDate, targetDayType, targetDays));

        //TODO Commented out this and above because Escalation Date is no longer editable. With no notes, this should be fixed by someone who knows what it should be or removed. Note Added by Bmartin on 7/6/2016
		/*if (!escalationDate.equals(escalationDateCalc)) {
			throw new GuidewirePolicyCenterException(Configuration.getWebDriver().getCurrentUrl(),	myPolicyObj.accountNumber, "The Escalation Date did not match 20 business days in the future.");
		}*/
        if (!targetDate.equals(targetDateCalc)) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + " The Target Date did not match " + targetDays + " business days in the future.");
        }

        newActivity.fillOutActivity(myPolicyObj.agentInfo.getAgentUserName(), activityRequest.getValue(), activityRequest.getValue());
    }

}
