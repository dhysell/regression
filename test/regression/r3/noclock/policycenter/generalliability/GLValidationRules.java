package regression.r3.noclock.policycenter.generalliability;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GLClassCode;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPGeneralLiability;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP;

public class GLValidationRules extends BaseTest {


    GeneratePolicy myPolicyObj = null;
    List<String> tempList = new ArrayList<String>();
    SoftAssert softAssert = new SoftAssert();
    WebDriver driver;

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
                .isDraft()
                .build(GeneratePolicyType.QuickQuote);

        System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentUserName());

    }


    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void ValidateGLBlockUser() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObj);
		
		
		/*
		When no class code has been added display the message.	
		Class Code : Missing required field "Class Code"*/

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuGLExposures();
        GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
        exposuresPage.clickExposureDetialsTab();
        exposuresPage.selectAll();
        exposuresPage.clickRemove();
        validateErrorMessageCommercialAutoLine("Class Code : Missing required field \"Class Code\"");


//		When endorsement CG 24 17 is selected and there is no row added to the endorsement, display the message. 	
//		Please add a Scheduled Railroad and Designated Job Site to endorsement Contractual Liability - Railroad CG 24 17.
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		
		When endorsement IDCG 31 2013 is selected and the limit is over $250,000 and one of the following class codes is selected: 66123 or 66122 display the message.	
		Legal services are not eligible for limits in excess of $250,000 please correct or contact Brokerage for increased limits.
		
		When endorsement CG 24 51 and questions "Term Type of unmanned aircraft." is answered with Term Option "Homemade", then display the message.	
		Homemade unmanned aircraft are not eligible, contact Brokerage.
		
		When class code 98303 is selected and question �Indicate type of buildings or structures does applicant/insured work on.� Is selected with Oilfield equipment or Tanks � petroleum then display the message.	
		Applicant/Insured is painting oilfield equipment or petroleum tanks. Risk is not eligible contact Brokerage.
		
		When "What are the spray receipts?" are 30% more than "What are the total receipts for all operations?", display the message.	
		Spray receipts are more than 30% of the total, risk is not eligible contact Brokerage.
		
		When class code 98303 is selected and question "What percentage of the total receipts does painting over 3 stories represent?" if it is "Over 20%" is selected display the message stop them from leaving the screen.	
		Painting over 3 stories exceeds 20% of operations-please refer to Brokerage.
		
		When class code 98306 is selected and question "What % of the total receipts does painting of oil or gas tanks represent?" is answered "Over 20%" display the message and stop them from leaving the screen.	
		Applicant/Insured painting of oil and gas tanks is not incidental. Please contact Brokerage for coverage.
		
		When class code 98309 is selected and question �What % of the total receipts does painting of steel structures or bridges represent?� is selected �11-20%� or "Over 20%" display the message and stop them on the screen.	
		Applicant/Insured painting of steel structures or bridges is not incidental. Please contact Brokerage for coverage.
		
		If exposure 10151 is selected and the question �Indicate % of rental receipts versus sales and service.� Is answered the Over 25% then display the message and stop them on the screen.	
		Bicycle rental receipts are more than incidental. Contact Brokerage for coverage.
		
		If exposure 10151 is selected and the question �Does the applicant/insured have written contracts with customers renting bicycles acknowledging they were given instructions and understands the proper usage of the equipment?� is answered no display the message.	
		Applicant/Insured does not have written contracts with customers renting bicycles. Contact Brokerage for coverage.
		
		When class code 13410 is selected and question "Indicate the % of receipts LPG Gas sales represent to the total business." is answered with Over 15% display the message.	
		Percentage of receipts LPG Gas sales is more than incidental, risk is not eligible contact Brokerage.
		
		When endorsement CG 24 51 is selected and question �What is the maximum altitude of the unmanned aircraft?� is answered with a response over 400 then display the message.	
		Maximum altitude of the unmanned aircraft exceeds our underwriting standards. Contact Brokerage.
		
		When endorsement CG 24 51 is selected and question �What is the maximum speed that the unmanned aircraft can operate at?� is answered with a response over 40 display the message.	
		Maximum speed that the unmanned aircraft can operate at exceeds our underwriting standards. Contact Brokerage.
		
		When endorsement CG 24 51 is selected and question "Weight of unmanned aircraft." is over 55 lbs. then display the message.	
		The unmanned aircraft is over 55 lbs. Please contact Brokerage for coverage.
		
		When class code 91342 and/or 91340 and questions �Indicate % of roofing receipts.� Is answered with 11-15%, 16-25%, or Over 25% display the message.	
		Applicant/Insured is considered a roofer and therefore not eligible. Contact Brokerage for assistance.
		
		When one of the following class codes are seleced: 16900, 16901, 16902, 16905, or 16906 and one of the following question codes are answered yes CG16900Q100, CG16901Q100, CG16902Q100, CG16905Q100, or CG16906Q100.	
		You have selected a restaurants with no sale of alcoholic beverages. You have also answered the question "Does the applicant have liquor sales?" with a Yes. Please change class to restaurants with sale of alcoholic beverages and add one of the following liquor class codes: 58161 or 58165.
		
		When you have one of the following class codes selected: 16910, 16911, 16915, 16916, 16930, or 16931, the user needs to be stopped on the wizard step until class code 58161 has been added to the same location.	
		You have class codes ${Class code} on Location ${Location number}. Please add class code 58161 to the same location.
		
		When this question is showing: CG97047Q2100, CG97050Q2100, the minium value needs to be $1  	
		You have class codes ${Class code} on Location ${Location number}. The question "What are the total receipts for all operations?" must be greater than 0.
		
		When question "Type of operation" is selected with any response from the drop down option, then display the message.	
		Please add class ${description from the answer} before proceeding any further.
		
		Whenever a General Liability question shows up with dependent questions that have OR in the "Required At" column on the sheet "Class Code Questions" and one of those dependent questions have not been checked then display the message.	
		At least 1 question associated with "${Parent Question}" is required
		
		When question CG41677Q200 is selected and class code 92663 is not on the policy, then dissplay the message.	
		Applicant/Insured engages in architectural or engineering consulting. Please go back to the Exposures Details tab and add class 92663 (Engineers or Architects � consulting � not engaged in actual construction).
		
		When question CG41677Q300 is selected and class code 66561 is not on the policy, then dissplay the message.	
		Applicant/Insured engages in medical consulting. Please go back to the Exposures Details tab and add class 66561 (Medical Offices).
		
		When question CG41677Q400 is selected and class code 66122 is not on the policy, then dissplay the message.	
		Applicant/Insured engages in legal consulting. Please go back to the Exposures Details tab and add class 66122 (Lawyers Offices � Other than Not-For-Profit).
		
		When question CG41677Q500 is selected and class code 41675 is not on the policy, then dissplay the message.	
		Applicant/Insured engages in computer consulting. Please go back to the Exposures Details tab and add class 41675 (Computer Consulting or Programming).
		
		When question CG46773Q600 is selected and class code 10331 is not on the policy, then dissplay the message.	
		Risk has campgrounds. Please go back to the Exposures Details tab and add class 10331 (Campgrounds � Other than Not-For-Profit).
		
		When question CG46773Q700 is selected and class code 46671 is not on the policy, then dissplay the message.	
		Risk has a playground. Please go back to the Exposures Details tab and add class 46671 (Parks or Playgrounds).
		
		When question CG95410Q300 is selected and class code 94007 is not on the policy, then dissplay the message.	
		Applicant/Insured does excavation. Please go back to the Exposures Details tab and add class 94007 (Excavation � NOC).
		
		When question CG95410Q400 is selected and class code 97047 is not on the policy, then dissplay the message.	
		Applicant/Insured does landscaping. Please go back to the Exposures Details tab and add class 97047 (Landscape Gardening).
		
		When question CG10331Q1 is selected and class code - is not on the policy, then dissplay the message.	
		Applicant/Insured operates a restaurant, please add a restaurant class.
		
		When question CG10331Q2 is selected and class code - is not on the policy, then dissplay the message.	
		Applicant/Insured operates a mobile home park, please add class 46202 Mobile Home Parks or Courts.
		
		When question CG10331Q3 is selected and class code - is not on the policy, then dissplay the message.	
		Applicant/Insured operates a swimming pool, please add class 48925 Swimming Pools � NOC.
		
		When question CG10332Q1 is selected and class code - is not on the policy, then dissplay the message.	
		Applicant/Insured operates a restaurant, please add a restaurant class.
		
		When question CG10332Q2 is selected and class code - is not on the policy, then dissplay the message.	
		Applicant/Insured operates a mobile home park, please add class 46202 Mobile Home Parks or Courts.
		
		When question "Does applicant/insured operate a swimming pool?" [CG10332Q3] is selected and class code - is not on the policy, then dissplay the message.	
		Applicant/Insured operates a swimming pool, please add class 48925 Swimming Pools � NOC.
		
		When question "Does applicant/insured operate a restaurant?" [CG11138Q1] is selected and class code - is not on the policy, then dissplay the message.	
		Applicant/Insured operates a restaurant, please add a restaurant class.
		
		When question "Does applicant/insured operate a mobile hom park" [CG11138Q2] is selected and class code - is not on the policy, then dissplay the message.	
		Applicant/Insured operates a mobile home park, please add class 46202 Mobile Home Parks or Courts.
		
		When question "Does applicant/insured operate a swimming pool?" [CG11138Q3] is selected and class code - is not on the policy, then dissplay the message.	
		Applicant/Insured operates a swimming pool, please add class 48925 Swimming Pools � NOC.
		
		When required information is not filled out on the screen "Additional Insured / Certificate Holder" then display the message.	
		General Liability: Must answer required fields for ${Name of the Additional Interest}.
		
		When these are the only class codes for the location they are tied to on the exposure screen: 70412, 50911, 59211, 58161, 58168, 58165, and/or 58166 then display the message.	
		You cannot have liquor class codes only. You need to add the class code for the exposure before you can proceed.

		*/


    }


    private void validateErrorMessageCommercialAutoLine(String errorMessage) {
        GenericWorkorder genWO = new GenericWorkorder(driver);
        genWO.clickPolicyChangeNext();
        softAssert.assertTrue(genWO.getValidationResultsList().get(0).getText().equals(errorMessage), "Failed to Block User on validation message: " + errorMessage);
    }


    private void setExposure(PolicyLocation location, String classCode) throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuGLExposures();
        CPPGeneralLiabilityExposures myExposure = new CPPGeneralLiabilityExposures(location, classCode);
        GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
        exposuresPage.clickExposureDetialsTab();
        exposuresPage.selectAll();
        exposuresPage.clickRemove();
        exposuresPage.clickAdd();
        exposuresPage.addExposure(myExposure);
        exposuresPage.clickLocationSpecificQuestionsTab();
        //FILL OUT UW QUESTIONS
        exposuresPage.fillOutBasicUWQuestionsQQ(myExposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
        exposuresPage.fillOutBasicUWQuestionsFullApp(myExposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());

    }


}
















