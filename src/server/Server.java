package server;

import interfaces.ServerHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Properties;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import resources.Configs;
import client.Client;

public class Server implements ServerHandler {

	private final int port;
	private final String key_store;
	private final String keyphrase;
	private final String key_store_password;
	
	public Server()
	{
		final Properties sProperty = Configs.getValue(SERVER);
		this.port = Integer.decode(sProperty.getProperty("port"));
		this.key_store = sProperty.getProperty("key_store");
		this.keyphrase = sProperty.getProperty("passphrase");
		this.key_store_password = sProperty.getProperty("key_store_password");
	}
	
	/**
	 * Метод запускает сервер на порту, указанном
	 *  в конфигурационном файле server_config.ini.
	 */
	@Override
	public void startServer() {
		
		try
		{
			// Получить экземпляр хранилища ключей.
	        KeyStore keyStore = KeyStore.getInstance("JKS");
	        FileInputStream fis = new FileInputStream(this.key_store);
	        keyStore.load(fis, this.keyphrase.toCharArray());
	        
	        // Получить диспетчеры ключей базовой реализации для заданного хранилища ключей.
	        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
	        keyManagerFactory.init(keyStore, this.key_store_password.toCharArray());
	        KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
	        
	        // Получить доверенные диспетчеры базовой реализации.
	        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
	        trustManagerFactory.init(keyStore);
	        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
	        
	        // Получить защищенное случайное число.
	        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
	        
	        // Создание SSL контекста
	        SSLContext sslContext = SSLContext.getInstance("SSLv3");
	        sslContext.init(keyManagers, trustManagers, secureRandom);
	        
	        // Создание фабрики SSL сокетов.
	        SSLServerSocketFactory sslSocketFactory = 
	                sslContext.getServerSocketFactory();
			SSLServerSocket server = (SSLServerSocket) sslSocketFactory.createServerSocket(this.port);
			server.setNeedClientAuth(true);
			System.out.println("Server started on port: " + port);
			while (true)
			{
				try
				{
					SSLSocket clientSocket = (SSLSocket) server.accept();
				    Client thread = new Client(clientSocket);
					thread.start();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
