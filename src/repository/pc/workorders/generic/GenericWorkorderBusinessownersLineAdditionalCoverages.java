package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.BusinessownersLine.*;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_Deductible;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_NumberLocations;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderBusinessownersLineAdditionalCoverages extends GenericWorkorderBusinessownersLine {

	private WebDriver driver;

	public GenericWorkorderBusinessownersLineAdditionalCoverages(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}


	public void fillOutBusinessOwnersLineAdditionalCoverages(PolicyBusinessownersLine busOwnLineObj) {
		clickBusinessownersLine_AdditionalCoverages();
		fillOutCrimeCoverages(busOwnLineObj);
		fillOutOtherLiabilityCovergaes(busOwnLineObj);
		fillOutLiquorCoverages(busOwnLineObj);
		fillOutProfessionalOccupations(busOwnLineObj);
		fillOutOtherPropertyCoverages(busOwnLineObj);
		fillOutAdditionalInsuredCoverages(busOwnLineObj);
	}


	public void fillOutCrimeCoverages(PolicyBusinessownersLine busOwnLineObj) {
		if (busOwnLineObj.getAdditionalCoverageStuff().isEmployeeDishonestyCoverage()) {
			checkBusinessownersLineAdditionalCoveragesEmployeeDishonesty(true,
					busOwnLineObj.getAdditionalCoverageStuff().getEmpDisLimit(),
					busOwnLineObj.getAdditionalCoverageStuff().getEmpDisNumCoveredEmployees(),
					busOwnLineObj.getAdditionalCoverageStuff().isEmpDisReferencesChecked(),
					busOwnLineObj.getAdditionalCoverageStuff().getEmpDisHowOftenAudits(),
					busOwnLineObj.getAdditionalCoverageStuff().getEmpDisAuditsPerformedBy(),
					busOwnLineObj.getAdditionalCoverageStuff().isEmpDisDiffWriteThanReconcile(),
					busOwnLineObj.getAdditionalCoverageStuff().isEmpDisLargeCheckProcedures());
		} else {
			checkBusinessownersLineAdditionalCoveragesEmployeeDishonesty(false, null, 0, false, null, null, false, false);
		}
	}

	public void fillOutOtherLiabilityCovergaes(PolicyBusinessownersLine busOwnLineObj) {
		// HIRED AUTO
		if(checkIfElementExists("//span[contains(@class, 'g-title') and contains(text(), 'Businessowners Line')]", 5000)) {
			clickBusinessownersLine_AdditionalCoverages();
		} else {
			new repository.pc.sidemenu.SideMenuPC(driver).clickSideMenuBusinessownersLine();
			clickBusinessownersLine_AdditionalCoverages();
		}

		if (busOwnLineObj.getAdditionalCoverageStuff().isHiredAutoCoverage()) {

			checkBusinessownersLineAdditionalCoveragesHiredAutoCheckbox(busOwnLineObj.getAdditionalCoverageStuff().isHiredAutoCoverage());
			setBusinessownersLineAdditionalCoveragesApplicantHaveOwnedAuto(busOwnLineObj.getAdditionalCoverageStuff().isHiredAutoOwnedAuto());
		}

		// NON OWNED LIABILITY
		if (busOwnLineObj.getAdditionalCoverageStuff().isNonOwnedAutoLiabilityCoverage()) {

			checkBusinessownersLineAdditionalCoveragesNonOwnedAutoLiability(
					busOwnLineObj.getAdditionalCoverageStuff().isNonOwnedAutoLiabilityCoverage());
			setBusinessownersLineAdditionalCoveragesDeliveryServiceWithNonCompnayVehicle(
					busOwnLineObj.getAdditionalCoverageStuff().isNonOwnedAutoNonCompanyVehicle());
			// method also always called below. jon larsen 9/1/2015
			setBusinessownersLineAdditionalCoveragesSellingChargingLiquor(
					busOwnLineObj.getAdditionalCoverageStuff().isSellingOrChargingLiquor());
		}

		//EPLI
		if(checkEmploymentPracticesLiabilityInsurance(busOwnLineObj.getAdditionalCoverageStuff().isEmploymentPracticesLiabilityInsurance())) {
			// hand rated Question is not displayed for Agent so commented
			if(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_Handrated() != 0) {
				setEPLI_HandRated(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_Handrated());
			}
			setEPLI_NumberOfFulltimeEmployees(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees());
			setEPLI_NumberofPartTimeEmployyes(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_NumberOfPartTimeEmployees());
			selectEPLI_AggregateLimit(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_AggregateLimit());
			switch(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_AggregateLimit()) {
			case FiftyThousand:
				//2500 5000
				if(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.TwoThousandFiveHundred) ||
						busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand)  ) {
					selectEPLI_Deductible(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible());
				} else {
					Assert.fail(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().getValue() + "IS NOT AVAILABLE WITH LIMIT OF 50,000");
				}
				break;
			case FiveHundredThousand:
				// 2500 5000 10000 25000
				if(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.TwoThousandFiveHundred) ||
						busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand) ||
						busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.TenThousand) ||
						busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.TwentyFiveThousand)) {
					selectEPLI_Deductible(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible());
				} else {
					Assert.fail(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().getValue() + "IS NOT AVAILABLE WITH LIMIT OF 500,000");
				}
				break;
			case OneHundredThousand:
				//2500 5000 10000
				if(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.TwoThousandFiveHundred) ||
						busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand)  ||
						busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.TenThousand)) {
					selectEPLI_Deductible(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible());
				} else {
					Assert.fail(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().getValue() + "IS NOT AVAILABLE WITH LIMIT OF 100,000");
				}
				break;
			case OneMillion:
				// 2500 5000 10000 25000
				if(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.TwoThousandFiveHundred) ||
						busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand) ||
						busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.TenThousand) ||
						busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.TwentyFiveThousand)) {
					selectEPLI_Deductible(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible());
				} else {
					Assert.fail(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().getValue() + "IS NOT AVAILABLE WITH LIMIT OF 1,000,000");
				}
				break;
			case SeventyFiveThousand:
				//5000
				if(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand)) {
					//do nothing
				}  else {
					Assert.fail(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().getValue() + " IS NOT AVAILABLE WITH LIMIT OF 75,000");
				}
				break;
			case TenThousand:
				//NOT AVAILABLE FOR BOP
				break;
			case TwentyFiveThousand:
				//2500 5000
				if(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.TwoThousandFiveHundred) ||
						busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand)) {
					selectEPLI_Deductible(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible());
				} else {
					Assert.fail(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().getValue() + "IS NOT AVAILABLE WITH LIMIT OF 25,000");
				}
				break;
			case TwoHundredFiftyThousand:
				//2500 5000 10000 25000
				if(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.TwentyFiveThousand) ||
						busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand) ||
						busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.TwoThousandFiveHundred) || 
						busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().equals(EmploymentPracticesLiabilityInsurance_Deductible.TenThousand)) {
					selectEPLI_Deductible(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible());
				} else {
					Assert.fail(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_deductible().getValue() + "IS NOT AVAILABLE WITH LIMIT OF 250,000");
				}
				break;
			}
			selectEPLI_NumberOfLocations(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_NumberOfLocations());
			if(checkThirdPartyViolations(busOwnLineObj.getAdditionalCoverageStuff().isEmploymentPracticesLiabilityInsurance_ThirdPartyViolations())) {
				if(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_ThirdPartyViolations_HandRated() != 0) {
					setThirdPartyViolations_handRated(busOwnLineObj.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsurance_ThirdPartyViolations_HandRated());
				}
			}
		}
	}

	private Guidewire8Checkbox checkbox_SupplementalExtendedReportingPeriodEnd() {
		return new Guidewire8Checkbox(driver,  "//div[contains(text(), 'Supplemental Extended Reporting Period Endorsement IDCW 31 0002')]/preceding-sibling::table");
	}
	public void checkSupplementalExtendedReportingPeriodEndorsement(boolean checked) {
		if(checkIfElementExists("//span[contains(@class, 'g-title') and contains(text(), 'Businessowners Line')]", 5000)) {
			clickBusinessownersLine_AdditionalCoverages();
		} else {
			new SideMenuPC(driver).clickSideMenuBusinessownersLine();
			clickBusinessownersLine_AdditionalCoverages();
		}
		checkbox_SupplementalExtendedReportingPeriodEnd().select(checked);
	}


	@FindBy(xpath="//div[text()='Third Party Violations']/ancestor::legend/following-sibling::div//label[contains(text(), 'Hand rated')]/parent::td/following-sibling::td/input")
	private WebElement thirdPartyViolations_HandRated;

	private void setThirdPartyViolations_handRated(int handrated) {
		setText(thirdPartyViolations_HandRated, handrated+"");
	}

	private Guidewire8Checkbox thirdPartyViolations() {
		return new Guidewire8Checkbox(driver, "//div[text()='Third Party Violations']/preceding-sibling::table");
	}
	private boolean checkThirdPartyViolations(boolean checked) {
		thirdPartyViolations().select(checked);
		return checked;
	}



	public void fillOutLiquorCoverages(PolicyBusinessownersLine busOwnLineObj) {
		setBusinessownersLineAdditionalCoveragesSellingChargingLiquor(busOwnLineObj.getAdditionalCoverageStuff().isSellingOrChargingLiquor());
	}

	public void fillOutProfessionalOccupations(PolicyBusinessownersLine busOwnLineObj) {
		// BEAUTY SALON
		if (busOwnLineObj.getAdditionalCoverageStuff().isBeautySalonBarberShopCoverage()) {

			checkBusinessownersLineAdditionalCoveragesBeautySalonsAndBarberShops(
					busOwnLineObj.getAdditionalCoverageStuff().isBeautySalonBarberShopCoverage());
			set_BeautySalonApplicantsExposure(
					busOwnLineObj.getAdditionalCoverageStuff().getBeautySalonTypeOfOperation());
			set_NumberOfOperators(busOwnLineObj.getAdditionalCoverageStuff().getBeautySalonNumOperators());
			set_NumberOfStations(busOwnLineObj.getAdditionalCoverageStuff().getBeautySalonNumStations());
		}
	}

	public void fillOutOtherPropertyCoverages(PolicyBusinessownersLine busOwnLineObj) {
		// ELECTRONIC DATA
		if (busOwnLineObj.getAdditionalCoverageStuff().isElectronicDataCoverage()) {

			checkBusinessownersLineAdditionalCoveragesElectronicData(
					busOwnLineObj.getAdditionalCoverageStuff().isElectronicDataCoverage());
			set_ElectronicDataLimit(busOwnLineObj.getAdditionalCoverageStuff().getElectronicDataLimit());
		}
		// INSURANCE TO VALUE
		checkBusinessownersLineAdditionalCoveragesInsuranceToValue(busOwnLineObj.getAdditionalCoverageStuff().isInsuranceToValueCoverage());
	}

	public void fillOutAdditionalInsuredCoverages(PolicyBusinessownersLine busOwnLineObj) {
		// ADDITIONAL INSURED ENG ARCH SURV
		checkBusinessownersLineAdditionalCoveragesAdditionalInsuredEngineers(busOwnLineObj.getAdditionalCoverageStuff().isAdditionalInsuredEngineersArchitectsSurveyorsCoverage());
	}


	//checkbox_LiquorLiabilityCoverage
	// @author ecoleman
	public void addLiquorLiability(Boolean yesOrNo) {
		setBusinessownersLineAdditionalCoveragesSellingChargingLiquor(yesOrNo);

		if (yesOrNo == true) {
			checkbox_LiquorLiabilityCoverage().select(true);
			select_LiquorSalesType().selectByVisibleText(LiquorSalesType.PackageSalesConsumedOffPremises.getValue());
		}
	}


	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------

	@FindBy(xpath = "//div[contains(text(), 'Employment Practices Liability Insurance')]/ancestor::legend/following-sibling::div")
	private WebElement element_EPLI;

	private Guidewire8Checkbox checkbox_EmploymentPracticesLiabilityInsurance() {
		return new Guidewire8Checkbox(driver,  "//div[contains(text(), 'Employment Practices Liability Insurance')]/preceding-sibling::table");
	}
	private boolean checkEmploymentPracticesLiabilityInsurance(boolean checked) {
		//jlarsen temp fix till i find a better one
		if(!finds(By.xpath("//div[contains(text(), 'Employment Practices Liability Insurance')]/preceding-sibling::table")).isEmpty()) {
			checkbox_EmploymentPracticesLiabilityInsurance().select(checked);
		}
		return checked;
	}

	@FindBy(xpath = "//div[contains(text(), 'Employment Practices Liability Insurance')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Hand rated')]/parent::td/following-sibling::td/input")
	private WebElement editbox_EPLI_handRated;
	private void setEPLI_HandRated (int handRated) {
		setText(editbox_EPLI_handRated, handRated+"");
	}

	@FindBy(xpath = "//div[contains(text(), 'Employment Practices Liability Insurance')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Number of full time employees')]/parent::td/following-sibling::td/input")
	private WebElement editbox_EPLI_NumberOfFullTimeEmployees;
	private void setEPLI_NumberOfFulltimeEmployees (int numOfEmployees) {
		setText(editbox_EPLI_NumberOfFullTimeEmployees, numOfEmployees+"");
	}

	@FindBy(xpath = "//div[contains(text(), 'Employment Practices Liability Insurance')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Number of part time employees')]/parent::td/following-sibling::td/input")
	private WebElement editbox_EPLI_NumberOfPartTimeEmployees;
	private void setEPLI_NumberofPartTimeEmployyes (int numOfEmployees) {
		setText(editbox_EPLI_NumberOfPartTimeEmployees, numOfEmployees+"");
	}

	private Guidewire8Select select_EPLI_AggregateLimit() {
		return new Guidewire8Select(driver, "//div[contains(text(), 'Employment Practices Liability Insurance')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Aggregate limit')]/parent::td/following-sibling::td/table");
	}
	private void selectEPLI_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit limit) {
		Guidewire8Select mySelect = select_EPLI_AggregateLimit();
		mySelect.selectByVisibleText(limit.getValue());
	}

	private Guidewire8Select select_EPLI_Deductible() {
		return new Guidewire8Select(driver, "//div[contains(text(), 'Employment Practices Liability Insurance')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Deductible')]/parent::td/following-sibling::td/table");
	}
	private void selectEPLI_Deductible(EmploymentPracticesLiabilityInsurance_Deductible deductible) {
		Guidewire8Select mySelect = select_EPLI_Deductible();
		mySelect.selectByVisibleText(deductible.getValue());
	}

	@FindBy(xpath = "//div[contains(text(), 'Employment Practices Liability Insurance')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Retroactive date')]/parent::td/following-sibling::td/input")
	private WebElement editbox_EPLI_RetroactiveDate;
	private void setEPLI_RetroactiveDate (String date) {
		setText(editbox_EPLI_RetroactiveDate, date);
	}

	private Guidewire8Checkbox checkbox_EPLI_ThirdPartyViolations() {
		return new Guidewire8Checkbox(driver,  "//div[contains(text(), 'Third Party Violation')]/preceding-sibling::table");
	}
	public void setEPLI_ThirdPartyViolations(boolean trueFalseChecked) {
		checkbox_EPLI_ThirdPartyViolations().select(trueFalseChecked);
	}

	private Guidewire8Select select_EPLI_TotalNumberOfLocations() {
		return new Guidewire8Select(driver, "//div[contains(text(), 'Employment Practices Liability Insurance')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Total number of locations')]/parent::td/following-sibling::td/table");
	}
	private void selectEPLI_NumberOfLocations(EmploymentPracticesLiabilityInsurance_NumberLocations locations) {
		Guidewire8Select mySelect = select_EPLI_TotalNumberOfLocations();
		mySelect.selectByVisibleText(locations.getValue());
	}





	private Guidewire8Checkbox checkbox_SubmissionBusinessownersLineAdditionalCoveragesEmployeeDishonesty() {
		return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPAdditionalCoverageCrimeCatDV:0:CoverageInputSet:CovPatternInputGroup-legendChk')]");
	}

	private Guidewire8Select select_SubmissionBusinessownersLineAdditionalCoveragesEmployeeDishonestyCoverageLimit() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'CoverageInputSet:CovPatternInputGroup:BOPEmpDisLimitTerm-triggerWrap')]");
	}

	@FindBy(xpath = "//input[contains(@id, 'CoverageInputSet:CovPatternInputGroup:BOPEmpDisNumEmpTerm-inputEl')]")
	private WebElement editBox_SubmissionBusinessownersLineAdditionalCoveragesEmployeeDishonestyNumberOfCoveredEmployees;

	@FindBy(xpath = "//input[contains(@id, 'CoverageInputSet:CovPatternInputGroup:BOPEmpDisAuditsTerm-inputEl')]")
	private WebElement editBox_SubmissionBusinessownersLineAdditionalCoveragesEmployeeDishonestyAuditsConductedFrequency;

	private Guidewire8Select select_SubmissionBusinessownersLineAdditionalCoveragesEmployeeDishonestyAuditsPerformedBy() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'CoverageInputSet:CovPatternInputGroup:BOPEmpDisWho-triggerWrap')]");
	}

	private Guidewire8Checkbox checkbox_SubmissionBusinessownersLineAdditionalCoveragesHiredAuto() {
		return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPAdditionalCoverageLiabilityOtherCatDV:0:CoverageInputSet:CovPatternInputGroup-legendChk')]");
	}

	private Guidewire8Checkbox checkbox_SubmissionBusinessownersLineAdditionalCoveragesNonOwnedAutoLiability() {
		return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPAdditionalCoverageLiabilityOtherCatDV:1:CoverageInputSet:CovPatternInputGroup-legendChk')]");
	}

	private Guidewire8Checkbox checkbox_SubmissionBusinessownersLineAdditionalCoveragesBeautySalonsAndBarberShops() {
		return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPAdditionalCoverageProfOccCatDV:0:CoverageInputSet:CovPatternInputGroup-legendChk')]");
	}

	private Guidewire8Select select_BeautySalonApplicantsExposure() {
		return new Guidewire8Select(driver, ":CoverageInputSet:CovPatternInputGroup:BOPApplicantsExposures-triggerWrap");
	}


	@FindBy(xpath = "//input[contains(@id, 'BusinessOwnersProfOccCatPanel:BOPAdditionalCoverageProfOccCatDV:0:CoverageInputSet:CovPatternInputGroup:BOPNumberOfOperators-inputEl')]")
	private WebElement editBox_SubmissionBusinessownersLineAdditionalCoveragesBeautySalonNumberOfOperators;

	@FindBy(xpath = "//input[contains(@id, 'BusinessOwnersProfOccCatPanel:BOPAdditionalCoverageProfOccCatDV:0:CoverageInputSet:CovPatternInputGroup:BOPNumberOfStations-inputEl')]")
	private WebElement editBox_SubmissionBusinessownersLineAdditionalCoveragesBeautySalonNumberofStations;

	private Guidewire8Checkbox checkbox_SubmissionBusinessownersLineAdditionalCoveragesElectronicData() {
		return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BusinessOwnersPolicyOtherCatPanel:BOPAdditionalCoveragePolicyOtherCatDV:0:CoverageInputSet:CovPatternInputGroup-legendChk')]");
	}

	@FindBy(xpath = "//fieldset[contains(., 'Electronic Data')]/div//td[.='Electronic Data Limit']/following-sibling::td/input[contains(@id, ':CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl')]")
	private WebElement editBox_SubmissionBusinessownersLineAdditionalCoveragesElectronicDataLimit;


	private Guidewire8Checkbox checkbox_SubmissionBusinessownersLineAdditionalCoveragesInsuranceToValue() {
		return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BusinessOwnersPolicyOtherCatPanel:BOPAdditionalCoveragePolicyOtherCatDV:2:CoverageInputSet:CovPatternInputGroup-legendChk')]");
	}

	private Guidewire8Checkbox checkbox_SubmissionBusinessownersLineAdditionalCoveragesAdditionalInsuredEngineers() {
		return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BusinessOwnersAddnlInsuredCatPanel:BOPAdditionalCoverageAddnlInsuredCatDV:3:FBAddnlCoverageInputSet:CovPatternInputGroup-legendChk')]");
	}

	private Guidewire8RadioButton radio_EmployeeDishonestyReferencesChecked() {
		return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPAdditionalCoverageCrimeCatDV:0:CoverageInputSet:CovPatternInputGroup:BOPEmpDisRef')]");
	}

	private Guidewire8RadioButton radio_EmployeeDishonestySeperateCheckReconciliation() {
		return new Guidewire8RadioButton(driver, "//table[contains(@id, ':CoverageInputSet:CovPatternInputGroup:BOPEmpDisChecks')]");
	}

	private Guidewire8RadioButton radio_EmployeeDishonestyChecksMultipleSignatures() {
		return new Guidewire8RadioButton(driver, "//table[contains(@id, ':CoverageInputSet:CovPatternInputGroup:BOPEmpDisProcedures')]");
	}

	private Guidewire8RadioButton radio_DeliveryServiceWithNonCompnayVehicle() {
		return new Guidewire8RadioButton(driver, "//label[contains(text(), 'Is Delivery Service with a Non-Company Vehicle?')]/ancestor::table[contains(@id, ':CovTermInputSet:BooleanTermInput')][1]");
	}

	private Guidewire8RadioButton radio_ApplicantHaveOwnedAuto() {
		return new Guidewire8RadioButton(driver, "//label[contains(text(), 'Does Applicant have an Owned Auto?')]/ancestor::table[contains(@id, ':CovTermInputSet:BooleanTermInput')][1]");
	}

	private Guidewire8RadioButton radio_SellingChargingLiquor() {
		return new Guidewire8RadioButton(driver, "//table[contains(@id, ':BOPAdditionalCoverageLiquorCatDV:LiquorLiabilityQuestion')]");
	}

	// @author ecoleman
	private Guidewire8Checkbox checkbox_LiquorLiabilityCoverage() {
		return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BusinessOwnersLiquorCatPanel:BOPAdditionalCoverageLiquorCatDV:1:CoverageInputSet')]");
	}

	// @author ecoleman
	public Guidewire8Select select_LiquorSalesType() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'CoverageInputSet:CovPatternInputGroup:BOPLiquorLiabilitySalesType-triggerWrap')]");
	}

	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------


	private void checkBusinessownersLineAdditionalCoveragesEmployeeDishonestyCheckbox(Boolean trueFalseChecked) {
		checkbox_SubmissionBusinessownersLineAdditionalCoveragesEmployeeDishonesty().select(trueFalseChecked);
	}


	private void setBusinessownersLineAdditionalCoveragesEmployeeDishonestyCoverageLimit(EmpDishonestyLimit coverageLimit) {
		Guidewire8Select mySelect = select_SubmissionBusinessownersLineAdditionalCoveragesEmployeeDishonestyCoverageLimit();
		mySelect.selectByVisibleText(coverageLimit.getValue());
	}


	private void setBusinessownersLineAdditionalCoveragesEmployeeDishonestyNumberEmployees(int numberEmployees) {
		waitUntilElementIsVisible(editBox_SubmissionBusinessownersLineAdditionalCoveragesEmployeeDishonestyNumberOfCoveredEmployees);
		editBox_SubmissionBusinessownersLineAdditionalCoveragesEmployeeDishonestyNumberOfCoveredEmployees.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editBox_SubmissionBusinessownersLineAdditionalCoveragesEmployeeDishonestyNumberOfCoveredEmployees.sendKeys(String.valueOf(numberEmployees));
	}


	private void setBusinessownersLineAdditionalCoveragesEmployeeDishonestyReferencesChecked(boolean radioValue) {
		radio_EmployeeDishonestyReferencesChecked().select(radioValue);
	}


	private void setBusinessownersLineAdditionalCoveragesEmployeeDishonestAuditsConductedFrequency(EmployeeDishonestyAuditsConductedFrequency auditFrequency) {
		waitUntilElementIsVisible(editBox_SubmissionBusinessownersLineAdditionalCoveragesEmployeeDishonestyAuditsConductedFrequency).sendKeys(auditFrequency.getValue());
	}


	private void setBusinessownersLineAdditionalCoveragesEmployeeDishonestyAuditsPerformedBy(
			EmployeeDishonestyAuditsPerformedBy auditPerformedBy) {
		Guidewire8Select mySelect = select_SubmissionBusinessownersLineAdditionalCoveragesEmployeeDishonestyAuditsPerformedBy();
		mySelect.selectByVisibleText(auditPerformedBy.getValue());
	}


	private void setBusinessownersLineAdditionalCoveragesEmployeeDishonestySeperateCheckReconciliation(
			boolean radioValue) {
		radio_EmployeeDishonestySeperateCheckReconciliation().select(radioValue);
	}


	private void setBusinessownersLineAdditionalCoveragesEmployeeDishonestyChecksMultipleSignatures(boolean radioValue) {
		radio_EmployeeDishonestyChecksMultipleSignatures().select(radioValue);
	}

	private void checkBusinessownersLineAdditionalCoveragesEmployeeDishonesty(boolean trueFalseChecked,
			EmpDishonestyLimit coverageLimit, int numberEmployees, boolean empReferencesChecked,
			EmployeeDishonestyAuditsConductedFrequency auditFrequency,
			EmployeeDishonestyAuditsPerformedBy auditsPerformedBy, boolean seperateCheckReconciliation,
			boolean multipleSignatures) {
		if (trueFalseChecked) {
			checkBusinessownersLineAdditionalCoveragesEmployeeDishonestyCheckbox(true);
			setBusinessownersLineAdditionalCoveragesEmployeeDishonestyCoverageLimit(coverageLimit);
			setBusinessownersLineAdditionalCoveragesEmployeeDishonestyNumberEmployees(numberEmployees);
			setBusinessownersLineAdditionalCoveragesEmployeeDishonestyReferencesChecked(empReferencesChecked);
			setBusinessownersLineAdditionalCoveragesEmployeeDishonestAuditsConductedFrequency(auditFrequency);
			setBusinessownersLineAdditionalCoveragesEmployeeDishonestyAuditsPerformedBy(auditsPerformedBy);
			setBusinessownersLineAdditionalCoveragesEmployeeDishonestySeperateCheckReconciliation(
					seperateCheckReconciliation);
			setBusinessownersLineAdditionalCoveragesEmployeeDishonestyChecksMultipleSignatures(multipleSignatures);
		} else {
			checkBusinessownersLineAdditionalCoveragesEmployeeDishonestyCheckbox(false);
		}
	}


	private void checkBusinessownersLineAdditionalCoveragesHiredAutoCheckbox(Boolean trueFalseChecked) {
		checkbox_SubmissionBusinessownersLineAdditionalCoveragesHiredAuto().select(trueFalseChecked);
	}


	private void setBusinessownersLineAdditionalCoveragesApplicantHaveOwnedAuto(boolean yesno) {
		radio_ApplicantHaveOwnedAuto().select(yesno);
	}


	private void checkBusinessownersLineAdditionalCoveragesNonOwnedAutoLiability(Boolean trueFalseChecked) {
		checkbox_SubmissionBusinessownersLineAdditionalCoveragesNonOwnedAutoLiability().select(trueFalseChecked);
	}


	private void setBusinessownersLineAdditionalCoveragesDeliveryServiceWithNonCompnayVehicle(boolean yesno) {
		radio_DeliveryServiceWithNonCompnayVehicle().select(yesno);
	}

	private void setBusinessownersLineAdditionalCoveragesSellingChargingLiquor(boolean radioValue) {
		radio_SellingChargingLiquor().select(radioValue);

	}

	private void checkBusinessownersLineAdditionalCoveragesBeautySalonsAndBarberShops(boolean trueFalseChecked) {
		checkbox_SubmissionBusinessownersLineAdditionalCoveragesBeautySalonsAndBarberShops().select(trueFalseChecked);
	}


	private void set_BeautySalonApplicantsExposure(SalonTypeOfOperation salonTypeOfOperation) {
		Guidewire8Select mySelect = select_BeautySalonApplicantsExposure();
		mySelect.selectByVisibleText(salonTypeOfOperation.getValue());
	}


	private void set_NumberOfOperators(int operators) {
		editBox_SubmissionBusinessownersLineAdditionalCoveragesBeautySalonNumberOfOperators
		.sendKeys(String.valueOf(operators));
	}


	private void set_NumberOfStations(int stations) {
		editBox_SubmissionBusinessownersLineAdditionalCoveragesBeautySalonNumberofStations
		.sendKeys(String.valueOf(stations));
	}


	private void checkBusinessownersLineAdditionalCoveragesElectronicData(boolean trueFalseChecked) {
		checkbox_SubmissionBusinessownersLineAdditionalCoveragesElectronicData().select(trueFalseChecked);
	}


	private void set_ElectronicDataLimit(long dataLimit) {
		editBox_SubmissionBusinessownersLineAdditionalCoveragesElectronicDataLimit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editBox_SubmissionBusinessownersLineAdditionalCoveragesElectronicDataLimit.sendKeys(String.valueOf(dataLimit));
		clickProductLogo();
	}


	private void checkBusinessownersLineAdditionalCoveragesInsuranceToValue(boolean trueFalseChecked) {
		checkbox_SubmissionBusinessownersLineAdditionalCoveragesInsuranceToValue().select(trueFalseChecked);
	}


	private void checkBusinessownersLineAdditionalCoveragesAdditionalInsuredEngineers(boolean trueFalseChecked) {
		checkbox_SubmissionBusinessownersLineAdditionalCoveragesAdditionalInsuredEngineers().select(trueFalseChecked);
	}
}
