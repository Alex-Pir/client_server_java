package main;

import java.net.URISyntaxException;

import resources.AdminConfig;
import resources.Configs;
import server.InitServer;
import server.Server;
import vfs.ReadConfig;
import dao.ConfigDaoImpl;
import dao.interfaces.ConfigDao;
import dao_entity.ConfigService;
import exceptions.DAOException;
import exceptions.ReadConfigException;

public class Main {

	private static final String SERVER_CONFIG = "configs/server_config.ini";
	private static final String DATABASE_CONFIG = "configs/database_config.ini";
	private static final String INIT_SERVER_CONFIG = "configs/init_server_config.ini";
	private static final String DATABASE = "database";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean flag = initComponents();
		while (!flag)
		{
			new InitServer().startServer();
			flag = initComponents();
		}
		new Server().startServer();
	}
	
	private static boolean initComponents()
	{
		boolean flag = false;
		try 
		{
			Configs.addResource(Server.SERVER, ReadConfig.readConfig(SERVER_CONFIG));
			Configs.addResource(DATABASE, ReadConfig.readConfig(DATABASE_CONFIG));
			Configs.addResource(Server.INIT_SERVER, ReadConfig.readConfig(INIT_SERVER_CONFIG));
			
			ConfigDao configDao = new ConfigDaoImpl();
			AdminConfig ac = configDao.getConfig();
			ConfigService.setAdminConfig(ac);
			
			flag = true;
		} 
		catch (ReadConfigException | DAOException | URISyntaxException e) 
		{
			System.out.println("WARNING: There are no configuration files");
			System.out.println("WARNING: Server is waiting configuration");
			flag = false;
		}
		
		return flag;
	}
}
