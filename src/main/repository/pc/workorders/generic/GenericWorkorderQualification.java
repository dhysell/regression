package repository.pc.workorders.generic;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderQualification extends GenericWorkorder {


    private WebDriver driver;
    private TableUtils tableUtils;

    public GenericWorkorderQualification(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    protected String radioElement(String keyword, boolean radioValue) {
        if (radioValue) {
            return "//div[contains(text(), '" + keyword + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "Yes" + "']/preceding-sibling::input";
        } else {
            return "//div[contains(text(), '" + keyword + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "No" + "']/preceding-sibling::input";
        }
    }

    protected String checkElement(String keyword, boolean radioValue) {
        if (radioValue) {
            return "//div[contains(text(), '" + keyword + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "Yes" + "']/parent::td[contains(@class, 'cb-checked')]";
        } else {
            return "//div[contains(text(), '" + keyword + "')]/parent::td/following-sibling::td/descendant::td/child::label[text()='" + "No" + "']/parent::td[contains(@class, 'cb-checked')]";
        }
    }

    private WebElement getQuestionText(String qualification, String followup) {
        return find(By.xpath("//div[contains(text(), '" + qualification + "')]/ancestor::tr[1]/following-sibling::tr[1]/child::td/div[contains(text(),'" + followup + "')]"));
    }

    private WebElement getDiv(String qualification, String followup) {
        WebElement divToReturn = getQuestionText(qualification, followup).findElement(By.xpath("//div[contains(text(), '" + qualification + "')]/ancestor::tr[1]/following-sibling::tr[1]/child::td/div[contains(text(),'" + followup + "')]/ancestor::td[1]/following-sibling::td/div"));
        return divToReturn;
    }

    private WebElement getEditbox(String qualification, String followup) {
        return find(By.xpath("//div[contains(@id,':PreQualQuestionSetsDV:QuestionSetsDV:')]/following-sibling::div[1]/table/tbody/tr/td[2]/textarea"));
    }

    private WebElement getEditboxInput(String qualification, String followup) {
        return find(By.xpath("//div[contains(@id,':PreQualQuestionSetsDV:QuestionSetsDV:')]/following-sibling::div[1]/table/tbody/tr/td[2]/input"));
    }

    /**
     * @param qualification      the text of the qualification question
     * @param expectedRadioValue the radio you are expecting to be selected ( true = yes, false = no)
     * @return whether a given qualification question has what you expect selected
     * Example:
     * Q = Are any owned vehicles insured with Hagerty or American Modern?;
     * expectedRadio = false;
     * So qualificationQuestionHasRadioExpectedChecked(Q, expectedRadio) should return true if the the answer is marked
     * no.
     */
    private boolean qualificationQuestionHasRadioExpectedChecked(String qualification, boolean expectedRadioValue) {
        return finds(By.xpath(checkElement(qualification, expectedRadioValue))).size() > 0;
    }


    public void fillOutQualifcationsPage(GeneratePolicy policy) throws Exception {
        switch (policy.productType) {
            case Businessowners:
//                setQuickQuoteAll(false);
                break;
            case CPP:
                setCPPPreQuallification(false);
                if (policy.lineSelection.contains(LineSelection.CommercialPropertyLineCPP)) {
                    setCPFullAppQualification(false);
                }
                if (policy.lineSelection.contains(LineSelection.GeneralLiabilityLineCPP)) {
                    setGLPreQualification(false);
                }
                if (policy.lineSelection.contains(LineSelection.CommercialAutoLineCPP)) {
                    setCAQualificationRequired(false);
                }
                break;
            case Squire:
                break;
            case PersonalUmbrella:
            	if(!policy.squireUmbrellaInfo.getCurrentPolicyType().equals(GeneratePolicyType.QuickQuote)) {
            		setUmbrellaQuestionsFavorably();
            	}
                break;
            case StandardIM:                
                break;
            case Membership:
                break;
            case StandardFire:            	
                break;
            case StandardLiability:  
                break;
        }
        waitForPostBack();
    }

    //////////////////
    //// BOP
    //////////////////


    public void setQuickQuoteAll(boolean radioValue) throws Exception {
        getToQualificationsPage();
        clickBOP_Aircraft(radioValue);
        clickBOP_Firearms(radioValue);
        clickBOP_Hazardous(radioValue);
        clickBOP_Recreation(radioValue);
        clickBOP_Watercraft(radioValue);
        clickBOP_OutsideUSOrCanada(radioValue);
        click_ConvictedOfAnyFelony(radioValue);
    }


    public void setFullAppAllTo(boolean radioValue) throws Exception {
        getToQualificationsPage();
        clickBOP_Aircraft(radioValue);
        clickBOP_Firearms(radioValue);
        clickBOP_Hazardous(radioValue);
        clickBOP_Recreation(radioValue);
        clickBOP_Watercraft(radioValue);
        clickBOP_OutsideUSOrCanada(radioValue);
        // Felony gets set at the end since it has extra questions
        click_CoverageDeclinedCanceledNonRenewed(radioValue);
        clickBOP_Losses(radioValue);
        click_UndergoingForeclosureRepossessionBankruptcy(radioValue);
        clickBOP_RePackagedReBranded(radioValue);
        clickBOP_ProductsSoldUnderOtherLabel(radioValue);
        clickBOP_Recall(radioValue);
        // Loaned gets set at the end since it has extra questions
        clickBOP_EmployeesLeasedToOthers(radioValue);
        clickBOP_SponsorEvents(radioValue);
        clickBOP_Subcontract(radioValue);
        clickBOP_OtherOperations(radioValue);
        clickBOP_Installation(radioValue);
        clickBOP_Exposure(radioValue);
        clickBOP_Cyber(radioValue);
        // These two have the extra questions
        click_ConvictedOfAnyFelony(radioValue);
        clickBOP_Loaned(radioValue);
    }

    public void fillOutFullAppQualifications(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuQualification();

        switch (policy.productType) {
            case Businessowners:
                setFullAppAllTo(false);
                break;
            case CPP:
                if (policy.lineSelection.contains(LineSelection.CommercialPropertyLineCPP)) {
                    setCPFullAppQualification(false);
                }
                if (policy.lineSelection.contains(LineSelection.GeneralLiabilityLineCPP)) {
                    setGLFullAppQualification(false);
                }
                if (policy.lineSelection.contains(LineSelection.CommercialAutoLineCPP)) {
                    setCAQualification(false);
                }
                break;
            case Squire:
                setSquireGeneralFullTo(false);
                if (policy.lineSelection.contains(LineSelection.PropertyAndLiabilityLinePL)) {
                    setSquireHOFullTo(false, "I was in a biker gang staying in motels, and not a homeowner");
                    setSquireGLFullTo(false);
                    if (policy.squire.isCountry() || policy.squire.isFarmAndRanch()) {
                        clickQualificationGLCattle(false);
                    }
                }
                if (policy.lineSelection.contains(LineSelection.PersonalAutoLinePL)) {
                    //This will not set the question regarding prior automobile insurance to false. This is to avoid an underwriting issue later.
                    setSquireAutoFullTo(false);
                }
                if (policy.lineSelection.contains(LineSelection.InlandMarineLinePL)) {
                    clickQualificationIMLosses(false);
                }
                break;
            case Membership:
                break;
            case PersonalUmbrella:
            	setUmbrellaQuestionsFavorably();
                break;
            case StandardFire:
                setStandardFireFullTo(false);
                setSquireGeneralFullTo(false);
                break;
            case StandardIM:
            	setStandardIMQuestionIMLosses();
                break;
            case StandardLiability:
            	 setSquireGeneralFullTo(false);
                 if (policy.lineSelection.contains(LineSelection.StandardLiabilityPL)) {
                     setStandardLiabilityFullTo(false);
                 }
                break;
        }

//        delay(1000);
//
//        ErrorHandlingQualifications errorHandling = new ErrorHandlingQualifications(getDriver());
//        errorHandling.errorHandlingQualificationPage(1000);
    }//END fillOutFullAppQualifications


    /////////////////////
    /// COMMERCIAL PROPERTY
    /////////////////////


    public void setCPPPreQuallification(boolean radioValue) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderQualification_CommercialProperty commProperty = new repository.pc.workorders.generic.GenericWorkorderQualification_CommercialProperty(getDriver());
        getToQualificationsPage();
        waitUntilElementIsVisible(text_QualificationsTitle);
        click_ConvictedOfAnyFelony(radioValue);
        click_CoverageDeclinedCanceledNonRenewed(radioValue);
        click_UndergoingForeclosureRepossessionBankruptcy(radioValue);
        commProperty.clickCPP_PriorInsurance(radioValue);
        commProperty.clickCPP_LossesLast3Years(radioValue);
    }


    public void setCPFullAppQualification(boolean radioValue) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderQualification_CommercialProperty commProperty = new GenericWorkorderQualification_CommercialProperty(getDriver());
        getToQualificationsPage();
        commProperty.clickCPHasOtherInsurance_SharedRisk(radioValue);
    }


    //////////////////
    //// GENERAL LIABILITY
    //////////////////


    public void setGLPreQualification(boolean radioValue) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderQualification_GeneralLiability generalLiability = new repository.pc.workorders.generic.GenericWorkorderQualification_GeneralLiability(getDriver());
        getToQualificationsPage();
        generalLiability.clickGL_HazardousMaterials(radioValue);
        generalLiability.clickGL_RecreationalActivities(radioValue);
        generalLiability.clickGL_Explosives(radioValue);
        generalLiability.clickGL_RelatedToIndustry(radioValue);
        generalLiability.clickGL_BeachesNotIncidental(radioValue);
        generalLiability.clickGL_ManufactureAnyProduct(radioValue);
        generalLiability.clickGL_OtherBusinessNames(radioValue);
        generalLiability.clickGL_DocksOrFloats(radioValue);
        generalLiability.clickGL_ExposureToChemicals(radioValue);
        generalLiability.clickGL_DistributeProductsOutside(radioValue);
        generalLiability.clickGL_SoldOrRepackaged(radioValue);
        generalLiability.clickGL_RecalledAnyProduct(radioValue);
        generalLiability.clickGL_DescontinuedAnyLines(radioValue);
        generalLiability.clickGL_SeasonalBusiness(radioValue);

    }


    public void setGLFullAppQualification(boolean radioValue) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderQualification_GeneralLiability generalLiability = new GenericWorkorderQualification_GeneralLiability(getDriver());
        getToQualificationsPage();
        generalLiability.clickGL_OtherOperationLocations(radioValue);
        generalLiability.clickGL_EquipmentLoanedOrRented(radioValue);
        generalLiability.clickGL_SponsorOrConductEvents(radioValue);
        generalLiability.clickGL_SubcontractAnyWork(radioValue);
        generalLiability.clickGL_OperationsOutsideIdaho(radioValue);
        generalLiability.clickGL_ProductsSoldOtherLabels(radioValue);
        generalLiability.clickQualificationNotForProfitOrgCharitableCauses(radioValue);
    }


    ////////////////
    /// COMMERCIAL AUTO
    ////////////////


    public void setCAQualificationRequired(boolean radioValue) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderQualification_CommercialAuto commauto = new repository.pc.workorders.generic.GenericWorkorderQualification_CommercialAuto(getDriver());
        getToQualificationsPage();
        commauto.clickCA_AutoWithOutDrivers(radioValue);
        commauto.clickCA_InterchangeAgreement(radioValue);
        commauto.clickCA_IndustrialUseVehicles(radioValue);
        commauto.clickCA_ServiceCommision(radioValue);
        commauto.clickCA_InterstateCommerce(radioValue);
        commauto.clickCA_HazardousMaterialWarning(radioValue);
        commauto.clickCA_TransportTheFollowing(radioValue);
        commauto.clickCA_TransportMilk(radioValue);
        commauto.clickCA_DUI(radioValue);
        commauto.clickCA_YouthfulDrivers(radioValue);
        commauto.clickCA_FoodDelivery(radioValue);
        commauto.clickCA_PilotCarService(radioValue);
    }


    public void setCAQualification(boolean radioValue) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderQualification_CommercialAuto commauto = new GenericWorkorderQualification_CommercialAuto(getDriver());
        getToQualificationsPage();
        commauto.clickCA_LeasedAutosWithOthers(radioValue);
        commauto.clickCA_LicenseRevoced(radioValue);
        commauto.clickCA_TrafficCitation(radioValue);
        commauto.clickCA_RetailBasis(radioValue);
        commauto.clickCA_TimeDelivery(radioValue);
        commauto.clickCA_TransportForOthers(radioValue);

    }


    ///////////////////
    /// PERSONAL LINES
    ///////////////////


    public void setUmbrellaQuestionsFavorably() {
        setPL_UmbrellaPolicyAutoCoverAll(true);
        setPL_UmbrellaPolicyCoverToys(true);
        setPL_UmbrellaPolicyOutOfStateOperations(false);
        setPL_UmbrellaPolicyLiabilityLosses(false);
    }


    public void setSquireAutoFullTo(boolean radioValue) throws Exception {
        getToQualificationsPage();
        clickPL_PALosses(radioValue);
        clickPL_Traffic(radioValue);
        click_ConvictedOfAnyFelony(radioValue);
        //clickPL_Impairment(radioValue);
        clickPL_Business(radioValue);
        clickPL_Hagerty(radioValue);
    }


    public void setStandardFireFullTo(boolean radioValue) throws Exception {
        getToQualificationsPage();
        clickPL_STDFireSupportingBusiness(true);
        clickPL_HOLosses(radioValue);
        clickPL_HOPriorInsurance(radioValue);
        clickPL_HOExistingDamage(radioValue);
        //clickPL_HOFloodInsurance(radioValue);
        //clickPL_HOBusinessConducted(radioValue);
    }


    public void setStandardLiabilityFullTo(boolean radioValue) throws Exception {
        getToQualificationsPage();
        clickPL_STDFireSupportingBusiness(true);
        clickPL_GLLosses(radioValue);
        clickPL_GLHazard(radioValue);
        clickPL_GLManufacturing(radioValue);
        clickPL_GLLivestock(radioValue);
    }


    public void setStandardIM4HQuestionsFavorably() {
        set4HQualificationInjury(false);
        set4HQualificationContagiousDisease(false);
        set4HQualificationInspected(true);
        answerFollowupDiv(DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter)), "I have examined", "Date of Inspection");
    }


    public void setSquireGeneralFullTo(boolean radioValue) throws Exception {
        getToQualificationsPage();
        clickPL_Cancelled(radioValue);
        clickPL_Bankruptcy(radioValue);
    }


    public void setStandardIMQuestionIMLosses() {
        clickPL_STDFireSupportingBusiness(true);
        clickQualificationIMLosses(false);
    }


    public void setStandardIMQuestionsFaborably() {
        clickPL_STDFireSupportingBusiness(true);
        clickQualificationIMLosses(false);
    }

    // HO

    public void setSquireHOFullTo(boolean radioValue, String reasonForNoPriorInsurance) throws Exception {
        getToQualificationsPage();
        clickPL_HOLosses(radioValue);
        clickPL_HOPriorInsurance(radioValue);
        answerFollowupDiv(reasonForNoPriorInsurance, "prior property insurance", "no prior insurance");
        try {
            clickQualificationHOInsuranceCancelled(radioValue);
            answerFollowupDiv("No Insurance Cancelled", "property insurance cancelled", "for cancel");
        } catch (Exception e) {
        }

        clickPL_HOExistingDamage(radioValue);
        clickPL_HOFloodInsurance(radioValue);
        clickPL_HOBusinessConducted(radioValue);
    }

    // GL

    public void setSquireGLFullTo(boolean radioValue) throws Exception {
        getToQualificationsPage();
        clickPL_GLLosses(radioValue);
        // clickQualificationGLCancelled(radioValue);
        clickPL_GLHazard(radioValue);
        clickPL_GLManufacturing(radioValue);
        clickQualificationGLhorses(radioValue);
        clickPL_GLLivestock(radioValue);
    }


    public void setPLQuestionsToFalse() throws Exception {
        setSquireGeneralFullTo(false);
        try {
            setSquireAutoFullTo(false);
        } catch (Exception e) {
        }
        try {
            setSquireHOFullTo(false, "First Home");
            setSquireGLFullTo(false);
            clickQualificationIMLosses(false);
        } catch (Exception e) {
        }
    }


    @FindBy(xpath = "//div[contains(@id, ':PreQualQuestionSetsDV:QuestionSetsDV:1:QuestionSetLV')]")
    private WebElement text_QualificationsPageDiv;

    @FindBy(xpath = "//span[contains(@class, '-title') and contains(text(), 'Qualification')]")
    private WebElement text_QualificationsTitle;

    @FindBy(xpath = "//div[@class='message']")
    private List<WebElement> validationMessages;

    @FindBy(xpath = "//div[contains(text(), 'Date of Inspection')]/parent::td/following-sibling::td/div")
    private WebElement div_QualificationsDateOfInspectionElement;


    public void answerFollowupDiv(String text, String qualification, String followup) {
        clickWhenClickable(getDiv(qualification, followup));
        if (qualification.contains("I have examined")) {
            new TableUtils(getDriver()).setValueForCellInsideTable(text_QualificationsPageDiv, "c2", text);
            return;
        } else if ((!qualification.contains("business conducted") && !qualification.contains("cattle"))) {
        	
            try {
                clickWhenClickable(getEditbox(qualification, followup));
            } catch (Exception e) {
                systemOut("The Edit Box was clicked, but the resulting text area is not currently available. Trying again...");
                clickWhenClickable(getDiv(qualification, followup));
                clickWhenClickable(getEditbox(qualification, followup));
            }
            waitForPostBack();
            setText(getEditbox(qualification, followup), text);   
        }
    }


    // General

    public List<WebElement> getValidationMessages() {
        return validationMessages;
    }


    public void clickQualificationAnimalClaims(boolean radioValue) {
        tableUtils.setRadioByText("animal claims", radioValue);
    }

    // BOP AND CPP QUESTIONS

    public void click_ConvictedOfAnyFelony(boolean radioValue) {
        tableUtils.setRadioByText("felony", radioValue);
    }


    public void click_CoverageDeclinedCanceledNonRenewed(boolean radioValue) {
        tableUtils.setRadioByText("non-renewed", radioValue);
    }


    public void click_UndergoingForeclosureRepossessionBankruptcy(boolean radioValue) {
        tableUtils.setRadioByText("foreclosure", radioValue);
    }


    // BOP QUESTIONS

    public void clickBOP_Aircraft(boolean radioValue) {
        tableUtils.setRadioByText("aircraft", radioValue);
    }


    public void clickBOP_Firearms(boolean radioValue) {
        tableUtils.setRadioByText("firearms", radioValue);
    }


    public void clickBOP_Hazardous(boolean radioValue) {
        tableUtils.setRadioByText("hazardous", radioValue);
    }


    public void clickBOP_Recreation(boolean radioValue) {
        tableUtils.setRadioByText("recreations", radioValue);
    }


    public void clickBOP_Watercraft(boolean radioValue) {
        tableUtils.setRadioByText("boat", radioValue);
    }


    public void clickBOP_OutsideUSOrCanada(boolean radioValue) {
        tableUtils.setRadioByText("Canada", radioValue);
    }


    public void clickBOP_Losses(boolean radioValue) {
        tableUtils.setRadioByText("losses", radioValue);
    }


    public void clickBOP_RePackagedReBranded(boolean radioValue) {
        tableUtils.setRadioByText("re-packaged", radioValue);
    }


    public void clickBOP_ProductsSoldUnderOtherLabel(boolean radioValue) {
        tableUtils.setRadioByText("products sold under the label of others", radioValue);
    }


    public void clickBOP_Recall(boolean radioValue) {
        tableUtils.setRadioByText("recalled", radioValue);
    }


    public void clickBOP_Loaned(boolean radioValue) {
        tableUtils.setRadioByText("loaned", radioValue);
    }


    public void clickBOP_EmployeesLeasedToOthers(boolean radioValue) {
        tableUtils.setRadioByText("Are employees leased to others", radioValue);
    }


    public void clickBOP_SponsorEvents(boolean radioValue) {
        tableUtils.setRadioByText("sponsor", radioValue);
    }


    public void clickBOP_Subcontract(boolean radioValue) {
        tableUtils.setRadioByText("subcontract", radioValue);
    }


    public void clickBOP_OtherOperations(boolean radioValue) {
        tableUtils.setRadioByText("have other operations or locations", radioValue);
    }


    public void clickBOP_Installation(boolean radioValue) {
        tableUtils.setRadioByText("installation", radioValue);
    }


    public void clickBOP_Exposure(boolean radioValue) {
        tableUtils.setRadioByText("exposure", radioValue);
    }


    public void clickBOP_Cyber(boolean radioValue) {
        tableUtils.setRadioByText("cyber", radioValue);
    }


    // SQUIRE QUESTIONS
    // PA

    public void clickQualificationAutoLosses(boolean radioValue) {
        tableUtils.setRadioByText("Auto losses", radioValue);
    }


    public void clickPL_Traffic(boolean radioValue) {
        tableUtils.setRadioByText("traffic", radioValue);
    }


    public void clickPL_Impairment(boolean radioValue) {
        tableUtils.setRadioByText("impairment", radioValue);
    }


    public void clickPL_Business(boolean radioValue) {
        tableUtils.setRadioByText("business purposes", radioValue);
    }


    public void clickPL_Employees(boolean radioValue) {
        tableUtils.setRadioByText("employees that drive these vehicles", radioValue);
    }


    public void clickPL_Hagerty(boolean radioValue) {
        tableUtils.setRadioByText("Hagerty", radioValue);
    }
    
    public void setHagertyVehicleList(String vehicles) {    	
    	answerFollowupDiv(vehicles, "Are any owned vehicles insured with Hagerty or American Modern?", "List vehicles");
    }


    public void clickPL_Cancelled(boolean radioValue) {
        tableUtils.setRadioByText("cancelled", radioValue);
    }


    public void clickPL_Bankruptcy(boolean radioValue) {
        tableUtils.setRadioByText("bankruptcy", radioValue);
    }


    public void clickPL_PALosses(boolean radioValue) {
        tableUtils.setRadioByText("Auto losses", radioValue);
    }

    // //HO

    public void clickPL_HOLosses(boolean radioValue) {
        tableUtils.setRadioByText("property losses", radioValue);
    }


    public void clickQualificationHOWaterDamage(boolean radioValue) {
        tableUtils.setRadioByText("water damage", radioValue);
    }


    public void clickPL_HOPriorInsurance(boolean radioValue) {
        tableUtils.setRadioByText("prior property insurance", radioValue);
    }


    public void clickQualificationHOInsuranceCancelled(boolean radioValue) {
        tableUtils.setRadioByText("property insurance cancelled,", radioValue);
    }


    public void clickPL_HOExistingDamage(boolean radioValue) {
        tableUtils.setRadioByText("existing damage", radioValue);
    }


    public void clickPL_HOFloodInsurance(boolean radioValue) {
        tableUtils.setRadioByText("flood insurance", radioValue);
    }


    public void clickPL_HOBusinessConducted(boolean radioValue) {
        tableUtils.setRadioByText("business conducted in any buildings", radioValue);
    }

    // PL Inland Marine

    public void clickQualificationIMLosses(boolean radioValue) {
        tableUtils.setRadioByText("Inland Marine losses", radioValue);
    }

    // GL

    public void clickPL_GLLosses(boolean radioValue) {
        tableUtils.setRadioByText("liability losses", radioValue);
    }


    public void clickQualificationGLAnimalClaim(boolean radioValue) {
        tableUtils.setRadioByText("dog bite or other animal claims", radioValue);
    }


    public void clickQualificationGLCancelled(boolean radioValue) {
        tableUtils.setRadioByText("liability insurance cancelled,", radioValue);
    }


    public void clickPL_GLHazard(boolean radioValue) {
        tableUtils.setRadioByText("special hazards", radioValue);
    }


    public void clickPL_GLManufacturing(boolean radioValue) {
        tableUtils.setRadioByText("manufacturing,", radioValue);
    }


    public void clickQualificationGLhorses(boolean radioValue) {
        tableUtils.setRadioByText("pasture other people", radioValue);
    }


    public void clickPL_GLLivestock(boolean radioValue) {
        tableUtils.setRadioByText("livestock", radioValue);
    }


    public void clickQualificationGLCattle(boolean radioValue) {
        tableUtils.setRadioByText("feed cattle", radioValue);
    }


    public void clickPL_STDFireSupportingBusiness(boolean radioValue) {
        tableUtils.setRadioByText("have supporting business with another Farm Bureau", radioValue);
    }


    //Umbrella

    public void setPL_UmbrellaPolicyAutoCoverAll(boolean radioValue) {
        tableUtils.setRadioByText("cover all owned or leased automobiles", radioValue);
    }


    public void setPL_UmbrellaPolicyHagerty(boolean radioValue) {
        tableUtils.setRadioByText("Are any owned vehicles insured with", radioValue);
    }


    public void setPL_UmbrellaPolicyCoverToys(boolean radioValue) {
        tableUtils.setRadioByText(" underlying policy cover all ATVs, Snowmobiles, Watercrafts, and Personal Watercrafts", radioValue);
    }


    public void setPL_UmbrellaPolicyOutOfStateOperations(boolean radioValue) {
        tableUtils.setRadioByText("any out-of-state operations", radioValue);
    }


    public void setPL_UmbrellaPolicyLiabilityLosses(boolean radioValue) {
        tableUtils.setRadioByText("any liability losses paid or now reserved in amounts greater", radioValue);
    }
    
//    public void fillOutUmbrellaQualificationQuestions() {
//    	setPL_UmbrellaPolicyAutoCoverAll(true);
//    	setPL_UmbrellaPolicyCoverToys(true);
//    	setPL_UmbrellaPolicyOutOfStateOperations(false);
//    	setPL_UmbrellaPolicyLiabilityLosses(false);
//    }


    //IM 4H

    public void set4HQualificationInjury(boolean radioValue) {
        tableUtils.setRadioByText("afflicted with any disease or sickness, or have received any injury", radioValue);
    }


    public void set4HQualificationContagiousDisease(boolean radioValue) {
        tableUtils.setRadioByText("contagious or communicable disease common to livestock", radioValue);
    }


    public void set4HQualificationInspected(boolean radioValue) {
        tableUtils.setRadioByText("I have examined", radioValue);
    }


    public void clickQualificationNext() {
        super.clickPolicyChangeNext();
    }


    public void clickQuote() {
        super.clickGenericWorkorderQuote();
    }


    public void clickQuoteOptionsQuote() {
        super.clickGenericWorkorderQuoteOptionsQuote();
    }

    private void getToQualificationsPage() throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQualification();
    }


    public void validatePortalQualifactionQuestionsSet(boolean employeeVehicleUse, boolean driversWithPhysicalImpairment) throws Exception {
        getToQualificationsPage();
        String q3 = "Are you adding a vehicle used for business purposes and driven by employees?";
        String q4 = "Do any drivers who will be added to the policy have a physical impairment, such as epilepsy or blindness, that impact their ability to drive?";
        boolean b3, b4 = false;

        b3 = qualificationQuestionHasRadioExpectedChecked(q3, employeeVehicleUse);
        b4 = qualificationQuestionHasRadioExpectedChecked(q4, driversWithPhysicalImpairment);

        Assert.assertTrue(b3, "Qualification question: " + q3 + " not set correctly.");
        Assert.assertTrue(b4, "Qualification question: " + q4 + " not set correctly.");
    }


    //vehicles used for the following business purposes

    public void checkBusinessPurposeUsedVehicle(String vehicles) {
        find(By.xpath("//td/div[contains(text(), '" + vehicles + "')]/../following-sibling::td/div[contains(@class, 'inner-checkcolumn')]/img")).click();
    }

}
