package regression.r2.noclock.policycenter.reinstate;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.ReinstateReason;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.reinstate.StartReinstate;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : DE4321: The Total Gross Premium is wrong
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Jan 18, 2017
 */
@QuarantineClass
public class TestSquireReinstateGrossPremium extends BaseTest {

    public GeneratePolicy mySQPolicyObjPL;
    private Underwriters uw;
    private double changeInCost;

    private WebDriver driver;

    @Test()
    private void testCancelSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LessorPL);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty prop = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        prop.setPolicyLocationBuildingAdditionalInterestArrayList(loc2Bldg1AdditionalInterests);
        locOnePropertyList.add(prop);
        locationsList.add(new PolicyLocation(locOnePropertyList));
        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Day, -90);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "Reinstate")
                .withPolEffectiveDate(newEff)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        new GuidewireHelpers(driver).logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                mySQPolicyObjPL.accountNumber);
        testCancelSquirePolicy(mySQPolicyObjPL.accountNumber);
    }

    @Test(dependsOnMethods = {"testCancelSquirePolicy"})
    public void testReinstatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                mySQPolicyObjPL.accountNumber);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickReinstatePolicy();
        String errorMessage = "";
        StartReinstate reinstatePolicy = new StartReinstate(driver);
        reinstatePolicy.setReinstateReason(ReinstateReason.Payment_Received.getValue());
        GenericWorkorder genericWO = new GenericWorkorder(driver);
        genericWO.clickGenericWorkorderQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

        if (quote.getQuoteTotalGrossPremium() != this.mySQPolicyObjPL.squire.getPremium().getTotalGrossPremium()) {
            errorMessage = errorMessage + " Total gross amount is not same.";
        }

        if (quote.getQuoteTotalDiscountsSurcharges() != this.mySQPolicyObjPL.squire.getPremium().getTotalDiscountsSurcharges()) {
            errorMessage = errorMessage + " Total discount/ surchanges amount is not same.";
        }

        if (quote.getQuoteChangeInCost() != -(this.changeInCost)) {
            errorMessage = errorMessage + " Change in cost value is not matched \n";
        }

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }

    private void testCancelSquirePolicy(String accountNumber) {
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        String comment = "Testing Purpose";
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickCancelPolicy();

        cancelPol.setSourceReasonAndExplanation(CancellationSourceReasonExplanation.LossRuns);
        cancelPol.setDescription(comment);
        try {
            cancelPol.setEffectiveDate(currentDate);
        } catch (Exception e) {

        }
        cancelPol.clickStartCancellation();
        cancelPol.clickNext();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        changeInCost = quote.getQuoteChangeInCost();

        cancelPol.clickSubmitOptionsCancelNow();

        // searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByAccountNumber(accountNumber);
        String errorMessage = "";
        PolicySummary summary = new PolicySummary(driver);
        if (summary.getTransactionNumber(TransactionType.Cancellation, comment) == null)
            errorMessage = "Cancellation is not done!!!";

        if (!errorMessage.equals(""))
            Assert.fail(errorMessage);
    }
}
