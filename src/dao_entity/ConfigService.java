package dao_entity;

import resources.AdminConfig;

public class ConfigService {

	private static AdminConfig adminConfig;
	
	public static void setAdminConfig(AdminConfig config)
	{
		adminConfig = config;
	}
	
	public static AdminConfig getAdminConfig()
	{
		return adminConfig;
	}
	
}
