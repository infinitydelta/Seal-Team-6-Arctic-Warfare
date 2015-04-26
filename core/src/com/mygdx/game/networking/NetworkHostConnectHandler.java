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
			
			//BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream())); //dont need anything from the other player yet
			System.out.println("connected");
			//Send the current state of the game as a starting point
			System.out.println("Sending initial game state");
			try
			{
				//ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(networkHost.mapSeed);
				oos.flush();
				oos.reset();
				NetworkHostUpdateHandler networkHostUpdateHandler = new NetworkHostUpdateHandler(networkHost, socket, oos);
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
