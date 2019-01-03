package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderModifiers extends GenericWorkorder {

    private TableUtils tableUtils;
    private WebDriver driver;

    public GenericWorkorderModifiers(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//div[contains(text(), 'Building features')]/parent::td/following-sibling::td[3]/child::div")
    public WebElement editbox_ModifiersBuildingFeaturesCreditDebit;

    @FindBy(xpath = "//div[contains(text(), 'Building features')]/parent::td/following-sibling::td[4]/child::div")
    public WebElement editbox_ModifiersBuildingFeaturesJustification;

    @FindBy(xpath = "//div[contains(text(), 'Premises and equipment')]/parent::td/following-sibling::td[3]/child::div")
    public WebElement editbox_ModifiersPremisesEquipmentCreditDebit;

    @FindBy(xpath = "//div[contains(text(), 'Premises and equipment')]/parent::td/following-sibling::td[4]/child::div")
    public WebElement editbox_ModifiersPremisesEquipmentJustification;

    @FindBy(xpath = "//div[contains(text(), 'Management')]/parent::td/following-sibling::td[3]/child::div")
    public WebElement editbox_ModifiersManagementCreditDebit;

    @FindBy(xpath = "//div[contains(text(), 'Management')]/parent::td/following-sibling::td[4]/child::div")
    public WebElement editbox_ModifiersManagementJustification;

    @FindBy(xpath = "//div[contains(text(), 'Employees')]/parent::td/following-sibling::td[3]/child::div")
    public WebElement editbox_ModifiersEmployeesCreditDebit;

    @FindBy(xpath = "//div[contains(text(), 'Employees')]/parent::td/following-sibling::td[4]/child::div")
    public WebElement editbox_ModifiersEmployeesJustification;

    @FindBy(xpath = "//div[contains(text(), 'Loss Experience')]/parent::td/following-sibling::td[3]/child::div")
    public WebElement text_ModifiersLossExperienceCreditDebit;

    @FindBy(xpath = "//div[contains(text(), 'Loss Experience')]/parent::td/following-sibling::td[4]/child::div")
    public WebElement editbox_ModifiersLossExperienceJustification;

    @FindBy(xpath = "//div[contains(text(), 'Loss Experience')]/parent::td/following-sibling::td[4]/child::div")
    public WebElement editbox_EditableModifiersLossExperienceJustification;

    @FindBy(xpath = "//div[contains(text(), 'Additional Risk Elements')]/parent::td/following-sibling::td[4]/child::div")
    public WebElement editbox_ModifiersAdditionalRiskElementsJustification;

    //Table
    @FindBy(xpath = "//div[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:ModifiersScreen:ScheduleRateDV:0:ScheduleRateLV']")
    private WebElement table_RatingModifiers;

    @FindBy(xpath = "//div[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:PersonalLinesModifiersScreen:PersonalLinesRateModsDV:0:PersonalLinesRateModsLV']")
    private WebElement table_PLPolicyModifiers;

    @FindBy(xpath = "//div[@id='SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:PersonalLinesModifiersScreen:SquireRatePolicyModsDV:0:SquireRatePolicyModsLV']")
    private WebElement table_PLSquireSectionModifiers;


    public WebElement getRatingModifiersTable() {
        waitUntilElementIsVisible(table_RatingModifiers);
        return table_RatingModifiers;
    }


    public WebElement getPLPolicyModifiersTable() {
        waitUntilElementIsVisible(table_PLPolicyModifiers);
        return table_PLPolicyModifiers;
    }


    public WebElement getPLSquireSectionModifiersTable() {
        waitUntilElementIsVisible(table_PLSquireSectionModifiers);
        return table_PLSquireSectionModifiers;
    }


    public String getModifiersCreditDebitValueByColumnName(String category) {
        int row = tableUtils.getRowNumberInTableByText(table_RatingModifiers, category);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_RatingModifiers, row, "Credit(-)/Debit(+)");
    }


    public void setModifiersBuildingFeaturesCreditDebit(int creditDebit) {
        tableUtils.setValueForCellInsideTable(table_RatingModifiers, tableUtils.getRowNumberInTableByText(table_RatingModifiers, "Building features"), "Credit(-)/Debit(+)", "CreditDebit", String.valueOf(creditDebit));
    }


    public void setModifiersBuildingFeaturesJustification(String justification) {
        tableUtils.setValueForCellInsideTable(table_RatingModifiers, tableUtils.getRowNumberInTableByText(table_RatingModifiers, "Building features"), "Justification", "Justification", justification);
    }


    public void setModifiersPremisesEquipmentCreditDebit(int creditDebit) {
        tableUtils.setValueForCellInsideTable(table_RatingModifiers, tableUtils.getRowNumberInTableByText(table_RatingModifiers, "Premises and equipment"), "Credit(-)/Debit(+)", "CreditDebit", String.valueOf(creditDebit));
    }


    public void setModifiersPremisesEquipmentJustification(String justification) {
        tableUtils.setValueForCellInsideTable(table_RatingModifiers, tableUtils.getRowNumberInTableByText(table_RatingModifiers, "Premises and equipment"), "Justification", "Justification", justification);
    }

    public void setModifiersSpecialUnderwritingAdjustmentCreditDebit(int creditDebit) {
        tableUtils.setValueForCellInsideTable(table_PLPolicyModifiers, tableUtils.getRowNumberInTableByText(table_PLPolicyModifiers, "Special Underwriting Adjustment"), "Discounts (-) / Surcharges (+)", "c2", String.valueOf(creditDebit));
    }

    public void setModifiersSpecialUnderwritingAdjustmentJustification(String justification) {
        tableUtils.setValueForCellInsideTable(table_PLPolicyModifiers, tableUtils.getRowNumberInTableByText(table_PLPolicyModifiers, "Special Underwriting Adjustment"), "Justification", "c3", justification);
    }


    public void setModifiersManagementCreditDebit(int creditDebit) {
        tableUtils.setValueForCellInsideTable(table_RatingModifiers, tableUtils.getRowNumberInTableByText(table_RatingModifiers, "Management"), "Credit(-)/Debit(+)", "CreditDebit", String.valueOf(creditDebit));
    }


    public void setModifiersManagementJustification(String justification) {
        tableUtils.setValueForCellInsideTable(table_RatingModifiers, tableUtils.getRowNumberInTableByText(table_RatingModifiers, "Management"), "Justification", "Justification", justification);
    }


    public void setModifiersEmployeesCreditDebit(int creditDebit) {
        tableUtils.setValueForCellInsideTable(table_RatingModifiers, tableUtils.getRowNumberInTableByText(table_RatingModifiers, "Employees"), "Credit(-)/Debit(+)", "CreditDebit", String.valueOf(creditDebit));
    }


    public void setModifiersEmployeesJustification(String justification) {
        tableUtils.setValueForCellInsideTable(table_RatingModifiers, tableUtils.getRowNumberInTableByText(table_RatingModifiers, "Employees"), "Justification", "Justification", justification);
    }


    public void setModifiersLossExperienceCreditDebit(int creditDebit) {
        tableUtils.setValueForCellInsideTable(table_RatingModifiers, tableUtils.getRowNumberInTableByText(table_RatingModifiers, "Loss Experience"), "Credit(-)/Debit(+)", "CreditDebit", String.valueOf(creditDebit));
    }


    public void setModifiersLossExperienceJustification(String justification) {
        tableUtils.setValueForCellInsideTable(table_RatingModifiers, tableUtils.getRowNumberInTableByText(table_RatingModifiers, "Loss Experience"), "Justification", "Justification", justification);
    }


    public void setModifiersAdditionalRiskElementsCreditDebit(int creditDebit) {
        tableUtils.setValueForCellInsideTable(table_RatingModifiers, tableUtils.getRowNumberInTableByText(table_RatingModifiers, "Additional Risk Elements"), "Credit(-)/Debit(+)", "CreditDebit", String.valueOf(creditDebit));
    }


    public void setModifiersAdditionalRiskElementsJustification(String justification) {
        tableUtils.setValueForCellInsideTable(table_RatingModifiers, tableUtils.getRowNumberInTableByText(table_RatingModifiers, "Additional Risk Elements"), "Justification", "Justification", justification);
    }


    public void setRandomModifiersValues() {
        setModifiersBuildingFeaturesCreditDebit(NumberUtils.generateRandomNumberInt(-2, 2));
        
        setModifiersBuildingFeaturesJustification("Building Features Justification");
        
        setModifiersPremisesEquipmentCreditDebit(NumberUtils.generateRandomNumberInt(-5, 5));
        
        setModifiersPremisesEquipmentJustification("Premises and Equipment Justification");
        
        setModifiersManagementCreditDebit(NumberUtils.generateRandomNumberInt(-6, 6));
        
        setModifiersManagementJustification("Management Justification");
        
        setModifiersEmployeesCreditDebit(NumberUtils.generateRandomNumberInt(-2, 2));
        
        setModifiersEmployeesJustification("Employees Justification");
        

        if (!text_ModifiersLossExperienceCreditDebit.getText().equals("0")) {
            setModifiersLossExperienceJustification("Loss Experience Justification");
        }
        
        if (!text_ModifiersLossExperienceCreditDebit.getText().equals("0")) {
            setModifiersAdditionalRiskElementsJustification("Additional Risk Elements Justification");
        }
    }


    public void clickPolicyChangeNext() {
        super.clickPolicyChangeNext();
    }


    public void setRandomModifierValuesGL() {

    }


    public void setRandomModifierValuesCA() {

    }


    public void setRandomModifierValuesCP() {

    }

    //TODO: find a way to find this box. Use tableUtils if you can because it changes and will be hard otherwise.

    public void checkIfModifiersIsEditable() {
        if (finds(By.xpath("//div[contains(text(), 'Loss Experience')]/parent::td/following-sibling::td[4 and contains(@class, 'g-cell-edit')]")).size() > 0) {
            Assert.fail("ERROR: Modifier Loss Experience is Editable when it should not be.");
        } else {
            if (finds(By.xpath("//div[contains(text(), 'Additional Risk Elements')]/parent::td/following-sibling::td[4 and contains(@class, 'g-cell-edit')]")).size() > 0) {
                Assert.fail("ERROR: Modifier Additional Risk Elements is Editable when it should not be.");
            }
        }
    }


    //***************** Squire
    @FindBy(xpath = "//div[contains(@id, ':PersonalLinesModifiersScreen:PersonalLinesRateModsDV:0:PersonalLinesRateModsLV')]")
    private WebElement table_SquireModifier;

    @FindBy(xpath = "//div[contains(@id, ':PersonalLinesRateModsDV:0:PersonalLinesRateModsLV-body')]")
    private WebElement table_SquirePolicyModifierDiscount;

    private String getSquireTotalPolicyDiscount() {
        int rowCount = tableUtils.getRowNumberInTableByText(table_SquirePolicyModifierDiscount, "Total Policy Discount");
        WebElement element = table_SquirePolicyModifierDiscount.findElement(By.xpath(".//tr[" + rowCount + "]/td[3]"));
        return element.getText();
    }


    public void setSquirePolicyModifiersEligibilityByCategory(String category, String eligibility) {
        
        int row = tableUtils.getRowNumberInTableByText(table_SquireModifier, category);
        tableUtils.clickCellInTableByRowAndColumnName(table_SquireModifier, row, "Eligibility");
        Guidewire8Select mySelect = new Guidewire8Select(driver, "(//table[contains(@id,'simplecombo') and contains(@id,'triggerWrap')])[last()]");
        mySelect.selectByVisibleTextPartial(eligibility);
    }


    public String getSquirePolicyModififierEligibilityByRow(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SquireModifier, row, "Eligibility");
    }


    public String getSquirePolicyModififierEligibilityByCategory(String category) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SquireModifier, tableUtils.getRowNumberInTableByText(table_SquireModifier, category), "Eligibility");
    }


    public String getSquirePolicyModifierDiscountSurchargeByCategory(String category) {
        String surcharge = tableUtils.getCellTextInTableByRowAndColumnName(table_SquireModifier, tableUtils.getRowNumberInTableByText(table_SquireModifier, category), "Discounts (-) / Surcharges (+)");
        waitForPostBack();
        return surcharge;
    }


    public String getSquirePolicyModifierDiscountSurchargeByRow(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_SquireModifier, row, "Discounts (-) / Surcharges (+)");
    }


    public void enterSquireModifierAdditionalDiscount(int row, String discount, String description) {
        tableUtils.setValueForCellInsideTable(table_SquireModifier, row, "Discounts (-) / Surcharges (+)", "c2", discount);
        
        tableUtils.setValueForCellInsideTable(table_SquireModifier, row, "Justification", "c3", description);
    }


    public int getTotalPolicyDiscount() {
        return Integer.parseInt(getSquireTotalPolicyDiscount().replace("%", "").replace("-", ""));
    }


    @FindBy(xpath = "//div[contains(@id, ':PersonalLinesModifiersScreen:SquireRatePolicyModsDV:0:SquireRatePolicyModsLV')]")
    private WebElement table_SquireSectionModifier;


    public int getSquireSectionModifierEligibility(int row) {
        return Integer.parseInt(tableUtils.getCellTextInTableByRowAndColumnName(table_SquireSectionModifier, row, "Eligibility"));

    }

    //Standard Fire
    @FindBy(xpath = "//div[contains(@id, ':PersonalLinesModifiersScreen:PersonalLinesRateModsDV:0:PersonalLinesRateModsLV')]")
    private WebElement table_StandardFireModifier;

    private String getStandardFireTotalDiscount() {
        WebElement element = table_StandardFireModifier.findElement(By.xpath(".//tfoot/tr/td[3]"));
        return element.getText();
    }


    public String getStandardFirePolicyModififierEligibilityByRow(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_StandardFireModifier, row, "Eligibility");
    }


    public String getStandardFirePolicyModifierDiscountSurchargeByRow(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_StandardFireModifier, row, "Discounts (-) / Surcharges (+)");
    }


    public int getStandardFireTotalPolicyDiscount() {
        return Integer.parseInt(getStandardFireTotalDiscount());
    }

}











