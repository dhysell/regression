package regression.r3.noclock.policycenter.generalliability;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInsuredTypeGL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.ExistanceType;
import repository.gw.enums.GLClassCode;
import repository.gw.enums.GeneralLiability.StandardCoverages.CG0001_OccuranceLimit;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPGeneralLiability;
import repository.gw.generate.custom.CPPGeneralLiabilityCoverages;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
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
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoverages_StandardCoverages;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.GLClassCodeHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

@QuarantineClass
public class GLExclusionsAndConditions extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    List<String> classCodes = new ArrayList<String>();
    boolean testFailed = false;
    String failureString = "";
    private WebDriver driver;

    @Test//(enabled=false)
    public void generatePolicy() throws Exception {

        // LOCATIONS
        ArrayList<PolicyLocationBuilding> builidngList = new ArrayList<PolicyLocationBuilding>();
        builidngList.add(new PolicyLocationBuilding()); // END BUILDING

        PolicyLocation policyLocation = new PolicyLocation(new AddressInfo(true), false);
        policyLocation.setBuildingList(builidngList);

        ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>();
        locationsLists.add(policyLocation);


        ArrayList<CPPGeneralLiabilityExposures> exposures = new ArrayList<CPPGeneralLiabilityExposures>();
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), GLClassCode.GL_10026.getValue()));

        CPPGeneralLiability generalLiability = new CPPGeneralLiability();
        generalLiability.setCPPGeneralLiabilityExposures(exposures);
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
                .withInsCompanyName("GL E&C")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsLists.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .isDraft()
                .build((new GuidewireHelpers(driver).getRandBoolean()) ? GeneratePolicyType.QuickQuote : GeneratePolicyType.FullApp);

        System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentUserName());

    }


    @SuppressWarnings("serial")
    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void testExclusionsAndConditions() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        login.loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        guidewireHelpers.editPolicyTransaction();

        //49333
        //Amendment - Travel Agency Tours (Limitation Of Coverage) CG 22 28
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_49333.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Amendment - Travel Agency Tours (Limitation Of Coverage) CG 22 28", ExistanceType.Required);


        //Boats CG 24 12
        //� Required when class codes 10117, 40115, 40140, 40117, or 43760 is selected.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_10117.getValue());
            this.add(GLClassCode.GL_40115.getValue());
            this.add(GLClassCode.GL_40140.getValue());
            this.add(GLClassCode.GL_40117.getValue());
            this.add(GLClassCode.GL_43760.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Boats CG 24 12", ExistanceType.Required);
        GenericWorkorderGeneralLiabilityCoveragesCPP coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions exclusionsConditions = new GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions(driver);
        GenericWorkorderGeneralLiabilityCoverages_AdditionalCoverages additionalCoverages = new GenericWorkorderGeneralLiabilityCoverages_AdditionalCoverages(driver);
        exclusionsConditions.addBoat();

        //Canoes Or Rowboats CG 24 16
        //� Available when one of the following class codes are selected: 41668, 41667, 45190, 45192, 64074, 10110, 40111, 41422, 45191, 45193, or 64075.
        classCodes = new ArrayList<String>();
        classCodes.add(GLClassCode.GL_41668.getValue());
        classCodes.add(GLClassCode.GL_41667.getValue());
        classCodes.add(GLClassCode.GL_45190.getValue());
        classCodes.add(GLClassCode.GL_45192.getValue());
        classCodes.add(GLClassCode.GL_64074.getValue());
        classCodes.add(GLClassCode.GL_10110.getValue());
        classCodes.add(GLClassCode.GL_40111.getValue());
        classCodes.add(GLClassCode.GL_41422.getValue());
        classCodes.add(GLClassCode.GL_41422.getValue());
        classCodes.add(GLClassCode.GL_45191.getValue());
        classCodes.add(GLClassCode.GL_45193.getValue());
        classCodes.add(GLClassCode.GL_64075.getValue());

        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Canoes Or Rowboats CG 24 16", ExistanceType.Required);

        //Operation Of Customers Autos On Particular Premises CG 22 68
        //� Available when one of the following class codes are selected: 10072, 10073, 10367, 13453, 13455, or 18616.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_10072.getValue());
            this.add(GLClassCode.GL_10073.getValue());
            this.add(GLClassCode.GL_10367.getValue());
            this.add(GLClassCode.GL_13453.getValue());
            this.add(GLClassCode.GL_13455.getValue());
            this.add(GLClassCode.GL_18616.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Operation Of Customers Autos On Particular Premises CG 22 68", ExistanceType.Required);

        //Products/Completed Operations Hazard Redefined CG 24 07
        //� Available when one of the following class codes are selected: 14401, 16820, 16819, 16900, 16901, 16902, 16905, 16906, 16910, 16911, 16915, 16916, 16930, 16931, and/or 16941.
        classCodes = new ArrayList<String>() {{
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
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        GenericWorkorderGeneralLiabilityCoveragesCPP glcoverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        glcoverages.fillOutGeneralLiabilityCoverages(myPolicyObj);
        verifyElectability("Products/Completed Operations Hazard Redefined CG 24 07", ExistanceType.Required);

        //Snow Plow Operations Coverage CG 22 92
        //� Available when class code 99310 is selected.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_99310.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Snow Plow Operations Coverage CG 22 92", ExistanceType.Required);


        //Amendment Of Liquor Liability Exclusion CG 21 50
        //� Available when one of the following class codes are selected: 16905 or 16906.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_16905.getValue());
            this.add(GLClassCode.GL_16906.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Amendment Of Liquor Liability Exclusion CG 21 50", ExistanceType.Required);

        //Employment-Related Practices Exclusion CG 21 47
        //� Available when Employement Practices Liability Insurance IDCG 31 2013 is not selected.
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuGLCoverages();
        coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        coverages.clickAdditionalCoverages();

        if (guidewireHelpers.isSuggested("Employment Practices Liability Insurance IDCG 31 2013")) {
            coverages.clickExclusionsAndConditions();
            if (guidewireHelpers.isElectable("Employment-Related Practices Exclusion CG 21 47") || guidewireHelpers.isSuggested("Employment-Related Practices Exclusion CG 21 47") || guidewireHelpers.isRequired("Employment-Related Practices Exclusion CG 21 47")) {
                testFailed = true;
                failureString += "Exclusion or Condition Employment-Related Practices Exclusion CG 21 47 was on the policy when it shouldn't have been \n";
                System.out.println("Exclusion or Condition Employment-Related Practices Exclusion CG 21 47 was on the policy when it shouldn't have been \n");
            }
            coverages.clickAdditionalCoverages();
            additionalCoverages.checkEmploymentPracticesLiabilityInsuranceIDCG312013(false);
            coverages.clickExclusionsAndConditions();
            if (!guidewireHelpers.isRequired("Employment-Related Practices Exclusion CG 21 47")) {
                testFailed = true;
                failureString += "Exclusion or Condition Employment-Related Practices Exclusion CG 21 47 was NOT Required when EPLI was Un-Selected \n";
                System.out.println("Exclusion or Condition Employment-Related Practices Exclusion CG 21 47 was NOT Required when EPLI was Un-Selected \n");
            }
        } else if (guidewireHelpers.isElectable("Employment Practices Liability Insurance IDCG 31 2013")) {
            coverages.clickExclusionsAndConditions();
            if (!guidewireHelpers.isRequired("Employment-Related Practices Exclusion CG 21 47")) {
                testFailed = true;
                failureString += "Exclusion or Condition Employment-Related Practices Exclusion CG 21 47 was NOT Required when EPLI was Un-Selected \n";
                System.out.println("Exclusion or Condition Employment-Related Practices Exclusion CG 21 47 was NOT Required when EPLI was Un-Selected \n");
            }
            coverages.clickAdditionalCoverages();
            additionalCoverages.checkEmploymentPracticesLiabilityInsuranceIDCG312013(true);
            coverages.clickExclusionsAndConditions();
            if (guidewireHelpers.isElectable("Employment-Related Practices Exclusion CG 21 47") || guidewireHelpers.isSuggested("Employment-Related Practices Exclusion CG 21 47") || guidewireHelpers.isRequired("Employment-Related Practices Exclusion CG 21 47")) {
                testFailed = true;
                failureString += "Exclusion or Condition Employment-Related Practices Exclusion CG 21 47 was on the policy when it shouldn't have been \n";
                System.out.println("Exclusion or Condition Employment-Related Practices Exclusion CG 21 47 was on the policy when it shouldn't have been \n");
            }
        }

        coverages.clickAdditionalCoverages();
        additionalCoverages.checkEmploymentPracticesLiabilityInsuranceIDCG312013(true);
        CPPGeneralLiabilityCoverages glcoverages1 = myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages();
        additionalCoverages.setEmploymentPracticesLiabilityInsuranceIDCG312013(glcoverages1.getNumberFullTimeEmployees(), glcoverages1.getNumberPartTimeEmployees(), glcoverages1.getAggragateLimit(), glcoverages1.getEmploymentPracticesLiabilityInsuranceDeductible(), false, "200", glcoverages1.getEmploymentPracticesLiabilityInsuranceNumberLocations());


        //Endorsement Excluding Bodily Injury Claim By Your Employee Against An Additional Insured IDCG 31 2008
        //� Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 99471.
        //COVERED IN GLExclusionsAndConditionsContractors()

        //Exclusion - Athletic Or Sports Participants CG 21 01
        //� Available when one of the following class codes are selected: 63218, 63217, 63220, 63219, 43421, or 43424.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_63218.getValue());
            this.add(GLClassCode.GL_63217.getValue());
            this.add(GLClassCode.GL_63220.getValue());
            this.add(GLClassCode.GL_63219.getValue());
            this.add(GLClassCode.GL_43421.getValue());
            this.add(GLClassCode.GL_43424.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Athletic Or Sports Participants CG 21 01", ExistanceType.Required);

        //Exclusion - Camps Or Campgrounds CG 22 39
        //� Available when one of the following class codes are selected: 10332, 10331, or 41422.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_10332.getValue());
            this.add(GLClassCode.GL_10331.getValue());
            this.add(GLClassCode.GL_41422.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Camps Or Campgrounds CG 22 39", ExistanceType.Required);

        //Exclusion - Construction Management Errors And Omissions CG 22 34
        //� Available when class code 41620 is selected.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_41620.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Construction Management Errors And Omissions CG 22 34", ExistanceType.Required);

        //Exclusion - Contractors - Professional Liability CG 22 79
        //� Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 99471.
        //COVERED IN GLExclusionsAndConditionsContractors()

        //Exclusion - Corporal Punishment CG 22 30
        //� Available when one of the following class codes are selected: 67513, 67512, 47474, or 47477.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_67513.getValue());
            this.add(GLClassCode.GL_67512.getValue());
            this.add(GLClassCode.GL_47474.getValue());
            this.add(GLClassCode.GL_47477.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Corporal Punishment CG 22 30", ExistanceType.Required);

        //Exclusion - Counseling Services CG 21 57
        //� Available when one of the following class codes are selected: 48600 or 41650.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_48600.getValue());
            this.add(GLClassCode.GL_41650.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Counseling Services CG 21 57", ExistanceType.Required);

        //Exclusion - Designated Operations Covered By A Consolidated (Wrap-Up) Insurance Program IDCG 31 2002
        //� Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 94225, 96816, 97047, 97050, 99471, or 99975.
        //COVERED IN GLExclusionsAndConditionsContractors()

        //Exclusion - Diagnostic Testing Laboratories CG 21 59
        //� Available when class code 46112 is selected.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_46112.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Diagnostic Testing Laboratories CG 21 59", ExistanceType.Required);

        //Exclusion - Engineers, Architects Or Surveyors Professional Liability CG 22 43
        //� Available when one of the following class codes are selected: 92663 or 99471. However if CG 22 79 is on the policy then the endorsement (CG 22 43) is not availabile.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_92663.getValue());
            this.add(GLClassCode.GL_99471.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Engineers, Architects Or Surveyors Professional Liability CG 22 43", ExistanceType.Required);

        //Exclusion - Erroneous Delivery Or Mixture And Resulting Failure Of Seed To Germinate - Seed Merchants CG 22 81
        //� Available when one of the following class codes are selected: 94225 or 16890.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_94225.getValue());
            this.add(GLClassCode.GL_16890.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Erroneous Delivery Or Mixture And Resulting Failure Of Seed To Germinate - Seed Merchants CG 22 81", ExistanceType.Required);

        //Exclusion - Explosion, Collapse And Underground Property Damage Hazard (Specified Operations) IDCG 31 2001
        //� Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 91111, 91150, 91405, 91481, 91523, 94225, 96816, 97047, 97050, 98993, 99310, 99471, or 99975.
        //COVERED IN GLExclusionsAndConditionsContractors()

        //Exclusion - Exterior Insulation and Finish Systems CG 21 86
        //� Available when Package Risk Type is Contractors and the Class Code starts with the Number 9 except if these class codes are the only ones on the policy: 96816, 97047, or 97050.
        //COVERED IN GLExclusionsAndConditionsContractors()

        //Exclusion - Failure To Supply CG 22 50
        //� Available when one of the following class codes are selected: 13410, 92445, 97501, 97502, and/or 99943.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_13410.getValue());
            this.add(GLClassCode.GL_92445.getValue());
            this.add(GLClassCode.GL_97501.getValue());
            this.add(GLClassCode.GL_97502.getValue());
            this.add(GLClassCode.GL_99943.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Failure To Supply CG 22 50", ExistanceType.Required);


        //Exclusion - Funeral Services CG 21 56
        //� Available when one of the following class codes is selected: 41604, 41603, 41697, 41696, 43889, 46005, and/or 46004.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_41604.getValue());
            this.add(GLClassCode.GL_41603.getValue());
            this.add(GLClassCode.GL_41697.getValue());
            this.add(GLClassCode.GL_41696.getValue());
            this.add(GLClassCode.GL_43889.getValue());
            this.add(GLClassCode.GL_46005.getValue());
            this.add(GLClassCode.GL_46004.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Funeral Services CG 21 56", ExistanceType.Required);

        //Exclusion - Inspection, Appraisal And Survey Companies CG 22 24
        //� Available when one of the following class codes are selected: 61223, or 96317.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_61223.getValue());
            this.add(GLClassCode.GL_96317.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Inspection, Appraisal And Survey Companies CG 22 24", ExistanceType.Required);

        //Exclusion - Internet Service Providers And Internet Access Providers Errors And Omissions CG 22 98
        //� Available when one of the following class codes are selected: 45334 and/or 47610.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_45334.getValue());
            this.add(GLClassCode.GL_47610.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Internet Service Providers And Internet Access Providers Errors And Omissions CG 22 98", ExistanceType.Required);

        //Exclusion - Laundry And Dry Cleaning Damage CG 22 53
        //� Available when one of the following class codes are selected: 14731, 19007, or 45678.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_14731.getValue());
            this.add(GLClassCode.GL_19007.getValue());
            this.add(GLClassCode.GL_45678.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Laundry And Dry Cleaning Damage CG 22 53", ExistanceType.Required);

        //Exclusion - Products And Professional Services (Druggists) CG 22 36
        //� Available when one of the following class codes are selected: 12375, 12374, or 45900.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_12375.getValue());
            this.add(GLClassCode.GL_12374.getValue());
            this.add(GLClassCode.GL_45900.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Products And Professional Services (Druggists) CG 22 36", ExistanceType.Required);

        //Exclusion - Products And Professional Services (Optical And Hearing Aid Establishments) CG 22 37
        //� Available when one of the following class codes are selected: 13759 or 15839.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_13759.getValue());
            this.add(GLClassCode.GL_15839.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Products And Professional Services (Optical And Hearing Aid Establishments) CG 22 37", ExistanceType.Required);


        //Exclusion - Products-Completed Operations Hazard CG 21 04
        //� Available when the class premium base does not include (+) and Exclude is chosen on CG 00 01 - Products / Completed Operations Aggregate Limit (no premium charge is made) attach this form as required.
        //NEEDS TO BE MADE TO CHOOSE A RANDOM CLASS CODE WHEN CLASS CODE TABLE IS UPDATED
        //class codes with +'s
        setGLExposure(GLClassCodeHelper.getRandomPlusGLPremiumBaseClassCode().getCode());
        sideMenu.clickSideMenuGLCoverages();
        coverages.clickExclusionsAndConditions();
        if (guidewireHelpers.isElectable("Exclusion - Products-Completed Operations Hazard CG 21 04") || guidewireHelpers.isSuggested("Exclusion - Products-Completed Operations Hazard CG 21 04") || guidewireHelpers.isRequired("Exclusion - Products-Completed Operations Hazard CG 21 04")) {
            testFailed = true;
            failureString += "Exclusion or Condition Exclusion - Products-Completed Operations Hazard CG 21 04 was on the policy when it shouldn't have been \n";
            System.out.println("Exclusion or Condition Exclusion - Products-Completed Operations Hazard CG 21 04 was on the policy when it shouldn't have been \n");
        }
        coverages.clickExclusionsAndConditions();
        if (guidewireHelpers.isElectable("Exclusion - Products-Completed Operations Hazard CG 21 04") || guidewireHelpers.isSuggested("Exclusion - Products-Completed Operations Hazard CG 21 04") || guidewireHelpers.isRequired("Exclusion - Products-Completed Operations Hazard CG 21 04")) {
            testFailed = true;
            failureString += "Exclusion or Condition Exclusion - Products-Completed Operations Hazard CG 21 04 was on the policy when it shouldn't have been \n";
            System.out.println("Exclusion or Condition Exclusion - Products-Completed Operations Hazard CG 21 04 was on the policy when it shouldn't have been \n");
        }


        //class codes without +'s
        setGLExposure(GLClassCodeHelper.getRandomNon_PlusGLPremiumBaseClassCode().getCode());
        sideMenu.clickSideMenuGLCoverages();
        coverages.clickStandardCoverages();
        coverages.clickExclusionsAndConditions();
        verifyElectability("Exclusion - Products-Completed Operations Hazard CG 21 04", ExistanceType.Required);
        coverages.clickStandardCoverages();
        GenericWorkorderGeneralLiabilityCoverages_StandardCoverages standardCoverages = new GenericWorkorderGeneralLiabilityCoverages_StandardCoverages(driver);
        standardCoverages.selectOccuranceLimit(CG0001_OccuranceLimit.OneHundredThousand);
        coverages.clickExclusionsAndConditions();
        if (guidewireHelpers.isElectable("Exclusion - Products-Completed Operations Hazard CG 21 04") || guidewireHelpers.isSuggested("Exclusion - Products-Completed Operations Hazard CG 21 04") || guidewireHelpers.isRequired("Exclusion - Products-Completed Operations Hazard CG 21 04")) {
            testFailed = true;
            failureString += "Exclusion or Condition Exclusion - Products-Completed Operations Hazard CG 21 04 was on the policy when it shouldn't have been \n";
            System.out.println("Exclusion or Condition Exclusion - Products-Completed Operations Hazard CG 21 04 was on the policy when it shouldn't have been \n");
        }


        //Exclusion - Professional Veterinarian Services CG 21 58
        //� Available when one of the following class codes are selected: 15839 or 99851.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_91200.getValue());
            this.add(GLClassCode.GL_99851.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Professional Veterinarian Services CG 21 58", ExistanceType.Required);

        //Exclusion - Property Entrusted CG 22 29
        //� Available when class code 98751, 49763, and/or 18991 is on the policy.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_98751.getValue());
            this.add(GLClassCode.GL_49763.getValue());
            this.add(GLClassCode.GL_18991.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Property Entrusted CG 22 29", ExistanceType.Required);

        //Exclusion - Real Estate Agents Or Brokers Errors Or Omissions CG 23 01
        //� This becomes required when class code 47050 is selected.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_47050.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Real Estate Agents Or Brokers Errors Or Omissions CG 23 01", ExistanceType.Required);

        //Exclusion - Services Furnished By Health Care Providers CG 22 44
        //jlarsen 10/18/2016
        //� This becomes required when one of the following class codes are selected: 40032, 40031, 43551, 66561.
        //� This becomes required when one of the following class codes are selected: 40032, 43551, 66561.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_66561.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Services Furnished By Health Care Providers CG 22 44", ExistanceType.Required);

        //Exclusion - Specified Therapeutic Or Cosmetic Services CG 22 45
        //� This becomes required when one of the following class codes are selected: 10113, 10115, 11128, 11127, 11234, 12356, 45190, 45192, 14655, 15600, 18912, 18911, 45191, or 45193.
        classCodes = new ArrayList<String>() {{
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
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Specified Therapeutic Or Cosmetic Services CG 22 45", ExistanceType.Required);


        //		Exclusion - Designated Professional Services CG 21 16
        //		� This becomes required when one of the following class codes are selected: 41650, 41677, 91805, 47052, 58408, 58409, 58456, 58457, 58458, or 58459. Other than that this remains electable.
        verifyElectability("Exclusion - Designated Professional Services CG 21 16", ExistanceType.Electable);
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_41650.getValue());
            this.add(GLClassCode.GL_41677.getValue());
            this.add(GLClassCode.GL_91805.getValue());
            this.add(GLClassCode.GL_47052.getValue());
            this.add(GLClassCode.GL_58408.getValue());
            this.add(GLClassCode.GL_58409.getValue());
            this.add(GLClassCode.GL_58456.getValue());
            this.add(GLClassCode.GL_58457.getValue());
            this.add(GLClassCode.GL_58458.getValue());
            this.add(GLClassCode.GL_58459.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Specified Therapeutic Or Cosmetic Services CG 22 45", ExistanceType.Required);


        //		Exclusion - Fiduciary Or Representative Liability Of Financial Institutions CG 22 38
        //		� Available when one of the following class codes are selected: 61223.
        //		� If class code 61227 or 61226 is selected and the question ""Does applicant/insured act in a fiduciary capacity?"" is answered yes automatically attach this endorsement."
        setGLExposure(GLClassCode.GL_61223.getValue());
        verifyElectability("Exclusion - Fiduciary Or Representative Liability Of Financial Institutions CG 22 38", ExistanceType.Required);


        //		Exclusion - Financial Services CG 21 52
        //		� Available when class code 61223 is selected.
        //		� If class code 61227 or 61226 is selected and the question ""Is applicant/insured involved in any of the following or related activities: Accounting, banking, credit card company, credit reporting, credit union, financial investment services, securities broker or dealer or tax preparation?"" is answered yes automatically attach this endorsement."
        setGLExposure(GLClassCode.GL_61223.getValue());
        verifyElectability("Exclusion - Financial Services CG 21 52", ExistanceType.Required);


        //		Exclusion - Insurance And Related Operations CG 22 48
        //		� This becomes required when one of the following class codes are selected: 61223 or 45334. Other than that this remains electable.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_61223.getValue());
            this.add(GLClassCode.GL_45334.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Insurance And Related Operations CG 22 48", ExistanceType.Required);


        //		Exclusion - Oil Or Gas Producing Operations CG 22 73
        //		� This becomes required when class code 99969 is selected. Other than that this remains electable.
        verifyElectability("Exclusion - Oil Or Gas Producing Operations CG 22 73", ExistanceType.Electable);
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_99969.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Oil Or Gas Producing Operations CG 22 73", ExistanceType.Required);


        //		Exclusion - Personal And Advertising Injury CG 21 38
        //		� Available CG 22 96 is selected unless the this become required because of the defined by script. Then CG 22 96 would be come unavailable.
        verifyElectability("Exclusion - Personal And Advertising Injury CG 21 38", ExistanceType.Electable);
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_91130.getValue());
            this.add(GLClassCode.GL_91636.getValue());
            this.add(GLClassCode.GL_43200.getValue());
            this.add(GLClassCode.GL_65007.getValue());
            this.add(GLClassCode.GL_66123.getValue());
            this.add(GLClassCode.GL_66122.getValue());
            this.add(GLClassCode.GL_46822.getValue());
            this.add(GLClassCode.GL_46882.getValue());
            this.add(GLClassCode.GL_46881.getValue());
            this.add(GLClassCode.GL_47052.getValue());
            this.add(GLClassCode.GL_98751.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Personal And Advertising Injury CG 21 38", ExistanceType.Required);

        //		Exclusion - Telecommunication Equipment Or Service Providers Errors And Omissions CG 22 91
        //		� Available when class code 18575 and/or 99600 is selected.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_18575.getValue());
            this.add(GLClassCode.GL_99600.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Telecommunication Equipment Or Service Providers Errors And Omissions CG 22 91", ExistanceType.Required);

        //		Exclusion - Testing Or Consulting Errors And Omissions CG 22 33
        //		� This becomes required when one of the following class codes are selected: 91135, 97003, or 97002.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_91135.getValue());
            this.add(GLClassCode.GL_97003.getValue());
            this.add(GLClassCode.GL_97002.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Exclusion - Testing Or Consulting Errors And Omissions CG 22 33", ExistanceType.Required);


        //		Exclusion - Unmanned Aircraft CG 21 09
        //		� Not available when CG 24 50 is selected.


        //		Farm Machinery Operations By Contractors Exclusion Endorsement IDCG 31 2009
        //		� This becomes required when class code 94225 is selected.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_94225.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Farm Machinery Operations By Contractors Exclusion Endorsement IDCG 31 2009", ExistanceType.Required);


        //		Fertilizer Distributors And Dealers Exclusion Endorsement IDCG 31 2010
        //		� This becomes required when class code 12683 is selected.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_12683.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Fertilizer Distributors And Dealers Exclusion Endorsement IDCG 31 2010", ExistanceType.Required);


        //		Limitation Of Coverage - Real Estate Operations CG 22 60
        //		"� This is not available when class code 47052 is on the policy 
        //		� This becomes required when class code 47050 is selected."
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_47050.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Limitation Of Coverage - Real Estate Operations CG 22 60", ExistanceType.Required);


        //		Limited Exclusion - Personal And Advertising Injury -Lawyers CG 22 96
        //		"� Not available when CG 21 38 is selected.
        //		� This becomes required when one of the following class codes are selected: 66123 or 66122."
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_66123.getValue());
            this.add(GLClassCode.GL_66122.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Limited Exclusion - Personal And Advertising Injury -Lawyers CG 22 96", ExistanceType.Required);


        //		Misdelivery Of Liquid Products Coverage CG 22 66
        //		� This becomes required when one of the following class codes are selected: 10036, 12683, 13410 or 57001.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_10036.getValue());
            this.add(GLClassCode.GL_12683.getValue());
            this.add(GLClassCode.GL_13410.getValue());
            this.add(GLClassCode.GL_57001.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Misdelivery Of Liquid Products Coverage CG 22 66", ExistanceType.Required);


        //		Professional Liability Exclusion - Computer Data Processing CG 22 77
        //		� This becomes required when class code 43151 is selected.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_43151.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Professional Liability Exclusion - Computer Data Processing CG 22 77", ExistanceType.Required);


        //		Professional Liability Exclusion - Electronic Data Processing Services And Computer Consulting Or Programming Services CG 22 88
        //		� This becomes required when one of the following class codes are selected: 41675.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_41675.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Professional Liability Exclusion - Electronic Data Processing Services And Computer Consulting Or Programming Services CG 22 88", ExistanceType.Required);


        //		Professional Liability Exclusion - Spas Or Personal Enhancement Facilities CG 22 90
        //		� This becomes required when class code 18200 is selected.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_18200.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Professional Liability Exclusion - Spas Or Personal Enhancement Facilities CG 22 90", ExistanceType.Required);


        //		Professional Liability Exclusion - Web Site Designers CG 22 99
        //		� This becomes required when class code 96930 is selected.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_96930.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Professional Liability Exclusion - Web Site Designers CG 22 99", ExistanceType.Required);

        //		Real Estate Property Managed CG 22 70
        //		� This becomes required when class code 47052 is selected.
        classCodes = new ArrayList<String>() {{
            this.add(GLClassCode.GL_47052.getValue());
        }};
        setGLExposure(classCodes.get(NumberUtils.generateRandomNumberInt(0, classCodes.size() - 1)));
        verifyElectability("Real Estate Property Managed CG 22 70", ExistanceType.Required);


        //		Total Pollution Exclusion Endorsement CG 21 49
        //		� Not available if CG 21 55 is selected.


        //		Total Pollution Exclusion with A Hostile Fire Exception CG 21 55
        //		� Not available if CG 21 49 is selected.


        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + failureString);
        }
    }


    @Test(dependsOnMethods = {"generatePolicy"})
    public void testRequiredExclusionsAndConditions() throws Exception {
        testFailed = false;
        failureString = "";

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuGLCoverages();

        GenericWorkorderGeneralLiabilityCoveragesCPP coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        coverages.clickExclusionsAndConditions();

        //Affiliate And Subsidiary Definition Endorsement IDCG 31 2004
        verifyElectability("Affiliate And Subsidiary Definition Endorsement IDCG 31 2004", ExistanceType.Required);

        //Amendment Of Insured Contract Definition CG 24 26
        verifyElectability("Amendment Of Insured Contract Definition CG 24 26", ExistanceType.Required);

        //Excess Provision - Vendors CG 24 10
        verifyElectability("Excess Provision - Vendors CG 24 10", ExistanceType.Electable);

        //Limitation Of Coverage To Designated Premises, Project Or Operation CG 21 44
        verifyElectability("Limitation Of Coverage To Designated Premises, Project Or Operation CG 21 44", ExistanceType.Electable);
        //HAS TERMS

        //Limitation Of Coverage To Insured Premises CG 28 06
        verifyElectability("Limitation Of Coverage To Insured Premises CG 28 06", ExistanceType.Electable);
        //HAS SCHEDULED ITEMS

        //Mobile Equipment Modification Endorsement IDCG 31 2003
        verifyElectability("Mobile Equipment Modification Endorsement IDCG 31 2003", ExistanceType.Required);

        //Pollutants Definition Endorsement IDCG 31 2005
        verifyElectability("Pollutants Definition Endorsement IDCG 31 2005", ExistanceType.Required);

        //Abuse Or Molestation Exclusion CG 21 46
        verifyElectability("Abuse Or Molestation Exclusion CG 21 46", ExistanceType.Required);

        //Communicable Disease Exclusion CG 21 32
        verifyElectability("Communicable Disease Exclusion CG 21 32", ExistanceType.Electable);

        //Exclusion - Access Or Disclosure Of Confidential Or Personal Information And Data-Related Liability - Limited Bodily Injury Exception Not Included CG 21 07
        verifyElectability("Exclusion - Access Or Disclosure Of Confidential Or Personal Information And Data-Related Liability - Limited Bodily Injury Exception Not Included CG 21 07", ExistanceType.Required);

        //Exclusion - Adult Day Care Centers CG 22 87
        verifyElectability("Exclusion - Adult Day Care Centers CG 22 87", ExistanceType.Electable);

        //Exclusion - All Hazards In Connection With Designated Premises CG 21 00
        verifyElectability("Exclusion - All Hazards In Connection With Designated Premises CG 21 00", ExistanceType.Electable);
        //HAS SCHEDULED ITEMS

        //Exclusion - Coverage C - Medical Payments CG 21 35
        verifyElectability("Exclusion - Coverage C - Medical Payments CG 21 35", ExistanceType.Electable);
        //HAS TERMS

        //Exclusion - Described Hazards (Carnivals, Circuses And Fairs) CG 22 58
        verifyElectability("Exclusion - Described Hazards (Carnivals, Circuses And Fairs) CG 22 58", ExistanceType.Electable);

        //Exclusion - Designated Ongoing Operations CG 21 53
        verifyElectability("Exclusion - Designated Ongoing Operations CG 21 53", ExistanceType.Electable);
        //HAS TERMS

        //Exclusion - Designated Products CG 21 33
        verifyElectability("Exclusion - Designated Products CG 21 33", ExistanceType.Electable);
        //HAS TERMS

        //Exclusion - Designated Work CG 21 34
        verifyElectability("Exclusion - Designated Work CG 21 34", ExistanceType.Electable);
        //HAS TERMS

        //Exclusion - Intercompany Products Suits CG 21 41
        verifyElectability("Exclusion - Intercompany Products Suits CG 21 41", ExistanceType.Electable);

        //Exclusion - Medical Payments To Children Day Care Centers CG 22 40
        verifyElectability("Exclusion - Medical Payments To Children Day Care Centers CG 22 40", ExistanceType.Electable);

        //Exclusion - Rolling Stock - Railroad Construction CG 22 46
        verifyElectability("Exclusion - Rolling Stock - Railroad Construction CG 22 46", ExistanceType.Electable);

        //Exclusion - Underground Resources And Equipment CG 22 57
        verifyElectability("Exclusion - Underground Resources And Equipment CG 22 57", ExistanceType.Electable);

        //Exclusion - Volunteer Workers CG 21 66
        verifyElectability("Exclusion - Volunteer Workers CG 21 66", ExistanceType.Electable);

        //Exclusion - Year 2000 Computer-Related And Other Electronic Problems CG 21 60
        verifyElectability("Exclusion - Year 2000 Computer-Related And Other Electronic Problems CG 21 60", ExistanceType.Required);

        //Exclusion Of Certified Nuclear, Biological, Chemical Or Radiological Acts Of Terrorism; Cap On Covered Certified Acts Losses From Certified Acts Of Terrorism CG 21 84
        verifyElectability("Exclusion Of Certified Nuclear, Biological, Chemical Or Radiological Acts Of Terrorism; Cap On Covered Certified Acts Losses From Certified Acts Of Terrorism CG 21 84", ExistanceType.Required);

        //Fungi Or Bacteria Exclusion CG 21 67
        verifyElectability("Fungi Or Bacteria Exclusion CG 21 67", ExistanceType.Required);

        //Silica or Silica-Related Dust Exclusion CG 21 96
        verifyElectability("Silica or Silica-Related Dust Exclusion CG 21 96", ExistanceType.Required);

        //		Exclusion - New Entities CG 21 36
        verifyElectability("Exclusion - New Entities CG 21 36", ExistanceType.Electable);


        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + failureString);
        }
    }


    @Test(dependsOnMethods = {"generatePolicy"}, enabled = false)
    public void testUnderWriterOnlyExclusionsAndConditions() throws Exception {


        testFailed = false;
        failureString = "";

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Underwriters underwriter = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Commercial, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(underwriter.getUnderwriterUserName(), underwriter.getUnderwriterPassword(), myPolicyObj.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuGLCoverages();

        GenericWorkorderGeneralLiabilityCoveragesCPP coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        coverages.clickExclusionsAndConditions();

        //		Commercial General Liability Manuscript Endorsement IDCG 31 2012
        //		� This is only available for underwriters to select. The agent can see the endorsement and what the underwriter has entered in however they cannot edit it or select it.


        //		Commercial General Liability Manuscript Endorsement IDCG 31 2012
        //		� This is only available for underwriters to select. The agent can see the endorsement and what the underwriter has entered in however they cannot edit it or select it.


        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + failureString);
        }

    }


    @Test(dependsOnMethods = {"generatePolicy"}, enabled = false)
    public void testAdditionalInsuredExclusionsAndConditions() throws Exception {

        testFailed = false;
        failureString = "";

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuGLCoverages();


        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + failureString);
        }


    }


    //changes the type of the Additional Insured and sets the required questions for that Type
    @SuppressWarnings("unused")
    private void setAdditionalInsuredsType(AdditionalInsuredTypeGL type) {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderGeneralLiabilityCoveragesCPP coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);

        sideMenu.clickSideMenuGLCoverages();
        coverages.clickAdditioanlInsureds();

        coverages.editAdditoinalInsured(myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().get(0));
        GenericWorkorderGLCoveragesAdditionalInsured additionalInsured = new GenericWorkorderGLCoveragesAdditionalInsured(driver);
        additionalInsured.selectType(type);

        myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().get(0).setType(type);

        additionalInsured.setTypeQuestions(myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAdditionalInsuredslist().get(0));

        additionalInsured.clickUpdate();
        if (!driver.findElements(By.xpath("//span[contains(text(), 'Location Information')]")).isEmpty()) {
            additionalInsured.clickOverride();
            additionalInsured.clickUpdate();
        }
    }


    private void setGLExposure(String classCode) throws Exception {

        System.out.println("Setting class code : " + classCode);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuGLExposures();

        CPPGeneralLiabilityExposures myExposure = new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0), classCode);

        GenericWorkorder genwo = new GenericWorkorder(driver);
        GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
        exposuresPage.selectAll();
        exposuresPage.clickRemove();
        exposuresPage.clickAdd();
        exposuresPage.addExposure(myExposure);
        ArrayList<CPPGeneralLiabilityExposures> exposuresList = new ArrayList<CPPGeneralLiabilityExposures>();
        exposuresList.add(myExposure);
        myPolicyObj.generalLiabilityCPP.setCPPGeneralLiabilityExposures(exposuresList);
        exposuresPage.clickLocationSpecificQuestionsTab();
        exposuresPage.fillOutBasicUWQuestionsQQ(myExposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
        if (!guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.QuickQuote)) {
            exposuresPage.fillOutBasicUWQuestionsFullApp(myExposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
        }

        genwo.clickPolicyChangeNext();

        List<WebElement> messageList = genwo.getValidationResultsList();
        for (WebElement message : messageList) {
            List<String> classCodeToAdd = getClassCodeToAdd(message.getText());
            CPPGeneralLiabilityExposures myExposureToAdd = new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0), classCodeToAdd.get(2));
            exposuresPage.clickExposureDetialsTab();
            exposuresPage.clickAdd();
            exposuresPage.addExposure(myExposureToAdd);
            exposuresPage.clickLocationSpecificQuestionsTab();
            exposuresPage.fillOutBasicUWQuestionsQQ(myExposureToAdd, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
            if (!guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.QuickQuote)) {
                exposuresPage.fillOutBasicUWQuestionsFullApp(myExposureToAdd, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
            }
        }

        sideMenu.clickSideMenuGLCoverages();
        try {
            genwo.clickClear();
        } catch (Exception e) {
        }

        GenericWorkorderGeneralLiabilityCoveragesCPP coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        coverages.clickExclusionsAndConditions();

        coverages.fillOutGeneralLiabilityCoverages(myPolicyObj);
    }

    @SuppressWarnings("serial")
    private List<String> getClassCodeToAdd(String message) {
        return new ArrayList<String>() {{
            this.add(message.replaceAll("[\\D]", "").substring(0, 5));
            this.add(message.substring(message.indexOf("Location ") + 12, message.indexOf(".")));
            this.add(message.replaceAll("[\\D]", "").substring(message.replaceAll("[\\D]", "").length() - 5));
        }};
    }


    private void verifyElectability(String condition, ExistanceType type) {

        System.out.println("Verifying " + condition);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderGeneralLiabilityCoveragesCPP coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        sideMenu.clickSideMenuGLCoverages();
        if (!driver.findElements(By.xpath("//span[contains(@id, 'ClearButton-btnEl')]")).isEmpty()) {
            coverages.clickWhenClickable(By.xpath("//span[contains(@id, 'ClearButton-btnEl')]"));
        }
        coverages.clickExclusionsAndConditions();

        switch (type) {
            case Required:
                if (!guidewireHelpers.isRequired(condition)) {
                    testFailed = true;
                    failureString += "Exclusion or Condition " + condition + " was NOT Required \n";
                    System.out.println("Exclusion or Condition " + condition + " was NOT Required \n");
                }
                break;
            case Electable:
                if (!guidewireHelpers.isElectable(condition)) {
                    testFailed = true;
                    failureString += "Exclusion or Condition " + condition + " was NOT Electable \n";
                    System.out.println("Exclusion or Condition " + condition + " was NOT Electable \n");
                }
                break;
            case Suggested:
                if (!guidewireHelpers.isSuggested(condition)) {
                    testFailed = true;
                    failureString += "Exclusion or Condition " + condition + " was NOT Suggested \n";
                    System.out.println("Exclusion or Condition " + condition + " was NOT Suggested \n");
                }
                break;
        }


    }


}

















