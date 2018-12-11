package repository.pc.policy;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PreRenewalDirectionExplanation;
import repository.gw.enums.PreRenewalDirectionType;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyPreRenewalDirections;
import repository.gw.generate.custom.PreRenewalDirection;
import repository.gw.generate.custom.PreRenewalDirection_Explanation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;

import java.util.ArrayList;
import java.util.List;

public class PolicyPreRenewalDirection extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public PolicyPreRenewalDirection(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//div[contains(@id, 'PreRenewalDirectionPage:PreRenewalDirectionScreen:PreRenewalInputSet')]")
    public WebElement table_PolicyPreRenewalDirectionTable;

    @FindBy(xpath = "//a[contains(@id, ':PreRenDirectionView')]")
    private WebElement link_PreRenewalDirectionView;


    @FindBy(xpath = "//div[contains(@id, 'CreateEditPreRenewalDirectionPopup:6') or contains(@id, 'CreateEditPreRenewalDirectionPopup:CreateEditPreRenewalDirectionPanelSet:5')]")
    public WebElement table_PolicyPreRenewalDirectionExplanation;


    @FindBy(xpath = "//a[contains(@id, ':CloseChange')]")
    public WebElement link_Close;

    @FindBy(xpath = "//a[contains(@id, 'CreateEditPreRenewalDirectionPopup:completeButton')]")
    public WebElement link_ClosePreRenewalDirection;

    @FindBy(xpath = "//a[contains(@id, '__crumb__')]")
    private WebElement link_ReturnToSummary;

    @FindBy(xpath = "//span[contains(@id, 'PreRenewalInputSet:addPreRenewal-btnEl')]")
    private WebElement button_AddPreRenewalDirection;

    @FindBy(xpath = "//textarea[contains(@id, 'CreateEditPreRenewalDirectionPanelSet:newExplanationBody-inputEl')]")
    private WebElement text_PreRenealDescription;


    public void createAnyPreRenewalDirection(GeneratePolicy policy) throws Exception {
        PreRenewalDirectionExplanation direction = PreRenewalDirectionExplanation.random();
        new SideMenuPC(getDriver()).clickSideMenuToolsSummary();
        clickViewPreRenewalDirections();
        clickAddPreRenewalDirection();
        select_PreRenewalDirectionType();
        setPreRenewalDirectionType(PreRenewalDirectionType.random());
        setCode(direction);
        setPreRenewalDirectionDescription(direction.getDescription());
        clickAdd();
        clickOK();
        clickReturnToSummaryPage();
    }

    private void setPreRenewalDirectionDescription(String description) {
        text_PreRenealDescription.clear();
        text_PreRenealDescription.sendKeys(description);
    }

    private Guidewire8Select select_PreRenewalDirection_Code() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'CreateEditPreRenewalDirectionPanelSet:NonRenewCodeNew-triggerWrap')]");
    }

    private void setCode(PreRenewalDirectionExplanation code) {
        Guidewire8Select mySelect = select_PreRenewalDirection_Code();
        mySelect.selectByVisibleText(code.getCode());
    }


    private void clickAddPreRenewalDirection() throws GuidewireNavigationException {
        clickWhenClickable(button_AddPreRenewalDirection);
        new GuidewireHelpers(getDriver()).isOnPage("//span[(@class='g-title') and (text()='Create Pre-Renewal Direction')]", 5000, "UNABLE TO GET TO CREATE PRE-RENEWAL DIRECTION PAGE AFTER CLICKING ADD PRERENEWAL DIRECTION");
    }


    public PolicyPreRenewalDirections getPreRenewalDirections() throws GuidewireNavigationException {
        List<PreRenewalDirection> preRenewalDirectionList = new ArrayList<PreRenewalDirection>();
        List<WebElement> viewButtons = finds(By.xpath("//a[contains(@id, 'PreRenDirectionView')]"));
        for (int i = 0; i < viewButtons.size(); i++) {
            PreRenewalDirection myPrerenewalDirection = new PreRenewalDirection();
            viewPreRenewalDirection(viewButtons.get(i));
            //SET PRERENEWAL DIRECTION INFO
            myPrerenewalDirection.setPreRenewalDirection(PreRenewalDirectionType.getEnum(getPreRenewalDirectionType()));
            myPrerenewalDirection.setAssignedUser(getAssignedUser());
            myPrerenewalDirection.setOpenedDate(getOpenedDate());
            myPrerenewalDirection.setOpenedBy(getOpenedBy());
            myPrerenewalDirection.setClosedDate(getClosedDate());
            myPrerenewalDirection.setCompletedBy(getCompletedBy());
            //SET EXPLANATION INFO
            List<WebElement> explanationList = finds(By.xpath("//div[contains(@id, ':CreateEditPreRenewalDirectionPanelSet:5-body')]/div/table/tbody/child::tr"));
            for (WebElement explanation : explanationList) {
                PreRenewalDirection_Explanation myExplanation = new PreRenewalDirection_Explanation();
                myExplanation.setClosed(!explanation.findElements(By.xpath(".//child::td/a[contains(@id, ':CloseChange') and contains(text(), 'Re-Open')]")).isEmpty());
                myExplanation.setCodeAndDescription(PreRenewalDirectionExplanation.getEnum(explanation.findElement(By.xpath(".//child::td/div/a[contains(@id, ':NoneRenewalCode')]")).getText()));
                myExplanation.setClosedDate(explanation.findElement(By.xpath(".//child::td[last()]/div")).getText());
                myPrerenewalDirection.getExplanationList().add(myExplanation);
            }
            preRenewalDirectionList.add(myPrerenewalDirection);
            clickCancel();
            new GuidewireHelpers(getDriver()).isOnPage("//span[contains(text(), 'Pre-Renewal Direction for Policy')]", 10, "UNALBE TO GET TO PRE-RENEWAL DIRECTION FOR POLICY PAGE AFTER CLICKING CANCEL ON PRE-RENEWAL DIRECTION EDIT PAGE.");
        }
        return new PolicyPreRenewalDirections(preRenewalDirectionList);
    }

    private void viewPreRenewalDirection(WebElement viewButton) throws GuidewireNavigationException {
        clickWhenClickable(viewButton);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@class, 'g-title') and (text()='Edit Pre-Renewal Direction')]", 10, "UNABLE TO GET TO EDIT PRE-RENEWAL DIRECTION PAGE AFTER CLICKING VEW BUTTON");
    }

    private Guidewire8Select select_PreRenewalDirectionType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':preRenewalDirectionType-triggerWrap')]");
    }

    private String getPreRenewalDirectionType() {
        Guidewire8Select mySelect = select_PreRenewalDirectionType();
        return mySelect.getText();
    }

    private void setPreRenewalDirectionType(PreRenewalDirectionType type) {
        Guidewire8Select mySelect = select_PreRenewalDirectionType();
        mySelect.selectByVisibleText(type.getValue());
    }


    @FindBy(xpath = "//div[contains(@id, ':preRenewalDirectionAssignee-inputEl')]")
    private WebElement text_AssignedUser;

    private String getAssignedUser() {
        return text_AssignedUser.getText();
    }

    @FindBy(xpath = "//div[contains(@id, ':preRenewalDirectionOpen-inputEl')]")
    private WebElement text_OpenedDate;

    private String getOpenedDate() {
        return text_OpenedDate.getText();
    }

    @FindBy(xpath = "//div[contains(@id, ':preRenewalDirectionOpenedBy-inputEl')]")
    private WebElement text_OpenedBy;

    private String getOpenedBy() {
        return text_OpenedBy.getText();
    }

    @FindBy(xpath = "//div[contains(@id, ':preRenewalDirectionClose-inputEl')]")
    private WebElement text_ClosedDate;

    private String getClosedDate() {
        return text_ClosedDate.getText();
    }

    @FindBy(xpath = "//div[contains(@id, ':preRenewalDirectionCompleted-inputEl')]")
    private WebElement text_CompletedBy;

    private String getCompletedBy() {
        return text_CompletedBy.getText();
    }


    public String getEditPreRenewalDirectionOpenDate() {
        String returnValue = null;
        returnValue = find(By.xpath("//div[contains(@id, ':preRenewalDirectionOpen-inputEl')]")).getText();
        return returnValue;
    }

    @FindBy(xpath = "//a[contains(@id, 'Policy_Summary_PolicyDV:ViewPreRenewalDirections')]")
    private WebElement link_ViewPreRenewalDirections;

    public void clickViewPreRenewalDirections() throws GuidewireNavigationException {
        clickWhenClickable(link_ViewPreRenewalDirections);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':addPreRenewal-btnInnerEl')]", 10, "UNABLE TO GET TO PRE-RENEWAL DIRECTIONS PAGE AFTER CLICKING LINK ON POLICY SUMMARY PAGE");
    }


    public boolean checkPreRenewalExists() {
        List<WebElement> preRenewalList = new ArrayList<WebElement>();
        preRenewalList = finds(By.xpath(".//tbody/child::tr"));
        return !preRenewalList.isEmpty();
    }


    public void clickViewInPreRenewalDirection() throws GuidewireNavigationException {
        clickWhenClickable(link_PreRenewalDirectionView);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@class, 'g-title') and (text()='Edit Pre-Renewal Direction')]", 10, "UNABLE TO GET TO EDIT PRE-RENEWAL DIRECTION PAGE");
    }


    public int getPreRenewalDirectionExplanationTableRowCount() {
        return tableUtils.getRowCount(table_PolicyPreRenewalDirectionExplanation);
    }


    public String getPreRenewalDirExplanationTableCodeByRow(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PolicyPreRenewalDirectionExplanation, row, "Code");
    }


    public boolean checkPreRenewalDirectionWithExpectedCode(String code) {
        for (int currentRow = 1; currentRow <= getPreRenewalDirectionExplanationTableRowCount(); currentRow++) {
            String currentText = getPreRenewalDirExplanationTableCodeByRow(currentRow);
            if (currentText.contains(code) || code.contains(currentText)) {
                return true;
            }
        }
        return false;
    }


    public void closeAllPreRenewalDirectionExplanations() {
        List<WebElement> closeElements = finds(By.xpath("//a[contains(@id,':CloseChange')]"));
        //        int currentRow = 0;
        for (int i = 0; i < closeElements.size(); i++) {
            //        for (WebElement element : closeElements) {
            WebElement linkXpath = find(By.xpath("//a[contains(@id,'" + (i) + ":CloseChange')]"));
            clickWhenClickable(linkXpath);
            selectOKOrCancelFromPopup(OkCancel.OK);
            //            currentRow++;
        }
    }


    public void clickClosePreRenewalDirection() {
        clickWhenClickable(link_ClosePreRenewalDirection);
    }


    public void closePreRenewalExplanations(GeneratePolicy policy) throws GuidewireNavigationException {
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(getDriver());
        policySearchPC.searchPolicyByPolicyNumber(new GuidewireHelpers(getDriver()).getPolicyNumber(policy));

        PolicySummary summaryPage = new PolicySummary(getDriver());
        if (summaryPage.checkIfActivityExists("Pre-Renewal Review")) {
            summaryPage.clickViewPreRenewalDirection();
            boolean preRenewalDirectionExists = checkPreRenewalExists();
            if (preRenewalDirectionExists) {
                clickViewInPreRenewalDirection();
                closeAllPreRenewalDirectionExplanations();
                clickClosePreRenewalDirection();
            }
            clickReturnToSummaryPage();
        }
    }


    public void clickReturnToSummaryPage() throws GuidewireNavigationException {
        link_ReturnToSummary.click();
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@class, 'g-title') and text()='Summary']", 10, "UNABLE TO GET TO POLICY SUMMARY PAGE AFTER CLICKING RETURN TO SUMMARY LINK");
    }


    public String getPreRenewalClosedByUW() {
        String value = tableUtils.getCellTextInTableByRowAndColumnName(table_PolicyPreRenewalDirectionTable, 1, "Closed By");
        return value;
    }

}
