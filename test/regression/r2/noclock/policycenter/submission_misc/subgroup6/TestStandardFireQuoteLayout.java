package regression.r2.noclock.policycenter.submission_misc.subgroup6;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author skandibanda
 * @Requirement : US6849 PL - Standard Lines (fire, liability, Inland Marine) Quote screen layout
 * @RequirementsLink <a href="http:// "
 * rally1.rallydev.com/#/33274298124d/detail/userstory/50822761518</a>
 * @Description -
 * @DATE June 14, 2016
 */
public class TestStandardFireQuoteLayout extends BaseTest {
    private GeneratePolicy myStandardFirePolicyObjPL, StandardLiabilityPolicyObjPL;
    private Agents agent;


    private WebDriver driver;

    //Quote Layout validations for Standard Fire
    @Test(dependsOnMethods = {"createStandardFirePolicy"})
    public void validateStandardFireQuote() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myStandardFirePolicyObjPL.agentInfo.getAgentUserName(), myStandardFirePolicyObjPL.agentInfo.getAgentPassword(), this.myStandardFirePolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQuote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        boolean testFailed = false;
        String errorMessage = "";

        //Clicking on Location link
        quote.clickSectionISectionIIPropertyDetailsLocationLink();
        quote.clickReturnToQuote();
        if (!quote.getSectionISectionIIPropertyDetailsLocationText().contains(myStandardFirePolicyObjPL.standardFire.getLocationList().get(0).getAddress().getLine1())) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Locations Address : " + myStandardFirePolicyObjPL.standardFire.getLocationList().get(0).getAddress().getLine1() + " is not displayed.\n";
        }

        if (quote.getSquirePremiumSummaryAmount("Section I", false, true, false) != (quote.getSquirePremiumSummaryAmount("Section I", true, false, false) - quote.getSquirePremiumSummaryAmount("Section I", false, false, true))) {
            testFailed = true;
            errorMessage = errorMessage + "Section I Net amount is not displayed correctly. \n";
        }

        // discounts
        if (quote.getQuoteTotalDiscountsSurcharges() != quote.getSquirePremiumSummaryAmount("Section I", false, false, true)) {
            testFailed = true;
            errorMessage = errorMessage + "Total discount : " + quote.getQuoteTotalDiscountsSurcharges() + " is not displayed correctly. \n";
        }

        //Net calculations
        if (quote.getQuoteTotalGrossPremium() != (quote.getQuoteTotalDiscountsSurcharges() + quote.getQuoteTotalNetPremium())) {
            testFailed = true;
            errorMessage = errorMessage + "Total Gross Policy Premium : " + quote.getQuoteTotalGrossPremium() + " is not displayed correctly. \n";
        }

        //All Net
        if (quote.getQuoteTotalNetPremium() != quote.getSquirePremiumSummaryAmount("Section I", false, true, false)) {
            testFailed = true;
            errorMessage = errorMessage + "Total Net Policy Premium : " + quote.getQuoteTotalNetPremium() + " is not displayed correctly. \n";
        }

        if (Double.parseDouble(quote.getSectionIPropertyDetailsValue(1, "Coverage A", "Coverage A").replace(",", "")) != myStandardFirePolicyObjPL.standardFire.getLocationList().get(0).getPropertyList().get(0).getPropertyCoverages().getCoverageA().getLimit()) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Section 1 Coverage A Limit: is not displayed correctly \n";
        }

        if (quote.getSquirePremiumSummaryAmount("Section I", false, true, false) < (quote.getSectionIPremiumByCoverageType(1, "Coverage A") + quote.getSectionIPremiumByCoverageType(1, "Coverage C"))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Section 1 Coverage A Net Premium is not matched with policy summary \n";
        }

        if (testFailed)
            Assert.fail(errorMessage);
    }

    @Test()
    public void createStandardFirePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);
        myStandardFire.section1Deductible = SectionIDeductible.FiveHundred;

        this.myStandardFirePolicyObjPL = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withStandardFire(myStandardFire)
                .withInsFirstLastName("Test", "Quote")
                .build(GeneratePolicyType.FullApp);
    }

    @Test()
    public void testGenerateStandardLiability() throws Exception {
        this.agent = AgentsHelper.getRandomAgent();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation());

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

        StandardLiabilityPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withProductType(ProductLineType.StandardLiability)
                .withLineSelection(LineSelection.StandardLiabilityPL)
                .withStandardLiability(myStandardLiability)
                .withInsFirstLastName("Guy", "StdLiab")
                .withInsAge(26)
                .withPolOrgType(OrganizationType.Individual)
                .build(GeneratePolicyType.FullApp);
    }

    //Quote Layout validations for Standard Liability
    @Test(dependsOnMethods = {"testGenerateStandardLiability"})
    public void validateStandardLiabilityQuote() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(StandardLiabilityPolicyObjPL.agentInfo.getAgentUserName(), StandardLiabilityPolicyObjPL.agentInfo.getAgentPassword(), StandardLiabilityPolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQuote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        boolean testFailed = false;
        String errorMessage = "";

        //coverages
        if (!quote.getStandardLiabilityCoverages("Liability").replace("K", "000").equals(StandardLiabilityPolicyObjPL.standardLiability.liabilitySection.getGeneralLiabilityLimit().getValue().replace(",", "").substring(0, 5))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected Standard Liability Coverage : " + quote.getStandardLiabilityCoverages("Liability") + " is not displayed. \n";
        }

        //Medical limit is showing as Medical in one line and Limit in another line so entering Limit
        if (!quote.getStandardLiabilityCoverages("Limit").replace(",", "").equals(StandardLiabilityPolicyObjPL.standardLiability.liabilitySection.getMedicalLimit().getValue().replace(",", ""))) {
            testFailed = true;
            errorMessage = errorMessage + "Expected  Standard Liability Medical Limit : " + quote.getStandardLiabilityCoverages("Medical") + " is not displayed. \n";
        }

        //Policy Premium
        if (quote.getQuoteTotalGrossPremium() != (quote.getStandardLiabilityPremium("General Liability"))) {// + quote.getStandardLiabilityPremium("Third Party Liability"))){
            testFailed = true;
            errorMessage = errorMessage + "Total Gross Policy Premium : " + quote.getQuoteTotalGrossPremium() + " is not displayed correctly. \n";
        }

        //DE4260: Standard Liability -- Premium Summary Missing on Quote Page

        // discounts
        if (quote.getQuoteTotalDiscountsSurcharges() != (quote.getSquirePremiumSummaryAmount("Section II", false, false, true))) {
            testFailed = true;
            errorMessage = errorMessage + "Total discount : " + quote.getQuoteTotalDiscountsSurcharges() + " is not displayed correctly. \n";
        }


        //Net premium
        if (quote.getQuoteTotalGrossPremium() != (quote.getSquirePremiumSummaryAmount("Section II", false, true, false))) {
            testFailed = true;
            errorMessage = errorMessage + "Total Net Policy Premium : " + quote.getQuoteTotalNetPremium() + " is not displayed correctly. \n";
        }

        //Gross premium
        if (quote.getQuoteTotalGrossPremium() != (quote.getSquirePremiumSummaryAmount("Section II", true, false, false))) {
            testFailed = true;
            errorMessage = errorMessage + "Total Gross Policy Premium : " + quote.getQuoteTotalGrossPremium() + " is not displayed correctly. \n";
        }

        if (testFailed)
            Assert.fail(errorMessage);
    }


}


