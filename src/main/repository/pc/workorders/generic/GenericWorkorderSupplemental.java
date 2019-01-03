package repository.pc.workorders.generic;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.enums.BuildingClassCode;
import repository.gw.enums.EmploymentPracticesLiabilityInsuranceIfApplicantIsA;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.pc.sidemenu.SideMenuPC;


public class GenericWorkorderSupplemental extends GenericWorkorder {
    private WebDriver driver;
    public GenericWorkorderSupplemental(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private void setCheckbox(String checkboxText, Boolean checked) {
        
        //List<WebElement> web = finds(By.xpath("//td/div[contains(text(), '" + checkboxText + "')]/parent::td/following-sibling::td/div/img"));
        WebElement checkbox = find(By.xpath("//td/div[contains(text(), '" + checkboxText + "')]/parent::td/following-sibling::td/div/img"));
//		List<WebElement> checkboxList = finds(By.xpath("//td/div[contains(text(), '" + checkboxText + "')]/parent::td/following-sibling::td/div/img"));
        if (checked) {
            if (!checkbox.getAttribute("class").contains("-checked")) {
                dragAndDrop(checkbox, 0, 0);
            }
        } else {
            if (checkbox.getAttribute("class").contains("-checked")) {
                dragAndDrop(checkbox, 0, 0);
            }
        }
    }

    public Boolean getRandBoolean() {
	    return new Random().nextBoolean();
	}
    
    private void setRadioButton(String radioText, Boolean yesno) {
        
        String radioYesNo = (yesno) ? "Yes" : "No";
        //List<WebElement> web = finds(By.xpath("//div[contains(text(), '" + radioText + "')]/parent::td/following-sibling::td/div/table/tbody/tr/child::td/child::label[contains(text(), '" + radioYesNo + "')]/preceding-sibling::input"));
        WebElement radio = find(By.xpath("//div[contains(text(), '" + radioText + "')]/parent::td/following-sibling::td/div/table/tbody/tr/child::td/child::label[contains(text(), '" + radioYesNo + "')]/preceding-sibling::input"));
        List<WebElement> radioList = radio.findElements(By.xpath("/parent::td[contains(@class, '-checked')]"));
        if (radioList.size() <= 0) {
            radio.click();
        }
    }


    @FindBy(xpath = "//div[contains(@id, 'BOPSupplementalScreen:QuestionSetsDV:3:QuestionSetLV-body')]/following-sibling::div[1]/table/tbody/tr/child::td/input[contains(@name, 'c2')]")
    public WebElement editbox_CateringGrossReceipts;

    @FindBy(xpath = "//div[contains(text(), 'Provide gross receipts.')]/parent::td/following-sibling::td/div")
    public WebElement div_CateringGrossReceipts;

    @FindBy(xpath = "//div[contains(@id, 'BOPSupplementalScreen:QuestionSetsDV:3:QuestionSetLV-body')]/following-sibling::div[1]/table/tbody/tr/child::td/input[contains(@name, 'c2')]")
    public WebElement editbox_LiquorGrossReceipts;

    @FindBy(xpath = "//div[contains(text(), 'Provide gross receipts from liquor sales')]/parent::td/following-sibling::td/div")
    public WebElement div_liquorGrossReceipts;

    @FindBy(xpath = "//div[contains(@id, 'BOPSupplementalScreen:QuestionSetsDV:3:QuestionSetLV-body')]/following-sibling::div[1]/table/tbody/tr/child::td/textarea[contains(@name, 'c2')]")
    public WebElement editbox_OtherDescription;

    @FindBy(xpath = "//div[contains(text(), 'Description')]/parent::td/following-sibling::td/div")
    public WebElement div_OtherDescription;

    @FindBy(xpath = "//div[contains(text(), 'rooms available with kitchens')]/parent::td/following-sibling::td[1]/div")
    public WebElement edixBox_PercentOfRoomsWithKitchens;

    @FindBy(xpath = "//div[contains(text(), 'Alcoholic gross sales')]/parent::td/following-sibling::td[1]/div")
    public WebElement edixBox_MotelAlchoholGrossReciepts;

    @FindBy(xpath = "//div[contains(text(), 'Please provide the name on the liquor license.')]/parent::td/following-sibling::td[1]/div")
    public WebElement editBox_LiquorLicense;

//    @editor ecoleman 5/21/18 : broken xpaths
//    @FindBy(xpath = "//div[contans(@class, 'x-small-editor')]/table/tbody/tr/child::td/input")
//    public WebElement input_LiquorLicense;
    
    @FindBy(xpath = "//table/tbody/tr[contains(@id, 'textfield')]/td/input[contains(@name, 'c2')]")
    public WebElement input_LiquorLicense;    

    @FindBy(xpath = "//div[contains(text(), 'What is the expiration date of the liquor license?')]/parent::td/following-sibling::td[1]/div")
    public WebElement editBox_LiquorLicenseExpirationDate;

//  @editor ecoleman 5/21/18 : broken xpaths
//    @FindBy(xpath = "//div[contans(@class, 'x-small-editor')]/table/tbody/tr/child::td/table.tbody/tr/child::td/input")
//    public WebElement input_LiquorLicenseExpirationDate;
    
    @FindBy(xpath = "//td[contains(@id, 'datefield')]/table/tbody/tr/td/input[contains(@name, 'c2')]")
    public WebElement input_LiquorLicenseExpirationDate;

    @FindBy(xpath = "//div[contains(text(), 'How long has applicant operated this business at this location?')]/parent::td/following-sibling::td[1]/div")
    public WebElement editBox_TimeBusinessAtLocation;

    @FindBy(xpath = "//div[contains(text(), 'Describe management experience in this industry.')]/parent::td/following-sibling::td[1]/div")
    public WebElement editBox_ManagementExperianceInIndustry;

    @FindBy(xpath = "//div[contains(text(), 'Who is the current liquor liability insurer?')]/parent::td/following-sibling::td[1]/div")
    public WebElement editBox_LiquorLiabilityInsurer;

    @FindBy(xpath = "//div[contains(text(), 'Comments:')]/parent::td/following-sibling::td[1]/div")
    public WebElement editBox_LiquorLiabilityComments;

    @FindBy(xpath = "//div[contains(text(), 'Describe the circumstances that the applicant provides liquor.')]/parent::td/following-sibling::td[1]/div")
    public WebElement editBox_HowOftenApplicantProvideLiquor;

    @FindBy(xpath = "//div[contains(text(), 'How often does the applicant provide liquor?')]/parent::td/following-sibling::td[1]/div")
    public WebElement editbox_CircumstancesApplicantProvideLiquor;

    @FindBy(xpath = "//div[contains(text(), 'Why is the \"host liquor\" coverage not sufficient for the applicant's operations?')]/parent::td/following-sibling::td[1]/div")
    public WebElement editbox_HostLiquorCoverageNotSufficient;

    @FindBy(xpath = "//div[contains(text(), 'Current Policy Year Liquor Receipts (including beer/wine)')]/parent::td/following-sibling::td[1]/div")
    public WebElement editBox_LiquorReceipts;

    @FindBy(xpath = "//div[contains(text(), 'Current Policy Year Food Receipts')]/parent::td/following-sibling::td[1]/div")
    public WebElement editbox_FoodReceipts;

    @FindBy(xpath = "//div[contains(text(), 'Current Policy Year Package Receipts (retail sales)')]/parent::td/following-sibling::td[1]/div")
    public WebElement editbox_PackageReceipts;

    @FindBy(xpath = "//div[contains(text(), 'Current Policy Year Other Receipts')]/parent::td/following-sibling::td[1]/div")
    public WebElement editbox_OtherReceipts;

    @FindBy(xpath = "//div[contains(text(), 'Please describe')]/parent::td/following-sibling::td[1]/div")
    public WebElement editBox_OffPremisesEventsDescription;

    @FindBy(xpath = "//div[contains(text(), 'Provide date(s) and details of citations and management's response.')]/parent::td/following-sibling::td[1]/div")
    public WebElement editBox_EmployeeViolationLast3Years;

    @FindBy(xpath = "//div[contains(text(), 'Provide date(s), descriptions of claims, status and what was management response.')]/parent::td/following-sibling::td[1]/div")
    public WebElement editBox_LiquorLiabilityClaims;

    @FindBy(xpath = "//div[contains(text(), 'Explain')]/parent::td/following-sibling::td[1]/div")
    public WebElement editBox_LiquorCoverageCanceledExpaination;

    @FindBy(xpath = "//div[contains(text(), 'Has applicant ever had the liquor license suspended or revoked?')]/parent::td/parent::tr/following-sibling::tr[1]/td/div")
    public WebElement editBox_LiquorLicenseCanceledRevocedExpaination;

    @FindBy(xpath = "//div[contains(@class, 'x-small-editor')]/table/tbody/tr/child::td/textarea")
    public WebElement textarea_MultiUseTextarea;

//    @FindBy(xpath = "//div[contains(@class, 'x-small-editor')]/table/tbody/tr/child::td/input")
//    public WebElement textarea_MultiUseInput;
    
    @FindBy(xpath = "//table[contains(@id, 'textfield')]/tbody/tr/td/input[contains(@name, 'c2')]")
    public WebElement textarea_MultiUseInput;


    public void handleSupplementalQuestions2(ArrayList<PolicyLocation> locationList, boolean retry, boolean quickQuote, GeneratePolicy policy) {
    	new SideMenuPC(driver).clickSideMenuSupplemental();
//        try {
            if (policy.busOwnLine.getAdditionalCoverageStuff().employmentPracticesLiabilityInsurance_exists && !quickQuote) {
                if (policy.busOwnLine.getAdditionalCoverageStuff().isEmploymentPracticesLiabilityInsurance()) {
                    radio_EPLI_Does_Application_Have_Employees_In_AK_CA_LA_NewMexico_or_vermont().select(policy.busOwnLine.getAdditionalCoverageStuff().isEmploymentPracticesLiabilityInsuranceDoesApplicantHaveEmployees());
                    selectEPLI_If_Applicant_Is_a(policy.busOwnLine.getAdditionalCoverageStuff().getEmploymentPracticesLiabilityInsuranceIfApplicantIsA());
                }
            }


            List<String> buildingClasses = new ArrayList<>();
            List<BuildingClassCode> classCodes = new ArrayList<BuildingClassCode>();
            for (PolicyLocation location : locationList) {
                for (PolicyLocationBuilding building : location.getBuildingList()) {
                    if (!building.getClassCode().equals("") && building.getClassCode() != null) {
                        buildingClasses.add(building.getClassCode());
                    }
                    if (!building.getClassClassification().equals("") && building.getClassClassification() != null) {
                        buildingClasses.add(building.getClassClassification());
                    }
                }
            }

            for (String code : buildingClasses) {
                BuildingClassCode tempCode = BuildingClassCode.valueOfClassCode(code);
                if (tempCode == null) {
                    tempCode = BuildingClassCode.valueOfClassification(code);
                    if (tempCode == null) {
                    	try {
                    		Assert.fail("unable to find enum with class Code/Classification: " + code);
                    	} catch (AssertionError ae) {
                    		
                    	}
                    } else {
                        classCodes.add(tempCode);
                    }
                } else {
                    classCodes.add(tempCode);
                }
            }

            String switchCode;
            for (BuildingClassCode code : classCodes) {
                switchCode = code.getClassCode();
                switch (switchCode) {
                    case "69151":
                        setMotelKitchensRadio(false);
                        setMotelHallwaysWellLit(true);
                        setMotelExerciseFacility(false);
                        boolean defaultSetting = false;

                        if (defaultSetting) {
                            setOffPremisesExposuresRadio(true);
                            setHospitalCheckBox(getRandBoolean());
                            setNursingHomeCheckBox(getRandBoolean());
                            setHotelCheckBox(getRandBoolean());
                            setDepartmentStoreCheckBox(getRandBoolean());
                            setPrivateResidenceCheckBox(getRandBoolean());
                            if (getRandBoolean()) {
                                setOtherCheckBox(true);
                                setOtherDescription("Some Other Description");
                            }
                        } else {
                            setOffPremisesExposuresRadio(false);
                        }

                        setMotelBanquetFacilities(false);
                        setMotelSellAlchoholicProducts(false);
                        break;
                    case "69161":
                    case "69171":
                        setMotelKitchensRadio(false);
                        setMotelHallwaysWellLit(true);
                        setMotelExerciseFacility(false);
                        setMotelExposuresRadio(true);
                        setBarberBeautyCheckbox(getRandBoolean());
                        setConventionCheckbox(getRandBoolean());
                        setChildCareCheckbox(getRandBoolean());
                        if (getRandBoolean()) {
                            setOtherActivitiesCheckbox(true);
                        }
                        setMotelBanquetFacilities(false);
                        setMotelSellAlchoholicProducts(false);
                        setTrashInSteelContainerRadio(true);
                        if (getRandBoolean()) {
                            setLossControlFeaturesRadio(true);
                            setEmergencyLightingCheckBox(getRandBoolean());
                            setFloorLightsCheckBox(getRandBoolean());
                            setNONSlipFloorsCheckBox(getRandBoolean());
                            setFloorMatsCheckBox(getRandBoolean());
                            setPestControlCheckBox(getRandBoolean());
                            setCleaningSuppliesAwayFromFoodCheckBox(getRandBoolean());
                            setFirstAidKitsCheckBox(getRandBoolean());
                            setHeimlichManuverTrainedCheckBox(getRandBoolean());
                        } else {
                            setLossControlFeaturesRadio(false);
                        }
                        setDishesChippedRadio(false);
                        setSturdyFurnitureRadio(true);
                        setCateringServicesRadio(false);
                        setLiquorSalesRadio(false);
                        break;
                    case "71332":
                    case "71952(1)":
                        //tanning beds
                        //spray tanning
                        //beauty school
                        setOperateDaySpayRadio(false);
                        defaultSetting = false;
                        if (defaultSetting) {
                            setOffPremisesExposuresRadio(true);
                            setHospitalCheckBox(getRandBoolean());
                            setNursingHomeCheckBox(getRandBoolean());
                            setHotelCheckBox(getRandBoolean());
                            setDepartmentStoreCheckBox(getRandBoolean());
                            setPrivateResidenceCheckBox(getRandBoolean());
                            if (getRandBoolean()) {
                                setOtherCheckBox(true);
                                setOtherDescription("Some Other Description");
                            }
                        } else {
                            setOffPremisesExposuresRadio(false);
                        }
                        //off premises exposures
                        break;
                    case "71952(2)":
                        //tanning beds
                        //spray tanning
                        break;
                    case "71952(3)":
                        //tanning beds
                        //spray tanning
                        //jon larsen 9/15/2015
                        //MAY HAVE BEEN PUT IN BY MISTAKE :(
//					setOperateDaySpayRadio(false);
//						setOffPremisesExposuresRadio(true);
//						setHospitalCheckBox(getRandBoolean());
//						setNursingHomeCheckBox(getRandBoolean());
//						setHotelCheckBox(getRandBoolean());
//						setDepartmentStoreCheckBox(getRandBoolean());
//						setPrivateResidenceCheckBox(getRandBoolean());
//						if(getRandBoolean()) {
//							setOtherCheckBox(true);
//							setOtherDescription("Some Other Description");
//						}
                        break;
                    case "09011":
                    case "09041":
                    case "09061":
                    case "09081":
                    case "09101":
                    case "09121":
                    case "09141":
                    case "09171":
                    case "09211":
                    case "09231":
                    case "09261":
                    case "09001":
                    case "09021":
                    case "09031":
                    case "09051":
                    case "09071":
                    case "09091":
                    case "09111":
                    case "09131":
                    case "09151":
                    case "09161":
                    case "09181":
                    case "09191":
                    case "09201":
                    case "09221":
                    case "09241":
                    case "09251":
                    case "09611":
                    case "09621":
                    case "09631":
                    case "09641":
                    case "09651":
                    case "09661":
                    case "09421":
                    case "09431":
                    case "09441":
                        
                        setTrashInSteelContainerRadio(true);
                        
                        if (getRandBoolean()) {
                            setLossControlFeaturesRadio(true);
                            setEmergencyLightingCheckBox(getRandBoolean());
                            setFloorLightsCheckBox(getRandBoolean());
                            setNONSlipFloorsCheckBox(getRandBoolean());
                            setFloorMatsCheckBox(getRandBoolean());
                            setPestControlCheckBox(getRandBoolean());
                            setCleaningSuppliesAwayFromFoodCheckBox(getRandBoolean());
                            setFirstAidKitsCheckBox(getRandBoolean());
                            setHeimlichManuverTrainedCheckBox(getRandBoolean());
                        } else {
                            setLossControlFeaturesRadio(false);
                        }
                        waitForPostBack();
                        setDishesChippedRadio(false);
                        waitForPostBack();
                        setSturdyFurnitureRadio(true);
                        waitForPostBack();
                        setCateringServicesRadio(false);
                        waitForPostBack();
                        setLiquorSalesRadio(false);
                        waitForPostBack();
                        if (!quickQuote) {
                        	waitForPostBack();
                            setTrashStoredAwayFromFoodRadio(true);
                            
                            setMajorhealthViolationsRadio(false);
                        }
                        break;
                    case "54136":
                    case "09321":
                        //auto sales
                        //carwash
                        //firearms on premises
                        //shower and sleeping
                        //unpasturized milk
                        //propane filling
                        break;
                    case "09361":
                    case "09351":
                    case "09331":
                    case "09341":
                        //auto sales
                        //carwash
                        //firearms on premises
                        //shower and sleeping
                        //unpasturized milk
                        //propane filling
                        setTrashInSteelContainerRadio(true);
                        if (getRandBoolean()) {
                            setLossControlFeaturesRadio(true);
                            
                            setEmergencyLightingCheckBox(getRandBoolean());
                            
                            setFloorLightsCheckBox(getRandBoolean());
                            
                            setNONSlipFloorsCheckBox(getRandBoolean());
                            
                            setFloorMatsCheckBox(getRandBoolean());
                            
                            setPestControlCheckBox(getRandBoolean());
                            
                            setCleaningSuppliesAwayFromFoodCheckBox(getRandBoolean());
                            
                            setFirstAidKitsCheckBox(getRandBoolean());
                            
                            setHeimlichManuverTrainedCheckBox(getRandBoolean());
                            
                        } else {
                            setLossControlFeaturesRadio(false);
                            
                        }
                        setDishesChippedRadio(false);
                        
                        setSturdyFurnitureRadio(true);
                        
                        setCateringServicesRadio(false);
                        
                        setLiquorSalesRadio(false);
                        
                        break;
                }//END SWITCH
            }//END FOR LOOP


//        } catch (Exception e) {
//            System.out.println("Caught Exception");
//            e.printStackTrace();
//            if (retry) {
//                System.out.println("Retrying Supplemental Page");
//                handleSupplementalQuestions2(locationList, false, quickQuote, policy);
//            } else {
//                e.printStackTrace();
//                Assert.fail(" Failure Exception happend twice on Supplemental Page.");
//            }
//        }


//		catch (StaleElementReferenceException e) {
//			System.out.println("StaleElementReferenceException thrown on Supplemental Page");
//			e.printStackTrace();
//		}
//		catch (NoSuchElementException e) {
//			System.out.println("NoSuchElementException thrown on Supplemental Page");
//			e.printStackTrace();
//		}
//		catch (ElementNotVisibleException e) {
//			System.out.println("ElementNotVisibleException on Supplemental Page.");
//			e.printStackTrace();
//			if(retry) {
//				ErrorHandlingSupplemental errorHandling = ErrorHandlingFactory.getErrorHandlingSupplemental();
//				errorHandling.errorHandlingSupplementalPage(1000);
//			} else {
//				e.printStackTrace();
//				throw new GuidewirePolicyCenterException(Configuration.getTargetServer(), "ElementNotVisibleException happend twice on Supplemental Page.");
//			}
//		}
//		catch (TimeoutException e) {
//			System.out.println("TimeoutException happend on Supplemental Page.");
//			e.printStackTrace();
//			if(retry) {
//				handleSupplementalQuestions2(locationList, false);
//			} else {
//				e.printStackTrace();
//				throw new GuidewirePolicyCenterException(Configuration.getTargetServer(), "TimeoutException happend twice on the Supplemental Page.");
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			throw new GuidewirePolicyCenterException(Configuration.getTargetServer(), "");
//		}
    }


    public void handleSupplementalQuestions() {

        if (finds(By.xpath("//label[contains(text(), 'BOP Barber/Beauty Shop Coverage Information')]")).size() > 0) {
            setOperateDaySpayRadio(false);
            boolean defaultSetting = false;
            if (defaultSetting) {
                setOffPremisesExposuresRadio(true);
                setHospitalCheckBox(getRandBoolean());
                setNursingHomeCheckBox(getRandBoolean());
                setHotelCheckBox(getRandBoolean());
                setDepartmentStoreCheckBox(getRandBoolean());
                setPrivateResidenceCheckBox(getRandBoolean());
                if (getRandBoolean()) {
                    setOtherCheckBox(true);
                    setOtherDescription("Some Other Description");
                }
            } else {
                setOffPremisesExposuresRadio(false);
            }
        }


        if (finds(By.xpath("//label[contains(text(), 'BOP Restaurant Coverage Information')]")).size() > 0) {
            setTrashInSteelContainerRadio(true);
            
            if (getRandBoolean()) {
                setLossControlFeaturesRadio(true);
                
                setEmergencyLightingCheckBox(getRandBoolean());
                
                setFloorLightsCheckBox(getRandBoolean());
                
                setNONSlipFloorsCheckBox(getRandBoolean());
                
                setFloorMatsCheckBox(getRandBoolean());
                
                setPestControlCheckBox(getRandBoolean());
                
                setCleaningSuppliesAwayFromFoodCheckBox(getRandBoolean());
                
                setFirstAidKitsCheckBox(getRandBoolean());
                
                setHeimlichManuverTrainedCheckBox(getRandBoolean());
                
            } else {
                setLossControlFeaturesRadio(false);
            }
            
            setDishesChippedRadio(false);
            
            setSturdyFurnitureRadio(true);
            
            setCateringServicesRadio(false);
            
            setLiquorSalesRadio(false);
            
            try {
                setTrashStoredAwayFromFoodRadio(true);
                
                setMajorhealthViolationsRadio(false);
            } catch (Exception e) {
            }
        }

        if (finds(By.xpath("//label[contains(text(), 'BOP Motel Coverage Information')]")).size() > 0) {
            setMotelKitchensRadio(false);
            setMotelHallwaysWellLit(true);
            setMotelExerciseFacility(false);
            setMotelExposuresRadio(true);
            setBarberBeautyCheckbox(getRandBoolean());
            setConventionCheckbox(getRandBoolean());
            setChildCareCheckbox(getRandBoolean());
            if (getRandBoolean()) {
                setOtherActivitiesCheckbox(true);
            }
            setMotelBanquetFacilities(false);
            setMotelSellAlchoholicProducts(false);

        }

        if (finds(By.xpath("//label[contains(text(), 'BOP Liquor Liability Coverage BP 04 89')]")).size() > 0) {
// 			@existential crisis ecoleman 5/25/18: Why can't we just have smart wait built in yet?
        	
            setWrittenRules(true);
            
            setDancersAthleticEvents(false);
            
            setSponsor2For1Drinks(false);
            
            setHaveDanceFloorsLiveMusic(false);
            
            setLadiesSinglesNight(false);
            
            setNameOnLiquorLicense("Bob Hope");
            
            setLiquorLicenseExpirationDate("01/01/2020");
            
            setTypeOfLiquorLicense(false);
            
            setAnnualGrossReceipts(false);
            
            setClubMotelPackagingStore(false);
            
            setEmployeesRequiedTIPSTraining(true);
            
            setApplicantSponsorOffPremisesEvents(false);
            
            setEmployeeViolations3Years(false);
            
            setHealthDeptViolations(false);
            
            setLiquorLiabilityClaims(false);
            
            setLiquorCoverageCanceled(false);
            
            setLiquorLicenseSuspendedRevoced(false);
            
            setHowLongOperatedBusiness("Not Long at all");
            waitForPostBack();
            
            setExperianceInIndustry("None");
            waitForPostBack();
            
            setCurrentLiquorLiabilityInsurer("Some Guy");
            
        }

        if (finds(By.xpath("//label[contains(text(), 'BOP Liquor Liability BP 04 88')]")).size() > 0) {
            setApplicantSponsorSpecialEvents(false);
            setApplicantHaveSpecialPermit(false);
            setHostLiquorCoverageNotSufficient("Parties");
            setCircumstancesApplicantProvideLiquor("Whenever");
            setHowOftenApplicantProvideLiquor("Weekendly");
        }


    }


    public void setCateringGrossReceipts(String receipts) {
        div_CateringGrossReceipts.click();
        editbox_CateringGrossReceipts.clear();
        editbox_CateringGrossReceipts.sendKeys(receipts);
    }


    public void setLiquorGrossReceipts(String receipts) {
        div_liquorGrossReceipts.click();
        editbox_LiquorGrossReceipts.clear();
        editbox_LiquorGrossReceipts.sendKeys(receipts);
    }


    public void setOtherDescription(String desc) {
        div_OtherDescription.click();
        editbox_OtherDescription.clear();
        editbox_OtherDescription.sendKeys(desc);
    }


    //////////////////////////////
    //LIQUOR LIABILITY BP 04 88 //
    //////////////////////////////
    public void setApplicantSponsorSpecialEvents(Boolean checked) {
        
        setCheckbox("Does applicant sponsor special events where liquor is provided?", checked);
    }

    public void setApplicantHaveSpecialPermit(Boolean checked) {
        
        setCheckbox("Does applicant have a special permit for liquor operations?", checked);
    }

    public void setHostLiquorCoverageNotSufficient(String desc) {
        editbox_HostLiquorCoverageNotSufficient.click();
        textarea_MultiUseTextarea.clear();
        textarea_MultiUseTextarea.sendKeys(desc);
    }

    public void setCircumstancesApplicantProvideLiquor(String desc) {
        editbox_CircumstancesApplicantProvideLiquor.click();
        textarea_MultiUseTextarea.clear();
        textarea_MultiUseTextarea.sendKeys(desc);
    }

    public void setHowOftenApplicantProvideLiquor(String desc) {
        editBox_HowOftenApplicantProvideLiquor.click();
        textarea_MultiUseTextarea.clear();
        textarea_MultiUseTextarea.sendKeys(desc);
    }


    //////////////////////////////////////////////
    //  BOP Liquor Liability Coverage BP 04 89  //
    //////////////////////////////////////////////
    public void setWrittenRules(Boolean yesno) {
        setRadioButton("Does applicant provide written employee rules/guidelines regarding sales to minors, sales to intoxicated persons, arrangements for transportation of intoxicated persons from the premises, etc.?", yesno);
    }

    public void setDancersAthleticEvents(Boolean yesno) {
        setRadioButton("Does applicant have any of the following: Exotic dancers, Athletic contests/events, Firearms on premises? If Yes, check all that apply.", yesno);
    }

    public void setExoticDancersCheckBox(Boolean checked) {
        
        setCheckbox("Exotic dancers", checked);
    }

    public void setAthleticEventsCheckBox(Boolean checked) {
        
        setCheckbox("Athletic contests/events", checked);
    }

    public void setFireamsOnPremisesCheckBox(Boolean checked) {
        
        setCheckbox("Firearms on premises", checked);
    }


    public void setSponsor2For1Drinks(Boolean yesno) {
        setRadioButton("Does applicant sponsor or provide 2 for 1 drinks or free alcoholic drinks?", yesno);
    }

    public void setHaveDanceFloorsLiveMusic(Boolean yesno) {
        setRadioButton("Does applicant have any Dance Floors or Live Music? If yes, check all that apply.", yesno);
    }

    public void setDanceFloorCheckBox(Boolean checked) {
        
        setCheckbox("Dance floor", checked);
    }

    public void setLiveMusicCheckBox(Boolean checked) {
        
        setCheckbox("Live music", checked);
    }

    public void setLadiesSinglesNight(Boolean yesno) {
        setRadioButton("Does applicant sponsor or provide either ladies night or singles nights?", yesno);
    }

    public void setNameOnLiquorLicense(String desc) {
        editBox_LiquorLicense.click();
        input_LiquorLicense.clear();
        input_LiquorLicense.sendKeys(desc);
    }

    public void setLiquorLicenseExpirationDate(String date) {
        editBox_LiquorLicenseExpirationDate.click();
        input_LiquorLicenseExpirationDate.clear();
        input_LiquorLicenseExpirationDate.sendKeys(date);
    }

    public void setTypeOfLiquorLicense(Boolean yesno) {
        setRadioButton("Is the type of liquor license held one of the following: Beer, Wine, Liquor, On site, Off site? Check all that apply.", yesno);
    }

    public void setBeerCheckBox(Boolean checked) {
        
        setCheckbox("Beer", checked);
    }

    public void setWineCheckBox(Boolean checked) {
        
        setCheckbox("Wine", checked);
    }

    public void setLiquorCheckBox(Boolean checked) {
        
        setCheckbox("Liquor", checked);
    }

    public void setOnSiteCheckBox(Boolean checked) {
        
        setCheckbox("On site", checked);
    }

    public void setOffSiteCheckBox(Boolean checked) {
        
        setCheckbox("Off site", checked);
    }

    public void setAnnualGrossReceipts(Boolean yesno) {
        setRadioButton("Are there annual gross receipts for any of the following: Liquor (including beer/wine), Food, Package (retail sales), other?", yesno);
    }

    public void setLiquorReceipts(String date) {
        editBox_LiquorReceipts.click();
        textarea_MultiUseInput.clear();
        textarea_MultiUseInput.sendKeys(date);
    }

    public void setFoodReceipts(String date) {
        editbox_FoodReceipts.click();
        textarea_MultiUseInput.clear();
        textarea_MultiUseInput.sendKeys(date);
    }

    public void setPackageReceipts(String date) {
        editbox_PackageReceipts.click();
        textarea_MultiUseInput.clear();
        textarea_MultiUseInput.sendKeys(date);
    }

    public void setOtherReceipts(String date) {
        editbox_OtherReceipts.click();
        textarea_MultiUseInput.clear();
        textarea_MultiUseInput.sendKeys(date);
    }

    public void setClubMotelPackagingStore(Boolean yesno) {
        setRadioButton("Are premises used as any of the following: Club, Convenience store, Grocery store, Motel/hotel, Package (retail store), Restaurant, Other? Check all that apply.", yesno);
    }

    public void setClubCheckBox(Boolean checked) {
        
        setCheckbox("Club", checked);
    }

    public void setConvenienceStoreCheckBox(Boolean checked) {
        
        setCheckbox("Convenience store", checked);
    }

    public void setGroceryStoreCheckBox(Boolean checked) {
        
        setCheckbox("Grocery store", checked);
    }

    public void setMotelHotelCheckBox(Boolean checked) {
        
        setCheckbox("Motel/hotel", checked);
    }

    public void setPackageStoreCheckBox(Boolean checked) {
        
        setCheckbox("Package (retail store)", checked);
    }

    public void setRestaurantCheckBox(Boolean checked) {
        
        setCheckbox("Stairways have emergency backup lighting", checked);
    }

    public void setOtherUseageCheckBox(Boolean checked) {
        
        setCheckbox("Stairways have emergency backup lighting", checked);
    }

    public void setEmployeesRequiedTIPSTraining(Boolean yesno) {
        setRadioButton("Are all employees required to be certified by TIPS or comparable serving training? See TIPS training at www.gettips.com.", yesno);
    }

    public void setApplicantSponsorOffPremisesEvents(Boolean yesno) {
        setRadioButton("Does applicant sponsor or supply liquor for off premises events?", yesno);
    }

    public void setOffPremisesDescription(String date) {
        editBox_OffPremisesEventsDescription.click();
        textarea_MultiUseTextarea.clear();
        textarea_MultiUseTextarea.sendKeys(date);
    }

    public void setEmployeeViolations3Years(Boolean yesno) {
        setRadioButton("Within the past 3 years, has the applicant and/or any employees been fined or cited for violations of law or ordinance related to illegal activities or the sale of alcohol?", yesno);
    }

    public void setEmployeeViolation(String date) {
        editBox_EmployeeViolationLast3Years.click();
        textarea_MultiUseTextarea.clear();
        textarea_MultiUseTextarea.sendKeys(date);
    }

    public void setHealthDeptViolations(Boolean yesno) {
        setRadioButton("Within the past 3 years, has applicant received any health department violations?", yesno);
    }

    public void setLiquorLiabilityClaims(Boolean yesno) {
        setRadioButton("Within the past 3 years, has the applicant had any liquor liability claims (whether insured or not)?", yesno);
    }

    public void setLiquorLiabilityClaimsDesc(String date) {
        editBox_LiquorLiabilityClaims.click();
        textarea_MultiUseTextarea.clear();
        textarea_MultiUseTextarea.sendKeys(date);
    }

    public void setLiquorCoverageCanceled(Boolean yesno) {
//    	@editor ecoleman 5/21/18 : xpath won't work with certain characters, so a partial match works just fine here
        setRadioButton("liquor coverage been cancelled or non-renewed?", yesno);
    }

    public void setLiquorCoverageCanceledExplaination(String date) {
        editBox_LiquorCoverageCanceledExpaination.click();
        textarea_MultiUseTextarea.clear();
        textarea_MultiUseTextarea.sendKeys(date);
    }

    public void setLiquorLicenseSuspendedRevoced(Boolean yesno) {
        setRadioButton("Has applicant ever had the liquor license suspended or revoked?", yesno);
    }

    public void setLiquorLicenseCanceledRevocedExplaination(String date) {
        editBox_LiquorCoverageCanceledExpaination.click();
        textarea_MultiUseTextarea.clear();
        textarea_MultiUseTextarea.sendKeys(date);
    }

    public void setHowLongOperatedBusiness(String desc) {
    	waitForPostBack();
        editBox_TimeBusinessAtLocation.click();
        textarea_MultiUseInput.clear();
        textarea_MultiUseInput.sendKeys(desc);
    }

    public void setExperianceInIndustry(String desc) {
    	waitForPostBack();
        editBox_ManagementExperianceInIndustry.click();
        textarea_MultiUseTextarea.clear();
        textarea_MultiUseTextarea.sendKeys(desc);
    }

    public void setCurrentLiquorLiabilityInsurer(String desc) {
    	waitForPostBack();
        editBox_LiquorLiabilityInsurer.click();
        
        textarea_MultiUseInput.clear();
        
        textarea_MultiUseInput.sendKeys(desc);
        
    }

    public void setLiquorLiabilityComments(String desc) {
        editBox_LiquorLiabilityComments.click();
        textarea_MultiUseTextarea.clear();
        textarea_MultiUseTextarea.sendKeys(desc);
    }


    public void setEmergencyLightingCheckBox(Boolean checked) {
        
        setCheckbox("Stairways have emergency backup lighting", checked);
    }


    public void setFloorLightsCheckBox(Boolean checked) {
        
        setCheckbox("Steps are lit with floor lights", checked);
    }


    public void setNONSlipFloorsCheckBox(Boolean checked) {
        
        setCheckbox("Non-slip wax is used on hard floor surfaces", checked);
    }


    public void setFloorMatsCheckBox(Boolean checked) {
        
        setCheckbox("Floor mats are present at all entrances", checked);
    }


    public void setPestControlCheckBox(Boolean checked) {
        
        setCheckbox("Pest control contractor is employed", checked);
    }


    public void setCleaningSuppliesAwayFromFoodCheckBox(Boolean checked) {
        
        setCheckbox("Cleaning supplies are stored away from food", checked);
    }


    public void setFirstAidKitsCheckBox(Boolean checked) {
        
        setCheckbox("First aid kit is available", checked);
    }


    public void setHeimlichManuverTrainedCheckBox(Boolean checked) {
        
        setCheckbox("Employees are trained in the Heimlich Maneuver", checked);
    }


    public void setHospitalCheckBox(Boolean checked) {
        
        setCheckbox("Hospital", checked);
    }


    public void setNursingHomeCheckBox(Boolean checked) {
        
        setCheckbox("Nursing home", checked);
    }


    public void setHotelCheckBox(Boolean checked) {
        
        setCheckbox("Hotel", checked);
    }


    public void setDepartmentStoreCheckBox(Boolean checked) {
        
        setCheckbox("Department Store", checked);
    }


    public void setPrivateResidenceCheckBox(Boolean checked) {
        
        setCheckbox("Private residence", checked);
    }


    public void setOtherCheckBox(Boolean checked) {
        
        setCheckbox("Other", checked);
    }


    public void setOperateDaySpayRadio(Boolean yesno) {
        setRadioButton("Does applicant operate a day spa", yesno);
    }


    public void setOffPremisesExposuresRadio(Boolean yesno) {
        setRadioButton("have off premises exposures", yesno);
    }


    public void setTrashInSteelContainerRadio(Boolean yesno) {
        setRadioButton("Is trash stored in a steel covered container", yesno);
    }


    public void setLossControlFeaturesRadio(Boolean yesno) {
        setRadioButton("Do any loss control features apply", yesno);
    }


    public void setDishesChippedRadio(Boolean yesno) {
        setRadioButton("Are dishes chipped", yesno);
    }


    public void setSturdyFurnitureRadio(Boolean yesno) {
        setRadioButton("Is furniture in good repair and sturdy", yesno);
    }


    public void setCateringServicesRadio(Boolean yesno) {
        setRadioButton("Does applicant have catering services", yesno);
    }


    public void setLiquorSalesRadio(Boolean yesno) {
        setRadioButton("Does the applicant have liquor sales", yesno);
    }


    public void setTrashStoredAwayFromFoodRadio(Boolean yesno) {
        setRadioButton("trash stored in a location separate", yesno);
    }


    public void setMajorhealthViolationsRadio(Boolean yesno) {
        setRadioButton("any major health department violations", yesno);
    }


    //Motel Coverage

    public void setMotelKitchensRadio(Boolean yesno) {
        setRadioButton("Does the applicant have kitchen units available to guests?", yesno);
    }


    public void setMotelHallwaysWellLit(Boolean yesno) {
        setRadioButton("Are parking areas, hallways and common areas well lit at night?", yesno);
    }


    public void setMotelExerciseFacility(Boolean yesno) {
        setRadioButton("Does applicant provide banquet facilities for weddings, birthdays or other special events?", yesno);
    }


    public void setMotelExposuresRadio(Boolean yesno) {
        setRadioButton("Does operation have an exercise facility with free weights, sauna or steam room?", yesno);
    }


    public void setMotelBanquetFacilities(Boolean yesno) {
        setRadioButton("Do any of the following exposures exist on the premises include concessionaires or leased operations? If yes, check all that apply.", yesno);
    }


    public void setMotelSellAlchoholicProducts(Boolean yesno) {
        setRadioButton("Does applicant sell alcoholic products?", yesno);
    }


    public void setBarberBeautyCheckbox(Boolean checked) {
        setCheckbox("Barber or beauty shops", checked);
    }


    public void setSpaCheckbox(Boolean checked) {
        setCheckbox("Spa", checked);
    }


    public void setConventionCheckbox(Boolean checked) {
        setCheckbox("Convention or conference centers", checked);
    }


    public void setChildCareCheckbox(Boolean checked) {
        setCheckbox("Child care", checked);
    }


    public void setOtherActivitiesCheckbox(Boolean checked) {
        setCheckbox("Other similar activities", checked);
    }


    public void clickPolicyChangeNext() {
        super.clickPolicyChangeNext();
    }


    public void handleSalonSupplementalQuestions() {

        if (finds(By.xpath("//label[contains(text(), 'BOP Barber/Beauty Shop Coverage Information')]")).size() > 0) {
            setOperateDaySpayRadio(false);
            setOffPremisesExposuresRadio(true);
            setHospitalCheckBox(true);
        }
    }

    public Guidewire8RadioButton radio_EPLI_Does_Application_Have_Employees_In_AK_CA_LA_NewMexico_or_vermont() {
        return new Guidewire8RadioButton(driver, "//div[contains(text(), 'Does applicant/insured have employees in Arkansas, California, Louisiana, New Mexico, or Vermont?')]/parent::td/following-sibling::td/div/table");
    }

    public void setEPLI_Does_Application_Have_Employees_In_AK_CA_LA_NewMexico_or_vermont(boolean yesno) {
        radio_EPLI_Does_Application_Have_Employees_In_AK_CA_LA_NewMexico_or_vermont().select(yesno);
    }


    public void selectEPLI_If_Applicant_Is_a(EmploymentPracticesLiabilityInsuranceIfApplicantIsA applicantIsA) {
        WebElement div = find(By.xpath("//div[contains(text(), 'Indicate if the applicant/insured is a:')]/parent::td/following-sibling::td/div"));
        clickWhenClickable(div);
        WebElement mySelect = find(By.xpath("//input[@name = 'c2']"));
        mySelect.clear();
        mySelect.sendKeys(applicantIsA.getValue());
    }



}




















