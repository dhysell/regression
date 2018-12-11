package scratchpad.sai;


import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.helpers.TableUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;

import java.util.ArrayList;
import java.util.List;

public class NewTR2 extends BaseTest {

    public List<String> getPolicyNumbers() {

        List<String> policyNumbers = new ArrayList<>();
        policyNumbers.add("");
        policyNumbers.add("");
        policyNumbers.add("");

        return policyNumbers;
    }

    private WebDriver driver;

    @Test
    public void SquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter, "STAB04 (Anthony)");
        driver = buildDriver(cf);

        new Login(driver).login("skane", "gw");

        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
        desktopMenu.clickDesktopMenuActionsMultiplePayment();
        DesktopActionsMultiplePayment multiplePaymentsPage = new DesktopActionsMultiplePayment(driver);


        TableUtils tableUtils = new TableUtils(driver);
        int rowCount = tableUtils.getRowCount(multiplePaymentsPage.table_DesktopActionsMultiPaymentEntry);

        for (int i = 1; i <= rowCount; i++) {
            if (i % 3 == 0) {
                multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(i, "01-295977-01", repository.gw.enums.PaymentInstrumentEnum.Check, Double.valueOf(i));
            } else if (i % 2 == 0) {
                multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(i, "01-295977-01", repository.gw.enums.PaymentInstrumentEnum.Check_Cash_Equivalent, Double.valueOf(i));
            } else {
                multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(i, "01-295977-01", repository.gw.enums.PaymentInstrumentEnum.Cash, Double.valueOf(i));
            }

        }

        multiplePaymentsPage.clickAdd();
        rowCount = tableUtils.getRowCount(multiplePaymentsPage.table_DesktopActionsMultiPaymentEntry);
        for (int i = 51; i <= rowCount - 20; i++) {
            if (i % 3 == 0) {
                multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(i, "08-291384-01" , repository.gw.enums.PaymentInstrumentEnum.Check, Double.valueOf(i));
            } else if (i % 2 == 0) {
                multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(i, "08-291384-01", repository.gw.enums.PaymentInstrumentEnum.Check_Cash_Equivalent, Double.valueOf(i));
            } else {
                multiplePaymentsPage.fillOutNextLineOnMultiPaymentTable(i, "08-291384-01", PaymentInstrumentEnum.Cash, Double.valueOf(i));
            }
        }

        multiplePaymentsPage.clickNext();
        multiplePaymentsPage.clickFinish();

/*

 Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber("bswindle", "gw", "003706");

        AdditionalInterest additionalInterest = new AdditionalInterest(ContactSubType.Company);
        additionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
        additionalInterest.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIProperty);
        additionalInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
        additionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        additionalInterest.setLoanContractNumber(loanNumber);

        PolicyLocation location = new PolicyLocation();
        PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        location1Property1.setBuildingAdditionalInterest(additionalInterest);

        this.myPolicyObj = new GeneratePolicy(driver);

        AccountSummaryPC accountSummaryPC = new AccountSummaryPC(driver);
        accountSummaryPC.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.setPolicyInfoEffectiveDate(DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 2)));

        sideMenu.clickSideMenuLineSelection();


        GenericWorkorderLineSelection GWLine = new GenericWorkorderLineSelection(driver);
        GWLine.checkPersonalPropertyLine(true);

        sideMenu.clickSideMenuQualification();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);


        qualificationPage.setSquireHOFullTo(false, "I was in a biker gang staying in motels, and not a homeowner");
        qualificationPage.setSquireGLFullTo(false);

        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation GenericWorkorderSquirePropertyAndLiabilityLocation = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);

        GenericWorkorderSquirePropertyAndLiabilityLocation.addNewOrSelectExistingLocationFA(PLLocationType.Address, location.getAddress(), 1, 1);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail genericWorkorderSquirePropertyAndLiabilityPropertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        genericWorkorderSquirePropertyAndLiabilityPropertyDetail.fillOutPLPropertyQQ(true, location1Property1, SectionIDeductible.FiveHundred, location);

        GenericWorkorderSquirePropertyAndLiabilityCoverages genericWorkorderSquirePropertyAndLiabilityCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        genericWorkorderSquirePropertyAndLiabilityCoverages.fillOutPropertyAndLiabilityCoverages(myPolicyObj);


*/
    }

}
