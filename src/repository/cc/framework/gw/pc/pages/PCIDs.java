package repository.cc.framework.gw.pc.pages;

import org.openqa.selenium.By;
import repository.cc.framework.gw.element.Identifier;

public class PCIDs {
    public final static class Login {
        public static final Identifier USER_NAME = new Identifier(By.id("Login:LoginScreen:LoginDV:username-inputEl"));
        public static final Identifier PASSWORD = new Identifier(By.id("Login:LoginScreen:LoginDV:password-inputEl"));
        public static final Identifier LOGIN_BUTTON = new Identifier(By.id("Login:LoginScreen:LoginDV:submit"));
    }

    public final static class PolicyTab {
        public static final Identifier POLICY_TAB_ARROW = new Identifier(By.id("TabBar:PolicyTab"));
        public static final Identifier NEW_SUBMISSION_LINK = new Identifier(By.id("TabBar:PolicyTab:PolicyTab_NewSubmission-itemEl"));
    }

    public final static class NewPolicy {
        public static final class PolicyInfo {
            public static final Identifier ORGANIZATION_TYPE = new Identifier(By.id("SubmissionWizard:LOBWizardStepGroup:SubmissionWizard_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:AccountInfoInputSet:OrganizationType-inputEl"));
            public static final Identifier ORDER_PREFILL_REPORT_BUTTON = new Identifier(By.id("SubmissionWizard:LOBWizardStepGroup:SubmissionWizard_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:AccountInfoInputSet:prefillServiceButton-btnInnerEl"));
            public static final Identifier RATING_COUNTY = new Identifier(By.id("SubmissionWizard:LOBWizardStepGroup:SubmissionWizard_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:AccountInfoInputSet:PolicyAddressDisplayInputSet:RatingCounty-inputEl"));
            public static final Identifier TERM_TYPE = new Identifier(By.id("SubmissionWizard:LOBWizardStepGroup:SubmissionWizard_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:PolicyInfoInputSet:TermType-inputEl"));
            public static final Identifier EFFECTIVE_DATE = new Identifier(By.id("SubmissionWizard:LOBWizardStepGroup:SubmissionWizard_PolicyInfoScreen:SubmissionWizard_PolicyInfoDV:PolicyInfoInputSet:EffectiveDate-inputEl"));
        }

        public final static class NewSubmissions {
            public static final Identifier NAME = new Identifier(By.id("FBNewSubmission:ContactSearchScreen:SolrSearch_FBMPanelSet:searchName-inputEl"));
            public static final Identifier SEARCH_BUTTON = new Identifier(By.id("FBNewSubmission:ContactSearchScreen:SolrSearch_FBMPanelSet:SearchAndResetInputSet:SearchLinksInputSet:Search"));
            public static final Identifier CREATE_NEW_BUTTON = new Identifier(By.id("FBNewSubmission:ContactSearchScreen:SolrSearch_FBMPanelSet:SearchResultsToolbar:NewAccountButton"));
            public static final Identifier PERSON_LINK = new Identifier(By.id("FBNewSubmission:ContactSearchScreen:SolrSearch_FBMPanelSet:SearchResultsToolbar:NewAccountButton:NewAccount_Person-itemEl"));
            public static final Identifier QUOTE_TYPE = new Identifier(By.id("NewSubmission:NewSubmissionScreen:ProductSettingsDV:QuoteType-inputEl"));
            public static final Identifier PRODUCT_OFFERS = new Identifier(By.id("NewSubmission:NewSubmissionScreen:ProductOffersDV:ProductSelectionLV-body"));
            public static final Identifier NEXT_BUTTON = new Identifier(By.id("SubmissionWizard:Next"));
            public static final Identifier TYPE = new Identifier(By.id("FBNewSubmission:ContactSearchScreen:ContactSubtype-inputEl"));
        }

        public final static class CreateAccount {
            public static final Identifier DATE_OF_BIRTH = new Identifier(By.id("CreateAccount:CreateAccountScreen:CreateAccountContactInputSet:DateOfBirth-inputEl"));
            public static final Identifier SSN = new Identifier(By.id("CreateAccount:CreateAccountScreen:CreateAccountContactInputSet:ContactIdentificationInputSet:SSN-inputEl"));
            public static final Identifier ADDRESS1 = new Identifier(By.id("CreateAccount:CreateAccountScreen:AddressInputSet:globalAddressContainer:GlobalAddressInputSet:AddressLine1-inputEl"));
            public static final Identifier CITY = new Identifier(By.id("CreateAccount:CreateAccountScreen:AddressInputSet:globalAddressContainer:GlobalAddressInputSet:City-inputEl"));
            public static final Identifier STATE = new Identifier(By.id("CreateAccount:CreateAccountScreen:AddressInputSet:globalAddressContainer:GlobalAddressInputSet:State-inputEl"));
            public static final Identifier ZIP = new Identifier(By.id("CreateAccount:CreateAccountScreen:AddressInputSet:globalAddressContainer:GlobalAddressInputSet:PostalCode-inputEl"));
            public static final Identifier ADDRESS_TYPE = new Identifier(By.id("CreateAccount:CreateAccountScreen:AddressInputSet:AddressType-inputEl"));
            public static final Identifier UPDATE_BUTTON = new Identifier(By.id("CreateAccount:CreateAccountScreen:ForceDupCheckUpdate-btnInnerEl"));
        }

        public final static class MatchingContacts {
            public static final Identifier RETURN_TO_CREATE_ACCOUNT_LINK = new Identifier(By.id("DuplicateContactsPopup:__crumb__"));
        }

        public final static class SquireEligibility {
            public static final Identifier PERSONAL_LINES_SQUIRE_ELIGIBILITY_QUESTIONS = new Identifier(By.id("SubmissionWizard:SquireEligibilityScreen:SquireEligibilityQuestionSet:QuestionSetsDV:0:QuestionSetLV-body"));
        }

        public final static class Qualification {
            public static final Identifier GENERAL_PREQUAL_QUESTIONS = new Identifier(By.id("SubmissionWizard:SubmissionWizard_PreQualificationScreen:PreQualQuestionSetsDV:QuestionSetsDV:0:QuestionSetLV-body"));
            public static final Identifier PROPERTY_PREQUAL_QUESTIONS = new Identifier(By.id("SubmissionWizard:SubmissionWizard_PreQualificationScreen:PreQualQuestionSetsDV:QuestionSetsDV:1:QuestionSetLV-body"));
            public static final Identifier GENERAL_LIABILITY_PREQUAL_QUESTIONS = new Identifier(By.id("SubmissionWizard:SubmissionWizard_PreQualificationScreen:PreQualQuestionSetsDV:QuestionSetsDV:2:QuestionSetLV-body"));
            public static final Identifier AUTO_PREQUAL_QUESTIONS = new Identifier(By.id("SubmissionWizard:SubmissionWizard_PreQualificationScreen:PreQualQuestionSetsDV:QuestionSetsDV:3:QuestionSetLV-body"));
            public static final Identifier INLAND_MARINE_PREQUAL_QUESTIONS = new Identifier(By.id("SubmissionWizard:SubmissionWizard_PreQualificationScreen:PreQualQuestionSetsDV:QuestionSetsDV:4:QuestionSetLV-body"));
        }

        public final static class OrderPrefillReportFromVerisk {
            public static final Identifier CANCEL_BUTTON = new Identifier(By.id("OrderPrefillReportVeriskPopup:OrderPrefillReportScreen:PrefillCancel-btnInnerEl"));
        }

        public final static class InsuranceScore {
            public static final Identifier ORGANIZATION_TYPE = new Identifier(By.id("SubmissionWizard:LOBWizardStepGroup:CreditReportScreen:CreditReportPanelSet:CreditReportContact-inputEl"));
            public static final Identifier ORDER_INSURANCE_REPORT_BUTTON = new Identifier(By.id("SubmissionWizard:LOBWizardStepGroup:CreditReportScreen:CreditReportPanelSet:OrderCreditReportButton-btnInnerEl"));
        }

        public final static class Locations {
            public static final Identifier NEW_LOCATION_BUTTON = new Identifier(By.id("SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HOLocationsScreen:HOLocationsHOEPanelSet:LocationsEdit_DP_tb:Add-btnInnerEl"));
        }

        public final static class LocationInformation {
            public static final Identifier ACRES = new Identifier(By.id("HOLocationFBMPopup:HOLocation_FBMCV:HOLocation_FBMDetailsDV:acres-inputEl"));
            public static final Identifier NUMBER_OF_RESIDENCE = new Identifier(By.id("HOLocationFBMPopup:HOLocation_FBMCV:HOLocation_FBMDetailsDV:NumberOfResidence-inputEl"));
            public static final Identifier OK_BUTTON = new Identifier(By.id("HOLocationFBMPopup:Update-btnInnerEl"));
            public static final Identifier STANDARDIZE_ADDRESS = new Identifier(By.id("HOLocationFBMPopup:HOLocation_FBMCV:HOLocation_FBMDetailsDV:HOLocationInputSet:StandardizeAddress-btnInnerEl"));
        }

        public final static class PropertyDetail {

            public static final Identifier ADD_BUTTON = new Identifier(By.id("SubmissionWizard:LOBWizardStepGroup:LineWizardStepSet:HOWizardStepGroup:HODwellingHOEScreen:HODwellingSingleHOEPanelSet:DwellingsPanelSet:DwellingListDetailPanel:HODwellingListLV_tb:Add-btnInnerEl"));
        }

    }

    public final static class Policy {

    }
}
