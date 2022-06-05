package client.service;

import java.util.ArrayList;

import dao.LogDaoImpl;
import dao.interfaces.LogDao;
import dao_entity.UserLog;
import exceptions.DAOException;

public class FillLog {

	private UserLog userLog;
	private LogDao logDao;
	
	public FillLog()
	{
		this.userLog = new UserLog();
		this.logDao = new LogDaoImpl();
	}
	
	public void addLogEvent(String successful, String login) throws DAOException
	{
		userLog.setSuccessful(successful);
		userLog.setLogin(login);
		this.logDao.updateValues(this.userLog);
	}
	
	public ArrayList<String> getLogs() throws DAOException
	{
		return this.logDao.getLogs();
	}
	
	public int clearLog() throws DAOException
	{
		return this.logDao.clearLog();
	}
}
