package currentProgramIncrement.nonFeatures.Aces;

import org.testng.Assert;
import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.init.Environments;
import repository.gw.enums.ClaimsUsers;
import repository.gw.helpers.NumberUtils;
import gwclockhelpers.ApplicationOrCenter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

/**
 * @Author Denver Hysell
 * @Requirement
 * @RequirementsLink &lt;a href="http:// "&gt;https://rally1.rallydev.com/#/203558471292ud/detail/userstory/266283702112&lt;/a&gt;
 * @Description Make Payments on QCS vendor invoices.
 * @DATE 12/05/2018
 */

/**
 * @Author Denver Hysell
 * @Requirement
 * @RequirementsLink &lt;a href="http:// "&gt;https://rally1.rallydev.com/#/203558471292ud/detail/userstory/266283702112&lt;/a&gt;
 * @Description Make Payments on QCS vendor invoices.
 * @DATE 12/05/2018
 */

public class US16987_QCSVendorPaymentProcessImprovement extends BaseOperations {
    @BeforeTest
    public void setupTest() {
        this.initOn(ApplicationOrCenter.ClaimCenter, Environments.DEV);
        HashMap<String, String> result = interact.withDB.randomResultFromQuerey("select TOP 100 c.ClaimNumber, i.CCCReferenceID from cc_claim c inner join cc_incident i on c.id = i.ClaimID inner join cc_exposure e on e.IncidentID = i.ID where i.CCCReferenceID is not null and c.ClaimNumber is not null order by i.UpdateTime");
        storage.put("ClaimNumber", result.get("ClaimNumber"));
        cc.loginAs(ClaimsUsers.abatts);
        interact.withElement(CCIDs.Desktop.SideMenu.VENDOR_INVOICES).click();
        interact.withElement(CCIDs.Desktop.SideMenu.VendorInvoices.QCS).click();
        interact.withElement(CCIDs.Desktop.VendorInvoices.QCSInvoices.CREATE_NEW_INVOICE).click();
        interact.withTexbox(CCIDs.Desktop.VendorInvoices.QCSInvoices.NewQCSInvoice.TRANSFER_FEE).fill(String.valueOf(NumberUtils.generateRandomNumberInt(5, 15)));
        interact.withTexbox(CCIDs.Desktop.VendorInvoices.QCSInvoices.NewQCSInvoice.CLAIM_NUMBER).fill(result.get("ClaimNumber"));
        interact.withTexbox(CCIDs.Desktop.VendorInvoices.QCSInvoices.NewQCSInvoice.UNIQUE_ID).fill(result.get("CCCReferenceID"));
        interact.withElement(CCIDs.Desktop.VendorInvoices.QCSInvoices.NewQCSInvoice.UPDATE).click();
    }

    @Test
    public void qcsVendorPayment() {
        interact.withTable(CCIDs.Desktop.VendorInvoices.QCSInvoices.NewQCSInvoice.SEARCH_RESULTS).getRowWithText(storage.get("ClaimNumber")).getCell(0).clickLink();
        interact.withElement(CCIDs.Desktop.VendorInvoices.QCSInvoices.PROCESS).click();

        interact.withSelectBox(CCIDs.Desktop.VendorInvoices.QCSInvoices.QCSInvoiceProcess.ACTION_TYPE).select("Pay on Existing Claim");
        interact.withSelectBox(CCIDs.Desktop.VendorInvoices.QCSInvoices.QCSInvoiceProcess.EXPOSURE).selectRandom();
        List<String> options = interact.withSelectBox(CCIDs.Desktop.VendorInvoices.QCSInvoices.QCSInvoiceProcess.PAYMENT_TYPE).getOptions();
        Assert.assertTrue(options.contains("Supplemental") || options.contains("Final"), "Options missing from Payment Type Select Box.");
        interact.withSelectBox(CCIDs.Desktop.VendorInvoices.QCSInvoices.QCSInvoiceProcess.PAYMENT_TYPE).selectFirstExisting(new String[]{"Final", "Supplemental"});
        interact.withElement(CCIDs.Desktop.VendorInvoices.QCSInvoices.QCSInvoiceProcess.MAKEPAYMENT).click();

        Assert.assertFalse(interact.withOptionalElement((CCIDs.ERROR_MESSAGE)).isPresent());
    }
}
