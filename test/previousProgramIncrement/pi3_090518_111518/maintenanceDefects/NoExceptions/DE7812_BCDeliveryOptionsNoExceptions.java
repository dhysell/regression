package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.NoExceptions;

import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonCharges;
import repository.bc.search.BCSearchAccounts;
import com.idfbins.driver.BaseTest;
import com.idfbins.enums.OkCancel;
import repository.driverConfiguration.Config;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;


public class DE7812_BCDeliveryOptionsNoExceptions extends BaseTest {

    private WebDriver driver;
    private boolean isEditDeliveryOptionsGood =  true;
    private double amount = (double) NumberUtils.generateRandomNumberInt(299,999);

    @Test
    public void testEditDeliveryOptionsPopulating() throws Exception {
        ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Clerical , ARCompany.Personal);

        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).login(arUser.getUserName(),arUser.getPassword());
        String lienAccountNumber = new BCSearchAccounts(driver).findLienholderAccountInGoodStanding("98");
        new BCSearchAccounts(driver).searchAccountByAccountNumber(lienAccountNumber);
        BCAccountMenu bcAccountMenu = new BCAccountMenu(driver);
        bcAccountMenu.clickBCMenuCharges();
        BCCommonCharges bcCommonCharges= new BCCommonCharges(driver);
        bcCommonCharges.sortChargeTableByRecentDate();
        String chargePolicyNumber = bcCommonCharges.getPolicyNumberWithPayerCharge(lienAccountNumber);
        bcCommonCharges.clickChargeRow(null,lienAccountNumber,null,null,null,null,null,null,chargePolicyNumber,null,null,null,null,null,null,null);
        bcCommonCharges.clickReturnToCharges();
        bcCommonCharges.clickChargeInformation();
        bcCommonCharges.clickEditDeliveryOptions();
        if(!new GuidewireHelpers(driver).checkIfElementExists(bcCommonCharges.link_ReturnToCharges,5)){
            isEditDeliveryOptionsGood = false;
            System.out.println("EditDeliveryOptions button has a problem from LH account");
        }
        bcCommonCharges.clickReturnToCharges();
        new GuidewireHelpers(driver).selectOKOrCancelFromPopup(OkCancel.OK);

        new BCSearchAccounts(driver).searchAccountByPolicyNumber(chargePolicyNumber);

        bcAccountMenu.clickBCMenuCharges();
        bcCommonCharges.sortChargeTableByRecentDate();
        bcCommonCharges.clickChargeRow(null,lienAccountNumber,null,null,null,null,null,null,chargePolicyNumber,null,null,null,null,null,null,null);
        bcCommonCharges.clickChargeInformation();
        bcCommonCharges.clickEditDeliveryOptions();

        if(!new GuidewireHelpers(driver).checkIfElementExists(bcCommonCharges.link_ReturnToCharges,5)){
            isEditDeliveryOptionsGood = false;
            System.out.println(" EditDeliveryOptions button has a problem from Insured account");
        }

        assertTrue(isEditDeliveryOptionsGood,"EditDeliveryOptions has problem populating either from Insured or LH , please check the logs");

    }
}
