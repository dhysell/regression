package repository.pc.workorders.generic;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.idfbins.enums.OkCancel;

import repository.gw.activity.ActivityPopup;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SR22FilingFee;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.UnderwriterIssue;
import repository.gw.generate.custom.UnderwritingIssueHistory;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import repository.gw.topinfo.TopInfo;
import persistence.globaldatarepo.entities.CSRs;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.pc.account.AccountSummaryPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.desktop.DesktopMyActivitiesPC;
import repository.pc.search.SearchAccountsPC;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderRiskAnalysis_UWIssues extends GenericWorkorderRiskAnalysis {

	private WebDriver driver;

	public GenericWorkorderRiskAnalysis_UWIssues(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//div[contains(@id,':Job_RiskAnalysisScreen:RiskAnalysisCV:EvaluationIssuesCardTab:panelId')]//div[contains(@id, '-body')]//table")
	private WebElement table_UWIssues;


	private List<WebElement> buttons_SpecialApproveActive() {
		return table_UWIssues.findElements(By.xpath(".//td/div/a[contains(.,'Special Approve')]"));
	}

	private List<WebElement> buttons_SpecialApproveInactive() {
		return table_UWIssues.findElements(By.xpath(".//td/div/span[contains(.,'Special Approve')]"));
	}

	@FindBy(xpath = "//div[contains(@id, ':RiskEvaluationPanelSet:0-body')]/descendant::tbody/child::tr/child::td/div[contains(text(), 'Blocking Bind') or contains(text(), 'Blocking Submit')]")
	private WebElement blockSubmit;

	@FindBy(xpath = "//div[contains(@id, ':RiskEvaluationPanelSet:0-body')]/descendant::tbody/child::tr/child::td/div[contains(text(), 'Informational')]")
	private WebElement informational;

	@FindBy(xpath = "//div[contains(@id, ':RiskEvaluationPanelSet:0-body')]/descendant::tbody/child::tr/child::td/div[contains(text(), 'Blocking Issuance')]")
	private WebElement blockIssuance;

	@FindBy(xpath = "//div[contains(@id, ':RiskEvaluationPanelSet:0-body')]/descendant::tbody/child::tr/child::td/div[contains(text(), 'Informational')]")
	private WebElement blockQuote;

	@FindBy(xpath = "//a[not(contains(@class, '-disabled'))]/span/span[contains(@id, ':Approve-btnEl')]")
	private WebElement approveAllButton;

	private List<WebElement> buttons_Review() {
		return table_UWIssues.findElements(By.xpath(".//td/div/a[contains(.,'Review')]"));
	}
	
	
	private List<WebElement> buttons_Reopen() {
		return table_UWIssues.findElements(By.xpath(".//td/div/a[contains(.,'Reopen')]"));
	}


	@FindBy(xpath = "//div[contains(@id,'UWIssueHistory:UWIssueHistoryLV')]")
	private WebElement table_UWIssuesHistory;
	
	@FindBy(xpath = "//div[contains(@id,'RiskApprovalDetailsPopup:0:IssueDetailsDV:UWApproval:UWApprovalLV')]")
	private WebElement table_UWIssuesNewApproval;
	

	public boolean checkIfSpecialApproveButtonsExist(boolean active) {
		if (active) {
			return buttons_SpecialApproveActive().size() > 0;
		} else {
			return buttons_SpecialApproveInactive().size() > 0;
		}
	}

	public boolean checkIfSpecialApproveButtonsExist() {
		return !finds(By.xpath("//a[contains(@id, ':SpecialApprove')]")).isEmpty() || !finds(By.xpath("//span[contains(@id, ':SpecialApprove')]")).isEmpty();
	}


	// GET BLOCK Submit LIST
	public List<WebElement> getBlockSubmitList() {
		List<WebElement> blockSubmitList = new ArrayList<WebElement>();
		try {
			int start = Integer.parseInt(blockSubmit.findElement(By.xpath(".//parent::td/parent::tr")).getAttribute("data-recordindex")) + 1;
			int end;
			try {
				end = Integer.parseInt(informational.findElement(By.xpath(".//parent::td/parent::tr")).getAttribute("data-recordindex")) - 1;
			} catch (Exception e) {
				end = Integer.parseInt(blockIssuance.findElement(By.xpath(".//parent::td/parent::tr")).getAttribute("data-recordindex")) - 1;
			}

			for (int i = start; i <= end; i++) {
				blockSubmitList.add(find(By.xpath("//a[contains(@id, 'RiskEvaluationPanelSet:" + i + ":UWIssueRowSet:ShortDescription')]")));
			}
		} catch (Exception e) {
		}
		return blockSubmitList;
	}

	// GET ALL UW ISSUES

	public List<WebElement> getUWIssuesList() {
		List<WebElement> uwIssueList = table_UWIssues.findElements(By.xpath("./tbody/child::tr"));
		return uwIssueList;
	}

	
	
	
	

	public UnderwriterIssue getUWIssue(WebElement trWithIssue, UnderwriterIssueType type) {
		UnderwriterIssue returnUWIssue = new UnderwriterIssue();
		returnUWIssue.setType(type);

		if (!trWithIssue.findElements(By.xpath(".//child::td[2]/div/a")).isEmpty()) {
			clickWhenClickable(trWithIssue.findElement(By.xpath(".//child::td[2]/div/a")));
			returnUWIssue.setLongDescription(find(By.xpath("//textarea[contains(@id, ':IssueLongDescription-inputEl')]")).getText());
			returnUWIssue.setShortDescription(find(By.xpath("//label[contains(@id, 'IssueLongDescription-labelEl')]")).getText());
			//JLARSEN 5/29
			//THIS IS ADDED BECAUSE THE BROWSER IS 'NORMALIZING' THE TEXT BY REMOVEING THE DOUBLE SPACE AFTER A PERIOD WHILE GETTING TEXT FROM THE LABEL
			if (returnUWIssue.getShortDescription().equals("Policy Effective Date is equal to or more than your 10 day submitting authority. If you want this date, please request approval from Underwriting.")) {
				returnUWIssue.setShortDescription("Policy Effective Date is equal to or more than your 10 day submitting authority.  If you want this date, please request approval from Underwriting.");
			}
			
			UnderwritingIssueHistory history = new UnderwritingIssueHistory();
			List<WebElement> historyRows = finds(By.xpath("//div[contains(@id, ':IssueDetailsDV:UWIssueHistory:UWIssueHistoryLV-body')]/div/table/tbody/child::tr"));
			for(WebElement historyRow : historyRows) {
				history.setUser(historyRow.findElement(By.xpath(".//td[1]/div")).getText());
				history.setDate(historyRow.findElement(By.xpath(".//td[2]/div")).getText());
				history.setEffectiveDate(historyRow.findElement(By.xpath(".//td[3]/div")).getText());
				if(!historyRow.findElements(By.xpath(".//td[4]/div")).isEmpty()) {
					history.setPolicyTransaction(historyRow.findElement(By.xpath(".//td[4]/div")).getText());
				} else {
					history.setPolicyTransaction(historyRow.findElement(By.xpath(".//td[4]/div/a")).getText());
				}
				history.setAllowEdit(historyRow.findElement(By.xpath(".//td[5]/div")).getText());
				history.setThrough(historyRow.findElement(By.xpath(".//td[6]/div")).getText());
				history.setValidUntil(historyRow.findElement(By.xpath(".//td[7]/div")).getText());
				history.setStatus(historyRow.findElement(By.xpath(".//td[8]/div")).getText());
				returnUWIssue.getHistory().add(history);
			}
			clickReturnRiskAnalysisPage();
		}//END IF

		return returnUWIssue;
	}//END getUWIssue

	public UnderwriterIssue getUWIssueBasic(WebElement trWithIssue, UnderwriterIssueType type) {
		UnderwriterIssue returnUWIssue = new UnderwriterIssue();
		returnUWIssue.setType(type);

		if (!trWithIssue.findElements(By.xpath(".//child::td[2]/div/a")).isEmpty()) {
			returnUWIssue.setShortDescription(trWithIssue.findElement(By.xpath(".//child::td[2]/div/a")).getText());
		}//END IF
		if(returnUWIssue.getShortDescription().equals("Policy Effective Date is equal to or more than your 10 day submitting authority. If you want this date, please request approval from Underwriting.")) {
			returnUWIssue.setShortDescription("Policy Effective Date is equal to or more than your 10 day submitting authority.  If you want this date, please request approval from Underwriting.");
		}
		return returnUWIssue;
	}//END getUWIssue


	/**
	 * @param fullUWIssues
	 * @param uwIssue
	 * @return UnderwriterIssueType
	 * @author jlarsen
	 * @Description - Loops thru Object lists and returns what list type it found the UW Issue in.
	 * @DATE - Sep 14, 2016
	 */

	public UnderwriterIssueType hasUWIssue(FullUnderWriterIssues fullUWIssues, String uwIssue) {
		repository.pc.sidemenu.SideMenuPC sidemenu = new repository.pc.sidemenu.SideMenuPC(driver);
		sidemenu.clickSideMenuRiskAnalysis();
		clickUWIssuesTab();

		for (UnderwriterIssue listIssue : fullUWIssues.getInformationalList()) {
			if (listIssue.getLongDescription() == null) {

			}
			if (listIssue.getShortDescription() == null) {

			}
			if (uwIssue != null) {

				if (listIssue.getLongDescription().contains(uwIssue) || listIssue.getShortDescription().contains(uwIssue)) {
					return UnderwriterIssueType.Informational;
				}
			} else {

			}

		}

		for (UnderwriterIssue listIssue : fullUWIssues.getBlockSubmitList()) {
			if (listIssue.getLongDescription().contains(uwIssue) || listIssue.getShortDescription().contains(uwIssue)) {
				return UnderwriterIssueType.BlockSubmit;
			}
		}

		for (UnderwriterIssue listIssue : fullUWIssues.getBlockIssuanceList()) {
			if (listIssue.getLongDescription().contains(uwIssue) || listIssue.getShortDescription().contains(uwIssue)) {
				return UnderwriterIssueType.BlockIssuance;
			}
		}

		for (UnderwriterIssue listIssue : fullUWIssues.getBlockQuoteList()) {
			if (listIssue.getLongDescription().contains(uwIssue) || listIssue.getShortDescription().contains(uwIssue)) {
				return UnderwriterIssueType.BlockQuote;
			}
		}

		for (UnderwriterIssue listIssue : fullUWIssues.getBlockQuoteReleaseList()) {
			if (listIssue.getLongDescription().contains(uwIssue) || listIssue.getShortDescription().contains(uwIssue)) {
				return UnderwriterIssueType.BlockQuoteRelease;
			}
		}

		for (UnderwriterIssue listIssue : fullUWIssues.getRejectList()) {
			if (listIssue.getLongDescription().contains(uwIssue) || listIssue.getShortDescription().contains(uwIssue)) {
				return UnderwriterIssueType.Reject;
			}
		}

		for (UnderwriterIssue listIssue : fullUWIssues.getAlreadyApprovedList()) {
			if (listIssue.getLongDescription().contains(uwIssue) || listIssue.getShortDescription().contains(uwIssue)) {
				return UnderwriterIssueType.AlreadyApproved;
			}
		}
		return UnderwriterIssueType.NONE;
	}


	private String getIssueType(WebElement trWithIssue) {
		String returnText = null;
		if (!trWithIssue.findElements(By.xpath(".//child::td[2]/div/a")).isEmpty()) {
			return trWithIssue.findElement(By.xpath(".//child::td[2]/div/a")).getText();
		} else if (!trWithIssue.findElements(By.xpath(".//child::td[2]/div")).isEmpty()) {
			return trWithIssue.findElement(By.xpath(".//child::td[2]/div")).getText();
		}
		return returnText;
	}


	//returns an object with all UW Issues sorted by type

	public FullUnderWriterIssues getUnderwriterIssues() {
		FullUnderWriterIssues fullUWIssues = new FullUnderWriterIssues();
		UnderwriterIssueType listType = UnderwriterIssueType.Informational;

		List<WebElement> tempWebElementList = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUWIssuesList();

		for (int i = 0; i <= tempWebElementList.size() - 1; i++) {

			tempWebElementList = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUWIssuesList();
			switch (getIssueType(tempWebElementList.get(i))) {
			case "Informational":
				listType = UnderwriterIssueType.Informational;
				break;
			case "Block Issuance":
			case "Blocking Issuance":
				listType = UnderwriterIssueType.BlockIssuance;
				break;
			case "Blocking Quote Release":
			case "Block Quote Release":
				listType = UnderwriterIssueType.BlockQuoteRelease;
				break;
			case "Block Bind":
			case "Blocking Bind":
			case "Block Submit":
			case "Blocking Submit":
				listType = UnderwriterIssueType.BlockSubmit;
				break;
			case "Block Quote":
			case "Blocking Quote":
				listType = UnderwriterIssueType.BlockQuote;
				break;
			case "Reject":
			case "Must be corrected":
				listType = UnderwriterIssueType.Reject;
				break;
			case "Already Approved":
				listType = UnderwriterIssueType.AlreadyApproved;
				break;
			default:
				tempWebElementList = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUWIssuesList();
				switch (listType) {
				case Informational:
					fullUWIssues.getInformationalList().add(getUWIssue(tempWebElementList.get(i), listType));
					break;
				case BlockIssuance:
					fullUWIssues.getBlockIssuanceList().add(getUWIssue(tempWebElementList.get(i), listType));
					break;
				case BlockQuoteRelease:
					fullUWIssues.getBlockQuoteReleaseList().add(getUWIssue(tempWebElementList.get(i), listType));
					break;
				case BlockSubmit:
					fullUWIssues.getBlockSubmitList().add(getUWIssue(tempWebElementList.get(i), listType));
					break;
				case BlockQuote:
					fullUWIssues.getBlockQuoteList().add(getUWIssue(tempWebElementList.get(i), listType));
					break;
				case Reject:
					fullUWIssues.getRejectList().add(getUWIssue(tempWebElementList.get(i), listType));
					break;
				case AlreadyApproved:
					fullUWIssues.getAlreadyApprovedList().add(getUWIssue(tempWebElementList.get(i), listType));
					break;
				default:
					break;
				}
				break;
			}//END SWITCH
		}//END FOR
		return fullUWIssues;
	}

	public FullUnderWriterIssues getUnderwriterIssuesBasic() {
		FullUnderWriterIssues fullUWIssues = new FullUnderWriterIssues();
		UnderwriterIssueType listType = UnderwriterIssueType.Informational;

		List<WebElement> tempWebElementList = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUWIssuesList();

		for (int i = 0; i <= tempWebElementList.size() - 1; i++) {

			tempWebElementList = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUWIssuesList();
			switch (getIssueType(tempWebElementList.get(i))) {
			case "Informational":
				listType = UnderwriterIssueType.Informational;
				break;
			case "Block Issuance":
			case "Blocking Issuance":
				listType = UnderwriterIssueType.BlockIssuance;
				break;
			case "Blocking Quote Release":
			case "Block Quote Release":
				listType = UnderwriterIssueType.BlockQuoteRelease;
				break;
			case "Block Bind":
			case "Blocking Bind":
			case "Block Submit":
			case "Blocking Submit":
				listType = UnderwriterIssueType.BlockSubmit;
				break;
			case "Block Quote":
			case "Blocking Quote":
				listType = UnderwriterIssueType.BlockQuote;
				break;
			case "Reject":
			case "Must be corrected":
				listType = UnderwriterIssueType.Reject;
				break;
			case "Already Approved":
				listType = UnderwriterIssueType.AlreadyApproved;
				break;
			default:
				tempWebElementList = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUWIssuesList();
				switch (listType) {
				case Informational:
					fullUWIssues.getInformationalList().add(getUWIssueBasic(tempWebElementList.get(i), listType));
					break;
				case BlockIssuance:
					fullUWIssues.getBlockIssuanceList().add(getUWIssueBasic(tempWebElementList.get(i), listType));
					break;
				case BlockQuoteRelease:
					fullUWIssues.getBlockQuoteReleaseList().add(getUWIssueBasic(tempWebElementList.get(i), listType));
					break;
				case BlockSubmit:
					fullUWIssues.getBlockSubmitList().add(getUWIssueBasic(tempWebElementList.get(i), listType));
					break;
				case BlockQuote:
					fullUWIssues.getBlockQuoteList().add(getUWIssueBasic(tempWebElementList.get(i), listType));
					break;
				case Reject:
					fullUWIssues.getRejectList().add(getUWIssueBasic(tempWebElementList.get(i), listType));
					break;
				case AlreadyApproved:
					fullUWIssues.getAlreadyApprovedList().add(getUWIssueBasic(tempWebElementList.get(i), listType));
					break;
				default:
					break;
				}
				break;
			}//END SWITCH
		}//END FOR
		return fullUWIssues;
	}


	public FullUnderWriterIssues getUnderwriterIssuesWithButtons() {
		FullUnderWriterIssues fullUWIssues = new FullUnderWriterIssues();
		UnderwriterIssueType listType = UnderwriterIssueType.Informational;
		List<WebElement> tempWebElementList = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUWIssuesList();
		String tempString = null;

		for (int i = 0; i <= tempWebElementList.size() - 1; i++) {
			tempWebElementList = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUWIssuesList();
			tempString = getUWIssueText(tempWebElementList.get(i));
			switch (tempString) {
			case "Informational":
				listType = UnderwriterIssueType.Informational;
				break;
			case "Block Issuance":
				listType = UnderwriterIssueType.BlockIssuance;
				break;
			case "Block Quote Release":
				listType = UnderwriterIssueType.BlockQuoteRelease;
				break;
			case "Block Bind":
			case "Blocking Bind":
			case "Block Submit":
			case "Blocking Submit":
				listType = UnderwriterIssueType.BlockSubmit;
				break;
			case "Block Quote":
				listType = UnderwriterIssueType.BlockQuote;
				break;
			case "Reject":
			case "Must be corrected":
				listType = UnderwriterIssueType.Reject;
				break;
			case "Already Approved":
				listType = UnderwriterIssueType.AlreadyApproved;
				break;
			default:
				tempWebElementList = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUWIssuesList();
				switch (listType) {
				case Informational:
					fullUWIssues.getInformationalList().add(getUWIssueWithButtons(tempWebElementList.get(i), listType));
					break;
				case BlockIssuance:
					fullUWIssues.getBlockIssuanceList().add(getUWIssueWithButtons(tempWebElementList.get(i), listType));
					break;
				case BlockQuoteRelease:
					fullUWIssues.getBlockQuoteReleaseList().add(getUWIssueWithButtons(tempWebElementList.get(i), listType));
					break;
				case BlockSubmit:
					fullUWIssues.getBlockSubmitList().add(getUWIssueWithButtons(tempWebElementList.get(i), listType));
					break;
				case BlockQuote:
					fullUWIssues.getBlockQuoteList().add(getUWIssueWithButtons(tempWebElementList.get(i), listType));
					break;
				case Reject:
					fullUWIssues.getRejectList().add(getUWIssueWithButtons(tempWebElementList.get(i), listType));
					break;
				case AlreadyApproved:
					fullUWIssues.getAlreadyApprovedList().add(getUWIssueWithButtons(tempWebElementList.get(i), listType));
					break;
				default:
					break;
				}
				break;
			}//END SWITCH
		}//END FOR

		return fullUWIssues;
	}//END getUnderwriterIssuesWithButtons

	public UnderwriterIssue getUWIssueWithButtons(WebElement trWithIssue, UnderwriterIssueType type) {
		UnderwriterIssue returnUWIssue = new UnderwriterIssue();

		for (WebElement button : trWithIssue.findElements(By.xpath(".//child::td[6]/div/a"))) {
			switch (button.getText()) {
			case "Approve":
				returnUWIssue.setApprove(true);
				returnUWIssue.setApprove_Available(true);
				break;
			case "Reject":
				returnUWIssue.setReject(true);
				returnUWIssue.setReject_Available(true);
				break;
			case "Reopen":
				returnUWIssue.setReopen(true);
				returnUWIssue.setReopen_Available(true);
				break;
			case "Special Approve":
				returnUWIssue.setSpecialApprove(true);
				returnUWIssue.setSpecialApprove_Available(true);
				break;
			}//END SWITCH
		}//END FOR

		for (WebElement button : trWithIssue.findElements(By.xpath(".//child::td[5]/div/span"))) {
			switch (button.getText()) {
			case "Approve":
				returnUWIssue.setApprove(true);
				break;
			case "Reject":
				returnUWIssue.setReject(true);
				break;
			case "Reopen":
				returnUWIssue.setReopen(true);
				break;
			case "Special Approve":
				returnUWIssue.setSpecialApprove(true);
				break;
			}//END SWITCH
		}//END FOR

		returnUWIssue.setType(type);

		if (!trWithIssue.findElements(By.xpath(".//child::td[2]/div/a")).isEmpty()) {
			trWithIssue.findElement(By.xpath(".//child::td[2]/div/a")).click();
			waitUntilElementIsVisible(By.xpath("//textarea[contains(@id, ':IssueLongDescription-inputEl')]"), 100);
			returnUWIssue.setLongDescription(find(By.xpath("//textarea[contains(@id, ':IssueLongDescription-inputEl')]")).getText());
			returnUWIssue.setShortDescription(find(By.xpath("//label[contains(@id, 'IssueLongDescription-labelEl')]")).getText());
			clickReturnRiskAnalysisPage();
		}//END IF

		return returnUWIssue;
	}//END getUWIssueWithButtons

	public List<String> getUWIssuesListLongDescription() {
		List<WebElement> tempWebElementList = getUWIssuesList();
		List<String> tempStringList = new ArrayList<String>();
		String tempString = null;
		for (int i = 0; i <= tempWebElementList.size() - 1; i++) {
			tempWebElementList = getUWIssuesList();
			tempString = getUWIssueText(tempWebElementList.get(i));
			if (tempString != null) {
				tempStringList.add(tempString);
			}
		}
		return tempStringList;
	}


	public String getUWIssueText(WebElement trWithIssue) {
		String returnText = null;
		if (!trWithIssue.findElements(By.xpath(".//child::td[2]/div/a")).isEmpty()) {
			clickWhenClickable(trWithIssue.findElement(By.xpath(".//child::td[2]/div/a")));
			if (!finds(By.xpath("//textarea[contains(@id, ':IssueLongDescription-inputEl')]")).isEmpty()) {
				returnText = find(By.xpath("//textarea[contains(@id, ':IssueLongDescription-inputEl')]")).getText();
			}
			clickReturnRiskAnalysisPage();
		} else if (!trWithIssue.findElements(By.xpath(".//child::td[2]/div")).isEmpty()) {
			return trWithIssue.findElement(By.xpath(".//child::td[2]/div")).getText();
		}
		return returnText;
	}

	public boolean approveUWIssue(UnderwriterIssue uwIssue) {
		//FIX FOR DYNAMIC CHANGING LIST.
		List<WebElement> uwIssuesList = finds(By.xpath("//td/div/a[contains(text(), 'Approve')]"));//getUWIssuesList();
		for (WebElement uwIssues : uwIssuesList) {
			String uwIssueText = uwIssues.findElement(By.xpath("//ancestor::td/div/a[contains(@id, 'ShortDescription')]")).getText();
			if (uwIssueText.equals(uwIssue.getShortDescription())) {
				if (uwIssue.isApprove() && uwIssue.isApprove_Available()) {
					clickWhenClickable(uwIssues.findElement(By.xpath("//td/div/a[contains(text(), 'Approve')]")));

					super.clickOK();
					selectOKOrCancelFromPopup(OkCancel.OK);
					break;
				} else if (uwIssue.isApprove() && !uwIssue.isApprove_Available()) {
					systemOut("APPROVE button is visible to User but not Clickable.");
				} else {
					systemOut("APPROVE button is not available");
				}
			}
		}
		return false;
	}


	//Blindly Approve all UW Issues
	public FullUnderWriterIssues approveAll() throws Exception {
		boolean alreadyHandled = false;

		FullUnderWriterIssues uwIssues = getUnderwriterIssuesBasic();

		//FIX SOETHING FOR SR-22
		if (!uwIssues.isInList_ShortDescription("SR-22 Charge").equals(UnderwriterIssueType.NONE) && alreadyHandled == false) {
			//Go to the drivers page
			repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
			sideMenu.clickSideMenuPADrivers();
			new GuidewireHelpers(driver).editPolicyTransaction();
			//Edit Each Driver (In the interest of not having to change the signature of this method, we will loop through all drivers already
			//set on the page and check if the SR-22 checkbox is already set. If so, we will charge the fee. If not, it will be left alone.)
			GenericWorkorderSquireAutoDrivers_ContactDetail driversPage = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
			for (int i = 1; i <= driversPage.getDriverTableRowsCount(); i++) {
				driversPage.clickEditButtonInDriverTable(i);
				//Add SR-22 Filing Charge
				if (driversPage.isSR22CheckboxChecked()) {
					driversPage.setSR22FilingFee(SR22FilingFee.Charged);
				}
				driversPage.clickOk();
			}
			GenericWorkorder workflow = new GenericWorkorder(driver);
			workflow.clickGenericWorkorderQuote();
			sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
			sideMenu.clickSideMenuRiskAnalysis();
			alreadyHandled = true;
		}
		List<UnderwriterIssue> approvableUWIssues = new ArrayList<UnderwriterIssue>();
		approvableUWIssues.addAll(uwIssues.getAllApprovableUWIssues());
		for (UnderwriterIssue issue : approvableUWIssues) {
			selectUnderWriterIssue(issue);
		}

		if (!approvableUWIssues.isEmpty()) {
			if(!finds(By.xpath("//img[contains(@class, '-checked')]")).isEmpty()) {
				clickWhenClickable(approveAllButton);
				waitForPostBack(30);
				clickOK();
			}
		}
		return uwIssues;
	}

	private void selectUnderWriterIssue(UnderwriterIssue issue) {
		List<WebElement> foo = finds(By.xpath("//a[text()=" + StringsUtils.xPathSpecialCharacterEscape(issue.getShortDescription()) + "]/ancestor::tr/child::td[6]/div/child::a[(text()='Approve')]/ancestor::tr/child::td[1]/div/img[not (contains(@class, '-checked'))]"));
		if (!foo.isEmpty()) {
			clickWhenClickable(foo.get(0));
		}
	}

	public void approveAll_IncludingSpecial() throws Exception {
		while (buttons_SpecialApproveActive().size() > 0) {
			buttons_SpecialApproveActive().get(0).click();
			clickOK();
			selectOKOrCancelFromPopup(OkCancel.OK);
		}
		approveAll();
	}


	public void handleBlockSubmitForPolicyChange() throws Exception {
		repository.pc.sidemenu.SideMenuPC sideBar = new repository.pc.sidemenu.SideMenuPC(driver);
		sideBar.clickSideMenuRiskAnalysis();
		clickRequestApproval();
		repository.pc.activity.UWActivityPC activity = new repository.pc.activity.UWActivityPC(driver);
		activity.setText("Please Approve this Stuff!!");
		activity.setNewNoteSubject("Please Approve this Stuff!!");
		activity.clickSendRequest();

		Underwriters uw = null;
		InfoBar infoBar = new InfoBar(driver);
		String accountNumber = infoBar.clickInfoBarAccountNumber();

		repository.pc.account.AccountSummaryPC aSumm = new repository.pc.account.AccountSummaryPC(driver);
		ArrayList<String> activityOwners = new ArrayList<String>();
		activityOwners = aSumm.getActivityAssignedTo("Approval Requested");
		uw = UnderwritersHelper.getUnderwriterInfoByFullName(activityOwners.get(activityOwners.size() - 1));

		TopInfo topInfoStuff = new TopInfo(driver);
		topInfoStuff.clickTopInfoLogout();

		Login loginPage = new Login(driver);
		loginPage.login(uw.getUnderwriterUserName(), uw.getUnderwriterPassword());
		repository.pc.search.SearchAccountsPC search = new repository.pc.search.SearchAccountsPC(driver);
		search.searchAccountByAccountNumber(accountNumber);

		repository.pc.account.AccountSummaryPC acct = new repository.pc.account.AccountSummaryPC(driver);
		acct.clickActivitySubject("Approval Requested");
		ActivityPopup actPop = new ActivityPopup(driver);
		actPop.clickCompleteButton();

		sideBar = new repository.pc.sidemenu.SideMenuPC(driver);
		sideBar.clickSideMenuRiskAnalysis();

		approveAll();
		if (isAlertPresent()) {
			selectOKOrCancelFromPopup(OkCancel.OK);
		}
		if (new GenericWorkorderRiskAnalysis_UWIssues(driver).checkIfSpecialApproveButtonsExist(false)) {
			clickRequestApproval();
			activity = new repository.pc.activity.UWActivityPC(driver);
			activity.setText("Please Special Approve this Stuff!!");
			activity.setNewNoteSubject("Please Special Approve this Stuff!!");
			activity.clickSendRequest();

			infoBar = new InfoBar(driver);
			infoBar.clickInfoBarAccountNumber();

			aSumm = new repository.pc.account.AccountSummaryPC(driver);
			activityOwners = aSumm.getActivityAssignedTo("Approval Requested");
			uw = UnderwritersHelper.getUnderwriterInfoByFullName(activityOwners.get(activityOwners.size() - 1));

			topInfoStuff = new TopInfo(driver);
			topInfoStuff.clickTopInfoLogout();

			loginPage = new Login(driver);
			loginPage.login(uw.getUnderwriterUserName(), uw.getUnderwriterPassword());
			search = new repository.pc.search.SearchAccountsPC(driver);
			search.searchAccountByAccountNumber(accountNumber);

			acct = new repository.pc.account.AccountSummaryPC(driver);
			acct.clickActivitySubject("Approval Requested");
			actPop = new ActivityPopup(driver);
			actPop.clickCompleteButton();

			sideBar = new repository.pc.sidemenu.SideMenuPC(driver);
			sideBar.clickSideMenuRiskAnalysis();


			approveAll_IncludingSpecial();
		}

		clickReleaseLock();


		infoBar = new InfoBar(driver);
		infoBar.clickInfoBarAccountNumber();

		acct = new repository.pc.account.AccountSummaryPC(driver);
		acct.clickActivitySubject("Underwriter has reviewed this job");

	}

	public void handleBlockSubmitForPolicyChangeWithSameLogin(WebDriver driver) throws Exception {
		repository.pc.sidemenu.SideMenuPC sideBar = new repository.pc.sidemenu.SideMenuPC(driver);
		sideBar.clickSideMenuRiskAnalysis();


		approveAll();
		if (isAlertPresent()) {
			selectOKOrCancelFromPopup(OkCancel.OK);
		}
		if (new GenericWorkorderRiskAnalysis_UWIssues(driver).checkIfSpecialApproveButtonsExist(false)) {
			approveAll_IncludingSpecial();
		}

	}

	public void handleBlockSubmit(GeneratePolicy policy) throws Exception {
		handleBlockSubmitAndReleaseLockToRequester(policy);
		Login loginPage;

		loginPage = new Login(driver);
		loginPage.login(policy.agentInfo.getAgentUserName(), policy.agentInfo.getAgentPassword());

		repository.pc.desktop.DesktopMyActivitiesPC myActivities = new repository.pc.desktop.DesktopMyActivitiesPC(driver);
		myActivities.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Underwriter has reviewed this job", policy.accountNumber, (policy.pniContact.isPerson()) ? policy.pniContact.getFirstName() : policy.pniContact.getCompanyName());
	}

	public void handleBlockSubmitAndReleaseLockToRequester(GeneratePolicy policy) throws Exception {
		requestAndApproveUwIssues(policy);
		repository.pc.activity.UWActivityPC activity;
		InfoBar infoBar;
		repository.pc.account.AccountSummaryPC aSumm;
		ArrayList<String> activityOwners;
		Underwriters uw;
		TopInfo topInfoStuff;
		Login loginPage;
		repository.pc.search.SearchAccountsPC search;
		repository.pc.account.AccountSummaryPC acct;
		ActivityPopup actPop;
		repository.pc.sidemenu.SideMenuPC sideBar;
		if (new GenericWorkorderRiskAnalysis_UWIssues(driver).checkIfSpecialApproveButtonsExist()) {
			
			
//			clickRequestApproval();
//			activity = new UWActivityPC(driver);
//			activity.setText("Please Special Approve this Stuff!!");
//			activity.setNewNoteSubject("Please Special Approve this Stuff!!");
//			activity.clickSendRequest();
//
//			infoBar = new InfoBar(driver);
//			infoBar.clickInfoBarAccountNumber();
//
//			aSumm = new AccountSummaryPC(driver);
//			activityOwners = aSumm.getActivityAssignedTo("Approval Requested");
//			uw = UnderwritersHelper.getUnderwriterInfoByFullName(activityOwners.get(activityOwners.size() - 1));
			
			topInfoStuff = new TopInfo(driver);
			topInfoStuff.clickTopInfoLogout();

			loginPage = new Login(driver);
//			loginPage.login(uw.getUnderwriterUserName(), uw.getUnderwriterPassword());
			//DELETE ME
			loginPage.login("hhill", "gw");
			
			
			search = new repository.pc.search.SearchAccountsPC(driver);
			search.searchAccountByAccountNumber(policy.accountNumber);

			acct = new repository.pc.account.AccountSummaryPC(driver);
//			acct.clickActivitySubject("Approval Requested");
//			actPop = new ActivityPopup(driver);
//			actPop.clickCompleteButton();
			//DELETE ME
			acct.clickAccountSummaryPendingTransactionByProduct(policy.productType);
			

			sideBar = new repository.pc.sidemenu.SideMenuPC(driver);
			sideBar.clickSideMenuRiskAnalysis();


			approveAll_IncludingSpecial();
		}

		clickReleaseLock();

		if (policy.productType.equals(ProductLineType.Squire)) {
			boolean driversContainSR22Charges = false;
			for (Contact driver : policy.squire.squirePA.getDriversList()) {
				if (driver.hasSR22Charges()) {
					driversContainSR22Charges = true;
					break;
				}
			}
			if (policy.squire.squirePA.getDriversList().size() > 0 && driversContainSR22Charges) {
				GenericWorkorderComplete quoteCompletePage = new GenericWorkorderComplete(driver);
				quoteCompletePage.clickViewYourSubmission();

				//Go to the drivers page
				repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
				sideMenu.clickSideMenuPADrivers();

				GenericWorkorderSquireAutoDrivers_ContactDetail driversPage = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);

				for (Contact policyDriver : policy.squire.squirePA.getDriversList()) {
					String driverName = "";
					if (policyDriver.getPersonOrCompany().equals(ContactSubType.Company)) {
						driverName = policyDriver.getCompanyName();
					} else {
						driverName = (policyDriver.getFirstName() + " " + policyDriver.getLastName());
					}
					if (policyDriver.hasSR22Charges()) {
						WebElement tableRow = new TableUtils(driver).getRowInTableByColumnNameAndValue(driversPage.getDriverDetailsTable(), "Name", driverName);
						new TableUtils(driver).clickRowInTableByRowNumber(driversPage.getDriverDetailsTable(), new TableUtils(driver).getRowNumberFromWebElementRow(tableRow));

						if (driversPage.getSR22FilingFee().equals("Charged")) {
							policyDriver.setSr22Charge(25.00);
						} else {
							Assert.fail("SR-22 fees should have been charged for this driver, but were not. test integrity cannot be trusted. Please investigate.");
						}
					}
				}

			}
		}

		new GuidewireHelpers(driver).logout();
	}

	public void requestAndApproveUwIssues(GeneratePolicy policy) throws Exception {
		repository.pc.sidemenu.SideMenuPC sideBar = new repository.pc.sidemenu.SideMenuPC(driver);
		sideBar.clickSideMenuRiskAnalysis();
		clickRequestApproval();
		repository.pc.activity.UWActivityPC activity = new repository.pc.activity.UWActivityPC(driver);
		activity.setText("Please Approve this Stuff!!");
		activity.setNewNoteSubject("Please Approve this Stuff!!");
		activity.clickSendRequest();

		LogOutAndLoginWithAssignedUWAndComplete(policy);

		InfoBar infoBar;
		repository.pc.account.AccountSummaryPC aSumm;
		ArrayList<String> activityOwners;
		Underwriters uw;
		TopInfo topInfoStuff;
		Login loginPage;
		repository.pc.search.SearchAccountsPC search;
		repository.pc.account.AccountSummaryPC acct;
		ActivityPopup actPop;

		sideBar = new repository.pc.sidemenu.SideMenuPC(driver);
		sideBar.clickSideMenuRiskAnalysis();

		approveAll();
		if (isAlertPresent()) {
			selectOKOrCancelFromPopup(OkCancel.OK);
		}
	}

	public void LogOutAndLoginWithAssignedUWAndComplete(GeneratePolicy policy) throws Exception {
		Underwriters uw = null;
		InfoBar infoBar = new InfoBar(driver);
		infoBar.clickInfoBarAccountNumber();

		repository.pc.account.AccountSummaryPC aSumm = new repository.pc.account.AccountSummaryPC(driver);
		ArrayList<String> activityOwners = new ArrayList<String>();
		activityOwners = aSumm.getActivityAssignedTo("Approval Requested");
		uw = UnderwritersHelper.getUnderwriterInfoByFullName(activityOwners.get(activityOwners.size() - 1));

		TopInfo topInfoStuff = new TopInfo(driver);
		topInfoStuff.clickTopInfoLogout();

		Login loginPage = new Login(driver);
		loginPage.login(uw.getUnderwriterUserName(), uw.getUnderwriterPassword());
		repository.pc.search.SearchAccountsPC search = new SearchAccountsPC(driver);
		search.searchAccountByAccountNumber(policy.accountNumber);

		repository.pc.account.AccountSummaryPC acct = new AccountSummaryPC(driver);
		acct.clickActivitySubject("Approval Requested");
		ActivityPopup actPop = new ActivityPopup(driver);
		actPop.clickCompleteButton();
	}

	public void handleBlockQuoteRelease(GeneratePolicy policy) throws Exception {
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (policy.handleBlockSubmitRelease) {
			boolean blockQuoteRelease = quotePage.hasBlockQuoteRelease();
			boolean blockQuote = quotePage.hasBlockQuote();
			if (blockQuoteRelease) {

				handleBlockSubmit(policy);
			}
			if (blockQuote) {
				ActivityPopup actPop = new ActivityPopup(driver);
				actPop.clickCompleteButton();
				clickGenericWorkorderQuote();
			}
			repository.pc.sidemenu.SideMenuPC sideMenuStuff = new repository.pc.sidemenu.SideMenuPC(driver);
			sideMenuStuff.clickSideMenuQuote();
		}
	}


	public void handleBlockQuote(GeneratePolicy policy) throws Exception {
		handleBlockSubmit(policy);
		if (isQuotable()) {
			clickGenericWorkorderQuote();
			/*waitUntilElementIsClickable(find(By.xpath("//span[contains(@id, 'Quoute-btnEl')]")));
            find(By.xpath("//span[contains(@id, 'Quoute-btnEl')]")).click();*/
		} else {
			repository.pc.sidemenu.SideMenuPC sideMenuStuff = new repository.pc.sidemenu.SideMenuPC(driver);
			sideMenuStuff.clickSideMenuQuote();
		}
	}


	public String getUWBlockSubmitDescriptionByUWIssueShortDescription(WebElement trWebElement) {
		clickWhenClickable(trWebElement.findElement(By.xpath(".//child::td[2]/div/a")));

		return find(By.xpath("//textarea[contains(@id, ':IssueLongDescription-inputEl')]")).getText();
	}


	public void handleBlockSubmitCSR(GeneratePolicy policy, CSRs myCSR) throws Exception {
		requestApproval(policy.underwriterInfo);

		GenericWorkorderComplete info = new GenericWorkorderComplete(driver);
		Underwriters uw = info.getUnderwriterInfo();

		TopInfo topInfoStuff = new TopInfo(driver);
		topInfoStuff.clickTopInfoLogout();

		Login loginPage = new Login(driver);
		loginPage.login(uw.getUnderwriterUserName(), uw.getUnderwriterPassword());

		repository.pc.desktop.DesktopMyActivitiesPC myActivities = new repository.pc.desktop.DesktopMyActivitiesPC(driver);
		myActivities.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Approval Needed", policy.accountNumber, policy.pniContact.getCompanyName() == null ? policy.pniContact.getFirstName() + " " + policy.pniContact.getLastName() : policy.pniContact.getCompanyName());
		repository.pc.sidemenu.SideMenuPC sideBar = new repository.pc.sidemenu.SideMenuPC(driver);
		sideBar = new SideMenuPC(driver);
		sideBar.clickSideMenuRiskAnalysis();

		new GenericWorkorderRiskAnalysis_UWIssues(driver).approveAll();

		clickReleaseLock();
		repository.pc.activity.UWActivityPC activity = new repository.pc.activity.UWActivityPC(driver);
		activity = new UWActivityPC(driver);
		activity.setText("Stuff Approved");
		activity.clickSendRequest();

		topInfoStuff = new TopInfo(driver);
		topInfoStuff.clickTopInfoLogout();

		loginPage = new Login(driver);
		loginPage.login(policy.agentInfo.getAgentUserName(), policy.agentInfo.getAgentPassword());

		myActivities = new DesktopMyActivitiesPC(driver);
		myActivities.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Underwriter has reviewed this job", policy.accountNumber, policy.pniContact.getCompanyName() == null ? policy.pniContact.getFirstName() + " " + policy.pniContact.getLastName() : policy.pniContact.getCompanyName());
	}

	public boolean checkIfReviewButtonsExist() {
		return !finds(By.xpath("//a[contains(@id, ':Review')]")).isEmpty();
	}

	public void reviewAllInformationalUWIssues(){
		while (buttons_Review().size() > 0) {
			buttons_Review().get(0).click();
			clickUpdate();
		}
	}
	
	public void reOpenAllInformationalUWIssues(){
		while (buttons_Reopen().size() > 0) {
			buttons_Reopen().get(0).click();
			clickProductLogo();
		}
	}

	public  int checkUWHistoryName(String name){
		return new TableUtils(driver).getRowNumberInTableByText(table_UWIssuesHistory, name);
	} 
	
	
	public void enterValidUntilForSpecificUWIssue(String uwIssue, String validUntil){
		new TableUtils(driver).setCheckboxInTable(table_UWIssues,new TableUtils(driver).getRowNumberInTableByText(table_UWIssues, uwIssue), true);
		clickWhenClickable(approveAllButton);
//		waitForPostBack(30);
		WebElement tableCell = table_UWIssuesNewApproval.findElement(By.xpath("//tbody/tr[contains(@data-recordindex,'1')]/td[3]/div"));
//		clickProductLogo();
		clickWhenClickable(tableCell);
		Guidewire8Select mySelect = new Guidewire8Select(driver,"(//table[contains(@id,'simplecombo') and contains(@id,'triggerWrap')])[last()]");
		mySelect.selectByVisibleTextPartialWithNoFail(validUntil);
		clickProductLogo();
		clickOK();
	}
}

