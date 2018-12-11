package scratchpad.rusty.misc;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class TestListener {
    @BeforeClass
    public void beforeClass() {
    }

    @Test
    public void thisPasses() {

        System.out.println("Execution of thisPasses");
        Assert.assertTrue(true);
    }

    @Test
    public void thisFails() {
        System.out.println("Execution of thisFails");
        Assert.assertTrue(false);
    }

    @BeforeMethod
    public void beforeMethod() {
        System.out.println("Execution of Before method");
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("Execution of After method");
    }
}