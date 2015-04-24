package com.mygdx.game.networking;

import java.io.ObjectOutputStream;

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
			
			networkHost.sockets.add(socket);
			
			//BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream())); //dont need anything from the other player yet
			System.out.println("connected");
			//Send the current state of the game as a starting point
			System.out.println("Sending initial game state");
			try
			{
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(networkHost.mapSeed);
				oos.flush();
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
