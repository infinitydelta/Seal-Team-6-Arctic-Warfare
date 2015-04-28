package projectServer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashTest {
	public static String hash(String password){
		StringBuffer sb=new StringBuffer();
        try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			password="Passoword";
			md.update(password.getBytes());
			byte [] passData=md.digest();
		   	for (int i=0;i<passData.length;i++) {
	    		String hex=Integer.toHexString(0xff & passData[i]);
	   	     	if(hex.length()==1) sb.append('0');
	   	     	sb.append(hex);
	    	}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        return sb.toString();
	}
}
