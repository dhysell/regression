package scratchpad.evan;

import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.helpers.GuidewireHelpers;
import repository.pc.workorders.generic.GenericWorkorderInlandMarineAccountsReceivableCPP;
import repository.pc.workorders.generic.GenericWorkorderInlandMarineBaileesCustomersCPP;
import repository.pc.workorders.generic.GenericWorkorderInlandMarineCameraAndMusicalInstrumentCPP;
import repository.pc.workorders.generic.GenericWorkorderInlandMarineCommercialArticlesCPP;
import repository.pc.workorders.generic.GenericWorkorderInlandMarineComputerSystemsCPP;
import repository.pc.workorders.generic.GenericWorkorderInlandMarineContractorsEquipmentCPP;
import repository.pc.workorders.generic.GenericWorkorderInlandMarineExhibitionCPP;
import repository.pc.workorders.generic.GenericWorkorderInlandMarineInstallationCPP;
import repository.pc.workorders.generic.GenericWorkorderInlandMarineMiscellaneousArticlesCPP;
import repository.pc.workorders.generic.GenericWorkorderInlandMarineMotorTruckCargoCPP;
import repository.pc.workorders.generic.GenericWorkorderInlandMarineSignsCPP;
import repository.pc.workorders.generic.GenericWorkorderInlandMarineTripTransitCPP;
import repository.pc.workorders.generic.GenericWorkorderInlandMarineValuablePapersCPP;

public class SideMenuPC extends BasePage {
   
    public SideMenuPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------
    @FindBy(xpath = "//td[contains(@id,':PreQualification') or contains(@id,'PolicyChangeWizard:Qualification') or contains(@id, ':Qualification')]/parent::tr")
    private WebElement link_SideMenuQualification;

    @FindBy(xpath = "//td[contains(@id,'LOBWizardStepGroup')]/parent::tr")
    private WebElement link_SideMenuPolicyContract;

    @FindBy(xpath = "//td[contains(@id,'PolicyInfo')]/parent::tr")
    private WebElement link_SideMenuPolicyInfo;

    @FindBy(xpath = "//td[contains(@id,':LOBWizardStepGroup:BOP')]/parent::tr")
    private WebElement link_SideMenuBusinessownersLine;

    @FindBy(xpath = "//td[contains(@id,':LOBWizardStepGroup:Locations') or contains(@id, ':LOBWizardStepGroup:CPPLocations')  or contains(@id, ':LOBWizardStepGroup:HOWizardStepGroup:HomeownersLocations')]/div/span")
    private WebElement link_SideMenuLocations;

    @FindBy(xpath = "//td[contains(@id,':BOPBuildings') or contains(@id, 'PolicyFile:PolicyFileAcceleratedMenuActions:PolicyMenuItemSet:PolicyMenuItemSet_Buildings') or contains(@id,'PolicyMenuItemSet_Dwelling')]/parent::tr")
    private WebElement link_SideMenuBuildings;

    @FindBy(xpath = "//td[contains(@id,'Admin:MenuLinks:Admin_UsersAndSecurity')]")
    private WebElement link_SideMenuUsersSecurity;

    @FindBy(xpath = "//td[contains(@id,'Admin:MenuLinks:Admin_UsersAndSecurity:UsersAndSecurity_Roles')]")
    private WebElement link_SideMenuUsersSecurityRoles;

    @FindBy(xpath = "//td[contains(@id,':LOBWizardStepGroup:BOPSupplemental')]/parent::tr")
    private WebElement link_SideMenuSupplemental;

    @FindBy(xpath = "//td[contains(@id,'LOBWizardStepGroup:Modifiers') or contains(@id, ':PolicyMenuItemSet_Modifiers') or contains(@id,'LOBWizardStepGroup:HOWizardStepGroup:StdFireModifiers') or contains(@id, 'SubmissionWizard:LOBWizardStepGroup:StdFireModifiers') or contains(@id,'LOBWizardStepGroup:SquireModifiers') or contains(@id, ':LOBWizardStepGroup:CPPModifiers') or contains(@id, ':LOBWizardStepGroup:CAWizardStepGroup:Modifiers') or contains(@id, ':LOBWizardStepGroup:StdFireModifiers')]/parent::tr")
    private WebElement link_SideMenuModifiers;

    @FindBy(xpath = "//td[contains(@id,'LOBWizardStepGroup:PayerAssignment')]/parent::tr")
    private WebElement link_SideMenuPayerAssignment;

    @FindBy(xpath = "//td[contains(@id,'RiskAnalysis')]")
    private WebElement link_SideMenuRiskAnalysis;

    @FindBy(xpath = "//td[contains(@id,'PolicyReview')]/parent::tr")
    private WebElement link_SideMenuPolicyReview;

    @FindBy(xpath = "//td[contains(@id,'ViewQuote') or contains(@id, 'ViewMultiLineQuote') or contains (@id, ':MultiLineQuote')]/parent::tr")
    private WebElement link_SideMenuQuote;

    @FindBy(xpath = "//td[contains(@id,'Forms')]/parent::tr | //td[contains(@id,'PolicyMenuItemSet_Endorsements')]/div/span | //td[contains(@id,':PolicyMenuItemSet_Endorsements')]/div/span")
    private WebElement link_SideMenuForms;

    @FindBy(xpath = "//td[contains(@id,'BillingInfo')]/parent::tr")
    private WebElement link_SideMenuPayment;

    @FindBy(xpath = "//td[contains(@id,'Summary')]/parent::tr")
    private WebElement link_SideMenuToolsSummary;

    @FindBy(xpath = "//td[contains(@id,'Billing')]/parent::tr")
    private WebElement link_SideMenuToolsBilling;

    @FindBy(xpath = "//td[contains(@id,'Contacts')]/parent::tr")
    private WebElement link_SideMenuToolsContacts;

    @FindBy(xpath = "//td[contains(@id,'Participants')]/parent::tr")
    private WebElement link_SideMenuToolsParticipants;

    @FindBy(xpath = "//td[contains(@id,'Notes')]/parent::tr")
    private WebElement link_SideMenuToolsNotes;

    @FindBy(xpath = "//td[contains(@id,'Documents')]/parent::tr")
    private WebElement link_SideMenuToolsDocuments;

    @FindBy(xpath = "//td[contains(@id,'Icons')]/parent::tr")
    private WebElement link_SideMenuToolsFileMarkers;

    @FindBy(xpath = "//td[contains(@id,'Jobs')]/parent::tr")
    private WebElement link_SideMenuToolsWorkOrders;

    @FindBy(xpath = "//td[contains(@id,'Workplan')]/parent::tr")
    private WebElement link_SideMenuToolsWorkplan;

    @FindBy(xpath = "//td[contains(@id,'RiskEvaluation')]/parent::tr")
    private WebElement link_SideMenuToolsRiskAnalysis;

    @FindBy(xpath = "//td[contains(@id,'History')]/parent::tr")
    private WebElement link_SideMenuToolsHistory;

    @FindBy(xpath = "//span[contains(text(),'Businessowners Line') and contains(@class, 'g-title')]")
    private WebElement title_BusinessOwnersLine;

    @FindBy(xpath = "//td[contains(@id,':MenuLinks:PolicyFile_ChargesToBC')]")
    private WebElement link_SideMenuChargesToBC;

    // jlarsen R2
    // CPP GROUP LINKS
    @FindBy(xpath = "//td[contains(@id,':LOBWizardStepGroup:GLWizardStepGroup')]/parent::tr")
    private WebElement link_SideMenuGeneralLiabilityGroup;

    @FindBy(xpath = "//td[contains(@id,':LOBWizardStepGroup:CAWizardStepGroup')]/parent::tr")
    private WebElement link_SideMenuCommercialAutoGroup;

    @FindBy(xpath = "//td[contains(@id,':LOBWizardStepGroup:CPWizardStepGroup')]/parent::tr")
    private WebElement link_SideMenuCommercialPropertyGroup;

    @FindBy(xpath = "//td[contains(@id,':LOBWizardStepGroup:IMWizardStepGroup') or contains(@id, ':LOBWizardStepGroup:PIMCovSelection')]/parent::tr")
    private WebElement link_SideMenuInlandMarineGroup;

    // CPP LINKS
    @FindBy(xpath = "//span[contains(text(), 'Coverages')]/parent::div/parent::td[contains(@id, ':GLWizardStepGroup:GLLine')]/parent::tr")
    private WebElement link_SideMenuGLCoverages;

    @FindBy(xpath = "//td[contains(@id, ':GLWizardStepGroup:GLLineEU')]/parent::tr")
    private WebElement link_SideMenuGLExposures;

    @FindBy(xpath = "//td[contains(@id, ':GLWizardStepGroup:GLModifiers')]/parent::tr")
    private WebElement link_SideMenuGLModifiers;

    @FindBy(xpath = "//td[contains(@id, ':CAWizardStepGroup:caLineStep')]/parent::tr")
    private WebElement link_SideMenuCACommercialAutoLine;

    @FindBy(xpath = "//td[contains(@id, ':CAWizardStepGroup:GarageKeepers')]/parent::tr")
    private WebElement link_SideMenuCAGarageKeepers;

    @FindBy(xpath = "//td[contains(@id, ':CAWizardStepGroup:BusinessVehicles')]/parent::tr")
    private WebElement link_SideMenuCAVehicles;

    @FindBy(xpath = "//td[contains(@id, ':CAWizardStepGroup:CADrivers')]/parent::tr")
    private WebElement link_SideMenuCADrivers;

    @FindBy(xpath = "//td[contains(@id, ':CAWizardStepGroup:CoveredAutoSymbols')]/parent::tr")
    private WebElement link_SideMenuCACoveredVehicles;

    @FindBy(xpath = "//td[contains(@id, ':CAWizardStepGroup:StateInfo')]/parent::tr")
    private WebElement link_SideMenuCAStateInfo;

    @FindBy(xpath = "//td[contains(@id, ':CAWizardStepGroup:Modifiers')]/parent::tr")
    private WebElement link_SideMenuCAModifiers;

    @FindBy(xpath = "//td[contains(@id, ':CPWizardStepGroup:CPBuildings')]/parent::tr")
    private WebElement link_SideMenuCPProperty;

    @FindBy(xpath = "//td[contains(@id, ':CPWizardStepGroup:cpLineStep')]/parent::tr")
    private WebElement link_SideMenuCPCommercialPropertyLine;

    @FindBy(xpath = "//td[contains(@id, ':CPWizardStepGroup:CPModifiers')]/parent::tr")
    private WebElement link_SideMenuCPModifiers;

    @FindBy(xpath = "//td[contains(@id, ':IMWizardStepGroup:IMPartSelection')]/parent::tr")
    private WebElement link_SideMenuIMCoveragePartSelection;

    @FindBy(xpath = "//td[contains(@id, ':IMWizardStepGroup:Buildings')]/parent::tr")
    private WebElement link_SideMenuIMBuilidingsAndLocations;

    @FindBy(xpath = "//span[contains(text(), 'Accounts Receivable Coverage Form CM 00 66')]/ancestor::tr")
    private WebElement link_SideMenuIMAccountsReceivable;

    @FindBy(xpath = "//span[contains(text(), 'Bailees Customers Coverage Form IH 00 85')]/ancestor::tr")
    private WebElement link_SideMenuIMBaileesCustomers;

    @FindBy(xpath = "//span[contains(text(), 'Camera And Musical Instrument Dealers Coverage Form CM 00 21')]/ancestor::tr")
    private WebElement link_SideMenuIMCameraAndMusicalInstrument;

    @FindBy(xpath = "//span[contains(text(), 'Commercial Articles Coverage Form CM 00 20')]/ancestor::tr")
    private WebElement link_SideMenuIMCommercialArticles;

    @FindBy(xpath = "//span[contains(text(), 'Computer Systems Coverage Form IH 00 75')]/ancestor::tr")
    private WebElement link_SideMenuIMComputerSystems;

    @FindBy(xpath = "//span[contains(text(), 'Contractors Equipment Coverage Form IH 00 68')]/ancestor::tr")
    private WebElement link_SideMenuIMContractorsEquipment;

    @FindBy(xpath = "//span[contains(text(), 'Exhibition Coverage Form IH 00 92')]/ancestor::tr")
    private WebElement link_SideMenuIMExhibition;

    @FindBy(xpath = "//span[contains(text(), 'Installation Floater Coverage (Standard Form) IDCM 31 4073')]/ancestor::tr")
    private WebElement link_SideMenuIMInstallation;

    @FindBy(xpath = "//span[contains(text(), 'Miscellaneous Articles Coverage Form IH 00 79')]/ancestor::tr")
    private WebElement link_SideMenuIMMiscellaneousArticles;

    @FindBy(xpath = "//span[contains(text(), 'Motor Truck Cargo')]/ancestor::tr")
    private WebElement link_SideMenuIMMotorTruckCargo;

    @FindBy(xpath = "//span[contains(text(), 'Signs Coverage Form CM 00 28')]/ancestor::tr")
    private WebElement link_SideMenuIMSigns;

    @FindBy(xpath = "//span[contains(text(), 'Trip Transit Coverage Form IH 00 78')]/ancestor::tr")
    private WebElement link_SideMenuIMTripTransit;

    @FindBy(xpath = "//span[contains(text(), 'Valuable Papers And Records Coverage Form CM 00 67 ')]/ancestor::tr")
    private WebElement link_SideMenuIMValuablePapers;

    // SQUIRE LINKS

    @FindBy(xpath = "//td[contains(@id, ':SquireEligibility')]/parent::tr")
    private WebElement link_SideMenuSquireEligibility;

    @FindBy(xpath = "//td[contains(@id, ':LineSelection')]/parent::tr")
    private WebElement link_SideMenuLineSelection;

    @FindBy(xpath = "//td[contains(@id, 'PolicyMembers')]/parent::tr")
    private WebElement link_SideMenuHouseholdMembers;
    
    @FindBy(xpath = "//td[contains(@id, ':MSLineWizardStepGroup') or contains(@id,':MSWizardStepGroup')]/parent::tr")
    private WebElement link_SideMenuMembership;
    
    @FindBy(xpath = "//td[contains(@id, ':MSLineWizardStepGroup:MembershipPolicyTypeStep')]/parent::tr")
    private WebElement link_SideMenuMembershipMembershipType;
    
    @FindBy(xpath = "//span[text()='Members']/ancestor::tr[1]")
    private WebElement link_SideMenuMembershipMembers;
    
    @FindBy(xpath = "//span[text()='Commodities']/ancestor::tr[1]")
    private WebElement link_SideMenuMembershipCommodities; 

    @FindBy(xpath = "//td[contains(@id,'CreditReport')]/parent::tr")
    private WebElement link_SideMenuInsuranceScore;

    @FindBy(xpath = "//td[contains(@id, ':PAWizardStepGroup')]/parent::tr")
    private WebElement link_SideMenuPersonalAutoGroup;

    @FindBy(xpath = "//span[contains(text(), 'Sections I & II - Property & Liability')]")
    private WebElement link_SideMenuPersonalPropertyAndGLGroup;

    @FindBy(xpath = "//td[contains(@id, ':PADrivers')]/parent::tr")
    private WebElement link_SideMenuPADrivers;

    @FindBy(xpath = "//td[contains(@id, ':PersonalVehicles')]/parent::tr")
    private WebElement link_SideMenuPAVehicles;

    @FindBy(xpath = "//td[contains(@id, ':PALine')]/parent::tr")
    private WebElement link_SideMenuPACoverages;

    @FindBy(xpath = "//td[contains(@id, ':PALineReview')]/parent::tr")
    private WebElement link_SideMenuPALineReview;

    @FindBy(xpath = "//td[contains(@id, ':HOLineReview')]/parent::tr")
    private WebElement linkSideMenuPropertyLineReview;

    @FindBy(xpath = "//td[contains(@id, 'HOWizardStepGroup:HOLineReview')]/parent::tr")
    private WebElement link_SideMenuPropertyGlLineReview;

    @FindBy(xpath = "//td[contains(@id, ':PIMExclusionsAndConditions')]/parent::tr")
    private WebElement link_SideMenuPAIMExclusionsAndConditions;

    @FindBy(xpath = "//td[contains(@id, ':PIMLineReview')]/parent::tr")
    private WebElement link_SideMenuPAIMLineReview;

    @FindBy(xpath = "//td[contains(@id, ':HOWizardStepGroup')]/parent::tr")
    private WebElement link_SideMenuSquireProperty;

    @FindBy(xpath = "//td[contains(@id, ':HomeownersDwelling')]/parent::tr")
    private WebElement link_SideMenuSquirePropertyDetails;

    @FindBy(xpath = "//td[contains(@id, ':HOCoverages')]/parent::tr")
    private WebElement link_SideMenuSquirePropertyCoverages;

    @FindBy(xpath = "//td[contains(@id, ':HOPriorLosses')]/parent::tr")
    private WebElement link_SideMenuSquirePropertyCLUE;

    @FindBy(xpath = "//td[contains(@id, ':LOBWizardStepGroup:PAWizardStepGroup:PAPriorLossExt')]/parent::tr")
    private WebElement link_SideMenuSquireAutoCLUE;

    @FindBy(xpath = "//td[contains(@id, ':IMWizardStepGroup:RecreationVehicle')]/parent::tr")
    private WebElement link_SideMenuSquireIMRecreationVehicles;

    @FindBy(xpath = "//td[contains(@id, ':IMWizardStepGroup:Watercraft')]/parent::tr")
    private WebElement link_SideMenuSquireIMWatercraft;

    @FindBy(xpath = "//td[contains(@id, ':IMWizardStepGroup:FarmEquipment')]/parent::tr")
    private WebElement link_SideMenuSquireIMFarmEquipment;

    @FindBy(xpath = "//td[contains(@id, ':LOBWizardStepGroup:FarmEquipment')]/parent::tr")
    private WebElement link_SideMenuSquireStandAloneIMFarmEquipment;

    @FindBy(xpath = "//td[contains(@id, ':IMWizardStepGroup:PersonalEquipment') or contains(@id, ':LOBWizardStepGroup:PersonalEquipment')]/parent::tr")
    private WebElement link_SideMenuSquireIMPersonalEquipment;

    @FindBy(xpath = "//td[contains(@id, ':IMWizardStepGroup:Cargo')]/parent::tr")
    private WebElement link_SideMenuSquireIMCargo;

    @FindBy(xpath = "//td[contains(@id, ':IMWizardStepGroup:Livestock')]/parent::tr")
    private WebElement link_SideMenuSquireIMLivestock;

    @FindBy(xpath = "//td[contains(@id, ':LOBWizardStepGroup:HOWizardStepGroup')]/parent::tr")
    private WebElement link_SideMenuSquirePropertyLiability;

    @FindBy(xpath = "//td[contains(@id, ':HOWizardStepGroup:HomeownersLocations')]/parent::tr")
    private WebElement link_SideMenuSquireLocations;

    @FindBy(xpath = "//td[contains(@id, ':LOBWizardStepGroup:PUPLiabilityCoverageInfo')]/parent::tr")
    private WebElement link_SideMenuSquireUmbrellaCoverages;

    @FindBy(xpath = "//td[contains(@id, ':LOBWizardStepGroup:Livestock')]/parent::tr")
    private WebElement link_SideMenuStandAloneLivestock;

    @FindBy(xpath = "//td[contains(@id, ':LOBWizardStepGroup:PIMCovSelection')]/parent::tr")
    private WebElement link_SideMenuStandardIMCoverageSelection;

    @FindBy(xpath = "//td[contains(@id, ':LOBWizardStepGroup:FarmEquipment')]/parent::tr")
    private WebElement link_SideMenuStandardIMFarmEquipment;

    @FindBy(xpath = "//td[contains(@id, ':LOBWizardStepGroup:PersonalEquipment')]/parent::tr")
    private WebElement link_SideMenuStandardIMPersonalProperty;

    @FindBy(xpath = "//td[contains(@id, ':LOBWizardStepGroup:PIMLineReview')]/parent::tr")
    private WebElement link_SideMenuStandardIMLineReview;

    @FindBy(xpath = "//td[contains(@id, 'RenewalWizard:PastTermInfo')]/parent::tr")
    private WebElement link_SideMenuRenewalInformation;
    // END R2
    //Policy Contract methods

    @FindBy(xpath = "//td[contains(@id, ':LOBWizardStepGroup:PAWizardStepGroup')]/parent::tr")
    private WebElement link_SideMenuPolicyContractSectionIIAutoLine;

    //@FindBy(xpath = "//td[contains(@id, 'PolicyFile:PolicyFileAcceleratedMenuActions:PolicyMenuItemSet:0:lineParent:PolicyLineMenuItemSet:PolicyMenuItemSet_Vehicles')]/parent::tr")
    @FindBy(xpath = "//*[contains(@id, 'PAWizardStepGroup:PersonalVehicles')]/div/span")
    private WebElement link_SideMenuPolicyContractSectionVehicles;

    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

    // jlarsen R2
    public void clickSideMenuStandAloneIMLivestock() {
        clickWhenClickable(link_SideMenuStandAloneLivestock);
    }

    public void clickSideMenuHouseholdMembers() throws Exception {
        clickWhenClickable(link_SideMenuHouseholdMembers);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Policy Members')]", 10000, "UNABLE TO GET TO POLICY MEMBERS PAGE");
    }
    
    public void clickSideMenuMembership() {
        clickWhenClickable(link_SideMenuMembership);
    }
    
    public void clickSideMenuMembershipMembershipType() throws Exception {
    	clickSideMenuMembership();
        clickWhenClickable(link_SideMenuMembershipMembershipType);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Membership Type')]", 10000, "UNABLE TO GET TO MEMBERSHIP TYPE PAGE");
    }
    
    public void clickSideMenuMembershipMembers() throws Exception {
    	clickSideMenuMembership();
        clickWhenClickable(link_SideMenuMembershipMembers);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Members')]", 10000, "UNABLE TO GET TO MEMBERS PAGE");
    }
    
    public void clickSideMenuMembershipCommodities() throws Exception {
        clickSideMenuMembership();
        clickWhenClickable(link_SideMenuMembershipCommodities);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Commodities')]", 10000, "UNABLE TO GET TO COMMODITIES PAGE");
     }

    public void waitUntilPropertySidebarLinkIsNotVisible() {
        waitUntilElementIsNotVisible(link_SideMenuPersonalPropertyAndGLGroup);
    }

    public void waitUntilAutoSidebarLinkIsNotVisible() {
        waitUntilElementIsNotVisible(link_SideMenuPersonalAutoGroup);
    }

    public void waitUntilInlandMarineSidebarLinkIsNotVisible() {
        waitUntilElementIsNotVisible(link_SideMenuInlandMarineGroup);
    }

    public void clickSideMenuPADrivers() throws Exception {
        clickWhenClickable(link_SideMenuPersonalAutoGroup);
        clickWhenClickable(link_SideMenuPADrivers);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Drivers')]", 10000, "UNABLE TO GET TO DRIVERS PAGE");
    }

    public void clickSideMenuPAVehicles() throws Exception {
        clickWhenClickable(link_SideMenuPersonalAutoGroup);
        clickWhenClickable(link_SideMenuPAVehicles);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Vehicles')]", 10000, "UNABLE TO GET TO VEHICLES PAGE");
    }

    public void clickSideMenuPACoverages() throws Exception {
        clickWhenClickable(link_SideMenuPersonalAutoGroup);
        clickWhenClickable(link_SideMenuPACoverages);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Coverages')]", 10000, "UNABLE TO GET TO COVERAGES PAGE");
    }

    public void clickSideMenuSquireProperty() {
        clickWhenClickable(link_SideMenuSquireProperty);
    }

    public void clickSideMenuSquireUmbrellaCoverages() throws Exception {
        clickWhenClickable(link_SideMenuSquireUmbrellaCoverages);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Coverages')]", 10000, "UNABLE TO GET TO COVERAGES PAGE");
    }

    public void clickSideMenuSquireIMLineReview() throws Exception {
        clickWhenClickable(link_SideMenuInlandMarineGroup);
        clickWhenClickable(link_SideMenuPAIMLineReview);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Line Review')]", 10000, "UNABLE TO GET TO LINE REVIEW PAGE");
    }

    public void clickSideMenuSquirePropertyLineReview() throws Exception {
        clickWhenClickable(link_SideMenuSquirePropertyCoverages);
        clickWhenClickable(linkSideMenuPropertyLineReview);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Line Review')]", 10000, "UNABLE TO GET TO LINE REVIEW PAGE");
    }

    public void clickSideMenuSquireLineReview() throws Exception {
        clickWhenClickable(link_SideMenuPersonalAutoGroup);
        clickWhenClickable(link_SideMenuPALineReview);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Line Review')]", 10000, "UNABLE TO GET TO LINE REVIEW PAGE");
    }

    public void clickSideMenuSquirePropertyGlLineReview() throws Exception {
        clickWhenClickable(link_SideMenuPersonalPropertyAndGLGroup);
        clickWhenClickable(link_SideMenuPropertyGlLineReview);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Line Review')]", 10000, "UNABLE TO GET TO LINE REVIEW PAGE");
    }

    public void clickSideMenuSquirePropertyDetail() {
        clickSideMenuSquireProperty();
        clickWhenClickable(link_SideMenuSquirePropertyDetails);
    }

    public void clickSideMenuSquirePropertyCoverages() throws Exception {
        clickSideMenuSquireProperty();
        clickWhenClickable(link_SideMenuSquirePropertyCoverages);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Coverages')]", 10000, "UNABLE TO GET TO PROPERTY COVERAGES PAGE");
    }

    public void clickSideMenuSquirePropertyCLUE() throws Exception {
        clickSideMenuSquireProperty();
        clickWhenClickable(link_SideMenuSquirePropertyCLUE);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'CLUE Property')]", 10000, "UNABLE TO GET TO CLUE PROPERTY PAGE");
    }

    public void clickSideMenuGLCoverages() {
        if (checkIfElementExists(link_SideMenuGLCoverages, 5)) {
            clickWhenClickable(link_SideMenuGLCoverages);
        } else {
            clickWhenClickable(link_SideMenuGeneralLiabilityGroup);
            clickWhenClickable(link_SideMenuGLCoverages);
        }
    }

    public void clickSideMenuGLExposures() {
        clickWhenClickable(link_SideMenuGeneralLiabilityGroup);
        clickWhenClickable(link_SideMenuGLExposures);
    }

    public void clickSideMenuGLModifiers() {
        if (checkIfElementExists(link_SideMenuGLModifiers, 5)) {
            clickWhenClickable(link_SideMenuGLModifiers);
        } else {
            clickWhenClickable(link_SideMenuGeneralLiabilityGroup);
            clickWhenClickable(link_SideMenuGLModifiers);
        }
    }

    public void clickSideMenuCACommercialAutoLine() {
        clickWhenClickable(link_SideMenuCommercialAutoGroup);
        clickWhenClickable(link_SideMenuCACommercialAutoLine);
    }

    public void clickSideMenuCAGarageKeepers() {
        if (checkIfElementExists(link_SideMenuCAGarageKeepers, 5)) {
            clickWhenClickable(link_SideMenuCAGarageKeepers);
        } else {
            clickWhenClickable(link_SideMenuCommercialAutoGroup);
            clickWhenClickable(link_SideMenuCAGarageKeepers);
        }
    }

    public void clickSideMenuCAVehicles() {
        if (checkIfElementExists(link_SideMenuCAVehicles, 5)) {
            clickWhenClickable(link_SideMenuCAVehicles);
        } else {
            clickWhenClickable(link_SideMenuCommercialAutoGroup);
            clickWhenClickable(link_SideMenuCAVehicles);
        }
    }

    public void clickSideMenuCAStateInfo() {
        if (checkIfElementExists(link_SideMenuCAStateInfo, 5)) {
            clickWhenClickable(link_SideMenuCAStateInfo);
        } else {
            clickWhenClickable(link_SideMenuCommercialAutoGroup);
            clickWhenClickable(link_SideMenuCAStateInfo);
        }
    }

    public void clickSideMenuCADrivers() {
        if (checkIfElementExists(link_SideMenuCADrivers, 5)) {
            clickWhenClickable(link_SideMenuCADrivers);
        } else {
            clickWhenClickable(link_SideMenuCommercialAutoGroup);
            clickWhenClickable(link_SideMenuCADrivers);
        }
    }

    public void clickSideMenuCACoveredVehicles() {
        if (checkIfElementExists(link_SideMenuCACoveredVehicles, 5)) {
            clickWhenClickable(link_SideMenuCACoveredVehicles);
        } else {
            clickWhenClickable(link_SideMenuCommercialAutoGroup);
            clickWhenClickable(link_SideMenuCACoveredVehicles);
        }
    }

    public void clickSideMenuCAModifiers() {
        if (checkIfElementExists(link_SideMenuCAModifiers, 5)) {
            clickWhenClickable(link_SideMenuCAModifiers);
        } else {
            clickWhenClickable(link_SideMenuCommercialAutoGroup);
            clickWhenClickable(link_SideMenuCAModifiers);
        }
    }

    public void clickSideMenuCPProperty() {
        clickWhenClickable(link_SideMenuCommercialPropertyGroup);
        clickWhenClickable(link_SideMenuCPProperty);
    }

    public void clickSideMenuCPCommercialPropertyLine() {
        clickWhenClickable(link_SideMenuCommercialPropertyGroup);
        clickWhenClickable(link_SideMenuCPCommercialPropertyLine);
    }
    public void clickSideMenuCPModifiers() {
        clickWhenClickable(link_SideMenuCommercialPropertyGroup);
        clickWhenClickable(link_SideMenuCPModifiers);
    }

    public void clickSideMenuIMCoveragePartSelection() {
        clickWhenClickable(link_SideMenuInlandMarineGroup);
    }

    public GenericWorkorderInlandMarineAccountsReceivableCPP clickSideMenuIMAccountsReceivable() {
        clickWhenClickable(link_SideMenuIMAccountsReceivable);
        return new GenericWorkorderInlandMarineAccountsReceivableCPP(getDriver());
    }

    public GenericWorkorderInlandMarineBaileesCustomersCPP clickSideMenuIMBaileesCustomers() {
        clickWhenClickable(link_SideMenuIMBaileesCustomers);
        return new GenericWorkorderInlandMarineBaileesCustomersCPP(getDriver());
    }

    public GenericWorkorderInlandMarineCameraAndMusicalInstrumentCPP clickSideMenuIMCameraAndMusicalInstrument() {
        clickWhenClickable(link_SideMenuIMCameraAndMusicalInstrument);
        return new GenericWorkorderInlandMarineCameraAndMusicalInstrumentCPP(getDriver());
    }

    public GenericWorkorderInlandMarineCommercialArticlesCPP clickSideMenuIMCommercialArticles() {
        clickWhenClickable(link_SideMenuIMCommercialArticles);
        return new GenericWorkorderInlandMarineCommercialArticlesCPP(getDriver());
    }

    public GenericWorkorderInlandMarineComputerSystemsCPP clickSideMenuIMComputerSystems() {
        clickWhenClickable(link_SideMenuIMComputerSystems);
        return new GenericWorkorderInlandMarineComputerSystemsCPP(getDriver());
    }

    public GenericWorkorderInlandMarineContractorsEquipmentCPP clickSideMenuIMContractorsEquipment() {
        clickWhenClickable(link_SideMenuIMContractorsEquipment);
        return new GenericWorkorderInlandMarineContractorsEquipmentCPP(getDriver());
    }

    public GenericWorkorderInlandMarineExhibitionCPP clickSideMenuIMExhibition() {
        clickWhenClickable(link_SideMenuIMExhibition);
        return new GenericWorkorderInlandMarineExhibitionCPP(getDriver());
    }

    public GenericWorkorderInlandMarineInstallationCPP clickSideMenuIMInstallation() {
        clickWhenClickable(link_SideMenuIMInstallation);
        return new GenericWorkorderInlandMarineInstallationCPP(getDriver());
    }

    public GenericWorkorderInlandMarineMiscellaneousArticlesCPP clickSideMenuIMMiscellaneousArticles() {
        clickWhenClickable(link_SideMenuIMMiscellaneousArticles);
        return new GenericWorkorderInlandMarineMiscellaneousArticlesCPP(getDriver());
    }

    public GenericWorkorderInlandMarineMotorTruckCargoCPP clickSideMenuIMMotorTruckCargo() {
        clickWhenClickable(link_SideMenuIMMotorTruckCargo);
        return new GenericWorkorderInlandMarineMotorTruckCargoCPP(getDriver());
    }

    public GenericWorkorderInlandMarineSignsCPP clickSideMenuIMSigns() {
        clickWhenClickable(link_SideMenuIMSigns);
        return new GenericWorkorderInlandMarineSignsCPP(getDriver());
    }

    public GenericWorkorderInlandMarineTripTransitCPP clickSideMenuIMTripTransit() {
        clickWhenClickable(link_SideMenuIMTripTransit);
        return new GenericWorkorderInlandMarineTripTransitCPP(getDriver());
    }

    public GenericWorkorderInlandMarineValuablePapersCPP clickSideMenuIMValuablePapers() {
        clickWhenClickable(link_SideMenuIMValuablePapers);
        return new GenericWorkorderInlandMarineValuablePapersCPP(getDriver());
    }

    public void clickSideMenuSquireEligibility() throws Exception {
        clickWhenClickable(link_SideMenuSquireEligibility);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Squire Eligibility')]", 10000, "UNABLE TO GET TO SQUIRE ELIGIBILITY PAGE");
    }

    public void clickSideMenuLineSelection() throws Exception {
        clickWhenClickable(link_SideMenuLineSelection);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Line Selection')]", 10000, "UNABLE TO GET TO LINE SELECTION PAGE");
    }

    public void clickSideMenuIMBuildingsAndLocations() {
        clickWhenClickable(link_SideMenuInlandMarineGroup);
        clickWhenClickable(link_SideMenuIMBuilidingsAndLocations);
    }

//	
//	public void clickSideMenuIMLineReview() {
//		clickWhenClickable(link_SideMenuInlandMarineGroup);
//		clickWhenClickable(link_SideMenuIMLineReview);
//		delay(110);
//	}

    public void clickSideMenuIMExclusionsAndConditions() throws Exception {
        clickWhenClickable(link_SideMenuInlandMarineGroup);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@class, 'tree-node') and text()='Exclusions and Conditions']", 10000, "UNABLE TO GET TO INLAND MARINE SUB-WIZARD STEPS");
        clickWhenClickable(link_SideMenuPAIMExclusionsAndConditions);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Exclusions and Conditions')]", 10000, "UNABLE TO GET TO EXCLUSIONS AND CONDITIONS PAGE");
    }

    public void clickSideMenuIMRecreationVehicle() throws Exception {
        clickWhenClickable(link_SideMenuInlandMarineGroup);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@class, 'tree-node') and text()='Recreational Equipment']", 10000, "UNABLE TO GET TO INLAND MARINE SUB-WIZARD STEPS");
        clickWhenClickable(link_SideMenuSquireIMRecreationVehicles);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Recreational Equipment')]", 10000, "UNABLE TO GET TO RECREATIONAL EQUIPMENT PAGE");
    }

    public void clickSideMenuIMWatercraft() throws Exception {
        clickWhenClickable(link_SideMenuInlandMarineGroup);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@class, 'tree-node') and text()='Watercraft']", 10000, "UNABLE TO GET TO INLAND MARINE SUB-WIZARD STEPS");
        clickWhenClickable(link_SideMenuSquireIMWatercraft);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Watercraft')]", 10000, "UNABLE TO GET TO WATERCRAFT PAGE");
    }

    public void clickSideMenuIMFarmEquipment() throws Exception {
        clickWhenClickable(link_SideMenuInlandMarineGroup);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@class, 'tree-node') and text()='Farm Equipment']", 10000, "UNABLE TO GET TO INLAND MARINE SUB-WIZARD STEPS");
        clickWhenClickable(link_SideMenuSquireIMFarmEquipment);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Farm Equipment')]", 10000, "UNABLE TO GET TO FARM EQUIPMENT PAGE");
    }

    public void clickSideMenuStandAloneIMFarmEquipment() throws Exception {
        clickWhenClickable(link_SideMenuSquireStandAloneIMFarmEquipment);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Farm Equipment')]", 10000, "UNABLE TO GET TO FARM EQUIPMENT PAGE");
    }

    public void clickSideMenuIMPersonalEquipment() throws Exception {
        clickWhenClickable(link_SideMenuInlandMarineGroup);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@class, 'tree-node') and text()='Personal Property']", 10000, "UNABLE TO GET TO INLAND MARINE SUB-WIZARD STEPS");
        clickWhenClickable(link_SideMenuSquireIMPersonalEquipment);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Personal Property')]", 10000, "UNABLE TO GET TO PERSONAL PROPERTY PAGE");
    }

    public void clickSideMenuStandAloneIMPersonalProperty() throws Exception {
        clickWhenClickable(link_SideMenuSquireIMPersonalEquipment);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Personal Property')]", 10000, "UNABLE TO GET TO PERSONAL PROPERTY PAGE");
    }

    public void clickSideMenuIMCargo() throws Exception {
        clickWhenClickable(link_SideMenuInlandMarineGroup);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@class, 'tree-node') and text()='Cargo']", 10000, "UNABLE TO GET TO INLAND MARINE SUB-WIZARD STEPS");
        clickWhenClickable(link_SideMenuSquireIMCargo);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Cargo')]", 10000, "UNABLE TO GET TO CARGO PAGE");
    }

    public void clickSideMenuIMLivestock() throws Exception {
        clickWhenClickable(link_SideMenuInlandMarineGroup);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@class, 'tree-node') and text()='Livestock']", 10000, "UNABLE TO GET TO INLAND MARINE SUB-WIZARD STEPS");
        clickWhenClickable(link_SideMenuSquireIMLivestock);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Livestock')]", 10000, "UNABLE TO GET TO LIVESTOCK PAGE");
    }
    // END R2

    public void clickSideMenuQualification() throws Exception {
        clickWhenClickable(link_SideMenuQualification);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Qualification')]", 10000, "UNABLE TO GET TO QUALIFICATION PAGE");
    }

    public void clickSideMenuPolicyContract() {
        clickWhenClickable(link_SideMenuPolicyContract);

    }

    public void clickSideMenuPolicyInfo() throws Exception{
        clickWhenClickable(link_SideMenuPolicyInfo);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') or contains(@class, 'g-title') and contains(text(), 'Policy Info')]", 10000, "UNABLE TO GET TO POLICY INFO PAGE");
    }

    public void clickSideMenuBusinessownersLine() {

        clickWhenClickable(link_SideMenuBusinessownersLine);

    }

    public void clickSideMenuLocations() throws Exception {
        clickWhenClickable(link_SideMenuLocations);       
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, 'LocationsScreen:ttlBar') or contains(text(), 'Location Information') or contains(@id, 'LocationScreen:LocationDetailPanelSet:')]", 10000, "UNABLE TO GET TO LOCATIONS PAGE");
        }

    public void clickSideMenuBuildings() {
        clickWhenClickable(link_SideMenuBuildings);
    }

    public void clickSideMenuUsersSecurity() {
        clickWhenClickable(link_SideMenuUsersSecurity);
    }

    public void clickSideMenuUsersSecurityRoles() {
        clickWhenClickable(link_SideMenuUsersSecurityRoles);
    }

    public void clickSideMenuSupplemental() {
        clickWhenClickable(link_SideMenuSupplemental);
    }

    public void clickSideMenuModifiers() throws Exception {
        clickWhenClickable(link_SideMenuModifiers);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Modifiers')]", 10000, "UNABLE TO GET TO MODIFIERS PAGE");
        }

    public void clickSideMenuPayerAssignment() throws Exception {
        clickWhenClickable(link_SideMenuPayerAssignment);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Payer Assignment')]", 10000, "UNABLE TO GET TO PAYER ASSIGNMENT PAGE");
    }

    public void clickSideMenuRiskAnalysis() {

        try {
            clickWhenClickable(link_SideMenuRiskAnalysis);
            long endTime = new Date().getTime() + 5000;
            while (finds(By.xpath("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]")).isEmpty() && new Date().getTime() < endTime) {

            }

        } catch (Exception e) {
            try {
                clickWhenClickable(find(By.xpath("//*[@id='SubmissionWizard:LOBWizardStepGroup:Modifiers']/parent::tr")));
            } catch (Exception e1) {
                try {
                    clickWhenClickable(find(By.xpath("//*[@id='SubmissionWizard:LOBWizardStepGroup:Modifiers']/div")));
                } catch (Exception e2) {
                    clickWhenClickable(find(By.xpath("//*[@id='SubmissionWizard:LOBWizardStepGroup:Modifiers']/div/child::span")));
                }
            }
        }

    }

    public void clickSideMenuPolicyReview() throws Exception {
        clickWhenClickable(link_SideMenuPolicyReview);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Policy Review')]", 10000, "UNABLE TO GET TO POLICY REVIEW PAGE");
    }

    public void clickSideMenuPolicyChangeReview() throws Exception {
        clickWhenClickable(link_SideMenuPolicyReview);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Policy Change Review')]", 10000, "UNABLE TO GET TO POLICY CHANGE REVIEW PAGE");
    }

    public void clickSideMenuQuote() throws Exception {
        clickWhenClickable(link_SideMenuQuote);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Quote')]", 10000, "UNABLE TO GET TO QUOTE PAGE");
    }

    public void clickSideMenuForms() throws Exception {
        clickWhenClickable(link_SideMenuForms);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') or contains(@id, 'PolicyFile_PolicyLine_FormsScreen:0') and contains(text(), 'Forms')]", 10000, "UNABLE TO GET TO FORMS PAGE");
        }

    public void clickSideMenuPayment() throws Exception {
        clickWhenClickable(link_SideMenuPayment);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Payment')]", 10000, "UNABLE TO GET TO PAYMENT PAGE");
    }

    public void clickSideMenuToolsSummary() throws Exception {
        clickWhenClickable(link_SideMenuToolsSummary);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') or contains(@class, 'g-title') and contains(text(), 'Summary')]", 10000, "UNABLE TO GET TO SUMMARY PAGE");
    }

    public void clickSideMenuToolsBilling() throws Exception {
        clickWhenClickable(link_SideMenuToolsBilling);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Billing')]", 10000, "UNABLE TO GET TO BILLING PAGE");
    }

    public void clickSideMenuToolsContacts() throws Exception {
        clickWhenClickable(link_SideMenuToolsContacts);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Contacts')]", 10000, "UNABLE TO GET TO CONTACTS PAGE");
    }

    public void clickSideMenuToolsParticipants() throws Exception {
        clickWhenClickable(link_SideMenuToolsParticipants);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Participants')]", 10000, "UNABLE TO GET TO PARTICIPANTS PAGE");
    }

    public void clickSideMenuToolsNotes() throws Exception {
        clickWhenClickable(link_SideMenuToolsNotes);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Notes')]", 10000, "UNABLE TO GET TO NOTES PAGE");
    }

    public void clickSideMenuToolsDocuments() throws Exception {
        clickWhenClickable(link_SideMenuToolsDocuments);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Documents')]", 10000, "UNABLE TO GET TO DOCUMENTS PAGE");
    }

    public void clickSideMenuToolsFileMarkers() throws Exception {
        clickWhenClickable(link_SideMenuToolsFileMarkers);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'File Markers')]", 10000, "UNABLE TO GET TO FILE MARKERS PAGE");
    }

    public void clickSideMenuToolsWorkOrders() throws Exception {
        clickWhenClickable(link_SideMenuToolsWorkOrders);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Policy Transactions')]", 10000, "UNABLE TO GET TO POLICY TRANSACTIONS PAGE");
    }

    public void clickSideMenuToolsWorkplan() throws Exception {
        clickWhenClickable(link_SideMenuToolsWorkplan);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Workplan')]", 10000, "UNABLE TO GET TO WORKPLAN PAGE");
    }

    public void clickSideMenuToolsRiskAnalysis() throws Exception {
        clickWhenClickable(link_SideMenuToolsRiskAnalysis);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, 'Job_RiskAnalysisScreen') and contains(text(), 'Risk Analysis')]", 10000, "UNABLE TO GET TO RISK ANALYSIS PAGE");
    }

    public void clickSideMenuToolsHistory() throws Exception {
        clickWhenClickable(link_SideMenuToolsHistory);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'History')]", 10000, "UNABLE TO GET TO HISTORY PAGE");
    }

    public void clickSideMenuPLInsuranceScore() throws Exception {
        clickWhenClickable(link_SideMenuInsuranceScore);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Insurance Score')]", 10000, "UNABLE TO GET TO INSURANCE SCORE PAGE");
    }

    public void clickSideMenuChargesToBC() throws Exception {
        clickWhenClickable(link_SideMenuChargesToBC);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'ChargesToBC')]", 10000, "UNABLE TO GET TO CHARGESTOBC PAGE");
    }

    public boolean isLineReviewPresent() {
        return checkIfElementExists(link_SideMenuPALineReview, 200);
    }

    public void clickSideMenuPropertyLocations() throws GuidewireNavigationException {
        clickWhenClickable(link_SideMenuSquirePropertyLiability);
        new GuidewireHelpers(getDriver()).isOnPage("//span[text()='Locations']", 10000, "UNABLE TO GET TO PROPERTY AND LIABILITY SUB-WIZARD STEPS");
        clickWhenClickable(link_SideMenuSquireLocations);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Locations')]", 10000, "UNABLE TO GET TO LOCATIONS PAGE");
    }

    public void clickSideMenuStandardIMCoverageSelection() throws Exception {
        clickWhenClickable(link_SideMenuStandardIMCoverageSelection);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Coverage Selection')]", 10000, "UNABLE TO GET TO COVERAGE SELECTION PAGE");
    }

    public void clickSideMenuStandardIMFarmEquipment() throws Exception {
        clickWhenClickable(link_SideMenuStandardIMFarmEquipment);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Farm Equipment')]", 10000, "UNABLE TO GET TO FARM EQUIPMENT PAGE");
    }

    public void clickSideMenuStandardIMPersonalProperty() throws Exception {
        clickWhenClickable(link_SideMenuStandardIMPersonalProperty);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Personal Property')]", 10000, "UNABLE TO GET TO PERSONAL PROPERTY PAGE");
    }

    public void clickSideMenuStandardIMLineReview() throws Exception {
        clickWhenClickable(link_SideMenuStandardIMLineReview);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Line Selection')]", 10000, "UNABLE TO GET TO IM - LINE SELECTION PAGE");
    }

    public void clickSideMenuClueAuto() throws Exception {
        clickWhenClickable(link_SideMenuSquireAutoCLUE);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'CLUE Auto')]", 10000, "UNABLE TO GET TO CLUE AUTO PAGE");
    }

    public void clickRenewalInformation() throws Exception {
        clickWhenClickable(link_SideMenuRenewalInformation);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Renewal Information')]", 10000, "UNABLE TO GET TO RENEWAL INFORMATION PAGE");
    }

    public void clickSideMenuPAModifiers() throws Exception {
        //Select modifiers link in issuance job
        clickWhenClickable(link_SideMenuModifiers);
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Modifiers')]", 10000, "UNABLE TO GET TO MODIFIERS PAGE");
    }

    //Policy Contract
    public void clickPolicyContractQuote() {
        clickWhenClickable(find(By.xpath("//td[contains(@id, 'PolicyFile:PolicyFileAcceleratedMenuActions:PolicyMenuItemSet:PolicyMenuItemSet_Pricing') and contains(.,'Quote')]")));
    }

    public void clickPolicyContractSectionIIIAutoLine() {
        clickWhenClickable(link_SideMenuPolicyContractSectionIIAutoLine);
    }

    public void clickPolicyContractVehicles() {
        clickPolicyContractSectionIIIAutoLine();
        clickWhenClickable(link_SideMenuPolicyContractSectionVehicles);
    }
}
