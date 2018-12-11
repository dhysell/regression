package repository.cc.claim;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;
import repository.gw.helpers.WaitUtils;

public class Matters extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;
    private WaitUtils waitUtils;

    public Matters(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "span[id*='ClaimMatters:ClaimMatterScreen:']")
    private WebElement pageHeader;

    @FindBy(css = "a[id*='ClaimMatters:ClaimMatterScreen:ClaimMatters_NewMatterButton']")
    private WebElement buttonNewMatter;

    private void clickPageHeader() {
        waitUtils.waitUntilElementIsVisible(pageHeader);
        pageHeader.click();
    }

    private NewMatter clickNewMatter() {
        waitUtils.waitUntilElementIsVisible(buttonNewMatter, 15);
        buttonNewMatter.click();
        return new NewMatter(getDriver());
    }

    public NewMatter newRandomMatter() {
        clickPageHeader();
        clickNewMatter().constructNewMatter();
        return new NewMatter(getDriver());
    }

    @FindBy(css = "div[id*='ClaimMatters:ClaimMatterScreen:MattersLV']")
    private WebElement mattersTable;

    public boolean findMatterInTable(String matterName) {
        int row;
        boolean isFound = false;
        try {
            row = tableUtils.getRowNumberInTableByText(mattersTable, matterName);
            isFound = true;
        } catch (Exception e) {
            System.out.println("Unable to find Matter.");
        }
        return isFound;
    }

/*    // ELEMENTS
    // =============================================================================

    // Matters page~~~~~~~~~~~~~

    @FindBy(xpath = "//a[@id='ClaimMatters:ClaimMatterScreen:ClaimMatters_AssignButton']")
    public WebElement button_Assign;

    @FindBy(xpath = "//a[@id='ClaimMatters:ClaimMatterScreen:ClaimMatters_RefreshButton']")
    public WebElement button_Refresh;

    @FindBy(xpath = "//a[@id='ClaimMatters:ClaimMatterScreen:ClaimMatters_CloseMatterButton']")
    public WebElement button_CloseMatter;

    @FindBy(xpath = "//a[@id='ClaimMatters:ClaimMatterScreen:ClaimMatters_CalendarButton-btnInnerEl']")
    public WebElement button_Calendar;

    // Edit
    // Matter********************************************************************************************************************************

    @FindBy(xpath = "//a[@id='MatterDetailPage:MatterDetailScreen:Edit']")
    public WebElement button_Edit;

    @FindBy(xpath = "//a[@id='MatterDetailPage:MatterDetailScreen:Update']")
    public WebElement button_UpdateEdit;

    @FindBy(xpath = "//a[@id='MatterDetailPage:MatterDetailScreen:MatterDetailPage_AssignButton']")
    public WebElement button_DetailsAssign;

    @FindBy(xpath = "//a[@id='MatterDetailPage:MatterDetailScreen:MatterDetailPage_CloseMatterButton']")
    public WebElement button_DetailsCloseMatter;

    @FindBy(xpath = "//a[@id='MatterDetailPage:MatterDetailScreen:MatterDetailPage_CalendarButton']")
    public WebElement button_DetailsMyCal;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:Matter_Name-inputEl']")
    public WebElement textbox_EName;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:Matter_CaseNumber-inputEl']")
    public WebElement textbox_ECaseNum;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:TrialDetails_SubroRelated_true-inputEl']")
    public WebElement radio_EYes;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:TrialDetails_SubroRelated_false-inputEl']")
    public WebElement radio_ENo;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:Counsel_DefenseApptDate-inputEl']")
    public WebElement textbox_EDefenseAppointedDate;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:Counsel_SentToDefenseDate']")
    public WebElement textbox_ESentToDefenseDate;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:TrialDetails_TrialDate-inputEl']")
    public WebElement textbox_ETrailDate;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:TrialDetails_Venue-inputEl']")
    public WebElement textbox_ETrialVenue;

    // Litigation Details

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:TrialDetails_Room-inputEl']")
    public WebElement textbox_ETrialRoom;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:TrialDetails_Judge-inputEl']")
    public WebElement textbox_ETrialJudge;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:ArbitrationDetails_TrialDate-inputEl']")
    public WebElement textbox_EArbitrationDate;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:ArbitrationDetails_Room-inputEl']")
    public WebElement textbox_EArbitrationRoom;

    // Primary Council

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:HearingDetails_HearingDate-inputEl']")
    public WebElement textbox_EHearingDate;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:HearingDetails_Room-inputEl']")
    public WebElement textbox_EHearingRoom;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:DocketNumber-inputEl']")
    public WebElement textbox_EDocketNumber;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:TrialDetails_FilingDate-inputEl']")
    public WebElement textbox_EFilingDate;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:ResponseDue-inputEl']")
    public WebElement textbox_EResponseDue;

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:ResponseFiled-inputEl']")
    public WebElement textbox_EResponseFiled;

    // Trial Details

    @FindBy(xpath = "//input[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:Matter_FinalSettleDate-inputEl']")
    public WebElement textbox_EFinalSettlementDate;

    @FindBy(xpath = "//a[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:MatterGeneral_Status:EditableMatterStatusLinesLV_tb:Add']")
    public WebElement button_AddStatusLine;

    @FindBy(xpath = "//a[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:MatterGeneral_Status:EditableMatterStatusLinesLV_tb:Remove']")
    public WebElement button_RemoveStatusLine;

    @FindBy(xpath = "//a[@id='CloseMatterPopup:CloseMatterScreen:Update']")
    public WebElement button_CloseMatterPopUp;

    // Arbitration Details

    @FindBy(xpath = "//a[@id='CloseMatterPopup:CloseMatterScreen:Cancel']")
    public WebElement button_CancelPopUp;

    @FindBy(xpath = "//textarea[@id='CloseMatterPopup:CloseMatterScreen:CloseMatterInfoDV:Note-inputEl']")
    public WebElement textarea_Note;

    @FindBy(xpath = "//a[@id='NewMatter:NewMatterScreen:NewMatterDV_tb:Update']")
    public WebElement button_Update;

    @FindBy(xpath = "//a[@id='NewMatter:NewMatterScreen:NewMatterDV_tb:Cancel']")
    public WebElement button_Cancel;

    // Hearing Details

    @FindBy(xpath = "//input[@id='NewMatter:NewMatterScreen:NewMatterDV:Matter_Name-inputEl']")
    public WebElement textbox_Name;

    @FindBy(xpath = "//input[@id='NewMatter:NewMatterScreen:NewMatterDV:Matter_CaseNumber-inputEl']")
    public WebElement textbox_CaseNumber;

    @FindBy(xpath = "NewMatter:NewMatterScreen:NewMatterDV:TrialDetails_SubroRelated_true-inputEl")
    public WebElement radio_SubroYes;

    @FindBy(xpath = "//input[@id='NewMatter:NewMatterScreen:NewMatterDV:TrialDetails_SubroRelated_false-inputEl']")
    public WebElement radio_SubroNo;



    // Additional Details

    public void clickCancelButton() {
        clickWhenClickable(button_Cancel);
    }

    public void clickCloseUpdate() {
        clickWhenClickable(button_CloseMatterPopUp);
    }

    public void clickDetailsAssign() {
        clickWhenClickable(button_DetailsAssign);
    }

    public void ClickDetailsClose() {
        clickWhenClickable(button_DetailsCloseMatter);
    }

    // Resolution

    public void ClickDetailsMyCal() {
        clickWhenClickable(button_DetailsMyCal);
    }

    public void clickEditMatter() {
        clickWhenClickable(button_Edit);
    }

    // Status Lines

    public void clickSubroNoRadio() {
        clickWhenClickable(radio_SubroNo);
    }

    public void clickSubroYesRadio() {
        clickWhenClickable(radio_SubroYes);
    }

    public Date addDateServedStatusLine(String date) throws Exception {
        clickWhenClickable(button_AddStatusLine);
        delay(500);
        String tableXpath = "//div[contains(@id,':EditableMatterStatusLinesLV')]";
        WebElement ele = find(By.xpath(tableXpath));
        delay(500);
        tableUtils.selectValueForSelectInTable(ele, tableUtils.getRowCount(ele), "Matter Status", "Date Served");
        delay(500);
        tableUtils.setValueForCellInsideTable(ele, tableUtils.getRowCount(ele), "Start Date", "StartDate", date);
        delay(500);
        clickUpdateEdit();
        return DateUtils.dateAddSubtract(DateUtils.convertStringtoDate(date, "MM/dd/yy"), DateAddSubtractOptions.Day, 10);
    }

    // Close Matter

    // *************************** New Matter **********************************
    public void clickUpdateButton() {
        clickWhenClickable(button_Update);
    }

    public void clickUpdateEdit() {
        clickWhenClickable(button_UpdateEdit);
    }

    public void randomCoDefendant() {
        select_CoDefendant().selectByVisibleTextRandom();
    }

    public void randomCourtDistrict() {
        select_CourtDistrict().selectByVisibleTextRandom();
    }

    // *************************************************************************************************************************************

    // New Matter ~~~~~~~~~~~~~~

    public void randomCourtType() {
        select_CourtyType().selectByVisibleTextRandom();
    }

    public void randomDefendant() {
        select_Defendant().selectByVisibleTextRandom();
    }

    public void randomDefenseAttorney() {
        select_DefenseAttorney().selectByVisibleTextRandom();
    }

    public void randomDefenseLawFirm() {
        select_DefenseLawFirm().selectByVisibleTextRandom();
    }

    public void randomLegalSpecialty() {
        select_LegalSpecialty().selectByVisibleTextRandom();
    }

    public void randomMatterType() {
        select_NewMatter().selectByVisibleTextRandom();
    }

    public void randomOwner() {
        select_Owner().selectByVisibleTextRandom();
    }

    public void randomPlaintiff() {
        select_Plaintiff().selectByVisibleTextRandom();
    }

    public void randomPlaintiffAttorney() {
        select_PlaintiffAttorney().selectByVisibleTextRandom();
    }

    public void randomPlaintiffLawFirm() {
        select_PlaintiffLawFirm().selectByVisibleTextRandom();
    }

    public void randomPrimaryCause() {
        select_PrimaryCause().selectByVisibleTextRandom();
    }

    public Guidewire8Select select_CloseResolution() {
        return new Guidewire8Select("//table[@id='CloseMatterPopup:CloseMatterScreen:CloseMatterInfoDV:Resolution-triggerWrap']", driver);

    }

    public Guidewire8Select select_CoDefendant() {
        return new Guidewire8Select("//table[@id='NewMatter:NewMatterScreen:NewMatterDV:CoDefendantExt-triggerWrap']", driver);
    }

    public Guidewire8Select select_CourtDistrict() {
        return new Guidewire8Select("//table[@id='NewMatter:NewMatterScreen:NewMatterDV:CourtDistrict-triggerWrap']", driver);
    }

    public Guidewire8Select select_CourtyType() {
        return new Guidewire8Select("//table[@id='NewMatter:NewMatterScreen:NewMatterDV:CourtType-triggerWrap']", driver);
    }

    public Guidewire8Select select_Defendant() {
        return new Guidewire8Select("//table[@id='NewMatter:NewMatterScreen:NewMatterDV:Defendant-triggerWrap']", driver);
    }

    public Guidewire8Select select_DefenseAttorney() {
        return new Guidewire8Select("//table[@id='NewMatter:NewMatterScreen:NewMatterDV:Counsel_DefenseAttorney-inputEl']", driver);
    }

    public Guidewire8Select select_DefenseLawFirm() {
        return new Guidewire8Select("//table[@id='NewMatter:NewMatterScreen:NewMatterDV:Counsel_DefenseLawFirm-inputCell']", driver);
    }

    public Guidewire8Select select_EArbitrationVenue() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:ArbitrationDetails_Venue-triggerWrap']", driver);
    }

    public Guidewire8Select select_EArbitrator() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:ArbitrationDetails_Arbitrator-triggerWrap']", driver);
    }

    // Helpers
    // =============================================================================

    public Guidewire8Select select_ECoDefendant() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:CoDefendantExt-triggerWrap']", driver);
    }

    public Guidewire8Select select_ECourtDistrict() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:CourtDistrict-triggerWrap']", driver);
    }

    public Guidewire8Select select_ECourtType() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:CourtType-triggerWrap']", driver);
    }

    public Guidewire8Select select_EDefendant() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:Defendant-triggerWrap']", driver);
    }

    public Guidewire8Select select_EDefenseAttorney() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:Counsel_DefenseAttorney-triggerWrap']", driver);
    }

    public Guidewire8Select select_EDefenseLawFirm() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:Counsel_DefenseLawFirm-triggerWrap']", driver);
    }

    public Guidewire8Select select_EHearingJudge() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:HearingDetails_Arbitrator-triggerWrap']", driver);
    }

    public Guidewire8Select select_EHearingVenue() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:HearingDetails_Venue-triggerWrap']", driver);
    }

    public Guidewire8Select select_ELegalSpecialty() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:LegalSpecialty-triggerWrap']", driver);
    }

    public Guidewire8Select select_EPlaintiff() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:Plaintiff-triggerWrap']", driver);
    }

    public Guidewire8Select select_EPlaintiffAttorney() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:Counsel_PlaintiffAttorney-triggerWrap']", driver);
    }

    public Guidewire8Select select_EPlaintiffLawFirm() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:Counsel_PlaintiffLawFirm-triggerWrap']", driver);
    }

    public Guidewire8Select select_EPrimaryCause() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:TrialDetails_PrimaryCause2-triggerWrap']", driver);
    }

    public Guidewire8Select select_EResolution() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:Matter_Resolution-triggerWrap']", driver);
    }

    public Guidewire8Select select_EType() {
        return new Guidewire8Select("//table[@id='MatterDetailPage:MatterDetailScreen:MatterDetailsDV:TrialDetails_MatterType-triggerWrap']", driver);
    }

    public Guidewire8Select select_Exposure() {
        return new Guidewire8Select("//div[@id='NewMatter:NewMatterScreen:NewMatterDV:NewMatterExposuresLV-body']", driver);
    }

    public Guidewire8Select select_LegalSpecialty() {
        return new Guidewire8Select("//table[@id='']", driver);
    }

    public Guidewire8Select select_NewMatter() {
        return new Guidewire8Select("//table[@id='NewMatter:NewMatterScreen:NewMatterDV:MatterType-triggerWrap']", driver);
    }

    public Guidewire8Select select_Owner() {
        return new Guidewire8Select("//table[contains(@id,'Matter_AssignActivity-triggerWrap') or contains(@id,':Matter_AssignActivityInhouse-triggerWrap')]", driver);
    }

    public Guidewire8Select select_Plaintiff() {
        return new Guidewire8Select("//table[@id='NewMatter:NewMatterScreen:NewMatterDV:Plaintiff-triggerWrap']", driver);
    }

    public Guidewire8Select select_PlaintiffAttorney() {
        return new Guidewire8Select("//table[@id='NewMatter:NewMatterScreen:NewMatterDV:Counsel_PlaintiffAttorney-triggerWrap']", driver);
    }

    public Guidewire8Select select_PlaintiffLawFirm() {
        return new Guidewire8Select("//table[]@id='NewMatter:NewMatterScreen:NewMatterDV:Counsel_PlaintiffLawFirm-triggerWrap']", driver);
    }

    public Guidewire8Select select_PrimaryCause() {
        return new Guidewire8Select("//table[@id='NewMatter:NewMatterScreen:NewMatterDV:TrialDetails_PrimaryCause-triggerWrap']", driver);
    }

    public void selectCloseResolution() {
        select_CloseResolution().selectByVisibleTextRandom();
    }

    public void selectEResolution() {
        select_EResolution().selectByVisibleTextRandom();
    }

    public void selectNewMatter() {
        select_NewMatter().selectByVisibleTextRandom();

    }

    public void selectSpecific_Exposure(String exposure) {
        Guidewire8Select mySelect = select_Exposure();
        mySelect.selectByVisibleTextPartial(exposure);
    }

    private void selectRandomExposure() {
        Guidewire8Select mySelect = select_Exposure();
        mySelect.selectByVisibleTextRandom();
    }

    public void sendCaseNumber(String caseNum) {
        textbox_CaseNumber.sendKeys(caseNum);
    }

    public void sendFinalSettlementDate(String date) {

        if (textbox_EFinalSettlementDate.getText().equals("")) {
            textbox_EFinalSettlementDate.sendKeys(date);
        } else {
            textbox_EFinalSettlementDate.clear();
            delay(500);
            textbox_EFinalSettlementDate.sendKeys(date);
        }

    }

    public void setName(String name) {
        textbox_Name.sendKeys(name);
    }

    public void sendNote(String note) {
        textarea_Note.sendKeys(note);
    }

    public void setCoDefendant(String coDefendant) {
        select_CoDefendant().selectByVisibleText(coDefendant);
    }

    public void setCourtDistrict(String district) {
        select_CourtDistrict().selectByVisibleText(district);
    }

    // ******************************************************************************************

    // Edit Matter

    public void setCourtType(String courtType) {
        select_CourtyType().selectByVisibleText(courtType);
    }

    public void setDefendant(String defendant) {
        select_Defendant().selectByVisibleText(defendant);
    }

    public void setDefenseAttorney(String attorney) {
        select_DefenseAttorney().selectByVisibleText(attorney);
    }

    public void setDefenseLawFirm(String lawFirm) {
        select_DefenseLawFirm().selectByVisibleText(lawFirm);
    }

    public void setLegalSpecialty(String legalSpecialty) {
        select_LegalSpecialty().selectByVisibleText(legalSpecialty);
    }

    public void setMatterType(String type) {
        select_NewMatter().selectByVisibleText(type);
    }

    public void setMatterTypeRandom() {
        select_NewMatter().selectByVisibleTextRandom();
    }

    public void setOwner(String owner) {
        select_Owner().selectByVisibleText(owner);
    }

    public void setPlaintiff(String plaintiff) {
        select_Plaintiff().selectByVisibleText(plaintiff);
    }

    public void setPlaintiffAttorney(String plaintiffAttorney) {
        select_PlaintiffAttorney().selectByVisibleText(plaintiffAttorney);
    }

    public void setPlaintiffLawFirm(String pLawFirm) {
        select_PlaintiffLawFirm().selectByVisibleText(pLawFirm);
    }

    public void setPrimaryCause(String primaryCause) {
        select_PrimaryCause().selectByVisibleText(primaryCause);
    }

    public void validateExposureInMatter(String string) throws Exception {
        List<WebElement> exposure = finds(By.xpath("//div[contains(text(),'" + string + "')]"));
        if (exposure.size() > 0) {
            systemOut("The exposure was created");
        } else {
            throw new Exception("It doesn't appear that the exposure was attached to the matter");
        }

    }

    public void validateMatterClosed() {
        // TODO

    }*/
}
