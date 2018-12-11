package regression.r2.noclock.policycenter.submission_property.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import helpers.DateUtils;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderLineReviewPL;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;

public class TestSquirePropertyLiabilityLineReviewPropertyClassications extends BaseTest {

    private GeneratePolicy myPolicyObjPL = null;

    private WebDriver driver;

    @Test()
    private void createPLPropertyAutoPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
        coverages.setUnderinsured(false);
        coverages.setAccidentalDeath(true);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact("Test", "PropClassReg", Gender.Male, DateUtils.convertStringtoDate("01/01/1979", "MM/dd/YYYY"));
        person.setMaritalStatus(MaritalStatus.Married);
        person.setRelationToInsured(RelationshipToInsured.Insured);
        person.setOccupation("Software");
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setEmergencyRoadside(true);
        vehicleList.add(toAdd);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locToAdd.setPlNumResidence(15);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(/*LineSelection.PersonalAutoLinePL, */LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "PropClass")
                .build(GeneratePolicyType.QuickQuote);
    }

    @Test(dependsOnMethods = "createPLPropertyAutoPolicy")
    public void validatePropertyClassification() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        //Login to the newly created account
        new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        //Adding multiple properties
        addMultiplePropertiesValidateClassificationCodes();

    }

    private void addMultiplePropertiesValidateClassificationCodes() throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        // adding multiple properties
        sideMenu.clickSideMenuSquirePropertyDetail();

        // Property Type=Residence Premises, Construction Type= Frame or Non-Frame	, A+	limit >= 60,000	year > 1968
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.setConstructionType(ConstructionTypePL.NonFrame);
        constructionPage.setYearBuilt(2005);
        constructionPage.clickOK();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.setCoverageALimit(65000);
        ArrayList<String> propertyClassficiations = new ArrayList<String>();
        propertyClassficiations.add(PropertyTypePL.ResidencePremises.getValue() + "\t" + ConstructionTypePL.NonFrame.getValue() + "\t 2005 \t A+");

        if (new GuidewireHelpers(driver).getRandBoolean()) {
            // Property Type= Residence Premises, Construction Type	= Modular/Manufactured, Foundation Type	 != None
            addNewPropertyCoverageLimit(PropertyTypePL.ResidencePremises, ConstructionTypePL.ModularManufactured, 59999, CoverageType.BroadForm, FoundationType.FullBasement, 1985);
            propertyClassficiations.add(PropertyTypePL.ResidencePremises.getValue() + "\t" + ConstructionTypePL.ModularManufactured.getValue() + "\t 1985 \t A");

            // Property Type=Residence Premises, Construction Type= Frame or Non-Frame	, B	 limit>= 15,000	 year <= 1954
            addNewPropertyCoverageLimit(PropertyTypePL.ResidencePremises, ConstructionTypePL.Frame, 20000, CoverageType.BroadForm, FoundationType.FullBasement, 1954);
            propertyClassficiations.add(PropertyTypePL.ResidencePremises.getValue() + "\t" + ConstructionTypePL.Frame.getValue() + "\t 1954 \t B");

            //Property Type=Residence Premises, 	Construction Type= Modular/Manufactured , Foundation Type	 = None	 A	>=40,000	> 1984
            addNewPropertyCoverageLimit(PropertyTypePL.ResidencePremises, ConstructionTypePL.ModularManufactured, 40000, CoverageType.BroadForm, FoundationType.None, 1985);
            propertyClassficiations.add(PropertyTypePL.ResidencePremises.getValue() + "\t" + ConstructionTypePL.ModularManufactured.getValue() + "\t 1985 \t A");

            //Property Type=Residence Premises, Construction Type=Mobile Home	, Foundation Type!= None C	>= 15,000	Any
            addNewPropertyCoverageLimit(PropertyTypePL.ResidencePremises, ConstructionTypePL.MobileHome, 15000, null, FoundationType.FullBasement, 1985);
            propertyClassficiations.add(PropertyTypePL.ResidencePremises.getValue() + "\t" + ConstructionTypePL.MobileHome.getValue() + "\t 1985 \t C");

            //Property Type=Residence Premises,Construction Type=Mobile Home, Foundation Type= None	D	>=4,000	Any
            addNewPropertyCoverageLimit(PropertyTypePL.ResidencePremises, ConstructionTypePL.MobileHome, 4000, null, FoundationType.None, 1950);
            propertyClassficiations.add(PropertyTypePL.ResidencePremises.getValue() + "\t" + ConstructionTypePL.MobileHome.getValue() + "\t 1950 \t D");

            //Property Type=Condo Residences Premises, Construction Type= Frame or Non-Frame	A	>= 40,000	> 1954
            addNewPropertyCoverageLimit(PropertyTypePL.CondominiumResidencePremise, ConstructionTypePL.Frame, 40000, CoverageType.BroadForm, FoundationType.FullBasement, 1955);
            propertyClassficiations.add(PropertyTypePL.CondominiumResidencePremise.getValue() + "\t" + ConstructionTypePL.Frame.getValue() + "\t 1955 \t A");

            //Property Type=Vacation Home, Construction Type=Frame or Non-Frame	, B	>= 15,000	<= 1954
            addNewPropertyCoverageLimit(PropertyTypePL.VacationHome, ConstructionTypePL.NonFrame, 41000, CoverageType.BroadForm, FoundationType.FullBasement, 1955);
            propertyClassficiations.add(PropertyTypePL.VacationHome.getValue() + "\t" + ConstructionTypePL.NonFrame.getValue() + "\t 1955 \t A");
        } else {
            //Property Type=Vacation Home, Construction Type=	Mobile Home	, Foundation Type != None	C	>= 15,000	Any
            addNewPropertyCoverageLimit(PropertyTypePL.VacationHome, ConstructionTypePL.Frame, 15000, null, FoundationType.FullBasement, 1999);
            propertyClassficiations.add(PropertyTypePL.VacationHome.getValue() + "\t" + ConstructionTypePL.Frame.getValue() + "\t 1999 \t B");

            //Property Type=Condo Vacation Home	, Construction Type	= Frame or Non-Frame	A	>= 40,000	> 1954
            addNewPropertyCoverageLimit(PropertyTypePL.CondominiumVacationHome, ConstructionTypePL.Frame, 40000, CoverageType.BroadForm, FoundationType.FullBasement, 1955);
            propertyClassficiations.add(PropertyTypePL.CondominiumVacationHome.getValue() + "\t" + ConstructionTypePL.Frame.getValue() + "\t 1955 \t A");

            //Property Type=Condo Vacation Home	, Construction Type	= Frame or Non-Frame	B	>= 20,000	<= 1954
            addNewPropertyCoverageLimit(PropertyTypePL.CondominiumVacationHome, ConstructionTypePL.NonFrame, 20000, CoverageType.BroadForm, FoundationType.FullBasement, 1954);
            propertyClassficiations.add(PropertyTypePL.CondominiumVacationHome.getValue() + "\t" + ConstructionTypePL.NonFrame.getValue() + "\t 1954 \t B");


            //DE5765 - TR/Legacy & New Submission - Dwelling Premises can't be class A

            //Property Type=Dwelling Premises	, Construction Type	= Frame or Non-Frame	B	>= 20,000	<= 1954
            addNewPropertyCoverageLimit(PropertyTypePL.DwellingPremises, ConstructionTypePL.NonFrame, 40000, CoverageType.BroadForm, FoundationType.FullBasement, 1999);
            propertyClassficiations.add(PropertyTypePL.DwellingPremises.getValue() + "\t" + ConstructionTypePL.NonFrame.getValue() + "\t 1999 \t B");

            addNewPropertyCoverageLimit(PropertyTypePL.DwellingPremises, ConstructionTypePL.ModularManufactured, 21000, CoverageType.BroadForm, FoundationType.None, 1999);
            propertyClassficiations.add(PropertyTypePL.DwellingPremises.getValue() + "\t" + ConstructionTypePL.ModularManufactured.getValue() + "\t 1999 \t B");

            addNewPropertyCoverageLimit(PropertyTypePL.DwellingPremises, ConstructionTypePL.ModularManufactured, 21000, CoverageType.BroadForm, FoundationType.FullBasement, 1999);
            propertyClassficiations.add(PropertyTypePL.DwellingPremises.getValue() + "\t" + ConstructionTypePL.ModularManufactured.getValue() + "\t 1999 \t B");

            //Property Type=Condo Dwelling Premises	, Construction Type	= Frame or Non-Frame	B	>= 20,000	<= 1954
            addNewPropertyCoverageLimit(PropertyTypePL.CondominiumDwellingPremises, ConstructionTypePL.Frame, 41000, CoverageType.BroadForm, FoundationType.FullBasement, 1999);
            propertyClassficiations.add(PropertyTypePL.CondominiumDwellingPremises.getValue() + "\t" + ConstructionTypePL.Frame.getValue() + "\t 1999 \t B");
        }
        //validing in lineReview
        validatePropertyClassificationsInLineReview(propertyClassficiations);

    }

    private void validatePropertyClassificationsInLineReview(ArrayList<String> propertyClassficiations) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        boolean testFailed = false;
        String errorMessage = "";
        sideMenu.clickSideMenuSquirePropertyLineReview();
        GenericWorkorderLineReviewPL lineReview = new GenericWorkorderLineReviewPL(driver);
        lineReview.clickSectionOneCoverages();
        int propertyTableRows = lineReview.getPropertyCoverageTableRowCount();

        for (int listprop = 0; listprop < propertyClassficiations.size(); listprop++) {
            boolean valueFound = false;
            String[] tempArray = propertyClassficiations.get(listprop).split("\t");
            for (int currentRow = 1; currentRow < propertyTableRows; currentRow++) {
                String propertyType = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Property Type");
                String propertyNum = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Bldg #");
                if (!propertyNum.trim().isEmpty()) {
                    if (Integer.parseInt(propertyNum) == (listprop + 1)) {
                        String constructionType = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Const. Type");
                        String yearBuilt = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Yr. Built");
                        String buildingClass = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Building Class");
                        if (buildingClass != null && !buildingClass.isEmpty()) {
                            if (tempArray[0].trim().contains(propertyType) && tempArray[1].trim().contains(constructionType) && tempArray[2].trim().contains(yearBuilt) && tempArray[3].trim().contains(buildingClass)) {
                                valueFound = true;
                                break;
                            }
                        }
                    }
                }

            }

            if (!valueFound) {
                testFailed = true;
                errorMessage = errorMessage + "\n Expected Building Class : " + tempArray[0].trim() + " - " + tempArray[1].trim() + " Number: " + listprop + " is not displayed. ";
            }
        }

        if (testFailed)
            Assert.fail(driver.getCurrentUrl() + errorMessage);
    }

    private void addNewPropertyCoverageLimit(PropertyTypePL propertyType, ConstructionTypePL constructionType, double limit, CoverageType CType, FoundationType foundationType, int yearBuilt) throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        PLPolicyLocationProperty property = new PLPolicyLocationProperty(propertyType);
        property.setConstructionType(constructionType);
        property.setFoundationType(foundationType);
        property.setYearBuilt(yearBuilt);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickAdd();
        propertyDetail.fillOutPropertyDetails_QQ(myPolicyObjPL.basicSearch, property);

        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
        constructionPage.setCoverageAPropertyDetailsQQ(property);
        constructionPage.clickProtectionDetails();
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        protectionPage.setProtectionPageQQ(property);
        protectionPage.clickOK();
        int BuildingNumber = propertyDetail.getSelectedBuildingNum();

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1, BuildingNumber);
        coverages.setCoverageALimit(limit);
        if (!constructionType.equals(ConstructionTypePL.MobileHome)) {
            coverages.setCoverageAIncreasedReplacementCost(property.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
        }
        coverages.setCoverageCValuation(property.getPropertyCoverages());
        if (property.getPropertyCoverages().getCoverageC().getValuationMethod().equals(ValuationMethod.ReplacementCost)) {
            if (CType != null) {
                coverages.selectCoverageCCoverageType(CType);
            }
        }
    }
}



























