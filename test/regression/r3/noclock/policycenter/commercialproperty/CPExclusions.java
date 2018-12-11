package regression.r3.noclock.policycenter.commercialproperty;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.CommercialProperty.PropertyCoverages;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialProperty;
import repository.gw.generate.custom.CPPCommercialPropertyLine;
import repository.gw.generate.custom.CPPCommercialPropertyLine_Coverages;
import repository.gw.generate.custom.CPPCommercialPropertyLine_ExclusionsConditions;
import repository.gw.generate.custom.CPPCommercialPropertyProperty;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.gw.generate.custom.CPPCommercialProperty_Building_Coverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyCommercialPropertyLineCPP;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_UWQuestions;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Coverages;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Details;


public class CPExclusions extends BaseTest {


    public GeneratePolicy myPolicyObj = null;


    private WebDriver driver;

    @SuppressWarnings("serial")
    @Test(enabled = true)
    public void generateQQPolicy() throws Exception {
        AddressInfo pniAddress = new AddressInfo(true);
        ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>();
        locationList.add(new PolicyLocation(pniAddress, true));


        //COMMERCIAL PROPERTY LINE
        CPPCommercialPropertyLine commercialPropertyLine = new CPPCommercialPropertyLine();
        commercialPropertyLine.setPropertyLineCoverages(new CPPCommercialPropertyLine_Coverages());
        commercialPropertyLine.setPropertyLineExclusionsConditions(new CPPCommercialPropertyLine_ExclusionsConditions());
        commercialPropertyLine.getPropertyLineCoverages().setEmployeeTheft(true);

        //LIST OF COMMERCIAL PROPERTY
        List<CPPCommercialPropertyProperty> commercialPropertyList = new ArrayList<CPPCommercialPropertyProperty>() {{
            this.add(new CPPCommercialPropertyProperty() {{
                this.setAddress(pniAddress);
                this.setCPPCommercialProperty_Building_List(new ArrayList<CPPCommercialProperty_Building>() {{
                    this.add(new CPPCommercialProperty_Building() {{
                        this.setCoverages(new CPPCommercialProperty_Building_Coverages(PropertyCoverages.BuildingCoverage) {{
                        }});
                    }});// end building 0
                }});
            }});
        }};

        CPPCommercialProperty commercialProperty = new CPPCommercialProperty() {{
            this.setCommercialPropertyLine(commercialPropertyLine);
            this.setCommercialPropertyList(commercialPropertyList);
        }};

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialProperty(commercialProperty)
                .withLineSelection(LineSelection.CommercialPropertyLineCPP)
                .withPolicyLocations(locationList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("CP Exclusions")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(pniAddress)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .isDraft()
                .build(GeneratePolicyType.QuickQuote);

    }


    /**
     * @Author jlarsen
     * @Requirement Commercial Property Product Model
     * @Description Exclude Certain Risks Inherent In Insurance Operations CR 25 23 Electable when Employee theft is selected
     * Exclude Designated Persons Or Classes Of Persons As Employees CR 25 01  ELECTABLE
     * Exclude Designated Premises CR 35 13  ELECTABLE
     * Exclude Specified Property CR 35 01  ELECTABLE
     * Exclude Unauthorized Advances, Require Annual Audit CR 25 25 Required when UW Question "Is the applicant/Insured a freternal organization of labor union? = Yes"
     * Exclusion Of Loss Due To Virus Or Bacteria CP 01 40  REQUIRED
     * @DATE Aug 21, 2017
     */
    @Test(dependsOnMethods = {"generateQQPolicy"}, enabled = true)
    public void testExclusions_CPProertyLine() throws Exception {
        SoftAssert softAssert = new SoftAssert();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        guidewireHelpers.editPolicyTransaction();

        GenericWorkorderCommercialPropertyCommercialPropertyLineCPP comProperty = new GenericWorkorderCommercialPropertyCommercialPropertyLineCPP(driver);
        comProperty.clickExclusionsConditionsTab();

        softAssert.assertTrue(guidewireHelpers.isElectable("Exclude Certain Risks Inherent In Insurance Operations CR 25 23"), "Exclude Certain Risks Inherent In Insurance Operations CR 25 23 was NOT ELECTABLE when Emplyee Theft was selected");
        myPolicyObj.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setEmployeeTheft(false);
        comProperty.fillOutCoveragesTab(myPolicyObj);
        comProperty.clickExclusionsConditionsTab();
        softAssert.assertTrue(!guidewireHelpers.isAvailable("Exclude Certain Risks Inherent In Insurance Operations CR 25 23"), "Exclude Certain Risks Inherent In Insurance Operations CR 25 23 WAS ELECTABLE when Emplyee Theft was NOT selected");

        softAssert.assertTrue(guidewireHelpers.isElectable("Exclude Designated Persons Or Classes Of Persons As Employees CR 25 01"), "Exclude Designated Persons Or Classes Of Persons As Employees CR 25 01 was NOT ELECTABLE");
        softAssert.assertTrue(guidewireHelpers.isElectable("Exclude Designated Premises CR 35 13"), "Exclude Designated Premises CR 35 13 was NOT ELECTABLE");
        softAssert.assertTrue(guidewireHelpers.isElectable("Exclude Specified Property CR 35 01"), "Exclude Specified Property CR 35 01 was NOT ELECTABLE");
        softAssert.assertTrue(guidewireHelpers.isRequired("Exclusion Of Loss Due To Virus Or Bacteria CP 01 40"), "Exclusion Of Loss Due To Virus Or Bacteria CP 01 40 was NOT REQUIRED");

        myPolicyObj.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setEmployeeTheft(true);
        comProperty.fillOutCoveragesTab(myPolicyObj);
        comProperty.clickUnderwritingQuestionsTab();

        GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_UWQuestions uwQuestions = new GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_UWQuestions(driver);
        uwQuestions.setIsApplicantAFraternalOrgOrLaborUnion(true);
        comProperty.clickExclusionsConditionsTab();
        softAssert.assertTrue(guidewireHelpers.isRequired("Exclude Unauthorized Advances, Require Annual Audit CR 25 25"), "Exclude Unauthorized Advances, Require Annual Audit CR 25 25 was NOT REQUIRED when Labor Union Question was answered Yes");
        softAssert.assertAll();
    }


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement Commercial Property Product Model
     * @Description Broken Or Cracked Glass Exclusion Form IDCP 31 1006 ELECTABLE
     * Roof Exclusion Endorsement IDCP 31 1004  ELECTIABLE WHEN BUILDNG COVERAGE IS SELECTED
     * Exclusion Of Loss Due To By-Products Of Production Or Processing Operations (Rental Properties) CP 10 34 NOT AVAILABLE WITH BUILDERS RISK CP 0017 OF CP 0018 IS SELCETED REQUIRED WHEN SQ FT RENTED TO OTHERS >0
     * Sprinkler Leakage Exclusion CP 10 56 ELECTABLE WHEN SPRINKLERS IS ANSWERED YES.
     * Theft Exclusion CP 10 33 NOT AVAILABLE WITH BUILDERS RISK, ELECTABLE WITH BUILDING COVERAGE BPP PROPERTY OF OTHERS, PERSONL PROPERTY OF OTHERS LEGAL LIABILITY
     * Vandalism Exclusion CP 10 55 ELECTABLE WHEN BUILDING COVERAGE BPP BUILDDERS RISK PROPERTY IN THE OPEN OF PERSON PROPERTY OF OTHERS IS SELECTED
     * Windstorm Or Hail Exclusion CP 10 54 ELECABLE WHEN BUILDNG COVERAGE, PROPERTY IN THE OPEN, OR BUILDERS RISK IS SELECTED
     * @DATE Aug 21, 2017
     */
    @Test(dependsOnMethods = {"generateQQPolicy"}, enabled = true)
    public void testExclusions_CPBuilding() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();


        GenericWorkorderCommercialPropertyPropertyCPP_Coverages propertyCoveragesPage = new GenericWorkorderCommercialPropertyPropertyCPP_Coverages(driver);
        propertyCoveragesPage.fillOutPropertyCoverages(myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0));
        GenericWorkorderCommercialPropertyPropertyCPP comProperty = new GenericWorkorderCommercialPropertyPropertyCPP(driver);
        comProperty.clickExclusionsConditionsTab();

        softAssert.assertTrue(guidewireHelpers.isElectable("Broken Or Cracked Glass Exclusion Form IDCP 31 1006"), "Broken Or Cracked Glass Exclusion Form IDCP 31 1006 was NOT ELECTABLE");
        softAssert.assertTrue(guidewireHelpers.isElectable("Roof Exclusion Endorsement IDCP 31 1004"), "Roof Exclusion Endorsement IDCP 31 1004 was NOT ELECTABLE");

        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0).getCoverages().getBuildingCoverageList().clear();
        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0).getCoverages().getBuildingCoverageList().add(PropertyCoverages.BuildersRiskCoverageForm_CP_00_20);

        propertyCoveragesPage.fillOutPropertyCoverages(myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0));
        comProperty.clickExclusionsConditionsTab();
        softAssert.assertFalse(guidewireHelpers.isAvailable("Roof Exclusion Endorsement IDCP 31 1004"), "Roof Exclusion Endorsement IDCP 31 1004 was Available when Building Coverage was not selected.");
        softAssert.assertFalse(guidewireHelpers.isAvailable("Exclusion Of Loss Due To By-Products Of Production Or Processing Operations (Rental Properties) CP 10 34"), "Exclusion Of Loss Due To By-Products Of Production Or Processing Operations (Rental Properties) CP 10 34 WAS available when Builders risk was selected.");

        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0).getCoverages().getBuildingCoverageList().clear();
        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0).getCoverages().getBuildingCoverageList().add(PropertyCoverages.BuildingCoverage);
        propertyCoveragesPage.fillOutPropertyCoverages(myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0));
        comProperty.clickExclusionsConditionsTab();
        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0).setPercentRentedToOthers("17");
        GenericWorkorderCommercialPropertyPropertyCPP_Details propertyDetail = new GenericWorkorderCommercialPropertyPropertyCPP_Details(driver);
        propertyDetail.fillOutBuildingCharacteristics(myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0));

        comProperty.clickExclusionsConditionsTab();
        softAssert.assertTrue(guidewireHelpers.isRequired("Exclusion Of Loss Due To By-Products Of Production Or Processing Operations (Rental Properties) CP 10 34"), "Exclusion Of Loss Due To By-Products Of Production Or Processing Operations (Rental Properties) CP 10 34 was NOT Required when % Rented to Others > 0.");
        softAssert.assertTrue(guidewireHelpers.isElectable("Sprinkler Leakage Exclusion CP 10 56"), "Sprinkler Leakage Exclusion CP 10 56 was not Electable when Sprinkles was set to YES.");

        softAssert.assertAll();
    }


}
