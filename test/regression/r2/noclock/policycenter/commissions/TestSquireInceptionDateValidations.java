package regression.r2.noclock.policycenter.commissions;

import org.testng.annotations.AfterMethod;

import com.idfbins.testng.listener.QuarantineClass;

import com.idfbins.driver.BaseTest;

/**
 * @Author nvadlamudi
 * @Requirement :US7266
 * @RequirementsLink <a
 * href="http://projects.idfbins.com/policycenter/Documents/
 * Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%
 * 208.0/Squire/PC8%20-%20Squire%20-%20QuoteApplication%20-%
 * 20Inception%20Date.xlsx</a>
 * @Description
 * @DATE May 17, 2016
 */
@QuarantineClass
public class TestSquireInceptionDateValidations extends BaseTest {
	/*private GeneratePolicy mySquirePolicy = null;
	private GeneratePolicy myPolicyObjBOP;
	private GeneratePolicy myPolicyObjCPP;*/

	@AfterMethod(alwaysRun = true)
	public void afterMethod() throws Exception {

	}

	/*@Test(dependsOnMethods = { "createSquirePolicy" })
	public void validate_InceptionDates() throws Exception {
		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
		boolean testFailed = false;
		String errorMessage = "";
		loginAndSearchSubmission(mySquirePolicy.agentInfo.getAgentUserName(),
				mySquirePolicy.agentInfo.getAgentPassword(), mySquirePolicy.accountNumber);

		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();

		ISideMenu sideMenu = SideMenuFactory.getMenu();
		sideMenu.clickSideMenuPolicyInfo();

		polInfo.setTransferedFromAnotherPolicy(true);
				ISearchAddressBook addressBook = SearchFactory.getSearchAddressBook();

		// Enter a cancelled (6months older) policy and validate the Inception
		// date
		Date newCancelDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -190);
		InceptionDateStuff pol1 = ExistingPoliciesHelper.getRandomPolicyForInceptionDates(
				Configuration.getServerFromPropsFile().toUpperCase(), PolicyType.Squire,
				PolicyLineType.PersonalAutoLinePL, false, newCancelDate, false);

		if (polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy,
				pol1.getPolicyPeriodPolicyNumber())) { // policy number need to
														// be modified with
														// database generic
														// method
			testFailed = true;
			errorMessage = errorMessage + "Select button exists for cancelled - more than 6 months policy: \n";
		} else {
			addressBook.ClickReturnToPolicyInfo();
					}

		// Enter a cancelled (with in 6months) policy and validate the Inception
		// date
		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
		newCancelDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -90);
		InceptionDateStuff pol2 = ExistingPoliciesHelper.getRandomPolicyForInceptionDates(
				Configuration.getServerFromPropsFile().toUpperCase(), PolicyType.Squire,
				PolicyLineType.PersonalAutoLinePL, true, newCancelDate, false);

		if (polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy,
				pol2.getPolicyPeriodPolicyNumber())) { // policy number need to
														// be modified with
														// database generic
														// method
			Date expectedPolicyEffectiveDate = pol2.getPolicyIssueDate();// "05/10/2016";
			String currentPolicyInceptionDate = polInfo
					.getInceptionDateFromTableBySelection(InceptionDateSections.Policy);

			if (expectedPolicyEffectiveDate
					.equals(DateUtils.convertStringtoDate(currentPolicyInceptionDate, "MM/dd/yyyy"))) {
				systemOut("Canceled within 6 months: Expected policy Effective Date : " + expectedPolicyEffectiveDate
						+ " is displayed.");
			} else {
				testFailed = true;
				errorMessage = errorMessage + "Canceled within 6 months: Expected Policy Inception Date: "
						+ expectedPolicyEffectiveDate + " Actual Policy Inception Date : " + currentPolicyInceptionDate
						+ " are not displayed.  \n";
			}
		}

		// Enter a expired (6months older) policy and validate the Inception
		// date
		Date newExpireDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -190);
		InceptionDateStuff pol3 = ExistingPoliciesHelper.getRandomPolicyForInceptionDates(
				Configuration.getServerFromPropsFile().toUpperCase(), PolicyType.Squire,
				PolicyLineType.PersonalAutoLinePL, false, newExpireDate, true);

		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
		if (polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy,
				pol3.getPolicyPeriodPolicyNumber())) { // policy number need to
														// be modified with
														// database generic
														// method
			testFailed = true;
			errorMessage = errorMessage + "Select button exists for expired - more than 6 months policy: \n";
		} else {
			addressBook.ClickReturnToPolicyInfo();
					}

		// Enter a expired (with in 6months) policy and validate the Inception
		// date
		newExpireDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -90);
		InceptionDateStuff pol4 = ExistingPoliciesHelper.getRandomPolicyForInceptionDates(
				Configuration.getServerFromPropsFile().toUpperCase(), PolicyType.Squire,
				PolicyLineType.PersonalAutoLinePL, true, newExpireDate, true);

		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, pol4.getPolicyPeriodPolicyNumber()); // policy
																														// number
																														// need
																														// to
																														// be
																														// modified
																														// with
																														// database
																														// generic
																														// method
		Date expectedPolicyEffectiveDate = pol4.getPolicyIssueDate();// "05/10/2016";
		String currentPolicyInceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy);
		if (expectedPolicyEffectiveDate
				.equals(DateUtils.convertStringtoDate(currentPolicyInceptionDate, "MM/dd/yyyy"))) {
			systemOut("Expired within 6 months: Expected policy Effective Date : " + expectedPolicyEffectiveDate
					+ " is displayed.");
		} else {
			testFailed = true;
			errorMessage = errorMessage + "Expired within 6 months: Expected Policy Inception Date: "
					+ expectedPolicyEffectiveDate + " Actual Policy Inception Date : " + currentPolicyInceptionDate
					+ " are not displayed.  \n";
		}

		// Enter a valid policy with section 1 and validate the Inception date
		InceptionDateStuff pol5 = ExistingPoliciesHelper.getRandomPolicyForInceptionDates(
				Configuration.getServerFromPropsFile().toUpperCase(), PolicyType.Squire,
				PolicyLineType.PropertyAndLiabilityLinePL, false, newExpireDate, false);

		// Enter a valid policy with section 1 and validate the Inception date
		// policy number need to be modified with database generic method
		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, pol5.getPolicyPeriodPolicyNumber());
		String valPolInceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy);
		String valSection1InceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Property);
		String valExpectedPolInceptionDate = "05/10/2016";
		String valExpectedSection1InceptionDate = "05/10/2016";

		if (DateUtils.convertStringtoDate(valPolInceptionDate, "MM/dd/yyyy")
				.equals(DateUtils.convertStringtoDate(valExpectedPolInceptionDate, "MM/dd/yyyy"))
				&& (DateUtils.convertStringtoDate(valSection1InceptionDate, "MM/dd/yyyy")
						.equals(DateUtils.convertStringtoDate(valExpectedSection1InceptionDate, "MM/dd/yyyy")))) {
			systemOut("Expected Policy Inception Date : " + valPolInceptionDate
					+ " and Expected Section I Inception Date : " + valSection1InceptionDate + " are displayed.");
		} else {
			testFailed = true;
			errorMessage = errorMessage + "Expected Policy Inception Date: " + valExpectedPolInceptionDate
					+ " Expected Section 1 Inception Date : " + valExpectedSection1InceptionDate
					+ " are not displayed.  \n";
		}

		// Enter a valid policy with section 2 and validate the Inception date
		// policy number need to be modified with database generic method
		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, "252327");
		String val2PolInceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy);
		String val2Section1InceptionDate = polInfo
				.getInceptionDateFromTableBySelection(InceptionDateSections.Liability);
		String val2ExpectedPolInceptionDate = "05/10/2016";
		String val2ExpectedSection1InceptionDate = "05/10/2016";

		if (DateUtils.convertStringtoDate(val2PolInceptionDate, "MM/dd/yyyy")
				.equals(DateUtils.convertStringtoDate(val2ExpectedPolInceptionDate, "MM/dd/yyyy"))
				&& (DateUtils.convertStringtoDate(val2Section1InceptionDate, "MM/dd/yyyy")
						.equals(DateUtils.convertStringtoDate(val2ExpectedSection1InceptionDate, "MM/dd/yyyy")))) {
			systemOut("Expected Policy Inception Date : " + val2PolInceptionDate
					+ " and Expected Section 2 Inception Date : " + val2Section1InceptionDate + " are displayed.");
		} else {
			testFailed = true;
			errorMessage = errorMessage + "Expected Policy Inception Date: " + val2ExpectedPolInceptionDate
					+ " Expected Section 2 Inception Date : " + val2ExpectedSection1InceptionDate
					+ " are not displayed.  \n";
		}

		// Enter a valid policy with section 3 and validate the Inception date
		// policy number need to be modified with database generic method
		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, "252506");
		String val3PolInceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy);
		String val3Section1InceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Auto);
		String val3ExpectedPolInceptionDate = "05/16/2016";
		String val3ExpectedSection1InceptionDate = "05/16/2016";

		if (DateUtils.convertStringtoDate(val3PolInceptionDate, "MM/dd/yyyy")
				.equals(DateUtils.convertStringtoDate(val3ExpectedPolInceptionDate, "MM/dd/yyyy"))
				&& (DateUtils.convertStringtoDate(val3Section1InceptionDate, "MM/dd/yyyy")
						.equals(DateUtils.convertStringtoDate(val3ExpectedSection1InceptionDate, "MM/dd/yyyy")))) {
			systemOut("Expected Policy Inception Date : " + val3PolInceptionDate
					+ " and Expected Section 3 Inception Date : " + val3Section1InceptionDate + " are displayed.");
		} else {
			testFailed = true;
			errorMessage = errorMessage + "Expected Policy Inception Date: " + val3ExpectedPolInceptionDate
					+ " Expected Section 3 Inception Date : " + val3ExpectedSection1InceptionDate
					+ " are not displayed.  \n";
		}

		// Enter a valid policy with section 4 and validate the Inception date
		// policy number need to be modified with database generic method
		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, "252506");
		String val4PolInceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy);
		String val4Section4InceptionDate = polInfo
				.getInceptionDateFromTableBySelection(InceptionDateSections.InlandMarine);

		String val4ExpectedPolInceptionDate = "05/16/2016";
		String val4ExpectedSection4InceptionDate = "05/16/2016";

		if (DateUtils.convertStringtoDate(val4PolInceptionDate, "MM/dd/yyyy")
				.equals(DateUtils.convertStringtoDate(val4ExpectedPolInceptionDate, "MM/dd/yyyy"))
				&& (DateUtils.convertStringtoDate(val4Section4InceptionDate, "MM/dd/yyyy")
						.equals(DateUtils.convertStringtoDate(val4ExpectedSection4InceptionDate, "MM/dd/yyyy")))) {
			systemOut("Expected Policy Inception Date : " + val4PolInceptionDate
					+ " and Expected Section 4 Inception Date : " + val4Section4InceptionDate + " are displayed.");
		} else {
			testFailed = true;
			errorMessage = errorMessage + "Expected Policy Inception Date: " + val4ExpectedPolInceptionDate
					+ " Expected Section 4 Inception Date : " + val4ExpectedSection4InceptionDate
					+ " are not displayed.  \n";
		}

		// Enter a specific section Inception date and validate the Inception
		// date
		// policy number need to be modified with database generic method
		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Auto, "252550");
		String section3InceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Auto);
		String expectedSection3InceptionDate = "05/16/2016";

		if (DateUtils.convertStringtoDate(section3InceptionDate, "MM/dd/yyyy")
				.equals(DateUtils.convertStringtoDate(expectedSection3InceptionDate, "MM/dd/yyyy"))) {
			systemOut("Expected Section 3 Inception Date : " + section3InceptionDate + " are displayed.");
		} else {
			testFailed = true;
			errorMessage = errorMessage + " Expected Section 3 Inception Date : " + expectedSection3InceptionDate
					+ " are not displayed.  \n";
		}

		if (testFailed) {
			markTestAsExpectedToFailUntilAndAssert(false, errorMessage, "test data need to be modified.",
					"nvadlamudi@idfbins.com");
		}

	}

	@Test()
	private void createSquirePolicy() throws Exception {
		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
		Date newCancelDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -1);
		System.out.println(newCancelDate);	
		ArrayList<ExistingPolicyLookUp> mySquirePolicy = ExistingPoliciesHelper.getRandomPolicyByType(Configuration.getServerFromPropsFile().toUpperCase(), PolicyType.Squire, PolicyLineType.PropertyAndLiabilityLinePL, "Submission", newCancelDate , SqlSigns.LessThan);
		String Pol1 = mySquirePolicy.get(0).getPolicyNumber();
		System.out.println(Pol1);	
	}


	@Test()
	private void createPolicyBOP() throws Exception {
		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
		Date newCancelDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -1);
		System.out.println(newCancelDate);	
		ArrayList<ExistingPolicyLookUp> mySquirePolicy = ExistingPoliciesHelper.getRandomPolicyByType(Configuration.getServerFromPropsFile().toUpperCase(), PolicyType.Businessowners, PolicyLineType.Businessowners, "Submission", newCancelDate , SqlSigns.LessThan);
		String Pol2 = mySquirePolicy.get(0).getPolicyNumber();
		System.out.println(Pol2);
	}

	@Test(dependsOnMethods = { "createPolicyBOP" })
	private void testValidateInvoiceDateBOP() throws Exception {
		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
		boolean testFailed = false;
		String errorMessage = "";
		loginAndSearchSubmission(myPolicyObjBOP.agentInfo.getAgentUserName(),
				myPolicyObjBOP.agentInfo.getAgentPassword(), myPolicyObjBOP.accountNumber);

		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();

		ISideMenu sideMenu = SideMenuFactory.getMenu();
		sideMenu.clickSideMenuPolicyInfo();

		polInfo.setTransferedFromAnotherPolicy(true);
				polInfo.setPolicyOutOfForceMoreThan6MonthsRadio(false);

		// entering a valid policy number
		if (!polInfo.setPreviousPolicyNumberBOP("236556")) {
			testFailed = true;
			errorMessage = "Expected Inception date is not displayed.\n";
		}
		// more than cancelled policy - 6 months old
		ISearchAddressBook addressBook = SearchFactory.getSearchAddressBook();

		if (polInfo.setPreviousPolicyNumberBOP("236556")) {
			testFailed = true;
			errorMessage = "Select button exists for canceled policies \n";
		} else {
			addressBook.ClickReturnToPolicyInfo();
		}

		if (testFailed) {
			markTestAsExpectedToFailUntilAndAssert(false, errorMessage, "test data need to be modified.",
					"nvadlamudi@idfbins.com");
		}

	}*/

	/*@Test()
	private void createPolicyCPP() throws Exception {
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

		CPPCommercialAuto commercialAuto = new CPPCommercialAuto();
		commercialAuto.setCommercialAutoLine(new CPPCommercialAutoLine());

		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle vehToAdd = new Vehicle();
		vehToAdd.setGaragedAt(locationsList.get(0).getAddress());
		vehicleList.add(vehToAdd);
		commercialAuto.setVehicleList(vehicleList);
		commercialAuto.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());

		ArrayList<Person> driversList = new ArrayList<Person>();
		Person driToAdd = new Person();
		driToAdd.setGender(Gender.Male);
		driversList.add(driToAdd);
		commercialAuto.setDriversList(driversList);

		ArrayList<LineSelection> lines = new ArrayList<LineSelection>();
		lines.add(LineSelection.CommercialAutoLineCPP);

		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);

		this.myPolicyObjCPP = new GeneratePolicy.Builder(driver).withProductType(ProductLineType.CPP).withLineSelection(lines)
				.withCPPCommercialAuto(commercialAuto).withPolicyLocations(locationsList)
				.withInsCompanyName("Test InCPP").build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "createPolicyCPP" })
	private void testValidateInceptionDateCPP() throws Exception {
		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
		boolean testFailed = false;
		String errorMessage = "";
		loginAndSearchSubmission(myPolicyObjCPP.agentInfo.getAgentUserName(),
				myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();

		ISideMenu sideMenu = SideMenuFactory.getMenu();
		sideMenu.clickSideMenuPolicyInfo();

		polInfo.setTransferedFromAnotherPolicy(true);

				ISearchAddressBook addressBook = SearchFactory.getSearchAddressBook();

		// Enter a cancelled (6months older) policy and validate the Inception
		// date
		// policy number need to be modified with database generic method
		if (polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, "252327")) {
			testFailed = true;
			errorMessage = errorMessage + "Select button exists for cancelled - more than 6 months policy: \n";
		} else {
			addressBook.ClickReturnToPolicyInfo();
					}

		// Enter a cancelled (with in 6months) policy and validate the Inception
		// date
		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
		if (polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, "252327")) {
			String expectedPolicyEffectiveDate = "05/10/2016";
			String currentPolicyInceptionDate = polInfo
					.getInceptionDateFromTableBySelection(InceptionDateSections.Policy);

			if (DateUtils.convertStringtoDate(expectedPolicyEffectiveDate, "MM/dd/yyyy")
					.equals(DateUtils.convertStringtoDate(currentPolicyInceptionDate, "MM/dd/yyyy"))) {
				systemOut("Canceled within 6 months: Expected policy Effective Date : " + expectedPolicyEffectiveDate
						+ " is displayed.");
			} else {
				testFailed = true;
				errorMessage = errorMessage + "Canceled within 6 months: Expected Policy Inception Date: "
						+ expectedPolicyEffectiveDate + " Actual Policy Inception Date : " + currentPolicyInceptionDate
						+ " are not displayed.  \n";
			}
		}

		// Enter a expired (6months older) policy and validate the Inception
		// date
		// policy number need to be modified with database generic method
		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
		if (polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, "252327")) {
			testFailed = true;
			errorMessage = errorMessage + "Select button exists for expired - more than 6 months policy: \n";
		} else {
			addressBook.ClickReturnToPolicyInfo();
					}

		// Enter a expired (with in 6months) policy and validate the Inception
		// date
		// policy number need to be modified with database generic method
		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, "252327");
		String expectedPolicyEffectiveDate = "05/10/2016";
		String currentPolicyInceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy);
		if (DateUtils.convertStringtoDate(expectedPolicyEffectiveDate, "MM/dd/yyyy")
				.equals(DateUtils.convertStringtoDate(currentPolicyInceptionDate, "MM/dd/yyyy"))) {
			systemOut("Expired within 6 months: Expected policy Effective Date : " + expectedPolicyEffectiveDate
					+ " is displayed.");
		} else {
			testFailed = true;
			errorMessage = errorMessage + "Expired within 6 months: Expected Policy Inception Date: "
					+ expectedPolicyEffectiveDate + " Actual Policy Inception Date : " + currentPolicyInceptionDate
					+ " are not displayed.  \n";
		}

		// Enter a valid policy with section 1 and validate the Inception date
		// policy number need to be modified with database generic method
		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, "252327");
		String valPolInceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy);
		String valSection1InceptionDate = polInfo
				.getInceptionDateFromTableBySelection(InceptionDateSections.CPPProperty);
		String valExpectedPolInceptionDate = "05/10/2016";
		String valExpectedSection1InceptionDate = "05/10/2016";

		if (DateUtils.convertStringtoDate(valPolInceptionDate, "MM/dd/yyyy")
				.equals(DateUtils.convertStringtoDate(valExpectedPolInceptionDate, "MM/dd/yyyy"))
				&& (DateUtils.convertStringtoDate(valSection1InceptionDate, "MM/dd/yyyy")
						.equals(DateUtils.convertStringtoDate(valExpectedSection1InceptionDate, "MM/dd/yyyy")))) {
			systemOut("Expected Policy Inception Date : " + valPolInceptionDate
					+ " and Expected Section I Inception Date : " + valSection1InceptionDate + " are displayed.");
		} else {
			testFailed = true;
			errorMessage = errorMessage + "Expected Policy Inception Date: " + valExpectedPolInceptionDate
					+ " Expected Section 1 Inception Date : " + valExpectedSection1InceptionDate
					+ " are not displayed.  \n";
		}

		// Enter a valid policy with section 2 and validate the Inception date
		// policy number need to be modified with database generic method
		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, "252327");
		String val2PolInceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy);
		String val2Section1InceptionDate = polInfo
				.getInceptionDateFromTableBySelection(InceptionDateSections.CPPLiability);
		String val2ExpectedPolInceptionDate = "05/10/2016";
		String val2ExpectedSection1InceptionDate = "05/10/2016";

		if (DateUtils.convertStringtoDate(val2PolInceptionDate, "MM/dd/yyyy")
				.equals(DateUtils.convertStringtoDate(val2ExpectedPolInceptionDate, "MM/dd/yyyy"))
				&& (DateUtils.convertStringtoDate(val2Section1InceptionDate, "MM/dd/yyyy")
						.equals(DateUtils.convertStringtoDate(val2ExpectedSection1InceptionDate, "MM/dd/yyyy")))) {
			systemOut("Expected Policy Inception Date : " + val2PolInceptionDate
					+ " and Expected Section 2 Inception Date : " + val2Section1InceptionDate + " are displayed.");
		} else {
			testFailed = true;
			errorMessage = errorMessage + "Expected Policy Inception Date: " + val2ExpectedPolInceptionDate
					+ " Expected Section 2 Inception Date : " + val2ExpectedSection1InceptionDate
					+ " are not displayed.  \n";
		}

		// Enter a valid policy with section 3 and validate the Inception date
		// policy number need to be modified with database generic method
		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, "252506");
		String val3PolInceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy);
		String val3Section1InceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.CPPAuto);
		String val3ExpectedPolInceptionDate = "05/16/2016";
		String val3ExpectedSection1InceptionDate = "05/16/2016";

		if (DateUtils.convertStringtoDate(val3PolInceptionDate, "MM/dd/yyyy")
				.equals(DateUtils.convertStringtoDate(val3ExpectedPolInceptionDate, "MM/dd/yyyy"))
				&& (DateUtils.convertStringtoDate(val3Section1InceptionDate, "MM/dd/yyyy")
						.equals(DateUtils.convertStringtoDate(val3ExpectedSection1InceptionDate, "MM/dd/yyyy")))) {
			systemOut("Expected Policy Inception Date : " + val3PolInceptionDate
					+ " and Expected Section 3 Inception Date : " + val3Section1InceptionDate + " are displayed.");
		} else {
			testFailed = true;
			errorMessage = errorMessage + "Expected Policy Inception Date: " + val3ExpectedPolInceptionDate
					+ " Expected Section 3 Inception Date : " + val3ExpectedSection1InceptionDate
					+ " are not displayed.  \n";
		}

		// Enter a specific section Inception date and validate the Inception
		// date
		// policy number need to be modified with database generic method
		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Auto, "252550");
		String section3InceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.CPPAuto);
		String expectedSection3InceptionDate = "05/16/2016";

		if (DateUtils.convertStringtoDate(section3InceptionDate, "MM/dd/yyyy")
				.equals(DateUtils.convertStringtoDate(expectedSection3InceptionDate, "MM/dd/yyyy"))) {
			systemOut("Expected Section 3 Inception Date : " + section3InceptionDate + " are displayed.");
		} else {
			testFailed = true;
			errorMessage = errorMessage + " Expected Section 3 Inception Date : " + expectedSection3InceptionDate
					+ " are not displayed.  \n";
		}

		if (testFailed) {
			markTestAsExpectedToFailUntilAndAssert(false, errorMessage, "test data need to be modified.",
					"nvadlamudi@idfbins.com");
		}

	}*/
}
