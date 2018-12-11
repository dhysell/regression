package regression.r2.noclock.policycenter.submission_misc.subgroup3;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyScheduledItemType;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderLineReviewPL;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_LineReview;

/**
 * @Author skandibanda
 * @Requirement : US8842: PL - Section level Additional insured
 * @Description : Checking for question Is this a Trust? under New Policy Member
 * screen and also checking for additional insured count on Section
 * II Coverages when additional insured members added with
 * selection of additional insured YES, Trust is YES/NO and one of
 * the options selected yes
 * @DATE Aug 23, 2016
 */
public class TestSectionLevelAdditionalInsured5 extends BaseTest {
    private GeneratePolicy squire_PolicyObj;



//	@Test
//	public void testSquirePLPropertyPolicy() throws Exception {
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
//		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
//		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
//		locationsList.add(new PolicyLocation(locOnePropertyList));
//
//		SquireLiability myLiab = new SquireLiability();
//		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
//
//
//		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
//		PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
//				"Test" + StringsUtils.generateRandomNumberDigits(8), "comp", AdditionalNamedInsuredType.Spouse,
//				new AddressInfo(true));
//		ani.setHasMembershipDues(true);
//		ani.setNewContact(CreateNew.Create_New_Always);
//		listOfANIs.add(ani);
//
//		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
//		myPropertyAndLiability.locationList = locationsList;
//		myPropertyAndLiability.liabilitySection = myLiab;
//
//		Squire mySquire = new Squire(SquireEligibility.Country);
//		mySquire.propertyAndLiability = myPropertyAndLiability;
//
//		myPolicyObj = new GeneratePolicy.Builder(driver)
//				.withSquire(mySquire)
//				.withProductType(ProductLineType.Squire)
//				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
//				.withInsCompanyName("SQCountry")
//				.withANIList(listOfANIs)
//				.build(GeneratePolicyType.QuickQuote);
//	}
//
//	@Test(dependsOnMethods = { "testSquirePLPropertyPolicy" })
//	public void verifyCountWhenIsThatTrustQuestionIsYes() throws Exception {
//
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//		loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(),
//				myPolicyObj.accountNumber);
//		this.editPolicyTransaction();
//		SideMenuPC sideMenu = new SideMenuPC(driver);
//		sideMenu.clickSideMenuHouseholdMembers();
//		GenericWorkorderPolicyMembers houseHoldMember = new GenericWorkorderPolicyMembers(driver);
//		houseHoldMember.clickSearch();
//		SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
//		addressBook.searchAddressBookByCompanyName("BBIT-" + randFirst, "752 2ND AVE", "TWIN FALLS", State.Idaho,
//				"83301", CreateNew.Create_New_Always);
//		//		GenericWorkorderPolicyMembers newHousehodMember = new GenericWorkorderPolicyMembers(driver);
//		newHousehodMember.selectRelationshipToInsured(RelationshipToInsured.Insured);
//		//        newHousehodMember.setSSN("2" + StringsUtils.generateRandomNumberDigits(8).replace("00", "34"));
//		sendArbitraryKeys(Keys.TAB);
//		//		newHousehodMember.setNewPolicyMemberAlternateID("BBIT-" + randFirst);
//		sendArbitraryKeys(Keys.TAB);
//		//		newHousehodMember.setAdditionalInsured(true);
//		AddressInfo address = new AddressInfo("752 2ND AVE", "", "TWIN FALLS", State.Idaho, "83301", CountyIdaho.Ada,
//				"United States", AddressType.Home);
//		newHousehodMember.selectNotNewAddressListingIfNotExist(address);
//		sendArbitraryKeys(Keys.TAB);		
//		//		// Verify Is This A Trust Question Exists
//		if (!newHousehodMember.checkIsThisATrustQuestionExist())
//			throw new Exception("Is This A Trust Question not exists");
//
//		newHousehodMember.setIsThisATrust(true);
//		newHousehodMember.setSectionIIAdditionalInsured(true);
//		newHousehodMember.clickOK();
//
//		sideMenu.clickSideMenuPropertyLocations();
//		sideMenu.clickSideMenuSquirePropertyCoverages();
//
//		GenericWorkorderSquirePropertyAndLiabilityCoverages coverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
//		coverage.clickSectionIICoveragesTab();
//		GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages sectionII = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
//
//		// Verify Additional Insured Count Not added to sectionII coverages when
//		// Is This A Trust Question selected YES
//		if (sectionII.checkAdditionalNamedInsuredExists())
//			throw new Exception("Additional Named Insured count Exists in SectionII coverages");
//
//	}
//
//	@Test(dependsOnMethods = { "verifyCountWhenIsThatTrustQuestionIsYes" })
//	public void verifyCountWhenIsThatTrustQuestionIsNo() throws Exception {
//
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//		loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(),
//				myPolicyObj.accountNumber);
//		SideMenuPC sideMenu = new SideMenuPC(driver);
//		sideMenu.clickSideMenuHouseholdMembers();
//		GenericWorkorderPolicyMembers houseHoldMember = new GenericWorkorderPolicyMembers(driver);
//		houseHoldMember.clickSearch();
//
//		SearchAddressBookPC addressBook = new SearchAddressBookPC(driver);
//		addressBook.searchAddressBookByCompanyName("SUNRISE-" + randFirst, "752 2ND AVE", "TWIN FALLS", State.Idaho,
//				"83301", CreateNew.Create_New_Always);
//		//		GenericWorkorderPolicyMembers newHousehodMember = new GenericWorkorderPolicyMembers(driver);
//		newHousehodMember.selectRelationshipToInsured(RelationshipToInsured.Insured);
//		newHousehodMember.setSSN(StringsUtils.generateRandomNumberDigits(9));
//		sendArbitraryKeys(Keys.TAB);
//		//		newHousehodMember.setNewPolicyMemberAlternateID("SUNRISE-" + randFirst);
//		sendArbitraryKeys(Keys.TAB);
//		//		newHousehodMember.setAdditionalInsured(true);
//		AddressInfo address = new AddressInfo("752 2ND AVE", "", "TWIN FALLS", State.Idaho, "83301", CountyIdaho.Ada,
//				"United States", AddressType.Home);
//		newHousehodMember.selectNotNewAddressListingIfNotExist(address);
//
//		// Verify Is This A Trust Question Exists
//		if (!newHousehodMember.checkIsThisATrustQuestionExist())
//			throw new Exception("Is This A Trust Question not exists");
//
//		newHousehodMember.setSectionIIAdditionalInsured(true);
//		newHousehodMember.clickOK();
//
//		sideMenu.clickSideMenuPropertyLocations();
//		sideMenu.clickSideMenuSquirePropertyCoverages();
//
//		GenericWorkorderSquirePropertyAndLiabilityCoverages coverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
//		coverage.clickSectionIICoveragesTab();
//
//		// Verify Additional Insured Count added to sectionII coverages when Is
//		// This A Trust Question selected NO
//		GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages sectionII = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
//		int additionaInsuredQuantity = sectionII.getAdditionalNamedInsuredQuantity();
//		int additionalInsuredcount = 1;
//
//		boolean testFailed = false;
//		String errorMessage = "";
//
//		if (additionalInsuredcount != additionaInsuredQuantity) {
//			testFailed = true;
//		}
//		if (testFailed) {
//			errorMessage = errorMessage + "Expected Additional Insured Count not displayed in the SectionII coverages ."
//					+ "\n";
//			Assert.fail(errorMessage);
//		}
//	}
//
//
//
//	/**
//	 * @Author sbroderick
//	 * @Requirement : US8733: Pl - Add/Move Additional Insured to each section
//	 * @Description : need to assign the contacts that are section level
//	 *              additional insureds as "Additional Insured" role.
//	 * @DATE Sept 7, 2016
//	 */
//	@Test()
//	private void createStandardLiabPolicy() throws GuidewirePolicyCenterException, Exception {
//
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//		ArrayList<Contact> policyMembers = new ArrayList<Contact>();
//		Contact propertyAI = new Contact();
//		ArrayList<LineSelection> aiLines = new ArrayList<LineSelection>();
//		aiLines.add(LineSelection.StandardLiabilityPL);
//		propertyAI.setAdditionalInsured(aiLines);
//		policyMembers.add(propertyAI);
//
//		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
//		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
//		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
//		PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
//		propLoc.setPlNumAcres(10);
//		propLoc.setPlNumResidence(5);
//		locationsList.add(propLoc);
//
//		ArrayList<LineSelection> productLines = new ArrayList<LineSelection>();
//		productLines.add(LineSelection.StandardFirePL);
//
//		SquireLiability myLiab = new SquireLiability();
//		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);
//
//		StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
//		myStandardLiability.liabilitySection = myLiab;
//		myStandardLiability.setLocationList(locationsList);
//		myStandardLiability.setPolicyMembers(policyMembers);
//
//		sLiabPol = new GeneratePolicy.Builder(driver)
//				.withProductType(ProductLineType.StandardLiability)
//				.withLineSelection(LineSelection.StandardLiabilityPL)
//				.withStandardLiability(myStandardLiability)
//				.withInsFirstLastName("stdLiability", "Dues")
//				.withPolOrgType(OrganizationType.Individual)
//				.build(GeneratePolicyType.FullApp);
//
//		loginAndSearchJob(sLiabPol.agentInfo.getAgentUserName(), sLiabPol.agentInfo.getAgentPassword(),	sLiabPol.accountNumber);
//		SideMenuPC sidebar = new SideMenuPC(driver);
//		sidebar.clickSideMenuPolicyReview();
//		//		GenericWorkorderPolicyReviewPL policyReview = new GenericWorkorderPolicyReviewPL(driver);
//		ArrayList<String> additionalInsureds = policyReview.getAdditionalInsureds();
//		if (!additionalInsureds.get(0).contains(policyMembers.get(0).getFirstName() + " " + policyMembers.get(0).getLastName())) {
//			throw new GuidewirePolicyCenterException(Configuration.getWebDriver().getCurrentUrl(), sLiabPol.accountNumber,	"The only Additional Insured created during generate should be on the App.");
//		}
//
//	}
//
//	@Test()
//	public void createStandardInlandMarine() throws Exception {
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//
//		ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
//		inlandMarineCoverageSelection_PL_IM.add(InlandMarine.PersonalProperty);
//
//		ArrayList<Contact> policyMembers = new ArrayList<Contact>();
//		Contact propertyAI = new Contact("Test" + NumberUtils.generateRandomNumberDigits(8), "ANI", Gender.Male, DateUtils.convertStringtoDate("01/01/1980", "MM/dd/yyyy"));
//		ArrayList<LineSelection> aiLines = new ArrayList<LineSelection>();
//		aiLines.add(LineSelection.StandardInlandMarine);
//		propertyAI.setAdditionalInsured(aiLines);
//		policyMembers.add(propertyAI);
//
//		// PersonalProperty
//		PersonalProperty pprop = new PersonalProperty();
//		pprop.setType(PersonalPropertyType.SportingEquipment);
//		pprop.setLimit(5000);
//		pprop.setDeductible(PersonalPropertyDeductible.Ded1000);
//		PersonalPropertyScheduledItem sportsScheduledItem = new PersonalPropertyScheduledItem();
//		sportsScheduledItem.setParentPersonalPropertyType(PersonalPropertyType.SportingEquipment);
//		sportsScheduledItem.setLimit(5000);
//		sportsScheduledItem.setDescription("Sports Stuff");
//		sportsScheduledItem.setType(PersonalPropertyScheduledItemType.Guns);
//		sportsScheduledItem.setMake("Honda");
//		sportsScheduledItem.setModel("Accord");
//		sportsScheduledItem.setYear(2015);
//		sportsScheduledItem.setVinSerialNum("abcd12345");
//		ArrayList<PersonalPropertyScheduledItem> sportsScheduledItems = new ArrayList<PersonalPropertyScheduledItem>();
//		sportsScheduledItems.add(sportsScheduledItem);
//		pprop.setScheduledItems(sportsScheduledItems);
//
//		PersonalPropertyList ppropList = new PersonalPropertyList();
//		ppropList.setSportingEquipment(pprop);
//
//		StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
//		myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
//		myStandardInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();
//		myStandardInlandMarine.policyMembers = policyMembers;
//
//		this.myStandardIMPolicy = new GeneratePolicy.Builder(driver)
//				.withProductType(ProductLineType.StandardIM)
//				.withLineSelection(LineSelection.StandardInlandMarine)
//				.withStandardInlandMarine(myStandardInlandMarine)
//				.withInsFirstLastName("Im", "Dues")
//				.withPaymentPlanType(PaymentPlanType.Quarterly)
//				.withDownPaymentType(PaymentType.Cash)
//				.build(GeneratePolicyType.FullApp);
//
//		loginAndSearchJob(myStandardIMPolicy.agentInfo.getAgentUserName(), myStandardIMPolicy.agentInfo.getAgentPassword(), myStandardIMPolicy.accountNumber);
//		SideMenuPC sidebar = new SideMenuPC(driver);
//		sidebar.clickSideMenuPolicyReview();
//
//		GenericWorkorderPolicyReviewPL policyReview = new GenericWorkorderPolicyReviewPL(driver);
//		ArrayList<String> additionalInsureds = policyReview.getAdditionalInsureds();
//		if (!additionalInsureds.get(0).equals(policyMembers.get(0).getFirstName() + " " + policyMembers.get(0).getLastName())) {
//			throw new GuidewirePolicyCenterException(Configuration.getWebDriver().getCurrentUrl(), myStandardIMPolicy.accountNumber, "The only Additional Insured created during generate should be on the App.");
//		}
//	}
//
//	@Test()
//	public void testCreateStandardFireWithSquire() throws Exception {
//
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
//		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
//		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
//		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
//		locToAdd.setPlNumAcres(11);
//		locationsList.add(locToAdd);
//
//		ArrayList<Contact> policyMembers = new ArrayList<Contact>();
//		Contact propertyAI = new Contact();
//		ArrayList<LineSelection> aiLines = new ArrayList<LineSelection>();
//		aiLines.add(LineSelection.PropertyAndLiabilityLinePL);
//		propertyAI.setAdditionalInsured(aiLines);
//		policyMembers.add(propertyAI);
//
//		SquireLiability myLiab = new SquireLiability();
//		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
//
//		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
//		myPropertyAndLiability.locationList = locationsList;
//		myPropertyAndLiability.liabilitySection = myLiab;
//
//		Squire mySquire = new Squire(SquireEligibility.Country);
//		mySquire.propertyAndLiability = myPropertyAndLiability;
//		mySquire.policyMembers = policyMembers;
//
//		this.stdFireLiab_Squire_PolicyObj = new GeneratePolicy.Builder(driver)
//				.withSquire(mySquire)
//				.withProductType(ProductLineType.Squire)
//				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
//				.withInsFirstLastName("SQ", "Hops")
//				.withPaymentPlanType(PaymentPlanType.Annual)
//				.withDownPaymentType(PaymentType.Cash)
//				.build(GeneratePolicyType.PolicyIssued);
//
//		// standard fire
//		ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
//		ArrayList<PLPolicyLocationProperty> locOnePropertyList1 = new ArrayList<PLPolicyLocationProperty>();
//		locOnePropertyList1.add(new PLPolicyLocationProperty(PropertyTypePL.TrellisedHops));
//		PolicyLocation locToAdd1 = new PolicyLocation(locOnePropertyList1);
//		aiLines.remove(LineSelection.PropertyAndLiabilityLinePL);
//		aiLines.add(LineSelection.StandardLiabilityPL);
//
//		locToAdd1.setPlNumAcres(11);
//		locationsList1.add(locToAdd1);
//
//		PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
//		propertyCoverages.getCoverageA().setIncreasedReplacementCost(false);
//
//		StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
//		myStandardFire.setLocationList(locationsList1);
//		myStandardFire.setPolicyMembers(policyMembers);
//
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//
//		stdFireLiab_Squire_PolicyObj.standardFire = myStandardFire;
//		stdFireLiab_Squire_PolicyObj.lineSelection.add(LineSelection.StandardFirePL);
//		stdFireLiab_Squire_PolicyObj.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.FullApp);
//
//		// Check line review for additional Insured
//		loginAndSearchJob(stdFireLiab_Squire_PolicyObj.agentInfo.getAgentUserName(), stdFireLiab_Squire_PolicyObj.agentInfo.getAgentPassword(), stdFireLiab_Squire_PolicyObj.accountNumber);
//		SideMenuPC sidebar = new SideMenuPC(driver);
//		sidebar.clickSideMenuPolicyReview();
//
//		GenericWorkorderPolicyReviewPL policyReview = new GenericWorkorderPolicyReviewPL(driver);
//		ArrayList<String> additionalInsureds = policyReview.getAdditionalInsureds();
//		if (!additionalInsureds.get(0).equals(policyMembers.get(0).getFirstName() + " " + policyMembers.get(0).getLastName())) {
//			throw new GuidewirePolicyCenterException(Configuration.getWebDriver().getCurrentUrl(),
//					stdFireLiab_Squire_PolicyObj.accountNumber,
//					"The only Additional Insured created during generate should be on the App.");
//		}
//
//	}


    private WebDriver driver;

    @Test(description = "Test Line Level additional Insureds")
    public void squireAdditionalInsured() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
        inlandMarineCoverageSelection_PL_IM.add(InlandMarine.PersonalProperty);

        ArrayList<Contact> policyMembers = new ArrayList<Contact>();
        Contact propertyAI = new Contact();
        ArrayList<LineSelection> aiLines = new ArrayList<LineSelection>();
        aiLines.add(LineSelection.PersonalAutoLinePL);
        aiLines.add(LineSelection.PropertyAndLiabilityLinePL);
        aiLines.add(LineSelection.InlandMarineLinePL);
        propertyAI.setAdditionalInsured(aiLines);
        policyMembers.add(propertyAI);

        // PersonalProperty
        PersonalProperty pprop = new PersonalProperty();
        pprop.setType(PersonalPropertyType.SportingEquipment);
        pprop.setLimit(5000);
        pprop.setDeductible(PersonalPropertyDeductible.Ded1000);
        PersonalPropertyScheduledItem sportsScheduledItem = new PersonalPropertyScheduledItem();
        sportsScheduledItem.setParentPersonalPropertyType(PersonalPropertyType.SportingEquipment);
        sportsScheduledItem.setLimit(5000);
        sportsScheduledItem.setDescription("Sports Stuff");
        sportsScheduledItem.setType(PersonalPropertyScheduledItemType.Guns);
        sportsScheduledItem.setMake("Honda");
        sportsScheduledItem.setModel("Accord");
        sportsScheduledItem.setYear(2015);
        sportsScheduledItem.setVinSerialNum("abcd12345");
        ArrayList<PersonalPropertyScheduledItem> sportsScheduledItems = new ArrayList<PersonalPropertyScheduledItem>();
        sportsScheduledItems.add(sportsScheduledItem);
        pprop.setScheduledItems(sportsScheduledItems);

        PersonalPropertyList ppropList = new PersonalPropertyList();
        ppropList.setSportingEquipment(pprop);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = inlandMarineCoverageSelection_PL_IM;
        myInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.inlandMarine = myInlandMarine;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.policyMembers = policyMembers;

        this.squire_PolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("Squire", "Insureds")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);


        // IM
        new Login(driver).loginAndSearchJob(squire_PolicyObj.agentInfo.getAgentUserName(), squire_PolicyObj.agentInfo.getAgentPassword(),
                squire_PolicyObj.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuIMExclusionsAndConditions();
        sideMenu.clickSideMenuSquireIMLineReview();

        GenericWorkorderSquireInlandMarine_LineReview imLineReview = new GenericWorkorderSquireInlandMarine_LineReview(driver);
        ArrayList<String> imAdditionalInsureds = imLineReview.getAdditionalInsureds();
        if (!imAdditionalInsureds.get(0)
                .equals(policyMembers.get(0).getFirstName() + " " + policyMembers.get(0).getLastName())) {
            Assert.fail(driver.getCurrentUrl() +
                    squire_PolicyObj.accountNumber +
                    "The only Additional Insured created during generate should be on the App.");
        }

        // pa
        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquireLineReview();
        GenericWorkorderLineReviewPL lineReview = new GenericWorkorderLineReviewPL(driver);
        ArrayList<String> additionalInsureds = lineReview.getAdditionalInsureds();
        if (!additionalInsureds.get(0)
                .equals(policyMembers.get(0).getFirstName() + " " + policyMembers.get(0).getLastName())) {
            Assert.fail(driver.getCurrentUrl() +
                    squire_PolicyObj.accountNumber +
                    "The only Additional Insured created during generate should be on the App.");
        }

        // Property
        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        sideMenu.clickSideMenuSquirePropertyLineReview();
        GenericWorkorderLineReviewPL plLineReview = new GenericWorkorderLineReviewPL(driver);
        ArrayList<String> plAdditionalInsureds = plLineReview.getAdditionalInsureds();
        if (!plAdditionalInsureds.get(0)
                .equals(policyMembers.get(0).getFirstName() + " " + policyMembers.get(0).getLastName())) {
            Assert.fail(driver.getCurrentUrl() +
                    squire_PolicyObj.accountNumber +
                    "The only Additional Insured created during generate should be on the App.");
        }
    }
}
