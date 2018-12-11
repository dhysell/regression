package regression.r2.noclock.policycenter.submission_fire_im.subgroup8;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DeliveryOptionType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderDeliveryOption;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : DE4357 PL - Standard Liability Delivery Option not transferring
 * @Description : Additional interest ->Delivery options when added in standard fire should display automatically in standard Liability Additional Interest
 * @DATE Jan 10, 2017
 */
public class TestStandardLiabilityDeliveryOption extends BaseTest {
    private GeneratePolicy myPolicyObject;
    private Underwriters uw;
    private GeneratePolicy squirePolicy;
    private WebDriver driver;

    @Test()
    public void testStandardLiabilityWithStdFire() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();
        loc1Bldg1.setpropertyType(PropertyTypePL.DwellingPremises);
        locOnePropertyList.add(loc1Bldg1);
        locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withStandardFire(myStandardFire)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withInsFirstLastName("Guy", "Stdfire")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);

        new GuidewireHelpers(driver).logout();

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

        myPolicyObject.standardLiability = myStandardLiability;
        myPolicyObject.lineSelection.add(LineSelection.StandardLiabilityPL);
        myPolicyObject.stdFireLiability = true;
        myPolicyObject.addLineOfBusiness(ProductLineType.StandardLiability, GeneratePolicyType.FullApp);

//		stdFireLiab_Liability_PolicyObj = new GeneratePolicy.Builder(driver)
//				.withProductType(ProductLineType.StandardLiability)
//				.withLineSelection(LineSelection.StandardLiabilityPL)
//				.withStandardLiability(myStandardLiability)
//				.withAgent(myPolicyObject.agentInfo)
//				.withStandardFirePolicyUsedForStandardLiability(myPolicyObject ,true)
//				.withPolOrgType(OrganizationType.Individual)
//				.withPaymentPlanType(PaymentPlanType.Annual)
//				.withDownPaymentType(PaymentType.Cash)
//				.build(GeneratePolicyType.FullApp);	

    }

    @Test(dependsOnMethods = {"testStandardLiabilityWithStdFire"}, enabled = false)
    public void validateStdLibDeliveryOption() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchJob(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propDet = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);

        //Adding lien Holder
        sideMenu.clickSideMenuSquirePropertyDetail();
        propDet.clickViewOrEditBuildingButton(1);
        AddressInfo bankAddress = new AddressInfo();

        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
        loc2Bldg1AddInterest.setAddress(bankAddress);
        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);

        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
        SearchAddressBookPC search = new SearchAddressBookPC(driver);
        search.searchAddressBookByCompanyName(myPolicyObject.basicSearch, loc2Bldg1AddInterest.getCompanyName(), null, null, State.Idaho, null, CreateNew.Do_Not_Create_New);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
        //Delivery options button will not available for new address, added code to select existing address
        additionalInterests.checkBuildingsPropertyAdditionalInterestsFirstMortgage(true);

        DeliveryOptionType optionType = DeliveryOptionType.Attention;

        GenericWorkorderDeliveryOption deliveryOptions = new GenericWorkorderDeliveryOption(driver);
        deliveryOptions.addDeliveryOption(optionType);

        deliveryOptions.clickAddToSelectedDeliveryOptions();
        String companyName = additionalInterests.getAdditionalInterestsName();
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();


        propDet.clickOk();

        sideMenu.clickSideMenuPayerAssignment();
        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
        payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(1, 1, companyName, true, false);
        payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        quote.clickQuote();
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
        riskAnalysis.approveAll_IncludingSpecial();
        sideMenu.clickSideMenuPayment();
        GenericWorkorderPayment payments = new GenericWorkorderPayment(driver);
        payments.makeDownPayment(PaymentPlanType.Annual, PaymentType.Cash, 0.00);

        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchSubmission(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        polInfo.clickEditPolicyTransaction();

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickAdditionalInterest();

        additionalInterests.addExistingFromStandardFire(companyName);
        additionalInterests.clickBuildingsPropertyAdditionalInterestsLink(companyName);
        String stdFireDeliverytype = optionType.toString();
        String stdLibDeliveryType = additionalInterests.getDeliveryOption().replaceAll("'", " ").trim();

        if (!stdFireDeliverytype.equals(stdLibDeliveryType))
            Assert.fail("Standard Fire Delivery option should be displayed in Standard Liability Additional Interest");
    }

    /**
     * @Author skandibanda
     * @Requirement : DE5330: Delivery Options selection order
     * @DATE Apr 27, 2017
     */

    @Test
    public void generatePropertyFullApp() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));
        SquireLiability liabilitySection = new SquireLiability();

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        squirePolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "Delivery Options")
                .withPolOrgType(OrganizationType.Individual)
                .build(GeneratePolicyType.FullApp);

    }

    @Test(dependsOnMethods = {"generatePropertyFullApp"})
    public void validateStdLibDelivery() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchJob(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePolicy.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propDet = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);

        //Adding lien Holder
        sideMenu.clickSideMenuSquirePropertyDetail();
        propDet.clickViewOrEditBuildingButton(1);
        AddressInfo bankAddress = new AddressInfo();

        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
        loc2Bldg1AddInterest.setAddress(bankAddress);
        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);

        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
        SearchAddressBookPC search = new SearchAddressBookPC(driver);
        search.searchAddressBookByCompanyName(myPolicyObject.basicSearch, loc2Bldg1AddInterest.getCompanyName(), null, null, null, null, CreateNew.Do_Not_Create_New);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
        additionalInterests.checkBuildingsPropertyAdditionalInterestsFirstMortgage(true);

        //Add Delivery Options
        DeliveryOptionType[] options = {DeliveryOptionType.Attention, DeliveryOptionType.DBA, DeliveryOptionType.FBO, DeliveryOptionType.FLCA,
                DeliveryOptionType.MAC, DeliveryOptionType.Other, DeliveryOptionType.InCareOf, DeliveryOptionType.ISAOAorATIMA,
                DeliveryOptionType.PCA, DeliveryOptionType.AsTheirInterestsMayAppear, DeliveryOptionType.ItsSuccessorsAndOrAssigns
        };

        GenericWorkorderDeliveryOption deliveryOptions = new GenericWorkorderDeliveryOption(driver);
        for (DeliveryOptionType optionType : options) {
            deliveryOptions.addDeliveryOption(optionType);

        }
        //Select Delivery Options Buttons
        deliveryOptions.clickAddToSelectedDeliveryOptionsButton();
        String ExpectedDeliveryOptions = "' " + options[0].getTypeValue() + " ' , ' " + options[1].getTypeValue() + " ' , ' " + options[2].getTypeValue() + " ' , ' " + options[3].getTypeValue() + " ' , ' " + options[4].getTypeValue() +
                " ' , ' " + options[5].getTypeValue() + " ' , ' " + options[6].getTypeValue() + " ' , ' " + options[7].getTypeValue() + " ' , ' " + options[8].getTypeValue() + " ' , ' " + options[9].getTypeValue() + " ' , ' " + options[10].getTypeValue() + " '";

        String ActualDeliveryOptions = deliveryOptions.getSelectedDeliveryOptions();

        if (!ExpectedDeliveryOptions.equals(ActualDeliveryOptions))
            Assert.fail(ActualDeliveryOptions + " order not same as " + ExpectedDeliveryOptions);
    }
}
