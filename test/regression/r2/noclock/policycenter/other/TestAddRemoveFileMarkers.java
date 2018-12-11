package regression.r2.noclock.policycenter.other;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.ContactSubType;
import repository.gw.enums.FileMarker;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyFileMarkers;
import repository.pc.sidemenu.SideMenuPC;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author skandibanda
 * @Requirement : US9441 : Allow A/R users to manually apply/remove all file markers
 * @Description - A/R users can add or remove file markers
 * @DATE Feb 27, 2017
 */
public class TestAddRemoveFileMarkers extends BaseTest {
    private GeneratePolicy squirePolObj;
    private WebDriver driver;

    @Test()
    private void generateSquireAuto() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        squirePolObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Test", "FikeMarkers")
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"generateSquireAuto"})
    private void testValidateAddAndRemoveFileMarkers() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ARUsers arUser = ARUsersHelper.getRandomARUser();
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), squirePolObj.squire.getPolicyNumber());
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsFileMarkers();
        PolicyFileMarkers fileMarker = new PolicyFileMarkers(driver);
        String errorMessage = "";
        FileMarker[] fileMarkers = {FileMarker.Cash_Only, FileMarker.Certificate, FileMarker.Collection, FileMarker.Do_not_Reinstate_Rewrite,
                FileMarker.Form_E, FileMarker.Oregon_Washington_Construction_Board, FileMarker.Pending_Cancel, FileMarker.Pictures_in_DISR,
                FileMarker.Returned_Mail, FileMarker.SR22, FileMarker.Treaty_Exception, FileMarker.Umbrella};


        //Add File Markers
        for (FileMarker FileMarker : fileMarkers) {
            fileMarker.setFileMarker(FileMarker, true);
        }
        fileMarker.clickUpdate();

        //Validate File Markers Added
        String[] flags = {"CashOnly", "Certificate", "Collection", "ReinstateRewrite", "FormE", "Construction", "PendingCancel", "PicturesInDISR", "ReturnedMail", "SR22", "TreatyException", "Umbrealla"};

        for (String flag : flags)
            if (!fileMarker.verifyExpectedFlagStatusExist(flag))
                errorMessage = errorMessage + "Expected flag " + flag + " is not displayed.\n";


        //Remove File Markers
        fileMarker.clickEdit();

        for (FileMarker FileMarker : fileMarkers) {
            fileMarker.setFileMarker(FileMarker, false);
        }
        fileMarker.clickUpdate();

        //Validate File Markers Removed
        for (String flag : flags)
            if (fileMarker.verifyExpectedFlagStatusExist(flag))
                errorMessage = errorMessage + "UnExpected flag " + flag + " is displayed.\n";

        if (!errorMessage.equals(""))
            Assert.fail(errorMessage);
    }


}
