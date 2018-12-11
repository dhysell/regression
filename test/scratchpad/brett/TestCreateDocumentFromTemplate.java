package scratchpad.brett;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.common.actions.BCCommonActionsCreateNewDocumentFromTemplate;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.enums.DocumentType;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

@SuppressWarnings("unused")
public class TestCreateDocumentFromTemplate extends BaseTest {
    private String accountNumber = "";
    public ARUsers arUser = new ARUsers();


    public List<DocumentType> documentTypeList = new ArrayList<DocumentType>() {
        private static final long serialVersionUID = 1L;

        {
            this.add(DocumentType.Ad_Hoc_Document);
            this.add(DocumentType.Additional_Interest_Bill);
            this.add(DocumentType.Balance_Due_Partial_Cancel);
            this.add(DocumentType.Cash_Only_Letter);
            this.add(DocumentType.Cash_Only_Warning_Letter);
            this.add(DocumentType.Final_Notice_Balance_Due);
            this.add(DocumentType.First_Notice_Balance_Due);
            this.add(DocumentType.Notice_Of_Withdrawal);
            this.add(DocumentType.Other);
            this.add(DocumentType.Scheduled_Payment_Plan_Quarterly);
            this.add(DocumentType.Receipt_Of_Payment_On_Cancelled_Policy);
            this.add(DocumentType.Shortage_Bill);
        }
    };

    public DocumentType getRandomValueFromList(List<DocumentType> array) {
        if (array.size() < 1) {
            Assert.fail("The passed-in array is empty.");
        }

        int randomOption = NumberUtils.generateRandomNumberInt(0, array.size() - 1);
        return array.get(randomOption);
    }

    @Test
    public void testLienholderPaymentScreen() throws Exception {
        try {
            this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        } catch (Exception e) {
            System.out.println(e);
            Assert.fail("While attempting to get the AR User, the hibernate connection failed.");
        }

        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        WebDriver driver = buildDriver(cf);
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());

        BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
        this.accountNumber = accountSearch.findAccountInGoodStanding("08-");


        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuActionsCreateNewDocumentFromTemplate();


        BCCommonActionsCreateNewDocumentFromTemplate newDocument = new BCCommonActionsCreateNewDocumentFromTemplate(driver);
        String documentName = newDocument.selectDocumentTemplate(getRandomValueFromList(documentTypeList));
        newDocument.clickCreateDocument();


        // Blocked by a Defect for the time being.
    }
}
