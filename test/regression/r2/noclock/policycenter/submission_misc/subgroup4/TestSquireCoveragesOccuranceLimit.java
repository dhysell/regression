package regression.r2.noclock.policycenter.submission_misc.subgroup4;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.Property.SectionIIMedicalLimit;
import repository.gw.enums.Property.SectionIIMedicalOccuranceLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderLineReviewPL;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_PersonalProperty;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;

/**
 * @Author nvadlamudi
 * @Requirement : US9378 : Add the occurence and aggregate limit to coverages section II and IV
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Squire-Section%20II-Product-Model.xlsx"></a>
 * @Description
 * @DATE Oct 6, 2016
 */
public class TestSquireCoveragesOccuranceLimit extends BaseTest {
    private GeneratePolicy inlandMarinePolicyObjPL;

    private WebDriver driver;

    @Test()
    private void testCreateSquireFullApp() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locationsList.add(locToAdd);


        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
        inlandMarineCoverageSelection_PL_IM.add(InlandMarine.PersonalProperty);

        // PersonalProperty
        //property 1

        PersonalProperty pprop = new PersonalProperty();
        pprop.setType(PersonalPropertyType.RefrigeratedMilk);
        pprop.setDescription("Testdescription2");
        pprop.setLimit(1000);

        PersonalPropertyList ppropList = new PersonalPropertyList();
        ppropList.setRefrigeratedMilk(pprop);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = inlandMarineCoverageSelection_PL_IM;
        myInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        inlandMarinePolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.InlandMarineLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Coverage", "Occurance")
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testCreateSquireFullApp"})
    private void testvalidateOccuranceLimits() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(inlandMarinePolicyObjPL.agentInfo.getAgentUserName(), inlandMarinePolicyObjPL.agentInfo.getAgentPassword(), inlandMarinePolicyObjPL.accountNumber);
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickSectionIICoveragesTab();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        section2Covs.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.NamedPersonsMedical, 0, 0));
        section2Covs.setNamedPersonsMedical(this.inlandMarinePolicyObjPL.pniContact.getLastName());


        boolean testFailed = false;
        String errorMessage = "";

        //Medical Limit: 1000
        section2Covs.setMedicalLimit(SectionIIMedicalLimit.Limit_1000);
        section2Covs.sendArbitraryKeys(Keys.TAB);

        if (!section2Covs.getMedicalOccuranceLimit().contains(SectionIIMedicalOccuranceLimit.Limit_1000.getSectionIIOccuranceLimit().replace(",", ""))) {
            testFailed = true;
            errorMessage = errorMessage + "Medical Per Occurance Limit is not displayed correctly for Medical Limit : " + SectionIIMedicalLimit.Limit_1000.getValue() + "\n";
        }

        if (!section2Covs.getNamedPersonsMedicalPerOccuranceLimit().contains(SectionIIMedicalOccuranceLimit.Limit_1000.getSectionIIOccuranceLimit().replace(",", ""))) {
            testFailed = true;
            errorMessage = errorMessage + "Named Persons Medical Per Occurance Limit is not displayed correctly for Medical Limit : " + SectionIIMedicalLimit.Limit_1000.getValue() + "\n";
        }

        //Medical Limit: 2000
        section2Covs.setMedicalLimit(SectionIIMedicalLimit.Limit_2000);
        section2Covs.sendArbitraryKeys(Keys.TAB);

        if (!section2Covs.getMedicalOccuranceLimit().contains(SectionIIMedicalOccuranceLimit.Limit_2000.getSectionIIOccuranceLimit().replace(",", ""))) {
            testFailed = true;
            errorMessage = errorMessage + "Medical Per Occurance Limit is not displayed correctly for Medical Limit : " + SectionIIMedicalLimit.Limit_2000.getValue() + "\n";
        }

        if (!section2Covs.getNamedPersonsMedicalPerOccuranceLimit().contains(SectionIIMedicalOccuranceLimit.Limit_2000.getSectionIIOccuranceLimit().replace(",", ""))) {
            testFailed = true;
            errorMessage = errorMessage + "Named Persons Medical Per Occurance Limit is not displayed correctly for Medical Limit : " + SectionIIMedicalLimit.Limit_2000.getValue() + "\n";
        }


        //Medical Limit: 5000
        section2Covs.setMedicalLimit(SectionIIMedicalLimit.Limit_5000);
        section2Covs.sendArbitraryKeys(Keys.TAB);

        if (!section2Covs.getMedicalOccuranceLimit().contains(SectionIIMedicalOccuranceLimit.Limit_5000.getSectionIIOccuranceLimit().replace(",", ""))) {
            testFailed = true;
            errorMessage = errorMessage + "Medical Per Occurance Limit is not displayed correctly for Medical Limit : " + SectionIIMedicalLimit.Limit_5000.getValue() + "\n";
        }

        if (!section2Covs.getNamedPersonsMedicalPerOccuranceLimit().contains(SectionIIMedicalOccuranceLimit.Limit_5000.getSectionIIOccuranceLimit().replace(",", ""))) {
            testFailed = true;
            errorMessage = errorMessage + "Named Persons Medical Per Occurance Limit is not displayed correctly for Medical Limit : " + SectionIIMedicalLimit.Limit_5000.getValue() + "\n";
        }

        //Medical Limit: 10000
        section2Covs.setMedicalLimit(SectionIIMedicalLimit.Limit_10000);
        section2Covs.sendArbitraryKeys(Keys.TAB);

        if (!section2Covs.getMedicalOccuranceLimit().contains(SectionIIMedicalOccuranceLimit.Limit_10000.getSectionIIOccuranceLimit().replace(",", ""))) {
            testFailed = true;
            errorMessage = errorMessage + "Medical Per Occurance Limit is not displayed correctly for Medical Limit : " + SectionIIMedicalLimit.Limit_10000.getValue() + "\n";
        }

        if (!section2Covs.getNamedPersonsMedicalPerOccuranceLimit().contains(SectionIIMedicalOccuranceLimit.Limit_10000.getSectionIIOccuranceLimit().replace(",", ""))) {
            testFailed = true;
            errorMessage = errorMessage + "Named Persons Medical Per Occurance Limit is not displayed correctly for Medical Limit : " + SectionIIMedicalLimit.Limit_10000.getValue() + "\n";
        }

        //Medical Limit: 25000
        section2Covs.setMedicalLimit(SectionIIMedicalLimit.Limit_25000);
        section2Covs.sendArbitraryKeys(Keys.TAB);

        if (!section2Covs.getMedicalOccuranceLimit().contains(SectionIIMedicalOccuranceLimit.Limit_25000.getSectionIIOccuranceLimit().replace(",", ""))) {
            testFailed = true;
            errorMessage = errorMessage + "Medical Per Occurance Limit is not displayed correctly for Medical Limit : " + SectionIIMedicalLimit.Limit_25000.getValue() + "\n";
        }

        if (!section2Covs.getNamedPersonsMedicalPerOccuranceLimit().contains(SectionIIMedicalOccuranceLimit.Limit_10000.getSectionIIOccuranceLimit().replace(",", ""))) {
            testFailed = true;
            errorMessage = errorMessage + "Named Persons Medical Per Occurance Limit is not displayed correctly for Medical Limit : " + SectionIIMedicalLimit.Limit_25000.getValue() + "\n";
        }

        sideMenu.clickSideMenuSquirePropertyLineReview();
        GenericWorkorderLineReviewPL lineReview = new GenericWorkorderLineReviewPL(driver);
        lineReview.clickSectionTwoCoverages();
        if (!lineReview.getOccurenceLimit("Medical").contains(SectionIIMedicalOccuranceLimit.Limit_25000.getSectionIIOccuranceLimit().replace(",", ""))) {
            testFailed = true;
            errorMessage = errorMessage + "Line Reivew - Medical - Per Occurance Limit is not displayed correctly for Medical Limit : " + SectionIIMedicalLimit.Limit_25000.getValue() + "\n";
        }

        if (!lineReview.getOccurenceLimit("Named Persons Medical").contains(SectionIIMedicalOccuranceLimit.Limit_10000.getSectionIIOccuranceLimit().replace(",", ""))) {
            testFailed = true;
            errorMessage = errorMessage + "Line Reivew - Named Persons Medical - Per Occurance Limit is not displayed correctly for Medical Limit : " + SectionIIMedicalLimit.Limit_10000.getValue() + "\n";
        }

        sideMenu.clickSideMenuIMPersonalEquipment();
        GenericWorkorderSquireInlandMarine_PersonalProperty personalProperty = new GenericWorkorderSquireInlandMarine_PersonalProperty(driver);
        personalProperty.clickEditButton();
        personalProperty.setLimit(100);
        personalProperty.clickOk();

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (!guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains("Limit : can not be less than 1000"))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected page validation : 'Limit : can not be less than 1000' is not displayed. /n";
        }

        personalProperty.setLimit(70000);
        personalProperty.clickOk();

        if (!guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains("Limit : can not be more than 60000"))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected page validation : 'Limit : can not be more than 60000' is not displayed. /n";
        }


        personalProperty.setType(PersonalPropertyType.MilkContaminationAndRefrigeration);
        personalProperty.setDescription("Testing Purpose");
        personalProperty.setLimit(7001);
        personalProperty.clickOk();

        if (!guidewireHelpers.errorMessagesExist() && (!guidewireHelpers.getFirstErrorMessage().contains("Limit : Limit must be in increments of 1000"))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected page validation : 'Limit : Limit must be in increments of 1000' is not displayed. /n";
        }

        personalProperty.setLimit(5000);
        personalProperty.clickOk();


        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            risk.approveAll();
            risk.Quote();
        }
        sideMenu.clickSideMenuQuote();

        quote.clickSectionISectionIIPolicyPremium();
        quote.clickSectionIICoverages();

        if (!quote.getSectionIICoveragesValues("Per Occurrence Limit").replace(",", "").contains(SectionIIMedicalOccuranceLimit.Limit_25000.getSectionIIOccuranceLimit().replace(",", ""))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Section II Medical  Per Occurance Limit : " + SectionIIMedicalOccuranceLimit.Limit_25000.getSectionIIOccuranceLimit() + " is not displayed. \n";
        }

        if (testFailed)
            Assert.fail(errorMessage);
    }

}
