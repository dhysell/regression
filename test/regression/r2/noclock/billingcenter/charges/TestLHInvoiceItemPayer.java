package regression.r2.noclock.billingcenter.charges;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.PolicyCharges;
import repository.driverConfiguration.Config;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.ContactRole;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.TransactionStatus;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.desktop.DesktopMyRenewals;
import repository.pc.desktop.DesktopSideMenuPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.TransitionRenewalTeam;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.TransitionRenewalTeamHelper;

/**
 * @Author bhiltbrand
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/50980336161">Rally Defect DE3284</a>
 * @Description This test takes an existing policy in renewal status and performs a policy change to add a LH Building to the policy. It then
 * applies that change to the renewal work order. It then goes to Billing Center and verifies that the charges from the policy change are reflected
 * with the correct payers on the invoice items.
 * @DATE Mar 24, 2016
 */
@QuarantineClass
public class TestLHInvoiceItemPayer extends BaseTest {
	private WebDriver driver;
	private String policyNumber = null;
	private String accountNumber = null;
	private TransitionRenewalTeam transitionRenewalTeamMember;
	private ARUsers arUser;
	private PolicyLocationBuilding newBuilding = null;
	
	@Test()
	public void findPolicyInRenewalStatus() throws Exception {
		this.transitionRenewalTeamMember = TransitionRenewalTeamHelper.getRandomTRMemberWithLoadFactorGreaterThanZero();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).login(this.transitionRenewalTeamMember.getTruserName(), this.transitionRenewalTeamMember.getTrpassword());

		DesktopSideMenuPC sideMenu = new DesktopSideMenuPC(driver);
		sideMenu.clickMyRenewals();

        DesktopMyRenewals renewals = new DesktopMyRenewals(driver);
		this.policyNumber = renewals.getPolicyInRenewal(TransactionStatus.Renewing);
		String[] policyNumberSplit = this.policyNumber.split("-");
		this.accountNumber = policyNumberSplit[1];
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "findPolicyInRenewalStatus" })
	public void addLHBuildingToPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact myContactLienObj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.transitionRenewalTeamMember.getTruserName(), this.transitionRenewalTeamMember.getTrpassword(), this.policyNumber);

        StartPolicyChange policyChange = new StartPolicyChange(driver);
		this.newBuilding = addLienholderBuildingOnHighlightedLocation(myContactLienObj.companyName, myContactLienObj.addresses.get(0), myContactLienObj.lienNumber);
//		policyChange.applyChangeToRenewal(myContactLienObj);
		new GuidewireHelpers(driver).logout();
	}
	
	private PolicyLocationBuilding addLienholderBuildingOnHighlightedLocation(String companyName, AddressInfo address, String lienholderNumber) throws Exception {
		StartPolicyChange policyChange = new StartPolicyChange(driver);
		ArrayList<AdditionalInterest> additionalInterestList = new ArrayList<AdditionalInterest>();
        AdditionalInterest additionalInterest = new AdditionalInterest(companyName, address);
        additionalInterest.setLienholderNumber(lienholderNumber);
        additionalInterestList.add(additionalInterest);
        PolicyLocationBuilding building = new PolicyLocationBuilding(additionalInterestList);

        policyChange.startPolicyChange("Add LH Building", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        GenericWorkorderBuildings buildingsPage = new GenericWorkorderBuildings(driver);
        int buildingsLocationTableRowNumber = new TableUtils(driver).getHighlightedRowNumber(buildingsPage.getSubmissionBuildingsLocationsTable());
        int locationNumber = Integer.valueOf(new TableUtils(driver).getCellTextInTableByRowAndColumnName(buildingsPage.getSubmissionBuildingsLocationsTable(), buildingsLocationTableRowNumber, "Loc. #"));
        PolicyLocationBuilding newBuilding = buildingsPage.addBuildingOnLocation(true, locationNumber, building);

        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPayerAssignment();

        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
        payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(locationNumber, newBuilding.getNumber(), newBuilding.getAdditionalInterestList().get(0).getLienholderPayerAssignmentString(), true, false);

        policyChange.quoteAndIssue();
        return newBuilding;
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Test(dependsOnMethods = { "addLHBuildingToPolicy" })
    public void verifyChargesInBC() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.policyNumber);

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuCharges();

        PolicyCharges charges = new PolicyCharges(driver);
		charges.getChargesOrChargeHoldsPopupTableRow(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), this.newBuilding.getAdditionalInterestList().get(0).getLienholderNumber(), null, ChargeCategory.Premium, TransactionType.Policy_Change, null, null, null, null, null, "Add LH Building", null, this.newBuilding.getAdditionalInterestList().get(0).getLoanContractNumber(), null, null, null).click();
		try {
			charges.getChargesOrChargeHoldsInvoiceItemsTableRow(null, null, null, null, null, this.accountNumber, this.newBuilding.getAdditionalInterestList().get(0).getLienholderNumber(), null, null, null, null);
			getQALogger().info("The correct row was found in the invoice items table on the policy charges page. This means that the payer was set correctly.");
		} catch (Exception e) {
			Assert.fail("The correct row was not found in the invoice items table on the policy charges page. This means that the payer was not set correctly. Test Failed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
}
