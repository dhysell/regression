package regression.r3.noclock.policycenter.commercialproperty;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.CPUWQuestions;
import repository.gw.enums.CommercialProperty.PropertyCoverages;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialProperty;
import repository.gw.generate.custom.CPPCommercialPropertyLine;
import repository.gw.generate.custom.CPPCommercialPropertyLine_Coverages;
import repository.gw.generate.custom.CPPCommercialPropertyLine_ExclusionsConditions;
import repository.gw.generate.custom.CPPCommercialPropertyProperty;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.gw.generate.custom.CPPCommercialProperty_Building_Coverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.helpers.CPUWQuestionsHelper;

public class CPUnderWriterQuestions extends BaseTest {


    public GeneratePolicy myPolicyObj = null;
    public boolean testFailed = false;
    public String failureString = "";

    private WebDriver driver;


    @SuppressWarnings("serial")
    @Test()
    public void generateQQPolicy() throws Exception {

        List<persistence.globaldatarepo.entities.CPUWQuestions> uwQuestionsList = CPUWQuestionsHelper.getAllQuestionsNonBlockUser();
        updatePolicyObject(convertList(uwQuestionsList));

        AddressInfo pniAddress = new AddressInfo(true);
        ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>() {{
            this.add(new PolicyLocation(pniAddress, true));
        }};

        //COMMERCIAL PROPERTY LINE
        CPPCommercialPropertyLine commercialPropertyLine = new CPPCommercialPropertyLine() {{
            this.setPropertyLineCoverages(new CPPCommercialPropertyLine_Coverages() {{

                //SET COMMERCIAL PROPERTY LINE COVERAGES HERE
            }});
            this.setPropertyLineExclusionsConditions(new CPPCommercialPropertyLine_ExclusionsConditions() {{
                //SET COMMERCIAL PROPERTY LINE EXCLUSIONS CONDITIONS HERE
            }});
        }};

        //LIST OF COMMERCIAL PROPERTY
        List<CPPCommercialPropertyProperty> commercialPropertyList = new ArrayList<CPPCommercialPropertyProperty>() {{
            this.add(new CPPCommercialPropertyProperty() {{
                this.setAddress(pniAddress);
                this.setCPPCommercialProperty_Building_List(new ArrayList<CPPCommercialProperty_Building>() {{
                    this.add(new CPPCommercialProperty_Building() {{
                        this.setCoverages(new CPPCommercialProperty_Building_Coverages(PropertyCoverages.BuildingCoverage) {{
                        }});
                    }});// end building 0
                }});
            }});
        }};

        CPPCommercialProperty commercialProperty = new CPPCommercialProperty() {{
            this.setCommercialPropertyLine(commercialPropertyLine);
            this.setCommercialPropertyList(commercialPropertyList);
        }};

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialProperty(commercialProperty)
                .withLineSelection(LineSelection.CommercialPropertyLineCPP)
                .withPolicyLocations(locationList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Property UWIssues")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(pniAddress)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .isDraft()
                .build(GeneratePolicyType.QuickQuote);
    }//end generateQQPolicy()


    @Test(dependsOnMethods = {"generateQQPolicy"})
    public void verifyUnderWriterQuestionFailureMessage() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        List<persistence.globaldatarepo.entities.CPUWQuestions> uwQuestionsList = CPUWQuestionsHelper.getAllQuestionsNonBlockUser();
        updatePolicyObject(convertList(uwQuestionsList));


    }//end verifyUnderWriterQuestionFailureMessage


//	@Test(dependsOnMethods={"generateQQPolicy"})
//	public void verifyInformationalUWIssues() throws Exception {
//		
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//		
//		loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
//		
//		editPolicyTransaction2();
//		
//		List<persistence.globaldatarepo.entities.CPUWIssues> informationalUWIssueList = CPUWIssuesHelper.getInformationalUWIssues();
//		
//		
//		updatePolicyObject(convertList(informationalUWIssueList));
//		
//		
//		
//		
//		
//	}//end verifyInformationalUWIssues

    private void updatePolicyObject(List<CPUWQuestions> uwQuestionList) {

        for (CPUWQuestions question : uwQuestionList) {

            if (question != null) {
                switch (question) {
                    case ClassCode0921_1_Isshippingpackingandcratingmaterialsproperlystoredandfreeofdebris:
                        break;
                    case ClassCode0921_1_AreallchemicalsstoredinNFPAapprovedstoragereceptacles:
                        break;
                    case ClassCode0921_1_Arechemicalsoakedragsdisposedofproperly:
                        break;
                    case ClassCode0921_1_Isstockcoverageforfineartsdesired:
                        break;
                    case ClassCode2400_1_Doesapplicantinsuredhaveamaintenanceagreementonproductionmachinery:
                        break;
                    case ClassCode0841_Arelanesfinishedwithlacquerbasedfinishes:
                        break;
                    case ClassCode0841_Doesapplicantinsuredhaveafullscheduleofleaguebowling:
                        break;
                    case ClassCode0841_Howmanydaysaweekarebowlingleaguesactive:
                        break;
                    case ClassCode0841_Isthepremisescomprisedofonemainfiredivision:
                        break;
                    case ClassCode0841_Laneresurfacingisperformedby:
                        break;
                    case ClassCode0841_Istherearestaurant:
                        break;
                    case ClassCode0841_Arethereretailsalesofequipmentclothingetc:
                        break;
                    case ClassCode1150_Apartment:
                        break;
                    case ClassCode1150_ColdStorage:
                        break;
                    case ClassCode1150_Dwelling:
                        break;
                    case ClassCode1150_Electricalpower_boilersturbinegeneratorsfuelcellsorsolar:
                        break;
                    case ClassCode1150_Hospitalormedicalfacilities:
                        break;
                    case ClassCode1150_Printer:
                        break;
                    case ClassCode1150_SewageTreatment:
                        break;
                    case ClassCode1150_WaterTreatment:
                        break;
                    case ClassCode0567_15_Isstockcoveragetobeincluded:
                        break;
                    case ClassCode0931_3_Doespropertyhavebarrierstopreventvehiclesfromhittingthebuilding:
                        break;
                    case ClassCode0562_2_Hasapplicantinsuredhadpriorburglaryortheftlosses:
                        break;
                    case ClassCode0570_7_Doesapplicantinsuredhaveanhydrousammoniaonpremises:
                        break;
                    case ClassCode0570_7_Doesapplicantinsuredhavebulkfertilizer:
                        break;
                    case ClassCode0580_2_Areflammableitemsiechemicalsfuelsandsolventsstoredawayfrompotentialignitionsources:
                        break;
                    case ClassCode0580_2_Aremulchesandcompostpileslocatedawayfromthebuilding:
                        break;
                    case ClassCode0570_13_Isstockofthehatcherycoveragedesired:
                        break;
                    case ClassCode0570_14_Hasapplicantinsuredhadahayorstrawfire:
                        break;
                    case ClassCode0570_14_Ishayorstrawstoredinthebuilding:
                        break;
                    case ClassCode0196_Arethereadditionalstructuresorbuildingsatthedwellinglocation:
                        break;
                    case ClassCode0742_Doestheapplicanthavekitchenunitsavailabletoguests:
                        break;
                    case ClassCode0743_Doestheapplicanthavekitchenunitsavailabletoguests:
                        break;
                    case ClassCode0744_Doestheapplicanthavekitchenunitsavailabletoguests:
                        break;
                    case ClassCode0520_9_Doesapplicantinsuredpressclothes:
                        break;
                    case ClassCode0520_9_Doesapplicantinsuredprovidetheactualdrycleaningservice:
                        break;
                    case ClassCode0911_1_Doesapplicantinsuredprovidetheactualdrycleaningservice:
                        break;
                    case ClassCode0912_1_Aresolventfilterschangedregularly:
                        break;
                    case ClassCode0912_1_Islintandfilterresiduedisposedincoveredoutsidecontainers:
                        break;
                    case ClassCode0912_1_Arechemicalsproperlystored:
                        break;
                    case ClassCode0912_1_Aresteammachinesleftunattendedwhentheyareon:
                        break;
                    case ClassCode0913_2_Aresolventfilterschangedregularly:
                        break;
                    case ClassCode0913_2_Islintandfilterresiduedisposedincoveredoutsidecontainers:
                        break;
                    case ClassCode0913_2_Arechemicalsproperlystored:
                        break;
                    case ClassCode0913_2_Aresteammachinesleftunattendedwhentheyareon:
                        break;
                    case ClassCode0565_2_Doesapplicantinsuredhaveindividualjewelryitemsover$100:
                        break;
                    case ClassCode6900_12_Doesapplicantinsuredhaveindividualjewelryorpreciousstonesover$100:
                        break;
                    case ClassCode0563_14_Isthefuelstoredanddispensedawayfromthebuilding:
                        break;
                    case ClassCode0563_14_Dopartscleaningequipmenthaveanoperationaltrapdoorwhichautomaticallyclosesintheeventofafire:
                        break;
                    case ClassCode0563_14_Doesapplicantinsuredhaveaspraypaintingbooth:
                        break;
                    case ClassCode0563_14_WhenchemicalsarenotinusearetheyproperlystoredinaNFPAapprovedfireresistantcabinet:
                        break;
                    case ClassCode0922_26_Isthefuelstoredanddispensedawayfromthebuilding:
                        break;
                    case ClassCode0922_26_Dopartscleaningequipmenthaveanoperationaltrapdoorwhichautomaticallyclosesintheeventofafire:
                        break;
                    case ClassCode0922_26_Doesapplicantinsuredhaveaspraypaintingbooth:
                        break;
                    case ClassCode0922_26_WhenchemicalsarenotinusearetheyproperlystoredinaNFPAapprovedfireresistantcabinet:
                        break;
                    case ClassCode0933_8_Isthefuelstoredanddispensedawayfromthebuilding:
                        break;
                    case ClassCode0933_8_Dopartscleaningequipmenthaveanoperationaltrapdoorwhichautomaticallyclosesintheeventofafire:
                        break;
                    case ClassCode0933_8_Doesapplicantinsuredhaveaspraypaintingbooth:
                        break;
                    case ClassCode0933_8_WhenchemicalsarenotinusearetheyproperlystoredinaNFPAapprovedfireresistantcabinet:
                        break;
                    case ClassCode6850_27_Isthefuelstoredanddispensedawayfromthebuilding:
                        break;
                    case ClassCode6850_27_Dopartscleaningequipmenthaveanoperationaltrapdoorwhichautomaticallyclosesintheeventofafire:
                        break;
                    case ClassCode6850_27_Doesapplicantinsuredhaveaspraypaintingbooth:
                        break;
                    case ClassCode6850_27_WhenchemicalsarenotinusearetheyproperlystoredinaNFPAapprovedfireresistantcabinet:
                        break;
                    case ClassCode0932_2_Isthefuelstoredanddispensedawayfromthebuilding:
                        break;
                    case ClassCode0932_2_Dopartscleaningequipmenthaveanoperationaltrapdoorwhichautomaticallyclosesintheeventofafire:
                        break;
                    case ClassCode0932_2_Doesapplicantinsuredhaveaspraypaintingbooth:
                        break;
                    case ClassCode0932_2_WhenchemicalsarenotinusearetheyproperlystoredinaNFPAapprovedfireresistantcabinet:
                        break;
                    case ClassCode0932_2_IfautobodyworkisperformeddoestheclienthaveaULapprovedpaintboothonpremises:
                        break;
                    case ClassCode0933_3_Isthefuelstoredanddispensedawayfromthebuilding:
                        break;
                    case ClassCode0933_3_Dopartscleaningequipmenthaveanoperationaltrapdoorwhichautomaticallyclosesintheeventofafire:
                        break;
                    case ClassCode0933_3_Doesapplicantinsuredhaveaspraypaintingbooth:
                        break;
                    case ClassCode0933_3_WhenchemicalsarenotinusearetheyproperlystoredinaNFPAapprovedfireresistantcabinet:
                        break;
                    case ClassCode0933_3_IfautobodyworkisperformeddoestheclienthaveaULapprovedpaintboothonpremises:
                        break;
                    case ClassCode0562_3_Hasapplicantinsuredhadpriorburglaryortheftlosses:
                        break;
                    case ClassCode0567_71_Doesthemuseumhavefinearts:
                        break;
                    case ClassCode0567_71_Doesapplicantinsuredhavepersonalpropertyofothersonloan:
                        break;
                    case ClassCode1051_3_Doesthemuseumhavefinearts:
                        break;
                    case ClassCode1051_3_Doesapplicantinsuredhavepersonalpropertyofothersonloan:
                        break;
                    case ClassCode0580_3_Other:
                        break;
                    case ClassCode0580_3_Areflammableitemsiechemicalsfuelsandsolventsstoredawayfrompotentialignitionsources:
                        break;
                    case ClassCode0580_3_Aremulchesandcompostpileslocatedawayfromthebuilding:
                        break;
                    case ClassCode0570_20_Aremulchesandcompostpileslocatedawayfromthebuilding:
                        break;
                    case ClassCode0570_20_Areflammableitemsiechemicalsfuelsandsolventsstoredawayfrompotentialignitionsources:
                        break;
                    case ClassCode0922_10_Isthelaundrysuserestrictedtotenantsonly:
                        break;
                    case ClassCode3959_7_Doesapplicantinsuredhaveadesignatedsmokingareaequippedself_closingfire_resistantreceptacles:
                        break;
                    case ClassCode3959_7_Isdustsuppressionequipmentventedtotheoutsideofthebuilding:
                        break;
                    case ClassCode3959_7_Haveemployeesbeentrainedinuseoffireextinguishers:
                        break;
                    case ClassCode3959_10_Doesapplicantinsuredhaveadesignatedsmokingareaequippedself_closingfire_resistantreceptacles:
                        break;
                    case ClassCode3959_10_Isdustsuppressionequipmentventedtotheoutsideofthebuilding:
                        break;
                    case ClassCode3959_10_Haveemployeesbeentrainedinuseoffireextinguishers:
                        break;
                    case ClassCode0844_23_Doestheapplicantinsuredhaveashootinggallery:
                        break;
                    case ClassCode0844_23_Doesapplicantinsuredhaveadesignatedsmokingareaequippedself_closingfire_resistantreceptacles:
                        break;
                    case ClassCode0844_23_Doestheapplicantinsuredprovidefoodservice:
                        break;
                    case ClassCode0922_36_Doesapplicantinsuredstorehayorstrawinthebuilding:
                        break;
                    case ClassCode0570_29_DoesApplicantInsuredstorehayorstrawinthebuilding:
                        break;
                    case ClassCode0520_11_Doesapplicantinsuredhaveadesignatedsmokingareaequippedself_closingfire_resistantreceptacles:
                        break;
                    case ClassCode3009_11_Doesapplicantinsuredhaveadesignatedsmokingareaequippedself_closingfire_resistantreceptacles:
                        break;
                    case ClassCode0570_27_Doesapplicantinsuredstorehayorstrawinthebuilding:
                        break;
                    case ClassCode0844_20_Doesapplicantinsuredhaveadesignatedsmokingareaequippedself_closingfire_resistantreceptacles:
                        break;
                    case ClassCode0921_23_Areautoclavesequippedwithtemperatureprotectiondevicesorautomaticshut_offcontrols:
                        break;
                    case ClassCode0844_19_Isthisacampgroundrecreationalfacility:
                        break;
                    case ClassCode0844_19_Isthisaclubrecreationalfacility:
                        break;
                    case ClassCode0844_19_Isthisagolfclubandsimilarexposurerecreationalfacility:
                        break;
                    case ClassCode0844_19_Isthisaskatingrinkrecreationalfacility:
                        break;
                    case ClassCode0844_19_Isthisarecreationalfacilitythatwasnotmentionedabove:
                        break;
                    case ClassCode0545_Other:
                        break;
                    case ClassCode1230_2_Doesapplicantinsuredhaveadesignatedsmokingareaequippedself_closingfire_resistantreceptacles:
                        break;
                    case ClassCode1211_3_Doesapplicantinsuredhaveadesignatedsmokingareaequippedself_closingfire_resistantreceptacles:
                        break;
                    case ClassCode1220_Doesapplicantinsuredhaveadesignatedsmokingareaequippedself_closingfire_resistantreceptacles:
                        break;
                    case ClassCode1213_Doesapplicantinsuredhaveadesignatedsmokingareaequippedself_closingfire_resistantreceptacles:
                        break;
                    case ClassCode1212_Doesapplicantinsuredhaveadesignatedsmokingareaequippedself_closingfire_resistantreceptacles:
                        break;
                    case ClassCode6850_48_Doesapplicantinsuredhaveadesignatedsmokingareaequippedwithself_closingfire_resistantreceptacles:
                        break;
                    case ClassCode6850_48_Areequipmentoperatorsprohibitedfromleavinganypieceofmetal_workingmachineryunattendedwhileitisinoperation:
                        break;
                    case ClassCode6850_48_Doestheinsuredweldonsite:
                        break;
                    case ClassCode2459_2_Doesapplicantinsuredallowsmokingonpremises:
                        break;
                    case ClassCode2459_2_Doesapplicantinsuredhaveadesignatedsmokingareaequippedself_closingfire_resistantreceptacles:
                        break;
                    case ClassCode2459_2_Doesapplicantinsuredhaveamaintenanceagreementfortheproductionmachinery:
                        break;
                    default:
                        break;

                }//end switch


            }//end null check


        }//end for


    }//end updatePolicy()


    private List<CPUWQuestions> convertList(List<persistence.globaldatarepo.entities.CPUWQuestions> list) {
        List<CPUWQuestions> returnList = new ArrayList<>();
        for (persistence.globaldatarepo.entities.CPUWQuestions listItem : list) {
            returnList.add(CPUWQuestions.fromString(listItem.getFailureMessage()));
        }
        return returnList;
    }


}//END CLASS




















