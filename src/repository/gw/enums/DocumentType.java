package repository.gw.enums;

public enum DocumentType {
	
	//Policy Center document types
	Audit("Audit", ""),
	Binder_Lien_Letters("Binder/Lien Letters", "Lien/Lender Documents (examples: Lender Requests, Proof of coverage from lender)"),
	Clue_Reports("Clue Reports", ""),
	Cost_Estimator("Cost Estimator", ""),
	Drivers_License("Driver's License", "Driver’s License"),
	Inspection_Report("Inspection Report", "Inspection Reports (See FAQs Page)"),
	ISRB_Documents("ISRB Documents", ""),
	Loss_Run("Loss Run", "Claim History Documents"),
	Other_Supporting_Documents("Other Supporting Documents", "Other Supporting Documents"),
	Photos("Photos", "Photos"),
	Prior_Policy_Information("Prior Policy Information", ""),
	Professional_Audit("Professional Audit", ""),
	Questionnaire("Questionnaire", ""),
	Rating_Worksheet("Rating Worksheet", ""),
	Registration("Registration", "Recreational / Vehicle Registration"),
	Reinstatement_Document("Reinstatement Document", ""),
	Renewal_Documents_With_Changes("Renewal Documents with Changes", ""),
	Renewal_Documents_Without_changes("Renewal Documents without changes", ""),
	Secretary_Of_State("Secretary of State", ""),
	Signature_Page("Signature Page", "All Other Signed Documents"),
	Treaty_Exceptions("Treaty Exceptions", ""),
	UMUIM_Rejection_Letter("UM/UIM Rejection Letter", "UM/UIM waiver (See FAQs Page)"),
	Website_Information("Website Information", ""),
	Additional_Interest_Cancel("Additional Interest Cancel", ""),
	
	//Adding the below PL - PC documents 
	Appraisals("Appraisals", "Appraisals (examples: home, jewelry, cars and antiques)"),
	SeniorCitizenDiscount("Senior Citizen Discount", "Mature Driver Discount (See FAQs Page)"),
	GoodStudentDiscount("Good Student Discount", "Good Student Discount (See FAQs Page)"),
	StarMotorcycleCourse("STAR Motorcycle Course", "STAR Motorcycle Discount (See FAQs Page)"),
	LetterOfTestamentary("Letter of Testamentary", ""),
	PowerOfAttorney("Power of Attorney", "Power of Attorney"),
	SR22("SR22", ""),
	SR26("SR26", ""),
	DriverExclusion304("Driver Exclusion Authorization Ends 304", ""),
	OutOfState898("Out of State Information Letter 898", ""),
	ShowCarExclusion303("Show Car Exclusion Authorization Ends 303", ""),
	CommoditiesSheet("Commodities Sheet", "Commodities Sheet"),

	//Billing Center document types
	Ad_Hoc_Document("Ad Hoc Doc", ""),
	Additional_Interest_Bill("Additional Interest Bill", ""),
	Balance_Due_Partial_Cancel("Balance Due Partial Cancel", ""),
	Billing_Error("Billing Error", ""),
    Cash_Only_Letter("Cash Only Letter", ""),
    Cash_Only_Warning_Letter("Cash Only Warning Letter", ""),
	Delinquency("Delinquency", ""),
	Disbursement("Disbursement", ""),
	Dispute("Dispute", ""),
	Email("Email", ""),
	Email_Sent("Email Sent", ""),
	Final_Notice_Balance_Due("Final Notice Balance Due", ""),
	First_Notice_Balance_Due("First Notice Balance Due", ""),
	General("General", ""),
	IR_Fields_Missing_Scanned_Document("IR Fields Missing Scanned Document", ""),
	Notice_Of_Withdrawal("Notice of Withdrawal", ""),
	Monthly_Notice_Of_Withdrawal("Monthly Notice of Withdrawal", ""),
	Other("Other", ""),
	Policy_Transfer("Policy Transfer", ""),
	Receipt_Of_Payment_On_Cancelled_Policy("Receipt of Payment on Cancelled Policy", ""),
	Renewal_Carryover("Renewal Carryover", ""),
	Returned_Check("Returned Check", ""),
	Scheduled_Payment_Plan_Quarterly("Scheduled Payment Plan Quarterly", ""),
	Scheduled_Payment_Plan_Semi_Annual("Scheduled Payment Plan Semiannual", ""),
	Shortage_Bill("Shortage Bill", ""),
	Subrogation("Subrogation", ""),
	Unknown_Scanned_Document("Unknown Scanned Document", ""),


    //Shared document types by Billing Center and Policy Center
	Bank_Information("Bank Information", "Bank Information"),
	Bankruptcy_Papers("Bankruptcy Papers", ""),
	Check_Requests("Check Requests", ""),
	Check_Request("Check Request", ""),
    Disputed_Bank_Statement("Disputed Bank Statement", ""),
    Receipt_Of_Payment("Receipt of Payment", ""),
    Debit_Credit_Voucher("Debit/Credit Voucher", ""),

	
	//ContactManager documentTypes that are unique to AB
	LegalDocument("Legal document", ""),
	LetterReceived("Letter received", ""),
	LetterSent("Letter sent", ""),
	W9("W-9 Document", "");
	
	String value;



	String poratlValue;
	DocumentType(String type, String portalType){
		value = type;
		poratlValue = portalType;
	}
	
	public String getValue(){
		return value;
	}
	public String getPoratlValue() {
		return poratlValue;
	}
}
