package scratchpad.jon;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.helpers.DateUtils;
import gwclockhelpers.ApplicationOrCenter;

public class Shanigans {


    private WebDriver driver;

    public class MyThread extends Thread {
        public void run() {
            System.out.println("MyThread!!!!!!!!!!!!!!!!!!");
            DateUtils foo = new DateUtils();
            Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

//			try {
//				ClockUtils.getCurrentDates();
//			} catch (Exception e) {
//				System.out.println("FAIL!!!!!!!!!!!!!!!!!");
//			}

        }
    }


    @Test
    public void failme() {


        for (int i = 0; i < 10; i++) {
            new MyThread().start();
            new MyThread().start();
            new MyThread().start();
            new MyThread().start();
            new MyThread().start();
            new MyThread().start();
        }


    }

}
