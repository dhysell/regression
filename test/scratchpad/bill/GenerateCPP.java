package scratchpad.bill;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
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
import repository.gw.generate.custom.CPPGLCoveragesAdditionalInsureds;
import repository.gw.generate.custom.CPPGeneralLiability;
import repository.gw.generate.custom.CPPGeneralLiabilityCoverages;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import gwclockhelpers.ApplicationOrCenter;

public class GenerateCPP extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    boolean testFailed = false;
    String failureString = "";
    List<String> classList = new ArrayList<String>();

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
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), "10026"));

        CPPGeneralLiabilityCoverages coverages = new CPPGeneralLiabilityCoverages() {{
            this.setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                this.add(new CPPGLCoveragesAdditionalInsureds() {{
                    this.setLocationList(new ArrayList<PolicyLocation>() {{
                        this.add(locationsLists.get(0));
                    }});
                    this.setAddress(new AddressInfo(true));
                }});
            }});
        }};

        CPPGeneralLiability generalLiability = new CPPGeneralLiability() {{
            this.setCPPGeneralLiabilityExposures(exposures);
            this.setCPPGeneralLiabilityCoverages(coverages);
        }};
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        // GENERATE POLICY
        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPGeneralLiability(generalLiability)
                .withLineSelection(LineSelection.GeneralLiabilityLineCPP)
                .withPolicyLocations(locationsLists)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                //Creates Business or Person policy depending on date.
                .withInsPersonOrCompanyDependingOnDay("Bruce", "Wayne", "Wayne Tech")
                //If using the Business or Person switch this has to be Joint since it is the only one on both.
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withInsPrimaryAddress(locationsLists.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentUserName());
    }

}
