package repository.pc.workorders.generic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.Gender;
import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;

import services.broker.objects.lexisnexis.generic.request.ReportType;
import services.broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillClueADDSubjectType;
import services.broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillReport;
import services.broker.services.lexisnexis.ServiceLexisNexis;
import repository.driverConfiguration.BasePage;
import services.enums.Broker;
import repository.gw.addressstandardization.AddressStandardization;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.MembershipType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyInfoData;
import repository.gw.generate.custom.PrefillInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import gwclockhelpers.ApplicationOrCenter;

import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.LexisNexis;
import persistence.globaldatarepo.helpers.LexisNexisHelper;
import repository.pc.contact.ContactEditPC;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderPolicyInfo extends BasePage {

	private WebDriver driver;
	private TableUtils tableUtils;

	public GenericWorkorderPolicyInfo(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}




	public void fillOutPolicyInfo_QQ(GeneratePolicy policy) {
		switch(policy.productType) {
		case Businessowners:
			setPolicyInfoOrganizationType(policy.polOrgType);
			fillOutBusinessAndOperations(policy);
			setTermLength(policy.busOwnLine.getPolTermLengthDays(), new GuidewireHelpers(driver).getPolicyEffectiveDate(policy));
			break;
		case CPP:
			break;
		case Membership:
			break;
		case PersonalUmbrella:
			break;
		case Squire:
			setPolicyInfoRatingCounty(policy.ratingCounty.getValue());
			setTermLength(policy.squire.getPolTermLengthDays(), new GuidewireHelpers(driver).getPolicyEffectiveDate(policy));
			break;
		case StandardFire:
		case StandardLiability:
		case StandardIM:
			setPolicyInfoOrganizationType(policy.polOrgType);
			setPolicyInfoRatingCounty(policy.ratingCounty.getValue());
			setTermLength(policy.standardFire.getPolTermLengthDays(), new GuidewireHelpers(driver).getPolicyEffectiveDate(policy));
			break;
		}
	}

	public void fillOutPolicyInfo_FA(GeneratePolicy policy) {
		switch(policy.productType) {
		case Businessowners:
			break;
		case CPP:
			break;
		case Membership:
			setPolicyInfoMembershipType(policy.membership.getMembershipType());
			setTermLength(policy.membership.getPolTermLengthDays(), new GuidewireHelpers(driver).getPolicyEffectiveDate(policy));
			break;
		case PersonalUmbrella:
			break;
		case Squire:
			setPolicyInfoOrganizationType(policy.polOrgTypePL);
			break;
		case StandardFire:
		case StandardIM:
		case StandardLiability:
			break;
		}
	}

	public void fillOutPolicyInfo(GeneratePolicy policy) {
		switch(policy.productType) {
		case Businessowners:
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








	//ORGANIZATION TYPE
	private Guidewire8Select select_SubmissionPolicyInfoOrganizationType() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'AccountInfoInputSet:OrganizationType-triggerWrap')]");
	}
	public void setPolicyInfoOrganizationType(OrganizationType polOrgType) {
		Guidewire8Select mySelect = select_SubmissionPolicyInfoOrganizationType();
		mySelect.selectByVisibleText(polOrgType.getValue());
	}

	public void setPolicyInfoOrganizationType(OrganizationTypePL polOrgType) {
		waitForPostBack();
		Guidewire8Select mySelect = select_SubmissionPolicyInfoOrganizationType();
		mySelect.selectByVisibleText(polOrgType.getValue());
	}

	//PNI INFO


	//BUSINESS DESCRIPTION
	public void fillOutBusinessAndOperations(GeneratePolicy policy) {
		setPolicyInfoYearBusinessStarted(policy.yearBusinessStarted);
		setPolicyInfoDescriptionOfOperations(policy.descriptionOfOperations);
	}

	//POLICY DETAILS


	//AGENT DATA

	//ADDITIONAL NAMED INSURED






























	public void fillOutPolicyInfoPage(GeneratePolicy policy) throws Exception {
		PolicyInfoData customerInfo = null;

		repository.pc.sidemenu.SideMenuPC sideMenuStuff = new repository.pc.sidemenu.SideMenuPC(driver);
		sideMenuStuff.clickSideMenuPolicyInfo();

		setSubmissionNumber(policy);

		if (policy.productType.equals(ProductLineType.PersonalUmbrella)) {

			if (policy.squireUmbrellaInfo.getEffectiveDate() == null) {
				policy.squireUmbrellaInfo.setEffectiveDate(policy.squire.getEffectiveDate());
			}
			setPolicyInfoEffectiveDate(DateUtils.dateFormatAsString("MM/dd/yyyy", policy.squireUmbrellaInfo.getEffectiveDate()));
		} else {

			customerInfo = fillOutPage(policy);

			if (policy.productType.equals(ProductLineType.Squire)) {
				Contact newPerson = new Contact(policy.pniContact);
				newPerson.setNamedInsured(true);
				newPerson.setRelationToInsured(RelationshipToInsured.Insured);
				newPerson.setInsuredAlready(true);
				policy.squire.policyMembers.add(newPerson);
			}

			if (policy.prefillPersonal) {
				policy.prefillReport = customerInfo.getPrefillInfo();
				new GenericWorkorderPrefillReportSummary(driver).usePrefill(policy);
			}

			try {
				new GuidewireHelpers(driver).setPolicyEffectiveDate(policy, customerInfo.getEffectiveDate());
				new GuidewireHelpers(driver).setPolicyExpirationDate(policy, customerInfo.getExpirationDate());
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		if(!policy.squire.isFarmAndRanch()) {
			new repository.pc.workorders.generic.GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		}
	}// END fillOutPolicyInfoPage





//	private Guidewire8Select select_SubmissionPolicyInfoOrganizationType() {
//        return new Guidewire8Select(driver, "//table[contains(@id, 'AccountInfoInputSet:OrganizationType-triggerWrap')]");
//    }
//	public void setPolicyInfoOrganizationType(OrganizationType polOrgType) {
//		Guidewire8Select mySelect = select_SubmissionPolicyInfoOrganizationType();
//		mySelect.selectByVisibleText(polOrgType.getValue());
//	}




	@FindBy(xpath = "//input[contains(@id, 'AccountInfoInputSet:BOPInputSet:YearBusinessStarted-inputEl')]")
	private WebElement editbox_SubmissionPolicyInfoYearBusinessStarted;
	public void setPolicyInfoYearBusinessStarted(String yearBusinessStarted) {
		waitUntilElementIsClickable(editbox_SubmissionPolicyInfoYearBusinessStarted);
		editbox_SubmissionPolicyInfoYearBusinessStarted.click();
		editbox_SubmissionPolicyInfoYearBusinessStarted.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_SubmissionPolicyInfoYearBusinessStarted.sendKeys(yearBusinessStarted);
	}

	@FindBy(xpath = "//textarea[contains(@id, 'AccountInfoInputSet:BOPInputSet:BusOps-inputEl')]")
	private WebElement editbox_SubmissionPolicyInfoDescriptionOfOperations;
	public void setPolicyInfoDescriptionOfOperations(String descriptionOfOperations) {
		if (descriptionOfOperations == null) {
			descriptionOfOperations = "They operate their operation in the operable fashion of operators";
		}
		waitUntilElementIsVisible(editbox_SubmissionPolicyInfoDescriptionOfOperations);
		editbox_SubmissionPolicyInfoDescriptionOfOperations.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_SubmissionPolicyInfoDescriptionOfOperations.sendKeys(descriptionOfOperations);
	}

	private Guidewire8Select select_SubmissionPolicyInfoBillingCounty() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'AccountInfoInputSet:PolicyAddressDisplayInputSet:CountyList-triggerWrap')]");
	}

	private Guidewire8Select select_SubmissionPolicyInfoRatingCounty() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'AccountInfoInputSet:PolicyAddressDisplayInputSet:RatingCounty-triggerWrap')]");
	}

	private String xpathDuesCounty = "//table[contains(@id, 'AccountInfoInputSet:PolicyAddressDisplayInputSet:DuesCounty-triggerWrap')]";
	private Guidewire8Select select_SubmissionPolicyInfoDuesCounty() {
		return new Guidewire8Select(driver, xpathDuesCounty);
	}

	private Guidewire8Checkbox checkbox_TransferedFromAnotherPolicy() {
		return new Guidewire8Checkbox(driver, "//table[contains(@id, ':PolicyInfoInputSet:TransferredAnotherPolicy')]");
	}

	@FindBy(xpath = "//a[contains(@id, '_PolicyInfoDV:AccountInfoInputSet:Name:AddDBA')]")
	private WebElement link_ShowDBAs;

	@FindBy(xpath = "//span[contains(@id, '_PolicyInfoDV:AccountInfoInputSet:PolicyDBALV_tb:Add-btnEl')]")
	private WebElement button_AddDBAs;

	@FindBy(xpath = "//div[contains(@id, '_PolicyInfoDV:AccountInfoInputSet:PolicyDBALV')]")
	private WebElement table_DBA;

	@FindBy(xpath = "//div[contains(@id, ':PolicyInfoInputSet:InceptionDateInputSet:1') or contains(@id, ':LOBWizardStepGroup:SubmissionWizard_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:PolicyInfoInputSet:InceptionDateInputSet:1') or contains (@id, ':LOBWizardStepGroup:RenewalWizard_PolicyInfoScreen:RenewalWizard_PolicyInfoDV:PolicyInfoInputSet:InceptionDateInputSet:1') or contains(@id, ':LOBWizardStepGroup:PolicyChangeWizard_PolicyInfoScreen:PolicyChangeWizard_PolicyInfoDV:PolicyInfoInputSet:InceptionDateInputSet:1')]")
	private WebElement table_InceptionDateSet;



	@FindBy(xpath = "//*[contains(@id,'SubmissionWizard:LOBWizardStepGroup:SubmissionWizard_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:NamedInsuredsInputSet:NamedInsuredsLV_tb:AddButton') or contains(@id,':RewriteNewAccountWizard_PolicyInfoDV:NamedInsuredsInputSet:NamedInsuredsLV_tb:AddButton-btnInnerEl')]")
	private WebElement button_addExists;

	@FindBy(xpath = "//a[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:SubmissionWizard_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:NamedInsuredsInputSet:NamedInsuredsLV_tb:AddButton:AddFromPrefill-itemEl')]")
	private WebElement link_AddExisting_FromPrefillReport;

	@FindBy(xpath = "//span[contains(@id,':SubmissionWizard_PolicyInfoDV:NamedInsuredsInputSet:NamedInsuredsLV_tb:AddButton:AddFromPrefill-textEl') or contains(@id,':NamedInsuredsInputSet:NamedInsuredsLV_tb:AddButton:AddOtherContact-textEl')]")
	public WebElement list_PrefillXpath;

	@FindBy(xpath = "//label[contains(@id,':PanelSet:Warning')]")
	private WebElement label_PageHeaderWarning;

	@FindBy(xpath = "//label[contains(@id,':AccountInfoInputSet:PolicyAddressDisplayInputSet:0')]")
	private WebElement label_BinghamCountyMessage;

	public WebElement list_addnlNamedInsuredSpecific(String name) {
		return find(By.xpath("//span[contains(@id,':NamedInsuredsLV_tb:AddButton:AddFromPrefill:') and contains(@id,':PrefillCandidate-textEl') and contains(.,'" + name + "')]"));
	}

	public WebElement list_addnlNamedInsuredOtherContact(String name) {
		return find(By.xpath("//a[contains(@id,':NamedInsuredsLV_tb:AddButton:AddOtherContact:') and contains(@id,':OtherContact-itemEl') and contains(.,'" + name + "')]"));
	}

	public Guidewire8Select select_AdditionalNamedInsuredPNI() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'PolicyContactRoleInputSet:Relationship-triggerWrap') or contains(@id, 'PolicyContactRoleInputSet:Relationship-inputEl')]");
	}


	private Guidewire8Select select_SubmissionPolicyInfoTermType() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'PolicyInfoInputSet:TermType-triggerWrap') or contains(@id, 'PolicyInfo_ExpDateInputSet:TermType')]");
	}

	@FindBy(xpath = "//input[contains(@id, ':PolicyInfoInputSet:EffectiveDate-inputEl')] | //div[contains(@id, ':PolicyInfoInputSet:EffectiveDate-inputEl')]")
	private WebElement editbox_SubmissionPolicyInfoEffectiveDate;
	//
	@FindBy(xpath = "//span[contains(@id, ':PolicyInfoInputSet:ExpirationDate')] | //div[contains(@id, ':PolicyInfoInputSet:EffectiveDate-inputEl')]")
	private WebElement textBox_SubmissionPolicyInfoEffectiveDate;
	//
	@FindBy(xpath = "//input[contains(@id, ':PolicyInfoInputSet:ExpirationDate') or contains(@id, ':PolicyInfo_ExpDateInputSet:ExpirationDate-inputEl')]")
	private WebElement editbox_SubmissionPolicyInfoExpirationDate;
	//
	@FindBy(xpath = "//div[contains(@id, ':PolicyInfoInputSet:ExpirationDate-inputEl')]")
	private WebElement textbox_SubmissionPolicyInfoExpirationDate;
	//
	@FindBy(xpath = "//input[contains (@id, ':PolicyInfoInputSet:WrittenDate-inputEl')]")
	private List<WebElement> editbox_SubmissionPolicyInfoWrittenDate;


	//
	@FindBy(xpath = "//div[contains(@id, 'NamedInsuredsInputSet:NamedInsuredsLV')]/div/div/table")
	private WebElement table_SubmissionPolicyInfoAdditionalNamedInsuredsResults;
	//
	@FindBy(xpath = "//div[@id='PolicyFile_PolicyInfo:PolicyFile_PolicyInfoScreen:PolicyFile_PolicyInfoDV:AccountInfoInputSet:BusinessPhone:GlobalPhoneInputSet:PhoneDisplay-inputEl']")
	private WebElement text_PolicyInfoBusinessPhone;
	//
	@FindBy(xpath = "//input[contains(@id, 'PolicyChangeWizard_PolicyInfoDV:AccountInfoInputSet:BusinessPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')]")
	private WebElement input_PolicyChangePolicyInfoBusinessPhone;
	//
	private Guidewire8Select select_AgentOfService() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':ProducerCode-triggerWrap')]");
	}
	//
	@FindBy(xpath = "//div[contains(@id, ':AccountInfoInputSet:Name-inputEl')]")
	private WebElement link_PrimaryNamedInsured;
	//
	@FindBy(xpath = "//div[contains(@id, ':AccountInfoInputSet:Name-inputEl') or contains(@id,'Name-inputEl')]")
	private WebElement text_PrimaryNamedInsured;
	//
	@FindBy(xpath = "//a[contains(@id,'NamedInsuredsInputSet:NamedInsuredsLV:0:Name')]")
	private WebElement link_ANI;
	//
	@FindBy(xpath = "//span[contains(@id,':ContactRelatedContactsCardTab-btnEl')]")
	private WebElement link_RelatedContactsTab;
	//
	@FindBy(xpath = "//span[contains(@id,':AddressesLV_tb:Add-btnEl')]")
	private WebElement button_Add;
	//
	Guidewire8Select select_DesignatedAddress() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':AddressListing-triggerWrap')]");
	}

	//
	@FindBy(css = "a[id*='LinkAddressMenuMenuIcon']")
	private WebElement button_NewLocationArrow;
	//
	@FindBy(xpath = "//span[contains(text(), 'New Location')]")
	private WebElement button_CreateNewLocation;
	//
	@FindBy(xpath = "//div[contains(@id,':MembershipDuesInputSet:PolicyAssociatesLV')]")
	private WebElement table_MembershipDues;
	//
	@FindBy(xpath = "//a[contains(@id,':MembershipDuesInputSet:PolicyAssociatesLV_tb:AddContactsButton')]")
	private WebElement button_MemberShipDuesAdd;
	//
	@FindBy(xpath = "//div[contains(@id,':PolicyContactRoleDetailsCV:RelatedContactsPanelSet:0')]")
	private WebElement table_RelatedContacts;
	//
	@FindBy(xpath = "//a[contains(@id,':RelatedContactsPanelSet:Add')]")
	private WebElement button_RelatedContactsAdd;
	//
	@FindBy(xpath = "//span[contains(@id,':PolicyAssociatesLV_tb:RemoveReverseButton-btnEl') or contains(@id, ':PolicyAssociatesLV_tb:Remove')]")
	private WebElement button_MemberShipDuesRemove;
	//
	@FindBy(xpath = "//a[contains(@id,'_PolicyInfoDV:MembershipDuesInputSet:PolicyAssociatesLV_tb:RemoveReverseButton-btnEl')]")
	private WebElement button_MemberShipDuesRemoveAndReverse;
	//
	@FindBy(xpath = "//span[contains(@id,':NamedInsuredsInputSet:NamedInsuredsLV_tb:Remove-btnInnerEl')]")
	private WebElement button_ANIRemove;


	/////////////////////////////////////////////////////


	Guidewire8Select select_PolicyInfoMembershipType() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':AccountInfoInputSet:MembershipType-triggerWrap')]");
	}
	//
	private Guidewire8RadioButton radio_HasPolicyOutOfForceMoreThan6Months() {
		return new Guidewire8RadioButton(driver, "//div[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:SubmissionWizard_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:PolicyInfoInputSet:outOfForceMoreThan6Months-containerEl')]/table");
	}
	//
	//	//	Read Only PolicyInfo
	@FindBy(xpath = "//*[contains(@id, '_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:AccountInfoInputSet:OrganizationType-inputEl')]")
	private WebElement text_PolicyInfoOrgType;
	//
	@FindBy(xpath = "//div[contains(@id, ':AddressSummary-inputEl')]")
	private WebElement text_PolicyInfoUmbrellaMailingAddress;
	//
	@FindBy(xpath = "//div[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:SubmissionWizard_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:PolicyInfoInputSet:ExpirationDate-inputEl')]")
	private WebElement text_PolicyInfoUmbrellaExpirationDate;
	//
	@FindBy(xpath = "//div[contains(@id, ':PolicyInfoInputSet:RateAsOfDate-inputEl')]")
	private WebElement text_PolicyInfoUmbrellaRateDate;
	//
	@FindBy(xpath = "//div[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:SubmissionWizard_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:PolicyInfoInputSet:underlyingPolicyNumber-inputEl') or contains(@id, ':underlyingPolicyNumber')]")
	private WebElement text_PolicyInfoUmbrellaPolicyNumber;
	//
	@FindBy(xpath = "//div[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:SubmissionWizard_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:PolicyInfoProducerOfRecordInputSet:ProducerCode-inputEl')]")
	private WebElement text_PolicyInfoUmbrellaAgent;
	//
	@FindBy(xpath = "//span[contains(@id, 'OrderPrefillReportPopup:OrderPrefillReportScreen:PrefillSubmit-btnEl')]")
	private WebElement button_sendPrefillRequest;
	//
	@FindBy(xpath = "//*[contains(@id, 'OrderPrefillReportPopup:OrderPrefillReportScreen:PrefillFinish-btnInnerEl') and contains(text(), 'Finish')]")
	private WebElement button_finish;
	//	// end R2
	//
	//	// PL drichards
	@FindBy(xpath = "//*[contains(@id, ':AccountInfoInputSet:prefillServiceButton')]")
	private WebElement button_orderPrefill;
	//
	@FindBy(xpath = "//div[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:SubmissionWizard_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:NamedInsuredsInputSet:NamedInsuredsLV') or contains(@id,'PolicyChangeWizard:LOBWizardStepGroup:PolicyChangeWizard_PolicyInfoScreen:PolicyChangeWizard_PolicyInfoDV:NamedInsuredsInputSet:NamedInsuredsLV-body') or contains(@id,':NamedInsuredsLV') and not(contains(@id, '-body'))]")
	private WebElement table_AdditionalInsured;
	//
	//	// end PL
	//	// PolicyInfo Insured Info page. These Repository Items are shown after clicking
	//	// the PNI insured Link.
	//
	@FindBy(xpath = "//textarea[contains(@id, ':ContactChangeReason_FBM-inputEl')]")
	private WebElement editbox_ReasonForContactChange;
	//
	@FindBy(xpath = "//input[contains(@id, ':LOBWizardStepGroup:SubmissionWizard_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:PolicyInfoInputSet:TransferredPreviousPolicy-inputEl')]")
	private WebElement editbox_PreviousPolicyNumber;
	//
	@FindBy(xpath = "//input[contains(@id, ':AccountInfoInputSet:OrganizationTypeOtherDescription-inputEl')]")
	private WebElement editbox_Description;
	//
	@FindBy(xpath = "//div[contains(@id, ':UnderlyingPolicyInputSet:underlyingPolicyNumber-inputEl')]")
	private WebElement text_UnderlyingPolicyNumber;
	//
	@FindBy(xpath = "//div[contains(@id, ':UnderlyingPolicyInputSet:underlyingPolicyJobNumber-inputEl')]")
	private WebElement text_UnderlyingPolicyJobNumber;

	@FindBy(xpath = "//input[contains(@id, ':AddressLine1-inputEl')]")
	private WebElement editBox_ContactAddress;

	@FindBy(xpath = "//input[contains(@id, ':City-inputEl') or contains(@id, ':city-inputEl')]")
	private WebElement editBox_ContactCity;

	private Guidewire8Select select_ContactState() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap') or contains(@id, ':state-triggerWrap')]");
	}

	@FindBy(xpath = "//input[contains(@id, ':PostalCode-inputEl') or contains(@id, ':postalCode-inputEl')]")
	private WebElement editBox_ContactZip;


	@FindBy(xpath = "//div[contains(@id, 'PolicyFile_PolicyInfo:PolicyFile_PolicyInfoScreen:PolicyFile_PolicyInfoDV:UWCompanyInputSet:PolicyInfoProducerInfoInputSet:ProducerCodeOfRecord-inputEl')]")
	private WebElement text_AgentOfRecord;

	@FindBy(xpath = "//div[contains(@id, ':PolicyInfoProducerInfoInputSet:CountyOfRecord-inputEl')]")
	private WebElement text_CountyOfRecord;

	private Guidewire8Select select_AddressType() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':AddressType-triggerWrap')]");
	}

    /////////////////////////////////////////
    // Helper Methods for page interaction //
    /////////////////////////////////////////

	private void setAddress(String addressToFill) {
		setText(editBox_ContactAddress, addressToFill);
    }

    private void setCity(String cityToFill) {
		setText(editBox_ContactCity, cityToFill);
    }

	private void setState(State stateToSelect) {
		Guidewire8Select commonState = select_ContactState();
		commonState.selectByVisibleText(stateToSelect.getName());
	}
	public boolean hasPreRenewalDirection() {
		return finds(By.xpath("//span[contains(@id, ':preRenewalDirectionWarning')]")).isEmpty();
	}

    private void setZip(String zipToFill) {
		setText(editBox_ContactZip, zipToFill);
    }

	private void setAddressType(AddressType type) {
		Guidewire8Select mySelect = select_AddressType();
		mySelect.selectByVisibleText(type.getValue());
	}

	public void clickAdd() {
		clickWhenClickable(button_Add);
	}

	public void orderPrefill() {
		clickWhenClickable(button_orderPrefill);
		clickWhenClickable(button_sendPrefillRequest);
		selectOKOrCancelFromPopup(OkCancel.OK);
	}

    public PrefillInfo getPrefill(String firstName, String middleName, String lastName) throws Exception {
		DataprefillReport customer = orderPrefillAndAlsoValidateDirectlyFromBrokerForComparison(firstName, middleName, lastName);
		PrefillInfo toReturn = new PrefillInfo();
		toReturn.setBrokerReport(customer);
		GenericWorkorderPrefillReportSummary prefillSummaryPage = null;
		try {
			prefillSummaryPage = new GenericWorkorderPrefillReportSummary(driver);
			toReturn.setPartiesReported(prefillSummaryPage.getPartiesReported());
			toReturn.setVehiclesReported(prefillSummaryPage.getVehiclesReported());
			toReturn.setPriorPoliciesReported(prefillSummaryPage.getPriorPoliciesReported());
		} catch (Exception e) {
			e.printStackTrace();
		}
		prefillSummaryPage.clickReturnToOrderPrefillReport();
		if (button_finish.isDisplayed())
			clickWhenClickable(button_finish);
		else {
			Assert.fail("Prefill report not retrived from broker");
		}
		return toReturn;
	}

    public DataprefillReport orderPrefillAndAlsoValidateDirectlyFromBrokerForComparison(String firstName, String middleName, String lastName) throws Exception {
		orderPrefill();
		boolean printRequestXMLToConsole = false;
		boolean printResponseXMLToConsole = true;
		Broker conn = new GuidewireHelpers(driver).getMessageBrokerConnDetails();
		systemOut(conn.name());
		ServiceLexisNexis testService = new ServiceLexisNexis();
		LexisNexis[] randomCustomers = { LexisNexisHelper.getCustomerByName(firstName, middleName, lastName) };
		DataprefillReport testResponse = testService.orderPrefillPersonal(testService.setUpTestOrder(ReportType.AUTO_DATAPREFILL, randomCustomers), conn, printRequestXMLToConsole, printResponseXMLToConsole);
		return testResponse;
	}

	public void setAgentOfService(Agents agent) {
		Guidewire8Select mySelect = select_AgentOfService();
		mySelect.selectByVisibleTextPartial(agent.agentPreferredName);
	}



	public String getOrganizationTypeCurrentOption() {
		Guidewire8Select mySelect = select_SubmissionPolicyInfoOrganizationType();
		return mySelect.getText();
	}

	public void setPolicyInfoBillingCounty(String county) {
		Guidewire8Select myBilling = select_SubmissionPolicyInfoBillingCounty();
		if (myBilling.getList().contains(county)) {
			myBilling.selectByVisibleText(county);
		} else {
			if (myBilling.getList().contains(CountyIdaho.Bannock.getValue())) {
				myBilling.selectByVisibleText(CountyIdaho.Bannock.getValue());
			} else {
				Assert.fail("The county searched (" + county + " County) was not found in the dropdown. We tried to set it to Bannock county, but that failed too. Test cannot continue.");
			}
		}
	}

	public void setPolicyInfoRatingCounty(String county) {
		Guidewire8Select myBilling = select_SubmissionPolicyInfoRatingCounty();
		myBilling.selectByVisibleText(county);
	}

	public void setPolicyInfoDuesCounty(String duesCounty) {
		if (duesCounty.equals("Owyhee")) {
			duesCounty = "East Owyhee";
		}
		Guidewire8Select myInfo = select_SubmissionPolicyInfoDuesCounty();
		myInfo.selectByVisibleText(duesCounty);
	}

//	private boolean isExistPolicyInfoDuesCounty() {
//        return checkIfElementExists(xpathDuesCounty, 1000);
//    }

	public void setPolicyInfoTermType(String termType) { // throws Exception {
		Guidewire8Select mySelect = select_SubmissionPolicyInfoTermType();
		mySelect.selectByVisibleText(termType);
	}

	public void setPolicyInfoMembershipType(MembershipType membershipType) {
		Guidewire8Select mySelect = select_PolicyInfoMembershipType();
		mySelect.selectByVisibleText(membershipType.getValue());
	}

	public void setPolicyInfoEffectiveDate(String effectiveDate) { // throws Exception {
		waitUntilElementIsClickable(editbox_SubmissionPolicyInfoEffectiveDate);
		editbox_SubmissionPolicyInfoEffectiveDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_SubmissionPolicyInfoEffectiveDate.sendKeys(effectiveDate);
	}

	public Date getPolicyInfoEffectiveDate() {
		Date dateToReturn = null;
		try {
			dateToReturn = DateUtils.convertStringtoDate(textBox_SubmissionPolicyInfoEffectiveDate.getText(), "MM/dd/yyyy");
		} catch (Exception e) {
			try {
				dateToReturn = DateUtils.convertStringtoDate(editbox_SubmissionPolicyInfoEffectiveDate.getAttribute("value"), "MM/dd/yyyy");
			} catch (ParseException e2) {
				Assert.fail("There was an error while attempting to parse the date string.");
			}
		}
		return dateToReturn;
	}

	public void setPolicyInfoExpirationDate(Date expirationDate) {
		waitUntilElementIsClickable(editbox_SubmissionPolicyInfoExpirationDate);
		editbox_SubmissionPolicyInfoExpirationDate.click();
		editbox_SubmissionPolicyInfoExpirationDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_SubmissionPolicyInfoExpirationDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", expirationDate));
	}

	public Date getPolicyInfoExpirationDate() {
		Date dateToReturn = null;
		try {
			dateToReturn = DateUtils.convertStringtoDate(editbox_SubmissionPolicyInfoExpirationDate.getAttribute("value"), "MM/dd/yyyy");
		} catch (Exception e) {
			try {
				dateToReturn = DateUtils.convertStringtoDate(textbox_SubmissionPolicyInfoExpirationDate.getText(), "MM/dd/yyyy");
			} catch (ParseException e2) {
				Assert.fail("There was an error while attempting to parse the date string.");
			}
		}
		return dateToReturn;
	}




	public Date setTermLength(int polTermLengthDays, Date currentSystemDate) {
		Date expirationDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Year, 1);

		if (polTermLengthDays != 365) {
			setPolicyInfoTermType("Other");
			clickProductLogo();
			expirationDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, polTermLengthDays);
			setPolicyInfoExpirationDate(expirationDate);
			clickProductLogo();
		}
		return expirationDate;
	}


	private void verifyContact(PolicyInfoAdditionalNamedInsured ani) {
		waitForPageLoad();
		if (ani.getCompanyOrPerson() == ContactSubType.Person) {
			try {
				waitUntilElementIsVisible(table_SubmissionPolicyInfoAdditionalNamedInsuredsResults);
				table_SubmissionPolicyInfoAdditionalNamedInsuredsResults.findElement(By.xpath(".//tr[(contains(., '" + ani.getPersonFirstName() + "')) and (contains(., '" + ani.getRelationshipToPNI().getType() + "'))]"));
			} catch (Exception e) {
				Assert.fail("The contact: " + ani.getPersonFirstName() + " " + ani.getPersonLastName() + " was not added correctly as an Additional Named Insured");
			}
		} else {
			try {
				table_SubmissionPolicyInfoAdditionalNamedInsuredsResults.findElement(By.xpath(".//tr[(contains(., '" + ani.getCompanyName() + "')) and (contains(., '" + ani.getRelationshipToPNI().getType() + "'))]"));
			} catch (Exception e) {
				Assert.fail("The contact: " + ani.getCompanyName() + " was not added correctly as an Additional Named Insured");
			}
		}
	}







	//	public void fillOutPolicyInfoPageFA(GeneratePolicy policy) throws Exception {
	//		if (policy.squire.isFarmAndRanch()) {
	//			fillOutPolicyInfoPage(policy);
	//		}
	//
	//		if (!Configuration.getWebDriver().findElements(By.xpath("//span[contains(text(), 'Matching Contacts')]")).isEmpty()) {
	//			clickWhenClickable(Configuration.getWebDriver().findElement(By.xpath("//a[contains(@id, '__crumb__')]")));
	//		}
	//		try {
	//			editANIPage.clickEditAdditionalNamedInsuredUpdate();
	//		} catch (Exception e) {
	//		}
	//	}

	//	private void setContactInfo(PolicyInfoAdditionalNamedInsured ani) {
	//		GenericWorkorderPolicyInfoAdditionalNamedInsured editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
	//		editANIPage.setEditAdditionalNamedInsuredAddressListing(ani.getAddress());
	//		editANIPage.setEditAdditionalNamedInsuredAddressLine1(ani.getAddress().getLine1());
	//		editANIPage.setEditAdditionalNamedInsuredAddressCity(ani.getAddress().getCity());
	//		editANIPage.setEditAdditionalNamedInsuredAddressState(ani.getAddress().getState());
	//		sendArbitraryKeys(Keys.TAB);
	//		editANIPage.setEditAdditionalNamedInsuredAddressZipCode(ani.getAddress().getZip());
	//		sendArbitraryKeys(Keys.TAB);
	//		editANIPage.setEditAdditionalNamedInsuredAddressAddressType(ani.getAddress().getType());
	//	}

	public Date[] fillOutPolicyInfoPage(boolean basicSearch, OrganizationType polOrgType, String polBillingCounty, CountyIdaho polDuesCounty, Integer polTermLengthDays, Date polEffectiveDate, ArrayList<PolicyInfoAdditionalNamedInsured> aniList, String primaryNamedInsured, Boolean membershipDues) throws Exception {

		Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
		String yearBusinessStarted = DateUtils.dateFormatAsString("yyyy", DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Year, -2));
		Date expirationDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Year, 1);

		if (polEffectiveDate == null) {
			polEffectiveDate = currentSystemDate;
		}

		setPolicyInfoOrganizationType(polOrgType);

		setPolicyInfoBillingCounty(polBillingCounty);

		/*
		 * if (polDuesCounty == null) { setPolicyInfoDuesCounty(polBillingCounty); } else { setPolicyInfoDuesCounty(polDuesCounty); }
		 */

		setPolicyInfoYearBusinessStarted(yearBusinessStarted);

		setPolicyInfoDescriptionOfOperations(null);

		setPolicyInfoEffectiveDate(DateUtils.dateFormatAsString("MM/dd/yyyy", polEffectiveDate));

		setTermLength(polTermLengthDays, currentSystemDate);

		for (PolicyInfoAdditionalNamedInsured ani : aniList) {
			addAdditionalNamedInsured(basicSearch, ani);
		}

		setMembershipDues(primaryNamedInsured, membershipDues);

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

		return new Date[] { polEffectiveDate, expirationDate };
	}

	public void fillOutPolicyInfoPageFA(GeneratePolicy policy) throws Exception {
		repository.pc.sidemenu.SideMenuPC sideMenuStuff = new repository.pc.sidemenu.SideMenuPC(driver);
		sideMenuStuff.clickSideMenuPolicyInfo();
		setPolicyInfoOrganizationType(policy.polOrgType);
		setPolicyInfoRatingCounty(policy.ratingCounty.getValue()); // @editor ecoleman 7/30/18: Seems to be required now, at least for FPP
		clickProductLogo();
		if (policy.polOrgType.equals(OrganizationType.Sibling)) {
			setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, policy.squire.getPolicyNumber().replace("-", ""));
		}
		
		if(policy.productType.equals(ProductLineType.Squire)) {
			if(policy.squire.isFarmAndRanch()) {
				Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
				if (policy.squire.getEffectiveDate() == null) {
					policy.squire.setEffectiveDate(currentSystemDate);
				}
				setPolicyInfoEffectiveDate(DateUtils.dateFormatAsString("MM/dd/yyyy", policy.squire.getEffectiveDate()));
				new GuidewireHelpers(driver).setPolicyExpirationDate(policy, setTermLength(new GuidewireHelpers(driver).getPolicyTermLength(policy), new GuidewireHelpers(driver).getPolicyEffectiveDate(policy)));
			}
		}
	}

	public void fillOutStandardIMPolicyInfoPageFA(GeneratePolicy policy) throws Exception {
		repository.pc.sidemenu.SideMenuPC sideMenuStuff = new SideMenuPC(driver);
		sideMenuStuff.clickSideMenuPolicyInfo();
		/*
		 * if (!policy.lineSelection.contains(LineSelection.FourHLivestock)) { setPolicyInfoOrganizationType(policy.polOrgType); }
		 */

		setPolicyInfoBillingCounty(policy.pniContact.getAddress().getCounty());
	}



	@FindBy(xpath = "//span[contains(@id, 'SubmissionWizard:0_header_hd-textEl')]")
	private WebElement text_SubmissionNumber;

	private void setSubmissionNumber(GeneratePolicy policy) {
		String submissionNumber = text_SubmissionNumber.getText().substring(11, text_SubmissionNumber.getText().length() - 6);// -6 is to remove the \nDraft off the end
		switch (policy.productType) {
		case Businessowners:
			policy.busOwnLine.setSubmissionNumber(submissionNumber);
			break;
		case CPP:
			policy.commercialPackage.setSubmissionNumber(submissionNumber);
			break;
		case Membership:
			policy.membership.setSubmissionNumber(submissionNumber);
			break;
		case PersonalUmbrella:
			policy.squireUmbrellaInfo.setSubmissionNumber(submissionNumber);
			break;
		case Squire:
			policy.squire.setSubmissionNumber(submissionNumber);
			break;
		case StandardFire:
			policy.standardFire.setSubmissionNumber(submissionNumber);
			break;
		case StandardIM:
			policy.standardInlandMarine.setSubmissionNumber(submissionNumber);
			break;
		case StandardLiability:
			policy.standardLiability.setSubmissionNumber(submissionNumber);
			break;
		}
	}

    public void usePrefill(GeneratePolicy policy) {

		try {
			for (int i = 0; i < policy.prefillReport.getBrokerReport().getReport().getDataprefillCCDriverDiscovery().getAdditionalDriverDiscovery().getSubject().size(); i++) {
				DataprefillClueADDSubjectType person = policy.prefillReport.getBrokerReport().getReport().getDataprefillCCDriverDiscovery().getAdditionalDriverDiscovery().getSubject().get(i);
				String firstName = person.getName().getFirst();
				String lastName = person.getName().getLast();
				Date dob = DateUtils.convertStringtoDate(person.getBirthdate(), "MM/dd/yyyy");
				Gender gender = Gender.valueOfString(person.getGender().value());

				Contact newPerson2 = new Contact();
				newPerson2.setFirstName(firstName);
				newPerson2.setLastName(lastName);
				newPerson2.setRelationToInsured(RelationshipToInsured.SignificantOther);
				newPerson2.setDob(driver, dob);
				newPerson2.setGender(gender);
				newPerson2.setFromPrefill(true);
				newPerson2.setAddress(policy.pniContact.getAddress());
				policy.squire.policyMembers.add(newPerson2);

			}
		} catch (Exception e) {
			Assert.fail("Prefill report does not have additional driver");
		}
	}// END USEPREFILL()

    public PolicyInfoData fillOutPage(GeneratePolicy policy) throws Exception {

		if (new GuidewireHelpers(driver).getPolicyEffectiveDate(policy) == null) {
			new GuidewireHelpers(driver).setPolicyEffectiveDate(policy, DateUtils.getDateValueOfFormat(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), "MM/dd/yyyy"));
		}

		switch(policy.productType) {
		case Businessowners:
		case CPP:
		case StandardFire:
		case StandardLiability:
		case StandardIM:
			setPolicyInfoOrganizationType(policy.polOrgType);
			break;
		case Membership:
		case PersonalUmbrella:
		case Squire:
			break;
		}

		PrefillInfo customerInfo = null;
		if (policy.prefillPersonal) {
			customerInfo = getPrefill(policy.pniContact.getFirstName(), policy.pniContact.getMiddleName(), policy.pniContact.getLastName());
		}

		switch(policy.productType) {
		case Squire:
		case StandardFire:
		case StandardLiability:
		case StandardIM:
		case Membership:
			break;
		case Businessowners:
		case CPP:
		case PersonalUmbrella:
			fillOutBusinessAndOperations(policy);
			break;
		}

		setPolicyInfoEffectiveDate(DateUtils.dateFormatAsString("MM/dd/yyyy", new GuidewireHelpers(driver).getPolicyEffectiveDate(policy)));
		
		

		Date expirationDate = setTermLength(new GuidewireHelpers(driver).getPolicyTermLength(policy), new GuidewireHelpers(driver).getPolicyEffectiveDate(policy));

		switch(policy.productType) {
		case Businessowners:
		case CPP:
		case Membership:
			break;
		case PersonalUmbrella:
		case Squire:
		case StandardFire:
		case StandardIM:
		case StandardLiability:
			if (policy.pniContact.getAddress().getState().equals(State.Idaho)) {
				setPolicyInfoRatingCounty(policy.ratingCounty.getValue());
			} else {
				// out of state mailing addresses (for lexis-nexus scripts)
				setPolicyInfoRatingCounty(CountyIdaho.Bannock.getValue());
			}
			break;
		}

		if (policy.productType.equals(ProductLineType.Membership)) {
			setPolicyInfoMembershipType(policy.membership.getMembershipType());
		} else {
			for (PolicyInfoAdditionalNamedInsured ani : policy.aniList) {
				if (!policy.productType.equals(ProductLineType.Businessowners) && !policy.productType.equals(ProductLineType.CPP)) {
					Contact newPerson = new Contact();
					newPerson.setNamedInsured(true);
					newPerson.setAddress(ani.getAddress());
					if (ani.getCompanyOrPerson() == ContactSubType.Person) {
						newPerson.setFirstName(ani.getPersonFirstName());
						newPerson.setMiddleName(ani.getPersonMiddleName());
						newPerson.setLastName(ani.getPersonLastName());
						newPerson.setMaritalStatus(MaritalStatus.Married);
						newPerson.setDob(driver,ani.getAniDOB());

					} else if (ani.getCompanyOrPerson() == ContactSubType.Company) {
						newPerson.setCompanyName(ani.getCompanyName());
					}
					newPerson.setRelationToInsured(RelationshipToInsured.Insured);
					newPerson.setInsuredAlready(true);
					policy.squire.policyMembers.add(newPerson);
				}
				addAdditionalNamedInsured(policy.basicSearch, ani);

				// commenting for now as membership dues not shown in policy info - Nagamani
				//This block will handle setting dues on all ANIs if they need them (This will set them to false as well as true depending on the value passed in).
				/*if (ani.getCompanyOrPerson().equals(ContactSubType.Company)) {
					setMembershipDues(ani.getCompanyName(), ani.hasMembershipDues());
					ani.setMembershipDuesAmount(NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_MembershipDues, tableUtils.getRowNumberInTableByText(table_MembershipDues, ani.getCompanyName()), "Amount")));
				} else if (ani.getCompanyOrPerson().equals(ContactSubType.Person)) {
					setMembershipDues((ani.getPersonFirstName() + " " + ani.getPersonLastName()), ani.hasMembershipDues());
					ani.setMembershipDuesAmount(NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_MembershipDues, tableUtils.getRowNumberInTableByText(table_MembershipDues, (ani.getPersonFirstName() + " " + ani.getPersonLastName())), "Amount")));
				}*/
			}

		}

		//            if (policy.aniList.size() > 0) {
		//                for (PolicyInfoAdditionalNamedInsured ani : policy.aniList) {
		//                    if (ani.hasMembershipDues()) {
		//                        if (!ani.getAddress().getState().equals(State.Idaho)) {
		//                            if (ani.getCompanyOrPerson().equals(ContactSubType.Company)) {
		//                                tableUtils.selectValueForSelectInTable(table_MembershipDues, tableUtils.getRowNumberInTableByText(table_MembershipDues, policy.pniContact.getCompanyName()), "County", membershipduesCounty);
		//                            } else if (ani.getCompanyOrPerson().equals(ContactSubType.Person)) {
		//                                tableUtils.selectValueForSelectInTable(table_MembershipDues, tableUtils.getRowNumberInTableByText(table_MembershipDues, policy.pniContact.getFirstName()), "County", membershipduesCounty);
		//                            }
		//                        }
		//                    }
		//                }
		//            }

		//This block will set dues on PNIs if they need them.

		switch(policy.productType) {
		case Businessowners:
			break;
		case CPP:
		case PersonalUmbrella:
			if (policy.pniContact.getPersonOrCompany().equals(ContactSubType.Company)) {
				if (setMembershipDues(policy.pniContact.getCompanyName(), policy.hasMembershipDuesForPrimaryNamedInsured)) {//This will set membership dues to true or false on the PNI, depending on what it's set to.
					new GuidewireHelpers(driver).getPolicyPremium(policy).setMembershipDuesAmountForPrimaryNamedInsured(NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_MembershipDues, tableUtils.getRowNumberInTableByText(table_MembershipDues, policy.pniContact.getCompanyName()), "Amount")));
				}
			} else if (policy.pniContact.getPersonOrCompany().equals(ContactSubType.Person)) {
				if (setMembershipDues((policy.pniContact.getFullName()), policy.hasMembershipDuesForPrimaryNamedInsured)) {
					new GuidewireHelpers(driver).getPolicyPremium(policy).setMembershipDuesAmountForPrimaryNamedInsured(NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(table_MembershipDues, tableUtils.getRowNumberInTableByText(table_MembershipDues, policy.pniContact.getFullName()), "Amount")));
				}
			}
			break;
		case Squire:
			break;
		case StandardIM:
		case StandardFire:
		case StandardLiability:
		case Membership:
			if (policy.hasMembershipDuesForPrimaryNamedInsured) {
				//PC will default membership dues to the PNI, so if the "hasMembershipDuesForPrimaryNamedInsured" flag is set to true, nothing needs to be done.
			} else {
				//Needs logic to remove membership dues from PNI, after and ANI is added and has membership dues assigned to them.
			}
			break;
		}

		//		}
		
		if(!policy.pniContact.getDbaList().isEmpty()) {
			for(String dba : policy.pniContact.getDbaList()) {
				addDBA(dba);
			}
		}
		
		

		return new PolicyInfoData(new GuidewireHelpers(driver).getPolicyEffectiveDate(policy), expirationDate, customerInfo);
	}

	public void addAdditionalNamedInsured(boolean basicSearch, PolicyInfoAdditionalNamedInsured ani) throws GuidewireException {
		GenericWorkorderPolicyInfoAdditionalNamedInsured editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
		editANIPage.clicknPolicyInfoAdditionalNamedInsuredsSearch();
		editANIPage.createNewContact(basicSearch, ani);
		verifyContact(ani);
	}


    public void clickPolicyInfoPrimaryNamedInsured() {
        clickWhenClickable(link_PrimaryNamedInsured);
    }
    
    public void changeAddress(AddressInfo correctAddress) {
    	repository.pc.contact.ContactEditPC editContact = new ContactEditPC(driver);
    	editContact.setContactEditAddressState(correctAddress.getState());
    	editContact.setContactEditAddressCity(correctAddress.getCity());
    	editContact.setContactEditAddressAddressType(correctAddress.getType());
    	editContact.setContactEditAddressLine1(correctAddress.getLine1());
    	editContact.setContactEditAddressZipCode(correctAddress.getZip());
    }
    
    public AddressInfo getAddress() {
    	clickPolicyInfoPrimaryNamedInsured();
    	String line1 = editBox_ContactAddress.getAttribute("value");
    	String city = editBox_ContactCity.getAttribute("value");
    	State state = State.valueOfName(select_ContactState().getText());
    	String zip = editBox_ContactZip.getAttribute("value");
    	
    	return new AddressInfo(line1, city, state, zip);
    }

	public void changePolicyInfoBusinessPhone(String businessPhone) {
		waitUntilElementIsVisible(input_PolicyChangePolicyInfoBusinessPhone);
		input_PolicyChangePolicyInfoBusinessPhone.click();
		input_PolicyChangePolicyInfoBusinessPhone.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		input_PolicyChangePolicyInfoBusinessPhone.sendKeys(businessPhone);
		clickProductLogo();

	}

	public String getPolicyInfoBusinessPhone() {
		waitUntilElementIsVisible(text_PolicyInfoBusinessPhone);
		String businessPhone = text_PolicyInfoBusinessPhone.getText();
		return businessPhone;

	}

	public void clickEditPolicyTransaction() {
		new GuidewireHelpers(driver).editPolicyTransaction();
	}

	public String getPNINameRoleAttribute() {
		return text_PrimaryNamedInsured.getAttribute("role");
	}

	public void clickANI() {
		clickWhenClickable(link_ANI);
	}

	//	public String getANIMembershipType() {
	//		waitUntilElementIsVisible(text_ANIMembershipType);
	//		String aniMembershipType = text_ANIMembershipType.getText();
	//		return aniMembershipType;
	//	}

	//	public boolean isTransferRequired() {
	//		boolean required = false;
	//		if (transferredAnotherPolicy.getAttribute("role").equals("textbox")) {
	//			required = true;
	//		}
	//		return required;
	//	}

	public void clickPreviousPolicySearch() {
		new TableUtils(driver).clickCellInTableByRowAndColumnName(table_InceptionDateSet, 1, "Policy Number");
		WebElement searchElement = find(By.xpath("//div[contains(@id,'0') and contains(@id,':AssociatedPolicyNumber:SelectAssociatedPolicyNumber')]"));
		clickWhenClickable(searchElement);
	}

	@FindBy(xpath = "//div[contains(@id, ':PolicyInfoInputSet:inceptionDate-inputEl')]")
	public WebElement textbox_SubmissionPolicyInfoInceptionDate;
	public Date getInceptionDate() throws ParseException {
		String dateString = textbox_SubmissionPolicyInfoInceptionDate.getText();
		Date dateDate = DateUtils.convertStringtoDate(dateString, "MM/dd/yyyy");
		return dateDate;
	}

	public boolean setMembershipDues(String contactName, boolean trueFalse) {
		int rowNumberContainingContactName = 0;
		if (tableUtils.getRowCount(table_MembershipDues) > 0) {
			rowNumberContainingContactName = tableUtils.getRowNumberInTableByText(table_MembershipDues, contactName);
		}
		if (rowNumberContainingContactName > 0) {
			tableUtils.setCheckboxInTable(table_MembershipDues, rowNumberContainingContactName, "Add Due", trueFalse);
			return true;
		}
		return false;
	}

	public void removeANI(String contactName) {

		int rowNumberContainingContactName = tableUtils.getRowNumberInTableByText(table_AdditionalInsured, contactName);
		tableUtils.setCheckboxInTable(table_AdditionalInsured, rowNumberContainingContactName, true);
		clickWhenClickable(button_ANIRemove);
	}

	public void removeMembershipDuesContact(String contactName) {
		int rowNumberContainingContactName = tableUtils.getRowNumberInTableByText(table_MembershipDues, contactName);
		tableUtils.setCheckboxInTable(table_MembershipDues, rowNumberContainingContactName, true);
		if (isElementDisabled(button_MemberShipDuesRemove) && isElementDisabled(button_MemberShipDuesRemoveAndReverse)) {
			Assert.fail("An attempt was made to remove membership dues, but the \"Remove\" button and the \"Remove And Reverse\" button was not active. This usually means that you attempted to remove the " + "dues tied to the Primary Named Insured and don't have the permissions to remove the dues. Please verify if this is the case and try again.");
		} else if (isElementDisabled(button_MemberShipDuesRemove)) {
			clickWhenClickable(button_MemberShipDuesRemoveAndReverse);
		} else {
			clickWhenClickable(button_MemberShipDuesRemove);
		}
	}

	public void clickANIContact(String contactName) {
		int rowNumber = tableUtils.getRowNumberInTableByText(table_AdditionalInsured, contactName);
		if (rowNumber != 0)
			tableUtils.clickLinkInSpecficRowInTable(table_AdditionalInsured, rowNumber);
		waitForPageLoad();
	}

	public boolean checkANIContact(String contactName) {
		int rowNumber = tableUtils.getRowNumberInTableByText(table_AdditionalInsured, contactName);
		return rowNumber > 0;
	}

	public void checkIfWrittenDateIsEditable() {
		if (editbox_SubmissionPolicyInfoWrittenDate.size() > 0) {
			Assert.fail("ERROR: Written Date is Editable when it should not be.");
		}
	}

	public void setPolicyInfoWrittenDate(Date date){    	
		waitUntilElementIsVisible(editbox_SubmissionPolicyInfoWrittenDate.get(0));
		editbox_SubmissionPolicyInfoWrittenDate.get(0).sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editbox_SubmissionPolicyInfoWrittenDate.get(0).sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", date));
	}



	// read only PolicyInfo

	public String getPolicyInfoOrganizationType() {
		return text_PolicyInfoOrgType.getText();
	}

	public String getPolicyInfoName() {
		waitUntilElementIsClickable(link_PrimaryNamedInsured);
		String name = link_PrimaryNamedInsured.getText();
		return name;
	}

	public String getPolicyInfoUmbrellaMailingAddress() {
		waitUntilElementIsVisible(text_PolicyInfoUmbrellaMailingAddress);
		String address = text_PolicyInfoUmbrellaMailingAddress.getText();
		return address;
	}

	public String getPolicyInfoExpireDate() {
		waitUntilElementIsVisible(text_PolicyInfoUmbrellaExpirationDate);
		String expireDate = text_PolicyInfoUmbrellaExpirationDate.getText();
		return expireDate;
	}

	public Date getPolicyInfoRateDate() throws ParseException {
		waitUntilElementIsVisible(text_PolicyInfoUmbrellaRateDate);
		Date rateDate = DateUtils.convertStringtoDate(text_PolicyInfoUmbrellaRateDate.getText(), "MM/dd/yyyy");
		return rateDate;
	}

	public String getPolicyInfoPolicyNumber() {
		waitUntilElementIsVisible(text_PolicyInfoUmbrellaPolicyNumber);
		String policyNum = text_PolicyInfoUmbrellaPolicyNumber.getText();
		return policyNum;
	}

	public String getPolicyInfoAgent() {
		waitUntilElementIsVisible(text_PolicyInfoUmbrellaAgent);
		String agent = text_PolicyInfoUmbrellaAgent.getText();
		return agent;
	}

	@FindBy(xpath = "//span[contains(@id, ':AddButton:AddFromPrefill-textEl')]")
	private WebElement link_PrefillAddExistingPrefill;

	public void selectAddExistingPrefillAdditionalInsured(String name) {
		/*
         *  clickWhenClickable(button_addExists);  hoverOver(list_PreillXpath); clickWhenClickable(list_addnlNamedInsuredSpecific(name));
         */

		String nameXpath = "//span[contains(text(), '" + name + "')]/parent::a[contains(@id, ':PrefillCandidate-itemEl')]";
		clickWhenClickable(button_addExists);
		hoverOver(link_PrefillAddExistingPrefill);
		clickWhenClickable(By.xpath(nameXpath));

	}

	public void selectAddExistingOtherContactAdditionalInsured(String name) {

		clickWhenClickable(button_addExists);

		hoverOver(list_PrefillXpath);

		clickWhenClickable(list_addnlNamedInsuredOtherContact(name));

	}

	public void clickReturnToOrderPrefillReport() {
		GenericWorkorderPrefillReportSummary prefillSummaryPage = new GenericWorkorderPrefillReportSummary(driver);
		prefillSummaryPage.clickReturnToOrderPrefillReport();

		clickWhenClickable(button_finish);

	}

	public String getAdditionalInsuredName(int row, String columnName) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_AdditionalInsured, row, columnName);
	}

	public void clickMemberShipDuesAddButton() {
		clickWhenClickable(button_MemberShipDuesAdd);
	}

	public void setTransferedFromAnotherPolicy(Boolean checked) {
		checkbox_TransferedFromAnotherPolicy().select(checked);
	}

	public void setCheckBoxInInceptionDateByRow(int row, boolean trueFalse) {
		tableUtils.setCheckboxInTable(table_InceptionDateSet, row, trueFalse);

	}


	public void setCheckBoxInInceptionDateByRowAndColumnHeader(int row, String header, boolean trueFalse) {

		tableUtils.setCheckboxInTable(table_InceptionDateSet, row, header, trueFalse);

	}

	public boolean checkCheckBoxBeforeInceptionDate(InceptionDateSections selection) {
		return checkIfElementExists("//td[contains(., '" + selection.getValue() + "')]/following-sibling::td[1]/div/img", 2000);
	}

	public void clickInceptionDateByRowAndColumnHeader(int row, String header) {

		tableUtils.clickCellInTableByRowAndColumnName(table_InceptionDateSet, row, header);

		hoverOver(table_InceptionDateSet);
	}

	public boolean isInceptionDateCheckBoxDisplayedByRowColumn(int row, String header) {
		String tableGridColumnId = tableUtils.getGridColumnFromTable(table_InceptionDateSet, header);
		try {
			WebElement tableCheckBoxElement = table_InceptionDateSet.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex, '" + (row - 1) + "')]/td[contains(@class, '"	+ tableGridColumnId + "')]//descendant::div[contains(@class, '-inner-checkcolumn')]"));
			return checkIfElementExists(tableCheckBoxElement, 2000);
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isInceptionDateCheckBoxCheckedByRowColumn(int row, String header) {
		String tableGridColumnId = tableUtils.getGridColumnFromTable(table_InceptionDateSet, header);
		try {
			WebElement tableCheckBoxElement = table_InceptionDateSet.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex, '" + (row - 1) + "')]/td[contains(@class, '"	+ tableGridColumnId + "')]//descendant::img[contains(@class, 'x-grid-checkcolumn')]"));
			return tableCheckBoxElement.getAttribute("class").contains("-checkcolumn-checked");
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getOverrideCommissionValue(int row) {
		WebElement tableCheckBox = table_InceptionDateSet.findElement(By.xpath(".//tbody/tr[" + (row) + "]/descendant::div[contains(@class, 'inner-checkcolumn')]/img"));
		List<WebElement> possibleCheckboxMatches = tableCheckBox.findElements(By.xpath(".//div[contains(@class,'grid-checkcolumn-checked')] | .//div/img[contains(@class,'grid-checkcolumn-checked')] | .//img[contains(@class,'grid-checkcolumn-checked')]"));
		return possibleCheckboxMatches.size() > 0;
	}

	public void setOverrideCommissionReason(int row, String reason) {
		tableUtils.clickCellInTableByRowAndColumnName(table_InceptionDateSet, row, "Reason For Override");

		WebElement cellTextBox = table_InceptionDateSet.findElement(By.xpath(".//input[contains(@name,'ReasonForOverrideSally')]"));
		setText(cellTextBox, reason);
		// tableUtils.setValueForCellInsideTable(table_InceptionDateSet, row, "Reason
		// For Override", "ReasonForOverrideSally", reason);

	}

	public String getOverrideCommissionReason(int row) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_InceptionDateSet, row, "Reason For Override");
	}

	private boolean searchInceptionDateByPolicyNumber(int row, String policy) {
		clickProductLogo();

		tableUtils.setCheckboxInTable(table_InceptionDateSet, row, true);


		sendArbitraryKeys(Keys.TAB);
		tableUtils.clickCellInTableByRowAndColumnName(table_InceptionDateSet, row, "Policy Number");

		WebElement searchElement = find(By.xpath("//div[contains(@id,'" + (row - 1)
				+ "') and contains(@id,':AssociatedPolicyNumber:SelectAssociatedPolicyNumber')]"));
		clickWhenClickable(searchElement);

		repository.pc.search.SearchPoliciesPC searchPolicies = new repository.pc.search.SearchPoliciesPC(driver);
		searchPolicies.clickReset();

		searchPolicies.setPolicyNumber(policy);
		searchPolicies.clickSearch();

		try {
			WebElement results = find(By.xpath("//a[contains(@id, 'PolicySearchPopup:PolicySearchScreen:DatabasePolicySearchPanelSet:PolicySearch_ResultsLV:0:_Select')]"));
			clickWhenClickable(results);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean setInceptionPolicyNumberBySelection(InceptionDateSections selection, String policyNumber) {
		int row = tableUtils.getRowNumberInTableByText(table_InceptionDateSet, selection.getValue());
		return searchInceptionDateByPolicyNumber(row, policyNumber);
	}

	public String getInceptionPolicyNumberBySelection(InceptionDateSections selection) {
		int row = tableUtils.getRowNumberInTableByText(table_InceptionDateSet, selection.getValue());
		return tableUtils.getCellTextInTableByRowAndColumnName(table_InceptionDateSet, row, "Policy Number");
	}

	public void setInceptionDatePolicyNumberDirectly(InceptionDateSections selection, String policyNumber) {
		int row = tableUtils.getRowNumberInTableByText(table_InceptionDateSet, selection.getValue());
		tableUtils.setValueForCellInsideTable(table_InceptionDateSet, row, "Policy Number", "AssociatedPolicyNumber",
				policyNumber);
	}

	public void setInceptionDateByRow(int row, Date date) {
		String newInceptionDate = DateUtils.dateFormatAsString("MM/dd/yyyy", date);
		waitForPostBack();
		tableUtils.setValueForCellInsideTable(table_InceptionDateSet, row, "Inception Date", "InceptionDate", newInceptionDate);
		waitForPostBack();
	}

	public Date getInceptionDateFromTableBySelection(InceptionDateSections selection) {
		int row = tableUtils.getRowNumberInTableByText(table_InceptionDateSet, selection.getValue());

		Date dateToReturn = null;
		try {
			dateToReturn = DateUtils.convertStringtoDate(tableUtils.getCellTextInTableByRowAndColumnName(table_InceptionDateSet, row, "Inception Date"), "MM/dd/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateToReturn;
	}

	public void addDBA(String dba) {
		clickWhenClickable(link_ShowDBAs);
		clickWhenClickable(button_AddDBAs);
		tableUtils.setValueForCellInsideTable(table_DBA, tableUtils.getNextAvailableLineInTable(table_DBA), "Name",	"Name", dba);
	}

    public void setReasonForContactChange(String reason) {
		setText(editbox_ReasonForContactChange, reason);
    }

    public void addNewAddress(AddressInfo newAddress, String reasonForChange) {
        clickPolicyInfoPrimaryNamedInsured();
		clickWhenClickable(button_NewLocationArrow);
		clickWhenClickable(button_CreateNewLocation);
		setZip(newAddress.getZip());
		setState(newAddress.getState());
        setCity(newAddress.getCity());
		setAddress(newAddress.getLine1());
        setAddressType(AddressType.Mailing);
		if (reasonForChange != "") {
			setReasonForContactChange(reasonForChange); //@editor ecoleman 7/17/18: not needed as far as I can see
		} else {
			if (checkIfElementExists(editbox_ReasonForContactChange, 5)) {
				setReasonForContactChange("Default Reason for stuff");
			}
		}
        clickUpdate();

		AddressStandardization standardizeAddress = new AddressStandardization(driver);
        if (standardizeAddress.checkAddressStandardizationOverrideLinkExists()) {
            clickUpdate();
        }
    }
    
	public void setPolicyOutOfForceMoreThan6MonthsRadio(boolean trueFalse) {

		radio_HasPolicyOutOfForceMoreThan6Months().select(trueFalse);

	}

	public boolean setPreviousPolicyNumberBOP(String accountNumber) {

		clickWhenClickable(editbox_PreviousPolicyNumber);
		WebElement searchElement = find(By.xpath(
				"//div[contains(@id,':SubmissionWizard_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:PolicyInfoInputSet:TransferredPreviousPolicy:SelectTransferredPreviousPolicy')]"));
		clickWhenClickable(searchElement);

		repository.pc.search.SearchPoliciesPC searchPolicies = new SearchPoliciesPC(driver);
		searchPolicies.clickReset();

		searchPolicies.setPolicyNumber(accountNumber);
		searchPolicies.clickSearch();

		if (AddressBookSelectExists()) {
			searchPolicies.selectFirstTableResult();

			return true;
		} else
			return false;
	}

	private boolean AddressBookSelectExists() {
		try {
			find(By.xpath("//a[contains(@id, 'PolicySearchPopup:PolicySearchScreen:DatabasePolicySearchPanelSet:PolicySearch_ResultsLV:0:_Select')]"));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getMembershipDuesNameByRowNumber(int row) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_MembershipDues, row, "Name");
	}

	public boolean getMembershipDueSelectedByName(String name) {
		int row = tableUtils.getRowNumberInTableByText(table_MembershipDues, name);
		return tableUtils.getCellTextInTableByRowAndColumnName(table_MembershipDues, row, "Add Due").contains("Yes");
	}

	public void setPolicyInfoDescription(String desc) {

		setText(editbox_Description, desc);

	}

	public String getPageTopWarningMessage() {
		return label_PageHeaderWarning.getText();
	}

	public String getUnderlyingPolicyNumber() {
		return text_UnderlyingPolicyNumber.getText();
	}

	public String getUnderlyingPolicyJobNumber() {
		return text_UnderlyingPolicyJobNumber.getText();
	}

	public String getBillingCountyMessage() {
		return label_BinghamCountyMessage.getText();
	}

	public boolean verifyPolicyInfoBillingCountyList(String item) {

		Guidewire8Select myBilling = select_SubmissionPolicyInfoBillingCounty();
		return myBilling.isItemInList(item);
	}

	public void selectMembershipDuesCounty(String firstName, String county) {
		tableUtils.selectValueForSelectInTable(table_MembershipDues,
				tableUtils.getRowNumberInTableByText(table_MembershipDues, firstName), "County", county);
	}

	public void clickRelatedContactsTab() {
		clickWhenClickable(link_RelatedContactsTab);
	}

	public void clickRelatedContactsAddButton() {
		clickWhenClickable(button_RelatedContactsAdd);
	}

	public void selectRelationship(int row, String relationship) {
		tableUtils.selectValueForSelectInTable(table_RelatedContacts, row, "Relationship", relationship);
	}

	public String getRelationship(int row) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_RelatedContacts, row, "Relationship");
	}

	public String getRelatedTo(int row) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_RelatedContacts, row, "Related To");
	}

	public void setRelatedTo(int row, String firstName, String lastName) {
		String gridColumnID = tableUtils.getGridColumnFromTable(table_RelatedContacts, "Related To");
		WebElement tableCell = table_RelatedContacts.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (row - 1) + "')]/td[contains(@class,'" + gridColumnID + "')]/div"));
		clickWhenClickable(tableCell);

		clickWhenClickable(find(By.xpath("//div[contains(@id,'EditPolicyContactRolePopup:ContactDetailScreen:PolicyContactRoleDetailsCV:RelatedContactsPanelSet:" + (row - 1) + ":pick:Selectpick')]")));
		repository.pc.search.SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
		WebElement selectButton = addressBook.searchAddressBookByPersonDetails(firstName, lastName, null, null, null);
		clickWhenClickable(selectButton);
	}

	public boolean checkMembershipDuesAddButtonDisabled() {
		return isElementDisabled(button_MemberShipDuesAdd);
	}

	public boolean checkMembershipDuesRemoveButtonDisabled(String contactName) {
		int rowNumberContainingContactName = tableUtils.getRowNumberInTableByText(table_MembershipDues, contactName);
		WebElement checkBox = table_MembershipDues.findElement(By.xpath(".//tbody/tr[" + (rowNumberContainingContactName) + "]/descendant::div[contains(@class, '-inner-checkcolumn')]"));
		clickWhenClickable(checkBox);

		return isElementDisabled(button_MemberShipDuesRemove);
	}

	public void clickMembershipContact(String contactName) {
		int rowNumber = tableUtils.getRowNumberInTableByText(table_MembershipDues, contactName);

		if (rowNumber != 0)
			tableUtils.clickLinkInSpecficRowInTable(table_MembershipDues, rowNumber);

	}

	public boolean checkMembershipContact(String contactName) {
		return tableUtils.getRowNumberInTableByText(table_MembershipDues, contactName) > 0;
	}

	public List<String> getTermTypeOptions() {
		Guidewire8Select mySelect = select_SubmissionPolicyInfoTermType();
		return mySelect.getList();
	}

	public String getAgentOfRecord() {
		waitUntilElementIsVisible(text_AgentOfRecord);
		return text_AgentOfRecord.getText();
	}
	public String getCoutyOfRecord() {
		waitUntilElementIsVisible(text_CountyOfRecord);
		return text_CountyOfRecord.getText();
	}
}
