package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.Achievers;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.desktop.DesktopProofOfMailPC;
import repository.pc.desktop.DesktopSideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;

/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description ENSURE THE USER GET A CANCELATION LETTER FOR EACH BUILDING WITH A DIFFERENT LOAN NUMBER
* @DATE Sep 20, 2018
*/
@Test(groups= {"ClockMove"})
public class DE6064_POM_LH_Printing extends BaseTest {

	public GeneratePolicy myPolicyObj = null;
	private WebDriver driver;

	@Test(enabled=true)
	public void pom_LHDocumentPrinting() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		
		PolicyLocation location1 = new PolicyLocation(new AddressInfo(true));
		PolicyLocation location2 = new PolicyLocation(new AddressInfo(true));
		PolicyLocation location3 = new PolicyLocation(new AddressInfo(true));
		
		
		AdditionalInterest ai1 = new AdditionalInterest(ContactSubType.Company);
		ai1.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		ai1.setLoanContractNumber("LN666");
		
		
		PolicyLocationBuilding building1 = new PolicyLocationBuilding();
		building1.getAdditionalInterestList().add(ai1);
		PolicyLocationBuilding building2 = new PolicyLocationBuilding();
		building2.getAdditionalInterestList().add(ai1);
		
		location1.getBuildingList().add(building1);
		location1.getBuildingList().add(building2);
		
		AdditionalInterest ai2 = new AdditionalInterest(ai1);
		ai2.setLoanContractNumber("LN667");
		
		PolicyLocationBuilding building3 = new PolicyLocationBuilding();
		building3.getAdditionalInterestList().add(ai2);
		PolicyLocationBuilding building4 = new PolicyLocationBuilding();
		building4.getAdditionalInterestList().add(ai2);
		
		location2.getBuildingList().add(building3);
		location2.getBuildingList().add(building4);
		
		
		AdditionalInterest ai3 = new AdditionalInterest(ai1);
		ai3.setLoanContractNumber("LN668");
		
		PolicyLocationBuilding building5 = new PolicyLocationBuilding();
		building5.getAdditionalInterestList().add(ai3);
		PolicyLocationBuilding building6 = new PolicyLocationBuilding();
		building6.getAdditionalInterestList().add(ai3);
		
		location3.getBuildingList().add(building5);
		location3.getBuildingList().add(building6);
		
		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine();
		boLine.locationList.add(location1);
		boLine.locationList.add(location2);
		boLine.locationList.add(location3);

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withAdvancedSearch()
				.withBusinessownersLine(boLine)
				.withDownPaymentType(PaymentType.Credit_Debit)
				.build(GeneratePolicyType.PolicyIssued);
		
		
		
		
		//LOG INTO BC AND MOVE CLOCK TO INVOICE DUE DATE
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
		new Login(driver).loginAndSearchAccountByAccountNumber("sbrunson", "gw", ai1.getLienholderNumber());
		new BCAccountMenu(driver).clickAccountMenuInvoices();
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(new AccountInvoices(driver).getListOfDueDates().get(0), DateAddSubtractOptions.Day, 1));
		
		//RUN BATCHES
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Partial_Nonpay_Cancel_Documents);
		
		
		//VERIFY CORRECT NUMBER OF DOCUMENTS DISPLAY ON PROOF OF MAIL
		new Login(driver).login("hhill", "gw");
		new DesktopSideMenuPC(driver).clickProofOfMail();
		new DesktopProofOfMailPC(driver).clickCommercialLinesProofOfMailTab();
		List<WebElement> pomDocuemnts = new GenericWorkorder(driver).finds(By.xpath("//div[contains(text(), '" + myPolicyObj.pniContact.getName() + "')]/ancestor::tr[1]/descendant::a[contains(text(), 'IWBC1000051112')]"));
		
		//FAIL IF NOT CORRECT WITH THIS MESSAGE
		Assert.assertTrue("THE CORRECT NUMBER OF DOCUMENTS DID NOT PRINT FOR THE PARTIAL CANCELS ON THIS POLICY. EXPECTED 3-4(depending on payment type) FOUND " + pomDocuemnts.size() + " FOR ACCOUNT NUMBER " + myPolicyObj.accountNumber, (pomDocuemnts.size() >= 3));
	}
	
	
	
//	private void curlExecuter() {
//		String url = "http://fbmsgw-regr07/pc/service/workorder/import?";
//		String path = "/content/geometrixx/en/toolbar/contacts";
//		
//		//cURL Command: curl -u admin:admin -X POST -F cmd="lockPage" -F path="/content/geometrixx/en/toolbar/contacts" -F "_charset_"="utf-8" http://localhost:4502/bin/wcmcommand
//		
//		//Equivalent command conversion for Java execution
////		String[] command = { "curl", "-u", username + ":" + password, "-X", "POST", "-F", "cmd=unlockPage", "-F",
////				"path=" + path, "-F", "_charset_=utf-8", url };
//		String[] command = { "curl", "-X", "POST", "-F", "file=@export.xml", url, "username=gw&password=gw"};
//
//		ProcessBuilder process = new ProcessBuilder(command);
//		Process p;
//		try {
//			p = process.start();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			StringBuilder builder = new StringBuilder();
//			String line = null;
//			while ((line = reader.readLine()) != null) {
//				builder.append(line);
//				builder.append(System.getProperty("line.separator"));
//			}
//			String result = builder.toString();
//			System.out.print(result);
//
//		} catch (IOException e) {
//			System.out.print("error");
//			e.printStackTrace();
//		}
//	}
	
	
	
	
	
}
