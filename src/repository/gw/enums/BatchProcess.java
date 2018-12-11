package repository.gw.enums;

public enum BatchProcess {
	//Policy Center Batches
	Additional_Interest_Billing("AdditionalInterestBilling", "Additional Interest Billing", null),
	Agent_Information_Update("AgentAdminUpdate", "Agent Information Update", null),
	Apply_Pending_Account_Data_Updates("ApplyPendingAccountDataUpdates", "Apply Pending Account Data Updates", null),
	Archive_Policy_Terms("ArchivePolicyTerm", "Archive policy terms", WorkQueue.Archive_Policy_Term),
	Audit_Task("AuditTask", "AuditTask", WorkQueue.Audit_Task),
	Bound_Policy_Exception("BoundPolicyException", "Bound policy Exception", WorkQueue.Bound_Policy_Exception),
	Bulk_Agent_Change(null, "Bulk Agent Change", null),
	Closed_Policy_Exception("ClosedPolicyException", "Closed Policy Exception", WorkQueue.Closed_Policy_Exception),
	Create_Documents("CreateDocuments", "CreateDocuments", null),
	County_Office_Update("CountyOfficeUpdate", "County / Office update", null),
	Extract_Rating_Worksheets("ExtractWorksheets", "Extract Rating Worksheets", WorkQueue.Extract_Worksheets),
	Finish_Quoted_Cancellations("FinishQuotedCancellations", "Finish Quoted Cancellations", null),
	Form_Text_Data_Delete("FormTextDataDelete", "Form Text Data Delete", null),
	Impact_Testing_Export("ImpactTestingExport", "Impact Testing Export", WorkQueue.Impact_Testing_Export),
	Impact_Testing_Test_Case_Preparation("ImpactTestingTestPrep", "Impact Testing Test Case Preparation", WorkQueue.Impact_Testing_TestPrep),
	Impact_Testing_Test_Case_Run("ImpactTestingTestRun", "Impact Testing Test Case Run", WorkQueue.Impact_Testing_Test_Run),
	Intran_File_Process("IntranFileProcess", "Intran File Process", null),
	Intran_File_Process_Monthly("IntranFileProcessMonthly", "Intran File Process Monthly", null),
	Job_Expire("JobExpire", "JobExpire", WorkQueue.Job_Expire),
	Jump_Start_Renewals("JumpStartRenewals", "Jump Start Renewals", null),
	KeyPay_MAM_File("KeyPayMAMFile", "KeyPay MAM File", null),
	Mutual_Boiler_Re_Monthly(null, "Mutual Boiler Re Monthly", null),
	Mutual_Boiler_Re_Quarterly(null, "Mutual Boiler Re Quarterly", null),
	Open_Policy_Exception("OpenPolicyException", "Open Policy Exception", WorkQueue.Open_Policy_Exception),
	Overdue_Premium_Report("OverduePremiumReport", "Overdue Premium Report", WorkQueue.Overdue_Premium_Report),
	Payment_Transaction_History("PaymentTransHistoryExt", "Payment Transaction History", null),
	Partial_Nonpay_Cancel("PartialNonpayCancel", "Partial Nonpay Cancel", null),
	Partial_Nonpay_Cancel_Documents("PartialNonpayCancelDocuments", "Partial Nonpay Cancel Documents", null),
	Policy_Hold_Job_Evaluation("PolicyHoldJobEval", "Policy Hold Job Evaluation", WorkQueue.Policy_Hold_Job_Evaluation),
	Policy_Numbering_CSV_Export("PolicyCSVExport", "Policy Numbering CSV Export", null),
	Policy_Renewal_Start("PolicyRenewalStart", "Policy Renewal Start", WorkQueue.Policy_Renewal_Start),
	Premium_Ceding("PremiumCeding", "PremiumCeding", WorkQueue.Premium_Ceding),
	Print_Documents("PrintDocuments", "Print Documents", null),
	Renewal_Photos_Needed("RenewalPhotosNeeded", "Renewal Photos Needed", null),
	Reset_Purge_Status_and_Check_Dates("ResetPurgeStatusAndCheckDates", "Reset Purge Status and Check Dates", WorkQueue.Reset_Purge_Status_And_Check_Dates),
	Retire_Activities("ActivityRetire", "Retire Activities", null),
	Trigger_Activities("TriggerActivities", "Trigger Activities", null),
	Quote_Expire("Quote Expire","Quote Expire" , null),
	Remove_Saved_PaymentInfo("Remove Saved Payment Info", "Remove Saved Payment Info", null),
	Retrieve_Policy_Terms(null, "Retrieve policy terms", null),
	Review_Case("ReviewCase", "Review Case", null),
	Send_Commission_File("SendCommissionFile", "Send Commission File", null),
	Solr_Data_Import("SolrDataImport", "Solr Data Import", null),
	Team_Screens("TeamScreens", "Team Screens", null),
	Test_XML_Generator("TestXmlGenerator", "Test Xml Generator", null),
	Transition_Renewal("TransitionRenewal", "Transition Renewal", null),
	PC_Workflow("Workflow", "Workflow", WorkQueue.Workflow),
	Work_Item_Set_Purge("WorkItemSetPurge", "WorkItemSetPurge", null),
	Work_Queue_Instrumentation_Purge("WorkQueueInstrumentationPurge", "WorkQueueInstrumentationPurge", null),
		
	//Billing Center Batches
	Account_Inactivity("AccountInactivity", "Account Inactivity", WorkQueue.Account_Inactivity), 
	Advance_Expiration("AdvanceExpiration", "Advance Expiration", WorkQueue.Advance_Expiration), 
	//Agency_Suspense_Payment("AgencySuspensePayment", "AgencySuspensePayment", WorkQueue.Agency_Suspense_Payment),
	Automatic_Disbursement("AutomaticDisbursement", "Automatic Disbursement", WorkQueue.Automatic_Disbursement),
	Charge_ProRata_Tax("ChargeProRataTx", "Charge ProRata Tx", WorkQueue.Charge_Pro_Rata_Tx),
	Collateral_Requirement_Effective("CollEffective", "Collateral Requirement Effective", null),
	Collateral_Requirement_Expiration("CollExpiration", "Collateral Requirement Expiration", null),
	Commission_Payable("CmsnPayable", "Commission Payable Caclulations", WorkQueue.Commission_Payable),
	Commission_Payment("CommissionPmt", "Commission Payment", WorkQueue.Commission_Payment),
	Contact_Auto_Sync("ContactAutoSync", "ContactAutoSync", null),
	Disbursement("Disbursement", "Disbursement", WorkQueue.Disbursement),
	Funds_Allotment("FundsAllotment", "Allot Funds for Funds Tracking", WorkQueue.Funds_Allotment),
	FBM_Agent_Update_Process("FBMAgentUpdateProcess", "FBM Agent Update Process", null),
	FBM_Capture_Daily_Account_Balances("FBMCaptureDailyAccountBalances", "FBM Capture Daily Account Balances", null),
	FBM_County_Office_Update("CountyOfficeUpdate", "FBM County / Office update", null),
	FBM_Dues_Installment_Batch("FBMDuesInstallment", "FBM Dues Installment Batch", null),
	FBM_End_Of_Term_Batch_Process("FBMEndOfTermBatchProcess", "FBM End Of Term Batch Process", null),
	FBM_Convert_Account_To_Policy_Level("FBMConvertAccountToPolicyLevel", "FBM Convert Account to Policy Level", null),
	FBM_Lienholder_Account_Migration("FBMLienholderAccountMigration", "FBM Lienholder Account Migration", null),
	FBM_Lienholder_Automatic_Disbursement("FBMLienholderAutomaticDisbursement", "FBM Lienholder Automatic Disbursement", null),
	//FBM_Disburse_LienHolder_Suspense_Items("FBMDisburseLienHolderSuspenseItems", "FBM Disburse Lienolder Suspense Items"/*"FBM Disburse LienHolder Suspense Items"*/, null),
	FBM_Disbursements_To_SunGard("FBMDisbursementsToSunGard", "FBM DisbursementsToSunGard", null),
	FBM_Intran_Account_Balance("FBMIntranAccountBalance", "FBM Intran Account Balance", null),
	FBM_Intran_File_Process("FBMIntranFileProcess", "FBM Intran File Process", null),
	
	FBM_Membership_Transactions_Batch("FBMMembershipTransactionsBatch", "FBM Membership Transactions Batch", null),
	FBM_OlieBankInformation_Import("FBMOlieBankInformationImport", "FBM OlieBankInformation Import", null),

	FBM_Trigger_NACHA_File_Process("FBMTriggerNACHAFileProcess", "FBM Trigger NACHA File Process", null),
	FBM_Trigger_Nexus_Payments_To_BC("FBMTriggerNexusPaymentsToBC", "FBM Trigger Nexus Payments To BC", null),
	FBM_Write_TSI_FIle_Process("FBMWriteTSIFIleProcess", "FBM Write TSI FIle Process", null),
	FBM_Carry_Forward("FBMCarryForward", "FBM Carry Forward", null),
	Full_Pay_Discount("FullPayDiscount", "Full Pay Discount", WorkQueue.Full_Pay_Discount),
	Invoice("Invoice", "Invoice", WorkQueue.Invoice), 
	Invoice_Due("InvoiceDue", "Invoice Due", WorkQueue.Invoice_Due),
	Legacy_Agency_Bill("LegacyAgencyBill", "Legacy Agency Bill", WorkQueue.Legacy_Agency_Bill),
	Legacy_Collateral("LegacyCollateral", "Legacy Collateral", null),
	Legacy_Delinquency("LegacyDelinquency","Legacy Delinquency", WorkQueue.Legacy_Delinquency),
	Legacy_Letter_Of_Credit("LegacyLetterOfCredit", "Legacy Letter Of Credit", WorkQueue.Legacy_Letter_Of_Credit),
	Letter_Of_Credit("LetterOfCredit", "Letter Of Credit", WorkQueue.Letter_Of_Credit),
	New_Payment("NewPayment", "New Payment", WorkQueue.New_Payment),
	Payment_Request("PaymentRequest", "Payment Request", WorkQueue.Payment_Request),
	Policy_Closure("PolicyClosure", "Policy Closure", WorkQueue.Policy_Closure),
	Premium_Report_Report_Due("PremiumReportReportDue", "Premium Reporting Report Due", WorkQueue.Premium_Report_Due),
	Producer_Payment("ProducerPayment", "Producer Payment", WorkQueue.Producer_Payment),
	Receivable_Aging("ReceivableAging", "Receivable Aging", null),
	Release_Charge_Holds("ReleaseChargeHolds", "Release Charge Holds", WorkQueue.Release_Charge_Holds),
	Release_Trouble_Ticket_Holds("ReleaseTtktHoldTypes", "Release Trouble Ticket Hold Types", WorkQueue.Release_Trouble_Ticket_Hold_Types),
	Statement_Billed("StatementBilled", "Statement Billed", WorkQueue.Statement_Billed),
	Statement_Due("StatementDue", "Statement Due", WorkQueue.Statement_Due),
	//Suspense_Payment("SuspensePayment", "Suspense Payment", WorkQueue.Suspense_Payment),
	Trouble_Ticket_Escalation("TroubleTicketEsc", "Trouble Ticket Escalation", WorkQueue.Trouble_Ticket_Escalation),
	Upgrade_Billing_Instruction_Payment_Plan("UpgradeBillingInstructionPaymentPlan", "Upgrade Billing Instruction Payment Plan", WorkQueue.Upgrade_Billing_Instruction_Payment_Plan),
	BC_Workflow("Workflow", "Workflow Writer", WorkQueue.Workflow),
	
	FBM_Membership_21_Day_Document("FBMMembership21DayDocument", "FBM Membership 21 Day Document", WorkQueue.FBM_Membership_21_Day_Document),
	
	// Claim Center Batch Processes
	CC_ManualChecksFile("ManualChecks","Mainframe Manual Checks File",null),
	Check_Register_Unconfirmed("CheckRegisterUnconfirmed","Check Register Unconfirmed",null),
	
	// Contact Manager Batch Processes
	//Membership_Dues_Update has been disabled. 
	Membership_Dues_Update(null,"Membership Dues Update",null),
	DuplicateContactsFinder(null,"Duplicate Contacts Finder",null),
	
	//Batches That Span Centers
	Activity_Escalation("ActivityEsc", "Activity Escalation", WorkQueue.Activity_Escalation),
	Database_Consistency_Check("DBConsistencyCheck", "Database Consistency Check", WorkQueue.DB_Consistency_Check),
	Database_Statistics("DBStats", "Database Statistics", WorkQueue.DB_Stats),
	Geocode_Writer("Geocode","Geocode Writer", null),
	Group_Exception("GroupException", "Group Exception", WorkQueue.Group_Exception),
	Phone_Number_Normalizer("PhoneNumberNormalizer", "Phone number normalizer", WorkQueue.Phone_Number_Normalizer),
	Process_Completion_Monitor("ProcessCompletionMonitor", "Process Completion Monitor", null),
	Process_History_Purge("ProcessHistoryPurge", "ProcessHistoryPurge", null),
	Purge("Purge", "Purge", WorkQueue.Purge),
	Purge_Cluster_Members("PurgeClusterMembers", "Purge Cluster Members", null),
	Purge_Failed_Work_Items("PurgeFailedWorkItems", "Purge Failed Work Items", null),
	User_Exception("UserException", "User Exception", WorkQueue.User_Exception);
	
	String quickJumpValue;
	String serverToolsValue;
	WorkQueue correspondingWorkQueue;
	
	BatchProcess(String quickJumpValue, String serverToolsValue, WorkQueue correspondingWorkQueue){
		this.quickJumpValue = quickJumpValue;
		this.serverToolsValue = serverToolsValue;
		this.correspondingWorkQueue = correspondingWorkQueue;
	}
	
	public String getQuickJumpValue(){
		return this.quickJumpValue;
	}
	
	public String getServerToolsValue(){
		return this.serverToolsValue;
	}
	
	public WorkQueue getCorrespondingWorkQueue(){
		return this.correspondingWorkQueue;
	}
}
