package dao_entity;

public class UserProfile extends BaseDaoEntity{
	
	private final String login;
	private final String password;
	private final String status;
	private final long time;
	
	public UserProfile(String login, String password, String status)
	{
		this.id = 0;
		this.login = login;
		this.password = password;
		this.status = status;
		this.time = 0;
	}
	
	public UserProfile(long id, String login, String password, String status)
	{
		this.id = id;
		this.login = login;
		this.password = password;
		this.status = status;
		this.time = System.currentTimeMillis();
	}
	
	public UserProfile(long id, String login, String password, String status, long time)
	{
		this.id = id;
		this.login = login;
		this.password = password;
		this.status =status;
		this.time = time;
	}
	
	public String getStatus()
	{
		return this.status;
	}
	
	public String getLogin()
	{
		return this.login;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public long getTime()
	{
		return this.time;
	}
	
	@Override
	public String toString()
	{
		return "User = {" + this.id + ", " + this.login +  this.status + "};";
	}
}
