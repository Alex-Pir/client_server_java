package dao.interfaces;

import dao_entity.BaseDaoEntity;
import exceptions.DAOException;
/**
 * Интерфейс, содержащий общие методы для работы с базой.
 * 
 * @author Александр пирогов
 *
 * @param <T> любой наследник класса BaseDaoEnyity
 */
public interface Dao <T extends BaseDaoEntity> {
	
	int updateValues(T persistent) throws DAOException;
}
