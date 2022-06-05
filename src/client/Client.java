package client;

import interfaces.ClientCommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import client.service.ClientServices;
/**
 * 
 * Класс для работы с клиентом.
 * Предназначен для получения входящих сообщений от клиента
 * и отправки ему результатов выполненных запросов
 * @author Александр Пирогов
 *
 */
public class Client extends Thread{
	
	private static final int MAX_CONNECT_VALUE = 3;
	
	private final Socket socket;
	
	private ClientServices clientServices;
	private BufferedReader reader;
	private PrintWriter writer;
	private int maxConnect;

	/**
	 * 
	 * @param socket сокет, на котором будет строиться взаимодействие
	 * 
	 */
	
	public Client(final Socket socket)
	{
		this.socket = socket;
		this.maxConnect = 0;
		try {
			
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.writer = new PrintWriter(socket.getOutputStream(), true);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.clientServices = new ClientServices(this.reader, this.writer);
	}
	
	@Override
	public void run() {
		
		exchangeMessage();
		
	}

	/**
	 * Обработка сообщений клиента
	 */
	private void exchangeMessage()
	{
		String command = "";
		String response = "";
		try {
			while ((command = this.reader.readLine()) != null)
			{
				if (command.equals(ClientCommands.BUE))
				{
					this.clientServices.getResponse(command);
					this.reader.close();
					this.writer.close();
					this.socket.close();
					break;
				}
				response = this.clientServices.getResponse(command);
				this.writer.println(response);
				if (response.equals("/r/NotAuthorization"))
				{
					notAuthorization();
				}
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
		}
	}
	
	private void notAuthorization()
	{
		this.maxConnect++;
		if (this.maxConnect == MAX_CONNECT_VALUE)
		{
			try
			{
				if (!this.socket.isClosed())
				{
					this.socket.close();
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
}
