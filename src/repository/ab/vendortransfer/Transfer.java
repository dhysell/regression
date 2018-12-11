package repository.ab.vendortransfer;


import com.idfbins.enums.OkCancel;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;

public class Transfer extends BasePage {

    private WebDriver driver;

    public Transfer(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[contains(@id, 'ContactTransfer:TransferPanelSet:ValidContact:SelectValidContact')]")
    private WebElement button_TransferToContactSearch;

    @FindBy(xpath = "//span[contains(@id, 'ContactTransfer:TransferPanelSet_tb:ToolbarButtonTransfer-btnEl') or contains(@id, 'ContactTransfer:TransferPanelSet:ToolbarButtonTransfer-btnEl')]")
    private WebElement button_VendorTransferTransfer;

    @FindBy(xpath = "//div[contains(@id, 'ContactTransfer:TransferPanelSet:1')]")
    private WebElement div_VendorTransferAddress;

    public Guidewire8RadioButton radio_VendorTransferWithActivity() {
        return new Guidewire8RadioButton(getDriver(), "//div[contains(@id, 'ContactTransfer:TransferPanelSet:WithActivity-containerEl')]/table");
    }

    public Guidewire8Select select_VendorTransferVendorName() {
        return new Guidewire8Select(driver, "//table[contains(@id,'ContactTransfer:TransferPanelSet:VendorName-triggerWrap')]");
    }

    public Guidewire8Select select_VendorTransferLienNumber() {
        return new Guidewire8Select(driver, "//table[contains(@id,'ContactTransfer:TransferPanelSet:VendorNumber-triggerWrap')]");
    }

    public Guidewire8Select select_VendorTransferVendorType() {
        return new Guidewire8Select(driver, "//table[contains(@id,'ContactTransfer:TransferPanelSet:VendorType-triggerWrap')]");
    }


    public void setAddresses(ArrayList<AddressInfo> addresses) {
        for (AddressInfo address : addresses) {
            new TableUtils(getDriver()).setCheckboxInTableByText(div_VendorTransferAddress, address.getLine1(), true);
        }
    }

    public void setLienholderNumber(String lienNum) {
        Guidewire8Select lienNumber = select_VendorTransferLienNumber();
        lienNumber.selectByVisibleTextPartial(lienNum);
    }

    public void setVendorName(String name) {
        Guidewire8Select vendorNameList = select_VendorTransferVendorName();
        vendorNameList.selectByVisibleTextPartial(name);
    }

    public void setVendorType(String vendorType) {
        Guidewire8Select vendorTypeList = select_VendorTransferVendorType();
        vendorTypeList.selectByVisibleTextPartial(vendorType);
    }

    public String getAddress() {
        waitUntilElementIsVisible(div_VendorTransferAddress);
        int row = new TableUtils(getDriver()).getRowCount(div_VendorTransferAddress);
        return new TableUtils(getDriver()).getCellTextInTableByRowAndColumnName(div_VendorTransferAddress, row, "Address Line 1");
    }

    public void findTransferContact(String transferContactName, String address, String role) throws Exception {
        clickWhenClickable(button_TransferToContactSearch);
        repository.ab.search.AdvancedSearchAB searchTransferContact = new AdvancedSearchAB(driver);
        searchTransferContact.selectAContactByRoleNameAddress(transferContactName, address, role);
    }

    public void setWithActivity(boolean trueFalse) {
        radio_VendorTransferWithActivity().select(trueFalse);
    }

    public void clickTransfer() {
        clickWhenClickable(button_VendorTransferTransfer);
        selectOKOrCancelFromPopup(OkCancel.OK);
    }

    public String transferContactNoAddress(String transferToContactName, String address, String role, String transferFromContactName, String vendorNumber, String vendorType) throws Exception {
        findTransferContact(transferToContactName, address, role);
        String vendorAddress = getAddress();
        setVendorName(transferFromContactName);
        setLienholderNumber(vendorNumber);
        setVendorType(vendorType);
        clickTransfer();
        return vendorAddress;
    }

    public void transferAcct(String transferContactName, String address, String role) throws Exception {
        findTransferContact(transferContactName, address, role);
        setWithActivity(true);
        clickTransfer();
        repository.ab.contact.ContactDetailsBasicsAB contactPage = new repository.ab.contact.ContactDetailsBasicsAB(driver);
        contactPage.getContactPageTitle();
    }

    public repository.ab.contact.ContactDetailsBasicsAB transferLien(String transferContactName, String address, String transferFromName, String role, String lienNumber, ArrayList<AddressInfo> addresses) throws Exception {
        findTransferContact(transferContactName, address, role);
        if (addresses.size() > 0) {
            setAddresses(addresses);
        }
        if (lienNumber != null || lienNumber != "") {
            setLienholderNumber(lienNumber);
        }
        clickTransfer();
        return new ContactDetailsBasicsAB(driver);
    }
}
