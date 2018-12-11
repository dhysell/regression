package repository.cc.framework.gw.cc;

import org.testng.Assert;
import repository.gw.helpers.NumberUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

public class ClaimDBSearch {

    Connection connection;

    public ClaimDBSearch(Connection connection) {
        this.connection = connection;
    }

    public String getRandomOpenVehicleClaim() {
        String sqlQuery = "select c.ClaimNumber from cc_incident i inner join cc_claim c on c.id = i.ClaimID inner join cc_exposure e on e.ClaimID = c.ID where c.State = 2 and i.Subtype = 3";
        HashMap<String, String> resultHashMap = randomResultFromQuerey(sqlQuery);
        return resultHashMap.get("ClaimNumber");
    }

    public String getRandomInjuryIncidentClaim() {
        String sqlQuery = "select c.ClaimNumber from cc_incident i inner join cc_claim c on c.id = i.ClaimID inner join cc_exposure e on e.ClaimID = c.ID where c.State = 2 and i.Subtype = 5";
        HashMap<String, String> resultHashMap = randomResultFromQuerey(sqlQuery);
        return resultHashMap.get("ClaimNumber");
    }

    public String getRandomPolicy() {
        String sqlQuery = "select PolicyNumber from cc_policy where CURRENT_TIMESTAMP <= ExpirationDate and CURRENT_TIMESTAMP > EffectiveDate and PolicyType in (10019,10025) and TotalVehicles > 0";
        HashMap<String, String> resultHashMap = randomResultFromQuerey(sqlQuery);
        return resultHashMap.get("PolicyNumber");
    }

    public String getRandomIncidentClaim() {
        String sqlQuery = "select c.ClaimNumber from cc_incident i inner join cc_claim c on c.id = i.ClaimID inner join cc_exposure e on e.ClaimID = c.ID where c.State = 2";
        HashMap<String, String> resultHashMap = randomResultFromQuerey(sqlQuery);
        return resultHashMap.get("ClaimNumber");
    }

    public String getRandomClaimWithVoidedFieldOrDraftCheck() {
        String sqlQuery = "select c.CheckNumber, cl.ClaimNumber from cc_transaction t inner join cc_check c on t.CheckID = c.ID inner join cc_claim cl on cl.ID = c.ClaimID  where c.Status = 5 and t.Subtype = 1 and c.PaymentMethod = 1 order by c.IssueDate DESC";
        HashMap<String, String> resultHashMap = randomResultFromQuerey(sqlQuery);
        return resultHashMap.get("ClaimNumber");
    }

    public HashMap getRandomManualCheckRegistryEntry() {
        String sqlQuery = "select ct.Name, cr.BegCheckNumber, cr.EndCheckNumber, crd.UserName from ccx_manualcheckregister_fbm cr inner join cc_user u on cr.AssignedTo = u.ID inner join cc_credential crd on crd.ID = u.CredentialID inner join cctl_fbchecktype ct on cr.CheckType = ct.ID where cr.CompletedDate is null";
        HashMap<String, String> resultHashMap = randomResultFromQuerey(sqlQuery);
        return resultHashMap;
    }

    public ResultSet executeQuery(String query) {
        try {
            return connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail("Unable to find policy number.");
        }
        return null;
    }

    public HashMap<String, String> randomResultFromQuerey(String query) {
        ResultSet resultSet = this.executeQuery(query);
        HashMap<String, String> result = new HashMap<>();
        int randNum = 0;
        try {
            if (resultSet.last()) {
                randNum = NumberUtils.generateRandomNumberInt(0, resultSet.getRow() - 1);
                resultSet.beforeFirst();
            }
            int count = 0;
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                count++;
                if (count == randNum) {
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        result.put(metaData.getColumnName(i), resultSet.getString(i));
                    }
                    break;
                }
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail("NO DATA FOUND");
        }
        return null;
    }
}