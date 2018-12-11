package regression.r3.noclock.policycenter.commercialproperty;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.CommercialProperty.PropertyCoverages;
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
import repository.gw.generate.custom.CPPCommercialProperty_Building_Coverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Details;
import persistence.globaldatarepo.entities.CPClassCodes;
import persistence.globaldatarepo.helpers.CPClassCodesHelper;

public class CPPropertyClassCodes extends BaseTest {


    public GeneratePolicy myPolicyObj = null;
    public boolean testFailed = false;
    public String failureString = "";

    private WebDriver driver;


    @Test(enabled = true)
    public void generateQQPolicy() throws Exception {
        AddressInfo pniAddress = new AddressInfo(true);
        ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>();
        locationList.add(new PolicyLocation(pniAddress, true));

        //COMMERCIAL PROPERTY LINE
        CPPCommercialPropertyLine commercialPropertyLine = new CPPCommercialPropertyLine();
        commercialPropertyLine.setPropertyLineCoverages(new CPPCommercialPropertyLine_Coverages());
        commercialPropertyLine.setPropertyLineExclusionsConditions(new CPPCommercialPropertyLine_ExclusionsConditions());

        //LIST OF COMMERCIAL PROPERTY
        List<CPPCommercialPropertyProperty> commercialPropertyList = new ArrayList<CPPCommercialPropertyProperty>();

        CPPCommercialProperty_Building theBuilding = new CPPCommercialProperty_Building();
        theBuilding.setCoverages(new CPPCommercialProperty_Building_Coverages(PropertyCoverages.BuildingCoverage));
        ArrayList<CPPCommercialProperty_Building> buildingList = new ArrayList<CPPCommercialProperty_Building>();
        buildingList.add(theBuilding);

        CPPCommercialPropertyProperty theProperty = new CPPCommercialPropertyProperty();
        theProperty.setAddress(pniAddress);
        theProperty.setCPPCommercialProperty_Building_List(buildingList);

        commercialPropertyList.add(theProperty);

        CPPCommercialProperty commercialProperty = new CPPCommercialProperty();
        commercialProperty.setCommercialPropertyLine(commercialPropertyLine);
        commercialProperty.setCommercialPropertyList(commercialPropertyList);

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
                .isDraft()
                .build(GeneratePolicyType.QuickQuote);
    }//end generateQQPolicy()


    @Test(dependsOnMethods = {"generateQQPolicy"})
    public void validateClassCodes() throws Exception {
        testFailed = false;
        failureString = "";
        List<CPClassCodes> classCodeList = CPClassCodesHelper.getAllPropertyClassCodes(20);

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCPProperty();

        GenericWorkorderCommercialPropertyPropertyCPP propertyLine = new GenericWorkorderCommercialPropertyPropertyCPP(driver);
        GenericWorkorderCommercialPropertyPropertyCPP_Details proeprtyDetails = new GenericWorkorderCommercialPropertyPropertyCPP_Details(driver);
        propertyLine.editPropertyByNumber(1);
        propertyLine.clickDetailsTab();

        for (CPClassCodes classCode : classCodeList) {
            GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
            building.selectFirstBuildingCodeResultClassCode(classCode.getClassCode());

            String classCodeDescription = proeprtyDetails.getPropertyClassDescription().replace("–", "");
            String classCodeRateType = proeprtyDetails.getRateType();
            if (!classCode.getDescription().replace("–", "").trim().equals(classCodeDescription.trim())) {
                testFailed = true;
                failureString = failureString + classCode.getClassCode() + " Description did not match. Expected: " + classCode.getDescription() + " Found: " + classCodeDescription + "\n";
            }//end if

            switch (classCode.getRated()) {
                case "Class Rated":
                    if (!classCodeRateType.equals("Class")) {
                        testFailed = true;
                        failureString = failureString + classCode.getClassCode() + " Rate Type did not match. Expected: Class | Found: " + classCodeRateType + "\n";
                    }
                    break;
                default:
                    if (!classCodeRateType.equals("Tentative")) {
                        testFailed = true;
                        failureString = failureString + classCode.getClassCode() + " Rate Type did not match. Expected: Tentative | Found: " + classCodeRateType + "\n";
                    }
                    break;
            }//end switch
        }//end for


        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + failureString);
        }
    }//end validateClassCodes()


}
