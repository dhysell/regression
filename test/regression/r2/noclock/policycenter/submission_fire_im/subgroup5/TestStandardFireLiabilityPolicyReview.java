package regression.r2.noclock.policycenter.submission_fire_im.subgroup5;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderPolicyReviewPL;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import persistence.globaldatarepo.entities.TownshipRange;
import persistence.globaldatarepo.helpers.TownshipRangeHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US6847: PL - Standard Fire and Liability Policy Review
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Jun 20, 2016
 */
public class TestStandardFireLiabilityPolicyReview extends BaseTest {
    private GeneratePolicy sFirePol = null;
    private GeneratePolicy myPolicyObject = null;

    private WebDriver driver;

    @Test()
    public void createStandardFirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
        propLoc.setPlNumAcres(10);
        propLoc.setPlNumResidence(5);
        locationsList.add(propLoc);

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);

        sFirePol = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withStandardFire(myStandardFire)
                .withInsFirstLastName("Pol", "Review")
                .withPolOrgType(OrganizationType.Individual)
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"createStandardFirePolicy"})
    public void testValidatePolicyReview() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(sFirePol.agentInfo.getAgentUserName(), sFirePol.agentInfo.getAgentPassword(), sFirePol.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyReview();

        boolean testFailed = false;
        String errorMessage = "";


        GenericWorkorderPolicyReviewPL polReview = new GenericWorkorderPolicyReviewPL(driver);
        if (!polReview.getProductName().contains(this.sFirePol.productType.getValue())) {
            testFailed = true;
            errorMessage = errorMessage + "Product Type is not displayed. \n";
        }

        if (polReview.getDeductibleAmount() != Double.parseDouble(this.sFirePol.standardFire.section1Deductible.getValue())) {
            testFailed = true;
            errorMessage = errorMessage + "Deductible is not displayed. \n";
        }

        if (polReview.getStandardFirePolicyReviewProductDetailsLimit(1, "Coverage A") != (sFirePol.standardFire.getLocationList().get(0).getPropertyList().get(0).getPropertyCoverages().getCoverageA().getLimit())) {
            testFailed = true;
            errorMessage = errorMessage + "Coverage A limit is not displayed. \n";
        }

        if (polReview.getStandardFirePolicyReviewProductDetailsLimit(1, "Coverage C") != (sFirePol.standardFire.getLocationList().get(0).getPropertyList().get(0).getPropertyCoverages().getCoverageC().getAdditionalValue())) {
            testFailed = true;
            errorMessage = errorMessage + "Coverage C limit is not displayed. \n";
        }

        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickNewLocation();
//        propertyLocations.selectCounty("Bannock");
        ArrayList<String> sections = new ArrayList<String>();
        sections.add("01");
        sections.add("02");
        sections.add("03");
        sections.add("04");
        sections.add("05");
        sections.add("06");
        TownshipRange townshipRange = TownshipRangeHelper.getRandomTownshipRangeForCounty("Bannock");
        propertyLocations.addSection(sections);
        propertyLocations.sendArbitraryKeys(Keys.TAB);
//        propertyLocations.selectTownshipNumber(townshipRange.getTownship());
//        propertyLocations.selectTownshipDirection(townshipRange.getTownshipDirection());
//        propertyLocations.selectRangeNumber(townshipRange.getRange());
//        propertyLocations.selectRangeDirection(townshipRange.getRangeDirection());
//        propertyLocations.setAcres(2);
//        propertyLocations.setNumberOfResidence(2);
        propertyLocations.clickOK();
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.highLightPropertyLocationByNumber(2);
        PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
        propertyDetail.fillOutPropertyDetails_FA(property1);
        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
        constructionPage.setCoverageAPropertyDetailsFA(property1);
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        propertyDetail.clickPropertyInformationDetailsTab();
        propertyDetail.setManualPublicProtectionClassCode(ProtectionClassCode.Prot5);
        protectionPage.clickOK();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(2, 2);
        coverages.setCoverageALimit(120000);
        coverages.setCoverageCLimit(125000);
        coverages.sendArbitraryKeys(Keys.TAB);
        //		coverages.getCoverageA().setIncreasedReplacementCost(false);

        coverages.setCoverageCValuation(property1.getPropertyCoverages());


        sideMenu.clickSideMenuPolicyReview();

        sideMenu.systemOut(polReview.getPropertyAddressByLocationNumber(2));
        String sectionText = "";
        for (int i = 0; i < sections.size(); i++) {
            sectionText = sectionText + ", " + sections.get(i);
        }

        sectionText = sectionText.substring(2, sectionText.length());
        if (!polReview.getPropertyAddressByLocationNumber(2).contains(townshipRange.getTownship() + townshipRange.getTownshipDirection() + ":" + townshipRange.getRange() + townshipRange.getRangeDirection()) && !polReview.getPropertyAddressByLocationNumber(2).contains(sectionText)) {
            testFailed = true;
            errorMessage = errorMessage + "Location Address :" + polReview.getPropertyAddressByLocationNumber(2) + " is not displayed correctly \n";
        }
        if (polReview.getStandardFirePolicyReviewProductDetailsLimit(2, "Coverage A") != (120000)) {
            testFailed = true;
            errorMessage = errorMessage + "Coverage A limit is not displayed. \n";
        }

        if (polReview.getStandardFirePolicyReviewProductDetailsLimit(2, "Coverage C") != (125000)) {
            testFailed = true;
            errorMessage = errorMessage + "Coverage C limit is not displayed. \n";
        }

        if (testFailed)
            Assert.fail(errorMessage);
    }

    @Test()
    public void createStandardLiabPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
        propLoc.setPlNumAcres(10);
        propLoc.setPlNumResidence(5);
        locationsList.add(propLoc);

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Pol", "Review")
                .withPolOrgType(OrganizationType.Individual)
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.FullApp);

        new GuidewireHelpers(driver).logout();


        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

//		myPolicyObject = new GeneratePolicy.Builder(driver)
//				.withProductType(ProductLineType.StandardLiability)
//				.withLineSelection(LineSelection.StandardLiabilityPL)
//				.withStandardLiability(myStandardLiability)
//				.withAgent(myPolicyObject.agentInfo)
//				.withStandardFirePolicyUsedForStandardLiability(myPolicyObject ,true)				
//				.withInsFirstLastName("Pol", "Review")
//				.withPolOrgType(OrganizationType.Individual)
//				.build(GeneratePolicyType.FullApp); 

        myPolicyObject.standardLiability = myStandardLiability;
        myPolicyObject.lineSelection.add(LineSelection.StandardLiabilityPL);
        myPolicyObject.addLineOfBusiness(ProductLineType.StandardLiability, GeneratePolicyType.FullApp);

    }

    @Test(dependsOnMethods = {"createStandardLiabPolicy"})
    public void testValidateStandardLiabilityPolReview() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(myPolicyObject.basicSearch, "Pol" + StringsUtils.generateRandomNumberDigits(6), "Review", null, null, null, null, CreateNew.Create_New_Always);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.setDateOfBirth(DateUtils.convertStringtoDate("01/01/1980", "MM/dd/YYYY"));
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
        householdMember.setNewPolicyMemberAlternateID(StringsUtils.generateRandomNumberDigits(12));
        householdMember.selectNotNewAddressListingIfNotExist(myPolicyObject.pniContact.getAddress());

        householdMember.sendArbitraryKeys(Keys.TAB);
        householdMember.setAdditionalInsured(true);
        householdMember.clickRelatedContactsTab();
        householdMember.clickOK();

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        sideMenu.clickSideMenuPolicyReview();

        boolean testFailed = false;
        String errorMessage = "";

        GenericWorkorderPolicyReviewPL polReview = new GenericWorkorderPolicyReviewPL(driver);
        if (!polReview.getProductName().contains(this.myPolicyObject.productType.getValue())) {
            testFailed = true;
            errorMessage = errorMessage + "Product Type is not displayed. \n";
        }

        if (!polReview.getStandardLiabilityMedicalbyType("Liability").contains(this.myPolicyObject.standardLiability.liabilitySection.getGeneralLiabilityLimit().getValue())) {
            testFailed = true;
            errorMessage = errorMessage + "Liability Limit is not displayed. \n";
        }

        if (!polReview.getStandardLiabilityQuantityAmountByDescription("Additional Named Insured").contains("1")) {
            testFailed = true;
            errorMessage = errorMessage + "Additional Insured is not displayed. \n";
        }

        if (Integer.parseInt(polReview.getStandardLiabilityQuantityAmountByDescription("Acres")) != this.myPolicyObject.standardLiability.getLocationList().get(0).getPlNumAcres()) {
            testFailed = true;
            errorMessage = errorMessage + "Locations acres are not displayed correctly \n";
        }

        if ((Integer.parseInt(polReview.getStandardLiabilityQuantityAmountByDescription("Additional Residence")) + 1) != this.myPolicyObject.standardLiability.getLocationList().get(0).getPlNumResidence()) {
            testFailed = true;
            errorMessage = errorMessage + "Locations Additional Residences are not displayed correctly \n";
        }

        if (!polReview.getStandardFireLocationsByRowColumnName(1, "Address").contains(this.myPolicyObject.standardLiability.getLocationList().get(0).getAddress().getLine1())) {
            testFailed = true;
            errorMessage = errorMessage + "Locations Address is not matched with policy locations address \n";
        }

        if (Integer.parseInt(polReview.getStandardFireLocationsByRowColumnName(1, "Acres")) != (this.myPolicyObject.standardLiability.getLocationList().get(0).getPlNumAcres())) {
            testFailed = true;
            errorMessage = errorMessage + "Locations Acres is not matched with policy locations Acres \n";
        }

        if (Integer.parseInt(polReview.getStandardFireLocationsByRowColumnName(1, "No. Of Residence")) != (this.myPolicyObject.standardLiability.getLocationList().get(0).getPlNumResidence())) {
            testFailed = true;
            errorMessage = errorMessage + "Locations Residences is not matched with policy locations Residences \n";
        }

        if (testFailed)
            Assert.fail(errorMessage);
    }

}
