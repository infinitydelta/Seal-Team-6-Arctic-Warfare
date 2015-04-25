package com.mygdx.game.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.net.Socket;

public class NetworkHostUpdateHandler extends Thread {
	NetworkHost networkHost;
	Socket socket;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	
	public ConcurrentHashMap<Long, HashMap<String, Object>> entities;
	public long pollingRate = 200;
	
	public NetworkHostUpdateHandler(NetworkHost networkHost, Socket socket) {
		this.networkHost = networkHost;
		this.socket = socket;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
				Object o = ois.readObject();
				if (o.getClass() == String.class) {
					if ((String)o == "Ready") {
						System.out.println("Sending data to " + socket.getRemoteAddress());
						oos.writeObject("test");
						oos.flush();
						oos.reset();
					}
					System.out.println("Receving string from " + socket.getRemoteAddress() + ":" + (String)o);
				}
				else {
					System.out.println("Receving other datatype from " + socket.getRemoteAddress());
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
