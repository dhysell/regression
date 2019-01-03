package repository.pc.policy;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.FileMarker;
import repository.gw.helpers.TableUtils;

public class PolicyFileMarkers extends BasePage {

    public PolicyFileMarkers(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[contains(@id, 'PolicyFile_Icons:Policy_IconScreen:Policy_IconScreen:Update')]")
    private WebElement button_Update;

    @FindBy(xpath = "//a[contains(@id, 'PolicyFile_Icons:Policy_IconScreen:Policy_IconScreen:Edit')]")
    private WebElement button_Edit;

    @FindBy(xpath = "//div[contains(@id, ':Policy_IconScreen:PolicyIconListViewPanelLV')]")
    private WebElement table_FileMarkers;

    @FindBy(xpath = "//span[@id = 'PolicyFile:PolicyFileMenuInfoBar:Infobar_CashOnly-btnInnerEl']")
    public WebElement flag_PolicyCashOnlyStatusFlag;

    @FindBy(xpath = "//span[@id = 'PolicyFile:PolicyFileMenuInfoBar:Infobar_Collection-btnInnerEl']")
    public WebElement flag_PolicyCollectionStatusFlag;

    @FindBy(xpath = "//span[@id = 'PolicyFile:PolicyFileMenuInfoBar:Infobar_Construction-btnInnerEl']")
    public WebElement flag_PolicyConstructionStatusFlag;

    @FindBy(xpath = "//span[@id = 'PolicyFile:PolicyFileMenuInfoBar:Infobar_ReinstateRewrite-btnInnerEl']")
    public WebElement flag_PolicyReinstateRewriteStatusFlag;

    @FindBy(xpath = "//span[@id = 'PolicyFile:PolicyFileMenuInfoBar:Infobar_FormE-btnInnerEl']")
    public WebElement flag_PolicyFormEStatusFlag;

    @FindBy(xpath = "//span[@id = 'PolicyFile:PolicyFileMenuInfoBar:Infobar_ReturnedMail-btnInnerEl']")
    public WebElement flag_PolicyReturnedMailStatusFlag;

    @FindBy(xpath = "//span[@id = 'PolicyFile:PolicyFileMenuInfoBar:Infobar_SR22-btnInnerEl']")
    public WebElement flag_PolicySR22StatusFlag;

    @FindBy(xpath = "//span[@id = 'PolicyFile:PolicyFileMenuInfoBar:Infobar_TreatyException-btnInnerEl']")
    public WebElement flag_PolicyTreatyExceptionStatusFlag;

    @FindBy(xpath = "//span[@id = 'PolicyFile:PolicyFileMenuInfoBar:Infobar_Umbrealla-btnInnerEl']")
    public WebElement flag_PolicyUmbrellaStatusFlag;

    @FindBy(xpath = "//span[@id = 'PolicyFile:PolicyFileMenuInfoBar:Infobar_PicturesInDISR-btnInnerEl']")
    public WebElement flag_PolicyPictureInDISRStatusFlag;

    @FindBy(xpath = "//span[@id = 'PolicyFile:PolicyFileMenuInfoBar:InfobarCertificate_FBM-btnInnerEl']")
    public WebElement flag_PolicyCertificateStatusFlag;

    @FindBy(xpath = "//span[@id = 'PolicyFile:PolicyFileMenuInfoBar:Infobar_PendingCancel-btnInnerEl']")
    public WebElement flag_PolicyPendingCancelStatusFlag;


    public boolean policyCashOnlyStatusFlagExist() {
        return super.checkIfElementExists(flag_PolicyCashOnlyStatusFlag, 1000);
    }


    public boolean policyCollectionStatusFlagExist() {
        return super.checkIfElementExists(flag_PolicyCollectionStatusFlag, 1000);
    }


    public boolean policyConstructionStatusFlagExist() {
        return super.checkIfElementExists(flag_PolicyConstructionStatusFlag, 1000);
    }


    public boolean policyReinstateRewriteStatusFlagExist() {
        return super.checkIfElementExists(flag_PolicyReinstateRewriteStatusFlag, 1000);
    }


    public boolean policyReturnedMailStatusFlagExist() {
        return super.checkIfElementExists(flag_PolicyReturnedMailStatusFlag, 1000);
    }


    public boolean policySR22StatusFlagExist() {
        return super.checkIfElementExists(flag_PolicySR22StatusFlag, 1000);
    }


    public boolean policyTreatyExceptionStatusFlagExist() {
        return super.checkIfElementExists(flag_PolicyTreatyExceptionStatusFlag, 1000);
    }


    public boolean policyUmbrellaStatusFlagExist() {
        return super.checkIfElementExists(flag_PolicyUmbrellaStatusFlag, 1000);
    }


    public boolean policyPictureInDISRStatusFlagExist() {
        return super.checkIfElementExists(flag_PolicyPictureInDISRStatusFlag, 1000);
    }


    public boolean policyCertificateStatusFlagExist() {
        return super.checkIfElementExists(flag_PolicyCertificateStatusFlag, 1000);
    }


    public boolean policyPendingCancelStatusFlagExist() {
        return super.checkIfElementExists(flag_PolicyPendingCancelStatusFlag, 1000);
    }


    public boolean policyFormEStatusFlagExist() {
        return super.checkIfElementExists(flag_PolicyFormEStatusFlag, 1000);
    }


    public boolean verifyExpectedFlagStatusExist(String flag) {
        return super.checkIfElementExists("//span[contains(@id, '" + flag + "')]", 1000);
    }


    public void setFileMarker(FileMarker fileMarker, boolean setCheckboxTrueFalse) {
        WebElement tableCheckBoxElement = find(By.xpath(".//td/div/img[(@title= '" + fileMarker.getValue() + "') and contains(@id, ':policyIconFileMarkerIcon')]"));
        clickWhenClickable(tableCheckBoxElement);
        new TableUtils(getDriver()).setCheckboxInTable(table_FileMarkers, new TableUtils(getDriver()).getHighlightedRowNumber(table_FileMarkers), setCheckboxTrueFalse);
    }


    public void clickUpdate() {
        clickWhenClickable(button_Update);
    }


    public void clickEdit() {
        clickWhenClickable(button_Edit);
    }

}
