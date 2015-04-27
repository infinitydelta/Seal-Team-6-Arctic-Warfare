package com.mygdx.game.networking;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.mygdx.game.GameScreen;
import com.mygdx.game.utility.Factory;

public class NetworkHostUpdateHandler extends Thread {
	NetworkHost networkHost;
	Socket socket;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	
	public NetworkHostUpdateHandler(NetworkHost networkHost, Socket socket, ObjectOutputStream oos) {
		this.networkHost = networkHost;
		this.socket = socket;
		try {
			this.ois = new ObjectInputStream(socket.getInputStream());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		this.oos = oos;
		
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
									Factory.createBullet((Float) entity.get("xPos"), (Float) entity.get("yPos"), (Float) entity.get("xVel"), (Float) entity.get("yVel"), (Integer)entity.get("playerNum"), (Long) entity.get("ownerID"));
								}
								else if (entity.get("type").equals("seal")) {
									Factory.createSeal((Float) entity.get("xPos"), (Float) entity.get("yPos"), (Integer)entity.get("playerNum"), (Long) entity.get("ownerID"));
								}
							}
						}
					}
					//System.out.println(GameScreen.allEntities);
					oos.writeObject(GameScreen.allEntities);
					oos.flush();
					oos.reset();
					
					//Run NetworkSystem here
					GameScreen.networkSystem.update(Gdx.graphics.getDeltaTime());
				}
				else if (o.getClass() == String.class) {
					GameScreen.networkSystem.update(Gdx.graphics.getDeltaTime());
					
					System.out.println(GameScreen.allEntities);
					oos.writeObject(GameScreen.allEntities);
					oos.flush();
					oos.reset();
				}
				else {
					//System.out.println("Receiving other datatype from " + socket.getRemoteAddress());
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