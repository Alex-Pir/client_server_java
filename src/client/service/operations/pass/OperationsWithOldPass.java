package client.service.operations.pass;

import java.io.BufferedReader;

import client.service.operations.Operations;
import dao.OldPassDaoImpl;
import dao.interfaces.OldPassDao;
import dao_entity.OldPass;
import exceptions.DAOException;

public class OperationsWithOldPass extends Operations {

	private final OldPassDao oldPassDao;
	
	public OperationsWithOldPass(final BufferedReader reader)
	{
		super(reader);
		this.oldPassDao = new OldPassDaoImpl();
	}
	
	public boolean searchPassInOldPass(final String pass) throws DAOException {
		boolean result = false;
		OldPass oldPass = this.oldPassDao.getPass(pass);
		if (oldPass != null) {
			result = true;
		}

		return result;
	}
	
	public OldPass getOldPass(final String pass) throws DAOException
	{
		return this.oldPassDao.getPass(pass);
	}
	
	public void addPassToOldPass(final String pass) throws DAOException
	{
		OldPass oldPass = new OldPass(pass);
		this.oldPassDao.updateValues(oldPass);
	}
}
