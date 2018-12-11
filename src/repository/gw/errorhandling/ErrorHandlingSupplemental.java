package repository.gw.errorhandling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import repository.pc.workorders.generic.GenericWorkorderSupplemental;

import java.util.List;

public class ErrorHandlingSupplemental extends ErrorHandlingHelpers {

	private WebDriver driver;
	public ErrorHandlingSupplemental(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}


    public void errorHandlingSupplementalPage(int timeToWait) {

		saveDraft(timeToWait);
        GenericWorkorderSupplemental supp = new GenericWorkorderSupplemental(driver);
		if (errorExists()) {
			List<WebElement> requiredFields = finds(By.xpath("//td[contains(@class, '-invalid')]/preceding-sibling::td[1]/div"));
			for (WebElement element : requiredFields) {
				String text = element.getText();
				switch (text) {
				case "Does applicant operate a day spa?":
					supp.setOperateDaySpayRadio(false);
					break;
				case "Does applicant have off premises exposures? If yes, check all that apply.":
					supp.setOffPremisesExposuresRadio(false);
					break;
				case "Description":
					supp.setOtherDescription("The Description Goes Here");
					break;
				case "Is trash stored in a steel covered container?":
					supp.setTrashInSteelContainerRadio(true);
					break;
				case "Do any loss control features apply? If yes, check all that apply.":
					supp.setOffPremisesExposuresRadio(false);
					break;
				case "Are dishes chipped?":
					supp.setDishesChippedRadio(false);
					break;
				case "Is furniture in good repair and sturdy?":
					supp.setSturdyFurnitureRadio(true);
					break;
				case "Does applicant have catering services?":
					supp.setCateringServicesRadio(false);
					break;
				case "Does the applicant have liquor sales?":
					supp.setLiquorSalesRadio(false);
					break;
				case "Is trash stored in a location separate from cooking area?":
					supp.setTrashStoredAwayFromFoodRadio(true);
					break;
				case "Has the applicant had any major health department violations in the last three years?":
					supp.setMajorhealthViolationsRadio(false);
					break;
				case "Does the applicant have kitchen units available to guests?":
					supp.setMotelKitchensRadio(false);
					break;
				case "Are parking areas, hallways and common areas well lit at night?":
					supp.setEmergencyLightingCheckBox(true);
					break;
				case "Does operation have an exercise facility with free weights, sauna or steam room?":
					supp.setMotelExerciseFacility(false);
					break;
				case "Do any of the following exposures exist on the premises include concessionaires or leased operations? If yes, check all that apply.":
					supp.setMotelExposuresRadio(false);
					break;
				case "Does applicant provide banquet facilities for weddings, birthdays or other special events?":
					supp.setMotelBanquetFacilities(false);
					break;
				case "Does applicant sell alcoholic products?":
					supp.setMotelSellAlchoholicProducts(false);
					break;

				}

			}
		}
	}
}
