package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.generate.GeneratePolicy;
import repository.pc.sidemenu.SideMenuPC;


public class GenericWorkorderSquireInlandMarine extends BasePage {

    private WebDriver driver;

    public GenericWorkorderSquireInlandMarine(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void fillOutPLInlandMarine(GeneratePolicy policy) throws Exception {
        policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM);
        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuIMCoveragePartSelection();

        fillOutCoverageSelection(policy);

        new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver).fillOutRecreationEquipment(policy);

        new GenericWorkorderSquireInlandMarine_Watercraft(driver).fillOutWatercraft(policy);

        new GenericWorkorderSquireInlandMarine_FarmEquipment(driver).fillOutFarmEquipment(policy);

        new GenericWorkorderSquireInlandMarine_PersonalProperty(driver).fillOutPersonalProperty(policy);

        new GenericWorkorderSquireInlandMarine_Cargo(driver).filloutCargo(policy);

        new GenericWorkorderSquireInlandMarine_Livestock(driver).fillOutLivestock(policy);

        sideMenu.clickSideMenuIMExclusionsAndConditions();
        sideMenu.clickSideMenuSquireIMLineReview();
    }//END fillOutPLMarine()


    private void fillOutCoverageSelection(GeneratePolicy policy) {
        repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_CoverageSelection imSelection = new GenericWorkorderSquireInlandMarine_CoverageSelection(driver);
        for (InlandMarine im : policy.squire.inlandMarine.inlandMarineCoverageSelection_PL_IM) {
            imSelection.checkCoverage(true, im.getValue());
        }
    }
}
