package server.init.advisor;

import interfaces.ClientCommands;
import interfaces.LogCommands;

import java.io.BufferedReader;
import java.io.IOException;

import server.init.commands.InitCommands;
import client.service.FillLog;
import client.service.operations.configs.OperationsWithConfigs;
import client.service.operations.pass.OperationsWithOldPass;
import client.service.operations.users.OperationsWithUsers;
import dao_entity.ConfigService;
import dao_entity.OldPass;
import exceptions.DAOException;

public class InitAdvisor {

	private static final String LOGIN = "INIT";
	private static final String PASS_IS_EXSIST = "this_pass_is_exsist";
	private static final String SUCCESSFUL = "successful";
	private static final String R = "/r/";
	private static final String EMPTY = "";
	private static final String MISTAKE_ALPHABET = "mistake";

	private static final String UPPER = "U";
	private static final String LOWWER = "L";
	private static final String NUMBER = "1";
	private static final String LITTLEA = "abcdefghijklmnopqrstuvwxyz";
	private static final String BIGA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUMBERS = "0123456789";

	private final BufferedReader reader;
	private final OperationsWithConfigs owc;
	private final OperationsWithUsers owu;
	private final OperationsWithOldPass owop;
	private final FillLog fillLog;

	public InitAdvisor(final BufferedReader reader) {
		this.reader = reader;
		this.owc = new OperationsWithConfigs(this.reader);
		this.owu = new OperationsWithUsers(this.reader);
		this.owop = new OperationsWithOldPass(this.reader);
		this.fillLog = new FillLog();
	}

	public String getResponse(String command) {
		String response = EMPTY;
		try {
			switch (command) {
			case InitCommands.SET_CONFIG:
				response = setNewConfig();
				break;
			case InitCommands.ADD_ADMIN:
				response = addNewAdmin();
				break;
			case ClientCommands.ALPHABET:
				response += ConfigService.getAdminConfig().getAlphabet();
				break;
			case ClientCommands.PASS_LENGTH:
				response += ConfigService.getAdminConfig().getLengthPass();
				break;
			}
		} catch (IOException | DAOException ex) {
			ex.printStackTrace();
		}
		response = R + response;
		return response;
	}

	private String setNewConfig() throws IOException, DAOException {
		String response = EMPTY;
		response = this.owc.deleteConfig();
		response = this.owc.firstConfig();
		if (response.equals(SUCCESSFUL)) {
			this.fillLog
					.addLogEvent(LogCommands.SET_FIRST_CONFIGURATION, LOGIN);
		}

		return response;
	}

	private String addNewAdmin() throws IOException, DAOException {
		final String status = "A";
		String response = EMPTY;
		String login = this.reader.readLine();
		String password = this.reader.readLine();
		if (isPassCorrespondAlphabet(password)) {
			String hashPass = this.owop.getHash(password);
			OldPass oldPass = this.owop.getOldPass(hashPass);
			if (oldPass != null) {
				response = PASS_IS_EXSIST;
			} else {
				response = this.owu.registration(login, password, status);
				if (response.equals(SUCCESSFUL)) {
					this.owop.addPassToOldPass(this.owop.getHash(password));
					this.fillLog.addLogEvent(
							LogCommands.SET_FIRST_ADMINISTRATION, LOGIN);
				}
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

}
