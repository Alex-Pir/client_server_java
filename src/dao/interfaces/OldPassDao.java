package dao.interfaces;

import dao_entity.OldPass;
import exceptions.DAOException;

public interface OldPassDao extends Dao<OldPass> {

	OldPass getPass(final String pass) throws DAOException;
}
