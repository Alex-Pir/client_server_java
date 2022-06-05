package client.service.operations.users;

import java.io.BufferedReader;
import java.io.IOException;

import resources.AdminConfig;
import client.service.operations.Operations;
import dao.UserDaoImpl;
import dao.interfaces.UserDao;
import dao_entity.AccountService;
import dao_entity.ConfigService;
import dao_entity.UserProfile;
import exceptions.DAOException;

public class OperationsWithUsers extends Operations {

	private static final String LOGIN_IS_EXSIST = "this_login_is_exsist";
	private static final String NOT_AUTHORIZATION = "NotAuthorization";
	private static final int MANY_TIME = -1;

	private final UserDao userDao;
	private long id;

	public OperationsWithUsers(final BufferedReader reader) {
		super(reader);
		this.userDao = new UserDaoImpl();
		this.id = 0;
	}

	public String autentificate(String login, String password)
			throws IOException, DAOException {
		UserProfile userProfile = null;
		String result = NOT_AUTHORIZATION;
		password = getHash(password);
		userProfile = this.userDao.getByLoginAndPass(login, password);
		if (userProfile != null) {
			this.id = userProfile.getId();
			AccountService.add(this.id, userProfile);
			result = userProfile.getStatus();
		}
		return result;
	}

	public long getUserTime() {
		long result = 0;
		final UserProfile userProfile = AccountService.getUserById(this.id);
		final long presentTime = System.currentTimeMillis();
		final long userTime = userProfile.getTime();
		result = presentTime - userTime;

		return result;
	}

	public long getPassTime(final long user_time) {
		final AdminConfig admin_config = ConfigService.getAdminConfig();
		final int min_time = admin_config.getMinTime();
		final int max_time = admin_config.getMaxTime();

		long result_time = 0;

		if (user_time >= max_time) {
			result_time = 0;
		} else {
			if (user_time >= min_time) {
				result_time = max_time - user_time;
			} else {
				result_time = MANY_TIME;
			}
		}

		return result_time;
	}

	public String newPassForUser(final String newPass) throws DAOException {
		String response = ERROR;
		UserProfile userProfile = AccountService.getUserById(this.id);
		final long id = userProfile.getId();
		final String login = userProfile.getLogin();
		final String status = userProfile.getStatus();
		String pass = newPass;
		if (pass != null) {
			pass = getHash(newPass);
			userProfile = new UserProfile(id, login, newPass, status);
			this.userDao.setNewPassById(id, newPass);
			AccountService.add(this.id, userProfile);
			response = SUCCESSFUL;
		}
		return response;
	}

	public String registration(String login, String new_pass, String status)
			throws IOException {
		int result = -1;
		String response = "0";
		long id = System.currentTimeMillis();
		new_pass = getHash(new_pass);

		try {
			UserProfile userProfile = new UserProfile(id, login, new_pass,
					status);
			result = this.userDao.updateValues(userProfile);
			if (result == -1) {
				response = LOGIN_IS_EXSIST;
			} else {
				response = SUCCESSFUL;
			}
		} catch (DAOException ex) {
			response = LOGIN_IS_EXSIST;
		}
		return response;
	}
	
	public long getId() {
		return this.id;
	}

	public void removeUser() {
		AccountService.removeUserById(this.id);
	}

}
