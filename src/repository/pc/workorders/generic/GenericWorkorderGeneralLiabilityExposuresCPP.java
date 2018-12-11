package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.enums.FormatType;
import repository.gw.enums.GeneralLiability.ExposureUnderwritingQuestions.CommercialConstructionType;
import repository.gw.enums.GeneralLiability.ExposureUnderwritingQuestions.ConstructionActivities;
import repository.gw.enums.GeneralLiability.ExposureUnderwritingQuestions.ResidentialConstructionType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.PackageRiskType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPGLExposureUWQuestions;
import repository.gw.generate.custom.CPPGeneralLiability;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

import java.util.ArrayList;
import java.util.List;

public class GenericWorkorderGeneralLiabilityExposuresCPP extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;

    public GenericWorkorderGeneralLiabilityExposuresCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    private void isOnPage() {
        if (finds(By.xpath("//label[contains(text(), 'Answer all questions for each location.')]")).isEmpty()) {
            if (finds(By.xpath("//span[contains(@class, 'g-title') and (text()='Exposures')]")).isEmpty()) {
                repository.pc.sidemenu.SideMenuPC sidemenu = new SideMenuPC(driver);
                sidemenu.clickSideMenuGLExposures();
            }
            clickLocationSpecificQuestionsTab();
        }
    }

    @FindBy(xpath = "//div[contains(@id, ':SubmissionWizard_GL_ExposureUnitsLV')]")
    public WebElement table_Exposures;

    @FindBy(xpath = "//span[contains(@id, ':GLL_ExposureDetailsCardTab-btnEl')]")
    public WebElement link_ExposureDetailsTab;

    @FindBy(xpath = "//span[contains(@id, ':GLL_ExposureQuestionsCardTab-btnEl')]")
    public WebElement link_UnderwritingQuestionsTab;

    @FindBy(xpath = "//span[contains(@id, ':CGLQuestionsTabbedTab-btnEl')]")
    public WebElement link_LocationSpecificQuestions;

    @FindBy(xpath = "//div[@class='message']")
    public List<WebElement> list_errorMessages;


    //GL_ExposureUnitsLV


    public void clickNext() {
        super.clickNext();
    }


    public void clickAdd() {
        super.clickAdd();
    }


    public void clickRemove() {
        super.clickRemove();
    }

    public void selectClassCode(String classCode) {
        tableUtils.setCheckboxInTableByText(table_Exposures, classCode, true);
    }


    public void selectAll() {
        waitUntilElementIsVisible(table_Exposures);
        tableUtils.setTableTitleCheckAllCheckbox(table_Exposures, true);
    }


    public List<String> getValidationMessages() {
        List<String> stringList = new ArrayList<String>();
        for (WebElement message : list_errorMessages) {
            stringList.add(message.getText());
        }
        return stringList;
    }


    public void clickExposureDetialsTab() {
        clickWhenClickable(link_ExposureDetailsTab);
    }


    public void clickUnderwritingQuestionsTab() {
        clickWhenClickable(link_UnderwritingQuestionsTab);
        
    }


    public void clickLocationSpecificQuestionsTab() {
        clickWhenClickable(link_LocationSpecificQuestions);
        
    }


    public void setClassCode(String classCode) {
        tableUtils.clickCellInTableByRowAndColumnName(table_Exposures, 1, "Class Code");
        
        GenericWorkorderBuildings classCodes = new GenericWorkorderBuildings(driver);
        classCodes.selectFirstBuildingCodeResultClassCode(classCode);
        
    }

    public String getExposureEffectiveDate(String exposueDescription) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_Exposures, tableUtils.getRowNumberInTableByText(table_Exposures, exposueDescription), "Effective Date");
    }

    public String getExposureExpirationDate(String exposureDescription) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_Exposures, tableUtils.getRowNumberInTableByText(table_Exposures, exposureDescription), "Expiration Date");
    }

    public String getExposureLocation(String exposureDescription) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_Exposures, tableUtils.getRowNumberInTableByText(table_Exposures, exposureDescription), "Location");
    }

    public String getExposureClassCode(String exposureDescription) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_Exposures, tableUtils.getRowNumberInTableByText(table_Exposures, exposureDescription), "Class Code");
    }

    public String getExposureDescription(String classCode) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_Exposures, tableUtils.getRowNumberInTableByText(table_Exposures, classCode), "Description");
    }

    public String getExposureBasis(String exposureDescription) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_Exposures, tableUtils.getRowNumberInTableByText(table_Exposures, exposureDescription), "Basis");
    }

    public String getExposureBasisType(String exposureDescription) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_Exposures, tableUtils.getRowNumberInTableByText(table_Exposures, exposureDescription), "Basis Type");
    }

    /**
     * @param exposure
     * @author jlarsen
     * @Description - Set all elements for adding an Exposure to the Exposures Table. Assumes Add button has already been clicked.
     * @DATE - Jun 24, 2016
     */

    public void addExposure(CPPGeneralLiabilityExposures exposure) {

        setExposureLocation(exposure.getLocation());
        // click class code field

        tableUtils.clickCellInTableByRowAndColumnName(table_Exposures, tableUtils.getRowCount(table_Exposures), "Class Code");
        
        // select class code
        GenericWorkorderBuildings classCode = new GenericWorkorderBuildings(driver);
        systemOut(exposure.getClassCode());
        classCode.selectFirstBuildingCodeResultClassCode(exposure.getClassCode());
        

        // get description
        //ADD CHECK FOR MULTIPLE PAGES!!!!!!!!!!!!


        int newRowNumber = tableUtils.getRowNumberInTableByText(table_Exposures, exposure.getClassCode());
        String desc = tableUtils.getCellTextInTableByRowAndColumnName(table_Exposures, newRowNumber, "Description");
        exposure.setDescription(desc);

        newRowNumber = tableUtils.getRowNumberInTableByText(table_Exposures, exposure.getClassCode());
        tableUtils.clickCellInTableByRowAndColumnName(table_Exposures, newRowNumber, "Basis");
        tableUtils.setValueForCellInsideTable(table_Exposures, "BasisAmount", String.valueOf(exposure.getBasis()));

        
    }


    public void setExposureLocation(PolicyLocation location) {
        
        tableUtils.selectValueForSelectInTable(table_Exposures, tableUtils.getRowCount(table_Exposures), "Location", location.getAddress().getLine1());
        
    }


    /**
     * @param policy
     * @author jlarsen
     * @Description - Adds all Exposures in Policy List and set Basic required questions. Assumes you are on the General Liability Exposures Tab
     */
    public void fillOutGeneralLiabilityExposures(GeneratePolicy policy) {

        clickExposureDetialsTab();

        if (new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
            for (CPPGeneralLiabilityExposures exposure : policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {
                clickAdd();
                
                addExposure(exposure);
            }
        }


        clickLocationSpecificQuestionsTab();
        List<WebElement> locationList = finds(By.xpath("//span[contains(text(), 'Location')]/ancestor::div[5]/following-sibling::div/div/table/tbody/child::tr"));
        List<String> locationStringList = new ArrayList<String>();
        for (WebElement locationItem : locationList) {
            locationStringList.add(locationItem.findElement(By.xpath(".//td/div")).getText());
        }

        for (String addressString : locationStringList) {
            //click row containing that address
            clickWhenClickable(find(By.xpath("//div[contains(text(), '" + addressString + "')]")));
            for (CPPGeneralLiabilityExposures exposure : policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {

                if (addressString.contains(exposure.getLocation().getFullAddressString())) {

                    if (new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
                        fillOutBasicUWQuestionsQQ(exposure, policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
                    } else {
                        fillOutBasicUWQuestionsQQ(exposure, policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
                        fillOutBasicUWQuestionsFullApp(exposure, policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
                    }//END ELSE IF
                }//END IF
                if (true) {
                    handleSpecialCaseQuestions(exposure);
                }
            }//END FOR
        }//END FOR

        
        clickUnderwritingQuestionsTab();
        //FILL OUT UW QUESTIONS

        if (policy.commercialPackage.packageRisk.equals(PackageRiskType.Contractor)) {
            if (!policy.generalLiabilityCPP.isContractorQuestionsFilledOut()) {
                fillOutUnderwritingContractorQuestions(policy);
                policy.generalLiabilityCPP.setContractorQuestionsFilledOut(true);
            }
        }
    }//END fillOutGeneralLiabilityExposures


    //set special case questions
    private void handleSpecialCaseQuestions(CPPGeneralLiabilityExposures exposure) {
        GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability uwQuestions = new GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability(driver);
        switch (exposure.getClassCode()) {
            case "97050":
                for (CPPGLExposureUWQuestions question : exposure.getUnderWritingQuestions()) {
                    if (question.getQuestionText().contains("What are the spray receipts")) {
                        uwQuestions.setUnderwritingQuestion(exposure, question, "100");
                    }
                }
                break;
            default:
                break;
        }
    }


    Guidewire8RadioButton radio_AreWrittenContractsObtainedBySubContractors() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Are written contracts obtained from all subcontractors requiring hold harmless agreements in your favor?')]/parent::td/following-sibling::td/div/table[contains(@class, 'radio-group')]");
    }


    public void setAreYouNamedAsAnAdditionalInsuredOnSubContracts(boolean yesno) {
        radio_AreYouNamedAsAdditionalInsured().select(yesno);
    }

    Guidewire8RadioButton radio_AreYouNamedAsAdditionalInsured() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Are you named as an Additional Insured on all subcontractors policies?')]/parent::td/following-sibling::td/div/table[contains(@class, 'radio-group')]");
    }


    public void setWrittenContractsObtainedFromSubContractors(boolean yesno) {
        radio_AreWrittenContractsObtainedBySubContractors().select(yesno);
    }

    /**
     * @param exposure
     * @author jlarsen
     * @Description - Fills our all Quick Quote questions for passed in Exposure. Will set parent questions as required to display chilc questions.
     */

    public void fillOutUnderwritingQuestionsQQ(CPPGeneralLiabilityExposures exposure, List<CPPGeneralLiabilityExposures> policyExposureList) {
        isOnPage();
        GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability glUWQuestions = new GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability(driver);
        if (!exposure.getUnderWritingQuestions().isEmpty()) {//if there are no questions skip all
            for (CPPGLExposureUWQuestions question : exposure.getUnderWritingQuestions()) {
                if (question.getRequiredAt().equals(GeneratePolicyType.QuickQuote)) {//is quesitons required at QQ
                    glUWQuestions.setUnderwritingQuestion(exposure, question);
                    setChildQuestionsQQ(exposure, question.getChildrenQuestions());
                }
            }
        }
    }


    private void setChildQuestionsQQ(CPPGeneralLiabilityExposures exposure, List<CPPGLExposureUWQuestions> childQuestions) {
        GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability glUWQuestions = new GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability(driver);
        for (CPPGLExposureUWQuestions childQuestion : childQuestions) {
            if (childQuestion.getRequiredAt().equals(GeneratePolicyType.QuickQuote)) {
                glUWQuestions.setUnderwritingQuestion(exposure, childQuestion);
                if (childQuestion.getChildrenQuestions() != null && !childQuestion.getChildrenQuestions().isEmpty()) {
                    setChildQuestionsQQ(exposure, childQuestion.getChildrenQuestions());
                }
            }
        }
    }

	/*private void setChildQuestions(CPPGeneralLiabilityExposures exposure, List<CPPGLExposureUWQuestions> childQuestions) {
		GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability glUWQuestions = new GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability();
		for(CPPGLExposureUWQuestions childQuestion : childQuestions) {
			glUWQuestions.setUnderwritingQuestion(exposure, childQuestion);
			if(childQuestion.getChildrenQuestions() != null && !childQuestion.getChildrenQuestions().isEmpty()) {
				setChildQuestions(exposure, childQuestion.getChildrenQuestions());
			}
		}
	}*/


    /**
     * @param exposure
     * @author jlarsen
     * @Description - Fills out only required Questions.
     */
    public void fillOutBasicUWQuestionsQQ(CPPGeneralLiabilityExposures exposure, List<CPPGeneralLiabilityExposures> policyExposureList) {
        isOnPage();
        GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability glUWQuestions = new GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability(driver);
        for (CPPGLExposureUWQuestions question : exposure.getUnderWritingQuestions()) {
            if (question.getRequiredAt().equals(GeneratePolicyType.QuickQuote) || question.getFormatType().equals(FormatType.BooleanCheckbox)) {
                glUWQuestions.setUnderwritingQuestion(exposure, question);
                for (CPPGLExposureUWQuestions childQuestion : question.getChildrenQuestions()) {
                    if (childQuestion.getDependentOnAnswer().equals(question.getCorrectAnswer())) {
//						if(childQuestion.getFormatType().equals(FormatType.BooleanCheckbox)) {
                        glUWQuestions.setUnderwritingQuestion(exposure, childQuestion);
//						}
                    }
                }
            }//END IF required at
        }//end FOR
    }


    /**
     * @param exposure
     * @author jlarsen
     * @Description - Fills our all questions for passed in Exposure. Will set parent questions as required to display chilc questions.
     */
    public void fillOutUnderwritingQuestionsFULLAPP(CPPGeneralLiabilityExposures exposure, List<CPPGeneralLiabilityExposures> policyExposureList) {
        GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability glUWQuestions = new GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability(driver);
        for (CPPGLExposureUWQuestions question : exposure.getUnderWritingQuestions()) {
            glUWQuestions.setUnderwritingQuestion(exposure, question);
            for (CPPGLExposureUWQuestions childQuestion : question.getChildrenQuestions()) {
                glUWQuestions.setUnderwritingQuestion(exposure, childQuestion);
            }
        }
    }

    /**
     * @param exposure
     * @author jlarsen
     * @Description - Fills out every question for exposure Full App. Will set parent question needed to display all child questions.
     */
    public void fillOutBasicUWQuestionsFullApp(CPPGeneralLiabilityExposures exposure, List<CPPGeneralLiabilityExposures> policyExposureList) {
        GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability glUWQuestions = new GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability(driver);
        if (!exposure.getUnderWritingQuestions().isEmpty()) {
            for (CPPGLExposureUWQuestions question : exposure.getUnderWritingQuestions()) {
                if (!question.getRequiredAt().equals(GeneratePolicyType.QuickQuote)) {
                    glUWQuestions.setUnderwritingQuestion(exposure, question);
                }
            }
        }
    }


    // GENERAL CONTRACTOR QUESTIONS

    public void setDoesApplicantMoveBuildings(boolean yesno) {
        new Guidewire8RadioButton(driver, "//div[contains(text(), 'Does applicant/insured move buildings or structures?')]/parent::td/following-sibling::td/div/table").select(yesno);
    }

    @FindBy(xpath = "//div[contains(text(), 'How many dwellings/buildings were built this year?')]/parent::td/following-sibling::td/div")
    private WebElement editbox_NumberOfBuildingsBuiltThisYear;

    @FindBy(xpath = "//input[contains(@name, 'c2')]")
    private WebElement textbox_NumberOfBuildingsBuiltThisYear;


    public void setNumberOfBuildingsBuiltThisYear(int numberBuildingsBuiltThisYear) {
        clickWhenClickable(editbox_NumberOfBuildingsBuiltThisYear);
        
        textbox_NumberOfBuildingsBuiltThisYear.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textbox_NumberOfBuildingsBuiltThisYear.sendKeys(String.valueOf(numberBuildingsBuiltThisYear));
        find(By.xpath("//div[contains(text(), 'How many dwellings/buildings were built this year?')]")).click();
    }

    @FindBy(xpath = "//div[contains(text(), 'How many dwellings/buildings were built last year?')]/parent::td/following-sibling::td/div")
    private WebElement editbox_NumberOfBuildingsBuiltLastYear;

    @FindBy(xpath = "//input[contains(@name, 'c2')]")
    private WebElement textbox_NumberOfBuildingsBuiltLastYear;


    public void setNumberOfBuildingsBuiltLastYear(int numberBuildingsBuiltLastYear) {
        clickWhenClickable(editbox_NumberOfBuildingsBuiltLastYear);
        
        textbox_NumberOfBuildingsBuiltLastYear.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        textbox_NumberOfBuildingsBuiltLastYear.sendKeys(String.valueOf(numberBuildingsBuiltLastYear));
        find(By.xpath("//div[contains(text(), 'How many dwellings/buildings were built last year?')]")).click();
    }
    //END GENERAL CONTRACTOR QUESTIONS


    //GENERAL CONTRACTOR PERCENTAGE METHODS

    @FindBy(xpath = "//label[contains(text(), 'General Contractor')]/parent::td/following-sibling::td/input")
    private WebElement edixbox_GeneralContractorPercent;


    public void setGeneralContractorPercent(int percent) {
        if (percent != 0) {
            setText(edixbox_GeneralContractorPercent, String.valueOf(percent));
            
            clickWhenClickable(find(By.xpath("//label[contains(text(), 'Developer')]")));
            
        }
    }

    @FindBy(xpath = "//label[contains(text(), 'Subcontractor')]/parent::td/following-sibling::td/input")
    private WebElement editbox_SubcontractorPercent;


    public void setSubcontractorPercent(int percent) {
        if (percent != 0) {
            setText(editbox_SubcontractorPercent, String.valueOf(percent));
            
            clickWhenClickable(find(By.xpath("//label[contains(text(), 'General Contractor')]")));
            
        }
    }

    @FindBy(xpath = "//label[contains(text(), 'Developer')]/parent::td/following-sibling::td/input")
    private WebElement editbox_DeveloperPercent;


    public void setDeveloperPercent(int percent) {
        if (percent != 0) {
            setText(editbox_DeveloperPercent, String.valueOf(percent));
            
            clickWhenClickable(find(By.xpath("//label[contains(text(), 'General Contractor')]")));
            
        }
    }

    @FindBy(xpath = "//label[contains(text(), 'Owner/Builder')]/parent::td/following-sibling::td/input")
    private WebElement editbox_OwnerBuilderPercent;


    public void setOwnerBuilderPercent(int percent) {
        if (percent != 0) {
            setText(editbox_OwnerBuilderPercent, String.valueOf(percent));
            
            clickWhenClickable(find(By.xpath("//label[contains(text(), 'General Contractor')]")));
            
        }
    }

    @FindBy(xpath = "//div[contains(@id, ':StateInfoPanelSet:StateINfoLV-body')]/div/table")
    private WebElement table_PercentWorkDoneByState;

    //jlarsen 3/12/2016
    //tableUtils doesn't work for this table because tableUtils is looking for text in a span where these are in div's

    public void setWorkDoneInIdaho(int percent) {
        if (percent > 0) {
            clickWhenClickable(table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'ID')]/parent::td/parent::tr/following-sibling::tr/child::td[1]/div")));
//			table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'ID')]/parent::td/parent::tr/following-sibling::tr/child::td[1]/div")).click();
            
            find(By.xpath("//input[contains(@name, 'c0')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
            find(By.xpath("//input[contains(@name, 'c0')]")).sendKeys(String.valueOf(percent));
//			table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'ID')]")).click();
            clickProductLogo();
            
        }
    }


    public void setWorkDoneInOregon(int percent) {
        if (percent > 0) {
            clickWhenClickable(table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'OR')]/parent::td/parent::tr/following-sibling::tr/child::td[2]/div")));
//			table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'OR')]/parent::td/parent::tr/following-sibling::tr/child::td[2]/div")).click();
            
            find(By.xpath("//input[contains(@name, 'c1')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
            find(By.xpath("//input[contains(@name, 'c1')]")).sendKeys(String.valueOf(percent));
//			table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'OR')]")).click();
            clickProductLogo();
            
        }
    }


    public void setWorkDoneInUtah(int percent) {
        if (percent > 0) {
            clickWhenClickable(table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'UT')]/parent::td/parent::tr/following-sibling::tr/child::td[3]/div")));
//			table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'UT')]/parent::td/parent::tr/following-sibling::tr/child::td[3]/div")).click();
            
            find(By.xpath("//input[contains(@name, 'c2')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
            find(By.xpath("//input[contains(@name, 'c2')]")).sendKeys(String.valueOf(percent));
//			table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'UT')]")).click();
            clickProductLogo();
            
        }
    }


    public void setWorkDoneInMontana(int percent) {
        if (percent > 0) {
            clickWhenClickable(table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'MT')]/parent::td/parent::tr/following-sibling::tr/child::td[4]/div")));
//			table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'MT')]/parent::td/parent::tr/following-sibling::tr/child::td[4]/div")).click();
            
            find(By.xpath("//input[contains(@name, 'c3')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
            find(By.xpath("//input[contains(@name, 'c3')]")).sendKeys(String.valueOf(percent));
//			table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'MT')]")).click();
            clickProductLogo();
            
        }
    }


    public void setWorkDoneInWashington(int percent) {
        if (percent > 0) {
            clickWhenClickable(table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'WA')]/parent::td/parent::tr/following-sibling::tr/child::td[5]/div")));
//			table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'WA')]/parent::td/parent::tr/following-sibling::tr/child::td[5]/div")).click();
            
            find(By.xpath("//input[contains(@name, 'c4')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
            find(By.xpath("//input[contains(@name, 'c4')]")).sendKeys(String.valueOf(percent));
//			table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'WA')]")).click();
            clickProductLogo();
            
        }
    }


    public void setWorkDoneInWyoming(int percent) {
        if (percent > 0) {
            clickWhenClickable(table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'WY')]/parent::td/parent::tr/following-sibling::tr/child::td[6]/div")));
//			table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'WY')]/parent::td/parent::tr/following-sibling::tr/child::td[6]/div")).click();
            
            find(By.xpath("//input[contains(@name, 'c5')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
            find(By.xpath("//input[contains(@name, 'c5')]")).sendKeys(String.valueOf(percent));
//			table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'WY')]")).click();
            clickProductLogo();
            
        }
    }


    public void setWorkDoneInOther(int percent) {
        if (percent > 0) {
            clickWhenClickable(table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'All Other')]/parent::td/parent::tr/following-sibling::tr/child::td[7]/div")));
//			table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'All Other')]/parent::td/parent::tr/following-sibling::tr/child::td[7]/div")).click();
            
            find(By.xpath("//input[contains(@name, 'c6')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
            find(By.xpath("//input[contains(@name, 'c6')]")).sendKeys(String.valueOf(percent));
//			table_PercentWorkDoneByState.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), 'All Other')]")).click();
            clickProductLogo();
            
        }
    }

    @FindBy(xpath = "//input[contains(@id, ':GrossAnnualReceipt-inputEl')]")
    private WebElement editbox_GrossAnnualReceipts;


    public void setGrossAnnualreceipts(int receipts) {
        setText(editbox_GrossAnnualReceipts, String.valueOf(receipts));
    }


    //RESIDENTIAL CONSTRUCTION
    @FindBy(xpath = "//input[contains(@id, ':ConTypeResidential-inputEl')]")
    private WebElement editbox_ResidentialConstructionPercent;


    public boolean setResidentialConstructionPercent(int percent) {
        if (percent > 0) {
            setText(editbox_ResidentialConstructionPercent, String.valueOf(percent));
            
            clickWhenClickable(find(By.xpath("//label[contains(text(), 'Commercial Construction')]")));
            
            return true;
        }
        return false;
    }


    @FindBy(xpath = "//input[contains(@id, ':ConTypeResidentialNew-inputEl')]")
    private WebElement editbox_ResidentialConstructionPercentNew;


    public void setResidentialConstructionPercent_New(int percent) {
        if (percent > 0) {
            setText(editbox_ResidentialConstructionPercentNew, String.valueOf(percent));
            
            clickWhenClickable(find(By.xpath("//label[contains(text(), 'Residential Construction')]")));
            
        }
    }

    @FindBy(xpath = "//input[contains(@id, ':ConTypeResRemodel-inputEl')]")
    private WebElement editbox_ResidentialConstructionPercentRemodel;


    public void setResidentialConstructionPercent_Remodel(int percent) {
        if (percent > 0) {
            setText(editbox_ResidentialConstructionPercentRemodel, String.valueOf(percent));
            
            clickWhenClickable(find(By.xpath("//label[contains(text(), 'Residential Construction')]")));
            
        }
    }
    //END RESIDENTAIL CONSTRUCTION


    //COMMERCIAL CONSTRUCTION

    @FindBy(xpath = "//input[contains(@id, ':ResidentialConstructionType-inputEl')]")
    private WebElement editbox_CommercialConstructionPercent;


    public boolean setCommercialConstructionPercent(int percent) {
        if (percent > 0) {
            setText(editbox_CommercialConstructionPercent, String.valueOf(percent));
            
            clickWhenClickable(find(By.xpath("//label[contains(text(), 'Residential Construction')]")));
            
            return true;
        }
        return false;
    }

    @FindBy(xpath = "//input[contains(@id, ':ConTypeComNew-inputEl')]")
    private WebElement editbox_CommercialConstructionPercentNew;


    public void setCommercialConstructionPercent_New(int percent) {
        if (percent > 0) {
            setText(editbox_CommercialConstructionPercentNew, String.valueOf(percent));
            
            clickWhenClickable(find(By.xpath("//label[contains(text(), 'Residential Construction')]")));
            
        }
    }

    @FindBy(xpath = "//input[contains(@id, ':ConTypeComRemdol-inputEl')]")
    private WebElement editbox_CommercialConstructionPercentRemodel;


    public void setCommercialConstructionPercent_Remodel(int percent) {
        if (percent > 0) {
            setText(editbox_CommercialConstructionPercentRemodel, String.valueOf(percent));
            
            clickWhenClickable(find(By.xpath("//label[contains(text(), 'Residential Construction')]")));
            
        }
    }

    //END COMMERCIAL CONSTRUCTION

    //GENERAL CONTRACTOR PERCENTAGE METHODS


    //RESIDENTIAL CONSTRUCTION TYPES
    Guidewire8Checkbox checkbox_1_2Family() {
        return new Guidewire8Checkbox(driver, "//label[contains(text(), '1-2 Family')]/parent::td/parent::tr/parent::tbody/parent::table");
    }

    Guidewire8Checkbox checkbox_3_4Family() {
        return new Guidewire8Checkbox(driver, "//label[contains(text(), '3-4 Family')]/parent::td/parent::tr/parent::tbody/parent::table");
    }

    Guidewire8Checkbox checkbox_Apartments() {
        return new Guidewire8Checkbox(driver, "//label[contains(text(), 'Apartments')]/parent::td/parent::tr/parent::tbody/parent::table");
    }

    Guidewire8Checkbox checkbox_Townhouse() {
        return new Guidewire8Checkbox(driver, "//label[contains(text(), 'Townhouse')]/parent::td/parent::tr/parent::tbody/parent::table");
    }

    Guidewire8Checkbox checkbox_Condos() {
        return new Guidewire8Checkbox(driver, "//label[contains(text(), 'Condos')]/parent::td/parent::tr/parent::tbody/parent::table");
    }
    //END RESIDENTAIL CONSTRUCTION TYPES


    //COMMERCIAL CONSTRUCTION TYPES
    Guidewire8Checkbox checkbox_Retail() {
        return new Guidewire8Checkbox(driver, "//label[contains(text(), 'Retail')]/parent::td/parent::tr/parent::tbody/parent::table");
    }

    Guidewire8Checkbox checkbox_Office() {
        return new Guidewire8Checkbox(driver, "//label[contains(text(), 'Office')]/parent::td/parent::tr/parent::tbody/parent::table");
    }

    Guidewire8Checkbox checkbox_MFG() {
        return new Guidewire8Checkbox(driver, "//label[contains(text(), 'MFG')]/parent::td/parent::tr/parent::tbody/parent::table");
    }

    Guidewire8Checkbox checkbox_Warehouse() {
        return new Guidewire8Checkbox(driver, "//label[contains(text(), 'Warehouse')]/parent::td/parent::tr/parent::tbody/parent::table");
    }

    Guidewire8Checkbox checkbox_Other() {
        return new Guidewire8Checkbox(driver, "//label[contains(text(), 'Other')]/parent::td/parent::tr/parent::tbody/parent::table");
    }
    //END COMMERCIAL CONSTRUCTION TYPES


    @FindBy(xpath = "//label[contains(text(), 'Please describe Other')]/parent::td/following-sibling::td/input")
    private WebElement edixbox_OtherDescription;


    //CONSTRUCTION ACTIVITIES

    @FindBy(xpath = "//div[contains(text(), 'Air conditioning service installation')]/ancestor::table")
    private WebElement table_ActivityPercentage;

    @FindBy(xpath = "//input[contains(@name, 'LiabPropDescRight')]")
    private WebElement editbox_ActivityPercentageRight;

    @FindBy(xpath = "//input[contains(@name, 'LiabPropLeft')]")
    private WebElement editbox_ActivityPercentageLeft;


    //jlarsen 3/12/2016
    //table is spit into two columns, custom method to set either side of the column.
    //CURRENTLY RIGHT SIDE NOT CLICKING INITIAL ELEMENT

    public void setPercentageOfActivity(ConstructionActivities activity, int percent) {
        if (activity.getSide().equals("LEFT")) {
            table_ActivityPercentage.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), '" + activity.getName() + "')]/parent::td/following-sibling::td/div")).click();
            editbox_ActivityPercentageLeft.sendKeys(Keys.chord(Keys.CONTROL + "a"));
            
            editbox_ActivityPercentageLeft.sendKeys(String.valueOf(percent));
            table_ActivityPercentage.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), '" + activity.getName() + "')]")).click();
        } else if (activity.getSide().equals("RIGHT")) {
            table_ActivityPercentage.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), '" + activity.getName() + "')]/parent::td/preceding-sibling::td/div")).click();
            editbox_ActivityPercentageRight.sendKeys(Keys.chord(Keys.CONTROL + "a"));
            
            editbox_ActivityPercentageRight.sendKeys(String.valueOf(percent));
            table_ActivityPercentage.findElement(By.xpath(".//tbody/child::tr/child::td/div[contains(text(), '" + activity.getName() + "')]")).click();
        }
    }

    //jlarsen 3/12/2016
    //get activity percentage from object
    private int getExposurePercent(CPPGeneralLiability exposure, ConstructionActivities activity) {

        switch (activity) {
            case AirConditioningServiceInstallation:
                return exposure.getAirConditioningServiceInstallation();
            case Carpentry_Commercial:
                return exposure.getCarpentry_Commercial();
            case CarpetRugUpholsteryCleaning:
                return exposure.getCarpetRugUpholsteryCleaning();
            case ConcreteConstruction:
                return exposure.getConcreteConstruction();
            case DrivewayParkingAreaSidewalkRepairPavingRepair:
                return exposure.getDrivewayParkingAreaSidewalkRepairPavingRepair();
            case ElectricalWorkWithinBuildings:
                return exposure.getElectricalWorkWithinBuildings();
            case FenceErectionContractors:
                return exposure.getFenceErectionContractors();
            case FurnitureOrFixturesInstallationInOffices:
                return exposure.getFurnitureOrFixturesInstallationInOffices();
            case GradingOfLand:
                return exposure.getGradingOfLand();
            case HeatOrHeat_AirConditioningInstallation_service_NOLPG:
                return exposure.getHeatOrHeat_AirConditioningInstallation_service_NOLPG();
            case Insulation_NoFoamOrChemical:
                return exposure.getInsulation_NoFoamOrChemical();
            case Janitorial:
                return exposure.getJanitorial();
            case Masonry:
                return exposure.getMasonry();
            case MetalErection_Structural:
                return exposure.getMetalErection_Structural();
            case PaintingInterior:
                return exposure.getPaintingInterior();
            case PlasteringOrStuccoWork_No_EFIS:
                return exposure.getPlasteringOrStuccoWork_No_EFIS();
            case PlumbingResidential:
                return exposure.getPlumbingResidential();
            case RoofingResidential_NoRe_Roofing:
                return exposure.getRoofingResidential_NoRe_Roofing();
            case SignPaintingOrLettering_Inside_NoSpraying:
                return exposure.getSignPaintingOrLettering_Inside_NoSpraying();
            case SupervisorOnly:
                return exposure.getSupervisorOnly();
            case WindowCleaning_3StoriesOrLess:
                return exposure.getWindowCleaning_3StoriesOrLess();

            case ApplianceInstallation_HouseholdOrCommercial:
                return exposure.getApplianceInstallation_HouseholdOrCommercial();
            case Carpentry_Residential:
                return exposure.getCarpentry_Residential();
            case CeilingOrWallInstallation_Metal:
                return exposure.getCeilingOrWallInstallation_Metal();
            case DoorWindoWorassembledmillworkinstallationmetal:
                return exposure.getDoorWindoWorassembledmillworkinstallationmetal();
            case DrywallOrWallboardinstallation:
                return exposure.getDrywallOrWallboardinstallation();
            case Excavation:
                return exposure.getExcavation();
            case FloorCoveringInstalation_NotTileOrStone:
                return exposure.getFloorCoveringInstalation_NotTileOrStone();
            case GlassDealersandGlaziers_SalesAndInstallation:
                return exposure.getGlassDealersandGlaziers_SalesAndInstallation();
            case GutterInstallation:
                return exposure.getGutterInstallation();
            case HouseFurnishingInstallation:
                return exposure.getHouseFurnishingInstallation();
            case InteriorDecorators:
                return exposure.getInteriorDecorators();
            case LawnSprinklerInstallation:
                return exposure.getLawnSprinklerInstallation();
            case MetalErection_NotStructural:
                return exposure.getMetalErection_NotStructural();
            case Painting_Exterior_LessThan3Stories:
                return exposure.getPainting_Exterior_LessThan3Stories();
            case PaperHanging:
                return exposure.getPaperHanging();
            case Plumbing_Commercial_Notindustrial_NoBoiler:
                return exposure.getPlumbing_Commercial_Notindustrial_NoBoiler();
            case RoofingCommercial_NoRe_Roofing:
                return exposure.getRoofingCommercial_NoRe_Roofing();
            case SidingInstallation_3StoriesOrLess:
                return exposure.getSidingInstallation_3StoriesOrLess();
            case SnowRemoval:
                return exposure.getSnowRemoval();
            case TileStoneMarbleMosaic_Interior:
                return exposure.getTileStoneMarbleMosaic_Interior();
            default:
                return 0;

        }

    }

    //END CONSTRUCTION ACTIVITIES


    public void fillOutUnderwritingContractorQuestions(GeneratePolicy policy) {
        boolean isContractor = false;
        boolean saveDraft = false;

        for (CPPGeneralLiabilityExposures exposure : policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {

            if (exposure.getClassCode().substring(0, 1).equals("9")) {
                switch (exposure.getClassCode()) {
                    case "91111":
                    case "91150":
                    case "91302":
                    case "91405":
                    case "91481":
                    case "91507":
                    case "91523":
                    case "92215":
                    case "94225":
                    case "95505":
                    case "96816":
                    case "97047":
                    case "97050":
                    case "97221":
                    case "97222":
                    case "97223":
                    case "98699":
                    case "98710":
                    case "98993":
                    case "99310":
                    case "99471":
                    case "99975":
                        //these do not have to fill out percent questions
                        break;
                    default:
                        isContractor = true;
                        setDoesApplicantMoveBuildings(false);
                        
                        if (!new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
                            setAreYouNamedAsAnAdditionalInsuredOnSubContracts(policy.generalLiabilityCPP.isAreyouNamedAsAdditionalInsured());
                        }//END IF
                        break;
                }//END SWITCH
            }//END IF


            if (isContractor) {
                setGeneralContractorPercent(policy.generalLiabilityCPP.getGeneralContractor());
                
                setSubcontractorPercent(policy.generalLiabilityCPP.getSubContractor());
                
                setDeveloperPercent(policy.generalLiabilityCPP.getDeveloper());
                
                setOwnerBuilderPercent(policy.generalLiabilityCPP.getOwnerBuilder());
                

                if (policy.generalLiabilityCPP.getGeneralContractor() > 0 || policy.generalLiabilityCPP.getDeveloper() > 0 || policy.generalLiabilityCPP.getOwnerBuilder() > 0) {
                    try {
                        setNumberOfBuildingsBuiltLastYear(policy.generalLiabilityCPP.getNumberBuildingsBuiltLastYear());
                        
                        setNumberOfBuildingsBuiltThisYear(policy.generalLiabilityCPP.getNumberBuildingsBuiltThisYear());
                        
                        setWrittenContractsObtainedFromSubContractors(policy.generalLiabilityCPP.isAreWrittenContractsObtained());
                        
                    } catch (Exception e) {
                        saveDraft = true;
                        systemOut("BUILDING QUESTIONS DID NOT SHOW.");
                    }//END TRY CATCH
                }//END IF

                setWorkDoneInIdaho(policy.generalLiabilityCPP.getIdaho());
                
                setWorkDoneInOregon(policy.generalLiabilityCPP.getOregon());
                
                setWorkDoneInUtah(policy.generalLiabilityCPP.getUtah());
                
                setWorkDoneInMontana(policy.generalLiabilityCPP.getMontana());
                
                setWorkDoneInWashington(policy.generalLiabilityCPP.getWashington());
                
                setWorkDoneInWyoming(policy.generalLiabilityCPP.getWyoming());
                
                setWorkDoneInOther(policy.generalLiabilityCPP.getAllOther());
                

                setGrossAnnualreceipts(policy.generalLiabilityCPP.getGrossAnnualReciepts());
                
                if (setResidentialConstructionPercent(policy.generalLiabilityCPP.getResidentialConstruction())) {
                    
                    setResidentialConstructionPercent_New(policy.generalLiabilityCPP.getResidentialNew());
                    
                    setResidentialConstructionPercent_Remodel(policy.generalLiabilityCPP.getResidentialRemodel());
                    

                    for (ResidentialConstructionType residentialType : policy.generalLiabilityCPP.getResidentialTypes()) {
                        switch (residentialType) {
                            case One_TwoFamily:
                                checkbox_1_2Family().select(true);
                                
                                break;
                            case Three_FourFamily:
                                checkbox_3_4Family().select(true);
                                
                                break;
                            case Apartments:
                                checkbox_Apartments().select(true);

                                break;
                            case Townhouse:
                                checkbox_Townhouse().select(true);
                                
                                break;
                            case Condos:
                                checkbox_Condos().select(true);
                                
                                break;
                        }//END SWITCH
                    }//END FOR
                }//END IF
                
                if (setCommercialConstructionPercent(policy.generalLiabilityCPP.getCommercialConstruction())) {
                    
                    setCommercialConstructionPercent_New(policy.generalLiabilityCPP.getCommercialNew());
                    
                    setCommercialConstructionPercent_Remodel(policy.generalLiabilityCPP.getCommercialRemodel());
                    

                    for (CommercialConstructionType commercialType : policy.generalLiabilityCPP.getCommercialtypes()) {
                        switch (commercialType) {
                            case Retail:
                                checkbox_Retail().select(true);
                                
                                break;
                            case Office:
                                checkbox_Office().select(true);
                                
                                break;
                            case MFG:
                                checkbox_MFG().select(true);
                                
                                break;
                            case Warehouse:
                                checkbox_Warehouse().select(true);
                                
                                break;
                            case Other:
                                checkbox_Other().select(true);
                                
                                setText(edixbox_OtherDescription, policy.generalLiabilityCPP.getCommercialConstructionOtherDescription());
                                break;
                        }//END SWITCH
                    }//END FOR
                }//END IF

                for (ConstructionActivities activity : policy.generalLiabilityCPP.getConstructionActivityList()) {
                    setPercentageOfActivity(activity, getExposurePercent(policy.generalLiabilityCPP, activity));
                    
                }//END FOR
                isContractor = false;
                if (saveDraft) {
                    GenericWorkorder genwo = new GenericWorkorder(driver);
                    genwo.clickGenericWorkorderSaveDraft();
                    
                    setNumberOfBuildingsBuiltLastYear(policy.generalLiabilityCPP.getNumberBuildingsBuiltLastYear());
                    
                    setNumberOfBuildingsBuiltThisYear(policy.generalLiabilityCPP.getNumberBuildingsBuiltThisYear());
                    
                    setWrittenContractsObtainedFromSubContractors(policy.generalLiabilityCPP.isAreWrittenContractsObtained());
                    

                }


            }//END ISCONTRACTOR IF
        }//END MAIN FOR
    }//END fillOutUnderwritingContractorQuestions()


    public void fillOutUnderwritingContractorQuestions(CPPGeneralLiabilityExposures exposure) {
        boolean isContractor = false;
        boolean saveDraft = false;
        //		boolean noContractorUWQuestions = GLClassCodeHelper.getGLContractorWithNoUWQuestions();


        if (exposure.getClassCode().substring(0, 1).equals("9")) {
            switch (exposure.getClassCode()) {
                case "91111":
                case "91150":
                case "91302":
                case "91405":
                case "91481":
                case "91507":
                case "91523":
                case "92215":
                case "94225":
                case "95505":
                case "96816":
                case "97047":
                case "97050":
                case "97221":
                case "97222":
                case "97223":
                case "98699":
                case "98710":
                case "98993":
                case "99310":
                case "99471":
                case "99975":
                    //these do not have to fill out percent questions
                    break;
                default:
                    isContractor = true;
                    setDoesApplicantMoveBuildings(false);
                    
                    setAreYouNamedAsAnAdditionalInsuredOnSubContracts(true);
                    break;
            }//END SWITCH
        }//END IF


        if (isContractor) {
            setGeneralContractorPercent(50);
            
            setSubcontractorPercent(50);
            
            setDeveloperPercent(0);
            
            setOwnerBuilderPercent(0);
            

            try {
                setNumberOfBuildingsBuiltLastYear(10);
                
                setNumberOfBuildingsBuiltThisYear(10);
                
                setWrittenContractsObtainedFromSubContractors(true);
                
            } catch (Exception e) {
                saveDraft = true;
                systemOut("BUILDING QUESTIONS DID NOT SHOW.");
            }//END TRY CATCH

            setWorkDoneInIdaho(25);
            
            setWorkDoneInOregon(25);
            
            setWorkDoneInUtah(25);
            
            setWorkDoneInMontana(25);
            
            setWorkDoneInWashington(0);
            
            setWorkDoneInWyoming(0);
            
            setWorkDoneInOther(0);
            

            setGrossAnnualreceipts(100);
            
            if (setResidentialConstructionPercent(50)) {
                
                setResidentialConstructionPercent_New(50);
                
                setResidentialConstructionPercent_Remodel(50);
                

                checkbox_1_2Family().select(true);
                
            }//END IF
            
            if (setCommercialConstructionPercent(50)) {
                
                setCommercialConstructionPercent_New(50);
                
                setCommercialConstructionPercent_Remodel(50);
                

                checkbox_Retail().select(true);
                
            }//END IF

            setPercentageOfActivity(ConstructionActivities.AirConditioningServiceInstallation, 100);
            

            isContractor = false;
            if (saveDraft) {
                GenericWorkorder genwo = new GenericWorkorder(driver);
                genwo.clickGenericWorkorderSaveDraft();
                
                setNumberOfBuildingsBuiltLastYear(10);
                
                setNumberOfBuildingsBuiltThisYear(10);
                
                setWrittenContractsObtainedFromSubContractors(true);
                

            }
        }//END ISCONTRACTOR IF
    }//END fillOutUnderwritingContractorQuestions()


    public List<String> getInvalidQuestions() {
        List<WebElement> invalidClasses = finds(By.xpath("//*[contains(@class, 'invalid-cell')]"));
        List<String> returnCodes = new ArrayList<String>();
        if (invalidClasses.isEmpty()) {
            return null;
        } else {
            for (WebElement error : invalidClasses) {
                List<WebElement> classCode = error.findElements(By.xpath(".//ancestor::tr[2]/preceding-sibling::tr/descendant::table[2]/tbody/tr/child::td[2]/div/table/tbody/tr/td/label"));
                boolean found = false;
                if (!classCode.isEmpty()) {
                    for (String code : returnCodes) {
                        if (code.equals(classCode.get(classCode.size() - 1).getText().replaceAll("[\\D]", ""))) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        returnCodes.add(classCode.get(classCode.size() - 1).getText().replaceAll("[\\D]", ""));
                        found = false;
                    }
                }
            }
        }
        return returnCodes;
    }


//    private boolean isOnExposuresWizardStep() {
//        return !finds(By.xpath("//span[contains(@id, ':GeneralLiabilityEUScreen:ttlBar') and (text()='Exposures')]")).isEmpty();
//    }

}
















