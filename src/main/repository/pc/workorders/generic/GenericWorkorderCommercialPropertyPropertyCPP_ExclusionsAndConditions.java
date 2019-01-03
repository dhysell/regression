package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.pc.sidemenu.SideMenuPC;


public class GenericWorkorderCommercialPropertyPropertyCPP_ExclusionsAndConditions extends BasePage {

    private WebDriver driver;

    public GenericWorkorderCommercialPropertyPropertyCPP_ExclusionsAndConditions(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    GenericWorkorderCommercialPropertyPropertyCPP commercialPropertyProperty = new GenericWorkorderCommercialPropertyPropertyCPP(driver);
    repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);

    private void isOnExclusionsAndCondtionsTab(CPPCommercialProperty_Building building) throws Exception {
        if (finds(By.xpath("//span[text()='Exclusions']")).isEmpty()) {
            //is on the correct builidng
            if (finds(By.xpath("//span[(text()='Location " + building.getPropertyLocation().getPropertyLocationNumber() + ": Property " + building.getNumber() + "') or (text()='New Property')]")).isEmpty()) {
                //is it on the wrong building
                if (!finds(By.xpath("//span[@id='CPBuildingPopup:ttlBar']")).isEmpty()) {
                    commercialPropertyProperty.clickCancel();
                }
                sideMenu.clickSideMenuCPProperty();
                commercialPropertyProperty.clickLocationRow(building.getPropertyLocation().getAddress());
                commercialPropertyProperty.editPropertyByNumber(building.getNumber());
            } else {
                commercialPropertyProperty.clickExclusionsConditionsTab();
            }//end else
        }//end if
    }//end isOnExclusionsAndCondtionsTab(CPPCommercialProperty_Building building)


    public void fillOutPropertyExclusionsConditions(CPPCommercialProperty_Building building) throws Exception {
        fillOutExclusionsQQ(building);
        fillOutExclusionsFA(building);
        fillOutConditionsQQ(building);
        fillOutConditionsFA(building);
    }


    public void fillOutExclusionsQQ(CPPCommercialProperty_Building building) throws Exception {
        isOnExclusionsAndCondtionsTab(building);
        //EXCLUSIONS
        //		Additional Property Not Covered CP 14 20
        //		Broken Or Cracked Glass Exclusion Form IDCP 31 1006
        //		Roof Exclusion Endorsement IDCP 31 1004
        //		Exclusion Of loss Due To By-Products Of Production Or Processing Operations (Rental Properties) CP 10 34
    }

    public void fillOutExclusionsFA(CPPCommercialProperty_Building building) throws Exception {
        isOnExclusionsAndCondtionsTab(building);
        //EXCLUSIONS
        //		Additional Property Not Covered CP 14 20
        //		Broken Or Cracked Glass Exclusion Form IDCP 31 1006
        //		Roof Exclusion Endorsement IDCP 31 1004
        //		Exclusion Of loss Due To By-Products Of Production Or Processing Operations (Rental Properties) CP 10 34
    }

    public void fillOutConditionsQQ(CPPCommercialProperty_Building building) throws Exception {
        isOnExclusionsAndCondtionsTab(building);
        //CONDITIONS
        //		Burglary And Robbery Protective Safeguards CP 12 11
        //		Limitations On Coverage For Roof Surfacing CP 10 36
        //		Protective Safeguards CP 04 11
        //		Tentative Rate CP 99 93
    }

    public void fillOutConditionsFA(CPPCommercialProperty_Building building) throws Exception {
        isOnExclusionsAndCondtionsTab(building);
        //CONDITIONS
        //		Burglary And Robbery Protective Safeguards CP 12 11
        //		Limitations On Coverage For Roof Surfacing CP 10 36
        //		Protective Safeguards CP 04 11
        //		Tentative Rate CP 99 93
    }


}
