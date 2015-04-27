package com.mygdx.game.networking;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;

public class NetworkHostConnectHandler extends Thread {
	NetworkHost networkHost;
	
	public NetworkHostConnectHandler(NetworkHost networkHost) {
		this.networkHost = networkHost;
		start();
	}
	
	public void run()
	{	
		//FOREVER
		while(true)
		{
			System.out.println("Waiting...");
			Socket socket = networkHost.serverSocket.accept(null);
			
			System.out.println("connected");
			//Send the current state of the game as a starting point
			System.out.println("Sending initial game state");
			try
			{
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				HashMap<String, Object> initializationData = new HashMap<String, Object>();
				initializationData.put("playerNum", networkHost.addPlayer(socket));
				initializationData.put("mapSeed", networkHost.mapSeed);
				
				oos.writeObject(initializationData);
				oos.flush();
				oos.reset();
				NetworkHostUpdateHandler networkHostUpdateHandler = new NetworkHostUpdateHandler(networkHost, socket, oos);
			}
			catch(Exception e)
			{
				System.out.println("network host connection handler: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}