package repository.gw.errorhandling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderBuildingsClassCodes;

import java.util.List;

public class ErrorHandlingBuildings extends ErrorHandlingHelpers {
    private WebDriver driver;

    public ErrorHandlingBuildings(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    int i = 0;
    int j = 0;


    public void errorHandlingBuildingsPage(int timeToWait, PolicyLocationBuilding building) throws GuidewireException {

        GenericWorkorderBuildings buildingsPage = new GenericWorkorderBuildings(driver);
        if (finds(By.xpath("//span[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:Update-btnEl')]")).size() > 0) {
            buildingsPage.clickOK();
        } else {
            saveDraft(timeToWait);
        }
        ErrorHandling errorBannerMessagesList = new ErrorHandling(driver);
        if (errorExists()) {
            this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
            for (this.i = 0; i < errorMessage.size(); i++) {
                errorMessage = errorHandlingBuildingsPageSwitchCase(timeToWait, building);
            }
            buildingsPage.clickOK();
        }

        if (finds(By.xpath("//span[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:Update-btnEl')]")).size() > 0) {
            buildingsPage.clickOK();
        } else {
            saveDraft(10);
        }

        if (errorExistsValidationResults()) {
            this.errorMessageValidationResults = errorBannerMessagesList.text_ErrorHandlingValidationResults();
            for (this.i = 0; i < errorMessageValidationResults.size(); i++) {
                errorMessageValidationResults = errorHandlingValidationResultsSwitchCase(timeToWait, building);
            }
            try {
                buildingsPage.clickOK();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        checkForRemainingErrors("Buildings");
        resetClassFields();
    }

    /*
     * ATTENTION: the following method is a copy of the method above, but is
     * only used in transition renewals, where some prior and post steps are
     * required to make the method work correctly.
     */

    public void errorHandlingRenewalBuildingsPage(int timeToWait, PolicyLocationBuilding building)
            throws GuidewireException {

        saveDraft(timeToWait);

        if (errorExists()) {
            ErrorHandling errorBannerMessagesList = new ErrorHandling(driver);
            this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
            for (this.i = 0; i < errorMessage.size(); i++) {
                errorMessage = errorHandlingBuildingsPageSwitchCase(timeToWait, building);
            }
            saveDraft(timeToWait);
        }
        checkForRemainingErrors("Buildings");
        resetClassFields();
    }

    public List<WebElement> errorHandlingBuildingsPageSwitchCase(int timeToWait, PolicyLocationBuilding building)
            throws GuidewireException {
        GenericWorkorderBuildings buildingsPage = new GenericWorkorderBuildings(driver);
        ErrorHandling errorBannerMessagesList = new ErrorHandling(driver);
        String errorString = this.errorMessage.get(this.i).getText();

        switch (errorString) {
            case "On current page:":
                GenericWorkorder workorder = new GenericWorkorder(driver);
                workorder.clickGenericWorkorderSaveDraft();
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                int tabErrorCount = 0;
                for (this.j = 0; j == errorMessage.size(); j++) {
                    String tabErrorString = errorMessage.get(j).getText();
                    if ((tabErrorString.equalsIgnoreCase("On \"Location Details\":"))
                            || (tabErrorString.equalsIgnoreCase("On current page:"))) {
                        tabErrorCount++;
                    }
                }
                if (tabErrorCount == 0) {
                    i = -1;
                }
                break;
            case "On \"Details\":":
                ErrorHandling errorDetails = new ErrorHandling(driver);
                clickWhenClickable(errorDetails.link_ErrorHandlingOnDetails());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                i = -1;
                break;
            case "Usage Description : Missing required field \"Usage Description\"":
                buildingsPage.setDescription(building.getUsageDescription());
                break;
            case "Interest : Missing required field \"Interest\"":
                buildingsPage.setInterestType(building.getOccupancyNamedInsuredInterest());
                break;
            case "Building Class Code : Missing required field \"Building Class Code\"":
                buildingsPage.setBuildingClassCode(building.getClassCode());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Building Limit : Missing required field \"Building Limit\"":
                buildingsPage.setBuildingLimit(building.getBuildingLimit());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Business Personal Property Limit : Missing required field \"Business Personal Property Limit\"":
                buildingsPage.setBuildingPersonalPropertyLimit(building.getBppLimit());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Year Built : Missing required field \"Year Built\"":
                buildingsPage.setYearBuilt(building.getYearBuilt());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Year Built : must be a four-digit year between 1000 and 2999":
                buildingsPage.setYearBuilt(building.getYearBuilt());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "# of Stories : Missing required field \"# of Stories\"":
                buildingsPage.setNumOfStories(building.getNumStories());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "# of Basements : Missing required field \"# of Basements\"":
                buildingsPage.setNumOfBasements(building.getNumBasements());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Total Area : Missing required field \"Total Area\"":
                buildingsPage.setTotalArea(building.getTotalArea());
                break;
            case "Year of Last Major Wiring Update : Missing required field \"Year of Last Major Wiring Update\"":
                buildingsPage.setLastUpdateWiring(building.getYearBuilt());
                break;
            case "Year of Last Major Wiring Update : must be a four-digit year between 1000 and 2999":
                buildingsPage.setLastUpdateWiring(building.getYearBuilt());
                break;
            case "Wiring Update Description : Missing required field \"Wiring Update Description\"":
                buildingsPage.setWiringUpdateDescription("Wiring Update");
                break;
            case "Year of Last Major Heating Update : Missing required field \"Year of Last Major Heating Update\"":
                buildingsPage.setLastUpdateHeating(building.getYearBuilt());
                break;
            case "Year of Last Major Heating Update : must be a four-digit year between 1000 and 2999":
                buildingsPage.setLastUpdateHeating(building.getYearBuilt());
                break;
            case "Heating Update Description : Missing required field \"Heating Update Description\"":
                buildingsPage.setHeatingUpdateDescription("Heating Update");
                break;
            case "Year of Last Major Plumbing Update : Missing required field \"Year of Last Major Plumbing Update\"":
                buildingsPage.setLastUpdatePlumbing(building.getYearBuilt());
                break;
            case "Year of Last Major Plumbing Update : must be a four-digit year between 1000 and 2999":
                buildingsPage.setLastUpdatePlumbing(building.getYearBuilt());
                break;
            case "Plumbing Update Description : Missing required field \"Plumbing Update Description\"":
                buildingsPage.setPlumbingUpdateDescription("Plumbing Update");
                break;
            case "Is the named insured the owner of the building? : Missing required field \"Is the named insured the owner of the building?\"":
                buildingsPage.setInsuredOwner(building.isNamedInsuredOwner());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Exterior House Keeping and Maintenance : Missing required field \"Exterior House Keeping and Maintenance\"":
                buildingsPage.setExteriorHousekeeping(building.getExteriorHouseKeepingAndMaintenance());
                break;
            case "Interior House Keeping and Maintenance : Missing required field \"Interior House Keeping and Maintenance\"":
                buildingsPage.setInteriorHousekeeping(building.getInteriorHouseKeepingAndMaintenance());
                break;
            case "Exits Properly Marked : Missing required field \"Exits Properly Marked\"":
                buildingsPage.setExitsMarked(building.isExitsProperlyMarked());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Number of Fire Extinguishers : Missing required field \"Number of Fire Extinguishers\"":
                buildingsPage.setNumberOfFireExtinguishers(building.getNumFireExtinguishers());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Exposure to Flammables, Chemicals, etc? : Missing required field \"Exposure to Flammables, Chemicals, etc?\"":
                buildingsPage.setExposureToFlammables(building.isExposureToFlammablesChemicals());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Construction Type : Missing required field \"Construction Type\"":
                buildingsPage.selectConstructionType(building.getConstructionType());
                break;
            case "Sprinklered : Missing required field \"Sprinklered\"":
                buildingsPage.setSprinklered(building.isSprinklered());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Roofing Type : Missing required field \"Roofing Type\"":
                buildingsPage.setRoofingType(building.getRoofingType());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Flat Roof : Missing required field \"Flat Roof\"":
                buildingsPage.setFlatRoof(building.isFlatRoof());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Roof Condition : Missing required field \"Roof Condition\"":
                buildingsPage.setRoofCondition(building.getRoofCondition());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Year Roof was Replaced : Missing required field \"Year Roof was Replaced\"":
                buildingsPage.setLastUpdateRoofing(building.getYearRoofReplaced());
                break;
            case "Wiring Type : Missing required field \"Wiring Type\"":
                buildingsPage.setWiringType(building.getWiringType());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Box Type : Missing required field \"Box Type\"":
                buildingsPage.setBoxType(building.getBoxType());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Does The Building Have Any Existing Damage? : Missing required field \"Does The Building Have Any Existing Damage?\"":
                buildingsPage.setExistingDamage(building.isExistingDamage());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Insured Property within 100 ft.? : Missing required field \"Insured Property within 100 ft.?\"":
                buildingsPage.setInsuredPropertyWithin100Feet(building.isInsuredPropertyWithin100Ft());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Does the Property have a Wood Burning Stove? : Missing required field \"Does the Property have a Wood Burning Stove?\"":
                new Guidewire8RadioButton(getDriver(), "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:WoodBurnStove')]").select(false);
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "Does the named insured operate the business? : Missing required field \"Does the named insured operate the business?\"":
                new Guidewire8RadioButton(getDriver(), "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:InsOperBuilding')]").select(false);
                break;
            case "Premium Basis Amount : Missing required field \"Premium Basis Amount\"":
                GenericWorkorderBuildingsClassCodes theBuilding = new GenericWorkorderBuildingsClassCodes(driver);
                theBuilding.setPremiumBasisAmount("25000");
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            default:
                checkForRemainingErrors("Buildings");
                break;

            // case "Risk Description : Missing required field \"Risk
            // Description\"":
            // Do Logic Necessary
            // break
            // This needs to be completed for transition renewals. Part of the
            // Spoilage coverage on the additional coverages tab.
            //
        }

        return errorMessage;
    }

}
