package regression.r3.noclock.policycenter.generalliability;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.CreateNew;
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
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderGLCoveragesAdditionalInsured;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoveragesCPP;
import persistence.globaldatarepo.entities.GLClassCodes;
import persistence.globaldatarepo.helpers.GLClassCodeHelper;

public class GLLienHolderAddressCheck extends BaseTest {

    GeneratePolicy myPolicyObj;
    String userName = null;
    String userPass = null;
    String accountNumber = null;

    private WebDriver driver;

    @Test(enabled = true,
            description = "Creates an account through Quick Quote")
    public void createPolicy() throws Exception {

        // LOCATIONS
        final ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>() {
            private static final long serialVersionUID = 1L;

            {
                this.add(new PolicyLocation(new AddressInfo(true), false) {{
                    this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {
                        private static final long serialVersionUID = 1L;

                        {
                            this.add(new PolicyLocationBuilding() {{
                            }}); // END BUILDING
                        }
                    }); // END BUILDING LIST
                }});// END POLICY LOCATION
            }
        }; // END LOCATION LIST


        ArrayList<CPPGeneralLiabilityExposures> exposures = new ArrayList<CPPGeneralLiabilityExposures>();
        GLClassCodes randomClassCode = GLClassCodeHelper.getRandomGLClassCodeWithoutUWQuestions();
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), randomClassCode.getCode()));

        CPPGeneralLiability generalLiability = new CPPGeneralLiability() {{
            this.setCPPGeneralLiabilityExposures(exposures);
        }};

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPGeneralLiability(generalLiability)
                .withLineSelection(LineSelection.GeneralLiabilityLineCPP)
                .withPolicyLocations(locationsLists)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompanyDependingOnDay("Clark", "Kent", "Daily Planet")
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withPackageRiskType(PackageRiskType.Office)
                .withInsPrimaryAddress(locationsLists.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        userName = myPolicyObj.agentInfo.getAgentUserName();
        userPass = myPolicyObj.agentInfo.getAgentPassword();
        accountNumber = myPolicyObj.accountNumber;

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("Agent: " + userName + "\n#############");
    }

    /**
     * @Author bmartin
     * @Requirement CL - General Liability-Product-Model
     * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Commercial%20Package%20Policy%20(CPP)/Guidewire%20-%20General%20Liability/WCIC%20General%20Liability-Product-Model.xlsx">WCIC General Liability-Product-Model</a>
     * @Description Add a Lien Holder onto the account, and check to make sure it won't let me leave the page without adding the address. Also make sure the address stays on the account once it's added
     * @DATE Jun 24, 2016
     */
    @Test(enabled = true,
            dependsOnMethods = {"createPolicy"},
            description = "Sets GL Coverages")
    public void setGLFormsCoverages() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(userName, userPass, accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuGLCoverages();

        GenericWorkorderGeneralLiabilityCoveragesCPP myCoverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        myCoverages.clickAdditioanlInsureds();

        GenericWorkorderGLCoveragesAdditionalInsured myAI = new GenericWorkorderGLCoveragesAdditionalInsured(driver);
        myAI.checkAdditionalInsuredsAddress(myPolicyObj);

    }

}