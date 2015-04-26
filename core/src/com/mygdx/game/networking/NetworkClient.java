package com.mygdx.game.networking;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.mygdx.game.GameScreen;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.dungeon.DungeonGenerator;
import com.mygdx.game.utility.Factory;
import com.mygdx.game.utility.RandomInt;

public class NetworkClient extends Thread {
	public GameScreen gScreen;
	
	ObjectInputStream ois;
	ObjectOutputStream oos;
	
	public NetworkClient(GameScreen gScreen) {
		this.gScreen = gScreen;
		
		System.out.println("CLIENT");
		SocketHints socketHints = new SocketHints();
		socketHints.connectTimeout = 10000; //10s?

		Socket socket = Gdx.net.newClientSocket(Protocol.TCP, gScreen.ip, gScreen.port, socketHints);
		
		System.out.println("CONNECTED");
		try 
		{
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} 
		catch (Exception e) 
		{
			System.out.println("Exception in client code:" + e.getMessage());
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
				
				gScreen.networkPlayerNum = (Integer)((HashMap)o).get("playerNum");
				long mapSeed = (Long)((HashMap)o).get("mapSeed");
				RandomInt.setSeed(mapSeed);
				DungeonGenerator.generateDungeon(gScreen);
				
				HashSet<HashMap<String, Object>> sendData = (HashSet<HashMap<String, Object>>) GameScreen.myEntities.clone();
				oos.writeObject(sendData);
				oos.flush();
				oos.reset();
				
				gScreen.initialized = true;
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void run() {
		while (true) {
			try {
				Object o = null;
				try {
					o = ois.readObject();
				}
				catch (Exception e) {System.out.println(e.getMessage());}

				if (o.getClass() == HashSet.class) {
					System.out.println("Receiving HashSet");
					
					for (HashMap<String, Object> entity : (HashSet<HashMap<String, Object>>)o) {
						if (GameScreen.allEntities.contains(entity)) {
							//Update the entity
						}
						else {
							//Create the entity
							if (entity.get("type").equals("player")) {
								//Factory.createPlayer(((Float) entity.get("xPos")).intValue(), ((Float) entity.get("yPos")).intValue());
								Factory.createBullet((Float) entity.get("xPos"), (Float) entity.get("yPos"), 0f, 0f);
							}
						}
						GameScreen.allEntities.remove(entity);
						GameScreen.allEntities.add(entity);
					}
					
					HashSet<HashMap<String, Object>> sendData = (HashSet<HashMap<String, Object>>) GameScreen.myEntities.clone();
					oos.writeObject(sendData);
					oos.flush();
					oos.reset();
				}
				else if (o.getClass() == String.class) {
					System.out.println("Receiving string: " + (String)o);
				}
				else {
					System.out.println("Receiving other datatype:" + o.toString());
				}
			}
			catch (Exception e) {
				System.out.println("Exception in client code:" + e.getMessage());
				e.printStackTrace();
			};
		}
	}
}
