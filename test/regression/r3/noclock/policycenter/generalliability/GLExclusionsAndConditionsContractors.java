package regression.r3.noclock.policycenter.generalliability;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GLClassCode;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PackageRiskType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPGeneralLiability;
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
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoveragesCPP;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.GLClassCodes;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.GLClassCodeHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author jlarsen
 * @Requirement See Product Model
 * @Description Test Class for CPP General Liabilty Exclusion and Conditions specific to contractors as they have extra question sets to fill out
 * @DATE Aug 18, 2016
 */
public class GLExclusionsAndConditionsContractors extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    SoftAssert softAssert = new SoftAssert();

    private WebDriver driver;


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement
     * @Description Generate Contractor type General Liability policy
     * @DATE Aug 18, 2016
     */
    @SuppressWarnings("serial")
    @Test//(enabled=false)
    public void generatePolicy() throws Exception {

        // LOCATIONS
        final ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>() {{
            this.add(new PolicyLocation(new AddressInfo(true), false) {{
                this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {{
                    this.add(new PolicyLocationBuilding() {{
                    }}); // END BUILDING
                }}); // END BUILDING LIST
            }});// END POLICY LOCATION
        }}; // END LOCATION LIST

        ArrayList<CPPGeneralLiabilityExposures> exposures = new ArrayList<CPPGeneralLiabilityExposures>();
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), GLClassCode.GL_10026.getValue()));

        CPPGeneralLiability generalLiability = new CPPGeneralLiability() {{
            this.setCPPGeneralLiabilityExposures(exposures);
        }};
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // GENERATE POLICY
        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPGeneralLiability(generalLiability)
                .withLineSelection(LineSelection.GeneralLiabilityLineCPP)
                .withPackageRiskType(PackageRiskType.Contractor)
                .withPolicyLocations(locationsLists)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("E&C Contractors")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsLists.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .isDraft()
                .build(GeneratePolicyType.QuickQuote);

    }


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement See Product Model
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description This test get 10 random contractor class codes and ensure that when added individually the correct
     * Exclusion and/or Condition appears with the correct availability: Required, Electable(checkbox Unchecked), Suggested(Checkbox Checked)
     * @DATE Aug 18, 2016
     */
    @SuppressWarnings("serial")
    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void testContractorsExclusionsAndConditions() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        List<GLClassCodes> contractorCodes = GLClassCodeHelper.getGLContractorClassCodes();
        List<GLClassCodes> testList = new ArrayList<GLClassCodes>() {{
            //			this.add(GLClassCodeHelper.getGLClassCodeByCode(GL_91340));
        }};

        for (int i = 0; i <= 10; i++) {
            testList.add(contractorCodes.get(NumberUtils.generateRandomNumberInt(0, contractorCodes.size() - 1)));
        }

        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        for (GLClassCodes classCode : testList) {
            //try block is to catch any failures on the class code. log the error. logout() the users and log back in esentaily reseting the policy
            //to allow for testing of the other class codes.
            try {
                SideMenuPC sideMenu = new SideMenuPC(driver);
                sideMenu.clickSideMenuGLExposures();
                GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
                exposuresPage.clickExposureDetialsTab();
                //remove exposure
                exposuresPage.selectAll();
                exposuresPage.clickRemove();
                exposuresPage.clickAdd();
                CPPGeneralLiabilityExposures myExposure = new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0), classCode.getCode());
                exposuresPage.addExposure(myExposure);
                myPolicyObj.generalLiabilityCPP.setCPPGeneralLiabilityExposures(new ArrayList<CPPGeneralLiabilityExposures>() {{
                    this.add(myExposure);
                }});

                exposuresPage.clickLocationSpecificQuestionsTab();
                exposuresPage.fillOutBasicUWQuestionsQQ(myExposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());

                exposuresPage.clickUnderwritingQuestionsTab();
                exposuresPage.fillOutUnderwritingContractorQuestions(myPolicyObj);

                sideMenu.clickSideMenuGLCoverages();
                GenericWorkorderGeneralLiabilityCoveragesCPP coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);


                if (!driver.findElements(By.xpath("//span[contains(@id, 'ClearButton-btnInnerEl')]")).isEmpty()) {
                    GenericWorkorder genwo = new GenericWorkorder(driver);
                    genwo.clickClear();
                }//END IF

                coverages.fillOutGeneralLiabilityCoverages(myPolicyObj);
                coverages.clickExclusionsAndConditions();


                //unless 99471
                //look for "Endorsement Excluding Bodily Injury Claim By Your Employee Against An Additional Insured IDCG 31 2008"
                switch (GLClassCode.valueOfName(classCode.getCode())) {
                    case GL_99471:
                        softAssert.assertFalse(guidewireHelpers.isAvailable("Endorsement Excluding Bodily Injury Claim By Your Employee Against An Additional Insured IDCG 31 2008"),
                                classCode.getCode() + " | " + classCode.getClassification() + " Endorsement Excluding Bodily Injury Claim By Your Employee Against An Additional Insured IDCG 31 2008 was available");
                        break;
                    default:
                        softAssert.assertTrue(guidewireHelpers.isRequired("Endorsement Excluding Bodily Injury Claim By Your Employee Against An Additional Insured IDCG 31 2008"),
                                classCode.getCode() + " | " + classCode.getClassification() + " Endorsement Excluding Bodily Injury Claim By Your Employee Against An Additional Insured IDCG 31 2008 was not required");
                        break;
                }//END SWITCH

                //unless 99471
                // look for "Exclusion - Contractors - Professional Liability CG 22 79"
                switch (GLClassCode.valueOfName(classCode.getCode())) {
                    case GL_99471:
                        softAssert.assertFalse(guidewireHelpers.isAvailable("Exclusion - Contractors - Professional Liability CG 22 79"),
                                classCode.getCode() + " | " + classCode.getClassification() + " Exclusion - Contractors - Professional Liability CG 22 79 was available");
                        break;
                    default:
                        softAssert.assertTrue(guidewireHelpers.isRequired("Exclusion - Contractors - Professional Liability CG 22 79"),
                                classCode.getCode() + " | " + classCode.getClassification() + " Exclusion - Contractors - Professional Liability CG 22 79 was not required");
                        break;
                }//END SWITCH

                //unless 94225 96816 97047 97050 99471 99975
                //look for "Exclusion - Designated Operations Covered By A Consolidated (Wrap-Up) Insurance Program IDCG 31 2002"
                switch (GLClassCode.valueOfName(classCode.getCode())) {
                    case GL_94225:
                    case GL_96816:
                    case GL_97047:
                    case GL_97050:
                    case GL_99471:
                    case GL_99975:
                        softAssert.assertFalse(guidewireHelpers.isAvailable("Exclusion - Designated Operations Covered By A Consolidated (Wrap-Up) Insurance Program IDCG 31 2002"),
                                classCode.getCode() + " | " + classCode.getClassification() + " Exclusion - Designated Operations Covered By A Consolidated (Wrap-Up) Insurance Program IDCG 31 2002 was available");
                        break;
                    default:
                        softAssert.assertTrue(guidewireHelpers.isRequired("Exclusion - Designated Operations Covered By A Consolidated (Wrap-Up) Insurance Program IDCG 31 2002"),
                                classCode.getCode() + " | " + classCode.getClassification() + " Exclusion - Designated Operations Covered By A Consolidated (Wrap-Up) Insurance Program IDCG 31 2002 was not required");
                        break;
                }//END SWITCH

                //unless 91111 91150 91405 91481 91523 94225 96816 97047 97050 98993 99310 99471 99975
                //look for "Exclusion - Explosion, Collapse And Underground Property Damage Hazard (Specified Operations) IDCG 31 2001"
                switch (GLClassCode.valueOfName(classCode.getCode())) {
                    case GL_91111:
                    case GL_91150:
                    case GL_91405:
                    case GL_91481:
                    case GL_91523:
                    case GL_94225:
                    case GL_96816:
                    case GL_97047:
                    case GL_97050:
                    case GL_98993:
                    case GL_99310:
                    case GL_99471:
                    case GL_99975:
                        softAssert.assertFalse(guidewireHelpers.isAvailable("Exclusion - Explosion, Collapse And Underground Property Damage Hazard (Specified Operations) IDCG 31 2001"),
                                classCode.getCode() + " | " + classCode.getClassification() + " Exclusion - Explosion, Collapse And Underground Property Damage Hazard (Specified Operations) IDCG 31 2001 was available");
                        break;
                    default:
                        softAssert.assertTrue(guidewireHelpers.isRequired("Exclusion - Explosion, Collapse And Underground Property Damage Hazard (Specified Operations) IDCG 31 2001"),
                                classCode.getCode() + " | " + classCode.getClassification() + " Exclusion - Explosion, Collapse And Underground Property Damage Hazard (Specified Operations) IDCG 31 2001 was not required");
                        // Test term options availability
                        switch (GLClassCode.valueOfName(classCode.getCode())) {
                            case GL_95410:
                            case GL_94007:
                            case GL_99315:
                            case GL_98482:
                            case GL_98820:
                            case GL_99507:
                            case GL_99946:
                            case GL_98806:
                            case GL_95357:
                                Guidewire8Select excludeHazardsSelect = null;
                                try {
                                    excludeHazardsSelect = new Guidewire8Select(driver, "//label[contains(text(),'Excluded Hazard(s)')]/parent::td//following-sibling::td/table");
                                } catch (Exception e) {
                                    softAssert.fail(classCode.getCode() + " | " + classCode.getClassification() + " Exclusion - Explosion, Collapse And Underground Property Damage Hazard (Specified Operations) IDCG 31 2001 did not have term 'Excluded Hazards(s)'");
                                }
                                List<String> options = excludeHazardsSelect.getList();

                                for (String option : options) {
                                    softAssert.assertFalse((option.contains("Explosion") && !option.contains("and")),
                                            classCode.getCode() + " | " + classCode.getClassification() + " Exclusion - Explosion, Collapse And Underground Property Damage Hazard (Specified Operations) IDCG 31 2001 'Excluded Hazard(s)' is NOT supposed to have 'Explosion' under this class code.");
                                    softAssert.assertFalse((option.contains("Explosion and Collapse")),
                                            classCode.getCode() + " | " + classCode.getClassification() + " Exclusion - Explosion, Collapse And Underground Property Damage Hazard (Specified Operations) IDCG 31 2001 'Excluded Hazard(s)' is NOT supposed to have 'Explosion and Collapse' under this class code.");
                                }
                                break;
                            default:
                                break;
                        }//END SWITCH
                        break;
                }//END SWITCH

                //unless 96816 97047 97050
                // look for "Exclusion - Exterior Insulation and Finish Systems CG 21 86"
                switch (GLClassCode.valueOfName(classCode.getCode())) {
                    case GL_96816:
                    case GL_97047:
                    case GL_97050:
                        softAssert.assertFalse(guidewireHelpers.isAvailable("Exclusion - Exterior Insulation and Finish Systems CG 21 86"),
                                classCode.getCode() + " | " + classCode.getClassification() + " Exclusion - Exterior Insulation and Finish Systems CG 21 86 was available");
                        break;
                    default:
                        softAssert.assertTrue(guidewireHelpers.isRequired("Exclusion - Exterior Insulation and Finish Systems CG 21 86"),
                                classCode.getCode() + " | " + classCode.getClassification() + " Exclusion - Exterior Insulation and Finish Systems CG 21 86 was not required");
                        break;
                }//END SWITCH


                //unless 97047 97050
                //Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94
                switch (GLClassCode.valueOfName(classCode.getCode())) {
                    case GL_97047:
                    case GL_97050:
                        softAssert.assertFalse(guidewireHelpers.isAvailable("Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94"),
                                classCode.getCode() + " | " + classCode.getClassification() + " Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94 was Available");
                        break;
                    default:
                        softAssert.assertTrue(guidewireHelpers.isRequired("Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94"),
                                classCode.getCode() + " | " + classCode.getClassification() + " Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94 was not Required");
                        break;
                }//END SWITCH
                //Required when one of the following class codes are seleted: {"91111", "91127", "91150", "91155", "91340", "91341", "91342", "91343", "91405", "91436", "91551", "91560", "91562", "91577", "91580", "91581", "91582", "91583", "91584", "91585", "98678", "91629", "91746", "91805", "92101", "92215", "92338", "92451", "92478", "92663", "94007", "94276", "94381", "94444", "94569", "95410", "95487", "95625", "95647", "95648", "96408", "98678", "96410", "96611", "96702", "97047", "97447", "97651", "97652", "97653", "97654", "97655", "98303", "98304", "98305", "98306", "98309", "98344", "98449", "98482", "98483", "98502", "98640", "98678", "98705", "98806", "98820", "98884", "98967", "99080", "99163", "99506", "99507", "99570", "99572", "99746", "99946", "99952", "99953", "99954", "99969", "99986", "99987"}. Other than that this electable.
                //Exclusion Of Coverage For Structures Built Outside Of Designated Areas Endorsement IDCG 31 2007
                switch (GLClassCode.valueOfName(classCode.getCode())) {
                    case GL_91111:
                    case GL_91127:
                    case GL_91150:
                    case GL_91155:
                    case GL_91340:
                    case GL_91341:
                    case GL_91342:
                    case GL_91343:
                    case GL_91405:
                    case GL_91436:
                    case GL_91551:
                    case GL_91560:
                    case GL_91562:
                    case GL_91577:
                    case GL_91580:
                    case GL_91581:
                    case GL_91582:
                    case GL_91583:
                    case GL_91584:
                    case GL_91585:
                    case GL_91629:
                    case GL_91746:
                    case GL_91805:
                    case GL_92101:
                    case GL_92215:
                    case GL_92338:
                    case GL_92451:
                    case GL_92478:
                    case GL_92663:
                    case GL_94007:
                    case GL_94276:
                    case GL_94381:
                    case GL_94444:
                    case GL_94569:
                    case GL_95410:
                    case GL_95487:
                    case GL_95625:
                    case GL_95647:
                    case GL_95648:
                    case GL_96408:
                    case GL_96410:
                    case GL_96611:
                    case GL_96702:
                    case GL_97047:
                    case GL_97447:
                    case GL_97651:
                    case GL_97652:
                    case GL_97653:
                    case GL_97654:
                    case GL_97655:
                    case GL_98303:
                    case GL_98304:
                    case GL_98305:
                    case GL_98306:
                    case GL_98309:
                    case GL_98344:
                    case GL_98449:
                    case GL_98482:
                    case GL_98483:
                    case GL_98502:
                    case GL_98640:
                    case GL_98678:
                    case GL_98705:
                    case GL_98806:
                    case GL_98820:
                    case GL_98884:
                    case GL_98967:
                    case GL_99080:
                    case GL_99163:
                    case GL_99506:
                    case GL_99507:
                    case GL_99570:
                    case GL_99572:
                    case GL_99746:
                    case GL_99946:
                    case GL_99952:
                    case GL_99953:
                    case GL_99954:
                    case GL_99969:
                    case GL_99986:
                    case GL_99987:
                        softAssert.assertTrue(guidewireHelpers.isRequired("Exclusion Of Coverage For Structures Built Outside Of Designated Areas Endorsement IDCG 31 2007"),
                                classCode.getCode() + " | " + classCode.getClassification() + " Exclusion Of Coverage For Structures Built Outside Of Designated Areas Endorsement IDCG 31 2007 was not Required");
                        break;
                    default:
                        softAssert.assertFalse(guidewireHelpers.isElectable("Exclusion Of Coverage For Structures Built Outside Of Designated Areas Endorsement IDCG 31 2007"),
                                classCode.getCode() + " | " + classCode.getClassification() + " Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94 was Available");
                        break;
                }//END SWITCH

                //				Exclusion - Movement Of Buildings Or Structures CG 21 17
                //				� Available when question "Does applicant/insured move buildings or structures?" is answered yes.
            } catch (Exception e) {
                softAssert.fail("FAILED ON CLASS CODE: " + classCode.getCode());
                System.out.println("FAILED ON CLASS CODE: " + classCode.getCode());
                guidewireHelpers.logout();
                new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
                guidewireHelpers.editPolicyTransaction();
            }//END CATCH
        }//END FOR

        softAssert.assertAll();

    }//END testContractorsExclusionsAndConditions


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement See Product Model
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description Test the Exclusion and/or Condition that should only be available to underwriters
     * @DATE Aug 18, 2016
     */
    @SuppressWarnings("serial")
    @Test(dependsOnMethods = {"generatePolicy"})
    public void testUWOnlyExclusionsAndConditions() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);
        Underwriters randomUW = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Commercial, Underwriter.UnderwriterTitle.Underwriter);

        login.loginAndSearchSubmission(randomUW.getUnderwriterUserName(), randomUW.getUnderwriterPassword(), myPolicyObj.accountNumber);
        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuGLExposures();

        GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
        exposuresPage.clickExposureDetialsTab();
        //remove exposure
        exposuresPage.selectAll();
        exposuresPage.clickRemove();
        exposuresPage.clickAdd();
        CPPGeneralLiabilityExposures myExposure = new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0), GLClassCodeHelper.getRandomGLContractorClassCode().getCode());
        exposuresPage.addExposure(myExposure);
        myPolicyObj.generalLiabilityCPP.setCPPGeneralLiabilityExposures(new ArrayList<CPPGeneralLiabilityExposures>() {{
            this.add(myExposure);
        }});

        exposuresPage.clickLocationSpecificQuestionsTab();
        exposuresPage.fillOutUnderwritingQuestionsQQ(myExposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());

        exposuresPage.clickUnderwritingQuestionsTab();
        if (myPolicyObj.commercialPackage.packageRisk.equals(PackageRiskType.Contractor)) {
            exposuresPage.fillOutUnderwritingContractorQuestions(myPolicyObj);
        }

        sideMenu.clickSideMenuGLCoverages();
        GenericWorkorderGeneralLiabilityCoveragesCPP coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        coverages.clickExclusionsAndConditions();


        //unless 97047 97050
        //Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94
        //� Required when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if the following class codes are selected: 97047 or 97050. 
        //Agents cannot add/remove this endorsement. When this endorsement is not required, Underwriter can add/remove this endorsement. 
        //UW cannot remove the endorsement when it is attached as required.
        switch (GLClassCode.valueOfName(myExposure.getClassCode())) {
            case GL_97047:
            case GL_97050:
                softAssert.assertFalse(guidewireHelpers.isAvailable("Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94"),
                        myExposure.getClassCode() + " | " + myExposure.getDescription() + " FAILED Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94 was Required/Suggested when it was supposed to be Electable");

                GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions exclusionsConditions = new GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions(driver);
                exclusionsConditions.checkExclusion_DamageToWorkPerformedBySubcontractorsOnYourBehalfCG2294(true);
                GenericWorkorder genwo = new GenericWorkorder(driver);
                genwo.clickGenericWorkorderSaveDraft();
                guidewireHelpers.logout();
                login.loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
                sideMenu.clickSideMenuGLCoverages();
                coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coverages.clickExclusionsAndConditions();
                softAssert.assertTrue(guidewireHelpers.isRequired("Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94"),
                        myExposure.getClassCode() + " | " + myExposure.getDescription() + " FAILED Exclusion - Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94 was not Required after UW added it.");
                break;
            default:
                softAssert.assertTrue(guidewireHelpers.isRequired("Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94"),
                        myExposure.getClassCode() + " | " + myExposure.getDescription() + " FAILED Exclusion - Exclusion - Damage To Work Performed By Subcontractors On Your Behalf CG 22 94 was not Required");
                break;
        }//END SWITCH
    }//END testUWOnlyExclusionsAndConditions

}//EOF





























