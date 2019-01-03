package repository.pc.workorders.generic;

import services.broker.objects.lexisnexis.clueproperty.response.actual.CluePersonalProperty;
import services.broker.objects.lexisnexis.generic.request.ReportType;
import services.broker.services.lexisnexis.ServiceLexisNexis;
import repository.driverConfiguration.BasePage;
import services.enums.Broker;
import repository.gw.elements.Guidewire8Select;
import repository.gw.generate.custom.CLUEPropertyInfo;
import repository.gw.generate.custom.UIClaimReportedPayoutReserve;
import repository.gw.generate.custom.UIPropertyClaimReported;
import repository.gw.helpers.GuidewireHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.LexisNexis;
import persistence.globaldatarepo.helpers.LexisNexisHelper;

import java.util.ArrayList;
import java.util.List;

public class GenericWorkorderSquireCLUEProperty extends BasePage {

    private WebDriver driver;

    public GenericWorkorderSquireCLUEProperty(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[contains(@id, ':LineWizardStepSet:HOWizardStepGroup:HOPriorLossExtScreen:HOPriorLossExtPanelSet:LossListDetailPanel:LossesLV_tb:ClueButton') or contains(@id, ':LineWizardStepSet:HOWizardStepGroup:HOPriorLossExtScreen:HOPriorLossExtPanelSet:LossListDetailPanel:ClueButton-btnEl')]")
    private WebElement button_RetrieveCLUE;


    @FindBy(xpath = "//*[contains(@id, ':LineWizardStepSet:HOWizardStepGroup:HOPriorLossExtScreen:HOPriorLossExtPanelSet:OrderWithConfirmationID')]")
    private WebElement button_OrderPropertyHistory;

    @FindBy(xpath = "//label[contains(@id,':HOPriorLossExtScreen:HOPriorLossExtPanelSet:APPropReportLabelClaimNotFound')]")
    private WebElement noClaims_PropertyHistory;

    @FindBy(xpath = "//label[contains(@id,':HOPriorLossExtScreen:HOPriorLossExtPanelSet:LossHistoryInputSet:0')]")
    private WebElement noLoss_PropertyHistory;

    public Guidewire8Select select_OrderReportFor() {
        return new Guidewire8Select(driver, "//table[contains(@id,':LineWizardStepSet:HOWizardStepGroup:HOPriorLossExtScreen:HOPriorLossExtPanelSet:LossListDetailPanel:ClueContact-triggerWrap')]");
    }


    public void clickRetrieveCLUE() {
        clickWhenClickable(button_RetrieveCLUE);
    }

    public void clickOrderPropertyHostory() {

        clickWhenClickable(button_OrderPropertyHistory);
    }



    public boolean checkRetrieveCLUE() {
        return checkIfElementExists(button_RetrieveCLUE, 1000);
    }


    public void setOrderReportFor(String name) {
        Guidewire8Select mySelect = select_OrderReportFor();
        mySelect.selectByVisibleTextPartial(name);
    }


    public CLUEPropertyInfo clickRetrieveCLUEAndAlsoValidateDirectlyFromBrokerForComparison(String firstName, String middleName, String lastName) throws Exception {

        clickRetrieveCLUE();
        boolean printRequestXMLToConsole = false;
        boolean printResponseXMLToConsole = true;
        Broker conn = Broker.valueOf(new GuidewireHelpers(getDriver()).getCurrentCenter().getValue());
        systemOut(conn.name());
        ServiceLexisNexis testService = new ServiceLexisNexis();
        LexisNexis[] randomCustomers = {LexisNexisHelper.getCustomerByName(firstName, middleName, lastName)};
        CluePersonalProperty testResponse = testService.orderCLUEProperty(testService.setUpTestOrder(ReportType.CLUE_PROPERTY, randomCustomers), conn, printRequestXMLToConsole, printResponseXMLToConsole);

        CLUEPropertyInfo toReturn = new CLUEPropertyInfo();
        toReturn.setBrokerReport(testResponse);

        try {
            toReturn.setClaimsReported(getClaimsReported());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturn;
    }



    @FindBy(xpath = "//div[contains(@id,':LineWizardStepSet:HOWizardStepGroup:HOPriorLossExtScreen:HOPriorLossExtPanelSet:LossListDetailPanel:LossesLV-body')]//table")
    private WebElement table_ClaimsReport;

    @FindBy(xpath = "//div[contains(@id,':LineWizardStepSet:HOWizardStepGroup:HOPriorLossExtScreen:HOPriorLossExtPanelSet:SelectedReportListPanelLV:SelectedClaimListDetailPanel:SelectedClaimPanelLV-body')]//table")
    private WebElement table_VeriskClaimsReport;


    public WebElement getVeriskCalimTable() {
        return table_VeriskClaimsReport;
    }

    private List<WebElement> getPropertyHistoryClaimsReportedRows() {
        return table_VeriskClaimsReport.findElements(By.xpath(".//tr"));
    }

    private List<WebElement> getClaimsReportedRows() {
        return table_ClaimsReport.findElements(By.xpath(".//tr"));
    }

    @FindBy(xpath = "//div[contains(@id,':LineWizardStepSet:HOWizardStepGroup:HOPriorLossExtScreen:HOPriorLossExtPanelSet:LossListDetailPanel:clueLossDetailsCV:HOPriorLossPaymentExtLV-body')]//table")
    private WebElement table_ClaimsReportPayouts;

    private List<WebElement> getClaimsReportedPayoutsRows() {
        return table_ClaimsReportPayouts.findElements(By.xpath(".//tr"));
    }

    @FindBy(xpath = "//div[contains(@id,':LineWizardStepSet:HOWizardStepGroup:HOPriorLossExtScreen:HOPriorLossExtPanelSet:LossListDetailPanel:clueLossDetailsCV:HOPriorLossDetailsExtDV')]//table")
    private WebElement table_ClaimsReportDetails;

    private String getUIData(String labelName) {
        WebElement toUse = table_ClaimsReportDetails.findElement(By.xpath(".//label[.='" + labelName + "']/parent::td/following-sibling::td/div"));
        return toUse.getText();
    }

    private ArrayList<UIPropertyClaimReported> getClaimsReported() {
        ArrayList<UIPropertyClaimReported> toReturn = new ArrayList<UIPropertyClaimReported>();

        int counter = 1;
        for (WebElement we : getClaimsReportedRows()) {
            UIPropertyClaimReported row = new UIPropertyClaimReported();

            clickWhenClickable(we);
            String weXpath = new GuidewireHelpers(driver).getXpathFromElement(table_ClaimsReport) + "//tr[" + counter + "]";
            WebElement newWe = find(By.xpath(weXpath));

            row.setClaimDate(newWe.findElement(By.xpath(".//td[2]/div")).getText());
            row.setPolicyHolderName(newWe.findElement(By.xpath(".//td[3]/div")).getText());
            row.setPropertyPolicyNumber(newWe.findElement(By.xpath(".//td[4]/div")).getText());
            row.setClaimAge(newWe.findElement(By.xpath(".//td[5]/div")).getText());

            ArrayList<UIClaimReportedPayoutReserve> rowPayouts = new ArrayList<>();
            for (WebElement we2 : getClaimsReportedPayoutsRows()) {
                UIClaimReportedPayoutReserve row2 = new UIClaimReportedPayoutReserve();

                row2.setClaimType(we2.findElement(By.xpath(".//td[2]/div")).getText());
                row2.setStatus(we2.findElement(By.xpath(".//td[3]/div")).getText());
                row2.setPaymentAmount(we2.findElement(By.xpath(".//td[4]/div")).getText());

                rowPayouts.add(row2);
            }

            row.setPayoutsReserves(rowPayouts);
            row.setSubClaimDate(getUIData("Claim Date"));
            row.setClaimStatus(getUIData("Claim Status"));
            row.setClaimScope(getUIData("Claim Scope"));
            row.setClaimType(getUIData("Claim Type"));
            row.setClaimNumber(getUIData("Claim Number"));
            row.setPaymentDate(getUIData("Payment Date"));
            row.setSubClaimAge(getUIData("Claim Age"));
            row.setDisputeDate(getUIData("Dispute Date"));
            row.setCatastropheIndicator(getUIData("Catastrophe Indicator"));
            row.setMortgageCompany(getUIData("Mortgage Company"));
            row.setMortgageNumber(getUIData("Mortgage Number"));
            row.setAmBestNumber(getUIData("Am Best Number"));
            row.setInsurer(getUIData("Insurer"));
            row.setSubPolicyHolderName(getUIData("Policy Holder Name"));
            row.setAddress(getUIData("Address"));
            row.setCity(getUIData("City"));
            row.setState(getUIData("State"));
            row.setZip(getUIData("Zip"));
            row.setAddressType(getUIData("Address Type"));
            row.setPropertyPolicyNumber(getUIData("Property Policy Number"));
            row.setPropertyType(getUIData("Property Type"));
            row.setLocationOfLoss(getUIData("Location Of Loss"));
            row.setSearchMatchIndicator(getUIData("Search Match Indicator"));
            row.setManuallyAddedLoss(getUIData("Manually Added Loss"));

            toReturn.add(row);

            counter++;
        }

        return toReturn;
    }

    @FindBy(xpath = "//div[contains(@id,':SelectedClaimListDetailPanel:APPropertyClaimDV')]//table")
    private WebElement table_SelectedClaimsReportDetails;

    private String getUIClaimData(String labelName) {
        WebElement toUse = table_SelectedClaimsReportDetails.findElement(By.xpath(".//label[.='" + labelName + "']/parent::td/following-sibling::td/div"));
        return toUse.getText();
    }

    @FindBy(xpath = "//div[contains(@id,':HOPriorLossExtScreen:HOPriorLossExtPanelSet:SelectedReportListPanelLV:SelectedClaimListDetailPanel:3-body')]//table")
    private WebElement table_Losses;

    private List<WebElement> getLossesRows() {
        return table_Losses.findElements(By.xpath(".//tr"));
    }

    public ArrayList<UIPropertyClaimReported> getPropertyHistoryClaimsReported() {
        ArrayList<UIPropertyClaimReported> toReturn = new ArrayList<UIPropertyClaimReported>();

        int counter = 1;
        List<WebElement> tableRows =getPropertyHistoryClaimsReportedRows();


        for (WebElement we : tableRows) {
            UIPropertyClaimReported row = new UIPropertyClaimReported();


            String weXpath = new GuidewireHelpers(driver).getXpathFromElement(table_VeriskClaimsReport) + "//tr[" + counter + "]";
            clickWhenClickable(By.xpath(weXpath));
            WebElement newWe = find(By.xpath(weXpath));

            row.setClaimNumber(newWe.findElement(By.xpath(".//td[1]/div")).getText());

            row.setClaimDate(newWe.findElement(By.xpath(".//td[2]/div")).getText()); //lossdate
            row.setPropertyPolicyNumber(newWe.findElement(By.xpath(".//td[3]/div")).getText());
            row.setCarrierName(newWe.findElement(By.xpath(".//td[4]/div")).getText());
            row.setLossAmmount(newWe.findElement(By.xpath(".//td[5]/div")).getText());
            row.setCatLoss(newWe.findElement(By.xpath(".//td[6]/div")).getText());
//claim details

            row.setLossDate(getUIClaimData("Loss Date"));
            row.setPolicyTypeCode(getUIClaimData("Policy Type Code"));

//losses
            ArrayList<UIClaimReportedPayoutReserve> rowPayouts = new ArrayList<>();
            for (WebElement we2 : getLossesRows()) {
                UIClaimReportedPayoutReserve row2 = new UIClaimReportedPayoutReserve();

                row2.setClaimType(we2.findElement(By.xpath(".//td[1]/div")).getText());
                row2.setStatus(we2.findElement(By.xpath(".//td[3]/div")).getText());
                row2.setPaymentAmount(we2.findElement(By.xpath(".//td[6]/div")).getText());

                rowPayouts.add(row2);
            }

            row.setPayoutsReserves(rowPayouts);

            toReturn.add(row);

            counter++;
        }

        return toReturn;
    }
    public boolean isNoMatchesFound(){

        boolean noLoss= checkIfElementExists(noLoss_PropertyHistory, 10) &&  noLoss_PropertyHistory.getText().equals("No Loss History");
        boolean noClaims= checkIfElementExists(noClaims_PropertyHistory, 10) &&  noClaims_PropertyHistory.getText().equals("Claims Activity Profiler: No Matches");
        return noLoss&&noClaims;
    }

}
