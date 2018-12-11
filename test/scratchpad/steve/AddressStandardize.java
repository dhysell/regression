package scratchpad.steve;

import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.addressstandardization.AddressStandardization;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.AddressType;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationAdditionalInsured;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineIncludedCoverages;
import repository.pc.workorders.generic.GenericWorkorderLocationAdditionalInsured;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact;

public class AddressStandardize extends BaseTest {

    GeneratePolicy myPolicy = null;
    AddressInfo nonStandardAddress = new AddressInfo("123 main", "", "Pocatello", State.Idaho, "83201", CountyIdaho.Bannock, "United States", AddressType.Work);
    //		private
    private PolicyBusinessownersLineAdditionalInsured boLineAI;

    //	private ArrayList<PolicyBusinessownersLineAdditionalInsured> setAdditonalInsuredBOLineList = new ArrayList<PolicyBusinessownersLineAdditionalInsured>();
    private WebDriver driver;

    @Test(description = "creates the Policy")
    public void generateQQ() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        this.myPolicy = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withCreateNew(CreateNew.Create_New_Only_If_Does_Not_Exist)
                .withInsFirstLastName("Firstname", "Lastname")
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.FullApp);

        System.out.println(myPolicy.accountNumber);
        System.out.println(myPolicy.agentInfo.getAgentUserName());

    }

    public void standardize() {
        AddressStandardization standardizeMe = new AddressStandardization(driver);
        boolean standardized = standardizeMe.handleAddressStandardization("123");
        if (!standardized) {
            Assert.fail(driver.getCurrentUrl() + " " + myPolicy.accountNumber + " The address Standardization page failed to appear.");
        }
    }

    public void getToQQ() {
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickWorkOrderbySuffix("001");

        new GuidewireHelpers(driver).editPolicyTransaction();

    }

    @Test(dependsOnMethods = "generateQQ")
    public void setNewPNIAddress() throws Exception {

        getToQQ();

        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
//		policyInfoPage.clickEditPolicyTransaction();
        policyInfoPage.clickPolicyInfoPrimaryNamedInsured();
        GenericWorkorderPolicyInfoContact policyInfoContactPage = new GenericWorkorderPolicyInfoContact(driver);
        policyInfoContactPage.setNewAddress(true, nonStandardAddress);
        policyInfoContactPage.clickOK();
        AddressStandardization standardizeMe = new AddressStandardization(driver);
        boolean standardized = standardizeMe.handleAddressStandardization("123");
        if (!standardized) {
            Assert.fail(driver.getCurrentUrl() + myPolicy.accountNumber + "The address Standardization page failed to appear.");
        }
        policyInfoContactPage = new GenericWorkorderPolicyInfoContact(driver);
        policyInfoContactPage.clickOK();
    }

    @Test(dependsOnMethods = "setNewPNIAddress")
    public void setAdditionalNamedInsured() throws Exception {
        getToQQ();
        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuPolicyInfo();
        PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Additional", "Type", AdditionalNamedInsuredType.Spouse, nonStandardAddress) {{
            this.setNewContact(CreateNew.Create_New_Always);
        }};

        new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver).addAdditionalNamedInsuredNoStandardizing(true, ani);
        AddressStandardization standardizeMe = new AddressStandardization(driver);
        boolean standardized = standardizeMe.handleAddressStandardization("123");
        if (!standardized) {
            Assert.fail(driver.getCurrentUrl() + myPolicy.accountNumber + "The address Standardization page failed to appear.");
        }

    }

    @Test(dependsOnMethods = "setAdditionalNamedInsured")
    public void setAdditionalInsured() throws GuidewireException {
        this.boLineAI = new PolicyBusinessownersLineAdditionalInsured(ContactSubType.Company, "Skywest", AdditionalInsuredRole.ControllingInterest, nonStandardAddress) {{
            this.setNewContact(CreateNew.Create_New_Always);
        }};

        getToQQ();
        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuBusinessownersLine();

        new GuidewireHelpers(driver).editPolicyTransaction();

        GenericWorkorderBusinessownersLineIncludedCoverages addAddInsured = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
        addAddInsured.addAdditionalInsureds(true, boLineAI);
        standardize();
    }

    @Test
    public void setAdditionalInsuredLocations() throws Exception {
        PolicyLocationAdditionalInsured locationAI = new PolicyLocationAdditionalInsured(ContactSubType.Person, "Barney", "Fyfe", AdditionalInsuredRole.OwnersOrOtherInterests, nonStandardAddress) {{
            this.setNewContact(CreateNew.Create_New_Always);
        }};
        getToQQ();
        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuLocations();

        GenericWorkorderLocations locationsPage = new GenericWorkorderLocations(driver);
        locationsPage.clickPrimaryLocationEdit();

        GenericWorkorderLocationAdditionalInsured locationAddInsured = new GenericWorkorderLocationAdditionalInsured(driver);
        locationAddInsured.addAdditionalInsured(true, locationAI, false);

        standardize();
    }

}
	





 
























