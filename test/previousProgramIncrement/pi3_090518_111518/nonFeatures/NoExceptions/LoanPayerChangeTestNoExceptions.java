package previousProgramIncrement.pi3_090518_111518.nonFeatures.NoExceptions;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.common.BCCommonActivities;
import repository.bc.common.BCCommonMenu;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.ArrayList;
import java.util.Date;

@Test(groups = {"ClockMove", "BillingCenter"})
public class LoanPayerChangeTestNoExceptions extends BaseTest {

  private GeneratePolicy generatePolicy;
  private WebDriver driver;

  private ARUsers arUser = new ARUsers();
  private GenerateContact generateContact;

    @Test
    public void testGenerateFullyLienBilledBOPPolicy() throws Exception {

      Config cf = new Config(ApplicationOrCenter.ContactManager);
      driver = buildDriver(cf);

      ArrayList<repository.gw.enums.ContactRole> rolesToAdd = new ArrayList<repository.gw.enums.ContactRole>();
      rolesToAdd.add(repository.gw.enums.ContactRole.Lienholder);

      generateContact = new GenerateContact.Builder(driver)
              .withCompanyName("LH FullLienBilled")
              .withRoles(rolesToAdd)
              .withGeneratedLienNumber(true)
              .withUniqueName(true)
              .build(GenerateContactType.Company);
      driver.quit();

      cf = new Config(ApplicationOrCenter.PolicyCenter);
      driver = buildDriver(cf);

      ArrayList<repository.gw.generate.custom.AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<repository.gw.generate.custom.AdditionalInterest>();
      ArrayList<repository.gw.generate.custom.PolicyLocationBuilding> locOneBuildingList = new ArrayList<repository.gw.generate.custom.PolicyLocationBuilding>();
      ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();

      ArrayList<repository.gw.generate.custom.AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<repository.gw.generate.custom.AdditionalInterest>();
      ArrayList<repository.gw.generate.custom.PolicyLocationBuilding> locTwoBuildingList = new ArrayList<repository.gw.generate.custom.PolicyLocationBuilding>();

      ArrayList<repository.gw.generate.custom.AdditionalInterest> loc3Bldg1AdditionalInterests = new ArrayList<repository.gw.generate.custom.AdditionalInterest>();
      ArrayList<repository.gw.generate.custom.PolicyLocationBuilding> locThreeBuildingList = new ArrayList<repository.gw.generate.custom.PolicyLocationBuilding>();

      repository.gw.generate.custom.PolicyLocationBuilding loc1Bldg1 = new repository.gw.generate.custom.PolicyLocationBuilding();
      loc1Bldg1.setClassClassification("storage");

      repository.gw.generate.custom.PolicyLocationBuilding loc2Bldg1 = new repository.gw.generate.custom.PolicyLocationBuilding();
      loc2Bldg1.setClassClassification("storage");

      repository.gw.generate.custom.PolicyLocationBuilding loc3Bldg1 = new repository.gw.generate.custom.PolicyLocationBuilding();
      loc2Bldg1.setClassClassification("storage");

      repository.gw.generate.custom.AdditionalInterest loc1Bld1AddInterest = new repository.gw.generate.custom.AdditionalInterest(generateContact.companyName, generateContact.addresses.get(0));
      loc1Bld1AddInterest.setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling.Bill_Lienholder);
      loc1Bldg1AdditionalInterests.add(loc1Bld1AddInterest);
      loc1Bldg1.setAdditionalInterestList(loc1Bldg1AdditionalInterests);
      loc1Bld1AddInterest.setLoanContractNumber("LOAN321");

      repository.gw.generate.custom.AdditionalInterest loc2Bld1AddInterest = new repository.gw.generate.custom.AdditionalInterest(generateContact.companyName, generateContact.addresses.get(0));
      loc2Bld1AddInterest.setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling.Bill_Lienholder);
      loc2Bldg1AdditionalInterests.add(loc2Bld1AddInterest);
      loc2Bldg1.setAdditionalInterestList(loc2Bldg1AdditionalInterests);
      loc2Bld1AddInterest.setLoanContractNumber("LOAN321");

      repository.gw.generate.custom.AdditionalInterest loc3Bld1AddInterest = new repository.gw.generate.custom.AdditionalInterest(generateContact.companyName, generateContact.addresses.get(0));
      loc3Bld1AddInterest.setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling.Bill_Lienholder);
      loc3Bldg1AdditionalInterests.add(loc3Bld1AddInterest);
      loc3Bldg1.setAdditionalInterestList(loc1Bldg1AdditionalInterests);
      loc3Bld1AddInterest.setLoanContractNumber("LOAN321");


      locOneBuildingList.add(loc1Bldg1);
      locTwoBuildingList.add(loc2Bldg1);
      locThreeBuildingList.add(loc3Bldg1);
      locationsList.add(new repository.gw.generate.custom.PolicyLocation(new repository.gw.generate.custom.AddressInfo(), locOneBuildingList));
      locationsList.add(new repository.gw.generate.custom.PolicyLocation(new repository.gw.generate.custom.AddressInfo(), locTwoBuildingList));
      locationsList.add(new repository.gw.generate.custom.PolicyLocation(new AddressInfo(), locThreeBuildingList));

      generatePolicy = new GeneratePolicy.Builder(driver)
              .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Company)
              .withInsCompanyName("LoanPayerChange")
              .withBusinessownersLine(new repository.gw.generate.custom.PolicyBusinessownersLine(repository.gw.enums.BusinessownersLine.SmallBusinessType.StoresRetail))
              .withPolOrgType(repository.gw.enums.OrganizationType.Partnership)
              .withPolicyLocations(locationsList)
              .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

    }

    @Test(dependsOnMethods = {"testGenerateFullyLienBilledBOPPolicy"})
    public void changeLoanNumberForFirstTime() throws Exception {
      this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
      StartPolicyChange policyChangePage;

      Config cf = new Config(ApplicationOrCenter.PolicyCenter);
      driver = buildDriver(cf);
      Date date = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, 1);
      ClockUtils.setCurrentDates(driver, date);
      new Login(driver).loginAndSearchPolicyByPolicyNumber(generatePolicy.underwriterInfo.getUnderwriterUserName(), generatePolicy.underwriterInfo.getUnderwriterPassword(), generatePolicy.busOwnLine.getPolicyNumber());
      policyChangePage = new StartPolicyChange(driver);
      policyChangePage.startPolicyChange("DescriptionLNChange", this.generatePolicy.busOwnLine.getEffectiveDate());
      SideMenuPC sideMenu = new SideMenuPC(driver);
      sideMenu.clickSideMenuBuildings();
      GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);

      building.clickBuildingsLocationsRow(1);
      building.clickBuildingsBuildingEdit(1);
      GenericWorkorderAdditionalInterests additionalInterest = new GenericWorkorderAdditionalInterests(driver);
      additionalInterest.clickBuildingsPropertyAdditionalInterestsLink(generateContact.companyName);
      additionalInterest.setAdditionalInterestsLoanNumber("LOANCHANGE1");
      additionalInterest.clickBuildingsPropertyAdditionalInterestsUpdateButton();
      building.clickOK();

      building.clickBuildingsLocationsRow(2);
      building.clickBuildingsBuildingEdit(1);
      additionalInterest.clickBuildingsPropertyAdditionalInterestsLink(generateContact.companyName);
      additionalInterest.setAdditionalInterestsLoanNumber("LOANCHANGE2");
      additionalInterest.clickBuildingsPropertyAdditionalInterestsUpdateButton();
      building.clickOK();

      building.clickBuildingsLocationsRow(3);
      building.clickBuildingsBuildingEdit(1);
      additionalInterest.clickBuildingsPropertyAdditionalInterestsLink(generateContact.companyName);
      additionalInterest.setAdditionalInterestsLoanNumber("LOANCHANGE3");
      additionalInterest.clickBuildingsPropertyAdditionalInterestsUpdateButton();
      building.clickOK();

      sideMenu.clickSideMenuPayerAssignment();
      new GenericWorkorderPayerAssignment(driver).setPayerAssignmentVerificationConfirmationCheckbox(true);
      policyChangePage.quoteAndIssue();
      driver.quit();

      cf = new Config(ApplicationOrCenter.BillingCenter);
      driver = buildDriver(cf);
      new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), generatePolicy.accountNumber);

      new BCAccountMenu(driver).clickBCMenuCharges();
      Assert.assertTrue(new AccountCharges(driver).waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(date, repository.gw.enums.TransactionType.Policy_Change, null), "PolicyChange charges has not made to BC");
      Assert.assertTrue(new AccountCharges(driver).verifyCharges(date, null, null, null, null, repository.gw.enums.TransactionType.Policy_Change, null, "DescriptionLNChange" , null ), "PolicyChange charges has not made to BC");
      new BCAccountMenu(driver).clickBCMenuSummary();
      new BCAccountSummary(driver).clickPolicyNumberInOpenPolicyStatusTable(generatePolicy.busOwnLine.getPolicyNumber());
      new BCCommonMenu(driver).clickBCMenuActivities();
      String activitySubject = "Loan Number / Payer Change On Policy "+generatePolicy.busOwnLine.getPolicyNumber();
      BCCommonActivities bcCommonActivities = new BCCommonActivities(driver);
      bcCommonActivities.clickActivityTableSubject(date, null, null, null, activitySubject);

      Assert.assertTrue(bcCommonActivities.getOldLoanNumber().equals("LOAN321"), "Old Loan number is wrong");
      Assert.assertTrue(bcCommonActivities.verifyLoanPayerChanges(date,null,null,null,"LOANCHANGE1"),"Couldn't find the new Loan payer change on this policy change");
      Assert.assertTrue(bcCommonActivities.verifyLoanPayerChanges(date,null,null,null,"LOANCHANGE2"),"Couldn't find the new Loan payer change on this policy change");
      Assert.assertTrue(bcCommonActivities.verifyLoanPayerChanges(date,null,null,null,"LOANCHANGE3"),"Couldn't find the new Loan payer change on this policy change");

    }

  }