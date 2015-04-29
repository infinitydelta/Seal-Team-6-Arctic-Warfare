package projectServer;

import java.util.ArrayList;

public class Test {
	static ArrayList<User> users=new ArrayList<User>();
	public static void main(String [] args){
		users.add(new User("asdf","asdf"));
		System.out.println(users.get(0).isEqual(new User("asdf","asdf")));
	}
}
