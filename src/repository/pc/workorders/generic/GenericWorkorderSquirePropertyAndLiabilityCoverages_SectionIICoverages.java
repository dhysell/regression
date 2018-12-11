package repository.pc.workorders.generic;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.Property;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.SquireLiablityCoverageIncidentalOccupancyItem;
import repository.gw.generate.custom.SquireLiablityCoverageLivestockItem;
import repository.gw.generate.custom.SquireLiablityCoverageNamedPersonsMedicalPerson;
import repository.gw.generate.custom.SquireLiablityCoverageWatercraftLengthItem;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages extends GenericWorkorderSquirePropertyAndLiabilityCoverages {
	
	private WebDriver driver;
	private TableUtils tableUtils;
	
    public GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
        this.driver = driver;
    }


    public void fillOutSectionIIcoverages(GeneratePolicy policy) throws GuidewireNavigationException {

        clickSectionIICoveragesTab();

        setGeneralLiabilityLimit(policy.squire.propertyAndLiability.liabilitySection.getGeneralLiabilityLimit());
        setMedicalLimit(policy.squire.propertyAndLiability.liabilitySection.getMedicalLimit());
        if (!policy.squire.propertyAndLiability.liabilitySection.getSectionIICoverageList().isEmpty()) {
            addCoveragesFromList(policy.squire.propertyAndLiability.liabilitySection.getSectionIICoverageList());
            setQuantityAndAmountFromList(policy.squire.propertyAndLiability.liabilitySection.getSectionIICoverageList());
        }
    }


    @FindBy(xpath = "//span[contains(@id, 'LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:HOAdditionalCoveragesPanelSet:HOAdditionalCoveragesDV_tb:Add-btnEl')]")
    private WebElement button_AddCoverages;

    private Guidewire8Select select_GeneralLiabilityLimit() {
        return new Guidewire8Select(driver, "//fieldset//legend[.='General Liability']/following-sibling::div//table[contains(@id, ':PackageTermInput-triggerWrap')]");
    }

    private Guidewire8Select select_CustomFarmingFireLimit() {
        return new Guidewire8Select(driver, "//fieldset//legend[.='Custom Farming Fire']/following-sibling::div//table[contains(@id, ':OptionTermInput-triggerWrap')]");
    }

    private Guidewire8Select select_MedicalLimit() {
        return new Guidewire8Select(driver, "//fieldset//legend[.='Medical']/following-sibling::div//table[contains(@id, ':OptionTermInput-triggerWrap')]");
    }

    @FindBy(xpath = "//div[contains(@id, '0:PerOccurenceLimit-inputEl')]")
    private WebElement text_MedicalOccuranceLimit;

    @FindBy(xpath = "//fieldset//legend[.='General Liability (Acres)']/following-sibling::div//div[contains(@id,'DirectTermInput-inputEl')]")
    private WebElement text_TotalAcres;

    @FindBy(xpath = "//fieldset//legend[.='Additional Named Insured']/following-sibling::div//div[contains(@id,'DirectTermInput-inputEl')]")
    private WebElement text_Quantity;

    @FindBy(xpath = "//fieldset//legend[.='Additional Named Insured']")
    private WebElement text_AdditionalNamedInsured;

    private WebElement checkbox_CoverageListCoverage(SectionIICoveragesEnum coverage) {
        return find(By.xpath("//div[(text()='" + coverage.getValue() + "')]/parent::td/preceding-sibling::td/div/img"));
    }

    private WebElement checkbox_CoverageListCoverage(SectionIICoverages coverage) {
        return find(By.xpath("//div[(text()='" + coverage.getSectionIICoverage().getValue() + "')]/parent::td/preceding-sibling::td/div/img"));
    }

    @FindBy(xpath = "//a[contains(@id, 'CoveragePatternSearchPopup:CoveragePatternSearchScreen:CoveragePatternSearchResultsLV_tb:AddCoverageButton')]")
    private WebElement button_CoverageListAddSelectedCoverages;

    @FindBy(xpath = "//div[contains(., 'Watercraft Length')]/ancestor::legend/following-sibling::div/descendant::a/descendant::span[contains(@id, ':CovPatternInputGroup:ScheduleInputSet:Add-btnEl')]")
    private WebElement button_WatercraftLengthAdd;

    @FindBy(xpath = "//div[contains(., 'Incidental Occupancy')]/ancestor::legend/following-sibling::div/descendant::a/descendant::span[contains(@id, ':CovPatternInputGroup:ScheduleInputSet:Add-btnEl')]")
    private WebElement button_IncidentalOccupancyAdd;

    @FindBy(xpath = "//div[contains(., 'Watercraft Length')]/ancestor::legend/following-sibling::div/descendant::table/tbody/tr/td/div")
    private WebElement html_WatercraftLengthScheduledItem;

    @FindBy(xpath = "//div[contains(., 'Incidental Occupancy')]/ancestor::legend/following-sibling::div/descendant::table/tbody/tr/td/div")
    private WebElement html_IncidentalOccupancyScheduledItem;

    private List<WebElement> editboxes_Quantity(SectionIICoveragesEnum coverage) {
        return finds(By.xpath("//div[text()='" + coverage.getValue() + "']/ancestor::legend//following-sibling::div/descendant::tbody/tr/td/label[contains(text(), 'Quantity')]/parent::td/following-sibling::td//input"));
    }

    private List<WebElement> editboxes_Amount(SectionIICoveragesEnum coverage) {
        return finds(By.xpath("//div[text()='" + coverage.getValue() + "']/ancestor::legend//following-sibling::div/descendant::tbody/tr/td/label[contains(text(), 'Amount')]/parent::td/following-sibling::td//input"));
    }

    @FindBy(xpath = "//legend[contains(@id, 'HOCoverageInputSet:CovPatternInputGroup-legend') and contains(., 'Livestock')]/following-sibling::div//a[contains(@id, ':HOCoverageInputSet:CovPatternInputGroup:ScheduleInputSet:Add')]")
    private WebElement button_AddLivestockSection2;

    @FindBy(xpath = "//legend[contains(@id, 'HOCoverageInputSet:CovPatternInputGroup-legend') and contains(., 'Named Persons Medical')]/following-sibling::div//a[contains(@id, ':HOCoverageInputSet:CovPatternInputGroup:AddPolicyMember')]")
    private WebElement button_NamedPersonsMedicalAddExisting;
    
    @FindBy(css = "a[id*='Remove']")
    private WebElement button_NamedPersonsMedicalRemove;
    
    @FindBy(css = "span[id*='addButton'] span span[id*='btnInner']")
    private WebElement button_NamedPersonsMedicalAddFreeForm;

    @FindBy(xpath = "//div[contains(., 'Livestock')]/ancestor::fieldset/legend/following-sibling::div//table/tbody/tr/td/div[contains(@id, ':HOCoverageInputSet:CovPatternInputGroup:ScheduleInputSet:0')]")
    private WebElement table_LivestockSection2;

    @FindBy(xpath = "//div[contains(@id, ':HOCoverageInputSet:CovPatternInputGroup:ScheduleInputSet:ScheduleLV')]")
    private WebElement table_PollutionLiability;

    @FindBy(xpath = "//div[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:_msgs')]")
    private WebElement message_CoveragePage;

    @FindBy(xpath = "//div[contains(., 'Horse Boarding')]/ancestor::fieldset/legend/span/div/table/tbody/tr/td[2]/div/input[contains(@id, ':HOCoverageInputSet:CovPatternInputGroup:_checkbox')]")
    private WebElement check_HorseBoardingCheckbox;

    @FindBy(xpath = "//div[contains(., 'Livestock')]/ancestor::fieldset/legend/span/div/table/tbody/tr/td[2]/div/input[contains(@id, ':HOCoverageInputSet:CovPatternInputGroup:_checkbox')]")
    private WebElement check_LivestockCheckbox;

    @FindBy(xpath = "//div[contains(., 'Custom Farming')]/ancestor::fieldset/legend/span/div/table/tbody/tr/td[2]/div/input[contains(@id, ':HOCoverageInputSet:CovPatternInputGroup:_checkbox')]")
    private WebElement check_CustomFarming;

    @FindBy(xpath = "//div[contains(., 'Custom Farming Fire')]/ancestor::fieldset/legend/span/div/table/tbody/tr/td[2]/div/input[contains(@id, ':HOCoverageInputSet:CovPatternInputGroup-legendChk')]")
    private WebElement check_CustomFarmingFire;

    @FindBy(xpath = "//div[contains(@id, ':HOCoverageInputSet:CovPatternInputGroup:limitTerm-inputEl')]")
    private WebElement NamedPersonsMedicalLimit;

    @FindBy(xpath = "//div[contains(@id, ':HOCoverageInputSet:CovPatternInputGroup:deductibleTerm-inputEl')]")
    private WebElement NamedPersonsMedicalDeductible;

    @FindBy(xpath = "//div[contains(@id, ':HOCoverageInputSet:CovPatternInputGroup:PerOccurenceLimit-inputEl')]")
    private WebElement NamedPersonsMedicalPerOccuranceLimit;
    
    @FindBy(css = "tbody table div[id*='ScheduleLV']")
    private WebElement NamedPersonsMedicalTable;

    private void clickWatercraftLengthAdd() {
        clickWhenClickable(button_WatercraftLengthAdd);
    }


    public void setWatercraftLength(String description, int length) {
        clickWatercraftLengthAdd();
        int currentRow = tableUtils.getNextAvailableLineInTable(html_WatercraftLengthScheduledItem);
        tableUtils.setValueForCellInsideTable(html_WatercraftLengthScheduledItem, currentRow, "Description", "c1", description);
        tableUtils.setValueForCellInsideTable(html_WatercraftLengthScheduledItem, currentRow, "Length", "c2", Integer.toString(length));
    }


    public void addIncidentalOccupancy(String desc, String address) {
        clickWhenClickable(button_IncidentalOccupancyAdd);
        
        tableUtils.setValueForCellInsideTable(html_IncidentalOccupancyScheduledItem, tableUtils.getNextAvailableLineInTable(html_IncidentalOccupancyScheduledItem), "Description", "c1", desc);
        
        tableUtils.selectValueForSelectInTable(html_IncidentalOccupancyScheduledItem, 1, "Residence", address);
        
    }

    /*
     * Methods
     */


    public void setGeneralLiabilityLimit(SectionIIGeneralLiabLimit limit) {
        select_GeneralLiabilityLimit().selectByVisibleText(limit.getValue());
    }


    public String getGeneralLiabilityLimit() {
        return select_GeneralLiabilityLimit().getText();
    }


    public void setMedicalLimit(Property.SectionIIMedicalLimit limit) {
        select_MedicalLimit().selectByVisibleText(limit.getValue());
    }


    public String getMedicalLimit() {
        return select_MedicalLimit().getText();
    }


    public int getTotalAcres() {
        return Integer.valueOf(text_TotalAcres.getText());
    }


    public int getAdditionalNamedInsuredQuantity() {
        return Integer.valueOf(text_Quantity.getText());
    }


    public void addCoverages(SectionIICoverages coverage) {
        clickWhenClickable(button_AddCoverages);
        clickSearch();
        clickWhenClickable(checkbox_CoverageListCoverage(coverage));
        clickWhenClickable(button_CoverageListAddSelectedCoverages);
    }


    public void addCoveragesFromList(List<SectionIICoverages> coverages) throws GuidewireNavigationException {
		clickWhenClickable(button_AddCoverages);
		clickSearch();
		for (SectionIICoverages coverage : coverages) {
			clickWhenClickable(checkbox_CoverageListCoverage(coverage.getSectionIICoverage()));
		}
		clickWhenClickable(button_CoverageListAddSelectedCoverages);
		new GuidewireHelpers(getDriver()).isOnPage("//div[text()='General Liability']", 5000, "UNABLE TO GET TO SECITON 2 COVERAGE PAGE AFTER ADDING COVERAGES.");
	}


    public void setQuantity(SectionIICoverages coverage) {
        if (coverage.getSectionIICoverage() == SectionIICoveragesEnum.BuffaloAndElk) {
            Assert.fail("Buffalo and Elk has two quantities... You need to use the specialized method");
        } else {
            
            clickWhenClickable(editboxes_Quantity(coverage.getSectionIICoverage()).get(0));
            
            editboxes_Quantity(coverage.getSectionIICoverage()).get(0).sendKeys(Keys.chord(Keys.CONTROL + "a"));
            editboxes_Quantity(coverage.getSectionIICoverage()).get(0).sendKeys(String.valueOf(coverage.getQuantity()));
            editboxes_Quantity(coverage.getSectionIICoverage()).get(0).sendKeys(Keys.TAB);
            
        }
    }


    public void setQuantityAndAmountFromList(List<SectionIICoverages> coverages) {

        for (SectionIICoverages coverage : coverages) {
            switch (coverage.getSectionIICoverage()) {
                case AllTerrainVehicles:
                case ChildCare:
                case EmployersNOE1:
                case EmployersNOE2:
                case FeedlotCustomFarmingEndo258:
                case GolfCart:
                case HorseBoarding:
                case HorseBoardingAndPasturing:
                case MotorBoats:
                case OffRoadMotorcycles:
                case PersonalWatercraft:
                case PrivateLandingStrips:
                case Sailboats:
                case Snowmobiles:
                    setQuantity(coverage);
                    break;
                case BuffaloAndElk:
                    setQuantityBuffaloElk(coverage.getQuantity(), coverage.getLimit());
                    break;
                case CustomFarming:
                    setAmount(coverage);
                    break;
                case IncidentalOccupancy:
                    for (SquireLiablityCoverageIncidentalOccupancyItem item : coverage.getCoverageIncidentalOccupancyItems()) {
                        addIncidentalOccupancy(item.getDescription(), "Residence Premises #1 at " + item.getLocation().getAddress().getLine1() + ", " + item.getLocation().getAddress().getCity() + ", " + item.getLocation().getAddress().getState().getAbbreviation());
                    }
                    break;
                case Livestock:
                    for (SquireLiablityCoverageLivestockItem livestock : coverage.getCoverageLivestockItems()) {
                        setLivestockTypeAndQuantity(livestock.getType(), livestock.getQuantity());
                    }
                    break;
                case NamedPersonsMedical:
                    for (SquireLiablityCoverageNamedPersonsMedicalPerson person : coverage.getCoverageNamedPersonsMedicalPersons()) {
                        setNamedPersonsMedical(person.getFullName());
                    }
                    break;
                case CropDustingAndSpraying:
                case RecreationalVehicles:
                case SeedsmanEAndO:
                case CustomFarmingFire:
                    //NONE
                    break;
                case WatercraftLength:
                    for (SquireLiablityCoverageWatercraftLengthItem watercraft : coverage.getCoverageWatercraftLengthItems()) {
                        setWatercraftLength(watercraft.getDescription(), watercraft.getLength());
                    }
                    break;
            }

        }
    }


	public void setAmount(SectionIICoverages coverage) {
		clickWhenClickable(editboxes_Amount(coverage.getSectionIICoverage()).get(0));
		editboxes_Amount(coverage.getSectionIICoverage()).get(0).sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editboxes_Amount(coverage.getSectionIICoverage()).get(0).sendKeys(coverage.getLimit()+"");
	}


    public void setQuantityBuffaloElk(int buffaloQuantity, int elkQuantity) {
        clickWhenClickable(editboxes_Quantity(SectionIICoveragesEnum.BuffaloAndElk).get(0));
        
        editboxes_Quantity(SectionIICoveragesEnum.BuffaloAndElk).get(0).sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editboxes_Quantity(SectionIICoveragesEnum.BuffaloAndElk).get(0).sendKeys(String.valueOf(buffaloQuantity));
        editboxes_Quantity(SectionIICoveragesEnum.BuffaloAndElk).get(0).sendKeys(Keys.TAB);
        

        clickWhenClickable(editboxes_Quantity(SectionIICoveragesEnum.BuffaloAndElk).get(1));
        
        editboxes_Quantity(SectionIICoveragesEnum.BuffaloAndElk).get(1).sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editboxes_Quantity(SectionIICoveragesEnum.BuffaloAndElk).get(1).sendKeys(String.valueOf(elkQuantity));
        editboxes_Quantity(SectionIICoveragesEnum.BuffaloAndElk).get(1).sendKeys(Keys.TAB);
        
    }


    public void addWatercraftLengthCoverage(String description, int length) {
        addCoverages(new SectionIICoverages(SectionIICoveragesEnum.WatercraftLength, 0, 0));
        setWatercraftLength(description, length);
        
        clickNext();
    }


    public void setLivestockTypeAndQuantity(LivestockScheduledItemType type, int quantity) {
		if(finds(By.xpath("//div[contains(@id, ':ScheduleInputSet:0-body')]/div/table/tbody/child::tr/child::td/div[text()='" + type.getSection2Value() + "']")).isEmpty()) {
			clickWhenClickable(button_AddLivestockSection2);
			int numRow = tableUtils.getNextAvailableLineInTable(table_LivestockSection2, "Quantity");
			String forDropdown = type.getSection2Value();
			tableUtils.selectValueForSelectInTable(table_LivestockSection2, numRow, "Livestock", forDropdown);
			tableUtils.setValueForCellInsideTable(table_LivestockSection2, numRow, "Quantity", "c2", String.valueOf(quantity));
		}
	}


    public String getCoveragesMessage() {
        return message_CoveragePage.getText();
    }


    public void clickLiveStockCheckBox() {
        clickWhenClickable(check_LivestockCheckbox);
    }


    public void clickCustomFarmingCheckBox() {
        clickWhenClickable(check_CustomFarming);
    }


    public boolean checkCustomFarmingFireExists() {
        return checkIfElementExists(check_CustomFarmingFire, 1000);
    }


    public void clickHorseBoardingCheckbox() {
        clickWhenClickable(check_HorseBoardingCheckbox);
    }


    public void clicknext() {
        
        clickNext();
    }


    public void setCustomFarmingFireLimit(String limit) {
        select_CustomFarmingFireLimit().selectByVisibleTextPartial(limit);
    }


    public String getPollutionLiabilityTableByRowColumnName(int row, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PollutionLiability, row, columnName);

    }


    public void clickPollutionLiabilityTableCheckbox(int row, boolean trueFalse) {
        tableUtils.clickFirstRowInTable(table_PollutionLiability);
        tableUtils.clickCellInTableByRowAndColumnName(table_PollutionLiability, row, "Select");
        tableUtils.setCheckboxInTable(table_PollutionLiability, row, trueFalse);
    }


    public void setPollutionLiabilityQuantity(int row, int qunatity) {
        
        tableUtils.clickCellInTableByRowAndColumnName(table_PollutionLiability, row, "Quantity");
        WebElement editquantity = table_PollutionLiability.findElement(By.xpath(".//input[contains(@name,'Quantity')]"));
        editquantity.click();
        editquantity.clear();
        editquantity.sendKeys(String.valueOf(qunatity));
        
    }


    public void setNamedPersonsMedical(String insLastName) {
        clickWhenClickable(button_NamedPersonsMedicalAddExisting);
        String nameXpath = "//span[contains(text(), '" + insLastName + "') and contains(@id, ':ExistingAdditionalInterest-textEl')]";
        WebElement existingContact = find(By.xpath(nameXpath));
        //hoverOverFirstToClickSecond(button_NamedPersonsMedicalAddExisting, nameXpath);
        clickWhenClickable(existingContact);
    }

    public String getNamedPersonsMedicalLimit() {
        return NamedPersonsMedicalLimit.getText();
    }


    public String getNamedPersonsMedicalDeductible() {
        return NamedPersonsMedicalDeductible.getText();
    }
    
    public void clickRemoveNamedPersonsMedicalButton() {
    	clickWhenClickable(button_NamedPersonsMedicalRemove);
    }
    
    public void checkBoxInNamedPersonsMedicalTable(String insLastName) {
    	clickWhenClickable(tableUtils.getCheckboxWebElementByTextInTable(NamedPersonsMedicalTable, insLastName));
    }


    public boolean checkAdditionalNamedInsuredExists() {

        return checkIfElementExists(text_AdditionalNamedInsured, 1000);
    }


    public String getMedicalOccuranceLimit() {
        return text_MedicalOccuranceLimit.getText();
    }


    public String getNamedPersonsMedicalPerOccuranceLimit() {
        return NamedPersonsMedicalPerOccuranceLimit.getText();
    }
}
