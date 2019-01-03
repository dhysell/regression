package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PackageRiskType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderLineSelection extends GenericWorkorder {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderLineSelection(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // /////////////////////////////
    // // WEB ELEMENTS /////////
    // /////////////////////////////

    @FindBy(xpath = "//div[contains(@id, ':LineSelectionScreen:') and contains(@id, 'LineSelectionDV:1')]")
    private WebElement table_LineSelection;

    @FindBy(xpath = "//span[contains(@id, ':LineSelectionScreen:ttlBar')]")
    private WebElement label_LineSelectionPageTitle;

    Guidewire8Select select_PackageRiskType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':PackageRisk-triggerWrap')]");
    }

    public Guidewire8Checkbox checkbox_CommercialPropertyLine() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, ':LineSelectionScreen:CPPLineSelectionDV:CPLineSelection')]");
    }

    public Guidewire8Checkbox checkbox_GeneralLiabilityLine() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, ':LineSelectionScreen:CPPLineSelectionDV:GLLineSeelection')]");
    }

    public Guidewire8Checkbox checkbox_CommercialAutoLine() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, ':LineSelectionScreen:CPPLineSelectionDV:BALineSelection')]");
    }

    public Guidewire8Checkbox checkbox_InlandMarineLine() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, ':LineSelectionScreen:CPPLineSelectionDV:IMLineSelection')]");
    }

    public Guidewire8Checkbox checkbox_SectionIVInlandMarine() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, ':LineSelectionScreen:CPPLineSelectionDV:IMLineSelection')]");
    }

    private WebElement lineSelectCheckbox(String text) {
        String xpath = "//div[contains(@id, ':LineSelectionScreen:PLineSelectionDV:1-body')]//div[contains(text(), '" + text + "')]/../following-sibling::td/div/img";
        return find(ByXPath.xpath(xpath));
    }

    // ////////////////////////////
    // // HELPER METHODS ///////
    // ////////////////////////////

    public void clickNext() {
        super.clickNext();
    }


    public void clickSaveDraft() {
        super.clickGenericWorkorderSaveDraft();
    }


    public void clickFullApp() {
        super.clickGenericWorkorderFullApp();
    }


    public void clickCloseOptions() {

    }


    public void selectPackageRiskType(PackageRiskType riskType) {
        Guidewire8Select mySelect = select_PackageRiskType();
        mySelect.selectByVisibleText(riskType.getValue());
        if (!finds(By.xpath("//input[contains(@id, ':PackageRisk-inputEl')]")).isEmpty()) {
            if (!find(By.xpath("//input[contains(@id, ':PackageRisk-inputEl')]")).getAttribute("value").equals(riskType.getValue())) {
                mySelect.selectByVisibleText(riskType.getValue());
            }
        }
    }


    public String getPackageRiskType() {
        Guidewire8Select mySelect = select_PackageRiskType();
        return mySelect.getText();
    }


    public void checkCommercialPropertyLine(boolean checked) {
        checkbox_CommercialPropertyLine().select(checked);
    }


    public void checkGeneralLiabilityLine(boolean checked) {
        checkbox_GeneralLiabilityLine().select(checked);
    }


    public void checkComercialAutoLine(boolean checked) {
        checkbox_CommercialAutoLine().select(checked);
    }


    public void checkInlandMarineLine(boolean checked) {
        checkbox_InlandMarineLine().select(checked);
    }


    public void checkSectionIVInlandMarine(boolean checked) {
        checkbox_SectionIVInlandMarine().select(checked);
    }


    public void checkPersonalAutoLine(boolean checked) {
        tableUtils.setCheckboxInTableByText(table_LineSelection, "Auto Line", checked);
    }


    public void checkPersonalPropertyLine(boolean checked) {
        tableUtils.setCheckboxInTableByText(table_LineSelection, "Property & Liability Line", checked);
    }


    public void checkSquireInlandMarine(boolean checked) {
    	waitForPostBack();
        tableUtils.setCheckboxInTableByText(table_LineSelection, "Inland Marine Line", checked);
    }

    public void checkMembership(boolean checked) {
        tableUtils.setCheckboxInTableByText(table_LineSelection, "Membership Line", checked);
    }


    public void checkSquireLineSelectionByTextNoVerify(String text, boolean checked) {
        tableUtils.clickCellInTableByRowAndColumnName(table_LineSelection, tableUtils.getRowNumberInTableByText(table_LineSelection, text), "Enabled");
        hoverOver(table_LineSelection);
    }


    public void checkSquireLineSelectionByText(String text, boolean checked) {
        tableUtils.setCheckboxInTableByText(table_LineSelection, text, checked);
    }


    public boolean lineTypeExists(String lineType) {
        return checkIfElementExists(lineSelectCheckbox(lineType), 1000);
    }


    public void filloutLineSelection(GeneratePolicy policy) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderSquireEligibility eligibilityPage = new repository.pc.workorders.generic.GenericWorkorderSquireEligibility(getDriver());
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);

        switch (policy.productType) {
            case Businessowners:
                break;
            case CPP:
                selectPackageRiskType(policy.commercialPackage.packageRisk);
                if (policy.lineSelection.contains(LineSelection.CommercialAutoLineCPP)) {
                    checkComercialAutoLine(true);
                }
                if (policy.lineSelection.contains(LineSelection.CommercialPropertyLineCPP)) {
                    checkCommercialPropertyLine(true);
                }
                if (policy.lineSelection.contains(LineSelection.GeneralLiabilityLineCPP)) {
                    checkGeneralLiabilityLine(true);
                }
                if (policy.lineSelection.contains(LineSelection.InlandMarineLineCPP)) {
                    checkInlandMarineLine(true);
                }
                break;
            case Membership:
                break;
            case PersonalUmbrella:
                break;
            case Squire:
                eligibilityPage.chooseEligibility(policy.squire.squireEligibility);
            	if(policy.squire.getEffectiveDate() != null && (policy.squire.squireEligibility.equals(SquireEligibility.City) || policy.squire.squireEligibility.equals(SquireEligibility.CustomAuto))) {
            		if(policy.squire.getEffectiveDate().before(DateUtils.convertStringtoDate("2/19/2018", "MM/dd/yyyy"))) {
                		new GenericWorkorderSquireEligibility(getDriver()).clickAutoOnly(false);
                    }
                }
            	if(policy.squire.isFarmAndRanch()) {
            		clickGenericWorkorderSaveDraft();
            		break;
            	}
                sideMenu.clickSideMenuLineSelection();
                if (!policy.lineSelection.contains(LineSelection.PersonalAutoLinePL)) {
                    checkPersonalAutoLine(false);
                    sideMenu.waitUntilAutoSidebarLinkIsNotVisible();
                }
                if (!policy.lineSelection.contains(LineSelection.PropertyAndLiabilityLinePL)) {
                    checkPersonalPropertyLine(false);
                    sideMenu.waitUntilPropertySidebarLinkIsNotVisible();
                } else if (!policy.lineSelection.contains(LineSelection.InlandMarineLinePL)) {
                    checkSquireInlandMarine(false);
                    sideMenu.waitUntilInlandMarineSidebarLinkIsNotVisible();
                }
                break;
            case StandardFire:
                eligibilityPage.setSquireEligibility(policy);
                //Not needed until membership is live.
                /*if (!policy.lineSelection.contains(LineSelection.Membership)) {
	                checkMembership(false);
	            }*/
                break;
            case StandardIM:
                sideMenu.clickSideMenuQualification();
                break;
            case StandardLiability:
                eligibilityPage.setSquireEligibility(policy);
                //Not needed until membership is live.
                /*if (!policy.lineSelection.contains(LineSelection.Membership)) {
	                checkMembership(false);
	            }*/
                break;
        }
    }//END filloutLineSelection

	public void checkline(LineSelection line) throws Exception {
		new SideMenuPC(driver).clickSideMenuLineSelection();
		switch(line) {
		case InlandMarineLinePL:
			checkInlandMarineLine(true);
			break;
		case PersonalAutoLinePL:
			checkPersonalAutoLine(true);
			break;
		case PropertyAndLiabilityLinePL:
			checkPersonalPropertyLine(true);
			break;
		}
	}
}





































