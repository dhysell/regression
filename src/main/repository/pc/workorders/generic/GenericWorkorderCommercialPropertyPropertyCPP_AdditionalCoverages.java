package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.enums.CommercialProperty.PropertyCoverages;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.pc.sidemenu.SideMenuPC;


public class GenericWorkorderCommercialPropertyPropertyCPP_AdditionalCoverages extends BasePage {

    private WebDriver driver;

    public GenericWorkorderCommercialPropertyPropertyCPP_AdditionalCoverages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    GenericWorkorderCommercialPropertyPropertyCPP commercialProperty = new GenericWorkorderCommercialPropertyPropertyCPP(driver);
    repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);

    private void onAdditionalCoveragesTab(CPPCommercialProperty_Building building) throws Exception {
        //check in not on tab
        if (finds(By.xpath("//span[text()='Income']")).isEmpty()) {
            //if not on tab check if on correct building
            if (finds(By.xpath("//span[(text()='Location " + building.getPropertyLocation().getPropertyLocationNumber() + ": Property " + building.getNumber() + "') or (text()='New Property')]")).isEmpty()) {
                sideMenu.clickSideMenuCPProperty();
                commercialProperty.clickLocationRow(building.getPropertyLocation().getAddress());
                commercialProperty.editPropertyByNumber(building.getNumber());
            } else {
                commercialProperty.clickAdditionalCoveragesTab();
            }
        }
    }


    public void fillOutPropertyAdditionalCoverages(CPPCommercialProperty_Building building) throws Exception {
        fillOutPropertyAdditionalCoveragesQQ(building);
        fillOutPropertyAdditionalCoveragesFA(building);
    }

    public void fillOutPropertyAdditionalCoveragesQQ(CPPCommercialProperty_Building building) throws Exception {
        onAdditionalCoveragesTab(building);
        //INCOME
        //		Business Income Coverage Form
        //		Business Income – Landlord As Additional Insured (Rental Value) CP 15 03
        //		Discretionary Payroll Expense CP 15 04
        //		Extra Expense Coverage CP 00 50
        //		Food Contamination (Business Interruption And Extra Expense) CP 15 05

        //OTHER ADDITIOANL COVERAGES
        //		Additional Building Property CP 14 15
        //		Additional Covered Property CP 14 10
        //		Additional Insured – Building Owner CP 12 19
        //		Builders’ Risk Renovations CP 11 13
        //		Building Glass – Tenant’s Policy CP 14 70
        //		Condominium Commercial Unit-Owners Optional Coverages CP 04 18
        //		Discharge From Sewer, Drain Or Sump (Not Flood-Related) CP 10 38
        //		Equipment Breakdown Enhancement Endorsement IDCP 31 1002
        if (!building.getCoverages().getBuildingCoverageList().contains(PropertyCoverages.BuildersRiskCoverageForm_CP_00_20)) {
            selectEquipmentBreakdownEnhancementEndorsement_IDCP311002(building.getAdditionalCoverages().isEquipmentBreakdownEnhancementEndorsementID_CP_31_1002());
        }
        //		Functional Building Valuation CP 04 38
        //		Guests' Property CR 04 11
        //		Leased Property CP 14 60
        //		Loss Payable Provisions CP 12 18
        //		Ordinance or Law CP 04 05
        //		Outdoor Signs CP 14 40
        //		Payroll Limitation Or Exclusion CP 15 10
        //		Peak Season Limit Of Insurance CP 12 30
        //		Radio Or Television Antennas CP 14 50
        //		Spoilage Coverage CP 04 40
        //		Theft Exclusion CP 10 33
        //		Theft Of Building Materials And Supplies (Other Than Builders Risk) CP 10 44
        //		Utility Services – Direct Damage CP 04 17
        //		Utility Services – Time Elements CP 15 45
    }

    public void fillOutPropertyAdditionalCoveragesFA(CPPCommercialProperty_Building building) throws Exception {
        onAdditionalCoveragesTab(building);
        //INCOME
        //		Business Income Coverage Form
        //		Business Income – Landlord As Additional Insured (Rental Value) CP 15 03
        //		Discretionary Payroll Expense CP 15 04
        //		Extra Expense Coverage CP 00 50
        //		Food Contamination (Business Interruption And Extra Expense) CP 15 05

        //OTHER ADDITIOANL COVERAGES
        //		Additional Building Property CP 14 15
        //		Additional Covered Property CP 14 10
        //		Additional Insured – Building Owner CP 12 19
        //		Builders’ Risk Renovations CP 11 13
        //		Building Glass – Tenant’s Policy CP 14 70
        //		Condominium Commercial Unit-Owners Optional Coverages CP 04 18
        //		Discharge From Sewer, Drain Or Sump (Not Flood-Related) CP 10 38
        //		Equipment Breakdown Enhancement Endorsement IDCP 31 1002
//		Equipment Breakdown Enhancement Endorsement IDCP 31 1002
        if (!building.getCoverages().getBuildingCoverageList().contains(PropertyCoverages.BuildersRiskCoverageForm_CP_00_20)) {
            selectEquipmentBreakdownEnhancementEndorsement_IDCP311002(building.getAdditionalCoverages().isEquipmentBreakdownEnhancementEndorsementID_CP_31_1002());
        }
        //		Functional Building Valuation CP 04 38
        //		Guests' Property CR 04 11
        //		Leased Property CP 14 60
        //		Loss Payable Provisions CP 12 18
        //		Ordinance or Law CP 04 05
        //		Outdoor Signs CP 14 40
        //		Payroll Limitation Or Exclusion CP 15 10
        //		Peak Season Limit Of Insurance CP 12 30
        //		Radio Or Television Antennas CP 14 50
        //		Spoilage Coverage CP 04 40
        //		Theft Exclusion CP 10 33
        //		Theft Of Building Materials And Supplies (Other Than Builders Risk) CP 10 44
        //		Utility Services – Direct Damage CP 04 17
        //		Utility Services – Time Elements CP 15 45
    }


    Guidewire8Checkbox checkbox_BusinessIncomeCoverage() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Business Income Coverage')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_BusinessIncome_LandlordAsAdditionalInsured_RentalValue_CP1503() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Business Income � Landlord As Additional Insured (Rental Value) CP 15 03')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_DiscretionaryPayrollExpense_CP1504() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Discretionary Payroll Expense CP 15 04')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_ExtraExpenseCoverage_CP0050() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Extra Expense Coverage CP 00 50')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_FoodContamination_BusinessInterruptionAndExtraExpense_CP1505() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Food Contamination (Business Interruption And Extra Expense) CP 15 05')]/preceding-sibling::table");
    }
    //END OF INCOME

    //OTHER ADDITIONAL COVERAGES
    Guidewire8Checkbox checkbox_AdditionalBuildingProperty_CP1415() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Additional Building Property CP 14 15')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_BuildersRiskRenovations_CP1113() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), ' Risk Renovations CP 11 13')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_BuildingGlass_TenantsPolicy_CP1470() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), ' Policy CP 14 70')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_CondominiumCommercialUnit_OwnersOptional_CP0418() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Condominium Commercial Unit-Owners Optional CP 04 18')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_DischargeFromSewerDrainOrSump_NotFloodRelated_CP1038() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Discharge From Sewer, Drain Or Sump (Not Flood-Related) CP 10 38')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_EquipmentBreakdownEnhancementEndorsement_IDCP311002() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Equipment Breakdown Enhancement Endorsement IDCP 31 1002')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_FunctionalBuildingValuation_CP0438() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Functional Building Valuation CP 04 38')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_GuestsProperty_CR0411() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Guests' Property CR 04 11')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_LeasedProperty_CP1460() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Leased Property CP 14 60')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_OrdinanceOrLaw_CP0405() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Ordinance or Law CP 04 05')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_OutdoorSigns_CP1440() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Outdoor Signs CP 14 40')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_PayrollLimitationOrExclusion_CP1510() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Payroll Limitation Or Exclusion CP 15 10')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_PeakSeasonLimitOfInsurance_CP1230() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Peak Season Limit Of Insurance CP 12 30')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_RadioOrTelevisionAntennas_CP1450() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Radio Or Television Antennas CP 14 50')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_SpoilageCoverage_CP0440() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Spoilage Coverage CP 04 40')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_TheftExclusion_CP1033() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Theft Exclusion CP 10 33')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_TheftOfBuildingMaterialsAndSupplies_OtherThanBuildersRisk_CP1044() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Theft Of Building Materials And Supplies (Other Than Builders Risk) CP 10 44')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_UtilityServices_DirectDamage_CP0417() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Utility Services � Direct Damage CP 04 17')]/preceding-sibling::table");
    }

    Guidewire8Checkbox checkbox_UtilityServices_TimeElements_CP1545() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Utility Services � Time Elements CP 15 45')]/preceding-sibling::table");
    }


    public void selectBusinessIncomeCoverage(boolean checked) {
        checkbox_BusinessIncomeCoverage().select(checked);
    }

    public void selectExtraExpenseCoverage_CP0050(boolean checked) {
        checkbox_ExtraExpenseCoverage_CP0050().select(checked);
    }


    private void selectEquipmentBreakdownEnhancementEndorsement_IDCP311002(boolean checked) {
        checkbox_EquipmentBreakdownEnhancementEndorsement_IDCP311002().select(checked);
    }


    public boolean selectUtilityServices_DirectDamage_CP0417(boolean checked) {
        checkbox_UtilityServices_DirectDamage_CP0417().select(checked);
        return checked;
    }

}
