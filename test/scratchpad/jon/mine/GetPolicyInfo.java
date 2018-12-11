package scratchpad.jon.mine;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import persistence.globaldatarepo.entities.BOPSignOff;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

public class GetPolicyInfo extends BaseTest {

    String environmentToTest = "QA";

    public List<String> accountList = new ArrayList<String>() {{
        this.add("006689");
        this.add("058198");
        this.add("081864");
        this.add("099460");
        this.add("210350");
        this.add("218560");
        this.add("229821");
        this.add("241369");
        this.add("241499");
        this.add("241518");
        this.add("241673");
        this.add("003680");
        this.add("012572");
        this.add("105726");
        this.add("148425");
        this.add("171294");
        this.add("241367");
        this.add("004180");
        this.add("076995");
        this.add("083677");
        this.add("101128");
        this.add("174131");
        this.add("236880");
        this.add("252250");
        this.add("252303");
        this.add("071430");
        this.add("104193");
        this.add("105960");
        this.add("209503");
        this.add("273127");
        this.add("273164");
        this.add("273165");
        this.add("053120");
        this.add("109129");
        this.add("156296");
        this.add("170783");
        this.add("170797");
        this.add("178101");
        this.add("191096");
        this.add("216222");
        this.add("223549");
        this.add("237390");
    }};
    public BOPSignOff accountInfo = null;


    private WebDriver driver;


    @Test(enabled = true)
    public void getPolicyInfo() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter, environmentToTest);
        driver = buildDriver(cf);

        new Login(driver).login("hhill", "gw");

        for (String accountNumbers : accountList) {

            try {
                BOPSignOff acountInfo = new BOPSignOff();
                String accountNumber;
                String accountName;
                String accountAgent;
                String accountUw;
                String accountExpirationDate;
                int numberOfLocations;
                String numberOfBuildingsPer = "";
                String leinHolderName = "";
                String leinHolderAccount = "";
                String loanNumber = "";
//			String paymentType;
                String accountStatus;

                SearchPoliciesPC search = new SearchPoliciesPC(driver);
                search.searchPolicyByAccountNumber(accountNumbers);

                accountNumber = search.find(By.xpath("//div[contains(@id, ':Policy_Summary_AccountDV:Number-inputEl')]")).getText();
                accountName = search.find(By.xpath("//div[contains(@id, ':AccountName-inputEl')]")).getText();
                accountAgent = search.find(By.xpath("//div[contains(@id, ':ProducerCode-inputEl')]")).getText();
                accountUw = search.find(By.xpath("//div[contains(@id, ':Policy_Summary_PolicyDV:Underwriter-inputEl') or contains(@id, ':PolicyInfoUnderwriterInputSet:Underwriter-inputEl')]")).getText();
                accountExpirationDate = search.find(By.xpath("//div[contains(@id, ':PolicyPerExpirDate-inputEl')]")).getText();
                accountStatus = search.find(By.xpath("//span[contains(@id, ':StatusAndExpDate-btnInnerEl')]/span")).getText();

                search.clickWhenClickable(search.find(By.xpath("//td[contains(@id, ':PolicyMenuItemSet_Locations')]")));


                numberOfLocations = search.finds(By.xpath("//div[contains(@id, ':LocationsEdit_DP:LocationsEdit_LV-body')]/div/table/tbody/child::tr")).size();

                search.clickWhenClickable(search.find(By.xpath("//td[contains(@id, ':PolicyMenuItemSet_Buildings')]")));

                int numofLocations = search.finds(By.xpath("//div[contains(@id, ':BOPLocationBuildingsPanelSet:BOPLocationsLV-body')]/div/table/tbody/child::tr")).size();


                for (int i = 1; i <= numofLocations; i++) {
                    //click location row
                    search.clickWhenClickable(search.find(By.xpath("//div[contains(@id, ':BOPLocationBuildingsPanelSet:BOPLocationsLV-body')]/div/table/tbody/child::tr[" + i + "]")));
                    //get number of builidngs
                    int numberOfBuildingsPerLocation = search.finds(By.xpath("//div[contains(@id, ':BOPLocationBuildingsLV-body')]/div/table/tbody/child::tr")).size();
                    //for each buildng
                    numberOfBuildingsPer = numberOfBuildingsPer + String.valueOf(numberOfBuildingsPerLocation);
                    for (int j = 1; j <= numberOfBuildingsPerLocation; j++) {
                        search.clickWhenClickable(search.find(By.xpath("//div[contains(@id, ':BOPLocationBuildingsPanelSet:BOPLocationBuildingsLV-body')]/div/table/tbody/child::tr[" + j + "]")));
                        boolean building = !search.finds(By.xpath("//div[text()='Building']")).isEmpty();
                        boolean bpp = !search.finds(By.xpath("//div[text()='Business Personal Property']")).isEmpty();
                        if (building && bpp) {
                            numberOfBuildingsPer = numberOfBuildingsPer + "Q";
                        } else if (building) {
                            numberOfBuildingsPer = numberOfBuildingsPer + "B";
                        } else if (bpp) {
                            numberOfBuildingsPer = numberOfBuildingsPer + "P";
                        }

                        if (!search.finds(By.xpath("//div[contains(@id, ':AdditionalInterestLV-bod')]/div/table/tbody/child::tr")).isEmpty()) {
                            numberOfBuildingsPer = numberOfBuildingsPer + "+";
                            leinHolderName = leinHolderName + search.find(By.xpath("//div[contains(@id, ':AdditionalInterestLV-bod')]/div/table/tbody/child::tr[1]/child::td[2]/div/a")).getText() + ",";
                            loanNumber = loanNumber + search.find(By.xpath("//div[contains(@id, ':AdditionalInterestLV-bod')]/div/table/tbody/child::tr[1]/child::td[4]/div")).getText() + ",";
                            search.clickWhenClickable(search.find(By.xpath("//div[contains(@id, ':AdditionalInterestLV-bod')]/div/table/tbody/child::tr[1]/child::td[2]/div/a")));
                            leinHolderAccount = leinHolderAccount + search.find(By.xpath("//div[contains(@id, ':LienholderNumber-inputEl')]")).getText() + ",";
                            search.clickWhenClickable(search.find(By.xpath("//a[contains(@id, 'EditAdditionalInterestPopup:__crumb__')]")));
                        }
                    }
                    numberOfBuildingsPer = numberOfBuildingsPer + ":";
                }

                acountInfo.setAccountAgent(AgentsHelper.getAgentByAgentNumber(accountAgent.replaceAll("\\D+", "")).getAgentUserName());
                acountInfo.setAccountExpirationDate(accountExpirationDate);
                acountInfo.setAccountName(accountName);
                acountInfo.setAccountNumber(accountNumber);
                acountInfo.setAccountStatus(accountStatus);
                acountInfo.setAccountUw(UnderwritersHelper.getUnderwriterInfoByFullName(accountUw).getUnderwriterUserName());
                acountInfo.setNumberOfLocations(String.valueOf(numberOfLocations));
                acountInfo.setNumberOfBuildingsPer(numberOfBuildingsPer);
                acountInfo.setLeinHolderName(leinHolderName);
                acountInfo.setLeinHolderAccount(leinHolderAccount);
                acountInfo.setLoanNumber(loanNumber);

                BOPSignOff.createNew(acountInfo);
            } catch (Exception e) {
                System.out.println("FAILED ON " + accountNumbers);
                new GuidewireHelpers(driver).logout();
                new Login(driver).login("hhill", "gw");
            }

        }
    }


    @Test(enabled = true, dependsOnMethods = {"getPolicyInfo"})
    public void getPaymentPlan() throws Exception {
        List<BOPSignOff> bopObjects = BOPSignOff.getAllAccounts();
        String paymentPlan = "";

        Config cf = new Config(ApplicationOrCenter.BillingCenter, environmentToTest);
        driver = buildDriver(cf);
        new Login(driver).login("sbrunson", "gw");

        for (BOPSignOff bop : bopObjects) {

            BCSearchPolicies searchbc = new BCSearchPolicies(driver);
            searchbc.searchPolicyByAccountNumber(bop.getAccountNumber());

            paymentPlan = searchbc.find(By.xpath("//div[contains(@id, ':PolicyDetailDV:PaymentPlan-inputEl')]")).getText();
            BOPSignOff.updatePaymentplan(bop, paymentPlan);


        }
    }


}











