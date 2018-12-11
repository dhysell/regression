package regression.r3.noclock.policycenter.commercialproperty;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.Building.FireBurglaryAlarmType;
import repository.gw.enums.CommercialProperty.BuildingCoverageDeductible;
import repository.gw.enums.CommercialProperty.PropertyCoverages;
import repository.gw.enums.CommercialProperty.RateType;
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
import repository.pc.search.SearchSubmissionsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyCommercialPropertyLineCPP;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Details;
import persistence.enums.Underwriter.UnderwriterLine;

public class CPConditions extends BaseTest {


    public GeneratePolicy myPolicyObj = null;
    SoftAssert softAssert = new SoftAssert();

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

        //LIST OF COMMERCIAL PROPERTY
        List<CPPCommercialPropertyProperty> commercialPropertyList = new ArrayList<CPPCommercialPropertyProperty>() {{
            this.add(new CPPCommercialPropertyProperty() {{
                this.setAddress(pniAddress);
                this.setCPPCommercialProperty_Building_List(new ArrayList<CPPCommercialProperty_Building>() {{
                    this.add(new CPPCommercialProperty_Building() {{
                        this.setCoverages(new CPPCommercialProperty_Building_Coverages(PropertyCoverages.BuildingCoverage) {{
                            this.setBuildingCoverage_Deductible(BuildingCoverageDeductible.FiveHundred);
                        }});
                    }});// end building 0
                    this.add(new CPPCommercialProperty_Building() {{
                        this.setCoverages(new CPPCommercialProperty_Building_Coverages(PropertyCoverages.BuildingCoverage) {{
                            this.setBuildingCoverage_Deductible(BuildingCoverageDeductible.OneThousand);
                        }});
                    }});// end building 1
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
                .withInsCompanyName("Property Conditions")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(pniAddress)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .isDraft()
                .build(GeneratePolicyType.FullApp);

    }


    /**
     * @Author jlarsen
     * @Requirement Commercial Property Product Model
     * @Description Binding Arbitration CR 20 12
     * Change In Control Of The Insured � Notice To The Company CR 20 29
     * Commercial Crime Manuscript Endorsement IDCR 31 1001
     * Commercial Property Conditions CP 00 90
     * Commercial Property Manuscript Endorsement IDCP 31 1005
     * Convert To An Aggregate Limit Of Insurance CR 20 08
     * Idaho Changes CR 02 12
     * Multiple Deductible Form IDCP 31 1001
     * @DATE Aug 21, 2017
     */
    @Test(dependsOnMethods = {"generateQQPolicy"})
    public void testConditions_CPPropertyLine() throws Exception {


        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        softAssert = new SoftAssert();
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        //Binding Arbitration - Required
        //available when clients property CR 04 01, Employee theft, Forgery of Alteration, Guest peoeprty 04 11, include volunteer workers other than fund , 
        //inside the premises - theft of money and securities, inside the premises - theft of other property, joint loss payable, outside the premises

        //Cahnge in control of the insured- notice to the company CR 20 29 - required
        //available when clients property CR 04 01, Employee theft, Forgery of Alteration, Guest peoeprty 04 11, include volunteer workers other than fund , 
        //inside the premises - theft of money and securities, inside the premises - theft of other property, joint loss payable, outside the premises
        SideMenuPC sidemenu = new SideMenuPC(driver);
        GenericWorkorderCommercialPropertyCommercialPropertyLineCPP properyLine = new GenericWorkorderCommercialPropertyCommercialPropertyLineCPP(driver);
        sidemenu.clickSideMenuCPCommercialPropertyLine();

        myPolicyObj.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setEmployeeTheft(true);
        properyLine.fillOutCoveragesTab(myPolicyObj);
        properyLine.clickExclusionsConditionsTab();
        softAssert.assertTrue(guidewireHelpers.isRequired("Binding Arbitration CR 20 12"), "Binding Arbitration CR 20 12 was not REQUIRED when Employee Theft was Selected.");
        softAssert.assertTrue(guidewireHelpers.isRequired("Change In Control Of The Insured - Notice To The Company CR 20 29"), "Change In Control Of The Insured - Notice To The Company CR 20 29 was not REQUIRED when Employee Theft was Selected.");

        //Convert to an aggregate limit of insurance CR 20 08 - Required
        //available when employee theft, forgery or alteration, guest property, inside the premises - both, outside the premises
        softAssert.assertTrue(guidewireHelpers.isRequired("Convert To An Aggregate Limit Of Insurance CR 20 08"), "Convert To An Aggregate Limit Of Insurance CR 20 08 was not REQUIRED when Employee Theft was Selected.");

        //Idaho changes - required
        //available when clients property CR 04 01, Employee theft, Forgery of Alteration, Guest peoeprty 04 11, include volunteer workers other than fund , 
        //inside the premises - theft of money and securities, inside the premises - theft of other property, joint loss payable, outside the premises
        softAssert.assertTrue(guidewireHelpers.isRequired("Idaho Changes CR 02 12"), "Idaho Changes CR 02 12 was not REQUIRED when Employee Theft was Selected.");

        //commercial Property Conditions CP 00 90 - Required
        //always
        softAssert.assertTrue(guidewireHelpers.isRequired("Commercial Property Conditions CP 00 90"), "Commercial Property Conditions CP 00 90 was NOT Required");

        //Commercial crime Manuscript end 1001- Electable
        //UW ONLY
        //Commercial Property manuscript end 1005 - Electable
        //UW ONLY
        softAssert.assertFalse(guidewireHelpers.isElectable("Commercial Crime Manuscript Endorsement IDCR 31 1001"), "Commercial Crime Manuscript Endorsement IDCR 31 1001 was AVAILABLE to the Agent to select");
        softAssert.assertFalse(guidewireHelpers.isElectable("Commercial Property Manuscript Endorsement IDCP 31 1005"), "Commercial Property Manuscript Endorsement IDCP 31 1005 was AVAILABLE to the Agent to Select");
        new Login(driver).switchUserToUW(UnderwriterLine.Commercial);
        new SearchSubmissionsPC(driver).searchSubmission(myPolicyObj);
        sidemenu.clickSideMenuCPCommercialPropertyLine();
        properyLine.clickExclusionsConditionsTab();
        softAssert.assertTrue(guidewireHelpers.isElectable("Commercial Crime Manuscript Endorsement IDCR 31 1001"), "Commercial Crime Manuscript Endorsement IDCR 31 1001 was NOT Available for the Underwriter");
        softAssert.assertTrue(guidewireHelpers.isElectable("Commercial Property Manuscript Endorsement IDCP 31 1005"), "Commercial Property Manuscript Endorsement IDCP 31 1005 was NOT Available for the Underwriter");


        //Multiple Deductible form - required
        //available when theer is atleast 2 deductibles selected on the same policy under property. 
        //NOT available when all deductibles on the policy have th esame value. 

        softAssert.assertAll();

    }


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement Commercial Property Product Model
     * @Description Burglary And Robbery Protective Safeguards CP 12 11
     * Limitations On Coverage For Roof Surfacing CP 10 36
     * Protective Safeguards CP 04 11
     * Tentative Rate CP 99 93
     * @DATE Aug 21, 2017
     */
    @Test(dependsOnMethods = {"generateQQPolicy"})
    public void testConditions_CPBuilding() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        softAssert = new SoftAssert();
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sidemenu = new SideMenuPC(driver);
        GenericWorkorderCommercialPropertyPropertyCPP propertyPage = new GenericWorkorderCommercialPropertyPropertyCPP(driver);
        GenericWorkorderCommercialPropertyPropertyCPP_Details propertyDetail = new GenericWorkorderCommercialPropertyPropertyCPP_Details(driver);
        sidemenu.clickSideMenuCPProperty();
        propertyPage.editPropertyByNumber(1);
        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0).setBurglaryAndRobberyProtectiveSafeguards_CP_12_11(FireBurglaryAlarmType.CentralStationWithKeys);
        propertyDetail.fillOutPropertyDetailsFA(myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0));
        propertyPage.clickExclusionsConditionsTab();
        softAssert.assertTrue(guidewireHelpers.isRequired("Burglary And Robbery Protective Safeguards CP 12 11"), "Burglary And Robbery Protective Safeguards CP 12 11 was NOT Required when Burglary And Robbery Protective Safeguards CP 12 11 is set.");
        softAssert.assertTrue(guidewireHelpers.isElectable("Limitations On Coverage For Roof Surfacing CP 10 36"), "Limitations On Coverage For Roof Surfacing CP 10 36 was NOT Electible");
        softAssert.assertTrue(guidewireHelpers.isRequired("Protective Safeguards CP 04 11"), "Protective Safeguards CP 04 11 was NOT Required when Sprinklers was set to Yes");
        propertyPage.clickOK();

        new Login(driver).switchUserToUW(UnderwriterLine.Commercial);
        new SearchSubmissionsPC(driver).searchSubmission(myPolicyObj);
        sidemenu.clickSideMenuCPProperty();
        propertyPage.editPropertyByNumber(1);
        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0).setRateType(RateType.Tentative);
        propertyDetail.fillOutPropertyDetailsFA(myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0));
        propertyDetail.selectRateType(RateType.Tentative);
        propertyPage.clickExclusionsConditionsTab();
        softAssert.assertTrue(guidewireHelpers.isRequired("Tentative Rate CP 99 93"), "Tentative Rate CP 99 93 was NOT Required when Rate Type was set to Tentative.");

        softAssert.assertAll();
    }


}























