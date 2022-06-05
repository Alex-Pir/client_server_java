package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.interfaces.OldPassDao;
import dao_entity.OldPass;
import exceptions.DAOException;

public class OldPassDaoImpl implements OldPassDao {

	private final DaoFactory daoFactory;
	
	public OldPassDaoImpl()
	{
		this.daoFactory = DaoFactory.getInstance();
	}
	
	@Override
	public int updateValues(OldPass persistent) throws DAOException {
		// TODO Auto-generated method stub
		final String sql = "INSERT INTO OLD_PASS (PASS) VALUES (?);";
		final String pass = persistent.getPass();
		int result = 0;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try
		{
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, pass);
			result = statement.executeUpdate();
		}
		catch(SQLException ex)
		{
			throw new DAOException("Can't add password in table \"OLD_PASS\" ", ex);
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
				ex.printStackTrace();
			}
		}
		
		return result;
	}

	@Override
	public OldPass getPass(final String pass) throws DAOException {
		// TODO Auto-generated method stub
		final String sql = "SELECT * FROM OLD_PASS WHERE PASS = ?";
		OldPass old_pass = null;
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try
		{
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, pass);
			resultSet = statement.executeQuery();
			
			while(resultSet.next())
			{
				String old_pass_value = resultSet.getString("PASS");
				if (old_pass_value.equals(pass))
				{
					old_pass = new OldPass(pass);
					break;
				}
			}
			
		}
		catch(SQLException ex)
		{
			throw new DAOException("Can't get password from table \"OLD_PASS\" ", ex);
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
				ex.printStackTrace();
			}
		}
		
		return old_pass;
	}

	
	
}
