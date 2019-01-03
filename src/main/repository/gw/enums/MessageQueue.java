package repository.gw.enums;

public enum MessageQueue {
	// Billing Center Destinations
	Nexus_Account_Status("NexusAccountStatus"),
	Update_CDM_Messaging("UpdateCDMMessaging"),
	PAS("PAS"),
	Document_Store_BC("Document Store"),
	GL_Transaction_Service("GLTransactionService"),
	Email_BC("email"),
	Contact_Message_Transport_BC("Contact Message Transport"),
	Document_Production("DocumentProduction"),
	Check_Printing("CheckPrinting"),
	Nexus_Payment_Service("NexusPaymentService"),
	// Policy Center Destinations
	Printing_Transport("Printing Transport"),
	SolrFBM("Solr FBM"),
	Send_Documents_To_BC("Send Documents To BC"),
	Payment_Transport("Payment Transport"),
	Document_Store_PC("DocumentStore"),
	Transition_Renewal("Transition Renewal"),
	Billing_System("BillingSystem"),
	Email_PC("Email"),
	Contact_Message_Transport_PC("ContactMessageTransport"),
	Nexus_Policy_Bind("Nexus Policy Bind"),
	//Contact Center Destinations
	NewSalvageVendors("New Salvage Vendors"),
	PolicyCenterContactBroadcast("PolicyCenter Contact Broadcast"),
	ClaimVendorInfoUpdate("Claim Vendor Info Update"),
	ClaimCenterContactBroadcast("ClaimCenterContactBroadcast"),
	Email_AB("Email"),
	SolrFBM_AB("Solr FBM"),
	BillingCenterContactBroadcast("BillingCenter Contact Broadcast");

	String value;

	private MessageQueue(String type) {
		value = type;
	}

	public String getValue() {
		return value;
	}
}
