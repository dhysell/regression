package scratchpad.swathi.cpi;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Test(groups = {"ClockMove"})
public class DE7464AdditionalIntrestWithDifferentAddress extends BaseTest {
    private GeneratePolicy myPolicyObject = null;
    private WebDriver driver;
    private repository.gw.generate.custom.AddressInfo address1;
    private repository.gw.generate.custom.AddressInfo address2;
    private Config cf;
    private void generate() throws Exception {
         cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        address1 = new repository.gw.generate.custom.AddressInfo(true);
        address2 = new repository.gw.generate.custom.AddressInfo(true);

        while(address1.getLine1().equals(address2.getLine1())){
            address2 = new repository.gw.generate.custom.AddressInfo(true);
        }
        ArrayList<repository.gw.generate.custom.AddressInfo> addresses = new ArrayList<AddressInfo>();
        addresses.add(address1);
        addresses.add(address2);
        ArrayList<repository.gw.enums.ContactRole> rolesToAdd = new ArrayList<repository.gw.enums.ContactRole>();
        rolesToAdd.add(repository.gw.enums.ContactRole.Lienholder);
        GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
                .withCompanyName("sameAI")
                .withAddresses(addresses)
                .withGeneratedLienNumber(true)
                .withRoles(rolesToAdd)
                .withUniqueName(true)
                .build(GenerateContactType.Company);

        driver.quit();
        cf = new Config(ApplicationOrCenter.PolicyCenter, "REGR06");
        driver = buildDriver(cf);

        Vehicle toAdd = new Vehicle();
        toAdd.setVehicleTypePL(repository.gw.enums.Vehicle.VehicleTypePL.PrivatePassenger);
        repository.gw.generate.custom.AdditionalInterest autoAI1 = new repository.gw.generate.custom.AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
        autoAI1.setAdditionalInterestType(repository.gw.enums.AdditionalInterestType.LienholderPL);
        autoAI1.setAdditionalInterestSubType(repository.gw.enums.AdditionalInterestSubType.PLSectionIIIAuto);
        ArrayList<repository.gw.generate.custom.AdditionalInterest> autoAIList = new ArrayList<repository.gw.generate.custom.AdditionalInterest>();
        autoAIList.add(autoAI1);
        toAdd.setAdditionalInterest(autoAIList);

        Vehicle toAdd2 = new Vehicle();
        toAdd2.setVehicleTypePL(repository.gw.enums.Vehicle.VehicleTypePL.PrivatePassenger);
        repository.gw.generate.custom.AdditionalInterest autoAI2 = new repository.gw.generate.custom.AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(1));
        autoAI2.setAdditionalInterestType(repository.gw.enums.AdditionalInterestType.LienholderPL);
        autoAI2.setAdditionalInterestSubType(repository.gw.enums.AdditionalInterestSubType.PLSectionIIIAuto);
        ArrayList<repository.gw.generate.custom.AdditionalInterest> autoAIList2 = new ArrayList<repository.gw.generate.custom.AdditionalInterest>();
        autoAIList2.add(autoAI2);
        toAdd2.setAdditionalInterest(autoAIList2);

        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        vehicleList.add(toAdd);
        vehicleList.add(toAdd2);
        repository.gw.generate.custom.SquirePersonalAuto squirePersonalAuto = new repository.gw.generate.custom.SquirePersonalAuto();
        squirePersonalAuto.setVehicleList(vehicleList);

        repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire(repository.gw.enums.SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObject = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL).withInsFirstLastName("diff", "address")
                .withPaymentPlanType(repository.gw.enums.PaymentPlanType.Annual).withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                        repository.gw.enums.DateAddSubtractOptions.Day, -2))
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

    }
    @Test
    public void twoDifferentAddress() throws Exception{

        generate();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);

        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.squire.getPolicyNumber());
        ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Month, 6);
        StartCancellation cancelPol = new StartCancellation(driver);

        cancelPol.cancelPolicy(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.NoPaymentReceived, null, DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                repository.gw.enums.DateAddSubtractOptions.Day, 20), true, 200.00);
        ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 22);
        guidewireHelpers.logout();
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.squire.getPolicyNumber());
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRewriteRemainderOfTerm();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLineSelection();
        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
        while(!guidewireHelpers.isOnPage("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]")){
            eligibilityPage.clickNext();
        }

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll();
        GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
        pcWorkOrder.clickIssuePolicyButton();

        GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
        pcWorkorderCompletePage.clickViewYourPolicy();
        sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments policyDocuments = new PolicyDocuments(driver);
        policyDocuments.selectRelatedTo("Rewrite Remainder");
        policyDocuments.clickSearch();


        TableUtils tableUtils = new TableUtils(driver);

        HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
        columnRowKeyValuePairs.put("Description", "Additional Interest Reinstatement Notice");
        List<WebElement> allRows = tableUtils.getRowsInTableByColumnsAndValues(policyDocuments.getTableDocuments(), columnRowKeyValuePairs);


        List<String> AddressList= new ArrayList<>();
        for (WebElement row :allRows){
            AddressList.add(tableUtils.getCellTextInTableByRowAndColumnName(policyDocuments.getTableDocuments(), row, "Name & Address")) ;
        }

        boolean  address1Found=false;
        boolean  address2Found=false;
        for ( String address : AddressList){
            if (address.contains(address1.getLine1()) && address.contains(address1.getCity()) && address.contains(address1.getZip()) ){
                address1Found=true;
            }
            if (address.contains(address2.getLine1()) && address.contains(address2.getCity()) && address.contains(address2.getZip()) ){
                address2Found=true;
            }
        }
        Assert.assertTrue(address2Found && address1Found , "Additional interest reinstatement notices should have the two different addresses but not ");
    
    
    }
}
