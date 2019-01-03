package repository.pc.workorders.generic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import repository.gw.generate.GeneratePolicy;

public class GenericWorkorderSquirePropertyAndLiability extends GenericWorkorder {

    private WebDriver driver;

    public GenericWorkorderSquirePropertyAndLiability(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void fillOutPropertyAndLiability(GeneratePolicy policy) throws Exception {
        fillOutPropertyAndLiability_Locations(policy);
        fillOutPropertyAndLiability_PropertyDetails(policy);
        fillOutPropertyAndLiability_Coverages(policy);
        fillOutPropertyAndLiability_CLUEProperty(policy);
        fillOutPropertyAndLiability_LineReview(policy);

        repository.pc.workorders.generic.GenericWorkorderPayerAssignment payerassignmentPage = new GenericWorkorderPayerAssignment(driver);
        payerassignmentPage.fillOutPayerAssignmentPage(policy);
    }


    public void fillOutPropertyAndLiability_Locations(GeneratePolicy policy) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocation = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocation.fillOutPropertyLocations(policy);
    }

    public void fillOutPropertyAndLiability_PropertyDetails(GeneratePolicy policy) throws Exception {
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		propertyDetail.fillOutPropertyDetail(policy);
    }

    public void fillOutPropertyAndLiability_Coverages(GeneratePolicy policy) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages propertyCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        propertyCoverages.fillOutPropertyAndLiabilityCoverages(policy);
    }

    public void fillOutPropertyAndLiability_CLUEProperty(GeneratePolicy policy) {
//        GenericWorkorderSquireCLUEProperty clueProperty = new GenericWorkorderSquireCLUEProperty(driver);
    }

    public void fillOutPropertyAndLiability_LineReview(GeneratePolicy policy) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderLineReviewPL propertyLineReview = new GenericWorkorderLineReviewPL(driver);
        propertyLineReview.fillOutLineReview(policy);
    }


}
