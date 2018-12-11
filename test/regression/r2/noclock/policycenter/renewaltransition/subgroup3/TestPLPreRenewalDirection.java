package regression.r2.noclock.policycenter.renewaltransition.subgroup3;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.CommuteType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GenerateCheckType;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.NotRatedReason;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyScheduledItemType;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.Property.SectionIIMedicalLimit;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.enums.Vehicle.VehicleTruckTypePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.cc.GenerateCheck;
import repository.gw.generate.cc.GenerateExposure;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.generate.cc.GenerateReserve;
import repository.gw.generate.cc.ReserveLine;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireFPPTypeItem;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US4767: PL - Pre-renewal Direction
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Homeowners/PC8%20-%20HO%20-%20Renewal%20-%20Pre-renewal%20Direction.xlsx">PC8 - HO - Renewal - Pre-renewal Direction</a>
 * @Description
 * @DATE Jul 28, 2016
 */
@QuarantineClass
public class TestPLPreRenewalDirection extends BaseTest {
    private GeneratePolicy myPolicyObjPL;
    private String squireActivitySubject = "Pre-Renewal Review for Squire";
    private Underwriters uw;
    private GeneratePolicy myClaimPolicyObjPL;
    private WebDriver driver;


    @Test(dependsOnMethods = {"testIssueSquirePolWithRules"})
    private void testPreRenewalDirection() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        // Login with UW
        this.uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myPolicyObjPL.accountNumber);

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();
        sideMenu.clickSideMenuPolicyInfo();

        new GuidewireHelpers(driver).editPolicyTransaction();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
        Boolean testFailed = false;
        String errorMessage = "";
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByPolicyNumber(myPolicyObjPL.squire.getPolicyNumber());
        PolicySummary summaryPage = new PolicySummary(driver);
        if (summaryPage.checkIfActivityExists(squireActivitySubject)) {
            summaryPage.clickViewPreRenewalDirection();
            PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
            boolean preRenewalDirectionExists = preRenewalPage.checkPreRenewalExists();
            if (!preRenewalDirectionExists) {
                Assert.fail("An Activity was found with no preRenewal Direction.");
            } else {
                preRenewalPage.clickViewInPreRenewalDirection();
                String[] preRenewalExplanations = {"Vacant Property",
                        "Rating County and Primary Location County Mismatch", "Manual Protection Class",
                        "Follow Up With Agent for Custom Farming", "Not Rated Driver with Early Follow-up Date",
                        "Coverage Limit of Over $1 Million", "Vehicle with SRP 21 or Over", "Override Category Code",
                        "Category code valuation over $10 Million", "Marshall and Swift Needed",
                        "Property Photos Needed", "Jewelry Photos Required",
                        "Follow Up With Agent for Farm Truck/Show Car"};

                for (String currentExplanation : preRenewalExplanations) {
                    if (!preRenewalPage.checkPreRenewalDirectionWithExpectedCode(currentExplanation)) {
                        testFailed = true;
                        errorMessage = errorMessage + "Expected Explanation Code: " + currentExplanation
                                + " is not displayed.\n";
                    }
                }
            }
        } else
            Assert.fail("Expected Activity " + squireActivitySubject + " is not displayed.");

        if (testFailed)
            Assert.fail(errorMessage);
    }

    @Test(dependsOnMethods = {"testIssueUmbrellaPolicy"})
    private void testPeRenewalTestForClaims() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        // Login with UW
        this.uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.myClaimPolicyObjPL.accountNumber);

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        sideMenu.clickSideMenuPolicyInfo();
        guidewireHelpers.editPolicyTransaction();

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

        Boolean testFailed = false;
        String errorMessage = "";
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByPolicyNumber(this.myClaimPolicyObjPL.squire.getPolicyNumber());
        PolicySummary summaryPage = new PolicySummary(driver);

        if (summaryPage.checkIfActivityExists(squireActivitySubject)) {
            summaryPage.clickViewPreRenewalDirection();
            PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
            boolean preRenewalDirectionExists = preRenewalPage.checkPreRenewalExists();
            if (!preRenewalDirectionExists) {
                Assert.fail("An Activity was found with no preRenewal Direction.");
            } else {
                preRenewalPage.clickViewInPreRenewalDirection();
                String[] preRenewalExplanations = {"Claim of $50,000 or More"};

                for (String currentExplanation : preRenewalExplanations) {
                    if (!preRenewalPage.checkPreRenewalDirectionWithExpectedCode(currentExplanation)) {
                        testFailed = true;
                        errorMessage = errorMessage + "Expected Explanation Code: " + currentExplanation
                                + " is not displayed.\n";
                    }
                }
            }
        } else
            Assert.fail("Expected Activity " + squireActivitySubject + " is not displayed.");

        policySearchPC.searchPolicyByPolicyNumber(this.myClaimPolicyObjPL.squireUmbrellaInfo.getPolicyNumber());
        policyMenu.clickRenewPolicy();
        policySearchPC.searchPolicyByPolicyNumber(this.myClaimPolicyObjPL.squireUmbrellaInfo.getPolicyNumber());
        if (summaryPage.checkIfActivityExists("Pre-Renewal Review for Personal Umbrella")) {
            summaryPage.clickViewPreRenewalDirection();
            PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
            boolean preRenewalDirectionExists = preRenewalPage.checkPreRenewalExists();
            if (!preRenewalDirectionExists) {
                Assert.fail("An Activity was found with no preRenewal Direction.");
            } else {
                preRenewalPage.clickViewInPreRenewalDirection();
                String[] preRenewalExplanations = {"Limit Over $5 Million"};

                for (String currentExplanation : preRenewalExplanations) {
                    if (!preRenewalPage.checkPreRenewalDirectionWithExpectedCode(currentExplanation)) {
                        testFailed = true;
                        errorMessage = errorMessage + "Expected Explanation Code: " + currentExplanation
                                + " is not displayed.\n";
                    }
                }
            }
        } else
            Assert.fail("Expected Activity 'Pre-Renewal Review for Personal Umbrella' is not displayed.");

        if (testFailed)
            Assert.fail(errorMessage);
    }

    @Test()
    private void testIssueClaimsChecks() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_500000_CSL);
        myLiab.setMedicalLimit(SectionIIMedicalLimit.Limit_10000);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        SquirePersonalAutoCoverages autoCoverages = new SquirePersonalAutoCoverages();
        autoCoverages.setLiability(LiabilityLimit.CSL500K);
        autoCoverages.setUninsuredLimit(UninsuredLimit.CSL500K);
        autoCoverages.setUnderinsured(true);
        autoCoverages.setUnderinsuredLimit(UnderinsuredLimit.CSL500K);
        squirePersonalAuto.setCoverages(autoCoverages);

        //FPP
        SquireFPP squireFPP = new SquireFPP();
        SquireFPPTypeItem machinery = new SquireFPPTypeItem(squireFPP);
        machinery.setType(FPPFarmPersonalPropertySubTypes.Tractors);
        machinery.setValue(1000);

        ArrayList<SquireFPPTypeItem> listOfFPPItems = new ArrayList<SquireFPPTypeItem>();
        listOfFPPItems.add(machinery);
        squireFPP.setItems(listOfFPPItems);

        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -9);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        myPropertyAndLiability.squireFPP = squireFPP;


        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.myClaimPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withPolEffectiveDate(newEff)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withPolTermLengthDays(79)
                .withInsFirstLastName("Claim", "PreRenewal")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        LocalDate dateOfLoss = DateUtils.convertDateToLocalDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.ClaimCenter));
        System.out.println("Current policy  " + this.myClaimPolicyObjPL.squire.getPolicyNumber());

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();

        GenerateFNOL myFNOLObj = new GenerateFNOL.Builder(this.driver)
                .withCreatorUserNamePassword(ClaimsUsers.gmurray.toString(), "gw")
                .withSpecificIncident("Random")
                .withLossDescription("Loss Descrption Test")
                .withLossCause("Random")
                .withDateOfLoss(dateOfLoss).withLossRouter("Random")
                .withAdress("Random")
                .withPolicyNumber(this.myClaimPolicyObjPL.squire.getPolicyNumber())
                .build(GenerateFNOLType.Auto);
        guidewireHelpers.logout();

        new GenerateExposure.Builder(this.driver)
                .withCreatorUserNamePassword(ClaimsUsers.gmurray.toString(), "gw")
                .withClaimNumber(myFNOLObj.claimNumber)
                .withCoverageType("Comprehensive")
                .withCoverageType("Auto Property Damage - Property")
                .build();

        ArrayList<ReserveLine> line = new ArrayList<ReserveLine>();
        ReserveLine line1 = new ReserveLine();
        line1.setReserveAmount("252000");
        line.add(line1);

        new GenerateReserve.Builder(this.driver)
                .withCreatorUserNamePassword(ClaimsUsers.gmurray.toString(), "gw")
                .withClaimNumber(myFNOLObj.claimNumber)
                .withReserveLines(line)
                .build();

        new GenerateCheck.Builder(this.driver)
                .withCreatorUserNamePassword(ClaimsUsers.gmurray, "gw")
                .withClaimNumber(myFNOLObj.claimNumber)
                .withDeductible(false)
                .withDeductibleAmount("250")
                .withPaymentType(CheckLineItemType.INDEMNITY)
                .withCategoryType(CheckLineItemCategory.INDEMNITY)
                .withPaymentAmount("251000")
                .withCompanyCheckBook("Farm Bureau")
                .build(GenerateCheckType.Regular);

    }

    @Test(dependsOnMethods = {"testIssueClaimsChecks"})
    private void testIssueUmbrellaPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_5000000);

        myClaimPolicyObjPL.squireUmbrellaInfo = squireUmbrellaInfo;
        myClaimPolicyObjPL.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicySubmitted);


        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myClaimPolicyObjPL.accountNumber);
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickActivitySubject("Submitted Full Application");
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquireUmbrellaCoverages();
        GenericWorkorderSquireUmbrellaCoverages covs = new GenericWorkorderSquireUmbrellaCoverages(driver);
        covs.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_6000000);
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQuote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            sideMenu.clickSideMenuRiskAnalysis();
            GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
            risk.approveAll_IncludingSpecial();
            qualificationPage.clickQuote();
        }
        sideMenu.clickSideMenuQuote();
        quote.clickHandrate();
        quote.enterRatingOverrideByDescription("Six Million Premium", "200", "Testing Purpose");
        quote.clickUpdateButton();

        sideMenu.clickSideMenuRiskAnalysis();


        quote.issuePolicy(IssuanceType.NoActionRequired);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
    }

    @Test()
    public void testIssueSquirePolWithRules() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
                MedicalLimit.TenK);
        coverages.setUnderinsured(false);
        coverages.setUninsuredLimit(UninsuredLimit.CSL300K);
        coverages.setAccidentalDeath(true);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact("Test" + StringsUtils.generateRandomNumberDigits(7), "AutoRegression", Gender.Male, DateUtils.convertStringtoDate("01/01/1979", "MM/dd/YYYY"));
        person.setMaritalStatus(MaritalStatus.Married);
        person.setRelationToInsured(RelationshipToInsured.Insured);
        person.setOccupation("Software");
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setEmergencyRoadside(true);
        vehicleList.add(toAdd);

        PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
        loc1Bldg2.setBuildingLimit(15000000);
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(loc1Bldg2);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty Prop = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        Prop.setDwellingVacant(true);
        locOnePropertyList.add(Prop);
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locToAdd.setPlNumResidence(11);
        locToAdd.setBuildingList(locOneBuildingList);
        locToAdd.setManualProtectionClassCode(ProtectionClassCode.Prot3);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        ArrayList<InlandMarine> imCoverages = new ArrayList<InlandMarine>();
        imCoverages.add(InlandMarine.PersonalProperty);

        PersonalProperty pprop10 = new PersonalProperty();
        pprop10.setType(PersonalPropertyType.Jewelry);
        pprop10.setDeductible(PersonalPropertyDeductible.Ded5Perc);
        pprop10.setLimit(1234);
        PersonalPropertyScheduledItem jewelryScheduledItem = new PersonalPropertyScheduledItem();
        jewelryScheduledItem.setParentPersonalPropertyType(PersonalPropertyType.Jewelry);
        jewelryScheduledItem.setType(PersonalPropertyScheduledItemType.Ring);
        jewelryScheduledItem.setLimit(1234);
        Date photoYear = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -6);

        jewelryScheduledItem.setPhotoUploadDate(photoYear);
        jewelryScheduledItem.setAppraisalDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        jewelryScheduledItem.setDescription("Jewelry");
        ArrayList<PersonalPropertyScheduledItem> jewelryScheduledItems = new ArrayList<PersonalPropertyScheduledItem>();
        jewelryScheduledItems.add(jewelryScheduledItem);
        pprop10.setScheduledItems(jewelryScheduledItems);

        PersonalPropertyList ppropList = new PersonalPropertyList();
        ppropList.setJewelry(pprop10);

        //FPP
        SquireFPP squireFPP = new SquireFPP();
        SquireFPPTypeItem machinery = new SquireFPPTypeItem(squireFPP, FPPFarmPersonalPropertySubTypes.Tractors);
        machinery.setValue(1000);
        ArrayList<SquireFPPTypeItem> listOfFPPItems = new ArrayList<SquireFPPTypeItem>();
        listOfFPPItems.add(machinery);
        squireFPP.setItems(listOfFPPItems);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        myPropertyAndLiability.squireFPP = squireFPP;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imCoverages;
        myInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withSquireEligibility(SquireEligibility.Country)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withPolTermLengthDays(79)
                .withInsFirstLastName("PreRenewal", "Direction")
                .build(GeneratePolicyType.FullApp);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        guidewireHelpers.editPolicyTransaction();

        // ******************************************* Section I - Changes
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        // Cov A or Cov E limit is $1 million in renewing term
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverage.setCoverageALimit(11000000);

        // Adding msyear and photoyear
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);

        // Manual protection and category code > $10million
        propertyDetail.setManualPublicProtectionClassCode(ProtectionClassCode.Prot9);
        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);

        int yearField = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy");
        int monthField = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "MM");
        if (monthField == 1) {
            monthField = 12;
            yearField = yearField - 1;
        }
        constructionPage.setMSYear((monthField - 1) + "/" + (yearField - 4));
        constructionPage.setPhotoYear((monthField - 1) + "/" + (yearField - 7));
        propertyDetail.clickOk();
        addNewPropertyCoverageLimit(propertyDetail, PropertyTypePL.DwellingPremises, NumberOfUnits.FourUnits, FoundationType.FullBasement, 2500000, CoverageType.BroadForm);
        sideMenu.clickSideMenuSquirePropertyDetail();

        propertyDetail.clickViewOrEditBuildingButton(2);
        propertyDetail.setRisk("A");
        propertyDetail.clickOverrideCategoryCodeCheck();
        propertyDetail.setCategoryCodeReason("082", "Testing purpose");
        propertyDetail.clickOk();

        // ******************************************* Section III - changes
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
        vehiclePage.editVehicleByVehicleNumber(1);
        vehiclePage.clickOK();

        // Not Rated Driver
        Date genericDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Year, -40);
        Contact notRated = new Contact("driver-" + StringsUtils.generateRandomNumberDigits(7), "notRated", Gender.Female, DateUtils.getDateValueOfFormat(genericDate, "MM/dd/yyyy"));
        addNewPolicyMember(myPolicyObjPL.basicSearch, notRated);
        AddingNewDriver(notRated.getLastName(), notRated.getGender(), "Relax", "CB54553D");
        sideMenu.clickSideMenuPAVehicles();
        if (guidewireHelpers.errorMessagesExist() && (guidewireHelpers.getErrorMessages().toString().contains("Discard Unsaved Change"))) {
            guidewireHelpers.clickDiscardUnsavedChangesLink();
            sideMenu.clickSideMenuPADrivers();
            AddingNewDriver(notRated.getLastName(), notRated.getGender(), "Relax", "CB54553D");
        }
        sideMenu.clickSideMenuPADrivers();
        addExistingDriverWithInfo(notRated.getLastName());
        sideMenu.clickSideMenuPAVehicles();

        // Adding Farm Truck
        sideMenu.clickSideMenuPAVehicles();
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.FarmTruck);
        vehiclePage.setOdometer(5000);
        vehiclePage.selectTruckType(VehicleTruckTypePL.FourPlus);
        vehiclePage.setGVW(52000);
        vehiclePage.selectGaragedAtZip(myPolicyObjPL.pniContact.getAddress().getCity());
        vehiclePage.clickOK();

        // Adding Show Car
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.ShowCar);
        vehiclePage.setStatedValue(20000);
        vehiclePage.sendArbitraryKeys(Keys.TAB);
        vehiclePage.setModelYear(2005);
        vehiclePage.sendArbitraryKeys(Keys.TAB);
        vehiclePage.setModel("test");
        vehiclePage.setMake("test");
        vehiclePage.setOdometer(20000);
        vehiclePage.selectGaragedAtZip(myPolicyObjPL.pniContact.getAddress().getCity());
        vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setComprehensive(true);
        vehiclePage.sendArbitraryKeys(Keys.TAB);
        vehicleCoverages.setCollision(true);
        vehicleCoverages.clickOK();

        // ******************************************* Section II coverages
        sideMenu.clickSideMenuSquirePropertyCoverages();
        coverage.clickSectionIICoveragesTab();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        SectionIICoverages sectionIICoverage = new SectionIICoverages(SectionIICoveragesEnum.CustomFarming, 0, 25000);
        section2Covs.addCoverages(sectionIICoverage);
        section2Covs.setQuantity(sectionIICoverage);

        // Billing county
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoBillingCounty("Ada");
        policyInfo.setPolicyInfoRatingCounty("Ada");

        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk.performRiskAnalysisAndQuote(myPolicyObjPL);
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        if (quotePage.hasBlockBind()) {
            risk.handleBlockSubmit(myPolicyObjPL);
        }
        guidewireHelpers.logout();
        guidewireHelpers.setPolicyType(myPolicyObjPL, GeneratePolicyType.FullApp);
        myPolicyObjPL.convertTo(driver, GeneratePolicyType.PolicySubmitted);

        guidewireHelpers.logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickActivitySubject("Submitted Full Application");

        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages cov = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        cov.clickFarmPersonalProperty();
        cov.setFarmPersonalPropertyRisk("A");

        sideMenu.clickSideMenuSquirePropertyDetail();

        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            sideMenu.clickSideMenuRiskAnalysis();
            risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
            risk.approveAll_IncludingSpecial();
            quote.clickSaveDraftButton();
        }
        guidewireHelpers.logout();

        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);

        qualificationPage.clickQuote();

        sideMenu.clickSideMenuQuote();

        quote.issuePolicy(IssuanceType.NoActionRequired);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

    }

    private void addExistingDriverWithInfo(String name) throws GuidewireNavigationException {
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.clickEditButtonInDriverTableByDriverName(name);
        paDrivers.setNotRatedCheckbox(true);
        paDrivers.selectNotRatedReason(NotRatedReason.ExchangeStudent);
        paDrivers.selectMaritalStatus(MaritalStatus.Married);
        paDrivers.selectGender(Gender.Male);
        paDrivers.setFollowUpDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -1));
        paDrivers.clickOk();

        //SRP - 21
        paDrivers.clickEditButtonInDriverTable(1);
        paDrivers.clickSRPIncidentsTab();
        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents driver_SRP = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
        driver_SRP.setInternationalDLIncident(30);
        driver_SRP.sendArbitraryKeys(Keys.TAB);
        driver_SRP.setUnverifiableDrivingRecordIncident(30);
        driver_SRP.sendArbitraryKeys(Keys.TAB);
        driver_SRP.setNoProofInsuranceIncident(30);
        paDrivers.clickOk();
    }

    private void AddingNewDriver(String lastName, Gender gender, String occupation, String license) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.addExistingDriver(lastName);
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
        paDrivers.selectGender(gender);
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
        paDrivers.selectDriverHaveCurrentInsurance(true);
        paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");
        paDrivers.setOccupation(occupation);
        paDrivers.setLicenseNumber(license);
        paDrivers.sendArbitraryKeys(Keys.TAB);
        State state = State.Idaho;
        paDrivers.selectLicenseState(state);
        paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.clickOk();
    }

    private void addNewPolicyMember(boolean basicSearch, Contact person) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();

        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(basicSearch, person.getFirstName(), person.getLastName(), null, null, null,
                null, CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.setDateOfBirth(person.getDob());
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
        householdMember.setNewPolicyMemberAlternateID(person.getFirstName());
        householdMember.selectNotNewAddressListingIfNotExist(myPolicyObjPL.pniContact.getAddress());
        householdMember.sendArbitraryKeys(Keys.TAB);
        householdMember.clickRelatedContactsTab();
        householdMember.clickOK();

    }

    private void addNewPropertyCoverageLimit(GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail,
                                             PropertyTypePL propertyType, NumberOfUnits noOfUnits, FoundationType foundationType, double limit,
                                             CoverageType CType) throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        PLPolicyLocationProperty property = new PLPolicyLocationProperty(propertyType);
        property.setFoundationType(foundationType);
        property.setNumberOfUnits(noOfUnits);

        if (propertyType.equals(PropertyTypePL.DwellingUnderConstruction)) {
            property.setYearBuilt(2016);
        }

        if (propertyType.equals(PropertyTypePL.ResidencePremises) || propertyType.equals(PropertyTypePL.VacationHomeCovE)) {
            property.setYearBuilt(1950);
        }

        propertyDetail.fillOutPropertyDetails_FA(property);

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        if (propertyType.equals(PropertyTypePL.Contents)) {
            constructionPage.setFoundationType(foundationType);
            constructionPage.setConstructionType(ConstructionTypePL.Frame);

        } else {
            constructionPage.fillOutPropertyConstrustion_FA(property);
            constructionPage.setLargeShed(false);
        }
        constructionPage.clickProtectionDetailsTab();
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property);
        protectionPage.clickOK();
        int BuildingNumber = propertyDetail.getSelectedBuildingNum();

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, BuildingNumber);

        if (propertyType.equals(PropertyTypePL.Contents)) {
            coverages.setCoverageCLimit(limit);
            coverages.selectOtherCoverageCValuation(ValuationMethod.ActualCashValue);
        } else if (propertyType.equals(PropertyTypePL.VacationHomeCovE)) {
            coverages.setCoverageELimit(limit);
            coverages.setCoverageECoverageType(CoverageType.Peril_1);
            coverages.setCoverageCLimit(limit);
            coverages.selectOtherCoverageCValuation(ValuationMethod.ActualCashValue);
        } else {
            coverages.setCoverageALimit(limit);
            coverages.setCoverageCValuation(property.getPropertyCoverages());
        }

        if (CType != null) {
            coverages.selectCoverageCCoverageType(CType);
        }
    }
}
