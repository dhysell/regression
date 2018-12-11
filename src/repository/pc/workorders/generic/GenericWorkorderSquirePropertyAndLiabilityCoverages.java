package repository.pc.workorders.generic;


import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.exception.GuidewireException;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderSquirePropertyAndLiabilityCoverages extends GenericWorkorder {


    private WebDriver driver;

    public GenericWorkorderSquirePropertyAndLiabilityCoverages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void fillOutPropertyAndLiabilityCoveragesQQ(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        waitForPageLoad();
        new GuidewireHelpers(driver).clickProductLogo();

        fillOutPropertyDetialcoverages(policy);
        fillOutFarmPersonalProperty(policy);
        fillOutSecitonIICoverages(policy);
        fillOutExclusionsAndConditions(policy);
    }

    public void fillOutPropertyAndLiabilityCoverages(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();

        fillOutPropertyDetialcoverages(policy);
        fillOutFarmPersonalProperty(policy);
        fillOutSecitonIICoverages(policy);
        fillOutExclusionsAndConditions(policy);
    }

	public void fillOutPropertyDetialcoverages(GeneratePolicy policy) throws GuidewireNavigationException {
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages propertyDetailCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        propertyDetailCoverages.fillOutPropertyDetailCoverages(policy);
    }

    public void fillOutPropertyDetialcoveragesQQ(GeneratePolicy policy) {
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages propertyDetailCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        propertyDetailCoverages.fillOutPropertyDetailCoveragesQQ(policy);
    }

    public void fillOutFarmPersonalProperty(GeneratePolicy policy) throws GuidewireException {
        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages AdditionalSectionICoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
        AdditionalSectionICoverages.fillOutAdditionalSectionICoverages(policy);
    }

    public void fillOutSecitonIICoverages(GeneratePolicy policy) throws GuidewireNavigationException {
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages sectionIIcoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        sectionIIcoverages.fillOutSectionIIcoverages(policy);
    }

    public void fillOutExclusionsAndConditions(GeneratePolicy policy) throws Exception {
        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions excConds = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
        excConds.fillOutExclutionsAndConditions(policy);
    }
    @FindBy(xpath = "//span[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:HOPolicyLevelExclusionsAndConditionsCardTab-btnEl')]")
    private WebElement link_SquirePropertyCoveragesExclusions;

    public void clickCoveragesExclusionsAndConditions() {
        clickWhenClickable(link_SquirePropertyCoveragesExclusions);
        boolean isOnPage = !finds(By.xpath("//span[(@class='g-title') and (text()='Exclusions')]")).isEmpty();
        long endtime = new Date().getTime() + 5000;
        while (!isOnPage && new Date().getTime() < endtime) {
            sleep(1); //Unsure if this is still needed.
            isOnPage = !finds(By.xpath("//span[(@class='g-title') and (text()='Exclusions')]")).isEmpty();
        }
    }

    private Guidewire8Checkbox checkbox_ExclusionsAndConditions(String labelName) {
        return new Guidewire8Checkbox(driver, "//div[contains(., '" + labelName + "')]/preceding-sibling::table[1]");
    }
	
	@FindBy(xpath = "//span[contains(@id, ':HOCoveragesPanelSet:HOPolicyLevelCoveragesIDTab-btnEl')]")
	private WebElement link_SquirePropertyCoveragesPropertyDetailCoverages;

    @FindBy(xpath = "//span[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:FarmPersonalPropertyTab-btnEl') or contains(@id, ':HOCoveragesPanelSet:FarmPersonalPropertyTab-btnInnerEl')]")
    private WebElement link_SquirePropertyCoveragesFarmPersonalProperty;

    @FindBy(xpath = "//span[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:SectionIITab-btnEl')]")
    private WebElement link_SquirePropertyCoveragesSectionIICoverages;

    @FindBy(xpath = "//span[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:secIIAddlInterestsTab-btnEl')]")
    private WebElement link_SquirePropertyCoveragesAdditionalInterest;


    private WebElement button_ExclusionsConditionsAdd(String labelName) {
        return find(By.xpath("//div[contains(., '" + labelName + "')]/ancestor::fieldset//span[contains(.,'Add')]/parent::span"));
    }

    private WebElement div_ExclusionsConditionsEndorsementContainer(String labelName) {
        return find(By.xpath("//div[contains(., '" + labelName + "')]/ancestor::legend/following-sibling::div/span/div/div/table/tbody/tr/td/div"));
    }

    private WebElement divInput_ExclusionsConditionsEndorsement(String labelName) {
        return div_ExclusionsConditionsEndorsementContainer(labelName).findElement(By.xpath(".//div[3]/div/table/tbody/tr/td[2]/div"));
    }

    private WebElement textarea_ExclusionsConditionsEndorsement(String labelName) {
        return div_ExclusionsConditionsEndorsementContainer(labelName).findElement(By.xpath(".//div[4]/table/tbody/tr/td[2]/textarea"));
    }

    //Other Structure

    @FindBy(xpath = "//label[contains(text(), 'Valuation Method')]/parent::td/following-sibling::td/div'")
    private WebElement div_SquirePropertyCoveragesOtherStrctureValuationMethod;

    private Guidewire8Checkbox checkbox_StandardFireCoverageC() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:HOMainCoveragesHOEPanelSet:1:HOCoverageInputSet:CovAScreenCovPatternInputGroup-legendChk')]");
    }

    @FindBy(xpath = "//div[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:GenericExclusionCondition_ExtPanelSet:ExclDV_ref') or contains(@id, 'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:PolicyLevelConditionsAndExclusions:HOAdditionalExclusionsAndConditionsPanelSet:ConditionsPanel:HOAdditionalConditionsDV_ref') or contains(@id,'SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:GenericExclusionCondition_ExtPanelSet:CondDV_ref')]/descendant::table[2]/descendant::tr/descendant::div[2]")
    private WebElement text_SquirePropertyCoveragesLineConditions;

    private Guidewire8Checkbox checkbox_AccessYesEndorsement209() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Access Yes Endorsement 209')]/preceding-sibling::table");
    }

    @FindBy(xpath = "//*[contains(@id, ':riskVal-inputEl')]")
    private WebElement Editbox_FarmPersonalPropertyRisk;


    /*
     * Methods
     */
    public void checkStandardFireCoverageC(boolean trueFalse) {
        
        checkbox_StandardFireCoverageC().select(trueFalse);
        
    }

    public void setEnds105(String description) {
        checkbox_ExclusionsAndConditions("Special Endorsement for Property 105").select(true);
        
        button_ExclusionsConditionsAdd("105").click();
        
        clickWhenClickable(divInput_ExclusionsConditionsEndorsement("105"));
        
        clickWhenClickable(textarea_ExclusionsConditionsEndorsement("105"));
        
        textarea_ExclusionsConditionsEndorsement("105").sendKeys(Keys.CONTROL + "a");
        
        textarea_ExclusionsConditionsEndorsement("105").sendKeys(description);
        
        textarea_ExclusionsConditionsEndorsement("105").sendKeys(Keys.TAB);
        
    }

    public void setEnds205(String description) {
        
        checkbox_ExclusionsAndConditions("Special Endorsement for Liability 205").select(true);
        
        button_ExclusionsConditionsAdd("205").click();
        
        clickWhenClickable(divInput_ExclusionsConditionsEndorsement("205"));
        
        clickWhenClickable(textarea_ExclusionsConditionsEndorsement("205"));
        
        textarea_ExclusionsConditionsEndorsement("205").sendKeys(Keys.CONTROL + "a");
        
        textarea_ExclusionsConditionsEndorsement("205").sendKeys(description);
        
        textarea_ExclusionsConditionsEndorsement("205").sendKeys(Keys.TAB);
        
    }
	
    public void setFarmPersonalPropertyRisk(String risk) {
        setText(Editbox_FarmPersonalPropertyRisk, risk);
    }

    public String getFarmPersonalPropertyRisk() {
        return Editbox_FarmPersonalPropertyRisk.getText();
    }

	public void clickPropertyDetailCoverages() {
		clickWhenClickable(link_SquirePropertyCoveragesPropertyDetailCoverages);
	}

    public void clickFarmPersonalProperty() {
        clickWhenClickable(link_SquirePropertyCoveragesFarmPersonalProperty);
    }

    public void clickAdditionalInterest() {
        clickWhenClickable(link_SquirePropertyCoveragesAdditionalInterest);
    }

    public void clickSectionIICoveragesTab() {
        clickWhenClickable(link_SquirePropertyCoveragesSectionIICoverages);
    }

    public void checkAccessYesEndorsement209(boolean yesno) {
        checkbox_AccessYesEndorsement209().select(yesno);
    }

    public void clickOk() {
        super.clickOK();
    }

    public void clickNext() {
        super.clickNext();
    }

    public void clickQuote() {
        super.clickGenericWorkorderQuote();
    }

    public String getOtherStructureDetachedGarageValuationMethod() {
        return div_SquirePropertyCoveragesOtherStrctureValuationMethod.getText();
    }

    public void editPLPropertyLiabilityCoveragesAndExclusionsConditionsFA(GeneratePolicy policy) throws Exception {
		repository.pc.sidemenu.SideMenuPC sidebar = new repository.pc.sidemenu.SideMenuPC(driver);
		if(policy.squire.isFarmAndRanch()) {
			fillOutPropertyAndLiabilityCoveragesQQ(policy);
		}
        sidebar.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        for (PolicyLocation loc : policy.squire.propertyAndLiability.locationList) {
            for (PLPolicyLocationProperty prop : loc.getPropertyList()) {
                try {
                    coverages.clickSpecificBuilding(loc.getNumber(), prop.getPropertyNumber());
                } catch (Exception e) {
                    coverages.clickSpecificBuilding(1, prop.getPropertyNumber());
                }//END CATCH
                
                coverages.fillOutMiscellaneousCoverages(prop);
            }//END FOR
        }//END FOR

        if (policy.squire.propertyAndLiability.squireFPP != null && (policy.squire.isFarmAndRanch() || policy.squire.isCountry())) {
            clickFarmPersonalProperty();
            // TODO: Put any new full app stuff here
        }//END IF

        clickSectionIICoveragesTab();
        // TODO: Put any new full app stuff here

        coverages.clickCoveragesExclusionsAndConditions();

        if (policy.squire.propertyAndLiability.propLiabExclusionsConditions != null) {
            // GenericWorkorderSquirePropLiabUmbCoveragesExclusions excConds = new GenericWorkorderSquirePropLiabUmbCoveragesExclusions();
            // TODO: Put any new full app stuff here
        }//END IF
    }//END editPLPropertyLiabilityCoveragesAndExclusionsConditionsFA

    public void fillOutPLPropertyLiabilityCoveragesAndExclusionsConditionsStdFire(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);


        //        if (!policy.standardFireAndLiability.getLocationList().get(0).getPropertyList().get(0).getpropertyType().equals(PropertyTypePL.TrellisedHops)) {
        if (!policy.standardFire.getLocationList().get(0).getPropertyList().get(0).getpropertyType().equals(PropertyTypePL.TrellisedHops)) {
            coverages.selectSectionIDeductible(policy.squire.propertyAndLiability.section1Deductible);
        }//END IF

        //        for (PolicyLocation loc : policy.standardFireAndLiability.getLocationList()) {
        for (PolicyLocation loc : policy.standardFire.getLocationList()) {
            for (PLPolicyLocationProperty prop : loc.getPropertyList()) {
                if (!prop.getpropertyType().equals(PropertyTypePL.TrellisedHops)) {
                    coverages.clickSpecificBuilding(loc.getNumber(), prop.getPropertyNumber());
                }//END IF
                
                // Any Std Fire will have this unless Peril 1 is selected
                policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.StdFire_AddCoverageTypePerilGreaterThan1to8);

                switch (prop.getpropertyType()) {
                    case GrainSeed:
                        coverages.setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        coverages.setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());
                        break;
                    case HayStrawInOpen:
                    case HayStrawInStorage:
                    case TrellisedHops:
                        coverages.setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        break;
                    case SolarPanels:
                        coverages.setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        break;
                    case VacationHomeCovE:
                    case CondominiumDwellingPremisesCovE:
                        coverages.setCoverageELimit(prop.getPropertyCoverages().getCoverageE().getLimit());
                        coverages.setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());
                        coverages.setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        coverages.setCoverageCValuation(prop.getPropertyCoverages());
                        break;
                    case CondominiumVacationHome:
                        coverages.setCoverageALimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        coverages.setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        coverages.setStdFireCoverageCValuation(prop.getPropertyCoverages());
                        break;
                    case CondoVacationHomeCovE:
                        coverages.setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        coverages.setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());
                        coverages.setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        coverages.setCoverageCValuation(prop.getPropertyCoverages());
                        break;
                    case Potatoes:
                        coverages.setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        coverages.setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());
                        break;
                    case AlfalfaMill:
                        coverages.setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        coverages.setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());
                        break;
                    case FarmOffice:
                        coverages.setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        coverages.setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());

                        if (prop.getpropertyType().equals(PropertyTypePL.FarmOffice) && !prop.getPropertyCoverages().getCoverageE().getCoverageType().equals(CoverageType.Peril_1)) {
                            coverages.setCoverageEValuation(ValuationMethod.ActualCashValue);
                        }
                        coverages.setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        coverages.setCoverageCValuation(prop.getPropertyCoverages());
                        break;
                    case BunkHouse:
                        coverages.setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        coverages.setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());
                        coverages.setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        coverages.setCoverageCValuation(prop.getPropertyCoverages());
                        break;
                    case DwellingPremisesCovE:
                        coverages.setCoverageELimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        coverages.setCoverageECoverageType(prop.getPropertyCoverages().getCoverageE().getCoverageType());
                        coverages.setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        coverages.selectOtherCoverageCValuation(prop.getPropertyCoverages().getCoverageC().getValuationMethod());
                        break;
                    case DwellingPremises:
                        coverages.setCoverageALimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        coverages.setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        coverages.setCoverageCValuation(prop.getPropertyCoverages());
                        break;
                    case VacationHome:
                        coverages.setCoverageALimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        coverages.setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        coverages.setCoverageCValuation(prop.getPropertyCoverages());
                        break;
                    default:
                        coverages.setCoverageALimit(prop.getPropertyCoverages().getCoverageA().getLimit());
                        coverages.setCoverageCLimit(prop.getPropertyCoverages().getCoverageC().getAdditionalValue());
                        coverages.setStdFireCoverageCValuation(prop.getPropertyCoverages());
                        break;
                }//EDN SWITCH
                coverages.clickBuildingsExclusionsAndConditions();
                // TODO: Set property level exclusionConditions here that the Agent is allowed to set
                coverages.clickBuildingsCoverages();
            }//END FOR
        }//END FOR
        coverages.clickCoveragesExclusionsAndConditions();

        
        clickGenericWorkorderSaveDraft();
    }//END fillOutPLPropertyLiabilityCoveragesAndExclusionsConditionsStdFire
}
