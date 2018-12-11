package scratchpad.jon;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

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
import repository.gw.generate.custom.CPPGeneralLiability;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoveragesCPP;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP;

public class QuickTest extends BaseTest {


    GeneratePolicy myPolicyObj = null;

    private WebDriver driver;


    @SuppressWarnings("serial")
    @Test
    public void createPolicy() throws Exception {

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
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), "18206"));

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
                .withInsCompanyName("UW Questions")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsLists.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentUserName());

    }


    @SuppressWarnings("serial")
    @Test(dependsOnMethods = {"createPolicy"})
    public void setSomething11() throws Exception {

        List<String> exclutionsconditions = new ArrayList<String>() {{
            this.add("Exclusion - Corporal Punishment CG 22 30");
            this.add("Exclusion - Construction Management Errors And Omissions CG 22 34");
            this.add("Exclusion - Camps Or Campgrounds CG 22 39");
            this.add("Exclusion - Athletic Or Sports Participants CG 21 01");
            this.add("Amendment Of Liquor Liability Exclusion CG 21 50");
            this.add("Snow Plow Operations Coverage CG 22 92");
            this.add("Products/Completed Operations Hazard Redefined CG 24 07");
            this.add("Operation Of Customers Autos On Particular Premises CG 22 68");
            this.add("Canoes Or Rowboats CG 24 16");
            this.add("Boats CG 24 12");
            this.add("Amendment - Travel Agency Tours (Limitation Of Coverage) CG 22 28");
            this.add("Colleges Or Schools (Limited Form) CG 22 71");
            this.add("Exclusion - Counseling Services CG 21 57");
            this.add("Exclusion - Diagnostic Testing Laboratories CG 21 59");
            this.add("Exclusion - Erroneous Delivery Or Mixture And Resulting Failure Of Seed To Germinate - Seed Merchants CG 22 81");

            this.add("Exclusion - Failure To Supply CG 22 50");
            this.add("Exclusion - Funeral Services CG 21 56");
            this.add("Exclusion - Inspection, Appraisal And Survey Companies CG 22 24");
            this.add("Exclusion - Internet Service Providers And Internet Access Providers Errors And Omissions CG 22 98");
            this.add("Exclusion - Laundry And Dry Cleaning Damage CG 22 53");
            this.add("Exclusion - Products And Professional Services (Druggists) CG 22 36");
            this.add("Exclusion - Products And Professional Services (Optical And Hearing Aid Establishments) CG 22 37");
            this.add("Exclusion - Professional Veterinarian Services CG 21 58");
            this.add("Exclusion - Property Entrusted CG 22 29");
            this.add("Exclusion - Real Estate Agents Or Brokers Errors Or Omissions CG 23 01");
            this.add("Exclusion - Services Furnished By Health Care Providers CG 22 44");
            this.add("Exclusion - Specified Therapeutic Or Cosmetic Services CG 22 45");
            this.add("Exclusion - Telecommunication Equipment Or Service Providers Errors And Omissions CG 22 91");
            this.add("Exclusion - Testing Or Consulting Errors And Omissions CG 22 33");

            this.add("");

        }};
        List<List<String>> classCodeLists = new ArrayList<List<String>>() {{
            this.add(new ArrayList<String>() {{
                this.add("67513");
                this.add("67512");
                this.add("47474");
                this.add("47477");
            }});
            this.add(new ArrayList<String>() {{
                this.add("41620");
            }});
            this.add(new ArrayList<String>() {{
                this.add("10332");
                this.add("10331");
                this.add("41422");
            }});
            this.add(new ArrayList<String>() {{
                this.add("63218");
                this.add("63217");
                this.add("63220");
                this.add("63219");
                this.add("43421");
                this.add("43424");
            }});
            this.add(new ArrayList<String>() {{
                this.add("16905");
                this.add("16906");
            }});
            this.add(new ArrayList<String>() {{
                this.add("99310");
            }});
            this.add(new ArrayList<String>() {{
                this.add("14401");
                this.add("16820");
                this.add("16819");
                this.add("16900");
                this.add("16901");
                this.add("16902");
                this.add("16905");
                this.add("16906");
                this.add("16910");
                this.add("16911");
                this.add("16915");
                this.add("16916");
                this.add("16930");
                this.add("16931");
                this.add("16941");
            }});
            this.add(new ArrayList<String>() {{
                this.add("10072");
                this.add("10073");
                this.add("10367");
                this.add("13453");
                this.add("13455");
                this.add("18616");
            }});
            this.add(new ArrayList<String>() {{
                this.add("41668");
                this.add("41667");
                this.add("45190");
                this.add("45192");
                this.add("64074");
                this.add("10110");
                this.add("40111");
                this.add("41422");
                this.add("45191");
                this.add("45193");
                this.add("47477");
            }});
            this.add(new ArrayList<String>() {{
                this.add("10117");
                this.add("40115");
                this.add("40140");
                this.add("40117");
                this.add("43760");
            }});
            this.add(new ArrayList<String>() {{
                this.add("49333");
            }});
            this.add(new ArrayList<String>() {{
                this.add("67513");
                this.add("67512");
                this.add("47474");
                this.add("47477");
            }});
            this.add(new ArrayList<String>() {{
                this.add("48600");
                this.add("41650");
            }});
            this.add(new ArrayList<String>() {{
                this.add("46112");
            }});
            this.add(new ArrayList<String>() {{
                this.add("94225");
                this.add("16890");
            }});
            this.add(new ArrayList<String>() {{
                this.add("13410");
                this.add("92445");
                this.add("97501");
                this.add("97502");
                this.add("99943");
            }});
            this.add(new ArrayList<String>() {{
                this.add("41604");
                this.add("41603");
                this.add("41697");
                this.add("41696");
                this.add("43889");
                this.add("46005");
                this.add("46004");
            }});
            this.add(new ArrayList<String>() {{
                this.add("61223");
                this.add("96317");
            }});
            this.add(new ArrayList<String>() {{
                this.add("45334");
                this.add("47610");
            }});
            this.add(new ArrayList<String>() {{
                this.add("14731");
                this.add("19007");
                this.add("45678");
            }});
            this.add(new ArrayList<String>() {{
                this.add("12375");
                this.add("12374");
                this.add("45900");
            }});
            this.add(new ArrayList<String>() {{
                this.add("13759");
                this.add("15839");
            }});
            this.add(new ArrayList<String>() {{
                this.add("91200");
                this.add("99851");
            }});
            this.add(new ArrayList<String>() {{
                this.add("98751");
                this.add("49763");
                this.add("18991");
            }});
            this.add(new ArrayList<String>() {{
                this.add("47050");
            }});
            this.add(new ArrayList<String>() {{
                this.add("40032");
                this.add("40031");
                this.add("43551");
                this.add("66561");
            }});
            this.add(new ArrayList<String>() {{
                this.add("10113");
                this.add("10115");
                this.add("11128");
                this.add("11127");
                this.add("11234");
                this.add("12356");
                this.add("45192");
                this.add("14655");
                this.add("15600");
                this.add("18912");
                this.add("18911");
                this.add("45191");
                this.add("45193");
                this.add("45190");
            }});
            this.add(new ArrayList<String>() {{
                this.add("18575");
                this.add("99600");
            }});
            this.add(new ArrayList<String>() {{
                this.add("91135");
                this.add("97003");
                this.add("97002");
            }});


            this.add(new ArrayList<String>() {{
                this.add("");
            }});
        }};

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        try {
            for (int i = 0; i <= exclutionsconditions.size() - 1; i++) {
                if (testAvailabilityRule(exclutionsconditions.get(i), classCodeLists.get(i)) != null) {
                    guidewireHelpers.systemOut("SOMETHING FAILED SEE CONSOLE");
                }
            }
        } catch (Exception e) {

            guidewireHelpers.logout();
            new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
            guidewireHelpers.editPolicyTransaction();
        }
    }


    private String testAvailabilityRule(String exclutionCondition, List<String> classCodes) throws Exception {

        boolean testFailed = false;
        String failureList = "This test failed:  \n";
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        for (String theClass : classCodes) {
            guidewireHelpers.systemOut("testing class code : " + theClass);
            SideMenuPC sideMenu = new SideMenuPC(driver);
            sideMenu.clickSideMenuGLExposures();

            CPPGeneralLiabilityExposures myExposure = new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0), theClass);

            GenericWorkorder genwo = new GenericWorkorder(driver);
            GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
            exposuresPage.selectAll();
            exposuresPage.clickRemove();
            exposuresPage.clickAdd();
            exposuresPage.addExposure(myExposure);
            exposuresPage.clickLocationSpecificQuestionsTab();
            exposuresPage.fillOutUnderwritingQuestionsQQ(myExposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
            if (!guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.QuickQuote)) {
                exposuresPage.fillOutUnderwritingQuestionsFULLAPP(myExposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
            }

            sideMenu.clickSideMenuGLCoverages();
            try {
                genwo.clickClear();
            } catch (Exception e) {
            }

            GenericWorkorderGeneralLiabilityCoveragesCPP coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
            coverages.clickExclusionsAndConditions();

            if (!guidewireHelpers.isRequired(exclutionCondition)) {
                testFailed = true;
                failureList = failureList + exclutionCondition + myExposure.getClassCode();
            }
        }

        if (testFailed) {
            guidewireHelpers.systemOut(failureList);
            return failureList;
        } else {
            return null;
        }
    }


}

















