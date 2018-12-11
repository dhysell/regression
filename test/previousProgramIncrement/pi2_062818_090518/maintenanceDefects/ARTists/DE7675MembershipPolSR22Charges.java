package previousProgramIncrement.pi2_062818_090518.maintenanceDefects.ARTists;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SR22FilingFee;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;

/**
 * @Author nvadlamudi
 * @Requirement :DE7675: Quote Screen is including SR22 charge in Total Net
 *              Premium
 * @RequirementsLink <a href=
 *                   "https://rally1.rallydev.com/#/203558458764/detail/defect/236655080472">
 *                   </a>
 * @Description : Validate The Total Net Premium should only include premium and
 *              any charges but not SR22 charge
 * @DATE Jul 12, 2018
 */

// Note: this issue exists only in membership branches
public class DE7675MembershipPolSR22Charges extends BaseTest {
	private GeneratePolicy mySqPolObj;

	@Test
	public void testSR22ChargesInQuoteNetPremium() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh,
				MedicalLimit.TenK);
		
		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact person = new Contact("Test" + StringsUtils.generateRandomNumberDigits(6), "Auto", Gender.Male,
				DateUtils.convertStringtoDate("01/01/1979", "MM/dd/YYYY"));
		person.setMaritalStatus(MaritalStatus.Married);
		person.setRelationToInsured(RelationshipToInsured.Insured);
		person.setOccupation("Software");
		driversList.add(person);
		
		
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setDriversList(driversList);

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = squirePersonalAuto;

		mySqPolObj = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.Membership)
				.withInsPersonOrCompany(ContactSubType.Person).withInsFirstLastName("SR22", "Charges")
				.withPolOrgType(OrganizationType.Individual).build(GeneratePolicyType.FullApp);

		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);
		
		new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				mySqPolObj.accountNumber);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuQuote();
		GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		double beforeSR22 = quote.getQuoteTotalNetPremium();
		double sr22Value = quote.getQuoteSR22Charge();		
		new GuidewireHelpers(driver).editPolicyTransaction();
		sideMenu.clickSideMenuPADrivers();
		GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.clickEditButtonInDriverTable(1);
        paDrivers.setSR22Checkbox(true);
		paDrivers.setSR22FilingFee(SR22FilingFee.Charged);
        paDrivers.setSR22EffectiveDate(mySqPolObj.squire.getEffectiveDate());
		paDrivers.clickOk();
		quote.clickGenericWorkorderQuote();
		
		Assert.assertTrue(beforeSR22==quote.getQuoteTotalNetPremium(), "Quote - total net premium amount before and after SR22 change  not matching...");
		
		Assert.assertTrue(sr22Value < quote.getQuoteSR22Charge(), "Quote - SR22 charges are not added for adding new SR22 charges.");
	
		Assert.assertTrue(quote.getQuoteTotalGrossPremium() - quote.getQuoteTotalDiscountsSurcharges() == quote.getQuoteTotalNetPremium(), "Quote Net Premium amount calculation and Gross Premium amounts are not calculated properly.");
		
		
	}
}
