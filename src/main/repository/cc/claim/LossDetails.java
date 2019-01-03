package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.cc.sidemenu.SideMenuCC;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.WaitUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LossDetails extends BasePage {

    private WaitUtils waitUtils;
    private WebDriver driver;

    public LossDetails(WebDriver driver) {
        super(driver);
        this.waitUtils = new WaitUtils(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//textArea[contains(@id,':Description-inputEl')]")
    public WebElement editBox_LossDescription;

    @FindBy(xpath = "//a[contains(@id, ':Cancel')]")
    public WebElement button_Cancel;

    @FindBy(xpath = "//a[@id='FNOLWizard:Finish']")
    public WebElement button_Finish;

    private Guidewire8Select selectLitigationStatus() {
        return new Guidewire8Select(
                driver, "//table[contains(@id,'Status_LitigationStatus-triggerWrap')]");
    }

    @FindBy(css = "span[id*=':AddOtherButton-btnInnerEl']")
    private WebElement buttonAddOtherIncident;

    @FindBy(css = "div[id*='LossDetailsDV:PoliceReportsLNLV-body'] div table")
    private WebElement tablePoliceReports;

    @FindBy(css = "span[id*='ClaimLossDetails:ClaimLossDetailsScreen:ttlBar']")
    private WebElement pageHeader;

    private void clickPageHeader() {
        waitUtils.waitUntilElementIsVisible(pageHeader, 10);
        pageHeader.click();
    }

    public int getNumberPoliceReports() {
        clickPageHeader();
        sendArbitraryKeys(Keys.PAGE_DOWN);
        sleep(1); //Used to ensure that we get two full page down clicks.
        sendArbitraryKeys(Keys.PAGE_DOWN);
        waitUtils.waitUntilElementIsVisible(tablePoliceReports, 10);
        return tablePoliceReports.findElements(By.cssSelector("tr")).size();
    }

    public boolean confirmPoliceReportStatus(String status, int row) {

        waitUtils.waitUntilElementIsVisible(tablePoliceReports);
        String cellText = tablePoliceReports.findElement(By.cssSelector("tr:nth-child(" + row + ") td:nth-child(5)")).getText();

        return cellText.equalsIgnoreCase(status);
    }

    public repository.cc.claim.Incidents clickAddOtherIncident() {
        waitUtils.waitUntilElementIsClickable(buttonAddOtherIncident, 10);
        buttonAddOtherIncident.click();
        waitForPageLoad();
        return new Incidents(getDriver());
    }

    public void setLitigationStatus(String selection) {
        waitUtils.waitUntilElementIsVisible(By.cssSelector("input[id*='Status_LitigationStatus-inputEl']"));
        clickProductLogo();
        selectLitigationStatus().selectByVisibleText(selection);
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
    }

    public Guidewire8Select select_LossCause() {
        return new Guidewire8Select(
                driver, "//table[@id='FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:LossDetailsAddressDV:Claim_LossCause-triggerWrap']");
    }

    public Guidewire8Select select_LossRouter() {
        return new Guidewire8Select(
                driver, "//table[@id='FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:LossDetailsAddressDV:Claim_LossRouter-triggerWrap']");
    }

    public Guidewire8Select select_Location() {
        return new Guidewire8Select(driver, "//table[contains(@id,':LossDetailsAddressDetailInputSet:LossLocation_Name-triggerWrap')]");
    }

    private void setLossLocation() {

        List<String> options = select_Location().getList();

        int count = options.size();

        for (int i = 0; i < count; i++) {
            if (options.get(i).equalsIgnoreCase("New...") || options.get(i).equalsIgnoreCase("<none>")) {
                options.remove(i);
                count = options.size();
                i--;
            }
        }

        String selection = options.get(NumberUtils.generateRandomNumberInt(0, options.size() - 1));

        select_Location().selectByVisibleText(selection);
    }

    @FindBy(xpath = "//input[@id='FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:LossDetailsAddressDV:AddressDetailInputSetRef:LossDetailsAddressDetailInputSet:FNOLAddressInputSet:City-inputEl']")
    public WebElement editBox_City;

    private Guidewire8Select selectState() {
        return new Guidewire8Select(driver, "//table[contains(@id,':FNOLAddressInputSet:State-triggerWrap')]");
    }

    private void setState(String selectString) {
        selectState().selectByVisibleText(selectString);
    }

    @FindBy(xpath = "//img[@src='images/app/drop_button.png']")
    public List<WebElement> incidentLinks;

    @FindBy(xpath = "//div[starts-with(@id,'FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:')]")
    public List<WebElement> incidentItems;

    @FindBy(xpath = "//span[@id='FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:ttlBar']")
    public WebElement element_pageTitle;

    @FindBy(xpath = "//a[@id='ClaimLossDetails:ClaimLossDetailsScreen:Edit']")
    public WebElement button_Edit;

    @FindBy(xpath = "//a[contains(@id,':Update')]")
    public WebElement button_Update;

    @FindBy(xpath = "//a[@id='EditCropIncidentPopup:EditCropIncidentScreen:Edit']")
    public WebElement button_EditCrop;

    public Guidewire8Select select_FaultRating() {
        return new Guidewire8Select(
                driver, "//table[@id='ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV:LossDetailsDV:Notification_Fault-triggerWrap']");
    }

    @FindBy(css = "input[id$=':AddressLine1-inputEl']")
    public WebElement input_LlAddressLineOne;

    @FindBy(css = "input[id$=':AddressLine2-inputEl']")
    public WebElement input_LlAddressLineTwo;

    @FindBy(css = "input[id$=':City-inputEl']")
    public WebElement input_LlCity;

    @FindBy(css = "input[id$=':State-inputEl']")
    public WebElement input_LlState;

    @FindBy(css = "input[id$=':PostalCode-inputEl']")
    public WebElement input_LlZipCode;

    @FindBy(xpath = "//textarea[@id='ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV:LossDetailsDV:Description-inputEl']")
    public WebElement textarea_LlLossDescription;

    @FindBy(xpath = "//input[contains(@id,':LossDate-inputEl')]")
    public WebElement input_DateOfLoss;

    @FindBy(xpath = "//input[contains(@id,':Notification_Fault-inputEl')]")
    private WebElement inputFaultRating;

    @FindBy(css = "span[id*='ClaimLossDetails:ClaimLossDetailsScreen:ttlBar'")
    private WebElement pageHeaderElement;

    @FindBy(css = "input[id*=':ClaimsDetailInputSet:Status_CoverageQuestion_false-inputEl']")
    private WebElement radioCoverageInQuestionFalse;

    @FindBy(css = "span[id*=':PoliceReportsLNLV_tb:ToolbarButton-btnInnerEl']")
    private WebElement buttonOrderNewPoliceReport;

    public OrderPoliceReport clickOrderNewPoliceReport() {
        waitUtils.waitUntilElementIsClickable(buttonOrderNewPoliceReport, 5);
        buttonOrderNewPoliceReport.click();
        return new OrderPoliceReport(getDriver());
    }

    private Guidewire8Select selectCatastrophe() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Catastrophe_CatastropheNumber-triggerWrap')]");
    }

    public void setCatastrophe(String selection) {
        waitUtils.waitUntilElementIsClickable(selectCatastrophe().getSelectButtonElement());
        selectCatastrophe().selectByVisibleText(selection);
    }

    public void clickCoverageInQuestionFalse() {
        clickWhenClickable(radioCoverageInQuestionFalse, 5000);
    }

    public void selectFaultRating() {
        waitUntilElementIsClickable(inputFaultRating);
        select_FaultRating().selectByVisibleText("Insured at fault");
    }

    public void populateLossDescription(String text) {
        waitUntilElementIsClickable(editBox_LossDescription).sendKeys(text);
    }

    public void clickUpdateButton() {
        clickWhenClickable(button_Update);
    }

    public void clickEditButton() {
        clickWhenClickable(button_Edit);
        waitUntilElementIsClickable(By.id("ClaimLossDetails:ClaimLossDetailsScreen:Update-btnInnerEl"), 30);
    }

    public void clickCancelButton() {
        clickWhenClickable(button_Cancel);
    }

    public void selectSpecific_LossCause(String selectString) {
        Guidewire8Select mySelect = select_LossCause();
        mySelect.selectByVisibleText(selectString);
    }

    public String selectRandom_LossCause() {
        Guidewire8Select mySelect = select_LossCause();
        return mySelect.selectByVisibleTextRandom();
    }

    public void selectSpecific_LossRouter(String selectString) {
        Guidewire8Select mySelect = select_LossRouter();
        mySelect.selectByVisibleText(selectString);
    }

    public String selectRandom_LossRouter() {

        String selection;

        Guidewire8Select mySelect = select_LossRouter();
        mySelect.selectByVisibleTextRandom();
        selection = mySelect.getText();

        return selection;
    }

    public void selectSpecific_State(String selectString) {
        selectState().selectByVisibleText(selectString);
    }

    public void clickFinishButton() {
        clickWhenClickable(button_Finish);
    }

    private String getCity() {
        return waitUntilElementIsClickable(editBox_City).getAttribute("Value");
    }

    public void selectRandom_Location() {

        setLossLocation();

        

        if (getCity() == null) {
            
            waitUntilElementIsClickable(editBox_City).sendKeys("Pocatello" + Keys.ESCAPE);
            waitForPostBack();
            setState("Idaho");
            
        }

        if (getLlState().equalsIgnoreCase("<none>")) {
            setState("Idaho");
        }
    }

    public String clickIncidentLinks(Integer currentIndex) {

        List<WebElement> currentLink = finds
                (By.xpath("//a[starts-with(@id,'FNOLWizard:FBClaimWizardStepSet:"
                        + "FNOLWizard_NewLossDetailsScreen:') and contains(@id,'Iterator:')]"));

        String incidentID = currentLink.get(currentIndex).getAttribute("id");
        clickWhenClickable(currentLink.get(currentIndex));

        WebElement editLink = currentLink.get(currentIndex).findElement(By.xpath("//a[contains((.),'Edit')]"));
        clickWhenClickable(editLink);

        return incidentID;
    }

    public Integer findNumberOfIncidents() {

        List<WebElement> linksList = finds
                (By.xpath("//a[starts-with(@id,'FNOLWizard:FBClaimWizardStepSet:"
                        + "FNOLWizard_NewLossDetailsScreen:') and contains(@id,'Iterator:')]"));

        return linksList.size();
    }

    public void waitForPage() {
        waitUtils.waitUntilElementIsClickable(editBox_LossDescription);
    }

    public List<String> getAutoIncidentList() {

        SideMenuCC sideMenu = new SideMenuCC(this.driver);

        // Collect incident data
        sideMenu.clickLossDetailsLink();
        

        List<String> incidentStrings = new ArrayList<String>();
        List<WebElement> incidentElements = finds(By.xpath(
                "//div[contains(@id,'ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV')]/div/table/tbody/tr"));

        for (int i = 1; i < incidentElements.size(); i++) {
            for (int j = 2; j <= 5; j++) {
                String elementText = driver
                        .findElement(By
                                .xpath("//div[contains(@id,'ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV')]/div/table/tbody/tr/td["
                                        + j + "]"))
                        .getText();
                if (!elementText.equalsIgnoreCase("")) {
                    incidentStrings.add(elementText);
                }
            }
        }

        return incidentStrings;

    }

    public List<String> getIncidentListV2() {

//        SideMenuCC sideMenu = new SideMenuCC(this.driver);
//
//        // Collect incident data
//        sideMenu.clickLossDetailsLink();
        
        List<String> incidentStrings = new ArrayList<String>();
        List<WebElement> incidentElements = finds(By.xpath(
                "//div[contains(@id,'ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV')]/div/table/tbody/tr"));
        // contains the first 3 columns of data
        String incidentText = "";
        for (int i = 1; i < incidentElements.size(); i++) {
            for (int j = 2; j <= 4; j++) {

                String elementText = "";

                try {
                    elementText = driver.findElement(By.xpath("//div[contains(@id,'ClaimLossDetails:ClaimLossDetailsScreen:" +
                            "LossDetailsPanelSet:LossDetailsCardCV')]/div/table/tbody/tr/td[" + j + "]")).getText();
                } catch (Exception e) {
                    System.out.println("Table cell does not contain any data.");
                }

                if (!elementText.equalsIgnoreCase("")) {
                    incidentText += elementText + " ";
                }
            }
            // remove beginning and trailing spaces.
            incidentStrings.add(incidentText.trim());
        }
        return incidentStrings;
    }

    public void clickIncidentsInTable(Integer index) {
        // Pass index of element to click
        List<WebElement> propertyIncidents = finds(By.xpath(
                "//a[contains(@id,'ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV:LossDetailsDV:EditableFixedPropertyIncidentsLV')]"));
        hoverOverAndClick(propertyIncidents.get(index));
    }

    public List<String> getIncidentList() {

        List<String> incidentStrings = new ArrayList<String>();
        List<WebElement> incidentElements = finds(By.xpath(
                "//div[contains(@id,'ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV')]/div/table/tbody/tr"));

        // contains the first 3 columns of data
        for (int i = 1; i < incidentElements.size(); i++) {
            for (int j = 2; j <= 5; j++) {

                String elementText = "";

                try {
                    elementText = driver.findElement(By.xpath("//div[contains(@id,'ClaimLossDetails:ClaimLossDetailsScreen:" +
                            "LossDetailsPanelSet:LossDetailsCardCV')]/div/table/tbody/tr/td[" + j + "]")).getText();
                } catch (Exception e) {
                    System.out.println("Table cell does not contain any data.");
                }

                if (!elementText.equalsIgnoreCase("")) {
                    // remove beginning and trailing spaces.
                    incidentStrings.add(elementText.trim());
                }
            }

        }
        return incidentStrings;
    }

    public List<String> getIncidentAddresses() {
        List<String> accidentStrings = new ArrayList<String>();
        List<WebElement> accidentElements = finds(By.xpath(
                "//div[contains(@id,'ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV')]/div/table/tbody/tr/td[3]"));

        for (int i = 0; i < accidentElements.size(); i++) {
            String elementText = accidentElements.get(i).getText();
            accidentStrings.add(elementText);
        }

        return accidentStrings;
    }

    public void clickEdit() {

        waitUntilElementIsClickable(button_Edit, 5000).click();
    }

    public void clickEditCrop() {
        clickWhenClickable(button_EditCrop);

    }

    public String getLlAddressLineOne() {
        return input_LlAddressLineOne.getAttribute("value");
    }

    public String getLlAddressLineTwo() {

        return input_LlAddressLineTwo.getAttribute("value");
    }

    public String getLlCity() {

        return input_LlCity.getAttribute("value");
    }

    public String getLlState() {

        return input_LlState.getAttribute("value");
    }

    public String getLlZipCode() {

        return input_LlZipCode.getAttribute("value");
    }

    public String getLlLossDescription() {

        return textarea_LlLossDescription.getText();
    }


    public void inputDate(String changedDate) {
        input_DateOfLoss.clear();
        
        input_DateOfLoss.sendKeys(changedDate);

    }

    public String getDOL() {
        return input_DateOfLoss.getAttribute("value");
    }

    public void changeDolByXDays(int numDays) throws ParseException {
        SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date cDate = formatDate.parse(getDOL());
        Date changedDate = DateUtils.dateAddSubtract(cDate, DateAddSubtractOptions.Day, numDays);
        
        inputDate(formatDate.format(changedDate));
    }

    public void addFaultRating() {
        clickEditButton();

        try {
            selectFaultRating();
            sendArbitraryKeys(Keys.ESCAPE);
            waitForPostBack();
        } catch (Exception e) {
            System.out.println("Fault Rating Not Required.");
        }

        clickUpdateButton();
    }

}
