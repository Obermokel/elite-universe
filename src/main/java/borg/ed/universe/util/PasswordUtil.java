package borg.ed.universe.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class PasswordUtil {

	/**
	 * Converts a clear text String to the MD5 hash with prepadded 0s.
	 * <p>
	 * Example: The clear text &quot;foobar&quot; results in the MD5 hash &quot;3858F62230AC3C915F30<b>0</b>C664312C63F&quot;. If 0s were not prepadded the MD5 hash would be
	 * &quot;3858F62230AC3C915F30C664312C63F&quot;.
	 * </p>
	 *
	 * @param cleartext
	 *            Clear text string
	 * @return Uppercase MD5 hash in hexadecimal notation or <code>null</code> if <code>cleartext</code> was <code>null</code>
	 */
	public static String md5(String cleartext) {
		if (cleartext == null) {
			return null;
		} else {
			try {
                // Compute the MD5 hash
                byte[] md5Hash = PasswordUtil.md5(cleartext.getBytes("UTF-8"));

                // Convert to a hex string with prepadded 0s
                StringBuilder md5Hex = new StringBuilder();
                for (byte b : md5Hash) {
                    md5Hex.append(Integer.toHexString((0xF0 & b) >> 4));
                    md5Hex.append(Integer.toHexString(0x0F & b));
                }

                // Return
                return md5Hex.toString().toUpperCase();
            } catch (UnsupportedEncodingException e) {
				throw new RuntimeException("UTF-8 encoding is not supported");
			}
		}
	}

    public static byte[] md5(byte[] data) {
        if (data == null) {
            return null;
        } else {
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(data);
                return digest.digest();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("MD5 algorith is not available");
            }
        }
    }

}
