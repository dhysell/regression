package regression.r3.noclock.policycenter.commercialproperty;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypeCPP;
import repository.gw.enums.Building.FireBurglaryAlarmType;
import repository.gw.enums.Building.HouseKeepingMaint;
import repository.gw.enums.Building.RoofCondition;
import repository.gw.enums.Building.RoofMaintScheduleCPP;
import repository.gw.enums.Building.RoofingType;
import repository.gw.enums.Building.WiringType;
import repository.gw.enums.CPUWIssues;
import repository.gw.enums.CommercialProperty.AdditionalCoverages;
import repository.gw.enums.CommercialProperty.BuildingCoverageValuationMethod;
import repository.gw.enums.CommercialProperty.BusinessPersonalPropertyCoverageValuationMethod;
import repository.gw.enums.CommercialProperty.CommercialProperty_ExclusionsAndConditions;
import repository.gw.enums.CommercialProperty.Condominium;
import repository.gw.enums.CommercialProperty.DiscretionaryPayrollExpense_CP_15_04_NumberOfDays;
import repository.gw.enums.CommercialProperty.PropertyCoverages;
import repository.gw.enums.CommercialProperty.RateType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialProperty;
import repository.gw.generate.custom.CPPCommercialPropertyLine;
import repository.gw.generate.custom.CPPCommercialPropertyLine_Coverages;
import repository.gw.generate.custom.CPPCommercialPropertyLine_ExclusionsConditions;
import repository.gw.generate.custom.CPPCommercialPropertyProperty;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import persistence.globaldatarepo.helpers.CPUWIssuesHelper;

@QuarantineClass
public class CPUnderwriterIssues extends BaseTest {


    public GeneratePolicy myPolicyObj = null;
    public boolean testFailed = false;
    public String failureString = "";
    public List<String> classCodeList = new ArrayList<String>();

    private WebDriver driver;


    @Test
    public void verifyInformationalUWIssues() throws Exception {
        AddressInfo pniAddress = new AddressInfo(true);
        ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>();
        locationList.add(new PolicyLocation(pniAddress, true));

        //COMMERCIAL PROPERTY LINE
        CPPCommercialPropertyLine commercialPropertyLine = new CPPCommercialPropertyLine();
        commercialPropertyLine.setPropertyLineCoverages(new CPPCommercialPropertyLine_Coverages());
        commercialPropertyLine.setPropertyLineExclusionsConditions(new CPPCommercialPropertyLine_ExclusionsConditions());

        //LIST OF COMMERCIAL PROPERTY
        CPPCommercialPropertyProperty property = new CPPCommercialPropertyProperty(pniAddress);

        List<CPPCommercialPropertyProperty> commercialPropertyList = new ArrayList<CPPCommercialPropertyProperty>();
        commercialPropertyList.add(property);

        CPPCommercialProperty commercialProperty = new CPPCommercialProperty();
        commercialProperty.setCommercialPropertyLine(commercialPropertyLine);
        commercialProperty.setCommercialPropertyList(commercialPropertyList);

        ArrayList<LineSelection> lineSelectionList = new ArrayList<LineSelection>();
        lineSelectionList.add(LineSelection.CommercialPropertyLineCPP);
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy(driver);
        myPolicyObj.productType = ProductLineType.CPP;
        myPolicyObj.commercialPropertyCPP = commercialProperty;
        myPolicyObj.lineSelection = lineSelectionList;
        myPolicyObj.commercialPackage.locationList = locationList;
        myPolicyObj.pniContact.setPersonOrCompany(ContactSubType.Company);
        myPolicyObj.pniContact.setCompanyName("Property UWIssues");
        myPolicyObj.polOrgType = OrganizationType.LLC;
        myPolicyObj.handleBlockSubmit = false;
        myPolicyObj.handleBlockSubmitRelease = false;


        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();

        List<persistence.globaldatarepo.entities.CPUWIssues> informationalUWIssueList = CPUWIssuesHelper.getInformationalUWIssues(5);

        List<CPUWIssues> uwIssueList = convertList(informationalUWIssueList);
//		uwIssueList.clear();
        uwIssueList.add(CPUWIssues.Acopyoftheassociationagreementisrequiredpleaseuploadtofile);
        uwIssueList.add(CPUWIssues.ClassCodePublicwarehousescanvarysignificantlyPleaseprovidetypesandquantitiesofproductsstoredmultipleinteriorphotosandlossrunsetcDependingonthetypeofgoodsstoredUnderwritingmayaskforadditionalinformation);
        uwIssueList.add(CPUWIssues.ContactUnderwritingwithdetailsofconditionofroofnoteifacceptableendorsementBP1404WindstormorHailLossesToRoofSurfacingActualCashValueLossSettlementwillbeattached);

        updatePolicyObject(uwIssueList);

        myPolicyObj.generate(GeneratePolicyType.QuickQuote);


        new Login(driver).loginAndSearchSubmission(myPolicyObj);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

        for (CPUWIssues uwIssue : uwIssueList) {
            String uwIssueText = "";
            boolean found = false;
            if (myPolicyObj.commercialPropertyCPP.getCommercialPropertyLine().getUnderwritingIssuesList().contains(uwIssue)) {
                uwIssueText = uwIssue.getValue();
                found = true;
            }//end if
            if (!found) {
                for (CPPCommercialPropertyProperty propertyLocation : myPolicyObj.commercialPropertyCPP.getCommercialPropertyList()) {
                    if (propertyLocation.getUnderwritingIssuesList().contains(uwIssue)) {
                        uwIssueText = uwIssue.getValue().replace("${Location #}", property.getPropertyLocationNumber());
                        break;
                    } //end if
                    for (CPPCommercialProperty_Building building : propertyLocation.getCPPCommercialProperty_Building_List()) {
                        if (building.getUwIssue().equals(uwIssue)) {
                            uwIssueText = uwIssue.getValue().replace("${Location #}", property.getPropertyLocationNumber()).replace("${Property #}", String.valueOf(building.getNumber())).replace("${Class Code}", building.getClassCode());
                            found = true;
                            break;
                        } //end if
                    } //end for
                    if (found)
                        break;
                } //end for
            }//end if(!found)

            UnderwriterIssueType issueType = UnderwriterIssueType.NONE;
            switch (CPUWIssuesHelper.getUWIssueByRuleMessage(uwIssue.getValue()).getResult()) {
                case "Non-Blocking":
                    issueType = UnderwriterIssueType.Informational;
                    break;
                case "Blocks Quote Release":
                    issueType = UnderwriterIssueType.BlockQuoteRelease;
                    break;
                case "Blocks Bind":
                    issueType = UnderwriterIssueType.BlockSubmit;
                    break;
                case "Reject":
                    issueType = UnderwriterIssueType.Reject;
                    break;
                case "Blocks Quote":
                    issueType = UnderwriterIssueType.BlockQuote;
                    break;
                case "Blocks Issuance":
                    issueType = UnderwriterIssueType.BlockIssuance;
                    break;
            }

            if (!uwIssues.isInList(uwIssueText).equals(issueType)) {
                testFailed = true;
                failureString = failureString + uwIssueText + " did NOT generate\n";
            }
        }


        testFailed();

    }//end verifyInformationalUWIssues


    private void updatePolicyObject(List<CPUWIssues> uwIssueList) {

        for (CPUWIssues issue : uwIssueList) {

            if (issue != null) {
                CPPCommercialProperty_Building theBuilding = new CPPCommercialProperty_Building("0532(18)");
                switch (issue) {
                    case AManualPublicProtectionClasshasbeenenteredthatdoesnotmatchtheAutoPublicProtectionClassUnderwriterpleaseverifytheProtectionClassiscorrect:
                        //When under the property Details tab and in the Manual Public Protection Class a number is entered that does not match the Auto Public Protection Class, then display the message.
                        //myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0).setProtectionClassCodeCPP();
                        break;
                    case Acopyoftheassociationagreementisrequiredpleaseuploadtofile:
                        //When Condominium Association Coverage Form CP 00 17 or Condominium Commercial Unit-Owners Coverage Form CP 00 18 is selected display the message.
//						Available when under the building "Details" tab Condominium is select with the option "Association".
//						Available when under the building "Details" tab Condominium is select with the option "Unit Owner".
                        theBuilding = new CPPCommercialProperty_Building(true);
                        theBuilding.setRandomCondominium(Condominium.Association, Condominium.UnitOwner);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case AdditionalBuildingPropertyCP1415isonthebuildingPleaseincludevalueoffixturesmachineryorequipmentinthebuildingamountandshowontheMS_Bvalueestimate:
                        //					When endorsement Additional Building Property CP 14 15 is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.AdditionalBuildingProperty_CP_14_15);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case AdditionalCoveredPropertyCP1410hasbeenselectedIncludeadditionalcoveredpropertyvalueinthelimitofinsuranceasitwillbesubjecttothecoinsuranceclause:
                        //					When Additional Covered Property CP 14 10 is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.AdditionalCoveredProperty_CP_14_10);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case AdditionalInsuredBuildingOwnerCP1219hasbeenselectedprovidecopyofleaseatissuetodocumentinsurableinterest:
                        //					When endorsement Additional Insured � Building Owner CP 12 19 is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.AdditionalInsured_BuildingOwner_CP_12_19);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case AgreedValuerequiresannualstatementofvaluescontractualagreementandupdatedMS_Bnotifytheinsured:
                        //When coverage Building Coverage term Valuation Method term option Agreed Value is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
                        theBuilding.getCoverages().setBuildingCoverage_ValuationMethod(BuildingCoverageValuationMethod.AgreedValue);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case AlarmTypeofhasbeenselectedPleaseprovideUnderwritingwithcopiesofthecontractorcertificates:
                        //When under Burglary And Robbery Protective Safeguards CP 12 11 located on the building on the Details tab and Alarm Type is selected with an option of Central Station with keys, Central Station without keys, or Local Alarm display the message.
                        theBuilding = new CPPCommercialProperty_Building();
                        theBuilding.setBurglaryAndRobberyProtectiveSafeguards_CP_12_11(FireBurglaryAlarmType.CentralStationWithKeys);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case AlarmcredithasbeengivenpleaseverifywithISRBorrequestcertificatefromthealarmcompany:
                        //When any of the following options are selected under the Details tab under the title Protective Safeguards CP 04 11: Automatic Sprinkler System (P-1), Automatic Fire Alarm (P-2), Security Service (P-3), Service Contract (P-4), Automatic Commercial Cooking Exhaust and Extinguishing System (P-5), or Other (P-9), display the message.
                        theBuilding = new CPPCommercialProperty_Building();
                        theBuilding.setProtectiveSafeguards_CP_04_11_P2(true);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case Applicant_Insuredhasclass0844_20_RifleorPistolRangesonthepolicyGeneralLiabilitycoverageisnotavailableforRifleorPistolRangesPleasecontactBrokerageforGeneralLiabilitycoverage:
                        //When class code 0844(20) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("0844(20)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case Applicant_InsuredisamachineryorequipmentdealerwedonothavedealerinventorycoveragePleasecontactBrokerageforcoverage:
                        //When class code 0563(17), 0922(26), or 0933(8) is selected, display the message.
                        classCodeList.clear();
                        classCodeList.add("0563(17)");
                        classCodeList.add("0922(26)");
                        classCodeList.add("0933(8");

                        theBuilding = new CPPCommercialProperty_Building(classCodeList.get(NumberUtils.generateRandomNumberInt(0, classCodeList.size() - 1)));
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case Applicant_InsuredisatradeorvocationalschoolProvidedetailsofoccupancytoUnderwritingforassistanceinclassifyingtherisk:
                        //When class code 0921(21) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("0921(21)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case AutomaticIncreaseof8PERCENTorgreateraresubjecttoUnderwritingapproval:
                        //When the term Auto Increase % is 8% or more on coverages Building Coverage, Business Personal Property Coverage, and/or Property In The Open display the message.
                        theBuilding = new CPPCommercialProperty_Building("0921(21)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case BecauseTheftExclusionCP1033isaddedtoabuildingwithBurglaryAndRobberyProtectiveSafeguardsCP1211nocreditwillbegivenforCP1211:
                        //When on the Details tab under Burglary And Robbery Protective Safeguards CP 12 11, Alarm Type is selected with one of the following options: Central Station with keys, Central Station without keys, or Local Alarm, and Theft Exclusion CP 10 33 is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.TheftExclusion_CP_10_33);
                        theBuilding.setBurglaryAndRobberyProtectiveSafeguards_CP_12_11(FireBurglaryAlarmType.CentralStationWithKeys);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case BuildersRiskinPC9or10withalimitof$500000ormorerequirespriorhomeofficeapprovalProvidephotosanddescriptionofclearspaceforconsideration:
                        //When Builders' Risk Coverage Form CP 00 20 is selected and the protection class for either the Manual Public Protection Class Code or Auto Public Protection Class is a 9 or 10 and a limit of $500,000 or more is entered in display the message.
                        theBuilding = new CPPCommercialProperty_Building(PropertyCoverages.BuildersRiskCoverageForm_CP_00_20);
                        theBuilding.getCoverages().setBuildersRiskCoverageForm_CP_00_20_Limit(501000);
                        theBuilding.setProtectionClassCodeCPP(ProtectionClassCode.Prot10);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case BuildingisTentativeratedpleaseorderaspecificrate:
                        //When Tentative Rate CP 99 93 is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building();
                        theBuilding.setUwIssue(issue);
                        theBuilding.setOverrideRateType(true);
                        theBuilding.setRateType(RateType.Tentative);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case Buildingislessthan50yearsoldpleaseexplainwhyFunctionalBuildingValuationisbeingrequested:
                        //When endorsement Functional Building Valuation CP 04 38 is selected and under the Details tab if the Year built is less than 50 years old, display the message.
                        break;
                    case Buildingisover3storiespleasecontactUnderwritingtoseeifthebuildingiseligibleforcoverage:
                        //When under the property Details tab under # of stories a value greater than 3 is entered in, then display the message.
                        theBuilding = new CPPCommercialProperty_Building();
                        theBuilding.setUwIssue(issue);
                        theBuilding.setNumStories(4);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case BuildinglimitforFunctionalBuildingValuationincludescostofdemolitionandincreasedcosttorebuildaccordingtocurrentordinancesandlaw:
                        //When endorsement Functional Building Valuation CP 04 38 is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.FunctionalBuildingValuation_CP_04_38);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case BurglaryandRobberyProtectiveSafeguardshasbeenaddedexplaintheconditionstotheinsured:
                        //When endorsement Burglary And Robbery Protective Safeguards CP 12 11 is selected display the message.
                        break;
                    case BusinessIncomeexceeds$150000Pleasehaveapplicant_insuredcompleteaBusinessIncomeReport_WorkSheetCP1515orprovideotherfinancialstatementsthatshowthesameinformation:
                        //When form Business Income Coverage Form is selected and the limit is over $150,000, display the message.
                        theBuilding = new CPPCommercialProperty_Building(true);
                        theBuilding.getAdditionalCoverages().setBusinessIncomeCoverageForm(true);
                        theBuilding.getAdditionalCoverages().setBusinessIncomeCoverageForm_Limit(160000);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case BusinessIncomeexceeds$150000ThelimithasbeenincreasedfromapriorapprovaloftoPleasecontactUnderwritingregardinganexceptionPleasehaveapplicant_insuredcompleteaBusinessIncomeReport_WorkSheetCP1515orprovideotherfinancialstatementsthatshowthesameinformation:
                        //When Business Income Coverage Form has a limit over $150,000, and existed in the prior policy period and the limit for the current policy period is higher than the limit in the prior policy period, then display the message. display the message.
                        //SPECIAL CASE
                        break;
                    case BusinessPersonalPropertylimitisover$200000inahazardousclass2400_1_PleasesubmittoUnderwritingforreviewUnderwriterpleasereviewwithUnderwritersupervisor:
                        //When class code 2400(1) is selected and coverage Business Personal Property term Limit is in excess of $200,000 display the message.
                        theBuilding = new CPPCommercialProperty_Building("2400(1)");
                        theBuilding.setUwIssue(issue);
                        theBuilding.getCoverages().getBuildingCoverageList().clear();
                        theBuilding.getCoverages().getBuildingCoverageList().add(PropertyCoverages.BusinessPersonalPropertyCoverage);
                        theBuilding.getCoverages().setBusinessPersonalPropertyCoverage_Limit("222222");
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case BusinessPersonalPropertylimitisover$200000inahazardousclass2400_1_ThelimithasbeenincreasedfromapriorapprovaloftoPleasecontactUnderwritingregardinganexceptionUnderwriterpleasereviewwithUnderwritersupervisor:
                        //When class code 2400(1) coverage Business Personal Property existed in the prior policy period and the limit for the current policy period is higher than the limit in the prior policy period and the limit is above $200,000, then display the message.
                        //SPECIAL CASE
                        break;
                    case ClassCode0532_18_Restaurants_nocookingisforsaleoffoodproductslikeretailbakeriesandbeveragesexcludingalcoholicbeverages:
                        //When class code 0532(18) is selected, display the message.
                        theBuilding = new CPPCommercialProperty_Building("0532(18)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode0567_138_SoapMfgisonthepolicyPleaseprovideUnderwritingasamplelabelshowingtheingredientsTreatyexceptionisrequiredforliabilitycoverage:
                        //					When class code 0567(138) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("0567(138)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode0567_138_SoapMfgisonthepolicySmallhobbyfarmermarkettypeofexposuresaregenerallyacceptableLargercommercialventurearenotduetochemicalsandheavyfireload:
                        //When class code 0567(138) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("0567(138)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode0567_92_RailroadConstruction_contractorsarenoteligibleforGeneralLiabilityContactUnderwritingregardingtypeofpropertystoredlocationetcforpropertyconsideration:
                        //When Class Code 0567(92) is selected, display the message.
                        theBuilding = new CPPCommercialProperty_Building("0567(92)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode0570_25_PestcontrolserviceswithchemicalstorageisahazardousclassInordertoevaluateriskpleaseforwardinteriorphotosofchemicalstorageareas:
                        //When Class Code 0570(25) is selected, display the message.
                        theBuilding = new CPPCommercialProperty_Building("0570(25)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode0570_25_isonthepolicyRefertoUnderwritingduetoflammableandcombustiblematerialsandtoreviewthelosscontrolproceduresforchemicalstorage:
                        //When Class Code 0570(25) is selected, display the message.
                        theBuilding = new CPPCommercialProperty_Building("0570(25)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode0570_27_RidingAcademiesisonthepolicypleasecontactBrokerageforGeneralLiabilitycoverage:
                        //When class code 0570(27) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("0570(27)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode0702_7_isonthepolicyThedwellingmustbeinsuredtoreplacementvalueusingMS_B:
                        //When class code 0702(7) is selected, display the message.
                        theBuilding = new CPPCommercialProperty_Building("0702(7)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode0844_23_isonthepolicyGeneralLiabilitycoverageisnotavailableforthisclassofbusiness:
                        //When class code 0844(23) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("0844(23)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode0921_23_TattooParlorsisonthepolicyCrimecoverageislimitedto$10000forInsideThePremisesTheftOfMoneyAndSecuritiesandOutsideThePremisesIfadditionalcoverageisrequiredcontactUnderwritingregardinganexception:
                        //When class code 0921(23) is selected with coverage Inside The Premises – Theft Of Money And Securities or Outside The Premises with a limit that exceeds $10,000, then display the message.
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setInsideThePremises_TheftOfMoneyAndSecurities(true);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setInsideThePremises_TheftOfMoneyAndSecurities_Limit(11000);
                        theBuilding = new CPPCommercialProperty_Building("0921(23)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode0921_23_TattooParlorsisonthepolicyCrimecoverageislimitedto$10000forInsideThePremisesTheftOfMoneyAndSecuritiesandOutsideThePremisesThelimithasbeenincreasedfromapriorapprovaloftoPleasecontactUnderwritingregardinganexception:
                        //When class code 0921(23) and coverage Inside The Premises – Theft Of Money And Securities or Outside The Premises existed in the prior policy period and the limit for in the current policy period is higher than the limit in the prior policy period and the limit is above $10,000, then display the message.
                        //SPECIAL CASE
                        break;
                    case ClassCode0921_23_TattooParlorsisonthepolicyGeneralLiabilitycoverageisnotavailablePleasecontactBrokeragefordesiredcoverage:
                        //When class code 0921(23) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("0921(23)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode0931_3_CarWasheshavesignificantbuiltinequipmentMakesurethevalueisincludedinthebuilding:
                        //When class code 0931(3) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("0931(3)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode2800_12_TextileMfgotherthanwearingapparelTextileSpinningWeavingorKnittingMillsisonthepolicyProvideUnderwritingwithlosscontrolprogramregardingfabricdustandlint:
                        //When class code 2800(12) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("2800(12)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode2800_12_TextileMfgotherthanwearingapparelTextileSpinningWeavingorKnittingMillsisonthepolicyThisclassofbusinessisextremelyhazardousProvideUnderwritingwithinteriorphotosforconsiderationIfwrittenalosscontrolreportmaybeordered:
                        //When class code 2800(12) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("2800(12)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode3009_12_TextileProductsMfgwearingapparelisonthepolicyProvideUnderwritingwithlosscontrolprogramregardingfabricdustandlint:
                        //When class code 3009(12) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("3009(12)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode3009_12_TextileProductsMfgwearingapparelisonthepolicyThisclassofbusinessisextremelyhazardousProvideUnderwritingwithinteriorphotosforconsiderationIfwrittenalosscontrolreportmaybeordered:
                        //When class code 3009(12) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("3009(12)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCode6850_48_isonthepolicyUnderwriterpleasereviewlosscontrolprocedurestopreventsparksfromignitingproperty:
                        //When class code 6850(48) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("6850(48)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCodePublicwarehousescanvarysignificantlyPleaseprovidetypesandquantitiesofproductsstoredmultipleinteriorphotosandlossrunsetcDependingonthetypeofgoodsstoredUnderwritingmayaskforadditionalinformation:
                        //When class code 1230(2), 1220, 1213, 1211(3), or 1212 is selected display the message.
                        classCodeList.clear();
                        classCodeList.add("1230(2)");
                        classCodeList.add("1220");
                        classCodeList.add("1213");
                        classCodeList.add("1211(3)");
                        classCodeList.add("1212");

                        theBuilding = new CPPCommercialProperty_Building(classCodeList.get(NumberUtils.generateRandomNumberInt(0, classCodeList.size() - 1)));
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ClassCodehasbeenselectedDetermineifEquipmentBreakdowntreatyexceptionisrequiredforthisclass:
                        //When Equipment Breakdown Enhancement Endorsement IDCP 31 1002 is selected under any building and any of the following class codes are selected from either Commercial Property or Commercial General Liability then display the message.
                        //					Commercial Property Class Codes:
                        //						6810(15), 6900(17), 6850(30), 6810(3), 6850(5), 5500(4), 6009(6), 6009(14), 5500(14), 5759(2), 1400(5), 2300(4), 1700, or 3959(10).
                        //						Commercial General Liability Class Codes:
                        //						56911, 56912, 51958, 54077, 92445, 99943, 57716, 57725,

                        //SPECIAL CASE
                        break;
                    case ClassCodehasbeenselectedDetermineifEquipmentBreakdowntreatyexceptionisrequiredforthisclass2:
                        //When Equipment Breakdown Enhancement Endorsement IDCP 31 1002 is selected under any building and any of the following type of Property In The Open is selected under the details tab, display the message.
                        //					Property Type:
                        //						15a, 15b, 20a, 20b, 29, 37a, or 37b,
                        break;
                    case ClassCodeisonthepolicyWarehouseshaveaBaileeexposureandWarehouseOperatorsLegalLiabilityexposureWesternCommunityInsuranceCompanydoesnotprovidecoverageforthesecoveragesPleasecontactBrokeragefordesiredcoverage:
                        //When class code 0532(20), 0432(1), 1230(2), 1220, 1213, 1211(3), or 1212 is selected display the message.
                        classCodeList.clear();
                        classCodeList.add("0532(20)");
                        classCodeList.add("0432(1)");
                        classCodeList.add("1230(2)");
                        classCodeList.add("1220");
                        classCodeList.add("1213");
                        classCodeList.add("1211(3)");
                        classCodeList.add("1212");

                        theBuilding = new CPPCommercialProperty_Building(classCodeList.get(NumberUtils.generateRandomNumberInt(0, classCodeList.size() - 1)));
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ContactUnderwritingtoestablishEquipmentBreakdownreinsurance:
                        //This is for the combined limits of the whole Commercial Property Line. When Equipment Breakdown Enhancement Endorsement IDCP 31 1002 is selected and the combined Limits for Building Coverage, Business Personal Property Coverage, Business Income Coverage Form, Property In The Open, Personal Property Of Others, is $10,000,000 or more display the message.
                        //SPECIAL CASE
                        break;
                    case ContactUnderwritingwithdetailsofconditionofroofnoteifacceptableendorsementBP1404WindstormorHailLossesToRoofSurfacingActualCashValueLossSettlementwillbeattached:
                        //When under the property Details tab under Roof condition the option Wear and Tear is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building(true);
                        theBuilding.setRoofCondition(RoofCondition.HasSomeWearAndTear);
                        theBuilding.setUwIssue(issue);
                        theBuilding.setFillOutAllFields(true);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ContactUnderwritingwithdetailsregardingwhenrepairsareanticipated:
                        //When under the property Details tab under Roof condition the option Major Damage is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building(true);
                        theBuilding.setRoofCondition(RoofCondition.HasMajorDamage);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case CostestimatoryearisrequiredatPolicyIssuancePleaseverifythatwehavereceivedthecostestimatorandentertheyearbeforeapproving:
                        //When under the details tab on Cost estimator year if the field is left blank and Cost estimator not required is not checked, then display the message.
                        break;
                    case DiscretionaryPayrollExpenseCP1504hasmorethan180daysselectedpleasecontactUnderwritingforapproval:
                        //When endorsement Discretionary Payroll Expense CP 15 04 is selected and the Number of Days is more than 180 display the message.
                        ArrayList<String> numberOfDays = new ArrayList<String>();
                        numberOfDays.add("270");
                        numberOfDays.add("360");

                        theBuilding = new CPPCommercialProperty_Building(true);
                        theBuilding.getAdditionalCoverages().setBusinessIncomeCoverageForm(true);
                        theBuilding.getAdditionalCoverages().setDiscretionaryPayrollExpense_CP_15_04(true);
                        theBuilding.getAdditionalCoverages().setDiscretionaryPayrollExpense_CP_15_04_NumberOfDays(DiscretionaryPayrollExpense_CP_15_04_NumberOfDays.TwoHundredSeventy);

                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case DocumentationofthetotalguttingwillberequiredBuildingisinexcellentconditionandreflectsgoodhousekeepingandmaintenanceSubmitrequeststoUnderwritingonatrialbasiswiththeappropriateinformationStructureswithoutsolidconcretefoundationsarenoteligible:
                        //When the question Has the property been gutted out to the framing and then rebuilt, including drywall, wiring, plumbing, and heating? is answered with Yes, then display the message.
                        break;
                    case ElectronicDataisnoteligibleforthispolicypleaseremovethecoverage:
                        //When Electronic Data (located under Commercial Property Line on Coverages tab) is selected with Builders Risk Coverage Form CP 00 20 (located under the Property on the Coverages tab), then display the message. Please attach the rule to the Property object Builders’ Risk Coverage Form CP 00 20.

                        break;
                    case ElectronicDatalimitinexcessof$50000requirespriorapprovalPleaseprovidedetailsregardingfirewallsvirusprotectionandoffpremisesbackupprocedures:
                        //When the coverage Electronic Data is selected and the Limit for that coverage is over $50,000 display the message.

                        break;
                    case ElectronicDatalimitisover$50000ThelimithasbeenincreasedfromapriorapprovaloftoPleasecontactUnderwritingregardinganexceptionPleaseprovidedetailsregardingfirewallsvirusprotectionandoffpremisesbackupprocedures:
                        //When the coverage Electronic Data existed in the prior policy period and the limit for the current policy period is higher that the limit in the prior policy period and the limit is above $50,000, then display the message.
                        break;
                    case EquipmentBreakdownEnhancementEndorsementIDCP311002isonthepolicyThisendorsementdoesnotextendcoveragetoBuildersRisk:
                        // When Equipment Breakdown Enhancement Endorsement IDCP 31 1002 is selected at the line level then display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.EquipmentBreakdownEnhancementEndorsementID_CP_31_1002);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ExteriorHouseKeepingandMaintenanceneedsimprovementpleaseprovideUnderwritingwithmoredetails:
                        //When the question Exterior House Keeping and Maintenance is answered with Needs Improvement the display the message.
                        theBuilding = new CPPCommercialProperty_Building();
                        theBuilding.setUwIssue(issue);
                        theBuilding.setExteriorHousekeepingMaint(HouseKeepingMaint.NeedsImprovement);
                        theBuilding.setInteriorHousekeepingMaint(HouseKeepingMaint.NeedsImprovement);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ExtraExpenseCoverageFormCP0050exceeds$150000Pleasehaveapplicant_insuredcompleteaBusinessIncomeReport_WorkSheetCP1515orprovideotherfinancialstatementsthatshowthesameinformation:
                        //When form Extra Expense Coverage Form CP 00 50 is selected and the limit is over $150,000, display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.ExtraExpenseCoverageForm_CP_00_50);
                        theBuilding.getAdditionalCoverages().setExtraExpenseCoverageForm_CP_00_50_limit(160000);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ExtraExpenseCoverageFormCP0050exceeds$150000ThelimithasbeenincreasedfromapriorapprovaloftoPleasecontactUnderwritingregardinganexceptionPleasehaveapplicant_insuredcompleteaBusinessIncomeReport_WorkSheetCP1515orprovideotherfinancialstatementsthatshowthesameinformation:
                        //When form Extra Expense Coverage Form CP 00 50 has a limit that is over $150,000, and existed in the prior policy period and the limit for the current policy period is higher than the limit in the prior policy period, then display the message.
                        break;
                    case FlatroofsneedregularmaintenanceNotifyapplicant_insuredthatincreasedlosscontrolisrecommendedPolicyhaslimitationsforwaterdamagewearandtearandmaintenance:
                        //When under the property Details tab under Maintenance Schedule and the option Every 3 - 5 years is selected, then display the message.
                        theBuilding = new CPPCommercialProperty_Building();
                        theBuilding.setUwIssue(issue);
                        theBuilding.setFlatRoof(true);
                        theBuilding.setRoofMaintSchedule(RoofMaintScheduleCPP.Every3_5Years);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case FoodContamination_BusinessInterruptionAndExtraExpense_CP1505Limitexceeds$25000PleasecontactUnderwritingforapproval:
                        //When endorsement Food Contamination (Business Interruption And Extra Expense) CP 15 05 is selected and the limit for Annual Aggregate Food Contamination Limit is over $25,000 but is $50,000 or less display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.FoodContaminationBusinessInterruptionAndExtraExpense_CP_15_05);
                        theBuilding.getAdditionalCoverages().setFoodContamination_BusinessInterruptionAndExtraExpense_CP_15_05_AnnualAggregateLimit(26000);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case FoodContamination_BusinessInterruptionAndExtraExpense_CP1505Limitisinexcessof$50000whichrequiresamaintenanceagreementbeforecoveragecanbebound:
                        //When endorsement or Food Contamination (Business Interruption And Extra Expense) CP 15 05 is selected and the limit for Annual Aggregate Food Contamination Limit is over $50,000 display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.FoodContaminationBusinessInterruptionAndExtraExpense_CP_15_05);
                        theBuilding.getAdditionalCoverages().setFoodContamination_BusinessInterruptionAndExtraExpense_CP_15_05_AnnualAggregateLimit(52000);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case FoodContamination_BusinessInterruptionAndExtraExpense_CP1505Limitisover$50000ThelimithasbeenincreasedfromapriorapprovaloftoPleasecontactUnderwritingregardinganexception:
                        //When endorsement Food Contamination (Business Interruption And Extra Expense) CP 15 05 limit for Annual Aggregate Food Contamination Limit is over $50,000 and existed in the prior policy period and the limit for the current policy period is higher than the limit in the prior policy period, then display the message.

                        break;
                    case FunctionalBuildingValuationisrequestedprovideexteriorinteriorphotosincludingheatingplumbingandelectricalSubmittoUnderwritingforconsideration:
                    case FunctionalBuildingValuationistobeusedforolderbuildingsthatthearchitecturalstyleisobsoleteorunnecessaryduetocurrentuseofthebuildingBuildingmusthavesignificantupgradeswhereoriginalmaterialsarenolongeravailableandbewellmaintainedMS_Bisrequired:
                        //When endorsement Functional Building Valuation CP 04 38 is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.FunctionalBuildingValuation_CP_04_38);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case HealthCareFacilitiesHomesfortheagedandhomesforthephysicallyhandicappedororphanedInpatientalcoholanddrugtreatmentandMentalarenoteligibleforGeneralLiabilityPleasecontactBrokeragefordesiredcoverage:
                        //When class code 0852(1) or 0852(2)is selected display the message.
                        classCodeList.clear();
                        classCodeList.add("0852(1)");
                        classCodeList.add("0852(2)");

                        theBuilding = new CPPCommercialProperty_Building(classCodeList.get(NumberUtils.generateRandomNumberInt(0, classCodeList.size() - 1)));
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case IftheinsuredhasanobligationtoprotecttheinterestofContractorsorsub_contractorsinpoliciescoveringtenantsorlesseesofpremisesorTenantslesseesconcessionairesorexhibitorsinpoliciescoveringgenerallesseesmanagersoroperatorsofpremisescoverageisnoteligibleforLegalLiabilityFormpleasecontactBrokerageforcoverage:
                        //When Legal Liability Coverage Form CP 00 40 is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building(PropertyCoverages.LegalLiabilityCoverageForm_CP_00_40);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case IncreasetheBusinessIncomeLimittoreflecttheadditionalcoverageinDiscretionaryPayrollExpenseCP1504:
                        //When endorsement Discretionary Payroll Expense CP 15 04 is selected, display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.DiscretionaryPayrollExpense_CP_15_04);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case InsideThePremisesTheftOfMoneyAndSecuritieshasexceeded$30000UnderwritingapprovalisrequiredProvidedetailssuchasnumberofbankdepositsperweekaverageamountofdepositaverageamountkeptonpremisestypeofsafeguardsorsafeetc:
                        //When coverage Inside The Premises – Theft Of Money And Securities is selected and the limit is over $30,000. Display the message.
                        break;
                    case InsideThePremisesTheftOfMoneyAndSecuritieslimitisover$30000ThelimithasbeenincreasedfromapriorapprovaloftoPleasecontactUnderwritingregardinganexceptionProvidedetailssuchasnumberofbankdepositsperweekaverageamountofdepositaverageamountkeptonpremisestypeofsafeguardsorsafeetc:
                        //When coverage Inside The Premises – Theft Of Money And Securities limit is over $30,000 and existed in the prior policy period and the limit for the current policy period is higher than the limit in the prior policy period, then display the message.
                        break;
                    case InteriorHouseKeepingandMaintenanceneedsimprovementpleaseprovideUnderwritingwithmoredetails:
                        //When the question Interior House Keeping and Maintenance is answered with Needs Improvement then display the message.
                        break;
                    case LiabilitycoverageforClassCode0567_84_PileDrivingcontractorsstorageisnotavailablePleasecontactBrokeragefordesiredcoverage:
                        //When Class Code 0567(84) is selected, display the message.
                        theBuilding = new CPPCommercialProperty_Building("0567(84)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case LiabilitycoverageforClassCode0567_85_PipelineConstructioncontractorsstorageisnotavailablePleasecontactBrokeragefordesiredcoverage:
                        //When Class Code 0567(85) is selected, display the message.
                        theBuilding = new CPPCommercialProperty_Building("0567(85)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ManualentryofLongitudeandLatituderequiresUnderwritingapproval:
                        //When Longitude and Latitude is manually entered, then display the message.
                        break;
                    case Manufactured_MobileHomeisolderthan30yearsRefertoUnderwritingforconsideration:
                        //When Construction type is selected with Manufactured/Mobile Home and the year entered into Year built is older than 30 years, then display the message.
                        theBuilding = new CPPCommercialProperty_Building();
                        theBuilding.setUwIssue(issue);
                        theBuilding.setConstructionType(ConstructionTypeCPP.ManufacturedMobileHome);
                        theBuilding.setYearBuilt(1985);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case Mercantileoccupanciesover15000squarefeetarespecificallyratedContactUnderwritingforassistance:
                        //When class code 0433, 0434, 0432(2), or 0431 is selected display the message.
                        classCodeList.clear();
                        classCodeList.add("0433");
                        classCodeList.add("0434");
                        classCodeList.add("0432(2)");
                        classCodeList.add("0431");

                        theBuilding = new CPPCommercialProperty_Building(classCodeList.get(NumberUtils.generateRandomNumberInt(0, classCodeList.size() - 1)));
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case MiscellaneousRealPropertyCoverageinexcessof$50000requirespriorapprovalSubmitacopyoftheAssociationAgreementforreview:
                        //When endorsement Condominium Commercial Unit-Owners Optional Coverages CP 04 18 is selected and the Term Miscellaneous Real Property Limit has a limit over $50,000, then display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.CondominiumCommercialUnit_OwnersOptionalCoverages_CP_04_18);
                        theBuilding.getAdditionalCoverages().setCondominiumCommercialUnit_OwnersOptionalCoverages_CP_04_18_MiscellaneousRealPropertyLimit(52000);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case MiscellaneousRealPropertyCoverageisover$50000ThelimithasbeenincreasedfromapriorapprovaloftoPleasecontactUnderwritingregardinganexceptionSubmitacopyoftheAssociationAgreementforreview:
                        //When endorsement Condominium Commercial Unit-Owners Optional Coverages CP 04 18 is selected and the Term Miscellaneous Real Property Limit has a limit over $50,000, and existed in the prior policy period and the limit for the current policy period is higher than the limit in the prior policy period, then display the message.
                        break;
                    case ModelhomefurnishingscanbeincludedasBusinessPersonalPropertyorcoveredintheInlandMarinesectionVisitwithclientregardingbestapproachforthem:
                        //When class code 0702(5) is selected, display the message.
                        theBuilding = new CPPCommercialProperty_Building("0702(5)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case Notifytheapplicant_insuredthatCP1054WindorHailExclusionappliestopropertyonthispolicy:
                        //When "Windstorm Or Hail Exclusion CP 10 54" is selected Yes display the message.
                        theBuilding = new CPPCommercialProperty_Building(CommercialProperty_ExclusionsAndConditions.WindstormOrHailExclusion_CP_10_54);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case Notifytheapplicant_insuredthatCP1055VandalismExclusionappliesto:
                        //When endorsement "Vandalism Exclusion CP 10 55" is selected, display the message.
                        theBuilding = new CPPCommercialProperty_Building(CommercialProperty_ExclusionsAndConditions.VandalismExclusion_CP_10_55);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case OrdinanceorLawCoverageCP0405andtheCombinedLimitofInsuranceLimitisover$100000ThelimithasbeenincreasedfromapriorapprovaloftoPleasecontactUnderwritingregardinganexceptionPleaseprovidedetailsofbuildingupdatesforelectricalplumbingheatingandlifesafetyWhatordinanceisapplicant_insuredconcernedabout:
                        //"When endorsement ""Ordinance or Law Coverage CP 04 05"" term ""Coverage B And C Combined Limit Of Insurance"" meets the following conditions:
//						Current policy period limit exceeds $100,000 and
//						Prior policy period limit exceeds $100,000 and
//						The current policy period limit is higher than the prior policy priod limit by 10%
                        break;
                    case OrdinanceorLawCoverageCP0405andtheDemolitionCostLimitisover$50000ThelimithasbeenincreasedfromapriorapprovaloftoPleasecontactUnderwritingregardinganexceptionPleaseprovideinformationregardingcostofdemolition:
                        //"When ""Ordinance or Law Coverage CP 04 05"" term ""Coverage B - Demolition Cost Coverage"" meets the following conditions:
//						Current policy period limit exceeds $50,000 and
//						Prior policy period limit exceeds $50,000 and
//						The current policy period limit is higher than the prior policy priod limit by 10%
                        break;
                    case OrdinanceorLawCoverageCP0405andtheIncreasedCostOfConstructionCoverageLimitisover$50000ThelimithasbeenincreasedfromapriorapprovaloftoPleasecontactUnderwritingregardinganexceptionPleaseprovideinformationregardingtheincreasedcostofconstruction:
                        //"When ""Ordinance or Law Coverage CP 04 05"" term ""Coverage C - Increased Cost Of Construction Coverage"" meets the following conditions:
//						Current policy period limit exceeds $50,000 and
//						Prior policy period limit exceeds $50,000 and
//						The current policy period limit is higher than the prior policy priod limit by 10%
                        break;
                    case OrdinanceorLawCoverageCP0405hasbeenselectedandtheCombinedLimitofInsuranceLimitisinexcessof$100000thisrequiresUnderwritingapprovalPleaseprovidedetailsofbuildingupdatesforelectricalplumbingheatingandlifesafetyWhatordinanceisapplicant_insuredconcernedabout:
                        //When endorsement "Ordinance or Law Coverage CP 04 05" and a limit is entered in the "Coverage B And C Combined Limit Of Insurance" over $100,000, display the message.
                        break;
                    case OrdinanceorLawCoverageCP0405hasbeenselectedandtheDemolitionCostLimitisinexcessof$50000thisrequiresUnderwritingapprovalPleaseprovideinformationregardingcostofdemolition:
                        //When "Ordinance or Law Coverage CP 04 05" is selected and a limit is entered in the "Coverage B - Demolition Cost Coverage" over $50,000, display the message.
                        break;
                    case OrdinanceorLawCoverageCP0405hasbeenselectedandtheDemolitionCostLimitisinexcessof$50000thisrequiresUnderwritingapprovalPleaseprovideinformationregardingcostofincreasedcostofconstruction:
                        //When "Ordinance or Law Coverage CP 04 05" is selected and a limit is entered in the "Coverage C - Increased Cost Of Construction Coverage" over $50,000, display the message.
                        break;
                    case OutdoorSignsCP1440hasalimitover$50000thiswillrequireUnderwritingapprovalPleaseprovidephotosofthesignandinformationregardingvalue:
                        //When endorsement "Outdoor Signs CP 14 40" is selected and the limit under "Property In The Open" is over $50,000, display the message.
                        break;
                    case OutdoorSignsCP1440isonthebuildingpleasesendphotosofthesigntoUnderwriting:
                        //When endorsement "Outdoor Signs CP 14 40" is selected, display the message.
                        break;
                    case OutsideThePremiseshasexceeded$30000UnderwritingapprovalisrequiredProvidedetailssuchasnumberofbankdepositsperweekaverageamountofdepositaverageamountkeptonpremisestypeofsafeguardsorsafeetc:
                        //When coverage "Outside The Premises" and the limit is over $30,000. Display the message.
                        break;
                    case OutsideThePremiseslimitisover$30000ThelimithasbeenincreasedfromapriorapprovaloftoPleasecontactUnderwritingregardinganexceptionProvidedetailssuchasnumberofbankdepositsperweekaverageamountofdepositaverageamountkeptonpremisestypeofsafeguardsorsafeetc:
                        //"When coverage ""Outside The Premises"" term ""Limit"" meets the following conditions:
//						Current policy period limit exceeds $30,000 and
//						Prior policy period limit exceeds $30,000 and
//						The current policy period limit is higher than the prior policy priod limit by 10%
                        break;
                    case PhotoyearisrequiredatPolicyIssuancePleaseverifythatwehavereceivedthephotosandentertheyearbeforeapproving:
                        //When the "Photo year" is left blank, then display this message.
                        break;
                    case PleasedescribeconditionoftherooffortheUnderwritersconsideration:
                        //When under the property Details tab under "Roof condition" the option "Other" is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
                        theBuilding.setRoofCondition(RoofCondition.Other);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case PleaseverifyaddressonpolicylocationsCouldresultinaProtectionClasschangePleasecontactUnderwritingwithdetails:
                        //If Smarty Streets doesn't return a valid address and the user assigns a Manual Public Protection Class of 1-10. (side note the Manual Public Protection Class is located under a Property on the details tab.)
                        break;
                    case Propertycoveragewrittenonspecialformbasislimitscoveragefortheftofjewelrywatchesjewelspearlsetcto$2500:
                        //When class code 0565(2) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("0565(2)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ProvideUnderwritingwithapplicant_insuredslosscontrolprogramtoavoidspontaneouscombustionofhay:
                        //When class code 0570(14) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("0570(14)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ProvideUnderwritingwithjustificationastowhythefixturesmachineryorequipmentnotattachedtothebuildingshouldnotbeconsideredBusinessPersonalProperty:
                        //When endorsement "Additional Building Property CP 14 15" is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.AdditionalBuildingProperty_CP_14_15);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ProvideUnderwritingwithmaintenanceprocedures:
                        //When class code 0913(2) or 0912(1) display the message.
                        classCodeList.clear();
                        classCodeList.add("0913(2)");
                        classCodeList.add("0912(1)");

                        theBuilding = new CPPCommercialProperty_Building(classCodeList.get(NumberUtils.generateRandomNumberInt(0, classCodeList.size() - 1)));
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case RadioOrTelevisionAntennasCP1450andthePropertyInTheOpenlimitisover$50000thiswillrequireUnderwritingapprovalPleaseprovidephotosheighthowtheyarestabilizedandinformationregardingvalue:
                        //When endorsement "Radio Or Television Antennas CP 14 50" is selected and the limit under "Property In The Open" is over $50,000, display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.RadioOrTelevisionAntennas_CP_14_50);
                        theBuilding.getAdditionalCoverages().setRadioOrTelevisionAntennas_CP_14_50_limit(52000);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ReplacementCostdoesnotapplytocontentsofaresidenceInformclientofthislimitationorchangeittoActualCashValue:
                        //When class code 0196 is selected and under the coverage "Business Personal Property Coverage" term "Valuation Method" and term option "Replacement cost" is selected, then display the message.
                        theBuilding = new CPPCommercialProperty_Building(PropertyCoverages.BusinessPersonalPropertyCoverage);
                        theBuilding.setClassCode("0196");
                        theBuilding.getCoverages().setBusinessPersonalPropertyCoverage_ValuationMethod(BusinessPersonalPropertyCoverageValuationMethod.ReplacementCost);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ReplacementcosthasbeenrequestedonBusinessPersonalPropertyCoverageThepolicyhasrestrictionsonartantiquesorrarearticlesAspecializedfloatermaybeavailablethroughBrokerageInformapplicantlimitationofcoverage:
                        //When "Business Personal Property Coverage" is selected and the term "Valuation Method" is selected with the term option of "Replacement Cost" display the message.
                        theBuilding = new CPPCommercialProperty_Building(PropertyCoverages.BusinessPersonalPropertyCoverage);
                        theBuilding.getCoverages().setBusinessPersonalPropertyCoverage_ValuationMethod(BusinessPersonalPropertyCoverageValuationMethod.ReplacementCost);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case Reviewprotectivemeasuresforcontrolledsubstancesfromtheft:
                        //When class code 0562(2) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("0562(2)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case SpoilageCoverageCP0440Limitexceeds$25000PleasecontactUnderwritingforapproval:
                        //When endorsement or "Spoilage Coverage CP 04 40" is selected and the Limit is over $25,000 but is $50,000 or less display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.SpoilageCoverage_CP_04_40);
                        theBuilding.getAdditionalCoverages().setSpoilageCoverage_CP_04_40_Limit(26000);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case SpoilageCoverageCP0440isinexcessof$50000whichrequiresamaintenanceagreementbeforecoveragecanbebound:
                        //When endorsement or "Spoilage Coverage CP 04 40" is selected and the Limit is over $50,000 display the message.
                        theBuilding = new CPPCommercialProperty_Building(AdditionalCoverages.SpoilageCoverage_CP_04_40);
                        theBuilding.getAdditionalCoverages().setSpoilageCoverage_CP_04_40_Limit(56000);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case SprinklerleakageExclusionappliestoInformclientoflimitation:
//						When endorsement "Sprinkler Leakage Exclusion CP 10 56" is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building(CommercialProperty_ExclusionsAndConditions.SprinklerLeakageExclusion_CP_10_56);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case TheFoodContamination_BusinessInterruptionAndExtraExpense_CP1505limitforAnnualAggregateAdditionalAdvertisingExpenseLimitexceeds$5000ContactUnderwritingwithinformationtojustifythislimitofcoverage:
                        //When endorsement "Food Contamination (Business Interruption And Extra Expense) CP 15 05" is selected and the limit for "Annual Aggregate Additional Advertising Expense Limit" is over 5,000 display the message.
                        break;
                    case TheFoodContamination_BusinessInterruptionAndExtraExpense_CP1505limitforAnnualAggregateAdditionalAdvertisingExpenseLimitisover$5000ThelimithasbeenincreasedfromapriorapprovaloftoPleasecontactUnderwritingregardinganexception:
//						"When endorsement ""Food Contamination (Business Interruption And Extra Expense) CP 15 05"" term ""Annual Aggregate Additional Advertising Expense Limit"" meets the following conditions:
//						Current policy period limit exceeds $5,000 and
//						Prior policy period limit exceeds $5,000 and
//						The current policy period limit is higher than the prior policy priod limit by 10%
                        break;
                    case TheManualPublicProtectionClassCodehasbeenchangedfromtoUnderwriterpleaseverifytheProtectionClassiscorrect:
                        //When under the property Details tab and in the "Manual Public Protection Class" does not match the "Manual Public Protection Class" of the prior policy period, then display the message.
                        break;
                    case Theapplicant_insuredisafraternalorganizationorlaborunionPleaseexplainthatthepolicydoesnotprovidecoverageforlossresultingfromunauthorizedadvancestoamemberfordelinquentduesorassessmentsThereisarequirementforanannualauditofthebooksandaccountsincludingallsecuritiesandbankbalancespertainingtoeachemployee:
                        //Is the applicant/insured a fraternal organization or labor union? [CP3000BQ40] is answered with a Yes, then display the message.
                        break;
                    case Thecombinedlimitatthislocationexceeds$2000000andrequirespriorhomeofficeapprovalSubmitphotslossrunsandcostestimatorforconsideration:
                        //This is for the combined limits at a location under Commercial Property. When the combined Limits for "Building Coverage", "Business Personal Property Coverage", "Property In The Open", "Personal Property Of Others", is greater than $2,000,000 display the message.
                        break;
                    case Thecombinedlimitatthislocationexceeds$750000andrequirespriorhomeofficeapproval:
                        //This is for the combined limits at one address under the same location. When the combined Limits for "Building Coverage", "Business Personal Property Coverage", "Property In The Open", "Personal Property Of Others", is greater than $750,000 but less than or equal to $2,000,000 display the message. Remember that this is the combined limit at any one addresses, you can have multiple building with the same address.
                        break;
                    case ThecombinedlimitforBuildingCoverageBusinessPersonalPropertyCoveragePropertyInTheOpenPersonalPropertyOfOthersandBusinessIncomeCoverageFormexceeds$10000000AAICapprovalisrequired:
                        //When the combined Limits for "Building Coverage", "Business Personal Property Coverage", "Business Income Coverage Form", "Property In The Open", "Personal Property Of Others", is $10,000,000 or more display the message. Remember that this is the combined limit at any one address, you can have multiple building with the same address.
                        break;
                    case TheftOfBuildingMaterialsAndSupplies_OtherThanBuildersRisk_CP1044limitisover$25000ThelimithasbeenincreasedfromapriorapprovaloftoPleasecontactUnderwritingregardinganexceptionProvideUnderwriterwithadescriptionoftheprojecttheftsafeguardsanticipatedcompletiondateetc:
                        //"When endorsement ""Theft Of Building Materials And Supplies (Other Than Builders Risk) CP 10 44"" term ""Limit"" meets the following conditions:
//						Current policy period limit exceeds $50,000 and
//						Prior policy period limit exceeds $50,000 and
//						The current policy period limit is higher than the prior policy priod limit by 10%
                        break;
                    case TheftOfBuildingMaterialsAndSupplies_OtherThanBuildersRisk_CP1044limitsisinexcessof$25000thiswillrequireapprovalProvideUnderwriterwithadescriptionoftheprojecttheftsafeguardsanticipatedcompletiondateetc:
                        //When endorsement "Theft Of Building Materials And Supplies (Other Than Builders Risk) CP 10 44" is selected and the limit is over $25,000, display the message.
                        break;
                    case Thehood_ductworkislessthan18inchesfromthecombustiblematerialspleaserefertoUnderwritertodetermineeligibility:
                        //When the question "In inches how far is the hood/duct work from the combustible materials?" answered with a number that is less than 18, display the message.
                        break;
                    case Thereisnocoverageforgrainhaystraworothercropsintheopen:
                        //When class code 0570(14) is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building("0570(14)");
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ThisbuildingisaSpecificRatedbuildingpleasecontactUnderwritingsothattheymayenterarate:
                        //When under the property Details tab "Rate Type" is selected with "Specific" or "Tentative", then display the message.
                        theBuilding = new CPPCommercialProperty_Building();
                        theBuilding.setRateType(RateType.Specific);
                        theBuilding.setOverrideRateType(true);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case ThispropertyisgenerallynoteligibleforcoveragewiththisrooftypePleaserefertoUnderwritingforconsideration:
                        //When "Roofing type" is selected with the option of "Rolled Roofing", then display the message.
                        theBuilding = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
                        theBuilding.setRoofingType(RoofingType.RolledRoofing);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case UnderwriterAdditionalPropertyNotCoveredCP1420hasbeenaddedtothepropertyPleasecompletetheendorsement:
                        //When "Additional Property Not Covered CP 14 20" is selected display the message.
                        theBuilding = new CPPCommercialProperty_Building(CommercialProperty_ExclusionsAndConditions.AdditionalPropertyNotCovered_CP_14_20);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case UnderwriterpleasereviewtheWiringtypeasotherhasbeenselected:
                        //When "Other" is selected under the "Wiring type" then display the message.
                        theBuilding = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
                        theBuilding.setWiringType(WiringType.Other);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case UtilityServicesDirectDamageCP0417hasUtilityasnotpublicPleaseprovideUnderwritingwithownershipsubscriptionagreementsandreliabilityoftheservice:
                        //When "Utility Services � Direct Damage CP 04 17" is selected and the term "Utility is" has term option "Not Public" selected then display the message.
                        break;
                    case VerifythecorrectGLclassappliestothetypeofconstruction:
                        //When form "Builders' Risk Coverage Form CP 00 20" is selected, display the message.
                        theBuilding = new CPPCommercialProperty_Building(PropertyCoverages.BuildersRiskCoverageForm_CP_00_20);
                        theBuilding.setUwIssue(issue);
                        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
                        break;
                    case YouhaveindicatedthatasprinklersystemispresentSpecificrateswillapplytorisksotherthanapartmentordwellingUnderwritingwillvalidatesprinklersystemwithISRBandreclassifyaccordingly:
                        //When Sprinklered is checked Yes located under the Details tab, then display the message.
                        break;
                }//end switch
            }//end null check
        }//end for

        if (myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().isEmpty()) {
            myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(new CPPCommercialProperty_Building());
        }

    }//end updatePolicy()


    private List<CPUWIssues> convertList(List<persistence.globaldatarepo.entities.CPUWIssues> list) {
        List<CPUWIssues> returnList = new ArrayList<CPUWIssues>();
        for (persistence.globaldatarepo.entities.CPUWIssues listItem : list) {
            returnList.add(CPUWIssues.fromString(listItem.getRuleMessage()));
        }
        return returnList;
    }


    private void testFailed() {
        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + failureString);
        }
    }


}//END CLASS



















