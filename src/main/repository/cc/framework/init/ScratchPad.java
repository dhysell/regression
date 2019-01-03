package repository.cc.framework.init;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ScratchPad {
    @Test
    public void ChangeCountyLookupForRecoveries() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://fbmis117c.idfbins.com:1433;DatabaseName=ccDB;User=ccUser;Password=mau$2ug");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM cc_claim");
            System.out.println(resultSet.getFetchSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
