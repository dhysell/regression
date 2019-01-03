package repository.cc.framework.utils.helpers;

import org.testng.Assert;
import repository.gw.helpers.NumberUtils;

import java.sql.ResultSet;
import java.util.LinkedList;

public class ClaimCenterDBActions extends DBAccess {

    public ClaimCenterDBActions(String env) {
        super(env);
    }

    public String getRandomAutoPolicyNumber() {
        String sqlQuery = "select PolicyNumber from cc_policy where CURRENT_TIMESTAMP <= ExpirationDate and CURRENT_TIMESTAMP > EffectiveDate and PolicyType in (10019,10025) and TotalVehicles > 0";
        return getPolicy(sqlQuery);
    }

    public String getRandomPropertyPolicyNumber() {
        String sqlQuery = "select PolicyNumber from cc_policy where CURRENT_TIMESTAMP <= ExpirationDate and CURRENT_TIMESTAMP > EffectiveDate and PolicyType in (10016,10012) and TotalProperties > 0";
        return getPolicy(sqlQuery);
    }

    public String getRandomClaimForRecoveries() {
        String sqlQuery = "select ClaimNumber from cc_claim c inner join cc_transaction t on c.ID = t.ClaimID where t.Subtype = '2' and c.ClaimNumber <> '99999%'";
        return getClaim(sqlQuery);
    }

    public String getRandomClaimForChecks() {
        String sqlQuery = "select ClaimNumber from cc_claim where ID in (select ClaimID from cc_transaction where Subtype = '1')";
        return getClaim(sqlQuery);
    }

    public String getFullUserNameWithAttibute(String attributeName) {
        String sqlQuery = "select c.FirstName, c.LastName from cc_contact c where ID = (select ContactID from cc_user where ID = (select UserID from cc_attributeuser where AttributeID = (select ID from cc_attribute where Name = '" + attributeName + "')))";
        ResultSet resultSet = super.executeQuery(sqlQuery);
        try {
            resultSet.next();
            String firstName = resultSet.getString("FirstName");
            String lastName = resultSet.getString("LastName");
            return firstName + " " + lastName;
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Bad SQL Query");
        }

        return null;
    }

    private String getPolicy(String sqlQueryString) {
        ResultSet policyNumbers = super.executeQuery(sqlQueryString);
        LinkedList<String> policies = new LinkedList<>();
        int count = 0;

        try {
            while (policyNumbers.next()) {
                policies.add(policyNumbers.getString("PolicyNumber"));
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return policies.get(NumberUtils.generateRandomNumberInt(0, count));
    }

    private String getClaim(String sqlQueryString) {
        ResultSet policyNumbers = super.executeQuery(sqlQueryString);
        LinkedList<String> claims = new LinkedList<>();
        int count = 0;

        try {
            while (policyNumbers.next()) {
                claims.add(policyNumbers.getString("ClaimNumber"));
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims.get(NumberUtils.generateRandomNumberInt(0, count));
    }
}
