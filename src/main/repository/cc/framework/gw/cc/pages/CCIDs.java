package repository.cc.framework.gw.cc.pages;

import org.openqa.selenium.By;
import repository.cc.framework.gw.element.Identifier;
import repository.cc.framework.gw.element.enums.ElementType;


public class CCIDs {

    public static final Identifier LIST_OPTIONS = new Identifier(By.cssSelector("div.x-boundlist.x-boundlist-floating.x-boundlist-default.x-layer.x-border-box:not([style*='display: none'])"));
    public static final Identifier ESCAPE_CLICKER = new Identifier(By.id("QuickJump-inputEl"));
    public static final Identifier ERROR_MESSAGE = new Identifier(By.xpath("//div[@class='message']/img[@class='error_icon']/parent::div"));

    public static class Desktop {
        public static class SideMenu {
            public static final Identifier VENDOR_INVOICES = new Identifier(By.id("Desktop:MenuLinks:Desktop_DesktopInvoicesGroup"), ElementType.LINK);

            public static class VendorInvoices {
                public static final Identifier QCS = new Identifier(By.id("Desktop:MenuLinks:Desktop_DesktopInvoicesGroup:DesktopInvoicesGroup_DesktopQCSInvoice"), ElementType.LINK);
            }
        }

        public static class VendorInvoices {
            public static class QCSInvoices {
                public static final Identifier CREATE_NEW_INVOICE = new Identifier(By.id("DesktopQCSInvoice:DesktopQCSInvoiceSearchScreen:DesktopQCSInvoiceLV_tb:ToolbarButton"), ElementType.TEXT_BOX);
                public static final Identifier EDIT = new Identifier(By.id("DesktopQCSInvoicePopup:DesktopSGRejectedInvoiceDetailScreen:Edit"), ElementType.ELEMENT);
                public static final Identifier PROCESS = new Identifier(By.id("DesktopQCSInvoicePopup:DesktopSGRejectedInvoiceDetailScreen:ProcessInvoice"), ElementType.ELEMENT);

                public static class QCSInvoiceProcess {
                    public static final Identifier UPDATE = new Identifier(By.id("DesktopQCSInvoiceProcessPopup:DesktopQCSInvoiceDetailScreen:Update-btnInnerEl"), ElementType.ELEMENT);
                    public static final Identifier ACTION_TYPE = new Identifier(By.id("DesktopQCSInvoiceProcessPopup:DesktopQCSInvoiceDetailScreen:DesktopQCSInvoiceDetailDV:CreateClaimFlag-inputEl"), ElementType.ELEMENT);
                    public static final Identifier EXPOSURE = new Identifier(By.id("DesktopQCSInvoiceProcessPopup:DesktopQCSInvoiceDetailScreen:DesktopQCSInvoiceDetailDV:SelectedExposure-inputEl"), ElementType.SELECT_BOX);
                    public static final Identifier PAYMENT_TYPE = new Identifier(By.id("DesktopQCSInvoiceProcessPopup:DesktopQCSInvoiceDetailScreen:DesktopQCSInvoiceDetailDV:SelectedPaymentType-inputEl"), ElementType.SELECT_BOX);
                    public static final Identifier MAKEPAYMENT = new Identifier(By.id("DesktopQCSInvoiceProcessPopup:DesktopQCSInvoiceDetailScreen:DesktopQCSInvoiceDetailDV:ButtonPay-btnInnerEl"), ElementType.ELEMENT);
                }

                public static class NewQCSInvoice {
                    public static final Identifier TRANSFER_FEE = new Identifier(By.id("DesktopQCSInvoiceNewPopup:InvoiceAmount-inputEl"), ElementType.TEXT_BOX);
                    public static final Identifier CLAIM_NUMBER = new Identifier(By.id("DesktopQCSInvoiceNewPopup:Controller-inputEl"), ElementType.TEXT_BOX);
                    public static final Identifier UNIQUE_ID = new Identifier(By.id("DesktopQCSInvoiceNewPopup:IncidentKey-inputEl"), ElementType.TEXT_BOX);
                    public static final Identifier YEAR = new Identifier(By.id("DesktopQCSInvoiceNewPopup:VehicleYear-inputEl"), ElementType.TEXT_BOX);
                    public static final Identifier MAKE = new Identifier(By.id("DesktopQCSInvoiceNewPopup:Make-inputEl"), ElementType.TEXT_BOX);
                    public static final Identifier VIN = new Identifier(By.id("DesktopQCSInvoiceNewPopup:VIN-inputEl"), ElementType.TEXT_BOX);
                    public static final Identifier MODEL = new Identifier(By.id("DesktopQCSInvoiceNewPopup:Model-inputEl"), ElementType.TEXT_BOX);
                    public static final Identifier OWNER = new Identifier(By.id("DesktopQCSInvoiceNewPopup:VehicleOwner-inputEl"), ElementType.TEXT_BOX);
                    public static final Identifier UPDATE = new Identifier(By.id("DesktopQCSInvoiceNewPopup:Update-btnInnerEl"), ElementType.TEXT_BOX);
                    public static final Identifier SEARCH_RESULTS = new Identifier(By.id("DesktopQCSInvoice:DesktopQCSInvoiceSearchScreen:DesktopQCSInvoiceLV-body"), ElementType.TABLE);
                }
            }
        }
    }

    public static class UserSearch {
        public static final Identifier FIRST_NAME = new Identifier(By.id("GroupUserPickerPopup:GroupUserPickerScreen:GroupUserSearchDV:Name:GlobalPersonNameInputSet:FirstName-inputEl"));
        public static final Identifier LAST_NAME = new Identifier(By.id("GroupUserPickerPopup:GroupUserPickerScreen:GroupUserSearchDV:Name:GlobalPersonNameInputSet:LastName-inputEl"));
        public static final Identifier SEARCH_BUTTON = new Identifier(By.id("GroupUserPickerPopup:GroupUserPickerScreen:GroupUserSearchDV:SearchAndResetInputSet:SearchLinksInputSet:Search"));
        public static final Identifier USERS_TABLE = new Identifier(By.id("GroupUserPickerPopup:GroupUserPickerScreen:GroupUserLV-body"));
    }

    public static class SearchAddressBook {
        public static final Identifier NAME = new Identifier(By.id("AddressBookPickerPopup:AddressBookPickerSearchScreen:SearchPanel_FBMPanelSet:NameDenorm-inputEl"));
        public static final Identifier SEARCH = new Identifier(By.id("AddressBookPickerPopup:AddressBookPickerSearchScreen:SearchPanel_FBMPanelSet:Search"));
        public static final Identifier CREATE_NEW = new Identifier(By.id("AddressBookPickerPopup:AddressBookPickerSearchScreen:SearchPanel_FBMPanelSet:FBMAddressBookSearchLV_tb:ContactSearchToolbarButtonSet:ClaimContacts_CreateNewContactButton-btnInnerEl"));
        public static final Identifier COMPANY = new Identifier(By.id("AddressBookPickerPopup:AddressBookPickerSearchScreen:SearchPanel_FBMPanelSet:FBMAddressBookSearchLV_tb:ContactSearchToolbarButtonSet:ClaimContacts_CreateNewContactButton:Parties_Company-textEl"));
        public static final Identifier RETURN_TO_CONTACTS = new Identifier(By.id("AddressBookPickerPopup:__crumb__"));

        public static class NewCompany {
            public static final Identifier NAME = new Identifier(By.id("NewPartyInvolvedPopup:ContactDetailScreen:ContactBasicsDV:OrganizationName:GlobalContactNameInputSet:Name-inputEl"));
            public static final Identifier ROLES_TABLE = new Identifier(By.id("NewPartyInvolvedPopup:ContactDetailScreen:ContactBasicsDV:ContactBasicsHeaderInputSet:EditableClaimContactRolesLV-body"));
            public static final Identifier ROLE = new Identifier(By.name("Role"));
            public static final Identifier TIN = new Identifier(By.id("NewPartyInvolvedPopup:ContactDetailScreen:ContactBasicsDV:TIN-inputEl"));
            public static final Identifier TYPE = new Identifier(By.id("NewPartyInvolvedPopup:ContactDetailScreen:ContactBasicsDV:FBVendorInputSet:VendorType-inputEl"));
            public static final Identifier ADDRESS1 = new Identifier(By.id("NewPartyInvolvedPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:globalAddress:GlobalAddressInputSet:AddressLine1-inputEl"));
            public static final Identifier CITY = new Identifier(By.id("NewPartyInvolvedPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:globalAddress:GlobalAddressInputSet:City-inputEl"));
            public static final Identifier ZIP_CODE = new Identifier(By.id("NewPartyInvolvedPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:globalAddress:GlobalAddressInputSet:PostalCode-inputEl"));
            public static final Identifier MOBILE = new Identifier(By.id("NewPartyInvolvedPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:Cell:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl"));
            public static final Identifier UPDATE = new Identifier(By.id("NewPartyInvolvedPopup:ContactDetailScreen:ContactBasicsDV_tb:ContactDetailToolbarButtonSet:CustomUpdateButton-btnInnerEl"));
            public static final Identifier PRIMARY_ADDRESS_TYPE = new Identifier(By.id("NewPartyInvolvedPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:Address_AddressType-inputEl"));
            public static final Identifier STATE = new Identifier(By.id("NewPartyInvolvedPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:globalAddress:GlobalAddressInputSet:State-inputEl"));
        }
    }

    public static class NavBar {
        public static final Identifier LOG_OUT = new Identifier(By.id("TabBar:LogoutTabBarLink-textEl"));
        public static final Identifier GEAR = new Identifier(By.id(":TabLinkMenuButton"));
        public static final Identifier CLAIM = new Identifier(By.id("TabBar:ClaimTab-btnInnerEl"));
        public static final Identifier ADMINISTRATION = new Identifier(By.id("TabBar:AdminTab"));
        public static final Identifier CLAIM_ARROW = new Identifier(By.id("TabBar:ClaimTab"));
        public static final Identifier NEW_CLAIM = new Identifier(By.id("TabBar:ClaimTab:ClaimTab_FNOLWizard-itemEl"));
        public static final Identifier CLAIM_NUMBER = new Identifier(By.id("TabBar:ClaimTab:ClaimTab_FindClaim-inputEl"));
        public static final Identifier FIND_CLAIM = new Identifier(By.id("TabBar:ClaimTab:ClaimTab_FindClaim_Button"));
    }

    public static class Login {
        public static final Identifier USER_NAME = new Identifier(By.id("Login:LoginScreen:LoginDV:username-inputEl"));
        public static final Identifier PASSWORD = new Identifier(By.id("Login:LoginScreen:LoginDV:password-inputEl"));
        public static final Identifier LOG_IN = new Identifier(By.id("Login:LoginScreen:LoginDV:submit-btnInnerEl"));
    }

    public static class Administration {
        public static class ScriptParameters {
            public static final Identifier BEGINNING_WITH = new Identifier(By.id("ScriptParametersPage:ScriptParametersScreen:ScriptParametersLV:FirstSubjectCharacter-inputEl"));
            public static final Identifier SCRIPT_PARAMETERS_TABLE = new Identifier(By.id("ScriptParametersPage:ScriptParametersScreen:ScriptParametersLV-body"));
        }

        public static class SideMenu {
            public static final Identifier UTILITIES = new Identifier(By.id("Admin:MenuLinks:Admin_Utilities"));
            public static final Identifier UTILITIES_SCRIPT_PARAMETERS = new Identifier(By.id("Admin:MenuLinks:Admin_Utilities:Utilities_ScriptParametersPage"));
        }

        public static class EditScriptParameter {
            public static final Identifier EDIT = new Identifier(By.id("ScriptParameterDetail:ScriptParameterDetailScreen:Edit"));
            public static final Identifier VALUE_NO = new Identifier(By.id("ScriptParameterDetail:ScriptParameterDetailScreen:ScriptParameterDetailDV:BitValue_false-inputEl"));
            public static final Identifier UPDATE = new Identifier(By.id("ScriptParameterDetail:ScriptParameterDetailScreen:Update"));
        }
    }

    public static class AdvancedSearch {
        public static final Identifier CLAIMNUMBER = new Identifier(By.id("ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:ClaimNumber-inputEl"));
        public static final Identifier CLAIMSTATUS = new Identifier(By.id("ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:ClaimStatus-triggerWrap"));
        public static final Identifier LOSSTYPE = new Identifier(By.id("ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:LossType-triggerWrap"));
        public static final Identifier SEARCHFORDATE_SINCE = new Identifier(By.id("ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:DateSearch:DateSearchRangeValue-triggerWrap"));
        public static final Identifier SEARCH_BUTTON = new Identifier(By.id("ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchAndResetInputSet:Search"));
    }

    public static class NewClaim {
        public static final Identifier FNOL_WIZARD_NEXT = new Identifier(By.id("FNOLWizard:Next"));
        public static final Identifier FNOL_WIZARD_FINISH = new Identifier(By.id("FNOLWizard:Finish"));
        public static final Identifier ERROR_MESSAGE_ELEMENT = new Identifier(By.className("message"));

        public static class SearchOrCreatePolicy {
            public static final Identifier POLICY_ROOT_NUMBER = new Identifier(By.id("FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:policyNumber-inputEl"));
            public static final Identifier SEARCH_BUTTON = new Identifier(By.id("FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:Search"));
            public static final Identifier SEARCH_RESULTS = new Identifier(By.id("FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:PolicyResultLV-body"));
            public static final Identifier DATE_OF_LOSS = new Identifier(By.id("FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:Claim_LossDate-inputEl"));
            public static final Identifier PROPERTY_RADIO = new Identifier(By.id("FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:ClaimMode_option4-inputEl"));
            public static final Identifier CREATE_UNVERIFIED_POLICY = new Identifier(By.id("FNOLWizard:FNOLWizard_FindPolicyScreen:ScreenMode_false-inputEl"));
            public static final Identifier POLICY_NUMBER = new Identifier(By.id("FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:PolicyNumber-inputEl"));

            public static class UnverifiedPolicy {
                public static final Identifier AGENT_NAME_PICKER = new Identifier(By.id("FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:NewClaimPolicyGeneralPanelSet:NewClaimPolicyGeneralDV:PolicyAgentInputSet:Agent_Name:Agent_NameMenuIcon"));
                public static final Identifier VIEW_CONTACT_DETAILS_LINK = new Identifier(By.id("FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:NewClaimPolicyGeneralPanelSet:NewClaimPolicyGeneralDV:PolicyAgentInputSet:Agent_Name:MenuItem_ViewDetails-textEl"));
                public static final Identifier TYPE = new Identifier(By.id("FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:Type-inputEl"));
                public static final Identifier AGENT_NAME_SEARCH = new Identifier(By.id("FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:NewClaimPolicyGeneralPanelSet:NewClaimPolicyGeneralDV:PolicyAgentInputSet:Agent_Name:MenuItem_Search-textEl"));
                public static final Identifier AGENT_NAME = new Identifier(By.id("FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:NewClaimPolicyGeneralPanelSet:NewClaimPolicyGeneralDV:PolicyAgentInputSet:Agent_Name-inputEl"));
            }
        }

        public static class BasicInformation {
            public static final Identifier NAME = new Identifier(By.id("FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:BasicInfoDetailViewPanelDV:ReportedBy_Name-inputEl"));
            public static final Identifier MOBILE = new Identifier(By.id("FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:BasicInfoDetailViewPanelDV:FNOLWizard_ContactInfoInputSet:FBContactInfoInputSet:Cell:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl"));
            public static final Identifier PRIMARY_PHONE = new Identifier(By.id("FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:BasicInfoDetailViewPanelDV:FNOLWizard_ContactInfoInputSet:FBContactInfoInputSet:primaryPhone-inputEl"));
            public static final Identifier EMAIL = new Identifier(By.id("FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:BasicInfoDetailViewPanelDV:FNOLWizard_ContactInfoInputSet:reporter_email-inputEl"));
            public static final Identifier VEHICLES_INVOLVED = new Identifier(By.id("FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:RightPanel:FNOLWizard_BasicInfoRightPanelSet:3-body"));
            public static final Identifier DUPLICATE_CLAIMS = new Identifier(By.id("wsTabBar:wsTab_0-btnInnerEl"));
            public static final Identifier CLOSE_BUTTON = new Identifier(By.id("NewClaimDuplicatesWorksheet:NewClaimDuplicatesScreen:NewClaimDuplicatesWorksheet_CloseButton-btnInnerEl"));
        }

        public static final class AddClaimInformation {
            public static final Identifier LOSS_DESCRIPTION = new Identifier(By.id("FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:LossDetailsAddressDV:Description-inputEl"));
            public static final Identifier LOSS_CAUSE = new Identifier(By.id("FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:LossDetailsAddressDV:Claim_LossCause-inputEl"));
            public static final Identifier LOSS_ROUTER = new Identifier(By.id("FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:LossDetailsAddressDV:Claim_LossRouter-inputEl"));
            public static final Identifier LOCATION = new Identifier(By.id("FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:LossDetailsAddressDV:AddressDetailInputSetRef:LossDetailsAddressDetailInputSet:LossLocation_Name-inputEl"));
            public static final Identifier CITY = new Identifier(By.id("FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:LossDetailsAddressDV:AddressDetailInputSetRef:LossDetailsAddressDetailInputSet:FNOLAddressInputSet:City-inputEl"));
            public static final Identifier STATE = new Identifier(By.id("FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:LossDetailsAddressDV:AddressDetailInputSetRef:LossDetailsAddressDetailInputSet:FNOLAddressInputSet:State-inputEl"));
        }

        public static final class NewClaimSaved {
            public static final Identifier CLAIM_NUMBER = new Identifier(By.id("NewClaimSaved:NewClaimSavedScreen:NewClaimSavedDV:Header"));
            public static final Identifier VIEW_NEW_CLAIM = new Identifier(By.id("NewClaimSaved:NewClaimSavedScreen:NewClaimSavedDV:GoToClaim-inputEl"));
        }
    }

    public static class Claim {
        public static final Identifier INSURED_NAME = new Identifier(By.cssSelector("span[id*='ClaimInfoBar:Insured-btnInnerEl'] span[class*='infobar_elem_val']"));
        public static final Identifier ADJUSTER = new Identifier(By.id("Claim:ClaimInfoBar:Adjuster-btnWrap"), ElementType.ELEMENT);

        public static class ValidationResults {
            public static final Identifier VALIDATION_RESULTS_MESSAGES = new Identifier(By.cssSelector("div[id='WebMessageWorksheet:WebMessageWorksheetScreen:grpMsgs'] div[class='message']"), ElementType.ELEMENT);
        }

        public static class CloseClaim {
            public static final Identifier NOTES = new Identifier(By.id("CloseClaimPopup:CloseClaimScreen:CloseClaimInfoDV:Note-inputEl"), ElementType.TEXT_AREA);
            public static final Identifier OUTCOME = new Identifier(By.id("CloseClaimPopup:CloseClaimScreen:CloseClaimInfoDV:Outcome-inputEl"), ElementType.SELECT_BOX);
            public static final Identifier CLOSE_CLAIM = new Identifier(By.id("CloseClaimPopup:CloseClaimScreen:Update"), ElementType.ELEMENT);
        }

        public static class NewActivityInClaim {
            public static final Identifier MANDATORY_YES = new Identifier(By.id("NewActivity:NewActivityScreen:NewActivityDV:Activity_Mandatory_true-inputEl"), ElementType.ELEMENT);
            public static final Identifier UPDATE = new Identifier(By.id("NewActivity:NewActivityScreen:NewActivity_UpdateButton"), ElementType.ELEMENT);
        }

        public static class ReopenClaim {
            public static final Identifier NOTE = new Identifier(By.id("ReopenClaimPopup:ReopenClaimScreen:ReopenClaimInfoDV:Note-inputEl"), ElementType.TEXT_AREA);
            public static final Identifier REASON = new Identifier(By.id("ReopenClaimPopup:ReopenClaimScreen:ReopenClaimInfoDV:Reason-inputEl"), ElementType.SELECT_BOX);
            public static final Identifier REOPEN_CLAIM = new Identifier(By.id("ReopenClaimPopup:ReopenClaimScreen:Update-btnInnerEl"), ElementType.ELEMENT);
        }

        public static class VoidOrStopCheck {
            public static final Identifier REASON_FOR_VOID_STOP = new Identifier(By.id("VoidStopCheck:VoidStopCheckScreen:VoidStopCheckDV:Comments-inputEl"));
            public static final Identifier VOID = new Identifier(By.id("VoidStopCheck:VoidStopCheckScreen:VoidStopCheck_VoidButton"));
        }

        public static class CheckDetails {
            public static final Identifier VOID_STOP = new Identifier(By.id("ClaimFinancialsChecksDetail:ClaimFinancialsChecksDetailScreen:ClaimFinancialsChecksDetail_VoidStopButton"));
            public static final Identifier PAYMENTS = new Identifier(By.id("ClaimFinancialsChecksDetail:ClaimFinancialsChecksDetailScreen:CheckDV:CheckSummaryPaymentsLV-body"));
        }

        public static class ApprovalPopup {
            public static final Identifier APPROVE = new Identifier(By.id("ApprovalDetailWorksheet:ApprovalDetailScreen:ApprovalDetailWorksheet_ApproveButton"));
        }

        public static class Financials {
            public static class Checks {
                public static final Identifier FINANCIALS_CHECKS = new Identifier(By.id("ClaimFinancialsChecks:ClaimFinancialsChecksScreen:ChecksLV-body"));
            }
        }

        public static class CreateDuplicateClaim {
            public static final Identifier TYPE = new Identifier(By.id("CreateDuplicatePopup:SelectDuplicateType-inputEl"));
            public static final Identifier NOTES_YES = new Identifier(By.id("CreateDuplicatePopup:withOrWithoutNotes_true-inputEl"));
            public static final Identifier NOTES_NO = new Identifier(By.id("CreateDuplicatePopup:withOrWithoutNotes_false-inputEl"));
            public static final Identifier DUPLICATE_CLAIM = new Identifier(By.id("CreateDuplicatePopup:DuplicateButton"));
        }

        public static class SideMenu {
            public static final Identifier EXPOSURES = new Identifier(By.id("Claim:MenuLinks:Claim_ClaimExposures"));
            public static final Identifier PARTIES_INVOLVED = new Identifier(By.id("Claim:MenuLinks:Claim_ClaimPartiesGroup"));
            public static final Identifier WORKPLAN = new Identifier(By.id("Claim:MenuLinks:Claim_ClaimWorkplan"));
            public static final Identifier FINANCIALS = new Identifier(By.id("Claim:MenuLinks:Claim_ClaimFinancialsGroup"));
            public static final Identifier FINANCIALS_CHECKS = new Identifier(By.id("Claim:MenuLinks:Claim_ClaimFinancialsGroup:ClaimFinancialsGroup_ClaimFinancialsChecks"));
            public static final Identifier LOSS_DETAILS = new Identifier(By.id("Claim:MenuLinks:Claim_ClaimLossDetailsGroup"), ElementType.LINK);
        }

        public static class Contacts {
            public static final Identifier ADD_CONTACT = new Identifier(By.id("ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:PeopleInvolvedDetailedLV_tb:ClaimContacts_AddExistingButton-btnInnerEl"));
            public static final Identifier ROLES_FILTER = new Identifier(By.id("ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:PeopleInvolvedDetailedLV:RoleFilter-inputEl"), ElementType.SELECT_BOX);
            public static final Identifier EDIT = new Identifier(By.id("ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV_tb:ContactDetailToolbarButtonSet:Edit-btnInnerEl"), ElementType.BUTTON);
            public static final Identifier WORK_PHONE = new Identifier(By.id("ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:Work:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl"), ElementType.TEXT_BOX);
            public static final Identifier UPDATE = new Identifier(By.id("ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV_tb:ContactDetailToolbarButtonSet:Update"), ElementType.BUTTON);
        }

        public static class Contact {
            public static class Basics {
                public static final Identifier EDIT_BUTTON = new Identifier(By.id("ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV_tb:ContactDetailToolbarButtonSet:Edit-btnInnerEl"));
                public static final Identifier MOBILE = new Identifier(By.id("ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:Cell:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl"));
                public static final Identifier PRIMARY_PHONE = new Identifier(By.id("ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV:PrimaryAddressInputSet:CCAddressInputSet:globalAddressContainer:FBContactInfoInputSet:primaryPhone-inputEl"));
                public static final Identifier OK_BUTTON = new Identifier(By.id("ClaimContactDetailPopup:ContactDetailScreen:ContactBasicsDV_tb:ContactDetailToolbarButtonSet:Update-btnInnerEl"));
            }
        }

        public static class SystemCheckWizard {
            public static final Identifier NAME = new Identifier(By.id("NormalCreateCheckWizard:CheckWizard_CheckPayeesScreen:NewCheckPayeeDV:PrimaryPayee_Name-inputEl"));
            public static final Identifier TYPE = new Identifier(By.id("NormalCreateCheckWizard:CheckWizard_CheckPayeesScreen:NewCheckPayeeDV:PrimaryPayee_Type-inputEl"));
            public static final Identifier REPORT_AS = new Identifier(By.id("NormalCreateCheckWizard:CheckWizard_CheckPayeesScreen:NewCheckPayeeDV:Reportability-inputEl"));
            public static final Identifier CHECK_DELIVERY = new Identifier(By.id("NormalCreateCheckWizard:CheckWizard_CheckPayeesScreen:NewCheckPayeeDV:DeliveryMethod-inputEl"));
            public static final Identifier CHECK_TYPE = new Identifier(By.id("NormalCreateCheckWizard:CheckWizard_CheckPayeesScreen:NewCheckPayeeDV:Check_CheckType-inputEl"));
            public static final Identifier NEXT_BUTTON = new Identifier(By.id("NormalCreateCheckWizard:Next"));
            public static final Identifier RESERVE_LINE = new Identifier(By.id("NormalCreateCheckWizard:CheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:ReserveLineInputSet:ReserveLine-inputEl"));
            public static final Identifier PAYMENT_TYPE = new Identifier(By.id("NormalCreateCheckWizard:CheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:Payment_PaymentType-inputEl"));
            public static final Identifier PAYMENT_LINE_ITEMS = new Identifier(By.id("NormalCreateCheckWizard:CheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:EditablePaymentLineItemsLV-body"));
            public static final Identifier LINE_ITEM_TYPE = new Identifier(By.name("LineCategory"));
            public static final Identifier AMOUNT = new Identifier(By.name("Amount"));
            public static final Identifier FINISH_BUTTON = new Identifier(By.id("NormalCreateCheckWizard:Finish"));
            public static final Identifier LINE_ITEM_CATEGORY = new Identifier(By.name("Category"));
        }

        public static final class Policy {
            public static final class Vehicles {
                public static final Identifier POLICY_VEHICLES = new Identifier(By.id("ClaimPolicyVehicles:ClaimPolicyVehiclesScreen:VehiclesLV-body"));
            }
        }

        public static final class LossDetails {
            public static final Identifier PROPERTIES_INCIDENTS = new Identifier(By.id("ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV:LossDetailsDV:EditableFixedPropertyIncidentsLV-body"));
            public static final Identifier VEHICLES_INCIDENTS = new Identifier(By.id("ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV:LossDetailsDV:EditableVehicleIncidentsLV-body"));
            public static final Identifier EDIT_BUTTON = new Identifier(By.id("ClaimLossDetails:ClaimLossDetailsScreen:Edit-btnInnerEl"));
            public static final Identifier UPDATE_BUTTON = new Identifier(By.id("ClaimLossDetails:ClaimLossDetailsScreen:Update"));
            public static final Identifier SALVAGE_STATUS = new Identifier(By.id("ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV:LossDetailsDV:ClaimsDetailInputSet:Status_SalvageStatus-inputEl"));
            public static final Identifier INJURIES_INCIDENTS = new Identifier(By.id("ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV:LossDetailsDV:EditableInjuryIncidentsLV-body"), ElementType.TABLE);
        }

        public static final class Workplan {
            public static final Identifier ACTIVITIES_TABLE = new Identifier(By.id("ClaimWorkplan:ClaimWorkplanScreen:WorkplanLV-body"));
            public static final Identifier APPROVE_BUTTON = new Identifier(By.id("ApprovalDetailWorksheet:ApprovalDetailScreen:ApprovalDetailWorksheet_ApproveButton-btnInnerEl"));
            public static final Identifier ACTIVITIES = new Identifier(By.id("ClaimWorkplan:ClaimWorkplanScreen:WorkplanLV:WorkplanFilter-inputEl"));
        }

        public static final class Incidents {
            public static final Identifier CREATE_RESERVE = new Identifier(By.id("ExposureDetail:ExposureDetailScreen:ExposureDetailScreen_CreateReserveButton"));

            public static final class InjuryIncident {
                public static final Identifier EDIT_BUTTON = new Identifier(By.id("EditInjuryIncidentPopup:EditInjuryIncidentScreen:Edit"));
                public static final Identifier MBI = new Identifier(By.id("EditInjuryIncidentPopup:EditInjuryIncidentScreen:InjuryIncidentDV:MBI-inputEl"));
                public static final Identifier IS_INJURED_PARTY_US_CITIZEN_NO = new Identifier(By.id("EditInjuryIncidentPopup:EditInjuryIncidentScreen:InjuryIncidentDV:USCitizen_FBM_false-inputEl"), ElementType.BUTTON);
                public static final Identifier UPDATE = new Identifier(By.id("EditInjuryIncidentPopup:EditInjuryIncidentScreen:Update"), ElementType.BUTTON);
            }
            
            public static final class VehicleIncident {
                public static final Identifier VEHICLE_SALVAGE_TAB = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:VehicleDamage_VehicleSalvageCardTab"));
                public static final Identifier TOTAL_LOSS_YES = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:VehicleIncidentDV:TotalLoss_true-inputEl"));
                public static final Identifier EDIT_BUTTON = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:Edit"));

                public static final class Details {
                    public static final Identifier TOTAL_LOSS_YES = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:VehicleIncidentDV:TotalLoss_true-inputEl"));
                    public static final Identifier IS_THIS_VEHICLE_DAMAGED_NO = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:VehicleIncidentDV:Collision_Indicator_false-inputEl"));
                    public static final Identifier DRIVER_NAME = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:VehicleIncidentDV:Driver_Picker-inputEl"));
                    public static final Identifier RELATION_TO_INSURED = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:VehicleIncidentDV:RelationToInsured-inputEl"));
                }

                public static final class VehicleSalvage {
                    public static final Identifier OWNER_BUY_BACK_NO = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:FB_VI_VehicleSalvageDV:OwnerBuyBack_false-inputEl"));
                    public static final Identifier PROQUOTE_STYLE = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:FB_VI_VehicleSalvageDV:Vehicle_Style-inputEl"));
                    public static final Identifier GET_PROQUOTE_BUTTON = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:FB_VI_VehicleSalvageDV:Copart_ProQuote"));
                    public static final Identifier PROQUOTE_COPART_PRIMAY_DAMAGE = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:FB_VI_VehicleSalvageDV:Property_Damage_RangeInput-inputEl"));
                    public static final Identifier PROQUOTE_RESPONSE_PROQUOTE = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:FB_VI_VehicleSalvageDV:ProQuote_ProQuote-inputEl"));

                    public static final class CopartAssignmentDetails {
                        public static final Identifier LOT_NUMBER = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:CopartStatusInputSet:SalvageUpdate_LotNumber-inputEl"));
                        public static final Identifier CANCEL_SERVICE_BUTTON = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartStateToolbar:Cancel"));
                        public static final Identifier ASSIGNMENT_STATUS = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:CopartStatusInputSet:AssignmentStatus-inputEl"));
                        public static final Identifier PRIMARY_DAMAGE = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:Property_Damage_RangeInput-inputEl"));
                        public static final Identifier INSPECT_FOR_REPAIRABLE_TOTAL_LOSS_YES = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:CopartDetailsInputSet:inpect_at_copart_true-inputEl"));
                        public static final Identifier TYPE_OF_LOSS = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:CopartDetailsInputSet:Type_Of_Loss-inputEl"));
                        public static final Identifier RESERVE = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:CopartDetailsInputSet:reserve_typeId-inputEl"));
                        public static final Identifier PICKUP_ADDRESS = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:copart_vehicle_location_option1-inputEl"));
                        public static final Identifier CONTACT_PERSON = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:VehicleIncident_AltContactName-inputEl"));
                        public static final Identifier CREATE_COPART_ASSIGNMENT_BUTTON = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartStateToolbar:SubmitInstruction"));
                        public static final Identifier CITY = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:CopartAddressInputSet:globalAddressContainer:CountryInputSet:City-inputEl"));
                        public static final Identifier STATE = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:CopartAddressInputSet:globalAddressContainer:CountryInputSet:State-inputEl"));
                        public static final Identifier CONTACT_PHONE = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:ContactPhone-inputEl"));
                        public static final Identifier CONTACT_PERSON_PICKER = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:VehicleIncident_AltContactName:VehicleIncident_AltContactNameMenuIcon"));
                        public static final Identifier VIEW_CONTACT_DETAILS = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:VehicleIncident_AltContactName:MenuItem_ViewDetails-textEl"));
                        public static final Identifier UPDATE_COPART_ASSIGNMENT = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartStateToolbar:EditCopartAssignment"));
                        public static final Identifier PRIMARY_DAMAGE_ELEMENT = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:Property_Damage_RangeInput-inputEl"));
                        public static final Identifier VISIT_COPART_WEBSITE_BUTTON = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartStateToolbar:LoginToCopart"));
                        public static final Identifier PICKUP_LOCATION = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:Address_Picker-inputEl"));
                        public static final Identifier SALVAGE_ASSIGNMENT = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:FB_VI_VehicleSalvageDV:HowSalvageWasDone-inputEl"));
                        public static final Identifier STATUS_DESCRIPTION = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:CopartStatusInputSet:Description-inputEl"));
                        public static final Identifier INSPECT_FOR_REPAIRABLE_TOTAL_LOSS_NO = new Identifier(By.id("EditVehicleIncidentPopup:EditVehicleIncidentScreen:VehIncidentDetailDV:CopartDetailsDV:CopartDetailsInputSet:inpect_at_copart_false-inputEl"), ElementType.RADIO);
                        public static final Identifier CLEAR = new Identifier(By.id("WebMessageWorksheet:WebMessageWorksheetScreen:WebMessageWorksheet_ClearButton"), ElementType.BUTTON);
                    }

                    public static final class CopartCancelAssignment {
                        public static final Identifier CANCEL_REASON = new Identifier(By.id("CopartCancelAssignmentPopup:CopartAssignmentCancelDV:copartCancelType-inputEl"));
                        public static final Identifier UPDATE_BUTTON = new Identifier(By.id("CopartCancelAssignmentPopup:Update"));
                    }
                }
            }

            public static final class PropertyIncident {
                public static final Identifier ROOF_DAMAGE_INVOLVED = new Identifier(By.id("EditFixedPropertyIncidentPopup:EditFixedPropertyIncidentScreen:FixPropIncidentDetailDV:FixedPropertyIncidentDV:PropertyAttributeInputSet:LossLocation_Roof-inputEl"));
                public static final Identifier MOLD_INVOLVED = new Identifier(By.id("EditFixedPropertyIncidentPopup:EditFixedPropertyIncidentScreen:FixPropIncidentDetailDV:FixedPropertyIncidentDV:PropertyAttributeInputSet:LossLocation_Mold-inputEl"));
                public static final Identifier OWNER = new Identifier(By.id("EditFixedPropertyIncidentPopup:EditFixedPropertyIncidentScreen:FixPropIncidentDetailDV:FixedPropertyIncidentDV:Owner_Picker-inputEl"));
                public static final Identifier OK_BUTTON = new Identifier(By.id("EditFixedPropertyIncidentPopup:EditFixedPropertyIncidentScreen:Update"));
                public static final Identifier ERROR_MESSAGE = new Identifier(By.id("EditFixedPropertyIncidentPopup:EditFixedPropertyIncidentScreen:_msgs"));
            }
        }

        public static class ManualCheckWizard {
            public static class Payees {
                public static final Identifier PRIMARY_PAYEE_NAME = new Identifier(By.id("ManualCreateCheckWizard:ManualCheckWizard_CheckPayeesScreen:NewManualCheckPayeeDV:PrimaryPayee_Name-inputEl"));
                public static final Identifier TYPE = new Identifier(By.id("ManualCreateCheckWizard:ManualCheckWizard_CheckPayeesScreen:NewManualCheckPayeeDV:PrimaryPayee_Type-inputEl"));
                public static final Identifier NEXT = new Identifier(By.id("ManualCreateCheckWizard:Next"));
                public static final Identifier CHECK_TYPE = new Identifier(By.id("ManualCreateCheckWizard:ManualCheckWizard_CheckPayeesScreen:NewManualCheckPayeeDV:Check_CheckType-inputEl"));
                public static final Identifier CHECK_NUMBER = new Identifier(By.id("ManualCreateCheckWizard:ManualCheckWizard_CheckPayeesScreen:NewManualCheckPayeeDV:Check_CheckNumber-inputEl"));
                public static final Identifier ERROR_MESSAGE = new Identifier(By.id("ManualCreateCheckWizard:ManualCheckWizard_CheckPayeesScreen:_msgs"));
            }

            public static class Payments {
                public static final Identifier RERSERVE_LINE = new Identifier(By.id("ManualCreateCheckWizard:ManualCheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:ReserveLineInputSet:ReserveLine-inputEl"));
                public static final Identifier PAYMENT_TYPE = new Identifier(By.id("ManualCreateCheckWizard:ManualCheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:Payment_PaymentType-inputEl"));
                public static final Identifier LINE_ITEMS = new Identifier(By.id("ManualCreateCheckWizard:ManualCheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:EditablePaymentLineItemsLV-body"));
                public static final Identifier TYPE = new Identifier(By.name("LineCategory"));
                public static final Identifier AMOUNT = new Identifier(By.name("Amount"));
                public static final Identifier NEXT = new Identifier(By.id("ManualCreateCheckWizard:Next"));
                public static final Identifier CATEGORY = new Identifier(By.name("Category"));
            }

            public static class Instructions {
                public static final Identifier FINISH = new Identifier(By.id("ManualCreateCheckWizard:Finish"));
            }
        }

        public static class ActionsMenu {
            public static final Identifier ACTIONS_BUTTON = new Identifier(By.id("Claim:ClaimMenuActions"));
            public static final Identifier CHOOSE_BY_COVERAGE = new Identifier(By.id("Claim:ClaimMenuActions:ClaimMenuActions_NewExposure:NewExposureMenuItemSet:NewExposureMenuItemSet_ByCoverage-itemEl"));
            public static final Identifier CHECK = new Identifier(By.id("Claim:ClaimMenuActions:ClaimMenuActions_NewTransaction:ClaimMenuActions_NewTransaction_CheckSet-itemEl"));
            public static final Identifier REGULAR_PAYMENT = new Identifier(By.id("Claim:ClaimMenuActions:ClaimMenuActions_NewTransaction:ClaimMenuActions_NewTransaction_CheckSet:ClaimMenuActions_NewTransaction_Payment-itemEl"));
            public static final Identifier DUPLICATE_CLAIM = new Identifier(By.id("Claim:ClaimMenuActions:ClaimFileMenuItemSet:ClaimMenuActions_ClaimActions:ClaimMenuActions_CreateDuplicate-itemEl"));
            public static final Identifier FIELD_CHECK_DRAFT = new Identifier(By.id("Claim:ClaimMenuActions:ClaimMenuActions_NewTransaction:ClaimMenuActions_NewTransaction_CheckSet:ClaimMenuActions_NewTransaction_Check-textEl"));
            public static final Identifier DUPLICATE_CLAIM_COVERAGE_SPLIT = new Identifier(By.id("Claim:ClaimMenuActions:ClaimFileMenuItemSet:ClaimMenuActions_ClaimActions:ClaimMenuActions_CreateDuplicate:ClaimMenuActions_SplitFile-itemEl"));
            public static final Identifier DUPLICATE_CLAIM_INHOUSE = new Identifier(By.id("Claim:ClaimMenuActions:ClaimFileMenuItemSet:ClaimMenuActions_ClaimActions:ClaimMenuActions_CreateDuplicate:ClaimMenuActions_Inhouse-itemEl"));
            public static final Identifier DUPLICATE_CLAIM_SPECIAL_INVESTIGATION = new Identifier(By.id("Claim:ClaimMenuActions:ClaimFileMenuItemSet:ClaimMenuActions_ClaimActions:ClaimMenuActions_CreateDuplicate:ClaimMenuActions_SIUhouse-itemEl"));
            public static final Identifier DUPLICATE_CLAIM_COVERAGE_SPLIT_NOTES = new Identifier(By.id("Claim:ClaimMenuActions:ClaimFileMenuItemSet:ClaimMenuActions_ClaimActions:ClaimMenuActions_CreateDuplicate:ClaimMenuActions_SplitFile:ClaimMenuActions_CoverageFileWithNotes-itemEl"));
            public static final Identifier DUPLICATE_CLAIM_COVERAGE_SPLIT_NO_NOTES = new Identifier(By.id("Claim:ClaimMenuActions:ClaimFileMenuItemSet:ClaimMenuActions_ClaimActions:ClaimMenuActions_CreateDuplicate:ClaimMenuActions_SplitFile:ClaimMenuActions_CoverageFileWithoutNotes-itemEl"));
            public static final Identifier DUPLICATE_CLAIM_INHOUSE_NOTES = new Identifier(By.id("Claim:ClaimMenuActions:ClaimFileMenuItemSet:ClaimMenuActions_ClaimActions:ClaimMenuActions_CreateDuplicate:ClaimMenuActions_Inhouse:ClaimMenuActions_InhouseClaimWithNotes-itemEl"));
            public static final Identifier DUPLICATE_CLAIM_INHOUSE_NO_NOTES = new Identifier(By.id("Claim:ClaimMenuActions:ClaimFileMenuItemSet:ClaimMenuActions_ClaimActions:ClaimMenuActions_CreateDuplicate:ClaimMenuActions_Inhouse:ClaimMenuActions_InhouseClaimWithoutNotes-itemEl"));
            public static final Identifier DUPLICATE_CLAIM_SPECIAL_INVESTIGATION_NOTES = new Identifier(By.id("Claim:ClaimMenuActions:ClaimFileMenuItemSet:ClaimMenuActions_ClaimActions:ClaimMenuActions_CreateDuplicate:ClaimMenuActions_SIUhouse:ClaimMenuActions_SIUClaimWithNotes-itemEl"));
            public static final Identifier DUPLICATE_CLAIM_SPECIAL_INVESTIGATION_NO_NOTES = new Identifier(By.id("Claim:ClaimMenuActions:ClaimFileMenuItemSet:ClaimMenuActions_ClaimActions:ClaimMenuActions_CreateDuplicate:ClaimMenuActions_SIUhouse:ClaimMenuActions_SIUClaimWithoutNotes-itemEl"));
            public static final Identifier REOPEN_CLAIM = new Identifier(By.id("Claim:ClaimMenuActions:ClaimFileMenuItemSet:ClaimMenuActions_ClaimActions:ClaimMenuActions_ReopenClaim-itemEl"), ElementType.ELEMENT);
            public static final Identifier GENERAL = new Identifier(By.linkText("General"), ElementType.ELEMENT);
            public static final Identifier REVIEW_BI_EVALUATION = new Identifier(By.linkText("Review BI Evaluation"), ElementType.ELEMENT);
            public static final Identifier NEW_MAIL = new Identifier(By.linkText("New Mail"), ElementType.ELEMENT);
            public static final Identifier REVIEW_NEW_MAIL = new Identifier(By.linkText("Review New Mail"), ElementType.ELEMENT);
            public static final Identifier CLOSE_CLAIM = new Identifier(By.linkText("Close Claim"), ElementType.ELEMENT);
        }

        public static class NewExposure {
            public static final Identifier CLAIMANT = new Identifier(By.id("NewExposure:NewExposureScreen:NewExposureDV:NewClaimVehicleDamageDV:ClaimantTypeInputSet:Claimant_Picker-inputEl"));
            public static final Identifier TYPE = new Identifier(By.id("NewExposure:NewExposureScreen:NewExposureDV:NewClaimVehicleDamageDV:ClaimantTypeInputSet:Claimant_Type-inputEl"));
            public static final Identifier VEHICLE = new Identifier(By.id("NewExposure:NewExposureScreen:NewExposureDV:NewClaimVehicleDamageDV:ClaimantTypeInputSet:Vehicle_Incident-inputEl"));
            public static final Identifier UPDATE_BUTTON = new Identifier(By.id("NewExposure:NewExposureScreen:Update"));

        }
        public static class Exposures {
            public static final Identifier EXPOSURES_TABLE = new Identifier(By.id("ClaimExposures:ClaimExposuresScreen:ExposuresLV-body"));
            public static final Identifier INCIDENT_PICKER = new Identifier(By.cssSelector("a[id*='_IncidentMenuIcon']"));
            public static final Identifier EDIT_INCIDENT_DETAILS = new Identifier(By.cssSelector("a[id*='_EditIncidentMenuItem-itemEl']"));
            public static final Identifier VIEW_INCIDENT_DETAILS = new Identifier(By.cssSelector("a[id*='_ViewIncidentMenuItem-itemEl']"));

            public static class VehicleDamageExposure {

            }

            public static class InjuryExposure {

            }
        }

        public static class SetReserves {

            public static final Identifier ADD_BUTTON = new Identifier(By.id("NewReserveSet:NewReserveSetScreen:Add"));
            public static final Identifier RESERVES_TABLE = new Identifier(By.id("NewReserveSet:NewReserveSetScreen:ReservesSummaryDV:EditableReservesLV-body"));
            public static final Identifier COST_CATEGORY = new Identifier(By.name("CostCategory"));

        }
    }
}
