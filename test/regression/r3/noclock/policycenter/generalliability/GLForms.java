package regression.r3.noclock.policycenter.generalliability;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GLClassCode;
import repository.gw.enums.GeneralLiabilityForms;
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
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.forms.cpp.generalliability.GeneralLiabilityFormInference;
import repository.pc.workorders.generic.GenericWorkorderForms;
import persistence.globaldatarepo.entities.Glforms;
import persistence.globaldatarepo.helpers.GLFormsHelpers;

public class GLForms extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    //	GeneratePolicy myPolicyObjForms = null;
//	List<GLForms> changeFormsList = new ArrayList<GLForms>();
//	List<GeneralLiabilityForms> caFormsList = new ArrayList<GeneralLiabilityForms>();
    private WebDriver driver;


    @SuppressWarnings("serial")
    @Test
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
                .withInsCompanyName("GL Forms")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsLists.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);

        System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentUserName());

    }


    @Test(dependsOnMethods = {"generatePolicy"})
    public void testGeneralLiabilityForms() throws Exception {
        boolean testFailed = false;
        List<String> extraForms = new ArrayList<String>();
        List<Glforms> missingForms = new ArrayList<Glforms>();

        new GeneralLiabilityFormInference(driver);

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Login login = new Login(driver);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);

        if (!guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicyIssued)) {
            login.loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        } else {
            login.loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        }

        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuForms();

        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        List<String> formsList = formsPage.getFormDescriptionsFromTable();

        for (String form : formsList) {
            boolean found = false;
            for (GeneralLiabilityForms glform : myPolicyObj.generalLiabilityCPP.getGlForms()) {
                //if policy is in Submission status
                Glforms tempForm = GLFormsHelpers.getGeneralLiabilityFormByName(glform.getValue());
                if (guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.FullApp) || guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicySubmitted)) {
                    if (tempForm.getSubmission().equalsIgnoreCase("Yes")) {
                        if (tempForm.getName().equalsIgnoreCase(form)) {
                            found = true;
                            break;
                        }
                    }
                    //if policy is issued
                } else if (guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicyIssued)) {
                    if (tempForm.getIssuance().equalsIgnoreCase("Yes")) {
                        if (tempForm.getName().equalsIgnoreCase(form)) {
                            found = true;
                            break;
                        }
                    }
                }
            }//END FormsCA FOR
            if (!found) {
                extraForms.add(form);
                testFailed = true;
            }//END IF
        }//END String FOR


        for (GeneralLiabilityForms glform : myPolicyObj.generalLiabilityCPP.getGlForms()) {
            Glforms tempForm = GLFormsHelpers.getGeneralLiabilityFormByName(glform.getValue());
            boolean found = false;
            //if policy is in Submission status
            if (guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.FullApp) || guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicySubmitted)) {
                if (tempForm.getSubmission().equalsIgnoreCase("Yes")) {
                    for (String form : formsList) {
                        if (form.equalsIgnoreCase(tempForm.getName())) {
                            found = true;
                            break;
                        }//END IF
                    }//END FOR
                }//END IF
                //if policy is issued
            } else if (guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicyIssued)) {
                if (tempForm.getIssuance().equalsIgnoreCase("Yes")) {
                    for (String form : formsList) {
                        if (form.equalsIgnoreCase(tempForm.getName())) {
                            found = true;
                            break;
                        }//END IF
                    }//END OFR
                }//END IF
            }//END ELSE IF
            if (!found) {
                missingForms.add(tempForm);
                testFailed = true;
            }//END IF
        }// END FOR
        if (testFailed) {
            String missingList = "";
            String extraList = "";
            for (String form : extraForms) {
                extraList = extraList + form + " \n ";
            }//END FOR
            for (Glforms form : missingForms) {
                missingList = form.getName() + " \n ";
            }//END FOR
            Assert.fail(driver.getCurrentUrl() + " " + myPolicyObj.accountNumber + "\nDocuments either didn't generate or generated when not supposed to\n" +
                    "Documents that FAILED to Generate: \n" + missingList + "\n" +
                    "Docuemnts that generated falsely: \n" + extraList);
        }//END IF testFailed
    }


}























