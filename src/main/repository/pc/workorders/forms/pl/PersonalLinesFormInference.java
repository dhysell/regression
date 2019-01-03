package repository.pc.workorders.forms.pl;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.PLForms;
import repository.gw.generate.GeneratePolicy;

import java.util.ArrayList;
import java.util.List;

public class PersonalLinesFormInference extends BasePage {

    public PersonalLinesFormInference(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    List<PLForms> formsList = new ArrayList<PLForms>();


    public GeneratePolicy createFormsPolicyObject(List<PLForms> formsList) {


        return null;
    }


    public PersonalLinesFormInference(GeneratePolicy policyObject, WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }


    public List<PLForms> getFormsList() {
        for (PLForms form : formsList) {
            systemOut(form.getName());
        }
        return formsList;
    }


}
