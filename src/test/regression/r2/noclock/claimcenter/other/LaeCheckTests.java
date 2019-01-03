package regression.r2.noclock.claimcenter.other;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.actionsmenu.ActionsMenu;
import repository.cc.claim.CheckWizard;
import repository.cc.claim.FinancialsTransactions;
import repository.cc.claim.searchpages.AdvancedSearchCC;
import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
@QuarantineClass
public class LaeCheckTests extends BaseTest {
	private WebDriver driver;
    private ClaimsUsers userName = ClaimsUsers.gmurray;
    private String password = "gw";

    private String claimNumber = null;

    @Test
    public void laeCheckTests() throws Exception {

        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);

        new Login(driver).login(userName.toString(), password);
        TopMenu topMenu = new TopMenu(driver);

        AdvancedSearchCC advancedSearch = topMenu.clickSearchTab().clickAdvancedSearch();
        SideMenuCC sideMenu = new SideMenuCC(driver);
        FinancialsTransactions financialTransactions = new FinancialsTransactions(driver);
        claimNumber = advancedSearch.findRandomClaimReadyForCheckWriting();

        ActionsMenu actMenu = new ActionsMenu(driver);
        actMenu.checkAbilityToPay();
        sideMenu.clickFinancialTransactions();

        financialTransactions.selectReserves();

        String costCategory = financialTransactions.getCostCategory(0);

        actMenu.clickActionsButton();
        actMenu.checkTypeMenuPicker("LAE");

        CheckWizard checkWizard = new CheckWizard(driver);
        checkWizard.setPrimaryPayeeName("insured");

        checkWizard.setPrimaryPayeeType("Claimant");

        checkWizard.clickNextButton();

        checkWizard.selectSpecific_ReserveLine(costCategory);

        checkWizard.setPaymentType("Partial");


        List<String> costCatString = Arrays.asList("All Other Costs", "Attorney Fees", "Expert Witness Fees");
        costCatString.sort(Collator.getInstance());
        checkWizard.setChecksLineItems(CheckLineItemType.LAE, CheckLineItemCategory.INDEMNITY, "500");

        checkWizard.clickAddItemButton();

        checkWizard.setChecksLineItems(CheckLineItemType.LAE, CheckLineItemCategory.ATTORNEYFEES, "600");

        checkWizard.clickAddItemButton();

        checkWizard.setChecksLineItems(CheckLineItemType.LAE, CheckLineItemCategory.EXPERTWITNESSFEES, "700");

        checkWizard.clickAddItemButton();

        checkWizard.setChecksLineItems(CheckLineItemType.LAE, CheckLineItemCategory.ALLOTHERCOSTS, "800");
        checkWizard.clickNextButton();

        checkWizard.click_FinishButton();
        checkWizard.validationControl();
        checkWizard.approveChecks(claimNumber);

        sideMenu.clickFinancialTransactions();

        WebElement table = sideMenu.waitUntilElementIsVisible(driver.findElement(By.xpath("//div[contains(@id,':TransactionsLV')]")));

        WebElement row = new TableUtils(driver).getRowInTableByColumnNameAndValue(table, "Check Type", "LAE");

        new TableUtils(driver).clickLinkInSpecficRowInTable(table, new TableUtils(driver).getRowNumberFromWebElementRow(row));


        String checkTableXpath = "//div[contains(@id,':TransactionLineItemsLV')]";

        ArrayList<String> lineCats = new TableUtils(driver).getAllCellTextFromSpecificColumn(driver.findElement(By.xpath(checkTableXpath)), "Line Category");
        lineCats.sort(Collator.getInstance());
        for (int i = 0; i < lineCats.size(); i++) {
            Assert.assertTrue(lineCats.get(i).equals(costCatString.get(i)), lineCats.get(i) + " Didn't match " + costCatString.get(i));
        }

    }

}
