package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.interfaces.UserDao;
import dao_entity.UserProfile;
import exceptions.DAOException;

public class UserDaoImpl implements UserDao {
	
	private final DaoFactory daoFactory;
	
	public UserDaoImpl()
	{
		this.daoFactory = DaoFactory.getInstance();
	}
	
	@Override
	public UserProfile getByLoginAndPass(final String login, final String password) throws DAOException {
		String sql = "SELECT * FROM USERS WHERE LOGIN = ? AND PASSWORD = ?";
		UserProfile user = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try
		{
			connection = this.daoFactory.getConnection();
			
			statement = connection.prepareStatement(sql);
			statement.setString(1, login);
			statement.setString(2, password);
			
			resultSet = statement.executeQuery();
			
			while (resultSet.next())
			{
				if (login.equals(resultSet.getString("LOGIN")) && password.equals(resultSet.getString("PASSWORD")))
				{
					user = new UserProfile(resultSet.getLong("ID"),
										   resultSet.getString("LOGIN"),
										   resultSet.getString("PASSWORD"),
										   resultSet.getString("STATUS"),
										   resultSet.getDate("DATE").getTime());
					break;
				}
			}
		}
		catch(SQLException ex)
		{
			throw new DAOException("Can't get user profile for user with login = " + login, ex);
		}
		finally
		{
			try
			{
				if (resultSet != null)
				{
					resultSet.close();
				}
				if (statement != null)
				{
					statement.close();
				}
				if (connection != null)
				{
					connection.close();
				}
			}
			catch(SQLException ex)
			{
			}
		}
		
		return user;
	}

	@Override
	public int setNewPassById(final long id, final String password) throws DAOException {
		String sql = "UPDATE USERS SET PASSWORD = ?, DATE = CURRENT_DATE WHERE ID = ?";
		int res = 0;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try
		{
			connection = this.daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, password);
			statement.setLong(2, id);
			res = statement.executeUpdate();
		}
		catch(SQLException ex)
		{
			throw new DAOException("Can't set new password for user with ID = " + id, ex);
		}
		finally
		{
			try
			{
				if (statement != null)
				{
					statement.close();
				}
				if (connection != null)
				{
					connection.close();
				}
			}
			catch(SQLException ex)
			{
			}
		}
		
		return res;
	}

	@Override
	public int updateValues(UserProfile persistent) throws DAOException {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO USERS (ID, LOGIN, PASSWORD, STATUS, DATE) VALUES (?, ?, ?, ?, CURRENT_DATE)";
		Connection connection = null;
		PreparedStatement statement = null;
		
		final long id = persistent.getId();
		final String login = persistent.getLogin();
		final String password = persistent.getPassword();
		final String status = persistent.getStatus();
		int res = -1;
		
		try
		{
			connection = this.daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1,  id);
			statement.setString(2, login);
			statement.setString(3, password);
			statement.setString(4, status);
			res = statement.executeUpdate();
		}
		catch(SQLException ex)
		{
			throw new DAOException("Can't create new user with login = " + persistent.getLogin(), ex);
		}
		finally
		{
			try
			{
				if (statement != null)
				{
					statement.close();
				}
				if (connection != null)
				{
					connection.close();
				}
			}
			catch(SQLException ex)
			{
			}
		}
		
		return res;
	}
	
}
