// On 2 Nov. '15 this test only works on Trunk
package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.BusinessownersLine.LiabilityLimits;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationAdditionalInsured;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;

/**
 * @Author bmartin
 * @Requirement
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Nov 2, 2015
 */
@QuarantineClass
public class TestCheckForMultipleCountryInputs extends BaseTest {

    GeneratePolicy myPolObj = null;
    String userName = "";
    String password = "";
    String accountNumber = "";
    String policyNumber = "";
    String dateString = DateUtils.dateFormatAsString("yyyyMMddHHmmssSS", new Date());

    private WebDriver driver;

    @SuppressWarnings("serial")
    @Test
    public void createPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        final PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding() {{
            this.setClassClassification("storage");
            this.setAdditionalInterestList(new ArrayList<AdditionalInterest>() {{
                this.add(new AdditionalInterest(ContactSubType.Company) {{
                    this.setAdditionalInterestType(AdditionalInterestType.Additional_Insured_Building_Owner);
                }});
            }});
        }};

        locOneBuildingList.add(loc1Bldg1);

        locOneBuildingList.add(new PolicyLocationBuilding());

        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList) {{
            this.setAdditionalInsuredLocationsList(new ArrayList<PolicyLocationAdditionalInsured>() {{
                this.add(new PolicyLocationAdditionalInsured(ContactSubType.Person, "Tony", "Stark", AdditionalInsuredRole.ManagersOrLessorsOrPremises, new AddressInfo(true)) {{
                    this.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
                }});
            }});
        }});

        // BUSINESS OWNERS LINE
        PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(smallBusiness.get(NumberUtils.generateRandomNumberInt(0, (smallBusiness.size() - 1)))) {{
            this.setLiabilityLimits(liabilityLimits.get(NumberUtils.generateRandomNumberInt(0, (liabilityLimits.size() - 1))));
            this.setAdditionalCoverageStuff(new PolicyBusinessownersLineAdditionalCoverages(false, true) {{
            }}); // END ADDITIONAL COVERAGES

            this.setAdditonalInsuredBOLineList(new ArrayList<PolicyBusinessownersLineAdditionalInsured>() {{
                this.add(new PolicyBusinessownersLineAdditionalInsured(ContactSubType.Person, "Thomas", "Wayne", AdditionalInsuredRole.Vendors, new AddressInfo(true)) {{
                    this.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
                }});
            }}); // END BUSINESS OWNERS LINE ADDITIONAL INSURED LIST
        }}; // END BUSINESS OWNERS LINE

        ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
        listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Additional", "Type",
                AdditionalNamedInsuredType.Spouse, new AddressInfo(true)) {
            {
                this.setNewContact(CreateNew.Create_New_Always);
            }
        });

        this.myPolObj = new GeneratePolicy.Builder(driver)
                .withBusinessownersLine(boLine)
                .withInsPersonOrCompanyDependingOnDay("Country", "Check", "Country Check")
                .withANIList(listOfANIs)
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        userName = myPolObj.agentInfo.getAgentUserName();
        password = myPolObj.agentInfo.getAgentPassword();
        accountNumber = myPolObj.accountNumber;

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("Agent: " + userName + "\n#############");
    }

    /**
     * @throws Exception
     * @Author bmartin
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description This test checks to see if country shows up more than once
     * on the ANI and AI On 2 Nov. '15 this test only works on
     * Trunk
     * @DATE Nov 2, 2015
     */
    @Test(dependsOnMethods = {"createPolicy"})
    public void CheckCoutryInput() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);
        login.loginAndSearchSubmission(userName, password, accountNumber);

        guidewireHelpers.editPolicyTransaction();

        GenericWorkorderPolicyInfo myPolicy = new GenericWorkorderPolicyInfo(driver);
        myPolicy.clickANI();

        GenericWorkorder myGenWork = new GenericWorkorder(driver);
        myGenWork.checkAdditionalCountry();
        //click cancel
        myGenWork.clickGenericWorkorderCancel();

        SideMenuPC mySide = new SideMenuPC(driver);
        mySide.clickSideMenuBusinessownersLine();


        myGenWork = new GenericWorkorder(driver);
        myGenWork.clickAdditionalInsuredName();
        myGenWork.checkAdditionalCountry();

        //click cancel
        myGenWork.clickGenericWorkorderCancel();

        mySide = new SideMenuPC(driver);
        mySide.clickSideMenuLocations();

        GenericWorkorderLocations location = new GenericWorkorderLocations(driver);
        location.clickPrimaryLocationEdit();

        myGenWork = new GenericWorkorder(driver);
        myGenWork.clickAdditionalInsuredName();
        myGenWork.checkAdditionalCountry();

        myGenWork.clickGenericWorkorderCancel();
        myGenWork.clickGenericWorkorderCancel();
        guidewireHelpers.selectOKOrCancelFromPopup(OkCancel.OK);

        mySide = new SideMenuPC(driver);
        mySide.clickSideMenuBuildings();

        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
        buildings.clickBuildingsBuildingEdit(1);

        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.clickBuildingsPropertyAdditionalInterestsLink(myPolObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNameFromPolicyCenter());

        myGenWork = new GenericWorkorder(driver);
        //		myGenWork.clickAdditionalInsuredName();
        myGenWork.checkAdditionalCountry();
    }

    private List<SmallBusinessType> smallBusiness = new ArrayList<SmallBusinessType>() {
        private static final long serialVersionUID = 1L;

        {
            this.add(SmallBusinessType.Apartments);
            this.add(SmallBusinessType.Offices);
            this.add(SmallBusinessType.Condominium);
            this.add(SmallBusinessType.SelfStorageFacilities);
        }
    };

    private List<LiabilityLimits> liabilityLimits = new ArrayList<LiabilityLimits>() {
        private static final long serialVersionUID = 1L;

        {
            this.add(LiabilityLimits.Three00_600_600);
            this.add(LiabilityLimits.Five00_1000_1000);
            this.add(LiabilityLimits.One000_2000_2000);
            this.add(LiabilityLimits.Two000_4000_4000);
        }
    };
}
