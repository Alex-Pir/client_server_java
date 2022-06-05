package dao_entity;

public class OldPass extends BaseDaoEntity {

	private String pass;
	
	public OldPass(final String pass)
	{
		this.pass = pass;
	}
	
	public String getPass()
	{
		return this.pass;
	}
}
