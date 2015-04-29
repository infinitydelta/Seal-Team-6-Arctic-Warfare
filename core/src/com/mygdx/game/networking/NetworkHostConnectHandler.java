package com.mygdx.game.networking;

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
			
			//NetworkHostPingpong networkHostPingpong = new NetworkHostPingpong(networkHost, socket);
			NetworkHostOutput netHosOut = new NetworkHostOutput(networkHost, socket);
			NetworkHostInput netHosInp = new NetworkHostInput(networkHost, socket);
		}
	}
}