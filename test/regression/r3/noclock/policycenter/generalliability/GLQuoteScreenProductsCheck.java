package regression.r3.noclock.policycenter.generalliability;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneralLiabilityCoverages;
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
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCPP;

public class GLQuoteScreenProductsCheck extends BaseTest {
    GeneratePolicy myPolicyObj;
    String userName = null;
    String userPass = null;
    String uwUserName = null;
    String uwPassword = null;
    String accountNumber = null;
    String policyNumber = null;

    private WebDriver driver;


    @Test(enabled = false,
            description = "Creates an account through Full Application")
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
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), "10026"));

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

    @Test(enabled = false,
            dependsOnMethods = {"createPolicy"},
            description = "Sets GL Coverages")
    public void setGLFormsCoverages() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(userName, userPass, accountNumber);

        GenericWorkorderGeneralLiabilityCPP coverages = new GenericWorkorderGeneralLiabilityCPP(driver);
        coverages.setCoveragesCheckbox(myPolicyObj.commercialPackage.locationList.get(0), GeneralLiabilityCoverages.IDCG312001);
    }
}
