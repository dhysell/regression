package scratchpad.jon.currentSprint;

import org.testng.annotations.Test;

import repository.gw.generate.GeneratePolicy;

/**
 * @Author jlarsen
 * @Requirement Create a squire
 * Add dues members to Members screen
 * Leave Renewal Dues status as: " no change" or change to either " Charge at Renewal" or " Remove at Renewal" > Quote.
 * Actual: Upon selecting Quote, Validation error: Cannot assign null as renewal payer for membership dues only (SQ063) generates and policy does not quote.
 * Expected: Info from step 3 should allow policy to quote without validation error generating.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558466972d/detail/defect/205835169680">DE7333</a>
 * @Description Membership Dues Renewal Status on Squire causing validation error
 * @DATE Apr 19, 2018
 */
@Test()
public class DE7333 {


    public GeneratePolicy myPolicyObject = null;

    @Test(enabled = false)
    public void WHATAREYOUTESTING() {


    }
}


























