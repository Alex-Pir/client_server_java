package resources;

import dao_entity.BaseDaoEntity;

/**
 * Класс содержит необходимую конфигурацию для
 * работы с пользователями
 * @author Александр Пирогов
 *
 */
public class AdminConfig extends BaseDaoEntity{

	private final int max_time;
	private final int min_time;
	private final int length_pass;
	private final String alphabet;
	
	public AdminConfig(final long id, final int min_time, final int max_time, final int length_pass, final String alphabet)
	{
		this.id = id;
		this.min_time = min_time;
		this.max_time = max_time;
		this.length_pass = length_pass;
		this.alphabet = alphabet;
	}
	
	
	public int getMinTime()
	{
		return this.min_time;
	}
	
	public int getMaxTime()
	{
		return this.max_time;
	}
	
	public int getLengthPass()
	{
		return this.length_pass;
	}
	
	public String getAlphabet()
	{
		return this.alphabet;
	}
	
	public String toString()
	{
		return "Config = {" + this.id + ", " + this.min_time + " - " + this.max_time + ", " + this.length_pass + "}";
	}
}
