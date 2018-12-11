package regression.r2.noclock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.topmenu.BCTopMenu;
import repository.bc.topmenu.BCTopMenuAccount;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import persistence.globaldatarepo.entities.Underwriters;

/**
 * DE2742 Steps: Generate a
 * policy. Change the expiration date in PolicyCenter. Verify Due dates list in
 * BillingCenter with the calculated due dates list. Make policy change (add
 * coverage). Verify the Due dates list in BillingCenter with the calculated due
 * dates list again. (defect: some due dates change back to original due dates,
 * have duplicate invoices) By:Jessica Qu Date: August 18, 2015
 */
@QuarantineClass
public class VerifyDueDatesAfterTermAndPolicyChange extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
	private String accountNumber;
	private String bcUserName = "sbrunson";
	private String pwd = "gw";
	private String uwUserName;
	private BCAccountMenu acctMenu;
	public Date currentDate, newDate, partialCancelDate;
    private SearchPoliciesPC policySearch;
	private AccountInvoices acctInvoice;
	private Date effectiveDate, expirationDate;
	private List<Date> dueDatesFromBC, calculatedDueDate;
	private PaymentPlanType paymentPlanType;	

	private void loginAndSearch(ApplicationOrCenter product, String acctNum, String userName, String pwd) throws Exception {
		Config cf = new Config(product);
		driver = buildDriver(cf);
		Login myloginPage = new Login(driver);
		myloginPage.login(userName, pwd);
		if (product.getValue().equals("BC")) {
			BCTopMenu topMenu;
			topMenu = new BCTopMenu(driver);
			topMenu.clickAccountArrow();
			BCTopMenuAccount topMenuStuff = new BCTopMenuAccount(driver);
			topMenuStuff.menuAccountSearchAccountByAccountNumber(acctNum);
		} else {// procuct=policycenter
            policySearch = new SearchPoliciesPC(driver);
			policySearch.searchPolicyByAccountNumber(acctNum);
		}
	}

	@Test
	public void generate() throws Exception {
		// Get Basic 1 Location with 1 Building
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());

		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("ExpDateChange")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		accountNumber = myPolicyObj.accountNumber;
		Underwriters underwriter = myPolicyObj.underwriterInfo;
		uwUserName = underwriter.getUnderwriterUserName();
        effectiveDate = myPolicyObj.busOwnLine.getEffectiveDate();
        expirationDate = myPolicyObj.busOwnLine.getExpirationDate();
		paymentPlanType = myPolicyObj.paymentPlanType;		

	}	
	//change a Exp. date to a date before the original exp date
	//randomly generate the days to change.
	@Test(dependsOnMethods = { "generate" })
    public void changeExpirationDateInPC() throws Exception {
		loginAndSearch(ApplicationOrCenter.PolicyCenter, accountNumber, uwUserName, pwd);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		Random rand = new Random();
		int randNum = rand.nextInt(20) + 1;
		randNum *= -1;
		expirationDate = DateUtils.dateAddSubtract(expirationDate, DateAddSubtractOptions.Day, randNum);
		policyChangePage.changeExpirationDate(expirationDate, "Expiration Date Change");
	}

	@Test(dependsOnMethods = { "changeExpirationDateInPC" })	
	public void verifyNewInvoiceDueDatesAfterExpDateChange() throws Exception {
		loginAndSearch(ApplicationOrCenter.BillingCenter, accountNumber, bcUserName, pwd);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		dueDatesFromBC = acctInvoice.getListOfDueDates();
		int i;
		System.out.println("Due date from BC after Exp. Date change are: \n");
		for (i = 0; i < dueDatesFromBC.size(); i++) {
			System.out.println(dueDatesFromBC.get(i) + "\n");
		}
		calculatedDueDate = acctInvoice.calculateListOfDueDates(effectiveDate, expirationDate, paymentPlanType);
		System.out.println("Caculated Due dates after Exp. Date change are: \n");
		for (i = 0; i < calculatedDueDate.size(); i++) {
			System.out.println(calculatedDueDate.get(i) + "\n");
		}
		new GuidewireHelpers(driver).verifyLists(dueDatesFromBC, calculatedDueDate);
	}

	@Test(dependsOnMethods = { "verifyNewInvoiceDueDatesAfterExpDateChange" })
	public void addCoverageInPC() throws Exception {		
		loginAndSearch(ApplicationOrCenter.PolicyCenter, accountNumber, uwUserName, pwd);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
//		policyChangePage.addLocation(myPolicyObj);
		
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        PolicyLocationBuilding policyLocationBuilding = new PolicyLocationBuilding();
        policyLocationBuilding.setUsageDescription("New Location and Building");
        locOneBuildingList.add(new PolicyLocationBuilding());
        addLocation(locOneBuildingList, myPolicyObj);
        
        
        
        
        
	}
	
	
	public void addLocation(ArrayList<PolicyLocationBuilding> locOneBuildingList, GeneratePolicy myPolicyObj)
            throws Exception {
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        PolicyLocation newPolicyLocation = new PolicyLocation(new AddressInfo(true), locOneBuildingList);
        policyChangePage.startPolicyChange("Add Location", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLocations();

        GenericWorkorderLocations location = new GenericWorkorderLocations(driver);
        location.clickLocationsNewLocation();
        location.addNewLocationAndBuildings(true, newPolicyLocation, false, true);

        policyChangePage.quoteAndIssue();

        // update generate object
        switch (myPolicyObj.productType) {
            case Businessowners:
                myPolicyObj.busOwnLine.locationList.add(newPolicyLocation);
                break;
            case CPP:
                myPolicyObj.commercialPackage.locationList.add(newPolicyLocation);
                break;
            case PersonalUmbrella:
                break;
            case Squire:
                myPolicyObj.squire.propertyAndLiability.locationList.add(newPolicyLocation);
                break;
//		case StandardFL:
//			myPolicyObj.standardFireAndLiability.getLocationList().add(newPolicyLocation);
//			break;
            case StandardIM:
                break;
            case Membership:
                break;
            case StandardFire:
                myPolicyObj.standardFire.getLocationList().add(newPolicyLocation);
                break;
            case StandardLiability:
                myPolicyObj.standardLiability.getLocationList().add(newPolicyLocation);
                break;
        }
    }

	@Test(dependsOnMethods = { "addCoverageInPC" })
	public void verifyInvoiceScreenAfterAddCoverage() throws Exception {
		loginAndSearch(ApplicationOrCenter.BillingCenter, accountNumber, bcUserName, pwd);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		dueDatesFromBC = acctInvoice.getListOfDueDates();
		int i;
		System.out.println("Due date from BC after adding coverage in PolicyCenter: \n");
		for (i = 0; i < dueDatesFromBC.size(); i++) {
			System.out.println(dueDatesFromBC.get(i) + "\n");
		}
		calculatedDueDate = acctInvoice.calculateListOfDueDates(effectiveDate, expirationDate, paymentPlanType);
		System.out.println("Caculated Due dates after adding coverage in PolicyCenter: \n");
		for (i = 0; i < calculatedDueDate.size(); i++) {
			System.out.println(calculatedDueDate.get(i) + "\n");
		}
		new GuidewireHelpers(driver).verifyLists(dueDatesFromBC, calculatedDueDate);
	}

}
