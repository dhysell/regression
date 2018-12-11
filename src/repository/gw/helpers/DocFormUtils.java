package repository.gw.helpers;

import persistence.globaldatarepo.helpers.DocumentsFormsHelper;
import repository.gw.enums.DocFormEvents;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;

import java.util.ArrayList;

public class DocFormUtils {

	public static ArrayList<String> getListOfDocumentNamesForListOfEventNames(ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringPolicyCreation) throws Exception {
		if(eventsHitDuringPolicyCreation == null || eventsHitDuringPolicyCreation.size() < 1) {
			return new ArrayList<String>();
		} else {
			ArrayList<DocFormEvents.PolicyCenter> eventsToActuallySend = new ArrayList<DocFormEvents.PolicyCenter>();
			for(DocFormEvents.PolicyCenter pcEvent : eventsHitDuringPolicyCreation) {
				if(pcEvent.getDirectlyTriggersDocuments()) {
					eventsToActuallySend.add(pcEvent);
				}
			}
			
			if(eventsToActuallySend.size() < 1) {
				return new ArrayList<String>();
			} else {
				ArrayList<String> namesToSend = DocFormEvents.PolicyCenter.getEventNamesFromListOfEvents(eventsToActuallySend);
				return DocumentsFormsHelper.getListOfDocumentNamesForListOfEventNames(namesToSend);
			}
		}
	}

	public static ActualExpectedDocumentDifferences compareListsForDifferences(ArrayList<String> formsOrDocsActualFromUI, ArrayList<String> formsOrDocsExpectedBasedOnEventsHit) {
		ArrayList<String> actual = new ArrayList<String>(formsOrDocsActualFromUI);
		ArrayList<String> expected = new ArrayList<String>(formsOrDocsExpectedBasedOnEventsHit);


		ArrayList<String> actualDifferences = new ArrayList<String>(actual);
		ArrayList<String> expectedDifferences = new ArrayList<String>(expected);


	    actualDifferences.removeAll(expected);
	    expectedDifferences.removeAll(actual);

	    ActualExpectedDocumentDifferences aeToReturn = new ActualExpectedDocumentDifferences();
	    aeToReturn.setInUserInterfaceNotInExpected(actualDifferences);
	    aeToReturn.setInExpectedNotInUserInterface(expectedDifferences);
	 
	    return aeToReturn;
	}

}
