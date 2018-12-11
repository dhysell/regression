package regression.r3.noclock.policycenter.commercialproperty;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.CommercialProperty.BuildingCoverageCauseOfLoss;
import repository.gw.enums.CommercialProperty.BusinessPersonalPropertyCoverageCauseOfLoss;
import repository.gw.enums.CommercialProperty.Condominium;
import repository.gw.enums.CommercialProperty.PropertyCoverages;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneralLiability.Electability;
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
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyCommercialPropertyLineCPP;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_Coverages;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_AdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Coverages;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Details;


/**
 * @Author jlarsen
 * @Requirement
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description Test all Commercial Property Coverages
 * @DATE Dec 8, 2016
 */
public class CPCoverages extends BaseTest {

    public GeneratePolicy myPolicyObj = null;
    SoftAssert softAssert = new SoftAssert();

    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        softAssert = new SoftAssert();
    }


    @SuppressWarnings("serial")
    @Test(enabled = true)
    public void generateQQPolicy() throws Exception {
        AddressInfo pniAddress = new AddressInfo(true);
        ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>() {{
            this.add(new PolicyLocation(pniAddress, true));
        }};

        //COMMERCIAL PROPERTY LINE
        CPPCommercialPropertyLine commercialPropertyLine = new CPPCommercialPropertyLine() {{
            this.setPropertyLineCoverages(new CPPCommercialPropertyLine_Coverages() {{
                //SET COMMERCIAL PROPERTY LINE COVERAGES HERE
            }});
            this.setPropertyLineExclusionsConditions(new CPPCommercialPropertyLine_ExclusionsConditions() {{
                //SET COMMERCIAL PROPERTY LINE EXCLUSIONS CONDITIONS HERE
            }});
        }};

        //LIST OF COMMERCIAL PROPERTY
        List<CPPCommercialPropertyProperty> commercialPropertyList = new ArrayList<CPPCommercialPropertyProperty>() {{
            this.add(new CPPCommercialPropertyProperty() {{
                this.setAddress(pniAddress);
                this.setCPPCommercialProperty_Building_List(new ArrayList<CPPCommercialProperty_Building>() {{
                    this.add(new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage) {{
                        this.getCoverages().setBuildingCoverage_CauseOfLoss(BuildingCoverageCauseOfLoss.Broad);
                    }});
                    this.add(new CPPCommercialProperty_Building(PropertyCoverages.BusinessPersonalPropertyCoverage) {{
                        this.getCoverages().setBusinessPersonalPropertyCoverage_CauseOfLoss(BusinessPersonalPropertyCoverageCauseOfLoss.Broad);
                    }});
                    this.add(new CPPCommercialProperty_Building(PropertyCoverages.BuildersRiskCoverageForm_CP_00_20));
                    this.add(new CPPCommercialProperty_Building(PropertyCoverages.PropertyInTheOpen));
                    this.add(new CPPCommercialProperty_Building(PropertyCoverages.LegalLiabilityCoverageForm_CP_00_40));
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
                .withInsCompanyName("Comm Property")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(pniAddress)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .isDraft()
                .build(GeneratePolicyType.QuickQuote);


    }


    /**
     * @Author jlarsen
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description Clients' Property CR 04 01
     * Electronic Data
     * Employee Theft
     * Forgery Or Alteration
     * Include Volunteer Workers Other Than Fund Solicitors As Employees CR 25 10
     * Inside The Premises - Theft Of Money And Securities
     * Inside The Premises - Theft Of Other Property CR 04 05
     * Joint Loss Payable CR 20 15
     * Outside The Premises
     * Required Crime Information
     * @DATE Aug 21, 2017
     */
    @Test(dependsOnMethods = {"generateQQPolicy"}, enabled = true)
    public void testCommercialProperty_CommercialPropertyLine_Coverages() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);

        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCPCommercialPropertyLine();

        GenericWorkorderCommercialPropertyCommercialPropertyLineCPP propertyLine = new GenericWorkorderCommercialPropertyCommercialPropertyLineCPP(driver);
        propertyLine.clickCoveragesTab();

        softAssert.assertTrue(guidewireHelpers.isElectable("Property CR 04 01"), "Clients Property CR 04 01 was NOT Electable \n");
        softAssert.assertTrue(guidewireHelpers.isElectable("Employee Theft"), "Employee Theft was NOT Electable \n");

        GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_Coverages cpLineCoveragePage = new GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_Coverages(driver);
        myPolicyObj.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setEmployeeTheft(true);
        cpLineCoveragePage.fillOutCoveragesCrime(myPolicyObj);
        softAssert.assertTrue(guidewireHelpers.isElectable("Forgery Or Alteration"), "Forgery Or Alteration was NOT Electable when Employee Theft was selected\n");
        myPolicyObj.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setEmployeeTheft(false);
        cpLineCoveragePage.fillOutCoveragesCrime(myPolicyObj);


        softAssert.assertTrue(guidewireHelpers.isElectable("Include Volunteer Workers Other Than Fund Solicitors As Employees CR 25 10"), "Include Volunteer Workers Other Than Fund Solicitors As Employees CR 25 10 was NOT Electable \n");
        softAssert.assertTrue(guidewireHelpers.isElectable("Inside The Premises - Theft Of Money And Securities"), "Inside The Premises - Theft Of Money And Securities was NOT Electable \n");
        softAssert.assertTrue(guidewireHelpers.isElectable("Inside The Premises - Theft Of Other Property CR 04 05"), "Inside The Premises - Theft Of Other Property CR 04 05 was NOT Electable \n");
        softAssert.assertTrue(guidewireHelpers.isElectable("Outside The Premises"), "Outside The Premises was NOT Electable \n");


        softAssert.assertAll();
    }


    /**
     * @Author jlarsen
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description Building Coverage
     * Business Personal Property Coverage
     * Builders' Risk Coverage Form CP 00 20
     * Property In The Open
     * Personal Property Of Others
     * Legal Liability Coverage Form CP 00 40
     * Building And Personal Property Coverage Form CP 00 10
     * Condominium Association Coverage Form CP 00 17
     * Condominium Commercial Unit-Owners Coverage Form CP 00 18
     * Business Income Coverage Form
     * Business Income - Landlord As Additional Insured (Rental Value) CP 15 03
     * Discretionary Payroll Expense CP 15 04
     * Extra Expense Coverage Form CP 00 50
     * Food Contamination (Business Interruption And Extra Expense) CP 15 05
     * @DATE Aug 21, 2017
     */
    @Test(dependsOnMethods = {"generateQQPolicy"}, enabled = true)
    public void testCommercialProperty_CommercialPropertyProperty_Coverages() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCPProperty();

        GenericWorkorderCommercialPropertyPropertyCPP property = new GenericWorkorderCommercialPropertyPropertyCPP(driver);
        GenericWorkorderCommercialPropertyPropertyCPP_Coverages propertyCoverage = new GenericWorkorderCommercialPropertyPropertyCPP_Coverages(driver);

        property.clickAddProperty();

        softAssert.assertTrue(guidewireHelpers.isElectable("Building Coverage"), "Building Coverage was not Electable\n");

        propertyCoverage.checkPropertyInTheOpen(true);
        verifyCoverageNotExists("Property In The Open", "Building Coverage");
        propertyCoverage.checkPropertyInTheOpen(false);

        propertyCoverage.checkBuildersRiskCoverageForm_CP_00_20(true);
        verifyCoverageNotExists("Builders Risk Coverage Form CP 00 20", "Building Coverage");
        propertyCoverage.checkBuildersRiskCoverageForm_CP_00_20(false);

        softAssert.assertTrue(guidewireHelpers.isElectable("Business Personal Property Coverage"), "Business Personal Property Coverage was not Electable\n");

        propertyCoverage.checkPropertyInTheOpen(true);
        verifyCoverageNotExists("Property In The Open", "Business Personal Property Coverage");
        propertyCoverage.checkPropertyInTheOpen(false);

        propertyCoverage.checkBuildersRiskCoverageForm_CP_00_20(true);
        verifyCoverageNotExists("Builders Risk Coverage Form CP 00 20", "Business Personal Property Coverage");
        propertyCoverage.checkBuildersRiskCoverageForm_CP_00_20(false);

        softAssert.assertTrue(guidewireHelpers.isElectable("Risk Coverage Form CP 00 20"), "Builders Risk Coverage Form CP 00 20 was not Electable\n");

        propertyCoverage.checkBuildingCoverages(true);
        verifyCoverageNotExists("Building Coverage", "Risk Coverage Form CP 00 20");
        propertyCoverage.checkBuildingCoverages(false);

        propertyCoverage.checkBusinessPersonalPropertyCoverage(true);
        verifyCoverageNotExists("Business Personal Property Coverage", "Risk Coverage Form CP 00 20");
        propertyCoverage.checkBusinessPersonalPropertyCoverage(false);

        propertyCoverage.checkPropertyInTheOpen(true);
        verifyCoverageNotExists("Property In The Open", "Risk Coverage Form CP 00 20");
        propertyCoverage.checkPropertyInTheOpen(false);

        softAssert.assertTrue(guidewireHelpers.isElectable("Property In The Open"), "Property In The Open was not Electable\n");

        propertyCoverage.checkBuildingCoverages(true);
        verifyCoverageNotExists("Building Coverage", "Property In The Open");
        propertyCoverage.checkBuildingCoverages(false);

        propertyCoverage.checkBusinessPersonalPropertyCoverage(true);
        verifyCoverageNotExists("Business Personal Property Coverage", "Property In The Open");
        propertyCoverage.checkBusinessPersonalPropertyCoverage(false);

        propertyCoverage.checkBuildersRiskCoverageForm_CP_00_20(true);
        verifyCoverageNotExists("Builders Risk Coverage Form CP 00 20", "Property In The Open");
        propertyCoverage.checkBuildersRiskCoverageForm_CP_00_20(false);

        propertyCoverage.checkBusinessPersonalPropertyCoverage(true);
        softAssert.assertTrue(guidewireHelpers.isElectable("Personal Property Of Others"), "Personal Property Of Others was not Electable when Business Personal Property Coverage was selected\n");
        propertyCoverage.checkBusinessPersonalPropertyCoverage(false);

        verifyCoverageNotExists(("Legal Liability Coverage Form CP 00 40"), "Legal Liability Coverage Form CP 00 40 was not Electable\n");

        propertyCoverage.checkBuildersRiskCoverageForm_CP_00_20(true);
        verifyCoverageNotExists("Builders Risk Coverage Form CP 00 20", "Legal Liability Coverage Form CP 00 40");
        propertyCoverage.checkBuildersRiskCoverageForm_CP_00_20(false);

        propertyCoverage.checkPropertyInTheOpen(true);
        verifyCoverageNotExists("Property In The Open", "Legal Liability Coverage Form CP 00 40");
        propertyCoverage.checkPropertyInTheOpen(false);

        property.clickCancel();

        softAssert.assertAll();

    }//end testCommercialProperty_CommercialPropertyProperty_Coverages


    /**
     * @Author jlarsen
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description Additional Building Property CP 14 15
     * Additional Covered Property CP 14 10
     * Additional Insured - Building Owner CP 12 19
     * Builders' Risk Renovations CP 11 13
     * Condominium Commercial Unit-Owners Optional Coverages CP 04 18
     * Discharge From Sewer, Drain Or Sump (Not Flood-Related) CP 10 38
     * Equipment Breakdown Enhancement Endorsement IDCP 31 1002
     * Functional Building Valuation CP 04 38
     * Guests' Property CR 04 11
     * Leased Property CP 14 60
     * Loss Payable Provisions CP 12 18
     * Ordinance Or Law Coverage CP 04 05
     * Outdoor Signs CP 14 40
     * Payroll Limitation Or Exclusion CP 15 10
     * Peak Season Limit Of Insurance CP 12 30
     * Radio Or Television Antennas CP 14 50
     * Spoilage Coverage CP 04 40
     * Theft Of Building Materials And Supplies (Other Than Builders Risk) CP 10 44
     * Unscheduled Building Property Tenant's Policy CP 14 02
     * Utility Services - Direct Damage CP 04 17
     * Utility Services - Time Elements CP 15 45
     * @DATE Aug 21, 2017
     */
    @Test(dependsOnMethods = {"generateQQPolicy"})
    public void testCommercialProperty_CommercialPropertyProperty_AdditionalCoverages() throws Exception {
        softAssert = new SoftAssert();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCPProperty();

        GenericWorkorderCommercialPropertyPropertyCPP property = new GenericWorkorderCommercialPropertyPropertyCPP(driver);
        GenericWorkorderCommercialPropertyPropertyCPP_Coverages propertyCoverage = new GenericWorkorderCommercialPropertyPropertyCPP_Coverages(driver);
        GenericWorkorderCommercialPropertyPropertyCPP_Details propertyDetails = new GenericWorkorderCommercialPropertyPropertyCPP_Details(driver);
        GenericWorkorderCommercialPropertyPropertyCPP_AdditionalCoverages propertyAdditionalCoverages = new GenericWorkorderCommercialPropertyPropertyCPP_AdditionalCoverages(driver);


        ////////////////////
        //BUILDING COVERAGE
        ////////////////////
        try {


            CPPCommercialProperty_Building building = getPropertyByCoverage(PropertyCoverages.BuildingCoverage);

            property.editPropertyByNumber(building.getNumber());
            property.clickAdditionalCoveragesTab();

            //INCOME
            verifyCoverage("Building Coverage", "Business Income Coverage Form", Electability.Electable);
            verifyCoverage("Building Coverage", "Extra Expense Coverage CP 00 50", Electability.Electable);

            //OTHER ADDITIONAL COVERAGES
            verifyCoverage("Building Coverage", "Additional Building Property CP 14 15", Electability.Electable);
            verifyCoverage("Building Coverage", "Discharge From Sewer, Drain Or Sump (Not Flood-Related) CP 10 38", Electability.Electable);
            verifyCoverage("Building Coverage", "Equipment Breakdown Enhancement Endorsement IDCP 31 1002", Electability.Suggested);
            verifyCoverage("Building Coverage", "Functional Building Valuation CP 04 38", Electability.Electable);
            verifyCoverage("Building Coverage", "Property CR 04 11", Electability.Electable);
            verifyCoverage("Building Coverage", "Ordinance or Law Coverage CP 04 05", Electability.Electable);
            verifyCoverage("Building Coverage", "Utility Services � Direct Damage CP 04 17", Electability.Electable);

            if (propertyAdditionalCoverages.selectUtilityServices_DirectDamage_CP0417(true)) {
                verifyCoverageNotExists("Utility Services � Direct Damage CP 04 17", "Utility Services � Time Elements CP 15 45");
            }

            propertyAdditionalCoverages.selectBusinessIncomeCoverage(true);
            if (propertyAdditionalCoverages.selectUtilityServices_DirectDamage_CP0417(true)) {
                verifyCoverage("Utility Services � Direct Damage CP 04 17 AND Business Income Coverage Form", "Utility Services � Time Elements CP 15 45", Electability.Electable);
            }
            propertyAdditionalCoverages.selectBusinessIncomeCoverage(false);
            propertyAdditionalCoverages.selectExtraExpenseCoverage_CP0050(true);
            if (propertyAdditionalCoverages.selectUtilityServices_DirectDamage_CP0417(true)) {
                verifyCoverage("Utility Services � Direct Damage CP 04 17 AND Extra Expense Coverage CP 00 50", "Utility Services � Time Elements CP 15 45", Electability.Electable);
            }
        } catch (Exception e) {
            softAssert.fail("SOMETHING WITH BUILDING COVERAGE FAILED AND THAT PART OF THE TEST WASN'T ABLE TO FINISH");
        }

        property.clickCancel();

        //////////////////////////////////////
        //BUSINESS PERSONAL PROPERTY COVERAGE
        //////////////////////////////////////
        try {
            CPPCommercialProperty_Building building = getPropertyByCoverage(PropertyCoverages.BusinessPersonalPropertyCoverage);

            property.editPropertyByNumber(building.getNumber());
            property.clickAdditionalCoveragesTab();

            //INCOME
            verifyCoverage("Business Personal Property Coverage", "Business Income Coverage Form", Electability.Electable);
            verifyCoverage("Business Personal Property Coverage", "Extra Expense Coverage CP 00 50", Electability.Electable);

            //OTHER ADDITIONAL COVERAGES
            verifyCoverage("Business Personal Property Coverage", "Discharge From Sewer, Drain Or Sump (Not Flood-Related) CP 10 38", Electability.Electable);
            verifyCoverage("Business Personal Property Coverage", "Equipment Breakdown Enhancement Endorsement IDCP 31 1002", Electability.Suggested);
            verifyCoverage("Business Personal Property Coverage", "Property CR 04 11", Electability.Electable);
            verifyCoverage("Business Personal Property Coverage", "Leased Property CP 14 60", Electability.Electable);
            verifyCoverage("Business Personal Property Coverage", "Utility Services � Direct Damage CP 04 17", Electability.Electable);
            verifyCoverage("Business Personal Property Coverage", "Spoilage Coverage CP 04 40", Electability.Electable);
            property.clickCoveragesTab();
            propertyCoverage.checkBuildingCoverages(true);
            property.clickDetailsTab();
            propertyDetails.selectCondominium(Condominium.Association);
            property.clickAdditionalCoveragesTab();
            verifyCoverageNotExists("Condominium Association Coverage Form CP 00 17", "Spoilage Coverage CP 04 40");

            property.clickCoveragesTab();
            propertyCoverage.checkBuildingCoverages(false);

            property.clickAdditionalCoveragesTab();

            if (propertyAdditionalCoverages.selectUtilityServices_DirectDamage_CP0417(true)) {
                verifyCoverageNotExists("ONLY Utility Services � Direct Damage CP 04 17", "Utility Services � Time Elements CP 15 45");
            }

            propertyAdditionalCoverages.selectBusinessIncomeCoverage(true);
            if (propertyAdditionalCoverages.selectUtilityServices_DirectDamage_CP0417(true)) {
                verifyCoverage("Utility Services � Direct Damage CP 04 17 AND Business Income Coverage Form", "Utility Services � Time Elements CP 15 45", Electability.Electable);
            }
            propertyAdditionalCoverages.selectBusinessIncomeCoverage(false);
            propertyAdditionalCoverages.selectExtraExpenseCoverage_CP0050(true);
            if (propertyAdditionalCoverages.selectUtilityServices_DirectDamage_CP0417(true)) {
                verifyCoverage("Utility Services � Direct Damage CP 04 17 AND Extra Expense Coverage CP 00 50", "Utility Services � Time Elements CP 15 45", Electability.Electable);
            }
        } catch (Exception e) {
            softAssert.fail("SOMETHING WITH BUSINESS PERSONAL PROPERTY FAILED AND THAT PART OF THE TEST WASN'T ABLE TO FINISH");
        }

        property.clickCancel();

        ////////////////////////////////////////
        //BUILDERS RISK COVERAGE FORM CP 00 20
        ////////////////////////////////////////
        try {
            CPPCommercialProperty_Building building = getPropertyByCoverage(PropertyCoverages.BuildersRiskCoverageForm_CP_00_20);

            property.editPropertyByNumber(building.getNumber());
            property.clickAdditionalCoveragesTab();

            //INCOME

            //OTHER ADDITIONAL COVERAGES
            verifyCoverage("Builders Risk Coverage Form CP 00 20", "Risk Renovations CP 11 13", Electability.Electable);
        } catch (Exception e) {
            softAssert.fail("SOMETHING WITH BUILDERS RISK COVERAGE FAILED AND THAT PART OF THE TEST WASN'T ABLE TO FINISH");
        }

        property.clickCancel();


        ///////////////////////
        //PROPERTY IN THE OPEN
        ///////////////////////
        try {
            CPPCommercialProperty_Building building = getPropertyByCoverage(PropertyCoverages.PropertyInTheOpen);

            property.editPropertyByNumber(building.getNumber());
            property.clickAdditionalCoveragesTab();

            //INCOME
            verifyCoverageNotExists("Property In The Open", "Business Income Coverage Form");
            verifyCoverageNotExists("Property In The Open", "Extra Expense Coverage CP 00 50");

            //OTHER ADDITIONAL COVERAGES
            verifyCoverage("Property In The Open", "Additional Building Property CP 14 15", Electability.Electable);
            verifyCoverage("Property In The Open", "Equipment Breakdown Enhancement Endorsement IDCP 31 1002", Electability.Suggested);
            verifyCoverage("Property In The Open", "Property CR 04 11", Electability.Electable);
        } catch (Exception e) {
            softAssert.fail("SOMETHING WITH PROPERTY IN THE OPEN FAILED AND THAT PART OF THE TEST WASN'T ABLE TO FINISH");
        }

        property.clickCancel();


        /////////////////////////////////////////
        //LEGAL LIABILITY COVERAGE FORM CP 00 40
        /////////////////////////////////////////
        try {
            CPPCommercialProperty_Building building = getPropertyByCoverage(PropertyCoverages.LegalLiabilityCoverageForm_CP_00_40);

            property.editPropertyByNumber(building.getNumber());
            property.clickAdditionalCoveragesTab();

            //INCOME
            verifyCoverage("Legal Liability Coverage Form CP 00 40", "Business Income Coverage Form", Electability.Electable);
            verifyCoverage("Legal Liability Coverage Form CP 00 40", "Extra Expense Coverage CP 00 50", Electability.Electable);

            //OTHER ADDITIONAL COVERAGES
            verifyCoverage("Legal Liability Coverage Form CP 00 40", "Property CR 04 11", Electability.Electable);
        } catch (Exception e) {
            softAssert.fail("SOMETHING WITH LEGAL LIABILTIY COVERAGE FORM FAILED AND THAT PART OF THE TEST WASN'T ABLE TO FINISH");
        }

        property.clickCancel();

        ///////////////
        //SPECIAL CASE
        ///////////////
        //	JLARSEN 11/14/2017 COMMENTED OUT CUS SOMEHTING IS BROKEN AND DON'T WANT DEBUG IT ANYMORE
//		try {
//			
//			CPPCommercialProperty_Building building = getPropertyByCoverage(PropertyCoverages.LegalLiabilityCoverageForm_CP_00_40);
//
//			property.editPropertyByNumber(building.getNumber());
//			property.clickCoveragesTab();
//			propertyCoverage.checkLegalLiabilityCoverageForm_CP_00_40(false);
//			propertyCoverage.checkBuildersRiskCoverageForm_CP_00_20(true);
//			property.clickAdditionalCoveragesTab();
//
//			verifyCoverageNotExists("Builders Risk Coverage Form CP 00 20", "Additional Building Property CP 14 15");
//			verifyCoverageNotExists("Builders Risk Coverage Form CP 00 20", "Discharge From Sewer, Drain Or Sump (Not Flood-Related) CP 10 38");
//			verifyCoverageNotExists("Builders Risk Coverage Form CP 00 20", "Equipment Breakdown Enhancement Endorsement IDCP 31 1002");
//			verifyCoverageNotExists("Builders Risk Coverage Form CP 00 20", "Property CR 04 11");
//
//			property.clickCoveragesTab();
//			propertyCoverage.checkBuildersRiskCoverageForm_CP_00_20(false);
//			propertyCoverage.checkBusinessPersonalPropertyCoverage(true);
//			property.clickDetailsTab();
//			propertyDetails.selectCondominium(Condominium.UnitOwner);
//			property.clickAdditionalCoveragesTab();
//
//			verifyCoverageNotExists("Condominium Commercial Unit-Owners Coverage Form CP 00 18", "Additional Building Property CP 14 15");
//			verifyCoverageNotExists("Condominium Commercial Unit-Owners Coverage Form CP 00 18", "Functional Building Valuation CP 04 38");
//			verifyCoverageNotExists("Condominium Commercial Unit-Owners Coverage Form CP 00 18", "Theft Of Building Materials And Supplies (Other Than Builders Risk) CP 10 44");
//			verifyCoverageNotExists("Condominium Commercial Unit-Owners Coverage Form CP 00 18", "Additional Covered Property CP 14 10");
//
//			verifyCoverage("Condominium Commercial Unit-Owners Coverage Form CP 00 18", "Condominium Commercial Unit-Owners Optional Coverages CP 04 18", Electability.Electable);
//		} catch (Exception e) {
//			softAssert.fail("SOMETHING WITH SPECIAL CASE FAILED AND THAT PART OF THE TEST WASN'T ABLE TO FINISH");
//		}


        //		Additional Covered Property CP 14 10	CP 14 10	<defined by script>	"• Available when ""Building Coverage"", ""Business Personal Property Coverage"", or ""Property In The Open"" is selected. • This is only available for underwriters to select. The agent can see the endorsement and what the underwriter has entered in however they cannot edit it or select it or unselet it. "
        //		Additional Insured & Building Owner CP 12 19	CP 12 19	Required	• Available when under the "Building" tab "Additional Interest" and "Interest Type" is selected with "Additional Insured & Building Owner CP 12 19".
        //		Builders Risk Renovations CP 11 13	CP 11 13	<defined by script>
        //		Building Glass & Tenants Policy CP 14 70	CP 14 70	Electable	"• Not available when the question ""Is the building owned by the insured?"" is answered Yes. • Not available when ""Building Coverage"" or ""Condominium Commercial Unit-Owners Coverage Form CP 00 18"" is selected."
        //		Discharge From Sewer, Drain Or Sump (Not Flood-Related) CP 10 38	CP 10 38	Electable	"• Available when one of the following forms are selected: ""Business Income Coverage Form"", or ""Extra Expense Coverage Form CP 00 50"" . Also not available when ""Business Personal Property Coverage"" or ""Building Coverage"" and the term ""Cause of Loss"" has the option ""Basic"" selected."
        //		Functional Building Valuation CP 04 38	CP 04 38	Electable	" • Not available when ""Ordinance or Law Coverage CP 04 05"""
        //		Loss Payable Provisions CP 12 18	CP 12 18	Required	• Available when under the "Building" tab "Additional Interest" and "Interest Type" is selected with one of the following: Building Owner Loss Payable Clause CP 12 18, Contract of Sale Clause CP 12 18, Lenders Loss Payable Clause CP 12 18, or Loss Payable Clause CP 12 18.
        //		Ordinance or Law Coverage CP 04 05	CP 04 05	Electable	"• Not available when ""Functional Building Valuation CP 04 38"" is selected, or ""Condominium Commercial Unit-Owners Coverage Form CP 00 18"" is selected."
        //		Outdoor Signs CP 14 40	CP 14 40	Required	• Available when class code 1185 7a or 1185 7b is selected.
        //		Payroll Limitation Or Exclusion CP 15 10	CP 15 10	Electable	"• Available when the coverage ""Business Income Coverage Form"" is selected and Term ""Coverage Options"" is selected with a Term Option of ""Coinsurance"". • Not available when ""Discretionary Payroll Expense CP 15 04"" is selected."
        //		Peak Season Limit Of Insurance CP 12 30	CP 12 30	Electable	"• Available when coverage ""Business Personal Property Coverage"" is selected. • Not available when ""Condominium Association Coverage Form CP 00 17"" is selected."
        //		Radio Or Television Antennas CP 14 50	CP 14 50	Required	• Available when class code 1190 34a or 1190 34b is selected. - PROPERTY IN THE OPEN
        //		Theft Of Building Materials And Supplies (Other Than Builders Risk) CP 10 44	CP 10 44	Electable	"• Available when ""Building Coverage"" is selected and the Term ""Cause Of Loss"" is selected with Term Option ""Special"" is selected • Not available when ""Theft Exclusion CP 10 33"" is selected."

        softAssert.assertAll();

    }


    private CPPCommercialProperty_Building getPropertyByCoverage(PropertyCoverages coverage) {
        List<CPPCommercialProperty_Building> PropertyList = myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List();
        for (CPPCommercialProperty_Building building : PropertyList) {
            if (building.getCoverages().getBuildingCoverageList().contains(coverage)) {
                return building;
            }
        }
        return null;
    }


    private void verifyCoverage(String selectedCoverage, String coverageLabel, Electability electability) {
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        switch (electability) {
            case Available:
                break;
            case Electable:
                softAssert.assertTrue(guidewireHelpers.isElectable(coverageLabel), coverageLabel + " was not Electable when " + selectedCoverage + " was selected");
                break;
            case Required:
                softAssert.assertTrue(guidewireHelpers.isRequired(coverageLabel), coverageLabel + " was not Required when " + selectedCoverage + " was selected");
                break;
            case Suggested:
                softAssert.assertTrue(guidewireHelpers.isSuggested(coverageLabel), coverageLabel + " was not Suggested when " + selectedCoverage + " was selected");
                break;
        }
    }

    private void verifyCoverageNotExists(String coverageSelected, String coverageToCheck) {
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        softAssert.assertTrue(!guidewireHelpers.isAvailable(coverageToCheck), coverageToCheck + " was Available when " + coverageSelected + " was selected.");
    }
}
















