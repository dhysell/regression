package regression.r2.noclock.policycenter.submission_bop;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBuildingAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderBuildings;

/**
 * @Author sbroderick
 * @Requirement Currently on Ordinance or Law Coverage BP0446, when there is a
 * value on one box and user enters a value on a different box, the
 * first box gets changed to zero when the page refreshes. We need
 * a warning message that warns the user.
 * <p>
 * The message should read: �(Location Number and Location Address)
 * (Building Number): (First type of limit) has changed to (Second
 * type of limit) on Ordinance or Law BP 04 46. Please verify the
 * type of limit wanted. If both Demolition Cost limit and Incr.
 * Cost of Construction Limit are desired, please choose Demo. &
 * Incr. Cost of Construction Com. Limit and include all limits.�
 * <p>
 * First type of limit = the original limit that user enters;
 * second type of limit = the new limit that user enters.
 * @RequirementsLink <a href=
 * "https://rally1.rallydev.com/#/9877319305d/detail/userstory/44225129290">
 * Requirements are in the Rally Story</a>
 * @Description New Ordinance or Law Coverage Requirements
 * @DATE Nov 11, 2015
 * @throws Exception
 */
public class OrdinanceOrLawMessage extends BaseTest {

    private GeneratePolicy myQQ;

    private WebDriver driver;

    @Test
    public void generateNewQQ() throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.myQQ = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Ordinance or Law")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.QuickQuote);

        System.out.println("Policy with account Number: " + myQQ.accountNumber + " was created by "
                + myQQ.agentInfo.getAgentUserName());

    }

    @Test(dependsOnMethods = "generateNewQQ")
    public void testOrdinanceOrLawMessage() throws Exception {

        String messageDemo = "Location 1 - " + myQQ.busOwnLine.locationList.get(0).getAddress().getLine1() + ", "
                + myQQ.busOwnLine.locationList.get(0).getAddress().getCity() + ", ID " + myQQ.busOwnLine.locationList.get(0).getAddress().getZip()
                + ", Building - 1: Demolition Cost Limit has changed to Incr. Cost Of Construction Limit on Ordinance or Law BP 04 46. Please verify the type of limit wanted. If both Demolition Cost limit and Incr. Cost of Construction Limit are desired, please choose Demo. & Incr. Cost of Construction Com. Limit and include all limits.";

        String controlMessageInc = "Location 1 - " + myQQ.busOwnLine.locationList.get(0).getAddress().getLine1() + ", "
                + myQQ.busOwnLine.locationList.get(0).getAddress().getCity() + ", ID" + " " + myQQ.busOwnLine.locationList.get(0).getAddress().getZip()
                + ", Building - 1: Incr.";

        String controlMessageDemo = "Location 1 - " + myQQ.busOwnLine.locationList.get(0).getAddress().getLine1() + ", "
                + myQQ.busOwnLine.locationList.get(0).getAddress().getCity() + ", ID" + " " + myQQ.busOwnLine.locationList.get(0).getAddress().getZip()
                + ", Building - 1: Demolition Cost";

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myQQ);
        SideMenuPC menu = new SideMenuPC(driver);
        menu.clickSideMenuBuildings();
        new GuidewireHelpers(driver).editPolicyTransaction();
        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
        buildings.clickBuildingsBuildingEdit(1);
        buildings.clickAdditionalCoverages();
        GenericWorkorderBuildingAdditionalCoverages addOrdinanceOrLaw = new GenericWorkorderBuildingAdditionalCoverages(driver);
        addOrdinanceOrLaw.setOrdinanceLaw(true);
        addOrdinanceOrLaw.setDemoCost(5000);
        addOrdinanceOrLaw.setCostOfConstruction(2000);

        GenericWorkorder validationPopup = new GenericWorkorder(driver);
        String validationMessage = validationPopup.getValidationResults();
        System.out.println(validationMessage);
        System.out.println("Should match requirements");
        System.out.println(messageDemo);
        if ((!validationMessage.contains(controlMessageDemo))) {
            Assert.fail(driver.getCurrentUrl() + myQQ.accountNumber +
                    "The Validation Results message did not match the expected message.");
        }
        addOrdinanceOrLaw = new GenericWorkorderBuildingAdditionalCoverages(driver);
        addOrdinanceOrLaw.setCostOfConstruction(1000);
        addOrdinanceOrLaw.setDemoCost(5000);

        validationPopup = new GenericWorkorder(driver);
        String validationMessageInc = validationPopup.getValidationResults();

        if ((!validationMessageInc.contains(controlMessageInc))) {
            Assert.fail(driver.getCurrentUrl() + myQQ.accountNumber +
                    "The Validation Results message did not match the expected message.");
        }

    }

}
