package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import resources.AdminConfig;
import dao.interfaces.ConfigDao;
import exceptions.DAOException;

public class ConfigDaoImpl implements ConfigDao {

	private final DaoFactory daoFactory;
	
	public ConfigDaoImpl()
	{
		this.daoFactory = DaoFactory.getInstance();
	}
	
	@Override
	public int updateValues(AdminConfig persistent) throws DAOException 
	{
		String sql = "UPDATE CONFIGS SET ID = ?, MIN_TIME = ?, MAX_TIME = ?, PASS_LENGTH = ?, ALPHABET = ?";
		Connection connection = null;
		PreparedStatement statement = null;
		
		final long id = persistent.getId();
		final int min_time = persistent.getMinTime();
		final int max_time = persistent.getMaxTime();
		final int pass_length = persistent.getLengthPass();
		final String alphabet = persistent.getAlphabet();
		int res = 0;
		
		try
		{
			connection = this.daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			statement.setInt(2, min_time);
			statement.setInt(3, max_time);
			statement.setInt(4, pass_length);
			statement.setString(5, alphabet);
			
			res = statement.executeUpdate();
		}
		catch(SQLException ex)
		{
			throw new DAOException("Can't create new admin config", ex);
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
	public int SetFirstConfig(AdminConfig persistent) throws DAOException 
	{
		String sql = "INSERT INTO CONFIGS (ID, MIN_TIME, MAX_TIME, PASS_LENGTH, ALPHABET) VALUES (?, ?, ?, ?, ?)";
		Connection connection = null;
		PreparedStatement statement = null;
		
		final long id = persistent.getId();
		final int min_time = persistent.getMinTime();
		final int max_time = persistent.getMaxTime();
		final int pass_length = persistent.getLengthPass();
		final String alphabet = persistent.getAlphabet();
		int res = 0;
		
		try
		{
			connection = this.daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			statement.setInt(2, min_time);
			statement.setInt(3, max_time);
			statement.setInt(4, pass_length);
			statement.setString(5, alphabet);
			
			res = statement.executeUpdate();
		}
		catch(SQLException ex)
		{
			throw new DAOException("Can't create new admin config", ex);
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
	public AdminConfig getConfig() throws DAOException
	{
		String sql = "SELECT * FROM CONFIGS";
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		AdminConfig adminConfig = null;
		
		try
		{
			connection = this.daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			
			resultSet.next();
			adminConfig = new AdminConfig(resultSet.getLong("ID"), resultSet.getInt("MIN_TIME"), resultSet.getInt("MAX_TIME"), resultSet.getInt("PASS_LENGTH"), resultSet.getString("ALPHABET"));
		}
		catch(SQLException ex)
		{
			throw new DAOException("Can't get admin config", ex);
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
		
		return adminConfig;
	}

	@Override
	public int deleteConfig() throws DAOException
	{
		String sql = "DELETE CONFIGS";
		Connection connection = null;
		PreparedStatement statement = null;
		int result = 0;
		
		try
		{
			connection = this.daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			result = statement.executeUpdate();
		}
		catch(SQLException ex)
		{
			throw new DAOException("Can't delete config", ex);
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
		
		return result;
	}
}
