package com.mygdx.game.networking;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.mygdx.game.GameScreen;
import com.mygdx.game.dungeon.DungeonGenerator;
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
		start();
	}
	
	public void run() {
		while (true) {
			try {
				Object o = null;
				while (o == null) {
					try {
						o = ois.readObject();
					}
					catch (Exception e) {System.out.println(e.getMessage());}
				}
				System.out.println("Receiving data");
				oos.writeObject("Ready");
				oos.flush();
				oos.reset();
				if (o.getClass() == Long.class) {
					System.out.println("Receiving Long");
					long mapSeed = (Long) o;
					RandomInt.setSeed(mapSeed);
					DungeonGenerator.generateDungeon(gScreen);
				}
				else if (o.getClass() == HashMap.class) {
					System.out.println("Receiving hashmap");
					for (Map.Entry<Long, Map<String, Object>> entry : ((ConcurrentHashMap<Long, Map<String, Object>>) o).entrySet()) {
					}
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
