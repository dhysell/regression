package scratchpad.ian.scratchpads;

import repository.listeners.TestInfo;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

@TestInfo(author = "iclouser", team = "DataWizards", centers = {"PolicyCenter"}, featureNumber = "f69", storyNumber = "US69", themes = {"bob", "tom", "hippy"})
public class DummyTest {

    @Test
    @TestInfo(author = "iclouser", team = "DataWizards", centers = {"BillingCenter, PolicyCenter"}, featureNumber = "f420", storyNumber = "US6969", themes = {"bob", "tom", "hippy"})
    public void doingTheThing() throws Exception {
        Method m = this.getClass().getMethod("doingTheThing");


        System.out.println("Class level annotations: \n");
        if (this.getClass().isAnnotationPresent(TestInfo.class)) {
            System.out.println(this.getClass().getAnnotation(TestInfo.class).author());
            System.out.println(this.getClass().getAnnotation(TestInfo.class).team());
            System.out.println(Arrays.asList(this.getClass().getAnnotation(TestInfo.class).centers()));
            System.out.println(this.getClass().getAnnotation(TestInfo.class).featureNumber());
            System.out.println(this.getClass().getAnnotation(TestInfo.class).storyNumber());
            System.out.println(Arrays.asList(this.getClass().getAnnotation(TestInfo.class).themes()));
            System.out.println("\n");

        }


        System.out.println("Method annotations: \n");
        if (m.isAnnotationPresent(TestInfo.class)) {
            System.out.println(m.getAnnotation(TestInfo.class).author());
            System.out.println(m.getAnnotation(TestInfo.class).team());
            System.out.println(Arrays.asList(m.getAnnotation(TestInfo.class).centers()));
            System.out.println(m.getAnnotation(TestInfo.class).featureNumber());
            System.out.println(m.getAnnotation(TestInfo.class).storyNumber());
            System.out.println(Arrays.asList(m.getAnnotation(TestInfo.class).themes()));
            System.out.println("\n\n");

        }
    }
}

