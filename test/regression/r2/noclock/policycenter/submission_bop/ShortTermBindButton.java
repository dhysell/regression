package regression.r2.noclock.policycenter.submission_bop;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.Building.CoverageOrNot;
import repository.gw.enums.Building.UtilitiesCoverageAppliesTo;
import repository.gw.enums.Building.UtilitiesUtilityIs;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.PolicyLocationBuildingAdditionalCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.globaldatarepo.entities.Agents;

public class ShortTermBindButton extends BaseTest {

    private GeneratePolicy myPolicyObj;
    private String accountNumber;
    private String agentUsername;
    private String agentPassword;
    private WebDriver driver;

    @Test
    public void generate() throws Exception {

        try {
            // customizing location and building
            ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
            ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
            PolicyLocationBuilding building = new PolicyLocationBuilding();
            PolicyLocationBuildingAdditionalCoverages additionalCoveragesStuff = new PolicyLocationBuildingAdditionalCoverages();

            additionalCoveragesStuff.setUtilitiesDirectDamageCoverage(true);
            additionalCoveragesStuff.setUtilitiesDirectDamageCoverageAppliesTo(UtilitiesCoverageAppliesTo.BuildingAndBusinessPersonalProperty);
            additionalCoveragesStuff.setUtilitiesDirectDamageUtilityIs(UtilitiesUtilityIs.Public);
            additionalCoveragesStuff.setUtilitiesDirectDamageCommunicationsIncOHLines(CoverageOrNot.Covered);
            additionalCoveragesStuff.setUtilitiesDirectDamagePowerIncOHLines(CoverageOrNot.Covered);
            additionalCoveragesStuff.setUtilitiesDirectDamageWaterSupply(CoverageOrNot.Covered);
            building.setAdditionalCoveragesStuff(additionalCoveragesStuff);
            locOneBuildingList.add(building);
            locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

            Config cf = new Config(ApplicationOrCenter.PolicyCenter);
            driver = buildDriver(cf);

            this.myPolicyObj = new GeneratePolicy.Builder(driver)
                    .withInsPersonOrCompany(ContactSubType.Company)
                    .withInsCompanyName("Test Loss of Rental Value")
                    .withPolOrgType(OrganizationType.Partnership)
                    .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                    .withPolicyLocations(locationsList)
                    .withPaymentPlanType(PaymentPlanType.Quarterly)
                    .withDownPaymentType(PaymentType.Cash)
                    .build(GeneratePolicyType.FullApp);

            accountNumber = myPolicyObj.accountNumber;
            Agents agent = myPolicyObj.agentInfo;
            agentUsername = agent.getAgentUserName();
            agentPassword = agent.getAgentPassword();
        } catch (UnhandledAlertException e) {
            System.out.println("Caught and UnhandledAlert and trying to click OK and Quit()");
            driver.switchTo().alert().accept();

            e.printStackTrace();
        }
    }

    /**
     * @throws Exception
     * @Author drichards
     * @Requirement DE2262
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description The bind button should not be available on a short term
     * policy without Payment Info on a monthly payment plan.
     * @DATE Sep 29, 2015
     */
    @Test(dependsOnMethods = {"generate"}, enabled = true)
    public void bindButtonWithoutPaymentInfo() throws Exception {
        try {

            Config cf = new Config(ApplicationOrCenter.PolicyCenter);
            driver = buildDriver(cf);

            new Login(driver).loginAndSearchJob(agentUsername, agentPassword, accountNumber);

            GenericWorkorder workorder = new GenericWorkorder(driver);
            new GuidewireHelpers(driver).editPolicyTransaction();

            GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
            Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
            policyInfoPage.setTermLength(85, currentDate);
            workorder.clickGenericWorkorderQuote();

            SideMenuPC sideMenu = new SideMenuPC(driver);
            sideMenu.clickSideMenuForms();
            sideMenu.clickSideMenuPayment();

            GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);
            if (paymentPage.checkQuarterlyPaymentPlanExists()) {
                paymentPage.clickPaymentPlan(PaymentPlanType.Monthly);
            } else {
                throw new Exception("This test requires a large enough down payment to use a monthly payment plan.");
            }
            double downPayment = paymentPage.getDownPaymentAmount();
            paymentPage.clickAddDownPayment();
            paymentPage.setDownPaymentAmount(downPayment);
            paymentPage.setDownPaymentType(PaymentType.Cash);
            paymentPage.clickOk();

            // check for bind button being usable
            boolean isBindable = workorder.isSubmittable();
            if (isBindable) {
                throw new Exception(
                        "The bind button should be disabled until the Payment Info has been entered for a monthly policy.");
            }
        } catch (UnhandledAlertException e) {
            System.out.println("Caught and UnhandledAlert and trying to click OK and Quit()");
            driver.switchTo().alert().accept();

            e.printStackTrace();
        }

    }

}
