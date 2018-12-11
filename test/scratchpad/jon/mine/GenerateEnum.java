package scratchpad.jon.mine;

import java.util.List;

import org.testng.annotations.Test;

import persistence.globaldatarepo.entities.CPForms;
import persistence.globaldatarepo.helpers.CPFormsHelper;

public class GenerateEnum {


    @Test
    public void createEnumFromTable() throws Exception {

        List<CPForms> formsList = CPFormsHelper.getAllCommercialPropertyForm();

        for (CPForms form : formsList) {
            System.out.println(form.getName().replace(" ", "").replace("(", "_").replace(")", "_").replace("�", "_") + "_" + form.getNumber().replace(" ", "_") + "(\"" + form.getName() + "\"),");
        }


    }


}
