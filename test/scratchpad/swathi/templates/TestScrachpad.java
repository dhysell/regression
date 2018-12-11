package scratchpad.swathi.templates;

import com.idfbins.driver.BaseTest;
import org.testng.annotations.Test;
import persistence.globaldatarepo.entities.*;
import persistence.globaldatarepo.helpers.APlusAutoHelper;

import java.util.List;

public class TestScrachpad extends BaseTest {

    @Test
    public void test() throws Exception {
        /*Underwriters uw = UnderwritersHelper.getUnderwriterInfoByFullName("Matthew Brown");
        Underwriters uw1 = UnderwritersHelper.getUnderwriterInfoByFullName("Bill Terry");*/
        List<APlusAuto>x = APlusAutoHelper.  getAPlusAutoTestCase();

    }
}
