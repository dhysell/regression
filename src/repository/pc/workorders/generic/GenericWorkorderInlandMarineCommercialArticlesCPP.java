package repository.pc.workorders.generic;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.InlandMarineCPP.*;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPInlandMarineCommercialArticles;
import repository.gw.generate.custom.CPPInlandMarineCommercialArticles_ScheduledItem;
import repository.gw.helpers.GuidewireHelpers;

import java.util.List;


public class GenericWorkorderInlandMarineCommercialArticlesCPP extends BasePage {
    private WebDriver driver;

    public GenericWorkorderInlandMarineCommercialArticlesCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    ///////////////////////
    //       Tabs        //
    ///////////////////////

    @FindBy(xpath = "//span[contains(@id , ':IMComArticlesPartPanelSet:coveragesTab-btnInnerEl')]")
    private WebElement link_CoveragesTab;

    private void clickCoveragesTab() {
        clickWhenClickable(link_CoveragesTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':IMComArticlesPartPanelSet:scheduledItemsTab-btnInnerEl')]")
    private WebElement link_ScheduledItemsTab;

    private void clickScheduledItemsTab() {
        clickWhenClickable(link_ScheduledItemsTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':IMComArticlesPartPanelSet:exlusionsAndConditionsTab-btnInnerEl')]")
    private WebElement link_ExclusionsAndConditionsTab;

    private void clickExclusionsAndConditionsTab() {
        clickWhenClickable(link_ExclusionsAndConditionsTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':IMComArticlesPartPanelSet:addtInterestTab-btnInnerEl')]")
    private WebElement link_AdditionalInterestTab;

    private void clickAdditionalInterestTab() {
        clickWhenClickable(link_AdditionalInterestTab);
        
    }

    @FindBy(xpath = "//span[contains(@id , ':IMComArticlesPartPanelSet:uwQuestionsTab-btnInnerEl')]")
    private WebElement link_UnderwritingQuestionsTab;

    private repository.pc.workorders.generic.GenericWorkorderInlandMarineCPP_UnderwritingQuestions clickUnderwritingQuestionsTab() {
        clickWhenClickable(link_UnderwritingQuestionsTab);
        
        return new GenericWorkorderInlandMarineCPP_UnderwritingQuestions(getDriver());
    }

    ///////////////////////
    //     Coverages     //
    ///////////////////////

    private Guidewire8Select select_Deductible() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':OptionTermInput-triggerWrap')]");
    }

    private void setDeductible(CommercialArticlesCoverageForm_CM_00_20_Deductible deductible) {
        Guidewire8Select mySelect = select_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
        
    }

    // Cameras Blanket

    private Guidewire8Checkbox checkbox_CamerasBlanket() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Cameras Blanket')]/preceding-sibling::table");
    }

    private boolean checkCamerasBlanket(Boolean checked) {
        checkbox_CamerasBlanket().select(checked);
        
        return checked;
    }

    private Guidewire8Select select_MotionPicture() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':TypekeyTermInput-triggerWrap')]");
    }

    private void setMotionPicture(CamerasBlanket_MotionPicture motionPicture) {
        Guidewire8Select mySelect = select_MotionPicture();
        mySelect.selectByVisibleText(motionPicture.getValue());
        
    }

    @FindBy(xpath = "//input[contains(@id,':1:CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl')]")
    private WebElement input_CameraLimitPerAnyOneOccurrence;

    private void setCameraLimitPerAnyOneOccurrence(String value) {
        input_CameraLimitPerAnyOneOccurrence.click();
        input_CameraLimitPerAnyOneOccurrence.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    // Musical Instruments Blanket

    @FindBy(xpath = "//div[contains(text(), 'Musical Instruments Blanket')]/preceding-sibling::table//input")
    private WebElement checkbox_MusicalInstrumentsBlanket;

    private Guidewire8Checkbox checkbox_MusicalInstrumentsBlanket() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Musical Instruments Blanket')]/preceding-sibling::table");
    }

    private boolean checkMusicalInstrumentsBlanket(Boolean checked) {
        if (!checkbox_MusicalInstrumentsBlanket().isSelected() && checked) {
            clickAndHoldAndRelease(checkbox_MusicalInstrumentsBlanket);
        }
        
        return checked;
    }

    private Guidewire8Select select_MusicalInstruments() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':MusicalInstrumentsTermInput-triggerWrap')]");
    }

    private void setMusicalInstruments(MusicalInstrumentsBlanket_MusicalInstruments musicalInstruments) {
        Guidewire8Select mySelect = select_MusicalInstruments();
        mySelect.selectByVisibleText(musicalInstruments.getValue());
        
    }

    @FindBy(xpath = "//input[contains(@id,':0:CovTermInputSet:DirectCovTermInputSet:DirectTermInput-inputEl')]")
    private WebElement input_MusicalLimitPerAnyOneOccurrence;

    private void setMusicalLimitPerAnyOneOccurrence(String value) {
        input_MusicalLimitPerAnyOneOccurrence.click();
        input_MusicalLimitPerAnyOneOccurrence.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    ///////////////////////
    //  Scheduled Items  //
    ///////////////////////

    @FindBy(xpath = "//span[contains(@id, ':IMComArticlesScheduledArticlesPanelSet:Add-btnEl')]")
    private WebElement buttonAddScheduledItem;

    private void clickAddScheduledItem() {
        clickWhenClickable(buttonAddScheduledItem);
        
    }

    @FindBy(xpath = "//textarea[contains(@id, ':IMComArticlesScheduledArticlesPanelSet:description-inputEl')]")
    private WebElement textarea_ScheduledItemDescription;

    private void setItemDescription(String value) {
        textarea_ScheduledItemDescription.click();
        textarea_ScheduledItemDescription.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    @FindBy(xpath = "//input[contains(@id, ':IMComArticlesScheduledArticlesPanelSet:limit-inputEl')]")
    private WebElement input_ScheduledItemLimit;

    private void setItemLimit(String value) {
        input_ScheduledItemLimit.click();
        input_ScheduledItemLimit.sendKeys(value + Keys.TAB);
        waitForPostBack();
        
    }

    private Guidewire8Select select_EquipmentType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':IMComArticlesScheduledArticlesPanelSet:type-triggerWrap')]");
    }

    private void setEquipmentType(CommercialArticles_EquipmentType equipmentType) {
        Guidewire8Select mySelect = select_EquipmentType();
        mySelect.selectByVisibleText(equipmentType.getValue());
        
    }

    private Guidewire8Select select_ItemMotionPicture() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':IMComArticlesScheduledArticlesPanelSet:motionPicture-triggerWrap')]");
    }

    private void setItemMotionPicture(CamerasBlanket_MotionPicture motionPicture) {
        Guidewire8Select mySelect = select_ItemMotionPicture();
        mySelect.selectByVisibleText(motionPicture.getValue());
        
    }

    private Guidewire8Select select_ItemMusicalInstruments() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':IMComArticlesScheduledArticlesPanelSet:instruments-triggerWrap')]");
    }

    private void setItemMusicalInstruments(MusicalInstrumentsBlanket_MusicalInstruments musicalInstruments) {
        Guidewire8Select mySelect = select_ItemMusicalInstruments();
        mySelect.selectByVisibleText(musicalInstruments.getValue());
        
    }

    private Guidewire8Select select_ItemBandType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':IMComArticlesScheduledArticlesPanelSet:bandType-triggerWrap')]");
    }

    private void setItemBandType(CommercialArticles_BandType bandType) {
        Guidewire8Select mySelect = select_ItemBandType();
        mySelect.selectByVisibleText(bandType.getValue());
        
    }

    ///////////////////////
    //   PUBLIC METHODS  //
    ///////////////////////

    public void fillOutCommercialArticles(GeneratePolicy policy) {

        // COVERAGES

        clickCoveragesTab();

        CPPInlandMarineCommercialArticles commercialArticles = policy.inlandMarineCPP.getCommercialArticles();

        setDeductible(commercialArticles.getDeductible());

        if (commercialArticles.isCamerasBlanket()) {
            checkCamerasBlanket(true);
            setMotionPicture(commercialArticles.getMotionPicture());
            setCameraLimitPerAnyOneOccurrence(commercialArticles.getCamerasLimitPerAnyOneOccurance());
        }

        

        if (commercialArticles.isMusicalInstrumentsBlanket()) {
            checkMusicalInstrumentsBlanket(true);
            setMusicalInstruments(commercialArticles.getMusicalInstruments());
            setMusicalLimitPerAnyOneOccurrence(commercialArticles.getMusicalInstrumentsLimitPerAnyOneOccurance());
        }

        // SCHEDULED ITEMS

        clickScheduledItemsTab();

        if (!commercialArticles.getScheduledItems().isEmpty()) {
            List<CPPInlandMarineCommercialArticles_ScheduledItem> scheduledItems = commercialArticles.getScheduledItems();

            for (CPPInlandMarineCommercialArticles_ScheduledItem item : scheduledItems) {

                clickAddScheduledItem();

                setItemDescription(item.getDescription());
                setItemLimit(item.getLimit());
                setEquipmentType(item.getEquipmentType());

                switch (item.getEquipmentType()) {
                    case Camera:
                        setItemMotionPicture(item.getMotionPicture());
                        break;
                    case MusicalInstruments:
                        setItemMusicalInstruments(item.getMusicalInstruments());
                        setItemBandType(item.getBandType());
                        break;
                    default:
                        break;
                }

            }
        }


        // EXCLUSIONS & CONDITIONS

        clickExclusionsAndConditionsTab();

        // ADDITIONAL INTEREST

        clickAdditionalInterestTab();

        // UNDERWRITING QUESTIONS

        clickUnderwritingQuestionsTab().answerQuestions(policy.inlandMarineCPP.getUwQuestionsByCoveragePart(InlandMarineCoveragePart.CommercialArticles_CM_00_20), new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy));

    }

}
