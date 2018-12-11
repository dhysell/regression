package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.ARTists;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.rewrite.StartRewrite;

public class DE8132_UnableToStartUmbrellaRewrite extends BaseTest {
	
	
	@Test(enabled=true)
	public void verifyAbleToRewriteUmbrella() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locOnePropertyList.add(locationOneProperty);
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability liabilitySection = new SquireLiability();      

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = liabilitySection;
		myPropertyAndLiability.liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_500000_CSL);

		Squire mySquire = new Squire();
		mySquire.propertyAndLiability = myPropertyAndLiability;

		GeneratePolicy myPolicy = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -10))
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.build(GeneratePolicyType.PolicyIssued);
		
		//ADD UMBRELLA TO THE ACCOUNT
		myPolicy.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);
		
		
		//CANCEL THE SQUIRE POLICY
		myPolicy.productType = ProductLineType.Squire;
		new Login(driver).loginAndSearchPolicy_asUW(myPolicy);
		new StartCancellation(driver).cancelPolicy(CancellationSourceReasonExplanation.Photos, "HAHAHAHA YOU GOT CANCELED. DONT WORRY WE WILL REWRITE YOU LATER", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), true);
		new StartCancellation(driver).clickViewPolicyLink();
		new InfoBar(driver).clickInfoBarAccountNumber();
		
		//REWRITE THE SQUIRE POLICY
		new AccountSummaryPC(driver).clickPolicyTerm_ByProduct(ProductLineType.Squire);
		new StartRewrite(driver).rewriteNewTerm(myPolicy);
		
		//REWRITE UMBRELLA POLICY
		new InfoBar(driver).clickInfoBarAccountNumber();
		myPolicy.productType = ProductLineType.PersonalUmbrella;
		new AccountSummaryPC(driver).clickPolicyTerm_ByProduct(ProductLineType.PersonalUmbrella);
		new StartRewrite(driver).rewriteNewTerm(myPolicy);
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}











