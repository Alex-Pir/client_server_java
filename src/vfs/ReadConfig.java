package vfs;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import exceptions.ReadConfigException;

public class ReadConfig {
	
	public static Properties readConfig(String fileName) throws ReadConfigException, URISyntaxException
	{
		Properties property = new Properties();
		//File config_file = new File(ReadConfig.class.getClassLoader().getResource(fileName).toURI());
		try(FileInputStream fis = new FileInputStream(fileName))
		{
			property.load(fis);
		}
		catch(IOException ex)
		{
			throw new ReadConfigException("Can't read config: " + fileName + ".", ex);
		}
		
		return property;
	}
	
}