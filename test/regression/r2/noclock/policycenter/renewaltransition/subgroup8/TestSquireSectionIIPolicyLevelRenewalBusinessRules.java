package regression.r2.noclock.policycenter.renewaltransition.subgroup8;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.Property.SectionIIMedicalLimit;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/*
 * This test might fail due to DE6095: Renewal - Changing medical Limit to 25,000 and Quoting giving IllegalStateException
 */

/**
 * @Author nvadlamudi
 * @Requirement : US7931: Section II/Policy Level Renewal Business Rules
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Jun 22, 2016
 */
@QuarantineClass
public class TestSquireSectionIIPolicyLevelRenewalBusinessRules extends BaseTest {
    private GeneratePolicy myPolicyObjPL;
    private Underwriters uw;

    private WebDriver driver;

    @Test()
    private void testIssueSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "Renew")
                .withPolTermLengthDays(79)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueSquirePolicy"})
    private void testRenewalFRSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login with agent
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(myPolicyObjPL);
        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickPendingTransaction(TransactionType.Renewal);

        new GuidewireHelpers(driver).editPolicyTransaction();

        addingSquireBlockBindIssues(SquireEligibility.FarmAndRanch);

        addingSquirePolicyLevelBlockBindIssues(SquireEligibility.FarmAndRanch);

        addingPolicyLevelOtherBlockBindIssues(SquireEligibility.FarmAndRanch);

    }

    private void addingPolicyLevelOtherBlockBindIssues(SquireEligibility eligibility)
            throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();

        // adding non-standardize location
        AddressInfo newAddress = new AddressInfo();
        newAddress.setLine1("123 Main St");
        newAddress.setCity("Pocatello");
        newAddress.setZip("83201");
        newAddress.setCounty(CountyIdaho.Bannock);

        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		/*propertyLocations.SelectLocationsCheckboxByRowNumber(2);
		propertyLocations.clickRemoveButton();
		*/
        propertyLocations.clickNewLocation();
        propertyLocations.selectLocationAddress("New");
        propertyLocations.addLocationInfoFA(newAddress, 2, 2);

        propertyLocations.clickStandardizeAddress();
        try {
            propertyLocations.clickLocationInformationOverride();
        } catch (Exception e) {
        }
        propertyLocations.clickOK();

        // validating in property details for the standardize and non
        // standardize address
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);

        propertyDetail.highLightPropertyLocationByNumber(2);

        PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
        propertyDetail.fillOutPropertyDetails_FA(property1);

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.fillOutPropertyConstrustion_FA(property1);
        constructionPage.setLargeShed(false);
        constructionPage.clickProtectionDetailsTab();
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property1);

        propertyDetail.clickPropertyInformationDetailsTab();
        propertyDetail.setManualPublicProtectionClassCode(ProtectionClassCode.Prot5);
        protectionPage.clickOK();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(2, 2);
        coverages.setCoverageALimit(120000);
        coverages.setCoverageAValuation(ValuationMethod.ActualCashValue);
        coverages.setCoverageCValuation(property1.getPropertyCoverages());
        String[] newErrorMessages = {"Manual PC Code Changed"};
        validateRiskAnalysisApprovals(newErrorMessages, eligibility);
    }

    private void addingSquirePolicyLevelBlockBindIssues(SquireEligibility eligibility)
            throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickEditPolicyTransaction();

        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
        // If the photo year is entered as more than 6 years prior, If the cost
        // estimator year is entered as more than 3 years prior
        int yearField = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy");
        int monthField = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "MM");
        constructionPage.setMSYear((monthField - 1) + "/" + (yearField - 4));
        constructionPage.setPhotoYear((monthField - 1) + "/" + (yearField - 7));
        constructionPage.clickOK();

        // If there is $10 million in property coverage per risk.
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, 1);
        coverages.setCoverageALimit(12000000);

        // When Term type = 'Other', create UW block quote
        sideMenu.clickSideMenuPolicyInfo();
        policyInfo.setPolicyInfoTermType("Other");

        // If organization type is "Other", create UW
        policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Other);

        // Agent set the effective date that is over 10 day in past
        // Date newEffectiveDate =
        // DateUtils.dateAddSubtract(DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter),
        // DateAddSubtractOptions.Day, -11);
        // policyInfo.setPolicyInfoEffectiveDate(DateUtils.dateFormatAsString("MM/dd/yyyy",
        // newEffectiveDate));
        policyInfo.setPolicyInfoDescription("Testing Purpose");

        // If the designated address is not Idaho for the Primary Named Insured,
        // create UW issue block bind.
        policyInfo.clickPolicyInfoPrimaryNamedInsured();

        AddressInfo newAddress = new AddressInfo();
        newAddress.setLine1("3696 S 6800 W");
        newAddress.setCity("West Valley City");
        newAddress.setState(State.Utah);
        newAddress.setZip("84128");
        newAddress.setType(AddressType.Home);

        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        //		GWCommon common = new GWCommon();
        GenericWorkorderPolicyInfoAdditionalNamedInsured aInsured = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
        aInsured.setEditAdditionalNamedInsuredAddressListing(newAddress);
        aInsured.sendArbitraryKeys(Keys.TAB);
		/*common.setAddress(newAddress.getLine1());
		common.setCity(newAddress.getCity());
				common.setState(newAddress.getState());
		common.setZip(newAddress.getZip());
		common.setAddressType(newAddress.getType());
				sendArbitraryKeys(Keys.TAB);*/
        GenericWorkorderAdditionalNamedInsured namedInsured = new GenericWorkorderAdditionalNamedInsured(driver);
        namedInsured.setReasonForContactChange("Testing purpose Added");
        householdMember.clickUpdate();

        // Min age of policy holder
        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        for (int currentMember = 1; currentMember <= household
                .getPolicyHouseholdMembersTableRowCount(); currentMember++) {
            if (household.getPolicyHouseHoldMemberTableCellValue(currentMember, "Name").contains(myPolicyObjPL.pniContact.getLastName())) {
                household.clickPolicyHouseHoldTableCell(currentMember, "Name");
                break;
            } else {
                continue;
            }
        }
        Date newDOB = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Year, -17);
        householdMember.setDateOfBirth(DateUtils.dateFormatAsString("MM/dd/yyyy", newDOB));
        householdMember.sendArbitraryKeys(Keys.TAB);
        boolean testFailed = false;
        String errorMessage = "";
        if (!householdMember.getPNIUnder18Validation().contains("The policy holder must be over the age of 17")) {
            testFailed = true;
            errorMessage = errorMessage + "Expected page validation : 'The policy holder must be over the age of 17' is not displayed.";
        }
        householdMember.setDateOfBirth("01/01/1980");
        householdMember.clickCancel();

        // adding additional insured
        household.clickSearch();
        SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
        addressBook.searchAddressBookByFirstLastName(myPolicyObjPL.basicSearch, "Last", "First" + StringsUtils.generateRandomNumberDigits(5), null,
                null, null, null, CreateNew.Create_New_Always);
        householdMember.setDateOfBirth(DateUtils.dateFormatAsString("MM/dd/yyyy", newDOB));
        householdMember.sendArbitraryKeys(Keys.TAB);

        householdMember.selectRelationshipToInsured(RelationshipToInsured.SignificantOther);
        householdMember.selectNotNewAddressListingIfNotExist(myPolicyObjPL.pniContact.getAddress());
        householdMember.setAdditionalInsured(true);
        householdMember.sendArbitraryKeys(Keys.TAB);
        householdMember.sendArbitraryKeys(Keys.TAB);
        householdMember.setSectionIAdditionalInsured(true);
        householdMember.setSectionIIAdditionalInsured(true);
        householdMember.setSSN(StringsUtils.generateRandomNumberDigits(9));
        householdMember.clickRelatedContactsTab();

        householdMember.clickOK();

        // If the Protection Class is 9 or 10 on a property, create
        // informational UW issue.
        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.setManualPublicProtectionClassCode(ProtectionClassCode.Prot9);
        propertyDetail.clickOk();

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        if (risk.getValidationMessagesText().contains(" Additional Insured on Policy Member must be at least 18 years young")) {
            risk.clickClearButton();
            sideMenu.clickSideMenuHouseholdMembers();
            for (int currentMember = 1; currentMember <= household
                    .getPolicyHouseholdMembersTableRowCount(); currentMember++) {
                if (household.getPolicyHouseHoldMemberTableCellValue(currentMember, "Name").contains("Last")) {
                    household.clickPolicyHouseHoldTableCell(currentMember, "Name");
                    break;
                } else {
                    continue;
                }
            }

            householdMember.setDateOfBirth("01/01/1980");
            householdMember.sendArbitraryKeys(Keys.TAB);
            namedInsured.setReasonForContactChange("Testing purpose Added");
            householdMember.clickOK();
        }

        String[] newReqFurtherUWMessages = {"Photo Year Over 6 Years", "MS Year Over 3 Years",
                "Organization Type of Other", "Designated Out-of-State", "High Protection Class"};
        validateRiskAnalysisApprovals(newReqFurtherUWMessages, eligibility);

        if (testFailed)
            Assert.fail(errorMessage);
    }

    private void addingSquireBlockBindIssues(SquireEligibility eligibility) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);

        // On city squire, acres is over 10 then block bind, If the sum of all
        // location acre is under 10 acres on Country or Farm and Ranch offering
        // then create UW issue - Block Bind
        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickEditLocation(1);
        int newAcres = 0;
        if (eligibility.equals(SquireEligibility.City))
            newAcres = 11;
        else
            newAcres = 3;
//        propertyLocations.setAcres(newAcres);
        propertyLocations.clickOK();

        // On location, if state != Idaho create UW issue - Block Bind
        sideMenu.clickSideMenuPropertyLocations();
        propertyLocations.clickNewLocation();
//        propertyLocations.selectAddressType("Out of State");
//        propertyLocations.selectState(State.Oregon);
//        propertyLocations.setCounty("Bend");
//        propertyLocations.setAddressLegal("Testing purpose");
//        propertyLocations.setAcres(2);
        propertyLocations.clickOK();

        // Adding dairy confinement pollution requires UW approval, block bind
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickSectionIICoveragesTab();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        section2Covs.clickPollutionLiabilityTableCheckbox(3, true);
        section2Covs.setPollutionLiabilityQuantity(3, 2);

        // Liability limit of $1 million will require UW approval - Block Bind.
        section2Covs.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        // If the Section II Medical limit is set at 25,000 then create UW issue
        // - Block Bind
        section2Covs.setMedicalLimit(SectionIIMedicalLimit.Limit_25000);
        // If Incidental Occupancy coverage is selected on a policy then create
        // UW issue - Block Issue
        section2Covs.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.IncidentalOccupancy, 0, 0));
        section2Covs.addIncidentalOccupancy("Testing", "ID");

        // If the length entered on a scheduled item on watercraft length
        // coverage exceeds 40, then create a UW issue - Block Issue
        section2Covs.addWatercraftLengthCoverage("Testing", 42);

        String[] expectedQuoteMessage = {"Dairy Confinement added.", "Medical limit at 25,000.", "State is not Idaho.",
                "Incidental Occupancy coverage exists.", "Watercraft length over 40 ft.", "acre", "Missing MS year",
                "Missing Photo year"};

        validateRiskAnalysisApprovals(expectedQuoteMessage, eligibility);

    }

    private void validateRiskAnalysisApprovals(String[] expectedUWMessages, SquireEligibility eligibility) {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        riskAnalysis.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        } else {
            sideMenu.clickSideMenuRiskAnalysis();
        }

        for (String uwIssue : expectedUWMessages) {
            boolean messageFound = false;
            for (int i = 0; i < riskAnalysis.getUWIssuesList().size(); i++) {
                String currentUWIssueText = riskAnalysis.getUWIssuesList().get(i).getText();
                if (!currentUWIssueText.contains("Blocking Bind") && (!currentUWIssueText.contains("Blocking Issuance")
                        && (!currentUWIssueText.contains("Informational")))) {

                    if (currentUWIssueText.contains("City acreage over 10") && uwIssue.contains("acres")
                            && eligibility.equals(SquireEligibility.City)) {
                        messageFound = true;
                        break;
                    }

                    if (currentUWIssueText.contains("acreage") && uwIssue.contains("acre")
                            && (eligibility.equals(SquireEligibility.Country)
                            || eligibility.equals(SquireEligibility.FarmAndRanch))) {
                        messageFound = true;
                        break;
                    }

                    if (currentUWIssueText.contains(uwIssue)) {
                        messageFound = true;
                        break;
                    }
                }
            }
            if (!messageFound) {
                testFailed = true;
                errorMessage = errorMessage + "Expected error Bind Issue : " + uwIssue + " is not displayed.";
            }
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }

    }


}
