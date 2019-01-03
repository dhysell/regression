package repository.gw.enums;

public enum WorkQueue {
	//PC Work Queue Items
	Activity_Retire("ActivityRetire"),
	Archive_Policy_Term("ArchivePolicyTerm"),
	Audit_Task("AuditTask"),
	Bound_Policy_Exception("BoundPolicyException"),
	Closed_Policy_Exception("ClosedPolicyException"),
	Extract_Worksheets("ExtractWorksheets"),
	Geocode("Geocode"),
	Impact_Testing_Export("ImpactTestingExport"),
	Impact_Testing_TestPrep("ImpactTestingTestPrep"),
	Impact_Testing_Test_Run("ImpactTestingTestRun"),
	Job_Expire("JobExpire"),
	Open_Policy_Exception("OpenPolicyException"),
	Overdue_Premium_Report("OverduePremiumReport"),
	Policy_Hold_Job_Evaluation("PolicyHoldJobEval"),
	Policy_Renewal_Start("PolicyRenewalStart"),
	Premium_Ceding("PremiumCeding"),
	Purge("Purge"),
	Purge_Orphaned_Policy_Period("PurgeOrphanedPolicyPeriod"),
	Purge_Worksheets("PurgeWorksheets"),
	Reset_Purge_Status_And_Check_Dates("ResetPurgeStatusAndCheckDates"),
	Restore_Policy_Term("RestorePolicyTerm"),
	
	//BC Work Queue Items
	Account_Inactivity("AccountInactivity"),
	Advance_Expiration("AdvanceExpiration"),
	//Agency_Suspense_Payment("AgencySuspensePayment"),
	Automatic_Disbursement("AutomaticDisbursement"),
	Charge_Pro_Rata_Tx("ChargeProRataTx"),
	Commission_Payable("CmsnPayable"),
	Collection_Effective("CollEffective"),
	Collection_Expiration("CollExpiration"),
	Commission_Payment("CommissionPmt"),
	Disbursement("Disbursement"),
	Full_Pay_Discount("FullPayDiscount"),
	Funds_Allotment("FundsAllotment"),
	Invoice("Invoice"),
	Invoice_Due("InvoiceDue"),
	Legacy_Agency_Bill("LegacyAgencyBill"),
	Legacy_Delinquency("LegacyDelinquency"),
	Legacy_Letter_Of_Credit("LegacyLetterOfCredit"),
	Letter_Of_Credit("LetterOfCredit"),
	New_Payment("NewPayment"),
	Payment_Request("PaymentRequest"),
	Policy_Closure("PolicyClosure"),
	Premium_Report_Due("PremiumReportReportDue"),
	Producer_Payment("ProducerPayment"),
	Release_Charge_Holds("ReleaseChargeHolds"),
	Release_Trouble_Ticket_Hold_Types("ReleaseTtktHoldTypes"),
	Statement_Billed("StatementBilled"),
	Statement_Due("StatementDue"),
	//Suspense_Payment("SuspensePayment"),
	Trouble_Ticket_Escalation("TroubleTicketEsc"),
	Upgrade_Billing_Instruction_Payment_Plan("UpgradeBillingInstructionPaymentPlan"),
	Writeoff_Staging("WriteoffStaging"),
	
	FBM_Membership_21_Day_Document("FBMMembership21DayDocument"),
	//Work Queue Items that Span Centers
	Activity_Escalation("ActivityEsc"),
	DB_Consistency_Check("DBConsistencyCheck"),
	DB_Stats("DBStats"),
	Group_Exception("GroupException"),
	Phone_Number_Normalizer("PhoneNumberNormalizer"),
	User_Exception("UserException"),
	Workflow("Workflow");
	
	String Value;
	
	private WorkQueue(String Value){
		this.Value = Value;
	}
	
	public String getValue(){
		return this.Value;
	}
}
