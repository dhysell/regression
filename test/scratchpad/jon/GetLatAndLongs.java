package scratchpad.jon;


import java.math.BigDecimal;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AddressTemp2;

public class GetLatAndLongs extends BaseTest {
    private WebDriver driver;

    @Test
    public void getlatLongs() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        driver.get("https://www.latlong.net/");


        List<AddressTemp2> foo = AddressTemp2.getRandomAddressList(500);

        for (AddressTemp2 address : foo) {
            try {
                driver.findElement(By.xpath("//input[contains(@placeholder, 'Type a place name')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"));
//				find(By.xpath("//input[contains(@placeholder, 'Type a place name')]")).sendKeys(address.getFormattedAddress());
                driver.findElement(By.xpath("//button[@title='Find Lat & Long']")).click();
                System.out.println(driver.findElement(By.xpath("//span[@id='latlngspan']")).getText());
                String latLong = driver.findElement(By.xpath("//span[@id='latlngspan']")).getText().replace("(", "").replace(")", "");
                String[] latLongSplit = latLong.split(",");
                address.setLatitudeFbm(new BigDecimal(latLongSplit[0].trim()));
                address.setLongitudeFbm(new BigDecimal(latLongSplit[1].trim()));
            } catch (UnhandledAlertException e) {
                driver.switchTo().alert().accept();
            }
        }

        AddressTemp2.updateLatAndLong(foo);


        driver.quit();

    }


}
