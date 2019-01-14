package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.cc.actionsmenu.ActionsMenu;
import repository.cc.claim.incidents.EditIncident;
import repository.cc.claim.incidents.InjuryIncident;
import repository.cc.claim.incidents.NewInjuryIncidents;
import repository.cc.claim.incidents.NewVehicleIncidents;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.ClaimHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.WaitUtils;

import java.util.ArrayList;
import java.util.List;

public class Exposures extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public Exposures(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public String createExposure(String incident, boolean isICDtest) {

        String exposureType = find(By.xpath("//span[@id='NewExposure:NewExposureScreen:ttlBar']")).getText();
        String coverageType = new String();

        switch (exposureType) {
            case "New Exposure - Vehicle Damage":
                coverageType = vehicleDamageExposure(incident);
                break;
            case "New Exposure - Bodily Injury":
                coverageType = bodilyInjuryExposure(incident, isICDtest);
                break;
            case "New Exposure - Med Pay":
                coverageType = medPayExposure(incident, isICDtest);
                break;
            case "New Exposure - Roadside Assistance":
                coverageType = roadsideAssistanceExposure(incident);
                break;
            case "New Exposure - Personal Property":
                coverageType = personalPropertyExposure(incident);
                break;
            case "New Exposure - Property":
                coverageType = propertyExposure(incident);
                break;
            case "New Exposure - Personal Injury":
                coverageType = personalInjuryExposure(incident);
                break;
            case "New Exposure - Advertising Injury":
                coverageType = advertisingInjuryExposure(incident);
                break;
            case "New Exposure - Loss of Use":
                coverageType = lossOfUseExposure(incident);
                break;
            case "New Exposure - Other":
                otherExposure(incident);
                break;
            default:
                throw new IllegalArgumentException("Missing exposure method for " + exposureType);
        }
        return coverageType;
    }


    public void buildExposure(String exposureType, boolean isICDtest) {

        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        repository.cc.actionsmenu.ActionsMenu actionsMenu = new ActionsMenu(this.driver);
        LossDetails lossDetails = new LossDetails(this.driver);

        sideMenu.clickLossDetailsLink();

        List<String> incidents = lossDetails.getIncidentList();
        List<String> fullStrings = lossDetails.getIncidentListV2();
        

        sideMenu.clickExposuresLink();
        

        for (String incident : fullStrings) {

            String menuIncident = actionsMenu.lessCrappyCoveragePicker(incident, incidents, exposureType);
            
            exposureType = createExposure(menuIncident, isICDtest);

            sideMenu.clickExposuresLink();
            if (validateExposureCreation(exposureType) == false) {
                int counter = 0;
                do {
                    
                    menuIncident = actionsMenu.lessCrappyCoveragePicker(incident, incidents, exposureType);
                    
                    exposureType = createExposure(menuIncident, isICDtest);
                } while (counter < 3 && validateExposureCreation(exposureType) == false);
            }
        }
    }

    public Boolean validateExposureCreation(String exposureType) {

        List<WebElement> tableCells = finds(By.cssSelector("div[id$='ExposuresLV-body'] tr td"));
        boolean found = false;

        for (WebElement cell : tableCells) {
            if (cell.getText().equalsIgnoreCase(exposureType) || cell.getText().contains(exposureType)) {
                found = true;
                break;
            }
        }

        return found;
        }

    @FindBy(xpath = "//a[contains(@id,':Update')]")
    private WebElement buttonUpdate;


    private void clickUpdateButton() {
        clickWhenClickable(buttonUpdate);
        waitUntilElementIsNotVisible(buttonUpdate, 30);
    }

    @FindBy(xpath = "//span[@id='Claim:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']")
    private WebElement elementInsuredName;


    private String getInsuredName() {
        String insuredName = elementInsuredName.getText();
        return insuredName;
    }

    @FindBy(xpath = "//span[@id='Claim:ClaimInfoBar:Losstype-btnInnerEl']/span[@class='infobar_elem_val']")
    private WebElement elementLossType;


    private String getLossType() {
        String lossType = elementLossType.getText();
        return lossType;
    }

    private Guidewire8Select selectCoverage() {
        return new Guidewire8Select(driver, "//table[contains(@id,'Exposure_Coverage-triggerWrap') or "
				        + "contains(@id,'FBExposureDetailInputSet:Coverage-triggerWrap')]");
    }

    private String getCoverage() {
        String coverage = selectCoverage().getText();
        clickProductLogo();
        return coverage;
    }

    private void setCoverage(String selection) {
        selectCoverage().selectByVisibleText(selection);
    }

    private void setCoverageRandom() {
        selectCoverage().selectByVisibleTextRandom();
    }

    private Guidewire8Select selectInjury() {
        return new Guidewire8Select(driver, "//table[contains(@id,'Injury_Incident-triggerWrap')]");
    }

    private String getInjury() {
        String coverage = selectInjury().getText();
        selectInjury().sendKeys(Keys.TAB);
        return coverage;
    }

    private void setInjury(String selection) {
        selectInjury().selectByVisibleText(selection);
    }


    private void setInjuryRandom() {
        selectInjury().selectByVisibleTextRandom();
    }

    private Guidewire8Select selectClaimant() {
        return new Guidewire8Select(driver, "//table[contains(@id,'Claimant_Picker-triggerWrap')]");
    }


    private String getClaimant() {
        waitUntilElementNotClickable(By.cssSelector("div[role='presentation'][class='x-mask x-mask-fixed']"));
        waitUntilElementIsVisible(By.xpath("//div[contains(@id,'PrimaryCoverage-inputEl')]"));
        
        String coverage = selectClaimant().getText();

        return coverage;
    }


    private void setClaimant(String selection) {
        try {
            waitUtils.waitUntilElementIsNotVisible(By.cssSelector("div[role='presentation'][class='x-mask x-mask-fixed']"));
            waitUtils.waitUntilElementIsClickable(By.xpath("//div[contains(@id,'PrimaryCoverage-inputEl')]"), 10);
            find(By.xpath("//div[contains(@id,'PrimaryCoverage-inputEl')]")).click();
            selectClaimant().selectByVisibleTextPartial(selection);
        } catch (Exception e) {
            
            selectClaimant().selectByVisibleTextPartial(selection);
        }
    }

    private String setClaimantRestricted(List<String> doNotPick) {

        List<String> options = selectClaimant().getList();

        for (String item : doNotPick) {
                options.remove(item);
            }

        String selection = options.get(NumberUtils.generateRandomNumberInt(0, options.size() - 1));
        selectClaimant().selectByVisibleTextPartial(selection);

        return selection;
    }


    private void setClaimantRandom() {
        selectClaimant().selectByVisibleTextRandom();
    }

    private Guidewire8Select selectClaimantType() {
        return new Guidewire8Select(driver, "//table[contains(@id,'Claimant_Type-triggerWrap')]");
    }


    private String getClaimantType() {
        String coverage = selectClaimantType().getText();
        
        return coverage;
    }


    private void setClaimantType(String selection) {
        selectClaimantType().selectByVisibleText(selection);
    }


    private void setClaimantTypeRandom() {
        selectClaimantType().selectByVisibleTextRandom();
        
        selectClaimantType().sendKeys(Keys.TAB);
        selectClaimantType().sendKeys(Keys.TAB);
    }

    private void setClaimantTypeRestricted(List<String> blockList) {
        List<String> options = selectClaimantType().getList();
        List<String> validOptions = new ArrayList<>();
        boolean match = false;

        for (String option : options) {
            match = false;
            for (String item : blockList) {
                if (option.trim().equalsIgnoreCase(item.trim())) {
                    match = true;
                }
            }
            if (!match) {
                validOptions.add(option);
            }
        }

        String selection = validOptions.get(NumberUtils.generateRandomNumberInt(0, validOptions.size() - 1));

        selectClaimantType().selectByVisibleText(selection);
    }

    private Guidewire8Select selectAlternatContact() {
        return new Guidewire8Select(driver, "//table[contains(@id,'AltContact_Name-triggerWrap')]");
    }


    private String getPropertyExposureAlternatContact() {
        String coverage = selectAlternatContact().getText();
        selectAlternatContact().sendKeys(Keys.TAB);
        return coverage;
    }


    private void setAlternatContact(String selection) {
        selectAlternatContact().selectByVisibleText(selection);
    }


    private void setAlternatContactRandom() {
        selectAlternatContact().selectByVisibleTextRandom();
    }

    private Guidewire8Select selectPropertyName() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Property_Incident-triggerWrap')]");
        // NewExposure:NewExposureScreen:NewExposureDV:NewClaimThirdPartyPropertyDamageDV
    }


    private String getPropertyName() {
        String coverage = selectPropertyName().getText();
        selectPropertyName().sendKeys(Keys.TAB);
        return coverage;
    }


    private void setPropertyName(String selection) {
        selectPropertyName().selectByVisibleText(selection);
    }


    private void setPropertyNameRandom() {
        selectPropertyName().selectByVisibleTextRandom();
        selectPropertyName().sendKeys(Keys.TAB);
    }

    private Guidewire8Select selectExpLossCause() {
        return new Guidewire8Select(driver, "//table[contains(@id,'LossCause-triggerWrap')]");
    }


    private String getExpLossCause() {
        String coverage = selectExpLossCause().getText();
        selectExpLossCause().sendKeys(Keys.TAB);
        return coverage;
    }


    private void setExpLossCause(String selection) {
        selectExpLossCause().selectByVisibleText(selection);
    }


    private void setExpLossCauseRandom() {
        selectExpLossCause().selectByVisibleTextRandom();
    }

    private Guidewire8Select selectIncidentOverviewVehicle() {
        return new Guidewire8Select(driver, "//table[contains(@id,'Vehicle_Incident-triggerWrap')]");
    }


    private String getIncidentOverviewVehicle() {
        String coverage = selectIncidentOverviewVehicle().getText();
        selectIncidentOverviewVehicle().sendKeys(Keys.TAB);
        return coverage;
    }


    private void setIncidentOverviewVehicle(String selection) {
        selectIncidentOverviewVehicle().selectByVisibleText(selection);
    }


    private void setIncidentOverviewVehicleRandom() {
        selectIncidentOverviewVehicle().selectByVisibleTextRandom();
    }


    private void setIncidentOverviewVehicleContains(String selection) {
        selectIncidentOverviewVehicle().selectByVisibleTextPartial(selection);
    }

    private Guidewire8Select selectIncidentOverviewDescription() {
        return new Guidewire8Select(driver, "//table[contains(@id,'Other_Incident-triggerWrap')]");
    }


    private String getIncidentOverviewDescription() {
        String coverage = selectIncidentOverviewDescription().getText();
        selectIncidentOverviewDescription().sendKeys(Keys.TAB);
        return coverage;
    }


    private void setIncidentOverviewDescription(String selection) {
        selectIncidentOverviewDescription().selectByVisibleText(selection);
    }


    private void setIncidentOverviewDescriptionRandom() {
        selectIncidentOverviewDescription().selectByVisibleTextRandom();
    }

    private Guidewire8Select selectResponsibilityStatus() {
        try {
            return new Guidewire8Select(driver, "//table[contains(@id,'Section111LiabilityStatusExt-triggerWrap')]");
        } catch (Exception e) {
            
            return new Guidewire8Select(driver, "//table[contains(@id,'Section111LiabilityStatusExt-triggerWrap')]");
        }
    }


    private String getResponsibilityStatus() {
        String coverage = selectResponsibilityStatus().getText();
        selectResponsibilityStatus().sendKeys(Keys.TAB);
        return coverage;
    }


    private void setResponsibilityStatus(String selection) {
        selectResponsibilityStatus().selectByVisibleText(selection);
    }


    private void setResponsibilityStatusRandom() {
        selectResponsibilityStatus().selectByVisibleTextRandom();
    }

    private Guidewire8Select selectLossParty() {
        return new Guidewire8Select(driver, "//table[contains(@id,'fbLossParty-triggerWrap')]");
    }


    private String getLossPartySelect() {
        String coverage = selectLossParty().getText();
        selectLossParty().sendKeys(Keys.TAB);
        return coverage;
    }


    private void setLossParty(String selection) {
        selectLossParty().selectByVisibleTextPartial(selection);
    }


    private void setLossPartyRandom() {
        selectLossParty().selectByVisibleTextRandom();
    }

    @FindBy(xpath = "//div[contains(@id,'Exposure_LossParty-inputEl') "
            + "or contains(@id,'fbLossParty-inputEl')"
            + "or contains(@id,'LossParty-inputEl')]")
    private WebElement elementPropertyExposureLossParty;


    private String getLossParty() {
        String lossParty = elementPropertyExposureLossParty.getText();
        return lossParty;
    }

    @FindBy(xpath = "//input[contains(@id,'ResponsibilityAcceptedDate-inputEl')]")
    private WebElement inputResponsibilityAcceptedDate;


    private void setResponsibilityAcceptedDate(String inputString) {
        inputResponsibilityAcceptedDate.sendKeys(inputString);
        inputResponsibilityAcceptedDate.sendKeys(Keys.TAB);
    }

    private String getResponsibilityAcceptedDate() {
        return inputResponsibilityAcceptedDate.getAttribute("value");
    }

    @FindBy(xpath = "//a[contains(@id,'Claimant_PickerMenuIcon')]")
    private WebElement linkClaimantPicker;


    private void clickClaimantPicker() {
        linkClaimantPicker.click();
    }

    @FindBy(xpath = "//a[contains(@id,'Injury_IncidentMenuIcon')]")
    private WebElement linkInjuryPicker;


    private void clickInjuryPicker() {
        linkInjuryPicker.click();
    }

    @FindBy(xpath = "//a[contains(@id,'Other_IncidentMenuIcon')]")
    private WebElement linkIncidentPicker;


    private void clickIncidentPicker() {
        linkIncidentPicker.click();
    }

    @FindBy(xpath = "//a[contains(@id,'AltContact_NameMenuIcon')]")
    private WebElement linkAlternateContactPicker;


    private void clickAlternatContactPicker() {
        linkAlternateContactPicker.click();
    }

    @FindBy(xpath = "//a[contains(@id,'Property_IncidentMenuIcon')]")
    private WebElement linkPropertyNamePicker;


    private repository.cc.claim.Incidents clickPropertyNamePicker() {
        Actions builder = new Actions(this.driver);

        try {
            builder.moveToElement(linkPropertyNamePicker)
                    .click(linkPropertyNamePicker)
                    .moveToElement(linkPropertyNamePicker, 1, 1);
            Action runMe = builder.build();
            runMe.perform();
        } catch (Exception e) {
            System.out.println("Selection has been clicked.");
        }

        clickWhenClickable(linkPropertyNamePicker);

        return clickEditIncidentDetails();
    }


    private NewInjuryIncidents propertyNamePickerNewIncident() {
        Actions builder = new Actions(driver);

        builder.moveToElement(linkPropertyNamePicker)
                .click(linkPropertyNamePicker)
                .moveToElement(linkPropertyNamePicker, 1, 1);
        Action runMe = builder.build();
        runMe.perform();

        clickWhenClickable(linkPropertyNamePicker);

        return clickNewIncident();
    }

    @FindBy(xpath = "//span[contains(text(),'New Incident')]")
    private WebElement linkNewIncident;


    private NewInjuryIncidents clickNewIncident() {
        clickWhenClickable(linkNewIncident);
        

        return new NewInjuryIncidents(this.driver);
    }

    @FindBy(xpath = "//span[contains(text(),'New Incident')]")
    private WebElement linkNewIncidentDescription;


    private void clickNewIncidentDescription() {
        clickWhenClickable(linkNewIncidentDescription);
    }

    @FindBy(css = "a[id*='EditIncidentMenuItem']")
    private WebElement linkEditIncident;

    private repository.cc.claim.Incidents clickEditIncidentDetails() {
        clickWhenClickable(linkEditIncident);
        return new repository.cc.claim.Incidents(this.driver);
    }

    @FindBy(css = "a[id*='NewIncidentMenuItem']")
    private WebElement linkNewPropertyIncident;

    /*private Incidents clickNewPropertyIncident() {
        clickWhenClickable(linkNewPropertyIncident);
        return new Incidents(this.driver);
    }*/

    // EXPOSURES ============================================================================================================================================================================================================


    private void otherExposure(String incident) {
        // TODO Auto-generated method stub

    }


    private String advertisingInjuryExposure(String incident) {

        if (!getLossParty().equalsIgnoreCase("Insured's loss")) {
            while (getCoverage().equalsIgnoreCase("<none>")) {
                setCoverageRandom();
            }

            while (getClaimant().equalsIgnoreCase("<none>") || getClaimant().equalsIgnoreCase(getInsuredName())) {
                setClaimantRandom();
            }

            while (getClaimantType().equalsIgnoreCase("<none>") || getClaimantType().equalsIgnoreCase("Insured")) {
                setClaimantTypeRandom();
            }
        } else {
            setClaimant(getInsuredName());
            if (getClaimantType().equalsIgnoreCase("<none>")) {
                setClaimantTypeRandom();
            }
        }

        if (getIncidentOverviewDescription().equalsIgnoreCase("<none>")) {
            repository.cc.claim.Incidents incidents = new repository.cc.claim.Incidents(this.driver);

            clickIncidentPicker();
            clickNewIncident();
            incidents.createOtherIncident();
        }

        String coverageType = getCoverageType();

        if (getClaimant().equalsIgnoreCase("<none>")) {
            
            setClaimant(getInsuredName());
        }

        
        clickUpdateButton();

        return coverageType;
    }

    private String lossOfUseExposure(String incident) {

        String insuredName = getInsuredName();
        String lossParty = getLossParty();

        if (incident.isEmpty() || incident.equalsIgnoreCase("")) {
            incident = getCoverage();
        }

        if (lossParty.equalsIgnoreCase("Insured's loss")) {
            setClaimant(insuredName);
            setClaimantType("Insured");
        } else {
            setClaimantRandom();
            setClaimantTypeRandom();
        }

        setPropertyName(incident);

        String coverageType = getCoverageType();

        if (getClaimant().equalsIgnoreCase("<none>")) {
            
            setClaimant(getInsuredName());
        }

        
        clickUpdateButton();

        return coverageType;

    }


    private String personalInjuryExposure(String incident) {
        
        if (getLossParty().equalsIgnoreCase("Insured's loss")) {
            setClaimant(getInsuredName());
        } else {
            setClaimantRandom();
            setClaimantType("Self");
            if (getClaimant().equalsIgnoreCase(getInsuredName())) {
                do {
                    setClaimantRandom();
                } while (getClaimant().equalsIgnoreCase(getInsuredName()));
                setClaimantTypeRandom();
            }
        }

        

        if (getIncidentOverviewDescription().equalsIgnoreCase("<none>")) {
            repository.cc.claim.Incidents incidents = new repository.cc.claim.Incidents(this.driver);

            clickIncidentPicker();
            clickNewIncident();
            incidents.createOtherIncident();
        }

        String coverageType = getCoverageType();

        if (getClaimant().equalsIgnoreCase("<none>")) {
            
            setClaimant(getInsuredName());
        }

        
        clickUpdateButton();

        return coverageType;

    }


    private String propertyExposure(String incident) {

        String primaryCoverage = "";

        primaryCoverage = find(By.xpath("//div[contains(@id,':Exposure_PrimaryCoverage-inputEl')]")).getText();

        if (primaryCoverage.contains("Equipment Breakdown")) {
            propertyExposureEquipmentBreakdown(incident);

        } else {
            
            String insuredName = getInsuredName();
            
            String lossParty = getLossParty();

            if (getLossType().equalsIgnoreCase("General Liability")) {
                setExpLossCauseRandom();

                
                if (!getLossParty().equalsIgnoreCase("Insured's loss")) {
                    while (getClaimant().equalsIgnoreCase("<none>") || getClaimant().equalsIgnoreCase(getInsuredName())) {
                        setClaimantRandom();
                    }

                    while (getClaimantType().equalsIgnoreCase("<none>") || getClaimantType().equalsIgnoreCase("Insured")) {
                        setClaimantTypeRandom();
                    }
                } else {
                    setClaimant(insuredName);
                    setClaimantType("Insured");
                }
            }

            if (getLossType().equalsIgnoreCase("Auto")) {

                setClaimant(insuredName);
                setClaimantType("Insured");

                if (getPropertyName().equalsIgnoreCase("<none>")) {
                    propertyNamePickerNewIncident();

                    repository.cc.claim.Incidents incidents = new Incidents(this.driver);
                    incidents.createPropertyIncident();
                }

            } else {
                if (incident.isEmpty() || incident.equalsIgnoreCase("")) {
                    incident = getCoverage();
                }
                setPropertyNameRandom();
            }

            if (lossParty.equalsIgnoreCase("Insured's loss")) {
                setClaimant(insuredName);
                setClaimantType("Insured");
            }
        }

        String coverageType = getCoverageType();

        if (getClaimant().equalsIgnoreCase("<none>")) {
            
            setClaimant(getInsuredName());
        }

        
        clickUpdateButton();

        return coverageType;
    }


    private void propertyExposureEquipmentBreakdown(String incident) {

        String insuredName = getInsuredName();

        setClaimant(insuredName);
        setClaimantType("Insured");

        setPropertyNameRandom();

        clickPropertyNamePicker();
        clickEditIncidentDetails();

        EditIncident editIncident = new EditIncident(this.driver);
        editIncident.editIncidents(repository.gw.enums.Incidents.editPropertyEquipmentBreakDown);
    }


    private String personalPropertyExposure(String incident) {

        String insuredName = getInsuredName();
        
        String lossParty = getLossParty();

        if (incident.isEmpty() || incident.equalsIgnoreCase("")) {
            incident = getCoverage();
        }

        if (lossParty.equalsIgnoreCase("Insured's loss")) {
            setClaimant(insuredName);
            setClaimantType("Insured");
        } else {
            setClaimantRandom();
            setClaimantTypeRandom();
        }

        setPropertyName(incident);

        String coverageType = getCoverageType();

        if (coverageType.equals("Jewelry") || coverageType.equals("Textiles") || coverageType.equals("Credit Cards")) {
            clickPropertyNamePicker().addImpliedCoverageDetails(coverageType);
        }

        waitUtils.waitUntilElementIsNotVisible(By.xpath("//span[text()='OK']"), 20);

        if (getClaimant().equalsIgnoreCase("<none>")) {
            
            setClaimant(insuredName);
        }

        
        clickUpdateButton();

        return coverageType;

    }


    private String roadsideAssistanceExposure(String incident) {

        setClaimant(getInsuredName());

        

        if (!getClaimantType().equalsIgnoreCase("Insured")) {
            setClaimantTypeRandom();
        }

        setIncidentOverviewVehicle(getCoverage());

        String coverageType = getCoverageType();

        if (getClaimant().equalsIgnoreCase("<none>")) {
            
            setClaimant(getInsuredName());
        }

        
        clickUpdateButton();

        return coverageType;

    }

    private void setInjuryByFarmProductsProduced(Boolean yesNo) {
        if (yesNo) {
            waitUtils.waitUntilElementIsClickable(injuryCausedByFarmProductsYes);
            injuryCausedByFarmProductsYes.click();
        } else {
            waitUtils.waitUntilElementIsClickable(injuryCausedByFarmProductsNo);
            injuryCausedByFarmProductsNo.click();
        }
    }

    @FindBy(css = "input[id*='FarmProductsProducedOnLoc_true-inputEl']")
    private WebElement injuryCausedByFarmProductsYes;

    @FindBy(css = "input[id*='FarmProductsProducedOnLoc_false-inputEl']")
    private WebElement injuryCausedByFarmProductsNo;

    private String medPayExposure(String incident, Boolean isICDtest) {

        clickInjuryPicker();
        
        InjuryIncident injuryIncident = clickNewIncident().newInjury(isICDtest);
        List<String> nonClaimants = new ArrayList<>();

        if (getLossType().equalsIgnoreCase("General Liability")) {
            setExpLossCause("General Liability (including medical)");

            setClaimant(getInsuredName());
            setClaimantType("Insured");
            
            if (getLossParty().equalsIgnoreCase("Third-party liability")) {

                // Do not allow claimant to be set is injured if severity is death.
                if (injuryIncident.getSeverity().equalsIgnoreCase("death")) {
                    nonClaimants.add(injuryIncident.getInjuredPerson());
                    setClaimantRestricted(nonClaimants);
                } else {
                    setClaimant(injuryIncident.getInjuredPerson());
                }

                List<String> doNotSelectClaimantType = new ArrayList<>();
                doNotSelectClaimantType.add("<none>");
                doNotSelectClaimantType.add("Insured");
                ClaimHelpers.setDynamicSelectBoxRestricted(selectClaimantType(), doNotSelectClaimantType);
            }

        } else if (getLossType().equalsIgnoreCase("Auto")) {
            waitUntilElementIsVisible(By.cssSelector("input[id$='fbLossParty-inputEl']"));
            setLossParty("Insured");
            setClaimant(getInsuredName());
            setClaimantType("Insured");
        }

        

        String coverageType = getCoverageType();

        if (checkIfElementExists(injuryCausedByFarmProductsYes, 5000)) {
            setInjuryByFarmProductsProduced(true);
        }

        
        clickUpdateButton();

        return coverageType;
    }


    private String bodilyInjuryExposure(String incident, boolean isICDtest) {

        List<String> nonClaimants = new ArrayList<>();

        clickInjuryPicker();
        waitUtils.waitUntilElementIsClickable(By.xpath("//span[text()='New Incident...']"));
        InjuryIncident injuryIncident = clickNewIncident().newInjury(isICDtest);
        
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        
        setResponsibilityStatus("Responsibility Accepted");

        validateResponsibilityAcceptedDate();

        // Do not allow claimant to be set is injured if severity is death.
        if (injuryIncident.getSeverity().equalsIgnoreCase("death")) {
            nonClaimants.add(injuryIncident.getInjuredPerson());
            setClaimantRestricted(nonClaimants);
        } else {
            setClaimant(injuryIncident.getInjuredPerson());
        }

        // Set Claimant Type
        List<String> doNotSelectListType = new ArrayList<>();
        doNotSelectListType.add("<none>");
        doNotSelectListType.add("Insured");

        setClaimantTypeRestricted(doNotSelectListType);

        // Focus Page
        clickInsuredName();

        String coverageType = getCoverageType();

        if (getLossType().equalsIgnoreCase("General Liability")) {
            setExpLossCauseRandom();
        }

        if (checkIfElementExists(injuryCausedByFarmProductsYes, 5000)) {
            setInjuryByFarmProductsProduced(true);
        }

        
        clickUpdateButton();

        return coverageType;
    }

    private void clickInsuredName() {
        waitUtils.waitUntilElementIsVisible(elementInsuredName);
        elementInsuredName.click();
    }

    /**
     * @author iclouser
     * @Requirement - US8840 - Accepted Resonsibility
     * @Link - <a href="https://rally1.rallydev.com/#/10552165958d/detail/userstory/60793597300"Rally Story</a>
     * @Description - Make sure responsibility accepted auto populates with the dol of the claim.
     * @DATE - Aug 29, 2016
     */
    private void validateResponsibilityAcceptedDate() {
        TopMenu topMenu = new TopMenu(this.driver);
        String dol = topMenu.gatherDOLString();
        
        String acceptedDate = getResponsibilityAcceptedDate();
        
        Assert.assertTrue(dol.equals(acceptedDate), "Expected the date to be: " + dol + " but it was: " + acceptedDate);
        
    }

    @FindBy(xpath = "//a[contains(@id,':Vehicle_Incident:Vehicle_IncidentMenuIcon')]")
    WebElement pickerVehicle;

    private void clickVehiclePicker() {
        clickWhenClickable(pickerVehicle);
    }

    @FindBy(xpath = "//a[contains(@id,':Vehicle_Incident:NewClaimVehicleDamageDV_NewIncidentMenuItem-itemEl')]")
    WebElement linkVehiclePickerNewVehicle;

    private void clickVehiclePickerNewVehicle() {
        clickWhenClickable(linkVehiclePickerNewVehicle);
        waitUtils.waitUntilElementIsClickable(By.cssSelector("span[id*=':NewVehicleIncidentScreen:']"));
    }


    private String vehicleDamageExposure(String incident) {
        
        waitUntilElementIsVisible(By.cssSelector("input[id$=':Claimant_Picker-inputEl']"));

        if (getLossType().equalsIgnoreCase("Inland Marine")) {
            vehicleDamageInlandMarine();
        } else {
            
            if (getLossParty().equalsIgnoreCase("Third-party liability")) {

                clickVehiclePicker();
                waitUtils.waitUntilElementIsClickable(By.xpath("//span[text()='New Vehicle Incident...']"));
                clickVehiclePickerNewVehicle();

                NewVehicleIncidents incidents = new NewVehicleIncidents(this.driver);

                incidents.thirdPartyVehicle();
                
                setClaimant("Anthony Ball 1974");
                
                setClaimantType("Driver of other vehicle");
            } else {
                setIncidentOverviewVehicle(getCoverage());
            }
        }

        String coverageType = getCoverageType();


        if (getClaimant().equalsIgnoreCase("<none>")) {
            
            setClaimant(getInsuredName());
        }

        
        clickUpdateButton();

        return coverageType;
    }

    private String getCoverageType() {
        waitUntilElementIsVisible(By.xpath("//div[contains(@id,'PrimaryCoverage-inputEl')]"));
        String coverageType = find(By.xpath("//div[contains(@id,'PrimaryCoverage-inputEl')]")).getText();
        
        return coverageType;
    }

    private void vehicleDamageInlandMarine() {
        String currentCoverage = getCoverage();
        List<String> options = selectIncidentOverviewVehicle().getList();
        String selection = "";

        for (String option : options) {
            
            if (currentCoverage.contains(option)) {
                selection = option;
                break;
            }
        }
        
        if (!selection.equalsIgnoreCase("")) {
            setIncidentOverviewVehicleContains(selection);
        } else {
            setIncidentOverviewVehicleRandom();
        }
    }


    public int getNumberOfExposures() {

        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        sideMenu.clickExposuresLink();
        

        List<WebElement> exposureLinks = finds(By.xpath("//a[contains(@id,':CoverageItem')]"));
        

        return exposureLinks.size();

    }

}
