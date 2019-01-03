package repository.pc.workorders.generic;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.idfbins.enums.Gender;
import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;

import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DriverType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.Vehicle.AdditionalInterestTypeCPP;
import repository.gw.enums.Vehicle.BodyType;
import repository.gw.enums.Vehicle.BusinessUseClass;
import repository.gw.enums.Vehicle.CAVehicleAdditionalCoverages;
import repository.gw.enums.Vehicle.CAVehicleExclusionsAndConditions;
import repository.gw.enums.Vehicle.CommutingMiles;
import repository.gw.enums.Vehicle.DamageDescriptionCPP;
import repository.gw.enums.Vehicle.FrontDamagedItemCPP;
import repository.gw.enums.Vehicle.GlassDamagedItemCPP;
import repository.gw.enums.Vehicle.HoodRoofTrunklidDamagedItemCPP;
import repository.gw.enums.Vehicle.HowManyTimesAYearDoesApplicantTravelOver300Miles;
import repository.gw.enums.Vehicle.LeftFrontDamagedItemCPP;
import repository.gw.enums.Vehicle.LeftFrontPillarDamagedItemCPP;
import repository.gw.enums.Vehicle.LeftQuarterPostDamagedItemCPP;
import repository.gw.enums.Vehicle.LeftRearDamagedItemCPP;
import repository.gw.enums.Vehicle.LeftTBoneDamagedItemCPP;
import repository.gw.enums.Vehicle.LocationOfDamageCPP;
import repository.gw.enums.Vehicle.OtherDamagedItemCPP;
import repository.gw.enums.Vehicle.Radius;
import repository.gw.enums.Vehicle.RearDamagedItemCPP;
import repository.gw.enums.Vehicle.RightFrontDamagedItemCPP;
import repository.gw.enums.Vehicle.RightFrontPillarDamagedItemCPP;
import repository.gw.enums.Vehicle.RightQuarterPostDamagedItemCPP;
import repository.gw.enums.Vehicle.RightRearDamagedItemCPP;
import repository.gw.enums.Vehicle.RightTBoneDamagedItemCPP;
import repository.gw.enums.Vehicle.SeatingCapacity;
import repository.gw.enums.Vehicle.SecondaryClass;
import repository.gw.enums.Vehicle.SecondaryClassType;
import repository.gw.enums.Vehicle.SizeClass;
import repository.gw.enums.Vehicle.SpecifiedCauseOfLoss;
import repository.gw.enums.Vehicle.UndercarriageDamagedItemCPP;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.enums.VehicleUse;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderVehicles extends repository.pc.workorders.generic.GenericWorkorder {


    private Agents agent = null;
    private Underwriters underwriter = null;
    private String accountNumber = "";

    private WebDriver driver;
    private TableUtils tableUtils;
    GuidewireHelpers gwh;

    public GenericWorkorderVehicles(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        gwh = new GuidewireHelpers(driver);
        PageFactory.initElements(driver, this);
    }


    public void fillOutVehicles_QQ(GeneratePolicy policy) throws Exception {
        for (Vehicle vehicle : policy.squire.squirePA.getVehicleList()) {
            if (vehicle.getDriverPL() == null || vehicle.getDriverPL().isContactIsPNI()) {
                vehicle.setDriverPL(policy.pniContact);
            }
            createVehicleManually();
            new repository.pc.workorders.generic.GenericWorkorderVehicles_Details(driver).fillOutVehicleDetails_QQ(policy.basicSearch, vehicle);
            new GenericWorkorderVehicles_CoverageDetails(driver).fillOutCoverageDetails_QQ(vehicle);
            new GenericWorkorderVehicles_ExclusionsAndConditions(driver).fillOutExclusionsAndConditions_QQ(vehicle);
            clickOK();
            new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':VehicleDetailTitle') and (text()='Vehicle Details')]", 5000, "UNABLE TO GET TO MAIN VEHICLE PAGE AFTER CLICKING OK BUTTON.");
            vehicle.setVehicleNumber(Integer.parseInt(text_vehicleNumber.getText()));
        }
        clickGenericWorkorderSaveDraft();
    }

	public void fillOutVehicles_FA(GeneratePolicy policy) throws Exception {
		for(Vehicle vehicle : policy.squire.squirePA.getVehicleList()) {
			if(policy.squire.isFarmAndRanch()) {
				createVehicleManually();
				new repository.pc.workorders.generic.GenericWorkorderVehicles_Details(driver).fillOutVehicleDetails_QQ(policy.basicSearch, vehicle);
				new repository.pc.workorders.generic.GenericWorkorderVehicles_Details(driver).fillOutVehicleDetails_FA(vehicle);
				new GenericWorkorderVehicles_CoverageDetails(driver).fillOutCoverageDetails_QQ(vehicle);
				new GenericWorkorderVehicles_CoverageDetails(driver).fillOutCoverageDetails_FA(vehicle);
				new GenericWorkorderVehicles_ExclusionsAndConditions(driver).fillOutExclusionsAndConditions_QQ(vehicle);
				new GenericWorkorderVehicles_ExclusionsAndConditions(driver).fillOutExclusionsAndConditions_FA(vehicle);
			} else {
				editVehicleByVehicleNumber(vehicle.getVehicleNumber());
				new repository.pc.workorders.generic.GenericWorkorderVehicles_Details(driver).fillOutVehicleDetails_FA(vehicle);
				new GenericWorkorderVehicles_CoverageDetails(driver).fillOutCoverageDetails_FA(vehicle);
				new GenericWorkorderVehicles_ExclusionsAndConditions(driver).fillOutExclusionsAndConditions_FA(vehicle);
			}
			waitForPostBack();
            clickOK();
            new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':VehicleDetailTitle') and (text()='Vehicle Details')]", 5000, "UNABLE TO GET TO MAIN VEHICLE PAGE AFTER CLICKING OK BUTTON");
        }
    }

    public void fillOutVehicles(GeneratePolicy policy) throws Exception {
        for (Vehicle vehicle : policy.squire.squirePA.getVehicleList()) {
            new GenericWorkorderVehicles_Details(driver).fillOutVehicleDetails(policy.basicSearch, vehicle);
            new GenericWorkorderVehicles_CoverageDetails(driver).fillOutCoverageDetails(vehicle);
            new GenericWorkorderVehicles_ExclusionsAndConditions(driver).fillOutExclusionsAndConditions(vehicle);
            clickOK();
            new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':VehicleDetailTitle') and (text()='Vehicle Details')]", 5000, "UNABLE TO GET TO MAIN VEHICLE PAGE AFTER CLICKING OK BUTTON");
        }
    }


    private Guidewire8Select select_VehicleType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'VehicleDV:Type-triggerWrap') or contains(@id, ':PersonalAuto_VehicleDV:Type_DV-triggerWrap')]");
    }

    private void selectVehicleType(VehicleType type) {
        Guidewire8Select mySelect = select_VehicleType();
        mySelect.selectByVisibleText(type.getValue());
    }


    @FindBy(xpath = "//div[contains(@id, 'VehicleNumber_DV-inputEl') or contains(@id, 'VehicleNumber-inputEl')]")
    private WebElement text_vehicleNumber;


    @FindBy(xpath = "//span[contains(@id, 'PAVehiclePopup:VehicleDetailsCardTab-btnInnerEl')]")
    private WebElement tab_VehicleDetails;

    public void selectVehicleDetailsTab() {
        
        clickWhenClickable(tab_VehicleDetails);
    }

    @FindBy(xpath = "//span[contains(@id, 'PAVehiclePopup:VehicleCoverageDetailsCardTab-btnEl')]")
    private WebElement tab_CoverageDetails;

    public void selectVehicleCoverageDetailsTab() {
        
        clickWhenClickable(tab_CoverageDetails);
        
    }

    @FindBy(xpath = "//span[contains(@id, 'PAVehiclePopup:VehicleExclusionsConditionsCardTab-btnInnerEl')]")
    private WebElement tab_ExclusionsConditions;

    public void clickExclusionsAndConditionsTab() {
        clickWhenClickable(tab_ExclusionsConditions);
    }

//    private String radioElement(String keyword, boolean radioValue) {
//        if (radioValue) {
//            return "//div[contains(text(), '" + keyword + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "Yes" + "']/preceding-sibling::input";
//        } else {
//            return "//div[contains(text(), '" + keyword + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "No" + "']/preceding-sibling::input";
//        }
//    }
//
//    private String checkElement(String keyword, boolean radioValue) {
//        if (radioValue) {
//            return "//div[contains(text(), '" + keyword + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "Yes" + "']/parent::td[contains(@class, 'cb-checked')]";
//        } else {
//            return "//div[contains(text(), '" + keyword + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "No" + "']/parent::td[contains(@class, 'cb-checked')]";
//        }
//    }

    // Declarations
    private StringBuilder shownClassCode = new StringBuilder();
    private StringBuilder classCode = new StringBuilder();
    private StringBuilder beginClassCode = new StringBuilder();
    private StringBuilder endClassCode = new StringBuilder();
    private StringBuilder driverClassCode = new StringBuilder();
    private StringBuilder seatingCapClassCode = new StringBuilder();
    private StringBuilder privateTransportationClassCode = new StringBuilder();
    private StringBuilder beginningprivateTransportationClassCode = new StringBuilder();
    private StringBuilder additionalClassCode = new StringBuilder();
    private StringBuilder miscellaneousClassCode = new StringBuilder();

    private StringBuilder firstDigit = new StringBuilder();
    private StringBuilder secondDigit = new StringBuilder();
    private StringBuilder thirdDigit = new StringBuilder();
    private StringBuilder fourthDigit = new StringBuilder();
    private StringBuilder fifthDigit = new StringBuilder();
    private StringBuilder firstAndSecondDigit = new StringBuilder();
    private StringBuilder secondAndThirdDigit = new StringBuilder();
    private StringBuilder thirdAndFourthDigit = new StringBuilder();

    // Buttons
    @FindBy(xpath = "//a[contains(@id, 'VehiclesListDetailPanel_tb:CreateVehicleOptionsButton')] | //span[contains(@id, ':CreateVehicleOptionsButton-btnEl')]")
    private WebElement button_createVehicle;

    @FindBy(xpath = "//a[contains(@id, 'CreateVehicleOptionsButton:CreateVehicleMenuInline-itemEl')]")
    private WebElement button_createVehicleManual;

    @FindBy(xpath = "//span[contains(@id,':CreateVehicleOptionsButton:CreateVehicleMenuPrefill-textEl')]")
    private WebElement button_createVehiclePrefill;

    @FindBy(xpath = "//div[contains(@class,'x-grid-checkcolumn')]")
    private WebElement checkbox_checkAllFromPrefillVehicles;

    @FindBy(xpath = "//span[contains(@id,'AddVehiclesCheckboxButton-btnInnerEl')]")
    private WebElement button_addVehicle;

    @FindBy(xpath = "//span[contains(@id,'PAWizardStepGroup:PAVehiclesScreen:ttlBar')]")
    private WebElement page_vehicleConfirmPage;

    @FindBy(xpath = "//a[contains(@id, 'NamedInsuredsInputSet:NamedInsuredsLV:0:OrderPrefillButton')]")
    private WebElement link_reorderPrefill;


    @FindBy(xpath = "//span[contains(@id, ':RemoveVehicleButton-btnEl')]")
    private WebElement button_RemoveVehicle;

    @FindBy(xpath = "//input[contains(@inputvalue, 'false')]")
    private List<WebElement> radio_UnderwriterQuestionsNo;

    @FindBy(xpath = "")
    private List<WebElement> radio_UnderwriterQuestions;

    @FindBy(xpath = "//a[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:CAWizardStepGroup:BAVehiclesScreen:BAVehiclesLV:0:VehicleNumber']")
    private WebElement link_FirstVehicle;

    @FindBy(xpath = "//input[contains(@id, ':0:CovTermInputSet:StringTermInput-inputEl')]")
    private WebElement editbox_DescribeExistingDamage;


    @FindBy(xpath = "//div[contains(@id, 'VehicleFBSymbol-inputEl')]")
    private WebElement text_vinSymbol;

    @FindBy(xpath = "//span[contains(@id, ':VehicleDetailsCardTab-btnEl')]")
    private WebElement link_VehicleDetailsTabCPP;

    @FindBy(xpath = "//span[contains(@id, ':CoveragesCardTab-btnEl') or contains(@id, 'VehicleCoverageDetailsCardTab-btnEl')]")
    private WebElement link_VehicleCoveragesTabCPP;

    @FindBy(xpath = "//span[contains(@id, ':AdditionalCoveragesCardTab-btnEl')]")
    private WebElement link_VehicleAdditionalCoveragesTabCPP;

    @FindBy(xpath = "//span[contains(@id, ':ExclusionsConditionsCardTab-btnEl')]")
    private WebElement link_VehicleExclusionsAndConditionsTabCPP;

    @FindBy(xpath = "//span[contains(@id, ':AdditionalInterestsTab-btnInnerEl')]")
    private WebElement link_VehicleAdditioanlInterestsTabCPP;

    @FindBy(xpath = "//span[contains(@id, ':UnderwritingInformationTab-btnEl')]")
    private WebElement link_VehicleUnderwritingQuestionsTabCPP;

    @FindBy(xpath = "//div/a[contains(text(),'Return to Vehicles')]")
    private WebElement link_ReturnToVehicles;

    @FindBy(xpath = "//div[contains(@id,'BAVehiclePopup:VehicleScreen:BAVehicleCV:VehicleDV:ClassCode-inputEl')]")
    private WebElement text_ClassCode;

    @FindBy(xpath = "//div[contains(@id,':CAWizardStepGroup:BAVehiclesScreen:Fleet-inputEl')]")
    private WebElement text_Fleet;

    @FindBy(xpath = "//div[contains(@class,'message')]")
    private List<WebElement> validationMessages;


    @FindBy(xpath = "//div[contains(@id, 'BAVehiclePopup:VehicleScreen:BAVehicleCV:VehicleDV:Vin-inputEl')]")
    private WebElement text_UneditableTrailerVIN;


    // Table
    @FindBy(xpath = "//div[contains(@id,'BAVehiclesScreen:BAVehiclesLV-body')]/div/table")
    private WebElement table_SubmissionVehicleDetails;

    @FindBy(xpath = "//div[contains(@id,':PAVehiclesScreen:PAVehiclesPanelSet:VehiclesListDetailPanel:VehiclesLV')]/div[contains(@id,'-body')]/parent::div")
    private WebElement table_SubmissionVehicleDetailsForUtils;

    @FindBy(xpath = "//div[contains(text(),'Removal Of Property Damage Coverage IDCA 31 3006')]/ancestor::fieldset")
    private WebElement table_RemovalOfPropertyDamageCoverage_IDCA_31_3006;

    @FindBy(xpath = "//div[contains(@id, ':VehiclesLV') or contains(@id, ':BAVehiclesLV')]")
    private WebElement table_Vehicles;

//    private Guidewire8Select select_BodyType() {
//        return new Guidewire8Select(driver, "//table[contains(@id, 'VehicleDV:BodyTypeTrailer-triggerWrap')]");
//    }
//
//    private Guidewire8Select select_SizeClass() {
//        return new Guidewire8Select(driver, "//table[contains(@id, 'BAVehicleCV:VehicleDV:SizeClass-triggerWrap')]");
//    }
//
//    private Guidewire8Select select_LicenseState() {
//        return new Guidewire8Select(driver, "//table[contains(@id, 'LicenseState-triggerWrap')]");
//    }
//
//    private Guidewire8Select select_Radius() {
//        return new Guidewire8Select(driver, "//table[contains(@id, 'BAVehicleCV:VehicleDV:Radius-triggerWrap')]");
//    }
//
//    private Guidewire8Select select_LongDistanceOptions() {
//        return new Guidewire8Select(driver, "//table[contains(@id, 'BAVehicleCV:VehicleDV:LongDistanceOptions-triggerWrap')]");
//    }
//
//    private Guidewire8Select select_BusinessUseClass() {
//        return new Guidewire8Select(driver, "//table[contains(@id, 'BAVehicleCV:VehicleDV:BusinessUseClass-triggerWrap')]");
//    }
//
//    private Guidewire8Select select_SecondaryClassType() {
//        return new Guidewire8Select(driver, "//table[contains(@id, 'BAVehicleCV:VehicleDV:SecondaryClassType-triggerWrap')]");
//    }
//
//    private Guidewire8Select select_SecondaryClass() {
//        return new Guidewire8Select(driver, "//table[contains(@id, 'BAVehicleCV:VehicleDV:SecondaryClass-triggerWrap')]");
//    }
//
//    private Guidewire8Select select_SeatingCapacity() {
//        return new Guidewire8Select(driver, "//table[contains(@id, 'BAVehicleCV:VehicleDV:SeatingCapacity-triggerWrap')]");
//    }


    @FindBy(xpath = "//div[contains(text(), 'How many times a year does applicant/insured travel over 300 miles')]/parent::td/following-sibling::td/div")
    private WebElement select_TimesDrivenOver300MilesPerYear;

    Guidewire8Select select_HowManyTimesAYearDoesApplicantTravelOver300Miles() {
        return new Guidewire8Select(driver, "//div[contains(@id,'BAVehiclePopup:VehicleScreen:BAVehicleCV:QuestionSetsDV:0:QuestionSetLV-body') or contains(@id, 'BAVehiclePopup:VehicleScreen:BAVehicleCV:QuestionSetsDV:0:QuestionSetTabbedLV-body')]/following-sibling::div/table");
    }


    // radio buttons
//    private Guidewire8RadioButton radio_LeasedOrRentedToOthers() {
//        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'VehicleDV:LeaseOrRent')]");
//    }
//
//
//    private Guidewire8RadioButton radio_EquippedWithAMechanicalLift() {
//        return new Guidewire8RadioButton(driver, "//table[contains(@id,':BAVehicleCV:VehicleDV:MechanicalLift')]");
//    }


    private Guidewire8Checkbox checkbox_LoanLeaseGapCoverage_CA20_71() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Auto Loan/Lease Gap Coverage CA 20 71')]/preceding-sibling::table");
    }

    private Guidewire8Checkbox checkbox_AudioVisualAndDataElectronicEquipmentCoverageAddedLimits() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Audio, Visual And Data Electronic Equipment Coverage Added Limits')]/preceding-sibling::table");
    }


    //Messages
    @FindBy(xpath = "//div[contains(@id, 'PAVehiclePopup:_msgs')]")
    private WebElement vehiclePageValidations;


    @FindBy(xpath = "//div[contains(@id, ':BAVehiclesScreen:Fleet-inputEl')]")
    private WebElement text_FleetValidation;


    private Guidewire8Checkbox checkbox_SpecifiedCauseOfLoss() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Specified Causes of Loss')]/preceding-sibling::table");
    }

    public void setSpecifiedCauseOfLoss(boolean checked) {
        checkbox_SpecifiedCauseOfLoss().select(checked);
        
    }

//    private Guidewire8Select select_SpecifiedCauseOfLoss() {
//        return new Guidewire8Select(driver, "//div[contains(text(), 'Specified Causes of Loss')]/ancestor::legend/following-sibling::div/span/div/child::table/tbody/child::tr/child::td/label[contains(text(), 'Specified Cause of Loss')]/parent::td/following-sibling::td/table");
//    }

//    private void selectSpecifiedCauseOfLoss(SpecifiedCauseOfLoss causeOfLoss) {
//        Guidewire8Select mySelect = select_SpecifiedCauseOfLoss();
//        mySelect.selectByVisibleText(causeOfLoss.getValue());
//    }

//    private Guidewire8Select select_SpecifiedCauseOfLossLimit() {
//        return new Guidewire8Select(driver, "//div[contains(text(), 'Specified Causes of Loss')]/ancestor::legend/following-sibling::div/span/div/child::table/tbody/child::tr/child::td/label[contains(text(), 'Deductible')]/parent::td/following-sibling::td/table");
//    }

//    private void selectSpecifiedCauseOfLossLimit(SpecifiedCauseOfLossDeductible causeOfLossDeductible) {
//        Guidewire8Select mySelect = select_SpecifiedCauseOfLossLimit();
//        mySelect.selectByVisibleText(causeOfLossDeductible.getValue());
//    }

    @FindBy(xpath = "//div[contains(text(), 'Mobile Homes Contents Coverage CA 20 16')]/ancestor::legend/following-sibling::div/span/div/table/tbody/child::tr[1]/child::td[2]/input")
    private WebElement editbox_MobileHomeContentsCoverageCA2016;

    private Guidewire8Select select_MobileContentsSpecifiedCauseOfLoss() {
        return new Guidewire8Select(driver, "//label[contains(text() , 'Specified Cause of Loss')]/parent::td/following-sibling::td/table");
    }

    public void setMobileHomeContentsCoverageCA2016Limit(String limit) {
        clickWhenClickable(editbox_MobileHomeContentsCoverageCA2016);
        editbox_MobileHomeContentsCoverageCA2016.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_MobileHomeContentsCoverageCA2016.sendKeys(limit);
        
    }

    private void setMobileSpecifiedCauseOfLossCA2016(SpecifiedCauseOfLoss causeOfLoss) {
        Guidewire8Select mySelect = select_MobileContentsSpecifiedCauseOfLoss();
        mySelect.selectByVisibleText(causeOfLoss.getValue());
        
    }

    private Guidewire8Checkbox checkbox_AdditionalNamedInsuredForDesignatedPersonOrOrganizationIDCA313009() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Additional Named Insured For Designated Person Or Organization IDCA 31 3009')]/preceding-sibling::table");
    }

    private void setAdditionalNamedInsuredForDesignatedPersonOrOrganizationIDCA313009(boolean checked) {
        checkbox_AdditionalNamedInsuredForDesignatedPersonOrOrganizationIDCA313009().select(checked);
    }

    @FindBy(xpath = "//div[contains(text() , 'Additional Named Insured For Designated Person Or Organization IDCA 31 3009')]/ancestor::legend/following-sibling::div")
    private WebElement table_AdditonalNamedInsuredForDesignatedPerson;

    private void addNameOnVehicleRegistration(String name) {
        clickAdd();
        
        clickWhenClickable(find(By.xpath("//span[contains(text(), 'Name On Vehicle Registration')]/parent::div/parent::div/parent::div/parent::div/parent::div/following-sibling::div/descendant::tr/child::td[2]/div")));
        setText(find(By.xpath("//textarea[contains(@name, 'c1')]")), "Little Bunny Foo Foo");
    }

    private Guidewire8Checkbox checkbox_RoadsideAssistanceIDCA313008() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Roadside Assistance IDCA 31 3008')]/preceding-sibling::table");
    }

    private boolean setRoadsideAssistanceIDCA313008(boolean checked) {
        String coverage = "Roadside Assistance IDCA 31 3008";

        if (gwh.isElectable(coverage) || gwh.isSuggested(coverage)) {
            checkbox_RoadsideAssistanceIDCA313008().select(checked);
            return true;
        } else if (gwh.isRequired(coverage)) {
            return false;
        }
        Assert.fail(coverage + " was NOT available when expected.");
        return false;
    }

    private Guidewire8Checkbox checkbox_MotorCarriers_InsuranceForNon_TruckingUseCA2309() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Motor Carriers - Insurance For Non-Trucking Use CA 23 09')]/preceding-sibling::table");
    }

    private void setMotorCarriers_InsuranceForNon_TruckingUseCA2309(boolean checked) {
        checkbox_MotorCarriers_InsuranceForNon_TruckingUseCA2309().select(checked);
    }

    @FindBy(xpath = "//div[contains(text(), 'Audio, Visual And Data Electronic Equipment Coverage Added Limits CA 99 60')]/ancestor::legend/following-sibling::div/span/div/table/tbody/child::tr/child::td/label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
    private WebElement audiovisualEquipmentLimit;

    @FindBy(xpath = "//div[contains(text(), 'Audio, Visual And Data Electronic Equipment Coverage Added Limits CA 99 60')]/ancestor::legend/following-sibling::div/span/div/child::table/tbody/child::tr/child::td/label[contains(text(), 'Type of equipment')]/parent::td/following-sibling::td/textarea")
    private WebElement audiovisualEquipmentTypeDescription;

    ////UW QUESTIONS
    public boolean clickUWQuestionIsTheVehicleUsedForTowing(boolean radioValue) {
        tableUtils.setRadioByText("Is the vehicle being used for towing", radioValue);
        return radioValue;
    }

    public void clickUWQuestionIsTheVehicleUsedForPoliceRotation(boolean radioValue) {
        tableUtils.setRadioByText("Is the vehicle used for Police Rotation", radioValue);
    }

    public void clickUWQuestionDoTheyRepossessAutos(boolean radioValue) {
        tableUtils.setRadioByText("Do they repossess autos?", radioValue);
    }

    public void clickUWQuestionDoTheyAdvertiseAsATowingCompany(boolean radioValue) {
        tableUtils.setRadioByText("Do they advertise as a towing company?", radioValue);
    }

    //Is the vehicle being driven more than a 300 mile radius?
    public void clickUWQuestionVehicleBeingDrivenMoreThan300MileRadius(boolean radioValue) {
        tableUtils.setRadioByText("Is the vehicle being driven more than a 300 mile radius", radioValue);
    }

    private void clickUWQuestionMotorHomeLivingQuarters(boolean radioValue) {
        tableUtils.setRadioByText("Does Motor Home have living quarters", radioValue);
    }

    private void clickUWQuestionMotorHomeContentsCoverage(boolean radioValue) {
        tableUtils.setRadioByText("Is contents coverage desired", radioValue);
    }

    private void clickUWQuestionUsedToTransportMigrantWorkers(boolean radioValue) {
        tableUtils.setRadioByText("Is bus used to transport seasonal or migrant workers", radioValue);
    }

    //UW Questions For Collision & Comprehensive Coverage
    private void clickUWQuestionHasExistingDamage(boolean radioValue) {
        tableUtils.setRadioByText("Does the vehicle have any existing damage", radioValue);
    }

    @FindBy(xpath = "//div[contains(text(), 'Please describe the existing damage')]/parent::td/following-sibling::td/div")
    private WebElement editbox_ExistingDamageDescDIV;

    @FindBy(xpath = "//div[contains(text(), 'Please describe the existing damage')]/parent::td/ancestor::table/parent::div/parent::div/following-sibling::div/table/tbody/child::tr/child::td/textarea[contains(@name, 'c2')]")
    private WebElement editbox_ExistingDamageDescINPUT;


    // BAVehiclePopup:VehicleScreen:BAVehicleCV:BAGarageLocationInputSet:territoryCode-inputEl
    @FindBy(xpath = "//div[contains(@id,':territoryCode-inputEl')]")
    private WebElement text_TerritoryCode;

    @FindBy(xpath = "//span[contains(text(), 'Vehicle #')]/parent::div/parent::div/preceding-sibling::div[2]/div/span/div[contains(@class,'checkcolumn')]")
    private WebElement selectAll;

    public void selectAll() {
        clickWhenClickable(selectAll);
    }


    public void selectVehicleByDriver(String driver) {
        tableUtils.setCheckboxInTableByText(table_Vehicles, driver, true);
    }


    public void editVehicleByType(VehicleType type) {
        int foo = tableUtils.getRowNumberInTableByText(table_Vehicles, type.getValue());
        
        tableUtils.clickLinkInSpecficRowInTable(table_Vehicles, foo);
    }

    public void editVehicleByType(VehicleTypePL type) {
        int foo = tableUtils.getRowNumberInTableByText(table_Vehicles, type.getValue());
        
        tableUtils.clickLinkInSpecficRowInTable(table_Vehicles, foo);
    }

    public void editVehicleByRowNumber(int rowNumber) {
        
        tableUtils.clickLinkInSpecficRowInTable(table_Vehicles, rowNumber);
        
    }

    public void editVehicleByVehicleNumber(int vehicleNumber) {
        
        tableUtils.clickLinkInSpecficRowInTable(table_Vehicles, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_Vehicles, "Vehicle #", String.valueOf(vehicleNumber))));
        
    }

    public void editVehicleByVin(String vin) {
        
        tableUtils.clickLinkInSpecficRowInTable(table_Vehicles, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_Vehicles, "VIN", vin)));
        
    }

    @FindBy(xpath = "//div[contains(@id, 'AddVehicleFromPrefillPopup:AddVehicleFromPrefillScreen:VehiclePrefillLV')]")
    private WebElement VehicleFromPreFillTable;


    public int addFromPrefillVehicles() {
        clickWhenClickable(button_createVehicle);
        clickWhenClickable(button_createVehiclePrefill);
        
        int rowCount = tableUtils.getRowCount(VehicleFromPreFillTable);
        clickWhenClickable(checkbox_checkAllFromPrefillVehicles);
        clickWhenClickable(button_addVehicle);

        return rowCount;
    }


    public void selectFirstVehicleFromPrefillVehicles() {
        clickWhenClickable(button_createVehicle);
        clickWhenClickable(button_createVehiclePrefill);
        
        tableUtils.setCheckboxInTable(VehicleFromPreFillTable, 1, true);
        clickWhenClickable(button_addVehicle);
    }


    public int getNumVehiclesAdded() {
        return tableUtils.getRowCount(table_SubmissionVehicleDetailsForUtils);
    }


    public List<WebElement> getValidationMessages() {
        return validationMessages;
    }


    public void clickOK() {
        super.clickOK();
        
    }


    public void clickCancel() {
        super.clickCancel();
    }


    public String getTerritoryCode() {
        return text_TerritoryCode.getText();
    }


    public void clickVehicleDetailsTab() {
        clickWhenClickable(link_VehicleDetailsTabCPP);
        
    }


    public void clickVehicleCoveragesTab() {
        clickWhenClickable(link_VehicleCoveragesTabCPP);
        
    }


    public void clickVehicleAdditionalCoveragesTab() {
        clickWhenClickable(link_VehicleAdditionalCoveragesTabCPP);
        
    }


    public void clickVehicleExclusionsAndConditionsTab() {
        clickWhenClickable(link_VehicleExclusionsAndConditionsTabCPP);
        
    }


    public void clickVehicleAdditionalInterestsTab() {
        clickWhenClickable(link_VehicleAdditioanlInterestsTabCPP);
        
    }


    public void clickVehicleUnderwritingQuestionsTab() {
        clickWhenClickable(link_VehicleUnderwritingQuestionsTabCPP);
        
    }


    public void createVehicleManually() throws GuidewireNavigationException {
		
        clickWhenClickable(button_createVehicle);
        clickWhenClickable(button_createVehicleManual);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@class, 'g-title') and ((text()='Vehicle Information') or text()='Edit Vehicle')]", 5000, "UNABLE TO GET TO CREATE VEHICLE PAGE!");
    }

    public void removeVehicleByRowNumber(int rowNumber) {
        tableUtils.setCheckboxInTable(table_Vehicles, rowNumber, true);
        
        clickRemoveVehicle();
    }

    public void removeVehicleByVehicleNumber(int vehicleNumber) {
        tableUtils.setCheckboxInTable(table_Vehicles, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_Vehicles, "Vehicle #", String.valueOf(vehicleNumber))), true);
        
        clickRemoveVehicle();
    }


    public void removeAllVehicles() {
        tableUtils.setTableTitleCheckAllCheckbox(table_Vehicles, true);
        
        clickRemoveVehicle();
    }

    public void clickRemoveVehicle() {
        clickWhenClickable(button_RemoveVehicle);
        if (checkIfElementExists("//div[contains(@id,'messagebox-1001-display')]", 2000)) {
            selectOKOrCancelFromPopup(OkCancel.OK);
        }
    }

//    private void selectSizeClass(SizeClass sizeClass) {
//        Guidewire8Select mySelect = select_SizeClass();
//        mySelect.selectByVisibleText(sizeClass.getValue());
//    }
//
//    private void selectBodyType(BodyType bodyType) {
//        Guidewire8Select mySelect = select_BodyType();
//        mySelect.selectByVisibleText(bodyType.getValue());
//    }
//
//    private void selectRadius(Radius radius) {
//        Guidewire8Select mySelect = select_Radius();
//        mySelect.selectByVisibleText(radius.getValue());
//        
//    }
//
//    private void selectLongDistanceOptions(LongDistanceOptions option) {
//        Guidewire8Select mySelect = select_LongDistanceOptions();
//        mySelect.selectByVisibleText(option.getValue());
//        
//    }
//
//    private void selectBusinessUseClass(BusinessUseClass businessUseClass) {
//        Guidewire8Select mySelect = select_BusinessUseClass();
//        mySelect.selectByVisibleText(businessUseClass.getValue());
//    }
//
//    private void selectSecondaryClassType(SecondaryClassType secondaryClassType) {
//        Guidewire8Select mySelect = select_SecondaryClassType();
//        mySelect.selectByVisibleText(secondaryClassType.getValue());
//    }
//
//    private void selectSecondaryClass(SecondaryClass secondaryClass) {
//        Guidewire8Select mySelect = select_SecondaryClass();
//        mySelect.selectByVisibleText(secondaryClass.getValue());
//    }
//
//    private void selectSeatingCapacity(SeatingCapacity seatingCapacity) {
//        Guidewire8Select mySelect = select_SeatingCapacity();
//        mySelect.selectByVisibleText(seatingCapacity.getValue());
//    }
//
//    private void selectLicenseState(State state) {
//        Guidewire8Select mySelect = select_LicenseState();
//        mySelect.selectByVisibleText(state.getName());
//    }
//
//    private void setRadioLeasedToOthers(boolean yesNo) {
//        radio_LeasedOrRentedToOthers().select(yesNo);
//    }
//
//
//    private void setRadioEquippedWithAMechanicalLift(boolean yesNo) {
//        radio_EquippedWithAMechanicalLift().select(yesNo);
//    }


//	public void addVehicle(Vehicle vehicle) throws Exception {
//		ArrayList<Vehicle> vehicleList = new ArrayList<>();
//		vehicleList.add(vehicle);
//		addVehicles(vehicleList);
//	}


//	public void updatePAVehiclesFA(GeneratePolicy policy) {
//		for (Vehicle vehicle : policy.squire.squirePA.getVehicleList()) {
//			editVehicleByVehicleNumber(vehicle.getVehicleNumber());
//
//			if (Objects.equals(vehicle.getVin(), "make new vin")) {
//				setRandomVin();
//			} else {
//				setVIN(vehicle.getVin());
//			}
//
//			
//			if (!isVinValid()) {
//				policy.hasUWIssue = true;
//			}
//			vinAutoFill(vehicle);
//			VehicleTypePL vehicleType = vehicle.getVehicleTypePL();
//
//			// odometer
//			if (vehicleType == VehicleTypePL.FarmTruck || vehicleType == VehicleTypePL.ShowCar) {
//				setOdometer(vehicle.getOdometer());
//			}
//
//			// trailer type
//			if (vehicleType == VehicleTypePL.Trailer) {
//				selectTrailer(vehicle.getTrailerType());
//			}
//
//			clickOK();
//			
//
//		}
//	}


    private void checkLoanLeaseGapCoverage_CA_20_71(boolean checked) {
        checkbox_LoanLeaseGapCoverage_CA20_71().select(checked);
        
    }


    public void checkAudioVisualAndDataElectronicEquipmentCoverageAddedLimits(boolean checked) {
        checkbox_AudioVisualAndDataElectronicEquipmentCoverageAddedLimits().select(checked);
        
    }

    private void setAudioVisualDataLimit(int limit) {
        clickWhenClickable(audiovisualEquipmentLimit);
        audiovisualEquipmentLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        audiovisualEquipmentLimit.sendKeys(String.valueOf(limit));
        
    }


    private void setAudioVisualDataTypeDesc(String desc) {
        clickWhenClickable(audiovisualEquipmentTypeDescription);
        
        audiovisualEquipmentTypeDescription.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        audiovisualEquipmentTypeDescription.sendKeys(desc);
        
    }

    private void selectLocationOfDamage(Vehicle vehicle) {
        if (vehicle.getLocationOfDamage().equals("")) {
            vehicle.setLocationOfDamage(LocationOfDamageCPP.random().getValue());
        }
        tableUtils.setValueForCellInsideTable(table_RemovalOfPropertyDamageCoverage_IDCA_31_3006, tableUtils.getNextAvailableLineInTable(table_RemovalOfPropertyDamageCoverage_IDCA_31_3006, "Location Of Damage"), "Location Of Damage", "c1", vehicle.getLocationOfDamage());
        
    }

    private void selectDamagedItem(Vehicle vehicle) {
        if (vehicle.getDamageItem().equals("")) {
            switch (vehicle.getLocationOfDamage()) {
                case "Front":
                    vehicle.setDamageItem(FrontDamagedItemCPP.random().getValue());
                    break;
                case "Right Front":
                    vehicle.setDamageItem(RightFrontDamagedItemCPP.random().getValue());
                    break;
                case "Left Front":
                    vehicle.setDamageItem(LeftFrontDamagedItemCPP.random().getValue());
                    break;
                case "Right Front Pillar":
                    vehicle.setDamageItem(RightFrontPillarDamagedItemCPP.random().getValue());
                    break;
                case "Left Front Pillar":
                    vehicle.setDamageItem(LeftFrontPillarDamagedItemCPP.random().getValue());
                    break;
                case "Right T Bone":
                    vehicle.setDamageItem(RightTBoneDamagedItemCPP.random().getValue());
                    break;
                case "Left T Bone":
                    vehicle.setDamageItem(LeftTBoneDamagedItemCPP.random().getValue());
                    break;
                case "Right Quarter Post":
                    vehicle.setDamageItem(RightQuarterPostDamagedItemCPP.random().getValue());
                    break;
                case "Left Quarter Post":
                    vehicle.setDamageItem(LeftQuarterPostDamagedItemCPP.random().getValue());
                    break;
                case "Right Rear":
                    vehicle.setDamageItem(RightRearDamagedItemCPP.random().getValue());
                    break;
                case "Left Rear":
                    vehicle.setDamageItem(LeftRearDamagedItemCPP.random().getValue());
                    break;
                case "Rear":
                    vehicle.setDamageItem(RearDamagedItemCPP.random().getValue());
                    break;
                case "Hood Roof Trunklid":
                    vehicle.setDamageItem(HoodRoofTrunklidDamagedItemCPP.random().getValue());
                    break;
                case "Undercarriage":
                    vehicle.setDamageItem(UndercarriageDamagedItemCPP.random().getValue());
                    break;
                case "Glass":
                    vehicle.setDamageItem(GlassDamagedItemCPP.random().getValue());
                    break;
                case "Other":
                    vehicle.setDamageItem(OtherDamagedItemCPP.random().getValue());
                    break;
            }
        }
        tableUtils.setValueForCellInsideTable(table_RemovalOfPropertyDamageCoverage_IDCA_31_3006, tableUtils.getNextAvailableLineInTable(table_RemovalOfPropertyDamageCoverage_IDCA_31_3006, "Damaged Item"), "Damaged Item", "c2", vehicle.getDamageItem());
        
    }


    private void selectDamageDescription(Vehicle vehicle) {
        if (vehicle.getDamageDescription().equals("")) {
            vehicle.setDamageDescription(DamageDescriptionCPP.random().getValue());
        }
        tableUtils.setValueForCellInsideTable(table_RemovalOfPropertyDamageCoverage_IDCA_31_3006, tableUtils.getNextAvailableLineInTable(table_RemovalOfPropertyDamageCoverage_IDCA_31_3006, "Damage Description"), "Damage Description", "c3", vehicle.getDamageDescription());
        
    }


    private void setExplanationOfDamage(Vehicle vehicle) {
        if (vehicle.getDamageDescription().equalsIgnoreCase("Other Please Describe") || vehicle.getDamageItem().contains("Other")) {
            tableUtils.setValueForCellInsideTable(table_RemovalOfPropertyDamageCoverage_IDCA_31_3006, tableUtils.getNextAvailableLineInTable(table_RemovalOfPropertyDamageCoverage_IDCA_31_3006, "Explanation Of Damage"), "Explanation Of Damage", "c4", vehicle.getExplanationOfDamage());
            
        }
    }


    private void addRightTBoneDamagedItemCPP(RightTBoneDamagedItemCPP rightTBoneDamagedItemCPP) {
        super.clickAdd();
        
        tableUtils.setValueForCellInsideTable(table_RemovalOfPropertyDamageCoverage_IDCA_31_3006, 1, "Damaged Item", "c2", rightTBoneDamagedItemCPP.getValue());
    }

    private void addDamageDescriptionCPP(DamageDescriptionCPP damageDescriptionCPP) {
        //		super.clickAdd();
        
        tableUtils.setValueForCellInsideTable(table_RemovalOfPropertyDamageCoverage_IDCA_31_3006, 1, "Damage Description", "c3", damageDescriptionCPP.getValue());
    }

    public void clickDoesApplicantInsuredHaulSandOrGravelForOthers(boolean radioValue) {
        tableUtils.setRadioByText("Does Applicant/insured haul sand or gravel for others?", radioValue);

    }


    public void clickDoesApplicantInsuredBackHaulOrTransportGoodsWhileNotOperatingUnderTheAuthorityOfAnotherCarrier(boolean radioValue) {
        tableUtils.setRadioByText("Does applicant/insured back haul or transport goods while not operating under the authority of another carrier", radioValue);

    }


    public void clickDoesThePartyWhoseAuthorityUnderWhichTheApplicantInsuredHaveCoverageForTheTrucksAndTrailersShownOnThePolicy(boolean radioValue) {
        tableUtils.setRadioByText("Does the party whose authority under which the applicant/insured have coverage for the trucks and trailers shown on the policy", radioValue);

    }


    public void clickDoesTheApplicantInsuredLeaseOrOperateTheirVehicleUnderAnotherPartiesPUCAuthority(boolean radioValue) {
        tableUtils.setRadioByText("Does the applicant/insured lease or operate their vehicle under another parties PUC authority", radioValue);
    }


    public void clickDoesTheApplicantInsuredLeaseTheVehicleWithoutADriver(boolean radioValue) {
        tableUtils.setRadioByText("Does the applicant/insured lease the vehicle without a driver", radioValue);

    }


    public void clickIsBusUsedToTransportSeasonalOrMigrantWorkers(boolean radioValue) {
        tableUtils.setRadioByText("Is bus used to transport seasonal or migrant workers", radioValue);
    }


    public void selectHowManyTimesAYearDoesApplicantTravelOver300Miles(HowManyTimesAYearDoesApplicantTravelOver300Miles timesDrivenPerYear) {
        clickWhenClickable(select_TimesDrivenOver300MilesPerYear);
        
        finds(By.xpath("//div[contains(text(), 'How many times a year does applicant/insured travel over 300 miles?')]/parent::td/following-sibling::td/div"));
        
        
        find(By.xpath("//input[contains(@name, 'c2')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
        find(By.xpath("//input[contains(@name, 'c2')]")).sendKeys(timesDrivenPerYear.getValue());
        
        find(By.xpath("//div[contains(text(), 'How many times a year does applicant/insured travel over 300 miles?')]")).click();

    }


    // METHOD USED BY GENERATE TO FILLOUT PAGE for CPP

    public void fillOutVehiclesCPP(GeneratePolicy policy) throws Exception {

        for (Vehicle vehicle : policy.commercialAutoCPP.getVehicleList()) {
            boolean isUnderwriter = false;
            //LAME thing on some coverages are only able to be added by UW. so instead of creating vehicle and going back in as UW UW will just create the vehicle
            if (vehicle.getCaVehicleAdditionalCoveragesList().contains(CAVehicleAdditionalCoverages.RoadsideAssistanceIDCA313008)) {
                isUnderwriter = logInAsUnderwriter();
            }//end if underwriter

            if (gwh.getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
                printVehicle(vehicle);
                if (vehicle.getGaragedAt() == null) {
                    vehicle.setGaragedAt(policy.pniContact.getAddress());
                }
                fillOutVehicleDetailsCPP(vehicle);
                fillOutVehiclesCoveragesCPP(vehicle);
                fillOutAdditionalInterests(policy.basicSearch, vehicle);
                fillOutVehiclesAdditionalCoverages(policy.basicSearch, vehicle);
                fillOutVehiclesExclusionsAndConditions(vehicle);
                fillOutVehiclesUnderwritingQuestions(true, vehicle);
                super.clickOK();
                
                //get vehicle number
                super.clickGenericWorkorderSaveDraft();
            } else {
                //FULL APP
                
                editVehicleByVin(vehicle.getVin());
                
                fillOutVehiclesUnderwritingQuestions(false, vehicle);
                clickOK();
                
            }//end else

            if (isUnderwriter) {
                logInAsAgent();
            }

        }//end for
    }//end fillOutVehiclesCPP

    private void printVehicle(Vehicle vehicle) {
        systemOut("Adding Vehicle: \n");
        systemOut(vehicle.getVehicleName());
    }


    private boolean logInAsUnderwriter() {
        InfoBar infoBarStuff = new InfoBar(driver);
        try {
            agent = infoBarStuff.getAgent();
            underwriter = infoBarStuff.getUnderwriter();
            accountNumber = infoBarStuff.getInfoBarAccountNumber();
        } catch (Exception e) {
            Assert.fail("Vehicle need RoadSide Assistance added but failed get the Agent from the Info Bar");
        }
        if (agent != null && underwriter != null) {
            repository.pc.workorders.generic.GenericWorkorder genwo = new GenericWorkorder(driver);
            repository.pc.sidemenu.SideMenuPC sidemenu = new repository.pc.sidemenu.SideMenuPC(driver);
            genwo.clickGenericWorkorderSaveDraft();
            
            new GuidewireHelpers(driver).logout();
            new Login(driver).loginAndSearchSubmission(underwriter.getUnderwriterUserName(), underwriter.getUnderwriterPassword(), accountNumber);
            sidemenu.clickSideMenuCAVehicles();
        } else {
            Assert.fail("Agent or Underwriter was NULL");
        }//end else

        return true;
    }

    private void logInAsAgent() {
        Login login = new Login(driver);
        new GuidewireHelpers(driver).logout();
        login.loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), accountNumber);
        repository.pc.sidemenu.SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuCAVehicles();
    }


    public void createNewVehicleCPP(boolean basicSearch, Vehicle vehicle) throws Exception {
        boolean isUnderwriter = false;
        //LAME thing on some coverages are only able to be added by UW. so instead of creating vehicle and going back in as UW UW will just create the vehicle
        if (vehicle.getCaVehicleAdditionalCoveragesList().contains(CAVehicleAdditionalCoverages.RoadsideAssistanceIDCA313008)) {
            isUnderwriter = logInAsUnderwriter();
        }//end if underwriter
        fillOutVehicleDetailsCPP(vehicle);
        fillOutVehiclesCoveragesCPP(vehicle);
        fillOutAdditionalInterests(basicSearch, vehicle);
        fillOutVehiclesAdditionalCoverages(basicSearch, vehicle);
        fillOutVehiclesExclusionsAndConditions(vehicle);
        fillOutVehiclesUnderwritingQuestions(false, vehicle);
        clickOK();
        if (isUnderwriter) {
            logInAsAgent();
        }
    }


    public void fillOutVehicleDetailsCPP(Vehicle vehicle) {

//		createVehicleManually();
//		
//		selectGaragedAt(vehicle.getGaragedAt());
//		
//		selectVehicleType(vehicle.getVehicleType());
//		
//		setRadioLeasedToOthers(vehicle.isLeasedToOthers());
//		
//		vehicle.setVehicleNumber(Integer.parseInt(text_vehicleNumber.getText()));
//		
//
//
//		switch (vehicle.getVehicleType()) {
//		case Trucks:
//		case TruckTractors:
//			selectBusinessUseClass(vehicle.getBusinessUseClass());
//			
//		case Trailer:
//			selectRadius(vehicle.getRadius());
//			if (vehicle.getRadius().equals(Radius.LongDistancesOver301Miles)) {
//				selectLongDistanceOptions(vehicle.getLongDistanceOptions());
//			}
//			
//			selectSizeClass(vehicle.getSizeClass());
//			
//			selectSecondaryClassType(vehicle.getSecondaryClassType());
//			
//			selectSecondaryClass(vehicle.getSecondaryClass());
//			
//
//		case PrivatePassenger:
//			setVIN(vehicle.getVin());
//			
//			vinAutoFill(vehicle);
//			
//			selectLicenseState(vehicle.getLicencedState());
//			
//			break;
//		case PublicTransportation:
//			setVIN(vehicle.getVin());
//			
//			vinAutoFill(vehicle);
//			
//			selectBodyType(vehicle.getBodyType());
//			
//			selectLicenseState(vehicle.getLicencedState());
//			
//
//
//			selectRadius(vehicle.getRadius());
//			if (vehicle.getRadius().equals(Radius.LongDistancesOver301Miles)) {
//				selectLongDistanceOptions(vehicle.getLongDistanceOptions());
//			}
//			
//			selectSeatingCapacity(vehicle.getSeatingCapacity());
//			
//			setRadioEquippedWithAMechanicalLift(vehicle.hasEquippedWithAMechanicalLift());
//			
//			break;
//		case Miscellaneous:
//			setVIN(vehicle.getVin());
//			
//			vinAutoFill(vehicle);
//			
//			selectBodyType(vehicle.getBodyType());
//			
//			selectLicenseState(vehicle.getLicencedState());
//			
//			if (vehicle.getBodyType().equals(BodyType.MotorHomesMoreThan22Feet) || vehicle.getBodyType().equals(BodyType.MotorHomesUpTo22Feet) || vehicle.getBodyType().equals(BodyType.TrailerEquippedAsLivingQuarters)) {
//				
//				selectSizeClass(vehicle.getSizeClass());
//				
//				selectRadius(vehicle.getRadius());
//				
//
//				//				selectSecondaryClassType(vehicle.getSecondaryClassType());
//				//				
//				//				selectSecondaryClass(vehicle.getSecondaryClass());
//				//				
//				//				selectBusinessUseClass(vehicle.getBusinessUseClass());
//				//				
//			}
//			break;
//		}

    }


    public void fillOutVehiclesCoveragesCPP(Vehicle vehicle) {
//		clickVehicleCoveragesTab();
//		
//		// Collision
//		if (setCollision(vehicle.hasCollision())) {
//			
//			selectCollisionDeductible(vehicle.getCollisionDeductible());
//		}
//		
//		// Comprehensive
//		setComprehensive(vehicle.hasComprehensive());
//		if (vehicle.hasComprehensive()) {
//			
//			selectComprehensiveDeductible(vehicle.getComprehensiveDeductible());
//		}
//
//		if (!vehicle.getVehicleType().equals(VehicleType.PrivatePassenger)) {
//			//specified cause of loss
//			if (!vehicle.hasComprehensive()) {
//				setSpecifiedCauseOfLoss(vehicle.hasSpecifiedCauseofLoss());
//				if (vehicle.hasSpecifiedCauseofLoss()) {
//					selectSpecifiedCauseOfLoss(vehicle.getSpecifiedCauseOfLoss());
//					
//					selectSpecifiedCauseOfLossLimit(vehicle.getSpecifiedCauseOfLossDeductible());
//				}
//			}
//		}
    }


    public void fillOutVehiclesAdditionalCoverages(boolean basicSearch, Vehicle vehicle) throws Exception {
        clickVehicleAdditionalCoveragesTab();
        for (CAVehicleAdditionalCoverages coverage : vehicle.getCaVehicleAdditionalCoveragesList()) {
            switch (coverage) {
                case AudioVisualDataElectronicEquipmentCoverageAddedLimitsCA9960:
                    checkAudioVisualAndDataElectronicEquipmentCoverageAddedLimits(true);
                    
                    setAudioVisualDataLimit(vehicle.getAudioVisualDataLimit());
                    
                    setAudioVisualDataTypeDesc(vehicle.getAudioVisualDataTypeOfEquipment());
                    break;
                case AdditionalNamedInsuredForDesignatedPersonOrOrganizationIDCA313009:
                    setAdditionalNamedInsuredForDesignatedPersonOrOrganizationIDCA313009(true);
                    addNameOnVehicleRegistration("Little Bunny Foo Foo");
                    break;
                case MotorCarriersInsuranceForNonTruckingUseCA2309:
                    setMotorCarriers_InsuranceForNon_TruckingUseCA2309(true);
                    break;
                case AutoLoanLeaseGapCoverageCA2071:
                    if (vehicle.getAdditionalInterest().isEmpty()) {
                        Assert.fail("Vehicle must have an Additional Interest to have this coverage.");
                    } else {
                        fillOutAdditionalInterests(basicSearch, vehicle);
                        clickVehicleAdditionalCoveragesTab();
                        checkLoanLeaseGapCoverage_CA_20_71(true);
                    }
                    break;
                case RoadsideAssistanceIDCA313008:
                    setRoadsideAssistanceIDCA313008(true);
                    break;
                case FireFireAndTheftFireTheftAndWindstormAndLimitedSpecifiedCausesOfLossCoveragesCA9914:
                    break;
                case LessorAdditionalInsuredAndLossPayeeCA2001:
                    break;
                case LossPayableClauseAudioVisualAndDataElectronicEquipmentCoverageAddedLimitsIDCA313002:
                    break;
                case MobileHomesContentsCoverageCA2016:
                    break;
                case RentalReimbursementCA9923:
                    break;
            }//END SWITCH
        }//END FOR
    }

    private void fillOutAdditionalInterests(boolean basicSearch, Vehicle vehicle) throws Exception {
        clickVehicleAdditionalInterestsTab();
        for (AdditionalInterest additionalInterest : vehicle.getAdditionalInterest()) {
            setAdditionalInterest(basicSearch, additionalInterest);
            if (additionalInterest.getAdditionalInterestTypeCPP().equals(AdditionalInterestTypeCPP.LossPayableClause_AudioVisualAndDataElectronicEquipmentCoverageAddedLimitsIDCA_31_3002)) {
                if (vehicle.isAudioVisualData_CA_99_60()) {
                    clickVehicleAdditionalCoveragesTab();
                    checkAudioVisualAndDataElectronicEquipmentCoverageAddedLimits(true);
                    setAudioVisualDataLimit(vehicle.getAudioVisualDataLimit());
                    setAudioVisualDataTypeDesc(vehicle.getAudioVisualDataTypeOfEquipment());
                    clickVehicleAdditionalInterestsTab();
                }
            }
        }
    }


    private void setAdditionalInterest(boolean basicSearch, AdditionalInterest addInterest) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderAdditionalInterests additinalInterests = new GenericWorkorderAdditionalInterests(driver);
        additinalInterests.fillOutAdditionalInterest(basicSearch, addInterest);
    }

    @FindBy(xpath = "//div[contains(text(), 'Exclusion or Excess Coverage Hazards Otherwise Insured CA 99 40')]/preceding-sibling::table")
    private WebElement checkbox_checkExclusionExcessCoverageHazards_CA_99_40;


    private void checkExclusionExcessCoverageHazards_CA_99_40(boolean checked) {
        if (checked) {
            if (!checkbox_checkExclusionExcessCoverageHazards_CA_99_40.getAttribute("class").contains("cb-checked")) {
                clickWhenClickable(checkbox_checkExclusionExcessCoverageHazards_CA_99_40.findElement(By.xpath(".//tbody/child::tr/child::td/div/input")));
            }
        } else {
            if (checkbox_checkExclusionExcessCoverageHazards_CA_99_40.getAttribute("class").contains("cb-checked")) {
                clickWhenClickable(checkbox_checkExclusionExcessCoverageHazards_CA_99_40.findElement(By.xpath(".//tbody/child::tr/child::td/div/input")));
            }
        }
    }

    @FindBy(xpath = "//label[contains(text(), 'Covered Autos Liability Coverage does not apply')]")
    private WebElement CoveredAutosLiabilityCoverageDoesNotApply;

    @FindBy(xpath = "//label[contains(text(), 'Covered Autos Liability Coverage does not apply to \"bodily injury\" or \"property damage\"')]")
    private WebElement CoveredAutosLiabilityCoverageDoesNotApplytoBodilyInjuryPropertyDamage;

    //Covered Autos Liability Coverage does not apply to "bodily injury" or "property damage" occurring before the other insurance ends except to the extent damages exceed the limits of the other insurance. However, the most we will pay is the difference between the Limit of Insurance for Covered Autos Liability Coverage in this Coverage Form and the limits of the other insurance, if this Coverage Form's limits are higher.
    @FindBy(xpath = "//label[contains(text(), 'Covered Autos Liability Coverage does not apply')]")
    private WebElement CoveredAuvtosLiabilityCoverageDoesNotApply;


    public void fillOutVehiclesExclusionsAndConditions(Vehicle vehicle) {
        clickVehicleExclusionsAndConditionsTab();
        // Mobile Homes Contents Not Covered IDCA 31 3003
        // Removal Of Property Damage Coverage IDCA 31 3006
        // Exclusion or Excess Coverage Hazards Otherwise Insured CA 99 40
        for (CAVehicleExclusionsAndConditions exctionCondition : vehicle.getCaVehicleExclusionsAndConditionsList()) {
            switch (exctionCondition) {
                //FINISHME
                case ExclusionOrExcessCoverageHazardsOtherwiseInsuredCA9940:
                    checkExclusionExcessCoverageHazards_CA_99_40(true);
                    
                    clickWhenClickable(CoveredAutosLiabilityCoverageDoesNotApply.findElement(By.xpath(".//parent::div/following-sibling::div/descendant::label[contains(text(), 'Yes')]/preceding-sibling::input")));
                    break;
                default:
                    break;
            }//END SWITCH
        }//END FOR
    }//END fillOutVehiclesExclusionsAndConditions(Vehicle vehicle)

    @FindBy(xpath = "//div[contains(text(), 'Who is the registered owner of this vehicle')]/parent::td/following-sibling::td/div")
    private WebElement editbox_RegisteredOwner;


    private void setRegisteredOwner(String owner) {
        clickWhenClickable(editbox_RegisteredOwner);
        
        find(By.xpath("//textArea[contains(@name, 'c2')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
        find(By.xpath("//textArea[contains(@name, 'c2')]")).sendKeys(owner);
        
        find(By.xpath("//div[contains(text(), 'Who is the registered owner of this vehicle')]")).click();
        
    }


    public void fillOutVehiclesUnderwritingQuestions(boolean quickQuote, Vehicle vehicle) {
        clickVehicleUnderwritingQuestionsTab();
        

        if (!vehicle.getVehicleType().equals(VehicleType.PublicTransportation) && !vehicle.getVehicleType().equals(VehicleType.Miscellaneous) && !vehicle.getVehicleType().equals(VehicleType.Trailer)) {
            if (clickUWQuestionIsTheVehicleUsedForTowing(vehicle.isVehicleUsedForTowing())) {
                clickUWQuestionIsTheVehicleUsedForPoliceRotation(vehicle.isVehicleUsedForPoliceRotation());
                clickUWQuestionDoTheyRepossessAutos(vehicle.isDoTheyRepossessAutos());
                clickUWQuestionDoTheyAdvertiseAsATowingCompany(vehicle.isDoTheyAdvertiseAsTowingCompany());
            }
        }
        if (vehicle.getSecondaryClass().equals(SecondaryClass.SandAndGravelOtherThanQuarrying)) {
            clickDoesApplicantInsuredHaulSandOrGravelForOthers(false);
        }
        
        if (vehicle.getRadius().equals(Radius.LongDistancesOver301Miles)) {
            selectHowManyTimesAYearDoesApplicantTravelOver300Miles(vehicle.getHowManyTimesAYearDoesApplicantTravelOver300Miles());
        }

        switch (vehicle.getVehicleType()) {
            case PrivatePassenger:
                break;
            case PublicTransportation:
                clickUWQuestionVehicleBeingDrivenMoreThan300MileRadius(vehicle.isVehicleBeingDrivenMoreThan300MileRadius());
                break;
            case Trucks:
                break;
            case TruckTractors:
                break;
            case Trailer:
                break;
            case Miscellaneous:
                break;
        }

        for (CAVehicleAdditionalCoverages coverage : vehicle.getCaVehicleAdditionalCoveragesList()) {
            if (coverage.equals(CAVehicleAdditionalCoverages.MotorCarriersInsuranceForNonTruckingUseCA2309)) {
                clickDoesApplicantInsuredBackHaulOrTransportGoodsWhileNotOperatingUnderTheAuthorityOfAnotherCarrier(false);
                clickDoesThePartyWhoseAuthorityUnderWhichTheApplicantInsuredHaveCoverageForTheTrucksAndTrailersShownOnThePolicy(true);
                clickDoesTheApplicantInsuredLeaseOrOperateTheirVehicleUnderAnotherPartiesPUCAuthority(true);
                clickDoesTheApplicantInsuredLeaseTheVehicleWithoutADriver(false);
            }
        }

        // UW QUESTIONS
        switch (vehicle.getBodyType()) {
            case MotorHomesMoreThan22Feet:
            case MotorHomesUpTo22Feet:
            case TrailerEquippedAsLivingQuarters:
                clickUWQuestionMotorHomeLivingQuarters(vehicle.isMotorHomeHaveLivingQuarters());
                
                if (vehicle.isMotorHomeHaveLivingQuarters()) {
                    clickUWQuestionMotorHomeContentsCoverage(vehicle.isMotorHomeContentsCoverage());
                    clickVehicleExclusionsAndConditionsTab();
                    clickVehicleUnderwritingQuestionsTab();
                }

                if (vehicle.hasComprehensive() && vehicle.isMotorHomeContentsCoverage()) {
                    clickVehicleAdditionalCoveragesTab();
                    setMobileHomeContentsCoverageCA2016Limit(String.valueOf(vehicle.getMobileHomesContentsCoverageCA2016Limit()));
                    setMobileSpecifiedCauseOfLossCA2016(vehicle.getAdditionalCoveragesCauseOfLoss());
                    clickVehicleUnderwritingQuestionsTab();
                } else if (vehicle.hasSpecifiedCauseofLoss() && vehicle.isMotorHomeContentsCoverage()) {
                    clickVehicleAdditionalCoveragesTab();
                    setMobileHomeContentsCoverageCA2016Limit(String.valueOf(vehicle.getMobileHomesContentsCoverageCA2016Limit()));
                    setMobileSpecifiedCauseOfLossCA2016(vehicle.getAdditionalCoveragesCauseOfLoss());
                    clickVehicleUnderwritingQuestionsTab();
                    clickVehicleExclusionsAndConditionsTab();
                    clickVehicleUnderwritingQuestionsTab();
                }

                break;
            case FuneralLimo:
                break;
            case ChurchBus:
            case MotelCourtesyBus:
            case VanPoolEmployeeOnly:
                clickUWQuestionUsedToTransportMigrantWorkers(vehicle.isUsedToTransportMigrantWorkers());
                break;
            default:

                break;
        }

        //QUESTIONS FOR FULL APP ONLY
        if (!quickQuote) {
            systemOut("Setting FA questions for: " + vehicle.getVehicleName());
            setRegisteredOwner(vehicle.getWhoIsTheRegisteredOwner());
            if (vehicle.hasCollision() || vehicle.hasComprehensive() || vehicle.hasSpecifiedCauseofLoss()) {
                //set existing damage
                clickUWQuestionHasExistingDamage(vehicle.hasExistingDamage());
                if (vehicle.hasExistingDamage()) {
                    clickVehicleExclusionsAndConditionsTab();
                    clickAdd();
                    
                    selectLocationOfDamage(vehicle);
                    selectDamagedItem(vehicle);
                    selectDamageDescription(vehicle);
                    setExplanationOfDamage(vehicle);

                    clickVehicleUnderwritingQuestionsTab();
                }
            }
        }
    }


    public void setRandomPropertyDamage(Vehicle vehicle) {

        LocationOfDamageCPP damageLocation = LocationOfDamageCPP.random();

        switch (damageLocation) {
            case Front:
                vehicle.setDamageItem(FrontDamagedItemCPP.random().getValue());
                break;
            case RightFront:
                vehicle.setDamageItem(RightFrontDamagedItemCPP.random().getValue());
                break;
            case LeftFront:
                vehicle.setDamageItem(LeftFrontDamagedItemCPP.random().getValue());
                break;
            case RightFrontPillar:
                vehicle.setDamageItem(RightFrontPillarDamagedItemCPP.random().getValue());
                break;
            case LeftFrontPillar:
                vehicle.setDamageItem(LeftFrontPillarDamagedItemCPP.random().getValue());
                break;
            case RightTBone:
                vehicle.setDamageItem(RightTBoneDamagedItemCPP.random().getValue());
                break;
            case LeftTBone:
                vehicle.setDamageItem(LeftTBoneDamagedItemCPP.random().getValue());
                break;
            case RightQuarterPost:
                vehicle.setDamageItem(RightQuarterPostDamagedItemCPP.random().getValue());
                break;
            case LeftQuarterPost:
                vehicle.setDamageItem(LeftQuarterPostDamagedItemCPP.random().getValue());
                break;
            case RightRear:
                vehicle.setDamageItem(RightRearDamagedItemCPP.random().getValue());
                break;
            case LeftRear:
                vehicle.setDamageItem(LeftRearDamagedItemCPP.random().getValue());
                break;
            case Rear:
                vehicle.setDamageItem(RearDamagedItemCPP.random().getValue());
                break;
            case HoodRoofTrunklid:
                vehicle.setDamageItem(HoodRoofTrunklidDamagedItemCPP.random().getValue());
                break;
            case Undercarriage:
                vehicle.setDamageItem(UndercarriageDamagedItemCPP.random().getValue());
                break;
            case Glass:
                vehicle.setDamageItem(GlassDamagedItemCPP.random().getValue());
                break;
            case Other:
                vehicle.setDamageItem(OtherDamagedItemCPP.random().getValue());
                break;
        }

        vehicle.setLocationOfDamage(damageLocation.getValue());
        vehicle.setDamageDescription(DamageDescriptionCPP.random().getValue());
    }


    public void checkClassCode(GeneratePolicy myPolicy) throws Exception {

        int numVehicles = finds(By.xpath("//div[contains(@id,':BAVehiclesScreen:VehiclesListDetailPanel:BAVehiclesLV-body')]/div/table/tbody/tr")).size();

        
        boolean fleet = text_FleetValidation.getText().contentEquals("5 or more self-propelled vehicles");

        for (int i = 1; i <= numVehicles; i++) {

            

            editVehicleByVehicleNumber(i);

            int j = i - 1;

            defineWhichPathForClassCode(myPolicy.commercialAutoCPP.getCommercialAutoLine(),
                    myPolicy.commercialAutoCPP.getVehicleList().get(j), myPolicy.commercialAutoCPP.getDriversList(), fleet);
            
            classCode.append(beginClassCode).append(endClassCode);
            
            shownClassCode.append(text_ClassCode.getText());
            System.out.println("Shown Vehicle Number " + i + " is Vehicle Class Number " + shownClassCode);
            System.out.println("Vehicle Number " + i + " is Vehicle Class Number " + classCode);
            
            if (!classCode.toString().equals(shownClassCode.toString())) {
                throw new Exception("Thrown Exceptions: Class code shown is " + shownClassCode + " and it should be " + classCode + ".");
            }
            System.out.println("Tested Good: Class code shown is " + shownClassCode + " which matched the class code expected of " + classCode + ".");
            clickOK();
            

            shownClassCode.setLength(0);
            classCode.setLength(0);
            beginClassCode.setLength(0);
            endClassCode.setLength(0);
            driverClassCode.setLength(0);
            seatingCapClassCode.setLength(0);
            privateTransportationClassCode.setLength(0);
            beginningprivateTransportationClassCode.setLength(0);
            additionalClassCode.setLength(0);
            miscellaneousClassCode.setLength(0);

            firstDigit.setLength(0);
            secondDigit.setLength(0);
            thirdDigit.setLength(0);
            fourthDigit.setLength(0);
            fifthDigit.setLength(0);
            firstAndSecondDigit.setLength(0);
            secondAndThirdDigit.setLength(0);
            thirdAndFourthDigit.setLength(0);

        }
    }

    private Guidewire8Checkbox checkbox_NoVIN() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, ':NoVinCheckbox')]");
    }

    public void setNOVIN(boolean checked) {
        checkbox_NoVIN().select(checked);
        
    }

    @FindBy(xpath = "//input[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:Vin_DV')]")
    private WebElement text_VIN;

    public void checkTrailerVINUneditable() throws GuidewireNavigationException {
        createVehicleManually();
        selectVehicleType(VehicleType.Trailer);
        
        setNOVIN(true);
        if (checkIfElementExists(text_VIN, 10)) {
            Assert.fail("ERROR: VIN is editable when Trailer is Body Type and No VIN is checked. VIN should not be editable.");
        }
    }


    private Guidewire8Checkbox checkbox_Collision() {
        return new Guidewire8Checkbox(driver, "//div[contains(text() , 'Collision')]/preceding-sibling::table");
    }

    public boolean setCollision(boolean checked) {
        checkbox_Collision().select(checked);
        
        return checked;
    }

    public void checkRemovalOfPropertyDamageCoverage() {

        editVehicleByVehicleNumber(1);

        clickVehicleCoveragesTab();
        setCollision(true);
        

        clickVehicleUnderwritingQuestionsTab();
        clickUWQuestionHasExistingDamage(true);

        clickVehicleExclusionsAndConditionsTab();
        addRightTBoneDamagedItemCPP(RightTBoneDamagedItemCPP.PassengersSideFrontDoor);
        addDamageDescriptionCPP(DamageDescriptionCPP.HailDamage);
    }

    private void defineWhichPathForClassCode(CPPCommercialAutoLine autoLine, Vehicle vehicleInfo, List<Contact> list, boolean fleet) {

        VehicleType vehicleType = vehicleInfo.getVehicleType();

        if (!fleet && (vehicleType.equals(VehicleType.Trucks) || vehicleType.equals(VehicleType.TruckTractors)
                || vehicleType.equals(VehicleType.Trailer))) {

            nonFleetTrucksClassCode(vehicleInfo);
            secondaryClassCode(vehicleInfo);
            beginClassCode.append(driverClassCode);
            endClassCode.append(additionalClassCode);
        } else {
            if (fleet && (vehicleType.equals(VehicleType.Trucks)
                    || vehicleType.equals(VehicleType.TruckTractors) || vehicleType.equals(VehicleType.Trailer))) {

                fleetTrucksClassCode(vehicleInfo);
                secondaryClassCode(vehicleInfo);
                beginClassCode.append(driverClassCode);
                endClassCode.append(additionalClassCode);
            } else {
                if (!fleet && vehicleType.equals(VehicleType.PrivatePassenger)) {
                    privatePassengerWithoutDriverClassCode(autoLine, vehicleInfo, false);
                    beginClassCode.append(driverClassCode);
                } else {
                    if (fleet && vehicleType.equals(VehicleType.PrivatePassenger)) {
                        privatePassengerWithoutDriverClassCode(autoLine, vehicleInfo, true);
                        beginClassCode.append(driverClassCode);
                    } else {
                        if (!fleet && vehicleType.equals(VehicleType.PublicTransportation)) {
                            nonFleetprivateTransportationClassCode(vehicleInfo);
                            beginClassCode.append(driverClassCode);
                        } else {
                            if (fleet && vehicleType.equals(VehicleType.PublicTransportation)) {
                                fleetprivateTransportationClassCode(vehicleInfo);
                                beginClassCode.append(driverClassCode);
                            } else {
                                if (vehicleType.equals(VehicleType.Miscellaneous)) {
                                    miscellaneousClassCode(vehicleInfo);
                                } /*else {
									if (vehicleType.equals(VehicleType.PrivatePassenger) && age < 21) {
										privatePassengerWithDriverClassCode((Person) list);
										beginClassCode.append(driverClassCode);
										endClassCode.append(additionalClassCode);
									}
								}*/
                            }
                        }
                    }
                }
            }
        }
    }

    private void privatePassengerWithoutDriverClassCode(CPPCommercialAutoLine autoLine, Vehicle vehicleInfo, boolean fleet) {

        VehicleType vehicleType = vehicleInfo.getVehicleType();

        if (!fleet && vehicleType.equals(VehicleType.PrivatePassenger)) {
            driverClassCode.append("7391");
        } else {
            if (fleet && vehicleType.equals(VehicleType.PrivatePassenger)) {
                driverClassCode.append("7398");
            }
        }

    }

    @SuppressWarnings("unused")
    private void privatePassengerWithDriverClassCode(Contact driver) {

        Gender gender = driver.getGender();
        int age = driver.getAge();
        VehicleUse vehicleUse = driver.getVehicleUse();
        DriverType driverType = driver.getDriverType();

        if (gender.equals(Gender.Male)) {
            firstDigit.append("1");
        } else {
            if (gender.equals(Gender.Female)) {
                firstDigit.append("2");
            }
        }
        if (age <= 16) {
            secondAndThirdDigit.append("16");
        } else {
            if (age == 17) {
                secondAndThirdDigit.append("17");
            } else {
                if (age == 18) {
                    secondAndThirdDigit.append("18");
                } else {
                    if (age == 19) {
                        secondAndThirdDigit.append("19");
                    }
                    if (age == 20) {
                        secondAndThirdDigit.append("20");
                    }
                }
            }
        }
        if (vehicleUse.equals(VehicleUse.NotDriventoWorkorSchool)) {
            fourthDigit.append("1");
        } else {
            if (vehicleUse.equals(VehicleUse.DriventoWorkorSchoollessthan15miles)) {
                fourthDigit.append("2");
            } else {
                if (vehicleUse.equals(VehicleUse.DriventoWorkorSchoolmorethan15miles)) {
                    fourthDigit.append("3");
                }
            }
        }
        if (driverType.equals(DriverType.Primary)) {
            fifthDigit.append("1");
        } else {
            if (driverType.equals(DriverType.Occasional)) {
                fifthDigit.append("2");
            }
        }
        driverClassCode.append(firstDigit).append(secondAndThirdDigit).append(fourthDigit).append(fifthDigit);
    }

    private void nonFleetTrucksClassCode(Vehicle vehicleInfo) {

		/*		VehicleType vehicleType = vehicleInfo.getVehicleType();
		SizeClass sizeClass = vehicleInfo.getSizeClass();
		BusinessUseClass businessUseClass = vehicleInfo.getBusinessUseClass();
		BodyType bodyType = vehicleInfo.getBodyType();*/
        Radius radius = vehicleInfo.getRadius();

        trucksClassCode(vehicleInfo);
        if (radius.equals(Radius.Local0To100Miles)) {
            thirdDigit.append("1");
        } else {
            if (radius.equals(Radius.Intermediate101To300Miles)) {
                thirdDigit.append("2");
            } else {
                if (radius.equals(Radius.LongDistancesOver301Miles)) {
                    thirdDigit.append("3");
                }
            }
        }
        driverClassCode.append(firstDigit).append(secondDigit).append(thirdDigit);
    }

    private void fleetTrucksClassCode(Vehicle vehicleInfo) {

		/*		VehicleType vehicleType = vehicleInfo.getVehicleType();
		SizeClass sizeClass = vehicleInfo.getSizeClass();
		BusinessUseClass businessUseClass = vehicleInfo.getBusinessUseClass();
		BodyType bodyType = vehicleInfo.getBodyType();*/
        Radius radius = vehicleInfo.getRadius();

        trucksClassCode(vehicleInfo);
        if (radius.equals(Radius.Local0To100Miles)) {
            thirdDigit.append("4");
        } else {
            if (radius.equals(Radius.Intermediate101To300Miles)) {
                thirdDigit.append("5");
            } else {
                if (radius.equals(Radius.LongDistancesOver301Miles)) {
                    thirdDigit.append("6");
                }
            }
        }
        driverClassCode.append(firstDigit).append(secondDigit).append(thirdDigit);
    }

    private void nonFleetprivateTransportationClassCode(Vehicle vehicleInfo) {

        BodyType bodyType = vehicleInfo.getBodyType();
        Radius radius = vehicleInfo.getRadius();
        //		SeatingCapacity seatingCapacity = vehicleInfo.getSeatingCapacity();

        if (bodyType.equals(BodyType.ChurchBus)) {
            firstAndSecondDigit.append("63");
            if (radius.equals(Radius.Local0To100Miles)) {
                thirdDigit.append("5");
            } else {
                if (radius.equals(Radius.Intermediate101To300Miles)) {
                    thirdDigit.append("6");
                } else {
                    if (radius.equals(Radius.LongDistancesOver301Miles)) {
                        thirdDigit.append("7");
                    }
                }
            }
            beginningprivateTransportationClassCode.append(firstAndSecondDigit).append(thirdDigit);

        } else {
            firstAndSecondDigit.append("58");
            if (radius.equals(Radius.Local0To100Miles)) {
                thirdDigit.append("5");
            } else {
                if (radius.equals(Radius.Intermediate101To300Miles)) {
                    thirdDigit.append("6");
                } else {
                    if (radius.equals(Radius.LongDistancesOver301Miles)) {
                        thirdDigit.append("79");
                    }
                }
            }
            beginningprivateTransportationClassCode.append(firstAndSecondDigit).append(thirdDigit);
        }
        seatingCapacity(vehicleInfo);
        driverClassCode.append(beginningprivateTransportationClassCode).append(seatingCapClassCode);
    }

    private void fleetprivateTransportationClassCode(Vehicle vehicleInfo) {

        BodyType bodyType = vehicleInfo.getBodyType();
        Radius radius = vehicleInfo.getRadius();
        //		SeatingCapacity seatingCapacity = vehicleInfo.getSeatingCapacity();

        if (bodyType.equals(BodyType.ChurchBus)) {
            firstAndSecondDigit.append("63");
            if (radius.equals(Radius.Local0To100Miles)) {
                thirdDigit.append("8");
            } else {
                if (radius.equals(Radius.Intermediate101To300Miles)) {
                    thirdDigit.append("9");
                } else {
                    if (radius.equals(Radius.LongDistancesOver301Miles)) {
                        thirdDigit.append("0");
                    }
                }
            }
            beginningprivateTransportationClassCode.append(firstAndSecondDigit).append(thirdDigit);
        } else {
            firstAndSecondDigit.append("58");
            if (radius.equals(Radius.Local0To100Miles)) {
                thirdDigit.append("5");
            } else {
                if (radius.equals(Radius.Intermediate101To300Miles)) {
                    thirdDigit.append("6");
                } else {
                    if (radius.equals(Radius.LongDistancesOver301Miles)) {
                        thirdAndFourthDigit.append("09");
                    }
                }
            }
            beginningprivateTransportationClassCode.append(firstAndSecondDigit).append(thirdDigit);
        }
        seatingCapacity(vehicleInfo);
        driverClassCode.append(beginningprivateTransportationClassCode).append(seatingCapClassCode);
    }

    private void miscellaneousClassCode(Vehicle vehicleInfo) {

        BodyType bodyType = vehicleInfo.getBodyType();

        if (bodyType.equals(BodyType.MotorHomesUpTo22Feet)) {
            miscellaneousClassCode.append("7960");
        } else {
            if (bodyType.equals(BodyType.MotorHomesMoreThan22Feet)) {
                miscellaneousClassCode.append("7961");
            } else {
                if (bodyType.equals(BodyType.TrailerEquippedAsLivingQuarters)) {
                    miscellaneousClassCode.append("7963");
                } else {
                    if (bodyType.equals(BodyType.AntiqueAutos)) {
                        miscellaneousClassCode.append("9625");
                    } else {
                        if (bodyType.equals(BodyType.FuneralLimo)) {
                            miscellaneousClassCode.append("7915");
                        } else {
                            if (bodyType.equals(BodyType.Hearse)) {
                                miscellaneousClassCode.append("7922");
                            } else {
                                if (bodyType.equals(BodyType.FlowerCar)) {
                                    miscellaneousClassCode.append("7922");
                                }
                            }
                        }
                    }
                }
            }
        }
        driverClassCode.append(miscellaneousClassCode);
    }

    private void secondaryClassCode(Vehicle vehicleInfo) {

        SecondaryClassType secondaryClassType = vehicleInfo.getSecondaryClassType();
        SecondaryClass secondaryClass = vehicleInfo.getSecondaryClass();

        if (secondaryClassType.equals(SecondaryClassType.Truckers)) {
            if (secondaryClass.equals(SecondaryClass.CommonCarriers)) {
                additionalClassCode.append("21");
            } else {
                if (secondaryClass.equals(SecondaryClass.ContractCarriersOtherThanChemicalOrIronAndSteelHaulers)) {
                    additionalClassCode.append("22");
                } else {
                    if (secondaryClass.equals(SecondaryClass.ExemptCarriersOtherThanLivestockHaulers)) {
                        additionalClassCode.append("25");
                    } else {
                        if (secondaryClass.equals(SecondaryClass.ExemptCarriersHaulingLivestock)) {
                            additionalClassCode.append("26");
                        } else {
                            if (secondaryClass
                                    .equals(SecondaryClass.CarriersEngagedInBothPrivateCarriageAndTransportingGoods)) {
                                additionalClassCode.append("20");
                            } else {
                                if (secondaryClass.equals(SecondaryClass.AllOther)) {
                                    additionalClassCode.append("29");
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (secondaryClassType.equals(SecondaryClassType.FoodDelivery)) {
                if (secondaryClass.equals(SecondaryClass.CanneriesAndPackingPlants)) {
                    additionalClassCode.append("31");
                } else {
                    if (secondaryClass.equals(SecondaryClass.FishAndSeafood)) {
                        additionalClassCode.append("32");
                    } else {
                        if (secondaryClass.equals(SecondaryClass.FrozenFood)) {
                            additionalClassCode.append("33");
                        } else {
                            if (secondaryClass.equals(SecondaryClass.FruitAndVegtable)) {
                                additionalClassCode.append("34");
                            } else {
                                if (secondaryClass.equals(SecondaryClass.MeatAndPoultry)) {
                                    additionalClassCode.append("35");
                                } else {
                                    if (secondaryClass.equals(SecondaryClass.AllOther)) {
                                        additionalClassCode.append("39");
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (secondaryClassType.equals(SecondaryClassType.SpecializedDelivery)) {
                    if (secondaryClass.equals(SecondaryClass.FilmDelivery)) {
                        additionalClassCode.append("42");
                    } else {
                        if (secondaryClass.equals(SecondaryClass.MagazinesOrNewspapers)) {
                            additionalClassCode.append("43");
                        } else {
                            if (secondaryClass.equals(SecondaryClass.MailAndParcelPost)) {
                                additionalClassCode.append("44");
                            } else {
                                if (secondaryClass.equals(SecondaryClass.AllOther)) {
                                    additionalClassCode.append("49");
                                }
                            }
                        }
                    }
                } else {
                    if (secondaryClassType.equals(SecondaryClassType.WasteDisposal)) {
                        if (secondaryClass.equals(SecondaryClass.Garbage)) {
                            additionalClassCode.append("53");
                        } else {
                            if (secondaryClass.equals(SecondaryClass.JunkDealers)) {
                                additionalClassCode.append("54");
                            } else {
                                if (secondaryClass.equals(SecondaryClass.AllOther)) {
                                    additionalClassCode.append("59");
                                }
                            }
                        }
                    } else {
                        if (secondaryClassType.equals(SecondaryClassType.DumpAndTransitMix)) {
                            if (secondaryClass.equals(SecondaryClass.Excavating)) {
                                additionalClassCode.append("71");
                            } else {
                                if (secondaryClass.equals(SecondaryClass.SandAndGravelOtherThanQuarrying)) {
                                    additionalClassCode.append("72");
                                } else {
                                    if (secondaryClass.equals(SecondaryClass.AllOther)) {
                                        additionalClassCode.append("79");
                                    }
                                }
                            }
                        } else {
                            if (secondaryClassType.equals(SecondaryClassType.Contractors)) {
                                if (secondaryClass.equals(SecondaryClass.BuildingCommercial)) {
                                    additionalClassCode.append("81");
                                } else {
                                    if (secondaryClass.equals(SecondaryClass.BuildingPrivateDwellings)) {
                                        additionalClassCode.append("82");
                                    } else {
                                        if (secondaryClass.equals(
                                                SecondaryClass.ElectricalPlumbingMasonryPlasteringAndOtherRepairOrSevice)) {
                                            additionalClassCode.append("83");
                                        } else {
                                            if (secondaryClass.equals(SecondaryClass.Excavating)) {
                                                additionalClassCode.append("84");
                                            } else {
                                                if (secondaryClass.equals(SecondaryClass.StreetAndRoad)) {
                                                    additionalClassCode.append("85");
                                                } else {
                                                    if (secondaryClass.equals(SecondaryClass.AllOther)) {
                                                        additionalClassCode.append("89");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (secondaryClassType.equals(SecondaryClassType.NotOtherwiseSpecified)) {
                                    if (secondaryClass.equals(SecondaryClass.AllOther)) {
                                        additionalClassCode.append("99");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    //Methods Transfered from Vehicles Popup


//	public String getAdditionalInsuredNameByRowNumber(int row) {
//		return tableUtils.getCellTextInTableByRowAndColumnName(table_AdditionalInsured, row, "Name");
//	}


    public String getVehicleTableCellByColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_Vehicles, row, columnName);
    }


    public ArrayList<String> getVehicleDriverNames(String columnName) {
        return tableUtils.getAllCellTextFromSpecificColumn(table_Vehicles, columnName);
    }


    public void clickLinkInVehicleTable(String linkText) {
        tableUtils.clickLinkInTable(table_Vehicles, linkText);
    }


    public int getVehicleTableRowCount() {
        return tableUtils.getRowCount(table_Vehicles);
    }


    public void selectDriverTableSpecificRowByText(int rowNumber) {
        tableUtils.clickLinkInSpecficRowInTable(table_Vehicles, rowNumber);

    }


    public String getVehiclePopupValidationMessage() {
        return vehiclePageValidations.getText();
    }


    public void setCheckBoxInVehicleTable(int currentVeh) {
        tableUtils.setCheckboxInTable(table_Vehicles, currentVeh, true);

    }


//	@FindBy(xpath = "//div[contains(text(),'Deletion of Material Damage Coverage Endorsement 310')]/ancestor::fieldset")
//	private WebElement table_DeletionOfMaterialDamageCoverageEndorsement_310;


    @FindBy(xpath = "//*[contains(@id, ':removeAssignedDriverMicro')]")
    private WebElement button_RemoveAssignedDriver;

    Guidewire8Select select_ExclusionOrExcessCoverage() {
        return new Guidewire8Select(driver, "//table[contains(@id,':1:LineExcl:CoverageInputSet:CovPatternInputGroup-legendChk')]");
    }


//	public void fillOutVehiclesExclusionsConditions(Vehicle vehicle) {
//		clickExclusionsAndConditionsTab();
//		setDeletionOfMaterial310(true);
//		clickAdd();
//		selectLocationOfDamageDeletionOfMaterialDamageCoverageEndorsement_310(vehicle);
//		selectDamagedItemDeletionOfMaterialDamageCoverageEndorsement_310(vehicle);
//		selectDamageDescriptionDeletionOfMaterialDamageCoverageEndorsement_310(vehicle);
//	}


//	private void selectLocationOfDamageDeletionOfMaterialDamageCoverageEndorsement_310(Vehicle vehicle) {
//		tableUtils.setValueForCellInsideTable(table_DeletionOfMaterialDamageCoverageEndorsement_310, tableUtils.getNextAvailableLineInTable(table_DeletionOfMaterialDamageCoverageEndorsement_310, "Location Of Damage"), "Location Of Damage", "c1", vehicle.getLocationOfDamage());
//		
//	}
//
//
//	private void selectDamagedItemDeletionOfMaterialDamageCoverageEndorsement_310(Vehicle vehicle) {
//		tableUtils.setValueForCellInsideTable(table_DeletionOfMaterialDamageCoverageEndorsement_310, tableUtils.getNextAvailableLineInTable(table_DeletionOfMaterialDamageCoverageEndorsement_310, "Damaged Item"), "Damaged Item", "c2", vehicle.getDamageItem());
//		
//	}
//
//
//	private void selectDamageDescriptionDeletionOfMaterialDamageCoverageEndorsement_310(Vehicle vehicle) {
//		tableUtils.setValueForCellInsideTable(table_DeletionOfMaterialDamageCoverageEndorsement_310, tableUtils.getNextAvailableLineInTable(table_DeletionOfMaterialDamageCoverageEndorsement_310, "Damage Description"), "Damage Description", "c3", vehicle.getDamageDescription());
//		
//	}


    public void removeAssignedDriver() {
        clickWhenClickable(button_RemoveAssignedDriver);

    }


    @FindBy(xpath = "//input[contains(@id, 'VehicleDV:Make-inputEl') or contains(@id, ':PersonalAuto_VehicleDV:Make_DV-inputEl')]")
    private WebElement editbox_Make;
    @FindBy(xpath = "//input[contains(@id, 'VehicleDV:Vin-inputEl') or contains(@id, ':PersonalAuto_VehicleDV:Vin_DV-inputEl')]")
    private WebElement editbox_VIN;
    @FindBy(xpath = "//input[contains(@id, 'VehicleDV:Year-inputEl') or contains(@id, ':PersonalAuto_VehicleDV:Year_DV-inputEl')]")
    private WebElement editbox_ModelYear;
    @FindBy(xpath = "//input[contains(@id, 'VehicleDV:Model-inputEl') or contains(@id, ':PersonalAuto_VehicleDV:Model_DV-inputEl')]")
    private WebElement editbox_Model;

    private Guidewire8Select select_DriverToAssign() {
        return new Guidewire8Select(driver, "//table[contains(@id,'PAVehiclePopup:PersonalAuto_AssignDriversDV:assignDriverPolicyDriver-triggerWrap')]");
    }

    //DO NOT DELETE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
    public Vehicle gatherVehicleInfoToValidate(String vehicleName) {
        Vehicle vehicleGathered = new Vehicle();
        vehicleGathered.removeDefaultValues();
        By tableSelector = By.cssSelector("div[id$=':VehiclesLV-body']");
        waitUntilElementIsVisible(tableSelector);
        int rowThatHasName = tableUtils.getRowNumberInTableByText(table_SubmissionVehicleDetailsForUtils, vehicleName);
        tableUtils.clickLinkInSpecficRowInTable(table_SubmissionVehicleDetailsForUtils, rowThatHasName);

        By vehicleType = By.cssSelector("input[id$='Type_DV-inputEl']");
        System.out.println(find(vehicleType).getAttribute("value"));
        vehicleGathered.setVehicleTypePL(VehicleTypePL.getEnumFromStringValue(find(vehicleType).getAttribute("value")));
        vehicleGathered.setVin(editbox_VIN.getAttribute("value"));
        vehicleGathered.setModelYear(Integer.valueOf(editbox_ModelYear.getAttribute("value")));
        vehicleGathered.setMake(editbox_Make.getAttribute("value"));
        vehicleGathered.setModel(editbox_Model.getAttribute("value"));

        AddressInfo garagedAt = new AddressInfo();
        garagedAt.removeDefaultValues();

        By addressLineOne = By.cssSelector("div[id$='::AddressLine1-inputEl']");
        By city = By.cssSelector("div[id$=':city-inputEl']");
        By zip = By.cssSelector("div[id$=':postalCode-inputEl']");


        garagedAt.setLine1(find(addressLineOne).getText());
        garagedAt.setCity(find(city).getText());
        By state = By.cssSelector("input[id$='state-inputEl']");
        garagedAt.setState(State.valueOf(find(state).getAttribute("value")));
        garagedAt.setZip(find(zip).getText());

        vehicleGathered.setGaragedAt(garagedAt);
        By commutingMiles = By.cssSelector("input[id$='commutemiles_DV-inputEl']");
        vehicleGathered.setCommutingMiles(CommutingMiles.getEnumFromStringValue(find(commutingMiles).getAttribute("value")));

        Contact driver = new Contact();
        driver.removeDefaultValues();
        driver.setFullName(select_DriverToAssign().getText());

        vehicleGathered.setDriverPL(driver);

        return vehicleGathered;

    }


    private void trucksClassCode(Vehicle vehicleInfo) {

        VehicleType vehicleType = vehicleInfo.getVehicleType();
        SizeClass sizeClass = vehicleInfo.getSizeClass();
        BusinessUseClass businessUseClass = vehicleInfo.getBusinessUseClass();
        BodyType bodyType = vehicleInfo.getBodyType();

        if (vehicleType.equals(VehicleType.Trailer)) {
            firstDigit.append("6");
            if (bodyType.equals(BodyType.Semitrailers) || sizeClass.equals(SizeClass.Semitrailers)) {
                secondDigit.append("7");
            } else {
                if (bodyType.equals(BodyType.TrailersOver2000lbs) || sizeClass.equals(SizeClass.TrailersOver2000lbs)) {
                    secondDigit.append("8");
                } else {
                    if (bodyType.equals(BodyType.ServiceOrUtilityTrailers)
                            || sizeClass.equals(SizeClass.ServiceOrUtilityTrailers)) {
                        secondDigit.append("9");
                    }
                }
            }
        } else {
            if (sizeClass.equals(SizeClass.LightTrucksGVWOf10000PoundsOrLess)) {
                firstDigit.append("0");
            } else {
                if (sizeClass.equals(SizeClass.MediumTrucksGVWOf10001To20000Pounds)) {
                    firstDigit.append("2");
                } else {
                    if (sizeClass.equals(SizeClass.HeavyTrucksGVWOf20001To45000Pounds)) {
                        firstDigit.append("3");
                    } else {
                        if (sizeClass.equals(SizeClass.ExtraHeavyTrucksGVWOver45000Pounds)) {
                            firstDigit.append("4");
                        } else {
                            if (sizeClass
                                    .equals(SizeClass.HeavyTruckTractorAGrossCombinationWeightGCWOf45000PoundsOrLess)) {
                                firstDigit.append("3");
                            } else {
                                if (sizeClass.equals(
                                        SizeClass.ExtraHeavyTruckTractorAGrossCombinationWeightGCWOver45000Pounds)) {
                                    firstDigit.append("5");
                                }
                            }
                        }
                    }
                }
            }
        }
        if (businessUseClass.equals(BusinessUseClass.None)) {
            secondDigit.append("0");
        } else {
            if (vehicleType.equals(VehicleType.Trucks)) {
                if (businessUseClass.equals(BusinessUseClass.ServiceUse)) {
                    secondDigit.append("1");
                } else {
                    if (businessUseClass.equals(BusinessUseClass.RetailUse)) {
                        secondDigit.append("2");
                    } else {
                        if (businessUseClass.equals(BusinessUseClass.CommercialUse)) {
                            secondDigit.append("3");
                        }
                    }
                }
            } else {
                if (vehicleType.equals(VehicleType.TruckTractors)) {
                    if (businessUseClass.equals(BusinessUseClass.ServiceUse)) {
                        secondDigit.append("4");
                    } else {
                        if (businessUseClass.equals(BusinessUseClass.RetailUse)) {
                            secondDigit.append("5");
                        } else {
                            if (businessUseClass.equals(BusinessUseClass.CommercialUse)) {
                                secondDigit.append("6");
                            }
                        }
                    }
                }
            }
        }

    }

    private void seatingCapacity(Vehicle vehicleInfo) {

        SeatingCapacity seatingCapacity = vehicleInfo.getSeatingCapacity();

        if (seatingCapacity.equals(SeatingCapacity.OneToEight)) {
            seatingCapClassCode.append("1");
        } else {
            if (seatingCapacity.equals(SeatingCapacity.NineToTwenty)) {
                seatingCapClassCode.append("2");
            } else {
                if (seatingCapacity.equals(SeatingCapacity.TwentyOneToSixty)) {
                    seatingCapClassCode.append("3");
                } else {
                    if (seatingCapacity.equals(SeatingCapacity.Over60)) {
                        seatingCapClassCode.append("4");
                    }
                }
            }
        }
    }

}

