package repository.gw.generate;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;
import gwclockhelpers.ApplicationOrCenter;
import org.fluttercode.datafactory.impl.DataFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.InsuranceScoreTestCases;
import persistence.globaldatarepo.entities.LexisNexis;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.LexisNexisHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.*;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.exception.GuidewireException;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.custom.*;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import repository.gw.topinfo.TopInfo;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchSubmissionsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.*;
import repository.pc.workorders.renewal.StartRenewal;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import repository.pc.workorders.submission.SubmissionProductSelection;
import services.broker.objects.lexisnexis.cbr.response.actual.InquirySubjectType.Vehicle;

import java.text.ParseException;
import java.util.*;

import static repository.gw.enums.CreateNew.Create_New_Always;
import static repository.gw.enums.GeneratePolicyType.*;
import static repository.gw.enums.GeneratePolicyType.*;


public class GeneratePolicy extends GenericWorkorder {

	//    private ArrayList<ProductLineType> partsToBuild = new ArrayList<ProductLineType>();

	//    public DataFactory generateDataFactory = new DataFactory();
	public ProductLineType productType = ProductLineType.Businessowners;

	public boolean hasUWIssue = false;
	public boolean getUWIssues = false;
	public FullUnderWriterIssues allUWIssuesAfterQQ;
	public FullUnderWriterIssues allUWIssuesAfterFA;
	public FullUnderWriterIssues allUWIssuesAfterIssuance;
	public String accountNumber = null;
	public String fullAccountNumber = null;

	public boolean handleBlockSubmit = true;
	public boolean handleBlockSubmitRelease = true;

	public boolean hasSR22ChargesForPrimaryNamedInsured = false;
	public boolean membershipDuesOnAllInsureds = false;
	public List<Contact> additionalMembersToAddToMembershipList = new ArrayList<Contact>();
	public boolean hasMembershipDuesForPrimaryNamedInsured = true;

	public PaymentPlanType paymentPlanType = PaymentPlanType.getRandom();
	public PaymentType downPaymentType = PaymentType.getRandom();
	public BankAccountInfo bankAccountInfo = null;

	public Renewal renewal = null;

	public Agents agentInfo = null;
	public boolean randomAgent = true;
	public boolean nonAgent = false;
	public Underwriters underwriterInfo = null;

	public OrganizationType polOrgType = OrganizationType.Partnership;
	public OrganizationTypePL polOrgTypePL = OrganizationTypePL.Partnership;
	public Contact pniContact = new Contact(true);
	public CountyIdaho ratingCounty = CountyIdaho.Bannock;
	public CountyIdaho polDuesCounty = CountyIdaho.Bannock;
	public ArrayList<PolicyInfoAdditionalNamedInsured> aniList = new ArrayList<PolicyInfoAdditionalNamedInsured>();

	public String yearBusinessStarted = "2000";
	public String descriptionOfOperations = "They operate their operation in the operable fashion of operators";

	public boolean overrideDefaults = false;

	public boolean basicSearch = false;

	//MEMBERSHIP
	public Membership membership = new Membership();

	//BUSINESSOWNERS
	public PolicyBusinessownersLine busOwnLine = new PolicyBusinessownersLine();

	//COMMERCIAL LINES
	public CommercialPackagePolicy commercialPackage = new CommercialPackagePolicy();
	public CPPCommercialProperty commercialPropertyCPP = null;
	public CPPGeneralLiability generalLiabilityCPP = null;
	public CPPCommercialAuto commercialAutoCPP = null;
	public CPPInlandMarine inlandMarineCPP = null;

	//PERSONAL LINES
	public Squire squire = new Squire();
	public SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
	public StandardFireAndLiability standardFire = new StandardFireAndLiability();
	public StandardFireAndLiability standardLiability = new StandardFireAndLiability();
	public StandardInlandMarine standardInlandMarine = new StandardInlandMarine();
	public List<StandardFireAndLiability> standardFireList = new ArrayList<StandardFireAndLiability>();
	public boolean agentAutoIssue = false; // added to check whether agent can auto issue a policy - Nagamani

	public ArrayList<LineSelection> lineSelection = new ArrayList<>();
	private boolean addLineOfBusiness = false;

	public ArrayList<LineSelection> getLineSelection() {
		return lineSelection;
	}

	public void setLineSelection(LineSelection...lineSelection) {
		ArrayList<LineSelection> newLineSelectionList = new ArrayList<LineSelection>();
		for(LineSelection line : lineSelection) {
			newLineSelectionList.add(line);
		}
		this.lineSelection = newLineSelectionList;
	}

	public PolicyForms policyForms = new PolicyForms();
	public boolean veriskData;
	public boolean lexisNexisData;
	public boolean prefillPersonal;
	public boolean prefillCommercial;
	public boolean insuranceScore;
	public boolean mvr;
	public boolean clueAuto;
	public boolean stdFireLiability;
	public boolean StdFireSquire;
	public boolean StdFireBOP;
	public boolean stdIMSquire;
	public boolean bopForSQ;
	public boolean stdIMStdFire;
	public boolean stdFireLiabilityIM;
	public boolean bopForCPP;
	public boolean multipleStdFire;
	public boolean commodity;
	public boolean clueProperty;
	public PrefillInfo prefillReport;

	public CLUEAutoInfo clueAutoReport;

	public CLUEPropertyInfo cluePropertyReport;

	private WebDriver webDriver;
	GuidewireHelpers guidewireHelpers;

	public String altUserName = "";
	public String altPassword = "gw";
	public boolean altUser = false;
	
	public boolean quickBuild = false;



	public WebDriver getWebDriver() {
		return webDriver;
	}
	
	public void setWebDriver(WebDriver driver) {
		this.webDriver = driver;
	}

	public GeneratePolicy(WebDriver webDriver) {
		super(webDriver);
		this.webDriver = webDriver;
		guidewireHelpers = new GuidewireHelpers(webDriver);
		try {
			this.agentInfo = AgentsHelper.getRandomAgent();
		} catch (Exception e) {
		}
	}


	public GeneratePolicy(GeneratePolicyType typeToGenerate, Builder builderDetails) throws Exception {
		super(builderDetails.webDriver);
		this.webDriver = builderDetails.webDriver;
		this.guidewireHelpers = new GuidewireHelpers(webDriver);
		guidewireHelpers.setTypeToGenerate(this, typeToGenerate);
		if(builderDetails.quickBuild) {
			setQuickQuoteBuilderStuff(builderDetails);
			switch(this.productType) {
			case Businessowners:
				new QuickBuildBOP().quickBuildBOP(this);
				break;
			case CPP:
				break;
			case Membership:
				break;
			case PersonalUmbrella:
				break;
			case Squire:
				break;
			case StandardFire:
				break;
			case StandardIM:
				break;
			case StandardLiability:
				break;
			}
		} else {
			switch (typeToGenerate) {
			case QuickQuote:
				quickQuote(builderDetails);
				break;
			case FullApp:
				fullApp(builderDetails);
				break;
			case PolicySubmitted:
				policySubmitted(builderDetails);
				break;
			case PolicyIssued:
				policyIssued(builderDetails);
				break;
			}
		}
	}

	public void generate(GeneratePolicyType typeToGenerate) throws Exception {
		guidewireHelpers.setTypeToGenerate(this, typeToGenerate);
		switch (guidewireHelpers.getCurrentLineType(this)) {
		case QuickQuote:
			quickQuote(null);
			if (typeToGenerate.equals(QuickQuote)) break;
		case FullApp:
			fullAppGuts();
			if (typeToGenerate.equals(FullApp)) break;
		case PolicySubmitted:
			policySubmittedGuts();
			if (typeToGenerate.equals(PolicySubmitted)) break;
		case PolicyIssued:
			policyIssuedGuts();
		}
	}


	public void convertTo(WebDriver driver, GeneratePolicyType typeToGenerate) throws Exception {
		this.webDriver = driver;
		new GuidewireHelpers(webDriver).setTypeToGenerate(this, typeToGenerate);
		switch (guidewireHelpers.getCurrentLineType(this)) {
		case QuickQuote:
			fullAppGuts();
			if (typeToGenerate.equals(FullApp)) break;
		case FullApp:
			policySubmittedGuts();
			if (typeToGenerate.equals(PolicySubmitted)) break;
		case PolicySubmitted:
			policyIssuedGuts();
			break;
		case PolicyIssued:
			throw new NullPointerException("YOU IDIOT!! WHY ARE YOU CALLING THIS METHOD IF ITS ISSUED!!!!");
		}
	}


	public void addLineOfBusiness(ProductLineType lineToAdd, GeneratePolicyType typeToGenerate) throws Exception {

		addLineOfBusiness = true;
		productType = lineToAdd;
		switch (lineToAdd) {
		case Businessowners:
			busOwnLine.setTypeToGenerate(typeToGenerate);
			break;
		case CPP:
			commercialPackage.setTypeToGenerate(typeToGenerate);
			break;
		case Membership:
			membership.setTypeToGenerate(typeToGenerate);
			break;
		case PersonalUmbrella:
			squireUmbrellaInfo.setTypeToGenerate(typeToGenerate);
			break;
		case Squire:
			squire.setTypeToGenerate(typeToGenerate);
			break;
		case StandardFire:
			standardFire.setTypeToGenerate(typeToGenerate);
			lineSelection.add(repository.gw.enums.LineSelection.StandardFirePL);
			break;
		case StandardIM:
			standardInlandMarine.setTypeToGenerate(typeToGenerate);
			lineSelection.add(repository.gw.enums.LineSelection.StandardInlandMarine);
			break;
		case StandardLiability:
			standardLiability.setTypeToGenerate(typeToGenerate);
			lineSelection.add(LineSelection.StandardLiabilityPL);
			break;
		}

		generate(typeToGenerate);
	}//end addlineofbusiness()
	
	/**
	 * @param lineSelection
	 * @throws Exception 
	 * @assumptions POLICY IS ISSUED
	 */
	public void addSquireLine(LineSelection...lineSelection) throws Exception {
		addSquireLine(DateUtils.getCenterDate(getDriver(), ApplicationOrCenter.PolicyCenter) , lineSelection);
	}
	
	public void updateDriver(WebDriver driver) {
		this.webDriver = driver;
	}
	
	public void addSquireLine(Date effectiveDate, LineSelection...lineSelection) throws Exception {

		new Login(webDriver).loginAndSearchPolicy_asUW(this);
		new StartPolicyChange(webDriver).startPolicyChange("Adding Squire Line " + lineSelection, effectiveDate);
		for(LineSelection line : lineSelection) {
			this.lineSelection.add(line);

			new GenericWorkorderLineSelection(webDriver).checkline(line);
			new SideMenuPC(webDriver).clickSideMenuQualification();
			new GenericWorkorderQualification(webDriver).setPLQuestionsToFalse();
			switch (line) {
				case InlandMarineLinePL:
					fillOutPLInlandMarine();
					break;
				case PersonalAutoLinePL:
					new SideMenuPC(webDriver).clickSideMenuPADrivers();
					if(this.squire.squirePA.getDriversList().isEmpty()) {
						this.squire.squirePA.getDriversList().add(this.pniContact);
					}
					for(Contact driver : this.squire.squirePA.getDriversList()) {
						new GenericWorkorderSquireAutoDrivers(webDriver).addNewDriver(driver);
					}
					new SideMenuPC(webDriver).clickSideMenuPACoverages();
					new GenericWorkorderSquirePACoverages(webDriver).fillOutSquireAutoCoverages(this);
					new SideMenuPC(webDriver).clickSideMenuPAVehicles();
					new GenericWorkorderVehicles(webDriver).fillOutVehicles_QQ(this);
					if (this.clueAuto) {
						GenericWorkorderSquireCLUEAuto clueAutoPL = new GenericWorkorderSquireCLUEAuto(webDriver);
						if (this.lexisNexisData) {
							this.clueAutoReport = clueAutoPL.clickRetrieveCLUEAndAlsoValidateDirectlyFromBrokerForComparison(pniContact.getFirstName(), pniContact.getMiddleName(), pniContact.getLastName());
						} else {
							clueAutoPL.clickRetrieveCLUE();
						}
					}
					break;
				case PropertyAndLiabilityLinePL:
					fillOutSectionsIAndII_PropertyAndLiabilityQQ();
					new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(webDriver).fillOutPropertyDetail_FA(this);
					new GenericWorkorderSquirePropertyAndLiabilityCoverages(webDriver).editPLPropertyLiabilityCoveragesAndExclusionsConditionsFA(this);
					if (this.clueProperty) {
						SideMenuPC sideMenu = new SideMenuPC(webDriver);
						sideMenu.clickSideMenuSquirePropertyCLUE();

						GenericWorkorderSquireCLUEProperty clueProp = new GenericWorkorderSquireCLUEProperty(webDriver);
						this.cluePropertyReport = clueProp.clickRetrieveCLUEAndAlsoValidateDirectlyFromBrokerForComparison(pniContact.getFirstName(), pniContact.getMiddleName(), pniContact.getLastName());
					}
					break;
			}
		}
		new StartPolicyChange(webDriver).quoteAndIssue();
	}


	// /////////////////////////////////////
	// Methods for the Various Quote Types//
	// /////////////////////////////////////
	// method should contain major wizard steps only
	// each method call with in this one will call page object classes that will fill out subwizard steps
	// if there are no sub wizardsteps the method should call page object class to fill out tabs on currect wizard step
	public void quickQuote(Builder builderStuff) throws Exception {
		if (builderStuff != null) {
			setQuickQuoteBuilderStuff(builderStuff);
		}

		new FieldIntegrityAndDefaults().checkClassFieldIntegrityAndDefaults(this);

		if(addLineOfBusiness) {
			new Login(webDriver).loginAndSearchAccountByAccountNumber(this.agentInfo.getAgentUserName(), this.agentInfo.getAgentPassword(), this.accountNumber);
			addLineOfBusiness();
		} else if(altUser) {
			new Login(webDriver).login(altUserName, altPassword);
			startNewSubmission();
		} else {
			new Login(webDriver).login(this.agentInfo.getAgentUserName(), this.agentInfo.getAgentPassword());
			if (new Login(webDriver).accountLocked() && randomAgent) {
				this.agentInfo = new Login(webDriver).loginAsRandomAgent();
			}
			startNewSubmission();
		}
		selectProductAndFillOutQualificationsPage();
		systemOut("ACCOUNT NUMBER: " + accountNumber);
		systemOut("AGENT USERNAME: " + agentInfo.getAgentUserName());
		if (!squire.isFarmAndRanch()) {//farm and ranch squire if being forced to full app.

			GenericWorkorderPolicyMembers houseHold = new GenericWorkorderPolicyMembers(webDriver);
			new GenericWorkorderPolicyInfo(webDriver).fillOutPolicyInfoPage(this);
			switch (this.productType) {
			case Businessowners:
				fillOutBusinessOwnersLinePage();
				fillOutLocationsAndBuildingsPages();
//				fillOutBOPLocations_QQ();
//				fillOutBOPBuildings_QQ();
				fillOutSuplementalAndModifiersPages(true);
				fillOutPayerAssignment();
				break;
			case Squire:
				houseHold.fillOutPLHouseholdMembersQQ(this);

				if (lineSelection.contains( LineSelection.PropertyAndLiabilityLinePL)) {
					fillOutSectionsIAndII_PropertyAndLiabilityQQ();
				}

				if (lineSelection.contains( LineSelection.PersonalAutoLinePL)) {
					fillOutPLPersonalAutoQQ();
				}

				if (lineSelection.contains( LineSelection.InlandMarineLinePL)) {
					fillOutPLInlandMarine();
				}

				if (lineSelection.contains( LineSelection.Membership)) {
					fillOutMembershipMembers();
				}
				break;
			case StandardIM:
				houseHold.fillOutPLHouseholdMembersQQ(this);
				new GenericWorkorderStandardInlandMarine(webDriver).fillOutStandardInlandMarine(this);
				break;
			case CPP:
				GenericWorkorderLocations locationsPage = new GenericWorkorderLocations(webDriver);
				locationsPage.fillOutCPPLocations(this);
				if (lineSelection.contains( LineSelection.CommercialPropertyLineCPP)) {
					fillOutCPPCommercialProperty();
				}
				if (lineSelection.contains( LineSelection.GeneralLiabilityLineCPP)) {
					fillOutCPPGeneralLiability();
				}
				if (lineSelection.contains( LineSelection.CommercialAutoLineCPP)) {
					fillOutCPPCommercialAuto();
				}
				if (lineSelection.contains( LineSelection.InlandMarineLineCPP)) {
					fillOutCPPInlandMarine();
				}
				break;
			case PersonalUmbrella:
				fillOutPLUmbrellaCoverages();
				break;
			case Membership:
				fillOutMembershipMembers();
				break;
			case StandardFire:
				houseHold.fillOutPLHouseholdMembersQQ(this);
				new GenericWorkorderStandardFireAndLiability(webDriver).fillOutStandardFire(this);
				break;
			case StandardLiability:
				houseHold.fillOutPLHouseholdMembersQQ(this);
				new GenericWorkorderStandardFireAndLiability(webDriver).fillOutStandardLiability(this);
				new SideMenuPC(webDriver).clickSideMenuRiskAnalysis();
				break;
			}

			if(getUWIssues) {
				GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(webDriver);
				risk.performRiskAnalydidAndQuote_UWIssuesOnly(this);
			} else if (guidewireHelpers.getTypeToGenerate(this).equals(QuickQuote)) {
				if (!guidewireHelpers.isDraft(this)) {
					GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(webDriver);
					risk.performRiskAnalysisAndQuoteQQ(this);
				}
			}//end draft
		}
		new GuidewireHelpers(webDriver).logout();
	}

	protected void fillOutMembershipMembers() throws Exception {
		SideMenuPC sideMenu = new SideMenuPC(webDriver);

		if (!this.productType.equals( ProductLineType.Membership)) {
			sideMenu.clickSideMenuMembershipMembershipType();
		} else {
			sideMenu.clickSideMenuMembershipMembers();
		}

		GenericWorkorderMembershipMembers membershipMembers = new GenericWorkorderMembershipMembers(webDriver);
		if (!this.productType.equals( ProductLineType.Membership)) {
			membershipMembers.selectMembershipType(this.membership.getMembershipType());
			clickGenericWorkorderSaveDraft();
			sideMenu.clickSideMenuMembershipMembers();
		}

		if (this.polDuesCounty == null) {
			//This block will handle setting dues for out of state contacts
			if (this.pniContact.getAddress().getState().equals(State.Idaho)) {
				this.polDuesCounty = CountyIdaho.valueOfName(this.pniContact.getAddress().getCounty());
			} else {
				this.polDuesCounty = CountyIdaho.valueOfName(this.agentInfo.agentCounty);
			}
		}
		membershipMembers.setMembershipMembers(this, true);

		clickGenericWorkorderSaveDraft();
	}

	protected void fillOutPLInlandMarine() throws Exception {
		GenericWorkorderSquireInlandMarine inlandMarinePage = new GenericWorkorderSquireInlandMarine(webDriver);
		inlandMarinePage.fillOutPLInlandMarine(this);
	}

	protected void fillOutPayerAssignment() throws Exception {
		GenericWorkorderPayerAssignment payerassignmentPage = new GenericWorkorderPayerAssignment(webDriver);
		payerassignmentPage.fillOutPayerAssignmentPage(this);
	}

	protected void fillOutSectionsIAndII_PropertyAndLiabilityQQ() throws Exception {

		//FILL OUT LOCATIONS
		GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocation = new GenericWorkorderSquirePropertyAndLiabilityLocation(webDriver);
		propertyLocation.fillOutPropertyLocations(this);

		//FILL OUT PROPERTY DETAILS
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(webDriver);
		propertyDetail.fillOutPropertyDetail_QQ(this);

		//FILL OUT COVERAGES
		GenericWorkorderSquirePropertyAndLiabilityCoverages propertyCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(webDriver);
		propertyCoverages.fillOutPropertyAndLiabilityCoveragesQQ(this);

		//LINE REVIEW
		GenericWorkorderLineReviewPL propertyLineReview = new GenericWorkorderLineReviewPL(webDriver);
		propertyLineReview.fillOutLineReview(this);

		//PAYER ASSIGNMENT
		GenericWorkorderPayerAssignment payerassignmentPage = new GenericWorkorderPayerAssignment(webDriver);
		payerassignmentPage.fillOutPayerAssignmentPage(this);
	}


	public void fillOutPLUmbrellaCoverages() throws Exception {
		SideMenuPC sideMenu = new SideMenuPC(webDriver);
		sideMenu.clickSideMenuSquireUmbrellaCoverages();

		GenericWorkorderSquireUmbrellaCoverages umbCovs = new GenericWorkorderSquireUmbrellaCoverages(webDriver);
		umbCovs.fillOutUnbrellaCoverages(this);
	}

	public void fullAppGuts() throws Exception {
		GenericWorkorderQuote quote = new GenericWorkorderQuote(webDriver);
		GenericWorkorderQualification qualifications = new GenericWorkorderQualification(webDriver);
		GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(webDriver);
		GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(webDriver);
		GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(webDriver);

		new GuidewireHelpers(webDriver).setPolicyType(this, FullApp);

		new repository.gw.login.Login(webDriver).loginAndSearchSubmission(this);
		if(productType.equals( ProductLineType.PersonalUmbrella)) {
			new SideMenuPC(webDriver).clickSideMenuPolicyInfo();
		}
		clickGenericWorkorderFullApp();


		try {
			GenericWorkorderSquireEligibility eligibility = new GenericWorkorderSquireEligibility(webDriver);
			eligibility.clickAutoOnly(false);
		} catch (Exception e) {
			//CUS IM LAZY TODAY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		}

		if (this.productType.equals( ProductLineType.Squire) && squire.isFarmAndRanch()) {
			SideMenuPC sideMenu = new SideMenuPC(webDriver);
			GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(webDriver);

			lineSelection.filloutLineSelection(this);
			sideMenu.clickSideMenuLineSelection();
			if (!this.lineSelection.contains( LineSelection.PersonalAutoLinePL)) {
				lineSelection.checkPersonalAutoLine(false);
				sideMenu.waitUntilAutoSidebarLinkIsNotVisible();
			}
			if (!this.lineSelection.contains( LineSelection.PropertyAndLiabilityLinePL)) {
				lineSelection.checkPersonalPropertyLine(false);
				sideMenu.waitUntilPropertySidebarLinkIsNotVisible();
			} else if (!this.lineSelection.contains( LineSelection.InlandMarineLinePL)) {
				lineSelection.checkSquireInlandMarine(false);
				sideMenu.waitUntilInlandMarineSidebarLinkIsNotVisible();
			}
		}

		qualifications.fillOutFullAppQualifications(this);

		switch (productType) {
		case Businessowners:
			new GenericWorkorderBusinessownersLineAdditionalCoverages(webDriver).fillOutOtherLiabilityCovergaes(this.busOwnLine);
			fillOutFullAppLocationsAndBuilings(true);
			fillOutSuplementalAndModifiersPages(false);
			break;
		case CPP:
			if (lineSelection.contains( LineSelection.CommercialPropertyLineCPP)) {
				fillOutCPPCommercialProperty();
			}
			if (lineSelection.contains( LineSelection.GeneralLiabilityLineCPP)) {
				fillOutCPPGeneralLiability();
			}
			if (lineSelection.contains( LineSelection.CommercialAutoLineCPP)) {
				fillOutCPPCommercialAuto();
			}
			if (lineSelection.contains( LineSelection.InlandMarineLineCPP)) {
				fillOutCPPInlandMarine();
			}
			break;
		case Squire:

			policyInfo.fillOutPolicyInfoPageFA(this);
			household.updatePLHouseholdMembersFA(this);
			policyForms.updateFullAppForms(this);

			if (!this.polOrgType.equals( OrganizationType.Sibling)) {
				if (this.squire.alwaysOrderCreditReport) {
					this.squire.insuranceScoreReport = creditReport.fillOutCreditReporting(this);
				} else {
					creditReport.selectCreditReportIndividual(pniContact.getLastName());
				}
			}

			if (lineSelection.contains( LineSelection.PropertyAndLiabilityLinePL)) {
				if (squire.isFarmAndRanch()) {
					new GenericWorkorderSquirePropertyAndLiabilityLocation(webDriver).fillOutPropertyLocations(this);
				}
				new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(webDriver).fillOutPropertyDetail_FA(this);
				new GenericWorkorderSquirePropertyAndLiabilityCoverages(webDriver).editPLPropertyLiabilityCoveragesAndExclusionsConditionsFA(this);
			}

			if (this.clueProperty) {
				SideMenuPC sideMenu = new SideMenuPC(webDriver);
				sideMenu.clickSideMenuSquirePropertyCLUE();

				GenericWorkorderSquireCLUEProperty clueProp = new GenericWorkorderSquireCLUEProperty(webDriver);
				this.cluePropertyReport = clueProp.clickRetrieveCLUEAndAlsoValidateDirectlyFromBrokerForComparison(pniContact.getFirstName(), pniContact.getMiddleName(), pniContact.getLastName());
			}

			if (lineSelection.contains( LineSelection.PersonalAutoLinePL)) {
				updatePLPersonalAutoFA();
			}

			if (this.clueAuto) {
				GenericWorkorderSquireCLUEAuto clueAuto = new GenericWorkorderSquireCLUEAuto(webDriver);
				if (this.lexisNexisData) {
					this.clueAutoReport = clueAuto.clickRetrieveCLUEAndAlsoValidateDirectlyFromBrokerForComparison(pniContact.getFirstName(), pniContact.getMiddleName(), pniContact.getLastName());
				} else {
					clueAuto.clickRetrieveCLUE();
				}
			}
			if (squire.isFarmAndRanch()) {
				if (lineSelection.contains( LineSelection.InlandMarineLinePL)) {
					fillOutPLInlandMarine();
				}
			}


			break;
		case StandardIM:
			GenericWorkorderPolicyInfo standardIMpi = new GenericWorkorderPolicyInfo(webDriver);
			standardIMpi.fillOutStandardIMPolicyInfoPageFA(this);
			break;
		case PersonalUmbrella://click coverages
			new SideMenuPC(getDriver()).clickSideMenuSquireUmbrellaCoverages();
			new GenericWorkorderSquireUmbrellaCoverages(getDriver()).setIncreasedLimit(squireUmbrellaInfo.getIncreasedLimit());
			break;
		case Membership:
			break;
		case StandardFire:
			household.updatePLHouseholdMembersFA(this);
			if (lineSelection.contains( LineSelection.StandardFirePL)) {
				if (this.standardFire.hasStdFireCommodity() == false && pniContact.getInsuranceScore().equals(InsuranceScore.NeedsOrdered)) {
					this.standardFire.setInsuranceScoreReport(creditReport.fillOutCreditReporting(this));
				}
				for (PolicyLocation location : this.standardFire.getLocationList()) {
					for (PLPolicyLocationProperty property : location.getPropertyList()) {
						fillOutStandardFirePropertyFA(property, this.standardFire.section1Deductible);
					}//END FOR
				}//end for
			}//end if
			fillOutPayerAssignment();
			break;
		case StandardLiability:
			household.updatePLHouseholdMembersFA(this);
			break;
		}//end switch

		if(getUWIssues) {
			GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(webDriver);
			risk.performRiskAnalydidAndQuote_UWIssuesOnly(this);
		} else if (!guidewireHelpers.isDraft(this) || !guidewireHelpers.getTypeToGenerate(this).equals(FullApp)) {
			GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(webDriver);
			risk.performRiskAnalysisAndQuote(this);

			if (this.squire.alwaysOrderCreditReport) {
				if (handleBlockSubmit) {
					GenericWorkorderQuote quotePage = new GenericWorkorderQuote(webDriver);
					if (quotePage.hasBlockBind()) {
						risk.handleBlockSubmit(this);
						SideMenuPC sideMenu = new SideMenuPC(webDriver);
						sideMenu.clickSideMenuQuote();
					}
				}

				quote = new GenericWorkorderQuote(webDriver);
				if (productType.equals( ProductLineType.Squire)) {
					squire.getPremium().setSr22ChargesAmount(quote.getQuoteSR22Charge());
				}

				SideMenuPC sideMenu = new SideMenuPC(webDriver);
				sideMenu.clickSideMenuForms();
				sideMenu.clickSideMenuPayment();

				GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(webDriver);
				new GuidewireHelpers(webDriver).getPolicyPremium(this).setInsuredPremium(paymentPage.getTotalInsuredPremiumPortion());
				new GuidewireHelpers(webDriver).getPolicyPremium(this).setTotalAdditionalInterestPremium(paymentPage.getAdditionalInterestPremiumPortion(getLocationList(this)));
				//This section is to get the membership dues amount for BOP policies only. This amount does not show up on the quote page as expected for the time being, but is here.
				//So, we need to get it from here.
				if (productType.equals( ProductLineType.Businessowners)) {
					new GuidewireHelpers(webDriver).getPolicyPremium(this).setMembershipDuesAmount(paymentPage.getMembershipDuesAmount());
				}
				sideMenu.clickSideMenuQuote();
				sideMenu.clickSideMenuForms();
				GenericWorkorderForms formsPage = new GenericWorkorderForms(webDriver);
				formsPage.updateFormsLists(this);
				switch(this.productType) {
				case Businessowners:
					this.busOwnLine.setSubmissionForms(formsPage.getFormDescriptionsFromTable());
					break;
				case CPP:
					break;
				case Membership:
					break;
				case PersonalUmbrella:
					break;
				case Squire:
					break;
				case StandardFire:
					break;
				case StandardIM:
					break;
				case StandardLiability:
					break;
				}

			}

			if (this.squire.alwaysOrderCreditReport && this.productType.equals( ProductLineType.Squire)) {
				SideMenuPC sideMenu = new SideMenuPC(webDriver);
				sideMenu.clickSideMenuRiskAnalysis();
			}
		}else if (guidewireHelpers.isDraft(this)){
			if(!new GuidewireHelpers(webDriver).finds(By.xpath("//span[contains(@id, 'JobWizardToolbarButtonSet:Draft-btnEl')]")).isEmpty()) {
				new GenericWorkorder(webDriver).clickGenericWorkorderSaveDraft();
			}
		}//end draft
		new GuidewireHelpers(webDriver).logout();
	}

	public void fullApp(Builder builderStuff) throws Exception {

		quickQuote(builderStuff);

		if (this.productType !=  ProductLineType.Membership) {
			fullAppGuts();
		}
	}

	public void policySubmittedGuts() throws Exception {
		if(altUser) {
			new repository.gw.login.Login(webDriver).loginAndSearchSubmission(altUserName, altPassword, this.accountNumber);
		} else {
			new repository.gw.login.Login(webDriver).loginAndSearchSubmission(this);
		}

		if (finds(By.xpath("//span[contains(text(), 'Full Application (Quoted)')]")).isEmpty()) {
			if (this.productType.equals( ProductLineType.Membership)) {
				clickGenericWorkorderQuote();
				GenericWorkorderQuote quote = new GenericWorkorderQuote(webDriver);
				PolicyPremium premium = new GuidewireHelpers(webDriver).getPolicyPremium(this);
				premium.setTotalNetPremium(quote.getQuoteTotalPremium());
				premium.setMembershipDuesAmount(quote.getQuoteTotalMembershipDues());
			} else {
				GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(webDriver);
				risk.performRiskAnalysisAndQuote(this);
			}
		}

		if (this.productType.equals( ProductLineType.Membership)) {
			SideMenuPC sideMenu = new SideMenuPC(webDriver);
			sideMenu.clickSideMenuPayment();
		}

		GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(webDriver);
		paymentPage.fillOutPaymentPage(this);
		SideMenuPC sideMenu = new SideMenuPC(webDriver);

		//The below condition is added to check whether agent can auto issue and issue a polocy if no UW Issues -- nagamani
		if(!this.productType.equals( ProductLineType.Businessowners)){
			sideMenu.clickSideMenuForms();
			if(paymentPage.checkGenericWorkorderSubmitOptionsIssuePolicyOption() && new GuidewireHelpers(webDriver).getTypeToGenerate(this).equals(PolicyIssued)){
				sideMenu.clickSideMenuPayment();
				agentAutoIssue = true;
				paymentPage.clickGenericWorkorderSubmitOptionsIssuePolicy();			
				waitUntilElementIsVisible(new GenericWorkorderComplete(webDriver).getJobCompleteTitleBar());
			}
		}
		
		if(!agentAutoIssue) {
			if(paymentPage.canSubmit()) {
				if(!paymentPage.SubmitOnly()) {
					new GenericWorkorderRiskAnalysis_UWIssues(webDriver).handleBlockSubmit(this);
					if(paymentPage.checkGenericWorkorderSubmitOptionsIssuePolicyOption()) {
						agentAutoIssue = true;
						paymentPage.clickGenericWorkorderSubmitOptionsIssuePolicy();			
					} else {
						paymentPage.SubmitOnly();
					}
				}
			} else if(guidewireHelpers.getTypeToGenerate(this).equals(PolicySubmitted)) {
				Assert.fail("YOU REQUESTED A SUBMITTED POLICY BUT THE AGENT ONLY HAS TH OPTION TO ISSUE. PLEASE REWORK YOUR TEST TO ACCOMODATE THIS.");
			}
		}
		
		//The below commented section need to be removed once we got permanent solution for auto issuance.
		
		/*if (this.productType.equals(ProductLineType.Membership)) {
			sideMenu.clickSideMenuQuote();
			if (finds(By.xpath("//label[contains(text(), 'This quote will require underwriting approval prior to issuance.') or contains(text(), 'This quote will require underwriting approval prior to binding.')]")).size() > 0) {
				sideMenu.clickSideMenuRiskAnalysis();
				GenericWorkorderRiskAnalysis_UWIssues riskAnaysis = new GenericWorkorderRiskAnalysis_UWIssues(webDriver);
				riskAnaysis.handleBlockSubmit(this);
				sideMenu = new SideMenuPC(webDriver);
				sideMenu.clickSideMenuQuote();
			}

			sideMenu.clickSideMenuForms();
			clickWhenClickable(find(By.xpath("//span[contains(@id, ':BindOptions-btnEl')]")));
			clickWhenClickable(find(By.xpath("//div[contains(@id, ':BindAndIssue')]")));
			selectOKOrCancelFromPopup(OkCancel.OK);
		} else {
			if(!paymentPage.SubmitOnly()) {
				new GenericWorkorderRiskAnalysis_UWIssues(webDriver).handleBlockSubmit(this);
				paymentPage.SubmitOnly();
			}
		}*/

		setPolicyNumber();

		if (this.productType.equals( ProductLineType.Businessowners)) {
			//click policy number
			clickWhenClickable(find(By.xpath("//span[contains(@id, ':PolicyNumber-btnInnerEl')]")));
			sideMenu.clickSideMenuBuildings();
			for (PolicyLocation location : this.busOwnLine.locationList) {
				GenericWorkorderBuildings buildingPage = new GenericWorkorderBuildings(webDriver);
				buildingPage.clickBuildingsLocationsRow(location.getNumber());
				buildingPage.setAdditionalInterestInfo(location);
			}//end for
		}//end if
		//		if(!this.productType.equals(ProductLineType.Membership)) {
		new GenericWorkorderComplete(webDriver).clickAccountNumber();
		if (!agentAutoIssue) {
			this.underwriterInfo = new AccountSummaryPC(webDriver).getAssignedToUW("Submitted Full Application");
			new GuidewireHelpers(webDriver).setPolicyType(this, PolicySubmitted);
		} else if(!productType.equals( ProductLineType.Membership)) {
			new AccountSummaryPC(webDriver).clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
			this.underwriterInfo = UnderwritersHelper.getUnderwriterInfoByFullName(new PolicySummary(webDriver).getUnderWriter());
		}
		new repository.gw.topinfo.TopInfo(webDriver).clickTopInfoLogout();
	}


	private void setPolicyNumber() {
		GenericWorkorderComplete submittedPage = new GenericWorkorderComplete(webDriver);
		switch(productType) {
		case Businessowners:
			busOwnLine.setPolicyNumber(submittedPage.getPolicyNumber());
			break;
		case CPP:
			commercialPackage.setPolicyNumber(submittedPage.getPolicyNumber());
			break;
		case Membership:
			membership.setPolicyNumber(submittedPage.getPolicyNumber());
			break;
		case PersonalUmbrella:
			squireUmbrellaInfo.setPolicyNumber(submittedPage.getPolicyNumber());
			break;
		case Squire:
			squire.setPolicyNumber(submittedPage.getPolicyNumber());
			break;
		case StandardFire:
			standardFire.setPolicyNumber(submittedPage.getPolicyNumber());
			break;
		case StandardIM:
			standardInlandMarine.setPolicyNumber(submittedPage.getPolicyNumber());
			break;
		case StandardLiability:
			standardLiability.setPolicyNumber(submittedPage.getPolicyNumber());
			break;
		}

		if (this.productType.equals( ProductLineType.Businessowners) || this.productType.equals( ProductLineType.CPP)) {
			this.fullAccountNumber = submittedPage.getAccountNumber() + "-008";
		} else {
			this.fullAccountNumber = submittedPage.getAccountNumber() + "-001";
		}
	}


	public void policySubmitted(Builder builderStuff) throws Exception {
		if (builderStuff.downPaymentType == null) {
			Assert.fail("Submitting Policy, must have a Down Payment Type.");
		}

		if (builderStuff.paymentPlanType == null) {
			Assert.fail("Submitting Policy, must have a Payment Plan Type.");
		}

		this.paymentPlanType = builderStuff.paymentPlanType;
		this.downPaymentType = builderStuff.downPaymentType;

		fullApp(builderStuff);

		policySubmittedGuts();
	}



	public void policyIssuedGuts() throws Exception {
		new GuidewireHelpers(webDriver).setPolicyType(this, PolicyIssued);

		if(!agentAutoIssue){
			if (!this.productType.equals( ProductLineType.Membership)) {
				AccountSummaryPC accountSummaryPage = new AccountSummaryPC(webDriver);
				String uwUserName = underwriterInfo.getUnderwriterUserName();
				String uwPassword = underwriterInfo.getUnderwriterPassword();

				new repository.gw.login.Login(webDriver).loginAndSearchAccountByAccountNumber(uwUserName, uwPassword, this.accountNumber);
				try {
					accountSummaryPage.clickActivitySubject("Submitted Full Application");
				} catch (Exception e) {
					accountSummaryPage.clickActivitySubject("Review and approve submission");
				}
				repository.gw.activity.ActivityPopup activityPopupPage = new ActivityPopup(webDriver);
				try {
					activityPopupPage.setUWIssuanceActivity();
				} catch (Exception e) {
					activityPopupPage.clickCloseWorkSheet();
				}
				switch(this.productType) {
				case Businessowners:
					break;
				case CPP:
					new SideMenuPC(webDriver).clickSideMenuQualification();
					break;
				case Membership:
					break;
				case PersonalUmbrella:
					new SideMenuPC(webDriver).clickSideMenuPolicyInfo();
					break;
				case Squire:
					/*if ((squire.isCountry() || squire.isFarmAndRanch()) && lineSelection.contains(LineSelection.PropertyAndLiabilityLinePL)) {
						if(!squire.propertyAndLiability.squireFPP.getItems().isEmpty()) {
							SideMenuPC sideMenu = new SideMenuPC(webDriver);
							sideMenu.clickSideMenuSquirePropertyCoverages();
							GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(webDriver);
							coverages.clickFarmPersonalProperty();
							coverages.setFarmPersonalPropertyRisk("A");
							sideMenu.clickSideMenuQualification();
						}
					}*/
					break;
				case StandardFire:
					/*SideMenuPC sideMenu = new SideMenuPC(webDriver);
					sideMenu.clickSideMenuSquirePropertyDetail();
					GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details prop = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(webDriver);
					prop.clickViewOrEditBuildingButton(1);
					prop.setRisk("A");
					prop.clickOk();
					prop.clickOkayIfMSPhotoYearValidationShows();
					sideMenu.clickSideMenuQualification();
					//handle block issuance - Supporting Business Declaration Pages Needed
					sideMenu.clickSideMenuRiskAnalysis();
					GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(webDriver);

					risk.approveAll();*/
					break;
				case StandardIM:
					break;
				case StandardLiability:
					break;
				}

				clickGenericWorkorderQuote();

				//Handles prequote issues for PL while they work on the
				new GenericWorkorderQuote(webDriver).handlePreQuoteIssues(this);

				SideMenuPC sideMenu = new SideMenuPC(webDriver);
				sideMenu.clickSideMenuQuote();

				// ACCOUNT FOR "An invalid quote was Generated"
				// PLEASE DO NOT COMMENT OUT, IF ERROR - FIX OR LET ME KNOW :)
				//		if (!this.productType.equals(ProductLineType.Squire) && !this.productType.equals(ProductLineType.StandardFL) && !this.productType.equals(ProductLineType.StandardIM)) {
				if (!this.productType.equals( ProductLineType.Squire) && !this.productType.equals( ProductLineType.Membership) && !this.productType.equals( ProductLineType.StandardFire) && !this.productType.equals( ProductLineType.StandardLiability) && !this.productType.equals( ProductLineType.StandardIM)) {
					repository.gw.errorhandling.ErrorHandlingHelpers quoteError = new ErrorHandlingHelpers(webDriver);
					if (quoteError.errorHandlingRiskAnalysis()) {
						if (quoteError.getErrorMessage().contains("Could not find this Policy in Billing System")) {
							sideMenu = new SideMenuPC(webDriver);
							sideMenu.clickSideMenuQuote();
						} else {
							Assert.fail("Generate Was NOT able to generate a valid quote after five(5) tries or two(2) minutes.");
						}
					}
				}
				GenericWorkorderQuote quotePage = new GenericWorkorderQuote(webDriver);
				quotePage.setPolicyPremiumFields(this);

				sideMenu = new SideMenuPC(webDriver);
				sideMenu.clickSideMenuForms();

				policyForms.updateIssuedPolicyforms(this);
				switch(this.productType) {
				case Businessowners:
					this.busOwnLine.setIssuanceForms(new GenericWorkorderForms(webDriver).getFormDescriptionsFromTable());
					break;
				case CPP:
					break;
				case Membership:
					break;
				case PersonalUmbrella:
					break;
				case Squire:
					break;
				case StandardFire:
					break;
				case StandardIM:
					break;
				case StandardLiability:
					break;
				}

				sideMenu = new SideMenuPC(webDriver);
				sideMenu.clickSideMenuQuote();
				quotePage.issuePolicy( IssuanceType.NoActionRequired);
				if (quotePage.isPreQuoteDisplayed()) {
					sideMenu.clickSideMenuRiskAnalysis();
					new GenericWorkorderRiskAnalysis_UWIssues(webDriver).approveAll();
					sideMenu.clickSideMenuQuote();
					quotePage.issuePolicy(IssuanceType.NoActionRequired);
				}				
				waitUntilElementIsVisible(new GenericWorkorderComplete(webDriver).getJobCompleteTitleBar());
				new TopInfo(webDriver).clickTopInfoLogout();
			}
		}
		
	}


	public void policyIssued(Builder builderStuff) throws Exception {
		policySubmitted(builderStuff);

		policyIssuedGuts();
	}
	
	// The below method is not required and handled during fullapp- Block Issues
	/*public void handleSubmitIssuanceRiskCategories(){
		new GuidewireHelpers(webDriver).editPolicyTransaction();

		SideMenuPC sideMenu = new SideMenuPC(webDriver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		//MOVEME
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propDetailsDetials = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(webDriver);
		char risk = 'A';
		if (this.squire.propertyAndLiability.locationList != null) {
			for (PolicyLocation location : this.squire.propertyAndLiability.locationList) {//int lcv = 0; lcv < TableUtils.getRowCount(propertyDetail.getPropertyDetailLocationsTable()); lcv++){
				propDetailsDetials.clickLocation(location);
				for (PLPolicyLocationProperty building : location.getPropertyList()) {
					propDetailsDetials.clickViewOrEditBuildingButton(building.getPropertyNumber());
					propDetailsDetials.setRisk(risk + "");
					propDetailsDetials.clickOk();
					propDetailsDetials.clickOkayIfMSPhotoYearValidationShows();
					risk = risk++;
				}
			}
		} else {
			int locationCount = new TableUtils(webDriver).getRowCount(propDetailsDetials.getPropertyDetailLocationsTable());
			for (int locationRowNumber = 1; locationRowNumber <= locationCount; locationRowNumber++) {
				new TableUtils(webDriver).clickRowInTableByRowNumber(propDetailsDetials.getPropertyDetailLocationsTable(), locationRowNumber);
				int propertyCount = new TableUtils(webDriver).getRowCount(propDetailsDetials.getPropertyDetailPropertiesTable());
				for (int propertyRowNumber = 1; propertyRowNumber <= propertyCount; propertyRowNumber++) {
					propDetailsDetials.clickViewOrEditBuildingButtonByRowNumber(propertyRowNumber);
					propDetailsDetials.setRisk(risk + "");
					propDetailsDetials.clickOk();
					risk = risk++;
				}
			}
		}
	
}*/

	//    public void policyIssued(GeneratePolicy generateStuff) throws Exception {
	//        mapOldGenerateToNew(generateStuff);
	//
	//        policyIssuedGuts();
	//    }


	public void startNewSubmission() throws Exception {

		//        new Login(webDriver).login(this.agentInfo.getAgentUserName(), this.agentInfo.getAgentPassword());
		//        if (new GuidewireHelpers(webDriver).accountLocked() && randomAgent) {
		//            this.agentInfo = AgentsHelper.getRandomAgent();
		//            new Login(webDriver).login(this.agentInfo.getAgentUserName(), this.agentInfo.getAgentPassword());
		//        }

		new TopMenuPolicyPC(webDriver).clickNewSubmission();

		// Steve Broderick 3-18-2016 added so that the insured will be the driver when using prefill
		boolean found = new SubmissionNewSubmission(webDriver).searchOrCreateSubmission(this).equals("notFound");

		switch(new GuidewireHelpers(webDriver).getCreateNew(this)) {
		case Create_New_Only_If_Does_Not_Exist:
			if(found) {break;}
		case Create_New_Always:
			new SubmissionCreateAccount(webDriver).createAccount(this);
			break;
		case Do_Not_Create_New:
			break;
		}

		if (!finds(By.xpath("//*[contains(text(), 'An error has occurred while standardizing this address')]")).isEmpty()) {
			Assert.fail("ERROR WITH ADDRESS STANDARDIZATION");
		}//END IF
		if(!finds(By.xpath("//div[contains(text(), 'Duplicate key in Account')]")).isEmpty()) {
			Assert.fail("ERROR WITH ACCOUNT NUMBERING");
		}
		waitForPageLoad();
	}//END loginAndStartNewSubmission


	private void addLineOfBusiness() {
		//        new Login(webDriver).loginAndSearchAccountByAccountNumber(this.agentInfo.getAgentUserName(), this.agentInfo.getAgentPassword(), this.accountNumber);

		ActionsPC actions = new ActionsPC(webDriver);
		actions.click_Actions();
		actions.click_NewSubmission();
	}


	public void selectProductAndFillOutQualificationsPage() throws Exception {
		SubmissionProductSelection selectProductPage = new SubmissionProductSelection(webDriver);
		if (new GuidewireHelpers(webDriver).getPolicyEffectiveDate(this) != null) {
			selectProductPage.setSubmissionProductSelectionEffectiveDate(repository.gw.helpers.DateUtils.dateFormatAsString("MM/dd/yyyy", new GuidewireHelpers(webDriver).getPolicyEffectiveDate(this)));
		}
		
		if(quickBuild) {
			busOwnLine.setCurrentPolicyType(FullApp);
			this.accountNumber = selectProductPage.startQuoteSelectProductAndGetAccountNumber( QuoteType.FullApplication, this.productType);
		} else {
			this.accountNumber = selectProductPage.startQuoteSelectProductAndGetAccountNumber(QuoteType.QuickQuote, this.productType);
		}

		if (!(this.productType ==  ProductLineType.Businessowners || this.productType ==  ProductLineType.StandardIM)) {
			new GenericWorkorderLineSelection(webDriver).filloutLineSelection(this);
		}

		new GenericWorkorderQualification(webDriver).fillOutQualifcationsPage(this);
	}


	public void fillOutBusinessOwnersLinePage() throws Exception {
		boolean uwSwitch = false;
		if(busOwnLine.getAdditionalCoverageStuff().isEmploymentPracticesLiabilityInsurance_ThirdPartyViolations() && busOwnLine.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_Handrated() > 0) {
			uwSwitch = true;
			new repository.gw.login.Login(webDriver).switchUserToUW(UnderwriterLine.Commercial);
			new SearchSubmissionsPC(webDriver).searchSubmission(this);
		}
		GenericWorkorderBusinessownersLineIncludedCoverages boLineInclCovPage = new GenericWorkorderBusinessownersLineIncludedCoverages(webDriver);
		boLineInclCovPage.fillOutBusinessownersLinePages(basicSearch, busOwnLine);
		if(uwSwitch) {
			new GenericWorkorder(webDriver).clickGenericWorkorderSaveDraft();
			new GuidewireHelpers(webDriver).logout();
			new repository.gw.login.Login(webDriver).loginAndSearchSubmission(this);
		}
	}


	public void fillOutLocationsAndBuildingsPages() throws Exception {
		// Because the buildings page is inextricably connected to the locations page, it is necessary to fill both pages out within the same method calls. Please follow this method one level deeper to see it split between the locations page and the buildings page.
		GenericWorkorderForms forms = new GenericWorkorderForms(webDriver);
		forms.setProtectiveDevicesForms(this);
		GenericWorkorderLocations location = new GenericWorkorderLocations(webDriver);
		location.fillOutLocationsBuildingsSubmissionQuick(this.basicSearch, this.busOwnLine.locationList);
	}

	public void fillOutBOPLocations_QQ() throws Exception {
		GenericWorkorderLocations location = new GenericWorkorderLocations(webDriver);
		location.fillOutBOPLocation_QQ(this);

	}

	public void fillOutBOPBuildings_QQ() throws Exception {
		GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(webDriver);
		buildings.fillOutBOPBuildings_QQ(this);
	}

	public void fillOutSuplementalAndModifiersPages(boolean quickQuote) throws Exception {
		SideMenuPC sideMenu = new SideMenuPC(webDriver);
		sideMenu.clickSideMenuSupplemental();
		new GenericWorkorderSupplemental(webDriver).handleSupplementalQuestions2(this.busOwnLine.locationList, true, quickQuote, this);
		if (!quickQuote) {
			sideMenu.clickSideMenuModifiers();
		}
	}


	public void fillOutFullAppLocationsAndBuilings(boolean retry) throws Exception {
		SideMenuPC sideMenu = new SideMenuPC(webDriver);
		sideMenu.clickSideMenuLocations();

		GenericWorkorderLocations location = new GenericWorkorderLocations(webDriver);
		location.fillOutLocationsBuildingSubmissionRemaining(this.busOwnLine.locationList);
	}


	///////////////////////
	// COMMERCIAL LINES
	//////////////////////
	public void fillOutCPPCommercialProperty() throws Exception {
		SideMenuPC sideMenu = new SideMenuPC(webDriver);

		//COMMERCIAL PROPERTY LINE
		sideMenu.clickSideMenuCPCommercialPropertyLine();
		GenericWorkorderCommercialPropertyCommercialPropertyLineCPP commercialPropertyLine = new GenericWorkorderCommercialPropertyCommercialPropertyLineCPP(webDriver);
		commercialPropertyLine.fillOutCommercialPropertyLine(this);

		//COMMERCIAL PROPERTY
		sideMenu.clickSideMenuCPProperty();
		GenericWorkorderCommercialPropertyPropertyCPP cppProperty = new GenericWorkorderCommercialPropertyPropertyCPP(webDriver);
		cppProperty.fillOutCommercialPropertyProperty(this);

		// CP modifiers
		sideMenu.clickSideMenuCPModifiers();
		GenericWorkorderModifiers modifiers = new GenericWorkorderModifiers(webDriver);
		modifiers.setRandomModifierValuesCP();

	}//END fillOutCPPCommercialProperty

	public void fillOutCPPGeneralLiability() throws Exception {
		SideMenuPC sideMenu = new SideMenuPC(webDriver);

		// GL exposures
		sideMenu.clickSideMenuGLExposures();
		GenericWorkorderGeneralLiabilityExposuresCPP exposures = new GenericWorkorderGeneralLiabilityExposuresCPP(webDriver);
		exposures.fillOutGeneralLiabilityExposures(this);

		// GL coverages
		sideMenu.clickSideMenuGLCoverages();
		GenericWorkorderGeneralLiabilityCoveragesCPP coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(webDriver);
		coverages.fillOutGeneralLiabilityCoverages(this);

		// GL modifiers
		sideMenu.clickSideMenuGLModifiers();
		GenericWorkorderModifiers modifiers = new GenericWorkorderModifiers(webDriver);
		modifiers.setRandomModifierValuesGL();

	}//END fillOutCPPGeneralLiability

	public void fillOutCPPCommercialAuto() throws Exception {
		SideMenuPC sideMenu = new SideMenuPC(webDriver);
		// CA autoLine
		sideMenu.clickSideMenuCACommercialAutoLine();
		GenericWorkorderCommercialAutoCommercialAutoLineCPP autoLine = new GenericWorkorderCommercialAutoCommercialAutoLineCPP(webDriver);
		autoLine.fillOutCommercialAutoLine(this);

		//CA garagekeepers
		sideMenu.clickSideMenuCAGarageKeepers();
		GenericWorkorderGaragekeepersCoverageCPP garage = new GenericWorkorderGaragekeepersCoverageCPP(webDriver);
		garage.fillOutGaragekeepersCoverageLine(this);

		// CA vehicles
		sideMenu.clickSideMenuCAVehicles();
		GenericWorkorderVehicles vehicles = new GenericWorkorderVehicles(webDriver);
		vehicles.fillOutVehiclesCPP(this);

		// CA stateInfo
		sideMenu.clickSideMenuCAStateInfo();
		GenericWorkorderCommercialAutoStateInfoCPP stateInfo = new GenericWorkorderCommercialAutoStateInfoCPP(webDriver);
		stateInfo.fillOutCommercialAutoStateInfo(this);

		// CA drivers
		sideMenu.clickSideMenuCADrivers();
		GenericWorkorderCommercialAutoDriver driver = new GenericWorkorderCommercialAutoDriver(webDriver);
		driver.fillOutCommercialAutoDriver(this);

		// CA Covered Vehicles
		sideMenu.clickSideMenuCACoveredVehicles();
		GenericWorkorderCommercialAutoCoveredVehicles coveredVehicles = new GenericWorkorderCommercialAutoCoveredVehicles(webDriver);
		coveredVehicles.setCoveredVehiclesPage(this);

		// CA modifiers
		sideMenu.clickSideMenuCAModifiers();
		GenericWorkorderModifiers modifiers = new GenericWorkorderModifiers(webDriver);
		modifiers.setRandomModifierValuesCA();

	}//END fillOutCPPCommercialAuto

	public void fillOutCPPInlandMarine() {
		SideMenuPC sideMenu = new SideMenuPC(webDriver);
		// IM coverage part selection
		sideMenu.clickSideMenuIMCoveragePartSelection();
		GenericWorkorderInlandMarineCoverageSelectionCPP partSelection = new GenericWorkorderInlandMarineCoverageSelectionCPP(webDriver);
		partSelection.fillOutInlandMarineCoveragePartSelection(this);

		// IM Coverage Part Pages
		if (this.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.AccountsReceivable_CM_00_66)) {
			sideMenu.clickSideMenuIMAccountsReceivable().fillOutAccountsReceivable(this);
		}
		if (this.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.BaileesCustomers_IH_00_85)) {
			sideMenu.clickSideMenuIMBaileesCustomers().fillOutBaileesCustomers(this);
		}
		if (this.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.CameraAndMusicalInstrumentDealers_CM_00_21)) {
			sideMenu.clickSideMenuIMCameraAndMusicalInstrument().fillOutCameraAndMusicalInstruments(this);
		}
		if (this.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.CommercialArticles_CM_00_20)) {
			sideMenu.clickSideMenuIMCommercialArticles().fillOutCommercialArticles(this);
		}
		if (this.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.ComputerSystems_IH_00_75)) {
			sideMenu.clickSideMenuIMComputerSystems().fillOutComputerSystems(this);
		}
		if (this.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.ContractorsEquipment_IH_00_68)) {
			sideMenu.clickSideMenuIMContractorsEquipment().fillOutContractorsEquipment(this);
		}
		if (this.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.Exhibition_IH_00_92)) {
			sideMenu.clickSideMenuIMExhibition().fillOutExhibition(this);
		}
		if (this.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.Installation_IDCM_31_4073)) {
			sideMenu.clickSideMenuIMInstallation().fillOutInstallation(this);
		}
		if (this.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.MiscellaneousArticles_IH_00_79)) {
			sideMenu.clickSideMenuIMMiscellaneousArticles().fillOutMiscellaneousArticles(this);
		}
		if (this.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.MotorTruckCargo)) {
			sideMenu.clickSideMenuIMMotorTruckCargo().fillOutMotorTruckCargo(this);
		}
		if (this.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.Signs_CM_00_28)) {
			sideMenu.clickSideMenuIMSigns().fillOutSigns(this);
		}
		if (this.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.TripTransit_IH_00_78)) {
			sideMenu.clickSideMenuIMTripTransit().filloutTripTransit(this);
		}
		if (this.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.ValuablePapers_CM_00_67)) {
			sideMenu.clickSideMenuIMValuablePapers().fillOutValuablePapers(this);
		}

	}//END fillOutCPPInlandMarine


	public void fillOutStandardFirePropertyFA(PLPolicyLocationProperty property, Property.SectionIDeductible deductible) throws GuidewireNavigationException {
		new SideMenuPC(webDriver).clickSideMenuSquirePropertyDetail();

		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(webDriver);
		if (property.getPropertyNumber() == -1) {
			clickAdd();
			propertyDetail.fillOutPropertyDetails_QQ(this.basicSearch, property);
		} else {
			propertyDetail.clickViewOrEditBuildingButton(property.getPropertyNumber());
		}
		propertyDetail.fillOutPropertyDetails_FA(property);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(webDriver);
		constructionPage.editCoverageAPropertyDetailsFA(property);//setCoverageAPropertyDetailsFA(property);
		if (property.getpropertyType() == Property.PropertyTypePL.ResidencePremises || property.getpropertyType() == Property.PropertyTypePL.DwellingPremises || property.getpropertyType() == Property.PropertyTypePL.VacationHome || property.getpropertyType() == Property.PropertyTypePL.CondominiumVacationHome) {
			policyForms.eventsHitDuringSubmissionCreation.add( DocFormEvents.PolicyCenter.StdFire_AddPropertyTypeWhereYouLive);
			if (property.getpropertyType() == Property.PropertyTypePL.CondominiumVacationHome) {
				policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.StdFire_AddPropertyTypeCondo);
			}
		}
		GenericWorkorderSquirePropertyAndLiabilityCoverages coverages2 = new GenericWorkorderSquirePropertyAndLiabilityCoverages(webDriver);
		coverages2.clickOk();
		new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(webDriver).clickOkayIfMSPhotoYearValidationShows();
		if (property.getPropertyNumber() == -1) {
			property.setPropertyNumber(propertyDetail.getSelectedBuildingNum());
		}
	}//END fillOutPLPropertyFA


	public void fillOutPLPersonalAutoQQ() throws Exception {
		SideMenuPC sideMenu = new SideMenuPC(webDriver);
		sideMenu.clickSideMenuPADrivers();
		//DRIVERS
		GenericWorkorderSquireAutoDrivers drivers = new GenericWorkorderSquireAutoDrivers(webDriver);
		drivers.fillOutDriversQQ(this);

		sideMenu.clickSideMenuPACoverages();

		//COVERAGES
		GenericWorkorderSquirePACoverages coverages = new GenericWorkorderSquirePACoverages(webDriver);
		coverages.fillOutSquireAutoCoverages(this);

		sideMenu.clickSideMenuPAVehicles();
		//VEHICLES
		GenericWorkorderVehicles vehicles = new GenericWorkorderVehicles(webDriver);
		vehicles.fillOutVehicles_QQ(this);

	}//END fillOutPLPersonalAutoQQ

	public void updatePLPersonalAutoFA() throws Exception {
		GenericWorkorderSquireAutoDrivers drivers = new GenericWorkorderSquireAutoDrivers(webDriver);
		GenericWorkorderVehicles vehicles = new GenericWorkorderVehicles(webDriver);

		SideMenuPC sideMenu = new SideMenuPC(webDriver);
		// PA drivers
		sideMenu.clickSideMenuPADrivers();
		drivers.fillOutDriversFA(this);

		if (this.productType.equals( ProductLineType.Squire) && this.squire.isFarmAndRanch()) {
			sideMenu.clickSideMenuPACoverages();
			GenericWorkorderSquirePACoverages coverages = new GenericWorkorderSquirePACoverages(webDriver);
			coverages.fillOutSquireAutoCoverages(this);
		}

		// PA vehicles
		sideMenu.clickSideMenuPAVehicles();
		vehicles.fillOutVehicles_FA(this);

	}//END updatePLPersonalAutoFA

	public ArrayList<PolicyLocation> getLocationList(GeneratePolicy policy) {

		switch (policy.productType) {
		case Businessowners:
			return policy.busOwnLine.locationList;
		case CPP:
			return policy.commercialPackage.locationList;
		case PersonalUmbrella:
		case Squire:
			return policy.squire.propertyAndLiability.locationList;
		case StandardIM:
		case Membership:
			break;
		case StandardFire:
			return policy.standardFire.getLocationList();
		case StandardLiability:
			return policy.standardLiability.getLocationList();
		}
		return null;
	}



	public void cancelPolicy(Cancellation.CancellationSourceReasonExplanation explanationToSelect, String description, Date date, boolean cancelNow) {
		new StartCancellation(webDriver).cancelPolicy(explanationToSelect, description, date, cancelNow);
		if(cancelNow) {
			switch(this.productType) {
			case Businessowners:
				break;
			case CPP:
				break;
			case Membership:
				break;
			case PersonalUmbrella:
				break;
			case Squire:
				this.squire.setPolicyCanceled(true);
				break;
			case StandardFire:
				break;
			case StandardIM:
				break;
			case StandardLiability:
				break;
			}
		}
	}

	public void startRenewal() throws Exception {
		if(finds(By.xpath("//input[contains(@id, 'Login:LoginScreen:LoginDV:username-inputEl')]")).isEmpty()) {
			new GuidewireHelpers(webDriver).logout();
		}
		new Login(webDriver).loginAndSearchPolicy_asUW(this);
		new StartRenewal(webDriver).startRenewal();
	}


	public void rewritePolicy() {

	}


	//PROPERTY
	/**
	 * @param property
	 */
	public void addProperty(PLPolicyLocationProperty property) {

	}

	/**
	 * @param property
	 */
	public void removeProperty(PLPolicyLocationProperty property) {

	}

	/**
	 * @param property
	 * @throws Exception
	 */
	public void updateProperty(PLPolicyLocationProperty property, Date changeDate) throws Exception {
		new StartPolicyChange(webDriver).startPolicyChange("Update Property: " + property.getPropertyNumber(), changeDate);
		new SideMenuPC(webDriver).clickSideMenuSquirePropertyCoverages();
		new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(webDriver).clickSpecificBuilding(1, 1);
		new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(webDriver).fillOutCoverageA(property);
		new StartPolicyChange(webDriver).quoteAndIssue();
	}


	//VEHICLE
	public void updateVehicle(Vehicle vehicle) {

	}



	// ////////////////////////////////////
	// Builder Methods to Set Up Policies//
	// ////////////////////////////////////
	public static class Builder {

		private ArrayList< ProductLineType> partsToBuild = new ArrayList< ProductLineType>();

		public DataFactory generateDataFactory = new DataFactory();

		public  ProductLineType productType =  ProductLineType.Businessowners;

		public boolean hasUWIssue = false;

		public FullUnderWriterIssues allUWIssuesAfterFAQuote;

		public String accountNumber = null;
		public String policyNumber = null;
		public String fullAccountNumber = null;
		public Date effectiveDate = null;
		public Date expirationDate = null;
		public Integer polTermLengthDays = 365;

		public boolean hasSR22ChargesForPrimaryNamedInsured = false;
		public boolean membershipDuesOnAllInsureds = false;
		public boolean membershipDuesForPNISetInBuilder = false;
		public boolean membershipDuesForPNILienPaid = false;
		public boolean hasMembershipDuesForPrimaryNamedInsured = true;
		public AdditionalInterest membershipDuesLienHolder = null;

		public  CreateNew createNew = Create_New_Always;
		public boolean handleBlockSubmit = true;
		public boolean handleBlockSubmitRelease = true;

		public  PaymentPlanType paymentPlanType =  PaymentPlanType.getRandom();
		public  PaymentType downPaymentType =  PaymentType.getRandom();
		public BankAccountInfo bankAccountInfo = null;

		public boolean getUWIssues = false;

		public Renewal renewal = null;

		public Agents agentInfo = null;
		public boolean randomAgent = true;
		public boolean nonAgent = false;
		public Underwriters underwriterInfo = null;

		public  OrganizationType polOrgType =  OrganizationType.Partnership;
		public Contact pniContact = new Contact();
		public CountyIdaho ratingCounty = CountyIdaho.Bannock;
		public CountyIdaho polDuesCounty;
		public ArrayList<PolicyInfoAdditionalNamedInsured> aniList = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		public  MembershipType membershipType =  MembershipType.Associate;
		public List<Contact> additionalMembersToAddToMembershipList = new ArrayList<Contact>();

		//MEMBERSHIP
		public Membership membership = new Membership();

		//BUSINESSOWNERS
		public PolicyBusinessownersLine busOwnLine = new PolicyBusinessownersLine();

		//COMMERCIAL LINES
		public CommercialPackagePolicy commercialPackage = new CommercialPackagePolicy();
		public CPPCommercialProperty commercialPropertyCPP = null;
		public CPPGeneralLiability generalLiabilityCPP = null;
		public CPPCommercialAuto commercialAutoCPP = null;
		public CPPInlandMarine inlandMarineCPP = null;

		//PERSONAL LINES
		public Squire squire = new Squire();
		public SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		public StandardFireAndLiability standardFire = new StandardFireAndLiability();
		public StandardFireAndLiability standardLiability = new StandardFireAndLiability();
		public StandardInlandMarine standardInlandMarine = new StandardInlandMarine();
		public List<StandardFireAndLiability> standardFireList = new ArrayList<StandardFireAndLiability>();

		public PolicyPremium premium = new PolicyPremium();
		public ArrayList< LineSelection> lineSelection = new ArrayList< LineSelection>();
		public PolicyForms policyForms = new PolicyForms();
		public boolean veriskData;
		public boolean lexisNexisData;
		public boolean prefillPersonal;
		public boolean prefillCommercial;
		public boolean insuranceScore;
		public boolean mvr;
		public boolean clueAuto;
		public boolean squireStdFire;
		public boolean stdFireLiability;
		public boolean StdFireSquire;
		public boolean StdFireBOP;
		public boolean stdIMSquire;
		public boolean bopForSQ;
		public boolean stdIMStdFire;
		public boolean stdFireLiabilityIM;
		public boolean bopForCPP;
		public boolean multipleStdFire;
		public boolean commodity;
		public boolean clueProperty;
		public PrefillInfo prefillReport;

		public CLUEAutoInfo clueAutoReport;

		public CLUEPropertyInfo cluePropertyReport;
		private WebDriver webDriver;

		public String altUserName = "";
		public String altPassword = "gw";
		public boolean altUser = false;

		private boolean basicSearch = true;
		
		private boolean quickBuild = false;

		public Builder(WebDriver webDriver) {
			this.webDriver = webDriver;
		}

		public Builder forRewrite(WebDriver driver, RewriteType rewriteType) {
			switch (rewriteType) {
			case FullTerm:
				effectiveDate(driver, -5);
				break;
				//			case NewAccount:
				//				break;
			case NewTerm:
				effectiveDate(driver, -50);
				break;
			case RemainderOfTerm:
				effectiveDate(driver, -30);
				break;
			}
			return this;
		}
		

		private Builder effectiveDate(WebDriver driver, int days) {
			return withPolEffectiveDate(repository.gw.helpers.DateUtils.dateAddSubtract(repository.gw.helpers.DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),  DateAddSubtractOptions.Day, days));
		}

		public Builder isDraft() {
			switch (this.productType) {
			case Businessowners:
				busOwnLine.setDraft(true);
				break;
			case CPP:
				commercialPackage.setDraft(true);
				break;
			case Membership:
				membership.setDraft(true);
				break;
			case PersonalUmbrella:
				squireUmbrellaInfo.setDraft(true);
				break;
			case Squire:
				squire.setDraft(true);
				break;
			case StandardFire:
				standardFire.setDraft(true);
				break;
			case StandardIM:
				standardInlandMarine.setDraft(true);
				break;
			case StandardLiability:
				standardLiability.setDraft(true);
				break;
			}
			return this;
		}
		
		public Builder quickBuild() {
			this.quickBuild = true;
			this.nonAgent = true;
			return this;
		}

		public Builder withContact(Contact contact) {
			this.pniContact = contact;
			return this;
		}

		public Builder withNonAgent() {
			this.nonAgent = true;
			return this;
		}


		public Builder withSquireUmbrellaInfo(SquireUmbrellaInfo squireUmbrellaInfo) {
			this.squireUmbrellaInfo = squireUmbrellaInfo;
			return this;
		}

		public Builder withSquire(Squire squire) {
			this.squire = squire;
			return this;
		}

		public Builder withStandardFire(StandardFireAndLiability standardFire) {
			this.standardFire = standardFire;
			return this;
		}

		public Builder withStandardLiability(StandardFireAndLiability standardLiability) {
			this.standardLiability = standardLiability;
			return this;
		}

		public Builder withStandardInlandMarine(StandardInlandMarine standardInlandMarine) {
			this.standardInlandMarine = standardInlandMarine;
			return this;
		}

		public Builder withBusinessownersLine() throws Exception {
			this.partsToBuild.add( ProductLineType.Businessowners);

			PolicyBusinessownersLine basicBOLine = new PolicyBusinessownersLine();
			ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
			locOneBuildingList.add(new PolicyLocationBuilding());

			ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
			locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

			basicBOLine.locationList = locationsList;

			this.busOwnLine = basicBOLine;
			return this;
		}

		public Builder withBusinessownersLine(PolicyBusinessownersLine boLine) throws GuidewireException {
			if(boLine.locationList.isEmpty()) {
				ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
				locOneBuildingList.add(new PolicyLocationBuilding());

				ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
				locationsList1.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));
				boLine.locationList = locationsList1;
			}

			this.productType =  ProductLineType.Businessowners;

			this.busOwnLine = boLine;
			return this;
		}

		public Builder withSquirePolicyUsedForUmbrella(GeneratePolicy squirePolicyUsedForUmbrella) {
			this.partsToBuild.add( ProductLineType.PersonalUmbrella);
			this.squire = squirePolicyUsedForUmbrella.squire;
			this.pniContact = squirePolicyUsedForUmbrella.pniContact;
			this.agentInfo = squirePolicyUsedForUmbrella.agentInfo;
			return this;
		}


		public Builder withBOPPolicyUsedForCPP(GeneratePolicy bopPolicyUsedForCPP, boolean bopForCPP) {
			this.busOwnLine = bopPolicyUsedForCPP.busOwnLine;
			this.pniContact = bopPolicyUsedForCPP.pniContact;
			this.agentInfo = bopPolicyUsedForCPP.agentInfo;
			this.bopForCPP = bopForCPP;
			return this;
		}


		public Builder withMultipleStandardFirePolicies(GeneratePolicy multipleStandardFirePolicies, boolean multipleStdFire) {
			this.standardFireList.add(multipleStandardFirePolicies.standardFire);
			this.pniContact = multipleStandardFirePolicies.pniContact;
			this.agentInfo = multipleStandardFirePolicies.agentInfo;
			this.multipleStdFire = multipleStdFire;
			return this;
		}

		public Builder withSquirePropertyLiabilityExclusionsConditions(SquirePropertyLiabilityExclusionsConditions propLiabExclusionsConditions) {
			this.squire.propertyAndLiability.propLiabExclusionsConditions = propLiabExclusionsConditions;
			return this;
		}

		// JON LARSEN R2

		public Builder withLineSelection( LineSelection...lineSelection) {
			ArrayList< LineSelection> foo = new ArrayList< LineSelection>();
			for(LineSelection selection : lineSelection) {
				foo.add(selection);
			}
			this.lineSelection = foo;
			return this;
		}

		public Builder withCPPCommercialAuto(CPPCommercialAuto cppCommercialAuto) {
			this.commercialAutoCPP = cppCommercialAuto;
			return this;
		}

		public Builder withCPPGeneralLiability(CPPGeneralLiability cppGeneralLiability) {
			this.generalLiabilityCPP = cppGeneralLiability;
			return this;
		}

		public Builder withCPPCommercialProperty(CPPCommercialProperty commercialPropertyCPP) {
			this.commercialPropertyCPP = commercialPropertyCPP;
			return this;
		}

		public Builder withCPPInlandMarine(CPPInlandMarine cppInlandMarine) {
			this.inlandMarineCPP = cppInlandMarine;
			return this;
		}

		public Builder withSquireEligibility( SquireEligibility squireEligibility) {
			this.productType =  ProductLineType.Squire;
			this.squire.squireEligibility = squireEligibility;
			return this;
		}

		public Builder withMembershipType(MembershipType membershipType) {
			this.membershipType = membershipType;
			return this;
		}

		public Builder withMembershipDuesOnAllInsureds() {
			this.membershipDuesOnAllInsureds = true;
			return this;
		}

		/**
		 * This builder method only has effect when the PNI is on a policy with at least one additional insured or membership member being charged dues as well.
		 * Otherwise, the PNI will be forced to have dues anyway.
		 * *****THIS BUILDER OPTION MUST BE SET ONLY AFTER SETTING A COMPANY OR PERSON NAME IN THE BUILDER!!!!!******
		 *
		 * @return
		 */
		public Builder withMembershipDuesOnPNI() {
			this.pniContact.setHasMembershipDues(true);
			return this;
		}

		/**
		 * This builder method only has effect when the PNI is on a policy with at least one additional insured or membership member being charged dues as well.
		 * Otherwise, the PNI will be forced to have dues anyway.
		 * This builder method will take the additional interest entered and set it on the PNI contact object. It will also set the 'hasMembershipDues' and the 'membershipDuesAreLienPaid' boolean flags.
		 * *****THIS BUILDER OPTION MUST BE SET ONLY AFTER SETTING A COMPANY OR PERSON NAME IN THE BUILDER!!!!!******
		 *
		 * @param lienholder The additional interest you want assigned to pay dues for the PNI.
		 * @return
		 */
		public Builder withMembershipDuesForPNIPaidByLienholder(AdditionalInterest lienholder) {
			this.pniContact.setHasMembershipDues(true);
			this.pniContact.setMembershipDuesAreLienPaid(true);
			this.pniContact.setMembershipDuesLienHolder(lienholder);
			return this;
		}

		public Builder withAdditionalMembersToAddToMembershipList(List<Contact> listOfAdditionalMemberContactsToAdd) {
			this.additionalMembersToAddToMembershipList = listOfAdditionalMemberContactsToAdd;
			return this;
		}

		// END R2

		public Builder withProductType( ProductLineType productType) {
			this.productType = productType;
			return this;
		}

		public Builder asAlternateUser(String userName, String password) {
			this.altUser = true;
			this.altUserName = userName;
			this.altPassword = password;
			return this;
		}

		public Builder withCreateNew(CreateNew createNew) {
			this.createNew = createNew;
			return this;
		}

		public Builder withAgent(Agents agentInfo) {
			this.agentInfo = agentInfo;
			this.randomAgent = false;
			return this;
		}
		
		public Builder withContact(GenerateContact contact) {
			withAgent(contact.agent);
			if(contact.companyName == null) {
				withInsFirstLastName(contact.firstName, contact.lastName);
				withInsPersonOrCompany( ContactSubType.Person);
			} else {
				withInsPersonOrCompany( ContactSubType.Company);
				withInsCompanyName(contact.companyName);
			}
			withInsAddressList(contact.addresses);
			return this;
		}

		public Builder withInsPersonOrCompany( ContactSubType insPersonOrCompany) {
			this.pniContact.setPersonOrCompany(insPersonOrCompany);
			return this;
		}

		public Builder withInsFirstLastName(String insFirstName, String insLastName) {
			this.pniContact.setFirstName(insFirstName);
			this.pniContact.setLastName(insLastName);
			this.pniContact.setPersonOrCompany( ContactSubType.Person);
			return this;
		}

		public Builder withInsCompanyName(String insCompanyName) {
			this.pniContact.setCompanyName(insCompanyName);
			this.pniContact.setPersonOrCompany( ContactSubType.Company);
			return this;
		}

		public Builder withInsPersonOrCompanyDependingOnDay(String insFirstName, String insLastName, String insCompanyName) {
			Calendar calendar = Calendar.getInstance();
			int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
			if (dayOfMonth % 2 == 0) {
				this.pniContact.setFirstName(insFirstName);
				this.pniContact.setLastName(insLastName);
				this.pniContact.setPersonOrCompany( ContactSubType.Person);
			} else {
				this.pniContact.setCompanyName(insCompanyName);
				this.pniContact.setPersonOrCompany( ContactSubType.Company);
			}

			return this;
		}

		public Builder withInsPersonOrCompanyRandom(String insFirstName, String insLastName, String insCompanyName) {
			this.pniContact.setPersonOrCompany(new Random().nextBoolean() ?  ContactSubType.Company :  ContactSubType.Person);

			if (this.pniContact.isPerson()) {
				this.pniContact.setFirstName(insFirstName);
				this.pniContact.setLastName(insLastName);
			} else if (this.pniContact.isCompany()) {
				this.pniContact.setCompanyName(insCompanyName);
			}
			return this;
		}

		public Builder withSocialSecurityNumber(String socialSecurityNumber) {
			this.pniContact.setSocialSecurityNumber(socialSecurityNumber);
			return this;
		}

		public Builder withInsPrimaryAddress(AddressInfo insPrimaryAddress) {
			this.pniContact.setAddress(insPrimaryAddress);
			return this;
		}
		
		public Builder withInsAddressList(List<AddressInfo> insAddressList) {
			this.pniContact.setAddress(insAddressList.get(0));
			this.pniContact.setAddressList(insAddressList);
			return this;
		}
		
		public Builder withPniCommuteType(CommuteType type) {
			this.pniContact.setCommuteType(type);
			return this;
		}

		public Builder withRatingCounty(CountyIdaho _county) {
			this.ratingCounty = _county;
			return this;
		}

		public Builder withPolOrgType( OrganizationType polOrgType) {
			this.polOrgType = polOrgType;
			return this;
		}

		public Builder withSiblingPolicy(GeneratePolicy squirePolicyUsedForSibling, String insFirstName, String insLastName) {
			this.pniContact.setFirstName(insFirstName);
			this.pniContact.setLastName(insLastName);
			this.pniContact.setPersonOrCompany( ContactSubType.Person);
			withSiblingStuff(squirePolicyUsedForSibling, null);
			return this;
		}

		public Builder withSiblingPolicy(String squirePolicyNumberUsedForSibling, String insFirstName, String insLastName) {
			this.pniContact.setFirstName(insFirstName);
			this.pniContact.setLastName(insLastName);
			this.pniContact.setPersonOrCompany( ContactSubType.Person);
			withSiblingStuff(null, squirePolicyNumberUsedForSibling);
			return this;
		}


		private void withSiblingStuff(GeneratePolicy squirePolicyUsedForSibling, String squirePolicyNumberUsedForSibling) {
			if (squirePolicyNumberUsedForSibling == null) {
				//				this.squirePolicyUsedForSibling = squirePolicyUsedForSibling;
			} else {
				//				this.squirePolicyNumberUsedForSibling = squirePolicyNumberUsedForSibling;
			}

			this.productType = ProductLineType.Squire;
			this.squire.squireEligibility = SquireEligibility.City;
			this.polOrgType = OrganizationType.Sibling;
			Date currentSystemDate = repository.gw.helpers.DateUtils.getCenterDate(webDriver, ApplicationOrCenter.PolicyCenter);
			this.pniContact.setDob(webDriver, repository.gw.helpers.DateUtils.dateAddSubtract(currentSystemDate,  DateAddSubtractOptions.Year, -20));
			this.pniContact.setMaritalStatus(MaritalStatus.Single);
		}

		public Builder withPolDuesCounty(CountyIdaho polDuesCounty) {
			this.polDuesCounty = polDuesCounty;
			return this;
		}

		public Builder withMembershipDuesForPrimaryNamedInsured(Boolean hasMembershipDuesForPrimaryNamedInsured) {
			this.membershipDuesForPNISetInBuilder = true;
			this.hasMembershipDuesForPrimaryNamedInsured = hasMembershipDuesForPrimaryNamedInsured;
			return this;
		}

		public Builder withMembershipDuesForPNIPaidByLienholder(Boolean membershipDuesForPNILienPaid) {
			this.membershipDuesForPNILienPaid = membershipDuesForPNILienPaid;
			return this;
		}

		public Builder withMembershipDuesLienholderPayer(AdditionalInterest membershipDuesLienHolder) {
			this.membershipDuesLienHolder = membershipDuesLienHolder;
			return this;
		}

		public Builder withPolTermLengthDays(int polTermLengthDays) {
			switch (this.productType) {
			case Businessowners:
				this.busOwnLine.setPolTermLengthDays(polTermLengthDays);
				break;
			case CPP:
				this.commercialPackage.setPolTermLengthDays(polTermLengthDays);
				break;
			case Membership:
				this.membership.setPolTermLengthDays(polTermLengthDays);
				break;
			case PersonalUmbrella:
				this.squireUmbrellaInfo.setPolTermLengthDays(polTermLengthDays);
				break;
			case Squire:
				this.squire.setPolTermLengthDays(polTermLengthDays);
				break;
			case StandardFire:
				this.standardFire.setPolTermLengthDays(polTermLengthDays);
				break;
			case StandardIM:
				this.standardInlandMarine.setPolTermLengthDays(polTermLengthDays);
				break;
			case StandardLiability:
				this.standardLiability.setPolTermLengthDays(polTermLengthDays);
				break;
			}
			return this;
		}

		public Builder withPolEffectiveDate(Date effectiveDate) {
			this.effectiveDate = effectiveDate;
			switch (this.productType) {
			case Businessowners:
				this.busOwnLine.setEffectiveDate(effectiveDate);
				break;
			case CPP:
				this.commercialPackage.setEffectiveDate(effectiveDate);
				break;
			case Membership:
				this.membership.setEffectiveDate(effectiveDate);
				break;
			case PersonalUmbrella:
				this.squireUmbrellaInfo.setEffectiveDate(effectiveDate);
				break;
			case Squire:
				this.squire.setEffectiveDate(effectiveDate);
				break;
			case StandardFire:
				this.standardFire.setEffectiveDate(effectiveDate);
				break;
			case StandardIM:
				this.standardInlandMarine.setEffectiveDate(effectiveDate);
				break;
			case StandardLiability:
				this.standardLiability.setEffectiveDate(effectiveDate);
				break;
			}
			return this;
		}

		public Builder withANIList(ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs) {
			this.aniList = listOfANIs;
			return this;
		}
		
		public Builder withANIList(PolicyInfoAdditionalNamedInsured...listOfANIs) {
			for(PolicyInfoAdditionalNamedInsured insured : listOfANIs) {
				this.aniList.add(insured);
			}
			return this;
		}

		public Builder withPolicyLocations(ArrayList<PolicyLocation> locationList) {
			switch (this.productType) {
			case Businessowners:
				busOwnLine.locationList = locationList;
				break;
			case CPP:
				commercialPackage.locationList = locationList;
				break;
			case PersonalUmbrella:
				break;
			case Squire:
				squire.propertyAndLiability.locationList = locationList;
				break;
			case StandardIM:
				break;
			case Membership:
				break;
			case StandardFire:
				standardFire.setLocationList(locationList);
				break;
			case StandardLiability:
				standardLiability.setLocationList(locationList);
				break;
			}
			return this;
		}

		public Builder withPaymentPlanType(PaymentPlanType paymentPlan) {
			this.paymentPlanType = paymentPlan;
			return this;
		}


		public Builder withDownPaymentType(PaymentType downPaymentType) {
			this.downPaymentType = downPaymentType;
			return this;
		}

		public Builder withBankAccountInfo(BankAccountInfo bankAccountInfo) {
			this.bankAccountInfo = bankAccountInfo;
			return this;
		}

		public Builder withInsAge(int age) {
			Date currentSystemDate = repository.gw.helpers.DateUtils.getCenterDate(webDriver, ApplicationOrCenter.PolicyCenter);
			this.pniContact.setDob(webDriver, repository.gw.helpers.DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Year, -age));
			return this;
		}

		public Builder withPackageRiskType(PackageRiskType riskType) {

			this.commercialPackage.packageRisk = riskType;
			return this;
		}

		public Builder withoutOrderingCreditReport() {
			this.squire.alwaysOrderCreditReport = false;
			return this;
		}

		public Builder withAdvancedSearch() {
			this.basicSearch = false;
			return this;
		}


		public Builder withLexisNexisData(boolean person, boolean prefillPersonal, boolean prefillCommercial, boolean insuranceScore, boolean mvr, boolean clueAuto, boolean clueProperty) throws Exception {
			this.lexisNexisData = true;
			this.prefillPersonal = prefillPersonal;
			this.prefillCommercial = prefillCommercial;
			this.insuranceScore = insuranceScore;
			this.mvr = mvr;
			this.clueAuto = clueAuto;
			this.clueProperty = clueProperty;

			LexisNexis customer = LexisNexisHelper.getRandomCustomer(ClockUtils.getCurrentDates(webDriver).get(ApplicationOrCenter.PolicyCenter), person, prefillPersonal, prefillCommercial, insuranceScore, mvr, clueAuto, clueProperty);

			if (person) {
				this.pniContact.setPersonOrCompany( ContactSubType.Person);
				this.pniContact.setFirstName(customer.getFirstName());
				this.pniContact.setLastName(customer.getLastName());
				this.pniContact.setMiddleName(customer.getMiddleName());
				this.pniContact.setDob(webDriver, customer.getDob());
				this.pniContact.setSocialSecurityNumber(customer.getSsn());
				this.pniContact.setDriversLicenseNum(customer.getDlnumber());
				this.pniContact.setAlternateID("");

				if (!this.pniContact.getStateLicenced().equals(State.Idaho)) {
					this.pniContact.setStateLicenced(State.Idaho);
					this.pniContact.setDriversLicenseNum("GA136510F");
				}

				if (this.pniContact.getDriversLicenseNum() == null) {
					this.pniContact.setDriversLicenseNum("GA136510F");
					this.pniContact.setStateLicenced(State.Idaho);
				} else {
					this.pniContact.setStateLicenced(State.valueOfAbbreviation(customer.getState()));
				}
			} else {
				this.pniContact.setPersonOrCompany( ContactSubType.Company);
				this.pniContact.setCompanyName(customer.getLastName());
			}

			this.pniContact.setAddress(new AddressInfo(customer.getStreet(), customer.getApartment(), customer.getCity(), State.valueOfAbbreviation(customer.getState()), customer.getZip(), CountyIdaho.Ada, "United States", AddressType.Home));

			return this;
		}
		
		public Builder withVeriskContact(InsuranceScoreTestCases contact) throws ParseException {
			this.pniContact.setPersonOrCompany(ContactSubType.Person);
			this.pniContact.setFirstName(contact.getFirstName());
			this.pniContact.setLastName(contact.getLastName());
			this.pniContact.setMiddleName(contact.getMiddleName());
			if(!contact.getDobasof102017().trim().isEmpty()) {
				this.pniContact.setDob(webDriver, repository.gw.helpers.DateUtils.convertStringtoDate(contact.getDobasof102017(), "yyyyMMdd"));
			} else {
				contact.setDobasof102017(DateUtils.dateFormatAsString("yyyyMMdd", this.pniContact.getDob()));
			}
			this.pniContact.setSocialSecurityNumber(contact.getSsn());
			AddressInfo address = new AddressInfo();
			address.setLine1(contact.getaddressLine1());
			address.setCity(contact.getCity());
			address.setState(State.valueOfAbbreviation(contact.getState()));
			address.setZip(contact.getZipCode());
			this.pniContact.setAddress(address);
			this.pniContact.getAddressList().clear();
			this.pniContact.getAddressList().add(address);
			
			this.veriskData= true;
			
			return this;
		}

		public Builder getUWIssues() {
			this.getUWIssues = true;
			return this;
		}

		public Builder withVeriskData(boolean verisk){
			this.veriskData= verisk;
			return this;
		}

		public GeneratePolicy build(GeneratePolicyType typeToGenerate) throws Exception {
			switch (this.productType) {
			case Businessowners:
				this.busOwnLine.setTypeToGenerate(typeToGenerate);
				break;
			case CPP:
				this.commercialPackage.setTypeToGenerate(typeToGenerate);
				break;
			case Membership:
				this.membership.setTypeToGenerate(typeToGenerate);
				break;
			case PersonalUmbrella:
				this.squireUmbrellaInfo.setTypeToGenerate(typeToGenerate);
				break;
			case Squire:
				if (squire.isFarmAndRanch() && typeToGenerate.equals(QuickQuote)) {
					typeToGenerate = FullApp;
				}
				this.squire.setTypeToGenerate(typeToGenerate);
				break;
			case StandardFire:
				this.standardFire.setTypeToGenerate(typeToGenerate);
				break;
			case StandardIM:
				this.standardInlandMarine.setTypeToGenerate(typeToGenerate);
				break;
			case StandardLiability:
				this.standardLiability.setTypeToGenerate(typeToGenerate);
				break;
			}
			return new GeneratePolicy(typeToGenerate, this);
		}

		public Builder withAlternateID(String altID) {
			this.pniContact.setAlternateID(altID);
			return this;
		}

		public Builder withDBA(String...dbas) {
			for(String dba : dbas) {
				pniContact.getDbaList().add(dba);
			}
			return this;
		}

	}

	protected void setQuickQuoteBuilderStuff(Builder builderStuff) {

		switch (builderStuff.productType) {
		case Businessowners:
			this.busOwnLine = builderStuff.busOwnLine;
			break;
		case CPP:
			this.commercialPackage = builderStuff.commercialPackage;
			this.commercialPropertyCPP = builderStuff.commercialPropertyCPP;
			this.generalLiabilityCPP = builderStuff.generalLiabilityCPP;
			this.commercialAutoCPP = builderStuff.commercialAutoCPP;
			this.inlandMarineCPP = builderStuff.inlandMarineCPP;
			break;
		case PersonalUmbrella:
			this.squireUmbrellaInfo = builderStuff.squireUmbrellaInfo;
			break;
		case Squire:
			this.squire = builderStuff.squire;
			break;
		case StandardIM:
			this.standardInlandMarine = builderStuff.standardInlandMarine;
			break;
		case Membership:
			this.membership = builderStuff.membership;
			break;
		case StandardFire:
			this.standardFire = builderStuff.standardFire;
			break;
		case StandardLiability:
			this.standardLiability = builderStuff.standardLiability;
			break;
		}

		//        this.partsToBuild = builderStuff.partsToBuild;
		//        this.generateDataFactory = builderStuff.generateDataFactory;

		this.productType = builderStuff.productType;
		


		this.accountNumber = builderStuff.accountNumber;
		this.fullAccountNumber = builderStuff.fullAccountNumber;

		this.hasSR22ChargesForPrimaryNamedInsured = builderStuff.hasSR22ChargesForPrimaryNamedInsured;
		this.membershipDuesOnAllInsureds = builderStuff.membershipDuesOnAllInsureds;

		this.handleBlockSubmit = builderStuff.handleBlockSubmit;
		this.handleBlockSubmitRelease = builderStuff.handleBlockSubmitRelease;

		this.paymentPlanType = builderStuff.paymentPlanType;
		this.downPaymentType = builderStuff.downPaymentType;
		this.bankAccountInfo = builderStuff.bankAccountInfo;

		this.renewal = builderStuff.renewal;
		this.getUWIssues = builderStuff.getUWIssues;

		this.agentInfo = builderStuff.agentInfo;
		this.randomAgent = builderStuff.randomAgent;
		this.nonAgent = builderStuff.nonAgent;
		this.underwriterInfo = builderStuff.underwriterInfo;

		this.polOrgType = builderStuff.polOrgType;
		this.pniContact = builderStuff.pniContact;
		this.ratingCounty = builderStuff.ratingCounty;
		this.polDuesCounty = builderStuff.polDuesCounty;
		this.aniList = builderStuff.aniList;
		this.additionalMembersToAddToMembershipList = builderStuff.additionalMembersToAddToMembershipList;

		this.lineSelection = builderStuff.lineSelection;
		this.veriskData = builderStuff.veriskData;
		this.lexisNexisData = builderStuff.lexisNexisData;
		this.prefillPersonal = builderStuff.prefillPersonal;
		this.prefillCommercial = builderStuff.prefillCommercial;
		this.insuranceScore = builderStuff.insuranceScore;
		this.mvr = builderStuff.mvr;
		this.clueAuto = builderStuff.clueAuto;
		//		this.squireStdFire = builderStuff.squireStdFire;
		this.stdFireLiability = builderStuff.stdFireLiability;
		this.StdFireSquire = builderStuff.StdFireSquire;
		this.StdFireBOP = builderStuff.StdFireBOP;
		this.stdIMSquire = builderStuff.stdIMSquire;
		this.bopForSQ = builderStuff.bopForSQ;
		this.stdIMStdFire = builderStuff.stdIMStdFire;
		this.stdFireLiabilityIM = builderStuff.stdFireLiabilityIM;
		this.bopForCPP = builderStuff.bopForCPP;
		this.multipleStdFire = builderStuff.multipleStdFire;
		this.commodity = builderStuff.commodity;
		this.clueProperty = builderStuff.clueProperty;
		this.basicSearch = builderStuff.basicSearch;


		this.prefillReport = builderStuff.prefillReport;
		this.clueAutoReport = builderStuff.clueAutoReport;
		this.cluePropertyReport = builderStuff.cluePropertyReport;
		this.altUserName = builderStuff.altUserName;
		this.altPassword = builderStuff.altPassword;
		this.altUser = builderStuff.altUser;
		this.webDriver = builderStuff.webDriver;
		
		this.nonAgent = builderStuff.nonAgent;
		this.quickBuild = builderStuff.quickBuild;

	}

}
