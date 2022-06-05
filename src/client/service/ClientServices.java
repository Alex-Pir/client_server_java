package client.service;

import interfaces.ClientCommands;
import interfaces.LogCommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import client.service.operations.configs.OperationsWithConfigs;
import client.service.operations.pass.OperationsWithOldPass;
import client.service.operations.users.OperationsWithUsers;
import dao_entity.AccountService;
import dao_entity.ConfigService;
import dao_entity.OldPass;
import dao_entity.UserProfile;
import exceptions.DAOException;

/**
 * Класс предназначен для обработки клиентских запросов.
 * 
 * @author Александр Пирогов
 *
 */
public class ClientServices {

	private static final String RESPONSE_COMMAND = "/r/";
	private static final String PASS_IS_EXSIST = "this_pass_is_exsist";
	private static final String NOT_AUTHORIZATION = "NotAuthorization";
	private static final String SUCCESSFUL = "successful";
	private static final String MISTAKE_ALPHABET = "mistake";
	private static final String ERROR = "error";
	private static final String STATUS_A = "A";
	private static final String STATUS_U = "U";
	private static final String UNKNOWN = "Неизвестно";
	private static final String EMPTY = "";

	private static final String UPPER = "U";
	private static final String LOWWER = "L";
	private static final String NUMBER = "1";
	private static final String LITTLEA = "abcdefghijklmnopqrstuvwxyz";
	private static final String BIGA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUMBERS = "0123456789";

	private static final int SECONDS = 1000;
	private static final int MINUTS = 60;
	private static final int HOURS = 60;
	private static final int DAYS = 24;

	private final BufferedReader reader;
	private final PrintWriter writer;
	private OperationsWithConfigs owc;
	private OperationsWithUsers owu;
	private OperationsWithOldPass owop;
	private final FillLog fillLog;
	private long id;

	/**
	 * 
	 * @param reader
	 *            поток, через который на сервер приходят пользовательские
	 *            данные
	 * @param writer
	 *            поток, через который данные отправляются клиенту
	 */
	public ClientServices(final BufferedReader reader, final PrintWriter writer) {
		this.id = 0;
		this.reader = reader;
		this.writer = writer;
		this.owc = new OperationsWithConfigs(this.reader);
		this.owu = new OperationsWithUsers(this.reader);
		this.owop = new OperationsWithOldPass(this.reader);
		this.fillLog = new FillLog();
	}

	/**
	 * Метод обрабатывает пользовательские запросы.
	 * 
	 * @param command
	 *            запрос пользователя
	 * @return ответ сервера на запрос
	 */
	public String getResponse(final String command) {
		String response = "";
		try {
			switch (command) {
			case ClientCommands.BUE:
				UserProfile userProfile = AccountService.getUserById(this.id);
				if (userProfile != null) {
					this.fillLog.addLogEvent(LogCommands.EXIT,
							userProfile.getLogin());
					AccountService.removeUserById(this.id);
				}
				break;
			case ClientCommands.AUTHORIZATION:
				response = autentificate();
				break;
			case ClientCommands.TIME:
				final long user_time = this.owu.getUserTime() / SECONDS
						/ MINUTS / HOURS / DAYS;
				response += this.owu.getPassTime(user_time);
				break;
			case ClientCommands.ALPHABET:
				response += ConfigService.getAdminConfig().getAlphabet();
				break;
			case ClientCommands.PASS_LENGTH:
				response += ConfigService.getAdminConfig().getLengthPass();
				break;
			case ClientCommands.MIN_TIME_PASS:
				response += ConfigService.getAdminConfig().getMinTime();
				break;
			case ClientCommands.MAX_TIME_PASS:
				response += ConfigService.getAdminConfig().getMaxTime();
				break;
			case ClientCommands.NEW_PASS:
				response += installNewPassword();
				break;
			case ClientCommands.REGISTRATION:
				if (statusBelongAdmin()) {
					response += registration();
					if (response.equals(SUCCESSFUL)) {
						this.fillLog.addLogEvent(LogCommands.REGISTRATION_USER,
								AccountService.getUserById(this.id).getLogin());
					}
				}
				break;
			case ClientCommands.NEW_CONFIG:
				if (statusBelongAdmin()) {
					this.fillLog.addLogEvent(LogCommands.SET_NEW_CONFIGURATION,
							AccountService.getUserById(this.id).getLogin());
					response += this.owc.newConfig();
				}
				break;
			case ClientCommands.GET_LOG:
				if (statusBelongAdmin()) {
					this.fillLog.addLogEvent(LogCommands.GET_LOG,
							AccountService.getUserById(this.id).getLogin());
					response += sendLogToClient();
				}
				break;
			}
		} catch (IOException | DAOException ex) {
			ex.printStackTrace();
		}

		response = RESPONSE_COMMAND + response;

		return response;
	}

	private String autentificate() throws IOException, DAOException {
		String response = NOT_AUTHORIZATION;
		String login = reader.readLine();
		String password = reader.readLine();
		response = this.owu.autentificate(login, password);
		if (response.equals(NOT_AUTHORIZATION)) {
			this.fillLog.addLogEvent(LogCommands.RENDER_IN_ACCESS, UNKNOWN);
		} else if (response.equals(STATUS_A) || response.equals(STATUS_U)) {
			this.id = this.owu.getId();
			this.fillLog.addLogEvent(LogCommands.AUTENTFICATION,
					login);
		}

		return response;
	}

	private String registration() throws IOException, DAOException {
		String response = EMPTY;
		String login = this.reader.readLine();
		String password = reader.readLine();
		String status = reader.readLine();
		if (isPassCorrespondAlphabet(password)) {
			String hashPass = this.owu.getHash(password);
			OldPass oldPass = this.owop.getOldPass(hashPass);
			if (oldPass == null) {
				response = this.owu.registration(login, password, status);
				if (response.equals(SUCCESSFUL)) {
					this.owop.addPassToOldPass(hashPass);
				}
			} else {
				response = PASS_IS_EXSIST;
			}
		} else {
			response = MISTAKE_ALPHABET;
		}
		return response;
	}

	private String sendLogToClient() throws DAOException {
		String response = ERROR;
		ArrayList<String> logs = this.fillLog.getLogs();

		for (String log : logs) {
			writer.println(log);
		}

		response = SUCCESSFUL;

		return response;
	}

	private String installNewPassword() throws IOException, DAOException {
		String response = EMPTY;
		String newPass = this.reader.readLine();
		if (isPassCorrespondAlphabet(newPass)) {
			newPass = this.owu.getHash(newPass);
			if (!this.owop.searchPassInOldPass(newPass)) {
				this.owop.addPassToOldPass(newPass);
				response = this.owu.newPassForUser(newPass);
				this.fillLog.addLogEvent(LogCommands.SET_NEW_PASSWORD,
						AccountService.getUserById(this.id).getLogin());
			} else {
				response = PASS_IS_EXSIST;
			}
		} else {
			response = MISTAKE_ALPHABET;
		}
		return response;
	}

	private boolean isPassCorrespondAlphabet(String pass) {
		boolean flag = true;
		char[] charPass = pass.toCharArray();
		String resultAlphabet = EMPTY;
		String alphabet = ConfigService.getAdminConfig().getAlphabet();
		resultAlphabet = genAlphabet(alphabet);
		for (char c : charPass) {
			if (resultAlphabet.indexOf(c) == -1) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	private String genAlphabet(String alphabet) {
		String resultAlphabet = EMPTY;
		int searchInAlph = -1;
		searchInAlph = alphabet.indexOf(UPPER);
		if (searchInAlph != -1) {
			resultAlphabet += BIGA;
		}
		searchInAlph = alphabet.indexOf(LOWWER);
		if (searchInAlph != -1) {
			resultAlphabet += LITTLEA;
		}
		searchInAlph = alphabet.indexOf(NUMBER);
		if (searchInAlph != -1) {
			resultAlphabet += NUMBERS;
		}
		return resultAlphabet;
	}

	private boolean statusBelongAdmin() {
		boolean result = false;
		String status = AccountService.getUserById(this.id).getStatus();
		if (status.equals(STATUS_A)) {
			result = true;
		}
		return result;
	}
}
