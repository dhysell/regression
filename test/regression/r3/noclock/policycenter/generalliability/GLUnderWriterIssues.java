package regression.r3.noclock.policycenter.generalliability;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInsuredTypeGL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GLClassCode;
import repository.gw.enums.GeneralLiabilityCoverages;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPGLCoveragesAdditionalInsureds;
import repository.gw.generate.custom.CPPGeneralLiability;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.UnderwriterIssue;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderGLCoveragesAdditionalInsured;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoveragesCPP;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoverages_AdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoverages_UnderwriterQuestions;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification_GeneralLiability;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import persistence.globaldatarepo.entities.GLUWIssues;
import persistence.globaldatarepo.helpers.GLClassCodeHelper;
import persistence.globaldatarepo.helpers.GLUWIssuesHelper;

@QuarantineClass
public class GLUnderWriterIssues extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    CPPGeneralLiabilityExposures currentExposure = null;
    SoftAssert softAssert = new SoftAssert();
    private WebDriver driver;


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement http://projects.idfbins.com/policycenter/To%20Be%20Process/Commercial%20Package%20Policy%20(CPP)/Guidewire%20-%20General%20Liability/WCIC%20General%20Liability-Product-Model.xlsx
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description Test all Underwriting Issues for General Liability
     * @DATE May 3, 2016
     */
    @SuppressWarnings("serial")
    @Test(enabled = true)
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
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), GLClassCode.GL_18206.getValue()));

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
                .withPolicyLocations(locationsLists)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("GL_UW Issues")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsLists.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);


    }


    /**
     * @throws Exception
     * @Author jlarsen
     * @Description This test will test a random set of UW Issues. to test them all would take forevaaaaa
     * get the random list
     * and pass them into a Giant switch statement to set all that need setting
     * Quote the policy and verify the correct messages appear on Risk Analysis page
     * @DATE Sep 9, 2016
     */
    @SuppressWarnings("serial")
    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void verifyInformationalUWIssues() throws Exception {

        System.out.println("#########################\nRUNNING TEST VERIFY INFORMATIONAL UW ISSUES\n######################");
        List<String> coverageList = new ArrayList<String>();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        List<GLUWIssues> uwIssuesToTest = GLUWIssuesHelper.getInformationalUWIssues(5);

        new Login(driver).loginAndSearchSubmission("hhill", "gw", myPolicyObj.accountNumber);
        //		loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        String restrictedClassCode = GLClassCodeHelper.getGLRestrictedClassCodes().toString();
        //set condition in policy
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        for (GLUWIssues uwIssues : uwIssuesToTest) {
            guidewireHelpers.editPolicyTransaction();

            switch (uwIssues.getRuleMessage().replace("  ", " ")) {
                case "Professional coverage for Beauty Parlors and Hair Styling Salons is not offered under the CPP. Please contact Brokerage or review for eligibility under the BOP.":
                    //When class code 10115 is selected display the message.
                    validateUWIssueInformationalRefresh(uwIssues, GLClassCode.GL_10115.getValue());
                    break;
                case "Building Material Dealers � secondhand material is eligible only in conjunction with an otherwise eligible classification.":
                    //When class code 10256 is selected display the message.
                    validateUWIssueInformationalRefresh(uwIssues, GLClassCode.GL_10256.getValue());
                    break;
                case "Provide details regarding what industries and services the applicant/insured is involved in.":
                    //When class code 11208 or 11201 is selected display the message.
                    validateUWIssueInformationalRefresh(uwIssues, (guidewireHelpers.getRandBoolean()) ? GLClassCode.GL_11208.getValue() : GLClassCode.GL_11201.getValue());
                    break;
                case "Contact Brokerage for Rigger Bailee Coverage.": //HAS 2 RULE CONDITIONS
                    //When class code 11201 is selected display the message.
                    validateUWIssueInformationalRefresh(uwIssues, GLClassCode.GL_11201.getValue());
                    break;
                case "Furrier Block coverage is recommended. Refer to Brokerage.":
                    //When class code 13314 is selected display the message.
                    validateUWIssueInformationalRefresh(uwIssues, GLClassCode.GL_13314.getValue());
                    break;
                case "If insured has outside lumber storage photos are required.":
                    //When class code 14279 is selected display the message.
                    validateUWIssueInformationalRefresh(uwIssues, GLClassCode.GL_14279.getValue());
                    break;
                case "Provide underwriting with details of the types of property rented.":
                    //When class code 16722 is selected display the message.
                    validateUWIssueInformationalRefresh(uwIssues, GLClassCode.GL_16722.getValue());
                    break;
                case "Provide underwriting with details of types of property sold.":
                    //When class code 16881 is selected display the message.
                    validateUWIssueInformationalRefresh(uwIssues, GLClassCode.GL_16881.getValue());
                    break;
                case "Provide underwriting with details of what type of products the applicant/insured sells.":
                    //When class code 18438 or 18437 is selected display the message.
                    validateUWIssueInformationalRefresh(uwIssues, (guidewireHelpers.getRandBoolean()) ? GLClassCode.GL_18438.getValue() : GLClassCode.GL_18437.getValue());
                    break;
                case "Provide underwriting with details of the purpose of the club.":
                    //When class code 41667, 41669, 41668, or 41670 is selected display the message.
                    List<String> classCodes = new ArrayList<String>() {{
                        this.add(GLClassCode.GL_41667.getValue());
                        this.add(GLClassCode.GL_41669.getValue());
                        this.add(GLClassCode.GL_41668.getValue());
                        this.add(GLClassCode.GL_41670.getValue());
                    }};
                    validateUWIssueInformationalRefresh(uwIssues, classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
                    break;
                case "Applicant/Insured may desire event cancellation coverage or professional coverage, contact Brokerage for availability.":
                    //When class code 44280 is selected display the message.
                    validateUWIssueInformationalRefresh(uwIssues, GLClassCode.GL_44280.getValue());
                    break;
                case "Describe quality control processes that the applicant/insured has in place for Lighting Fixtures Manufacturing. ":
                    //When class code 56391 is selected display the message.
                    validateUWIssueInformationalRefresh(uwIssues, GLClassCode.GL_56391.getValue());
                    break;
                case "Personal/advertising injury is excluded per CG 00 01 due to nature of business. Contact Brokerage for this coverage.":
                    //When class code 58408, 58409, 58456, 58457, 58458, 58459, 90089, 98597, or 98598 is selected display the message.
                    classCodes = new ArrayList<String>() {{
                        this.add(GLClassCode.GL_58408.getValue());
                        this.add(GLClassCode.GL_58409.getValue());
                        this.add(GLClassCode.GL_58456.getValue());
                        this.add(GLClassCode.GL_58457.getValue());
                        this.add(GLClassCode.GL_58458.getValue());
                        this.add(GLClassCode.GL_58459.getValue());
                        this.add(GLClassCode.GL_90089.getValue());
                        this.add(GLClassCode.GL_98597.getValue());
                        this.add(GLClassCode.GL_98598.getValue());
                    }};
                    validateUWIssueInformationalRefresh(uwIssues, classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
                    break;
                case " is a restricted class, the underwriter will need to review this class to determine eligibility.":
                    //There is a Excel spread sheet a called GL classes for new system and excluded classes in SharePoint. On this spread sheet on the tab Class Codes to Include on column F any class code that has an R in that column we need to block quote the policy and display the message.
                    setExposure(myPolicyObj.commercialPackage.locationList.get(0), restrictedClassCode);
                    break;
                case "Please describe what methods the applicant/insured employs to inventory and track the goods that are stored in the warehouse and who is authorized to remove the property.":
                    //When class code 99917 is selected display the message.
                    validateUWIssueInformationalRefresh(uwIssues, GLClassCode.GL_99917.getValue());
                    break;
                case "There is no coverage for property in the insured�s care custody or control.":
                    //When class code 99917 or 99938 is selected display the message.
                    validateUWIssueInformationalRefresh(uwIssues, (guidewireHelpers.getRandBoolean()) ? GLClassCode.GL_99917.getValue() : GLClassCode.GL_99938.getValue());
                    break;
                case "Underwriter please review question �Please provide underwriting details of the equine exposure i.e. type of events, number of animals, number of events etc.�":
                    //When endorsement CG 20 14 is selected display the message.
                    GenericWorkorderGeneralLiabilityCoverages_AdditionalCoverages additioanlCoverages = new GenericWorkorderGeneralLiabilityCoverages_AdditionalCoverages(driver);
                    additioanlCoverages.checkAIUsersOfTeamsDraftOrSaddleAnimalsCG2014(true);
                    break;
                case "Professional liability exclusion has been added please check to see if E&O is written elsewhere, if not contact Brokerage for a quote.":
                    //When endorsement CG 21 16, CG 21 52, CG 21 56, CG 21 57, CG 21 58, CG 21 59, CG 22 24, CG 22 32, CG 22 33, CG 22 43, CG 22 44 , CG 22 45, CG 22 48, CG 22 75, CG 22 77 , CG 22 79, CG 22 88, CG 22 90, CG 22 91, CG 22 96, CG 22 98, CG 22 99, or CG 23 01 is selected display the message.
                    coverageList = new ArrayList<String>() {{
                        this.add("CG 21 16");
                        this.add("CG 21 52");
                        this.add("CG 21 56");
                        this.add("CG 21 57");
                        this.add("CG 21 58");
                        this.add("CG 21 59");
                        this.add("CG 22 24");
                        this.add("CG 22 32");
                        this.add("CG 22 33");
                        this.add("CG 22 43");
                        this.add("CG 22 44");
                        this.add("CG 22 45");
                        this.add("CG 22 48");
                        this.add("CG 22 75");
                        this.add("CG 22 77");
                        this.add("CG 22 79");
                        this.add("CG 22 88");
                        this.add("CG 22 90");
                        this.add("CG 22 91");
                        this.add("CG 22 96");
                        this.add("CG 22 98");
                        this.add("CG 22 99");
                        this.add("CG 23 01");
                    }};
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), coverageList.get(NumberUtils.generateRandomNumberInt(0, coverageList.size() - 1)));
                    break;
                case "Personal and Advertising Injury has been excluded. Notify the applicant/insured.":
                    //When endorsement CG 21 38 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 21 38");
                    break;
                case "Property entrusted to the applicant/insured is not covered. Contact Brokerage for coverage.":
                    //When endorsement CG 22 29 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 22 29");
                    break;
                case "Corporal Punishment has been Excluded notify applicant":
                    //When endorsement CG 22 30 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 22 30");
                    break;
                case "No coverage is provided under this policy for professional coverage; inform the applicant/insured that they need professional coverage through other sources.":
                    //When endorsement CG 22 34 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 22 34");
                    break;
                case "Fiduciary exclusion has been added please check to see if coverage is written elsewhere, if not contact Brokerage for a quote.":
                    //When endorsement CG 22 38 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 22 38");
                    break;
                case "A medical professional liability exclusion has been added please check to see if E&O is written elsewhere, if not contact Brokerage for a quote.":
                    //When endorsement CG 22 39 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 22 39");
                    break;
                case "Notify applicant/insured there is no coverage for the failure to supply gas, oil, water, electricity, steam or biofuel.":
                    //When endorsement CG 22 50 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 22 50");
                    break;
                case "Notify applicant/insured there is no coverage for damage to laundry and dry cleaning.":
                    //When endorsement CG 22 53 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 22 53");
                    break;
                case "Underground Resources and equipment is not covered. Contact Brokerage for coverage.":
                    //When endorsement CG 22 57 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 22 57");
                    break;
                case "Notify applicant/insured there is no coverage for property damage to property managed.":
                    //When endorsement CG 22 70 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 22 70");
                    break;
                case "Notify applicant/insured that the Colleges or Schools endorsement restricts coverage for transportation of students, heath services, sports � related bodily injury and medical coverage for students.":
                    //When endorsement CG 22 71 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 22 71");
                    break;
                case "A seed merchants exclusion has been added please check to see if coverage is written elsewhere, if not contact Brokerage for a quote.":
                    //When endorsement CG 22 81 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 22 81");
                    break;
                case "Inform applicant/insured that coverage is limited to the premises and if additional locations are to be considered, please add them to CG 28 06.":
                    //When endorsement CG 28 06 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 28 06");
                    break;
                case "Inform applicant/insured that Bodily Injury claims by your employees against an additional insured is not covered.":
                    //When endorsement IDCG 31 2008 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "IDCG 31 2008");
                    break;
                case "Inform applicant/insured that coverage is not provided for mixing or application of chemicals, cleaning of seed, containers, lack of performance, failure of product or work to meet the level of performance. If coverage is desired contact Brokerage for assistance.":
                    //When endorsement IDCG 31 2010 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "IDCG 31 2010");
                    break;
                case "Employment Practices Liability Warranty Statement IDCW 32 0001 is required.":
                    //When endorsement IDCG 31 2013 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "IDCG 31 2013");
                    break;
                case "Products-Completed Operations has been excluded. Explain the coverage limitation to the insured.":
                    //When endorsement CG 21 04 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 21 04");
                    break;
                case "No coverage is provided under this policy for products or professional coverage; inform the applicant/insured that they need products/professional through a professional policy.":
                    //When one of the following class codes is selected: 12375, 12374, 15839, or 45900 display the message.
                    classCodes = new ArrayList<String>() {{
                        this.add(GLClassCode.GL_12375.getValue());
                        this.add(GLClassCode.GL_12374.getValue());
                        this.add(GLClassCode.GL_15839.getValue());
                        this.add(GLClassCode.GL_45900.getValue());
                    }};
                    validateUWIssueInformationalRefresh(uwIssues, classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
                    break;
                case "Provide evidence of certification for all unmanned operators.":
                    //When endorsement CG 24 50 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 24 50");
                    break;
                case "Notify applicant/insured that CG 24 50 Limited coverage for designated unmanned aircraft does not provide coverage for personal or advertising injury.":
                    //When endorsement CG 24 50 is selected display the message.
                    break;
                case "Unmanned aircraft was assembled from a kit, provide underwriting with complexity of assembly and experience.":
                    //When question CGQAC1100 is answered with "Assembled from a kit" display the message.
                    break;
                case "WCIC does not provide coverage for non-owned animals, inform applicant/insured.":
                    //When class code 48636 is selected display the message.
                    validateUWIssueInformationalRefresh(uwIssues, GLClassCode.GL_48636.getValue());
                    break;
                case "Underwriting please review where boats are used.":
                    //When class code 10117 is selected display the message.
                    validateUWIssueInformationalRefresh(uwIssues, GLClassCode.GL_10117.getValue());
                    break;

                case "Applicant/Insured works on buildings or structures over 3 stories. Risk is being referred to underwriting.":
                    //When class code 98303 is selected and question �Indicate type of buildings or structures does applicant/insured work on.� Is selected with � Apartments � Bridges � Manufacturer building � Manufacturer equipment � Office building � Steel � Structures � Tanks � water � Other, then display the message.
                    break;

                case "Applicant/Insured provides real estate management services. Add class 47052 or real estate management services will be excluded with CG 22 60.":
                    //If class code 47050 is on the policy and the question �Does applicant/Insured provide real estate management services?� is yes and class code 47052 is not on the policy display the message.
                    setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_47050.getValue());
                    //TODO Set Location Specific Questions to Yes
                    break;

                case "An Additional Insured under the General Liability line has location that has been edited/removed.":
                    //"Please go back to ${endorsement number, Name if Company, first and last name if person} under the "General Liability" wizard step "Coverages" and tab "Additional Insureds / Certificate Holders" and either remove the endorsement or change the location on the additional insured."
                    //Check each designated location/premises from each General Liability additional insured against a list of the current locations displaynames to make sure all the designated locations/premises are currently on the policy. If it finds one that is not currently on the locations display the message. This only works with the drop down of existing locations.
                    break;
                //case "Contact Brokerage for Rigger Bailee Coverage.":
                //When class code 11208 is selected and question �Does applicant move items other than house trusses or a/c units?� is selected no display the message.
                //setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_11208.getValue());
                //break;   //HAS 2 RULE CONDITIONS
                case "Percentage of receipts LPG Gas sales is incidental.":
                    //When class code 13410 is selected and question "Indicate the % of receipts LPG Gas sales represent to the total business." is answered with 0-5% display the message.
                    setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_13410.getValue());
                    break;
                case "To qualify for Excess vendor coverage, please provide certificate of insurance from the manufacturer showing additional insured status.":
                    //If Yes is selected on the question "Is applicant/insured an additional insured on the manufacturers� policy?", display the message.
                    break;
                case "Insured is licensed for unmanned aircraft provide copy of FAA license to underwriting.":
                    //When endorsement CG 24 50 is selected display the message.
                    break;
                case "Provide underwriting with a description of procedures regarding waiting periods for drugs, medications and feed additives.":
                    //When class code 48636 is selected and the question "Certain drugs, medications or feed additives require a waiting period before an animal can be marketed. Are any of these used?" is answered Yes the display the message.
                    setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_48636.getValue());
                    break;
                case "Underwriter please check for Excess policy. If applicant/insured has Excess policy please refer to AAIC for Excess pricing.  ":
                    //If 5,000,000 or more is entered in for the question "What are the applicant/insured estimated annual gross receipts?" located on the  Screen Coverages > Tab Underwriting Questions, then display the message.
                    break;

            }//END SWITCH
        }//END FOR
        softAssert.assertAll();
    }//END verifyInformationalUWIssues

    /**
     * @throws Exception
     * @Author jlarsen
     * @Description This test will test all the UW Issues from above
     * set all coverage requirements to get all the Issues and Quote
     * @DATE Sep 9, 2016
     */
    @SuppressWarnings({"serial", "unused"})
    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void verifyBlockBindUWIssues() throws Exception {

        System.out.println("#########################\nRUNNING TEST VERIFY BLOCK BIND UW ISSUES\n######################");

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        List<GLUWIssues> uwIssuesToTest = GLUWIssuesHelper.getAllBlockBindUWIssues(5);

        new Login(driver).loginAndSearchSubmission("hhill", "gw", myPolicyObj.accountNumber);
        //loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
        GenericWorkorderGeneralLiabilityCoveragesCPP coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        String restrictedClassCode = GLClassCodeHelper.getGLRestrictedClassCodes().toString();
        //set condition in policy
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        for (GLUWIssues uwIssues : uwIssuesToTest) {

            guidewireHelpers.editPolicyTransaction();

            switch (uwIssues.getRuleMessage()) {
                case "Underwriter please review the water in which the applicant/insured uses the boat.":
                    //When class code 40115 and/or 40117 is selected display the message.
                    List<String> classCodes = new ArrayList<String>() {{
                        this.add(GLClassCode.GL_40115.getValue());
                        this.add(GLClassCode.GL_40117.getValue());
                    }};
                    validateUWIssueBlockBindRefresh(uwIssues, classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
                    break;
                case "Underwriter please review the year, make, model, and HP of the watercraft.":
                    //When class code 40115 is selected display the message.
                    validateUWIssueBlockBindRefresh(uwIssues, GLClassCode.GL_40115.getValue());
                    break;
                case "Designated Construction and location limits require a copy of the contract, number of projects the client is involved in per year, and where the projects are. Provide details to underwriting.":
                    //When endorsement CG 25 03 is selected display the message.
                    //Available when Designated Construction Project(s) General Aggregate Limit CG 25 03 located under the Additional Insured screen is selected Yes.
                    break;
                case "Underwriting please review where boats are used.":
                    //When class code 10117 is selected display the message.
                    validateUWIssueBlockBindRefresh(uwIssues, GLClassCode.GL_10117.getValue());
                    break;
                case "Percentage of receipts LPG Gas sales is moderate contact underwriting for acceptability.":
                    //When class code 13410 is selected and question Indicate the % of receipts LPG Gas sales represent to the total business. is answered with 6-10% or 11-15% display the message.
                    //setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_13410.getValue());
                    break;
                case "Insured is operating out of our preferred territory. Please provide details for Underwriting approval.":
                    //If under the Additional Insured - Owners, Lessees Or Contractors - Scheduled Person Or Organization CG 20 10 or Additional Insured - Designated Person Or Organization CG 20 26 under the question Where does the Insured perform the activities described below? is answered with any of the following: Washington, Oregon, Montana, Utah, Or Wyoming, then display the message.
                    break;
                case "${ADDITIONAL INSURED NAME} works for companies in high risk activities. Please supply Underwriting with details of our insured's activities.":
                    //On Additional Insured screen, if user answer Yes on any of the available industries under the question Does the Certificate Holder or Additional Insured operate in any of the following industries?
                    break;
                case "Special wording on certificates requires underwriting approval.":
                    //On the Additional Insured screen if the User selects Yes to the Special Wording then display the message.
                    break;
            }//END SWITCH
        }//END FOR
    }//END verifyBlockBindUWIssues


    /**
     * @throws Exception
     * @Author jlarsen
     * @Description This test will test a random set from the list above. As with the Informational testing them all would take forevaaaaaaa
     * get the random list
     * and pass them into a Giant switch statement to set all that need setting
     * Quote the policy and verify the correct messages appear on Risk Analysis page
     * @DATE Sep 9, 2016
     */
    @SuppressWarnings({"serial", "unused"})
    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void verifyBlockQuoteReleaseUWIssues() throws Exception {

        System.out.println("#########################\nRUNNING TEST VERIFY BLOCK QUOTE RELEASE UW ISSUES\n######################");

        boolean testFailed = false;
        String failureList = "UW ISSUES THAT FAILED:   \n";
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        List<GLUWIssues> uwIssuesToTest = GLUWIssuesHelper.getAllBlockQuoteReleaseUWIssues(5);

        new Login(driver).loginAndSearchSubmission("hhill", "gw", myPolicyObj.accountNumber);
        //loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
        GenericWorkorderGeneralLiabilityCoveragesCPP coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        String restrictedClassCode = GLClassCodeHelper.getGLRestrictedClassCodes().toString();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        //set condition in policy
        for (GLUWIssues uwIssues : uwIssuesToTest) {

            guidewireHelpers.editPolicyTransaction();

            switch (uwIssues.getRuleMessage()) {
                case "Treaty exception is required for limits over $500,000.":
                    //When class code 10140, 10141, 51350, 51351, and/or 51352 is selected and the Liability limit is over $500,000 display the message.
                    List<String> classCodes = new ArrayList<String>() {{
                        this.add(GLClassCode.GL_10140.getValue());
                        this.add(GLClassCode.GL_10141.getValue());
                        this.add(GLClassCode.GL_51350.getValue());
                        this.add(GLClassCode.GL_51351.getValue());
                        this.add(GLClassCode.GL_51352.getValue());
                    }};
                    //validateUWIssueBlockQuoteReleaseRefresh(uwIssues, classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size()-1)));
                    break;
                case "Insured rents heavy equipment to others, please provide copies of sample contracts - need AAIC approval.":
                    //When class code 11208 is selected display the message.
                    validateUWIssueBlockQuoteReleaseRefresh(uwIssues, GLClassCode.GL_11208.getValue());
                    break;
                case "Contact underwriting with description of activities performed to determine if a more specific class applies.":
                    //When class code 48600 is selected display the message.
                    validateUWIssueBlockQuoteReleaseRefresh(uwIssues, GLClassCode.GL_48600.getValue());
                    break;
                case "Applicant/Insured  operates a theater. Refer to underwriting for treaty exception.":
                    //When class code 49181 is selected display the message.
                    validateUWIssueBlockQuoteReleaseRefresh(uwIssues, GLClassCode.GL_49181.getValue());
                    break;
                case "Underwriter please review the control procedures for Feed Manufacturing.":
                    //When class code 53001 is selected display the message.
                    validateUWIssueBlockQuoteReleaseRefresh(uwIssues, GLClassCode.GL_53001.getValue());
                    break;
                case "Provide copy of label(s).":
                    //When class code 59223 is selected display the message.
                    validateUWIssueBlockQuoteReleaseRefresh(uwIssues, GLClassCode.GL_59223.getValue());
                    break;
                case "Underwriting please review the type of products manufactured.":
                    //When class code 59947 is selected display the message.
                    validateUWIssueBlockQuoteReleaseRefresh(uwIssues, GLClassCode.GL_59947.getValue());
                    break;
                case "Underwriting please review the type of chemicals used in the process.":
                    //When class code 59984 is selected display the message.
                    validateUWIssueBlockQuoteReleaseRefresh(uwIssues, GLClassCode.GL_59984.getValue());
                    break;
                case "Underwriting please review detail of vacant building.":
                    //When class code 68604, 68607, or 68606 is selected display the message.
                    classCodes = new ArrayList<String>() {{
                        this.add(GLClassCode.GL_68604.getValue());
                        this.add(GLClassCode.GL_68607.getValue());
                        this.add(GLClassCode.GL_68606.getValue());
                    }};
                    validateUWIssueBlockQuoteReleaseRefresh(uwIssues, classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
                    break;
                case "Underwriter please review the type of research, development and testing in the questions.":
                    //When class code 97003 or 97002 is selected display the message.
                    classCodes = new ArrayList<String>() {{
                        this.add(GLClassCode.GL_97003.getValue());
                        this.add(GLClassCode.GL_97002.getValue());
                    }};
                    validateUWIssueBlockQuoteReleaseRefresh(uwIssues, classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
                    break;
                case "Provide pictures of all areas of the pit including fence, gate & sign(s).":
                    //When class code 98555 is selected display the message.
                    validateUWIssueBlockQuoteReleaseRefresh(uwIssues, GLClassCode.GL_98555.getValue());
                    break;
                case "Underwriter please review the questions for Quarries. ":
                    //When class code 98555 is selected display the message.
                    validateUWIssueBlockQuoteReleaseRefresh(uwIssues, GLClassCode.GL_98555.getValue());
                    break;
                case "Treaty exception is required for Quarries with  limits over $500,000. ":
                    //When class code 98555 is selected and if the liability limits are $1,000,000 or more display the message.
                    //validateUWIssueBlockQuoteReleaseRefresh(uwIssues, GLClassCode.GL_98555.getValue());
                    break;
                case "Sandblasting is eligible only if incidental to otherwise eligible class and a $500 PD deductible applies.":
                    //When class code 98705, endorsement is selected display the message.
                    //validateUWIssueBlockQuoteReleaseRefresh(uwIssues, GLClassCode.GL_98705.getValue());
                    break;
                case "ISO developed CG 24 16 Canoes or Rowboats to be used in conjunction with Hotels and Motels. Please fully describe activities involved for consideration.":
                    //Business rule If class code 10110 is not used in conjunction with motel classifications 45190, 45191, 45192, 45193, 64074, or 64075 display the message.
                    validateUWIssueBlockQuoteReleaseRefresh(uwIssues, GLClassCode.GL_10110.getValue());
                    break;
                case "Class code _______ is a restricted class, the underwriter will need to review this class to determine eligibility.":
                    //There is a Excel spread sheet a called GL classes for new system and excluded classes in SharePoint. On this spread sheet on the tab Class Codes to Include on column F any class code that has an R in that column we need to block quote the policy and display the message.
                    break;
                case "Please describe what methods the applicant/insured employs to inventory and track the goods that are stored in the warehouse and who is authorized to remove the property.":
                    //When class code 99917 is selected display the message.
                    validateUWIssueBlockQuoteReleaseRefresh(uwIssues, GLClassCode.GL_99917.getValue());
                    break;
                case "Underwriter please review question �Please provide underwriting details of the equine exposure i.e. type of events, number of animals, number of events etc.�":
                    //When endorsement CG 20 14 is selected display the message.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 20 14");
                    break;
                case "For CG 24 17 please refer to Underwriting supervisor for review, treaty exception, and pricing.":
                    //When endorsement CG 24 17 is selected display the message. Also the Underwriting supervisor is the only one who can approve it.
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "CG 24 17");
                    break;
                case "EPLI limit must be submitted to our reinsurance carrier for consideration and pricing. Complete supplemental application and submit to underwriting.":
                    //When endorsement IDCG 31 2013 is selected and if aggregate limit is set to 500,000 or 1,000,000 the display the message.
                    break;
                case "EPLI deductible of $25,000 has been chosen and must be submitted to our reinsurance carrier for consideration and pricing.":
                    //When endorsement IDCG 31 2013 is selected and if a deductible limit of 25,000 is selected display the message.
                    break;
                case "Applicant/insured�s business type is not eligible for EPLI coverage. Contact Brokerage for coverage.":
                    //When endorsement IDCG 31 2013 is selected and one of the following class codes is selected: 43200, 11138, 67513, 67512, 47468, 47477, 47474, or 43551 display the message.
                    classCodes = new ArrayList<String>() {{
                        this.add(GLClassCode.GL_68604.getValue());
                        this.add(GLClassCode.GL_68607.getValue());
                        this.add(GLClassCode.GL_68606.getValue());
                    }};
                    setExposure(myPolicyObj.commercialPackage.locationList.get(0), classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
                    setCoverage(myPolicyObj.commercialPackage.locationList.get(0), "IDCG 31 2013");
                    break;
                case "Policy does not provide completed operations coverage, please remove Additional Insured-Completed Operations Endorsement CG 20 37.":
                    //The policy has to have completed operations (This is located under the General Liability CG 00 01 > Term Products / Completed Operations Aggregate Limit) or completed operations are included in the class code (This is defined by looking at the �GL classes for new system and excluded classes� excel file and looking at the column PremBase and looking for the symbol p+). Then display the message.
                    break;
                case "Unmanned aircraft was assembled from a kit, provide underwriting with complexity of assembly and experience.":
                    //When endorsement CG 24 50 > Term Type of unmanned aircraft. > Term Option Assembled from a kit is selected display the message.
                    break;
                case "Applicant/Insured works on buildings or structures over 3 stories. Risk is being referred to underwriting.":
                    //When class code 98303 is selected and question �Indicate type of buildings or structures does applicant/insured work on.� Is selected with � Apartments � Bridges � Manufacturer building � Manufacturer equipment � Office building � Steel � Structures � Tanks � water � Other, then display the message.
                    setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_98303.getValue());
                    break;
                case "Applicant/Insured  provides real estate management services. Add class 47052 or attach CG 22 60 to exclude the exposure.":
                    //If class code 47050 and the question �Does applicant/Insured provide real estate management services?� is yes and class code 47052 and CG 22 60 is not on the policy display the message.
                    setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_47050.getValue());
                    break;
                case "Please provide underwriting with details on type of instruction given.":
                    //When class code 47474 is selected and question �Does the applicant/insured provide training in welding, auto repair or sports activities?� is marked no then display the message.
                    setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_47474.getValue());
                    break;
                case "Risk contains at least 5 stores and 25,000 square feet of parking. Add appropriate parking classification(s) and/or refer to underwriting for assistance.":
                    //If class code 67635 is selected and question �Does the risk contain at least 5 stores and 25,000 square feet of parking?� is answered yes, display the message.
                    setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_67635.getValue());
                    break;
                case "Painting over 3 stories is more than incidental. Provide underwriting with details regarding types and locations of jobs, loss control methods to prevent injury to property or people below where work is being performed etc.":
                    //When class code 98303 is selected and question What percentage of the total receipts does painting over 3 stories represent? if �11-20%� is selected display the message.
                    setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_98303.getValue());
                    break;
                case "Applicant/Insured  paints oil and gas tanks between 6-10% of the time. Provide underwriting details regarding type, size and location of tanks.":
                    //When class code 98306 is selected and question �What % of the total receipts does painting of oil or gas tanks represent?� is answered 6-10% display the message.
                    setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_98306.getValue());
                    break;
                case "Applicant/Insured  paints steel structures or bridges between 6-10% of the time. Provide underwriting details regarding type, size and location of tanks.":
                    //When class code 98306 is selected and question �What % of the total receipts does painting of steel structures or bridges represent?� is answered 6-10% display the message.
                    //setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_98306.getValue());
                    break;
                case "Bicycle rental receipts are higher than average. Contact underwriting regarding pricing of the account.":
                    //If exposure 10151 is selected and the question �Indicate % of rental receipts versus sales and service.� Is answered the 16-25% then display the message.
                    setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_10151.getValue());
                    break;
                case "Insured/applicant exceeds what we consider incidental roofing activities. Please provide underwriting with details regarding past volume and future anticipated volume for consideration.":
                    //When class code 91342 and/or 91340 is selected and question �Indicate % of roofing receipts.� Is answered with 6-10% display the message.
                    setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_10117.getValue());
                    break;
                case "Based on the question Does applicant/insured need Liquor Liability Coverage? is answered Yes and the dependent question Type of operation is selected with _________. Please add that class code on to the policy before quoting.":
                    //When question Does applicant/insured need Liquor Liability Coverage? is answered Yes and the dependent question Type of operation is selected with a class code in it, Block Quote the policy until the class code that is selected, is on the policy. Display the message until the class code is selected.
                    break;
            }//END SWITCH
        }//END FOR

        softAssert.assertAll();

    }//END verifyBlockQuoteReleaseUWIssues


    /**
     * @param uwIssues  - UW Issue from Database
     * @param classCode - String Class Code
     * @Author jlarsen
     * @Description Checks Informational UW Issues Only
     * Sets required exposure class and answers all required questions
     * Verifies correct UW Issue was generated and Approves it.
     * Removes the condition required for that UW Issue - verifies it was removed.
     * Creates conditions for UW Issue and verifies a new Issue was generated
     * @DATE Sep 9, 2016
     */
    private void validateUWIssueInformationalRefresh(GLUWIssues uwIssues, String classCode) throws Exception {
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        //generate UW Issue
        setExposure(myPolicyObj.commercialPackage.locationList.get(0), classCode);
        UnderwriterIssue uwIssue = verifyAnyUWIssue(uwIssues, UnderwriterIssueType.Informational);
        if (uwIssue == null) {
            testFailedToGenerate(uwIssues, classCode);
        }


        //remove UW Issue condition
        guidewireHelpers.editPolicyTransaction();
        setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_18206.getValue());
        uwIssue = verifyAnyUWIssue(uwIssues, UnderwriterIssueType.Informational);
        if (uwIssue != null) {
            testFailedToRemove(uwIssues, classCode);
        }

        //generate UW Issue
        guidewireHelpers.editPolicyTransaction();
        setExposure(myPolicyObj.commercialPackage.locationList.get(0), classCode);
        uwIssue = verifyAnyUWIssue(uwIssues, UnderwriterIssueType.Informational);
        if (uwIssue != null) {
            System.out.println("UW Issue was as New! Yippy!!");
        } else {
            testFailedToRefresh(uwIssues, classCode);
        }
    }//END validateUWIssueInformationalRefresh


    /**
     * @param uwIssues  - UW Issue from Database
     * @param classCode - String Class Code
     * @Author jlarsen
     * @Description Checks Block Bind UW Issues Only
     * Sets required exposure class and answers all required questions
     * Verifies correct UW Issue was generated and Approves it.
     * Removes the condition required for that UW Issue - verifies it was removed.
     * Creates conditions for UW Issue and verifies a new Issue was generated
     * @DATE Sep 9, 2016
     */
    private void validateUWIssueBlockBindRefresh(GLUWIssues uwIssues, String classCode) throws Exception {
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        //generate UW Issue
        setExposure(myPolicyObj.commercialPackage.locationList.get(0), classCode);
        UnderwriterIssue uwIssue = verifyAnyUWIssue(uwIssues, UnderwriterIssueType.BlockSubmit);
        if (uwIssue != null) {
            GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
            risk.approveAll();
        } else {
            testFailedToGenerate(uwIssues, classCode);
        }
        //remove UW Issue condition
        guidewireHelpers.editPolicyTransaction();
        setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_18206.getValue());
        uwIssue = verifyAnyUWIssue(uwIssues, UnderwriterIssueType.BlockSubmit);
        if (uwIssue != null) {
            testFailedToRemove(uwIssues, classCode);
        }

        //generate UW Issue
        guidewireHelpers.editPolicyTransaction();
        setExposure(myPolicyObj.commercialPackage.locationList.get(0), classCode);
        uwIssue = verifyAnyUWIssue(uwIssues, UnderwriterIssueType.BlockSubmit);
        if (uwIssue != null) {
            System.out.println("UW Issue was as New! Yippy!!");
        } else {
            testFailedToRefresh(uwIssues, classCode);
        }
    }//END validateUWIssueBlockBindRefresh


    /**
     * @param uwIssues  - UW Issue from Database
     * @param classCode - String Class Code
     * @Author jlarsen
     * @Description Checks Block Quote Release UW Issues Only
     * Sets required exposure class and answers all required questions
     * Verifies correct UW Issue was generated and Approves it.
     * Removes the condition required for that UW Issue - verifies it was removed.
     * Creates conditions for UW Issue and verifies a new Issue was generated
     * @DATE Sep 9, 2016
     */
    private void validateUWIssueBlockQuoteReleaseRefresh(GLUWIssues uwIssues, String classCode) throws Exception {
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        //generate UW Issue
        setExposure(myPolicyObj.commercialPackage.locationList.get(0), classCode);
        UnderwriterIssue uwIssue = verifyAnyUWIssue(uwIssues, UnderwriterIssueType.BlockQuoteRelease);
        if (uwIssue != null) {
            GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
            risk.approveAll();
        } else {
            testFailedToGenerate(uwIssues, classCode);
        }
        //remove UW Issue condition
        guidewireHelpers.editPolicyTransaction();
        setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_18206.getValue());
        uwIssue = verifyAnyUWIssue(uwIssues, UnderwriterIssueType.BlockQuoteRelease);
        if (uwIssue != null) {
            testFailedToRemove(uwIssues, classCode);
        }

        //generate UW Issue
        guidewireHelpers.editPolicyTransaction();
        setExposure(myPolicyObj.commercialPackage.locationList.get(0), classCode);
        uwIssue = verifyAnyUWIssue(uwIssues, UnderwriterIssueType.BlockQuoteRelease);
        if (uwIssue != null) {
            System.out.println("UW Issue was as New! Yippy!!");
        } else {
            testFailedToRefresh(uwIssues, classCode);
        }
    }//END validateUWIssueBlockQuoteReleaseRefresh

    @SuppressWarnings("serial")
    private void setExposure(PolicyLocation location, String classCode) throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuGLExposures();
        CPPGeneralLiabilityExposures myExposure = new CPPGeneralLiabilityExposures(location, classCode);
        GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
        exposuresPage.clickExposureDetialsTab();
        exposuresPage.selectAll();
        exposuresPage.clickRemove();
        myPolicyObj.generalLiabilityCPP.setCPPGeneralLiabilityExposures(new ArrayList<CPPGeneralLiabilityExposures>() {{
            this.add(myExposure);
        }});
        currentExposure = myExposure;
        exposuresPage.clickAdd();
        exposuresPage.addExposure(myExposure);
        exposuresPage.clickLocationSpecificQuestionsTab();
        //FILL OUT UW QUESTIONS
        exposuresPage.fillOutBasicUWQuestionsQQ(myExposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
        //fillOutBasicUWQuestionsFullApp(myExposure);

        //set coverages
        sideMenu.clickSideMenuGLCoverages();
        GenericWorkorderGeneralLiabilityCoveragesCPP coveragesPage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        coveragesPage.fillOutGeneralLiabilityCoverages(myPolicyObj);
    }//END setExposure


    /**
     * @return
     * @Author jlarsen
     * @Description Creates the FullUnderWriterIssues with all the UW Issues on the Risk Analysis page.
     * Does not get what Buttons are available.
     * @DATE Sep 9, 2016
     */
    private FullUnderWriterIssues getUWIssues() {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        //quote the policy and check UW issues
        GenericWorkorder genwo = new GenericWorkorder(driver);
        genwo.clickGenericWorkorderQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();

        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        return risk.getUnderwriterIssues();
    }

    /**
     * @return
     * @Author jlarsen
     * @Description Creates the FullUnderWriterIssues with all the UW Issues on the Risk Analysis page.
     * Sets what buttons are available.
     * Much slower process. use getUWIssues() if you don't need to know about the buttons.
     * @DATE Sep 9, 2016
     */
    private FullUnderWriterIssues getUWIssuesWithButtons() {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        //quote the policy and check UW issues
        GenericWorkorder genwo = new GenericWorkorder(driver);
        genwo.clickGenericWorkorderQuote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();

        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        return risk.getUnderwriterIssuesWithButtons();
    }

    /**
     * @param uwIssue
     * @param type
     * @return UnderwriterIssue if UW Issue is found else return null
     * @Author rlonardo
     * @Description Verifies that UW Issue was generated
     * Gets All UW Issues and loops thru them
     * Sometimes Long and Short text gets switched
     * @DATE March 28, 2017
     */
    private UnderwriterIssue verifyAnyUWIssue(GLUWIssues uwIssue, UnderwriterIssueType type) {

        boolean longPassed = false;
        boolean shortPassed = false;

        List<UnderwriterIssue> issues = null;
        switch (type) {
            case Informational:
                issues = getUWIssues().getInformationalList();
                break;
            case BlockSubmit:
                issues = getUWIssuesWithButtons().getBlockSubmitList();
                break;
            case BlockQuoteRelease:
                issues = getUWIssuesWithButtons().getBlockQuoteReleaseList();
                break;
            default:
                break;
        }
        if (issues != null) {
            for (UnderwriterIssue issue : issues) {
                longPassed = validateLongUWMessage(uwIssue, issue);
                shortPassed = validateShortUWMessage(uwIssue, issue);
                if (longPassed || shortPassed) {
                    if (!longPassed) {
                        softAssert.fail("The LONG message for the " + type.name() + " under writer issue, associated with class code: " + currentExposure.getClassCode() + ", was not found or is incorrectly formatted.");
                    }
                    if (!shortPassed) {
                        softAssert.fail("The SHORT message for the " + type.name() + " under writer issue, associated with class code: " + currentExposure.getClassCode() + ", was not found or is incorrectly formatted.");
                    }
                    return issue;
                }
            }
        }
        return null;
    }

    private boolean validateLongUWMessage(GLUWIssues uwIssue, UnderwriterIssue issueToTest) {

        return issueToTest.getLongDescription().replace("  ", " ").trim().contains(uwIssue.getRuleMessage().replace("  ", " ").trim());

    }

    private boolean validateShortUWMessage(GLUWIssues uwIssue, UnderwriterIssue issueToTest) {

        return issueToTest.getShortDescription().replace("  ", " ").trim().contains(uwIssue.getRuleMessage().replace("  ", " ").trim());

    }


    private void setAdditionalIsured() {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuGLCoverages();
        GenericWorkorderGeneralLiabilityCoveragesCPP coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        coveragePage.clickAdditioanlInsureds();
        coveragePage.editAdditoinalInsured(myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().get(0));
        GenericWorkorderGLCoveragesAdditionalInsured additionalInsureds = new GenericWorkorderGLCoveragesAdditionalInsured(driver);
        additionalInsureds.fillOutAdditionalInsureds(myPolicyObj);
    }


    @SuppressWarnings("serial")
    private void setCoverage(PolicyLocation location, String coverage) throws Exception {
        GenericWorkorderGeneralLiabilityCoverages_UnderwriterQuestions uwQuestions = new GenericWorkorderGeneralLiabilityCoverages_UnderwriterQuestions(driver);
        List<String> tempList = new ArrayList<String>();
        SideMenuPC sideMenu = new SideMenuPC(driver);

        switch (coverage) {
            case "CG 00 01":
                //REQUIRED
                break;
            case "CG 20 20":
                //This becomes required when the question �Is the applicant/insured a not for profit organization whose major purposes is charitable causes?� is answered Yes.
                sideMenu.clickSideMenuQualification();
                GenericWorkorderQualification_GeneralLiability qual = new GenericWorkorderQualification_GeneralLiability(driver);
                qual.clickQualificationNotForProfitOrgCharitableCauses(true);
                break;
            case "CG 20 22":
                //This becomes required when class code 41650 is selected
                setExposure(location, GLClassCode.GL_41650.getValue());
                break;
            case "CG 20 02":
                //This becomes required when one of the following class codes are selected: 41668, 41667, 41670, 41669, or 11138.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_41668.getValue());
                    this.add(GLClassCode.GL_41667.getValue());
                    this.add(GLClassCode.GL_41670.getValue());
                    this.add(GLClassCode.GL_41669.getValue());
                    this.add(GLClassCode.GL_11138.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 20 03":
                //Available when Additional Insured - Concessionaires Trading Under Your Name CG 20 03 is selected on the Screen New Additional Insured.
                CPPGLCoveragesAdditionalInsureds myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_ConcessionairesTradingUnderYourName_CG_20_03);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 20 04":
                //This becomes required when one of the following class codes are selected: 62000, 62001, 62002, or 62003.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_62000.getValue());
                    this.add(GLClassCode.GL_62001.getValue());
                    this.add(GLClassCode.GL_62002.getValue());
                    this.add(GLClassCode.GL_62003.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 20 05":
                //Available when Additional Insured - Controlling Interest CG 20 05 is selected on the Screen New Additional Insured.
                myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_ControllingInterest_CG_20_05);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 20 27":
                //Available when Additional Insured - Co-Owner Of Insured Premises CG 20 27 is selected on the Screen New Additional Insured.
                myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_CoownerOfInsuredPremises_CG_20_27);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 20 26":
                //Available when Additional Insured - Designated Person Or Organization CG 20 26 is selected on the Screen New Additional Insured.
                myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_DesignatedPersonOrOrganization_CG_20_26);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 20 07":
                sideMenu.clickSideMenuGLCoverages();
                GenericWorkorderGeneralLiabilityCoveragesCPP coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);

                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2007);
                //ELECTIBLE
                break;
            case "CG 20 32":
                //Available when Additional Insured - Engineers, Architects Or Surveyors Not Engaged By The Named Insured CG 20 32 is selected on the Screen New Additional Insured.
                myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_EngineersArchitectsOrSurveyorsNotEngagedByTheNamedInsured_CG_20_32);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 20 23":
                //Available when Organization Type is Trust. Located on the Policy Info.
                sideMenu.clickSideMenuPolicyInfo();
                GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
                policyInfoPage.setPolicyInfoOrganizationType(OrganizationType.Trust);
                break;
            case "CG 20 29":
                //Available when Additional Insured - Grantor Of Franchise CG 20 29 is selected on the Screen New Additional Insured.
                myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_GrantorOfFranchise_CG_20_29);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 20 36":
                //Available when Additional Insured - Grantor Of Licenses CG 20 36 is selected on the Screen New Additional Insured.
                myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_GrantorOfLicenses_CG_20_36);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 20 28":
                //Available when Additional Insured - Lessor Of Leased Equipment CG 20 28 is selected on the Screen New Additional Insured.
                myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_LessorOfLeasedEquipment_CG_20_28);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 20 11":
                //Available when Additional Insured - Managers Or Lessors Of Premises CG 20 11 is selected on the Screen New Additional Insured.
                myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_ManagersOrLessorsOfPremises_CG_20_11);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 20 18":
                //Available when Additional Insured - Mortgagee, Assignee Or Receiver CG 20 18 is selected on the Screen New Additional Insured.
                myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_MortgageeAssigneeOrReceiver_CG_20_18);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 20 24":
                //Available when Additional Insured - Owners Or Other Interests From Whom Land Has Been Leased CG 20 24 is selected on the Screen New Additional Insured.
                myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_OwnersOrOtherInterestsFromWhomLandHasBeenLeased_CG_20_24);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 20 37":
                //Available when Additional Insured - Owners, Lessees Or Contractors - Completed Operations CG 20 37 is selected on the Screen New Additional Insured.
                myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_OwnersLesseesOrContractors_CompletedOperations_CG_20_37);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 20 10":
                //Available when Additional Insured - Owners, Lessees Or Contractors - Scheduled Person Or Organization CG 20 10 is selected on the Screen New Additional Insured.
                myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_OwnersLesseesOrContractors_ScheduledPersonOrOrganization_CG_20_10);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 20 12":
                //Available when Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations CG 20 12 is selected on the Screen New Additional Insured.
                myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizations_CG_20_12);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 20 13":
                //Available when Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations Relating To Premises CG 20 13 is selected on the Screen New Additional Insured.
                myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizationsRelatingToPremises_CG_20_13);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 20 17":
                //This becomes required when class code 68500 is selected.
                setExposure(location, GLClassCode.GL_68500.getValue());
                break;
            case "CG 20 08":
                //This becomes required when one of the following class codes are selected: 11138, 44070, 44072, 45190, 45191, 45192, or 45193.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_11138.getValue());
                    this.add(GLClassCode.GL_44070.getValue());
                    this.add(GLClassCode.GL_44072.getValue());
                    this.add(GLClassCode.GL_45190.getValue());
                    this.add(GLClassCode.GL_45191.getValue());
                    this.add(GLClassCode.GL_45192.getValue());
                    this.add(GLClassCode.GL_45193.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 20 14":
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2014);
                break;
            case "CG 20 15":
                //Available when Additional Insured - Vendors CG 20 15 is selected on the Screen New Additional Insured.
                myAdditionalInsured = new CPPGLCoveragesAdditionalInsureds() {{
                    this.setType(AdditionalInsuredTypeGL.AdditionalInsured_Vendors_CG_20_15);
                }};
                myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                    this.add(myAdditionalInsured);
                }});
                setAdditionalIsured();
                break;
            case "CG 21 51":
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2151);
                break;
            case "CG 24 17":
                //This is only available for underwriters to select. The agent can see the endorsement only when the underwriter selects it and then they can also see what the underwriter has written but they are not able to edit it. The agent can unselect the endorsement but cannot reselect it, the endorsement will no longer be visible.
                break;
            case "CG 25 03":
                //Available when Designated Construction Project(s) General Aggregate Limit CG 25 03 located under the Additional Insured screen is selected Yes.
                break;
            case "CG 25 04":
                //Available when Designated Location(s) General Aggregate Limit CG 25 04 located under the Additional Insured screen is selected Yes.
                break;
            case "IDCG 31 2013":
                //SUGGESTED
                break;
            case "IDCG 04 0001":
                //Available for Underwriters to select all the time. The agent can see the endorsement when the underwriter selects it and what the underwriter has entered in however they cannot edit it or select it. However if class code 97047 or 97050 is selected and the question "Is applicant/insured a licensed applicator?" is answered yes then the agent can select the endorsement. Also the agent can remove the endorsement whenever they want.
                break;
            case "IDCG 31 2006":
                //This becomes available when class code 97047 and/or 97050 is selected.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_97047.getValue());
                    this.add(GLClassCode.GL_97050.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            //TODO US6721
            case "CG 24 50":
                //Not available when CG 21 09 is selected.
                /*Used for Provide evidence of certification for all unmanned operators. and Notify applicant/insured that CG 24 50 Limited coverage for designated unmanned aircraft does not provide coverage for personal or advertising injury.*/

                break;

            //TODO US6721
            case "CG 21 04":
                //Available when the class premium base does not include (+) and Exclude is chosen on CG 00 01 - Products / Completed Operations Aggregate Limit (no premium charge is made) attach this form as required.

                break;


            case "CG 24 06":
                //This becomes required when one of the following class codes are selected: 16905 or 16906.
                //This also becomes required when the question ""Does applicant/insured allow patrons to bring their own alcoholic beverages?"" is answered Yes."
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_16905.getValue());
                    this.add(GLClassCode.GL_16906.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 00 33":
                //Available when one of the following class codes are selected: 70412, 58161, 50911, 59211, 58165, or 58166.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_70412.getValue());
                    this.add(GLClassCode.GL_58161.getValue());
                    this.add(GLClassCode.GL_50911.getValue());
                    this.add(GLClassCode.GL_59211.getValue());
                    this.add(GLClassCode.GL_58165.getValue());
                    this.add(GLClassCode.GL_58166.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 20 01":
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2001);
                //ELECTABLE
                break;
            case "IDCW 31 0002":
                //Available when Employment Practices Liability Insurance IDCG 31 2013 is selected and only in job type Policy Change.
                //Available for Underwriters and Agents to see but the Underwriter will only be allowed to select the endorsement at a backdated policy change job if the policy is canceled."

                break;
            case "IDCG 31 2004":
                //REQUIRED
                break;
            case "CG 22 28":
                //Available when class code 49333 is selected.
                setExposure(location, GLClassCode.GL_49333.getValue());
                break;
            case "CG 24 26":
                //REQUIRED
                break;
            case "CG 24 12":
                //Required when class codes 10117, 40115, 40140, 40117, or 43760 is selected.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_10117.getValue());
                    this.add(GLClassCode.GL_40115.getValue());
                    this.add(GLClassCode.GL_40140.getValue());
                    this.add(GLClassCode.GL_40117.getValue());
                    this.add(GLClassCode.GL_43760.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 24 16":
                //Available when one of the following class codes are selected: 41668, 41667, 45190, 45192, 64074, 10110, 40111, 41422, 45191, 45193, or 64075.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_41668.getValue());
                    this.add(GLClassCode.GL_41667.getValue());
                    this.add(GLClassCode.GL_45190.getValue());
                    this.add(GLClassCode.GL_45192.getValue());
                    this.add(GLClassCode.GL_64074.getValue());
                    this.add(GLClassCode.GL_10110.getValue());
                    this.add(GLClassCode.GL_40111.getValue());
                    this.add(GLClassCode.GL_41422.getValue());
                    this.add(GLClassCode.GL_45191.getValue());
                    this.add(GLClassCode.GL_45193.getValue());
                    this.add(GLClassCode.GL_64075.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 71":
                //Available when one of the following class codes are selected: 67513, 67512,  47474, or 47477.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_67513.getValue());
                    this.add(GLClassCode.GL_67512.getValue());
                    this.add(GLClassCode.GL_47474.getValue());
                    this.add(GLClassCode.GL_47477.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 24 10":
                //ELECTABLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2410);
                break;
            case "CG 21 44":
                //ELECTABLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2144);
                break;
            case "CG 28 06":
                //ELECTABLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2806);
                break;
            case "IDCG 31 2003":
                //REQUIRED
                break;
            case "CG 22 68":
                //Available when one of the following class codes are selected: 10072, 10073, 10367, 13453, 13455, or 18616.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_10072.getValue());
                    this.add(GLClassCode.GL_10073.getValue());
                    this.add(GLClassCode.GL_10367.getValue());
                    this.add(GLClassCode.GL_13453.getValue());
                    this.add(GLClassCode.GL_13455.getValue());
                    this.add(GLClassCode.GL_18616.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "IDCG 31 2005":
                //REQUIRED
                break;
            case "CG 24 07":
                //Available when one of the following class codes are selected: 14401, 16820, 16819, 16900, 16901, 16902, 16905, 16906, 16910, 16911, 16915, 16916, 16930, 16931, and/or 16941.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_14401.getValue());
                    this.add(GLClassCode.GL_16820.getValue());
                    this.add(GLClassCode.GL_16819.getValue());
                    this.add(GLClassCode.GL_16900.getValue());
                    this.add(GLClassCode.GL_16901.getValue());
                    this.add(GLClassCode.GL_16902.getValue());
                    this.add(GLClassCode.GL_16905.getValue());
                    this.add(GLClassCode.GL_16906.getValue());
                    this.add(GLClassCode.GL_16910.getValue());
                    this.add(GLClassCode.GL_16911.getValue());
                    this.add(GLClassCode.GL_16915.getValue());
                    this.add(GLClassCode.GL_16916.getValue());
                    this.add(GLClassCode.GL_16930.getValue());
                    this.add(GLClassCode.GL_16931.getValue());
                    this.add(GLClassCode.GL_16941.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 92":
                //Available when class code 99310 is selected.
                setExposure(location, GLClassCode.GL_99310.getValue());
                break;
            case "CG 24 04":
                //If Waiver of Subrogation is marked Yes under the Additional Insured screen then add this endorsement as required. This endorsement cannot be unselected. It is removed if Waiver of Subrogation is changed to a No.
                break;
            case "CG 21 46":
                //REQUIRED
                break;
            case "CG 21 50":
                //Available when one of the following class codes are selected: 16905 or 16906.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_16905.getValue());
                    this.add(GLClassCode.GL_16906.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "IDCG 31 2012":
                //This is only available for underwriters to select. The agent can see the endorsement and what the underwriter has entered in however they cannot edit it or select it.
                break;
            case "CG 21 32":
                //ELECTABLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2132);
                break;
            case "CG 21 47":
                //Available when Employement Practices Liability Insurance IDCG 31 2013 is not selected.
                break;
            case "IDCG 31 2008":
                //Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 99471.
                break;
            case "CG 21 07":
                //REQUIRED
                break;
            case "CG 22 87":
                //ELECTIBLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2287);
                break;
            case "CG 21 00":
                //ELECTIBLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2100);
                break;
            case "CG 21 01":
                //Available when one of the following class codes are selected: 63218, 63217, 63220, 63219, 43421, or 43424.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_63218.getValue());
                    this.add(GLClassCode.GL_63217.getValue());
                    this.add(GLClassCode.GL_63220.getValue());
                    this.add(GLClassCode.GL_63219.getValue());
                    this.add(GLClassCode.GL_43421.getValue());
                    this.add(GLClassCode.GL_43424.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 39":
                //Available when one of the following class codes are selected: 10332, 10331, or 41422.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_10332.getValue());
                    this.add(GLClassCode.GL_10331.getValue());
                    this.add(GLClassCode.GL_41422.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 34":
                //Available when class code 41620 is selected.
                setExposure(location, GLClassCode.GL_41620.getValue());
                break;
            case "CG 22 79":
                //Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 99471.

                break;
            case "CG 22 30":
                //Available when one of the following class codes are selected: 67513, 67512, 47474, or 47477.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_67513.getValue());
                    this.add(GLClassCode.GL_67512.getValue());
                    this.add(GLClassCode.GL_47474.getValue());
                    this.add(GLClassCode.GL_47477.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 21 57":
                //Available when one of the following class codes are selected: 48600 or 41650.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_48600.getValue());
                    this.add(GLClassCode.GL_41650.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 21 35":
                //ELECTIBLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2135);
                break;
            case "CG 22 94":
                //� Required when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if the following class codes are the only class code selected: 97047 or 97050. Agents cannot add/remove this endorsement. When this endorsement is not required, Underwriter can add/remove this endorsement. UW cannot remove the endorsement when it is attached as required.  If the coverage goes from being required to not being required, it should automatically uncheck the coverage
                break;
            case "CG 22 58":
                //ELECTIBLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2258);
                break;
            case "CG 21 53":
                //ELECTIBLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2153);
                break;
            case "IDCG 31 2002":
                //Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 94225, 96816, 97047, 97050, 99471, or 99975.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_94225.getValue());
                    this.add(GLClassCode.GL_96816.getValue());
                    this.add(GLClassCode.GL_97047.getValue());
                    this.add(GLClassCode.GL_97050.getValue());
                    this.add(GLClassCode.GL_99471.getValue());
                    this.add(GLClassCode.GL_99975.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 21 33":
                //ELECTIBLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2153);
                break;
            case "CG 21 16":
                //� This becomes required when one of the following class codes are selected: 41650, 41677, 91805, 47052, 58408, 58409, 58456, 58457, 58458, or 58459. Other than that this remains electable.
                //ELECTIBLE
                break;
            case "CG 21 34":
                //ELECTIBLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2134);
                break;
            case "CG 21 59":
                //Available when class code 46112 is selected.
                setExposure(location, GLClassCode.GL_46112.getValue());
                break;
            case "CG 22 43":
                //Available when one of the following class codes are selected: 92663 or 99471. However if CG 22 79 is on the policy then the endorsement (CG 22 43) is not availabile.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_92663.getValue());
                    this.add(GLClassCode.GL_99471.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 81":
                //Available when one of the following class codes are selected: 94225 or 16890.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_94225.getValue());
                    this.add(GLClassCode.GL_16890.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "IDCG 31 2001":
                //Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 91111, 91150, 91405, 91481, 91523, 94225, 96816, 97047, 97050, 98993, 99310, 99471, or 99975.
                break;
            case "CG 21 86":
                //Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 96816, 97047, or 97050.
                break;
            case "CG 22 50":
                //Available when one of the following class codes are selected: 13410, 92445, 97501, 97502, and/or 99943.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_13410.getValue());
                    this.add(GLClassCode.GL_92445.getValue());
                    this.add(GLClassCode.GL_97501.getValue());
                    this.add(GLClassCode.GL_97502.getValue());
                    this.add(GLClassCode.GL_99943.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 38":
                //Available when one of the following class codes are selected: 61223.
                //If class code 61227 or 61226 is selected and the question ""Does applicant/insured act in a fiduciary capacity?"" is answered yes automatically attach this endorsement."
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_61227.getValue());
                    this.add(GLClassCode.GL_61226.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 21 52":
                //Available when class code 61223 is selected.
                //If class code 61227 or 61226 is selected and the question ""Is applicant/insured involved in any of the following or related activities: Accounting, banking, credit card company, credit reporting, credit union, financial investment services, securities broker or dealer or tax preparation?"" is answered yes automatically attach this endorsement."
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_61227.getValue());
                    this.add(GLClassCode.GL_61226.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 21 56":
                //Available when one of the following class codes is selected: 41604, 41603, 41697, 41696, 43889, 46005, and/or 46004.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_41604.getValue());
                    this.add(GLClassCode.GL_41603.getValue());
                    this.add(GLClassCode.GL_41603.getValue());
                    this.add(GLClassCode.GL_41697.getValue());
                    this.add(GLClassCode.GL_41696.getValue());
                    this.add(GLClassCode.GL_43889.getValue());
                    this.add(GLClassCode.GL_46005.getValue());
                    this.add(GLClassCode.GL_46004.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 24":
                //Available when one of the following class codes are selected: 61223, or 96317.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_61223.getValue());
                    this.add(GLClassCode.GL_96317.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 48":
                //� This becomes required when one of the following class codes are selected: 61223 or 45334. Other than that this remains electable.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_61223.getValue());
                    this.add(GLClassCode.GL_45334.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 21 41":
                //Electable
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2141);
                break;
            case "CG 22 98":
                //Available when one of the following class codes are selected: 45334 and/or 47610.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_45334.getValue());
                    this.add(GLClassCode.GL_47610.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 53":
                //Available when one of the following class codes are selected: 14731, 19007, or 45678.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_14731.getValue());
                    this.add(GLClassCode.GL_19007.getValue());
                    this.add(GLClassCode.GL_45678.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 40":
                //ELECTIBLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2240);
                break;
            case "CG 21 17":
                //Available when question "Does applicant/insured move buildings or structures?" is answered yes.
                break;
            case "CG 21 36":
                //ELECTIBLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2136);
                break;
            case "CG 22 73":
                //� This becomes required when class code 99969 is selected. Other than that this remains electable.
                setExposure(location, GLClassCode.GL_99969.getValue());
                break;
            case "CG 21 38":
                //Available CG 22 96 is selected unless the this become required because of the defined by script. Then CG 22 96 would be come unavailable.
                break;
            case "CG 22 36":
                //Available when one of the following class codes are selected: 12375, 12374, or 45900.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_12375.getValue());
                    this.add(GLClassCode.GL_12374.getValue());
                    this.add(GLClassCode.GL_45900.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 37":
                //Available when one of the following class codes are selected: 13759 or 15839.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_13759.getValue());
                    this.add(GLClassCode.GL_15839.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 21 58":
                //Available when one of the following class codes are selected: 91200 or 99851.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_91200.getValue());
                    this.add(GLClassCode.GL_99851.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 29":
                //Available when class code 98751, 49763, and/or 18991 is on the policy.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_98751.getValue());
                    this.add(GLClassCode.GL_49763.getValue());
                    this.add(GLClassCode.GL_18991.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 23 01":
                //This becomes required when class code 47050 is selected.
                setExposure(location, GLClassCode.GL_47050.getValue());
                break;
            case "CG 22 46":
                //ELECTIBLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2246);
                break;
            case "CG 22 44":
                //This becomes required when one of the following class codes are selected: 40032, 40031, 43551, 66561.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_66561.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 45":
                //This becomes required when one of the following class codes are selected: 10113, 10115, 11128, 11127, 11234, 12356, 45190, 45192, 14655, 15600, 18912, 18911, 45191, or 45193.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_10113.getValue());
                    this.add(GLClassCode.GL_10115.getValue());
                    this.add(GLClassCode.GL_11128.getValue());
                    this.add(GLClassCode.GL_11127.getValue());
                    this.add(GLClassCode.GL_11234.getValue());
                    this.add(GLClassCode.GL_12356.getValue());
                    this.add(GLClassCode.GL_45190.getValue());
                    this.add(GLClassCode.GL_45192.getValue());
                    this.add(GLClassCode.GL_14655.getValue());
                    this.add(GLClassCode.GL_15600.getValue());
                    this.add(GLClassCode.GL_18912.getValue());
                    this.add(GLClassCode.GL_18911.getValue());
                    this.add(GLClassCode.GL_45191.getValue());
                    this.add(GLClassCode.GL_45193.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 91":
                //Available when class code 18575 and/or 99600 is selected.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_18575.getValue());
                    this.add(GLClassCode.GL_99600.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 33":
                //This becomes required when one of the following class codes are selected: 91135, 97003, or 97002.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_91135.getValue());
                    this.add(GLClassCode.GL_97003.getValue());
                    this.add(GLClassCode.GL_97002.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 57":
                //ELECTIBLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2257);
                break;
            case "CG 21 09":
                //Not available when CG 24 50 is selected.
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2109);
                break;
            case "CG 21 66":
                //ELECTIBLE
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2166);
                break;
            case "CG 21 60":
                //REQUIRED
                break;
            case "CG 21 84":
                //REQUIREED
                break;
            case "IDCG 31 2007":
                //Required when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy:  94225, 97047, 97050, and 96816.
                break;
            case "IDCG 31 2009":
                //This becomes required when class code 94225 is selected.
                setExposure(location, GLClassCode.GL_94225.getValue());
                break;
            case "IDCG 31 2010":
                //This becomes required when class code 12683 is selected.
                setExposure(location, GLClassCode.GL_12683.getValue());
                break;
            case "CG 21 67":
                //REQUIRED
                break;
            case "CG 22 60":
                //This is not available when class code 47052 is on the policy
                //This becomes required when class code 47050 is selected."
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_47050.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 96":
                //Not available when CG 21 38 is selected.
                //This becomes required when one of the following class codes are selected: 66123 or 66122."
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_66123.getValue());
                    this.add(GLClassCode.GL_66122.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 66":
                //This becomes required when one of the following class codes are selected: 10036, 12683, 13410 or 57001.
                tempList = new ArrayList<String>() {{
                    this.add(GLClassCode.GL_10036.getValue());
                    this.add(GLClassCode.GL_12683.getValue());
                    this.add(GLClassCode.GL_13410.getValue());
                    this.add(GLClassCode.GL_57001.getValue());
                }};
                setExposure(location, tempList.get(NumberUtils.generateRandomNumberInt(0, tempList.size() - 1)));
                break;
            case "CG 22 77":
                //This becomes required when class code 43151 is selected.
                setExposure(location, GLClassCode.GL_43151.getValue());
                break;
            case "CG 22 88":
                //This becomes required when one of the following class codes are selected: 41675.
                setExposure(location, GLClassCode.GL_41675.getValue());
                break;
            case "CG 22 90":
                //This becomes required when class code 18200 is selected.
                setExposure(location, GLClassCode.GL_18200.getValue());
                break;
            case "CG 22 99":
                //This becomes required when class code 96930 is selected.
                setExposure(location, GLClassCode.GL_96930.getValue());
                break;
            case "CG 22 70":
                //This becomes required when class code 47052 is selected.
                setExposure(location, GLClassCode.GL_47052.getValue());
                break;
            case "CG 21 96":
                //REQUIRED
                break;
            case "CG 21 49":
                //Not available if CG 21 55 is selected.
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2149);
                break;
            case "CG 21 55":
                //Not available if CG 21 49 is selected.
                coveragePage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragePage.clickAdditionalCoverages();
                uwQuestions.setGLCoveragesUWQuestion(GeneralLiabilityCoverages.CG2155);
                break;
        }
    }

    @SuppressWarnings("unused")
    private void setConflictingCoverages(PolicyLocation location, String coverage) throws Exception {
        List<String> tempList = new ArrayList<String>();

        switch (coverage) {
            case "ISO developed CG 24 16 Canoes or Rowboats to be used in conjunction with Hotels and Motels. Please fully describe activities involved for consideration.":
                //Business rule If class code 10110 is not used in conjunction with motel classifications 45190, 45191, 45192, 45193, 64074, or 64075 display the message.
                setExposure(myPolicyObj.commercialPackage.locationList.get(0), GLClassCode.GL_99917.getValue());
                break;
        }
    }


    /**
     * @param uwIssues
     * @param classCode
     * @Author jlarsen
     * @Description Generate Failure Message
     * @DATE Sep 9, 2016
     */
    private void testFailedToGenerate(GLUWIssues uwIssues, String classCode) {
        softAssert.fail(uwIssues.getRuleMessage() + "UW Issue Failed to generate!! With ClassCode: " + classCode);
        System.out.println(uwIssues.getRuleMessage() + " UW Issue Failed to generate!! With ClassCode: " + classCode);
    }

    /**
     * @param uwIssues
     * @param classCode
     * @Author jlarsen
     * @Description Generate Failure Message
     * @DATE Sep 9, 2016
     */
    private void testFailedToRemove(GLUWIssues uwIssues, String classCode) {
        softAssert.fail(uwIssues.getRuleMessage() + " UW Issue Was Not still approved or something!! With ClassCode: " + classCode);
        System.out.println(uwIssues.getRuleMessage() + " UW Issue Was Not still approved or something!! With ClassCode: " + classCode);
    }

    /**
     * @param uwIssues
     * @param classCode
     * @Author jlarsen
     * @Description Generate Failure Message
     * @DATE Sep 9, 2016
     */
    private void testFailedToRefresh(GLUWIssues uwIssues, String classCode) {
        softAssert.fail(uwIssues.getRuleMessage() + "\nUW Issue Failed to Refresh! Sad!! With ClassCode: " + classCode);
        System.out.println(uwIssues.getRuleMessage() + " UW Issue Failed to regenerate! Sad!! With ClassCode: " + classCode);
    }


}
















