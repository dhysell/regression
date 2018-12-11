package regression.r3.noclock.policycenter.generalliability;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.AdditionalInsuredTypeGL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.ExistanceType;
import repository.gw.enums.GLClassCode;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPGLCoveragesAdditionalInsureds;
import repository.gw.generate.custom.CPPGeneralLiability;
import repository.gw.generate.custom.CPPGeneralLiabilityCoverages;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderGLCoveragesAdditionalInsured;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoveragesCPP;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification_GeneralLiability;

/**
 * @Author jlarsen
 * @Requirement http://projects.idfbins.com/policycenter/To%20Be%20Process/Commercial%20Package%20Policy%20(CPP)/Guidewire%20-%20General%20Liability/WCIC%20General%20Liability-Product-Model.xlsx
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description Test the Availability Rules for GL Coverages
 * @DATE Apr 28, 2016
 */
public class GLCoverages extends BaseTest {


    GeneratePolicy myPolicyObj = null;
    SoftAssert softAssert = new SoftAssert();
    List<String> classList = new ArrayList<String>();

    private WebDriver driver;


    @SuppressWarnings("serial")
    @Test//(enabled=false)
    public void generatePolicy() throws Exception {

        // LOCATIONS
        ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>() {{
            this.add(new PolicyLocation(new AddressInfo(true), false) {{
                this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {{
                    this.add(new PolicyLocationBuilding() {{
                    }}); // END BUILDING
                }}); // END BUILDING LIST
            }});// END POLICY LOCATION
        }}; // END LOCATION LIST

        ArrayList<CPPGeneralLiabilityExposures> exposures = new ArrayList<CPPGeneralLiabilityExposures>();
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), GLClassCode.GL_10026.getValue()));

        CPPGeneralLiabilityCoverages coverages = new CPPGeneralLiabilityCoverages() {{
            this.setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                this.add(new CPPGLCoveragesAdditionalInsureds() {{
                    this.setLocationList(new ArrayList<PolicyLocation>() {{
                        this.add(locationsLists.get(0));
                    }});
                }});
            }});
        }};

        CPPGeneralLiability generalLiability = new CPPGeneralLiability() {{
            this.setCPPGeneralLiabilityExposures(exposures);
            this.setCPPGeneralLiabilityCoverages(coverages);
        }};
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // GENERATE POLICY
        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPGeneralLiability(generalLiability)
                .withLineSelection(LineSelection.GeneralLiabilityLineCPP)
                .withPolicyLocations(locationsLists)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("GL Coverages")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsLists.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .isDraft()
                .build(GeneratePolicyType.FullApp);
    }


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement http://projects.idfbins.com/policycenter/To%20Be%20Process/Commercial%20Package%20Policy%20(CPP)/Guidewire%20-%20General%20Liability/WCIC%20General%20Liability-Product-Model.xlsx
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description verify all the Coverage Availability Rules dealing with setting a specific Exposure Class Code.
     * Some Coverage are available with many class codes. The test will randomly get one of those from the list to test.
     * Some Coverages don't require any specific class code. In this case a random class code is chosen.
     * @DATE Apr 28, 2016
     */
    @SuppressWarnings("serial")
    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void testGLCoverages() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);

        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        guidewireHelpers.editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderQualification_GeneralLiability qual = new GenericWorkorderQualification_GeneralLiability(driver);
        GenericWorkorderGeneralLiabilityCoveragesCPP coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);

        sideMenu.clickSideMenuGLCoverages();
        coverages.clickStandardCoverages();


        //General Liability CG 00 01
        softAssert.assertTrue(guidewireHelpers.isRequired("General Liability CG 00 01"), "Coverage General Liability CG 00 01 was NOT Required");


        //Additional Insured - Charitable Institutions CG 20 20
        sideMenu.clickSideMenuQualification();
        qual.clickQualificationNotForProfitOrgCharitableCauses(true);
        sideMenu.clickSideMenuGLCoverages();
        coverages.clickAdditionalCoverages();
        softAssert.assertTrue(guidewireHelpers.isRequired("Additional Insured - Charitable Institutions CG 20 20"), "Additional coverage Additional Insured - Charitable Institutions CG 20 20 was NOT Required");

        sideMenu.clickSideMenuQualification();
        qual.clickQualificationNotForProfitOrgCharitableCauses(false);


        //Additional Insured - Church Members And Officers CG 20 22
        classList = new ArrayList<String>() {{
            this.add(GLClassCode.GL_41650.getValue());
        }};
        testGLCoverageAdditionalCoverages("Additional Insured - Church Members And Officers CG 20 22", ExistanceType.Required);

        //Additional Insured - Executors, Administrators, Trustees Or Beneficiaries CG 20 23
        // Available when Organization Type is Trust. Located on the Policy Info.
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo info = new GenericWorkorderPolicyInfo(driver);
        info.setPolicyInfoOrganizationType(OrganizationType.Trust);
        myPolicyObj.polOrgType = OrganizationType.Trust;

        sideMenu.clickSideMenuGLCoverages();
        coverages.clickAdditionalCoverages();

        softAssert.assertTrue(guidewireHelpers.isRequired("Additional Insured - Executors, Administrators, Trustees Or Beneficiaries CG 20 23"), "Additional Coverage Additional Insured - Executors, Administrators, Trustees Or Beneficiaries CG 20 23 was NOT Required");

        //create and Additional Insured
        sideMenu.clickSideMenuGLCoverages();
        coverages.clickAdditioanlInsureds();


        //Designated Construction Project(s) General Aggregate Limit CG 25 03
        // Available when Designated Construction Project(s) General Aggregate Limit CG 25 03 located under the Additional Insured screen is selected Yes.
        CPPGLCoveragesAdditionalInsureds myAdditionalInsured = myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().get(0);
        myAdditionalInsured.setType(AdditionalInsuredTypeGL.AdditionalInsured_OwnersLesseesOrContractors_ScheduledPersonOrOrganization_CG_20_10);
        myAdditionalInsured.setDesignatedConstructionProjectGeneralAggregateLimit_CG_25_03(true);
        coverages.editAdditoinalInsured(myAdditionalInsured);
        GenericWorkorderGLCoveragesAdditionalInsured additionalInsureds = new GenericWorkorderGLCoveragesAdditionalInsured(driver);
        additionalInsureds.selectType(AdditionalInsuredTypeGL.AdditionalInsured_OwnersLesseesOrContractors_ScheduledPersonOrOrganization_CG_20_10);
        additionalInsureds.setTypeQuestions(myAdditionalInsured);
        additionalInsureds.clickUpdate();
        coverages.clickAdditionalCoverages();
        softAssert.assertTrue(guidewireHelpers.isRequired("Designated Construction Project(s) General Aggregate Limit CG 25 03"), "Designated Construction Project(s) General Aggregate Limit CG 25 03 was NOT Required when Additional Insured AdditionalInsured_OwnersLesseesOrContractors_ScheduledPersonOrOrganization_CG_20_10 was added to the policy");


        coverages.clickAdditioanlInsureds();

        //Designated Location(s) General Aggregate Limit CG 25 04
        // Available when Designated Location(s) General Aggregate Limit CG 25 04 located under the Additional Insured screen is selected Yes.
        myAdditionalInsured = myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().get(0);
        myAdditionalInsured.setDesignatedLocationsAggeragateLimitCG2504(true);
        myAdditionalInsured.setType(AdditionalInsuredTypeGL.AdditionalInsured_ManagersOrLessorsOfPremises_CG_20_11);
        coverages.editAdditoinalInsured(myAdditionalInsured);
        additionalInsureds = new GenericWorkorderGLCoveragesAdditionalInsured(driver);
        additionalInsureds.selectType(AdditionalInsuredTypeGL.AdditionalInsured_ManagersOrLessorsOfPremises_CG_20_11);
        additionalInsureds.setTypeQuestions(myAdditionalInsured);
        additionalInsureds.clickUpdate();
        coverages.clickAdditionalCoverages();
        softAssert.assertTrue(guidewireHelpers.isRequired("Designated Location(s) General Aggregate Limit CG 25 04"), "Designated Location(s) General Aggregate Limit CG 25 04 was NOT Required when AdditionalInsured_ManagersOrLessorsOfPremises_CG_20_11 was added to the policy");

        //Additional Insured - Club Members CG 20 02
        classList = new ArrayList<String>() {{
            this.add(GLClassCode.GL_41668.getValue());
            this.add(GLClassCode.GL_41667.getValue());
            this.add(GLClassCode.GL_41670.getValue());
            this.add(GLClassCode.GL_41669.getValue());
            this.add(GLClassCode.GL_11138.getValue());
        }};
        testGLCoverageAdditionalCoverages("Additional Insured - Club Members CG 20 02", ExistanceType.Required);


        //Additional Insured - Condominium Unit Owners CG 20 04
        classList = new ArrayList<String>() {{
            this.add(GLClassCode.GL_62000.getValue());
            this.add(GLClassCode.GL_62002.getValue());
            this.add(GLClassCode.GL_62003.getValue());
        }};
        testGLCoverageAdditionalCoverages("Additional Insured - Condominium Unit Owners CG 20 04", ExistanceType.Required);


        //Additional Insured - Townhouse Associations CG 20 17
        classList = new ArrayList<String>() {{
            this.add(GLClassCode.GL_68500.getValue());
        }};
        testGLCoverageAdditionalCoverages("Additional Insured - Townhouse Associations CG 20 17", ExistanceType.Required);


        //Additional Insured - Users Of Golfmobiles CG 20 08
        classList = new ArrayList<String>() {{
            this.add(GLClassCode.GL_11138.getValue());
            this.add(GLClassCode.GL_44070.getValue());
            this.add(GLClassCode.GL_44072.getValue());
            this.add(GLClassCode.GL_45190.getValue());
            this.add(GLClassCode.GL_45191.getValue());
            this.add(GLClassCode.GL_45192.getValue());
            this.add(GLClassCode.GL_45193.getValue());
        }};
        testGLCoverageAdditionalCoverages("Additional Insured - Users Of Golfmobiles CG 20 08", ExistanceType.Required);


        //Additional Insured - Users Of Teams, Draft Or Saddle Animals CG 20 14
        classList = new ArrayList<String>() {{
            this.add("0");
        }};
        testGLCoverageAdditionalCoverages("Additional Insured - Users Of Teams, Draft Or Saddle Animals CG 20 14", ExistanceType.Electable);


        //Amendment Of Liquor Liability Exclusion - Exception For Scheduled Activities CG 21 51
        classList = new ArrayList<String>() {{
            this.add("0");
        }};
        testGLCoverageAdditionalCoverages("Amendment Of Liquor Liability Exclusion - Exception For Scheduled Activities CG 21 51", ExistanceType.Electable);


        //Employment Practices Liability Insurance IDCG 31 2013
        classList = new ArrayList<String>() {{
            this.add("0");
        }};
        testGLCoverageAdditionalCoverages("Employment Practices Liability Insurance IDCG 31 2013", ExistanceType.Suggested);


        //Lawn Care Services Coverage IDCG 31 2006
        classList = new ArrayList<String>() {{
            this.add(GLClassCode.GL_97047.getValue());
            this.add(GLClassCode.GL_97050.getValue());
        }};
        testGLCoverageAdditionalCoverages("Lawn Care Services Coverage IDCG 31 2006", ExistanceType.Required);


        //Liquor Liability - Bring Your Own Alcohol Establishments CG 24 06
        classList = new ArrayList<String>() {{
            this.add(GLClassCode.GL_58165.getValue());
            this.add(GLClassCode.GL_58166.getValue());
        }};
        testGLCoverageAdditionalCoverages("Liquor Liability - Bring Your Own Alcohol Establishments CG 24 06", ExistanceType.Required);

        //DO NOT UNCOMMENT THIS SECTION
        //NEED TO FIGURE OUT HOW TO FIX IT.
        //thses class codes requore adding additoinal class codes but doens't specify what ones to add.
//		//Liquor Liability Coverage Form CG 00 33
//		classList = new ArrayList<String>(){{
//			this.add(GLClassCode.GL_70412.getValue());
//			this.add(GLClassCode.GL_58161.getValue());
//			this.add(GLClassCode.GL_50911.getValue());
//			this.add(GLClassCode.GL_59211.getValue());
//			this.add(GLClassCode.GL_58165.getValue());
//			this.add(GLClassCode.GL_58166.getValue());
//		}};
//		testGLCoverageAdditionalCoverages("Liquor Liability Coverage Form CG 00 33", ExistanceType.Required);


        //Primary And Noncontributory - Other Insurance Condition CG 20 01
        classList = new ArrayList<String>() {{
            this.add("0");
        }};
        testGLCoverageAdditionalCoverages("Primary And Noncontributory - Other Insurance Condition CG 20 01", ExistanceType.Electable);

        softAssert.assertAll();
    }


    /**
     * @Author jlarsen
     * @Requirement http://projects.idfbins.com/policycenter/To%20Be%20Process/Commercial%20Package%20Policy%20(CPP)/Guidewire%20-%20General%20Liability/WCIC%20General%20Liability-Product-Model.xlsx
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description test all the availability rules dealing with the type of Additional Insured set on the policy
     * @DATE Apr 28, 2016
     */
    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void testGLCoveragesAdditionalInsured() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        //Additional Insured - Concessionaires Trading Under Your Name CG 20 03
        //Available when Additional Insured - Concessionaires Trading Under Your Name CG 20 03 is selected on the Screen New Additional Insured.
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_ConcessionairesTradingUnderYourName_CG_20_03);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_ConcessionairesTradingUnderYourName_CG_20_03.getValue(), ExistanceType.Required);


        //Additional Insured - Controlling Interest CG 20 05
        //Available when Additional Insured - Controlling Interest CG 20 05 is selected on the Screen New Additional Insured.
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_ControllingInterest_CG_20_05);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_ControllingInterest_CG_20_05.getValue(), ExistanceType.Required);


        //Additional Insured - Designated Person Or Organization CG 20 26
        //Available when Additional Insured - Designated Person Or Organization CG 20 26 is selected on the Screen New Additional Insured.
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_DesignatedPersonOrOrganization_CG_20_26);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_DesignatedPersonOrOrganization_CG_20_26.getValue(), ExistanceType.Required);


        //Additional Insured - Engineers, Architects Or Surveyors Not Engaged By The Named Insured CG 20 32
        //Available when Additional Insured - Engineers, Architects Or Surveyors Not Engaged By The Named Insured CG 20 32 is selected on the Screen New Additional Insured.
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_EngineersArchitectsOrSurveyorsNotEngagedByTheNamedInsured_CG_20_32);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_EngineersArchitectsOrSurveyorsNotEngagedByTheNamedInsured_CG_20_32.getValue(), ExistanceType.Required);


        //Additional Insured - Co-Owner Of Insured Premises CG 20 27
        //Available when Additional Insured - Co-Owner Of Insured Premises CG 20 27 is selected on the Screen New Additional Insured.
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_CoownerOfInsuredPremises_CG_20_27);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_CoownerOfInsuredPremises_CG_20_27.getValue(), ExistanceType.Required);

        //Additional Insured - Grantor Of Franchise CG 20 29
        //Available when Additional Insured - Grantor Of Franchise CG 20 29 is selected on the Screen New Additional Insured.
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_GrantorOfFranchise_CG_20_29);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_GrantorOfFranchise_CG_20_29.getValue(), ExistanceType.Required);


        //Additional Insured - Grantor Of Licenses CG 20 36
        //Available when Additional Insured - Grantor Of Licenses CG 20 36 is selected on the Screen New Additional Insured.
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_GrantorOfLicenses_CG_20_36);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_GrantorOfLicenses_CG_20_36.getValue(), ExistanceType.Required);


        //Additional Insured - Lessor Of Leased Equipment CG 20 28
        //Available when Additional Insured - Lessor Of Leased Equipment CG 20 28 is selected on the Screen New Additional Insured.
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_LessorOfLeasedEquipment_CG_20_28);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_LessorOfLeasedEquipment_CG_20_28.getValue(), ExistanceType.Required);


        //Additional Insured - Managers Or Lessors Of Premises CG 20 11
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_ManagersOrLessorsOfPremises_CG_20_11);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_ManagersOrLessorsOfPremises_CG_20_11.getValue(), ExistanceType.Required);


        //Additional Insured - Mortgagee, Assignee Or Receiver CG 20 18
        // Available when Additional Insured - Mortgagee, Assignee Or Receiver CG 20 18 is selected on the Screen New Additional Insured.
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_MortgageeAssigneeOrReceiver_CG_20_18);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_MortgageeAssigneeOrReceiver_CG_20_18.getValue(), ExistanceType.Required);


        //Additional Insured - Owners Or Other Interests From Whom Land Has Been Leased CG 20 24
        // Available when Additional Insured - Owners Or Other Interests From Whom Land Has Been Leased CG 20 24 is selected on the Screen New Additional Insured.
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_OwnersOrOtherInterestsFromWhomLandHasBeenLeased_CG_20_24);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_OwnersOrOtherInterestsFromWhomLandHasBeenLeased_CG_20_24.getValue(), ExistanceType.Required);


        //Additional Insured - Owners, Lessees Or Contractors - Completed Operations CG 20 37
        // Available when Additional Insured - Owners, Lessees Or Contractors - Completed Operations CG 20 37 is selected on the Screen New Additional Insured.
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_OwnersLesseesOrContractors_CompletedOperations_CG_20_37);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_OwnersLesseesOrContractors_CompletedOperations_CG_20_37.getValue(), ExistanceType.Required);


        //Additional Insured - Owners, Lessees Or Contractors - Scheduled Person Or Organization CG 20 10
        // Available when Additional Insured - Owners, Lessees Or Contractors - Scheduled Person Or Organization CG 20 10 is selected on the Screen New Additional Insured.
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_OwnersLesseesOrContractors_ScheduledPersonOrOrganization_CG_20_10);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_OwnersLesseesOrContractors_ScheduledPersonOrOrganization_CG_20_10.getValue(), ExistanceType.Required);


        //Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations CG 20 12
        // Available when Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations CG 20 12 is selected on the Screen New Additional Insured.
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizations_CG_20_12);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizations_CG_20_12.getValue(), ExistanceType.Required);


        //Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations Relating To Premises CG 20 13
        // Available when Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations Relating To Premises CG 20 13 is selected on the Screen New Additional Insured.
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizationsRelatingToPremises_CG_20_13);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizationsRelatingToPremises_CG_20_13.getValue(), ExistanceType.Required);

        //Additional Insured - Vendors CG 20 15
        // Available when Additional Insured - Vendors CG 20 15 is selected on the Screen New Additional Insured.
        setAdditionalInsuredsType(AdditionalInsuredTypeGL.AdditionalInsured_Vendors_CG_20_15);
        checkCoverage(AdditionalInsuredTypeGL.AdditionalInsured_Vendors_CG_20_15.getValue(), ExistanceType.Required);


        softAssert.assertAll();

    }


    /**
     * @Author jlarsen
     * @Requirement http://projects.idfbins.com/policycenter/To%20Be%20Process/Commercial%20Package%20Policy%20(CPP)/Guidewire%20-%20General%20Liability/WCIC%20General%20Liability-Product-Model.xlsx
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description Test the Coverages that are only available to Underwriters
     * @DATE Apr 28, 2016
     */
    @Test(dependsOnMethods = {"generatePolicy"}, enabled = false)
    public void testGLUnderwriterCoverages() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();


        //UNDERWRITER ONLY COVERAGES
        //Contractual Liability - Railroads CG 24 17
        // This is only available for underwriters to select. The agent can see the endorsement only when the underwriter selects it and then they can also see what the underwriter has written but they are not able to edit it. The agent can unselect the endorsement but cannot reselect it, the endorsement will no longer be visible.


//		Idaho Professional Applicator Certificate Of Insurance IDCG 04 0001
//		 Available for Underwriters to select all the time. The agent can see the endorsement when the underwriter selects it and what the underwriter has entered in however they cannot edit it or select it. However if class code 97047 or 97050 is selected and the question "Is applicant/insured a licensed applicator?" is answered yes then the agent can select the endorsement. Also the agent can remove the endorsement whenever they want.


        //Supplemental Extended Reporting Period Endorsement IDCW 31 0002
        //" Available when Employment Practices Liability Insurance IDCG 31 2013 is selected and only in job type Policy Change. 
        // Available for Underwriters and Agents to see but the Underwriter will only be allowed to select the endorsement at a backdated policy change job if the policy is canceled."


        softAssert.assertAll();

    }

    //changes the type of the Additional Insured and sets the required questions for that Type
    private void setAdditionalInsuredsType(AdditionalInsuredTypeGL type) {
        SideMenuPC sidemenu = new SideMenuPC(driver);
        GenericWorkorderGeneralLiabilityCoveragesCPP coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);

        sidemenu.clickSideMenuGLCoverages();
        coverages.clickAdditioanlInsureds();

        coverages.editAdditoinalInsured(myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().get(0));
        GenericWorkorderGLCoveragesAdditionalInsured additionalInsured = new GenericWorkorderGLCoveragesAdditionalInsured(driver);
        additionalInsured.selectType(type);

        myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().get(0).setType(type);

        additionalInsured.setTypeQuestions(myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().get(0));

        additionalInsured.clickUpdate();
        if (!additionalInsured.finds(By.xpath("//span[contains(text(), 'Location Information')]")).isEmpty()) {
            additionalInsured.clickOverride();
            additionalInsured.clickUpdate();
        }
    }


    //Adds and removes class codes as required for testing Coverages
    @SuppressWarnings("serial")
    private void testGLCoverageAdditionalCoverages(String coverage, ExistanceType type) {
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);
        try {
            System.out.println("Testing Coverage:  " + coverage);
            SideMenuPC sidemenu = new SideMenuPC(driver);
            GenericWorkorderGeneralLiabilityCoveragesCPP coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
            GenericWorkorderGeneralLiabilityExposuresCPP exposures = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);

            sidemenu.clickSideMenuGLExposures();
            exposures.clickExposureDetialsTab();
            //remove exposure
            exposures.selectAll();
            exposures.clickRemove();
            myPolicyObj.generalLiabilityCPP.setCPPGeneralLiabilityExposures(new ArrayList<CPPGeneralLiabilityExposures>());
            exposures.clickAdd();
            CPPGeneralLiabilityExposures myExposure = new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0));
            CPPGeneralLiabilityExposures myExposure2 = null;
            int randomNumber = NumberUtils.generateRandomNumberInt(0, classList.size() - 1);

            System.out.println("With Class Code:  " + classList.get(randomNumber));

            if (classList.get(0).equals("0")) { //no specific class code so set random one.
                myExposure = new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0));
                System.out.println("With Class Code:  " + myExposure.getClassCode());
                switch (myExposure.getClassCode()) {
                    case "169151":
                        myExposure2 = new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0), "58161");
                        break;
                }
            } else {
                myExposure = new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0), classList.get(randomNumber));
            }
            myPolicyObj.generalLiabilityCPP.setCPPGeneralLiabilityExposures(new ArrayList<CPPGeneralLiabilityExposures>() {{
                this.add(new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0), classList.get(randomNumber)));
            }});
            if (myExposure2 != null) {
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0), classList.get(randomNumber)));
                exposures.addExposure(myExposure2);
            }
            exposures.addExposure(myExposure);
            exposures.clickLocationSpecificQuestionsTab();
            exposures.fillOutBasicUWQuestionsQQ(myExposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
            exposures.fillOutBasicUWQuestionsFullApp(myExposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
            if (myExposure2 != null) {
                exposures.fillOutBasicUWQuestionsQQ(myExposure2, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
                exposures.fillOutBasicUWQuestionsFullApp(myExposure2, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
            }

            checkCoverage(coverage, type);
            coverages.fillOutGeneralLiabilityCoverages(myPolicyObj);
        } catch (Exception e) {
            System.out.println("FAILED ON ADDITIONAL COVERAGE: " + coverage + " PROBABLY AN COVERAGE TERM NOT FILLED OUT OR QUESTIONS DIDN'T GET FILLED OUT");
            softAssert.fail("FAILED ON ADDITIONAL COVERAGE: " + coverage + " PROBABLY AN COVERAGE TERM NOT FILLED OUT OR QUESTIONS DIDN'T GET FILLED OUT");
            guidewireHelpers.logout();
            login.loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        }
    }


    //Verify that the coverage has the correct availabilty(Required, Suggested, Electable)
    private void checkCoverage(String coverage, ExistanceType type) {
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        SideMenuPC sidemenu = new SideMenuPC(driver);
        GenericWorkorderGeneralLiabilityCoveragesCPP coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        sidemenu.clickSideMenuGLCoverages();
        if (!sidemenu.finds(By.xpath("//span[contains(@id, 'ClearButton-btnEl')]")).isEmpty()) {
            sidemenu.find(By.xpath("//span[contains(@id, 'ClearButton-btnEl')]")).click();
        }
        coverages.clickAdditionalCoverages();

        switch (type) {
            case Required:
                softAssert.assertTrue(guidewireHelpers.isRequired(coverage), "Additional Coverage " + coverage + " was NOT Required");
                break;
            case Electable:
                softAssert.assertTrue(guidewireHelpers.isElectable(coverage), "Additional Coverage " + coverage + " was NOT Electable");
                break;
            case Suggested:
                softAssert.assertTrue(guidewireHelpers.isSuggested(coverage), "Additional Coverage " + coverage + " was NOT Suggested");
                break;
        }
    }


}
