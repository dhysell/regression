package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.NoExceptions2;

import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.DesktopDisbursements;
import repository.bc.desktop.DesktopSuspensePayments;
import repository.bc.wizards.CreateDisbursementWizard;
import repository.bc.wizards.CreateNewSuspensePaymentWizard;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ActivityQueuesBillingCenter;
import repository.gw.enums.SuspensePaymentStatus;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import repository.pc.topmenu.TopMenuPC;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.Date;
import static org.testng.Assert.assertTrue;


@Test(groups = {"ClockMove", "BillingCenter"})
public class DE8024_SuspensePaymentDisbursementsWrongQueueNoExceptions extends BaseTest {

    private WebDriver driver;
    private double suspenseAmountPL = NumberUtils.generateRandomNumberInt(10,500);
    private double suspenseAmountBOP = NumberUtils.generateRandomNumberInt(501,999);
    private String accountNumberPL = String.valueOf(NumberUtils.generateRandomNumberInt(100000,499999))+"-001";
    private String accountNumberBOP = String.valueOf(NumberUtils.generateRandomNumberInt(500000,899999))+"-008";


    private void createDisbursementOnSuspensePayment(Date suspenseDate, Date dateDisbursement, SuspensePaymentStatus suspensePaymentStatus, String accountNumber, double amount){
        new DesktopSuspensePayments(driver).setCheckBox(suspenseDate, null, suspensePaymentStatus, null, accountNumber, null, null, null, null, amount, null, true);
        new DesktopSuspensePayments(driver).clickCreateDisbursement();
        CreateDisbursementWizard disbursementWizard = new CreateDisbursementWizard(driver);
        disbursementWizard.setDisbursementDueDate(dateDisbursement);
        disbursementWizard.selectReasonForDisbursement(repository.gw.enums.DisbursementReason.Other);
        disbursementWizard.fillOutPayeeInfo();
        disbursementWizard.clickNext();
        disbursementWizard.clickFinish();
    }


    @Test
    public void createSuspensePaymentOnBOPAndCreateDisbursement() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        ARUsers arUserClericalAdvanced , arUserManager = new ARUsers();
        arUserClericalAdvanced = ARUsersHelper.getRandomARUserByRole(ARUserRole.Billing_Clerical_Advanced);
        arUserManager = ARUsersHelper.getRandomARUserByRole(ARUserRole.Billing_Manager);

        ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 1);
        Date dateSuspensePayment = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
        Date dateDisbursement = DateUtils.dateAddSubtract(dateSuspensePayment, repository.gw.enums.DateAddSubtractOptions.Day,1);

        new Login(driver).login(arUserClericalAdvanced.getUserName(), arUserClericalAdvanced.getPassword());
        new TopMenuPC(driver).clickDesktopTab();
        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
        desktopMenu.clickDesktopMenuSuspensePayments();
        DesktopSuspensePayments desktopSuspensePayments = new DesktopSuspensePayments(driver);
        desktopSuspensePayments.clickNew();

        // Create PL Suspense and verify
        new CreateNewSuspensePaymentWizard(driver).createNewSuspensePayment(dateSuspensePayment,suspenseAmountPL, repository.gw.enums.PaymentInstrumentEnum.ACH_EFT,accountNumberPL);
        assertTrue(desktopSuspensePayments.verifyRecentlyMadeSuspensePayment(dateSuspensePayment,null, SuspensePaymentStatus.Open,null,accountNumberPL,null,null,null,null,suspenseAmountPL,null,null), "Couldn't find the suspense payment with amount of $ "+suspenseAmountPL);

        // Create BOP Suspense and verify
        desktopMenu.clickDesktopMenuSuspensePayments();
        desktopSuspensePayments.clickNew();
        new CreateNewSuspensePaymentWizard(driver).createNewSuspensePayment(dateSuspensePayment,suspenseAmountBOP, repository.gw.enums.PaymentInstrumentEnum.ACH_EFT,accountNumberBOP);
        assertTrue(desktopSuspensePayments.verifyRecentlyMadeSuspensePayment(dateSuspensePayment,null, SuspensePaymentStatus.Open,null,accountNumberBOP,null,null,null,null,suspenseAmountBOP,null,null), "Couldn't find the suspense payment with amount of $ "+suspenseAmountBOP);

        ClockUtils.setCurrentDates(cf, dateDisbursement);
        //Create PL Disbursement
        createDisbursementOnSuspensePayment(dateSuspensePayment, dateDisbursement, SuspensePaymentStatus.Open, accountNumberPL, suspenseAmountPL);

        //Create BOP Disbursement
        createDisbursementOnSuspensePayment(dateSuspensePayment, dateDisbursement, SuspensePaymentStatus.Open, accountNumberBOP, suspenseAmountBOP);

        new GuidewireHelpers(driver).logout();

        // logging in as a manager to verify tho whom the Disbursement is assigned to
        new Login(driver).login(arUserManager.getUserName(), arUserManager.getPassword());
        new TopMenuPC(driver).clickDesktopTab();
        desktopMenu.clickDesktopMenuDisbursements();
        DesktopDisbursements desktopDisbursements = new DesktopDisbursements(driver);
        assertTrue(desktopDisbursements.verifyRecentlyMadeSuspensePayment(dateDisbursement,null,null,null,suspenseAmountPL,null,null,null,null, repository.gw.enums.ActivityQueuesBillingCenter.ARSupervisorFarmBureau),"Failed to Find the Disbursement");
        assertTrue(desktopDisbursements.verifyRecentlyMadeSuspensePayment(dateDisbursement,null,null,null,suspenseAmountBOP,null,null,null,null, ActivityQueuesBillingCenter.ARSupervisorWesternCommunity),"Failed to Find the Disbursement");
    }

}