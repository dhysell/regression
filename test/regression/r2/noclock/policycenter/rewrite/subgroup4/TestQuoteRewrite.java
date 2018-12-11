/**
 *
 */
package regression.r2.noclock.policycenter.rewrite.subgroup4;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.BoatType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.InlandMarineTypes.WatercraftTypes;
import repository.gw.enums.InlandMarineTypes.WatercratItems;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireIMWatercraft;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.WatercraftScheduledItems;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.search.SearchAccountsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/*
 * US13621: Don't force user to click all Risk Analysis tabs to Quote or Submit
 * This test will fail as the latest code is not in REGR environment.
 */

/**
 * @Author skanndibanda
 * @Requirement : US4790 - [Part II] PL - Quote Rewrite
 * @Description : User will be able to quote Rewrite policy. Issued Squire with all lines, cancelled with Rewrite reasons
 * and Quoted Rewrite New account, also done Rewrite Full Term quote for Standard Fire
 * @DATE Sep 22, 2016
 */
@QuarantineClass
public class TestQuoteRewrite extends BaseTest {
    private GeneratePolicy standardFirePolicy = null;
    private Agents agentInfo;
    private GeneratePolicy myPolicyObj;
    private GeneratePolicy reWrieFullTermObj;
    private Underwriters uw;

    private WebDriver driver;

    //Issue Squire Policy with all lines
    @Test()
    public void testGenerateSquirePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        this.agentInfo = AgentsHelper.getRandomAgent();

        // Rec Equip
        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500", PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));

        // PersonalProperty
        PersonalProperty pprop = new PersonalProperty();
        pprop.setType(PersonalPropertyType.MedicalSuppliesAndEquipment);
        pprop.setYear(2010);
        pprop.setMake("Testmake");
        pprop.setModel("Testmodel");
        pprop.setVinSerialNum("123123123");
        pprop.setDescription("Testdescription");
        pprop.setLimit(1234);
        pprop.setDeductible(PersonalPropertyDeductible.Ded25);
        ArrayList<String> ppropAdditIns = new ArrayList<String>();
        ppropAdditIns.add("First Guy");

        pprop.setAdditionalInsureds(ppropAdditIns);

        PersonalPropertyList ppropList = new PersonalPropertyList();
        ArrayList<PersonalProperty> msaeList = new ArrayList<PersonalProperty>();
        msaeList.add(pprop);
        ppropList.setMedicalSuppliesAndEquipment(msaeList);

        // Watercraft
        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);

        WatercraftScheduledItems boat = new WatercraftScheduledItems(WatercraftTypes.BoatAndMotor, DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);
        boat.setLimit(3123);
        boat.setItem(WatercratItems.Boat);
        boat.setLength(28);
        boat.setBoatType(BoatType.Outboard);
        ArrayList<WatercraftScheduledItems> boats = new ArrayList<WatercraftScheduledItems>();
        boats.add(boat);
        ArrayList<String> boatAddInsured = new ArrayList<String>();
        boatAddInsured.add("Cor Hofman");
        SquireIMWatercraft boatType = new SquireIMWatercraft(WatercraftTypes.BoatAndMotor, PAComprehensive_CollisionDeductible.OneHundred100, boats);
        boatType.setAdditionalInterest(loc2Bldg1AdditionalInterests);
        ArrayList<SquireIMWatercraft> boatTypes = new ArrayList<SquireIMWatercraft>();
        boatTypes.add(boatType);

        SquireLiability liability = new SquireLiability();

        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.RecreationalEquipment);
        imTypes.add(InlandMarine.PersonalProperty);
        imTypes.add(InlandMarine.Watercraft);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liability;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();
        myInlandMarine.watercrafts_PL_IM = boatTypes;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withAgent(agentInfo)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Account1", "Squire")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    //Rewrite New Account is only available if the old policy is cancelled with specified reasons
    @Test(dependsOnMethods = {"testGenerateSquirePolicy"})
    public void testRewriteNewAccount() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);

        testNewAccountCancelPolicy(myPolicyObj.accountNumber);
        testIssueStandardFirePolicy();
        testRewriteNewAccountPolicy(standardFirePolicy, myPolicyObj);
    }

    private void testIssueStandardFirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locationPropertyList = new ArrayList<PLPolicyLocationProperty>();
        locationPropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE));
        PolicyLocation propertyLocation = new PolicyLocation(locationPropertyList);
        propertyLocation.setPlNumAcres(50);
        propertyLocation.setPlNumResidence(25);
        locationsList.add(propertyLocation);

        this.agentInfo = AgentsHelper.getRandomAgent();

        standardFirePolicy = new GeneratePolicy.Builder(driver)
                .withAgent(agentInfo)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withInsFirstLastName("StandardFire", "Policy")
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    //Cancel Policy with reasons
    private void testNewAccountCancelPolicy(String accountNumber) {

        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        String comment = "Renumbering to another policy";

        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NameChange, comment, currentDate, true);

    }

    //Quote Rewrite New Account and validate Premium Summary
    public void testRewriteNewAccountPolicy(GeneratePolicy stdPol, GeneratePolicy squirePol) throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdPol.accountNumber);

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.startQuoteRewrite(squirePol.accountNumber, stdPol.accountNumber);

        SearchAccountsPC accountSearchPC = new SearchAccountsPC(driver);
        accountSearchPC.searchAccountByAccountNumber(squirePol.accountNumber);

        //Verify Expected activity displayed
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        String currenActivitySubject = summary.getActivitySubject(1);
        String expectedActivitySubject = "Rewrite to new account " + stdPol.accountNumber + " from source account " + squirePol.accountNumber + " for policy #" + myPolicyObj.squire.getPolicyNumber();

        if (!expectedActivitySubject.equals(currenActivitySubject)) {
            Assert.fail("Expected Activity subject not displayed");
        }
        accountSearchPC.searchAccountByAccountNumber(stdPol.accountNumber);
        AccountSummaryPC acctSummary = new AccountSummaryPC(driver);
        acctSummary.clickAccountSummaryPendingTransactionByStatus("Rewrite New Account");
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationType.Individual);
        policyInfo.clickPolicyInfoPrimaryNamedInsured();
        GenericWorkorderPolicyInfoContact policyInfoContactPage = new GenericWorkorderPolicyInfoContact(driver);
        policyInfoContactPage.clickUpdate();

        policyInfo.selectAddExistingOtherContactAdditionalInsured(squirePol.pniContact.getFirstName());
        GenericWorkorderAdditionalNamedInsured ani = new GenericWorkorderAdditionalNamedInsured(driver);
        ani.selectAdditionalInsuredRelationship(RelationshipToInsured.Friend);
        ani.selectAddtionalInsuredAddress(squirePol.pniContact.getAddress().getLine1());
        ani.clickUpdate();
        //policyInfo.setMembershipDues(squirePol.pniContact.getFirstName(), true);

        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers houseHold = new GenericWorkorderPolicyMembers(driver);
        int row = houseHold.getPolicyHouseholdMembersTableRow(stdPol.pniContact.getFirstName());
        houseHold.clickPolicyHouseHoldTableCell(row, "Name");

        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        AddressInfo address = new AddressInfo("6315 W YORK ST", "", "BOISE", State.Idaho, "837047573", CountyIdaho.Ada, "United States", AddressType.Home);
        householdMember.selectNotNewAddressListingIfNotExist(address);
        householdMember.clickOK();
        houseHold.addExistingInsured(squirePol.pniContact.getFirstName());
        householdMember.selectNotNewAddressListingIfNotExist(address);
        householdMember.clickRelatedContactsTab();
        householdMember.clickOK();
        sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.fillOutCreditReport(stdPol);

        sideMenu.clickSideMenuPADrivers();
        sideMenu.clickSideMenuPACoverages();
        sideMenu.clickSideMenuPAVehicles();
        sideMenu.clickSideMenuClueAuto();
        sideMenu.clickSideMenuSquireLineReview();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

        double SectionInetpremium = quote.getSquireRewritePremiumSummaryAmount(1, false, true, false);
        double SectionIgrosspremium = quote.getSquireRewritePremiumSummaryAmount(1, true, false, false);
        double SectionIDiscount = quote.getSquireRewritePremiumSummaryAmount(1, false, false, true);

        double SectionIInetpremium = quote.getSquireRewritePremiumSummaryAmount(2, false, true, false);
        double SectionIIgrosspremium = quote.getSquireRewritePremiumSummaryAmount(2, true, false, false);
        double SectionIIDiscount = quote.getSquireRewritePremiumSummaryAmount(2, false, false, true);

        double SectionIIInetpremium = quote.getSquireRewritePremiumSummaryAmount(3, false, true, false);
        double SectionIIIgrosspremium = quote.getSquireRewritePremiumSummaryAmount(3, true, false, false);
        double SectionIIIDiscount = quote.getSquireRewritePremiumSummaryAmount(3, false, false, true);

        double SectionIVnetpremium = quote.getSquireRewritePremiumSummaryAmount(4, false, true, false);
        double SectionIVgrosspremium = quote.getSquireRewritePremiumSummaryAmount(4, true, false, false);
        double SectionIVDiscount = quote.getSquireRewritePremiumSummaryAmount(4, false, false, true);

        double TotalNetPremium = quote.getQuoteTotalNetPremium();
        double TotalGrossPremium = quote.getQuoteTotalGrossPremium();
        double TotalDiscount = quote.getQuoteTotalDiscountsSurcharges();

        double PolicySummaryNetPremium = SectionInetpremium + SectionIInetpremium + SectionIIInetpremium + SectionIVnetpremium;
        double PolicySummaryGrossPremium = SectionIgrosspremium + SectionIIgrosspremium + SectionIIIgrosspremium + SectionIVgrosspremium;
        double PolicySummaryDiscountValue = SectionIDiscount + SectionIIDiscount + SectionIIIDiscount + SectionIVDiscount;
        double PolicySummaryDiscount = PolicySummaryDiscountValue;

        //Policy Premium
        if (TotalNetPremium != PolicySummaryNetPremium) {
            testFailed = true;
            errorMessage = errorMessage + "Total Net Policy Premium : " + quote.getQuoteTotalNetPremium() + " is not displayed correctly. \n";
        }

        if (TotalGrossPremium != PolicySummaryGrossPremium) {
            testFailed = true;
            errorMessage = errorMessage + "Total Gross Premium : " + quote.getQuoteTotalNetPremium() + " is not displayed correctly. \n";
        }

        if (TotalDiscount != PolicySummaryDiscount) {
            testFailed = true;
            errorMessage = errorMessage + "Total Discount/Surcharges : " + quote.getQuoteTotalNetPremium() + " is not displayed correctly. \n";
        }

        if (testFailed)
            Assert.fail(errorMessage);

    }


    @Test()
    public void testGenerateStandardFire() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locationPropertyList = new ArrayList<PLPolicyLocationProperty>();
        locationPropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE));
        PolicyLocation propertyLocation = new PolicyLocation(locationPropertyList);
        propertyLocation.setPlNumAcres(50);
        propertyLocation.setPlNumResidence(15);
        locationsList.add(propertyLocation);

        reWrieFullTermObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withInsFirstLastName("Test", "Renewal")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }


    @Test(dependsOnMethods = {"testGenerateStandardFire"})
    public void testStdFire_RewriteFullTerm() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), reWrieFullTermObj.accountNumber);
        testRewriteCancelPolicy(reWrieFullTermObj.accountNumber);
        new GuidewireHelpers(driver).logout();
        login.loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), reWrieFullTermObj.accountNumber);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRewriteFullTerm();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQualification();
        sideMenu.clickSideMenuPolicyInfo();
        sideMenu.clickSideMenuHouseholdMembers();
        sideMenu.clickSideMenuPLInsuranceScore();

        validateRewriteQuote();
    }

    private void testRewriteCancelPolicy(String accountNumber) {
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        String comment = "For Rewrite full term of the policy";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, comment, currentDate, true);

    }

    //Quote Rewrite Full Term and validate Premium Summary
    private void validateRewriteQuote() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();
        sideMenu.clickSideMenuSquirePropertyDetail();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        sideMenu.clickSideMenuSquirePropertyCLUE();
        sideMenu.clickSideMenuPAModifiers();
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

        double SectionInetpremium = quote.getSquireRewritePremiumSummaryAmount(1, false, true, false);
        double SectionIgrosspremium = quote.getSquireRewritePremiumSummaryAmount(1, true, false, false);
        double SectionIDiscountValue = quote.getSquireRewritePremiumSummaryAmount(1, false, false, true);
        double SectionIDiscount = SectionIDiscountValue;

        double TotalNetPremium = quote.getQuoteTotalNetPremium();
        double TotalGrossPremium = quote.getQuoteTotalGrossPremium();
        double TotalDiscount = quote.getQuoteTotalDiscountsSurcharges();


        if (TotalNetPremium != SectionInetpremium) {
            testFailed = true;
            errorMessage = errorMessage + "Total Net Policy Premium : " + quote.getQuoteTotalNetPremium() + " is not displayed correctly. \n";
        }

        if (TotalGrossPremium != SectionIgrosspremium) {
            testFailed = true;
            errorMessage = errorMessage + "Total Gross Policy Premium : " + quote.getQuoteTotalNetPremium() + " is not displayed correctly. \n";
        }

        if (TotalDiscount != SectionIDiscount) {
            testFailed = true;
            errorMessage = errorMessage + "Total Discounts/Surcharges : " + quote.getQuoteTotalNetPremium() + " is not displayed correctly. \n";
        }

        if (testFailed)
            Assert.fail(errorMessage);

    }

}
