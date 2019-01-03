package repository.pc.workorders.rewrite;

import com.idfbins.enums.OkCancel;
import repository.driverConfiguration.Config;
import repository.driverConfiguration.DriverBuilder;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RewriteType;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import helpers.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.search.SearchAccountsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StartRewrite extends repository.pc.workorders.generic.GenericWorkorder {

	private WebDriver driver;
	private TableUtils tableUtils;

	public StartRewrite(WebDriver driver) {
		super(driver);
		this.driver = driver;
		this.tableUtils = new TableUtils(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//a[contains(@id, ':BindRewrite')]")
	private WebElement IssueRewrite;

	@FindBy(xpath = "//a[contains(@id, 'OtherAccountSearchResultsLV:0:Select')]")
	private WebElement link_accountNumberSelect;

	@FindBy(xpath = "//span[contains(@id, 'RewritePoliciesButton-btnInnerEl')]")
	private WebElement button_rewritePolicies;

	@FindBy(xpath = "//div[contains(@id,'AccountFile_PolicySelectionLV')]")
	private WebElement tableDiv_PoliciesTable;

	@FindBy(xpath = "//div[contains(@id,'RewriteWizard:0')]")
	private WebElement tableDiv_RewriteWizardStepsTable;

	@FindBy(xpath = "//a[contains(@id, 'AccountFile_Summary_ActivitiesLV:1:Subject') or contains(@id, 'AccountFile_Summary_ActivitiesLV:0:Subject') or contains(@id, ':AccountFile_Summary_WorkOrdersLV:0:WorkOrderNumber')]")
	private WebElement link_gototheRewriteAccount;

	@FindBy(xpath = "//span[contains(@id, 'RewriteWizard:0_header_hd-textEl')]")
	private WebElement text_StartRewritePageTitle;

	@FindBy(xpath = "//span[contains(@class, 'g-title') and contains(text(), 'Risk Analysis')]")
	private WebElement text_StartRewriteRiskAnalysisPageTitle;

	@FindBy(xpath = "//div[contains(@id, 'AccountFile_RewritePoliciesSelection:AccountFile_PolicySelectionLV')]")
	private WebElement table_RewritePoliciesSelection;

	@FindBy(xpath = "//span[contains(@id, 'AccountFile_RewritePoliciesSelection:RewritePoliciesButton-btnEl')]")
	private WebElement button_RewritePoliciesToThisAccount;


	public void clickIssuePolicy() {
		clickWhenClickable(IssueRewrite);
		selectOKOrCancelFromPopup(OkCancel.OK);
		waitForPostBack();
	}

    public void startRewrite(RewriteType rewriteType) throws GuidewireNavigationException {
		new repository.pc.policy.PolicyMenu(driver).clickMenuActions();
		switch (rewriteType) {
		case FullTerm:
			new repository.pc.policy.PolicyMenu(driver).clickRewriteFullTerm();
			break;
//		case NewAccount:
//			break;
		case NewTerm:
			new repository.pc.policy.PolicyMenu(driver).clickRewriteNewTerm();
			break;
		case RemainderOfTerm:
			new repository.pc.policy.PolicyMenu(driver).clickRewriteRemainderOfTerm();
			break;
		}
	}


	public void rewriteFullTerm(GeneratePolicy pol) throws Exception {
		repository.pc.policy.PolicyMenu policyMenu = new repository.pc.policy.PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickRewriteFullTerm();
		rewriteFullTermGuts(pol);
		clickIssuePolicy();
		if(finds(By.xpath("//label[contains(text(), 'UW Issues that block issuance')]")).size() > 0) {
			repository.pc.workorders.generic.GenericWorkorderBlockBind blocker = new GenericWorkorderBlockBind(driver);
			blocker.clickDetails();
			repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(driver);
			riskAnalysis.approveAll();
		}
	}
	public void initiateRewriteRemainderOfTerm() {
		repository.pc.policy.PolicyMenu policyMenu = new repository.pc.policy.PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickRewriteRemainderOfTerm();
		waitUntilElementIsVisible(text_StartRewritePageTitle);
	}


	public void clickThroughAllPagesUntilRiskAnalysis(GeneratePolicy policy , RewriteType rewriteType) throws Exception {
		List<String> pageLinkTextValues = new ArrayList<String>();
		List<WebElement> pageLinkRows = tableUtils.getAllTableRows(tableDiv_RewriteWizardStepsTable);
		pageLinkRows.remove(pageLinkRows.size() - 1);
		for (WebElement pageLinkRow : pageLinkRows) {
			pageLinkTextValues.add(pageLinkRow.findElement(By.xpath(".//td[1]/div")).getText());
		}
		repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
		boolean foundRiskAnalysisPage = false;
		outerLoop:
			for (String pageLinkText : pageLinkTextValues) {
				switch (pageLinkText) {
				case "Qualification":
					sideMenu.clickSideMenuQualification();
					if(rewriteType != RewriteType.RemainderOfTerm && (policy.productType.equals(ProductLineType.Squire) || policy.productType.equals(ProductLineType.PersonalUmbrella))) {
						new repository.pc.workorders.generic.GenericWorkorderQualification(driver).fillOutFullAppQualifications(policy);
					}
					break;
				case "Squire Eligibility":
					sideMenu.clickSideMenuSquireEligibility();
					break;
				case "Line Selection":
					sideMenu.clickSideMenuLineSelection();
					break;
				case "Policy Contract":
					break;
				case "Policy Info":
					sideMenu.clickSideMenuPolicyInfo();
					break;
				case "Policy Members":
					sideMenu.clickSideMenuHouseholdMembers();
					break;
				case "Insurance Score":
					sideMenu.clickSideMenuPLInsuranceScore();
					break;
				case "Coverages":
					if(policy.productType.equals(ProductLineType.PersonalUmbrella)) {
						sideMenu.clickSideMenuSquireUmbrellaCoverages();
					}
					break;
				case "Businessowners Line":
					sideMenu.clickSideMenuBusinessownersLine();
					break;
				case "Locations":
					sideMenu.clickSideMenuLocations();
					break;
				case "Buildings":
					sideMenu.clickSideMenuBuildings();
					break;
				case "Supplemental":
					sideMenu.clickSideMenuSupplemental();
					break;
				case "Membership":
					sideMenu.clickSideMenuMembership();

					int membershipDataRecordIndex = pageLinkTextValues.indexOf("Membership");
					String nextLinkTextMembership = pageLinkTextValues.get(membershipDataRecordIndex + 1);
					sideMenu.clickSideMenuMembership();
					
					List<String> membershipPageLinkTextValues = new ArrayList<String>();
					List<WebElement> membershipPageLinkRows = tableUtils.getAllTableRows(tableDiv_RewriteWizardStepsTable);
					membershipPageLinkRows.remove(membershipPageLinkRows.size() - 1);
					for (WebElement section3PageLinkRow : membershipPageLinkRows) {
						membershipPageLinkTextValues.add(section3PageLinkRow.findElement(By.xpath(".//td[1]/div")).getText());
					}
					int nextLinkNewIndexMembership = membershipPageLinkTextValues.indexOf(nextLinkTextMembership);
					membershipLoop:
					for (int i = (membershipDataRecordIndex + 1); i <= nextLinkNewIndexMembership; i++) {
						if (membershipPageLinkTextValues.get(i).equals(nextLinkTextMembership)) {
							systemOut("Done with Section 3 Links.");
							break membershipLoop;
						} else {
							switch (membershipPageLinkTextValues.get(i)) {
								case "Membership Type":
									sideMenu.clickSideMenuMembershipMembershipType();
									break;
								case "Members":
									sideMenu.clickSideMenuMembershipMembers();
									break;
								default:
									Assert.fail("A side menu item was found in Membership Section that did not have a corresponding case. The item was " + membershipPageLinkTextValues.get(i) + ". Please add this case if necessary.");
									break;
							}
						}
					}
					break;
				case "Modifiers":
					sideMenu.clickSideMenuModifiers();
					break;
				case "Payer Assignment":
					sideMenu.clickSideMenuPayerAssignment();
					break;
				case "Risk Analysis":
					sideMenu.clickSideMenuRiskAnalysis();
					foundRiskAnalysisPage = true;
					break outerLoop;
				case "Section III - Auto":
					int section3DataRecordIndex = pageLinkTextValues.indexOf("Section III - Auto");
					String nextLinkText = pageLinkTextValues.get(section3DataRecordIndex + 1);
					sideMenu.clickPolicyContractSectionIIIAutoLine();
					
					List<String> section3PageLinkTextValues = new ArrayList<String>();
					List<WebElement> section3PageLinkRows = tableUtils.getAllTableRows(tableDiv_RewriteWizardStepsTable);
					section3PageLinkRows.remove(section3PageLinkRows.size() - 1);
					for (WebElement section3PageLinkRow : section3PageLinkRows) {
						section3PageLinkTextValues.add(section3PageLinkRow.findElement(By.xpath(".//td[1]/div")).getText());
					}
					int nextLinkNewIndex = section3PageLinkTextValues.indexOf(nextLinkText);
					section3Loop:
						for (int i = (section3DataRecordIndex + 1); i <= nextLinkNewIndex; i++) {
							if (section3PageLinkTextValues.get(i).equals(nextLinkText)) {
								systemOut("Done with Section 3 Links.");
								break section3Loop;
							} else {
								switch (section3PageLinkTextValues.get(i)) {
								case "Drivers":
									sideMenu.clickSideMenuPADrivers();
									break;
								case "Coverages":
									sideMenu.clickSideMenuPACoverages();
									break;
								case "Vehicles":
									sideMenu.clickSideMenuPAVehicles();
									break;
								case "CLUE Auto":
									sideMenu.clickSideMenuClueAuto();
									break;
								case "Line Review":
									sideMenu.clickSideMenuSquireLineReview();
									break;
								default:
									Assert.fail("A side menu item was found in the Section III - Auto Section that did not have a corresponding case. The item was " + section3PageLinkTextValues.get(i) + ". Please add this case if necessary.");
									break;
								}
							}
						}
					break;
				case "Sections I & II - Property & Liability":
					int section3DataRecordIndex1 = pageLinkTextValues.indexOf("Sections I & II - Property & Liability");
					String nextLinkText1 = pageLinkTextValues.get(section3DataRecordIndex1 + 1);
					sideMenu.clickPolicyContractSecttionIAndIIPropertyAndLiability();
					
					List<String> section3PageLinkTextValues1 = new ArrayList<String>();
					List<WebElement> section3PageLinkRows1 = tableUtils.getAllTableRows(tableDiv_RewriteWizardStepsTable);
					section3PageLinkRows1.remove(section3PageLinkRows1.size() - 1);
					for (WebElement section3PageLinkRow : section3PageLinkRows1) {
						section3PageLinkTextValues1.add(section3PageLinkRow.findElement(By.xpath(".//td[1]/div")).getText());
					}
					int nextLinkNewIndex1 = section3PageLinkTextValues1.indexOf(nextLinkText1);
					section3Loop:
						for (int i = (section3DataRecordIndex1 + 1); i <= nextLinkNewIndex1; i++) {
							if (section3PageLinkTextValues1.get(i).equals(nextLinkText1)) {
								systemOut("Done with Section 3 Links.");
								break section3Loop;
							} else {
								switch (section3PageLinkTextValues1.get(i)) {
								case "Locations":
									sideMenu.clickSideMenuPropertyLocations();
									break;
								case "Property Detail":
									sideMenu.clickSideMenuSquirePropertyDetail();
									break;
								case "Coverage":
								case "Coverages":
									sideMenu.clickSideMenuSquirePropertyCoverages();
									new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages(driver).clickSectionIICoveragesTab();
									new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages(driver).clickCoveragesExclusionsAndConditions();
									break;
								case "CLUE Property":
									sideMenu.clickSideMenuSquirePropertyCLUE();
									break;
								case "Line Review":
									sideMenu.clickSideMenuSquirePropertyLineReview();
									break;
								case "Sections I & II - Property & Liability":
									break;
								default:
									Assert.fail("A side menu item was found in the Sections I & II - Property & Liability that did not have a corresponding case. The item was " + section3PageLinkTextValues1.get(i) + ". Please add this case if necessary.");
									break;
								}
							}
						}
					break;
				default:
					Assert.fail("A side menu item was found that did not have a corresponding case. The item was " + pageLinkText + ". Please add this case if necessary.");
					break;
				}
			}
		if (!foundRiskAnalysisPage) {
			Assert.fail("The Risk Analysis page was not in the list of side menu items to click. Please investigate this!");
		}
	}


	public void riskAnalysisAndQuote() {
		repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
		risk.clickPriorPolicyCardTab();
		
		risk.clickClaimsCardTab();
		
		risk.clickPriorLossesCardTab();
		
		risk.Quote();
		clickPolicyChangeNext();
		
		clickPolicyChangeNext();
		
	}


	public void rewriteRemainderOfTerm(GeneratePolicy policy) throws Exception {
		initiateRewriteRemainderOfTerm();
		clickThroughAllPagesUntilRiskAnalysis(policy , RewriteType.RemainderOfTerm);
		riskAnalysisAndQuote();
		clickIssuePolicy();
	}

	// work in progress. Needs to be finished.

	public void rewriteNewTerm(GeneratePolicy policy) throws Exception {
		repository.pc.policy.PolicyMenu policyMenu = new repository.pc.policy.PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickRewriteNewTerm();
		rewriteNewTermGuts(policy);
	}


	public void rewriteNewTermGuts(GeneratePolicy policy) throws Exception {
		if(!policy.productType.equals(ProductLineType.PersonalUmbrella)) {
			clickNext();
		}
		clickThroughAllPagesUntilRiskAnalysis(policy , RewriteType.NewTerm);
		repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
		risk.clickPriorPolicyCardTab();
		
		risk.clickClaimsCardTab();
		
		risk.clickPriorLossesCardTab();
		
		risk.Quote();
		clickPolicyChangeNext();
		
		clickPolicyChangeNext();
		
		clickIssuePolicy();
	}

	public void rewriteFullTermGuts(GeneratePolicy policy) throws Exception {
		//	public void rewriteFullTermGuts(ArrayList<LineSelection> lineSelection, ProductLineType productType) {
        int maxNext = 11;
		int currentNext = 1;
		while (checkGenericWorkorderNextExists() && currentNext <= maxNext) {
			clickPolicyChangeNext();
			currentNext++;
		}
		repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
		switch (policy.productType) {
		case PersonalUmbrella:
			sideMenu.clickSideMenuSquireUmbrellaCoverages();
			
			repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages umbCovs = new GenericWorkorderSquireUmbrellaCoverages(driver);
			umbCovs.clickExclusionsConditions();
			break;
		case Squire:
			if (policy.lineSelection.contains(LineSelection.PropertyAndLiabilityLinePL)) {
				sideMenu.clickSideMenuSquirePropertyDetail();
				sideMenu.clickSideMenuSquirePropertyCoverages();
				repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
				coverages.clickSectionIICoveragesTab();
				coverages.clickCoveragesExclusionsAndConditions();
			}
			if (policy.lineSelection.contains(LineSelection.PersonalAutoLinePL)) {
				sideMenu.clickSideMenuLineSelection();
				sideMenu.clickSideMenuQualification();
				sideMenu.clickSideMenuPolicyInfo();
				sideMenu.clickSideMenuHouseholdMembers();
				//PLEASE INSERT CORRECT LOGIC HERE PLEASE.
				if (!policy.polOrgType.equals(OrganizationType.Sibling)) {
					sideMenu.clickSideMenuPLInsuranceScore();
				}
				sideMenu.clickSideMenuPADrivers();
				sideMenu.clickSideMenuPACoverages();
				sideMenu.clickSideMenuPAVehicles();
				sideMenu.clickSideMenuClueAuto();
				sideMenu.clickSideMenuSquireLineReview();
				sideMenu.clickSideMenuPAModifiers();
				sideMenu.clickSideMenuRiskAnalysis();
			}
			if (policy.lineSelection.contains(LineSelection.InlandMarineLinePL)) {
				sideMenu.clickSideMenuIMCoveragePartSelection();
			}

			break;
		case Businessowners:
			break;
		case CPP:
			break;
			//            case StandardFL:
				//                break;
		case StandardIM:
			sideMenu.clickSideMenuStandAloneIMFarmEquipment();
			break;
		case Membership:
			
			break;
		case StandardFire:
			break;
		case StandardLiability:
			break;
		}

		sideMenu.clickSideMenuRiskAnalysis();
		repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
		if (!policy.productType.equals(ProductLineType.PersonalUmbrella) && !policy.productType.equals(ProductLineType.Membership)) {
			risk.clickPriorPolicyCardTab();
			risk.clickPriorLossesCardTab();
		}

		if(policy.productType != ProductLineType.Membership){
			risk.clickClaimsCardTab();
		}
		
		risk.Quote();

		repository.pc.workorders.generic.GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.hasBlockBind()) {
			repository.pc.workorders.generic.GenericWorkorderRiskAnalysis riskAnalysis = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
			try {
				sideMenu.clickSideMenuRiskAnalysis();
				riskAnalysis.approveAll_IncludingSpecial();				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		clickPolicyChangeNext();
		clickPolicyChangeNext();
	}


	public void startRewriteNewTerm() throws GuidewireNavigationException {
		repository.pc.policy.PolicyMenu policyMenu = new repository.pc.policy.PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickRewriteNewTerm();
		clickPolicyChangeNext();
		new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':JobLabel-btnInnerEl')]/span[contains(text(), 'Rewrite New Term')]", 5000, "UNABLE TO GET TO REWRITE NEW TERM JOB AFTER CLICKING NEXT ON SET EFFECTIVE DATE PAGE.");
	}

	public void startRewriteNewTerm(Date effectiveDate) throws GuidewireNavigationException {
		repository.pc.policy.PolicyMenu policyMenu = new repository.pc.policy.PolicyMenu(getDriver());
		policyMenu.clickMenuActions();
		policyMenu.clickRewriteNewTerm();
		setRewriteEffeciveDate(effectiveDate);
		clickPolicyChangeNext();
	}

	@FindBy(xpath = "//input[contains(@id, ':EffectiveDate-inputEl')]")
	private WebElement input_EffectiveDate;
	private void setRewriteEffeciveDate(Date effectiveDate) {
		input_EffectiveDate.clear();
		input_EffectiveDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", effectiveDate));
	}


	public void rewriteNewTerm(LineSelection lineSelection) throws Exception {
		startRewriteNewTerm();
		rewriteNewTermGuts(lineSelection);
		clickGenericWorkorderQuote();
		clickIssuePolicy();
	}


	public void rewriteNewTermGuts(LineSelection lineSelection) throws Exception {
		// Fill Out Qualification Questions.
		repository.pc.workorders.generic.GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		switch (lineSelection) {
		case Businessowners:
			qualificationPage.setQuickQuoteAll(false);
			break;
		case CommercialAutoLineCPP:
		case GeneralLiabilityLineCPP:
			qualificationPage.setCPPPreQuallification(false);
			if (lineSelection == LineSelection.GeneralLiabilityLineCPP) {
				qualificationPage.setGLPreQualification(false);
			}
			if (lineSelection == LineSelection.CommercialAutoLineCPP) {
				qualificationPage.setCAQualificationRequired(false);
				qualificationPage.setCAQualification(false);
			}
			break;
		case PropertyAndLiabilityLinePL:
		case PersonalAutoLinePL:
		case InlandMarineLinePL:
			qualificationPage.setSquireGeneralFullTo(false);
			if (lineSelection == LineSelection.PropertyAndLiabilityLinePL) {
				qualificationPage.setSquireHOFullTo(false, "I was in a biker gang staying in motels, and not a homeowner");
				qualificationPage.setSquireGLFullTo(false);
				//				if (squireEligibility.equals(SquireEligibility.Country)) {
				//					qualificationPage.clickQualificationGLCattle(false);
				//				}
			}
			if (lineSelection == LineSelection.PersonalAutoLinePL) {
				qualificationPage.setSquireAutoFullTo(false);
			}
			if (lineSelection == LineSelection.InlandMarineLinePL) {
				qualificationPage.clickQualificationIMLosses(false);
			}
			break;
		case StandardFirePL:
		case StandardLiabilityPL:
			qualificationPage.setSquireGeneralFullTo(false);
			if (lineSelection == LineSelection.StandardFirePL) {
				qualificationPage.setStandardFireFullTo(false);
			} else if (lineSelection == LineSelection.StandardLiabilityPL) {
				qualificationPage.setStandardLiabilityFullTo(false);
			}
			break;
		default:
			break;
		}
	}


	public String rewriteNewTermCopySubmission(GeneratePolicy policy) throws Exception {
		repository.pc.policy.PolicyMenu policyMenu = new repository.pc.policy.PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickCopySubmission();
		rewriteNewTermGuts(policy);
		repository.pc.workorders.generic.GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);
		String collectionAmount = paymentPage.getCollectionAmount();
		return collectionAmount;

	}


	public void startQuoteRewrite(String sourceAccountNumber, String targetAccountNumber) {

		repository.pc.actions.ActionsPC actions = new repository.pc.actions.ActionsPC(driver);

		actions.click_Actions();
		actions.click_RewritePoliciesToThisAccount();

		repository.pc.search.SearchAccountsPC search = new repository.pc.search.SearchAccountsPC(driver);
		search.setAccountAccountNumber(sourceAccountNumber);
		search.clickSearch();
		
		clickSelect();
		selectPolicy();
		click_button_rewritePolicies();
	}


	public void clickSelect() {
		clickWhenClickable(link_accountNumberSelect);
		
	}

	private void selectPolicy() {
		tableUtils.setCheckboxInTable(tableDiv_PoliciesTable, 1, true);
	}


	public void click_link_gototheRewriteAccount() {
		clickWhenClickable(link_gototheRewriteAccount);
	}


	public boolean checkRewritePoliciesCheckBoxExists() {
		return checkIfElementExists("//div[contains(@id,'AccountFile_PolicySelectionLV')]//div/img", 2000);
	}


	public void selectAllPolicies() {
		int rowsCount = tableUtils.getRowCount(tableDiv_PoliciesTable);
		for (int i = 1; i <= rowsCount; i++) {
			tableUtils.setCheckboxInTable(tableDiv_PoliciesTable, i, true);
			
		}
	}


	public boolean checkPolicyExistsByProductName(String productName) {
		return tableUtils.verifyRowExistsInTableByColumnsAndValues(tableDiv_PoliciesTable, "Product", productName);
	}

	public void click_button_rewritePolicies() {
		clickWhenClickable(button_rewritePolicies);
		
	}


	public void rewriteRemainderOfTerm(ArrayList<LineSelection> lineSelection, ProductLineType productType) throws Exception {
		repository.pc.policy.PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickRewriteRemainderOfTerm();
		

		int maxNext = 20;
		int currentNext = 1;
		while (checkGenericWorkorderNextExists() && currentNext <= maxNext) {
			clickPolicyChangeNext();
			currentNext++;
		}
		repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
		if (lineSelection.contains(LineSelection.PropertyAndLiabilityLinePL)) {
			sideMenu.clickSideMenuSquirePropertyDetail();
			sideMenu.clickSideMenuSquirePropertyCoverages();
			repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
			coverages.clickFarmPersonalProperty();
			coverages.clickSectionIICoveragesTab();
			coverages.clickCoveragesExclusionsAndConditions();
			int currentNext1 = 1;
			while (checkGenericWorkorderNextExists() && currentNext1 <= maxNext) {
				clickPolicyChangeNext();
				currentNext1++;
			}
		}

		sideMenu.clickSideMenuRiskAnalysis();
		repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		if (!productType.equals(ProductLineType.PersonalUmbrella)) {
			
			risk.clickPriorPolicyCardTab();
			
			risk.clickPriorLossesCardTab();
		}
		
		risk.clickClaimsCardTab();
		
		risk.Quote();
		
		clickIssuePolicy();
	}


	public void clickRewritePoliciesToThisAccount() {
		
		clickWhenClickable(button_RewritePoliciesToThisAccount);
		
	}


	public GeneratePolicy rewriteNewAccount(GeneratePolicy cancelPolicy, GenerateContact newContact) throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver newDriver = new DriverBuilder().buildGWWebDriver(cf);// new config
		new Login(newDriver).loginAndSearchAccountByAccountNumber(cancelPolicy.underwriterInfo.getUnderwriterUserName(), cancelPolicy.underwriterInfo.getUnderwriterPassword(), newContact.accountNumber);
		repository.pc.actions.ActionsPC actions = new ActionsPC(newDriver);

		actions.click_Actions();
		actions.click_RewritePoliciesToThisAccount();

		repository.pc.search.SearchAccountsPC search = new SearchAccountsPC(newDriver);
		search.setAccountAccountNumber(cancelPolicy.accountNumber);
		search.clickSearch();
		StartRewrite rewrite = new StartRewrite(newDriver);
		rewrite.clickSelect();
		cancelPolicy.pniContact.setLastName(newContact.lastName);
		cancelPolicy.pniContact.setFirstName(newContact.firstName);
		cancelPolicy.pniContact.setCompanyName(newContact.companyName);
		cancelPolicy.accountNumber = newContact.accountNumber;
		tableUtils.setCheckboxInTable(table_RewritePoliciesSelection, tableUtils.getRowNumberInTableByText(table_RewritePoliciesSelection, cancelPolicy.accountNumber), "Policy #", true);
		clickRewritePoliciesToThisAccount();
		repository.pc.account.AccountSummaryPC acctSummaryPage = new AccountSummaryPC(newDriver);
		acctSummaryPage.clickAccountSummaryPendingTransactionByStatus("Draft");
		repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(newDriver);
		sideMenu.clickSideMenuPolicyInfo();
		repository.pc.workorders.generic.GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(newDriver);
		policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);
		sideMenu = new repository.pc.sidemenu.SideMenuPC(newDriver);
		sideMenu.clickSideMenuHouseholdMembers();
		repository.pc.workorders.generic.GenericWorkorderPolicyMembers houseMemberPage = new GenericWorkorderPolicyMembers(newDriver);
		houseMemberPage.clickPolicyHolderMembersByName(cancelPolicy.pniContact.getFullName());
		houseMemberPage.setDateOfBirth(cancelPolicy.pniContact.getDob());
		houseMemberPage.clickOK();
		sideMenu = new repository.pc.sidemenu.SideMenuPC(newDriver);
		sideMenu.clickSideMenuPLInsuranceScore();
		repository.pc.workorders.generic.GenericWorkorderInsuranceScore insuranceScore = new GenericWorkorderInsuranceScore(newDriver);
		insuranceScore.fillOutCreditReport(cancelPolicy);
		repository.pc.workorders.generic.GenericWorkorder wo = new GenericWorkorder(newDriver);
		wo.clickGenericWorkorderQuote();
		sideMenu = new repository.pc.sidemenu.SideMenuPC(newDriver);
		sideMenu.clickSideMenuRiskAnalysis();
		repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(newDriver);
		riskAnalysis.approveAll();
		wo.clickIssuePolicyButton();

		newDriver.quit();
		return cancelPolicy;
	}


	public void visitAllPages(GeneratePolicy policy) throws Exception {
		repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);

		sideMenu.clickSideMenuQualification();
		sideMenu.clickSideMenuPolicyInfo();
		if(policy.productType.equals(ProductLineType.Squire)) {
			sideMenu.clickSideMenuHouseholdMembers();
			sideMenu.clickSideMenuPLInsuranceScore();
		}

		for(LineSelection line : policy.lineSelection) {
			switch(line) {
			case Businessowners:
				break;
			case CommercialAutoLineCPP:
				break;
			case CommercialPropertyLineCPP:
				break;
			case GeneralLiabilityLineCPP:
				break;
			case InlandMarineLineCPP:
				break;
			case InlandMarineLinePL:
				break;
			case Membership:
		        sideMenu.clickSideMenuMembershipMembershipType();
		        sideMenu.clickSideMenuMembershipMembers();
		        break;
		      case PersonalAutoLinePL:
		        sideMenu.clickSideMenuPACoverages();
		        sideMenu.clickSideMenuPADrivers();
		        sideMenu.clickSideMenuPAModifiers();
		        sideMenu.clickSideMenuPAVehicles();
		        break;
		      case PropertyAndLiabilityLinePL:
		        sideMenu.clickSideMenuPropertyLocations();
		        sideMenu.clickSideMenuSquirePropertyDetail();
		        sideMenu.clickSideMenuSquirePropertyCoverages();
		        new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages(driver).clickSectionIICoveragesTab();
		        new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver).clickCoveragesExclusionsAndConditions();sideMenu.clickSideMenuSquirePropertyCLUE();
				sideMenu.clickSideMenuSquirePropertyLineReview();
				sideMenu.clickSideMenuModifiers();
				break;
			case StandardFirePL:
				break;
			case StandardInlandMarine:
				break;
			case StandardLiabilityPL:
				break;
			default:
				break;
			}

			sideMenu.clickSideMenuRiskAnalysis();
		}
	}
}





















