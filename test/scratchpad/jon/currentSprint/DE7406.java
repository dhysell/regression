package scratchpad.jon.currentSprint;

/**
* @Author jlarsen
* @Requirement
Create a PL policy with something that will fire off a pre-renewal direction (examples:custom farming coverage, jewelry coverage, have a farm truck or show car, missing photo or marshall and swift year, out of state DL, cat code overrident)
submitt and issue the policy
move the clock to renewal date -80
run the 'Policy Renewal Start' batch to trigger the renewal job
notice that the renewal is stopped for the pre-renewal directions (there is activities and pre-renewal directions)
DO NOT close any of them
start a cancellation job in the current term and chose 'cancel now' to finish the job (the renewal job should withdraw automatically)
start a reinstate job in the current term and issue the job
run the 'Policy Renewal Start' batch to trigger another renewal job or manually start the renewal from the actions menu
notice that the renewal is stopped for the pre-renewal directions again (there is a second set of activities)
visit the pre-renewal directions to find they have all doubled
Actual: The pre-renewal directions are not closing out/ nor are the activities tied to them when the renewal job is withdrawn and then firing off again when a new renewal starts
Expected: The pre-renewal directions and pre-renewal direction activities tied to the renewal job that was withdrawn should automatically close and then fire off again when a new renewal job is started (if they still qualify)
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558466972d/detail/defect/211178441204">DE7406</a>
* @Description Pre-Renewal directions are doubled
* @DATE Apr 19, 2018
*/
public class DE7406 {

}
