package regression.r2.noclock.policycenter.submission_property.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Measurement;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderLineReviewPL;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

public class TestSquirePropertyLiabilityLineReviewSectionOne extends BaseTest {

    private Agents agent;
    private GeneratePolicy squirePolicy;
    private Underwriters uw;

    private WebDriver driver;

    @Test()
    public void generatePropertyFullApp() throws Exception {
        this.agent = AgentsHelper.getRandomAgent();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));
        SquireLiability liabilitySection = new SquireLiability();

        ArrayList<String> policyLocationList = new ArrayList<String>();
        ArrayList<PLPolicyLocationProperty> propertiesList = new ArrayList<PLPolicyLocationProperty>();
        ArrayList<PLPropertyCoverages> propertyCoverages = new ArrayList<PLPropertyCoverages>();

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.squirePolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withAgent(agent)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Stor", "Broderman")
                .withInsAge(26)
                .withPolOrgType(OrganizationType.Individual)
                .build(GeneratePolicyType.FullApp);

        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), squirePolicy.accountNumber);


        for (PolicyLocation policyLocation : locationsList) {
            policyLocationList.add(policyLocation.getAddress().getLine1());
            System.out.println(policyLocation.getAddress().getLine1());
            propertiesList = policyLocation.getPropertyList();
            for (PLPolicyLocationProperty property : propertiesList) {
                property.getPropertyNumber();
                propertyCoverages.add(property.getPropertyCoverages());
            }
        }

        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderLineReviewPL lineReview = new GenericWorkorderLineReviewPL(driver);

        sideMenu.clickSideMenuSquirePropertyCoverages();
        sideMenu.clickSideMenuSquirePropertyGlLineReview();

        // Information from Policy Object
        double policyPropertyDeductible = Double.parseDouble(this.squirePolicy.squire.propertyAndLiability.section1Deductible.getValue());
        double policyCoverageALimit = this.squirePolicy.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getPropertyCoverages().getCoverageA().getLimit();
        double policyCoverageCAdditionalValue = this.squirePolicy.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getPropertyCoverages().getCoverageC().getAdditionalValue();

        double policyCoverageCLimit = (policyCoverageCAdditionalValue + (policyCoverageALimit * (squirePolicy.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getPropertyCoverages().getCoverageC().getLimitPercent() / 100)));

        // Information from Line Review Page
        //		List<WebElement> locationCoverages = TableUtils.getAllTableRows(lineReview.getPropertyCovageTable());

        double reviewPropertyDeductible = Double.parseDouble(lineReview.getPropertyLineDeductible().getText());
        double reviewCoverageALimit = 0.00;
        double reviewCoverageCLimit = 0.00;

        int propertyTableRows = lineReview.getPropertyCoverageTableRowCount();

        for (int currentRow = 1; currentRow < propertyTableRows; currentRow++) {
            String propertyType = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Property Type");
            if (propertyType.trim().startsWith("Coverage") && !propertyType.trim().contains("Loss Of Use")) {

                switch (propertyType.trim()) {
                    case "Coverage A":
                        reviewCoverageALimit = Double.parseDouble(lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Limit").replace(",", ""));
                        break;
                    case "Coverage C":
                        reviewCoverageCLimit = Double.parseDouble(lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Limit").replace(",", "").trim());
                        break;
                    default:
                        break;
                }
            }
        }

        lineReview.clickSectionTwoCoverages();

        double reviewADlimit = lineReview.getLineReviewAccidentalDeath();
        double reviewDamageToPropOthers = lineReview.getDamageToPropertyOthers();

        double policyAD = 1000;
        double policyDamagePropOthers = 1000;


        // Run compare and throw exception if not match
        boolean errorsExist = false;
        String errorLog = "";

        if (!convertAndCompare(policyPropertyDeductible, reviewPropertyDeductible)) {
            errorLog = errorLog + "Property deductible does not match.  " + policyPropertyDeductible + " - " + reviewPropertyDeductible + "\n";
            errorsExist = true;
        }
        if (!convertAndCompare(policyCoverageALimit, reviewCoverageALimit)) {
            errorLog = errorLog + "Coverage A Limit does not match.  " + policyCoverageALimit + " - " + reviewCoverageALimit + "\n";
            errorsExist = true;
        }
        if (!convertAndCompare(policyCoverageCLimit, reviewCoverageCLimit)) {
            errorLog = errorLog + "Coverage C Limit does not match.  " + policyCoverageCLimit + " - " + reviewCoverageCLimit + "\n";
            errorsExist = true;
        }
        if (!convertAndCompare(policyAD, reviewADlimit)) {
            errorLog = errorLog + "AccidentalDeath does not match.  " + policyAD + " - " + reviewADlimit + "\n";
            errorsExist = true;
        }
        if (!convertAndCompare(policyDamagePropOthers, reviewDamageToPropOthers)) {
            errorLog = errorLog + "Damage to property of others does not match.  " + policyDamagePropOthers + " - " + reviewDamageToPropOthers + "\n";
            errorsExist = true;
        }


        if (errorsExist) {
            throw new Exception(errorLog);
        }


    }

    @Test(dependsOnMethods = {"generatePropertyFullApp"})
    private void addCoveragesAndValidateLineReview() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.squirePolicy.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();


        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);

        //Additional Interest details
        AddingAdditionalInterest("Test" + StringsUtils.generateRandomNumberDigits(4), "LessorPerson");

        //adding Rated Year
        int newRatedYear = 2015;
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        propertyDetail.clickPropertyConstructionTab();
        constructionPage.setRatedYear(newRatedYear);
        constructionPage.clickOK();

        //Adding optional coverages
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.checkEarthquake(true);
        coverages.setIncludeMasonry(true);
        double newLimit = 1000;
        coverages.setGunIncreasedTheftLimit(newLimit);
        coverages.setSilverwareIncreasedTheftLimit(newLimit);
        coverages.setToolsIncreasedLimit(newLimit);
        coverages.setSaddlesTackIncreasedLimit(newLimit);
        double otherStructuresLimit = coverages.getOtherStructuresLimit();
        double coverageBLossOfUse = coverages.getCoverageBLossOfUse();


        //Adding dairy complex
        sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickAdd();
        PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.DairyComplex);
        property.setConstructionType(ConstructionTypePL.Frame);
        property.setFoundationType(FoundationType.FullBasement);

        propertyDetail.setPropertyType(PropertyTypePL.DairyComplex);
        propertyDetail.clickPropertyConstructionTab();
        constructionPage.setYearBuilt(property.getYearBuilt());
        constructionPage.setConstructionType(property.getConstructionType());
        constructionPage.setSquareFootage(property.getSquareFootage());
        constructionPage.setMeasurement(Measurement.SQFT);
        constructionPage.setFoundationType(property.getFoundationType());
        constructionPage.setRoofType(property.getRoofType());
        constructionPage.clickOK();
        int BuildingNumber = propertyDetail.getSelectedBuildingNum();

        sideMenu.clickSideMenuSquirePropertyCoverages();
        coverages.clickSpecificBuilding(1, BuildingNumber);
        coverages.setCoverageELimit(45000);
        coverages.setCoverageECoverageType(CoverageType.BroadForm);
        coverages.setCoverageEValuation(ValuationMethod.ActualCashValue);
        coverages.setLossIncomeExtraExpense(newLimit);

        GenericWorkorderLineReviewPL lineReview = new GenericWorkorderLineReviewPL(driver);

        sideMenu.clickSideMenuSquirePropertyCoverages();
        sideMenu.clickSideMenuSquirePropertyGlLineReview();
        int propertyTableRows = lineReview.getPropertyCoverageTableRowCount();
        String ratedYear = null;
        String additionalInterest = null;
        double lineReviewOtherStructure = 0.00;
        double lineReviewSewageBackup = 0.00;
        double lineReviewCoverageLossOfUse = 0.00;
        double lineReviewGunsLimit = 0.00;
        double lineReviewSilverwareLimit = 0.00;
        double lineReviewToolsLimit = 0.00;
        double lineReviewSaddleLimit = 0.00;
        double lineReviewLossOfIncome = 0.00;
        String lineReviewEarthQuake = null;
        for (int currentRow = 1; currentRow <= propertyTableRows; currentRow++) {
            String propertyType = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Property Type");
            System.out.println(propertyType);
            String ActualpropertyType = this.squirePolicy.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getpropertyType().getValue();
            if (ActualpropertyType.equals(propertyType)) {
                ratedYear = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Rated Yr.");
                additionalInterest = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Additional Interests");
            }
            switch (propertyType) {
                case "Other Structures":
                    lineReviewOtherStructure = Double.parseDouble(lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Limit").replace(",", ""));
                    break;
                case "Coverage B Loss Of Use":
                    lineReviewCoverageLossOfUse = Double.parseDouble(lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Limit").replace(",", ""));
                    break;
                case "Sewage System Backup":
                    lineReviewSewageBackup = Double.parseDouble(lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Limit").replace(",", ""));
                    break;
                case "Guns Increased Theft Limit":
                    lineReviewGunsLimit = Double.parseDouble(lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Limit").replace(",", ""));
                    break;
                case "Silverware Increased Theft Limit":
                    lineReviewSilverwareLimit = Double.parseDouble(lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Limit").replace(",", ""));
                    break;
                case "Tools Increased Limit":
                    lineReviewToolsLimit = Double.parseDouble(lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Limit").replace(",", ""));
                    break;
                case "Saddles and Tack Increased Limit":
                    lineReviewSaddleLimit = Double.parseDouble(lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Limit").replace(",", ""));
                    break;
                case "Loss of Income & Extra Expense":
                    lineReviewLossOfIncome = Double.parseDouble(lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Limit").replace(",", ""));
                    break;
                case "Coverage A":
                    lineReviewEarthQuake = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Earthquake").trim();
                default:
                    break;
            }
        }

        boolean errorsExist = false;
        String errorLog = null;

        if (!convertAndCompare(otherStructuresLimit, lineReviewOtherStructure)) {
            errorLog = errorLog + "Property Other Structure does not match.  " + otherStructuresLimit + " - " + lineReviewOtherStructure + "\n";
            errorsExist = true;
        }

        if (Integer.parseInt(ratedYear) != newRatedYear) {
            errorLog = errorLog + "Property Rated Year does not match.  " + ratedYear + " - " + newRatedYear + "\n";
            errorsExist = true;
        }

        if (!additionalInterest.contains("LessorPerson")) {
            errorLog = errorLog + "Property Additional Interest does not match.  " + additionalInterest + " -  LessorPerson \n";
            errorsExist = true;
        }

        if (lineReviewSewageBackup == 0.00) {
            errorLog = errorLog + "Property Sewage Backup is still null.  " + lineReviewSewageBackup + "\n";
            errorsExist = true;
        }
        if (!lineReviewEarthQuake.contains("Include")) {
            errorLog = errorLog + "Property Earthquake does not match.  " + lineReviewEarthQuake + " -  Include \n";
            errorsExist = true;
        }
        if (!convertAndCompare(coverageBLossOfUse, lineReviewCoverageLossOfUse)) {
            errorLog = errorLog + "Property Coverage B Loss Of Use does not match.  " + coverageBLossOfUse + " -  " + lineReviewCoverageLossOfUse + " \n";
            errorsExist = true;
        }
        if (!convertAndCompare(newLimit, lineReviewGunsLimit)) {
            errorLog = errorLog + "Property Guns Increased Theft Limit does not match.  " + newLimit + " -  " + lineReviewGunsLimit + " \n";
            errorsExist = true;
        }
        if (!convertAndCompare(newLimit, lineReviewSilverwareLimit)) {
            errorLog = errorLog + "Property Silverware Increased Theft Limit does not match.  " + newLimit + " -  " + lineReviewSilverwareLimit + " \n";
            errorsExist = true;
        }
        if (!convertAndCompare(newLimit, lineReviewToolsLimit)) {
            errorLog = errorLog + "Property Tools Increased Limit does not match.  " + newLimit + " -  " + lineReviewToolsLimit + " \n";
            errorsExist = true;
        }
        if (!convertAndCompare(newLimit, lineReviewSaddleLimit)) {
            errorLog = errorLog + "Property Saddles and Tack Increased Limit does not match.  " + newLimit + " -  " + lineReviewSaddleLimit + " \n";
            errorsExist = true;
        }

        if (!convertAndCompare(newLimit, lineReviewLossOfIncome)) {
            errorLog = errorLog + "Property Loss of Income Limit does not match.  " + newLimit + " -  " + lineReviewLossOfIncome + " \n";
            errorsExist = true;
        }

        if (errorsExist)
            Assert.fail(errorLog);
    }

    private void AddingAdditionalInterest(String firstName, String lastName) throws Exception {
        AddressInfo bankAddress = new AddressInfo();

		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(firstName,lastName, bankAddress);
        loc2Bldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
        loc2Bldg1AddInterest.setAddress(bankAddress);
        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LessorPL);

        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
        SearchAddressBookPC search = new SearchAddressBookPC(driver);
        search.searchForContact(true, loc2Bldg1AddInterest);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
        additionalInterests.setAddressListing("New...");
        additionalInterests.setContactEditAddressLine1(loc2Bldg1AddInterest.getAddress().getLine1());
        additionalInterests.setContactEditAddressCity(loc2Bldg1AddInterest.getAddress().getCity());
        additionalInterests.setContactEditAddressState(loc2Bldg1AddInterest.getAddress().getState());
        additionalInterests.setContactEditAddressAddressType(loc2Bldg1AddInterest.getAddress().getType());
        additionalInterests.setContactEditAddressZipCode(loc2Bldg1AddInterest.getAddress().getZip());
        additionalInterests.clickRelatedContactsTab();
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
    }

    private boolean convertAndCompare(double valueOne, double valueTwo) {

        System.out.println(valueOne);
        System.out.println(valueTwo);

        return valueOne == valueTwo;
    }
}
