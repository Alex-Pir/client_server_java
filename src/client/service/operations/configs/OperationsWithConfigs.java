package client.service.operations.configs;

import java.io.BufferedReader;
import java.io.IOException;

import resources.AdminConfig;
import client.service.operations.Operations;
import dao.ConfigDaoImpl;
import dao.interfaces.ConfigDao;
import dao_entity.ConfigService;
import exceptions.DAOException;

public class OperationsWithConfigs extends Operations {

	private final ConfigDao configDao;

	public OperationsWithConfigs(final BufferedReader reader) {
		super(reader);
		this.configDao = new ConfigDaoImpl();
	}

	public String newConfig() throws IOException, DAOException {
		String response = ERROR;
		int isUpdate = -1;
		long id = 1;
		String min_pass_time = reader.readLine();
		String max_pass_time = reader.readLine();
		String pass_length = reader.readLine();
		String alphabet = reader.readLine();

		AdminConfig adminConfig = new AdminConfig(id,
				Integer.parseInt(min_pass_time),
				Integer.parseInt(max_pass_time), Integer.parseInt(pass_length),
				alphabet);
		isUpdate = this.configDao.updateValues(adminConfig);
		if (isUpdate == 1) {
			ConfigService.setAdminConfig(adminConfig);
			response = SUCCESSFUL;
		}

		return response;
	}

	public String firstConfig() throws IOException, DAOException {
		String response = ERROR;
		int isUpdate = -1;
		long id = 1;
		String min_pass_time = reader.readLine();
		String max_pass_time = reader.readLine();
		String pass_length = reader.readLine();
		String alphabet = reader.readLine();

		AdminConfig adminConfig = new AdminConfig(id,
				Integer.parseInt(min_pass_time),
				Integer.parseInt(max_pass_time), Integer.parseInt(pass_length),
				alphabet);
		isUpdate = this.configDao.SetFirstConfig(adminConfig);
		if (isUpdate == 1) {
			ConfigService.setAdminConfig(adminConfig);
			response = SUCCESSFUL;
		}

		return response;
	}

	public String deleteConfig() throws IOException, DAOException {
		String response = ERROR;
		int isDelete = this.configDao.deleteConfig();
		if (isDelete == 1) {
			response = SUCCESSFUL;
		}

		return response;
	}
}
