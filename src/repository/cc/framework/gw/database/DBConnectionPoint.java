package repository.cc.framework.gw.database;

import gwclockhelpers.ApplicationOrCenter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class DBConnectionPoint {
    private String serverName;
    private String userName;
    private String password;
    private String environmentName;
    private String databaseName;

    public static DBConnectionPoint getConnectionTo(ApplicationOrCenter applicationOrCenter, String environment) {
        String connectionEnvironment = (applicationOrCenter.getValue() + environment).toUpperCase().replaceAll(" ", "");
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlserver://fbms2048:1433;DatabaseName=QAWIZPROGlobalDataRepository;User=SeleniumUser;Password=seleniumuser");
            String sqlString = "select top 1 * from DBConnectionToGWServers where EnvironmentName = '" + connectionEnvironment + "'";
            ResultSet resultSet = connection.createStatement().executeQuery("select top 1 * from DBConnectionToGWServers where EnvironmentName = '" + connectionEnvironment + "'");
            resultSet.next();
            DBConnectionPoint connectionPoint = new DBConnectionPoint();
            connectionPoint.serverName = resultSet.getString("ServerName");
            connectionPoint.userName = resultSet.getString("UserName");
            connectionPoint.password = new String(Base64.getDecoder().decode(resultSet.getString("Password")));
            connectionPoint.environmentName = resultSet.getString("EnvironmentName");
            connectionPoint.databaseName = resultSet.getString("DatabaseName");
            return connectionPoint;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getServerName() {
        return serverName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getConnectionString() {
        return "jdbc:sqlserver://" + this.serverName + ":1433;DatabaseName=" + this.databaseName + ";User=" + this.userName + ";Password=" + this.password;
    }
}
