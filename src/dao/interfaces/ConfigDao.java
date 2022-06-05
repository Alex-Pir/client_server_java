package dao.interfaces;

import resources.AdminConfig;
import exceptions.DAOException;

/**
 * Интерфейс, содержащий методы для работы с конфигурацией
 * клиентской части сервера.
 * 
 * @author Александр Пирогов
 *
 */
public interface ConfigDao extends Dao<AdminConfig> {

	AdminConfig getConfig() throws DAOException;
	int SetFirstConfig(AdminConfig persistent) throws DAOException;
	int deleteConfig() throws DAOException;
}
