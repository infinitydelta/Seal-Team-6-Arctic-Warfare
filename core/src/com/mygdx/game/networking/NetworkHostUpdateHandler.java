package com.mygdx.game.networking;

import java.io.ObjectOutputStream;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.net.Socket;

public class NetworkHostUpdateHandler extends Thread {
	NetworkHost networkHost;
	
	public ConcurrentHashMap<Long, HashMap<String, Object>> entities;
	public long pollingRate = 1000;
	
	public NetworkHostUpdateHandler(NetworkHost networkHost) {
		this.networkHost = networkHost;
		
		entities = new ConcurrentHashMap<Long, HashMap<String, Object>>();
		
		start();
	}
	
	public void run()
	{	
		//FOREVER
		while(true)
		{
			try
			{
				sleep(pollingRate);
				for (Map.Entry<Socket, ObjectOutputStream> client : networkHost.clients.entrySet()) {
					client.getValue().writeObject(entities);
					client.getValue().flush();
					client.getValue().reset();
				}
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
