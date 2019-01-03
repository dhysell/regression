package repository.cc.framework.utils.helpers;

import repository.cc.framework.init.Environments;

import java.sql.*;

public abstract class DBAccess {

    private Connection connection;
    private String env;

    public DBAccess(String env) {
        this.createConnection(env);
        this.env = env;
    }

    private void createConnection(String env) {
        try {
            switch (env) {
                case Environments.DEV:
                    this.connection = DriverManager.getConnection(Environments.DEV_DB);
                    break;
                case Environments.UAT:
                    this.connection = DriverManager.getConnection(Environments.UAT_DB);
                    break;
                case Environments.QA:
                    this.connection = DriverManager.getConnection(Environments.QA_DB);
                    break;
                case Environments.IT:
                    this.connection = DriverManager.getConnection(Environments.IT_DB);
                    break;
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sql) {

        ResultSet resultSet = null;

        try {
            if (this.connection.isClosed()) {
                this.createConnection(this.env);
            }
            Statement statement = this.connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void closeConnection() {
        try {
            this.connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
