package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;

/**
 * @Author nvadlamudi
 * @Requirement :DE4411: Modal Pop-up Error (BOP location screen) Might be common
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Dec 9, 2016
 */
@QuarantineClass
public class TestBOPLocationsValidations extends BaseTest {
    private GeneratePolicy bopPolicyObj;

    private WebDriver driver;

    @Test()
    private void testCreateBOP() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
        listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Additional" + StringsUtils.generateRandomNumberDigits(8), "Type",
                AdditionalNamedInsuredType.Spouse, new AddressInfo(true)) {
            {
                this.setNewContact(CreateNew.Create_New_Always);
            }
        });

        bopPolicyObj = new GeneratePolicy.Builder(driver)
                .withBusinessownersLine()
                .withPolicyLocations(locationsList)
                .withANIList(listOfANIs)
                .withInsFirstLastName("Test", "BOPQQ")
                .build(GeneratePolicyType.QuickQuote);
    }

    @Test(dependsOnMethods = {"testCreateBOP"})
    private void testValidateBOPLocations() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(bopPolicyObj.agentInfo.getAgentUserName(), bopPolicyObj.agentInfo.getAgentPassword(), bopPolicyObj.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLocations();

        GenericWorkorderLocations location = new GenericWorkorderLocations(driver);
        location.clickLocationsNewLocation();
        location.setLocationsLocationAddress("New...");
        location.setLocationsAddressLine1("123 N Main St");
        location.clickLocationsCancel();
        location.selectOKOrCancelFromPopup(OkCancel.OK);
        location.clickLocationsCancel();
        location.selectOKOrCancelFromPopup(OkCancel.OK);
        if (location.getAddressLine1() == null) {
            Assert.fail("Not able to read the existing location address!!!!!");
        }
    }

    //Added the below test part of DE4050: Unable to update ANI information

    @Test(dependsOnMethods = {"testValidateBOPLocations"})
    private void testUpdateANI() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(bopPolicyObj.agentInfo.getAgentUserName(), bopPolicyObj.agentInfo.getAgentPassword(), bopPolicyObj.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickANIContact(this.bopPolicyObj.aniList.get(0).getPersonFirstName());
        GenericWorkorderPolicyInfoAdditionalNamedInsured editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
        editANIPage.setEditAdditionalNamedInsuredAddressListing(new AddressInfo(true));
/*
		AddressInfo address = new AddressInfo(true);
		editANIPage.setEditAdditionalNamedInsuredAddressLine1(address.getLine1());
		editANIPage.setEditAdditionalNamedInsuredAddressCity(address.getCity());
		editANIPage.setEditAdditionalNamedInsuredAddressState(address.getState());
				editANIPage.setEditAdditionalNamedInsuredAddressZipCode(address.getZip());
		editANIPage.setEditAdditionalNamedInsuredAddressAddressType(address.getType());
		*/
        polInfo.setReasonForContactChange("Testing Purpose");
        editANIPage.clickEditAdditionalNamedInsuredUpdate();
		new GuidewireHelpers(driver).clickNext();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (guidewireHelpers.errorMessagesExist()) {
            Assert.fail("Unexpected validation message  :" + guidewireHelpers.getFirstErrorMessage() + " is displayed.");
        }

    }
}
