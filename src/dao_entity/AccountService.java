package dao_entity;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AccountService {

	private static ConcurrentMap<Long, UserProfile> userById = new ConcurrentHashMap<>();
	
	public static void add(long ip, UserProfile user)
	{
		userById.put(ip, user);
	}
	
	public static UserProfile getUserById(long id)
	{
		return userById.get(id);
	}
	
	public static void removeUserById(long id)
	{
		userById.remove(id);
	}
}
