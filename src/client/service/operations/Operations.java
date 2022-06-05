package client.service.operations;

import java.io.BufferedReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class Operations {

	private static final String ALGORITHM = "SHA-1";
	
	protected static final String SUCCESSFUL = "successful";
	protected static final String ERROR = "error";
	
	protected final BufferedReader reader;
	
	public Operations(final BufferedReader reader)
	{
		this.reader = reader;
	}
	
	public String getHash(final String strForHash) {
		String result = null;
		byte[] byteForHash = strForHash.getBytes();
		try {
			MessageDigest messDigest = MessageDigest.getInstance(ALGORITHM);
			messDigest.update(byteForHash);
			byte[] hashSymbols = messDigest.digest();
			result = hashToHexString(hashSymbols);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return result;
	}

	private String hashToHexString(final byte[] hash) {
		final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'a', 'b', 'c', 'd', 'e', 'f' };
		final int length = hash.length;
		char[] str = new char[length * 2];
		String hex = null;

		for (int i = 0; i < length; i++) {
			str[i * 2] = hexArray[(hash[i] >>> 4) & 0xf];
			str[i * 2 + 1] = hexArray[(hash[i]) & 0xf];
		}

		hex = new String(str);

		return hex;
	}
	
}
