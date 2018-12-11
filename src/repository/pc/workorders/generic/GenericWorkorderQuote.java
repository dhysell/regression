package repository.pc.workorders.generic;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.PolicyPremium;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.activity.UWActivityPC;

public class GenericWorkorderQuote extends GenericWorkorder {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderQuote(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    @FindBy(xpath = "//span[contains(@id, 'JobWizardToolbarButtonSet:Draft-btnEl')]")
    private WebElement button_SubmissionQuoteSaveDraft;

    @FindBy(xpath = "//div[contains(@id, 'Quote_SummaryDV:TotalPremium-inputEl') or contains(@id, 'Quote_SummaryDV:PolicyPremiumInputSet:TotalPremium-inputEl') or contains(@id, ':Quote_SummaryDV:PolicyPremiumInputSet:TotalNetPremium-inputEl') or contains(@id, ':Quote_SummaryDV:PolicyPremiumInputSet:TotalCost-inputEl')]")
    public WebElement text_SubmissionQuoteTotalPremium;

    @FindBy(xpath = "//div[contains(@id, 'Quote_SummaryDV:TotalGrossPremium-inputEl') or contains(@id, 'Quote_SummaryDV:PolicyPremiumInputSet:TotalGrossPremium-inputEl') or contains (@id, ':Quote_SummaryDV:PolicyPremiumInputSet:TotalGrossPremium')]")
    public WebElement text_SubmissionQuoteGrossPremium;

    @FindBy(xpath = "//div[contains(@id, 'Quote_SummaryDV:TotalDiscountsSurcharges-inputEl') or contains(@id, 'Quote_SummaryDV:PolicyPremiumInputSet:TotalDiscountsSurcharges-inputEl')]")
    public WebElement text_SubmissionQuoteDiscountsSurcharges;

    @FindBy(xpath = "//div[contains(@id, 'Quote_SummaryDV:TotalNetPremium-inputEl') or contains(@id, 'Quote_SummaryDV:PolicyPremiumInputSet:TotalNetPremium-inputEl')]")
    public WebElement text_SubmissionQuoteNetPremium;

    @FindBy(xpath = "//div[contains(@id, 'Quote_SummaryDV:TotalMembership-inputEl') or contains(@id, 'Quote_SummaryDV:PolicyPremiumInputSet:TotalMembership-inputEl') or contains(@id, 'Quote_SummaryDV:PolicyPremiumInputSet:TotalNetDues-inputEl')]")
    public WebElement text_SubmissionQuoteTotalMembershipDues;

    @FindBy(xpath = "//div[contains(@id, 'Quote_SummaryDV:sr22Charge-inputEl') or contains(@id, 'Quote_SummaryDV:PolicyPremiumInputSet:sr22Charge-inputEl')]")
    public WebElement text_SubmissionQuoteSR22Charge;

    @FindBy(xpath = "//div[contains(@id, 'Quote_SummaryDV:TotalLienAmountBilled-inputEl') or contains(@id, 'Quote_SummaryDV:PolicyPremiumInputSet:TotalLienAmountBilled-inputEl')]")
    public WebElement text_SubmissionQuoteLienAmountBilled;

    @FindBy(xpath = "//div[contains(@id, 'Quote_SummaryDV:TotalCost-inputEl') or contains(@id, 'Quote_SummaryDV:PolicyPremiumInputSet:TotalCost-inputEl')]")
    public WebElement text_SubmissionQuoteTotalCostToInsured;

    @FindBy(xpath = "//div[contains(@id, ':Quote_SummaryDV:ChangeInCost-inputEl')]")
    public WebElement text_ChangeInCost;

    @FindBy(xpath = "//a[contains(@id, ':PolicyLineCV:PropertyDetailCoveragesPanelSet:0:LocationLabelInputSet:ViewLocationLabel')]")
    public WebElement link_SectionISectionIIPropertyDetailsLocation;

    public List<WebElement> tables_SubmissionQuoteCoverages() {
        String tablesXpath = "//div[contains(@id, 'RatingCumulDetailsPanelSet:') and contains(@id, '-body')]/div/table[contains(@id, '-table')]";
        return finds(By.xpath(tablesXpath));
    }

    @FindBy(xpath = "//label[contains(@id, ':PanelSet:Warning')]")
    private WebElement banner_BlockBind;

    @FindBy(xpath = "//span[contains(@id, 'JobWizardToolbarButtonSet:BindRewrite-btnInnerEl') or contains(@id, 'JobWizardToolbarButtonSet:BindPolicyChange-btnInnerEl')]")
    private WebElement policyFileMenuActions_IssuePolicy;

    @FindBy(xpath = "//div[contains (@id, 'SubmissionWizard:SubmissionWizard_MultiLine_QuoteScreen:Common_MultiLine_QuoteScreenPanelSet:Quote_SummaryDV:CreditScore-inputEl')]")
    private WebElement text_SubmissionQuoteCreditScore;

    @FindBy(xpath = "//div[contains (@id, 'UWBlockProgressIssuesPopup:IssuesScreen:ApproveDV')]")
    private WebElement table_PreQuoteApprovalMessages;

    @FindBy(xpath = "//span[contains(@id, 'UWBlockProgressIssuesPopup:IssuesScreen:DetailsButton-btnEl')]")
    private WebElement link_Details;

    @FindBy(xpath = "//span[contains(@id, 'UWBlockProgressIssuesPopup:IssuesScreen:PreQuoteIssueTitle')]")
    private WebElement page_PreQuote;

    @FindBy(xpath = "//div[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:SectionBreakdownPanelSet:0') or contains(@id, 'QuoteScreen:SectionBreakdownPanelSet:0')]")
    private WebElement table_SquirePremiumSummary;

    @FindBy(xpath = "//div[contains(@id, ':SubmissionWizard_QuoteScreen:RatingCumulDetailsPanelSet:PolicyLineCV:2')]")
    private WebElement table_StandardLiabilityPolicyPremium;

    @FindBy(xpath = "//div[contains(@id, ':SubmissionWizard_QuoteScreen:RatingCumulDetailsPanelSet:PolicyLineCV:1')]")
    private WebElement table_StandardLiabilityCoverages;

    @FindBy(xpath = "//span[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:policyLineCardTabTab-btnEl') or contains(@id,'PolicyFile_Pricing_PL:PolicyFile_PricingScreen:PolicyPremiumCV:0:PolicyLineCV:sectionIICoveragesTab-btnEl')]")
    private WebElement link_SectionISectionIIPolicyPremium;

    @FindBy(xpath = "//span[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:1:policyLineCardTabTab-btnEl')]")
    private WebElement link_SectionIIIPolicyPremium;

    @FindBy(xpath = "//span[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:2:policyLineCardTabTab-btnEl')]")
    private WebElement link_SectionIVPolicyPremium;

    @FindBy(xpath = "//span[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:sectionICoveragesTab-btnEl') or contains(@id, ':PolicyPremiumCV:0:PolicyLineCV:sectionICoveragesTab-btnEl')]")
    private WebElement link_SectionICoverages;

    @FindBy(xpath = "//span[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:sectionIICoveragesTab-btnEl')]")
    private WebElement link_SectionIICoverages;

    @FindBy(xpath = "//span[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:fppCoveragesTab-btnEl')]")
    private WebElement link_SectionFPPCoverages;

    @FindBy(xpath = "//span[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:2:PolicyLineCV:recreationalEquipmentCardTab-btnEl')]")
    private WebElement link_SectionIVRecEquipment;

    @FindBy(xpath = "//span[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:2:PolicyLineCV:watercraftCardTab-btnEl')]")
    private WebElement link_SectionIVWatercraft;

    @FindBy(xpath = "//span[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:2:PolicyLineCV:farmEquipmentCardTab-btnEl')]")
    private WebElement link_SectionIVFarmEquipment;

    @FindBy(xpath = "//span[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:2:PolicyLineCV:personalEquipmentCardTab-btnEl')]")
    private WebElement link_SectionIVPersonalProperty;

    @FindBy(xpath = "//span[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:2:PolicyLineCV:cargoCardTab-btnEl')]")
    private WebElement link_SectionIVCargo;

    @FindBy(xpath = "//span[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:2:PolicyLineCV:livestockCardTab-btnEl')]")
    private WebElement link_SectionIVLiveStock;

    @FindBy(xpath = "//span[contains(@id, ':SubmissionWizard_QuoteScreen:RatingCumulDetailsPanelSet:PolicyLineCV:farmEquipmentCardTab-btnInnerEl')]")
    private WebElement link_StandardIMFarmEquip;

    @FindBy(xpath = "//span[contains(@id, ':SubmissionWizard_QuoteScreen:RatingCumulDetailsPanelSet:PolicyLineCV:personalEquipmentCardTab-btnInnerEl')]")
    private WebElement link_StandardIMPersonalProperty;

    @FindBy(xpath = "//span[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:Blank_tb:expandedViewBtn-btnEl') or contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:1:Blank_tb:expandedViewBtn-btnEl')]")
    private WebElement link_ExtendedView;

    @FindBy(xpath = "//span[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:Blank_tb:compactViewBtn-btnEl')]")
    private WebElement link_CompactView;

    @FindBy(xpath = "//div[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:2') or contains(@id, ':RatingCumulDetailsPanelSet:PolicyLineCV:PropertyDetailCoveragesPanelSet:1')]")
    private WebElement table_SectionISectionIIDeductible;

    @FindBy(xpath = "//a[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:CostChangeDetailCV:2:RatingTxDetailsPanelSet:0:coverageCardTab')]")
    private WebElement tab_CostChangeDetailsSectionIVRecreationalEquipment;

    @FindBy(xpath = "//a[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:CostChangeDetailCV:2:RatingTxDetailsPanelSet:2:coverageCardTab') or contains(@id, ':CancellationWizard_QuoteScreen:RatingTxDetailsPanelSet:1:coverageCardTab')]")
    private WebElement tab_CostChangeDetailsSectionIVPersonalProperty;

    @FindBy(xpath = "//a[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:CostChangeDetailCV:2:RatingTxDetailsPanelSet:1:coverageCardTab')]")
    private WebElement tab_CostChangeDetailsSectionIVWatercraft;

    public WebElement table_SectionICoveragesLocations(int location) {
        String tablesXpath = "//div[contains(@id, 'PolicyLineCV:PropertyDetailCoveragesPanelSet:0:HOLocationDwellingsPanelSet:" + (location - 1) + ":1')]";
        return find(By.xpath(tablesXpath));
    }

    public WebElement text_QuotePropertyDetailCoveragesBuildingPremium(String locationNumber, String locationAddress, String BuildingNumber, String propertyType) {
        return find(By.xpath("//a[contains(., '" + locationNumber + "') and contains(.,'" + locationAddress + "')]/ancestor::table[3]/descendant::tr[1]/following-sibling::tr[contains(., '" + BuildingNumber + "') and contains(., '" + propertyType + "')]/descendant::tr/td/div/a[contains(., '" + BuildingNumber + "') and contains(., '" + propertyType + "')]/ancestor::td[1]/following-sibling::td/div"));
    }

    @FindBy(xpath = "//div[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:5')]")
    private WebElement table_SectionIICoveragesDeductibles;


    @FindBy(xpath = "//div[contains(@id, ':PolicyPremiumCV:0:PolicyLineCV:2')]")
    private WebElement table_SectionILineCoveragesDetails;

    @FindBy(xpath = "//div[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:3') or contains (@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:') or contains(@id, 'PolicyFile_Pricing_PL:PolicyFile_PricingScreen:PolicyPremiumCV:0:PolicyLineCV:6')]")
    private WebElement table_SectionIICoveragesPremium;

    @FindBy(xpath = "//div[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:FPPCoveragesViewPanelSet:1')]")
    private WebElement table_FPPCoveragesDeductibles;

    @FindBy(xpath = "//div[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:FPPCoveragesViewPanelSet:2')]")
    private WebElement table_FPPCoveragesPremiumValues;

    @FindBy(xpath = "//div[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:1:PolicyLineCV:PersonalAutoLinePanelSet:0') or contains (@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:PersonalAutoLinePanelSet:0')]")
    private WebElement table_SectionIIIAutoVehicleDetails;

    @FindBy(xpath = "//div[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:2:PolicyLineCV:recrationalVehiclePanel:PIMQuoteScreenViewPanelSet:1')]")
    private WebElement table_SectionIVRecEquipmentDetails;

    @FindBy(xpath = "//div[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:2:PolicyLineCV:watercraftPanel:PIMQuoteScreenViewPanelSet:1')]")
    private WebElement table_SectionIVWatercraft;

    @FindBy(xpath = "//div[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:2:PolicyLineCV:farmEquipmentPanel:PIMQuoteScreenViewPanelSet:1') or contains(@id, ':SubmissionWizard_QuoteScreen:RatingCumulDetailsPanelSet:PolicyLineCV:farmEquipmentPanel:PIMQuoteScreenViewPanelSet:1')]")
    private WebElement table_SectionIVFarmEquipment;

    @FindBy(xpath = "//div[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:2:PolicyLineCV:personalEquipmentPanel:PIMQuoteScreenViewPanelSet:1') or contains(@id, ':SubmissionWizard_QuoteScreen:RatingCumulDetailsPanelSet:PolicyLineCV:personalEquipmentPanel:PIMQuoteScreenViewPanelSet:1')]")
    private WebElement table_SectionIVPersonalProperty;

    @FindBy(xpath = "//div[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:2:PolicyLineCV:cargoPanel:PIMQuoteScreenViewPanelSet:1')]")
    private WebElement table_SectionIVCargo;

    @FindBy(xpath = "//div[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:2:PolicyLineCV:livestockPanel:PIMQuoteScreenViewPanelSet:1')]")
    private WebElement table_SectionIVLiveStock;

    @FindBy(xpath = "//div[contains(@id, 'Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:2:PolicyLineCV:1') or contains(@id, ':SubmissionWizard_QuoteScreen:RatingCumulDetailsPanelSet:PolicyLineCV:1')]")
    private WebElement table_SectionIVSummary;


    @FindBy(xpath = "//div[contains(@id, ':PolicyLineCV:PropertyDetailCoveragesPanelSet:0:2')]")
    private WebElement table_SectionISectionIIPropertyDetailsCoverages;

    @FindBy(xpath = "//a[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:linkToSectionIICoverages')]")
    private WebElement link_SectionISectionIIViewSectionIICoverages;

    @FindBy(xpath = "//a[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:FPPCoveragesViewPanelSet:linkToAdditionalSectionICoverages')]")
    private WebElement link_SectionISectionIIViewAdditionalSectionICoverages;

    @FindBy(xpath = "//*[contains(@id, ':JobChangeWizard_Quote_TransactionCardTab-btnEl') or contains(@id, ':CancellationWizard_Quote_TransactionCardTab-btnEl') or contains(@id, ':PolicyChangeWizard_Quote_TransactionCardTab-btnEl') or contains(@id, ':IssuanceWizard_Quote_TransactionCardTab-btnEl')]")
    private WebElement tab_CostChangeDetails;

    @FindBy(xpath = "//div[contains(@id,'PolicyChangeWizard:PolicyChangeWizard_MultiLine_QuoteScreen:Common_MultiLine_QuoteScreenPanelSet:CostChangeDetailCV') or contains(@id,'PolicyChangeWizard:PolicyChangeWizard_QuoteScreen:RatingTxDetailsPanelSet:0') ]")
    private WebElement table_CostChangeReview;

    @FindBy(xpath = "//a[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:CostChangeDetailCV:0:costChangeDetailLineCardTab')]")
    private WebElement tab_CostChangeDetailsSectionIAndIIPropertyAndLiabilityLine;

    @FindBy(xpath = "//a[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:CostChangeDetailCV:1:costChangeDetailLineCardTab')]")
    private WebElement tab_CostChangeDetailsSectionIIIAutoLine;

    @FindBy(xpath = "//a[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:CostChangeDetailCV:2:costChangeDetailLineCardTab')]")
    private WebElement tab_CostChangeDetailsSectionIVInlandMarineLine;

    @FindBy(xpath = "//a[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:CostChangeDetailCV:0:RatingTxDetailsPanelSet:sectionOneCardTab')]")
    private WebElement tab_CostChangeDetailsSectionIAndIIPropertyAndLiabilityLinePropertyDetailCoverages;

    @FindBy(xpath = "//a[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:CostChangeDetailCV:0:RatingTxDetailsPanelSet:sectionTwoCardTab')]")
    private WebElement tab_CostChangeDetailsSectionIAndIIPropertyAndLiabilityLineSectionIICoverages;


    @FindBy(xpath = "//div[contains(@id,':RatingCumulDetailsPanelSet:PolicyLineCV:3')]")
    private WebElement table_UmbrellaQuotePremiums;

    @FindBy(xpath = "//div[contains(@id,':RatingCumulDetailsPanelSet:PolicyLineCV:UnderlyingLimit-inputEl')]")
    private WebElement text_UnderlyingLimits;

    @FindBy(xpath = "//div[contains(@id,':RatingCumulDetailsPanelSet:PolicyLineCV:IncreasedLimit-inputEl')]")
    private WebElement text_IncreasedLimits;

    @FindBy(xpath = "//a[contains(@id, ':RatingOverrideButtonDV:RatingOverrideButtonDV:OverrideRating')]")
    private WebElement link_Handrate;

    @FindBy(xpath = "//div[contains(@id, 'RatingOverridePopup:RatingOverridePanelSet:1-body')]")
    private WebElement table_RatingOverrides;

    @FindBy(xpath = "//div[contains(@id, ':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:PropertyDetailCoveragesPanelSet:0:HOLocationDwellingsPanelSet:0:1') or contains(@id, ':RatingCumulDetailsPanelSet:PolicyLineCV:PropertyDetailCoveragesPanelSet:0:HOLocationDwellingsPanelSet:0:1')]")
    private WebElement table_PropertyDetails;

    @FindBy(xpath = "//div[contains(@id, 'SubmissionWizard:SubmissionWizard_MultiLine_QuoteScreen:Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:2') or contains(@id, ':PolicyPremiumCV:0:PolicyLineCV:2')]")
    private WebElement table_SectionILineCoverages;


    @FindBy(xpath = "//div[contains(@id, ':RatingTxDetailsPanelSet:1')]")
    private WebElement table_BOPLineLevelCoverageChanges;

    @FindBy(xpath = "//div[contains(@id,'PolicyChangeWizard:PolicyChangeWizard_MultiLine_QuoteScreen:Common_MultiLine_QuoteScreenPanelSet:CostChangeDetailCV:0:RatingTxDetailsPanelSet:sectionOneCardTab:panelId')]//div/table//following-sibling::td")
    private WebElement table_CostchangeDetailsMultipleLocations;

    @FindBy(xpath = "//div[contains(@id,':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV')]//div/table//following-sibling::td")
    private WebElement table_ExpandedViewMultipleVehicles;

    @FindBy(xpath = "//div[contains(@id, ':RatingTxDetailsPanelSet:')]")
    private WebElement table_CostChangeDetails;

    @FindBy(xpath = "//span[contains(@id, 'CostChangeDetailCV') and contains(@id, 'additionalSectionOneCoveragesCardTab-btnEl')]")
    private WebElement link_CostChangeDetailsAdditionalSectionOneCoverages;

    @FindBy(xpath = "//div[contains(@id, 'CostChangeDetailCV') and contains(@id, 'RatingTxDetailsPanelSet:StandardAdditionalSectionOneTransactionsLV')]")
    private WebElement table_CostChangeDetailsAdditionalSectionOneCoverages;

    @FindBy(xpath = "//tr[contains(., 'Membership Line')]/following-sibling::tr//div[contains(@id, ':PolicyLineCV')]")
    private WebElement table_MembershipLineSummary;

    @FindBy(xpath = "//span[contains(@id, ':policyLineCardTabTab-btnInnerEl') and contains(., 'Membership Line')]")
    private WebElement link_MembershipLine;


    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    public double getDollarValueFromCostChangeDetails(String rowColumnName, String description, String columnName) {

        return NumberUtils.getCurrencyValueFromElement(getCellValueFromCostChangeDetails(rowColumnName, description, columnName));

    }


    public String getCellValueFromCostChangeDetails(String rowColumnName, String description, String columnName) {

        String value = "";
        int rowCount = tableUtils.getRowCount(table_CostChangeDetails);
        int currentRow = 1;
        boolean found = false;
        while (currentRow <= rowCount && found == false) {
            String desc = tableUtils.getCellTextInTableByRowAndColumnName(table_CostChangeDetails, currentRow, rowColumnName);
            if (desc.contains(description)) {
                value = tableUtils.getCellTextInTableByRowAndColumnName(table_CostChangeDetails, currentRow, columnName);
                found = true;
            }
            currentRow++;
        }
        return value;
    }


    public boolean  getSectionILineCoveragesHasEquipmentBreakdown(){

        int rowCount = tableUtils.getRowCount(table_SectionILineCoveragesDetails);
        int currentRow = 1;
        boolean found = false;
        while (currentRow <= rowCount ) {
            String desc = tableUtils.getCellTextInTableByRowAndColumnName(table_SectionILineCoveragesDetails, currentRow, "Description");
            if (desc.contains("Equipment Breakdown")) {
                String value = tableUtils.getCellTextInTableByRowAndColumnName(table_SectionILineCoveragesDetails, currentRow, "Value(s)");
                String premium = tableUtils.getCellTextInTableByRowAndColumnName(table_SectionILineCoveragesDetails, currentRow, "Premium");
           return StringUtils.isNotBlank(value) && StringUtils.isNotBlank(premium);
            }
            currentRow++;
        }
   return false;
    }

    public void clickReleaseLock() {
        GenericWorkorderRiskAnalysis ra = new GenericWorkorderRiskAnalysis(getDriver());
        ra.clickReleaseLock();
        repository.pc.activity.UWActivityPC activity = new UWActivityPC(driver);
        activity.setText("Stuff Approved");
        activity.clickSendRequest();
    }


    public void issuePolicy(IssuanceType type) {
        super.clickGenericWorkorderIssue(type);
    }


    public void clickPolicyChangeNext() {
        super.clickPolicyChangeNext();
    }


    public void clickGenericWorkorderFullApp() {
        super.clickGenericWorkorderFullApp();
    }


    public void clickSaveDraftButton() {
        clickWhenClickable(button_SubmissionQuoteSaveDraft);
    }

    //ATTENTION: This method is for lines other than Squire and will most likely be deprecated in the future. Please only use it if you know what you are doing.

    public double getQuoteTotalPremium() {
        int i = 0;
        do {
            waitUntilElementIsClickable(text_SubmissionQuoteTotalPremium);
            i++;
        } while (!checkIfElementExists(text_SubmissionQuoteTotalPremium, 100) && i < 60);
        double totalPremium = NumberUtils.getCurrencyValueFromElement(text_SubmissionQuoteTotalPremium);
        return totalPremium;
    }


    public double getQuoteTotalGrossPremium() {
        waitUntilElementIsClickable(text_SubmissionQuoteGrossPremium);
        double policyPremium = NumberUtils.getCurrencyValueFromElement(text_SubmissionQuoteGrossPremium);
        return policyPremium;
    }


    public double getQuoteTotalDiscountsSurcharges() {
        waitUntilElementIsClickable(text_SubmissionQuoteDiscountsSurcharges);
        double totalDiscountsSurcharges = NumberUtils.getCurrencyValueFromElement(text_SubmissionQuoteDiscountsSurcharges.getText().replace("(", "").replace(")", ""));
        return totalDiscountsSurcharges;
    }


    public double getQuoteTotalNetPremium() {
        waitUntilElementIsClickable(text_SubmissionQuoteNetPremium);
        double netPremium = NumberUtils.getCurrencyValueFromElement(text_SubmissionQuoteNetPremium);
        return netPremium;
    }


    public double getQuoteSR22Charge() {
        waitUntilElementIsClickable(text_SubmissionQuoteSR22Charge, 2000);
        double sr22Charge = NumberUtils.getCurrencyValueFromElement(text_SubmissionQuoteSR22Charge);
        return sr22Charge;
    }


    public double getQuoteTotalMembershipDues() {
        waitUntilElementIsClickable(text_SubmissionQuoteTotalMembershipDues, 2000);
        double membershipDues = NumberUtils.getCurrencyValueFromElement(text_SubmissionQuoteTotalMembershipDues);
        return membershipDues;
    }


    public double getQuoteTotalLienAmountBilled() {
        waitUntilElementIsClickable(text_SubmissionQuoteLienAmountBilled);
        double lienAmountBilled = NumberUtils.getCurrencyValueFromElement(text_SubmissionQuoteLienAmountBilled);
        return lienAmountBilled;
    }


    public double getQuoteChangeInCost() {
        waitUntilElementIsClickable(text_ChangeInCost);
        double totalCost = NumberUtils.getCurrencyValueFromElement(text_ChangeInCost);
        return totalCost;
    }


    public double getQuoteTotalCostToInsured() {
        waitUntilElementIsClickable(text_SubmissionQuoteTotalCostToInsured);
        double totalCost = NumberUtils.getCurrencyValueFromElement(text_SubmissionQuoteTotalCostToInsured);
        return totalCost;
    }


    public String getQuoteCreditScore() {
        
        String creditScore = text_SubmissionQuoteCreditScore.getText();
        return creditScore;
    }


    public double getQuoteTotalInsuredPremiumPortion() {
        double totalInsuredPremium = 0;

        List<WebElement> allCovTables = tables_SubmissionQuoteCoverages();

        for (WebElement covTable : allCovTables) {
            int numCols = covTable.findElements(By.xpath(".//tbody/tr[1]/td")).size(); // Get numCols from first row in table
            List<WebElement> insuredCells = covTable.findElements(By.xpath(".//tbody/tr/td[(" + numCols + ") and (contains(.,'Insured'))]")); // Assuming always last column

            for (WebElement insSpanCell : insuredCells) {
                List<WebElement> myList = insSpanCell.findElements(By.xpath(".//preceding-sibling::td[1]/div"));
                
                if (!myList.isEmpty()) {
                    totalInsuredPremium += NumberUtils.getCurrencyValueFromElement(insSpanCell.findElement(By.xpath(".//preceding-sibling::td[1]/div")));
                }
            }
        }
        return totalInsuredPremium;
    }

    /*
     *  public double
     * getAdditionalInterestPremiumPortion(ArrayList<PolicyLocation>
     * locationList) { double totalAdditionalInterestPremium = 0; for
     * (PolicyLocation locations : locationList) { for (PolicyLocationBuilding
     * locBldg : locations.getBuildingList()) { if
     * (locBldg.getAdditionalInterestList().size() > 0) { if
     * (locBldg.getAdditionalInterestList().get(0).getAdditionalInterestBilling(
     * ) == AdditionalInterestBilling.Bill_Building) { List<WebElement>
     * allCovTables = tables_SubmissionQuoteCoverages(); for(WebElement covTable
     * : allCovTables) { int numCols =
     * covTable.findElements(By.xpath(".//tbody/tr[1]/td")).size(); //Get
     * numCols from first row in table List<WebElement> additionalInterestCells
     * = covTable.findElements(By.xpath(".//tbody/tr/td[(" + numCols +
     * ") and (contains(.,'" + locBldg.getAdditionalInterestList().get(0).
     * getLienholderNameFromPolicyCenter() + "'))]")); //Assuming always last
     * column
     *
     * double additionalInterestPremiumPerLienholder = 0; for(WebElement
     * additionalInterestSpanCell : additionalInterestCells) { double
     * additionalInterestPremiumAmount =
     * NumberUtils.getCurrencyValueFromElement(additionalInterestSpanCell.
     * findElement(By.xpath(".//preceding-sibling::td[1]/div")));
     * additionalInterestPremiumPerLienholder +=
     * additionalInterestPremiumAmount; totalAdditionalInterestPremium +=
     * additionalInterestPremiumAmount; } if
     * (additionalInterestPremiumPerLienholder > 0) {
     * locBldg.getAdditionalInterestList().get(0).
     * setAdditionalInterestPremiumAmount(additionalInterestPremiumPerLienholder
     * ); } } } } } } return totalAdditionalInterestPremium; }
     */


    public double getAdditionalInterestPremiumPortion(ArrayList<PolicyLocation> locationList) {
        int locationCounter = -1;
        int buildingCounterPerLocation = -1;
        double totalAdditionalInterestPremium = 0;
        String payerName = "";

        // Location and Building Level Coverages.
        for (PolicyLocation locations : locationList) {
            locationCounter++;
            for (PolicyLocationBuilding locBldg : locations.getBuildingList()) {
                buildingCounterPerLocation++;
                if (locBldg.getAdditionalInterestList().size() > 0) {
                    if (locBldg.getAdditionalInterestList().get(0).getAdditionalInterestBilling() == AdditionalInterestBilling.Bill_Lienholder) {
                        WebElement buildingPerLocationIterable = find(By.xpath("//div[contains(@id, 'RatingCumulDetailsPanelSet:locationIterator:" + locationCounter + ":" + buildingCounterPerLocation + ":')]"));
                        String payerGridColumnID = tableUtils.getGridColumnFromTable(buildingPerLocationIterable, "Payer");
                        List<WebElement> payerRows = buildingPerLocationIterable.findElements(By.xpath(".//tbody/tr/td[contains(@class,'" + payerGridColumnID + "') and contains(.,'" + locBldg.getAdditionalInterestList().get(0).getLienholderNameFromPolicyCenter() + "')]"));
                        double additionalInterestPremiumPerLienholder = 0;
                        for (WebElement payerRow : payerRows) {
                            additionalInterestPremiumPerLienholder += NumberUtils.getCurrencyValueFromElement(payerRow.findElement(By.xpath(".//parent::td/preceding-sibling::td[1]/div")));
                        }
                        // Line-Level Coverages.
                        WebElement lineLevelChargesIterable = find(By.xpath("//div[contains(@id, ':RatingCumulDetailsPanelSet:') and contains(@id, 'CoverageCostLV') and not(contains(@id, 'locationIterator'))]"));
                        String lineLevelpayerGridColumnID = tableUtils.getGridColumnFromTable(lineLevelChargesIterable, "Payer");
                        List<WebElement> lineLevelAdditionalInterestCheck = lineLevelChargesIterable.findElements(By.xpath(".//tbody/tr/td[contains(@class,'" + lineLevelpayerGridColumnID + "') and contains(@class, 'lvtitle') and not(contains(.,'Insured'))]"));
                        if (lineLevelAdditionalInterestCheck.size() > 0) {
                            if (!locBldg.getAdditionalInterestList().get(0).getLienholderNameFromPolicyCenter().equalsIgnoreCase(payerName)) {
                                List<WebElement> lineLevelpayerRows = lineLevelChargesIterable.findElements(By.xpath(".//tbody/tr/td[contains(@class,'" + lineLevelpayerGridColumnID + "') and contains(.,'" + locBldg.getAdditionalInterestList().get(0).getLienholderNameFromPolicyCenter() + "')]"));
                                for (WebElement lineLevelpayerRow : lineLevelpayerRows) {
                                    additionalInterestPremiumPerLienholder += NumberUtils.getCurrencyValueFromElement(lineLevelpayerRow.findElement(By.xpath(".//parent::td/preceding-sibling::td[1]/div")));
                                }
                                payerName = locBldg.getAdditionalInterestList().get(0).getLienholderNameFromPolicyCenter();
                            }
                        }
                        totalAdditionalInterestPremium += additionalInterestPremiumPerLienholder;
                        if (additionalInterestPremiumPerLienholder > 0) {
                            locBldg.getAdditionalInterestList().get(0).setAdditionalInterestPremiumAmount(additionalInterestPremiumPerLienholder);
                        }
                    }
                }
            }
            buildingCounterPerLocation = -1;
        }
        return totalAdditionalInterestPremium;
    }


    public void clickQuote() {
        super.clickGenericWorkorderQuote();
    }


    public Boolean hasBlockBind() {
        return finds(By.xpath("//label[contains(text(), 'This quote will require underwriting approval prior to')]")).size() > 0;
    }


    public Boolean hasBlockQuoteRelease() {
        return finds(By.xpath("//div[contains(text(), 'Blocking Quote Release') or contains(text(), 'Blocking Quote')]")).size() > 0;
    }


    public Boolean hasBlockQuote() {
        return finds(By.xpath("//div[text()='Blocking Quote']")).size() > 0;
    }


    public double getAdditionalInsuredCharges(String aiRole) {

        List<WebElement> allCovTables = tables_SubmissionQuoteCoverages();
        if (!allCovTables.isEmpty()) {
            for (WebElement table : allCovTables) {
                List<WebElement> aiRoleTR = table.findElements(By.xpath(".//div[contains(., '" + aiRole + "')]/parent::td/parent::tr"));
                if (!aiRoleTR.isEmpty()) {
                    if (!aiRoleTR.get(0).findElements(By.xpath(".//descendant::td[contains(@class, 'currency-positive')]/div")).isEmpty()) {
                        return NumberUtils.getCurrencyValueFromElement(aiRoleTR.get(0).findElement(By.xpath(".//descendant::td[contains(@class, 'currency-positive')]/div")));
                    }
                }
            }
        }

        return 0;
    }


    public String getDescriptionValue(String description) {

        List<WebElement> allCovTables = tables_SubmissionQuoteCoverages();
        if (!allCovTables.isEmpty()) {
            for (WebElement table : allCovTables) {
                List<WebElement> aiRoleTR = table.findElements(By.xpath(".//div[contains(., '" + description + "')]/parent::td/parent::tr"));
                if (!aiRoleTR.isEmpty()) {
                    if (!aiRoleTR.get(0).findElements(By.xpath(".//descendant::td[2]/div")).isEmpty()) {
                        return aiRoleTR.get(0).findElement(By.xpath(".//descendant::td[2]/div")).getText();
                    }
                }
            }
        }

        return null;
    }
    
    public String getDescriptionPremium(String description) {

        List<WebElement> allCovTables = tables_SubmissionQuoteCoverages();
        if (!allCovTables.isEmpty()) {
            for (WebElement table : allCovTables) {
                List<WebElement> aiRoleTR = table.findElements(By.xpath(".//div[contains(., '" + description + "')]/parent::td/parent::tr"));
                if (!aiRoleTR.isEmpty()) {
                    if (!aiRoleTR.get(0).findElements(By.xpath(".//descendant::td[3]/div")).isEmpty()) {
                        return aiRoleTR.get(0).findElement(By.xpath(".//descendant::td[3]/div")).getText();
                    }
                }
            }
        }

        return null;
    }
    
    public String getDescriptionQuantity(String description) {

        List<WebElement> allCovTables = tables_SubmissionQuoteCoverages();
        if (!allCovTables.isEmpty()) {
            for (WebElement table : allCovTables) {
                List<WebElement> aiRoleTR = table.findElements(By.xpath(".//div[contains(., '" + description + "')]/parent::td/parent::tr"));
                if (!aiRoleTR.isEmpty()) {
                    if (!aiRoleTR.get(0).findElements(By.xpath(".//descendant::td[2]/div")).isEmpty()) {
                        return aiRoleTR.get(0).findElement(By.xpath(".//descendant::td[2]/div")).getText();
                    }
                }
            }
        }

        return null;
    }


    public void clickIssuePolicy() {
        clickWhenVisible(policyFileMenuActions_IssuePolicy);
    }


    public boolean checkDescriptionOptions(String description) {

        List<WebElement> allCovTables = tables_SubmissionQuoteCoverages();
        if (!allCovTables.isEmpty()) {
            for (WebElement table : allCovTables) {
                List<WebElement> aiRoleTR = table.findElements(By.xpath(".//div[contains(., '" + description + "')]/parent::td/parent::tr"));
                if (!aiRoleTR.isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }


    public void clickPreQuoteDetails() {
        
        clickWhenClickable(link_Details);
        
    }


    public String getTextFromPreApprovedMessages() {
        return table_PreQuoteApprovalMessages.getText();
    }


    public boolean isPreQuoteDisplayed() {
        try {
            return checkIfElementExists(page_PreQuote, 500);
        } catch (Exception e) {
            return false;
        }
    }


    public double getSquirePremiumSummaryAmount(String sectionName, boolean gross, boolean net, boolean discount) {
        int row = tableUtils.getRowNumberInTableByText(table_SquirePremiumSummary, sectionName);
        String amount = "";
        if (row > 0) {
            if (gross) {
                amount = tableUtils.getCellTextInTableByRowAndColumnName(table_SquirePremiumSummary, row, "Gross");
            }

            if (net) {
                amount = tableUtils.getCellTextInTableByRowAndColumnName(table_SquirePremiumSummary, row, "Net");
            }

            if (discount) {
                amount = tableUtils.getCellTextInTableByRowAndColumnName(table_SquirePremiumSummary, row, "Discounts/Surcharge").replace("(", "").replace(")", "");
            }
        }

        return NumberUtils.getCurrencyValueFromElement(amount);

    }


    public void clickSectionISectionIIPolicyPremium() {
        
        clickWhenClickable(link_SectionISectionIIPolicyPremium);
        
    }


    public void clickSectionIIIPolicyPremium() {
        
        clickWhenClickable(link_SectionIIIPolicyPremium);
        
    }


    public void clickSectionIVPolicyPremium() {
        
        clickWhenClickable(link_SectionIVPolicyPremium);
        
    }


    public void clickSectionICoverages() {
        
        clickWhenClickable(link_SectionICoverages);
        
    }


    public void clickSectionIICoverages() {
        clickSectionISectionIIPolicyPremium();
        clickWhenClickable(link_SectionIICoverages);
        
    }


    public void clickSectionFPPCoverages() {
        clickSectionISectionIIPolicyPremium();
        clickWhenClickable(link_SectionFPPCoverages);
        
    }


    public void clickSectionIVRecEquipment() {
        clickSectionIVPolicyPremium();
        clickWhenClickable(link_SectionIVRecEquipment);
        
    }


    public void clickSectionIVWatercraft() {
        clickSectionIVPolicyPremium();
        clickWhenClickable(link_SectionIVWatercraft);
        
    }


    public void clickSectionIVFarmEquipment() {
        clickSectionIVPolicyPremium();
        clickWhenClickable(link_SectionIVFarmEquipment);
        
    }


    public void clickSectionIVPersonalProperty() {
        clickSectionIVPolicyPremium();
        clickWhenClickable(link_SectionIVPersonalProperty);
        
    }


    public void clickSectionIVCargo() {
        clickSectionIVPolicyPremium();
        clickWhenClickable(link_SectionIVCargo);
        
    }


    public void clickSectionIVLiveStock() {
        clickSectionIVPolicyPremium();
        clickWhenClickable(link_SectionIVLiveStock);
        
    }


    public void clickStandardIMFarmEquipment() {
        clickWhenClickable(link_StandardIMFarmEquip);
        
    }

    public void clickMembershiLineTab() {
        clickWhenClickable(link_MembershipLine);
        
    }

    public void clickStandardIMPersonalProperty() {
        clickWhenClickable(link_StandardIMPersonalProperty);
        
    }

    // Section 1 Methods


    public double getSectionICoveragesDeductible() {
        String amount = "";
        List<WebElement> section1Coverages = tableUtils.getAllTableRows(table_SectionISectionIIDeductible);
        for (WebElement element : section1Coverages) {
            String[] tempArray = element.getText().split("\n");
            
            if (tempArray[0].trim().contains("Deductible")) {
                amount = tempArray[1].trim();
                break;
            }
        }
        return Double.parseDouble(amount);
    }

    private int getSection1CoverageRowNumber(int propertyNumber, String coverageType) {
        int returnRow = 0;
        int rowCount = tableUtils.getRowCount(table_SectionICoveragesLocations(propertyNumber));
        for (int cRow = 1; cRow < rowCount; cRow++) {
            String desc = tableUtils.getCellTextInTableByRowAndColumnName(table_SectionICoveragesLocations(propertyNumber), cRow, "Description");
            if (desc.contains(coverageType)) {
                returnRow = cRow;
                break;
            }
        }
        return returnRow;
    }


    public double getSectionOnePropertyPremium(String locationNumber, String locationAddress, String BuildingNumber, String propertyType) {
        waitUntilElementIsVisible(text_QuotePropertyDetailCoveragesBuildingPremium(locationNumber, locationAddress, BuildingNumber, propertyType));
        return NumberUtils.getCurrencyValueFromElement(text_QuotePropertyDetailCoveragesBuildingPremium(locationNumber, locationAddress, BuildingNumber, propertyType).getText());
    }

    private int getStandardLiabilityPremiumRowNumber(String coverageType, WebElement tablePolicyPremium) {
        int returnRow = 0;
        int rowCount = tableUtils.getRowCount(tablePolicyPremium);
        for (int cRow = 1; cRow <= rowCount; cRow++) {
            String desc = tableUtils.getCellTextInTableByRowAndColumnName(tablePolicyPremium, cRow, "Description");
            if (desc.contains(coverageType)) {
                returnRow = cRow;
                break;
            }
        }
        return returnRow;
    }


    public String getSectionIPropertyDetailsValue(int propertyNumber, String coverageType, String description) {
        String includedInBaseText = "";
        int coverageRow = getSection1CoverageRowNumber(propertyNumber, coverageType);
        

        if (coverageType.equals(description)) {
            includedInBaseText = tableUtils.getCellTextInTableByRowAndColumnName(table_SectionICoveragesLocations(1), coverageRow, "Value");
        } else {
            for (int newRow = (coverageRow); newRow < (tableUtils.getRowCount(table_SectionICoveragesLocations(1)) - coverageRow); newRow++) {
                String desc = tableUtils.getCellTextInTableByRowAndColumnName(table_SectionICoveragesLocations(1), newRow, "Description");
                if (desc.trim().contains(description)) {
                    includedInBaseText = tableUtils.getCellTextInTableByRowAndColumnName(table_SectionICoveragesLocations(1), newRow, "Value");
                    break;
                }
            }
        }

        return includedInBaseText;

    }


    public boolean checkSectionICoveragePremiumPayerByName(String description, String name) {
        boolean valueFound = false;
        List<WebElement> tableElements = table_PropertyDetails.findElements(By.xpath(".//tbody/descendant::*[contains(text(), '" + description + "')]/ancestor::tr[1] | .//tbody/descendant::div[contains(text(), '" + description + "')]/ancestor::tr[1]"));
        int row = 0;
        for (WebElement element : tableElements) {
            row = tableUtils.getRowNumberFromWebElementRow(element);
            if (tableUtils.getCellTextInTableByRowAndColumnName(table_PropertyDetails, row, "Payer").contains(name)) {
                valueFound = true;
                break;
            }
        }
        return valueFound;
    }


    public double getSectionIPremiumByCoverageType(int propertyNumber, String coverageType) {
        String premium = "";
        int coverageRow = getSection1CoverageRowNumber(propertyNumber, coverageType);
        
        premium = tableUtils.getCellTextInTableByRowAndColumnName(table_SectionICoveragesLocations(1), coverageRow, "Premium");
        return NumberUtils.getCurrencyValueFromElement(premium);
    }


    public String getSectionIValueByCoverageType(int propertyNumber, String coverageType) {
        String premium = "";
        int coverageRow = getSection1CoverageRowNumber(propertyNumber, coverageType);
        
        premium = tableUtils.getCellTextInTableByRowAndColumnName(table_SectionICoveragesLocations(2), coverageRow, "Value");
        return premium;
    }


    public double getStandardLiabilityPremium(String coverageType) {
        String premium = "";
        int coverageRow = getStandardLiabilityPremiumRowNumber(coverageType, table_StandardLiabilityPolicyPremium);
        
        premium = tableUtils.getCellTextInTableByRowAndColumnName(table_StandardLiabilityPolicyPremium, coverageRow, "Premium");
        return NumberUtils.getCurrencyValueFromElement(premium);
    }


    public String getStandardLiabilityCoverages(String coverageType) {
        String value = "";
        int coverageRow = getStandardLiabilityPremiumRowNumber(coverageType, table_StandardLiabilityCoverages);
        
        value = tableUtils.getCellTextInTableByRowAndColumnName(table_StandardLiabilityCoverages, coverageRow, "Value(s)");
        return value;
    }


    public double getStandardInlandMarinePremium(String coverageType) {
        String premium = "";
        int coverageRow = getStandardLiabilityPremiumRowNumber(coverageType, table_SectionIVSummary);
        
        premium = tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIVSummary, coverageRow, "Premium");
        return NumberUtils.getCurrencyValueFromElement(premium);
    }

    // Section II Methods

    public String getSectionIICoveragesValues(String description) {
        String value = "";
        int rowCount = tableUtils.getRowCount(table_SectionIICoveragesDeductibles);
        for (int currentRow = 1; currentRow <= rowCount; currentRow++) {
            String desc = tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIICoveragesDeductibles, currentRow, "Description");
            if (desc.contains(description)) {
                value = tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIICoveragesDeductibles, currentRow, "Value(s)");
                break;
            }
        }
        return value;
    }


    public String getSectionIICoveragesPremiumQuantity(String description) {
        String quantity = "";
        int rowCount = tableUtils.getRowCount(table_SectionIICoveragesPremium);
        for (int currentRow = 1; currentRow <= rowCount; currentRow++) {
            String desc = tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIICoveragesPremium, currentRow, "Description");
            if (desc.trim().contains(description)) {
                quantity = tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIICoveragesPremium, currentRow, "Quantity/Amount");
                break;
            }
        }
        return quantity;
    }


    public String premiumByDescription(String description) {
        
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIICoveragesPremium, tableUtils.getRowInTableByColumnNameAndValue(table_SectionIICoveragesPremium, "Description", description), "Premium");
    }


    public String getSectionIICoveragesQuantityByRow(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIICoveragesPremium, row, "Quantity/Amount");
    }


    public int getSectionIICoveragesTableRowCount() {
        return tableUtils.getRowCount(table_SectionIICoveragesPremium);
    }


    public String getFPPCoverageDeductible() {
        String amount = "";
        List<WebElement> section1Coverages = tableUtils.getAllTableRows(table_FPPCoveragesDeductibles);
        for (WebElement element : section1Coverages) {
            String[] tempArray = element.getText().split("\n");
            
            if (tempArray[0].trim().contains("Deductible")) {
                amount = tempArray[1].trim();
                break;
            }
        }
        return amount;
    }


    public String getFPPCoveragesPremiumQunatity(String description) {
        String quantity = "";
        int rowCount = tableUtils.getRowCount(table_FPPCoveragesPremiumValues);
        for (int currentRow = 1; currentRow <= rowCount; currentRow++) {
            String desc = tableUtils.getCellTextInTableByRowAndColumnName(table_FPPCoveragesPremiumValues, currentRow, "Type");
            if (desc.trim().contains(description)) {
                quantity = tableUtils.getCellTextInTableByRowAndColumnName(table_FPPCoveragesPremiumValues, currentRow, "Quantity");
                break;
            }
        }
        return quantity;

    }


    public String getFPPCoveragesPremiumQunatityByRow(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_FPPCoveragesPremiumValues, row, "Quantity");
    }

    public int getFPPCoveragesTableCount() {
        return tableUtils.getRowCount(table_FPPCoveragesPremiumValues);
    }


    public String getFPPCoveragesPremiumValue(String description) {
        String value = "";
        int rowCount = tableUtils.getRowCount(table_FPPCoveragesPremiumValues);
        for (int currentRow = 1; currentRow <= rowCount; currentRow++) {
            String desc = tableUtils.getCellTextInTableByRowAndColumnName(table_FPPCoveragesPremiumValues, currentRow, "Description");
            if (desc.contains(description)) {
                value = tableUtils.getCellTextInTableByRowAndColumnName(table_FPPCoveragesPremiumValues, currentRow, "Value");
                break;
            }
        }
        return value;
    }

    // Section III Methods

    public String getSectionIIIAutoLineVehicleDetails(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIIIAutoVehicleDetails, row, "Description");
    }


    public int getSectionIIIAutoVehicleRowCount() {
        return tableUtils.getRowCount(table_SectionIIIAutoVehicleDetails);
    }


    public void clickSectionIIIAutoVhicleLinkByRow(int row) {
        tableUtils.clickLinkInSpecficRowInTable(table_SectionIIIAutoVehicleDetails, row);
    }


    public double getSectionIIIVehiclePremiumByRow(int row) {
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIIIAutoVehicleDetails, row, "Premium"));
    }

    //Section IV methods


    public String getSectionIVRecEquipmentDetails(int row, String headerName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIVRecEquipmentDetails, row, headerName);
    }


    public void clickSectionIVRecEquipmentLinkByRow(int row) {
        tableUtils.clickLinkInSpecficRowInTable(table_SectionIVRecEquipmentDetails, row);
        
    }


    public int getSectionIVRecEquipmentRowCount() {
        return tableUtils.getRowCount(table_SectionIVRecEquipmentDetails);
    }


    public String getSectionIVWatercraftDetails(int row, String headerName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIVWatercraft, row, headerName);
    }


    public void clickSectionIVWatercraftLinkByRow(int row) {
        tableUtils.clickLinkInSpecficRowInTable(table_SectionIVWatercraft, row);
        
    }


    public int getSectionIVWatercraftRowCount() {
        return tableUtils.getRowCount(table_SectionIVWatercraft);
    }


    public String getSectionIVFarmEquipmentDetails(int row, String headerName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIVFarmEquipment, row, headerName);
    }


    public void clickSectionIVFarmEquipmentDetailsLinkByRow(int row) {
        tableUtils.clickLinkInSpecficRowInTable(table_SectionIVFarmEquipment, row);
        
    }


    public int getSectionIVFarmEquipmentRowCount() {
        return tableUtils.getRowCount(table_SectionIVFarmEquipment);
    }


    public String getSectionIVPersonalPropertyDetails(int row, String headerName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIVPersonalProperty, row, headerName);
    }


    public void clickSectionIVPersonalPropertyLinkByRow(int row) {
        tableUtils.clickLinkInSpecficRowInTable(table_SectionIVPersonalProperty, row);
        
    }


    public int getSectionIVPersonalPropertyRowCount() {
        return tableUtils.getRowCount(table_SectionIVPersonalProperty);
    }


    public String getSectionIVCargoDetails(int row, String headerName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIVCargo, row, headerName);
    }


    public void clickSectionIVCargoLinkByRow(int row) {
        tableUtils.clickLinkInSpecficRowInTable(table_SectionIVCargo, row);
        
    }

    public int getSectionIVCargoRowCount() {
        return tableUtils.getRowCount(table_SectionIVCargo);
    }


    public String getSectionIVLiveStockDetails(int row, String headerName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIVLiveStock, row, headerName);
    }


    public void clickSectionIVLiveStockLinkByRow(int row) {
        tableUtils.clickLinkInSpecficRowInTable(table_SectionIVLiveStock, row);
        
    }

    public int getSectionIVLiveStockRowCount() {
        return tableUtils.getRowCount(table_SectionIVLiveStock);
    }


    public double getSectionIVInlandMarineSubTotal() {
        int rows = tableUtils.getRowCount(table_SectionIVSummary);
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIVSummary, rows, "Premium"));
    }


    public int getSectionIVInlandMarineSummaryRowCount() {
        return tableUtils.getRowCount(table_SectionIVSummary);
    }


    public void clickLinkSectionIVInlandMarinesummaryLinkByRow(int row) {
        tableUtils.clickLinkInSpecficRowInTable(table_SectionIVSummary, row);
    }


    public Double getSquireRewritePremiumSummaryAmount(int row, boolean gross, boolean net, boolean discount) {

        String amount = "";
        if (gross)
            amount = tableUtils.getCellTextInTableByRowAndColumnName(table_SquirePremiumSummary, row, "Gross");
        else if (net)
            amount = tableUtils.getCellTextInTableByRowAndColumnName(table_SquirePremiumSummary, row, "Net");
        else if (discount)
            amount = tableUtils.getCellTextInTableByRowAndColumnName(table_SquirePremiumSummary, row, "Discounts/Surcharge");

        if (amount.equals("-"))
            amount = "0";

        return Double.parseDouble(amount.replace("$", "").replace(",", "").replace("(", "").replace(")", ""));
    }


    public void clickSectionISectionIIPropertyDetailsLocationLink() {
        clickWhenClickable(link_SectionISectionIIPropertyDetailsLocation);
        
    }


    public String getSectionISectionIIPropertyDetailsLocationText() {
        return link_SectionISectionIIPropertyDetailsLocation.getText();
    }


    public void clickReturnToQuote() {
        super.clickReturnTo();
    }


    public void clickLinkSectionISectionIIPropertyDetailsCoveragesByRowNumber(int row) {
        tableUtils.clickLinkInSpecficRowInTable(table_SectionISectionIIPropertyDetailsCoverages, row);
    }


    public String getSectionISectionIIPropertyDetailscoveragesByRowAndColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SectionISectionIIPropertyDetailsCoverages, row, columnName);
    }


    public int getRowNumberSectionISectionIIPropertyDetailsCoverageByName(String desc) {
        return tableUtils.getRowNumberInTableByText(table_SectionISectionIIPropertyDetailsCoverages, desc);
    }


    public void clickExpandedViewLink() {
        clickWhenClickable(link_ExtendedView);
        
    }


    public void clickCompactViewLink() {
        clickWhenClickable(link_CompactView);
        
    }


    public void clickSectionIIViewSectionIICoverages() {
        clickWhenClickable(link_SectionISectionIIViewSectionIICoverages);
        
    }


    public void clickSectionIIViewAdditionalSectionICoverages() {
        clickWhenClickable(link_SectionISectionIIViewAdditionalSectionICoverages);
        
    }


    public void clickCostChangeDetailsSectionIandII() {
        clickWhenClickable(tab_CostChangeDetailsSectionIAndIIPropertyAndLiabilityLine);
        
    }


    public void clickCostChangeDetailsSectionIII() {
        clickWhenClickable(tab_CostChangeDetailsSectionIIIAutoLine);
        
    }


    public void clickCostChangeDetailsSectionIV() {
        clickWhenClickable(tab_CostChangeDetailsSectionIVInlandMarineLine);
        
    }


    public void clickCostChangeDetailsSectionIandIIPropertyDetailCoverages() {
        clickWhenClickable(tab_CostChangeDetailsSectionIAndIIPropertyAndLiabilityLinePropertyDetailCoverages);
        
    }


    public void clickCostChangeDetailsSectionIandIISectionIICoverages() {
        clickWhenClickable(tab_CostChangeDetailsSectionIAndIIPropertyAndLiabilityLineSectionIICoverages);
        
    }


    public void clickCostChangeDetailsSectionIVRecreationalEquipment() {
        clickWhenClickable(tab_CostChangeDetailsSectionIVRecreationalEquipment);
        
    }


    public void clickCostChangeDetailsSectionIVWatercraft() {
        clickWhenClickable(tab_CostChangeDetailsSectionIVWatercraft);
        
    }


    public void clickCostChangeDetailsSectionIVPersonalProperty() {
        clickWhenClickable(tab_CostChangeDetailsSectionIVPersonalProperty);
        
    }


    public String getValueBeforeChange(String rowColoumName, String description, String coloumName) {

        String value = "";
        int rowCount = tableUtils.getRowCount(table_CostChangeReview);
        for (int currentRow = 1; currentRow <= rowCount; currentRow++) {
            String desc = tableUtils.getCellTextInTableByRowAndColumnName(table_CostChangeReview, currentRow, rowColoumName);
            if (desc.contains(description)) {
                value = tableUtils.getCellTextInTableByRowAndColumnName(table_CostChangeReview, currentRow, coloumName);
                break;
            }
        }
        return value;
    }


    public String getValueAfterChange(String rowColoumName, String description, String coloumName) {
        boolean flag = true;
        String value = "";
        int rowCount = tableUtils.getRowCount(table_CostChangeReview);
        for (int currentRow = 1; currentRow <= rowCount; currentRow++) {
            String desc = tableUtils.getCellTextInTableByRowAndColumnName(table_CostChangeReview, currentRow, rowColoumName);
            if (desc.contains(description)) {
                flag = !flag;
                value = tableUtils.getCellTextInTableByRowAndColumnName(table_CostChangeReview, currentRow, coloumName);
                if (flag)
                    break;
            }
        }
        return value;
    }


    public void clickCostChangeDetails() {
        clickWhenClickable(tab_CostChangeDetails);
        

    }


    public int getUmbrellaQuotePremiumTableRowCount() {
        return tableUtils.getRowCount(table_UmbrellaQuotePremiums);
    }


    public double getUmbrellaQuotePremiumByRowNumber(int row) {
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_UmbrellaQuotePremiums, row, "Premium"));
    }


    public String getUmbrellaQuoteUnderlyingLimits() {
        return text_UnderlyingLimits.getText();
    }


    public String getUmbrellaQuoteIncreasedLimits() {
        return text_IncreasedLimits.getText();
    }


    public boolean checkUmbrellaAllowingCredit(SectionIIGeneralLiabLimit generalLiabilityLimit) {
        boolean valueDisplayed = false;
        List<WebElement> elements = tableUtils.getAllTableRows(table_UmbrellaQuotePremiums);
        for (WebElement element : elements) {
            String[] tempArray = element.getText().split("\n");
            
            if (tempArray[0].trim().contains("First")) {
                String allowingCredit = tempArray[1].trim();

                switch (generalLiabilityLimit.getValue()) {
                    case "1,000,000 CSL":
                        if (allowingCredit.contains("Allowing 30% Credit"))
                            valueDisplayed = true;
                        break;
                    case "300,000 CSL":
                        if (allowingCredit.contains("Allowing 0% Credit"))
                            valueDisplayed = true;
                        break;
                    case "500,000 CSL":
                        if (allowingCredit.contains("Allowing 20% Credit"))
                            valueDisplayed = true;
                        break;
                    case "300/500/300":
                        if (allowingCredit.contains("Allowing 0% Credit"))
                            valueDisplayed = true;
                        break;
                }
                break;
            }
        }

        return valueDisplayed;
    }


    public String getMultiplyMillionStatementByDescription(String desc) {
        String returnValue = "";
        List<WebElement> elements = tableUtils.getAllTableRows(table_UmbrellaQuotePremiums);
        for (WebElement element : elements) {
            String[] tempArray = element.getText().split("\n");
            
            if (tempArray[0].trim().contains(desc)) {
                returnValue = tempArray[1].trim();
                break;
            }
        }
        return returnValue;
    }


    public void clickHandrate() {
        clickWhenClickable(link_Handrate);
        
    }


    public void clickCostChangeDetailsAdditionalSectionOneCoveragesTab() {
        clickWhenClickable(link_CostChangeDetailsAdditionalSectionOneCoverages);
        
    }


    public boolean checkCostChangeDetailsAdditionalSectionOneCoveragesTab() {
        return checkIfElementExists(link_CostChangeDetailsAdditionalSectionOneCoverages, 1000);
    }


    public void enterRatingOverrideByDescription(String desc, String premium, String reason) {
        int row = tableUtils.getRowNumberInTableByText(table_RatingOverrides, desc);
        WebElement premiumLink = table_RatingOverrides.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex, '" + (row - 1) + "')]/td[6]/div"));
        clickWhenClickable(premiumLink);
        
        WebElement premiumElement = table_RatingOverrides.findElement(By.xpath("//input[contains(@name, 'c5')]"));
        premiumElement.sendKeys(premium);
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        WebElement reasonLink = table_RatingOverrides.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex, '" + (row - 1) + "')]/td[7]"));
        clickWhenClickable(reasonLink);
        
        WebElement reasonlement = table_RatingOverrides.findElement(By.xpath("//input[contains(@name, 'c6')]"));
        reasonlement.sendKeys(reason);
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();

    }


    public void clickUpdateButton() {
        super.clickUpdate();
        
    }


    public ArrayList<String> getDescriptionsFromSectionIIICoveragesTable() {
        ArrayList<String> toReturn = tableUtils.getAllCellTextFromSpecificColumn(table_SectionIIIAutoVehicleDetails, "Description");
        return toReturn;
    }


    public ArrayList<String> getDescriptionsFromSectionIPropertyDetailsTable() {
        ArrayList<String> toReturn = tableUtils.getAllCellTextFromSpecificColumn(table_PropertyDetails, "Description");
        return toReturn;
    }


    public boolean checkPropertyCoverageDetailsByColumnNameValue(String desc, String column, String expectedValue) {
        boolean valueFound = false;
        List<WebElement> tableElements = table_PropertyDetails.findElements(By.xpath(".//tbody/descendant::*[contains(text(), '" + desc + "')]/ancestor::tr[1] | .//tbody/descendant::div[contains(text(), '" + desc + "')]/ancestor::tr[1]"));
        int row = 0;
        for (WebElement element : tableElements) {
            row = tableUtils.getRowNumberFromWebElementRow(element);
            if (tableUtils.getCellTextInTableByRowAndColumnName(table_PropertyDetails, row, column).contains(expectedValue)) {
                valueFound = true;
                break;
            }
        }
        return valueFound;
    }


    public ArrayList<String> getDescriptionFromSectionILineCoveragesTable() {
        ArrayList<String> toReturn = tableUtils.getAllCellTextFromSpecificColumn(table_SectionILineCoverages, "Description");
        return toReturn;
    }


    public String getSectionIIICoverageValueBeforeChange(String rowName, String coloumName) {
        int row = tableUtils.getRowNumberInTableByText(table_SectionIIIAutoVehicleDetails, rowName);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIIIAutoVehicleDetails, row, coloumName);
    }


    public String getSectionIIICoverageValueAfterChange(String rowName, String coloumName) {
        int row = tableUtils.getRowNumberInTableByText(table_SectionIIIAutoVehicleDetails, rowName);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SectionIIIAutoVehicleDetails, row + 1, coloumName);
    }


    public boolean checkPrintQuoteButtonExists() {
        return checkIfElementExists("//span[contains(@id, ':CreateSubmissionQuote-btnEl') or contains(@id, ':CreateIssuanceQuote-btnEl') or contains(@id, ':CreatePolicyChangeQuote-btnEl') or contains(@id, ':ReinstateQuote-btnEl') or contains(@id, ':CreateRewriteQuote-btnEl')]", 500);
    }


    public double getBOPLineLevelCoverageChangesPremium(String desc) {
        int row = tableUtils.getRowNumberInTableByText(table_BOPLineLevelCoverageChanges, desc);
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_BOPLineLevelCoverageChanges, row, "Premium"));
    }


    public String getPropertiesMultipleTablesDescriptionColoumFirstRowText(int tableNum) {
        WebElement particularLocationTable = table_CostchangeDetailsMultipleLocations.findElement(By.xpath("//div[contains(@id,'Common_MultiLine_QuoteScreenPanelSet:CostChangeDetailCV:0:RatingTxDetailsPanelSet:secOneLocWrapperIterator:" + tableNum + ":0:1')]"));
        return tableUtils.getCellTextInTableByRowAndColumnName(particularLocationTable, 1, "Description");
    }


    public ArrayList<String> getAllCellTextFromSpecificVehicleSpecificColoum(int tableNum, String coloumName) {
        WebElement specificVehicleTable = table_ExpandedViewMultipleVehicles.findElement(By.xpath("//div[contains(@id,':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:PersonalAutoLinePanelSet:" + tableNum + ":0')]"));
        return tableUtils.getAllCellTextFromSpecificColumn(specificVehicleTable, coloumName);
    }


    public String getSectionIIISpecificVehicleCoverageValueBeforeChange(int vehicleNum, String rowName, String coloumName) {
        WebElement specificVehicleTable = table_ExpandedViewMultipleVehicles.findElement(By.xpath("//div[contains(@id,':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:PersonalAutoLinePanelSet:" + vehicleNum + ":0')]"));
        int row = tableUtils.getRowNumberInTableByText(specificVehicleTable, rowName);
        return tableUtils.getCellTextInTableByRowAndColumnName(specificVehicleTable, row, coloumName);
    }


    public String getSectionIIISpecificVehicleCoverageValueAfterChange(int vehicleNum, String rowName, String coloumName) {
        WebElement specificVehicleTable = table_ExpandedViewMultipleVehicles.findElement(By.xpath("//div[contains(@id,':Common_MultiLine_QuoteScreenPanelSet:PolicyPremiumCV:0:PolicyLineCV:PersonalAutoLinePanelSet:" + vehicleNum + ":0')]"));
        int row = tableUtils.getRowNumberInTableByText(specificVehicleTable, rowName);
        return tableUtils.getCellTextInTableByRowAndColumnName(specificVehicleTable, row + 1, coloumName);
    }


    public void setPolicyPremiumFields(GeneratePolicy policy) {
        PolicyPremium premium = new GuidewireHelpers(getDriver()).getPolicyPremium(policy);
        switch (policy.productType) {
            case Businessowners:
                //Membership dues do not show up on the Quote page for BOP policies right now. Therefore, we have to make a branch that doesn't attempt to get this field.
                //Otherwise, it will reset the membership dues amount back to zero. This can be removed once membership dues are displayed on the quote screen for BOPs again.
                premium.setTotalNetPremium(getQuoteTotalPremium());
                break;
            case CPP:
                premium.setInsuredPremium(premium.getTotalNetPremium());
                break;
            case Membership:
                break;
            case PersonalUmbrella:
                premium.setTotalNetPremium(getQuoteTotalPremium());
                break;
            case Squire:
                premium.setTotalGrossPremium(getQuoteTotalGrossPremium());
                premium.setTotalDiscountsSurcharges(getQuoteTotalDiscountsSurcharges());
                premium.setTotalNetPremium(getQuoteTotalNetPremium());
                //This block is needed because membership dues only show up on the quote page of the job in which they were added to the policy.
                //Thus, if we got the membership dues amount in a previous job (i.e. - submission), we will need to skip overwriting this field.
                double membershipDuesAmountPlaceholder = getQuoteTotalMembershipDues();
                if (membershipDuesAmountPlaceholder > 0.00) {
                    premium.setMembershipDuesAmount(premium.getMembershipDuesAmount() + membershipDuesAmountPlaceholder);
                }
                premium.setSr22ChargesAmount(getQuoteSR22Charge());

                //This block is needed because membership dues only show up on the quote page of the job in which they were added to the policy.
                //This effects the value of the total cost to insured cell.
                //Thus, if we got the membership dues amount in a previous job (i.e. - submission), we will need to skip overwriting this field.
                double totalCostToInsuredAmountPlaceholder = getQuoteTotalCostToInsured();
                if (membershipDuesAmountPlaceholder > 0.00) {
                    premium.setTotalCostToInsured(totalCostToInsuredAmountPlaceholder);
                } else if (premium.getTotalCostToInsured() < totalCostToInsuredAmountPlaceholder) {
                    premium.setTotalCostToInsured(totalCostToInsuredAmountPlaceholder);
                }
                //else membership dues were never charged on the policy, or they were charged previously and have been captured correctly.
                //The same method above is also needed for Lienholder calculations...
                double totalLienholderBilledAmountPlaceholder = getQuoteTotalLienAmountBilled();
                if (membershipDuesAmountPlaceholder > 0.00) {
                    premium.setLienBilledAmount(totalLienholderBilledAmountPlaceholder);
                } else if (premium.getLienBilledAmount() < totalLienholderBilledAmountPlaceholder) {
                    premium.setLienBilledAmount(totalLienholderBilledAmountPlaceholder);
                }
                //else membership dues were never charged TO THE LIENHOLDER on the policy, or they were charged previously and have been captured correctly.

                premium.setChangeInCost(getQuoteChangeInCost());
                break;
            //		case StandardFL:
            //			break;
            case StandardIM:
                premium.setTotalNetPremium(getQuoteTotalCostToInsured());
                premium.setMembershipDuesAmount(getQuoteTotalMembershipDues());
                break;
            case StandardFire:
                break;
            case StandardLiability:
                break;
            default:
                premium.setTotalNetPremium(getQuoteTotalPremium());
                premium.setMembershipDuesAmount(getQuoteTotalMembershipDues());
                break;
        }
    }


    public void handlePreQuoteIssues(GeneratePolicy policy) throws Exception {
        if (isPreQuoteDisplayed()) {
            clickPreQuoteDetails();
            

            repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(getDriver());
            risk.clickUWIssuesTab();
            
            if (policy.productType.equals(ProductLineType.Squire) ||
                    policy.productType.equals(ProductLineType.StandardFire) ||
                    policy.productType.equals(ProductLineType.StandardLiability)) {
                risk.handleBlockQuote(policy);
            } else {
                risk.handleBlockQuoteRelease(policy);
            }
        }


    }


    public boolean checkCostChangeDetailsAdditionalSectionOneCoveragesByType(String type) {
        boolean valueFound = false;
        if (tableUtils.getRowNumberInTableByText(table_CostChangeDetailsAdditionalSectionOneCoverages, type) > 0) {
            valueFound = true;
        }
        return valueFound;
    }


    public double getSubtotalPremiumValueFromCostChangeDetails() {
        WebElement element = find(By.xpath("//tr/td/div[contains(text(),'Subtotal')]/ancestor::td/following-sibling::td/div/a"));
        return NumberUtils.getCurrencyValueFromElement(element.getText());
    }

    public Double getMembershipMemberNetAmountByMemberName(String memberName) {
        return NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_MembershipLineSummary, tableUtils.getRowNumberInTableByText(table_MembershipLineSummary, memberName), "Net"));
    }


}










































