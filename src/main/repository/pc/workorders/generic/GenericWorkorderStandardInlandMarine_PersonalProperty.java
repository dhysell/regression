package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderStandardInlandMarine_PersonalProperty extends BasePage {

    private WebDriver driver;

    public GenericWorkorderStandardInlandMarine_PersonalProperty(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void fillOutPersonalProperty(GeneratePolicy policy) throws Exception {

        if (policy.standardInlandMarine.inlandMarineCoverageSelection_Standard_IM.contains(InlandMarine.PersonalProperty)) {
            new SideMenuPC(driver).clickSideMenuStandardIMPersonalProperty();

            for (PersonalProperty personalProperty : policy.standardInlandMarine.personalProperty_PL_IM) {
                if (personalProperty != null) {
                    GenericWorkorderSquireInlandMarine_PersonalProperty personalPropertyPage = new GenericWorkorderSquireInlandMarine_PersonalProperty(driver);
                    personalPropertyPage.clickAdd();
                    personalPropertyPage.setDeductible(personalProperty.getDeductible());

                    for (PersonalPropertyScheduledItem personalPropertyScheduledItem : personalProperty.getScheduledItems()) {
                        personalPropertyPage.clickAddScheduledItems();
                        personalPropertyPage.setDescription(personalPropertyScheduledItem.getDescription());
                        personalPropertyPage.setScheduledItemLimit(personalPropertyScheduledItem.getLimit());
                    }//AND FOR
                    if (personalProperty.getAdditionalInsureds() != null) {
                        for (String ai : personalProperty.getAdditionalInsureds()) {
                            personalPropertyPage.clickAddAdditionalInsured();
                            personalPropertyPage.setNameAdditionalInsured(ai);
                        }//END FOR
                    }//END IF
                    personalPropertyPage.clickOk();
                }//END IF
            }//END FOR
        }//END IF
    }


}
