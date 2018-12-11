package random;

import java.util.List;

import org.testng.annotations.Test;

import persistence.globaldatarepo.helpers.LHTableHelper;
import persistence.guidewire.helpers.TerminatedContactsHelper;

public class RemoveTerminatedLienholders {	
    @Test
    public void RemoveLienholders() throws Exception {
        List<String> listOfLHNumbers = new LHTableHelper().getListOfLienholderNumbers();
        List<String> listOfLHNumbersToRemove = new TerminatedContactsHelper().getTerminatedLienholderContacts(listOfLHNumbers);
        new LHTableHelper().removeTerminatedLienholders(listOfLHNumbersToRemove);
    }
}
