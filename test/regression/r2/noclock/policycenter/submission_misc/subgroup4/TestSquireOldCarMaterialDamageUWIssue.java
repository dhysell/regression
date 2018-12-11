package regression.r2.noclock.policycenter.submission_misc.subgroup4;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

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
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :DE4199: FA Old Car material damage UW Issue wrong message
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Nov 22, 2016
 */
public class TestSquireOldCarMaterialDamageUWIssue extends BaseTest {
    private GeneratePolicy myPolicyObjPL;

    private WebDriver driver;

    @Test()
    public void testCreateFarmRanchSquireFA() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK);
        coverages.setUninsuredLimit(UninsuredLimit.CSL300K);
        coverages.setUnderinsuredLimit(UnderinsuredLimit.CSL300K);

        SquirePersonalAuto myAuto = new SquirePersonalAuto();
        myAuto.setCoverages(coverages);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locToAdd.setPlNumResidence(6);
        locationsList.add(locToAdd);

        SquireLiability liabilitySection = new SquireLiability();
        liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
        SectionIICoverages sectionIIcoverage = new SectionIICoverages(SectionIICoveragesEnum.PrivateLandingStrips, 0, 1);
        liabilitySection.getSectionIICoverageList().add(sectionIIcoverage);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.squirePA = myAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("SQ", "UWIssue")
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testCreateFarmRanchSquireFA"})
    private void testValidateUWIssue() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObjPL.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();


        sideMenu.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
        vehiclePage.editVehicleByType(this.myPolicyObjPL.squire.squirePA.getVehicleList().get(0).getVehicleType());

        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        Date oldDate = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Year, -21);
        int yearField = DateUtils.dateFormatAsInt("yyyy", oldDate);
        vehiclePage.setModelYear(yearField);
        vehiclePage.sendArbitraryKeys(Keys.TAB);
        vehiclePage.setFactoryCostNew(20000);
        vehiclePage.sendArbitraryKeys(Keys.TAB);
        vehicleCoverages.clickVehicleCoveragesTab();
        vehicleCoverages.setComprehensive(true);
        vehicleCoverages.sendArbitraryKeys(Keys.TAB);
        vehicleCoverages.setCollision(true);
        vehicleCoverages.clickOK();
        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk.Quote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        String uwIssue = "older Than twenty years with comp and collision";
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();
        String expectedDescription = "in Idaho is older than 20 years and has material damage. Underwriting approval is required to issue.(AU018)";
        String description = "";
        Vehicle currentVehicle = this.myPolicyObjPL.squire.squirePA.getVehicleList().get(0);
        String vehicleName = currentVehicle.getModelYear() + " " + currentVehicle.getMake() + " " + currentVehicle.getModel();
        boolean valueFound = false;
        for (int i = 0; i < risk.getUWIssuesList().size(); i++) {
            String currentUWIssueText = risk.getUWIssuesList().get(i).getText();
            if (!currentUWIssueText.contains("Blocking Submit") && (!currentUWIssueText.contains("Blocking Issuance"))) {

                if (currentUWIssueText.contains(uwIssue)) {
                    description = risk.getUWBlockSubmitDescriptionByUWIssueShortDescription(risk.getUWIssuesList().get(i));
                    valueFound = true;
                    break;
                }
            }
        }
        String errorMessage = "";
        if (valueFound) {
            if (!description.contains(expectedDescription) && !description.contains(vehicleName) && (description.contains("FarmTruckWithSemiTrailer_FBM"))) {
                errorMessage = errorMessage + "Expected uw issue long description : " + expectedDescription + " is not displayed. \n";
            }
        } else {
            errorMessage = errorMessage + "Expected uw issue : " + uwIssue + " is not displayed. \n";
        }

        if (!errorMessage.equals("")) {
            Assert.fail(errorMessage);
        }

    }
}
