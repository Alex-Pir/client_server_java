package dao.interfaces;

import dao_entity.UserProfile;
import exceptions.DAOException;
/**
 * Интерфейс, содержащий методы для работы с пользователями через базу.
 * 
 * @author Александр Пирогов
 *
 */
public interface UserDao extends Dao<UserProfile> {

	UserProfile getByLoginAndPass(final String login, final String password) throws DAOException;
	int setNewPassById(final long id,final String password) throws DAOException;
	
}
