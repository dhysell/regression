package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.CommercialPropertyLine.*;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPCommercialPropertyLine_Coverages;
import repository.pc.sidemenu.SideMenuPC;


public class GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_Coverages extends BasePage {

    private WebDriver driver;

    public GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_Coverages(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    private void isOnCoveragesTab() {
        repository.pc.workorders.generic.GenericWorkorderCommercialPropertyCommercialPropertyLineCPP commercialPropertyLine = new GenericWorkorderCommercialPropertyCommercialPropertyLineCPP(getDriver());
        if (finds(By.xpath("//span[contains(@id, ':CPLineScreen:CPLineCV') and (text()='Crime')]")).isEmpty()) {
            if (finds(By.xpath("//span[contains(@id, ':CPWizardStepGroup:CPLineScreen:ttlBar') and (text()='Commercial Property Line')]")).isEmpty()) {
                repository.pc.sidemenu.SideMenuPC sidemenu = new SideMenuPC(driver);
                sidemenu.clickSideMenuCPCommercialPropertyLine();
            }
            commercialPropertyLine.clickCoveragesTab();
        }
    }


    private String getSelectXpath(String coverage, String title) {
        return "//div[contains(text(), '" + coverage + "')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), '" + title + "')]/parent::td/following-sibling::td/table";
    }


    /**
     * @param policy
     * @Description FILL OUT COMMERCIAL PROPERTY LINE COVERAGES TAB
     */
    public void fillOutCoverages(GeneratePolicy policy) {
        fillOutCoveragesProperty(policy);
        fillOutCoveragesCrime(policy);
    }


    /**
     * @param policy policy
     * @Description FILL OUT COMMERCAIL PROPERY LINE COVERAGE TAB - PROPERTY SECTION
     */
    public void fillOutCoveragesProperty(GeneratePolicy policy) {
        CPPCommercialPropertyLine_Coverages myCoverages = policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages();
        isOnCoveragesTab();
        if (selectClientsProperty_CR0401(myCoverages.isClientsProperty_CR_04_01())) {
            setClientsProperty_CR0401_Limit(String.valueOf(myCoverages.getClientsProperty_CR_04_01_Limit()));
            selectClientsProperty_CR0401_Deductible(myCoverages.getClientsProperty_CR_04_01_Deductible());
        }
    }


    /**
     * @param policy policy
     * @Description FILL OUT COMMERCIAL PROPERTY LINE CONVERAGE TAB - CRIME SECTION
     */
    public void fillOutCoveragesCrime(GeneratePolicy policy) {
        CPPCommercialPropertyLine_Coverages myCoverages = policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages();
        isOnCoveragesTab();

//		Employee Theft
        if (selectEmployeeTheft(myCoverages.isEmployeeTheft())) {
            setEmployeeTheft_Limit(String.valueOf(myCoverages.getEmployeeTheft_Limit()));
            selectEmployeeTheft_Deductible(myCoverages.getEmployeeTheft_Deductible());

//			Forgery Or Alteration
            //available when employee theft is selected
            selectForgeryOrAlteration(myCoverages.isForgeryOrAlteration());
        }

//		Include Volunteer Workers Other Than Fund Solicitors As Employees CR 25 10
        selectIncludeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR2510(myCoverages.isIncludeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10());

//		Inside The Premises � Theft Of Money And Securities
        if (selectInsideThePremises_TheftOfMoneyAndSecurities(myCoverages.isInsideThePremises_TheftOfMoneyAndSecurities())) {
            setInsideThePremises_TheftOfMoneyAndSecurities_Limit(String.valueOf(myCoverages.getInsideThePremises_TheftOfMoneyAndSecurities_Limit()));
            selectInsideThePremises_TheftOfMoneyAndSecurities_Deductible(myCoverages.getInsideThePremises_TheftOfMoneyAndSecurities_Deductible());
        }

//		Inside The Premises � Theft Of Other Property CR 04 05
        if (selectInsideThePremises_TheftOfOtherProperty_CR0405(myCoverages.isInsideThePremises_TheftOfOtherProperty_CR_04_05())) {
            setInsideThePremises_TheftOfOtherProperty_CR0405_Limit(String.valueOf(myCoverages.getInsideThePremises_TheftOfOtherProperty_CR_04_05_Limit()));
            selectInsideThePremises_TheftOfOtherProperty_CR0405_Deductible(myCoverages.getInsideThePremises_TheftOfOtherProperty_CR_04_05_Deductible());
        }

//		Joint Loss Payable CR 20 15 -- Available when "Joint Loss Payable CR 20 15" is selected under the Additional Interest tab.


//		Outside The Premises
        if (selectOutsideThePremises(myCoverages.isOutsideThePremises())) {
            setOutsideThePremises_Limit(String.valueOf(myCoverages.getOutsideThePremises_Limit()));
            selectOutsideThePremises_Deductible(myCoverages.getOutsideThePremises_Deductible());
        }

        //Required Crime Information
        if (myCoverages.isInsideThePremises_TheftOfOtherProperty_CR_04_05() || myCoverages.isEmployeeTheft() || myCoverages.isOutsideThePremises()) {
            setRequiredCrimeInformation_ClassCode(myCoverages.getRequiredCrimeInformation_ClassCode());
            setRequiredCrimeInformation_NumberOfEmployees(String.valueOf(myCoverages.getRequiredCrimeInformation_NumberOfEmoployees()));
            setRequiredCrimeInformation_NumberOfAdditionalPremises(String.valueOf(myCoverages.getRequiredCrimeInformation_NumberOfAdditionalPremises()));
        }
    }


    /////////////////
    //PROPERTY
    /////////////////

    private Guidewire8Checkbox checkbox_ClientsProperty_CR0401() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Property CR 04 01')]/preceding-sibling::table");
    }

    private boolean selectClientsProperty_CR0401(boolean checked) {
        checkbox_ClientsProperty_CR0401().select(checked);
        return checked;
    }

    @FindBy(xpath = "//div[contains(text(), 'Property CR 04 01')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
    private WebElement editbox_ClientsProperty_CR0401_Limit;

    private void setClientsProperty_CR0401_Limit(String limit) {
        setText(editbox_ClientsProperty_CR0401_Limit, limit);
    }

    private Guidewire8Select select_ClientsProperty_CR0401_Deductible() {
        return new Guidewire8Select(driver,getSelectXpath("Property CR 04 01", "Deductible"));
    }

    private void selectClientsProperty_CR0401_Deductible(ClientsProperty_CR_04_01Deductible deductible) {
        Guidewire8Select mySelect = select_ClientsProperty_CR0401_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
    }


    ///////////////
    //CRIME
    ///////////////

    private Guidewire8Checkbox checkbox_EmployeeTheft() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Employee Theft')]/preceding-sibling::table");
    }

    private boolean selectEmployeeTheft(boolean checked) {
        checkbox_EmployeeTheft().select(checked);
        return checked;
    }

    @FindBy(xpath = "//div[contains(text(), 'Employee Theft')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
    private WebElement editbox_EmployeeTheft_Limit;

    private void setEmployeeTheft_Limit(String limit) {
        setText(editbox_EmployeeTheft_Limit, limit);
    }

    private Guidewire8Select select_EmployeeTheft_Deductible() {
        return new Guidewire8Select(driver, getSelectXpath("Employee Theft", "Deductible"));
    }

    private void selectEmployeeTheft_Deductible(EmployeeTheftDeductible deductible) {
        Guidewire8Select mySelect = select_EmployeeTheft_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
    }


    private Guidewire8Checkbox checkbox_ForgeryOrAlteration() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Forgery Or Alteration')]/preceding-sibling::table");
    }

    private boolean selectForgeryOrAlteration(boolean checked) {
        checkbox_ForgeryOrAlteration().select(checked);
        return checked;
    }

    private Guidewire8Checkbox checkbox_IncludeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR2510() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Include Volunteer Workers Other Than Fund Solicitors As Employees CR 25 10')]/preceding-sibling::table");
    }

    private boolean selectIncludeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR2510(boolean checked) {
        checkbox_IncludeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR2510().select(checked);
        return checked;
    }

    private Guidewire8Checkbox checkbox_InsideThePremises_TheftOfMoneyAndSecurities() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Inside The Premises - Theft Of Money And Securities')]/preceding-sibling::table");
    }

    private boolean selectInsideThePremises_TheftOfMoneyAndSecurities(boolean checked) {
        checkbox_InsideThePremises_TheftOfMoneyAndSecurities().select(checked);
        return checked;
    }

    @FindBy(xpath = "//div[contains(text(), 'Inside The Premises - Theft Of Money And Securities')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
    private WebElement editbox_InsideThePremises_TheftOfMoneyAndSecurities_Limit;

    private void setInsideThePremises_TheftOfMoneyAndSecurities_Limit(String limit) {
        setText(editbox_InsideThePremises_TheftOfMoneyAndSecurities_Limit, limit);
    }

    private Guidewire8Select select_InsideThePremises_TheftOfMoneyAndSecurities_Deductible() {
        return new Guidewire8Select(driver,getSelectXpath("Inside The Premises - Theft Of Money And Securities", "Deductible"));
    }

    private void selectInsideThePremises_TheftOfMoneyAndSecurities_Deductible(InsideThePremises_TheftOfMoneyAndSecuritiesDeductible deductible) {
        Guidewire8Select mySelect = select_InsideThePremises_TheftOfMoneyAndSecurities_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
    }


    private Guidewire8Checkbox checkbox_InsideThePremises_TheftOfOtherProperty_CR0405() {
        return new Guidewire8Checkbox(driver,"//div[contains(text(), 'Inside The Premises - Theft Of Other Property CR 04 05')]/preceding-sibling::table");
    }

    private boolean selectInsideThePremises_TheftOfOtherProperty_CR0405(boolean checked) {
        checkbox_InsideThePremises_TheftOfOtherProperty_CR0405().select(checked);
        return checked;
    }

    @FindBy(xpath = "//div[contains(text(), 'Inside The Premises - Theft Of Other Property CR 04 05')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
    private WebElement editbox_InsideThePremises_TheftOfOtherProperty_CR0405_Limit;

    private void setInsideThePremises_TheftOfOtherProperty_CR0405_Limit(String limit) {
        setText(editbox_InsideThePremises_TheftOfOtherProperty_CR0405_Limit, limit);
    }

    private Guidewire8Select select_InsideThePremises_TheftOfOtherProperty_CR0405_Deductible() {
        return new Guidewire8Select(driver,getSelectXpath("Inside The Premises - Theft Of Other Property CR 04 05", "Deductible"));
    }

    private void selectInsideThePremises_TheftOfOtherProperty_CR0405_Deductible(InsideThePremises_TheftOfOtherProperty_CR_04_05Deductible deductible) {
        Guidewire8Select mySelect = select_InsideThePremises_TheftOfOtherProperty_CR0405_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
    }

    private Guidewire8Checkbox checkbox_OutsideThePremises() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Outside The Premises')]/preceding-sibling::table");
    }

    private boolean selectOutsideThePremises(boolean checked) {
        checkbox_OutsideThePremises().select(checked);
        return checked;
    }

    @FindBy(xpath = "//div[contains(text(), 'Outside The Premises')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/folloing-sibling::td/input")
    private WebElement editbox_OutsideThePremises_Limit;

    private void setOutsideThePremises_Limit(String limit) {
        setText(editbox_OutsideThePremises_Limit, limit);
    }

    private Guidewire8Select select_OutsideThePremises_Deductible() {
        return new Guidewire8Select(driver,getSelectXpath("Outside The Premises", "Deductible"));
    }

    private void selectOutsideThePremises_Deductible(OutsideThePremisesDeductible deductible) {
        Guidewire8Select mySelect = select_OutsideThePremises_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
    }


    @FindBy(xpath = "//input[contains(@id, ':CrimeClassPicker-inputEl')]")
    private WebElement editbox_RequiredCrimeInformation_ClassCode;

    private void setRequiredCrimeInformation_ClassCode(String classCode) {
        setText(editbox_RequiredCrimeInformation_ClassCode, classCode);
    }

    @FindBy(xpath = "//input[contains(@id, 'CovPatternInputGroup:NumEmployerTermInput-inputEl')]")
    private WebElement editbox_RequiredCrimeInformation_NumberOfEmployees;

    private void setRequiredCrimeInformation_NumberOfEmployees(String numberOfEmployees) {
        setText(editbox_RequiredCrimeInformation_NumberOfEmployees, numberOfEmployees);
    }

    @FindBy(xpath = "//input[contains(@id, 'CovPatternInputGroup:NumPremisesTermInput-inputEl')]")
    private WebElement editbox_RequiredCrimeInformation_NumberOfAdditionalPremises;

    private void setRequiredCrimeInformation_NumberOfAdditionalPremises(String numberOfAdditionalPremises) {
        setText(editbox_RequiredCrimeInformation_NumberOfAdditionalPremises, numberOfAdditionalPremises);
    }
}
