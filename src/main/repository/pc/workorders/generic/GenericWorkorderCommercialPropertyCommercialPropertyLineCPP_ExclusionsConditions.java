package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import persistence.enums.Underwriter.UnderwriterLine;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialPropertyLine_ExclusionsConditions;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import repository.pc.search.SearchSubmissionsPC;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_ExclusionsConditions extends BasePage {
    private WebDriver driver;
    private TableUtils tableUtils;

    public GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_ExclusionsConditions(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }


    repository.pc.workorders.generic.GenericWorkorderCommercialPropertyCommercialPropertyLineCPP commercialPropertyLine = new repository.pc.workorders.generic.GenericWorkorderCommercialPropertyCommercialPropertyLineCPP(driver);

    private void isOnExclusionsAndConditionsTab() {
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyCommercialPropertyLineCPP commercialPropertyLine = new GenericWorkorderCommercialPropertyCommercialPropertyLineCPP(driver);
        if (finds(By.xpath("//span[contains(@id, ':CPLineScreen:CPLineCV:GenericExclusionCondition_ExtPanelSet') and (text()='Conditions')]")).isEmpty()) {
            if (finds(By.xpath("//span[contains(@id, ':CPWizardStepGroup:CPLineScreen:ttlBar') and (text()='Commercial Property Line')]")).isEmpty()) {
                repository.pc.sidemenu.SideMenuPC sidemenu = new SideMenuPC(driver);
                sidemenu.clickSideMenuCPCommercialPropertyLine();
            }
            commercialPropertyLine.clickExclusionsConditionsTab();
        }
    }


    /**
     * @param policy policy
     *                       FILL OUT COMMERCIAL PROPERTY LINE EXCLUSIONS AND CONDITONS TAB
     * @throws Exception
     */
    public void fillOutExclusionsAndConditions(GeneratePolicy policy) throws Exception {
        fillOutExclusionsConditionsExclusions(policy);
        fillOutExclusionsConditionsConditions(policy);
    }


    /**
     * @param policy policy
     *                       FILL OUT COMMERCIAL PROPERTY LINE EXCLUSIONS AND CONDITIONS TAB - EXCLUSIONS
     */
    public void fillOutExclusionsConditionsExclusions(GeneratePolicy policy) {
        CPPCommercialPropertyLine_ExclusionsConditions myExclusions = policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineExclusionsConditions();
        isOnExclusionsAndConditionsTab();


        //		Exclude Certain Risks Inherent In Insurance Operations CR 25 23        � Available when "Employee Theft" is selected.
        if (policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().isEmployeeTheft()) {
            selectExcludeCertainRisksInherentInInsuranceOperations_CR_25_23(myExclusions.isExcludeCertainRisksInherentInInsuranceOperations_CR_25_23());
        }

        //		Exclude Designated Persons Or Classes Of Persons As Employees CR 25 01
        if (selectExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01(myExclusions.isExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01())) {
            for (String personOrClass : myExclusions.getPersonsOrClassesOfPersones()) {
                clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Designated Persons Or Classes Of Persons As Employees CR 25 01')]/ancestor::legend/following-sibling::div/descendant::span[contains(@id, 'ScheduleInputSet:Add-btnInnerEl')]")));
                WebElement table = find(By.xpath("//div[contains(text(), 'Exclude Designated Persons Or Classes Of Persons As Employees CR 25 01')]/ancestor::legend/following-sibling::div/descendant::table/tbody/tr/td/div"));
                int tableRow = tableUtils.getNextAvailableLineInTable(table, "Persons Or Classes Of Persons");
                tableUtils.setValueForCellInsideTable(table, tableRow, "Persons Or Classes Of Persons", "c1", personOrClass);
            }
        }
        //		Exclude Designated Premises CR 35 13
        if (selectExcludeDesignatedPremises_CR_35_13(myExclusions.isExcludeDesignatedPremises_CR_35_13())) {
            for (AddressInfo address : myExclusions.getExcludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfMoneyAndSecurities()) {
                clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Designated Premises CR 35 13')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Inside The Premises - Theft Of Money And Securities')]/following-sibling::div[1]/descendant::span[text()='Add']/parent::span")));
                WebElement table = find(By.xpath("//div[contains(text(), 'Exclude Designated Premises CR 35 13')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Inside The Premises - Theft Of Money And Securities')]/following-sibling::div"));
                int tableRow = tableUtils.getNextAvailableLineInTable(table, "Address Of Premises");
                tableUtils.setValueForCellInsideTable(table, tableRow, "Address Of Premises", "c1", address.getDropdownAddressFormat());
            }
            for (AddressInfo address : myExclusions.getExcludeDesignatedPremises_CR_35_13_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty()) {
                clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Designated Premises CR 35 13')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Inside The Premises - Robbery Or Safe Burglary Of Other Property')]/following-sibling::div[1]/descendant::span[text()='Add']/parent::span")));
                WebElement table = find(By.xpath("//div[contains(text(), 'Exclude Designated Premises CR 35 13')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Inside The Premises - Robbery Or Safe Burglary Of Other Property')]/following-sibling::div"));
                int tableRow = tableUtils.getNextAvailableLineInTable(table, "Address Of Premises");
                tableUtils.setValueForCellInsideTable(table, tableRow, "Address Of Premises", "c1", address.getDropdownAddressFormat());
            }
            for (AddressInfo address : myExclusions.getExcludeDesignatedPremises_CR_35_13_OutsideThePremises()) {
                clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Designated Premises CR 35 13')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Outside The Premises')]/following-sibling::div[1]/descendant::span[text()='Add']/parent::span")));
                WebElement table = find(By.xpath("//div[contains(text(), 'Exclude Designated Premises CR 35 13')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Outside The Premises')]/following-sibling::div"));
                int tableRow = tableUtils.getNextAvailableLineInTable(table, "Address Of Premises");
                tableUtils.setValueForCellInsideTable(table, tableRow, "Address Of Premises", "c1", address.getDropdownAddressFormat());
            }
            for (AddressInfo address : myExclusions.getExcludeDesignatedPremises_CR_35_13_InsideThePremises_TheftOfOtherProperty()) {
                clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Designated Premises CR 35 13')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Inside The Premises - Theft Of Other Property')]/following-sibling::div[1]/descendant::span[contains(@id, 'CPScheduledItemsInputSet:Add-btnEl')]")));
                WebElement table = find(By.xpath("//div[contains(text(), 'Exclude Designated Premises CR 35 13')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Inside The Premises - Theft Of Other Property')]/following-sibling::div"));
                int tableRow = tableUtils.getNextAvailableLineInTable(table, "Address Of Premises");
                tableUtils.setValueForCellInsideTable(table, tableRow, "Address Of Premises", "c1", address.getDropdownAddressFormat());
            }
        }
        //		Exclude Specified Property CR 35 01
        if (selectExcludeSpecifiedProperty_CR_35_01(myExclusions.isExcludeSpecifiedProperty_CR_35_01())) {
            for (String proertyNotCovered : myExclusions.getExcludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfMoneyAndSecurities()) {
                clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Specified Property CR 35 01')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Inside The Premises - Theft Of Money And Securities')]/following-sibling::div[1]/descendant::span[text()='Add']/parent::span")));
                WebElement table = find(By.xpath("//div[contains(text(), 'Exclude Specified Property CR 35 01')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Inside The Premises - Theft Of Money And Securities')]/following-sibling::div"));
                int tableRow = tableUtils.getNextAvailableLineInTable(table, "Property Not Covered");
                tableUtils.setValueForCellInsideTable(table, tableRow, "Property Not Covered", "c1", proertyNotCovered);
            }
            for (String proertyNotCovered : myExclusions.getExcludeSpecifiedProperty_CR_35_01_InsideThePremises_RobberyOrSafeBurglaryOfOtherProperty()) {
                clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Specified Property CR 35 01')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Inside The Premises - Robbery Or Safe Burglary Of Other Property')]/following-sibling::div[1]/descendant::span[text()='Add']/parent::span")));
                WebElement table = find(By.xpath("//div[contains(text(), 'Exclude Specified Property CR 35 01')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Inside The Premises - Robbery Or Safe Burglary Of Other Property')]/following-sibling::div"));
                int tableRow = tableUtils.getNextAvailableLineInTable(table, "Property Not Covered");
                tableUtils.setValueForCellInsideTable(table, tableRow, "Property Not Covered", "c1", proertyNotCovered);
            }
            for (String proertyNotCovered : myExclusions.getExcludeSpecifiedProperty_CR_35_01_OutsideThePremises()) {
                clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Specified Property CR 35 01')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Outside The Premises')]/following-sibling::div[1]/descendant::span[text()='Add']/parent::span")));
                WebElement table = find(By.xpath("//div[contains(text(), 'Exclude Specified Property CR 35 01')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Outside The Premises')]/following-sibling::div"));
                int tableRow = tableUtils.getNextAvailableLineInTable(table, "Property Not Covered");
                tableUtils.setValueForCellInsideTable(table, tableRow, "Property Not Covered", "c1", proertyNotCovered);
            }
            for (String proertyNotCovered : myExclusions.getExcludeSpecifiedProperty_CR_35_01_InsideThePremises_TheftOfOtherProperty()) {
                clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Specified Property CR 35 01')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Inside The Premises - Theft Of Other Property')]/following-sibling::div[1]/descendant::span[contains(@id, 'CPScheduledItemsInputSet:Add-btnEl')]")));
                WebElement table = find(By.xpath("//div[contains(text(), 'Exclude Specified Property CR 35 01')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Inside The Premises - Theft Of Other Property')]/following-sibling::div"));
                int tableRow = tableUtils.getNextAvailableLineInTable(table, "Property Not Covered");
                tableUtils.setValueForCellInsideTable(table, tableRow, "Property Not Covered", "c1", proertyNotCovered);
            }
        }

        //		Exclude Unauthorized Advances, Require Annual Audit CR 25 25 -- REQUIRED -- Available when question "Is the applicant/insured a fraternal organization or labor union?" is answered Yes.
        //		Exclusion Of Loss Due To Virus Or Bacteria CP 01 40 - REQUIRED
    }


    /**
     * @param policy policy
     *                       FILL OUT COMMERCIAL PROPERTY LINE EXCLUSIONS AND CONDITIONS TAB - CONDITIONS
     */
    public void fillOutExclusionsConditionsConditions(GeneratePolicy policy) {
        CPPCommercialPropertyLine_ExclusionsConditions myConditions = policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineExclusionsConditions();
        isOnExclusionsAndConditionsTab();

        //		Binding Arbitration CR 20 12 -- REQUIRED -- � Available when at least one of these coverages is selected: Clients' Property CR 04 01, Employee Theft, Forgery Or Alteration, Guests' Property CR 04 11, Include Volunteer Workers Other Than Fund Solicitors As Employees CR 25 10, Inside The Premises � Theft Of Money And Securities, Inside The Premises � Theft Of Other Property CR 04 05, Joint Loss Payable CR 20 15, and/or Outside The Premises.

        //		Change In Control Of The Insured � Notice To The Company CR 20 29 -- REQUIRED -- � Available when at least one of these coverages is selected: Clients' Property CR 04 01, Employee Theft, Forgery Or Alteration, Guests' Property CR 04 11, Include Volunteer Workers Other Than Fund Solicitors As Employees CR 25 10, Inside The Premises � Theft Of Money And Securities, Inside The Premises � Theft Of Other Property CR 04 05, Joint Loss Payable CR 20 15, and/or Outside The Premises.

        //		Commercial Property Conditions CP 00 90 -- REQUIRED

        //		Convert To An Aggregate Limit Of Insurance CR 20 08 -- REQUIRED -- � Available when at least one of these coverages is selected: "Employee Theft", "Forgery Or Alteration", "Guests' Property CR 04 11", "Inside The Premises � Theft Of Money And Securities", "Inside The Premises � Theft Of Other Property CR 04 05", and/or Outside The Premises.

        //		Idaho Changes CR 02 12 -- REQUIRED -- � Available when at least one of these coverages is selected: Clients' Property CR 04 01, Employee Theft, Forgery Or Alteration, Guests' Property CR 04 11, Include Volunteer Workers Other Than Fund Solicitors As Employees CR 25 10, Inside The Premises � Theft Of Money And Securities, Inside The Premises � Theft Of Other Property CR 04 05, Joint Loss Payable CR 20 15, and/or Outside The Premises.

        //		Multiple Deductible Form IDCP 31 1001 -- REQUIRED -- � Available when there is at least 2 deductibles on the same policy under Property. The deductibles that we need to look out for are: "Building Coverage", "Business Personal Property Coverage", Builders' Risk Coverage Form CP 00 20", "Property In The Open", and "Personal Property Of Others".


        //MUST BE ADDED BY UNDERWRITER
        if (myConditions.isCommercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition() || myConditions.isCommercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition()) {
            try {
				new Login(driver).switchUserToUW(UnderwriterLine.Commercial);
			} catch (Exception e) {
				Assert.fail("Unable to login as an Underwriter.");
			}
            new SearchSubmissionsPC(driver).searchSubmission(policy);
            commercialPropertyLine.clickExclusionsConditionsTab();
            if (selectConditionCommercialCrimeManuscriptEndorsement_IDCR_31_1001(myConditions.isCommercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition())) {
                for (String endorsement : myConditions.getCommercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition_List()) {
                    clickWhenClickable(table_1001Manuscript.findElement(By.xpath(".//descendant::span[contains(@id, ':Add-btnInnerEl')]/parent::span")));
                    tableUtils.setValueForCellInsideTable(table_1001Manuscript, tableUtils.getNextAvailableLineInTable(table_1001Manuscript), "Description", "c1", endorsement);
                    clickProductLogo();
                }
            }

            if (selectConditionCommercialPropertyManuscriptEndorsement_IDCP_31_1005(myConditions.isCommercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition())) {
                for (String endorsement : myConditions.getCommercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition_List()) {
                    clickWhenClickable(table_1005Manuscript.findElement(By.xpath(".//descendant::span[contains(@id, ':Add-btnInnerEl')]/parent::span")));
                    tableUtils.setValueForCellInsideTable(table_1005Manuscript, tableUtils.getNextAvailableLineInTable(table_1005Manuscript), "Description", "c1", endorsement);
                    clickProductLogo();
                }
            }

            repository.pc.workorders.generic.GenericWorkorder genWO = new GenericWorkorder(driver);
            genWO.clickGenericWorkorderSaveDraft();
            new GuidewireHelpers(driver).logout();
            new Login(driver).loginAndSearchSubmission(policy);
        }
    }


    //commercial crime 1001 manuscript end table
    @FindBy(xpath = "//div[(text()='Commercial Crime Manuscript Endorsement IDCR 31 1001')]/ancestor::legend/following-sibling::div")
    private WebElement table_1001Manuscript;

    //commercial crime 1005 manuscritpt end table
    @FindBy(xpath = "//div[(text()='Commercial Property Manuscript Endorsement IDCP 31 1005')]/ancestor::legend/following-sibling::div")
    private WebElement table_1005Manuscript;


    private boolean selectExcludeCertainRisksInherentInInsuranceOperations_CR_25_23(boolean checked) {
        new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclude Certain Risks Inherent In Insurance Operations CR 25 23')]/preceding-sibling::table").select(checked);
        return checked;
    }

    private boolean selectExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01(boolean checked) {
        new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclude Designated Persons Or Classes Of Persons As Employees CR 25 01')]/preceding-sibling::table").select(checked);
        return checked;
    }

    private boolean selectExcludeDesignatedPremises_CR_35_13(boolean checked) {
        new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclude Designated Premises CR 35 13')]/preceding-sibling::table").select(checked);
        if (finds(By.xpath("//div[contains(text(), 'Exclude Designated Premises CR 35 13')]/ancestor::legend//following-sibling::div/descendant::label[contains(text(), 'Inside The Premises - Theft Of Money And Securities')]")).isEmpty()) {
             Assert.fail("The checkbox for Exclude Designated Premises was verified to have been checked, but the resultant post back didn't change as it was supposed to. Please investigate.");
        }
        return checked;
    }

    private boolean selectExcludeSpecifiedProperty_CR_35_01(boolean checked) {
        new Guidewire8Checkbox(driver, "//div[contains(text(), 'Exclude Specified Property CR 35 01')]/preceding-sibling::table").select(checked);
        if (finds(By.xpath("//div[contains(text(), 'Exclude Specified Property CR 35 01')]/ancestor::legend//following-sibling::div/descendant::label[contains(text(), 'Inside The Premises - Theft Of Money And Securities')]")).isEmpty()) {
        	Assert.fail("The checkbox for Exclude Specified Property was verified to have been checked, but the resultant post back didn't change as it was supposed to. Please investigate.");
        }
        return checked;
    }


    private boolean selectConditionCommercialCrimeManuscriptEndorsement_IDCR_31_1001(boolean checked) {
        new Guidewire8Checkbox(driver, "//span[contains(text(), 'Conditions')]/ancestor::tbody/tr[2]/descendant::div[contains(text(), 'Commercial Crime Manuscript Endorsement IDCR 31 1001')]/preceding-sibling::table").select(checked);
        return checked;
    }


    private boolean selectConditionCommercialPropertyManuscriptEndorsement_IDCP_31_1005(boolean checked) {
        new Guidewire8Checkbox(driver, "//span[contains(text(), 'Conditions')]/ancestor::tbody/tr[2]/descendant::div[contains(text(), 'Commercial Property Manuscript Endorsement IDCP 31 1005')]/preceding-sibling::table").select(checked);
        return checked;
    }


    //Exclude Designated Premises CR 35 13
    //Exclude Specified Property CR 35 01
/*	private void clickAddExcludeDesignatedPremises_CR_35_13(String subCategory) {
		clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Designated Premises CR 35 13')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), '" + subCategory + "')]/following-sibling::div/descendant::span[contains(@id, ':Add-btnEl')]")));
	}

	private void setAddressOfPremisesExcludeDesignatedPremises_CR_35_13(String subCategory, String addressOfPremises) {
		clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Designated Premises CR 35 13')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), '" + subCategory + "')]/following-sibling::div/table/tbody/tr/td/div/child::div[last()]/div/table/tbody/tr/child::td[2]/div")));
		find(By.xpath("//textarea[contains(@name, 'c1')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
		find(By.xpath("//textarea[contains(@name, 'c1')]")).sendKeys(addressOfPremises);
		clickWhenClickable(find(By.xpath("//label[contains(text(), '" + subCategory + "')]")));
	}

	private void addAddressOfPremisesExcludeDesignatedPremises_CR_35_13(String subCategory, String addressOfPremises) {
		clickAddExcludeDesignatedPremises_CR_35_13(subCategory);
		setAddressOfPremisesExcludeDesignatedPremises_CR_35_13(subCategory, addressOfPremises);
	}


	private void clickAddExcludeSpecifiedProperty_CR_35_01(String subCategory) {
		clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Specified Property CR 35 01')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), '" + subCategory + "')]/following-sibling::div/descendant::span[contains(@id, ':Add-btnEl')]")));
	}

	private void setAddressOfPremisesExcludeSpecifiedProperty_CR_35_01(String subCategory, String addressOfPremises) {
		clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Specified Property CR 35 01')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), '" + subCategory + "')]/following-sibling::div/table/tbody/tr/td/div/child::div[last()]/div/table/tbody/tr/child::td[2]/div")));
		find(By.xpath("//textarea[contains(@name, 'c1')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
		find(By.xpath("//textarea[contains(@name, 'c1')]")).sendKeys(addressOfPremises);
		clickWhenClickable(find(By.xpath("//label[contains(text(), '" + subCategory + "')]")));
	}

	private void addAddressOfPremisesExcludeSpecifiedProperty_CR_35_01(String subCategory, String addressOfPremises) {
		clickAddExcludeSpecifiedProperty_CR_35_01(subCategory);
		setAddressOfPremisesExcludeSpecifiedProperty_CR_35_01(subCategory, addressOfPremises);
	}
	//end Exclude Specified Property CR 35 01 Exclude Designated Premises CR 35 13



	//Exclude Designated Persons Or Classes Of Persons As Employees CR 25 01
	private void clickAddExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01() {
		clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Designated Persons Or Classes Of Persons As Employees CR 25 01')]/ancestor::legend/following-sibling::div/descendant::span[contains(@id, ':ScheduleInputSet:Add-btnEl')]")));
	}

	private void setExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01(String persons) {
		clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Designated Persons Or Classes Of Persons As Employees CR 25 01')]/ancestor::legend/following-sibling::div//span/div/div/table/tbody/child::tr[1]/child::td[1]/div/child::div[last()]/div/table/tbody/tr/child::td[2]/div")));
		find(By.xpath("//textarea[contains(@name, 'c1')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
		find(By.xpath("//textarea[contains(@name, 'c1')]")).sendKeys(persons);
		clickWhenClickable(find(By.xpath("//div[contains(text(), 'Exclude Designated Persons Or Classes Of Persons As Employees CR 25 01')]")));
	}

	private void addExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01(String persons) {
		clickAddExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01();
		setExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01(persons);
	}*/
    //end Exclude Designated Persons Or Classes Of Persons As Employees CR 25 01


}
