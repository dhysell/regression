package repository.pc.workorders.forms;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.Forms_BOP;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;

import java.util.ArrayList;
import java.util.List;

public class BOP_Forms extends BasePage {
	
	private WebDriver driver;
	List<Forms_BOP> formsList = new ArrayList<Forms_BOP>();
	
	public BOP_Forms(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
	
	
	public GeneratePolicy createFormsPolicyObject(List<Forms_BOP> formsList) {
		GeneratePolicy myPolicy = new GeneratePolicy(driver);
		myPolicy.busOwnLine = new PolicyBusinessownersLine();
		
		PolicyLocation location = new PolicyLocation(new AddressInfo(true));
		PolicyLocationBuilding buildng = new PolicyLocationBuilding();
		location.getBuildingList().add(buildng);
		
        myPolicy.busOwnLine.locationList.add(location);
		
		for(Forms_BOP form : formsList) {
			switch (form) {
			case EmploymentPracticesLiabilityInsurance:
			case CommercialEmploymentPracticesLiabilityInsuranceCoverageSupplementalDeclarations:
			case EmploymentPracticesLiabilityWarrantyStatement:
				myPolicy.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance(true);
				break;
			case BusinessownersPolicyDeclarations:
				break;
			case EmploymentPracticesLiabilityInsuranceSupplementalApplication:
				break;
			case OfferToPurchaseExtendedReportingPeriodLetter:
				break;
			}
		}
		return myPolicy;
	}
	
	
	
	
	public BOP_Forms(GeneratePolicy policy, WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        
        if(policy.busOwnLine.getAdditionalCoverageStuff().isEmploymentPracticesLiabilityInsurance()) {
        	formsList.add(Forms_BOP.EmploymentPracticesLiabilityInsurance);
        	formsList.add(Forms_BOP.CommercialEmploymentPracticesLiabilityInsuranceCoverageSupplementalDeclarations);
        	formsList.add(Forms_BOP.EmploymentPracticesLiabilityWarrantyStatement);
        }
    }
	
	
	
	
	
	public List<Forms_BOP> getFormsList() {
        return formsList;
    }
	
	
	
	
	
	
	

}
