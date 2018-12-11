package regression.r2.noclock.contactmanager.changes;


import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import com.idfbins.enums.State;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DeliveryOptionType;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

import java.util.ArrayList;

/**
 * @Author sbroderick
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/10075774537ud/detail/defect/53490831504">Delivery Options on 981546 in CM</a>
 * @Description
 * @DATE Mar 31, 2016
 */
public class AddressChangeDeliveryOptions extends BaseTest {
	private WebDriver driver;
    GeneratePolicy myPolicyObjInsuredAndLien;
	private AbUsers abUser;

	//This test will create a policy with a lienholder so we can see how the change in Delivery Option change will affect PC Policy.
    @Test
	public void generateNewPolicy() throws Exception {
	//Get Basic 2 Location with 1 Building Each, 1 Building's Lien With Charges
			ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

//			ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
//			locOneBuildingList.add(new PolicyLocationBuilding());

			ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<PolicyLocationBuilding>();

			ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();

			AddressInfo bankAddress = new AddressInfo();
			bankAddress.setLine1("3696 S 6800 W");
			bankAddress.setCity("West Valley City");
			bankAddress.setState(State.Utah);
			bankAddress.setZip("84128");

        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest("Freedom Bank", new AddressInfo("550 Kinderkamack Road", "Oradell", State.NewJersey, "07649")) {{
            this.setSocialSecurityTaxNum(Integer.toString(NumberUtils.generateRandomNumberInt(111111111, 999999999)));
			}};
			loc2Bldg1AddInterest.setNewContact(CreateNew.Do_Not_Create_New);
			loc2Bldg1AddInterest.setAddress(bankAddress);
			loc2Bldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
			loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);


			PolicyLocationBuilding loc2Bldg1 = new PolicyLocationBuilding();
			loc2Bldg1.setAdditionalInterestList(loc2Bldg1AdditionalInterests);
			locTwoBuildingList.add(loc2Bldg1);
//			locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
			locationsList.add(new PolicyLocation(new AddressInfo(), locTwoBuildingList));

			Config cf = new Config(ApplicationOrCenter.PolicyCenter);
			WebDriver driver = buildDriver(cf);

        this.myPolicyObjInsuredAndLien = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("Delivery Option")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Credit_Debit)
				.build(GeneratePolicyType.PolicyIssued);

        System.out.println("Policy with account Number: " + myPolicyObjInsuredAndLien.accountNumber + " was created by " + myPolicyObjInsuredAndLien.agentInfo.getAgentUserName());
			cf.setCenter(ApplicationOrCenter.ContactManager);
	        driver = buildDriver(cf);
			this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
        Login logMeIn = new Login(driver);
			logMeIn.login(this.abUser.getUserName(), this.abUser.getUserPassword());

        TopMenuAB menu = new TopMenuAB(driver);
        AdvancedSearchAB search = new AdvancedSearchAB(driver);

			menu.clickSearchTab();
			search.clickReset();
        search = new AdvancedSearchAB(driver);
//			search.selectSpecificComboBox_ContactType(ContactSubType.Company.getValue());
			search.searchLienholderNumber(myPolicyObjInsuredAndLien.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber());
			search.clickSearch();
			search.clickAdvancedSearchCompanySearchResults(myPolicyObjInsuredAndLien.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getCompanyName(), myPolicyObjInsuredAndLien.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAddress().getLine1(), myPolicyObjInsuredAndLien.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAddress().getState());

        ContactDetailsBasicsAB policyContact = new ContactDetailsBasicsAB(driver);
			policyContact.clickContactDetailsBasicsEditLink();
			policyContact.clickContactDetailsBasicsAddressLink();
        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
			addressPage.addDeliveryOption(DeliveryOptionType.Attention, "ATTN: KJ RULES!");
			addressPage.clickContactDetailsAddressesUpdate();
			new GuidewireHelpers(driver).logout();

			cf = new Config(ApplicationOrCenter.PolicyCenter);
			driver = buildDriver(cf);
			new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjInsuredAndLien.agentInfo.getAgentUserName(), myPolicyObjInsuredAndLien.agentInfo.getAgentPassword(), myPolicyObjInsuredAndLien.accountNumber);

        SideMenuPC sidebar = new SideMenuPC(driver);
			sidebar.clickSideMenuBuildings();

        GenericWorkorderAdditionalInterests addInterest = new GenericWorkorderAdditionalInterests(driver);
			addInterest.clickBuildingsPropertyAdditionalInterestsLink(myPolicyObjInsuredAndLien.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getCompanyName());
			ArrayList<String> deliveryOptions = addInterest.getDeliveryOptions();
        if (deliveryOptions == null) {
				System.out.println("No delivery Options were found.");
			} else {
            for (String option : deliveryOptions) {
                if (option.equals("ATTN: KJ RULES!")) {
						System.out.println("Delivery Option added to ContactManager is found in AB.");
					} else {
						System.out.println("Delivery Option created in ContactManager is not found in AB");
					}
				}
	}
    }

	@Test(dependsOnMethods = {"generateNewPolicy"})
    public void addDeliveryOptions() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        Login logMeIn = new Login(driver);
		logMeIn.login(this.abUser.getUserName(), this.abUser.getUserPassword());

        TopMenuAB menu = new TopMenuAB(driver);
        AdvancedSearchAB search = new AdvancedSearchAB(driver);

		menu.clickSearchTab();
		search.clickReset();
        search = new AdvancedSearchAB(driver);
//		search.selectSpecificComboBox_ContactType(ContactSubType.Company.getValue());
		search.searchLienholderNumber(myPolicyObjInsuredAndLien.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber());
		search.clickSearch();
		search.clickAdvancedSearchCompanySearchResults(myPolicyObjInsuredAndLien.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getCompanyName(), myPolicyObjInsuredAndLien.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAddress().getLine1(), myPolicyObjInsuredAndLien.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAddress().getState());

        ContactDetailsBasicsAB policyContact = new ContactDetailsBasicsAB(driver);
		policyContact.clickContactDetailsBasicsEditLink();
		policyContact.clickContactDetailsBasicsAddressLink();
        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
		addressPage.addDeliveryOption(DeliveryOptionType.Attention, "ATTN: Ken RULES!");
		addressPage.clickContactDetailsAddressesUpdate();
		ArrayList<String> deliveryOptions = addressPage.getDeliveryOption();
        if (deliveryOptions.get(0).contains("Ken")) {
				Assert.fail("Check the delivery Options order. KJ is awsome should come last.");
		}
		new GuidewireHelpers(driver).logout();
	}


}
