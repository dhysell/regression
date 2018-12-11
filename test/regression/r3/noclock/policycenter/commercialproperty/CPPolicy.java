package regression.r3.noclock.policycenter.commercialproperty;

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
import repository.gw.generate.custom.CPPCommercialProperty;
import repository.gw.generate.custom.CPPCommercialPropertyLine;
import repository.gw.generate.custom.CPPCommercialPropertyLine_Coverages;
import repository.gw.generate.custom.CPPCommercialPropertyLine_ExclusionsConditions;
import repository.gw.generate.custom.CPPCommercialPropertyProperty;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.gw.generate.custom.PolicyLocation;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;

public class CPPolicy extends BaseTest {

    public GeneratePolicy myPolicyObj = null;

    private WebDriver driver;


    @SuppressWarnings("serial")
    @Test()
    public void generateQQPolicy() throws Exception {
        AddressInfo pniAddress = new AddressInfo(true);
        ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>() {{
            this.add(new PolicyLocation(pniAddress, true));
        }};

        //COMMERCIAL PROPERTY LINE
        CPPCommercialPropertyLine commercialPropertyLine = new CPPCommercialPropertyLine() {{
            this.setPropertyLineCoverages(new CPPCommercialPropertyLine_Coverages() {{
                //SET COMMERCIAL PROPERTY LINE COVERAGES HERE
            }});
            this.setPropertyLineExclusionsConditions(new CPPCommercialPropertyLine_ExclusionsConditions() {{
                //SET COMMERCIAL PROPERTY LINE EXCLUSIONS CONDITIONS HERE
            }});
        }};

        //LIST OF COMMERCIAL PROPERTY
        List<CPPCommercialPropertyProperty> commercialPropertyList = new ArrayList<CPPCommercialPropertyProperty>() {{
            this.add(new CPPCommercialPropertyProperty() {{
                this.setAddress(pniAddress);
                this.setCPPCommercialProperty_Building_List(new ArrayList<CPPCommercialProperty_Building>() {{
                    this.add(new CPPCommercialProperty_Building() {{
                        //SET BUILDING STUFF HERE
                    }});
                }});
            }});
        }};

        CPPCommercialProperty commercialProperty = new CPPCommercialProperty() {{
            this.setCommercialPropertyLine(commercialPropertyLine);
            this.setCommercialPropertyList(commercialPropertyList);
        }};

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialProperty(commercialProperty)
                .withLineSelection(LineSelection.CommercialPropertyLineCPP)
                .withPolicyLocations(locationList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Comm Property")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(pniAddress)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        //return myPolicyObj;

    }


    @Test(dependsOnMethods = {"generateQQPolicy"})
    public void convertToPolicySubmitted() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObj.convertTo(driver, GeneratePolicyType.PolicySubmitted);
    }


}










