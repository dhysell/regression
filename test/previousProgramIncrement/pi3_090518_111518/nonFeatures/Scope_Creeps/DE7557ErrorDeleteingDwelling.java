package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;

import java.util.ArrayList;

/**
 * @Author swathiAkarapu
 * @Requirement DE7557
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/defect/227435384744">DE7557</a>
 * @Description
 * UAT, DEV
 * hsavage, ctatom
 * Account/Policy: 290208 (DEV) 019430 (UAT)
 * Either create or transition in a policy with sections I and II and only one dwelling
 * On the "property detail" screen, remove the only dwelling and add a new one
 * Try to click next
 * Actual: Illegal State Exception
 * Expected: No error
 *
 * Stack trace: (store in stikked) http://stikked.idfbins.com/view/c82593df
 *
 * See attached screenshots.
 * @DATE September 14, 2018
 */
public class DE7557ErrorDeleteingDwelling extends BaseTest {

    @Test(enabled = true)
    public void verifyErrorDeletingDwelling() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        GeneratePolicy myPolicyObject = null;

        ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
        ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> locOnePropertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();

        repository.gw.generate.custom.PLPolicyLocationProperty property = new PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.ResidencePremises);
         locOnePropertyList.add(property);

        repository.gw.generate.custom.PolicyLocation locToAdd = new repository.gw.generate.custom.PolicyLocation(locOnePropertyList);
        locationsList.add(locToAdd);

        repository.gw.generate.custom.SquireLiability myLiab = new repository.gw.generate.custom.SquireLiability();


        repository.gw.generate.custom.SquirePropertyAndLiability myPropertyAndLiability = new repository.gw.generate.custom.SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire(repository.gw.enums.SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("delete", "andAddProperty")
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.FullApp);



        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);

        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);


        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details pcPropertyDetailsPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction pcPropertyConstructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        pcPropertyDetailsPage.selectBuilding("1");
        pcPropertyDetailsPage.clickRemoveProperty();

        pcPropertyDetailsPage.clickAdd();

        pcPropertyDetailsPage.fillOutPropertyDetails(property);
        pcPropertyConstructionPage.fillOutPropertyConstruction(property);
        new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property);
        pcPropertyDetailsPage.clickOk();

        pcPropertyDetailsPage.clickOkayIfMSPhotoYearValidationShows();

        pcPropertyDetailsPage.clickNext();
        softAssert.assertFalse(new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Property Information')]"),
                "NEXT BUTTON  is  NOT navigated to next page of Property Information");
        softAssert.assertAll();

    }


}
