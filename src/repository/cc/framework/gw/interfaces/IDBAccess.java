package repository.cc.framework.gw.interfaces;

import repository.cc.framework.gw.database.DBConnectionPoint;

import java.sql.Connection;

public interface IDBAccess {

    Connection createConnection(DBConnectionPoint dbConnectionPoint);

}
