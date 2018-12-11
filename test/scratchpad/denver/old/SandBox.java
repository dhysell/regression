package scratchpad.denver.old;

import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.init.Environments;
import gwclockhelpers.ApplicationOrCenter;
import org.testng.annotations.Test;

import java.sql.ResultSet;

@SuppressWarnings("unused")
public class SandBox extends BaseOperations {

	@Test()
	public void createCheckComprehensive() {
		super.initOn(ApplicationOrCenter.ClaimCenter, Environments.DEV);
		ResultSet resultSet = interact.withDB.executeQuery("Select TOP 10 * from cc_claim");

	}	
}

