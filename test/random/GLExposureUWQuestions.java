package random;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.BlockingAction;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PackageRiskType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPGLExposureUWQuestions;
import repository.gw.generate.custom.CPPGeneralLiability;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoveragesCPP;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP;
import persistence.globaldatarepo.entities.GLClassCodes;
public class GLExposureUWQuestions extends BaseTest {
	//////////////////////////////////////////////////////////////////////////
	///////  TEST WILL FAIL A LOT TILL ALL THE BUGS ARE WORKED OUT  //////////
	//////////////////////////////////////////////////////////////////////////
	private WebDriver driver;
	GeneratePolicy myPolicyObj = null;
	List<GLClassCodes> classCodes = new ArrayList<GLClassCodes>();
	List<GLClassCodes> faildClassCodes = new ArrayList<GLClassCodes>();
	List<String> exposureClassCodes = new ArrayList<String>() {{		
	}};
	
	
	ArrayList<CPPGeneralLiabilityExposures> goodExposures = new ArrayList<CPPGeneralLiabilityExposures>();
	

	@SuppressWarnings("serial")
	@Test(enabled=true)
	public void verifyGLClassCodes() throws Exception {

		// LOCATIONS
		final ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>() {{
			this.add(new PolicyLocation(new AddressInfo(true), false) {{
				this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {{
					this.add(new PolicyLocationBuilding() {{
					}}); // END BUILDING
				}}); // END BUILDING LIST
			}});// END POLICY LOCATION
		}}; // END LOCATION LIST

		ArrayList<CPPGeneralLiabilityExposures> exposures = new ArrayList<CPPGeneralLiabilityExposures>();
		exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), "18206"));

		CPPGeneralLiability generalLiability = new CPPGeneralLiability() {{
			this.setCPPGeneralLiabilityExposures(exposures);
		}};
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		// GENERATE POLICY
        myPolicyObj = new GeneratePolicy.Builder(driver)
		.withProductType(ProductLineType.CPP)
		.withCPPGeneralLiability(generalLiability)
                .withLineSelection(LineSelection.GeneralLiabilityLineCPP)
		.withPolicyLocations(locationsLists)
		.withCreateNew(CreateNew.Create_New_Always)
		.withInsPersonOrCompany(ContactSubType.Company)
		.withInsCompanyName("UW Questions")
		.withPolOrgType(OrganizationType.LLC)
		.withInsPrimaryAddress(locationsLists.get(0).getAddress())
		.withPaymentPlanType(PaymentPlanType.Annual)
		.withDownPaymentType(PaymentType.Cash)
		.build(GeneratePolicyType.FullApp);

		System.out.println(myPolicyObj.accountNumber);
		System.out.println(myPolicyObj.agentInfo.getAgentUserName());

	}





	@Test(dependsOnMethods={"verifyGLClassCodes"}, enabled=true)
	public void addExposures() throws Exception {
		getQALogger().info("#########################\nRUNNING TEST ADD EXPOSURES\n######################");

		boolean testFailed = false;
		String failureList = "Questions that failed thier validation message:  \n";
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

		new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuGLExposures();

		myPolicyObj.generalLiabilityCPP.setCPPGeneralLiabilityExposures(new ArrayList<CPPGeneralLiabilityExposures>());

 		for(String classCode : exposureClassCodes) {
			getQALogger().info(classCode);
			myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0), classCode));
		}

        GenericWorkorder genwo = new GenericWorkorder(driver);
        GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
		exposuresPage.selectAll();
		exposuresPage.clickRemove();

		for(CPPGeneralLiabilityExposures exposure : myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {
			try {
				exposuresPage.clickAdd();
                exposuresPage.addExposure(exposure);
                genwo.clickGenericWorkorderSaveDraft();
                goodExposures.add(exposure);
			} catch (Exception e) {
				testFailed = true;
				failureList = failureList + "Failed setting Exposure for : " + exposure.getClassCode() + " | " + exposure.getDescription();
			}
		}

		new GuidewireHelpers(driver).logout();
		getQALogger().info("#########################\nDONE RUNNING TEST ADD EXPOSURES\n######################");
		if(testFailed) {
			getQALogger().warn(failureList);
			Assert.fail(failureList);
		}
	}


	
	
	@Test(dependsOnMethods={"addExposures"}, enabled=true)
	public void setUWQuestions() throws Exception {
		getQALogger().info("#########################\nRUNNING TEST SET UW QUESTIONS\n######################");
		boolean testFailed = false;
		String failureList = "Questions that failed to set:  \n";
		List<String> removeStuff = new ArrayList<String>();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

		new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuGLExposures();

        GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
		exposuresPage.clickLocationSpecificQuestionsTab();
		
		if(myPolicyObj.commercialPackage.packageRisk.equals(PackageRiskType.Contractor)) {
			exposuresPage.fillOutUnderwritingContractorQuestions(myPolicyObj);
		}

		for(CPPGeneralLiabilityExposures exposure : myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {
			try {
                exposuresPage.fillOutBasicUWQuestionsQQ(exposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
				//exposuresPage.fillOutUnderwritingQuestionsQQ(exposure);
				exposuresPage.fillOutBasicUWQuestionsFullApp(exposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
				//exposuresPage.fillOutUnderwritingQuestionsFULLAPP(exposure);
			} catch (Exception e) {
				testFailed = true;
				removeStuff.add(exposure.getClassCode());
				failureList = failureList + "Failed setting UW Questions for : " + exposure.getClassCode() + " | " + exposure.getDescription();
				getQALogger().warn(failureList);
			}
		}
		
		getQALogger().info("clicking save draft");
        GenericWorkorder genwo = new GenericWorkorder(driver);
		genwo.clickGenericWorkorderSaveDraft();

		getQALogger().info("get invalid questions");
		if(exposuresPage.getInvalidQuestions() != null) {
			removeStuff.addAll(exposuresPage.getInvalidQuestions());
		}
		
		getQALogger().info("remove bad exposures");
		for(String exposure : removeStuff) {
			sideMenu.clickSideMenuGLExposures();
			exposuresPage.clickExposureDetialsTab();
            WebElement exposureTable = new GuidewireHelpers(driver).find(By.xpath("//div[contains(@id, ':SubmissionWizard_GL_ExposureUnitsLV')]"));
            TableUtils tableUtils = new TableUtils(driver);
			int pages =  tableUtils.getNumberOfTablePages(exposureTable);
			
			try {
				if(pages > 1) {
					for(int i = 0; i<pages;i++) {
						if(tableUtils.getRowNumberInTableByText(exposureTable, exposure) == 0) {
							tableUtils.clickNextPageButton(exposureTable);
						} else {
							tableUtils.setCheckboxInTable(exposureTable, tableUtils.getRowNumberInTableByText(exposureTable, exposure), true);
							exposuresPage.clickRemove();
                            break;
						}
					}
				} else {
					tableUtils.setCheckboxInTable(exposureTable, tableUtils.getRowNumberInTableByText(exposureTable, exposure), true);
					exposuresPage.clickRemove();
                }
			} catch (Exception e) {
			}
		}
		
		for(String remove : removeStuff) {
			testFailed = true;
			myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures().remove(new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0), remove));
		}
		
		genwo.clickGenericWorkorderSaveDraft();

		getQALogger().info("#########################\nDONE RUNNING TEST SET UW QUESTIONS\n######################");
		if(testFailed) {
			getQALogger().warn(failureList);
			for(String failed : removeStuff) {
				getQALogger().warn("CLASS CODES THAT GOT REMOVED");
				getQALogger().warn(failed);
			}
//			throw new GuidewirePolicyCenterException(getCurrentURL(), myPolicyObj.accountNumber, failureList);
		}

	}
	
	
	
	//SET NEEDED COVERAGES QUESTIONS
	@Test(dependsOnMethods = {"setUWQuestions"}, enabled=true)
	public void setRequiredCoveragesQuestions() throws Exception {
		getQALogger().info("#########################\nRUNNING TEST SET COVERAGES QUESTIONS\n######################");
		boolean testFailed = false;
		String failureList = "Questions that failed thier validation message:  \n";
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

		new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuGLCoverages();
		

		for(CPPGeneralLiabilityExposures exposure : myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {
            GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions exclusions = new GenericWorkorderGeneralLiabilityCoverages_ExclusionsConditions(driver);
			switch(exposure.getClassCode()) {
			case "40115":
			case "40117":
                GenericWorkorderGeneralLiabilityCoveragesCPP coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
				
				coverages.clickExclusionsAndConditions();
                exclusions.addBoat();
				break;
			case "63218":
			case "63217":
			case "63220":
			case "63219":
			case "43421":
			case "43424":
                coverages = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
				coverages.clickExclusionsAndConditions();
                exclusions.setExclusion_AthleticOrSportsParticipantsCG2101_Description("Something clever goes here");
				break;
			}
		}
		//STOP HERE

		getQALogger().info("#########################\nDONE RUNNING TEST SET COVERAGES QUESTIONS\n######################");
		if(testFailed) {
			Assert.fail(failureList);
		}
	}
	

	
	
	

	@Test(dependsOnMethods = {"setRequiredCoveragesQuestions"}, enabled=true)
	public void checkFailureBlockingUser() throws Exception {
		getQALogger().info("#########################\nRUNNING TEST VERIFY BOLCKING USER QUESTIONS\n######################");
		boolean testFailed = false;
		String failureList = "Questions that failed thier validation message:  \n";
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

		new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuGLExposures();
        GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
		exposuresPage.clickLocationSpecificQuestionsTab();


		for(CPPGeneralLiabilityExposures exposure : goodExposures) {
			for(CPPGLExposureUWQuestions question : exposure.getUnderWritingQuestions()) {
				try {
                    GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability glUWQuestions = new GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability(driver);
					if(question.getBlockingAction().equals(BlockingAction.Blockuser) && question.getRequiredAt().equals(GeneratePolicyType.QuickQuote) && !question.isDependantQuestion() && question.isHandleIncorrectAnswer()) {
						glUWQuestions.setUnderwritingQuesiton_AndChildQuestions(exposure, question, glUWQuestions.getIncorrectAnswer(question));
						exposuresPage.clickNext();
                        boolean found = false;
						for(String message : exposuresPage.getValidationMessages()) {
							if(message.replaceAll("  ", " ").equals(question.getFailureMessage().replaceAll("  ", " "))) {
								found = true;
								break;
							}
						}
						if(!found) {
							testFailed = true;
							failureList = failureList + question.getQuestionText() + " | " + question.getFailureMessage() + "\n";
							found = false;
						}
                        if (!new GuidewireHelpers(driver).finds(By.xpath("//span[contains(text(), 'Exposures')]")).isEmpty()) {
							glUWQuestions.setUnderwritingQuestion(exposure, question);
						} else {
							sideMenu.clickSideMenuGLExposures();
                            exposuresPage.clickLocationSpecificQuestionsTab();
                        }
					}
				} catch (Exception e) {
					testFailed = true;
					failureList = failureList + " FAILED ON EXPOSURE |" + exposure.getClassCode() + " ON QUESTION | " + question.getQuestionText();
					getQALogger().warn(failureList);
				}
			}
		}
		getQALogger().info("#########################\nDONE RUNNING TEST VERIFY BOLCKING USER QUESTIONS\n######################");
		if(testFailed) {
			Assert.fail(failureList);
		}
	}
	
//	@Test(dependsOnMethods={"checkFailureBlockingUser"})
//	public void converToFullApp() throws Exception {
//		systemOut("#########################\nCONVERT TO FULL APP\n######################");
//		
//		Configuration.setProduct(Product.PolicyCenter);
//		myPolicyObj = new GeneratePolicy(GeneratePolicyType.FullApp, myPolicyObj);
//	}


	@Test(dependsOnMethods={"setRequiredCoveragesQuestions"}, enabled=true)
    public void verifyUWBlockingQuestions() throws Exception {
		getQALogger().info("#########################\nRUNNING TEST VERIFY BOLCKING UW QUESTIONS\n######################");
		boolean testFailed = false;
		String failureList = "Questions that failed thier UW Quote validation message:  \n";
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

		new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuGLExposures();
        GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
		exposuresPage.clickLocationSpecificQuestionsTab();

        GenericWorkorder genwo = new GenericWorkorder(driver);
		
		for(CPPGeneralLiabilityExposures exposure : goodExposures) {
			for(CPPGLExposureUWQuestions question : exposure.getUnderWritingQuestions()) {
				if (question.getDuplicateQuestions() != null) {
					if (!question.getDuplicateQuestions().isEmpty()) {
						if (question.getUwIssueType() != null) {
							getQALogger().warn("QUESTION NEEDS MANUALLY TESTED : " + exposure.getClassCode() + " | " + question.getQuestionText());
						}
					}
				}
				if (question.getUwIssueType() != null) {
					if (question.getUwIssueType().equals("Question blocking quote") && !question.isDependantQuestion() && question.isHandleIncorrectAnswer() && question.getDuplicateQuestions().isEmpty()) {
						boolean questionSet = true;
                        GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability glUWQuestions = new GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability(driver);
						try {
							glUWQuestions.setUnderwritingQuesiton_AndChildQuestions(exposure, question, glUWQuestions.getIncorrectAnswer(question));
                            sideMenu.clickSideMenuGLCoverages();
                            genwo.clickGenericWorkorderQuote();
                            sideMenu.clickSideMenuRiskAnalysis();
							boolean found = false;
                            List<WebElement> uwIssues = new GuidewireHelpers(driver).finds(By.xpath("//a[contains(@id, ':ShortDescription')]"));
							if (!uwIssues.isEmpty()) {
								for (WebElement issue : uwIssues) {
									getQALogger().warn(issue.getText());
									getQALogger().warn(question.getFailureMessage());
									if (issue.getText().replace("  ", " ").trim().equals(question.getFailureMessage())) {
										found = true;
										break;
									}
								}
								if (!found) {
									testFailed = true;
									failureList = failureList + exposure.getDescription() + " | " + question.getQuestionText();
								}
							}
						} catch (Exception e) {
							testFailed = true;
							failureList = failureList + " FAILED ON EXPOSURE | " + exposure.getClassCode() + " ON QUESTION | " + question.getQuestionText() + " UNDERWRITER ISSUE.\n";
							getQALogger().warn(failureList);
						} finally {
							sideMenu.clickSideMenuGLExposures();
							new GuidewireHelpers(driver).editPolicyTransaction();
                            exposuresPage.clickLocationSpecificQuestionsTab();
                            if (questionSet) {
								glUWQuestions.setUnderwritingQuestion(exposure, question);
							}
						}

					}
				}
			}
		}
		getQALogger().info("#########################\nDONE RUNNING TEST VERIFY BOLCKING UW QUESTIONS\n######################");
		if(testFailed) {
			Assert.fail(failureList);
		}
		
	}







}



































