package currentProgramIncrement.maintenanceDefects.Nucleus;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;

public class DE8110_RelatedContactsEditableOnInForcePolicies extends BaseTest{
		
		@Test
		public void ensureRelatedContactsAreNotEditableOnInForcePolicies() throws Exception {
			Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		    WebDriver driver = buildDriver(cf);
		    
		    PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Wild", "Bear", AdditionalNamedInsuredType.Spouse, new AddressInfo(true));
		    ani.setNewContact(CreateNew.Create_New_Always);
		    ArrayList<PolicyInfoAdditionalNamedInsured> aniList = new ArrayList<>();
		    aniList.add(ani);  
			
			ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
			ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();	
			
			PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();
			loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);
		
			SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
			myPropertyAndLiability.locationList = locationsList;
		
			Squire mySquire = new Squire(SquireEligibility.City);
			mySquire.propertyAndLiability = myPropertyAndLiability;
			
			locOnePropertyList.add(loc1Bldg1);
			locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));		
			
			GeneratePolicy myPolicy = new GeneratePolicy.Builder(driver)
					.withCreateNew(CreateNew.Create_New_Always)
					.withInsPersonOrCompany(ContactSubType.Person)
					.withANIList(aniList)
					.withInsFirstLastName("Mr", "Pni")
					.withSquire(mySquire)
					.withProductType(ProductLineType.Squire)
					.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withPolOrgType(OrganizationType.Individual)
					.withPolicyLocations(locationsList)
					.withPaymentPlanType(PaymentPlanType.Annual)
					.withDownPaymentType(PaymentType.Cash)
					.build(GeneratePolicyType.PolicyIssued);	
			
			
			Login login = new Login(driver);
			login.loginAndSearchPolicyByAccountNumber(myPolicy);
			
			SideMenuPC sideMenu = new SideMenuPC(driver);
			sideMenu.clickSideMenuPolicyInfo();
			
			GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
			policyInfoPage.clickANIContact(myPolicy.aniList.get(0).getPersonFirstName());
			
			GenericWorkorderAdditionalNamedInsured aniPage = new GenericWorkorderAdditionalNamedInsured(driver);
			Assert.assertFalse(aniPage.ensureRelatedContactIsUnEditable(), "The Change Related To button is available, this means that contact looks editable. Consider reopening the defect.");
		}
}
