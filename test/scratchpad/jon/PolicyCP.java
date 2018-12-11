package scratchpad.jon;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

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
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP;
import persistence.globaldatarepo.entities.CPClassCodes;
import persistence.globaldatarepo.helpers.CPClassCodesHelper;

public class PolicyCP extends BaseTest {

    boolean testFailed = false;
    String failureString = "";
    public GeneratePolicy myPolicyObj = null;
    private WebDriver driver;

    @SuppressWarnings("serial")
    @Test(enabled = false)
    public void generateCommercialPropertyPolicy() throws Exception {


//		CPPolicy newPolicy = new CPPolicy();
//		newPolicy.generatePolicyBound();


        AddressInfo pniAddress = new AddressInfo(true);
        ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>() {{
            this.add(new PolicyLocation(pniAddress, true));
//			this.add(new PolicyLocation(new AddressInfo(true), true));
//			this.add(new PolicyLocation(new AddressInfo(true), true));
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
                        //SET BUILDING STUFF HERE
                    }});
                }});
            }});
//			this.add(new CPPCommercialPropertyProperty(){{
//				this.setAddress(locationList.get(1).getAddress());
//				this.setCPPCommercialProperty_Building_List(new ArrayList<CPPCommercialProperty_Building>(){{
//					this.add(new CPPCommercialProperty_Building(){{
//						//SET BUILDING STUFF HERE
//					}});
//				}});
//			}});
//			this.add(new CPPCommercialPropertyProperty(){{
//				this.setAddress(locationList.get(2).getAddress());
//				this.setCPPCommercialProperty_Building_List(new ArrayList<CPPCommercialProperty_Building>(){{
//					this.add(new CPPCommercialProperty_Building(){{
//						//SET BUILDING STUFF HERE
//					}});
//				}});
//			}});
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
                .withInsCompanyName("Commercial Property")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(pniAddress)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

    }//END generateCommercialPropertyPolicy()


    @Test//(dependsOnMethods={"generateCommercialPropertyPolicy"})
    public void checkClassCodes() throws Exception {
        boolean testFailed = false;
        String failureString = "";


        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        new Login(driver).loginAndSearchSubmission("hhill", "gw", "257464");
//		loginAndSearchSubmission(myPolicyObj);

        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCPProperty();

        guidewireHelpers.clickWhenClickable(guidewireHelpers.find(By.xpath("//a[contains(text(), 'Edit')]")));

        GenericWorkorderCommercialPropertyPropertyCPP propertyPage = new GenericWorkorderCommercialPropertyPropertyCPP(driver);
        propertyPage.clickDetailsTab();

        guidewireHelpers.clickWhenClickable(guidewireHelpers.find(By.xpath("//div[contains(@id, ':SelectClassCode')]")));

        List<CPClassCodes> classCodesList = CPClassCodesHelper.getAllPropertyClassCodes();
        GenericWorkorderBuildings searhCodes = new GenericWorkorderBuildings(driver);

        for (CPClassCodes code : classCodesList) {
            guidewireHelpers.systemOut("Testing Class Code: " + code.getClassCode() + " | " + code.getDescription());

            searhCodes.setBuildingClassCode(code.getClassCode());
            guidewireHelpers.clickWhenClickable(guidewireHelpers.find(By.xpath("//a[contains(@id, 'SearchLinksInputSet:Search')]")));

            List<WebElement> tempList = guidewireHelpers.finds(By.xpath("//div[contains(@id, ':ClassCodeSearchResultsLV-body')]/div/table/tbody/child::tr"));
            if (tempList.isEmpty()) {
                testFailed = true;
                failureString = failureString + "\nFailed to find Class Code in Search:  " + code.getClassCode() + "\n";
            } else {
                guidewireHelpers.clickWhenClickable(tempList.get(0).findElement(By.xpath(".//child::td[1]/div/a")));
                String description = guidewireHelpers.find(By.xpath("//textarea[contains(@id, ':ClassDescription-inputEl')]")).getText();
                String rateType = guidewireHelpers.find(By.xpath("//input[contains(@id, ':RateType-inputEl')]")).getAttribute("value");

                if (!code.getDescription().replace("�", "-").trim().equals(description.replace("�", "-").trim())) {
                    testFailed = true;
                    guidewireHelpers.systemOut("\nClass Code" + code.getClassCode() + " Description Selected did not match Class Code Searched | \n   Found: " + description + " \nSearched: " + code.getDescription() + "\n\n");
                    failureString = failureString + "\nClass Code " + code.getClassCode() + " Description Selected did not match Class Code Searched | \nFound: " + description + " \nSearched: " + code.getDescription() + "\n\n";
                }

                if (code.getRated().replace("�", "-").equals("-")) {
                    if (!rateType.equals("Tentative")) {
                        testFailed = true;
                        guidewireHelpers.systemOut("\nClass Code" + code.getClassCode() + " Rate Type didn't match required. \nFound " + rateType + " Expected: Tentative\n\n");
                        failureString = failureString + "\nClass Code " + code.getClassCode() + " Rate Type didn't match required. \n   Found " + rateType + " Expected: Tentative\n\n";
                    }
                } else {
                    if (!rateType.equals("Class")) {
                        testFailed = true;
                        guidewireHelpers.systemOut("\nClass Code" + code.getClassCode() + " Rate Type didn't match required. \nFound " + rateType + " Expected: Class\n\n");
                        failureString = failureString + "\nClass Code " + code.getClassCode() + " Rate Type didn't match required. \n   Found " + rateType + " Expected: Class\n\n";
                    }
                }


                guidewireHelpers.clickWhenClickable(guidewireHelpers.find(By.xpath("//div[contains(@id, ':SelectClassCode')]")));

            }


//			if(tempList.isEmpty()) {
//				testFailed = true;
//				failureString = failureString + "\nFailed to find Class Code in Search:  " + code.getClassCode() + "\n";
//			} else {
//				String classCode = tempList.get(0).findElement(By.xpath(".//child::td[2]/div")).getText();
//				String classDescription = tempList.get(0).findElement(By.xpath(".//child::td[3]/div")).getText();
//				
//				if(!classCode.equals(code.getClassCode())) {
//					testFailed = true;
//					systemOut("\nClass Code found did not match Class Code Searched: \nFound:" + classCode + " \nSearched: " + code.getClassCode()) + "\n\n";
//					failureString = failureString + "\nClass Code found did not match Class Code Searched: \nFound:" + classCode + " \nSearched: " + code.getClassCode() + "\n\n";
//				}
//				
//				if(!classDescription.replace("�", "-").trim().equals(code.getDescription().replace("�", "-").trim())) {
//					testFailed = true;
//					systemOut("\nClass Code: " + code.getClassCode() + " found Classification did not match Search:  \nFound" + classDescription + " \nSearched: " + code.getDescription() + "\n\n");
//					failureString = failureString + "\nClass Code: " + code.getClassCode() + " found Classification did not match Search:  \nFound" + classDescription + " \nSearched: " + code.getDescription() + "\n\n";
//				}
//			}
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + "256731" + failureString);
        }


    }


}
















