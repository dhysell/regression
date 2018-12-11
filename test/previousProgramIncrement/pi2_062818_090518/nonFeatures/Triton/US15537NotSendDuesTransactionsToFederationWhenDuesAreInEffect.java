package previousProgramIncrement.pi2_062818_090518.nonFeatures.Triton;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/**
* @Author JQU
* @Requirement 	US15537 - Do not send membership Transactions records to federation for Dues charges that are already in effect
* 				

* @DATE 
* 
* */
@Test(groups = {"ClockMove"})
public class US15537NotSendDuesTransactionsToFederationWhenDuesAreInEffect extends BaseTest{
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private WebDriver driver;
	private double firstPaymentOnDues = NumberUtils.generateRandomNumberInt(5, 15);	
	private String fileLocation = "\\\\fbmsqawizpro01\\guidewire\\billingcenter\\membership\\duesTest";
	private String newFileToRead;

	private void generatePolicyWithDues() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome));
		locationsList.add(new PolicyLocation(locOnePropertyList));

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);
        myStandardFire.section1Deductible = SectionIDeductible.FiveHundred;
        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withStandardFire(myStandardFire)				
				.withInsFirstLastName("Payment", "Location")				
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
	}
	@Test
	public void verifyDocumentToFederation() throws Exception {	
		generatePolicyWithDues();

        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);

        Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        //pay part of dues
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuInvoices();
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		NewDirectBillPayment directBillPayment = new NewDirectBillPayment(driver);
		directBillPayment.makeDirectBillPaymentExecute(firstPaymentOnDues, myPolicyObj.accountNumber);
		
		//delete all old files in federation folder
		//delete all files in a folder
		File folder = new File(fileLocation); /*path to your folder*/
	    File[] filesPresent = folder.listFiles();
	    if(filesPresent.length==0){
	        System.out.println("Nothing to delete");
	    }else{
	    	for(File file: filesPresent) 
	    	    if (!file.isDirectory()) 
	    	        file.delete();
	    }
		//trigger document to federation and verify
		new BatchHelpers(cf).runBatchProcess(BatchProcess.FBM_Membership_Transactions_Batch);
		//verify the new file
		 String[] newCreatedFile = folder.list();
		    if(newCreatedFile.length==0){
		        System.out.println("Nothing to delete");
		    }else if(newCreatedFile.length>1){
		    	
		    }else{
		    
		    	newFileToRead = newCreatedFile[0];
		    }
						
	        BufferedReader br = null;
	        String line = "";
	        String cvsSplitBy = ",";

	        try {

	            br = new BufferedReader(new FileReader(folder+"\\"+newFileToRead));
	            while ((line = br.readLine()) != null) {

	                // use comma as separator
	                String[] cells = line.split(cvsSplitBy);

	                System.out.println("Amount [amount= " + cells[4] + " , next=" + cells[5] + "]");
	                if(cells[5]!=null){
	                	Assert.fail("This cell should be empty.");
	                }
	            }

	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }		
	}
}
