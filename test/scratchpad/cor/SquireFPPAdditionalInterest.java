package scratchpad.cor;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.ElectricalSystem;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.Garage;
import repository.gw.enums.Property.KitchenBathClass;
import repository.gw.enums.Property.NumberOfStories;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.Plumbing;
import repository.gw.enums.Property.PrimaryHeating;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.RoofType;
import repository.gw.enums.Property.SprinklerSystemType;
import repository.gw.enums.Property.Wiring;
import repository.gw.enums.QuoteType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FPPAdditionalInterest;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.generic.GenericWorkorderSquireFPPAdditionalInterest;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import repository.pc.workorders.submission.SubmissionProductSelection;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

@SuppressWarnings("unused")
public class SquireFPPAdditionalInterest extends BaseTest {

    private Agents policyAgent;
    private String acctNum;
    private AddressInfo newLocation;
    private ArrayList<AdditionalInterest> additionalInterestList;
    private WebDriver driver;

    /**
     * @throws Exception
     * @Author sbroderick
     * @Requirement US5093
     * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story Cards/01_PolicyCenter/01_User_Stories/PolicyCenter 8.0/Homeowners/PC8 - HO - QuoteApplication - FPP Additional Interest.xlsx">PC8 - HO - QuoteApplication -FPP Additional Interest</a>
     * @Description Ensure Functionality for FPP Additional Interests
     * @DATE Dec 29, 2015
     */

    @Test//(dependsOnMethods = { "generate" }, enabled = true)
    public void testFPPBusinessRules() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
        rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact myContactLienObj = new GenerateContact.Builder(driver)
                .withCompanyName("Lienholder")
                .withRoles(rolesToAdd)
                .withGeneratedLienNumber(true)
                .withUniqueName(true)
                .build(GenerateContactType.Company);

        driver.quit();

        cf.setCenter(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        FPPAdditionalInterest additionalInterestMountain = new FPPAdditionalInterest(AdditionalInterestType.LienholderPL, ContactSubType.Company, myContactLienObj.companyName, myContactLienObj.addresses.get(0));
        additionalInterestMountain.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        FPPAdditionalInterest additionalInterestBankOfMomAndDad = new FPPAdditionalInterest(AdditionalInterestType.LessorPL, ContactSubType.Company, "Bank of Mom and Dad", new AddressInfo(true));
        additionalInterestMountain.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        ArrayList<AdditionalInterest> additionalInterestList = new ArrayList<AdditionalInterest>();
        additionalInterestList.add(additionalInterestMountain);
        additionalInterestList.add(additionalInterestBankOfMomAndDad);
        this.newLocation = new AddressInfo(true);
        this.policyAgent = AgentsHelper.getRandomAgent();
        new Login(driver).login(policyAgent.getAgentUserName(), policyAgent.getAgentPassword());
        TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
        menuPolicy.clickNewSubmission();
        String pniLastName = "Underhill";
        SubmissionNewSubmission newSubmissionPage = new SubmissionNewSubmission(driver);
        AddressInfo addressInfo = new AddressInfo();
        newSubmissionPage.fillOutFormSearchAndEitherCreateNewOrUseExisting(true, CreateNew.Create_New_Always, ContactSubType.Person, pniLastName, "Mr", null, null, addressInfo.getCity(), addressInfo.getState(), addressInfo.getZip());

        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
        Date birthDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        birthDate = DateUtils.dateAddSubtract(birthDate, DateAddSubtractOptions.Year, -16);
        createAccountPage.setDOB(birthDate);
        createAccountPage.setSubmissionCreateAccountBasicsSSN(Integer.toString(NumberUtils.generateRandomNumberInt(111111111, 999999998)));
        createAccountPage.fillOutPrimaryAddressFields(addressInfo);
        createAccountPage.clickSubmissionCreateAccountUpdate();

        if (createAccountPage.finds(By.xpath("//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:ttlBar') and contains(text(), 'Location Information')]")).size() > 0) {
            createAccountPage.getSubmissionCreateAccountAddressNotFound();
            createAccountPage.updateAddressStandardization();
        }
        SubmissionProductSelection selectProductPage = new SubmissionProductSelection(driver);
        selectProductPage.startQuoteSelectProductAndGetAccountNumber(QuoteType.FullApplication, ProductLineType.Squire);

        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
//			eligibilityPage.chooseCountry();
        eligibilityPage.chooseFarm();
        eligibilityPage.clickNext();

//			IGenericWorkorderLineSelectionPL lineSelect = GenericWorkorderFactory.getGenericWorkorderLineSelectionPL();
//			lineSelect.checkPersonalPropertyLine(true);
//			lineSelect.checkInlandMarine(false);
//			lineSelect.checkPersonalAutoLine(false);
//			lineSelect.clickNext();

        InfoBar myInfoBar = new InfoBar(driver);
        this.acctNum = myInfoBar.getInfoBarAccountNumber();

        GenericWorkorderQualification quals = new GenericWorkorderQualification(driver);
        quals.setPLQuestionsToFalse();
        quals.setSquireHOFullTo(false, "Stayed in Motels when I was in a biker gang.");
        quals.setSquireGLFullTo(false);
        quals.clickQualificationGLCattle(false);
        quals.clickQualificationNext();

        GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
        policyInfoPage.setPolicyInfoOrganizationType(OrganizationType.Individual);
        policyInfoPage.setPolicyInfoBillingCounty("Ada");
        policyInfoPage.setPolicyInfoDuesCounty("Ada");

        SideMenuPC sidebar = new SideMenuPC(driver);

        sidebar.clickSideMenuSquireProperty();

        GenericWorkorderSquirePropertyAndLiabilityLocation myProperty = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        String[] sections = {};
        myProperty.clickNewLocation();
        myProperty = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
//			myProperty.addLocationInfo(newLocation.getLine1(), "", "", newLocation.getCity(), newLocation.getState(), newLocation.getZip(), newLocation.getCounty(), sections, 1);

        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuSquirePropertyDetail();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickAdd();

        propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.setPropertyType(PropertyTypePL.ResidencePremises);
        propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.setDwellingVacantRadio(false);
        propertyDetail.setUnits(NumberOfUnits.OneUnit);
        propertyDetail.setWoodFireplaceRadio(false);
        propertyDetail.setExoticPetsRadio(false);
        propertyDetail.setSwimmingPoolRadio(false);
        propertyDetail.setWaterLeakageRadio(false);
        propertyDetail.clickPropertyConstructionTab();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.setYearBuilt(1993);
        constructionPage.setConstructionType(ConstructionTypePL.Frame);
        constructionPage.setBathClass(KitchenBathClass.Basic);
        constructionPage.setSquareFootage(1700);
        constructionPage.setStories(NumberOfStories.One);
        constructionPage.setGarage(Garage.AttachedGarage);
        constructionPage.setLargeShed(false);
        constructionPage.setCoveredPorches(false);
        constructionPage.setFoundationType(FoundationType.FullBasement);
        constructionPage.setRoofType(RoofType.WoodShingles);
        constructionPage.setPrimaryHeating(PrimaryHeating.Gas);
        constructionPage.setPlumbing(Plumbing.Copper);
        constructionPage.setWiring(Wiring.Copper);
        constructionPage.setElectricalSystem(ElectricalSystem.CircuitBreaker);
        constructionPage.setAmps(100);
        constructionPage.setKitchenClass(KitchenBathClass.Basic);
        constructionPage.clickProtectionDetailsTab();

        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        protectionPage.setSprinklerSystemType(SprinklerSystemType.Full);
        protectionPage.setFireExtinguishers(true);
        protectionPage.setSmokeAlarm(true);
        protectionPage.setNonSmoker(true);
        protectionPage.setDeadBoltLocks(true);
        protectionPage.setDefensibleSpace(true);
        protectionPage.clickOK();
        protectionPage.clickNext();

        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickFarmPersonalProperty();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fpp = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
        fpp.checkCoverageD(true);
        fpp.selectCoverageType(FPPCoverageTypes.BlanketInclude);
//			fpp.selectDeductible("500");
//			fpp.addMachinery(FPPMachinery.Tractors, "1", "45120","");
        fpp.clickFPPAdditionalInterests();

        for (AdditionalInterest bank : additionalInterestList) {
            GenericWorkorderSquireFPPAdditionalInterest fppSearchAddInterest = new GenericWorkorderSquireFPPAdditionalInterest(driver);
            fppSearchAddInterest.addInterest(true, bank);

            GenericWorkorderSquireFPPAdditionalInterest fppAddInterests = new GenericWorkorderSquireFPPAdditionalInterest(driver);
//				fppAddInterests.addProperty(property);
        }

        GenericWorkorderSquireFPPAdditionalInterest fppSearchAddInterest = new GenericWorkorderSquireFPPAdditionalInterest(driver);
//			fppSearchAddInterest.addAdditionalInterest(additionalInterestMountain);		

    }
}
