package projectServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {
	Scanner scan=new Scanner(System.in);
	public Client(){
		try{
			Socket s=new Socket("localhost", 6789);
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			while(true){
				System.out.println("Username: ");
				String username=scan.nextLine();
				System.out.println("Password: ");
				String password=scan.nextLine();
				User temp=new User(username,password);
				oos.writeObject(temp);
				oos.flush();
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				boolean flag=(boolean)ois.readObject();
				if(flag){
					System.out.println("Logged in");
					break;
				}else{
					System.out.println("Login fail");
				}
			}
		}catch(IOException ioe){
			System.out.println("IOException thrown in Client Constructor: " +ioe.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run(){

	}
	public static void main(String [] args){
		new Client();
	}
}
