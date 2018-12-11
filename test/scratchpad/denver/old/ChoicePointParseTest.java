package scratchpad.denver.old;

import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("unused")
public class ChoicePointParseTest {

	@Test
	public void parseChoicePointFile() throws Exception {

		parseChoicePoint();

	}
	
	private void parseChoicePoint() throws FileNotFoundException {
		File file = new File("C:\\dev\\workspaces\\eclipse\\selenium\\guidewire\\GWTesting\\test\\scratchpad\\denver\\choicepoint\\clue_history_fabuid01_20170206045108.txt");
		Scanner input = new Scanner(file);	
		
		List<String> claimNumbers = new ArrayList<String>();
		List<BigDecimal> amountsPaid = new ArrayList<BigDecimal>();
		
		while(input.hasNext()) {
			try {
				String tempString = input.nextLine();
				String claimNumber = tempString.substring(591, 611);
				String totalPaidString = tempString.substring(619,628);
				BigDecimal totalPaid = formatTotalPaid(totalPaidString);
				
				claimNumbers.add(claimNumber);
				amountsPaid.add(totalPaid);

			} catch (Exception e) {
				System.out.println("Skip Parsing - Row does not contain parsable data.");
			}			
		}

		input.close();
		
		runCompare(claimNumbers, amountsPaid);
	}
	
	private ChoicePointFile runCompare(List<String> claimNumbers, List<BigDecimal> amountsPaid) {
		
		String previousClaim = null;
		String currentClaim = null;
		
		for (int i = 0; i < claimNumbers.size(); i++) {
			
			ChoicePointFile currentRecord = new ChoicePointFile(claimNumbers.get(i), amountsPaid.get(i));			
		}
		return null;	
		
		
	}

	private BigDecimal formatTotalPaid(String amountString) {
		
		amountString = new StringBuilder(amountString).insert(amountString.length()-2, ".").toString();		
		boolean negateValue = false;
		BigDecimal value;
		
		if (amountString.contains("-")) {
			amountString = amountString.replace("-", "");
			negateValue = true;
		}
		
		if (negateValue) {
			value = new BigDecimal(amountString).negate();
		} else {
			value = new BigDecimal(amountString);
		}
		
		return value;
	}
	
	private void retrieveGuideWireData(String claimNumber) {
		
	}
	
}
