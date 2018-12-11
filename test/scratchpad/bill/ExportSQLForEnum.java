package scratchpad.bill;

import java.util.List;

import persistence.globaldatarepo.entities.Glforms;
import persistence.globaldatarepo.helpers.GLFormsHelpers;
public class ExportSQLForEnum {
	
	public static String fullList = "Fisrt Line of Stuff";
	
	public static void main(String[] args) throws Exception {
		try {
			List<Glforms> st = GLFormsHelpers.getAllGeneralLiabilityForm();
			for (Glforms form: st){
				System.out.println("Stuff and Things");
				String name = form.getName();
				String nameReplace = name.replace(" ", "").replace("-", "_").replace("\"", "").replace("(", "").replace(")", "")+ "(\"" + name.replace("\"", "") + "\"),\n";
				fullList = fullList + nameReplace;
			}
		}
		finally {
			System.out.println(fullList);
		}
	}
}
