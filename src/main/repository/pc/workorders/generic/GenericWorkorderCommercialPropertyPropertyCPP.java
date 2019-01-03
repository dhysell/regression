package repository.pc.workorders.generic;

import com.idfbins.enums.OkCancel;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialPropertyProperty;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.gw.helpers.GuidewireHelpers;
import repository.pc.sidemenu.SideMenuPC;

import java.util.List;

public class GenericWorkorderCommercialPropertyPropertyCPP extends BasePage {

    private WebDriver driver;

    public GenericWorkorderCommercialPropertyPropertyCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    ////////////
    //PAGE TABS
    ////////////
    @FindBy(xpath = "//span[contains(@id, ':CoveragesCovTab-btnEl')]")
    private WebElement link_CoveragesTab;

    public void clickCoveragesTab() {
        clickWhenClickable(link_CoveragesTab);
        
    }

    public void clickCoveragesTab(CPPCommercialProperty_Building building) throws Exception {
        if (finds(By.xpath("//span[contains(@class, 'g-title') and (contains(text(), 'Location " + building.getPropertyLocation().getPropertyLocationNumber() + ": Property " + building.getNumber() + "') or contains(text(), 'New Property'))]")).isEmpty()) {
            repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
            sideMenu.clickSideMenuCPProperty();
            GenericWorkorderCommercialPropertyPropertyCPP cpProperty = new GenericWorkorderCommercialPropertyPropertyCPP(driver);
            cpProperty.editPropertyByNumber(building.getNumber());
        }
        clickWhenClickable(link_CoveragesTab);
        
    }

    @FindBy(xpath = "//span[contains(@id, ':BuildingDetailsTab-btnEl')]")
    private WebElement link_DetailsTab;

    public void clickDetailsTab() {
        clickWhenClickable(link_DetailsTab);
        
    }

    @FindBy(xpath = "//span[contains(@id, ':CPBuildingAddCoveragesCardTab-btnEl')]")
    private WebElement link_AdditionalCoveragesTab;

    public void clickAdditionalCoveragesTab() {
        clickWhenClickable(link_AdditionalCoveragesTab);
        
    }

    @FindBy(xpath = "//span[contains(@id, ':CPBuildingExclCondCardPropertyTab-btnEl')]")
    private WebElement link_ExclusionsConditionsTab;

    public void clickExclusionsConditionsTab() {
        clickWhenClickable(link_ExclusionsConditionsTab);
        
    }

    public void clickExclusionsConditionsTab(CPPCommercialProperty_Building building) throws Exception {
        if (finds(By.xpath("//span[contains(@class, 'g-title') and (contains(text(), 'Location " + building.getPropertyLocation().getPropertyLocationNumber() + ": Property " + building.getNumber() + "') or contains(text(), 'New Property'))]")).isEmpty()) {
            repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);
            sideMenu.clickSideMenuCPProperty();
            GenericWorkorderCommercialPropertyPropertyCPP cpProperty = new GenericWorkorderCommercialPropertyPropertyCPP(driver);
            cpProperty.editPropertyByNumber(building.getNumber());
        }
        clickWhenClickable(link_ExclusionsConditionsTab);
        
    }

    @FindBy(xpath = "//span[contains(@id, ':BuildingAddlInterestTab-btnEl')]")
    private WebElement link_AdditionalInterestTab;

    public void clickAdditioanlInterestTab() {
        clickWhenClickable(link_AdditionalInterestTab);
        
    }

    @FindBy(xpath = "//span[contains(@id, ':CPBuildingUWQuestionsCardTab-btnEl')]")
    private WebElement link_UnderwritingQuestionsTab;

    public void clickUnderwritingQuestionsTab() {
        clickWhenClickable(link_UnderwritingQuestionsTab);
        
    }


    @FindBy(xpath = "//span[contains(@id, ':AdditionalCoveragesDV_tb:Add-btnEl')]")
    private WebElement button_AddCoverages;

    public void clickAddCoverages() {
        clickWhenClickable(button_AddCoverages);
    }

    @FindBy(xpath = "//span[contains(@id, ':AddCoverageButton-btnEl')]")
    public WebElement button_AddSelectedCoverages;


    public void clickAddSelectedCoverages() {
        clickWhenClickable(button_AddSelectedCoverages);
    }

    @FindBy(xpath = "//div[contains(@id, ':CoveragePatternSearchResultsLV-body')]/div/table")
    public WebElement table_CoveragesResults;


    public void selectCoverageFromResults(String coverage) {
        List<WebElement> searchResults = table_CoveragesResults.findElements(By.xpath(".//tr/td/div[contains(text(), '" + coverage + "')]/parent::td/preceding-sibling::td[1]/div/img"));
        if (!searchResults.isEmpty()) {
            if (!searchResults.get(0).getAttribute("class").contains("-checked")) {
                searchResults.get(0).click();
            }
        }
        if (button_AddSelectedCoverages.isEnabled()) {
            button_AddSelectedCoverages.click();
        }
    }


    @FindBy(xpath = "//span[contains(@id, ':addLocationsTB-btnEl')]")
    private WebElement link_AddLocation;

    private void clickAddLocation() {
        clickWhenClickable(link_AddLocation);
    }

    @FindBy(xpath = "//div[contains(@id, 'addLocationsTB:addExistingLocations')]")
    public WebElement link_ExistingLocation;

    public void clickExistingLocation() {
        hoverOverAndClick(link_ExistingLocation);
    }

    public void clickExistingLocationAddress(String address) {
        clickWhenClickable(find(By.xpath("//span[contains(text(), '" + address + "')]/parent::a/parent::div")));
    }

    public void clickLocationRowByAddress(String address) {
        clickWhenClickable(find(By.xpath("//a[contains(text(), '" + address + "') and contains(@id, 'LocationName')]")));
    }

    @FindBy(xpath = "//div[contains(@id, ':CPPropertyPanelSet:CPLocationBuildingsLV')]")
    public WebElement table_PropertyList;


    public void editPropertyByNumber(int propertyNumber) throws GuidewireNavigationException {
        
        clickWhenClickable(find(By.xpath("//a[contains(@id, 'BuildingNumEdit') and text()='" + propertyNumber + "']")));
        new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, 'CPBuildingPopup:ttlBar')]", 5000, "UNABLE TO GET TO EDIT BUILDINGS PAGE!");
    }

    public void clickAddProperty() {
        clickAdd();
        if (!new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, 'CPBuildingPopup:ttlBar') and contains(text(), 'New Property')]", 5000)) {
            Assert.fail("Could not get to New Property popup after clicking Add property button.");
        }
    }

    public void clickCancel() {
        clickCancel();
        selectOKOrCancelFromPopup(OkCancel.OK);
    }

    public void clickOK() {
        clickOK();
        if (!new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':CPBuildingsScreen:ttlBar') and contains(text(), 'Property')]", 10)) {
            Assert.fail("Could not get to Main Property page after clicking OK.");
        }
    }


    /**
     * @param policy
     * @throws Exception
     * @author jlarsen
     * @Description - for every property create/fill out every building
     * @DATE - Jul 6, 2016
     */

    public void fillOutCommercialPropertyProperty(GeneratePolicy policy) throws Exception {
        for (CPPCommercialPropertyProperty property : policy.commercialPropertyCPP.getCommercialPropertyList()) {
            if (new GuidewireHelpers(driver).getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
                clickAddLocation();
                clickExistingLocationAddress(property.getAddress().getLine1() + ", " + property.getAddress().getCity() + ", " + property.getAddress().getState().getAbbreviation());
            } else {
                clickLocationRow(property.getAddress());
            }
            
            property.setPropertyLocationNumber(find(By.xpath("//div[contains(text(), '" + property.getAddress().getLine1() + ", " + property.getAddress().getCity() + ", " + property.getAddress().getState().getAbbreviation() + "')]/parent::td/parent::tr/child::td[3]/div")).getText());//temp fix till find a solution to link locations to properties.
            
            for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
                building.setPropertyLocation(property);
                if (building.isFillOutAllFields()) {
                    addPropertyQQ(policy.basicSearch, building);
                    converToFullApp(building);
                    clickOK();
                } else if (new GuidewireHelpers(driver).getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
                    addPropertyQQ(policy.basicSearch, building);
                } else {
                    converToFullApp(building);
                    clickOK();
                }
            }
        }
    }

    public void converToFullApp(CPPCommercialProperty_Building building) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Details propertyDetail = new repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Details(driver);
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_AdditionalCoverages propertyAdditionalCoverages = new repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_AdditionalCoverages(driver);
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_ExclusionsAndConditions propertyExclusionsConditions = new repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_ExclusionsAndConditions(driver);
        //Additional Interests are not coded in to CPP yet. When they are, uncomment this line and use it.
//		GenericWorkorderCommercialPropertyPropertyCPP_AdditionalInterest additionalInterest = new GenericWorkorderCommercialPropertyPropertyCPP_AdditionalInterest();
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_UnderwritingQuestions underwritingQuestions = new repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_UnderwritingQuestions(driver);


        editPropertyByNumber(building.getNumber());
        propertyDetail.fillOutPropertyDetailsFA(building);
        propertyAdditionalCoverages.fillOutPropertyAdditionalCoverages(building);
        propertyExclusionsConditions.fillOutPropertyExclusionsConditions(building);
        underwritingQuestions.fillOutPropertyUnderwriterQuestionsFA(building);
        underwritingQuestions.fillOutBasicClassCodeUWQuestionFA(building);
    }

    public void convertToFullApp(GeneratePolicy policy) throws Exception {
        new repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Coverages(driver);
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Details propertyDetail = new repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Details(driver);
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_AdditionalCoverages propertyAdditionalCoverages = new repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_AdditionalCoverages(driver);
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_ExclusionsAndConditions propertyExclusionsConditions = new repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_ExclusionsAndConditions(driver);
        new repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_UnderwritingQuestions(driver);

        for (CPPCommercialPropertyProperty property : policy.commercialPropertyCPP.getCommercialPropertyList()) {
            clickLocationRow(property.getAddress());
            
            property.setPropertyLocationNumber(find(By.xpath("//div[contains(text(), '" + property.getAddress().getLine1() + ", " + property.getAddress().getCity() + ", " + property.getAddress().getState().getAbbreviation() + "')]/parent::td/parent::tr/child::td[3]/div")).getText());//temp fix till find a solution to link locations to properties.
            
            for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
                building.setPropertyLocation(property);
                editPropertyByNumber(building.getNumber());
                propertyDetail.fillOutPropertyDetailsFA(building);
                propertyAdditionalCoverages.fillOutPropertyAdditionalCoverages(building);
                propertyExclusionsConditions.fillOutPropertyExclusionsConditions(building);
                //fill out additoanl interest
                //fill out underwriting questions

            }
        }
    }

    public void clickLocationRow(AddressInfo address) {
        clickWhenClickable(find(By.xpath("//div[contains(text(), '" + address.getDropdownAddressFormat() + "')]")));
    }


    @FindBy(xpath = "//span[contains(text(), 'Bldg. #')]/parent::div/parent::div/parent::div/div/div/span/div")
    public WebElement checkbox_SelectAll;


    public void removeAllProperties() {
        clickWhenClickable(checkbox_SelectAll);
        
        clickWhenClickable(find(By.xpath("//span[contains(@id, ':CPPropertyPanelSet:CPLocationBuildingsLV_tb:Remove-btnEl')]")));
        
        selectOKOrCancelFromPopup(OkCancel.OK);
        
    }


    public void addPropertyQQ(boolean basicSearch, CPPCommercialProperty_Building building) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Coverages propertyCoveragesPage = new repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Coverages(driver);
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Details propertyDetail = new repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Details(driver);
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_AdditionalCoverages propertyAdditionalCoverages = new repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_AdditionalCoverages(driver);
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_ExclusionsAndConditions propertyExclusionsConditions = new repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_ExclusionsAndConditions(driver);
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_UnderwritingQuestions propertyUnderwritingQuestions = new repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_UnderwritingQuestions(driver);

        clickAddProperty();
        
        propertyCoveragesPage.fillOutPropertyCoverages(building);
        propertyDetail.fillOutPropertyDetailsQQ(building);
        propertyAdditionalCoverages.fillOutPropertyAdditionalCoverages(building);
        propertyExclusionsConditions.fillOutPropertyExclusionsConditions(building);
        fillOutPropertyAdditionalInterest(basicSearch, building);
        propertyUnderwritingQuestions.fillOutPropertyUnderwriterQuestionsQQ(building);
        if (building.isGenerateUWIssuesFromQuestions()) {
            propertyUnderwritingQuestions.fillOutBasicClassCodeUWQuestionFA(building);
        } else {
            propertyUnderwritingQuestions.fillOutBasicClassCodeUWQuestionQQ(building);
        }

        clickOK();
    }


    public void addPropertyFA(boolean basicSearch, CPPCommercialProperty_Building building) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Coverages propertyCoveragesPage = new GenericWorkorderCommercialPropertyPropertyCPP_Coverages(driver);
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Details propertyDetail = new GenericWorkorderCommercialPropertyPropertyCPP_Details(driver);
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_AdditionalCoverages propertyAdditionalCoverages = new GenericWorkorderCommercialPropertyPropertyCPP_AdditionalCoverages(driver);
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_ExclusionsAndConditions propertyExclusionsConditions = new GenericWorkorderCommercialPropertyPropertyCPP_ExclusionsAndConditions(driver);
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_UnderwritingQuestions propertyUnderwritingQuestions = new GenericWorkorderCommercialPropertyPropertyCPP_UnderwritingQuestions(driver);

        clickAddProperty();
        
        propertyCoveragesPage.fillOutPropertyCoverages(building);
        propertyDetail.fillOutPropertyDetailsFA(building);
        propertyAdditionalCoverages.fillOutPropertyAdditionalCoverages(building);
        propertyExclusionsConditions.fillOutPropertyExclusionsConditions(building);
        fillOutPropertyAdditionalInterest(basicSearch, building);
        propertyUnderwritingQuestions.fillOutPropertyUnderwriterQuestions(building);
        propertyUnderwritingQuestions.fillOutBasicClassCodeUWQuestionFA(building);

        clickOK();
    }


    public void fillOutPropertyAdditionalInterest(boolean basicSearch, CPPCommercialProperty_Building building) throws Exception {
        clickAdditioanlInterestTab();
        
        for (AdditionalInterest additionalInterest : building.getAdditionalInterestList()) {
            repository.pc.workorders.generic.GenericWorkorderAdditionalInterests additinalInterests = new GenericWorkorderAdditionalInterests(driver);
            additinalInterests.fillOutAdditionalInterest(basicSearch, additionalInterest);

            //FIRST LENDER
            //IF BUILDING COVERAGE
            //IF BUSINESS PERSONAL PROPERTY COVERAGE
            //IF BUILDINGER RISK COVERAGE CP 00 20
            //IF PROPERTY IN THE OPEN


            switch (additionalInterest.getAdditionalInterestTypeCPP()) {
                case CP_AdditionalInsured_BuildingOwner_CP_12_19:
                    break;
                case CP_BuildingOwnerLossPayableClause_CP_12_18:
                    break;
                case CP_ContractOfSaleClause_CP_12_18:
                    break;
                case CP_LendersLossPayableClause_CP_12_18:
                case CP_LossPayableClause_CP_12_18:
                    //leased equipemtn radio
                    //equipment value
                    //desc of leased equiment
                    break;
                case CP_Mortgagee:
                    break;
                default:
                    break;
            }//end switch

        }


    }


}//END OF FILE

















