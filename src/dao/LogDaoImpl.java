package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.interfaces.LogDao;
import dao_entity.UserLog;
import exceptions.DAOException;

public class LogDaoImpl implements LogDao {

	private static final String SEPARATION = "/";
	private static final String EMPTY = "";

	private DaoFactory daoFactory;

	public LogDaoImpl() {
		this.daoFactory = DaoFactory.getInstance();
	}

	@Override
	public int updateValues(UserLog persistent) throws DAOException {
		String sql = "INSERT INTO USER_LOG (TIME, SUCCESSFUL, LOGIN) VALUES (CURRENT_TIMESTAMP, ?, ?);";
		int result = -1;
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = this.daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, persistent.getSuccessful());
			statement.setString(2, persistent.getLogin());
			result = statement.executeUpdate();
		} catch (SQLException ex) {
			throw new DAOException("Can't insert log inside table", ex);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException ex) {
			}
		}
		return result;
	}

	@Override
	public ArrayList<String> getLogs() throws DAOException {
		ArrayList<String> result = new ArrayList<>();
		String sql = "SELECT * FROM USER_LOG;";
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = this.daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String strForList = EMPTY;
				strForList = resultSet.getTimestamp("TIME") + SEPARATION
						+ resultSet.getString("SUCCESSFUL") + SEPARATION
						+ resultSet.getString("LOGIN");
				result.add(strForList);
			}
		} catch (SQLException ex) {
			throw new DAOException("Can't get logs", ex);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException ex) {

			}
		}
		return result;
	}

	@Override
	public int clearLog() throws DAOException {
		String sql = "DELETE USER_LOG";
		int result = 0;
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = this.daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			result = statement.executeUpdate();
		} catch (SQLException ex) {
			throw new DAOException("Can't delete log", ex);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException ex) {
			}
		}

		return result;
	}

}
