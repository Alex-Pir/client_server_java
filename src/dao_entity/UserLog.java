package dao_entity;

public class UserLog extends BaseDaoEntity {

	private static final String EMPTY = "";
	
	private long time;
	private String successful;
	private String login;
	
	public UserLog()
	{
		this.time = 0;
		this.successful = EMPTY;
		this.login = EMPTY;
	}
	
	public UserLog(long time, String successful, String login)
	{
		this.time = time;
		this.successful = successful;
		this.login = login;
	}
	
	public long getTime()
	{
		return this.time;
	}
	
	public String getSuccessful()
	{
		return this.successful;
	}
	
	public String getLogin()
	{
		return this.login;
	}
	
	public void setTime(long time)
	{
		this.time = time;
	}
	
	public void setSuccessful(String successful)
	{
		this.successful = successful;
	}
	
	public void setLogin(String login)
	{
		this.login = login;
	}
	
}
