package repository.gw.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Permissions_SA {

	Advance_policy_change("advancepolchange"),
	Bind_policy_change("bindpolchange"),
	Create_Date_of_Birth("createdatebirth"),
	Create_documents("doccreate"),
	Create_Gender("creategender"),
	Create_notes("notecreate"),
	Create_payment("paymentcreate"),
	Create_policy_change("createpolchange"),
	Create_renewal("createrenewal"),
	Create_Social_Security_Number("createssn"),
	Edit_account("editaccountsummary"),
	Edit_aAccount_Agents("editacctagents"),
	Edit_account_file_contacts("editaccountcontacts"),
	Edit_Additional_Named_Insured("editAddlNamedInsured"),
	Edit_Building_Additional_Coverage("editBuildingAddlCov"),
	Edit_Building_Additional_Interest("editBuildingAddlInterest"),
	Edit_Building_Characteristics("editbldgchar"),
	Edit_Building_Existing_Damage("editBuildingExistDamage"),
	Edit_Building_Heating("editBuildingHeating"),
	Edit_Building_Plumbing("editBuildingPlumbing"),
	Edit_Building_Roofing("editBuildingRoofing"),
	Edit_BuildingWiring("editBuildingWiring"),
	Edit_Building_Within_100_Feet("editBuilding100Feet"),
	Edit_Budiness_Operations("editBusinessOperations"),
	Edit_Business_Owners_Line_Additional_Coverages("editBOPAddlCov"),
	Edit_Business_Owners_Line_Additional_Insured("editBOPAddlInsured"),
	Edit_Business_Personal_Property_Limit("editBPPLimit"),
	Edit_Business_Year_Start("editBusinessYearStart"),
	Edit_Date_of_Birth("editdatebirth"),
	Edit_DBA("editDBA"),
	Edit_Expiration_Date("editExpDate"),
	Edit_for_Service_Associate("editsa"),
	Edit_Gender("editgender"),
	Edit_Gross_Receipts("editGrossReceipts"),
	Edit_Liability_Limits("editLiabilityLimits"),
	Edit_Life_Safety_Questions("editlifesafety"),
	Edit_Location_Additional_Coverages("EditLocAddlCov"),
	Edit_Location_Additional_Insuerd("editLocAddlInsured"),
	Edit_Mailing_Address("editMailingAddress"),
	Edit_Membership_Dues("editMembershipDues"),
	Edit_note("noteedit"),
	Edit_note_body("noteeditbody"),
	Edit_Parking_Lot_Questions("editparkinglot"),
	Edit_policy_change("editpolchange"),
	Edit_Permises_Medical("editPremisesMedical"),
	Edit_Property_Deductible("editPropertyDeductible"),
	Edit_renewal("editrenewal"),
	Edit_Small_Business_Type("editSmallBusinessType"),
	Edit_Social_Security_Number("editssn"),
	Edit_Term_Type("editTermType"),
	Own_activity("actown"),
	Quote("quote"),
	Reassign_owned_activities("actraown"),
	Reset_Quoting_Lock("resetquotinglock"),
	Search_accounts("Yes"),
	Search_activities("searchactivities"),
	Search_related_contacts("searchcontacts"),
	Search_related_policies("searchpols"),
	Send_Email("sendemail"),
	View_account("viewaccount"),
	View_account_file_contacts("accountcontacts"),
	View_account_file_documents("accountdocs"),
	View_account_file_notes("accountnotes"),
	View_account_file_roles("accountroles"),
	View_account_file_summary("accountsummary"),
	View_account_file_work_orders("accountworkorders"),
	View_activities("actview"),
	View_address_book_contacts("abview"),
	View_audit("viewaudit"),
	View_cancellation("viewcancellation"),
	View_Date_of_Birth("viewdatebirth"),
	View_documents("docview"),
	View_Gender("viewgender"),
	View_History("viewhist"),
	View_issuance("viewissuance"),
	View_my_activities("viewmyactivities"),
	View_my_policy_changes("viewmypolicychanges"),
	View_my_renewals("viewmyrenewals"),
	View_my_submissions("viewmysubmissions"),
	View_notes("noteview"),
	View_policy_change("viewpolchange"),
	View_policy_file("viewpolicyfile"),
	View_policy_file_contacts("pfilecontacts"),
	View_policy_file_details("pfiledetails"),
	View_policy_file_payments("pfilepayments"),
	View_policy_file_pricing("pfilepricing"),
	View_policy_file_summary("pfilesummary"),
	View_prerenewal("viewprerenewal"),
	View_reinstatement("viewreinstate"),
	View_renewal("viewrenewal"),
	View_rewrite("viewrewrite"),
	View_risk_analysis_claims("viewriskclaims"),
	View_risk_analysis_prior_losses("viewriskpriorlosses"),
	View_risk_analysis_prior_policies("viewriskpriorpolicies"),
	View_risk_analysis_UW_issues("viewriskevalissues"),
	View_Social_Security_Number("viewssn"),
	View_submission("viewsubmission"),
	View_Workplan("viewworkplan"),
	Withdraw("withdraw");
	String value;


	private Permissions_SA(String value){
		this.value = value;
	}

	public String getValue(){
		return this.value;
	}

	public static Permissions_SA valueOfName(final String name) {
		final String enumName = name.replaceAll(" ", "");
		try {
			return valueOf(enumName);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}

	private static final List<Permissions_SA> VALUES = Collections.unmodifiableList(Arrays.asList(values()));

	public static Permissions_SA randomGender()  {
		return VALUES.get(new Random().nextInt(VALUES.size()));
	}
}
