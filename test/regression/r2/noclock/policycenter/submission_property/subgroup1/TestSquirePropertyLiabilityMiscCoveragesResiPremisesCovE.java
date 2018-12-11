package regression.r2.noclock.policycenter.submission_property.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
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
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandiabnda
 * @Requirement : US10706 :PL - Misc Coverages to also be available on Residence Premises Cov E with electable Coverage C
 * @Description
 * @DATE Apr 14, 2017
 */
public class TestSquirePropertyLiabilityMiscCoveragesResiPremisesCovE extends BaseTest {

    private GeneratePolicy propertyLiabilityPol = null;
    String lineReviewGunsLimit, lineReviewSilverwareLimit, lineReviewToolsLimit = "", lineReviewSaddleLimit;
    private Underwriters uw;

    private WebDriver driver;

    @Test()
    public void creatingPropertyLiabilityPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty myProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremisesCovE);
        myProperty.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
        locOnePropertyList.add(myProperty);
        PolicyLocation resPrem = new PolicyLocation(locOnePropertyList);
        resPrem.setPlNumAcres(11);
        resPrem.setPlNumResidence(5);
        locationsList.add(resPrem);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        propertyLiabilityPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "PollCov")
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = "creatingPropertyLiabilityPolicy")
    public void validateAgentMiscCoverages() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(propertyLiabilityPol.agentInfo.getAgentUserName(), propertyLiabilityPol.agentInfo.getAgentPassword(), propertyLiabilityPol.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();
        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuSquirePropertyDetail();

        validateMiscCoverages();
    }

    @Test(dependsOnMethods = "creatingPropertyLiabilityPolicy")
    public void validateUWMiscCoverages() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), propertyLiabilityPol.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuSquirePropertyDetail();

        validateMiscCoverages();
    }

    @Test(dependsOnMethods = "creatingPropertyLiabilityPolicy")
    public void validateUWSupervisorMiscCoverages() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), propertyLiabilityPol.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuSquirePropertyDetail();

        validateMiscCoverages();
    }

    public void validateMiscCoverages() throws Exception {
        int newLimit = 20000;
        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);

        coverages.checkGunsMiscellaneousCoverages(true);
        coverages.setGunsLimit(newLimit);

        coverages.checkSilverwareMiscellaneousCoverages(true);
        coverages.setSilverwareLimit(newLimit);

        coverages.checkToolsMiscellaneousCoverages(true);
        coverages.setToolsLimit(newLimit);

        coverages.checkSaddlesTackMiscellaneousCoverages(true);
        coverages.setSaddlesTackLimit(newLimit);

        sideBar.clickSideMenuSquirePropertyGlLineReview();

        GenericWorkorderLineReviewPL lineReview = new GenericWorkorderLineReviewPL(driver);
        int propertyTableRows = lineReview.getPropertyCoverageTableRowCount();

        for (int currentRow = 1; currentRow <= propertyTableRows; currentRow++) {
            String propertyType = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Property Type");
            switch (propertyType) {
                case "Guns Limit":
                    lineReviewGunsLimit = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Limit").replace(",", "");
                    break;
                case "Silverware Limit":
                    lineReviewSilverwareLimit = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Limit").replace(",", "");
                    break;
                case "Tools Limit":
                    lineReviewToolsLimit = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Limit").replace(",", "");
                    break;
                case "Saddles and Tack Limit":
                    lineReviewSaddleLimit = lineReview.getPropertyCoverageTableCellByRowNumberColumnName(currentRow, "Limit").replace(",", "");

                default:
                    break;
            }
        }

        boolean errorsExist = false;
        String errorLog = null;

        if (!Integer.toString(newLimit).equals(lineReviewGunsLimit)) {
            errorLog = errorLog + "Property Guns limit does not match.  " + newLimit + " -  " + lineReviewGunsLimit + " \n";
            errorsExist = true;
        }
        if (!Integer.toString(newLimit).equals(lineReviewSilverwareLimit)) {
            errorLog = errorLog + "Property Silverware Limit does not match.  " + newLimit + " -  " + lineReviewSilverwareLimit + " \n";
            errorsExist = true;
        }
        if (!Integer.toString(newLimit).equals(lineReviewToolsLimit)) {
            errorLog = errorLog + "Property Tools limit does not match.  " + newLimit + " -  " + lineReviewToolsLimit + " \n";
            errorsExist = true;
        }
        if (!Integer.toString(newLimit).equals(lineReviewSaddleLimit)) {
            errorLog = errorLog + "Property Saddle Limit does not match.  " + newLimit + " -  " + lineReviewSaddleLimit + " \n";
            errorsExist = true;
        }

        if (errorsExist)
            Assert.fail(errorLog);

        sideBar.clickSideMenuSquirePropertyCoverages();
        coverages.checkGunsMiscellaneousCoverages(true);
        coverages.checkSilverwareMiscellaneousCoverages(true);
        coverages.checkToolsMiscellaneousCoverages(true);
        coverages.checkSaddlesTackMiscellaneousCoverages(true);
        coverages.clickQuote();
        new GuidewireHelpers(driver).logout();
    }
}
