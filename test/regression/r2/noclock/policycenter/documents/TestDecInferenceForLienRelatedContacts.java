package regression.r2.noclock.policycenter.documents;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.AddressType;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.RelatedContacts;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquireLiablityCoverageLivestockItem;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInsured;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;

/**
 * @Author nvadlamudi
 * @Requirement : US12421: If Additional interests are multiple people need to print from Related Contacts
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Common-Admin/PC8%20-%20Common%20-%20ClientContacts%20-%20Policy%20File%20-%20Contacts.xlsx">PC8- Common- ClientContacts- Policy File</a>
 * @Description : Not able to validate inside the document but adding related contacts and checking in the documents for additional interest name
 * @DATE Nov 21, 2017
 */
public class TestDecInferenceForLienRelatedContacts extends BaseTest {
    private GeneratePolicy myPolicyObjPL;
    private String PropoertyDetailsAdditionalInterestLastName = "Prop" + StringsUtils.generateRandomNumberDigits(10);
    private String relatedAdditionalInterestLastName = "Rela" + StringsUtils.generateRandomNumberDigits(10);

    private WebDriver driver;

    @Test()
    public void testCreateSquirePolicyWithAI() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);


        PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
        propertyCoverages.getCoverageA().setIncreasedReplacementCost(false);
        ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
        loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        loc1LNBldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        loc1LNBldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
        loc1LNBldg1AddInterest.setLoanContractNumber("LN12345");
        loc1LNBldg1AddInterest.setPersonFirstName("New" + StringsUtils.generateRandomNumberDigits(10));
        loc1LNBldg1AddInterest.setPersonLastName(PropoertyDetailsAdditionalInterestLastName);
        loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);

        // Property details
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty prop1 = new PLPolicyLocationProperty();
        prop1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
        prop1.setpropertyType(PropertyTypePL.ResidencePremises);
        prop1.setConstructionType(ConstructionTypePL.Frame);
        locOnePropertyList.add(prop1);
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locToAdd.setPlNumResidence(5);
        locationsList.add(locToAdd);

        // Section II coverages


        SquireLiablityCoverageLivestockItem livestockSectionIICowCoverage = new SquireLiablityCoverageLivestockItem();
        livestockSectionIICowCoverage.setQuantity(100);
        livestockSectionIICowCoverage.setType(LivestockScheduledItemType.Cow);

        ArrayList<SquireLiablityCoverageLivestockItem> coveredLivestockItems = new ArrayList<SquireLiablityCoverageLivestockItem>();
        coveredLivestockItems.add(livestockSectionIICowCoverage);
        SectionIICoverages livestock = new SectionIICoverages(SectionIICoveragesEnum.Livestock, coveredLivestockItems);


        SquireLiability liability = new SquireLiability();
        liability.getSectionIICoverageList().add(livestock);


        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liability;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Related", "Document")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .build(GeneratePolicyType.FullApp);

    }

    @Test(dependsOnMethods = {"testCreateSquirePolicyWithAI"})
    private void testIssuePolicyRelatedContacts() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(this.myPolicyObjPL.agentInfo.getAgentUserName(),
                this.myPolicyObjPL.agentInfo.getAgentPassword(), this.myPolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickEditPolicyTransaction();

        // property Details Additional Interest
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        propertyDetail.clickViewOrEditBuildingButton(1);
        propertyDetail.clickAdditionalInterestByName(this.PropoertyDetailsAdditionalInterestLastName);
        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.clickRelatedContactsTab();
        AddressInfo address = new AddressInfo(true);
        additionalInterests.addNewPersonRelatedContact(myPolicyObjPL.basicSearch, RelatedContacts.Spouse, "Cont" + StringsUtils.generateRandomNumberDigits(8), relatedAdditionalInterestLastName, address);
        additionalInterests.setContactEditAddressLine1(address.getLine1());
        additionalInterests.setContactEditAddressCity(address.getCity());
        additionalInterests.sendArbitraryKeys(Keys.TAB);
        additionalInterests.setContactEditAddressState(address.getState());
        additionalInterests.setContactEditAddressZipCode(address.getZip());
        additionalInterests.sendArbitraryKeys(Keys.TAB);
        additionalInterests.sendArbitraryKeys(Keys.TAB);

        additionalInterests.setContactEditAddressAddressType(AddressType.Home);
        additionalInterests.sendArbitraryKeys(Keys.TAB);
        GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
        additionalInsured.setDOB(DateUtils.dateFormatAsString("MM/dd/YYYY", DateUtils.convertStringtoDate("3/5/1981", "MM/DD/YYYY")));
        additionalInterests.clickRelatedContactsUpdate();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (guidewireHelpers.overrideAddressStandardization()) {
            additionalInterests.clickRelatedContactsUpdate();
        }
        if (guidewireHelpers.duplicateContacts()) {
            additionalInterests.clickRelatedContactsUpdate();
        }
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
        propertyDetail.clickOk();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();

        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk.performRiskAnalysisAndQuote(this.myPolicyObjPL);

        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        if (quotePage.hasBlockBind()) {
            risk.handleBlockSubmit(this.myPolicyObjPL);
        }
        guidewireHelpers.logout();

        guidewireHelpers.setPolicyType(myPolicyObjPL, GeneratePolicyType.FullApp);
        this.myPolicyObjPL.convertTo(driver, GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssuePolicyRelatedContacts"})
    private void testCheckPolicyDocument() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObjPL.underwriterInfo.getUnderwriterUserName(), myPolicyObjPL.underwriterInfo.getUnderwriterPassword(), myPolicyObjPL.squire.getPolicyNumber());
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
        boolean propertAdditonalInterest = false;
        for (String name : docs.getDocumentNameAddress()) {
            if (name.contains(this.PropoertyDetailsAdditionalInterestLastName)) {
                propertAdditonalInterest = true;
            }
        }

        if (!propertAdditonalInterest) {
            Assert.fail("Expected Additional Interest name is not displayed.");
        }
    }
}
