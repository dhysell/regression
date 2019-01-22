package repository.gw.generate.cc;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import persistence.globaldatarepo.helpers.ClaimsDataHelper;
import repository.cc.claim.FindPolicyUnverified;
import repository.cc.claim.ValidationResults;
import repository.cc.claim.incidents.EditIncident;
import repository.cc.claim.searchpages.AdvancedSearchCC;
import repository.cc.entities.InvolvedParty;
import repository.cc.enums.Catastrophe;
import repository.cc.enums.PolicyType;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.ClaimSearchLineOfBusiness;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.enums.Incidents;
import repository.gw.login.Login;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GenerateFNOL extends BasePage {

//    private WaitUtils waitUtils;
    private WebDriver driver;

    public static class Builder {
        private List<InvolvedParty> partiesInvolved = new ArrayList<>();
        private Catastrophe catastrophe = null;
        private String claimNumber = null;
        private String creatorUserName = null;
        private String creatorPassword = null;
        private String policyNumber = null;
        private String fnolType = null;
        private repository.gw.enums.ClaimSearchLineOfBusiness lob = null;
        private LocalDate dateOfLoss = null;
        private String lossCause = null;
        private String lossRouter = null;
        private String address = null;
        private String lossDescription = null;
        private String specificIncident = null;
        private String topLevelCoverage = "Random";
        private Boolean isVerifiedPolicy = true;
        private PolicyType policyType = null;
        private String assignedUser = null;
        private WebDriver driver;

        public Builder(WebDriver driver) {
            this.driver = driver;
        }

        public String getClaimNumber() {
            return claimNumber;
        }

        public String getPolicyNumber() {
            return policyNumber;
        }

        public LocalDate getDateOfLoss() {
            return dateOfLoss;
        }

        public String getAssignedUser() {
            return assignedUser;
        }

        public GenerateFNOL build(repository.gw.enums.GenerateFNOLType typeToGenerate) throws Exception {
            this.fnolType = typeToGenerate.getValue();
            return new GenerateFNOL(typeToGenerate, this);

        }

        public Builder withCatastrophe(Catastrophe catastrophe) {
            this.catastrophe = catastrophe;
            return this;
        }

        public Builder withPartiesInvolved(List<InvolvedParty> partiesInvolved) {
            this.partiesInvolved = partiesInvolved;
            return this;
        }

        public Builder withPolicyType(PolicyType policyType) {
            this.policyType = policyType;
            return this;
        }

        public Builder withVerifiedPolicy(Boolean isVerifiedPolicy) {
            this.isVerifiedPolicy = isVerifiedPolicy;
            return this;
        }

        public Builder withClaimNumber(String claimNumber) {
            this.claimNumber = claimNumber;
            return this;
        }

        public Builder withCreatorUserNamePassword(String string, String creatorPassword) {
            this.creatorUserName = string;
            this.creatorPassword = creatorPassword;
            return this;
        }

        public Builder withPolicyNumber(String policyNumber) {
            this.policyNumber = policyNumber;
            return this;
        }

        public Builder withLOB(repository.gw.enums.ClaimSearchLineOfBusiness lob) {
            this.lob = lob;
            return this;
        }


        public Builder withLossRouter(String lossRouter) {
            this.lossRouter = lossRouter;
            return this;
        }

        public Builder withAdress(String address) {
            this.address = address;
            return this;
        }

        public Builder withLossDescription(String lossDescription) {
            this.lossDescription = lossDescription;
            return this;
        }

        public Builder withSpecificIncident(String incident) {
            this.specificIncident = incident;
            return this;
        }

        public Builder withLossCause(String lossCause) {
            this.lossCause = lossCause;
            return this;
        }

        public Builder withtopLevelCoverage(String exposureType) {
            this.topLevelCoverage = exposureType;
            return this;
        }

        public Builder withDateOfLoss(LocalDate dateOfLoss) {
            this.dateOfLoss = dateOfLoss;
            return this;
        }
    }

    private List<InvolvedParty> partiesInvolved = new ArrayList<>();
    public Catastrophe catastrophe = null;
    public String claimNumber = null;
    private String creatorUserName = null;
    private String creatorPassword = null;
    private String policyNumber = null;
    private String fnolType = null;
    private repository.gw.enums.ClaimSearchLineOfBusiness lob = null;
    private String lossRouter = null;
    public String lossCause = null;
    public String lossDescription = null;
    public String specificIncident = null;
    public LocalDate dateOfLoss = null;
    private String address = null;
    private String topLevelCoverage = null;
    private Boolean isVerifiedPolicy = null;
    private PolicyType policyType = null;
    public String assignedUser = null;

    public GenerateFNOL(GenerateFNOLType typeToGenerate, Builder builderDetails) throws Exception {
        super(builderDetails.driver);
        this.driver = builderDetails.driver;

        this.isVerifiedPolicy = builderDetails.isVerifiedPolicy;

        if (!this.isVerifiedPolicy) {
            unverifiedPolicyFNOL(builderDetails);
        } else {
            switch (typeToGenerate) {
                case Auto:
                    autoFNOL(builderDetails);
                    break;
                case AutoGlass:
                    autoGlassFNOL(builderDetails);
                    break;
                case GeneralLiability:
                    generalLiabilityFNOL(builderDetails);
                    break;
                case InlandMarine:
                    inlandMarineFNOL(builderDetails);
                    break;
                case Property:
                    propertyFNOL(builderDetails);
                    break;
                case ResidentialGlass:
                    residentialGlassFNOL(builderDetails);
                    break;
                case CropHail:
                    cropHailFNOL(builderDetails);
                    break;
                case PortalDraft:
                    finishPortalFNOL(builderDetails);
                case Membership:
                    membershipFNOL(builderDetails);
                default:
                    break;
            }
        }
    }

    private List<String> searchPolicyNumber(String policyNumber, ClaimSearchLineOfBusiness lineOfBusiness) {

        if (lineOfBusiness != null) {
            this.lob = lineOfBusiness;
        }

        // Bypass search page if policy number is provided.
        
        List<String> policyNumbers = new ArrayList<>();

        if (policyNumber == null || policyNumber.equalsIgnoreCase("Random")) {
            TopMenu topMenu = new TopMenu(this.driver);
            AdvancedSearchCC advanceSearch = topMenu.clickSearchTab().clickAdvancedSearch().searchForClaimNumbers(policyNumber, fnolType, this.lob, null);

            policyNumbers = advanceSearch.gatherPolicyRootNumbers(fnolType);
            this.policyNumber = advanceSearch.getRandomPolicyNumberFromList(policyNumbers);
        } else {
            policyNumbers.add(policyNumber);
            this.policyNumber = policyNumber;
        }

        return policyNumbers;
    }

    private void membershipFNOL(Builder builderStuff) throws Exception {
        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.policyNumber = builderStuff.policyNumber;
        this.fnolType = builderStuff.fnolType;
        this.lossDescription = builderStuff.lossDescription;
        this.lossRouter = builderStuff.lossRouter;
        this.lossCause = builderStuff.lossCause;
        this.setAddress(builderStuff.address);
        this.specificIncident = builderStuff.specificIncident;
        this.topLevelCoverage = builderStuff.topLevelCoverage;
        this.dateOfLoss = builderStuff.dateOfLoss;
        this.lob = builderStuff.lob;
        this.isVerifiedPolicy = builderStuff.isVerifiedPolicy;
        this.catastrophe = builderStuff.catastrophe;

        TopMenu topMenu = new TopMenu(this.driver);
        repository.cc.claim.FindPolicy findPolicy = new repository.cc.claim.FindPolicy(this.driver);
        repository.cc.claim.BasicInfo basicInfo = new repository.cc.claim.BasicInfo(this.driver);
        repository.cc.claim.LossDetails lossDetails = new repository.cc.claim.LossDetails(this.driver);
        repository.cc.claim.NewClaimSaved newClaimSaved = new repository.cc.claim.NewClaimSaved(this.driver);
        repository.cc.claim.VacationStatusCC vacationStatus = new repository.cc.claim.VacationStatusCC(this.driver);
        repository.gw.login.Login login = new repository.gw.login.Login(this.driver);

        login.login(creatorUserName, creatorPassword);
        vacationStatus.setVacationStatusAtWork();

        List<String> policyNumbers = searchPolicyNumber(this.policyNumber, lob);

        // Find Policy - Step 1
        topMenu.clickClaimTabArrow();
        
        topMenu.clickNewClaimLink();
        

        this.dateOfLoss = findPolicy.searchOrCreatePolicy(policyNumber, policyNumbers, fnolType, dateOfLoss, repository.gw.enums.PolicyType.Membership);
        waitUntilElementIsClickable(By.xpath("//span[@id='FNOLWizard:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']"), 60);

        // Basic Info - Step 2
        String insuredName = basicInfo.getNameOfInsured();
        basicInfo.clickNameTrigger();
        
        basicInfo.selectSpecific_ReportedByName(insuredName);
        
        basicInfo.setRandomMobileNumber();
        basicInfo.setEmailAddress("NOEMAIL@IDFBINS.COM");

        basicInfo.clickNextButton();

        basicInfo.checkPhoneNumberErrorMessages();
        
        basicInfo.clickCloseButtonIfPresent();
        
        lossDetails.populateLossDescription(lossDescription);
        

        if (lossCause.equalsIgnoreCase("Random")) {
            this.lossCause = lossDetails.selectRandom_LossCause();
        } else {
            lossDetails.selectSpecific_LossCause(lossCause);
        }

        if (catastrophe != null) {
            lossDetails.setCatastrophe(catastrophe.getSelectionText());
        }

        lossDetails.selectRandom_Location();

        

        // Add Other Incident
        lossDetails.clickAddOtherIncident().createOtherIncident();

        
        lossDetails.clickFinishButton();

        ValidationResults validation = new ValidationResults(this.driver);
        if (validation.isValidationResultsVisible()) {
            validation.clickClearButton();
            lossDetails.clickFinishButton();
        }

        List<String> claimSavedData = newClaimSaved.waitForNewClaimSaved(lossRouter);
        this.assignedUser = claimSavedData.get(1);
        this.claimNumber = claimSavedData.get(0);

        ClaimsDataHelper.writeFNOL(claimNumber, policyNumber, dateOfLoss.toString(), this.fnolType, this.driver.getCurrentUrl());
    }



    private void autoFNOL(Builder builderStuff) throws Exception {
        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.policyNumber = builderStuff.policyNumber;
        this.fnolType = builderStuff.fnolType;
        this.lossDescription = builderStuff.lossDescription;
        this.lossRouter = builderStuff.lossRouter;
        this.lossCause = builderStuff.lossCause;
        this.setAddress(builderStuff.address);
        this.specificIncident = builderStuff.specificIncident;
        this.topLevelCoverage = builderStuff.topLevelCoverage;
        this.dateOfLoss = builderStuff.dateOfLoss;
        this.lob = builderStuff.lob;
        this.isVerifiedPolicy = builderStuff.isVerifiedPolicy;
        this.catastrophe = builderStuff.catastrophe;

        TopMenu topMenu = new TopMenu(this.driver);
        repository.cc.claim.FindPolicy findPolicy = new repository.cc.claim.FindPolicy(this.driver);
        repository.cc.claim.BasicInfo basicInfo = new repository.cc.claim.BasicInfo(this.driver);
        repository.cc.claim.LossDetails lossDetails = new repository.cc.claim.LossDetails(this.driver);
        repository.cc.claim.NewClaimSaved newClaimSaved = new repository.cc.claim.NewClaimSaved(this.driver);
        repository.cc.claim.VacationStatusCC vacationStatus = new repository.cc.claim.VacationStatusCC(this.driver);
        repository.gw.login.Login login = new repository.gw.login.Login(this.driver);

        login.login(creatorUserName, creatorPassword);
        vacationStatus.setVacationStatusAtWork();

        List<String> policyNumbers = searchPolicyNumber(this.policyNumber, lob);

        // Find Policy - Step 1
        topMenu.clickClaimTabArrow();
        
        topMenu.clickNewClaimLink();
        

        this.dateOfLoss = findPolicy.searchOrCreatePolicy(policyNumber, policyNumbers, fnolType, dateOfLoss, null);
        waitUntilElementIsClickable(By.xpath("//span[@id='FNOLWizard:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']"),60);

        // Basic Info - Step 2
        String insuredName = basicInfo.getNameOfInsured();
        basicInfo.clickNameTrigger();
        
        basicInfo.selectSpecific_ReportedByName(insuredName);
        
        basicInfo.setRandomMobileNumber();
        basicInfo.setEmailAddress("NOEMAIL@IDFBINS.COM");

        this.specificIncident = basicInfo.incidentPicker(1, specificIncident, fnolType, topLevelCoverage, lossCause, 0);

        basicInfo.clickNextButton();

        basicInfo.checkPhoneNumberErrorMessages();
        
        basicInfo.clickCloseButtonIfPresent();
        
        lossDetails.populateLossDescription(lossDescription);
        

        if (lossCause.equalsIgnoreCase("Random")) {
            this.lossCause = lossDetails.selectRandom_LossCause();
        } else {
            lossDetails.selectSpecific_LossCause(lossCause);
        }

        if (catastrophe != null) {
            lossDetails.setCatastrophe(catastrophe.getSelectionText());
        }

        

        if (lossRouter.equalsIgnoreCase("Random")) {
            lossRouter = lossDetails.selectRandom_LossRouter();
        } else {
            lossDetails.selectSpecific_LossRouter(lossRouter);
        }

        lossDetails.selectRandom_Location();

        

        // Cycle through all incidents and add information.
        clickIncidents();

        
        lossDetails.clickFinishButton();
        List<String> claimSavedData = newClaimSaved.waitForNewClaimSaved(lossRouter);
        this.assignedUser = claimSavedData.get(1);
        this.claimNumber = claimSavedData.get(0);

        ClaimsDataHelper.writeFNOL(claimNumber, policyNumber, dateOfLoss.toString(), this.fnolType, driver.getCurrentUrl());
    }

    private void unverifiedPolicyFNOL(Builder builderStuff) throws Exception {

        // Method specific variables
        this.partiesInvolved = builderStuff.partiesInvolved;
        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.policyType = builderStuff.policyType;
        this.policyNumber = builderStuff.policyNumber;
        this.fnolType = builderStuff.fnolType;
        this.lossDescription = builderStuff.lossDescription;
        this.lossRouter = builderStuff.lossRouter;
        this.lossCause = builderStuff.lossCause;
        this.setAddress(builderStuff.address);
        this.specificIncident = builderStuff.specificIncident;
        this.topLevelCoverage = builderStuff.topLevelCoverage;
        this.dateOfLoss = builderStuff.dateOfLoss;
        this.lob = builderStuff.lob;
        this.catastrophe = builderStuff.catastrophe;

        // Set Product
        repository.gw.login.Login login = new repository.gw.login.Login(this.driver);

        // Imports From Factory
        TopMenu topMenu = new TopMenu(this.driver);
        repository.cc.claim.FindPolicy findPolicy = new repository.cc.claim.FindPolicy(this.driver);
        repository.cc.claim.FindPolicyUnverified unverifiedPolicy = new FindPolicyUnverified(this.driver);
        repository.cc.claim.BasicInfo basicInfo = new repository.cc.claim.BasicInfo(this.driver);
        repository.cc.claim.LossDetails lossDetails = new repository.cc.claim.LossDetails(this.driver);
        repository.cc.claim.NewClaimSaved newClaimSaved = new repository.cc.claim.NewClaimSaved(this.driver);
        repository.cc.claim.VacationStatusCC vacationStatus = new repository.cc.claim.VacationStatusCC(this.driver);

        login.login(creatorUserName, creatorPassword);
        
        vacationStatus.setVacationStatusAtWork();
        

        // Find Policy - Step 1
        topMenu.clickClaimTabArrow();
        
        topMenu.clickNewClaimLink();
        

        findPolicy.clickCreateUnverifiedPolicy();
        unverifiedPolicy.waitForPageToLoad();
        unverifiedPolicy.setPolicyNumber(policyNumber);
        unverifiedPolicy.setType(policyType.getPolicyText());
        
        unverifiedPolicy.setTypeOfClaim(fnolType);
        unverifiedPolicy.setDateOfLoss(dateOfLoss);
        unverifiedPolicy.setEffectiveDate(dateOfLoss.minusMonths(6));
        unverifiedPolicy.setExpirationDate(dateOfLoss.plusMonths(6));

        InvolvedParty newUser = unverifiedPolicy.searchInsuredNameCreateContact("Other");
        partiesInvolved.add(newUser);

        unverifiedPolicy.setInsuredName(newUser.getFirstName() + " " + newUser.getLastName());

        unverifiedPolicy.addNewVehicle();

        findPolicy.clickNextButton();

        // Basic Info - Step 2
        String insuredName = basicInfo.getNameOfInsured();
        basicInfo.clickNameTrigger();
        
        basicInfo.selectSpecific_ReportedByName(insuredName);
        
        basicInfo.setRandomMobileNumber();
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        
        basicInfo.setEmailAddress("NOEMAIL@IDFBINS.COM");

        
        this.specificIncident = basicInfo.incidentPicker(1, specificIncident, fnolType, topLevelCoverage, lossCause, 0);

        
        basicInfo.clickNextButton();
        
        basicInfo.checkPhoneNumberErrorMessages();
        
        basicInfo.clickCloseButtonIfPresent();
        
        lossDetails.populateLossDescription(lossDescription);
        

        if (lossCause.equalsIgnoreCase("Random")) {
            this.lossCause = lossDetails.selectRandom_LossCause();
        } else {
            lossDetails.selectSpecific_LossCause(lossCause);
        }

        

        if (lossRouter.equalsIgnoreCase("Random")) {
            lossRouter = lossDetails.selectRandom_LossRouter();
        } else {
            lossDetails.selectSpecific_LossRouter(lossRouter);
        }

        if (catastrophe != null) {
            lossDetails.setCatastrophe(catastrophe.getSelectionText());
        }

        lossDetails.selectRandom_Location();

        

        // Cycle through all incidents and add information.
        clickIncidents();

        
        lossDetails.clickFinishButton();
        List<String> claimSavedData = newClaimSaved.waitForNewClaimSaved(lossRouter);
        this.assignedUser = claimSavedData.get(1);
        this.claimNumber = claimSavedData.get(0);

        ClaimsDataHelper.writeFNOL(claimNumber, policyNumber, dateOfLoss.toString(), this.fnolType, driver.getCurrentUrl());
    }

    public List<InvolvedParty> getPartiesInvolved() {
        return partiesInvolved;
    }

    private void autoGlassFNOL(Builder builderStuff) throws Exception {
        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.policyNumber = builderStuff.policyNumber;
        this.fnolType = builderStuff.fnolType;
        this.lossDescription = builderStuff.lossDescription;
        this.lossRouter = builderStuff.lossRouter;
        this.lossCause = builderStuff.lossCause;
        this.setAddress(builderStuff.address);
        this.specificIncident = builderStuff.specificIncident;
        this.topLevelCoverage = builderStuff.topLevelCoverage;
        this.dateOfLoss = builderStuff.dateOfLoss;
        this.lob = builderStuff.lob;
        this.catastrophe = builderStuff.catastrophe;

        repository.gw.login.Login login = new repository.gw.login.Login(this.driver);

        TopMenu topMenu = new TopMenu(this.driver);
        repository.cc.claim.FindPolicy findPolicy = new repository.cc.claim.FindPolicy(this.driver);
        repository.cc.claim.BasicInfo basicInfo = new repository.cc.claim.BasicInfo(this.driver);
        repository.cc.claim.NewClaimSaved newClaimSaved = new repository.cc.claim.NewClaimSaved(this.driver);
        repository.cc.claim.AutoERSorGlass autoERSorGlass = new repository.cc.claim.AutoERSorGlass(this.driver);
        repository.cc.claim.VacationStatusCC vacationStatus = new repository.cc.claim.VacationStatusCC(this.driver);

        login.login(creatorUserName, creatorPassword);
        
        vacationStatus.setVacationStatusAtWork();
        

        List<String> policyNumbers = searchPolicyNumber(this.policyNumber, lob);

        // Find Policy - Step 1
        topMenu.clickClaimTabArrow();
        
        topMenu.clickNewClaimLink();
        
        this.dateOfLoss = findPolicy.searchOrCreatePolicy(policyNumber, policyNumbers, fnolType, dateOfLoss, null);
        waitUntilElementIsClickable(By.xpath("//span[@id='FNOLWizard:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']"),60);
        // Basic Info - Step 2

        String insuredName = basicInfo.getNameOfInsured();
        autoERSorGlass.selectSpecific_ReportedByNameERS(insuredName);
        
        autoERSorGlass.selectSpecific_RelationERS("Self");
        
        autoERSorGlass.setLossDescriptionERS(lossDescription);
        

        if (lossCause.equalsIgnoreCase("Random")) {
            this.lossCause = autoERSorGlass.selectRandom_LossCauseERS();
        } else {
            autoERSorGlass.selectSpecific_LossCauseERS(lossCause);
        }

        
        this.specificIncident = basicInfo.incidentPicker(1, specificIncident, fnolType, topLevelCoverage, lossCause, 0);
        
        String lossCause = autoERSorGlass.getLossCauseERSValue();
        if (lossCause.equalsIgnoreCase("Auto Glass-Windshield")) {
            autoERSorGlass.clickRadioRepairYes();
        }
        
        autoERSorGlass.clickFinishButton();

        autoERSorGlass.errorChecking();
        
        List<String> claimSavedData = newClaimSaved.waitForNewClaimSaved(lossRouter);
        this.assignedUser = claimSavedData.get(1);
        this.claimNumber = claimSavedData.get(0);

        ClaimsDataHelper.writeFNOL(claimNumber, policyNumber, dateOfLoss.toString(), this.fnolType, driver.getCurrentUrl());
    }

    private void generalLiabilityFNOL(Builder builderStuff) throws Exception {
        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.policyNumber = builderStuff.policyNumber;
        this.fnolType = builderStuff.fnolType;
        this.lossDescription = builderStuff.lossDescription;
        this.lossRouter = builderStuff.lossRouter;
        this.lossCause = builderStuff.lossCause;
        this.setAddress(builderStuff.address);
        this.specificIncident = builderStuff.specificIncident;
        this.topLevelCoverage = builderStuff.topLevelCoverage;
        this.dateOfLoss = builderStuff.dateOfLoss;
        this.lob = builderStuff.lob;
        this.catastrophe = builderStuff.catastrophe;

        repository.gw.login.Login login = new repository.gw.login.Login(this.driver);

        TopMenu topMenu = new TopMenu(this.driver);
        repository.cc.claim.FindPolicy findPolicy = new repository.cc.claim.FindPolicy(this.driver);
        repository.cc.claim.BasicInfo basicInfo = new repository.cc.claim.BasicInfo(this.driver);
        repository.cc.claim.LossDetails lossDetails = new repository.cc.claim.LossDetails(this.driver);
        repository.cc.claim.NewClaimSaved newClaimSaved = new repository.cc.claim.NewClaimSaved(this.driver);
        repository.cc.claim.VacationStatusCC vacationStatus = new repository.cc.claim.VacationStatusCC(this.driver);

        login.login(creatorUserName, creatorPassword);
        
        vacationStatus.setVacationStatusAtWork();

        List<String> policyNumbers = searchPolicyNumber(this.policyNumber, lob);

        // Find Policy - Step 1
        topMenu.clickClaimTabArrow();
        
        topMenu.clickNewClaimLink();
        
        this.dateOfLoss = findPolicy.searchOrCreatePolicy(policyNumber, policyNumbers, fnolType, dateOfLoss, null);
        waitUntilElementIsClickable(By.xpath("//span[@id='FNOLWizard:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']"),90);
        // Basic Info - Step 2

        String insuredName = basicInfo.getNameOfInsured();
        basicInfo.clickNameTrigger();
        
        basicInfo.selectSpecific_ReportedByName(insuredName);
        
        basicInfo.setRandomMobileNumber();
        basicInfo.setEmailAddress("NOEMAIL@IDFBINS.COM");
        

        if (!specificIncident.equalsIgnoreCase("New")) {
            this.specificIncident = basicInfo.incidentPicker(1, specificIncident, fnolType, topLevelCoverage, lossCause, 0);
        }

        
        basicInfo.clickNextButton();
        
        basicInfo.checkPhoneNumberErrorMessages();
        
        basicInfo.clickCloseButtonIfPresent();
        
        lossDetails.populateLossDescription(lossDescription);
        

        if (lossCause.equalsIgnoreCase("Random")) {
            this.lossCause = lossDetails.selectRandom_LossCause();
        } else {
            lossDetails.selectSpecific_LossCause(lossCause);
        }

        if (lossRouter.equalsIgnoreCase("Random")) {
            lossRouter = lossDetails.selectRandom_LossRouter();
        } else {
            lossDetails.selectSpecific_LossRouter(lossRouter);
        }

        if (catastrophe != null) {
            lossDetails.setCatastrophe(catastrophe.getSelectionText());
        }

        lossDetails.selectRandom_Location();

        

        // Cycle through all incidents and add information.
        if (specificIncident.equalsIgnoreCase("New")) {
            repository.cc.claim.Incidents otherIncident = lossDetails.clickAddOtherIncident();
            otherIncident.setOtherIncidentClaimDescription("Other Incident");
            otherIncident.setNewOtherIncidentClaimantRandom();
            otherIncident.clickOkButton();
        } else {
            clickIncidents();
        }

        
        lossDetails.clickFinishButton();
        List<String> claimSavedData = newClaimSaved.waitForNewClaimSaved(lossRouter);
        this.assignedUser = claimSavedData.get(1);
        this.claimNumber = claimSavedData.get(0);

        ClaimsDataHelper.writeFNOL(claimNumber, policyNumber, dateOfLoss.toString(), this.fnolType, driver.getCurrentUrl());
    }

    private void inlandMarineFNOL(Builder builderStuff) throws Exception {
        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.policyNumber = builderStuff.policyNumber;
        this.fnolType = builderStuff.fnolType;
        this.lossDescription = builderStuff.lossDescription;
        this.lossRouter = builderStuff.lossRouter;
        this.lossCause = builderStuff.lossCause;
        this.setAddress(builderStuff.address);
        this.specificIncident = builderStuff.specificIncident;
        this.topLevelCoverage = builderStuff.topLevelCoverage;
        this.dateOfLoss = builderStuff.dateOfLoss;
        this.lob = builderStuff.lob;
        this.catastrophe = builderStuff.catastrophe;

        repository.gw.login.Login login = new repository.gw.login.Login(this.driver);

        TopMenu topMenu = new TopMenu(this.driver);
        repository.cc.claim.FindPolicy findPolicy = new repository.cc.claim.FindPolicy(this.driver);
        repository.cc.claim.BasicInfo basicInfo = new repository.cc.claim.BasicInfo(this.driver);
        repository.cc.claim.LossDetails lossDetails = new repository.cc.claim.LossDetails(this.driver);
        repository.cc.claim.NewClaimSaved newClaimSaved = new repository.cc.claim.NewClaimSaved(this.driver);
        repository.cc.claim.VacationStatusCC vacationStatus = new repository.cc.claim.VacationStatusCC(this.driver);

        login.login(creatorUserName, creatorPassword);
        
        vacationStatus.setVacationStatusAtWork();
        

        List<String> policyNumbers = searchPolicyNumber(this.policyNumber, lob);

        // Find Policy - Step 1
        topMenu.clickClaimTabArrow();
        
        topMenu.clickNewClaimLink();
        
        this.dateOfLoss = findPolicy.searchOrCreatePolicy(policyNumber, policyNumbers, fnolType, dateOfLoss, null);
        waitUntilElementIsClickable(By.xpath("//span[@id='FNOLWizard:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']"),60);
        // Basic Info - Step 2

        String insuredName = basicInfo.getNameOfInsured();
        basicInfo.clickNameTrigger();
        
        basicInfo.selectSpecific_ReportedByName(insuredName);
        
        basicInfo.setRandomMobileNumber();
        basicInfo.setEmailAddress("NOEMAIL@IDFBINS.COM");
        

        this.specificIncident = basicInfo.incidentPicker(1, specificIncident, fnolType, topLevelCoverage, lossCause, 0);
        
        basicInfo.clickNextButton();
        
        basicInfo.checkPhoneNumberErrorMessages();
        
        basicInfo.clickCloseButtonIfPresent();
        
        lossDetails.populateLossDescription(lossDescription);
        

        if (lossCause.equalsIgnoreCase("Random")) {
            this.lossCause = lossDetails.selectRandom_LossCause();
        } else {
            lossDetails.selectSpecific_LossCause(lossCause);
        }

        

        if (lossRouter.equalsIgnoreCase("Random")) {
            lossRouter = lossDetails.selectRandom_LossRouter();
        } else {
            lossDetails.selectSpecific_LossRouter(lossRouter);
        }

        if (catastrophe != null) {
            lossDetails.setCatastrophe(catastrophe.getSelectionText());
        }

        lossDetails.selectRandom_Location();

        

        // Cycle through all incidents and add information.
        clickIncidents();

        
        lossDetails.clickFinishButton();
        List<String> claimSavedData = newClaimSaved.waitForNewClaimSaved(lossRouter);
        this.assignedUser = claimSavedData.get(1);
        this.claimNumber = claimSavedData.get(0);

        ClaimsDataHelper.writeFNOL(claimNumber, policyNumber, dateOfLoss.toString(), this.fnolType, driver.getCurrentUrl());
    }

    private void propertyFNOL(Builder builderStuff) throws Exception {
        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.policyNumber = builderStuff.policyNumber;
        this.fnolType = builderStuff.fnolType;
        this.lossDescription = builderStuff.lossDescription;
        this.lossRouter = builderStuff.lossRouter;
        this.lossCause = builderStuff.lossCause;
        this.setAddress(builderStuff.address);
        this.specificIncident = builderStuff.specificIncident;
        this.topLevelCoverage = builderStuff.topLevelCoverage;
        this.dateOfLoss = builderStuff.dateOfLoss;
        this.lob = builderStuff.lob;
        this.catastrophe = builderStuff.catastrophe;

        repository.gw.login.Login login = new repository.gw.login.Login(this.driver);

        TopMenu topMenu = new TopMenu(this.driver);
        repository.cc.claim.FindPolicy findPolicy = new repository.cc.claim.FindPolicy(this.driver);
        repository.cc.claim.BasicInfo basicInfo = new repository.cc.claim.BasicInfo(this.driver);
        repository.cc.claim.LossDetails lossDetails = new repository.cc.claim.LossDetails(this.driver);
        repository.cc.claim.NewClaimSaved newClaimSaved = new repository.cc.claim.NewClaimSaved(this.driver);
        repository.cc.claim.VacationStatusCC vacationStatus = new repository.cc.claim.VacationStatusCC(this.driver);

        login.login(creatorUserName, creatorPassword);
        
        vacationStatus.setVacationStatusAtWork();
        

        List<String> policyNumbers = searchPolicyNumber(this.policyNumber, lob);

        // Find Policy - Step 1
        topMenu.clickClaimTabArrow();
        
        topMenu.clickNewClaimLink();

        this.dateOfLoss = findPolicy.searchOrCreatePolicy(policyNumber, policyNumbers, fnolType, dateOfLoss, null);
       waitUntilElementIsClickable(By.xpath("//span[@id='FNOLWizard:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']"),60);
        // Basic Info - Step 2

        String insuredName = basicInfo.getNameOfInsured();
        basicInfo.clickNameTrigger();
        
        basicInfo.selectSpecific_ReportedByName(insuredName);
        
        basicInfo.setRandomMobileNumber();
        basicInfo.setEmailAddress("NOEMAIL@IDFBINS.COM");
        

        this.specificIncident = basicInfo.incidentPicker(1, specificIncident, fnolType, topLevelCoverage, lossCause, 0);
        
        basicInfo.clickNextButton();
        
        basicInfo.checkPhoneNumberErrorMessages();
        
        basicInfo.clickCloseButtonIfPresent();
        
        lossDetails.populateLossDescription(lossDescription);
        

        if (lossCause.equalsIgnoreCase("Random")) {
            this.lossCause = lossDetails.selectRandom_LossCause();
        } else {
            lossDetails.selectSpecific_LossCause(lossCause);
        }

        

        if (lossRouter.equalsIgnoreCase("Random")) {
            lossRouter = lossDetails.selectRandom_LossRouter();
        } else {
            lossDetails.selectSpecific_LossRouter(lossRouter);
        }

        if (catastrophe != null) {
            lossDetails.setCatastrophe(catastrophe.getSelectionText());
        }

        lossDetails.selectRandom_Location();

        // Cycle through all incidents and add information.
        clickIncidents();

        
        lossDetails.clickFinishButton();
        List<String> claimSavedData = newClaimSaved.waitForNewClaimSaved(lossRouter);
        this.assignedUser = claimSavedData.get(1);
        this.claimNumber = claimSavedData.get(0);

        ClaimsDataHelper.writeFNOL(claimNumber, policyNumber, dateOfLoss.toString(), this.fnolType, driver.getCurrentUrl());
    }

    private void residentialGlassFNOL(Builder builderStuff) throws Exception {
        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.policyNumber = builderStuff.policyNumber;
        this.fnolType = builderStuff.fnolType;
        this.lossDescription = builderStuff.lossDescription;
        this.lossRouter = builderStuff.lossRouter;
        this.lossCause = builderStuff.lossCause;
        this.setAddress(builderStuff.address);
        this.specificIncident = builderStuff.specificIncident;
        this.topLevelCoverage = builderStuff.topLevelCoverage;
        this.dateOfLoss = builderStuff.dateOfLoss;
        this.lob = builderStuff.lob;
        this.catastrophe = builderStuff.catastrophe;

        repository.gw.login.Login login = new repository.gw.login.Login(this.driver);

        TopMenu topMenu = new TopMenu(this.driver);
        repository.cc.claim.FindPolicy findPolicy = new repository.cc.claim.FindPolicy(this.driver);
        repository.cc.claim.BasicInfo basicInfo = new repository.cc.claim.BasicInfo(this.driver);
        repository.cc.claim.NewClaimSaved newClaimSaved = new repository.cc.claim.NewClaimSaved(this.driver);
        repository.cc.claim.AutoERSorGlass autoERSorGlass = new repository.cc.claim.AutoERSorGlass(this.driver);
        repository.cc.claim.VacationStatusCC vacationStatus = new repository.cc.claim.VacationStatusCC(this.driver);

        login.login(creatorUserName, creatorPassword);
        
        vacationStatus.setVacationStatusAtWork();
        

        List<String> policyNumbers = searchPolicyNumber(this.policyNumber, lob);

        // Find Policy - Step 1
        topMenu.clickClaimTabArrow();
        
        topMenu.clickNewClaimLink();
        
        this.dateOfLoss = findPolicy.searchOrCreatePolicy(policyNumber, policyNumbers, fnolType, dateOfLoss, null);
        waitUntilElementIsClickable(By.xpath("//span[@id='FNOLWizard:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']"),60);
        // Basic Info - Step 2

        String insuredName = basicInfo.getNameOfInsured();
        autoERSorGlass.selectSpecific_ReportedByNameERS(insuredName);
        
        autoERSorGlass.selectSpecific_RelationERS("Self");
        
        autoERSorGlass.setLossDescriptionERS(lossDescription);
        

        if (lossCause.equalsIgnoreCase("Random")) {
            this.lossCause = autoERSorGlass.selectRandom_LossCauseERS();
        } else {
            autoERSorGlass.selectSpecific_LossCauseERS(lossCause);
        }
        
        this.specificIncident = basicInfo.incidentPicker(1, specificIncident, fnolType, topLevelCoverage, lossCause, 0);
        
        String lossCause = autoERSorGlass.getLossCauseERSValue();
        if (lossCause.equalsIgnoreCase("Auto Glass-Windshield")) {
            autoERSorGlass.clickRadioRepairYes();
        }
        
        autoERSorGlass.clickFinishButton();
        
        autoERSorGlass.errorChecking();
        
        List<String> claimSavedData = newClaimSaved.waitForNewClaimSaved(lossRouter);
        this.assignedUser = claimSavedData.get(1);
        this.claimNumber = claimSavedData.get(0);

        ClaimsDataHelper.writeFNOL(claimNumber, policyNumber, dateOfLoss.toString(), this.fnolType, driver.getCurrentUrl());
    }

    private void cropHailFNOL(Builder builderStuff) throws Exception {
        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.policyNumber = builderStuff.policyNumber;
        this.fnolType = builderStuff.fnolType;
        this.lossDescription = builderStuff.lossDescription;
        this.lossRouter = builderStuff.lossRouter;
        this.lossCause = builderStuff.lossCause;
        this.setAddress(builderStuff.address);
        this.specificIncident = builderStuff.specificIncident;
        this.topLevelCoverage = builderStuff.topLevelCoverage;
        this.dateOfLoss = builderStuff.dateOfLoss;
        this.lob = builderStuff.lob;
        this.catastrophe = builderStuff.catastrophe;

        repository.gw.login.Login login = new Login(this.driver);

        TopMenu topMenu = new TopMenu(this.driver);
        repository.cc.claim.FindPolicy findPolicy = new repository.cc.claim.FindPolicy(this.driver);
        repository.cc.claim.BasicInfo basicInfo = new repository.cc.claim.BasicInfo(this.driver);
        repository.cc.claim.NewClaimSaved newClaimSaved = new repository.cc.claim.NewClaimSaved(this.driver);
        repository.cc.claim.LossDetails lossDetails = new repository.cc.claim.LossDetails(this.driver);
        repository.cc.claim.VacationStatusCC vacationStatus = new repository.cc.claim.VacationStatusCC(this.driver);

        login.login(creatorUserName, creatorPassword);
        
        vacationStatus.setVacationStatusAtWork();
        

        List<String> policyNumbers = searchPolicyNumber(this.policyNumber, lob);

        // Find Policy - Step 1
        topMenu.clickClaimTabArrow();
        
        topMenu.clickNewClaimLink();
        
        this.dateOfLoss = findPolicy.searchOrCreatePolicy(policyNumber, policyNumbers, fnolType, dateOfLoss, null);
       waitUntilElementIsClickable(By.xpath("//span[@id='FNOLWizard:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']"),60);
        
//        findPolicy.clickNextButton();
        
        basicInfo.clickNameTrigger();
        
        String insuredName = basicInfo.getNameOfInsured();
        basicInfo.selectSpecific_ReportedByName(insuredName);
        
        basicInfo.setRandomMobileNumber();
        basicInfo.setEmailAddress("NOEMAIL@IDFBINS.COM");
        

        this.specificIncident = basicInfo.incidentPicker(1, specificIncident, fnolType, topLevelCoverage, lossCause, 0);

        
        basicInfo.clickNextButton();
        
        basicInfo.clickCloseButtonIfPresent();
        
        basicInfo.checkPhoneNumberErrorMessages();
        
        lossDetails.populateLossDescription(lossDescription);
        

        if (lossCause.equalsIgnoreCase("Random")) {
            this.lossCause = lossDetails.selectRandom_LossCause();
        } else {
            lossDetails.selectSpecific_LossCause(lossCause);
        }

        if (catastrophe != null) {
            lossDetails.setCatastrophe(catastrophe.getSelectionText());
        }

        lossDetails.otherInsuranceSelect();
        
        lossDetails.selectRandom_Location();
        
        lossDetails.clickFinishButton();
        
        List<String> claimSavedData = newClaimSaved.waitForNewClaimSaved(lossRouter);
        this.assignedUser = claimSavedData.get(1);
        this.claimNumber = claimSavedData.get(0);

        ClaimsDataHelper.writeFNOL(claimNumber, policyNumber, dateOfLoss.toString(), this.fnolType, driver.getCurrentUrl());
    }

    private void finishPortalFNOL(Builder builderStuff) throws Exception {
        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.policyNumber = builderStuff.policyNumber;
        this.fnolType = builderStuff.fnolType;
        this.lossDescription = builderStuff.lossDescription;
        this.lossRouter = builderStuff.lossRouter;
        this.lossCause = builderStuff.lossCause;
        this.setAddress(builderStuff.address);
        this.specificIncident = builderStuff.specificIncident;
        this.topLevelCoverage = builderStuff.topLevelCoverage;
        this.dateOfLoss = builderStuff.dateOfLoss;
        this.lob = builderStuff.lob;
        this.catastrophe = builderStuff.catastrophe;
        //Login login = new Login(this.driver);

        repository.cc.claim.FindPolicy findPolicy = new repository.cc.claim.FindPolicy(this.driver);
        repository.cc.claim.BasicInfo basicInfo = new repository.cc.claim.BasicInfo(this.driver);
        repository.cc.claim.NewClaimSaved newClaimSaved = new repository.cc.claim.NewClaimSaved(this.driver);
        repository.cc.claim.LossDetails lossDetails = new repository.cc.claim.LossDetails(this.driver);

        
        findPolicy.clickNextButton();
        
        basicInfo.clickNameTrigger();
        
        String insuredName = basicInfo.getNameOfInsured();
        basicInfo.selectSpecific_ReportedByName(insuredName);
        

        this.specificIncident = basicInfo.incidentPicker(1, specificIncident, fnolType, topLevelCoverage, lossCause, 0);

        
        basicInfo.clickNextButton();
        
        basicInfo.clickCloseButtonIfPresent();
        
        basicInfo.checkPhoneNumberErrorMessages();
        
        lossDetails.populateLossDescription(lossDescription);
        

        if (lossCause.equalsIgnoreCase("Random")) {
            this.lossCause = lossDetails.selectRandom_LossCause();
        } else {
            lossDetails.selectSpecific_LossCause(lossCause);
        }
        
        lossDetails.selectRandom_Location();
        
        lossDetails.clickFinishButton();
        
        List<String> claimSavedData = newClaimSaved.waitForNewClaimSaved(lossRouter);
        this.assignedUser = claimSavedData.get(1);
        this.claimNumber = claimSavedData.get(0);
    }

    private void clickIncidents() {

        repository.cc.claim.LossDetails details = new repository.cc.claim.LossDetails(this.driver);
        EditIncident edit = new EditIncident(this.driver);

        int numIncidents = details.findNumberOfIncidents();

        for (int i = 0; i < numIncidents; i++) {
            String incidentID = details.clickIncidentLinks(i);
            
            if (incidentID.contains("VehicleIncident")) {
                try {
                    repository.gw.enums.Incidents thisIncident = getIncidentEnum(incidentID);
                    
                    edit.editIncidents(thisIncident);
                } catch (Exception e) {
                    repository.gw.enums.Incidents thisIncident = getIncidentEnum("RecVehicleIncident");
                    
                    edit.editIncidents(thisIncident);
                }
            } else {
                repository.gw.enums.Incidents thisIncident = getIncidentEnum(incidentID);
                
                edit.editIncidents(thisIncident);
            }
        }
    }

    private repository.gw.enums.Incidents getIncidentEnum(String incidentID) {

        repository.gw.enums.Incidents incidentType = null;

        if (incidentID.contains("InjuryIncident")) {
            incidentType = repository.gw.enums.Incidents.editInjury;
        } else if (incidentID.contains("PropertyIncident")) {
            incidentType = repository.gw.enums.Incidents.editProperty;
        } else if (incidentID.contains("OtherIncident")) {
            incidentType = repository.gw.enums.Incidents.editOther;
        } else if (incidentID.contains("VehicleIncident") && !incidentID.contains("RecVehicleIncident")) {
            incidentType = repository.gw.enums.Incidents.editVehicle;
        } else if (incidentID.contains("RecVehicleIncident")) {
            incidentType = Incidents.editRecVehicle;
        }

        return incidentType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
