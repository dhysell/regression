package repository.cc.framework.gw.database;

import repository.cc.framework.gw.interfaces.IDBAccess;
import repository.cc.framework.init.Environments;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBAccess implements IDBAccess {
    @Override
    public Connection createConnection(DBConnectionPoint dbConnectionPoint) {
        try {
            switch (dbConnectionPoint.getEnvironmentName()) {
                case Environments.DEV:
                    return DriverManager.getConnection(dbConnectionPoint.getConnectionString());
                case Environments.UAT:
                    return DriverManager.getConnection(Environments.UAT_DB);
                case Environments.QA:
                    return DriverManager.getConnection(Environments.QA_DB);
                case Environments.IT:
                    return DriverManager.getConnection(Environments.IT_DB);
                default:
                    throw new SQLException("Unrecognized Environment Name: " + dbConnectionPoint.getEnvironmentName());
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
        return null;
    }
}
