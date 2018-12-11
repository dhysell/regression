package regression.r2.noclock.policycenter.submission_property.subgroup2;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.RoofType;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderLineReviewPL;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US10739: Detached garage new requirements
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Homeowners/PC8%20-%20HO%20-%20QuoteApplication%20-%20Homeowner's%20Property%20Details.xlsx">
 * Link Text</a>
 * @Description
 * @DATE Apr 4, 2017
 */
public class TestSquirePropertyLiabilityDetachedGarage extends BaseTest {
    private GeneratePolicy squirePLObject;
    private Underwriters uw;
    private WebDriver driver;

    @Test
    public void testCreateSquireFA() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.VacationHome);
        PLPolicyLocationProperty property3 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
        locOnePropertyList.add(property1);
        locOnePropertyList.add(property2);
        locOnePropertyList.add(property3);
        PolicyLocation loc = new PolicyLocation(locOnePropertyList);
        loc.setPlNumAcres(11);
        loc.setPlNumResidence(5);
        locationsList.add(loc);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        squirePLObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "DetachedGarage")
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testCreateSquireFA"})
    private void testAddDetachedGarage() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePLObject.accountNumber);

        boolean testFailed = false;
        String errorMessage = "";
        String additionalLast = "Detach" + StringsUtils.generateRandomNumberDigits(7);
        PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.DetachedGarage);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);

        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        household.clickSearch();
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(squirePLObject.basicSearch, "PropDet", additionalLast, squirePLObject.pniContact.getAddress().getLine1(), squirePLObject.pniContact.getAddress().getCity(), squirePLObject.pniContact.getAddress().getState(), squirePLObject.pniContact.getAddress().getZip(), CreateNew.Create_New_Always);
        householdMember.setDateOfBirth(DateUtils.convertStringtoDate("01/01/1980", "MM/dd/YYYY"));
        householdMember.setNewPolicyMemberAlternateID("Little Bunny Foo Foo");
        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
        householdMember.selectNotNewAddressListingIfNotExist(squirePLObject.pniContact.getAddress());
        guidewireHelpers.sendArbitraryKeys(Keys.TAB);
        householdMember.clickRelatedContactsTab();
        householdMember.clickOK();

        sideMenu.clickSideMenuSquirePropertyDetail();

        propertyDetail.clickViewOrEditBuildingButton(1);
        String publicProtection = propertyDetail.getAutoPublicProtectionClassCode();
        String riskValue = propertyDetail.getRisk();
        String categoryCodeValue = propertyDetail.getReadOnlyCategoryCode();

        // Adding additional interest for new property
        propertyDetail.addExistingAdditionalInterest(additionalLast);
        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.checkBuildingsPropertyAdditionalInterestsFirstMortgage(true);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lienholder");
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();

        // adding additional insured
        String additionalInsured = "Testing Additional";
        propertyDetail.addAddtionalInsured();
        propertyDetail.setAddtionalInsuredName(additionalInsured);
        propertyDetail.clickOk();

        propertyDetail.fillOutPropertyDetails_FA(property);
        propertyDetail.clickPropertyInformationDetailsTab();
//		propertyDetail.setCoverageAPropertyTypeAttachedTo(PropertyTypePL.DwellingPremises.getValue());
//		propertyDetail.setCoverageAPropertyTypeAttachedTo(PropertyTypePL.VacationHome.getValue());
        propertyDetail.setCoverageAPropertyTypeAttachedTo(PropertyTypePL.ResidencePremises.getValue());
        if (!propertyDetail.getAutoPublicProtectionClassCode().equals(publicProtection)) {
            testFailed = true;
            errorMessage = errorMessage + "Auto public protection class code is not the same for Residence Premises \n";
        }

        if (!propertyDetail.getReadOnlyCategoryCode().equals(categoryCodeValue)) {
            testFailed = true;
            errorMessage = errorMessage + "Category Code value is not the same for Residence Premises \n";
        }

        if (!propertyDetail.getReadOnlyRiskValue().equals(riskValue)) {
            testFailed = true;
            errorMessage = errorMessage + "Risk value is not the same for Residence Premises \n";
        }

        if (!propertyDetail.getAdditionalInsuredNameByRowNumber(1).contains(additionalInsured)) {
            testFailed = true;
            errorMessage = errorMessage + "Additional Insured Name is not the same for Residence Premises \n";
        }

        if (!propertyDetail.checkAdditionalInterestByName(additionalLast)) {
            testFailed = true;
            errorMessage = errorMessage + "Additional Interest Name is not the same for Residence Premises \n";
        }

        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.setYearBuilt(property.getYearBuilt());
        constructionPage.setConstructionType(ConstructionTypePL.Masonry);
        constructionPage.setSquareFootage(1000);
        constructionPage.setRoofType(RoofType.SlateConcrete);

        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        protectionPage.clickOK();

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverage.clickSpecificBuilding(1, 4);
        coverage.setOtherStructureDetachedGarageAdditionalValue(110000);
        sideMenu.clickSideMenuSquirePropertyLineReview();

        GenericWorkorderLineReviewPL lineReview = new GenericWorkorderLineReviewPL(driver);
        lineReview.clickSectionOneCoverages();
        int propertyTableRows = lineReview.getPropertyCoverageTableRowCount();
        String constructionType = "", yearBuilt = "", lineReviewLimit = "";
        for (int currentRow = 1; currentRow <= propertyTableRows; currentRow++) {
            String propertyType = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Property Type");
            if (propertyType.equals("Detached Garage")) {
                constructionType = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Const. Type");
                yearBuilt = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Yr. Built");
            }

            if (propertyType.equals("Detached Garage - Additional Value")) {
                lineReviewLimit = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Limit");
            }
        }

        if (Double.parseDouble(lineReviewLimit.replace(",", "")) != 110000 || !constructionType.equals(ConstructionTypePL.Masonry.getValue()) || Integer.parseInt(yearBuilt) != property.getYearBuilt()) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Detached Garage coverage not displayed in Line Review. /n";
        }

        // Payer Assignment
        sideMenu.clickSideMenuPayerAssignment();
        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
        payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(1, 1, additionalLast, true, false);
        payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);

        if (!payerAssignment.getPayerAssignmentPayerDetailsByLocationAndBuildingNumber(1, 4).contains(additionalLast)) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Detached Garage Payer Assignment is not displayed with Residency premises. /n";
        }


        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }

        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();

        // Quote page validations
        sideMenu.clickSideMenuQuote();
        quote.clickSaveDraftButton();
        int detachedRow = quote.getRowNumberSectionISectionIIPropertyDetailsCoverageByName("Detached Garage");
        if (detachedRow <= 0) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Property Type : Detached Garage is not displayed. /n";
        }
        sideMenu.clickSideMenuPolicyInfo();
        guidewireHelpers.editPolicyTransaction();

        // Edit Detached property Types
        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickViewOrEditBuildingButton(4);
        if (propertyDetail.checkPropertyTypeLinkExists()) {//check if user is able to change the property type
            testFailed = true;
            errorMessage = errorMessage + "UnExpected Property Type link exists for Detached Garage Property. /n";
        }
        propertyDetail.clickOk();

        // Removing the residency premises along with Detached Garage
        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.removeBuilding("1");
        if (!guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains(
                "The building(s) that were removed included detached garage(s) that have also been removed"))) {
            testFailed = true;
            errorMessage = errorMessage
                    + "Expected page validation : 'The building(s) that were removed included detached garage(s) that have also been removed' is not displayed. /n";
        }


        if (testFailed) {
            Assert.fail(errorMessage);
        }

    }
}
