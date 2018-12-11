package regression.r2.noclock.policycenter.cancellation;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsPaidDuesAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.PolicyCharges;
import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionNumber;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AbUserHelper;
@QuarantineClass
public class FlatCancelRefundDues extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPol = null;
	
	private void createPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty prop1 = new PLPolicyLocationProperty();
		prop1.setpropertyType(PropertyTypePL.ResidencePremises);
		prop1.setConstructionType(ConstructionTypePL.Frame);
		locOnePropertyList.add(prop1);
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);

		locToAdd.setPlNumAcres(11);
		locToAdd.setPlNumResidence(1);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.liabilitySection = myLiab;
        property.locationList = locationsList;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = property;


        this.myPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("FormInference", "SectionI")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		
	}

	private boolean checkABForDuesRecord() throws Exception{
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		AbUsers user = AbUserHelper.getRandomDeptUser("Number");
		new Login(driver).login(user.getUserName(), user.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchByAccountNumber(myPol.accountNumber);

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsPaidDuesLink();

        ContactDetailsPaidDuesAB duesPage = new ContactDetailsPaidDuesAB(driver);
		duesPage.clickContactDetailsPaidDuesEditLink();
        return duesPage.findDuesRowInTableByText(DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager))) > 0;
	}

	private void flatCancelPolicy() throws Exception{
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPol.underwriterInfo.getUnderwriterUserName(), myPol.underwriterInfo.getUnderwriterPassword(), myPol.accountNumber);
        StartCancellation flatCancel = new StartCancellation(driver);
		flatCancel.cancelPolicy(CancellationSourceReasonExplanation.SubmittedOnAccident, "Flat Cancel", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), true);
	}
	
	@Test
	public void ensureOffsetCharge() throws Exception{
		createPolicy();
		if(!checkABForDuesRecord()){
			Assert.fail("A dues record should be created in ContactManager for dues assigned in PolicyCenter.");
		}
		flatCancelPolicy();		 
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPol.squire.getPolicyNumber());
		

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuCharges();


        PolicyCharges charges = new PolicyCharges(driver);
	
		try {   
			int rowNumber = new TableUtils(driver).getRowNumberFromWebElementRow(charges.getChargesOrChargeHoldsPopupTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), myPol.accountNumber + "-001", TransactionNumber.Membership_Dues_Charged, ChargeCategory.Membership_Dues , TransactionType.Cancellation, null, null, null, null, 43.00, "Flat Cancel", null, null, null, null, null));
			getQALogger().info("The Flat Cancel Dues offset Charge was found on row number " + rowNumber + " .");
		} catch (Exception e) {
			Assert.fail("The Flat Cancel Dues offset Charge was not found in BC yet. Check Account: " + myPol.accountNumber);
		}
		
		new GuidewireHelpers(driver).logout();	}
}
