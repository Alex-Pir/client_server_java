package dao.interfaces;

import java.util.ArrayList;

import dao_entity.UserLog;
import exceptions.DAOException;

public interface LogDao extends Dao<UserLog> {

	ArrayList<String> getLogs() throws DAOException;
	int clearLog() throws DAOException;
}
