package previousProgramIncrement.pi2_062818_090518.nonFeatures.Triton;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.wizards.EditSplitMoveChargeWizard;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.TransactionNumber;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.ArrayList;
/**
* @Author JQU
* @Requirement 	US14139 -- Edit/Split move allows charges to be moved between companies
* 							Ensure that the user cannot transfer a charge group to a different company. (Req. 18-13-14)
* 				
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/_layouts/15/WopiFrame2.aspx?sourcedoc=/billingcenter/Documents/Release%202%20Requirements/18%20-%20UI%20-%20Account%20Screens/18-13%20Edit%20Split%20Move%20Screen/18-13%20Edit%20Split%20Move%20Screen.docx&action=default">18-13 Edit Split Move Screen</a>
* @DATE August 09, 2018
*/
public class US14139EditSplitNotAllowChargesMovingBetweenCompanies extends BaseTest{
	private GeneratePolicy myPolicyBOP = null, myPolicyPL = null;
	private ARUsers arUser = new ARUsers();
	private WebDriver driver;
	private Config cf;
	private String lienholderNumber;
	private String loanNumber = "LN12345";
	private String errorMessage = "Charge cannot be paid by a Western Community company payer";
	

	private void generateBOPWithLH() throws Exception {		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building1=new PolicyLocationBuilding();
		
//		cf = new Config(ApplicationOrCenter.ContactManager);
//		driver = buildDriver(cf);
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.ContactManager));
		
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);
//        driver.quit();
		AdditionalInterest loc1Property1AdditionalInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
		loc1Property1AdditionalInterest.setLienholderNumber(myContactLienLoc1Obj.lienNumber);
		loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		loc1Property1AdditionalInterest.setLoanContractNumber(loanNumber);
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();		
		loc1LNBldg1AdditionalInterests.add(loc1Property1AdditionalInterest);
		building1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		locOneBuildingList.add(building1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
		
//		cf = new Config(ApplicationOrCenter.PolicyCenter);
//		driver = buildDriver(cf);
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));

		myPolicyBOP = new GeneratePolicy.Builder(driver)
			.withInsPersonOrCompany(ContactSubType.Company)
			.withInsCompanyName("EditSplitTest")
			.withPolOrgType(OrganizationType.Partnership)
			.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
			.withPolicyLocations(locationsList)
			.withPaymentPlanType(PaymentPlanType.Annual)
			.withDownPaymentType(PaymentType.Cash)
			.build(GeneratePolicyType.PolicyIssued);	
		getQALogger().info("The quoted account number is: " + myPolicyBOP.accountNumber);
		lienholderNumber = myPolicyBOP.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
//		driver.quit();		
	}
	private void generatePLPolicy() throws Exception {			
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();			
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		
		locOnePropertyList.add(location1Property1);
		locationsList.add(new PolicyLocation(locOnePropertyList));		

		SquireLiability liabilitySection = new SquireLiability();		
		
		
//		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
//		driver = buildDriver(cf);
		

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;        

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Squire", "Insured")
				.build(GeneratePolicyType.PolicyIssued);				
	}
	@Test
	public void moveChargesAndVerify() throws Exception {	
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		generateBOPWithLH();
		generatePLPolicy();

        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);

        driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
        
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyPL.accountNumber);
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		AccountCharges charge = new AccountCharges(driver);
		charge.clickEditSplitMoveCharges(myPolicyPL.accountNumber, TransactionNumber.Premium_Charged, TransactionType.Policy_Issuance, myPolicyPL.accountNumber);
		
		EditSplitMoveChargeWizard editCharge = new EditSplitMoveChargeWizard(driver);
		
		editCharge.clickAdd();
		editCharge.fillOutEditSplitMoveChargeTable(1, lienholderNumber, "Charge Group", loanNumber, null, myPolicyPL.squire.getPremium().getInsuredPremium());
		editCharge.clickUpdate();
		String alertFound = editCharge.getAlert();
		if(!alertFound.contains(errorMessage)){
			Assert.fail("There should be error message but it wasn't found. The message should be: "+ errorMessage);
		}
	}
}
