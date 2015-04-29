package com.mygdx.game.networking;

import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.mygdx.game.GameScreen;
import com.mygdx.game.utility.Factory;

public class NetworkHostInput extends Thread {
	NetworkHost networkHost;
	Socket socket;
	ObjectInputStream ois;
	Integer playerNum = null;
	
	public NetworkHostInput(NetworkHost networkHost, Socket socket) {
		this.networkHost = networkHost;
		this.socket = socket;
		try {
			this.ois = new ObjectInputStream(socket.getInputStream());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		start();
	}
	
	public void run()
	{	
		//FOREVER
		while(true)
		{
			try
			{
				Object o = ois.readObject();
				
				if (o.getClass() == CopyOnWriteArraySet.class) {
					//System.out.println("Receiving (" + ((CopyOnWriteArraySet<HashMap<String, Object>>)o).size() + "):" + o.toString());
					
					CopyOnWriteArraySet<ConcurrentHashMap<String, Object>> oCasted = ((CopyOnWriteArraySet<ConcurrentHashMap<String, Object>>)o);
					
					//Get incoming player number
					if (playerNum == null) {
						for(ConcurrentHashMap<String, Object> oEnt : oCasted ) {
							playerNum = (Integer)oEnt.get("playerNum");
							if (playerNum != null)
								break;
						}
					}
					
					//Remove any entities with playerNum matching incoming player from allEntities, and clone all of these entities to oldEntities
					CopyOnWriteArraySet<ConcurrentHashMap<String, Object>> oldEntities = new CopyOnWriteArraySet<ConcurrentHashMap<String, Object>>();
					for(ConcurrentHashMap<String, Object> allEnt : GameScreen.allEntities ) {
						if (((Integer)allEnt.get("playerNum")).equals(playerNum)) {
							ConcurrentHashMap<String, Object> allEntClone = allEnt;
							oldEntities.add(allEntClone);
							GameScreen.allEntities.remove(allEnt);
						}
					}
					
					//Add all incoming entities to allEntities
					for(ConcurrentHashMap<String, Object> incEnt : oCasted ) {
						GameScreen.allEntities.add(incEnt);
					}
					
					//Create an instance of every allEntity that is not in oldEntity but belongs to the incoming player
					for(ConcurrentHashMap<String, Object> allEnt : GameScreen.allEntities ) {
						boolean oldEntExists = false;
						for(ConcurrentHashMap<String, Object> oldEnt : oldEntities ) {
							if (allEnt.get("playerNum").equals(oldEnt.get("playerNum")) && allEnt.get("ownerID").equals(oldEnt.get("ownerID")))
								oldEntExists = true;
						}
						if (!oldEntExists) {
							if (allEnt.get("playerNum").equals(playerNum)) {
								synchronized (GameScreen.world) {
									if (allEnt.get("type").equals("player")) {
										Factory.createPlayer((Float) allEnt.get("xPos"), (Float) allEnt.get("yPos"), (Integer)allEnt.get("playerNum"), (Long) allEnt.get("ownerID"));
									}
									else if (allEnt.get("type").equals("bullet")) {
										Factory.createBullet((Float) allEnt.get("xPos"), (Float) allEnt.get("yPos"), (Float) allEnt.get("xVel"), (Float) allEnt.get("yVel"), (Integer)allEnt.get("playerNum"), (Long) allEnt.get("ownerID"));
									}
									else if (allEnt.get("type").equals("seal")) {
										Factory.createSeal((Float) allEnt.get("xPos"), (Float) allEnt.get("yPos"), (Integer)allEnt.get("playerNum"), (Long) allEnt.get("ownerID"));
									}
								}
							}
						}
					}
					
					//Run NetworkSystem here
					GameScreen.networkSystem.update(Gdx.graphics.getDeltaTime());
				}
				else {
					System.out.println("Server receiving other datatype from " + socket.getRemoteAddress());
				}
			}
			catch(SocketException e)
			{
				//Handle disconnect
				networkHost.removePlayer(socket);
			}
			catch(Exception e)
			{
				System.out.println("Exception in NetworkHostUpdateHandler reading ois: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
