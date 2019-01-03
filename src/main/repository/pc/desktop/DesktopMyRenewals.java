package repository.pc.desktop;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionStatus;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.workorders.generic.GenericWorkorderComplete;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DesktopMyRenewals extends BasePage {
	
	private TableUtils tableUtils;
	
    public DesktopMyRenewals(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    //////////////////////////////////////////
    // "Recorded Elements" and their XPaths //
    //////////////////////////////////////////

    @FindBy(xpath = "//div[@id='DesktopRenewals:DesktopRenewalsScreen:DesktopRenewalsLV']")
    public WebElement table_DesktopMyRenewals;

    @FindBy(xpath = "//select[contains(@id,'DesktopRenewals:DesktopRenewalsScreen:DesktopRenewalsLV:renewalsFilter')]")
    public WebElement select_DesktopMyRenewalsFilter;

    /////////////////////////////////////////
    // Helper methods for the above xPaths //
    /////////////////////////////////////////


    public WebElement getDesktopMyRenewalsTable() {
        return table_DesktopMyRenewals;
    }


    public WebElement getDesktopMyRenewalsTableRow(String primaryInsured, Date effectiveDate, Date renewalDate, String transactionNumber, TransactionStatus transactionStatus, ProductLineType productLine, String agentName, String underwriterName) {
        StringBuilder xpathBuilder = new StringBuilder();
        if (!(primaryInsured == null)) {
            String primaryInsuredGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyRenewals, "Primary Insured");
            xpathBuilder.append("/parent::tr/td[contains(@class,'" + primaryInsuredGridColumnID + "') and contains(.,'" + primaryInsured + "')]");
        }
        if (!(effectiveDate == null)) {
            String effectiveDateGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyRenewals, "Effective Date");
            xpathBuilder.append("/parent::tr/td[contains(@class,'" + effectiveDateGridColumnID + "') and contains(.,'" + DateUtils.dateFormatAsString("MM/dd/yyyy", effectiveDate) + "')]");
        }
        if (!(renewalDate == null)) {
            String renewalDateGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyRenewals, "Renewal Date");
            xpathBuilder.append("/parent::tr/td[contains(@class,'" + renewalDateGridColumnID + "') and contains(.,'" + DateUtils.dateFormatAsString("MM/dd/yyyy", renewalDate) + "')]");
        }
        if (!(transactionNumber == null)) {
            String transactionNumberGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyRenewals, "Transaction #");
            xpathBuilder.append("/parent::tr/td[contains(@class,'" + transactionNumberGridColumnID + "') and contains(.,'" + transactionNumber + "')]");
        }
        if (!(transactionStatus == null)) {
            String transactionStatusGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyRenewals, "Status");
            xpathBuilder.append("/parent::tr/td[contains(@class,'" + transactionStatusGridColumnID + "') and contains(.,'" + transactionStatus.getValue() + "')]");
        }
        if (!(productLine == null)) {
            String productLineGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyRenewals, "Product");
            xpathBuilder.append("/parent::tr/td[contains(@class,'" + productLineGridColumnID + "') and contains(.,'" + productLine.getValue() + "')]");
        }
        if (!(agentName == null)) {
            String agentNameGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyRenewals, "Agent");
            xpathBuilder.append("/parent::tr/td[contains(@class,'" + agentNameGridColumnID + "') and contains(.,'" + agentName + "')]");
        }
        if (!(underwriterName == null)) {
            String underwriterNameGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyRenewals, "Underwriter");
            xpathBuilder.append("/parent::tr/td[contains(@class,'" + underwriterNameGridColumnID + "') and contains(.,'" + underwriterName + "')]");
        }

        xpathBuilder.replace(0, 9, ".//");
        WebElement tableRow = table_DesktopMyRenewals.findElement(By.xpath(xpathBuilder.toString()));
        return tableRow;
    }


    public String getPolicyInRenewal(TransactionStatus renewalStatus) {
        List<WebElement> viableRenewalList = new ArrayList<>();
        for (int i = 0; i < (tableUtils.getRowCount(table_DesktopMyRenewals)); i++) {
            String transactionStatusGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyRenewals, "Status");
            WebElement editBox_RenewalStatus = table_DesktopMyRenewals.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + i + "')]/td[contains(@class,'" + transactionStatusGridColumnID + "')]/div"));
            if (editBox_RenewalStatus.getText().equalsIgnoreCase(renewalStatus.getValue())) {
                viableRenewalList.add(editBox_RenewalStatus);
            }
        }
        if (viableRenewalList.size() < 1) {
            tableUtils.sortByHeaderColumn(table_DesktopMyRenewals, "Status");
            tableUtils.sortByHeaderColumn(table_DesktopMyRenewals, "Status");
            for (int i = 0; i < (tableUtils.getRowCount(table_DesktopMyRenewals)); i++) {
                String transactionStatusGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopMyRenewals, "Status");
                WebElement editBox_RenewalStatus = table_DesktopMyRenewals.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + i + "')]/td[contains(@class,'" + transactionStatusGridColumnID + "')]/div"));
                if (editBox_RenewalStatus.getText().equalsIgnoreCase(renewalStatus.getValue())) {
                    viableRenewalList.add(editBox_RenewalStatus);
                }
            }
        }
        if (viableRenewalList.size() < 1) {
            systemOut("Sorry, a viable renewal in renewing status could not be found. Test cannot continue.");
            throw new SkipException("Sorry, a viable renewal in renewing status could not be found. Test cannot continue.");
//			Assert.fail("Sorry, a viable renewal in renewing status could not be found. Test cannot continue.");
        }
        tableUtils.clickLinkInTableByRowAndColumnName(table_DesktopMyRenewals, tableUtils.getRowNumberFromWebElementRow(viableRenewalList.get(NumberUtils.generateRandomNumberInt(0, (viableRenewalList.size() - 1)))), "Transaction #");

        GenericWorkorderComplete genericWorkorder = new GenericWorkorderComplete(getDriver());
        return genericWorkorder.getPolicyNumber();
    }


    public void setDesktopMyRenewalsStatusResultsFilter(String statusToSelect) {
        waitUntilElementIsVisible(select_DesktopMyRenewalsFilter);
        new Select(select_DesktopMyRenewalsFilter).selectByVisibleText(statusToSelect);
    }

}
