package currentProgramIncrement.f349_APLUSReports_ScopeCreeps;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.State;
import repository.driverConfiguration.Config;
import repository.gw.administration.AdminScriptParameters;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ScriptParameter;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
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
import repository.pc.workorders.generic.GenericWorkorderSquireCLUEProperty;
import persistence.globaldatarepo.entities.FarmBureauPropertyMaster0917;
import persistence.globaldatarepo.entities.Underwriters;
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
@Test(groups = {"ClockMove"})
//This is not technically a clock move test, but it will mess with script parameters, so it needs to run in series.
public class VerifyVeriskPropertyHistory extends BaseTest {
    private GeneratePolicy myPolicyObjPL;
    private WebDriver driver;
    private List<FarmBureauPropertyMaster0917> fbpropertyHistoryList;

    @Test
    public void testVeriskProperty() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        AdminScriptParameters scriptParameters = new AdminScriptParameters(driver);
        try {
            //Need to set Verisk feature Enabled Script Parameter to true
          new Login(driver).login("su", "gw");
            new TopMenuAdministrationPC(driver).clickScriptParameters();
            scriptParameters.editScriptParameter(ScriptParameter.VeriskFeatureToggleProperty, true);
             new GuidewireHelpers(driver).logout();
            generateWithVeriskData();
            Underwriters uw = UnderwritersHelper.getRandomUnderwriter();
            //299971
            new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
            AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
            pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
            SideMenuPC pcSideMenu = new SideMenuPC(driver);
            pcSideMenu.clickSideMenuSquirePropertyHistory();
            GenericWorkorderSquireCLUEProperty propHistory = new GenericWorkorderSquireCLUEProperty(driver);
            propHistory.clickOrderPropertyHostory();


            ArrayList<repository.gw.generate.custom.UIPropertyClaimReported> uiData = propHistory.getPropertyHistoryClaimsReported();
            SoftAssert softAssert = new SoftAssert();

            for (FarmBureauPropertyMaster0917 testData : fbpropertyHistoryList) {

                double lossAmountTestdata = Double.valueOf(testData.getClaimamount()) / 100;

                boolean valueFound = false;

                for (repository.gw.generate.custom.UIPropertyClaimReported currentProp : uiData) {
                    double uiLossAmmount = Double.valueOf(currentProp.getLossAmmount());
                    if (datesEquals(testData.getLossaccidentdate(), currentProp.getClaimDate())
                            && currentProp.getPropertyPolicyNumber().contains(testData.getPolicynumber())
                            && currentProp.getClaimNumber().contains(testData.getCasefilenumberclaim())
                            && currentProp.getCarrierName().contains("ISO TEST DATA")
                            && lossAmountTestdata == uiLossAmmount
                            && datesEquals(testData.getLossaccidentdate(), currentProp.getLossDate())
                            && testData.getPolicytype().equalsIgnoreCase(currentProp.getPolicyTypeCode())) {
                        boolean matchfound = false;
                        for (repository.gw.generate.custom.UIClaimReportedPayoutReserve lossRow : currentProp.getPayoutsReserves()) {

                            if ( StringUtils.trim(testData.getClaimtype()).contains(lossRow.getClaimType())
                                    && lossRow.getStatus().equals(testData.getClaimstatus())) {
                                matchfound = true;

                                break;
                            }

                        }
                        if (matchfound) {
                            valueFound = true;
                            break;
                        }
                    } else {
                        continue;
                    }
                }
                softAssert.assertTrue(valueFound, "Property Report  for " + testData.getCasefilenumberclaim() + "" + testData.getPolicynumber() + " is not displayed on the UI");
            }
            softAssert.assertAll();
        } catch (Exception e) {
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
            scriptParameters.editScriptParameter(ScriptParameter.VeriskFeatureToggleProperty, false);
            new GuidewireHelpers(driver).logout();
            //End Script Parameter Setup
        }
    }


    private void generateWithVeriskData() throws Exception {

        repository.gw.generate.custom.Contact veriskContact = new repository.gw.generate.custom.Contact();
        veriskContact.setPersonOrCompany(repository.gw.enums.ContactSubType.Person);
        fbpropertyHistoryList = FarmBureauPropertyMaster0917.getAPlusAutoTestCase();
        FarmBureauPropertyMaster0917 fbp = fbpropertyHistoryList.get(0);
        veriskContact.setFirstName(fbp.getInsuredfirstname());
        veriskContact.setLastName(fbp.getInsuredlastname());
        veriskContact.setDob(driver, getDate(fbp.getInsureddob(), "yyyyMMdd"));
        if (StringUtils.isNoneBlank(fbp.getInsuredssn())) {
            veriskContact.setSocialSecurityNumber(fbp.getInsuredssn());
        } else {
            veriskContact.setSocialSecurityNumber(StringsUtils.getValidSSN());
        }

        veriskContact.setAlternateID("");

        if (!veriskContact.getStateLicenced().equals(State.Idaho)) {
            veriskContact.setStateLicenced(State.Idaho);
            veriskContact.setDriversLicenseNum("GA136510F");
        }

        if (veriskContact.getDriversLicenseNum() == null) {
            veriskContact.setDriversLicenseNum("GA136510F");
            veriskContact.setStateLicenced(State.Idaho);
        } else {
            veriskContact.setStateLicenced(State.valueOfAbbreviation(fbp.getInsuredsstate()));
        }

        repository.gw.generate.custom.AddressInfo addressInfo = new repository.gw.generate.custom.AddressInfo();

        addressInfo.setLine1(fbp.getInsuredsstreetnumber() + " " + fbp.getInsuredsstreetname() + " " + fbp.getInsuredsstreettype());
        addressInfo.setCity(fbp.getInsuredscity());
        addressInfo.setState(State.valueOfAbbreviation(fbp.getInsuredsstate()));
        addressInfo.setZip(fbp.getInsuredszipcode());
        addressInfo.setCounty(" ");

        List<repository.gw.generate.custom.AddressInfo> addressList = new ArrayList<>();
        addressList.add(addressInfo);
        veriskContact.setAddressList(addressList);


        ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
        ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> locationPropertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();
        locationPropertyList.add(new repository.gw.generate.custom.PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.ResidencePremises));
        repository.gw.generate.custom.PolicyLocation propertyLocation = new repository.gw.generate.custom.PolicyLocation(locationPropertyList);
        repository.gw.generate.custom.AddressInfo locationAddress = new AddressInfo();

        locationAddress.setLine1(fbp.getLossoraccidentlocationstreetnumber() + " " + fbp.getLossoraccidentlocationstreetname() + " " + fbp.getLossoraccidentlocationstreettype());
        locationAddress.setCity(fbp.getLossoraccidentlocationcity());
        locationAddress.setState(State.valueOfAbbreviation(fbp.getLossoraccidentlocationstate()));
        locationAddress.setZip(fbp.getLossoraccidentlocationzipcode());

        propertyLocation.setAddress(locationAddress);
        locationsList.add(propertyLocation);


        repository.gw.generate.custom.SquireLiability myLiab = new repository.gw.generate.custom.SquireLiability();


        repository.gw.generate.custom.SquirePropertyAndLiability myPropertyAndLiability = new repository.gw.generate.custom.SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;


        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withVeriskData(true)
                .withSquire(mySquire)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withContact(veriskContact)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.FullApp);
    }


    private Date getDate(String dateString, String yyyyMMdd) {
        SimpleDateFormat formatter = new SimpleDateFormat(yyyyMMdd);
        Date date = null;
        try {

            date = formatter.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    private boolean datesEquals(String date1, String date2) {
        return getDate(date1, "yyyyMMdd").equals(getDate(date2, "yyyy-MM-dd"));
    }
}
