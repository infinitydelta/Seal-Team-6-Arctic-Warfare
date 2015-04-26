package projectServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	ArrayList<User> users=new ArrayList<User>();
	ServerSocket ss;
	public Server(){
		try{
			System.out.println("Starting Server");
			ss=new ServerSocket(6789);
			while(true){
				System.out.println("Waiting for connection...");
				Socket s = ss.accept();
				System.out.println("Accepted connection: " + s.getInetAddress() + ":" + s.getPort());

				ServerThread st = new ServerThread(this, s);

				st.start();
			}
		}catch(IOException ioe){
			System.out.println("IOException thrown in ServerConstructor: "+ioe.getMessage());
		}finally{
			if(ss!=null){
				try {
					ss.close();
				} catch (IOException ioe) {
					System.out.println("IOException thrown in ServerConstructor: "+ioe.getMessage());
				}
			}
		}
	}
	public static void main(String [] args){
		new Server();
	}
}

class ServerThread extends Thread{
	Server server;
	Socket s;
	public ServerThread(Server server, Socket s){
		this.s=s;
		this.server=server;


	}
	public void run(){
		try {
			while(true){
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				String action=(String)ois.readObject();
				if(action.equals("Login")){
					String username=(String)ois.readObject();
					String password=(String)ois.readObject();
					User temp=new User(username,password);
					temp.getData();
					ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
					boolean tempBool=false;
					for(int i=0;i<server.users.size();i++){
						
						if(server.users.get(i).userName.equals(temp.userName)&&server.users.get(i).password.equals(temp.password)){
							System.out.println("Reached");
							tempBool=true;
							break;
						}
					}
					oos.writeObject(tempBool);
					oos.flush();
				}else if(action.equals("New User")){
					String username=(String)ois.readObject();
					String password=(String)ois.readObject();
					User temp=new User(username,password);
					temp.getData();
					ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
					if(server.users.contains(temp)){
						oos.writeObject(false);
					}else{
						server.users.add(temp);
						System.out.println("added");
						oos.writeObject(true);
					}
					oos.flush();
				}
			}
		} catch (IOException ioe) {
			System.out.println("IOException thrown in ServerThread.run(): "+ioe.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException in ServerThread.run(): " +e.getMessage());
		}

	}
}