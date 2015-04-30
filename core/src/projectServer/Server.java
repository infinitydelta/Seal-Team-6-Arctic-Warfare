package projectServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	ArrayList<User> users=new ArrayList<User>();
	ServerSocket ss;
	public Server(){
		try{
			parseFile();
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
	public void parseFile(){
		try {
			BufferedReader br=new BufferedReader(new FileReader("Users.txt"));
			String [] tokens;
			String toToken;
			while((toToken=br.readLine())!=null){
				tokens=toToken.split(" ");
				System.out.println(tokens[0]+ " "+tokens[1]);
				users.add(new User(tokens[0],tokens[1]));
			}
			br.close();
		} catch (FileNotFoundException e) {
			File file = new File("Users.txt");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
					boolean flag=false;
					for(int i=0;i<username.length();i++){
						if(username.charAt(i)==' '){
							flag=true;
						}
					}
					if(flag){
						continue;
					}
					password=HashTest.hash(password);
					User temp=new User(username,password);
					ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
					boolean tempBool=false;
					for(int i=0;i<server.users.size();i++){						
						if(server.users.get(i).userName.equals(temp.userName)&&server.users.get(i).password.equals(temp.password)){
							tempBool=true;
							break;
						}
					}
					oos.writeObject(tempBool);
					oos.flush();
				}else if(action.equals("New User")){
					String username=(String)ois.readObject();
					String password=(String)ois.readObject();
					boolean flag=false;
					for(int i=0;i<username.length();i++){
						if(username.charAt(i)==' '){
							flag=true;
						}
					}
					if(flag){
						continue;
					}
					password=HashTest.hash(password);
					User temp=new User(username,password);
					temp.getData();
					ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
					if(server.users.contains(temp)){
						oos.writeObject(false);
					}else{
						server.users.add(temp);
						oos.writeObject(true);
					}
					oos.flush();
				}
			}
		} catch (IOException ioe) {
			System.out.println("IOException thrown in ServerThread.run(): "+ioe.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException in ServerThread.run(): " +e.getMessage());
		} finally {
			writeBack();
		}
	}
	public void writeBack(){
		try {
			FileWriter fw=new FileWriter("Users.txt");
			PrintWriter pw=new PrintWriter(fw);
			for(int i=0;i<server.users.size();i++){
				User temp=server.users.get(i);
				pw.write(temp.getData());
				pw.write("\n");
				pw.flush();
			}
			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}