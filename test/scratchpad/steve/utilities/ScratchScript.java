package scratchpad.steve.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.State;

import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.helpers.AbUserHelper;
public class ScratchScript extends BaseTest{
	
	private List<ScriptItem> contactInfo = new ArrayList<>();
	
	@Test
	public void getLineItems() throws Exception {
		this.contactInfo = processInputFile("\\\\fbmis139\\share\\Email Phone Update\\ir_contact_info.csv");
		for(ScriptItem updateMe : contactInfo) {
			checkUpdate(updateMe);
		}
	}
	
	private void checkUpdate(ScriptItem contact) throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.loginAndSearchContact(AbUserHelper.getRandomDeptUser("Policy"), contact.getFirstName(), contact.getLastName(), contact.getAddress(), State.valueOfAbbreviation(contact.getState()));
        //Add Steps
        
        
		System.out.println("Done");
		
	}
	
	private List<ScriptItem> processInputFile(String inputFilePath) throws IOException {
	    List<ScriptItem> inputList = new ArrayList<>();
	    try{
	      File inputF = new File(inputFilePath);
	      InputStream inputFS = new FileInputStream(inputF);
	      BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
	      inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
	      br.close();
	    } catch (IOException e) {
	    	System.out.println("An error occured while trying to process the file.");
	    	throw e;
	    }
	    return inputList;
	}
	
	private Function<String, ScriptItem> mapToItem = (line) -> {
		ArrayList<ScriptItem> lineItems = new ArrayList<ScriptItem>();  
		String[] p = line.split("\\|");// a CSV has comma separated lines  
		  ScriptItem item = new ScriptItem();
		  if(p.length>=13)
			  item.setBusinessPhone(p[12]);
		  if(p.length>=12)
			  item.setAltPhone(p[11]);
		  if(p.length>=11)
			  item.setCellPhone(p[10]);
		  if(p.length>=10)
			  item.setHomePhone(p[9]);
		  if(p.length>=9)
			  item.setZip(p[8]);
		  if(p.length>=8)
			  item.setState(p[7]);
		  if(p.length>=7)
			  item.setCity(p[6]);
		  if(p.length>=6)
			  item.setAddress(p[5]);
		  if(p.length>=5)
			  item.setEmail(p[4]);
		  if(p.length>=4)
			  item.setFirstName(p[3]);
		  if(p.length>=3)
			  item.setLastName(p[2]);
		  if(p.length>=2)
			  item.setSSN(p[1]);
		  if(p.length>=1)
			  item.setMemberNum(p[0]);
		  return item;	  
	};
/*		
	private void readFile(String crunchifyFile) {
 
		Stream<String> crunchifyStream = null;
		try {
 
			// Read all lines from a file as a Stream. Bytes from the file are decoded into characters using the UTF-8 charset
			crunchifyStream = Files.lines(Paths.get(crunchifyFile));
 
		} catch (IOException e) {
			e.printStackTrace();
		}
 
		crunchifyStream.forEach(System.out::println);
	}
  */  
}
