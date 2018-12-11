package regression.r2.noclock.policycenter.submission_misc.subgroup8;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US7573: PL- Umbrella Inception date
 * @RequirementsLink <a href=
 * " http://projects.idfbins.com/policycenter/Documents/Story Cards/01_PolicyCenter/01_User_Stories/PolicyCenter 8.0/Squire/PC8 - Squire - QuoteApplication - Inception Date.xlsx"
 * PC Squire QuoteApplication Inception Date</a>
 * @Description
 * @DATE Sep 9, 2016
 */
@QuarantineClass
public class TestUmbrellaInceptionDate extends BaseTest {
    private GeneratePolicy squireFullApp;
    private Underwriters uw;

    private WebDriver driver;

    @Test()
    public void testCreateUmbrellaFA() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
                MedicalLimit.TenK);
        coverages.setUninsuredLimit(UninsuredLimit.CSL300K);
        coverages.setUnderinsuredLimit(UnderinsuredLimit.CSL300K);

        SquirePersonalAuto myAuto = new SquirePersonalAuto();
        myAuto.setCoverages(coverages);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability liabilitySection = new SquireLiability();
        liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
        SectionIICoverages sectionIIcoverage = new SectionIICoverages(SectionIICoveragesEnum.PrivateLandingStrips, 0, 1);
        liabilitySection.getSectionIICoverageList().add(sectionIIcoverage);

        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Day, -2);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = myAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.squireFullApp = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("Guy", "Inception")
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withPolEffectiveDate(newEff)
                .build(GeneratePolicyType.FullApp);

        new GuidewireHelpers(driver).logout();

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);
        squireUmbrellaInfo.setEffectiveDate(squireFullApp.squire.getEffectiveDate());
        squireFullApp.squireUmbrellaInfo = squireUmbrellaInfo;
        squireFullApp.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.QuickQuote);
    }


    @Test(dependsOnMethods = {"testCreateUmbrellaFA"})
    private void testUmbrellaInceptionDate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.squireFullApp.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.setCheckBoxInInceptionDateByRow(1, true);
        policyInfo.sendArbitraryKeys(Keys.TAB);
        policyInfo.setOverrideCommissionReason(1, "Testing purpose");
        policyInfo.sendArbitraryKeys(Keys.TAB);
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.performRiskAnalysisAndQuoteQQ(squireFullApp);
    }

    @Test(dependsOnMethods = {"testUmbrellaInceptionDate"})
    private void testSquireOverrideCommission() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.squireFullApp.accountNumber);
        AccountSummaryPC aSumm = new AccountSummaryPC(driver);
        aSumm.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickInceptionDateByRowAndColumnHeader(2, "New");
        policyInfo.sendArbitraryKeys(Keys.TAB);
        policyInfo.setOverrideCommissionReason(2, "Testing purpose");
        policyInfo.sendArbitraryKeys(Keys.TAB);
        String errorMessage = "";
        if (policyInfo.isInceptionDateCheckBoxDisplayedByRowColumn(2, "Renewal")) {
            errorMessage = errorMessage + "Renewal Checkbox is still displayed after New checkbox selected \n";
        }
        policyInfo.setCheckBoxInInceptionDateByRowAndColumnHeader(2, "New", false);
        policyInfo.sendArbitraryKeys(Keys.TAB);
        policyInfo.clickInceptionDateByRowAndColumnHeader(2, "Renewal");
        policyInfo.sendArbitraryKeys(Keys.TAB);
        policyInfo.setOverrideCommissionReason(2, "Testing purpose");
        policyInfo.sendArbitraryKeys(Keys.TAB);
        if (policyInfo.isInceptionDateCheckBoxDisplayedByRowColumn(2, "New")) {
            errorMessage = errorMessage + "New Checkbox is still displayed after Renewal checkbox selected \n";
        }

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }
}
