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
import repository.gw.helpers.TableUtils;

public class VendorTransfer extends BasePage {

    private WebDriver driver;

    public VendorTransfer(WebDriver driver) {
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


    public String getAddress() {
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

    public String transferContactNoAddress(String transferContactName, String address, String role) throws Exception {
        findTransferContact(transferContactName, address, role);
        String vendorAddress = getAddress();
        clickTransfer();
        return vendorAddress;
    }

    public void transferAcct(String transferContactName, String address, String role) throws Exception {
        findTransferContact(transferContactName, address, role);
        setWithActivity(true);
        clickTransfer();
        repository.ab.contact.ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.getContactPageTitle();
    }


}
