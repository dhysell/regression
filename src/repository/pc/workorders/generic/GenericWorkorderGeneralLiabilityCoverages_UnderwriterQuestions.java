package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.GeneralLiability.IndicateIfTheApplicantInsuredIsA;
import repository.gw.enums.GeneralLiabilityCoverages;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;

import java.util.ArrayList;
import java.util.List;

public class GenericWorkorderGeneralLiabilityCoverages_UnderwriterQuestions extends BasePage {

    private WebDriver driver;
    public GenericWorkorderGeneralLiabilityCoverages_UnderwriterQuestions(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    Guidewire8RadioButton radio_HaveEmployeesIn() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Does applicant/insured have employees in Arkansas, California, Louisiana, New Mexico, or Vermont')]/parent::td/following-sibling::td/div/table");
    }

    Guidewire8RadioButton radio_DoesTheApplicant_InsuredHaveAContractWithTheAdditionalInsured() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Does the applicant/insured have a contract with the additional insured?')]/parent::td/following-sibling::td/div/table");
    }

    Guidewire8RadioButton radio_IsTheApplicantInsuredRequiredToProvideInsuredStatusToTheEntityHiredBySomeoneElseByContract() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Is the applicant/insured required to provide insured status to the entity hired by someone else by contract?')]/parent::td/following-sibling::td/div/table");
    }

    Guidewire8RadioButton radio_IsApplicant_InsuredTheManufacturerOfTheProduct() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Is applicant/insured the manufacturer of the product?')]/parent::td/following-sibling::td/div/table");
    }

    Guidewire8RadioButton radio_IsInsuredLicensedForUnmannedAircraftByTheFAA() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Is insured licensed for unmanned aircraft by the FAA?')]/parent::td/following-sibling::td/div/table");
    }

    Guidewire8RadioButton radio_AreAllOperatorsOverTheAgeOf21() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Are all operators over the age of 21?')]/parent::td/following-sibling::td/div/table");
    }

    Guidewire8RadioButton radio_WillTheUnmannedAircraftBeOperatedWithin5MilesOfAnAirportOrPrivateRunway() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Will the unmanned aircraft be operated within 5 miles of an airport or private runway?')]/parent::td/following-sibling::td/div/table");
    }

    Guidewire8RadioButton radio_WillTheUnmannedAircraftBeOperatedWithin5MilesOfAWildfire() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Will the unmanned aircraft be operated within 5 miles of a wildfire?')]/parent::td/following-sibling::td/div/table");
    }

    Guidewire8RadioButton radio_WillTheUnmannedAircraftBeOperatedOverPeopleOrCrowds() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Will the unmanned aircraft be operated over people or crowds?')]/parent::td/following-sibling::td/div/table");
    }

    Guidewire8RadioButton radio_WillTheUnmannedAircraftBeOperatedOutOfTheSightOfTheOperator() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Will the unmanned aircraft be operated out of the sight of the operator?')]/parent::td/following-sibling::td/div/table");
    }

    Guidewire8RadioButton radio_DoesTheUnmannedAircraftHaveAReturnToHomeFeature() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Does the unmanned aircraft have a return to home feature?')]/parent::td/following-sibling::td/div/table");
    }

    Guidewire8RadioButton radio_HaveAnyOfTheUnmannedOperatorsHadAnAccident() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Have any of the unmanned operators had an accident?')]/parent::td/following-sibling::td/div/table");
    }

    Guidewire8RadioButton radio_IsApplicantInsuredAnAdditionalInsuredOnTheManufacturersPolicy() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Is applicant/insured an additional insured on the manufacturers')]/parent::td/following-sibling::td/div/table");
    }

    //SELECT
    Guidewire8Select select_IndicateIfTheApplicantInsuredIsA() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Indicate if the applicant/insured is a:')]/parent::td/following-sibling::td/div");
    }

    //DROPDOWN
    @FindBy(xpath = "//div[contains(text(), 'Indicate if the applicant/insured is a:')]/parent::td/following-sibling::td/div")
    private WebElement dropdown_IndicateIfTheApplicantInsuredIsA;

    @FindBy(xpath = "//div[contains(text(), 'Type of unmanned aircraft.')]/parent::td/following-sibling::td/div")
    private WebElement dropdown_TypeOfUnmannedAircraft;

    @FindBy(xpath = "//div[contains(text(), 'Type of equipment attached to the unmanned aircraft.')]/parent::td/following-sibling::td/div")
    private WebElement dropdown_TypeOfEquipmentAttachedToTheUnmannedAircraft;

    @FindBy(xpath = "//div[contains(text(), 'Is applicant/insured an additional insured on the manufacturers')]/parent::td/following-sibling::td/div")
    private WebElement dropdown_IsApplicant_InsuredAnAdditionalInsuredOnTheManufacturersPolicy;

    //EDIT BOXES
    @FindBy(xpath = "//div[contains(text(), 'Please provide underwriting details of the equine exposure i.e. type of events, number of animals, number of events etc.')]/parent::td/following-sibling::td/div")
    private WebElement editbox_PleaseProvideUnderwritingDetailsOfTheEquineExposure_TypeOfEventsNumberOfAnimalsOrEvents;

    @FindBy(xpath = "//div[contains(text(), 'Weight of unmanned aircraft.')]/parent::td/following-sibling::td/div")
    private WebElement editbox_WeightOfUnmannedAircraft;

    @FindBy(xpath = "//div[contains(text(), 'What is the maximum altitude of the unmanned aircraft?')]/parent::td/following-sibling::td/div")
    private WebElement editbox_WhatIsTheMaximumAltitudeOfTheUnmannedAircraft;

    @FindBy(xpath = "//div[contains(text(), 'What is the maximum speed that the unmanned aircraft can operate at?')]/parent::td/following-sibling::td/div")
    private WebElement editbox_WhatIsTheMaximumSpeedThatTheUnmannedAircraftCanOperateAt;

    @FindBy(xpath = "//div[contains(text(), 'Please provide the qualifications of the drone operator.')]/parent::td/following-sibling::td/div")
    private WebElement editbox_PleaseProvideTheQualificationsOfTheDroneOperator;


    //START OF INDIVIDUAL UW QUESTIONS
    public void setEstimatedAnnualGrossReciepts(int reciepts) {
        find(By.xpath("//div[contains(text(), 'What is the applicant/insured estimated annual gross receipts?')]/parent::td/following-sibling::td/div")).click();
        
        find(By.xpath("//input[contains(@name, 'c2')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        find(By.xpath("//input[contains(@name, 'c2')]")).sendKeys(String.valueOf(reciepts));
        
        find(By.xpath("//div[contains(text(), 'What is the applicant/insured estimated annual gross receipts?')]")).click();
    }

    //CG 20 07
    public void setDoesTheApplicant_InsuredHaveAContractWithTheAdditionalInsured(boolean yesNo) {
        radio_DoesTheApplicant_InsuredHaveAContractWithTheAdditionalInsured().select(yesNo);
    }

    //CG 20 32
    public void setIsTheApplicantInsuredRequiredToProvideInsuredStatusToTheEntityHiredBySomeoneElseByContract(boolean yesNo) {
        radio_IsTheApplicantInsuredRequiredToProvideInsuredStatusToTheEntityHiredBySomeoneElseByContract().select(yesNo);
    }

    //CG 20 14
    public void setPleaseProvideUnderwritingDetailsOfTheEquineExposure_TypeOfEventsNumberOfAnimalsOrEvents(String desc) {
        editbox_PleaseProvideUnderwritingDetailsOfTheEquineExposure_TypeOfEventsNumberOfAnimalsOrEvents.click();
        
        find(By.xpath("//textarea[contains(@name, 'c2')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        find(By.xpath("//textarea[contains(@name, 'c2')]")).sendKeys(String.valueOf(desc));
        
        find(By.xpath("//textarea[contains(@name, 'c2')]")).sendKeys(Keys.chord(Keys.TAB));
        waitForPostBack();
    }

    //CG 20 15
    public void setIsApplicant_InsuredTheManufacturerOfTheProduct(boolean yesNo) {
        radio_IsApplicant_InsuredTheManufacturerOfTheProduct().select(yesNo);
    }

    //IDCG 31 2013
    public void setDoesApplicantInsuredHaveEmployeesInArkansasCaliforniaLouisianaNewMexicoOrVermont(boolean yesNo) {
        radio_HaveEmployeesIn().select(yesNo);
    }

    public void selectIndicateIfTheApplicantInsuredIsA(IndicateIfTheApplicantInsuredIsA appEmployees) {
        Guidewire8Select mySelect = select_IndicateIfTheApplicantInsuredIsA();
        mySelect.selectByVisibleText(appEmployees.getValue());
    }

    @SuppressWarnings("serial")
    public void selectIndicateIfApplicantIsA() {
        setGLDropDown("Indicate if the applicant/insured is a:", new ArrayList<String>() {{
            this.add("None of the above");
        }});
    }

    //CG 24 50

    public void setIsInsuredLicensedForUnmannedAircraftByTheFAA(boolean yesNo) {
        radio_IsInsuredLicensedForUnmannedAircraftByTheFAA().select(yesNo);
    }


    public void setAreAllOperatorsOverTheAgeOf21(boolean yesNo) {
        radio_AreAllOperatorsOverTheAgeOf21().select(yesNo);
    }


    public void selectTypeOfUnmannedAircraft(String aircraftType) {
        setGLDropDown("Type of unmanned aircraft.", new ArrayList<String>() {
            private static final long serialVersionUID = 1L;

            {
                this.add(aircraftType);
            }
        });
    }


    public void setWeightOfUnmannedAircraft(int weight) {
        editbox_WeightOfUnmannedAircraft.click();
        
        find(By.xpath("//input[contains(@name, 'c2')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        find(By.xpath("//input[contains(@name, 'c2')]")).sendKeys(String.valueOf(weight));
        
        clickWhenClickable(find(By.xpath("//div[contains(text(), 'Weight of unmanned aircraft.')]")));
    }


    public void selectTypeOfEquipmentAttachedToTheUnmannedAircraft(String aircraftEquip) {
        setGLDropDown("Type of equipment attached to the unmanned aircraft.", new ArrayList<String>() {
            private static final long serialVersionUID = 1L;

            {
                this.add(aircraftEquip);
            }
        });
    }


    public void setWillTheUnmannedAircraftBeOperatedWithin5MilesOfAnAirportOrPrivateRunway(boolean yesNo) {
        radio_WillTheUnmannedAircraftBeOperatedWithin5MilesOfAnAirportOrPrivateRunway().select(yesNo);
    }


    public void setWillTheUnmannedAircraftBeOperatedWithin5MilesOfAWildfire(boolean yesNo) {
        radio_WillTheUnmannedAircraftBeOperatedWithin5MilesOfAWildfire().select(yesNo);
    }


    public void setWillTheUnmannedAircraftBeOperatedOverPeopleOrCrowds(boolean yesNo) {
        radio_WillTheUnmannedAircraftBeOperatedOverPeopleOrCrowds().select(yesNo);
    }


    public void setWillTheUnmannedAircraftBeOperatedOutOfTheSightOfTheOperator(boolean yesNo) {
        radio_WillTheUnmannedAircraftBeOperatedOutOfTheSightOfTheOperator().select(yesNo);
    }


    public void setWhatIsTheMaximumAltitudeOfTheUnmannedAircraft(int altitude) {
        editbox_WhatIsTheMaximumAltitudeOfTheUnmannedAircraft.click();
        
        find(By.xpath("//input[contains(@name, 'c2')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        find(By.xpath("//input[contains(@name, 'c2')]")).sendKeys(String.valueOf(altitude));
        
        find(By.xpath("//div[contains(text(), 'What is the maximum altitude of the unmanned aircraft?')]")).click();
    }


    public void setWhatIsTheMaximumSpeedThatTheUnmannedAircraftCanOperateAt(int speed) {
        editbox_WhatIsTheMaximumSpeedThatTheUnmannedAircraftCanOperateAt.click();
        
        find(By.xpath("//input[contains(@name, 'c2')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        find(By.xpath("//input[contains(@name, 'c2')]")).sendKeys(String.valueOf(speed));
        
        clickWhenClickable(find(By.xpath("//div[contains(text(), 'What is the maximum speed that the unmanned aircraft can operate at?')]")));
    }


    public void setDoesTheUnmannedAircraftHaveAReturnToHomeFeature(boolean yesNo) {
        radio_DoesTheUnmannedAircraftHaveAReturnToHomeFeature().select(yesNo);
    }


    public void setPleaseProvideTheQualificationsOfTheDroneOperator(String desc) {
        editbox_PleaseProvideTheQualificationsOfTheDroneOperator.click();
        
        find(By.xpath("//textarea[contains(@name, 'c2')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        find(By.xpath("//textarea[contains(@name, 'c2')]")).sendKeys(String.valueOf(desc));
        
        clickWhenClickable(find(By.xpath("//div[contains(text(), 'Please provide the qualifications of the drone operator.')]")));
    }


    public void setHaveAnyOfTheUnmannedOperatorsHadAnAccident(boolean yesNo) {
        radio_HaveAnyOfTheUnmannedOperatorsHadAnAccident().select(yesNo);
    }

    //CG 24 10
    public void setIsApplicantInsuredAnAdditionalInsuredOnTheManufacturersPolicy(boolean yesNo) {
        radio_IsApplicantInsuredAnAdditionalInsuredOnTheManufacturersPolicy().select(yesNo);
    }

    //END OF INDIVIDUAL UW QUESTIONS


    //TODO Make sure this takes care of UW questions for coverages that have been added
    public void filloutUnderwriterQuestions(GeneratePolicy policy, GeneralLiabilityCoverages coverage) {
        if (policy.generalLiabilityCPP.getGLCoverages().isEmploymentPracticesLiabilityInsuranceIDCG312013()) {
            radio_HaveEmployeesIn().select(false);
            selectIndicateIfApplicantIsA();
        }

        if (!new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
            setEstimatedAnnualGrossReciepts(policy.generalLiabilityCPP.getGrossAnnualReciepts());
        }
        if (coverage != null) {
            setGLCoveragesUWQuestion(coverage);
        }
    }


    private void setGLDropDown(String questionText, List<String> choices) {
        find(By.xpath("//div[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(questionText) + ")]/parent::td/following-sibling::td/div")).click();
        
        find(By.xpath("//div[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(questionText) + ")]/parent::td/ancestor::table/parent::div/parent::div/following-sibling::div/descendant::input[contains(@name, 'c2')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        find(By.xpath("//div[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(questionText) + ")]/parent::td/ancestor::table/parent::div/parent::div/following-sibling::div/descendant::input[contains(@name, 'c2')]")).sendKeys(choices.get(NumberUtils.generateRandomNumberInt(0, choices.size() - 1)));
        
        clickWhenClickable(find(By.xpath("//div[contains(text(), " + StringsUtils.xPathSpecialCharacterEscape(questionText) + ")]")));
        
    }


    public void setGLCoveragesUWQuestion(GeneralLiabilityCoverages coverage) {

        switch (coverage) {
            case CG2007:
                setDoesTheApplicant_InsuredHaveAContractWithTheAdditionalInsured(true);
                break;
            case CG2032:
                setIsTheApplicantInsuredRequiredToProvideInsuredStatusToTheEntityHiredBySomeoneElseByContract(true);
                break;
            case CG2014:
                setPleaseProvideUnderwritingDetailsOfTheEquineExposure_TypeOfEventsNumberOfAnimalsOrEvents("Little Bunny Foo Foo");
                break;
            case CG2015:
                setIsApplicant_InsuredTheManufacturerOfTheProduct(true);
                break;
            case IDCG312013:
                setDoesApplicantInsuredHaveEmployeesInArkansasCaliforniaLouisianaNewMexicoOrVermont(false);
                
                selectIndicateIfTheApplicantInsuredIsA(IndicateIfTheApplicantInsuredIsA.NoneOfTheAbove);
                break;
            case CG2450:
                if (getIfQuickQuote(true)) {
                    setIsInsuredLicensedForUnmannedAircraftByTheFAA(true);
                    setAreAllOperatorsOverTheAgeOf21(true);
                    selectTypeOfUnmannedAircraft("Preassembled");
                    setWeightOfUnmannedAircraft(1);
                    selectTypeOfEquipmentAttachedToTheUnmannedAircraft("Photographic");
                    setWillTheUnmannedAircraftBeOperatedWithin5MilesOfAnAirportOrPrivateRunway(false);
                    setWillTheUnmannedAircraftBeOperatedWithin5MilesOfAWildfire(false);
                    setWillTheUnmannedAircraftBeOperatedOutOfTheSightOfTheOperator(false);
                    setWillTheUnmannedAircraftBeOperatedOverPeopleOrCrowds(false);
                    setWhatIsTheMaximumAltitudeOfTheUnmannedAircraft(1);
                    setWhatIsTheMaximumSpeedThatTheUnmannedAircraftCanOperateAt(1);
                    setDoesTheUnmannedAircraftHaveAReturnToHomeFeature(true);
                } else {
                    setIsInsuredLicensedForUnmannedAircraftByTheFAA(true);
                    setAreAllOperatorsOverTheAgeOf21(true);
                    selectTypeOfUnmannedAircraft("Preassembled");
                    setWeightOfUnmannedAircraft(1);
                    selectTypeOfEquipmentAttachedToTheUnmannedAircraft("Photographic");
                    setWillTheUnmannedAircraftBeOperatedWithin5MilesOfAnAirportOrPrivateRunway(false);
                    setWillTheUnmannedAircraftBeOperatedWithin5MilesOfAWildfire(false);
                    setWillTheUnmannedAircraftBeOperatedOutOfTheSightOfTheOperator(false);
                    setWillTheUnmannedAircraftBeOperatedOverPeopleOrCrowds(false);
                    setWhatIsTheMaximumAltitudeOfTheUnmannedAircraft(1);
                    setWhatIsTheMaximumSpeedThatTheUnmannedAircraftCanOperateAt(1);
                    setDoesTheUnmannedAircraftHaveAReturnToHomeFeature(true);
                    setPleaseProvideTheQualificationsOfTheDroneOperator("Little Bunny Foo Foo");
                    setHaveAnyOfTheUnmannedOperatorsHadAnAccident(false);
                }
                break;
            case CG2410:
                setIsApplicantInsuredAnAdditionalInsuredOnTheManufacturersPolicy(true);
                break;
            default:
                break;
        }
    }


    private boolean getIfQuickQuote(boolean quickQuote) {
        return checkIfElementExists(text_QuickQuote, 10);
    }

    @FindBy(xpath = "//span[@id='SubmissionWizard:JobWizardInfoBar:JobLabel-btnInnerEl']/span[contains(text(), 'Quick Quote')]")
    private WebElement text_QuickQuote;


}









