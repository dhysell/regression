package repository.pc.workorders.generic;


import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PolicyPremium;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.idfbins.enums.OkCancel;

import persistence.globaldatarepo.entities.Underwriters;
import repository.pc.activity.UWActivityPC;
import repository.pc.sidemenu.SideMenuPC;

import java.util.Date;

public class GenericWorkorderRiskAnalysis extends GenericWorkorder {

	private WebDriver driver;

	public GenericWorkorderRiskAnalysis(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//a[contains(@id, '__crumb__')]")
	private WebElement link_ReturnToRiskAnalysisPage;

	public void clickReturnRiskAnalysisPage() {
		clickWhenClickable(link_ReturnToRiskAnalysisPage);
		long endTime = new Date().getTime() + 5000;
		do {

		}
		while (finds(By.xpath("//span[contains(@id, 'Job_RiskAnalysisScreen:RiskAnalysisCV:EvaluationIssuesCardTab-btnEl')]")).isEmpty() && new Date().getTime() < endTime);
	}

	@FindBy(xpath = "//span[contains(@id, 'Job_RiskAnalysisScreen:RiskAnalysisCV:PriorPolicyCardTab-btnEl')]")
	public WebElement PriorPolicyCardTab;

	public void clickPriorPolicyCardTab() {
		if (checkIfElementExists(PriorPolicyCardTab, 2000)) {
			clickWhenClickable(PriorPolicyCardTab);
		}
	}

	@FindBy(xpath = "//span[contains(@id, 'Job_RiskAnalysisScreen:RiskAnalysisCV:ClaimsCardTab')]")
	public WebElement ClaimsCardTab;

	public void clickClaimsCardTab() {
		waitUntilElementIsVisible(ClaimsCardTab);
		clickWhenClickable(ClaimsCardTab);
	}

	@FindBy(xpath = "//span[contains(@id, 'Job_RiskAnalysisScreen:RiskAnalysisCV:EvaluationIssuesCardTab-btnEl')]")
	public WebElement EvaluationIssuesCardTab;

	public void clickUWIssuesTab() {
		//        waitUntilElementIsVisible(EvaluationIssuesCardTab);
		//        EvaluationIssuesCardTab.click();
		clickWhenClickable(EvaluationIssuesCardTab);
	}

	@FindBy(xpath = "//span[contains(@id, 'Job_RiskAnalysisScreen:RiskAnalysisCV:LossHistoryCardTab-btnEl')]")
	public WebElement LossHistoryCardTab;

	public void clickPriorLossesCardTab() {
		if (checkIfElementExists(PriorPolicyCardTab, 2000)) {
			clickWhenClickable(LossHistoryCardTab);
		}
		long endTime = new Date().getTime() + 5000;
		do {
			waitForPostBack();
		}while (finds(By.xpath("//label[text()='Prior Losses']")).isEmpty() && new Date().getTime() < endTime);
	}

	@FindBy(xpath = "//span[contains(@id, 'Job_RiskAnalysisScreen:RiskAnalysisCV:LossRatiosTab-btnEl')]")
	private WebElement lossRatioTab;

	public void clickLossRatioTab() {
		clickWhenClickable(lossRatioTab);
	}

	@FindBy(xpath = "//span[contains(@id, 'Job_RiskAnalysisScreen:RiskAnalysisCV:PredictiveAnalyticsTab-btnEl')]")
	private WebElement predictiveAnalyticsTab;

	public void clickPredictiveAnalyticsTab() {
		clickWhenClickable(predictiveAnalyticsTab);
	}

	public FullUnderWriterIssues approveAll() throws Exception {
		return new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(getDriver()).approveAll();
	}

	public void approveAll_IncludingSpecial() throws Exception {
		waitForPostBack();
		new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(getDriver()).approveAll_IncludingSpecial();
	}

	public FullUnderWriterIssues getUnderwriterIssues() {
		return new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(getDriver()).getUnderwriterIssues();
	}


	public void fillOutRiskAnalysis(GeneratePolicy policy) {
		fillOutUWIssues(policy);
		fillOutPriorPolicies(policy);
		fillOutClaims(policy);
		fillOutPriorLosses(policy);
		fillOutLossRatios(policy);
	}


	public void fillOutUWIssues(GeneratePolicy policy) {
		clickUWIssuesTab();
	}

	public void fillOutPriorPolicies(GeneratePolicy policy) {
		clickPriorPolicyCardTab();
	}

	public void fillOutClaims(GeneratePolicy policy) {
		clickClaimsCardTab();
	}

	public void fillOutPriorLosses(GeneratePolicy policy) {
		clickPriorLossesCardTab();
	}

	public void fillOutMotorVehicleRecords(GeneratePolicy policy) {

	}

	public void fillOutLossRatios(GeneratePolicy policy) {
		clickLossRatioTab();
	}


	public void Quote() {
		clickGenericWorkorderQuote();
	}

	public void clickQuoteWithoutDocuments() {
		clickWhenClickable(By.xpath("//span[contains(@id, ':QuoteWithoutDocuments-btnInnerEl')]"));
		selectOKOrCancelFromPopup(OkCancel.OK);
	}

	public boolean isQuotable() {
		return super.isQuotable();
	}

	@FindBy(xpath = "//span[contains(@id, 'RiskAnalysisCV_tb:RequestApproval-btnEl')]")
	private WebElement requestApproval;

	@FindBy(xpath = "//a[contains(@id, 'RiskAnalysisCV_tb:RequestApproval')]")
	private WebElement link_requestApproval;

	public void clickRequestApproval() {
		clickWhenClickable(requestApproval);
		waitForPostBack();
	}

	public boolean checkRequestApprovalDisabled(){    	
		return link_requestApproval.getAttribute("class").contains("x-btn-disabled");
	}

	public String getRequestApporvalToolTip(){    	
		return link_requestApproval.getAttribute("data-qtip");
	}

	@FindBy(xpath = "//span[contains(@id, ':Unlock-btnEl')]")
	private WebElement button_ReleaseLock;

	public void clickReleaseLock() {
		button_ReleaseLock.click();


		repository.pc.activity.UWActivityPC activity = new repository.pc.activity.UWActivityPC(driver);
		activity = new repository.pc.activity.UWActivityPC(driver);
		activity.setText("Stuff Approved");
		activity.clickSendRequest();
	}


	public void requestApproval(Underwriters uw) {
		repository.pc.sidemenu.SideMenuPC sideBar = new repository.pc.sidemenu.SideMenuPC(driver);
		sideBar.clickSideMenuRiskAnalysis();
		int lcv = 0;
		do {

			clickRequestApproval();
			lcv++;
		}
		while (checkIfElementExists("//span[contains(@id, 'UWActivityPopup:Update-btnEl') or contains(@id, 'UWActivityPolicyChangePopup:Update-btnEl')]", 1000) == false && lcv < 10);

		repository.pc.activity.UWActivityPC activity = new repository.pc.activity.UWActivityPC(driver);
		activity.setText("Please Approve this Stuff!!");

		activity.setAssignTo(uw.getUnderwriterUserName());
		activity.setNewNoteSubject("Approval Needed");
		activity.clickSendRequest();
	}


	public void requestApproval() {
		repository.pc.sidemenu.SideMenuPC sideBar = new repository.pc.sidemenu.SideMenuPC(driver);
		sideBar.clickSideMenuRiskAnalysis();
		int lcv = 0;
		do {

			clickRequestApproval();
			lcv++;
		}
		while (checkIfElementExists("//span[contains(@id, 'UWActivityPopup:Update-btnEl') or contains(@id, 'UWActivityPolicyChangePopup:Update-btnEl')]", 1000) == false && lcv < 10);

		repository.pc.activity.UWActivityPC activity = new UWActivityPC(driver);
		activity.setText("Please Approve this Stuff!!");
		activity.clickSendRequest();
	}


	@FindBy(xpath = "//div[contains(@id, 'WebMessageWorksheet:WebMessageWorksheetScreen:grpMsgs')]")
	private WebElement validation_Message;

	public String getValidationMessagesText() {
		try {
			return validation_Message.getText();
		} catch (Exception e) {
			return "";
		}
	}

	public void clickClearButton() {
		clickClear();

	}

	@FindBy(xpath = "//input[contains(@id, ':LossRatiosPanelSet:0:SectionDiscountDV:reasonForChange-inputEl') or contains(@id, ':RiskAnalysisCV:LossRatiosPanelSet:stdFireReasonForChange-inputEl')]")
	private WebElement editbox_ReasonForChange;

	public void setPropertyReasonForChange(String value) {

		setText(editbox_ReasonForChange, value);
	}


	@FindBy(xpath = "//input[contains(@id, ':LossRatiosPanelSet:1:SectionDiscountDV:reasonForChange-inputEl')]")
	private WebElement editbox_LiabilityReasonForChange;

	public void setLiabilityReasonForChange(String value) {

		setText(editbox_LiabilityReasonForChange, value);
	}

	@FindBy(xpath = "//input[contains(@id, ':LossRatiosPanelSet:2:SectionDiscountDV:reasonForChange-inputEl')]")
	private WebElement editbox_autoReasonForChange;

	public void setAutoReasonForChange(String value) {

		setText(editbox_autoReasonForChange, value);
	}

	@FindBy(xpath = "//input[contains(@id, ':LossRatiosPanelSet:3:SectionDiscountDV:reasonForChange-inputEl')]")
	private WebElement editbox_IMReasonForChange;

	public void setIMReasonForChange(String value) {

		setText(editbox_IMReasonForChange, value);
	}


	@FindBy(xpath = "//label[contains(@id, ':Warning')]")
	private WebElement label_WarningMessage;

	public String getWarningMessage() {
		return label_WarningMessage.getText();
	}

	@FindBy(xpath = "//a[contains(@id, ':HandlePreemptions')]")
	private WebElement button_HandlePreemption;

	public void clickHandlePreemption() {
		clickWhenClickable(button_HandlePreemption);
	}


	public void clickQuoteOptionsQuote() {
		super.clickGenericWorkorderQuoteOptionsQuote();
	}

	public void performRiskAnalydidAndQuote_UWIssuesOnly(GeneratePolicy policy) {
		repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
		Quote();

		sideMenu.clickSideMenuRiskAnalysis();
		repository.pc.workorders.generic.GenericWorkorderQuote quote = new repository.pc.workorders.generic.GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			systemOut("Pre-Quote Button was displayed, Handling it now.");
			quote.clickPreQuoteDetails();

			clickUWIssuesTab();

		}

		clickUWIssuesTab();

		switch (new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy)) {
		case FullApp:
			policy.allUWIssuesAfterFA = getUnderwriterIssues();
			break;
		case PolicyIssued:
			policy.allUWIssuesAfterIssuance = getUnderwriterIssues();
			break;
		case PolicySubmitted:
			break;
		case QuickQuote:
			policy.allUWIssuesAfterQQ = getUnderwriterIssues();
			break;
		}
	}


	public void performRiskAnalysisAndQuoteQQ(GeneratePolicy policy) throws Exception {

		repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);

		sideMenu.clickSideMenuRiskAnalysis();
		if (!policy.productType.equals(ProductLineType.PersonalUmbrella)) {
			clickPriorPolicyCardTab();
			clickPriorLossesCardTab();
		}//END IF
		clickClaimsCardTab();

		Quote();
		repository.pc.workorders.generic.GenericWorkorderQuote quote = new repository.pc.workorders.generic.GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
			clickUWIssuesTab();
			systemOut("Releasing Block Quote Now.");
			new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(getDriver()).handleBlockQuoteRelease(policy);
		}//END IF

		ErrorHandlingHelpers quoteError = new ErrorHandlingHelpers(getDriver());
		if (quoteError.areThereErrorMessages()) {
			// ACCOUNT FOR "An invalid quote was Generated"
			// PLEASE DO NOT COMMENT OUT, IF ERROR - FIX OR LET JON LARSEN KNOW :)
			if (quoteError.errorHandlingRiskAnalysis()) {
				Assert.fail("Generate Was NOT able to generate a valid quote after five(5) tries or two(2) minutes.");
			}
		}//END IF
		PolicyPremium premium = new GuidewireHelpers(getDriver()).getPolicyPremium(policy);
		if (policy.handleBlockSubmitRelease || policy.handleBlockSubmit) {

			if (policy.productType.equals(ProductLineType.Squire)) {
				if (policy.squire.alwaysOrderCreditReport) {
					premium.setTotalGrossPremium(quote.getQuoteTotalGrossPremium());
					premium.setTotalDiscountsSurcharges(quote.getQuoteTotalDiscountsSurcharges());
					premium.setTotalNetPremium(quote.getQuoteTotalNetPremium());
					premium.setMembershipDuesAmount(quote.getQuoteTotalMembershipDues());
					premium.setMembershipDuesAmount(premium.getMembershipDuesAmount() + premium.getMembershipDuesAmountForPrimaryNamedInsured());
					premium.setSr22ChargesAmount(quote.getQuoteSR22Charge());
					premium.setLienBilledAmount(quote.getQuoteTotalLienAmountBilled());
					premium.setTotalCostToInsured(quote.getQuoteTotalCostToInsured());
				}
			} else {
				if (policy.productType.equals(ProductLineType.StandardIM) || policy.lineSelection.contains(LineSelection.StandardLiabilityPL)) {
					premium.setTotalNetPremium(quote.getQuoteTotalGrossPremium());
				} else {
					premium.setTotalNetPremium(quote.getQuoteTotalPremium());
				}

				if (!policy.productType.equals(ProductLineType.PersonalUmbrella)) {
					premium.setMembershipDuesAmount(quote.getQuoteTotalMembershipDues());
				}
			}//END ELSE
		}//END ELSE
	}


	public void performRiskAnalysisAndQuote(GeneratePolicy policy) throws Exception {
		repository.pc.sidemenu.SideMenuPC sideMenuStuff = new SideMenuPC(driver);

		sideMenuStuff.clickSideMenuRiskAnalysis();

		if (!policy.productType.equals(ProductLineType.PersonalUmbrella)) {
			clickPriorPolicyCardTab();
			clickPriorLossesCardTab();
		}//END IF
		clickClaimsCardTab();
		Quote();
		repository.pc.workorders.generic.GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

		//THIS WAS A STACK OF IF ELSE STATEMENTS.
		//Issue is that if there are instances when 2 of the case needs to be handled it can't. Like clock was moved and pre-quote issue.
		ErrorHandlingHelpers quoteError = new ErrorHandlingHelpers(getDriver());
		if (!(policy.productType.equals(ProductLineType.Businessowners) || policy.productType.equals(ProductLineType.CPP))) {
			if (getValidationMessagesText().contains("Credit Report is required to quote policy")) {
				systemOut("Received a Credit report required error message. Clearing it now. If this test fails, Check if Verisk script parameter has to be flipped ");
				clickClearButton();
				systemOut("Clear Button Clicked.");

				if (policy.productType.equals(ProductLineType.StandardFire) || policy.productType.equals(ProductLineType.StandardLiability) || policy.productType.equals(ProductLineType.StandardIM)) {
					sideMenuStuff.clickSideMenuPLInsuranceScore();
					Quote();
				}
			}
		}
		// @editor ecoleman 5/14/18 : This was unreachable code for anything but BOP/CPP, and yet it checks for that stuff inside?
		if (quote.isPreQuoteDisplayed()) {
			systemOut("Pre-Quote Button was displayed, Handling it now.");
			quote.clickPreQuoteDetails();
			clickUWIssuesTab();
			if (policy.productType.equals(ProductLineType.Squire) ||
					policy.productType.equals(ProductLineType.StandardFire) ||
					policy.productType.equals(ProductLineType.StandardLiability) ||
					policy.productType.equals(ProductLineType.PersonalUmbrella)) {
				systemOut("Handling Block Quote Now.");
				new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(getDriver()).handleBlockQuote(policy);
			} else {
				systemOut("Releasing Block Quote Release Now.");
				new GenericWorkorderRiskAnalysis_UWIssues(getDriver()).handleBlockQuoteRelease(policy);
			}
		}

		//        @editor ecoleman : is handled by the above, only issue is if this was designed to specificially not do this for BOP. Let me know!
		//        if (quote.isPreQuoteDisplayed() && policy.productType.equals(ProductLineType.CPP)) {
		//            systemOut("Pre-Quote Button was displayed, Handling it now.");
		//            quote.clickPreQuoteDetails();
		//
		//            clickUWIssuesTab();
		//
		//            systemOut("Releasing Block Quote Now.");
		//            new GenericWorkorderRiskAnalysis_UWIssues().handleBlockQuoteRelease(policy);
		//        }//END IF
		waitForPostBack(120);
		//        if (quoteError.areThereErrorMessages()) {
		// ACCOUNT FOR "An invalid quote was Generated"
		// PLEASE DO NOT COMMENT OUT, IF ERROR - FIX OR LET JON LARSEN KNOW :)
		if (quoteError.errorHandlingRiskAnalysis()) {
			Assert.fail("Generate Was NOT able to generate a valid quote after five(5) tries or two(2) minutes.");
		}
		//        }//END IF

		InfoBar infoBar = new InfoBar(getDriver());
		String jobLabel = infoBar.getInfoBarJobLabel();
		PolicyPremium premium = new GuidewireHelpers(getDriver()).getPolicyPremium(policy);
		if (!jobLabel.contains("Policy Change")) {
			if (policy.productType.equals(ProductLineType.Squire)) {
				if (policy.squire.alwaysOrderCreditReport) {
					premium.setTotalGrossPremium(quote.getQuoteTotalGrossPremium());
					premium.setTotalDiscountsSurcharges(quote.getQuoteTotalDiscountsSurcharges());
					premium.setTotalNetPremium(quote.getQuoteTotalNetPremium());
					premium.setMembershipDuesAmount(quote.getQuoteTotalMembershipDues());
					premium.setSr22ChargesAmount(quote.getQuoteSR22Charge());
					premium.setLienBilledAmount(quote.getQuoteTotalLienAmountBilled());
					premium.setTotalCostToInsured(quote.getQuoteTotalCostToInsured());
				}
			} else {
				if (policy.productType.equals(ProductLineType.StandardIM) || policy.lineSelection.contains(LineSelection.StandardLiabilityPL)) {
					premium.setTotalNetPremium(quote.getQuoteTotalGrossPremium());
				} else {
					//Needed to get the membership dues on the quote page. Currently showing nothing on this page. It is shown on the Payment page, so we will get it there for now.
					//policy.membershipDuesAmount = quote.getQuoteTotalMembershipDues();
					premium.setTotalNetPremium(quote.getQuoteTotalPremium());
				}
				if (!policy.productType.equals(ProductLineType.PersonalUmbrella)) {
					premium.setMembershipDuesAmount(quote.getQuoteTotalMembershipDues());
				}

			}
		}
		//END ELSE
	}//END performRiskAnalysisAndQuote()


}
























