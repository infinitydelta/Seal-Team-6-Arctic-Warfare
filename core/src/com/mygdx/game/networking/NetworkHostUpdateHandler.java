package com.mygdx.game.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.net.Socket;

public class NetworkHostUpdateHandler extends Thread {
	NetworkHost networkHost;
	Socket socket;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	
	public HashSet<HashMap<String, Object>> entities;
	
	public NetworkHostUpdateHandler(NetworkHost networkHost, Socket socket, ObjectOutputStream oos) {
		this.networkHost = networkHost;
		this.socket = socket;
		try {
			this.ois = new ObjectInputStream(socket.getInputStream());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		};
		this.oos = oos;
		
		entities = new HashSet<HashMap<String, Object>>();
		
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
				if (o.getClass() == String.class) {
					if (((String)o).equals("Ready")) {
						//System.out.println("Sending data to " + socket.getRemoteAddress());
						oos.writeObject(networkHost.entities);
						oos.flush();
						oos.reset();
					}
					//System.out.println("Receiving string from " + socket.getRemoteAddress() + ":" + (String)o);
				}
				else {
					//System.out.println("Receiving other datatype from " + socket.getRemoteAddress());
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
