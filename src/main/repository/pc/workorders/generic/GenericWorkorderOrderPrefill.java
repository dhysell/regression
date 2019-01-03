package repository.pc.workorders.generic;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.LexisNexis;
import persistence.globaldatarepo.helpers.LexisNexisHelper;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.GuidewireHelpers;
import services.broker.objects.lexisnexis.generic.request.ReportType;
import services.broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillReport;
import services.broker.services.lexisnexis.ServiceLexisNexis;
import services.enums.Broker;

public class GenericWorkorderOrderPrefill extends GenericWorkorder {

    private WebDriver driver;

    public GenericWorkorderOrderPrefill(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[contains(@id, 'PrefillSubmit')]")
    private WebElement button_prefillSendRequest;

    @FindBy(xpath = "//input[contains(@id, 'PrefillCurrentAddressStreetNumber')]")
    private WebElement textbox_prefillCurrentStreetNumber;

    @FindBy(xpath = "//input[contains(@id, 'PrefillCurrentAddressStreetName')]")
    private WebElement textbox_prefillCurrentStreetName;

    @FindBy(xpath = "//input[contains(@id, 'PrefillCurrentAddressApartmentNum')]")
    private WebElement textbox_prefillCurrentApartment;

    @FindBy(xpath = "//input[contains(@id, 'PrefillCurrentAddressCity')]")
    private WebElement textbox_prefillCurrentCity;

    @FindBy(xpath = "//input[contains(@id, 'PrefillCurrentAddressZip')]")
    private WebElement textbox_prefillCurrentZip;

    @FindBy(xpath = "//input[contains(@id, 'PrefillPriorAddressStreetNumber')]")
    private WebElement textbox_prefillPriorStreetNumber;

    @FindBy(xpath = "//input[contains(@id, 'PrefillPriorAddressStreetName')]")
    private WebElement textbox_prefillPriorStreetName;

    @FindBy(xpath = "//input[contains(@id, 'PrefillPriorAddressApartmentNum')]")
    private WebElement textbox_prefillPriorApartment;

    @FindBy(xpath = "//input[contains(@id, 'PrefillPriorAddressCity')]")
    private WebElement textbox_prefillPriorCity;

    @FindBy(xpath = "//input[contains(@id, 'PrefillPriorAddressZip')]")
    private WebElement textbox_prefillPriorZip;

    @SuppressWarnings("unused")
    private Guidewire8Select currentState() {
        String tableOrDivXpath = "PrefillCurrentAddressState-triggerWrap";
        return new Guidewire8Select(driver, tableOrDivXpath);
    }

    @SuppressWarnings("unused")
    private Guidewire8Select priorState() {
        String tableOrDivXpath = "PrefillPriorAddressState-triggerWrap";
        return new Guidewire8Select(driver,tableOrDivXpath);
    }

    private Guidewire8RadioButton currentAddress() {
        String tableXPath = "//table[contains(@id, 'OrderPrefillReportCurrentAddressInputSet:AtCurrentAddress60Days')]";
        return new Guidewire8RadioButton(driver,tableXPath);
    }


    public void orderDefaultReport() {
        sendRequest();
    }


    public DataprefillReport orderDefaultReportAndRequestFromBroker(String firstName, String middleName, String lastName) throws Exception {

        // TODO:

        sendRequest();

        boolean printRequestXMLToConsole = false;
        boolean printResponseXMLToConsole = true;
        Broker conn = new GuidewireHelpers(getDriver()).getMessageBrokerConnDetails();
        ServiceLexisNexis nexisService = new ServiceLexisNexis();
        LexisNexis[] randomCustomers = {LexisNexisHelper.getCustomerByName(firstName, middleName, lastName)};
        DataprefillReport testResponse = nexisService.orderPrefillPersonal(nexisService.setUpTestOrder(ReportType.AUTO_DATAPREFILL, randomCustomers), conn, printRequestXMLToConsole, printResponseXMLToConsole);
        return testResponse;
    }


    public void orderCustomReport(AddressInfo address) {
        // TODO: set address info
        sendRequest();
    }


    public void orderCustomReport(AddressInfo currentAddress, boolean hasPriorAddress, AddressInfo priorAddress) {
        // TODO: set current address info
        isAddressCurrent(hasPriorAddress);
        // TODO: set prior address
        sendRequest();
    }


    public void getPrefillResults() {
        // TODO: scrape screen for information
    }

    private void sendRequest() {
        clickWhenClickable(button_prefillSendRequest);
        selectOKOrCancelFromPopup(OkCancel.OK);
    }

    // setters

    // TODO: add rest of editbox and select setters

    public void setCurrentStreetNumber(String streetNum) {
        setText(textbox_prefillCurrentStreetNumber, streetNum);
    }


    public void isAddressCurrent(boolean yesNo) {
        Guidewire8RadioButton currentAddressRadio = currentAddress();
        currentAddressRadio.select(yesNo);
    }
}
