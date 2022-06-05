package server;

import interfaces.ClientCommands;
import interfaces.ServerHandler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
import server.init.advisor.InitAdvisor;

public class InitServer implements ServerHandler {

	private final int port;
	private final String key_store;
	private final String keyphrase;
	private final String key_store_password;

	public InitServer() {
		final Properties property = Configs.getValue(INIT_SERVER);
		this.port = Integer.parseInt(property.getProperty("port"));
		this.key_store = property.getProperty("key_store");
		this.keyphrase = property.getProperty("passphrase");
		this.key_store_password = property.getProperty("key_store_password");
	}

	@Override
	public void startServer() {
		SSLServerSocket server = null;
		SSLSocket socket = null;
		try {

			KeyStore keyStore = KeyStore.getInstance("JKS");
			FileInputStream fis = new FileInputStream(this.key_store);
			keyStore.load(fis, this.keyphrase.toCharArray());

			KeyManagerFactory keyFactory = KeyManagerFactory
					.getInstance("SunX509");
			keyFactory.init(keyStore, this.key_store_password.toCharArray());
			KeyManager[] keyManager = keyFactory.getKeyManagers();

			TrustManagerFactory trustFactory = TrustManagerFactory
					.getInstance("SunX509");
			trustFactory.init(keyStore);
			TrustManager[] trustManager = trustFactory.getTrustManagers();

			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

			SSLContext sslContext = SSLContext.getInstance("SSLv3");
			sslContext.init(keyManager, trustManager, random);

			SSLServerSocketFactory sslFactory = sslContext
					.getServerSocketFactory();

			server = (SSLServerSocket) sslFactory.createServerSocket(this.port);
			server.setNeedClientAuth(true);
			System.out.println("Server started on port: " + this.port);
			socket = (SSLSocket) server.accept();
			String[] supported = socket.getSupportedCipherSuites();
			socket.setEnabledCipherSuites(supported);
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(socket.getOutputStream(),
						true);
				handlerRequests(reader, writer);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
				if (server != null) {
					server.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	private void handlerRequests(BufferedReader reader, PrintWriter writer)
			throws IOException {
		InitAdvisor initAdvisor = new InitAdvisor(reader);
		String command = EMPTY;
		String response = EMPTY;
		while ((command = reader.readLine()) != null) {
			if (command.equals(ClientCommands.BUE)) {
				reader.close();
				writer.close();
				break;
			}
			response = initAdvisor.getResponse(command);
			writer.println(response);
		}
	}
}
