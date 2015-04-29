package com.mygdx.game.networking;

import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.net.Socket;
import com.mygdx.game.GameScreen;

public class NetworkHostOutput extends Thread {
	NetworkHost networkHost;
	Socket socket;
	ObjectOutputStream oos;
	Integer playerNum = null;
	
	public NetworkHostOutput(NetworkHost networkHost, Socket socket) {
		this.networkHost = networkHost;
		this.socket = socket;

		try 
		{
			oos = new ObjectOutputStream(socket.getOutputStream());
		} 
		catch (Exception e) 
		{
			System.out.println("Exception in server output code preinitialization:" + e.getMessage());
			e.printStackTrace();
		}
		
		initialize();
		
		start();
	}
	
	public void initialize() {
		//Send the current state of the game as a starting point
		System.out.println("Sending initial game state");
		
		ConcurrentHashMap<String, Object> initializationData = new ConcurrentHashMap<String, Object>();
		playerNum = networkHost.addPlayer(socket);
		initializationData.put("playerNum", playerNum);
		initializationData.put("mapSeed", networkHost.mapSeed);
		
		try {
			oos.writeObject(initializationData);
			oos.flush();
			oos.reset();
		}
		catch (Exception e) {
			System.out.println("Exception in server output initialization:" + e.getMessage());
			e.printStackTrace();
		};
	}
	
	public void run()
	{	
		//FOREVER
		while(true)
		{
			try
			{
				//Run NetworkSystem here
				//GameScreen.networkSystem.update(Gdx.graphics.getDeltaTime());
				
				//Reduce amount of data to be sent:
				//CopyOnWriteArraySet<HashMap<String, Object>> otherEntities = new CopyOnWriteArraySet<HashMap<String, Object>>();
				//for(HashMap<String, Object> allEnt : GameScreen.allEntities ) {
				//	if (!(allEnt.get("playerNum")).equals(playerNum))
				//		otherEntities.add(allEnt);		//Only add entities to send that are from the players besides the one we are sending to
				//}
				
				//if (!otherEntities.isEmpty()) {
					oos.writeObject(GameScreen.allEntities);
					oos.flush();
					oos.reset();
				//}
			}
			catch(Exception e)
			{
				System.out.println("Exception in NetworkHostUpdateHandler reading ois: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
