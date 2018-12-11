package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.Building.ConstructionTypeCPP;
import repository.gw.enums.CommercialProperty.BuildingCoverageCauseOfLoss;
import repository.gw.enums.CommercialProperty.BuildingCoverageValuationMethod;
import repository.gw.enums.CommercialProperty.PropertyCoverages;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.generate.custom.CPPCPClassCodeUWQuestions;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.gw.helpers.TableUtils;

import java.util.List;


public class GenericWorkorderCommercialPropertyPropertyCPP_UnderwritingQuestions extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;

    public GenericWorkorderCommercialPropertyPropertyCPP_UnderwritingQuestions(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    GenericWorkorderCommercialPropertyPropertyCPP commercialProperty = new GenericWorkorderCommercialPropertyPropertyCPP(driver);


    private boolean setUWQuestion(String question, boolean yesno) {
        WebElement generalQuestionsTable = find(By.xpath("//label[contains(text(), 'General Questions')]/ancestor::table"));

        tableUtils.setRadioValueForCellInsideTable(generalQuestionsTable, null, question, yesno);
        return yesno;
    }

    //SOME INSTANCES THERE ARE MORE THAN ONE INPUT/TEXTAREA IN THE DOB
    private void setUWQuestion(String question, String text) {
        clickWhenClickable(find(By.xpath("//div[contains(text(), '" + question + "')]/parent::td/following-sibling::td/descendant::div")));
        List<WebElement> boxesinDOM = finds(By.xpath("//textarea[@name='c2'] | //input[@name='c2']"));
        for (WebElement availableBox : boxesinDOM) {
            if (availableBox.isDisplayed()) {
                availableBox.sendKeys(Keys.chord(Keys.CONTROL + "a"));
                availableBox.sendKeys(Keys.chord(text));
                clickWhenClickable(find(By.xpath("//div[contains(text(), '" + question + "')]")));
                break;
            }
        }
    }

    public void fillOutBasicClassCodeUWQuestionQQ(CPPCommercialProperty_Building building) {
        GenericWorkorderCommercialUnderwritingQuestion_CommercialProperty uwQuestionsPage = new GenericWorkorderCommercialUnderwritingQuestion_CommercialProperty(getDriver());
        for (CPPCPClassCodeUWQuestions question : building.getUwQuestionList()) {
            if (question.getRequiredAt().equals(GeneratePolicyType.QuickQuote)) {
                if (!question.isDependantQuestion()) {
                    uwQuestionsPage.setUnderwritingQuestion(question);
                } else {
                    if (question.getDependentOnQuestion().getRequiredAt().equals(GeneratePolicyType.QuickQuote)) {
                        if (question.getDependentOnQuestion().getCorrectAnswer().equals(question.getDependentOnAnswer())) {
                            uwQuestionsPage.setUnderwritingQuestion(question);
                        }
                    }
                }
            }
        }//end for
    }


    public void fillOutBasicClassCodeUWQuestionFA(CPPCommercialProperty_Building building) {
        GenericWorkorderCommercialUnderwritingQuestion_CommercialProperty uwQuestionsPage = new GenericWorkorderCommercialUnderwritingQuestion_CommercialProperty(getDriver());
        for (CPPCPClassCodeUWQuestions question : building.getUwQuestionList()) {
            //jlarsen 4/26/2017
            //if the generate is testing generated UW Issues from class code questions this is overriding the answer to generate such issues.
            if (building.isGenerateUWIssuesFromQuestions()) {
                if (question.getUwIssueType() == null) {
                    uwQuestionsPage.setUnderwritingQuestion(question);
                } else {
                    uwQuestionsPage.setUnderwritingQuestion(question, getOverrideAnswer(question));
                }
                continue;
            }//end if

            //fill out questions normally
            if (!question.isDependantQuestion()) {
                uwQuestionsPage.setUnderwritingQuestion(question);
            } else {
                if (question.getDependentOnQuestion().getCorrectAnswer().equals(question.getDependentOnAnswer())) {
                    uwQuestionsPage.setUnderwritingQuestion(question);
                }
            }
        }//end for
    }//end fillOutBasicClassCodeUWQuestionFA(CPPCommercialProperty_Building building)


    public CPPCPClassCodeUWQuestions fillOutClassCodeUWQuestions(CPPCommercialProperty_Building building) {
        for (CPPCPClassCodeUWQuestions question : building.getUwQuestionList()) {
            try {
                GenericWorkorderCommercialUnderwritingQuestion_CommercialProperty uwQuestionsPage = new GenericWorkorderCommercialUnderwritingQuestion_CommercialProperty(getDriver());
                uwQuestionsPage.setUnderwritingQuestion(question);
            } catch (Exception e) {
                e.printStackTrace();
                return question;
            }
        }//end for
        return null;
    }//end fillOutClassCodeUWQuestions(CPPCommercialProperty_Building building)


    private String getOverrideAnswer(CPPCPClassCodeUWQuestions question) {
        switch (question.getQuestionsType()) {
            case Boolean:
                return (question.getCorrectAnswer().equals("Yes")) ? "No" : "Yes";
            case Choice:
                int indexOf = question.getChoiceOptions().indexOf(question.getCorrectAnswer());
                return (indexOf == 0) ? question.getChoiceOptions().get(1) : question.getChoiceOptions().get(0);
            case Date:
                return "1/1/1990";
            case Integer:
                return "1001";
            case OR:
                return (question.getCorrectAnswer().equals("Checked")) ? "Not Checked" : "Checked";
            case String:
                return "Foo For Brett";
        }//end switch
        return null;
    }

    public void fillOutPropertyUnderwriterQuestions(CPPCommercialProperty_Building building) {
        fillOutPropertyUnderwriterQuestionsQQ(building);

    }

    public void fillOutPropertyUnderwriterQuestionsQQ(CPPCommercialProperty_Building building) {
        commercialProperty.clickUnderwritingQuestionsTab();

        setUWQuestion("Is the applicant/insured the building owner or tenant?", building.getIsTheApplicantTheBuildingOwnerOfTennant());


        if (building.getAdditionalCoverages().isAdditionalBuildingProperty_CP_14_15()) {
            setUWQuestion("Is the property to be insured fixtures, machinery or equipment permanently installed", false);
        }

//		� Available when "Business Income � Landlord As Additional Insured (Rental Value) CP 15 03" or "Unscheduled Building Property Tenant's Policy CP 14 02" is selected.

        if ((building.getAdditionalCoverages().isBusinessIncome_LandlordAsAdditionalInsuredRentalValue_CP_15_03() || building.getAdditionalCoverages().isUnscheduledBuildingPropertyTenantsPolicy_CP_14_02()) && !building.getCoverages().isBuildingCoverage()) {
            setUWQuestion("Is the building owned by the insured", false);
        }

        if (building.getCoverages().isBuildingCoverage()) {
            if (building.getCoverages().getBuildingCoverage_ValuationMethod().equals(BuildingCoverageValuationMethod.ReplacementCost)) {
                if (building.getCoverages().getBuildingCoverage_CauseOfLoss().equals(BuildingCoverageCauseOfLoss.Broad) || building.getCoverages().getBuildingCoverage_CauseOfLoss().equals(BuildingCoverageCauseOfLoss.Special)) {
                    setUWQuestion("Is there a hot water heater", false);
                }
            }
        }

        if (building.getYearBuilt() < 2000) {
            setUWQuestion("Does the building have any asbestos present in the structure", false);
        }

        if (building.getCoverages().getBuildingCoverageList().contains(PropertyCoverages.BuildingCoverage)) {
            if (building.getCoverages().getBuildingCoverage_ValuationMethod().equals(BuildingCoverageValuationMethod.ReplacementCost)) {
                setUWQuestion("Is the building designated as a National Historic building", false);
            }
        }

        if (building.getCoverages().isBuildingCoverage() || building.getCoverages().isBusinessPersonalPropertyCoverage() || building.getCoverages().isPropertyInTheOpen()) {
            if (building.getProtectionClassCode().equals(ProtectionClassCode.Prot9) || building.getProtectionClassCode().equals(ProtectionClassCode.Prot10) || building.getAutoProtecitonClass().equals(ProtectionClassCode.Prot9) || building.getAutoProtecitonClass().equals(ProtectionClassCode.Prot10)) {
                setUWQuestion("Does the property have a wood burning stove", false);
            }
        }


        if (building.getCoverages().getBuildingCoverageList().contains(PropertyCoverages.BuildingCoverage)) {
            setUWQuestion("Does the building have any existing damage", building.isExistingDamage());
            if (building.isExistingDamage()) {
                setUWQuestion("Please describe the damage", building.getExistingDamageDescription());
            }
        }

//		if(building.getCoverages().isBuildersRiskCoverageForm_CP_00_20()) {
//			setUWQuestion("Date construction was started", "1/1/2017");
//			setUWQuestion("Anticipated completion date", "1/1/2018");
//			
//			//has buildng framed or above ground work been started
//			setUWQuestion("Has building been framed or above ground work been started?", false);
//		}

        if (building.getCoverages().isBuildingCoverage()) {
            setUWQuestion("Who is the building deeded to?", "Some Guy Named Carl");
        }

        if (building.getConstructionTypeCPP().equals(ConstructionTypeCPP.ManufacturedMobileHome)) {
            setUWQuestion("Does mobile home have a wood burning stove?", false);
        }
        //jlarsen TEMP WORK AROUND AND I HATE MYSELF FOR IT
        try {
            setUWQuestion("Does mobile home have a wood burning stove", false);
        } catch (Exception e) {
        }

    }


    public void fillOutPropertyUnderwriterQuestionsFA(CPPCommercialProperty_Building building) {
        commercialProperty.clickUnderwritingQuestionsTab();

        setUWQuestion("Is there insured property within 100ft", false);
//		Policy holder name	QQ	-
//		Policy number	QQ	-

//		Is there any exposure to flammables, chemicals, etc.?	FA	-
        setUWQuestion("Is there any exposure to flammables, chemicals, etc.", false);
//		Does the building have a foundation?	FA	• Available when "Building Coverage" is selected.
        if (building.getCoverages().isBuildingCoverage()) {
            setUWQuestion("Does the building have a foundation", true);
            clickProductLogo();
//			What kind of foundation does the building have?	QQ	-
            setUWQuestion("What kind of foundation does the building have?", "Concrete");
        }
        setUWQuestion("Exterior House Keeping and Maintenance", building.getExteriorHousekeepingMaint().getValue());
        setUWQuestion("Interior House Keeping and Maintenance", building.getInteriorHousekeepingMaint().getValue());


//		Please describe other.	QQ	-
//		Who is the building deeded to?	FA	-
//		Does Stock include secondhand inventory?	FA	 Available when "Business Personal Property Coverage" is selected and term "Is stock included?" is answered Yes.
//		Has the Association leveled assessments in the past 3 years?	FA	 Available when Condominium Commercial Unit-Owners Optional Coverages CP 04 18 is selected and the Term Miscellaneous Real Property Limit has a limit over $50,000.
//		Under what circumstance and what was the amount?	FA	-
//		Does applicant/insured have backup power?	FA	 Available when or Spoilage Coverage CP 04 40 is selected. 
//		Back-up power sources are available for spoilage coverage. Please describe type of system.	QQ	-
//		Is the value of the leased property included in the BPP limit?	FA	 Available when "Leased Property CP 14 60" is selected.
//		Does the applicant/insured rent out any part of the building?	FA	 Available when "Exclusion Of loss Due To By-Products Of Production Or Processing Operations (Rental Properties) CP 10 34" is selected.
//		Exterior House Keeping and Maintenance	FA	-
//		Has the property been gutted out to the framing and then rebuilt, including drywall, wiring, plumbing, and heating?	FA	 Available when "Building Coverage" the "Valuation Method" is selected with "Replacement Cost" and under the Details tab "Year built" is older than 1965.

    }


}//END OF FILE





















