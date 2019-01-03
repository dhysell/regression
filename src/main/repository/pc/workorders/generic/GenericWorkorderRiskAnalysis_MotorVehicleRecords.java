package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class GenericWorkorderRiskAnalysis_MotorVehicleRecords extends GenericWorkorderRiskAnalysis {


    public GenericWorkorderRiskAnalysis_MotorVehicleRecords(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }


}
