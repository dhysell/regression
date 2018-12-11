package previousProgramIncrement.pi1_041918_062718.maintenanceDefects;

import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchAccounts;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.ReinstateReason;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireFPPTypeItem;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.reinstate.StartReinstate;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;

/**
* @Author nvadlamudi
* @Requirement: DE7083: Cancel/Reinstate/Rescind documents not going to Documents from Forms for FPP Additional Interests
*				STEPS: Create a county squire policy with FPP & list at least 1 additional interest
						Quote, Submit & Issue
						Cancel policy for non-payment
						Cancel Now
* @Description: Validate the below FPP - Additional Interest documents are showing up in policy document page
* 					1. Non-Payment Notice - Additional Interest
* 					2. Additional Interest Reinstatement Notice
* 					3. Reinstatement Notice - Additional Interest
* 					4. Additional Interest Rescind Cancel Notice
* 					5. Rescind Cancel - Additional Interest
* 					6. Non Renew Non Payment Cancel - Partial
* @DATE Apr 27, 2018
*/
@Test(groups = {"ClockMove"})
public class CancelReinstateRescindDocumentsForFPPAI extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private String cancelDocument = "Non Payment Notice - Additional Interest";
	private String cancelDocumentUW = "Additional Interest Termination Notice";
	private String rescindDocument = "Additional Interest Rescind Cancel Notice";
	private String reinstatementDocument = "Additional Interest Reinstatement Notice";
	private String rescindDocumentUW = "Rescind Cancel - Additional Interest - UW";
	private String reinstatementDocumentUW = "Reinstatement Notice - Additional Interest - UW";
	private String renewalDocumentBC = "Non Payment Notice - Additional Interest";
	
	@Test
	public void testCancelReinstateRescindFPPAIDocs() throws Exception {
		int RandomJobs = NumberUtils.generateRandomNumberInt(1,5);
		switch (RandomJobs) {
		case 1:
			testGenerateSquirePol(SquireEligibility.Country);
			testValidateCancelAndRescindCancelFPPAIDocs();
			break;
		case 2:
			testGenerateSquirePol(SquireEligibility.Country);
			testCancelReinstatementFPPAIDocs();
			break;
		case 3:
			testGenerateSquirePol(SquireEligibility.FarmAndRanch);
			testCancelRescindFPPAddInterestDocs();
			break;
		case 4:
			testGenerateSquirePol(SquireEligibility.FarmAndRanch);
			testCancelReinstatementFPPAddInterestDocs();
			break;
		case 5:
			testGenerageShortSquireCountryPol();
			testMakeInitialDownpayment();
			testInitialRenewal();
			testRenewalRunBatchesAndClockMove();
			testRenewalPartialCancelDocument();
			break;
		}
	}
	
	
	private void testGenerateSquirePol(SquireEligibility eligibility) throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(12);
		locationsList.add(locToAdd);
		
		ArrayList<AdditionalInterest> fppAdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest fppAddInterest = new AdditionalInterest(ContactSubType.Company);
		fppAddInterest.setAdditionalInterestType(AdditionalInterestType.LessorPL);
		fppAdditionalInterests.add(fppAddInterest);

        SquireFPP squireFPP = new SquireFPP();
		squireFPP.setAdditionalInterests(fppAdditionalInterests);

		SquireFPPTypeItem machinery1 = new SquireFPPTypeItem(squireFPP, FPPFarmPersonalPropertySubTypes.Tractors);
		machinery1.setAdditionalInterest(fppAddInterest);
		machinery1.setDescription("Testing Purpose");
		machinery1.setSerialNumber("13324435435");
		machinery1.setValue(100);
		squireFPP.setItems(machinery1);
		
        
        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.squireFPP = squireFPP;

        Squire mySquire = new Squire(eligibility);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Defect", "DE7083")
				.withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPaymentPlanType(PaymentPlanType.Annual)				
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	private void testValidateCancelAndRescindCancelFPPAIDocs() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolicyObj.squire.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
		
		//Cancel policy and check the document Non-Payment Notice - Additional Interest
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NoPaymentReceived, null, null, false, 200.00);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickViewYourPolicy();
        //checking document
        PolicySummary policySummary = new PolicySummary(driver);
		String cancelTransNumber = policySummary.getTransactionNumber(TransactionType.Cancellation);
		testValidateFPPDocuments(cancelTransNumber,cancelDocument);		
		
		//Rescind Cancellation 
		cancelPol.cancelRescind(true);			
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickViewYourPolicy();		
		
		//checking Rescind Cancel - Additional Interest 
		testValidateFPPDocuments(cancelTransNumber,rescindDocument);

		new GuidewireHelpers(driver).logout();
	}
	
	
	private void testCancelReinstatementFPPAIDocs() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolicyObj.squire.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
		
		//Cancel policy and check the document Non-Payment Notice - Additional Interest
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NoPaymentReceived, null, null, true, 200.00);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickViewYourPolicy();
        PolicySummary policySummary = new PolicySummary(driver);
		testValidateFPPDocuments(policySummary.getCompletedTransactionNumberByType(TransactionType.Cancellation),cancelDocument);		
		
		//Reinstate 
		StartReinstate reinstatePolicy = new StartReinstate(driver);
		reinstatePolicy.reinstatePolicy(ReinstateReason.Payment_Received,"Testing purpose");
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickViewYourPolicy();		
		
		//checking Reinstatement Cancel - Additional Interest 
		testValidateFPPDocuments(policySummary.getCompletedTransactionNumberByType(TransactionType.Reinstatement),reinstatementDocument);
		
		new GuidewireHelpers(driver).logout();
	}
	
	
	private void testCancelRescindFPPAddInterestDocs()  throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolicyObj.squire.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
		
		//Cancel policy and check the document Non-Payment Notice - Additional Interest
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.ThisAccountNotMeetingOurEligibilityRequirements, "Testing purpose", DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 1), false);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickViewYourPolicy();
        PolicySummary policySummary = new PolicySummary(driver);
		String cancelTransNumber = policySummary.getPendingPolicyTransactionByColumnName(TransactionType.Cancellation.getValue(), "Transaction #");
		testValidateFPPDocuments(cancelTransNumber,cancelDocumentUW);		
	
		//Rescind Cancellation 
		cancelPol.cancelRescind(true);	
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickViewYourPolicy();		
		
		//checking Rescind Cancel - Additional Interest 			
		testValidateFPPDocuments(cancelTransNumber,rescindDocumentUW);
		
		new GuidewireHelpers(driver).logout();
	}
	
	
	private void testCancelReinstatementFPPAddInterestDocs()  throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);

		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
		
		//Cancel policy and check the document Non-Payment Notice - Additional Interest
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.ThisAccountNotMeetingOurEligibilityRequirements, "Testing purpose", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), true);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickViewYourPolicy();
		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
        // delay required

        PolicySummary policySummary = new PolicySummary(driver);
		String cancelTransNumber = policySummary.getCompletedTransactionNumberByType(TransactionType.Cancellation);
		testValidateFPPDocuments(cancelTransNumber,cancelDocumentUW);	
		
		//Reinstate 
		StartReinstate reinstatePolicy = new StartReinstate(driver);
    	reinstatePolicy.reinstatePolicy(ReinstateReason.Other,"Testing purpose");
    	new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
    	completePage.clickViewYourPolicy();		
    		
    	//checking Reinstatement Cancel - Additional Interest 
    	testValidateFPPDocuments(policySummary.getCompletedTransactionNumberByType(TransactionType.Reinstatement),reinstatementDocumentUW);
    	
    	new GuidewireHelpers(driver).logout();
	}
		
	
	private void testGenerageShortSquireCountryPol() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc1LNBldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		loc1LNBldg1AddInterest.setLoanContractNumber("LN12345");

		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		PLPolicyLocationProperty plBuilding = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		plBuilding.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		locOnePropertyList.add(plBuilding);
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);
		
		ArrayList<AdditionalInterest> fppAdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest fppAddInterest = new AdditionalInterest(ContactSubType.Company);
		fppAddInterest.setAdditionalInterestType(AdditionalInterestType.LessorPL);
		fppAdditionalInterests.add(fppAddInterest);

        SquireFPP squireFPP = new SquireFPP();
		squireFPP.setAdditionalInterests(fppAdditionalInterests);

		SquireFPPTypeItem machinery1 = new SquireFPPTypeItem(squireFPP, FPPFarmPersonalPropertySubTypes.Tractors);
		machinery1.setAdditionalInterest(fppAddInterest);
		machinery1.setDescription("Testing Purpose");
		machinery1.setSerialNumber("13324435435");
		machinery1.setValue(100);
		squireFPP.setItems(machinery1);
		        
        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.squireFPP = squireFPP;
        
       	SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.squirePA = squirePersonalAuto;


        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Renewal", "DE7083")
				.withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -315))
				.withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withPaymentPlanType(PaymentPlanType.Annual)				
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	private void testMakeInitialDownpayment() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
		newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());

        BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber());

        newPayment = new NewDirectBillPayment(driver);
        newPayment.makeLienHolderPaymentExecute(myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount(), myPolicyObj.squire.getPolicyNumber(), myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber());
		
        new GuidewireHelpers(driver).logout();
	}
	
	private void testInitialRenewal() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolicyObj.accountNumber);
        StartRenewal renewal = new StartRenewal(driver);
        renewal.renewPolicyManually(RenewalCode.Renew_Good_Risk, myPolicyObj);		
        new GuidewireHelpers(driver).logout();
	}
	
	private void testRenewalRunBatchesAndClockMove() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickTopInfoBarAccountNumber();
        
        //move to day -46 and run batches and ensure that your renewal charges show. 
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 4);
        BatchHelpers batchHelpers = new BatchHelpers(cf);
        batchHelpers.runBatchProcess(BatchProcess.Invoice);      
        batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
        batchHelpers.runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);  
        
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 25);
        batchHelpers.runBatchProcess(BatchProcess.Invoice);      
        batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);

        BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
       	accountSearch.searchAccountByAccountNumber(myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber());

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeLienHolderPaymentExecute(myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount(), myPolicyObj.squire.getPolicyNumber(), myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber());
		        
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 24);
        batchHelpers.runBatchProcess(BatchProcess.Invoice);      
        batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
        batchHelpers.runBatchProcess(BatchProcess.BC_Workflow);
        // delay required

        batchHelpers.runBatchProcess(BatchProcess.Partial_Nonpay_Cancel);
        batchHelpers.runBatchProcess(BatchProcess.Partial_Nonpay_Cancel_Documents); 
        batchHelpers.runBatchProcess(BatchProcess.BC_Workflow);
        batchHelpers.runBatchProcess(BatchProcess.PC_Workflow);
        // delay required
        new GuidewireHelpers(driver).logout();
	}
	
	private void testRenewalPartialCancelDocument() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());
        PolicySummary pcSummary = new PolicySummary(driver);
        String transactionEffDate = "";
        transactionEffDate = pcSummary.getPendingCancelTransactionEffectiveDate(1);
		if (transactionEffDate != "") {
			int noOfDays = DateUtils.getDifferenceBetweenDates(
					DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
					DateUtils.convertStringtoDate(transactionEffDate, "MM/dd/yyyy"), DateDifferenceOptions.Day);
			ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, (noOfDays + 2));
			new BatchHelpers(cf).runBatchProcess(BatchProcess.Partial_Nonpay_Cancel_Documents);
			new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);

			//checking Renewal partial Cancel - Additional Interest 
    		testValidateFPPDocuments(pcSummary.getCompletedTransactionNumberByType(TransactionType.Renewal),renewalDocumentBC);
		}else{
			Assert.fail("Pending Cancellation for A/R No-Payment is not displayed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
	
	 private void testValidateFPPDocuments(String type, String documentName) throws Exception {
	        SideMenuPC sideMenu = new SideMenuPC(driver);
	        sideMenu.clickSideMenuToolsDocuments();
         PolicyDocuments docs = new PolicyDocuments(driver);
	        docs.selectRelatedTo(type);
	        docs.clickSearch();
         ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();
	        boolean docFound = false;
	        for (String desc : descriptions) {
	            if (desc.equals(documentName)) {
	            	docFound = true;
	                    break;
	                }
	         }
	        Assert.assertTrue(docFound, "Policy Number: '+ myPolicyObj.squire.getPolicyNumber() +' - FPP Additional Interest Document '"+documentName+"' is not displayed in Documents -> Job '"+type+"'." );
	 }
}
