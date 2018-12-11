package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.gw.generate.GeneratePolicy;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderStandardInlandMarine extends BasePage {

    private WebDriver driver;

    public GenericWorkorderStandardInlandMarine(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void fillOutStandardInlandMarine(GeneratePolicy policy) throws Exception {
        fillOutCoverageSelection(policy);
        fillOutFarmEquipment(policy);
        fillOutPersonalProperty(policy);
        fillOutExclusionsAndConditions(policy);
    }


    public void fillOutCoverageSelection(GeneratePolicy policy) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderStandardIMCoverageSelection coverageSelection = new GenericWorkorderStandardIMCoverageSelection(driver);
        coverageSelection.fillOutCoverageSelection(policy);
    }

    public void fillOutFarmEquipment(GeneratePolicy policy) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderStandardIMFarmEquipment farmEquipment = new GenericWorkorderStandardIMFarmEquipment(driver);
        farmEquipment.fillOutFarmEquipment(policy);
    }

    public void fillOutPersonalProperty(GeneratePolicy policy) throws Exception {
        repository.pc.workorders.generic.GenericWorkorderStandardInlandMarine_PersonalProperty inlandMarine_PersonalProperty = new GenericWorkorderStandardInlandMarine_PersonalProperty(driver);
        inlandMarine_PersonalProperty.fillOutPersonalProperty(policy);
    }

    public void fillOutExclusionsAndConditions(GeneratePolicy policy) throws Exception {
        new SideMenuPC(driver).clickSideMenuIMExclusionsAndConditions();
    }


}
