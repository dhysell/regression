package repository.pc.workorders.generic;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_Deductible;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_NumberLocations;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderGeneralLiabilityCoverages_AdditionalCoverages extends BasePage {

	private TableUtils tableUtils;
	private WebDriver driver;
    public GenericWorkorderGeneralLiabilityCoverages_AdditionalCoverages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    Guidewire8Checkbox checkbox_AdditionalInsured_ChurchMembersAndOfficers_CG2022() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Additional Insured - Church Members And Officers CG 20 22')]/preceding-sibling::table");
    }

    Guidewire8Select select_AICharitableInstitutions() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Additional Insured - Charitable Institutions CG 20 20')]/preceding-sibling::table");
    }

    Guidewire8Select select_AIClubMembers() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Additional Insured - Club Members CG 20 02')]/preceding-sibling::table");
    }

    Guidewire8Select select_AIEngineersArchitectsOrSurveyors() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Additional Insured - Engineers, Architects Or Surveyors CG 20 07')]/preceding-sibling::table");
    }

    Guidewire8Select select_AIUsersOfGolfmobiles() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Additional Insured - Users Of Golfmobiles CG 20 08')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_AIUsersOfTeamsDraftOrSaddleAnimals() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Additional Insured - Users Of Team, Draft Or Saddle Animals CG 20 14')]/preceding-sibling::table");
    }

    Guidewire8Select select_AmendmentOfLiquorLiabilityExclusionExceptionForScheduledActivities() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Amendment Of Liquor Liability Exclusion - Exception For Scheduled Activities CG 21 51')]/preceding-sibling::table");
    }

    Guidewire8Select select_DesignatedConstructionProjectGeneralAggregateLimit() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Designated Construction Project(s) General Aggregate Limit CG 25 03')]/preceding-sibling::table");
    }

    Guidewire8Select select_DesignatedLocationGeneralAggregateLimit() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Designated Location(s) General Aggregate Limit CG 25 04')]/preceding-sibling::table");
    }

    Guidewire8Select select_EmploymentPracticesLiabilityInsurance() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Employment Practices Liability Insurance IDCG 31 2013')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_LimitedCoverageForDesignatedUnmannedAircraft() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Limited Coverage For Designated Unmanned Aircraft CG 24 50')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_EmploymentPracticesLiabilityInsuranceIDCG312013() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Employment Practices Liability Insurance IDCG 31 2013')]/preceding-sibling::table");
    }


    public void setEmploymentPracticesLiabilityInsurance_IDCG_31_2013(boolean checked) {
        checkbox_EmploymentPracticesLiabilityInsuranceIDCG312013().select(checked);
    }

    @FindBy(xpath = "//div[contains(text(), 'Employment Practices Liability Insurance IDCG 31 2013')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Number of full time employees')]/parent::td/following-sibling::td/input")
    private WebElement editbox_LiabilityPracticesNumberOfFullTimeEmployees;

    @FindBy(xpath = "//div[contains(text(), 'Employment Practices Liability Insurance IDCG 31 2013')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Number of part time employees')]/parent::td/following-sibling::td/input")
    private WebElement editbox_LiabilityPracticesNumberOfPartTimeEmployees;

    Guidewire8Select select_EmploymentPracticesAggregateLimit() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Employment Practices Liability Insurance IDCG 31 2013')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Aggregate limit')]/parent::td/following-sibling::td/table");
    }

    Guidewire8Select select_EmploymentPracticesDeductible() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Employment Practices Liability Insurance IDCG 31 2013')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Deductible')]/parent::td/following-sibling::td/table");
    }

    @FindBy(xpath = "//div[contains(text(), 'Employment Practices Liability Insurance IDCG 31 2013')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Retroactive Date')]/parent::td/following-sibling::td/input")
    private WebElement editbox_EPLI_RetroactiveDate;

    Guidewire8RadioButton radio_EmploymentPracticesThirdPartyLiability() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Employment Practices Liability Insurance IDCG 31 2013')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Third party violations')]/parent::td/following-sibling::td/div/table");
    }

    @FindBy(xpath = "//div[contains(text(), 'Employment Practices Liability Insurance IDCG 31 2013')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Hand rated')]/parent::td/following-sibling::td/input")
    private WebElement editbox_LiabilityPracticesHandRated;

    Guidewire8Select select_EmploymentPracticesTotalNumberLocations() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Employment Practices Liability Insurance IDCG 31 2013')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Total number of locations')]/parent::td/following-sibling::td/table");
    }


    @FindBy(xpath = "//div[contains(text(), 'Limited Coverage For Designated Unmanned Aircraft CG 24 50')]/ancestor::fieldset//input[contains(@id, 'CovTermInputSet:DirectTermInput-inputEl')]")
    private WebElement editbox_LimitedCoverageForDesignatedUnmannedAircraft_Limit;

    @FindBy(xpath = "//div[contains(text(), 'Limited Coverage For Designated Unmanned Aircraft CG 24 50')]/ancestor::fieldset//span[contains(@id,':Add-btnInnerEl')]")
    private WebElement link_Add_LimitedCoverageForDesignatedUnmannedAircraft;

    @FindBy(xpath = "//div[contains(text(), 'Limited Coverage For Designated Unmanned Aircraft CG 24 50')]/ancestor::fieldset//span[contains(@id,':Remove-btnInnerEl')]")
    private WebElement link_Remove_LimitedCoverageForDesignatedUnmannedAircraft;

    @FindBy(xpath = "//div[contains(text(), 'Limited Coverage For Designated Unmanned Aircraft CG 24 50')]/ancestor::fieldset//div[contains(@id,':LineCovs:CoverageInputSet:CovPatternInputGroup:ScheduleInputSet')]")
    public WebElement table_LimitedCoverageForDesignatedUnmannedAircraft;

    @FindBy(xpath = "//div[contains(text(), 'Contractual Liability - Railroads CG 24 17')]/ancestor::fieldset//span[contains(@id,':Remove-btnInnerEl')]")
    private WebElement link_Remove_ContractualLiability_Railroads;


    Guidewire8Select select_LiquorLiabilityBringYourOwnAlcoholEstablishments() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Liquor Liability - Bring Your Own Alcohol Establishments CG 24 06')]/preceding-sibling::table");
    }

    Guidewire8Select select_LiquorLiabilityCoverageForm() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Liquor Liability Coverage Form CG 00 33')]/preceding-sibling::table");
    }

    Guidewire8Select select_PrimaryAndNoncontributoryOtherInsuranceCondition() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Primary And Noncontributory - Other Insurance Condition CG 20 01')]/preceding-sibling::table");
    }


    public void checkAdditionalInsured_ChurchMembersAndOfficers_CG2022(boolean checked) {
        checkbox_AdditionalInsured_ChurchMembersAndOfficers_CG2022().select(checked);
    }


    public void checkAIUsersOfTeamsDraftOrSaddleAnimalsCG2014(boolean checked) {
        checkbox_AIUsersOfTeamsDraftOrSaddleAnimals().select(checked);
    }


    public void checkEmploymentPracticesLiabilityInsuranceIDCG312013(boolean checked) {
        checkbox_EmploymentPracticesLiabilityInsuranceIDCG312013().select(checked);
    }


    public void setLiabilityPracticesNumberOfFullTimeEmployees(int fullTimeEmployees) {
        clickWhenClickable(editbox_LiabilityPracticesNumberOfFullTimeEmployees);
        editbox_LiabilityPracticesNumberOfFullTimeEmployees.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_LiabilityPracticesNumberOfFullTimeEmployees.sendKeys(String.valueOf(fullTimeEmployees));
        clickProductLogo();
        
    }


    public void setLiabilityPracticesNumberOfPartTimeEmployees(int partTimeEmployees) {
        clickWhenClickable(editbox_LiabilityPracticesNumberOfPartTimeEmployees);
        
        editbox_LiabilityPracticesNumberOfPartTimeEmployees.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        editbox_LiabilityPracticesNumberOfPartTimeEmployees.sendKeys(String.valueOf(partTimeEmployees));
        clickProductLogo();
        
    }


    public void selectEmploymentPracticesAggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit aggregateLimit) {
        Guidewire8Select mySelect = select_EmploymentPracticesAggregateLimit();
        mySelect.selectByVisibleText(aggregateLimit.getValue());
        
    }


    public void selectEmploymentPracticesDeductible(EmploymentPracticesLiabilityInsurance_Deductible deductible) {
        Guidewire8Select mySelect = select_EmploymentPracticesDeductible();
        mySelect.selectByVisibleText(deductible.getValue());
        
    }


    public void setRadioEmploymentPracticesThirdPartyLiability(boolean yesNo) {
        radio_EmploymentPracticesThirdPartyLiability().select(yesNo);
        
    }


    public void setLiabilityPracticesHandrated(String handRated) {
        clickWhenClickable(editbox_LiabilityPracticesHandRated);
        editbox_LiabilityPracticesHandRated.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_LiabilityPracticesHandRated.sendKeys(handRated);
    }


    public void selectEmploymentPracticesTotalNumberLocations(EmploymentPracticesLiabilityInsurance_NumberLocations numLocations) {
        Guidewire8Select mySelect = select_EmploymentPracticesTotalNumberLocations();
        mySelect.selectByVisibleText(numLocations.getValue());
        
    }


    public void checkLimitedCoverageForDesignatedUnmannedAircraftCG2450(boolean checked) {
        checkbox_LimitedCoverageForDesignatedUnmannedAircraft().select(checked);
    }


    //JLARSEN 10/169/2017
    //FILED REMOVED FORM UI
//	@FindBy (xpath = "//div[contains(text(), 'Liquor Liability Coverage Form CG 00 33')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Sales')]/parent::td/following-sibling::td/input")
//	private WebElement editbox_LiquorLiabilityCoverageFormCG0033_Sales;


//	
//	public void setLiquorLiabilityCoverageFormCG0033_Sales(double sales) {
//		clickWhenClickable(editbox_LiquorLiabilityCoverageFormCG0033_Sales);
//		
//		editbox_LiquorLiabilityCoverageFormCG0033_Sales.sendKeys(Keys.chord(Keys.CONTROL + "a"));
//		
//		editbox_LiquorLiabilityCoverageFormCG0033_Sales.sendKeys(String.valueOf(sales));
//		
//		clickWhenClickable(find(By.xpath("//div[contains(text(), 'Liquor Liability Coverage Form CG 00 33')]")));
////		setText(editbox_LiquorLiabilityCoverageFormCG0033_Sales, String.valueOf(sales));
//		
//	}


//	@FindBy (xpath = "//div[contains(text(), 'Liquor Liability Coverage Form CG 00 33')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Tasting Room')]/parent::td/following-sibling::td/input")
//	private WebElement editbox_LiquorLiabilityCoverageFormCG0033_TestingRooms;
//	
//	
//	public void setLiquorLiabilityCoverageFormCG0033_TestingRooms(int tastingRooms) {
//		clickWhenClickable(editbox_LiquorLiabilityCoverageFormCG0033_TestingRooms);
//		
//		editbox_LiquorLiabilityCoverageFormCG0033_TestingRooms.sendKeys(Keys.chord(Keys.CONTROL + "a"));
//		
//		editbox_LiquorLiabilityCoverageFormCG0033_TestingRooms.sendKeys(String.valueOf(tastingRooms));
//		
//		clickWhenClickable(find(By.xpath("//div[contains(text(), 'Liquor Liability Coverage Form CG 00 33')]")));
////		setText(editbox_LiquorLiabilityCoverageFormCG0033_TestingRooms, String.valueOf(tastingRooms));
//		
//	}


    public void setUnmannedAircraftDescriptionCG2450(String descriptionOfUnmannedAircraft, String descriptionOfOperationsOrProject, int limit) {
        editbox_LimitedCoverageForDesignatedUnmannedAircraft_Limit.click();
        
        editbox_LimitedCoverageForDesignatedUnmannedAircraft_Limit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        editbox_LimitedCoverageForDesignatedUnmannedAircraft_Limit.sendKeys(String.valueOf(limit));
        
        clickWhenClickable(link_Add_LimitedCoverageForDesignatedUnmannedAircraft);
        
        tableUtils.clickCellInTableByRowAndColumnName(table_LimitedCoverageForDesignatedUnmannedAircraft, tableUtils.getRowCount(table_LimitedCoverageForDesignatedUnmannedAircraft), "Description of Unmanned Aircraft - Make, Model, and Serial Number");
        tableUtils.setValueForCellInsideTable(table_LimitedCoverageForDesignatedUnmannedAircraft, "c1", descriptionOfUnmannedAircraft);
        
        tableUtils.clickCellInTableByRowAndColumnName(table_LimitedCoverageForDesignatedUnmannedAircraft, tableUtils.getRowCount(table_LimitedCoverageForDesignatedUnmannedAircraft), "Description of Operation(s) or Project(s)");
        tableUtils.setValueForCellInsideTable(table_LimitedCoverageForDesignatedUnmannedAircraft, "c2", descriptionOfOperationsOrProject);
    }


    public void setEmploymentPracticesLiabilityInsuranceIDCG312013(int fullTimeEmployees, int partTimeEmployees, EmploymentPracticesLiabilityInsurance_AggregateLimit aggregateLimit, EmploymentPracticesLiabilityInsurance_Deductible deductible, boolean yesNo, String handRated, EmploymentPracticesLiabilityInsurance_NumberLocations numLocations) {
        selectEmploymentPracticesTotalNumberLocations(numLocations);
        setRadioEmploymentPracticesThirdPartyLiability(yesNo);
        selectEmploymentPracticesAggregateLimit(aggregateLimit);
        if (aggregateLimit.equals(EmploymentPracticesLiabilityInsurance_AggregateLimit.TenThousand) || aggregateLimit.equals(EmploymentPracticesLiabilityInsurance_AggregateLimit.SeventyFiveThousand) || aggregateLimit.equals(EmploymentPracticesLiabilityInsurance_AggregateLimit.FiveHundredThousand) || aggregateLimit.equals(EmploymentPracticesLiabilityInsurance_AggregateLimit.OneMillion)) {
            //NO EPLI OPTINS AVAILABLE
        } else if (aggregateLimit.equals(EmploymentPracticesLiabilityInsurance_AggregateLimit.TwentyFiveThousand) || aggregateLimit.equals(EmploymentPracticesLiabilityInsurance_AggregateLimit.FiftyThousand)) {
            selectEmploymentPracticesDeductible(EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand);
        } else if (aggregateLimit.equals(EmploymentPracticesLiabilityInsurance_AggregateLimit.OneHundredThousand)) {
            selectEmploymentPracticesDeductible(EmploymentPracticesLiabilityInsurance_Deductible.TenThousand);
        } else if (aggregateLimit.equals(EmploymentPracticesLiabilityInsurance_AggregateLimit.TwoHundredFiftyThousand)) {
            selectEmploymentPracticesDeductible(EmploymentPracticesLiabilityInsurance_Deductible.TwentyFiveThousand);
        }
        setLiabilityPracticesNumberOfFullTimeEmployees(fullTimeEmployees);
        setLiabilityPracticesNumberOfPartTimeEmployees(partTimeEmployees);
    }

    @FindBy(xpath = "//div[contains(text(), 'Contractual Liability - Railroads CG 24 17')]/ancestor::fieldset//input[contains(@id, 'CovTermInputSet:DirectTermInput-inputEl')]")
    private WebElement editbox_ContractualLiability_Railroads_Premium;

    @FindBy(xpath = "//div[contains(text(), 'Contractual Liability - Railroads CG 24 17')]/ancestor::fieldset//span[contains(@id,':Add-btnInnerEl')]")
    private WebElement link_Add_ContractualLiability_Railroads;

    @FindBy(xpath = "//div[contains(text(), 'Contractual Liability - Railroads CG 24 17')]/ancestor::fieldset//div[contains(@id,':LineCovs:CoverageInputSet:CovPatternInputGroup:ScheduleInputSet')]")
    public WebElement table_ContractualLiability_Railroads;


    /*
     * Contractual Liability - Railroads CG 24 17
     */

    public void setContractualLiability_Railroads_CG_24_17(String scheduledRailroad, String designatedJobSite, int premium) {
        editbox_ContractualLiability_Railroads_Premium.click();
        
        editbox_ContractualLiability_Railroads_Premium.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        editbox_ContractualLiability_Railroads_Premium.sendKeys(String.valueOf(premium));
        
        clickWhenClickable(link_Add_ContractualLiability_Railroads);
        
        tableUtils.clickCellInTableByRowAndColumnName(table_ContractualLiability_Railroads, tableUtils.getRowCount(table_ContractualLiability_Railroads), "Scheduled Railroad");
        tableUtils.setValueForCellInsideTable(table_ContractualLiability_Railroads, "c1", scheduledRailroad);
        
        tableUtils.clickCellInTableByRowAndColumnName(table_ContractualLiability_Railroads, tableUtils.getRowCount(table_ContractualLiability_Railroads), "Designated Job Site");
        tableUtils.setValueForCellInsideTable(table_ContractualLiability_Railroads, "c2", designatedJobSite);
    }


}





















