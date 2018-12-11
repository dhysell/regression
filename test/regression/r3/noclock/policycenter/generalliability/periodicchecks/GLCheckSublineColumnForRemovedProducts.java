package regression.r3.noclock.policycenter.generalliability.periodicchecks;

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
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCPP;
import persistence.globaldatarepo.entities.GLClassCodes;
import persistence.globaldatarepo.helpers.GLClassCodeHelper;

public class GLCheckSublineColumnForRemovedProducts extends BaseTest {

    GeneratePolicy myPolicyObj;
    String userName = null;
    String userPass = null;
    String accountNumber = null;
    String classCode = null;
    private WebDriver driver;

    @Test(enabled = true,
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

        GLClassCodes plusClassCode = GLClassCodeHelper.getRandomPlusGLPremiumBaseClassCode();
        String usedClassCode = plusClassCode.getCode();

        ArrayList<CPPGeneralLiabilityExposures> exposures = new ArrayList<CPPGeneralLiabilityExposures>();
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), usedClassCode));

//		myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(locationsLists.get(0), usedClassCode));

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
                .withInsPersonOrCompanyDependingOnDay("Lex", "Luther", "Lex Corp")
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withPackageRiskType(PackageRiskType.Contractor)
                .withInsPrimaryAddress(locationsLists.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        userName = myPolicyObj.agentInfo.getAgentUserName();
        userPass = myPolicyObj.agentInfo.getAgentPassword();
        accountNumber = myPolicyObj.accountNumber;

        classCode = usedClassCode;

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("Agent: " + userName + "\n#############");
    }


    /**
     * @throws Exception
     * @Author bmartin
     * @Requirement PC8 GL QuoteApplication Quote - UI Final Tab
     * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/GeneralLiability/PC8%20-%20GL%20-%20QuoteApplication%20-%20Quote.xlsx">PC8 GL QuoteApplication Quote</a>
     * @Description Checks the quote screen for class codes that have + in them, looks at the Subline Column, and makes sure the word Products is not there.
     * @DATE Oct 13, 2016
     */
    @Test(enabled = true,
            description = "Checks the quote screen for class codes that have + in them, looks at the Subline Column, and makes sure the word Products is not there.",
            dependsOnMethods = {"createPolicy"})
    public void testGLSublineColumn() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(userName, userPass, accountNumber);

        GenericWorkorderGeneralLiabilityCPP myGL = new GenericWorkorderGeneralLiabilityCPP(driver);
        myGL.checkCheckSublineColumnForRemovedProducts(classCode);

    }


    /**
     * @throws Exception
     * @Author bmartin
     * @Requirement WCIC General Liability-Product-Model
     * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Commercial%20Package%20Policy%20(CPP)/Guidewire%20-%20General%20Liability/WCIC%20General%20Liability-Product-Model.xlsx">WCIC General Liability-Product-Model</a>
     * @Description Checks to make sure that we have validation messages for the required fields.
     * @DATE Oct 21, 2016
     */
    @Test(enabled = true,
            description = "Checks to make sure that we have validation messages for the required fields.",
            dependsOnMethods = {"createPolicy"})
    public void testGLValidationMessages() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(userName, userPass, accountNumber);

        GenericWorkorderGeneralLiabilityCPP myGL = new GenericWorkorderGeneralLiabilityCPP(driver);
        myGL.checkCheckSublineColumnForRemovedProducts(classCode);

    }

}
