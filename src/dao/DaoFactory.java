package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.h2.jdbcx.JdbcConnectionPool;

import resources.Configs;

public class DaoFactory {

	private static JdbcConnectionPool pool;
	private static DaoFactory daoFactory;
	
	private DaoFactory()
	{	
		final Properties dbProperty = Configs.getValue("database");
		
		String url = dbProperty.getProperty("url");
		String user = dbProperty.getProperty("user");
		String password = dbProperty.getProperty("password");
		int max_connection = Integer.decode(dbProperty.getProperty("max_connection"));
		
		pool = JdbcConnectionPool.create(url, user, password);
		pool.setMaxConnections(max_connection);
	}
	
	
	
	public static synchronized DaoFactory getInstance()
	{
		if (daoFactory == null)
		{
			daoFactory = new DaoFactory();
		}
		
		return daoFactory;
	}
	
	public Connection getConnection()
	{
		try {
			return pool.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
