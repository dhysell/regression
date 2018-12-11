package regression.r2.noclock.policycenter.change.subgroup8;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
@QuarantineClass
public class TestUmbrellaPolicyTerm extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySquireUmbrellaPol;
	private Underwriters uw;

    @Test()
    public void testIssueUnderLyingSquire() throws Exception {
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

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK,true,
				UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -1);
		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Test"+StringsUtils.generateRandomNumberDigits(8), "Comp",
				AdditionalNamedInsuredType.Spouse, new AddressInfo(true)) {
			{
				this.setNewContact(CreateNew.Create_New_Always);
			}
		});

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySquireUmbrellaPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withPolEffectiveDate(newEff)
				.withPolTermLengthDays(90)
				.withANIList(listOfANIs)
				.withInsFirstLastName("Umbrella", "Terms")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(mySquireUmbrellaPol.agentInfo.getAgentUserName(), mySquireUmbrellaPol.agentInfo.getAgentPassword(), mySquireUmbrellaPol.squireUmbrellaInfo.getPolicyNumber());
        Date expDate = DateUtils.dateAddSubtract(mySquireUmbrellaPol.squireUmbrellaInfo.getExpirationDate(), DateAddSubtractOptions.Day, 4);
		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.changeExpirationDate(expDate, "Testing purpose");
    }

    @Test(dependsOnMethods = {"testIssueUnderLyingSquire"})
    private void testIssueUmbrellaPol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_2000000);

        mySquireUmbrellaPol.squireUmbrellaInfo = squireUmbrellaInfo;
        mySquireUmbrellaPol.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.QuickQuote);

    }

    @Test(dependsOnMethods = {"testIssueUmbrellaPol"})
    private void testValidateUmbrellaDetails() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySquireUmbrellaPol.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.clickEditPolicyTransaction();
		sideMenu.clickSideMenuPolicyInfo();
        getQALogger().info(DateUtils.dateFormatAsString("MM/dd/yyyy", policyInfo.getPolicyInfoExpirationDate()));
        getQALogger().info(policyInfo.getUnderlyingPolicyNumber());
        getQALogger().info(policyInfo.getUnderlyingPolicyJobNumber());
        getQALogger().info(policyInfo.getAdditionalInsuredName(1, "Name"));

        String errorMessage = "";


        if (!DateUtils.dateFormatAsString("MM/dd/YYYY", mySquireUmbrellaPol.squireUmbrellaInfo.getExpirationDate()).equals(policyInfo.getPolicyInfoExpireDate())) {
            errorMessage = "Underlying Squire Expiration Date: " + mySquireUmbrellaPol.squireUmbrellaInfo.getExpirationDate() + ":" + policyInfo.getPolicyInfoExpireDate() + " is not displayed in Umbrella -> Policy Info. \n";
        }
        if (!mySquireUmbrellaPol.squireUmbrellaInfo.getPolicyNumber().contains(policyInfo.getUnderlyingPolicyNumber())) {
            errorMessage = errorMessage + "Underlying Squire Policy Number: " + mySquireUmbrellaPol.squireUmbrellaInfo.getPolicyNumber() + " is not displayed in Umbrella -> Policy Info. \n";
		}

        if (!policyInfo.getUnderlyingPolicyJobNumber().contains(mySquireUmbrellaPol.squireUmbrellaInfo.getPolicyNumber().replace("-", ""))) {
            errorMessage = errorMessage + "Underlying Squire Policy Job Number: " + mySquireUmbrellaPol.squireUmbrellaInfo.getPolicyNumber() + " is not displayed in Umbrella -> Policy Info. \n";
		}
		String aniName = this.mySquireUmbrellaPol.aniList.get(0).getPersonFirstName() + " " + this.mySquireUmbrellaPol.aniList.get(0).getPersonLastName();

        if (!policyInfo.getAdditionalInsuredName(1, "Name").contains(aniName)) {
			errorMessage = errorMessage + "Underlying Squire Policy Additional Named Insured : "+ aniName + " is not displayed in Umbrella -> Policy Info.\n";
		}

        if (errorMessage != "")
			Assert.fail(errorMessage);
    }

}
