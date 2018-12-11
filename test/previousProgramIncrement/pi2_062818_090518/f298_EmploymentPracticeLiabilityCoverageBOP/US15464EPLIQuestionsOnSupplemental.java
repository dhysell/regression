package previousProgramIncrement.pi2_062818_090518.f298_EmploymentPracticeLiabilityCoverageBOP;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.EmploymentPracticesLiabilityInsuranceIfApplicantIsA;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.QuoteType;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineIncludedCoverages;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSupplemental;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import repository.pc.workorders.submission.SubmissionProductSelection;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author swathiAkarapu
 * @Requirement : US15464: BOP EPLI Supplemental Questions  displayed when Employment Practice Liability coverage selected on Additional Coverage selected
 * @RequirementsLink https://rally1.rallydev.com/#/203558454824ud/detail/userstory/231412189632
 * @Description : Check that the appropriate questions are available on Supplemental Screen and answerable per acceptance criteria
 * 1.	BP-Q59 is a yes/no question regarding employees in different listed states
 * 2.	BP-Q60 - is a choice question
 * @DATE July 20, 2018
 */
@Test(groups = {"ClockMove"})
public class US15464EPLIQuestionsOnSupplemental extends BaseTest {
    private WebDriver driver;
    private AddressInfo address;

    @Test(enabled = false)
    public void testSupplementalEPLIQuestionsDisplayed() throws Exception {
        SideMenuPC sideMenu = initiateBOPAndNavigateToSupplementalScreen(true);

        GenericWorkorderSupplemental supplemental = new GenericWorkorderSupplemental(driver);
        //Verify both Questions present
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Assert.assertTrue(guidewireHelpers.isRequired("Does applicant/insured have employees in Arkansas, California, Louisiana, New Mexico, or Vermont?"), "Does applicant/insured have employees in Arkansas, California, Louisiana, New Mexico, or Vermont?- Question not displayed on page");
        Assert.assertTrue(guidewireHelpers.isRequired("Indicate if the applicant/insured is a:"), "Indicate if the applicant/insured is a: - Question not displayed on page");
        //verify required statements present
        supplemental.clickNext();
        SoftAssert softassert = new SoftAssert();

        softassert.assertFalse(guidewireHelpers.isOnPage("//span[contains(@class, 'g-title') and (text()='Supplemental')]"),
                "NEXT BUTTON  is Navigated to next page of Supplemental when required fields not answered");
        softassert.assertFalse(guidewireHelpers.isRequired("Please fill in all required fields."), "Please fill in all required fields is not displayed on page");
        supplemental.clickBack();
        // verify blocker message  for 1 st question
        supplemental.radio_EPLI_Does_Application_Have_Employees_In_AK_CA_LA_NewMexico_or_vermont().select(true);
        supplemental.clickNext();
        softassert.assertTrue(guidewireHelpers.isOnPage("//span[contains(@class, 'g-title') and (text()='Supplemental')]"),
                "NEXT BUTTON  is Navigated to next page of Supplemental when blocker message present");
        softassert.assertTrue(guidewireHelpers.isRequired("Applicant/Insured has employees in Arkansas, California, Louisiana, New Mexico, or Vermont. Risk is not eligible for EPLI coverage. Please contact Brokerage."), "Applicant/Insured has employees in Arkansas, California, Louisiana, New Mexico, or Vermont. Risk is not eligible for EPLI coverage. Please contact Brokerage.- message is not displayed on page");
        // verify 1 Question has  correct answer and  can able to move further
        supplemental.radio_EPLI_Does_Application_Have_Employees_In_AK_CA_LA_NewMexico_or_vermont().select(false);

        sideMenu.clickSideMenuRiskAnalysis();

        softassert.assertFalse(guidewireHelpers.isRequired("Applicant/Insured has employees in Arkansas, California, Louisiana, New Mexico, or Vermont. Risk is not eligible for EPLI coverage. Please contact Brokerage."), "Applicant/Insured has employees in Arkansas, California, Louisiana, New Mexico, or Vermont. Risk is not eligible for EPLI coverage. Please contact Brokerage.- message is  displayed on page");
        softassert.assertTrue(guidewireHelpers.isOnPage("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]"),
                "Blocking the Navigation when No Blockers present");
        // 2nd Question blocker message
        sideMenu.clickSideMenuSupplemental();
        supplemental.selectEPLI_If_Applicant_Is_a(EmploymentPracticesLiabilityInsuranceIfApplicantIsA.PRIVATE_GOLF_CLUB);
        supplemental.clickNext();
        softassert.assertTrue(guidewireHelpers.isOnPage("//span[contains(@class, 'g-title') and (text()='Supplemental')]"),
                "NEXT BUTTON  is Navigated to next page of Supplemental when blocker message present");
        softassert.assertTrue(guidewireHelpers.isRequired("Applicant/Insured business type is not eligible for EPLI coverage. Contact Brokerage for coverage."), "Applicant/Insured business type is not eligible for EPLI coverage. Contact Brokerage for coverage.- message is not displayed on page");

        // verify 2nd Question correct answer and  can able to move further
        supplemental.selectEPLI_If_Applicant_Is_a(EmploymentPracticesLiabilityInsuranceIfApplicantIsA.NONE_OF_THE_ABOVE);
        supplemental.clickNext();
        sideMenu.clickSideMenuRiskAnalysis();

        softassert.assertFalse(guidewireHelpers.isRequired("Applicant/Insured has employees in Arkansas, California, Louisiana, New Mexico, or Vermont. Risk is not eligible for EPLI coverage. Please contact Brokerage."), "Applicant/Insured has employees in Arkansas, California, Louisiana, New Mexico, or Vermont. Risk is not eligible for EPLI coverage. Please contact Brokerage.- message is  displayed on page");
        softassert.assertTrue(guidewireHelpers.isOnPage("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]"),
                "Blocking the Navigation when No Blockers present");
        softassert.assertAll();
    }


    @Test(enabled = false)
    public void testSupplementalEPLIQuestionsNotDisplayed() throws Exception {
        initiateBOPAndNavigateToSupplementalScreen(false);
        SoftAssert softassert = new SoftAssert();
        GenericWorkorderSupplemental supplemental = new GenericWorkorderSupplemental(driver);
        //verify Questions are not displayed when EPLI coverage is not selected
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        softassert.assertFalse(guidewireHelpers.isRequired("Does applicant/insured have employees in Arkansas, California, Louisiana, New Mexico, or Vermont?"), "Does applicant/insured have employees in Arkansas, California, Louisiana, New Mexico, or Vermont?- Question is displayed on page when EPLI coverage not present");
        softassert.assertFalse(guidewireHelpers.isRequired("Indicate if the applicant/insured is a:"), "Indicate if the applicant/insured is a: - Question is displayed on page when EPLI coverage not present");
        // verify its not blocking the flow
        supplemental.clickNext();
        softassert.assertFalse(guidewireHelpers.isOnPage("//span[contains(@class, 'g-title') and (text()='Supplemental')]"),
                "NEXT BUTTON  is not Navigated to next page of Supplemental");
        softassert.assertAll();

    }

    private SideMenuPC initiateBOPAndNavigateToSupplementalScreen(boolean epliCoverage) throws Exception {
        Agents agent = AgentsHelper.getRandomAgent();
        address = new AddressInfo();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        if (DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter).before(DateUtils.convertStringtoDate("10/1/2018", "MM/dd/yyyy"))) {
            ClockUtils.setCurrentDates(driver, DateUtils.convertStringtoDate("10/1/2018", "MM/dd/yyyy"));
        }
        new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());
        TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
        menuPolicy.clickNewSubmission();

        SubmissionNewSubmission newSubmissionPage = new SubmissionNewSubmission(driver);
        new GenericWorkorder(driver).clickWhenClickable(By.xpath("//span[contains(@id, ':ContactSearchScreen:dbSearchTab-btnEl')]")); 
        newSubmissionPage.fillOutFormSearchCreateNewWithoutStamp(true, ContactSubType.Company, null, null, null,
                "BOPEPLI " + StringsUtils.generateRandomNumberDigits(7), address.getCity(), address.getState(),
                address.getZip());
        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
        createAccountPage.fillOutPrimaryAddressFields(address);
        createAccountPage.setSubmissionCreateAccountBasicsAltID(StringsUtils.generateRandomNumberDigits(12));
        createAccountPage.clickCreateAccountUpdate();
        SubmissionProductSelection selectProductPage = new SubmissionProductSelection(driver);
        selectProductPage.startQuoteSelectProductAndGetAccountNumber(QuoteType.QuickQuote,
                ProductLineType.Businessowners);
        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickNext();

        GenericWorkorderBusinessownersLineIncludedCoverages boLineInclCovPage = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
        boLineInclCovPage.setSmallBusinessType(BusinessownersLine.SmallBusinessType.Apartments);
        boLineInclCovPage.clickBusinessownersLine_AdditionalCoverages();
        GenericWorkorderBusinessownersLineAdditionalCoverages additionalCoverages = new GenericWorkorderBusinessownersLineAdditionalCoverages(driver);
        PolicyBusinessownersLine policyBusinessownersLine = new PolicyBusinessownersLine();
        PolicyBusinessownersLineAdditionalCoverages additionalCoverageStuff = new PolicyBusinessownersLineAdditionalCoverages(false, true);
        additionalCoverageStuff.setEmploymentPracticesLiabilityInsurance(epliCoverage);
        policyBusinessownersLine.setAdditionalCoverageStuff(additionalCoverageStuff);
        additionalCoverages.fillOutOtherLiabilityCovergaes(policyBusinessownersLine);
        polInfo.clickNext();

        GenericWorkorderLocations location = new GenericWorkorderLocations(driver);
        PolicyLocation locToAdd = new PolicyLocation();
        locToAdd.setPlNumAcres(12);
        locToAdd.setAddress(address);
        location.addNewLocationAndBuildings(true, locToAdd, true, true);
        polInfo.clickNext();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSupplemental();
        return sideMenu;
    }

}
