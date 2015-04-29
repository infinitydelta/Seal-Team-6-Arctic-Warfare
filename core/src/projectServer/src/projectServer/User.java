package projectServer;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String userName="Temp";
	String password;
	public User(String userName,String password){
		this.userName=userName;
		this.password=password;
	}
	public String getData(){
		return (userName + " "+ password);
	}
	public boolean isEqual(User user){
		if(user.userName==this.userName&&user.password==this.password){
			return true;
		}else{
			return false;
		}
	}
}
