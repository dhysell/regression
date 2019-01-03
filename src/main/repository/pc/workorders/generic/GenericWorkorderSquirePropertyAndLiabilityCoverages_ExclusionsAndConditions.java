package repository.pc.workorders.generic;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.SquirePropertyLiabilityExclusionsConditions;
import repository.gw.generate.custom.UnderlyingInsuranceUmbrella;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions extends repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages {

    private WebDriver driver;

    public GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void fillOutExclutionsAndConditions(GeneratePolicy policy) throws Exception {
        clickCoveragesExclusionsAndConditions();
        filloutExclusions(policy);
        filloutConditions(policy);
    }

    public void filloutExclusions(GeneratePolicy policy) {
        SquirePropertyLiabilityExclusionsConditions exclusionsAndConditions = policy.squire.propertyAndLiability.propLiabExclusionsConditions;
        if (exclusionsAndConditions.isCanineExclusionEndorsemenet_280()) {
            clickCanineExclusionEndorsement280();
            
        }
    }

    public void filloutConditions(GeneratePolicy policy) throws Exception {
        SquirePropertyLiabilityExclusionsConditions exclusionsAndConditions = policy.squire.propertyAndLiability.propLiabExclusionsConditions;
        if (exclusionsAndConditions.isSpecialEndorsementForProperty_105()) {
            clickSpecialEndorsementForProperty105(exclusionsAndConditions.getSpecialEndorsementForProperty_105_DescriptionsList());
            
        }

        if (exclusionsAndConditions.isConditionSpecialEndorsementForLiability_205()) {
            clickSpecialEndorsementForLiability205(exclusionsAndConditions.getConditionSpecialEndorsementForLiability_205_DescriptionsList());
            
        }

        if (exclusionsAndConditions.isVendorAsAdditionalInsuredEndorsement_207()) {
            systemOut("Vendor As Additional Insured Endorsement 207 IS SET BY UW'S ONLY. SWITCHING USER TO UW");
            
            clickGenericWorkorderSaveDraft();
            new GuidewireHelpers(getDriver()).logout();
            Underwriters randomUW = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
            new Login(getDriver()).loginAndSearchSubmission(randomUW.getUnderwriterUserName(), randomUW.getUnderwriterPassword(), policy.accountNumber);
            repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
            repository.pc.sidemenu.SideMenuPC sidebar = new repository.pc.sidemenu.SideMenuPC(driver);
            sidebar = new SideMenuPC(driver);
            sidebar.clickSideMenuSquirePropertyCoverages();
            clickCoveragesExclusionsAndConditions();

            clickVendorAsAdditionalInsuredEndorsement207(exclusionsAndConditions.getVendorAsAdditionalInsuredEndorsement_207_VendorNameList());
            
            clickGenericWorkorderSaveDraft();

            new GuidewireHelpers(getDriver()).logout();
            new Login(getDriver()).loginAndSearchSubmission(policy);
            sidebar.clickSideMenuSquirePropertyCoverages();
            coverages.clickCoveragesExclusionsAndConditions();
        }

        if (exclusionsAndConditions.isConditionAccessYesEndorsement_209()) {
//            systemOut("Access Yes Endorsement 209 IS SET BY UW'S ONLY. SWITCHING USER TO UW");
//            
//            clickGenericWorkorderSaveDraft();
//            new GuidewireHelpers(getDriver()).logout();
//            Underwriters randomUW = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
//            new Login(getDriver()).loginAndSearchSubmission(randomUW.getUnderwriterUserName(), randomUW.getUnderwriterPassword(), policy.accountNumber);
//            GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
//            SideMenuPC sidebar = new SideMenuPC(driver);
//            sidebar = new SideMenuPC(driver);
//            sidebar.clickSideMenuSquirePropertyCoverages();
//            coverages.clickCoveragesExclusionsAndConditions();
        	if(finds(By.xpath("//div/div[text()='Access Yes Endorsement 209']")).isEmpty()) {
        		clickSectionIICoveragesTab();
        		clickCoveragesExclusionsAndConditions();
        	}
            clickAccessYesEndorsement209();
//            
//            clickGenericWorkorderSaveDraft();
//
//            new GuidewireHelpers(getDriver()).logout();
//            new Login(getDriver()).loginAndSearchSubmission(policy);
//            sidebar.clickSideMenuSquirePropertyCoverages();
//            coverages.clickCoveragesExclusionsAndConditions();

        }

        if (exclusionsAndConditions.isAdditionalInsured_LandlordEndorsement_291()) {
            clickAdditionalInsuredLandlordEndorsement291("Bunny", "Foo Foo");
            
        }


    }


    private WebElement checkbox_ExclusionCondition(String labelName) {
        return find(By.xpath("//div/div[text()='" + labelName + "']/preceding-sibling::table//input[contains(@id, ':CoverageInputSet:CovPatternInputGroup:_checkbox')]"));
    }

    @FindBy(xpath = "//a[contains(@id, ':LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOCoveragesHOEScreen:HOCoveragesPanelSet:HOPolicyLevelCoveragesIDTab')]")
    private WebElement button_CoveragesTab;

    final String name208 = "Additional Insured - Vendor's Endorsement 208";
    final String xpath_AdditionalInsuredVendorsEndorsement208 = "//fieldset[contains(., '" + name208 + "')]";

    //************************************************************************************
    final String name217 = "Additional \"Underlying Insurance\" Umbrella Endorsement 217";
    final String xpath_AdditionalUnderlyingInsuranceUmbrellaEndorsement217 = "//fieldset[contains(., '" + name217 + "')]";

    @FindBy(xpath = "//fieldset[contains(., '" + name217 + "')]//div[substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[2]/div")
    public WebElement div_AdditionalUnderlyingInsuranceUmbrellaEndorsement217Company;

    @FindBy(xpath = "//fieldset[contains(., '" + name217 + "')]//div[substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[3]/div")
    public WebElement div_AdditionalUnderlyingInsuranceUmbrellaEndorsement217PolicyNum;

    @FindBy(xpath = "//fieldset[contains(., '" + name217 + "')]//div[substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[4]/div")
    public WebElement div_AdditionalUnderlyingInsuranceUmbrellaEndorsement217LimitOfInsurance;

    @FindBy(xpath = "//fieldset[contains(., '" + name217 + "')]//div[contains(@id,':CoverageInputSet:CovPatternInputGroup:')]//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input")
    public List<WebElement> editboxes_AdditionalUnderlyingInsuranceUmbrellaEndorsement217;

    @FindBy(xpath = "//fieldset[contains(., '" + name217 + "')]//div[contains(@id,':CoverageInputSet:CovPatternInputGroup:')]")
    public WebElement table_AdditionalUnderlyingInsuranceUmbrellaEndorsement217;

    @FindBy(xpath = "//fieldset[contains(., '" + name217 + "')]//a[contains(@id, 'CoverageInputSet:CovPatternInputGroup:Add')]")
    private WebElement button_AdditionalUnderlyingInsuranceUmbrellaEndorsement217Add;

    //************************************************************************************
    final String name105 = "Special Endorsement for Property 105";
    @FindBy(xpath = "//fieldset[contains(., '" + name105 + "')]//div[substring(@id, string-length(@id) - 44)]//table[contains(@id, 'gridview-')]//tr[last()]/td[2]/div")
    public WebElement div_SpecialEndorsementForProperty105Description;

    @FindBy(xpath = "//fieldset[contains(., '" + name105 + "')]//div[substring(@id, string-length(@id) - 39)]//table[contains(@id, 'textarea-')]//tr[last()]/td[2]/textarea")
    public WebElement editbox_SpecialEndorsementForProperty105Description;

    @FindBy(xpath = "//fieldset[contains(., '" + name105 + "')]//a[contains(@id, 'CoverageInputSet:CovPatternInputGroup:Add')]")
    private WebElement button_SpecialEndorsementForProperty105Add;


    //************************************************************************************
    final String name205 = "Special Endorsement for Liability 205";
    @FindBy(xpath = "//fieldset[contains(., '" + name205 + "')]//div[substring(@id, string-length(@id) - 44) = ':CoverageInputSet:CovPatternInputGroup:0-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[2]/div")
    public WebElement div_SpecialEndorsementForProperty205Description;

    @FindBy(xpath = "//fieldset[contains(., '" + name205 + "')]//div[substring(@id, string-length(@id) - 39) = ':CoverageInputSet:CovPatternInputGroup:0']//table[contains(@id, 'textarea-')]//tr[last()]/td[2]/textarea")
    public WebElement editbox_SpecialEndorsementForProperty205Description;

    @FindBy(xpath = "//fieldset[contains(., '" + name205 + "')]//a[contains(@id, 'CoverageInputSet:CovPatternInputGroup:Add')]")
    private WebElement button_SpecialEndorsementForProperty205Add;


    //************************************************************************************
    final String name207 = "Vendor as Additional Insured Endorsement 207";
    @FindBy(xpath = "//fieldset[contains(., '" + name207 + "')]//div[substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[2]/div")
    public WebElement div_VendorAsAdditionalInsuredEndorsement207Description;

    @FindBy(xpath = "//fieldset[contains(., '" + name207 + "')]//div[contains(@id,':CovPatternInputGroup:ScheduleInputSet:')]//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input")
    public WebElement editbox_VendorAsAdditionalInsuredEndorsement207Description;

    @FindBy(xpath = "//fieldset[contains(., '" + name207 + "')]//a[contains(@id, ':CovPatternInputGroup:ScheduleInputSet:Add')]")
    private WebElement button_VendorAsAdditionalInsuredEndorsement207Add;
    //************************************************************************************

    final String name280 = "Canine Exclusion Endorsement 280";
    //************************************************************************************
    final String name291 = "Additional Insured - Landlord Endorsement 291";
    @FindBy(xpath = "//fieldset[contains(., '" + name291 + "')]//a[contains(@id, ':CovPatternInputGroup:ScheduleInputSet:Add')]")
    private WebElement button_AdditionalInsuredLandlordEndorsement291Add;

    @FindBy(xpath = "//fieldset[contains(., '" + name291 + "')]//div[substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[2]/div")
    public WebElement div_AdditionalInsuredLandlordEndorsement291Name;

    @FindBy(xpath = "//fieldset[contains(., '" + name291 + "')]//div[contains(@id,':CovPatternInputGroup:ScheduleInputSet:')]//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input[contains(@name,'c1')]")
    public WebElement editbox_AdditionalInsuredLandlordEndorsement291Name;

    @FindBy(xpath = "//fieldset[contains(., '" + name291 + "')]//div[substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[3]/div")
    public WebElement div_AdditionalInsuredLandlordEndorsement291Description;

    @FindBy(xpath = "//fieldset[contains(., '" + name291 + "')]//div[contains(@id,':CovPatternInputGroup:ScheduleInputSet:')]//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input[contains(@name,'c2')]")
    public WebElement editbox_AdditionalInsuredLandlordEndorsement291Description;

    //************************************************************************************
    final String name108 = "Coverage D Extension Endorsement 108";

    @FindBy(xpath = "//fieldset[contains(., '" + name108 + "')]//a[contains(@id, ':CovPatternInputGroup:ScheduleInputSet:Add')]")
    private WebElement button_CoverageDExtensionEndorsement108Add;

    @FindBy(xpath = "//fieldset[contains(., '" + name108 + "')]//div[substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[2]/div")
    public WebElement div_CoverageDExtensionEndorsement108Description;

    @FindBy(xpath = "//fieldset[contains(., '" + name108 + "')]//div[contains(@id,':CovPatternInputGroup:ScheduleInputSet:')]//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input")
    public WebElement editbox_CoverageDExtensionEndorsement108Description;
    //************************************************************************************

    final String name143 = "Additional Livestock Endorsement 143";
    @FindBy(xpath = "//fieldset[contains(., '" + name143 + "')]//a[contains(@id, ':CovPatternInputGroup:ScheduleInputSet:Add')]")
    private WebElement button_AdditionalLivestockendo143Add;

    @FindBy(xpath = "//fieldset[contains(., '" + name143 + "')]//div[substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[2]/div")
    public WebElement div_AdditionalLivestockendo143Type;

    @FindBy(xpath = "//fieldset[contains(., '" + name143 + "')]//div[contains(@id,':CovPatternInputGroup:ScheduleInputSet:')]//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input[contains(@name,'c1')]")
    public WebElement editbox_AdditionalLivestockendo143Type;

    @FindBy(xpath = "//fieldset[contains(., '" + name143 + "')]//div[substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[3]/div")
    public WebElement div_AdditionalLivestockendo143Head;

    @FindBy(xpath = "//fieldset[contains(., '" + name143 + "')]//div[contains(@id,':CovPatternInputGroup:ScheduleInputSet:')]//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input[contains(@name,'c2')]")
    public WebElement editbox_AdditionalLivestockendo143Head;

    @FindBy(xpath = "//fieldset[contains(., '" + name143 + "')]//div[substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[4]/div")
    public WebElement div_AdditionalLivestockendo143Limit;

    @FindBy(xpath = "//fieldset[contains(., '" + name143 + "')]//div[contains(@id,':CovPatternInputGroup:ScheduleInputSet:')]//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input[contains(@name,'c3')]")
    public WebElement editbox_AdditionalLivestockendo143Limit;

    @FindBy(xpath = "//fieldset[contains(., '" + name143 + "')]//div[substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[5]/div")
    public WebElement div_AdditionalLivestockendo143TotalLimit;

    @FindBy(xpath = "//fieldset[contains(., '" + name143 + "')]//div[contains(@id,':CovPatternInputGroup:ScheduleInputSet:')]//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input[contains(@name,'c4')]")
    public WebElement editbox_AdditionalLivestockendo143TotalLimit;

    @FindBy(xpath = "//fieldset[contains(., '" + name143 + "')]//div[substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[6]/div")
    public WebElement div_AdditionalLivestockendo143Description;

    @FindBy(xpath = "//fieldset[contains(., '" + name143 + "')]//div[contains(@id,':CovPatternInputGroup:ScheduleInputSet:')]//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input[contains(@name,'c5')]")
    public WebElement editbox_AdditionalLivestockendo143Description;

    //************************************************************************************
    final String name140 = "Coverage D Additional Insured Endorsement 140";
    @FindBy(xpath = "//fieldset[contains(., '" + name140 + "')]//a[contains(@id, ':CovPatternInputGroup:ScheduleInputSet:Add')]")
    private WebElement button_CoverageDAdditionalInsuredEndo140Add;

    @FindBy(xpath = "//fieldset[contains(., '" + name140 + "')]//div[substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[2]/div")
    public WebElement div_CoverageDAdditionalInsuredEndo140Name;

    @FindBy(xpath = "//fieldset[contains(., '" + name140 + "')]//div[contains(@id,':CovPatternInputGroup:ScheduleInputSet:')]//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input[contains(@name,'c1')]")
    public WebElement editbox_CoverageDAdditionalInsuredEndo140Name;

    @FindBy(xpath = "//fieldset[contains(., '" + name140 + "')]//div[substring(@id, string-length(@id) - 4) = '-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[3]/div")
    public WebElement div_CoverageDAdditionalInsuredEndo140Description;

    @FindBy(xpath = "//fieldset[contains(., '" + name140 + "')]//div[contains(@id,':CovPatternInputGroup:ScheduleInputSet:')]//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input[contains(@name,'c2')]")
    public WebElement editbox_CoverageDAdditionalInsuredEndo140Description;
    //************************************************************************************


    final String name246 = "Personal Umbrella Excluded Driver Endorsement 246";
    @FindBy(xpath = "//fieldset[contains(., '" + name246 + "')]//a[contains(@id, ':CoverageInputSet:CovPatternInputGroup:AddDriver')]")
    private WebElement button_PersonalUmbrellaExcludedDriverEndorsement246AddExisting;
    //************************************************************************************

    private WebElement link_PersonalUmbrellaExcludedDriverEndorsement246AddExistingName(Contact driver) {
        return find(By.xpath("//a[contains(@id, ':ExistingAdditionalInterest-itemEl') and .='" + driver.getFirstName() + " " + driver.getLastName() + "']"));
    }

    //************************************************************************************
    final String name224 = "Farm and Ranch Umbrella Excluded Driver Endorsement 224";
    @FindBy(xpath = "//fieldset[contains(., '" + name224 + "')]//a[contains(@id, ':CoverageInputSet:CovPatternInputGroup:AddDriver')]")
    private WebElement button_FarmAndRanchUmbrellaExcludedDriverEndorsement224AddExisting;

    private WebElement link_FarmAndRanchUmbrellaExcludedDriverEndorsement224AddExistingName(Contact driver) {
        return find(By.xpath("//a[contains(@id, ':ExistingAdditionalInterest-itemEl') and .='" + driver.getFirstName() + " " + driver.getLastName() + "']"));
    }

    final String name270 = "Umbrella Limitation Endorsement 270";
    //************************************************************************************


    public void clickNext() {
        super.clickNext();
    }


    public void clickCoveragesTab() {
        clickWhenClickable(button_CoveragesTab);
    }


    public boolean checkIfAdditionalInsuredVendorsEndorsement208Exists() {
        return checkIfElementExists(xpath_AdditionalUnderlyingInsuranceUmbrellaEndorsement217, 5000);
    }

    private void setAdditionalUnderlyingInsuranceUmbrellaEndorsement217Company(String company) {
        clickWhenClickable(div_AdditionalUnderlyingInsuranceUmbrellaEndorsement217Company);
        waitUntilElementIsClickable(editboxes_AdditionalUnderlyingInsuranceUmbrellaEndorsement217.get(editboxes_AdditionalUnderlyingInsuranceUmbrellaEndorsement217.size() - 1));
        editboxes_AdditionalUnderlyingInsuranceUmbrellaEndorsement217.get(editboxes_AdditionalUnderlyingInsuranceUmbrellaEndorsement217.size() - 1).sendKeys(company);
    }

    private void setAdditionalUnderlyingInsuranceUmbrellaEndorsement217PolicyNum(String policyNum) {
        
        new TableUtils(getDriver()).setValueForCellInsideTable(table_AdditionalUnderlyingInsuranceUmbrellaEndorsement217, "c2", policyNum);

        //waitUntilElementIsClickable(editboxes_AdditionalUnderlyingInsuranceUmbrellaEndorsement217.get(editboxes_AdditionalUnderlyingInsuranceUmbrellaEndorsement217.size() - 1));
        //editboxes_AdditionalUnderlyingInsuranceUmbrellaEndorsement217.get(editboxes_AdditionalUnderlyingInsuranceUmbrellaEndorsement217.size() - 1).sendKeys(policyNum);*/
    }

    private void setAdditionalUnderlyingInsuranceUmbrellaEndorsement217LimitOfInsurance(String limit) {
        clickWhenClickable(div_AdditionalUnderlyingInsuranceUmbrellaEndorsement217LimitOfInsurance);
        waitUntilElementIsClickable(editboxes_AdditionalUnderlyingInsuranceUmbrellaEndorsement217.get(editboxes_AdditionalUnderlyingInsuranceUmbrellaEndorsement217.size() - 1));
        editboxes_AdditionalUnderlyingInsuranceUmbrellaEndorsement217.get(editboxes_AdditionalUnderlyingInsuranceUmbrellaEndorsement217.size() - 1).sendKeys(limit);
    }

    private void clickAdditionalUnderlyingInsuranceUmbrellaEndorsement217Add() {
        clickWhenClickable(button_AdditionalUnderlyingInsuranceUmbrellaEndorsement217Add);
        
    }


    public void addAdditionalUnderlyingInsuranceUmbrellaEndorsement217Details(ArrayList<UnderlyingInsuranceUmbrella> underlyingInsurances) {
        for (UnderlyingInsuranceUmbrella ulIns : underlyingInsurances) {
            clickAdditionalUnderlyingInsuranceUmbrellaEndorsement217Add();
            setAdditionalUnderlyingInsuranceUmbrellaEndorsement217Company(ulIns.getCompany());
            sendArbitraryKeys(Keys.TAB);
            waitForPostBack();
            setAdditionalUnderlyingInsuranceUmbrellaEndorsement217PolicyNum(ulIns.getPolicyNumber());
            sendArbitraryKeys(Keys.TAB);
            waitForPostBack();
            setAdditionalUnderlyingInsuranceUmbrellaEndorsement217LimitOfInsurance(ulIns.getLimitOfInsurance());
        }
    }


    public boolean checkIfAdditionalUnderlyingInsuranceUmbrellaEndorsement217Exists() {
        return checkIfElementExists(xpath_AdditionalUnderlyingInsuranceUmbrellaEndorsement217, 5000);
    }

    private void setSpecialEndorsementForProperty105Description(String description) {
        clickWhenClickable(div_SpecialEndorsementForProperty105Description);
        waitUntilElementIsClickable(editbox_SpecialEndorsementForProperty105Description);
        editbox_SpecialEndorsementForProperty105Description.sendKeys(description);
    }

    private void clickSpecialEndorsementForProperty105Add() {
        clickWhenClickable(button_SpecialEndorsementForProperty105Add);
    }


    public void clickFarmAndRanchUmbrellaExcludedDriverEndorsement224(ArrayList<Contact> drivers) {
        clickWhenClickable(checkbox_ExclusionCondition(name224));

        for (Contact driver : drivers) {
            clickWhenClickable(button_FarmAndRanchUmbrellaExcludedDriverEndorsement224AddExisting);
            
            clickWhenClickable(link_FarmAndRanchUmbrellaExcludedDriverEndorsement224AddExistingName(driver));
        }
    }


    public void clickPersonalUmbrellaExcludedDriverEndorsement246(ArrayList<Contact> drivers) {
        clickWhenClickable(checkbox_ExclusionCondition(name246));

        for (Contact driver : drivers) {
            clickWhenClickable(button_PersonalUmbrellaExcludedDriverEndorsement246AddExisting);
            
            clickWhenClickable(link_PersonalUmbrellaExcludedDriverEndorsement246AddExistingName(driver));
        }
    }


    public void clickSpecialEndorsementForProperty105(ArrayList<String> descriptions) {
        clickWhenClickable(checkbox_ExclusionCondition(name105));
        

        for (String desc : descriptions) {
            clickSpecialEndorsementForProperty105Add();
            
            setSpecialEndorsementForProperty105Description(desc);
        }
    }

    private void setSpecialEndorsementForProperty205Description(String description) {
        clickWhenClickable(div_SpecialEndorsementForProperty205Description);
        waitUntilElementIsClickable(editbox_SpecialEndorsementForProperty205Description);
        editbox_SpecialEndorsementForProperty205Description.sendKeys(description);
    }

    private void clickSpecialEndorsementForProperty205Add() {
        clickWhenClickable(button_SpecialEndorsementForProperty205Add);
    }


    public void clickSpecialEndorsementForLiability205(ArrayList<String> descriptions) {
        clickWhenClickable(checkbox_ExclusionCondition(name205));

        for (String desc : descriptions) {
            clickSpecialEndorsementForProperty205Add();
            setSpecialEndorsementForProperty205Description(desc);
        }
    }


    public void clickAccessYesEndorsement209() {
        clickWhenClickable(checkbox_ExclusionCondition("Access Yes Endorsement 209"));
    }


    private void setVendorAsAdditionalInsuredEndorsement207Description(String description) {
        clickWhenClickable(div_VendorAsAdditionalInsuredEndorsement207Description);
        waitUntilElementIsClickable(editbox_VendorAsAdditionalInsuredEndorsement207Description);
        editbox_VendorAsAdditionalInsuredEndorsement207Description.sendKeys(description);
    }

    private void clickVendorAsAdditionalInsuredEndorsement207Add() {
        clickWhenClickable(button_VendorAsAdditionalInsuredEndorsement207Add);
    }


    public void clickVendorAsAdditionalInsuredEndorsement207(ArrayList<String> namesDescs) {
        clickWhenClickable(checkbox_ExclusionCondition(name207));

        for (String desc : namesDescs) {
            clickVendorAsAdditionalInsuredEndorsement207Add();
            setVendorAsAdditionalInsuredEndorsement207Description(desc);
        }
    }


    public void clickCanineExclusionEndorsement280() {
        clickWhenClickable(checkbox_ExclusionCondition(name280));
    }


    public void clickAdditionalInsuredLandlordEndorsement291(String name, String desc) {
        clickWhenClickable(checkbox_ExclusionCondition(name291));
        clickAdditionalInsuredLandlordEndorsement291ADD();
        
        clickWhenClickable(div_AdditionalInsuredLandlordEndorsement291Name);
        
        //waitUntilElementIsClickable(editbox_AdditionalInsuredLandlordEndorsement291Name);
        editbox_AdditionalInsuredLandlordEndorsement291Name.sendKeys(name);
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        //clickWhenClickable(div_AdditionalInsuredLandlordEndorsement291Description);
        //
        //div_AdditionalInsuredLandlordEndorsement291Description.click();
        //waitUntilElementIsClickable(editbox_AdditionalInsuredLandlordEndorsement291Description);
        editbox_AdditionalInsuredLandlordEndorsement291Description.sendKeys(desc);

    }


    public void clickCoverageDExtensionEndorsement108(String desc) {
        clickWhenClickable(checkbox_ExclusionCondition(name108));
        clickWhenClickable(button_CoverageDExtensionEndorsement108Add);
        
        clickWhenClickable(div_CoverageDExtensionEndorsement108Description);
        waitUntilElementIsClickable(editbox_CoverageDExtensionEndorsement108Description);
        editbox_CoverageDExtensionEndorsement108Description.sendKeys(desc);
    }

    private void clickAdditionalInsuredLandlordEndorsement291ADD() {
        clickWhenClickable(button_AdditionalInsuredLandlordEndorsement291Add);
    }


    public void setCoverageDAdditionalInsuredEndo140(String name, String desc) {
        clickWhenClickable(checkbox_ExclusionCondition(name140));

        clickWhenClickable(button_CoverageDAdditionalInsuredEndo140Add);
        clickWhenClickable(div_CoverageDAdditionalInsuredEndo140Name);
        waitUntilElementIsClickable(editbox_CoverageDAdditionalInsuredEndo140Name);
        editbox_CoverageDAdditionalInsuredEndo140Name.sendKeys(name);
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        //clickWhenClickable(div_CoverageDAdditionalInsuredEndo140Description);
        //waitUntilElementIsClickable(editbox_CoverageDAdditionalInsuredEndo140Description);
        editbox_CoverageDAdditionalInsuredEndo140Description.sendKeys(desc);
    }


    public void setAdditionalLivestockEndorsement143(String type, int head, int limit, int totalLimit, String desc) {
        clickWhenClickable(checkbox_ExclusionCondition(name143));
        clickWhenClickable(button_AdditionalLivestockendo143Add);

        clickWhenClickable(div_AdditionalLivestockendo143Type);
        waitUntilElementIsClickable(editbox_AdditionalLivestockendo143Type);
        editbox_AdditionalLivestockendo143Type.sendKeys(type);
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        //clickWhenClickable(div_AdditionalLivestockendo143Head);
        //waitUntilElementIsClickable(editbox_AdditionalLivestockendo143Head);
        editbox_AdditionalLivestockendo143Head.sendKeys(String.valueOf(head));
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        //clickWhenClickable(div_AdditionalLivestockendo143Limit);
        //waitUntilElementIsClickable(editbox_AdditionalLivestockendo143Limit);
        editbox_AdditionalLivestockendo143Limit.sendKeys(String.valueOf(limit));
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        //clickWhenClickable(div_AdditionalLivestockendo143TotalLimit);
        //waitUntilElementIsClickable(editbox_AdditionalLivestockendo143TotalLimit);
        editbox_AdditionalLivestockendo143TotalLimit.sendKeys(String.valueOf(totalLimit));
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        //clickWhenClickable(div_AdditionalLivestockendo143Description);
        //waitUntilElementIsClickable(editbox_AdditionalLivestockendo143Description);
        editbox_AdditionalLivestockendo143Description.sendKeys(desc);
        editbox_AdditionalLivestockendo143Description.sendKeys(Keys.TAB);
        waitForPostBack();
    }


    public void clickUmbrellaLimitationEndorsement270() {
        clickWhenClickable(checkbox_ExclusionCondition(name270));
        
    }


}
