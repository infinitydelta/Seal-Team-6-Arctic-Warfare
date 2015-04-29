package com.mygdx.game.networking;

import java.io.ObjectInputStream;
import java.util.HashMap;
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
			if (o.getClass() == HashMap.class) {
				System.out.println("Receiving Hashmap");
				
				GameScreen.networkPlayerNum = (Integer)((HashMap<String, Object>)o).get("playerNum");
				long mapSeed = (Long)((HashMap<String, Object>)o).get("mapSeed");
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
				Object o = null;
				try {
					o = ois.readObject();
				}
				catch (Exception e) {System.out.println("Exception in NetworkClient line 87:" + e.getMessage());}

				if (o.getClass() == CopyOnWriteArraySet.class) {
					//System.out.println("Receiving (" + ((CopyOnWriteArraySet<HashMap<String, Object>>)o).size() + "):" + o.toString());
					
					for (HashMap<String, Object> entity : (CopyOnWriteArraySet<HashMap<String, Object>>)o) {
						boolean entityExists = false;
						for (HashMap<String, Object> entity2 : GameScreen.allEntities) {
		            		if (entity2.get("playerNum").equals(entity.get("playerNum")) && entity2.get("ownerID").equals(entity.get("ownerID"))) {
		            			//Entity received exists in allEntries, so replace its values
		            			GameScreen.allEntities.remove(entity2);
		            			GameScreen.allEntities.add(entity);
		            			entityExists = true;
		            		}
		            	}
						
						if (!entityExists) {
							//Create the entity
							synchronized (GameScreen.world) {
								if (entity.get("type").equals("player")) {
									Factory.createPlayer((Float) entity.get("xPos"), (Float) entity.get("yPos"), (Integer)entity.get("playerNum"), (Long) entity.get("ownerID"));
								}
								else if (entity.get("type").equals("bullet")) {
									//System.out.println("Creating Bullet");
									Factory.createBullet((Float) entity.get("xPos"), (Float) entity.get("yPos"), (Float) entity.get("xVel"), (Float) entity.get("yVel"), (Integer)entity.get("playerNum"), (Long) entity.get("ownerID"));
								}
								else if (entity.get("type").equals("seal")) {
									Factory.createSeal((Float) entity.get("xPos"), (Float) entity.get("yPos"), (Integer)entity.get("playerNum"), (Long) entity.get("ownerID"));
								}
								else if (entity.get("type").equals("enemybullet")) {
									Factory.createEnemyBullet((Float) entity.get("xPos"), (Float) entity.get("yPos"), (Float) entity.get("xVel"), (Float) entity.get("yVel"), (Integer)entity.get("playerNum"), (Long) entity.get("ownerID"));
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
