package guidewire.claimcenter.systemMaintenance;

import gwclockhelpers.ApplicationOrCenter;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.ab.pages.ABIDs;
import repository.cc.framework.gw.cc.pages.CCIDs;
import repository.cc.framework.init.Environments;
import repository.cc.framework.integrations.faker.utils.FullAddress;

/**
 * @Author Denver Hysell
 * @Requirement
 * @RequirementsLink &lt;a href="http:// "&gt;https://rally1.rallydev.com/#/203558471292ud/detail/userstory/267133936348&lt;/a&gt;
 * @Description Covers US17101 Vendor's Going to Pending Queue in AB
 * @DATE 11/29/2018
 */

public class US17101_VendorPendingQueueAB extends BaseOperations {

    @BeforeMethod
    public void setupTest() {
        super.randomClaimTestSetup();
    }

    @Test
    public void claimVendorPendingQueueTest() {
        interact.withElement(CCIDs.Claim.SideMenu.PARTIES_INVOLVED).click();
        interact.withElement(CCIDs.Claim.Contacts.ADD_CONTACT).click();

        // New vendor information
        String vendorName = faker.company().name();
        String vendorTIN = faker.idNumber().ssnValid().replace("-", "");
        vendorTIN = vendorTIN.replaceFirst(vendorTIN.substring(0, 1), "9");

        FullAddress fullAddress = new FullAddress(faker);

        // Add new contact as vendor
        interact.withTexbox(CCIDs.SearchAddressBook.NAME).fill(vendorName);
        interact.withElement(CCIDs.SearchAddressBook.SEARCH).click();
        interact.withElement(CCIDs.SearchAddressBook.CREATE_NEW).click();
        interact.withElement(CCIDs.SearchAddressBook.COMPANY).click();
        interact.withTexbox(CCIDs.SearchAddressBook.NewCompany.NAME).fillIfEmpty(vendorName);
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        interact.withTable(CCIDs.SearchAddressBook.NewCompany.ROLES_TABLE).getRows().get(0).getCell(2).click();
        interact.withTexbox(CCIDs.SearchAddressBook.NewCompany.ROLE).fill("Vendor");
        interact.withElement(CCIDs.ESCAPE_CLICKER).click();
        interact.withTexbox(CCIDs.SearchAddressBook.NewCompany.TIN).fill(vendorTIN);
        interact.withSelectBox(CCIDs.SearchAddressBook.NewCompany.TYPE).select("Salvage Vendor");
        interact.withTexbox(CCIDs.SearchAddressBook.NewCompany.ADDRESS1).fill(fullAddress.getStreetAddress());
        interact.withTexbox(CCIDs.SearchAddressBook.NewCompany.CITY).fill(fullAddress.getCity());
        interact.withSelectBox(CCIDs.SearchAddressBook.NewCompany.STATE).select(fullAddress.getState());
        interact.withTexbox(CCIDs.SearchAddressBook.NewCompany.ZIP_CODE).fill(fullAddress.getZip());
        interact.withSelectBox(CCIDs.SearchAddressBook.NewCompany.PRIMARY_ADDRESS_TYPE).select("1099");
        interact.withTexbox(CCIDs.SearchAddressBook.NewCompany.MOBILE).fill(faker.phoneNumber().cellPhone());
        interact.withElement(CCIDs.SearchAddressBook.NewCompany.UPDATE).click();
        interact.withElement(CCIDs.SearchAddressBook.RETURN_TO_CONTACTS).click();

        // Logout and close ClaimCenter instance
        cc.logout();
        cc.closeWindows(this.getDriver());

        // Login to contact manager
        super.initOn(ApplicationOrCenter.ContactManager, Environments.DEV);
        interact.withTexbox(ABIDs.Login.USER_NAME).fill("knlindauer");
        interact.withTexbox(ABIDs.Login.PASSWORD).fill("gw");
        interact.withElement(ABIDs.Login.LOG_IN).click();

        // Search for vendor and approve.  Fail if not found.
        interact.withElement(ABIDs.NavBar.SEARCH).click();
        interact.withElement(ABIDs.SideMenu.PENDING_CHANGES).click();
        interact.withElement(ABIDs.CCPendingChanges.CREATES).click();

        Assert.assertTrue(interact.withTable(ABIDs.CCPendingChanges.Creates.PENDING_CHANGES).getRowWithText(vendorName).isPresent(), "Selected Vendor is not visible in search.");
        interact.withTable(ABIDs.CCPendingChanges.Creates.PENDING_CHANGES).getRowWithText(vendorName).click();
        interact.withElement(ABIDs.CCPendingChanges.Creates.APPROVE).click();
        interact.withElement(ABIDs.CCPendingChanges.Creates.OK).click();

        // Search for contact and confirm history item
        interact.withElement(ABIDs.SideMenu.SEARCH).click();
        interact.withTexbox(ABIDs.AdvancedSearch.NAME_LAST_NAME).fill(vendorName);
        interact.withElement(ABIDs.AdvancedSearch.SEARCH).click();
        interact.withTable(ABIDs.AdvancedSearch.SEARCH_RESULTS).getRowWithText(vendorName).getCellByText(vendorName).clickLink();
        interact.withElement(ABIDs.Contact.HISTORY_TAB).click();
        String description = interact.withTable(ABIDs.Contact.History.CONTACT_HISTORY).getRowWithText("Pending Create Approved").getCell(3).screenGrab();

        Assert.assertTrue(description.contains("Contact accepted") && description.contains("Claims Pending Queue"));
    }
}
