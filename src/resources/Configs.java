package resources;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Configs {

	private static final Map<String, Properties> configs = new HashMap<>();
	
	public static void addResource(String key, Properties property)
	{
		configs.put(key, property);
	}
	
	public static Properties getValue(String key)
	{
		return configs.get(key);
	}
}
