package tests;

import static org.junit.Assert.assertEquals;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class SHA1Test {

	private String getHash(final String strForHash)
	{
		String result = null;
		byte[] byteForHash = strForHash.getBytes();
		try 
		{
			MessageDigest messDigest = MessageDigest.getInstance("SHA-1");
			messDigest.update(byteForHash);
			byte[] hashSymbols = messDigest.digest();
			result = hashToHexString(hashSymbols);
		} catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	private String hashToHexString(final byte[] hash)
	{
		final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		final int length = hash.length;
		char[] str = new char[length * 2];
		String hex = null;
		
		for (int i = 0; i < length; i++)
		{
			str[i * 2] = hexArray[(hash[i] >>> 4) & 0xf];
			str[i * 2 + 1] = hexArray[(hash[i]) & 0xf];
		}
		
		hex = new String(str);
		
		return hex;
	}
	
	@Test
	public void test() {
		String hash = getHash("12345");
		assertEquals("8cb2237d0679ca88db6464eac60da96345513964", hash);
	}

}
