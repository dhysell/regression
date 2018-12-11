package previousProgramIncrement.pi1_041918_062718.f58_QuickerQuickQuote_ReduceRequiredFieldsPL;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.QuoteType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.Vehicle.CommutingMiles;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.actions.ActionsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_PersonalProperty;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderStandardIMCoverageSelection;
import repository.pc.workorders.generic.GenericWorkorderStandardIMFarmEquipment;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import repository.pc.workorders.submission.SubmissionProductSelection;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.VINHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * @Author nvadlamudi
 * @Requirement:US14788: Unrequire fields and add default values on: Squire QQ,
 * Sections III & IV and Risk Analysis
 * US14784: Unrequire fields and add default values on:  Squire QQ,
 * Sections I & II
 * US14786: Unrequire fields and add default values on: Standard Fire
 * US14789: Unrequire fields and add default values on: Standard Liability and Standard Inland Marine
 * @RequirementsLink <a href=
 * "https://fbmicoi-my.sharepoint.com/:x:/g/personal/tharrild_idfbins_com/EfcFrKIa1gpGjJw1Pyhco44Bly9-OkewH8elKGiCMPqAQA?e=i0jMiT">
 * PL QQ Fields to UnRequire and Fields to Default</a>
 * @Description: Validating default values in all sections and all PL lines.
 * Acceptance criteria: Ensure that the fields identified via
 * US14794 default with the appropriate values Ensure that the QQ
 * can still proceed as normal
 * US14784 acceptance Criteria:
 * Ensure that the fields identified via US14787 are not required on QQ.
 * Ensure that the fields identified via US14794 default with the appropriate values.
 * Ensure that the QQ can still proceed as normal.
 * @DATE May 8, 2018
 */
public class PLLinesQQDefaultValues extends BaseTest {
    private String accountNumber;
    AddressInfo newAddress;
    private String insLastName = "DefaultVal" + StringsUtils.generateRandomNumberDigits(9);
    private GeneratePolicy myQQPolObj;
    private WebDriver driver;

    @Test
    public void testSquireDefaultValuesInQQ() throws Exception {
    	 createAccountAndSelectLine(ProductLineType.Squire);
         addLineSelectionEligibility();
         checkSectionIIIDefaultValues();
         checkSectionILocationsDefaultValues(false);
         checkSectionIPropertyDetailsCoveragesDefaultValues(PropertyTypePL.ResidencePremises);
         checkSectionIIDefaultValues(ProductLineType.Squire);
         checkSectionIIIAfterSectionIAndIIChanges();         
    }
    
    
    @Test
    public void testStandardFireDefaultValuesInQQ() throws Exception {
    	createAccountAndSelectLine(ProductLineType.StandardFire);
        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
        eligibilityPage.setPolicySelection(true);
        checkSectionILocationsDefaultValues(false);
        checkSectionIPropertyDetailsCoveragesDefaultValues(PropertyTypePL.DwellingPremises);
    }
   
    @Test
    public void testStandardIMDefaultValuesInQQ() throws Exception {
    	createAccountAndSelectLine(ProductLineType.StandardIM);
        checkSectionIVIMDefaultValues();
    }
    
    @Test
    public void testStandardLiabDefaultValuesInQQ() throws Exception {
         createAccountAndSelectLine(ProductLineType.StandardLiability);
         GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
         eligibilityPage.setPolicySelection(false);
         checkSectionILocationsDefaultValues(false);
         checkSectionIIDefaultValues(ProductLineType.StandardLiability);
    }
    
    @Test
    public void testStandardFireCommodDefaultValuesInQQ() throws Exception {
          testCreateSquireQQ();
          GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
          eligibilityPage.setPolicySelection(true);
          eligibilityPage.setStandardFirewithCommodity(true);
          checkSectionILocationsDefaultValues(true);
   }
    

    private void checkSectionIVIMDefaultValues() throws Exception {
        boolean testPassed = true;
        String errorMessage = "Section IV - Default Values";
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);
        policyInfo.setPolicyInfoRatingCounty("Bannock");
        sideMenu.clickSideMenuStandardIMCoverageSelection();
        GenericWorkorderStandardIMCoverageSelection imSelection = new GenericWorkorderStandardIMCoverageSelection(driver);
        imSelection.checkCoverage(true, InlandMarine.FarmEquipment.getValue());
        imSelection.checkCoverage(true, InlandMarine.PersonalProperty.getValue());

        //Farm Equipment	Deductible		Default to 250
        sideMenu.clickSideMenuStandardIMFarmEquipment();
        GenericWorkorderStandardIMFarmEquipment farmEquipment = new GenericWorkorderStandardIMFarmEquipment(driver);
        farmEquipment.clickAdd();

        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);
        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);

        if (!farmEquipment.getDeductible().equals(IMFarmEquipmentDeductible.TwoHundredFifty.getValue())) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Standard IM -  Farm Equipment default value '" + IMFarmEquipmentDeductible.TwoHundredFifty.getValue() + "' is not displayed in QQ. \n";

        }
        farmEquipment.setType(imFarmEquip.getIMFarmEquipmentType());
        farmEquipment.setListScheduledItem(imFarmEquip.getScheduledFarmEquipment());
        farmEquipment.clickOK();
        //Personal Property	Deductible		Default to 250
        sideMenu.clickSideMenuStandardIMPersonalProperty();
        GenericWorkorderSquireInlandMarine_PersonalProperty personalPropertyPage = new GenericWorkorderSquireInlandMarine_PersonalProperty(driver);
        personalPropertyPage.clickAdd();
        if (!personalPropertyPage.getDeductible().equals(PersonalPropertyDeductible.Ded250.getValue())) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Standard IM - Personal Property default value '" + PersonalPropertyDeductible.Ded250.getValue() + "' is not displayed in QQ. \n";
        }
        Assert.assertTrue(testPassed, "Account Number: '" + accountNumber + "' - Section I Failed Default Values:  " + errorMessage + ".");


    }

    private void createAccountAndSelectLine(ProductLineType lineType) throws Exception {
        Agents agent = AgentsHelper.getRandomAgent();
        AddressInfo address = new AddressInfo();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());

        TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
        menuPolicy.clickNewSubmission();

        SubmissionNewSubmission newSubmissionPage = new SubmissionNewSubmission(driver);
        newSubmissionPage.selectAdvancedSearchSubmission();		
        newSubmissionPage.fillOutFormSearchCreateNewWithoutStamp(true, ContactSubType.Person,
                insLastName,
                "QQ" + StringsUtils.generateRandomNumberDigits(7), null, null, address.getCity(), address.getState(),
                address.getZip());
        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
        createAccountPage.fillOutPrimaryAddressFields(address);
        createAccountPage.setSubmissionCreateAccountBasicsAltID(StringsUtils.generateRandomNumberDigits(12));
        createAccountPage.setDOB(DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -35));
        createAccountPage.clickCreateAccountUpdate();
        SubmissionProductSelection selectProductPage = new SubmissionProductSelection(driver);
        accountNumber = selectProductPage.startQuoteSelectProductAndGetAccountNumber(QuoteType.QuickQuote, lineType);
    }

    private void addLineSelectionEligibility() throws Exception {
        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
        eligibilityPage.chooseCity();
        eligibilityPage.clickNext();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
        policyInfoPage.setPolicyInfoRatingCounty("Bannock");
        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
    }

    private void checkSectionILocationsDefaultValues(Boolean commodities) throws Exception {
        boolean testPassed = true;
        String errorMessage = "Section I Locations - Default Values";
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation pLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        pLocations.clickNewLocation();
        newAddress = new AddressInfo(true);
        pLocations.selectLocationAddress("New");
        //Acres		Default to 1
        if (pLocations.getAcres() != 1) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section I - Location Acres default value '1' is not displayed in QQ. \n";
        }

        if (commodities && pLocations.getNumberOfResidence() != 0) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section I - Location Residences default value '0' is not displayed in QQ. \n";
        }

        if (!commodities && pLocations.getNumberOfResidence() != 1) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section I - Location Residences default value '1' is not displayed in QQ. \n";
        }
        pLocations.addLocationInfoFA(newAddress, 1, 1);

        pLocations.clickStandardizeAddress();
        pLocations.clickOK();

        Assert.assertTrue(testPassed, "Account Number: '" + accountNumber + "' - Section I Failed Default Values:  " + errorMessage + ".");

    }

    private void checkSectionIPropertyDetailsCoveragesDefaultValues(PropertyTypePL propertyType) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        boolean testPassed = true;
        String errorMessage = "Section I PropertyDetails and Coverages - Default Values";

        //Property Information, Details	# of units in building		default to 1
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickAdd();
        propertyDetail.setPropertyType(propertyType);
        if (!propertyDetail.getUnits().equals(NumberOfUnits.OneUnit.getValue())) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section I - Property details # of Units in building default value '1 Unit' is not displayed in QQ. \n";
        }

        //Wood fireplace or wood stove		default to no
        if (propertyType.equals(PropertyTypePL.ResidencePremises) && !propertyDetail.getWoodFireplaceRadio(false)) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section I - Property details Wood Fireplace or Wood Stove default value 'no' is not displayed in QQ. \n";
        }
        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.setYearBuilt((DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy") - 2));
        constructionPage.setConstructionType(ConstructionTypePL.Frame);
        constructionPage.setSquareFootage(1200);
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);

        if (propertyType.equals(PropertyTypePL.ResidencePremises)) {
            constructionPage.clickProtectionDetailsTab();
            //Property Information, Protection Details	Smoke Alarms		default to no

            if (!protectionPage.getSmokeAlarm(false)) {
                testPassed = false;
                errorMessage = errorMessage + "Expected Section I - Protection Details Smoke Alrm default value 'no' is not displayed in QQ. \n";
            }
            //Non Smoker		default to no
            if (!protectionPage.getNonSmoker(false)) {
                testPassed = false;
                errorMessage = errorMessage + "Expected Section I - Protection Details Non Smoker default value 'no' is not displayed in QQ. \n";
            }

            //Dead bolt locks		default to no
            if (!protectionPage.getDeadBoltLocks(false)) {
                testPassed = false;
                errorMessage = errorMessage + "Expected Section I - Protection Details Dead bolt locks default value 'no' is not displayed in QQ. \n";
            }
        }

        protectionPage.clickOK();
        
        //adding another click OK button for photo year validation
        protectionPage.clickOK();

        //Coverages, Prop Detail Cov, Coverages	Deductible		Default to 1000
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        if (!coverages.getSquirePropertyCoveragesSectionIDeductible().equals(SectionIDeductible.OneThousand.getValue())) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section I - Coverages Deductible default value '" + SectionIDeductible.OneThousand.getValue() + "' is not displayed in QQ. \n";
        }

        if (propertyType.equals(PropertyTypePL.ResidencePremises)) {
            //Coverage A Valuation Method		Default to Replacement Cost
            if (!coverages.getCoverageAValuation().equals(ValuationMethod.ReplacementCost.getValue())) {
                testPassed = false;
                errorMessage = errorMessage + "Expected Section I - Coverages - Coverage A Valuation Method - default value '" + ValuationMethod.ReplacementCost.getValue() + "' is not displayed in QQ. \n";
            }

            //Coverage A Coverage Type		Default to Special Form
            if (!coverages.getCoverageACoverageType().equals(CoverageType.SpecialForm.getValue())) {
                testPassed = false;
                errorMessage = errorMessage + "Expected Section I - Coverages - Coverage A Coverage Type - default value '" + CoverageType.SpecialForm.getValue() + "' is not displayed in QQ. \n";
            }

            //Coverage C Coverage Type		default to match Coverage A
            if (!coverages.getCoverageCCoverageType().equals(CoverageType.SpecialForm.getValue())) {
                testPassed = false;
                errorMessage = errorMessage + "Expected Section I - Coverages - Coverage C Coverage Type - default value '" + CoverageType.SpecialForm.getValue() + "' is not displayed in QQ. \n";
            }
        }

        //Coverage C Valuation Method		default to match Coverage A
        if (!coverages.getCoverageCValuation().equals(ValuationMethod.ReplacementCost.getValue())) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section I - Coverages - Coverage C Valuation Method - default value '" + ValuationMethod.ReplacementCost.getValue() + "' is not displayed in QQ. \n";
        }

        coverages.setCoverageALimit(120000);

        Assert.assertTrue(testPassed, "Account Number: '" + accountNumber + "' - Section I Failed Default Values:  " + errorMessage + ".");

    }

    private void checkSectionIIDefaultValues(ProductLineType productType) throws Exception {
        boolean testPassed = true;
        String errorMessage = "Section II - Default Values: ";
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages addCoverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);

        //Coverages, Section II Coverages	GL Limit		Default to 100000 CSL
        if (productType.equals(ProductLineType.Squire)) {
            coverages.clickSectionIICoveragesTab();
            if (!addCoverage.getGeneralLiabilityLimit().equals(LiabilityLimit.FiftyHigh.getValue())) {
                testPassed = false;
                errorMessage = errorMessage + "Expected Section II - Liability Limit  default value : '" + LiabilityLimit.FiftyHigh.getValue()
                        + "' is not displayed in QQ. /n";
            }
        } else {
            if (!addCoverage.getGeneralLiabilityLimit().equals(LiabilityLimit.CSL100K.getValue())) {
                testPassed = false;
                errorMessage = errorMessage + "Expected Liability Limit  default value : '" + LiabilityLimit.CSL100K.getValue()
                        + "' is not displayed in QQ. /n";
            }
        }

        //Medical Limit		Default to 2000
        if (!addCoverage.getMedicalLimit().equals(MedicalLimit.TwoK.getValue())) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section II - Medical Limit default value: '" + MedicalLimit.TwoK.getValue()
                    + "' is not displayed in QQ. /n";
        }
        Assert.assertTrue(testPassed, "Account Number: '" + accountNumber + "' - Section II Failed Default Values:  " + errorMessage + ".");

    }

    private void checkSectionIIIDefaultValues() throws Exception {
        boolean testPassed = true;
        String errorMessage = "Squire - Section III - Default Values: ";
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new GenericWorkorderSquireAutoCoverages_Coverages(driver);

        //Section III, Coverages, Coverages	Liability		default to 50/100/50
        if (!coveragePage.getLiabilityLimit().equals(LiabilityLimit.FiftyHigh.getValue())) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section III - Liability Limit default value '" + LiabilityLimit.FiftyHigh.getValue() + "' is not displayed in QQ. \n";
        }

        //Medical		Default to 2000
        if (!coveragePage.getMedicalLimit().equals(MedicalLimit.TwoK.getValue())) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section III - Medical Limit default value '" + MedicalLimit.TwoK.getValue() + "' is not displayed in QQ. \n";
        }

        //Uninsured Motorist		default to 50/100
        if (!coveragePage.getUninsuredMotoristBodilyInjury().equals(UninsuredLimit.Fifty.getValue())) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section III - Uninsured Motorist  default value '" + UninsuredLimit.Fifty.getValue() + "' is not displayed in QQ. \n";
        }

        //Underinsured Motorist		default to 50/100
        if (!coveragePage.getUnderInsuredMotoristBodilyInjury().equals(UnderinsuredLimit.Fifty.getValue())) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section III - Underinsured Motorist  default value '" + UnderinsuredLimit.Fifty.getValue() + "' is not displayed in QQ. \n";
        }

        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        //Section III, Vehicles, Details	Vehicle type		Default to Private Passenger
        vehiclePage.createVehicleManually();
        Vin vin = VINHelper.getRandomVIN();
        vehiclePage.setVIN(vin.getVin());
        if (!vehiclePage.getVehicleType().equals(VehicleType.PrivatePassenger.getValue())) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section III - Vehicle Type default value '" + VehicleType.PrivatePassenger.getValue() + "' is not displayed in QQ. \n";
        }

        //Commuting Miles		Default to Work/School 3-10 miles
        if (!vehiclePage.getVehicleCommutingMiles().equals(CommutingMiles.WorkSchool3To10Miles.getValue())) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section III - Commuting Miles default value '" + CommutingMiles.WorkSchool3To10Miles.getValue() + "' is not displayed in QQ. \n";
        }

        vehiclePage.clickGenericWorkorderCancel();
        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

        Assert.assertTrue(testPassed, "Account Number: '" + accountNumber + "' - Section III Failed Default Values:  " + errorMessage + ".");

    }


    private void checkSectionIIIAfterSectionIAndIIChanges() throws Exception {
        boolean testPassed = true;
        String errorMessage = "Section III - After Section I & II - Default Values: ";
        SideMenuPC sideMenu = new SideMenuPC(driver);

        //Garaged At		Default to primary location
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.createVehicleManually();
        Vin vin = VINHelper.getRandomVIN();
        vehiclePage.setVIN(vin.getVin());
        if (!vehiclePage.getGarageAt().contains(newAddress.getLine1())) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section III - GarageAt default value '" + newAddress.getLine1() + "' is not displayed in QQ. \n";
        }
        vehiclePage.setModelYear(2002);
        vehiclePage.setMake("honda");
        vehiclePage.setModel("accord");
        new GuidewireHelpers(driver).sendArbitraryKeys(Keys.TAB);
        vehiclePage.setNoDriverAssigned(true);
        vehiclePage.clickOK();
        sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new GenericWorkorderSquireAutoCoverages_Coverages(driver);

        //Section III, Coverages, Coverages	Liability		default to 50/100/50
        if (!coveragePage.getLiabilityLimit().equals(LiabilityLimit.FiftyHigh.getValue())) {
            testPassed = false;
            errorMessage = errorMessage + "Expected Section III - Liability Limit default value '" + LiabilityLimit.FiftyHigh.getValue() + "' is not displayed in QQ. \n";
        }

        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
        Assert.assertTrue(testPassed, "Account Number: '" + accountNumber + "' - Section III Failed Default Values:  " + errorMessage + ".");

    }

    private void testCreateSquireQQ() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(12);
        locationsList.add(locToAdd);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myQQPolObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("SQQQ", "US14786")
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .build(GeneratePolicyType.QuickQuote);
        accountNumber = myQQPolObj.accountNumber;

        new Login(driver).loginAndSearchAccountByAccountNumber(myQQPolObj.agentInfo.getAgentUserName(), myQQPolObj.agentInfo.getAgentPassword(), accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_NewSubmission();
        SubmissionProductSelection selectProductPage = new SubmissionProductSelection(driver);
        selectProductPage.startQuoteSelectProductAndGetAccountNumber(QuoteType.QuickQuote, ProductLineType.StandardFire);

    }

}
