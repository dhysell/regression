package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.Achievers;

import static org.testng.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CancellationObject;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.ClockUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.ChargeObject;
import repository.pc.policy.PolicyChargesToBC;
import repository.pc.workorders.cancellation.StartCancellation;
import scratchpad.evan.SideMenuPC;

public class DE8091_IncorrectWaxingOfPremiumsOnCancelation extends BaseTest {
	
	
	@Test(enabled=false)
    public void incorrectWaxingOfPremiumsOnCancelation() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		
		PolicyLocation location1 = new PolicyLocation(new AddressInfo(true));
		
		AdditionalInterest ai1 = new AdditionalInterest(ContactSubType.Company);
		ai1.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		
		PolicyLocationBuilding building1 = new PolicyLocationBuilding();
		building1.getAdditionalInterestList().add(ai1);
		
		location1.getBuildingList().add(building1);
		
		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine();
		boLine.locationList.add(location1);
		boLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance(false);
		

		GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
				.withAdvancedSearch()
				.withBusinessownersLine(boLine)
				.build(GeneratePolicyType.PolicyIssued);
		
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 13);
		
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
		CancellationObject transactionNumber = new StartCancellation(driver).cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "MY AGENT WAS MEAN TO ME.", null, true);
		new StartCancellation(driver).clickViewPolicyLink();
		
		new SideMenuPC(driver).clickSideMenuChargesToBC();
		List<ChargeObject> chargeObject_Submission = new PolicyChargesToBC(driver).getChargesToBC("001");
		List<ChargeObject> chargeObject_Cancelation = new PolicyChargesToBC(driver).getChargesToBC(transactionNumber.getTransactionNumber());
		
		double submissionCharge = -0.0;
		double cancelationCharge = -0.0;
		for(ChargeObject charge : chargeObject_Submission) {
			if(charge.getLinePayer().contains(ai1.getLienholderNumber())) {
				submissionCharge = charge.getLineAmount();
			}
		}
		for(ChargeObject charge : chargeObject_Cancelation) {
			if(charge.getLinePayer().contains(ai1.getLienholderNumber())) {
				cancelationCharge = charge.getLineAmount();
			}
		}
		
		assertTrue(Math.abs(submissionCharge) == Math.abs(cancelationCharge), "AFTER CANCELATION, THE WAX-OFF AMOUNT(" + cancelationCharge + ") DID NOT MATCH AMOUNT CHARGED AT SUBMISSION(" + submissionCharge + "). THIS IS BAD!!!!");
	}
	
	
	@Test(enabled=false)
    public void incorrectWaxingOfPremiumsOnCancelation_Scenario2() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		
		PolicyLocation location1 = new PolicyLocation(new AddressInfo(true));
		
		AdditionalInterest ai1 = new AdditionalInterest(ContactSubType.Company);
		ai1.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		ai1.setFirstMortgage(true);
		
		AdditionalInterest ai2 = new AdditionalInterest(ContactSubType.Company);
		ai2.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Insured);
		ai2.setFirstMortgage(false);
		
		PolicyLocationBuilding building1 = new PolicyLocationBuilding();
		building1.getAdditionalInterestList().add(ai1);
		building1.getAdditionalInterestList().add(ai2);
		
		location1.getBuildingList().add(building1);
		
		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine();
		boLine.locationList.add(location1);
		boLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance(false);
		

		GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
				.withAdvancedSearch()
				.withBusinessownersLine(boLine)
				.build(GeneratePolicyType.PolicyIssued);
		
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 13);
		
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
		CancellationObject transactionNumber = new StartCancellation(driver).cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "MY AGENT WAS MEAN TO ME.", null, true);
		new StartCancellation(driver).clickViewPolicyLink();
		
		new SideMenuPC(driver).clickSideMenuChargesToBC();
		List<ChargeObject> chargeObject_Submission = new PolicyChargesToBC(driver).getChargesToBC("001");
		List<ChargeObject> chargeObject_Cancelation = new PolicyChargesToBC(driver).getChargesToBC(transactionNumber.getTransactionNumber());
		
		double submissionCharge = -0.0;
		double cancelationCharge = -0.0;
		for(ChargeObject charge : chargeObject_Submission) {
			if(charge.getLinePayer().contains(ai1.getLienholderNumber())) {
				submissionCharge = charge.getLineAmount();
			}
		}
		for(ChargeObject charge : chargeObject_Cancelation) {
			if(charge.getLinePayer().contains(ai1.getLienholderNumber())) {
				cancelationCharge = charge.getLineAmount();
			}
		}
		
		assertTrue(Math.abs(submissionCharge) == Math.abs(cancelationCharge), "AFTER CANCELATION, THE WAX-OFF AMOUNT(" + Math.abs(cancelationCharge) + ") DID NOT MATCH AMOUNT CHARGED AT SUBMISSION(" + Math.abs(submissionCharge) + "). THIS IS BAD!!!!");
	}
}















