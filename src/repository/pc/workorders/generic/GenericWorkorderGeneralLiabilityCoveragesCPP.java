package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_Deductible;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_NumberLocations;
import repository.gw.enums.GeneralLiability.StandardCoverages.CG0001_DeductibleLiabilityInsCG0300;
import repository.gw.enums.GeneralLiability.StandardCoverages.CG0001_OccuranceLimit;
import repository.gw.enums.GeneralLiabilityCoverages;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.PackageRiskType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPGLCoveragesAdditionalInsureds;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;

import java.util.Date;

public class GenericWorkorderGeneralLiabilityCoveragesCPP extends BasePage {

    private WebDriver driver;

    public GenericWorkorderGeneralLiabilityCoveragesCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(@id, ':GeneralLiabilityScreen:GeneralLiability_IncludedCardTab-btnEl')]")
    public WebElement link_StandardCoveragesTab;

    @FindBy(xpath = "//span[contains(@id, ':GeneralLiabilityScreen:GeneralLiability_AdditionalCoveragesCardTab-btnEl')]")
    public WebElement link_AdditionalCoveragesTab;

    @FindBy(xpath = "//span[contains(@id, ':GeneralLiabilityScreen:ExclusionsAndConditionsCardTab-btnEl')]")
    public WebElement link_ExclutionsAndConditionsTab;

    @FindBy(xpath = "//span[contains(@id, ':GeneralLiabilityScreen:GeneralLiability_AdditionalInsuredCardTab-btnEl')]")
    public WebElement link_AdditionalInsuredsTab;

    @FindBy(xpath = "//span[contains(@id, ':GeneralLiability_QuestionsCardTab-btnEl')]")
    public WebElement link_UnderwritingQuestionsTab;


    public void clickUnderwritingQuestionsTab() {
        
        clickWhenClickable(link_UnderwritingQuestionsTab);
        
    }

    Guidewire8Checkbox checkbox_Blank(GeneralLiabilityCoverages coverage) {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), '" + coverage.getValue() + "' )]/preceding-sibling::table");
    }

    private PolicyLocation location;
    private GeneralLiabilityCoverages coverage;


    public void clickStandardCoverages() {
        long endTime = new Date().getTime() + 5000;
        do {
            clickWhenClickable(link_StandardCoveragesTab);
            
        }
        while (finds(By.xpath("//div[contains(text(), 'General Liability CG 00 01')]")).size() <= 0 && new Date().getTime() < endTime);
    }

    // ADDITIONAL COVERAGES

    public void clickAdditionalCoverages() {
        long endTime = new Date().getTime() + 5000;
        do {
            clickWhenClickable(link_AdditionalCoveragesTab);
            
        }
        while (finds(By.xpath("//span[contains(@id, ':GenericCoverage_ExtPanelSet:0')]")).size() <= 0 && new Date().getTime() < endTime);
    }


    //TODO set all the Additional Coverage Tab Questions
    public void setAdditionalCoverageQuestions(GeneralLiabilityCoverages coverage, PolicyLocation location) {
        GenericWorkorderGeneralLiabilityCoverages_AdditionalCoverages additionalCoverages = new GenericWorkorderGeneralLiabilityCoverages_AdditionalCoverages(driver);
        GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions exclusionsConditoins = new GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions(driver);
        
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (!guidewireHelpers.isRequired(coverage.getValue()) && (guidewireHelpers.isElectable(coverage.getValue()) || guidewireHelpers.isSuggested(coverage.getValue()))) {
            
            checkbox_Blank(coverage).select(true);

            switch (coverage) {
                case CG2037:

                    break;
                case CG2116:
                    
                    exclusionsConditoins.setDesignatedProfessionalServices_CG_21_16("Little Bunny Foo Foo");
                case CG2417:
                    
                    additionalCoverages.setContractualLiability_Railroads_CG_24_17("Little Bunny Foo Foo", "Hopping Through The Forest", 100);
                    break;
                case CG2450:
                    
                    additionalCoverages.setUnmannedAircraftDescriptionCG2450("Little Bunny Foo Foo", "Hopping Through The Forest", 100);
                    break;
                case CG2503:

                    break;
                case CG2806:
                    
                    exclusionsConditoins.setLimitationOfCoverageToInsuredPremises_CG_28_06("Little Bunny Foo Foo", location);
                    break;
                case IDCG312010:

                    break;
                case IDCG312013:
                    
                    additionalCoverages.setEmploymentPracticesLiabilityInsuranceIDCG312013(50, 50, EmploymentPracticesLiabilityInsurance_AggregateLimit.FiftyThousand, EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand, true, "3", EmploymentPracticesLiabilityInsurance_NumberLocations.ONE);
                    break;
                default:
                    break;
            }
        }
    }

    //set required fields on Additional Coverages Tab
    public void fillOutAdditionalCoveragesTab(GeneratePolicy policy/*, GeneralLiabilityCoverages coverage*/, PolicyLocation location) {
        GenericWorkorderGeneralLiabilityCoverages_AdditionalCoverages additionalCoverages = new GenericWorkorderGeneralLiabilityCoverages_AdditionalCoverages(driver);
        if (new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
            if (policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().isEmploymentPracticesLiabilityInsuranceIDCG312013()) {
                additionalCoverages.setEmploymentPracticesLiabilityInsurance_IDCG_31_2013(true);
                additionalCoverages.setLiabilityPracticesNumberOfFullTimeEmployees(policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getNumberFullTimeEmployees());
                
                additionalCoverages.setLiabilityPracticesNumberOfPartTimeEmployees(policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getNumberPartTimeEmployees());
                
                additionalCoverages.selectEmploymentPracticesAggregateLimit(policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getAggragateLimit());
                
                additionalCoverages.selectEmploymentPracticesDeductible(policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getEmploymentPracticesLiabilityInsuranceDeductible());
                
                additionalCoverages.setRadioEmploymentPracticesThirdPartyLiability(policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().isThirdPartyLiability());
                
                additionalCoverages.selectEmploymentPracticesTotalNumberLocations(policy.generalLiabilityCPP.getCPPGeneralLiabilityCoverages().getEmploymentPracticesLiabilityInsuranceNumberLocations());
                
            } else {
                additionalCoverages.setEmploymentPracticesLiabilityInsurance_IDCG_31_2013(false);
            }//END ELSE
        }//END IF
        //			TODO: Add all of the needed classes to fill out Additional Coverages Tab
        for (CPPGeneralLiabilityExposures exposure : policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {
            switch (exposure.getClassCode()) {
                case "97047":
                case "97050":
                    //lawn care services
                    break;
                case "70412":
                case "58161":
                case "50911":
                case "59211":
                case "58165":
                case "58166":
                    break;
                case "58408":
                case "58409":
                case "58456":
                case "58457":
                case "58458":
                case "58459":
                case "90089":
                case "98597":
                case "98598":
                    //					coverage = GeneralLiabilityCoverages.CG2450;
                    //setAdditionalCoverageQuestions(coverage, policy.locationList.get(0));
                    break;
            }//end switch
        }//END FOR
    }//END fillOutAdditionalCoveragesTab

    // EXCLUTIONS AND CONDITIONS


    public void clickExclusionsAndConditions() {
        long endTime = new Date().getTime() + 5000;
        do {
            clickWhenClickable(link_ExclutionsAndConditionsTab);
            
        }
        while (finds(By.xpath("//span[text() = 'Exclusions']")).size() <= 0 && new Date().getTime() < endTime);
    }


    // END OF CONDITIONS


    //TODO Set Exclusions and Conditions Tab Questions
    public void setExclusionsAndConditionsQuestions(GeneralLiabilityCoverages coverage, PolicyLocation location) {
        GenericWorkorderGeneralLiabilityCoverages_AdditionalCoverages additioanlCoverages = new GenericWorkorderGeneralLiabilityCoverages_AdditionalCoverages(driver);
        GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions exclusionsConditoins = new GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions(driver);
        switch (coverage) {
            case CG2037:

                break;
            case CG2116:
                
                exclusionsConditoins.setDesignatedProfessionalServices_CG_21_16("Little Bunny Foo Foo");
            case CG2417:
                
                additioanlCoverages.setContractualLiability_Railroads_CG_24_17("Little Bunny Foo Foo", "Hopping Through The Forest", 100);
                break;
            case CG2503:

                break;
            case CG2806:
                
                exclusionsConditoins.setLimitationOfCoverageToInsuredPremises_CG_28_06("Little Bunny Foo Foo", location);
                break;
            case IDCG312010:

                break;
            case IDCG312013:
                
                additioanlCoverages.setEmploymentPracticesLiabilityInsuranceIDCG312013(50, 50, EmploymentPracticesLiabilityInsurance_AggregateLimit.FiftyThousand, EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand, true, "3", EmploymentPracticesLiabilityInsurance_NumberLocations.ONE);
                break;


            default:
                break;
        }
    }


    public void clickAdditioanlInsureds() {
        long endTime = new Date().getTime() + 5000;
        do {
            clickWhenClickable(link_AdditionalInsuredsTab);
            
        }
        while (finds(By.xpath("//label[contains(@id, ':AdditionalInsuredsDV:0')]")).size() <= 0 && new Date().getTime() < endTime);
    }


    //Enter the Additonal Insudes Pop-Up by clicking the name link
    public void editAdditoinalInsured(CPPGLCoveragesAdditionalInsureds additionalInsured) {
        if (additionalInsured.getCompanyPerson().equals(ContactSubType.Company)) {
            find(By.xpath("//a[contains(text(), '" + additionalInsured.getCompanyName() + "')]")).click();
        } else {
            find(By.xpath("//a[contains(text(), '" + additionalInsured.getFirstName() + "') and contains(text(), '" + additionalInsured.getLastName() + "')]")).click();
        }
        long endtime = new Date().getTime() + 5000;
        do {
            
        }
        while (finds(By.xpath("//span[contains(@id, 'EditPolicyAddnlInsuredContactRolePopup:ContactDetailScreen:ttlBar')]")).isEmpty() && new Date().getTime() < endtime);
    }


    //TODO Use this instead of making a whole new section. Make this the reference point for everything.
    //Enters all the required information on the Coverages Wizard step for select exposure class codes
    public void fillOutGeneralLiabilityCoverages(GeneratePolicy policy) throws GuidewireException {
        repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoverages_StandardCoverages standardCoverages = new GenericWorkorderGeneralLiabilityCoverages_StandardCoverages(driver);
        clickStandardCoverages();
        
        standardCoverages.selectOccuranceLimit(CG0001_OccuranceLimit.FiveHundredThousand);
        
        for (CPPGeneralLiabilityExposures exposure : policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {
            switch (exposure.getClassCode()) {
                case "10368":
                case "98304":
                case "98305":
                case "98306":
                case "98309":
                case "99003":
                case "99004":
                    standardCoverages.selectDeductibleLiability(CG0001_DeductibleLiabilityInsCG0300.FiveHundred);
                    
                    break;
                default:
                    break;
            }
        }


        clickAdditionalCoverages();
        fillOutAdditionalCoveragesTab(policy/*, coverage*/, location);
        

        clickExclusionsAndConditions();
        fillOutExclutionsAndConditionsTab(policy);
        
        clickAdditioanlInsureds();
        fillOutAdditionalInsureds(policy);
        
        clickUnderwritingQuestionsTab();
        
        GenericWorkorderGeneralLiabilityCoverages_UnderwriterQuestions uwQuestions = new GenericWorkorderGeneralLiabilityCoverages_UnderwriterQuestions(driver);
        uwQuestions.filloutUnderwriterQuestions(policy, coverage);

    }


    public void fillOutAdditionalInsureds(GeneratePolicy policy) throws GuidewireException {
        GenericWorkorderGLCoveragesAdditionalInsured additionalInsureds = new GenericWorkorderGLCoveragesAdditionalInsured(getDriver());
        additionalInsureds.fillOutAdditionalInsureds(policy);
    }


    //TODO Find out what relates to which tab sort each of them out from this one
    //set required fields for specific exclusions.
    public void fillOutExclutionsAndConditionsTab(GeneratePolicy policy) {
        GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions exclusionsConditoins = new GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions(driver);
//		if(policy.currentPolicyType.equals(GeneratePolicyType.QuickQuote)) {
        for (CPPGeneralLiabilityExposures exposure : policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {
            switch (exposure.getClassCode()) {
                case "10036":
                    break;
                case "10113":
                    break;
                case "10115":
                    break;
                case "10117":
                    break;
                case "11127":
                    break;
                case "11128":
                    break;
                case "11138":
                    break;
                case "11234":
                    break;
                case "12356":
                    break;
                case "12683":
                    break;
                case "13410":
                    break;
                case "14401":
                    exclusionsConditoins.setProductsCompletedOperationsHazardRedefined_CG_24_07("Foo for Brett");
                    break;
                case "14655":
                    break;
                case "15600":
                    break;
                case "16819":
                    exclusionsConditoins.setProductsCompletedOperationsHazardRedefined_CG_24_07("Foo for Brett");
                    break;
                case "16820":
                    exclusionsConditoins.setProductsCompletedOperationsHazardRedefined_CG_24_07("Foo for Brett");
                    break;
                case "16900":
                    exclusionsConditoins.setProductsCompletedOperationsHazardRedefined_CG_24_07("Foo for Brett");
                    break;
                case "16901":
                    exclusionsConditoins.setProductsCompletedOperationsHazardRedefined_CG_24_07("Foo for Brett");
                    break;
                case "16902":
                    exclusionsConditoins.setProductsCompletedOperationsHazardRedefined_CG_24_07("Foo for Brett");
                    break;
                case "16905":
                    exclusionsConditoins.setProductsCompletedOperationsHazardRedefined_CG_24_07("Foo for Brett");
                    break;
                case "16906":
                    exclusionsConditoins.setProductsCompletedOperationsHazardRedefined_CG_24_07("Foo for Brett");
                    break;
                case "16910":
                    exclusionsConditoins.setProductsCompletedOperationsHazardRedefined_CG_24_07("Foo for Brett");
                    break;
                case "16911":
                    exclusionsConditoins.setProductsCompletedOperationsHazardRedefined_CG_24_07("Foo for Brett");
                    break;
                case "16915":
                    exclusionsConditoins.setProductsCompletedOperationsHazardRedefined_CG_24_07("Foo for Brett");
                    break;
                case "16916":
                    exclusionsConditoins.setProductsCompletedOperationsHazardRedefined_CG_24_07("Foo for Brett");
                    break;
                case "16930":
                    exclusionsConditoins.setProductsCompletedOperationsHazardRedefined_CG_24_07("Foo for Brett");
                    break;
                case "16931":
                    exclusionsConditoins.setProductsCompletedOperationsHazardRedefined_CG_24_07("Foo for Brett");
                    break;
                case "16941":
                    exclusionsConditoins.setProductsCompletedOperationsHazardRedefined_CG_24_07("Foo for Brett");
                    break;
                case "18200":
                    break;
                case "18911":
                    break;
                case "18912":
                    break;
                case "40031":
                    break;
                case "40032":
                    break;
                case "40115":
                    exclusionsConditoins.addBoat("Something, Something, Dark Side", 69, 2500, "Something, Something, The Force", "And Luke", "Blah Blah 626", 2000, "A long time ago in a far away galaxy...");
                    break;
                case "40117":
                    exclusionsConditoins.addBoat("Something, Something, Dark Side", 69, 2500, "Something, Something, The Force", "And Luke", "Blah Blah 626", 2000, "A long time ago in a far away galaxy...");
                    break;
                case "40140":
                    exclusionsConditoins.addBoat("Something, Something, Dark Side", 69, 2500, "Something, Something, The Force", "And Luke", "Blah Blah 626", 2000, "A long time ago in a far away galaxy...");
                    break;
                case "41650":
                    exclusionsConditoins.setDesignatedProfessionalServices_CG_21_16("Little Bunny Foo Foo");
                    break;
                case "41667":
                    exclusionsConditoins.setDesignatedProfessionalServices_CG_21_16("Little Bunny Foo Foo");
                    break;
                case "41668":
                    break;
                case "41669":
                    break;
                case "41670":
                    break;
                case "41675":
                    break;
                case "41677":
                    exclusionsConditoins.setDesignatedProfessionalServices_CG_21_16("Little Bunny Foo Foo");
                case "43151":
                    break;
                case "43200":
                    break;
                case "43217":
                    break;
                case "43218":
                    break;
                case "43219":
                    break;
                case "43220":
                    break;
                case "43421":
                    exclusionsConditoins.setExclusionAthleticOrSportsParticipants_CG_21_01("Foo for Brett");
                    break;
                case "43424":
                    exclusionsConditoins.setExclusionAthleticOrSportsParticipants_CG_21_01("Foo for Brett");
                    break;
                case "43551":
                    break;
                case "43760":
                    break;
                case "45190":
                    break;
                case "45191":
                    break;
                case "45192":
                    break;
                case "45193":
                    break;
                case "45334":
                    break;
                case "46822":
                    break;
                case "46881":
                    break;
                case "46882":
                    break;
                case "47050":
                    break;
                case "47052":
                    exclusionsConditoins.setDesignatedProfessionalServices_CG_21_16("Little Bunny Foo Foo");
                    break;
                case "57001":
                    break;
                case "58408":
                    exclusionsConditoins.setDesignatedProfessionalServices_CG_21_16("Little Bunny Foo Foo");
                    break;
                case "58409":
                    exclusionsConditoins.setDesignatedProfessionalServices_CG_21_16("Little Bunny Foo Foo");
                    break;
                case "58456":
                    exclusionsConditoins.setDesignatedProfessionalServices_CG_21_16("Little Bunny Foo Foo");
                    break;
                case "58457":
                    exclusionsConditoins.setDesignatedProfessionalServices_CG_21_16("Little Bunny Foo Foo");
                    break;
                case "58458":
                    exclusionsConditoins.setDesignatedProfessionalServices_CG_21_16("Little Bunny Foo Foo");
                    break;
                case "58459":
                    exclusionsConditoins.setDesignatedProfessionalServices_CG_21_16("Little Bunny Foo Foo");
                    break;
                case "61223":
                    break;
                case "62000":
                    break;
                case "62001":
                    break;
                case "62002":
                    break;
                case "62003":
                    break;
                case "63217":
                case "63218":
                case "63219":
                case "63220":
                    exclusionsConditoins.setExclusionAthleticOrSportsParticipants_CG_21_01("Foo for Brett");
                    break;
                case "65007":
                    break;
                case "66122":
                    break;
                case "66123":
                    break;
                case "66561":
                    break;
                case "68500":
                    break;
                case "91130":
                    break;
                case "91135":
                    break;
                case "91636":
                    break;
                case "91805":
                    exclusionsConditoins.setDesignatedProfessionalServices_CG_21_16("Little Bunny Foo Foo");
                    break;
                case "97002":
                    break;
                case "96930":
                    break;
                case "97003":
                    break;
                case "98751":
                    break;
                default:
                    break;
            }//end switch
        }//end for
        if (policy.commercialPackage.packageRisk.equals(PackageRiskType.Contractor)) {
            for (CPPGeneralLiabilityExposures exposure : policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {
                if (exposure.getClassCode().startsWith("9")) {
                    switch (exposure.getClassCode()) {
                        case "91111":
                        case "91150":
                        case "91405":
                        case "91481":
                        case "91523":
                        case "94225":
                        case "96816":
                        case "97047":
                        case "97050":
                        case "98993":
                        case "99310":
                        case "99471":
                        case "99975":
                            break;
                        default:
                            exclusionsConditoins.selectExplosionCollapseAndUndergroundPropertyDamageHazard_IDCG_31_2001_ExcludedHazards("default setting");
                            break;
                    }//end switch
                }//end if
            }//end for
        }//end if
//		}//end if
    }


    //set the Descrition for ExclusionDesignatedProfessionalServicesCG2116
    //this is a little difficult because the name of the input changes depending on what order the Coverage was added.
    public void setExclusionDesignatedProfessionalServicesCG2116() {
        clickAddExclusionDesignatedProfessionalServicesCG2116();
        if (!finds(By.xpath("//div[contains(text(), 'Exclusion - Designated Professional Services CG 21 16')]/ancestor::legend/following-sibling::div/span/div/div/table/tbody/tr/td/div/child::div/div/table/tbody/tr/child::td[2]")).isEmpty()) {
            find(By.xpath("//div[contains(text(), 'Exclusion - Designated Professional Services CG 21 16')]/ancestor::legend/following-sibling::div/span/div/div/table/tbody/tr/td/div/child::div/div/table/tbody/tr/child::td[2]")).click();
            
        }

        if (!finds(By.xpath("//div[contains(text(), 'Exclusion - Designated Professional Services CG 21 16')]/ancestor::legend/following-sibling::div/span/div/div/table/tbody/tr/td/div/child::div[last()]/table/tbody/tr/child::td/input")).isEmpty()) {
            find(By.xpath("//div[contains(text(), 'Exclusion - Designated Professional Services CG 21 16')]/ancestor::legend/following-sibling::div/span/div/div/table/tbody/tr/td/div/child::div[last()]/table/tbody/tr/child::td/input")).sendKeys("Foo For Brett");
            
        }
        


    }

    //click the add button the the ExclusionDesignatedProfessionalServicesCG2116 table.
    public void clickAddExclusionDesignatedProfessionalServicesCG2116() {
        if (!finds(By.xpath("//div[contains(text(), 'Exclusion - Designated Professional Services CG 21 16')]/ancestor::legend/following-sibling::div/descendant::span[contains(@id, ':Add-btnEl')]")).isEmpty()) {
            find(By.xpath("//div[contains(text(), 'Exclusion - Designated Professional Services CG 21 16')]/ancestor::legend/following-sibling::div/descendant::span[contains(@id, ':Add-btnEl')]")).click();
            
        }
        
    }

    public PolicyLocation getLocation() {
        return location;
    }

    public void setLocation(PolicyLocation location) {
        this.location = location;
    }

    public GeneralLiabilityCoverages getCoverage() {
        return coverage;
    }

    public void setCoverage(GeneralLiabilityCoverages coverage) {
        this.coverage = coverage;
    }

//    private boolean isOnCoveragesWizardStep() {
//        return !finds(By.xpath("//span[contains(@id, ':GLWizardStepGroup:GeneralLiabilityScreen:ttlBar') and (text()='Coverages')]")).isEmpty();
//    }

}



















