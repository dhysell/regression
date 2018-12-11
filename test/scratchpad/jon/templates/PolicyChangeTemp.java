package scratchpad.jon.templates;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;

import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineIncludedCoverages;
import repository.pc.workorders.generic.GenericWorkorderModifiers;

public class PolicyChangeTemp extends BaseTest {


    GeneratePolicy myPolicyObj = null;
    private WebDriver driver;

    @Test(enabled = true)
    public void createPolicy() throws Exception {

        GenerateTestPolicyTemp policy = new GenerateTestPolicyTemp();
        policy.generateRandomPolicy(1, 2, 1, "Test Policy ", GeneratePolicyType.FullApp);
        this.myPolicyObj = policy.myPolicy;


        System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentUserName());
    }


    /**
     * @Author jlarsen
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description when creating a policy change one right after the other effective same day verify that the second change is not flagged as an OOS change
     * and verify that the changes from the first change are reflected in the second change
     * @DATE Oct 1, 2015
     */
    @Test(dependsOnMethods = {"createPolicy"})
    public void verifySequentialPolicyChanges() throws Exception {
        boolean oosFail = false;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber("hhill", "gw", myPolicyObj.accountNumber);

        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("First policy Change", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBusinessownersLine();

        GenericWorkorderBusinessownersLineIncludedCoverages boLIne = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
        boLIne.setSmallBusinessType(SmallBusinessType.Apartments);

        policyChangePage.quoteAndIssue();

        System.out.println("First Policy Change Done");

        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchAccountByAccountNumber("hhill", "gw", myPolicyObj.accountNumber);

        policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("Second Policy Change", null);

        System.out.println("CHECK IF OOS CHANGE");
        if (new StartPolicyChange(driver).finds(By.xpath("//label[contains(@id, 'PanelSet:Warning')]")).isEmpty()) {
            oosFail = true;
        }


        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBusinessownersLine();

        boLIne = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
        String smallBusinessType = boLIne.getSmallBusinessType();
        System.out.println(smallBusinessType);
        System.out.println(SmallBusinessType.Apartments.getValue());

        if (!SmallBusinessType.Apartments.getValue().contains(smallBusinessType)) {
            Assert.fail(driver.getCurrentUrl() + " Small business type was not set to that of the first policy change: OOS change was displayed = :" + oosFail);
        }


    }


    /**
     * @throws Exception
     * @author jlarsen
     * Requirement - US5387 - Agents should not be able to change modifiers mid term. Please make modifiers page un-editable for agents during a Policy Change work order.
     * Link - 6.6.1 & 6.8.5
     * Description - randomly try to set one(1) of the modifiers values. if it doen't fail to do so test will fail.
     * @DATE - Sep 23, 2015
     */
    @Test(dependsOnMethods = {"createPolicy"}, enabled = false)
    public void editModifiersPage() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("Can I change the Modifiers Page?", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuModifiers();

        GenericWorkorderModifiers modify = new GenericWorkorderModifiers(driver);
        int number = NumberUtils.generateRandomNumberInt(0, 3);

        try {
            switch (number) {
                case 0:
                    System.out.println("Trying to set: Building features");
                    modify.setModifiersBuildingFeaturesCreditDebit(1);
                    Assert.fail(driver.getCurrentUrl() + " " + myPolicyObj.accountNumber + " Building features: On the modifiers page was able to be set midterm.");
                case 1:
                    System.out.println("Trying to set: Premises and equipment");
                    modify.setModifiersPremisesEquipmentCreditDebit(1);
                    Assert.fail(driver.getCurrentUrl() + " " + myPolicyObj.accountNumber + "Premises and equipment: On the modifiers page was able to be set midterm.");
                case 2:
                    System.out.println("Trying to set: Management");
                    modify.setModifiersManagementCreditDebit(1);
                    Assert.fail(driver.getCurrentUrl() + " " + myPolicyObj.accountNumber + "Management: On the modifiers page was able to be set midterm.");
                case 3:
                    System.out.println("Trying to set: Employees");
                    modify.setModifiersEmployeesCreditDebit(1);
                    Assert.fail(driver.getCurrentUrl() + " " + myPolicyObj.accountNumber + "Employees: On the modifiers page was able to be set midterm.");
            }
        } catch (Exception e) {
            //DO NOTHING BECAUSE TEST PASSED
        } finally {
            //withdraw transaction
            GenericWorkorder wo = new GenericWorkorder(driver);
            wo.clickWithdrawTransaction();
            new GuidewireHelpers(driver).selectOKOrCancelFromPopup(OkCancel.OK);
        }


    }


}


















