package repository.cc.framework.utils.helpers;

public class CCIDs {

    public static final Identifier LIST_OPTIONS = new Identifier(Identifier.CSS, ".x-boundlist.x-boundlist-floating.x-layer.x-boundlist-default.x-border-box");
    public static final Identifier ESCAPE_CLICKER = new Identifier(Identifier.ID, "QuickJump-inputEl");

    public static class NavBar {
        public static class Elements {
            public static final Identifier LOG_OUT = new Identifier(Identifier.ID, "TabBar:LogoutTabBarLink-textEl");
            public static final Identifier GEAR = new Identifier(Identifier.ID, ":TabLinkMenuButton");
        }
    }
    
    public static class Login {

        public static class Elements {
            public static final Identifier USER_NAME = new Identifier(Identifier.ID, "Login:LoginScreen:LoginDV:username-inputEl");
            public static final Identifier PASSWORD = new Identifier(Identifier.ID, "Login:LoginScreen:LoginDV:password-inputEl");
            public static final Identifier LOG_IN = new Identifier(Identifier.ID, "Login:LoginScreen:LoginDV:submit-btnInnerEl");
        }
    }

    public static class Administration {
        public static class ScriptParameters {
            public static class Elements {
                public static final Identifier BEGINNING_WITH = new Identifier(Identifier.ID, "ScriptParametersPage:ScriptParametersScreen:ScriptParametersLV:FirstSubjectCharacter-inputEl");
            }
        }
    }

    public static class AdvancedSearch {

        public static class Navigation {
            public static final Identifier STEPS[] = {
                    new Identifier(Identifier.ID, "TabBar:SearchTab-btnInnerEl"),
                    new Identifier(Identifier.ID, "Search:MenuLinks:Search_ClaimSearchesGroup:ClaimSearchesGroup_ClaimSearch")
            };

        }

        public static class Elements {
            public static final Identifier CLAIMNUMBER = new Identifier(Identifier.ID, "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:ClaimNumber-inputEl");
            public static final Identifier CLAIMSTATUS = new Identifier(Identifier.ID, "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:ClaimStatus-triggerWrap");
            public static final Identifier LOSSTYPE = new Identifier(Identifier.ID, "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:LossType-triggerWrap");
            public static final Identifier SEARCHFORDATE_SINCE = new Identifier(Identifier.ID, "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:DateSearch:DateSearchRangeValue-triggerWrap");
            public static final Identifier SEARCH_BUTTON = new Identifier(Identifier.ID, "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchAndResetInputSet:Search");
        }
    }

    public static class NewClaim {

        public static class Navigation {
            public static final Identifier STEPS[] = {
                    new Identifier(Identifier.ID, "TabBar:DesktopTab-btnInnerEl", Identifier.WAIT_VISIBLE),
                    new Identifier(Identifier.OFFSET, "TabBar:ClaimTab-btnIconEl", Identifier.WAIT_VISIBLE),
                    new Identifier(Identifier.ID, "TabBar:ClaimTab:ClaimTab_FNOLWizard-textEl")
            };
        }

        public static class Elements {
            public static final Identifier FNOL_WIZARD_NEXT = new Identifier(Identifier.ID, "FNOLWizard:Next");
            public static final Identifier FNOL_WIZARD_FINISH = new Identifier(Identifier.ID, "FNOLWizard:Finish");
            public static final Identifier ERROR_MESSAGE_ELEMENT = new Identifier(Identifier.CLASS, "message", Identifier.WAIT_OPTIONAL);
        }

        public static class SearchOrCreatePolicy {
            public static class Elements {
                public static final Identifier POLICY_ROOT_NUMBER = new Identifier(Identifier.ID, "FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:policyNumber-inputEl");
                public static final Identifier SEARCH_BUTTON = new Identifier(Identifier.ID, "FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:Search");
                public static final Identifier SEARCH_RESULTS = new Identifier(Identifier.ID, "FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:PolicyResultLV-body");
                public static final Identifier DATE_OF_LOSS = new Identifier(Identifier.ID, "FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:Claim_LossDate-inputEl");
                public static final Identifier PROPERTY_RADIO = new Identifier(Identifier.ID, "FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:ClaimMode_option4-inputEl");
            }
        }

        public static class BasicInformation {
            public static class Elements {
                public static final Identifier NAME = new Identifier(Identifier.ID, "FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:BasicInfoDetailViewPanelDV:ReportedBy_Name-inputEl", Identifier.WAIT_VISIBLE);
                public static final Identifier MOBILE = new Identifier(Identifier.ID, "FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:BasicInfoDetailViewPanelDV:FNOLWizard_ContactInfoInputSet:FBContactInfoInputSet:Cell:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl");
                public static final Identifier PRIMARY_PHONE = new Identifier(Identifier.ID, "FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:BasicInfoDetailViewPanelDV:FNOLWizard_ContactInfoInputSet:FBContactInfoInputSet:primaryPhone-inputEl", Identifier.WAIT_VISIBLE);
                public static final Identifier EMAIL = new Identifier(Identifier.ID, "FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:BasicInfoDetailViewPanelDV:FNOLWizard_ContactInfoInputSet:reporter_email-inputEl");
                public static final Identifier VEHICLES_INVOLVED = new Identifier(Identifier.ID, "FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:RightPanel:FNOLWizard_BasicInfoRightPanelSet:3-body");
                public static final Identifier DUPLICATE_CLAIMS = new Identifier(Identifier.ID, "wsTabBar:wsTab_0-btnInnerEl", Identifier.WAIT_OPTIONAL);
                public static final Identifier CLOSE_BUTTON = new Identifier(Identifier.ID, "NewClaimDuplicatesWorksheet:NewClaimDuplicatesScreen:NewClaimDuplicatesWorksheet_CloseButton-btnInnerEl");
            }
        }

        public static final class AddClaimInformation {

            public static class Elements {
                public static final Identifier LOSS_DESCRIPTION = new Identifier(Identifier.ID, "FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:LossDetailsAddressDV:Description-inputEl");
                public static final Identifier LOSS_CAUSE = new Identifier(Identifier.ID, "FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:LossDetailsAddressDV:Claim_LossCause-inputEl");
                public static final Identifier LOSS_ROUTER = new Identifier(Identifier.ID, "FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:LossDetailsAddressDV:Claim_LossRouter-inputEl");
                public static final Identifier LOCATION = new Identifier(Identifier.ID, "FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:LossDetailsAddressDV:AddressDetailInputSetRef:LossDetailsAddressDetailInputSet:LossLocation_Name-inputEl");
                public static final Identifier CITY = new Identifier(Identifier.ID, "FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:LossDetailsAddressDV:AddressDetailInputSetRef:LossDetailsAddressDetailInputSet:FNOLAddressInputSet:City-inputEl");
                public static final Identifier STATE = new Identifier(Identifier.ID, "FNOLWizard:FBClaimWizardStepSet:FNOLWizard_NewLossDetailsScreen:LossDetailsAddressDV:AddressDetailInputSetRef:LossDetailsAddressDetailInputSet:FNOLAddressInputSet:State-inputEl");
            }

        }

        public static final class NewClaimSaved {
            public static class Elements {
                public static final Identifier CLAIM_NUMBER = new Identifier(Identifier.ID, "NewClaimSaved:NewClaimSavedScreen:NewClaimSavedDV:Header");
                public static final Identifier VIEW_NEW_CLAIM = new Identifier(Identifier.ID, "NewClaimSaved:NewClaimSavedScreen:NewClaimSavedDV:GoToClaim-inputEl");
            }
        }
    }

    public static class Claim {
        public static class Navigation {
            public static final Identifier STEPS[] = {
                    new Identifier(Identifier.ID, "TabBar:DesktopTab-btnInnerEl", Identifier.WAIT_VISIBLE),
                    new Identifier(Identifier.OFFSET, "TabBar:ClaimTab-btnIconEl", Identifier.WAIT_VISIBLE)
            };
        }

        public static class Elements {
            public static final Identifier CLAIM_NUMBER = new Identifier(Identifier.ID, "TabBar:ClaimTab:ClaimTab_FindClaim-inputEl", Identifier.WAIT_VISIBLE);
            public static final Identifier FIND_CLAIM = new Identifier(Identifier.ID, "TabBar:ClaimTab:ClaimTab_FindClaim_Button");
            public static final Identifier INSURED_NAME = new Identifier(Identifier.CSS, "span[id*='ClaimInfoBar:Insured-btnInnerEl'] span[class*='infobar_elem_val']");
        }

        public static class SystemCheckWizard {
            public static class Elements {
                public static final Identifier NAME = new Identifier(Identifier.ID, "NormalCreateCheckWizard:CheckWizard_CheckPayeesScreen:NewCheckPayeeDV:PrimaryPayee_Name-inputEl");
                public static final Identifier TYPE = new Identifier(Identifier.ID, "NormalCreateCheckWizard:CheckWizard_CheckPayeesScreen:NewCheckPayeeDV:PrimaryPayee_Type-inputEl");
                public static final Identifier REPORT_AS = new Identifier(Identifier.ID, "NormalCreateCheckWizard:CheckWizard_CheckPayeesScreen:NewCheckPayeeDV:Reportability-inputEl");
                public static final Identifier CHECK_DELIVERY = new Identifier(Identifier.ID, "NormalCreateCheckWizard:CheckWizard_CheckPayeesScreen:NewCheckPayeeDV:DeliveryMethod-inputEl");
                public static final Identifier CHECK_TYPE = new Identifier(Identifier.ID, "NormalCreateCheckWizard:CheckWizard_CheckPayeesScreen:NewCheckPayeeDV:Check_CheckType-inputEl");
                public static final Identifier NEXT_BUTTON = new Identifier(Identifier.ID, "NormalCreateCheckWizard:Next");
                public static final Identifier RESERVE_LINE = new Identifier(Identifier.ID, "NormalCreateCheckWizard:CheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:ReserveLineInputSet:ReserveLine-inputEl");
                public static final Identifier PAYMENT_TYPE = new Identifier(Identifier.ID, "NormalCreateCheckWizard:CheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:Payment_PaymentType-inputEl");
                public static final Identifier PAYMENT_LINE_ITEMS = new Identifier(Identifier.ID, "NormalCreateCheckWizard:CheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:EditablePaymentLineItemsLV-body");
                public static final Identifier LINE_ITEM_TYPE = new Identifier(Identifier.NAME, "LineCategory");
                public static final Identifier AMOUNT = new Identifier(Identifier.NAME, "Amount");
                public static final Identifier FINISH_BUTTON = new Identifier(Identifier.ID, "NormalCreateCheckWizard:Finish");
            }
        }

        public static final class Policy {
            public static final class Navigation {
                public static final Identifier[] STEPS = {new Identifier(Identifier.ID, "Claim:MenuLinks:Claim_ClaimPolicyGroup")};
            }

            public static final class Vehicles {
                public static final class Navigation {
                    public static final Identifier[] STEPS = {
                            new Identifier(Identifier.ID, "Claim:MenuLinks:Claim_ClaimPolicyGroup"),
                            new Identifier(Identifier.ID, "Claim:MenuLinks:Claim_ClaimPolicyGroup:ClaimPolicyGroup_ClaimPolicyVehicles")
                    };
                }

                public static final class Elements {
                    public static final Identifier POLICY_VEHICLES = new Identifier(Identifier.ID, "ClaimPolicyVehicles:ClaimPolicyVehiclesScreen:VehiclesLV-body");
                }
            }
        }

        public static final class LossDetails {
            public static class Navigation {
                public static final Identifier STEPS[] = {
                        new Identifier(Identifier.ID, "Claim:MenuLinks:Claim_ClaimLossDetailsGroup")
                };
            }

            public static class Elements {
                public static final Identifier PROPERTIES_INCIDENTS = new Identifier(Identifier.ID, "ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV:LossDetailsDV:EditableFixedPropertyIncidentsLV-body");
                public static final Identifier VEHICLES_INCIDENTS = new Identifier(Identifier.ID, "ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV:LossDetailsDV:EditableVehicleIncidentsLV-body");
                public static final Identifier EDIT_BUTTON = new Identifier(Identifier.ID, "ClaimLossDetails:ClaimLossDetailsScreen:Edit-btnInnerEl");
                public static final Identifier UPDATE_BUTTON = new Identifier(Identifier.ID, "ClaimLossDetails:ClaimLossDetailsScreen:Update");
                public static final Identifier SALVAGE_STATUS = new Identifier(Identifier.ID, "ClaimLossDetails:ClaimLossDetailsScreen:LossDetailsPanelSet:LossDetailsCardCV:LossDetailsDV:ClaimsDetailInputSet:Status_SalvageStatus-inputEl");
            }
        }

        public static final class Workplan {
            public static final class Navigation {
                public static final Identifier STEPS[] = {
                        new Identifier(Identifier.ID, "Claim:MenuLinks:Claim_ClaimWorkplan")
                };
            }

            public static final class Elements {
                public static final Identifier ACTIVITIES_TABLE = new Identifier(Identifier.ID, "ClaimWorkplan:ClaimWorkplanScreen:WorkplanLV-body");
                public static final Identifier APPROVE_BUTTON = new Identifier(Identifier.ID, "ApprovalDetailWorksheet:ApprovalDetailScreen:ApprovalDetailWorksheet_ApproveButton-btnInnerEl");
            }
        }

        public static final class Incidents {
            public static final class Elements {
                public static final Identifier CREATE_RESERVE = new Identifier(Identifier.ID, "ExposureDetail:ExposureDetailScreen:ExposureDetailScreen_CreateReserveButton");
            }
            public static final class PropertyIncident {
                public static class Elements {
                    public static final Identifier ROOF_DAMAGE_INVOLVED = new Identifier(Identifier.ID, "EditFixedPropertyIncidentPopup:EditFixedPropertyIncidentScreen:FixPropIncidentDetailDV:FixedPropertyIncidentDV:PropertyAttributeInputSet:LossLocation_Roof-inputEl");
                    public static final Identifier MOLD_INVOLVED = new Identifier(Identifier.ID, "EditFixedPropertyIncidentPopup:EditFixedPropertyIncidentScreen:FixPropIncidentDetailDV:FixedPropertyIncidentDV:PropertyAttributeInputSet:LossLocation_Mold-inputEl");
                    public static final Identifier OWNER = new Identifier(Identifier.ID, "EditFixedPropertyIncidentPopup:EditFixedPropertyIncidentScreen:FixPropIncidentDetailDV:FixedPropertyIncidentDV:Owner_Picker-inputEl");
                    public static final Identifier OK_BUTTON = new Identifier(Identifier.ID, "EditFixedPropertyIncidentPopup:EditFixedPropertyIncidentScreen:Update");
                    public static final Identifier ERROR_MESSAGE = new Identifier(Identifier.ID, "EditFixedPropertyIncidentPopup:EditFixedPropertyIncidentScreen:_msgs");
                }
            }
        }

        public static class ActionsMenu {
            public static class Elements {
                public static final Identifier ACTIONS_BUTTON = new Identifier(Identifier.ID, "Claim:ClaimMenuActions");
                public static final Identifier CHOOSE_BY_COVERAGE = new Identifier(Identifier.ID, "Claim:ClaimMenuActions:ClaimMenuActions_NewExposure:NewExposureMenuItemSet:NewExposureMenuItemSet_ByCoverage-itemEl");
                public static final Identifier CHECK = new Identifier(Identifier.ID, "Claim:ClaimMenuActions:ClaimMenuActions_NewTransaction:ClaimMenuActions_NewTransaction_CheckSet-itemEl");
                public static final Identifier REGULAR_PAYMENT = new Identifier(Identifier.ID, "Claim:ClaimMenuActions:ClaimMenuActions_NewTransaction:ClaimMenuActions_NewTransaction_CheckSet:ClaimMenuActions_NewTransaction_Payment-itemEl");
            }
        }

        public static class NewExposure {
            public static class Elements {
                public static final Identifier CLAIMANT = new Identifier(Identifier.ID, "NewExposure:NewExposureScreen:NewExposureDV:NewClaimVehicleDamageDV:ClaimantTypeInputSet:Claimant_Picker-inputEl");
                public static final Identifier TYPE = new Identifier(Identifier.ID, "NewExposure:NewExposureScreen:NewExposureDV:NewClaimVehicleDamageDV:ClaimantTypeInputSet:Claimant_Type-inputEl");
                public static final Identifier VEHICLE = new Identifier(Identifier.ID, "NewExposure:NewExposureScreen:NewExposureDV:NewClaimVehicleDamageDV:ClaimantTypeInputSet:Vehicle_Incident-inputEl");
                public static final Identifier UPDATE_BUTTON = new Identifier(Identifier.ID, "NewExposure:NewExposureScreen:Update");
            }
        }

        public static class Exposures {
            public static class Elements {
                public static final Identifier EXPOSURES_TABLE = new Identifier(Identifier.ID, "ClaimExposures:ClaimExposuresScreen:ExposuresLV-body");
            }
        }

        public static class SetReserves {
            public static class Elements {
                public static final Identifier ADD_BUTTON = new Identifier(Identifier.ID, "NewReserveSet:NewReserveSetScreen:Add");
                public static final Identifier RESERVES_TABLE = new Identifier(Identifier.ID, "NewReserveSet:NewReserveSetScreen:ReservesSummaryDV:EditableReservesLV-body");
                public static final Identifier COST_CATEGORY = new Identifier(Identifier.NAME, "CostCategory");
            }
        }
    }
}
