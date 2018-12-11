package scratchpad.jon.mine;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;

import repository.driverConfiguration.Config;
import repository.gw.enums.BuildingClassCode;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.search.SearchAccountsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.globaldatarepo.entities.Agents;

public class GetBldClassCodesWithQuestions extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    Agents oldAgent = null;
    Agents newAgent = null;

    BuildingClassCode[] possiblevalues = BuildingClassCode.values();

    private WebDriver driver;

    @Test(enabled = false)

    public void generatePolicy() throws Exception {

        System.out.println(possiblevalues[0]);
        System.out.println(possiblevalues[0].getClassification());


        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withCreateNew(CreateNew.Create_New_Only_If_Does_Not_Exist)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("ServiceAgent Change")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);

        oldAgent = myPolicyObj.agentInfo;

        System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentFirstName() + " " + myPolicyObj.agentInfo.getAgentLastName());
        System.out.println(myPolicyObj.agentInfo.getAgentNum());
    }


    @Test()
    public void printFailures() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        login.login("mgarman", "gw");
        SearchAccountsPC search = new SearchAccountsPC(driver);
        search.searchAccountByAccountNumber("241550");
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickWorkOrderbySuffix("001");
        GenericWorkorderPolicyInfo pi = new GenericWorkorderPolicyInfo(driver);
        pi.clickEditPolicyTransaction();
        //selectOKOrCancelFromPopup(OkCancel.OK);
        SideMenuPC sideMenuStuff = new SideMenuPC(driver);
        sideMenuStuff.clickSideMenuBuildings();


        for (BuildingClassCode code : possiblevalues) {
            GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
            buildings.clickBuildingsBuildingEdit(1);
            buildings.selectFirstBuildingCodeResult(code.getClassification());
            buildings.clickOK();

            List<WebElement> building = buildings.finds(By.xpath("//a[contains(text(), 'Edit')]"));

            if (building.isEmpty()) {
                System.out.println(code.getClassification());
                buildings.find(By.xpath("//span[contains(text(), 'Cancel')]/parent::span")).click();
                buildings.selectOKOrCancelFromPopup(OkCancel.OK);
            }
        }


    }
	
	
	
	/*
	Barber Shops
	Beauty Parlors and Hair Styling Salons - Including Nail Salons
	Casual Dining Restaurants - Bistros, Brasseries and Cafes - With No Sales of Alcoholic Beverages
	Casual Dining Restaurants - Bistros, Brasseries and Cafes - With Sales of Alcoholic Beverages - Up to 50% of Total Sales
	Casual Dining Restaurants - Diners - With No Sales of Alcoholic Beverages
	Casual Dining Restaurants - Diners - With Sales of Alcoholic Beverages - Up to 50% of Total Sales
	Casual Dining Restaurants - Family Style - With No Sales of Alcoholic Beverages
	Casual Dining Restaurants - Family Style - With Sales of Alcoholic Beverages - Up to 50% of Total Sales
	Convenience Food Stores - With Fast Food Restaurant - No Gasoline Sales
	Convenience Food Stores - With Fast Food Restaurant - With Gasoline Sales
	Convenience Food Stores - With Gasoline Sales - No Restaurant
	Convenience Food Stores - With Limited Cooking Restaurant - No Gasoline Sales
	Convenience Food Stores - With Limited Cooking Restaurant - With Gasoline Sales
	Convenience Food Stores - Without Gasoline Sales - No Restaurant
	Dairy Products or Butter and Egg Stores - Including Ice Cream
	Estheticians - Skin Care Specialist
	Fast Food Restaurants - Cafes
	Fast Food Restaurants - Cafeteria Style - Buffet
	Fast Food Restaurants - Chicken
	Fast Food Restaurants - Concession Stands/Snack Bars
	Fast Food Restaurants - Delicatessens and Sandwich Shops
	Fast Food Restaurants - Donut Shops - Including Baking With Frying Operations
	Fast Food Restaurants - Drive-ins/Service in Car
	Fast Food Restaurants - Drug Stores
	Fast Food Restaurants - Hamburger/Malt Shops
	Fast Food Restaurants - Hotdog Shops
	Fast Food Restaurants - Oriental Style (Chinese, Japanese, Thai, etc.)
	Fast Food Restaurants - Other Ethnic Style (Italian, Mexican, Greek, etc.)
	Fast Food Restaurants - Pizza Shops
	Fast Food Restaurants - Roast Beef
	Fast Food Restaurants - Seafood
	Fast Food Restaurants - Take Out Only Restaurants - No On-Premises Consumption of Food
	Fine Dining Restaurants - With No Sales of Alcoholic Beverages
	Fine Dining Restaurants - With Sales of Alcoholic Beverages - More Than 30% up to 75%
	Fine Dining Restaurants - With Sales of Alcoholic Beverages - Up to 30% of Total Sales
	Grocery Stores - With an Area at Least 4,000 Square Feet - With Gasoline Sales
	Grocery Stores - With an Area at Least 4,000 Square Feet - Without Gasoline Sales
	Grocery Stores - With an Area Less Than 4,000 Square Feet - With Gasoline Sales
	Grocery Stores - With an Area Less Than 4,000 Square Feet - Without Gasoline Sales
	Health or Natural Food Stores - With an Area at Least 4,000 Square Feet
	Health or Natural Food Stores - With an Area Less Than 4,000 Square Feet
	Limited Cooking Restaurants - Coffee Bars or Shops
	Limited Cooking Restaurants - Concession Stands/Snack Bars
	Limited Cooking Restaurants - Delicatessens and Sandwich Shops
	Limited Cooking Restaurants - Donut Shops
	Limited Cooking Restaurants - Drive-ins/Service in Car
	Limited Cooking Restaurants - Drug Stores
	Limited Cooking Restaurants - Ice Cream and Yogurt Stores
	Limited Cooking Restaurants - Pizza Shops
	Limited Cooking Restaurants - Salad Bars
	Limited Cooking Restaurants - Take Out Only Restaurants - No On-Premises Consumption of Food
	Limited Cooking Restaurants Cafes
	Massage Therapy
	Motels No Restaurants
	Motels With Fast Food Restaurants
	Motels With Limited Cooking Restaurants
	Supermarkets - With an Area at Least 4,000 Square Feet - With Gasoline Sales
	Supermarkets - With an Area at Least 4,000 Square Feet - Without Gasoline Sales
	Supermarkets - With an Area Less Than 4,000 Square Feet - With Gasoline Sales
	Supermarkets - With an Area Less Than 4,000 Square Feet - Without Gasoline Sales
	*/


}

















