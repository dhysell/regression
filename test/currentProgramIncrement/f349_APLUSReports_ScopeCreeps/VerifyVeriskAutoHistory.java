package currentProgramIncrement.f349_APLUSReports_ScopeCreeps;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import repository.driverConfiguration.Config;
import repository.gw.administration.AdminScriptParameters;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ScriptParameter;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.UIAutoClaimReported;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.generic.GenericWorkorderSquireCLUEAuto;
import persistence.globaldatarepo.entities.APlusAuto;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.APlusAutoHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author swathiAkarapu
 * @Requirement US16973
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/266263983400">US16973</a>
 * @Description Steps to get there:
 * Create policy with auto and/or property
 * Order reports (with verisk toggled on)
 * Acceptance criteria:
 * Ensure that Verisk results return the data we need, and that we get the history we expect to see when a report is ordered for auto and property.
 * Ensure that the policy file screen reflects the screens, based on the script parameters (toggle)
 * Ensure that the UW issues for Verisk are working as expected, per the Product Model.
 * @DATE October 27, 2018
 */
@Test(groups = {"ClockMove"}) //This is not technically a clock move test, but it will mess with script parameters, so it needs to run in series.
public class VerifyVeriskAutoHistory extends BaseTest {
    private GeneratePolicy myPolicyObjPL;
    private WebDriver driver;
    private List<APlusAuto> testdata;

    @Test(enabled = false) // Due to limited SSNs  (testdata ) ,  this Test case is failing as SSns are already present in Guidewire Database
    public void testVeriskAuto() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        AdminScriptParameters scriptParameters = new AdminScriptParameters(driver);
        try {
            //Need to set Verisk feature Enabled Script Parameter to true
          new Login(driver).login("su", "gw");
            new TopMenuAdministrationPC(driver).clickScriptParameters();
            scriptParameters.editScriptParameter(ScriptParameter.VeriskFeatureToggle, true);
            new GuidewireHelpers(driver).logout();

            generateWithVeriskData();

            Underwriters uw = UnderwritersHelper.getRandomUnderwriter();
            new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

            AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
            pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
            SideMenuPC pcSideMenu = new SideMenuPC(driver);
            pcSideMenu.clickSideMenuAutoHistory();
            GenericWorkorderSquireCLUEAuto autoHiostory = new GenericWorkorderSquireCLUEAuto(driver);
            autoHiostory.clickOrderAutoHistory();
            ArrayList<UIAutoClaimReported> uiReport = autoHiostory.getAutoHistoryClaimsReported();
            SoftAssert softAssert = new SoftAssert();
            for (APlusAuto testDataRow : testdata) {


                boolean valueFound = false;

                for (UIAutoClaimReported uiRow : uiReport) {
                    if (datesEquals(testDataRow.getColumn13(), uiRow.getClaimDate())

                            && testDataRow.getColumn12().contains(uiRow.getClaimNumber())) {
                        valueFound = true;
                        break;
                    } else {
                        continue;
                    }
                }
                softAssert.assertTrue(valueFound, "Property Report  for " + testDataRow.getColumn12() + " is not displayed on the UI");
            }
            softAssert.assertAll();
        }catch (Exception e) {
            throw e;
        } finally {
            //switch Verisk feature enabled back to false
            try {
                new GuidewireHelpers(driver).logout();
            } catch (Exception e) {
                //Already logged out.
            }
            new Login(driver).login("su", "gw");
            new TopMenuAdministrationPC(driver).clickScriptParameters();
            scriptParameters.editScriptParameter(ScriptParameter.VeriskFeatureToggle, false);
             new GuidewireHelpers(driver).logout();
            //End Script Parameter Setup
        }
    }


    private void generateWithVeriskData() throws Exception {

        Contact veriskContact = new Contact();
        veriskContact.isVeriskTestData = true;
        veriskContact.setPersonOrCompany(repository.gw.enums.ContactSubType.Person);
        testdata = APlusAutoHelper.getAPlusAutoTestCase();
        APlusAuto aPlusAuto = testdata.stream().filter(a->StringUtils.isNotBlank(a.getColumn41())).findFirst().get();

        veriskContact.setFirstName(aPlusAuto.getColumn37());
        veriskContact.setLastName(aPlusAuto.getColumn36());

        if (StringUtils.isNotBlank(aPlusAuto.getColumn38())) {
            veriskContact.setMiddleName(aPlusAuto.getColumn38());
        }

        if (StringUtils.isNotBlank(aPlusAuto.getColumn39())) {
            veriskContact.setDob(driver, getDate(aPlusAuto.getColumn39(), "MMddyyyy"));
        }

        if ("F".equalsIgnoreCase(aPlusAuto.getColumn40())) {
            veriskContact.setGender(Gender.Female);
        }
        if ("F".equalsIgnoreCase(aPlusAuto.getColumn40())) {
            veriskContact.setGender(Gender.Male);
        }

        if (StringUtils.isNotBlank(aPlusAuto.getColumn41())) {
            veriskContact.setSocialSecurityNumber(aPlusAuto.getColumn41());
        } else {
            veriskContact.setSocialSecurityNumber(StringsUtils.getValidSSN());
        }
        if (StringUtils.isNotBlank(aPlusAuto.getColumn42())) {
            veriskContact.setTaxIDNumber(aPlusAuto.getColumn42());
        }
        veriskContact.setAlternateID("");


        veriskContact.setStateLicenced(State.Idaho);


        if (StringUtils.isNotBlank(aPlusAuto.getColumn43())) {
            veriskContact.setDriversLicenseNum(aPlusAuto.getColumn43());
        } else {
            veriskContact.setDriversLicenseNum("GA136510F");
        }

        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setLine1(aPlusAuto.getColumn46());
        addressInfo.setCity(aPlusAuto.getColumn48());
        addressInfo.setState(State.valueOfAbbreviation(aPlusAuto.getColumn49()));
        addressInfo.setZip(aPlusAuto.getColumn50());
        addressInfo.setCounty(" ");

        List<AddressInfo> addressList = new ArrayList<>();
        addressList.add(addressInfo);
        veriskContact.setAddressList(addressList);
        repository.gw.generate.custom.Vehicle myVehicle = new repository.gw.generate.custom.Vehicle();
        Squire mySquire = new Squire();
        if (StringUtils.isNotBlank(aPlusAuto.getColumn124())) {

            myVehicle.setVehicleTypePL(repository.gw.enums.Vehicle.VehicleTypePL.PrivatePassenger);
            myVehicle.setVin(aPlusAuto.getColumn124());
            myVehicle.setModelYear(Integer.valueOf(aPlusAuto.getColumn116()));
            myVehicle.setMake(aPlusAuto.getColumn118());
            myVehicle.setModel(aPlusAuto.getColumn120());
        }
        ArrayList<repository.gw.generate.custom.Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(myVehicle);
        mySquire.squirePA.setVehicleList(vehicleList);
        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withVeriskData(true)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withContact(veriskContact)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.FullApp);
    }

    private Date getDate(String dateString, String mMddyyyy) {
        SimpleDateFormat formatter = new SimpleDateFormat(mMddyyyy);
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private boolean datesEquals(String date1, String date2) {
        return getDate(date1, "MMddyyyy").equals(getDate(date2, "yyyy-MM-dd"));
    }
}
