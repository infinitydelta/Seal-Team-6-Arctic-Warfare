package com.mygdx.game.networking;

import java.io.ObjectInputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.mygdx.game.GameScreen;
import com.mygdx.game.dungeon.DungeonGenerator;
import com.mygdx.game.utility.Factory;
import com.mygdx.game.utility.RandomInt;

public class NetworkClientInput extends Thread {
	public GameScreen gScreen;
	
	Socket socket;
	ObjectInputStream ois;
	
	public NetworkClientInput(GameScreen gScreen, Socket socket) {
		this.gScreen = gScreen;
		this.socket = socket;
		
		
		try 
		{
			ois = new ObjectInputStream(socket.getInputStream());
		} 
		catch (Exception e) 
		{
			System.out.println("Exception in client input code preinitialization:" + e.getMessage());
			e.printStackTrace();
		}
		initialize();
		start();
	}
	
	public void initialize() {
		try {
			Object o = null;
			o = ois.readObject();
			if (o.getClass() == ConcurrentHashMap.class) {
				System.out.println("Receiving Hashmap");
				
				GameScreen.networkPlayerNum = (Integer)((ConcurrentHashMap<String, Object>)o).get("playerNum");
				long mapSeed = (Long)((ConcurrentHashMap<String, Object>)o).get("mapSeed");
				RandomInt.setSeed(mapSeed);
				DungeonGenerator.generateDungeon(gScreen);
				
				GameScreen.initialized = true;
			}
		}
		catch (Exception e) {
			System.out.println("Exception in client input code initialization:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (true) {
			try {
				Object o = ois.readObject();
				if (o.getClass() == CopyOnWriteArraySet.class) {
					//System.out.println("Receiving (" + ((CopyOnWriteArraySet<HashMap<String, Object>>)o).size() + "):" + o.toString());
					
					CopyOnWriteArraySet<ConcurrentHashMap<String, Object>> oCasted = ((CopyOnWriteArraySet<ConcurrentHashMap<String, Object>>)o);
					
					//Remove any entities that arent mine from allEntities, and clone all of these entities to oldEntities
					CopyOnWriteArraySet<ConcurrentHashMap<String, Object>> oldEntities = new CopyOnWriteArraySet<ConcurrentHashMap<String, Object>>();
					for(ConcurrentHashMap<String, Object> allEnt : GameScreen.allEntities ) {
						if (!(allEnt.get("playerNum")).equals(GameScreen.networkPlayerNum)) {
							ConcurrentHashMap<String, Object> allEntClone = allEnt;
							oldEntities.add(allEntClone);
							GameScreen.allEntities.remove(allEnt);
						}
					}
					
					//Add all incoming entities that arent mine to allEntities
					for(ConcurrentHashMap<String, Object> incEnt : oCasted ) {
						if (!(incEnt.get("playerNum")).equals(GameScreen.networkPlayerNum))
							GameScreen.allEntities.add(incEnt);
					}
					
					//Create an instance of every allEntity that is not in oldEntity but doesnt belong to me
					for(ConcurrentHashMap<String, Object> allEnt : GameScreen.allEntities ) {
						boolean oldEntExists = false;
						for(ConcurrentHashMap<String, Object> oldEnt : oldEntities ) {
							if (allEnt.get("playerNum").equals(oldEnt.get("playerNum")) && allEnt.get("ownerID").equals(oldEnt.get("ownerID")))
								oldEntExists = true;
						}
						if (!oldEntExists) {
							if (!allEnt.get("playerNum").equals(GameScreen.networkPlayerNum)) {
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
					System.out.println("Client receiving other datatype:" + o.toString());
				}
			}
			catch (Exception e) {
				System.out.println("Exception in client code run:" + e.getMessage());
				e.printStackTrace();
			};
		}
	}
}
